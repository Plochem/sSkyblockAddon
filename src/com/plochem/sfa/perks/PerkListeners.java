package com.plochem.sfa.perks;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
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
		if (e.getEntity() instanceof Player && e.getDamager() instanceof LivingEntity) { // if player hits mob/player
			Player damaged = (Player)e.getEntity();
			int currLevel = PerkManager.currentLevel(PerkType.DEFLECT, damaged);
			if(currLevel > 0) {
				PerkType.DEFLECT.performAction(damaged, (LivingEntity)e.getDamager(), currLevel);
			}
		}
		
		if(e.getEntity() instanceof Player && (e.getDamager() instanceof Player || e.getDamager() instanceof Projectile)) { // if player hits player
			Player damaged = (Player)e.getEntity();
			Player damager; // shooter of projectile or melee attacker
			if(e.getDamager() instanceof Projectile) {
				if(((Projectile) e.getDamager()).getShooter() instanceof Player){
					damager = (Player)(((Projectile)e.getDamager()).getShooter());
				} else {
					return;
				}
			} else {
				damager = (Player)e.getDamager();
			}
			int currLevel = PerkManager.currentLevel(PerkType.FREEZE, damager);
			if(currLevel > 0) {
				PerkType.FREEZE.performAction(damager, (Player)e.getEntity(), currLevel);
			}
			if(PerkManager.playersInLeech.contains(damaged.getUniqueId())){
				damaged.setHealth(damaged.getHealth() + e.getDamage()); //TOOD check if right, & msg
			}
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(PerkManager.frozenPlayers.contains(e.getPlayer().getUniqueId())) {
	        if(e.getTo().getBlockX() != e.getFrom().getBlockX() || e.getTo().getBlockZ() != e.getFrom().getBlockZ()) {
	        	Location loc = new Location(e.getPlayer().getWorld(), e.getFrom().getX(), (int)e.getFrom().getY(), e.getFrom().getZ(), e.getFrom().getYaw(), e.getFrom().getPitch());
	            e.getPlayer().teleport(loc);
	        }
		}
	}
	
	@EventHandler
	public void onBowRelease(EntityShootBowEvent e) {
		if(e.getEntity() instanceof Player) {
			Player source = (Player)e.getEntity();
			int currLevel = PerkManager.currentLevel(PerkType.FIRE_ARROW, source);
			PerkType.FIRE_ARROW.performAction(source, null, currLevel, (Arrow)e.getProjectile());
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		int currLevel = PerkManager.currentLevel(PerkType.FORTUNE, e.getPlayer());
		PerkType.FORTUNE.performAction(e.getPlayer(), null, currLevel, e.getBlock().getDrops());
	}
	
	@EventHandler
	public void onPurchase(InventoryClickEvent e) {
		if(e.getClickedInventory() != null && e.getClickedInventory().getItem(e.getSlot()) != null) {
			if(e.getInventory().getTitle().equalsIgnoreCase("perk shop")) {
				e.setCancelled(true);
				if(e.getClickedInventory().getTitle().equalsIgnoreCase("perk shop")) {
					ItemStack item = e.getCurrentItem();
					String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
					if(name != null) {
						Player buyer = (Player)e.getWhoClicked();
						String[] temp = name.split(" ");
						temp = Arrays.copyOf(temp, temp.length-1);
						PerkType type = PerkType.valueOf(String.join("_", temp).toUpperCase());
						int cost = type.getCost(PerkManager.currentLevel(type, buyer));
						int currentLevel = PerkManager.currentLevel(type, buyer);
						if(type.getMaxLevel() == currentLevel) { // already at max
							buyer.sendMessage("§cYou already maxed out this perk!");
							// play sound
						} else { //upgrading or buying new perk
							if(eco.getBalance(buyer) >= cost) {
								eco.withdrawPlayer(buyer, cost);
								PerkManager.upgrade(type, buyer);
								if(currentLevel == 0) {
									buyer.sendMessage("§aYou successfully purchased this perk.");
								} else {
									buyer.sendMessage("§aYou successfully upgraded this perk.");
								}
								// play sound
							} else {
								if(currentLevel == 0) {
									buyer.sendMessage("§cYou do not have enough money to purchase this perk.");
								} else {
									buyer.sendMessage("§cYou do not have enough money to upgrade this perk.");
								}
								// play sound
							}
						}
					}
					e.getWhoClicked().closeInventory();
				}
			}
		}
	}
	
}
