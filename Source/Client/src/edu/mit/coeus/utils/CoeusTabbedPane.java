/*
 * CoeusTabbedPane.java
 *
 * Created on February 10, 2004, 1:27 PM
 */

package edu.mit.coeus.utils;

import javax.swing.JTabbedPane;
import java.awt.Component;
import java.awt.KeyboardFocusManager;

import java.awt.event.*;
import javax.swing.*;

/**
 * CoeusTabbedPane is an extension of JTabbedPane which doesn't allow the user to change
 * to other tabs if any of the fields have InputVerfier and the data entered in the
 * field is not valid.
 *
 * @author  ravikanth
 */
public class CoeusTabbedPane extends JTabbedPane {
    
    /**
     * Input verifier TraversalStyle
     * doesn't allow the user to change to other tabs if any of the fields have 
     * InputVerfier and the data entered in the field is not valid.
     */
    public static final int INPUT_VERIFIER = 1;
    
    /**
     * Enables the Tabbed Pane Traversal using CTRL + T 
     */
    public static final int CTRL_T = 2;
    
    /** Traversal Style for Tabbed Pane
     * Defaults to INPUT_VERIFIER Type
     */
    private int traversalStyle = INPUT_VERIFIER; //Default
    
    public CoeusTabbedPane() {
        installCtrlT();
    }
    
    public CoeusTabbedPane(int traversalStyle){
        this.traversalStyle = traversalStyle;
        if(traversalStyle == CTRL_T) {
            installCtrlT();
        }else if(traversalStyle == INPUT_VERIFIER + CTRL_T) {
            installCtrlT();
        }
    }
    
    public void setSelectedIndex(int index) {
        if(traversalStyle == CTRL_T) {
            super.setSelectedIndex(index);
            return ;
        }
        
        Component comp = KeyboardFocusManager.
        getCurrentKeyboardFocusManager().getFocusOwner();
        
        // if no tabs are selected
        // -OR- the current focus owner is me
        // -OR- I request focus from another component and get it
        // then proceed with the tab switch
        
        if(getSelectedIndex()==-1 || comp==this || requestFocus(false)) {
            super.setSelectedIndex(index);
        }
    }
    
    public void installCtrlT() {
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_MASK),
        "CTRL+T");
        Action anAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int index = getSelectedIndex();
                if(index == getTabCount() -1) {
                    //Already at the end set to first tab
                    index = 0;
                }else {
                    index = index + 1;
                }
                setSelectedIndex(index);
                //select the tab so as to make the next Ctrl+T work
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        int index = getSelectedIndex();
                        getComponent(index).requestFocusInWindow();
                    }
                });
            }
        };
        this.getActionMap().put("CTRL+T", anAction);
        
    }
    
    /**
     * Getter for property traversalStyle.
     * @return Value of property traversalStyle.
     */
    public int getTraversalStyle() {
        return traversalStyle;
    }
    
}
