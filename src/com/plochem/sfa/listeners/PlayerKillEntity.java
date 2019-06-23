package com.plochem.sfa.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import com.plochem.sfa.SFactionAddon;
import com.plochem.sfa.boosters.BoosterManager;

public class PlayerKillEntity implements Listener{
	SFactionAddon sfa = SFactionAddon.getPlugin(SFactionAddon.class);
	@EventHandler
	public void onPlayerKill(EntityDeathEvent e) {
		Entity entity = e.getEntity();
		if(entity.getLastDamageCause() instanceof EntityDamageByEntityEvent){
			EntityDamageByEntityEvent nEvent = (EntityDamageByEntityEvent) entity.getLastDamageCause();
			Player killer;
			if(nEvent.getDamager() instanceof Projectile) {
				if(((Projectile) nEvent.getDamager()).getShooter() instanceof Player){
					killer = (Player)(((Projectile)nEvent.getDamager()).getShooter());
				} else {
					return;
				}
			} else {
				killer = (Player)nEvent.getDamager();
			}
			int baseMoney = 2;
			int baseXP = e.getDroppedExp();
			String boosterMessage = "";
			String expMessage = "";
			String killedWhoMessage = " (Killed a mob)";
			e.setDroppedExp(0);
			if(entity instanceof Player) {
				baseMoney = 5;
				killedWhoMessage = " (Killed a player)";
			}
			if(BoosterManager.moneyBoosterIsActive()) {
				baseMoney *= 2;
				boosterMessage = " §a(x2 money booster)";
			}
			if(BoosterManager.expBoosterIsActive()) {
				baseXP *= 2;
				expMessage = " §3(x2 EXP booster)";
			}
			killer.giveExp(baseXP);
			sfa.getSEconomy().getEconomyImplementer().depositPlayer(killer, baseMoney);
			killer.sendMessage("§a+$" + baseMoney + killedWhoMessage + boosterMessage);
			killer.sendMessage("§3+" + baseXP + " exp" + killedWhoMessage + expMessage);

		}
	}

}
