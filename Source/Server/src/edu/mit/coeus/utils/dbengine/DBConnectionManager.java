/*
 * @(#) DBConnectionManager.java 1.0 03/14/2002
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.utils.dbengine;

import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import java.io.PrintWriter;
import java.io.InputStream;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Date;

import edu.mit.coeus.utils.UtilFactory;
//To implement DataSource 6/11/2003 - start
import javax.naming.*;
import javax.sql.DataSource;
//To implement DataSource 6/11/2003 - end

/**
 * This class is a Singleton that provides access to one or many
 * connection pools defined in a Property file. A client gets
 * access to the single instance through the static getInstance()
 * method and can then check-out and check-in connections from a pool.
 * When the client shuts down it should call the release() method
 * to close all open connections and do other clean up.
 *
 * @version 1.0 March 14, 2002, 10:31 PM
 * @author  Geo Thomas
 */
public class DBConnectionManager {
    static private DBConnectionManager instance;       // The single instance
    static private int clients;
    //To implement DataSource 6/11/2003 - start
    static private DataSource ds ;
    //To implement DataSource 6/11/2003 - end
    
    /*
     *  Constants used for fetching database properties from coeusDB.properties file
     */
    private final String kStrDBDriver = DBEngineConstants.kStrDBDriver;
    private final String kStrDBDataSourceName = DBEngineConstants.kStrDBDataSourceName;
    private final String kStrDBLogFile = DBEngineConstants.kStrDBLogFile;
    private final String kStrDBUserName = DBEngineConstants.kStrDBUserName;
    private final String kStrDBPassword = DBEngineConstants.kStrDBPassword;
    private final String kStrDBRequestDB = DBEngineConstants.kStrDBRequestDB;
    private final String kStrDBNoOfNodes = DBEngineConstants.kStrDBNoOfNodes;
    private final String kStrDBDriverUrl = DBEngineConstants.kStrDBDriverUrl;
    
    private final String kStrLockWaitInterval = DBEngineConstants.kStrLockWaitInterval;
    
    private UtilFactory UtilFactory;
    private Vector drivers = new Vector();
    private PrintWriter log;
    private Hashtable pools = new Hashtable();
    
    /**
     * Returns the single instance, creating one if it's the
     * first time this method is called.
     *
     * @return DBConnectionManager The single instance.
     */
    static synchronized public DBConnectionManager getInstance() {
        if (instance == null) {
            instance = new DBConnectionManager();
        }
        clients++;
        return instance;
    }
    
    /**
     * A private constructor since this is a Singleton
     */
    private DBConnectionManager() {
        //To implement DataSource 6/11/2003 - start    
        //init();
//        InputStream is = getClass().getResourceAsStream("/coeus.properties");
//        Properties dbProps = new Properties();
////        UtilFactory = new UtilFactory();
//        try {
//            dbProps.load(is);
//        }catch (Exception e) {
//            log(e,"Can't read the properties file. " +
//                "Make sure coeus.properties is in the CLASSPATH");
//            return;
//        }        
        try{
            /*
            String jndiName = dbProps.getProperty("DS_JNDI_NAME","jdbc/coeus");
            Context initContext = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:comp/env");
            //ds = (DataSource)envContext.lookup("jdbc/myoracle");
            ds = (DataSource)envContext.lookup(jndiName);
            */
            String jndiName = CoeusProperties.getProperty(CoeusPropertyKeys.DS_JNDI_NAME, "jdbc/coeus");            
            String initialContextFactory = CoeusProperties.getProperty(CoeusPropertyKeys.CONTEXT_FACTORY,"");
            String contextURL = CoeusProperties.getProperty(CoeusPropertyKeys.CONTEXT_URL,"");
            Properties p = new Properties();
            Context envContext;
            //For other App Servers use the respective Context Factory for JNDI look up
            if(!initialContextFactory.equals("") && !contextURL.equals("")){
                p.put("java.naming.factory.initial", initialContextFactory);
                p.put("java.naming.provider.url", contextURL);
                envContext = new InitialContext(p);
            }else{
                //For tomcat no need to use Context Factory for JNDI look up
                Context initContext = new InitialContext();
                envContext  = (Context)initContext.lookup("java:comp/env");
            }
            ds = (DataSource)envContext.lookup(jndiName);
        }catch(Exception e) {
           log(e,"Could not get Data Source through JNDI");
        }
        //To implement DataSource 6/11/2003 - end
    }
    
    /**
     * Returns a connection to the named pool.
     *
     * @param name The pool name as defined in the properties file
     * @param con The Connection
     */
    public void freeConnection(String name, Connection con) throws SQLException{
        //To implement DataSource 6/11/2003 - start
        /*DBConnectionPool pool = (DBConnectionPool) pools.get(name);
        if (pool != null && con != null) {
            pool.freeConnection(con);
        }
        */
        con.close();
        //To implement DataSource 6/11/2003 - end
    }
    /**
     * Returns an open connection. If no one is available, and the max
     * number of connections has not been reached, a new connection is
     * created.
     *
     * @param name The pool name as defined in the properties file
     * @return Connection The connection or null
     */
    public Connection getConnection(String name) throws SQLException{
        //To implement DataSource 6/11/2003 - start
        /*DBConnectionPool pool = (DBConnectionPool) pools.get(name);
        if (pool != null) {
            return pool.getConnection();
        }
        return null;*/
        Connection con = null ;
        try{
            con = ds.getConnection();
            if(ds==null) throw new SQLException("Datasource is null\n" +
            "Make sure that the DS_JNDI_NAME specified in the Coeus.properties file is correct\n" +
            "Make sure that CONTEXT_FACTORY and CONTEXT_URL are defined, if necessary");
        }catch(SQLException ex){
            log(ex,"Could not get Connection from DataSource");
            throw ex;
        }
        return con;        
        //To implement DataSource 6/11/2003 - end
    }
    /**
     * Returns an open connection. If no one is available, and the max
     * number of connections has not been reached, a new connection is
     * created. If the max number has been reached, waits until one
     * is available or the specified time has elapsed.
     *
     * @param name The pool name as defined in the properties file
     * @param time The number of milliseconds to wait
     * @return Connection The connection or null
     */
    public Connection getConnection(String name, long time) throws SQLException{
        //To implement DataSource 6/11/2003 - start
        /*DBConnectionPool pool = (DBConnectionPool) pools.get(name);
        if (pool != null) {
            return pool.getConnection(time);
        }
        return null;*/
        Connection con = null;
        try{
            con = ds.getConnection();        
        }catch(Exception ex){
            log(ex,"Could not get Connection from DataSource");
        }
        return con;           
        //To implement DataSource 6/11/2003 - end
    }
    /**
     * Closes all open connections and deregisters all drivers.
     */
    public synchronized void release() {
        //To implement DataSource 6/11/2003 - start
        // Wait until called by the last client
        /*if (--clients != 0) {
            return;
        }
        
        Enumeration allPools = pools.elements();
        while (allPools.hasMoreElements()) {
            DBConnectionPool pool = (DBConnectionPool) allPools.nextElement();
            pool.release();
        }
        Enumeration allDrivers = drivers.elements();
        while (allDrivers.hasMoreElements()) {
            Driver driver = (Driver) allDrivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                log("Deregistered JDBC driver " + driver.getClass().getName());
            }
            catch (SQLException e) {
                log(e,"Can't deregister JDBC driver: " + driver.getClass().getName());
            }
        }*/
        //To implement DataSource 6/11/2003 - end
    }
    
    /**
     * Creates instances of DBConnectionPool based on the properties.
     * A DBConnectionPool can be defined with the following properties:
     * <PRE>
     * &lt;poolname&gt;.url         The JDBC URL for the database
     * &lt;poolname&gt;.user        A database user (optional)
     * &lt;poolname&gt;.password    A database user password (if user specified)
     * &lt;poolname&gt;.maxconn     The maximal number of connections (optional)
     * </PRE>
     *
     * @param props The connection pool properties
     */
    private void createPools(Properties dbProps) {
        String strDataSourceName = dbProps.getProperty(kStrDBDataSourceName, "coeus");
        String strDriver = dbProps.getProperty(kStrDBDriver, "ociDriver");
        String strUserName = dbProps.getProperty(kStrDBUserName);
        String strPassword = (String)dbProps.get(kStrDBPassword);
        String strMaxConn = (String)dbProps.get(kStrDBNoOfNodes);
        String strJDBCDriverUrl = (String)dbProps.get(kStrDBDriverUrl);
        String lockWaitInterval = (String)dbProps.getProperty(kStrLockWaitInterval,"15");
        DBEngineConstants.setLockInterval(lockWaitInterval);
        if (strJDBCDriverUrl == null) {
            log("No URL specified for " + strDataSourceName);
            return;
        }
        int max;
        try {
            max = Integer.valueOf(strMaxConn).intValue();
        }
        catch (NumberFormatException e) {
            log("Invalid maxconn value " + strMaxConn + " for " + strDataSourceName);
            max = 0;
        }
        DBConnectionPool pool = new DBConnectionPool(strDataSourceName, 
                strJDBCDriverUrl, strUserName, strPassword, max);
        pools.put(strDataSourceName, pool);
        log("Initialized pool " + strDataSourceName);
    }
    
    /**
     * Loads properties and initializes the instance with its values.
     */
    private void init() {
        InputStream is = getClass().getResourceAsStream("/coeusDB.properties");
        Properties dbProps = new Properties();
//        UtilFactory = new UtilFactory();
        try {
            dbProps.load(is);
        }catch (Exception e) {
            System.err.println("Can't read the properties file. " +
            "Make sure db.properties is in the CLASSPATH");
            return;
        }
        loadDrivers(dbProps);
        createPools(dbProps);
    }
    
    /**
     * Loads and registers all JDBC drivers. This is done by the
     * DBConnectionManager, as opposed to the DBConnectionPool,
     * since many pools may share the same driver.
     *
     * @param props The connection pool properties
     */
    private void loadDrivers(Properties props) {
        String driverClasses = props.getProperty(kStrDBDriver,"ociDriver");
        StringTokenizer st = new StringTokenizer(driverClasses);
        while (st.hasMoreElements()) {
            String driverClassName = st.nextToken().trim();
            try {
                Driver driver = (Driver)
                Class.forName(driverClassName).newInstance();
                DriverManager.registerDriver(driver);
                drivers.addElement(driver);
                log("Registered JDBC driver " + driverClassName);
            }
            catch (Exception e) {
                log("Can't register JDBC driver: " +
                driverClassName + ", Exception: " + e);
            }
        }
    }
    
    /**
     * Writes a message to the log file.
     */
    private void log(String msg) {
        UtilFactory.log(new Date() + ": " + msg,null,"","");
    }
    
    /**
     * Writes a message with an Exception to the log file.
     */
    private void log(Exception e, String msg) {
        UtilFactory.log(new Date() + ": " + msg,e,"","");
    }
    
    /**
     * This inner class represents a connection pool. It creates new
     * connections on demand, up to a max number if specified.
     * It also makes sure a connection is still open before it is
     * returned to a client.
     */
    static int newConnCnt;
    class DBConnectionPool {
        private int checkedOut;
        private Vector freeConnections = new Vector();
        private Hashtable lockedConnections = new Hashtable();
        private int maxConn;
        private String name;
        private String password;
        private String URL;
        private String user;
        
        /**
         * Creates new connection pool.
         *
         * @param name The pool name
         * @param URL The JDBC URL for the database
         * @param user The database user, or null
         * @param password The database user password, or null
         * @param maxConn The maximal number of connections, or 0
         *   for no limit
         */
        public DBConnectionPool(String name, String URL, String user, String password,
        int maxConn) {
            this.name = name;
            this.URL = URL;
            this.user = user;
            this.password = password;
            this.maxConn = maxConn;
        }
        
        /**
         * Checks in a connection to the pool. Notify other Threads that
         * may be waiting for a connection.
         *
         * @param con The connection to check in
         */
        public synchronized void freeConnection(Connection con) throws SQLException{
            // Put the connection at the end of the Vector if the connection is valid
            if(isValid(con)){
                freeConnections.addElement(con);
                checkedOut--;
                notifyAll();
            }
        }
        public synchronized void lockConnection(String refId,Connection con){
            lockedConnections.put(refId,con);
        }
        public synchronized void unLockConnection(String refId){
            if(refId!=null)
                lockedConnections.remove(refId);
        }
        /**
         * Checks out a connection from the pool. If no free connection
         * is available, a new connection is created unless the max
         * number of connections has been reached. If a free connection
         * has been closed by the database, it's removed from the pool
         * and this method is called again recursively.
         */
        public synchronized Connection getConnection() throws SQLException{
            Connection con = null;
            if (freeConnections.size() > 0) {
                // Pick the first Connection in the Vector
                // to get round-robin usage
                con = (Connection) freeConnections.firstElement();
                freeConnections.removeElementAt(0);
                if (!isValid(con)) {
                    //if (con.isClosed()) {
                    log("Removed bad connection from " + name);
                    // Try again recursively
                    con = getConnection();
                }
            }else if (maxConn == 0 || checkedOut < maxConn) {
                con = newConnection();
            }
            if (con != null) {
                checkedOut++;
            }
            return con;
        }
        
        /**
         * Checks out a connection from the pool. If no free connection
         * is available, a new connection is created unless the max
         * number of connections has been reached. If a free connection
         * has been closed by the database, it's removed from the pool
         * and this method is called again recursively.
         * <P>
         * If no connection is available and the max number has been
         * reached, this method waits the specified time for one to be
         * checked in.
         *
         * @param timeout The timeout value in milliseconds
         */
        public synchronized Connection getConnection(long timeout) throws SQLException{
            long startTime = new Date().getTime();
            Connection con;
            while ((con = getConnection()) == null) {
                try {
                    wait(timeout);
                }
                catch (InterruptedException e) {}
                if ((new Date().getTime() - startTime) >= timeout) {
                    // Timeout has expired
                    return null;
                }
            }
            return con;
        }
        
        /**
         * Closes all available connections.
         */
        public synchronized void release() {
            Enumeration allConnections = freeConnections.elements();
            while (allConnections.hasMoreElements()) {
                Connection con = (Connection) allConnections.nextElement();
                try {
                    con.close();
                    log("Closed connection for pool " + name);
                }
                catch (SQLException e) {
                    log(e, "Can't close connection for pool " + name);
                }
            }
            freeConnections.removeAllElements();
        }
        
        /**
         * Creates a new connection, using a userid and password
         * if specified.
         */
        private Connection newConnection() throws SQLException{
            Connection con = null;
            // try {
            if (user == null) {
                con = DriverManager.getConnection(URL);
            } else {
                con = DriverManager.getConnection(URL, user, password);
            }
            log("Created a new connection in pool " + name);
           /* }catch (SQLException e) {
                log(e, "Can't create a new connection for " + URL);
                return null;
            }*/
            return con;
        }
        /**
         * Check whether the new connection is valid or not.
         * It will check the validity by executing a light weight select statement
         * If it throws any SQLException, it returns false, else, it returns true
         * @return status of the connection.
         */
        private boolean isValid(Connection con) throws SQLException{
            Statement stmt = null;
            ResultSet rs = null;
            try {
                if(con.isClosed()){
                    return false;
                }else{
                    con.rollback();
                    /*stmt = con.createStatement();
                    rs = stmt.executeQuery("select sysdate from dual");
                     */
                }
            }catch (SQLException e) {
                log(e, "Connection is not valid");
                return false;
            }finally{
                try{
                    if(rs!=null){
                        rs.close();
                    }
                    if(stmt!=null){
                        stmt.close();
                    }
                }catch(SQLException sqlEx){
                    throw sqlEx;
                }
            }
            return true;
        }
    }
}