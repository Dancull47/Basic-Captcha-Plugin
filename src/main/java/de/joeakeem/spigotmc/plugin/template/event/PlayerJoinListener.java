package de.joeakeem.spigotmc.plugin.template.event;

import de.joeakeem.spigotmc.plugin.template.TemplatePlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.PermissionAttachment;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class PlayerJoinListener implements Listener {
    static TemplatePlugin plugin;
    private HashMap<UUID, String> savedCode = new HashMap<>();

    @EventHandler
    public void join(PlayerJoinEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (player.hasPermission("VERIFIED")){
            player.sendMessage("Hey, you're verified!");
        }   else {
            player.sendMessage("Hey, you're NOT verified!");
            String code = rng(uuid);
            player.sendMessage("Please type in the following code: " + code);
        }
    }
    @EventHandler
    public void chat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!player.hasPermission("VERIFIED")){
            checker(uuid, player, event.getMessage());
            event.setCancelled(true);
        }
    }

    public String rng(UUID uuid){
        Random random = new Random();
        int limit = 9;
        String code = random.nextInt(limit) +""+ random.nextInt(limit) +""+ random.nextInt(limit);
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
        plugin.getPermissions().playerAdd(player, "VERIFIED");
    }
}
