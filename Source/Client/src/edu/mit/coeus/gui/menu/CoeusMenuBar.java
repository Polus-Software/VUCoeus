/*
 * @(#)CoeusMenuBar.java 1.0 07/25/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.gui.menu;

import javax.swing.*;
import java.util.Vector;
import java.awt.event.ActionListener;

/**
 * This class is used to construct CoeusMenuBar
 * @author Geo
 * @version 1.0 07/25/02
 */
public class CoeusMenuBar extends JMenuBar{
    /*
     * name of the menu bar
     */
    private String name;
    /*
     * menus to be stored
     */
    private Vector menus;
    
    /**
     * Creates Coeus menu bar
     */
    public  CoeusMenuBar() {
        super();
    }
    
    /**
     * Creates CoeusMenuBar
     *
     * @param menus Vector contains menus
     */
    public CoeusMenuBar(Vector menus){
        super();
        this.menus = menus;
        addMenus();
    }

    /**
     * This method is used to set new menu items.
     * @param allMenus  collection of menu items
     */    
    public void setMenus(Vector allMenus){
        menus = allMenus;
        addMenus();
    }

    /**
     * This method is used to get the existing menu items.
     * @return  Vector collection of menu item instances
     */    
    public Vector getMenus(){
        return menus;
    }

    /**
     * This method is used to add the menus to the menubar
     */
    public void addMenus(){
        for (int cntMenu=0 ;cntMenu < menus.size();cntMenu++){
            if (menus.get(cntMenu) instanceof JMenu){
                add((JMenu)menus.get(cntMenu));
            }
        }
    }
}
