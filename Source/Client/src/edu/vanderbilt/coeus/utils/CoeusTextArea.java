/*
 * CoeusTextArea.java
 * 
 * @author:  Jill McAfee, Vanderbilt University Office of Research
 * @created: July 21, 2011
 * 
 * This class allows customization of the JTextArea element for Coeus
 */

package edu.vanderbilt.coeus.utils;

import javax.swing.JTextArea;
import java.awt.Color;
import edu.mit.coeus.gui.CoeusFontFactory;

/**
 * This class is used to coeus custom Text Field.
 * 
 * @author Ravi
 * @version 1.0
 */
public class CoeusTextArea extends JTextArea{
    
    private boolean editable;
  
    /**
     * This will construct the coeus Text Field
     */
    public CoeusTextArea(){
        super(); 
        setFont(CoeusFontFactory.getNormalFont());
        editable = true;
    } 
    /**
     * This will construct the coeus Text Field with default text
     * @param text default value
     */
    public CoeusTextArea(String text){
        super(text); 
        setFont(CoeusFontFactory.getNormalFont());
    } 
    
    /**
     * This will set the editable flag for this text field component.
     * @param value true if enable editing else false.
     */
    public void setEditable(boolean value) {        
        
        super.setEditable(value);
        editable = value;
        if (value == false){
            setOpaque(false);
        }else{
            setOpaque(true);
        }
    }
// JM 7-21-2011 set background color to yellow to indicate required element    
    /**
     * This will set the background color to yellow indicating a required element
     * @param boolean isRequired
     */
    public void setRequired(boolean isRequired) {
    	if (isRequired) {
    		setOpaque(true);
    		setBackground(Color.YELLOW);
    		super.paint(getGraphics());
    	}
    }
// END    
    /**
     * This will set the component edable flag for this text field componnet.
     * @param value true if enabled else false.
     */
    public void setEnabled(boolean value){
        super.setEnabled(value);
        setDisabledTextColor(Color.black);
    }
    
    /**
     * This will Traverse focusable flag for this component.
     * @return boolean Focus Traversal Flag.
     */
    public boolean isFocusTraversable(){
        return editable;
    }
    
    public void setText( String text) {
        super.setText(text);
        setCaretPosition(0);
    }

    protected void processFocusEvent(java.awt.event.FocusEvent e) {
        super.processFocusEvent(e);
        if (e.getID() == java.awt.event.FocusEvent.FOCUS_GAINED) {
            this.selectAll();
        }
    }     
    
}
