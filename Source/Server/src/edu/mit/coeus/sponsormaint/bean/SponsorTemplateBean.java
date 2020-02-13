/*
 * SponsorTemplateBean.java
 *
 * Created on August 21, 2004, 11:40 AM
 */

package edu.mit.coeus.sponsormaint.bean;

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
public class SponsorTemplateBean extends SponsorMaintenanceFormBean implements CoeusBean,Serializable {
    
    private int packageNumber;
    
    private int pageNumber;
    
    private String pageDescription;
    
    private byte[] formTemplate;
    
    private String updateUser;
    
    private Timestamp updateTimestamp;
    
    private int rowId;
    
    /** Creates a new instance of SponsorTemplateBean */
    public SponsorTemplateBean() {
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
    
    /** Getter for property pageNumber.
     * @return Value of property pageNumber.
     *
     */
    public int getPageNumber() {
        return pageNumber;
    }
    
    /** Setter for property pageNumber.
     * @param pageNumber New value of property pageNumber.
     *
     */
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
    
    /** Getter for property pageDescription.
     * @return Value of property pageDescription.
     *
     */
    public java.lang.String getPageDescription() {
        return pageDescription;
    }
    
    /** Setter for property pageDescription.
     * @param pageDescription New value of property pageDescription.
     *
     */
    public void setPageDescription(java.lang.String pageDescription) {
        this.pageDescription = pageDescription;
    }
    
    
    
    public boolean equals(Object obj) {
        SponsorTemplateBean sponsorTemplateBean = (SponsorTemplateBean)obj;
        if(sponsorTemplateBean.getSponsorCode().equals(getSponsorCode()) &&
            sponsorTemplateBean.getPackageNumber() == getPackageNumber() &&
            sponsorTemplateBean.getPageNumber() == getPageNumber() &&
            (sponsorTemplateBean.getRowId() == getRowId())){
            return true;
        }else{
            return false;
        }
    }
    
    public boolean isLike(ComparableBean comparableBean) throws CoeusException{
        return true;
    }  
    
    public boolean equals(CoeusBean coeusBean) throws CoeusException {
        return isLike(coeusBean);
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
    
    /** Getter for property formTemplate.
     * @return Value of property formTemplate.
     *
     */
    public byte[] getFormTemplate() {
        return this.formTemplate;
    }
    
    /** Setter for property formTemplate.
     * @param formTemplate New value of property formTemplate.
     *
     */
    public void setFormTemplate(byte[] formTemplate) {
        this.formTemplate = formTemplate;
    }
    
}
