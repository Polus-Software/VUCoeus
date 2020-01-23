/*
 * ArraReportBean.java
 *
 * Created on November 9, 2009, 12:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.arra.bean;

import java.sql.Date;
/**
 *
 * @author suganyadevipv
 */
public class ArraReportBean implements java.io.Serializable{
    
    private int arraReportNumber;
    private java.sql.Date reportPeriodStartDate;
    private java.sql.Date reportPeriodEndDate;
    private int arraReportStatusCode;
    private java.sql.Date loadDate;
    private java.sql.Date submissionDate;
    private java.sql.Date updateDate;
    private String updateUser;
    /** Creates a new instance of ArraReportBean */
    public ArraReportBean() {
    }
    public int getArraReportNumber() {
        return arraReportNumber;
    }
    public Date getReportPeriodStartDate() {
        return reportPeriodStartDate;
    }
    public Date getReportPeriodEndDate() {
        return reportPeriodEndDate;
    }
    public int getArraReportStatusCode() {
        return arraReportStatusCode;
    }
    public Date getLoadDate() {
        return loadDate;
    }
    public Date getSubmissionDate() {
        return submissionDate;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public String getUpdateUser() {
        return updateUser;
    }
    public void setArraReportNumber(int arraReportNumber) {
        this.arraReportNumber = arraReportNumber;
    }
    public void setReportPeriodStartDate(Date reportPeriodStartDate) {
        this.reportPeriodStartDate = reportPeriodStartDate;
    }
    public void setReportPeriodEndDate(Date reportPeriodEndDate) {
        this.reportPeriodEndDate = reportPeriodEndDate;
    }
    public void setArraReportStatusCode(int arraReportStatusCode) {
        this.arraReportStatusCode = arraReportStatusCode;
    }
    public void setLoadDate(Date loadDate) {
        this.loadDate = loadDate;
    }
    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    
    
    
}
