/*
 * @(#)AddFinEntityAction.java	1.0 06/08/2002 14:59:29
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.action.coi;

import java.io.InputStream;
import java.util.Properties;

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
import java.text.DecimalFormat;

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
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.action.common.CoeusActionBase;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;

/**
 * <code>AddFinEntityAction</code> is a struts implemented Action class
 * to set the default values in form bean <code>FinancialEntityDetailsActionForm</code>
 * to create a new Financial Entity.
 * <br><b> Defaults to create a new FinancialEntity </b>
 * <li>ActionType = "I"
 * <li>Sequence Number = "1"
 * <li>Organization Relation Type = "X" (Unknown)
 *
 * @author RaYaKu
 * @version 1.0 June 03, 2002
 */
public class AddFinEntityAction extends CoeusActionBase{

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an ActionForward instance describing where and how control
     * should be forwarded, or null if the response has already been completed.
     *
     * <br> Method will take the necessary default information that required for
     * adding a new Financial Entity, from the <code>coeus</code> database, and
     * attach  details to the <code>FinancialEntityDetailsActionForm</code>
     * class.
     *
     * @param actionMapping The ActionMapping used to select this instance
     * @actionForm The optional ActionForm bean for this request (if any)
     * @request The HTTP request we are processing
     * @response The HTTP response we are creating
     *
     * @throws java.io.IOException if an input/output error occurs
     * @throws javax.servlet.ServletException if a servlet exception occurs.
     */
    public ActionForward perform( ActionMapping actionMapping ,
            ActionForm actionForm , HttpServletRequest request ,
            HttpServletResponse response ) throws IOException , ServletException{

        String personId = null;
        String personFullName = null;
        String userName = null;
        PersonInfoBean personInfoBean = null;
        HttpSession session = null;
        ActionForward actionforward = actionMapping.findForward( SUCCESS );
//        UtilFactory UtilFactory = new UtilFactory();
        boolean errorFlag = false;
        String loggedinpersonid = null;
        String userprivilege = null;
        /* Read coeus.properties to get default values for new financial entity. */
//        InputStream is = getClass().getResourceAsStream("/coeus.properties");
//        Properties properties = new Properties();
//        try {
//            properties.load(is);
//        }catch (Exception e) {
//            System.err.println("Can't read the properties file. " +
//            "Make sure coeus.properties is in the CLASSPATH");
//            return null;
//        }
        String defaultTypeCode = CoeusProperties.getProperty(CoeusPropertyKeys.DEFAULT_TYPE_CODE);
        String defaultActionType = CoeusProperties.getProperty(CoeusPropertyKeys.DEFAULT_ACTION_TYPE);
        String defaultSeqNum = CoeusProperties.getProperty(CoeusPropertyKeys.DEFAULT_SEQ_NUM);
        String defaultOrgRelType = CoeusProperties.getProperty(CoeusPropertyKeys.DEFAULT_ORG_REL_TYPE);
        String defaultType = CoeusProperties.getProperty(CoeusPropertyKeys.DEFAULT_TYPE);
        try {
            session = request.getSession( true );
            //look username attribute in session scope
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
            personInfoBean = ( PersonInfoBean ) session.getAttribute( PERSONINFO);
            if( personInfoBean != null ) {
                personId = personInfoBean.getPersonID();
                personFullName = personInfoBean.getFullName();
            }

            /* CASE #212 BEGIN
             * Check whether loggedinpersonid is the same as personId from PersonInfoBean.
             * If yes, create new financial entity this id.  Else, check
             * whether user has Maintain COI right.  If yes, create new FE with
             * personId from PersonInfoBean.  Else, create new FE with loggedinpersonid.
             */
             loggedinpersonid = (String)session.getAttribute(LOGGEDINPERSONID);
             userprivilege = (String)session.getAttribute("userprivilege");
             if(personId != null && loggedinpersonid != null){
                if(!loggedinpersonid.equals(personId)){
                    if(!userprivilege.equals("2")){
                        personId = loggedinpersonid;
                        personFullName = (String)session.getAttribute(LOGGEDINPERSONNAME);
                    }
                }
             }
            /* CASE #212 END */
            DecimalFormat decimalFormat = new DecimalFormat( "0000000000" );
            FinancialEntityDetailsBean financialEntityDetailsBean =
                    new FinancialEntityDetailsBean( "Coeus" , "Coeus" , personId.trim() );
            String lastUpdate = financialEntityDetailsBean.getTimestamp().toString();
            String entityNum = decimalFormat.format
                ( Long.parseLong( financialEntityDetailsBean.getNextSeqNum() ) );
            // Where is the request coming from?
            String actionFrom = request.getParameter( "actionFrom" );

            /*  Populate the EditFinancialEntityDetails form    */
            FinancialEntityDetailsActionForm financialEntityDetailsActionForm =
                    ( FinancialEntityDetailsActionForm ) actionForm;

            financialEntityDetailsActionForm.setActionFrom( actionFrom );

            /* set the organization type for financial entity, for example "For-Profit". */
            financialEntityDetailsActionForm.setTypeCode(defaultTypeCode);

            financialEntityDetailsActionForm.setActionType( defaultActionType );

            /* set the Number of Financial Entity */
            financialEntityDetailsActionForm.setNumber( entityNum.trim() );

            /* set the user name who will modify or view this Financial Entity Details */
            financialEntityDetailsActionForm.setUserName( userName );

            /* set the user full name who will modify or view this Financial Entity Details */
            financialEntityDetailsActionForm.setUserFullName( personFullName );

            /*set the Last Updated date of this Financial entity in both String */
            financialEntityDetailsActionForm.setLastUpdate( lastUpdate );

            /*set the Sequence Number of this Financial Entity. */
            financialEntityDetailsActionForm.setSequenceNum( defaultSeqNum );

            /*set the Organziation relation type with this entity as not known*/
            financialEntityDetailsActionForm.setOrgRelationType( defaultOrgRelType );

            Vector collFinEntityCertDetails =
                financialEntityDetailsBean.getAllCertDet(defaultType );

            //Get all Organization types
            LinkedList collOrgTypes = financialEntityDetailsBean.getAllOrgTypes();

            // Get all Financial Entities' Status
            LinkedList collEntStatus = financialEntityDetailsBean.getAllEntStatus();

            //Get all Financial Entities Relations
            LinkedList collRelations = financialEntityDetailsBean.getAllRelations();

            /*
             * Bind all data to session object with attribute names,which would
             * be forwarded a jsp  where these are shown to user
             */
            session.setAttribute( "collEntityCertDetails" , collFinEntityCertDetails );
            session.setAttribute( "collOrgTypes" , collOrgTypes );
            session.setAttribute( "collEntityStatus" , collEntStatus );
            session.setAttribute( "collRelations" , collRelations );
            /* CASE #1374 Put actionFrom attribute in request */
            request.setAttribute("actionFrom", actionFrom);

        } catch( CoeusException coeusEx ) {
            errorFlag = true;
            UtilFactory.log( coeusEx.getMessage() , coeusEx , "AddFinEntityAction" ,
                    "perform()" );
            request.setAttribute( "EXCEPTION" , coeusEx );
        } catch( DBException dbEx ) {
            errorFlag = true;
            /* CASE #735 Comment Begin */
            //DBEngine prints the exception to log file.
            //UtilFactory.log( dbEx.getMessage() , dbEx , "AddFinEntityAction" ,
                    //"perform()" );
            /* CASE #735 Comment End */
            request.setAttribute( "EXCEPTION" , dbEx );
        } catch( Exception ex ) {
            ex.printStackTrace();
            errorFlag = true;
            UtilFactory.log( ex.getMessage() , ex , "AddFinEntityAction" , "perform()" );
            request.setAttribute( "EXCEPTION" , new CoeusException
              ( "exceptionCode.80001" ) );
        }
        //forward the output to failure/success page depends on errorFlag state
        if( errorFlag ) {
            actionforward = actionMapping.findForward( FAILURE );
        } else {
            /* CASE #665 Begin */
            //Put token in session, to be checked
            //before form is processed, to avoid duplicate submissions.
            saveToken(request);
            /* CASE #665 End */
            actionforward = actionMapping.findForward( SUCCESS );
        }
        return actionforward;
    }
}
