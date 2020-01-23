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
public class CustomTableCellRenderer extends DefaultTableCellRenderer  {
    
	public int conditionalColumn;
	public Object condition;
	public Color conditionalColor;
	private String conditionValue;
	private boolean setForeground;
	private boolean setBackground;
	private boolean rowHighlighting;
	private char displayMode;
	private String EMPTY_STRING = "";
		
    /**
     * Constructor
     */
    public CustomTableCellRenderer() {

    }
    
    /**
     * Alternate constructor
     * @param conditionalColumn : index of column containing value to evaluate for condition
     * @param condition : condition value that should evaluate as true and triggered special formatting
     * @param conditionalColor : color that will be used to indicate true condition in target column
     * @parma setForeground : if true, use condition to set foreground (text) color
     * @param setBackground : if true, use condition to set background color
     */
    public CustomTableCellRenderer(int conditionalColumn, Object condition, Color conditionalColor,
    		boolean setForeground, boolean setBackground, boolean rowHighlighting, char displayMode) {
    	this.conditionalColumn = conditionalColumn;
    	this.condition = condition;
    	this.conditionalColor = conditionalColor;
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
    	
    	// Get the value from the column that drives the condition check; change nulls to EMPTY_STRING for comparison
	    conditionValue = (String) table.getValueAt(row, conditionalColumn);
	    if (conditionValue == null) {
	    	conditionValue = EMPTY_STRING;
	    }
	    
	    if (condition == null) {
	    	condition = EMPTY_STRING;
	    }

    	// If the the condition column value matches the condition, then format
	    if (conditionIsTrue(conditionValue)) {
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
    
    public boolean conditionIsTrue(Object value) {
    	if (value == null) {
    		if (condition == null) {
    			return true;
    		}
    		else {
    			return false;
    		}
    	}
    	else {
    		if (value.equals(condition)) {
    			return true;
    		}
	    	else {
	    		return false;
	    	}
    	}
    }

}