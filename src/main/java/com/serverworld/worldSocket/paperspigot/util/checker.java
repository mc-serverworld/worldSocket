package com.serverworld.worldSocket.paperspigot.util;

import com.serverworld.worldSocket.paperspigot.worldSocket;

import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

public class checker {
    static worldSocket worldsocket;
    static ConcurrentLinkedQueue<String> list = new ConcurrentLinkedQueue<>();
    public checker(worldSocket worldsocket){
        worldsocket.getServer().getScheduler().scheduleSyncRepeatingTask(worldsocket, new Runnable() {
            @Override
            public void run() {
                synchronized (list) {
                    if (!list.isEmpty()) {
                        if(list.size()>3)
                            worldsocket.reconnect();
                    }
                }
            }
        }, 0L, worldsocket.config.checkrate());
    }

    public void checksender(){
        worldsocket.getServer().getScheduler().scheduleSyncRepeatingTask(worldsocket, new Runnable() {
            @Override
            public void run() {
                synchronized (list) {
                    list.add("CHECKING:" + new Date().getTime());
                    messager.sendmessage("CONNECTCHECK");
                }
            }
        }, 0L, worldsocket.config.checkrate());
    }

    public void clearlist(){
        synchronized (list) {
            list.clear();
        }
    }
}
