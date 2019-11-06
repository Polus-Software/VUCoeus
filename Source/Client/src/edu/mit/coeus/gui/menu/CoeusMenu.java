/*
 * @(#)CoeusAppletMDIForm.java 1.0 07/25/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.gui.menu;

import javax.swing.JMenu;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.Icon;
import java.util.Vector;

/**
 * This class is used to construct the customized Coeus Menu. It extends 
 * JMenu.
 * @version 1.0
 */
public class CoeusMenu extends JMenu{
    // menu name
    private String name;
    // icon to be displyed with the menu item
    private Icon iconName;
    // sub menu items
    private Vector children;
    // to set visible
    private boolean mnuVisible;
    // to set enabled
    private boolean mnuEnabled;

    /**
     * Construct the customized Coeus Menu.
     */
    public CoeusMenu(){
        super();
    }

    /**
     * Construct the customized Coeus Menu.
     *
     * @param name String name of the menu
     */
    public CoeusMenu(String name){
        super(name);
        this.name =name;
    }

    /** Construct the customized Coeus Menu.
     *
     * @param children collection of Menu Items
     * @param visible flag
     * @param enable flag
     * @param name String name of the menu
     * @param iconName Icon icon to be set with the menu item */
    public CoeusMenu(String name,Icon iconName,Vector children,boolean visible,boolean enable){
        super(name);
        this.name=name;
        this.iconName = iconName;
        this.children = children;
        this.mnuVisible = visible;
        this.mnuEnabled =enable;
        addMenuItems();
    }

    /**
     * This method is used to add menu items to the menu
     */
    public void addMenuItems(){
        if (children !=null) {
            for(int cnt = 0; cnt < children.size() ;cnt++) {
                //CoeusMenuItem temp = (CoeusMenuItem)children.get(cnt);                
                if (children.get(cnt) instanceof CoeusMenuItem){
                    this.add((CoeusMenuItem)children.get(cnt));
                } else if(children.get(cnt) instanceof CoeusMenu){
                    this.add((CoeusMenu)children.get(cnt));
                } else if(children.get(cnt) instanceof JCheckBoxMenuItem ){
                    this.add((JCheckBoxMenuItem)children.get(cnt));
                } else {
                    this.addSeparator();
                }
            }
        } 
    }

    /**
     * This Method is used to get the menu name
     * @return  String */    
    public String getName(){
        return this.name;
    }
    /**
     * This Method is used to set the menu name
     * @param name menu name.
     */    
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * This Method is used to get the menu Icon
     * @return  Icon menu icon.
     */    
    public Icon getIcon(){
        return this.iconName;
    }
    
    /**
     * This Method is used to set the menu Icon
     * @param iconName  Icon
     */    
    public void setIcon(Icon iconName){
        this.iconName = iconName;
    }

    /**
     * This method is used to get all the Children( Menu Items ) of this menu.
     * @return  Vector collection of menu items
     */    
    public Vector getChildren(){
        return this.children;
    }

    /**
     * This method is used to set the Childrens( Menu Items ) of this menu.
     * @param children  collection of Menu Items
     */    
    public void setChildren(Vector children){
        this.children = children;
        this.removeAll();
        addMenuItems();
    }

    /**
     * Check whether this menu is visible or not
     * @return  boolean true if visible else return false
     */    
    public boolean isVisible(){
        return this.mnuVisible;
    }

    /**
     * Sets the visible flag
     * @param visible true if visible else false
     */    
    public void setVisible(boolean visible){
        this.mnuVisible = visible;
    }

    /**
     * This method is used to set the enable flag.
     *
     * @param enable  True if enable else False
     */    
    public void setEnabled (boolean enable){
        this.mnuEnabled = enable;
    }

    /**
     * Check this menu is Enabled.
     * @return  boolean True if Enabled else False
     */    
    public boolean isEnabled(){
        return this.mnuEnabled;
    }
}
