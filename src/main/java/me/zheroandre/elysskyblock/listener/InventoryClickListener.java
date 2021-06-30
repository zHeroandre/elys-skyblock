package me.zheroandre.elysskyblock.listener;

import me.zheroandre.elysskyblock.ElysSkyBlockPlugin;
import me.zheroandre.elysskyblock.builder.inventory.ElysInventory;
import me.zheroandre.elysskyblock.builder.item.SkullGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {

    public InventoryClickListener(ElysSkyBlockPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent e) {
        ItemStack itemStack = e.getCurrentItem();

        Inventory inventory = e.getClickedInventory();
        Inventory openInventory = e.getWhoClicked().getOpenInventory().getTopInventory();

        if (itemStack == null || inventory == null) {
            e.setCancelled(true);
            return;
        }

        if (inventory.getType() == InventoryType.PLAYER && openInventory.getHolder() instanceof ElysInventory) {
            e.setCancelled(true);
            return;
        }

        if (!(inventory.getHolder() instanceof ElysInventory)) return;
        ElysInventory elysInventory = (ElysInventory) inventory.getHolder();
        SkullGUI skullGUI = elysInventory.get(e.getSlot());

        if (skullGUI == null) {
            e.setCancelled(true);
            return;
        }

        skullGUI.write(e);
    }

}
