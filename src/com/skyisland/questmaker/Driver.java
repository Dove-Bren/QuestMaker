package com.skyisland.questmaker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import com.skyisland.questmaker.actions.ExitAction;
import com.skyisland.questmaker.actions.OpenProjectAction;

/**
 * Main driver class. Starts the program, pops up the window, and starts the ball rolling.
 * @author Skyler
 *
 */
public class Driver {

	public static Driver driver;
	
	public static Menus menus;
	
	private JFrame mainWindow;
	
	private JScrollPane fileViewer;
	
	private JPanel editor;
	
	private JToolBar toolBar;
	
	public static void main(String[] args) {
		driver = new Driver();
	}
	
	public Driver() {
		mainWindow = new JFrame("QuestMaker");
		
		toolBar = new JToolBar("toolbar");
		toolBar.setFloatable(false);
		toolBar.setAutoscrolls(false);
		toolBar.setPreferredSize(new Dimension(0,32));
		JButton button = new JButton();
		
//		URL iconAddr = null;;
//		try {
//			iconAddr = ((new File("src/resources/icon_open.png")).toURI().toURL());
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		URL iconAddr = Driver.class.getResource("icon_open.png");
		
		button.setToolTipText("Open a QuestManager project");
		
		button.setAction(OpenProjectAction.instance());
		if (iconAddr != null)
			button.setIcon(new ImageIcon(iconAddr));
		else 
			button.setText("Open");
		button.setRolloverEnabled(true);
		
		toolBar.add(button);
		mainWindow.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		fileViewer = new JScrollPane(new JPanel());
		fileViewer.setPreferredSize(new Dimension(200, 400));
		mainWindow.getContentPane().add(fileViewer, BorderLayout.LINE_START);
		
		editor = new JPanel();
		editor.setPreferredSize(new Dimension(400,400));
		editor.setBackground(Color.WHITE);
		editor.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		mainWindow.getContentPane().add(editor, BorderLayout.CENTER);
		
		createMenus(mainWindow);
		
		
		//mainWindow.setSize(640, 480);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.pack();
		mainWindow.setVisible(true);
	}
	
	private void createMenus(JFrame frame) {
		MenuBar bar = new MenuBar();
		
		menus = new Menus(bar);
		
		Menu menu = new Menu("Project");
		MenuItem item = new MenuItem("Exit");
		item.addActionListener(ExitAction.instance());
		menu.add(item);
		
		menus.addMenu(menu);
		
		frame.setMenuBar(bar);
	}
	
	public JFrame getMainWindow() {
		return this.mainWindow;
	}

}
