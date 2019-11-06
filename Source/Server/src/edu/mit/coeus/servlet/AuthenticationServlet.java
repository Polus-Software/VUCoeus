/*
 * AuthenticationServlet.java
 *
 * Created on August 31, 2006, 10:27 AM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.servlet;

import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.bean.LoginBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.user.auth.AuthResponseListener;
import edu.mit.coeus.user.auth.CoeusAuthService;
import edu.mit.coeus.user.auth.CoeusAuthServiceFactory;
import edu.mit.coeus.utils.AuthenticationLogger;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.security.*;
import java.io.*;
import java.net.*;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author  Geo thomas
 * @version 1.0
 */
public class AuthenticationServlet extends HttpServlet {
    
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
    
    /** Destroys the servlet.
     */
    public void destroy() {
    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
//        String userId = null;
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
        
            //Authenticate user
            CoeusAuthService authService = CoeusAuthServiceFactory.getCoeusAuthService(requester.getId());
            authService.addResponseListener(new AuthResponseListener(){
                public void respond(Object dataObj){
                    responder.setDataObject(dataObj);
                }
            });
            authService.addParam(HttpServletRequest.class.getName(),request);
            authService.addParam(RequesterBean.class.getName(),requester);
            success = authService.authenticate();
            if(success){
                //JIRA COEUSQA 2527 - START
                AuthenticationLogger authLog = new AuthenticationLogger();
                Object reqObject = requester.getDataObject();
                String user = "Unknown";
                if(reqObject instanceof LoginBean){
                    LoginBean loginBean = (LoginBean)requester.getDataObject();
                    user = loginBean.getUserId();
                }else if(reqObject instanceof String){
                    user = (String)requester.getDataObject();
                }
                String sessionId = request.getSession().getId();
                String  ipAddress = request.getRemoteAddr();
                authLog.logon(user, ipAddress, sessionId);
                responder.setSessionId(sessionId);
                //JIRA COEUSQA 2527 - END
                String seed = (String)getServletContext().getAttribute(CoeusCipher.SERVLET_SECURE_SEED);
                
                if(seed==null){
                    seed = new SecureSeedTxnBean().getServerSecureSeedValue(CoeusCipher.SERVLET_SECURE_SEED);
                }
                Hashtable map = (Hashtable)responder.getDataObject();
                String userId = (String)map.get("USER_ID");
                responder.setAuthKey(CoeusCipher.getEncriptedKey(userId, seed));
                UtilFactory.log("set authkey to responder");
            }
        }catch (CoeusException cEx) {
            /* Log any other exceptions in error log. */
            UtilFactory.log(cEx.getMessage(),cEx,"AuthenticationServlet","processRequest");
            success = false;
            CoeusMessageResourcesBean resBean = new CoeusMessageResourcesBean();
            String errMsg = resBean.parseMessageKey(cEx.getMessage());
            responder.setMessage(errMsg);
        }catch (Exception ex) {
            /* Log any other exceptions in error log. */
            UtilFactory.log(ex.getMessage(),ex,"AuthenticationServlet","processRequest");
            success = false;
            responder.setMessage(ex.getMessage());
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "AuthenticationServlet", "doPost");
        //Case 3193 - END
        } finally {
            try{
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
                "AuthenticationServlet", "processRequest");
            }
        }
    }
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    
}
