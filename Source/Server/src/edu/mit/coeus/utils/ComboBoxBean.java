/*
 * @(#)ComboBoxBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on April 5, 2002, 7:14 PM
 * @author  Geo Thomas
 * @version 1.0
 */
package edu.mit.coeus.utils;

//import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.bean.BaseBean;

/**
 * The Class is used to populate any combo box
 */
public class ComboBoxBean implements java.io.Serializable, BaseBean {
	private String code;
	private String description = "";

	/** Default Constructor */
	public ComboBoxBean() {
	}

	/** Constructor with parameters */
	public ComboBoxBean(String code, String description) {
		this.code = code;
		this.description = description;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ComboBoxBean) {
			ComboBoxBean comboBoxBean = (ComboBoxBean) obj;
			if (description != null && comboBoxBean.getDescription() != null && !(description.equals(""))) {
				return getDescription().equals(comboBoxBean.getDescription());
			} else if (code != null && comboBoxBean.getCode() != null) {
				return code.equals(comboBoxBean.getCode());
			}
		}
		return super.equals(obj);
	}

	/**
	 * This method gets the Code
	 * 
	 * @return String code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * This method gets the Description
	 * 
	 * @return String description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * This method sets the Code
	 * 
	 * @param String
	 *            code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * This method sets the Description
	 * 
	 * @param String
	 *            description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		/*
		 * StringBuffer strBffr = new StringBuffer("");
		 * strBffr.append("Code=>"+code); strBffr.append("; ");
		 * strBffr.append("Description=>"+description); return
		 * strBffr.toString();
		 */
		if (description != null && !description.trim().equals("")) {
			return getDescription();
		} else if (code != null && !code.trim().equals("")) {
			return getCode();
		}
		return "";
	}
}