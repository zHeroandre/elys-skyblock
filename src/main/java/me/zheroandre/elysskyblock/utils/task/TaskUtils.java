package me.zheroandre.elysskyblock.utils.task;

import me.zheroandre.elysskyblock.ElysSkyBlockPlugin;
import org.bukkit.Bukkit;

public class TaskUtils {

    public static void async(ElysSkyBlockPlugin plugin, Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public static void async(ElysSkyBlockPlugin plugin, Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay);
    }

}
