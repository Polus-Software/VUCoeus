/**
 * @(#)AwardCommentTypeBean.java 1.0 10/03/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.bean;
import java.util.Vector;
import java.sql.Date;
import edu.mit.coeus.bean.BaseBean;
/**
 * This class is used to hold Award Comments data
 *
 * @author Ajay G M
 * @version 1.0
 * Created on March 10, 2004 4:00 PM
 */

public class AwardCommentTypeBean implements java.io.Serializable, BaseBean{
    
    private int commentCode;
    private String description;
    private boolean templateFlag;   
    private boolean checklistFlag;
    private boolean awardCommentScreenFlag; 
    private java.sql.Timestamp updateTimestamp; 
    private String updateUser;


    /**
     *	Default Constructor
     */
    
    public AwardCommentTypeBean(){
    }       
    
    /** Getter for property commentCode.
     * @return Value of property commentCode.
     *
     */
    public int getCommentCode() {
        return commentCode;
    }
    
    /** Setter for property commentCode.
     * @param commentCode New value of property commentCode.
     *
     */
    public void setCommentCode(int commentCode) {
        this.commentCode = commentCode;
    }
    
    /** Getter for property description.
     * @return Value of property description.
     *
     */
    public java.lang.String getDescription() {
        return description;
    }
    
    /** Setter for property description.
     * @param description New value of property description.
     *
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
    /** Getter for property templateFlag.
     * @return Value of property templateFlag.
     *
     */
    public boolean isTemplateFlag() {
        return templateFlag;
    }
    
    /** Setter for property templateFlag.
     * @param templateFlag New value of property templateFlag.
     *
     */
    public void setTemplateFlag(boolean templateFlag) {
        this.templateFlag = templateFlag;
    }
    
    /** Getter for property checklistFlag.
     * @return Value of property checklistFlag.
     *
     */
    public boolean isChecklistFlag() {
        return checklistFlag;
    }
    
    /** Setter for property checklistFlag.
     * @param checklistFlag New value of property checklistFlag.
     *
     */
    public void setChecklistFlag(boolean checklistFlag) {
        this.checklistFlag = checklistFlag;
    }
    
    /** Getter for property awardCommentScreenFlag.
     * @return Value of property awardCommentScreenFlag.
     *
     */
    public boolean isAwardCommentScreenFlag() {
        return awardCommentScreenFlag;
    }
    
    /** Setter for property awardCommentScreenFlag.
     * @param awardCommentScreenFlag New value of property awardCommentScreenFlag.
     *
     */
    public void setAwardCommentScreenFlag(boolean awardCommentScreenFlag) {
        this.awardCommentScreenFlag = awardCommentScreenFlag;
    }
    
    /** Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     *
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /** Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     *
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /** Getter for property updateUser.
     * @return Value of property updateUser.
     *
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /** Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     *
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }
    
}