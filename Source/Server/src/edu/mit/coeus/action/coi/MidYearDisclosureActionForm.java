/*
 * @(#)MidYearDisclosureActionForm.java 1.0	2002/06/03 16:30:23.
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
public class MidYearDisclosureActionForm extends ActionForm{
    
    public void MidYearDisclosureActionForm(){
        //System.out.println("***inside midyeardisclsouseactionform constructor setDisclosrueType to 1");
        setDisclosureType( "1" );
    }

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
     * Award Number of new Disclosure
     */
    private String awardNum;

    /*
     * Type of  new Disclosure
     */
    private String disclosureType;

    /*
     * Proposal Number of new Disclosure
     */
    private String proposalNum;

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
     * Gets the Award Number of new disclosure that is going to be created.
	 *
	 * @return Award number of new disclosure.
     */
    public String getAwardNum(){
        return this.awardNum;
    }

    /**
     * Gets the Proposal Number of new disclosure  that is going to created
	 *
	 * @return Proposal Number of new Disclosure.
     */
    public String getProposalNum(){
        return this.proposalNum;
    }

    /**
     * Gets the Disclosure Type.(Award/Proposal/Temporary)
	 *
	 * @return Type of Disclosure.
     */
    public String getDisclosureType(){
        System.out.println("inside getDisclosureType");
        return this.disclosureType;
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
     * Sets the Award Number
	 *
	 * @param newAwardNum The Award number on which a new disclosure is created.
     */
    public void setAwardNum( String newAwardNum ){
        this.awardNum = newAwardNum;
    }

    /**
     * Sets the Proposal Number
	 *
	 * @param newProposalNum The Proposal Number on which a new Disclosure is created.
     */
    public void setProposalNum( String newProposalNum ){
        this.proposalNum = newProposalNum;
    }

    /**
     * sets the Disclosure Type
     *
     * @param newDisclosureType - The type of disclousure that user wish to create
     *  on person . Ex. 1-Award, 2-Proposal and 3-Temporary
     */
    public void setDisclosureType( String newDisclosureType ){
        //System.out.println(" inside setDisclosureType()");
        this.disclosureType = newDisclosureType;
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
        //System.out.println("inside reset method, set disclosuretype to 1");
        setDisclosureType( "1" );
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
        //Ensure user should should select on disclosure type
        if( ( disclosureType == null ) || ( disclosureType.trim().equals( "" ) ) ) {
            //System.out.println("disclosure type null");
            actionErrors.add( "" , new ActionError(
                "error.newCOIDisclosureForm.disclosureType.required" ) );
        }        
        //System.out.println("validate for proposal or award disclosure for actionmapping midyeardisclsure");
        //System.out.println("disclosureType: "+disclosureType);
        boolean validResult = false;
        DisclosureValidationBean disclosureValidationBean = new DisclosureValidationBean();
        try{
            // if user wish to create Disclosure for existing Award then ensure the user to provide award number
            if( disclosureType.equals( "1" ) ) {
                // validate award number
                if( ( awardNum == null ) || ( awardNum.trim().equals( "" ) ) ) {
                    //System.out.println("disclosure type null");
                    actionErrors.add( "awardProposalNumRequired" , new ActionError(
                        "error.NewCOIDisclosureActionForm.awardNumRequired" ) );
                    return actionErrors;
                }                    
                validResult = disclosureValidationBean.isAwardNumValid( awardNum );
                request.setAttribute( "appliesToCode" , awardNum );
                // collect and keep  error information if award number is invalid.
                if( !validResult ) {
                    actionErrors.add( "Invalid Award Number" ,
                    new ActionError( "error.invalidAwardNumber" ) );
                }                    
            }

            // if user wish to create Disclosure for existing proposal then ensure the user to provide proposal number
            if( disclosureType.equals( "2" ) ) {
                //System.out.println("disclosureType equals 2");
                // validate proposal number
                if( ( proposalNum == null ) || ( proposalNum.trim().equals( "" ) ) ) {
                    System.out.println("adding action error for propoalNum null or ''");
                    actionErrors.add( "awardProposalNumRequired" , new ActionError(
                        "error.NewCOIDisclosureActionForm.proposalNumRequired" ) );
                    return actionErrors;
                }                      
                validResult = disclosureValidationBean.isProposalNumValid( proposalNum );
                request.setAttribute( "appliesToCode" , proposalNum );
                // if proposal number is invalid then generate an error
                if( !validResult ) {
                    actionErrors.add( "Invalid Proposal Number" ,
                    new ActionError( "error.invalidProposalNumber" ) );
                }                    
            }
        }
        catch (DBException DBEx){
            UtilFactory.log(DBEx.getMessage(), DBEx, "NewCOIDisclosureActionForm", "validate()");
        }
        return actionErrors;
       
    }////end of validate Method
}