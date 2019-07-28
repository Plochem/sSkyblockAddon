package com.plochem.ssa.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.plochem.ssa.SSkyblockAddon;
import com.plochem.ssa.tags.TagManager;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class ChatHandling implements Listener{
	SSkyblockAddon sfa;
	
	public ChatHandling(SSkyblockAddon sfa) {
		this.sfa = sfa;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		String rank = PermissionsEx.getUser(p).getPrefix().replaceAll("&", "§");
		String tag = TagManager.getTag(p);
		String msg = e.getMessage();
		if(p.hasPermission("sfa.coloredchat")) {
			msg = msg.replaceAll("&", "§");
		}
		e.setFormat(rank + p.getName()  + tag + "§r: " + msg.replaceAll("%", "%%"));
	}

}
