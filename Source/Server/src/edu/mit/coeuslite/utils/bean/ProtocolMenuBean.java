/*
 * ProtocolMenuBean.java
 *
 * Created on March 30, 2005, 3:47 PM
 */

package edu.mit.coeuslite.utils.bean;

import edu.mit.coeus.bean.CoeusBean;

/**
 *
 * @author  bijosht
 */
public class ProtocolMenuBean implements java.io.Serializable {
    
private String menuId;
private String menuName;
    /** Creates a new instance of ProtocolMenuBean */
    public ProtocolMenuBean() {
    }
    
    /**
     * Getter for property menuId.
     * @return Value of property menuId.
     */
    public java.lang.String getMenuId() {
        return menuId;
    }
    
    /**
     * Setter for property menuId.
     * @param menuId New value of property menuId.
     */
    public void setMenuId(java.lang.String menuId) {
        this.menuId = menuId;
    }
    
    /**
     * Getter for property menuName.
     * @return Value of property menuName.
     */
    public java.lang.String getMenuName() {
        return menuName;
    }
    
    /**
     * Setter for property menuName.
     * @param menuName New value of property menuName.
     */
    public void setMenuName(java.lang.String menuName) {
        this.menuName = menuName;
    }
    
}
