package com.skyisland.questmaker.project;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import com.skyisland.questmaker.Driver;
import com.skyisland.questmaker.configutils.PluginConfigurationWriter;
import com.skyisland.questmaker.editor.ProjectWindow;
import com.skyisland.questmaker.explorer.ProjectExplorer;
import com.skyisland.questmaker.quest.QuestTemplate;
import com.skyisland.questmaker.spell.SpellTemplate;
import com.skyisland.questmanager.QuestManagerPlugin;
import com.skyisland.questmanager.configuration.AlterablePluginConfiguration;
import com.skyisland.questmanager.configuration.PluginConfiguration;
import com.skyisland.questmanager.configuration.QuestConfigurationField;

/**
 * On open project being edited. Encapsulates all of the information about the open project.
 * <p>This class operates under the assumption that only one exists at a time.</p>
 * @author Skyler
 *
 */
public class Project {
	
	private static class ResourceRecord<E extends ProjectResource> {
		
		private E resource;
		
		private boolean dirty;
		
		public ResourceRecord(E resource) {
			this(resource, true);
		}
		
		public ResourceRecord(E resource, boolean dirty) {
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
	
	private List<ResourceRecord<QuestTemplate>> quests;
	
	private List<ResourceRecord<SpellTemplate>> spells;
	
	private List<ResourceRecord> regions;
	
	//TODO this would be neat, but a little more dynamic than step 1
	//private List<ResourceRecord> skillsConfigs;
	
	public Project() {
		dirty = false;
		quests = new LinkedList<>();
		spells = new LinkedList<>();
		regions = new LinkedList<>();
		config = AlterablePluginConfiguration.copyOf(PluginConfiguration.generateDefault());
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
			for (ResourceRecord<QuestTemplate> record : quests) {
				if (record.dirty) {
					record.resource.save(new File(saveFile.getParentFile(), config.getQuestPath()));
					record.dirty = false;
				}
			}

			for (ResourceRecord<SpellTemplate> record : spells) {
				if (record.dirty) {
					record.resource.save(new File(saveFile.getParentFile(), config.getSpellPath()));
					record.dirty = false;
				}
			}

			for (ResourceRecord record : regions) {
				if (record.dirty) {
					record.resource.save(new File(saveFile.getParentFile(), config.getRegionPath()));
					record.dirty = false;
				}
			}
		
			PluginConfigurationWriter.saveConfig(config, saveFile);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Driver.driver.getMainWindow(),
					"Encountered an IO Exception while saving! Check the device is available, and try again",
					"Failed To Save", JOptionPane.PLAIN_MESSAGE);
			return false;
		}
		
		
		dirty(false);
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
				return save();
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
		
		project.loadQuests();
		
		
		return project;
	}
	
	private void loadQuests() {
		if (saveFile == null) {
			System.err.println("Cannot load quests with no base config location");
			return;
		}
		if (config == null) {
			System.err.println("Cannot load quests without a base config");
			return;
		}
		
		File dir = new File(saveFile.getParentFile(), config.getQuestPath());
		System.out.print("Loading Quests from " + dir.getAbsolutePath() + "... ");
		if (!dir.exists())
			return;
		
		if (!dir.isDirectory()) {
			System.out.println("");
			JOptionPane.showMessageDialog(Driver.driver.getMainWindow(),
					"Quest directory points to an invalid file; expected a directory, but got a file!",
					"Invalid File",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (dir.listFiles().length == 0) {
			System.out.println("no files!");
			return;
		}
		
		int count = 0;
		
		//lookup and load templates for each quest name given
		for (File templateFile : dir.listFiles()) {
			count += loadTemplateFile(templateFile);			
		}
		
		
		for (ResourceRecord<QuestTemplate> rec : quests) {
			Driver.driver.getExplorer().addItem(rec.resource, ProjectExplorer.Section.QUEST);
		}
		
		System.out.println("Loaded " + count + " quests");
	}
	
	private int loadTemplateFile(File templateFile) {
		if (templateFile == null || !templateFile.exists()) {
			return 0;
		}
		
		if (templateFile.isDirectory()) {
			int count = 0;
			
			for (File f : templateFile.listFiles()) {
				count += loadTemplateFile(f);
			}
			
			return count;
		}
		
		if (!templateFile.getName().endsWith(".yml") && 
				!templateFile.getName().endsWith(".yaml")) {
			return 0;
		}
		
		//found the file, let's load it up!
		YamlConfiguration questConfig = new YamlConfiguration();
		try {
			questConfig.load(templateFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
			QuestManagerPlugin.logger.warning(
					"Unable to load quest from file: " + templateFile.getAbsolutePath());
			return 0;
		}
		
		QuestTemplate questTemplate = new QuestTemplate(questConfig);
		quests.add(new ResourceRecord<QuestTemplate>(questTemplate, false));
		
		return 1;
	}
	
	public void addQuest(QuestTemplate quest) {
		dirty();
		ResourceRecord<QuestTemplate> record = new ResourceRecord<>(quest);
		quests.add(record);
		sort();
		int index = quests.indexOf(record);
		Driver.driver.getExplorer().addItem(quest, ProjectExplorer.Section.QUEST, index);
	}
	
	public void addSpell(SpellTemplate spell) {
		dirty();
		ResourceRecord<SpellTemplate> record = new ResourceRecord<>(spell);
		spells.add(record);
		sort();
		int index = spells.indexOf(record);
		Driver.driver.getExplorer().addItem(spell, ProjectExplorer.Section.SPELL, index);
	}
	
	public void dirty() {
		dirty(true);
	}
	
	private void dirty(boolean isDirty) {
		if (isDirty == dirty)
			return;
		
		if (isDirty)
			Driver.driver.getMainWindow().setTitle(Driver.MAIN_TITLE + "*"); //newly dirty 
		else
			Driver.driver.getMainWindow().setTitle(Driver.MAIN_TITLE);
		
		dirty = isDirty;
	}
	
	private void sort() {
		Collections.sort(quests, (ResourceRecord<QuestTemplate> q1, ResourceRecord<QuestTemplate> q2) -> {
			return (q1.resource.getTitle().compareTo(q2.resource.getTitle()));
		});
	}
	
	public boolean hasQuest(String name) {
		for (ResourceRecord<QuestTemplate> quest : quests) {
			if (quest.resource.getValue(QuestConfigurationField.NAME).equals(name))
				return true;
		}
		
		return false;
	}
	
	public boolean hasSpell(String name) {
		for (ResourceRecord<SpellTemplate> spell : spells) {
			if (spell.resource.getName().equals(name))
				return true;
		}
		
		return false;
	}
	
	public void openSettings() {
		Driver.driver.getEditor().openWindow(new ProjectWindow(this, config));
	}
}
