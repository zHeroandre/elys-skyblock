package me.zheroandre.elysskyblock.builder.inventory;

import me.zheroandre.elysskyblock.builder.item.SkullGUI;
import me.zheroandre.elysskyblock.utils.string.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ElysInventory implements InventoryHolder {

    private final Map<Integer, SkullGUI> itemsMap = new HashMap<>();
    private final Inventory inventory;

    public ElysInventory(InventoryType inventoryType, String title) {
        inventory = Bukkit.createInventory(this, inventoryType, StringUtils.color(title));
    }

    public ElysInventory(int slot, String title) {
        inventory = Bukkit.createInventory(this, slot, StringUtils.color(title));
    }

    public ElysInventory fill(ItemStack itemStack) {
        for (int i = 0; i < inventory.getSize(); i++) inventory.setItem(i, itemStack);

        return this;
    }

    public ElysInventory set(int slot, SkullGUI skullGUI) {
        inventory.setItem(slot, skullGUI.build());
        itemsMap.put(slot, skullGUI);

        return this;
    }

    public SkullGUI get(int slot) {
        return itemsMap.get(slot);
    }

    public void refresh() {
        itemsMap.forEach((integer, skullGUI) -> {
            inventory.setItem(integer, skullGUI.build());
        });
    }

    @Override @NotNull
    public Inventory getInventory() {
        return inventory;
    }

}
