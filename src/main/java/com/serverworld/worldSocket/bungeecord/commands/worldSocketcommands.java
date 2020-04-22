package com.serverworld.worldSocket.bungeecord.commands;

import com.serverworld.worldSocket.bungeecord.*;
import com.serverworld.worldSocket.bungeecord.util.messagecoder;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class worldSocketcommands extends Command{
    worldSocket worldsocket;

    public worldSocketcommands(worldSocket worldSocket){
        super("worldsocket");
        this.worldsocket = worldSocket;

    }
    public void execute(CommandSender commandSender, String[] strings) {
        messagecoder messagecoder=new messagecoder();
        messagecoder.setSender("worldsocket");
        messagecoder.setReceiver("ALL");
        messagecoder.setType("TEST");
        messagecoder.setChannel("test");
        messagecoder.setMessage("test message測試");
        worldsocket.sendmessage(messagecoder.createmessage());
    }
}
