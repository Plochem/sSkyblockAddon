package com.plochem.sfa.boosters;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BoosterClaim implements Listener{
	@EventHandler
	public void onClickItem(PlayerInteractEvent e) {
		if(e.getPlayer().getItemInHand().getType() == Material.INK_SACK){
			if(e.getItem().getItemMeta().getDisplayName() != null && e.getItem().getItemMeta().getDisplayName().contains("§")) {
				String[] splitName = e.getItem().getItemMeta().getDisplayName().replaceAll("§", "").split(" ");
				String color = "§3";
				BoosterType type = BoosterType.EXPERIENCE;
				if(splitName[2].equalsIgnoreCase("money")) {
					type = BoosterType.MONEY;
					color = "§6";
				}
				int duration = Integer.parseInt(splitName[0].substring(2));
				if(splitName[1].equalsIgnoreCase("hour")) {
					duration *= 60;
				}
				BoosterManager.claimBooster(new Booster(e.getPlayer().getUniqueId(), duration, duration, type, e.getItem().getItemMeta().getDisplayName().replaceAll("§", "&")), color);
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
