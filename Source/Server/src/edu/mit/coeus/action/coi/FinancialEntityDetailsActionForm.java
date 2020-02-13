/*
 * @(#)FinancialEntityDetailsActionForm.java 	1.0	06/03/2002	16:30:23.
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.action.coi;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;

/**
 * <code>FinancialEntityDetailsActionForm</code> is a struts implemented
 * ActionForm class which holds details of Financial Entity that are
 * entered/modified by user. This form will be used by an action(FinancialEntityDetailsAction)
 * class to recieve and process these details.
 *
 * @version	1.0 June 3,2002  16:30:23
 * @author	RaYaKu
 */
public class FinancialEntityDetailsActionForm extends ActionForm{

    /*
     * Requester page information .
     */
    private String actionFrom;

    /*
     * The Value of  Financial Entity manipulation type (Update/Delete/Add).
     */
    private String actionType;

    /*
     * The value of the of Financial Entity Name that user is going to modify/add
     */
    private String name;

    /*
     * The value of the of Financial Entity Number  that user is going to modify/add
     */
    private String number;

    /*
     * The value of the Financial Entity Type that user wants.
     */
    private String type;

    /*
     * The value of the Financial Entity Type Code that user wants.
     */
    private String typeCode;

    /*
     * The value of Shared  Ownership of this Financial Entity
     */
    private String shareOwnership;

    /*
     * The value of the Financial Entity Status.
     */
    private String status;

    /*
     * The value of the Description of this Financial Entity.
     */
    private String description;

    /*
     * The value of Person Relationship Type  to Financial Entity.
     */
    private String personRelationType;

    /*
     * The value of Person Relationship Type code  to Financial Entity.
     */
    private String personRelationTypeCode;

    /*
     * The value of Person Relationship Description to Financial Entity.
     */
    private String personRelationDesc;

    /*
     * The value of Organization relationship type  to Financial Entity
     */
    private String orgRelationType;

    /*
     *	 The value of an Organization relationship Description to Financial Entity.
     */
    private String orgRelationDesc;

    /*
     * The value of Sponser name to Financial Entity.
     */
    private String sponsorName;

    /*
     * The value of  Sponser id to Financial Entity.
     */
    private String sponsorId;

    /*
     * The value of Last Updated time of this Financial Entity.
     */
    private String lastUpdate;

    /*
     * The value of Last Updated time in java.sql.Timestamp  in of this Financial Entity.
     */
    private String lastUpdateTimestamp;

    /*
     * The value of Last updated user name of this Financial Entity.
     */
    private String lastUpdatedUser;

    /*
     *	The value of Sequence Number of this Financial Entity.
     */
    private String sequenceNum;

    /*
     * The Value of User Name who is looking this Financial Entity.
     */
    private String userFullName;

    /*
     * The Value of status code of this Financial Entity.
     */
    private String statusCode;

    /*
     * The Value of User Name who is looking this Financial Entity.
     */
    private String userName;

    /**
     * Gets the Requester page information and can be used to forward back to same.
     * Ex: AnnualFE - Request is genereted from Annual Disclosures module so after
	 * Finanical Entity is added/modified the user visits the same( AnnualDisclosure module)
	 * page back.
	 *
     * @return Requester(page) information
     */
    public String getActionFrom(){
        return this.actionFrom;
    }

    /**
     * Sets the Requester page information, can be used to supply
     * this page back.
     *
     * @param newActionFrom User of this bean or requester information.
     */
    public void setActionFrom( String newActionFrom ){
        this.actionFrom = newActionFrom;
    }

    /**
     * Gets the Action Type that is going to be applied on this Financial Entity
     * that User wish.
     * Example:  Update(U)/Add(A).
     *
     *@return actionType
     */
    public String getActionType(){
        return this.actionType;
    }

    /**
     * Sets the Type of action that is going to applied on this Financial Entity by User
     *
     * @return  newActionType The type of action that an action class should
     *  do with these details Update(U)/Add(I).
     */
    public void setActionType( String newActionType ){
        this.actionType = newActionType;
    }

    /**
     * Get the User Name who is going to add/edit  this Financial Entity
     *
     * @return  Name of user who is adding/modfifying this finanical entity
     */
    public String getUserName(){
        return this.userName;
    }

    /**
     * Set the Name of the User who is going to modify this  Financial Entity.
     *
     * @return newUserName Name of the user who is going to modify this Financial Entity
     */
    public void setUserName( String newUserName ){
        this.userName = newUserName;
    }

    /**
     * Get the StatusCode of Financial Entity
     *
     * @return Status code of Entity.
     */
    public String getStatusCode(){
        return this.statusCode;
    }

    /**
     * Sets the StatusCode of  Financial Entity.
     *
     * @param newStatusCode New Status Code of Financial Entity.
     */
    public void setStatusCode( String newStatusCode ){
        this.statusCode = newStatusCode;
    }

    /**
     * Gets the User Full Name who is going to edit  this Financial Entity
     *
     * @return Full Name of the user who is adding/modifying this Financial Entity
     */
    public String getUserFullName(){
        return this.userFullName;
    }

    /**
     * Sets the Full Name of User who is going to modify/add this  Financial Entity.
     *
     * @param newUserFullName Full Name of the user who is adding/modifying this Financial Entity
     */
    public void setUserFullName( String newUserFullName ){
        this.userFullName = newUserFullName;
    }

    /**
     * Gets the Name of this Financial Entity
     *
     * @return  Name of the Financial Entity
     */
    public String getName(){
        return this.name;
    }

    /**
     * Sets the Name of Financial Entity on whcih it is created and identified.
     *
     * @param newName The Name of Financial Entity.
     */
    public void setName( String newName ){
        this.name = newName;
    }

    /**
     * Gets this Financial Entity Descritption and that to is modified/added  by user.
     *
     * @return description Returns the Financial Entity Description.
     */
    public String getDescription(){
        return this.description;
    }

    /**
     * Sets Financial Entity Descritption and that is modified/added by user.
     *
     * @param newDescription The description that is used in adding/modifying of FinancialEntity Details.
     */
    public void setDescription( String newDescription ){
        this.description = newDescription;
    }

    /**
     * Gets this Financial Entity Number .
     *
     * @return Returns the Financial Entity Number
     */
    public String getNumber(){
        return this.number;
    }

    /**
     * Sets  Financial Entity Number.
     *
     * @param newNumber  New Financial Entity Number of this Financial Entity.
     */
    public void setNumber( String newNumber ){
        this.number = newNumber;
    }

    /**
     * Gets this Person Relation Descritpion to this Financial Entity.
     *
     * @return personRelationDesc  Description of Person Relation to this Financial Entity.
     */
    public String getPersonRelationDesc(){
        return this.personRelationDesc;
    }

    /**
     * Sets Person Relation Description of this Financial Entity.
	 *
	 * @param newPersonRelationDesc Person Relation Description to this Entity.
     */
    public void setPersonRelationDesc( String newPersonRelationDesc ){
        this.personRelationDesc = newPersonRelationDesc;
    }

    /**
     * Gets this Organization Relation Descritpion to this Financial Entity.
     *
     * @return orgRelationDesc  Description of Organization Relation to this
     *  Financial Entity.
     */
    public String getOrgRelationDesc(){
        return this.orgRelationDesc;
    }

    /**
     * Sets Organization Relation Description with this Financial Entity.
	 *
	 * @param newOrgRelationDesc Organization relation description to this Entity
     */
    public void setOrgRelationDesc( String newOrgRelationDesc ){
        this.orgRelationDesc = newOrgRelationDesc;
    }

    /**
     * Gets this Sponsor Name of the Financial Entity
     *
     * @return Sponsor name to this Financial Entity.
     */
    public String getSponsorName(){
        return this.sponsorName;
    }

    /**
     * Sets  Sponsor Name of this Financial Entity.
	 *
	 * @param newSponsorName Name of the sponsor of Entity.
     */
    public void setSponsorName( String newSponsorName ){
        this.sponsorName = newSponsorName;
    }

    /**
     * Gets this Sponsor Id of the Financial Entity
     *
     * @return Sponsor ID to this Financial Entity.
     */
    public String getSponsorId(){
        return this.sponsorId;
    }

    /**
     * Sets  Sponsor ID of this Financial Entity.
     *
     * @param newSponsorId The Sponsor Id of Financial Entity.
     */
    public void setSponsorId( String newSponsorId ){
        this.sponsorId = newSponsorId;
    }

    /**
     * Gets this Last Updated user name of the Financial Entity
     *
     * @return Last Updated user name of this Financial Entity.
     */
    public String getLastUpdatedUser(){
        return this.lastUpdatedUser;
    }

    /**
     * Sets  Last updated user name of this Financial Entity.
	 *
	 * @param newLastUpdatedUser Last updated user name of this Financial Entity.
     */
    public void setLastUpdatedUser( String newLastUpdatedUser ){
        this.lastUpdatedUser = newLastUpdatedUser;
    }

    /**
     * Gets  Last Updated time of the Financial Entity
     *
     * @return Last Updated time of  this Financial Entity.
     */
    public String getLastUpdate(){
        return this.lastUpdate;
    }

    /**
     * Sets  Last updated time of this Financial Entity.
     *
     * @param newLastUpdate The Last updated time of Financial Entity in String format.
     */
    public void setLastUpdate( String newLastUpdate ){
        this.lastUpdate = newLastUpdate;
    }

    /**
     * Gets  Last Updated time of the Financial Entity
     *
     * @return Last Updated time of  this Financial Entity in String.
     */
    public String getLastUpdateTimestamp(){
        return this.lastUpdateTimestamp;
    }

    /**
     * Sets  Last updated time of this Financial Entity in String format
     *
     * @param newLastUpdateTimestamp LastUpdated time of this Financial Entity in java.sql.Timestamp format.
     */
    public void setLastUpdateTimestamp( String newLastUpdateTimestamp ){
        this.lastUpdateTimestamp = newLastUpdateTimestamp.toString();
    }

    /**
     * Gets Type of Financial Entity
     *
     * @return Type of Financial Entity.
     */
    public String getType(){
        return this.type;
    }

    /**
     * Sets Type of this Financial Entity.
     *
     * @param newType Type of Financial Entity that will be used in adding/modifying FE.
     */
    public void setType( String newType ){
        this.type = newType;
    }

    /**
     * Gets Type Code of Financial Entity that belongs to Financial Entity type
     *
     * @return Type Code of this Financial Entity.
     */
    public String getTypeCode(){
        return this.typeCode;
    }

    /**
     * Sets  Type Code  of this Financial Entity that belongs to Financial Entity Type.
     *
     * @param newTypeCode The TypeCode of Financial Entity that is used in adding/modifying of FE.
     */
    public void setTypeCode( String newTypeCode ){
        this.typeCode = newTypeCode;
    }

    /**
     * Gets Share ownership  of Financial Entity
     *
     * @return Share Ownership of this Financial Entity.
     */
    public String getShareOwnership(){
        return this.shareOwnership;
    }

    /**
     * Sets Share ownership of this Financial Entity.
     *
     * @param newShareOwnership Share ownership of this financial entity.
     */
    public void setShareOwnership( String newShareOwnership ){
        this.shareOwnership = newShareOwnership;
    }

    /**
     * Gets Status of Financial Entity
     *
     * @return Status of Financial Entity.
     */
    public String getStatus(){
        return this.status;
    }

    /**
     * Sets stauts of this Financial Entity.
     *
     * @param newStatus Status of Financial Entity.
     */
    public void setStatus( String newStatus ){
        this.status = newStatus;
    }

    /**
     * Gets Sequence Number of Financial Entity
     *
     * @return Sequence Number of this Financial Entity.
     */
    public String getSequenceNum(){
        return this.sequenceNum;
    }

    /**
     * Sets Sequence Number of this Financial Entity.
     *
     * @return newSequenceNum The Sequence Number of this Financial Entity
     *  that i sused in add/modifying it.
     */
    public void setSequenceNum( String newSequenceNum ){
        this.sequenceNum = newSequenceNum;
    }

    /**
     * Gets Organization Relation Type with this Financial Entity.
     *
     * @return Organization Relation type with this Financial Entity.
     */
    public String getOrgRelationType(){
        return this.orgRelationType;
    }

    /**
     * Sets Organization Relation type with this Financial Entity.
     *
     * @param newOrgRelationType Organization Relation type of Financial Entity
     */
    public void setOrgRelationType( String newOrgRelationType ){
        this.orgRelationType = newOrgRelationType;
    }

    /**
     * Gets Person Relation Type with this Financial Entity.
     *
     * @return Person Relation with this Financial Entity.
     */
    public String getPersonRelationType(){
        return this.personRelationType;
    }

    /**
     * Sets Person Relation type with this Financial Entity.
     *
     * @param newPersonRelationType Person Relation Type of Financial Entity.
     */
    public void setPersonRelationType( String newPersonRelationType ){
        this.personRelationType = newPersonRelationType;
    }

    /**
     * Gets Person Relation Type code with this Financial Entity.
     *
     * @return Person Relation Type Code with this Financial Entity.
     */
    public String getPersonRelationTypeCode(){
        return this.personRelationTypeCode;
    }

    /**
     * Sets Person Relation type code with this Financial Entity.
     *
     * @param newPersonRelationTypeCode Person relation type code to Financial entity
     */
    public void setPersonRelationTypeCode( String newPersonRelationTypeCode ){
        this.personRelationTypeCode = newPersonRelationTypeCode;
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
    }

    /**
     * Validate the properties that have been set for this request,
     * and return an ActionErrors object that encapsulates any validation errors
     * that have been found.
     * If no errors are found, return null or an ActionErrors object with no recorded
     * error messages
     * <br><b> Validations </b>
	 * <li> All Answers are required for All Questions
	 * <li> Financial Entity name is required
	 * <li> Financial Entity Type Code is required
	 * <li> Financial Entity Status is required
	 * <li> Financial Entity Person Relation type code is required
	 * <li> Financial Entity Sponsor code or Sponsor name is required
	 * <li> Financial Entity Organization Relation Type code is required
	 * <li> Financial Entity Description is required if status code is 2.
	 *
     * @param actionMapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public ActionErrors validate( ActionMapping actionMapping , HttpServletRequest request ){

        System.out.println("Inside FinancialEntityDetailsActionForm.validate()");
        ActionErrors actionErrors = new ActionErrors();
        
        /* CASE #1374 Begin */
        System.out.println("actionMapping: "+actionMapping.getPath());
        if( actionMapping.getPath().equals("/deactivateFinEnt2")){
            //Explanation for inactive status of fin ent is required field.
            if(description == null || description.trim().length() < 1){
                actionErrors.add(ActionErrors.GLOBAL_ERROR ,
                new ActionError( 
                    "error.finEntityDetailsForm.personRelationType.required" ) );
            }
            else if ( 2000 < description.length() )  {
                actionErrors.add( ActionErrors.GLOBAL_ERROR ,
                new ActionError( "error.FinancialEntityDetails.description.toolong" ) );               
            }
            return actionErrors;
        }
        /* CASE #1374 End */
        
        //validate question related information
        String[] arrQuestionIDs = request.getParameterValues( "hdnQuestionID" );
        String[] arrQuestionDescs = request.getParameterValues( "hdnQuestionDesc" );
        String[] arrQSeqNumbers = request.getParameterValues( "hdnQSeqNum" );
        String explanation = request.getParameter( "txtExplanation" );
        /* CASE #736 Comment Begin */
        //String[] arrAnswers= new String[arrQuestionIDs.length];
        /* CASE #736 Comment End */
        /* CASE #736 Begin */
        /* If arrQuestionIDs, then session has timed out.  Return control to action
        class, which will forward to session expired page. */
        String[] arrAnswers = null;
        if(arrQuestionIDs != null){
            arrAnswers= new String[arrQuestionIDs.length];
        }
        else{
            return actionErrors;
        }
        /* CASE #736 End */
        boolean isAnswerAvailable=true;

        if( arrQuestionIDs != null ) {
            for( int questionIndex= 0 ; questionIndex < arrQuestionIDs.length ; questionIndex ++ ) {
                String answer = request.getParameter( arrQuestionIDs[ questionIndex] );
                //if answer is not available then throw error.
                if( answer == null ) {
                    // set the flag to false
                    isAnswerAvailable=false;
                }
                //collect all errors
                arrAnswers[questionIndex]=answer;
                request.setAttribute("selectedQuestions",arrQuestionIDs);
                request.setAttribute("selectedAnswers",arrAnswers);
            }
        }
        if( ! isAnswerAvailable){
            actionErrors.add( ActionErrors.GLOBAL_ERROR ,
            new ActionError( "error.finEntityDetailsForm.answers.required" ) );
            //return actionErrors;
        }


        // Ensure Name is required
        if( ( name == null ) || ( name.trim().length() == 0 ) ) {

            actionErrors.add( ActionErrors.GLOBAL_ERROR ,
            new ActionError( "error.finEntityDetailsForm.name.required" ) );

            //return actionErrors;
        }
        //Ensure TypeCode is required
        if( ( typeCode == null ) || ( 0 == typeCode.trim().length()) ) {

            actionErrors.add( ActionErrors.GLOBAL_ERROR ,
            new ActionError( "error.finEntityDetailsForm.type.required" ) );

            //return actionErrors;
        }
        /* CASE #1374 Comment Begin*/
        //status removed from add/edit page
        //Ensure Status is required
        /*if( ( status == null ) || ( 0 == status.trim().length() ) ) {
            actionErrors.add( ActionErrors.GLOBAL_ERROR ,
            new ActionError( "error.finEntityDetailsForm.status.required" ) );
            return actionErrors;
        }*/
        /* CASE #1374 Comment End */
        //Ensure PersonRelation Type to this Entity is required
        if( ( personRelationTypeCode == null )
        || ( 0 == personRelationTypeCode.trim().length() ) ) {

            actionErrors.add( ActionErrors.GLOBAL_ERROR ,
            new ActionError( "error.finEntityDetailsForm.personRelationType.required" ) );

            //return actionErrors;
        }
        /* CASE #1374 Comment Begin*/
        //inactive status explanation removed from add/edit page        
        //If Entity Status is 2(inactive)  then Entity Description is required.
        /*if( status != null ) {
            if( ( status.equals( "2" ) )
            && ( ( description == null ) || ( 0 == description.trim().length() ) ) ) {

                actionErrors.add( ActionErrors.GLOBAL_ERROR ,
                new ActionError( "error.finEntityDetailsForm.desc.required" ) );

                return actionErrors;
            }
        }*/
        /* CASE #1374 Comment End */
        // If Organization relation is Y(Related)  then Organization Relation Description is required.
       /*For this release, don't require description or sponsor for user selection
          that organization has a relationship to this entity. */
       /* if( orgRelationType != null ) {
            if( orgRelationType.equals( "Y" ) ){
                if ( ( orgRelationDesc == null ) ||
                        ( 0 == orgRelationDesc.trim().length()) )  {
                    actionErrors.add( ActionErrors.GLOBAL_ERROR ,
                        new ActionError( "error.finEntityDetailsForm.orgRelationDesc.required" ) );
                    return actionErrors;
                }
                // Sponsor Id and name both should not be null , either of two is required.
                if( ( sponsorId == null ) || ( sponsorId.trim().length() == 0 ) ) {
                    actionErrors.add( ActionErrors.GLOBAL_ERROR ,
                        new ActionError( "error.finEntityDetailsForm.sponsor.required" ) );
                    this.sponsorName = "";
                    return actionErrors;
                }
            }
        }*/

        if ( ( name != null ) && (60 < name.length()) ){
            actionErrors.add( ActionErrors.GLOBAL_ERROR ,
            new ActionError( "error.FinancialEntityDetails.name.toolong" ) );
            //return actionErrors;
        }
        /* CASE #1216 Change  200 character limit to 2000 character limit for description 
         fields .*/
        if ( (description != null ) && (2000 < description.length() ) ) {
            actionErrors.add( ActionErrors.GLOBAL_ERROR ,
            new ActionError( "error.FinancialEntityDetails.description.toolong" ) );
            //return actionErrors;
        }
        if ( (personRelationDesc != null) && (2000 < personRelationDesc.length()) )   {
            actionErrors.add( ActionErrors.GLOBAL_ERROR ,
            new ActionError( "error.FinancialEntityDetails.personRelationDescription.toolong" ) );
            //return actionErrors;
        }
        if ( (orgRelationDesc != null) && (2000 < orgRelationDesc.length()) ) {
            actionErrors.add( ActionErrors.GLOBAL_ERROR ,
            new ActionError( "error.FinancialEntityDetails.organizationsRelationDescription.toolong" ) );
           // return actionErrors;
        }
        /* CASE 1374 Comment Begin */
        //Not asking for sponsor info
        /*if ( (sponsorId != null) && (6 < sponsorId.length()) ) {
            actionErrors.add( ActionErrors.GLOBAL_ERROR ,
            new ActionError( "error.FinancialEntityDetails.sponsorId.toolong" ) );
           // return actionErrors;
        }*/
        
        /* CASE #1374 Begin */
        if(!actionErrors.isEmpty()){
            request.setAttribute("actionFrom", actionFrom);
        }
        /* CASE #1374 End */

        return actionErrors;
    }//end of validate Method
}