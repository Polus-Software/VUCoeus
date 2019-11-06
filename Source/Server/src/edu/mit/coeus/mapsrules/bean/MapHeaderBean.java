/*
 * MapHeaderBean.java
 *
 * Created on October 14, 2005, 3:04 PM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.mapsrules.bean;

/**
 *
 * @author  vinayks
 */
public class MapHeaderBean extends MapBaseBean  {
    
    private String mapDescription;
    private String mapType;
    private boolean defaultMapFlag;
    private String unitNumber;
   
    /**
     * Getter for property mapType.
     * @return Value of property mapType.
     */
    public java.lang.String getMapType() {
        return mapType;
    }
    
    /**
     * Setter for property mapType.
     * @param mapType New value of property mapType.
     */
    public void setMapType(java.lang.String mapType) {
        this.mapType = mapType;
    }
    
    /**
     * Getter for property defaultMapFlag.
     * @return Value of property defaultMapFlag.
     */
    public boolean isDefaultMapFlag() {
        return defaultMapFlag;
    }
    
    /**
     * Setter for property defaultMapFlag.
     * @param defaultMapFlag New value of property defaultMapFlag.
     */
    public void setDefaultMapFlag(boolean defaultMapFlag) {
        this.defaultMapFlag = defaultMapFlag;
    }
    
    /**
     * Getter for property unitNumber.
     * @return Value of property unitNumber.
     */
    public java.lang.String getUnitNumber() {
        return unitNumber;
    }
    
    /**
     * Setter for property unitNumber.
     * @param unitNumber New value of property unitNumber.
     */
    public void setUnitNumber(java.lang.String unitNumber) {
        this.unitNumber = unitNumber;
    }
    
    /**
     * Getter for property mapDescription.
     * @return Value of property mapDescription.
     */
    public java.lang.String getMapDescription() {
        return mapDescription;
    }
    
    /**
     * Setter for property mapDescription.
     * @param mapDescription New value of property mapDescription.
     */
    public void setMapDescription(java.lang.String mapDescription) {
        this.mapDescription = mapDescription;
    }
    
}