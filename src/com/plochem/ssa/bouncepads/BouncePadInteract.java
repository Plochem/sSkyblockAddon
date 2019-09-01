package com.plochem.ssa.bouncepads;

import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.plochem.ssa.SSkyblockAddon;

public class BouncePadInteract implements Listener{
	private SSkyblockAddon sfa = SSkyblockAddon.getPlugin(SSkyblockAddon.class);
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		List<Location> locs = (List<Location>)sfa.getBpData().getList("bp.locs");		
		if(e.getAction() == Action.PHYSICAL && e.getClickedBlock().getType() == Material.GOLD_PLATE && locs != null && locs.contains( e.getClickedBlock().getLocation())) {
			p.getWorld().playSound(p.getLocation(), Sound.WOOD_CLICK, 3.0F, 3.0F);
	        p.getWorld().playEffect(p.getLocation(), Effect.SMOKE, 3);
	        p.setVelocity(p.getLocation().getDirection().normalize().multiply(30).setY(2));
		}
	}
}
