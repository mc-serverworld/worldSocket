package com.serverworld.worldSocket.paperspigot.commands;

import com.serverworld.worldSocket.paperspigot.worldSocket;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.json.JSONObject;

public class worldSocketCommands implements CommandExecutor {
    worldSocket worldsocket;
    public worldSocketCommands(worldSocket worldSocket){worldsocket=worldSocket;}
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        try {
            return true;
        }catch (Exception e){
            commandSender.sendMessage(ChatColor.RED + "Error!");
            e.printStackTrace();
            return false;
        }
    }
}
