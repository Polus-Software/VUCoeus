package edu.utk.coeuslite.propdev.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 * User: muralidharann
 * Date: Jul 25, 2006
 * Time: 2:45:27 PM
 * To change this template use File | Settings | File Templates.
 */


public class GeneralProposalForm extends DynaValidatorForm{
    //Change the bean property name
    private String proposalNumber;
    private String unitNumber;
    private String updateUser;
    private Timestamp updateTimeStamp;
    private String acType;
    private int roleId;
    private String userId;
    private String description;
    private String ruleId;
    private String ruleType;
    private String unitName;
    private String sponsorCode;
    private String rolodexId;
    private String primeSponsorCode;

    /** Creates a new instance of ProposalUploadForm */
    public GeneralProposalForm() {
    }
    /**
     * Getter for property proposalNumber.
     * @return Value of property proposalNumber.
     */
    public String getProposalNumber() {
        return proposalNumber;
    }
    /**
     * Setter for property proposalNumber.
     * @param proposalNumber New value of property proposalNumber.
     */
    public void setProposalNumber(java.lang.String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
    /**
     * Getter for property unitNumber.
     * @return Value of property unitNumber.
     */
    public String getUnitNumber() {
        return unitNumber;
    }
    /**
     * Setter for property proposalNumber.
     * @param unitNumber New value of property proposalNumber.
     */
    public void setUnitNumber(java.lang.String unitNumber) {
        this.unitNumber = unitNumber;
    }
    /**
    * Getter for property updateTimeStamp.
    * @return Value of property updateTimeStamp.
    */
    public String getUpdateUser() {
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
    * Getter for property updateTimeStamp.
    * @return Value of property updateTimeStamp.
    */
    public Timestamp getUpdateTimeStamp() {
       return updateTimeStamp;
   }
    /**
    * Setter for property updateTimeStamp.
    * @param updateTimeStamp New value of property updateTimeStamp.
    */
    public void setUpdateTimeStamp(Timestamp updateTimeStamp) {
       this.updateTimeStamp = updateTimeStamp;
   }
    /**
    * Getter for property acType.
    * @return Value of property acType.
    */
    public String getAcType() {
       return acType;
   }
    /**
    * Setter for property acType.
    * @param acType New value of property acType.
    */
    public void setAcType(String acType) {
       this.acType = acType;
   }
    /**
    * Getter for property roleId.
    * @return Value of property roleId.
    */
    public int getRoleId() {
       return roleId;
   }
    /**
    * Setter for property roleId.
    * @param roleId New value of property updateUser.
    */
    public void setRoleId(int roleId) {
       this.roleId = roleId;
   }
    /**
     * Getter for property userId.
     * @return Value of property userId.
     */
    public String getUserId() {
        return userId;
    }
    /**
     * Setter for property userId.
     * @param userId New value of property userId.
     */
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }
    /**
    * Getter for property ruleType.
    * @return Value of property ruleType.
    */
    public String getRuleType() {
       return ruleType;
   }
    /**
    * Setter for property ruleType.
    * @param ruleType New value of property ruleType.
    */
    public void setRuleType(String ruleType) {
       this.ruleType = ruleType;
   }
    /**
    * Getter for property description.
    * @return Value of property description.
    */
    public String getDescription() {
       return description;
   }
    /**
    * Setter for property description.
    * @param description New value of property description.
    */
    public void setDescription(String description) {
       this.description = description;
   }
   /**
    * Getter for property ruleId.
    * @return Value of property ruleId.
    */
    public String getRuleId() {
       return ruleId;
   }
    /**
    * Setter for property ruleId.
    * @param ruleId New value of property ruleId.
    */
    public void setRuleId(String ruleId) {
       this.ruleId = ruleId;
   }
   /**
    * Getter for property unitName.
    * @return Value of property unitName.
    */
    public String getUnitName() {
       return unitName;
   }
    /**
    * Setter for property unitName.
    * @param unitName New value of property unitName.
    */
    public void setUnitName(String unitName) {
       this.unitName = unitName;
   }
    /**
     * Getter for property sponsorCode.
     * @return Value of property sponsorCode.
     */
    public String getSponsorCode() {
        return sponsorCode;
    }
     /**
     * Setter for property sponsorCode.
     * @param sponsorCode New value of property sponsorCode.
     */
    public void setSponsorCode(String sponsorCode) {
        this.sponsorCode = sponsorCode;
    }
     /**
     * Getter for property rolodexID.
     * @return Value of property rolodexId.
     */
    public String getRolodexId() {
        return rolodexId;
    }
     /**
     * Setter for property rolodexId.
     * @param rolodexId New value of property rolodexId.
     */
    public void setRolodexId(String rolodexId) {
        this.rolodexId = rolodexId;
    }

    /**
     * Getter for property primeSponsorCode.
     * @return Value of property primeSponsorCode.
     */
    public java.lang.String getPrimeSponsorCode() {
        return primeSponsorCode;
    }    

    /**
     * Setter for property primeSponsorCode.
     * @param primeSponsorCode New value of property primeSponsorCode.
     */
    public void setPrimeSponsorCode(java.lang.String primeSponsorCode) {
        this.primeSponsorCode = primeSponsorCode;
    }
    
}
