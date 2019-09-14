package de.langomatisch.skyblock.minion.minion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.Inventory;

@Getter @Setter
@RequiredArgsConstructor
public abstract class Minion {

    private final Location location;
    private final Material material;
    private final int level;
    private final double speed;
    private final int storage;
    private ArmorStand armorStand;
    private Inventory inventory;

    /**
     * gets called every time the minion does a action
     */
    public abstract void onTick();

    /**
     * sets up the minion
     */
    public abstract void setup();

    /***
     * calls when the minion should spawn
     * usually on place or server start or world load etc
     */
    public abstract void spawnMinion();

    /***
     * calls when the minion should despawn
     * usually on destroy, world unload or server stop
     */
    public abstract void despawnMinion();

}
