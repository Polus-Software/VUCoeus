/*
 * @(#)DisclosureInfoDetailBean.java 1.0 4/04/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.coi.bean;

import edu.mit.coeus.utils.UtilFactory;
/**
 * This class is for getting and setting historical info,
 * corresponding to disclosures
 *
 * @version 1.0 April 04, 2002, 6:12 PM
 * @author  Anil Nandakumar
 */
public class DisclosureInfoHistoryBean extends java.lang.Object {
    private String conflictStatus;
    private String reviewer;
    private String entityNumber;
    /**
     * Sequence number.
     * Corresponds to OSP$INV_COI_DISC_DETAILS.SEQUENCE_NUMBER.
     */
    private String sequenceNumber;

    /**
     * Entity sequence number.
     * Corresponds to OSP$INV_COI_DISC_DETAILS.ENTITY_SEQUENCE_NUMBER.
     */
    private String entitySequenceNumber;
    private String desc;
    private String updatedBy;
    private java.sql.Timestamp updatedDate;

    /** Creates new DisclosureInfoHistoryBean */
    public DisclosureInfoHistoryBean() {
    }
    /**
     *  This method gets the Updated Date
     *  @return String updatedDate
     */
    public java.sql.Timestamp getUpdatedDate() {
        return updatedDate;
    }
    /**
     *  This method sets the Updated Date
     *  @param String updatedDate
     */
    public void setUpdatedDate(java.sql.Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }
    /**
     *  This method gets the Entity Number
     *  @return String entityNumber
     */
    public String getEntityNumber() {
        return entityNumber;
    }
    /**
     *  This method sets the  Entity Number
     *  @param String entityNumber
     */
    public void setEntityNumber(java.lang.String entityNumber) {
        this.entityNumber = UtilFactory.checkNullStr(entityNumber);
    }
    /**
     *  This method gets the Sequence Number
     *  @return String sequenceNumber
     */
    public String getSequenceNumber() {
        return sequenceNumber;
    }
    /**
     *  This method sets the Sequence Number
     *  @param String sequenceNumber
     */
    public void setSequenceNumber(java.lang.String sequenceNumber) {
        this.sequenceNumber = UtilFactory.checkNullStr(sequenceNumber);
    }
    /**
     *  This method gets the Conflict Status
     *  @return String conflictStatus
     */
    public String getConflictStatus() {
        return conflictStatus;
    }
    /**
     *  This method sets the Conflict Status
     *  @param String conflictStatus
     */
    public void setConflictStatus(java.lang.String conflictStatus) {
        this.conflictStatus = UtilFactory.checkNullStr(conflictStatus);
    }
    /**
     *  This method gets the Reviewer
     *  @return String reviewer
     */
    public String getReviewer() {
        return reviewer;
    }
    /**
     *  This method sets the Reviewer
     *  @param String reviewer
     */
    public void setReviewer(java.lang.String reviewer) {
        this.reviewer = UtilFactory.checkNullStr(reviewer);
    }
    /**
     *  This method gets the Description
     *  @return String desc
     */
    public String getDesc() {
        return desc;
    }
    /**
     *  This method sets the Description
     *  @param String desc
     */
    public void setDesc(java.lang.String desc) {
        this.desc = UtilFactory.checkNullStr(desc);
    }
    /**
     *  This method gets the Updated By
     *  @return String updatedBy
     */
    public String getUpdatedBy() {
        return updatedBy;
    }
    /**
     *  This method sets the Updated By
     *  @param String updatedBy
     */
    public void setUpdatedBy(java.lang.String updatedBy) {
        this.updatedBy = UtilFactory.checkNullStr(updatedBy);
    }

    /**
     * Set entitySequenceNumber.
     * @param entitySequenceNumber
     */
    public void setEntitySequenceNumber(String entitySequenceNumber)
    {
      this.entitySequenceNumber = entitySequenceNumber;
    }

    /**
     * Get entitySequenceNumber.
     * @return String entitySequenceNumber.
     */
    public String getEntitySequenceNumber()
    {
      return entitySequenceNumber;
    }
}
