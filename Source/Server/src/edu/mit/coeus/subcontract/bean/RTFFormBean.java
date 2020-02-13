/*
 * RTFFormBean.java
 *
 * Created on December 14, 2004, 2:20 PM
 */

package edu.mit.coeus.subcontract.bean;

import edu.mit.coeus.bean.BaseBean;


/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class RTFFormBean implements java.io.Serializable, BaseBean {
	
	// holds the form Id
	private String formId;
	//holds the description
	private String description;
        
        private String formDescription;
	//holds update user id
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds Action type
    private String acType;
    // Added by Shivakumar
    private byte[] form;
    
    private String aw_Form_Id;
    // Added for COEUSQA-1412 Subcontract Module changes - Start
    private int templateTypeCode;
    private String templateTypeDescription;
    // Added for COEUSQA-1412 Subcontract Module changes - End
    
    
	/** Creates a new instance of RTFFormBean */
	public RTFFormBean() {
	}
	
	/**
	 * Getter for property formId.
	 * @return Value of property formId.
	 */
	public java.lang.String getFormId() {
		return formId;
	}
	
	/**
	 * Setter for property formId.
	 * @param formId New value of property formId.
	 */
	public void setFormId(java.lang.String formId) {
		this.formId = formId;
	}
	
	/**
	 * Getter for property description.
	 * @return Value of property description.
	 */
	public java.lang.String getDescription() {
		return description;
	}
	
	/**
	 * Setter for property description.
	 * @param description New value of property description.
	 */
	public void setDescription(java.lang.String description) {
		this.description = description;
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
	
	/**
	 * Getter for property acType.
	 * @return Value of property acType.
	 */
	public java.lang.String getAcType() {
		return acType;
	}
	
	/**
	 * Setter for property acType.
	 * @param acType New value of property acType.
	 */
	public void setAcType(java.lang.String acType) {
		this.acType = acType;
	}
        
        /** Getter for property form.
         * @return Value of property form.
         *
         */
        public byte[] getForm() {
            return this.form;
        }        
        
        /** Setter for property form.
         * @param form New value of property form.
         *
         */
        public void setForm(byte[] form) {
            this.form = form;
        }
        
        public boolean equals(Object obj) {
        RTFFormBean rTFFormBean = (RTFFormBean)obj;

        if(rTFFormBean.getFormId().equals(getFormId())) {
            return true;       
        }else {
            return false;
        }
        
    }
        
        /**
         * Getter for property aw_Form_Id.
         * @return Value of property aw_Form_Id.
         */
        public java.lang.String getAw_Form_Id() {
            return aw_Form_Id;
        }
        
        /**
         * Setter for property aw_Form_Id.
         * @param aw_Form_Id New value of property aw_Form_Id.
         */
        public void setAw_Form_Id(java.lang.String aw_Form_Id) {
            this.aw_Form_Id = aw_Form_Id;
        }
        
        /** Getter for property formDescription.
         * @return Value of property formDescription.
         *
         */
        public java.lang.String getFormDescription() {
            return formDescription;
        }
        
        /** Setter for property formDescription.
         * @param formDescription New value of property formDescription.
         *
         */
        public void setFormDescription(java.lang.String formDescription) {
            this.formDescription = formDescription;
        }

        // Added for COEUSQA-1412 Subcontract Module changes - Start
        /**
         * Method to get the template type code
         * @return templateTypeCode
         */
        public int getTemplateTypeCode() {
            return templateTypeCode;
        }
        
        /**
         * Method to set the template type code
         * @param templateTypeCode
         */
        public void setTemplateTypeCode(int templateTypeCode) {
            this.templateTypeCode = templateTypeCode;
        }
        
        /**
         * Method to get the template type description
         * @return templateTypeDescription
         */
        public String getTemplateTypeDescription() {
            return templateTypeDescription;
        }
        
        /**
         * Method to set the template type description
         * @param templateTypeDescription
         */
        public void setTemplateTypeDescription(String templateTypeDescription) {
            this.templateTypeDescription = templateTypeDescription;
        }
        // Added for COEUSQA-1412 Subcontract Module changes - End

        
}
