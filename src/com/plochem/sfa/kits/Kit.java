package com.plochem.sfa.kits;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

public class Kit implements ConfigurationSerializable{
	private int cooldown;
	private String name;
	private ItemStack itemRep;
	private List<ItemStack> items;
	
	public Kit(int cooldown, String name, ItemStack itemRep, List<ItemStack> items) {
		this.cooldown = cooldown;
		this.name = name;
		this.itemRep = itemRep;
		this.items = items;
	}
	
	@SuppressWarnings("unchecked")
	public Kit(Map<String, Object> map) {
		this.cooldown =(Integer)map.get("cooldown");
		this.name = (String)map.get("name");
		this.itemRep = (ItemStack)map.get("itemRep");
		this.items = (List<ItemStack>)map.get("items");
	}
	
	@Override
	public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("cooldown", cooldown);
        map.put("name", name);
        map.put("itemRep", itemRep);
        map.put("items", items);
        return map;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ItemStack getItemRep() {
		return itemRep;
	}

	public void setItemRep(ItemStack itemRep) {
		this.itemRep = itemRep;
	}

	public List<ItemStack> getItems() {
		return items;
	}

	public void setItems(List<ItemStack> items) {
		this.items = items;
	}
	
}
