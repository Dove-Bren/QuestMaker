package com.skyisland.questmaker.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.skyisland.questmaker.Driver;

/**
 * Prompts the user for a file location to save the current project to.
 * @author Skyler
 *
 */
public class SaveProjectAsAction extends AbstractAction {

	private static SaveProjectAsAction instance = null;
	
	public static SaveProjectAsAction instance() {
		if (instance == null)
			instance = new SaveProjectAsAction();
		
		return instance;
	}
	
	private SaveProjectAsAction() {
		;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser dia = new JFileChooser();
		dia.setDialogTitle("Save Project As");
		dia.addChoosableFileFilter(new FileNameExtensionFilter("QuestManager Settings File", "yml", "yaml"));
		
		int ret = dia.showSaveDialog(Driver.driver.getMainWindow());
		
		if (ret != JFileChooser.APPROVE_OPTION)
			return;
		
		File saveFile = dia.getSelectedFile();
		
		Driver.driver.saveProject(saveFile);
	}

}
