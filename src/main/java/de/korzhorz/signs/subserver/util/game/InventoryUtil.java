package de.korzhorz.signs.subserver.util.game;

import org.bukkit.entity.Player;

public class InventoryUtil {
    private InventoryUtil() {

    }

    public static void clearInventory(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setExp(0);
        player.setLevel(0);
    }
}
