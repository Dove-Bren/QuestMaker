package com.skyisland.questmaker.spell;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.skyisland.questmaker.editor.ProjectWindow;
import com.skyisland.questmaker.swingutils.Theme;
import com.skyisland.questmaker.swingutils.Theme.Themed;
import com.skyisland.questmanager.configuration.utils.YamlWriter;
import com.skyisland.questmanager.magic.spell.effect.CastPylonEffect;
import com.skyisland.questmanager.magic.spell.effect.SpellEffect;

public class CastPylonEffectWindow implements SpellEffectWindow, Themed {
	
	private JPanel gui;
	
	private boolean dirty;
	
	private JTextField nameBox;
	
	private JComboBox<String> iconBox;
	
	/**
	 * Create new swap effect window with the given defaulted from and to's.
	 * If they're left null, no default is selected (the first item is used instead)
	 * @param from
	 * @param to
	 */
	public CastPylonEffectWindow(String name, Material icon) {
		dirty = true;
		gui = new JPanel();
		gui.setBackground(Theme.BACKGROUND_EDITWINDOW.get());
		SpringLayout lay = new SpringLayout();
		gui.setLayout(lay);
		JLabel label = new JLabel("Cast Pylon Effect");
		label.setForeground(Theme.TEXT_FORWARD.get());
		gui.add(label);
		lay.putConstraint(SpringLayout.NORTH, label, 10, SpringLayout.NORTH, gui);
		lay.putConstraint(SpringLayout.HORIZONTAL_CENTER, label, 0, SpringLayout.HORIZONTAL_CENTER, gui);
				
	    label = new JLabel("Pylon Name");
		label.setForeground(Theme.TEXT_EDITWINDOW.register(this));
		gui.add(label);
		lay.putConstraint(SpringLayout.NORTH, label, 40, SpringLayout.NORTH, gui);
		lay.putConstraint(SpringLayout.WEST, label, 10, SpringLayout.WEST, gui);
		nameBox = new JTextField();
		nameBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dirty();
			}
			
		});
		if (name != null)
			nameBox.setText(name);
		
		nameBox.setColumns(10);
		gui.add(nameBox);
		lay.putConstraint(SpringLayout.WEST, nameBox, 0, SpringLayout.WEST, label);
		lay.putConstraint(SpringLayout.NORTH, nameBox, 3, SpringLayout.SOUTH, label);
		
		iconBox = new JComboBox<String>(ProjectWindow.getMaterialList().toArray(new String[0]));
		iconBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dirty();
			}
			
		});
		if (icon != null) {
			//preset material
			for (int i = 0; i < iconBox.getItemCount(); i++) {
				if (iconBox.getItemAt(i).toString().equalsIgnoreCase(YamlWriter.toStandardFormat(icon.name()))) {
					iconBox.setSelectedIndex(i);
					break;
				}
			}
		}
		
		iconBox.setEditable(false);
		gui.add(iconBox);
		lay.putConstraint(SpringLayout.WEST, iconBox, 75, SpringLayout.EAST, nameBox);
		lay.putConstraint(SpringLayout.NORTH, iconBox, 0, SpringLayout.NORTH, nameBox);

		label = new JLabel("To");
		label.setForeground(Theme.TEXT_EDITWINDOW.register(this));
		gui.add(label);
		lay.putConstraint(SpringLayout.SOUTH, label, -3, SpringLayout.NORTH, iconBox);
		lay.putConstraint(SpringLayout.WEST, label, 0, SpringLayout.WEST, iconBox);
		
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
		return new CastPylonEffect(
				nameBox.getText(),
				new ItemStack(Material.matchMaterial(iconBox.getSelectedItem().toString()))
				);
	}
	
	protected void dirty() {
		dirty = true;
	}
	
	@Override
	public boolean isDirty() {
		return dirty;
	}
}
