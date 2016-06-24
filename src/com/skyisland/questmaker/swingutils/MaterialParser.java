package com.skyisland.questmaker.swingutils;

import org.bukkit.Material;

public class MaterialParser implements StringParser<Material> {

	private static MaterialParser inst = null;
	
	public static MaterialParser instance() {
		if (inst == null)
			inst = new MaterialParser();
		
		return inst;
	}

	@Override
	public Material parse(String text) {
		return Material.matchMaterial(text);
	}

	
	
}
