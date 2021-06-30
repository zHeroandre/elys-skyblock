package me.zheroandre.elysskyblock.listener;

import me.zheroandre.elysskyblock.ElysSkyBlockPlugin;
import me.zheroandre.elysskyblock.handler.db.IDBHandler;
import me.zheroandre.elysskyblock.handler.db.PDBHandler;
import me.zheroandre.elysskyblock.handler.island.IslandHandler;
import me.zheroandre.elysskyblock.handler.player.PlayerHandler;
import me.zheroandre.elysskyblock.handler.spawn.SpawnHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final SpawnHandler spawnHandler;
    private final IslandHandler islandHandler;

    private final PDBHandler pdbHandler;
    private final IDBHandler idbHandler;

    private final PlayerHandler playerHandler;

    public PlayerJoinListener(ElysSkyBlockPlugin plugin) {
        this.spawnHandler = plugin.getSpawnHandler();
        this.islandHandler = plugin.getIslandHandler();

        this.pdbHandler = plugin.getPdbHandler();
        this.idbHandler = plugin.getIdbHandler();

        this.playerHandler = plugin.getPlayerHandler();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        player.setOp(true);
        if (spawnHandler.isSetting()) player.teleport(spawnHandler.getLocation());

        if (!playerHandler.has(player)) {
            pdbHandler.present(player.getUniqueId()).whenComplete(
                    (uuid, throwable) -> {
                        if (throwable != null) throwable.printStackTrace();
                        if (uuid == null) {
                            playerHandler.initialize(player.getUniqueId());
                        } else {
                            pdbHandler.load(player.getUniqueId()).whenComplete(
                                    (ePlayer, throwable1) -> {
                                        if (throwable1 != null) throwable1.printStackTrace();
                                    }
                            );
                        }
                    }
            );
        }

        if (!islandHandler.has(player)) {
            idbHandler.has(player.getUniqueId()).whenComplete(
                    (identifiers, throwable) -> {
                        System.out.println(identifiers);
                        if (throwable != null) throwable.printStackTrace();
                        if (identifiers.size() == 0) return;

                        for (Integer identifier : identifiers) {
                            if (identifier != null)
                                idbHandler.load(identifier).whenComplete(
                                        (eIsland, throwable1) -> {
                                            if (throwable1 != null) throwable1.printStackTrace();
                                        }
                                );
                        }
                    }
            );
        }
    }

}
