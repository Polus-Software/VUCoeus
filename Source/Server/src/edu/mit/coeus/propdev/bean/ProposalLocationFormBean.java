/* 
 * @(#)ProposalLocationFormBean.java 1.0 03/12/03 3:01 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.propdev.bean;

import java.beans.*;
import java.sql.Timestamp;

/**
 * The class used to hold the information of <code>Proposal Location</code>
 *
 * @author  Mukundan.C
 * @version 1.0
 * Created on March 12, 2003, 3:01 PM
 */
public class ProposalLocationFormBean implements java.io.Serializable {
    
    private static final String PROP_ADDRESS = "address";
     //holds the proposal number
     private String proposalNumber;
     //holds the proposal location
     private String location;
     //holds the rolodex id
     private int rolodexId;
     //holds the proposal loccation address
     private String address;
    //holds update user id
     private String updateUser;
    //holds update timestamp
     private Timestamp updateTimestamp;
    //holds account type
     private String acType;
     
    private PropertyChangeSupport propertySupport;
    
    /** Creates new ProtocolLocationListBean */
    public ProposalLocationFormBean() {
        propertySupport = new PropertyChangeSupport( this );
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
    
    public String getProposalNumber() {
        return proposalNumber;
    }

    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
    public String getProposalLocation() {
        return location;
    }

    public void setProposalLocation(String location) {
        this.location = location;
    }
    public int getRolodexId() {
        return rolodexId;
    }

    public void setRolodexId(int rolodexId) {
        this.rolodexId = rolodexId;
    }
    
    /**
     * Method used to get the address
     * @return address String
     */
    public String getAddress(){
        return address;
    }
    /**
     * Method used to set the address
     * @param newAddress String
     */
    public void setAddress(String newAddress){
        String oldAddress = address;
        this.address =newAddress;
        propertySupport.firePropertyChange(PROP_ADDRESS, 
                oldAddress, address);
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
