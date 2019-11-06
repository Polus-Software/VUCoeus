/*
* @(#)DataSource.java 1.0 03/14/2002
*
* Copyright (c) Massachusetts Institute of Technology
* 77 Massachusetts Avenue, Cambridge, MA 02139-4307
* All rights reserved.
*
*/

package edu.mit.coeus.utils.dbengine;

/**
 * This class is used to represent a datasource for implementing connection pool
 *
 * @version 1.0 March 14, 2002, 12:23 PM
 * @author  Geo Thomas
 */
public class DataSource {
    
    
    private String userName;
    private String password;
    private String jdbcDriverUrl;
    private String  requestDB;
    private String  requestTable;
    private String  driver;
    private int     numOfNodes;
    private String  dataSourceName;
    /**
     *  Contructor with seven arguments for creating data source
     *  @param String Data source name
     *  @param String driver class name
     *  @param String JDBC driver url
     *  @param String User name
     *  @param String password
     *  @param int Number of nodes
     *  @param String Database name
     */
    public DataSource(String DTS,String Drv, String JDBCDrv,String UsrName, String Pwd, int NoNds,String RDB) {
        userName = UsrName;
        password = Pwd;
        jdbcDriverUrl = JDBCDrv;
        requestDB=RDB;
        //requestTable=RT;
        driver=Drv;
        numOfNodes=NoNds;
        dataSourceName=DTS;
    }
    
    /**
     *  Get the database
     *  @return String data base
     */
    public String getRequestDB() {
        return requestDB;
    }
    
    /**
     *  Get the user name
     *  @return String user name
     */
    public String getUserName() {
        return userName;
    }
    
    /**
     *  Get the password
     *  @return String password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     *  Get the JDBC url
     *  @return String JDBC url
     */
    public String getJDBCDriverUrl() {
        return jdbcDriverUrl;
    }
    
    
    /**
     *  Get the data source name
     *  @return String data source name
     */
    public String getDataSourceName() {
        return dataSourceName;
    }
    
    /**
     *  Get the driver
     *  @return String driver
     */
    public String getDriver() {
        return driver;
    }
    
    /**
     *  Get the number of nodes
     *  @return String number of nodes
     */
    public int getNoOfNodes() {
        return numOfNodes;
    }
    
}
