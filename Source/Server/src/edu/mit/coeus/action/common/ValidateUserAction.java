/*
 * @(#) ValidateUserAction.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeus.action.common;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.exception.CoeusException;
/* CASE #748 Comment Begin */
//import edu.mit.coeus.coi.action.CoeusActionBase;
/* cASE #748 Comment End */
/* CASE #748 Begin */
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.ValidateUserTxnBean;
import edu.mit.coeus.utils.CoeusConstants;
/* CASE #748 End */

import java.io.*;
import java.util.Properties;
import com.oreilly.servlet.Base64Decoder;
import edu.mit.coeus.user.auth.LDAPAuthentication;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;

/**
 * Extend Struts Action class, to validate Coeus user.
 * @author Coeus Dev Team
 * @version $Revision:   1.12  $ $Date:   Nov 13 2003 09:39:42  $
 */
public class ValidateUserAction extends CoeusActionBase
{
    public ValidateUserAction(){}
    /**
     * Check for existing validation of this user.  If not found, attempt to validate.
     * Check for userId in session object. If not found, attempt to validate this user.
     * If user is valid, create a UserInfoBean, and store the UserInfoBean, and
     * UserInfoBean.userId in the HttpSession object.
     * If userId exists in session object, or on successful validation, forward
     * to passed requestedURL parameter.  If no requestedURL is passed, forward
     * to welcome page.
     * @param mapping The ActionMapping object associated with this Action.
     * @param form The ActionForm object associated with this Action.  Value
     * is set in struts-config.xml.
     * @param req The HttpServletRequest we are processing.
     * @param res HttpServletResponse
     * @return ActionForward object. ActionServlet.processActionPerform() will
     * use this to forward to the specified JSP or servlet.
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward perform(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException{

        System.out.println("Inside ValidateUserAction.perform()");
        HttpSession session = request.getSession(true);
//        UtilFactory UtilFactory = new UtilFactory();
        UserInfoBean userInfoBean = null;
        String userName = null;
        boolean errorFlag = true;
        ActionForward actionforward = null;
        String validationType = null;
        String requestedURL = (String)request.getAttribute("requestedURL");
        String loginMode = "";
        String requestURI = request.getRequestURI();
        validationType = (String)request.getAttribute("validationType");
        if(validationType == null && requestURI != null){
            if(requestURI.indexOf(CoeusConstants.LOGIN_COI_KEY) != -1){
                validationType = CoeusConstants.LOGIN_COI_KEY;
            }
            else if(requestURI.indexOf(CoeusConstants.LOGIN_KEY) != -1){
                validationType = CoeusConstants.LOGIN_KEY;
            }
        }
        System.out.println("validationType: "+validationType);

        try {
            loginMode = getLoginMode(); // get login mode from coeus.properties file
            System.out.println("****** Login mode --> " + loginMode);
        }
        catch (Exception e) {
            UtilFactory.log("Can't read the properties file. " +
                    "Make sure Coeus.properties is in the CLASSPATH or in the classes folder");
        }

        try{
            if(loginMode.equalsIgnoreCase("CERTTOMCAT")) { // certificate - Tomcat Standalone.
                /* following code for Tomcat 4.0 */
                X509Certificate[] certs = (X509Certificate[])request.getAttribute(
                                           "javax.servlet.request.X509Certificate");
                userName = getUserName(certs,request);
                System.out.println("**************Validate user CertTomcat, username: "+userName);
                if(userName==null || userName.equals("")){
                    /*If certificate has no username, forward to error page.*/
                    throw new CoeusException("exceptionCode.error.cert.no.username");
                }
            }

            /* CASE #1170 Comment Begin   */
            /*else if(loginMode.equalsIgnoreCase("CERTAPACHE")) { // Certificate - Tomcat/Apache.
                if (request.getAttribute("SSL_CLIENT_S_DN_Email") != null
                && ! request.getAttribute("SSL_CLIENT_S_DN_Email").toString().equals("nodefault")) {

                // extract kerb ID from email address
                int at_pos = request.getAttribute("SSL_CLIENT_S_DN_Email").toString().indexOf("@");
                userName = request.getAttribute("SSL_CLIENT_S_DN_Email").toString().substring(0, at_pos);
                System.out.println("******** Validate user CertApache, username: "+userName);
                }
                else {
                  throw new CoeusException("exceptionCode.error.cert.no.username");
                }
            }
            /* CASE #1170 Comment End */
            /* CASE #1170 Begin */
            //Change environment variable from which to parse username.
            else if(loginMode.equalsIgnoreCase("CERTAPACHE")) { // Certificate - Tomcat/Apache.
                //Get value of env variable from which to parse the username.
                String emailString = "";
//                InputStream is = null;
//                try{
//                        is = getClass().getResourceAsStream("/coeus.properties");
//                }
//                catch(Exception ex){
//                    String errorMsg = "Can't read the properties file. ";
//                    errorMsg += "Make sure coeus.properties is in the CLASSPATH or in the classes folder.";
//                    System.out.println(errorMsg);
//                }
//                Properties coeusProps = new Properties();
                try {
//                        coeusProps.load(is);
                        emailString = CoeusProperties.getProperty(CoeusPropertyKeys.EMAIL_STRING);
                        //System.out.println("emailString from properties file: "+emailString);
                }
                catch (Exception e) {
                    String errorMsg = "Value for EMAIL_STRING not found in coeus.properties.";
                    System.out.println(errorMsg);
                    UtilFactory.log(errorMsg, e, "ValidateUserAction", "perform");
                }
                if (request.getAttribute(emailString) != null
                && ! request.getAttribute(emailString).toString().equals("nodefault")) {

                // extract kerb ID from email address
                int at_pos = request.getAttribute(emailString).toString().indexOf("@");
                userName = request.getAttribute(emailString).toString().substring(0, at_pos);
                System.out.println("******** Validate user CertApache, username: "+userName);
                }
                else {
                  throw new CoeusException("exceptionCode.error.cert.no.username");
                }
            }
            /* CASE #1170 End */
            else if(loginMode.equalsIgnoreCase("LDAPURL")) { // LDAP authentication - LDAP url.
                //userName = "jmc2011"; // for testing
                userName = getRemoteUser(request);
                System.out.println("****** Validate user LDAP, username: " + userName);
                if(userName==null || userName.equals("")){
                    /*If certificate has no username, forward to error page.*/
                    throw new CoeusException("exceptionCode.error.LDAP");
                }
            }
            else if(loginMode.equalsIgnoreCase("LDAP")) { // LDAP authentication.
                LoginForm loginForm = (LoginForm)form;
                LDAPAuthentication ldapAuth = new LDAPAuthentication();
                ldapAuth.authenticate(loginForm.getUserId(),loginForm.getPassword());
                userName = loginForm.getUserId();
            }

            /* CASE #748 Comment Begin */
          /*
           * Construct a UserInfoBean.  Call UserInfoBean.getUserInfo().
           * If no exception is thrown, put the UserInfoBean in the session.
           */
            /*userInfoBean = new UserInfoBean();
            userInfoBean.getUserInfo(userName);
            /*If we've gotten this far, then this is a valid user, put the UserInfoBean
            and userId in the Session */
            /*session.setAttribute("userInfoBean", userInfoBean);
            session.setAttribute("userId", userName);
            errorFlag = false;*/
            /* CASE #748 Comment End */
            /* CASE #748 Begin */
            UserDetailsBean userDetails = new UserDetailsBean();
            if(validationType.equalsIgnoreCase(CoeusConstants.LOGIN_KEY)){
                if(isValidCoeusUser(userName)){
                    userInfoBean = userDetails.getUserInfo(userName);
                    session.setAttribute(USER_INFO_REF, userInfoBean);
                    session.setAttribute(USER_ID, userName);
                    System.out.println("session.setAttribute("+USER_ID+", "+userName+")");
                    actionforward = mapping.findForward(WELCOME);
                    errorFlag = false;
                }
                else{
                    throw new CoeusException("exceptionCode.error.invalid.user");
                }
            }
            else if(validationType.equalsIgnoreCase(CoeusConstants.LOGIN_COI_KEY)){
                //System.out.println("instantiate PersonInfoBean");
                PersonInfoBean personInfo = userDetails.getPersonInfo(userName);
                if(personInfo.getPersonID() != null ){
                    //setting personal details with the session object
                    session.setAttribute(PERSON_DETAILS_REF,personInfo);
                    //setting privilege of a logged in user with the session
                    session.setAttribute(PRIVILEGE,""+userDetails.getCOIPrivilege(userName));
                    //setting logged in user name with the session
                    session.setAttribute( USERNAME , userName);
                    //System.out.println("set "+USERNAME+ " in session: "+userName);
                    //setting logged in user's person id with the session
                    session.setAttribute(LOGGEDINPERSONID, personInfo.getPersonID());
                    //setting logged in user's person name with the session
                    String personName = personInfo.getFullName();
                    session.setAttribute(LOGGEDINPERSONNAME, personName);
                    /* CASE #1046 Begin */
                    //Check whether to show link for View Pending Disclosures
                    if(userDetails.canViewPendingDisc(userName)){
                        session.setAttribute(VIEW_PENDING_DISC, VIEW_PENDING_DISC);
                    }
                    /* CASE #1046 End */
                    actionforward = mapping.findForward(WELCOME_COI);
                    errorFlag = false;
                }
                else{
                    throw new CoeusException("exceptionCode.error.invalid.personid");
                }
            }
            else{
                throw new CoeusException("exceptionCode.validationType.unavailable");
            }
            /* CASE #748 End */
        }
        catch(DBException dbEx){
            request.setAttribute("EXCEPTION", dbEx);
        }
        catch(CoeusException CEx){
            UtilFactory.log(CEx.getMessage(), CEx, "ValidateUserAction", "perform()");
            request.setAttribute("EXCEPTION", CEx);
        }
        catch(Exception e){
            UtilFactory.log(e.getMessage(), e, "ValidateUserAction", "perform");
            request.setAttribute("EXCEPTION", e);
        }
        if(errorFlag){
            if(validationType != null &&
                    validationType.equalsIgnoreCase(CoeusConstants.LOGIN_KEY)){
               //So that propdev nav bar will be displayed on Error Page.
               request.setAttribute("usePropDevErrorPage", "true");
            }
            actionforward = mapping.findForward(FAILURE);
        }
        else if(requestedURL != null){
            System.out.println("requestedURL: "+requestedURL);
            if(requestedURL.equalsIgnoreCase("ChangePassword.jsp")){
                actionforward = mapping.findForward(CHANGE_PASSWORD);
                return actionforward;
            }
            
            RequestDispatcher rd = request.getRequestDispatcher(requestedURL);
            //System.out.println("ValidateUserAction before rd.forward");
            rd.forward(request, response);
            //System.out.println("ValidateUserAction after rd.forward");
            return null;
        }
        //If no requestedURL exists in request object and user has been validated,
        //then forward to appropriate welcome page.
        return actionforward;
    }


    /**
     * Return login mode. Read login mode from coeus.properties file.
    */
    private String getLoginMode() throws CoeusException {
//        InputStream is = null;
//        is = getClass().getResourceAsStream("/coeus.properties");
//        Properties coeusProps = new Properties();
//        try {
//             coeusProps.load(is);
//        }
//        catch (Exception e) {
//            UtilFactory.log("Can't read the properties file. " +
//                    "Make sure Coeus.properties is in the CLASSPATH or in the classes folder");
//            throw new CoeusException("Can't read the properties file. " +
//                    "Make sure Coeus.properties is in the CLASSPATH or in the classes folder");
//        }
        String loginMode;
        try{
            loginMode = CoeusProperties.getProperty(CoeusPropertyKeys.LOGIN_MODE);
        }catch(IOException e) {
            UtilFactory.log("Can't read the properties file. " +
                    "Make sure Coeus.properties is in the CLASSPATH or in the classes folder");
            throw new CoeusException("Can't read the properties file. " +
                    "Make sure Coeus.properties is in the CLASSPATH or in the classes folder");
        }
        return loginMode;
    }

    /**
     * Return remote user id for LDAP.
    */
    public static String getRemoteUser(HttpServletRequest request) {
        String cwid = null;
        String auth = request.getHeader("authorization"); // as send by apache
        if (auth != null) {
            try {
                auth = Base64Decoder.decode(auth.substring(6));
            }
            catch (Exception exc) {
                exc.printStackTrace();
                return null;
            }
        }
        int p = 0;
        if (auth != null)
            p = auth.indexOf(":");
        if (p > 0)
            cwid = auth.substring(0, auth.indexOf(":"));
        return cwid;
    }

    /**
     *  Extract user id from the certificate.
     *  Fetch all the information from all the certificates of the type 'X509Certificate'
     *  Take subject name from the certificate. This may vary with certificate to certificate
     *  Since mit certificate user id comes with email, check the subject name
     *  has the email format. ie, check for 'emailaddress' string and '@' format.
     *  If yes, parse the String and take out the user id from it.
     */
    private String getUserName(X509Certificate[] certs,HttpServletRequest req) throws CoeusException{
        String userName = null;
        if (certs != null) {
            for (int i = 0; i < certs.length; i++) {
                X509Certificate certificate = certs[i];
                String sigAlgName = certificate.getSigAlgName();
                String sigAlgOID = certificate.getSigAlgOID();
                String subName = certificate.getSubjectDN().getName();
                byte[] sigAlgParams = certificate.getSigAlgParams();
                String emailKey = subName.substring(0,12);
                if(emailKey.equalsIgnoreCase("emailaddress")
                      && subName.indexOf('@')!=-1){
                    StringTokenizer stkr = new StringTokenizer(subName,",");
                    while(stkr.hasMoreElements()){
                        String token = stkr.nextToken();
                        int eqIndex = token.indexOf('=');
                        int atIndex = token.indexOf('@');
                        if(eqIndex!=-1 && atIndex!=-1){
                            userName = token.substring(eqIndex+1,atIndex);
                        }
                    }
                }
            }
            UtilFactory.log("User name from the Client Certificate = "+userName);
        }
        else {
            if ("https".equals(req.getScheme())) {
                UtilFactory.log("This was an HTTPS request, " +
                        "but no client certificate is available");
                throw new CoeusException("This was an HTTPS request, " +
                        "but no client certificate is available");
            }
            else {
                UtilFactory.log("This was not an HTTPS request, " +
                        "so no client certificate is available");
                throw new CoeusException("This was not an HTTPS request, " +
                        "so no client certificate is available");
            }
        }
        return userName;
    }

    /* CASE #748 Begin */
    private boolean isValidCoeusUser(String userName)
                                  throws CoeusException, DBException{
        ValidateUserTxnBean validateUserTxn = new ValidateUserTxnBean();
        return validateUserTxn.isThisUserValidUser(userName);
    }
    /* CASE #748 End */
}
