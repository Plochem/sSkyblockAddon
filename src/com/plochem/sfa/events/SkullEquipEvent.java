package com.plochem.sfa.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

import com.plochem.sfa.staffheadabilities.SkullActionType;

public class SkullEquipEvent extends PlayerEvent{
	private static final HandlerList handlers = new HandlerList();
	private ItemStack skull;
	private SkullActionType type;
	
	public SkullEquipEvent(Player player, ItemStack skull, SkullActionType type){
		super(player);
		this.skull = skull;
		this.type = type;
	}

	public static HandlerList getHandlerList(){
		return handlers;
	}

	public HandlerList getHandlers(){
		return handlers;
	}

	public ItemStack getSkull() {
		return skull;
	}
	
	public SkullActionType getType() {
		return type;
	}

}
