package com.serverworld.worldSocket.bungeecord.socket;


import com.serverworld.worldSocket.bungeecord.events.MessagecomingEvent;
import com.serverworld.worldSocket.bungeecord.worldSocket;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class eventsender {
    static ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
    public eventsender(worldSocket worldsocket){
        worldsocket.getProxy().getScheduler().schedule(worldsocket, new Runnable() {
            @Override
            public void run() {
                synchronized (queue) {
                    if (!queue.isEmpty()) {
                        for (String stuff : queue) {
                            MessagecomingEvent messagecomingEvent = new MessagecomingEvent(stuff);
                            worldsocket.getProxy().getPluginManager().callEvent(messagecomingEvent);
                        }
                        queue.clear();
                    }
                }
            }
        }, 0L, 1L, TimeUnit.SECONDS);
    }
    public void addeventqueue(String message){
        queue.add(message);
    }

}
