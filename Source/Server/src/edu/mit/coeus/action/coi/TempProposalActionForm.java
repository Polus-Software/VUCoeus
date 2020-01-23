/*
 * @(#)TempProposalActionForm.java 1.0 06/03/2002 16:30:23.
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.action.coi;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.UtilFactory;
/* CASE #734 Comment Begin */
//import edu.mit.coeus.coi.exception.CoeusException;
/* CASE #734 Comment End */
/* CASE #734 Begin */
import edu.mit.coeus.exception.CoeusException;
/* CASE #734 End */
import edu.mit.coeus.coi.bean.DisclosureValidationBean;
import edu.mit.coeus.action.common.CoeusActionBase;

/**
 * <code>TempProposalActionForm</code> is a struts implemented form bean to hold
 * information that used in creating a new Temporary Proposal in <code>TempProposalAction
 * </code> component.
 *
 * @version 1.0  June 10,2002 16:30:23
 * @author  RaYaKu
 */

public class TempProposalActionForm extends ActionForm{

    /*
     * Value of Temporary Proposal Type Code
     */
    private String typeCode;

    /*
     * value of Temporary Proposal Title that user wish to create
     */
    private String title;

    /*
     * Value of  Lead Unit on which the Temporary Proposal is going to be created.
     */
    private String leadUnit;

    /**
     * Value of lead unit name for this propopal.
     */
    private String leadUnitName;

    /*
     * Value of Sponsor Name on which this proposal is going to be created
     */
    private String sponsorName;

    /*
     * Value of Sponsor ID of this proposal
     */
    private String sponsorId;

    /*
     * Comments on this proposal
     */
    private String comments;

    /*
     * Value of Primary Investigater ID
     */
    private String primInvestigaterId;

    /*
     * Value of Primary Investigaer Name.
     */
    private String primInvestigaterName;

    /*
     * Value of Log Status
     */
    private String logStatus;


    /*
     * identify search refresh
     */
    private String searchRefresh;

    /**
     * Gets the Primary Investigater Id of this temporary proposal
	 *
	 * @return Primary Investigater ID
     */
    public String getPrimInvestigaterId(){
        return this.primInvestigaterId;
    }

    /**
     * sets the Primary Investigater Id of this temporary proposal
	 *
	 * @param newPrimInvestigaterId  Primary Investigater ID who creates this Temporary Proposal.
     */
    public void setPrimInvestigaterId( String newPrimInvestigaterId ){
        this.primInvestigaterId = newPrimInvestigaterId;
    }

    /**
     * Gets the Primary Investigater Name of this temporary proposal
	 *
	 * @return Name of the Primary Investigater
     */
    public String getPrimInvestigaterName(){
        return this.primInvestigaterName;
    }

    /**
     * Sets the Primary Investigater Name of this temporary proposal
	 *
	 * @return newPrimInvestigaterName Primary Investigater Name on which this Temporary Proposal will be created.
     */
    public void setPrimInvestigaterName( String newPrimInvestigaterName ){
        this.primInvestigaterName = newPrimInvestigaterName;
    }

    /**
     * Gets the Log Status of this temporary proposal
	 *
	 * @return Log Status of Temporary Disclosure.
     */
    public String getLogStatus(){
        return this.logStatus;
    }

    /**
     * sets the Log Status of this temporary proposal
	 *
	 * @param newLogStatus Log Status of Temporary Disclosure(Ex: default is T ).
     */
    public void setLogStatus( String newLogStatus ){
        this.logStatus = newLogStatus;
    }

    /**
     * Gets the Type Code
	 *
	 * @return Type code of proposal that user selected from proposals
     */
    public String getTypeCode(){
        return this.typeCode;
    }

    /**
     * Gets the Title of this temporary proposal
	 *
	 * @return Title information of Temporary Proposal
     */
    public String getTitle(){
        return this.title;
    }

    /**
     * Gets the Lead Unit.
	 *
	 * @return Lead unit information
     */
    public String getLeadUnit(){
        return this.leadUnit;
    }

    /**
     * Set value of Lead Unit Name
     * @return Name of Lead Unit for this proposal.
     */
    public String getLeadUnitName(){
        return this.leadUnitName;
    }

    /**
     * Gets the Sponsor Name of this temporary proposal
	 *
	 * @return  Name of the Sponsor.
     */
    public String getSponsorName(){
        return this.sponsorName;
    }

    /**
     * Gets the Sponsor Id/Code of this temporary proposal
	 *
	 * @return Code/ID of Sponsor on which this Temporary Proposal will be created.
     */
    public String getSponsorId(){
        return this.sponsorId;
    }


    /**
     * Gets the searchRefresh value of this temporary proposal
	 *
     */
    public String getSearchRefresh(){
        return this.searchRefresh;
    }

    /**
     * Gets the Comments of this temporary proposal
	 *
	 * @return Comments of Temporary Proposal
     */
    public String getComments(){
        return this.comments;
    }

    /**
     * Sets the Type Code
	 *
	 * @param newTypeCode The Code of user selected Proposal Type
     */
    public void setTypeCode( String newTypeCode ){
        this.typeCode = newTypeCode;
    }

    /**
     * Sets the Title of this temporary proposal
	 *
	 * @param newTitle Title of Temporary Proposal
     */
    public void setTitle( String newTitle ){
        this.title = newTitle;
    }

    /**
     * Sets the Lead Unit.
	 *
	 * @param newLeadUnit Number of Lead unit of this Temporary proposal that user wishes
     */
    public void setLeadUnit( String newLeadUnit ){
        this.leadUnit = newLeadUnit;
    }

    /**
     * Set value of Lead Unit Name.
     * @param newLeadUnitName
     */
    public void setLeadUnitName( String newLeadUnitName ){
        this.leadUnitName = newLeadUnitName;
    }

    /**
     * Sets the Sponsor Name of this temporary proposal
	 *
	 * @param newSponsorName Name of the Sponsor that user entered to created Temporary proposal
     */
    public void setSponsorName( String newSponsorName ){
        this.sponsorName = newSponsorName;
    }

    /**
     * Gets the Sponsor Id/Code of this temporary proposal
	 *
	 * @param newSponsorId Id/Code of the Sponsor that user entered/provided to created Temporary proposal
     */
    public void setSponsorId( String newSponsorId ){
        //System.out.println("setting sponsor id");
        this.sponsorId = newSponsorId;
    }

    /**
     * Sets the searchRefresh of this temporary proposal
	 *
     */
    public void setSearchRefresh( String newSearchRefresh ){
        this.searchRefresh = newSearchRefresh;
    }

    /**
     * Sets the Comments of this temporary proposal
	 *
	 * @param newComments Comments of This New Temporary Proposal that user provided
     */
    public void setComments( String newComments ){
        this.comments = newComments;
    }

    /**
     * Reset all bean properties to their default state.
     * This method is called before the properties are repopulated by the
     * controller servlet.
     * The default implementation is nothing.
     *
     * @param actionMaping The actionMaping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset( ActionMapping actionMaping , HttpServletRequest request ){
    }

    /**
     * Validate the properties that have been set for this HTTP request, and
     * return an ActionErrors object that encapsulates any validation errors
     * that have been found. If no errors are found, return null or
     * an ActionErrors object with no recorded error messages.
	 * <br><b> Validations </b>
	 * <li> LeadUnit information is required.
	 * <li> Title information of Temporary Proposal is required.
     *
     * @param  actionMapping The actionMapping used to select this instance
     * @param  request The servlet request we are processing
     */
    public ActionErrors validate( ActionMapping actionMapping , HttpServletRequest request ){


        ActionErrors actionErrors = new ActionErrors();

        /* CASE #231 Begin */
        /* If action is to edit temp proposal information, and request is coming
        from Edit Disclosure page, skip the validation. */
        HttpSession session = request.getSession(true);
        String tempProposalAction  = (String)session.getAttribute("tempProposalAction");
        if((tempProposalAction != null) && (tempProposalAction.equals("edit"))){
          return actionErrors;
        }
        /*CASE #231 End */
        /* CASE #231 Begin */
        /* Do searchRefresh validation only if searchRefresh is not null. */
        /* CASE #231 End */
        if(searchRefresh != null){
          if( this.searchRefresh.equalsIgnoreCase("Y")) {
            this.searchRefresh = "N"; // don't display error message
            /* below error message will not be displayed - lead unit name is displayed
               after form submit - this error is added to redirect back to input page - considering
               a case where form has valid input and lead unit is searched at the end - we need to
               redirect back to input page and display lead unit name */
              actionErrors.add( "leadUnit Required" ,
              new ActionError( "error.tempProposalForm.leadUnit.required" ) );
            request.setAttribute("srchRefresh", "N");
          } else {
            this.searchRefresh = "D"; // display error message
            request.setAttribute("srchRefresh", "D");
          }
        }
//        UtilFactory UtilFactory = new UtilFactory();

        //System.out.println("********validate TempProposalActionForm********");
        // Ensure lead unit and title are required to create a temporary proposal
        /* CASE #1374 Comment Begin */
        //lead unit no longer required
        /*if( ( leadUnit == null ) || ( leadUnit.equals( "" ) ) ) {
            this.leadUnitName = null;
            actionErrors.add( "leadUnit Required" ,
            new ActionError( "error.tempProposalForm.leadUnit.required" ) );
        } */
            
        if( ( title == null ) || ( title.equals( "" ) ) ) {
            actionErrors.add( "Title Required" ,
            new ActionError( "error.tempProposalForm.title.required" ) );
        }
        if ( (title != null) && (150 < title.length()) ) {
            actionErrors.add( "Title too long" ,
            new ActionError( "error.tempProposalForm.title.toolong" ) );
        }
        /* CASE #1374 Comment Begin */
        //No longer asking user for lead unit or sponsor id.
        /*
        if ((leadUnit != null) && (8 < leadUnit.length()) ) {
            actionErrors.add( "Lead unit too long" ,
            new ActionError( "error.tempProposalForm.leadUnit.toolong" ) );
        }
        if ((sponsorId != null) && (6 < sponsorId.length()) ) {
            actionErrors.add( "sponsor too long" ,
            new ActionError( "error.tempProposalForm.sponsor.toolong" ) );
        }
         **/
        /* CASE #1374 Comment End */
        if ((comments != null) && (300 < comments.length()) ) {
            actionErrors.add( "comments too long" ,
            new ActionError( "error.tempProposalForm.comments.toolong" ) );
        }

        DisclosureValidationBean disclosureValidationBean =
                                              new DisclosureValidationBean();

        //System.out.println("********validate sponsor******** " + sponsorId);
        // validate the Sponsor Number and Name
        /* CASE #1374 Comment Begin */        
        /*String spName = null;
        if( ( ( sponsorId == null || sponsorId.trim().equals( "" ))
                && ( sponsorName == null || sponsorName.trim().equals( "" ) ) ) ) {

          //System.out.println("********sponsor not entered ******** ");
          actionErrors.add( "sponsorRequired" ,
          new ActionError( "error.tempPrposal.sponsor.required" ) );
        } else {
          //System.out.println("********sponsor exist ******** ");
          if(( sponsorId != null && !sponsorId.trim().equals( "" ))) {
            //System.out.println("********get sponsor name for ******** " + sponsorId);
            try{
              spName = disclosureValidationBean.getSponsorName(
                                  sponsorId.trim() , null );
              //System.out.println("********sponsor ******** " + spName);
              if( spName == null ) {
                this.sponsorName = null;
                actionErrors.add( "invalidSponsor" ,
                  new ActionError( "error.tempPrposal.invalidSponsor" ) );
              }else {
                this.sponsorName = spName;
              }
            /*} catch( CoeusException coeusEx ) {
              UtilFactory.log( coeusEx.getMessage() , coeusEx , "CreateTempProposalAction" ,
              "perform()" );
              request.setAttribute( "EXCEPTION" , coeusEx );
            } catch( DBException dbEx ) {
              UtilFactory.log( dbEx.getMessage() , dbEx , "CreateTempProposalAction" ,
              "perform()" );
              request.setAttribute( "EXCEPTION" , dbEx );
            }catch( Exception ex ) {
              ex.printStackTrace();
              UtilFactory.log( ex.getMessage() , ex , "CreateTempProposalAction" ,
              "perform()" );
              request.setAttribute( "EXCEPTION" , new CoeusException( "exceptionCode.60009" ) );
            }        
          }
        }

        String leadName = null;
        if( ( leadUnit != null ) && ( !leadUnit.equals( "" ) ) ) {
            try{
              leadName = disclosureValidationBean.getLeadUnitName(leadUnit.trim());
              //System.out.println("********lead unit  ******** " + leadName);
              if( leadName == null ) {
                    actionErrors.add( "invalidLead" ,
                        new ActionError( "error.tempPrposal.invalidLeadNumber" ) );
                    this.leadUnitName = null;
              }else {
                this.leadUnitName = leadName;
              }
              /* CASE #748 Comment Begin */
            /*} catch( CoeusException coeusEx ) {
              UtilFactory.log( coeusEx.getMessage() , coeusEx , "TempProposalActionForm" ,
              "perform()" );
              request.setAttribute( "EXCEPTION" , coeusEx );*/
              /* CASE #748 Comment End 
            } catch( DBException dbEx ) {
              UtilFactory.log( dbEx.getMessage() , dbEx , "TempProposalActionForm" ,
              "perform()" );
              request.setAttribute( "EXCEPTION" , dbEx );
            }catch( Exception ex ) {
              UtilFactory.log( ex.getMessage() , ex , "TempProposalActionForm" ,
              "perform()" );
              request.setAttribute( "EXCEPTION" , new CoeusException( "exceptionCode.60009" ) );
            }
        }*/
        /* CASE #1374 Comment End */
        /* CASE #1374 Begin */
        if ( sponsorName == null || sponsorName.trim().equals( "" ) ){
            actionErrors.add("sponsorRequired", 
                new ActionError( "error.tempPrposal.sponsor.required" ) );
        }
        else if( sponsorName.trim().equalsIgnoreCase( "NIH" ) || 
            sponsorName.trim().equalsIgnoreCase( "National Institute of Health" ) ){
                sponsorName = "NIH";
                sponsorId = "000340";
        }
        else if( sponsorName.trim().equalsIgnoreCase( "NSF" ) ||
            sponsorName.trim().equalsIgnoreCase( "National Science Foundation" ) ){
                sponsorName = "NSF";
                sponsorId = "000500";
        }
        else{
            sponsorId = "000001";
        }
        System.out.println("sponsorId: "+sponsorId);
        /* CASE #1374 End */
        return actionErrors;
    }//end of validate Method
}