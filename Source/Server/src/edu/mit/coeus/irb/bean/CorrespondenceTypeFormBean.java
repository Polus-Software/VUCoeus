/*
 * @(#)CorrespondenceFormBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/*
 * Created on February 26, 2003, 10:15 AM
 */

package edu.mit.coeus.irb.bean;

import java.sql.Timestamp;
import java.beans.*;
/**
 * The class used to hold the information of <code>Protocol Correspondence Type Detail</code>
 * The data will get attached with the <code>ProtocolMaintenanceForm</code>.
 *
 * 
 * @author  Geo Thomas
 * @version 1.0
 * Created on February 26, 2003, 10:15 AM
 */
public class CorrespondenceTypeFormBean  implements java.io.Serializable {
    
    private int protoCorrespTypeCode;
    private String protoCorrespTypeDesc;
    private Timestamp updateTimestamp;
    private String updateUser;
    private char acType;
    private String committeeId;
    private String committeeName;
    private byte[] fileBytes;
    private String aw_Committee_Id;
    // Added for Coeus 4.3 enhancement
    private boolean finalFlag;    
    
    // holds the protocol type code
    private static final String PROTO_CORRESP_TYPE_CODE = "protoCorrespTypeCode";
    // holds the protocol type description
    private static final String PROTO_CORRESP_TYPE_DESC = "protoCorrespTypeDesc";    
    // holds the byte array containing template
    private static final String FILE_BYTES = "fileBytes";    
    
    private PropertyChangeSupport propertySupport;
    
    /** Creates a new instance of CorrespondenceFormBean */
    public CorrespondenceTypeFormBean() {
        propertySupport = new PropertyChangeSupport( this );
    }    
    
    /**
     *  Gets the protocol correspondence type code
     *  @return protocol correspondence type code
     */
    public int getProtoCorrespTypeCode(){
     return this.protoCorrespTypeCode;   
    }
    /**
     *  Sets the protocol correspondence type code
     *  @param protocol correspondence type code
     */
    public void setProtoCorrespTypeCode(int newProtoCorrespTypeCode){
        int oldProtoCorrespTypeCode = protoCorrespTypeCode;
        this.protoCorrespTypeCode = newProtoCorrespTypeCode;
        propertySupport.firePropertyChange(PROTO_CORRESP_TYPE_CODE, 
                oldProtoCorrespTypeCode, protoCorrespTypeCode);        
    }   
    /**
     *  Gets the protocol correspondence type description
     *  @return protocol correspondence type description
     */
    public String getProtoCorrespTypeDesc(){
     return this.protoCorrespTypeDesc;   
    }
    /**
     *  Sets the protocol correspondence type description
     *  @param protocol correspondence type description
     */
    public void setProtoCorrespTypeDesc(String newProtoCorrespTypeDesc){
        String oldProtoCorrespTypeDesc = protoCorrespTypeDesc;
        this.protoCorrespTypeDesc = newProtoCorrespTypeDesc;
        propertySupport.firePropertyChange(PROTO_CORRESP_TYPE_DESC, 
                oldProtoCorrespTypeDesc, protoCorrespTypeDesc);                
    }   
    /**
     *  Gets the update timestamp
     *  @return update timestamp
     */
    public Timestamp getUpdateTimestamp(){
     return this.updateTimestamp;   
    }
    /**
     *  Sets the update timestamp
     *  @param update timestamp
     */
    public void setUpdateTimestamp(Timestamp updateTimestamp){
        this.updateTimestamp = updateTimestamp;
    }   
    /**
     *  Gets the update user
     *  @return update user
     */
    public String getUpdateUser(){
     return this.updateUser;   
    }
    /**
     *  Sets the update user
     *  @param update user
     */
    public void setUpdateUser(String updateUser){
        this.updateUser = updateUser;
    }   
    /**
     *  Gets the Action Type
     *  @return action type
     */
    public char getAcType(){
     return this.acType;   
    }
    /**
     *  Sets the Action Type
     *  @param action type
     */
    public void setAcType(char acType){
        this.acType = acType;
    }
    /**
     *  Overridden method to return the contents of this class.
     */
    public String toString(){
        StringBuffer strBffr = new StringBuffer("");
        strBffr.append("Code=>"+this.protoCorrespTypeCode);
        strBffr.append("Description=>"+this.protoCorrespTypeDesc);
        strBffr.append("Update User=>"+this.updateUser);
        strBffr.append("Update Timestamp=>"+this.updateTimestamp);
        strBffr.append("Action Type=>"+this.acType);
        return strBffr.toString();
    }
    
    /** Getter for property committeeId.
     * @return Value of property committeeId.
     */
    public java.lang.String getCommitteeId() {
        return committeeId;
    }
    
    /** Setter for property committeeId.
     * @param committeeId New value of property committeeId.
     */
    public void setCommitteeId(java.lang.String committeeId) {
        this.committeeId = committeeId;
    }
    
    /** Getter for property fileBytes.
     * @return Value of property fileBytes.
     */
    public byte[] getFileBytes() {
        return this.fileBytes;
    }
    
    /** Setter for property fileBytes.
     * @param fileBytes New value of property fileBytes.
     */
    public void setFileBytes(byte[] newFileBytes) {
        byte[] oldFileBytes = fileBytes;
        this.fileBytes = newFileBytes;
        propertySupport.firePropertyChange(FILE_BYTES, 
                oldFileBytes, fileBytes);                
    }
    
    /** Getter for property committeeName.
     * @return Value of property committeeName.
     */
    public java.lang.String getCommitteeName() {
        return committeeName;
    }
    
    /** Setter for property committeeName.
     * @param committeeName New value of property committeeName.
     */
    public void setCommitteeName(java.lang.String committeeName) {
        this.committeeName = committeeName;
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
    
   
    /** Getter for property aw_Committee_Id.
     * @return Value of property aw_Committee_Id.
     */
    public String getAw_Committee_Id() {
        return aw_Committee_Id;
    }
    
    /** Setter for property aw_Committee_Id.
     * @param aw_Committee_Id New value of property aw_Committee_Id.
     */
    public void setAw_Committee_Id(String aw_Committee_Id) {
        this.aw_Committee_Id = aw_Committee_Id;
    }
    
    /**
     * Getter for property finalFlag.
     * @return Value of property finalFlag.
     */
    public boolean isFinalFlag() {
        return finalFlag;
    }
    
    /**
     * Setter for property finalFlag.
     * @param finalFlag New value of property finalFlag.
     */
    public void setFinalFlag(boolean finalFlag) {
        this.finalFlag = finalFlag;
    }
    
}
