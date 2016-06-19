package com.skyisland.questmaker.project;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.skyisland.questmaker.Driver;
import com.skyisland.questmaker.configutils.PluginConfigurationWriter;
import com.skyisland.questmanager.configuration.AlterablePluginConfiguration;

/**
 * On open project being edited. Encapsulates all of the information about the open project.
 * <p>This class operates under the assumption that only one exists at a time.</p>
 * @author Skyler
 *
 */
public class Project {
	
	private static class ResourceRecord {
		
		private ProjectResource resource;
		
		private boolean dirty;
		
		public ResourceRecord(ProjectResource resource) {
			this(resource, true);
		}
		
		public ResourceRecord(ProjectResource resource, boolean dirty) {
			this.resource = resource;
			this.dirty = dirty;
		}
		
	}
	
	/**
	 * Whether the project has been edited since the last time it was saved
	 */
	private boolean dirty;
	
	private AlterablePluginConfiguration config;
	
	private File saveFile;
	
	private List<ResourceRecord> quests;
	
	private List<ResourceRecord> spells;
	
	private List<ResourceRecord> regions;
	
	//TODO this would be neat, but a little more dynamic than step 1
	//private List<ResourceRecord> skillsConfigs;
	
	public Project() {
		dirty = true;
		quests = new LinkedList<>();
		spells = new LinkedList<>();
		regions = new LinkedList<>();
	}
	
	/**
	 * Saves the project using the loaded (or last saved-to) file as the file to save to.
	 * @return Whether the save was successful or not (ran into error)
	 */
	public boolean save() {
		if (saveFile == null) {
			//Copied from SaveProjectAsAction, unfortunately cause of the no return :/
			JFileChooser dia = new JFileChooser();
			dia.setDialogTitle("Save Project As");
			dia.addChoosableFileFilter(new FileNameExtensionFilter("QuestManager Settings File", "yml", "yaml"));
			
			int ret = dia.showSaveDialog(Driver.driver.getMainWindow());
			
			if (ret != JFileChooser.APPROVE_OPTION)
				return false;
			
			saveFile = dia.getSelectedFile();
		}
		
		return save(saveFile);
	}
	
	/**
	 * Saves the project to the given file, overwriting anything that's there.
	 * @param saveFile
	 * @return
	 */
	public boolean save(File saveFile) {
		
		try {
			for (ResourceRecord record : quests) {
				if (record.dirty) {
					record.resource.save(new File(saveFile, config.getQuestPath()));
					record.dirty = false;
				}
			}

			for (ResourceRecord record : spells) {
				if (record.dirty) {
					record.resource.save(new File(saveFile, config.getSpellPath()));
					record.dirty = false;
				}
			}

			for (ResourceRecord record : regions) {
				if (record.dirty) {
					record.resource.save(new File(saveFile, config.getRegionPath()));
					record.dirty = false;
				}
			}
		
			PluginConfigurationWriter.saveConfig(config, saveFile);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		
		dirty = false;
		return true;
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
		project.config = new AlterablePluginConfiguration(loadFile);
		
		project.saveFile = loadFile;
		
		
		return project;
	}
	
	
	
	
	
}
