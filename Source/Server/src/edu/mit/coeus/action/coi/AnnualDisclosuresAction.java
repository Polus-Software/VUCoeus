/*
 * @(#)AnnualDisclosuresAction.java 1.0 6/9/02
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
/* CASE #1046 Begin */
import java.util.LinkedList;
/* CASE #1046 End */

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ForwardingActionForward;

import edu.mit.coeus.coi.bean.SearchBean;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.UtilFactory;
/* CASE #734 Comment Begin */
//import edu.mit.coeus.coi.exception.CoeusException;
/* CASE #734 Comment End */
/* CASE #734 Begin */
import edu.mit.coeus.exception.CoeusException;
/* CASE #734 End */
import edu.mit.coeus.coi.bean.AnnualDiscFinEntitiesBean;
import edu.mit.coeus.coi.bean.AnnualDisclosuresBean;
import edu.mit.coeus.coi.bean.EntityDetailsBean;
import edu.mit.coeus.coi.bean.FinancialEntityDetailsBean;
import edu.mit.coeus.coi.bean.AnnDisclosureErrorBean;
import edu.mit.coeus.coi.bean.AnnDisclFinalUpdateBean;
import edu.mit.coeus.action.common.CoeusActionBase;
/* CASE #1046 Begin */
import edu.mit.coeus.coi.bean.DisclosureDetailsBean;
/* CASE #1046 End */

import java.util.HashMap;
import java.util.Vector;
import java.io.IOException;
import java.sql.SQLException;

/**
 * <code>AnnualDisclosuresAction</code> is a struts implemented Action class
 * to get all the pending AnnualDisclosures for the review and shows the user
 * selected AnnualDisclosure information that is available in querystring
 * parameter ( entityIndex ) in request object.
 *
 * @version 1.0 June 9,2002
 * @author Phaneendra Kumar.
 */

public class AnnualDisclosuresAction extends CoeusActionBase{

    /**
     * Process the specified HTTP request, and create the corresponding HTTP response
     * (or forward to another web component that will create it). Return an ActionForward instance
     * describing where and how control should be forwarded, or null if the response
     * has already been completed.
     *
     * <br> The method used to fetch all the annual financial entity disclosures
     * for a person by using <code>getAnnualDiscEntities</code> of
     * <code>AnnualDiscFinEntitiesBean</code> and fetches all details of pending Financial Entity
	 * Disclosures by using <code>getPenidngDisclosures</code> of <code>AnnualDisclosuresBean</code> class.
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
                HttpServletResponse response )
                    throws IOException ,ServletException{
        System.out.println("Inside AnnualDisclosuresAction");
        String personId = null;
        String userName = null;
        String entityNumber = null;
        boolean errorFlag = true;
        int finEntityIndex;
        EntityDetailsBean entityDetails = null;
        String action = null;

        HttpSession session = request.getSession();
        ActionForward actionforward = actionMapping.findForward( SUCCESS );
//        UtilFactory UtilFactory = new UtilFactory();

        try {
            /* look userName attribute in session scope */
            userName = ( String ) session.getAttribute( USERNAME );
            /* If user session is  expired then forward the
             * user to session exipiration page
             */
            if( userName == null ) {
                actionforward = actionMapping.findForward( EXPIRE );
                return ( actionforward );
            }

            /* look personId in session scope */
            personId = ( String ) session.getAttribute( LOGGEDINPERSONID );

            /*  get the instance of AnnualDisclFinEntitiesBean to get all the
             *  pending Financial Entities.
             *  set the the collection of pending Financial Entities in the request scope.
             */
            AnnualDiscFinEntitiesBean annualDisclFinEntities =
                    new AnnualDiscFinEntitiesBean( personId.trim() );
            Vector finEntities = annualDisclFinEntities.getAnnualDiscEntities();
            entityNumber = request.getParameter("entityNumber");
            //System.out.println("entityNumber: "+entityNumber);
            //If user has no active fin entities, forward to confirmation page.
            if(finEntities.size() == 0){
                //System.out.println("User has no active financial entities");
                AnnDisclFinalUpdateBean finalUpdateBean =
                    new AnnDisclFinalUpdateBean(personId);
                Vector checkErrors = finalUpdateBean.checkAnnualDisclComplete();
                AnnDisclosureErrorBean errorBean = null;
                if( ( checkErrors != null ) && (checkErrors.size() > 0 ) ) {
                    if ( checkErrors.get(0) instanceof AnnDisclosureErrorBean ) {
                        errorBean = (AnnDisclosureErrorBean)checkErrors.get(0);
                    }
                    //User has disclosures with status 104, and conflict
                    //status for all fin entities for those discl's is 100 or 301.
                    if ( errorBean.getEntityNumber().toString().equals("1") ) {
                        actionforward = actionMapping.findForward( TO_CONFIRMATION_PAGE );
                    }
                    // User has no disclosures with status 104.
                    else {
                        //user has no disclosures with status 104, and no fin entites.
                        throw new CoeusException("exceptionCode.80012");
                    }
                    return actionforward;
                }
            }
            /* Check for presence of "errors" attribute set in request by
            * AnnDisclFinalUpdateAction.  If present, get the entity number for
            the first AnnDisclosureErrorBean in the collection. */
            if(request.getAttribute("errors") != null){
              Vector checkErrors = (Vector)request.getAttribute("errors");
              AnnDisclosureErrorBean errorBean =
                  (AnnDisclosureErrorBean)checkErrors.get(0);
              entityNumber = errorBean.getEntityNumber();
              //System.out.println("errorBean.getEntityNumber(): "+errorBean.getEntityNumber());
            }
            if(entityNumber == null && request.getAttribute("entityNumber") != null){
                entityNumber = (String)request.getAttribute("entityNumber");
                //System.out.println("got entityNumber from requestAttribute: "+entityNumber);
            }
            //If no entity number in request attribute or request parameter and
            //user has active financial entities,
            //get next entity that has not been updated yet, and display that page.
            if(entityNumber == null ){
                if(finEntities.size() > 0){
                  EntityDetailsBean tempEntityDetails = null;
                  for(int entCount = 0; entCount < finEntities.size(); entCount++){
                      tempEntityDetails = (EntityDetailsBean)finEntities.get(entCount);
                      if(tempEntityDetails.getAnnDisclUpdated() != null &&
                          tempEntityDetails.getAnnDisclUpdated().equals("N")){
                          break;
                      }
                  }
                  entityNumber = tempEntityDetails.getNumber();
                }
            }

            /* Get the instance of FinancialEntityDetailsBean to retrieve the
             * Financial Entity Details
             */
            FinancialEntityDetailsBean finEntityDetails =
                new FinancialEntityDetailsBean( personId.trim() );
            EntityDetailsBean finDetails =
                finEntityDetails.getFinancialEntityDetails( entityNumber );

            /* set the Financial Entity details in the request scope to display
             * info in the AnnualDisclosures page
             */
            request.setAttribute( "financialEntity" , finDetails );

            /* Get the AnnualDisclosuresBean instance to get the pending disclosures
             * for the selected Financial Entity
             */
            AnnualDisclosuresBean annDisclDetails =
                    new AnnualDisclosuresBean( personId.trim() , entityNumber );
            Vector pendingDisclosures = annDisclDetails.getPenidngDisclosures();
            /* CASE #939 Begin */
            //Check whether user has updated all FEs.  If yes, put attribute in session.
            boolean allEntitiesUpdated = true;
            for(int cntFE=0; cntFE<finEntities.size(); cntFE++){
                EntityDetailsBean tempEntityDetails =
                    (EntityDetailsBean)finEntities.get(cntFE);
                if(tempEntityDetails.getAnnDisclUpdated() != null &&
                    tempEntityDetails.getAnnDisclUpdated().equals("N")){
                        allEntitiesUpdated = false;
                        break;
                }
            }
            //System.out.println("allEntitiesUpdated: "+allEntitiesUpdated);
            //If all updated, set flag in session to show OK button to go to confirmation page.
            if(allEntitiesUpdated){
                session.setAttribute("allEntitiesUpdated", "allEntitiesUpdated");
            }
            else{
                session.removeAttribute("allEntitiesUpdated");
            }
            /* CASE #939 End */

            /* set the retrieved pending disclosures in the session scope to display
             * the information in the Annual Disclosures page
             */
            session.setAttribute( "allPendingDisclosures" , pendingDisclosures );
            /* CASE # 912 comment begin */
            //request.setAttribute( "allAnnualDiscEntities" , finEntities );
            /* CASE # 912 comment end */
            /* CASE # 912 begin */
            //Put ann disc fe in session to be used by jsp and AnnualDisclosuresUpdateAction
            session.setAttribute("allAnnualDiscEntities", finEntities);
            /* CASE # 912 end */
            /* CASE #1046 Begin */
            DisclosureDetailsBean disclosureDetailsBean
                        = new DisclosureDetailsBean( personId.trim() );
            LinkedList collCOIStatus = disclosureDetailsBean.getAllCOIStatus();
            session.setAttribute( "collCOIStatus" , collCOIStatus );
            /* CASE #1046 End */
            errorFlag = false;
        } catch( CoeusException coeusEx ) {
            UtilFactory.log( coeusEx.getMessage() , coeusEx,
                                "AnnualDisclosuresAction" , "perform()" );
            request.setAttribute( "EXCEPTION" , coeusEx );
        } catch( DBException dbEx ) {
            request.setAttribute( "EXCEPTION" , dbEx );
        } catch( Exception ex ) {
            ex.printStackTrace();
            UtilFactory.log( ex.getMessage() , ex ,
                                "AnnualDisclosuresAction" , "perform()" );
            request.setAttribute( "EXCEPTION" ,
                                new CoeusException( "exceptionCode.70006" ) );
        }
        if( errorFlag ) {
            actionforward = actionMapping.findForward( FAILURE );
        }
        return actionforward;
    }


   /* CASE #855 Comment Begin */
    /**
     * Process the specified HTTP request, and create the corresponding HTTP response
     * (or forward to another web component that will create it). Return an ActionForward instance
     * describing where and how control should be forwarded, or null if the response
     * has already been completed.
     *
     * <br> The method used to fetch all the annual financial entity disclosures
     * for a person by using <code>getAnnualDiscEntities</code> of
     * <code>AnnualDiscFinEntitiesBean</code> and fetches all details of pending Financial Entity
	 * Disclosures by using <code>getPenidngDisclosures</code> of <code>AnnualDisclosuresBean</code> class.
     *
     * @param actionMapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @throws java.io.IOException if an input/output error occurs
     * @throws javax.servlet.ServletException if a servlet exception occurs.

    public ActionForward perform( ActionMapping actionMapping ,
            ActionForm actionForm , HttpServletRequest request ,
                HttpServletResponse response )
                    throws IOException ,ServletException{
        System.out.println("Inside AnnualDisclosuresAction");
        String personId = null;
        String userName = "";
        String entityNumber = null;
        boolean errorFlag = false;
        int finEntityIndex;
        /* CASE #404 Begin
        EntityDetailsBean entityDetails = null;
        String action = null;
        /* CASE #404 End

        final String TO_CONFIRMATION_PAGE = "annDisclConfirmation";
        /* CASE #406 Begin
        final String CHECK_UPDATE_FAILURE = "checkUpdateFailure";
        /* CASE #406 End

        HttpSession session = request.getSession();
        ActionForward actionforward = actionMapping.findForward( SUCCESS );
        UtilFactory UtilFactory = new UtilFactory();

        try {
            /* look userName attribute in session scope
            userName = ( String ) session.getAttribute( USERNAME );
            /* If user session is  expired then forward the
             * user to session exipiration page

            if( userName == null ) {
                actionforward = actionMapping.findForward( EXPIRE );
                return ( actionforward );
            }

            /* look personId in session scope
            personId = ( String ) session.getAttribute( LOGGEDINPERSONID );

            /* CASE #404 Comment Begin */
            /* check whether this action is called from AnnDisclosuresLeftPane.jsp by
             * checking the entityIndex in request scope.If it is available set
             * the index in session scope for further use in
             * AnnDisclosureUpdateAction.java and if it is not availbale in
             * the request scope fetch it from the session and use in
             * getting the information about that financial entity.
             */
            /*String finIndex = request.getParameter( "entityIndex" );
            if( finIndex != null ) {
                finEntityIndex = Integer.parseInt( finIndex.toString() );
                session.setAttribute( "finEntityIndex" , new Integer( finEntityIndex ) );
            } else {
                Object tempIndex = session.getAttribute( "finEntityIndex" );
                if( tempIndex != null ) {
                    finEntityIndex = Integer.parseInt(
                          session.getAttribute( "finEntityIndex" ).toString() );
                } else {
                    finEntityIndex = 0;
                }
            }*/
            /* CASE #404 Comment End */

            /*  get the instance of AnnualDisclFinEntitiesBean to get all the
             *  pending Financial Entities.
             *  set the the collection of pending Financial Entities in the request scope.

            AnnualDiscFinEntitiesBean annualDisclFinEntities =
                    new AnnualDiscFinEntitiesBean( personId.trim() );
            Vector finEntities = annualDisclFinEntities.getAnnualDiscEntities();
            request.setAttribute( "allAnnualDiscEntities" , finEntities );
            /* CASE #404 Begin
            action = (String)request.getAttribute("action");
            // entityNumber parameter passed by link in JSP left pane
            entityNumber = request.getParameter("entityNumber");
            EntityDetailsBean tempEntityDetails = null;
            /* CASE #406 Begin
            /* Check for presence of "errors" attribute set in request by
             * AnnDisclFinalUpdateAction.  If present, get the entity number for
             the first AnnDisclosureErrorBean in the collection.
             if(request.getAttribute("errors") != null){
                Vector checkErrors = (Vector)request.getAttribute("errors");
                AnnDisclosureErrorBean errorBean =
                    (AnnDisclosureErrorBean)checkErrors.get(0);
                entityNumber = errorBean.getEntityNumber();
             }
            /* CASE #406 End
            //If no entity number or action passed, get the first entity from entities
            //returned by stored procedure call (ordered by entity name).
            else if(action == null && entityNumber == null){
                /* CASE #409 Begin */
                /* Check if request is from user with no fin entities, wanting to
                finalize the ann disc process
                if(request.getParameter("action") != null
                  && request.getParameter("action").equalsIgnoreCase("noFinEnt")){
                    AnnDisclFinalUpdateBean finalUpdateBean =
                        new AnnDisclFinalUpdateBean(personId);
                    Vector checkErrors = finalUpdateBean.checkAnnualDisclComplete();
                    AnnDisclosureErrorBean errorBean = null;
                    if( ( checkErrors != null ) && (checkErrors.size() > 0 ) ) {
                        if ( checkErrors.get(0) instanceof AnnDisclosureErrorBean ) {
                            errorBean = (AnnDisclosureErrorBean)checkErrors.get(0);
                        }
                        //User has disclosures with status 104, and conflict
                        //status for all fin entities for those discl's is 100 or 301.
                        if ( errorBean.getEntityNumber().toString().equals("1") ) {
                        /* CASE #406 End
                            actionforward = actionMapping.findForward( TO_CONFIRMATION_PAGE );
                        /* CASE #406 Begin
                        }
                        // User has no disclosures with status 104.
                        else {
                            request.setAttribute("errors", checkErrors);
                            actionforward = actionMapping.findForward( CHECK_UPDATE_FAILURE );
                        }
                        return actionforward;
                    }
                }
                else{
                /* CASE #409 End */
                    /* CASE #410 Comment Begin */
                    //tempEntityDetails = (EntityDetailsBean)finEntities.get(0);
                    /* CASE #410 Comment End */
                    /* CASE #410 Begin
                    for(int entCount = 0; entCount < finEntities.size(); entCount++){
                        tempEntityDetails = (EntityDetailsBean)finEntities.get(entCount);
                        if(tempEntityDetails.getAnnDisclUpdated() != null &&
                            tempEntityDetails.getAnnDisclUpdated().equals("N")){
                            break;
                        }
                    }
                    /* CASE #410 End
                    entityNumber = tempEntityDetails.getNumber();
                }
            }
            //For save and proceed action, if there are more entities in the list,
            //display the next entity.  Else, check for if user is ready to submit
            //annual disclosures.
            else if(action != null && action.equals("saveAndProceed")){
                entityNumber = (String)request.getAttribute("entityNumber");
                for(int lookForEnt = 0; lookForEnt < finEntities.size(); lookForEnt++){
                    tempEntityDetails =
                        (EntityDetailsBean)finEntities.get(lookForEnt);
                    if(tempEntityDetails.getNumber().equals(entityNumber)){
                        //get the next entity from entities passed by stored procedure call.
                        int nextEntIndex = lookForEnt + 1;
                        if(nextEntIndex == finEntities.size()){
                            /* CASE #406 Begin
                            //If there are no more fin entities in the list, check if
                            //user is ready to submit annual disclosures.  If yes,
                            //forward to confirmation page. Else, display main page
                            //with errors.
                            AnnDisclFinalUpdateBean finalUpdateBean =
                                new AnnDisclFinalUpdateBean(personId);
                            Vector checkErrors = finalUpdateBean.checkAnnualDisclComplete();
                            AnnDisclosureErrorBean errorBean = null;
                            if( ( checkErrors != null ) && (checkErrors.size() > 0 ) ) {
                                if ( checkErrors.get(0) instanceof AnnDisclosureErrorBean ) {
                                    errorBean = (AnnDisclosureErrorBean)checkErrors.get(0);
                                }
                                //User has disclosures with status 104, and conflict
                                //status for all fin entities for those discl's is 100 or 301.
                                if ( errorBean.getEntityNumber().toString().equals("1") ) {
                                /* CASE #406 End
                                    actionforward = actionMapping.findForward( TO_CONFIRMATION_PAGE );
                                /* CASE #406 Begin
                                }
                                // User has no disclosures with status 104.
                                else if( errorBean.getEntityNumber().toString().equals("0") ){
                                    request.setAttribute("errors", checkErrors);
                                    actionforward = actionMapping.findForward( CHECK_UPDATE_FAILURE );
                                }
                                // User has disclosures with status 104, and some
                                // have fin entities with conflict status not 100 or 301.
                                else {
                                    request.setAttribute("errors", checkErrors);
                                    actionforward = actionMapping.findForward( CHECK_UPDATE_FAILURE );
                                }
                            }
                            /* CASE #406 End */
                            /* CASE #408 Comment Begin */
                            //return actionforward;
                            /* CASE #408 Comment End
                        }
                        else{
                            EntityDetailsBean tempEntityDetails2 =
                                (EntityDetailsBean)finEntities.get(nextEntIndex);
                            entityNumber = tempEntityDetails2.getNumber();
                            break;
                        }
                    }
                }
            }

            /* CASE #404 End */

            /* CASE #404 Comment Begin */
            /*
             * If finEntityIndex is more than the size of the collection of pending
             * Financial Entities then forward yo confirmation page.
             */
            /*if( finEntityIndex > finEntities.size() - 1 ) {
                actionforward = actionMapping.findForward( TO_CONFIRMATION_PAGE );
                return actionforward;
                //finEntityIndex = finEntities.size() - 1;
            }*/

            /* Get the instance of EntityDetailsBean  to get the info of a selected
             * Financial Entity
             */
            //EntityDetailsBean financialEntity = new EntityDetailsBean();

            /* get the Financial Entity Details for the selected finEntityIndex */
            //if( finEntities.get( finEntityIndex ) instanceof EntityDetailsBean ) {
               // financialEntity = ( EntityDetailsBean ) finEntities.get( finEntityIndex );
            //}

            /*
             * Get the Entity Number if it is available in request scope (ie. If a
             * particular Fianncial entity is selected in the left pane of the
             * Annual Disclosures page).
             */
            //entityNumber = request.getParameter( "entityNumber" );
            /* if the entityNumber is not present in the request scope that means
             * if no entity is selected by the user then get the entityNumber from the
             * retrieved Financial Entity info.
             *
             * set the entityNumber in request scope
             */
            //if( entityNumber == null ) {
              // entityNumber = financialEntity.getNumber().trim();
            //}
            //request.setAttribute( "entityNumber" , entityNumber );
            /* CASE #404 Comment End */

            /* Get the instance of FinancialEntityDetailsBean to retrieve the
             * Financial Entity Details

            FinancialEntityDetailsBean finEntityDetails =
                new FinancialEntityDetailsBean( personId.trim() );
            EntityDetailsBean finDetails =
                finEntityDetails.getFinancialEntityDetails( entityNumber );

            /* set the Financial Entity details in the request scope to display
             * info in the AnnualDisclosures page

            request.setAttribute( "financialEntity" , finDetails );

            /* Get the AnnualDisclosuresBean instance to get the pending disclosures
             * for the selected Financial Entity

            AnnualDisclosuresBean annDisclDetails =
            new AnnualDisclosuresBean( personId.trim() , entityNumber );
            Vector pendingDisclosures = annDisclDetails.getPenidngDisclosures();
            /* set the retrieved pending disclosures in the session scope to display
             * the information in the Annual Disclosures page

            session.setAttribute( "allPendingDisclosures" , pendingDisclosures );
        } catch( CoeusException coeusEx ) {
            errorFlag = true;
            UtilFactory.log( coeusEx.getMessage() , coeusEx,
                                "AnnualDisclosuresAction" , "perform()" );
            request.setAttribute( "EXCEPTION" , coeusEx );
        } catch( DBException dbEx ) {
            errorFlag = true;
            /* CASE #735 Comment Begin */
            //DBEngine prints the exception to log file.
            //UtilFactory.log( dbEx.getMessage() , dbEx ,
                              //  "AnnualDisclosuresAction" , "perform()" );
            /* CASE #735 Comment End
            request.setAttribute( "EXCEPTION" , dbEx );
        } catch( Exception ex ) {
            ex.printStackTrace();
            errorFlag = true;
            UtilFactory.log( ex.getMessage() , ex ,
                                "AnnualDisclosuresAction" , "perform()" );
            request.setAttribute( "EXCEPTION" ,
                                new CoeusException( "exceptionCode.70006" ) );
        }
        if( errorFlag ) {
            actionforward = actionMapping.findForward( "failure" );
        }
        /* CASE #408 Comment Begin */
        //else {
            //actionforward = actionMapping.findForward( "success" );
       // }
       /* CASE #408 Comment End
        return actionforward;
    }*/
    /* CASE #855 Comment End */
}
