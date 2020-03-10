package com.serverworld.worldSocket.bungeecord.socket;

import com.serverworld.worldSocket.bungeecord.events.MessagecomingEvent;
import com.serverworld.worldSocket.bungeecord.worldSocket;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.ExportException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class socketserver extends Thread {
    private static Set<String> names = new HashSet<>();
    private static Set<PrintWriter> writers = new HashSet<>();
    public static worldSocket worldsocket;

    public socketserver(worldSocket worldSocket) {
        worldsocket=worldSocket;
    }

    public void run() {
        try (ServerSocket listener = new ServerSocket(worldsocket.config.serverport())) {
            worldsocket.getLogger().info("starting socket server...");
            worldsocket.getLogger().info("using port "+worldsocket.config.serverport());
            ExecutorService pool = Executors.newFixedThreadPool(worldsocket.config.threads());
            worldsocket.getLogger().info("using "+worldsocket.config.threads()+" threads");
            while (true) {
                pool.execute(new Handler(listener.accept()));
            }
        }catch (IOException e){

        }
    }
    private static class Handler implements Runnable {
        private String name;
        private Socket socket;
        private Scanner in;
        private PrintWriter out;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream(), true);
                while (true) {
                    name = in.nextLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (names) {
                        if (!names.contains(name)) {
                            names.add(name);
                            break;
                        }else {
                            out.println("ERROR:NAME_USED");
                            if(worldsocket.config.debug()){
                                worldsocket.getLogger().warning(ChatColor.YELLOW + "Opps! seem some one use the same name: " + name);
                            }
                        }
                    }
                }
                out.println("ACCEPTED");
                worldsocket.getLogger().info("Socket join: " + name);
                for (PrintWriter writer : writers) { }
                writers.add(out);
                //-------END---------
                while (true) {
                    String input = in.nextLine();
                    if(worldsocket.config.debug()){
                        worldsocket.getLogger().info(name + "send message: " + input);
                        worldsocket.getLogger().info("sent to " + writers.size() + " clients");
                    }

                    if (input.toLowerCase().startsWith("leave")) {
                        return;
                    }
                    for (PrintWriter writer : writers) {
                        writer.println(input);
                    }
                    worldsocket.getProxy().getPluginManager().callEvent(new MessagecomingEvent(input));
                }
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (out != null) {
                    writers.remove(out);
                }
                if (name != null) {
                    names.remove(name);
                    worldsocket.getLogger().info("Socket quit: " + name);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
