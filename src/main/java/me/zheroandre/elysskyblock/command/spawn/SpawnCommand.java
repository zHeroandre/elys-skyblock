package me.zheroandre.elysskyblock.command.spawn;

import me.zheroandre.elysskyblock.ElysSkyBlockPlugin;
import me.zheroandre.elysskyblock.abstraction.command.ElysCommand;
import me.zheroandre.elysskyblock.handler.spawn.SpawnHandler;
import me.zheroandre.elysskyblock.utils.message.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

/*
 *  COMMAND  ( SPAWN )
 */
public class SpawnCommand extends ElysCommand {

    private final SpawnHandler spawnHandler;

    public SpawnCommand(ElysSkyBlockPlugin plugin) {
        super("spawn");

        permission = "elys.skyblock.spawn";

        this.spawnHandler = plugin.getSpawnHandler();
    }

    @Override
    public boolean command(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (!spawnHandler.isSetting()) {
            MessageUtils.send(player, "&4&l(&c&l!&4&l) &cThe spawn is not set");
            return true;
        }

        if (Objects.equals(player.getWorld(), spawnHandler.getLocation().getWorld())) {
            MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYou are already in the spawn world");
            return true;
        }

        player.teleport(spawnHandler.getLocation());

        return true;
    }
}
