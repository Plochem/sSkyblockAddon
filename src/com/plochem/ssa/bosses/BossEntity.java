package com.plochem.ssa.bosses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.plochem.ssa.bosses.skills.Skill;

public class BossEntity {
	private String name;
	private LivingEntity entity;
	private EntityType type;
	private List<Skill> skills;
	private List<String> info;
	private BossReward reward;
	private BossStatistics stats;
	private Map<UUID, Double> playerDamage = new HashMap<>();
	
	public BossEntity(String name, String type, List<Skill> skills, List<String> info, BossReward reward, BossStatistics stats) {
		this.name = name;
		this.type = EntityType.valueOf(type);
		this.skills = skills;
		this.info = info;
		this.reward = reward;
		this.stats = stats;
	}
	
	public void spawn() {
		entity = (LivingEntity)Bukkit.getWorld(BossManager.getWorld()).spawnEntity(BossManager.getLoc(), type);
		entity.setCustomName(name);
		entity.setCustomNameVisible(true);
		BossManager.getCurrBosses().put(entity.getUniqueId(), this);
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public EntityType getType() {
		return type;
	}
	
	public LivingEntity getEntity() {
		return entity;
	}
	
	public List<Skill> getSkills() {
		return skills;
	}
	
	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}
	
	public List<String> getInfo() {
		return info;
	}
	
	public void setInfo(List<String> info) {
		this.info = info;
	}
	
	public BossReward getReward() {
		return reward;
	}
	
	public void setReward(BossReward reward) {
		this.reward = reward;
	}
	
	public BossStatistics getStats() {
		return stats;
	}
	
	public void setStats(BossStatistics stats) {
		this.stats = stats;
	}
	
	public Map<UUID, Double> getPlayerDamage() {
		return playerDamage;
	}
	
	public List<UUID> getNearbyPlayers(){
		List<UUID> nearby = new ArrayList<>();
        List<Entity> nearbyBossEntities = this.getEntity().getNearbyEntities(stats.getTargetRadius(), stats.getTargetRadius(),stats.getTargetRadius());
        for (Entity entity : nearbyBossEntities) {
            if (!(entity instanceof Player)) continue;
            Player player = (Player) entity;
            if (player.getGameMode() == GameMode.SPECTATOR || player.getGameMode() == GameMode.CREATIVE) continue;
            nearby.add(player.getUniqueId());
        }
        return nearby;
	}
	
}
