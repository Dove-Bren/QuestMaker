package com.skyisland.questmaker.spell;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.skyisland.questmaker.explorer.ExplorerItem;
import com.skyisland.questmaker.project.ProjectResource;

public class SpellTemplate implements ProjectResource, ExplorerItem {
	
	private String name;
	
	private String description;
	
	private int difficulty;
	
	private int cost;
	
	public SpellTemplate(String name) {
		this.name = name;
	}

	@Override
	public void save(File saveFile) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTitle() {
		return name;
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
		// TODO Auto-generated method stub
		
	}
	
	public String getName() {
		return name;
	}
	
}
