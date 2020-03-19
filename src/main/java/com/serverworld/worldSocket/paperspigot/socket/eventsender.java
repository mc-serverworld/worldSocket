package com.serverworld.worldSocket.paperspigot.socket;

import com.serverworld.worldSocket.paperspigot.events.MessagecomingEvent;
import com.serverworld.worldSocket.paperspigot.worldSocket;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.ConcurrentLinkedQueue;

public class eventsender {
    static ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
    public eventsender(worldSocket worldsocket){
        worldsocket.getServer().getScheduler().scheduleSyncRepeatingTask(worldsocket, new Runnable() {
            @Override
            public void run() {
                synchronized (queue) {
                    if (!queue.isEmpty()) {
                        for (String stuff : queue) {
                            MessagecomingEvent messagecomingEvent = new MessagecomingEvent(stuff);
                            worldsocket.getServer().getPluginManager().callEvent(messagecomingEvent);
                        }
                        queue.clear();
                    }
                }
            }
        }, 0L, 20L);
    }
    public void addeventqueue(String message){
        queue.add(message);
    }

}
