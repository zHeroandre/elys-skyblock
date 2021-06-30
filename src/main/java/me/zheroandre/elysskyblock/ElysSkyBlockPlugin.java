package me.zheroandre.elysskyblock;

import lombok.Getter;
import me.zheroandre.elysskyblock.command.elys.ElysSCommand;
import me.zheroandre.elysskyblock.command.island.IslandCommand;
import me.zheroandre.elysskyblock.command.player.PlayerCommand;
import me.zheroandre.elysskyblock.command.spawn.SpawnCommand;
import me.zheroandre.elysskyblock.handler.db.IDBHandler;
import me.zheroandre.elysskyblock.handler.db.PDBHandler;
import me.zheroandre.elysskyblock.handler.island.IslandHandler;
import me.zheroandre.elysskyblock.handler.player.PlayerHandler;
import me.zheroandre.elysskyblock.handler.spawn.SpawnHandler;
import me.zheroandre.elysskyblock.listener.*;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class ElysSkyBlockPlugin extends JavaPlugin {

    private PDBHandler pdbHandler;
    private IDBHandler idbHandler;

    private PlayerHandler playerHandler;
    private IslandHandler islandHandler;

    private SpawnHandler spawnHandler;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        this.pdbHandler = new PDBHandler(this);
        this.idbHandler = new IDBHandler(this);

        this.playerHandler = new PlayerHandler(this);
        this.islandHandler = new IslandHandler(this);

        this.spawnHandler = new SpawnHandler(this);

        this.registerCommands();
        this.registerListeners();
    }

    @Override
    public void onDisable() {
        //
    }

    private void registerCommands() {
        new ElysSCommand(this);
        new IslandCommand(this);
        new PlayerCommand(this);


        new SpawnCommand(this);
    }

    private void registerListeners() {
        new BlockPlaceListener(this);
        new BlockBreakListener(this);
        new InventoryClickListener(this);

        new EntityDamageListener(this);

        new PlayerJoinListener(this);
        new PlayerQuitListener(this);

        new AsyncPlayerChatListener(this);
    }

}
