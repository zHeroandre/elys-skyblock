package me.zheroandre.elysskyblock.command.elys;

import me.zheroandre.elysskyblock.ElysSkyBlockPlugin;
import me.zheroandre.elysskyblock.abstraction.command.ElysCommand;
import me.zheroandre.elysskyblock.abstraction.command.ElysSubCommand;
import me.zheroandre.elysskyblock.handler.island.IslandHandler;
import me.zheroandre.elysskyblock.handler.spawn.SpawnHandler;
import me.zheroandre.elysskyblock.utils.message.MessageUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/*
 *  COMMAND  ( ELYS )
 */
public class ElysSCommand extends ElysCommand {

    public ElysSCommand(ElysSkyBlockPlugin plugin) {
        super("elys", "", "/", new ArrayList<>());

        console = false;
        permission = "elys.skyblock.admin";

        addSubCommand(new SpawnSubCommand(plugin));
    }

    @Override
    public boolean command(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length == 0) {
            //
            return true;
        }

        return true;
    }

    /*
     *  SUBCOMMAND  ( SPAWN )
     *  USE  ( /ELYS SPAWN )
     */
    private static class SpawnSubCommand extends ElysSubCommand {
        private final SpawnHandler spawnHandler;

        public SpawnSubCommand(ElysSkyBlockPlugin plugin) {
            super("spawn");

            this.spawnHandler = plugin.getSpawnHandler();
        }

        @Override
        public boolean command(CommandSender sender, String[] args) {
            Player player = (Player) sender;

            spawnHandler.setLocation(player.getLocation());
            MessageUtils.send(player, "&2&l(&a&l!&2&l) &aYou have set global spawn in this location");

            return true;
        }
    }

}
