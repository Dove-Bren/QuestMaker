package com.skyisland.questmaker.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.skyisland.questmaker.Driver;
import com.skyisland.questmaker.editor.QuestWindow;
import com.skyisland.questmaker.project.Project;
import com.skyisland.questmaker.quest.QuestTemplate;

public class CreateQuestAction extends AbstractAction {
	private static CreateQuestAction instance = null;
	
	public static CreateQuestAction instance() {
		if (instance == null)
			instance = new CreateQuestAction();
		
		return instance;
	}
	
	private CreateQuestAction() {
		;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Project proj = Driver.driver.getOpenProject();
		if (proj == null) {
			JOptionPane.showMessageDialog(Driver.driver.getMainWindow(), "You must create or open a project before creating a quest");
			return;
		}
			
		
		
		String name = JOptionPane.showInputDialog(Driver.driver.getMainWindow(), "Create a new Quest", "Quest", JOptionPane.PLAIN_MESSAGE);
		
		if (name == null)
			return;
		
		QuestTemplate template = new QuestTemplate(name);
		proj.addQuest(template);
		Driver.driver.getEditor().openWindow(new QuestWindow(template));
	}

}
