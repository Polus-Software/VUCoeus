/*
 * @(#)FrequencyBaseBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.bean;

import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.utils.ComboBoxBean;
/**
 * The class used to hold the information of <code>Frequency Base</code>
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on June 07, 2004, 12:26 PM
 */

public class FrequencyBaseBean extends ComboBoxBean implements java.io.Serializable, BaseBean {

    //holds frequency code
    private int frequencyCode;
    //holds numberOfDays
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds Action type
    private String acType;
    
    /** Creates new FrequencyBean */
    public FrequencyBaseBean() {
    }    
    
    /** Getter for property frequencyCode.
     * @return Value of property frequencyCode.
     *
     */
    public int getFrequencyCode() {
        return frequencyCode;
    }
    
    /** Setter for property frequencyCode.
     * @param frequencyCode New value of property frequencyCode.
     *
     */
    public void setFrequencyCode(int frequencyCode) {
        this.frequencyCode = frequencyCode;
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
    
    /** Getter for property acType.
     * @return Value of property acType.
     *
     */
    public java.lang.String getAcType() {
        return acType;
    }
    
    /** Setter for property acType.
     * @param acType New value of property acType.
     *
     */
    public void setAcType(java.lang.String acType) {
        this.acType = acType;
    }
}