/*
 * ProtocolExceptionBean.java
 *
 * Created on March 19, 2010, 4:48 PM
 
 */

package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.bean.IBaseDataBean;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


/**
 *
 * @author sreenathv
 */
public class ProtocolExceptionBean  implements java.io.Serializable, IBaseDataBean {
    
    private  String protocolNumber;
    private int sequenceNumber;
    private String acType;
    private String updateUser;
    private java.sql.Timestamp updateTimestamp;
    
   //COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC
   private int speciesId;
   private int exceptionCategoryCode;
   private String exceptionDescription;
   private int exceptionId;
   private String  exceptionCategoryDesc;
   
   private PropertyChangeSupport propertySupport;
   private static final String EXCEPTION_CATEGORY_CODE = "exceptionCategoryCode";
   private static final String EXCEPTION_DESCRIPTION = "exceptionDescription";
           
    /** Creates a new instance of ProtocolExceptionBean */
    public ProtocolExceptionBean() {
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

    public int getExceptionCategoryCode() {
        return exceptionCategoryCode;
    }
    
    public void setExceptionCategoryCode(int newExceptionCategoryCode) {
        int oldExceptioncategoryCode = exceptionCategoryCode;
        this.exceptionCategoryCode = newExceptionCategoryCode;
        propertySupport.firePropertyChange(EXCEPTION_CATEGORY_CODE, oldExceptioncategoryCode, exceptionCategoryCode);
    }

    public String getExceptionDescription() {
        return exceptionDescription;
    }

    public void setExceptionDescription(String newExceptionDescription) {
        String oldExceptionDescription = exceptionDescription;
        this.exceptionDescription = newExceptionDescription;
        propertySupport.firePropertyChange(EXCEPTION_DESCRIPTION,oldExceptionDescription, exceptionDescription);
    }

    public int getExceptionId() {
        return exceptionId;
    }

    public void setExceptionId(int exceptionId) {
        this.exceptionId = exceptionId;
    }

    public String getExceptionCategoryDesc() {
        return exceptionCategoryDesc;
    }

    public void setExceptionCategoryDesc(String newExceptionCategoryDesc) {
        String oldExceptionCategoryDesc = exceptionCategoryDesc;
        this.exceptionCategoryDesc = newExceptionCategoryDesc;
        propertySupport.firePropertyChange(EXCEPTION_CATEGORY_CODE, oldExceptionCategoryDesc, exceptionCategoryDesc);
    }
    
    //COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC - start
    /**
     * Method used to get SpeciesId
     * @return int speciesId
     */
    public int getSpeciesId() {
        return speciesId;
    }

    /**
     * Method used to set SpeciesId
     * @param speciesId
     */
    public void setSpeciesId(int speciesId) {
        this.speciesId = speciesId;
    }
   //COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC - end
}
