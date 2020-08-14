package com.serverworld.worldSocket.paperspigot.events;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.json.JSONObject;

public class MessagecomingEvent extends Event{

    private static final HandlerList HANDLERS = new HandlerList();
    final String msg;

    private String sender;
    private String receiver;
    private String type;
    private String channel;
    private String message;

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public MessagecomingEvent(String msg) {
        this.msg = msg;
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonmsg = jsonParser.parse(msg).getAsJsonObject();
        JSONObject json = new JSONObject(msg);
        sender = json.getString("sender");
        receiver = json.getString("receiver");
        type = json.getString("type");
        channel = json.getString("channel");
        message = json.getString("message");
    }


    public String getSender() {
        return this.sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getType() {
        return type;
    }

    public String getChannel() {
        return channel;
    }

    public String getMessage() {
        return message;
    }

}
