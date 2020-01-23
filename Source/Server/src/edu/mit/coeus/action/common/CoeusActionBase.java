/*
 * @(#)CoeusActionBase.java	1.0	05/14/2002 10:21:18 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.action.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ForwardingActionForward;
import java.io.IOException;


/**
 * <code>CoesuActionBase</code> is a Struts implemented Action class
 * and acts as Base class to Coeus provides required constants that are used in
 * <code>coeus</code>  application in different action components.
 *
 * <br>Note: The similar constants available in <code> CoeusConstants</code> class and all constants will be moved to
 *  to CoeusConstants class in next release.
 *
 * @version 1.0 May 14,2002 10:21:18 AM
 * @author RaYaKu
 */

public abstract class CoeusActionBase extends Action{

    /**
     * The page scope attribute under which the page forward (welcome, main
     * Coeus page) is stored.
     */
     public static final String WELCOME = "welcome";

    /**
     * The page scope attribute under which the page forward (failure)
     *  information is stored.
     */
    public static final String FAILURE = "failure";

    /**
     * The session scope attribute under which the  Person
     *  information is stored.
     */
    public static final String PERSONINFO = "personInfo";

    /**
     * The page scope attribute under which the page forward (success)
     *  information is stored
     */
    public static final String SUCCESS = "success";

    /**
     * The page scope attribute under which the search page information is stored
    */
    public static final String SEARCH = "search";

    /**
     * The page scope attribute under which the search name informationi stored.
     */
    public static final String SEARCH_NAME = "searchname";

    /**
     * The session scope attribute under which the search file name is stored.
     */
    public static final String SEARCH_XML_FILE_NAME = "/CoeusSearch.xml";

    /**
     * The page scope attribute under which the logon page information is stored
     */
    public static final String LOGON = "logon";

    /**
     * The page scope attribute under which the exception page nformation is stored
     */
    public static final String EXCEPTION = "exception";

    /**
     * The page scope attribute under which the search logoff information is stored
     */
    public static final String LOGOFF = "logoff";

    /**
     * The page scope attribute under which the session expiration page
     *  information is stored
     */
    public static final String EXPIRE = "sessionexpired";

    /**
     * The session scope attribute under which the logged in user's user name is stored
     * for COI
     */
    public static final String USERNAME = "username";

    /**
     * The session scope attribute under which the logged in user's person id is stored
     */
    public static final String LOGGEDINPERSONID = "loggedinpersonid";

    /**
     * The session scope attribute under which the logged in user's person name is stored
     */
    public static final String LOGGEDINPERSONNAME = "loggedinpersonname";

    /* CASE #748 Begin */

    /**
     * The page scope attribute under which actionMapping for welcomeCOI page is stored.
     */
    public static final String WELCOME_COI = "welcomeCOI";
    /**
     * The session scope attribute under which the PersonInfoBean object is stored.
     */
    public static final String PERSON_DETAILS_REF = "personInfo";

    /**
     * The session scope attribute under which the user's COI privilege is stored.
     */
    public static final String PRIVILEGE = "userprivilege";

    /**
     * The session scope attribute under which userName is stored for Propdev webapp.
     */
    public static final String USER_ID = "userId";

    /**
     * The session scope attribute under which UserInfoBean object is stored.
     */
    public static final String USER_INFO_REF = "userInfoBean";
    
    public static final String CHANGE_PASSWORD = "changePassword";

    /* CASE #748 End */

    /* CASE #861 Begin */
     /**
      * Page scope attribute under which annual disclosure forward
      * (to be accessed in struts-config.xml) is stored.
      */
     public static final String TO_CONFIRMATION_PAGE = "annDisclConfirmation";

     /**
      * Page scope attribute under which annual disclosure forward
      * (to be accessed in struts-config.xml) is stored.
      */
     public static final String CHECK_UPDATE_FAILURE = "checkUpdateFailure";
    /* CASE #861 End */

     /* CASE #1046 Begin */
     public static final String VIEW_PENDING_DISC = "viewPendingDisc";
     /* CASE #1046 End */

    /**
     * Process the specified HTTP request, and create the corresponding HTTP response
     * (or forward to another web component that will create it). Return an
     * ActionForward instance describing where and how control should be forwarded,
     * or null if the response has already been completed.
     * An abstract method and all subclasses of this class should write implementation of this method.
     *
     * @param mapping The ActionMapping used to select this instance
     * @actionForm The optional ActionForm bean for this request (if any)
     * @request The HTTP request we are processing
     * @response The HTTP response we are creating
     *
     * @throws java.io.IOException if an input/output error occurs
     * @throws javax.servlet.ServletException if a servlet exception occurs.
     */
    public abstract ActionForward perform( ActionMapping mapping ,
    	ActionForm form , HttpServletRequest request ,
    	HttpServletResponse response ) throws IOException , ServletException;

} //end of CoeusActionBase