package com.skyisland.questmaker.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.border.BevelBorder;

import com.skyisland.questmaker.Driver;
import com.skyisland.questmaker.swingutils.StringParser;
import com.skyisland.questmanager.QuestManagerPlugin;
import com.skyisland.questmanager.configuration.AlterablePluginConfiguration;
import com.skyisland.questmanager.configuration.PluginConfiguration.PluginConfigurationKey;
import com.skyisland.questmanager.configuration.utils.YamlWriter;

public class MapEditor<K, V> implements EditorWindow {
	
	private static int MIN_HEIGHT = 400;
	
	private static int MENU_HEIGHT = 40;
	
	private class TableRow {
		
		private JTextField left;
		
		private JTextField right;
		
		private K key;
		
		private V value;
		
		public TableRow(K startingKey, V startingValue) {
			key = startingKey;
			value = startingValue;
			this.left = new JTextField(startingKey.toString(), 20);
			this.left.addFocusListener(new FocusListener() {
				@Override
				public void focusGained(FocusEvent e) {
					;
				}

				@Override
				public void focusLost(FocusEvent e) {
					if (keyParser == null) {
						QuestManagerPlugin.logger.warning("No parser set for key in map editor!");
						return;
					}
						
					String text = left.getText().trim();

					K temp = keyParser.parse(text);
					if (temp != null)
						key = temp;
					
					left.setText(key.toString());
				}
			});
			this.right = new JTextField(startingValue.toString());
			this.right.addFocusListener(new FocusListener() {
				@Override
				public void focusGained(FocusEvent e) {
					;
				}

				@Override
				public void focusLost(FocusEvent e) {
					if (valueParser == null) {
						QuestManagerPlugin.logger.warning("No parser set for value in map editor!");
						return;
					}
						
					String text = right.getText().trim();
					V temp = valueParser.parse(text);
					if (temp != null)
						value = temp;
					
					right.setText(value.toString());
				}
			});
			
			left.setBackground(new Color(30, 30, 50));
			left.setForeground(new Color(255, 255, 220));
			right.setBackground(new Color(50, 50, 70));
			right.setForeground(new Color(255, 255, 220));
			left.setCaretColor(left.getForeground());
			right.setCaretColor(right.getForeground());
		}
		
		
	}

	private List<TableRow> map;
	
	private StringParser<K> keyParser;
	
	private StringParser<V> valueParser;
	
	private String name;
	
	private AlterablePluginConfiguration config;
	
	private PluginConfigurationKey key;
	
	private JPanel gui;
	
	private int selected;
	
	public static <K, V> void showEditor(AlterablePluginConfiguration config, PluginConfigurationKey key,
			String ownerName, Map<K, V> map, StringParser<K> keyParser, StringParser<V> valueParser) {
		MapEditor<K, V> editor = new MapEditor<K, V>(config, key, ownerName, map);
		editor.supplyKeyParser(keyParser);
		editor.supplyValueParser(valueParser);
		Driver.driver.getEditor().openWindow(
				editor, new Dimension(800, 400));
	}
	
	public MapEditor(AlterablePluginConfiguration config, PluginConfigurationKey key, String ownerName, Map<K, V> map) {
		this.map = new LinkedList<>();
		for (Entry<K, V> entry : map.entrySet()) {
			this.map.add(new TableRow(entry.getKey(), entry.getValue()));
		}
		this.name = ownerName;
		this.config = config;
		this.key = key;
		this.gui = new JPanel();
		selected = 0;
		
		setupGui();
	}
	
	public void supplyKeyParser(StringParser<K> parser) {
		keyParser = parser;
	}
	
	public void supplyValueParser(StringParser<V> parser) {
		valueParser = parser;
	}
	
	@Override
	public String getWindowTitle() {
		return name + ": " + YamlWriter.toStandardFormat(key.name());
	}

	@Override
	public boolean close() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Component getContainingComponent() {
		return gui;
	}
	
	private Map<K, V> asMap() {
		if (map.isEmpty())
			return new HashMap<>();
		
		Map<K, V> map = new HashMap<>();
		for (TableRow row : this.map) {
			if (row.key == null || row.value == null)
				continue;
			map.put(row.key, row.value);
		}
		return map;
	}
	
	private void setupGui() {
		JButton button;
		JPanel menu = new JPanel();
		SpringLayout lay = new SpringLayout();
		menu.setLayout(lay);
		button = new JButton("Submit");
		button.setPreferredSize(new Dimension(80, 30));
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				submit();
			}
		});
		menu.add(button);
		lay.putConstraint(SpringLayout.WEST, button, 40, SpringLayout.WEST, menu);
		lay.putConstraint(SpringLayout.NORTH, button, 5, SpringLayout.NORTH, menu);
		lay.putConstraint(SpringLayout.SOUTH, menu, 5, SpringLayout.SOUTH, button);
		
		
		button = new JButton("Insert");
		button.setPreferredSize(new Dimension(80, 30));
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				insertRow();
			}
		});
		menu.add(button);
		lay.putConstraint(SpringLayout.WEST, button, 500, SpringLayout.WEST, menu);
		lay.putConstraint(SpringLayout.NORTH, button, 5, SpringLayout.NORTH, menu);
		lay.putConstraint(SpringLayout.SOUTH, menu, 5, SpringLayout.SOUTH, button);
		
		button = new JButton("Delete");
		button.setPreferredSize(new Dimension(80, 30));
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				insertRow();
			}
		});
		menu.add(button);
		lay.putConstraint(SpringLayout.WEST, button, 600, SpringLayout.WEST, menu);
		lay.putConstraint(SpringLayout.NORTH, button, 5, SpringLayout.NORTH, menu);
		lay.putConstraint(SpringLayout.SOUTH, menu, 5, SpringLayout.SOUTH, button);
		lay.putConstraint(SpringLayout.EAST, menu, Spring.constant(20, 20, 1000), SpringLayout.EAST, button);
		
		menu.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		menu.setBackground(new Color(40, 40, 60));
		
		gui.setLayout(new BorderLayout());
		gui.add(menu, BorderLayout.NORTH);
		
		JPanel content = new JPanel();
		lay = new SpringLayout();
		content.setLayout(lay);
		TableRow last = null;
		for (TableRow row : map) {
			content.add(row.left);
			content.add(row.right);
			if (last != null)
				lay.putConstraint(SpringLayout.NORTH, row.left, 0, SpringLayout.SOUTH, last.left);
			else
				lay.putConstraint(SpringLayout.NORTH, row.left, 0, SpringLayout.NORTH, content);
			
			lay.putConstraint(SpringLayout.WEST, row.right, 0, SpringLayout.EAST, row.left);
			lay.putConstraint(SpringLayout.EAST, row.right, 0, SpringLayout.EAST, content);
			lay.putConstraint(SpringLayout.SOUTH, row.left, 50, SpringLayout.NORTH, row.left);
			lay.putConstraint(SpringLayout.NORTH, row.right, 0, SpringLayout.NORTH, row.left);
			lay.putConstraint(SpringLayout.SOUTH, row.right, 0, SpringLayout.SOUTH, row.left);
			
			last = row;
		}
		
		lay.putConstraint(SpringLayout.SOUTH, content, Spring.constant(0, 0, 1000), SpringLayout.SOUTH, last.left);
		
		content.setBackground(Color.DARK_GRAY);
		gui.add(content, BorderLayout.CENTER);
		
		gui.setPreferredSize(new Dimension(
				Math.max(MIN_HEIGHT, MENU_HEIGHT + map.size() * 50), 800));
		gui.validate();
	}
	
	private TableRow addRow(K key, V value) {
		TableRow row = new TableRow(key, value);
		map.add(row);
		return row;
	}
	
	private void insertRow() {
		
	}
	
	private void deleteRow() {
		
	}
	
	private void submit() {
		
	}
	
	private void selected(int pos) {
		this.selected = pos;
	}

}
