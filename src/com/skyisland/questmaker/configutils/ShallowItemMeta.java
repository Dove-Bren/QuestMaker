package com.skyisland.questmaker.configutils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class ShallowItemMeta implements ItemMeta {

	private String displayName;
	
	private Set<ItemFlag> itemFlags;
	
	private List<String> lore;
	
	private Map<Enchantment, Integer> enchantments;
	
	public ShallowItemMeta() {
		displayName = null;
		itemFlags = new HashSet<>();
		lore = null;
		enchantments = new HashMap<>();
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();
		if (displayName != null)
			map.put("display-name", displayName);
		if (lore != null)
			map.put("lore", lore);
		if (!enchantments.isEmpty()) {
			Map<String, Integer> enchMap = new HashMap<>();
			for (Enchantment ench : enchantments.keySet()) {
				enchMap.put(ench.getName(), enchantments.get(ench));
			}
			map.put("enchants", enchMap);
		}
		
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public static ShallowItemMeta valueOf(Map<String, Object> map) {
		ShallowItemMeta item = new ShallowItemMeta();
		if (map.containsKey("display-name"))
			item.displayName = (String) map.get("display-name");
		if (map.containsKey("lore"))
			item.lore = (List<String>) map.get("lore");
		if (map.containsKey("enchants")) {
			Map<String, Integer> enchMap = (Map<String, Integer>) map.get("enchants");
			for (String key : enchMap.keySet()) {
				item.enchantments.put(Enchantment.getByName(key), enchMap.get(key));
			}
			
		}
		
		return item;
	}

	@Override
	public boolean addEnchant(Enchantment ench, int level, boolean ignoreLevelRestriction) {
		return enchantments.put(ench, level) != null;
	}

	@Override
	public void addItemFlags(ItemFlag... itemFlags) {
		for (ItemFlag flag : itemFlags)
			this.itemFlags.add(flag);
	}

	@Override
	public ItemMeta clone() {
		ShallowItemMeta meta = new ShallowItemMeta();
		meta.displayName = displayName;
		meta.lore = (lore == null ? null : Lists.newArrayList(lore));
		meta.itemFlags = Sets.newHashSet(itemFlags);
		meta.enchantments = Maps.newHashMap(enchantments);
		return meta;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public int getEnchantLevel(Enchantment ench) {
		Integer ret = enchantments.get(ench);
		return (ret == null ? 0 : ret);
	}

	@Override
	public Map<Enchantment, Integer> getEnchants() {
		return enchantments;
	}

	@Override
	public Set<ItemFlag> getItemFlags() {
		return itemFlags;
	}

	@Override
	public List<String> getLore() {
		return lore;
	}

	@Override
	public boolean hasConflictingEnchant(Enchantment ench) {
		return false;
	}

	@Override
	public boolean hasDisplayName() {
		return displayName != null;
	}

	@Override
	public boolean hasEnchant(Enchantment ench) {
		return enchantments.containsKey(ench);
	}

	@Override
	public boolean hasEnchants() {
		return enchantments.isEmpty();
	}

	@Override
	public boolean hasItemFlag(ItemFlag flag) {
		return itemFlags.contains(flag);
	}

	@Override
	public boolean hasLore() {
		return lore != null;
	}

	@Override
	public boolean removeEnchant(Enchantment ench) {
		return enchantments.remove(ench) != null;
	}

	@Override
	public void removeItemFlags(ItemFlag... itemFlags) {
		for (ItemFlag flag : itemFlags)
			this.itemFlags.remove(flag);
	}

	@Override
	public void setDisplayName(String name) {
		displayName = name;
	}

	@Override
	public void setLore(List<String> lore) {
		this.lore = lore;
	}

	@Override
	public Spigot spigot() {
		// TODO Auto-generated method stub
		return null;
	}
}
