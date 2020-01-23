/*
 * @(#)ParameterUtils.java 1.0 February 17, 2009, 7:25 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */


package edu.mit.coeus.utils;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBException;

/**
 *
 * @author satheeshkumarkn
 */
public class ParameterUtils {
    
    /**
     * Default value for MAX_ACCOUNT_NUMBER_LENGTH parameter
     */
    private static final String DEFAULT_MAX_ACCOUNT_NUMBER_LENGTH = "100";
    
    /**
     *  Method to get MAX_ACCOUNT_NUMBER_LENGTH parameter value
     *  @return parameterValue
     */
    public static String getMaxAccountNumberLength()throws CoeusException, DBException{
        String parameterValue = "";
        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        parameterValue = coeusFunctions.getParameterValue(CoeusConstants.MAX_ACCOUNT_NUMBER_LENGTH);
        if(parameterValue == null || parameterValue.equals("0")){
            parameterValue = DEFAULT_MAX_ACCOUNT_NUMBER_LENGTH;
            UtilFactory.log("Invalid "+CoeusConstants.MAX_ACCOUNT_NUMBER_LENGTH+
                    " parameter value, default value set's to "+DEFAULT_MAX_ACCOUNT_NUMBER_LENGTH);
        }
        return parameterValue;
    }
    
   /**
     *  Method to get PROPOSAL_PRINTING_HIERARCHY parameter value
     *  @return parameterValue
     */
    public static String getPropPrintHierarchyName()throws CoeusException, DBException{
        String parameterValue = "";
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        parameterValue = coeusFunctions.getParameterValue(CoeusConstants.SPONSOR_HIERARCHY_FOR_PRINTING);
        if(parameterValue == null){
            parameterValue = "";
        }
        return parameterValue;
    }
    
}
