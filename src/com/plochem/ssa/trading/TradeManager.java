package com.plochem.ssa.trading;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TradeManager {
	
	private static Map<UUID, UUID> tradeReq = new HashMap<>();

	public static void trade(Player requester, Player p) {
		Inventory tradeGUI = Bukkit.createInventory(null, 54, requester.getName() + " and " + p.getName() + " trade");
		for(int i = 0; i < 5; i++ ) {
			tradeGUI.setItem(4 + (9*i), new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)15)); // black pane divider
		}
		ItemStack bookInfo = new ItemStack(Material.BOOK);
		ItemMeta meta = bookInfo.getItemMeta();
		meta.setDisplayName("§eHow does trading work?");
		meta.setLore(Arrays.asList(
				"§7Left-click on an item in your", 
				"§7inventory to put it up for trading.", 
				"§7Click on an item in your side of the", 
				"§7trading interface to cancel trading",
				"§7with it. When you are ready to confirm",
				"§7the trade, click on one of the red",
				"§7glass panes on your side. Click again",
				"§7to undo the confirmation. Whenever",
				"§7a change has been made to the trading",
				"§7interface, your trade confirmation will",
				"§7be canceled."));
		bookInfo.setItemMeta(meta);
		ItemStack confirm = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14);
		meta = confirm.getItemMeta();
		meta.setDisplayName("§c§lNOT READY");
		confirm.setItemMeta(meta);
		for(int i = 45; i <= 53; i++) {
			if(i==49)
				tradeGUI.setItem(i, bookInfo); // on hover shows useful instructions/info about trading
			else
				tradeGUI.setItem(i, confirm); // §l§aREADY				
		}
		requester.openInventory(tradeGUI);
		
	}
	
	public static Map<UUID, UUID> getTradeReq(){
		return tradeReq;
	}

}
