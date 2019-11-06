/*
 * @(#)ViewFinEntityAction.java	1.0 05/31/2002
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
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
import java.io.IOException;
import java.sql.SQLException;

import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.UtilFactory;
/* CASE #734 Comment Begin */
//import edu.mit.coeus.coi.exception.CoeusException;
/* CASE #734 Comment End */
/* CASE #734 Begin */
import edu.mit.coeus.exception.CoeusException;
/* CASE #734 End */
import edu.mit.coeus.coi.bean.FinancialEntityDetailsBean;
import edu.mit.coeus.coi.bean.EntityDetailsBean;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.action.common.CoeusActionBase;

/**
 * <code>ViewFinEntityAction</code> is a struts implemented Action class
 * to get the details(History,current and Certificates) of a Financial Entity.
 * <br>This component uses <code>FinancialEntityDetailsBean</code> model to
 * extract the details from <code>coeus</code> database.
 * <br>This component is used by 2 different view components and which present
 * almost same information to user in different windows that is identified by
 * a request parameter("actionFrom") in querystring.
 *
 * @version 1.0 May 31,2002
 * @author RaYaKu
 */
public class ViewFinEntityAction extends CoeusActionBase{

    /**
     * Process the specified HTTP request, and create the corresponding HTTP response
     * (or forward to another web component that will create it). Return an ActionForward instance
     * describing where and how control should be forwarded, or null if the response
     * has already been completed.
	 * <br> Method forwards to a different view components depend on the quereystring
	 * parameter("actionFrom").
     *
     * @param actionMapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @throws java.io.IOException if an input/output error occurs
     * @throws javax.servlet.ServletException if a servlet exception occurs.
     */
    public ActionForward perform( ActionMapping actionMapping ,
            ActionForm actionForm , HttpServletRequest request ,
                HttpServletResponse response ) throws IOException , ServletException{

        System.out.println("begin ViewFinEntityAction");
        String personId = null;
        String personName = null;
        String userName = null;
        String actionFrom = null;
        PersonInfoBean personInfoBean = null;
        HttpSession session = null;
        ActionForward actionforward = actionMapping.findForward( SUCCESS );
//        UtilFactory UtilFactory = new UtilFactory();
        boolean errorFlag = false;
        try {
            session = request.getSession( true );
            // look username attribute in session scope
            userName = ( String ) session.getAttribute( USERNAME );

            /*
             * If userName information is not available in session scope then
             * supply him a session expiration page
             */
            if( userName == null ) {
                actionforward = actionMapping.findForward( EXPIRE );
                return ( actionforward );
            }

            // look personInfo attribute in session scope
            personInfoBean = ( PersonInfoBean ) session.getAttribute(PERSONINFO );
            if( personInfoBean != null ) {
                personId = personInfoBean.getPersonID();
                personName = personInfoBean.getFullName();
            }

            //get the Parameters from request to process the Entity
            String entityNumber = request.getParameter( "entityNo" );
            String seqNumber = request.getParameter( "seqNo" );
            String entSeqNumber = request.getParameter( "entSeqNo" );
            //System.out.println("entSeqNumber: "+entSeqNumber);
            actionFrom = request.getParameter( "actionFrom" ); // the request coming from
            
            

            FinancialEntityDetailsBean financialEntityDetailsBean
                    = new FinancialEntityDetailsBean( "Coeus" , "Coeus" ,
                    personId.trim() );

            EntityDetailsBean entityDetails = null;

            if( ( actionFrom != null ) && ( actionFrom.equals( "entityDetails" ) ) ) {
              entityDetails = financialEntityDetailsBean.
                getFinancialEntityHistoryDetails
                (entityNumber.trim(), entSeqNumber.trim());
            } else {
                entityDetails
                        = financialEntityDetailsBean.getFinancialEntityDetails(
                                entityNumber.trim() );
                /* CASE #1221 Begin */
                entSeqNumber = entityDetails.getSeqNumber();
                System.out.println("view fin ent, seq number is: "+entSeqNumber);
                /* CASE #1221 End */
            }

            /* CASE #352 Begin */
            boolean hasRightToView = false;
            String loggedinpersonid =
                    (String)session.getAttribute(LOGGEDINPERSONID);              
            String strUserprivilege = (String)session.getAttribute("userprivilege");
            int userprivilege = Integer.parseInt(strUserprivilege);
            if(userprivilege > 0){
                hasRightToView = true;
            }
            else if(entityDetails.getPersonId().equals(loggedinpersonid)){
                hasRightToView = true;
            }
            if(!hasRightToView){
                String errorMsg = "You do not have the right to view this financial ";
                errorMsg += "entity.  If you believe you are seeing this message ";
                errorMsg += "in error, please contact the Office of Sponsored ";
                errorMsg += "Programs";
                throw new CoeusException(errorMsg);

            }
            /* CASE #352 End */

            /* CASE #1221 Comment Begin */
            //get all Financial Entity Certificate details
            /*Vector colFinCertDetails
                    = financialEntityDetailsBean.getFECertificateDetails(
                            entityNumber.trim() , seqNumber.trim() );*/
            /* CASE #1221 Comment End */
            /* CASE #1221 Begin */
            Vector colFinCertDetails = null;                
            colFinCertDetails
                    = financialEntityDetailsBean.getFECertificateDetails(
                            entityNumber.trim() , entSeqNumber.trim() );
            /* CASE #1221 End */
            //bind the Financial Entity to request scope
            request.setAttribute( "personName" , personName );
            request.setAttribute( "entityNo" , entityNumber );
            request.setAttribute( "seqNo" , seqNumber );
            request.setAttribute( "entSeqNo", entSeqNumber );
            request.setAttribute( "entityDetails" , entityDetails );
            request.setAttribute( "colFinCertificationDetails" , colFinCertDetails );
            request.setAttribute( "actionFrom" , actionFrom );
            /* CASE #1400 Begin */
            session.setAttribute("originalEntityDetails", entityDetails);
            session.setAttribute("originalEntityCertDetails", colFinCertDetails );
            /* CASE #1400 End */

        } catch( CoeusException coeusEx ) {
            errorFlag = true;
            UtilFactory.log( coeusEx.getMessage() , coeusEx , "ViewFinEntityAction" ,
                    "perform()" );
            request.setAttribute( "EXCEPTION" , coeusEx );
        } catch( DBException dbEx ) {
            errorFlag = true;
            /* CASE #735 Comment Begin */
            //DBEngine prints the exception to log file.
            //UtilFactory.log( dbEx.getMessage() , dbEx , "ViewFinEntityAction" ,
                  //  "perform()" );
            /* CASE #735 Comment End */
            request.setAttribute( "EXCEPTION" , dbEx );
        }/* catch( SQLException sqlEx ) {
            errorFlag = true;
            UtilFactory.log( sqlEx.getMessage() , sqlEx , "ViewFinEntityAction" ,
                    "perform()" );
            request.setAttribute( "EXCEPTION" , new CoeusException( "exceptionCode.80008" ) );
        } */catch( Exception ex ) {
            errorFlag = true;
            UtilFactory.log( ex.getMessage() , ex , "ViewEntityAction" , "perform()" );
            request.setAttribute( "EXCEPTION" , new CoeusException( "exceptionCode.80009" ) );
        }
        //forward the output to failure/success page depends on errorFlag state
        if( errorFlag ) {
            actionforward = actionMapping.findForward( FAILURE );
        } else {
            /*
             * if request came with actionFrom is  entityDetail then go to same page from which
             * the request has come
             */
            if( ( actionFrom != null ) && ( actionFrom.equals( "entityDetails" ) ) ) {
                actionforward = actionMapping.findForward( "entityDetails" );
            } else {
                actionforward = actionMapping.findForward( SUCCESS );
            }
        }
        return actionforward;
    }
}