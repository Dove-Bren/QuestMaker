package com.skyisland.questmaker.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.skyisland.questmaker.Driver;

/**
 * Prompts the user for a file location to save the current project to.
 * @author Skyler
 *
 */
public class SaveProjectAction extends AbstractAction {

	private static SaveProjectAction instance = null;
	
	public static SaveProjectAction instance() {
		if (instance == null)
			instance = new SaveProjectAction();
		
		return instance;
	}
	
	private SaveProjectAction() {
		;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Driver.driver.saveProject();
	}

}
