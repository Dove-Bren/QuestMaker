package com.skyisland.questmaker.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.bukkit.Material;

import com.skyisland.questmaker.project.Project;
import com.skyisland.questmanager.configuration.AlterablePluginConfiguration;
import com.skyisland.questmanager.configuration.PluginConfiguration;
import com.skyisland.questmanager.configuration.PluginConfiguration.PluginConfigurationKey;
import com.skyisland.questmanager.configuration.utils.YamlWriter;

public class ProjectWindow implements EditorWindow {
	
	private static Set<PluginConfiguration.PluginConfigurationKey> ignoreKeys = new HashSet<>();
	
	{
		ignoreKeys.add(PluginConfigurationKey.MUSICDURATIONS);
		ignoreKeys.add(PluginConfigurationKey.VERSION);
		ignoreKeys.add(PluginConfigurationKey.WORLDS);
	}
	
	private static List<String> materialList;
	
	public static List<String> getMaterialList() {
		if (materialList == null) {
			materialList = new ArrayList<>(Material.values().length);
			for (Material type : Material.values()) {
				materialList.add(YamlWriter.toStandardFormat(type.name()));
			}
			
			Collections.sort(materialList);
		}
		return materialList;
	}

	private Project project;
	
	private AlterablePluginConfiguration config;
	
	private Map<PluginConfigurationKey, ButtonGroup> booleanSwitches;
	
	private JPanel gui;
	
	public ProjectWindow(Project project, AlterablePluginConfiguration config) {
		this.project = project;
		this.config = config;
		booleanSwitches = new HashMap<>();
		gui = new JPanel();
		gui.setLayout(new BoxLayout(gui, BoxLayout.PAGE_AXIS));//new SpringLayout());
		setupGui();
	}

	@Override
	public String getWindowTitle() {
		return "Project Settings";
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
		gui.setForeground(Color.WHITE);
		gui.setPreferredSize(new Dimension(250, 500));
		JLabel cache;
		Component comp;
		for (PluginConfiguration.PluginConfigurationKey field : PluginConfiguration.PluginConfigurationKey.values()) {
			if (ignoreKeys.contains(field))
				continue;
			cache = new JLabel(field.getName(), JLabel.TRAILING);
			comp = createField(field);
			cache.setLabelFor(comp);
			gui.add(cache);
			gui.add(comp);
		}
		
		//SwingUtilities.gr;
	}
	
	private Component createField(PluginConfiguration.PluginConfigurationKey key) {
		if (key == PluginConfigurationKey.ALTERTYPE || key == PluginConfigurationKey.COMPASSTYPE
				|| key == PluginConfigurationKey.INVOKERTYPE) {
			JComboBox<String> comp = new JComboBox<>(new DefaultComboBoxModel<String>());
			List<String> list = getMaterialList();
			
			for (String e : list)
				comp.addItem(e);
			
			comp.setSelectedItem(config.getBaseValue(key));
			
			return comp;
		}
		
		if (key.getDef() instanceof Boolean) {
			ButtonGroup group = new ButtonGroup();
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
			
			boolean on = (Boolean) config.getBaseValue(key);
			
			JRadioButton button;
			button = new JRadioButton("true", on);
			group.add(button);
			buttonPanel.add(button);
			button = new JRadioButton("false", !on);
			group.add(button);
			buttonPanel.add(button);
			
			booleanSwitches.put(key, group);
			return buttonPanel;
		}
		
		//anything else just do a text field
		JTextField field = new JTextField(
				config.getBaseValue(key).toString(), 20
				);
		
		
		return field;
	}
}
