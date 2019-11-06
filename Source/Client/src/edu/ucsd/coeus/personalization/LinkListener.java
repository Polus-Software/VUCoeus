package edu.ucsd.coeus.personalization;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class LinkListener implements MouseListener {
	private String urlaction;
	private Component comp;
	
	public LinkListener(String urlaction, Component comp) {
		super();
		this.urlaction = urlaction;
		this.comp = comp;
	}
	
	public void mouseClicked(MouseEvent e) {
		ClientUtils.openURL(urlaction);
	}

	public void mouseEntered(MouseEvent e) {
        comp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));		
	}

	public void mouseExited(MouseEvent e) {
        comp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));		
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
		
	}
	
	

}
