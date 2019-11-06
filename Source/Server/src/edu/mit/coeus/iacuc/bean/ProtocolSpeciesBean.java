/*
 * ProtocolSpeciesBean.java
 *
 * Created on March 18, 2010, 12:31 PM
 *
 */

/* PMD check performed, and commented unused imports and variables on 15-NOV-2010
 * by Johncy M John
 */

package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.bean.IBaseDataBean;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Vector;

/**
 *
 * @author sreenathv
 */
public class ProtocolSpeciesBean implements java.io.Serializable, IBaseDataBean {
    
   private  String protocolNumber;
   private int sequenceNumber;
   private String acType;
   private String updateUser;
   private java.sql.Timestamp updateTimestamp;
  
   private int speciesId;
   private int awSpeciesId;
   private boolean usdaCovered;
   private int speciesCode;  
   private String speciesName;            
   private int speciesCount;
   //Added for-COEUSQA-2798 Add count type to species/group screen-Start
   private int speciesCountTypeCode;
   private String speciesCountTypeName;
   private static final String COUNT_TYPE_CODE = "countTypeCode";
   //Added for-COEUSQA-2798 Add count type to species/group screen-End    
   private String strain;
   private String speciesGroupName;//COEUSQA-2551
   private PropertyChangeSupport propertySupport;
   private static final String SPECIES_CODE = "speciesCode";
   private static final String USDA_COVERED = "usdaCovered";
   private static final String STRAIN = "strain";
   private static final String SPECIES_COUNT = "speciesCount";
   private static final String SPECIES_GROUP_NAME = "speciesGroupName";//COEUSQA-2551
   
   //COEUSQA-2627_Move pain category in IACUC protocol to Protocol Species/Group level_start
   private int painCategoryCode;
   private String painCategoryName;
   private static final String PAIN_CATEGORY_CODE = "painCategoryCode";
   //COEUSQA-2627_Move pain category in IACUC protocol to Protocol Species/Group level_end
   
   //COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC
   private boolean exceptionsPresent;
   private static final String EXCEPTION_PRESENT = "exceptionsPresent";
   private Vector speciesExceptions;
   
   /** Creates a new instance of ProtocolSpeciesBean */
   public ProtocolSpeciesBean() {
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
   
    public String getProtocolNumber() {
        return protocolNumber;
    }

    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getAcType() {
        return acType;
    }

    public void setAcType(String acType) {
        this.acType = acType;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public int getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(int speciesId) {
        this.speciesId = speciesId;
    }

    public int getAwSpeciesId() {
        return awSpeciesId;
    }

    public void setAwSpeciesId(int awSpeciesId) {
        this.awSpeciesId = awSpeciesId;
    }

    public boolean isUsdaCovered() {
        return usdaCovered;
    }

    public void setUsdaCovered(boolean newUsdaCovered) {
        boolean oldUsdaCovered = usdaCovered;
        this.usdaCovered = newUsdaCovered;
        propertySupport.firePropertyChange(USDA_COVERED,oldUsdaCovered, usdaCovered);
    }

    public int getSpeciesCode() {
        return speciesCode;
    }

    public void setSpeciesCode(int newSpeciesCode) {
        int oldSpeciesCode = speciesCode;
        this.speciesCode = newSpeciesCode;
        propertySupport.firePropertyChange(SPECIES_CODE,oldSpeciesCode, speciesCode);
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String newSpeciesName) {
        String oldSpeciesName = speciesName;
        this.speciesName = newSpeciesName;
        propertySupport.firePropertyChange(SPECIES_CODE,oldSpeciesName, speciesName);
    }

    public int getSpeciesCount() {
        return speciesCount;
    }

    public void setSpeciesCount(int newSpeciesCount) {
        int oldSpeciesCount = speciesCount;
        this.speciesCount = newSpeciesCount;
        propertySupport.firePropertyChange(SPECIES_COUNT,oldSpeciesCount, speciesCount);

    }

    public String getStrain() {
        return strain;
    }

    public void setStrain(String newStrain) {
        String oldStrain = strain;
        this.strain = newStrain;
        propertySupport.firePropertyChange(STRAIN,oldStrain, strain);
    }
    
    //Added with COEUSQA-2551:Rework how user enters species, study groups, and procedures in IACUC protocols
    /**
     * 
     * @return speciesGroupName
     */
    public String getSpeciesGroupName() {
        return speciesGroupName;
    }

    /**
     * 
     * @param newSpeciesGroupName 
     */
    public void setSpeciesGroupName(String newSpeciesGroupName) {
        String oldSpeciesGroupName = speciesGroupName;
        this.speciesGroupName = newSpeciesGroupName;
        propertySupport.firePropertyChange(SPECIES_GROUP_NAME,oldSpeciesGroupName, speciesGroupName);
    }
    
    // Added for COEUSQA-2627_Move pain category in IACUC protocol to Protocol Species/Group level_start
    /**
     * Method used to get PainCategoryCode
     * @return painCategoryCode
     */
    public int getPainCategoryCode() {
        return painCategoryCode;
    }
    /**
     * Method used to set PainCategoryCode
     * @param newPainCategoryCode
     */
    public void setPainCategoryCode(int newPainCategoryCode) {
        
         int oldPainCategoryCode = painCategoryCode;
        this.painCategoryCode = newPainCategoryCode;
        propertySupport.firePropertyChange(PAIN_CATEGORY_CODE,oldPainCategoryCode, painCategoryCode);
        
        
    }
     
    /**
     * Method used to get PainCategoryName
     * @return painCategoryName
     */
     public String getPainCategoryName() {
        return painCategoryName;
    }

    /**
     * Method used to set PainCategoryName
     * @return newPainCategoryName
     */ 
    public void setPainCategoryName(String newPainCategoryName) {
        
        String oldPainCategoryName = painCategoryName;
        this.painCategoryName = newPainCategoryName;
        propertySupport.firePropertyChange(PAIN_CATEGORY_CODE,oldPainCategoryName, painCategoryName);
        
        
    }
    
    // Added for COEUSQA-2627_Move pain category in IACUC protocol to Protocol Species/Group level_end
    
    //Added for-COEUSQA-2798 Add count type to species/group screen-Start
     /**
     * Method used to get speciesCountTypeCode
     * @return speciesCountTypeCode
     */
    public int getSpeciesCountTypeCode() {
        return speciesCountTypeCode;
    }
    /**
     * Method used to set speciesCountTypeCode
     * @param speciesCountTypeCode
     */
    public void setSpeciesCountTypeCode(int newSpeciesCountTypeCode) {
        
         int oldspeciesCountTypeCode = speciesCountTypeCode;
        this.speciesCountTypeCode = newSpeciesCountTypeCode;
        propertySupport.firePropertyChange(COUNT_TYPE_CODE,oldspeciesCountTypeCode, speciesCountTypeCode);
        
        
    }
    
    /**
     * Method used to get speciesCountTypeName
     * @return speciesCountTypeName
     */
     public String getspeciesCountTypeName() {
        return speciesCountTypeName;
    }

    /**
     * Method used to set PainCategoryName
     * @return newPainCategoryName
     */ 
    public void setSpeciesCountTypeName(String newSpeciesCountTypeName) {
        
        String oldSpeciesCountTypeName = speciesCountTypeName;
        this.speciesCountTypeName = newSpeciesCountTypeName;
        propertySupport.firePropertyChange(COUNT_TYPE_CODE,oldSpeciesCountTypeName, speciesCountTypeName);
        
        
    }
    //Added for-COEUSQA-2798 Add count type to species/group screen-End
    
    //COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC - start    
    /**
     * Method used to check whether Exceptions Present or not
     * @return boolean exceptionsPresent
     */
    public boolean isExceptionsPresent() {
        return exceptionsPresent;
    }

    /**
     * Method used to set ExceptionsPresent
     * @return newExceptionsPresent
     */ 
    public void setExceptionsPresent(boolean newExceptionsPresent) {
        boolean oldExceptionsPresent = exceptionsPresent;
        this.exceptionsPresent = newExceptionsPresent;
        propertySupport.firePropertyChange(EXCEPTION_PRESENT,oldExceptionsPresent, exceptionsPresent);
    }
    
    /**
     * Method used to get the speciesExceptions
     * @return speciesExceptions Vector
     */
    public java.util.Vector getSpeciesExceptions(){
        return speciesExceptions;
        
    }
    /**
     * Method used to set the speciesExceptions
     * @param speciesExceptions Vector
     */
    public void setSpeciesExceptions(java.util.Vector speciesExceptions){
        this.speciesExceptions = speciesExceptions;
    }
    //COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC - end
}
