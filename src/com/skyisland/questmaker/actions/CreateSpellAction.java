package com.skyisland.questmaker.actions;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.skyisland.questmaker.Driver;
import com.skyisland.questmaker.editor.SpellWindow;
import com.skyisland.questmaker.editor.SpellWindow.SpellType;
import com.skyisland.questmaker.project.Project;
import com.skyisland.questmaker.spell.SpellTemplate;
import com.skyisland.questmaker.swingutils.Theme;

public class CreateSpellAction extends AbstractAction {
	private static CreateSpellAction instance = null;
	
	public static CreateSpellAction instance() {
		if (instance == null)
			instance = new CreateSpellAction();
		
		return instance;
	}
	
	private CreateSpellAction() {
		;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Project proj = Driver.driver.getOpenProject();
		if (proj == null) {
			JOptionPane.showMessageDialog(Driver.driver.getMainWindow(), "You must create or open a project before creating a quest");
			return;
		}
		
		JDialog picker = new JDialog(Driver.driver.getMainWindow(), "Create a new spell", true);
		picker.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		JPanel panel = new JPanel();
		SpringLayout lay = new SpringLayout();
		panel.setLayout(lay);
		
		JTextField nameField;
		JComboBox<SpellType> typeField;
		
		JLabel label;
		Component field;
		label = new JLabel("Name the new spell and set its type");
		label.setForeground(Theme.TEXT_EDITWINDOW.get());
		panel.add(label);
		lay.putConstraint(SpringLayout.NORTH, label, 20, SpringLayout.NORTH, panel);
		lay.putConstraint(SpringLayout.WEST, label, 20, SpringLayout.WEST, panel);
		field = label;
		
		label = new JLabel("Spell Name");
		label.setForeground(Theme.TEXT_EDITWINDOW.get());
		panel.add(label);
		lay.putConstraint(SpringLayout.NORTH, label, 40, SpringLayout.NORTH, field);
		lay.putConstraint(SpringLayout.WEST, label, 20, SpringLayout.WEST, panel);
		nameField = new JTextField("Spell", 30); field = nameField;
		panel.add(field);
		lay.putConstraint(SpringLayout.NORTH, field, 3, SpringLayout.SOUTH, label);
		lay.putConstraint(SpringLayout.WEST, field, 0, SpringLayout.WEST, label);
		lay.putConstraint(SpringLayout.EAST, panel, 20, SpringLayout.EAST, field);
		
		label = new JLabel("Spell Type");
		label.setForeground(Theme.TEXT_EDITWINDOW.get());
		panel.add(label);
		lay.putConstraint(SpringLayout.WEST, label, 20, SpringLayout.WEST, panel);
		lay.putConstraint(SpringLayout.NORTH, label, 20, SpringLayout.SOUTH, field);
		typeField = new JComboBox<SpellType>(SpellType.values()); field = typeField;
		panel.add(field);
		lay.putConstraint(SpringLayout.NORTH, field, 3, SpringLayout.SOUTH, label);
		lay.putConstraint(SpringLayout.WEST, field, 0, SpringLayout.WEST, label);
		lay.putConstraint(SpringLayout.EAST, field, 0, SpringLayout.EAST, nameField);
		
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
			//Closed window. cancel;
			return;
		}
		
		String name = nameField.getText().trim();
		SpellType type = (SpellType) typeField.getSelectedItem();
		
		if (name == null || name.isEmpty())
			return;
		
		if (proj.hasSpell(name)) {
			JOptionPane.showMessageDialog(Driver.driver.getMainWindow(), "A spell with that name already exists",
					"Couldn't make spell", JOptionPane.PLAIN_MESSAGE);
			return;
		}
		
		SpellTemplate template = new SpellTemplate(name);
		SpellWindow window = new SpellWindow(template, name, type);
		proj.addSpell(template);
		Driver.driver.getEditor().openWindow(window, new Dimension(625, 400));
	}

}
