package com.skyisland.questmaker;

import java.io.File;

import javax.swing.JOptionPane;

import com.skyisland.questmanager.configuration.PluginConfiguration;

/**
 * On open project being edited. Encapsulates all of the information about the open project.
 * <p>This class operates under the assumption that only one exists at a time.</p>
 * @author Skyler
 *
 */
public class Project {
	
	/**
	 * Whether the project has been edited since the last time it was saved
	 */
	private boolean dirty;
	
	private PluginConfiguration config;
	
	private File saveFile;
	
	public Project() {
		dirty = true;
	}
	
	public void save() {
		save(saveFile);
	}
	
	public void save(File saveFile) {
		dirty = false;
	}
	
	/**
	 * Tries to close the project. If the project is unsaved, the user is prompted to save or discard changes.
	 * <p>
	 * This method returns whether the project was successfully closed. False indicates the resource should be
	 * considered open and still operating, and the following action cancelled.
	 * </p>
	 * @return If the project has been closed, or has refused to close
	 */
	public boolean close() {
		if (dirty) {
			int rep = JOptionPane.showConfirmDialog(Driver.driver.getMainWindow(),
					"The project has unsaved changes. Save changes before closing?",
					"Save Project",
					JOptionPane.YES_NO_CANCEL_OPTION);
			
			if (rep == JOptionPane.CANCEL_OPTION)
				return false;
			
			if (rep == JOptionPane.YES_OPTION) {
				save();
			}
		}
		return true;
	}
	
	public static Project load(File loadFile) {
		if (loadFile == null || !loadFile.exists())
			return null;
		
		Project project = new Project();
		project.config = new PluginConfiguration(loadFile);
		project.saveFile = loadFile;
		
		
		return project;
	}
	
	
	
}
