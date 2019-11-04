package de.langomatisch.skyblock.language;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Locale {

    private Map<String, String> messageMap = new HashMap<>();

    public Locale(File file) {
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        Map<String, Object> values = yamlConfiguration.getValues(true);
        values.forEach((s, o) -> {
            if (o instanceof String) {
                System.out.println("[Language] loaded " + s + ": " + o);
                messageMap.put(s, (String) o);
            }
        });
    }

    public String getMessage(String key, Object... replacements) {
        return String.format(ChatColor.translateAlternateColorCodes('&', messageMap.getOrDefault(key, "N/A (" + key + ")")), replacements);
    }

}
