package com.skyisland.questmaker.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.skyisland.questmaker.Driver;

/**
 * Opens a new project. Any previously open project will be closed.
 * @author Skyler
 *
 */
public class OpenProjectAction extends AbstractAction {

	private static OpenProjectAction instance = null;
	
	public static OpenProjectAction instance() {
		if (instance == null)
			instance = new OpenProjectAction();
		
		return instance;
	}
	
	private OpenProjectAction() {
		;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser dia = new JFileChooser();
		dia.addChoosableFileFilter(new FileNameExtensionFilter("QuestManager Settings File", "yml", "yaml"));
		
		int ret = dia.showSaveDialog(Driver.driver.getMainWindow());
		
		if (ret != JFileChooser.APPROVE_OPTION)
			return;
		
		File openFile = dia.getSelectedFile();
		System.out.println("got file " + openFile.getName());
		
		Driver.driver.openProject(openFile);
	}

}
