package com.plochem.ssa.trading;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

public class TradeListener implements Listener{
	int[] accepterTradeSpots = {5,6,7,8,
			14,15,16,17,
			23,24,25,26,
			32,33,34,35,
			41,42,43,44};
	int[] requesterTradeSpots = {0,1,2,3,
			9,10,11,12,
			18,19,20,21,
			27,28,29,30,
			36,37,38,39};
	@EventHandler
	public void onclick(InventoryClickEvent e) {
		Player clicker = (Player)e.getWhoClicked();
		if(isRequester(clicker) || isAccepter(clicker)){
			if(e.getClickedInventory() != null && e.getClickedInventory().getItem(e.getSlot()) != null) {
				if(e.getInventory().getTitle().contains("trade")) {
					e.setCancelled(true);
					if(e.getClickedInventory().getTitle().contains("trade")) { // reclaim

					}
					if(e.getClickedInventory().getType() == InventoryType.PLAYER) { // put to ui
						int[] tradeSpots = requesterTradeSpots;
						if(isAccepter(clicker)) {
							tradeSpots = accepterTradeSpots;
						}
						for(int pos : tradeSpots) {
							if(e.getInventory().getItem(pos) == null) {
								e.getInventory().setItem(pos, e.getCurrentItem());
								break;
							}									
						}
					}
				}
			}

		}

	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		
	}

	private static boolean isRequester(Player p) {
		if(TradeManager.getTradeReq().containsKey(p.getUniqueId())) {
			return true;
		}
		return false;
	}

	private static boolean isAccepter(Player p) {
		if(TradeManager.getTradeReq().containsValue(p.getUniqueId())) {
			return true;
		}
		return false;
	}

}
