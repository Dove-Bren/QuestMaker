package com.skyisland.questmaker.configutils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Maps;

public class ShallowItemStack extends ItemStack {

	private Material type;
	
	private short durability;
	
	private int amount;
	
	private ItemMeta meta;
	
	public ShallowItemStack() {
		this.type = Material.AIR;
		this.durability = 0;
		this.amount = 1;
		this.meta = null;
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();
		map.put("type", type.name());
		if (amount != 1)
			map.put("amount", amount);
		if (durability != 0)
			map.put("damage", durability);
		if (meta != null)
			map.put("meta", meta);
		
		return map;
	}
	
	public static ShallowItemStack valueOf(Map<String, Object> map) {
		ShallowItemStack item = new ShallowItemStack();
		if (map.containsKey("type"))
			item.type = Material.matchMaterial((String) map.get("type"));
		if (map.containsKey("amount"))
			item.amount = (Integer) map.get("amount");
		if (map.containsKey("damage"))
			item.durability = (short) ((int) map.get("damage"));
		if (map.containsKey("meta"))
			item.meta = (ItemMeta) map.get("meta");
		
		return item;
	}
	
	@Override
	public void addEnchantment(Enchantment enchantment, int level) {
		if (meta == null)
			meta = new ShallowItemMeta();
		
		meta.addEnchant(enchantment, level, false);
	}
	
	@Override
	public void addUnsafeEnchantment(Enchantment enchantment, int level) {
		if (meta == null)
			meta = new ShallowItemMeta();
		
		meta.addEnchant(enchantment, level, true);
	}
	
	@Override
	public void addEnchantments(Map<Enchantment, Integer> enchantments) {
		if (meta == null)
			meta = new ShallowItemMeta();
		
		for (Entry<Enchantment, Integer> entry : enchantments.entrySet())
			meta.addEnchant(entry.getKey(), entry.getValue(), false);
	}
	
	@Override
	public void addUnsafeEnchantments(Map<Enchantment, Integer> enchantments) {
		if (meta == null)
			meta = new ShallowItemMeta();
		
		for (Entry<Enchantment, Integer> entry : enchantments.entrySet())
			meta.addEnchant(entry.getKey(), entry.getValue(), true);
	}
	
	@Override
	public ShallowItemStack clone() {
		ShallowItemStack item = new ShallowItemStack();
		item.amount = amount;
		item.durability = durability;
		item.type = type;
		item.meta = meta.clone();
		return item;
	}
	
	@Override
	public boolean containsEnchantment(Enchantment ench) {
		if (meta == null)
			return false;	
		return meta.hasEnchant(ench);
	}
	
	@Override
	public int getAmount() {
		return amount;
	}
	
	@Override
	public short getDurability() {
		return durability;
	}
	
	@Override
	public int getEnchantmentLevel(Enchantment ench) {
		if (meta == null)
			return 0;
		return meta.getEnchantLevel(ench);
	}
	
	@Override
	public Map<Enchantment, Integer> getEnchantments() {
		if (meta == null)
			return new HashMap<>();
		return Maps.newHashMap(meta.getEnchants());
	}
	
	@Override
	public ItemMeta getItemMeta() {
		return meta.clone();
	}
	
	@Override
	public Material getType() {
		return type;
	}
	
	@Override
	public boolean hasItemMeta() {
		return meta != null;
	}

	public void setMeta(ItemMeta meta) {
		this.meta = meta;
	}

	public void setType(Material type) {
		this.type = type;
	}

	public void setDurability(short durability) {
		this.durability = durability;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}
