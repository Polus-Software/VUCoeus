/*
 * @(#)AbsenteesInfoBean.java 1.0 11/18/02 3:43 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.iacuc.bean.*;
import java.io.Serializable;
/**
 * The class used to hold the information of <code>Schedule Absentees</code>
 * The data will get attached with the <code>Schedule Attendance</code> form.
 * 
 * @author  Mukundan C
 * @version 1.0
 * Created on November 18, 2002, 3:43 PM
 */

public class AbsenteesInfoBean implements Serializable {
    // holds the committee id
    private String committeeId;
    // holds the person id
    private String personId;
    // holds the membership id
    private String membershipId;
    // holds the sequence number
    private int sequenceNumber;
    // holds the person name
    private String personName;

    /** Creates a new instance of AbsenteesInfoBean */
    public AbsenteesInfoBean() {
    }

    /**
     * Method used to get the committee id
     * @return committeeId String
     */
    public String getCommitteeId() {
        return committeeId;
    }

    /**
     * Method used to set the committee id
     * @param committeeId String
     */
    public void setCommitteeId(String committeeId) {
        this.committeeId = committeeId;
    }

    /**
     * Method used to get the person id
     * @return personId String
     */
    public String getPersonId() {
        return personId;
    }

    /**
     * Method used to set the person id
     * @param personId String
     */
    public void setPersonId(String personId) {
        this.personId = personId;
    }

    /**
     * Method used to get the membership id
     * @return membershipId String
     */
    public String getMembershipId() {
        return membershipId;
    }

    /**
     * Method used to set the membership id
     * @param membershipId String
     */
    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
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
     * Method used to get the person name
     * @return personName String
     */
    public String getPersonName() {
        return personName;
    }

    /**
     * Method used to set the person name
     * @param personName String
     */
    public void setPersonName(String personName) {
        this.personName = personName;
    }

}
