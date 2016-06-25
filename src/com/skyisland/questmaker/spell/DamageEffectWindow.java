package com.skyisland.questmaker.spell;

import java.awt.Component;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import com.skyisland.questmaker.swingutils.Theme;
import com.skyisland.questmaker.swingutils.Theme.Themed;
import com.skyisland.questmanager.magic.spell.effect.DamageEffect;
import com.skyisland.questmanager.magic.spell.effect.SpellEffect;

public class DamageEffectWindow implements SpellEffectWindow, Themed {

	private DamageEffect effect;
	
	private JPanel gui;
	
	private JFormattedTextField damageField;
	
	public DamageEffectWindow() {
		this(new DamageEffect(0.0));
	}
	
	public DamageEffectWindow(DamageEffect effect) {
		this.effect = effect;
		this.gui = new JPanel();
		setupGui();
	}
	
	private void setupGui() {
		gui.setBackground(Theme.BACKGROUND_EDITWINDOW.get());
		SpringLayout lay = new SpringLayout();
		gui.setLayout(lay);
		JLabel label = new JLabel("Damage");
		label.setForeground(Theme.TEXT_EDITWINDOW.register(this));
		gui.add(label);
		lay.putConstraint(SpringLayout.NORTH, label, 10, SpringLayout.NORTH, gui);
		lay.putConstraint(SpringLayout.WEST, label, 10, SpringLayout.WEST, gui);
		damageField = new JFormattedTextField(effect.getDamage());
		damageField.setColumns(5);
		damageField.setEditable(true);
		gui.add(damageField);
		lay.putConstraint(SpringLayout.WEST, damageField, 0, SpringLayout.WEST, label);
		lay.putConstraint(SpringLayout.NORTH, damageField, 3, SpringLayout.SOUTH, label);
		
		gui.validate();
		
	}
	
	@Override
	public Component getWindow() {
		return gui;
	}

	@Override
	public SpellEffect getEffect() {
		effect.setDamage((Double) (damageField.getValue()));
		return effect;
	}
	
	@Override
	public void themeChange(Theme theme) {
		
	}

}
