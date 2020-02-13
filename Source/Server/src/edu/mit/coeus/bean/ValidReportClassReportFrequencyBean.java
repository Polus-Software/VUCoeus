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
 * The class used to hold the information of <code>Report Class Report and Frequency</code>
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on June 03, 2004, 4:41 PM
 */

public class ValidReportClassReportFrequencyBean implements java.io.Serializable, BaseBean {

    //holds report class code
    private int reportClassCode;
    //holds report class description
    private String reportClassDescription;
    //holds reportCode
    private int reportCode;
    //holds report Description
    private String reportDescription;
    //holds frequency code
    private int frequencyCode;
    //holds frequency Description
    private String frequencyDescription;
    //holds update user id
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds Action type
    private String acType;
    
    /** Creates new FrequencyBean */
    public ValidReportClassReportFrequencyBean() {
    }
    
    /** Getter for property reportClassCode.
     * @return Value of property reportClassCode.
     *
     */
    public int getReportClassCode() {
        return reportClassCode;
    }
    
    /** Setter for property reportClassCode.
     * @param reportClassCode New value of property reportClassCode.
     *
     */
    public void setReportClassCode(int reportClassCode) {
        this.reportClassCode = reportClassCode;
    }
    
    /** Getter for property reportClassDescription.
     * @return Value of property reportClassDescription.
     *
     */
    public java.lang.String getReportClassDescription() {
        return reportClassDescription;
    }
    
    /** Setter for property reportClassDescription.
     * @param reportClassDescription New value of property reportClassDescription.
     *
     */
    public void setReportClassDescription(java.lang.String reportClassDescription) {
        this.reportClassDescription = reportClassDescription;
    }
    
    /** Getter for property reportCode.
     * @return Value of property reportCode.
     *
     */
    public int getReportCode() {
        return reportCode;
    }
    
    /** Setter for property reportCode.
     * @param reportCode New value of property reportCode.
     *
     */
    public void setReportCode(int reportCode) {
        this.reportCode = reportCode;
    }
    
    /** Getter for property reportDescription.
     * @return Value of property reportDescription.
     *
     */
    public java.lang.String getReportDescription() {
        return reportDescription;
    }
    
    /** Setter for property reportDescription.
     * @param reportDescription New value of property reportDescription.
     *
     */
    public void setReportDescription(java.lang.String reportDescription) {
        this.reportDescription = reportDescription;
    }
    
    /** Getter for property frequencyCode.
     * @return Value of property frequencyCode.
     *
     */
    public int getFrequencyCode() {
        return frequencyCode;
    }
    
    /** Setter for property frequencyCode.
     * @param frequencyCode New value of property frequencyCode.
     *
     */
    public void setFrequencyCode(int frequencyCode) {
        this.frequencyCode = frequencyCode;
    }
    
    /** Getter for property frequencyDescription.
     * @return Value of property frequencyDescription.
     *
     */
    public java.lang.String getFrequencyDescription() {
        return frequencyDescription;
    }
    
    /** Setter for property frequencyDescription.
     * @param frequencyDescription New value of property frequencyDescription.
     *
     */
    public void setFrequencyDescription(java.lang.String frequencyDescription) {
        this.frequencyDescription = frequencyDescription;
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