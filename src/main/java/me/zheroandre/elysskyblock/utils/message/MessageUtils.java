package me.zheroandre.elysskyblock.utils.message;

import me.zheroandre.elysskyblock.utils.string.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageUtils {

    public static void send(CommandSender sender, String string) {
        sender.sendMessage(StringUtils.color(string));
    }

    public static void send(Player player, String string) {
        player.sendMessage(StringUtils.color(string));
    }

    public static void send(Player player, String string, Object[] values) {
        for (int i = 0; i < values.length; i++) {
            string = string.replace("{" + i + "}", (String) values[i]);
        }

        player.sendMessage(StringUtils.color(string));
    }

    public static void send(Player player, String string, String[] placeholders, Object[] values) {
        if (placeholders.length != values.length) player.sendMessage(StringUtils.color(string));

        for (int i = 0; i < placeholders.length; i++) {
            string = string.replace(placeholders[i], (String) values[i]);
        }

        player.sendMessage(StringUtils.color(string));
    }

}
