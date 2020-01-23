package edu.mit.coeus.taglib;

import java.io.*;
import java.util.*;
import java.sql.*;

import javax.servlet.http.*;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import org.apache.struts.taglib.logic.ForwardTag;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.action.common.CoeusActionBase;

/**
 * Check for existing userInfoBean, stored in Session object, for this user.
 * @author Coeus Dev Team
 * @version 1.0
 */
public class CheckLoginTag extends ForwardTag
{
    /* CASE #748 Comment Begin */
    //private String loginForward = "login";
    /* CASE #748 Comment End */
    /* CASE #748 Begin */
    /**
     * Class to which control will be forwarded in order to validate the user.
     * To be read from struts-config.xml
     */
    private String forwardName = null;

    public void setForwardName(String forwardName){
        this.forwardName = forwardName;
    }

    /* CASE #748 End */
    /**
     * URL of calling JSP, that is, the page that user is attempting to access.
     */
    private String requestedURL = null;

    /**
     * UtilFactory object, used here for logging.
     */
//    private UtilFactory UtilFactory;

    public void setRequestedURL(String requestedURL){
        this.requestedURL = requestedURL;
    }


 	/**
     * Check for existing userInfoBean stored in Session object for this user.
     * If we find one, continue evaluating the page.  If we don't find one,
     * @throws JspException
     */
    public int doEndTag() throws JspException{
        System.out.println("begin CheckLoginTag.doEndTag()");
//        UtilFactory = new UtilFactory();
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        HttpSession session = request.getSession(true);
        boolean validUser = false;
        /* CASE #748 Comment Begin */
        /*if(forwardName.equalsIgnoreCase(CoeusConstants.LOGIN_KEY)){
            UserInfoBean userInfo = (UserInfoBean)session.getAttribute
                                                (CoeusActionBase.USER_INFO_REF);
            if(userInfo != null){
                validUser = true;
            }
            System.out.println("set validUser to true");
        }
        else if(forwardName.equalsIgnoreCase(CoeusConstants.LOGIN_COI_KEY)){
            PersonInfoBean personInfo= (PersonInfoBean)session.getAttribute
                                                (CoeusActionBase.PERSON_DETAILS_REF);
            if(personInfo != null){
                validUser = true;
            }
        }*/
        /* CASE #748 Comment End */
        /* CASE #748 Begin */
        //Look for userId and userName in session to check if already validated,
        //instead of UserInfoBean and PersonInfoBean.  For consistency with action classes.
        if(forwardName.equalsIgnoreCase(CoeusConstants.LOGIN_KEY)){
            String userId = (String)session.getAttribute(CoeusActionBase.USER_ID);
            if(userId != null){
                validUser = true;
            }
        }
        else if(forwardName.equalsIgnoreCase(CoeusConstants.LOGIN_COI_KEY)){
            String userName = (String) session.getAttribute(CoeusActionBase.USERNAME);
            if(userName != null){
                validUser = true;
            }
        }
        /* CASE #748 End */

        /* If userInfoBean exists, then the user has already been validated, so
         display the rest of the JSP. */
        if(validUser){
            return EVAL_PAGE;
        }
        /* If no valid user exists in session, forward control to validation class.*/
        else {
            /*
            * Supply forwardName, to be read from application scope
            * (i.e forward from global-forwards in struts-config.xml) and
            * forward control to that page.
            * Set requestedURL as a request attribute.
            * Set forwardName in request as validationType.
            */
            if(requestedURL != null){
                request.setAttribute("requestedURL", requestedURL);
                System.out.println("set requestedURL in request object: "+requestedURL);
            }
            request.setAttribute("validationType", forwardName);
            System.out.println("forwardName: "+forwardName);
            super.setName(forwardName);
            super.doEndTag();
        }
        return (SKIP_PAGE);
    }
}
