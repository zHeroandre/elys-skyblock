package me.zheroandre.elysskyblock.handler.island;

import lombok.Getter;
import me.zheroandre.elysskyblock.ElysSkyBlockPlugin;
import me.zheroandre.elysskyblock.builder.inventory.ElysInventory;
import me.zheroandre.elysskyblock.builder.item.SkullGUI;
import me.zheroandre.elysskyblock.enums.island.IslandRole;
import me.zheroandre.elysskyblock.enums.island.IslandSetting;
import me.zheroandre.elysskyblock.enums.island.IslandUpgrade;
import me.zheroandre.elysskyblock.generator.world.VoidGenerator;
import me.zheroandre.elysskyblock.objects.island.EIsland;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class IslandHandler {

    private final ElysSkyBlockPlugin plugin;

    public final World islandWorld;
    private final Location startLocation;

    @Getter private final ElysInventory createInventory;
    @Getter private final Map<Integer, EIsland> islandsMap = new LinkedHashMap<>();

    public IslandHandler(ElysSkyBlockPlugin plugin) {
        this.plugin = plugin;

        this.islandWorld = new WorldCreator("islands-world").generator(new VoidGenerator()).createWorld();
        this.startLocation = new Location(islandWorld, 0, 63, 0, (float) -90.14915, (float) 15.599821);

        this.createInventory = new ElysInventory(InventoryType.DISPENSER, "&r")
                .fill(new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
                .set(
                        4, new SkullGUI()
                                .name(" &b&lCREATE ISLAND ")
                                .lore(
                                        Arrays.asList(
                                                "&r",
                                                " &7Create your island and start the adventure ",
                                                "&r",
                                                " &b➜ Click me "
                                        )
                                )
                                .base64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWY1ZjE1OTg4NmNjNTMxZmZlYTBkOGFhNWY5MmVkNGU1ZGE2NWY3MjRjMDU3MGFmODZhOTBiZjAwYzY3YzQyZSJ9fX0=")
                                .event(event -> {
                                    Player player = (Player) event.getWhoClicked();
                                    if (!this.has(player)) this.initialize(player.getUniqueId());

                                    event.setCancelled(true);
                                    player.closeInventory();
                                })
                );
    }

    private int last() {
        if (islandsMap.size() == 0) return 0;

        List<Map.Entry<Integer, EIsland>> entryList = new ArrayList<>(islandsMap.entrySet());
        Map.Entry<Integer, EIsland> lastEntry = entryList.get(entryList.size() - 1);

        return lastEntry.getValue().getIdentifier() + 1;
    }

    public void initialize(UUID ownerUUID) {
        int identifier = last();

        EIsland island = new EIsland(plugin, identifier, ownerUUID, startLocation.clone().add(identifier * 1000, 1, 0));
        islandsMap.put(identifier, island);
    }

    public EIsland initialize(int identifier, UUID owner, Map<UUID, IslandRole> membersMap, Location coreLocation, Location spawnLocation, Map<IslandUpgrade, Integer> upgradesMap, Map<IslandSetting, Boolean> settingsMap, int level, double points, int token, boolean creating) {
        return islandsMap.put(identifier, new EIsland(plugin, identifier, owner, membersMap, coreLocation, spawnLocation, upgradesMap, settingsMap, level, points, token, creating));
    }

    public void remove(int identifier) {
        islandsMap.remove(identifier);
    }

    public boolean has(Player player) {
        for (EIsland island : islandsMap.values()) {
            if (island.isMember(player.getUniqueId())) return true;
        }
        return false;
    }

    public boolean has(Location location) {
        for (EIsland island : islandsMap.values()) {
            if (island.isPart(location)) return true;
        }
        return false;
    }

    public boolean has(Block block) { // TODO - Radius
        BlockFace[] blockFaces = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};

        for (BlockFace blockFace : blockFaces) {
            if (this.has(block.getRelative(blockFace).getLocation())) return true;
        }
        return false;
    }

    public EIsland get(Player player) {
        for (EIsland island : islandsMap.values()) {
            if (island.isMember(player.getUniqueId())) return island;
        }
        return null;
    }

    public EIsland get(Location location) {
        for (EIsland island : islandsMap.values()) {
            if (island.isPart(location)) return island;
        }
        return null;
    }

    public EIsland get(Block block) { // TODO - Radius
        BlockFace[] blockFaces = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};

        for (BlockFace blockFace : blockFaces) {
            if (this.has(block.getRelative(blockFace).getLocation())) return this.get(block.getRelative(blockFace).getLocation());
        }
        return null;
    }

}
