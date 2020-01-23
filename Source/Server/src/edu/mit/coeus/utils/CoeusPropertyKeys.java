/*
 * CoeusPropertyKeys.java
 *
 * Created on November 29, 2004, 11:44 AM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.utils;

/**
 *
 * @author  Geo Thomas
 * All the<code> keys </code>defined in the Coeus.properties file should be defined in this
 */
public interface CoeusPropertyKeys {
    public static final String LOG_HOME = "LOG_HOME";
    public static final String COEUS_HOME = "COEUS_HOME";
    public static final String COEUS_GLOBAL_IMAGE = "COEUS_GLOBAL_IMAGE";
    public static final String SEARCH_FILE_NAME = "SEARCH_FILE_NAME";
    // Case# 2775- Extending Coeus Search XML File
    public static final String LOCAL_SEARCH_FILE_NAME = "LOCAL_SEARCH_FILE_NAME";
    public static final String CODETABLE_XML_FILE_NAME = "CODETABLE_XML_FILE_NAME";
    public static final String SWING_LOGIN_MODE = "SWING_LOGIN_MODE";
    public static final String PARAMETER_FILE_NAME = "PARAMETER_FILE_NAME";
    public static final String REPORT_GENERATED_PATH = "REPORT_GENERATED_PATH";
    public static final String Domain = "Domain";
    public static final String SMTPHost = "SMTPHost";
    public static final String GENERATE_XML_FOR_DEBUGGING = "GENERATE_XML_FOR_DEBUGGING";
    public static final String DS_JNDI_NAME = "DS_JNDI_NAME";
    public static final String CONTEXT_FACTORY = "CONTEXT_FACTORY";
    public static final String CONTEXT_URL = "CONTEXT_URL";
    public static final String POLLING_INTERVAL = "POLLING_INTERVAL";
    public static final String ENDING_CUSTOM_TAG = "ENDING_CUSTOM_TAG";
    public static final String STARTING_CUSTOM_TAG = "STARTING_CUSTOM_TAG";
    public static final String APPLICATION_URL = "APPLICATION_URL";
    public static final String APP_HOME_URL = "APP_HOME_URL";
    public static final String WEB_APP_HOME_URL = "WEB_APP_HOME_URL";
    public static final String SCHOOL_NAME = "SCHOOL_NAME";
    public static final String SCHOOL_ACRONYM = "SCHOOL_ACRONYM";
    public static final String TEMP_PROPOSAL_BEGIN = "TEMP_PROPOSAL_BEGIN";
    public static final String DEFAULT_TYPE_CODE = "DEFAULT_TYPE_CODE";
    public static final String DEFAULT_SEQ_NUM = "DEFAULT_SEQ_NUM";
    public static final String DEFAULT_ORG_REL_TYPE = "DEFAULT_ORG_REL_TYPE";
    public static final String DEFAULT_TYPE = "DEFAULT_TYPE";
    public static final String DEFAULT_ACTION_TYPE = "DEFAULT_ACTION_TYPE";
    public static final String LOGIN_MODE = "LOGIN_MODE";
    public static final String SECONDARY_LOGIN_MODE = "SECONDARY_LOGIN_MODE";
    public static final String EMAIL_STRING = "EMAIL_STRING";
    public static final String SEARCH_DEBUG = "SEARCH_DEBUG";
    /* Added by Shivakumar for SAP Feed Generation -- 23 Dec 2004 */
    public static final String DEVELOPMENT = "DEVELOPMENT";
    public static final String TEST = "TEST";
    public static final String PRODUCTION = "PRODUCTION";
    // End Shivakumar
    public static final String HELP_URL = "HELP_URL";
    public static final String LICENSE_FILE_NAME = "LICENSE_FILE_NAME";
    
    public static final String KERBEROS_REALM = "java.security.krb5.realm";
    public static final String KERBEROS_KDC_SERVER = "java.security.krb5.kdc";

    public static final String JDBC_DRIVER_URL = "JDBCDriverUrl";
    public static final String JDBC_DRIVER = "Driver";
    public static final String WEB_LOGIN_MODE = "WEB_LOGIN_MODE";
    public static final String DB_INSTANCE_NAME ="DB_INSTANCE_NAME"; 
     /* To get the Coeus Product version number 
     *Enhancement 2019
     */
    public static final String PRODUCT_VERSION = "PRODUCT_VERSION";
    
    //ClientAuthe class name key
    public static final String CLIENT_AUTH_CLASS = "CLIENT_AUTH_CLASS";
    
    public static final String LOCAL_TIMEZONE_ID = "LOCAL_TIMEZONE_ID";
    public static final String COEUS_HOME_URL = "COEUS_HOME_URL";
    
    //Case 3243 - Bringing server side properties to client side
    public static final String SEARCH_DATE_FORMAT = "SEARCH_DATE_FORMAT";
    
    //FOR Dartmouth COI module
    public static final String COI_MODULE = "COI_MODULE";
    public static final String POLICY_FILE = "POLICY_FILE"; 

}
