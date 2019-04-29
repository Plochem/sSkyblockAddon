package com.plochem.sfa.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.plochem.sfa.SFactionAddon;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class ChatHandling implements Listener{
	SFactionAddon sfa;
	
	public ChatHandling(SFactionAddon sfa) {
		this.sfa = sfa;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		String rank = PermissionsEx.getUser(p).getPrefix().replaceAll("&", "§");
		e.setFormat(rank + p.getName() + "§r: " + e.getMessage().replaceAll("%", "%%"));
	}

}
