package com.skyisland.questmaker.quest;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;

import com.skyisland.questmaker.Driver;
import com.skyisland.questmaker.editor.QuestWindow;
import com.skyisland.questmaker.explorer.ExplorerItem;
import com.skyisland.questmaker.project.ProjectResource;
import com.skyisland.questmanager.configuration.QuestConfigurationField;

public class QuestTemplate implements ProjectResource, ExplorerItem {

	private Map<QuestConfigurationField, Object> values;
	
	public QuestTemplate(String name) {
		this.values = new HashMap<>();
		values.put(QuestConfigurationField.NAME, name);
	}
	
	public QuestTemplate(YamlConfiguration sourceFile) {
		values = new HashMap<>();
		
		if (sourceFile != null)
		for (QuestConfigurationField field : QuestConfigurationField.values()) {
			values.put(field, sourceFile.get(field.getKey(), field.getDefault()));
		}
		
	}
	
	public void setValue(QuestConfigurationField field, Object value) {
		values.put(field, value);
	}
	
	public Object getValue(QuestConfigurationField field) {
		return values.get(field);
	}

	@Override
	public void save(File saveFile) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTitle() {
		return values.get(QuestConfigurationField.NAME).toString();
	}

	@Override
	public boolean canExpand() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<ExplorerItem> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void open() {
		Driver.driver.getEditor().openWindow(new QuestWindow(this));
	}
	
}
