package de.langomatisch.skyblock.ranksync.rank;

import lombok.Getter;
import net.hypixel.api.HypixelAPI;

@Getter
public enum HypixelRank {

    OWNER(11, "§c[OWNER] ", "Owner"),
    ADMIN(10, "§c[ADMIN] ", "Admin"),
    MODERATOR(9, "§2[MOD] ", "Moderator"),
    HELPER(8, "§9[HELPER] ", "Helper"),
    JR_HELPER(7, "§9[JR HELPER]", "Jr Helper"),
    YOUTUBER(6, "§c[§fYOUTUBE§c] ", "Youtuber"),
    SUPERSTAR(5, "§6[MVP++] ", "MVP++"),
    MVP_PLUS(4, "§b[MVP+] ", "MVP+"),
    MVP(3, "§b[MVP] ", "MVP"),
    VIP_PLUS(2, "§a[VIP§6+§a] ", "VIP+"),
    VIP(1, "§a[VIP] ", "VIP"),
    DEFAULT(0, "§7", "Default");

    String prefix, name;
    int id;

    HypixelRank(int id, String prefix, String name) {
        this.id = id;
        this.prefix = prefix;
        this.name = name;
    }

    public static HypixelRank getRank(String rank) {
        for (HypixelRank value : values()) {
            if (value.name().equalsIgnoreCase(rank)) {
                return value;
            }
        }
        return DEFAULT;
    }

}
