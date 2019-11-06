/*
 * @(#)FormatOutputTag.java	1.0 05/20/2002
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.taglib.utils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;

/**
 * <code> FormatOutputTag </code> a java bean that represents a Tag in Coeus
 * developed on struts classes.
 * Tag that retrieves the specified property of the specified bean, converts
 * it to a String representation (if necessary), and writes it to the current
 * output stream, optionally <i>modifies the output </i> to lower/upper cases
 * as well as to user default Value that he wants to display in view component.
 *
 * @version 1.0  May 29, 2002 11:53:08
 * @author RaYaKu
 */

public class FormatOutputTag extends TagSupport{

	/**
	 * Name of the bean that contains the data we will be rendering.
	 */
	protected String name;

	/**
	 * Name of the property to be accessed on the specified bean.
	 */
	protected String property;

	/**
	 * Case conversion of bean output
	 */
	protected String convertTo;

	/**
	 * The fully qualified Java class name of the value to be exposed.
	 */
	protected String type;

	/**
	 * The output that user wants if bean output is null or "" .
	 */
	protected String defaultValue;

	/**
	 * The scope to be searched to retrieve the specified bean.
	 */
	protected String scope;

	/**
	 * Get the name of bean(instance)
	 *
	 * @return name  Bean instance
	 */
	public String getName(){
		return this.name;
	}

	/**
	 * Sets the Name of bean (instance).
	 */
	public void setName( String name ){
		this.name = name;
	}

	/**
	 * Get the property of bean(instance)
	 *
	 * @return property  Method Name of  Bean instance
	 */
	public String getProperty(){
		return this.property;
	}

	/**
	 * Sets the property(method name)  of bean(instance).
	 */
	public void setProperty( String property ){
		this.property = property;
	}

	/**
	 * Get the convertTo value of bean(instance).
	 * Converts all of the characters in this bean output  to lower/upper case.
	 *
	 * @return convertTo  Case type to convert bean output.
	 */
	public String getConvertTo(){
		return this.convertTo;
	}

	/**
	 * Sets the convertTo value of bean with which the tag output can be modified
	 * only into lower or upper case.
	 *
	 * @param convertTo   accepts any string but output will be modified
	 *  if string is either  lower or upper without case senistive.
	 */
	public void setConvertTo( String convertTo ){
		this.convertTo = convertTo;
	}

	/**
	 * Get the type of bean(instance)
	 *
	 * @return type  Fully qualified class name of this class
	 */
	public String getType(){
		return this.type;
	}

	/**
	 * Set the type value of bean.
	 */
	public void setType( String type ){
		this.type = type;
	}

	/**
	 * Get the Defalut output  value of bean(instance).
	 * By using this, User can change the output of the bean if bean output
	 * is null or ""  to in  required format .
	 *
	 * @return defaultValue  User Required output of bean if bean regular
	 * output is null or "".
	 */
	public String getDefaultValue(){
		return this.defaultValue;
	}

	/**
	 * Sets the Default output value bean, the output of tag is  defaultValue if
	 * actual bean output is null or "".
	 */
	public void setDefaultValue( String defaultValue ){
		this.defaultValue = defaultValue;
	}

	/**
	 * Get the scope of bean in which this bean is loaded
	 * ex. page,request,session,application
	 *
	 * @return defaultValue  User Required output of bean if bean regular output
	 * is null or "".
	 */
	public String getScope(){
		return this.scope;
	}

	/**
	 * Sets the scope value of bean.
	 */
	public void setScope( String scope ){
		this.scope = scope;
	}

	/**
	 * Process the start tag.
	 * Default processing of the start tag, returning SKIP_BODY.
	 *
	 * @return int  SKIP_BODY.
	 * @exception JspException if a JSP exception has occurred
	 */
	public int doStartTag() throws JspException{

		Object bean = null;

		if( defaultValue == null ) {
			defaultValue = "";
		}

		if( scope == null ) {
			scope = "page";
		}

		if( convertTo == null ) {
			convertTo = "";
		}

		// Look up the requested bean (if necessary)
		if( RequestUtils.lookup( pageContext , name , scope ) == null ) {
			return ( SKIP_BODY );  // Nothing to output
		}

		Object value = RequestUtils.lookup( pageContext , name , property , scope );


		// Print this property value to our output writer.
		String output = "";
		if( ( value == null ) || ( value.toString().trim().length() == 0 ) ) {
			// if value is null or "" then print the user defaultValue
			output = defaultValue;
		} else {
			output = value.toString();
		}

		//convert if user looking to change the output case
		if( convertTo.equalsIgnoreCase( "lower" ) ) {
			output = output.toLowerCase();
		}

		if( convertTo.equalsIgnoreCase( "upper" ) ) {
			output = output.toUpperCase();
		}
		/*
		 * if a tag refers this tag handler class with name is certificateDetails
		 * and property is answer then modify the output
		 */
		if( this.property.equals( "answer" ) && ( this.name.equals( "certficateDetails" ) ) ) {
			if( output.trim().length() > 0 ) {
				switch( output.trim().toUpperCase().charAt( 0 ) ) {
					case ( 'Y' ):
						output = "Yes";
						break;
					case ( 'N' ):
						output = "No";
						break;
					case ( 'X' ):
						output = "N/A";
						break;
				}//swtich ends
			}

		} else
		/*
		 * if a tag refers this tag handler class with name is entityDetails
		 * and property is orgRelationship then modify the output
		 */ if( this.property.equals( "orgRelationship" )
				 && ( this.name.equals( "entityDetails" ) ) ) {
			 if( output.trim().length() > 0 ) {
				 switch( output.trim().toUpperCase().charAt( 0 ) ) {
					 case ( 'Y' ):
						 output = "Related";
						 break;
					 case ( 'N' ):
						 output = "Not Related";
						 break;
					 case ( 'X' ):
						 output = "Don't Know";
						 break;
				 } //switch ends
			 }
		 }
		//print the output
		ResponseUtils.write( pageContext , output );

		// Continue processing this page
		return ( SKIP_BODY );
	}

	/**
	 * Release all allocated resources(state).
	 */
	public void relase(){
		super.release();
		name = null;
		property = null;
		convertTo = null;
		type = null;
		defaultValue = null;
		scope = null;
	}

}