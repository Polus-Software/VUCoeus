/*
 * @(#)ProtocolReferencesBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.iacuc.bean;

import java.beans.*;
import java.sql.Date;
import edu.mit.coeus.bean.ReferencesBean;
import edu.mit.coeus.bean.IBaseDataBean;
/**
 * The class used to hold the information of <code>Protocol References</code>
 *
 * @author
 * @version 1.0
 * Created on July 14, 2003, 10:26 PM
 */

public class ProtocolReferencesBean extends ReferencesBean implements java.io.Serializable, IBaseDataBean {

    //holds protocol number
    private  String protocolNumber;
    //holds sequence number
    private int sequenceNumber;

    /** Creates new ProtocolReferencesBean */
    public ProtocolReferencesBean() {
    }

    /** This is a Copy Constructor. This is used to copy all the contents 
     *  of a given ReferencesBean to ProtocolReferencesBean.
     *  
     * @param referencesBean to be copied to ProposalSpecialReviewFormBean
     */
    
    public ProtocolReferencesBean(ReferencesBean referencesBean) {
        setReferenceNumber(referencesBean.getReferenceNumber());
        setReferenceTypeCode(referencesBean.getReferenceTypeCode());
        setReferenceTypeDescription(referencesBean.getReferenceTypeDescription());
        setReferenceKey(referencesBean.getReferenceKey());
        setComments(referencesBean.getComments());
        setApplicationDate(referencesBean.getApplicationDate());
        setApprovalDate(referencesBean.getApprovalDate());
        //COEUSQA-1724-Added for New Expiration Date column-start
        setExpirationDate(referencesBean.getExpirationDate());
        //COEUSQA-1724-Added for New Expiration Date column-end
        setUpdateUser(referencesBean.getUpdateUser());
        setUpdateTimestamp(referencesBean.getUpdateTimestamp());
        setAcType(referencesBean.getAcType());
    }   
    
    /**
     * Method used to get the protocol number
     * @return protocolNumber String
     */
    public String getProtocolNumber() {
        return protocolNumber;
    }

    /**
     * Method used to set the protocol number
     * @param protocolNumber String
     */
    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }

    /**
     * Method used to get the sequence number
     * @return sequenceNumber int
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Method used to set the sequence number
     * @param sequenceNumber int
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
}
