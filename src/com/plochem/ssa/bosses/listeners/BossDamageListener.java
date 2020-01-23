package com.plochem.ssa.bosses.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.plochem.ssa.bosses.BossEntity;
import com.plochem.ssa.bosses.BossManager;

public class BossDamageListener implements Listener{
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		Entity entity = e.getEntity();
		BossEntity hitBoss = BossManager.getCurrBosses().get(entity.getUniqueId());
		if(hitBoss != null) {
			Player damager;
			if(e.getDamager() instanceof Projectile) {
				if(((Projectile) e.getDamager()).getShooter() instanceof Player){
					damager = (Player)(((Projectile)e.getDamager()).getShooter());
				} else {
					return;
				}
			} else if(e.getDamager() instanceof Player){
				damager = (Player)e.getDamager();
			} else {
				return;
			}

			e.setDamage(e.getDamage() - hitBoss.getStats().getDefense());
			Double dmg = hitBoss.getPlayerDamage().get(e.getDamager().getUniqueId());
			if(dmg == null) dmg = 0.0;
			hitBoss.getPlayerDamage().put(damager.getUniqueId(), dmg + e.getDamage());
			hitBoss.setNameAndHealth();
		}
	}

	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		BossEntity hitBoss = BossManager.getCurrBosses().get(e.getEntity().getUniqueId());
		if(hitBoss != null) {
			Bukkit.broadcastMessage(BossManager.prefix + "§7The " + hitBoss.getName() + " §7boss has been slain!");
			hitBoss.sortTopDamage();
			hitBoss.giveRewards();
			hitBoss.showStats();
			hitBoss.despawn();
		}
	}

	@EventHandler
	public void onRegen(EntityRegainHealthEvent e) {
		BossEntity hitBoss = BossManager.getCurrBosses().get(e.getEntity().getUniqueId());
		if(hitBoss != null) {
			hitBoss.setNameAndHealth();
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		for(BossEntity boss : BossManager.getCurrBosses().values()) {
			if(boss.getTarget().getUniqueId().equals(e.getEntity().getUniqueId())) {
				boss.setTarget(null);
			}
		}
	}

}
