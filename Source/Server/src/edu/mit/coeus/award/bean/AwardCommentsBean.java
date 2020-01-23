/**
 * @(#)AwardCommentsBean.java 1.0 01/03/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.bean;
import java.util.Vector;
import java.sql.Date;
/**
 * This class is used to hold Award Comments data
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on March 05, 2004 12:30 PM
 */

public class AwardCommentsBean extends AwardBaseBean{
    
    private int commentCode;
    private boolean checkListPrintFlag;
    private String comments;   
    private int aw_CommentCode;
    
    /**
     *	Default Constructor
     */
    
    public AwardCommentsBean(){
    }       
    
    /**
     *	Copy Constructor
     */
    
    public AwardCommentsBean(TemplateCommentsBean templateCommentsBean){
        this.setCommentCode(templateCommentsBean.getCommentCode());
        this.setCheckListPrintFlag(templateCommentsBean.isCheckListPrintFlag());
        this.setComments(templateCommentsBean.getComments());
        this.setAw_CommentCode(templateCommentsBean.getAw_CommentCode());
    }

    /** Getter for property commentCode.
     * @return Value of property commentCode.
     */
    public int getCommentCode() {
        return commentCode;
    }
    
    /** Setter for property commentCode.
     * @param commentCode New value of property commentCode.
     */
    public void setCommentCode(int commentCode) {
        this.commentCode = commentCode;
    }
    
    /** Getter for property checkListPrintFlag.
     * @return Value of property checkListPrintFlag.
     */
    public boolean isCheckListPrintFlag() {
        return checkListPrintFlag;
    }
    
    /** Setter for property checkListPrintFlag.
     * @param checkListPrintFlag New value of property checkListPrintFlag.
     */
    public void setCheckListPrintFlag(boolean checkListPrintFlag) {
        this.checkListPrintFlag = checkListPrintFlag;
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
    
    /** Getter for property aw_CommentCode.
     * @return Value of property aw_CommentCode.
     */
    public int getAw_CommentCode() {
        return aw_CommentCode;
    }
    
    /** Setter for property aw_CommentCode.
     * @param aw_CommentCode New value of property aw_CommentCode.
     */
    public void setAw_CommentCode(int aw_CommentCode) {
        this.aw_CommentCode = aw_CommentCode;
    }    
    
    
    /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        if(super.equals(obj)){
            AwardCommentsBean awardCommentsBean = (AwardCommentsBean)obj;
            if(awardCommentsBean.getCommentCode() == getCommentCode()){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }        
    }
}