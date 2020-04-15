package com.serverworld.worldSocket.bungeecord;

public class worldSocketconfig {
    private worldSocket plugin;

    public worldSocketconfig(worldSocket i){
        plugin = i;
    }
    public void loadDefConfig(){ }

    //general
    public int port() {return plugin.configuration.getInt("general.port");}
    public String password() {return plugin.configuration.getString("general.password");}

    //server
    public int threads() {return plugin.configuration.getInt("socketserver.threads");}

    //client
    public String name() {return plugin.configuration.getString("client.name");}
    public String host() {return plugin.configuration.getString("client.host");}

    //configinfo
    public int api_version() {return plugin.configuration.getInt("configinfo.api-version");}
    public boolean debug() {return plugin.configuration.getBoolean("configinfo.debug");}

    //SSL
    public boolean useSSL() {return plugin.configuration.getBoolean("SSL.useSSL");}
    public String keyStore_file() {return plugin.configuration.getString("SSL.keyStore_file");}
    public String keyStorePassword() {return plugin.configuration.getString("SSL.keyStorePassword");}
    public String trustStore_file() {return plugin.configuration.getString("SSL.trustStore_file");}
    public String trustStorePassword() {return plugin.configuration.getString("SSL.trustStorePassword");}
}
