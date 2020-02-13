/* 
 * @(#)ProposalBiographyFormBean.java 1.0 03/12/03 5:17 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.bean;

import java.beans.*;
import java.sql.Timestamp;
import edu.mit.coeus.departmental.bean.DepartmentBioPersonFormBean;

/**
 * The class used to hold the information of <code>Proposal Biography</code>
 *
 * @author  Mukundan.C
 * @version 1.0
 * Created on March 12, 2003, 5:17 PM
 */
public class ProposalBiographyFormBean extends DepartmentBioPersonFormBean implements java.io.Serializable {
    
    // holds the proposal number
     private String proposalNumber;
     // holds the person id
     //private String personId;
     // holds the bio number
     //private int bioNumber;
     // holds the description
     //private String Description;
    //holds update user id
     //private String updateUser;
    //holds update timestamp
     //private Timestamp updateTimestamp;
    //holds account type
     //private String acType;
    //holds proposalPersonBioPDFBean
    private ProposalPersonBioPDFBean proposalPersonBioPDFBean;
    //holds proposalPersonBioSourceBean
    private ProposalPersonBioSourceBean proposalPersonBioSourceBean;
    // Enhancement to include person document type code and description
    private int documentTypeCode;
    private String documentTypeDescription;
   // Added for Proposal Hierarchy Enhancement Case# 3183 - Start
    private boolean parentProposal;
    // Added for Proposal Hierarchy Enhancement Case# 3183 - End
     
    public String getProposalNumber() {
        return proposalNumber;
    }

    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
    
    /** Getter for property proposalPersonBioPDFBean.
     * @return Value of property proposalPersonBioPDFBean.
     */
    public edu.mit.coeus.propdev.bean.ProposalPersonBioPDFBean getProposalPersonBioPDFBean() {
        return proposalPersonBioPDFBean;
    }
    
    /** Setter for property proposalPersonBioPDFBean.
     * @param proposalPersonBioPDFBean New value of property proposalPersonBioPDFBean.
     */
    public void setProposalPersonBioPDFBean(edu.mit.coeus.propdev.bean.ProposalPersonBioPDFBean proposalPersonBioPDFBean) {
        this.proposalPersonBioPDFBean = proposalPersonBioPDFBean;
    }
    
    /** Getter for property proposalPersonBioSourceBean.
     * @return Value of property proposalPersonBioSourceBean.
     */
    public edu.mit.coeus.propdev.bean.ProposalPersonBioSourceBean getProposalPersonBioSourceBean() {
        return proposalPersonBioSourceBean;
    }
    
    /** Setter for property proposalPersonBioSourceBean.
     * @param proposalPersonBioSourceBean New value of property proposalPersonBioSourceBean.
     */
    public void setProposalPersonBioSourceBean(edu.mit.coeus.propdev.bean.ProposalPersonBioSourceBean proposalPersonBioSourceBean) {
        this.proposalPersonBioSourceBean = proposalPersonBioSourceBean;
    }
    
    /**
     * Getter for property documentTypeCode.
     * @return Value of property documentTypeCode.
     */
    public int getDocumentTypeCode() {
        return documentTypeCode;
    }
    
    /**
     * Setter for property documentTypeCode.
     * @param documentTypeCode New value of property documentTypeCode.
     */
    public void setDocumentTypeCode(int documentTypeCode) {
        this.documentTypeCode = documentTypeCode;
    }
    
    /**
     * Getter for property documentTypeDescription.
     * @return Value of property documentTypeDescription.
     */
    public java.lang.String getDocumentTypeDescription() {
        return documentTypeDescription;
    }
    
    /**
     * Setter for property documentTypeDescription.
     * @param documentTypeDescription New value of property documentTypeDescription.
     */
    public void setDocumentTypeDescription(java.lang.String documentTypeDescription) {
        this.documentTypeDescription = documentTypeDescription;
    }
    
    /**
     * Getter for property parentProposal.
     * @return Value of property parentProposal.
     */
    public boolean isParentProposal() {
        return parentProposal;
    }
    
    /**
     * Setter for property parentProposal.
     * @param parentProposal New value of property parentProposal.
     */
    public void setParentProposal(boolean parentProposal) {
        this.parentProposal = parentProposal;
    }
    
    /*
    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }
    
    public int getBioNumber() {
        return bioNumber;
    }

    public void setBioNumber(int bioNumber) {
        this.bioNumber = bioNumber;
    }
    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }
    
    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    public String getAcType() {
        return acType;
    }

    public void setAcType(String acType) {
        this.acType = acType;
    }*/

}
