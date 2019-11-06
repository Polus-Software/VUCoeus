/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.beans;

import java.util.Date;

/**
 *
 * @author Mr
 */
public class Coiv2CertificationBean {

    private String disclosureNumber;
    private int sequenceNumber;
    private String certificationText;
    private String certificationBy;
    private Date certitifcationTimeStamp;
    private Integer reviewStatusCode;
    private String updateUser;

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    /**
     * @return the disclosureNumber
     */
    public String getDisclosureNumber() {
        return disclosureNumber;
    }

    /**
     * @param disclosureNumber the disclosureNumber to set
     */
    public void setDisclosureNumber(String disclosureNumber) {
        this.disclosureNumber = disclosureNumber;
    }

    /**
     * @return the sequenceNumber
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * @param sequenceNumber the sequenceNumber to set
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * @return the certificationText
     */
    public String getCertificationText() {
        return certificationText;
    }

    /**
     * @param certificationText the certificationText to set
     */
    public void setCertificationText(String certificationText) {
        this.certificationText = certificationText;
    }

    /**
     * @return the certificationBy
     */
    public String getCertificationBy() {
        return certificationBy;
    }

    /**
     * @param certificationBy the certificationBy to set
     */
    public void setCertificationBy(String certificationBy) {
        this.certificationBy = certificationBy;
    }

    /**
     * @return the certitifcationTimeStamp
     */
    public Date getCertitifcationTimeStamp() {
        return certitifcationTimeStamp;
    }

    /**
     * @param certitifcationTimeStamp the certitifcationTimeStamp to set
     */
    public void setCertitifcationTimeStamp(Date certitifcationTimeStamp) {
        this.certitifcationTimeStamp = certitifcationTimeStamp;
    }

    /**
     * @return the reviewStatusCode
     */
    public Integer getReviewStatusCode() {
        return reviewStatusCode;
    }

    /**
     * @param reviewStatusCode the reviewStatusCode to set
     */
    public void setReviewStatusCode(Integer reviewStatusCode) {
        this.reviewStatusCode = reviewStatusCode;
    }
    
}
