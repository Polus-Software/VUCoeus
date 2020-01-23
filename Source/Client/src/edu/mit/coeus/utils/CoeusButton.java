/*
 * @(#)CoeusButton.java 1.0 9/12/07
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.utils;

import edu.mit.coeus.gui.CoeusFontFactory;
import javax.swing.Action;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * <code> CoeusButton </code> is a Custom button for usage on all screens.
 * This component provide a JButton of standard size for consistency across
 * screens. However a specific screen can override the standard settings wherever
 * required.
 * @author noorula
 */
public class CoeusButton extends JButton{
    
    /** Creates a new instance of CoeusButton */
    
    /**
     * Creates a coeus button with no text or icon.
     */
    public CoeusButton() {
        super();
        initProperties();
    }
    
    /**
     * Creates a coeus button with an icon.
     *
     * @param icon  the Icon image to display on the button
     */
    public CoeusButton(Icon icon) {
        super(null, icon);
        initProperties();        
    }    
    
    /**
     * Creates a coeus button with text.
     *
     * @param text  the text of the button
     */
    public CoeusButton(String text) {
        super(text, null);
        initProperties();        
    }
    
    /**
     * Creates a coeus button where properties are taken from the 
     * <code>Action</code> supplied.
     *
     * @param a the <code>Action</code> used to specify the new button
     *
     */
    public CoeusButton(Action a) {
        super();
	setAction(a);
        initProperties();        
    }

    /**
     * Creates a coeus button with initial text and an icon.
     *
     * @param text  the text of the button
     * @param icon  the Icon image to display on the button
     */
    public CoeusButton(String text, Icon icon) {
        // Create the model
        setModel(new DefaultButtonModel());

        // initialize
        init(text, icon);
        initProperties();
    }
    
    /**
     * Set the size, margin and font for the button
     */
    private void initProperties(){
        setMargin(new java.awt.Insets(2, 5, 2, 5));
        setMaximumSize(new java.awt.Dimension(85, 23));
        setMinimumSize(new java.awt.Dimension(85, 23));
        setPreferredSize(new java.awt.Dimension(85, 23));
        setFont(CoeusFontFactory.getLabelFont());
    }
}
