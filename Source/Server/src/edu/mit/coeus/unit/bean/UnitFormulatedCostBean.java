/*
 * UnitFormulatedCostBean.java
 *
 * Created on November 21, 2011, 3:05 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.unit.bean;

import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;
import java.sql.Timestamp;

/**
 *
 * @author satheeshkumarkn
 */
public class UnitFormulatedCostBean implements CoeusBean, java.io.Serializable{
    
    private int formulatedCode;
    private String formulatedCodeDescription;
    private String unitNumber;
    private double unitCost;
    private String updateUser;
    private Timestamp updateTimestamp;
    private String acType;
    private int awFormulatedCode;
    private String awUnitNumber;
    private Timestamp awUpdateTimestamp;
    private String awUpdateUser;
    
    /** Creates a new instance of UnitFormulatedCostBean */
    public UnitFormulatedCostBean() {
    }
    
    /**
     * Method to get the formulated code
     * @return formulatedCode - int
     */
    public int getFormulatedCode() {
        return formulatedCode;
    }
    
    /**
     * Method to set formulated code
     * @param formulatedCode - int
     */
    public void setFormulatedCode(int formulatedCode) {
        this.formulatedCode = formulatedCode;
    }
    
    /**
     * Method to get formulated code description
     * @return formulatedCodeDescription - String
     */
    public String getFormulatedCodeDescription() {
        return formulatedCodeDescription;
    }
    
    /**
     * Method to get formulated code description
     * @param formulatedCodeDescription - String
     */
    public void setFormulatedCodeDescription(String formulatedCodeDescription) {
        this.formulatedCodeDescription = formulatedCodeDescription;
    }
    
    /**
     * Method to get unit number
     * @return unitNumber - String
     */
    public String getUnitNumber() {
        return unitNumber;
    }
    
    /**
     * Method to set unit number
     * @param unitNumber - String
     */
    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }
    
    /**
     * Method to get unit cost
     * @return unitCost - double
     */
    public double getUnitCost() {
        return unitCost;
    }
    
    /**
     * Method to set unit cost
     * @param unitCost 
     */
    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }
    
    /**
     * Method to get update user
     * @return updateUser - String
     */
    public String getUpdateUser() {
        return updateUser;
    }
    
    /**
     * Method to set update user
     * @param updateUser - String
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    
    /**
     * Method to get update timestamp
     * @return updateTimestamp - Timestamp
     */
    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /**
     * Method to set updateTimestamp
     * @param updateTimestamp - Timestamp
     */
    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /**
     * Method to get acType
     * @return acType - String
     */
    public String getAcType() {
        return acType;
    }
    
    /**
     * Method to set acType
     * @param acType - String
     */
    public void setAcType(String acType) {
        this.acType = acType;
    }
    
    /**
     * Method will return true when comparable bean unit number and formualted code are equal to UnitFormulatedCostBean data
     * @param comparableBean 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @return success - boolean
     */
    public boolean isLike(ComparableBean comparableBean) throws CoeusException {
        boolean success = false;
        if(comparableBean instanceof UnitFormulatedCostBean){
            UnitFormulatedCostBean unitFormulatedCostBean = (UnitFormulatedCostBean)comparableBean;
            if(getUnitNumber().equals(unitFormulatedCostBean.getUnitNumber()) && getFormulatedCode() == unitFormulatedCostBean.getFormulatedCode()){
                success  = true;
            }
        }
        return success;
    }
    
    /**
     * Overridden method of toString. It will form a string representation of
     * each element associated with this class.
     * @return Concatinated string representation of each element
     */
    public String toString(){
        StringBuffer strBffr = new StringBuffer("");
        strBffr.append("Formulated Code =>"+getFormulatedCode());
        strBffr.append(";");
        strBffr.append("Formulated Description=>"+getFormulatedCodeDescription());
        strBffr.append(";");
        strBffr.append("Unit Number =>"+ getUnitNumber());
        strBffr.append(";");
        strBffr.append("Updater user=>"+getUpdateUser());
        strBffr.append(";");
        strBffr.append("Update time stamp=>"+getUpdateTimestamp());
        strBffr.append(";");
        strBffr.append("Action type=>"+getAcType());
        strBffr.append("\n");
        return strBffr.toString();
    }

    /**
     * Method to get aw formulated code
     * @return awFormulatedCode
     */
    public int getAwFormulatedCode() {
        return awFormulatedCode;
    }

    /**
     * Method to set awFormulatedCode
     * @param awFormulatedCode 
     */
    public void setAwFormulatedCode(int awFormulatedCode) {
        this.awFormulatedCode = awFormulatedCode;
    }

    /**
     * Method to get awUnitNumber
     * @return awUnitNumber
     */
    public String getAwUnitNumber() {
        return awUnitNumber;
    }

    /**
     * Method to set awUnitNumber
     * @param awUnitNumber 
     */
    public void setAwUnitNumber(String awUnitNumber) {
        this.awUnitNumber = awUnitNumber;
    }

    /**
     * Method to get awUpdateTimestamp
     * @return awUpdateTimestamp
     */
    public Timestamp getAwUpdateTimestamp() {
        return awUpdateTimestamp;
    }

    /**
     * Method to set awUpdateTimestamp
     * @param awUpdateTimestamp 
     */
    public void setAwUpdateTimestamp(Timestamp awUpdateTimestamp) {
        this.awUpdateTimestamp = awUpdateTimestamp;
    }

    /**
     * Method to get awUpdateUser
     * @return awUpdateUser
     */
    public String getAwUpdateUser() {
        return awUpdateUser;
    }

    /**
     * Method to set awUpdateUser
     * @param awUpdateUser 
     */
    public void setAwUpdateUser(String awUpdateUser) {
        this.awUpdateUser = awUpdateUser;
    }
    
}
