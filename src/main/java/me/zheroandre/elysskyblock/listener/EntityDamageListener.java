package me.zheroandre.elysskyblock.listener;

import me.zheroandre.elysskyblock.ElysSkyBlockPlugin;
import me.zheroandre.elysskyblock.handler.island.IslandHandler;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Objects;

public class EntityDamageListener implements Listener {

    private final IslandHandler islandHandler;

    public EntityDamageListener(ElysSkyBlockPlugin plugin) {
        this.islandHandler = plugin.getIslandHandler();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void entityDamageEvent(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        World world = e.getEntity().getWorld();
        EntityDamageEvent.DamageCause damageCause = e.getCause();

        if (!(entity instanceof Player)) return;
        Player player = (Player) entity;

        /*
         * CATEGORY ( ISLAND )
         */
        if (Objects.equals(world, islandHandler.islandWorld)) {
            // TODO
        }
    }

}
