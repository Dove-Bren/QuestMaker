package com.skyisland.questmaker.spell;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.skyisland.questmaker.swingutils.Theme;
import com.skyisland.questmaker.swingutils.Theme.Themed;
import com.skyisland.questmanager.magic.spell.effect.SpellEffect;
import com.skyisland.questmanager.magic.spell.effect.StatusEffect;

public class StatusEffectWindow implements SpellEffectWindow, Themed {
	
	private static PotionEffectType[] effectList;
	
	public static PotionEffectType[] getEffectList() {
		if (effectList == null) {
			List<PotionEffectType> types = new ArrayList<>(Material.values().length);
			for (PotionEffectType type : PotionEffectType.values()) {
				types.add(type);
			}
			
			Collections.sort(types, (PotionEffectType type1, PotionEffectType type2) -> {
				return type1.getName().compareTo(type2.getName());
			});
			effectList = types.toArray(new PotionEffectType[PotionEffectType.values().length]);
		}
		return effectList;
	}
	
	private JPanel gui;
	
	private JComboBox<PotionEffectType> effectBox;
	
	private JFormattedTextField durationField;
	
	private JFormattedTextField amplifierField;
	
	/**
	 * Create new swap effect window with the given defaulted from and to's.
	 * If they're left null, no default is selected (the first item is used instead)
	 * @param from
	 * @param to
	 */
	public StatusEffectWindow(PotionEffectType type, int defaultDuration, int defaultAmplification) {
		gui = new JPanel();
		gui.setBackground(Theme.BACKGROUND_EDITWINDOW.get());
		SpringLayout lay = new SpringLayout();
		gui.setLayout(lay);
		JLabel label = new JLabel("Status Effect");
		label.setForeground(Theme.TEXT_FORWARD.get());
		gui.add(label);
		lay.putConstraint(SpringLayout.NORTH, label, 10, SpringLayout.NORTH, gui);
		lay.putConstraint(SpringLayout.HORIZONTAL_CENTER, label, 0, SpringLayout.HORIZONTAL_CENTER, gui);
				
	    label = new JLabel("Effect");
		label.setForeground(Theme.TEXT_EDITWINDOW.register(this));
		gui.add(label);
		lay.putConstraint(SpringLayout.NORTH, label, 40, SpringLayout.NORTH, gui);
		lay.putConstraint(SpringLayout.WEST, label, 10, SpringLayout.WEST, gui);
		effectBox = new JComboBox<PotionEffectType>(PotionEffectType.values());
		if (type != null) {
			//preset material
			effectBox.setSelectedItem(type);
		}
		
		effectBox.setEditable(false);
		gui.add(effectBox);
		lay.putConstraint(SpringLayout.WEST, effectBox, 0, SpringLayout.WEST, label);
		lay.putConstraint(SpringLayout.NORTH, effectBox, 3, SpringLayout.SOUTH, label);
		
		
	    label = new JLabel("Duration");
		label.setForeground(Theme.TEXT_EDITWINDOW.register(this));
		gui.add(label);
		lay.putConstraint(SpringLayout.NORTH, label, 40, SpringLayout.SOUTH, effectBox);
		lay.putConstraint(SpringLayout.WEST, label, 10, SpringLayout.WEST, gui);
		durationField = new JFormattedTextField(durationField);
		
		durationField.setColumns(4);
		durationField.setEditable(false);
		gui.add(durationField);
		lay.putConstraint(SpringLayout.WEST, durationField, 0, SpringLayout.WEST, label);
		lay.putConstraint(SpringLayout.NORTH, durationField, 3, SpringLayout.SOUTH, label);
		
		amplifierField = new JFormattedTextField(defaultAmplification);
		
		amplifierField.setColumns(4);
		amplifierField.setEditable(false);
		gui.add(amplifierField);
		lay.putConstraint(SpringLayout.WEST, amplifierField, 75, SpringLayout.EAST, durationField);
		lay.putConstraint(SpringLayout.NORTH, amplifierField, 0, SpringLayout.NORTH, durationField);

		label = new JLabel("Amplification (base 0)");
		label.setForeground(Theme.TEXT_EDITWINDOW.register(this));
		gui.add(label);
		lay.putConstraint(SpringLayout.SOUTH, label, -3, SpringLayout.NORTH, amplifierField);
		lay.putConstraint(SpringLayout.WEST, label, 0, SpringLayout.WEST, amplifierField);
		
		gui.validate();
	}

	@Override
	public void themeChange(Theme theme) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Component getWindow() {
		return gui;
	}

	@Override
	public SpellEffect getEffect() {
		return new StatusEffect(
				new PotionEffect((PotionEffectType) effectBox.getSelectedItem(), (Integer) durationField.getValue(), (Integer) amplifierField.getValue())
				);
	}
}
