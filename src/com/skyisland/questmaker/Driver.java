package com.skyisland.questmaker;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Main driver class. Starts the program, pops up the window, and starts the ball rolling.
 * @author Skyler
 *
 */
public class Driver {

	public static Driver driver;
	
	public static Menus menus;
	
	private JFrame mainWindow;
	
	public static void main(String[] args) {
		driver = new Driver();
	}
	
	public Driver() {
		mainWindow = new JFrame("QuestMaker");
		mainWindow.setVisible(true);
		mainWindow.setSize(640, 480);
		mainWindow.add(new JPanel());
		
		createMenus(mainWindow);
	}
	
	private void createMenus(JFrame frame) {
		MenuBar bar = new MenuBar();
		
		menus = new Menus(bar);
		
		Menu menu = new Menu("Project");
		menu.add(new MenuItem("Exit"));
		
		menus.addMenu(menu);
		
		frame.setMenuBar(bar);
	}

}
