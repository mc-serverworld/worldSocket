package com.serverworld.worldSocket.paperspigot.util;

import com.google.gson.JsonObject;

public class socketloginer {
    private String name;
    private String password;
    private String apiverison;

    public void setName(String name){
        this.name = name;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setApiverison(String apiverison){
        this.apiverison = apiverison;
    }
    public String createmessage(){
        JsonObject jsonmsg = new JsonObject();
        jsonmsg.addProperty("name", name);
        jsonmsg.addProperty("password", password);
        //jsonmsg.addProperty("apiversion", apiverison);
        return (jsonmsg.toString());
    }
}
