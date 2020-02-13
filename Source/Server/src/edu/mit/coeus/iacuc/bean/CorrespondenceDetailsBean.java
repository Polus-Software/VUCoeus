/*
 * CorrespondenceDetailsBean.java
 *
 * Created on November 26, 2003, 3:32 PM
 */

package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.iacuc.bean.*;
import java.io.Serializable;



public class CorrespondenceDetailsBean implements Serializable
{
    
    private String protocolNumber;
    
    private int sequenceNumber;
    
    private int submissionNumber ;
    
    private String description;
    
    private java.sql.Timestamp updateTimestamp;
    
    private int actionId ;
    
    private boolean valid ;
    
    private int protocolCorrespondenceTypeCode ;
    
    /** Creates a new instance of CorrespondenceDetailsBean */
    public CorrespondenceDetailsBean()
    {
    }
    
    /** Getter for property description.
     * @return Value of property description.
     */
    public java.lang.String getDescription()
    {
        return description;
    }
    
    /** Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(java.lang.String description)
    {
        this.description = description;
    }
    
    /** Getter for property protocolNumber.
     * @return Value of property protocolNumber.
     */
    public java.lang.String getProtocolNumber()
    {
        return protocolNumber;
    }
    
    /** Setter for property protocolNumber.
     * @param protocolNumber New value of property protocolNumber.
     */
    public void setProtocolNumber(java.lang.String protocolNumber)
    {
        this.protocolNumber = protocolNumber;
    }
    
    /** Getter for property sequenceNumber.
     * @return Value of property sequenceNumber.
     */
    public int getSequenceNumber()
    {
        return sequenceNumber;
    }
    
    /** Setter for property sequenceNumber.
     * @param sequenceNumber New value of property sequenceNumber.
     */
    public void setSequenceNumber(int sequenceNumber)
    {
        this.sequenceNumber = sequenceNumber;
    }
    
    /** Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     */
    public java.sql.Timestamp getUpdateTimestamp()
    {
        return updateTimestamp;
    }
    
    /** Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp)
    {
        this.updateTimestamp = updateTimestamp;
    }
 
    public int getActionId() 
    {
        return actionId ;
    }
    
    public void setActionId(int actionId) 
    {
        this.actionId = actionId ;
    }
    
    /** Getter for property valid.
     * @return Value of property valid.
     */
    public boolean isValid()
    {
        return valid;
    }
    
    /** Setter for property valid.
     * @param valid New value of property valid.
     */
    public void setValid(boolean valid)
    {
        this.valid = valid;
    }
    
    /** Getter for property protocolCorrespondenceTypeCode.
     * @return Value of property protocolCorrespondenceTypeCode.
     */
    public int getProtocolCorrespondenceTypeCode()
    {
        return protocolCorrespondenceTypeCode;
    }    
   
    /** Setter for property protocolCorrespondenceTypeCode.
     * @param protocolCorrespondenceTypeCode New value of property protocolCorrespondenceTypeCode.
     */
    public void setProtocolCorrespondenceTypeCode(int protocolCorrespondenceTypeCode)
    {
        this.protocolCorrespondenceTypeCode = protocolCorrespondenceTypeCode;
    }    
    
    /** Getter for property submissionNumber.
     * @return Value of property submissionNumber.
     *
     */
    public int getSubmissionNumber() {
        return submissionNumber;
    }
    
    /** Setter for property submissionNumber.
     * @param submissionNumber New value of property submissionNumber.
     *
     */
    public void setSubmissionNumber(int submissionNumber) {
        this.submissionNumber = submissionNumber;
    }
    
}
