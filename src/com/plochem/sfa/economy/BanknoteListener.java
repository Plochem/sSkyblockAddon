package com.plochem.sfa.economy;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.plochem.sfa.SFactionAddon;

public class BanknoteListener implements Listener{
	SEconomyImplementer eco = SFactionAddon.getPlugin(SFactionAddon.class).getSEconomy().getEconomyImplementer();
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if(e.getPlayer().getItemInHand().getType() == Material.PAPER){
			if(e.getItem().getItemMeta().getDisplayName() != null && e.getItem().getItemMeta().getDisplayName().contains("§")) {
				String name = ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName().split(" ")[0]);
				String num = name.replaceAll("\\$", "").replaceAll(",", "");
				if(StringUtils.isNumeric(num)) {
					eco.depositPlayer(e.getPlayer(), Double.parseDouble(num));
					e.getPlayer().sendMessage("§eYou received §a" + name + "§e from the banknote.");
					int amt = e.getPlayer().getItemInHand().getAmount() - 1;
					if(amt == 0) {
						e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR));
					} else {
						e.getItem().setAmount(amt);						
					}
				}
			}
		}
	}

}
