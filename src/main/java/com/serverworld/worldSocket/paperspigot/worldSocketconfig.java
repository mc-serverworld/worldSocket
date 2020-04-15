package com.serverworld.worldSocket.paperspigot;

import com.serverworld.worldSocket.paperspigot.worldSocket;
public class worldSocketconfig {
    private com.serverworld.worldSocket.paperspigot.worldSocket plugin;

    public worldSocketconfig(com.serverworld.worldSocket.paperspigot.worldSocket i){
        plugin = i;
    }
    public void loadDefConfig(){ }

    //general
    public int port() {return plugin.getConfig().getInt("general.port");}
    public String password() {return plugin.getConfig().getString("general.password");}

    //server
    public int threads() {return plugin.getConfig().getInt("socketserver.threads");}

    //client
    public String name() {return plugin.getConfig().getString("client.name");}
    public String host() {return plugin.getConfig().getString("client.host");}

    //configinfo
    public int api_version() {return plugin.getConfig().getInt("configinfo.api-version");}
    public boolean debug() {return plugin.getConfig().getBoolean("configinfo.debug");}

    //SSL
    public boolean useSSL() {return plugin.getConfig().getBoolean("SSL.useSSL");}
    public String keyStore_file() {return plugin.getConfig().getString("SSL.keyStore_file");}
    public String keyStorePassword() {return plugin.getConfig().getString("SSL.keyStorePassword");}
    public String trustStore_file() {return plugin.getConfig().getString("SSL.trustStore_file");}
    public String trustStorePassword() {return plugin.getConfig().getString("SSL.trustStorePassword");}
}
