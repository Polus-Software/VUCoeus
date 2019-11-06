/*
 * @(#)COIDisclosureActionForm.java	1.0 05/13/2002 10:20:09
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.action.coi;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import javax.servlet.http.HttpServletRequest;

/**
 * <code>COIDisclosureActionForm</code> is a struts implemented action form bean
 * holds user selected search criteria and used in searching COI Disclosures.
 *
 * @version  May 13, 2002  10:20:09
 * @author RaYaKu
 */

public class COIDisclosureActionForm extends ActionForm{

    /*
     * The value of the Status that user has selected  in form to
     * search COIDisclosures
     */
    private String status;

    /*
     * The value of the Type that user has selected as form data to search COIDisclosures
     */
    private String type;

    /*
     * The value of the AppliesTo that user has selected as form data to search
     * COIDisclosures
     */
    private String appliesTo;

    /*
     * The value of the Award/Proposal number that user has selected as form data
     * to search COIDisclosures
     */
    private String awardProposalNum;

    /*
     * The value of the Person  that user has selected as form data
     * to search COIDisclosures
     */
    private String personName;

    /*
     * The value of the Person ID  that user has selected as form data
     *  to search COIDisclosures
     */
    private String personId;

    /**
     * Get the selected Status from the form to search COIDisclosures
     *
     * @return  Status of COI DIsclosure that is used as parameter in COI Disclosure search.
     */
    public String getStatus(){
        return this.status;
    }

    /**
     * Set the selected Status into the form to search COI Disclosures
     *
     * @param newStatus User selected status to search COI Disclosure.
     */
    public void setStatus( String newStatus ){
        this.status = newStatus;
    }

    /**
     * Get the selected Type from the from to search COI Disclosures
     *
     * @return Type of COI DIsclosure that is used as search parameter.
     */
    public String getType(){
        return this.type;
    }

    /**
     * Set the selected Type into the form to search COI Disclosures
     *
     * @param newType User selected disclosure type which is used in search
     */
    public void setType( String newType ){
        this.type = newType;
    }

    /**
     * Get the selected AppliesTo from the form to search COI Disclosures
     *
     * @return a search parameter for COI Disclosures search.
     */
    public String getAppliesTo(){
        return this.appliesTo;
    }

    /**
     * Set the selected AppliesTo from the form to search COI Disclosures
     *
     * @param newAppliesTo User selected Applies To value used in search
     */
    public void setAppliesTo( String newAppliesTo ){
        this.appliesTo = newAppliesTo;
    }

    /**
     * Get the Award/Proposal Number from the form to search COI Disclosures
     *
     * @param AwardProposal Number that will be used in COI DIslcosures search.
     */
    public String getAwardProposalNum(){
        return this.awardProposalNum;
    }

    /**
     * Set the Award/Proposal Number in the form to search COI Disclosures
     *
     * @param newAwardProposalNum New AwardProposal number going to be used in search.
     */
    public void setAwardProposalNum( String newAwardProposalNum ){
        this.awardProposalNum = newAwardProposalNum;
    }

    /**
     * Get the Person Name from  the form to search COI Disclosures
     *
     * @return Name of the Person that is used as search parameter for COI Disclosures
     */
    public String getPersonName(){
        return this.personName;
    }

    /**
     * Set the Person Name into the form to search COIDisclosures
     *
     * @param newPersonName Name of the person used in  coi disclosures search
     */
    public void setPersonName( String newPersonName ){
        this.personName = newPersonName;
    }

    /**
     * Get the Person ID from the form to search COI Disclosures.
     *
     * @return Persson ID of COI Disclosure
     */
    public String getPersonId(){
        return this.personId;
    }

    /**
     * Set the Person ID into the form to search COIDisclosures.
     *
     * @param newPersonId Id of a person that is used in search for coi disclosures.
     */
    public void setPersonId( String newPersonId ){
        this.personId = newPersonId;
    }

    /**
     * Reset all bean properties to their default state.
     * This method is called before the properties are repopulated by the
     * controller servlet.
     * The default implementation resets the values to "" or null.
     *
     * @param actionMapping The actionMapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset( ActionMapping actionMapping , HttpServletRequest request ){
        this.status = "";
        this.type = "";
        this.appliesTo = "";
        this.awardProposalNum = "";
        this.personName = "";
        this.personId="";
    }

    /**
     * Validate the properties that have been set for this  request,
     * and return an ActionErrors object that encapsulates any validation errors
     * that have been found.
     * If no errors are found, return null or an ActionErrors object with no recorded error messages
     *
     * <br> <b> Validation rules </b>
     * <li> Atleast a single search comoponent should be selected .
     *
     * @param actionMapping -The mapping used to select this instance
     * @param request  The servlet request we are processing
     */
    public ActionErrors validate( ActionMapping actionMapping ,
    	HttpServletRequest request ){
        ActionErrors actionErrors = new ActionErrors();

        /* other than person Information user should select one of
         * selectStatus,selectType,selectAppliesTo and textAPNo components
         * to search for COIDisclosures.
         */
        if (!  ( ( status.length() > 0 ) || ( type.length() > 0 )
        	||( appliesTo.length() > 0 ) || ( awardProposalNum.length() > 0 )
        	||(personName.length()>0)   )  ){

            actionErrors.add( ActionErrors.GLOBAL_ERROR ,
            new ActionError( "error.coiDisclosureForm.search.required" ) );
        }
        if ( (awardProposalNum != null) && (10 <  awardProposalNum.length()) ) {
            actionErrors.add( "Award/Proposal Number is too long" , new ActionError( "error.COIDisclosureActionForm.AwardPropNum.toolong" ) );
        }
        return actionErrors;
    }//end of validate Method
}