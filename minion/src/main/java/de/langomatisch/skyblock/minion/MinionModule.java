package de.langomatisch.skyblock.minion;

import de.langomatisch.skyblock.minion.command.TestCommand;
import de.mcgregordev.kiara.core.module.Module;

public class MinionModule extends Module {

    @Override
    public void onEnable() {
        registerCommand(new TestCommand());
    }

    @Override
    public void onDisable() {

    }

}
