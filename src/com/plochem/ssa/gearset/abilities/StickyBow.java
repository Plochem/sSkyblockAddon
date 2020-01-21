package com.plochem.ssa.gearset.abilities;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.plochem.ssa.SSkyblockAddon;
import com.plochem.ssa.gearset.Ability;

public class StickyBow extends Ability{

	public StickyBow() {
		super("Sticky Bow", 20, Arrays.asList("§7Chance of spawning a cobweb at the", "§7entity you shot with an arrow"));
	}

	@Override
	public void execute(Player caster, List<UUID> targets) {
		Random r = new Random();
		int num = r.nextInt(100);
		if(num < this.getChance()) {
			Entity p = Bukkit.getEntity(targets.get(0));
			p.sendMessage("§c" + caster.getName() + "'s Paladin gear set ability has been activated!");
			caster.sendMessage("§aYour Paladin gear set ability has been activated!");
			Location loc = p.getLocation();
			Material type = loc.getBlock().getType();
			loc.getBlock().setType(Material.WEB);
			p.teleport(loc);
			new BukkitRunnable() {	
				Location temp = loc;
				Material oldType = type;
				@Override
				public void run() {
					temp.getBlock().setType(oldType);
				}
			}.runTaskLater(SSkyblockAddon.getPlugin(SSkyblockAddon.class), 20*8);
		}

	}

}
