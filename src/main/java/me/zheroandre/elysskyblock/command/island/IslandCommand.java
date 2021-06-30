package me.zheroandre.elysskyblock.command.island;

import me.zheroandre.elysskyblock.ElysSkyBlockPlugin;
import me.zheroandre.elysskyblock.abstraction.command.ElysCommand;
import me.zheroandre.elysskyblock.abstraction.command.ElysSubCommand;
import me.zheroandre.elysskyblock.enums.island.IslandInventory;
import me.zheroandre.elysskyblock.enums.island.IslandPermission;
import me.zheroandre.elysskyblock.enums.island.IslandRole;
import me.zheroandre.elysskyblock.enums.island.IslandSetting;
import me.zheroandre.elysskyblock.handler.island.IslandHandler;
import me.zheroandre.elysskyblock.handler.player.PlayerHandler;
import me.zheroandre.elysskyblock.handler.spawn.SpawnHandler;
import me.zheroandre.elysskyblock.objects.island.EIsland;
import me.zheroandre.elysskyblock.objects.player.EPlayer;
import me.zheroandre.elysskyblock.utils.message.MessageUtils;
import me.zheroandre.elysskyblock.utils.task.TaskUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Objects;

/*
 *  COMMAND  ( ISLAND )
 */
public class IslandCommand extends ElysCommand {

    private final IslandHandler islandHandler;

    public IslandCommand(ElysSkyBlockPlugin plugin) {
        super("island", "", "/", Collections.singletonList("is"));

        this.islandHandler = plugin.getIslandHandler();

        console = false;
        permission = "elys.skyblock.island";

        addSubCommand(new SetSpawnSubCommand(plugin));
        addSubCommand(new InviteSubCommand(plugin));
        addSubCommand(new KickSubCommand(plugin));
        addSubCommand(new JoinSubCommand(plugin));
        addSubCommand(new LeaveSubCommand(plugin));
        addSubCommand(new CancelSubCommand(plugin));

        addSubCommand(new GoSubCommand(plugin));
        addSubCommand(new VisitSubCommand(plugin));

        addSubCommand(new PromoteSubCommand(plugin));
        addSubCommand(new DemoteSubCommand(plugin));

        addSubCommand(new ChatSubCommand(plugin));
    }

    @Override
    public boolean command(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (islandHandler.has(player)) {
            EIsland island = islandHandler.get(player);
            player.openInventory(island.inventory(IslandInventory.MANAGER).getInventory());
            // TO COMPLETE
            return true;
        }

        player.openInventory(islandHandler.getCreateInventory().getInventory());

        return true;
    }

    /*
     *  SUBCOMMAND  ( GO )
     *  USE  ( /ISLAND GO )
     */
    private static class GoSubCommand extends ElysSubCommand {
        private final IslandHandler islandHandler;

        public GoSubCommand(ElysSkyBlockPlugin plugin) {
            super("go");

            this.islandHandler = plugin.getIslandHandler();
        }

        @Override
        public boolean command(CommandSender sender, String[] args) {
            Player player = (Player) sender;

            if (!islandHandler.has(player)) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYou don't own an island");
                return true;
            }

            if (args.length != 1) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cWrong syntax, please try again");
                return true;
            }

            EIsland island = islandHandler.get(player);
            player.teleport(island.getSpawnLocation());

            return true;
        }
    }

    /*
     * SUBCOMMAND ( SETSPAWN )
     * USE ( /ISLAND SETSPAWN )
     */
    private static class SetSpawnSubCommand extends ElysSubCommand {
        private final IslandHandler islandHandler;

        public SetSpawnSubCommand(ElysSkyBlockPlugin plugin) {
            super("setspawn");

            this.islandHandler = plugin.getIslandHandler();
        }

        @Override
        public boolean command(CommandSender sender, String[] args) {
            Player player = (Player) sender;

            if (!islandHandler.has(player)) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYou don't own an island");
                return true;
            }

            EIsland island = islandHandler.get(player);
            if (!island.hasPermission(player.getUniqueId(), IslandPermission.ISLAND_SETTING_SPAWN)) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYou are not allowed to perform this action");
                return true;
            }

            if (args.length != 1) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cWrong syntax, please try again");
                return true;
            }

            Location location = player.getLocation();

            if (!Objects.equals(location.getWorld(), islandHandler.islandWorld) || !island.isPart(location)) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYou are not inside your island");
                return true;
            }

            MessageUtils.send(player, "&2&l(&a&l!&2&l) &aYou have successfully set the island's spawn location");
            island.setSpawnLocation(location);

            return true;
        }
    }

    /*
     *  SUBCOMMAND  ( INVITE )
     *  USE  ( /ISLAND INVITE <player> )
     */
    private static class InviteSubCommand extends ElysSubCommand {
        private final IslandHandler islandHandler;

        public InviteSubCommand(ElysSkyBlockPlugin plugin) {
            super("invite");

            this.islandHandler = plugin.getIslandHandler();
        }

        @Override
        public boolean command(CommandSender sender, String[] args) {
            Player player = (Player) sender;

            if (!islandHandler.has(player)) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYou don't own an island");
                return true;
            }

            EIsland island = islandHandler.get(player);
            if (!island.hasPermission(player.getUniqueId(), IslandPermission.ISLAND_INVITE_MEMBER)) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYou are not allowed to perform this action");
                return true;
            }

            if (args.length != 2) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cWrong syntax, please try again");
                return true;
            }

            if (island.totalMembers() == island.maxMembers()) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cThis island has reached the maximum of members");
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cThis player is currently offline");
                return true;
            }

            if (player.getName().equalsIgnoreCase(target.getName())) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYou cannot execute this command on yourself");
                return true;
            }

            if (island.isMember(target.getUniqueId())) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cThis player is already part of your island");
                return true;
            }

            if (islandHandler.has(target)) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cThis player already owns an island");
                return true;
            }

            if (island.check(target.getUniqueId())) {
                return true;
            }

            MessageUtils.send(player, "&2&l(&a&l!&2&l) &aYou invited {0} to successfully enter your island", new Object[]{target.getName()});
            MessageUtils.send(target, "&2&l(&a&l!&2&l) &aYou received an invitation from {0}", new Object[]{player.getName()});
            island.send(player.getUniqueId(), target.getUniqueId());

            return true;
        }
    }

    /*
     *  SUBCOMMAND  ( KICK )
     *  USE  ( /ISLAND KICK <player> )
     */
    private static class KickSubCommand extends ElysSubCommand {
        private final IslandHandler islandHandler;

        public KickSubCommand(ElysSkyBlockPlugin plugin) {
            super("kick");

            this.islandHandler = plugin.getIslandHandler();
        }

        @Override
        public boolean command(CommandSender sender, String[] args) {
            Player player = (Player) sender;

            if (!islandHandler.has(player)) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYou don't own an island");
                return true;
            }

            EIsland island = islandHandler.get(player);
            if (!island.hasPermission(player.getUniqueId(), IslandPermission.ISLAND_KICK_MEMBER)) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYou are not allowed to perform this action");
                return true;
            }

            if (args.length != 2) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cWrong syntax, please try again");
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cThis player is currently offline");
                return true;
            }

            if (player.getName().equalsIgnoreCase(target.getName())) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYou cannot execute this command on yourself");
                return true;
            }

            if (!island.isMember(target.getUniqueId())) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cThis player is not a member of your island");
                return true;
            }

            if (island.role(target.getUniqueId()) == IslandRole.OWNER) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &CYou can't kick the island owner");
                return true;
            }

            MessageUtils.send(player, "&2&l(&a&l!&2&l) &aYou successfully kicked {0}", new Object[]{target.getName()});
            island.removeMember(target.getUniqueId());

            return true;
        }
    }

    /*
     *  SUBCOMMAND  ( JOIN )
     *  USE  ( /ISLAND JOIN <player> )
     */
    private static class JoinSubCommand extends ElysSubCommand {
        private final IslandHandler islandHandler;

        public JoinSubCommand(ElysSkyBlockPlugin plugin) {
            super("join");

            this.islandHandler = plugin.getIslandHandler();
        }

        @Override
        public boolean command(CommandSender sender, String[] args) {
            Player player = (Player) sender;

            if (args.length != 2) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cWrong syntax, please try again");
                return true;
            }

            if (islandHandler.has(player)) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYou are already part of an island");
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cThis player is currently offline");
                return true;
            }

            if (player.getName().equalsIgnoreCase(target.getName())) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYou cannot execute this command on yourself");
                return true;
            }

            EIsland island = islandHandler.get(target);
            if (!island.check(player.getUniqueId())) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYou have no invitation from {0}", new Object[]{target.getName()});
                return true;
            }

            if (island.totalMembers() == island.maxMembers()) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cThis island has reached the maximum of members");
                return true;
            }

            island.onlinePlayers().forEach(member ->
                    MessageUtils.send(member, "&2&l(&a&l!&2&l) &a{0} has joined the island", new Object[]{player.getName()})
            );
            MessageUtils.send(player, "&2&l(&a&l!&2&l) &aYou have joined the island of {0}", new Object[]{target.getName()});
            island.addMember(player.getUniqueId());

            return true;
        }
    }

    /*
     * SUBCOMMAND ( LEAVE )
     * USE ( /ISLAND LEAVE )
     */
    private static class LeaveSubCommand extends ElysSubCommand {
        private final SpawnHandler spawnHandler;
        private final IslandHandler islandHandler;

        public LeaveSubCommand(ElysSkyBlockPlugin plugin) {
            super("leave");

            this.spawnHandler = plugin.getSpawnHandler();
            this.islandHandler = plugin.getIslandHandler();
        }

        @Override
        public boolean command(CommandSender sender, String[] args) {
            Player player = (Player) sender;

            if (!islandHandler.has(player)) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYou don't own an island");
                return true;
            }

            if (args.length != 1) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cWrong syntax, please try again");
                return true;
            }

            EIsland island = islandHandler.get(player);
            if (island.role(player.getUniqueId()) == IslandRole.OWNER) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYou are the island owner use /island disband");
                return true;
            }

            if (spawnHandler.isSetting()) player.teleport(spawnHandler.getLocation());
            island.removeMember(player.getUniqueId());

            if (island.onlinePlayers().size() == 0) island.save();

            island.onlinePlayers().forEach(member ->
                        MessageUtils.send(member, "&2&l(&a&l!&2&l) &a{0} has left the island", new Object[]{player.getName()})
                    );
            MessageUtils.send(player, "&2&l(&a&l!&2&l) &aYou have successfully left your island");

            return true;
        }
    }

    /*
     * SUBCOMMAND ( CANCEL )
     * USE ( /ISLAND CANCEL )
     */
    private static class CancelSubCommand extends ElysSubCommand {
        private final ElysSkyBlockPlugin plugin;
        private final IslandHandler islandHandler;

        public CancelSubCommand(ElysSkyBlockPlugin plugin) {
            super("cancel");

            this.plugin = plugin;
            this.islandHandler = plugin.getIslandHandler();
        }

        @Override
        public boolean command(CommandSender sender, String[] args) {
            Player player = (Player) sender;

            if (!islandHandler.has(player)) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYou don't own an island");
                return true;
            }

            if (args.length != 1) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cWrong syntax, please try again");
                return true;
            }

            EIsland island = islandHandler.get(player);
            if (island.role(player.getUniqueId()) != IslandRole.OWNER) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYou must be the owner to be able to eliminate the island");
                return true;
            }

            if (!island.cancellation) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cSend the command back within 10 seconds to confirm");
                island.cancellation = true;

                TaskUtils.async(plugin, () -> island.cancellation = false, 200);
                return true;
            }

            MessageUtils.send(player, "&2&l(&a&l!&2&l) &aYou have canceled your island");
            island.cancellation = false;
            island.delete();

            return true;
        }
    }

    /*
     *  SUBCOMMAND  ( PROMOTE )
     *  USE  ( /ISLAND PROMOTE <player> )
     */
    private static class PromoteSubCommand extends ElysSubCommand {
        private final IslandHandler islandHandler;

        public PromoteSubCommand(ElysSkyBlockPlugin plugin) {
            super("promote");

            this.islandHandler = plugin.getIslandHandler();
        }

        @Override
        public boolean command(CommandSender sender, String[] args) {
            Player player = (Player) sender;

            if (!islandHandler.has(player)) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYou don't own an island");
                return true;
            }

            EIsland island = islandHandler.get(player);
            if (!island.hasPermission(player.getUniqueId(), IslandPermission.ISLAND_PROMOTE_MEMBER)) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYou are not allowed to perform this action");
                return true;
            }

            if (args.length != 2) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cWrong syntax, please try again");
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cThis player is currently offline");
                return true;
            }

            if (player.getName().equalsIgnoreCase(target.getName())) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYou cannot execute this command on yourself");
                return true;
            }

            if (!island.isMember(target.getUniqueId())) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cThis player is not a member of your island");
                return true;
            }

            if (island.role(target.getUniqueId()) == IslandRole.MODERATOR) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYou cannot promote this player player further");
                return true;
            }

            MessageUtils.send(player, "&2&l(&a&l!&2&l) &aYou have successfully promoted {0}", new Object[]{target.getName()});
            island.promote(target.getUniqueId());

            return true;
        }
    }

    /*
     *  SUBCOMMAND  ( DEMOTE )
     *  USE  ( /ISLAND DEMOTE <player> )
     */
    private static class DemoteSubCommand extends ElysSubCommand {
        private final IslandHandler islandHandler;

        public DemoteSubCommand(ElysSkyBlockPlugin plugin) {
            super("demote");

            this.islandHandler = plugin.getIslandHandler();
        }

        @Override
        public boolean command(CommandSender sender, String[] args) {
            Player player = (Player) sender;

            if (!islandHandler.has(player)) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYou don't own an island");
                return true;
            }

            EIsland island = islandHandler.get(player);
            if (!island.hasPermission(player.getUniqueId(), IslandPermission.ISLAND_PROMOTE_MEMBER)) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYou are not allowed to perform this action");
                return true;
            }

            if (args.length != 2) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cWrong syntax, please try again");
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cThis player is currently offline");
                return true;
            }

            if (player.getName().equalsIgnoreCase(target.getName())) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYou cannot execute this command on yourself");
                return true;
            }

            if (!island.isMember(target.getUniqueId())) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cThis player is not a member of your island");
                return true;
            }

            if (island.role(target.getUniqueId()) == IslandRole.DEFAULT) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYou cannot degrade this player more");
                return true;
            }

            MessageUtils.send(player, "&2&l(&a&l!&2&l) &aYou have successfully downgraded {0}", new Object[]{target.getName()});
            island.demote(target.getUniqueId());

            return true;
        }
    }

    /*
     *  SUBCOMMAND  ( VISIT )
     *  USE  ( /ISLAND VISIT <player> )
     */
    private static class VisitSubCommand extends ElysSubCommand {
        private final IslandHandler islandHandler;

        public VisitSubCommand(ElysSkyBlockPlugin plugin) {
            super("visit");

            this.islandHandler = plugin.getIslandHandler();
        }

        @Override
        public boolean command(CommandSender sender, String[] args) {
            Player player = (Player) sender;

            if (args.length != 2) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cWrong syntax, please try again");
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cThis player is currently offline");
                return true;
            }

            if (player.getName().equalsIgnoreCase(target.getName())) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYou cannot visit your own island");
                return true;
            }

            if (!islandHandler.has(target)) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cThis player does not have an island");
                return true;
            }

            EIsland island = islandHandler.get(target);
            if (!island.setting(IslandSetting.ISLAND_VISIT)) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cThis island has blocked visits");
                return true;
            }

            MessageUtils.send(player, "&2&l(&a&l!&2&l) &aYou are visiting the island of {0}", new Object[]{target.getName()});
            player.teleport(island.getSpawnLocation());

            return true;
        }
    }

    /*
     * SUBCOMMAND ( CHAT )
     * USE ( /ISLAND CHAT )
     */
    private static class ChatSubCommand extends ElysSubCommand {
        private final PlayerHandler playerHandler;
        private final IslandHandler islandHandler;

        public ChatSubCommand(ElysSkyBlockPlugin plugin) {
            super("chat");

            this.playerHandler = plugin.getPlayerHandler();
            this.islandHandler = plugin.getIslandHandler();
        }

        @Override
        public boolean command(CommandSender sender, String[] args) {
            Player player = (Player) sender;

            if (!islandHandler.has(player)) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYou don't own an island");
                return true;
            }

            if (args.length != 1) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cWrong syntax, please try again");
                return true;
            }

            if (!playerHandler.has(player)) {
                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cA problem was encountered, re-enter the server");
                return true;
            }

            EPlayer ePlayer = playerHandler.get(player);
            if (ePlayer.isIslandChat()) {
                MessageUtils.send(player, "&2&l(&a&l!&2&l) &aYou have disabled island chat");
                ePlayer.setIslandChat(false);
                return true;
            }

            MessageUtils.send(player, "&2&l(&a&l!&2&l) &aYou have enabled island chat");
            ePlayer.setIslandChat(true);

            return true;
        }
    }

}
