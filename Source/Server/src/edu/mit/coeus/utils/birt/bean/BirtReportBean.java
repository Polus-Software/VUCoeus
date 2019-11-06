/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeus.utils.birt.bean;

import edu.mit.coeus.bean.BaseBean;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author sharathk
 */
public class BirtReportBean implements Serializable, BaseBean{

    private int reportId;
    private String reportLabel;
    private String reportDescription;
    private int reportTypeCode;
    private String reportTypeDesc;
    private String right;
    private byte[] report;
    private String reportName;
    
    private Timestamp updateTimestamp;
    private Timestamp awUpdateTimestamp;
    private String updateUser;

    private Timestamp designUpdateTimestamp;
    private Timestamp awDesignUpdateTimestamp;
    private String designUpdateUser;

    private String acType;

    /**
     * @return the reportId
     */
    public int getReportId() {
        return reportId;
    }

    /**
     * @param reportId the reportId to set
     */
    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    /**
     * @return the reportLabel
     */
    public String getReportLabel() {
        return reportLabel;
    }

    /**
     * @param reportLabel the reportLabel to set
     */
    public void setReportLabel(String reportLabel) {
        this.reportLabel = reportLabel;
    }

    /**
     * @return the reportDescription
     */
    public String getReportDescription() {
        return reportDescription;
    }

    /**
     * @param reportDescription the reportDescription to set
     */
    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }

    /**
     * @return the reportTypeCode
     */
    public int getReportTypeCode() {
        return reportTypeCode;
    }

    /**
     * @param reportTypeCode the reportTypeCode to set
     */
    public void setReportTypeCode(int reportTypeCode) {
        this.reportTypeCode = reportTypeCode;
    }

    /**
     * @return the right
     */
    public String getRight() {
        return right;
    }

    /**
     * @param right the right to set
     */
    public void setRight(String right) {
        this.right = right;
    }

    /**
     * @return the report
     */
    public byte[] getReport() {
        return report;
    }

    /**
     * @param report the report to set
     */
    public void setReport(byte[] report) {
        this.report = report;
    }

    /**
     * @return the reportName
     */
    public String getReportName() {
        return reportName;
    }

    /**
     * @param reportName the reportName to set
     */
    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    /**
     * @return the updateTimestamp
     */
    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    /**
     * @param updateTimestamp the updateTimestamp to set
     */
    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    /**
     * @return the updateUser
     */
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * @param updateUser the updateUser to set
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * @return the designUpdateTimestamp
     */
    public Timestamp getDesignUpdateTimestamp() {
        return designUpdateTimestamp;
    }

    /**
     * @param designUpdateTimestamp the designUpdateTimestamp to set
     */
    public void setDesignUpdateTimestamp(Timestamp designUpdateTimestamp) {
        this.designUpdateTimestamp = designUpdateTimestamp;
    }

    /**
     * @return the designUpdateUser
     */
    public String getDesignUpdateUser() {
        return designUpdateUser;
    }

    /**
     * @param designUpdateUser the designUpdateUser to set
     */
    public void setDesignUpdateUser(String designUpdateUser) {
        this.designUpdateUser = designUpdateUser;
    }

    /**
     * @return the awUpdateTimestamp
     */
    public Timestamp getAwUpdateTimestamp() {
        return awUpdateTimestamp;
    }

    /**
     * @param awUpdateTimestamp the awUpdateTimestamp to set
     */
    public void setAwUpdateTimestamp(Timestamp awUpdateTimestamp) {
        this.awUpdateTimestamp = awUpdateTimestamp;
    }

    /**
     * @return the awDesignUpdateTimestamp
     */
    public Timestamp getAwDesignUpdateTimestamp() {
        return awDesignUpdateTimestamp;
    }

    /**
     * @param awDesignUpdateTimestamp the awDesignUpdateTimestamp to set
     */
    public void setAwDesignUpdateTimestamp(Timestamp awDesignUpdateTimestamp) {
        this.awDesignUpdateTimestamp = awDesignUpdateTimestamp;
    }

    /**
     * @return the acType
     */
    public String getAcType() {
        return acType;
    }

    /**
     * @param acType the acType to set
     */
    public void setAcType(String acType) {
        this.acType = acType;
    }

    /**
     * @return the reportTypeDesc
     */
    public String getReportTypeDesc() {
        return reportTypeDesc;
    }

    /**
     * @param reportTypeDesc the reportTypeDesc to set
     */
    public void setReportTypeDesc(String reportTypeDesc) {
        this.reportTypeDesc = reportTypeDesc;
    }

}
