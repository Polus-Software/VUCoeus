/*
 * AdhocDetailsBean.java
 *
 * Created on December 19, 2003, 3:27 PM
 */

package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.iacuc.bean.*;


public class AdhocDetailsBean implements java.io.Serializable
{
    private String formId ;
    private String description ;
    private char module ;
    private String protocolNumber ;
    private String scheduleId ;
    private String committeeId ;
    private int sequenceNumber ;
    private int submissionNumber ;
    
    /** Creates a new instance of AdhocDetailsBean */
    public AdhocDetailsBean() 
    {
        
    }
    
    /** Getter for property formId.
     * @return Value of property formId.
     *
     */
    public java.lang.String getFormId() {
        return formId;
    }
    
    /** Setter for property formId.
     * @param formId New value of property formId.
     *
     */
    public void setFormId(java.lang.String formId) {
        this.formId = formId;
    }
    
    /** Getter for property description.
     * @return Value of property description.
     *
     */
    public java.lang.String getDescription() {
        return description;
    }
    
    /** Setter for property description.
     * @param description New value of property description.
     *
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
    /** Getter for property module.
     * @return Value of property module.
     *
     */
    public char getModule() {
        return module;
    }
    
    /** Setter for property module.
     * @param module New value of property module.
     *
     */
    public void setModule(char module) {
        this.module = module;
    }
    
    /** Getter for property protocolNumber.
     * @return Value of property protocolNumber.
     *
     */
    public java.lang.String getProtocolNumber() {
        return protocolNumber;
    }
    
    /** Setter for property protocolNumber.
     * @param protocolNumber New value of property protocolNumber.
     *
     */
    public void setProtocolNumber(java.lang.String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }
    
    /** Getter for property scheduleId.
     * @return Value of property scheduleId.
     *
     */
    public java.lang.String getScheduleId() {
        return scheduleId;
    }
    
    /** Setter for property scheduleId.
     * @param scheduleId New value of property scheduleId.
     *
     */
    public void setScheduleId(java.lang.String scheduleId) {
        this.scheduleId = scheduleId;
    }
    
    /** Getter for property committeeId.
     * @return Value of property committeeId.
     *
     */
    public java.lang.String getCommitteeId() {
        return committeeId;
    }
    
    /** Setter for property committeeId.
     * @param committeeId New value of property committeeId.
     *
     */
    public void setCommitteeId(java.lang.String committeeId) {
        this.committeeId = committeeId;
    }
    
    /** Getter for property sequenceNumber.
     * @return Value of property sequenceNumber.
     *
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }
    
    /** Setter for property sequenceNumber.
     * @param sequenceNumber New value of property sequenceNumber.
     *
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
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
