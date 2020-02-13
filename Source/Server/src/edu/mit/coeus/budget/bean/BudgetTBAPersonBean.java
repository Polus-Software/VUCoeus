/*
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.budget.bean;

import edu.mit.coeus.bean.BaseBean;
import java.io.Serializable;

/**
 * The class used to hold the information of <code>Budget TBA Persons</code>
 *
 * @author  noorula
 * Created on May 9, 2007, 9:36 PM
 */
public class BudgetTBAPersonBean implements BaseBean, Serializable{
    
    //holds tba id
    private String tbaId;
    //holds name
    private String name;
    //holds job code
    private String jobCode;
    //holds update user id
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    
    /** Creates a new instance of BudgetTBAPersonBean */
    public BudgetTBAPersonBean() {
    }
    
    /**
     * Getter for property tbaId.
     * @return Value of property tbaId.
     */
    public java.lang.String getTbaId() {
        return tbaId;
    }
    
    /**
     * Setter for property tbaId.
     * @param tbaId New value of property tbaId.
     */
    public void setTbaId(java.lang.String tbaId) {
        this.tbaId = tbaId;
    }
    
    /**
     * Getter for property name.
     * @return Value of property name.
     */
    public java.lang.String getName() {
        return name;
    }
    
    /**
     * Setter for property name.
     * @param name New value of property name.
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }
    
    /**
     * Getter for property jobCode.
     * @return Value of property jobCode.
     */
    public java.lang.String getJobCode() {
        return jobCode;
    }
    
    /**
     * Setter for property jobCode.
     * @param jobCode New value of property jobCode.
     */
    public void setJobCode(java.lang.String jobCode) {
        this.jobCode = jobCode;
    }
    
    /**
     * Getter for property updateUser.
     * @return Value of property updateUser.
     */
    public java.lang.String getUpdateUser() {
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
     * Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /**
     * Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
}
