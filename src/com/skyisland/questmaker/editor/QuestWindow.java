package com.skyisland.questmaker.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.skyisland.questmaker.quest.QuestTemplate;

public class QuestWindow implements EditorWindow {

	private enum Fields {
		NAME("Name", new JTextField()),
		DESCRIPTION("Description", new JTextArea("Short Description Here", 30, 1), new Dimension(0, 10));
		
		
		private String label;
		
		private Component component;
		
		private Fields(String label, Component component) {
			this.label = label;
			this.component = component;
		}
		
		private Fields(String label, Component component, Dimension preferredSize) {
			this(label, component);
			component.setPreferredSize(preferredSize);
		}
	}
	
	private QuestTemplate template;
	
	private JPanel gui;
	
	public QuestWindow(QuestTemplate template) {
		this.template = template;
		gui = new JPanel();
		gui.setLayout(new BoxLayout(gui, BoxLayout.PAGE_AXIS));
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
		gui.setPreferredSize(new Dimension(250, 400));
		JLabel cache;
		for (Fields field : Fields.values()) {
			cache = new JLabel(field.label, JLabel.TRAILING);
			cache.setLabelFor(field.component);
			gui.add(cache);
			gui.add(field.component);
		}
	}
	
}
