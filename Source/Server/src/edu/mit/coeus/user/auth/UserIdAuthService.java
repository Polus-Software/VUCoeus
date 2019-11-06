/*
 * UserIdAuthService.java
 *
 * Created on August 28, 2006, 1:11 PM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.user.auth;

import edu.mit.coeus.bean.LoginBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBConnectionManager;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.irb.form.MyLogonForm;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Properties;

/**
 *
 * @author  Geo Thomas
 */
public class UserIdAuthService extends AuthServiceServerBase{
    private Hashtable data;
    private Properties props;
    private static String dbURL;
    /** Creates a new instance of UserIdAuthService */
    public UserIdAuthService() {
    }
    
    public void init(java.util.Properties props) {
        this.props = props;
    }
    public boolean authenticate() throws CoeusException {
        UtilFactory.log( "Trying to authenticate User with login mode as USERID");
        RequesterBean requester = (RequesterBean)data.get(RequesterBean.class.getName());
        String userName = "";
        String password = "";
        if(requester!=null){
            //for swing
            LoginBean loginBean = (LoginBean)requester.getDataObject();
            userName = loginBean.getUserId();
            password = loginBean.getPassword();
        }else{
            //for web
            MyLogonForm form = (MyLogonForm)data.get(MyLogonForm.class.getName());
            userName = form.getUsername();
            password = form.getPassword();
        }
        Connection con = null;
        Connection conn = null;
        try{
            //try to get the url from datasource connection
            if(dbURL==null){
                conn = new DBEngineImpl().beginTxn();
                DatabaseMetaData dbMetaData = conn.getMetaData();
                dbURL = dbMetaData.getURL();
            }
            //if not get it from coeus.properties file
            if(dbURL==null || dbURL.trim().equals("")){
                String DB_DRIVER = props.getProperty(CoeusPropertyKeys.JDBC_DRIVER);
                String dbURL = props.getProperty(CoeusPropertyKeys.JDBC_DRIVER_URL);
                Class.forName(DB_DRIVER);
            }
            //still not, throw error
            if(dbURL==null)
                throw new CoeusException("Existing oracle driver does not support getUrl() method," +
                        " Please mention "+
                        CoeusPropertyKeys.JDBC_DRIVER+" and " +
                        CoeusPropertyKeys.JDBC_DRIVER_URL+
                        " in coeus.properties file");
//            con = DriverManager.getConnection(DB_URL, userName, password);
//            Class.forName(dbDriverName);
            con = DriverManager.getConnection(dbURL, userName, password);
            if (con != null) {
                if(requester==null)
                    setResponseForWeb(userName);//for web
                else
                    setResponse(userName);//for swing
            }
            
        }catch(ClassNotFoundException ex){
            UtilFactory.log(ex.getMessage(),ex,"UserIdAuthService", "authenticate");
            throw new CoeusException(ex.getMessage());
        }catch(DBException ex){
            UtilFactory.log(ex.getMessage(),ex,"UserIdAuthService", "authenticate");
            throw new CoeusException(ex.getMessage());
        }catch(SQLException ex){
            UtilFactory.log(ex.getMessage(),ex,"UserIdAuthService", "authenticate");
            throw new CoeusException(ex.getMessage());
        }finally{
            try{
                if(conn!=null && !conn.isClosed()) conn.close();
                if(con!=null && !con.isClosed()) con.close();
            }catch(SQLException ex){
                UtilFactory.log(ex.getMessage(),ex,"UserIdAuthService", "authenticate");
                throw new CoeusException(ex.getMessage());
            }
        }
        return true;
    }
    
    public void setParams(java.util.Hashtable data) {
        this.data = data;
    }
}
