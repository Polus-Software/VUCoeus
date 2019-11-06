/*
 * @(#)ProposalKeyPersonFormBean.java 1.0 03/11/03 9:33 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.bean;

import java.beans.*;
import java.sql.Timestamp;

import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.bean.IBaseDataBean;
import edu.mit.coeus.bean.KeyPersonBean;

import java.util.Vector;
/**
 * The class used to hold the information of <code>Proposal Investigator</code>
 *
 * @author  Mukundan.C
 * @version 1.0
 * Created on March 11, 2003, 9:33 AM
 */
public class ProposalKeyPersonFormBean extends KeyPersonBean implements IBaseDataBean{

     private static final String PROP_FACULTY_FLAG ="facultyFlag";
     private static final String PROP_PERCENTAGE_EFFORT ="percentageEffort";
     private static final String PROP_ROLE ="projectRole";

   
     //holds the proposal number
     private String proposalNumber;
     private int sequenceNumber;
    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
    //holds the true value if person is both PI and Key person flag
    private boolean bothPIAndKeyPersonFlag;
    private static final String PROP_BPIKP_FLAG ="bothPIAndKeyPersonFlag";
    private PropertyChangeSupport propertySupport;
    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
    
    /* JM 9-7-2015 added person status */
    private String status;
    
    /* JM 1-8-2016 added isExternalPerson flag */
    private String isExternalPerson ;
    /* JM END */
    
    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
    //public ProposalKeyPersonFormBean() {}
    public ProposalKeyPersonFormBean() {
        propertySupport = new PropertyChangeSupport( this );
    }
    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
     
    public ProposalKeyPersonFormBean(KeyPersonBean keyPersonBean) {
       super(keyPersonBean);
       //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
       propertySupport = new PropertyChangeSupport( this );
       //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
    }
    
    /* JM 1-8-2016 added isExternalPerson flag */
    public String getIsExternalPerson() {
    	return isExternalPerson ;
    }
    
    public void setIsExternalPerson(String isExternalPerson ) {
    	this.isExternalPerson  = isExternalPerson ;
    }
    /* JM END */
    
    /* JM 9-7-2015 added person status */
    public String getStatus() {
    	return status;
    }
    
    public void setStatus(String status) {
    	this.status = status;
    }
    /* JM END */

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
        if(obj == null){
            return false;
        }
        if(obj == this){
            return true;
        }
        if(obj instanceof ProposalKeyPersonFormBean){
            ProposalKeyPersonFormBean proposalKeyPersonBean = (ProposalKeyPersonFormBean)obj;
            if(proposalKeyPersonBean.getProposalNumber().equals(getProposalNumber()) &&
                    proposalKeyPersonBean.getSequenceNumber() == getSequenceNumber() &&
                    proposalKeyPersonBean.getPersonId().equals(getPersonId())){
                return true;
            }
        }
        return false;
    }
    public int hashCode(){
        StringBuffer buffer= new StringBuffer(30);
        buffer.append(proposalNumber);
        buffer.append('-');
        buffer.append(sequenceNumber);
        buffer.append('-');
        buffer.append(getPersonId());
        return buffer.toString().hashCode();
    }
    
    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
    public boolean isBothPIAndKeyPerson() {
        return bothPIAndKeyPersonFlag;
    }
    
    public void setBothPIAndKeyPerson(boolean newbothPIAndKeyPersonFlag) {
        boolean oldbothPIAndKeyPersonFlag = bothPIAndKeyPersonFlag;
        this.bothPIAndKeyPersonFlag = newbothPIAndKeyPersonFlag;
        propertySupport.firePropertyChange(PROP_BPIKP_FLAG,
                oldbothPIAndKeyPersonFlag, bothPIAndKeyPersonFlag);
    }
    
    /**
     * Method used to add propertychange listener to the fields
     * @param listener PropertyChangeListener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    /**
     * Method used to remove propertychange listener
     * @param listener PropertyChangeListener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
}
