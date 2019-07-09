package com.plochem.ssa.trading;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TradeListener implements Listener{
	private int[] accepterTradeSpots = {5,6,7,8,
			14,15,16,17,
			23,24,25,26,
			32,33,34,35,
			41,42,43,44};
	private int[] accepterConfirm = {50,51,52,53};
	
	private int[] requesterTradeSpots = {0,1,2,3,
			9,10,11,12,
			18,19,20,21,
			27,28,29,30,
			36,37,38,39};
	private int[] requesterConfirm = {45,46,47,48};
	private ItemStack ready = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)5);
	private ItemStack notReady = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14);
	{
		ItemMeta meta = ready.getItemMeta();
		meta.setDisplayName("§a§lREADY");
		ready.setItemMeta(meta);
		meta = notReady.getItemMeta();
		meta.setDisplayName("§c§lNOT READY");
		notReady.setItemMeta(meta);
	}
	@EventHandler
	public void onclick(InventoryClickEvent e) {
		Player clicker = (Player)e.getWhoClicked();
		if(isRequester(clicker) || isAccepter(clicker)){
			if(e.getClickedInventory() != null && e.getClickedInventory().getItem(e.getSlot()) != null) {
				if(e.getInventory().getTitle().contains("trade")) {
					e.setCancelled(true);
					ItemStack confirm = notReady;
					int[] tradeSpots = requesterTradeSpots;
					int[] confirmSpots = requesterConfirm;
					if(isAccepter(clicker)) {
						tradeSpots = accepterTradeSpots;
						confirmSpots = accepterConfirm;
					}
					if(e.getClickedInventory().getTitle().contains("trade")) {
						if(isInArray(e.getSlot(), tradeSpots)) { // reclaim
							clicker.getInventory().addItem(e.getCurrentItem());
							e.getInventory().setItem(e.getSlot(), null);
							for(int i : requesterConfirm) e.getInventory().setItem(i, confirm); // cancel confirmation
							for(int i : accepterConfirm) e.getInventory().setItem(i, confirm);
						} else if(isInArray(e.getSlot(), confirmSpots)) { // confirm
							if(e.getCurrentItem().equals(notReady)) {
								if(tradeEmpty(e.getInventory(), tradeSpots)) { // check if there isnt an item in your side of trade.
									clicker.sendMessage("§cYou must have at least one item to trade before confirming.");
									return;
								} 
								confirm = ready;
							}
							for(int i : confirmSpots) {
								e.getInventory().setItem(i, confirm);
							}
							Player requester = null;
							Player accepter = null;
							if(bothReady(e.getInventory())) { //make trade
								for(HumanEntity entity : e.getViewers()) {
									if(isAccepter((Player)entity)) 
										accepter = (Player)entity;
									else
										requester = (Player)entity;
								}
								for(int i : requesterTradeSpots) {
									if(e.getInventory().getItem(i) != null)
										accepter.getInventory().addItem(e.getInventory().getItem(i));
								}
								for(int i : accepterTradeSpots) {
									if(e.getInventory().getItem(i) != null)
										requester.getInventory().addItem(e.getInventory().getItem(i));
								}
								TradeManager.getTradeReq().remove(requester.getUniqueId());
								accepter.closeInventory();
								requester.closeInventory();
								accepter.sendMessage("§aYou successfully completed the trade!");
								requester.sendMessage("§aYou successfully completed the trade!");
								return;
							}
						}
					} else if(e.getClickedInventory().getType() == InventoryType.PLAYER) { // clicked own inventory & add item
						for(int pos : tradeSpots) {
							if(e.getInventory().getItem(pos) == null) { // available spot
								e.getInventory().setItem(pos, e.getCurrentItem());
								e.getClickedInventory().setItem(e.getSlot(), null);
								for(int i : requesterConfirm) e.getInventory().setItem(i, confirm); // cancel confirmation
								for(int i : accepterConfirm) e.getInventory().setItem(i, confirm);
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
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(isRequester(p)) { // requester left
			Player accepter = Bukkit.getPlayer(TradeManager.getTradeReq().get(p.getUniqueId()));
			accepter.sendMessage("§cThe trade request has canceled because " + p.getName() + " is now offline.");
			TradeManager.getTradeReq().remove(p.getUniqueId());
		} else if(isAccepter(p)) { //accepter left
			for(UUID id : TradeManager.getTradeReq().keySet()) {
				if(p.getUniqueId().equals(TradeManager.getTradeReq().get(id))) {
					Player requester = Bukkit.getPlayer(id);
					requester.sendMessage("§cThe trade request has canceled because " + p.getName() + " is now offline.");
					TradeManager.getTradeReq().remove(id);
					break;
				}
			}
			
		} else {
			return;
		}
	}
	
	private boolean tradeEmpty(Inventory inv, int[] tradeSpots) {
		for(int i : tradeSpots) {
			if(inv.getItem(i) != null) {
				return false; // have at least 1 item to trade
			}
		}
		return true;
	}
	
	private boolean bothReady(Inventory trade) {
		for(int i : accepterConfirm) {
			if(!trade.getItem(i).equals(ready)) {
				return false;
			}
		}
		for(int i : requesterConfirm) {
			if(!trade.getItem(i).equals(ready)) {
				return false;
			}
		}
		return true;
	}
	
	private boolean isInArray(int num, int[] spots) {
		for(int i : spots) {
			if(num == i) return true;
		}
		return false;
	}

	private boolean isRequester(Player p) {
		if(TradeManager.getTradeReq().containsKey(p.getUniqueId())) {
			return true;
		}
		return false;
	}

	private boolean isAccepter(Player p) {
		if(TradeManager.getTradeReq().containsValue(p.getUniqueId())) {
			return true;
		}
		return false;
	}

}
