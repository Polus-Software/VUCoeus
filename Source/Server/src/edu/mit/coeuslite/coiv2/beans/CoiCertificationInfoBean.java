/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coiv2.beans;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Sony
 */
public class CoiCertificationInfoBean implements Serializable{

    public CoiCertificationInfoBean() {
    }

    private String coiDisclosureNumber;
    private Integer sequenceNumber;
    private String certificationText;
    private String certifiedBy;
    private Date certificationTimestamp;

    /**
     * @return the coiDisclosureNumber
     */
    public String getCoiDisclosureNumber() {
        return coiDisclosureNumber;
    }

    /**
     * @param coiDisclosureNumber the coiDisclosureNumber to set
     */
    public void setCoiDisclosureNumber(String coiDisclosureNumber) {
        this.coiDisclosureNumber = coiDisclosureNumber;
    }

    /**
     * @return the sequenceNumber
     */
    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * @param sequenceNumber the sequenceNumber to set
     */
    public void setSequenceNumber(Integer sequenceNumber) {
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
     * @return the certifiedBy
     */
    public String getCertifiedBy() {
        return certifiedBy;
    }

    /**
     * @param certifiedBy the certifiedBy to set
     */
    public void setCertifiedBy(String certifiedBy) {
        this.certifiedBy = certifiedBy;
    }

    /**
     * @return the certificationTimestamp
     */
    public Date getCertificationTimestamp() {
        return certificationTimestamp;
    }

    /**
     * @param certificationTimestamp the certificationTimestamp to set
     */
    public void setCertificationTimestamp(Date certificationTimestamp) {
        this.certificationTimestamp = certificationTimestamp;
    }

    

}
