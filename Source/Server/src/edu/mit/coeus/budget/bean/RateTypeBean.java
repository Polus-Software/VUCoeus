/*
 * RateTypeBean.java
 *
 * Created on March 02, 2004, 6:15 PM
 */

package edu.mit.coeus.budget.bean;

import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;

/**
 * The class is the used to hold data of RateType
 
 * @author  Prasanna Kumar
 * @version 1.0
 * Created on March 02, 2004, 6:15 PM
 */

public class RateTypeBean extends ComboBoxBean implements CoeusBean, java.io.Serializable{
    
    //holds rate class code
    private int rateClassCode = -1;
    //holds update user id
    private String updateUser = null;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp = null;
    //holds account type
    private String acType = null;    
    
    /** Creates a new instance of BudgetCategoryBean */
    public RateTypeBean() {
    } 
    
    public boolean isLike(ComparableBean comparableBean) throws CoeusException{
        return true;
    }  
    
    public boolean equals(CoeusBean coeusBean) throws CoeusException {
        return isLike(coeusBean);
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
}