package com.skyisland.questmaker.swingutils;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

public enum Theme {
	BACKGROUND_BASE(new Color(70, 70, 100)),
	BACKGROUND_FORWARD(new Color(114, 114, 150)),
	BACKGROUND_SYSTEM(new Color(130, 135, 150)),
	BACKGROUND_EDITWINDOW(new Color(40, 45, 40)),
	TEXT_EDITWINDOW(new Color(255, 255, 230)),
	TEXT_BASE(Color.WHITE),
	TEXT_FORWARD(new Color(255, 255, 230));
	
	public interface Themed {
		
		/**
		 * Uses to notify registered theme registrants that a theme has changed, and the registrant
		 * should update to match the new theme color
		 * @param theme
		 */
		void themeChange(Theme theme);
		
	}
	
	private Color color;
	
	private Set<Themed> registrants;
	
	private Theme(Color color) {
		this.color = color;
		this.registrants = new HashSet<>();
	}
	
	public Color get() {
		return color;
	}
	
	public Color register(Themed registrant) {
		this.registrants.add(registrant);
		return get();
	}
	
	public void unregister(Themed registrant) {
		registrants.remove(registrant);
	}
	
	public void setColor(Color color) {
		this.color = color;
		
		for (Themed r : registrants)
			r.themeChange(this);
	}
}
