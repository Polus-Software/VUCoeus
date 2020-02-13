/*
 * @(#)LoginServlet.java 1.0 15/25/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.servlet;

import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.bean.LoginBean;
import edu.mit.coeus.bean.ValidateUserTxnBean;
import edu.mit.coeus.utils.AuthenticationLogger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.*;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import edu.mit.coeus.utils.UtilFactory;
//Added by sharath to get UserInfoBean (28/7/2003)
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import com.oreilly.servlet.Base64Decoder;
import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.user.auth.AuthResponseListener;
import edu.mit.coeus.user.auth.CoeusAuthService;
import edu.mit.coeus.user.auth.CoeusAuthServiceFactory;
import edu.mit.coeus.user.auth.LDAPAuthentication;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.ucsd.coeus.personalization.Personalization;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * This servlet is a controller that performs user authentication and gets the role id
 * with the database.
 * For user authentication, the server sends the username,password to the database. any
 * exception at this stage indicates the failure of authentication.
 *
 * @version :1.0 July 23, 2002, 10:15 AM
 * @author Guptha K
 *
 */
public class LoginServlet extends HttpServlet {
    /* driver to connect the database */
    private String DB_DRIVER;
    /* url to connect the database */
    private String DB_URL;
    
    public void init(ServletConfig config) throws ServletException{
        super.init(config);
    }
    
    /**
     *  This method is used for applets.
     */
    public void doGet(HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
    }
    
    /**
     *  This method is used for applets.
     *  Gets an input stream from the applet, reads the username and password
     *  and authenticates with the database.
     */
    public void doPost(HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        
        String userId = null;
        String authId = null;
        // the request object from applet
        RequesterBean requester = null;
        // the response object to applet
        final ResponderBean responder = new ResponderBean();
        
        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        
        /* message to be returned to the applet */
        boolean success = false;
        
        try {
            // get an input stream
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            
            if (requester.getId().equalsIgnoreCase("LOGINMODE")) {
                HashMap hashProps = new HashMap() ;
                hashProps.put("LoginMode", CoeusProperties.getProperty(CoeusPropertyKeys.SWING_LOGIN_MODE)) ;
                UtilFactory.log(CoeusPropertyKeys.SWING_LOGIN_MODE +" : "+CoeusProperties.getProperty(CoeusPropertyKeys.SWING_LOGIN_MODE));
                hashProps.put(CoeusPropertyKeys.COEUS_GLOBAL_IMAGE,CoeusProperties.getProperty(CoeusPropertyKeys.COEUS_GLOBAL_IMAGE));
                hashProps.put(CoeusPropertyKeys.POLLING_INTERVAL,CoeusProperties.getProperty(CoeusPropertyKeys.POLLING_INTERVAL));
                hashProps.put(CoeusPropertyKeys.HELP_URL,new CoeusFunctions().getParameterValue(CoeusPropertyKeys.HELP_URL));
                hashProps.put(CoeusPropertyKeys.KERBEROS_KDC_SERVER,CoeusProperties.getProperty(CoeusPropertyKeys.KERBEROS_KDC_SERVER));
                hashProps.put(CoeusPropertyKeys.KERBEROS_REALM,CoeusProperties.getProperty(CoeusPropertyKeys.KERBEROS_REALM));
                hashProps.put(CoeusPropertyKeys.DB_INSTANCE_NAME,CoeusProperties.getProperty(CoeusPropertyKeys.DB_INSTANCE_NAME));
                hashProps.put(CoeusPropertyKeys.CLIENT_AUTH_CLASS,
                CoeusAuthServiceFactory.getCoeusAuthDetails(
                CoeusProperties.getProperty(CoeusPropertyKeys.SWING_LOGIN_MODE)));//get the class name from xml file
                hashProps.put("LOCAL_TIME_ZONE",UtilFactory.getLocalTimeZoneId());
                hashProps.put(CoeusPropertyKeys.SECONDARY_LOGIN_MODE,CoeusProperties.getProperty(CoeusPropertyKeys.SECONDARY_LOGIN_MODE));

                responder.setDataObject(hashProps) ;
                success = true ;
            }else if (requester.getId().equalsIgnoreCase("APP_DETAILS")) {
                HashMap hashProps = new HashMap() ;
                hashProps.put("DataSource", CoeusProperties.getProperty(CoeusPropertyKeys.DS_JNDI_NAME)) ;
                hashProps.put(CoeusPropertyKeys.COEUS_GLOBAL_IMAGE,CoeusProperties.getProperty(CoeusPropertyKeys.COEUS_GLOBAL_IMAGE));
                hashProps.put(CoeusPropertyKeys.POLLING_INTERVAL,CoeusProperties.getProperty(CoeusPropertyKeys.POLLING_INTERVAL));
                hashProps.put(CoeusPropertyKeys.DB_INSTANCE_NAME,CoeusProperties.getProperty(CoeusPropertyKeys.DB_INSTANCE_NAME));
                
                InputStream is = getClass().getResourceAsStream("/builddetails.properties");
                Properties coeusProps = new Properties();
                try {
                    coeusProps.load(is);
                }
                catch (IOException e) {
                    System.err.println("Can't read the build properties file. " +
                    "Make sure builddetails.properties is in the CLASSPATH or in the classes folder");
                    return;
                }
                
                hashProps.put("BuildDate", coeusProps.getProperty("build.date")) ;
                hashProps.put("BuildTimestamp", coeusProps.getProperty("build.timestamp")) ;
                
                responder.setDataObject(hashProps) ;
                success = true ;
                is.close() ;
            }else if (requester.getId().equalsIgnoreCase("LOGOUT")){
                //Process log out events if any
                //JIRA COEUSQA 2527 - START
                AuthenticationLogger authLog = new AuthenticationLogger();
                String id = requester.getSessionId();
                authLog.logout(id);
                //JIRA COEUSQA 2527 - END
                UtilFactory.log("User "+requester.getUserName()+" logged out");
                success=true;
            }
            //Authentication part moved to AuthenticationServlet
//            else{
//                //Authenticate user
//                CoeusAuthService authService = CoeusAuthServiceFactory.getCoeusAuthService(requester.getId());
//                authService.addResponseListener(new AuthResponseListener(){
//                    public void respond(Object dataObj){
//                        responder.setDataObject(dataObj);
//                    }
//                });
//                authService.addParam(HttpServletRequest.class.getName(),request);
//                authService.addParam(RequesterBean.class.getName(),requester);
//                success = authService.authenticate();
//            }
            
            // UCSD - rdias coeus personalization
            //Intercept the responderbean for including personalization. info
            try{
            Personalization.getInstance().setLocalizationXML(responder,requester);
            //UCSD - rdias coeus personalization      
            }catch (Exception exception) {
                //exception.printStackTrace();
                 UtilFactory.log(exception.getMessage(),exception,"LoginServlet","perform");
            }
        }catch (CoeusException cEx) {
            /* Log any other exceptions in error log. */
            UtilFactory.log(cEx.getMessage(),cEx,"LoginServlet","perform");
            success = false;
            CoeusMessageResourcesBean resBean = new CoeusMessageResourcesBean();
            String errMsg = resBean.parseMessageKey(cEx.getMessage());
            responder.setMessage(errMsg);
        }catch (Exception ex) {
            /* Log any other exceptions in error log. */
            UtilFactory.log(ex.getMessage(),ex,"LoginServlet","perform");
            success = false;
            responder.setMessage(ex.getMessage());
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "LoginServlet", "doPost");
        //Case 3193 - END
        } finally {
            try{
                if(!requester.getId().equalsIgnoreCase("LOGINMODE") && !requester.getId().equalsIgnoreCase("LOGOUT")){
                    if(success){
                        UtilFactory.log("User "+ authId + " authenticated and logged in");
                    }else{
                        UtilFactory.log("Authentication failed for user "+ authId);
                    }
                }
                responder.setResponseStatus(success);
                                
                
                // send the object to applet
                outputToApplet =
                new ObjectOutputStream(response.getOutputStream());
                outputToApplet.writeObject(responder);
                // close the streams
                if (inputFromApplet!=null){
                    inputFromApplet.close();
                }
                if (outputToApplet!=null){
                    outputToApplet.flush();
                    outputToApplet.close();
                }
            }catch (IOException ioe){
                UtilFactory.log( ioe.getMessage(), ioe,
                "LoginServlet", "perform");
            }
        }
    }
    
}
