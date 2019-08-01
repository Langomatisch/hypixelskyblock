package de.langomatisch.skyblock.coins.command;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import de.langomatisch.skyblock.coins.CoinsModule;
import de.mcgregordev.kiara.core.command.ModuleCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;
import java.util.UUID;

public class SetCoinsCommand extends Command {

    private CoinsModule module;

    public SetCoinsCommand(CoinsModule coinsModule) {
        super("setcoins");
        this.module = coinsModule;
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (strings.length < 2) {
            return false;
        }
        UUID uuid = UUID.fromString(strings[0]);
        int amount = Integer.parseInt(strings[1]);
        Futures.addCallback(module.getCoinsProvider().setCoins(uuid, amount), new FutureCallback<Void>() {
            @Override
            public void onSuccess(@Nullable Void aVoid) {
                module.getMessageStorage().getMessage("command.setcoins.success");
            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
                commandSender.sendMessage("§cEin Fehler ist aufgetreten: " + throwable.getMessage());
            }
        });
        return false;
    }
}
