package com.plochem.ssa.generator;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class Generator implements ConfigurationSerializable{
	private Location loc;
	private GeneratorType type;
	private int level;
	private int numGenerated;
	private BlockFace signDir;
	
	public Generator(Location loc, GeneratorType type, int level, int numGenerated, BlockFace signDir) {
		this.loc = loc;
		this.type = type;
		this.level = level;
		this.numGenerated = numGenerated;
		this.signDir = signDir;
	}

	public Generator(Map<String, Object> map) {
		this.loc =(Location)map.get("loc");
		this.type = GeneratorType.valueOf((String)map.get("type"));
		this.level = (Integer)map.get("level");
		this.numGenerated = (Integer)map.get("numGenerated");
		this.signDir = BlockFace.valueOf((String)map.get("signDir"));
	}
	
	@Override
	public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("loc", loc);
        map.put("type", type.toString());
        map.put("level", level);
        map.put("numGenerated", numGenerated);
        map.put("signDir", signDir.toString());
        return map;
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	public GeneratorType getType() {
		return type;
	}

	public void setType(GeneratorType type) {
		this.type = type;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getNumGenerated() {
		return numGenerated;
	}

	public void setNumGenerated(int numGenerated) {
		this.numGenerated = numGenerated;
	}
	
	public BlockFace getSignDir() {
		return signDir;
	}
	
	public void setSignDir(BlockFace signDir) {
		this.signDir = signDir;
	}
	
}
