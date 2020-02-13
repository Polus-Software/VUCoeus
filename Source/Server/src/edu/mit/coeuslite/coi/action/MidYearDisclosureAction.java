/*
 * MidYearDisclosureAction.java
 *
 * Created on 04 January 2006, 13:35
 */

package edu.mit.coeuslite.coi.action;

/**
 *
 * @author  mohann
 */

import edu.mit.coeus.bean.PersonInfoBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.struts.validator.DynaValidatorForm;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/*
 * To create a new disclosure on existing award, proposal
 * by validating Award Number or Proposal Number respectively or on a Temporary
 * Proposal.
 *
 */

public class MidYearDisclosureAction extends COIBaseAction {
    
    //private ActionForward actionForward = null;
    //private HttpServletRequest req;
    //private HttpServletResponse res;
    //private ActionMapping mapping;
    //private WebTxnBean webTxnBean ;
    
    
    /** Creates a new instance of MidYearDisclosureAction */
    public MidYearDisclosureAction() {
    }
    /*
    *Fix # 2866
    *change method name from performExecute to performExecuteCOI
    */
    
    public ActionForward performExecuteCOI(
    ActionMapping actionMapping,
    org.apache.struts.action.ActionForm actionForm,
    HttpServletRequest request,
    HttpServletResponse response) throws Exception {
        //this.req = request;
        //this.res = response;
        //this.mapping = actionMapping;
        WebTxnBean webTxnBean =  new WebTxnBean();
        ActionMessages messages = new ActionMessages();
        HttpSession session= request.getSession();      
        String userName = null;
        DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)actionForm;
        
       
        ActionForward actionforward = actionMapping.findForward( "success" );      
      
        boolean validResult = false;
        
        if(actionMapping.getPath().equals("/midYearDisclosure") ) {
            dynaValidatorForm.set("disclosureTypeCode","1");
            return actionforward;
        }
        // process form information
       
        String personId = (String)dynaValidatorForm.get("personId");
        String personFullName =(String)dynaValidatorForm.get("fullName");
        
        PersonInfoBean personInfoBean = ( PersonInfoBean ) session.getAttribute("person");
        String loggedinpersonid = (String)session.getAttribute("loggedinpersonid");
        String userprivilege = (String)session.getAttribute("userprivilege");
        
         /* Check whether loggedinpersonid is the same as personID.
        If yes, create a new disclosure for personID.  Else,
        check whether user has Maintain COI.  If yes, create a new disclosure for
        personID.  Else, create a new disclosure for loggedinpersonid.
          */
        if(personId == null){
            personId = personInfoBean.getPersonID();
        }
        if(loggedinpersonid.equals(personId)){
            personFullName = personInfoBean.getFullName();
        }
        else{
            if(userprivilege.equals("2")){
                personFullName = personInfoBean.getFullName();
            }
            else{
                personId = loggedinpersonid;
                personFullName = (String)session.getAttribute("loggedinpersonname");
            }
        }
        //String disclosureType = request.getParameter("disclosureTypeCode");
        String disclosureType = (String)dynaValidatorForm.get("disclosureTypeCode");
        
        if((disclosureType == null ) || ( disclosureType.trim().equals( "" ) ) ) {
            messages.add("disclosureTypeRequired",
            new ActionMessage("error.newCOIDisclosureForm.disclosureType.requried"));
            saveMessages(session, messages); 
            actionforward = actionMapping.findForward("exception");
        }
        if( disclosureType.equals( "1" ) ) {
            String awardNum = (String)dynaValidatorForm.get("sponsorAwdNumber");
            // validate award number
            if( ( awardNum == null ) || ( awardNum.trim().equals( "" ) ) ) {               
                messages.add( "awardProposalNumRequired" , new ActionMessage(
                "error.NewCOIDisclosureActionForm.awardNumRequired" ) );
                saveMessages(session, messages);
                actionforward = actionMapping.findForward("exception");
            }else{
                validResult = isAwardNumValid(dynaValidatorForm, request);
                if( !validResult ) {
                    messages.add( "Invalid Award Number" ,
                    new ActionMessage( "error.invalidAwardNumber" ) );
                    saveMessages(request,messages);
                    actionforward = actionMapping.findForward("exception");
                }else {
                    request.setAttribute("appliesToCode", awardNum );
                }
            }
            
        }//end if
        
        // if user wish to create Disclosure for existing proposal then ensure the user to provide proposal number
        if( disclosureType.equals( "2" ) ) {
            String proposalNum = (String)dynaValidatorForm.get("proposalNumber");
            // validate proposal number
            if( ( proposalNum == null ) || ( proposalNum.trim().equals( "" ) ) ) {               
                messages.add( "awardProposalNumRequired" , new ActionMessage(
                "error.NewCOIDisclosureActionForm.proposalNumRequired" ) );
                saveMessages(session, messages);
                actionforward = actionMapping.findForward("exception");
            }else{
                validResult = isProposalNumValid(dynaValidatorForm, request);
                // if proposal number is invalid then generate an error
                if( !validResult ) {
                    messages.add( "Invalid Proposal Number" ,
                    new ActionMessage( "error.invalidProposalNumber" ) );
                    saveMessages(request,messages);
                    actionforward = actionMapping.findForward("exception");
                }else{
                    request.setAttribute( "appliesToCode" , proposalNum );
                }
            }
        }
        //keep the user selected disclosureType in  request scope
        request.setAttribute( "disclosureTypeCode" , disclosureType );
        request.setAttribute("personId",personId);
        
        return actionforward;
    }
    
    
    /** Method to check whether Award Number exists or not
     */
    private boolean isAwardNumValid(DynaValidatorForm
    dynaValidatorForm, HttpServletRequest request)throws Exception{
        
        boolean awardNumberExists =false;
        String awardNum = (String)dynaValidatorForm.get("sponsorAwdNumber");
        HashMap hmData = new HashMap();
        hmData.put("sponsorAwdNumber",awardNum);
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htDiscExists =
        (Hashtable)webTxnBean.getResults(request, "isValidAwardNumber", hmData);
        HashMap hmDiscExists = (HashMap)htDiscExists.get("isValidAwardNumber");
        int awardNo = Integer.parseInt(hmDiscExists.get("isValid").toString());
        if(awardNo == 1){
            awardNumberExists = true;
            return awardNumberExists ;
        }else{
            return awardNumberExists ;
        }
    }
    
    /** Method to check whether Proposal Number exists or not
     */
    private boolean isProposalNumValid(DynaValidatorForm
    dynaValidatorForm, HttpServletRequest request)throws Exception{
        boolean proposalNumberExists =false;
        String proposalNum = (String)dynaValidatorForm.get("proposalNumber");
        HashMap hmData = new HashMap();
        hmData.put("proposalNumber",proposalNum);
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htDiscExists =
        (Hashtable)webTxnBean.getResults(request, "isValidProposalNumber", hmData);
        HashMap hmDiscExists = (HashMap)htDiscExists.get("isValidProposalNumber");
        int proposalNo = Integer.parseInt(hmDiscExists.get("isValid").toString());
        if(proposalNo == 1){
            proposalNumberExists = true;
            return proposalNumberExists ;
        }else{
            return proposalNumberExists ;
        }
    }
}
