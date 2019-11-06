/* 
 * @(#)ProposalScienceCodeFormBean.java 1.0 03/12/03 4:40 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.bean;

import java.beans.*;
import java.sql.Timestamp;

/**
 * The class used to hold the information of <code>Proposal Science code</code>
 *
 * @author  Mukundan.C
 * @version 1.0
 * Created on March 12, 2003, 4:40 PM
 */

public class ProposalScienceCodeFormBean implements java.io.Serializable {
    
    //holds the proposal number
     private String proposalNumber;
     //holds the science code
     private String scienceCode;
     //holds the description
     private String Description;
    //holds update user id
     private String updateUser;
    //holds update timestamp
     private Timestamp updateTimestamp;
    //holds account type
     private String acType;

    public String getProposalNumber() {
        return proposalNumber;
    }

    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
    public String getScienceCode() {
        return scienceCode;
    }

    public void setScienceCode(String scienceCode) {
        this.scienceCode = scienceCode;
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
    }
}
