package com.skyisland.questmaker.explorer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 * Holds all of the items displayed in the explorer panel.
 * @author smanzana
 *
 */
public class ProjectExplorer {
	
	private List<ExplorerItem> tree;
	
	private JPanel panel;
	
	private JList<String> list;
	
	private DefaultListModel<String> data;
	
	public ProjectExplorer(JPanel fileViewer) {
		this.panel = fileViewer;
		this.data = new DefaultListModel<>();
		this.list = new JList<>(data);
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		//from http://stackoverflow.com/questions/4344682/double-click-event-on-jlist-element
		list.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent evt) {
		        JList list = (JList)evt.getSource();
		        if (evt.getClickCount() == 2) {

		            // Double-click detected
		            select(list.getSelectedIndex());
		        }
		    }
		});
		
		this.panel.add(new JScrollPane(list));
		
		
		this.tree = new LinkedList<>();
	}
	
	public void addItem(ExplorerItem item) {
		System.out.println("whoops");
		tree.add(item);
		data.addElement(item.getTitle());
	}
	
	public void addItem(ExplorerItem item, int position) {
		tree.add(position, item);
		data.add(position, item.getTitle());
	}
	
	public void removeItem(int position) {
		tree.remove(position);
		data.remove(position);
	}
	
	public void refresh() {
		data.clear();
		for (ExplorerItem item : tree) 
			data.addElement(item.getTitle());
	}
	
	public void clear() {
		data.clear();
		tree.clear();
	}
	
	private void select(int index) {
		if (index == -1) {
			//no selection
			return;
		}
		
		tree.get(index).open();
	}
}
