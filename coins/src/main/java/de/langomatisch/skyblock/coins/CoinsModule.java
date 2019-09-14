package de.langomatisch.skyblock.coins;

import de.langomatisch.skyblock.coins.command.CoinsCommand;
import de.langomatisch.skyblock.coins.command.SetCoinsCommand;
import de.langomatisch.skyblock.coins.provider.CoinsProvider;
import de.langomatisch.skyblock.core.CoreModule;
import de.langomatisch.skyblock.core.database.MySQLDatabase;
import de.mcgregordev.kiara.core.module.Module;
import lombok.Getter;

@Getter
public class CoinsModule extends Module {

    private CoreModule coreModule;
    private CoinsProvider coinsProvider;

    @Override
    public void onEnable() {
        this.coreModule = CoreModule.getInstance();
        registerCommand(new CoinsCommand(this));
        registerCommand(new SetCoinsCommand(this));
        coinsProvider = new CoinsProvider(new MySQLDatabase("localhost", "hypixel", "root", "", 3306));
    }

    @Override
    public void onDisable() {

    }

}
