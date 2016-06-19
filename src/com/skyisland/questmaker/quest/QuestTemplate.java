package com.skyisland.questmaker.quest;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;

import com.skyisland.questmaker.project.ProjectResource;
import com.skyisland.questmanager.configuration.QuestConfigurationField;

public class QuestTemplate implements ProjectResource {

	private Map<QuestConfigurationField, Object> values;
	
	public QuestTemplate(String name) {
		this.values = new HashMap<>();
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
	
}
