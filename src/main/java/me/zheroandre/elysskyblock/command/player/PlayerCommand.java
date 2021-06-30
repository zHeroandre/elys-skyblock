package me.zheroandre.elysskyblock.command.player;

import me.zheroandre.elysskyblock.ElysSkyBlockPlugin;
import me.zheroandre.elysskyblock.abstraction.command.ElysCommand;
import me.zheroandre.elysskyblock.abstraction.command.ElysSubCommand;
import me.zheroandre.elysskyblock.enums.island.IslandInventory;
import me.zheroandre.elysskyblock.handler.player.PlayerHandler;
import me.zheroandre.elysskyblock.objects.island.EIsland;
import me.zheroandre.elysskyblock.objects.player.EPlayer;
import me.zheroandre.elysskyblock.utils.message.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/*
 *  COMMAND  ( PLAYER )
 */
public class PlayerCommand extends ElysCommand {

    public PlayerCommand(ElysSkyBlockPlugin plugin) {
        super("player", "", "/", Collections.singletonList("p"));

        console = false;
        permission = "elys.skyblock.player";

        addSubCommand(new SettingSubCommand(plugin));
    }

    @Override
    public boolean command(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        MessageUtils.send(player, "&4&l(&c&l!&4&l) &cWrong syntax, please try again");

        return true;
    }

    /*
     * SUBCOMMAND ( SETTING )
     * USE ( /PLAYER SETTING )
     */
    private static class SettingSubCommand extends ElysSubCommand {
        private final PlayerHandler playerHandler;

        public SettingSubCommand(ElysSkyBlockPlugin plugin) {
            super("setting");

            this.playerHandler = plugin.getPlayerHandler();
        }

        @Override
        public boolean command(CommandSender sender, String[] args) {
            Player player = (Player) sender;

            if (!playerHandler.has(player)) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cA problem was encountered, re-enter the server");
                return true;
            }

            if (args.length != 1) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cWrong syntax, please try again");
                return true;
            }

            EPlayer ePlayer = playerHandler.get(player);
            player.openInventory(ePlayer.getSettingInventory().getInventory());

            return true;
        }
    }

}
