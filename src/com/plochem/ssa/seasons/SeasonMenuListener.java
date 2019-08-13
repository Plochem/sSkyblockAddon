package com.plochem.ssa.seasons;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SeasonMenuListener implements Listener{ 
	/*
	 * 
	 * #1 - $30 Buycraft, IslandTop#1 Tag &  Special Island
#2 - $20 Buycraft & Special Island
#3 - $10 Buycraft & Special Island
#4 to #10 -  $1 Buycraft
	 */
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if(e.getClickedInventory() != null && e.getClickedInventory().getItem(e.getSlot()) != null) {
			if(e.getInventory().getTitle().equalsIgnoreCase("Season Rewards")) {
				e.setCancelled(true);
				if(e.getClickedInventory().getTitle().equalsIgnoreCase("Season Rewards")) {
					Player p = (Player)e.getWhoClicked();
					ItemStack item = e.getCurrentItem();
					String reward = ChatColor.stripColor(item.getItemMeta().getDisplayName());
					int seasonRewardIsIn = Integer.parseInt(reward.split(" ")[1]);
					int rank = -1;
					
					if(SeasonManager.getCurrentSeason() > seasonRewardIsIn) { // check if attempting the claim previous season rewards
						rank = SeasonManager.getRankInSeason(seasonRewardIsIn, p);
						if(rank !=0 && rank <= SeasonManager.getHighestRank(seasonRewardIsIn, reward)) {
							if(rank >= SeasonManager.getLeastRank(seasonRewardIsIn, reward)) { // in the rank range
								if(SeasonManager.alreadyClaimed(seasonRewardIsIn, reward, p)) {
									p.sendMessage("§cYou already claimed this reward!");
								} else {
									for(String cmd : SeasonManager.getCommands(seasonRewardIsIn, reward)) {
										Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%player%", p.getName()));
									}
									p.sendMessage(SeasonManager.getClaimMsg(seasonRewardIsIn, reward).replaceAll("%player%", p.getName()));
									SeasonManager.claimed(seasonRewardIsIn, reward, p);
								}
							} else {
								p.sendMessage("§cYou are qualified for a better reward!");
							}
							
						} else {
							p.sendMessage("§cYour island rank in season " + seasonRewardIsIn + " is not qualified to claim this reward.");
						}
					} else {
						p.sendMessage("§cYou may not claim the rewards for season " + seasonRewardIsIn + " yet.");
					}
	
				}
			}
		}
	}

}
