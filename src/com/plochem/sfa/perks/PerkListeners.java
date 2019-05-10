package com.plochem.sfa.perks;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PerkListeners implements Listener{
	@EventHandler
	public void onKill(PlayerDeathEvent e) {
		Player killer = e.getEntity().getKiller();
		int currLevel = PerkManager.currentLevel(PerkType.REGENERATION, killer);
		if(currLevel > 0) {
			PerkType.REGENERATION.performAction(killer, null, currLevel);
		}
	}
	
	@EventHandler
	public void onReceiveDamage(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof LivingEntity) {
			Player damaged = (Player)e.getEntity();
			int currLevel = PerkManager.currentLevel(PerkType.DEFLECT, damaged);
			if(currLevel > 0) {
				PerkType.REGENERATION.performAction(damaged, (LivingEntity)e.getDamager(), currLevel);
			}
		}
	}
	
}
