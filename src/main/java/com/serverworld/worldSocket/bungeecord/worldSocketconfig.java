package com.serverworld.worldSocket.bungeecord;

public class worldSocketconfig {
    private worldSocket plugin;

    public worldSocketconfig(worldSocket i){
        plugin = i;
    }
    public void loadDefConfig(){ }
    public int threads() {return plugin.configuration.getInt("socketserver.threads");}
    public int serverport() {return plugin.configuration.getInt("socketserver.port");}
    public String name() {return plugin.configuration.getString("client.name");}
    public String host() {return plugin.configuration.getString("client.host");}
    public int clientport() {return plugin.configuration.getInt("client.port");}
    public boolean debug() {return plugin.configuration.getBoolean("debug");}

}
