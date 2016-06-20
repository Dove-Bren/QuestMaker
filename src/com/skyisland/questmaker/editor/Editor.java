package com.skyisland.questmaker.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

public class Editor {

	private Map<EditorWindow, JInternalFrame> openWindows;
	
	private JDesktopPane pane;
	
	public Editor(JDesktopPane pane) {
		this.pane = pane;
		this.openWindows = new HashMap<>();
	}
	
	public void openWindow(EditorWindow window) {
		if (window == null)
			return;
		
		JInternalFrame frame = new JInternalFrame(window.getWindowTitle());
		frame.setVisible(true);
		frame.add(window.getContainingComponent());
		frame.setAutoscrolls(true);
		frame.setClosable(true);
		frame.pack();
		openWindows.put(window, frame);
		pane.add(frame);
	}
	
	public boolean closeWindow(EditorWindow window) {
		if (window == null)
			return false;
		
		if (!window.close()) {
			return false;
		}
		
		JInternalFrame frame = openWindows.remove(window);
		if (frame != null)
			pane.remove(frame);
		
		pane.validate();
		
		return true;
	}
	
	public boolean closeAllWindows() {
		for (EditorWindow window : openWindows.keySet()) {
			if (!window.close()) {
				pane.validate();
				return false;
			}
				
			
			JInternalFrame frame = openWindows.remove(window);
			if (frame != null)
				pane.remove(frame);
		}
		
		pane.validate();
		return true;
	}
}
