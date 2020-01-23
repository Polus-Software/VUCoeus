/*
 * PlaceHolderBean.java
 *
 * Created on April 19, 2005, 3:04 PM
 */

package edu.mit.coeuslite.utils.bean;

import edu.mit.coeus.bean.BaseBean;

/**
 *
 * @author  bijosht
 */
public class PlaceHolderBean implements BaseBean,java.io.Serializable{
    
    /** Creates a new instance of PlaceHolderBean */
    public PlaceHolderBean() {
    }
    
    private String basePage;
    private String placeHolder;
    private String includeFile;
    
    /**
     * Getter for property basePage.
     * @return Value of property basePage.
     */
    public java.lang.String getBasePage() {
        return basePage;
    }
    
    /**
     * Setter for property basePage.
     * @param basePage New value of property basePage.
     */
    public void setBasePage(java.lang.String basePage) {
        this.basePage = basePage;
    }
    
    /**
     * Getter for property placeHolder.
     * @return Value of property placeHolder.
     */
    public java.lang.String getPlaceHolder() {
        return placeHolder;
    }
    
    /**
     * Setter for property placeHolder.
     * @param placeHolder New value of property placeHolder.
     */
    public void setPlaceHolder(java.lang.String placeHolder) {
        this.placeHolder = placeHolder;
    }
    
    /**
     * Getter for property includeFile.
     * @return Value of property includeFile.
     */
    public java.lang.String getIncludeFile() {
        return includeFile;
    }
    
    /**
     * Setter for property includeFile.
     * @param includeFile New value of property includeFile.
     */
    public void setIncludeFile(java.lang.String includeFile) {
        this.includeFile = includeFile;
    }
    
   
    
}
