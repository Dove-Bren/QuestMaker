package com.skyisland.questmaker.spell;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.skyisland.questmaker.Driver;
import com.skyisland.questmaker.editor.spell.SpellWindow;
import com.skyisland.questmaker.explorer.ExplorerItem;
import com.skyisland.questmaker.project.ProjectResource;
import com.skyisland.questmanager.magic.spell.Spell;

public class SpellTemplate implements ProjectResource, ExplorerItem {
	
	private String name;
	
	private Spell spell;
	
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
		return false;
	}

	@Override
	public List<ExplorerItem> getChildren() {
		return null;
	}

	@Override
	public void open() {
		SpellWindow window = SpellWindow.getWindow(this, spell);
		Driver.driver.getEditor().openWindow(window);
	}
	
	public String getName() {
		return name;
	}

	public Spell getSpell() {
		return spell;
	}

	public void setSpell(Spell spell) {
		this.spell = spell;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
