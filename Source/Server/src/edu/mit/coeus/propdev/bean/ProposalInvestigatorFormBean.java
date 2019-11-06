/*
 * @(#)ProposalInvestigatorFormBean.java 1.0 03/10/03 5:56 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.bean;

import java.beans.*;
import java.sql.Timestamp;
import java.util.Vector;
import java.util.Hashtable;

import edu.mit.coeus.bean.BaseBean;
/**
 * The class used to hold the information of <code>Proposal Investigator</code>
 *
 * @author  Mukundan.C
 * @version 1.0
 * Created on March 10, 2003, 5:56 PM
 */

public class ProposalInvestigatorFormBean implements java.io.Serializable , BaseBean {
    
    private static final String PROP_FACULTY_FLAG ="facultyFlag";
    private static final String PROP_CONF_FLAG ="conflictOfIntersetFlag";
    private static final String PROP_PERCENTAGE_EFFORT ="percentageEffort";
    private static final String PROP_DEBR_FLAG ="fedrDebrFlag";
    private static final String PROP_DELQ_FLAG ="fedrDelqFlag";
    //holds the proposal number
    private String proposalNumber;
    //holds the person id
    private String personId;
    //holds the person name
    private String personName;
    //holds the principle Investigator flag
    private boolean principleInvestigatorFlag;
    //holds the faculty flag
    private boolean facultyFlag;
    //holds the non mit flag
    private boolean nonMITPersonFlag;
    //holds the conflict of interset flag
    private boolean conflictOfIntersetFlag;
    //holds the percentage effort
    private float percentageEffort;
    //holds the fedr debr flag
    private boolean fedrDebrFlag;
    //holds the fedr delg flag
    private boolean fedrDelqFlag;
    //holds the vector investigator bean
    private java.util.Vector investigatorUnits;
    //holds update user id
    private String updateUser;
    //holds update timestamp
    private Timestamp updateTimestamp;
    //holds account type
    private String acType;
    private Object certifyFlag;
    
    // Added for Case # 2229 - Holds the Mullti PI Flag
    private boolean multiPIFlag;
    
    // Declared for Case # 2270- Start
    // Holds the Academic Year of the Effort
    private float academicYearEffort;
    // Holds the Summer Effort of the Effort
    private float summerYearEffort;
    // Holds the Calender Year of the Effort
    private float calendarYearEffort;
    // Declared for Case # 2270- End
    
    private PropertyChangeSupport propertySupport;
    /** Creates a new instance of PersonCustomElementsInfoBean */
    
    private Vector investigatorAnswers; // answers for the investigator persons
    
    
    private Vector investigatorQuestions; // question list for investigator type
    private Hashtable moreExplanation; // more explanation for the questions
    
    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
    //holds the true value if person is both PI and Key person flag
    private boolean bothPIAndKeyPersonFlag;
    private static final String PROP_BPIKP_FLAG ="bothPIAndKeyPersonFlag";
    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
    
    /* JM 9-7-2015 added person status */
    private String status;
    
    /* JM 1-8-2016 added isExternalPerson flag */
    private String isExternalPerson;
    /* JM END */

    public ProposalInvestigatorFormBean() {
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
    
    /* JM 1-8-2016 added isExternalPerson flag */
    public String getIsExternalPerson() {
    	return isExternalPerson;
    }
    
    public void setIsExternalPerson(String isExternalPerson) {
    	this.isExternalPerson = isExternalPerson;
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
    
    public String getPersonId() {
        return personId;
    }
    
    public void setPersonId(String personId) {
        this.personId = personId;
    }
    
    public String getPersonName() {
        return personName;
    }
    
    public void setPersonName(String personName) {
        this.personName = personName;
    }
    
    public boolean isPrincipleInvestigatorFlag() {
        return principleInvestigatorFlag;
    }
    
    public void setPrincipleInvestigatorFlag(boolean principleInvestigatorFlag) {
        this.principleInvestigatorFlag = principleInvestigatorFlag;
    }
    
    public boolean isFacultyFlag() {
        return facultyFlag;
    }
    
    public void setFacultyFlag(boolean newFacultyFlag) {
        boolean oldFacultyFlag = facultyFlag;
        this.facultyFlag = newFacultyFlag;
        propertySupport.firePropertyChange(PROP_FACULTY_FLAG,
                oldFacultyFlag, facultyFlag);
    }
    
    public boolean isNonMITPersonFlag() {
        return nonMITPersonFlag;
    }
    
    public void setNonMITPersonFlag(boolean nonMITPersonFlag) {
        this.nonMITPersonFlag = nonMITPersonFlag;
    }
     public void setCertifyFlag(Object certifyFlag) {
        this.certifyFlag = certifyFlag;
    }
 
    public boolean isConflictOfIntersetFlag() {
        return conflictOfIntersetFlag;
    }
    
    public void setConflictOfIntersetFlag(boolean newConflictOfIntersetFlag) {
        boolean oldConflictOfIntersetFlag = conflictOfIntersetFlag;
        this.conflictOfIntersetFlag = newConflictOfIntersetFlag;
        propertySupport.firePropertyChange(PROP_CONF_FLAG,
                oldConflictOfIntersetFlag, conflictOfIntersetFlag);
    }
    
    public float getPercentageEffort() {
        return percentageEffort;
    }
    
    public void setPercentageEffort(float percentageEffort) {
        this.percentageEffort = percentageEffort;
    }
    
    public boolean isFedrDebrFlag() {
        return fedrDebrFlag;
    }
    
    public void setFedrDebrFlag(boolean newFedrDebrFlag) {
        boolean oldFedrDebrFlag = fedrDebrFlag;
        this.fedrDebrFlag = newFedrDebrFlag;
        propertySupport.firePropertyChange(PROP_DEBR_FLAG,
                oldFedrDebrFlag, fedrDebrFlag);
        
    }
    
    public boolean isFedrDelqFlag() {
        return fedrDelqFlag;
    }
    
    public void setFedrDelqFlag(boolean newFedrDelqFlag) {
        boolean oldFedrDelqFlag = fedrDebrFlag;
        this.fedrDelqFlag = newFedrDelqFlag;
        propertySupport.firePropertyChange(PROP_DELQ_FLAG,
                oldFedrDelqFlag, fedrDelqFlag);
    }
    
    /**
     * Method used to get the investigatorUnits
     * @return investigatorUnits Vector
     */
    public java.util.Vector getInvestigatorUnits(){
        return investigatorUnits;
        
    }
    /**
     * Method used to set the InvestigatorUnits
     * @param investigatorUnits Vector
     */
    public void setInvestigatorUnits(java.util.Vector investigatorUnits){
        this.investigatorUnits = investigatorUnits;
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
    
        /*
         * Vector contains vector of investigator answers ie. YNQBean
         */
    public void setInvestigatorAnswers(Vector investigatorAnswers){
        this.investigatorAnswers = investigatorAnswers;
    }
    public Vector getInvestigatorAnswers(){
        return investigatorAnswers;
    }
    
    //Added for Case # 2229  -  Getter and Setter Methods for multiPIFlag -Start
    public boolean isMultiPIFlag() {
        return multiPIFlag;
    }
    
    public void setMultiPIFlag(boolean multiPIFlag) {
        this.multiPIFlag = multiPIFlag;
    }
    //Added for Case # 2229  -  Getter and Setter Methods for multiPIFlag -End
    
    //Added for Case # 2270  -  Getter and Setter Methods for academicYear,summerEffort,calendarYear -Start
    public float getAcademicYearEffort() {
        return academicYearEffort;
    }

    public void setAcademicYearEffort(float academicYearEffort) {
        this.academicYearEffort = academicYearEffort;
    }

    public float getSummerYearEffort() {
        return summerYearEffort;
    }

    public void setSummerYearEffort(float summerYearEffort) {
        this.summerYearEffort = summerYearEffort;
    }

    public float getCalendarYearEffort() {
        return calendarYearEffort;
    }

    public void setCalendarYearEffort(float calendarYearEffort) {
        this.calendarYearEffort = calendarYearEffort;
    }
    //Added for Case # 2270  -  Getter and Setter Methods for academicYear,summerEffort,calendarYear -End
    
     //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
   /** Getter for property bothPIAndKeyPersonFlag.
    * @return Value of property bothPIAndKeyPersonFlag.
    */
    public boolean isBothPIAndKeyPerson() {
        return bothPIAndKeyPersonFlag;
    }
    
    /** Setter for property bothPIAndKeyPersonFlag.
     * @param newbothPIAndKeyPersonFlag New value of property bothPIAndKeyPersonFlag.
     */
    public void setBothPIAndKeyPerson(boolean newbothPIAndKeyPersonFlag) {
        boolean oldbothPIAndKeyPersonFlag = bothPIAndKeyPersonFlag;
        this.bothPIAndKeyPersonFlag = newbothPIAndKeyPersonFlag;
        propertySupport.firePropertyChange(PROP_BPIKP_FLAG,
                oldbothPIAndKeyPersonFlag, bothPIAndKeyPersonFlag);
    }

    /**
     * @return the certifyFlag
     */
    public Object isCertifyFlag() {
        return certifyFlag;
    }
    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
}
