package com.skyisland.questmaker.spell;

import java.awt.Component;

import com.skyisland.questmanager.magic.spell.effect.SpellEffect;

/**
 * Sub window used in the spell window to alter and configure a single spell effect.
 * This spell effect window is considered a discrete window for the spell effect. The window cannot
 * add more windows to the above list, but may provide buttons that allow opening of remote
 * windows.
 * @see {@code com.skyisland.questmaker.editor.MapEditor#showEditor(int, com.skyisland.questmaker.editor.MapEditReceiver, String, java.util.Map, com.skyisland.questmaker.swingutils.StringParser, com.skyisland.questmaker.swingutils.StringParser)}
 * @author Skyler
 *
 */
public interface SpellEffectWindow {

	Component getWindow();
	
	SpellEffect getEffect();
	
	boolean isDirty();
	
}
