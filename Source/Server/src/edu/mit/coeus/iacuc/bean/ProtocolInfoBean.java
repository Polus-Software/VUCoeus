/*
 * @(#)ProtocolInfoBean.java October 24, 2002, 9:58 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 29-NOV-2010
 * by Johncy M John
 */

package edu.mit.coeus.iacuc.bean;

import java.beans.*;
import java.util.Vector;
import edu.mit.coeus.bean.IBaseDataBean;

/**
 * The class used to hold the information of <code>Protocol InfoBean</code>
 *
 * @author  phani
 * @version 1.0
 * Created on October 24, 2002, 9:58 AM
 */

public class ProtocolInfoBean implements java.io.Serializable, IBaseDataBean {
    // holds the protocol type code
    private static final String PROP_PROTOCOL_TYPE_CODE = "protocolTypeCode";
    // holds the protocol status code
    private static final String PROP_PROJECT_TYPE_CODE = "projectTypeCode";
    private static final 
                   String PROP_PROTOCOL_STATUS_CODE = "protocolStatusCode";
    // holds the protocol title
    private static final String PROP_TITLE = "title";
    // holds the protocol application date
    private static final String PROP_APPLICATION_DATE = "applicationDate";
    // holds the protocol approval date
    private static final String PROP_APPROVAL_DATE = "approvalDate";
    // holds the protocol expiration date
    private static final String PROP_EXPIRATION_DATE = "expirationDate";
    // holds the protocol application number
    private static final 
                   String PROP_FDA_APPLICATION_NUMBER = "FDAApplicationNumber";
    
    private static final String PROP_REF_1 = "refNum_1";
    
    private static final String PROP_REF_2 = "refNum_2";
    
    private static final String PROP_DESCRIPTION = "description";
    private static final String LAY_STATEMENT_1 = "layStmt1";
    private static final String LAY_STATEMENT_2 = "layStmt2";
    //COEUSQA-2628-Add to Study Group screen a large comment box called "Overview and Timeline"
    private static final String OVERVIEW_TIMELINE = "overviewTimeline";
    
    //holds protocol number
    private String protocolNumber;
    //holds sequence number
    private int sequenceNumber;
    //holds version number
    private int versionNumber;
    //holds protocol type code
    private int protocolTypeCode;
    //holds protocol type description
    private String protocolTypeDesc;
    //holds protocol status code
    private int protocolStatusCode;
    //holds protocol status description
    private String protocolStatusDesc;
    //holds protocol title
    private String title;
    //holds protocol Description - Added new field for Phase III enhancement
    private String description;    
    //holds the special review indicator
    private String specialReviewIndicator;
    
    private boolean isAllSpecialReviewsDeleted;
    //holds the vulnerable subject indicator
    private String vulnerableSubjectIndicator;
    //holds the indicator for vulnerable table
    private String vulnerableIndicator;
    private boolean isAllVulnerablesDelete;
    // holds the indicator flag for KeyStudyPersonnel data
    private String keyStudyIndicator;
    // holds the status ( Modified/ Not Modified / Not Present ) which set in ProtocolDetailForm 
    private String keyStudyIndicatorStatus;
    //private boolean isAllKeyPersonnelsDelete;
    
    // holds the indicator flag for FundingSource data
    private String fundingSourceIndicator;
    // holds the status ( Modified/ Not Modified / Not Present ) which set in ProtocolDetailForm 
    private String fundingSourceIndicatorStatus;
    private boolean isAllFundingSourcesDelete;
    
    private String correspondenceIndicator;
    private String correspondenceIndicatorStatus;
    private boolean isAllCorrespondentsDelete;
    
    private String referenceIndicator;
    private String referenceIndicatorStatus;
    private boolean isAllReferencesDelete;    
    
    //Protocol Related Projects
    private String projectsIndicator;
    private String projectsIndicatorStatus;
    private boolean isAllProjectsDelete;
    //Added for indicator logic start
    private String scientificJustIndicator;
    private String alterSearchIndicator;
    //Added for indicator logic end
    //holds application date
    private java.sql.Date applicationDate;
    //holds approval date
    private java.sql.Date approvalDate;
    //holds expiration date
    private java.sql.Date expirationDate;
    //holds last approval date
    private java.sql.Date lastApprovalDate;
    //holds FDA application number
    private String FDAApplicationNumber;
    // holds parameter value of REF_1
    private String refNum_1;
    // holds parameter value of REF_2
    private String refNum_2;
    
    //holds the is billable flag
    private boolean isBillable;
    //holds the isReviewlistexist
    private boolean isReviewListExists;
    //holds the status of the child table for indicator logic
    private String specialReviewDataStatus;
    //holds the status of the child table for indicator logic
    private String vulnerableDataStatus;
    //holds protocol vulnerable subject bean
    private Vector vulnerableSubjectLists;
    //holds protocol locations bean
    private Vector locationLists;
    //holds protocol keystudy personnel bean
    private Vector keyStudyPersonnel;
    //holds protocol investigators bean
    private Vector investigators;
    //holds protocol correspondents bean
    private Vector correspondants;
    //holds protocol areaofresearch bean
    private Vector areaOfResearch;
    //holds protocol funding souces bean
    private Vector fundingSources;
    //holds protocol actions bean
    private Vector actions;
    
    // Added By Raghunath P.V. for implementing ProtocolSpecialReview
    //holds protocol alternative bean
    //private Vector alternativeSearch;
    //holds protocol special review bean
    private Vector specialReviews;
    //holds protocol user roles
    private Vector userRoles;
    //holds UserRolesInfoBean
    private Vector userRolesInfoBean;
    //holds ProtocolNotepadBeans for the protocol
    private Vector vecNotepad;
    //holds ProtocolReferencesBeans for the protocol
    private Vector references;    
    //holds ProtocolCustomElementsInfoBean for the protocol
    private Vector customElements;
    //holds ProtocolRelatedProjectsBean for the protocol
    private Vector relatedProjects;
    
    //holds create user id
    private String createUser;
    //holds create timestamp
    private java.sql.Timestamp createTimestamp;
    //holds update user id
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds account type
    private String acType;
    
    private String refId;
    
    private PropertyChangeSupport propertySupport;
    
    private int aw_ProtocolStatusCode;
    
    private ProtocolDetailChangeFlags  protocolDetailChangeFlags;
    
    private Vector amendmentRenewal;
    /*
     *Added by Geo to check whether it has got any pending ammendment or renewal
     */
    private boolean pendingAmmendRenewal;
    //To store the datas of Amendments/Renewals editable modules
    private Vector amendRenewEditableModules;
    
    //Added for COEUSDEV-328 : Notify IRB FYI submission only permits the Aggregator to upload one document - Start
    private Vector actionsDocuments;
    //COEUSDEV-328 : End
    private String layStmt1;
    private String layStmt2;
    private int projectTypeCode;
    private String projectTypeDesc;
    private Vector vecSpecies;
    private Vector altSearch;
    private Vector vecStudyGroup;
    private Vector vecPrinciples;
    private Vector vecExceptions;

    /*Added for IACUC Issue #1905 - Start*/
    // Modified with Indicator logic implementation in Species-Study Groups
    // Species and Study group should be treated as a single module
    private String speciesStudyGroupIndicator;
    // Indicator logic implementation in Species-Study Groups - End
    private String alterSearchDataStatus;   
    private String scientJustPrinciplesDataStatus;
    private String scientJustExceptionDataStatus;
    /*Added for IACUC Issue #1905 - End*/
    
    //COEUSQA:3503 - Protocol Copying - Option to Copy Attachments and Questionnaires- Start
    private boolean copyQnr;
    private boolean copyAttachments;
    private boolean copyOtherAttachments;
    private String awProtocolNumber;
    private Vector attachments;
    private Vector otherAttachments;
    //COEUSQA:35303 - End
    
    //COEUSQA-2628-Add to Study Group screen a large comment box called "Overview and Timeline"
    private String overviewTimeline;
    /** Creates new ProtocolInfoBean */
    public ProtocolInfoBean() {
        propertySupport = new PropertyChangeSupport( this );
        protocolDetailChangeFlags = new ProtocolDetailChangeFlags();
    }
    
    /**
     * Method used to get the protocol number
     * @return protocolNumber String
     */
    public String getProtocolNumber(){
        return protocolNumber;
    }
    
    /**
     * Method used to set the protocol number
     * @param protocolNumber String
     */
    public void setProtocolNumber(String protocolNumber){
        this.protocolNumber = protocolNumber;
    }

    /**
     * Method used to get the sequence number
     * @return sequenceNumber int
     */
    public int getSequenceNumber(){
        return sequenceNumber;
    }
    
    /**
     * Method used to set the sequence number
     * @param sequenceNumber int
     */
    public void setSequenceNumber(int sequenceNumber){
        this.sequenceNumber = sequenceNumber;
    }
/**
     * Method used to get the version number
     * @return versionNumber int
     */
    public int getVersionNumber(){
        return versionNumber;
    }
    
    /**
     * Method used to set the version number
     * @param versionNumber int
     */
    public void setVersionNumber(int versionNumber){
        this.versionNumber = versionNumber;
    }

    /**
     * Method used to get the protocol type code
     * @return protocolTypeCode int
     */
    public int getProtocolTypeCode(){
        return protocolTypeCode;
    }
    
    /**
     * Method used to set the protocol type code
     * @param newProtocolTypeCode integer
     */
    public void setProtocolTypeCode(int newProtocolTypeCode){
        int oldProtocolTypeCode = protocolTypeCode;
        this.protocolTypeCode = newProtocolTypeCode;
        propertySupport.firePropertyChange(PROP_PROTOCOL_TYPE_CODE, 
                oldProtocolTypeCode, protocolTypeCode);
    }

    /**
     * Method used to get the protocol type description
     * @return protocolTypeDesc String
     */
    public String getProtocolTypeDesc(){
        return protocolTypeDesc;
    }
    
    /**
     * Method used to set the protocol type description
     * @param protocolTypeDesc String
     */
    public void setProtocolTypeDesc(String protocolTypeDesc){
        this.protocolTypeDesc = protocolTypeDesc;
    }

    /**
     * Method used to get the protocol status code
     * @return protocolStatusCode int
     */
    public int getProtocolStatusCode(){
        return protocolStatusCode;
    }
    
    /**
     * Method used to set the protocol status code
     * @param newProtocolStatusCode int
     */
    public void setProtocolStatusCode(int newProtocolStatusCode){
        int oldProtocolStatusCode = protocolStatusCode;
        this.protocolStatusCode = newProtocolStatusCode;
        propertySupport.firePropertyChange(PROP_PROTOCOL_STATUS_CODE, 
                oldProtocolStatusCode, protocolStatusCode);
    }

    /**
     * Method used to get the protocol status description
     * @return protocolStatusDesc String
     */
    public String getProtocolStatusDesc(){
        return protocolStatusDesc;
    }
    
    /**
     * Method used to set the protocol status description
     * @param protocolStatusDesc String
     */
    public void setProtocolStatusDesc(String protocolStatusDesc){
        this.protocolStatusDesc = protocolStatusDesc;
    }

    /**
     * Method used to get the protocol title
     * @return title String
     */
    public String getTitle(){
        return title;
    }
    
    /**
     * Method used to set the protocol title
     * @param newTitle String
     */
    public void setTitle(String newTitle){
        String oldTitle = title;
        this.title = newTitle;
        propertySupport.firePropertyChange(PROP_TITLE, 
                oldTitle, title);   
    }
    
    /**
     * Method used to get the protocol vulnerableIndicator
     * @return vulnerableIndicator String
     */
    public String getVulnerableIndicator(){
        return vulnerableIndicator;
    }
    
    /**
     * Method used to set the protocol vulnerableIndicator
     * @param vulnerableIndicator String
     */
    public void setVulnerableIndicator(String vulnerableIndicator){
        this.vulnerableIndicator = vulnerableIndicator;
    }
    
    /**
     * Method used to get the application date
     * @return applicationDate Date
     */
    public java.sql.Date getApplicationDate(){
        return applicationDate;
    }
    
    /**
     * Method used to set the application date
     * @param newApplicationDate Date
     */
    public void setApplicationDate(java.sql.Date newApplicationDate){
        java.sql.Date oldApplicationDate = applicationDate;
        this.applicationDate = newApplicationDate;
        propertySupport.firePropertyChange(PROP_APPLICATION_DATE, 
                oldApplicationDate, applicationDate);   

    }

    /**
     * Method used to get the approval date
     * @return approvalDate Date
     */
    public java.sql.Date getApprovalDate(){
        return approvalDate;
    }
    
    /**
     * Method used to set the approval date
     * @param newApprovalDate Date
     */
    public void setApprovalDate(java.sql.Date newApprovalDate){
        java.sql.Date oldApprovalDate = approvalDate;
        this.approvalDate = newApprovalDate;
        propertySupport.firePropertyChange(PROP_APPROVAL_DATE, 
                oldApprovalDate, approvalDate);   
    }

    /**
     * Method used to get the expiration date
     * @return expirationDate Date
     */
    public java.sql.Date getExpirationDate(){
        return expirationDate;
    }
    
    /**
     * Method used to set the expiration date
     * @param newExpirationDate Date
     */
    public void setExpirationDate(java.sql.Date newExpirationDate){
        java.sql.Date oldExpirationDate = expirationDate;
        this.expirationDate = newExpirationDate;
        propertySupport.firePropertyChange(PROP_EXPIRATION_DATE, 
                oldExpirationDate, expirationDate);   
    }

    /**
     * Method used to get the FDA application number
     * @return FDAApplicationNumber String
     */
    public String getFDAApplicationNumber(){
        return FDAApplicationNumber;
    }
    
    /**
     * Method used to set the FDA application number
     * @param newFDAApplicationNumber String
     */
    public void setFDAApplicationNumber(String newFDAApplicationNumber){
        String oldFDAApplicationNumber = FDAApplicationNumber;
        this.FDAApplicationNumber = newFDAApplicationNumber;
        propertySupport.firePropertyChange(PROP_FDA_APPLICATION_NUMBER, 
                oldFDAApplicationNumber, FDAApplicationNumber);   

    }

    /**
     * Method used to get the is billable
     * @return isBillable boolean
     */
    public boolean isBillableFlag(){
        return isBillable;
    }
    
    /**
     * Method used to set the isBillable 
     * @param isBillable boolean
     */
    public void setIsBillableFlag(boolean isBillable){
        this.isBillable = isBillable;
    }

    
    /**
     * Method used to get the Vector of ProtocolInvestigatorsBean
     * @return investigators Vector
     */
    public Vector getInvestigators(){
        return investigators;
    }
    
    /**
     * Method used to set the Vector of ProtocolInvestigatorsBean
     * @param investigators Vector
     */
    public void setInvestigators(Vector investigators){
        this.investigators = investigators;
    }

    /**
     * Method used to get the Vector of ProtocolVulnerableSubListsBean
     * @return vulnerableSubjectLists Vector
     */
    public Vector getVulnerableSubjectLists(){
        return vulnerableSubjectLists;
    }
    
    /**
     * Method used to set the Vector of ProtocolVulnerableSubListsBean
     * @param vulnerableSubjectLists Vector
     */
    public void setVulnerableSubjectLists(Vector vulnerableSubjectLists){
        this.vulnerableSubjectLists = vulnerableSubjectLists;
    }

    /**
     * Method used to get the Vector of ProtocolLocationListBean
     * @return locationLists Vector
     */
    public Vector getLocationLists(){
        return locationLists;
    }
    
    /**
     * Method used to set the Vector of ProtocolLocationListBean
     * @param locationLists Vector
     */
    public void setLocationLists(Vector locationLists){
        this.locationLists = locationLists;
    }

    /**
     * Method used to get the Vector of ProtocolKeyPersonnelBean
     * @return keyStudyPersonnel Vector
     */
    public Vector getKeyStudyPersonnel(){
        return keyStudyPersonnel;
    }
    
    /**
     * Method used to set the Vector of ProtocolKeyPersonnelBean
     * @param keyStudyPersonnel Vector
     */
    public void setKeyStudyPersonnel(Vector keyStudyPersonnel){
        this.keyStudyPersonnel = keyStudyPersonnel;
    }

    /**
     * Method used to get the Vector of ProtocolCorrespondentsBean
     * @return correspondants Vector
     */
    public Vector getCorrespondetns(){
        return correspondants;
    }
    
    /**
     * Method used to set the Vector of ProtocolCorrespondentsBean
     * @param correspondants Vector
     */
    public void setCorrespondants(Vector correspondants){
        this.correspondants = correspondants;
    }

    /**
     * Method used to get the Vector of ProtocolReasearchAreasBean
     * @return areaOfResearch Vector
     */
    public Vector getAreaOfResearch(){
        return areaOfResearch;
    }
    
    /**
     * Method used to set the Vector of ProtocolReasearchAreasBean
     * @param areaOfResearch Vector
     */
    public void setAreaOfResearch(Vector areaOfResearch){
        this.areaOfResearch = areaOfResearch;
    }
    
    /**
     * Method used to get the Vector of ProtocolFundingSourceBean
     * @return fundingSources Vector
     */
    public Vector getFundingSources(){
        return fundingSources;
    }
    
    /**
     * Method used to set the Vector of ProtocolFundingSourceBean
     * @param fundingSources Vector
     */
    public void setFundingSources(Vector fundingSources){
        this.fundingSources = fundingSources;
    }
    
    // Added By Raghunath P.V. for implementing ProtocolSpecialReview
    
    /**
     * Method used to get the Vector of ProtocolSpecialReviewFormBean
     * @return specialReviews Vector
     */
    public Vector getSpecialReviews(){
        return specialReviews;
    }
    
    // Added By Raghunath P.V. for implementing ProtocolSpecialReview
    
    /**
     * Method used to set the Vector of ProtocolSpecialReviewFormBean
     * @param reviews Vector
     */
    public void setSpecialReviews(Vector reviews){
        this.specialReviews = reviews;
    }     
    
    /**
     * Method used to get the Vector of ProtocolActionsBean
     * @return actions Vector
     */
    public Vector getActions(){
        return actions;
    }
    
    /**
     * Method used to set the Vector of ProtocolActionsBean
     * @param actions Vector
     */
    public void setActions(Vector actions){
        this.actions = actions;
    }
    
    /**
     * Method used to get the create user id
     * @return createUser String
     */
    public String getCreateUser(){
        return createUser;
    }
    
    /**
     * Method used to set the create user id
     * @param createUser String
     */
    public void setCreateUser(String createUser){
        this.createUser = createUser;
    }
    
    /**
     * Method used to get the create timestamp
     * @return createTimestamp Timestamp
     */
    public java.sql.Timestamp getCreateTimestamp(){
        return createTimestamp;
    }
    
    /**
     * Method used to set the create timestamp
     * @param createTimestamp Timestamp
     */
    public void setCreateTimestamp(java.sql.Timestamp createTimestamp){
        this.createTimestamp = createTimestamp;
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
     * Method used to get the Account Type
     * @return acType String
     */
    public String getAcType(){
        return acType;
    }
    
    /**
     * Method used to set the Account Type
     * @param acType String
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
        strBffr.append("protocolTypeCode  =>"+ protocolTypeCode);
        strBffr.append("protocolTypeDesc =>"+protocolTypeDesc);
        strBffr.append("protocolStatusCode =>"+protocolStatusCode);
        strBffr.append("protocolStatusDesc  =>"+ protocolStatusDesc);
        strBffr.append("title =>"+title);
        strBffr.append("description =>"+description);
        strBffr.append("applicationDate =>"+applicationDate);
        strBffr.append("approvalDate  =>"+ approvalDate);
        strBffr.append("expirationDate =>"+expirationDate);
        strBffr.append("FDAApplicationNumber =>"+FDAApplicationNumber);
        strBffr.append("createUser =>"+createUser);
        strBffr.append("createTimestamp =>"+createTimestamp);
        strBffr.append("updateUser =>"+updateUser);
        strBffr.append("updateTimestamp =>"+updateTimestamp);
        strBffr.append("acType =>"+acType);
    
        return strBffr.toString();
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
     * Method used to set the reference Id
     * @param refId String
     */
    public void setRefId(String refId){
        this.refId = refId;
    }
    
    /**
     * Method used to get the reference Id
     * @return refId String
     */
    public String getRefId(){
        return this.refId;
    }
    
    /** Getter for property specialReviewIndicator.
     * @return Value of property specialReviewIndicator.
     */
    public java.lang.String getSpecialReviewIndicator() {
        return specialReviewIndicator;
    }
    
    /** Setter for property specialReviewIndicator.
     * @param specialReviewIndicator New value of property specialReviewIndicator.
     */
    public void setSpecialReviewIndicator(java.lang.String specialReviewIndicator) {
        this.specialReviewIndicator = specialReviewIndicator;
    }
    
    /** Getter for property userRoles.
     * @return Value of property userRoles.
     */
    public java.util.Vector getUserRoles() {
        return userRoles;
    }
    
    /** Setter for property userRoles.
     * @param userRoles New value of property userRoles.
     */
    public void setUserRoles(java.util.Vector userRoles) {
        this.userRoles = userRoles;
    }
    
    /** Getter for property isReviewListExists.
     * @return Value of property isReviewListExists.
     */
    public boolean isReviewListExists() {
        return isReviewListExists;
    }
    
    /** Setter for property isReviewListExists.
     * @param isReviewListExists New value of property isReviewListExists.
     */
    public void setIsReviewListExists(boolean isReviewListExists) {
        this.isReviewListExists = isReviewListExists;
    }
    
    /** Getter for property userRolesInfoBean.
     * @return Value of property userRolesInfoBean.
     */
    public java.util.Vector getUserRolesInfoBean() {
        return userRolesInfoBean;
    }
    
    /** Setter for property userRolesInfoBean.
     * @param userRolesInfoBean New value of property userRolesInfoBean.
     */
    public void setUserRolesInfoBean(java.util.Vector userRolesInfoBean) {
        this.userRolesInfoBean = userRolesInfoBean;
    }
    
    /** Getter for property vulnerableSubjectIndicator.
     * @return Value of property vulnerableSubjectIndicator.
     */
    public java.lang.String getVulnerableSubjectIndicator() {
        return vulnerableSubjectIndicator;
    }
    
    /** Setter for property vulnerableSubjectIndicator.
     * @param vulnerableSubjectIndicator New value of property vulnerableSubjectIndicator.
     */
    public void setVulnerableSubjectIndicator(java.lang.String vulnerableSubjectIndicator) {
        this.vulnerableSubjectIndicator = vulnerableSubjectIndicator;
    }
    
    
    /** Getter for property specialReviewDataStatus.
     * @return Value of property specialReviewDataStatus.
     */
    public java.lang.String getSpecialReviewDataStatus() {
        return specialReviewDataStatus;
    }
    
    /** Setter for property specialReviewDataStatus.
     * @param specialReviewDataStatus New value of property specialReviewDataStatus.
     */
    public void setSpecialReviewDataStatus(java.lang.String specialReviewDataStatus) {
        this.specialReviewDataStatus = specialReviewDataStatus;
    }
    
    /** Getter for property vulnerableDataStatus.
     * @return Value of property vulnerableDataStatus.
     */
    public java.lang.String getVulnerableDataStatus() {
        return vulnerableDataStatus;
    }
    
    /** Setter for property vulnerableDataStatus.
     * @param vulnerableDataStatus New value of property vulnerableDataStatus.
     */
    public void setVulnerableDataStatus(java.lang.String vulnerableDataStatus) {
        this.vulnerableDataStatus = vulnerableDataStatus;
    }
  
    /** Getter for property vecNotepad.
     * @return Value of property vecNotepad.
     */
    public java.util.Vector getVecNotepad() {
        return vecNotepad;
    }
    
    /** Setter for property vecNotepad.
     * @param vecNotepad New value of property vecNotepad.
     */
    public void setVecNotepad(java.util.Vector vecNotepad) {
        this.vecNotepad = vecNotepad;
    }
    
    /** Getter for property refNum_1.
     * @return Value of property refNum_1.
     */
    public java.lang.String getRefNum_1() {
        return refNum_1;
    }
    
    /** Setter for property refNum_1.
     * @param newRefNum_1 New value of property refNum_1.
     */
    public void setRefNum_1(java.lang.String newRefNum_1) {
        //this.refNum_1 = refNum_1;
        
        String oldRefNum_1 = refNum_1;
        this.refNum_1 = newRefNum_1;
        propertySupport.firePropertyChange(PROP_REF_1, oldRefNum_1, refNum_1);   
        
    }
    
    /** Getter for property refNum_2.
     * @return Value of property refNum_2.
     */
    public java.lang.String getRefNum_2() {
        return refNum_2;
    }
    
    /** Setter for property refNum_2.
     * @param newRefNum_2 New value of property refNum_2.
     */
    public void setRefNum_2(java.lang.String newRefNum_2) {
        String oldRefNum_2 = refNum_2;
        this.refNum_2 = newRefNum_2;
        propertySupport.firePropertyChange(PROP_REF_2, oldRefNum_2, refNum_2);   
    }
    
    /** Getter for property keyStudyIndicator.
     * @return Value of property keyStudyIndicator.
     */
    public java.lang.String getKeyStudyIndicator() {
        return keyStudyIndicator;
    }
    
    /** Setter for property keyStudyIndicator.
     * @param keyStudyIndicator New value of property keyStudyIndicator.
     */
    public void setKeyStudyIndicator(java.lang.String keyStudyIndicator) {
        this.keyStudyIndicator = keyStudyIndicator;
    }
    
    /** Getter for property keyStudyIndicatorStatus.
     * @return Value of property keyStudyIndicatorStatus.
     */
    public java.lang.String getKeyStudyIndicatorStatus() {
        return keyStudyIndicatorStatus;
    }
    
    /** Setter for property keyStudyIndicatorStatus.
     * @param keyStudyIndicatorStatus New value of property keyStudyIndicatorStatus.
     */
    public void setKeyStudyIndicatorStatus(java.lang.String keyStudyIndicatorStatus) {
        this.keyStudyIndicatorStatus = keyStudyIndicatorStatus;
    }
    
    /** Getter for property fundingSourceIndicator.
     * @return Value of property fundingSourceIndicator.
     */
    public java.lang.String getFundingSourceIndicator() {
        return fundingSourceIndicator;
    }
    
    /** Setter for property fundingSourceIndicator.
     * @param fundingSourceIndicator New value of property fundingSourceIndicator.
     */
    public void setFundingSourceIndicator(java.lang.String fundingSourceIndicator) {
        this.fundingSourceIndicator = fundingSourceIndicator;
    }
    
    /** Getter for property fundingSourceIndicatorStatus.
     * @return Value of property fundingSourceIndicatorStatus.
     */
    public java.lang.String getFundingSourceIndicatorStatus() { 
        return fundingSourceIndicatorStatus;
    }
    
    /** Setter for property fundingSourceIndicatorStatus.
     * @param fundingSourceIndicatorStatus New value of property fundingSourceIndicatorStatus.
     */
    public void setFundingSourceIndicatorStatus(java.lang.String fundingSourceIndicatorStatus) {
        this.fundingSourceIndicatorStatus = fundingSourceIndicatorStatus;
    }
    
    /**
     * Method used to get the aw_Protocol status code
     * @return aw_ProtocolStatusCode int
     */
    public int getAw_ProtocolStatusCode(){
        return aw_ProtocolStatusCode;
    }
    
    /**
     * Method used to set the aw_Protocol status code
     * @param aw_ProtocolStatusCode int
     */
    public void setAw_ProtocolStatusCode(int aw_ProtocolStatusCode){
        this.aw_ProtocolStatusCode = aw_ProtocolStatusCode;
    }    
    
    /** Getter for property description.
     * @return Value of property description.
     */
    public java.lang.String getDescription() {
        return description;
    }
    
    /** Setter for property description.
     * @param newDescription New value of property description.
     */
    public void setDescription(java.lang.String newDescription) {
        String oldDescription = description;
        this.description = newDescription;
        propertySupport.firePropertyChange(PROP_DESCRIPTION,
                oldDescription, description);
    }
    
    /** Getter for property correspondenceIndicator.
     * @return Value of property correspondenceIndicator.
     */
    public java.lang.String getCorrespondenceIndicator() {
        return correspondenceIndicator;
    }
    
    /** Setter for property correspondenceIndicator.
     * @param correspondenceIndicator New value of property correspondenceIndicator.
     */
    public void setCorrespondenceIndicator(java.lang.String correspondenceIndicator) {
        this.correspondenceIndicator = correspondenceIndicator;
    }
    
    /** Getter for property correspondenceIndicatorStatus.
     * @return Value of property correspondenceIndicatorStatus.
     */
    public java.lang.String getCorrespondenceIndicatorStatus() {
        return correspondenceIndicatorStatus;
    }
    
    /** Setter for property correspondenceIndicatorStatus.
     * @param correspondenceIndicatorStatus New value of property correspondenceIndicatorStatus.
     */
    public void setCorrespondenceIndicatorStatus(java.lang.String correspondenceIndicatorStatus) {
        this.correspondenceIndicatorStatus = correspondenceIndicatorStatus;
    }
    
    /** Getter for property isAllCorrespondentsDelete.
     * @return Value of property isAllCorrespondentsDelete.
     */
    public boolean isAllCorrespondentsDelete() {
        return isAllCorrespondentsDelete;
    }
    
    /** Setter for property isAllCorrespondentsDelete.
     * @param isAllCorrespondentsDelete New value of property isAllCorrespondentsDelete.
     */
    public void setIsAllCorrespondentsDelete(boolean isAllCorrespondentsDelete) {
        this.isAllCorrespondentsDelete = isAllCorrespondentsDelete;
    }
    
    /** Getter for property isAllVulnerablesDelete.
     * @return Value of property isAllVulnerablesDelete.
     */
    public boolean isAllVulnerablesDelete() {
        return isAllVulnerablesDelete;
    }
    
    /** Setter for property isAllVulnerablesDelete.
     * @param isAllVulnerablesDelete New value of property isAllVulnerablesDelete.
     */
    public void setIsAllVulnerablesDelete(boolean isAllVulnerablesDelete) {
        this.isAllVulnerablesDelete = isAllVulnerablesDelete;
    }

    /** Getter for property isAllSpecialReviewsDeleted.
     * @return Value of property isAllSpecialReviewsDeleted.
     */
    public boolean isAllSpecialReviewsDeleted() {
        return isAllSpecialReviewsDeleted;
        
    }
    
    /** Setter for property isAllSpecialReviewsDeleted.
     * @param isAllSpecialReviewsDeleted New value of property isAllSpecialReviewsDeleted.
     */
    public void setIsAllSpecialReviewsDeleted(boolean isAllSpecialReviewsDeleted) {
        this.isAllSpecialReviewsDeleted = isAllSpecialReviewsDeleted;
    }
    
    /** Getter for property isAllFundingSourcesDelete.
     * @return Value of property isAllFundingSourcesDelete.
     */
    public boolean isAllFundingSourcesDelete() {
        return isAllFundingSourcesDelete;
    }
    
    /** Setter for property isAllFundingSourcesDelete.
     * @param isAllFundingSourcesDelete New value of property isAllFundingSourcesDelete.
     */
    public void setIsAllFundingSourcesDelete(boolean isAllFundingSourcesDelete) {
        this.isAllFundingSourcesDelete = isAllFundingSourcesDelete;
    }
    
    /** Getter for property referenceIndicator.
     * @return Value of property referenceIndicator.
     */
    public java.lang.String getReferenceIndicator() {
        return referenceIndicator;
    }
    
    /** Setter for property referenceIndicator.
     * @param referenceIndicator New value of property referenceIndicator.
     */
    public void setReferenceIndicator(java.lang.String referenceIndicator) {
        this.referenceIndicator = referenceIndicator;
    }
    
    /** Getter for property referenceIndicatorStatus.
     * @return Value of property referenceIndicatorStatus.
     */
    public java.lang.String getReferenceIndicatorStatus() {
        return referenceIndicatorStatus;
    }
    
    /** Setter for property referenceIndicatorStatus.
     * @param correspondenceIndicatorStatus New value of property referenceIndicatorStatus.
     */
    public void setReferenceIndicatorStatus(java.lang.String referenceIndicatorStatus) {
        this.referenceIndicatorStatus = referenceIndicatorStatus;
    }
    
    /** Getter for property isAllReferencesDelete.
     * @return Value of property isAllReferencesDelete.
     */
    public boolean isAllReferencesDelete() {
        return isAllReferencesDelete;
    }
    
    /** Setter for property isAllReferencesDelete.
     * @param isAllCorrespondentsDelete New value of property isAllReferencesDelete.
     */
    public void setIsAllReferencesDelete(boolean isAllReferencesDelete) {
        this.isAllReferencesDelete = isAllReferencesDelete;
    }    
    
    /** Getter for property references.
     * @return Value of property references.
     */
    public java.util.Vector getReferences() {
        return references;
    }
    
    /** Setter for property vecReferences.
     * @param vecReferences New value of property vecReferences.
     */
    public void setReferences(java.util.Vector references) {
        this.references = references;
    }        
    
    /** Getter for property customElements.
     * @return Value of property customElements.
     */
    public java.util.Vector getCustomElements() {
        return customElements;
    }
    
    /** Setter for property customElements.
     * @param customElements New value of property customElements.
     */
    public void setCustomElements(java.util.Vector customElements) {
        this.customElements = customElements;
    }    
    
    /** Getter for property protocolDetailChangeFlags.
     * @return Value of property protocolDetailChangeFlags.
     */
    public edu.mit.coeus.iacuc.bean.ProtocolDetailChangeFlags getProtocolDetailChangeFlags() {
        return protocolDetailChangeFlags;
    }    

    /** Setter for property protocolDetailChangeFlags.
     * @param protocolDetailChangeFlags New value of property protocolDetailChangeFlags.
     */
    public void setProtocolDetailChangeFlags(edu.mit.coeus.iacuc.bean.ProtocolDetailChangeFlags protocolDetailChangeFlags) {
        this.protocolDetailChangeFlags = protocolDetailChangeFlags;
    }    
    
    /** Getter for property projectsIndicator.
     * @return Value of property projectsIndicator.
     */
    public java.lang.String getProjectsIndicator() {
        return projectsIndicator;
    }
    
    /** Setter for property projectsIndicator.
     * @param projectsIndicator New value of property projectsIndicator.
     */
    public void setProjectsIndicator(java.lang.String projectsIndicator) {
        this.projectsIndicator = projectsIndicator;
    }
    
    /** Getter for property projectsIndicatorStatus.
     * @return Value of property projectsIndicatorStatus.
     */
    public java.lang.String getProjectsIndicatorStatus() {
        return projectsIndicatorStatus;
    }
    
    /** Setter for property projectsIndicatorStatus.
     * @param projectsIndicatorStatus New value of property projectsIndicatorStatus.
     */
    public void setProjectsIndicatorStatus(java.lang.String projectsIndicatorStatus) {
        this.projectsIndicatorStatus = projectsIndicatorStatus;
    }
    
    /** Getter for property isAllProjectsDelete.
     * @return Value of property isAllProjectsDelete.
     */
    public boolean isAllProjectsDelete() {
        return isAllProjectsDelete;
    }
    
    /** Setter for property isAllProjectsDelete.
     * @param isAllProjectsDelete New value of property isAllProjectsDelete.
     */
    public void setIsAllProjectsDelete(boolean isAllProjectsDelete) {
        this.isAllProjectsDelete = isAllProjectsDelete;
    }
    
    /** Getter for property relatedProjects.
     * @return Value of property relatedProjects.
     */
    public java.util.Vector getRelatedProjects() {
        return relatedProjects;
    }
    
    /** Setter for property relatedProjects.
     * @param relatedProjects New value of property relatedProjects.
     */
    public void setRelatedProjects(java.util.Vector relatedProjects) {
        this.relatedProjects = relatedProjects;
    }
    
    /** Getter for property amendmentRenewal.
     * @return Value of property amendmentRenewal.
     */
    public java.util.Vector getAmendmentRenewal() {
        return amendmentRenewal;
    }
    
    /** Setter for property amendmentRenewal.
     * @param amendmentRenewal New value of property amendmentRenewal.
     */
    public void setAmendmentRenewal(java.util.Vector amendmentRenewal) {
        this.amendmentRenewal = amendmentRenewal;
    }
    
    /**
     * Getter for property lastApprovalDate.
     * @return Value of property lastApprovalDate.
     */
    public java.sql.Date getLastApprovalDate() {
        return lastApprovalDate;
    }
    
    /**
     * Setter for property lastApprovalDate.
     * @param lastApprovalDate New value of property lastApprovalDate.
     */
    public void setLastApprovalDate(java.sql.Date lastApprovalDate) {
        this.lastApprovalDate = lastApprovalDate;
    }
    
    
    /**
     * Getter for property pendingAmmendRenewal.
     * @return Value of property pendingAmmendRenewal.
     */
    public boolean isPendingAmmendRenewal() {
        return pendingAmmendRenewal;
    }
    
    /**
     * Setter for property pendingAmmendRenewal.
     * @param pendingAmmendRenewal New value of property pendingAmmendRenewal.
     */
    public void setPendingAmmendRenewal(boolean pendingAmmendRenewal) {
        this.pendingAmmendRenewal = pendingAmmendRenewal;
    }

    public Vector getAmendRenewEditableModules() {
        return amendRenewEditableModules;
    }

    public void setAmendRenewEditableModules(Vector amendRenewEditableModules) {
        this.amendRenewEditableModules = amendRenewEditableModules;
    }
    
    //Added for COEUSDEV-328 : Notify IRB FYI submission only permits the Aggregator to upload one document - Start
    /*
     * Setter for property actionsDocuments
     * @param actionsDocuments - Vector
     */
    public void setActionsDocument(Vector actionsDocuments){
        this.actionsDocuments = actionsDocuments;
    }
    
    /*
     * Getter method for actionsDocuments
     * @return actionsDocuments - vector
     */
    public Vector getActionsDocuments(){
        return this.actionsDocuments;
    }
    //COEUSDEV-328 : End

    public String getLayStmt1() {
        return layStmt1;
    }

    public void setLayStmt1(String newLayStmt1) {
        String oldLayStmt1 = layStmt1;
        this.layStmt1 = newLayStmt1;
        propertySupport.firePropertyChange(LAY_STATEMENT_1,
                oldLayStmt1, layStmt1);
    }

    public String getLayStmt2() {
        return layStmt2;
    }

    public void setLayStmt2(String newLayStmt2) {
        String oldLayStmt2 = layStmt2;
        this.layStmt2 = newLayStmt2;
        propertySupport.firePropertyChange(LAY_STATEMENT_2,
                oldLayStmt2, layStmt2);
    }
    
 
    public int getProjectTypeCode() {
        return projectTypeCode;
    }

    public void setProjectTypeCode(int newProjectTypeCode) {
        int oldProjectTypeCode = projectTypeCode;
        this.projectTypeCode = newProjectTypeCode;
        propertySupport.firePropertyChange(PROP_PROJECT_TYPE_CODE,
                oldProjectTypeCode, projectTypeCode);
    }
    

    public String getProjectTypeDesc() {
        return projectTypeDesc;
    }

    public void setProjectTypeDesc(String projectTypeDesc) {
        this.projectTypeDesc = projectTypeDesc;
    }

    /*
     * Method to get the species
     * @return vecSpecies - Vector
     */
    public Vector getSpecies() {
        return vecSpecies;
    }
    
    /*
     * Method to set the protocol species
     * @param vecSpecies - Vector
     */
    public void setSpecies(Vector vecSpecies) {
        this.vecSpecies = vecSpecies;
    }
    
    /*
     * Method to get the alternative search
     * @return altSearch - Vector
     */
    public Vector getAlternativeSearch() {
        return altSearch;
    }
    
    /*
     * Method to set the alternative search
     * @param altSearch - Vector
     */
    public void setAlternativeSearch(Vector altSearch) {
        this.altSearch = altSearch;
    }
    
    /*
     * Method to get the study groups
     * @return vecSpecies - Vector
     */
    public Vector getStudyGroups() {
        return vecStudyGroup;
    }
    
    /*
     * Method to set the protocol study groups
     * @param vecSpecies - Vector
     */
    public void setStudyGroups(Vector vecStudyGroup) {
        this.vecStudyGroup = vecStudyGroup;
    }    
    
    /*
     * Method to get the Principles
     * @return vecPrinciples - Vector
     */
    public Vector getScientJustPrinciples() {
        return vecPrinciples;
    }
    
    /*
     * Method to set the protocol Principles
     * @param vecPrinciples - Vector
     */
    public void setScientJustPrinciples(Vector vecPrinciples) {
        this.vecPrinciples = vecPrinciples;
    }
    
     /*
     * Method to get the Exceptions
     * @return vecExceptions - Vector
     */
    public Vector getScientJustExceptions() {
        return vecExceptions;
    }
    
    /*
     * Method to set the protocol Exceptions
     * @param vecExceptions - Vector
     */
    public void setScientJustExceptions(Vector vecExceptions) {
        this.vecExceptions = vecExceptions;
    }
    
    /*Added For IACUC Issue # 1905 - Start*/
    // Commented with Indicator logic implementation in Species-Study Groups
//    /** Getter for property speciesDataStatus.
//     * @return Value of property speciesDataStatus.
//     */
//    public java.lang.String getSpeciesDataStatus() {
//        return speciesDataStatus;
//    }
//    
//    /** Setter for property speciesDataStatus.
//     * @param speciesDataStatus New value of property speciesDataStatus.
//     */
//    public void setSpeciesDataStatus(java.lang.String speciesDataStatus) {
//        this.speciesDataStatus = speciesDataStatus;
//    }
//    
//    /** Getter for property studyGroupDataStatus.
//     * @return Value of property studyGroupDataStatus.
//     */
//    public java.lang.String getStudyGroupDataStatus() {
//        return studyGroupDataStatus;
//    }
//    
//    /** Setter for property studyGroupDataStatus.
//     * @param studyGroupDataStatus New value of property studyGroupDataStatus.
//     */
//    public void setStudyGroupDataStatus(java.lang.String studyGroupDataStatus) {
//        this.studyGroupDataStatus = studyGroupDataStatus;
//    }
    
    /** Getter for property alterSearchDataStatus.
     * @return Value of property alterSearchDataStatus.
     */
    public java.lang.String getAlterSearchDataStatus() {
        return alterSearchDataStatus;
    }
    
    /** Setter for property alterSearchDataStatus.
     * @param alterSearchDataStatus New value of property alterSearchDataStatus.
     */
    public void setAlterSearchDataStatus(java.lang.String alterSearchDataStatus) {
        this.alterSearchDataStatus = alterSearchDataStatus;
    }
    
    /** Getter for property scientJustPrinciplesDataStatus.
     * @return Value of property scientJustPrinciplesDataStatus.
     */
    public java.lang.String getScientJustPrinciplesDataStatus() {
        return scientJustPrinciplesDataStatus;
    }
    
    /** Setter for property scientJustPrinciplesDataStatus.
     * @param scientJustPrinciplesDataStatus New value of property scientJustPrinciplesDataStatus.
     */
    public void setScientJustPrinciplesDataStatus(java.lang.String scientJustPrinciplesDataStatus) {
        this.scientJustPrinciplesDataStatus = scientJustPrinciplesDataStatus;
    }
    
    /** Getter for property scientJustExceptionDataStatus.
     * @return Value of property scientJustExceptionDataStatus.
     */
    public java.lang.String getScientJustExceptionDataStatus() {
        return scientJustExceptionDataStatus;
    }
    
    /** Setter for property scientJustExceptionDataStatus.
     * @param scientJustExceptionDataStatus New value of property scientJustExceptionDataStatus.
     */
    public void setScientJustExceptionDataStatus(java.lang.String scientJustExceptionDataStatus) {
        this.scientJustExceptionDataStatus = scientJustExceptionDataStatus;
    }
    /*Added For IACUC Issue # 1905 - End*/
    
    //COEUSQA-2628-Add to Study Group screen a large comment box called "Overview and Timeline" - start
    
    /** Getter for property overviewTimeline.
     * @return Value of property overviewTimeline.
     */
    public String getOverviewTimeline() {
        return overviewTimeline;
    }

    /** Setter for property overviewTimeline.
     *  @param newOverviewTimeline New value of property overviewTimeline.
     */
    public void setOverviewTimeline(String newOverviewTimeline) {
        String oldOverviewTimeline = overviewTimeline;
        this.overviewTimeline = newOverviewTimeline;
        propertySupport.firePropertyChange(OVERVIEW_TIMELINE, oldOverviewTimeline, overviewTimeline);
    }
     //COEUSQA-2628-Add to Study Group screen a large comment box called "Overview and Timeline" - end
    
    //Added for indicator logic start
    public String getScientificJustIndicator() {
        return scientificJustIndicator;
    }
    
    public void setScientificJustIndicator(String scientificJustIndicator) {
        this.scientificJustIndicator = scientificJustIndicator;
    }

    public void setAlterSearchIndicator(String alterSearchIndicator) {
        this.alterSearchIndicator = alterSearchIndicator;
    }
   
    public String getAlterSearchIndicator() {
        return alterSearchIndicator;
    }

    public void setSpeciesStudyGroupIndicator(String speciesStudyGroupIndicator) {
        this.speciesStudyGroupIndicator = speciesStudyGroupIndicator;
    }
    
    public String getSpeciesStudyGroupIndicator() {
        return speciesStudyGroupIndicator;
    }
    //Added for indicator logic end
    
    //COEUSQA:3503 - Protocol Copying - Option to Copy Attachments and Questionnaires- Start
    /**
     * Method used to get the awProtocolNumber
     * @return awProtocolNumber String
     */
    public String getAwProtocolNumber(){
        return awProtocolNumber;
    }
    
    /**
     * Method used to set the awProtocolNumber
     * @param awProtocolNumber String
     */
    public void setAwProtocolNumber(String awProtocolNumber){
        this.awProtocolNumber = awProtocolNumber;
    }
    /*
     * Setter for property attachments
     * @param attachments - Vector
     */
    public void setAttachments(Vector attachments){
        this.attachments = attachments;
    }
    
    /*
     * Getter method for attachments
     * @return attachments - vector
     */
    public Vector getAttachments(){
        return this.attachments;
    }
    
    /*
     * Setter for property otherAttachments
     * @param otherAttachments - Vector
     */
    public void setOtherAttachments(Vector otherAttachments){
        this.otherAttachments = otherAttachments;
    }
    
    /*
     * Getter method for otherAttachments
     * @return otherAttachments - vector
     */
    public Vector getOtherAttachments(){
        return this.otherAttachments;
    }   

    public boolean isCopyQnr() {
        return copyQnr;
    }

    public void setCopyQnr(boolean copyQnr) {
        this.copyQnr = copyQnr;
    }

    public boolean isCopyAttachments() {
        return copyAttachments;
    }

    public void setCopyAttachments(boolean copyAttachments) {
        this.copyAttachments = copyAttachments;
    }

    public boolean isCopyOtherAttachments() {
        return copyOtherAttachments;
    }

    public void setCopyOtherAttachments(boolean copyOtherAttachments) {
        this.copyOtherAttachments = copyOtherAttachments;
    }
    //COEUSQA:3503 - End
}
