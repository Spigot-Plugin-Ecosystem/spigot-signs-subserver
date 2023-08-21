package de.korzhorz.template.handlers;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

public class ItemHandler {
    public static ItemStack getItem(String material, int amount, String displayName) {
        ItemStack itemStack = new ItemStack(Objects.requireNonNull(Material.matchMaterial(material)), amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getItem(String material, int amount, String displayName, List<String> lore) {
        ItemStack itemStack = new ItemStack(Objects.requireNonNull(Material.matchMaterial(material)), amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
