package de.korzhorz.signs.subserver.util.data;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryData {
    private final Player player;
    private final ItemStack[] inventory;
    private final ItemStack[] armor;
    private final GameMode gameMode;
    private final float exp;
    private final int level;
    
    public InventoryData(Player player) {
        this.player = player;
        this.inventory = player.getInventory().getContents();
        this.armor = player.getInventory().getArmorContents();
        this.gameMode = player.getGameMode();
        this.exp = player.getExp();
        this.level = player.getLevel();
    }
    
    public void restore() {
        this.player.getInventory().setContents(this.inventory);
        this.player.getInventory().setArmorContents(this.armor);
        this.player.setGameMode(this.gameMode);
        this.player.setExp(this.exp);
        this.player.setLevel(this.level);
    }
}
