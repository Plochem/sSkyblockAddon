package com.plochem.ssa.stats;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.bgsoftware.superiorskyblock.Locale;
import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.api.events.IslandCreateEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandDisbandEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandJoinEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandKickEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandLeaveEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandWorthCalculatedEvent;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.handlers.GridHandler;
import com.bgsoftware.superiorskyblock.island.IslandRegistry;
import com.bgsoftware.superiorskyblock.utils.threads.SuperiorThread;
import com.bgsoftware.superiorskyblock.wrappers.SBlockPosition;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.math.transform.Transform;

public class StatsListener implements Listener {
	
	@EventHandler
	public void onKill(PlayerDeathEvent e) {
		Player killer = e.getEntity().getKiller();
		Player killed = e.getEntity();
		StatsManager.addDeath(killed);
		StatsManager.showScoreboard(killed);
		if(killer != null) {
			StatsManager.addKill(killer);
			StatsManager.showScoreboard(killer);
		}
	}
	
	@EventHandler
	public void onIslandJoin(IslandJoinEvent e) {
		e.getIsland().calcIslandWorth(e.getPlayer());
	}
	
	@EventHandler
	public void onIslandLeave(IslandLeaveEvent e) {
		for(UUID id : e.getIsland().getAllMembers()) {
			StatsManager.showScoreboard(Bukkit.getPlayer(id));
			StatsManager.updateTab(Bukkit.getPlayer(id));
		}
	}
	
	@EventHandler
	public void onIslandCreate(IslandCreateEvent e) {
		e.setCancelled(true);
		long startTime = System.currentTimeMillis();
		SuperiorSkyblockPlugin plugin = SuperiorSkyblockPlugin.getPlugin();
		Island island = e.getIsland();
		SuperiorPlayer superiorPlayer = e.getPlayer();
		try {
			Field islands = GridHandler.class.getDeclaredField("islands");
			islands.setAccessible(true);
			((IslandRegistry)islands.get(plugin.getGrid())).add(superiorPlayer.getUniqueId(), island);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException e1) {
			e1.printStackTrace();
		}
		
		Method method;
		try {
			method = GridHandler.class.getDeclaredMethod("setLastIsland", SBlockPosition.class);
			method.setAccessible(true);
			method.invoke(plugin.getGrid(), SBlockPosition.of(island.getCenter()));
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			e1.printStackTrace();
		}       

		String path = "plugins/SuperiorSkyblock2/schematics/";
		String schem = e.getSchematic() + ".schematic";
		File schematic = new File(path + schem);
		
		try {
			ClipboardFormat.findByFile(schematic).load(schematic).paste(new BukkitWorld(island.getCenter().getWorld()), BukkitUtil.toVector(island.getCenter()), false, true, (Transform) null);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
        island.getAllChunks(true).forEach(chunk -> plugin.getNMSAdapter().refreshChunk(chunk));
        island.setBonusWorth(BigDecimal.ZERO);
        island.setBiome(Biome.PLAINS);
        if (superiorPlayer.asOfflinePlayer().isOnline()) {
        	Locale.CREATE_ISLAND.send(superiorPlayer, SBlockPosition.of(island.getCenter()), System.currentTimeMillis() - startTime);
        	superiorPlayer.asPlayer().teleport(island.getCenter());
        	if (island.isInside(superiorPlayer.getLocation()))
        		Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.getNMSAdapter().setWorldBorder(superiorPlayer, island), 20L);
        	new SuperiorThread(() -> island.calcIslandWorth(null)).start();
        }
		plugin.getDataHandler().insertIsland(e.getIsland());

	}

	@EventHandler
	public void onIslandCreate(IslandKickEvent e) {
		for(UUID id : e.getIsland().getAllMembers()) {
			StatsManager.showScoreboard(Bukkit.getPlayer(id));
			StatsManager.updateTab(Bukkit.getPlayer(id));
		}
	}
	
	@EventHandler
	public void onIslandCreate(IslandDisbandEvent e) {
		for(UUID id : e.getIsland().getAllMembers()) {
			StatsManager.showScoreboard(Bukkit.getPlayer(id));
			StatsManager.updateTab(Bukkit.getPlayer(id));
		}
	}
	
	@EventHandler
	public void onIslandCreate(IslandWorthCalculatedEvent e) {
		for(UUID id : e.getIsland().getAllMembers()) {
			StatsManager.showScoreboard(Bukkit.getPlayer(id));
			StatsManager.updateTab(Bukkit.getPlayer(id));
		}

	}

	
}
