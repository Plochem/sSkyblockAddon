package com.plochem.sfa.rewards;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class RewardListener implements Listener{
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if(e.getClickedInventory() != null && e.getClickedInventory().getItem(e.getSlot()) != null) {
			if(e.getInventory().getTitle().equalsIgnoreCase("rewards") || e.getInventory().getTitle().equalsIgnoreCase("rewards")) {
				e.setCancelled(true);
				if(e.getClickedInventory().getTitle().equalsIgnoreCase("rewards")) {
					String name = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
					if(name != null) {
						LocalTime midnight = LocalTime.MIDNIGHT;
						LocalDate today = LocalDate.now();
						LocalDateTime now = LocalDateTime.now();
						LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
						LocalDateTime tomorrowMidnight = todayMidnight.plusDays(1);
						Player clicker = (Player)e.getWhoClicked();
						if(name.equalsIgnoreCase("Daily Rewards")) {
							if(now.isAfter(todayMidnight) && RewardManager.notClaim(clicker, RewardType.DAILY)) {
								// give reward and add to claimed list 
								System.out.println("YSAFFPYDSHFIDSHJPFSJDIPF");
								RewardManager.addToClaim(clicker, RewardType.DAILY);
							} else {
								clicker.sendMessage("§cYou already claimed this reward. Wait for " + difference(tomorrowMidnight, now) + ".");
							}
						} else if(name.equalsIgnoreCase("Monthly Rewards")) {
							
						}
						clicker.closeInventory();
					}
				}
			}
		}
	}

	private String difference(LocalDateTime tomorrowMidnight, LocalDateTime now) {
		LocalDateTime tempDateTime = LocalDateTime.from(now);
		long years = tempDateTime.until(tomorrowMidnight, ChronoUnit.YEARS);
		tempDateTime = tempDateTime.plusYears(years);

		long months = tempDateTime.until(tomorrowMidnight, ChronoUnit.MONTHS);
		tempDateTime = tempDateTime.plusMonths(months);

		long days = tempDateTime.until(tomorrowMidnight, ChronoUnit.DAYS);
		tempDateTime = tempDateTime.plusDays(days);


		long hours = tempDateTime.until(tomorrowMidnight, ChronoUnit.HOURS);
		tempDateTime = tempDateTime.plusHours(hours);

		long minutes = tempDateTime.until(tomorrowMidnight, ChronoUnit.MINUTES);
		tempDateTime = tempDateTime.plusMinutes(minutes);

		long seconds = tempDateTime.until(tomorrowMidnight, ChronoUnit.SECONDS);
		String time = hours + " hours " + minutes + " minutes " + seconds + " seconds";
		if(days > 0) {
			time = days + " days " + time;
		}
		return time;
	}
}
