/*
 * DecoratorConstants.java
 *
 * Created on October 31, 2007, 12:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.utils.document.decorator;

import java.awt.Color;
import com.lowagie.text.pdf.BaseFont;
import java.util.HashMap;
import java.util.Map;

/**
 * constants used for decoration
 * @author sharathk
 */
public interface DecoratorConstants {
    
    //XML Constants - START
    public static final String DECORATIONS = "DECORATIONS";
    public static final String DECORATION = "DECORATION";
    public static final String GROUP = "GROUP";
    public static final String NAME = "NAME";
    public static final String STATUS = "STATUS";
    public static final String DOCTYPE = "DOCTYPE";
    public static final String HEADER = "HEADER";
    public static final String FOOTER = "FOOTER";
    public static final String WATERMARK = "WATERMARK";
    public static final String ALIGN = "ALIGN";
    public static final String TYPE = "TYPE";
    public static final String FONT = "FONT";
    public static final String FONT_COLOR= "FONT-COLOR";
    public static final String FONT_SIZE = "FONT-SIZE";
    //XML Constants - END
    
    public static final String ALIGN_RIGHT = "RIGHT";
    public static final String ALIGN_LEFT = "LEFT";
    public static final String ALIGN_CENTER = "CENTER";
    
    public static final int DEFAULT_FONT_SIZE = 10;
    public static final int DEFAULT_WATERMARK_FONT_SIZE = 50;
    
    public static final Color DEFAULT_COLOR = Color.BLACK;
    public static final Color DEFAULT_WATERMARK_COLOR = Color.LIGHT_GRAY;
    
    public static final String WATERMARK_TYPE_TEXT = "TEXT";
    
    public static final String WATERMARK_TYPE_IMAGE = "IMAGE";
    
    /**
     * Multiply this with Font Size to get the font width.
     * (i.e. the font width is 0.4 times the font size)
     * Remember this is only an average value. 
     * M or W will infact have more width than I.
     */
    public static final float CHAR_WIDTH_CONSTANT = 0.4f;
    
    public static final int MARGIN = 25;

    //Font Constants
    public static final String COURIER = BaseFont.COURIER;
    public static final String COURIER_BOLD = BaseFont.COURIER_BOLD;
    public static final String COURIER_OBLIQUE = BaseFont.COURIER_OBLIQUE;
    public static final String COURIER_BOLDOBLIQUE = BaseFont.COURIER_BOLDOBLIQUE;
    public static final String HELVETICA = BaseFont.HELVETICA;
    public static final String HELVETICA_BOLD = BaseFont.HELVETICA_BOLD;
    public static final String HELVETICA_OBLIQUE = BaseFont.HELVETICA_OBLIQUE;
    public static final String HELVETICA_BOLDOBLIQUE = BaseFont.HELVETICA_BOLDOBLIQUE;
    public static final String TIMES_ROMAN = BaseFont.TIMES_ROMAN;
    public static final String TIMES_BOLD = BaseFont.TIMES_BOLD;
    public static final String TIMES_ITALIC = BaseFont.TIMES_ITALIC;
    public static final String TIMES_BOLDITALIC = BaseFont.TIMES_BOLDITALIC;

    public static final Map<String, String> fontMap = new HashMap<String, String>(){
    {
        put("COURIER", COURIER);
        put("COURIER_BOLD", COURIER_BOLD);
        put("COURIER_OBLIQUE", COURIER_OBLIQUE);
        put("COURIER_BOLDOBLIQUE", COURIER_BOLDOBLIQUE);
        put("HELVETICA", HELVETICA);
        put("HELVETICA_BOLD", HELVETICA_BOLD);
        put("HELVETICA_OBLIQUE", HELVETICA_OBLIQUE);
        put("HELVETICA_BOLDOBLIQUE", HELVETICA_BOLDOBLIQUE);
        put("TIMES_ROMAN", TIMES_ROMAN);
        put("TIMES_BOLD", TIMES_BOLD);
        put("TIMES_ITALIC", TIMES_ITALIC);
        put("TIMES_BOLDITALIC", TIMES_BOLDITALIC);
    }
    };
}
