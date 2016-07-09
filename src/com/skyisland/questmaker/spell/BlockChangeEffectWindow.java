package com.skyisland.questmaker.spell;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import org.bukkit.Material;

import com.skyisland.questmaker.swingutils.Theme;
import com.skyisland.questmaker.swingutils.Theme.Themed;
import com.skyisland.questmanager.configuration.utils.YamlWriter;
import com.skyisland.questmanager.magic.spell.effect.BlockEffect;
import com.skyisland.questmanager.magic.spell.effect.SpellEffect;

public class BlockChangeEffectWindow implements SpellEffectWindow, Themed {
	
	private static List<String> placeableList;
	
	public static List<String> getPlaceableMaterials() {
		if (placeableList == null) {
			placeableList = new ArrayList<>(Material.values().length);
			for (Material type : Material.values()) {
				if (type.isBlock())
				placeableList.add(YamlWriter.toStandardFormat(type.name()));
			}
			
			Collections.sort(placeableList);
		}
		return placeableList;
	}
	
	private JPanel gui;
	
	private JComboBox<String> fromBox;
	
	private JComboBox<String> toBox;
	
	/**
	 * Create new swap effect window with the given defaulted from and to's.
	 * If they're left null, no default is selected (the first item is used instead)
	 * @param from
	 * @param to
	 */
	public BlockChangeEffectWindow(Material from, Material to) {
		gui = new JPanel();
		gui.setBackground(Theme.BACKGROUND_EDITWINDOW.get());
		SpringLayout lay = new SpringLayout();
		gui.setLayout(lay);
		JLabel label = new JLabel("Block Change Effect");
		label.setForeground(Theme.TEXT_FORWARD.get());
		gui.add(label);
		lay.putConstraint(SpringLayout.NORTH, label, 10, SpringLayout.NORTH, gui);
		lay.putConstraint(SpringLayout.HORIZONTAL_CENTER, label, 0, SpringLayout.HORIZONTAL_CENTER, gui);
				
	    label = new JLabel("From");
		label.setForeground(Theme.TEXT_EDITWINDOW.register(this));
		gui.add(label);
		lay.putConstraint(SpringLayout.NORTH, label, 40, SpringLayout.NORTH, gui);
		lay.putConstraint(SpringLayout.WEST, label, 10, SpringLayout.WEST, gui);
		fromBox = new JComboBox<String>(getPlaceableMaterials().toArray(new String[0]));
		if (from != null) {
			//preset material
			for (int i = 0; i < fromBox.getItemCount(); i++) {
				if (fromBox.getItemAt(i).toString().equalsIgnoreCase(YamlWriter.toStandardFormat(from.name()))) {
					fromBox.setSelectedIndex(i);
					break;
				}
			}
		}
		
		fromBox.setEditable(false);
		gui.add(fromBox);
		lay.putConstraint(SpringLayout.WEST, fromBox, 0, SpringLayout.WEST, label);
		lay.putConstraint(SpringLayout.NORTH, fromBox, 3, SpringLayout.SOUTH, label);
		
		toBox = new JComboBox<String>(getPlaceableMaterials().toArray(new String[0]));
		if (to != null) {
			//preset material
			for (int i = 0; i < toBox.getItemCount(); i++) {
				if (toBox.getItemAt(i).toString().equalsIgnoreCase(YamlWriter.toStandardFormat(to.name()))) {
					toBox.setSelectedIndex(i);
					break;
				}
			}
		}
		
		toBox.setEditable(false);
		gui.add(toBox);
		lay.putConstraint(SpringLayout.WEST, toBox, 75, SpringLayout.EAST, fromBox);
		lay.putConstraint(SpringLayout.NORTH, toBox, 0, SpringLayout.NORTH, fromBox);

		label = new JLabel("To");
		label.setForeground(Theme.TEXT_EDITWINDOW.register(this));
		gui.add(label);
		lay.putConstraint(SpringLayout.SOUTH, label, -3, SpringLayout.NORTH, toBox);
		lay.putConstraint(SpringLayout.WEST, label, 0, SpringLayout.WEST, toBox);
		
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
		return new BlockEffect(
				Material.matchMaterial(fromBox.getSelectedItem().toString()),
				Material.matchMaterial(toBox.getSelectedItem().toString())
				);
	}
}
