/*
 * UnitAdministratorBean.java
 *
 * Created on December 20, 2005, 6:55 PM
 */

package edu.mit.coeus.unit.bean;

import edu.mit.coeus.bean.BaseBean;
import java.sql.Timestamp;


/**
 *
 * @author  tarique
 */
public class UnitAdministratorBean implements BaseBean, java.io.Serializable{
    
    private String unitNumber;
    private int unitAdminTypeCode;
    private String administrator;
    private String personName;
    private String acType;
    private String updateUser;
    private Timestamp updateTimestamp;
  
    private String awUnitNumber;
    private int awUnitAdminTypeCode;
    private String awAdministrator;
    /** Creates a new instance of UnitAdministratorBean */
    public UnitAdministratorBean() {
    }
    
    public boolean isLike(edu.mit.coeus.bean.ComparableBean comparableBean) throws edu.mit.coeus.exception.CoeusException {
        return true;
    }
    
    /**
     * Getter for property unitNumber.
     * @return Value of property unitNumber.
     */
    public java.lang.String getUnitNumber() {
        return unitNumber;
    }
    
    /**
     * Setter for property unitNumber.
     * @param unitNumber New value of property unitNumber.
     */
    public void setUnitNumber(java.lang.String unitNumber) {
        this.unitNumber = unitNumber;
    }
    
    /**
     * Getter for property unitAdminTypeCode.
     * @return Value of property unitAdminTypeCode.
     */
    public int getUnitAdminTypeCode() {
        return unitAdminTypeCode;
    }
    
    /**
     * Setter for property unitAdminTypeCode.
     * @param unitAdminTypeCode New value of property unitAdminTypeCode.
     */
    public void setUnitAdminTypeCode(int unitAdminTypeCode) {
        this.unitAdminTypeCode = unitAdminTypeCode;
    }
    
    /**
     * Getter for property administrator.
     * @return Value of property administrator.
     */
    public java.lang.String getAdministrator() {
        return administrator;
    }
    
    /**
     * Setter for property administrator.
     * @param administrator New value of property administrator.
     */
    public void setAdministrator(java.lang.String administrator) {
        this.administrator = administrator;
    }
    
   
    
    /**
     * Getter for property acType.
     * @return Value of property acType.
     */
    public java.lang.String getAcType() {
        return acType;
    }
    
    /**
     * Setter for property acType.
     * @param acType New value of property acType.
     */
    public void setAcType(java.lang.String acType) {
        this.acType = acType;
    }
    
    /**
     * Getter for property updateUser.
     * @return Value of property updateUser.
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /**
     * Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }
    
    /**
     * Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     */
    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /**
     * Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /**
     * Getter for property awUnitNumber.
     * @return Value of property awUnitNumber.
     */
    public java.lang.String getAwUnitNumber() {
        return awUnitNumber;
    }
    
    /**
     * Setter for property awUnitNumber.
     * @param awUnitNumber New value of property awUnitNumber.
     */
    public void setAwUnitNumber(java.lang.String awUnitNumber) {
        this.awUnitNumber = awUnitNumber;
    }
    
    /**
     * Getter for property awUnitAdminTypeCode.
     * @return Value of property awUnitAdminTypeCode.
     */
    public int getAwUnitAdminTypeCode() {
        return awUnitAdminTypeCode;
    }
    
    /**
     * Setter for property awUnitAdminTypeCode.
     * @param awUnitAdminTypeCode New value of property awUnitAdminTypeCode.
     */
    public void setAwUnitAdminTypeCode(int awUnitAdminTypeCode) {
        this.awUnitAdminTypeCode = awUnitAdminTypeCode;
    }
    
    /**
     * Getter for property awAdministrator.
     * @return Value of property awAdministrator.
     */
    public java.lang.String getAwAdministrator() {
        return awAdministrator;
    }
    
    /**
     * Setter for property awAdministrator.
     * @param awAdministrator New value of property awAdministrator.
     */
    public void setAwAdministrator(java.lang.String awAdministrator) {
        this.awAdministrator = awAdministrator;
    }
    /** 
     * Equals method to check whether Primary Key values and acType are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        UnitAdministratorBean unitAdministratorBean = (UnitAdministratorBean)obj;
        //Modified for COEUSDEV-170 : Array index out of bounds - Start
        //Check is done for acType of the bean
//        if(unitAdministratorBean.getUnitNumber().equals(getUnitNumber()) &&
//            unitAdministratorBean.getUnitAdminTypeCode() == getUnitAdminTypeCode() &&
//            unitAdministratorBean.getAdministrator().equals(getAdministrator())){ 
        String beanAcType = unitAdministratorBean.getAcType();
        beanAcType = beanAcType == null ? "" : beanAcType;
        String acType = getAcType();
        acType = acType == null ? "" : acType;

        if(unitAdministratorBean.getUnitNumber().equals(getUnitNumber()) &&
            unitAdministratorBean.getUnitAdminTypeCode() == getUnitAdminTypeCode() &&
            unitAdministratorBean.getAdministrator().equals(getAdministrator()) && 
            beanAcType.equals(acType)){  //COEUSDEV-170 : END
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * Getter for property personName.
     * @return Value of property personName.
     */
    public java.lang.String getPersonName() {
        return personName;
    }
    
    /**
     * Setter for property personName.
     * @param personName New value of property personName.
     */
    public void setPersonName(java.lang.String personName) {
        this.personName = personName;
    }
    
}
