/*
 * @(#)FormatStringTag.java	1.0 06/13/2002 16:00:18
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

import java.util.Locale;
import java.text.NumberFormat;

import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;

import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.CoeusConstants;

/**
 * <code> FormatDateTag </code> a java bean that represents a Tag in Coeus
 * developed on struts classes.
 * Tag that retrieves the specified property of the specified bean, converts
 * it to a desired date format.
 * Alternatively, JSP may pass a String to be formatted.
 *
 * @version 1.0  June 13, 2002 16:00:18
 * @author RaYaKu
 */

public class FormatStringTag extends TagSupport{

        /**
         * String to be formatted.
         */
        protected String stringToFormat;

	/**
	 * Name of the bean that contains the data we will be rendering.
	 */
	protected String name;

	/**
	 * Name of the property to be accessed on the specified bean.
	 */
	protected String property;

	/**
	 * The fully qualified Java class name of the value to be exposed.
	 */
	protected String type;

	/**
	 * The scope to be searched to retrieve the specified bean.
	 */
	protected String scope;

        /**
         * The type of formatting that is desired.
         */
        protected String formatType;

        /**
         * Locale to be used for formatting.
         */
        protected Locale locale;

        /**
         * Get dateToFormat.
         * @return dateToFormat.
         */
        public String getStringToFormat(){
            return stringToFormat;
        }

        /**
         * Set dateToFormat.
         * @param dateToFormat
         */
        public void setStringToFormat(String stringToFormat){
            this.stringToFormat = stringToFormat;
        }

        /**
         * Get formatType.
         * @return formatType
         */
        public String getFormatType(){
            return formatType;
        }

        /**
         * Set formatType.
         * @param formatType
         */
        public void setFormatType(String formatType){
            this.formatType = formatType;
        }

        /**
         * Get the Locale
         * @return
         */
        public Locale getLocale(){
               return locale;
        }

        /**
         * Set the locale.
         * @param locale
         */
        public void setLocale(Locale locale){
            this.locale = locale;
        }
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
	public int doEndTag() throws JspException{
            String output =null;
//            UtilFactory UtilFactory = new UtilFactory();
            if(locale == null){
                locale = Locale.US;
            }

            // Look up the requested bean (if necessary)
            if( RequestUtils.lookup( pageContext , name , scope ) == null
                    && stringToFormat == null) {
                UtilFactory.log("Unable to instantiate bean in FormatStringTag",
                    null, "", "");
                return ( SKIP_BODY );  // Nothing to output
            }

            // Look up the requested property value
            String value =
                RequestUtils.lookup(pageContext, name, property, scope).toString();
            if ( value == null) {
                value = stringToFormat;
            }
            if( value == null || value.equals("") ){
                 output = "&nbsp;";
            }
            System.out.println("value: "+value);
            if(value != null && formatType != null &&
                    formatType.equalsIgnoreCase(CoeusConstants.CURRENCY_FORMAT) ){
                double dblValue = Double.parseDouble(value);
                output = NumberFormat.getCurrencyInstance(locale).format(dblValue);
            }

            //print the output
            ResponseUtils.write( pageContext , output );

            // Continue processing this page
            return ( EVAL_PAGE );
        }

	/**
	 * Release all allocated resources(state).
	 */
	public void relase(){
		super.release();
		name = null;
		property = null;
		type = null;
		scope = null;
	}

}