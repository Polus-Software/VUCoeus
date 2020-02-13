/*
 * SubcontractCloseoutBean.java
 *
 * Created on September 7, 2004, 3:45 PM
 */

package edu.mit.coeus.subcontract.bean;

import java.sql.Date;
import java.sql.Timestamp;
/**
 *
 * @author  chandrashekara
 */
public class SubcontractCloseoutBean  extends SubContractBaseBean {
    
    private int closeoutNumber;
    private int closeoutTypeCode;
    private Date dateRequested;
    private Date dateFollowUp;
    private Date dateReceived;
    private String comment;
    //Case #3338 - Add new elements the person and rolodex details to Subcontract schema -Start
    private String closeoutTypeDesc;
    //Case #3338 - Add new elements the person and rolodex details to Subcontract schema -End

    
    /** Creates a new instance of SubcontractCloseoutBean */
    public SubcontractCloseoutBean() {
        
    }
    
    /** Getter for property closeoutNumber.
     * @return Value of property closeoutNumber.
     *
     */
    public int getCloseoutNumber() {
        return closeoutNumber;
    }
    
    /** Setter for property closeoutNumber.
     * @param closeoutNumber New value of property closeoutNumber.
     *
     */
    public void setCloseoutNumber(int closeoutNumber) {
        this.closeoutNumber = closeoutNumber;
    }
    
    /** Getter for property closeoutTypeCode.
     * @return Value of property closeoutTypeCode.
     *
     */
    public int getCloseoutTypeCode() {
        return closeoutTypeCode;
    }
    
    /** Setter for property closeoutTypeCode.
     * @param closeoutTypeCode New value of property closeoutTypeCode.
     *
     */
    public void setCloseoutTypeCode(int closeoutTypeCode) {
        this.closeoutTypeCode = closeoutTypeCode;
    }
    
    /** Getter for property dateRequested.
     * @return Value of property dateRequested.
     *
     */
    public java.sql.Date getDateRequested() {
        return dateRequested;
    }
    
    /** Setter for property dateRequested.
     * @param dateRequested New value of property dateRequested.
     *
     */
    public void setDateRequested(java.sql.Date dateRequested) {
        this.dateRequested = dateRequested;
    }
    
    /** Getter for property dateFollowUp.
     * @return Value of property dateFollowUp.
     *
     */
    public java.sql.Date getDateFollowUp() {
        return dateFollowUp;
    }
    
    /** Setter for property dateFollowUp.
     * @param dateFollowUp New value of property dateFollowUp.
     *
     */
    public void setDateFollowUp(java.sql.Date dateFollowUp) {
        this.dateFollowUp = dateFollowUp;
    }
    
    /** Getter for property dateReceived.
     * @return Value of property dateReceived.
     *
     */
    public java.sql.Date getDateReceived() {
        return dateReceived;
    }
    
    /** Setter for property dateReceived.
     * @param dateReceived New value of property dateReceived.
     *
     */
    public void setDateReceived(java.sql.Date dateReceived) {
        this.dateReceived = dateReceived;
    }
    
    /** Getter for property comment.
     * @return Value of property comment.
     *
     */
    public java.lang.String getComment() {
        return comment;
    }
    
    /** Setter for property comment.
     * @param comment New value of property comment.
     *
     */
    public void setComment(java.lang.String comment) {
        this.comment = comment;
    }
    
    // Code added by Shivakumar begin
     public boolean equals(Object obj) {        
        if(super.equals(obj)){
            SubcontractCloseoutBean subcontractCloseoutBean= (SubcontractCloseoutBean)obj;
             if(subcontractCloseoutBean.getCloseoutNumber()==getCloseoutNumber()){ 
                 return true;
             }else{
                 return false;
             }
        }else{
            return false;
        }    
    }
     
     /**
      * Getter for property closeoutTypeDesc.
      * @return Value of property closeoutTypeDesc.
      */
     public java.lang.String getCloseoutTypeDesc() {
         return closeoutTypeDesc;
     }     
    
     /**
      * Setter for property closeoutTypeDesc.
      * @param closeoutTypeDesc New value of property closeoutTypeDesc.
      */
     public void setCloseoutTypeDesc(java.lang.String closeoutTypeDesc) {
         this.closeoutTypeDesc = closeoutTypeDesc;
     }
     
}
