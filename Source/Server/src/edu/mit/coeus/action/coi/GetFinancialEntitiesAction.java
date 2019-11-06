/*
 * @(#)GetFinancialEntitiesAction.java 1.0	05/16/2002 16:59:43
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.action.coi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ForwardingActionForward;
import java.util.Vector;
import java.util.ArrayList;
import java.io.IOException;
import java.sql.SQLException;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.UtilFactory;
/* CASE #734 Comment Begin */
//import edu.mit.coeus.coi.exception.CoeusException;
/* CASE #734 Comment End */
/* CASE #734 Begin */
import edu.mit.coeus.exception.CoeusException;
/* CASE #734 End */
import edu.mit.coeus.coi.bean.FinancialEntitiesBean;
import edu.mit.coeus.action.common.CoeusActionBase;

/**
 * <code>GetFinancialEntitiesction</code> is a struts implemented Action class
 * to get all Financial Entities for a person based on logged in user privileges.
 * If user has privilege to view all financial entities then looks for person, if
 * person is not selected yet then it diverts the user to a page where he selectes
 * a person.
 *
 * @version 1.0 May 16,2002
 * @author RaYaKu
 */
public class GetFinancialEntitiesAction extends CoeusActionBase{

    private final String SELECTPERSON = "selectperson";

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an ActionForward instance describing where and how control
     * should be forwarded, or null if the response has already been completed.
     *
     * <br>The method fetches all the Financial Entities for a person by using
     * the <code>getFinancialEntities</code> method of <code>FinancialEntitiesBean</code> class.
     * <br><b>Validation</b>
     * <li> It will check whether the logged in user has any OSPRight or not,
     * and if he/she has the right, It will check whether the user has selected
     * any person or not, for doing operations on Financial Entity. If not, redirect
     * the request to a page where he can select a person.
     *
     * @param mapping The ActionMapping used to select this instance
     * @actionForm The optional ActionForm bean for this request (if any)
     * @request The HTTP request we are processing
     * @response The HTTP response we are creating
     *
     * @throws java.io.IOException if an input/output error occurs
     * @throws javax.servlet.ServletException if a servlet exception occurs.
     */

    public ActionForward perform( ActionMapping mapping ,
    ActionForm form , HttpServletRequest request ,
    HttpServletResponse response ) throws IOException , ServletException{
      System.out.println("begin GetFinancialEntitiesAction.perform");

        String personId = null;
        String personName = null;
        String userName = null;
        HttpSession session = request.getSession();
        ActionForward actionforward = mapping.findForward( SUCCESS );
//        UtilFactory UtilFactory = new UtilFactory();
        boolean errorFlag = false;
        PersonInfoBean personInfoBean = null;
        try {
            //look username attribute in session scope
            userName = ( String ) session.getAttribute( USERNAME );
            /*
             * If userName information is not available in session scope then
             * supply him a session expiration page
             */
            if( userName == null ) {
                actionforward = mapping.findForward( EXPIRE );
                return ( actionforward );
            }

            // look personInfo attribute in session scope
            personInfoBean = ( PersonInfoBean ) session.getAttribute( PERSONINFO );
            if( personInfoBean != null ) {
                personId = personInfoBean.getPersonID();
                personName = personInfoBean.getFullName();
            }
            //creating instance of UserDetailsBean for checking the privilege of a user.
            //UserDetailsBean userDetails = new UserDetailsBean();

            //int privilegeCode = userDetails.getCOIPrivilege( userName );

            // get all financial entities
            FinancialEntitiesBean financialEntitiesBean
                    = new FinancialEntitiesBean( personId.trim() );

            Vector allFinEntities = financialEntitiesBean.getFinancialEntities();
            
            //If user is coming from add/edit a discl, set attribute in session
            //so user can see link back to the disclosure
            String actionFrom = request.getParameter("actionFrom");
            System.out.println("@@@@@@actionFrom: "+actionFrom);
            if(actionFrom != null && ( actionFrom.equals("addDiscl") ||
                actionFrom.equals("editDiscl") || actionFrom.equals("annDiscCert") ) ){
                    if(actionFrom.equals("addDiscl") ){
                        session.setAttribute("backToDiscl", "addDiscl");
                    }
                    if(actionFrom.equals("editDiscl") ){
                        session.setAttribute("backToDiscl", "editDiscl");
                    }
                    if(actionFrom.equals("annDiscCert") ){
                        session.setAttribute("backToDiscl", "annDiscCert");
                    }                    
            }

            request.setAttribute( "allFinancialEntities" , allFinEntities );
            request.setAttribute( "personName" , personName );

        } catch( CoeusException coeusEx ) {
            errorFlag = true;
            UtilFactory.log( coeusEx.getMessage() , coeusEx , "GetFinancialEntitiesAction" ,
                    "perform()" );
            request.setAttribute( "EXCEPTION" , coeusEx );
        } catch( DBException dbEx ) {
            errorFlag = true;
            /* CASE #735 Comment Begin */
            //DBEngine prints the exception to log file.
            //UtilFactory.log( dbEx.getMessage() , dbEx , "GetFinancialEntitiesAction" ,
                    //"perform()" );
            /* CASE #735 Comment End */
            request.setAttribute( "EXCEPTION" , dbEx );
        }catch( Exception ex ) {
            errorFlag = true;
            UtilFactory.log( ex.getMessage() , ex , "GetFinancialEntitiesAction" ,
                    "perform()" );
            request.setAttribute( "EXCEPTION" , new CoeusException( "exceptionCode.80007" ) );
        }

        if( errorFlag ) {
            actionforward = mapping.findForward( FAILURE );
        }
        return actionforward;
    }
}