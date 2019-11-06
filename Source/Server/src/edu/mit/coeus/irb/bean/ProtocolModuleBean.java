/*
 * ProtocolModuleBean.java
 *
 * Created on May 24, 2007, 5:45 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.irb.bean;

import edu.mit.coeus.bean.BaseBean;
import java.io.Serializable;

/**
 *
 * @author noorula
 */
public class ProtocolModuleBean implements BaseBean, Serializable{
    
    //holds protocol Amend/Renewal number
    private String protocolAmendRenewalNumber;
    //holds protocol number
    private String protocolNumber;
    //holds protocol module code
    private String protocolModuleCode;
    //holds Update user
    private String updateUser;
    //holds update Timestamp
    private java.sql.Timestamp updateTimestamp;    
    //holds Action type
    private String acType;
    // Added with CoeusQA2313: Completion of Questionnaire for Submission
    //holds the amendrenew sequence number
    private int protocolAmendRenewSequenceNumber;
    // CoeusQA2313: Completion of Questionnaire for Submission - end
    /** Creates a new instance of ProtocolModuleBean */
    public ProtocolModuleBean() {
    }

    public String getProtocolAmendRenewalNumber() {
        return protocolAmendRenewalNumber;
    }

    public void setProtocolAmendRenewalNumber(String protocolAmendRenewalNumber) {
        this.protocolAmendRenewalNumber = protocolAmendRenewalNumber;
    }

    public String getProtocolNumber() {
        return protocolNumber;
    }

    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }

    public String getProtocolModuleCode() {
        return protocolModuleCode;
    }

    public void setProtocolModuleCode(String protocolModuleCode) {
        this.protocolModuleCode = protocolModuleCode;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public String getAcType() {
        return acType;
    }

    public void setAcType(String acType) {
        this.acType = acType;
    }
    // Added with CoeusQA2313: Completion of Questionnaire for Submission
    /**
     * Getter for property protocolAmendRenewSequenceNumber
     */
    public int getProtocolAmendRenewSequenceNumber() {
        return protocolAmendRenewSequenceNumber;
    }
    
    /**
     * Setter for property protocolAmendRenewSequenceNumber
     */
    public void setProtocolAmendRenewSequenceNumber(int protocolAmendRenewSequenceNumber) {
        this.protocolAmendRenewSequenceNumber = protocolAmendRenewSequenceNumber;
    }
    // CoeusQA2313: Completion of Questionnaire for Submission - End

    
}
