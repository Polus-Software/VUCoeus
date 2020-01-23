/*
 * ProtocolStudyGroupBean.java
 *
 * Created on March 18, 2010, 5:44 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved
 */
/* PMD check performed, and commented unused imports and variables on 09-MARCH-2011
 * by Md.Ehtesham Ansari
 */
package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.bean.IBaseDataBean;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Vector;


/**
 *
 * @author satheeshkumarkn
 */
public class ProtocolStudyGroupBean implements java.io.Serializable , IBaseDataBean{
    
    private String protocolNumber;
    private int sequenceNumber;
    private int studyGroupId;
    //Commented with CoeusQQA-2551:Rework how user enters species,study groups and procedures in IACUC protocols
//    private String studyGroupName;
    private int speciesCode;
    private String speciesName;
    private int procedureCategoryCode;
    private String procedureCategoryName;
    private int procedureCode;
    private String procedureName;
    private int painCategoryCode;
    private String painCategoryName;
    private int speciesCount;
    private java.sql.Timestamp updateTimestamp;
    private String updateUser;
    private Vector vecLocation;
    private Vector vecOtherDetails;
    //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
    private Vector vecPersonsResponsible;
    //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
    private String acType;
    private boolean isOtherDetailsPresent;
    private int speciesId;//coeusQA:2551 - Introduced species Id
    private PropertyChangeSupport propertySupport;    
    private static final String PROCEDURE_CAT_CODE = "procedureCategoryCode";
    private static final String PROCEDURE_CODE = "procedureCode";
    private static final String PAIN_CAT_CODE = "painCategoryCode";
    private static final String SPECIES_COUNT = "speciesCount";
    private static final String SPECIES_ID = "speciesId";
    private String speciesGroupName;//COEUSQA-2551

    public void setSpeciesGroupName(String speciesGroupName) {
        this.speciesGroupName = speciesGroupName;
    }

    public String getSpeciesGroupName() {
        return speciesGroupName;
    }

    /** Creates a new instance of ProtocolStudyGroupBean */
    public ProtocolStudyGroupBean() {
        propertySupport = new PropertyChangeSupport(this);
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
    
    /*
     * Getter method for Protocol number
     * @return protocolnumber - String
     */
    public String getProtocolNumber() {
        return protocolNumber;
    }
    
    /*
     * Setter method for Protocol number
     * @param protocolNumber - String
     */
    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }
    
    /*
     * Getter method for protocol sequence number
     * @return sequenceNumber - int
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }
    
    /*
     * Setter method for protocol sequence numbert
     * @param sequenceNumber - int
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    
    /*
     * Getter method to get the study group Id
     * @return studyGroupId - int
     */
    public int getStudyGroupId() {
        return studyGroupId;
    }
    
    /*
     * Setter method for study group Id
     * @param studyGroupId - int
     */
    public void setStudyGroupId(int studyGroupId) {
        this.studyGroupId = studyGroupId;
    }
    //Commented with CoeusQQA-2551:Rework how user enters species,study groups and procedures in IACUC protocols
    /*
     * Getter method for study group name
     * @return studyGroupName - String
     */
//    public String getStudyGroupName() {
//        return studyGroupName;
//    }
    
    /*
     * Setter method for study group name
     * @param studyGroupName - String
     */
//     public void setStudyGroupName(String studyGroupName) {
//        this.studyGroupName = studyGroupName;
//     }
    
    /*
     * Getter method for species code
     * @return speciesCode - int
     */
    public int getSpeciesCode() {
        return speciesCode;
    }
    
    /*
     * Setter method for species code
     * @param speciesCode - int
     */
    public void setSpeciesCode(int speciesCode) {
        this.speciesCode = speciesCode;
    }
    
    /*
     * Getter method for procedure category code
     * @return procedureCategoryCode - int
     */
    public int getProcedureCategoryCode() {
        return procedureCategoryCode;
    }
    
    /*
     * Setter method for procedure category code
     * @param procedureCategoryCode - int
     */
    public void setProcedureCategoryCode(int newProcedureCategoryCode) {
        int oldProcedureCategoryCode = procedureCategoryCode;
        this.procedureCategoryCode = newProcedureCategoryCode;
        propertySupport.firePropertyChange(PROCEDURE_CAT_CODE,oldProcedureCategoryCode, procedureCategoryCode);
    }
    
    /*
     * Getter method for procedure code
     * @return procedureCode - int
     */
    public int getProcedureCode() {
        return procedureCode;
    }
    
    /*
     * Setter method for procedure code
     * @param procedureCode - int
     */
    public void setProcedureCode(int newProcedureCode) {
        int oldProcedureCode = procedureCode;
        this.procedureCode = newProcedureCode;
        propertySupport.firePropertyChange(PROCEDURE_CODE,oldProcedureCode, procedureCode);
    }
    
    /*
     * Getter method for pain category code
     * @return painCategoryCode
     */
    public int getPainCategoryCode() {
        return painCategoryCode;
    }
    
    /*
     * Setter method foe pain category code
     * @param painCategoryCode - int
     */
    public void setPainCategoryCode(int newPainCategoryCode) {
        int oldPainCategoryCode = painCategoryCode;
        this.painCategoryCode = newPainCategoryCode;
        propertySupport.firePropertyChange(PAIN_CAT_CODE,oldPainCategoryCode, painCategoryCode);
    }
    
    /*
     * Getter method for species speciesCount
     * @param speciesCount - int
     */
    public int getSpeciesCount() {
        return speciesCount;
    }
    
    /*
     * Setter method for species count
     * @return speciesCount - int
     */
    public void setSpeciesCount(int newSpeciesCount) {
        int oldSpeciesCount = speciesCount;
        this.speciesCount = newSpeciesCount;
        propertySupport.firePropertyChange(SPECIES_COUNT,oldSpeciesCount, speciesCount);
        
    }    
    /*
     * Getter method for update timestamp
     * @return updateTimestamp - java.sql.Timestamp
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /*
     * Setter method for update timestamp
     * @param updateTimestamp - java.sql.Timestamp
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /*
     * Getter method for update user id
     * @return updateUser - String
     */
    public String getUpdateUser() {
        return updateUser;
    }
    
    /*
     * Setter method for update user id
     * @param updateUser
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    
    /*
     * Getter method for locations
     * @return vecLocation - Vector
     */
    public Vector getLocations() {
        return vecLocation;
    }
    
    /*
     * Setter method for locations
     * @param vecLocation - Vector
     */
    public void setLocations(Vector vecLocation) {
        this.vecLocation = vecLocation;
    }
    
    /*
     * Getter method for species name
     * @retuen speciesName - String
     */
    public String getSpeciesName() {
        return speciesName;
    }
    
    /*
     * Setter method for species name
     * @param speciesName - String
     */
    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }
    
    /*
     * Getter method for procedure category name
     * @return procedureCategoryName - String
     */
    public String getProcedureCategoryName() {
        return procedureCategoryName;
    }
    
    /*
     * Setter method for procedure category name
     * @param procedureCategoryName - String
     */
    public void setProcedureCategoryName(String procedureCategoryName) {
        this.procedureCategoryName = procedureCategoryName;
    }
    
    /*
     * Getter method for procedure name
     * @return procedureName - String
     */
    public String getProcedureName() {
        return procedureName;
    }
    
    /*
     * Setter method for procedure Name
     * @param procedureName - String
     */
    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }
    
    /*
     * Getter method for pain category name
     * @return painCategoryName - String
     */
    public String getPainCategoryName() {
        return painCategoryName;
    }
    
    /*
     * Setter method for pain category name
     * @param painCategoryName - String
     */
    public void setPainCategoryName(String painCategoryName) {
        this.painCategoryName = painCategoryName;
    }
    
    /*
     * Getter method for other details (Custom elements)
     * @return vecOtherDetails - Vector
     */
    public Vector getOtherDetails() {
        return vecOtherDetails;
    }
    
    /*
     * Setter method for other details
     * @param vecOtherDetails - Vector
     */
    public void setOtherDetails(Vector vecOtherDetails) {
        this.vecOtherDetails = vecOtherDetails;
    }
    
    /*
     * Method to get the actype of the bean
     * @return acType - String
     */
    public String getAcType() {
        return acType;
    }

    /*
     * Method to set acType For the bean
     * @param acType - String
     */
    public void setAcType(String acType) {
        this.acType = acType;
    }

    public boolean isOtherDetailsPresent() {
        return isOtherDetailsPresent;
    }

    public void setOtherDetailsPresent(boolean isOtherDetailsPresent) {
        this.isOtherDetailsPresent = isOtherDetailsPresent;
    }

    //Added with CoeusQA-2551:Rework how user enters species,study groups and procedures in IACUC protocols
    public int getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(int newSpeciesId) {
        int oldSpeciesId = speciesId;
        this.speciesId = newSpeciesId;
        propertySupport.firePropertyChange(SPECIES_ID,oldSpeciesId, speciesId);
    }
    //CoeusQA-2551 End

    //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -Start
    /*
     * Method to get the persons responsible
     * @return vecPersonsResponsible - Vector
     */
    public Vector getPersonsResponsible() {
        return vecPersonsResponsible;
    }

    /*
     * Method to set person responsible data
     * @param vecPersonsResponsible - Vector
     */
    public void setPersonsResponsible(Vector vecPersonsresponsible) {
        this.vecPersonsResponsible = vecPersonsresponsible;
    }
    //Added for COEUSQA-2633 Ability to indicate which procedures IACUC study personnel will perform and training -End
}
