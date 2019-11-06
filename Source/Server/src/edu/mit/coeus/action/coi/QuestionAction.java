/*
 * @(#)QuestionAction.java 1.0 05/21/2002 17:49:43
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
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
import java.io.IOException;
import java.sql.SQLException;

import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.UtilFactory;
/* CASE #734 Comment Begin */
//import edu.mit.coeus.coi.exception.CoeusException;
/* CASE #734 Comment End */
/* CASE #734 Begin */
import edu.mit.coeus.exception.CoeusException;
/* CASE #734 End */
import edu.mit.coeus.coi.bean.CertQuestionDetailsBean;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.coi.bean.QuestionDetailsBean;
import edu.mit.coeus.action.common.CoeusActionBase;

/**
 * <code>QuestionAction</code> is a struts implemented Action class
 * to view details of a question that user wishes.
 *
 * @version May 21,2002 17:49:43
 * @author RaYaKu
 */
public class QuestionAction extends CoeusActionBase{

    /* Default value of question related information if not found */
    private static final String NO_EXPLANATION = "No Explanation";

    /**
     * Process the specified HTTP request, and create the corresponding HTTP response
     * (or forward to another web component that will create it). Return an ActionForward instance
     * describing where and how control should be forwarded, or null if the response
     * has already been completed.
     *
     * <br>The method used to get the details of a particular question by using
     * <code>getQuestionDetails</code> method of <code>CertQuestionDetailsBean</code>
     * class.
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
            HttpServletResponse response ) throws IOException , ServletException{

        String personId = null;
        String personName = null;
        HttpSession session = request.getSession();
        String userName = null;
        ActionForward actionforward = actionMapping.findForward( SUCCESS );
//        UtilFactory UtilFactory = new UtilFactory();
        boolean errorFlag = false;
        PersonInfoBean personInfoBean = null;
        String questionID = null;
        String questionDesc = NO_EXPLANATION;
        String questionExplanation = NO_EXPLANATION;
        String questionPolicy = NO_EXPLANATION;
        String questionRegulation = NO_EXPLANATION;
        try {
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
            personInfoBean = ( PersonInfoBean ) session.getAttribute( "personInfo" );
            if( personInfoBean != null ) {
                personId = personInfoBean.getPersonID();
                personName = personInfoBean.getFullName();
            }

            String questionNum = request.getParameter( "questionNo" );
            //get All questions and their corresponding details of user.
            CertQuestionDetailsBean certQuestionDetailsBean = new CertQuestionDetailsBean( personId.trim() );
            // get all question details of  question
            Vector collQuestionDetails = certQuestionDetailsBean.getQuestionDetails( questionNum.trim() );

            /*
             * Iterate collQuestionDetails which contains one question information
             * so collect all information of a single question .
             */
            QuestionDetailsBean questionInfo = new QuestionDetailsBean();
            for( int questionIndex = 0 ; ( collQuestionDetails != null && questionIndex < collQuestionDetails.size() ) ; questionIndex++ ) {

                QuestionDetailsBean questionDetailsBean
                = ( QuestionDetailsBean ) collQuestionDetails.elementAt( questionIndex );

                questionDesc = questionDetailsBean.getDescription();
                questionID = questionDetailsBean.getQuestionID();
                String explanationType = questionDetailsBean.getExplanationType();
                // get Question policy
                if( explanationType != null && explanationType.trim().equalsIgnoreCase( "P" ) ) {
                    questionPolicy = questionDetailsBean.getExplanation();
                }
                //get Question Regulation
                if( explanationType != null && explanationType.trim().equalsIgnoreCase( "R" ) ) {
                    questionRegulation = questionDetailsBean.getExplanation();
                }
                //get Question Explanation
                if( explanationType != null && explanationType.equalsIgnoreCase( "E" ) ) {
                    questionExplanation = questionDetailsBean.getExplanation();
                }
            }
            // add collected question information to one QuestionDetailsBean
            questionInfo.setDescription( questionDesc );
            questionInfo.setQuestionID( questionID );
            questionInfo.setPolicy( questionPolicy );
            questionInfo.setRegulation( questionRegulation );
            questionInfo.setExplanation( questionExplanation );

            request.setAttribute( "questionDetails" , questionInfo );
        } catch( CoeusException coeusEx ) {
            errorFlag = true;
            UtilFactory.log( coeusEx.getMessage() , coeusEx , "QuestionAction" ,
                    "perform()" );
            request.setAttribute( "EXCEPTION" , coeusEx );
        } catch( DBException dbEx ) {
            errorFlag = true;
            /* CASE #735 Comment Begin */
            //DBEngine prints the exception to log file.
            //UtilFactory.log( dbEx.getMessage() , dbEx , "QuestionAction" ,
                   // "perform()" );
            /* CASE # 735 Comment End */
            request.setAttribute( "EXCEPTION" , dbEx );
        }catch( Exception ex ) {
            errorFlag = true;
            UtilFactory.log( ex.getMessage() , ex , "QuestionAction" ,
                    "perform()" );
            request.setAttribute( "EXCEPTION" ,
                    new CoeusException( "exceptionCode.30003" ) );
        }
        if( errorFlag ) {
            actionforward = actionMapping.findForward( FAILURE );
        } else {
            actionforward = actionMapping.findForward( SUCCESS );
        }
        return actionforward;
    }
}