/*
 * ColorBean.java
 *
 * Created on October 6, 2004, 10:46 AM
 */

/**
 * @author  chandra
 */
package edu.mit.coeus.utils.rtf;

import java.awt.Color;

public class ColorBean {
    private Color color;
    private String colorName;
    
    public ColorBean(){}
    
    public ColorBean(Color color, String colorName) {
        setColor(color);
        setColorName(colorName);
    }
    
    /**
     * Getter for property color.
     * @return Value of property color.
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Setter for property color.
     * @param color New value of property color.
     */
    public void setColor(Color color) {
        this.color = color;
    }
    
    /**
     * Getter for property colorName.
     * @return Value of property colorName.
     */
    public String getColorName() {
        return colorName;
    }
    
    /**
     * Setter for property colorName.
     * @param colorName New value of property colorName.
     */
    public void setColorName(String colorName) {
        this.colorName = colorName;
    }
    
    // Equal check for the selected color
    public boolean equals(Object obj) {
        if(obj instanceof ColorBean) {
            ColorBean colorBean = (ColorBean)obj;
            if(color!=null) {
                return getColor().equals(colorBean.getColor());
            }
        }
         return super.equals(obj);
    }
    
}
