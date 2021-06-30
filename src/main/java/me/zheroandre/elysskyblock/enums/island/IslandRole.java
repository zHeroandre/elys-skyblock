package me.zheroandre.elysskyblock.enums.island;

import java.util.Arrays;
import java.util.List;

public enum IslandRole {
    OWNER(Arrays.asList(
            IslandPermission.ISLAND_PLACE,
            IslandPermission.ISLAND_BREAK,
            IslandPermission.ISLAND_INVITE_MEMBER,
            IslandPermission.ISLAND_KICK_MEMBER,
            IslandPermission.ISLAND_PROMOTE_MEMBER,
            IslandPermission.ISLAND_DEMOTE_MEMBER,
            IslandPermission.ISLAND_BAN,
            IslandPermission.ISLAND_UNBAN,
            IslandPermission.ISLAND_UPGRADE,
            IslandPermission.ISLAND_SETTING,
            IslandPermission.ISLAND_SETTING_SPAWN
    )),
    MODERATOR(Arrays.asList(
            IslandPermission.ISLAND_PLACE,
            IslandPermission.ISLAND_BREAK,
            IslandPermission.ISLAND_INVITE_MEMBER,
            IslandPermission.ISLAND_UPGRADE,
            IslandPermission.ISLAND_SETTING_SPAWN
    )),
    DEFAULT(Arrays.asList(
            IslandPermission.ISLAND_PLACE,
            IslandPermission.ISLAND_BREAK
    ));


    private final List<IslandPermission> PERMISSIONS;

    IslandRole(List<IslandPermission> PERMISSIONS) {
        this.PERMISSIONS = PERMISSIONS;
    }

    public boolean has(IslandPermission permission) {
        return this.PERMISSIONS.contains(permission);
    }

    public IslandRole next() {
        if (this == DEFAULT) return MODERATOR;
        if (this == MODERATOR) return OWNER;

        return null;
    }

    public IslandRole back() {
        if (this == MODERATOR) return DEFAULT;
        if (this == OWNER) return MODERATOR;

        return null;
    }

}
