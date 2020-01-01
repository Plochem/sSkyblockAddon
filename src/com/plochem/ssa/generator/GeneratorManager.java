package com.plochem.ssa.generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.plochem.ssa.SSkyblockAddon;


public class GeneratorManager {
	private static File genFile = new File("plugins/SFA/generators.yml");
	private static YamlConfiguration genData = YamlConfiguration.loadConfiguration(genFile);
	private static List<Generator> gens = new ArrayList<>();
	private static long lcm = 0;

	@SuppressWarnings("unchecked")
	public static void createGeneratorFile() {
		if(!(genFile.exists())) {
			Bukkit.getServer().getLogger().info("[SFA] Creating generator storage file!");
			saveGenFile();
		}  else {
			Bukkit.getServer().getLogger().info("[SFA] Generator storage file already exists! Skipping creation...");
			gens = ((List<Generator>)genData.getList("gens"));
		}
	}

	private static void saveGenFile() {
		genData.set("gens", gens);
		try {
			genData.save(genFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<Generator> getGens(){
		return gens;
	}

	public static void add(Generator gen) {
		gens.add(gen);
		saveGenFile();
		lcm = lcm();
		if(gens.size() == 1) startGlobalCounter();
	}
	
	public static void remove(Generator gen) {
		gens.remove(gen);
		saveGenFile();
		if(gens.size() >= 1)
			lcm = lcm();
	}

	public static void startGlobalCounter() {
        new BukkitRunnable() {
			int timeElapsed = -1;
			public void run() {
				if(gens.size() == 0) {
					this.cancel();
				}
				timeElapsed++;
				for(int i = 0; i < gens.size(); i++) {
					Generator gen = gens.get(i);
					if(timeElapsed >= gen.getType().getInterval() && timeElapsed % gen.getType().getInterval() == 0) {
						if(gen.getNumGenerated() < gen.getType().getMax()) { // limit
							gen.setNumGenerated(gen.getNumGenerated()+1);
							Block signBlock = gen.getLoc().getBlock().getRelative(gen.getSignDir());
							if(signBlock.getState().getType() != Material.WALL_SIGN) {
								remove(gen);
							} else {
								Sign sign = (Sign) (signBlock.getState());
								sign.setLine(2, String.valueOf(gen.getNumGenerated()));
								sign.update();
							}
						}
					}
				}
				saveGenFile();
				if(timeElapsed == lcm) timeElapsed = 0;
			}
        }.runTaskTimer(SSkyblockAddon.getPlugin(SSkyblockAddon.class), 0, 20*60); // TODO change to 1200 - runs every min
	}
	
	public static void giveGenerator(CommandSender sender, String name, String type) {
		if(!sender.hasPermission("sfa.givegen")) {
			sender.sendMessage("§cYou do not have permission to perform this command!");
		}

		Player target = Bukkit.getPlayer(name);
		if(target == null) {
			sender.sendMessage("§cSorry, but that player cannot be found!");
			return;
		}
		GeneratorType material = GeneratorType.valueOf(type.toUpperCase());
		if(material == null) {
			sender.sendMessage("§cYou entered an invalid item name. Try §8coal§c, §4redstone§c, §firon§c, §egold§c, §bdiamond§c, or §aemerald§c.");
			return;
		}
		ItemStack gen = new ItemStack(Material.ENDER_PORTAL_FRAME);
		ItemMeta genMeta = gen.getItemMeta();
		genMeta.setLore(Arrays.asList("§7Level: §e1", "§7Type: §e" + material.toString()));
		genMeta.setDisplayName(material.getColor() + material.toString() + " Generator");
		gen.setItemMeta(genMeta);
		HashMap<Integer, ItemStack> extra = target.getInventory().addItem(gen);
		if(!extra.isEmpty()) {
			for(Integer i : extra.keySet()) {
				target.getWorld().dropItem(target.getLocation(), extra.get(i));
			}
			target.sendMessage("§cYour inventory is full, so the generator is dropped on the ground.");
		}
	}

	private static long lcm() {
		int[] intervals = new int[gens.size()];
		for(int i = 0; i < gens.size(); i++) {
			intervals[i] = gens.get(i).getType().getInterval();
		}
	    long result = intervals[0];
	    for(int i = 1; i < intervals.length; i++) {
	    	result = lcm(result, intervals[i]);
	    }
	    return result;
	}
	private static long lcm(long a, long b) {
		if (a == 0 || b == 0) {
			return 0;
		}
		return (a * b) / gcd(a, b);
	}

	private static long gcd(long a, long b) {
		long remainder = 0;
		do {
			remainder = a % b;
			a = b; 
			b = remainder;
		} while (b != 0);
		return a;
	}
}
