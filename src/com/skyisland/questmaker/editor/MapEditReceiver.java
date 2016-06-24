package com.skyisland.questmaker.editor;

import java.util.Map;

public interface MapEditReceiver<K, V> {
	
	public void updateMap(int id, Map<K, V> result);
	
}
