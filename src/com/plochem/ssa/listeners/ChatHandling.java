package com.plochem.ssa.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.plochem.ssa.SSkyblockAddon;
import com.plochem.ssa.tags.TagManager;

public class ChatHandling implements Listener{
	SSkyblockAddon sfa;
	
	public ChatHandling(SSkyblockAddon sfa) {
		this.sfa = sfa;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		String tag = TagManager.getTag(p);
		e.setFormat(e.getFormat().replaceAll("§r:", tag + "§r:"));
	}

}
