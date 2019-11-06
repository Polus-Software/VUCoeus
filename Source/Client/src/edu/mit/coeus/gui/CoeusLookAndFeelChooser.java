/*
 * @(#)CoeusLookAndFeelChooser.java 1.0 07/25/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.gui;

import java.util.Hashtable;
import java.util.Iterator;
import javax.swing.plaf.*;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import edu.mit.coeus.utils.*;
/**
 * This Class is used to find the Look and Feel selection for the Specific
 * Platform and it is a Wrapper Class.
 * The Coeus Application dynamically/runtime introspect the
 * System dependent look and feel and sets it. This class provides other
 * supporting method to get mac, windows, metal, crossplatform look and feel
 * Name.
 *
 * @author  subramanya
 * Created on November 18, 2002, 2:20 PM
 */
public class CoeusLookAndFeelChooser {

    private static String lafString = "";

    /** Creates a new instance of CoeusLookAndFeelChooser */
    private CoeusLookAndFeelChooser() {
    }

    /**
     * This Method is used to get the Java Cross Platform Look and Feel.
     * @return String getCrossPlatformLookAndFeel String value
     */
    public static String getCrossPlatformLookAndFeel(){
        try{
            lafString = UIManager.getCrossPlatformLookAndFeelClassName();
        }catch( Exception genErr ){
            CoeusOptionPane.showErrorDialog( genErr.getMessage() );
        }
        return lafString;
    }


    /**
     * This Method is used to set the Coeus Application Look and Feel.
     * @param lafStr UIManager register the application with this look and
     * feel string.
     */
    public static void setCoeusLookAndFeel( String lafStr ){
        try{
            UIManager.setLookAndFeel( lafStr );
        }catch (Exception e) {
            CoeusOptionPane.showErrorDialog( e.getMessage() );
        }
    }


    /**
     * This Method is used to set the Coeus Application Look and Feel.
     * @param userDefinedLaf UIManager register the application with this
     * LookAndFeel instance.
     */
    public static void setCoeusLookAndFeel( LookAndFeel userDefinedLaf ){
        try{
            UIManager.setLookAndFeel( userDefinedLaf );
        }catch (Exception e) {
            CoeusOptionPane.showErrorDialog( e.getMessage() );
        }
    }


    /**
     * This Method is used to get current system/OS look and feel.
     * @return LookAndFeel getSystemDependentLookAndFeel
     */
    public static LookAndFeel getSystemDependentLookAndFeel(){
        LookAndFeel systemLaf = null;
        try{
            systemLaf = UIManager.getLookAndFeel();
        }catch( Exception err ){

        }
        return systemLaf;
    }
}
