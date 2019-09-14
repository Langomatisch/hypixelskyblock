package de.langomatisch.skyblock.core;

import de.mcgregordev.kiara.core.module.Module;
import lombok.Getter;

public class CoreModule extends Module {

    @Getter
    private static CoreModule instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {

    }

}
