/*
 * RTFFormVariableBean.java
 *
 * Created on December 14, 2004, 2:40 PM
 */

package edu.mit.coeus.subcontract.bean;

import edu.mit.coeus.bean.BaseBean;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class RTFFormVariableBean implements java.io.Serializable, BaseBean {
	
	private String variableName;
        private String functionName;
        private String updateUser;
        //holds update timestamp
        private java.sql.Timestamp updateTimestamp;
        //holds Action type
        private String acType;
	/** Creates a new instance of RTFFormVariableBean */
	public RTFFormVariableBean() {
	}
	
	/**
	 * Getter for property variableName.
	 * @return Value of property variableName.
	 */
	public java.lang.String getVariableName() {
		return variableName;
	}
	
	/**
	 * Setter for property variableName.
	 * @param variableName New value of property variableName.
	 */
	public void setVariableName(java.lang.String variableName) {
		this.variableName = variableName;
	}
        /**
         * Getter for property functionName.
         * @return Value of property functionName.
         */
        public java.lang.String getFunctionName() {
            return functionName;
        }
        
        /**
         * Setter for property functionName.
         * @param functionName New value of property functionName.
         */
        public void setFunctionName(java.lang.String functionName) {
            this.functionName = functionName;
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
        
}
