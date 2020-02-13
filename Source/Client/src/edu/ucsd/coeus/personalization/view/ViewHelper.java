package edu.ucsd.coeus.personalization.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import edu.mit.coeus.utils.CoeusComboBox;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.DollarCurrencyTextField;
import edu.ucsd.coeus.personalization.ClientUtils;
import edu.ucsd.coeus.personalization.PopUpListener;
import edu.ucsd.coeus.personalization.coeusforms.Attr;
import edu.ucsd.coeus.personalization.coeusforms.Cfield;

/**
 * 
 * @author rdias
 * static helper methods used only by views
 */
public class ViewHelper {
	
	private ViewHelper() {}
	protected static DateUtils dateUtils = new DateUtils();
	
//	private static CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();


	protected static Color getPanelForegroundColor() {
		return (java.awt.Color) javax.swing.UIManager.getDefaults().get(
				"Panel.foreground");
	}
	
	/**
	 * Will set following attributes: font, color, size, bg
	 * @param comp
	 * @param fattr
	 */
	protected static void setAttributes(JComponent comp, Attr fattr) {
		if (fattr == null) {
			return;
		}
		if (ClientUtils.isNotBlank(fattr.getFont())) {
			comp.setFont(Font.getFont(fattr.getFont()));
		}
		if (ClientUtils.isNotBlank(fattr.getBgcolor())) { //Set color
			try {
				java.awt.Color color = Color.decode(fattr.getBgcolor());
				comp.setBackground(color);
			} catch (NumberFormatException n) {
			}
		}
		if (ClientUtils.isNotBlank(fattr.getFgcolor())) { //Set color
			try {
				java.awt.Color color = Color.decode(fattr.getFgcolor());
				comp.setForeground(color);
			} catch (NumberFormatException n) {
			}
		}
		if (fattr.getWidth() > 0 && fattr.getHeight() > 0) { //Set dimension
			comp.setSize(fattr.getWidth(), fattr.getHeight());
			comp.setPreferredSize(new java.awt.Dimension(fattr.getWidth(),
					fattr.getHeight()));
		}
	}
	
	protected static void setMainAttributes(AbstractView aview, JComponent comp, Cfield afield) {
		if (ClientUtils.isNotBlank(afield.getTooltip())) {
			comp.setToolTipText(afield.getTooltip());
		}
		if (afield.getDisabled() != null && afield.getDisabled().intValue() == 1) {
			comp.setEnabled(false);
		}
		if (afield.getPopupArray() != null &&
				afield.getPopupArray().length > 0) { //Register listener
			comp.addMouseListener(new PopUpListener(aview, afield.getPopupArray(),comp));
			/* Check if a reports needs to be generated */
		}
		
	}
	

	/**
	 *  
	 * @param field
	 * @param comp
	 * @return
	 */
	protected static boolean checkRequiredFields(final Cfield field, Component comp, String errormesg) {
		if (comp instanceof CoeusTextField || comp instanceof JTextField || comp instanceof DollarCurrencyTextField) {
			JTextField textField = (JTextField) comp;
			if (textField.getText().trim().equals("")) {
				CoeusOptionPane.showInfoDialog(errormesg);
				textField.requestFocusInWindow();
				return false;
			}
		}
		//Validate Combo box
		if (comp instanceof CoeusComboBox || comp instanceof JComboBox) {
			JComboBox comboField = (JComboBox) comp;
			if (comboField.getSelectedIndex() == 0) {
				CoeusOptionPane.showInfoDialog(errormesg);
				comboField.requestFocusInWindow();
				return false;
			}
		}
		return true;
	}	
	
	/**
	 * Keep recursing until you get the tab object
	 * @param jpane
	 * @return
	 */
	public static Object getTabObject(JScrollPane jpane) {
		Object uiform = jpane.getViewport().getView();
		if (uiform != null && uiform instanceof JScrollPane) 
			uiform = getTabObject((JScrollPane)uiform);
		return uiform;
	}	


}