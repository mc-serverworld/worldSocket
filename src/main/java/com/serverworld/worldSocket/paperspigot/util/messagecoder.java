package com.serverworld.worldSocket.paperspigot.util;

import com.google.gson.JsonObject;

public class messagecoder {
    private String sender;
    private String receiver;
    private String type;
    private String channel;
    private String message;

    public void setSender(String sender){
        this.sender=sender;
    }
    public void setReceiver(String receiver){
        this.receiver=receiver;
    }
    public void setType(String type){
        this.type=type;
    }
    public void setChannel(String channel){
        this.channel=channel;
    }
    public void setMessage(String message){
        this.message=message;
    }
    public String createmessage(){
        if(sender.isEmpty()){return null;}
        JsonObject jsonmsg = new JsonObject();
        jsonmsg.addProperty("sender", sender);
        jsonmsg.addProperty("receiver", receiver);
        jsonmsg.addProperty("type", type);
        jsonmsg.addProperty("channel", channel);
        jsonmsg.addProperty("message", message);
        return (jsonmsg.toString());
    }
}
