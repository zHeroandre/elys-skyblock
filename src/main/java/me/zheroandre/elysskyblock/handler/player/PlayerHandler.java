package me.zheroandre.elysskyblock.handler.player;

import lombok.Getter;
import me.zheroandre.elysskyblock.ElysSkyBlockPlugin;
import me.zheroandre.elysskyblock.enums.player.PlayerSetting;
import me.zheroandre.elysskyblock.objects.island.EIsland;
import me.zheroandre.elysskyblock.objects.player.EPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerHandler {

    private final ElysSkyBlockPlugin plugin;

    @Getter private final Map<UUID, EPlayer> playersMap = new HashMap<>();

    public PlayerHandler(ElysSkyBlockPlugin plugin) {
        this.plugin = plugin;
    }

    public void initialize(UUID playerUUID) {
        playersMap.put(playerUUID, new EPlayer(plugin, playerUUID));
    }

    public EPlayer initialize(UUID playerUUID, boolean islandChat, Map<PlayerSetting, Boolean> settingsMap, double money) {
        return playersMap.put(playerUUID, new EPlayer(plugin, playerUUID, islandChat, settingsMap, money));
    }

    public boolean has(Player player) {
        return playersMap.containsKey(player.getUniqueId());
    }

    public EPlayer get(Player player) {
        return playersMap.get(player.getUniqueId());
    }

    public void remove(UUID playerUUID) {
        playersMap.remove(playerUUID);
    }

}
