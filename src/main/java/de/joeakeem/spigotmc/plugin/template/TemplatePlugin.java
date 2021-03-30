package de.joeakeem.spigotmc.plugin.template;

import de.joeakeem.spigotmc.plugin.template.commands.*;
import de.joeakeem.spigotmc.plugin.template.event.PlayerChat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class TemplatePlugin extends JavaPlugin {
    private static Permission perms = null;

    @Override
    public void onEnable() {
        this.getCommand("reload").setExecutor(new EnrichCommand(this));
        getLogger().info("Monzter's Plugin has started!");
        setupPermissions();
        this.saveDefaultConfig();
        new PlayerChat(this);
    }

    private boolean setupPermissions(){
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    public static Permission getPermissions() {
        return perms;
    }

}