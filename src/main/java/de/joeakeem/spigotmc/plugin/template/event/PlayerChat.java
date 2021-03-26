package de.joeakeem.spigotmc.plugin.template.event;

import de.joeakeem.spigotmc.plugin.template.TemplatePlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class PlayerChat implements Listener {
    static TemplatePlugin plugin;
    private static final String VERIFIED_PERMISSION = "VERIFIED";
    private static final int CODE_LIMIT = 5;
    private HashMap<UUID, String> savedCode = new HashMap<>();
    private Random random = new Random();
    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    @EventHandler(priority = EventPriority.LOW)
    public void chat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!plugin.getPermissions().playerHas(player, VERIFIED_PERMISSION)){
            checker(uuid, player, event.getMessage());
            event.setCancelled(true);
        }
    }

    public String rng(UUID uuid){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < CODE_LIMIT; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }
        String code = sb.toString().toLowerCase();
        savedCode.put(uuid, code);
        return code;
    }
    public void incorrect(UUID uuid, Player player, String enteredCode){
        player.sendMessage(ChatColor.RED + "The code you entered " + enteredCode + " was incorrect!");
        String code = rng(uuid);
        player.sendMessage(ChatColor.YELLOW + "Please type the following code: " + code);
    }
    public void correct(UUID uuid, Player player){
        player.sendMessage(ChatColor.GREEN + "The code you entered was correct and you may now chat!");
        permission(player);
    }
    public void checker(UUID uuid, Player player, String code){
        if (!code.equals(savedCode.get(uuid))){
            savedCode.remove(uuid);
            incorrect(uuid, player, code);
        }   else{correct(uuid, player);}
    }
    public void permission(Player player){
        plugin.getPermissions().playerAdd(player, VERIFIED_PERMISSION);
    }
}
