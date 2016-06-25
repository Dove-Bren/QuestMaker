package com.skyisland.questmaker.spell;

import com.skyisland.questmanager.magic.spell.effect.DamageEffect;
import com.skyisland.questmanager.magic.spell.effect.SpellEffect;

public final class SpellEffectWindowFactory {

	public static enum EffectType {
		DAMAGE("Damage");
		
		private String title;
		
		private EffectType(String title) {
			this.title = title;
		}
		
		@Override
		public String toString() {
			return title;
		}
		
		public String getTitle() {
			return title;
		}
	}
	
	public static SpellEffectWindow getWindow(EffectType type) {
		switch (type) {
		case DAMAGE:
			return new DamageEffectWindow();
		default:
			return null;
		}
	}
	
	/**
	 * Attempts to deduce the EffectType for the given effect from hard coded values.
	 * If no match is found, returns null
	 * @param effect
	 * @return
	 */
	public static EffectType resolveType(SpellEffect effect) {
		if (effect instanceof DamageEffect)
			return EffectType.DAMAGE;
		
		
		return null;
	}
	
}
