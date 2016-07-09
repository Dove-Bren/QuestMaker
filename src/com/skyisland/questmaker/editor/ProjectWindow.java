package com.skyisland.questmaker.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.Spring;
import javax.swing.SpringLayout;

import org.bukkit.Material;
import org.bukkit.Sound;

import com.skyisland.questmaker.project.Project;
import com.skyisland.questmaker.spell.BlockChangeEffectWindow;
import com.skyisland.questmaker.swingutils.DoubleParser;
import com.skyisland.questmaker.swingutils.SoundParser;
import com.skyisland.questmanager.configuration.AlterablePluginConfiguration;
import com.skyisland.questmanager.configuration.PluginConfiguration;
import com.skyisland.questmanager.configuration.PluginConfiguration.PluginConfigurationKey;
import com.skyisland.questmanager.configuration.utils.YamlWriter;

public class ProjectWindow implements EditorWindow, MapEditReceiver<Sound, Double> {
	
	public static final int MUSIC_DURATION_KEY = 0x443;
	
	private static Set<PluginConfiguration.PluginConfigurationKey> ignoreKeys = new HashSet<>();
	
	{
		//ignoreKeys.add(PluginConfigurationKey.MUSICDURATIONS);
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
	
	private interface Holder {
		public void input();
	}
	
	private static class TextFieldHolder implements FocusListener, Holder {
		
		protected enum Type {
			DOUBLE,
			INT,
			OTHER;
		}
		
		private PluginConfigurationKey key;
		
		private JTextField field;
		
		private ProjectWindow window;
		
		Type type;
		
		private TextFieldHolder(ProjectWindow window, PluginConfigurationKey key, JTextField field, Type type) {
			this.key = key;
			this.window = window;
			this.field = field;
			this.type = type;
		}
		
		@Override
		public void focusGained(FocusEvent e) {
			;
		}

		@Override
		public void focusLost(FocusEvent e) {
			input();
		}
		
		public void input() {
			Object value;
			String text = field.getText().trim();
			try {
			if (type == Type.DOUBLE)
				value = Double.parseDouble(text);
			else if (type == Type.INT)
				value = Integer.parseInt(text);
			else
				value = text;
			} catch (NumberFormatException ex) {
				field.setText(window.config.getBaseValue(key).toString());
				return;
			}
			
			//just store string 
			Object old = window.config.getBaseValue(key);
			window.config.setBaseValue(key, value);
			if (old != null && value != null)
			if ((old == null && value != null) || (old != null && value == null)
					|| !old.equals(value))
				window.project.dirty();
		}
	}
	
	private static class MaterialFieldHolder extends AbstractAction implements Holder {
		
		private PluginConfigurationKey key;
		
		private JComboBox<String> field;
		
		private ProjectWindow window;
		
		private MaterialFieldHolder(ProjectWindow window, PluginConfigurationKey key, JComboBox<String> field) {
			this.key = key;
			this.window = window;
			this.field = field;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			input();
		}
		
		public void input() {
			String fieldName = conv((String) field.getSelectedItem());
			//just store string 

			Object old = window.config.getBaseValue(key);
			window.config.setBaseValue(key, fieldName);

			if (old != null && fieldName != null)
			if ((old == null && fieldName != null) || (old != null && fieldName == null)
					|| !old.equals(fieldName))
				window.project.dirty();
		}
		
		private String conv(String display) {
			return display.toUpperCase().replace(' ', '_');
		}
	}
	
	private static class BooleanFieldHolder extends AbstractAction implements Holder {
		
		private PluginConfigurationKey key;
		
		private JRadioButton field;
		
		private ProjectWindow window;
		
		private BooleanFieldHolder(ProjectWindow window, PluginConfigurationKey key, JRadioButton field) {
			this.key = key;
			this.window = window;
			this.field = field;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			input();
		}
		
		public void input() {
			//just store string 
			window.config.setBaseValue(key, !field.getText().toLowerCase().contains("false"));
			window.project.dirty();
		}
	}

	private Project project;
	
	private AlterablePluginConfiguration config;
	
	private JPanel gui;
	
	private List<Holder> holders;
	
	public ProjectWindow(Project project, AlterablePluginConfiguration config) {
		this.project = project;
		this.config = config;
		this.holders = new LinkedList<>();
		gui = new JPanel();
		//wrappingGui.scroll
		//gui.setLayout(new BoxLayout(gui, BoxLayout.PAGE_AXIS));//new SpringLayout());
		setupGui();
	}

	@Override
	public String getWindowTitle() {
		return "Project Settings";
	}

	@Override
	public boolean close() {
		for (Holder h : holders)
			h.input();
		return true;
	}

	@Override
	public Component getContainingComponent() {
		return gui;
	}
	
	private void setupGui() {
		gui.setBackground(Color.DARK_GRAY);
		
		SpringLayout lay = new SpringLayout();
		gui.setLayout(lay);
		
		JLabel cache;
//		cache = new JLabel("Quest Editor");
//		cache.setFon
//		gui.add(cache);
//		lay.putConstraint(SpringLayout.WEST, cache, Spring.constant(20, 40, 60), SpringLayout.EAST, gui);
		
		//gui.setPreferredSize(new Dimension(250, 500));
		Component comp, last = null;
		boolean empty;
		List<Component> fields = new ArrayList<>(PluginConfiguration.PluginConfigurationKey.values().length);
		Component longestField = null;
		int longest = 0;
		for (PluginConfiguration.Category category : PluginConfiguration.Category.values()) {
			empty = true;
		
			for (PluginConfiguration.PluginConfigurationKey field : PluginConfiguration.PluginConfigurationKey.values()) {
				if (category != field.getCaterogy())
					continue;
				if (ignoreKeys.contains(field))
					continue;
				
				if (empty) {
					//first one in this category. create a label
					comp = new JLabel(category.getName());
					comp.setFont(new Font("Helvetica", Font.PLAIN, 14));
					comp.setForeground(Color.RED);
					gui.add(comp);
					lay.putConstraint(SpringLayout.WEST, comp, 10, SpringLayout.WEST, gui);
					lay.putConstraint(SpringLayout.NORTH, comp, 20, SpringLayout.NORTH, gui);
					
					if (last == null) {
						;
					} else {
						lay.putConstraint(SpringLayout.NORTH, comp, Spring.constant(10), SpringLayout.SOUTH, last);
						//lay.putConstraint(SpringLayout.WEST, comp, 0, SpringLayout.WEST, last);
					}
					
					last = comp;
					empty = false;
				}
				cache = new JLabel(field.getName(), JLabel.TRAILING);
				cache.setForeground(Color.WHITE);
				comp = createField(field);
				cache.setLabelFor(comp);
				cache.setToolTipText(field.getDescription());
				gui.add(cache);
				gui.add(comp);
				lay.putConstraint(SpringLayout.WEST, comp, Spring.constant(10, 20, 20), SpringLayout.EAST, cache);
				lay.putConstraint(SpringLayout.VERTICAL_CENTER, cache, Spring.constant(0, 0, 0), SpringLayout.VERTICAL_CENTER, comp);
				lay.putConstraint(SpringLayout.WEST, cache, 20, SpringLayout.WEST, gui);
				if (last != null) {
					lay.putConstraint(SpringLayout.NORTH, comp, Spring.constant(10), SpringLayout.SOUTH, last);
				}
				last = comp;
				
				if (longestField == null) {
					longestField = comp;
					longest = field.getName().length();
				} else if (longest < field.getName().length()) {
					longestField = comp;
					longest = field.getName().length();
				}
				
				fields.add(comp);
			}
		}
		
		for (Component component : fields) {
			if (component == longestField) {
				continue; //equals okay cause it will be a reference?
			}
			
			lay.putConstraint(SpringLayout.WEST, component, 0, SpringLayout.WEST, longestField);
		}
		
		
		lay.putConstraint(SpringLayout.SOUTH, gui, 20, SpringLayout.SOUTH, last);
		
		gui.validate();
		//SwingUtilities.gr;
	}
	
	private Component createField(PluginConfiguration.PluginConfigurationKey key) {
		if (key == PluginConfigurationKey.COMPASSTYPE || key == PluginConfigurationKey.INVOKERTYPE
				|| key == PluginConfigurationKey.RECALLERTYPE) {
			JComboBox<String> comp = new JComboBox<>(new DefaultComboBoxModel<String>());
			List<String> list = getMaterialList();
			
			for (String e : list)
				comp.addItem(e);
			
			comp.setSelectedItem(YamlWriter.toStandardFormat(config.getBaseValue(key).toString()));
			MaterialFieldHolder holder = new MaterialFieldHolder(this, key, comp);
			holders.add(holder);
			comp.addActionListener(holder);
			//comp.listener
			
			return comp;
		}
		
		if (key == PluginConfigurationKey.ALTERTYPE || key == PluginConfigurationKey.MARKLOCTYPE) {
			JComboBox<String> comp = new JComboBox<>(new DefaultComboBoxModel<String>());
			List<String> list = BlockChangeEffectWindow.getPlaceableMaterials();
			
			for (String e : list)
				comp.addItem(e);
			
			comp.setSelectedItem(YamlWriter.toStandardFormat(config.getBaseValue(key).toString()));
			MaterialFieldHolder holder = new MaterialFieldHolder(this, key, comp);
			holders.add(holder);
			comp.addActionListener(holder);
			//comp.listener
			
			return comp;
		}
		
		if (key.getDef() instanceof Boolean) {
			ButtonGroup group = new ButtonGroup();
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
			
			boolean on = (Boolean) config.getBaseValue(key);
			
			JRadioButton button;
			button = new JRadioButton("true", on);
			button.setBackground(Color.DARK_GRAY);
			button.setForeground(Color.WHITE);
			button.addActionListener(new BooleanFieldHolder(this, key, button));
			group.add(button);
			buttonPanel.add(button);
			button = new JRadioButton("false", !on);
			button.setBackground(Color.DARK_GRAY);
			button.setForeground(Color.WHITE);
			BooleanFieldHolder holder = new BooleanFieldHolder(this, key, button);
			button.addActionListener(holder);
			holders.add(holder);
			group.add(button);
			buttonPanel.add(button);
			
			return buttonPanel;
		}
		
//		if (key.getDef() instanceof Map) {
//			MapEditor edit = new MapEditor(config, key, "Project", (Map<>))
//		}
		if (key == PluginConfigurationKey.MUSICDURATIONS) {
			
			
			final ProjectWindow caller = this;
			
			JButton button = new JButton("Edit");
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					Map<Sound, Double> map = config.getMusicDurations();
					MapEditor.showEditor(MUSIC_DURATION_KEY, caller, "Music Durations", map,
							SoundParser.instance(), DoubleParser.instance());
				}
				
			});
			
			
			return button;
		}
		
		
		//anything else just do a text field
		JTextField field = new JTextField(
				config.getBaseValue(key).toString(), 20
				);
		TextFieldHolder holder;
		
		if (key.getDef() instanceof Double) 
			holder = new TextFieldHolder(this, key, field, TextFieldHolder.Type.DOUBLE);
		else if (key.getDef() instanceof Integer)
			holder = new TextFieldHolder(this, key, field, TextFieldHolder.Type.INT);
		else
			holder = new TextFieldHolder(this, key, field, TextFieldHolder.Type.OTHER);
		
		field.addFocusListener(holder);
		
		return field;
	}

	@Override
	public void updateMap(int id, Map<Sound, Double> result) {
		if (id == MUSIC_DURATION_KEY) {
			System.out.println("setting as " + result);
			config.setMusicDurations(result);
		}
		
	}
}
