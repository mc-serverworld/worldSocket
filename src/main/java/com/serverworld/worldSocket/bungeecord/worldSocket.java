package com.serverworld.worldSocket.bungeecord;

import com.serverworld.worldSocket.bungeecord.Listeners.Messagecoming;
import com.serverworld.worldSocket.bungeecord.commands.*;
import com.serverworld.worldSocket.bungeecord.socket.SSLsocketserver;
import com.serverworld.worldSocket.bungeecord.socket.socketserver;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class worldSocket extends Plugin {
    //config
    public static Configuration configuration;
    public worldSocketconfig config;
    private File file;

    public socketserver socketserver;
    public SSLsocketserver SSLsocketserver;

    @Override
    public void onEnable() {
        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        config = new worldSocketconfig(this);
        if(config.useSSL()){
            SSLsocketserver = new SSLsocketserver(this);
            SSLsocketserver.start();
        }else {
            socketserver = new socketserver(this);
            socketserver.start();
        }
        //getProxy().getPluginManager().registerCommand(this,new worldSocketcommands(this));
        new Messagecoming(this);
    }
    public void sendmessage(String msg){
        if(config.useSSL()){
            SSLsocketserver.sendmessage(msg);
        }else {
            socketserver.sendmessage(msg);
        }
    }
}
