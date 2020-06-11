package com.serverworld.worldSocket.bungeecord.events;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.plugin.Event;

public class MessagecomingEvent extends Event {

    final String msg;

    private String sender;
    private String receiver;
    private String type;
    private String channel;
    private String message;

    public MessagecomingEvent(String msg) {
        this.msg = msg;
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonmsg = jsonParser.parse(msg).getAsJsonObject();
        sender = jsonmsg.get("sender").getAsString();
        receiver = jsonmsg.get("receiver").getAsString();
        type = jsonmsg.get("type").getAsString();
        channel = jsonmsg.get("channel").getAsString();
        message = jsonmsg.get("message").getAsString();
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
