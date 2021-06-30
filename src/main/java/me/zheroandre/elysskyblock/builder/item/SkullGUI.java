package me.zheroandre.elysskyblock.builder.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.zheroandre.elysskyblock.item.event.Event;
import me.zheroandre.elysskyblock.utils.string.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

public class SkullGUI {

    private final ItemStack itemStack;
    private Event event;

    public SkullGUI() {
        itemStack = new ItemStack(Material.PLAYER_HEAD);
    }

    public SkullGUI name(String string) {
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        if (skullMeta == null) return this;

        skullMeta.setDisplayName(StringUtils.color(string));
        itemStack.setItemMeta(skullMeta);

        return this;
    }

    public SkullGUI lore(List<String> strings) {
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        if (skullMeta == null) return this;

        skullMeta.setLore(StringUtils.color(strings));
        itemStack.setItemMeta(skullMeta);

        return this;
    }

    public SkullGUI owner(UUID uuid) {
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        if (skullMeta == null) return this;

        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
        itemStack.setItemMeta(skullMeta);

        return this;
    }

    public SkullGUI base64(String string) {
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        if (string.isEmpty() || skullMeta == null) return this;

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
        gameProfile.getProperties().put("textures", new Property("textures", string));

        try {
            Method method = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
            method.setAccessible(true);
            method.invoke(skullMeta, gameProfile);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }
        itemStack.setItemMeta(skullMeta);

        return this;
    }

    public SkullGUI event(Event event) {
        this.event = event;

        return this;
    }

    public void write(InventoryClickEvent e) {
        event.write(e);
    }

    public ItemStack build() {
        return itemStack;
    }

}
