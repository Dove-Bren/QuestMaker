package com.skyisland.questmaker.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.skyisland.questmaker.Driver;
import com.skyisland.questmaker.project.Project;

public class ProjectSettingsAction extends AbstractAction {

	private static ProjectSettingsAction instance = null;
	
	public static ProjectSettingsAction instance() {
		if (instance == null)
			instance = new ProjectSettingsAction();
		
		return instance;
	}
	
	private ProjectSettingsAction() {
		;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		Project proj = Driver.driver.getOpenProject();
		if (proj == null) {
			JOptionPane.showMessageDialog(Driver.driver.getMainWindow(), "No project is open!");
			return;
		}
		
		proj.openSettings();
	}
}
