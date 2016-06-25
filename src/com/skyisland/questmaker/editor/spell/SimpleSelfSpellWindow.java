package com.skyisland.questmaker.editor.spell;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import org.bukkit.Effect;
import org.bukkit.Sound;

import com.skyisland.questmaker.spell.SpellTemplate;
import com.skyisland.questmanager.magic.spell.SimpleSelfSpell;
import com.skyisland.questmanager.magic.spell.Spell;

public class SimpleSelfSpellWindow extends SpellWindow {

	private JComboBox<Sound> sombo;
	
	private JComboBox<Effect> combo;
	
	protected SimpleSelfSpellWindow(SpellTemplate template, SimpleSelfSpell spell) {
		super(template, spell);
		
	}

	@Override
	protected void extendGui(JPanel gui, Component last) {
		SpringLayout lay = (SpringLayout) gui.getLayout();
		JLabel label = label("Cast Effect");
		List<Effect> effects = new ArrayList<>();
		for (Effect ef : Effect.values())
			effects.add(ef);
		effects.sort(new Comparator<Effect>() {
			@Override
			public int compare(Effect e1, Effect e2) {
				return e1.name().compareTo(e2.name());
			}
		});
		combo = new JComboBox<Effect>(effects.toArray(new Effect[effects.size()]));
		gui.add(label);
		lay.putConstraint(SpringLayout.NORTH, label, 10, SpringLayout.SOUTH, last);
		lay.putConstraint(SpringLayout.WEST, label, 20, SpringLayout.WEST, gui);
		gui.add(combo);
		lay.putConstraint(SpringLayout.NORTH, combo, 3, SpringLayout.SOUTH, label);
		lay.putConstraint(SpringLayout.WEST, combo, 0, SpringLayout.WEST, label);
		sombo = new JComboBox<Sound>(Sound.values());
		label = label("Cast Sound");
		gui.add(sombo);
		lay.putConstraint(SpringLayout.NORTH, sombo, 0, SpringLayout.NORTH, combo);
		lay.putConstraint(SpringLayout.WEST, sombo, 10, SpringLayout.EAST, combo);
		gui.add(label);
		lay.putConstraint(SpringLayout.SOUTH, label, -3, SpringLayout.NORTH, sombo);
		lay.putConstraint(SpringLayout.WEST, label, 0, SpringLayout.WEST, sombo);
		//lay.putConstraint(SpringLayout.SOUTH, sombo, 20, SpringLayout.SOUTH, gui);
	}

	@Override
	protected void doSpellPass(Spell spell) {
		SimpleSelfSpell sspell = (SimpleSelfSpell) spell;
		sspell.setCastEffect((Effect) combo.getSelectedItem());
		sspell.setCastSound((Sound) sombo.getSelectedItem());
	}

}
