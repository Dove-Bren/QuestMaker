package com.skyisland.questmaker.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.skyisland.questmaker.Driver;
import com.skyisland.questmaker.editor.EditorWindow;

public class CloseWindowAction extends AbstractAction {

	private EditorWindow window;
	
	public CloseWindowAction(EditorWindow window) {
		this.window = window;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Driver.driver.getEditor().closeWindow(window);
	}
	
}
