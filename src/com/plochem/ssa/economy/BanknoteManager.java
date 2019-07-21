package com.plochem.ssa.economy;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.plochem.ssa.SSkyblockAddon;


public class BanknoteManager {
	public static void giveBanknote(Player p, double amount) {
		String name = "§a$" + String.format("%,.2f", amount) + " §7(Right-Click)";
		ItemStack note = new ItemStack(Material.PAPER);
		ItemMeta noteMeta= note.getItemMeta();
		noteMeta.setDisplayName(name);
		note.setItemMeta(noteMeta);
		p.getInventory().addItem(note);
		p.sendMessage("§aYou received a $" + String.format("%,.2f", amount) + " banknote.");
		
	}
	
	// bal -> banknote
	public static void withdraw(Player p, double amount) {
		SSkyblockAddon ssa = SSkyblockAddon.getPlugin(SSkyblockAddon.class);
		SEconomyImplementer eco = ssa.getSEconomy().getEconomyImplementer();
		if(amount > 0) {
			if(eco.getBalance(p) > amount) {
				giveBanknote(p, amount);
				eco.withdrawPlayer(p, amount);
			} else {
				p.sendMessage("§cYou do not have enough money to withdraw this amount.");
			}
		} else {
			p.sendMessage("§cYou may only withdraw a positive amount.");
		}

	}
	
}
