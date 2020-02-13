/*
 * @(#)DisclosureHeaderBean.java 1.0 3/21/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.coi.bean;

import java.sql.Timestamp;
import edu.mit.coeus.utils.UtilFactory;
/**
 * This class is used to hold the COI disclosure Entity details.
 * It provides get and set methods for each attributes of
 * Entity details for a particular COI Disclosure.
 *
 * @version 1.0 March 21, 2002, 12:32 PM
 * @author  Anil Nandakumar
 */
public class DisclosureInfoBean extends java.lang.Object {

    /**
     * Entity name.
     */
    private String entityName;

    /**
     * Entity number.
     */
    private String entityNumber;

    /**
     * Conflict status description.
     * Corresponds to OSP$COI_STATUS.DESCRIPTION.
     */
    private String conflictStatus;

    /**
     * Conflict status code.
     * Corresponds to OSP$INV_COI_DISC_DETAILS.COI_STATUS_CODE.
     */
    private String conflictStatusCode;

    /**
     * Reviewer.
     */
    private String reviewer;

    /**
     * Reviewer code.
     * Corresponds to OSP$INV_COI_DISC_DETAILS.COI_REVIEWER_CODE.
     */
    private String reviewerCode;

    /**
     * Explanation of status.
     */
    private String desc;

    /**
     * Update user.
     */
    private String updatedBy;

    /**
     * Sequence number.
     */
    private String seqNumber;

    /**
     * Entity sequence number.
     */
    private String entSeqNumber;

    /**
     * Last updated date.
     */
    private Timestamp lastUpdated;
    /** Creates new DisclosureInfoBean */
    public DisclosureInfoBean() {
    }
    /**
     *  Get Entity number
     *  @return String Entity number
     */
    public String getEntityNumber() {
        return entityNumber;
    }
    /**
     *  Set Entity number
     *  @param String Entity number
     */
    public void setEntityNumber(java.lang.String entityNumber) {
        this.entityNumber = UtilFactory.checkNullStr(entityNumber);
    }
    /**
     *  Get Entity sequence number
     *  @return String Entity sequence number
     */
    public String getEntSeqNumber() {
        return entSeqNumber;
    }
    /**
     *  Set Entity sequence number
     *  @param String Entity sequence number
     */
    public void setEntSeqNumber(java.lang.String entSeqNumber) {
        this.entSeqNumber = UtilFactory.checkNullStr(entSeqNumber);
    }
    /**
     *  Get Sequence number
     *  @return String Sequence number
     */
    public String getSeqNumber() {
        return seqNumber;
    }
    /**
     *  Set Sequence number
     *  @param String Sequence number
     */
    public void setSeqNumber(java.lang.String seqNumber) {
        this.seqNumber = UtilFactory.checkNullStr(seqNumber);
    }
    /**
     *  Get Entity name
     *  @return String Entity name
     */
    public String getEntityName() {
        return entityName;
    }
    /**
     *  Set Entity name
     *  @param String Entity name
     */
    public void setEntityName(java.lang.String entityName) {
        this.entityName = UtilFactory.checkNullStr(entityName);
    }
    /**
     *  Get COI Status
     *  @return String COI status
     */
    public String getConflictStatus() {
        return conflictStatus;
    }
    /**
     *  Set COI Status
     *  @param String COI status
     */
    public void setConflictStatus(java.lang.String conflictStatus) {
        this.conflictStatus = UtilFactory.checkNullStr(conflictStatus);
    }
    /**
     *  Get Reviewer
     *  @return String Reviewer
     */
    public String getReviewer() {
        return reviewer;
    }
    /**
     *  Set Reviewer
     *  @param String Reviewer
     */
    public void setReviewer(java.lang.String reviewer) {
        this.reviewer = UtilFactory.checkNullStr(reviewer);
    }
    /**
     *  Get Reviewer code
     *  @return String Reviewer code
     */
    public String getReviewerCode() {
        return reviewerCode;
    }
    /**
     *  Set Reviewer code
     *  @param String Reviewer code
     */
    public void setReviewerCode(java.lang.String reviewerCode) {
        this.reviewerCode = UtilFactory.checkNullStr(reviewerCode);
    }
    /**
     *  Get Description
     *  @return String Description
     */
    public String getDesc() {
        return desc;
    }
    /**
     *  Set Description
     *  @param String Description
     */
    public void setDesc(java.lang.String desc) {
        this.desc = UtilFactory.checkNullStr(desc);
    }
    /**
     *  Get Update user
     *  @return String Update user
     */
    public String getUpdatedBy() {
        return updatedBy;
    }
    /**
     *  Set Update user
     *  @param String Update user
     */
    public void setUpdatedBy(java.lang.String updatedBy) {
        this.updatedBy = UtilFactory.checkNullStr(updatedBy);
    }
    /**
     *  Get Last Updated timestamp
     *  @return Timestamp Last Updated Timestamp
     */
    public Timestamp getLastUpdated() {
        return lastUpdated;
    }
    /**
     *  Set Last Updated Timestamp
     *  @param Timestamp Last Updated Timestamp
     */
    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * Get conflictStatusCode.
     * @return conflictStatusCode
     */
    public String getConflictStatusCode()
    {
      return conflictStatusCode;
    }

    /**
     * Set conflictStatusCode.
     * @param conflictStatusCode
     */
    public void setConflictStatusCode(String conflictStatusCode)
    {
      this.conflictStatusCode = conflictStatusCode;
    }
}
