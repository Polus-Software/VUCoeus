/*
 * TemplateBaseBean.java
 *
 * Created on December 15, 2004, 2:34 PM
 */

package edu.mit.coeus.admin.bean;

import edu.mit.coeus.bean.CoeusBean;


/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public abstract class TemplateBaseBean implements CoeusBean, java.io.Serializable {
    
    private int templateCode;
	private String updateUser = null;
    private java.sql.Timestamp updateTimestamp = null;    
    private String acType = null;	
    
    /** Creates a new instance of TemplateBaseBean */
    public TemplateBaseBean() {
    }
    
    /**
     * Getter for property templateCode.
     * @return Value of property templateCode.
     */
    public int getTemplateCode() {
        return templateCode;
    }
    
    /**
     * Setter for property templateCode.
     * @param templateCode New value of property templateCode.
     */
    public void setTemplateCode(int templateCode) {
        this.templateCode = templateCode;
    }
    
    /**
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */
    public boolean equals(Object obj) {
        TemplateBaseBean templateBaseBean= (TemplateBaseBean)obj;
        if(templateBaseBean.getTemplateCode() == getTemplateCode()){
            return true;
        }else{
            return false;
        }
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
	
}//End of Class
