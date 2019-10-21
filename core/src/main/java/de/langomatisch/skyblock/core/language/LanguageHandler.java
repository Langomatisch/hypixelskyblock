package de.langomatisch.skyblock.core.language;

import com.google.common.util.concurrent.ListenableFuture;
import org.bukkit.entity.Player;

public interface LanguageHandler {

    void sendMessage(Player player, String key, Object... replacements);

    String getMessage(Player player, String key, Object... replacements);

    ListenableFuture<String> getMessageAsync(Player player, String key, Object... replacements);

}
