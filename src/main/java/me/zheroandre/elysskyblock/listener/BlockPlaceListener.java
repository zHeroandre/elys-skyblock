package me.zheroandre.elysskyblock.listener;

import me.zheroandre.elysskyblock.ElysSkyBlockPlugin;
import me.zheroandre.elysskyblock.enums.island.IslandPermission;
import me.zheroandre.elysskyblock.handler.island.IslandHandler;
import me.zheroandre.elysskyblock.objects.island.EIsland;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Objects;

public class BlockPlaceListener implements Listener {

    private final IslandHandler islandHandler;

    public BlockPlaceListener(ElysSkyBlockPlugin plugin) {
        this.islandHandler = plugin.getIslandHandler();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void blockPlaceEvent(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        World world = e.getBlock().getWorld();
        Block block = e.getBlock();

        /*
         * CATEGORY ( ISLAND )
         */
        if (Objects.equals(world, islandHandler.islandWorld)) {
            if (islandHandler.has(block.getLocation())) {
                EIsland island = islandHandler.get(block.getLocation());
                if (island.isMember(player.getUniqueId()) && island.hasPermission(player.getUniqueId(), IslandPermission.ISLAND_PLACE)) return;

                e.setCancelled(true);
                return;
            }

            if (islandHandler.has(block)) {
                e.setCancelled(true);

                EIsland island = islandHandler.get(block);
                if (island.isMember(player.getUniqueId())) {
                    // TODO
                    return;
                }

                // TODO
            }

            e.setCancelled(true);
        }

    }

}
