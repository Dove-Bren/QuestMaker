package com.skyisland.questmaker.spell;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import com.skyisland.questmaker.swingutils.Theme;
import com.skyisland.questmaker.swingutils.Theme.Themed;

public abstract class EmptyWindow implements SpellEffectWindow, Themed {

	private JPanel gui;
	
	public EmptyWindow(String guiLabel) {
		this.gui = new JPanel();
		setupGui(guiLabel);
	}
	
	/**
	 * Called to set up gui with the single field. 
	 */
	protected void setupGui(String label) {
		gui.setBackground(Theme.BACKGROUND_EDITWINDOW.get());
		SpringLayout lay = new SpringLayout();
		gui.setLayout(lay);
		
		
		JLabel text = new JLabel(label);
		text.setForeground(Theme.TEXT_FORWARD.get());
		gui.add(text, BorderLayout.NORTH);
		lay.putConstraint(SpringLayout.NORTH, text, 10, SpringLayout.NORTH, gui);
		lay.putConstraint(SpringLayout.HORIZONTAL_CENTER, text, 0, SpringLayout.HORIZONTAL_CENTER, gui);
		gui.validate();
		
	}
	
	@Override
	public Component getWindow() {
		return gui;
	}
	
	@Override
	public void themeChange(Theme theme) {
		
	}
	
	@Override
	public boolean isDirty() {
		return false;
	}

}
