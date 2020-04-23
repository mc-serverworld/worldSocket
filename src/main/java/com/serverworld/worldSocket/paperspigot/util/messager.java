package com.serverworld.worldSocket.paperspigot.util;

import com.serverworld.worldSocket.paperspigot.worldSocket;
import com.serverworld.worldSocket.paperspigot.socket.socketclient;

public class messager {

    static worldSocket worldsocket;
    public messager(worldSocket worldSocket){
        this.worldsocket = worldSocket;
    }

    public static void sendmessage (String message){
        worldsocket.sendmessage(message);
    }
}