package com.skyisland.questmaker;

import java.io.File;

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
	
	public Project() {
		dirty = false;
	}
	
	public void save(File saveFile) {
		dirty = false;
	}
	
	public void close() {
		if (dirty) {
			//TODO
		}
		
		
	}
	
	public static Project load(File loadFile) {
		if (loadFile == null || !loadFile.exists())
			return null;
		
		Project project = new Project();
		project.config = new PluginConfiguration(loadFile);
		
		
		return project;
	}
	
	
	
}
