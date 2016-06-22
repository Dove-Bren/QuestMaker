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
	
	public static enum Section {
		QUEST,
		REGION,
		SPELL;
	}
	
	private List<ExplorerItem> questSection;
	
	private List<ExplorerItem> regionSection;
	
	private List<ExplorerItem> spellSection;
	
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
		        @SuppressWarnings("unchecked")
				JList<String> list = (JList<String>)evt.getSource();
		        if (evt.getClickCount() == 2) {

		            // Double-click detected
		            select(list.getSelectedIndex());
		        }
		    }
		});
		
		this.panel.add(new JScrollPane(list));
		
		
		this.questSection = new LinkedList<>();
		this.regionSection = new LinkedList<>();
		this.spellSection = new LinkedList<>();
		
	}
	
	public void addItem(ExplorerItem item, Section section) {
		switch (section) {
		case QUEST:
			questSection.add(item);
			data.add(questSection.size() -1, item.getTitle());
			break;
		case REGION:
			regionSection.add(item);
			data.add(questSection.size() + regionSection.size() -1, item.getTitle());
			break;
		case SPELL:
			spellSection.add(item);
			data.addElement(item.getTitle());
			break;
		}
	}
	
	public void addItem(ExplorerItem item, Section section, int position) {
		int pos;
		switch (section) {
		case QUEST:
			System.out.println("index: " + position);
			pos = Math.min(questSection.size(), position);
			System.out.println("capped?: " + pos);
			questSection.add(pos, item);
			data.add(pos + 1, item.getTitle());
			break;
		case REGION:
			pos = Math.max(regionSection.size(), position);
			regionSection.add(pos, item);
			data.add(questSection.size() + pos + 2, item.getTitle());
			break;
		case SPELL:
			pos = Math.max(spellSection.size(), position);
			spellSection.add(pos, item);
			data.add(questSection.size() + regionSection.size() + pos + 3, item.getTitle());
			break;
		}
	}
	
	public void removeItem(Section section, int position) {
		int pos;
		switch (section) {
		case QUEST:
			pos = Math.max(questSection.size(), position);
			questSection.remove(pos);
			data.remove(pos + 1);
			break;
		case REGION:
			pos = Math.max(regionSection.size(), position);
			regionSection.remove(pos);
			data.remove(questSection.size() + pos + 2);
			break;
		case SPELL:
			pos = Math.max(spellSection.size(), position);
			spellSection.remove(pos);
			data.remove(questSection.size() + regionSection.size() + pos + 3);
			break;
		}
	}
	
	public void refresh() {
		data.clear();
		data.addElement("    Quests");
		for (ExplorerItem item : questSection) 
			data.addElement(item.getTitle());
		data.addElement("    Regions");
		for (ExplorerItem item : regionSection) 
			data.addElement(item.getTitle());
		data.addElement("    Spells");
		for (ExplorerItem item : spellSection) 
			data.addElement(item.getTitle());
	}
	
	public void clear() {
		data.clear();
		data.addElement("    Quests");
		data.addElement("    Regions");
		data.addElement("    Spells");
		questSection.clear();
		regionSection.clear();
		spellSection.clear();
	}
	
	private void select(int index) {
		if (index == -1) {
			//no selection
			return;
		}
		//check not label
		if (index == 0 || index == (questSection.size() + 1) || index == (questSection.size() + regionSection.size() + 2))
			return; 
		
		if (index <= questSection.size()) {
			questSection.get(index - 1).open();
		} else if (index <= questSection.size() + regionSection.size()) {
			regionSection.get((index - questSection.size()) -2).open();;
		} else {
			spellSection.get(index - (questSection.size() + regionSection.size() - 3)).open();;
		}
	}
}
