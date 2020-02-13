/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;

import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeuslite.coiv2.beans.CoiAnnualPersonProjectDetails;
import edu.mit.coeuslite.coiv2.beans.CoiAwardInfoBean;
import edu.mit.coeuslite.coiv2.beans.CoiFinancialEntityBean;
import edu.mit.coeuslite.coiv2.beans.CoiInfoBean;
import edu.mit.coeuslite.coiv2.beans.CoiPersonProjectDetails;
import edu.mit.coeuslite.coiv2.beans.CoiProposalBean;
import edu.mit.coeuslite.coiv2.beans.CoiProtocolInfoBean;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.io.IOException;
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
public class CoiCentralAction extends COIBaseAction{
    private static String ANNUAL ="Annual";
    private static String REVISION ="Revision";    
    private static String PROPOSAL ="Proposal";
    private static String PROTOCOL ="Protocol";
    private static String IACUCPROTOCOL ="IACUCProtocol"; 
    private static String AWARD ="Award";
    private static String TRAVEL ="Travel";
    private static String CONTINUE_FROM_QNR ="/continueFromQnr";
    private static String GET_BY_PROJECTS ="/coiCentralAction";
    private static String COI_PROJECT_STATUS_CODE = "210";
    private static String USER_ID;
    @Override
    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(); 
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        USER_ID = userInfoBean.getUserId();
        request.setAttribute("DiscViewByPrjt", true);//to check Project menu selected
        String forward = "event";        
        String projecType = null;
        Vector vecFilteredProject = new Vector();
        CoiInfoBean coiInfoBean = (CoiInfoBean)request.getSession().getAttribute("CoiInfoBean");
           if(coiInfoBean==null){
              coiInfoBean =new CoiInfoBean();
           } 
           request.getSession().removeAttribute("disableCoiMenu");
           request.getSession().setAttribute("toFEPage", true);
            projecType =  coiInfoBean.getProjectType();
            request.getSession().setAttribute("projectType",projecType);           
            if(projecType.equalsIgnoreCase(ANNUAL)||projecType.equalsIgnoreCase(REVISION)){
              Vector vecProjectDetails = setAnnualProjects(request);                        
              vecFilteredProject = filterProjects(vecProjectDetails,coiInfoBean,request);
              request.getSession().setAttribute("CoiAnnualProjects", vecFilteredProject);                          
              seperateProjectsToShow(request,vecFilteredProject);
               forward = "annual"; 
            }
              if(!projecType.equalsIgnoreCase(ANNUAL) && !projecType.equalsIgnoreCase(REVISION)){                
                 request.getSession().setAttribute("InPrgssCode",coiInfoBean.getEventType());                
                 /*To show project in financialEntByProject.jsp*/
                 Vector vecEventProjectList = (Vector) request.getSession().getAttribute("projectDetailsListInSeesion");
                 request.setAttribute("eventProjectDetails",vecEventProjectList);
                 request.getSession().setAttribute("eventProjectDetails",vecEventProjectList);
                /*end*/
                 Vector vecEventProjects =  getEventProject(request);                           
                 vecFilteredProject = filterProjects(vecEventProjects,coiInfoBean,request);
                 request.getSession().setAttribute("CoiAnnualProjects", vecFilteredProject);               
                 forward = "event"; 
              }
            if(actionMapping.getPath().equalsIgnoreCase(CONTINUE_FROM_QNR)){                
                forward = toFindNavigatorFromQnr(vecFilteredProject,forward,request,coiInfoBean);
            }
            if(actionMapping.getPath().equalsIgnoreCase(GET_BY_PROJECTS)){
               forward = toFindNavigatorFromProject(vecFilteredProject,forward,request,coiInfoBean); 
            }
  //New flow for Travel disclosure START 
   //The flow should be New Travel  Travel details Page – Questionnaire (if exists)  Attachments  Notes  Certification.         
            if(coiInfoBean!=null  && coiInfoBean.getProjectType().equalsIgnoreCase(TRAVEL)) {                
                saveProjectWithoutQnrFinEnt(request,vecFilteredProject,coiInfoBean);
                forward = "attachment";
            } 
  //New flow for Travel disclosure ENDS              
           
       // sub header details S T A R T S      
        PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId = person.getPersonID();
        getCoiPersonDetails(personId,request);
        String disclosureNumber=null;
        Integer sequenceNumber=null;
        disclosureNumber = coiInfoBean.getDisclosureNumber();
        sequenceNumber =coiInfoBean.getSequenceNumber();
        if(sequenceNumber == null){
            sequenceNumber =coiInfoBean.getApprovedSequence();
        }
        setApprovedDisclosureDetails(disclosureNumber,sequenceNumber,personId,request);     
       // sub header details E N D S
        //Menu Saved Start   
        request.setAttribute("byProjectMenu",true);
        if(coiInfoBean!=null){
        coiMenuDataSaved(coiInfoBean.getDisclosureNumber(),coiInfoBean.getSequenceNumber(),coiInfoBean.getPersonId(),request);
        }
     //Menu Saved ends 
        return actionMapping.findForward(forward);
    }
     private Vector getFinEntity(HttpServletRequest request) throws IOException, Exception {
       WebTxnBean webTxnBean = new WebTxnBean();
       PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute("person");
       HashMap hmData = new HashMap();
       hmData.put("personId", person.getPersonID());
       Hashtable projectDetailsList = (Hashtable) webTxnBean.getResults(request, "getFinEntityListCoiv2Bean", hmData);
        Vector finEntityList = (Vector) projectDetailsList.get("getFinEntityListCoiv2Bean");
        Vector finEntityListNew=new Vector();
        if(finEntityList!=null){
        for(int index=0;index<finEntityList.size();index++){
        if(((CoiFinancialEntityBean)finEntityList.get(index)).getStatusCode()==1){
            finEntityListNew.add(finEntityList.get(index));
        }
        }
        }
        request.getSession().setAttribute("finEntityComboList", finEntityListNew);
        return finEntityListNew;
    }    
   private Vector getFinancialEnt(HttpServletRequest request) throws Exception{
        Vector finEntityCombo = getFinEntity(request);
        request.setAttribute("financialEntityList", finEntityCombo);
        String[] entityCode = new String[finEntityCombo.size()];
        int i = 0;
        for (Iterator it1 = finEntityCombo.iterator(); it1.hasNext();) {
            CoiFinancialEntityBean entity = (CoiFinancialEntityBean) it1.next();
            entityCode[i] = entity.getCode();
            i++;
        }
        request.setAttribute("financialArrayEntityList", entityCode);//setting finacial entity code Array
        return finEntityCombo;
   }
   
   private Vector setAnnualProjects(HttpServletRequest request){
       HttpSession session = request.getSession();
       Vector vecProposal = new Vector();
       Vector vecInstProposal = new Vector();
       Vector vecProtocol = new Vector();
       Vector vecIACUCProtocol = new Vector();
       Vector vecAward = new Vector();
       Vector vecProjectDetails = new Vector();       
       if(session.getAttribute("proposalListForAttachment")!=null){
           vecProposal = (Vector) request.getSession().getAttribute("proposalListForAttachment");
           request.getSession().setAttribute("proposalList",vecProposal);       
       }
       if(session.getAttribute("getInstProposals")!=null){
           vecInstProposal = (Vector) request.getSession().getAttribute("getInstProposals");
       }
       if(session.getAttribute("protocolProjectListList")!=null){
           vecProtocol = (Vector) request.getSession().getAttribute("protocolProjectListList");
           request.getSession().setAttribute("protocolList",vecProtocol); 
       }
       if(session.getAttribute("getAllIACUCProtocolList")!=null){
           vecIACUCProtocol = (Vector) request.getSession().getAttribute("getAllIACUCProtocolList");
       }
       if(session.getAttribute("allAwardProjectList")!=null){
           vecAward = (Vector) request.getSession().getAttribute("allAwardProjectList");
           request.getSession().setAttribute("allAwardList",vecAward); 
       }
        if(vecProposal!=null && !vecProposal.isEmpty()){
            for (Iterator it = vecProposal.iterator(); it.hasNext();) {
                 CoiProposalBean coiProposalBean = (CoiProposalBean) it.next();
                 CoiAnnualPersonProjectDetails coiAnnualPersonProjectDetails= new CoiAnnualPersonProjectDetails();
                 coiAnnualPersonProjectDetails.setCoiProjectId(coiProposalBean.getProposalNumber());
                 coiAnnualPersonProjectDetails.setCoiProjectTitle(coiProposalBean.getTitle());
                 coiAnnualPersonProjectDetails.setCoiStatusCode(coiProposalBean.getCreationStatusCode());
                 coiAnnualPersonProjectDetails.setCoiProjectSponser(coiProposalBean.getSponsorName());
                 coiAnnualPersonProjectDetails.setSponsorCode(coiProposalBean.getSponsorCode());
                 coiAnnualPersonProjectDetails.setCoiProjectStartDate(coiProposalBean.getStartDate());
                 coiAnnualPersonProjectDetails.setCoiProjectEndDate(coiProposalBean.getEndDate());
                 coiAnnualPersonProjectDetails.setCoiProjectType(coiProposalBean.getProposalTypeDesc());
                 coiAnnualPersonProjectDetails.setAcType("I");                
                 coiAnnualPersonProjectDetails.setDataSaved(false);
                 coiAnnualPersonProjectDetails.setModuleCode(ModuleConstants.PROPOSAL_DEV_MODULE_CODE);
                 coiAnnualPersonProjectDetails.setModuleItemKey(coiProposalBean.getProposalNumber());         
                 vecProjectDetails.add(coiAnnualPersonProjectDetails);
            }
        }
       
        if(vecInstProposal!=null && !vecInstProposal.isEmpty()){
            for (Iterator it = vecInstProposal.iterator(); it.hasNext();) {
                 CoiProposalBean coiProposalBean = (CoiProposalBean) it.next();
                 CoiAnnualPersonProjectDetails coiAnnualPersonProjectDetails= new CoiAnnualPersonProjectDetails();
                 coiAnnualPersonProjectDetails.setCoiProjectId(coiProposalBean.getProposalNumber());
                 coiAnnualPersonProjectDetails.setCoiProjectTitle(coiProposalBean.getTitle());                 
                 coiAnnualPersonProjectDetails.setCoiProjectSponser(coiProposalBean.getSponsorName());          
                 coiAnnualPersonProjectDetails.setCoiProjectStartDate(coiProposalBean.getStartDate());
                 coiAnnualPersonProjectDetails.setCoiProjectEndDate(coiProposalBean.getEndDate());              
                 coiAnnualPersonProjectDetails.setPersonName(coiProposalBean.getPersonName());
                 coiAnnualPersonProjectDetails.setAcType("I");
                 coiAnnualPersonProjectDetails.setDataSaved(false);
                 coiAnnualPersonProjectDetails.setModuleCode(ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE);
                 coiAnnualPersonProjectDetails.setModuleItemKey(coiProposalBean.getProposalNumber());
                 vecProjectDetails.add(coiAnnualPersonProjectDetails);
            }
        }
        if(vecProtocol!=null && !vecProtocol.isEmpty()){
            for (Iterator it = vecProtocol.iterator(); it.hasNext();) {
                 CoiProtocolInfoBean coiProtocolInfoBean = (CoiProtocolInfoBean) it.next();
                 CoiAnnualPersonProjectDetails coiAnnualPersonProjectDetails= new CoiAnnualPersonProjectDetails();
                 coiAnnualPersonProjectDetails.setCoiProjectId(coiProtocolInfoBean.getProtocolNumber());
                 coiAnnualPersonProjectDetails.setCoiProjectTitle(coiProtocolInfoBean.getTitle());                 
                 coiAnnualPersonProjectDetails.setCoiProjectRole(coiProtocolInfoBean.getProjectRole());       
                 coiAnnualPersonProjectDetails.setCoiProjectStartDate(coiProtocolInfoBean.getApplicationDate());
                 coiAnnualPersonProjectDetails.setCoiProjectEndDate(coiProtocolInfoBean.getExpirationDate());              
                 coiAnnualPersonProjectDetails.setAcType("I");
                 coiAnnualPersonProjectDetails.setDataSaved(false);
                 coiAnnualPersonProjectDetails.setModuleCode(ModuleConstants.PROTOCOL_MODULE_CODE);
                 coiAnnualPersonProjectDetails.setModuleItemKey(coiProtocolInfoBean.getProtocolNumber());
                 vecProjectDetails.add(coiAnnualPersonProjectDetails);
            }
        }
         if(vecIACUCProtocol!=null && !vecIACUCProtocol.isEmpty()){
            for (Iterator it = vecIACUCProtocol.iterator(); it.hasNext();) {
                 CoiProtocolInfoBean coiProtocolInfoBean = (CoiProtocolInfoBean) it.next();
                 CoiAnnualPersonProjectDetails coiAnnualPersonProjectDetails= new CoiAnnualPersonProjectDetails();
                 coiAnnualPersonProjectDetails.setCoiProjectId(coiProtocolInfoBean.getProtocolNumber());
                 coiAnnualPersonProjectDetails.setCoiProjectTitle(coiProtocolInfoBean.getTitle());                 
                 coiAnnualPersonProjectDetails.setCoiProjectRole(coiProtocolInfoBean.getProjectRole());       
                 coiAnnualPersonProjectDetails.setCoiProjectStartDate(coiProtocolInfoBean.getApplicationDate());
                 coiAnnualPersonProjectDetails.setCoiProjectEndDate(coiProtocolInfoBean.getExpirationDate());              
                 coiAnnualPersonProjectDetails.setAcType("I");
                 coiAnnualPersonProjectDetails.setDataSaved(false);
                 coiAnnualPersonProjectDetails.setModuleCode(ModuleConstants.IACUC_MODULE_CODE);
                 coiAnnualPersonProjectDetails.setModuleItemKey(coiProtocolInfoBean.getProtocolNumber());
                 vecProjectDetails.add(coiAnnualPersonProjectDetails);
            }
        }
         if(vecAward!=null && !vecAward.isEmpty()){
            for (Iterator it = vecAward.iterator(); it.hasNext();) {
                 CoiAwardInfoBean coiAwardInfoBean = (CoiAwardInfoBean) it.next();
                 CoiAnnualPersonProjectDetails coiAnnualPersonProjectDetails= new CoiAnnualPersonProjectDetails();
                 coiAnnualPersonProjectDetails.setCoiProjectId(coiAwardInfoBean.getMitAwardNumber());
                 coiAnnualPersonProjectDetails.setCoiProjectTitle(coiAwardInfoBean.getAwardTitle());                 
                 coiAnnualPersonProjectDetails.setSequenceNumber(coiAwardInfoBean.getSequenceNumber());      
                 coiAnnualPersonProjectDetails.setCoiProjectStartDate(coiAwardInfoBean.getStartDate());
                 coiAnnualPersonProjectDetails.setCoiProjectEndDate(coiAwardInfoBean.getAwardExecutionDate());
                 coiAnnualPersonProjectDetails.setCoiProjectSponser(coiAwardInfoBean.getProjectSponsor());
                 coiAnnualPersonProjectDetails.setAccountNumber(coiAwardInfoBean.getAccountNumber());
                 coiAnnualPersonProjectDetails.setAwardTitle(coiAwardInfoBean.getTitle());
                 coiAnnualPersonProjectDetails.setAcType("I");
                 coiAnnualPersonProjectDetails.setDataSaved(false);
                 coiAnnualPersonProjectDetails.setModuleCode(ModuleConstants.AWARD_MODULE_CODE);
                 coiAnnualPersonProjectDetails.setModuleItemKey(coiAwardInfoBean.getMitAwardNumber());
                 vecProjectDetails.add(coiAnnualPersonProjectDetails);
            }
        }
            return vecProjectDetails;
   }
   private Vector getSavedProjects(HttpServletRequest request,CoiInfoBean coiInfoBean) throws Exception{
        Vector vecSavedProject = new Vector(); 
        HashMap hmData = new HashMap();
        hmData.put("disclosureNumber", coiInfoBean.getDisclosureNumber());
        hmData.put("sequenceNumber",coiInfoBean.getSequenceNumber());
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable projectDetailsList = (Hashtable) webTxnBean.getResults(request, "getCoiDisclosureDetails", hmData);
        vecSavedProject = (Vector) projectDetailsList.get("getCoiDisclosureDetails");        
      return vecSavedProject;
   }
 private Vector filterProjects(Vector vecProjectDetails,CoiInfoBean coiInfoBean,HttpServletRequest request) throws Exception{
     boolean isSaved =false;
     Vector vecFilteredProject = new Vector();     
     Vector finEntityCombo = getFinEntity(request);  
     String entityNumber = null;
     Integer entitySeqNumber = null;
     if (vecProjectDetails!=null && !vecProjectDetails.isEmpty()){
         for (Iterator it = vecProjectDetails.iterator(); it.hasNext();) {
              CoiAnnualPersonProjectDetails coiAnnualPersonProjectDetails= (CoiAnnualPersonProjectDetails) it.next();   
                  coiAnnualPersonProjectDetails.setCoiDisclosureNumber(coiInfoBean.getDisclosureNumber());
                  coiAnnualPersonProjectDetails.setSequenceNumber(coiInfoBean.getSequenceNumber());
                  if(finEntityCombo!=null && !finEntityCombo.isEmpty()){
                       for (Iterator ite = finEntityCombo.iterator(); ite.hasNext();) {
                            CoiFinancialEntityBean coiFinancialEntityBean =(CoiFinancialEntityBean)ite.next();
                             entityNumber = coiFinancialEntityBean.getEntityNumber();
                             entitySeqNumber =coiFinancialEntityBean.getSequenceNumber();
                             coiAnnualPersonProjectDetails.setEntityNumber(entityNumber);
                             coiAnnualPersonProjectDetails.setEntitySequenceNumber(entitySeqNumber.toString());
                             if(isProjectSaved(coiAnnualPersonProjectDetails,request)){
                                isSaved =true; 
                             }else{
                                 isSaved =false;
                                 break;
                             }
                       }
                      
                  }else{                      
                      coiAnnualPersonProjectDetails.setEntityNumber(null);
                      coiAnnualPersonProjectDetails.setEntitySequenceNumber(null);
                      if(isProjectSaved(coiAnnualPersonProjectDetails,request)){
                                isSaved =true; 
                             }else{
                                 isSaved =false;                                 
                             }
                  }        
                   if(isSaved){
                       coiAnnualPersonProjectDetails.setAcType("U");
                       coiAnnualPersonProjectDetails.setDataSaved(true);    
                   }else{
                       coiAnnualPersonProjectDetails.setAcType("I");
                       coiAnnualPersonProjectDetails.setDataSaved(false);
                   }
                
            coiAnnualPersonProjectDetails.setFlag(false); 
            vecFilteredProject.add(coiAnnualPersonProjectDetails); 
         }
     }
     return vecFilteredProject;
 }
 private void seperateProjectsToShow(HttpServletRequest request,Vector vecFilteredProject){     
     Vector vecDevProposal = new Vector();
     Vector vecInstProposal = new Vector();
     Vector vecIRBProtocol = new Vector();
     Vector vecIACUCProtocol = new Vector();
     Vector vecAward = new Vector();
     if(vecFilteredProject!=null && !vecFilteredProject.isEmpty()){
         for (Iterator it = vecFilteredProject.iterator(); it.hasNext();) {
             CoiAnnualPersonProjectDetails coiAnnualPersonProjectDetails= (CoiAnnualPersonProjectDetails) it.next();             
             if(coiAnnualPersonProjectDetails.getModuleCode().equals(ModuleConstants.PROPOSAL_DEV_MODULE_CODE)){
                 vecDevProposal.add(coiAnnualPersonProjectDetails);
             }
             else if(coiAnnualPersonProjectDetails.getModuleCode().equals(ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE)){
                 vecInstProposal.add(coiAnnualPersonProjectDetails);
             }
             else if(coiAnnualPersonProjectDetails.getModuleCode().equals(ModuleConstants.PROTOCOL_MODULE_CODE)){
                 vecIRBProtocol.add(coiAnnualPersonProjectDetails);
             }
             else if(coiAnnualPersonProjectDetails.getModuleCode().equals(ModuleConstants.IACUC_MODULE_CODE)){
                 vecIACUCProtocol.add(coiAnnualPersonProjectDetails);
             }
             else if(coiAnnualPersonProjectDetails.getModuleCode().equals(ModuleConstants.AWARD_MODULE_CODE)){
                 vecAward.add(coiAnnualPersonProjectDetails);
             }
            
         }
     }
      request.setAttribute("coiDevProposal", vecDevProposal);
      request.setAttribute("coiInstProposals", vecInstProposal);
      request.setAttribute("coiIRBprotocol", vecIRBProtocol);
      request.setAttribute("coiIACUCProtocol", vecIACUCProtocol);
      request.setAttribute("coiAward", vecAward);
 }
 private Vector getEventProject(HttpServletRequest request){
        Vector vecReturn =  new Vector();
        Vector vecEventProjects =  (Vector) request.getSession().getAttribute("projectDetailsListInSeesion");
        if(vecEventProjects!=null && !vecEventProjects.isEmpty()){
            for (Iterator it = vecEventProjects.iterator(); it.hasNext();) { 
                 CoiPersonProjectDetails coiPersonProjectDetails = (CoiPersonProjectDetails) it.next();
                 CoiAnnualPersonProjectDetails coiAnnualPersonProjectDetails= new CoiAnnualPersonProjectDetails();
                 coiAnnualPersonProjectDetails.setModuleCode(coiPersonProjectDetails.getModuleCode());
                 coiAnnualPersonProjectDetails.setCoiProjectTitle(coiPersonProjectDetails.getCoiProjectTitle());
                 coiAnnualPersonProjectDetails.setAwardTitle(coiPersonProjectDetails.getAwardTitle()); 
                  
                 if(coiPersonProjectDetails.getModuleCode() == 0){
                    coiAnnualPersonProjectDetails.setCoiProjectSponser(coiPersonProjectDetails.getCoiProjectSponser());                    
                    coiAnnualPersonProjectDetails.setCoiProjectTitle(coiPersonProjectDetails.getEventName());
                 }                 
                 coiAnnualPersonProjectDetails.setCoiProjectId(coiPersonProjectDetails.getModuleItemKey());                
                 coiAnnualPersonProjectDetails.setCoiProjectSponser(coiPersonProjectDetails.getCoiProjectSponser());
               
                 coiAnnualPersonProjectDetails.setCoiProjectStartDate(coiPersonProjectDetails.getCoiProjectStartDate());
                 coiAnnualPersonProjectDetails.setCoiProjectEndDate(coiPersonProjectDetails.getCoiProjectEndDate());
                 coiAnnualPersonProjectDetails.setCoiProjectType(coiPersonProjectDetails.getEventName());
                 coiAnnualPersonProjectDetails.setAcType("I");                
                 coiAnnualPersonProjectDetails.setDataSaved(false);                 
                 coiAnnualPersonProjectDetails.setModuleItemKey(coiPersonProjectDetails.getModuleItemKey());
                 vecReturn.add(coiAnnualPersonProjectDetails);
            }
        }
        return vecReturn;
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
  private String toFindNavigatorFromProject(Vector vecProjectDetails,String navigate,HttpServletRequest request,CoiInfoBean coiInfoBean) throws Exception{
         //  boolean isFinEnt = isFinEntity(request); // it check whethere the person has any Financial Entity(Active or InActive)                 
           Vector finEntityCombo  = getFinEntity(request); //  it check whethere the person has any Active Financial Entity
           if((finEntityCombo == null ||(finEntityCombo!=null && finEntityCombo.isEmpty()))){
               saveProjectWithoutQnrFinEnt(request,vecProjectDetails,coiInfoBean);
               navigate = "certify";
           }
      return navigate;
  }
   private String toFindNavigatorFromQnr(Vector vecProjectDetails,String navigate,HttpServletRequest request,CoiInfoBean coiInfoBean) throws Exception{
      Integer qstnId = 0;
      boolean qstnYesFlag = false;
     // Vector finEntityCombo = new Vector();
         if(request.getSession().getAttribute("coiQuestionnaireId")!=null){
              qstnId =(Integer)request.getSession().getAttribute("coiQuestionnaireId");
           }
           qstnYesFlag = checkQnrSavedYes(request,qstnId,coiInfoBean);
       //    finEntityCombo = getFinEntity(request); //  it check whethere the person has any Active Financial Entity
           boolean isFinEnt = isFinEntity(request); // it check whethere the person has any Financial Entity(Active or InActive)
          
           if(!qstnYesFlag){ //questionnaire NO
               if(!isFinEnt){
                  saveProjectWithoutQnrFinEnt(request,vecProjectDetails,coiInfoBean); 
                  navigate = "certify"; 
               }
               else{
                  navigate = "felistScreen"; 
               }
           }else{ //questionnaire Yes
                if(!isFinEnt){
                  navigate = "createFinEntity"; 
               }
               else{
                  navigate = "felistScreen"; 
               }
           }                     
      return navigate;
  }
   private void saveProjectWithoutQnrFinEnt(HttpServletRequest request,Vector vecProjectDetails,CoiInfoBean coiInfoBean) throws IllegalAccessException, IOException, Exception{
            WebTxnBean webTxnBean = new WebTxnBean();
            Vector saveProjectDetailsList = new Vector();
            String relationshipDescription = " ";
            String orgRelationshipDesc = " ";
            if(vecProjectDetails!=null && !vecProjectDetails.isEmpty()){
                for (Iterator it = vecProjectDetails.iterator(); it.hasNext();) {
                    CoiAnnualPersonProjectDetails coiPersonProjectDetails = (CoiAnnualPersonProjectDetails)it.next();
                       if(coiPersonProjectDetails!=null){
                        String finEntityNumber = null;
                        String finSquenceNumber = null;
                        String statusCode = COI_PROJECT_STATUS_CODE;                
                        coiPersonProjectDetails.setUpdateUser(USER_ID);
                        coiPersonProjectDetails.setEntityNumber(finEntityNumber);
                        coiPersonProjectDetails.setEntitySequenceNumber(finSquenceNumber);           
                        coiPersonProjectDetails.setRelationShipDescription(relationshipDescription);                         
                        coiPersonProjectDetails.setOrgRelationDescription(orgRelationshipDesc);
                        coiPersonProjectDetails.setCoiStatusCode(Integer.parseInt(statusCode));

                        coiPersonProjectDetails.setModuleItemKey(String.valueOf(coiPersonProjectDetails.getModuleItemKey()));
                        coiPersonProjectDetails.setCoiProjectId(coiPersonProjectDetails.getModuleItemKey());
                        if(coiPersonProjectDetails.isDataSaved()){
                           coiPersonProjectDetails.setAcType("U"); 
                        }else{
                        coiPersonProjectDetails.setAcType(coiPersonProjectDetails.getAcType());
                        }
                        PersonInfoBean personInfoBean = (PersonInfoBean) request.getSession().getAttribute("person");
                        coiPersonProjectDetails.setUpdateUser(personInfoBean.getUserName());
                        coiPersonProjectDetails.setCoiDisclosureNumber(coiInfoBean.getDisclosureNumber());
                        coiPersonProjectDetails.setSequenceNumber(coiInfoBean.getSequenceNumber());
                        coiPersonProjectDetails.setCoiDisclosureNumber(coiInfoBean.getDisclosureNumber());
                        coiPersonProjectDetails.setSequenceNumber(coiInfoBean.getSequenceNumber());            
                        saveProjectDetailsList.add(coiPersonProjectDetails);
                        if(saveProjectDetailsList!=null && !saveProjectDetailsList.isEmpty()){               
                                    if(coiPersonProjectDetails!=null){
                                        Integer discDetNumber = null;       
                                        HashMap hmData = new HashMap();
                                        hmData.put("disclosureNumber" , coiPersonProjectDetails.getCoiDisclosureNumber());
                                        hmData.put("sequenceNumber",coiPersonProjectDetails.getSequenceNumber());
                                        hmData.put("moduleItemKey" ,coiPersonProjectDetails.getModuleItemKey());
                                        hmData.put("entityNumber",coiPersonProjectDetails.getEntityNumber());
                                        Hashtable htGetDisclDetNum =
                                        (Hashtable)webTxnBean.getResults(request, "fnGetCoiDisclDetNum", hmData);
                                        HashMap hmGetDisclDetNum = (HashMap)htGetDisclDetNum.get("fnGetCoiDisclDetNum");
                                            if(hmGetDisclDetNum !=null && hmGetDisclDetNum.size()>0){
                                                discDetNumber = Integer.parseInt(hmGetDisclDetNum.get("li_return").toString()); 
                                                coiPersonProjectDetails.setCoiDiscDetails(discDetNumber);
                                            }
                                         webTxnBean.getResults(request, "updCoiDiscDetails", coiPersonProjectDetails);
                                         coiPersonProjectDetails.setDataSaved(true);
                                         coiPersonProjectDetails.setFlag(true);
                             } 
                          }
                       }
                    }                
                 }        
              }
   private boolean checkQnrSavedYes(HttpServletRequest request, int qnrId , CoiInfoBean coiInfoBean) throws Exception{
       boolean returnValue = false;
       WebTxnBean webTxnBean = new WebTxnBean();
       // If questionnaire Answer has 'Y'          
                     HashMap hmData = new HashMap();
                     Integer count = 0;                    
                     hmData.put("AV_MODULE_ITEM_KEY",coiInfoBean.getDisclosureNumber());
                     hmData.put("AV_QUESTIONNAIRE_ID", qnrId);
                     hmData.put("AV_MODULE_ITEM_CODE",ModuleConstants.ANNUAL_COI_DISCLOSURE);
                     hmData.put("AV_MODULE_SUB_ITEM_CODE",coiInfoBean.getEventType());
                     hmData.put("AV_MODULE_SUB_ITEM_KEY", coiInfoBean.getSequenceNumber().toString());
                    Hashtable htansYes = (Hashtable) webTxnBean.getResults(request, "getQuestionAnswerYes", hmData);
                    HashMap resultMap=(HashMap)htansYes.get("getQuestionAnswerYes");
                    if (resultMap!=null)
                    {
                         count=Integer.parseInt((resultMap.get("li_count")).toString());
                    }
                    if (count>0){                         
                         returnValue = true;                   
                    }else {                           
                         returnValue = false;
                    }
                // If questionnaire Answer has 'Y' 
       return returnValue;
   } 
   private boolean isProjectSaved(CoiAnnualPersonProjectDetails coiAnnualPersonProjectDetails,HttpServletRequest request) throws Exception{
         boolean returnValue = false;
         int retValue = 0;
         WebTxnBean webTxnBean = new WebTxnBean();
            HashMap hmData = new HashMap();
            hmData.put("disclosureNumber" , coiAnnualPersonProjectDetails.getCoiDisclosureNumber());
            hmData.put("sequenceNumber",coiAnnualPersonProjectDetails.getSequenceNumber());
            hmData.put("moduleItemKey" ,coiAnnualPersonProjectDetails.getModuleItemKey());
            hmData.put("entityNumber",coiAnnualPersonProjectDetails.getEntityNumber());
            hmData.put("entitySeqNumber",coiAnnualPersonProjectDetails.getEntitySequenceNumber());
            Hashtable htGetDisclDetNum =   (Hashtable)webTxnBean.getResults(request, "fnGetCoiDisclDetNum", hmData);
            HashMap hmGetDisclDetNum = (HashMap)htGetDisclDetNum.get("fnGetCoiDisclDetNum");
            if(hmGetDisclDetNum !=null && hmGetDisclDetNum.size()>0){
                   retValue = Integer.parseInt(hmGetDisclDetNum.get("li_return").toString()); 
                   if(retValue>0){
                    returnValue = true;
                   }
            }
            return returnValue;
   }
   private boolean isFinEntity(HttpServletRequest request) throws IOException, Exception {
       WebTxnBean webTxnBean = new WebTxnBean();
       boolean retValue = false;
       PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute("person");
       HashMap hmData = new HashMap();
       hmData.put("personId", person.getPersonID());
       Hashtable projectDetailsList = (Hashtable) webTxnBean.getResults(request, "getFinEntityListCoiv2Bean", hmData);
        Vector finEntityList = (Vector) projectDetailsList.get("getFinEntityListCoiv2Bean");        
        if(finEntityList!=null){
          retValue = true;
        }       
        return retValue;
    }
   private Vector saveProjectConflitStatus(HttpServletRequest request,Vector vecProjectDetails,CoiInfoBean coiInfoBean) throws IllegalAccessException, IOException, Exception{
        WebTxnBean webTxnBean = new WebTxnBean();        
        Vector finEntityComboList = getFinancialEnt(request);
            if (finEntityComboList == null || finEntityComboList.isEmpty()) {
                CoiFinancialEntityBean empty = new CoiFinancialEntityBean();
                finEntityComboList.add(empty);
            }
            if(vecProjectDetails!=null && !vecProjectDetails.isEmpty()){
                for (Iterator it = vecProjectDetails.iterator(); it.hasNext();) {
                    CoiAnnualPersonProjectDetails coiPersonProjectDetails = (CoiAnnualPersonProjectDetails)it.next();
           if(coiPersonProjectDetails!=null){                
                for(int i=0;i<finEntityComboList.size();i++){
            CoiFinancialEntityBean finEntity =(CoiFinancialEntityBean)finEntityComboList.get(i);
            String statusCode = COI_PROJECT_STATUS_CODE;
            String relationshipDescription = " ";
            String orgRelationshipDesc = " ";
            String[] finValueArr = null;
            String finEntityNumber = null;
            String finSquenceNumber = null;
            if (finEntity.getCode() != null && !finEntity.getCode().equals("")) {                
                finValueArr = finEntity.getCode().split(":");
                finEntityNumber = finValueArr[0];
                finSquenceNumber = finValueArr[1];
                relationshipDescription = finEntity.getRelationshipDescription();
                orgRelationshipDesc = finEntity.getOrgRelationDescription();
            } 
            coiPersonProjectDetails.setEntityNumber(finEntityNumber);
            coiPersonProjectDetails.setEntitySequenceNumber(finSquenceNumber);
            coiPersonProjectDetails.setUpdateUser(USER_ID);
            if(relationshipDescription == null){
                coiPersonProjectDetails.setRelationShipDescription("test");
            }else{
               coiPersonProjectDetails.setRelationShipDescription(relationshipDescription); 
            }
            
            coiPersonProjectDetails.setOrgRelationDescription(orgRelationshipDesc);

            if (statusCode != null) {
                coiPersonProjectDetails.setCoiStatusCode(Integer.parseInt(statusCode));
            }
            coiPersonProjectDetails.setModuleItemKey(String.valueOf(coiPersonProjectDetails.getModuleItemKey()));
            coiPersonProjectDetails.setCoiProjectId(coiPersonProjectDetails.getModuleItemKey());
            PersonInfoBean personInfoBean = (PersonInfoBean) request.getSession().getAttribute("person");
            coiPersonProjectDetails.setUpdateUser(personInfoBean.getUserName());
            coiPersonProjectDetails.setCoiDisclosureNumber(coiInfoBean.getDisclosureNumber());
            coiPersonProjectDetails.setSequenceNumber(coiInfoBean.getSequenceNumber());
            coiPersonProjectDetails.setCoiDisclosureNumber(coiInfoBean.getDisclosureNumber());
            coiPersonProjectDetails.setSequenceNumber(coiInfoBean.getSequenceNumber());
                        if(coiPersonProjectDetails!=null){
                            Integer discDetNumber = null;       
                            HashMap hmData = new HashMap();
                            hmData.put("disclosureNumber" , coiPersonProjectDetails.getCoiDisclosureNumber());
                            hmData.put("sequenceNumber",coiPersonProjectDetails.getSequenceNumber());
                            hmData.put("moduleItemKey" ,coiPersonProjectDetails.getModuleItemKey());
                            hmData.put("entityNumber",coiPersonProjectDetails.getEntityNumber());
                            hmData.put("entitySeqNumber",coiPersonProjectDetails.getEntitySequenceNumber());
                            Hashtable htGetDisclDetNum =
                            (Hashtable)webTxnBean.getResults(request, "fnGetCoiDisclDetNum", hmData);
                            HashMap hmGetDisclDetNum = (HashMap)htGetDisclDetNum.get("fnGetCoiDisclDetNum");
                            if(hmGetDisclDetNum !=null && hmGetDisclDetNum.size()>0){
                                discDetNumber = Integer.parseInt(hmGetDisclDetNum.get("li_return").toString()); 
                                if(discDetNumber > 0){
                                    coiPersonProjectDetails.setCoiDiscDetails(discDetNumber);
                                    coiPersonProjectDetails.setDataSaved(true);
                                    coiPersonProjectDetails.setAcType("U");
                                }else{
                                    coiPersonProjectDetails.setDataSaved(false);
                                    coiPersonProjectDetails.setAcType("I");
                                }
                            }else{
                                coiPersonProjectDetails.setDataSaved(false);
                                coiPersonProjectDetails.setAcType("I");
                            }                            
                             webTxnBean.getResults(request, "updCoiDiscDetails", coiPersonProjectDetails);
                             coiPersonProjectDetails.setDataSaved(true);
                             coiPersonProjectDetails.setFlag(true);
                  }
               }
            }
         }                
      }
       return vecProjectDetails ;   
       }
    
}
