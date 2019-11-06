/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;

import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeuslite.coiv2.services.CoiCommonService;
import edu.mit.coeuslite.coiv2.services.GettingRightsCoiV2Service;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.beans.CoiInfoBean;
/**
 *
 * @author UnNamed
 */
public class CoiAnnualRevisionAction extends COIBaseAction {
  private static String PROJECT_TYPE_ANNUAL ="Annual";
  private static String PROJECT_TYPE_REVISION ="Revision";
  private int projectCount = 0;
    @Override
    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
       HttpSession session = request.getSession();
       request.getSession().setAttribute("disableCoiMenu",true);// to disable the questionnaire menu
       String projectType =(String)request.getParameter("projectType");
       CoiCommonService coiCommonService1 = CoiCommonService.getInstance();
        CoiInfoBean coiInfoBean = (CoiInfoBean)request.getSession().getAttribute("CoiInfoBean");
        if(coiInfoBean==null){
            coiInfoBean =new CoiInfoBean();
        }
        coiInfoBean.setProjectType(projectType);
        coiInfoBean.setModuleCode(ModuleConstants.ANNUAL_COI_DISCLOSURE);
        if(projectType!=null && projectType.equalsIgnoreCase(PROJECT_TYPE_ANNUAL)){
           coiInfoBean.setEventType(ModuleConstants.COI_EVENT_ANNUAL); 
           coiInfoBean.setMenuType("NewCreate");
        }
        else{
            coiInfoBean.setEventType(ModuleConstants.COI_EVENT_REVISION);  
            coiInfoBean.setMenuType("ReviseUpdate"); 
        }         
        Integer disclosureAvailable = coiCommonService1.userHasDisclosure(request);
        request.getSession().setAttribute("projectType",projectType);
        if (disclosureAvailable > 0) {
            request.setAttribute("disclosureAvailableMessage", true);
            CoiDisclosureBean annualBean = new CoiDisclosureBean();
            annualBean = coiCommonService1.getAnnualDisclosure(request);
            request.setAttribute("annualDisclosureBean", annualBean);
        } else {
            request.setAttribute("disclosureAvailableMessage", false);
        } 
        session.removeAttribute("CREATEFLAG");
        session.removeAttribute("getinonce");
        session.removeAttribute("qnranswerd");        
        PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId = person.getPersonID();
        getCoiPersonDetails(personId,request);
       if(projectType!=null && projectType.equalsIgnoreCase(PROJECT_TYPE_ANNUAL)){
        String disclosureNumber=null;
        Integer sequenceNumber=null;
        disclosureNumber = coiInfoBean.getDisclosureNumber();
        sequenceNumber =coiInfoBean.getApprovedSequence();
        if(sequenceNumber == null){
            sequenceNumber =coiInfoBean.getSequenceNumber();
        }
        setApprovedDisclosureDetails(disclosureNumber,sequenceNumber,personId,request);
        }   
        projectCount = 0;
        getAllAward(request);
        getAllInstProposals(request);   
        getAllProposals(request);          
        getAllProtocolList(request);
        getAllIacucProtocolList(request); 
        coiInfoBean.setProjectCount(projectCount);
        coiInfoBean.setProjectId(null);
        GettingRightsCoiV2Service gettingRightsCoiV2Service = GettingRightsCoiV2Service.getInstance();       
        String actionForward = "success";
      //Menu Saved Start   
        request.setAttribute("byProjectMenu",true);
        if(coiInfoBean!=null){
        coiMenuDataSaved(coiInfoBean.getDisclosureNumber(),coiInfoBean.getSequenceNumber(),coiInfoBean.getPersonId(),request);
        }
     //Menu Saved ends  
        session.setAttribute("CoiInfoBean",coiInfoBean);
        return actionMapping.findForward(actionForward);
    }
     
    private void setApprovedDisclosureDetails(String coiDisclosureNumber,Integer sequenceNumber,String personId,HttpServletRequest request) throws Exception {        
        HashMap hmData = new HashMap();
        hmData.put("coiDisclosureNumber", coiDisclosureNumber);
        if(coiDisclosureNumber==null)
        { hmData.put("sequenceNumber",0);}
        else
        {hmData.put("sequenceNumber", sequenceNumber);}
        hmData.put("personId", personId);
        WebTxnBean webTxn = new WebTxnBean();
        hmData.put("moduleCode",null);
        Hashtable DisclData = (Hashtable) webTxn.getResults(request, "getDisclBySequnce", hmData);
        Vector DisclDet = (Vector) DisclData.get("getDisclBySequnce");
       if (DisclDet != null && DisclDet.size() > 0) {
            request.setAttribute("ApprovedDisclDetView", DisclDet);
        } 
    }
     private Vector getAllAward(HttpServletRequest request) throws Exception {        
        Vector awardDet = null;  
        HttpSession session = request.getSession();
        WebTxnBean webTxn = new WebTxnBean();       
        PersonInfoBean personBean = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);       
        String personId = personBean.getPersonID();
        HashMap hmData = new HashMap();
        hmData.put("updateUser", personId);
        hmData.put("discnumber", null);
        Hashtable awardDetList = (Hashtable) webTxn.getResults(request, "getAllNewAwards", hmData);
        awardDet = (Vector) awardDetList.get("getAllNewAwards");        
        if (awardDet != null && awardDet.size() > 0) {
            projectCount = projectCount +  awardDet.size();
            request.setAttribute("allAwardList", awardDet);
            session.setAttribute("allAwardProjectList", awardDet);
        }

        return awardDet;
    }
     private Vector getAllInstProposals(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        WebTxnBean webTxn = new WebTxnBean();
        Vector propUserDet = null;
        PersonInfoBean personBean = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);       
        String personId = personBean.getPersonID();      
        HashMap hmData = new HashMap();
        hmData.put("pid", personId);
        hmData.put("discnumber",null);
        Hashtable propUserRole = (Hashtable) webTxn.getResults(request, "getInstProposals", hmData);
        propUserDet = (Vector) propUserRole.get("getInstProposals");
        if (propUserDet != null && propUserDet.size() > 0) {
            projectCount = projectCount + propUserDet.size();
            request.setAttribute("getInstProposals", propUserDet);
            session.setAttribute("getInstProposals", propUserDet);            
        }
        return propUserDet;
    }
      private Vector getAllProtocolList(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        WebTxnBean webTxn = new WebTxnBean();
        Vector protocolDet = null;              
        PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId = person.getPersonID();       
        HashMap hmData = new HashMap();
        hmData.put("createUser", personId);
         hmData.put("discnumber",null);
        Hashtable protocolData = (Hashtable) webTxn.getResults(request, "getAllNewProtocolList", hmData);
        protocolDet = (Vector) protocolData.get("getAllNewProtocolList");
        if (protocolDet != null && protocolDet.size() > 0) {
            projectCount = projectCount + protocolDet.size();
            request.setAttribute("protocolList", protocolDet);
            session.setAttribute("protocolProjectListList", protocolDet);           
        }

        return protocolDet;
    }
     private Vector getAllIacucProtocolList(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        WebTxnBean webTxn = new WebTxnBean();
        Vector protocolDet = null;       
        PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId = person.getPersonID();       
        HashMap hmData = new HashMap();
        hmData.put("createUser", personId);
         hmData.put("discnumber",null);
        Hashtable protocolData = (Hashtable) webTxn.getResults(request, "getAllIACUCProtocolList", hmData);
        protocolDet = (Vector) protocolData.get("getAllIACUCProtocolList");
        if (protocolDet != null && protocolDet.size() > 0) {
            projectCount = projectCount + protocolDet.size();
            request.setAttribute("getAllIACUCProtocolList", protocolDet);
            session.setAttribute("getAllIACUCProtocolList", protocolDet);            
        }
        return protocolDet;
    }
        private Vector getAllProposals(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        WebTxnBean webTxn = new WebTxnBean();
        Vector propUserDet = null;
        PersonInfoBean personBean = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);       
        String personId = personBean.getPersonID();        
        HashMap hmData = new HashMap();
        hmData.put("createUser", personId);
           hmData.put("discnumber",null);
        Hashtable propUserRole = (Hashtable) webTxn.getResults(request, "getAllNewProposals", hmData);
        propUserDet = (Vector) propUserRole.get("getAllNewProposals");
        if (propUserDet != null && propUserDet.size() > 0) {
            projectCount = projectCount + propUserDet.size();
            request.setAttribute("proposalList", propUserDet);
            session.setAttribute("proposalListForAttachment", propUserDet);          
          }
        return propUserDet;
    }
}