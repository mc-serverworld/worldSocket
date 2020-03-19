package com.serverworld.worldSocket.paperspigot.commands;

import com.serverworld.worldSocket.paperspigot.worldSocket;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class worldSocketCommands implements CommandExecutor {
    worldSocket worldsocket;
    public worldSocketCommands(worldSocket pl){worldsocket=pl;}
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return true;
    }
}
