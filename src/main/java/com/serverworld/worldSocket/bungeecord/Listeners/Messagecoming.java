/*
 *
 *  * WorldMISF - cms of mc-serverworld
 *  * Copyright (C) 2019-2020 mc-serverworld
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.serverworld.worldSocket.bungeecord.Listeners;

import com.serverworld.worldSocket.bungeecord.events.MessagecomingEvent;
import com.serverworld.worldSocket.bungeecord.worldSocket;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class Messagecoming implements Listener {

    private worldSocket worldSocket;

    public Messagecoming(Plugin plugin){
        ProxyServer.getInstance().getPluginManager().registerListener(plugin, this);
    }
    @EventHandler
    public void onMessagecomingEvent(MessagecomingEvent event){
     /*   if(!event.getChannel().equals("MISF"))
            return;
        if(!event.getReceiver().toLowerCase().equals("porxy"))
            return;*/
        try {
            ProxyServer.getInstance().getLogger().info((ChatColor.GREEN + "Getbysocket: " + event.getMessage()));
        }catch (Exception e){
            ProxyServer.getInstance().getLogger().info("Error on socket msg "+e.getMessage());
        }
    }
}
