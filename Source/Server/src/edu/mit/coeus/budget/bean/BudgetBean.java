/*
 * @(#)BudgetBean.java September 26, 2003, 12:58 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/*
 * PMD check performed, and commented unused imports and variables on 01-MAR-2011
 * by Maharaja Palanichamy
 */

package edu.mit.coeus.budget.bean;

import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.PrimaryKey;
import java.util.*;
import edu.mit.coeus.exception.CoeusException;

/**
 * The class is the base class of all Budget related classes
 *
 * @author  Prasanna Kumar
 * @version 1.0
 * Created on September 26, 2003, 12:58 PM
 */

public abstract class BudgetBean implements CoeusBean, java.io.Serializable {

    //holds 
    private String proposalNumber = null; 
    //holds 
    private int versionNumber = -1; 
    //holds update user id
    private String updateUser = null;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp = null;
    //holds account type
    private String acType = null;
    //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
    //holds unit number
    private String unitNumber = null;
    //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End
    
    /** Getter for property proposalNumber.
     * @return Value of property proposalNumber.
     */
    public String getProposalNumber() {
        return proposalNumber;
    }
    
    /** Setter for property proposalNumber.
     * @param proposalNumber New value of property proposalNumber.
     */
    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }    

    /** Getter for property versionNumber.
     * @return Value of property versionNumber.
     */
    public int getVersionNumber() {
        return versionNumber;
    }    
    
    /** Setter for property versionNumber.
     * @param versionNumber New value of property versionNumber.
     */
    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }
    
    /** Getter for property updateUser.
     * @return Value of property updateUser.
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /** Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }
    
    /** Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /** Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /** Getter for property acType.
     * @return Value of property acType.
     */
    public java.lang.String getAcType() {
        return acType;
    }
    
    /** Setter for property acType.
     * @param acType New value of property acType.
     */
    public void setAcType(java.lang.String acType) {
        this.acType = acType;
    } 
    
    //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
    /** Getter for property unitNumber.
     * @return Value of property unitNumber.
     */
    public String getUnitNumber() {
        return unitNumber;
    }
    
    /** Setter for property unitNumber.
     * @param unitNumber New value of property unitNumber.
     */
    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }
    //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End
    
    public boolean equals(CoeusBean coeusBean) throws CoeusException {
        return isLike(coeusBean);
     }
    
    /**
     * Overridden method of toString. It will form a string representation of
     * each element associated with this class.
     * @return Concatinated string representation of each element 
     */
    public String toString(){
        StringBuffer strBffr = new StringBuffer("");
        strBffr.append("Proposal Number =>"+proposalNumber);
        strBffr.append(";");
        strBffr.append("Version Name =>"+versionNumber);
        strBffr.append(";");        
        strBffr.append("Update User =>"+updateUser);
        strBffr.append(";");
        strBffr.append("Update Time Stamp =>"+updateTimestamp);
        strBffr.append(";");
        strBffr.append("AcType =>"+acType);
        strBffr.append(";");
        strBffr.append("Unit Number =>"+unitNumber);        
        return strBffr.toString();
    }
 } // end BudgetBean



