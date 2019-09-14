package de.langomatisch.skyblock.minion.minion;

import de.mcgregordev.kiara.core.item.ItemBuilder;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BlockMinion extends Minion {

    /***
     * 0 1 2
     * 3 4 5
     * 6 7 8
     */
    private List<Block> blocks = new ArrayList<>();

    public BlockMinion(Location location, Material material, int level, double speed, int storage) {
        super(location, material, level, speed, storage);
        setup();
    }

    @Override
    public void onTick() {
        //TODO: check field.
    }

    @Override
    public void setup() {
        Block block = getLocation().getBlock();

        for (int x = -2; x < 3; x++) {
            for (int z = -2; z < 3; z++) {
                Block relative = block.getRelative(x, -1, z);
                relative.setType(getMaterial());
                blocks.add(relative);
            }
        }
        spawnMinion();
    }

    @Override
    public void spawnMinion() {
        ArmorStand spawn = getLocation().getWorld().spawn(getLocation().getBlock().getLocation().add(0.5, 0, 0.5), ArmorStand.class);
        spawn.setSmall(true);
        spawn.setBasePlate(false);
        spawn.setHelmet(new ItemBuilder(Material.SKULL_ITEM).setHeadOwner("Langomatisch").build());
        setArmorStand(spawn);
    }

    @Override
    public void despawnMinion() {
        if (getArmorStand() != null) {
            getArmorStand().remove();
        }
    }

}
