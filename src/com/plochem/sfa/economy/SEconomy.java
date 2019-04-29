package com.plochem.sfa.economy;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

import com.plochem.sfa.SFactionAddon;

import net.milkbowl.vault.economy.Economy;

public class SEconomy {
	
	private SFactionAddon sfa;
    private Economy provider;
    private SEconomyImplementer economyImplementer;
    public final Map<UUID,Double> playerBank = new HashMap<>();
    
    public SEconomy(SFactionAddon sfa) {
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

}
