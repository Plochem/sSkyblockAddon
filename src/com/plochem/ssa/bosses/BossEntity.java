package com.plochem.ssa.bosses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.plochem.ssa.SSkyblockAddon;
import com.plochem.ssa.bosses.skills.Skill;

import net.minecraft.server.v1_12_R1.EnumMoveType;

public class BossEntity{
	private String name;
	private LivingEntity entity;
	private EntityType type;
	private List<Skill> specialSkills;
	private List<Skill> basicSkills;
	private List<String> info;
	private BossReward reward;
	private BossStatistics stats;
	private Map<UUID, Double> playerDamage = new HashMap<>();
	private Player target;
	private Tier tier;
	private boolean dead = false;
	
	private List<Entry<UUID,Double>> sortedDamage = new ArrayList<>();

	public BossEntity(String name, EntityType type, Tier tier, List<Skill> specialSkills, List<Skill> basicSkills, List<String> info, BossReward reward, BossStatistics stats) {
		this.name = tier.getColor() + name;
		this.type = type;
		this.tier = tier;
		this.specialSkills = specialSkills;
		this.basicSkills = basicSkills;
		this.info = info;
		this.reward = reward;
		this.stats = stats;
	}
	
	public BossEntity(BossEntity old) {
		this(old.getName(), old.getType(), old.getTier(), old.getSpecialSkills(), old.getBasicSkills(), old.getInfo(), old.getReward(), old.getStats());
	}

	public void giveRewards() {
		for(int i = 0; i < Math.min(sortedDamage.size(),3); i++) {
			String name = Bukkit.getPlayer(sortedDamage.get(i).getKey()).getName();
			for(String cmd : reward.getTopDmgCommands().get(i)) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%player%", name));
			}
		}

		for(int i = Math.min(sortedDamage.size(),3); i < sortedDamage.size(); i++) {
			String name = Bukkit.getPlayer(sortedDamage.get(i).getKey()).getName();
			for(String cmd : reward.getOtherDmgCommands()) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%player%", name));
			}
		}
	}
	
	public void showStats() {
		for(Entry<UUID, Double> e: sortedDamage) {
			Player p = Bukkit.getPlayer(e.getKey());
			if(p != null) {
				p.sendMessage("§a§l---------------------------------------------");
				p.sendMessage("§lBoss Results");
				p.sendMessage("");
				String[] place = {"§e§l1st Damager", "§6§l2nd Damager", "§c§l3rd Damager"};
				for(int i = 0; i < Math.min(sortedDamage.size(),3); i++) {
					String name = Bukkit.getPlayer(sortedDamage.get(i).getKey()).getName();
					Double dmg = sortedDamage.get(i).getValue();
					p.sendMessage(place[i] + " §7§l- §a" + name + " §7§l- " + dmg);
				}
				p.sendMessage("");
				p.sendMessage("§b§lYou §7§l- " + e.getValue());
				p.sendMessage("");
				p.sendMessage("§a§l---------------------------------------------");
			}
		}
		
	}

	public void spawn() {
		entity = (LivingEntity)BossManager.getWorld().spawnEntity(BossManager.getLoc(), type);
		setNameAndHealth();
		entity.setCustomNameVisible(true);
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(stats.getMaxHealth());
		entity.setHealth(stats.getMaxHealth());
		entity.setRemoveWhenFarAway(false);
		startSkillTask(this);
		startTargetTask();
		BossManager.getCurrBosses().put(entity.getUniqueId(), this);
		Bukkit.broadcastMessage(BossManager.prefix + "§7The " + name + " §7boss has spawned in the PVP arena!");
	}

	public void despawn() {
		entity.remove();
		BossManager.getCurrBosses().remove(entity.getUniqueId());
		dead = true;
	}

	public List<UUID> getNearbyPlayers(double radius){
		List<UUID> nearby = new ArrayList<>();
		List<Entity> nearbyBossEntities = this.getEntity().getNearbyEntities(radius, radius, radius);
		for (Entity entity : nearbyBossEntities) {
			if (!(entity instanceof Player)) continue;
			Player player = (Player) entity;
			if (player.getGameMode() == GameMode.SPECTATOR || player.getGameMode() == GameMode.CREATIVE) continue;
			nearby.add(player.getUniqueId());
		}
		return nearby;
	}

	private void startSkillTask(BossEntity boss) {
		int gcd = findGCD(stats.getSpecialInterval(), stats.getBasicInterval());
		new BukkitRunnable() {
			int cnt = 0;
			@Override
			public void run() {
				if(dead) this.cancel();
				cnt+=gcd;
				if(cnt % stats.getBasicInterval() == 0) {
					castRandomSkill(basicSkills);
				}
				if(cnt % stats.getSpecialInterval() == 0) {
					castRandomSkill(specialSkills);
				}
				if(cnt % stats.getBasicInterval() == 0 && cnt % stats.getSpecialInterval() == 0) {
					cnt = 0;
				}

			}
		}.runTaskTimer(SSkyblockAddon.getPlugin(SSkyblockAddon.class), 0, 20*gcd);
	}

	private void startTargetTask() {
		new BukkitRunnable() {
			int cnt = 0;
			@Override
			public void run() {
				if(dead) this.cancel();
				 // no target, or time to switch
				if(target == null || cnt == stats.getChangeTargetInterval() * 20) {
					findNewTarget();
					cnt = 0;
				}
				if(!target.getWorld().equals(BossManager.getWorld())) target = null; // remove target if in different world
				if(target != null) {
					navigateTo(target.getLocation(), stats.getSpeed());
					BossManager.getWorld().spawnParticle(Particle.REDSTONE, target.getLocation().add(0, 2.5, 0), 0, 1.0, 1.0/255, 1.0/255);
					cnt++;
				}
			}
		}.runTaskTimer(SSkyblockAddon.getPlugin(SSkyblockAddon.class), 0, 1); // change to 1
	}

	public void navigateTo(Location end, double speed) {
		double distance = end.distance(entity.getLocation());
		double ratio = distance / speed;
		double deltaX = (end.getX() - entity.getLocation().getX()) / ratio;
		//double deltaX = (end.getX() - entity.getLocation().getY());
		double deltaZ = (end.getZ() - entity.getLocation().getZ()) / ratio;
		((CraftLivingEntity)entity).getHandle().move(EnumMoveType.SELF, deltaX / 20, 0, deltaZ / 20); // x y z 
		Vector diff = end.toVector().subtract(entity.getLocation().toVector());
		Location loc = entity.getLocation();
		loc.setDirection(diff);
		entity.teleport(loc);
	}

	public void findNewTarget() {
		List<Entity> nearbyBossEntities = this.getEntity().getNearbyEntities(stats.getTargetRadius(), stats.getTargetRadius(), stats.getTargetRadius());
		Iterator<Entity> nearbyIterator = nearbyBossEntities.iterator();
		while(nearbyIterator.hasNext()) {
			if(!(nearbyIterator.next() instanceof Player)) { //removes all non-Players
				nearbyIterator.remove();
			}
		}
		if(nearbyBossEntities.size() > 0) {
			// get random player in radius
			Random rand = new Random();
			target = (Player)nearbyBossEntities.get(rand.nextInt(nearbyBossEntities.size()));
		} else {
			// if no random player, get last dmging player (handles noobs who only use bows)
			if(entity.getLastDamageCause() != null) {
				Entity lastHit = entity.getLastDamageCause().getEntity();
				if(lastHit != null) {
					if(lastHit instanceof Projectile) {
						if(((Projectile) lastHit).getShooter() instanceof Player){
							target = (Player)(((Projectile)lastHit).getShooter());
						}
					} else if(lastHit instanceof Player) {
						target = (Player)entity.getLastDamageCause().getEntity();
					}
				} else {
					target = null;
				}
			}
		}
	}

	public void castRandomSkill(List<Skill> skills) {
		Random rand = new Random();
		Skill randSkill = skills.get(rand.nextInt(skills.size()));
		List<UUID> nearby = getNearbyPlayers(randSkill.getRange());
		if(nearby != null && nearby.size() > 0) {
			randSkill.cast(this, nearby);
			BossManager.sendMessage(BossManager.prefix + "§7The " + name + " §7boss has cast the §b" + randSkill.getName() + " §7skill!");
		}
	}

	public void setNameAndHealth() {
		new BukkitRunnable() {
			@Override
			public void run() {
				entity.setCustomName(name + " §8§l| " + "§c" + entity.getHealth() + " ❤");				
			}
		}.runTaskLater(SSkyblockAddon.getPlugin(SSkyblockAddon.class), 5);
	}

	private int findGCD(int num1, int num2) {
		if(num2 == 0){
			return num1;
		}
		return findGCD(num2, num1%num2);
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

	public List<Skill> getSpecialSkills() {
		return specialSkills;
	}

	public void setSpecialSkills(List<Skill> specialSkills) {
		this.specialSkills = specialSkills;
	}

	public List<Skill> getBasicSkills() {
		return basicSkills;
	}

	public void setBasicSkills(List<Skill> basicSkills) {
		this.basicSkills = basicSkills;
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
	
	public void setTarget(Player target) {
		this.target = target;
	}

	public Player getTarget() {
		return target;
	}
	
	public Tier getTier() {
		return tier;
	}

	public void sortTopDamage(){
		List<Entry<UUID,Double>> sorted = new ArrayList<>(playerDamage.entrySet());
		Collections.sort(sorted, new Comparator<Entry<UUID,Double>>() { // custom comparator or nah?
			@Override
			public int compare(Entry<UUID,Double> o1, Entry<UUID,Double> o2) {
				if (o1.getValue() < o2.getValue()) {
					return 1;
				} else if (o1.getValue() > o2.getValue()) {
					return -1;
				}
				return 0;
			}
		});
		sortedDamage = sorted;
	}

}
