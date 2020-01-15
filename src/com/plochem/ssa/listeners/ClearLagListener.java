package com.plochem.ssa.listeners;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import com.kiwifisher.mobstacker.utils.StackUtils;
import com.plochem.ssa.bosses.BossEntity;
import com.plochem.ssa.bosses.BossManager;

import me.minebuilders.clearlag.events.EntityRemoveEvent;

public class ClearLagListener implements Listener{
	@EventHandler
	public void onClear(ServerCommandEvent e) {
		if(e.getCommand().equals("/lagg clear")) {
			removeAllStacks();
			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "All stacks were successfully removed");
		}
	}
	
	@EventHandler
	public void onClear(PlayerCommandPreprocessEvent e) {
		if(e.getMessage().equals("/lagg clear")) {
			removeAllStacks();
			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "All stacks were successfully removed");
		}
	}
	
	@EventHandler
	public void onClear(EntityRemoveEvent e) {
		removeAllStacks();
	}

	
	private void removeAllStacks() {
        for(Iterator<World> i = Bukkit.getServer().getWorlds().iterator(); i.hasNext();)
        {
            World world = i.next();
            Iterator<LivingEntity> i2 = world.getLivingEntities().iterator();
            while(i2.hasNext()) 
            {
                LivingEntity entity = i2.next();
    			BossEntity hitBoss = BossManager.getCurrBosses().get(entity.getUniqueId());
    			if(hitBoss == null) { // not boss
                    if(StackUtils.hasRequiredData(entity))
                        entity.remove();
    			}
            }
        }
	}
}
