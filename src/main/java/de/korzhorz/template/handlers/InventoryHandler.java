package de.korzhorz.template.handlers;

import org.bukkit.entity.Player;

public class InventoryHandler {
    public static void clearInventory(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setExp(0);
        player.setLevel(0);
    }
}
