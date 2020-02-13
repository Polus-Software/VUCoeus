/*
 * @(#)AnnDisclosureDetailsBean.java 1.0 6/6/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.coi.bean;
import edu.mit.coeus.utils.UtilFactory;

/**
 * <code>AnnDisclosureDetailsBean</code> is a class to hold the information
 * pertaining to Annual Disclosure.
 *
 * @version 1.0 June 6,2002
 * @author Phaneendra Kumar.
 */
public class AnnDisclosureDetailsBean {

    private String disclosureNumber;
    private String entityNumber;
    private String conflictStatus;
    private String type;
    private String disclosureFor;
    private String number;
    private String sponsor;
    private String title;
    private String entitySeqNumber;
    private String sequenceNumber;
    private String description;
    private String updateUser;
    private String updateTimeStamp;

    /** Creates new AnnDisclosreDetailsBean
     */
    public AnnDisclosureDetailsBean() {
    }

    /**
     * gets the Disclosure Number of the Disclosure
     * @return disclosureNumber of the Disclosure
     */
    public String getDisclosureNumber(){
        return disclosureNumber;
    }

    /**
     * sets the Disclosure Number to the Disclosure Info
     * @param disclosureNumber
     */
    public void setDisclosureNumber(String disclosureNumber){
        this.disclosureNumber = UtilFactory.checkNullStr(disclosureNumber);
    }

    /**
     * gets the EntityNumber Number of the Disclosure
     * @return entityNumber
     */
    public String getEntityNumber(){
        return entityNumber;
    }

    /**
     * sets the Entity Number of the Disclosure
     * @param entityNumber
     */
    public void setEntityNumber(String entityNumber){
        this.entityNumber = UtilFactory.checkNullStr(entityNumber);
    }

    /**
     * gets the Conflict Status of the Award or Proposal
     * @return conflictStatus
     */
    public String getConflictStatus(){
        return conflictStatus;
    }

    /**
     * sets the Conflict Status of the Award or Proposal
     * @param conflictStatus
     */
    public void setConflictStatus(String conflictStatus){
        this.conflictStatus = UtilFactory.checkNullStr(conflictStatus);
    }

    /**
     * gets the Type of the Disclosure
     * @return type
     */
    public String getType(){
        return type;
    }

    /**
     * sets the Type information of the Disclosure
     * @param type
     */
    public void setType(String type){
        this.type = UtilFactory.checkNullStr(type);
    }

    /**
     * gets the Disclosure For as either Award or Proposal of the Disclosure
     * @return disclosureFor
     */
    public String getDisclosureFor(){
        return disclosureFor;
    }

    /**
     * sets the Disclosure For as either Award or Proposal
     * @param disclosureFor
     */
    public void setDisclosureFor(String disclosureFor){
        this.disclosureFor = UtilFactory.checkNullStr(disclosureFor);
    }

    /**
     * gets the Number of either Award or Proposal
     * @return number
     */
    public String getNumber(){
        return number;
    }

    /**
     * sets the Number of either Award or Proposal
     * @param number
     */
    public void setNumber(String number){
        this.number = UtilFactory.checkNullStr(number);
    }

    /**
     * gets the sponsor for the Award or Proposal
     * @return sponsor
     */
    public String getSponsor(){
        return sponsor;
    }

    /**
     * sets the sponsor for the Award or Proposal
     * @param sponsor
     */
    public void setSponsor(String sponsor){
        this.sponsor = UtilFactory.checkNullStr(sponsor);
    }

    /**
     * gets the title information of the Award or Proposal
     * @return title
     */
    public String getTitle(){
        return title;
    }

    /**
     * sets the title information of the Award or Proposal
     * @param title
     */
    public void setTitle(String title){
        this.title = UtilFactory.checkNullStr(title);
    }

    /**
     * gets the EntitySequenceNumber of the Disclosure
     * @return entitySeqNumber
     */
    public String getEntitySeqNumber(){
        return entitySeqNumber;
    }

    /**
     * set the EntitySequenceNumber of the Disclosure
     * @param entitySeqNumber
     */
    public void setEntitySeqNumber(String entitySeqNumber){
        this.entitySeqNumber = UtilFactory.checkNullStr(entitySeqNumber);
    }

    /**
     * gets SequenceNumber of the Disclosure
     * @return sequenceNumber
     */
    public String getSequenceNumber(){
        return sequenceNumber;
    }

    /**
     * sets the SequenceNumber of the Disclosure
     * @param sequenceNumber
     */
    public void setSequenceNumber(String sequenceNumber){
        this.sequenceNumber = UtilFactory.checkNullStr(sequenceNumber);
    }

    /**
     * gets the Description of the Disclosure
     * @return description
     */
    public String getDescription(){
        return description;
    }

    /**
     * sets the Description of the Disclosure
     * @param description
     */
    public void setDescription(String description){
        this.description = UtilFactory.checkNullStr(description);
    }

    /**
     * gets the update user information of the Disclosure
     * @return updateUser
     */
    public String getUpdateUser(){
        return updateUser;
    }

    /**
     * sets the updated user information of the Disclosure
     * @param updateUser
     */
    public void setUpdateUser(String updateUser){
        this.updateUser = UtilFactory.checkNullStr(updateUser);
    }

    /**
     * gets the Time stamp
     * @return returns the timestamp at the time of updation
     */
    public String getUpdateTimeStamp(){
        return updateTimeStamp;
    }

    /**
     * sets the time stamp
     * @param updateTimeStamp
     */
    public void setUpdateTimeStamp(String updateTimeStamp){
        this.updateTimeStamp = UtilFactory.checkNullStr(updateTimeStamp);
    }

}

