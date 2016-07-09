package com.skyisland.questmaker.editor.spell;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.skyisland.questmaker.Driver;
import com.skyisland.questmaker.editor.EditorWindow;
import com.skyisland.questmaker.spell.SpellEffectWindow;
import com.skyisland.questmaker.spell.SpellEffectWindowFactory;
import com.skyisland.questmaker.spell.SpellEffectWindowFactory.EffectType;
import com.skyisland.questmaker.spell.SpellTemplate;
import com.skyisland.questmaker.swingutils.Theme;
import com.skyisland.questmaker.swingutils.Theme.Themed;
import com.skyisland.questmanager.magic.spell.SimpleSelfSpell;
import com.skyisland.questmanager.magic.spell.Spell;
import com.skyisland.questmanager.magic.spell.effect.SpellEffect;

public abstract class SpellWindow implements EditorWindow, Themed {
	
	private static List<SpellType> types = new LinkedList<>();
	
	public static void registerType(SpellType type) {
		types.add(type);
	}
	
	public static List<SpellType> getRegisteredTypes() {
		return types;
	}
	
	/**
	 * Uses list of registered spell types to createa window for the given
	 * spell. If no type matches the given spell, null is returned instead.
	 * @param spell
	 * @return
	 */
	public static SpellWindow getWindow(SpellTemplate template, Spell spell) {
		for (SpellType type : types) {
			if (type.matches(spell))
				return type.buildWindow(template);
		}
		
		return null;
	}
	
	public static void init() {
		SpellWindow.registerType(new SpellType("Simple Self Spell") {

			@Override
			public SpellWindow buildWindow(SpellTemplate template) {
				return new SimpleSelfSpellWindow(template, template.getSpell() == null ? 
						new SimpleSelfSpell(0, 0, template.getName(), "")
						: (SimpleSelfSpell) template.getSpell());
			}

			@Override
			public boolean matches(Spell spell) {
				return (spell instanceof SimpleSelfSpell);
			}
			
		});
	}
	
	public abstract static class SpellType {
//		SIMPLESELFSPELL("Simple Self Spell", new SimpleSelfSpell(0, 0, "", "")),
//		SIMPLETARGETSPELL("Simple Target Spell", new SimpleTargetSpell(0, 0, "", "", 0.0, 0)),
//		SPELLWEAVINGSPELL("Spell Weaving Spell", new SpellWeavingSpell(0, 0, "", "", SpellWeavingSpell.SpellTarget.BOTH));
		
		private String title;
		
		public SpellType(String title) {
			this.title = title;
		}
		
		@Override
		public String toString() {
			return title;
		}
		
		public abstract SpellWindow buildWindow(SpellTemplate template);
		
		public abstract boolean matches(Spell spell);
	}
	
	private static enum Param {
		NAME,
		DESC,
		DIFFICULTY,
		COST;
	}
	
	protected static int EFFECT_ID = 0;
	
	private class EffectPanel {
		
		private SpellEffectWindow effect;
		
		private JPanel container;
		
		private int id;
		
		public EffectPanel(SpellEffectWindow effect) {
			id = EFFECT_ID++;
			this.effect = effect;
			container = new JPanel(new BorderLayout());
			container.setBackground(Theme.BACKGROUND_FORWARD.get());
			container.setForeground(Theme.TEXT_FORWARD.get());
			JPanel buttonContainer = new JPanel();
			buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.Y_AXIS));
			buttonContainer.setPreferredSize(new Dimension(70, 100));
			buttonContainer.setBackground(Theme.BACKGROUND_BASE.get());
			JButton button = new JButton("Delete");
			final EffectPanel panel = this;
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					deleteEffect(panel);
				}
				
			});
			buttonContainer.add(button);
			container.add(buttonContainer, BorderLayout.WEST);
			container.add(effect.getWindow());
			container.setBorder(BorderFactory.createLineBorder(Color.WHITE));
			
			container.setPreferredSize(new Dimension(300, 100));
			container.validate();
		}
		
		@Override
		public boolean equals(Object o) {
			if (o instanceof EffectPanel) 
				return ((EffectPanel) o).id == id;
			
			return false;
		}
	}
	
	protected SpellTemplate template;
	
	private JPanel gui;
	
	private List<EffectPanel> effects;
	
	private JPanel effectPanel;
	
	//private DefaultListModel<EffectPanel> listModel;
	
	protected Spell spell;
	
	private boolean dirty;
	
	private Map<Param, JTextField> fields;
	
	private SpellWindow() {
		dirty = false;
		//listModel = new DefaultListModel<EffectPanel>();
		this.effectPanel = new JPanel();
		effectPanel.setLayout(new BoxLayout(effectPanel, BoxLayout.Y_AXIS));
		this.effects = new LinkedList<>();
//		this.effects.setCellRenderer(new ListCellRenderer<EffectPanel>() {
//
//			@Override
//			public Component getListCellRendererComponent(JList<? extends EffectPanel> list, EffectPanel value,
//					int index, boolean isSelected, boolean cellHasFocus) {
//				return value.container;
//			}
//		});
		
		fields = new EnumMap<>(Param.class);
		gui = new JPanel();
	}
	
	protected SpellWindow(SpellTemplate template, Spell spell) {
		this();
		this.template = template;
		this.spell = spell;
		
		if (!spell.getSpellEffects().isEmpty())
		for (SpellEffect ef : spell.getSpellEffects()) {
			//addEffect(SpellEffectWindowFactory.getWindow(SpellEffectWindowFactory.resolveType(ef)));
			addEffect(SpellEffectWindowFactory.getWindow(ef));
		}
		
		setupGui();
	}

	@Override
	public String getWindowTitle() {
		return "Spell: " + spell.getName();
	}

	@Override
	public boolean close() {
		if (!dirty) //check effects 
		for (EffectPanel efPanel : effects)
		if (efPanel.effect.isDirty()) {
			dirty = true;
			break;
		}
		if (dirty) {
			int ret = JOptionPane.showConfirmDialog(gui, "Would you like to save your changes to the spell?", "Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			
			if (ret == JOptionPane.CANCEL_OPTION)
				return false;
			
			if (ret == JOptionPane.YES_OPTION) {
				template.setName(fields.get(Param.NAME).getText());
				template.setSpell(this.buildSpell());
			}
		}
		
		return true;
	}

	@Override
	public Component getContainingComponent() {
		return gui;
	}
	
	private void setupGui() {
		JPanel paramPanel = new JPanel();
		SpringLayout lay = new SpringLayout();
		paramPanel.setLayout(lay);
		paramPanel.setBackground(Theme.BACKGROUND_EDITWINDOW.register(this));
		paramPanel.setForeground(Theme.TEXT_EDITWINDOW.register(this));
		
		
		JLabel label;
		JTextField field, swap;
		label = label("Name");
		paramPanel.add(label);
		lay.putConstraint(SpringLayout.WEST, label, 20, SpringLayout.WEST, paramPanel);
		lay.putConstraint(SpringLayout.NORTH, label, 20, SpringLayout.NORTH, paramPanel);
		field = new JTextField(spell.getName(), 10);
		paramPanel.add(field);
		fields.put(Param.NAME, field);
		lay.putConstraint(SpringLayout.WEST, field, 0, SpringLayout.WEST, label);
		lay.putConstraint(SpringLayout.NORTH, field, 3, SpringLayout.SOUTH, label);
		
		swap = new JTextField(spell.getDescription(), 40);
		paramPanel.add(swap);
		lay.putConstraint(SpringLayout.WEST, swap, 10, SpringLayout.EAST, field);
		lay.putConstraint(SpringLayout.NORTH, swap, 0, SpringLayout.NORTH, field);
		fields.put(Param.DESC, swap);
		label = label("Description");
		paramPanel.add(label);
		lay.putConstraint(SpringLayout.SOUTH, label, -3, SpringLayout.NORTH, swap);
		lay.putConstraint(SpringLayout.WEST, label, 0, SpringLayout.WEST, swap);
		
		lay.putConstraint(SpringLayout.EAST, paramPanel, 20, SpringLayout.EAST, swap);
		
		//next line; Difficulty, cost
		label = label("Difficulty");
		paramPanel.add(label);
		lay.putConstraint(SpringLayout.NORTH, label, 10, SpringLayout.SOUTH, field);
		lay.putConstraint(SpringLayout.WEST, label, 0, SpringLayout.WEST, field);
		
		field = new JFormattedTextField(spell.getDifficulty());
		field.setColumns(5);
		paramPanel.add(field);
		fields.put(Param.DIFFICULTY, field);
		lay.putConstraint(SpringLayout.NORTH, field, 3, SpringLayout.SOUTH, label);
		lay.putConstraint(SpringLayout.WEST, field, 0, SpringLayout.WEST, label);
		
		swap = new JFormattedTextField(spell.getCost());
		swap.setColumns(5);
		paramPanel.add(swap);
		fields.put(Param.COST, swap);
		lay.putConstraint(SpringLayout.WEST, swap, 10, SpringLayout.EAST, field);
		lay.putConstraint(SpringLayout.NORTH, swap, 0, SpringLayout.NORTH, field);
		label = label("Cost");
		paramPanel.add(label);
		lay.putConstraint(SpringLayout.SOUTH, label, -3, SpringLayout.NORTH, swap);
		lay.putConstraint(SpringLayout.WEST, label, 0, SpringLayout.WEST, swap);
		
		extendGui(paramPanel, swap);
		paramPanel.setPreferredSize(new Dimension(300, 200));
		paramPanel.validate();
		

		JPanel listPanel = new JPanel();
		JScrollPane pane = new JScrollPane(listPanel);
		listPanel.setPreferredSize(new Dimension(300, 100));
		listPanel.setBackground(Theme.BACKGROUND_FORWARD.register(this));
		listPanel.setForeground(Theme.TEXT_FORWARD.register(this));
		lay = new SpringLayout();
		listPanel.setLayout(lay);
		JButton button = new JButton("Add Effect");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addNewEffect();
			}
		});
		
		listPanel.add(button);
		lay.putConstraint(SpringLayout.NORTH, button, 20, SpringLayout.NORTH, listPanel);
		lay.putConstraint(SpringLayout.WEST, button, 20, SpringLayout.WEST, listPanel);
		listPanel.setPreferredSize(new Dimension(300, 500));
		effectPanel.validate();
		listPanel.add(effectPanel);
		lay.putConstraint(SpringLayout.NORTH, effectPanel, 20, SpringLayout.SOUTH, button);
		lay.putConstraint(SpringLayout.WEST, effectPanel, 0, SpringLayout.WEST, listPanel);
		lay.putConstraint(SpringLayout.EAST, effectPanel, 0, SpringLayout.EAST, listPanel);
		//lay.putConstraint(SpringLayout.SOUTH, listPanel, 0, SpringLayout.SOUTH, effectPanel);
		//lay.putConstraint(SpringLayout.NORTH, listPanel, Spring.constant(5, 5, 10000), SpringLayout.SOUTH, listPanel);
		
		listPanel.validate();
		pane.validate();
		lay = new SpringLayout();
		gui.setLayout(lay);
		gui.add(paramPanel);
		//gui.add(listPanel, BorderLayout.CENTER);
		gui.add(pane);
		lay.putConstraint(SpringLayout.WEST, paramPanel, 0, SpringLayout.WEST, gui);
		lay.putConstraint(SpringLayout.EAST, gui, 0, SpringLayout.EAST, paramPanel);
		lay.putConstraint(SpringLayout.NORTH, paramPanel, 0, SpringLayout.NORTH, gui);
		lay.putConstraint(SpringLayout.WEST, pane, 0, SpringLayout.WEST, gui);
		lay.putConstraint(SpringLayout.EAST, pane, 0, SpringLayout.EAST, paramPanel);
		lay.putConstraint(SpringLayout.NORTH, pane, 0, SpringLayout.SOUTH, paramPanel);
		lay.putConstraint(SpringLayout.SOUTH, gui, 0, SpringLayout.SOUTH, pane);
		
		gui.validate();
	}
	
	/**
	 * Called after the overarching window type has set up fields on the given JPanel. Subclasses can then
	 * add custom fields, etc.
	 * <p>
	 * The layout manager ( a SpringLayout) has been set up for the previous fields in a linear fashion, such that
	 * getting hte last component will return the last field added.
	 * </p>
	 * <p>
	 * After this call returns, the gui is validated and expected to be displayed to the user.
	 * </p>
	 */
	protected abstract void extendGui(JPanel gui, Component last);
	
	protected JLabel label(String label) {
		JLabel lab = new JLabel(label);
		lab.setForeground(Theme.TEXT_EDITWINDOW.get());
		return lab;
	}

	public Spell buildSpell() {
		if (spell == null)
			return null;
		
		spell.getSpellEffects().clear();
		
		for (EffectPanel ef : effects) {
			spell.addSpellEffect(ef.effect.getEffect());
		}
		
		doSpellPass(spell);
		
		return spell;
	}
	
	/**
	 * Gives implementaitons a chance to apply their configurations to the passed spell.<br />
	 * The spell has the base spell parameters set, and a accurate list of effects. Any additional
	 * information should be added by the implementing class.
	 * @param spell
	 */
	protected abstract void doSpellPass(Spell spell);

	@Override
	public void themeChange(Theme theme) {
		// TODO Auto-generated method stub
		
	}
	
	protected void dirty() {
		dirty = true;
		Driver.driver.getEditor().dirty(this, true);
	}
	
	protected void addEffect(SpellEffectWindow effect) {
		EffectPanel panel = new EffectPanel(effect);
		effects.add(panel);
		effectPanel.add(panel.container);
		
		gui.invalidate();
		effectPanel.revalidate();
		gui.revalidate();
		gui.repaint();
	}
	
	protected void deleteEffect(EffectPanel panel) {
		effects.remove(panel);
		effectPanel.remove(panel.container);
		
		gui.invalidate();
		effectPanel.revalidate();
		gui.revalidate();
		gui.repaint();
		dirty();
	}
	
	/**
	 * Show a menu with available effect types and add it to the list
	 */
	private void addNewEffect() {
		JDialog picker = new JDialog(Driver.driver.getMainWindow(), "Add New Effect", true);
		picker.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		JPanel panel = new JPanel();
		SpringLayout lay = new SpringLayout();
		panel.setLayout(lay);
		
		JComboBox<EffectType> typeField;
		
		JLabel label;
		Component field;
		label = new JLabel("Select the type of the new effect");
		label.setForeground(Theme.TEXT_EDITWINDOW.get());
		panel.add(label);
		lay.putConstraint(SpringLayout.NORTH, label, 20, SpringLayout.NORTH, panel);
		lay.putConstraint(SpringLayout.WEST, label, 20, SpringLayout.WEST, panel);
		lay.putConstraint(SpringLayout.EAST, panel, 20, SpringLayout.EAST, label);
		field = label;
		
		label = new JLabel("Effect Type");
		label.setForeground(Theme.TEXT_EDITWINDOW.get());
		panel.add(label);
		lay.putConstraint(SpringLayout.WEST, label, 20, SpringLayout.WEST, panel);
		lay.putConstraint(SpringLayout.NORTH, label, 20, SpringLayout.SOUTH, field);
		typeField = new JComboBox<EffectType>(SpellEffectWindowFactory.EffectType.values()); 
		panel.add(typeField);
		lay.putConstraint(SpringLayout.NORTH, typeField, 3, SpringLayout.SOUTH, label);
		lay.putConstraint(SpringLayout.WEST, typeField, 0, SpringLayout.WEST, label);
		lay.putConstraint(SpringLayout.EAST, typeField, 0, SpringLayout.EAST, field);
		field = typeField;
		
		JButton submit = new JButton("Submit");
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				picker.dispose();
			}
		});
		panel.add(submit);		
		lay.putConstraint(SpringLayout.NORTH, submit, 15, SpringLayout.SOUTH, field);
		lay.putConstraint(SpringLayout.HORIZONTAL_CENTER, submit, 0, SpringLayout.HORIZONTAL_CENTER, field);
		lay.putConstraint(SpringLayout.SOUTH, panel, 20, SpringLayout.SOUTH, submit);
		
		
		panel.validate();
		panel.setBackground(Theme.BACKGROUND_SYSTEM.get());
		panel.setForeground(Theme.TEXT_FORWARD.get());
		picker.add(panel);
		picker.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				picker.dispose();
				typeField.setEnabled(false);
			}
		});
		
		int offsetx, offsety;
		
		picker.pack();
		Dimension size = picker.getSize();
		offsetx = size.width / 2;
		offsety = size.height / 2;
		size = Driver.driver.getMainWindow().getSize();
		picker.setLocation((size.width / 2) - offsetx, (size.height / 2) - offsety);
		picker.validate();
		
		picker.setVisible(true);
		
		if (!typeField.isEnabled()) {
			//closed out
			return;
		}
		
		addEffect(SpellEffectWindowFactory.getWindow((EffectType) typeField.getSelectedItem()));
		dirty();
	}
	
}
