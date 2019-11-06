/*
 * CoeusTypeBean.java
 *
 * Created on November 25, 2011, 11:11 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.bean;

import edu.mit.coeus.exception.CoeusException;
import java.sql.Timestamp;

/**
 *
 * @author satheeshkumarkn
 */
public class CoeusTypeBean implements java.io.Serializable, BaseBean {
    
    private int typeCode;
    private String typeDescription;
    private String updateUser;
    private Timestamp updateTimestamp;

    /** Creates a new instance of CoeusTypeBean */
    public CoeusTypeBean() {
    }

    /**
     * Method to get the type code
     * @return typeCode
     */
    public int getTypeCode() {
        return typeCode;
    }

    /**
     * Method to set the type code
     * @param typeCode 
     */
    public void setTypeCode(int typeCode) {
        this.typeCode = typeCode;
    }

    /**
     * Method to get the type description
     * @return typeDescription
     */
    public String getTypeDescription() {
        return typeDescription;
    }

    /**
     * Method to set the type description
     * @param typeDescription 
     */
    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    /**
     * Method will return true when comparable bean unit number and formualted code are equal to UnitFormulatedCostBean data
     * @param comparableBean
     * @throws edu.mit.coeus.exception.CoeusException
     * @return success - boolean
     */
    public boolean isLike(ComparableBean comparableBean) throws CoeusException {
        boolean success = false;
        if(comparableBean instanceof CoeusTypeBean){
            CoeusTypeBean coeusTypeBean = (CoeusTypeBean)comparableBean;
            if(getTypeCode() == coeusTypeBean.getTypeCode()){
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
        strBffr.append("Type Code =>"+getTypeCode());
        strBffr.append(";");
        strBffr.append("Type Description=>"+getTypeDescription());
        strBffr.append(";");
        strBffr.append("Updater user=>"+getUpdateUser());
        strBffr.append(";");
        strBffr.append("Update time stamp=>"+getUpdateTimestamp());
        strBffr.append("\n");
        return strBffr.toString();
    }

    /**
     * Method to get the update user
     * @return updateUser
     */
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * Method to set the udpate user
     * @param updateUser 
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * Method to get the udpate time stamp
     * @return updateTimestamp
     */
    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    /**
     * Method to set the udpate time stamp
     * @param updateTimestamp 
     */
    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
}
