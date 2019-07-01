package com.plochem.ssa.boosters;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class Booster implements ConfigurationSerializable{
	private UUID uuid;
	private int timeLeft;
	private int duration;	
	private BoosterType type;
	private String name;
	
	public Booster(UUID uuid, int timeLeft, int duration, BoosterType type, String name) {
		this.uuid = uuid;
		this.timeLeft = timeLeft;
		this.duration = duration;
		this.type = type;
		this.name = name;
	}
	public Booster(Map<String, Object> map) {
		this.uuid = UUID.fromString((String)map.get("uuid"));
		this.timeLeft = (int)map.get("timeLeft");
		this.duration = (int)map.get("duration");
		this.type = BoosterType.valueOf((String)map.get("type"));
		this.name = (String) map.get("name");
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}
	
	public int getTimeLeft() {
		return timeLeft;
	}
	
	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public BoosterType getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("uuid", uuid.toString());
        map.put("timeLeft", timeLeft);
        map.put("duration", duration);
        map.put("type", type.toString());
        map.put("name", name);
        return map;
	}
	
}
