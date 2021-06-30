package me.zheroandre.elysskyblock.utils.string;

import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> color(List<String> strings) {
        List<String> colorStrings = new ArrayList<>();
        for (String string : strings) colorStrings.add(color(string));

        return colorStrings;
    }

}
