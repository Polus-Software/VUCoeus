/*
 * CoeusFontFactory.java
 *
 * Created on September 23, 2002, 11:08 AM
 */

package edu.mit.coeus.gui;

import java.awt.Font;

/**
 * This class consists of static methods which will give the fonts 
 * used in the application.
 * @author  geo
 * @version 1.0
 */
public class CoeusFontFactory extends java.awt.Component {

    private static Font defFont = new Font("Serif",Font.PLAIN,10);
    /** Creates new CoeusFontFactory */
    public CoeusFontFactory() {
    }
    
    /**
     * This method is used to get the font used for text fields, textareas etc.
     * @return reference to the Font with default font setting used in the 
     * application.
     */
    public static Font getNormalFont(){
        return new javax.swing.plaf.FontUIResource("MS Sans Serif",Font.PLAIN,11);
    }
    
    /**
     * This method is used to get the font used for labels.
     * @return reference to the Font with default font setting used in the 
     * application.
     */
    public static Font getLabelFont(){
        return new javax.swing.plaf.FontUIResource("Microsoft Sans Serif",Font.BOLD,11);
    }

}
