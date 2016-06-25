package com.skyisland.questmaker.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.border.BevelBorder;

import com.skyisland.questmaker.Driver;
import com.skyisland.questmaker.swingutils.StringParser;
import com.skyisland.questmanager.QuestManagerPlugin;

public class ListEditor<T> implements EditorWindow {
	
	private static int MIN_HEIGHT = 400;
	
	private static int MENU_HEIGHT = 40;
	
	protected static int ROW_ID_BASE = 0;
	
	private class TableRow {
		
		private JTextField left;
		
		private T value;
		
		private int id;
		
		
		public TableRow(T startingValue) {
			id = ROW_ID_BASE++;
			value = startingValue;
			final TableRow caller = this;
			this.left = new JTextField(value == null ? "" : value.toString());
			//this.left.setPreferredSize(new Dimension(left.getWidth(), 35));
			this.left.setFont(new Font("Helvetica", Font.PLAIN, 14));
			this.left.addFocusListener(new FocusListener() {
				@Override
				public void focusGained(FocusEvent e) {
					selected(caller);
				}

				@Override
				public void focusLost(FocusEvent e) {
					doValueInput();
				}
			});
			
			left.setBackground(new Color(50, 50, 70));
			left.setForeground(new Color(255, 255, 220));
			left.setCaretColor(left.getForeground());
		}
		
		private void doValueInput() {
			if (valueParser == null) {
				QuestManagerPlugin.logger.warning("No parser set for value in map editor!");
				return;
			}
				
			String text = left.getText().trim();
			T temp = valueParser.parse(text), old = value;
			if (temp != null)
				value = temp;
			
			if (old != null && value != null)
			if ((old == null && value != null) || (old != null && value == null)
					|| !old.equals(value)) {
				Driver.driver.getEditor().dirty(window, true);
				dirty = true;
			}
			left.setText(value == null ? "" : value.toString());
		}
		
		@Override
		public boolean equals(Object o) {
			if (o instanceof ListEditor.TableRow)
				return ((ListEditor<?>.TableRow) o).id == id;
			
			return false;
		}
		
		@Override
		public int hashCode() {
			return id;
		}
		
		private void setValue(T value) {
			this.value = value;
			left.setText(value == null ? "" : value.toString());
		}
	}
	
	protected ListEditor<T> window;
	
	private boolean dirty;

	private List<TableRow> list;
	
	private StringParser<T> valueParser;
	
	private String name;
	
	private ListEditReceiver<T> sendback;
	
	private JPanel gui;
	
	private JPanel cells;
	
	protected int selected;
	
	private int key;
	
	public static <T> void showEditor(int key, ListEditReceiver<T> receiver,
			String title, List<T> list, StringParser<T> valueParser) {
		ListEditor<T> editor = new ListEditor<T>(key, receiver, title, list);
		editor.supplyValueParser(valueParser);
		Driver.driver.getEditor().openWindow(
				editor, new Dimension(500, 400));
	}
	
	public ListEditor(int key, ListEditReceiver<T> receiver, String title, List<T> list) {
		this.window = this;
		this.list = new LinkedList<>();
		this.key = key;
		for (T entry : list) {
			this.list.add(new TableRow(entry));
		}
		this.name = title;
		this.sendback = receiver;
		this.gui = new JPanel();
		selected = 0;
		dirty = false;
		setupGui();
	}
	
	public void supplyValueParser(StringParser<T> parser) {
		valueParser = parser;
	}
	
	@Override
	public String getWindowTitle() {
		return "List Editor: " + name;
	}

	@Override
	public boolean close() {
		selected = 0;
		for (TableRow row : list) {
			row.doValueInput();
		}
		
		if (!dirty)
			return true;
		
		int ret = JOptionPane.showConfirmDialog(gui, "Would you like to save your changes to the list?", "Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		
		if (ret == JOptionPane.CANCEL_OPTION)
			return false;
		
		if (ret == JOptionPane.YES_OPTION) {
			submit(false);
		}
		
		return true;
	}

	@Override
	public Component getContainingComponent() {
		return gui;
	}
	
	private List<T> asList() {
		if (list.isEmpty())
			return new LinkedList<>();
		
		List<T> list = new LinkedList<>();
		for (TableRow row : this.list) {
			if (row.value == null)
				continue;
			list.add(row.value);
		}
		return list;
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
				submit(true);
			}
		});
		menu.add(button);
		lay.putConstraint(SpringLayout.WEST, button, 40, SpringLayout.WEST, menu);
		lay.putConstraint(SpringLayout.NORTH, button, 5, SpringLayout.NORTH, menu);
		lay.putConstraint(SpringLayout.SOUTH, menu, 5, SpringLayout.SOUTH, button);
		
		
		button = new JButton("Add");
		button.setPreferredSize(new Dimension(80, 30));
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addRow(null);
			}
		});
		menu.add(button);
		lay.putConstraint(SpringLayout.WEST, button, 150, SpringLayout.WEST, menu);
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
		lay.putConstraint(SpringLayout.WEST, button, 250, SpringLayout.WEST, menu);
		lay.putConstraint(SpringLayout.NORTH, button, 5, SpringLayout.NORTH, menu);
		lay.putConstraint(SpringLayout.SOUTH, menu, 5, SpringLayout.SOUTH, button);
		
		button = new JButton("Delete");
		button.setPreferredSize(new Dimension(80, 30));
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				deleteRow();
			}
		});
		menu.add(button);
		lay.putConstraint(SpringLayout.WEST, button, 350, SpringLayout.WEST, menu);
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
		for (TableRow row : list) {
			cells.add(row.left);
			if (last != null) {
				lay.putConstraint(SpringLayout.NORTH, row.left, 0, SpringLayout.SOUTH, last.left);
				lay.putConstraint(SpringLayout.WEST, row.left, 0, SpringLayout.WEST, last.left);
				lay.putConstraint(SpringLayout.EAST, row.left, 0, SpringLayout.EAST, last.left);
			} else {
				lay.putConstraint(SpringLayout.NORTH, row.left, 0, SpringLayout.NORTH, cells);
				lay.putConstraint(SpringLayout.WEST, row.left, 0, SpringLayout.WEST, cells);
				lay.putConstraint(SpringLayout.EAST, cells, 0, SpringLayout.EAST, row.left);
			}
			
			last = row;
		}
		
		//lay.putConstraint(SpringLayout.SOUTH, cells, Spring.constant(0, 0, 1000), SpringLayout.SOUTH, last.left);
		
		cells.setBackground(Color.DARK_GRAY);
		gui.add(cells, BorderLayout.CENTER);
		
		gui.setPreferredSize(new Dimension(
				Math.max(MIN_HEIGHT, MENU_HEIGHT + list.size() * 50), 800));
		gui.validate();
	}
	
	private TableRow addRow(T value) {
		TableRow row = new TableRow(value), last = null;
		if (!list.isEmpty())
			last = list.get(list.size() - 1);
		list.add(row);
		cells.add(row.left);
		SpringLayout lay = (SpringLayout) cells.getLayout();
		
		if (last != null) {
			lay.putConstraint(SpringLayout.NORTH, row.left, 0, SpringLayout.SOUTH, last.left);
			lay.putConstraint(SpringLayout.WEST, row.left, 0, SpringLayout.WEST, last.left);
			lay.putConstraint(SpringLayout.EAST, row.left, 0, SpringLayout.EAST, last.left);
		} else {
			lay.putConstraint(SpringLayout.NORTH, row.left, 0, SpringLayout.NORTH, cells);
			lay.putConstraint(SpringLayout.WEST, row.left, 0, SpringLayout.WEST, cells);
			lay.putConstraint(SpringLayout.EAST, cells, 0, SpringLayout.EAST, row.left);
		}
		
		
		
		cells.revalidate();
		
		return row;
	}
	
	private void insertRow() {
		TableRow last, cur;
		addRow(null);
		ListIterator<TableRow> it = list.listIterator(list.size());
		cur = it.previous();
		while (it.previousIndex() >= selected) {
			last = it.previous();
			cur.setValue(last.value);
			cur = last;
		}
		
		last = list.get(selected);
		last.setValue(null);
		
		selected++;
	}
	
	private void deleteRow() {
		if (list.isEmpty())
			return;
		
		TableRow last, cur;
		ListIterator<TableRow> it = list.listIterator(selected);
		last = it.next();
		while (it.hasNext()) {
			cur = it.next();
			last.setValue(cur.value);
			last = cur;
		}
		
		TableRow row = list.get(list.size() - 1);
		if (row.value != null && !row.value.toString().isEmpty()) {
			Driver.driver.getEditor().dirty(window, true);
			dirty = true;
		}
		//cells.remove(cells.getComponentCount() - 1);
		//cells.remove(cells.getComponentCount() - 1);
		SpringLayout lay = (SpringLayout) cells.getLayout();
		lay.removeLayoutComponent(row.left);
		lay.removeLayoutComponent(row.left);
		cells.remove(row.left);
		cells.remove(row.left);
		list.remove(list.size() - 1);
		
		cells.revalidate();
		cells.repaint();
		selected = Math.max(0, Math.min(list.size() - 1, selected));
	}
	
	private void submit(boolean closeWindow) {
		for (TableRow row : list) {
			row.doValueInput();
		}
		sendback.updateList(key, asList());
		
		dirty = false;
		if (closeWindow)
			Driver.driver.getEditor().closeWindow(this);
		else
			Driver.driver.getEditor().dirty(this, false);
	}
	
	private void selected(TableRow row) {
		int index = 0;
		for (TableRow a : list) {
			if (a.equals(row)) {
				selected = index;
				return;
			}
			index++;
		}
	}
}
