/*
 * @(#)InstituteProposalInvestigatorBean.java 1.0 01/20/04 10:56 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.instprop.bean;

import java.beans.*;
import edu.mit.coeus.bean.InvestigatorUnitBean;
import edu.mit.coeus.bean.IBaseDataBean;
/**
 * The class used to hold the information of <code>Institute Proposal Lead Unit</code>
 *
 * @author  Prasanna Kumar K
 * @version 1.0
 * Created on January 20, 2004, 10:56 AM
 */

public class InstituteProposalUnitBean extends InvestigatorUnitBean implements IBaseDataBean{
    
    //holds the proposal number
    private String proposalNumber;
    //hoilds sequence number
    private int sequenceNumber;
    //holds osp Admin Person Id
    private String ospAdminPersonId;
    
    /** Creates new InstituteProposalUnitBean */
    public InstituteProposalUnitBean() {
    }    
    
    /** Creates new InstituteProposalUnitBean */
    public InstituteProposalUnitBean(InvestigatorUnitBean investigatorUnitBean) {
        super(investigatorUnitBean);
    }

    /** Getter for property proposalNumber.
     * @return Value of property proposalNumber.
     */
    public java.lang.String getProposalNumber() {
        return proposalNumber;
    }    
    
    /** Setter for property proposalNumber.
     * @param proposalNumber New value of property proposalNumber.
     */
    public void setProposalNumber(java.lang.String proposalNumber) {
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
        InstituteProposalUnitBean instituteProposalUnitBean = (InstituteProposalUnitBean)obj;
        if(instituteProposalUnitBean.getProposalNumber().equals(getProposalNumber()) &&
            //Donot check equality for sequence number - 8th April, 2004
            //instituteProposalUnitBean.getSequenceNumber() == getSequenceNumber() &&
            instituteProposalUnitBean.getUnitNumber().equals(getUnitNumber()) &&
            instituteProposalUnitBean.getPersonId().equals(getPersonId())){
                return true;
       }else{
            return false;
        }
    }    
    
    /** Getter for property ospAdminPersonId.
     * @return Value of property ospAdminPersonId.
     */
    public java.lang.String getOspAdminPersonId() {
        return ospAdminPersonId;
    }
    
    /** Setter for property ospAdminPersonId.
     * @param ospAdminPersonId New value of property ospAdminPersonId.
     */
    public void setOspAdminPersonId(java.lang.String ospAdminPersonId) {
        this.ospAdminPersonId = ospAdminPersonId;
    }    
}
