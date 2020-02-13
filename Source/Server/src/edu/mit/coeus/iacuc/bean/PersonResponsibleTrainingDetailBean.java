/*
 * PersonResponsibleTrainingDetailBean.java
 *
 * Created on January 7, 2011, 6:24 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/* PMD check performed, and commented unused imports and variables on 09-MARCH-2011
 * by Md.Ehtesham Ansari
 */
package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.bean.BaseBean;

/**
 *
 * @author mdehtesham
 */
public class PersonResponsibleTrainingDetailBean implements java.io.Serializable, BaseBean{   
    private String trainingDescription;
    private String speciesType;
    private String procedureType;  
    private java.sql.Date dateRequested;
    private java.sql.Date dateSubmitted;
    private java.sql.Date dateAcknowledge;
    private java.sql.Date followupDate;
    
    //COEUSQA:3537 - Coeus IACUC Training Revisions - Start
    private String procedureCategory;  
    //COEUSQA:3537 - End
    

   /**
    * Method used to get the training Description
    * @return trainingDescription String
    */
    public String getTrainingDescription() {
        return trainingDescription;
    }

    /**
     * Method used to set the training Description
     * @param trainingDescription String
     */
    public void setTrainingDescription(String trainingDescription) {
        this.trainingDescription = trainingDescription;
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

   /**
    * Method used to get the date Requested
    * @return dateRequested Date
    */
    public java.sql.Date getDateRequested() {
        return dateRequested;
    }

    /**
     * Method used to set the date Requested
     * @param dateRequested Date
     */
    public void setDateRequested(java.sql.Date dateRequested) {
        this.dateRequested = dateRequested;
    }

   /**
    * Method used to get the date Submitted
    * @return dateSubmitted Date
    */
    public java.sql.Date getDateSubmitted() {
        return dateSubmitted;
    }

   /**
     * Method used to set the date Submitted
     * @param dateSubmitted Date
     */
    public void setDateSubmitted(java.sql.Date dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

   /**
    * Method used to get the date Acknowledge
    * @return dateAcknowledge Date
    */
    public java.sql.Date getDateAcknowledge() {
        return dateAcknowledge;
    }

    /**
     * Method used to set the training Description
     * @param dateAcknowledge Date
     */
    public void setDateAcknowledge(java.sql.Date dateAcknowledge) {
        this.dateAcknowledge = dateAcknowledge;
    }

   /**
    * Method used to get the followup Date
    * @return followupDate Date
    */
    public java.sql.Date getFollowupDate() {
        return followupDate;
    }

    /**
     * Method used to set the followup Date
     * @param followupDate Date
     */
    public void setFollowupDate(java.sql.Date followupDate) {
        this.followupDate = followupDate;
    }
    
    
    /**
    * Method used to get the procedure Category
    * @return procedureCategory String
    */
    public String getProcedureCategory() {
        return procedureCategory;
    }

   /**
     * Method used to set the procedure Category
     * @param procedureCategory String
     */
    public void setProcedureCategory(String procedureCategory) {
        this.procedureCategory = procedureCategory;
    }
    
}
