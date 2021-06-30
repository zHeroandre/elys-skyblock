package me.zheroandre.elysskyblock.abstraction.command;

import me.zheroandre.elysskyblock.utils.message.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class ElysCommand extends Command {

    public boolean console;
    public String permission;

    private final Map<String, ElysSubCommand> subCommandMap = new HashMap<>();

    protected ElysCommand(String name) {
        this(name, "", "/" + name, new ArrayList<>());
    }

    protected ElysCommand(String name, String description, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases);

        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            ((CommandMap) bukkitCommandMap.get(Bukkit.getServer())).register(this.getName(), this);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!console && sender instanceof ConsoleCommandSender) {
            MessageUtils.send(sender, "&4&l(&c&l!&4&l) &cThis command is only executable by player");
            // TO COMPLETE
            return true;
        }

        if (!permission.isEmpty() && !sender.hasPermission(permission)) {
            MessageUtils.send(sender, "Unknown command. Type \"/help\" for help.");
            // TO COMPLETE
            return true;
        }

        if (args.length > 0) {
            if (!subCommandMap.containsKey(args[0].toLowerCase())) {
                MessageUtils.send(sender, "&4&l(&c&l!&4&l) &cThis subcommand does not exist, try again!");
                // TO COMPLETE
                return true;
            }

            return subCommandMap.get(args[0].toLowerCase()).command(sender, args);
        }

        return command(sender, args);
    }

    @Override @NotNull
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!console && sender instanceof ConsoleCommandSender || (!permission.isEmpty() && !sender.hasPermission(permission)) || args.length == 0) return new ArrayList<>();

        if (args.length == 1) {
            return new ArrayList<>(subCommandMap.keySet());
        }

        if (args.length == 2) {
            return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    public abstract boolean command(CommandSender sender, String[] args);

    public void addSubCommand(ElysSubCommand elysSubCommand) {
        subCommandMap.put(elysSubCommand.getName(), elysSubCommand);
    }

}
