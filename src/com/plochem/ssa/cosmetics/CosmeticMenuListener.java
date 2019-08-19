package com.plochem.ssa.cosmetics;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.plochem.ssa.cosmetics.types.ProjectileTrail;
import com.plochem.ssa.cosmetics.types.TrailEffect;

import net.md_5.bungee.api.ChatColor;

public class CosmeticMenuListener implements Listener{
	
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if(e.getClickedInventory() != null && e.getClickedInventory().getItem(e.getSlot()) != null) {
			if(isValidMenu(e.getInventory().getName())) {
				e.setCancelled(true);
				if(isValidMenu(e.getClickedInventory().getTitle())) {
					Player p = (Player)e.getWhoClicked();
					String itemName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
					if(e.getClickedInventory().getTitle().equals("Cosmetics")) {
						if(itemName.equalsIgnoreCase("projectile trails")) {
							CosmeticManager.openShop(p, CosmeticType.Projectile_Trail);
						} else if(itemName.equalsIgnoreCase("trail effects")) {
							CosmeticManager.openShop(p, CosmeticType.Trail_Effect);
						} else { // 
							p.sendMessage("§cComing soon!");
							p.closeInventory();
						}
					} else { // clicked on sub-menu cosmetics
						String type = e.getClickedInventory().getTitle().replaceAll(" ", "_");
						CosmeticType cosmeticType = CosmeticType.valueOf(e.getClickedInventory().getTitle().replaceAll(" ", "_"));
						String itemNameEnum = itemName.replaceAll(" ", "_");
						if(p.hasPermission("sfa.cosmetics." + type + "." + itemName)){  
							if(cosmeticType == CosmeticType.Projectile_Trail) {
								CosmeticManager.projectile.put(p.getUniqueId(), ProjectileTrail.valueOf(itemNameEnum));
							} else if(cosmeticType == CosmeticType.Trail_Effect) {
								CosmeticManager.trail.put(p.getUniqueId(), TrailEffect.valueOf(itemNameEnum));								
							}
							p.sendMessage("§aSelected!");
							//
						} else {
							p.sendMessage("§cYou do not own this cosmetic!");
						}
						p.closeInventory();
					}
				}
			}
		}
	}

	private boolean isValidMenu(String name) {
		if(name.equals("Cosmetics")) return true;
		for(CosmeticType type : CosmeticType.values()) {
			if(name.equals(type.toString().replaceAll("_", " "))) {
				return true;
			}
		}
		return false;
	}

}
