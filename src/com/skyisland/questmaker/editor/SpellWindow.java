package com.skyisland.questmaker.editor;

import java.awt.Component;
import java.util.List;

import javax.swing.JPanel;

import com.skyisland.questmaker.project.Project;
import com.skyisland.questmaker.spell.SpellEffectWindow;
import com.skyisland.questmaker.spell.SpellTemplate;
import com.skyisland.questmaker.swingutils.Theme;
import com.skyisland.questmaker.swingutils.Theme.Themed;
import com.skyisland.questmanager.magic.spell.SimpleSelfSpell;
import com.skyisland.questmanager.magic.spell.SimpleTargetSpell;
import com.skyisland.questmanager.magic.spell.Spell;

public class SpellWindow implements EditorWindow, Themed {
	
	public enum SpellType {
		SIMPLESELFSPELL("Simple Self Spell", new SimpleSelfSpell(0, 0, "", "")),
		SIMPLETARGETSPELL("Simple Target Spell", new SimpleTargetSpell(0, 0, "", "", 0.0, 0));
		
		
		private Spell base;
		
		private String title;
		
		private SpellType(String title, Spell base) {
			this.title = title;
			this.base = base;
		}
		
		private Spell getBase() {
			return base;
		}
		
		@Override
		public String toString() {
			return title;
		}
	}
	
	private static class EffectHolder {
		
		private SpellEffectWindow effect;
		
		public EffectHolder(SpellEffectWindow effect) {
			this.effect = effect;
		}
	}
	
	private SpellTemplate template;
	
	private JPanel gui;
	
	private String name;
	
	private Spell spell;
	
	private boolean dirty;
	
	private List<EffectHolder> effects;
	
	private SpellWindow() {
		dirty = false;
		gui = new JPanel();
	}
	
	public SpellWindow(SpellTemplate template, String name, SpellType type) {
		this();
		this.template = template;
		this.name = name;
		setupGui(type);
	}
	
	public SpellWindow(SpellTemplate template, Spell spell) {
		this();
		this.template = template;
		this.name = spell.getName();
		this.spell = spell;
		
		SpellType type = resolveType(spell);
		setupGui(type);
	}

	@Override
	public String getWindowTitle() {
		return "Spell: " + name;
	}

	@Override
	public boolean close() {
		//TODO SAVE
		return true;
	}

	@Override
	public Component getContainingComponent() {
		return gui;
	}
	
	private void setupGui(SpellType type) {
		gui.setBackground(Theme.BACKGROUND_EDITWINDOW.register(this));
		gui.setForeground(Theme.TEXT_EDITWINDOW.register(this));
		
		//TODO
		
		gui.validate();
	}

	public Spell buildSpell() {
		if (spell == null)
			return null;
		
		for (EffectHolder window : effects) {
			spell.addSpellEffect(window.effect.getEffect());
		}
		
		return spell;
	}

	@Override
	public void themeChange(Theme theme) {
		// TODO Auto-generated method stub
		
	}
	
	//BREAKS ON UPDATE
	private SpellType resolveType(Spell spell) {
		if (spell instanceof SimpleTargetSpell)
			return SpellType.SIMPLETARGETSPELL;
		if (spell instanceof SimpleSelfSpell)
			return SpellType.SIMPLESELFSPELL;
		
		return null;
	}
	
}
