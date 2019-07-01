package com.plochem.ssa.staffheadabilities;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;

import com.plochem.ssa.events.SkullEquipEvent;

public class SkullEquipListeners implements Listener{
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){

		if(e.isCancelled()) return;
		if(e.getAction() == InventoryAction.NOTHING) return;

		if(e.getSlotType() != SlotType.ARMOR && e.getSlotType() != SlotType.QUICKBAR && e.getSlotType() != SlotType.CONTAINER) return;
		if(e.getClickedInventory() != null && !e.getClickedInventory().getType().equals(InventoryType.PLAYER)) return;
		if (!e.getInventory().getType().equals(InventoryType.CRAFTING) && !e.getInventory().getType().equals(InventoryType.PLAYER)) return;
		if(!(e.getWhoClicked() instanceof Player)) return;
		
		if(e.getCurrentItem().getType() == Material.SKULL_ITEM && e.getSlot() == 39) { //removing skull from head
			SkullEquipEvent skullEquipEvent = new SkullEquipEvent((Player) e.getWhoClicked(), e.getCurrentItem(), SkullActionType.UNEQUIP);
			Bukkit.getServer().getPluginManager().callEvent(skullEquipEvent);
			
		} 
		if(e.getCursor().getType() == Material.SKULL_ITEM && e.getSlot() == 39) { //equipping skull on head
			SkullEquipEvent skullEquipEvent = new SkullEquipEvent((Player) e.getWhoClicked(), e.getCursor(), SkullActionType.EQUIP);
			Bukkit.getServer().getPluginManager().callEvent(skullEquipEvent);
		}
	}
}
