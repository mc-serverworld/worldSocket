package com.serverworld.worldSocket.paperspigot.util;

import com.serverworld.worldSocket.paperspigot.worldSocket;

public class messager {

    static worldSocket worldsocket;
    public messager(worldSocket worldSocket){
        this.worldsocket = worldSocket;
    }

    public static void sendmessage (String message){
        worldsocket.sendmessage(message);
    }
}