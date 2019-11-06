/*
 * @(#)ModuleParameterList.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on June 16, 2003, 4:22 PM
 */

package edu.mit.coeus.moduleparameters.bean;

import java.util.HashMap;

/**
 * This class is used to hold the parameter values for all the modules.
 * @author  ravikanth
 */
public class ModuleParameterList extends HashMap {
    
    private HashMap parameterList;
    private ParameterBean parameterBean;
    /** Creates a new instance of ModuleParameterList */
    public ModuleParameterList() {
    }
    /**
     * Overloaded method which is used to store the parameter value for the given module
     * @param moduleName String representing the name of the module.
     * @param paramName String representing the parameter name.
     * @param paramValue String representing the parameter value.
     *
     */
    public void put(String moduleName,String paramName, String paramValue ) {
        if( this.containsKey( moduleName ) ) {
            parameterList = ( HashMap ) this.get( moduleName );
            if( parameterList != null ) {
                parameterBean = new ParameterBean( paramName, paramValue );
                parameterList.put(paramName,parameterBean);
            }
        }else{
            parameterList = new HashMap();
            parameterBean = new ParameterBean( paramName, paramValue );
            parameterList.put( paramName, parameterBean );
            
        }
        this.put( moduleName, parameterList );        
    }
    
    /**
     * Overloaded method to get the parameter value for the given parameter name and
     * module.
     * @param moduleName String representing the name of the module.
     * @param paramName String representing the parameter name.
     * @returns ParameterBean corresponding the specified paramName in given moduleName
     */
    public ParameterBean get(String moduleName, String paramName ) {
        ParameterBean returnBean = null;
        if( this.containsKey( moduleName ) ){
            parameterList = ( HashMap ) this.get( moduleName );
            if( parameterList != null ) {
                returnBean = ( ParameterBean ) parameterList.get(paramName);
            }
        }
        return returnBean;
    }
}
