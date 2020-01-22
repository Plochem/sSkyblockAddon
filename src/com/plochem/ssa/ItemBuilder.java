package com.plochem.ssa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import net.minecraft.server.v1_12_R1.NBTTagCompound;

public class ItemBuilder {
	private ItemStack item;
	private ItemMeta meta;
	private Material material = Material.STONE;
	private int amount = 1;
	private MaterialData data;
	private short damage = 0;
	private Map<Enchantment, Integer> enchantments = new HashMap<>();
	private String displayname;
	private List<String> lore = new ArrayList<>();

	public ItemBuilder(Material material, int amount) {
		if(material == null) material = Material.AIR;
		this.amount = amount;
		this.item = new ItemStack(material, amount);
		this.material = material;
	}
	
	public ItemBuilder(ItemStack item) {
        this.item = item;
        this.material = item.getType();
        this.amount = item.getAmount();
        this.data = item.getData();
        this.damage = item.getDurability();
        this.enchantments = item.getEnchantments();
        if(item.hasItemMeta()) {
        	this.meta = item.getItemMeta();
            this.displayname = item.getItemMeta().getDisplayName();
            this.lore = item.getItemMeta().getLore();
        }
	}

	public ItemBuilder data(MaterialData data) {
		this.data = data;
		return this;
	}

	public ItemBuilder durability(short damage) {
		this.damage = damage;
		return this;
	}

	public ItemBuilder meta(ItemMeta meta) {
		this.meta = meta;
		return this;
	}

	public ItemBuilder enchant(Enchantment enchant, int level) {
		enchantments.put(enchant, level);
		return this;
	}

	public ItemBuilder enchant(Map<Enchantment, Integer> enchantments) {
		this.enchantments = enchantments;
		return this;
	}

	public ItemBuilder displayname(String displayname) {
		this.displayname = displayname;
		return this;
	}

	public ItemBuilder lore(String... lines) {
		for (String line : lines) {
			lore.add(line);
		}
		return this;
	}

	public ItemBuilder lore(String line, int index) {
		lore.set(index, line);
		return this;
	}

	public String getDisplayname() {
		return displayname;
	}

	public int getAmount() {
		return amount;
	}

	public Map<Enchantment, Integer> getEnchantments() {
		return enchantments;
	}

	public short getDurability() {
		return damage;
	}

	public List<String> getLores() {
		return lore;
	}

	public Material getMaterial() {
		return material;
	}

	public ItemMeta getMeta() {
		return meta;
	}

	public MaterialData getData() {
		return data;
	}

	public ItemStack build() {
		item.setType(material);
		item.setAmount(amount);
		item.setDurability(damage);
		meta = item.getItemMeta();
		if(data != null) {
			item.setData(data);
		}
		if(displayname != null) {
			meta.setDisplayName(displayname);
		}
		if(lore.size() > 0) {
			meta.setLore(lore);
		}

		item.setItemMeta(meta);
		
		if(enchantments.size() > 0) {
			item.addUnsafeEnchantments(enchantments);
		}
		return item;
	}

	public ItemBuilder setNBTString(String key, String value) {
		net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		NBTTagCompound nbtCompounnd = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
		nbtCompounnd.setString(key, value);
		nmsItem.setTag(nbtCompounnd);
		item = CraftItemStack.asBukkitCopy(nmsItem);
		return this;
	}
	
	public String getNBTString(String key) {
		NBTTagCompound nbtCompound = CraftItemStack.asNMSCopy(item).getTag();
		if(nbtCompound == null) return null;
		return nbtCompound.getString(key);
	}
}