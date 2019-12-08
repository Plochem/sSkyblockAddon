package com.plochem.ssa.seasons;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class SeasonPlayerJoinListener implements Listener{
	@EventHandler
	public void onJoin(PlayerLoginEvent e) {
		if(e.getResult() == Result.KICK_WHITELIST && SeasonManager.isResetting()) {
			if(e.getPlayer().hasPermission("sfa.season.joinreset")) {
				Player p = e.getPlayer();
				e.setResult(Result.ALLOWED);
				p.sendMessage("§nAdditional manual reset guide");
				p.sendMessage("1. Delete /playerdata folder under each world");
				p.sendMessage("2. Go to plugin folders & delete all storage files.");
				p.sendMessage("3. Restart server");
				p.sendMessage("4. Unwhitelist");
			} else {
				e.setKickMessage("§cTo prepare for the new season, the server is under maintenance until further notice.\n§7Join the discord to be updated!");
			}
		}
	}

}
