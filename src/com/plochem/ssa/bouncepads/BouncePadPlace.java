package com.plochem.ssa.bouncepads;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.plochem.ssa.SSkyblockAddon;

public class BouncePadPlace implements Listener{			
	private SSkyblockAddon sfa = SSkyblockAddon.getPlugin(SSkyblockAddon.class);
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		String displayName = p.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
		if((displayName != null) && (displayName.equals("§aBounce Pad")) && (p.getInventory().getItemInMainHand().getType() == Material.SLIME_BLOCK)) {
			Location place = e.getBlock().getLocation().add(0, 1, 0);
			@SuppressWarnings("unchecked")
			List<Location> locs = (List<Location>)sfa.getBpData().getList("bp.locs");
			if(locs == null) {
				locs = new ArrayList<Location>();
			}
			locs.add(place);
			sfa.getBpData().set("bp.locs", locs);
			place.getBlock().setType(Material.GOLD_PLATE);
	        sfa.saveBP();			
		}
	}
}
