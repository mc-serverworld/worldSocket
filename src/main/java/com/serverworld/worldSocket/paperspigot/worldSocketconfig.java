package com.serverworld.worldSocket.paperspigot;

import com.serverworld.worldSocket.paperspigot.worldSocket;
public class worldSocketconfig {
    private worldSocket plugin;

    public worldSocketconfig(worldSocket i){
        plugin = i;
    }
    public void loadDefConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
    }
    public int threads() {return plugin.getConfig().getInt("socketserver.threads");}
    public int serverport() {return plugin.getConfig().getInt("socketserver.port");}
    public String name() {return plugin.getConfig().getString("client.name");}
    public String host() {return plugin.getConfig().getString("client.host");}
    public int clientport() {return plugin.getConfig().getInt("client.port");}
    public boolean debug() {return plugin.getConfig().getBoolean("debug");}
}
