package com.skyisland.questmaker.configutils;

import org.bukkit.potion.PotionEffectType;

public class ShallowPotionEffectType extends PotionEffectType {

	private String name;
	
	public ShallowPotionEffectType(String name, int id) {
		super(id);
		this.name = name;
	}
	
	public ShallowPotionEffectType(String name, PotionEffectType type) {
		super(type.getId());
		this.name = name;
	}

	@Override
	public double getDurationModifier() {
		return PotionEffectType.getById(getId()).getDurationModifier();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isInstant() {
		return PotionEffectType.getById(getId()).isInstant();
	}

}
