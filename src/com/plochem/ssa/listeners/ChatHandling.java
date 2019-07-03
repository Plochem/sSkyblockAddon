package com.plochem.ssa.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.plochem.ssa.SSkyblockAddon;
import com.wasteofplastic.askyblock.ASkyBlockAPI;

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
		long islandLevel = ASkyBlockAPI.getInstance().getLongIslandLevel((p.getUniqueId()));
		e.setFormat("§7[§a" + islandLevel + "§7]" + rank + p.getName() + "§r: " + e.getMessage().replaceAll("%", "%%"));
	}

}
