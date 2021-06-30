package me.zheroandre.elysskyblock.objects.island;

import lombok.Getter;
import lombok.Setter;
import me.zheroandre.elysskyblock.ElysSkyBlockPlugin;
import me.zheroandre.elysskyblock.builder.inventory.ElysInventory;
import me.zheroandre.elysskyblock.builder.item.SkullGUI;
import me.zheroandre.elysskyblock.enums.island.*;
import me.zheroandre.elysskyblock.utils.message.MessageUtils;
import me.zheroandre.elysskyblock.utils.task.TaskUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class EIsland {

    private final ElysSkyBlockPlugin plugin;

    @Getter private final int identifier;
    @Getter private final UUID owner;

    @Getter private final Map<UUID, IslandRole> membersMap;
    private final Map<UUID, UUID> invitesMap;

    @Getter private final Location coreLocation;
    @Getter @Setter private Location spawnLocation;

    @Getter private final Map<IslandUpgrade, Integer> upgradesMap;
    @Getter private final Map<IslandSetting, Boolean> settingsMap;

    @Getter @Setter private int level, token;
    @Getter @Setter private double points;

    @Getter private int xMin, xMax, zMin, zMax;

    private final ElysInventory managerInventory, settingInventory, upgradeInventory, membersInventory;

    public boolean cancellation;

    /*
     * CATEGORY ( CONSTRUCTOR )
     */
    public EIsland(ElysSkyBlockPlugin plugin, int identifier, UUID owner, Location coreLocation) {
        this(plugin, identifier, owner, null, coreLocation, coreLocation.clone().add(0, 1, 0), null, null, 1, 0.0, 0, true);
    }

    public EIsland(ElysSkyBlockPlugin plugin, int identifier, UUID owner, Map<UUID, IslandRole> membersMap, Location coreLocation, Location spawnLocation, Map<IslandUpgrade, Integer> upgradesMap, Map<IslandSetting, Boolean> settingsMap, int level, double points, int token, boolean creating) {
        this.plugin = plugin;

        this.identifier = identifier;
        this.owner = owner;

        if (membersMap == null) {
            this.membersMap = new LinkedHashMap<>();
            this.membersMap.put(owner, IslandRole.OWNER);
        } else {
            this.membersMap = membersMap;
        }

        this.invitesMap = new HashMap<>();

        this.coreLocation = coreLocation;
        this.spawnLocation = spawnLocation;

        if (creating) {
            coreLocation.getBlock().setType(Material.GRASS_BLOCK);
            coreLocation.clone().add(0, -1, 0).getBlock().setType(Material.BEDROCK);
        }

        if (upgradesMap == null) {
            this.upgradesMap = new LinkedHashMap<>();
            this.upgradesMap.put(IslandUpgrade.SIZE, 1);
            this.upgradesMap.put(IslandUpgrade.MEMBERS, 1);
            // TO COMPLETE
        } else {
            this.upgradesMap = upgradesMap;
        }

        if (settingsMap == null) {
            this.settingsMap = new LinkedHashMap<>();
            this.settingsMap.put(IslandSetting.ISLAND_VISIT, true);
            // TO COMPLETE
        } else {
            this.settingsMap = settingsMap;
        }

        this.level = level;
        this.points = points;
        this.token = token;

        this.xMin = coreLocation.getBlockX() - IslandUpgrade.SIZE.radius(this.upgradesMap.get(IslandUpgrade.SIZE));
        this.xMax = coreLocation.getBlockX() + IslandUpgrade.SIZE.radius(this.upgradesMap.get(IslandUpgrade.SIZE));
        this.zMin = coreLocation.getBlockZ() - IslandUpgrade.SIZE.radius(this.upgradesMap.get(IslandUpgrade.SIZE));
        this.zMax = coreLocation.getBlockZ() + IslandUpgrade.SIZE.radius(this.upgradesMap.get(IslandUpgrade.SIZE));

        this.managerInventory = new ElysInventory(27, "&8Island Manager ")
                .fill(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        this.upgradeInventory = new ElysInventory(27, "&8Island Upgrades ")
                .fill(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        this.membersInventory = new ElysInventory(27, "&8Island Members ")
                .fill(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        this.settingInventory = new ElysInventory(27, "&8Island Settings ")
                .fill(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
    }

    /*
     * CATEGORY ( INVENTORY )
     */
    public ElysInventory inventory(IslandInventory islandInventory) {
        int counter;

        switch (islandInventory) {
            case MANAGER:
                managerInventory
                        .set(4, new SkullGUI()
                                .name(" &b&lISLAND STATS ")
                                .lore(
                                        Arrays.asList(
                                                "&r",
                                                " &7Members  &b" + totalMembers() + "&8/&b" + maxMembers(),
                                                " &7Points  &b " + points + "&8/&b " + 0.0,
                                                " &7Level  &b " + level,
                                                "&r",
                                                " &7Range &b " + IslandUpgrade.SIZE.radius(this.upgradesMap.get(IslandUpgrade.SIZE)) + "&8*&b" + IslandUpgrade.SIZE.radius(this.upgradesMap.get(IslandUpgrade.SIZE)),
                                                "&r"
                                        )
                                )
                                .base64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWMzZGViOTQ5NjE4NDM1MGU0ZjJkN2RlOWNkZWNiOTQ1ZWIwZTdkZWVmNzk3NzM0ZThhMzRkOTRlNGNjNzljOSJ9fX0=")
                                .event(event -> {
                                    event.setCancelled(true);
                                })
                        )
                        .set(11, new SkullGUI()
                                .name(" &b&lISLAND UPGRADE ")
                                .lore(
                                        Arrays.asList(
                                                "&r",
                                                " &7Check and manage your island upgrades     ",
                                                "&r",
                                                "&7 You have " + this.token + " tokens to spend",
                                                "&r",
                                                " &b➜ Click me "
                                        )
                                )
                                .base64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGViODFlZjg5MDIzNzk2NTBiYTc5ZjQ1NzIzZDZiOWM4ODgzODhhMDBmYzRlMTkyZjM0NTRmZTE5Mzg4MmVlMSJ9fX0=")
                                .event(event -> {
                                    Player player = (Player) event.getWhoClicked();
                                    player.openInventory(inventory(IslandInventory.UPGRADE).getInventory());

                                    event.setCancelled(true);
                                })
                        )
                        .set(12, new SkullGUI()
                                .name(" &b&lISLAND MEMBERS ")
                                .lore(
                                        Arrays.asList(
                                                "&r",
                                                " &7See your island's member list     ",
                                                "&r",
                                                " &b➜ Click me "
                                        )
                                )
                                .base64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmVhZjljN2JkM2YxNWNjNTdjYjM0YTRkNmNhM2M2OGJhNjdhZmY0NzFmNDliYjQ5OTYzMzIyODA4ZmJkNmQxOSJ9fX0=")
                                .event(event -> {
                                    Player player = (Player) event.getWhoClicked();
                                    player.openInventory(this.inventory(IslandInventory.MEMBER).getInventory());

                                    event.setCancelled(true);
                                })
                        )
                        .set(13, new SkullGUI()
                                .name(" &b&lISLAND TELEPORT ")
                                .lore(
                                        Arrays.asList(
                                                "&r",
                                                " &7Teleport to your island spawn    ",
                                                "&r",
                                                " &b➜ Click me "
                                        )
                                )
                                .base64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGI4YTk5Y2JlOThmMDljODc0MTdhYmQ0ZWMxMmY3NzlhNTJkODMzMTRmYzQ3NGJlNTA3ZDQyMDU3MzM2NmJhNyJ9fX0=")
                                .event(event -> {
                                    Player player = (Player) event.getWhoClicked();
                                    player.teleport(spawnLocation);

                                    event.setCancelled(true);
                                })
                        )
                        .set(14, new SkullGUI()
                                .name(" &b&lISLAND SETTING ")
                                .lore(
                                        Arrays.asList(
                                                "&r",
                                                " &7Change your island settings     ",
                                                "&r",
                                                " &b➜ Click me "
                                        )
                                )
                                .base64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWM1ZDczY2I0ZGMwNjUzMTQ2MzBiN2RjNTBmZDQ0ZjE4MzM2MzdmMDdkZGRjYTdmZWRkYzIwOGMyMTUwNGQ2NSJ9fX0=")
                                .event(event -> {
                                    Player player = (Player) event.getWhoClicked();
                                    player.openInventory(inventory(IslandInventory.SETTING).getInventory());

                                    event.setCancelled(true);
                                })
                        )
                        .set(15, new SkullGUI()
                                .name(" &b&lISLAND CORE ")
                                .lore(
                                        Arrays.asList(
                                                "&r",
                                                " &7Manage the core of your island     ",
                                                "&r",
                                                " &b➜ Click me "
                                        )
                                )
                                .base64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWE2MmI5ZGU2YTI2Yjg2ODY5Y2EyMmVhNDBmMWJkZTgwYTA0MzBhNTQ1NDdiZWNjZThmZGE4NzA3Nzc3MjU4ZiJ9fX0=")
                                .event(event -> {
                                    Player player = (Player) event.getWhoClicked();

                                    player.closeInventory();
                                    MessageUtils.send(player, "&4&l(&c&l!&4&l) &cThis feature is being worked on ");

                                    event.setCancelled(true);
                                })
                        )
                        .set(22, new SkullGUI()
                                .name(" &b&lISLAND COMMANDS ")
                                .lore(
                                        Arrays.asList(
                                                "&r",
                                                " &b/is cancel  &8(( &7Request &b&lOWNER &8)) ",
                                                " &b/is kick  &8(( &7Request &b&lOWNER &8)) ",
                                                " &b/is promote  &8(( &7Request &b&lOWNER &8)) ",
                                                " &b/is demote  &8(( &7Request &b&lOWNER &8)) ",
                                                " &b/is invite  &8(( &7Request &b&lMODERATOR &8)) ",
                                                " &b/is setspawn  &8(( &7Request &b&lMODERATOR &8)) ",
                                                " &b/is go  &8(( &7Request &b&lDEFAULT &8)) ",
                                                " &b/is visit  &8(( &7Request &b&lDEFAULT &8)) ",
                                                "&r"
                                        )
                                )
                                .base64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDE3NDM0OWY3OTMxMWQxMDRkNzkxN2QzMmJmN2EwZGNlZTQyMzQyMWNhOWU4YTEzMWYyZDQwMmEzYzUzODU3MiJ9fX0=")
                                .event(event -> {
                                    event.setCancelled(true);
                                })
                        );
                return managerInventory;
            case MEMBER:
                List<Map.Entry<UUID, IslandRole>> entryMember = new ArrayList<>(membersMap.entrySet());

                counter = 10;
                for (Map.Entry<UUID, IslandRole> playerEntry : entryMember) {
                    membersInventory
                            .set(counter, new SkullGUI()
                                    .name(" &b" + Bukkit.getOfflinePlayer(playerEntry.getKey()).getName())
                                    .owner(playerEntry.getKey())
                                    .lore(
                                            Arrays.asList(
                                                    "&r",
                                                    " &7Degree  &b&l" + role(playerEntry.getKey()) + "&r     ",
                                                    "&r"
                                            )
                                    )
                                    .event(event -> event.setCancelled(true))
                            );
                    counter++;
                }
                return membersInventory;
            case UPGRADE:
                List<Map.Entry<IslandUpgrade, Integer>> entryUpgrade = new ArrayList<>(upgradesMap.entrySet());

                counter = 10;
                for (Map.Entry<IslandUpgrade, Integer> upgradeEntry : entryUpgrade) {
                    upgradeInventory
                            .set(counter, new SkullGUI()
                                    .name(" &b&l" + upgradeEntry.getKey() + " UPGRADE ")
                                    .lore(upgradeEntry.getKey().lore(upgradeEntry.getValue()))
                                    .event(event -> {
                                        event.setCancelled(true);

                                        Player player = (Player) event.getWhoClicked();

                                        ElysInventory elysInventory = (ElysInventory) event.getInventory().getHolder();
                                        if (elysInventory == null) return;

                                        SkullGUI skullGUI = elysInventory.get(event.getSlot());
                                        if (skullGUI == null) return;

                                        if (!hasPermission(player.getUniqueId(), IslandPermission.ISLAND_UPGRADE)) {
                                            MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYou are not allowed to perform this action");
                                            player.closeInventory();
                                            // TO COMPLETE
                                            return;
                                        }

                                        if (upgradesMap.get(upgradeEntry.getKey()) == upgradeEntry.getKey().MAX_LEVEL) {
                                            MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYour island has reached the maximum level");
                                            player.closeInventory();
                                            // TO COMPLETE
                                            return;
                                        }

                                        if (token < upgradeEntry.getKey().cost(upgradeEntry.getValue() + 1)) {
                                            MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYour island does not have enough tokens");
                                            player.closeInventory();
                                            // TO COMPLETE
                                            return;
                                        }

                                        MessageUtils.send(player, "&2&l(&a&l!&2&l) &aYou have successfully upgraded the " + upgradeEntry.getKey() + " upgrade");
                                        token = token - upgradeEntry.getKey().cost(upgradeEntry.getValue() + 1);

                                        upgradesMap.put(upgradeEntry.getKey(), upgradeEntry.getValue() + 1);
                                        reload();

                                        skullGUI.lore(upgradeEntry.getKey().lore(upgradesMap.get(upgradeEntry.getKey())));
                                        elysInventory.refresh();
                                    })
                            );
                    counter++;
                }
                return upgradeInventory;
            case SETTING:
                List<Map.Entry<IslandSetting, Boolean>> entrySetting = new ArrayList<>(settingsMap.entrySet());

                counter = 10;
                for (Map.Entry<IslandSetting, Boolean> settingEntry : entrySetting) {
                    settingInventory
                            .set(counter, new SkullGUI()
                                    .name(" &b&l" + settingEntry.getKey().string().replace("_", " "))
                                    .base64(this.setting(settingEntry.getKey()) ? "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU0ODRmNGI2MzY3Yjk1YmIxNjI4ODM5OGYxYzhkZDZjNjFkZTk4OGYzYTgzNTZkNGMzYWU3M2VhMzhhNDIifX19" : "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTMzYTViZmM4YTJhM2ExNTJkNjQ2YTViZWE2OTRhNDI1YWI3OWRiNjk0YjIxNGYxNTZjMzdjNzE4M2FhIn19fQ==")
                                    .lore(
                                            Arrays.asList(
                                                    "&r",
                                                    " &7 " + settingEntry.getKey().desc() + " ",
                                                    "&r",
                                                    " &b➜ Click me "
                                            )
                                    )
                                    .event(event -> {
                                        event.setCancelled(true);

                                        Player player = (Player) event.getWhoClicked();

                                        ElysInventory elysInventory = (ElysInventory) event.getInventory().getHolder();
                                        if (elysInventory == null) return;

                                        SkullGUI skullGUI = elysInventory.get(event.getSlot());
                                        if (skullGUI == null) return;

                                        if (!hasPermission(player.getUniqueId(), IslandPermission.ISLAND_SETTING)) {
                                            MessageUtils.send(player, "&4&l(&c&l!&4&l) &cYou are not allowed to perform this action");
                                            player.closeInventory();

                                            return;
                                        }

                                        if (this.setting(settingEntry.getKey())) {
                                            skullGUI.base64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTMzYTViZmM4YTJhM2ExNTJkNjQ2YTViZWE2OTRhNDI1YWI3OWRiNjk0YjIxNGYxNTZjMzdjNzE4M2FhIn19fQ==");
                                            this.settingsMap.put(settingEntry.getKey(), false);
                                            MessageUtils.send(player, "&2&l(&a&l!&2&l) &aYou have disabled the " + settingEntry.getKey() + " setting");
                                        } else {
                                            skullGUI.base64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU0ODRmNGI2MzY3Yjk1YmIxNjI4ODM5OGYxYzhkZDZjNjFkZTk4OGYzYTgzNTZkNGMzYWU3M2VhMzhhNDIifX19");
                                            this.settingsMap.put(settingEntry.getKey(), true);
                                            MessageUtils.send(player, "&2&l(&a&l!&2&l) &aYou have enabled the " + settingEntry.getKey() + " setting");
                                        }

                                        elysInventory.refresh();
                                    })
                            );
                    counter++;
                }
                return settingInventory;
            default:
                throw new IllegalStateException("Unexpected value: " + islandInventory);
        }
    }

    /*
     * CATEGORY ( OTHER )
     */
    public List<Player> onlinePlayers() {
        List<Player> players = new ArrayList<>();
        membersMap.keySet().forEach(playerUUID -> {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player != null) players.add(player);
        });

        return players;
    }

    public void reload() {
        this.xMin = coreLocation.getBlockX() - IslandUpgrade.SIZE.radius(this.upgradesMap.get(IslandUpgrade.SIZE));
        this.xMax = coreLocation.getBlockX() + IslandUpgrade.SIZE.radius(this.upgradesMap.get(IslandUpgrade.SIZE));
        this.zMin = coreLocation.getBlockZ() - IslandUpgrade.SIZE.radius(this.upgradesMap.get(IslandUpgrade.SIZE));
        this.zMax = coreLocation.getBlockZ() + IslandUpgrade.SIZE.radius(this.upgradesMap.get(IslandUpgrade.SIZE));
    }

    /*
     *  CATEGORY  ( MEMBERS )
     */
    public int totalMembers() {
        return membersMap.size();
    }

    public int maxMembers() {
        return IslandUpgrade.MEMBERS.members(upgradesMap.get(IslandUpgrade.MEMBERS));
    }

    public boolean isMember(UUID playerUUID) {
        return membersMap.containsKey(playerUUID);
    }

    public void addMember(UUID playerUUID) {
        membersMap.put(playerUUID, IslandRole.DEFAULT);
        invitesMap.remove(playerUUID);
    }

    public void removeMember(UUID playerUUID) {
        membersMap.remove(playerUUID);
    }

    public void send(UUID playerUUID, UUID targetUUID) {
        invitesMap.put(targetUUID, playerUUID);

        TaskUtils.async(plugin, () -> invitesMap.remove(targetUUID), 600);
    }

    public boolean check(UUID playerUUID) {
        return invitesMap.containsKey(playerUUID);
    }

    /*
     *  CATEGORY ( PERMISSION / ROLE )
     */
    public IslandRole role(UUID playerUUID) {
        return membersMap.get(playerUUID);
    }

    public boolean hasPermission(UUID playerUUID, IslandPermission permission) {
        return membersMap.get(playerUUID).has(permission);
    }

    public void promote(UUID playerUUID) {
        membersMap.put(playerUUID, membersMap.get(playerUUID).next());
    }

    public void demote(UUID playerUUID) {
        membersMap.put(playerUUID, membersMap.get(playerUUID).back());
    }

    /*
     * CATEGORY ( SETTINGS )
     */
    public boolean setting(IslandSetting islandSetting) {
        return settingsMap.get(islandSetting);
    }

    /*
     * CATEGORY ( LOCATIONS )
     */
    public boolean isPart(Location location) {
        return location.getBlockX() >= xMin && location.getBlockX() <= xMax && location.getBlockZ() >= zMin && location.getBlockZ() <= zMax;
    }

    /*
     * CATEGORY ( SAVING )
     */
    public void delete() {
        plugin.getIdbHandler().delete(this).whenComplete(
                (integer, throwable) -> {
                    if (throwable != null) throwable.printStackTrace();
                }
        );

        plugin.getIslandHandler().remove(identifier);
    }

    public void save() {
        plugin.getIdbHandler().present(identifier).whenComplete(
                (integer, throwable) -> {
                    if (throwable != null) throwable.printStackTrace();
                    if (integer == null) {
                        plugin.getIdbHandler().insert(this).whenComplete(
                                (integer1, throwable1) -> {
                                    if (throwable1 != null) throwable1.printStackTrace();

                                    plugin.getIslandHandler().remove(identifier);
                                }
                        );
                    } else plugin.getIdbHandler().update(this).whenComplete(
                            (integer1, throwable1) -> {
                                if (throwable1 != null) throwable1.printStackTrace();

                                plugin.getIslandHandler().remove(identifier);
                            }
                    );
                 }
        );
    }

}
