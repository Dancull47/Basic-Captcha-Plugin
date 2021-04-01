package monzter.captcha.plugin.event;

import monzter.captcha.plugin.Language;
import monzter.captcha.plugin.Captcha;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class PlayerChat implements Listener {
    private static final int CODE_LIMIT = Captcha.plugin.getConfig().getInt("Captcha-Length", 5);
    private HashMap<UUID, String> savedCode = new HashMap<>();
    private Random random = new Random();
    String stringMethod = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    String charsMethod = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String intsMethod = "1234567890";

    @EventHandler(priority = EventPriority.LOW)
    public void chat(AsyncPlayerChatEvent event){
        String configMethod = Captcha.plugin.getConfig().getString("Captcha-Type", "STRING");
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!Captcha.plugin.getPermissions().playerHas(player, Captcha.plugin.getConfig().getString("Given-Permission", "CAPTCHA.VERIFIED"))){
            switch(configMethod){
                case "INTS":
                    player.sendMessage(configMethod);
                    checker(uuid, player, event.getMessage(), "INTS");
                    event.setCancelled(true);
                    break;
                case "CHARS":
                    player.sendMessage(configMethod);
                    checker(uuid, player, event.getMessage(), "CHARS");
                    event.setCancelled(true);
                    break;
                default:
                    player.sendMessage(configMethod);
                    checker(uuid, player, event.getMessage(), "STRING");
                    event.setCancelled(true);
            }
        }
    }

    public String rng(UUID uuid, String type){
        StringBuilder sb = new StringBuilder();
        switch(type){
            case "INTS":
                for(int i = 0; i < CODE_LIMIT; i++) {
                    int index = random.nextInt(intsMethod.length());
                    char randomChar = intsMethod.charAt(index);
                    sb.append(randomChar);
                }
                String code = sb.toString();
                savedCode.put(uuid, code);
                return code;
            case "CHARS":
                for(int i = 0; i < CODE_LIMIT; i++) {
                    int index = random.nextInt(charsMethod.length());
                    char randomChar = charsMethod.charAt(index);
                    sb.append(randomChar);
                }
                code = sb.toString();
                savedCode.put(uuid, code);
                return code;
            default:
                for(int i = 0; i < CODE_LIMIT; i++) {
                    int index = random.nextInt(stringMethod.length());
                    char randomChar = stringMethod.charAt(index);
                    sb.append(randomChar);
                }
                code = sb.toString();
                savedCode.put(uuid, code);
                return code;
        }
    }
    public void incorrect(UUID uuid, Player player, String enteredCode, String type){
        player.sendMessage(Language.INCORRECT.toString().replace("%code%", enteredCode));
        String code = rng(uuid, type);
        player.sendMessage(Language.INCORRECT_RETRY.toString().replace("%code%", code));
    }
    public void correct(UUID uuid, Player player){
        player.sendMessage(Language.CORRECT.toString());
        permission(player);
    }
    public void checker(UUID uuid, Player player, String code, String type){
        if (!code.equals(savedCode.get(uuid))){
            savedCode.remove(uuid);
            incorrect(uuid, player, code, type);
        }   else{correct(uuid, player);}
    }
    public void permission(Player player){
        Captcha.plugin.getPermissions().playerAdd(player, Captcha.plugin.getConfig().getString("Given-Permission", "CAPTCHA.VERIFIED"));
    }
}
