package com.skyisland.questmaker.spell;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import com.skyisland.questmaker.swingutils.Theme;
import com.skyisland.questmaker.swingutils.Theme.Themed;

public abstract class SingleValueWindow<T> implements SpellEffectWindow, Themed {

	private JPanel gui;
	
	private JFormattedTextField damageField;
	
	public SingleValueWindow(String guiText, String labelText, T defaultValue) {
		this.gui = new JPanel();
		gui.setPreferredSize(new Dimension(400, 200));
		setupGui(guiText, labelText, defaultValue);
	}
	
	/**
	 * Called to set up gui with the single field. 
	 */
	protected void setupGui(String guiLabel, String labelText, T defaultValue) {
		gui.setBackground(Theme.BACKGROUND_EDITWINDOW.get());
		SpringLayout lay = new SpringLayout();
		gui.setLayout(lay);
		JLabel label = new JLabel(guiLabel);
		label.setForeground(Theme.TEXT_FORWARD.get());
		gui.add(label);
		lay.putConstraint(SpringLayout.NORTH, label, 10, SpringLayout.NORTH, gui);
		lay.putConstraint(SpringLayout.HORIZONTAL_CENTER, label, 0, SpringLayout.HORIZONTAL_CENTER, gui);
				
	    label = new JLabel(labelText);
		label.setForeground(Theme.TEXT_EDITWINDOW.register(this));
		gui.add(label);
		lay.putConstraint(SpringLayout.NORTH, label, 40, SpringLayout.NORTH, gui);
		lay.putConstraint(SpringLayout.WEST, label, 10, SpringLayout.WEST, gui);
		damageField = new JFormattedTextField(defaultValue);
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
	
	@SuppressWarnings("unchecked")
	protected T getValue() {
		return ((T) damageField.getValue());
	}

	@Override
	public void themeChange(Theme theme) {
		
	}

}
