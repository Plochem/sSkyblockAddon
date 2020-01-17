package com.plochem.ssa.bosses.skills.special;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.plochem.ssa.SSkyblockAddon;
import com.plochem.ssa.bosses.BossEntity;
import com.plochem.ssa.bosses.skills.Skill;

public class Cobwebs extends Skill{

	public Cobwebs() {
		super("Cobwebs", 10, Arrays.asList("§7Spawns cobwebs under 2 random players", "§7within a 10 block radius"));
	}

	@Override
	public void cast(BossEntity boss, List<UUID> nearby) {
		Collections.shuffle(nearby);	
		for(int i = 0; i < Math.min(2, nearby.size()); i++) {
			Player p = Bukkit.getPlayer(nearby.get(i));
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
