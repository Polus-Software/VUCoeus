/* 
 * @(#)DepartmentPersonTrainingBean.java 1.0 03/11/03 9:56 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.departmental.bean;

import java.beans.*;
import java.sql.Timestamp;
import java.sql.Date;
import edu.mit.coeus.bean.BaseBean;
/**
 * The class used to hold the information of <code>Department Person Training</code>
 *
 * @author  Prasanna Kumar K
 * @version 1.0
 * Created on March 09, 2004, 9:56 AM
 */

public class DepartmentPersonTrainingBean implements java.io.Serializable, BaseBean {
     
    //holds the person id
    private String personId;    
    
    private int trainingNumber;
    
    private int aw_TrainingNumber;
    
    private int trainingCode;

    //holds the date requested
    private Date dateRequested;
    
    //holds the date Submitted
    private Date dateSubmitted;    
    
    //holds the date Acknowledged
    private Date dateAcknowledged;    

    //holds the follow up date
    private Date followUpDate;
    
    //holds score
    private String score;
    
    //holds comments
    private String comments;
    
    //holds update user id
    private String updateUser;

    //holds update timestamp
    private Timestamp updateTimestamp;

    //holds account type
    private String acType;
    
    //COEUSQA:3537 - Coeus IACUC Training Revisions - Start
    private String speciesType;
    private String procedureType;  
    //COEUSQA:3537 - End
    
    public DepartmentPersonTrainingBean(){
        
    }
    
    /** Getter for property personId.
     * @return Value of property personId.
     */
    public java.lang.String getPersonId() {
        return personId;
    }    
    
    /** Setter for property personId.
     * @param personId New value of property personId.
     */
    public void setPersonId(java.lang.String personId) {
        this.personId = personId;
    }
    
    /** Getter for property trainingNumber.
     * @return Value of property trainingNumber.
     */
    public int getTrainingNumber() {
        return trainingNumber;
    }
    
    /** Setter for property trainingNumber.
     * @param trainingNumber New value of property trainingNumber.
     */
    public void setTrainingNumber(int trainingNumber) {
        this.trainingNumber = trainingNumber;
    }
    
    /** Getter for property trainingCode.
     * @return Value of property trainingCode.
     */
    public int getTrainingCode() {
        return trainingCode;
    }
    
    /** Setter for property trainingCode.
     * @param trainingCode New value of property trainingCode.
     */
    public void setTrainingCode(int trainingCode) {
        this.trainingCode = trainingCode;
    }
    
    /** Getter for property dateRequested.
     * @return Value of property dateRequested.
     */
    public java.sql.Date getDateRequested() {
        return dateRequested;
    }
    
    /** Setter for property dateRequested.
     * @param dateRequested New value of property dateRequested.
     */
    public void setDateRequested(java.sql.Date dateRequested) {
        this.dateRequested = dateRequested;
    }
    
    /** Getter for property dateSubmitted.
     * @return Value of property dateSubmitted.
     */
    public java.sql.Date getDateSubmitted() {
        return dateSubmitted;
    }
    
    /** Setter for property dateSubmitted.
     * @param dateSubmitted New value of property dateSubmitted.
     */
    public void setDateSubmitted(java.sql.Date dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }
    
    /** Getter for property dateAcknowledged.
     * @return Value of property dateAcknowledged.
     */
    public java.sql.Date getDateAcknowledged() {
        return dateAcknowledged;
    }
    
    /** Setter for property dateAcknowledged.
     * @param dateAcknowledged New value of property dateAcknowledged.
     */
    public void setDateAcknowledged(java.sql.Date dateAcknowledged) {
        this.dateAcknowledged = dateAcknowledged;
    }
    
    /** Getter for property followUpDate.
     * @return Value of property followUpDate.
     */
    public java.sql.Date getFollowUpDate() {
        return followUpDate;
    }
    
    /** Setter for property followUpDate.
     * @param followUpDate New value of property followUpDate.
     */
    public void setFollowUpDate(java.sql.Date followUpDate) {
        this.followUpDate = followUpDate;
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
    
    /** Getter for property score.
     * @return Value of property score.
     */
    public java.lang.String getScore() {
        return score;
    }
    
    /** Setter for property score.
     * @param score New value of property score.
     */
    public void setScore(java.lang.String score) {
        this.score = score;
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
    public void setComments(java.lang.String comments) {
        this.comments = comments;
    }    
    
    /** Getter for property aw_TrainingNumber.
     * @return Value of property aw_TrainingNumber.
     */
    public int getAw_TrainingNumber() {
        return aw_TrainingNumber;
    }
    
    /** Setter for property aw_TrainingNumber.
     * @param aw_TrainingNumber New value of property aw_TrainingNumber.
     */
    public void setAw_TrainingNumber(int aw_TrainingNumber) {
        this.aw_TrainingNumber = aw_TrainingNumber;
    }
    
    /**
    * Method used to get the species Type
    * @return speciesType String
    */
    public String getSpeciesType() {
        return speciesType;
    }

    /**
     * Method used to set the species Type
     * @param speciesType String
     */
    public void setSpeciesType(String speciesType) {
        this.speciesType = speciesType;
    }

   /**
    * Method used to get the procedure Type
    * @return procedureType String
    */
    public String getProcedureType() {
        return procedureType;
    }
    
    /**
     * Method used to set the procedure Type
     * @param procedureType String
     */
    public void setProcedureType(String procedureType) {
        this.procedureType = procedureType;
    }
    
}
