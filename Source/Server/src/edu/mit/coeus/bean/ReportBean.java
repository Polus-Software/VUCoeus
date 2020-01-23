/*
 * @(#)ReportBean.java 1.0 June 03, 2004, 4:41 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.bean;

import java.beans.*;
import java.sql.Date;
import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.utils.ComboBoxBean;
/**
 * The class used to hold the information of <code>Report</code>
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on June 03, 2004, 4:41 PM
 */

public class ReportBean extends ComboBoxBean implements java.io.Serializable, BaseBean {

    //holds final report flag
    private boolean finalReportFlag;
    //holds update user id
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds Action type
    private String acType;

    /** Creates new FrequencyBean */
    public ReportBean() {
    }
    
    /** Getter for property finalReportFlag.
     * @return Value of property finalReportFlag.
     *
     */
    public boolean isFinalReportFlag() {
        return finalReportFlag;
    }    

    /** Setter for property finalReportFlag.
     * @param finalReportFlag New value of property finalReportFlag.
     *
     */
    public void setFinalReportFlag(boolean finalReportFlag) {
        this.finalReportFlag = finalReportFlag;
    }
    
    /** Getter for property updateUser.
     * @return Value of property updateUser.
     *
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /** Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     *
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }
    
    /** Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     *
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /** Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     *
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /** Getter for property acType.
     * @return Value of property acType.
     *
     */
    public java.lang.String getAcType() {
        return acType;
    }
    
    /** Setter for property acType.
     * @param acType New value of property acType.
     *
     */
    public void setAcType(java.lang.String acType) {
        this.acType = acType;
    }    
}