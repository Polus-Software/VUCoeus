/*
 * UtilityServlet.java
 *
 * Created on September 3, 2004, 2:18 PM
 */

package edu.mit.coeus.servlet;


/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  ajaygm
 */
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.documenttype.DocumentCache;
import edu.mit.coeus.utils.documenttype.DocumentTypeChecker;
import java.sql.DatabaseMetaData;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.*;
import java.lang.ClassNotFoundException;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;

public class UtilityServlet extends CoeusBaseServlet{
    
//    private UtilFactory UtilFactory;    
    
    private final String strDBDriver = "Driver";
    private final String strDBDriverUrl = "JDBCDriverUrl";
    
    private static final char GET_PARAMETER_VALUE = 'A';
    //New functionality for changing password
    private static final char CHANGE_PASSWORD = 'B';
    /* New functionality added for updating OSP$FIRST_TIME_LOGIN
     */
    private static final char UPDATE_FIRST_TIME_LOGIN = 'C';
    /** Enhancement 2019 */
    private static final char GET_PRODUCT_VERSION = 'D'; 
    private static String dbURL;
    
    //Added for Coeus 4.3 PT ID 2320 - Ability to reset user password - start
    private static final char RESET_PASSWORD = 'E';
    private static final char GET_USER_RIGHTS = 'F'; 
    //Added for Coeus 4.3 PT ID 2320 - Ability to reset user password - end
    
    //Added for PT ID#3243 - Bringing server side properties to client side - start
    private static final char READ_COEUS_PROPERTIES = 'G';
    //Added for PT ID#3243 - Bringing server side properties to client side - end
    //Added for case 4007: Icon based on mime Type
     private static final char GET_DOCUMENT_TYPE_LIST = 'H';
     /**
     * This method handles all the POST requests from the Client
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if any ServletException
     * @throws IOException if any IOException
     */    
    protected void doPost(HttpServletRequest httpServletRequest,
    HttpServletResponse httpServletResponse) throws ServletException, IOException {
        
        // the request object from applet
        RequesterBean requester = null;
        // the response object to applet
        ResponderBean responder = new ResponderBean();
        
        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        
//        UtilFactory = new UtilFactory();
        
        
        
        try{
            // get an input stream
            inputFromApplet = new ObjectInputStream(httpServletRequest.getInputStream());
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            char functionType = requester.getFunctionType();
            
            if(functionType == GET_PARAMETER_VALUE){
                DepartmentPersonTxnBean departmentPersonTxnBean
                        = new DepartmentPersonTxnBean();
                String parmValue = (String)requester.getDataObject();
                try{
                    String value = departmentPersonTxnBean.getParameterValues(parmValue);
                    responder.setDataObject(value);
                    responder.setMessage(null);
                    responder.setResponseStatus(true);
                }catch (CoeusException coeusEx){                    
                    UtilFactory.log(coeusEx.getMessage(), coeusEx, "UtilityServlet", "doPost");                 
                    int index=0;
                    String errMsg;
                    if(coeusEx.getErrorId()==999999){
                        errMsg = "dbEngine_intlErr_exceptionCode.1028";
                        responder.setLocked(true);
                    }else{
                        errMsg = coeusEx.getMessage();
                    }
                    CoeusMessageResourcesBean coeusMessageResourcesBean
                                =new CoeusMessageResourcesBean();
                    
                    errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
                    //responder.setException(coeusEx);
                    responder.setResponseStatus(false);
                    responder.setMessage(errMsg);
                    UtilFactory.log( errMsg, coeusEx,
                    "UtilityServlet", "doPost");
                }catch (DBException dbEx){                    
                    UtilFactory.log(dbEx.getMessage(), dbEx, "UtilityServlet", "doPost");
                    int index=0;
                    String errMsg = dbEx.getUserMessage();
                    if (dbEx.getErrorId() == 20102 ) {
                        errMsg = "dbEngine_intlErr_exceptionCode.1028";
                    }
                    if (errMsg.equals("db_exceptionCode.1111")) {
                        responder.setCloseRequired(true);
                    }
                    CoeusMessageResourcesBean coeusMessageResourcesBean
                               = new CoeusMessageResourcesBean();
                    errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
                    responder.setResponseStatus(false);
                    
                    responder.setMessage(errMsg);
                    UtilFactory.log( errMsg, dbEx,
                    "UtilityServlet", "doPost");
                }
            }//End if
            if(functionType == CHANGE_PASSWORD){
                //Getting the username
                String userName = (String)requester.getId();
                
                Vector vecPasswords = (Vector)requester.getDataObject();
                //Getting the old password
                String oldPassword = (String)vecPasswords.elementAt(0);
                //Getting the new password
                String newPassword = (String)vecPasswords.elementAt(1);
                //Modified for Password change problem.    
                //Try to get the url from datasource connection
                Connection conn = null;
                Connection con = null;
                try{
                    if(dbURL==null){
                        conn = new DBEngineImpl().beginTxn();
                        DatabaseMetaData dbMetaData = conn.getMetaData();
                        dbURL = dbMetaData.getURL();
                    }
                    //If not get it from coeus.properties file
                    if(dbURL==null || dbURL.trim().equals("")){
                        String DB_DRIVER = CoeusProperties.getProperty(CoeusPropertyKeys.JDBC_DRIVER);
                        String dbURL = CoeusProperties.getProperty(CoeusPropertyKeys.JDBC_DRIVER_URL);
                        Class.forName(DB_DRIVER);
                    }
                    if (dbURL == null) {
                        UtilFactory.log("Existing oracle driver does not support getUrl() method," +
                                " Please mention "+
                                CoeusPropertyKeys.JDBC_DRIVER+" and " +
                                CoeusPropertyKeys.JDBC_DRIVER_URL+
                                " in coeus.properties file", null, "LoginServlet", "initProps");
                        return;
                    }
                    
                    con = DriverManager.getConnection(dbURL, userName, oldPassword);
                }catch(ClassNotFoundException sqlEx){
                    UtilFactory.log(sqlEx.getMessage(),sqlEx,"UtilityServlet", "perform");
                    throw new CoeusException(sqlEx.getMessage());
                }catch(DBException sqlEx){
                    UtilFactory.log(sqlEx.getMessage(),sqlEx,"UtilityServlet", "perform");
                    throw new CoeusException(sqlEx.getMessage());
                }catch (SQLException sqlEx){
                    /* SQLException will contain any Oracle error message.  Set exception in
                     * the response bean, so that message can be displayed to user. */
                    UtilFactory.log(sqlEx.getMessage(),sqlEx,"UtilityServlet","perform");
                    //responder.setException(sqlEx);
                    responder.setMessage("Your current password is incorrect");
                    responder.setResponseStatus(false);
                }
                if(con != null){
                    UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                    boolean passwordCheck = userMaintDataTxnBean.changePassword(userName, newPassword);
                    if(!passwordCheck){
                        responder.setMessage("Your password was not changed due to a database error");
                        responder.setResponseStatus(false);
                    }else{
                        responder.setMessage("Your password has been successfully changed");
                        responder.setResponseStatus(true);
                    }
                    try {
                        con.close();
                    }catch (SQLException e) {                        
                        UtilFactory.log(e.getMessage(), e, "UtilityServlet", "doPost");                 
                    }
                }
            }if(functionType == UPDATE_FIRST_TIME_LOGIN){
                //Getting the username
                String userName = (String)requester.getId();
                
                String password = (String)requester.getDataObject();
                
                boolean updStatus = false;
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                boolean passwordCheck = userMaintDataTxnBean.changePassword(userName, password);
  
                boolean resStatus = false;
                if(passwordCheck){
                    updStatus = userMaintDataTxnBean.updateFirstTimeLoginUser(userName);
                    if(updStatus){
                        resStatus = true;
                        responder.setMessage("Your password has been successfully changed");
                        //responder.setResponseStatus(true);
                    }else{
                        resStatus = false;
                        responder.setMessage("An error was encountered while attempting to initialize the userid.");
                        //responder.setResponseStatus(false);
                    }
                }else{
                    resStatus = false;
                    responder.setMessage("Your password was not changed due to a database error");
                    //responder.setResponseStatus(false);
                }
                responder.setResponseStatus(resStatus);
                /** Enhancement 2019
                 *Read the coeus product version from property file
                 */
            }else if(functionType == GET_PRODUCT_VERSION){
                String version = CoeusProperties.getProperty(CoeusPropertyKeys.PRODUCT_VERSION);
                responder.setDataObject(version);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            //Added for Coeus 4.3 PT ID 2320 - Ability to reset user password - start
            else if(functionType == RESET_PASSWORD){
                Vector vecInput = (Vector)requester.getDataObject();                
                String newPassword = (String)vecInput.get(0);
                String userId = (String)vecInput.get(1);      
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                boolean passwordCheck = userMaintDataTxnBean.changePassword(userId, newPassword);
                if(!passwordCheck){
                    responder.setMessage("Your password was not reset due to a database error");
                    responder.setResponseStatus(false);
                }else{
                    responder.setMessage("Your password has been successfully reset");
                    responder.setResponseStatus(true);
                }                
            }else if(functionType == GET_USER_RIGHTS){                    
                Vector vecInput = (Vector)requester.getDataObject();
                String unitNumber = (String)vecInput.get(0);
                String userId = (String)vecInput.get(1);             
                Vector vecRights = (Vector)vecInput.get(2);
                HashMap hmRights = new HashMap();                
                try{
                    UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();                
                    for(int index = 0; index < vecRights.size(); index++){
                        hmRights.put(vecRights.get(index), 
                                new Boolean(userMaintDataTxnBean.getUserHasRight(userId, (String)vecRights.get(index), unitNumber)));
                    }

                }catch(Exception ex){   
                   UtilFactory.log(ex.getMessage(), ex, "UtilityServlet", "doPost");  
                }                
                responder.setDataObject(hmRights);
                responder.setResponseStatus(true);                
            }         
            //Added for Coeus 4.3 PT ID 2320 - Ability to reset user password - end
            //Added for PT ID#3243 - Bringing server side properties to client side - start
            else if(functionType == READ_COEUS_PROPERTIES){
                Properties properties = null;
                try{
                    properties = CoeusProperties.getPropertyList();                    
                    responder.setResponseStatus(true);              
                }catch(Exception ex){
                    UtilFactory.log(ex.getMessage(), ex, "UtilityServlet", "doPost");  
                    responder.setMessage("Error while getting property list");                    
                    responder.setResponseStatus(false);
                }
                responder.setDataObject(properties);
            }
            //Added for PT ID#3243 - Bringing server side properties to client side - end
            //Added for Coeus 4.3 : PT ID 2232 - Custom Roles - end
            else if(functionType == GET_DOCUMENT_TYPE_LIST){
                DocumentCache documentCache = DocumentCache.getInstance();
                DocumentTypeChecker documentChecker = new DocumentTypeChecker();
                Vector dataObjects = new Vector();
                dataObjects.add(documentCache.getCache());
                dataObjects.add(documentChecker.getMimeTypeDetectionMode());
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            //4007 end
        }catch (CoeusException coeusEx){                
                //UtilFactory.log(coeusEx.getMessage(), coeusEx, "UtilityServlet", "doPost");                 
                int index=0;
                String errMsg;
                if(coeusEx.getErrorId()==999999){
                    errMsg = "dbEngine_intlErr_exceptionCode.1028";
                    responder.setLocked(true);
                }else{
                    errMsg = coeusEx.toString();
                }
                CoeusMessageResourcesBean coeusMessageResourcesBean
                            =new CoeusMessageResourcesBean();
                errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
                //responder.setException(coeusEx);
                responder.setResponseStatus(false);
                responder.setMessage(errMsg);
                UtilFactory.log( errMsg, coeusEx,
                "UtilityServlet", "doPost");
        }catch (DBException dbEx){                
                //UtilFactory.log(dbEx.getMessage(), dbEx, "UtilityServlet", "doPost");                 
                int index=0;
                String errMsg = dbEx.getUserMessage();
                if(dbEx.getErrorId() == 0 ){
                    errMsg = "dbEngine_intlErr_exceptionCode.1050";                    
                }    
                if (dbEx.getErrorId() == 20102 ) {
                    errMsg = "dbEngine_intlErr_exceptionCode.1028";
                }
                //Added for Coeus 4.3 PT ID 2320 - Ability to reset user password - start
                if (dbEx.getErrorId() == 20100 ) {
                    errMsg = "resetPassword_exceptionCode.1000";
                }   
                //Added for Coeus 4.3 PT ID 2320 - Ability to reset user password - end
                if (errMsg.equals("db_exceptionCode.1111")) {
                    responder.setCloseRequired(true);
                }
                CoeusMessageResourcesBean coeusMessageResourcesBean
                           = new CoeusMessageResourcesBean();
                errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
                CoeusException coeusException = new CoeusException(errMsg);
                //responder.setException(coeusException);                                
                responder.setResponseStatus(true);

                responder.setMessage(errMsg);
                UtilFactory.log( errMsg, dbEx,
                "UtilityServlet", "doPost");
        }catch (ClassNotFoundException ex){            
            UtilFactory.log(ex.getMessage(), ex, "UtilityServlet", "doPost");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "UtilityServlet", "doPost");
        //Case 3193 - END
        }finally {
            try{
                outputToApplet
                    = new ObjectOutputStream(httpServletResponse.getOutputStream());
                outputToApplet.writeObject(responder);
                //close the streams
                if (inputFromApplet!=null){
                    inputFromApplet.close();
                }
                if (outputToApplet!=null){
                    outputToApplet.flush();
                    outputToApplet.close();
                }
            }catch (IOException ioe){
                UtilFactory.log( ioe.getMessage(), ioe,
                "UtilityServlet", "doPost");
            }
        }//End finally
    }
    
}//End class
