package monzter.captcha.plugin;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Language {
    TITLE("title-name", "&c[&6Captcha&c] "),
    INVALID_PLAYER("invalid-player", "&cYou must specify a valid Player!"),

    RESET("reset", "&e%player% &ahas been reset, and will now require a captcha the next time they try to talk!"),
    CORRECT("correct", "&aThe code you entered was correct and you may now chat!"),
    INCORRECT("incorrect", "&cThe code you entered %code% was incorrect!"),
    INCORRECT_RETRY("incorrect-retry", "&ePlease type the following code: %code%"),

    NO_PERMS("no-permissions", "&cYou don''t have permission for that!");


    private String path;
    private String def;
    private static YamlConfiguration LANGUAGE;

    /**
     * Lang enum constructor.
     * @param path The string path.
     * @param start The default string.
     */
    Language(String path, String start) {
        this.path = path;
        this.def = start;
    }

    /**
     * Set the {@code YamlConfiguration} to use.
     * @param config The config to set.
     */
    public static void setFile(YamlConfiguration config) {
        LANGUAGE = config;
    }

    @Override
    public String toString() {
        if (this == TITLE)
            return ChatColor.translateAlternateColorCodes('&', LANGUAGE.getString(this.path, def)) + " ";
        return ChatColor.translateAlternateColorCodes('&', LANGUAGE.getString(this.path, def));
    }

    /**
     * Get the default value of the path.
     * @return The default value of the path.
     */
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

// Credit to https://bukkit.org/threads/language-files.149837/