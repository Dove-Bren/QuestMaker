package com.skyisland.questmaker;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;

import com.skyisland.questmaker.actions.CreateProjectAction;
import com.skyisland.questmaker.actions.CreateQuestAction;
import com.skyisland.questmaker.actions.ExitAction;
import com.skyisland.questmaker.actions.OpenProjectAction;
import com.skyisland.questmaker.actions.SaveProjectAction;
import com.skyisland.questmaker.actions.SaveProjectAsAction;

/**
 * Registers and tracks menus and menuitems for the program for easier getting and modifying.
 * @author Skyler
 *
 */
public class Menus {
	
	private static enum DEFAULT_MENUS {
		NEW_PROJECT(DEFAULT_CATS.FILE, CreateProjectAction.instance(), "New Project"),
		OPEN_PROJECT(DEFAULT_CATS.FILE, OpenProjectAction.instance(), "Open Project"),
		LINE1(DEFAULT_CATS.FILE),
		SAVE_PROJECT(DEFAULT_CATS.FILE, SaveProjectAction.instance(), "Save"),
		SAVE_AS_PROJECT(DEFAULT_CATS.FILE, SaveProjectAsAction.instance(), "Save As"),
		LINE2(DEFAULT_CATS.FILE),
		EXIT(DEFAULT_CATS.FILE, ExitAction.instance(), "Exit"),
		//EDIT
		
		//Project
		
		//Quest
		NEW_QUEST(DEFAULT_CATS.QUEST, CreateQuestAction.instance(), "New Quest")
		//NPC
		
		;
		
		
		protected static enum DEFAULT_CATS {
			FILE("File"),
			EDIT("Edit"),
			PROJECT("Project"),
			QUEST("Quest"),
			NPC("NPC"),
			HELP("Help");
			
			private String name;
			
			private DEFAULT_CATS(String name) {
				this.name = name;
			}
		}
		
		private DEFAULT_CATS menu;
		
		private Action action;
		
		private String title;
		
		private DEFAULT_MENUS(DEFAULT_CATS menu, Action action, String title) {
			this.menu = menu;
			this.action = action;
			this.title = title;
		}
		
		/**
		 * Indicates a line seperator should be used
		 */
		private DEFAULT_MENUS(DEFAULT_CATS menu) {
			this.menu = menu;
		}
	}
	
	private static int key_base = 0;
	
	private MenuBar menuBar;
	
	private Map<Integer, Menu> menus;
	
	private Map<Integer, MenuItem> items;
	
	public Menus(MenuBar menuBar) {
		this.menuBar = menuBar;
		this.menus = new HashMap<>();
		this.items = new HashMap<>();
		
		createDefaultMenus();
	}
	
	/**
	 * Registers a menu and adds it to the menu bar. The menu is then linked to
	 * an integer key, which is returned.
	 * @param menu The menu to register
	 * @return A key which can be used to fetch this menu quickly in the future.
	 */
	public int addMenu(Menu menu) {
		int key = key_base++;
		menus.put(key, menu);
		
		menuBar.add(menu);

		if (menu.getItemCount() > 0)
		for (int i = 0; i < menu.getItemCount(); i++)
			addItem(menu.getItem(i));
		
		return key;
	}
	
	/**
	 * Looks up a menu for the provided key and returns it.
	 * @param key
	 * @return The menu, or null if the key was not associated with anything.
	 */
	public Menu getMenu(int key) {
		return menus.get(key);
	}
	
	/**
	 * Looks up a menu by it's name. If multiple menus have the same name, this first in a hashed list is returned.
	 * @param title
	 * @return
	 */
	public Menu getMenu(String title) {
		for (Menu menu : menus.values())
		if (menu.getName().equals(title))
			return menu;
		
		return null;
	}
	
	/**
	 * Registers a new menu item to the menu handler. This method is meant to be used for late-minute
	 * additions to menus. If a menu is provided to the {@link #addMenu(Menu)}, all items are automatically registered
	 * as well.
	 * <p>Items registered with this method are not assigned to any menu!</p>
	 * @param item
	 * @return
	 */
	public int addItem(MenuItem item) {
		items.put(key_base, item);
		return key_base++;
	}
	
	public MenuItem getItem(int key) {
		return items.get(key);
	}
	
	private void createDefaultMenus() {
		Menu menu;
		MenuItem mitem;
		for (DEFAULT_MENUS.DEFAULT_CATS category : DEFAULT_MENUS.DEFAULT_CATS.values()) {
			menu = new Menu(category.name);
			for (DEFAULT_MENUS item : DEFAULT_MENUS.values()) {
				if (item.menu == category) {
					if (item.title == null) {
						menu.addSeparator();
						continue;
					}
					
					mitem = new MenuItem(item.title);
					mitem.addActionListener(item.action);
					menu.add(mitem);
				}
			}
			
			menuBar.add(menu);
		}
	}
}
