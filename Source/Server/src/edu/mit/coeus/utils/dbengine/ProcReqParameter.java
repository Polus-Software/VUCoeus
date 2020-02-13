/*
* @(#)Parameter.java 1.0 03/14/2002
*
* Copyright (c) Massachusetts Institute of Technology
* 77 Massachusetts Avenue, Cambridge, MA 02139-4307
* All rights reserved.
*
*/
package edu.mit.coeus.utils.dbengine;

import java.util.Vector;

/**
 * Class used to hold the attributes for executing the procedures.
 * To execute a procedure, <code>DBEngineImpl</code> class needs Data source name, 
 * Parameter class object list and SQL command. To execute more than one procedure
 * in one transaction, bundle the ProcReqParameter instances in a vector and 
 * pass the vector object into <code>executeStroeProcs</code> method.
 *
 * @version 1.0 April 10, 2002, 8:18 PM
 * @author  Geo Thomas
 */
public class ProcReqParameter {

    private String serviceName;
    
    private String dsn;
    
    private Vector parameterInfo;
    
    private String sqlCommand;
    
    /** Creates new ProcReqParameter */
    public ProcReqParameter() {
    }

    /** Three argument constructor to create a new ProcReqParameter*/
    public ProcReqParameter(String dataSourceName,String sqlCommand,Vector paramInfo) {
        this.dsn = dataSourceName;
        this.sqlCommand = sqlCommand;
        this.parameterInfo = paramInfo;
    }

    /**
     *  Gets the Service name
     *  @return String Service name
     */
    public String getServiceName() {
        return serviceName;
    }
    
    /**
     *  Sets the Service name
     *  @param String Service name
     */
    public void setServiceName(String strServiceName) {
        this.serviceName = strServiceName;
    }

    /**
     *  Gets the Data Source Name
     *  @return String Data Source name
     */
    public String getDSN() {
        return dsn;
    }
    
    /**
     *  Sets the Data Source Name
     *  @param String Data Source name
     */
    public void setDSN(String strDSN) {
        this.dsn = strDSN;
    }

    /**
     *  Gets the ParameterInfo
     *  @return Vector Parameter List
     */
    public Vector getParameterInfo() {
        return parameterInfo;
    }
    
    /**
     *  Sets the ParameterInfo
     *  @param Vector Parameter List
     */
    public void setParameterInfo(Vector vectParameterInfo) {
        this.parameterInfo = vectParameterInfo;
    }

    /**
     *  Gets the SQLCommand
     *  @return String SQLCommand
     */
    public String getSqlCommand() {
        return sqlCommand;
    }
    
    /**
     *  Sets the SQLCommand
     *  @return String SQLCommand
     */
    public void setSqlCommand(String strSqlCommand) {
        this.sqlCommand = strSqlCommand;
    }
}
