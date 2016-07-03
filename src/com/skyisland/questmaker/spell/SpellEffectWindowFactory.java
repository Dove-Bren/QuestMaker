package com.skyisland.questmaker.spell;

import com.skyisland.questmanager.magic.spell.effect.DamageEffect;
import com.skyisland.questmanager.magic.spell.effect.DamageMPEffect;
import com.skyisland.questmanager.magic.spell.effect.DamageUndeadEffect;
import com.skyisland.questmanager.magic.spell.effect.FireEffect;
import com.skyisland.questmanager.magic.spell.effect.HealEffect;
import com.skyisland.questmanager.magic.spell.effect.MarkEffect;
import com.skyisland.questmanager.magic.spell.effect.RecallEffect;
import com.skyisland.questmanager.magic.spell.effect.SpellEffect;

public final class SpellEffectWindowFactory {

	public static enum EffectType {
		DAMAGE("Damage"),
		DAMAGEMP("Damage MP"),
		DAMAGEUNDEAD("Damage Undead"),
		FIRE("Fire"),
		HEAL("Heal"),
		MARK("Mark"),
		RECALL("Recall");
		
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
			return new SingleValueWindow<Double>(EffectType.DAMAGE.getTitle(), "Damage", 0.0) {
				@Override
				public SpellEffect getEffect() {
					return new DamageEffect(getValue());
				}
			};
		case DAMAGEMP:
			return new SingleValueWindow<Double>(EffectType.DAMAGEMP.getTitle(), "Damage", 0.0) {
				@Override
				public SpellEffect getEffect() {
					return new DamageMPEffect(getValue());
				}
			};
		case DAMAGEUNDEAD:
			return new SingleValueWindow<Double>(EffectType.DAMAGEUNDEAD.getTitle(), "Damage", 0.0) {
				@Override
				public SpellEffect getEffect() {
					return new DamageUndeadEffect(getValue());
				}
			};
		case HEAL:
			return new SingleValueWindow<Double>(EffectType.HEAL.getTitle(), "Amount", 0.0) {
				@Override
				public SpellEffect getEffect() {
					return new HealEffect(getValue());
				}
			};
		case FIRE:
			return new SingleValueWindow<Integer>(EffectType.FIRE.getTitle(), "Duration (Ticks)", 5) {
				@Override
				public SpellEffect getEffect() {
					return new FireEffect(getValue());
				}
			};
		case MARK:
			return new EmptyWindow(EffectType.MARK.getTitle()) {
				@Override
				public SpellEffect getEffect() {
					return new MarkEffect();
				}
			};
		case RECALL:
			return new EmptyWindow(EffectType.RECALL.getTitle()) {
				@Override
				public SpellEffect getEffect() {
					return new RecallEffect();
				}
			};
		}
		
		return null;
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
