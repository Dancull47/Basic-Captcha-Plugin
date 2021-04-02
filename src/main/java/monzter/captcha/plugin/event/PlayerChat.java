package monzter.captcha.plugin.event;

import monzter.captcha.plugin.Language;
import monzter.captcha.plugin.Captcha;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

public class PlayerChat implements Listener {
    private HashMap<UUID, String> savedCode = new HashMap<>();
    private Random random = new Random();
    String stringMethod = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    String charsMethod = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String intsMethod = "1234567890";
    private final Captcha plugin;
    private final int codeLimit;
    private final String permission;
    private final String configMethod;
    private final Set<UUID> whitelist = new CopyOnWriteArraySet<>();

    public PlayerChat(Captcha plugin) {
        this.plugin = plugin;
        codeLimit = plugin.getConfig().getInt("Captcha-Length", 5);
        permission = plugin.getConfig().getString("Given-Permission", "CAPTCHA.VERIFIED");
        String configMethod = plugin.getConfig().getString("Captcha-Type", "STRING");
        if (!"STRING".equals(configMethod) && !"INTS".equals(configMethod) && !"CHARS".equals(configMethod)) {
            configMethod = "STRING";
            plugin.getLogger().log(Level.WARNING, ChatColor.RED + "The config method you're using " + configMethod + " is invalid!");
        }
        this.configMethod = configMethod;


    }

    public boolean permissionCheck(Player player) {
        if (whitelist.contains(player.getUniqueId())) {
            return true;
        }
        boolean hasPermission;
        try {
            if (Bukkit.isPrimaryThread()) {
                hasPermission = plugin.getPermissions().playerHas(player, permission);
            } else {
                hasPermission = Bukkit.getScheduler().callSyncMethod(plugin, () -> plugin.getPermissions().playerHas(player, permission)).get();
            }

        } catch (ExecutionException | InterruptedException e) {
            plugin.getLogger().log(Level.WARNING, Language.TITLE.toString() + ChatColor.RED + "Permission check failed." + "\n"
                    + Language.TITLE + ChatColor.RED + "Report this stack trace to Monzter#4951 on Discord!", e);
            return false;
        }

        if (hasPermission) {
            whitelist.add(player.getUniqueId());
        }
        return hasPermission;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void chat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!permissionCheck(player)) {
            event.setCancelled(true);
            checker(uuid, player, event.getMessage(), configMethod);
        }
    }

    public String rng(UUID uuid, String type) {
        StringBuilder sb = new StringBuilder();
        switch (type) {
            case "INTS":
                for (int i = 0; i < codeLimit; i++) {
                    int index = random.nextInt(intsMethod.length());
                    char randomChar = intsMethod.charAt(index);
                    sb.append(randomChar);
                }
                String code = sb.toString();
                savedCode.put(uuid, code);
                return code;
            case "CHARS":
                for (int i = 0; i < codeLimit; i++) {
                    int index = random.nextInt(charsMethod.length());
                    char randomChar = charsMethod.charAt(index);
                    sb.append(randomChar);
                }
                code = sb.toString();
                savedCode.put(uuid, code);
                return code;
            default:
                for (int i = 0; i < codeLimit; i++) {
                    int index = random.nextInt(stringMethod.length());
                    char randomChar = stringMethod.charAt(index);
                    sb.append(randomChar);
                }
                code = sb.toString();
                savedCode.put(uuid, code);
                return code;
        }
    }

    public void incorrect(UUID uuid, Player player, String enteredCode, String type) {
        player.sendMessage(Language.INCORRECT.toString().replace("%code%", enteredCode));
        String code = rng(uuid, type);
        player.sendMessage(Language.INCORRECT_RETRY.toString().replace("%code%", code));
    }

    public void correct(UUID uuid, Player player) {
        player.sendMessage(Language.CORRECT.toString());
        permission(player);
    }

    public void checker(UUID uuid, Player player, String code, String type) {
        if (!code.equals(savedCode.get(uuid))) {
            savedCode.remove(uuid);
            incorrect(uuid, player, code, type);
        } else {
            correct(uuid, player);
        }
    }

    public void permission(Player player) {
        plugin.getPermissions().playerAdd(player, permission);
    }
}
