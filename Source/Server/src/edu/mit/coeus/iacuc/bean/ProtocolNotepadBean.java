/*
 * @(#)ProtocolNotepadBean.java June 17, 2003, 17:10 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.iacuc.bean;

import java.beans.*;
import edu.mit.coeus.bean.IBaseDataBean;
/**
 * The class used to hold the information of <code>Protocol Notepad Tab</code>
 *
 * @author  Senthil
 * @version 1.0
 * Created on June 17, 2003, 17:10 PM
 */

public class ProtocolNotepadBean implements java.io.Serializable, IBaseDataBean {
    
    //holds protocol number
    private String protocolNumber;
    //holds sequence number
    private int sequenceNumber;
    //holds entry number
    private int entryNumber;
    //holds comments
    private String comments;
    //holds restricted view 
    private boolean restrictedView;
    //holds time stamp
    private java.sql.Timestamp updateTimestamp;
    //holds logged in user
    private String updateUser;
    //holds updated user name
    //Added for Userid to Username enhancement
    private String updateUserName;
    //holds acType
    private String acType;

    // holds the Comments string 
    private static final String PROTO_NOTEPAD_COMMENTS = "comments";
    
    // holds the restricted flag 
    private static final String PROTO_NOTERESTRICTED_FLAG = "restrictedView";
    
    
    private PropertyChangeSupport propertySupport;
    
    /** Creates new ProtocolNotepadBean */
    public ProtocolNotepadBean() {
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
     * Method used to get the nonemployee flag
     * @return nonEmployeeFlag boolean
     */
    public boolean  isRestrictedFlag() {
        return restrictedView;
    }

    /**
     * Method used to set the nonemployee flag
     * @param newNonEmployeeFlag boolean
     */
    public void setRestrictedFlag(boolean newRestrictedViewFlag) {
        boolean oldRestrictedViewFlag  = restrictedView;
        this.restrictedView = newRestrictedViewFlag;
        propertySupport.firePropertyChange(PROTO_NOTERESTRICTED_FLAG, 
                oldRestrictedViewFlag, restrictedView);
    }

     /**
     * Method used to get the protocol comments
     * @return comments String
     */
    public String getComments() {
        return comments;
    }

    /**
     * Method used to set the protocol comments
     * @param newComments String
     */
    public void setComments(String newComments) {
        String oldComments = comments;
        this.comments = newComments;
        propertySupport.firePropertyChange(PROTO_NOTEPAD_COMMENTS, 
                oldComments, comments);
    }

    /**
     * Method used to get the update user id
     * @return updateUser String
     */
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * Method used to set the update user id
     * @param updateUser String
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * Method used to get the update timestamp
     * @return updateTimestamp Timestamp
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    /**
     * Method used to set the update timestamp
     * @param updateTimestamp Timestamp
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
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

    /** Getter for property entryNumber.
     * @return Value of property entryNumber.
     */
    public int getEntryNumber() {
        return entryNumber;
    }
    
    /** Setter for property entryNumber.
     * @param entryNumber New value of property entryNumber.
     */
    public void setEntryNumber(int entryNumber) {
        this.entryNumber = entryNumber;
    }
    
    //Userid to UserName enhancement - Start
    /**
     * Method used to get the update user name
     * @return updateUserName String
     */
    public String getUpdateUserName() {
        return updateUserName;
    }

    /**
     * Method used to set the update user name
     * @param updateUserName String
     */
    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }
    //Userid to Username enhancement - End
}
