/*
 * YNQBaseBean.java
 *
 * Created on November 26, 2004, 10:33 AM
 */

package edu.mit.coeus.admin.bean;

import edu.mit.coeus.bean.CoeusBean;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class YNQBaseBean implements CoeusBean, java.io.Serializable {
	
	private String questionId = null;
	private String updateUser = null;
    private java.sql.Timestamp updateTimestamp = null;
	private String acType = null;
	
	/** Creates a new instance of YNQBaseBean */
	public YNQBaseBean() {
	}
	    
     /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        YNQBaseBean ynqBaseBean = (YNQBaseBean)obj;
        if(ynqBaseBean.getQuestionId().equals(getQuestionId())) {
            return true;
        }else{
            return false;
        }
    }
	
	/** Getter for property questionId.
	 * @return Value of property questionId.
	 *
	 */
	public java.lang.String getQuestionId() {
		return questionId;
	}
	
	/** Setter for property questionId.
	 * @param questionId New value of property questionId.
	 *
	 */
	public void setQuestionId(java.lang.String questionId) {
		this.questionId = questionId;
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
	
	public boolean isLike(edu.mit.coeus.bean.ComparableBean comparableBean) throws edu.mit.coeus.exception.CoeusException {
		 return true;
	}
	
}
