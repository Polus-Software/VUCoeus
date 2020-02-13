/**
 * @(#)AwardApprovedEquipmentBean.java 1.0 01/03/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.bean;
import java.util.Vector;

/**
 * This class is used to hold Award Approved Equipment data
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on March 05, 2004 12:30 PM
 */

public class AwardApprovedEquipmentBean extends AwardBaseBean implements java.io.Serializable{
    
    private String item;
    private String vendor;
    private String model;
    private double amount;
    private int rowId;
    private String aw_Item;
    private String aw_Vendor;
    private String aw_Model;    
    
    /**
     *	Default Constructor
     */
    
    public AwardApprovedEquipmentBean(){
    }    

    /** Getter for property item.
     * @return Value of property item.
     */
    public java.lang.String getItem() {
        return item;
    }
    
    /** Setter for property item.
     * @param item New value of property item.
     */
    public void setItem(java.lang.String item) {
        this.item = item;
    }
    
    /** Getter for property vendor.
     * @return Value of property vendor.
     */
    public java.lang.String getVendor() {
        return vendor;
    }
    
    /** Setter for property vendor.
     * @param vendor New value of property vendor.
     */
    public void setVendor(java.lang.String vendor) {
        this.vendor = vendor;
    }
    
    /** Getter for property model.
     * @return Value of property model.
     */
    public java.lang.String getModel() {
        return model;
    }
    
    /** Setter for property model.
     * @param model New value of property model.
     */
    public void setModel(java.lang.String model) {
        this.model = model;
    }
    
    /** Getter for property amount.
     * @return Value of property amount.
     */
    public double getAmount() {
        return amount;
    }
    
    /** Setter for property amount.
     * @param amount New value of property amount.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }    
    
    /** Getter for property rowId.
     * @return Value of property rowId.
     *
     */
    public int getRowId() {
        return rowId;
    }
    
    /** Setter for property rowId.
     * @param rowId New value of property rowId.
     *
     */
    public void setRowId(int rowId) {
        this.rowId = rowId;
    }    
    
     /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        AwardApprovedEquipmentBean awardApprovedEquipmentBean = (AwardApprovedEquipmentBean)obj;
        if(super.equals(obj)){
            if(awardApprovedEquipmentBean.getRowId() == getRowId()){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    
    /** Getter for property aw_Item.
     * @return Value of property aw_Item.
     *
     */
    public java.lang.String getAw_Item() {
        return aw_Item;
    }
    
    /** Setter for property aw_Item.
     * @param aw_Item New value of property aw_Item.
     *
     */
    public void setAw_Item(java.lang.String aw_Item) {
        this.aw_Item = aw_Item;
    }
    
    /** Getter for property aw_Vendor.
     * @return Value of property aw_Vendor.
     *
     */
    public java.lang.String getAw_Vendor() {
        return aw_Vendor;
    }
    
    /** Setter for property aw_Vendor.
     * @param aw_Vendor New value of property aw_Vendor.
     *
     */
    public void setAw_Vendor(java.lang.String aw_Vendor) {
        this.aw_Vendor = aw_Vendor;
    }
    
    /** Getter for property aw_Model.
     * @return Value of property aw_Model.
     *
     */
    public java.lang.String getAw_Model() {
        return aw_Model;
    }
    
    /** Setter for property aw_Model.
     * @param aw_Model New value of property aw_Model.
     *
     */
    public void setAw_Model(java.lang.String aw_Model) {
        this.aw_Model = aw_Model;
    }
    
}