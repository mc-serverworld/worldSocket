package com.serverworld.worldSocket.paperspigot.socket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.serverworld.worldSocket.paperspigot.util.socketloginer;
import com.serverworld.worldSocket.paperspigot.worldSocket;
import net.md_5.bungee.api.ChatColor;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyStore;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SSLsocketclient {
    private worldSocket worldsocket;
    private login loginer = new login();
    private receiver receiver = new receiver();
    private sender sender = new sender();

    private String CLIENT_KEY_STORE_FILE;
    private String CLIENT_TRUST_KEY_STORE_FILE;
    private String CLIENT_KEY_STORE_PASSWORD;
    private String CLIENT_TRUST_KEY_STORE_PASSWORD;

    static SSLSocket socket;
    private static Scanner in;
    private static PrintWriter out;

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
        SSLContext ctx;
        KeyManagerFactory kmf;
        TrustManagerFactory tmf;
        KeyStore ks;
        KeyStore tks;
        @Override
        public void run() {
            try {
                CLIENT_KEY_STORE_FILE = worldsocket.config.client_keyStore_file();
                CLIENT_TRUST_KEY_STORE_FILE = worldsocket.config.client_trustStore_file();
                CLIENT_KEY_STORE_PASSWORD = worldsocket.config.client_keyStore_password();
                CLIENT_TRUST_KEY_STORE_PASSWORD = worldsocket.config.client_keyStore_password();
                
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            try{
                socket = (SSLSocket) ctx.getSocketFactory().createSocket(worldsocket.config.host(),worldsocket.config.port());
                Scanner in = new Scanner(socket.getInputStream());
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                receiver.start();
                socketloginer socketloginer = new socketloginer();
                socketloginer.setName(worldsocket.config.name());
                socketloginer.setPassword(worldsocket.config.password());
                out.println(socketloginer.createmessage());
            } catch (IOException e) {
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
    private class receiver extends Thread {
        public void run() {
            try {
                in = new Scanner(socket.getInputStream());
                while (!shouldStop) {
                    synchronized (socket) {
                        if (in.hasNextLine()) {
                            String message = in.nextLine();
                            if (worldsocket.config.debug()) {worldsocket.getLogger().info("received: " + message);}
                            if(message.equals("ACCEPTED")){
                                worldsocket.getLogger().info(ChatColor.GREEN + "Connect to socket server!");
                            }else if(message.equals("ERROR:NAME_USED")) {
                                worldsocket.getLogger().warning(ChatColor.RED + "The name has been used!");
                            }else if(message.equals("ERROR:WRONG_PASSWORD")){
                                worldsocket.getLogger().warning(ChatColor.RED + "Wrong password!");
                            }else {
                                JsonParser jsonParser = new JsonParser();
                                JsonObject jsonmsg = jsonParser.parse(message).getAsJsonObject();
                                if(jsonmsg.get("receiver").getAsString().toLowerCase().equals(worldsocket.config.name()) && !jsonmsg.get("type").getAsString().toLowerCase().equals("socketapi")){
                                    worldsocket.eventsender.addeventqueue(message);
                                }else if(jsonmsg.get("receiver").getAsString().toLowerCase().equals("all")&& !jsonmsg.get("type").getAsString().toLowerCase().equals("socketapi")){
                                    worldsocket.eventsender.addeventqueue(message);
                                }else if(jsonmsg.get("type").getAsString().toLowerCase().equals("socketapi")){

                                }
                            }

                        }
                    }
                }
            } catch (IOException e) {e.printStackTrace();}
        }
    }

    private class sender extends Thread {
        public void run() {
            try {
                out = new PrintWriter(new PrintWriter(socket.getOutputStream()));
                        synchronized (queue) {
                            if(!queue.isEmpty()){
                                for (String stuff:queue) {
                                    //TODO create event
                                out.println(stuff);
                                out.flush();
                                if (worldsocket.config.debug()){worldsocket.getLogger().info("send: " + stuff);}
                                }
                                queue.clear();
                            }
                        }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
