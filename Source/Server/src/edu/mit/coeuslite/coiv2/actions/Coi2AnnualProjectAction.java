/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;

import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.coi.bean.ComboBoxBean;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeuslite.coiv2.beans.CoiAnnualPersonProjectDetails;
import edu.mit.coeuslite.coiv2.beans.CoiAwardInfoBean;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.beans.CoiFinancialEntityBean;
import edu.mit.coeuslite.coiv2.beans.CoiPersonProjectDetails;
import edu.mit.coeuslite.coiv2.beans.CoiProposalBean;
import edu.mit.coeuslite.coiv2.beans.CoiProtocolInfoBean;
import edu.mit.coeuslite.coiv2.formbeans.CoiPersonProjectDetailsForm;
import edu.mit.coeuslite.coiv2.services.CoiCommonService;
import edu.mit.coeuslite.coiv2.services.CoiProjectService;
import edu.mit.coeuslite.coiv2.services.GettingRightsCoiV2Service;
import edu.mit.coeuslite.coiv2.utilities.AnnualProjectSortingComparator;
import edu.mit.coeuslite.coiv2.utilities.CoiConstants;
import edu.mit.coeuslite.coiv2.utilities.ModuleCodeType;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author Lijo  Joy
 */
public class Coi2AnnualProjectAction extends COIBaseAction {

    private static final String Proposal_Based = "Proposal";
    private static final String Protocol_Based = "Protocol";
    private static final String Award_Based = "Award";
    private static final String Other = "Other";
    private static final String Annual = "Annual";
    private static final String Revision = "Revision";
    private static final String USER ="user";  

    /**
     *
     * Function to save project details
     * @param actionMapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //for annual disclosure menu change
        CoiCommonService coiCommonService1 = CoiCommonService.getInstance();
        Integer disclosureAvailable = coiCommonService1.userHasDisclosure(request);
        request.setAttribute("DiscViewByPrjt", true);//to check Project menu selected
        if (disclosureAvailable > 0) {
            request.setAttribute("disclosureAvailableMessage", true);
            CoiDisclosureBean annualBean = new CoiDisclosureBean();
            annualBean = coiCommonService1.getAnnualDisclosure(request);
            request.setAttribute("annualDisclosureBean", annualBean);
        } else {
            request.setAttribute("disclosureAvailableMessage", false);
        }
        //for annual disclosure menu change end
HttpSession session = request.getSession();
PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
String personId=person.getPersonID();
CoiDisclosureBean disclosureBean=getApprovedDisclosureBean(personId,request);
String disclosureNumber=disclosureBean.getCoiDisclosureNumber();
//Integer sequenceNumber=disclosureBean.getSequenceNumber();
//setApprovedDisclosureDetails(disclosureNumber,sequenceNumber,personId,request);
Integer sequenceNumber=null;
String disclosureNumb=null;
                if (request.getParameter("param1")!=null){
                  disclosureNumb=(String)request.getParameter("param1");
                }
                else if (session.getAttribute("param1")!=null){
                 disclosureNumb=(String)session.getAttribute("param1");
                }
                if(request.getParameter("param2")!=null){
                    sequenceNumber=Integer.parseInt(request.getParameter("param2"));
                    session.setAttribute("param2", sequenceNumber);
                    setApprovedDisclosureDetails(disclosureNumb,sequenceNumber,personId,request);
                }else if(session.getAttribute("param2")!=null){
                    sequenceNumber=(Integer) session.getAttribute("param2");
                    setApprovedDisclosureDetails(disclosureNumb,sequenceNumber,personId,request);
                }
       else if(session.getAttribute("fromQuestionnaire")!=null){
                CoiDisclosureBean currDisc = (CoiDisclosureBean) request.getSession().getAttribute("disclosureBeanSession");
                     Integer sequenceN=0;
                     String disclosureN = null;                     
                    if(currDisc!=null){
                      if(currDisc.getCoiDisclosureNumber() != null) {
                     disclosureN =(String)currDisc.getCoiDisclosureNumber();
                      }
                       if(currDisc.getSequenceNumber() != null) {
                       sequenceN = (Integer) currDisc.getSequenceNumber();
                       }
                      }
                    else if(currDisc==null){
                     disclosureN= (String)request.getSession().getAttribute("DisclNumber");
                     sequenceN=  (Integer)request.getSession().getAttribute("DisclSeqNumber");
                    }
                
                      sequenceNumber= (Integer) session.getAttribute("currentSequence");
                      if(session.getAttribute("currentSequence")==null){
                          sequenceNumber=sequenceN;
                      }
                      if(disclosureNumb==null){
                          disclosureNumb=disclosureN;
                      }
                      // NEW MENU CHANGE 
            if(disclosureNumb==null ||(disclosureNumb!=null && disclosureNumb.trim().length()<=0)){
                disclosureNumb = (String)session.getAttribute("COIDiscNumber");
            }
            // NEW MENU CHANGE 
            setApprovedDisclosureDetails(disclosureNumb,sequenceNumber,personId,request);

}
                // NEW MENU CHANGE 
           if(disclosureNumb==null ||(disclosureNumb!=null && disclosureNumb.trim().length()<=0)){
                disclosureNumb = (String)session.getAttribute("COIDiscNumber");
            }
           if(sequenceNumber == null){
              sequenceNumber =  (Integer)request.getSession().getAttribute("DisclSeqNumber");
           }
           // NEW MENU CHANGE 
        CoiProjectService coiProjectService = CoiProjectService.getInstance();
        CoiPersonProjectDetailsForm coiPersonProjectDetailsForm = (CoiPersonProjectDetailsForm) actionForm;
        GettingRightsCoiV2Service gettingRightsCoiV2Service = GettingRightsCoiV2Service.getInstance();        
        String operationType = request.getParameter("operationType");
        request.setAttribute("operationType", operationType);
        HashMap thisUserRights = (HashMap) request.getAttribute(RIGHT);
        Integer permissionType = (Integer) thisUserRights.get(CoiConstants.DISCL);
        Integer moduleCode = null;
        operationType = (String) request.getAttribute("operationType");
        // Function call to get project deatis and Financial entity
        if (actionMapping.getPath().equals("/getProjectDetailsAndFinEntityDetailsAnnualCoiv2")) {
            String success = "success";
            success = getProjectDetailsAndFinancialEntityDetails(request, actionMapping, actionForm);
 // enhancement for new Menu start 
   // request.getSession().setAttribute("frmPendingInPrg", "true");  
    //request.getSession().setAttribute("dontShowProjects", "false"); 
    setSavedDataMenu(request,personId);  
 // enhancement for new Menu ends  
            if(request.getSession().getAttribute("annualQstnFlag") != null && !success.equals("continue")) {
                String annualQstnFlag = request.getSession().getAttribute("annualQstnFlag").toString();
                if(annualQstnFlag != null && annualQstnFlag.equals("false")){
                         Vector finEntityCombo = new Vector();
//                if(finEntityCombo==null)
//                    {
                    PersonInfoBean person1 = (PersonInfoBean) request.getSession().getAttribute("person");
                    HashMap hmData1 = new HashMap();
                    hmData1.put("personId", person1.getPersonID());
                     finEntityCombo = getFinEntity(request, hmData1);
                      request.setAttribute("financialEntityList", finEntityCombo);//setting finacial entity
                    String[] entityCode = new String[finEntityCombo.size()];
                    int i = 0;
                    for (Iterator it1 = finEntityCombo.iterator(); it1.hasNext();) {
                        CoiFinancialEntityBean entity = (CoiFinancialEntityBean) it1.next();
                        entityCode[i] = entity.getCode();
                        i++;
                    }
                    request.setAttribute("financialArrayEntityList", entityCode);//setting finacial entity code Array
                    //Getting entity satus code to populate in combobox
                    WebTxnBean webTxnBean1 = new WebTxnBean();
                    Hashtable entityCodeList = (Hashtable) webTxnBean1.getResults(request, "getEntityStatusCode", hmData1);
                     Vector entityTypeList = (Vector)entityCodeList.get("getEntityStatusCode");
                    Vector entytyStatusList = filterEntityStatusCode(entityTypeList);
                    request.setAttribute("typeList", entytyStatusList);

                    CoiDisclosureBean disclosue = new CoiDisclosureBean();
                    operationType = (String) request.getAttribute("operationType");
                  //  operationType =null;
                    disclosue = getCurrentDisclPerson(request);
                    HashMap hmData = new HashMap();
                    hmData.put("personId", person.getPersonID());
                   // Vector finEntityCombo = new Vector();
                    finEntityCombo = getFinEntity(request, hmData);
                   if(finEntityCombo != null && finEntityCombo.size()> 0)
                 {
                   success = "success";
                 }
                 else
                 {
                    saveAnnualDisclosureDetailswithAnsNo(request,operationType,coiPersonProjectDetailsForm,permissionType,person,disclosue);
                    person = (PersonInfoBean) request.getSession().getAttribute("person");
                   success = "gotoAttachments";
                 }
                    if((session.getAttribute("checkedPropsalProjects")==null) &&(session.getAttribute("checkedAwardBasedProjects")==null )&&(session.getAttribute("checkedProtocolBasedProjects")==null )&&(session.getAttribute("checkedInstBasedProjects")==null )&&(session.getAttribute("checkedIacucPropBasedProjects")==null )){
                         success = "gotoAttachments";
                    }
                  //  success = "gotoNotes";
                    request.getSession().removeAttribute("annualQstnFlag");
                }
                else {
                     if(annualQstnFlag != null && annualQstnFlag.equals("true") && request.getSession().getAttribute("createEntity") == null) {
                       // create entity details if the user have no entities with one of thr answer for the question is "yes"
                        request.getSession().setAttribute("annualQstnFlag", annualQstnFlag);
                        request.setAttribute("mode", "add");
                        Vector finEntityCombo = (Vector)request.getAttribute("financialEntityList");
                                          if(finEntityCombo==null)
                    {
                    PersonInfoBean person1 = (PersonInfoBean) request.getSession().getAttribute("person");
                    HashMap hmData = new HashMap();
                    hmData.put("personId", person1.getPersonID());
                     finEntityCombo = getFinEntity(request, hmData);
                      request.setAttribute("financialEntityList", finEntityCombo);//setting finacial entity
                    String[] entityCode = new String[finEntityCombo.size()];
                    int i = 0;
                    for (Iterator it1 = finEntityCombo.iterator(); it1.hasNext();) {
                        CoiFinancialEntityBean entity = (CoiFinancialEntityBean) it1.next();
                        entityCode[i] = entity.getCode();
                        i++;
                    }
                    request.setAttribute("financialArrayEntityList", entityCode);//setting finacial entity code Array
                    //Getting entity satus code to populate in combobox
                    WebTxnBean webTxnBean1 = new WebTxnBean();
                    Hashtable entityCodeList = (Hashtable) webTxnBean1.getResults(request, "getEntityStatusCode", hmData);
                     Vector entityTypeList = (Vector)entityCodeList.get("getEntityStatusCode");
                    Vector entytyStatusList = filterEntityStatusCode(entityTypeList);
                    request.setAttribute("typeList", entytyStatusList);

                if(finEntityCombo != null && finEntityCombo.size()> 0)
                 {
                   success = "success";
                 }
                 else
                 {
                        CoiDisclosureBean disclosue = new CoiDisclosureBean();
                        disclosue = getCurrentDisclPerson(request);
                    saveAnnualDisclosureDetailswithAnsNo(request,operationType,coiPersonProjectDetailsForm,permissionType,person,disclosue);
                   success = "continue";
                 }
                    }
                        if(finEntityCombo == null || finEntityCombo.size() == 0) {
                            success = "createFinEntity";
                        }
                    }
                     if(request.getSession().getAttribute("createEntity") != null) {
                         request.getSession().removeAttribute("createEntity");
                     }
                }
                    }
//fix the issue financial entities page is showing if the user has no financial entities and questionnaire answers are no  starts
                   Vector finEntityCombo = new Vector();
                   HashMap hmData = new HashMap();
                   hmData.put("personId",personId);
                   finEntityCombo = this.getFinEntity(request, hmData);
                    if(request.getSession().getAttribute("annualQstnFlag") == null&&!(finEntityCombo.size()> 0) )
                    {
                        CoiDisclosureBean disclosue = new CoiDisclosureBean();
                        disclosue = getCurrentDisclPerson(request);
                        saveAnnualDisclosureDetailswithAnsNo(request,operationType,coiPersonProjectDetailsForm,permissionType,person,disclosue);
                       success = "gotoAttachments";
                    }
  //fix the issue if the user doesnot have any rights,projects to disclose,active FEs then project dosnot redirect to cereate FE page...starts
                    if((request.getSession().getAttribute("annualQstnFlag")!= null )&& (request.getSession().getAttribute("annualQstnFlag").equals("true"))&&( finEntityCombo.size() == 0)){
                    success = "createFinEntity";
                    }
                    if((request.getSession().getAttribute("annualQstnFlag")!= null )&& (request.getSession().getAttribute("annualQstnFlag").equals("true"))&&( finEntityCombo.size()> 0)){
                if((session.getAttribute("checkedPropsalProjects")==null) &&(session.getAttribute("checkedAwardBasedProjects")==null )&&(session.getAttribute("checkedProtocolBasedProjects")==null )&&(session.getAttribute("checkedInstBasedProjects")==null )&&(session.getAttribute("checkedIacucPropBasedProjects")==null )){
                         success = "gotoAttachments";
                    }
                else{   success = "success";}
                    }
                   if((request.getSession().getAttribute("annualQstnFlag")!= null )&& (request.getSession().getAttribute("annualQstnFlag").equals("false"))&&( finEntityCombo.size()> 0)){
                 success = "gotoAttachments";
                    }
                     if((request.getSession().getAttribute("annualQstnFlag")== null )&& (request.getAttribute("projectDetailsList")==null)){
                     success = "gotoAttachments";
                    }
                   
  //fix the issue if the user doesnot have any rights,projects to disclose,active FEs then project dosnot redirect to cereate FE page...ends
//fix the issue financial entities page is showing if the user has no financial entities and questionnaire answers are no  ends
       // new financial entity select saved data fix start    
                    hmData = new HashMap();                    
                    hmData.put("personId", person.getPersonID());
                    Vector finEntityComboNew  = getFinEntity(request, hmData);    
                    HashMap hmDiscData = new HashMap();
            if(finEntityComboNew!=null && finEntityComboNew.size()>0){
                       for (Iterator it1 = finEntityComboNew.iterator(); it1.hasNext();) {
                            CoiFinancialEntityBean entityDetail = (CoiFinancialEntityBean) it1.next();                     

                            if (entityDetail.getEntityNumber()!=null && entityDetail.getEntityNumber().trim().length() > 0){
                            hmDiscData.put("disclosureNumber", disclosureNumb);
                            hmDiscData.put("sequenceNumber", sequenceNumber);
                            hmDiscData.put("moduleItemKey", coiPersonProjectDetailsForm.getCoiProjectId());
                            hmDiscData.put("entityNumber", entityDetail.getEntityNumber());                    
                            Vector vecDiscDetailsData = getSavedDisclDetailsData(request, hmDiscData);
                                if(vecDiscDetailsData!=null){
                                  CoiFinancialEntityBean entityDetailMerge = (CoiFinancialEntityBean) vecDiscDetailsData.get(0); 
                                  entityDetail.setStatusCode(entityDetailMerge.getStatusCode());
                                  entityDetail.setRelationshipDescription(entityDetailMerge.getRelationshipDescription());
                                  entityDetail.setOrgRelationDescription(entityDetailMerge.getOrgRelationDescription());
                                }
                                else{
                                    entityDetail.setStatusCode(CoiConstants.COI_DISCLOSURE_STATUS_CODE);//  pending
                                }
                            }
                        }
                   }
             request.setAttribute("financialEntityList", finEntityComboNew);//setting finacial entity   
     // new financial entity select saved data fix end
                                
                   
                   
                   return actionMapping.findForward(success);
        } else if (actionMapping.getPath().equals("/annualSaveProjectDetails")) {
         
            request.getSession().removeAttribute("annualQstnFlag");
            String[] totalComments=request.getParameterValues("comments");
            Integer currentSeq=(Integer) request.getSession().getAttribute("currentSequence");
            if(currentSeq==null){
                currentSeq=(Integer) request.getSession().getAttribute("DisclSeqNumber");
            }
            CoiDisclosureBean discl = new CoiDisclosureBean();
            if ((operationType != null || !operationType.equals("null")) && !operationType.equals("MODIFY")) {
                discl = getCurrentDisclPerson(request);
                moduleCode = discl.getModuleCode();
            } else { ///Code for UPDATION STARTS
                String disclNumber = (String) request.getSession().getAttribute("DisclosureNumberInUpdateSession");
                Integer seqNumber = (Integer) request.getSession().getAttribute("SequenceNumberInUpdateSession");
                moduleCode = (Integer) request.getSession().getAttribute("ModuleCodeInUpdateSession");
              if(disclNumber==null){                  
                 disclNumber = (String) request.getSession().getAttribute("DisclNumber");         
              }
              if(seqNumber == null){
                 seqNumber= (Integer) request.getSession().getAttribute("DisclSeqNumber");
              }    
                
                discl.setCoiDisclosureNumber(disclNumber);
                discl.setSequenceNumber(seqNumber);
            } ///Code for UPDATION ENDS
  
          if(discl.getCoiDisclosureNumber()==null || (discl.getCoiDisclosureNumber()!=null && discl.getCoiDisclosureNumber().trim().length()<=0)){
                 String disNumber = (String) request.getSession().getAttribute("DisclNumber");    
                  discl.setCoiDisclosureNumber(disNumber);
                  Integer seNumber= (Integer) request.getSession().getAttribute("DisclSeqNumber");
                  discl.setSequenceNumber(seNumber);
          }
            String moduleItemKey = coiPersonProjectDetailsForm.getModuleItemKey();
            Vector projectDetailsList = (Vector) request.getSession().getAttribute("projectDetailsListInSeesion");
            Vector finEntityComboList = (Vector) request.getSession().getAttribute("finEntityComboList");
            if (finEntityComboList == null || finEntityComboList.isEmpty()) {
                CoiFinancialEntityBean empty = new CoiFinancialEntityBean();
                finEntityComboList.add(empty);
            }
            Vector saveProjectDetailsList = new Vector();
            String relationshipDescription = " ";
            String orgRelationshipDesc = " ";
if(projectDetailsList !=null && projectDetailsList.size()>0){
            for (Iterator it1 = projectDetailsList.iterator(); it1.hasNext();) {
                CoiAnnualPersonProjectDetails coiPersonProjectDetails = (CoiAnnualPersonProjectDetails) it1.next();
                request.setAttribute("ModuleCode", coiPersonProjectDetails.getModuleCode());
                if ((moduleCode != null && moduleCode.intValue() == ModuleConstants.COI_EVENT_OTHER) || coiPersonProjectDetails.getModuleItemKey() != null && coiPersonProjectDetails.getModuleItemKey().equals(moduleItemKey)) {
//                    for (Iterator it = finEntityComboList.iterator(); it.hasNext();) {
//                        ComboBoxBean finEntity = (ComboBoxBean) it.next();
                    for(int i=0;i<finEntityComboList.size();i++){
                        CoiFinancialEntityBean finEntity =(CoiFinancialEntityBean)finEntityComboList.get(i);
                        String statusCode = null;
                        String[] finValueArr = null;
                        String finEntityNumber = null;
                        String finSquenceNumber = null;
                        if (finEntity.getCode() != null && !finEntity.getCode().equals("")) {
                            statusCode = request.getParameter(finEntity.getCode());
                            finValueArr = finEntity.getCode().split(":");
                            finEntityNumber = finValueArr[0];
                            finSquenceNumber = finValueArr[1];
                             relationshipDescription = request.getParameter("relDesc"+finEntity.getCode());
                             orgRelationshipDesc = request.getParameter("orgRelDesc"+finEntity.getCode());
                        } else {
                            statusCode = "210";
                            finEntityNumber = null;
                            finSquenceNumber = null;
                            relationshipDescription = " ";
                            orgRelationshipDesc = " ";
                        }
                        CoiAnnualPersonProjectDetails coiPersonProjectDetailsSave = new CoiAnnualPersonProjectDetails();
                        BeanUtils.copyProperties(coiPersonProjectDetailsSave, coiPersonProjectDetails);
                        coiPersonProjectDetailsSave.setEntityNumber(finEntityNumber);
                        coiPersonProjectDetailsSave.setEntitySequenceNumber(finSquenceNumber);
                        coiPersonProjectDetailsSave.setRelationShipDescription(relationshipDescription);
                        coiPersonProjectDetailsSave.setOrgRelationDescription(orgRelationshipDesc);
                        //Integer lStatusCode=null;
//                        if(statusCode!=null){
//                          lStatusCode=Integer.parseInt(statusCode);
//                        }
//                        if(lStatusCode!=null && lStatusCode==2){
                        
//                        }else{
//                           coiPersonProjectDetailsSave.setComments("");
//                        }
                        if (statusCode != null) {
                            coiPersonProjectDetailsSave.setCoiStatusCode(Integer.parseInt(statusCode));
                        }
                        coiPersonProjectDetailsSave.setModuleItemKey(String.valueOf(coiPersonProjectDetails.getModuleItemKey()));
                        coiPersonProjectDetailsSave.setCoiProjectId(coiPersonProjectDetails.getModuleItemKey());
                        coiPersonProjectDetailsSave.setAcType("I");
                        PersonInfoBean personInfoBean = (PersonInfoBean) request.getSession().getAttribute("person");
                        coiPersonProjectDetailsSave.setUpdateUser(personInfoBean.getUserName());

                        if (coiPersonProjectDetails.getModuleCode() == ModuleConstants.PROPOSAL_DEV_MODULE_CODE) {
                            //Getting propsal from session
                            Vector propsalList=new Vector();
                            propsalList = (Vector) request.getSession().getAttribute("proposalProjectList");
                            coiPersonProjectDetailsSave.setNonIntegrated(false);
                            if (propsalList != null && !propsalList.isEmpty()) {
                            for (Iterator it2 = propsalList.iterator(); it2.hasNext();) {
                                CoiProposalBean propsalBean = (CoiProposalBean) it2.next();
                                if (propsalBean.getProposalNumber().equals(coiPersonProjectDetails.getModuleItemKey())) {
                                    coiPersonProjectDetailsSave.setNonIntegrated(propsalBean.isNonIntegrated());
                                    coiPersonProjectDetailsSave.setCoiProjectType(propsalBean.getProposalTypeDesc());
                                    coiPersonProjectDetailsSave.setCoiProjectTitle(propsalBean.getTitle());
                                    coiPersonProjectDetailsSave.setCoiProjectStartDate(propsalBean.getStartDate());
                                    coiPersonProjectDetailsSave.setCoiProjectSponser(propsalBean.getSponsorName());
                                    coiPersonProjectDetailsSave.setCoiProjectRole("TestRole");
                                    coiPersonProjectDetailsSave.setCoiProjectFundingAmount(propsalBean.getTotalCost());
                                    coiPersonProjectDetailsSave.setCoiProjectEndDate(propsalBean.getEndDate());
                                      coiPersonProjectDetailsSave.setModuleCode(ModuleConstants.PROPOSAL_DEV_MODULE_CODE);
                                }
                            }}
                        } else if (coiPersonProjectDetails.getModuleCode() == ModuleConstants.PROTOCOL_MODULE_CODE) {
                            //getting protocols from session
                            Vector protocolList =new Vector();
                            protocolList = (Vector) request.getSession().getAttribute("protocolProjectListList");
                            if (protocolList != null && !protocolList.isEmpty()) {
                            for (Iterator it2 = protocolList.iterator(); it2.hasNext();) {
                                CoiProtocolInfoBean protocolBean = (CoiProtocolInfoBean) it2.next();
                                if (protocolBean.getProtocolNumber().equals(coiPersonProjectDetails.getModuleItemKey())) {
                                     coiPersonProjectDetailsSave.setCoiProjectId(protocolBean.getProtocolNumber());
                                    coiPersonProjectDetailsSave.setModuleItemKey(protocolBean.getProtocolNumber());
                                }
                            }}
                        }  else if (coiPersonProjectDetails.getModuleCode() == ModuleConstants.IACUC_MODULE_CODE) {
                            //getting protocols from session
                            Vector protocolList =new Vector();
                            protocolList = (Vector) request.getSession().getAttribute("getAllIACUCProtocolList");
                            if (protocolList != null && !protocolList.isEmpty()) {
                            for (Iterator it2 = protocolList.iterator(); it2.hasNext();) {
                                CoiProtocolInfoBean protocolBean = (CoiProtocolInfoBean) it2.next();
                                if (protocolBean.getProtocolNumber().equals(coiPersonProjectDetails.getModuleItemKey())) {
                                     coiPersonProjectDetailsSave.setCoiProjectId(protocolBean.getProtocolNumber());
                                    coiPersonProjectDetailsSave.setModuleItemKey(protocolBean.getProtocolNumber());
                                }
                            }}
                        }
                        else if (coiPersonProjectDetails.getModuleCode() == ModuleConstants.AWARD_MODULE_CODE) {
                            // Getting awards from session
                            Vector awardList=new Vector();
                            awardList = (Vector) request.getSession().getAttribute("allAwardProjectList");
                            if (awardList != null && !awardList.isEmpty()) {
                            for (Iterator it2 = awardList.iterator(); it2.hasNext();) {
                                CoiAwardInfoBean awardBean = (CoiAwardInfoBean) it2.next();
                                if (awardBean.getMitAwardNumber().equals(coiPersonProjectDetails.getModuleItemKey())) {
                                     coiPersonProjectDetailsSave.setCoiProjectId(awardBean.getMitAwardNumber());
                                    coiPersonProjectDetailsSave.setModuleItemKey(awardBean.getMitAwardNumber());
                                }
                            }}
                        } else if (coiPersonProjectDetails.getModuleCode() == ModuleConstants.ANNUAL_COI_DISCLOSURE) {
                            // Getting annual from session
                            Vector propsalList = (Vector) request.getSession().getAttribute("proposalProjectList");
                            if (propsalList != null && !propsalList.isEmpty()) {
                                for (Iterator it2 = propsalList.iterator(); it2.hasNext();) {
                                    CoiProposalBean propsalBean = (CoiProposalBean) it2.next();
                                    if (propsalBean.getProposalNumber().equals(coiPersonProjectDetails.getModuleItemKey())) {
                                        if (propsalBean.getProposalTypeDesc() != null) {
                                            coiPersonProjectDetailsSave.setCoiProjectType(propsalBean.getProposalTypeDesc());
                                        }
                                        coiPersonProjectDetailsSave.setCoiProjectTitle(propsalBean.getTitle());
                                        coiPersonProjectDetailsSave.setCoiProjectStartDate(propsalBean.getStartDate());
                                        coiPersonProjectDetailsSave.setCoiProjectSponser(propsalBean.getSponsorName());
                                        coiPersonProjectDetailsSave.setCoiProjectRole("TestRole");
                                        coiPersonProjectDetailsSave.setCoiProjectFundingAmount(propsalBean.getTotalCost());
                                        coiPersonProjectDetailsSave.setCoiProjectEndDate(propsalBean.getEndDate());
                                         coiPersonProjectDetailsSave.setModuleCode(ModuleConstants.ANNUAL_COI_DISCLOSURE);
                                        coiPersonProjectDetailsSave.setAnnualProjectType("AnnualProposal");
                                    }
                                }
                            }
                            //getting protocols from session
                            Vector protocolList = (Vector) request.getSession().getAttribute("protocolProjectList");
                            if (protocolList != null && !protocolList.isEmpty()) {
                                for (Iterator it2 = protocolList.iterator(); it2.hasNext();) {
                                    CoiProtocolInfoBean protocolBean = (CoiProtocolInfoBean) it2.next();
                                    if (protocolBean.getProtocolNumber().equals(coiPersonProjectDetails.getModuleItemKey())) {
                                        coiPersonProjectDetailsSave.setCoiProjectId(protocolBean.getProtocolNumber());
                                        coiPersonProjectDetailsSave.setModuleItemKey(protocolBean.getProtocolNumber());
                                        coiPersonProjectDetailsSave.setAnnualProjectType("AnnualProtocol");
                                    }
                                }
                            }

                    //getting iacucprotocols from session

                            Vector iacucprotocolList = (Vector) request.getSession().getAttribute("getAllIACUCProtocolList");
                            if (iacucprotocolList != null && !iacucprotocolList.isEmpty()) {
                                for (Iterator it2 = iacucprotocolList.iterator(); it2.hasNext();) {
                                    CoiProtocolInfoBean protocolBean = (CoiProtocolInfoBean) it2.next();
                                    if (protocolBean.getProtocolNumber().equals(coiPersonProjectDetails.getModuleItemKey())) {
                                        coiPersonProjectDetailsSave.setCoiProjectId(protocolBean.getProtocolNumber());
                                        coiPersonProjectDetailsSave.setModuleItemKey(protocolBean.getProtocolNumber());
                                        coiPersonProjectDetailsSave.setAnnualProjectType("AnnualProtocol");
                                    }
                                }
                            }

                            // Getting awards from session
                            Vector awardList = (Vector) request.getSession().getAttribute("allAwardProjectList");
                            if (awardList != null && !awardList.isEmpty()) {
                                for (Iterator it2 = awardList.iterator(); it2.hasNext();) {
                                    CoiAwardInfoBean awardBean = (CoiAwardInfoBean) it2.next();
                                    if (awardBean.getMitAwardNumber().equals(coiPersonProjectDetails.getModuleItemKey())) {
                                        coiPersonProjectDetailsSave.setCoiProjectId(awardBean.getMitAwardNumber());
                                        coiPersonProjectDetailsSave.setModuleItemKey(awardBean.getMitAwardNumber());
                                        coiPersonProjectDetailsSave.setCoiProjectStartDate(awardBean.getStartDate());
                                        coiPersonProjectDetailsSave.setCoiProjectEndDate(awardBean.getAwardExecutionDate());
                                        coiPersonProjectDetailsSave.setAnnualProjectType("AnnualAward");
                                    }
                                }
                            }
                        }
                        coiPersonProjectDetailsSave.setCoiDisclosureNumber(discl.getCoiDisclosureNumber());
                        coiPersonProjectDetailsSave.setSequenceNumber(currentSeq);
                        coiPersonProjectDetails.setCoiDisclosureNumber(discl.getCoiDisclosureNumber());
                        coiPersonProjectDetails.setSequenceNumber(discl.getSequenceNumber());
                        coiPersonProjectDetails.setDataSaved(true);
                        saveProjectDetailsList.add(coiPersonProjectDetailsSave);

                    }
                    break;
                }

            }}
            WebTxnBean webTxnBean = new WebTxnBean();
            // Saving project details
            if ((moduleCode == null || moduleCode.intValue() != 0) && saveProjectDetailsList != null && !saveProjectDetailsList.isEmpty()/* && permissionType == 2*/) {
                String tempId = null;
                boolean repeatSave = false;
                Collections.sort(saveProjectDetailsList, new AnnualProjectSortingComparator());
                for (Iterator it = saveProjectDetailsList.iterator(); it.hasNext();) {
                    CoiAnnualPersonProjectDetails project = (CoiAnnualPersonProjectDetails) it.next();
                    if (tempId == null || !project.getCoiProjectId().equals(tempId)) {
                        tempId = project.getCoiProjectId();
                         if ((operationType.equals("null") || operationType != null) && !operationType.equals("MODIFY")&& !repeatSave)  { ///Code for UPDATION STARTS
                            coiProjectService.removeProjectAndDetails(discl.getCoiDisclosureNumber(),currentSeq, project.getModuleItemKey(), "updateRemoveAllProject", request);
                        } ///Code for UPDATION ENDS
                    } else {
                        project.setAcType("N");
                        tempId = project.getCoiProjectId();
                    }
                    if(project.isNonIntegrated()){
                       project.setAcType("I");
                    }else{
                       project.setAcType("N");
                    }
                    if(repeatSave){
                       project.setAcType("N");
                    }
                    //Calling function to save project details
                    webTxnBean.getResults(request, "updateAnnualCoiProjectDetailsCoiv2", project);
                    repeatSave = true;
                }

            } else if (moduleCode != null && moduleCode == 0) {
                boolean onceDeleted = false;
                for (Iterator it = saveProjectDetailsList.iterator(); it.hasNext();) {
                    CoiAnnualPersonProjectDetails project = (CoiAnnualPersonProjectDetails) it.next();
                    if (operationType != null && operationType.equals("MODIFY") && onceDeleted == false) { ///Code for UPDATION STARTS
                        onceDeleted = true;
                        coiProjectService.removeProjectAndDetails(discl.getCoiDisclosureNumber(), discl.getSequenceNumber(), null, "updateRemoveAllNonProject", request);
                    } ///Code for UPDATION ENDS
                    project.setAcType("N");
                     //Calling function to save project details
                    webTxnBean.getResults(request, "updateAnnualCoiProjectDetailsCoiv2", project);
                }
            }
   //Annual In progress fix start   
            setSavedDataMenu(request,personId);        
    //Annual In progress fix end     
   boolean hasEntered = false;

            request.getSession().setAttribute("projectDetailsListInSeesion", projectDetailsList);
            CoiCommonService coiCommSrvc = CoiCommonService.getInstance();
            //Getting next project from the List
            String[] entityCode=null;
            if(projectDetailsList !=null && projectDetailsList.size()>0){
                int projectNum = 0;
            for (Iterator it = projectDetailsList.iterator(); it.hasNext();) {
                CoiAnnualPersonProjectDetails coiPersonProjectDetails = (CoiAnnualPersonProjectDetails) it.next();
                projectNum += 1;
                if (coiPersonProjectDetails.isDataSaved() == false) {
                    coiPersonProjectDetailsForm.setModuleItemKey(coiPersonProjectDetails.getModuleItemKey());
//                    String moduleitemkey=coiPersonProjectDetails.getModuleItemKey();
//                    if(moduleitemkey==null)
//                    {coiPersonProjectDetailsForm.setModuleItemKey(coiPersonProjectDetails.getCoiProjectId());}
                    request.setAttribute("projectNum", projectNum);
                    coiPersonProjectDetailsForm.setCoiProjectTitle(coiPersonProjectDetails.getCoiProjectTitle());
                    coiPersonProjectDetailsForm.setCoiProjectId(coiPersonProjectDetails.getCoiProjectId());
                    coiPersonProjectDetailsForm.setCoiProjectType(coiPersonProjectDetails.getCoiProjectType());
                    coiPersonProjectDetailsForm.setModuleCode(coiPersonProjectDetails.getModuleCode());

                    if(coiPersonProjectDetails.getCoiProjectStartDate() != null) {
                        coiPersonProjectDetailsForm.setCoiProjectStartDate(coiCommSrvc.getFormatedDate(coiPersonProjectDetails.getCoiProjectStartDate()));
                    }else {
                       coiPersonProjectDetailsForm.setCoiProjectStartDate("");
                    }
                    if(coiPersonProjectDetails.getCoiProjectEndDate() != null) {
                        coiPersonProjectDetailsForm.setCoiProjectEndDate(coiCommSrvc.getFormatedDate(coiPersonProjectDetails.getCoiProjectEndDate()));
                    }else {
                       coiPersonProjectDetailsForm.setCoiProjectEndDate("") ;
                    }
                    person = (PersonInfoBean) request.getSession().getAttribute("person");
                    HashMap hmData = new HashMap();
                    hmData.put("personId", person.getPersonID());
                    //Get financial Entity
               //     Vector finEntityCombo = new Vector();
              //      finEntityCombo = getFinEntity(request, hmData);//calling function to get financail entity                    
              //      request.setAttribute("financialEntityList", finEntityCombo);//setting finacial entity
 // new financial entity select saved data fix start    
                    hmData = new HashMap();                    
                    hmData.put("personId", person.getPersonID());
                    Vector finEntityComboNew  = getFinEntity(request, hmData);  
                    entityCode = new String[finEntityComboNew.size()];
                    int i = 0;
                    HashMap hmDiscData = new HashMap();
            if(finEntityComboNew!=null && finEntityComboNew.size()>0){
                       for (Iterator it1 = finEntityComboNew.iterator(); it1.hasNext();) {
                            CoiFinancialEntityBean entityDetail = (CoiFinancialEntityBean) it1.next();                     
                            entityCode[i] = entityDetail.getCode(); 
                            i++;  
                            if (entityDetail.getEntityNumber()!=null && entityDetail.getEntityNumber().trim().length() > 0){
                            hmDiscData.put("disclosureNumber", disclosureNumb);
                            hmDiscData.put("sequenceNumber", sequenceNumber);
                            hmDiscData.put("moduleItemKey", coiPersonProjectDetailsForm.getCoiProjectId());
                            hmDiscData.put("entityNumber", entityDetail.getEntityNumber());                    
                            Vector vecDiscDetailsData = getSavedDisclDetailsData(request, hmDiscData);
                                if(vecDiscDetailsData!=null){
                                  CoiFinancialEntityBean entityDetailMerge = (CoiFinancialEntityBean) vecDiscDetailsData.get(0); 
                                  entityDetail.setStatusCode(entityDetailMerge.getStatusCode());
                                  entityDetail.setRelationshipDescription(entityDetailMerge.getRelationshipDescription());
                                  entityDetail.setOrgRelationDescription(entityDetailMerge.getOrgRelationDescription());
                                }
                                 else{
                                    entityDetail.setStatusCode(CoiConstants.COI_DISCLOSURE_STATUS_CODE);//  pending
                                }
                            }
                        }
                   }
             request.setAttribute("financialEntityList", finEntityComboNew);//setting finacial entity   
     // new financial entity select saved data fix end                   
                
                    request.setAttribute("financialArrayEntityList", entityCode);//setting finacial entity code Array
                    //Getting entity satus code to populate in combobox
                    Hashtable entityCodeList = (Hashtable) webTxnBean.getResults(request, "getEntityStatusCode", hmData);
                     Vector entityTypeList = (Vector)entityCodeList.get("getEntityStatusCode");
                    Vector entytyStatusList = filterEntityStatusCode(entityTypeList);
                    request.setAttribute("typeList", entytyStatusList);
                    hasEntered = true;

                    ///Code for UPDATION STARTS
                    CoiCommonService coiCommonService = CoiCommonService.getInstance();
                    String transactoinId = "getAnnualProjects";
                    String disclNumber = (String) request.getSession().getAttribute("param1");
                    Integer seqNumber = (Integer) request.getSession().getAttribute("DisclSeqNumber");
                    Vector deatilsofProject = coiCommonService.getProjectDetails(disclNumber, currentSeq, coiPersonProjectDetails.getModuleItemKey(), request, transactoinId);
                        request.setAttribute("projectDetails", deatilsofProject);
//                    if (operationType != null && operationType.equals("MODIFY")) {
//                        String disclNumber = (String) request.getSession().getAttribute("DisclosureNumberInUpdateSession");
//                        Integer seqNumber = (Integer) request.getSession().getAttribute("SequenceNumberInUpdateSession");
//                        String transactoinId = "";
//                        if (coiPersonProjectDetails.getModuleCode() == ModuleCodeType.propsal.getValue()) {
//                            transactoinId = "getPropPjtDetForDiscl";
//                        }
//                        if (coiPersonProjectDetails.getModuleCode() == ModuleCodeType.award.getValue()) {
//                            transactoinId = "getAwardDetForDiscl";
//                        }
//                        if (coiPersonProjectDetails.getModuleCode() == ModuleCodeType.protocol.getValue()) {
//                            transactoinId = "getProtoPjtDetForDiscl";
//                        }
//                        Vector deatilsofProject = coiCommonService.getProjectDetails(disclNumber, seqNumber, coiPersonProjectDetails.getModuleItemKey(), request, transactoinId);
//                        request.setAttribute("projectDetails", deatilsofProject);
//                    }
                    ///Code for UPDATION ENDS

                    break;
                }
            }}           
            if (hasEntered == true) {

               if(entityCode!=null&& entityCode.length>0) {
                  coiPersonProjectDetailsForm.setCoiStatusCode(entityCode[0]);
                }
         String projectType = (String) request.getSession().getAttribute("projectType");
         request.setAttribute("projectType", projectType);
                return actionMapping.findForward("success");
            } else {
                ///Code for UPDATION STARTS
                if (operationType.equals("MODIFY") && permissionType == 2) {
     //               removeUncheckedProjects(request);

                }
                ///Code for UPDATION ENDS               
            }
        } else if (actionMapping.getPath().equals("/findDisclosureProjectType")) {
            disclosureNumber = request.getParameter("param1");
            Integer sequenceNumber1 = null;
            if(request.getParameter("param2")!= null){
            sequenceNumber1 = Integer.parseInt(request.getParameter("param2"));
            request.getSession().setAttribute("currentSequence", sequenceNumber1);
            }
            moduleCode = coiProjectService.getModulecode(disclosureNumber, sequenceNumber1, request);
            request.getSession().setAttribute("DisclosureNumberInUpdateSession", disclosureNumber);
            request.getSession().setAttribute("SequenceNumberInUpdateSession", sequenceNumber);
            request.getSession().setAttribute("ModuleCodeInUpdateSession", moduleCode);
            //For Temoprary use only..Need to remove after finding solution
            if (moduleCode == null) {
                moduleCode = ModuleConstants.COI_EVENT_PROPOSAL;
            }
            //END For Temoprary use only..Need to remove after finding solution
            if (moduleCode.intValue() == ModuleConstants.COI_EVENT_PROPOSAL) {
                return actionMapping.findForward("successToPropsal");
            } else if (moduleCode.intValue() == ModuleConstants.COI_EVENT_PROTOCOL) {
                return actionMapping.findForward("successToProtocol");
            } else if (moduleCode.intValue() == ModuleConstants.COI_EVENT_AWARD) {
                return actionMapping.findForward("successToAward");
            } else if (moduleCode.intValue() == ModuleConstants.COI_EVENT_OTHER) {
                return actionMapping.findForward("successToOther");
            } else if (moduleCode.intValue() == ModuleConstants.COI_EVENT_ANNUAL) {
                return actionMapping.findForward("successToAnnual");
            }else if (moduleCode == ModuleConstants.COI_EVENT_IACUC) {
                return actionMapping.findForward("successToIacucProtocol");}
        }
        return actionMapping.findForward("continue");
    }

    /**
     * Function to get current disclosre of a person
     * @param request
     * @return
     * @throws Exception
     */
    private CoiDisclosureBean getCurrentDisclPerson(HttpServletRequest request) throws Exception {

        PersonInfoBean personInfoBean = (PersonInfoBean) request.getSession().getAttribute("person");
        String personId = personInfoBean.getPersonID();
        HashMap hmreviewData = new HashMap();
        hmreviewData.put("personId", personId);
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htCurrDiscl =
                (Hashtable) webTxnBean.getResults(request, "getCurrDisclPerCoiv2", hmreviewData);
        Vector vecDiscl = (Vector) htCurrDiscl.get("getCurrDisclPerCoiv2");
        CoiDisclosureBean discl = new CoiDisclosureBean();
        if (vecDiscl != null && vecDiscl.size() > 0) {
            for (int i = 0; i < vecDiscl.size(); i++) {
                discl = (CoiDisclosureBean) vecDiscl.get(0);
            }
        }
        return discl;
    }

    /**
     * Function to get project details as per project type And also getting Finacial Entity
     * @param hmFinData
     * @param actionForm
     * @param request
     * @param actionMapping
     * @return
     * @throws Exception
     */
    private String getProjectDetailsAndFinancialEntityDetails(HttpServletRequest request, ActionMapping actionMapping, ActionForm actionForm) throws Exception {
        HttpSession session = request.getSession();      
         String success = "success";
        String operationType = request.getParameter("operationType");
        PersonInfoBean person = (PersonInfoBean) session.getAttribute("person");
        HashMap hmData = new HashMap();
        hmData.put("personId", person.getPersonID());
        CoiDisclosureBean currDisclosure = this.getCurrentDisclPerson(request);
        request.getSession().setAttribute("disclosureBeanSession", currDisclosure);
        String projectType = (String) request.getSession().getAttribute("projectType");
         request.setAttribute("projectType", projectType);
        HashMap projectDetailArg = new HashMap();
        projectDetailArg.put("disclosurenumber", currDisclosure.getCoiDisclosureNumber());
        projectDetailArg.put("sequencenumber", currDisclosure.getSequenceNumber());
        WebTxnBean webTxnBean = new WebTxnBean();
        Vector projectDetails = new Vector();
        CoiAnnualPersonProjectDetails projectDet = new CoiAnnualPersonProjectDetails();
        Integer modulecode = null;
        HashMap thisUserRights = (HashMap) request.getAttribute(RIGHT);
        Integer permissionType = (Integer) thisUserRights.get(CoiConstants.DISCL);

         if (projectType.equals(Annual)||projectType.equals(Revision)) {
             session.setAttribute("disclPjctModuleName", "Annual");
            modulecode = ModuleConstants.ANNUAL_COI_DISCLOSURE;
            request.getSession().setAttribute("projectType", projectType);
            String[] checkedPropsalProjects = request.getParameterValues("checkedPropsalProjects");
            String[] checkedAwardBasedProjects = request.getParameterValues("checkedAwardBasedProjects");
            String[] checkedProtocolBasedProjects = request.getParameterValues("checkedProtocolBasedProjects");
            String[] checkedInstPropBasedProjects = request.getParameterValues("checkedInstPropsalProjects");
            String[] checkedIacucPropBasedProjects = request.getParameterValues("checkedIacucProtocolBasedProject");

            request.getSession().setAttribute("checkedPropsalProjects", checkedPropsalProjects);
            request.getSession().setAttribute("checkedAwardBasedProjects", checkedAwardBasedProjects);
            request.getSession().setAttribute("checkedProtocolBasedProjects", checkedProtocolBasedProjects);
            request.getSession().setAttribute("checkedInstBasedProjects", checkedInstPropBasedProjects);
            request.getSession().setAttribute("checkedIacucPropBasedProjects", checkedIacucPropBasedProjects);
            //return "continue";
            projectDetailArg.put("modulecode", modulecode);

            if ((checkedPropsalProjects == null || checkedPropsalProjects.length == 0) && (checkedProtocolBasedProjects == null || checkedProtocolBasedProjects.length == 0) && (checkedAwardBasedProjects == null || checkedAwardBasedProjects.length == 0) && (checkedIacucPropBasedProjects == null || checkedIacucPropBasedProjects.length == 0) && (checkedInstPropBasedProjects == null || checkedInstPropBasedProjects.length == 0) && permissionType == 2) {
                request.removeAttribute("projectDetailsListInSeesion");
                if (operationType != null && operationType.equals("MODIFY")) {
                    request.removeAttribute("projectDetailsListInSeesion");
                    request.setAttribute("ModuleCode", modulecode);
        //            removeUncheckedProjects(request);
                }                
                if(request.getSession().getAttribute("annualQstnFlag") != null && !request.getSession().getAttribute("annualQstnFlag").equals("false")){
                    success= "continue";
                }

                 return success;

            } else if (permissionType == 1) {
//                add by roshin start
                CoiCommonService coiCommonService = CoiCommonService.getInstance();
                String disclosureNumber = (String) request.getSession().getAttribute("DisclosureNumberInUpdateSession");
                Integer seqNumber = (Integer) request.getSession().getAttribute("SequenceNumberInUpdateSession");
                Vector savedProjectList = coiCommonService.getProjects(disclosureNumber, seqNumber, request, "getProposalProjectsForDiscl");
                Vector projectSaved = new Vector();

                Vector propsalList = (Vector) request.getAttribute("proposalList");
                if (savedProjectList != null) {
                    for (Iterator it = savedProjectList.iterator(); it.hasNext();) {
                        CoiProposalBean savedpropsalBean = (CoiProposalBean) it.next();
                        savedpropsalBean.setChecked(true);
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
//                add by roshin end
                ///  Vector projectSaved = (Vector) request.getSession().getAttribute("AlreadySavedProjectsForDiscl");
                if (projectSaved != null) {
                    int k = 0;
                    checkedPropsalProjects = new String[projectSaved.size()];
                    for (Iterator it = projectSaved.iterator(); it.hasNext();) {
                        CoiProposalBean savedpropsalBean = (CoiProposalBean) it.next();
                        checkedPropsalProjects[k] = savedpropsalBean.getProposalNumber() + ":" + savedpropsalBean.getTitle();
                        k++;
                    }
                } else {
                    request.removeAttribute("projectDetailsListInSeesion");
                    if (operationType != null && operationType.equals("MODIFY")) {
                        request.removeAttribute("projectDetailsListInSeesion");
                        request.setAttribute("ModuleCode", modulecode);
              //         removeUncheckedProjects(request);
                    }
                    String annualQstnFlagCheck = request.getSession().getAttribute("annualQstnFlag").toString();

                    if(annualQstnFlagCheck != null && !annualQstnFlagCheck.equals("false")){
                        success= "continue";
                    }

                     return success;

                    }
            } else if (permissionType == 0 && ((checkedPropsalProjects == null || checkedPropsalProjects.length == 0)&&
                    (checkedAwardBasedProjects == null || checkedAwardBasedProjects.length == 0)&&
                    (checkedProtocolBasedProjects == null || checkedProtocolBasedProjects.length == 0)&& (checkedIacucPropBasedProjects == null || checkedIacucPropBasedProjects.length == 0) && (checkedInstPropBasedProjects == null || checkedInstPropBasedProjects.length == 0))) {
                request.removeAttribute("projectDetailsListInSeesion");
                if (operationType != null && operationType.equals("MODIFY")) {
                    request.removeAttribute("projectDetailsListInSeesion");
                    request.setAttribute("ModuleCode", modulecode);
               //     removeUncheckedProjects(request);
                }
                 String annualQstnFlagCheck = (String)request.getSession().getAttribute("annualQstnFlag");

                if(annualQstnFlagCheck != null && !annualQstnFlagCheck.equals("false")){
                    success= "continue";
                }

                 return success;

            }
            //setting Proposal Based project to project List
           if (checkedPropsalProjects != null) {
                for (int i = 0; i < checkedPropsalProjects.length; i++) {
                    try{
                    String propVal = checkedPropsalProjects[i];
                    String[] propValArr = propVal.split(";");
                    projectDet = new CoiAnnualPersonProjectDetails();
                    if(propValArr!=null && propValArr.length>0)
                    projectDet.setCoiProjectId(propValArr[0]);
                    projectDet.setCoiProjectTitle(propValArr[1]);
                    projectDet.setCoiProjectType("Proposal");

                    if(propValArr.length > 2) {
                    Date date = null;
                     String d=propValArr[2];

                    try {
                     DateFormat formatter ;
                      formatter = new SimpleDateFormat("yyyy-MM-dd");
                      date = formatter.parse(d);

                       } catch (ParseException e){}
                        projectDet.setCoiProjectStartDate(date);
                    } else {
                        projectDet.setCoiProjectStartDate(null);
                    }

                    if(propValArr.length > 3) {
                         Date date1=null;
                        String d1=propValArr[3];
                        try {
                          DateFormat formatter ;
                          formatter = new SimpleDateFormat("yyyy-MM-dd");
                          date1 = formatter.parse(d1);
                           } catch (ParseException e)
                          {}
                        projectDet.setCoiProjectEndDate(date1);
                    } else {
                        projectDet.setCoiProjectEndDate(null);
                    }
                    modulecode =  ModuleConstants.PROPOSAL_DEV_MODULE_CODE;
                    projectDet.setModuleCode(modulecode);
                    if(propValArr!=null){
                         projectDet.setModuleItemKey(propValArr[0]);
                    }

                    projectDetails.add(projectDet);
                    }
                    catch (Exception ex) {
                        Logger.getLogger(Coi2ProjectAction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            // set Institute proposal list
             if (checkedInstPropBasedProjects != null) {
                for (int i = 0; i < checkedInstPropBasedProjects.length; i++) {
                    try{
                    String propVal = checkedInstPropBasedProjects[i];
                    String[] propValArr = propVal.split(";");
                    projectDet = new CoiAnnualPersonProjectDetails();
                    if(propValArr!=null && propValArr.length>0)
                    projectDet.setCoiProjectId(propValArr[0]);
                    projectDet.setCoiProjectTitle(propValArr[1]);
                    projectDet.setCoiProjectType("Proposal");

                    if(propValArr.length > 2) {
                        Date date = null;
                         String d=propValArr[2];
                        try {
                              DateFormat formatter ;
                              formatter = new SimpleDateFormat("yyyy-MM-dd");
                              date = (Date)formatter.parse(d);
                        } catch (ParseException e){}
                         projectDet.setCoiProjectStartDate(date);
                    }else{
                        projectDet.setCoiProjectStartDate(null);
                    }

                    if(propValArr.length > 3) {
                         Date date1 = null;
                        String d1=propValArr[3];
                        try {
                          DateFormat formatter ;
                          formatter = new SimpleDateFormat("yyyy-MM-dd");
                          date1 = (Date)formatter.parse(d1);
                        } catch (ParseException e){}
                        projectDet.setCoiProjectEndDate(date1);
                    }else {
                       projectDet.setCoiProjectEndDate(null);
                    }
                    modulecode =  ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE;
                    projectDet.setModuleCode(modulecode);
                    if(propValArr!=null){
                         projectDet.setModuleItemKey(propValArr[0]);
                    }

                    projectDetails.add(projectDet);
                    }
                    catch (Exception ex) {
                        Logger.getLogger(Coi2ProjectAction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            // set IACUC protocol list
            if (checkedIacucPropBasedProjects != null) {
                for (int i = 0; i < checkedIacucPropBasedProjects.length; i++) {
                    try{
                    String propVal = checkedIacucPropBasedProjects[i];
                    String[] iacucpropValArr = propVal.split(";");
                    projectDet = new CoiAnnualPersonProjectDetails();
                    if(iacucpropValArr!=null && iacucpropValArr.length>0)
                    projectDet.setCoiProjectId(iacucpropValArr[0]);
                    projectDet.setCoiProjectTitle(iacucpropValArr[1]);
                    projectDet.setCoiProjectType("Protocol");

                    if(iacucpropValArr.length > 2) {
                        Date date3 = null;
                         String d3=iacucpropValArr[2];
                        try {
                          DateFormat formatter ;
                          formatter = new SimpleDateFormat("yyyy-MM-dd");
                          date3 = (Date)formatter.parse(d3);
                           } catch (ParseException e)
                          {}
                        projectDet.setCoiProjectStartDate(date3);
                    }
                    else {
                      projectDet.setCoiProjectStartDate(null);
                    }

                    if(iacucpropValArr.length > 3) {
                      Date date4 = null;
                     String d4=iacucpropValArr[3];
                    try {
                      DateFormat formatter ;
                      formatter = new SimpleDateFormat("yyyy-MM-dd");
                      date4 = (Date)formatter.parse(d4);
                       } catch (ParseException e)
                      {}projectDet.setCoiProjectEndDate(date4);
                    } else {
                       projectDet.setCoiProjectEndDate(null);
                    }
                    modulecode =  ModuleConstants.IACUC_MODULE_CODE;
                    projectDet.setModuleCode(modulecode);
                    if(iacucpropValArr!=null){
                         projectDet.setModuleItemKey(iacucpropValArr[0]);
                    }

                    projectDetails.add(projectDet);
                    }
                    catch (Exception ex) {
                        Logger.getLogger(Coi2ProjectAction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }


            if (checkedProtocolBasedProjects != null) {
                for (int i = 0; i < checkedProtocolBasedProjects.length; i++) {
                    String protocolVal = checkedProtocolBasedProjects[i];
                    String[] protocolValArr = protocolVal.split(";");
                    projectDet = new CoiAnnualPersonProjectDetails();
                    if(protocolValArr!=null && protocolValArr.length>0)
                    projectDet.setCoiProjectId(protocolValArr[0]);
                    projectDet.setCoiProjectTitle(protocolValArr[1]);
                    projectDet.setCoiProjectType("Protocol");

                    if(protocolValArr.length > 2) {
                        Date date5 = null;
                         String d5=protocolValArr[2];
                        try {
                              DateFormat formatter ;
                              formatter = new SimpleDateFormat("yyyy-MM-dd");
                              date5 = (Date)formatter.parse(d5);
                               } catch (ParseException e)
                              {}
                            projectDet.setCoiProjectStartDate(date5);
                    }else {
                        projectDet.setCoiProjectStartDate(null);
                    }

                    if(protocolValArr.length > 3) {
                      Date date6 = null;
                     String d6=protocolValArr[3];
                    try {
                      DateFormat formatter ;
                      formatter = new SimpleDateFormat("yyyy-MM-dd");
                      date6 = (Date)formatter.parse(d6);
                       } catch (ParseException e)
                      {}projectDet.setCoiProjectEndDate(date6);

                    }else {
                        projectDet.setCoiProjectEndDate(null);
                    }
                    modulecode =  ModuleConstants.PROTOCOL_MODULE_CODE;
                    projectDet.setModuleCode(modulecode);
                    if(protocolValArr!=null)
                    projectDet.setModuleItemKey(protocolValArr[0]);
                    projectDetails.add(projectDet);
                }
            }
            if (checkedAwardBasedProjects != null) {
                for (int i = 0; i < checkedAwardBasedProjects.length; i++) {
                    String awardVal = checkedAwardBasedProjects[i];
                    String[] awardValValArr = awardVal.split(";");
                    projectDet = new CoiAnnualPersonProjectDetails();
                    if(awardValValArr!=null){
                      projectDet.setCoiProjectId(awardValValArr[0]);
                      projectDet.setModuleItemKey(awardValValArr[0]);
                      projectDet.setCoiProjectTitle(awardValValArr[1]);
                      projectDet.setCoiProjectType("Award");

                        if(awardValValArr.length > 2) {
                         Date date7 = null;
                         String d7=awardValValArr[2];
                        try {
                          DateFormat formatter ;
                          formatter = new SimpleDateFormat("yyyy-MM-dd");
                          date7 = (Date)formatter.parse(d7);
                           } catch (ParseException e){}
                         projectDet.setCoiProjectStartDate(date7);
                        }else {
                          projectDet.setCoiProjectStartDate(null);
                        }
                      if(awardValValArr.length > 3) {
                         Date date8 = null;
                         String d8=awardValValArr[3];
                        try {
                          DateFormat formatter ;
                          formatter = new SimpleDateFormat("yyyy-MM-dd");
                          date8 = (Date)formatter.parse(d8);
                           } catch (ParseException e){}
                         projectDet.setCoiProjectEndDate(date8);
                        }
                      else {
                          projectDet.setCoiProjectEndDate(null);
                      }
                    }

                     modulecode =  ModuleConstants.AWARD_MODULE_CODE;
                    projectDet.setModuleCode(modulecode);
                    projectDetails.add(projectDet);
                }
            }
        }
        
        request.getSession().setAttribute("projectDetailsListInSeesion", projectDetails);//keeping project list in session
        request.setAttribute("projectDetailsList", projectDetails);
        //Get financial Entity
        Vector finEntityCombo = new Vector();
        hmData = new HashMap();
        hmData.put("personId", person.getPersonID());
        finEntityCombo = getFinEntity(request, hmData);//calling function to get financail entity
        String[] entityCode = new String[finEntityCombo.size()];
        int i = 0;
        for (Iterator it = finEntityCombo.iterator(); it.hasNext();) {
            CoiFinancialEntityBean entity = (CoiFinancialEntityBean) it.next();
            entityCode[i] = entity.getCode();
            i++;
        }
        request.setAttribute("financialArrayEntityList", entityCode);//setting finacial entity code Array
        request.setAttribute("financialEntityList", finEntityCombo);//setting finacial entity
        //Getting entity satus code to populate in combobox
        Hashtable entityCodeList = (Hashtable) webTxnBean.getResults(request, "getEntityStatusCode", hmData);
         Vector entityTypeList = (Vector)entityCodeList.get("getEntityStatusCode");
            Vector entytyStatusList = filterEntityStatusCode(entityTypeList);
            request.setAttribute("typeList", entytyStatusList);

        CoiCommonService coiCommonService = CoiCommonService.getInstance();
        if ((projectDetails == null || projectDetails.isEmpty()) && projectType.equals(Other)) {
            CoiAnnualPersonProjectDetails coiPersonProjectDetails = new CoiAnnualPersonProjectDetails();
            coiPersonProjectDetails.setModuleCode(ModuleConstants.COI_EVENT_OTHER);
            projectDetails.add(coiPersonProjectDetails);
            request.setAttribute("projectDetailsList", projectDetails);
        }
        int projectNum = 0;
        for (Iterator it = projectDetails.iterator(); it.hasNext();) {
            projectNum += 1;
            CoiAnnualPersonProjectDetails coiPersonProjectDetails = (CoiAnnualPersonProjectDetails) it.next();
            if (coiPersonProjectDetails.isDataSaved() == false) {
                request.setAttribute("projectNum", projectNum);
                CoiPersonProjectDetailsForm coiPersonProjectDetailsForm = (CoiPersonProjectDetailsForm) actionForm;
                coiPersonProjectDetailsForm.setModuleItemKey(coiPersonProjectDetails.getModuleItemKey());
                coiPersonProjectDetailsForm.setCoiProjectId(coiPersonProjectDetails.getCoiProjectId());
                coiPersonProjectDetailsForm.setCoiProjectTitle(coiPersonProjectDetails.getCoiProjectTitle());
                coiPersonProjectDetailsForm.setCoiProjectType(coiPersonProjectDetails.getCoiProjectType());
                coiPersonProjectDetailsForm.setModuleCode(coiPersonProjectDetails.getModuleCode());

                if(coiPersonProjectDetails.getCoiProjectStartDate() != null) {
                    coiPersonProjectDetailsForm.setCoiProjectStartDate(coiCommonService.getFormatedDate(coiPersonProjectDetails.getCoiProjectStartDate()));
                } else {
                   coiPersonProjectDetailsForm.setCoiProjectStartDate("") ;
                }
                if(coiPersonProjectDetails.getCoiProjectEndDate() != null) {
                    coiPersonProjectDetailsForm.setCoiProjectEndDate(coiCommonService.getFormatedDate(coiPersonProjectDetails.getCoiProjectEndDate()));
                }
                else {
                     coiPersonProjectDetailsForm.setCoiProjectEndDate("");
                }

                //CODE FOR UPDATION STARTS
                if (operationType != null && operationType.equals("MODIFY")) {
                    String disclNumber = (String) request.getSession().getAttribute("DisclosureNumberInUpdateSession");
                    Integer seqNumber = (Integer) request.getSession().getAttribute("SequenceNumberInUpdateSession");
                    String transactoinId = "";
                    if (coiPersonProjectDetails.getModuleCode() == ModuleConstants.PROPOSAL_DEV_MODULE_CODE) {
                        transactoinId = "getPropPjtDetForDiscl";
                    }
                    if (coiPersonProjectDetails.getModuleCode() == ModuleConstants.AWARD_MODULE_CODE) {
                        transactoinId = "getAwardDetForDiscl";
                    }
                    if (coiPersonProjectDetails.getModuleCode() == ModuleConstants.PROTOCOL_MODULE_CODE) {
                        transactoinId = "getProtoPjtDetForDiscl";
                    }                  
                    if (coiPersonProjectDetails.getModuleCode() == ModuleConstants.ANNUAL_COI_DISCLOSURE) {
                        transactoinId = "getAnnualProjects";
                    }
                    if (coiPersonProjectDetails.getModuleCode() == ModuleConstants.IACUC_MODULE_CODE) {
                        transactoinId = "getIcProtoPjtDetForDiscl";
                    }
                  if(transactoinId!=null && transactoinId.trim().length()>0){
                    Vector deatilsofProject = coiCommonService.getProjectDetails(disclNumber, seqNumber, coiPersonProjectDetails.getModuleItemKey(), request, transactoinId);
                    request.setAttribute("projectDetails", deatilsofProject);
                  }else{
                      transactoinId = "getAnnualProjects";
                      disclNumber = (String) request.getSession().getAttribute("param1");
                      seqNumber = (Integer) request.getSession().getAttribute("DisclSeqNumber");
                    Vector deatilsofProject = coiCommonService.getProjectDetails(disclNumber, seqNumber, coiPersonProjectDetails.getModuleItemKey(), request, transactoinId);
                    request.setAttribute("projectDetails", deatilsofProject);
                  }
                }else
                {
                    String transactoinId = "getAnnualProjects";
                     String disclNumber = (String) request.getSession().getAttribute("param1");
                    Integer seqNumber = (Integer) request.getSession().getAttribute("DisclSeqNumber");
                    Vector deatilsofProject = coiCommonService.getProjectDetails(disclNumber, seqNumber, coiPersonProjectDetails.getModuleItemKey(), request, transactoinId);
                    request.setAttribute("projectDetails", deatilsofProject);
                }
                //CODE FOR UPDATION ENDS
               break;
            }
        }

        return "success";
    }

    private Vector getFinEntity(HttpServletRequest request, HashMap hmData) throws IOException, Exception {
       WebTxnBean webTxnBean = new WebTxnBean();
       Hashtable projectDetailsList = (Hashtable) webTxnBean.getResults(request, "getFinEntityListCoiv2Bean", hmData);
        Vector finEntityList = (Vector) projectDetailsList.get("getFinEntityListCoiv2Bean");
        Vector finEntityListNew=new Vector();
        if(finEntityList!=null){
        for(int index=0;index<finEntityList.size();index++){
        if(((CoiFinancialEntityBean)finEntityList.get(index)).getStatusCode()==1){
            finEntityListNew.add(finEntityList.get(index));}
        }
            }
        request.getSession().setAttribute("finEntityComboList", finEntityListNew);
        return finEntityListNew;
    }

    /**
     * Function to remove uncheckd project while UPDATE
     * @param request
     */
    private void removeUncheckedProjects(HttpServletRequest request) {
        CoiProjectService coiProjectService = CoiProjectService.getInstance();
        Integer moduleCode = (Integer) request.getAttribute("ModuleCode");
        Vector savedProjects = (Vector) request.getSession().getAttribute("AlreadySavedProjectsForDiscl");
        Vector projectDetailsList = (Vector) request.getSession().getAttribute("projectDetailsListInSeesion");
        String disclNumber = (String) request.getSession().getAttribute("DisclosureNumberInUpdateSession");
        Integer seqNumber = (Integer) request.getSession().getAttribute("SequenceNumberInUpdateSession");
        if (moduleCode == ModuleConstants.COI_EVENT_PROPOSAL) {

            for (Iterator it2 = savedProjects.iterator(); it2.hasNext();) {
                CoiProposalBean propsalBean = (CoiProposalBean) it2.next();
                boolean present = false;
                if (projectDetailsList != null) {
                    for (Iterator it = projectDetailsList.iterator(); it.hasNext();) {
                        CoiAnnualPersonProjectDetails coiPersonProjectDetails = (CoiAnnualPersonProjectDetails) it.next();
                        if (coiPersonProjectDetails.getModuleItemKey().equals(propsalBean.getProposalNumber())) {
                            present = true;
                            break;
                        }
                    }
                }
                if (present == false) {
                    try {
                        coiProjectService.removeProjectAndDetails(disclNumber, seqNumber, propsalBean.getProposalNumber(), "updateRemoveAllProject", request);
                    } catch (Exception ex) {
                        Logger.getLogger(Coi2ProjectAction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } else if (moduleCode == ModuleConstants.COI_EVENT_PROTOCOL) {

            for (Iterator it2 = savedProjects.iterator(); it2.hasNext();) {
                CoiProtocolInfoBean protocolBean = (CoiProtocolInfoBean) it2.next();
                boolean present = false;
                if (projectDetailsList != null) {
                    for (Iterator it = projectDetailsList.iterator(); it.hasNext();) {
                        CoiAnnualPersonProjectDetails coiPersonProjectDetails = (CoiAnnualPersonProjectDetails) it.next();
                        if (coiPersonProjectDetails.getModuleItemKey().equals(protocolBean.getProtocolNumber())) {
                            present = true;
                            break;
                        }
                    }
                }
                if (present == false) {
                    try {
                        coiProjectService.removeProjectAndDetails(disclNumber, seqNumber, protocolBean.getProtocolNumber(), "updateRemoveAllProject", request);
                    } catch (Exception ex) {
                        Logger.getLogger(Coi2ProjectAction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } else if (moduleCode == ModuleConstants.COI_EVENT_AWARD) {

            for (Iterator it2 = savedProjects.iterator(); it2.hasNext();) {
                CoiAwardInfoBean awardBean = (CoiAwardInfoBean) it2.next();
                boolean present = false;
                if (projectDetailsList != null) {
                    for (Iterator it = projectDetailsList.iterator(); it.hasNext();) {
                        CoiAnnualPersonProjectDetails coiPersonProjectDetails = (CoiAnnualPersonProjectDetails) it.next();
                        if (coiPersonProjectDetails.getModuleItemKey().equals(awardBean.getMitAwardNumber())) {
                            present = true;
                            break;
                        }
                    }
                }
                if (present == false) {
                    try {
                        coiProjectService.removeProjectAndDetails(disclNumber,seqNumber, awardBean.getMitAwardNumber(), "updateRemoveAllProject", request);
                    } catch (Exception ex) {
                        Logger.getLogger(Coi2ProjectAction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

    }
            //get Approved DisclosureBean
    public CoiDisclosureBean getApprovedDisclosureBean(String personId,HttpServletRequest request)throws Exception{
        HashMap hmData = new HashMap();
        hmData.put("personId", personId);
        WebTxnBean webTxn = new WebTxnBean();
        Vector apprvdDiscl = null;
        CoiDisclosureBean apprvdDisclosureBean = new CoiDisclosureBean();
        Hashtable apprvdDisclDet = (Hashtable) webTxn.getResults(request, "getApprovedDisclosure", hmData);
        apprvdDiscl = (Vector) apprvdDisclDet.get("getApprovedDisclosure");
        if (apprvdDiscl != null && apprvdDiscl.size() > 0) {
            apprvdDisclosureBean = (CoiDisclosureBean) apprvdDiscl.get(0);
            request.setAttribute("apprvdDisclosureBean", apprvdDisclosureBean);
            request.getSession().setAttribute("disclosureBeanSession", apprvdDisclosureBean);
        }
        return apprvdDisclosureBean;
}

    private void setApprovedDisclosureDetails(String coiDisclosureNumber,Integer sequenceNumber,String personId,HttpServletRequest request) throws Exception {
        HttpSession session=request.getSession();
        HashMap hmData = new HashMap();
        hmData.put("coiDisclosureNumber", coiDisclosureNumber);
         if(coiDisclosureNumber==null)
        { hmData.put("sequenceNumber",0);}
        else
        {hmData.put("sequenceNumber", sequenceNumber);}

        hmData.put("personId", personId);
        WebTxnBean webTxn = new WebTxnBean();
/* **
        Hashtable statusData = (Hashtable) webTxn.getResults(request, "getDisclDispositionStatus", hmData);
        statusDispDet = (Vector) statusData.get("getDisclDispositionStatus");
        if (statusDispDet != null && statusDispDet.size() > 0) {
            request.setAttribute("statusDispDetView", statusDispDet);
        }
 ** */
        if(session.getAttribute("currentSequence")!=null){
           hmData.put("sequenceNumber",(Integer)session.getAttribute("currentSequence"));           
        }
        hmData.put("moduleCode",null);
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
        hmData = new HashMap();
        hmData.put("personId", personId);
        Hashtable htPersonData = (Hashtable) webTxn.getResults(request, "getPersonDetails", hmData);
        Vector personDatas = (Vector) htPersonData.get("getPersonDetails");
        if (personDatas != null && personDatas.size() > 0) {
            PersonInfoBean personInfoBean = (PersonInfoBean) personDatas.get(0);
            request.setAttribute("PersonDetails", personDatas);
            session.setAttribute("person", personInfoBean);
    }

    }

    /**
     * Method for saving the financial entity details if the answer for all the questions are "No"
     * @param request
     * @param operationType
     * @param coiPersonProjectDetailsForm
     * @param permissionType
     * @param person
     * @param discl
     * @throws Exception
     */

    private void saveAnnualDisclosureDetailswithAnsNo( HttpServletRequest request,String operationType,CoiPersonProjectDetailsForm coiPersonProjectDetailsForm,
            Integer permissionType,PersonInfoBean person,CoiDisclosureBean discl) throws Exception {
            String[] totalComments=request.getParameterValues("comments");
            Integer currentSeq=(Integer) request.getSession().getAttribute("currentSequence");
            Integer moduleCode = null;
            CoiProjectService coiProjectService = CoiProjectService.getInstance();

            if(currentSeq==null){
                currentSeq=(Integer) request.getSession().getAttribute("DisclSeqNumber");
            }
            if(operationType != null && operationType.equals("MODIFY")) {
                 String disclNumber = (String) request.getSession().getAttribute("DisclosureNumberInUpdateSession");
                Integer seqNumber = (Integer) request.getSession().getAttribute("SequenceNumberInUpdateSession");
                moduleCode = (Integer) request.getSession().getAttribute("ModuleCodeInUpdateSession");
                discl.setCoiDisclosureNumber(disclNumber);
                discl.setSequenceNumber(seqNumber);
            } 
            else if(request.getSession().getAttribute("ModuleCodeInUpdateSession")!=null){
                 moduleCode = (Integer) request.getSession().getAttribute("ModuleCodeInUpdateSession");
            }
            else {
                 moduleCode = discl.getModuleCode();
            }

            String moduleItemKey = coiPersonProjectDetailsForm.getModuleItemKey();
            Vector projectDetailsList = (Vector) request.getSession().getAttribute("projectDetailsListInSeesion");
            Vector finEntityComboList = (Vector) request.getSession().getAttribute("finEntityComboList");

//Questionnare answer No then remove financial entity list
if(request.getSession().getAttribute("annualQstnFlag") != null && request.getSession().getAttribute("annualQstnFlag").toString().equals("false")) {
finEntityComboList = new Vector();
request.getSession().setAttribute("finEntityComboListcoi2",finEntityComboList);
}else{
request.getSession().setAttribute("finEntityComboListcoi2",finEntityComboList);
}
//Questionnare answer No then remove financial entity list

            if (finEntityComboList == null || finEntityComboList.isEmpty()) {
                finEntityComboList = new Vector();
                CoiFinancialEntityBean empty = new CoiFinancialEntityBean();
                finEntityComboList.add(empty);
            }
            Vector saveProjectDetailsList = new Vector();

            String relationshipDescription = " ";
            String orgRelationshipDesc = " ";

            if(projectDetailsList!=null){
   try{
            for (Iterator it1 = projectDetailsList.iterator(); it1.hasNext();) {
                
                CoiAnnualPersonProjectDetails coiPersonProjectDetails = (CoiAnnualPersonProjectDetails) it1.next();
                
                request.setAttribute("ModuleCode", coiPersonProjectDetails.getModuleCode());
                if ((moduleCode != null && moduleCode.intValue() == ModuleConstants.COI_EVENT_OTHER||moduleCode.intValue() == ModuleConstants.COI_EVENT_ANNUAL)
                        ||moduleCode.intValue() == ModuleConstants.COI_EVENT_REVISION || coiPersonProjectDetails.getModuleItemKey().equals(moduleItemKey)) {
//                    for (Iterator it = finEntityComboList.iterator(); it.hasNext();) {
//                        ComboBoxBean finEntity = (ComboBoxBean) it.next();
                    for(int i=0;i<finEntityComboList.size();i++){
                        CoiFinancialEntityBean finEntity =(CoiFinancialEntityBean)finEntityComboList.get(i);
                        String statusCode = null;
                        String[] finValueArr = null;
                        String finEntityNumber = null;
                        String finSquenceNumber = null;
                        if (finEntity.getCode() != null && !finEntity.getCode().equals("")) {
                            statusCode = "210";
                            finValueArr = finEntity.getCode().split(":");
                            finEntityNumber = finValueArr[0];
                            finSquenceNumber = finValueArr[1];
                             relationshipDescription = request.getParameter("relDesc"+finEntity.getCode());
                             orgRelationshipDesc = request.getParameter("orgRelDesc"+finEntity.getCode());
                        } else {
                            statusCode = "210";
                            finEntityNumber = null;
                            finSquenceNumber = null;
                            relationshipDescription = " ";
                           orgRelationshipDesc = " ";
                        }
                        CoiAnnualPersonProjectDetails coiPersonProjectDetailsSave = new CoiAnnualPersonProjectDetails();
                        BeanUtils.copyProperties(coiPersonProjectDetailsSave, coiPersonProjectDetails);
                        coiPersonProjectDetailsSave.setEntityNumber(finEntityNumber);
                        coiPersonProjectDetailsSave.setEntitySequenceNumber(finSquenceNumber);
                        coiPersonProjectDetailsSave.setRelationShipDescription(relationshipDescription);
                        coiPersonProjectDetailsSave.setOrgRelationDescription(orgRelationshipDesc);
                        //Integer lStatusCode=null;
//                        if(statusCode!=null){
//                          lStatusCode=Integer.parseInt(statusCode);
//                        }
//                        if(lStatusCode!=null && lStatusCode==2){
                        
//                        }else{
//                           coiPersonProjectDetailsSave.setComments("");
//                        }
                        if (statusCode != null) {
                            coiPersonProjectDetailsSave.setCoiStatusCode(Integer.parseInt(statusCode));
                        }
                        coiPersonProjectDetailsSave.setModuleItemKey(String.valueOf(coiPersonProjectDetails.getModuleItemKey()));
                        coiPersonProjectDetailsSave.setCoiProjectId(coiPersonProjectDetails.getModuleItemKey());
                        coiPersonProjectDetailsSave.setAcType("I");
                        PersonInfoBean personInfoBean = (PersonInfoBean) request.getSession().getAttribute("person");
                        coiPersonProjectDetailsSave.setUpdateUser(personInfoBean.getUserName());

                        if (coiPersonProjectDetails.getModuleCode()!=null && coiPersonProjectDetails.getModuleCode() == ModuleConstants.PROPOSAL_DEV_MODULE_CODE) {

                            //Getting propsal from session
                            Vector propsalList = (Vector) request.getSession().getAttribute("proposalProjectList");
                            if (propsalList != null && !propsalList.isEmpty()) {
                            coiPersonProjectDetailsSave.setNonIntegrated(false);
                            for (Iterator it2 = propsalList.iterator(); it2.hasNext();) {
                                CoiProposalBean propsalBean = (CoiProposalBean) it2.next();
                                if (propsalBean.getProposalNumber().equals(coiPersonProjectDetails.getModuleItemKey())) {
                                    coiPersonProjectDetailsSave.setNonIntegrated(propsalBean.isNonIntegrated());
                                    coiPersonProjectDetailsSave.setCoiProjectType(propsalBean.getProposalTypeDesc());
                                    coiPersonProjectDetailsSave.setCoiProjectTitle(propsalBean.getTitle());
                                    coiPersonProjectDetailsSave.setCoiProjectStartDate(propsalBean.getStartDate());
                                    coiPersonProjectDetailsSave.setCoiProjectSponser(propsalBean.getSponsorName());
                                    coiPersonProjectDetailsSave.setCoiProjectRole("TestRole");
                                    coiPersonProjectDetailsSave.setCoiProjectFundingAmount(propsalBean.getTotalCost());
                                    coiPersonProjectDetailsSave.setCoiProjectEndDate(propsalBean.getEndDate());
                                    coiPersonProjectDetailsSave.setModuleCode(ModuleConstants.PROPOSAL_DEV_MODULE_CODE);
                                }
                            }}
                        } else if (coiPersonProjectDetails.getModuleCode()!=null && coiPersonProjectDetails.getModuleCode() == ModuleConstants.PROTOCOL_MODULE_CODE) {
                            //getting protocols from session
                            Vector protocolList = (Vector) request.getSession().getAttribute("protocolProjectListList");
                            if (protocolList != null && !protocolList.isEmpty()) {
                            for (Iterator it2 = protocolList.iterator(); it2.hasNext();) {
                                CoiProtocolInfoBean protocolBean = (CoiProtocolInfoBean) it2.next();
                                if (protocolBean.getProtocolNumber().equals(coiPersonProjectDetails.getModuleItemKey())) {
                                    coiPersonProjectDetailsSave.setCoiProjectId(protocolBean.getProtocolNumber());
                                    coiPersonProjectDetailsSave.setModuleItemKey(protocolBean.getProtocolNumber());
                                }
                            }}
                         } else if (coiPersonProjectDetails.getModuleCode()!=null && coiPersonProjectDetails.getModuleCode() == ModuleConstants.IACUC_MODULE_CODE) {
                            // Getting iacuc protocols from session
                            Vector iacucList = (Vector) request.getSession().getAttribute("getAllIACUCProtocolList");
                            if (iacucList != null && !iacucList.isEmpty()) {
                            for (Iterator it2 = iacucList.iterator(); it2.hasNext();) {
                                CoiProtocolInfoBean iacucBean = (CoiProtocolInfoBean) it2.next();
                                if (iacucBean.getProtocolNumber().equals(coiPersonProjectDetails.getModuleItemKey())) {
                                    coiPersonProjectDetailsSave.setCoiProjectId(iacucBean.getProtocolNumber());
                                    coiPersonProjectDetailsSave.setModuleItemKey(iacucBean.getProtocolNumber());
                                }
                            }}
                        } else if (coiPersonProjectDetails.getModuleCode()!=null && coiPersonProjectDetails.getModuleCode() == ModuleConstants.AWARD_MODULE_CODE) {
                            // Getting awards from session
                            Vector awardList = (Vector) request.getSession().getAttribute("allAwardProjectList");
                            if (awardList != null && !awardList.isEmpty()) {
                            for (Iterator it2 = awardList.iterator(); it2.hasNext();) {
                                CoiAwardInfoBean awardBean = (CoiAwardInfoBean) it2.next();
                                if (awardBean.getMitAwardNumber().equals(coiPersonProjectDetails.getModuleItemKey())) {
                                    coiPersonProjectDetailsSave.setCoiProjectId(awardBean.getMitAwardNumber());
                                    coiPersonProjectDetailsSave.setModuleItemKey(awardBean.getMitAwardNumber());
                                }
                            }}
                        }
                         else if (coiPersonProjectDetails.getModuleCode()!=null && coiPersonProjectDetails.getModuleCode() == ModuleConstants.ANNUAL_COI_DISCLOSURE) {
                            // Getting annual from session
                            Vector propsalList = (Vector) request.getSession().getAttribute("proposalProjectList");
                            if (propsalList != null && !propsalList.isEmpty()) {
                                for (Iterator it2 = propsalList.iterator(); it2.hasNext();) {
                                    CoiProposalBean propsalBean = (CoiProposalBean) it2.next();
                                    if (propsalBean.getProposalNumber().equals(coiPersonProjectDetails.getModuleItemKey())) {
                                        if (propsalBean.getProposalTypeDesc() != null) {
                                            coiPersonProjectDetailsSave.setCoiProjectType(propsalBean.getProposalTypeDesc());
                                        }
                                        coiPersonProjectDetailsSave.setCoiProjectTitle(propsalBean.getTitle());
                                        coiPersonProjectDetailsSave.setCoiProjectStartDate(propsalBean.getStartDate());
                                        coiPersonProjectDetailsSave.setCoiProjectSponser(propsalBean.getSponsorName());
                                        coiPersonProjectDetailsSave.setCoiProjectRole("TestRole");
                                        coiPersonProjectDetailsSave.setCoiProjectId(propsalBean.getProposalNumber());
                                        coiPersonProjectDetailsSave.setCoiProjectFundingAmount(propsalBean.getTotalCost());
                                        coiPersonProjectDetailsSave.setCoiProjectEndDate(propsalBean.getEndDate());
                                        coiPersonProjectDetailsSave.setModuleCode(ModuleConstants.ANNUAL_COI_DISCLOSURE);
                                        coiPersonProjectDetailsSave.setAnnualProjectType("AnnualProposal");
                                    }
                                }
                            }
                            //getting protocols from session
                            Vector protocolList = (Vector) request.getSession().getAttribute("protocolProjectListList");
                            if (protocolList != null && !protocolList.isEmpty()) {
                                for (Iterator it2 = protocolList.iterator(); it2.hasNext();) {
                                    CoiProtocolInfoBean protocolBean = (CoiProtocolInfoBean) it2.next();
                                    if (protocolBean.getProtocolNumber().equals(coiPersonProjectDetails.getModuleItemKey())) {
                                        coiPersonProjectDetailsSave.setCoiProjectId(protocolBean.getProtocolNumber());
                                        coiPersonProjectDetailsSave.setModuleItemKey(protocolBean.getProtocolNumber());
                                        coiPersonProjectDetailsSave.setAnnualProjectType("AnnualProtocol");
                                    }
                                }
                            }
                    //getting iacuc protocols from session
                            Vector iacucList = (Vector) request.getSession().getAttribute("getAllIACUCProtocolList");
                            if (iacucList != null && !iacucList.isEmpty()) {
                                for (Iterator it2 = iacucList.iterator(); it2.hasNext();) {
                                    CoiProtocolInfoBean iacucprotocolBean = (CoiProtocolInfoBean) it2.next();
                                    if (iacucprotocolBean.getProtocolNumber().equals(coiPersonProjectDetails.getModuleItemKey())) {
                                        coiPersonProjectDetailsSave.setCoiProjectId(iacucprotocolBean.getProtocolNumber());
                                        coiPersonProjectDetailsSave.setModuleItemKey(iacucprotocolBean.getProtocolNumber());
                                        coiPersonProjectDetailsSave.setAnnualProjectType("AnnualProtocol");
                                    }
                                }
                            }


                            // Getting awards from session
                            Vector awardList = (Vector) request.getSession().getAttribute("allAwardProjectList");
                            if (awardList != null && !awardList.isEmpty()) {
                                for (Iterator it2 = awardList.iterator(); it2.hasNext();) {
                                    CoiAwardInfoBean awardBean = (CoiAwardInfoBean) it2.next();
                                    if (awardBean.getMitAwardNumber().equals(coiPersonProjectDetails.getModuleItemKey())) {
                                        coiPersonProjectDetailsSave.setCoiProjectId(awardBean.getMitAwardNumber());
                                        coiPersonProjectDetailsSave.setModuleItemKey(awardBean.getMitAwardNumber());
                                        coiPersonProjectDetailsSave.setAnnualProjectType("AnnualAward");
                                    }
                                }
                            }
                        }
                        coiPersonProjectDetailsSave.setCoiDisclosureNumber(discl.getCoiDisclosureNumber());
                        coiPersonProjectDetailsSave.setSequenceNumber(currentSeq);
                        coiPersonProjectDetails.setCoiDisclosureNumber(discl.getCoiDisclosureNumber());
                        coiPersonProjectDetails.setSequenceNumber(discl.getSequenceNumber());
                        coiPersonProjectDetails.setDataSaved(true);
                        saveProjectDetailsList.add(coiPersonProjectDetailsSave);

                    }
                   // break;
                }

            }
   } catch(Exception e){
      HttpSession session = request.getSession(true);
      UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute(USER+session.getId());
        String logInUserId = userInfoBean.getUserId();                      
       Integer statusCode = 210;
       CoiAnnualPersonProjectDetails coiPersonProjectDetailsSave = new CoiAnnualPersonProjectDetails();
                        coiPersonProjectDetailsSave.setCoiDisclosureNumber(discl.getCoiDisclosureNumber());
                        coiPersonProjectDetailsSave.setSequenceNumber(currentSeq);              
                        coiPersonProjectDetailsSave.setCoiStatusCode(statusCode);
                        coiPersonProjectDetailsSave.setRelationShipDescription("Sample data");
                        coiPersonProjectDetailsSave.setUpdateUser(logInUserId);
                        saveProjectDetailsList.add(coiPersonProjectDetailsSave); 
   }
            WebTxnBean webTxnBean = new WebTxnBean();
            // Saving project details
            if ((moduleCode == null || moduleCode.intValue() != 0) && saveProjectDetailsList != null && !saveProjectDetailsList.isEmpty() ) {//&& permissionType == 2
                String tempId = null;
                boolean repeatSave = false;
                Collections.sort(saveProjectDetailsList, new AnnualProjectSortingComparator());
                for (Iterator it = saveProjectDetailsList.iterator(); it.hasNext();) {
                    CoiAnnualPersonProjectDetails project = (CoiAnnualPersonProjectDetails) it.next();
                    if (tempId == null || !project.getCoiProjectId().equals(tempId)) {
                        tempId = project.getCoiProjectId();
                        if ((operationType != null && operationType.equals("null") ) && !operationType.equals("MODIFY")&& !repeatSave)  { ///Code for UPDATION STARTS
                            coiProjectService.removeProjectAndDetails(discl.getCoiDisclosureNumber(),currentSeq, project.getModuleItemKey(), "updateRemoveAllProject", request);
                        } ///Code for UPDATION ENDS
                    } else {
                        project.setAcType("N");
                        tempId = project.getCoiProjectId();
                    }
                    if(project.isNonIntegrated()){
                       project.setAcType("I");
                    }else{
                       project.setAcType("N");
                    }
                    if(repeatSave){
                       project.setAcType("N");
                    }
                    //Calling function to save project details
                    webTxnBean.getResults(request, "updateAnnualCoiProjectDetailsCoiv2", project);
                    repeatSave = true;
                }

            } else if (moduleCode != null && moduleCode == 0) {
                boolean onceDeleted = false;
                for (Iterator it = saveProjectDetailsList.iterator(); it.hasNext();) {
                    CoiAnnualPersonProjectDetails project = (CoiAnnualPersonProjectDetails) it.next();
                    if (operationType != null && operationType.equals("MODIFY") && onceDeleted == false) { ///Code for UPDATION STARTS
                        onceDeleted = true;
                        coiProjectService.removeProjectAndDetails(discl.getCoiDisclosureNumber(), discl.getSequenceNumber(), null, "updateRemoveAllNonProject", request);
                    } ///Code for UPDATION ENDS
                    project.setAcType("N");
                    //Calling function to save project details
                    webTxnBean.getResults(request, "updateAnnualCoiProjectDetailsCoiv2", project);
                }
            }
            boolean hasEntered = false;
            request.getSession().setAttribute("projectDetailsListInSeesion", projectDetailsList);            //Getting next project from the List
            try{
            for (Iterator it = projectDetailsList.iterator(); it.hasNext();) {
                CoiAnnualPersonProjectDetails coiPersonProjectDetails = (CoiAnnualPersonProjectDetails) it.next();
                if (coiPersonProjectDetails.isDataSaved() == false) {
                    coiPersonProjectDetailsForm.setModuleItemKey(coiPersonProjectDetails.getModuleItemKey());
                    coiPersonProjectDetailsForm.setCoiProjectTitle(coiPersonProjectDetails.getCoiProjectTitle());
                    coiPersonProjectDetailsForm.setAwardTitle(coiPersonProjectDetails.getAwardTitle());
                    person = (PersonInfoBean) request.getSession().getAttribute("person");
                    HashMap hmData = new HashMap();
                    hmData.put("personId", person.getPersonID());
                    //Get financial Entity
                    Vector finEntityCombo = new Vector();
                    finEntityCombo = getFinEntity(request, hmData);//calling function to get financail entity
                    request.setAttribute("financialEntityList", finEntityCombo);//setting finacial entity
                    String[] entityCode = new String[finEntityCombo.size()];
                    int i = 0;
                    for (Iterator it1 = finEntityCombo.iterator(); it1.hasNext();) {
                        CoiFinancialEntityBean entity = (CoiFinancialEntityBean) it1.next();
                        entityCode[i] = entity.getCode();
                        i++;
                    }
                    request.setAttribute("financialArrayEntityList", entityCode);//setting finacial entity code Array
                    //Getting entity satus code to populate in combobox
                    Hashtable entityCodeList = (Hashtable) webTxnBean.getResults(request, "getEntityStatusCode", hmData);
                     Vector entityTypeList = (Vector)entityCodeList.get("getEntityStatusCode");
                    Vector entytyStatusList = filterEntityStatusCode(entityTypeList);
                    request.setAttribute("typeList", entytyStatusList);
                    hasEntered = true;

                    ///Code for UPDATION STARTS
                    CoiCommonService coiCommonService = CoiCommonService.getInstance();
                    String transactoinId = "getAnnualProjects";
                    String disclNumber = (String) request.getSession().getAttribute("param1");
                    Integer seqNumber = (Integer) request.getSession().getAttribute("DisclSeqNumber");
                    Vector deatilsofProject = coiCommonService.getProjectDetails(disclNumber, currentSeq, coiPersonProjectDetails.getModuleItemKey(), request, transactoinId);
                    request.setAttribute("projectDetails", deatilsofProject);

                    ///Code for UPDATION ENDS

                    break;
                }
            }            
    }catch(Exception e){
        
    }
}

request.getSession().removeAttribute("annualQstnFlag");
            if (operationType != null && operationType.equals("MODIFY") && permissionType == 2) {
               // removeUncheckedProjects(request);

            }
                ///Code for UPDATION ENDS
            }
      private Vector getSavedDisclDetailsData(HttpServletRequest request, HashMap hmData) throws IOException, Exception {
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable projectDetailsList = (Hashtable) webTxnBean.getResults(request, "getCoiDiscDetails", hmData);
        Vector finEntityList = (Vector) projectDetailsList.get("getCoiDiscDetails");
        return finEntityList;
    }
      private void setSavedDataMenu(HttpServletRequest request,String personId) throws Exception{
           //Annual In progress fix Start       
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
//                      if(dNumber == null && sNumber == 0 && request.getSession().getAttribute("DisclSeqNumber")!=null){
//                         dNumber = (String)request.getSession().getAttribute("DisclNumber");
//                         sNumber  = Integer.parseInt(request.getSession().getAttribute("DisclSeqNumber").toString());
//                      }
             setApprovedDisclosureDetails(dNumber,sNumber,personId,request); 
             coiMenuDataSaved(dNumber,sNumber,personId,request);// saved menu    
         //Annual In progress fix end            
    
      }

    private Vector filterEntityStatusCode(Vector entityTypeList) {
      Vector entytyStatusList = new Vector();
      if(entityTypeList != null && entityTypeList.size() > 0) {
        for(int k=0; k < entityTypeList.size(); k++) {
            ComboBoxBean bean = (ComboBoxBean)entityTypeList.get(k);           
             if(bean.getCode().equals("210") || bean.getCode().equals("320") || bean.getCode().equals("310")){
                entytyStatusList.add(bean);
             }
            
        }
    }
      return entytyStatusList;
  }
}