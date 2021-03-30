package de.joeakeem.spigotmc.plugin.template.commands;

import de.joeakeem.spigotmc.plugin.template.TemplatePlugin;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

public class EnrichCommand implements CommandExecutor {
    public TemplatePlugin plugin;
    public EnrichCommand(TemplatePlugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()) {
            sender.sendMessage("Reloaded config!");
            sender.sendMessage(plugin.getConfig().getString("reload-message"));
            plugin.reloadConfig();
            return true;
        } else {return false;}
    }
}

