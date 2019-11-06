/*
 * @(#)ProposalAdminFormBean.java 1.0 04/03/03 11:45 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.bean;

import java.beans.*;
import java.sql.Timestamp;
import java.util.Vector;

/** This class is used to hold the information of Proposal Admin Details module.
 *
 * @author  mukund
 * @version 1.0
 * Created on April 3, 2003, 11:45 AM
 */

public class ProposalAdminFormBean implements java.io.Serializable {
    
   private String devProposalNumber;
   private String instProposalNumber;
   private int instPropSeqNumber;
   private Timestamp dateSubmittedByDept;
   private Timestamp dateReturnedToDept;
   private Timestamp dateApprovedByOsp;
   private Timestamp dateSubmittedToAgency;
   private Timestamp createDate;
   private String createUser;
   private String signedBy;
   private char submissionType;
   private String signedUserName;
   private String createdUserName;
   private Vector invCertification;
    
    private PropertyChangeSupport propertySupport;
    
    /** Creates new ProposalAdminFormBean */
    public ProposalAdminFormBean() {
        propertySupport = new PropertyChangeSupport( this );
    }
    
    public String getDevProposalNumber() {
        return devProposalNumber;
    }

    public void setDevProposalNumber(String devProposalNumber) {
        this.devProposalNumber = devProposalNumber;
    }
    
    public String getInstProposalNumber() {
        return instProposalNumber;
    }

    public void setInstProposalNumber(String instProposalNumber) {
        this.instProposalNumber = instProposalNumber;
    }
    
    public int getInstPropSeqNumber() {
        return instPropSeqNumber;
    }

    public void setInstPropSeqNumber(int instPropSeqNumber) {
        this.instPropSeqNumber = instPropSeqNumber;
    }
    
    public Timestamp getDateSubmittedByDept() {
        return dateSubmittedByDept;
    }

    public void setDateSubmittedByDept(Timestamp dateSubmittedByDept) {
        this.dateSubmittedByDept = dateSubmittedByDept;
    }
    
    public Timestamp getDateReturnedToDept() {
        return dateReturnedToDept;
    }

    public void setDateReturnedToDept(Timestamp dateReturnedToDept) {
        this.dateReturnedToDept = dateReturnedToDept;
    }
    
    public Timestamp getDateApprovedByOsp() {
        return dateApprovedByOsp;
    }

    public void setDateApprovedByOsp(Timestamp dateApprovedByOsp) {
        this.dateApprovedByOsp = dateApprovedByOsp;
    }

    public Timestamp getDateSubmittedToAgency() {
        return dateSubmittedToAgency;
    }

    public void setDateSubmittedToAgency(Timestamp dateSubmittedToAgency) {
        this.dateSubmittedToAgency = dateSubmittedToAgency;
    }
    
    public Timestamp getCreatedDate() {
        return createDate;
    }

    public void setCreatedDate(Timestamp createDate) {
        this.createDate = createDate;
    }
 
    public String getCreatedUser() {
        return createUser;
    }

    public void setCreatedUser(String createUser) {
        this.createUser = createUser;
    }
    
    public String getSignedBy() {
        return signedBy;
    }

    public void setSignedBy(String signedBy) {
        this.signedBy = signedBy;
    }
    
    public char getSubmissionType() {
        return submissionType;
    }

    public void setSubmissionType(char submissionType) {
        this.submissionType = submissionType;
    }
    
    public String getSignedUserName() {
        return signedUserName;
    }

    public void setSignedUserName(String signedUserName) {
        this.signedUserName = signedUserName;
    }

    public String getCreatedUserName() {
        return createdUserName;
    }

    public void setCreatedUserName(String createdUserName) {
        this.createdUserName = createdUserName;
    }    
    
    public Vector getInvCertification() {
        return invCertification;
    }

    public void setInvCertification(Vector invCertification) {
        this.invCertification = invCertification;
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
}
