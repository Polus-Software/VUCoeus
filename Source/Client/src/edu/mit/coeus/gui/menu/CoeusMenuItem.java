/*
 * @(#)CoeusMenuItem.java 1.0 07/24/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.gui.menu;

import javax.swing.JMenuItem;
import javax.swing.Icon;
import java.util.Vector;

/**
 * This class is used to construct CoeusMenuItem. It extends JMenuItem.
 * @author Geo
 * @version 1.0 07/24/02
 */
public class CoeusMenuItem extends JMenuItem{
    /* 
     * name of menu item
     */
    private String name;
    /*
     * icon for the menu item
     */
    private Icon iconName;
    /*
     * to set visible
     */
    private boolean mnuVisible;
    /*
     * to set enabled
     */
    private boolean mnuEnabled;

    /**
     * Creates CoeusMenuItem
     */
    public CoeusMenuItem(){
        super();
    }
    
    /**
     * Creates CoeusMenuItem
     *
     * @param name String name of the menu item
     * @param iconName Icon icon to be attached with the menu item
     * @param visible boolean to set visibile/invisible
     * @param enable boolean to set enabled/disabled
     */
    public CoeusMenuItem(String name,Icon iconName,boolean visible,boolean enable){
        super(name);
        this.name =name;
        this.iconName = iconName;
        this.mnuVisible = visible;
        this.mnuEnabled =enable;
    }

    /**
     * This Method is used to get the menu item name
     * @return  String menu item name.
     */    
    public String getName(){
        return name;
    }
    /**
     * This Method is used to get the menu item icon
     * @return  Icon menu Icon
     */    
    public Icon getIcon(){
        return iconName;
    }

    // setters
    
    /**
     * This Method is used to set the menu item name
     * @param name  menu item name
     */    
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * This Method is used to set the menu item icon
     * @param iconName  Icon
     */    
    public void setIcon(Icon iconName){
        this.iconName = iconName;
    }
    
    /**
     * This Method is used to set the menu item visiblity.
     * @param visible boolean value true if visible else false
     */    
    public void setVisible(boolean visible){
        this.mnuVisible = visible;
    }
    /**
     * This method is used to set the menu item enabled flag.
     * @param enable  true if enabled else false
     */    
    public void setEanable (boolean enable){
        this.mnuEnabled = enable;
    }
    
    /**
     * This method is used to check the menu item visibility.
     * @return  boolean true if visible else false
     */    
    public boolean isVisible(){
        return mnuVisible;
    }
    
    /**
     * This method is used to check the menu item enabled.
     * @return  boolean true if enabled else false
     */    
    public boolean isEnabled(){
        return mnuEnabled;
    }
}
