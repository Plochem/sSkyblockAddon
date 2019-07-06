package com.plochem.ssa.trading;

import java.util.UUID;

import org.bukkit.Bukkit;
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
	int[] accepterConfirm = {50,51,52,53};
	
	int[] requesterTradeSpots = {0,1,2,3,
			9,10,11,12,
			18,19,20,21,
			27,28,29,30,
			36,37,38,39};
	int[] requesterConfirm = {45,46,47,48};
	@EventHandler
	public void onclick(InventoryClickEvent e) {
		Player clicker = (Player)e.getWhoClicked();
		if(isRequester(clicker) || isAccepter(clicker)){
			if(e.getClickedInventory() != null && e.getClickedInventory().getItem(e.getSlot()) != null) {
				if(e.getInventory().getTitle().contains("trade")) {
					e.setCancelled(true);
					int[] tradeSpots = requesterTradeSpots;
					if(isAccepter(clicker)) {
						tradeSpots = accepterTradeSpots;
					}
					if(e.getClickedInventory().getTitle().contains("trade")) { // reclaim
						if(isInArray(e.getSlot(), tradeSpots)) {
							clicker.getInventory().addItem(e.getCurrentItem());
							e.getClickedInventory().setItem(e.getSlot(), null);
						}
					}
					if(e.getClickedInventory().getType() == InventoryType.PLAYER) { // clicked own inventory
						for(int pos : tradeSpots) {
							if(e.getInventory().getItem(pos) == null) { // available spot
								e.getInventory().setItem(pos, e.getCurrentItem());
								e.getClickedInventory().setItem(e.getSlot(), null);
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
		Player p = (Player)e.getPlayer();
		if(e.getInventory().getTitle().contains("trade")) {
			if(isRequester(p)) { // requester closed
				Player accepter = Bukkit.getPlayer(TradeManager.getTradeReq().get(p.getUniqueId()));
				accepter.sendMessage("§c" + p.getName() + " canceled the trade. Your items have been returned.");
				p.sendMessage("§cYou canceled the trade. Your items have been returned.");
				for(int i : requesterTradeSpots) {
					if(e.getInventory().getItem(i) != null)
						p.getInventory().addItem(e.getInventory().getItem(i));
				}
				for(int i : accepterTradeSpots) {
					if(e.getInventory().getItem(i) != null)
						accepter.getInventory().addItem(e.getInventory().getItem(i));
				}				
				TradeManager.getTradeReq().remove(p.getUniqueId());
				accepter.closeInventory();
			}
			else if(isAccepter(p)) { //accepter closed
				for(UUID id : TradeManager.getTradeReq().keySet()) {
					if(p.getUniqueId().equals(TradeManager.getTradeReq().get(id))) {
						Player requester = Bukkit.getPlayer(id);
						p.sendMessage("§cYou canceled the trade. Your items have been returned.");
						requester.sendMessage("§c" + p.getName() + " canceled the trade. Your items have been returned.");
						for(int i : accepterTradeSpots) {
							if(e.getInventory().getItem(i) != null)
								p.getInventory().addItem(e.getInventory().getItem(i));
						}
						for(int i : requesterTradeSpots) {
							if(e.getInventory().getItem(i) != null)
								requester.getInventory().addItem(e.getInventory().getItem(i));
						}
						TradeManager.getTradeReq().remove(id);
						requester.closeInventory();
						break;
					}
				}
				
			} else {
				return;
			}
		}
	}
	
	private static boolean isInArray(int num, int[] spots) {
		for(int i : spots) {
			if(num == i) return true;
		}
		return false;
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
