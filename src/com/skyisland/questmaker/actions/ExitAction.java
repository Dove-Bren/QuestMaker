package com.skyisland.questmaker.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * Exits the program. Prompts saving if needed.
 * @author Skyler
 *
 */
public class ExitAction extends AbstractAction {

	private static ExitAction instance = null;
	
	public static ExitAction instance() {
		if (instance == null)
			instance = new ExitAction();
		
		return instance;
	}
	
	private ExitAction() {
		;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		System.exit(0);
	}

}
