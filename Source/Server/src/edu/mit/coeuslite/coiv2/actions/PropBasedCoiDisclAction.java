/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;


import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeuslite.coiv2.beans.CoiProposalBean;
import edu.mit.coeuslite.coiv2.formbeans.CoiPersonProjectDetailsForm;
import edu.mit.coeuslite.coiv2.services.CoiCommonService;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.beans.CoiInfoBean;
import edu.mit.coeuslite.coiv2.services.GettingRightsCoiV2Service;
import edu.mit.coeuslite.utils.DateUtils;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

/**
 *
 * @author Sony
 * To fetch the list of all proposals for the corresponding logged in user
 * This would further be used for Creating a Proposal Based Disclosure
 */
public class PropBasedCoiDisclAction extends COIBaseAction{
    private static String PROJECT_TYPE ="Proposal";

    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getSession().setAttribute("disableCoiMenu",true);// to disable the questionnaire menu
        request.setAttribute("byProjectMenu",true);
        request.getSession().removeAttribute("hasEnteredCoiNonQnr"); //checking whether was once entered or to without QNR ACTION  
        CoiCommonService coiCommonService1 = CoiCommonService.getInstance();
        request.getSession().setAttribute("projectType",PROJECT_TYPE);
        CoiInfoBean coiInfoBean = (CoiInfoBean)request.getSession().getAttribute("CoiInfoBean");
        if(coiInfoBean==null){
            coiInfoBean =new CoiInfoBean();
        }
        coiInfoBean.setProjectType(PROJECT_TYPE);
        coiInfoBean.setModuleCode(ModuleConstants.PROPOSAL_DEV_MODULE_CODE);
        coiInfoBean.setEventType(ModuleConstants.COI_EVENT_PROPOSAL);
        coiInfoBean.setMenuType("NewProposal");
        Integer disclosureAvailable = coiCommonService1.userHasDisclosure(request);
        if (disclosureAvailable > 0) {
            request.setAttribute("disclosureAvailableMessage", true);
            CoiDisclosureBean annualBean = new CoiDisclosureBean();
            annualBean = coiCommonService1.getAnnualDisclosure(request);
            request.setAttribute("annualDisclosureBean", annualBean);
        } else {
            request.setAttribute("disclosureAvailableMessage", false);
        }        
        HttpSession session = request.getSession();        
        session.removeAttribute("CREATEFLAG");
        session.removeAttribute("getinonce");
        session.removeAttribute("qnranswerd");
        PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId = person.getPersonID();
        getCoiPersonDetails(personId,request);
        String disclosureNumber=null;
        Integer sequenceNumber=null;
        disclosureNumber = coiInfoBean.getDisclosureNumber();
        sequenceNumber =coiInfoBean.getApprovedSequence();
        if(sequenceNumber == null){
            sequenceNumber =coiInfoBean.getSequenceNumber();
        }
        setApprovedDisclosureDetails(disclosureNumber,sequenceNumber,personId,request);                
      //Menu Saved Start   
        request.setAttribute("byProjectMenu",true);
        if(coiInfoBean!=null){
        coiMenuDataSaved(coiInfoBean.getDisclosureNumber(),coiInfoBean.getSequenceNumber(),coiInfoBean.getPersonId(),request);
        }
     //Menu Saved ends
        String actionForward = null;
        WebTxnBean webTxn = new WebTxnBean();
        ActionErrors actionErrors = new ActionErrors();        
        Hashtable propUserRole = (Hashtable) webTxn.getResults(request, "getProposalTypeList", null);
        Vector propsalType = new Vector();
        propsalType = (Vector) propUserRole.get("getProposalTypeList");
        request.setAttribute("propsalType", propsalType);
        if (actionMapping.getPath().equals("/saveNonIntegratedProposals")) {
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
            proposalBean.setRoleName(coiPersonProjectDetailsForm.getCoiProjectRole());
            try {
            String startdate = coiPersonProjectDetailsForm.getCoiProjectStartDate();
            Date formattedStartDate = new Date();
            DateUtils dt=new DateUtils();
            formattedStartDate=dt.getSQLDate(startdate);
            //proposalBean.setStartDate(dateFormat.parse(coiPersonProjectDetailsForm.getCoiProjectStartDate()));
            proposalBean.setStartDate(formattedStartDate);

            } catch (Exception e) {
                request.setAttribute("invaliddate", "Please enter a valid start date");
                actionErrors.add(ActionErrors.GLOBAL_ERROR, new ActionError("nonintegratedproposal.invalidstartdate"));
                saveErrors(request, actionErrors);
                return actionMapping.findForward("success");
            }
            proposalBean.setSponsorName(coiPersonProjectDetailsForm.getCoiProjectSponser());
            proposalBean.setTotalCost(coiPersonProjectDetailsForm.getCoiProjectFundingAmount());
            try {
                String endDate =coiPersonProjectDetailsForm.getCoiProjectEndDate();
                Date formattedEnddate = new Date();
                DateUtils dt=new DateUtils();
                formattedEnddate=dt.getSQLDate(endDate);
                proposalBean.setEndDate(formattedEnddate);
            //  proposalBean.setEndDate(df.parse(coiPersonProjectDetailsForm.getCoiProjectEndDate()));
            } catch (Exception e) {
                request.setAttribute("invaliddate", "Please enter a valid end date");
                actionErrors.add(ActionErrors.GLOBAL_ERROR, new ActionError("nonintegratedproposal.invalidenddate"));
                saveErrors(request, actionErrors);
                return actionMapping.findForward("success");
            }
            proposalBean.setCreateUser(createUser);
            proposalBean.setAcType("I");
            proposalBean.setNonIntegrated(true);

            //check Duplicate Project Id
            try{
                String projectIdStatus = EMPTY_STRING;
                Integer hasDuplicate = 1;
                Hashtable htprojStatusDetail = new Hashtable();
                HashMap hmprojData = new HashMap();
                String projid = "";
                WebTxnBean webTxnBean = new WebTxnBean();
                Hashtable projectDetailsList = (Hashtable) webTxnBean.getResults(request, "fngetNextCoiPjtId", null);
                HashMap hmfinEntityList = (HashMap) projectDetailsList.get("fngetNextCoiPjtId");
                if(hmfinEntityList !=null && hmfinEntityList.size()>0){
                    projid = hmfinEntityList.get("ls_seq").toString();
                }
               proposalBean.setProposalNumber(projid);
                if (proposalList.size()>0) {
                    for (Iterator it1 = proposalList.iterator(); it1.hasNext();) {
                        CoiProposalBean proposalGetBean = (CoiProposalBean) it1.next();
                        if (projid.equals(proposalGetBean.getProposalNumber())) {
                            request.setAttribute("message", "true1");
                            actionErrors.add(ActionErrors.GLOBAL_ERROR, new ActionError("nonintegratedproposal.invalidenddate"));
                            saveErrors(request, actionErrors);
                            return actionMapping.findForward("success");

                        }
                    }
                }


            } catch (Exception e) {

                  request.setAttribute("invaliddate", "Please enter another ProjectId");
                  actionErrors.add(ActionErrors.GLOBAL_ERROR, new ActionError("nonintegratedproposal.invalidenddate"));
                  saveErrors(request, actionErrors);
                 return actionMapping.findForward("success");
            }
           // return projectIdStatus;
           //End check
            proposalList.add(proposalBean);
            request.setAttribute("proposalList", proposalList);
            request.getSession().setAttribute("proposalProjectList", proposalList);
            BeanUtils.copyProperties(actionForm, new CoiPersonProjectDetailsForm());
        } else {
            String operation = request.getParameter("acType");
            request.getSession().removeAttribute("proposalProjectList");
            getAllProposals(request,coiInfoBean);
            if (operation != null && !operation.equals("null") && operation.equals("MODIFY")) {
                CoiCommonService coiCommonService = CoiCommonService.getInstance();
                disclosureNumber = (String) request.getSession().getAttribute("DisclosureNumberInUpdateSession");
                Integer seqNumber = (Integer) request.getSession().getAttribute("SequenceNumberInUpdateSession");
                Vector savedProjectList = coiCommonService.getProjects(disclosureNumber, seqNumber, request, "getProposalProjectsForDiscl");
               // Vector savedProjectList = new Vector();
                request.getSession().setAttribute("extCodeAlreadySavedProjects", savedProjectList);//saved list
                Vector projectSaved = new Vector();                
                Vector propsalList = (Vector) request.getAttribute("proposalList");
                if (savedProjectList != null) {
                    for (Iterator it = savedProjectList.iterator(); it.hasNext();) {
                        CoiProposalBean savedpropsalBean = (CoiProposalBean) it.next();
                        savedpropsalBean.setChecked(true);
                      for(int i=0;i<savedProjectList.size();i++){
                 CoiProposalBean coidiscBean =
                    (CoiProposalBean)savedProjectList.get(0);
                 String pjtname=coidiscBean.getTitle();
                 request.getSession().setAttribute("pjtname", pjtname);
                 String protocolNo=coidiscBean.getProposalNumber();
                 request.getSession().setAttribute("protocolNo", protocolNo);
            }
                        savedpropsalBean.setNonIntegrated(false);
                        projectSaved.add(savedpropsalBean);
                        if (propsalList != null) {
                            for (Iterator it1 = propsalList.iterator(); it1.hasNext();) {
                                CoiProposalBean proposalBean = (CoiProposalBean) it1.next();
                                if (savedpropsalBean.getProposalNumber().equals(proposalBean.getProposalNumber())) {
                                    propsalList.remove(proposalBean);
                                    break;
                                }
                            }
                        }
                    }
                }
                if (savedProjectList == null || savedProjectList.isEmpty()) {
                    savedProjectList = new Vector();
                    if (propsalList != null) {
                        String moduleItemKey = "";
                        if(request.getSession().getAttribute("selectedModuleItemKey") != null) {
                         moduleItemKey = request.getSession().getAttribute("selectedModuleItemKey").toString();
                        }
                        CoiProposalBean selectedPjt = new CoiProposalBean();
                        for (Iterator it = propsalList.iterator(); it.hasNext();) {
                            CoiProposalBean savedpropsalBean = (CoiProposalBean) it.next();
                           if( savedpropsalBean.getProposalNumber().trim().equals(moduleItemKey)) {
                               savedpropsalBean.setChecked(true);
                               selectedPjt = savedpropsalBean;
                               savedProjectList.add(savedpropsalBean);                              
                           }
                            
                        }
                        propsalList.remove(selectedPjt);
                        savedProjectList.addAll(propsalList);
                    }
                } else if (propsalList != null) {
                    savedProjectList.addAll(propsalList);
                }
                request.getSession().setAttribute("AlreadySavedProjectsForDiscl", projectSaved);
                request.getSession().setAttribute("AlreadySavedProjectsForExt", projectSaved);
                Integer projExtCode=1;
                request.getSession().setAttribute("projectEdit", projExtCode);
                request.setAttribute("proposalList", savedProjectList);
                request.getSession().setAttribute("proposalProjectList", savedProjectList);
                request.getSession().setAttribute("extCodeAlreadySavedProjects_upd", savedProjectList);//saved list
                if(session.getAttribute("extProjListAll")==null){
                    request.getSession().setAttribute("extProjListAll", savedProjectList);/// for prject all list
                }
                request.setAttribute("operation", "MODIFY");
            }
        }
        actionForward = "success";
        session.setAttribute("CoiInfoBean",coiInfoBean);
        return actionMapping.findForward(actionForward);
    }

  private Vector getAllProposals(HttpServletRequest request,CoiInfoBean coiInfoBean) throws Exception {
        HttpSession session = request.getSession();
        WebTxnBean webTxn = new WebTxnBean();
        Vector propUserDet = null;
        Vector propUser = null;
        Vector vecAllProp = new Vector();       
        PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId = person.getPersonID();
        String disclosureNumberNewCase=null;// when we create a proposal discloure first
        disclosureNumberNewCase=coiInfoBean.getDisclosureNumber();
        if(disclosureNumberNewCase==null){
            disclosureNumberNewCase = "-1";// when we create a proposal discloure first
        }
        HashMap hmData = new HashMap();
        hmData.put("createUser", personId);
         hmData.put("discnumber", disclosureNumberNewCase);
        Hashtable propUserRole = (Hashtable) webTxn.getResults(request, "getAllNewProposals", hmData);
        propUserDet = (Vector) propUserRole.get("getAllNewProposals");   
        
        
        String disclosureNumber=null;
        disclosureNumber=coiInfoBean.getDisclosureNumber();
        hmData = new HashMap();
        hmData.put("pid", personId);
        hmData.put("discnumber", disclosureNumber);
        Hashtable htpropUser = (Hashtable) webTxn.getResults(request, "getInstProposals", hmData);
        propUser = (Vector) htpropUser.get("getInstProposals");
        
        if (propUserDet != null && propUserDet.size() > 0) {
            vecAllProp.addAll(propUserDet);            
        }
         if (propUser != null && propUser.size() > 0) {
            vecAllProp.addAll(propUser);            
        }
         if (vecAllProp != null && vecAllProp.size() > 0) {
            request.setAttribute("proposalList", vecAllProp);
            request.getSession().setAttribute("proposalProjectList", vecAllProp);                    
        }       
        
            
        return propUserDet;
    }       

    private void setApprovedDisclosureDetails(String coiDisclosureNumber,Integer sequenceNumber,String personId,HttpServletRequest request) throws Exception {       
        HashMap hmData = new HashMap();
        hmData.put("coiDisclosureNumber", coiDisclosureNumber);
            if(coiDisclosureNumber==null)
            { hmData.put("sequenceNumber",0);
            }
            else
            {hmData.put("sequenceNumber", sequenceNumber);
            }
        hmData.put("personId", personId);
        WebTxnBean webTxn = new WebTxnBean();            
            Hashtable DisclData = (Hashtable) webTxn.getResults(request, "getDisclBySequnce", hmData);
            Vector DisclDet = (Vector) DisclData.get("getDisclBySequnce");
            if (DisclDet != null && DisclDet.size() > 0) {
                request.setAttribute("ApprovedDisclDetView", DisclDet);
            }
      
    }
}