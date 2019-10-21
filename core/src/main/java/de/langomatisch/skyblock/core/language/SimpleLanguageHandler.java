package de.langomatisch.skyblock.core.language;

import com.google.common.util.concurrent.ListenableFuture;
import de.langomatisch.skyblock.core.locale.Locale;
import de.langomatisch.skyblock.core.locale.LocaleHandler;
import de.langomatisch.skyblock.core.locale.SimpleLocaleHandler;
import de.langomatisch.skyblock.core.module.Module;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
public class SimpleLanguageHandler implements LanguageHandler {

    private LocaleHandler localeHandler;
    private Locale defaultLocale;
    private Map<String, Locale> localeMap = new HashMap<>();

    public SimpleLanguageHandler(Module module) {
        System.out.println("hi");
        localeHandler = new SimpleLocaleHandler(this);
        File dataFolder = module.getDataFolder();
        System.out.println(dataFolder.toString());
        File localeFolder = new File(dataFolder, "locales/");
        if(!localeFolder.exists()) {
            localeFolder.mkdir();
            try {
                new File(localeFolder, "en-EN.yml").createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(localeFolder.toString());
        for (File file : localeFolder.listFiles()) {
            if (file.isFile()) {
                String replace = file.getName().replace(".yml", "");
                Locale locale = new Locale(file);
                localeMap.put(replace, locale);
                if (replace.startsWith("en")) {
                    defaultLocale = locale;
                }
            }
        }
        System.out.println(String.format("loaded %s languages %s", localeMap.size(), Arrays.asList(localeMap.keySet().toArray())));
    }

    @Override
    public void sendMessage(Player player, String key, Object... replacements) {
        player.sendMessage(getMessage(player, key, replacements));
    }

    @Override
    public String getMessage(Player player, String key, Object... replacements) {
        return localeHandler.getLocale(player).getMessage(key, replacements);
    }

    @Override
    public ListenableFuture<String> getMessageAsync(Player player, String key, Object... replacements) {
        return null;
    }

    public void broadcastMessage(String key, Object... replacements) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            sendMessage(onlinePlayer, key, replacements);
        }
    }
}
