package com.skyisland.questmaker.editor;

import java.awt.Component;

/**
 * A window that can be opened in the editor. Holds a resource and allows edit operations on it.
 * @author smanzana
 *
 */
public interface EditorWindow {
	
	String getWindowTitle();

	boolean close();
	
	/**
	 * Used to register the given component to an open window, while allowing the implementing class
	 * the ability to govern and react to their own components in an unrestricted way.
	 * <p>
	 * This method must return a single component that holds all other components needed by this window.
	 * </p>
	 * @return
	 */
	Component getContainingComponent();
	
}
