package me.zheroandre.elysskyblock.handler.db;

import com.glyart.mystral.database.AsyncDatabase;
import com.glyart.mystral.database.Credentials;
import com.glyart.mystral.database.Mystral;
import com.google.common.reflect.TypeToken;
import me.zheroandre.elysskyblock.ElysSkyBlockPlugin;
import me.zheroandre.elysskyblock.enums.island.IslandRole;
import me.zheroandre.elysskyblock.enums.island.IslandSetting;
import me.zheroandre.elysskyblock.enums.island.IslandUpgrade;
import me.zheroandre.elysskyblock.objects.island.EIsland;
import me.zheroandre.elysskyblock.utils.gson.GsonUtils;
import me.zheroandre.elysskyblock.utils.location.LocationUtils;
import me.zheroandre.elysskyblock.utils.task.TaskUtils;

import java.sql.Types;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class IDBHandler {

    private final ElysSkyBlockPlugin plugin;
    private final AsyncDatabase asyncDatabase;

    public IDBHandler(ElysSkyBlockPlugin plugin) {
        this.plugin = plugin;

        Credentials credentials = Credentials
                .builder()
                .host("178.63.211.78")
                .user("user")
                .password("password")
                .schema("elys")
                .pool("island-storage")
                .port(3306)
                .build();

        Executor executor = (command -> TaskUtils.async(plugin, command));
        this.asyncDatabase = Mystral.newAsyncDatabase(credentials, executor);

        this.table().whenComplete(
                (integer, throwable) -> {
                    if (throwable != null) throwable.printStackTrace();
                }
        );
    }

    public CompletableFuture<Integer> table() {
        return asyncDatabase.update("CREATE TABLE IF NOT EXISTS island_storage( identifier INTEGER NOT NULL, owner TEXT NOT NULL, members TEXT NOT NULL, core_location TEXT NOT NULL, spawn_location TEXT NOT NULL, upgrades TEXT NOT NULL, settings TEXT NOT NULL, level INTEGER NOT NULL, points DOUBLE NOT NULL, token INTEGER NOT NULL, primary key (identifier))", false);
    }

    public CompletableFuture<Integer> present(int identifier) {
        return asyncDatabase.queryForObject("SELECT * FROM island_storage WHERE identifier = ?", new Object[]{identifier}, (rs, rowNumber) -> rs.getInt("identifier"), Types.INTEGER);
    }

    public CompletableFuture<List<Integer>> has(UUID playerUUID) {
        return asyncDatabase.queryForList("SELECT * FROM island_storage", (resultSet, rowNumber) -> {
            if (resultSet.getString("members").contains(playerUUID.toString())) return resultSet.getInt("identifier");

            return null;
        });
    }

    public CompletableFuture<Integer> insert(EIsland island) {
        return asyncDatabase.update("INSERT INTO island_storage VALUES(? , ? , ? , ? , ? , ? , ? , ? , ? , ?)", new Object[]{
                island.getIdentifier(), island.getOwner().toString(), GsonUtils.serialize(island.getMembersMap()), LocationUtils.serialize(island.getCoreLocation()), LocationUtils.serialize(island.getSpawnLocation()), GsonUtils.serialize(island.getUpgradesMap()), GsonUtils.serialize(island.getSettingsMap()), island.getLevel(), island.getPoints(), island.getToken()
        }, false, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.DOUBLE, Types.INTEGER);
    }

    public CompletableFuture<Integer> delete(EIsland island) {
        return asyncDatabase.update("DELETE FROM island_storage WHERE identifier = ?", new Object[]{island.getIdentifier()}, false, Types.INTEGER);
    }

    public CompletableFuture<Integer> update(EIsland island) {
        return asyncDatabase.update("UPDATE island_storage SET owner = ?, members = ?, core_location = ?, spawn_location = ?, upgrades = ?, settings = ?, level = ?, points = ?, token = ? WHERE identifier = ?", new Object[]{
                island.getOwner().toString(), GsonUtils.serialize(island.getMembersMap()), LocationUtils.serialize(island.getCoreLocation()), LocationUtils.serialize(island.getSpawnLocation()), GsonUtils.serialize(island.getUpgradesMap()), GsonUtils.serialize(island.getSettingsMap()), island.getLevel(), island.getPoints(), island.getToken(), island.getIdentifier()
        }, false, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.DOUBLE, Types.INTEGER, Types.INTEGER);
    }

    @SuppressWarnings("UnstableApiUsage")
    public CompletableFuture<EIsland> load(int identifier) {
        return asyncDatabase.queryForObject("SELECT * FROM island_storage WHERE identifier = ?", new Object[]{identifier}, (rs, rowNumber) -> plugin.getIslandHandler().initialize(
                rs.getInt("identifier"), UUID.fromString(rs.getString("owner")), GsonUtils.deserialize(rs.getString("members"), new TypeToken<LinkedHashMap<UUID, IslandRole>>(){}), LocationUtils.deserialize(rs.getString("core_location")), LocationUtils.deserialize(rs.getString("spawn_location")), GsonUtils.deserialize(rs.getString("upgrades"), new TypeToken<LinkedHashMap<IslandUpgrade, Integer>>(){}), GsonUtils.deserialize(rs.getString("settings"), new TypeToken<HashMap<IslandSetting, Boolean>>(){}), rs.getInt("level"), rs.getDouble("points"), rs.getInt("token"), false
        ), Types.INTEGER);
    }

    public void save() {
        plugin.getIslandHandler().getIslandsMap().forEach(
                (integer, island) -> {
                    this.present(island.getIdentifier()).whenComplete(
                            (identifier, throwable) -> {
                                if (throwable != null) throwable.printStackTrace();
                                if (identifier == null) {
                                    this.insert(island).whenComplete(
                                            (integer1, throwable1) -> {
                                                if (throwable1 != null) throwable1.printStackTrace();
                                            }
                                    );
                                } else {
                                    this.update(island).whenComplete(
                                            (integer1, throwable1) -> {
                                                if (throwable1 != null) throwable1.printStackTrace();
                                            }
                                    );
                                }
                            }
                    );
                }
        );
    }

}
