package com.skyisland.questmaker;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.util.HashMap;
import java.util.Map;

/**
 * Registers and tracks menus and menuitems for the program for easier getting and modifying.
 * @author Skyler
 *
 */
public class Menus {
	
	private static int key_base = 0;
	
	private MenuBar menuBar;
	
	private Map<Integer, Menu> menus;
	
	private Map<Integer, MenuItem> items;
	
	public Menus(MenuBar menuBar) {
		this.menuBar = menuBar;
		this.menus = new HashMap<>();
		this.items = new HashMap<>();
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
}
