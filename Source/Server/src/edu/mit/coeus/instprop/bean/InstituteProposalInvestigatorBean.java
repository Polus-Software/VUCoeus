/*
 * @(#)InstituteProposalInvestigatorBean.java 1.0 01/20/04 10:56 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/*
 * PMD check performed, and commented unused imports and variables on 19-APR-2011
 * by Maharaja Palanichamy
 */
package edu.mit.coeus.instprop.bean;

import java.beans.*;
import edu.mit.coeus.bean.InvestigatorBean;
import edu.mit.coeus.bean.IBaseDataBean;
/**
 * The class used to hold the information of <code>Institute Proposal Investigator</code>
 *
 * @author  Prasanna Kumar K
 * @version 1.0
 * Created on January 20, 2004, 10:56 AM
 */

public class InstituteProposalInvestigatorBean extends InvestigatorBean implements IBaseDataBean {
    
     //holds the proposal number
     private String proposalNumber;
     //holds sequence number
     private int sequenceNumber;
     
     //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
     //holds the true value if person is both PI and Key person flag
     private boolean bothPIAndKeyPersonFlag;
     //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start

    public InstituteProposalInvestigatorBean() {         
     }
    
    /** Creates new InstituteProposalInvestigatorBean */
    public InstituteProposalInvestigatorBean(InvestigatorBean investigatorBean) {
        super(investigatorBean);
    }
    
    public String getProposalNumber() {
        return proposalNumber;
    }

    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
    
    /** Getter for property sequenceNumber.
     * @return Value of property sequenceNumber.
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }
    
    /** Setter for property sequenceNumber.
     * @param sequenceNumber New value of property sequenceNumber.
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    
     /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        InstituteProposalInvestigatorBean instituteProposalInvestigatorBean = (InstituteProposalInvestigatorBean)obj;
        //Donot check equality for sequence number - 8th April, 2004
        if(instituteProposalInvestigatorBean.getProposalNumber().equals(getProposalNumber()) &&
        //awardInvestigatorsBean.getSequenceNumber() == getSequenceNumber() &&
        instituteProposalInvestigatorBean.getPersonId().equals(getPersonId())){
            return true;
        }else{
            return false;
        }
    }
    
    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
    /** Getter for property bothPIAndKeyPersonFlag.
     * @return Value of property bothPIAndKeyPersonFlag.
     */
    public boolean isBothPIAndKeyPerson() {
        return bothPIAndKeyPersonFlag;
    }
    
    /** Setter for property bothPIAndKeyPersonFlag.
     * @param bothPIAndKeyPersonFlag property bothPIAndKeyPersonFlag.
     */
    public void setBothPIAndKeyPerson(boolean bothPIAndKeyPersonFlag) {
        this.bothPIAndKeyPersonFlag = bothPIAndKeyPersonFlag;
    }
    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
}