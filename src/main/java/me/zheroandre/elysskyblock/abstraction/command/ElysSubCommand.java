package me.zheroandre.elysskyblock.abstraction.command;

import lombok.Getter;
import org.bukkit.command.CommandSender;

public abstract class ElysSubCommand {

    @Getter private final String name;

    public ElysSubCommand(String name) {
        this.name = name;
    }

    public abstract boolean command(CommandSender sender, String[] args);

}
