package me.zheroandre.elysskyblock.handler.db;

import com.glyart.mystral.database.AsyncDatabase;
import com.glyart.mystral.database.Credentials;
import com.glyart.mystral.database.Mystral;
import com.google.common.reflect.TypeToken;
import me.zheroandre.elysskyblock.ElysSkyBlockPlugin;
import me.zheroandre.elysskyblock.enums.player.PlayerSetting;
import me.zheroandre.elysskyblock.objects.player.EPlayer;
import me.zheroandre.elysskyblock.utils.gson.GsonUtils;
import me.zheroandre.elysskyblock.utils.task.TaskUtils;

import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class PDBHandler {

    private final ElysSkyBlockPlugin plugin;
    private final AsyncDatabase asyncDatabase;

    public PDBHandler(ElysSkyBlockPlugin plugin) {
        this.plugin = plugin;

        Credentials credentials = Credentials
                .builder()
                .host("178.63.211.78")
                .user("user")
                .password("password")
                .schema("elys")
                .pool("player-storage")
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
        return asyncDatabase.update("CREATE TABLE IF NOT EXISTS player_storage( identifier INTEGER AUTO_INCREMENT, uuid TEXT NOT NULL, island_chat BOOLEAN NOT NULL, settings TEXT NOT NULL, money DOUBLE NOT NULL, primary key (identifier))", false);
    }

    public CompletableFuture<UUID> present(UUID playerUUID) {
        return asyncDatabase.queryForObject("SELECT * FROM player_storage WHERE uuid = ?", new Object[]{playerUUID.toString()}, (rs, rowNumber) -> UUID.fromString(rs.getString("uuid")), Types.VARCHAR);
    }

    public CompletableFuture<Integer> insert(EPlayer player) {
        return asyncDatabase.update("INSERT INTO player_storage (uuid , island_chat , settings , money) VALUES(? , ? , ? , ?)", new Object[]{
                player.getPlayerUUID().toString(), player.isIslandChat(), GsonUtils.serialize(player.getSettingsMap()) , player.getMoney()
        }, false, Types.VARCHAR, Types.BOOLEAN, Types.VARCHAR, Types.DOUBLE);
    }

    public CompletableFuture<Integer> update(EPlayer player) {
        return asyncDatabase.update("UPDATE player_storage SET island_chat = ?, settings = ? money = ? WHERE uuid = ?", new Object[]{
                player.isIslandChat(), GsonUtils.serialize(player.getSettingsMap()), player.getMoney(), player.getPlayerUUID().toString()
        }, false, Types.BOOLEAN, Types.VARCHAR, Types.DOUBLE, Types.VARCHAR);
    }

    @SuppressWarnings("UnstableApiUsage")
    public CompletableFuture<EPlayer> load(UUID playerUUID) {
        return asyncDatabase.queryForObject("SELECT * FROM player_storage WHERE uuid = ?", new Object[]{playerUUID.toString()}, (rs, rowNumber) -> plugin.getPlayerHandler().initialize(
                playerUUID, rs.getBoolean("island_chat"), GsonUtils.deserialize(rs.getString("settings"), new TypeToken<LinkedHashMap<PlayerSetting, Boolean>>(){}), rs.getDouble("money")
        ), Types.VARCHAR);
    }

    public void save() {
        plugin.getPlayerHandler().getPlayersMap().forEach(
                (integer, player) -> {
                    this.present(player.getPlayerUUID()).whenComplete(
                            (identifier, throwable) -> {
                                if (throwable != null) throwable.printStackTrace();
                                if (identifier == null) {
                                    this.insert(player).whenComplete(
                                            (integer1, throwable1) -> {
                                                if (throwable1 != null) throwable1.printStackTrace();
                                            }
                                    );
                                } else {
                                    this.update(player).whenComplete(
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
