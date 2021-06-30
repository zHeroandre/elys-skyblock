package me.zheroandre.elysskyblock.handler.spawn;

import lombok.Getter;
import lombok.Setter;
import me.zheroandre.elysskyblock.ElysSkyBlockPlugin;
import me.zheroandre.elysskyblock.utils.location.LocationUtils;
import org.bukkit.Location;

import java.util.Objects;

public class SpawnHandler {

    private final ElysSkyBlockPlugin plugin;

    @Getter Location location;
    @Getter private boolean setting;

    public SpawnHandler(ElysSkyBlockPlugin plugin) {
        this.plugin = plugin;

        if (plugin.getConfig().getString("spawn-location") == null) return;

        this.location = LocationUtils.deserialize(Objects.requireNonNull(plugin.getConfig().getString("spawn-location")));
        this.setting = true;
    }

    public void setLocation(Location location) {
        this.location = location;
        if (!setting) setting = true;

        plugin.getConfig().set("spawn-location", LocationUtils.serialize(location));
        plugin.saveConfig();
    }

}
