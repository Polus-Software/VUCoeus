/*
 * @(#)CoeusPropertiesBean.java 1.0 7/23/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.bean;

import javax.swing.*;
import java.awt.*;

/**
 * This class is used to set the Lookand Feel , background / foreground colors. 
 * It contains set of 
 * setter and getter functions for background and foreground colors 
 * for MDI form and Login Applet .
 * 
 * @version :1.0 July 23,2002
 * @author Guptha K
 */

public class CoeusPropertiesBean {
    String lookAndFeelClass= "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
    Color backgroundColor = Color.lightGray;
    Color foregroundColor = Color.white;
    Color foregroundColor1 = Color.blue;
    
    Font font = new Font("Ariel",Font.ITALIC,20);
    
    /**
     * setLookAndFeelClass gives an WindowsLookAnFeel for the applet and MDI form.
     *
     * @param lookAndFeelClass String
     */
    public void setLookAndFeelClass(String lookAndFeelClass){
        this.lookAndFeelClass = lookAndFeelClass;
    }
    
    /**
     * setBackgroundColor sets lightgrey as the foreground color for the applet and MDI form.
     *
     * @param backgroundColor Color
     */
    public void setBackgroundColor(Color backgroundColor){
        this.backgroundColor = backgroundColor;
    }
    
    /**
     * setForegroundColor sets white as the foreground color for the applet and MDI form.
     *
     * @param foregroundColor Color
     */
    public void setForegroundColor(Color foregroundColor){
        this.foregroundColor = foregroundColor;
    }
    
    /**
     * setForegroundColor1 sets blue as the foreground color for the applet and MDI form.
     *
     * @param foregroundColor1 Color
     */
    public void setForegroundColor1(Color foregroundColor1){
        this.foregroundColor1 = foregroundColor1;
    }
    
    /**
     * setFont gives Areial with italic 20.
     *
     * @param font Font
     */
    public void setFont(Font font){
        this.font = font;
    }
    
    /**
     * getLookAndFeelClass returns the class with WindowslookAndFeel for the applet and MDI form.
     *
     * @return String lookAndFeelClass
     */
    public String getLookAndFeelClass(){
        return lookAndFeelClass;
    }
    
    /**
     * getBackgroundColor returns lightgrey as the backgroundcolor for the applet and MDI form.
     *
     * @return Color background Color
     */
    public Color getBackgroundColor(){
        return backgroundColor;
    }
    
    /**
     * getForegroundColor returns white as the foregroundColor for the applet and MDI form.
     *
     * @return Color foreground Color
     */
    public Color getForegroundColor(){
        return foregroundColor;
    }
    
    /**
     * getForegroundColor1 returns blue as the foregroundColor for the applet and MDI form.
     *
     * @return Color foreground Color1
     */
     public Color getForegroundColor1(){
        return foregroundColor1;
    }
    
    /**
     * getFont returns font with size 20 areial italic.
     *
     * @return Font font
     */
    public Font getFont(){
        return font;
    }
}

