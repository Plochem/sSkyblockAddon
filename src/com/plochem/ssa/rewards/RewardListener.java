package com.plochem.ssa.rewards;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.plochem.ssa.SSkyblockAddon;

public class RewardListener implements Listener{
	SSkyblockAddon sfa = SSkyblockAddon.getPlugin(SSkyblockAddon.class);
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if(e.getClickedInventory() != null && e.getClickedInventory().getItem(e.getSlot()) != null) {
			if(e.getInventory().getTitle().equalsIgnoreCase("rewards")) {
				e.setCancelled(true);
				if(e.getClickedInventory().getTitle().equalsIgnoreCase("rewards")) {
					String name = e.getCurrentItem().getItemMeta().getDisplayName();
					if(name != null) {
						LocalDateTime now = LocalDateTime.now();
						Player clicker = (Player)e.getWhoClicked();
						for(Reward r : RewardManager.getRewards()) {
							if(name.equals(r.getName())) {
								if(r.getPermission().equals("") || clicker.hasPermission(r.getPermission())) {
									if(RewardManager.notClaim(clicker, r)) {
										RewardManager.addToClaim(clicker, r);
										 for(String cmd : r.getCommands()) {
											 Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%player%", clicker.getName()));
										 }
										 clicker.sendMessage("§aYou have successfuly claimed the " + r.getName() + " §areward!");
									} else {
										clicker.sendMessage("§cYou already claimed this reward. Wait for " + difference(r.getType(), now) + ".");
									}
								} else {
									clicker.sendMessage("§cYou do not have the rank to claim this reward!");
								}
								break;
							}
						}
						clicker.closeInventory(); 
					}
				}
			}
		}
	}
	
	private String difference(RewardType type, LocalDateTime now) {
		LocalDate today = LocalDate.now();
		LocalDateTime todayMidnight = LocalDateTime.of(today, LocalTime.MIDNIGHT);
		LocalDateTime tomorrowMidnight = todayMidnight.plusDays(1);
		LocalDateTime beginningNextMonth = LocalDateTime.of(today.plusMonths(1).withDayOfMonth(1), LocalTime.MIDNIGHT);
		
		LocalDateTime end = tomorrowMidnight;
		if(type == RewardType.MONTHLY) {
			end = beginningNextMonth;
		} else if(type == RewardType.YEARLY) {
			end = LocalDateTime.of(today.plusYears(1).withMonth(1).withDayOfMonth(1), LocalTime.MIDNIGHT);
		}
		LocalDateTime tempDateTime = LocalDateTime.from(now);
		long years = tempDateTime.until(end, ChronoUnit.YEARS);
		tempDateTime = tempDateTime.plusYears(years);

		long months = tempDateTime.until(end, ChronoUnit.MONTHS);
		tempDateTime = tempDateTime.plusMonths(months);

		long days = tempDateTime.until(end, ChronoUnit.DAYS);
		tempDateTime = tempDateTime.plusDays(days);

		long hours = tempDateTime.until(end, ChronoUnit.HOURS);
		tempDateTime = tempDateTime.plusHours(hours);

		long minutes = tempDateTime.until(end, ChronoUnit.MINUTES);
		tempDateTime = tempDateTime.plusMinutes(minutes);

		long seconds = tempDateTime.until(end, ChronoUnit.SECONDS);
		String time = hours + " hours " + minutes + " minutes " + seconds + " seconds";
		if(months > 0) {
			time = months + " months " + time;
		}
		if(days > 0) {
			time = days + " days " + time;
		}
		return time;
	}
}
