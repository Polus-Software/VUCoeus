/**
 * @(#)AwardInstPropDevPropBean.java 1.0 12/29/03
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.propdev.bean;

/**
 * This class is used to set and get the Award, Institute Proposal and Development Proposal Numbers
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on December 20, 2003 12:30 PM
 */

public class AwardInstPropDevPropBean implements java.io.Serializable{
    
    private String developmentProposalNumber;
    private String instituteProposalNumber;
    private int instituteSequenceNumber;
    private String negotiationNumber;   
    
    /**
     *	Default Constructor
     */
    
    public AwardInstPropDevPropBean(){
    }
    
    /** Getter for property developmentProposalNumber.
     * @return Value of property developmentProposalNumber.
     */
    public java.lang.String getDevelopmentProposalNumber() {
        return developmentProposalNumber;
    }    

    /** Setter for property developmentProposalNumber.
     * @param developmentProposalNumber New value of property developmentProposalNumber.
     */
    public void setDevelopmentProposalNumber(java.lang.String developmentProposalNumber) {
        this.developmentProposalNumber = developmentProposalNumber;
    }
    
    /** Getter for property instituteProposalNumber.
     * @return Value of property instituteProposalNumber.
     */
    public java.lang.String getInstituteProposalNumber() {
        return instituteProposalNumber;
    }
    
    /** Setter for property instituteProposalNumber.
     * @param instituteProposalNumber New value of property instituteProposalNumber.
     */
    public void setInstituteProposalNumber(java.lang.String instituteProposalNumber) {
        this.instituteProposalNumber = instituteProposalNumber;
    }
    
    /** Getter for property instituteSequenceNumber.
     * @return Value of property instituteSequenceNumber.
     */
    public int getInstituteSequenceNumber() {
        return instituteSequenceNumber;
    }
    
    /** Setter for property instituteSequenceNumber.
     * @param instituteSequenceNumber New value of property instituteSequenceNumber.
     */
    public void setInstituteSequenceNumber(int instituteSequenceNumber) {
        this.instituteSequenceNumber = instituteSequenceNumber;
    }
    
    /** Getter for property negotiationNumber.
     * @return Value of property negotiationNumber.
     */
    public java.lang.String getNegotiationNumber() {
        return negotiationNumber;
    }
    
    /** Setter for property negotiationNumber.
     * @param negotiationNumber New value of property negotiationNumber.
     */
    public void setNegotiationNumber(java.lang.String negotiationNumber) {
        this.negotiationNumber = negotiationNumber;
    }
    
}
