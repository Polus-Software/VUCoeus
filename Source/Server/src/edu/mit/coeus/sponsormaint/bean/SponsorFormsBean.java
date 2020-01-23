/*
 * SponsorFormsBean.java
 *
 * Created on August 19, 2004, 9:58 AM
 */

package edu.mit.coeus.sponsormaint.bean;

import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceFormBean;

import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import java.io.Serializable;
import edu.mit.coeus.exception.CoeusException;
import java.sql.Timestamp;
import java.util.Vector;
/**
 *
 * @author  shivakumarmj
 */
public class SponsorFormsBean extends SponsorMaintenanceFormBean implements CoeusBean, Serializable {
    
    private int packageNumber;
    
    private String packageName;
    
    private String updateUser;
    
    private Timestamp updateTimestamp;
    
    private int rowId;
    
    //Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy - Start
    private String groupName;
    //Case#2445 - End
    
    
    /** Creates a new instance of SponsorFormsBean */
    public SponsorFormsBean() {
    }
    
    /** Getter for property packageNumber.
     * @return Value of property packageNumber.
     *
     */
    public int getPackageNumber() {
        return packageNumber;
    }
    
    /** Setter for property packageNumber.
     * @param packageNumber New value of property packageNumber.
     *
     */
    public void setPackageNumber(int packageNumber) {
        this.packageNumber = packageNumber;
    }
    
    /** Getter for property packageName.
     * @return Value of property packageName.
     *
     */
    public java.lang.String getPackageName() {
        return packageName;
    }
    
    /** Setter for property packageName.
     * @param packageName New value of property packageName.
     *
     */
    public void setPackageName(java.lang.String packageName) {
        this.packageName = packageName;
    }
    
    public boolean equals(Object obj) {
        SponsorFormsBean sponsorFormsBean = (SponsorFormsBean)obj;
        if(sponsorFormsBean.getSponsorCode().equals(getSponsorCode()) &&
            sponsorFormsBean.getPackageNumber() == getPackageNumber() &&
            sponsorFormsBean.getRowId() == getRowId()){
            return true;
        }else{
            return false;
        }
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
    
    public boolean isLike(ComparableBean comparableBean) throws CoeusException{
        return true;
    }  
    
    public boolean equals(CoeusBean coeusBean) throws CoeusException {
        return isLike(coeusBean);
     }
    
    /** Getter for property rowId.
     * @return Value of property rowId.
     *
     */
    public int getRowId() {
        return rowId;
    }
    
    /** Setter for property rowId.
     * @param rowId New value of property rowId.
     *
     */
    public void setRowId(int rowId) {
        this.rowId = rowId;
    }
    
    //Added for Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy - Start
    /** Getter for property groupName.
     * @return Value of property groupName.
     *
     */
    public java.lang.String getGroupName() {
        return groupName;
    }
    
    /** Setter for property groupName.
     * @param packageName New value of property groupName.
     *
     */
    public void setGroupName(java.lang.String groupName) {
        this.groupName = groupName;
    }
    //Case#2445 - End
}
