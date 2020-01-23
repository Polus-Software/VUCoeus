/*
 * BudgetCategoryBean.java
 *
 * Created on August 17, 2004, 1:12 PM
 */

package edu.mit.coeus.rates.bean;

import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;
/**
 *
 * @author  shivakumarmj
 */
public class RateClassBean extends ComboBoxBean implements CoeusBean{
    
    private String rateClassType = null;    
    
    private int rateClassCode = -1;
    //holds update user id
    private String updateUser = null;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp = null;
    //holds account type
    private String acType = null;    
    
    /** Creates a new instance of BudgetCategoryBean */
    public RateClassBean() {
    }
    
    /** Getter for property categoryType.
     * @return Value of property categoryType.
     */
    public java.lang.String getRateClassType() {
        return rateClassType;
    }
    
    /** Setter for property categoryType.
     * @param categoryType New value of property categoryType.
     */
    public void setRateClassType(java.lang.String rateClassType) {
        this.rateClassType = rateClassType;
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
    
    public boolean isLike(ComparableBean comparableBean) throws CoeusException{
        return true;
    }  
    
//    public boolean equals(CoeusBean coeusBean) throws CoeusException {
//        return isLike(coeusBean);
//     }

    /** Getter for property rateClassCode.
     * @return Value of property rateClassCode.
     *
     */
    public int getRateClassCode() {
        return rateClassCode;
    }
    
    /** Setter for property rateClassCode.
     * @param rateClassCode New value of property rateClassCode.
     *
     */
    public void setRateClassCode(int rateClassCode) {
        this.rateClassCode = rateClassCode;
    }
    
    public boolean equals(Object obj) {
        RateClassBean rateClassBean = (RateClassBean)obj;        
        if(rateClassBean.getRateClassCode() == getRateClassCode()) {
            return true;       
        }else {
            return false;
        }
        
    }
    
}
