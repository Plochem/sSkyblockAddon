package com.plochem.ssa.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.plochem.ssa.SSkyblockAddon;
import com.plochem.ssa.economy.SEconomyImplementer;
import com.wasteofplastic.askyblock.ASkyBlockAPI;

public class GeneratorListeners implements Listener{
	private static BlockFace[] axis = { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
	private SEconomyImplementer eco = SSkyblockAddon.getPlugin(SSkyblockAddon.class).getSEconomy().getEconomyImplementer();

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		ItemMeta im = e.getItemInHand().getItemMeta();
		String name = im.getDisplayName();
		if(e.getBlock().getType() != Material.ENDER_PORTAL_FRAME || im.getLore() == null) return;
		int level = Integer.parseInt(ChatColor.stripColor(im.getLore().get(0).split(" ")[1]));
		String type = ChatColor.stripColor(im.getLore().get(1).split(" ")[1]);
		if(name.contains("Generator") && name.contains("§")) {
			name = ChatColor.stripColor(name).replaceAll(" Generator", "");
			BlockFace face = yawToFace(e.getPlayer().getLocation().getYaw());
			Block signBlock = e.getBlockPlaced().getRelative(face);
			if(signBlock.getType() != Material.AIR) {
				e.getPlayer().sendMessage("§cThere is not enough room to place this generator. Try to find another location.");
				e.setCancelled(true);
				return;
			}
			signBlock.setType(Material.WALL_SIGN);
			Sign sign = (Sign) (signBlock.getState());
			org.bukkit.material.Sign matSign =  new org.bukkit.material.Sign(Material.WALL_SIGN);
			matSign.setFacingDirection(face);
			sign.setData(matSign);
			sign.setLine(0, GeneratorType.valueOf(name.toUpperCase()).getColor() + name);
			sign.setLine(1, "§lAmount");
			sign.setLine(2, "0");
			sign.setLine(3, "§o§nClick to Claim");
			sign.update();
			GeneratorManager.add(new Generator(e.getBlock().getLocation(), GeneratorType.valueOf(type), level, 0, face));
		}
	}

	private BlockFace yawToFace(float yaw) {
		return axis[Math.round(yaw / 90f) & 0x3];
	} 

	@EventHandler
	public void genInteract(PlayerInteractEvent e) {
		if((e.getClickedBlock() != null) && (e.getClickedBlock().getType() == Material.WALL_SIGN)) {
			Sign sign = (Sign) (e.getClickedBlock().getState());
			org.bukkit.material.Sign matSign = (org.bukkit.material.Sign) sign.getData();
			Block block = e.getClickedBlock().getRelative(matSign.getAttachedFace()); // this block that the wall sign is attached to
			for(Generator gen : GeneratorManager.getGens()) {
				// prevents players from placing their own sign on their gen to claim any amount
				if(gen.getLoc().equals(block.getLocation()) && (gen.getSignDir() == matSign.getFacing())) {
					Player p = e.getPlayer();
					if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
						if(p.isOp() || ASkyBlockAPI.getInstance().playerIsOnIsland(e.getPlayer())) {
							int amt = gen.getNumGenerated();
							if(amt == 0) {
								p.sendMessage("§cThe generator is currently empty. Wait for at least one ore to be generated.");
								return;
							}
							if(amt != Integer.parseInt(sign.getLine(2))) {
								p.sendMessage("§cThe amount on the sign does not match with amount on the server. You'll receive the amount from the server instead.");
							}
							ItemStack items = new ItemStack(Material.valueOf(gen.getType().toString()), amt);
							HashMap<Integer,ItemStack> extra = p.getInventory().addItem(items);
							p.sendMessage("§aYou have claimed your ores!");
							//TODO sound
							if(!extra.isEmpty()) {	
								for(Entry<Integer, ItemStack> entry : extra.entrySet()){   
									p.getWorld().dropItem(p.getLocation(), entry.getValue());
								}
								p.sendMessage("§cSome ores dropped on the ground because your inventory is full.");
							}
							sign.setLine(2, "0");
							sign.update();
							gen.setNumGenerated(0);
						} else {
							p.sendMessage("§cYou may not claim ores from generators that are not on your island.");
						}
					}
					break;
				}
			}
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Block target = e.getBlock();
		if(e.getBlock().getType() == Material.WALL_SIGN) {
			Sign sign = (Sign) (e.getBlock().getState());
			org.bukkit.material.Sign matSign = (org.bukkit.material.Sign) sign.getData();
			target = e.getBlock().getRelative(matSign.getAttachedFace()); // the block that the wall sign is attached to
		} 
		if(target.getType() == Material.ENDER_PORTAL_FRAME) {
			for(Generator gen : GeneratorManager.getGens()) {
				if(gen.getLoc().equals(target.getLocation())) {
					GeneratorManager.remove(gen);
					target.setType(Material.AIR);
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "givegen " + e.getPlayer().getName() + " " + gen.getType().toString());
					break;
				}
			}
		}
	}

	@EventHandler
	public void onExplode(EntityExplodeEvent e) {
		for(Block block : new ArrayList<Block>(e.blockList())) {
			if(block.getType() == Material.WALL_SIGN) {
				Sign sign = (Sign) (block.getState());
				org.bukkit.material.Sign matSign = (org.bukkit.material.Sign) sign.getData();
				block = block.getRelative(matSign.getAttachedFace()); // the block that the wall sign is attached to
			} 
			if(block.getType() == Material.ENDER_PORTAL_FRAME) {
				for(Generator gen : GeneratorManager.getGens()) {
					if(gen.getLoc().equals(block.getLocation())) {
						e.blockList().remove(block);
						e.blockList().remove(block.getRelative(gen.getSignDir()));
						break;
					}
				}
			}
		}
	}

	@EventHandler
	public void onPurchase(InventoryClickEvent e) {
		if(e.getClickedInventory() != null && e.getClickedInventory().getItem(e.getSlot()) != null) {
			if(e.getInventory().getTitle().equalsIgnoreCase("generator shop")) {
				e.setCancelled(true);
				if(e.getClickedInventory().getTitle().equalsIgnoreCase("generator shop")) {
					ItemStack item = e.getCurrentItem();
					String name = item.getItemMeta().getDisplayName();
					if(name != null) {
						Player buyer = (Player)e.getWhoClicked();
						name = ChatColor.stripColor(name).replaceAll(" Generator", "").replaceAll(" ", "_");
						GeneratorType genType = GeneratorType.valueOf(name.toUpperCase());
						double cost = (double)genType.getCost();
						if(eco.getBalance(buyer) >= cost) {
							GeneratorManager.giveGenerator(Bukkit.getConsoleSender(), e.getWhoClicked().getName(), name);
							eco.withdrawPlayer(buyer, cost);
							buyer.sendMessage("§aYou purchased a(n) " + genType.getColor() + name + " §agenerator for $" + String.format("%,.2f", cost) + ".");
							// play sound
						} else {
							e.getWhoClicked().sendMessage("§cYou do not have enough money to purchase this generator.");
							// play sound
						}
					}
					e.getWhoClicked().closeInventory();
				}
			}
		}
	}


}
