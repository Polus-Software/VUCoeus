/*
 * InactiveItem.java
 * 
 * Gidget for help buttons
 * 
 * @version: 1.0
 * @author: Jill McAfee, Vanderbilt University Office of Research
 * @created: June 28, 2011
 */
package edu.vanderbilt.coeus.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.TypeConstants;

/**
 * This is a class for customizing table cell based on data values
 * 
 * @version 1.0 September 2, 2015
 * @author  Jill McAfee
 * @author  Vanderbilt University
 */
public class PersonTableCellRenderer extends DefaultTableCellRenderer  {
    
	public int statusColumn;
	public Object statusCondition = "I";
	public Color statusConditionalColor = Color.RED;
	private String statusValue;

	public int isExternalColumn;
	public Object isExternalCondition = "Y";
	public Color isExternalConditionalColor = Color.BLUE;
	private String isExternalValue;
	
	private Color conditionalColor;
	private boolean setForeground;
	private boolean setBackground;
	private boolean rowHighlighting;
	private char displayMode;
	
	private boolean conditionIsTrue;
		
    /**
     * Constructor
     */
    public PersonTableCellRenderer() {

    }
    
    /**
     * Alternate constructor
     * @param statusColumn : index of column containing status
     * @param isEmployeeColumn : index of column containing isEmployee flag
     * @parma setForeground : if true, use condition to set foreground (text) color
     * @param setBackground : if true, use condition to set background color
     * @param rowHighlighting : if true, use row highlighting
     * @param displayMode : if true, set to display mode
     */
    public PersonTableCellRenderer(int statusColumn, int isExternalColumn, 
    		boolean setForeground, boolean setBackground, boolean rowHighlighting, char displayMode) {
    	this.statusColumn = statusColumn;
    	this.isExternalColumn = isExternalColumn;
    	this.setForeground = setForeground;
    	this.setBackground = setBackground;
    	this.rowHighlighting = rowHighlighting;
    	this.displayMode = displayMode;
    }
    
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
	    boolean hasFocus, int row, int col) {  
 
    	Component renderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
    	
    	// Default colors generally used
    	Color defaultForeground = Color.BLACK;
    	Color defaultBackground = Color.WHITE;
    	Color selectedForeground = Color.YELLOW;
    	
    	// If row highlighting is being used and the row is selected, get the colors that the list is using
    	if (rowHighlighting && isSelected) {
    		defaultBackground = UIManager.getColor("List.selectionBackground");
    		defaultForeground = UIManager.getColor("List.selectionForeground");
    	}
    	
    	// If is display only mode, background should be grey
    	if (displayMode == TypeConstants.DISPLAY_MODE) {
            defaultBackground = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
    	}
    	
    	// Get the values
    	statusValue = (String) table.getValueAt(row, statusColumn);
	    isExternalValue = (String) table.getValueAt(row, isExternalColumn);
    	
	    // If there is no status, probably rolodex so assume Active
	    if (statusValue == null) {
	    	statusValue = "A";
	    }
	    
	    // IF there is no external flag, probably rolodex so assume "Is External"
	    if (isExternalValue == null) {
	    	isExternalValue = "Y";
	    }
	    
	    //System.out.println("Status = " + statusValue + " :: external = " + isExternalValue);
	    
	    // Determine what color is need for display
	    if (statusValue.equals(statusCondition)) {
	    	conditionalColor = statusConditionalColor;
	    	conditionIsTrue = true;
	    }
	    else if (isExternalValue.equals(isExternalCondition)) {
	    	conditionalColor = isExternalConditionalColor;
	    	conditionIsTrue = true;
	    }
	    else {
	    	conditionIsTrue = false;
	    }
    	
    	// If the the condition column value matches the condition, then format
	    if (conditionIsTrue) {
	    	// Set background
	    	if (setBackground) {
	    		renderer.setBackground((Color) conditionalColor);
	    	}
	    	else {
	    		renderer.setBackground(defaultBackground);
	    	}
	    	
	    	// Set foreground
	    	if (setForeground) {
	    		// Make the text show up better for selected with rowhighlighting
	    		if (isSelected && rowHighlighting) {
	    			renderer.setForeground(selectedForeground);
	    		}
	    		else {
	    			renderer.setForeground((Color) conditionalColor);
	    		}
	    	}
	    	else {
	    		renderer.setForeground(defaultForeground);
	    	}
	    }
	    // Reset default formatting if condition is false
	    else {
	    	renderer.setForeground(defaultForeground);
	    	renderer.setBackground(defaultBackground);
	    }
	    return this;
	}
}