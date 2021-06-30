package me.zheroandre.elysskyblock.enums.island;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum IslandUpgrade {
    SIZE(5),
    MEMBERS(5);

    public int MAX_LEVEL;

    IslandUpgrade(int MAX_LEVEL) {
        this.MAX_LEVEL = MAX_LEVEL;
    }

    public int cost(int level) {
        if (this == SIZE) {
            switch (level) {
                case 2:
                    return 1;
                case 3:
                    return 2;
                case 4:
                    return 3;
                case 5:
                    return 4;
            }
        }

        if (this == MEMBERS) {
            switch (level) {
                case 2:
                    return 1;
                case 3:
                    return 2;
                case 4:
                    return 3;
                case 5:
                    return 4;
            }
        }

        return 0;
    }

    public int radius(int level) {
        if (this != SIZE) return 0;
        if (level > this.MAX_LEVEL) return 0;

        switch (level) {
            case 1:
                return 9;
            case 2:
                return 14;
            case 3:
                return 19;
            case 4:
                return 24;
            case 5:
                return 29;
        }

        return 0;
    }

    public int members(int level) {
        if (this != MEMBERS) return 0;
        if (level > this.MAX_LEVEL) return 0;

        switch (level) {
            case 1:
                return 3;
            case 2:
                return 4;
            case 3:
                return 5;
            case 4:
                return 6;
            case 5:
                return 7;
        }

        return 0;
    }

    public List<String> lore(int level) {
        if (this == SIZE) {
            switch (level) {
                case 1:
                    return Arrays.asList(
                            "&r",
                            " &b&lADVANTAGES ",
                            "&r",
                            " &b▪ &7Ability to place in a " + IslandUpgrade.SIZE.radius(2) + "*" + IslandUpgrade.SIZE.radius(2) + " radius&r      ",
                            " &b▪ &7Ability to place in a " + IslandUpgrade.SIZE.radius(3) + "*" + IslandUpgrade.SIZE.radius(3) + " radius&r      ",
                            " &b▪ &7Ability to place in a " + IslandUpgrade.SIZE.radius(4) + "*" + IslandUpgrade.SIZE.radius(4) + " radius&r      ",
                            " &b▪ &7Ability to place in a " + IslandUpgrade.SIZE.radius(5) + "*" + IslandUpgrade.SIZE.radius(5) + " radius&r      ",
                            "&r",
                            " &7The next upgrade costs " + IslandUpgrade.SIZE.cost(level + 1)  + " tokens",
                            "&r",
                            " &b➜ Click me "
                    );
                case 2:
                    return Arrays.asList(
                            "&r",
                            " &b&lADVANTAGES ",
                            "&r",
                            " &b▪ &b&mAbility to place in a " + IslandUpgrade.SIZE.radius(2) + "*" + IslandUpgrade.SIZE.radius(2) + " radius&r      ",
                            " &b▪ &7Ability to place in a " + IslandUpgrade.SIZE.radius(3) + "*" + IslandUpgrade.SIZE.radius(3) + " radius&r      ",
                            " &b▪ &7Ability to place in a " + IslandUpgrade.SIZE.radius(4) + "*" + IslandUpgrade.SIZE.radius(4) + " radius&r      ",
                            " &b▪ &7Ability to place in a " + IslandUpgrade.SIZE.radius(5) + "*" + IslandUpgrade.SIZE.radius(5) + " radius&r      ",
                            "&r",
                            " &7The next upgrade costs " + IslandUpgrade.SIZE.cost(level + 1)  + " tokens",
                            "&r",
                            " &b➜ Click me "
                    );
                case 3:
                    return Arrays.asList(
                            "&r",
                            " &b&lADVANTAGES ",
                            "&r",
                            " &b▪ &b&mAbility to place in a " + IslandUpgrade.SIZE.radius(2) + "*" + IslandUpgrade.SIZE.radius(2) + " radius&r      ",
                            " &b▪ &b&mAbility to place in a " + IslandUpgrade.SIZE.radius(3) + "*" + IslandUpgrade.SIZE.radius(3) + " radius&r      ",
                            " &b▪ &7Ability to place in a " + IslandUpgrade.SIZE.radius(4) + "*" + IslandUpgrade.SIZE.radius(4) + " radius&r      ",
                            " &b▪ &7Ability to place in a " + IslandUpgrade.SIZE.radius(5) + "*" + IslandUpgrade.SIZE.radius(5) + " radius&r      ",
                            "&r",
                            " &7The next upgrade costs " + IslandUpgrade.SIZE.cost(level + 1)  + " tokens",
                            "&r",
                            " &b➜ Click me "
                    );
                case 4:
                    return Arrays.asList(
                            "&r",
                            " &b&lADVANTAGES ",
                            "&r",
                            " &b▪ &b&mAbility to place in a " + IslandUpgrade.SIZE.radius(2) + "*" + IslandUpgrade.SIZE.radius(2) + " radius&r      ",
                            " &b▪ &b&mAbility to place in a " + IslandUpgrade.SIZE.radius(3) + "*" + IslandUpgrade.SIZE.radius(3) + " radius&r      ",
                            " &b▪ &b&mAbility to place in a " + IslandUpgrade.SIZE.radius(4) + "*" + IslandUpgrade.SIZE.radius(4) + " radius&r      ",
                            " &b▪ &7Ability to place in a " + IslandUpgrade.SIZE.radius(5) + "*" + IslandUpgrade.SIZE.radius(5) + " radius&r      ",
                            "&r",
                            " &7The next upgrade costs " + IslandUpgrade.SIZE.cost(level + 1)  + " tokens",
                            "&r",
                            " &b➜ Click me "
                    );
                case 5:
                     return Arrays.asList(
                             "&r",
                             " &b&lADVANTAGES ",
                             "&r",
                             " &b▪ &b&mAbility to place in a " + IslandUpgrade.SIZE.radius(2) + "*" + IslandUpgrade.SIZE.radius(2) + " radius&r      ",
                             " &b▪ &b&mAbility to place in a " + IslandUpgrade.SIZE.radius(3) + "*" + IslandUpgrade.SIZE.radius(3) + " radius&r      ",
                             " &b▪ &b&mAbility to place in a " + IslandUpgrade.SIZE.radius(4) + "*" + IslandUpgrade.SIZE.radius(4) + " radius&r      ",
                             " &b▪ &b&mAbility to place in a " + IslandUpgrade.SIZE.radius(5) + "*" + IslandUpgrade.SIZE.radius(5) + " radius&r      ",
                             "&r",
                             " &cYou have reached the maximum level",
                             "&r"
                     );
                default: return new ArrayList<>();
            }
        }

        if (this == MEMBERS) {
            switch (level) {
                case 1:
                    return Arrays.asList(
                            "&r",
                            " &b&lADVANTAGES ",
                            "&r",
                            "&b▪ &7Possibility to invite " + IslandUpgrade.MEMBERS.members(2) + " total players&r     ",
                            "&b▪ &7Possibility to invite " + IslandUpgrade.MEMBERS.members(3) + " total players&r     ",
                            "&b▪ &7Possibility to invite " + IslandUpgrade.MEMBERS.members(4) + " total players&r     ",
                            "&b▪ &7Possibility to invite " + IslandUpgrade.MEMBERS.members(5) + " total players&r     ",
                            "&r",
                            " &7The next upgrade costs " + IslandUpgrade.MEMBERS.cost(level + 1)  + " tokens",
                            "&r",
                            " &b➜ Click me "
                    );
                case 2:
                    return Arrays.asList(
                            "&r",
                            " &b&lADVANTAGES ",
                            "&r",
                            "&b▪ &b&mPossibility to invite " + IslandUpgrade.MEMBERS.members(2) + " total players&r     ",
                            "&b▪ &7Possibility to invite " + IslandUpgrade.MEMBERS.members(3) + " total players&r     ",
                            "&b▪ &7Possibility to invite " + IslandUpgrade.MEMBERS.members(4) + " total players&r     ",
                            "&b▪ &7Possibility to invite " + IslandUpgrade.MEMBERS.members(5) + " total players&r     ",
                            "&r",
                            " &7The next upgrade costs " + IslandUpgrade.MEMBERS.cost(level + 1)  + " tokens",
                            "&r",
                            " &b➜ Click me "
                    );
                case 3:
                    return Arrays.asList(
                            "&r",
                            " &b&lADVANTAGES ",
                            "&r",
                            "&b▪ &b&mPossibility to invite " + IslandUpgrade.MEMBERS.members(2) + " total players&r     ",
                            "&b▪ &b&mPossibility to invite " + IslandUpgrade.MEMBERS.members(3) + " total players&r     ",
                            "&b▪ &7Possibility to invite " + IslandUpgrade.MEMBERS.members(4) + " total players&r     ",
                            "&b▪ &7Possibility to invite " + IslandUpgrade.MEMBERS.members(5) + " total players&r     ",
                            "&r",
                            " &7The next upgrade costs " + IslandUpgrade.MEMBERS.cost(level + 1)  + " tokens",
                            "&r",
                            " &b➜ Click me "
                    );
                case 4:
                    return Arrays.asList(
                            "&r",
                            " &b&lADVANTAGES ",
                            "&r",
                            "&b▪ &b&mPossibility to invite " + IslandUpgrade.MEMBERS.members(2) + " total players&r     ",
                            "&b▪ &b&mPossibility to invite " + IslandUpgrade.MEMBERS.members(3) + " total players&r     ",
                            "&b▪ &b&mPossibility to invite " + IslandUpgrade.MEMBERS.members(4) + " total players&r     ",
                            "&b▪ &7Possibility to invite " + IslandUpgrade.MEMBERS.members(5) + " total players&r     ",
                            "&r",
                            " &7The next upgrade costs " + IslandUpgrade.MEMBERS.cost(level + 1)  + " tokens",
                            "&r",
                            " &b➜ Click me "
                    );
                case 5:
                    return Arrays.asList(
                            "&r",
                            " &b&lADVANTAGES ",
                            "&r",
                            "&b▪ &b&mPossibility to invite " + IslandUpgrade.MEMBERS.members(2) + " total players&r     ",
                            "&b▪ &b&mPossibility to invite " + IslandUpgrade.MEMBERS.members(3) + " total players&r     ",
                            "&b▪ &b&mPossibility to invite " + IslandUpgrade.MEMBERS.members(4) + " total players&r     ",
                            "&b▪ &b&mPossibility to invite " + IslandUpgrade.MEMBERS.members(5) + " total players&r     ",
                            "&r",
                            " &cYou have reached the maximum level",
                            "&r"
                    );
                default: return new ArrayList<>();
            }
        }
        return null;
    }

}
