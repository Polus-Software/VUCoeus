/*
 * CoeusFocusManager.java
 * Created on December 5, 2002, 1:11 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.gui;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;


import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;

/**
 * This class provides the functionality to perform switching between the screens 
 * which are opened with CTRL+TAB combination.
 * 
 * @author  Sagin 
 * @version: 1.0 December 5, 2002, 1:11 PM
 */

// extending DefaultKeyboardFocusManager instead of DefaultFocusManager as it is 
// obsolete from jdk 1.4 onwards

public class CoeusFocusManager extends DefaultKeyboardFocusManager { //DefaultFocusManager {
    
    private CoeusAppletMDIForm coeusAppletMDIForm;
    
    /** 
     * Creates a new instance of CoeusFocusManager 
     * @param coeusAppletMDIForm reference to the CoeusAppletMDIForm.
     */
    public CoeusFocusManager(CoeusAppletMDIForm coeusAppletMDIForm) {
        this.coeusAppletMDIForm = coeusAppletMDIForm;
    }
    /**
     * This method is used to process the given key event for the given component.
     *
     * @param focusedComponent reference to the focused component.
     * @param anEvent KeyEvent which specifies the source for the event.
     */
    public void processKeyEvent(Component focusedComponent,KeyEvent anEvent){
        // Returning when you receive CTRL-TAB makes your components able to
        //control that key
        if ( (anEvent.getKeyCode() == KeyEvent.VK_TAB )
            && ((anEvent.getModifiers() & KeyEvent.CTRL_MASK) == KeyEvent.CTRL_MASK)
            && ( anEvent.getID() == anEvent.KEY_PRESSED ) ){
            try {
                if(coeusAppletMDIForm.getSelectedFrame() != null) {
                    if (!coeusAppletMDIForm.getSelectedFrame().hasFocus()) {
                        coeusAppletMDIForm.getSelectedFrame().requestFocus();
                    }
                    //Added for Bug Fix - CTRL + TAB - START
                    //coeusAppletMDIForm.getDeskTopPane().moveToBack(selectedFrame);
                    else if(coeusAppletMDIForm.getDeskTopPane().getAllFrames().length > 2){
                        JInternalFrame internalFrames[];
                        
                        /*internalFrames = coeusAppletMDIForm.getDeskTopPane().getAllFrames();
                        for(int index = 0; index < internalFrames.length; index++) {
                            System.out.println(internalFrames[index].getTitle());
                        }*/
                        JInternalFrame selectedFrame = coeusAppletMDIForm.getDeskTopPane().getSelectedFrame();
                        coeusAppletMDIForm.getDeskTopPane().moveToBack(selectedFrame);
                        internalFrames = coeusAppletMDIForm.getDeskTopPane().getAllFrames();
                        try{
                            internalFrames[1].setSelected(true);
                            internalFrames[1].requestFocus();
                        }catch (java.beans.PropertyVetoException propertyVetoException) {
                            propertyVetoException.printStackTrace();
                        }
                    }
                    //Added for Bug Fix - CTRL + TAB - END
                }else{
                    super.processKeyEvent(focusedComponent,anEvent);
                }
            } catch (Exception e) {
                e.printStackTrace();
                CoeusOptionPane.showErrorDialog(e.getMessage());
            }
            return;
        }else if ( anEvent.getKeyCode() == KeyEvent.VK_TAB && 
            ( (anEvent.getModifiers() & KeyEvent.SHIFT_MASK) == KeyEvent.SHIFT_MASK )
            && (anEvent.getID() == anEvent.KEY_PRESSED ) ){
               if( focusedComponent instanceof JTextArea ) {
                   anEvent.consume(); 
                   focusPreviousComponent();
                   return;
               }
                super.processKeyEvent(focusedComponent,anEvent);
                return;
                                    
        }else if ( ( focusedComponent instanceof JTextArea ) &&
                ( anEvent.getKeyCode() == KeyEvent.VK_TAB  )
                    && anEvent.getID() == anEvent.KEY_PRESSED ) {
                anEvent.consume();        
                focusNextComponent(); 
                return;
        }else{
            super.processKeyEvent(focusedComponent,anEvent);
        }
    }
    /* 
     * Overriding this method to dispatch the ESC event to the parent if the
     * ESC event source is table or tree. We will specify the event source as its
     * corresponding rootpane so that we can get the corresponding action to be
     * peformed while closing the dialog.
     */
    public boolean dispatchKeyEvent(KeyEvent e) {
        Object source = e.getSource(); 
        if ( ( source instanceof JTable || source instanceof JTree ) &&
             ( e.getKeyCode() == KeyEvent.VK_ESCAPE  )&& (e.getID() == e.KEY_PRESSED ) ) {        
                if( source instanceof JTable ) {
                    JTable table = (JTable)source;
                    ActionListener actionListener = table.getRootPane().getActionForKeyStroke(
                            KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ));
                    if( actionListener != null){
                        actionListener.actionPerformed(
                                new ActionEvent(table.getRootPane(),e.getID(),"escPressed"));
                    }
                }else if( source instanceof JTree ){
                    JTree tree = (JTree)source;
                    ActionListener actionListener = tree.getRootPane().getActionForKeyStroke(
                            KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ));
                    if( actionListener != null){
                        actionListener.actionPerformed(
                                new ActionEvent(tree.getRootPane(),e.getID(),"escPressed"));
                    }
                }
                return true;
                // Added by chandra 16-Aug-2004 to fix #1135
        }else if(e.getKeyCode() == KeyEvent.VK_ALT){
            try{
                UIManager.setLookAndFeel(UIManager.getLookAndFeel());
                javax.swing.SwingUtilities.updateComponentTreeUI(coeusAppletMDIForm.getJMenuBar());
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }// End Chandra - Bug Fix #1135 - 16-Aug-2004
        return super.dispatchKeyEvent(e);
    }
}
