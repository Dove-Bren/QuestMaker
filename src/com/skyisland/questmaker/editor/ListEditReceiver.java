package com.skyisland.questmaker.editor;

import java.util.List;

public interface ListEditReceiver<T> {
	
	public void updateList(int id,List<T> result);
	
}
