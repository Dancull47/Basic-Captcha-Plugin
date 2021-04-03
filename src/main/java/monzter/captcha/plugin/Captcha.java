package monzter.captcha.plugin;

import monzter.captcha.plugin.commands.Commands;
import monzter.captcha.plugin.event.PlayerChat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Captcha extends JavaPlugin implements Listener {
    private static Permission perms = null;
    Lang language = new Lang();

    @Override
    public void onEnable() {
        langStart();
        setupPermissions();
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerChat(this), this);
        getCommand("Captcha").setExecutor(new Commands(this));
        saveDefaultConfig();
        getLogger().info(ChatColor.GREEN + "has started!" + Lang.get().getString("title-name", "&c[&6Captcha&c] "));
    }

    @Override
    public void onDisable() {
        //getLogger().info(Language.TITLE.toString() + ChatColor.GREEN + "has shut down!");
    }


    private void setupPermissions(){
        perms = getServer().getServicesManager().getRegistration(Permission.class).getProvider();
    }

    public Permission getPermissions() {
        return perms;
    }

    public void langStart(){
        language.setup();
        language.get().options().copyDefaults(true);
        language.save();
    }
}