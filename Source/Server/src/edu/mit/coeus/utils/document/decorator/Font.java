/*
 * Font.java
 *
 * Created on March 18, 2008, 5:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.utils.document.decorator;

import edu.mit.coeus.utils.UtilFactory;
import java.awt.Color;

import com.lowagie.text.pdf.BaseFont;
import java.lang.reflect.Field;
/**
 * Encapsulates font properties
 * 
 * @author sharathk
 */
public class Font {
    
    /**
     * fontName name
     */
    private String fontName;
    private Color color;
    private int size;
    
    /** Creates a new instance of Font */
    public Font() {
    }
    
    public Font(int size) {
        this.size = size;
    }
    
    public String getFont() {
        return fontName;
    }

    public void setFont(String font) {
        this.fontName = font;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(String strColor) {
        if (strColor == null) {
            color = DecoratorConstants.DEFAULT_COLOR;
            return ;
        }
        try {
            // get color by hex or octal value
            color = Color.decode(strColor);
        } catch (NumberFormatException nfe) {
            // if we can't decode lets try to get it by name
            try {
                // try to get a color by name using reflection
                // black is used for an instance and not for the color itselfs
                final Field f = Color.class.getField(strColor);
                color = (Color) f.get(null);
            } catch (Exception exception) {
                //UtilFactory.log(exception.getMessage() + "No such Color"+color, exception, "Font", "setColor");
                // if we can't get any color return default color
                color = DecoratorConstants.DEFAULT_COLOR;
            }
        }
        
    }
    
    public void setColor(Color color) {
        this.color = color;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    public BaseFont getBaseFont(){
        try {
            String font = BaseFont.HELVETICA;
            if(fontName == null || fontName.trim().equals("")){
                font = BaseFont.HELVETICA;
            }else{
                font = DecoratorConstants.fontMap.get(fontName);
                if(font == null){
                    font = BaseFont.HELVETICA;
                }
            }
            return BaseFont.createFont(font, BaseFont.WINANSI, BaseFont.EMBEDDED);
        }catch (Exception exception) {
            UtilFactory.log("Font Error:"+fontName);
            return null;
        }
    }
    
}
