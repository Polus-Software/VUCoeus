/*
* @(#)EditFinEntityAction.java	1.0	06/03/2002 10:57:23 PM
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
import java.util.LinkedList;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;

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
 * <code>EditFinEntityAction</code> is a struts implemented Action class
 * to show the details of a Financial Entity to user for modifying them.
 * Gets all Entity Details (Certificate Details,Entity Status
 * and Organization information) of this Entity to available for user to modify
 * by keeping in a form bean <code>FinancialEntityDetailsActionForm</code>.
 *
 * @version 1.0 June 03, 2002
 * @author RaYaKu
 */
public class EditFinEntityAction extends CoeusActionBase{

	/**
	 * Process the specified HTTP request, and create the corresponding HTTP response
	 * (or forward to another web component that will create it).
	 * Return an ActionForward instance describing where and how control should
	 * be forwarded, or null if the response has already been completed.
	 *
	 * <br> The method gets all the details of a Financial Entity.
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
		ActionForm actionForm ,
		HttpServletRequest request ,
		HttpServletResponse response ) throws IOException , ServletException{

		String personId = null;
		String personFullName = null;
		String userName = null;
		String defaultActionType = "U";
		PersonInfoBean personInfoBean = null;
		HttpSession session = null;
		ActionForward actionforward = actionMapping.findForward( SUCCESS );
//		UtilFactory UtilFactory = new UtilFactory();
		boolean errorFlag = false;
		try {
                    System.out.println("inside EditFinEntityAction.perform()");
                    session = request.getSession( true );
                    userName = ( String ) session.getAttribute( USERNAME );
                    /*
                     * If user name is not available in session scope then
                     * supply him a session expiration page
                     */
                    if( userName == null ) {
                            actionforward = actionMapping.findForward( EXPIRE );
                            return ( actionforward );
                    }

                    // look personInfo attribute in session scope
                    personInfoBean = ( PersonInfoBean ) session.getAttribute( PERSONINFO);
                    if( personInfoBean != null ) {
                            personId = personInfoBean.getPersonID();
                            personFullName = personInfoBean.getFullName();
                    }
                    // get the parameter values  from request that are to be processed
                    String entiyNum = request.getParameter( "entityNo" );
                    /* CASE #1221 Comment Begin */
                    //seqNum no longer passed in url
                    //String seqNum = request.getParameter( "seqNo" );
                    /* CASE #1221 Comment End */
                    // find out who is requesting the page
                    String actionFrom = request.getParameter( "actionFrom" );
                    // check the entity information and if null then throw exception
                    if( entiyNum == null ) {
                            throw new CoeusException( "Entity Number is Not Available " );
                    }
                    String updateUser = userName;
                    FinancialEntityDetailsBean financialEntityDetailsBean
                            = new FinancialEntityDetailsBean( "Coeus" , "Coeus" , personId.trim() );
                    String lastUpdate = financialEntityDetailsBean.getTimestamp().toString();

                    EntityDetailsBean entityDetailsBean
                                    = financialEntityDetailsBean.getFinancialEntityDetails(
                                                        entiyNum.trim() );

                    /* CASE #352 Begin */
                    //System.out.println("entityDetailsBean.getPersonId(): "+entityDetailsBean.getPersonId());
                    //System.out.println("personID: "+personId);
                    String loggedinpersonid =
                            (String)session.getAttribute(LOGGEDINPERSONID);                          
                    boolean hasRightToEdit = false;
                    String strUserprivilege = (String)session.getAttribute("userprivilege");
                    int userprivilege = Integer.parseInt(strUserprivilege);
                    if(userprivilege > 1){
                        hasRightToEdit = true;
                    }
                    else if(entityDetailsBean.getPersonId().equals(loggedinpersonid)){
                        hasRightToEdit = true;
                    }
                    if(!hasRightToEdit){
                        String errorMsg = "You do not have rights to edit this ";
                        errorMsg += "financial entity.  If you believe you are ";
                        errorMsg += "seeing this message in error, please contact ";
                        errorMsg += "the Office of Sponsored Programs.";
                        throw new CoeusException(errorMsg);
                    }
                    /* CASE #352 End */
                    /* CASE #1221 Comment Begin */
                    // Get all Financial Entity Certification Details
                    /*Vector collFinEntityCertDetails
                                    = financialEntityDetailsBean.getFECertificateDetails(
                                            entiyNum.trim() , seqNum.trim() );*/
                   /* CASE #1221 Comment End */
                    /* CASE #1221 Begin */
                    Vector collFinEntityCertDetails
                                = financialEntityDetailsBean.getFECertificateDetails(
                                        entiyNum.trim(), entityDetailsBean.getSeqNumber());                    
                    /* CASE #1221 End */

                    //Get all Organization types
                    LinkedList collOrgTypes = financialEntityDetailsBean.getAllOrgTypes();

                    // Get all Financial Entities' Status
                    LinkedList collEntStats = financialEntityDetailsBean.getAllEntStatus();

                    //Get all Financial Entities Relations
                    LinkedList collRelations = financialEntityDetailsBean.getAllRelations();

                    /*  Populate the EditFinancialEntityDetails form    */
                    FinancialEntityDetailsActionForm financialEntityDetailsForm
                                    = ( FinancialEntityDetailsActionForm ) actionForm;
                    /* Strore the information in form about page from which request is generated*/
                    financialEntityDetailsForm.setActionFrom( actionFrom );

                    /* set the user name who will modify or looking this Financial Entity Details */
                    financialEntityDetailsForm.setActionType(defaultActionType );

                    /* set the Number of Financial Entity */
                    financialEntityDetailsForm.setNumber( entiyNum.trim() );

                    /* set the user name who will modify or looking this Financial Entity Details */
                    financialEntityDetailsForm.setUserName( userName );

                    /* set the user full name who will modify or looking this Financial Entity Details */
                    financialEntityDetailsForm.setUserFullName( personFullName );

                    /* set the Financial Entity Name */
                    financialEntityDetailsForm.setName( entityDetailsBean.getName() );

                    /* set the Financial Entity Type*/
                    financialEntityDetailsForm.setType( entityDetailsBean.getType() );

                    /* set the Financial Entity TypeCode*/
                    financialEntityDetailsForm.setTypeCode( entityDetailsBean.getTypeCode() );

                    /* set the Share owner ship  */
                    financialEntityDetailsForm.setShareOwnership( entityDetailsBean.getShareOwnship() );

                    /* set the Entity status */
                    financialEntityDetailsForm.setStatus( entityDetailsBean.getStatus() );
                    /* set the Entity status code */
                    financialEntityDetailsForm.setStatus( entityDetailsBean.getStatusCode() );

                    /* set the Entity Description */
                    financialEntityDetailsForm.setDescription( entityDetailsBean.getEntityDescription() );

                    /*set the Person Relation Type to this Entity. */
                    financialEntityDetailsForm.setPersonRelationType( entityDetailsBean.getPersonReType() );
                    /* set the Person Relation TypeCode to this entity */
                    financialEntityDetailsForm.setPersonRelationTypeCode( entityDetailsBean.getPersonReTypeCode() );

                    /*set the Person Relation Description to this Entity. */
                    financialEntityDetailsForm.setPersonRelationDesc( entityDetailsBean.getPersonReDesc() );

                    /* set Organization relation type to this entity. */
                    financialEntityDetailsForm.setOrgRelationType( entityDetailsBean.getOrgRelationship() );

                    /*  set the Organization relation description with entity. */
                    financialEntityDetailsForm.setOrgRelationDesc( entityDetailsBean.getOrgDescription() );

                    /*  set the sponsor Id  */
                    financialEntityDetailsForm.setSponsorId( entityDetailsBean.getSponsor() );

                    /*  set the sponsor name  */
                    financialEntityDetailsForm.setSponsorName( entityDetailsBean.getSponsorName() );

                    /*set the Last Updated date of this Financial entity in both String and Timestamp formats */
                    DateFormat dateFormat = DateFormat.getDateTimeInstance();//Format date
                    System.out.println("entityDetailsBean.getLastUPdate(): "+entityDetailsBean.getLastUpdate());
                    financialEntityDetailsForm.setLastUpdateTimestamp( entityDetailsBean.getLastUpdate().toString() );
                    financialEntityDetailsForm.setLastUpdate( dateFormat.format( entityDetailsBean.getLastUpdate() ) );

                    /* set the Last updated user name */
                    financialEntityDetailsForm.setLastUpdatedUser( entityDetailsBean.getUpdateUser() );

                    /*set the Sequence Number of this Financial Entity. */
                    financialEntityDetailsForm.setSequenceNum( entityDetailsBean.getSeqNumber() );

                    /*
                     *  STRUTS 1.0.2 DISADVANTAGE of validating the form at server side:
                     * if any object is bound to request scope and forwarded to jsp
                     * which has got form, this object  will not be available in request scope
                     * when user visits jsp page after  form validation errors occur.
                     * Unless this object is bound to session scope, the problem can't be avoided.
                     *
                     * In Struts 1.1 is expected to have a remedy.
                     */

                    /*
                     * Bind all data to session object with attribute names,which would
                     * be forwarded to a jsp where these are shown to user
                     */
                    session.setAttribute( "collEntityCertDetails" , collFinEntityCertDetails );
                    session.setAttribute( "collOrgTypes" , collOrgTypes );
                    session.setAttribute( "collEntityStatus" , collEntStats );
                    session.setAttribute( "collRelations" , collRelations );
                    /* CASE #1374 Begin */
                    request.setAttribute( "actionFrom" , actionFrom );
                    session.setAttribute( "originalEntityDetails", entityDetailsBean);
                    session.setAttribute( "originalEntityCertDetails", 
                                                collFinEntityCertDetails);
                    /* CASE #1374 End */
                    /* CASE #665 Begin */
                    //Associate token with add/edit fin entity form, to be checked
                    //before form is processed, to avoid duplicate submissions.
                    saveToken(request);
                    /* CASE #665 End */

		} catch( CoeusException coeusEx ) {
			errorFlag = true;
			UtilFactory.log( coeusEx.getMessage() , coeusEx , "EditFinEntityAction" ,
								"perform()" );
			request.setAttribute( "EXCEPTION" , coeusEx );
		} catch( DBException dbEx ) {
			errorFlag = true;
                        /*CASE #735 Comment Begin */
                        //DBEngine will print exception to log file.
			//UtilFactory.log( dbEx.getMessage() , dbEx , "EditFinEntityAction" ,
								//"perform()" );
                        /* CASE #735 Comment End */
			request.setAttribute( "EXCEPTION" , dbEx );
		}catch( Exception ex ) {
			errorFlag = true;
			UtilFactory.log( ex.getMessage() , ex , "EditFinEntityAction" ,
								"perform()" );
			request.setAttribute( "EXCEPTION" , new CoeusException( "exceptionCode.80003" ) );
		}
		//forward the output to failure/success page depends on errorFlag state
		if( errorFlag ) {
			actionforward = actionMapping.findForward( FAILURE );
		} else {
			actionforward = actionMapping.findForward( SUCCESS );
		}
		return actionforward;
	}// end of perform
}