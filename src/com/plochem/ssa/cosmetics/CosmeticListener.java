package com.plochem.ssa.cosmetics;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.plochem.ssa.SSkyblockAddon;
import com.plochem.ssa.cosmetics.types.ProjectileTrail;
import com.plochem.ssa.cosmetics.types.TrailEffect;


public class CosmeticListener implements Listener{
	
	private List<Projectile> launched = new ArrayList<>();
		
	@EventHandler
	public void arrowLauch(ProjectileLaunchEvent e) {
		if(e.getEntity().getShooter() instanceof Player){
			if(e.getEntity() instanceof FishHook) return;
			Player p = (Player) e.getEntity().getShooter();
			ProjectileTrail trail = CosmeticManager.projectile.get(p.getUniqueId());
			if(trail != null && trail != ProjectileTrail.Default){
				Projectile proj = e.getEntity();
				if(proj instanceof Arrow) {
					((Arrow)proj).setCritical(false);
				}
				launched.add(proj);
				new BukkitRunnable(){
					@Override
					public void run() {
						if(launched.contains(proj) && proj.getLocation().getBlockY() > -55) {
							trail.displayParticle(proj, p);
						} else {
							launched.remove(proj);
							this.cancel();
							return;
						}
					}
				}.runTaskTimer(SSkyblockAddon.getPlugin(SSkyblockAddon.class), 0, 1);

			}
		}
	}
	
	@EventHandler
	public void onArrowHit(ProjectileHitEvent e) {
		if(launched.contains(e.getEntity())){
			launched.remove(e.getEntity());
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		TrailEffect.particleRunnable(e.getPlayer());
	}

}

