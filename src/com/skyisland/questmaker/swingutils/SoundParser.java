package com.skyisland.questmaker.swingutils;

import org.bukkit.Sound;

public class SoundParser implements StringParser<Sound> {

	private static SoundParser inst = null;
	
	public static SoundParser instance() {
		if (inst == null)
			inst = new SoundParser();
		
		return inst;
	}

	@Override
	public Sound parse(String text) {
		try {
			return Sound.valueOf(text);
		} catch (Exception e) {
			return null;
		}
	}

	
	
}
