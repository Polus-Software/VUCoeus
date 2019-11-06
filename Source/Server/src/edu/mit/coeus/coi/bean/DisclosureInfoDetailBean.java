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
 *
 * This class is for getting and setting various details/values
 * concerning disclosures.
 *
 * @version 1.0 April 04, 2002, 6:12 PM
 * @author  Anil Nandakumar
 */
public class DisclosureInfoDetailBean extends java.lang.Object {
    private String entityName;
    private String conflictStatus;
    private String reviewer;
    private String desc;
    /** Creates new DisclosureInfoDetailBean */
    public DisclosureInfoDetailBean() {
    }
    /**
     *  This method gets the Entity Name
     *  @return String entityName
     */
    public String getEntityName() {
        return entityName;
    }
    /**
     *  This method sets the Entity Name
     *  @param String entityName
     */
    public void setEntityName(java.lang.String entityName) {
        this.entityName = UtilFactory.checkNullStr(entityName);
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
}
