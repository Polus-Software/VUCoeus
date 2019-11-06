/*
 * SubHeaderBean.java
 *
 * Created on April 8, 2005, 3:19 PM
 */

package edu.mit.coeuslite.utils.bean;

import java.util.Vector;

/**
 *
 * @author  bijosht
 */
public class SubHeaderBean {
    private String subHeaderId;
    private String subHeaderName;
    private String subHeaderLink;
    private Vector menuItems;
    private String protocolSearchLink;
    private boolean selected;
    
    /** Creates a new instance of SubHeaderBean */
    public SubHeaderBean() {
    }
    
    /**
     * Getter for property subHeaderId.
     * @return Value of property subHeaderId.
     */
    public java.lang.String getSubHeaderId() {
        return subHeaderId;
    }
    
    /**
     * Setter for property subHeaderId.
     * @param subHeaderId New value of property subHeaderId.
     */
    public void setSubHeaderId(java.lang.String subHeaderId) {
        this.subHeaderId = subHeaderId;
    }
    
    /**
     * Getter for property subHeaderName.
     * @return Value of property subHeaderName.
     */
    public java.lang.String getSubHeaderName() {
        return subHeaderName;
    }
    
    /**
     * Setter for property subHeaderName.
     * @param subHeaderName New value of property subHeaderName.
     */
    public void setSubHeaderName(java.lang.String subHeaderName) {
        this.subHeaderName = subHeaderName;
    }
    
    /**
     * Getter for property menuItems.
     * @return Value of property menuItems.
     */
    public java.util.Vector getMenuItems() {
        return menuItems;
    }
    
    /**
     * Setter for property menuItems.
     * @param menuItems New value of property menuItems.
     */
    public void setMenuItems(java.util.Vector menuItems) {
        this.menuItems = menuItems;
    }
    
    /**
     * Getter for property subHeaderLink.
     * @return Value of property subHeaderLink.
     */
    public java.lang.String getSubHeaderLink() {
        return subHeaderLink;
    }
    
    /**
     * Setter for property subHeaderLink.
     * @param subHeaderLink New value of property subHeaderLink.
     */
    public void setSubHeaderLink(java.lang.String subHeaderLink) {
        this.subHeaderLink = subHeaderLink;
    }
    
    /**
     * Getter for property protocolSearchLink.
     * @return Value of property protocolSearchLink.
     */
    public java.lang.String getProtocolSearchLink() {
        return protocolSearchLink;
    }
    
    /**
     * Setter for property protocolSearchLink.
     * @param protocolSearchLink New value of property protocolSearchLink.
     */
    public void setProtocolSearchLink(java.lang.String protocolSearchLink) {
        this.protocolSearchLink = protocolSearchLink;
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
