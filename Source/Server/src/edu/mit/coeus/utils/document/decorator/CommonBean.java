/*
 * CommonBean.java
 *
 * Created on March 18, 2008, 5:09 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.utils.document.decorator;

/**
 * encapsulates all decorations for a specific decoration area like header, footer, watermark.
 * common properties required in header, footer and watermark like text, font,
 * alignment or type have been encapsulated in this bean.
 * the property 'type' can be used to define watwermark type(text/image) or
 * can also be used for alignment in header, footer.
 * @author sharathk
 */
public class CommonBean {
    
    /**
     * display text
     */
    private String text;
    /**
     * the property 'type' can be used to define watwermark type(text/image) or
     * can also be used for alignment in header, footer.
     */
    private String type;
    
    /**
     * encapsulates font properties for text
     */
    private Font font;
    
    /** Creates a new instance of CommonBean */
    public CommonBean() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }
    
}
