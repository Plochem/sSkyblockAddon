package com.plochem.ssa.sellchest;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.plochem.ssa.SSkyblockAddon;

import net.brcdev.shopgui.ShopGuiPlusApi;
import net.brcdev.shopgui.api.exception.PlayerDataNotLoadedException;

public class SellChestListener implements Listener{
	@EventHandler
	public void onRightClick(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getPlayer().getInventory().getItemInMainHand().getType() == Material.BLAZE_ROD) {
			if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName() != null) {
				if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("§eSell-Chest Wand")){
					Block theChestBlock = e.getClickedBlock(); 
					BlockState state = theChestBlock.getState();
					if (state instanceof Chest) {
						e.setCancelled(true);
						Chest chest = (Chest) state;
						Inventory inventory = chest.getInventory();
						if (inventory instanceof DoubleChestInventory) {
							DoubleChest doubleChest = (DoubleChest) inventory.getHolder();
							inventory = doubleChest.getInventory();
						}
						if(e.getPlayer().isOp() || SuperiorSkyblockAPI.getIslandAt(e.getPlayer().getLocation()).isMember(SuperiorSkyblockAPI.getPlayer(e.getPlayer()))) {
							double soldCost = 0.0;
							int numItems = 0;
							for(int i = 0; i < inventory.getSize(); i++) {
								if(inventory.getItem(i) != null ) {
									try {
										double cost = ShopGuiPlusApi.getItemStackPriceSell(e.getPlayer(), inventory.getItem(i));
										if(cost != -1) {
											soldCost+=cost;
											numItems+=(inventory.getItem(i).getAmount());
											inventory.setItem(i, null);
										}
									} catch (PlayerDataNotLoadedException e1) {
										e.getPlayer().sendMessage("§aShop > §fAn error has occured while attempting to sell your items. Relog and try again.");
										e1.printStackTrace();
										return;
									}
								}
							}
							if(numItems == 0) {
								e.getPlayer().sendMessage("§aShop > §fThat chest does not contain any sellable items.");
							} else {
								soldCost*=1.25;
								e.getPlayer().sendMessage("§aShop > §fYou sold " + numItems + " items from your chest for a total of " + soldCost + "$. (x1.25 multiplier)");
								SSkyblockAddon.getPlugin(SSkyblockAddon.class).getSEconomy().getEconomyImplementer().depositPlayer(e.getPlayer(), soldCost);
								int amt = e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1;
								if(amt == 0) {
									e.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
								} else {
									e.getItem().setAmount(amt);						
								}
							}
						} else {
							e.getPlayer().sendMessage("§aShop > §fYou may not sell items from chests that are not on your island.");
						}

					}
				}
			}
		}
	}

}
