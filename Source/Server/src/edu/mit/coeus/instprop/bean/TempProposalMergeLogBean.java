/*
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * TempProposalMergeLogBean.java
 *
 * Created on April 20, 2011, 12:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.instprop.bean;

import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.bean.IBaseDataBean;
import edu.mit.coeus.exception.CoeusException;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author divyasusendran
 */
public class TempProposalMergeLogBean implements java.io.Serializable {
    
    private String tempProposalNumber;
    private String proposalNumber;
    private Timestamp updateTimeStamp;
    private String updateUser;
    
    /** Creates a new instance of TempProposalMergeLogBean */
    public TempProposalMergeLogBean() {
    }
    
    /**
     *Method to get tempProposalNumber
     *@return tempProposalNumber
     */
    public String getTempProposalNumber() {
        return tempProposalNumber;
    }
    
    /**
     *Method to set tempProposalNumber
     *@param tempProposalNumber
     */
    public void setTempProposalNumber(String tempProposalNumber) {
        this.tempProposalNumber = tempProposalNumber;
    }
    
    /**
     *Method to get proposalNumber
     *@return proposalNumber
     */
    public String getProposalNumber() {
        return proposalNumber;
    }
    
    /**
     *Method to set proposalNumber
     *@param proposalNumber
     */
    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
    
    /**
     *Method to get updateTimeStamp
     *@return updateTimeStamp
     */
    public Timestamp getUpdateTimeStamp() {
        return updateTimeStamp;
    }
    
    /**
     *Method to set updateTimeStamp
     *@param updateTimeStamp
     */
    public void setUpdateTimeStamp(Timestamp updateTimeStamp) {
        this.updateTimeStamp = updateTimeStamp;
    }
    
    /**
     *Method to get updateUser
     *@return updateUser
     */
    public String getUpdateUser() {
        return updateUser;
    }
    
    /**
     *Method to set updateUser
     *@param updateUser
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    
    
}
