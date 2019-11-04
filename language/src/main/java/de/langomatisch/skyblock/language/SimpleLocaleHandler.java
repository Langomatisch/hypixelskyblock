package de.langomatisch.skyblock.language;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.Executors;

@Getter
public class SimpleLocaleHandler implements LocaleHandler {

    private ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

    private SimpleLanguageHandler languageHandler;

    public SimpleLocaleHandler(SimpleLanguageHandler languageHandler) {
        this.languageHandler = languageHandler;
    }

    @Override
    public LanguageHandler getLanguageHandler(File path) {
        return null;
    }

    @Override
    public Locale getLocale(UUID uuid) {
        return null;
    }

    @Override
    public Locale getLocale(Player player) {
        String locale = player.getLocale();
        System.out.println(player.getName()+": "+locale);
        return languageHandler.getLocaleMap().getOrDefault(locale, languageHandler.getDefaultLocale());
    }

    @Override
    public ListenableFuture<Locale> getLocaleAsync(UUID uuid) {
        return service.submit(() -> getLocale(uuid));
    }

    @Override
    public ListenableFuture<Locale> getLocaleAsync(Player player) {
        return service.submit(() -> getLocale(player));
    }

}
