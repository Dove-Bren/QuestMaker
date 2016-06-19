package com.skyisland.questmaker.quest;

import java.util.HashMap;
import java.util.Map;

import com.skyisland.questmanager.configuration.QuestConfigurationField;

public class QuestTemplate {

	private Map<QuestConfigurationField, Object> values;
	
	public QuestTemplate(String name) {
		this.values = new HashMap<>();
	}
	
	public void setValue(QuestConfigurationField field, Object value) {
		values.put(field, value);
	}
	
	public Object getValue(QuestConfigurationField field) {
		return values.get(field);
	}
	
}
