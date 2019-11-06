/*
 * @(#)AnnualDisclosuresUpdateAction.java 1.0 6/9/02
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
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForward;
import java.util.Vector;
import java.io.IOException;

import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.UtilFactory;
/* CASE #734 Comment Begin */
//import edu.mit.coeus.coi.exception.CoeusException;
/* CASE #734 Comment End */
/* CASE #734 Begin */
import edu.mit.coeus.exception.CoeusException;
/* CASE #734 End */
import edu.mit.coeus.coi.bean.AnnDisclosureDetailsBean;
import edu.mit.coeus.coi.bean.AnnDisclosureUpdateBean;
import edu.mit.coeus.action.common.CoeusActionBase;
/* CASE #855 Begin */
import edu.mit.coeus.coi.bean.AnnDisclFinalUpdateBean;
import edu.mit.coeus.coi.bean.AnnDisclosureErrorBean;
import edu.mit.coeus.coi.bean.AnnualDiscFinEntitiesBean;
import edu.mit.coeus.coi.bean.EntityDetailsBean;
import edu.mit.coeus.coi.bean.AnnualDisclosuresBean;
/* CASE #855 End */


/**
 * <code>AnnualDisclosuresUpdateAction</code> is struts implemented Action class
 * to update the reviewed pending AnnualDisclosures.
 *
 * @version 1.0 June 9,2002
 * @author Phaneendra Kumar.
 */
public class AnnualDisclosuresUpdateAction extends CoeusActionBase{

    /**
     * Process the specified HTTP request, and create the corresponding HTTP response
     * (or forward to another web component that will create it).
     * Return an ActionForward instance describing where and how control should
     * be forwarded, or null if the response has already been completed.
     *
     * <br>The method used to update the status and submit the annual pending disclosures.
     * It will extract all the required data from the request object and set the details to
     * <code>AnnDisclosureDetailsBean</code> and pass the instance to
     * <code>updateAnnualDisclosureInfo</code> method of
     * <code>AnnDisclosureUpdateBean</code> class.
     *
     * @param actionMapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @throws java.io.IOException if an input/output error occurs
     * @throws javax.servlet.ServletException if a servlet exception occurs.
     */

    /* CASE #855 Begin */
    public ActionForward perform( ActionMapping actionMapping ,
    ActionForm actionForm , HttpServletRequest request , HttpServletResponse response )
    throws IOException , ServletException{

        System.out.println("Inside AnnualDisclosuresUpdateAction");
        String userName = null;
        boolean result = false;
        String personId = null;

        HttpSession session = request.getSession();
//        UtilFactory UtilFactory = new UtilFactory();

        boolean errorFlag = true;
        ActionForward actionforward = actionMapping.findForward( SUCCESS );
        AnnDisclosureDetailsBean annualDisclosure = new AnnDisclosureDetailsBean();
        try {
            /* look userName attribute in session scope */
            userName = ( String ) session.getAttribute( USERNAME );
            /* If user session is expired then forward the
             * user to session exipiration page
             */
            if( userName == null ) {
                actionforward = actionMapping.findForward( EXPIRE );
                return ( actionforward );
            }
            personId = ( String ) session.getAttribute( LOGGEDINPERSONID );
            AnnualDisclosuresActionForm form =
                                  (AnnualDisclosuresActionForm)actionForm;
            String entityNumber = form.getNumber();
            //System.out.println("entityNumber from form: "+entityNumber);
            AnnualDisclosuresBean annualDisclosuresBean =
                            new AnnualDisclosuresBean(personId, entityNumber);
            Vector annualDisclosures = annualDisclosuresBean.getPenidngDisclosures();

            /* CASE #1046 Comment Begin */
            /* Get all the changed conlfict Status values for all the Pending Disclosures */
            /*String[] conflictStatusValues =
                request.getParameterValues( "disclConflictStatus" );
            Vector paramDisclosures = new Vector();

           /* Check whether any of the session variable are null or the conflistStatusValues is null
            *
            *  Get an AnnualDisclosureDetails bean instance from the collection of pending Disclosures
            *  and set the corresponding user selected conflict status in the bean instance.
            *
            *  Get the instance of AnnualDisclosureUpdateBean to update the reviewed status to all the annual Disclosures
            *  send all the reviewed Disclosures to this bean instance to update in the data base.
            */
            /*int annualDisclosuresSize = annualDisclosures.size();
            if( ( annualDisclosures != null ) && ( 0 < annualDisclosures.size()  )
                    && ( conflictStatusValues != null ) ) {
                for( int cnt = 0 ; cnt < annualDisclosuresSize ; cnt++ ) {
                    if( annualDisclosures.get( cnt ) instanceof AnnDisclosureDetailsBean ) {
                        annualDisclosure = ( AnnDisclosureDetailsBean ) annualDisclosures.get( cnt );
                    }
                    annualDisclosure.setConflictStatus( conflictStatusValues[ cnt ] );
                    paramDisclosures.add( annualDisclosure );
                }
                AnnDisclosureUpdateBean annDisclBean = new AnnDisclosureUpdateBean( userName );
                result = annDisclBean.updateAnnualDisclosureInfo( paramDisclosures );
            }else{
                throw new CoeusException("exceptionCode.70012");
            }*/

            /* CASE #1046 Begin */
            /* Get an AnnualDisclosureDetails bean instance from the collection of pending Disclosures
            *  and set the corresponding user selected conflict status in the bean instance.
            *
            *  Get the instance of AnnualDisclosureUpdateBean to update the reviewed status to all the annual Disclosures
            *  send all the reviewed Disclosures to this bean instance to update in the data base.
            */
            Vector paramDisclosures = new Vector();
            if(annualDisclosures != null){
                for(int cnt=0; cnt<annualDisclosures.size(); cnt++){
                    annualDisclosure =
                        ( AnnDisclosureDetailsBean ) annualDisclosures.get( cnt );
                    String updatedConflictStatus =
                        request.getParameter("disclConflictStatus"+cnt);
                    //System.out.println("updatedConflictStatus in updateaction: "+updatedConflictStatus);
                    /* CASE #1400 Begin */
                    // Check if conflict status has changed for this entity in this disclosure.
                    //If no, don't include it in the update.
                    //System.out.println("annualDisclosure.getDisclosureNumber: "+annualDisclosure.getDisclosureNumber());
                    //System.out.print("updatedConflictStatus: "+updatedConflictStatus);
                    //System.out.println("***annualDisclosure.getConflcitStatus: "+annualDisclosure.getConflictStatus());
                    if(!updatedConflictStatus.equals(annualDisclosure.getConflictStatus())){
                    /* CASE #1400 End */
                        annualDisclosure.setConflictStatus(updatedConflictStatus);
                        paramDisclosures.add( annualDisclosure );                        
                    }

                }
            }
            AnnDisclosureUpdateBean annDisclBean = new AnnDisclosureUpdateBean( userName );
            /* CASE #1400 Begin */
            if(paramDisclosures.size() > 0){
            /* cASE #1400 End */
                result = annDisclBean.updateAnnualDisclosureInfo( paramDisclosures );
            }
            
            /* CASE #1046 End */

            /* Case # 912 Comment Begin */
            /*AnnualDiscFinEntitiesBean annualDisclFinEntities =
                    new AnnualDiscFinEntitiesBean( personId.trim() );
            Vector finEntities = annualDisclFinEntities.getAnnualDiscEntities();*/
            /* Case # 912 Comment End*/
            /* Case #912 Begin */
            //Get finEntities from session in order to improve performance.
            Vector finEntities = (Vector)session.getAttribute("allAnnualDiscEntities");
            /* Case #912 End */
            String nextEntityNumber = null;
            int nextEntIndex = 0;
            //First, check whether there are entities that have not been updated.
            boolean allOtherEntUpdated = true;
            for(int checkAll=0; checkAll<finEntities.size()-1; checkAll++){
                EntityDetailsBean tempEntityDetails =
                    (EntityDetailsBean)finEntities.get(checkAll);
                if(tempEntityDetails.getAnnDisclUpdated() != null &&
                    tempEntityDetails.getAnnDisclUpdated().equals("N")){
                        allOtherEntUpdated = false;
                        break;
                }
            }
            //Get the index of the next entity in the collection.
            for(int lookForEnt = 0; lookForEnt < finEntities.size(); lookForEnt++){
                EntityDetailsBean tempEntityDetails =
                    (EntityDetailsBean)finEntities.get(lookForEnt);
                if(tempEntityDetails.getNumber().equals(entityNumber)){
                    nextEntIndex = lookForEnt + 1;
                }
            }
            //If this is the last entity in the collection...
            if(nextEntIndex == finEntities.size()){
                if(!allOtherEntUpdated){
                    //loop from beginning to find the next entity to update.
                    EntityDetailsBean nextFinEntityToUpdate = null;
                    for(int entCount=0; entCount<finEntities.size();
                                                          entCount++){
                        nextFinEntityToUpdate =
                            (EntityDetailsBean)finEntities.get(entCount);
                        if(nextFinEntityToUpdate.getAnnDisclUpdated().equals("N")){
                            nextEntityNumber = nextFinEntityToUpdate.getNumber();
                            break;
                        }
                    }
                }
                else{
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
                            actionforward = actionMapping.findForward( TO_CONFIRMATION_PAGE );
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
                }
            }
            //this is not the last entity in the collection.  Go to the next
            //entity in the collection.
            else{
                EntityDetailsBean nextFinEntity =
                            (EntityDetailsBean)finEntities.get(nextEntIndex);
                    nextEntityNumber = nextFinEntity.getNumber();
            }
            request.setAttribute("entityNumber", nextEntityNumber);
            errorFlag = false;
        } catch( CoeusException coeusEx ) {
            UtilFactory.log( coeusEx.getMessage() , coeusEx , "AnnualDisclosuresUpdateAction" , "perform()" );
            request.setAttribute( "EXCEPTION" , coeusEx );
        } catch( DBException dbEx ) {
            request.setAttribute( "EXCEPTION" , dbEx );
        }catch( Exception ex ) {
            UtilFactory.log( ex.getMessage() , ex , "AnnualDisclosuresUpdateAction" , "perform()" );
            request.setAttribute( "EXCEPTION" , new CoeusException( "exceptionCode.70008" ) );
        }
        if( errorFlag ) {
            actionforward = actionMapping.findForward( FAILURE);
        }
        return actionforward;
    }
}

    /* CASE #855 End */

    /* CASE #855 Comment Begin */
    /*
    public ActionForward perform( ActionMapping actionMapping ,
    ActionForm actionForm , HttpServletRequest request , HttpServletResponse response )
    throws IOException , ServletException{

        System.out.println("Inside AnnualDisclosuresUpdateAction");

        final String To_WELCOME_PAGE = "welcome";
        String userName = null;
        boolean result = false;
        /* CASE #404 Comment Begin */
        //int finEntityIndex = 0;
        /* CASE #404 Comment End

        HttpSession session = request.getSession();
        UtilFactory UtilFactory = new UtilFactory();

        boolean errorFlag = true;
        /* CASE #404 Comment Begin */
        //ActionForward actionforward = actionMapping.findForward( FAILURE );
        /* CASE #404 Comment End */
        /* CASE #404 Begin
        ActionForward actionforward = actionMapping.findForward( SUCCESS );
        /* CASE #404 End
        AnnDisclosureDetailsBean annualDisclosure = new AnnDisclosureDetailsBean();
        try {
            /* look userName attribute in session scope
            userName = ( String ) session.getAttribute( USERNAME );
            /* If user session is really expired then forward the
             * user to session exipiration page

            if( userName == null ) {
                actionforward = actionMapping.findForward( EXPIRE );
                return ( actionforward );
            }

            /* CASE #404 Begin
            AnnualDisclosuresActionForm form =
                (AnnualDisclosuresActionForm)actionForm;
            String entityNumber = form.getNumber();
            request.setAttribute("entityNumber", entityNumber);
            request.setAttribute("action", "saveAndProceed");
            /* CASE #404 End */

            /* Get all the changed conlfict Status values for all the Pending Disclosures
            String[] conflictStatusValues = request.getParameterValues( "disclConflictStatus" );
            /* CASE #404 Comment Begin */
            // reqType parameter no longer needed since we are not using the Save and Exit
            // button.
            /* Get the request page index
             * if the index is 0, then forward to annualDisclosure.do
             * else, forward to welcome page
             */
             /*
            String pageIndex = request.getParameter("reqType");
            switch(Integer.parseInt(pageIndex)){
                case(1):
                    actionforward = actionMapping.findForward( SUCCESS );
                    break;
                case(2):
                    actionforward = actionMapping.findForward( To_WELCOME_PAGE );
                    break;
            }
            */
            /* Get all the collection of pending Disclosures from the session scope
            Vector annualDisclosures = ( Vector ) session.getAttribute( "allPendingDisclosures" );
            Vector paramDisclosures = new Vector();

           /* Check whether any of the session variable are null or the conflistStatusValues is null
            *
            *  Get an AnnualDisclosureDetails bean instance from the collection of pending Disclosures
            *  and set the corresponding user selected conflict status in the bean instance.
            *
            *  Get the instance of AnnualDisclosureUpdateBean to update the reviewed status to all the annual Disclosures
            *  send all the reviewed Disclosures to this bean instance to update in the data base.

            int annualDisclosuresSize = annualDisclosures.size();
            if( ( annualDisclosures != null ) && ( 0 < annualDisclosures.size()  )
                    && ( conflictStatusValues != null ) ) {
                for( int cnt = 0 ; cnt < annualDisclosuresSize ; cnt++ ) {
                    if( annualDisclosures.get( cnt ) instanceof AnnDisclosureDetailsBean ) {
                        annualDisclosure = ( AnnDisclosureDetailsBean ) annualDisclosures.get( cnt );
                    }
                    annualDisclosure.setConflictStatus( conflictStatusValues[ cnt ] );
                    paramDisclosures.add( annualDisclosure );
                }
                AnnDisclosureUpdateBean annDisclBean = new AnnDisclosureUpdateBean( userName );
                result = annDisclBean.updateAnnualDisclosureInfo( paramDisclosures );
            }else{
                throw new CoeusException("exceptionCode.70012");
            }

            /* CASE #404 Comment Begin */
            // finEntityIndex no longer needed.  Save and Proceed to Next Entity button now
            // set to use entityNumber instead of finEntityIndex
            /*
             * Get the finEntityIndex from session scope and increment it and put it in
             * the session scope to show the next Financial Entity Details
             * and corresponding pending Disclosures for review in the left frame of the
             * Annual disclosures page.
             */
             /*
            Object index = session.getAttribute( "finEntityIndex" );
            if( index != null ) {
                finEntityIndex = Integer.parseInt( index.toString() );
                ++finEntityIndex;
                //if(++finEntityIndex == annualDisclosuresSize){
                  //  actionforward = actionMapping.findForward( TO_CONFIRMATION_PAGE );
                //}
            }else{
                throw new CoeusException("exceptionCode.70013");
            }
            session.setAttribute( "finEntityIndex" , new Integer( finEntityIndex ) );
            */
            /* CASE #404 Comment End
            errorFlag = false;
        } catch( CoeusException coeusEx ) {
            UtilFactory.log( coeusEx.getMessage() , coeusEx , "AnnualDisclosuresUpdateAction" , "perform()" );
            request.setAttribute( "EXCEPTION" , coeusEx );
        } catch( DBException dbEx ) {
            /* CASE #735 Comment Begin */
            //DBEngine prints the exception to log file.
            //UtilFactory.log( dbEx.getMessage() , dbEx , "AnnualDisclosuresUpdateAction" , "perform()" );
            /* CASE #735 Comment End
            request.setAttribute( "EXCEPTION" , dbEx );
        }catch( Exception ex ) {
            UtilFactory.log( ex.getMessage() , ex , "AnnualDisclosuresUpdateAction" , "perform()" );
            request.setAttribute( "EXCEPTION" , new CoeusException( "exceptionCode.70008" ) );
        }
        if( errorFlag ) {
            actionforward = actionMapping.findForward( FAILURE);
        }
        return actionforward;
    }*/
    /* CASE #855 Comment End */
//}
