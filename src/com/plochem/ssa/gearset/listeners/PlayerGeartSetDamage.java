package com.plochem.ssa.gearset.listeners;

import java.util.Arrays;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import com.plochem.ssa.gearset.GearSet;
import com.plochem.ssa.gearset.GearSetManager;

public class PlayerGeartSetDamage implements Listener{
	@EventHandler
	public void onDamage(ProjectileHitEvent e) {
		if(e.getEntity() instanceof Arrow && e.getEntity().getShooter() instanceof Player) {
			Player shooter = (Player)e.getEntity().getShooter();
			GearSet set = GearSetManager.getGearSet("Paladin");
			if(GearSetManager.isWearingSet(set, shooter)) {
				set.getAbility().execute(shooter, Arrays.asList(e.getHitEntity().getUniqueId()));
			}
		}
		
	}
}
