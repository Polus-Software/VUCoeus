/*
 * @(#)NewCOIDisclosureActionForm.java 1.0	2002/06/03 16:30:23.
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.action.coi;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.mit.coeus.coi.bean.DisclosureValidationBean;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.UtilFactory;
/**
 * <code>NewCOIDisclosureActionForm</code> is a struts implemented ActionForm class
 *  which enables the user to select type(Award/Proposal/Temporary) of Disclosure to
 *  create a new disclosure along with its information.
 *
 * @version	1.0 June 10,2002 16:30:23
 * @author	RaYaKu
 */
public class NewCOIDisclosureActionForm extends ActionForm{
    
    /* CASE #1374 Begin */
    public void NewCOIDisclosureActionForm(){
        //System.out.println("***inside newcoidisclosureactionform constructor: setDisclosrueType to new");
        setDisclType( "new" );
    }
    /* CASE #1374 End */

    /*
     * Name of user who wish to create a new  disclosure.
     */
    private String userName;

    /*
     * Person Id on which the new  disclosure is going to be creat
     */
    private String personId;

    /*
     * Person Name on which the new  disclosure is going to be created
     */
    private String personName;

    /*
     * New or midyear disclosure
     */
    private String disclType;


    /**
     * Gets the Name of the user who is creating disclosure
     *
     * @return name of the logged in user.
     */
    public String getUserName(){
        return this.userName;
    }

    /**
     * Gets the person id
     *
     * @return Person Id to whom this disclosure is going to be created.
     */
    public String getPersonId(){
        return this.personId;
    }

    /**
     * Gets name of the person who is going to have this disclosure.
	 *
	 * @return Name of the person who will own this disclosure.
     */
    public String getPersonName(){
        return this.personName;
    }

    /**
     * Gets the Disclosure Type.(Award/Proposal/Temporary)
	 *
	 * @return Type of Disclosure.
     */
    public String getDisclType(){
        //System.out.println("inside getDisclType");
        return this.disclType;
    }

    /**
     * Sets the Name of the user who wish to create a disclosure
	 *
	 * @param newUserName  Name of the logged in user.
     */
    public void setUserName( String newUserName ){
        this.userName = newUserName;
    }

    /**
     * Set the person id on which the new disclosure is going to be created.
	 *
	 * @param newPersonId  Id/code of the Person on which the disclosure will be created.
     */
    public void setPersonId( String newPersonId ){
        this.personId = newPersonId;
    }

    /**
     * Set the person name on which user would like to create a new disclosure
	 *
	 * @param newPersonName Name of the Person on which the disclosure will be created.
     */
    public void setPersonName( String newPersonName ){
        this.personName = newPersonName;
    }

    /**
     * sets the Disclosure Type
     *
     * @param newDisclosureType - The type of disclousure that user wish to create
     *  on person . "new" for temp proposal, "midyear" for proposal or award
     */
    public void setDisclType( String newDisclType ){
        //System.out.println(" inside setDisclosureType()");
        this.disclType = newDisclType;
    }

    /**
     * Reset all bean properties to their default state.
     * This method is called before the properties are repopulated by the controller servlet.
     * The default implementation does nothing.
     *
     * @param actionMapping The actionMapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset( ActionMapping actionMapping , HttpServletRequest request ){
        //System.out.println("*** inside reset method, set disclosuretype to new");
        setDisclType( "new" );
    }

    /**
     * Validate the properties that have been set for this request,
     * and return an ActionErrors object that encapsulates any validation errors that have been found.
     * If no errors are found, return null or an ActionErrors object with no recorded error messages
     * <br> <b> Validations </b>
	 * <li> Disclosure Type is required
	 * <li> Award number is required if selected disclosure type is Award.
	 * <li> Proposal Number is required if selected disclosure type is Proposal.
	 *
     * @param actionMapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public ActionErrors validate( ActionMapping actionMapping , HttpServletRequest request ){
        ActionErrors actionErrors = new ActionErrors();
        System.out.println("inside NewCOIDisclosureActionForm.validate()");
        HttpSession session = request.getSession(true);
        /* CASE #1374 Comment Begin */
        /*String tempProposalAction = (String)session.getAttribute("tempProposalAction");
        if(tempProposalAction != null){
            System.out.println("tempProposalAction != null");
            return actionErrors;
        }
        /* CASE #1374 Comment End */
        /* CASE #231 End */
        //Ensure user should should select on disclosure type
        if( ( disclType == null ) || ( disclType.trim().equals( "" ) ) ) {
            //System.out.println("disclosure type null");
            actionErrors.add( "disclosureTypeRequired" , new ActionError(
                "error.newCOIDisclosureForm.disclosureType.required" ) );
        }
        return actionErrors;
       
    }////end of validate Method
}