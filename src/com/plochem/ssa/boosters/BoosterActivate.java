package com.plochem.ssa.boosters;

import java.io.File;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class BoosterActivate implements Listener{
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if(e.getClickedInventory()!=null) {
			if(e.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase("your boosters")) {
				e.setCancelled(true);
			}
			if(e.getClickedInventory().getTitle().equalsIgnoreCase("your boosters") && e.getClickedInventory().getItem(e.getSlot()) != null) {
				File playerFile = new File("plugins/SFA/playerBoosters/" + e.getWhoClicked().getUniqueId().toString() + ".yml");
				YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
				List<Booster> temp = (((List<Booster>)playerData.getList("boosters")));
				Booster selectedBooster = temp.get(e.getSlot());
				String color = "§3";
				temp.remove(e.getSlot());
				playerData.set("boosters", temp);
				BoosterManager.savePlayerBooster(playerData, playerFile);
				BoosterManager.addBooster(selectedBooster);
				if(selectedBooster.getType() == BoosterType.MONEY) color = "§6";
				e.getWhoClicked().sendMessage("§aYou added a(n) " + color + selectedBooster.getType().toString().toLowerCase() + " §ato the queue!");
				e.getClickedInventory().setItem(e.getSlot(), new ItemStack(Material.AIR));
				e.getWhoClicked().closeInventory();
			}
		}
	}

}
