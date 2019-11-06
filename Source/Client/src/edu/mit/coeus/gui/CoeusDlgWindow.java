/*
 * CoeusDlgWindow.java
 *
 * Created on September 21, 2002, 2:41 PM
 */

package edu.mit.coeus.gui;

import edu.mit.coeus.utils.CoeusGuiConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
/** <CODE>CoeusDlgWindow</CODE> is a custom dialog class which extends JDialog. This custom dialog component can be used throughout
 * the application wherever the response windows are required.
 * @author geo
 * @version 1.0
 */
public class CoeusDlgWindow  extends JDialog {
    
    private boolean changed;
    
    private Dimension screenSize;
    
    public static final int CENTER = 0;
    
    /** Creates new CoeusDlgWindow */
    public CoeusDlgWindow() {
        this((Frame)null, false);
        init();
    }
    
    /** Creates new CoeusDlgWindow with the given parent Component, title and specifies whether the dialog component is modal window or
     * not.
     *
     * @param owner reference to the parent Component.
     * @param title String which will be used to show in the title bar.
     * @param modal boolean value which specifies whether the dialog component is modal window or
     * not.
     */
    public CoeusDlgWindow(Component owner, String title, boolean modal) {
        if(owner==null){
            new CoeusDlgWindow((JFrame)null, title, modal);
        }else if(owner instanceof JFrame){
            new CoeusDlgWindow((JFrame)owner, title, modal);
        }else if(owner instanceof JDialog){
            new CoeusDlgWindow((JDialog)owner, title, modal);
        }else{
            new CoeusDlgWindow((JDialog)null, title, modal);
        }
    }
    
    /**
     * Creates new CoeusDlgWindow with the given parent frame
     *
     * @param owner reference to the parent Frame.
     */
    public CoeusDlgWindow(Frame owner) {
        this(owner, false);
        init();
    }
    
    /** Creates new CoeusDlgWindow with the given parent Frame and specifies whether the dialog component is modal window or
     * not.
     *
     * @param owner reference to the parent Frame.
     * @param modal boolean value which specifies whether the dialog is modal or
     * not.
     */
    public CoeusDlgWindow(Frame owner, boolean modal) {
        this(owner, null, modal);
        init();
    }
    
    /**
     * Creates new CoeusDlgWindow with the given parent Frame and title.
     *
     * @param owner reference to the parent Component.
     * @param title String which will be used to show in the title bar.
     */
    public CoeusDlgWindow(Frame owner, String title) {
        this(owner, title, false);
        init();
    }
    
    /** Creates new CoeusDlgWindow with the given parent Frame,title and specifies whether the dialog component is modal window or
     * not.
     *
     * @param owner reference to the parent Frame.
     * @param title String which will be used to show in the title bar.
     * @param modal boolean value which specifies whether the dialog is modal or
     * not.
     */
    public CoeusDlgWindow(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        init();
    }
    
    /**
     * Creates new CoeusDlgWindow with the given parent Dialog.
     *
     * @param owner reference to the parent Dialog.
     */
    public CoeusDlgWindow(Dialog owner) {
        this(owner, false);
        init();
    }
    
    /**
     * Creates new CoeusDlgWindow with the given parent Dialog.
     *
     * @param owner reference to the parent Dialog.
     * @param modal boolean value which specifies whether the dialog is modal or
     * not.
     */
    public CoeusDlgWindow(Dialog owner, boolean modal) {
        this(owner, null, modal);
        init();
    }
    
    /**
     * Creates new CoeusDlgWindow with the given parent Dialog and title.
     *
     * @param owner reference to the parent Dialog.
     * @param title String which will be used to show in the title bar.
     */
    public CoeusDlgWindow(Dialog owner, String title) {
        this(owner, title, false);
        init();
    }
    
    /** Creates new CoeusDlgWindow with the given parent Dialog, title and specifies whether the dialog component is modal window or
     * not.
     *
     * @param owner reference to the parent Dialog.
     * @param title String which will be used to show in the title bar.
     * @param modal boolean value which specifies whether the dialog is modal or
     * not.
     */
    public CoeusDlgWindow(Dialog owner, String title, boolean modal) {
        super(owner, title, modal);
        init();
    }
    
    /**
     * This method is used to set the changed flag.
     * @param changed boolean true if there are any changes else false.
     */
    public void setChanged(boolean changed){
        this.changed = changed;
    }
    
    /**
     * This method is used to get the changed flag.
     * @return changed boolean true if there are any changes else false.
     */
    public boolean isChanged(){
        return changed;
    }
    
    /**
     * returns the Desktop Screen Size .
     */
    private Dimension getScreenSize() {
        if(screenSize == null) screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return screenSize;
    }
    
    /** This methos sets the location of the Dialog.
     * @param width width of the Dialog.
     * @param height height of the dialog.
     * @param where Sets the Location.The following values are allowed:
     * <UL>
     * <LI> CENTER </LI>
     * </UL>
     */
    public void setLocation(int width, int height, int where) {
        setSize(width, height);
        setLocation(where);
    }
    
    /** This methos sets the location of the Dialog.
     * @param where Sets the Location.The following values are allowed:
     * <UL>
     * <LI> CENTER </LI>
     * </UL>
     */
    public void setLocation(int where) {
        //Check for where
        if(where != CENTER) return ;
        
        int screenWidth, screenHeight;
        screenWidth = (int)getScreenSize().getWidth();
        screenHeight = (int)getScreenSize().getHeight();
        
        switch (where) {
            case CENTER:
                setLocation((screenWidth-getWidth())/2, (screenHeight-getHeight())/2);
                break;
        }
    }
    
    //Any initialization do here
    private void init() {
        registerDefaultEscapeKeyListener();
    }
    
    public void addEscapeKeyListener(final Action escapeKeyListener) {
        getRootPane().getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW ).put(
        KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ),   "escPressed" );
        getRootPane().getActionMap().put("escPressed", escapeKeyListener );
    }
    
    private void registerDefaultEscapeKeyListener() {
        addEscapeKeyListener(
            new AbstractAction( "escPressed" )   {
                public void actionPerformed( ActionEvent actionEvent ){
                    // Code to be executed when 'Esc' is pressed should go here.
                        dispose();
                }
            });
    }
}