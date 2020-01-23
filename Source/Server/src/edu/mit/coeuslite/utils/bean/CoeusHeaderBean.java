/*
 * CoeusHeaderBean.java
 * * Created on April 8, 2005, 3:13 PM
 */

package edu.mit.coeuslite.utils.bean;

import java.util.Vector;

/**
 *
 * @author  bijosht
 */
public class CoeusHeaderBean {
    private String headerId;
    private String headerName;
    private String imagePath;
    private String link;
    private String onLoadImage;
    private Vector subHeaders;
    private String type;
    private boolean selected;
    /** Creates a new instance of CoeusHeaderBean */
    public CoeusHeaderBean() {
    }
    
    /**
     * Getter for property headerId.
     * @return Value of property headerId.
     */
    public java.lang.String getHeaderId() {
        return headerId;
    }
    
    /**
     * Setter for property headerId.
     * @param headerId New value of property headerId.
     */
    public void setHeaderId(java.lang.String headerId) {
        this.headerId = headerId;
    }
    
    /**
     * Getter for property headerName.
     * @return Value of property headerName.
     */
    public java.lang.String getHeaderName() {
        return headerName;
    }
    
    /**
     * Setter for property headerName.
     * @param headerName New value of property headerName.
     */
    public void setHeaderName(java.lang.String headerName) {
        this.headerName = headerName;
    }
    
    /**
     * Getter for property imagePath.
     * @return Value of property imagePath.
     */
    public java.lang.String getImagePath() {
        return imagePath;
    }
    
    /**
     * Setter for property imagePath.
     * @param imagePath New value of property imagePath.
     */
    public void setImagePath(java.lang.String imagePath) {
        this.imagePath = imagePath;
    }
    
    /**
     * Getter for property subHeaders.
     * @return Value of property subHeaders.
     */
    public Vector getSubHeaders() {
        return subHeaders;
    }
    
    /**
     * Setter for property subHeaders.
     * @param subHeaders New value of property subHeaders.
     */
    public void setSubHeaders(Vector subHeaders) {
        this.subHeaders = subHeaders;
    }
    
    /**
     * Getter for property link.
     * @return Value of property link.
     */
    public java.lang.String getLink() {
        return link;
    }
    
    /**
     * Setter for property link.
     * @param link New value of property link.
     */
    public void setLink(java.lang.String link) {
        this.link = link;
    }
    
    /**
     * Getter for property onLoadImage.
     * @return Value of property onLoadImage.
     */
    public java.lang.String getOnLoadImage() {
        return onLoadImage;
    }
    
    /**
     * Setter for property onLoadImage.
     * @param onLoadImage New value of property onLoadImage.
     */
    public void setOnLoadImage(java.lang.String onLoadImage) {
        this.onLoadImage = onLoadImage;
    }
    
    /**
     * Getter for property type.
     * @return Value of property type.
     */
    public java.lang.String getType() {
        return type;
    }
    
    /**
     * Setter for property type.
     * @param type New value of property type.
     */
    public void setType(java.lang.String type) {
        this.type = type;
    }
    
    /**
     * Getter for property selected.
     * @return Value of property selected.
     */
    public boolean isSelected() {
        return selected;
    }
    
    /**
     * Setter for property selected.
     * @param selected New value of property selected.
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
}
