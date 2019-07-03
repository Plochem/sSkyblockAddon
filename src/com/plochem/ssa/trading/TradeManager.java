package com.plochem.ssa.trading;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TradeManager {

	public static void trade(Player requester, Player p) {
		Inventory tradeGUI = Bukkit.createInventory(null, 7, "Trade between " + requester.getName() + " and " + p.getName());
		
	}

}
