package com.skyisland.questmaker.explorer;

import java.util.List;

public interface ExplorerItem {

	String getTitle();
	
	boolean canExpand();
	
	List<ExplorerItem> getChildren();
	
	void open();
	
}
