package me.zheroandre.elysskyblock.objects.player;

import lombok.Getter;
import lombok.Setter;
import me.zheroandre.elysskyblock.ElysSkyBlockPlugin;
import me.zheroandre.elysskyblock.builder.inventory.ElysInventory;
import me.zheroandre.elysskyblock.builder.item.SkullGUI;
import me.zheroandre.elysskyblock.enums.island.IslandRole;
import me.zheroandre.elysskyblock.enums.player.PlayerSetting;
import me.zheroandre.elysskyblock.utils.message.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class EPlayer {

    private final ElysSkyBlockPlugin plugin;

    @Getter private final UUID playerUUID;
    @Getter @Setter private boolean islandChat;
    @Getter private final Map<PlayerSetting, Boolean> settingsMap;
    @Getter @Setter private double money;

    private final ElysInventory settingInventory;

    public EPlayer(ElysSkyBlockPlugin plugin, UUID playerUUID) {
        this(plugin, playerUUID, false, null, 0.0);
    }

    public EPlayer(ElysSkyBlockPlugin plugin, UUID playerUUID, boolean islandChat, Map<PlayerSetting, Boolean> settingsMap, double money) {
        this.plugin = plugin;

        this.playerUUID = playerUUID;
        this.islandChat = islandChat;

        if (settingsMap == null) {
            this.settingsMap = new LinkedHashMap<>();
            this.settingsMap.put(PlayerSetting.ISLAND_CORE_GLOW_VIEW, true);
        } else {
            this.settingsMap = settingsMap;
        }

        this.money = money;

        this.settingInventory = new ElysInventory(27, "")
                .fill(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
    }

    public ElysInventory getSettingInventory() {
        List<Map.Entry<PlayerSetting, Boolean>> entrySetting = new ArrayList<>(settingsMap.entrySet());

        int counter = 10;
        for (Map.Entry<PlayerSetting, Boolean> settingEntry : entrySetting) {
            settingInventory
                    .set(counter, new SkullGUI()
                            .name(" &b&l" + settingEntry.getKey().string().replace("_", " "))
                            .lore(
                                    Arrays.asList(
                                            "&r",
                                            "",
                                            "&r"
                                    )
                            )
                            .event(event -> {
                                Player player = (Player) event.getWhoClicked();

                                player.closeInventory();
                                MessageUtils.send(player, "&4&l(&c&l!&4&l) &cThis feature is being worked on ");

                                event.setCancelled(true);
                            })
                    );
            counter++;
        }
        return settingInventory;
    }

    public void save() {
        plugin.getPdbHandler().present(playerUUID).whenComplete(
                (integer, throwable) -> {
                    if (throwable != null) throwable.printStackTrace();
                    if (integer == null) {
                        plugin.getPdbHandler().insert(this).whenComplete(
                                (integer1, throwable1) -> {
                                    if (throwable1 != null) throwable1.printStackTrace();

                                    plugin.getPlayerHandler().remove(playerUUID);
                                }
                        );
                    } else plugin.getPdbHandler().update(this).whenComplete(
                            (integer1, throwable1) -> {
                                if (throwable1 != null) throwable1.printStackTrace();

                                plugin.getPlayerHandler().remove(playerUUID);
                            }
                    );
                }
        );
    }

}
