package com.serverworld.worldSocket.paperspigot;

import com.serverworld.worldSocket.paperspigot.commands.worldSocketCommands;
import com.serverworld.worldSocket.paperspigot.worldSocketconfig;
import com.serverworld.worldSocket.paperspigot.socket.socketclient;
import org.bukkit.plugin.java.JavaPlugin;


public class worldSocket extends JavaPlugin {

    public worldSocketconfig config;
    public socketclient client;


    public void onLoad() {
        config = new worldSocketconfig(this);
    }

    @Override
    public void onEnable() {
        config.loadDefConfig();

        client = new socketclient(this);
        client.startlogin();

        this.getCommand("buntest").setExecutor(new worldSocketCommands(this)); //test socket
    }
}
