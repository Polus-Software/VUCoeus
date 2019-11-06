/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;

import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeuslite.coiv2.beans.CoiAwardInfoBean;
import edu.mit.coeuslite.coiv2.beans.CoiProposalBean;
import edu.mit.coeuslite.coiv2.beans.CoiProtocolInfoBean;
import edu.mit.coeuslite.coiv2.formbeans.CoiPersonProjectDetailsForm;
import edu.mit.coeuslite.coiv2.services.CoiCommonService;
import edu.mit.coeuslite.coiv2.services.GettingRightsCoiV2Service;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.beans.CoiMenuBean;

/**
 *
 * @author Sony
 * To fetch the list of all proposals for the corresponding logged in user
 * This would further be used for Creating a Proposal Based Disclosure
 */
public class AnnualCoiDisclAction extends COIBaseAction {
    
    private static int projectCount=0;
    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //for annual disclosure menu change
        CoiCommonService coiCommonService1 = CoiCommonService.getInstance();
        Integer disclosureAvailable = coiCommonService1.userHasDisclosure(request);
        request.removeAttribute("DiscViewQtnr");
        request.setAttribute("DiscViewByPrjt", true);//to check Project menu selected
        String operationType = (String)request.getAttribute("operationType");  
        request.setAttribute("operationType", operationType);
        if (disclosureAvailable > 0) {
            request.setAttribute("disclosureAvailableMessage", true);
            CoiDisclosureBean annualBean = new CoiDisclosureBean();
            annualBean = coiCommonService1.getAnnualDisclosure(request);
            request.setAttribute("annualDisclosureBean", annualBean);
        } else {
            request.setAttribute("disclosureAvailableMessage", false);
        }
        //for annual disclosure menu change end
 //disclosure submenu fix start
        PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId = person.getPersonID();
        CoiDisclosureBean currDisc = (CoiDisclosureBean) request.getSession().getAttribute("disclosureBeanSession");
                     Integer sequenceN=0,sNumber=0;
                     String disclosureN = null,dNumber=null;    
                     dNumber=(String)request.getSession().getAttribute("DisclosureNumberInUpdateSession"); 
                    if(currDisc!=null){
                      if(currDisc.getCoiDisclosureNumber() != null) {
                     disclosureN =(String)currDisc.getCoiDisclosureNumber();
                      }
                       if(currDisc.getSequenceNumber() != null) {
                       sequenceN = (Integer) currDisc.getSequenceNumber();
                       }
                      }
                
                      sNumber= (Integer) request.getSession().getAttribute("currentSequence");
                      if(request.getSession().getAttribute("currentSequence")==null){
                          sNumber=sequenceN;
                      }
                      if(dNumber==null){
                          dNumber=disclosureN;
                      }
            
             setApprovedDisclosureDetails(dNumber,sNumber,personId,request);  
    //disclosure submenu fix end    
             // NEW MENU CHANGE 
             if(dNumber == null ||(dNumber!=null &&dNumber.trim().length()<=0)){
                 dNumber = (String)request.getSession().getAttribute("DisclNumber");
             }
             if(sNumber == null || sNumber < 1){
                sNumber = (Integer)request.getSession().getAttribute("DisclSeqNumber");  
             }                
            coiMenuDataSaved(dNumber,sNumber,personId,request);// saved menu     
        if(request.getSession().getAttribute("contineWithoutQnr")==null){
            CoiMenuBean coiMenuBean = (CoiMenuBean)request.getSession().getAttribute("coiMenuDatasaved");   
              if(coiMenuBean == null || ((coiMenuBean!=null && coiMenuBean.isQuestDataSaved()==false)&&(coiMenuBean!=null && coiMenuBean.isPrjDataSaved()==false)&&(coiMenuBean!=null && coiMenuBean.isNoteDataSaved()==false)&&(coiMenuBean!=null && coiMenuBean.isAttachDataSaved()==false))){
            request.setAttribute("QnrNotCompleted","true");
            } else{
               request.removeAttribute("QnrNotCompleted");
            }
         }
            // NEW MENU CHANGE 

        String actionForward = null;
        WebTxnBean webTxn = new WebTxnBean();
        ActionErrors actionErrors = new ActionErrors();
        GettingRightsCoiV2Service gettingRightsCoiV2Service = GettingRightsCoiV2Service.getInstance();       
        Hashtable propUserRole = (Hashtable) webTxn.getResults(request, "getProposalTypeList", null);
        Vector propsalType = new Vector();
        propsalType = (Vector) propUserRole.get("getProposalTypeList");
        request.setAttribute("propsalType", propsalType);
        if (actionMapping.getPath().equals("/saveNonIntegratedAnnuals")) {
            String operation = request.getParameter("operation");
            request.setAttribute("operation", operation);
            Vector proposalList = new Vector();
            proposalList = (Vector) request.getSession().getAttribute("proposalProjectList");
            request.setAttribute("proposalList", proposalList);
            if (proposalList == null) {
                proposalList = new Vector();
            }
            PersonInfoBean personBean = (PersonInfoBean) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON);
            String createUser = personBean.getUserName();
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            CoiPersonProjectDetailsForm coiPersonProjectDetailsForm = (CoiPersonProjectDetailsForm) actionForm;
            CoiProposalBean proposalBean = new CoiProposalBean();
            proposalBean.setProposalNumber(coiPersonProjectDetailsForm.getCoiProjectId());
            proposalBean.setProposalTypeDesc(coiPersonProjectDetailsForm.getCoiProjectType());
            proposalBean.setTitle(coiPersonProjectDetailsForm.getCoiProjectTitle());
            proposalBean.setCreateUser(createUser);
            try {
                proposalBean.setStartDate(df.parse(coiPersonProjectDetailsForm.getCoiProjectStartDate()));
            } catch (Exception e) {
                e.printStackTrace();
                UtilFactory.log(e.getMessage(), e, "AnnualCoiDisclAction", "performExecuteCOI()");
                request.setAttribute("invaliddate", "Please enter a valid start date");
                actionErrors.add(ActionErrors.GLOBAL_ERROR, new ActionError("nonintegratedproposal.invalidstartdate"));
                saveErrors(request, actionErrors);
                return actionMapping.findForward("success");
            }
            proposalBean.setSponsorName(coiPersonProjectDetailsForm.getCoiProjectSponser());
            proposalBean.setTotalCost(coiPersonProjectDetailsForm.getCoiProjectFundingAmount());
            try {
                proposalBean.setEndDate(df.parse(coiPersonProjectDetailsForm.getCoiProjectEndDate()));
            } catch (Exception e) {
                request.setAttribute("invaliddate", "Please enter a valid end date");
                actionErrors.add(ActionErrors.GLOBAL_ERROR, new ActionError("nonintegratedproposal.invalidenddate"));
                saveErrors(request, actionErrors);
                return actionMapping.findForward("success");
            }
            proposalBean.setCreateUser(createUser);
            proposalBean.setAcType("I");
            proposalBean.setNonIntegrated(true);
            proposalList.add(proposalBean);
            request.setAttribute("proposalList", proposalList);
            request.getSession().setAttribute("proposalProjectList", proposalList);
            BeanUtils.copyProperties(actionForm, new CoiPersonProjectDetailsForm());
            getAllProtocolList(request);
            getAllAward(request);
        } else {
            String operation = request.getParameter("acType");
             operationType = request.getParameter("operationType");
            if (operationType!=null && operationType.equals("MODIFY")) {
                operation = operationType;
            }
            request.getSession().removeAttribute("proposalProjectList");
            projectCount=0;
            getAllProposals(request);  // list only Disclosed submitted Dev Proposal 
            getAllInstProposals(request);
            getAllProtocolList(request);
            getAllIacucProtocolList(request);
            getAllAward(request);
            request.getSession().setAttribute("certValidPjtCount",projectCount);            
//            proposal start
            if (operation != null && !operation.equals("null") && operation.equals("MODIFY")) {              
                request.setAttribute("operation", "MODIFY");
            }
        }
        actionForward = "success";
        return actionMapping.findForward(actionForward);
    }
     private Vector getAllInstProposals(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        WebTxnBean webTxn = new WebTxnBean();
        Vector propUserDet = null;
        if (session.getAttribute("person") == null) {
            checkCOIPrivileges(request);
        }

        PersonInfoBean personBean = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String createUser = personBean.getUserName();
         String personId = personBean.getPersonID();
         /* **  speed 
         PropBasedCoiDisclAction pDisclActn=new PropBasedCoiDisclAction();
        CoiDisclosureBean disclosureBean = pDisclActn.getApprovedDisclosureBean(personId, request);
        String disclosureNumber = disclosureBean.getCoiDisclosureNumber();
         ** */
        HashMap hmData = new HashMap();
        hmData.put("pid", personId);
        hmData.put("discnumber",null);
        Hashtable propUserRole = (Hashtable) webTxn.getResults(request, "getInstProposals", hmData);
        propUserDet = (Vector) propUserRole.get("getInstProposals");
        if (propUserDet != null && propUserDet.size() > 0) {
            request.setAttribute("getInstProposals", propUserDet);
            request.getSession().setAttribute("getInstProposals", propUserDet);
            projectCount=projectCount+propUserDet.size();
        }
        return propUserDet;
    }
    private void checkCOIPrivileges(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();

        UserDetailsBean userDetailsBean = new UserDetailsBean();
        PersonInfoBean personInfoBean = (PersonInfoBean) session.getAttribute(
                SessionConstants.LOGGED_IN_PERSON);
        if (personInfoBean != null && personInfoBean.getPersonID() != null && personInfoBean.getUserName() != null) {
            //setting personal details with the session object
            // session.setAttribute(PERSON_DETAILS_REF,personInfo);
            //setting privilege of a logged in user with the session
            String userName = personInfoBean.getUserName();
            int value = userDetailsBean.getCOIPrivilege(userName);
            session.setAttribute(PRIVILEGE, "" + userDetailsBean.getCOIPrivilege(userName));

            //setting logged in user's person id with the session
            session.setAttribute(LOGGEDINPERSONID, personInfoBean.getPersonID());
            //setting logged in user's person name with the session
            String personName = personInfoBean.getFullName();
            session.setAttribute(LOGGEDINPERSONNAME, personName);
            //Check whether to show link for View Pending Disclosures
            if (userDetailsBean.canViewPendingDisc(userName)) {
                session.setAttribute(VIEW_PENDING_DISC, VIEW_PENDING_DISC);
            }
        }
        session.setAttribute("person", personInfoBean);
    }

         private Vector getAllProtocolList(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        WebTxnBean webTxn = new WebTxnBean();
        Vector protocolDet = null;

        if (session.getAttribute("person") == null) {
            checkCOIPrivileges(request);
        }

        PersonInfoBean personBean = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String createUser = personBean.getUserId();
        PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId = person.getPersonID();
        /* **  speed 
        ProtocolBasedCoiDisclAction protoCoiDisclActn=new ProtocolBasedCoiDisclAction();
        CoiDisclosureBean disclosureBean = protoCoiDisclActn.getApprovedDisclosureBean(personId, request);
        String disclosureNumber = disclosureBean.getCoiDisclosureNumber();
        ** */
        HashMap hmData = new HashMap();
        hmData.put("createUser", personId);
         hmData.put("discnumber",null);
        Hashtable protocolData = (Hashtable) webTxn.getResults(request, "getAllNewProtocolList", hmData);
        protocolDet = (Vector) protocolData.get("getAllNewProtocolList");
        if (protocolDet != null && protocolDet.size() > 0) {
            request.setAttribute("protocolList", protocolDet);
            request.getSession().setAttribute("protocolProjectListList", protocolDet);
            projectCount=projectCount+protocolDet.size();
        }

        return protocolDet;
    }
     private Vector getAllIacucProtocolList(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        WebTxnBean webTxn = new WebTxnBean();
        Vector protocolDet = null;

        if (session.getAttribute("person") == null) {
            checkCOIPrivileges(request);
        }

        PersonInfoBean personBean = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String createUser = personBean.getUserId();
        PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId = person.getPersonID();
        /* **  speed 
        ProtocolBasedCoiDisclAction protoCoiDisclActn=new ProtocolBasedCoiDisclAction();
        CoiDisclosureBean disclosureBean = protoCoiDisclActn.getApprovedDisclosureBean(personId, request);
        String disclosureNumber = disclosureBean.getCoiDisclosureNumber();
        ** */
        HashMap hmData = new HashMap();
        hmData.put("createUser", personId);
         hmData.put("discnumber",null);
        Hashtable protocolData = (Hashtable) webTxn.getResults(request, "getAllIACUCProtocolList", hmData);
        protocolDet = (Vector) protocolData.get("getAllIACUCProtocolList");
        if (protocolDet != null && protocolDet.size() > 0) {
            request.setAttribute("getAllIACUCProtocolList", protocolDet);
            request.getSession().setAttribute("getAllIACUCProtocolList", protocolDet);
            projectCount=projectCount+protocolDet.size();
        }

        return protocolDet;
    }

    private Vector getAllAward(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        Vector awardDet = null;
        WebTxnBean webTxn = new WebTxnBean();
        if (session.getAttribute("person") == null) {
            checkCOIPrivileges(request);
        }

        PersonInfoBean personBean = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String updateUser = personBean.getUserName();
        PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId = person.getPersonID();
      /* **  speed 
        AwardBasedCoiDisclAction protoCoiDisclActn=new AwardBasedCoiDisclAction();
        CoiDisclosureBean disclosureBean = protoCoiDisclActn.getApprovedDisclosureBean(personId, request);
        String disclosureNumber = disclosureBean.getCoiDisclosureNumber();
         ** */
        HashMap hmData = new HashMap();
        hmData.put("updateUser", personId);
        hmData.put("discnumber",null);
        Hashtable awardDetList = (Hashtable) webTxn.getResults(request, "getAllNewAwards", hmData);
        awardDet = (Vector) awardDetList.get("getAllNewAwards");
        if (awardDet != null && awardDet.size() > 0) {
            request.setAttribute("allAwardList", awardDet);
            session.setAttribute("allAwardProjectList", awardDet);
            session.setAttribute("awardListForAttachment", awardDet);
            projectCount=projectCount+awardDet.size();
        }

        return awardDet;
    }
      private void setApprovedDisclosureDetails(String coiDisclosureNumber,Integer sequenceNumber,String personId,HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();  //added by Vineetha
        HashMap hmData = new HashMap();
        hmData.put("coiDisclosureNumber", coiDisclosureNumber);
         if(coiDisclosureNumber==null)
        { hmData.put("sequenceNumber",0);}
        else
        {hmData.put("sequenceNumber", sequenceNumber);}
        hmData.put("personId", personId);
        WebTxnBean webTxn = new WebTxnBean();
 /* **
        Vector statusDispDet = new Vector();
        Hashtable statusData = (Hashtable) webTxn.getResults(request, "getDisclDispositionStatus", hmData);
        statusDispDet = (Vector) statusData.get("getDisclDispositionStatus");
        if (statusDispDet != null && statusDispDet.size() > 0) {
            request.setAttribute("statusDispDetView", statusDispDet);
        }
  ** */
        Hashtable DisclData = (Hashtable) webTxn.getResults(request, "getDisclBySequnce", hmData);
        Vector DisclDet = (Vector) DisclData.get("getDisclBySequnce");
        if (DisclDet != null && DisclDet.size() > 0) {
            request.setAttribute("ApprovedDisclDetView", DisclDet);
/* **            for (Iterator it = DisclDet.iterator(); it.hasNext();) {
                CoiDisclosureBean object = (CoiDisclosureBean) it.next();
                if(object.getCertificationTimestamp()!=null){
                     request.setAttribute("isCertified", true);
                }else{
                    request.setAttribute("isCertified", false);
                }
            }
 ** */
        }
/* **
        DisclData = (Hashtable) webTxn.getResults(request, "getDisclStatus", hmData);
        Vector statusDet = (Vector) DisclData.get("getDisclStatus");
        if (DisclDet != null && DisclDet.size() > 0) {
            request.setAttribute("statusDetView", statusDet);
        }
** */
             //added by Vineetha
      hmData = new HashMap();
        hmData.put("personId", personId);
        Hashtable htPersonData = (Hashtable) webTxn.getResults(request, "getPersonDetails", hmData);
        Vector personDatas = (Vector) htPersonData.get("getPersonDetails");
        if (personDatas != null && personDatas.size() > 0) {
            PersonInfoBean personInfoBean = (PersonInfoBean) personDatas.get(0);

            //added by Vineetha
              request.setAttribute("PersonDetails", personDatas);
            session.setAttribute("person", personInfoBean);
    }

    }
           private Vector getAllProposals(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        WebTxnBean webTxn = new WebTxnBean();
        Vector propUserDet = null;

        PersonInfoBean personBean = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
       // String createUser = personBean.getUserName();
         String personId = personBean.getPersonID();
         /* **  speed
         PropBasedCoiDisclAction pDisclActn=new PropBasedCoiDisclAction();
        CoiDisclosureBean disclosureBean = pDisclActn.getApprovedDisclosureBean(personId, request);
        String disclosureNumber = disclosureBean.getCoiDisclosureNumber();
        ** */
        HashMap hmData = new HashMap();
        hmData.put("createUser", personId);
           hmData.put("discnumber",null);
        Hashtable propUserRole = (Hashtable) webTxn.getResults(request, "getAllNewProposals", hmData);
        propUserDet = (Vector) propUserRole.get("getAllNewProposals");
        if (propUserDet != null && propUserDet.size() > 0) {
            request.setAttribute("proposalList", propUserDet);
            request.getSession().setAttribute("proposalListForAttachment", propUserDet);
            projectCount=projectCount+propUserDet.size();
          }
        return propUserDet;
    }
}