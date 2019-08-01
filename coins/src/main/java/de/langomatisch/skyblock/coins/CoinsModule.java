package de.langomatisch.skyblock.coins;

import de.langomatisch.skyblock.coins.command.CoinsCommand;
import de.langomatisch.skyblock.coins.command.SetCoinsCommand;
import de.langomatisch.skyblock.coins.provider.CoinsProvider;
import de.mcgregordev.kiara.core.module.Module;
import lombok.Getter;

@Getter
public class CoinsModule extends Module {

    private CoinsProvider coinsProvider;

    @Override
    public void onEnable() {
        registerCommand(new CoinsCommand(this));
        registerCommand(new SetCoinsCommand(this));
        coinsProvider = new CoinsProvider(this);
    }

    @Override
    public void onDisable() {

    }

}
