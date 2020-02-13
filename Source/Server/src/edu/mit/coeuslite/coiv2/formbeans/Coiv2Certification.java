/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.formbeans;

import java.sql.Timestamp;
import java.util.Date;
import org.apache.struts.action.ActionForm;

/**
 *
 * @author Mr
 */
public class Coiv2Certification extends ActionForm {

    private String disclosureNumber;
    private int sequenceNumber;
    private String certificationText;
    private Timestamp updateTimeStamp;
    private String updateTimeStampNew;

    public String getUpdateTimeStampNew() {
        return updateTimeStampNew;
    }

    public void setUpdateTimeStampNew(String updateTimeStampNew) {
        this.updateTimeStampNew = updateTimeStampNew;
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
     * @return the updateTimeStamp
     */
    public Timestamp getUpdateTimeStamp() {
        return updateTimeStamp;
    }

    /**
     * @param updateTimeStamp the updateTimeStamp to set
     */
    public void setUpdateTimeStamp(Timestamp updateTimeStamp) {
        this.updateTimeStamp = updateTimeStamp;
    }
    
}
