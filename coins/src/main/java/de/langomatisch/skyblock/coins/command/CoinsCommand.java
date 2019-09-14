package de.langomatisch.skyblock.coins.command;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import de.langomatisch.skyblock.coins.CoinsModule;
import de.mcgregordev.kiara.core.storage.Variable;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public class CoinsCommand extends Command {

    private CoinsModule module;

    public CoinsCommand(CoinsModule coinsModule) {
        super("coins");
        this.module = coinsModule;
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        Futures.addCallback(module.getCoinsProvider().getCoins(((Player) commandSender).getUniqueId()), new FutureCallback<Integer>() {
            @Override
            public void onSuccess(@Nullable Integer integer) {
                System.out.println(integer);
                String message = module.getLanguageHandler().getMessage(((Player) commandSender).getLocale(), "command.coins.success", new Variable("coins", integer));
                commandSender.sendMessage(message);
                commandSender.sendMessage(String.format("§aDu hast §e%d Coins", integer));
            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
                String message = module.getLanguageHandler().getMessage(((Player) commandSender).getLocale(), "error");
                commandSender.sendMessage(message);
            }
        });
        return false;
    }

}
