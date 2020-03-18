package com.serverworld.worldSocket.paperspigot.socket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.serverworld.worldSocket.paperspigot.events.MessagecomingEvent;
import com.serverworld.worldSocket.paperspigot.worldSocket;
import net.md_5.bungee.api.ChatColor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

public class socketclient {
    private worldSocket worldsocket;
    private login loginer = new login();
    private receiver receiver = new receiver();
    private sender sender = new sender();

    static Socket socket;
    private static Scanner in;
    private static PrintWriter out;

    static ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();
    private boolean shouldStop=false;

    public socketclient(worldSocket worldsocket){
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
            try{
                socket = new Socket(worldsocket.config.host(), worldsocket.config.clientport());
                Scanner in = new Scanner(socket.getInputStream());
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                receiver.start();
                out.println(worldsocket.config.name());
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
                            }else {
                                JsonParser jsonParser = new JsonParser();
                                JsonObject jsonmsg = jsonParser.parse(message).getAsJsonObject();
                                if(jsonmsg.get("receiver").getAsString().toLowerCase()==worldsocket.config.name()&&jsonmsg.get("receiver").getAsString().toLowerCase()!="socketapi"){
                                    worldsocket.eventsender.queue.add(message);
                                }else if(jsonmsg.get("receiver").getAsString().toLowerCase()=="all"&&jsonmsg.get("receiver").getAsString().toLowerCase()!="socketapi"){
                                    worldsocket.eventsender.queue.add(message);
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
