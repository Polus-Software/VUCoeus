/*
 * OpportunityAction.java
 *
 * Created on July 14, 2006, 10:49 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.utk.coeuslite.propdev.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.bean.*;
import edu.mit.coeus.s2s.bean.DBOpportunityInfoBean;
import edu.mit.coeus.s2s.bean.S2SSubmissionDataTxnBean;
import edu.mit.coeus.s2s.validator.OpportunitySchemaParser;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.TypeConstants;
import java.sql.*;
import java.util.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;

/**
 *
 * @author sharathk
 */
public class OpportunityAction  extends ProposalBaseAction{
    
    /** Creates a new instance of OpportunityAction */
    public OpportunityAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward actionForward = null;
        
        String loggedinUser = null;
        UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute("user"+request.getSession().getId());
        loggedinUser = userInfoBean.getUserId();
        //JIRA COEUSDEV 61 - END
        //Case 3918 - if opportunity already exists, forward to grants gov page - START
        S2SSubmissionDataTxnBean s2SSubmissionDataTxnBean = new S2SSubmissionDataTxnBean();
        String proposalNumber = (String)request.getSession().getAttribute("proposalNumber");
        if(request.getParameter("proposalNumber") != null && request.getParameter("proposalNumber").length() > 0){
            proposalNumber = request.getParameter("proposalNumber");
            request.getSession().setAttribute("proposalNumber", proposalNumber);
        }
        //JIRA COEUSDEV 61 - START
        if(request.getParameter("checkSchema") != null){
            //Do Schema validation for AJAX Call
            try{
            new OpportunitySchemaParser().checkFormsAvailable(proposalNumber,request.getParameter("checkSchema"));
                response.getWriter().print("true");
            }catch(Exception exception){
                response.getWriter().print("false");
            }
            return null;
        }
        OpportunityInfoBean opportunityInfoBean =  s2SSubmissionDataTxnBean.getLocalOpportunity(proposalNumber);
        if(opportunityInfoBean != null) {
            //Opportunity already exists
            actionForward = new ActionForward("/grantsGovAction.do?proposalNumber="+proposalNumber);
            return actionForward;
        }
        //Case 3918 - if opportunity already exists, forward to grants gov page - END
        
        String opportunityId = request.getParameter("opportunityId");
        //Case 3377 - START
        String competitionId = request.getParameter("competitionId");
        //Case 3377 - END
        ArrayList oppList = (ArrayList)request.getSession().getAttribute("opportunity");
        
        //OpportunityInfoBean opportunityInfoBean = null;
        boolean found = false;
        // COEUSDEV-287: 4.4 QA - LITE error when I modify proposal Title & save  
        if(oppList != null ){
            for(int index = 0; index < oppList.size(); index++) {
                opportunityInfoBean = (OpportunityInfoBean)oppList.get(index);
                if(opportunityInfoBean.getOpportunityId().equalsIgnoreCase(opportunityId)
                //Case 3377 - START
                && (competitionId==null || competitionId.equals("") || competitionId.equalsIgnoreCase(opportunityInfoBean.getCompetitionId()))
                //Case 3377 - END
                ) {
                    found = true;
                    break;
                }//End If
            }//End For
        }
      if(found) {
            //S2SSubmissionDataTxnBean s2SSubmissionDataTxnBean = new S2SSubmissionDataTxnBean();
            //String proposalNumber = (String)request.getSession().getAttribute("proposalNumber");
            
            DBOpportunityInfoBean dBOpportunityInfoBean = new DBOpportunityInfoBean();
            dBOpportunityInfoBean.setProposalNumber(proposalNumber);
            dBOpportunityInfoBean.setCfdaNumber(opportunityInfoBean.getCfdaNumber());
            dBOpportunityInfoBean.setClosingDate(opportunityInfoBean.getClosingDate());
            dBOpportunityInfoBean.setCompetitionId(opportunityInfoBean.getCompetitionId());
            dBOpportunityInfoBean.setInstructionUrl(opportunityInfoBean.getInstructionUrl());
            dBOpportunityInfoBean.setOpeningDate(opportunityInfoBean.getOpeningDate());
            dBOpportunityInfoBean.setOpportunity(opportunityInfoBean.getOpportunity());
            dBOpportunityInfoBean.setOpportunityId(opportunityInfoBean.getOpportunityId());
            dBOpportunityInfoBean.setOpportunityTitle(opportunityInfoBean.getOpportunityTitle());
            dBOpportunityInfoBean.setRevisionCode(opportunityInfoBean.getRevisionCode());
            dBOpportunityInfoBean.setRevisionOtherDescription(opportunityInfoBean.getRevisionOtherDescription());
            dBOpportunityInfoBean.setSchemaUrl(opportunityInfoBean.getSchemaUrl());
            dBOpportunityInfoBean.setPackageID(opportunityInfoBean.getPackageID());
            //dBOpportunityInfoBean.setSubmissionTypeCode(opportunityInfoBean.getSubmissionTypeCode());
            //@todo:setSubmissionType Code
            dBOpportunityInfoBean.setSubmissionTypeCode(1);
            
            CoeusFunctions coeusFunctions = new CoeusFunctions();
            Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
            
            Timestamp timestamp = (Timestamp)request.getSession().getAttribute("SelectOpportunityTS");
            if(timestamp != null){
                dBOpportunityInfoBean.setAcType('U');
                dBOpportunityInfoBean.setAwUpdateTimestamp(timestamp);
                request.getSession().removeAttribute("SelectOpportunityTS");
            }else {
                dBOpportunityInfoBean.setAcType('I');
            }
            dBOpportunityInfoBean.setUpdateTimestamp(dbTimestamp);
            
            dBOpportunityInfoBean.setUpdateUser(loggedinUser);
            
            //s2SSubmissionDataTxnBean.updateDelOpportunity(dBOpportunityInfoBean);
            try{
                s2SSubmissionDataTxnBean.addOpportunity(dBOpportunityInfoBean);
                request.getSession().setAttribute("grantsGovExist", "1");
                //JIRA COEUSDEV 61 - START
                if(request.getParameter("forward") != null && request.getParameter("forward").equals("generalInfo")) {
                    actionForward = actionMapping.findForward("generalInfo");
                    request.getSession().removeAttribute("opportunity");
                }else {
                    actionForward = new ActionForward("/grantsGovAction.do?proposalNumber="+proposalNumber);
                    request.getSession().removeAttribute("opportunity");
                }
                //JIRA COEUSDEV 61 - END
            }catch (CoeusException coeusException) {
                //request.getSession().removeAttribute("opportunity");
                request.setAttribute("Exception", coeusException);
                actionForward = actionMapping.findForward("opportunity");
            }
            
        }
      // forwarded to general info page if there is no other forwards
      
      if(actionForward == null){
          actionForward = actionMapping.findForward("generalInfo");
      }
        return actionForward;
    }
}
