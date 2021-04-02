package monzter.captcha.plugin;

import monzter.captcha.plugin.event.PlayerChat;
import monzter.captcha.plugin.commands.Commands;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

public class Captcha extends JavaPlugin implements Listener {
    private static Permission perms = null;
    public static YamlConfiguration LANGUAGE;
    public static File LANGUAGE_FILE;

    @Override
    public void onEnable() {
        loadLang();
        setupPermissions();
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerChat(this), this);
        getCommand("Captcha").setExecutor(new Commands(this));
        saveDefaultConfig();
        getLogger().info(Language.TITLE.toString() + ChatColor.GREEN + "has started!");
    }

    @Override
    public void onDisable() {
        getLogger().info(Language.TITLE.toString() + ChatColor.GREEN + "has shut down!");
    }


    private void setupPermissions(){
        perms = getServer().getServicesManager().getRegistration(Permission.class).getProvider();
    }

    public Permission getPermissions() {
        return perms;
    }


    public void loadLang() {
        File language = new File(getDataFolder(), "language.yml");
        if (!language.exists()) {
            try {
                getDataFolder().mkdir();
                language.createNewFile();
                InputStream defConfigStream = this.getResource("language.yml");
                if (defConfigStream != null) {
                    YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(language);
                    defConfig.save(language);
                    Language.setFile(defConfig);
                }
            } catch(IOException e) {
                getLogger().log(Level.SEVERE, Language.TITLE.toString() + ChatColor.RED + "Couldn't create language file." + "\n"
                        + Language.TITLE + ChatColor.RED + "This is a fatal error. Now disabling", e);
                this.setEnabled(false);
            }
        }
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(language);
        for(Language item:Language.values()) {
            if (conf.getString(item.getPath()) == null) {
                conf.set(item.getPath(), item.getDefault());
            }
        }
        Language.setFile(conf);
        this.LANGUAGE = conf;
        this.LANGUAGE_FILE = language;
        try {
            conf.save(getLangFile());
        } catch(IOException e) {
            getLogger().log(Level.WARNING, Language.TITLE.toString() + ChatColor.RED + "Failed to save lang.yml." + "\n"
                    + Language.TITLE + ChatColor.RED + "Report this stack trace to Monzter#4951 on Discord!", e);
        }
    }

    public YamlConfiguration getLanguage() {
        return LANGUAGE;
    }

    public File getLangFile() {
        return LANGUAGE_FILE;
    }

}