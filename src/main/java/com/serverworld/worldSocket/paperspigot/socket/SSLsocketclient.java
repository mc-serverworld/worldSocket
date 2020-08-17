package com.serverworld.worldSocket.paperspigot.socket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.serverworld.worldSocket.paperspigot.util.socketloginer;
import com.serverworld.worldSocket.paperspigot.worldSocket;
import net.md_5.bungee.api.ChatColor;
import org.json.JSONObject;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.KeyStore;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SSLsocketclient {
    private worldSocket worldsocket;
    private login loginer = new login();
    private sender sender = new sender();



    private String CLIENT_KEY_STORE_FILE;
    private String CLIENT_TRUST_KEY_STORE_FILE;
    private String CLIENT_KEY_STORE_PASSWORD;
    private String CLIENT_TRUST_KEY_STORE_PASSWORD;

    static SSLSocket socket;
    //private static Scanner in;
    //private static PrintWriter out;

    static ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();
    private boolean shouldStop=false;

    public SSLsocketclient(worldSocket worldsocket){
        this.worldsocket=worldsocket;
    }
    public void startlogin(){
        loginer.start();
    }
    public void closesocket(){
        shouldStop=true;
    }

    class login extends Thread{
        @Override
        public void run() {
            try {
                SSLContext ctx;
                KeyManagerFactory kmf;
                TrustManagerFactory tmf;
                KeyStore ks;
                KeyStore tks;

                CLIENT_KEY_STORE_FILE = worldsocket.config.client_keyStore_file();
                CLIENT_TRUST_KEY_STORE_FILE = worldsocket.config.client_trustStore_file();
                CLIENT_KEY_STORE_PASSWORD = worldsocket.config.client_keyStore_password();
                CLIENT_TRUST_KEY_STORE_PASSWORD = worldsocket.config.client_trustStore_password();

                ctx = SSLContext.getInstance("SSL");

                kmf = KeyManagerFactory.getInstance("SunX509");
                tmf = TrustManagerFactory.getInstance("SunX509");

                ks = KeyStore.getInstance("JKS");
                tks = KeyStore.getInstance("JKS");

                ks.load(new FileInputStream(CLIENT_KEY_STORE_FILE), CLIENT_KEY_STORE_PASSWORD.toCharArray());
                tks.load(new FileInputStream(CLIENT_TRUST_KEY_STORE_FILE), CLIENT_TRUST_KEY_STORE_PASSWORD.toCharArray());

                kmf.init(ks, CLIENT_KEY_STORE_PASSWORD.toCharArray());
                tmf.init(tks);

                ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
                if(worldsocket.config.debug())
                    worldsocket.getLogger().info("SSL okay");
                socket = (SSLSocket) (ctx.getSocketFactory().createSocket(worldsocket.config.host(),worldsocket.config.port()));
                socket.setSoTimeout(300);
                socket.setNeedClientAuth(worldsocket.config.forceSSL());
                Scanner in = new Scanner(socket.getInputStream());
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                socketloginer socketloginer = new socketloginer();
                socketloginer.setName(worldsocket.config.name().toString());
                socketloginer.setPassword(worldsocket.config.password().toString());
                out.println(socketloginer.createmessage().toString());
                out.flush();
                if(worldsocket.config.debug())
                    worldsocket.getLogger().info("send login msg: " + socketloginer.createmessage());
                while (true){
                    in = new Scanner(socket.getInputStream());
                    if (in.hasNextLine()) {
                        String message = in.nextLine();
                        if (worldsocket.config.debug()) {worldsocket.getLogger().info("received: " + message);}
                        if(message.equals("ACCEPTED")){
                            worldsocket.getLogger().info(ChatColor.GREEN + "Connect to socket server!");
                        }else if(message.equals("ERROR:NAME_USED")) {
                            worldsocket.getLogger().warning(ChatColor.RED + "The name has been used!");
                        }else if(message.equals("ERROR:WRONG_PASSWORD")){
                            worldsocket.getLogger().warning(ChatColor.RED + "Wrong password!");
                        }else if(message.equals("CHECK:ONLINE")){
                            worldsocket.checker.clearlist();
                        } else {
                            JsonParser jsonParser = new JsonParser();
                            JsonObject jsonmsg = jsonParser.parse(message).getAsJsonObject();
                            JSONObject json = new JSONObject(message);
                            if(worldsocket.config.debug())
                                worldsocket.getLogger().info(json.toString());
                            if(json.getString("receiver").toLowerCase().equals(worldsocket.config.name().toLowerCase()) && !json.getString("type").toLowerCase().equals("socketapi")){
                                worldsocket.eventsender.addeventqueue(message);
                                if(worldsocket.config.debug()){
                                    worldsocket.getLogger().info("Event Fired");
                                    //worldsocket.getLogger().info("Event Fired " + "message: " + "sender: " + json.getString("sender") + " receiver: " + json.getString("receiver") + " channel: " + json.getString("channel") + " type: " + json.getString("type"));
                                }
                            }else if(json.getString("receiver").toLowerCase().equals("all")&& !json.getString("type").toLowerCase().equals("socketapi")){
                                worldsocket.eventsender.addeventqueue(message);
                                if(worldsocket.config.debug()){
                                    worldsocket.getLogger().info("Event Fired");
                                    //worldsocket.getLogger().info("Event Fired " + "message: " + "sender: " + json.getString("sender") + " receiver: " + json.getString("receiver") + " channel: " + json.getString("channel") + " type: " + json.getString("type"));
                                }


                            }else if(json.getString("type").toLowerCase().equals("socketapi")){

                            }else
                                if (worldsocket.config.debug())
                                worldsocket.getLogger().info(ChatColor.YELLOW + "Unknow message");
                        }

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                worldsocket.getLogger().warning(ChatColor.RED + "Error while connect to socket server");
            }
        }
    }

    public void sendmessage(String message){
        queue.add(message);
        sender = new sender();
        sender.start();
    }

    private class sender extends Thread {
        public void run() {
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                    synchronized (queue) {
                        if (!queue.isEmpty()) {
                            for (String stuff : queue) {
                                //TODO create event
                                out.println(stuff);
                                out.flush();
                                if (worldsocket.config.debug()) {
                                    worldsocket.getLogger().info("send: " + stuff);
                                }
                            }
                            queue.clear();
                        }
                    }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
