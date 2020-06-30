package com.serverworld.worldSocket.paperspigot.util;

import com.serverworld.worldSocket.paperspigot.worldSocket;
import net.md_5.bungee.api.ChatColor;

import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

public class checker {
    static worldSocket worldsocket;
    static ConcurrentLinkedQueue<String> list = new ConcurrentLinkedQueue<>();
    public checker(worldSocket worldsocket){
        this.worldsocket = worldsocket;
        worldsocket.getServer().getScheduler().scheduleSyncRepeatingTask(worldsocket, new Runnable() {
            @Override
            public void run() {
                synchronized (list) {
                    list.add("CHECKING:" + new Date().getTime());
                    messager.sendmessage("CONNECTCHECK");
                    if (worldsocket.config.debug())
                        worldsocket.getLogger().info("checking connection");

                    if (!list.isEmpty()) {
                        if(list.size()>3){
                            worldsocket.reconnect();
                            list.clear();
                        }
                    }
                }
            }
        }, 0L, worldsocket.config.checkrate());
    }

    public void clearlist(){
        synchronized (list) {
            list.clear();
            if (worldsocket.config.debug())
                worldsocket.getLogger().info(ChatColor.GREEN + "connection checked");
        }
    }
}
