/*
 * @(#)NotepadBean.java December 23, 2003, 11:10 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.bean;

import java.beans.*;
import edu.mit.coeus.bean.BaseBean;
/**
 * The class used to hold the information of <code>Proposal or Award Notepad info</code>
 *
 * @author  Prasanna Kumar K
 * @version 1.0
 * Created on December 23, 2003, 11:10 AM
 */

public class NotepadBean implements java.io.Serializable, BaseBean {
    
    //holds either Institute Proposal Number, Development Proposal Number or Award Number
    private String proposalAwardNumber;
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
    //holds acType
    private String acType;
    //holds type of NotePad
    private String notePadType;
    //holds user name
    private String userName;
    
    private PropertyChangeSupport propertySupport;
    private static final String PROPOSAL_AWARD_NUMBER = "proposalAwardNumber";
    private static final String ENTRY_NUMBER = "entryNumber";
    private static final String RESTRICTED_VIEW = "restrictedView";
    
    //Userid to Username enhancement
    private String updateUserName;
    /** Creates new ProtocolNotepadBean */
    public NotepadBean() {
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
    
    /** Getter for property proposalAwardNumber.
     * @return Value of property proposalAwardNumber.
     */
    public java.lang.String getProposalAwardNumber() {
        return proposalAwardNumber;
    }    
    
    /** Setter for property proposalAwardNumber.
     * @param proposalAwardNumber New value of property proposalAwardNumber.
     */
    public void setProposalAwardNumber(java.lang.String newProposalAwardNumber) {
        String oldProposalAwardNumber = proposalAwardNumber;
        this.proposalAwardNumber = newProposalAwardNumber;
        propertySupport.firePropertyChange(PROPOSAL_AWARD_NUMBER, 
                oldProposalAwardNumber, proposalAwardNumber);        
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
    public void setEntryNumber(int newEntryNumber) {
        int oldEntryNumber = entryNumber;
        this.entryNumber = newEntryNumber;
        propertySupport.firePropertyChange(ENTRY_NUMBER, 
                oldEntryNumber, entryNumber);         
    }
    
    /** Getter for property comments.
     * @return Value of property comments.
     */
    public java.lang.String getComments() {
        return comments;
    }
    
    /** Setter for property comments.
     * @param comments New value of property comments.
     */
    public void setComments(java.lang.String newComments) {
        String oldComments = comments;
        this.comments = newComments;
        propertySupport.firePropertyChange(ENTRY_NUMBER, 
                oldComments, comments);                 
    }
    
    /** Getter for property restrictedView.
     * @return Value of property restrictedView.
     */
    public boolean isRestrictedView() {
        return restrictedView;
    }
    
    /** Setter for property restrictedView.
     * @param restrictedView New value of property restrictedView.
     */
    public void setRestrictedView(boolean newRestrictedView) {
        boolean oldRestrictedView = restrictedView;
        this.restrictedView = newRestrictedView;
        propertySupport.firePropertyChange(RESTRICTED_VIEW, 
                oldRestrictedView, restrictedView);              
    }
    
    /** Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /** Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /** Getter for property updateUser.
     * @return Value of property updateUser.
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /** Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }
    
    /** Getter for property acType.
     * @return Value of property acType.
     */
    public java.lang.String getAcType() {
        return acType;
    }
    
    /** Setter for property acType.
     * @param acType New value of property acType.
     */
    public void setAcType(java.lang.String acType) {
        this.acType = acType;
    }    
    
    /** Getter for property notePadType.
     * @return Value of property notePadType.
     */
    public java.lang.String getNotePadType() {
        return notePadType;
    }
    
    /** Setter for property notePadType.
     * @param notePadType New value of property notePadType.
     */
    public void setNotePadType(java.lang.String notePadType) {
        this.notePadType = notePadType;
    }
    
    /** Getter for property userName.
     * @return Value of property userName.
     */
    public java.lang.String getUserName() {
        return userName;
    }
    
    /** Setter for property userName.
     * @param userName New value of property userName.
     */
    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }    

    //Userid to Username enhancement - Start
    /** Getter for property updateUserName.
     * @return Value of property updateUserName.
     */
    public java.lang.String getUpdateUserName() {
        return updateUserName;
    }
    
    /** Setter for property updateUserName.
     * @param updateUserName New value of property updateUserName.
     */
    public void setUpdateUserName(java.lang.String updateUserName) {
        this.updateUserName = updateUserName;
    }    
    //Userid to Username enhancement - End
}