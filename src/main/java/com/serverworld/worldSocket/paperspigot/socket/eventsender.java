package com.serverworld.worldSocket.paperspigot.socket;

import com.serverworld.worldSocket.paperspigot.events.MessagecomingEvent;
import com.serverworld.worldSocket.paperspigot.worldSocket;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.ConcurrentLinkedQueue;

public class eventsender {
    public static ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();
    public eventsender(worldSocket worldsocket){
        BukkitTask task = new BukkitRunnable() {

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
        }.runTaskTimer(worldsocket, 20 * 2, 20 * 2);
    }

}
