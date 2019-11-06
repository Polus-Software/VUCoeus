/*
 * @(#)ProtocolReasearchAreasBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.iacuc.bean;

import java.beans.*;
import edu.mit.coeus.bean.IBaseDataBean; 
/**
 * The class used to hold the information of <code>Protocol ResearchAreasBean</code>
 * The data will get attached with the <code>Protocol ResearchAreasBean</code> form.
 * 
 * @author  phani
 * @version 1.0
 * Created on October 24, 2002, 12:52 PM
 */

public class ProtocolReasearchAreasBean implements java.io.Serializable, IBaseDataBean {
    // holds the protocol number
    private String protocolNumber;
     // holds the sequence number
    private int sequenceNumber;
     // holds the research area code
    private String researchAreaCode;
     // holds the research area description
    private String researchAreaDescription;
     //holds update user id
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds account type
    private String acType;
    
    private PropertyChangeSupport propertySupport;
    
    /** Creates new ProtocolReasearchAreasBean */
    public ProtocolReasearchAreasBean() {
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

    /**
     * Method used to get the research Area Code
     * @return researchAreaCode String
     */
    public String getResearchAreaCode() {
        return researchAreaCode;
    }

    /**
     * Method used to set the research Area Code
     * @param researchAreaCode String
     */
    public void setResearchAreaCode(String researchAreaCode) {
        this.researchAreaCode = researchAreaCode;
    }

    /**
     * Method used to get the research Area description
     * @return researchAreaDescription String
     */
    public String getResearchAreaDescription() {
        return researchAreaDescription;
    }

    /**
     * Method used to set the research Area description
     * @param researchAreaDescription String
     */
    public void setResearchAreaDescription(String researchAreaDescription) {
        this.researchAreaDescription = researchAreaDescription;
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
     *  Method used to set the Action Type
     *  @param acType actionType
     */
    public void setAcType(String acType){
        this.acType = acType;
    }
    

    
    /**
     * To print the bean data in the server side 
     * 
     * @return String 
     */
     public String toString(){
        StringBuffer strBffr = new StringBuffer();
        strBffr.append("{ Protocol Number =>"+protocolNumber);
        strBffr.append("sequenceNumber =>"+sequenceNumber);
        strBffr.append("researchAreaCode  =>"+ researchAreaCode);
        strBffr.append("researchAreaDescription =>"+researchAreaDescription);
        strBffr.append("updateUser =>"+updateUser);
        strBffr.append("updateTimestamp =>"+updateTimestamp);
        strBffr.append("acType =>"+acType);
    
        return strBffr.toString();
    }
    

}
