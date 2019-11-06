/*
 * CustomQueryBean.java
 *
 * Created on August 2, 2005, 11:19 AM
 */

package edu.mit.coeus.search.bean;

/**
 *
 * @author  nadhgj
 */
public class CustomQueryBean implements java.io.Serializable {
    
    private String displayName;
    private String query;
    
    /** Creates a new instance of CustomQueryBean */
    public CustomQueryBean() {
    }
    public CustomQueryBean(String displayName, String query) {
        this.displayName = displayName;
        this.query = query;
    }
    
    /**
     * Getter for property displayName.
     * @return Value of property displayName.
     */
    public java.lang.String getDisplayName() {
        return displayName;
    }
    
    /**
     * Setter for property displayName.
     * @param displayName New value of property displayName.
     */
    public void setDisplayName(java.lang.String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Getter for property query.
     * @return Value of property query.
     */
    public java.lang.String getQuery() {
        return query;
    }
    
    /**
     * Setter for property query.
     * @param query New value of property query.
     */
    public void setQuery(java.lang.String query) {
        this.query = query;
    }
    
}
