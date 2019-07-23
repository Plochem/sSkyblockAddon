package com.plochem.ssa.oregen;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class OreManager implements Listener{
	  public static void openOresMenu(Player player){
	    Inventory inv = Bukkit.getServer().createInventory(null, 9, "§8§lORES MENU");
	    
	    ItemStack item2 = new ItemStack(Material.EMERALD_ORE);
	    ItemMeta item1Meta = item2.getItemMeta();
	    List<String> item1Lore = new ArrayList<>();
	    item1Lore.add(" §b§l* §eMined: §7" + player.getStatistic(Statistic.MINE_BLOCK, Material.EMERALD_ORE));
	    item1Meta.setDisplayName("§a§lEMERALD §8» §7Ore");
	    item1Meta.setLore(item1Lore);
	    item2.setItemMeta(item1Meta);
	    
	    ItemStack item3 = new ItemStack(Material.DIAMOND_ORE);
	    ItemMeta item3Meta = item3.getItemMeta();
	    List<String> item3Lore = new ArrayList<>();
	    item3Lore.add(" §b§l* §eMined: §7" + player.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE));
	    item3Meta.setDisplayName("§b§lDIAMOND §7Ore");
	    item3Meta.setLore(item3Lore);
	    item3.setItemMeta(item3Meta);
	    
	    ItemStack item4 = new ItemStack(Material.GOLD_ORE);
	    ItemMeta item4Meta = item4.getItemMeta();
	    List<String> item4Lore = new ArrayList<>();
	    item4Lore.add(" §b§l* §eMined: §7" + player.getStatistic(Statistic.MINE_BLOCK, Material.GOLD_ORE));
	    item4Meta.setDisplayName("§6§lGOLD §7Ore");
	    item4Meta.setLore(item4Lore);
	    item4.setItemMeta(item4Meta);
	    
	    ItemStack item5 = new ItemStack(Material.IRON_ORE);
	    ItemMeta item5Meta = item5.getItemMeta();
	    List<String> item5Lore = new ArrayList<>();
	    item5Lore.add(" §b§l* §eMined: §7" + player.getStatistic(Statistic.MINE_BLOCK, Material.IRON_ORE));
	    item5Meta.setDisplayName("§f§lIRON §7Ore");
	    item5Meta.setLore(item5Lore);
	    item5.setItemMeta(item5Meta);
	    
	    ItemStack item6 = new ItemStack(Material.LAPIS_ORE);
	    ItemMeta item6Meta = item6.getItemMeta();
	    List<String> item6Lore = new ArrayList<>();
	    item6Lore.add(" §b§l* §eMined: §7" + player.getStatistic(Statistic.MINE_BLOCK, Material.LAPIS_ORE));
	    item6Meta.setDisplayName("§9§lLAPIS §7Ore");
	    item6Meta.setLore(item6Lore);
	    item6.setItemMeta(item6Meta);
	    
	    ItemStack item7 = new ItemStack(Material.REDSTONE_ORE);
	    ItemMeta item7Meta = item7.getItemMeta();
	    List<String> item7Lore = new ArrayList<>();
	    item7Lore.add(" §b§l* §eMined: §7" + player.getStatistic(Statistic.MINE_BLOCK, Material.REDSTONE_ORE));
	    item7Meta.setDisplayName("§c§lREDSTONE §7Ore");
	    item7Meta.setLore(item7Lore);
	    item7.setItemMeta(item7Meta);
	    
	    ItemStack item8 = new ItemStack(Material.COAL_ORE);
	    ItemMeta item8Meta = item8.getItemMeta();
	    List<String> item8Lore = new ArrayList<>();
	    item8Lore.add(" §b§l* §eMined: §7" + player.getStatistic(Statistic.MINE_BLOCK, Material.COAL_ORE));
	    item8Meta.setDisplayName("§8§lCOAL §7Ore");
	    item8Meta.setLore(item8Lore);
	    item8.setItemMeta(item8Meta);
	    
	    ItemStack border = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)15);
	    ItemMeta borderMeta = border.getItemMeta();
	    borderMeta.setDisplayName("§8 ");
	    border.setItemMeta(borderMeta);
	    
	    inv.setItem(0, border);
	    inv.setItem(1, item2);
	    inv.setItem(2, item3);
	    inv.setItem(3, item4);
	    inv.setItem(4, item5);
	    inv.setItem(5, item6);
	    inv.setItem(6, item7);
	    inv.setItem(7, item8);
	    inv.setItem(8, border);
	    
	    player.openInventory(inv);
	  }
	  
	  @EventHandler
	  public void onInventoryClick(InventoryClickEvent e){
		if(e.getClickedInventory() != null && e.getClickedInventory().getItem(e.getSlot()) != null) {
			if(e.getInventory().getTitle().equalsIgnoreCase("§8§lores menu")) {
				e.setCancelled(true);
			}
		}
	  }
	  
}
