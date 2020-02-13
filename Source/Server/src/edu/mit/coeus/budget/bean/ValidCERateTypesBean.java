/*
 * @(#)ValidCERateTypesBean.java October 17, 2003, 10:22 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.budget.bean;

import edu.mit.coeus.bean.PrimaryKey;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import java.util.Hashtable;
import edu.mit.coeus.exception.CoeusException;
/**
 * The class used to hold the information of <code>Valid Cost Elements Rate Types</code>
 *
 * @author  Prasanna Kumar
 * @version 1.0
 * Created on October 17, 2003, 10:22 AM
 */
public class ValidCERateTypesBean implements CoeusBean, java.io.Serializable{

    //holds costelement
    private String costElement = null;
    //holds rate Class Code
    private int rateClassCode = -1;
    //holds rateClassDescription
    private String rateClassDescription = null;    
    //holds rate Type Code
    private int rateTypeCode = -1;  
    //holds rateTypeDescription
    private String rateTypeDescription = null;
    //holds update user id
    private String updateUser = null;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp = null;
    //holds actype
    private String acType = null;
    //holds rateClassType
    private String rateClassType = null;
    
    /** Creates a new instance of BudgetInfo */
    public ValidCERateTypesBean() {
    }      
    
   
    /** Getter for property costElement.
     * @return Value of property costElement.
     */
    public java.lang.String getCostElement() {
        return costElement;
    }    

    /** Setter for property costElement.
     * @param costElement New value of property costElement.
     */
    public void setCostElement(java.lang.String costElement) {
        this.costElement = costElement;
    }    
    
    /** Getter for property rateClassCode.
     * @return Value of property rateClassCode.
     */
    public int getRateClassCode() {
        return rateClassCode;
    }
    
    /** Setter for property rateClassCode.
     * @param rateClassCode New value of property rateClassCode.
     */
    public void setRateClassCode(int rateClassCode) {
        this.rateClassCode = rateClassCode;
    }
    
    /** Getter for property rateClassDescription.
     * @return Value of property rateClassDescription.
     */
    public java.lang.String getRateClassDescription() {
        return rateClassDescription;
    }
    
    /** Setter for property rateClassDescription.
     * @param rateClassDescription New value of property rateClassDescription.
     */
    public void setRateClassDescription(java.lang.String rateClassDescription) {
        this.rateClassDescription = rateClassDescription;
    }
    
    /** Getter for property rateTypeCode.
     * @return Value of property rateTypeCode.
     */
    public int getRateTypeCode() {
        return rateTypeCode;
    }
    
    /** Setter for property rateTypeCode.
     * @param rateTypeCode New value of property rateTypeCode.
     */
    public void setRateTypeCode(int rateTypeCode) {
        this.rateTypeCode = rateTypeCode;
    }
    
    /** Getter for property rateTypeDescription.
     * @return Value of property rateTypeDescription.
     */
    public java.lang.String getRateTypeDescription() {
        return rateTypeDescription;
    }
    
    /** Setter for property rateTypeDescription.
     * @param rateTypeDescription New value of property rateTypeDescription.
     */
    public void setRateTypeDescription(java.lang.String rateTypeDescription) {
        this.rateTypeDescription = rateTypeDescription;
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
    
      // Added by Chandra 04/10/2003 - start
    public boolean equals(Object obj) {
        ValidCERateTypesBean validCERateTypesBean = (ValidCERateTypesBean)obj;
        if(validCERateTypesBean.getCostElement().equals(getCostElement()) &&
        validCERateTypesBean.getRateClassCode() == getRateClassCode()&&
        validCERateTypesBean.getRateTypeCode()==getRateTypeCode()){
            return true;
        }else{
            return false;
        }
    }
    // Added by Chandra 04/10/2003 - end
    
    /** Getter for property rateClassType.
     * @return Value of property rateClassType.
     */
    public java.lang.String getRateClassType() {
        return rateClassType;
    }
    
    /** Setter for property rateClassType.
     * @param rateClassType New value of property rateClassType.
     */
    public void setRateClassType(java.lang.String rateClassType) {
        this.rateClassType = rateClassType;
    }      

    public boolean isLike(ComparableBean comparableBean) throws CoeusException{
        boolean success = false;
        if(comparableBean instanceof ValidCERateTypesBean){
            ValidCERateTypesBean validCERateTypesBean = (ValidCERateTypesBean)comparableBean;
            //Cost Element
            if(validCERateTypesBean.getCostElement()!=null){
                if(getCostElement().equals(validCERateTypesBean.getCostElement())){
                    success = true;
                }else{
                    return false;
                }
            }
            //Cost Element
            if(validCERateTypesBean.getRateClassCode()!=-1){
                if(getRateClassCode()==validCERateTypesBean.getRateClassCode()){
                    success = true;
                }else{
                    return false;
                }
            }
            //Cost Element
            if(validCERateTypesBean.getRateClassType()!=null){
                if(getRateClassType().equals(validCERateTypesBean.getRateClassType())){
                    success = true;
                }else{
                    return false;
                }
            }
            //Cost Element
            if(validCERateTypesBean.getRateClassDescription()!=null){
                if(getRateClassDescription().equals(validCERateTypesBean.getRateClassDescription())){
                    success = true;
                }else{
                    return false;
                }
            }
            //Cost Element
            if(validCERateTypesBean.getRateTypeCode()!=-1){
                if(getRateTypeCode()==validCERateTypesBean.getRateTypeCode()){
                    success = true;
                }else{
                    return false;
                }
            }
            //Cost Element
            if(validCERateTypesBean.getRateTypeDescription()!=null){
                if(getRateTypeDescription().equals(validCERateTypesBean.getRateTypeDescription())){
                    success = true;
                }else{
                    return false;
                }
            }
            //Cost Element
            if(validCERateTypesBean.getUpdateUser()!=null){
                if(getUpdateUser().equals(validCERateTypesBean.getUpdateUser())){
                    success = true;
                }else{
                    return false;
                }
            }
            //Cost Element
            if(validCERateTypesBean.getUpdateTimestamp()!=null){
                if(getUpdateTimestamp().equals(validCERateTypesBean.getUpdateTimestamp())){
                    success = true;
                }else{
                    return false;
                }
            }
            //Cost Element
            if(validCERateTypesBean.getAcType()!=null){
                if(getAcType().equals(validCERateTypesBean.getAcType())){
                    success = true;
                }else{
                    return false;
                }
            }
        }else{
            throw new CoeusException("budget_exception.1000");
        }
        return success;
    }
    
    /**
     * Overridden method of toString. It will form a string representation of
     * each element associated with this class.
     * @return Concatinated string representation of each element 
     */
    public String toString(){
        StringBuffer strBffr = new StringBuffer("");
        strBffr.append("Cost Element =>"+costElement);
        strBffr.append(";");
        strBffr.append("Rate Class Code=>"+rateClassCode);
        strBffr.append(";");
        strBffr.append("Rate Class Description =>"+ rateClassDescription);    
        strBffr.append(";");
        strBffr.append("Rate Type Code=>"+rateTypeCode);  
        strBffr.append(";");
        strBffr.append("Rate type Description=>"+rateTypeDescription);
        strBffr.append(";");
        strBffr.append("Rate Class Type=>"+rateClassType);
        strBffr.append(";");
        strBffr.append("Updater user=>"+updateUser);
        strBffr.append(";");
        strBffr.append("Update time stamp=>"+updateTimestamp);
        strBffr.append(";");
        strBffr.append("Action type=>"+acType);
        strBffr.append("\n");
        return strBffr.toString();
    }
}