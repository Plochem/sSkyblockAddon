package com.plochem.ssa.rewards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class Reward implements ConfigurationSerializable{
	private String name;
	private String permission;
	private List<String> commands = new ArrayList<>();
	private List<String> lore = new ArrayList<>();
	private RewardType type;
	private int position;
	
	public Reward(String name, String permission, List<String> commands, List<String> lore, RewardType type, int position) {
		this.name = name;
		this.permission = permission;
		this.commands = commands;
		this.lore = lore;
		this.type = type;
		this.position = position;
	}
	
	public String getName() {
		return name;
	}
	
	public List<String> getLore(){
		return lore;
	}

	public String getPermission() {
		return permission;
	}

	public List<String> getCommands() {
		return commands;
	}

	public RewardType getType() {
		return type;
	}

	public int getPosition() {
		return position;
	}
	
	@SuppressWarnings("unchecked")
	public Reward(Map<String, Object> map){
		this.name = ChatColor.translateAlternateColorCodes('&', (String) map.get("name"));
		this.permission = (String) map.get("permission");
		this.commands = (List<String>) map.get("commands");
		this.lore = (List<String>) map.get("lore");
		this.type = RewardType.valueOf((String)map.get("type"));
		this.position = (int) map.get("position");
		for(int i = 0; i < lore.size(); i++) {
			lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));
		}
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> reward = new HashMap<>();
		reward.put("name", name);
		reward.put("permission", permission);
		reward.put("commands", commands);
		reward.put("lore", lore);
		reward.put("type", type.toString());
		reward.put("position", position);
		return reward;
	}
	
	
}
