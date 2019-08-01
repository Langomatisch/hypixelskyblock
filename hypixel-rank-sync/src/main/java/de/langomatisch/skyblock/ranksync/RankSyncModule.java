package de.langomatisch.skyblock.ranksync;

import de.langomatisch.skyblock.ranksync.listener.PlayerJoinListener;
import de.mcgregordev.kiara.core.module.Module;
import lombok.Getter;
import net.hypixel.api.HypixelAPI;

import java.util.UUID;

@Getter
public class RankSyncModule extends Module {

    @Getter
    private static RankSyncModule instance;
    private HypixelAPI hypixelAPI;

    @Override
    public void onEnable() {
        instance = this;
        hypixelAPI = new HypixelAPI(UUID.fromString(getConfig().getString("api-key")));
        registerListener(new PlayerJoinListener());
    }

    @Override
    public void onDisable() {

    }

}
