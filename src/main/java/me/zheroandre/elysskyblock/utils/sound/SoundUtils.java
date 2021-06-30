package me.zheroandre.elysskyblock.utils.sound;

import me.zheroandre.elysskyblock.ElysSkyBlockPlugin;
import me.zheroandre.elysskyblock.utils.task.TaskUtils;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public class SoundUtils {

    public static void play(ElysSkyBlockPlugin plugin, Player player, Sound sound) {
        TaskUtils.async(plugin, () -> player.playSound(player.getLocation(), sound, SoundCategory.PLAYERS, 10f, 1f));
    }

}
