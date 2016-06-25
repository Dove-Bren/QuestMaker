package com.skyisland.questmaker.editor;

import java.awt.Component;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

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
	
	private static enum Param {
		NAME,
		DESC,
		DIFFICULTY,
		COST,
		SPEED,
		RANGE;
	}
	
	private static class EffectHolder {
		
		private SpellEffectWindow effect;
		
		public EffectHolder(SpellEffectWindow effect) {
			this.effect = effect;
		}
	}
	
	private SpellTemplate template;
	
	private JPanel gui;
	
	private Spell spell;
	
	private boolean dirty;
	
	private List<EffectHolder> effects;
	
	private Map<Param, Component> fields;
	
	private SpellWindow() {
		dirty = false;
		this.effects = new LinkedList<>();
		fields = new EnumMap<>(Param.class);
		gui = new JPanel();
	}
	
	public SpellWindow(SpellTemplate template, String name, SpellType type) {
		this();
		this.template = template;
		this.spell = type.getBase();
		this.spell.setName(name);
		setupGui(type);
	}
	
	public SpellWindow(SpellTemplate template, Spell spell) {
		this();
		this.template = template;
		this.spell = spell;
		
		SpellType type = resolveType(spell);
		setupGui(type);
	}

	@Override
	public String getWindowTitle() {
		return "Spell: " + spell.getName();
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
		SpringLayout lay = new SpringLayout();
		gui.setLayout(lay);
		
		
		JLabel label;
		JTextField field, swap;
		label = label("Name");
		gui.add(label);
		lay.putConstraint(SpringLayout.WEST, label, 20, SpringLayout.WEST, gui);
		lay.putConstraint(SpringLayout.NORTH, label, 20, SpringLayout.NORTH, gui);
		field = new JTextField(spell.getName(), 10);
		gui.add(field);
		fields.put(Param.NAME, field);
		lay.putConstraint(SpringLayout.WEST, field, 0, SpringLayout.WEST, label);
		lay.putConstraint(SpringLayout.NORTH, field, 3, SpringLayout.SOUTH, label);
		
		swap = new JTextField(spell.getDescription(), 40);
		gui.add(swap);
		lay.putConstraint(SpringLayout.WEST, swap, 10, SpringLayout.EAST, field);
		lay.putConstraint(SpringLayout.NORTH, swap, 0, SpringLayout.NORTH, field);
		fields.put(Param.DESC, swap);
		label = label("Description");
		gui.add(label);
		lay.putConstraint(SpringLayout.SOUTH, label, -3, SpringLayout.NORTH, swap);
		lay.putConstraint(SpringLayout.WEST, label, 0, SpringLayout.WEST, swap);
		
		lay.putConstraint(SpringLayout.EAST, gui, 20, SpringLayout.EAST, swap);
		
		//next line; Difficulty, cost
		label = label("Difficulty");
		gui.add(label);
		lay.putConstraint(SpringLayout.NORTH, label, 10, SpringLayout.SOUTH, field);
		lay.putConstraint(SpringLayout.WEST, label, 0, SpringLayout.WEST, field);
		
		field = new JFormattedTextField(spell.getDifficulty());
		field.setColumns(5);
		gui.add(field);
		fields.put(Param.DIFFICULTY, field);
		lay.putConstraint(SpringLayout.NORTH, field, 3, SpringLayout.SOUTH, label);
		lay.putConstraint(SpringLayout.WEST, field, 0, SpringLayout.WEST, label);
		
		swap = new JFormattedTextField(spell.getCost());
		swap.setColumns(5);
		gui.add(swap);
		fields.put(Param.COST, swap);
		lay.putConstraint(SpringLayout.WEST, swap, 10, SpringLayout.EAST, field);
		lay.putConstraint(SpringLayout.NORTH, swap, 0, SpringLayout.NORTH, field);
		label = label("Cost");
		gui.add(label);
		lay.putConstraint(SpringLayout.SOUTH, label, -3, SpringLayout.NORTH, swap);
		lay.putConstraint(SpringLayout.WEST, label, 0, SpringLayout.WEST, swap);
		
		if (type == SpellType.SIMPLETARGETSPELL) {
			SimpleTargetSpell tspell = (SimpleTargetSpell) spell;
			field = new JFormattedTextField(tspell.getMaxDistance());
			field.setColumns(5);
			gui.add(field);
			fields.put(Param.RANGE, field);
			lay.putConstraint(SpringLayout.SOUTH, field, 0, SpringLayout.SOUTH, swap);
			lay.putConstraint(SpringLayout.WEST, field, 10, SpringLayout.EAST, swap);
			label = label("Range");
			gui.add(label);
			lay.putConstraint(SpringLayout.SOUTH, label, -3, SpringLayout.NORTH, field);
			lay.putConstraint(SpringLayout.WEST, label, 0, SpringLayout.WEST, field);

			swap = new JFormattedTextField(tspell.getSpeed());
			swap.setColumns(5);
			gui.add(swap);
			fields.put(Param.RANGE, swap);
			lay.putConstraint(SpringLayout.SOUTH, swap, 0, SpringLayout.SOUTH, field);
			lay.putConstraint(SpringLayout.WEST, swap, 10, SpringLayout.EAST, field);
			label = label("Speed");
			gui.add(label);
			lay.putConstraint(SpringLayout.SOUTH, label, -3, SpringLayout.NORTH, swap);
			lay.putConstraint(SpringLayout.WEST, label, 0, SpringLayout.WEST, swap);
		}
		
		gui.validate();
	}
	
	private JLabel label(String label) {
		JLabel lab = new JLabel(label);
		lab.setForeground(Theme.TEXT_EDITWINDOW.get());
		return lab;
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
