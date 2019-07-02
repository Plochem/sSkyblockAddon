package com.plochem.ssa.economy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

import com.plochem.ssa.SSkyblockAddon;

import net.milkbowl.vault.economy.Economy;

public class SEconomy {
	
	private SSkyblockAddon sfa;
    private Economy provider;
    private SEconomyImplementer economyImplementer;
    public final Map<UUID,Double> playerBank = new HashMap<>();
    
    public SEconomy(SSkyblockAddon sfa) {
    	this.sfa = sfa;
    	this.economyImplementer = new SEconomyImplementer(this);
    }

    public void hook() {
        provider = getEconomyImplementer();
        Bukkit.getServicesManager().register(Economy.class, provider, sfa, ServicePriority.Normal);
    }

    public void unhook() {
        Bukkit.getServicesManager().unregister(Economy.class, this.provider);
    }

	public SEconomyImplementer getEconomyImplementer() {
		return economyImplementer;
	}
	
	public void loadToMap(UUID uuid, double amount) {
		playerBank.put(uuid, amount);
	}
	
	public List<Entry<UUID, Double>> getTopBals(){
	    List<Entry<UUID,Double>> results = new ArrayList<>(playerBank.entrySet());
	    Collections.sort(results, new Comparator<Entry<UUID,Double>>() { // custom comparator or nah?
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
	    return results.subList(0, Math.min(10, results.size()));
		
	}

}
