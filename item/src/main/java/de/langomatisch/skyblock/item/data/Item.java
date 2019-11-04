package de.langomatisch.skyblock.item.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Material;

import java.util.List;

@Data
@AllArgsConstructor
public class Item {

    private String key;
    private Material material;
    private String name;
    private List<String> lore;
    private ItemRarity itemRarity;

}
