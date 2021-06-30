package me.zheroandre.elysskyblock.listener;

import me.zheroandre.elysskyblock.ElysSkyBlockPlugin;
import me.zheroandre.elysskyblock.handler.island.IslandHandler;
import me.zheroandre.elysskyblock.handler.player.PlayerHandler;
import me.zheroandre.elysskyblock.objects.island.EIsland;
import me.zheroandre.elysskyblock.objects.player.EPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final PlayerHandler playerHandler;
    private final IslandHandler islandHandler;

    public PlayerQuitListener(ElysSkyBlockPlugin plugin) {
        this.playerHandler = plugin.getPlayerHandler();
        this.islandHandler = plugin.getIslandHandler();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void playerQuitEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        if (islandHandler.has(player)) {
            EIsland island = islandHandler.get(player);
            if (island.onlinePlayers().size() == 1) island.save();
        }

        if (playerHandler.has(player)) {
            EPlayer ePlayer = playerHandler.get(player);
            ePlayer.save(); // qui
        }
    }

}
