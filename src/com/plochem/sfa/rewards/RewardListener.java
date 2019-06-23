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

import com.plochem.sfa.SFactionAddon;

public class RewardListener implements Listener{
	SFactionAddon sfa = SFactionAddon.getPlugin(SFactionAddon.class);
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
						LocalDateTime beginningNextMonth = LocalDateTime.of(today.plusMonths(1).withDayOfMonth(1), midnight);
						Player clicker = (Player)e.getWhoClicked();
						if(name.equalsIgnoreCase("Daily Rewards")) {
							claimReward(clicker, RewardType.DAILY, now, tomorrowMidnight);
						} else if(name.equalsIgnoreCase("monthly rewards")) {
							claimReward(clicker, RewardType.MONTHLY, now, beginningNextMonth);
						} else if(name.equalsIgnoreCase("elite monthly rewards")) {
							if(clicker.hasPermission("sfa.rewards.elite"))
								claimReward(clicker, RewardType.ELITEMONTHLY, now, beginningNextMonth);
							else
								clicker.sendMessage("§cYou must have the §3§lELITE §crank to claim this reward.");
						} else if(name.equalsIgnoreCase("master monthly rewards")) {
							if(clicker.hasPermission("sfa.rewards.master"))
								claimReward(clicker, RewardType.MASTERMONTHLY, now, beginningNextMonth);
							else
								clicker.sendMessage("§cYou must have the §6§lMASTER §crank to claim this reward.");
						} else if(name.equalsIgnoreCase("legend monthly rewards")) {
							if(clicker.hasPermission("sfa.rewards.legend"))
								claimReward(clicker, RewardType.LEGENDMONTHLY, now, beginningNextMonth);
							else
								clicker.sendMessage("§cYou must have the §e§lLEGEND §crank to claim this reward.");
						} else if(name.equalsIgnoreCase("mystic monthly rewards")) {
							if(clicker.hasPermission("sfa.rewards.mystic"))
								claimReward(clicker, RewardType.MYSTICMONTHLY, now, beginningNextMonth);
							else
								clicker.sendMessage("§cYou must have the §d§lMYSTIC §crank to claim this reward.");
						}
						clicker.closeInventory(); 
					}
				}
			}
		}
	}
	
	private void claimReward(Player clicker, RewardType type, LocalDateTime now, LocalDateTime end) {
		if(RewardManager.notClaim(clicker, type)) {
			sfa.getSEconomy().getEconomyImplementer().depositPlayer(clicker, type.getMoney());
			clicker.setTotalExperience(clicker.getTotalExperience() + type.getXp());
			RewardManager.addToClaim(clicker, type);
			clicker.sendMessage("§aYou received §6$" + type.getMoney() + " §aand§3 " + type.getXp() + " XP§a.");
		} else {
			clicker.sendMessage("§cYou already claimed this reward. Wait for " + difference(end, now) + ".");
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
