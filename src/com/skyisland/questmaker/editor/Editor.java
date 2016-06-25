package com.skyisland.questmaker.editor;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import com.skyisland.questmaker.swingutils.Theme;
import com.skyisland.questmaker.swingutils.Theme.Themed;

public class Editor implements Themed {

	private Map<EditorWindow, JInternalFrame> openWindows;
	
	private JDesktopPane pane;
	
	public Editor(JDesktopPane pane) {
		this.pane = pane;
		pane.setBackground(Theme.BACKGROUND_BASE.register(this));
		this.openWindows = new HashMap<>();
	}
	
	public void openWindow(EditorWindow window) {
		openWindow(window, new Dimension(500, 750));
	}
	
	public void openWindow(EditorWindow window, Dimension windowSize){
		if (window == null)
			return;
		
		if (!openWindows.isEmpty())
		for (EditorWindow open : openWindows.keySet()) {
			if (open.getWindowTitle().equals(window.getWindowTitle()))
				return;
		}
		
		JInternalFrame frame = new JInternalFrame(window.getWindowTitle());
		frame.setVisible(true);
		JScrollPane spane = new JScrollPane();
		spane.setViewportView(window.getContainingComponent());
		frame.add(spane);
		frame.setClosable(true);
		frame.setAutoscrolls(true);
		frame.setResizable(true);
		frame.setPreferredSize(windowSize);
		frame.pack();
		frame.setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
		frame.addInternalFrameListener(new InternalFrameListener() {
			
			@Override
			public void internalFrameClosed(InternalFrameEvent e) {
				;
			}

			@Override
			public void internalFrameOpened(InternalFrameEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void internalFrameClosing(InternalFrameEvent e) {
				closeWindow(window);
			}

			@Override
			public void internalFrameIconified(InternalFrameEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void internalFrameDeiconified(InternalFrameEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void internalFrameActivated(InternalFrameEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void internalFrameDeactivated(InternalFrameEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		openWindows.put(window, frame);
		pane.add(frame);
		frame.toFront();
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

		pane.revalidate();
		pane.repaint();
		
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
		
		pane.revalidate();
		pane.repaint();
		return true;
	}
	
	public void dirty(EditorWindow window, boolean dirty) {
		JInternalFrame frame = openWindows.get(window);
		if (frame == null)
			return;
		
		if (dirty)
			frame.setTitle(window.getWindowTitle() + "*");
		else
			frame.setTitle(window.getWindowTitle());
	}

	@Override
	public void themeChange(Theme theme) {
		if (theme == Theme.BACKGROUND_BASE)
			pane.setBackground(theme.get());
	}
}
