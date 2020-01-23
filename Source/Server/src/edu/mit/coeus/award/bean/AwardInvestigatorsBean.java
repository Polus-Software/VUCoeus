/*
 * @(#)AwardInvestigatorsBean.java 1.0 01/05/04 11:41 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.award.bean;

import edu.mit.coeus.bean.InvestigatorBean;
import edu.mit.coeus.bean.IBaseDataBean;
/**
 * The class used to hold the information of <code>Award Investigators</code>
 *
 * @author  Prasanna Kumar K
 * @version 1.0
 * Created on January 05, 2004, 11:41 AM
 */

public class AwardInvestigatorsBean extends InvestigatorBean implements IBaseDataBean {
    // holds the proposal number
    private String mitAwardNumber;
    //hoilds sequence number
    private int sequenceNumber;
    // Added for Case# 2229 - Multi PI enhancement
    private boolean multiPIFlag;
    //Added for Case# 2270 - Tracking of Effort - Start
    private float academicYearEffort;
    private float calendarYearEffort;
    private float summerYearEffort;
    //Added for Case# 2270 - Tracking of Effort - End
    /** Creates new AwardUnitBean */
    public AwardInvestigatorsBean() {
    }
    
    /** Creates new AwardUnitBean */
    public AwardInvestigatorsBean(InvestigatorBean investigatorBean) {
        super(investigatorBean);
    }
    
    /** Getter for property mitAwardNumber.
     * @return Value of property mitAwardNumber.
     */
    public java.lang.String getMitAwardNumber() {
        return mitAwardNumber;
    }
    
    /** Setter for property mitAwardNumber.
     * @param mitAwardNumber New value of property mitAwardNumber.
     */
    public void setMitAwardNumber(java.lang.String mitAwardNumber) {
        this.mitAwardNumber = mitAwardNumber;
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
        AwardInvestigatorsBean awardInvestigatorsBean = (AwardInvestigatorsBean)obj;
        //Donot check equality for sequence number - 8th April, 2004
        if(awardInvestigatorsBean.getMitAwardNumber().equals(getMitAwardNumber()) &&
                // sequnece number is required for equality. Case 2399
                awardInvestigatorsBean.getSequenceNumber() == getSequenceNumber() &&
                awardInvestigatorsBean.getPersonId().equals(getPersonId())){
            return true;
        }else{
            return false;
        }
    }
    
    // Added for Case# 2229  - Multi PI Enhancement - Start
    public boolean isMultiPIFlag() {
        return multiPIFlag;
    }
    
    public void setMultiPIFlag(boolean multiPIFlag) {
        this.multiPIFlag = multiPIFlag;
    }
    // Added for Case# 2229  - Multi PI Enhancement - End
    // Added for Case# 2270  - Tracking of Effort - Start
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
    // Added for Case# 2270  - Tracking of Effort - Start
}