package com.skyisland.questmaker.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.skyisland.questmaker.Driver;
import com.skyisland.questmaker.project.Project;

/**
 * Opens a new project. Any previously open project will be closed.
 * @author Skyler
 *
 */
public class CreateProjectAction extends AbstractAction {

	private static CreateProjectAction instance = null;
	
	public static CreateProjectAction instance() {
		if (instance == null)
			instance = new CreateProjectAction();
		
		return instance;
	}
	
	private CreateProjectAction() {
		;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Project p = new Project();
		Driver.driver.openProject(p);
		p.dirty();
	}

}
