package com.plochem.ssa.oregen;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;


public class OreGenListener implements Listener{

	@EventHandler
	public void onForm(BlockFromToEvent e) {
		Material type = e.getBlock().getType();
		if (type == Material.WATER || type == Material.STATIONARY_WATER || type == Material.LAVA || type == Material.STATIONARY_LAVA){
			Block b = e.getToBlock();
			if (b.getType() == Material.AIR){
				if (generatesCobble(type, b)){
					//UUID owner = ASkyBlockAPI.getInstance().getIslandAt(e.getBlock().getLocation()).getOwner();
					int r = new Random().nextInt(100) + 1; // 1- 100
					if(r == 1) { // 1
						e.getToBlock().setType(Material.EMERALD_ORE);
					} else if (r <= 4) { // 3
						e.getToBlock().setType(Material.DIAMOND_ORE);
					} else if(r <= 9) { // 5
						e.getToBlock().setType(Material.GOLD_ORE);
					} else if(r <= 14) { // 5
						e.getToBlock().setType(Material.IRON_ORE);
					} else if(r <= 24) { // 10
						e.getToBlock().setType(Material.LAPIS_ORE);
					} else if(r <= 34) { // 10
						e.getToBlock().setType(Material.REDSTONE_ORE);
					} else if(r <= 49) { // 15
						e.getToBlock().setType(Material.COAL_ORE);
					}
				}
			}
		}
	}

	private final BlockFace[] faces = new BlockFace[]{
			BlockFace.SELF,
			BlockFace.UP,
			BlockFace.DOWN,
			BlockFace.NORTH,
			BlockFace.EAST,
			BlockFace.SOUTH,
			BlockFace.WEST
	};

	public boolean generatesCobble(Material type, Block b){
		Material mirrorID1 = (type == Material.WATER || type == Material.STATIONARY_WATER ? Material.LAVA : Material.WATER);
		Material mirrorID2 = (type == Material.WATER || type == Material.STATIONARY_WATER ? Material.STATIONARY_LAVA : Material.STATIONARY_WATER);
		for (BlockFace face : faces){
			Block r = b.getRelative(face, 1);
			if (r.getType() == mirrorID1 || r.getType() == mirrorID2){
				return true;
			}
		}
		return false;
	}

}
