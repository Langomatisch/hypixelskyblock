package de.langomatisch.skyblock.coins.command;

import de.langomatisch.skyblock.coins.CoinsModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

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

        return false;
    }
}
