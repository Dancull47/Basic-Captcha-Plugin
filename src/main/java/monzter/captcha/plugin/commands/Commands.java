package monzter.captcha.plugin.commands;

import monzter.captcha.plugin.Language;
import monzter.captcha.plugin.Captcha;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1){
            sender.sendMessage(ChatColor.GREEN + "Captcha Plugin, created by "
                    + ChatColor.GOLD + "Monzter" + ChatColor.GREEN + "!");
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")){
            if (sender.isOp() || sender.hasPermission(Captcha.plugin.getConfig().getString("Admin-Permission", "CAPTCHA.ADMIN"))){
                sender.sendMessage(Language.TITLE.toString() + ChatColor.GREEN + "The plugin has been reloaded!");
                Captcha.plugin.reloadConfig();
                Captcha.plugin.loadLang();
                return true;
            }
        }
        if (args[0].equalsIgnoreCase("reset")){
            if (sender.isOp() || sender.hasPermission(Captcha.plugin.getConfig().getString("Admin-Permission", "CAPTCHA.ADMIN"))){
                Player player = (Player) sender;
                if (args.length >= 2){
                    if (Bukkit.getPlayer(args[1]) != null){
                        Player target = Bukkit.getPlayer(args[1]);
                        Captcha.plugin.getPermissions().playerRemove(target, Captcha.plugin.getConfig().getString("Given-Permission", "CAPTCHA.VERIFIED"));
                        target.sendMessage(Language.TITLE.toString() + Language.RESET.toString().replace("%player%", target.getName()));
                    } else{
                        sender.sendMessage(Language.TITLE.toString() + Language.INVALID_PLAYER);
                    }
                } else {
                    Captcha.plugin.getPermissions().playerRemove(player, Captcha.plugin.getConfig().getString("Given-Permission", "CAPTCHA.VERIFIED"));
                    sender.sendMessage(Language.TITLE.toString() + Language.RESET.toString().replace("%player%", sender.getName()));
                }
                return true;
            }
        }

        return true;
    }
}

