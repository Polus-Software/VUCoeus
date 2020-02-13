package edu.mit.coeus.utils;

import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.text.Document;
import edu.mit.coeus.gui.CoeusFontFactory;

/**
 * This class is used to coeus custom Text Field.
 * 
 * @author Ravi
 * @version 1.0
 */
public class CoeusTextField extends JTextField{
    
    private boolean editable;
  
    /**
     * This will construct the coeus Text Field
     */
    public CoeusTextField(){
        super(); 
        setFont(CoeusFontFactory.getNormalFont());
        editable = true;
    } 
    /**
     * This will construct the coeus Text Field with defult text
     * @param text default value
     */
    public CoeusTextField(String text){
        super(text); 
        setFont(CoeusFontFactory.getNormalFont());
    } 
    
    /**
     * This will construct the coeus Text Field with defult length
     * @param columns default length
     */
    public CoeusTextField(int columns){
        super(columns); 
        setFont(CoeusFontFactory.getNormalFont());
    } 
    /**
     * This will construct the coeus Text Field with defult text & length
     * @param text default value
     * @param columns default length
     */
    public CoeusTextField(String text,
                  int columns){
        super(text, columns); 
        setFont(CoeusFontFactory.getNormalFont());
    }
    
    /**
     * This will construct the coeus Text Field with defult document, text & length
     * @param doc Document for the this text field.
     * @param text default value
     * @param columns default length
     */
    public CoeusTextField(Document doc,
                  String text,
                  int columns){
        super(doc, text, columns); 
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
    		setBackground(Color.YELLOW);
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
     * This will Travers focusable flag for this component.
     * @return boolean Focus Traversal Falg.
     */
    //public boolean isFocusTraversable(){ //Coeusdev 800 - Commented so that texts could be selected to copy in display mode.
    //    return editable;
    //}
    
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
    
} // class CurrencyField
