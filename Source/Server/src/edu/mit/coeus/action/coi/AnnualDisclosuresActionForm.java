/*
 * @(#)AnnualDisclosuresActionForm.java 1.0 6/5/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.action.coi;

import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.coi.bean.EntityDetailsBean;
import edu.mit.coeus.coi.bean.FinancialEntityDetailsBean;
import edu.mit.coeus.coi.bean.AnnDisclosureDetailsBean;
import edu.mit.coeus.coi.bean.AnnualDiscFinEntitiesBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
/* CASE #734 Comment Begin */
//import edu.mit.coeus.coi.exception.CoeusException;
/* CASE #734 Comment End */
/* CASE #734 Begin */
import edu.mit.coeus.exception.CoeusException;
/* CASE #734 End */
import edu.mit.coeus.action.common.CoeusActionBase;


/**
 * <code>AnnualDisclosuresActionForm</code> is to hold the information for the
 * AnnualDisclosure form used in <code>AnnualDisclosures.jsp</code> page.
 *
 * @version 1.0 June 5,2002
 * @author Phaneendra Kumar.
 */
public class AnnualDisclosuresActionForm extends ActionForm{

    /*CASE #404 Begin */
    /**
     * The value of the entity number for this financial entity.
     */
    private String number;
    /*CASE #404 End */

    /*
     * The value of the Financial Entity Name that user has selected as form data
     * to get pending disclosures
     */
    private String name;

    /*
     * The value of the Type that user has selected as form data
     */
    private String type;

    /*
     * The value of the Status that user has selected as form data
     */
    private String status;

    /*
     * The value of the ShareOwnShip that user has selected as form data
     */
    private String shareOwnship;

    /*
     * The value of the conflictStatus that user has selected as form data
     */
    private String conflictStatus;

    /* CASE #407 Begin */
    /**
     * Get entityNumber
     * @return entityNumber
     */
    public String getNumber(){
        return number;
    }

    /**
     * Set entityNumber
     * @param entityNumber
     */
    public void setNumber(String number){
        this.number = number;
    }
    /* CASE #407 End */


    /**
     * Gets the selected Status in the Annual Disclosures form
     *
     * @return The status of the Disclosure information user selected in the
     *  AnnualDisclosures page
     */
    public String getStatus(){
        return this.status;
    }

    /**
     * Sets the selected Status by the user in the AnnualDisclosures page.
     *
     * @param newStatus The Stastus information user selected for the Disclosure.
     */
    public void setStatus( String newStatus ){
        this.status = newStatus;
    }

    /**
     * Gets the selected Type of Annual Disclosure
	 *
     * @return The type of the Disclosure information displayed in the
     *  AnnualDisclosures page
     */
    public String getType(){
        return this.type;
    }

    /**
     * Sets the selected Type of Annual disclosure in the form
	 *
     * @param newType The Type for the Disclosure in the Annual Disclosures form
     */
    public void setType( String newType ){
        this.type = newType;
    }

    /**
     * Gets the ShareOwnship in the form
	 *
     * @return The selected shareOwnship of the disclosure
     */
    public String getShareOwnship(){
        return this.shareOwnship;
    }

    /**
     * Sets the ShareOwnship in the form
	 *
     * @param shareOwnship The selected shareOwnship of Annual Disclosure
     */
    public void setShareOwnship( String shareOwnship ){
        this.shareOwnship = shareOwnship;
    }

    /**
     * Gets the Entity Name of the Annual Disclosure.
	 *
     * @return The Entity Name of the disclosure in the AnnualDisclosures form
     */
    public String getName(){
        return this.name;
    }

    /**
     * Sets the Financial Entity Name in the Annual Disclosure form.
     *
     * @param name The Entity name in the Annual Disclosure form.
     */
    public void setName( String name ){
        this.name = name;
    }

    /**
     * Gets the selected conflictStatus of Disclosure in the form
	 *
     * @return  The selected conflict status from Annual Disclosures form.
     */
    public String getConflictStatus(){
        return this.conflictStatus;
    }

    /**
     * Sets the selected conflictStatus in the Annual Disclosures form
	 *
     * @param conflictStatus Annual Disclosure ConflictStatus
	 */
    public void setConflictStatus( String conflictStatus ){
        this.conflictStatus = conflictStatus;
    }

    /**
     * Reset all properties to their default values.
     *
     * @param actionMapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset( ActionMapping actionMapping , HttpServletRequest request ){
        /* cASE #1046 Comment Begin */
        /*this.status = "";
        this.type = "";
        this.name = "";
        this.shareOwnship = "";*/
        /* CASE #1046 Comment end */
    }

    /**
     * Validate the properties that have been set for this request,
     * and return an ActionErrors object that encapsulates any validation errors that have been found.
     * If no errors are found, return null or an ActionErrors object with no recorded error messages
	 *
     * Default implementation is nothing and may be used for further requirements
	 *
     * @param actionMapping The mapping used to select this instance
     * @param request  The servlet request we are processing
     */
    public ActionErrors validate( ActionMapping actionMapping ,
    HttpServletRequest request ){
        System.out.println("inside AnnualDisclosuresActionForm.validate()");
        ActionErrors actionErrors = new ActionErrors();
//        UtilFactory UtilFactory = new UtilFactory();
        HttpSession session = request.getSession();
        String personId =
            (String)session.getAttribute(CoeusActionBase.LOGGEDINPERSONID);
        /* CASE #736 Begin */
        // If session is expired, return.  Action class will forward appropriately.
        if(personId == null){
            return actionErrors;
        }
        /* CASE #736 End */
        /* CASE #1046 Comment Begin */
        /* CASE #404 Begin */
        //PI Identified Conflict and No Conflict are the only allowed values for
        //disclosure status.
        /*String[] conflictStatusValues = request.getParameterValues( "disclConflictStatus" );
        if(conflictStatusValues != null){
            for(int cnt=0; cnt < conflictStatusValues.length; cnt++){
                String conflictStatus = conflictStatusValues[cnt];
                if (!conflictStatus.equals("200") && !conflictStatus.equals("301")){
                    actionErrors.add( "invalidConflictSatus" ,
                        new ActionError( "error.annDisclosureActionForm.invalid.status" ) );
                    FinancialEntityDetailsBean finEntityDetails =
                        new FinancialEntityDetailsBean( personId.trim() );
                    EntityDetailsBean entityDetails = null;
                    try{
                        /* Get details of financial entity to which the disclosure pertains. */
                        /*entityDetails =
                            finEntityDetails.getFinancialEntityDetails( number );
                        request.setAttribute("financialEntity", entityDetails);
                        /* Get details for all financial entities to display in left pane. */
                        /*AnnualDiscFinEntitiesBean annualDisclFinEntities =
                                new AnnualDiscFinEntitiesBean( personId.trim() );
                        /* CASE #912 Comment Begin */
                        //Vector finEntities = annualDisclFinEntities.getAnnualDiscEntities();
                        //request.setAttribute( "allAnnualDiscEntities" , finEntities );
                        /* CASE #912 Comment End */
                        /* CASE #912 Begin */
                        /*Vector finEntities =
                            (Vector)session.getAttribute("allAnnualDiscEntities");
                        /* CASE #912 End */
                        /* Get details of all annual disclosures and update with
                        user input for conflict status. */
                        /*AnnDisclosureDetailsBean annualDisclosure =
                            new AnnDisclosureDetailsBean();
                        Vector annualDisclosures =
                            (Vector)session.getAttribute( "allPendingDisclosures" );
                        Vector updatedDisclosures = new Vector();
                        int annualDisclosuresSize = annualDisclosures.size();
                        if(( annualDisclosures != null ) && ( 0 < annualDisclosures.size())){
                            for( int count = 0 ; count < annualDisclosuresSize ; count++ ) {
                                if( annualDisclosures.get( cnt ) instanceof AnnDisclosureDetailsBean ) {
                                    annualDisclosure = ( AnnDisclosureDetailsBean ) annualDisclosures.get( count );
                                }
                                annualDisclosure.setConflictStatus( conflictStatusValues[ count ] );
                                updatedDisclosures.add( annualDisclosure );
                            }
                            session.setAttribute("allPendingDisclosures", updatedDisclosures);
                        }
                    }catch(CoeusException cEx){
                        UtilFactory.log( cEx.getMessage() , cEx ,
                                    "AnnualDisclosuresActionForm" , "validate()" );
                    }catch(DBException dbEx){
                      /* CASE #735 Comment Begin */
                      //DBEngine prints the exception to log file.
                        //UtilFactory.log( dbEx.getMessage(), dbEx,
                                  //  "AnnualDisclosuresActionForm" , "validate()" );
                        /* CASE #735 Comment End */
                    /*}
                    break;*/
                    /* CASE #404 End */
                    /* CASE #1046 Comment End */
        /* CASE #1046 Begin */
       //JSP updated to get conflict status descriptions dynamically
        try{
            Vector annualDisclosures =
                (Vector)session.getAttribute( "allPendingDisclosures" );
            Vector updatedDisclosures = new Vector();
            AnnDisclosureDetailsBean annualDisclosure =
                new AnnDisclosureDetailsBean();
            boolean invalidConflictStatus = false;
            for(int disclIndex=0; disclIndex<annualDisclosures.size(); disclIndex++){
                String updatedConflictStatus =
                  request.getParameter("disclConflictStatus"+disclIndex);
                annualDisclosure =
                  (AnnDisclosureDetailsBean)annualDisclosures.get(disclIndex);
                annualDisclosure.setConflictStatus(updatedConflictStatus);
                updatedDisclosures.add(annualDisclosure);
                if(updatedConflictStatus != null){
                    if (!updatedConflictStatus.equals("200") &&
                                          !updatedConflictStatus.equals("301")){
                        invalidConflictStatus = true;
                    }
                }
            }
            if(invalidConflictStatus){
                actionErrors.add( "invalidConflictSatus" ,
                    new ActionError( "error.annDisclosureActionForm.invalid.status" ) );
                FinancialEntityDetailsBean finEntityDetails =
                    new FinancialEntityDetailsBean( personId.trim() );
                EntityDetailsBean entityDetails = null;
                /* Get details of financial entity to which the disclosure pertains. */
                entityDetails =
                    finEntityDetails.getFinancialEntityDetails( number );
                request.setAttribute("financialEntity", entityDetails);
                session.setAttribute("allPendingDisclosures", updatedDisclosures);
            }//end if
        }catch(CoeusException cEx){
            UtilFactory.log( cEx.getMessage() , cEx ,
                        "AnnualDisclosuresActionForm" , "validate()" );
        }catch(DBException dbEx){
            //logged by dbengine.
        }

        /* CASE #1046 End */
        return actionErrors;

    }//end of validate method
}