package com.plochem.ssa.listeners;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import com.kiwifisher.mobstacker.utils.StackUtils;
import com.plochem.ssa.SSkyblockAddon;
import com.plochem.ssa.boosters.BoosterManager;

public class PlayerKillEntity implements Listener{
	SSkyblockAddon sfa = SSkyblockAddon.getPlugin(SSkyblockAddon.class);
	@EventHandler
	public void onPlayerKill(EntityDeathEvent e) {
		Entity entity = e.getEntity(); // entity that dies
		if(!(entity instanceof Creature || entity instanceof Player)) return;
		if(entity.getLastDamageCause() instanceof EntityDamageByEntityEvent){
			EntityDamageByEntityEvent nEvent = (EntityDamageByEntityEvent) entity.getLastDamageCause();
			Player killer;
			if(nEvent.getDamager() instanceof Projectile) {
				if(((Projectile) nEvent.getDamager()).getShooter() instanceof Player){
					killer = (Player)(((Projectile)nEvent.getDamager()).getShooter());
				} else {
					return;
				}
			} else if(nEvent.getDamager() instanceof Player){
				killer = (Player)nEvent.getDamager();
			} else {
				return;
			}
			int baseMoney = 10;
			int baseXP = e.getDroppedExp();
			int stackSize = StackUtils.getStackSize(e.getEntity());
			String boosterMessage = "";
			String expMessage = "";
			String killedWhoMessage;
			e.setDroppedExp(0);
			if(entity instanceof Player) {
				baseMoney = 50;
				killedWhoMessage = " (Killed a player)";
			} else {
				if(stackSize == 0) stackSize = 1;
				baseMoney *= stackSize;
				killedWhoMessage = " (Killed " + stackSize + " mob(s))";
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
