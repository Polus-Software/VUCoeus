/*
 * Link.java
 *
 * Created on February 7, 2006, 11:11 AM
 */

package edu.mit.coeus.gui;

import edu.mit.coeus.utils.CoeusOptionPane;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;

/**
 * displays a lablel which has properties as a web link
 * (i.e. displays the text with underline with hover properties and
 * opens a browser window with a specified URL when clicked)
 * @author sharathk
 */
public class Link extends JLabel implements MouseListener{
    
    private String url;
    
    private boolean hover = true;
    private boolean listeningHover = false;
    
    private Color inColor = Color.red;
    private Color outColor = Color.blue;
    
    private Cursor inCursor = new Cursor(Cursor.HAND_CURSOR);
    private Cursor outCursor = new Cursor(Cursor.DEFAULT_CURSOR);
    
    /**
     * Creates a new instance of Link
     */    
    public Link(){
        setHover(hover);
        setForeground(outColor);
    }
    
    /**
     * Creates a new instance of Link
     * @param text link text
     */    
    public Link(String text) {
        this();
        setText("<HTML><u>"+text+"</u></HTML>");
    }
    
    /**
     * sets the Text with underline
     * @param text link text
     */    
    public void setText(String text) {
        super.setText("<HTML><u>"+text+"</u></HTML>");
        setUrl(text);
    }
    
    /**
     * sets hover property
     * @param hover sets hover property
     */    
    public void setHover(boolean hover) {
        if(!listeningHover && hover) { 
            this.addMouseListener(this);
            listeningHover = true;
        }else if(listeningHover && !hover){
            this.removeMouseListener(this);
            listeningHover = false;
        }
        this.hover = hover;
    }
    
    /**
     * mouse clicked
     * @param mouseEvent mouse event
     */    
    public void mouseClicked(MouseEvent mouseEvent) {
        try{
            URLOpener.openUrl(url);            
        }catch (Exception exception) {
            exception.printStackTrace();
            CoeusOptionPane.showInfoDialog(exception.getMessage());
        }
    }

    public void mouseEntered(MouseEvent mouseEvent) {
        setForeground(inColor);
        setCursor(inCursor);
    }

    public void mouseExited(MouseEvent mouseEvent) {
        setForeground(outColor);
        setCursor(outCursor);
    }

    public void mousePressed(MouseEvent mouseEvent) {
    }

    public void mouseReleased(MouseEvent mouseEvent) {
    }

    /**
     * url displayed on the browser
     * @return url
     */    
    public String getUrl() {
        return url;
    }

    /**
     * url displayed on the browser
     * @param url url
     */    
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * returns hover property
     * @return hover
     */    
    public boolean isHover() {
        return hover;
    }
    
    /**
     * Getter for property inColor.
     * @return Value of property inColor.
     */
    public java.awt.Color getInColor() {
        return inColor;
    }
    
    /**
     * Setter for property inColor.
     * @param inColor New value of property inColor.
     */
    public void setInColor(java.awt.Color inColor) {
        this.inColor = inColor;
    }
    
    /**
     * Getter for property outColor.
     * @return Value of property outColor.
     */
    public java.awt.Color getOutColor() {
        return outColor;
    }
    
    /**
     * Setter for property outColor.
     * @param outColor New value of property outColor.
     */
    public void setOutColor(java.awt.Color outColor) {
        this.outColor = outColor;
    }
    
}
