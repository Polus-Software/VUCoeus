/*
 * @(#)AuthorizationServlet.java 1.0 6/14/04 8:11 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.servlet;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.bean.AuthorizationTxnBean;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

import java.net.URL;
import java.net.URLConnection;


/**
 * This class is used to handle all Authorization related request. 
 * This class will first check for Authorization as specified in Requester Bean.
 * After performing Authorization this will connect to other servlets if specified.
 *
 * @author Prasanna Kumar K.
 * @version :1.0 June 14, 2004 8:11 PM
 *
 */

public class AuthorizationServlet extends CoeusBaseServlet implements TypeConstants{

//    private UtilFactory UtilFactory = new UtilFactory();
    private final String CONTENT_KEY = "Content-Type";
    private final String CONTENT_VALUE =    "application/octet-stream";    
    
    /**This method is used handle all Requests from Client
     * 
     * @param request Request
     * @param response Response
     * @throws ServletException if any Servlet Exception
     * @throws IOException if any IO Exception
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        // the request object from applet
        RequesterBean requesterBean = null;
        // the response object to applet
        ResponderBean responderBean = new ResponderBean();
        
        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        
        String loggedinUser ="";
        String unitNumber = "";
        
        try {
            // get an input stream
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            // read the serialized request object from applet
            requesterBean = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requesterBean);
            // get the user
            UserInfoBean userBean = (UserInfoBean)new
            UserDetailsBean().getUserInfo(requesterBean.getUserName());            
            
            //Should always be the User Id with which user logged in - March 20, 2004
            //loggedinUser = userBean.getUserId();
            loggedinUser = requesterBean.getUserName();
            unitNumber = userBean.getUnitNumber();
            
            // keep all the beans into vector
            Vector dataObjects = new Vector();
            
            char functionType = requesterBean.getFunctionType();
            CoeusVector servletNames = requesterBean.getServletNames();
            Hashtable authorizationOperators = requesterBean.getAuthorizationOperators();
            Enumeration enuAuth = authorizationOperators.keys();
            AuthorizationOperator authorizationOperator = null;
            BaseBean baseBean = null;
            boolean blnAuth = false;
            String authorizationKey = "";
            //while(enuAuth.hasMoreElements()){
            AuthorizationTxnBean authorizationTxnBean = new AuthorizationTxnBean();
            for(enuAuth = authorizationOperators.keys();enuAuth.hasMoreElements();){
                authorizationKey = (String)enuAuth.nextElement();
                //No need to send BaseBean here. It will be bound inside Operator.
                //Send null
                authorizationOperator = (AuthorizationOperator)authorizationOperators.get(authorizationKey);
                //authorizationTxnBean.getResult(authorizationOperator);
                blnAuth = authorizationTxnBean.getResult(authorizationOperator);
                authorizationOperators.put(authorizationKey, new Boolean(blnAuth));
            }
            
            responderBean.setMessage(null);
            responderBean.setResponseStatus(true);
            
            //Connect to other Servlets if specified
            URL urlSrvServlet = null;
            if(requesterBean.getServletNames()!=null && requesterBean.getServletNames().size() > 0){
                urlSrvServlet = new URL((String)requesterBean.getServletNames().elementAt(0));
                
                requesterBean.setAuthorizationOperators(authorizationOperators);
                
                URLConnection servletConnection = urlSrvServlet.openConnection();
                // prepare for both input and output
                servletConnection.setDoInput(true);
                servletConnection.setDoOutput(true);
                // turn off caching
                servletConnection.setUseCaches(false);
                // Specify the content type that we will send binary data
                servletConnection.setRequestProperty(CONTENT_KEY, CONTENT_VALUE);
                // send the requester object to the servlet using serialization
                ObjectOutputStream outputToServlet = new ObjectOutputStream(servletConnection.getOutputStream());
                // serialize the object and send to servlet
                outputToServlet.writeObject(requesterBean);
                outputToServlet.flush();
                outputToServlet.close();

                // read the object
                InputStream inputToApplet = servletConnection.getInputStream();
                responderBean = (ResponderBean) (new ObjectInputStream(inputToApplet)).readObject();
            }
            //Set the Authorization results
            responderBean.setAuthorizationOperators(authorizationOperators);
        }catch( CoeusException coeusEx ) {
            //coeusEx.printStackTrace();
            int index=0;
            String errMsg;
            if(coeusEx.getErrorId()==999999){
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
                responderBean.setLocked(true);
            }else{
                errMsg = coeusEx.getMessage();
            }
            CoeusMessageResourcesBean coeusMessageResourcesBean
            =new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            responderBean.setException(coeusEx);
            responderBean.setResponseStatus(false);
            
            responderBean.setMessage(errMsg);
            UtilFactory.log( errMsg, coeusEx, "AuthorizationServlet", "perform");
            
        }catch( DBException dbEx ) {
            //dbEx.printStackTrace();
            int index=0;
            String errMsg = dbEx.getUserMessage();
            if (dbEx.getErrorId() == 20102 ) {
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
            }
            if (errMsg.equals("db_exceptionCode.1111")) {
                responderBean.setCloseRequired(true);
            }
            CoeusMessageResourcesBean coeusMessageResourcesBean
            = new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            responderBean.setException(dbEx);
            responderBean.setResponseStatus(false);
            
            responderBean.setMessage(errMsg);
            UtilFactory.log( errMsg, dbEx, "AuthorizationServlet", "perform");
            
        }catch(Exception e) {
            //e.printStackTrace();
            responderBean.setResponseStatus(false);
            responderBean.setException(e);
            responderBean.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e,
            "AuthorizationServlet", "perform");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responderBean.setException(ex);
            responderBean.setResponseStatus(false);
            responderBean.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "AuthorizationServlet", "doPost");
        //Case 3193 - END
        } finally {
            try{
                
                outputToApplet
                = new ObjectOutputStream(response.getOutputStream());
                outputToApplet.writeObject(responderBean);
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
                "AuthorizationServlet", "perform");
            }
        }
    }  
}