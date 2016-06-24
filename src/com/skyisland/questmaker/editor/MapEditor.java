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
import java.util.ListIterator;
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
	
	protected static int ROW_ID_BASE = 0;
	
	private class TableRow {
		
		private JTextField left;
		
		private JTextField right;
		
		private K key;
		
		private V value;
		
		private int id;
		
		
		public TableRow(K startingKey, V startingValue) {
			id = ROW_ID_BASE++;
			key = startingKey;
			value = startingValue;
			final TableRow caller = this;
			this.left = new JTextField(key == null ? "" : startingKey.toString(), 20);
			this.left.addFocusListener(new FocusListener() {
				@Override
				public void focusGained(FocusEvent e) {
					selected(caller);
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
					
					left.setText(key == null ? "" : key.toString());
				}
			});
			
			this.right = new JTextField(value == null ? "" : value.toString());
			this.right.addFocusListener(new FocusListener() {
				@Override
				public void focusGained(FocusEvent e) {
					selected(caller);
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
					
					right.setText(value == null ? "" : value.toString());
				}
			});
			
			left.setBackground(new Color(30, 30, 50));
			left.setForeground(new Color(255, 255, 220));
			right.setBackground(new Color(50, 50, 70));
			right.setForeground(new Color(255, 255, 220));
			left.setCaretColor(left.getForeground());
			right.setCaretColor(right.getForeground());
		}
		
		@Override
		public boolean equals(Object o) {
			if (o instanceof MapEditor.TableRow)
				return ((MapEditor<?,?>.TableRow) o).id == id;
			
			return false;
		}
		
		@Override
		public int hashCode() {
			return id;
		}
		
		private void setValues(K key, V value) {
			this.key = key;
			this.value = value;
			left.setText(key == null ? "" : key.toString());
			right.setText(value == null ? "" : value.toString());
		}
	}

	private List<TableRow> map;
	
	private StringParser<K> keyParser;
	
	private StringParser<V> valueParser;
	
	private String name;
	
	private AlterablePluginConfiguration config;
	
	private PluginConfigurationKey key;
	
	private JPanel gui;
	
	private JPanel cells;
	
	protected int selected;
	
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
		
		cells = new JPanel();
		lay = new SpringLayout();
		cells.setLayout(lay);
		TableRow last = null;
		for (TableRow row : map) {
			cells.add(row.left);
			cells.add(row.right);
			if (last != null)
				lay.putConstraint(SpringLayout.NORTH, row.left, 0, SpringLayout.SOUTH, last.left);
			else
				lay.putConstraint(SpringLayout.NORTH, row.left, 0, SpringLayout.NORTH, cells);
			
			lay.putConstraint(SpringLayout.WEST, row.right, 0, SpringLayout.EAST, row.left);
			lay.putConstraint(SpringLayout.EAST, row.right, 0, SpringLayout.EAST, cells);
			lay.putConstraint(SpringLayout.SOUTH, row.left, 50, SpringLayout.NORTH, row.left);
			lay.putConstraint(SpringLayout.NORTH, row.right, 0, SpringLayout.NORTH, row.left);
			lay.putConstraint(SpringLayout.SOUTH, row.right, 0, SpringLayout.SOUTH, row.left);
			
			last = row;
		}
		
		//lay.putConstraint(SpringLayout.SOUTH, cells, Spring.constant(0, 0, 1000), SpringLayout.SOUTH, last.left);
		
		cells.setBackground(Color.DARK_GRAY);
		gui.add(cells, BorderLayout.CENTER);
		
		gui.setPreferredSize(new Dimension(
				Math.max(MIN_HEIGHT, MENU_HEIGHT + map.size() * 50), 800));
		gui.validate();
	}
	
	private TableRow addRow(K key, V value) {
		TableRow row = new TableRow(key, value), last = null;
		if (!map.isEmpty())
			last = map.get(map.size() - 1);
		map.add(row);
		cells.add(row.left);
		cells.add(row.right);
		SpringLayout lay = (SpringLayout) cells.getLayout();
		
		if (last != null)
			lay.putConstraint(SpringLayout.NORTH, row.left, 0, SpringLayout.SOUTH, last.left);
		else
			lay.putConstraint(SpringLayout.NORTH, row.left, 0, SpringLayout.NORTH, cells);
		
		lay.putConstraint(SpringLayout.WEST, row.right, 0, SpringLayout.EAST, row.left);
		lay.putConstraint(SpringLayout.EAST, row.right, 0, SpringLayout.EAST, cells);
		lay.putConstraint(SpringLayout.SOUTH, row.left, 50, SpringLayout.NORTH, row.left);
		lay.putConstraint(SpringLayout.NORTH, row.right, 0, SpringLayout.NORTH, row.left);
		lay.putConstraint(SpringLayout.SOUTH, row.right, 0, SpringLayout.SOUTH, row.left);
		
		cells.validate();
		
		return row;
	}
	
	private void insertRow() {
		TableRow last, cur;
		addRow(null, null);
		ListIterator<TableRow> it = map.listIterator(map.size());
		cur = it.previous();
		while (it.previousIndex() >= selected) {
			last = it.previous();
			cur.setValues(last.key, last.value);
			cur = last;
		}
		
		last = map.get(selected);
		last.setValues(null, null);
		
	}
	
	private void deleteRow() {
		
	}
	
	private void submit() {
		
	}
	
	private void selected(TableRow row) {
		int index = 0;
		for (TableRow a : map) {
			if (a.equals(row)) {
				selected = index;
				return;
			}
			index++;
		}
	}
}
