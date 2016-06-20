package com.skyisland.questmaker.editor;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.skyisland.questmaker.quest.QuestTemplate;

public class QuestWindow implements EditorWindow {

	private enum Fields {
		NAME("Name", new JTextField()),
		DESCRIPTION("Description", new JTextArea("Short Description Here", 30, 1));
		
		
		private String label;
		
		private Component component;
		
		private Fields(String label, Component component) {
			this.label = label;
			this.component = component;
		}
	}
	
	private QuestTemplate template;
	
	private JPanel gui;
	
	public QuestWindow(QuestTemplate template) {
		this.template = template;
		gui = new JPanel();
		setupGui();
	}

	@Override
	public String getWindowTitle() {
		return "Quest: " + template.getTitle();
	}

	@Override
	public boolean close() {
		return true;
	}

	@Override
	public Component getContainingComponent() {
		return gui;
	}
	
	private void setupGui() {
		gui.setBackground(Color.DARK_GRAY);
		JLabel cache;
		for (Fields field : Fields.values()) {
			gui.add(field.component);
			cache = new JLabel(field.label, JLabel.TRAILING);
			cache.setLabelFor(field.component);
			gui.add(cache);
		}
	}
	
}
