package com.serverworld.worldSocket.bungeecord.commands;

import com.serverworld.worldSocket.bungeecord.*;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

public class worldSocketcommands extends Command{
    worldSocket plugin;
    public worldSocketcommands(Plugin pl,worldSocket worldSocket){
        super("worldsocket");
        plugin = worldSocket;

    }
    public void execute(CommandSender commandSender, String[] strings) {
    }
}
