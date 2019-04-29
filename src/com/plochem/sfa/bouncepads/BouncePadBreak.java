package com.plochem.sfa.bouncepads;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import com.plochem.sfa.SFactionAddon;

public class BouncePadBreak implements Listener{
	private SFactionAddon sfa = SFactionAddon.getPlugin(SFactionAddon.class);
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		removeBP(e);
	}
	
	@EventHandler
	public void onExplode(BlockExplodeEvent e) {
		removeBP(e);		
	}
	
	private void removeBP(BlockEvent e) {
		@SuppressWarnings("unchecked")
		List<Location> locs = (List<Location>)sfa.getBpData().getList("bp.locs");	
		if(locs != null) {
			if(e.getBlock().getType() == Material.SLIME_BLOCK) {
				locs.remove(e.getBlock().getLocation().add(0, 1, 0));
			} else if(e.getBlock().getType() == Material.GOLD_PLATE) {
		        locs.remove(e.getBlock().getLocation());
			} else {
				return;
			}
	        sfa.getBpData().set("bp.locs", locs);
	        sfa.saveBP();
		}
	}

	@EventHandler
	public void entityBreakBlock(EntityChangeBlockEvent e) {
		@SuppressWarnings("unchecked")
		List<Location> locs = (List<Location>)sfa.getBpData().getList("bp.locs");	
		if(locs != null) {
			if(e.getBlock().getType() == Material.SLIME_BLOCK) {
				locs.remove(e.getBlock().getLocation().add(0, 1, 0));
			} else if(e.getBlock().getType() == Material.GOLD_PLATE) {
		        locs.remove(e.getBlock().getLocation());
			}
	        sfa.getBpData().set("bp.locs", locs);
	        sfa.saveBP();
		}
	}
}
