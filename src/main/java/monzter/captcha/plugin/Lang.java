package monzter.captcha.plugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class Lang {
    private static File languageFile;
    private static FileConfiguration languageFileConfig;
    private static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Captcha");
    public String TITLE;
    private String path;
    private String def;

    Lang(String path, String start) {
        this.path = path;
        this.def = start;
    }

    public void setup() {
        languageFile = new File(plugin.getDataFolder(), "language.yml");
        if (!languageFile.exists()) {
            languageFile.getParentFile().mkdirs();
            plugin.saveResource("language.yml", false);
        }
        languageFileConfig = new YamlConfiguration();

        try {
            languageFileConfig.load(languageFile);
        } catch (IOException | InvalidConfigurationException e) {
            plugin.getLogger().log(Level.SEVERE, Language.TITLE.toString() + ChatColor.RED + "Config file is invalid!" + "\n"
                    + Language.TITLE + ChatColor.RED + "This is a fatal error. Now disabling", e);
        }
    }


    public static FileConfiguration get() {
        return languageFileConfig;
    }

    public void save() {
        try {
            languageFileConfig.save(languageFile);
        } catch (IOException e) {
            System.out.println("Couldn't save file");
        }
    }

    public void reload() {
        languageFileConfig = YamlConfiguration.loadConfiguration(languageFile);
    }

    @Override
    public String toString() {
        return ChatColor.translateAlternateColorCodes('&', Lang.get().getString(this.));
    }

    public String getDefault() {
        return this.def;
    }

    /**
     * Get the path to the string.
     * @return The path to the string.
     */
    public String getPath() {
        return this.path;
    }



}

