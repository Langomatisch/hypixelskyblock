package de.langomatisch.skyblock.minion.command;

import de.langomatisch.skyblock.minion.minion.BlockMinion;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand extends Command {

    public TestCommand() {
        super("test");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        Player player = (Player) commandSender;
        new BlockMinion(player.getLocation(), player.getItemInHand().getType(), 1, 1, 1);
        player.sendMessage("spawned");
        return false;
    }

}
