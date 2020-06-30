package com.serverworld.worldSocket.bungeecord.socket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.serverworld.worldSocket.bungeecord.events.MessagecomingEvent;
import com.serverworld.worldSocket.bungeecord.worldSocket;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;

import javax.net.ssl.*;
import javax.swing.plaf.SplitPaneUI;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SSLsocketserver extends Thread {
    static ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();
    private sender sender;
    private static Set<String> names = new HashSet<>();
    private static Set<PrintWriter> writers = new HashSet<>();
    //public static worldSocket worldsocket;

    private SSLContext ctx;
    private KeyManagerFactory kmf;
    private TrustManagerFactory tmf;
    private KeyStore ks;
    private KeyStore tks;


    private String SERVER_KEY_STORE_FILE;
    private String SERVER_TRUST_KEY_STORE_FILE;
    private String SERVER_KEY_STORE_PASSWORD;
    private String SERVER_TRUST_KEY_STORE_PASSWORD;

    public SSLsocketserver(worldSocket worldSocket) {
        //worldsocket=worldSocket;
    }

    public void run() {
        try{
            SERVER_KEY_STORE_FILE = worldSocket.getInstance().config.server_keyStore_file();
            SERVER_TRUST_KEY_STORE_FILE = worldSocket.getInstance().config.server_trustStore_file();
            SERVER_KEY_STORE_PASSWORD = worldSocket.getInstance().config.server_keyStore_password();
            SERVER_TRUST_KEY_STORE_PASSWORD = worldSocket.getInstance().config.server_trustStore_password();
            ctx = SSLContext.getInstance("SSL");

            kmf = KeyManagerFactory.getInstance("SunX509");
            tmf = TrustManagerFactory.getInstance("SunX509");

            ks = KeyStore.getInstance("JKS");
            tks = KeyStore.getInstance("JKS");

            ks.load(new FileInputStream(SERVER_KEY_STORE_FILE), SERVER_KEY_STORE_PASSWORD.toCharArray());
            tks.load(new FileInputStream(SERVER_TRUST_KEY_STORE_FILE), SERVER_TRUST_KEY_STORE_PASSWORD.toCharArray());

            kmf.init(ks, SERVER_KEY_STORE_PASSWORD.toCharArray());
            tmf.init(tks);

            ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            SSLServerSocket listener = (SSLServerSocket) ctx.getServerSocketFactory().createServerSocket(worldSocket.getInstance().config.port());
            listener.setNeedClientAuth(worldSocket.getInstance().config.forceSSL());
            worldSocket.getInstance().getLogger().info("starting socket server...");
            worldSocket.getInstance().getLogger().info("using SSL");
            if(worldSocket.getInstance().config.forceSSL())
                worldSocket.getInstance().getLogger().info("force using SSL");
            worldSocket.getInstance().getLogger().info("using port "+worldSocket.getInstance().config.port());
            ExecutorService pool = Executors.newFixedThreadPool(worldSocket.getInstance().config.threads());
            worldSocket.getInstance().getLogger().info("using "+worldSocket.getInstance().config.threads()+" threads");
            while (true) {
                pool.execute(new Handler((SSLSocket) listener.accept()));
            }
        }catch (Exception e){
            e.printStackTrace();
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
                synchronized (queue) {
                    if (!queue.isEmpty()) {
                        for (String stuff : queue) {
                            for (PrintWriter writer : writers) {
                                writer.println(stuff);
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

    private static class Handler implements Runnable {
        private String loginmessage;
        private String name;
        private SSLSocket socket;
        private Scanner in;
        private PrintWriter out;

        public Handler(SSLSocket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream(), true);
                while (true) {
                    loginmessage = in.nextLine();
                    if (loginmessage == null) {
                        return;
                    }
                    synchronized (names) {
                        JsonParser jsonParser = new JsonParser();
                        JsonObject jsonmsg = jsonParser.parse(loginmessage).getAsJsonObject();
                        name = jsonmsg.get("name").getAsString();
                        if (!names.contains(name)) {
                            if (jsonmsg.get("password").getAsString().equals(worldSocket.getInstance().config.password())){
                                names.add(name);
                                break;
                            }else {
                                out.println("ERROR:WRONG_PASSWORD");
                                out.flush();
                                worldSocket.getInstance().getLogger().warning(ChatColor.RED + "Warring: Some one try to login with wrong password!" + " IP: " + socket.getRemoteSocketAddress());
                            }
                        }else {
                            out.println("ERROR:NAME_USED");
                            out.flush();
                            worldSocket.getInstance().getLogger().warning(ChatColor.YELLOW + "Opps! seem some one use the same name: " + name);
                        }
                    }
                }
                out.println("ACCEPTED");
                out.flush();
                worldSocket.getInstance().getLogger().info("Socket join: " + name);
                //for (PrintWriter writer : writers) { }
                writers.add(out);
                //-------END---------
                while (true) {
                    String input = in.nextLine();
                    if (input.toLowerCase().startsWith("leave")) {
                        return;
                    }
                    if (input.toUpperCase().equals("CONNECTCHECK")){
                        out.println("CHECK:ONLINE");
                        if(worldSocket.getInstance().config.debug())
                            worldSocket.getInstance().getLogger().info(name + " checking connection");

                    }else {
                        JsonParser jsonParser = new JsonParser();
                        JsonObject jsonmsg = jsonParser.parse(input).getAsJsonObject();
                        try {
                            if(jsonmsg.get("receiver").getAsString().toLowerCase().equals("proxy")){
                                worldSocket.getInstance().getProxy().getPluginManager().callEvent(new MessagecomingEvent(input));
                                if(worldSocket.getInstance().config.debug())
                                    worldSocket.getInstance().getLogger().info("Event send");
                            }

                            else if(jsonmsg.get("receiver").getAsString().toLowerCase().equals("all")){
                                worldSocket.getInstance().getProxy().getPluginManager().callEvent(new MessagecomingEvent(input));
                                if(worldSocket.getInstance().config.debug())
                                    worldSocket.getInstance().getLogger().info("Event send");
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        if(!jsonmsg.get("receiver").getAsString().toLowerCase().equals("proxy")){
                            if(worldSocket.getInstance().config.debug()){
                                worldSocket.getInstance().getLogger().info(name + " send message: " + input);
                                worldSocket.getInstance().getLogger().info("sent to " + writers.size() + " clients");
                            }
                            for (PrintWriter writer : writers) {
                                writer.println(input);
                                writer.flush();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (out != null) {
                    writers.remove(out);
                }
                if (name != null) {
                    names.remove(name);
                    worldSocket.getInstance().getLogger().info("Socket quit: " + name);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
