package com.plochem.sfa.economy;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

public class SEconomyImplementer implements Economy{
	
	private SEconomy sEco;

	public SEconomyImplementer(SEconomy sEco) {
		this.sEco = sEco;
	}
	
	@Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double v) {
        return null;
    }

    @Override
    public String currencyNamePlural() {
        return null;
    }

    @Override
    public String currencyNameSingular() {
        return null;
    }

    @Override
    public boolean hasAccount(String s) {
        return false;
    }
    
    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return false;
    }

    @Override
    public boolean hasAccount(String s, String s1) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }

    @SuppressWarnings("deprecation")
	@Override
    public double getBalance(String s) {
    	// apparently, Factions uses this method so I have to use the String to look for a faction instead of a player
    	double total = 0;
    	Faction f = Factions.getInstance().getFactionById(s.split("-")[1]);
    	if(f==null) {
    		return 0;
    	}
    	for(FPlayer fp : f.getFPlayers()) {
    		total += getBalance(Bukkit.getOfflinePlayer(fp.getName()));
    	}
    	return total;
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        UUID uuid = offlinePlayer.getUniqueId();
        return sEco.playerBank.get(uuid);
    }

    @Override
    public double getBalance(String s, String s1) {
        Player player = Bukkit.getPlayer(s);
        UUID uuid = player.getUniqueId();
        return sEco.playerBank.get(uuid);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String s) {
        UUID uuid = offlinePlayer.getUniqueId();
        return sEco.playerBank.get(uuid);
    }

    @Override
    public boolean has(String s, double v) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        return false;
    }

    @Override
    public boolean has(String s, String s1, double v) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        return false;
    }

    @SuppressWarnings("deprecation")
	@Override
    public EconomyResponse withdrawPlayer(String s, double v) {

    	// apparently, Factions uses this method so i have to use the String to look for a faction instead of a player
    	double total = 0;
    	Faction f = Factions.getInstance().getFactionById(s.split("-")[1]);
    	if(f==null) {
    		System.out.println("awwww");
    		return new EconomyResponse(0, 0, ResponseType.FAILURE, "oof");
    	}
    	for(FPlayer fp : f.getFPlayers()) {
    		total += getBalance(Bukkit.getOfflinePlayer(fp.getName()));
    	}
        return new EconomyResponse(total, total - v, ResponseType.SUCCESS, "Withdrawed");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
        UUID uuid = offlinePlayer.getUniqueId();
        double oldBalance = sEco.playerBank.get(uuid);
        sEco.playerBank.put(uuid, oldBalance - v);
        updatePlayerBalanceFile(uuid);
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return null;
    }

	@SuppressWarnings("deprecation")
	@Override
    public EconomyResponse depositPlayer(String s, double v) { 
    	// apparently, Factions uses this method so i have to use the String to look for a faction instead of a player
    	double total = 0;
    	Faction f = Factions.getInstance().getFactionById(s.split("-")[1]);
    	if(f==null) {
    		System.out.println("awwww");
    		return new EconomyResponse(0, 0, ResponseType.FAILURE, "oof");
    	}
    	for(FPlayer fp : f.getFPlayers()) {
    		total += getBalance(Bukkit.getOfflinePlayer(fp.getName()));
    	}
        return new EconomyResponse(total, total + v, ResponseType.SUCCESS, "Got money in faction");
    }
    
    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        UUID uuid = offlinePlayer.getUniqueId();
        double oldBalance = sEco.playerBank.get(uuid);
        sEco.playerBank.put(uuid, oldBalance + v);
        updatePlayerBalanceFile(uuid);
        return new EconomyResponse(oldBalance, oldBalance + v, ResponseType.SUCCESS, "Successful deposit");
    }

    @Override
    public EconomyResponse depositPlayer(String s, String s1, double v) {
        Player player = Bukkit.getPlayer(s);
        UUID uuid = player.getUniqueId();
        double oldBalance = sEco.playerBank.get(uuid);
        sEco.playerBank.put(uuid, oldBalance + v);
        updatePlayerBalanceFile(uuid);
        return new EconomyResponse(oldBalance, oldBalance + v, ResponseType.SUCCESS, "Successful deposit");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        UUID uuid = offlinePlayer.getUniqueId();
        double oldBalance = sEco.playerBank.get(uuid);
        sEco.playerBank.put(uuid, oldBalance + v);
        updatePlayerBalanceFile(uuid);
        return new EconomyResponse(oldBalance, oldBalance + v, ResponseType.SUCCESS, "Successful deposit");
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "No bank account support");
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "No bank account support");
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "No bank account support");
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "No bank account support");
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "No bank account support");
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "No bank account support");
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "No bank account support");
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "No bank account support");
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "No bank account support");
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "No bank account support");
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "No bank account support");
    }
    
	@Override
	public List<String> getBanks() {
		return null;
	}

    @Override
    public boolean createPlayerAccount(String s) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }
    
    public void setBalance(OfflinePlayer offlinePlayer, double amt) {
        UUID uuid = offlinePlayer.getUniqueId();
        sEco.playerBank.put(uuid, amt);
        updatePlayerBalanceFile(uuid);
    }
    
    private void updatePlayerBalanceFile(UUID uuid) {
		File f = new File("plugins/SFA/playerbalance/" + uuid.toString() + ".yml");
		YamlConfiguration c = YamlConfiguration.loadConfiguration(f);
		c.set("balance", sEco.playerBank.get(uuid));
		try {
			c.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}
