package com.plochem.ssa.cosmetics;

import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.plochem.ssa.SSkyblockAddon;
import com.plochem.ssa.cosmetics.types.ProjectileTrail;
import com.plochem.ssa.cosmetics.types.TrailEffect;


public class CosmeticListener implements Listener{

	@EventHandler
	public void arrowLauch(ProjectileLaunchEvent e) {
		if(e.getEntity().getShooter() instanceof Player){
			if(e.getEntity() instanceof FishHook) return;
			Player p = (Player) e.getEntity().getShooter();
			ProjectileTrail trail = CosmeticManager.projectile.get(p.getUniqueId());
			if(trail != null && trail != ProjectileTrail.Default){
				Projectile proj = e.getEntity();
				new BukkitRunnable(){
					@Override
					public void run() {
						if(proj.getLocation().getBlockY() < -55) {
							this.cancel();
							return;
						} else {
							trail.displayParticle(proj, p);
						}
					}
				}.runTaskTimer(SSkyblockAddon.getPlugin(SSkyblockAddon.class), 0, 3);

			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Player p = e.getPlayer();
				TrailEffect trail = CosmeticManager.trail.get(p.getUniqueId());
				if(trail == TrailEffect.Default || trail == null || !p.isOnline()) {
					this.cancel();
					return;
				} else {
					trail.displayParticle(p);
				}
			}
		}.runTaskTimerAsynchronously(SSkyblockAddon.getPlugin(SSkyblockAddon.class), 0, 4);
	}

}

