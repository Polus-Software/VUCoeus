/*
 * ProposalCertificationFormBean.java
 *
 * Created on April 3, 2003, 12:33 PM
 */

package edu.mit.coeus.propdev.bean;

import java.beans.*;
import java.sql.Timestamp;
import java.sql.Date;

/**
 *
 * @author  mukund
 */
public class ProposalCertificationFormBean implements java.io.Serializable {
    
    private static final String PROP_CERTIFY_FLAG ="certifyFlag";
    // holds the proposal number
    private String proposalNumber;
    // holds the person id
    private String personId;
   // holds the certify flag
    private boolean certifyFlag;
    //holds the date certify
    private Date dateCertify;
    private static final String PROP_DATE_CERTIFY ="dateCertify";
    //holds the date received by osp
    private Date dateReceivedByOsp;
    private static final String PROP_DATE_RECEIVEDBY ="dateReceivedByOsp";    
    // holds the person name
    private String personName;
    //holds update user id
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds Action type
    private String acType;
    
    private PropertyChangeSupport propertySupport;
    
    /** Creates new ProposalCertificationFormBean */
    public ProposalCertificationFormBean() {
        propertySupport = new PropertyChangeSupport( this );
    }
    
    public String getProposalNumber() {
        return proposalNumber;
    }

    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
    
    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

     public boolean isCertifyFlag() {
        return certifyFlag;
    }

    public void setCertifyFlag(boolean newCertifyFlag) {
        boolean oldCertifyFlag  = certifyFlag;
        this.certifyFlag  = newCertifyFlag;
        propertySupport.firePropertyChange(PROP_CERTIFY_FLAG, 
                oldCertifyFlag, certifyFlag);
    }
    
    
    public Date getDateCertify() {
        return dateCertify;
    }

    public void setDateCertify(Date newDateCertify) {
        Date oldDateCertify  = dateCertify;
        this.dateCertify  = newDateCertify;
        propertySupport.firePropertyChange(PROP_DATE_CERTIFY, 
                oldDateCertify, dateCertify);
    }
    
    public Date getDateReceivedByOsp() {
        return dateReceivedByOsp;
    }

    public void setDateReceivedByOsp(Date newDateReceivedByOsp) {
        Date oldDateReceivedByOsp  = dateReceivedByOsp;
        this.dateReceivedByOsp  = newDateReceivedByOsp;
        propertySupport.firePropertyChange(PROP_DATE_RECEIVEDBY, 
                oldDateReceivedByOsp, dateReceivedByOsp);
    
    }
    
    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }
    
    /**
     * Method used to get the update user id
     * @return updateUser String
     */
    public String getUpdateUser(){
        return updateUser;
    }
    
    /**
     * Method used to set the update user id
     * @param updateUser String
     */
    public void setUpdateUser(String updateUser){
        this.updateUser = updateUser;
    }
    
    /**
     * Method used to get the update timestamp
     * @return updateTimestamp Timestamp
     */
    public java.sql.Timestamp getUpdateTimestamp(){
        return updateTimestamp;
    }
    
    /**
     * Method used to set the update timestamp
     * @param updateTimestamp Timestamp
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp){
        this.updateTimestamp = updateTimestamp;
    }
    
    /**
     * Method used to get the Action Type
     * @return acType String
     */
    public String getAcType(){
        return acType;
    }
    
    /**
     * Method used to set the Action Type
     * @param acType String
     */
    public void setAcType(String acType){
        this.acType = acType;
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
}
