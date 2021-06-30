package me.zheroandre.elysskyblock.listener;

import me.zheroandre.elysskyblock.ElysSkyBlockPlugin;
import me.zheroandre.elysskyblock.handler.island.IslandHandler;
import me.zheroandre.elysskyblock.handler.player.PlayerHandler;
import me.zheroandre.elysskyblock.objects.island.EIsland;
import me.zheroandre.elysskyblock.objects.player.EPlayer;
import me.zheroandre.elysskyblock.utils.string.StringUtils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener {

    private final PlayerHandler playerHandler;
    private final IslandHandler islandHandler;

    public AsyncPlayerChatListener(ElysSkyBlockPlugin plugin) {
        this.playerHandler = plugin.getPlayerHandler();
        this.islandHandler = plugin.getIslandHandler();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void asyncPlayerChatEvent(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        e.setFormat(StringUtils.color("&7" + player.getName() + " &8» &f") + e.getMessage());

        if (!islandHandler.has(player)) return;
        EIsland island = islandHandler.get(player);

        if (!playerHandler.has(player)) return;
        EPlayer ePlayer = playerHandler.get(player);

        if (!ePlayer.isIslandChat()) return;
        if (!e.getMessage().startsWith("!")) return;

        e.getRecipients().clear();
        e.getRecipients().addAll(island.onlinePlayers());

        e.setFormat(StringUtils.color("&b" + player.getName() + " &8» &b") + e.getMessage());
    }

}
