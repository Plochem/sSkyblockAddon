package com.plochem.sfa.perks;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.plochem.sfa.SFactionAddon;
import com.plochem.sfa.economy.SEconomyImplementer;

public class PerkListeners implements Listener{
	private SEconomyImplementer eco = SFactionAddon.getPlugin(SFactionAddon.class).getSEconomy().getEconomyImplementer();
	@EventHandler
	public void onKill(PlayerDeathEvent e) {
		Player killer = e.getEntity().getKiller();
		if(killer != null) {
			int currLevel = PerkManager.currentLevel(PerkType.REGENERATION, killer);
			if(currLevel > 0) {
				PerkType.REGENERATION.performAction(killer, null, currLevel);
			}
		}
	}
	
	@EventHandler
	public void onReceiveDamage(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof LivingEntity) {
			Player damaged = (Player)e.getEntity();
			int currLevel = PerkManager.currentLevel(PerkType.DEFLECT, damaged);
			if(currLevel > 0) {
				PerkType.DEFLECT.performAction(damaged, (LivingEntity)e.getDamager(), currLevel);
			}
		}
	}
	
	@EventHandler
	public void onPurchase(InventoryClickEvent e) {
		if(e.getClickedInventory() != null && e.getClickedInventory().getItem(e.getSlot()) != null) {
			if(e.getInventory().getTitle().equalsIgnoreCase("perk shop")) {
				e.setCancelled(true);
				if(e.getClickedInventory().getTitle().equalsIgnoreCase("perk shop")) {
					ItemStack item = e.getCurrentItem();
					String name = item.getItemMeta().getDisplayName();
					if(name != null) {
						Player buyer = (Player)e.getWhoClicked();
						String[] temp = name.split(" ");
						temp = Arrays.copyOf(temp, temp.length-1);
						PerkType type = PerkType.valueOf(String.join("_", temp).toUpperCase());
						int cost = type.getCost(PerkManager.getNextLvlUpgrade(type, buyer));
						if(eco.getBalance(buyer) >= cost) {
							eco.withdrawPlayer(buyer, cost);
							// play sound
						} else {
							e.getWhoClicked().sendMessage("§cYou do not have enough money to purchase this perk.");
							// play sound
						}
					}
					e.getWhoClicked().closeInventory();
				}
			}
		}
	}
	
}
