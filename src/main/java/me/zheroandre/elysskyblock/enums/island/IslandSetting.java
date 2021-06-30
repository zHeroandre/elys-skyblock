package me.zheroandre.elysskyblock.enums.island;

public enum IslandSetting {
    ISLAND_VISIT;

    public String desc() {
        if (this == ISLAND_VISIT) return "Enable or disable player visits";

        return "";
    }

    public String string() {
        if (this == ISLAND_VISIT) return "PLAYER_VISIT";

        return "";
    }

}
