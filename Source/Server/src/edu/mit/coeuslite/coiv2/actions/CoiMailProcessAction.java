/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.beans.CoiInfoBean;
import edu.mit.coeuslite.coiv2.beans.CoiProposalBean;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author UnNamed
 */
public class CoiMailProcessAction extends COIBaseAction{
   private static String ANNUAL ="Annual";
    private static String REVISION ="Revision";
    private static String PROPOSAL ="Proposal";
    private static String PROTOCOL ="Protocol";
    private static String IACUCPROTOCOL ="IACUCProtocol"; 
    private static String AWARD ="Award";
    private static String TRAVEL ="Travel";
    private static final String USER ="user";
    @Override
    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
       CoiInfoBean coiInfoBean = (CoiInfoBean)request.getSession().getAttribute("CoiInfoBean");
       if(coiInfoBean==null){
          coiInfoBean =new CoiInfoBean();
       }
       HttpSession session = request.getSession();
       String navigate ="success";
       String disclosureNumber = null;
       Integer sequenceNumber = null;
       Integer reviweCode = -1;
       String moduleItemKey = null;
       Integer moduleCode = null;
       String personId = null;
      request.getSession().setAttribute("projectType",PROPOSAL);
       if(request.getParameter("moduleItemKey")!=null){
           moduleItemKey = (String)request.getParameter("moduleItemKey");
       } 
       if(request.getParameter("moduleCode")!=null){
           moduleCode = Integer.parseInt(request.getParameter("moduleCode").toString());
       }
       if(request.getParameter("personId")!=null){
            personId = (String)request.getParameter("personId");
       }       
       if(moduleItemKey == null){
           navigate = "home";
           actionMapping.findForward("home");
       } 
       boolean flag = false;             
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmData = new HashMap();
        Vector projectDet = null;    
        hmData.put("personId", personId);
        hmData.put("moduleItemKey", moduleItemKey);
        Hashtable protocolData = (Hashtable) webTxnBean.getResults(request, "getCoiMailProcess", hmData);
        projectDet = (Vector) protocolData.get("getCoiMailProcess");
        if (projectDet != null && projectDet.size() > 0) {
           CoiDisclosureBean coiDisclosureBean = (CoiDisclosureBean)projectDet.get(0);
           if(coiDisclosureBean!=null){
               disclosureNumber = coiDisclosureBean.getCoiDisclosureNumber();
               sequenceNumber= coiDisclosureBean.getSequenceNumber();
               reviweCode = coiDisclosureBean.getReviewStatusCode();
           }
        }
        if(disclosureNumber == null){
            disclosureNumber = userHasDisclosure(request,personId);
            coiInfoBean.setDisclosureNumber(disclosureNumber);
            if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
               Vector vecProposal = getAllProposals(request,disclosureNumber);
               flag = checkProposalExist(vecProposal,moduleItemKey,request);
               coiInfoBean.setModuleCode(ModuleConstants.PROPOSAL_DEV_MODULE_CODE);
               coiInfoBean.setEventType(ModuleConstants.COI_EVENT_PROPOSAL);
               coiInfoBean.setPersonId(personId);
               coiInfoBean.setProjectId(moduleItemKey);
               coiInfoBean.setProjectType(PROPOSAL);   
               
            }         
            if(!flag){
                 navigate = "home";
                 actionMapping.findForward("home");
            }
            else{
                navigate = "commonSave";
                actionMapping.findForward("commonSave");
            }
        }
        else{
            if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
               coiInfoBean.setDisclosureNumber(disclosureNumber);
               coiInfoBean.setSequenceNumber(sequenceNumber);
               coiInfoBean.setModuleCode(ModuleConstants.PROPOSAL_DEV_MODULE_CODE);
               coiInfoBean.setEventType(ModuleConstants.COI_EVENT_PROPOSAL);
               coiInfoBean.setPersonId(personId);
               coiInfoBean.setProjectId(moduleItemKey);
               coiInfoBean.setProjectType(PROPOSAL);
            }
           navigate = setActionForward(request,moduleCode,personId,moduleItemKey,disclosureNumber,sequenceNumber,reviweCode);
        }
        UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute(USER+session.getId());
        coiInfoBean.setUserId(userInfoBean.getUserId());
       request.getSession().setAttribute("CoiInfoBean", coiInfoBean);  
       return actionMapping.findForward(navigate) ;       
    }
    private String userHasDisclosure(HttpServletRequest request,String personId) throws Exception {

        WebTxnBean webTxn = new WebTxnBean();
        String hasDisclosure = null;
        String disclNmbr = null;          
        HashMap hasRightMap = new HashMap();
        HashMap hmData = new HashMap();
        hmData.put("userId", personId);
        Hashtable hasRightHashtable = (Hashtable) webTxn.getResults(request, "fnGetUserCoiV2Discl", hmData);
        hasRightMap = (HashMap) hasRightHashtable.get("fnGetUserCoiV2Discl");
        if (hasRightMap != null && hasRightMap.size() > 0) {
            disclNmbr = ((String) hasRightMap.get("disclosureAvailable"));
            if(disclNmbr!=null && !disclNmbr.equalsIgnoreCase("0")){               
               hasDisclosure = disclNmbr ;
            }
        }
        return hasDisclosure;
    }
    private String setActionForward(HttpServletRequest request,Integer moduleCode,
            String personId,String moduleItemKey,String disclosureNumber,Integer sequenceNumber,Integer reviweCode){
         String navigate = "home";
        if(reviweCode == 1){
                    if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                            request.setAttribute("discl",disclosureNumber);
                            request.setAttribute("seq",sequenceNumber);
                            request.setAttribute("personId",personId);
                            request.setAttribute("projectID",moduleItemKey);       
                            request.setAttribute("eventType",PROPOSAL);
                           navigate ="InProgress";              
                    }
            }
        else if (reviweCode == 2){
                    if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                            request.setAttribute("param1",disclosureNumber);
                            request.setAttribute("param2",sequenceNumber.toString());
                            request.setAttribute("param3",personId);
                            request.setAttribute("param6","pendingDiscl");       
                            request.setAttribute("param5",PROPOSAL);
                            navigate ="submitted";            
                    }                    
        }
         return  navigate;    
      
    }      
    
        private Vector getAllProposals(HttpServletRequest request,String disclosureNumber) throws Exception {
        HttpSession session = request.getSession();
        WebTxnBean webTxn = new WebTxnBean();
        Vector propUserDet = null;
        PersonInfoBean personBean = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);       
        String personId = personBean.getPersonID();        
        HashMap hmData = new HashMap();
        hmData.put("createUser", personId);
           hmData.put("discnumber",disclosureNumber);
        Hashtable propUserRole = (Hashtable) webTxn.getResults(request, "getAllNewProposals", hmData);
        propUserDet = (Vector) propUserRole.get("getAllNewProposals");
        if (propUserDet != null && propUserDet.size() > 0) {           
            request.setAttribute("proposalList", propUserDet);
            session.setAttribute("proposalListForAttachment", propUserDet);          
          }
        return propUserDet;
    }
       private boolean checkProposalExist(Vector vecProposal,String moduleItemKey,HttpServletRequest request){
           boolean returnVal = false;
           if(vecProposal!=null && !vecProposal.isEmpty()){
               for (Iterator it = vecProposal.iterator(); it.hasNext();) {
                    CoiProposalBean coiProposalBean = (CoiProposalBean) it.next();
                    if(coiProposalBean!=null){
                        if(coiProposalBean.getProposalNumber().equalsIgnoreCase(moduleItemKey)){
                            String parameterValue[] = new String[1];
                            parameterValue[0] = coiProposalBean.getProposalNumber()+";"+coiProposalBean.getTitle()+";"+coiProposalBean.getSponsorName()
                                    +";"+coiProposalBean.getStartDate()+";"+coiProposalBean.getEndDate()+";"+false+";"+"0.00"+";"+coiProposalBean.getProposalTypeDesc();
                           request.setAttribute("checkedPropsalProjects",parameterValue);
                           returnVal = true; 
                           break;
                        }
                    }
                   
               }
           }
           return  returnVal;
       }
}
