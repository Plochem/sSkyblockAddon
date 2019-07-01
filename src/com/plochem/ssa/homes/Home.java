package com.plochem.ssa.homes;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;


public class Home implements ConfigurationSerializable{
	private String name;
	private Location loc;
	
	public Home(String name, Location loc) {
		this.name = name;
		this.loc = loc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	public Home(Map<String, Object> map) {
		this.name = (String) map.get("name");
		this.loc = (Location) map.get("loc");
	}
	
	@Override
	public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("loc", loc);
        return map;
	}
	
}
