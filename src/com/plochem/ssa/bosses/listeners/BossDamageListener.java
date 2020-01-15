package com.plochem.ssa.bosses.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
		if(e.getDamager() instanceof Player) {
			BossEntity hitBoss = BossManager.getCurrBosses().get(e.getEntity().getUniqueId());
			if(hitBoss != null) {
				e.setDamage(e.getDamage() - hitBoss.getStats().getDefense());
				Double dmg = hitBoss.getPlayerDamage().get(e.getDamager().getUniqueId());
				if(dmg == null) dmg = 0.0;
				hitBoss.getPlayerDamage().put(e.getDamager().getUniqueId(), dmg + e.getDamage());
				hitBoss.setNameAndHealth();
			}
		}
	}
	
	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		BossEntity hitBoss = BossManager.getCurrBosses().get(e.getEntity().getUniqueId());
		if(hitBoss != null) {
			Bukkit.broadcastMessage(BossManager.prefix + "§7The " + hitBoss.getName() + " §7boss has been slain!");
			hitBoss.giveRewards();
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
