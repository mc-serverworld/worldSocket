package com.serverworld.worldSocket.paperspigot;

import com.serverworld.worldSocket.paperspigot.commands.worldSocketCommands;
import com.serverworld.worldSocket.paperspigot.events.MessagecomingEvent;
import com.serverworld.worldSocket.paperspigot.socket.SSLsocketclient;
import com.serverworld.worldSocket.paperspigot.socket.eventsender;
import com.serverworld.worldSocket.paperspigot.util.messager;
import com.serverworld.worldSocket.paperspigot.worldSocketconfig;
import com.serverworld.worldSocket.paperspigot.socket.socketclient;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.net.ssl.SSLSocket;
import java.util.concurrent.ConcurrentLinkedQueue;


public class worldSocket extends JavaPlugin {

    public worldSocketconfig config;
    public eventsender eventsender;
    public socketclient socketclient;
    public SSLsocketclient SSLsocketclient;
    public messager messager;


    public void onLoad() {
        config = new worldSocketconfig(this);
    }

    @Override
    public void onEnable() {
        config.loadDefConfig();
        eventsender = new eventsender(this);
        if(config.useSSL()){
            SSLsocketclient = new SSLsocketclient(this);
            SSLsocketclient.startlogin();
        }else {
            socketclient = new socketclient(this);
            socketclient.startlogin();
        }

        messager = new messager(this);
    }
    public void sendmessage(String msg){
        if(config.useSSL()){
            SSLsocketclient.sendmessage(msg);
        }else {
            socketclient.sendmessage(msg);
        }
    }
}
