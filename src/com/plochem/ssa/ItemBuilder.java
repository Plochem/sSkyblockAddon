package com.plochem.ssa;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

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
	private boolean unsafeStackSize = false;

	public ItemBuilder(Material material, int amount) {
		if(material == null) material = Material.AIR;
		if(((amount > material.getMaxStackSize()) || (amount <= 0)) && (!unsafeStackSize)) amount = 1;
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
		if(enchantments.size() > 0) {
			item.addUnsafeEnchantments(enchantments);
		}
		if(displayname != null) {
			meta.setDisplayName(displayname);
		}
		if(lore.size() > 0) {
			meta.setLore(lore);
		}

		item.setItemMeta(meta);
		return item;
	}

	public ItemBuilder setNBTString(String key, String value) {
		Object nmsItem = CraftItemStack.asNMSCopy(item);
		Object compound = getNBTTagCompound(nmsItem);
		try {
			compound.getClass().getMethod("setString", String.class, String.class).invoke(compound, key, value);
			nmsItem = setNBTTag(compound, nmsItem);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
			ex.printStackTrace();
		}
		return this;
	}
	
	public String getNBTString(String key) {
		Object compound = getNBTTagCompound(CraftItemStack.asNMSCopy(item));
		try {
			return (String) compound.getClass().getMethod("getString", String.class).invoke(compound, key);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
			ex.printStackTrace();
		}
		return null;
	}

    private Object setNBTTag(Object tag, Object item) {
        try {
            item.getClass().getMethod("setTag", item.getClass()).invoke(item, tag);
            return item;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private Object getNBTTagCompound(Object nmsStack) {
        try {
            Object compound = nmsStack.getClass().getMethod("getTag").invoke(nmsStack);
            if(compound == null) {
                String ver = Bukkit.getServer().getClass().getPackage().getName().split(".")[3];
                return Class.forName("net.minecraft.server." + ver + ".NBTTagCompound").newInstance();
            }
            return compound;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}