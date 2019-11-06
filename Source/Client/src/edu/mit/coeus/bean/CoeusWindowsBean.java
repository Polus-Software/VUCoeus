/*
 * @(#)CoeusAppletMDIForm.java     1.0 23/07/2002
 *
 * Copyright (c) Nous Infosystems, Pvt Ltd.
 * #1, 1st Cross, 1st Main, Koramangala, Bangalore, 560034, India.
 * All rights reserved.
 */
package edu.mit.coeus.bean;

import javax.swing.*;
import java.util.Vector;
import java.util.Hashtable;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusInternalFrame;

/**
 * This class represent the CoeusWindowsBean
 * @version 1.0
 */
public class CoeusWindowsBean {

    CoeusAppletMDIForm desktop;
    Hashtable  windows = new Hashtable();

    /**
     * Construct the CoeusWindowsBean
     */
    public CoeusWindowsBean(){
    }
    /**
     * Construct the CoeusWindowsBean
     * @param desktop CoeusAppletMDIForm
     */
    public CoeusWindowsBean(CoeusAppletMDIForm desktop){
        this.desktop = desktop;
    }
    /**
     * This method is used to add window frame
     * @param window CoeusAppletMDIForm
     * @param windowName string represent the window Name.
     */
    public void add(String windowName,CoeusInternalFrame window){
        windows.put(windowName,window);
    }
    /**
     * This method is used to get the Window.
     * @param windowName string represent the window Name.
     * @return CoeusInternalFrame internal frame
     */
    
    public CoeusInternalFrame getWindow(String windowName){
        return (CoeusInternalFrame) windows.get(windowName);
    }
    
    /**
     * This method is used to get the desktop form.
     * @return CoeusAppletMDIForm     
     */
    public CoeusAppletMDIForm getDesktop(){
        return desktop;
    }

}