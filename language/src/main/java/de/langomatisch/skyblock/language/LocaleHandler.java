package de.langomatisch.skyblock.language;

import com.google.common.util.concurrent.ListenableFuture;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;

public interface LocaleHandler {

    LanguageHandler getLanguageHandler(File path);

    Locale getLocale(UUID uuid);

    Locale getLocale(Player player);

    ListenableFuture<Locale> getLocaleAsync(UUID uuid);

    ListenableFuture<Locale> getLocaleAsync(Player player);

}
