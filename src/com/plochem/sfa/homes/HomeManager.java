package com.plochem.sfa.homes;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.plochem.sfa.SFactionAddon;

public class HomeManager {
	
	public static Location loc = null;
	
	@SuppressWarnings("unchecked")
	public static void addHome(Player p, String[] args) {
		File f = new File("plugins/SFA/playerHomes/" + p.getUniqueId().toString() + ".yml");
		YamlConfiguration c = YamlConfiguration.loadConfiguration(f);
		if(args.length == 0) { // default home
			c.set("default", new Home("", p.getLocation()));
			p.sendMessage("§aYou have set the default home at §e(" + p.getLocation().getBlockX() + ", " + p.getLocation().getBlockY() + ", " + p.getLocation().getBlockZ() + ")§a.");
		} else { //TODO
			System.out.println(p.hasPermission("sfa.sethomemultiple2"));
			System.out.println(p.hasPermission("sfa.sethomemultiple4"));
			System.out.println(p.hasPermission("sfa.sethomemultiple6"));
			System.out.println(p.hasPermission("sfa.sethomemultiple8"));
			if(!p.hasPermission("sfa.sethomemultiple2") && !p.hasPermission("sfa.sethomemultiple4") && !p.hasPermission("sfa.sethomemultiple6") && !p.hasPermission("sfa.sethomemultiple8")) {
				p.sendMessage("§cYou do not have permission to set custom homes.");
				return;
			}
			List<Home> homes = (List<Home>) c.getList("homes");
			if(homes.size() == 2 && !p.hasPermission("sfa.sethomemultiple4")) {
				p.sendMessage("§cYou do not have permission to set more than 2 custom homes.");
			} else if(homes.size() == 4 && !p.hasPermission("sfa.sethomemultiple6")) {
				p.sendMessage("§cYou do not have permission to set more than 4 custom homes.");
			} else if(homes.size() == 6 && !p.hasPermission("sfa.sethomemultiple8")) {
				p.sendMessage("§cYou do not have permission to set more than 6 custom homes.");
			} else if(homes.size() == 8) {
				p.sendMessage("§cYou already have the maximum number of custom homes.");
			} else {
				Home target = null;
				for(Home h : homes) {
					if(h.getName().equalsIgnoreCase(args[0])) {
						target = h;
						break;
					}
				}
				if(target == null) { // home doesn't exist
					homes.add(new Home(args[0], p.getLocation()));
					p.sendMessage("§aYou created a new home called §e\"" + args[0].toLowerCase() + "\"§a at §e(" + p.getLocation().getBlockX() + ", " + p.getLocation().getBlockY() + ", " + p.getLocation().getBlockZ() + ")§a.");
				} else {
					target.setLoc(p.getLocation());
					p.sendMessage("§aYour home called §e\"" + args[0].toLowerCase() + "\"§a is now set at §e(" + p.getLocation().getBlockX() + ", " + p.getLocation().getBlockY() + ", " + p.getLocation().getBlockZ() + ")§a.");
				}
				c.set("homes", homes);
			}
		}
		saveHomes(f, c);
	}

	@SuppressWarnings("unchecked")
	public static void delHome(Player p, String[] args) {
		File f = new File("plugins/SFA/playerHomes/" + p.getUniqueId().toString() + ".yml");
		YamlConfiguration c = YamlConfiguration.loadConfiguration(f);
		if(args.length == 0) { // default home
			c.set("default", new Home("", null));
			p.sendMessage("§aYou deleted your default home.");
		} else {
			List<Home> homes = (List<Home>) c.getList("homes");
			Home target = null;
			for(Home h : homes) {
				if(h.getName().equalsIgnoreCase(args[0])) {
					target = h;
					break;
				}
			}
			if(target == null) { // home doesn't exist
				p.sendMessage("§cThe home you are attempting to delete does not exist.");
			} else {
				homes.remove(target);
				p.sendMessage("§cYou deleted your home called '" + args[0].toLowerCase() + "'§c.");
			}
			c.set("homes", homes);
		}
		saveHomes(f, c);
	}

	@SuppressWarnings("unchecked")
	public static void goHome(Player p, String[] args) {
		File f = new File("plugins/SFA/playerHomes/" + p.getUniqueId().toString() + ".yml");
		YamlConfiguration c = YamlConfiguration.loadConfiguration(f);
		SFactionAddon sfa = SFactionAddon.getPlugin(SFactionAddon.class);		
		List<UUID> runningSomeTPCmd = sfa.getWhoRunningSomeTPCmd();
		if(runningSomeTPCmd.contains(p.getUniqueId())) {
			p.sendMessage("§cWait for the command to finish executing!");
			return;
		}
		if(args.length == 0) { // default home
			loc = ((Home) c.get("default")).getLoc();
			if(loc == null) {
				p.sendMessage("§cYou do not have a default home set. Do '/sethome' to create one.");
				return;
			}

		} else { // custom home TODO: check if has perm
			List<Home> homes = (List<Home>) c.getList("homes");
			Home target = null;
			for(Home h : homes) {
				if(h.getName().equalsIgnoreCase(args[0])) {
					target = h;
					break;
				}
			}
			if(target == null) { // home doesn't exist
				p.sendMessage("§cA home with the name of '" + args[0].toLowerCase() + "' does not exist.");
				return;
			} else {
				loc = target.getLoc();
			}
		}

		runningSomeTPCmd.add(p.getUniqueId());
		new BukkitRunnable() {
			int time = 5;
			@Override
			public void run() {
				if(!runningSomeTPCmd.contains(p.getUniqueId())) {
					p.sendMessage("§cTeleportation has been canceled by your movement!");
					this.cancel();
					return;
				}
				if(time == 0) { 
					this.cancel();
					p.teleport(HomeManager.loc);
					runningSomeTPCmd.remove(p.getUniqueId());
					return;
				}
				p.sendMessage("§6Teleporting to your home in §c" + time + " §6second(s)...");
				time--;
			}
		}.runTaskTimer(sfa, 0, 20);
		
	}
	
	@SuppressWarnings("unchecked")
	public static void displayHomes(Player p) {
		File f = new File("plugins/SFA/playerHomes/" + p.getUniqueId().toString() + ".yml");
		YamlConfiguration c = YamlConfiguration.loadConfiguration(f);
		List<Home> homes = (List<Home>) c.getList("homes");
		if(homes.size() == 0) {
			p.sendMessage("§cYou do not have any custom homes set.");
		} else {
			p.sendMessage("§aHere is the list of your custom homes:");
			for(Home h : homes) {
				p.sendMessage("-§6 " + h.getName());
			}
		}
	}

	private static void saveHomes(File f, YamlConfiguration c) {

		try {
			c.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
