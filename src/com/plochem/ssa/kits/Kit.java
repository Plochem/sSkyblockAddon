package com.plochem.ssa.kits;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

public class Kit implements ConfigurationSerializable{
	private int cooldown;
	private String name;
	private ItemStack itemRepOwn;
	private ItemStack itemRepNotOwn;
	private List<ItemStack> items;
	
	public Kit(int cooldown, String name, ItemStack itemRepOwn, ItemStack itemRepNotOwn,List<ItemStack> items) {
		this.cooldown = cooldown;
		this.name = name;
		this.itemRepOwn = itemRepOwn;
		this.itemRepNotOwn = itemRepNotOwn;
		this.items = items;
	}
	
	@SuppressWarnings("unchecked")
	public Kit(Map<String, Object> map) {
		this.cooldown =(Integer)map.get("cooldown");
		this.name = (String)map.get("name");
		this.itemRepOwn = (ItemStack)map.get("itemRepOwn");
		this.itemRepNotOwn = (ItemStack)map.get("itemRepNotOwn");
		this.items = (List<ItemStack>)map.get("items");
	}
	
	@Override
	public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("cooldown", cooldown);
        map.put("name", name);
        map.put("itemRepOwn", itemRepOwn);
        map.put("itemRepNotOwn", itemRepNotOwn);
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

	public ItemStack getItemRepOwn() {
		return itemRepOwn;
	}

	public void setItemRepOwn(ItemStack itemRepOwn) {
		this.itemRepOwn = itemRepOwn;
	}
	
	public ItemStack getItemRepNotOwn() {
		return itemRepNotOwn;
	}

	public void setItemRepNotOwn(ItemStack itemRepNotOwn) {
		this.itemRepNotOwn = itemRepNotOwn;
	}

	public List<ItemStack> getItems() {
		return items;
	}

	public void setItems(List<ItemStack> items) {
		this.items = items;
	}
	
}
