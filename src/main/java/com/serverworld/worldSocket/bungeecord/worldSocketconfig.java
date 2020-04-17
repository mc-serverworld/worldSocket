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
    public String server_keyStore_file() {return plugin.configuration.getString("SSL.server.keyStore_file");}
    public String server_trustStore_file() {return plugin.configuration.getString("SSL.server.trustStore_file");}
    public String server_keyStore_password() {return plugin.configuration.getString("SSL.server.keyStorePassword");}
    public String server_trustStore_password() {return plugin.configuration.getString("SSL.server.trustStorePassword");}

    public String client_keyStore_file() {return plugin.configuration.getString("SSL.client.keyStore_file");}
    public String client_trustStore_file() {return plugin.configuration.getString("SSL.client.trustStore_file");}
    public String client_keyStore_password() {return plugin.configuration.getString("SSL.client.keyStorePassword");}
    public String client_trustStore_password() {return plugin.configuration.getString("SSL.client.trustStorePassword");}
}
