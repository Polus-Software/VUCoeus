/*
 * AwardTemplateCommentsBean.java
 *
 * Created on January 4, 2005, 4:27 PM
 */

package edu.mit.coeus.admin.bean;

/**
 *
 * @author  ajaygm
 */
public class AwardTemplateCommentsBean extends TemplateBaseBean{
    
    private int commentCode;
    private boolean checkListPrintFlag;
    private String comments;   
    private int aw_CommentCode;
    //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
    private String updateUserName;
    //COEUSQA-1456 : End
    
    /** Creates a new instance of AwardTemplateCommentsBean */
    public AwardTemplateCommentsBean(){
    }
    
    public boolean isLike(edu.mit.coeus.bean.ComparableBean comparableBean) throws edu.mit.coeus.exception.CoeusException {
        return true;
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
    
    /** Getter for property checkListPrintFlag.
     * @return Value of property checkListPrintFlag.
     *
     */
    public boolean isCheckListPrintFlag() {
        return checkListPrintFlag;
    }
    
    /** Setter for property checkListPrintFlag.
     * @param checkListPrintFlag New value of property checkListPrintFlag.
     *
     */
    public void setCheckListPrintFlag(boolean checkListPrintFlag) {
        this.checkListPrintFlag = checkListPrintFlag;
    }
    
    /** Getter for property comments.
     * @return Value of property comments.
     *
     */
    public java.lang.String getComments() {
        return comments;
    }
    
    /** Setter for property comments.
     * @param comments New value of property comments.
     *
     */
    public void setComments(java.lang.String comments) {
        this.comments = comments;
    }
    
    /** Getter for property aw_CommentCode.
     * @return Value of property aw_CommentCode.
     *
     */
    public int getAw_CommentCode() {
        return aw_CommentCode;
    }
    
    /** Setter for property aw_CommentCode.
     * @param aw_CommentCode New value of property aw_CommentCode.
     *
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
            AwardTemplateCommentsBean awardTemplateCommentsBean = (AwardTemplateCommentsBean)obj;
            if(awardTemplateCommentsBean.getCommentCode() == getCommentCode()){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    
    //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
    /*
     * Getter method to get the update user name
     * @reutnr updateUserName - String
     */
    public String getUpdateUserName() {
        return updateUserName;
    }
    
    /*
     * Setter method to set the update user name
     * @param updateUserName
     */
    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }
    //COEUSQA-1456 : End
    
}//End Class
