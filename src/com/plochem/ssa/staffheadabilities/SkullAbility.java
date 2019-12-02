package com.plochem.ssa.staffheadabilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.plochem.ssa.events.SkullEquipEvent;

public class SkullAbility implements Listener{
	
	Random ran = new Random();
	Map<String, PotionEffectType> skullEffect = new HashMap<>();
	{
		skullEffect.put("Plochem", PotionEffectType.INCREASE_DAMAGE);
		skullEffect.put("iCodeRoses", PotionEffectType.FAST_DIGGING);
		skullEffect.put("Dartling", PotionEffectType.SPEED);
		skullEffect.put("IHayteU", PotionEffectType.REGENERATION);
		skullEffect.put("ForumAddict", PotionEffectType.NIGHT_VISION);
	}
	
	@EventHandler
	public void onSkullEquip(SkullEquipEvent e) {
		Player p = e.getPlayer();
		ItemStack skull = e.getSkull();
		SkullMeta sm = (SkullMeta) skull.getItemMeta();
		if(sm==null || !sm.hasOwner()) {
			return;
		}
		PotionEffectType type = skullEffect.get(sm.getOwningPlayer().getName());
		if(type == null) return;
		
		int amplifier = 0;
		if(type == PotionEffectType.FAST_DIGGING)
			amplifier = 2;
		else if(type == PotionEffectType.SPEED)
			amplifier = 1;
		if(e.getType() == SkullActionType.EQUIP) {
			p.addPotionEffect(new PotionEffect(type, Integer.MAX_VALUE, amplifier));
		} else {
			p.removePotionEffect(type);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event){
        Entity entity = event.getEntity();
        if(!(entity instanceof Player)){
            if(entity.getLastDamageCause() instanceof EntityDamageByEntityEvent){
                EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) entity.getLastDamageCause();
                if(entityDamageByEntityEvent.getDamager() instanceof Player){ // killer of mob = player
                	Player p = (Player) entityDamageByEntityEvent.getDamager();
                	int num = ((int)(Math.random()*200))+1;
                	if(num == 100) { // 0.5% chance
            			p.sendMessage("§aYou got lucky! A mask has dropped because you killed a mob!");
                		List<String> keys = new ArrayList<>(skullEffect.keySet());
                		String randName = keys.get(ran.nextInt(keys.size()));
                		ItemStack head = new ItemStack(Material.SKULL_ITEM);
                		head.setDurability((short) 3);
                		SkullMeta im = (SkullMeta) head.getItemMeta();
                		im.setDisplayName("§cMask of " + randName);
                		im.setOwningPlayer(Bukkit.getOfflinePlayer(randName));
                		head.setItemMeta(im);
                		p.getWorld().dropItem(entity.getLocation(), head);
                	}
                }
            }
        }
    }
}
