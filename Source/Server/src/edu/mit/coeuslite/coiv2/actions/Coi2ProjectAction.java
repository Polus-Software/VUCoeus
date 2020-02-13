
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;

import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.coi.bean.ComboBoxBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.coiv2.beans.CoiAwardInfoBean;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.beans.CoiFinancialEntityBean;
import edu.mit.coeuslite.coiv2.beans.CoiMenuBean;
import edu.mit.coeuslite.coiv2.beans.CoiPersonProjectDetails;
import edu.mit.coeuslite.coiv2.beans.CoiProjectEntityDetailsBean;
import edu.mit.coeuslite.coiv2.beans.CoiProposalBean;
import edu.mit.coeuslite.coiv2.beans.CoiProtocolInfoBean;
import edu.mit.coeuslite.coiv2.formbeans.CoiPersonProjectDetailsForm;
import edu.mit.coeuslite.coiv2.services.CoiCommonService;
import edu.mit.coeuslite.coiv2.services.CoiProjectService;
import edu.mit.coeuslite.coiv2.services.GettingRightsCoiV2Service;
import edu.mit.coeuslite.coiv2.utilities.CoiConstants;
import edu.mit.coeuslite.coiv2.utilities.ModuleCodeType;
import edu.mit.coeuslite.coiv2.utilities.ProposalProjectSortingComparator;
import edu.mit.coeuslite.utils.DateUtils;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.Format;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
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
public class Coi2ProjectAction extends COIBaseAction {

    private static final String Proposal_Based = "Proposal";
    private static final String Protocol_Based = "Protocol";
    private static final String Award_Based = "Award";
    private static final String Travel_Based = "Travel";
    private static final String Other = "Other";
    private static final String Annual = "Annual";
    private static final String IACUC_Protocol = "IacucProtocol";
    private HashMap eventTypeMap = new HashMap();
    private HashMap disclModuleCodeMap = new HashMap();

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
 if(request.getAttribute("qstnAnsFlag") != null) {
 request.getSession().setAttribute("QstnAnsFlag", request.getAttribute("qstnAnsFlag").toString());
 }
    request.getSession().removeAttribute("savedQnrList");
 //for annual disclosure menu change
        CoiCommonService coiCommonService1 = CoiCommonService.getInstance();
        Integer disclosureAvailable = coiCommonService1.userHasDisclosure(request);
        if (disclosureAvailable > 0) {
            request.setAttribute("disclosureAvailableMessage", true);
            CoiDisclosureBean annualBean = new CoiDisclosureBean();
            annualBean = coiCommonService1.getAnnualDisclosure(request);
            request.setAttribute("annualDisclosureBean", annualBean);
        } else {
            request.setAttribute("disclosureAvailableMessage", false);
        }

        //for annual disclosure menu change end

String disclosureNumber=null;
String  disclosureNumb=null;
Integer sequenceNumber=null;
HttpSession session = request.getSession();

    eventTypeMap = (HashMap)session.getAttribute("EventTypeCodeMap");
    disclModuleCodeMap = (HashMap)session.getAttribute("DiscModuleCodeMap");
String personId=null;
//if(request.getParameter("param6")!=null){
//  personId=(String)request.getParameter("param3");
//  disclosureNumber=(String)request.getParameter("param1");
//  sequenceNumber = Integer.parseInt(request.getParameter("param2"));
//}
if(personId==null){
PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
personId=person.getPersonID();
//}else{
if (session.getAttribute("param1")!=null)
{
 disclosureNumber=(String)session.getAttribute("param1");
}
else
{
CoiDisclosureBean disclosureBean=getApprovedDisclosureBean(personId,request);
disclosureNumber=disclosureBean.getCoiDisclosureNumber();
}
//sequenceNumber=disclosureBean.getSequenceNumber();
if (request.getParameter("param1")!=null){
  disclosureNumb=(String)request.getParameter("param1");

  session.setAttribute("param1", disclosureNumb);
}
  else if(request.getParameter("param1")==null && session.getAttribute("DisclNumber")!=null){
  disclosureNumb=(String)session.getAttribute("DisclNumber");

}
if(session.getAttribute("fromQuestionnaire")==null){
if(request.getParameter("param2")!=null){
sequenceNumber=Integer.parseInt(request.getParameter("param2"));
session.setAttribute("param2", sequenceNumber);
setApprovedDisclosureDetails(disclosureNumb,sequenceNumber,personId,request);
}else if(session.getAttribute("param2")!=null){
sequenceNumber= (Integer) session.getAttribute("param2");
setApprovedDisclosureDetails(disclosureNumb,sequenceNumber,personId,request);
}
}
else if(session.getAttribute("fromQuestionnaire")!=null && session.getAttribute("currentSequence")!=null){
 sequenceNumber= (Integer) session.getAttribute("currentSequence");
setApprovedDisclosureDetails(disclosureNumb,sequenceNumber,personId,request);
session.setAttribute("fromQuestionnaire","fromQuestionnaire");
}
}

    if(request.getSession().getAttribute("ownDisclosure") != null) {
        request.getSession().removeAttribute("ownDisclosure");
    }
    if(request.getParameter("param3") != null) {
         PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String discPrsnId = person.getPersonID();
        String discPersonId = request.getParameter("param3").toString().trim();
        if(discPersonId.equals(discPrsnId.trim())){
            request.getSession().setAttribute("ownDisclosure", "true");
        }
    }
    // NEW MENU CHANGE 
if(disclosureNumber == null ||(disclosureNumber!=null && disclosureNumber.trim().length()<=0)){
    disclosureNumber = (String)session.getAttribute("COIDiscNumber");
}
if(sequenceNumber == null && session.getAttribute("currentSequence")!=null){
    sequenceNumber = (Integer)session.getAttribute("currentSequence");
}
if(sequenceNumber == null && session.getAttribute("DisclSeqNumber")!=null){
    sequenceNumber = (Integer)session.getAttribute("DisclSeqNumber");
}
 
setApprovedDisclosureDetails(disclosureNumber,sequenceNumber,personId,request);
   // NEW MENU CHANGE
            coiMenuDataSaved(disclosureNumber,sequenceNumber,personId,request);// saved menu    
            if(session.getAttribute("contineWithoutQnr")==null){
            CoiMenuBean coiMenuBean = (CoiMenuBean)request.getSession().getAttribute("coiMenuDatasaved");   
                        if(coiMenuBean == null || ((coiMenuBean!=null && coiMenuBean.isQuestDataSaved()==false)&&(coiMenuBean!=null && coiMenuBean.isPrjDataSaved()==false)&&(coiMenuBean!=null && coiMenuBean.isNoteDataSaved()==false)&&(coiMenuBean!=null && coiMenuBean.isAttachDataSaved()==false))){
            request.setAttribute("QnrNotCompleted","true");
            } else{
               request.removeAttribute("QnrNotCompleted");
            }
            }
    // NEW MENU CHANGE 

//if(disclosureNumber==null){
//    disclosureNumber=(String)request.getParameter("param1");
//    sequenceNumber = Integer.parseInt(request.getParameter("param2"));
//}
        CoiProjectService coiProjectService = CoiProjectService.getInstance();
        CoiPersonProjectDetailsForm coiPersonProjectDetailsForm = (CoiPersonProjectDetailsForm) actionForm;
        GettingRightsCoiV2Service gettingRightsCoiV2Service = GettingRightsCoiV2Service.getInstance();      
        String operationType = request.getParameter("operationType");
        request.setAttribute("operationType", operationType);
        HashMap thisUserRights = (HashMap) request.getAttribute(RIGHT);
        Integer permissionType = (Integer) thisUserRights.get(CoiConstants.DISCL);
        Integer moduleCode = null;
        // Function call to get project deatis and Financial entity
        if (actionMapping.getPath().equals("/getProjectDetailsAndFinEntityDetailsCoiv2")) {
            String success = "success";
            String successEdit = "success";        
            request.removeAttribute("DiscViewQtnr");
            String moduleItemKey =null;
            request.setAttribute("DiscViewByPrjt", true);//to check Project menu selected
            // new financial entity select saved data fix start
            Vector pjctDetails = (Vector)request.getSession().getAttribute("projectDetailsListInSeesion");
            if(pjctDetails !=null){
            CoiPersonProjectDetails coiPersonProjectDetails =(CoiPersonProjectDetails) pjctDetails.get(0);           
            moduleItemKey=coiPersonProjectDetails.getModuleItemKey();
            }
            // new financial entity select saved data fix start
             Integer extProjcode = (Integer) request.getSession().getAttribute("projectEdit");
             if(extProjcode!=null){
                 if(extProjcode==1){
                    successEdit = getProjectDetailsAndFinancialEntityForRecall(request, actionMapping, actionForm);
                 }
             }
            success = getProjectDetailsAndFinancialEntityDetails(request, actionMapping, actionForm);
            if(request.getAttribute("qstnAnsFlag") != null && request.getAttribute("qstnAnsFlag").toString().equals("false")) {
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
//
//                if(finEntityCombo != null && finEntityCombo.size()> 0)
//                 {
//                   success = "success";
//                 }
//                 else
//                 {
//                   saveProjectwithQstnAnsNo(request,operationType,coiPersonProjectDetailsForm,moduleCode,permissionType);
//                   success = "continue";
//                 }
//                    }
                    PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute("person");
                    HashMap hmData = new HashMap();
                    hmData.put("personId", person.getPersonID());
                    
                    finEntityCombo = getFinEntity(request, hmData);
                 if(finEntityCombo != null && finEntityCombo.size()> 0)
                 {
                   success = "success";
                 }
                 else
                 {
                   saveProjectwithQstnAnsNo(request,operationType,coiPersonProjectDetailsForm,moduleCode,permissionType);
                   success = "continue";
                 }
            } else {
                if(request.getAttribute("qstnAnsFlag") != null) {
                    request.getSession().setAttribute("QstnAnsFlag", request.getAttribute("qstnAnsFlag").toString());
                     request.setAttribute("mode", "add");
                    Vector finEntityCombo = (Vector)request.getAttribute("financialEntityList");
                    if(finEntityCombo==null)
                    {
                    PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute("person");
                    HashMap hmData = new HashMap();
                    hmData.put("personId", person.getPersonID());
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
                   saveProjectwithQstnAnsNo(request,operationType,coiPersonProjectDetailsForm,moduleCode,permissionType);
                   success = "continue";
                 }
                    }
                    if(finEntityCombo == null || finEntityCombo != null && finEntityCombo.size() == 0) {
                        success = "createFinEntity";
                    }
                }
                if(request.getAttribute("qstnAnsFlag") == null){
                    PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute("person");
                    HashMap hmData = new HashMap();                    
                    hmData.put("personId", person.getPersonID());
                    Vector finEntityCombo  = getFinEntity(request, hmData);
                    String[] entityCode = new String[finEntityCombo.size()];
                    int i = 0;
     // new financial entity select saved data fix start
                    HashMap hmDiscData = new HashMap();
            if(finEntityCombo!=null && finEntityCombo.size()>0){
                   for (Iterator it1 = finEntityCombo.iterator(); it1.hasNext();) {
                        CoiFinancialEntityBean entityDetail = (CoiFinancialEntityBean) it1.next();                     
                        entityCode[i] = entityDetail.getCode();
                        i++;
                    if (entityDetail.getEntityNumber()!=null && entityDetail.getEntityNumber().trim().length() > 0){
                    hmDiscData.put("disclosureNumber", disclosureNumber);
                    hmDiscData.put("sequenceNumber", sequenceNumber);
                    hmDiscData.put("moduleItemKey", moduleItemKey);
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
     // new financial entity select saved data fix end
                      request.setAttribute("financialEntityList", finEntityCombo);//setting finacial entity                   
                    
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
                   saveProjectwithQstnAnsNo(request,operationType,coiPersonProjectDetailsForm,moduleCode,permissionType);
                   success = "continue";
                 }
                }                
            }            
    // new financial entity select saved data fix start
                    PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute("person");
                    HashMap hmData = new HashMap();                    
                    hmData.put("personId", person.getPersonID());
              Vector finEntityComboNew  = getFinEntity(request, hmData);
                 HashMap hmDiscData = new HashMap();
            if(finEntityComboNew!=null && finEntityComboNew.size()>0){
                   for (Iterator it1 = finEntityComboNew.iterator(); it1.hasNext();) {
                        CoiFinancialEntityBean entityDetail = (CoiFinancialEntityBean) it1.next();                     
                        
                    if (entityDetail.getEntityNumber()!=null && entityDetail.getEntityNumber().trim().length() > 0){
                    hmDiscData.put("disclosureNumber", disclosureNumber);
                    hmDiscData.put("sequenceNumber", sequenceNumber);
                    hmDiscData.put("moduleItemKey", moduleItemKey);
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
              request.setAttribute("financialEntityList", finEntityComboNew);
               // NEW MENU CHANGE 
               session.setAttribute("isEventForInProgress",true);      
        // NEW MENU CHANGE  
     // new financial entity select saved data fix end          
            return actionMapping.findForward(success);
        } else if (actionMapping.getPath().equals("/saveProjectDetails")) {
           // session.removeAttribute("QstnAnsFlag");  
           
            CoiDisclosureBean discl = new CoiDisclosureBean();
            if (operationType != null && !operationType.equals("MODIFY")) {
                discl = getCurrentDisclPerson(request);
                moduleCode = discl.getModuleCode();
            } else { ///Code for UPDATION STARTS
                String disclNumber = (String) request.getSession().getAttribute("DisclosureNumberInUpdateSession");
                Integer seqNumber = (Integer) request.getSession().getAttribute("SequenceNumberInUpdateSession");
                moduleCode = (Integer) request.getSession().getAttribute("ModuleCodeInUpdateSession");

                discl.setCoiDisclosureNumber(disclNumber);
                discl.setSequenceNumber(seqNumber);
            } ///Code for UPDATION ENDS
            coiMenuDataSaved(disclosureNumber,sequenceNumber,personId,request);// saved menu
            String moduleItemKey = coiPersonProjectDetailsForm.getModuleItemKey();
            String relationshipDescription = " ";
            String orgRelationshipDesc = " ";

            Vector projectDetailsList = (Vector) request.getSession().getAttribute("projectDetailsListInSeesion");
            Vector finEntityComboList = (Vector) request.getSession().getAttribute("finEntityComboList");
            request.getSession().setAttribute("projectDetailsListInSeesioncoi2",projectDetailsList);
            //if(session.getAttribute("QstnAnsFlag") != null && session.getAttribute("QstnAnsFlag").toString().equals("false")) {
              //  finEntityComboList = new Vector();
                request.getSession().setAttribute("finEntityComboListcoi2",finEntityComboList);
//            }else{
//                 request.getSession().setAttribute("finEntityComboListcoi2",finEntityComboList);
//            }
            if (finEntityComboList == null || finEntityComboList.isEmpty()) {
                CoiFinancialEntityBean empty = new CoiFinancialEntityBean();
                finEntityComboList.add(empty);
            }
            Vector saveProjectDetailsList = new Vector();

            for (Iterator it1 = projectDetailsList.iterator(); it1.hasNext();) {
                CoiPersonProjectDetails coiPersonProjectDetails = (CoiPersonProjectDetails) it1.next();
                request.setAttribute("ModuleCode", coiPersonProjectDetails.getModuleCode());
                if(coiPersonProjectDetails.getModuleItemKey()!=null){
                if ((moduleCode != null && (moduleCode.intValue() == ModuleConstants.COI_EVENT_OTHER || moduleCode.intValue() == ModuleConstants.ANNUAL_COI_DISCLOSURE)) || (coiPersonProjectDetails.getModuleItemKey().equals(moduleItemKey))) {
                   //for (Iterator it = finEntityComboList.iterator(); it.hasNext();) {
                        //ComboBoxBean finEntity = (ComboBoxBean) it.next();
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

                      
                        CoiPersonProjectDetails coiPersonProjectDetailsSave = new CoiPersonProjectDetails();
                        BeanUtils.copyProperties(coiPersonProjectDetailsSave, coiPersonProjectDetails);
                        coiPersonProjectDetailsSave.setEntityNumber(finEntityNumber);
                        coiPersonProjectDetailsSave.setEntitySequenceNumber(finSquenceNumber);
                         
                        coiPersonProjectDetailsSave.setRelationShipDescription(relationshipDescription);
                        coiPersonProjectDetailsSave.setOrgRelationDescription(orgRelationshipDesc);

                        //Integer cod =null;
//                        if(statusCode!=null){
//                            cod = Integer.parseInt(statusCode);
//                        }
//                        if(cod!=null && cod==2){
                       
//                        }else{
//                         coiPersonProjectDetailsSave.setComments("");
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
                            session.setAttribute("disclPjctModuleName", "Proposal");
                            Vector propsalList = (Vector) request.getSession().getAttribute("proposalProjectList");
                            coiPersonProjectDetailsSave.setNonIntegrated(false);
                             if ( propsalList != null && !propsalList.isEmpty()){
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
//                                else{
//                                    coiPersonProjectDetailsSave.setNonIntegrated(propsalBean.isNonIntegrated());
//                                }

                            }}
                        } else if (coiPersonProjectDetails.getModuleCode() == ModuleConstants.PROTOCOL_MODULE_CODE) {
                            //getting protocols from session
                            session.setAttribute("disclPjctModuleName", "Protocol");
                            Vector protocolList = (Vector) request.getSession().getAttribute("protocolProjectListList");
                             if ( protocolList != null && !protocolList.isEmpty()){
                            for (Iterator it2 = protocolList.iterator(); it2.hasNext();) {
                                CoiProtocolInfoBean protocolBean = (CoiProtocolInfoBean) it2.next();
                                if (protocolBean.getProtocolNumber().equals(coiPersonProjectDetails.getModuleItemKey())) {
                                     coiPersonProjectDetailsSave.setCoiProjectId(protocolBean.getProtocolNumber());
                                    coiPersonProjectDetailsSave.setModuleItemKey(protocolBean.getProtocolNumber());
//                                    coiPersonProjectDetailsSave.setCoiProjectStartDate(protocolBean.getApplicationDate());
//                                    coiPersonProjectDetailsSave.setCoiProjectEndDate(protocolBean.getExpirationDate());
                                }}
                            }
                        }else if (coiPersonProjectDetails.getModuleCode() == ModuleConstants.IACUC_MODULE_CODE) {
                            //getting protocols from session
                            session.setAttribute("disclPjctModuleName", "Protocol");
                            Vector protocolList = (Vector) request.getSession().getAttribute("getAllIACUCProtocolList");
                             if ( protocolList != null && !protocolList.isEmpty()){
                            for (Iterator it2 = protocolList.iterator(); it2.hasNext();) {
                                CoiProtocolInfoBean protocolBean = (CoiProtocolInfoBean) it2.next();
                                if (protocolBean.getProtocolNumber().equals(coiPersonProjectDetails.getModuleItemKey())) {
                                    coiPersonProjectDetailsSave.setCoiProjectId(protocolBean.getProtocolNumber());
                                    coiPersonProjectDetailsSave.setModuleItemKey(protocolBean.getProtocolNumber());
//                                    coiPersonProjectDetailsSave.setCoiProjectStartDate(protocolBean.getApplicationDate());
//                                    coiPersonProjectDetailsSave.setCoiProjectEndDate(protocolBean.getExpirationDate());
                                }
                            }}
                        }
                        else if (coiPersonProjectDetails.getModuleCode() == ModuleConstants.AWARD_MODULE_CODE) {
                            // Getting awards from session
                            session.setAttribute("disclPjctModuleName", "Award");
                            Vector awardList = (Vector) request.getSession().getAttribute("allAwardProjectList");
                             if ( awardList != null && !awardList.isEmpty()){
                            for (Iterator it2 = awardList.iterator(); it2.hasNext();) {
                                CoiAwardInfoBean awardBean = (CoiAwardInfoBean) it2.next();
                                if (awardBean.getMitAwardNumber().equalsIgnoreCase(coiPersonProjectDetails.getModuleItemKey())) {
                                     coiPersonProjectDetailsSave.setCoiProjectId(awardBean.getMitAwardNumber());
                                    coiPersonProjectDetailsSave.setAwardTitle(awardBean.getAwardTitle());
                                    coiPersonProjectDetailsSave.setModuleItemKey(awardBean.getMitAwardNumber());
                                    coiPersonProjectDetailsSave.setCoiProjectStartDate(awardBean.getStartDate());
                                    coiPersonProjectDetailsSave.setCoiProjectEndDate(awardBean.getAwardExecutionDate());
                                }
                            }}
                        }
                        else if (coiPersonProjectDetails.getModuleCode() == ModuleConstants.TRAVEL_MODULE_CODE) {
                             session.setAttribute("disclPjctModuleName", "Travel");
                             CoiPersonProjectDetails pjtDet = (CoiPersonProjectDetails)request.getSession().getAttribute("travelPjtDetails");
                             coiPersonProjectDetailsSave.setCoiProjectId(pjtDet.getModuleItemKey());
                             coiPersonProjectDetailsSave.setModuleItemKey(pjtDet.getModuleItemKey());
                             coiPersonProjectDetailsSave.setCoiProjectStartDate(pjtDet.getCoiProjectStartDate());
                             coiPersonProjectDetailsSave.setCoiProjectEndDate(pjtDet.getCoiProjectEndDate());
                             coiPersonProjectDetailsSave.setEventName(pjtDet.getEventName());
                             coiPersonProjectDetailsSave.setDestination(pjtDet.getDestination());
                             coiPersonProjectDetailsSave.setPurpose(pjtDet.getPurpose());
                             coiPersonProjectDetailsSave.setCoiProjectFundingAmount(pjtDet.getCoiProjectFundingAmount());
                             coiPersonProjectDetailsSave.setCoiProjectSponser(pjtDet.getCoiProjectSponser());
                        }

                        coiPersonProjectDetailsSave.setCoiDisclosureNumber(discl.getCoiDisclosureNumber());
                        coiPersonProjectDetailsSave.setSequenceNumber(discl.getSequenceNumber());
                        coiPersonProjectDetails.setCoiDisclosureNumber(discl.getCoiDisclosureNumber());
                        coiPersonProjectDetails.setSequenceNumber(discl.getSequenceNumber());
                        coiPersonProjectDetails.setDataSaved(true);
                        saveProjectDetailsList.add(coiPersonProjectDetailsSave);

                    }
                    break;
                }
            }

            }
            WebTxnBean webTxnBean = new WebTxnBean();
            // Saving project details
            if ((moduleCode == null || moduleCode.intValue() != -1) && saveProjectDetailsList != null && !saveProjectDetailsList.isEmpty()) {
                String tempId = null;
                Collections.sort(saveProjectDetailsList, new ProposalProjectSortingComparator());
                boolean repeatSave = false;
                for (Iterator it = saveProjectDetailsList.iterator(); it.hasNext();) {
                    CoiPersonProjectDetails project = (CoiPersonProjectDetails) it.next();
                     if (tempId == null || !project.getCoiProjectId().equals(tempId)) {
                        tempId = project.getCoiProjectId();
//                        //added by Roshin
//                        if (operationType != null && operationType.equals("MODIFY")) { ///Code for UPDATION STARTS
//                            if(project.isNonIntegrated() & !project.getAcType().equalsIgnoreCase("I")){
//                            coiProjectService.removeProjectAndDetails(discl.getCoiDisclosureNumber(), discl.getSequenceNumber(), project.getModuleItemKey(), "updateRemoveAllProject", request);
//                            }else{
//                            coiProjectService.removeProjectAndDetails(discl.getCoiDisclosur6eNumber(), discl.getSequenceNumber(), project.getModuleItemKey(), "updateRemoveAllNonProject", request);
//                            }
//                        }
//                        //added by Roshin
                        if (operationType != null && operationType.equals("MODIFY")) { ///Code for UPDATION STARTS
                            //coiProjectService.removeProjectAndDetails(discl.getCoiDisclosureNumber(), discl.getSequenceNumber(), project.getModuleItemKey(), "updateRemoveAllProject", request);
                              if (project.getAcType().equalsIgnoreCase("I")) {
                     // NEW MENU CHANGE 
                                  //       coiProjectService.removeProjectAndDetails(discl.getCoiDisclosureNumber(), discl.getSequenceNumber(), project.getModuleItemKey(), "updateRemoveAllNonProject", request);
                      // NEW MENU CHANGE         
                              } else {
                                  // NEW MENU CHANGE 
                       //     coiProjectService.removeProjectAndDetails(discl.getCoiDisclosureNumber(), discl.getSequenceNumber(), project.getModuleItemKey(), "updateRemoveAllProject", request);
                            // NEW MENU CHANGE 
                              }
                        } ///Code for UPDATION ENDS
                    } else {
                        project.setAcType("N");
                        tempId = project.getCoiProjectId();
                    }
                    if(project.isNonIntegrated() || project.getModuleCode() == ModuleConstants.TRAVEL_MODULE_CODE){   //for non integrated
                       project.setAcType("I");
                    }else{
                       project.setAcType("N");
                    }
                    if(repeatSave){                  //for removing repeted saving on discl_project
                       project.setAcType("N");
                    }

                    if(project.getModuleCode() == ModuleConstants.TRAVEL_MODULE_CODE) {
                        project.setEventTypeCode(ModuleConstants.COI_EVENT_TRAVEL);
                    }
                    if(project.isNonIntegrated()){
                         project.setEventTypeCode(ModuleConstants.COI_EVENT_PROPOSAL);
                    }
     // NEW MENU CHANGE 
       int coiSequenceNumber = -1;
        String coiDiscNumber  =(String)session.getAttribute("COIDiscNumber");
        if(session.getAttribute("currentSequence")!=null){
         coiSequenceNumber = Integer.parseInt(session.getAttribute("currentSequence").toString());
        }            coiMenuDataSaved(coiDiscNumber,coiSequenceNumber,personId,request);//coi saved menu
           CoiMenuBean  coiMenuBean = (CoiMenuBean)request.getSession().getAttribute("coiMenuDatasaved");   
            if((coiMenuBean!=null && coiMenuBean.isPrjDataSaved()==true)){
            project.setAcType("N");
            }    
     // NEW MENU CHANGE  
                    //Calling function to save project details
                    UtilFactory.log("project name  :"+project.getCoiProjectTitle());
                    UtilFactory.log("actype  :"+project.getAcType());
                    webTxnBean.getResults(request, "updateCoiProjectDetailsCoiv2", project);
                    repeatSave = true;
       coiMenuDataSaved(coiDiscNumber,coiSequenceNumber,personId,request);//coi saved menu              
                }

            } 
//            else if (moduleCode != null) {
//                boolean onceDeleted = false;
//                for (Iterator it = saveProjectDetailsList.iterator(); it.hasNext();) {
//                    CoiPersonProjectDetails project = (CoiPersonProjectDetails) it.next();
//                    if (operationType != null && operationType.equals("MODIFY") && onceDeleted == false) { ///Code for UPDATION STARTS
//                        onceDeleted = true;
//                        coiProjectService.removeProjectAndDetails(discl.getCoiDisclosureNumber(), discl.getSequenceNumber(), null, "updateRemoveAllNonProject", request);
//                    } ///Code for UPDATION ENDS
//                    project.setAcType("N");
//                    //Calling function to save project details
//                    webTxnBean.getResults(request, "updateCoiProjectDetailsCoiv2", project);
//                }
//            }
            boolean hasEntered = false;
            request.getSession().setAttribute("projectDetailsListInSeesion", projectDetailsList);
            //Getting next project from the List
            for (Iterator it = projectDetailsList.iterator(); it.hasNext();) {
                CoiPersonProjectDetails coiPersonProjectDetails = (CoiPersonProjectDetails) it.next();
                if (coiPersonProjectDetails.isDataSaved() == false) {
                    coiPersonProjectDetailsForm.setModuleItemKey(coiPersonProjectDetails.getModuleItemKey());
                    coiPersonProjectDetailsForm.setCoiProjectTitle(coiPersonProjectDetails.getCoiProjectTitle());
                    coiPersonProjectDetailsForm.setAwardTitle(coiPersonProjectDetails.getAwardTitle());

                    if(coiPersonProjectDetails.getCoiProjectStartDate() != null){
                        coiPersonProjectDetailsForm.setCoiProjectStartDate(coiPersonProjectDetails.getCoiProjectStartDate().toString());
                    }
                    else {
                        coiPersonProjectDetailsForm.setCoiProjectStartDate("");
                    }
                    String moduleItemKeyAdded = coiPersonProjectDetails.getModuleItemKey();
                    //
                    // coiPersonProjectDetailsForm.setCoiProjectSponser(coiPersonProjectDetails.getCoiProjectSponser());
                    // String addedprojectSponser=coiPersonProjectDetailsForm.getCoiProjectSponser();
                    PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute("person");
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
                    CoiProjectEntityDetailsBean pjtBean = new CoiProjectEntityDetailsBean();
                    CoiCommonService coiCommonService = CoiCommonService.getInstance();

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
                       if (coiPersonProjectDetails.getModuleCode() == ModuleConstants.IACUC_MODULE_CODE) {
                            transactoinId = "getIacucProtoPjtDetForDiscl";
                        }
                        if (coiPersonProjectDetails.getModuleCode() == ModuleConstants.TRAVEL_MODULE_CODE) {
                            transactoinId = "getTravelPjtDetForDiscl";
                        }

                        Vector deatilsofProject = coiCommonService.getProjectDetails(disclNumber, seqNumber, coiPersonProjectDetails.getModuleItemKey(), request, transactoinId);

                        if (coiPersonProjectDetails.getModuleCode() == ModuleConstants.PROTOCOL_MODULE_CODE) {
                            if(deatilsofProject != null && deatilsofProject.size() != 0) {
                                 pjtBean =  (CoiProjectEntityDetailsBean) deatilsofProject.get(0);
                            }
                        }

                        request.setAttribute("projectDetails", deatilsofProject);
                        request.setAttribute("protList", pjtBean);
                    }

                    if(coiPersonProjectDetails.getModuleCode() == ModuleConstants.PROTOCOL_MODULE_CODE && pjtBean == null ||( pjtBean != null && (pjtBean.getCoiProjectId() == null || pjtBean.getCoiProjectId() ==""))) {
                         HashMap inputData = new HashMap();
                         inputData.put("protocolNumber", coiPersonProjectDetails.getModuleItemKey());
                          Hashtable ProtoDetails = (Hashtable) webTxnBean.getResults(request, "getSelectedProtocolDetails", inputData);
                          Vector details = (Vector)ProtoDetails.get("getSelectedProtocolDetails");
                           if(details!=null && details.size()!= 0) {
                             pjtBean =  (CoiProjectEntityDetailsBean) details.get(0);
                           }
                          request.setAttribute("protList", pjtBean);
                    }
                    ///Code for UPDATION ENDS

                    break;
                }
            }
// // enhancement for new Menu start 
//    request.getSession().setAttribute("frmPendingInPrg", "true");
//    request.getSession().setAttribute("dontShowProjects", "false"); 
//    setSavedDataMenu(request,personId);
// // enhancement for new Menu ends           
            if (hasEntered == true) {
                 if(request.getSession().getAttribute("frmPendingInPrg")!=null){
                      return actionMapping.findForward("continue");
                 }
                return actionMapping.findForward("success");
            } else {
                ///Code for UPDATION STARTS
                if (operationType.equals("MODIFY") && permissionType == 2) {
                    removeUncheckedProjects(request);

                }
                 if (operationType.equals("MODIFY") && permissionType == 0) {
                    removeUncheckedProjects(request);

                }
                ///Code for UPDATION ENDS
            }

        } else if (actionMapping.getPath().equals("/findDisclosureProjectType")) {
            disclosureNumber = request.getParameter("param1");
            if(request.getParameter("param2")!= null){
            sequenceNumber = Integer.parseInt(request.getParameter("param2"));
            request.getSession().setAttribute("currentSequence", sequenceNumber);
            }

            if(request.getParameter("param5") != null){
                moduleCode = Integer.parseInt(request.getParameter("param5"));
                if(moduleCode.equals(5)){
                    session.setAttribute("Annual",true);
                }
            }
            
            String moduleItemKey = "";
            
            if(request.getParameter("module_item_key") != null) {
                moduleItemKey = request.getParameter("module_item_key");
            }

           // moduleCode = coiProjectService.getModulecode(disclosureNumber, sequenceNumber, request);
            request.getSession().setAttribute("DisclosureNumberInUpdateSession", disclosureNumber);
            request.getSession().setAttribute("SequenceNumberInUpdateSession", sequenceNumber);
            request.getSession().setAttribute("ModuleCodeInUpdateSession", moduleCode);
            request.getSession().setAttribute("selectedModuleItemKey", moduleItemKey);
            //For Temoprary use only..Need to remove after finding solution
            if (moduleCode == null) {
                moduleCode = ModuleConstants.COI_EVENT_PROPOSAL;
            }


             if (moduleCode == 2 ) {
                return actionMapping.findForward("successToPropsal");
            } else if (moduleCode == 3) {
                return actionMapping.findForward("successToProtocol");
            } else if (moduleCode == 1) {
                return actionMapping.findForward("successToAward");
            }else if (moduleCode == 4) {
                return actionMapping.findForward("successToIacucProtocol");
            }else if (moduleCode == ModuleConstants.COI_EVENT_OTHER) {
                return actionMapping.findForward("successToOther");
            }else{
                return actionMapping.findForward("successToAnnual");

            }

        }
        else if (actionMapping.getPath().equals("/getPrjtDetailsAndFinEntDetailsCoiv2")) {
                String success = "success";
                String successEdit = "success";
             Integer extProjcode = (Integer) request.getSession().getAttribute("projectEdit");
             if(extProjcode!=null){
                 if(extProjcode==1){
                    successEdit = getProjectDetailsAndFinancialEntityForRecall(request, actionMapping, actionForm);
                 }
             }
         //   success = getProjectDetailsAndFinancialEntityDetails(request, actionMapping, actionForm);
            if(request.getAttribute("qstnAnsFlag") != null && request.getAttribute("qstnAnsFlag").toString().equals("false")) {
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
                    
//
//                if(finEntityCombo != null && finEntityCombo.size()> 0)
//                 {
//                   success = "success";
//                 }
//                 else
//                 {
//                   saveProjectwithQstnAnsNo(request,operationType,coiPersonProjectDetailsForm,moduleCode,permissionType);
//                   success = "continue";
//                 }
//                    }
                    PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute("person");
                    HashMap hmData = new HashMap();
                    hmData.put("personId", person.getPersonID());

                    finEntityCombo = getFinEntity(request, hmData);
                 if(finEntityCombo != null && finEntityCombo.size()> 0)
                 {
                   success = "success";
                 }
                 else
                 {
                   saveProjectwithQstnAnsNo(request,operationType,coiPersonProjectDetailsForm,moduleCode,permissionType);
                   success = "continue";
                 }
            } else {
                if(request.getAttribute("qstnAnsFlag") != null) {
                    request.getSession().setAttribute("QstnAnsFlag", request.getAttribute("qstnAnsFlag").toString());
                     request.setAttribute("mode", "add");
                    Vector finEntityCombo = (Vector)request.getAttribute("financialEntityList");
                    if(finEntityCombo==null)
                    {
                    PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute("person");
                    HashMap hmData = new HashMap();
                    hmData.put("personId", person.getPersonID());
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
                   saveProjectwithQstnAnsNo(request,operationType,coiPersonProjectDetailsForm,moduleCode,permissionType);
                   success = "continue";
                 }
                    }
                    if(finEntityCombo == null || finEntityCombo != null && finEntityCombo.size() == 0) {
                        success = "createFinEntity";
                    }
                }
                if(request.getAttribute("qstnAnsFlag") == null){
                    PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute("person");
                    HashMap hmData = new HashMap();
                    hmData.put("personId", person.getPersonID());
                    Vector finEntityCombo  = getFinEntity(request, hmData);
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
                   saveProjectwithQstnAnsNo(request,operationType,coiPersonProjectDetailsForm,moduleCode,permissionType);
                   success = "continue";
                 }
                }
            }
            return actionMapping.findForward(success);
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
     private String getProjectDetailsAndFinancialEntityForRecall(HttpServletRequest request, ActionMapping actionMapping, ActionForm actionForm) throws Exception {
        HttpSession session = request.getSession();
        String operationType = request.getParameter("operationType");
        PersonInfoBean person = (PersonInfoBean) session.getAttribute("person");
        HashMap hmData = new HashMap();
        hmData.put("personId", person.getPersonID());
        CoiDisclosureBean currDisclosure = this.getCurrentDisclPerson(request);

          CoiDisclosureBean discl =(CoiDisclosureBean) request.getSession().getAttribute("disclosureBeanSession");
            Integer seqNumber=0;
            seqNumber=(Integer) request.getSession().getAttribute("currentSequence");
            String disclNumber=currDisclosure.getCoiDisclosureNumber();
           currDisclosure.setSequenceNumber(seqNumber);

       // request.getSession().setAttribute("disclosureBeanSession", currDisclosure);
            CoiCommonService coiCommonService = CoiCommonService.getInstance();
        String projectType = (String) request.getSession().getAttribute("projectType");
        HashMap projectDetailArg = new HashMap();
        projectDetailArg.put("disclosurenumber", currDisclosure.getCoiDisclosureNumber());
        projectDetailArg.put("sequencenumber", currDisclosure.getSequenceNumber());
        WebTxnBean webTxnBean = new WebTxnBean();
        Vector projectDetails = new Vector();
        CoiPersonProjectDetails projectDet = new CoiPersonProjectDetails();
        Integer modulecode = null;
        HashMap thisUserRights = (HashMap) request.getAttribute(RIGHT);
        Integer permissionType = (Integer) thisUserRights.get(CoiConstants.DISCL);





//        ////-------------new updation for exit To Mycoi
         Vector finEntityCombo = new Vector();
        hmData = new HashMap();
        hmData.put("personId", person.getPersonID());
        finEntityCombo = getFinEntityForExt(request, hmData);//calling function to get financail entity
        String[] entityCode = new String[finEntityCombo.size()];
        int in = 0;
        for (Iterator it = finEntityCombo.iterator(); it.hasNext();) {
            CoiFinancialEntityBean entity = (CoiFinancialEntityBean) it.next();
            entityCode[in] = entity.getCode();
            in++;
        }
        request.getSession().setAttribute("financialArrayEntityList_ext", entityCode);/// for prject old
        request.getSession().setAttribute("financialEntityList_ext", finEntityCombo);/// for prject old


//        ///-----------------------------updation ends


        try{

        int count = 0;
        if (projectType.equals(Proposal_Based)) {
            modulecode = ModuleConstants.PROPOSAL_DEV_MODULE_CODE;
            projectDetailArg.put("modulecode", modulecode);

             Vector savedprojects = (Vector) request.getSession().getAttribute("AlreadySavedProjectsForExt");
             String[] checkedPropsalProjects = new String[savedprojects.size()];
           // String[] checkedPropsalProjects = (String[]) request.getSession().getAttribute("checkedPropsalProjects");
             if(savedprojects!=null){
                int k1 = 0;
                for (Iterator its = savedprojects.iterator(); its.hasNext();) {
                    CoiProposalBean savedpropsalBean = (CoiProposalBean) its.next();
                    checkedPropsalProjects[k1] = savedpropsalBean.getProposalNumber() + ":" + savedpropsalBean.getTitle()+ ":" +savedpropsalBean.getSponsorName()+ ":" +savedpropsalBean.getStartDate()+ ":" +savedpropsalBean.getEndDate();
                    k1++;
                }
             }

            if ((savedprojects == null || savedprojects.size() == 0) && permissionType == 2){
                request.removeAttribute("projectDetailsListInSeesion_ext");
                if (operationType != null && operationType.equals("MODIFY")) {
                    request.removeAttribute("projectDetailsListInSeesion_ext");
                    request.setAttribute("ModuleCode", modulecode);
                    //removeUncheckedProjects(request);5
                     removeUncheckedProjectsForExt(request,disclNumber, seqNumber);
                }
                return "continue";
            } else if (permissionType == 1 ){
               // Vector projectSaved = (Vector) request.getSession().getAttribute("AlreadySavedProjectsForDiscl");
                Vector projectSaved = (Vector) request.getSession().getAttribute("AlreadySavedProjectsForExt");
                if (projectSaved != null) {
                    int k = 0;
                    checkedPropsalProjects = new String[projectSaved.size()];
                    for (Iterator it = projectSaved.iterator(); it.hasNext();) {
                        CoiProposalBean savedpropsalBean = (CoiProposalBean) it.next();
                        checkedPropsalProjects[k] = savedpropsalBean.getProposalNumber() + ":" + savedpropsalBean.getTitle()+ ":" +savedpropsalBean.getSponsorName()+ ":" +savedpropsalBean.getStartDate()+ ":" +savedpropsalBean.getEndDate();
                        k++;
                    }
                }else {
                    request.removeAttribute("projectDetailsListInSeesion_ext");
                    if (operationType != null && operationType.equals("MODIFY")) {
                        request.removeAttribute("projectDetailsListInSeesion_ext");
                        request.setAttribute("ModuleCode", modulecode);
                       // removeUncheckedProjects(request);5
                         removeUncheckedProjectsForExt(request,disclNumber, seqNumber);
                    }
                    return "continue";
                }
            }

           else if (permissionType == 0 && (checkedPropsalProjects == null || checkedPropsalProjects.length == 0)) {
                request.removeAttribute("projectDetailsListInSeesion_ext");
                if (operationType != null && operationType.equals("MODIFY")) {
                    request.removeAttribute("projectDetailsListInSeesion_ext");
                    request.setAttribute("ModuleCode", modulecode);
                    //removeUncheckedProjects(request);5
                     removeUncheckedProjectsForExt(request,disclNumber, seqNumber);
                }
                return "continue";
            }
            //setting Proposal Based project to project List
             DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

             for (int i = 0; i < checkedPropsalProjects.length; i++) {
                String propVal = checkedPropsalProjects[i];
                String[] propValArr = propVal.split(":");
                projectDet = new CoiPersonProjectDetails();
                projectDet.setCoiProjectSponser(propValArr[2]);
                Integer s=propValArr[3].length();
                if(propValArr[3].length()<11){
                Date projectStartDate = new Date();
                Date projectEndDate = new Date();
                projectStartDate = dateFormat.parse(propValArr[3]);
                projectEndDate = dateFormat.parse(propValArr[4]);
                projectDet.setCoiProjectStartDate(projectStartDate);
                projectDet.setCoiProjectEndDate(projectEndDate);

                }
                else{
                    Date projectStartDate = new Date();
                    Date projectEndDate = new Date();
                    String startDateFormatted=propValArr[3].substring(0, 10);
                   // DateUtils dt=new DateUtils();
                   //  projectStartDate=dt.getSQLDate(startDateFormatted);
                    String endDateFormatted=propValArr[6].substring(0, 10);
                    projectStartDate = dateFormat.parse(startDateFormatted);
                    projectEndDate = dateFormat.parse(endDateFormatted);
                    projectDet.setCoiProjectStartDate(projectStartDate);
                    projectDet.setCoiProjectEndDate(projectEndDate);
                }
                String transactoinId="getPropPjtDetForDiscl";
                String commentsStr="";
                Vector finList = (Vector) request.getSession().getAttribute("financialEntityList_ext");
                Vector typeList = (Vector) request.getAttribute("typeList");
                ComboBoxBean typeBean = (ComboBoxBean) finList.get(count);
                String nameType = typeBean.getCode();

                 String[] finArray = nameType.split(":");
                 //count++;
                 Vector deatilsofProject = coiCommonService.getProjectDetails(disclNumber, seqNumber, propValArr[0], request, transactoinId);
                        try{
                            if (deatilsofProject != null && !deatilsofProject.isEmpty()) {
                                for (int k = 0; k < deatilsofProject.size(); k++) {
                                    CoiProjectEntityDetailsBean coiProjectEntityDetailsBean = new CoiProjectEntityDetailsBean();
                                    coiProjectEntityDetailsBean = (CoiProjectEntityDetailsBean) deatilsofProject.get(k);
                                    commentsStr=coiProjectEntityDetailsBean.getComments();
                                    if(commentsStr==null){
                                        commentsStr="";
                                    }
                                    if (finArray[0].equals(coiProjectEntityDetailsBean.getEntityNumber())
                                            && Integer.parseInt(finArray[1]) == coiProjectEntityDetailsBean.getEntitySequenceNumber()) {
                                       // statusCode = String.valueOf(coiProjectEntityDetailsBean.getEntityStatusCode());
                                        break;
                                    }
                                }
                            }
                        }
                        catch(Exception ext){
                           Logger.getLogger(Coi2ProjectAction.class.getName()).log(Level.SEVERE, null, ext);
                        }
//

                projectDet.setCoiProjectTitle(propValArr[1]);
                projectDet.setModuleCode(modulecode);
                projectDet.setModuleItemKey(propValArr[0]);
                projectDet.setRelationShipDescription(commentsStr);
                projectDetails.add(projectDet);
                  //--End---
            }

        } else if (projectType.equals(Protocol_Based)) {
            modulecode = ModuleConstants.PROTOCOL_MODULE_CODE;
            projectDetailArg.put("modulecode", modulecode);

            //String[] checkedProtocolBasedProjects = (String[]) request.getSession().getAttribute("checkedProtocolBasedProjects");
             Vector savedprojects = (Vector) request.getSession().getAttribute("AlreadySavedProjectsForExt");
             String[] checkedProtocolBasedProjects = new String[savedprojects.size()];


            if ((checkedProtocolBasedProjects == null || checkedProtocolBasedProjects.length == 0) && permissionType == 2) {
                request.removeAttribute("projectDetailsListInSeesion_ext");
                if (operationType != null && operationType.equals("MODIFY")) {
                    request.setAttribute("ModuleCode", modulecode);
                    //removeUncheckedProjects(request);5
                     removeUncheckedProjectsForExt(request,disclNumber, seqNumber);
                }
                return "continue";
            } else if (permissionType == 1) {
                Vector projectSaved = (Vector) request.getSession().getAttribute("AlreadySavedProjectsForExt");
                if (projectSaved != null) {
                    int k = 0;
                    checkedProtocolBasedProjects = new String[projectSaved.size()];
                    for (Iterator it = projectSaved.iterator(); it.hasNext();) {
                        CoiProtocolInfoBean savedprotocolBean = (CoiProtocolInfoBean) it.next();
                        checkedProtocolBasedProjects[k] = savedprotocolBean.getProtocolNumber() + ":" + savedprotocolBean.getTitle();
                        k++;
                    }
                } else {
                    request.removeAttribute("projectDetailsListInSeesion_ext");
                    if (operationType != null && operationType.equals("MODIFY")) {
                        request.removeAttribute("projectDetailsListInSeesion_ext");
                        request.setAttribute("ModuleCode", modulecode);
                      //  removeUncheckedProjects(request);5
                         removeUncheckedProjectsForExt(request,disclNumber, seqNumber);
                    }
                    return "continue";
                }
            } else if (permissionType == 0) {
                request.removeAttribute("projectDetailsListInSeesion_ext");
                if (operationType != null && operationType.equals("MODIFY")) {
                    request.removeAttribute("projectDetailsListInSeesion_ext");
                    request.setAttribute("ModuleCode", modulecode);
                   // removeUncheckedProjects(request);5
                     removeUncheckedProjectsForExt(request,disclNumber, seqNumber);
                }
                return "continue";
            }
            //setting Protocol Based project to project List
            for (int i = 0; i < checkedProtocolBasedProjects.length; i++) {
                String protocolVal = checkedProtocolBasedProjects[i];
                String[] protocolValArr = protocolVal.split(":");
                projectDet = new CoiPersonProjectDetails();
                projectDet.setCoiProjectTitle(protocolValArr[1]);
                projectDet.setModuleCode(modulecode);
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
                projectDet.setModuleItemKey(protocolValArr[0]);
                projectDetails.add(projectDet);
            }
        }else if (projectType.equals(IACUC_Protocol)) {
            modulecode = ModuleConstants.IACUC_MODULE_CODE;
            projectDetailArg.put("modulecode", modulecode);
            //String[] checkedProtocolBasedProjects = (String[]) request.getSession().getAttribute("checkedProtocolBasedProjects");
             Vector savedprojects = (Vector) request.getSession().getAttribute("AlreadySavedProjectsForExt");
             String[] checkediacucProtocolBasedProjects = new String[savedprojects.size()];
            if ((checkediacucProtocolBasedProjects == null || checkediacucProtocolBasedProjects.length == 0) && permissionType == 2) {
                request.removeAttribute("projectDetailsListInSeesion_ext");
                if (operationType != null && operationType.equals("MODIFY")) {
                    request.setAttribute("ModuleCode", modulecode);
                    //removeUncheckedProjects(request);5
                     removeUncheckedProjectsForExt(request,disclNumber, seqNumber);
                }
                return "continue";
            } else if (permissionType == 1) {
                Vector projectSaved = (Vector) request.getSession().getAttribute("AlreadySavedProjectsForExt");
                if (projectSaved != null) {
                    int k = 0;
                    checkediacucProtocolBasedProjects = new String[projectSaved.size()];
                    for (Iterator it = projectSaved.iterator(); it.hasNext();) {
                        CoiProtocolInfoBean savediacucprotocolBean = (CoiProtocolInfoBean) it.next();
                        checkediacucProtocolBasedProjects[k] = savediacucprotocolBean.getProtocolNumber() + ":" + savediacucprotocolBean.getTitle();
                        k++;
                    }
                } else {
                    request.removeAttribute("projectDetailsListInSeesion_ext");
                    if (operationType != null && operationType.equals("MODIFY")) {
                        request.removeAttribute("projectDetailsListInSeesion_ext");
                        request.setAttribute("ModuleCode", modulecode);
                      //  removeUncheckedProjects(request);5
                         removeUncheckedProjectsForExt(request,disclNumber, seqNumber);
                    }
                    return "continue";
                }
            } else if (permissionType == 0) {
                request.removeAttribute("projectDetailsListInSeesion_ext");
                if (operationType != null && operationType.equals("MODIFY")) {
                    request.removeAttribute("projectDetailsListInSeesion_ext");
                    request.setAttribute("ModuleCode", modulecode);
                   // removeUncheckedProjects(request);5
                     removeUncheckedProjectsForExt(request,disclNumber, seqNumber);
                }
                return "continue";
            }
            //setting Protocol Based project to project List
            for (int i = 0; i < checkediacucProtocolBasedProjects.length; i++) {
                String protocolVal = checkediacucProtocolBasedProjects[i];
                String[] protocolValArr = protocolVal.split(":");
                projectDet = new CoiPersonProjectDetails();
                projectDet.setCoiProjectTitle(protocolValArr[1]);
                projectDet.setModuleCode(modulecode);
                projectDet.setModuleItemKey(protocolValArr[0]);
                 if(protocolValArr.length > 2) {
                        Date date3 = null;
                         String d3=protocolValArr[2];
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

                    if(protocolValArr.length > 3) {
                      Date date4 = null;
                     String d4=protocolValArr[3];
                    try {
                      DateFormat formatter ;
                      formatter = new SimpleDateFormat("yyyy-MM-dd");
                      date4 = (Date)formatter.parse(d4);
                       } catch (ParseException e)
                      {}projectDet.setCoiProjectEndDate(date4);
                    } else {
                       projectDet.setCoiProjectEndDate(null);
                    }
                projectDetails.add(projectDet);
            }
        }
        else if (projectType.equals(Other)) {
            modulecode = 0;
            projectDetailArg.put("modulecode", modulecode);
            if (permissionType == 2) {
                if (operationType != null && operationType.equals("MODIFY")) {
                    request.setAttribute("ModuleCode", modulecode);
                }
                //     return "continue";
            } else if (permissionType == 1) {
                if (operationType != null && operationType.equals("MODIFY")) {
                    request.setAttribute("ModuleCode", modulecode);
                }
                //    return "continue";

            } else if (permissionType == 0) {
                if (operationType != null && operationType.equals("MODIFY")) {
                    request.setAttribute("ModuleCode", modulecode);
                }
                //  return "continue";
            }
        } else if (projectType.equals(Award_Based)) {
            modulecode = ModuleConstants.AWARD_MODULE_CODE;
            projectDetailArg.put("modulecode", modulecode);
            Vector savedaward = (Vector) request.getSession().getAttribute("AlreadySavedProjectsForExt");
            //String[] checkedAwardBasedProjects = (String[]) request.getSession().getAttribute("checkedAwardBasedProjects");
            String[] checkedAwardBasedProjects = new String[savedaward.size()];
            if ((checkedAwardBasedProjects == null || checkedAwardBasedProjects.length == 0) && permissionType == 2) {
                request.removeAttribute("projectDetailsListInSeesion_ext");
                if (operationType != null && operationType.equals("MODIFY")) {
                    request.setAttribute("ModuleCode", modulecode);
                   // removeUncheckedProjects(request);5
                     removeUncheckedProjectsForExt(request,disclNumber, seqNumber);
                }
                return "continue";
            } else if (permissionType == 1) {
                Vector projectSaved = (Vector) request.getSession().getAttribute("AlreadySavedProjectsForExt");
                if (projectSaved != null) {
                    int k = 0;
                    checkedAwardBasedProjects = new String[projectSaved.size()];
                    for (Iterator it = projectSaved.iterator(); it.hasNext();) {
                        CoiAwardInfoBean savedAwardBean = (CoiAwardInfoBean) it.next();
                        checkedAwardBasedProjects[k] = savedAwardBean.getMitAwardNumber() + ":" + savedAwardBean.getMitAwardNumber();
                        k++;
                    }
                } else {
                    request.removeAttribute("projectDetailsListInSeesion_ext");
                    if (operationType != null && operationType.equals("MODIFY")) {
                        request.removeAttribute("projectDetailsListInSeesion_ext");
                        request.setAttribute("ModuleCode", modulecode);
                       // removeUncheckedProjects(request);5
                         removeUncheckedProjectsForExt(request,disclNumber, seqNumber);
                    }
                    return "continue";
                }
            } else if (permissionType == 0) {
                request.removeAttribute("projectDetailsListInSeesion_ext");
                if (operationType != null && operationType.equals("MODIFY")) {
                    request.removeAttribute("projectDetailsListInSeesion_ext");
                    request.setAttribute("ModuleCode", modulecode);
                    //removeUncheckedProjects(request);
                    removeUncheckedProjectsForExt(request,disclNumber, seqNumber);
                }
                return "continue";
            }
            //setting Award based project to project List
            for (int i = 0; i < checkedAwardBasedProjects.length; i++) {
                String awardVal = checkedAwardBasedProjects[i];
                String[] awardValValArr = awardVal.split(":");
                projectDet = new CoiPersonProjectDetails();
                projectDet.setAwardTitle(awardValValArr[1]);
                projectDet.setModuleItemKey(awardValValArr[0]);
                projectDet.setModuleCode(modulecode);
                projectDetails.add(projectDet);
            }

        }
        }catch (Exception e){
            Logger.getLogger(Coi2ProjectAction.class.getName()).log(Level.SEVERE, null, e);
        }
        //request.setAttribute("projectDetailsList", projectDetails);
        request.setAttribute("projectDetailsList_ext", projectDetails);
        //request.getSession().setAttribute("projectDetailsListInSeesion", projectDetails);//keeping project list in session
        request.getSession().setAttribute("projectDetailsListInSeesion_ext", projectDetails);
         request.getSession().setAttribute("projectDetailsList_ext", projectDetails);
        request.setAttribute("projectDetailsList", projectDetails);

        if ((projectDetails == null || projectDetails.isEmpty()) && projectType.equals(Other)) {
            CoiPersonProjectDetails coiPersonProjectDetails = new CoiPersonProjectDetails();
            coiPersonProjectDetails.setModuleCode(ModuleConstants.COI_EVENT_OTHER);
            projectDetails.add(coiPersonProjectDetails);
           // request.setAttribute("projectDetailsList", projectDetails);
            request.setAttribute("projectDetailsList_ext", projectDetails);
            request.getSession().setAttribute("projectDetailsList_ext", projectDetails);
        }
        Format formatter = new SimpleDateFormat("dd-MMM-yy");
        ///for next project------
        for (Iterator it = projectDetails.iterator(); it.hasNext();) {
            CoiPersonProjectDetails coiPersonProjectDetails = (CoiPersonProjectDetails) it.next();
            if (coiPersonProjectDetails.isDataSaved() == false) {
                CoiPersonProjectDetailsForm coiPersonProjectDetailsForm = (CoiPersonProjectDetailsForm) actionForm;
                coiPersonProjectDetailsForm.setModuleItemKey(coiPersonProjectDetails.getModuleItemKey());
                coiPersonProjectDetailsForm.setCoiProjectTitle(coiPersonProjectDetails.getCoiProjectTitle());
                coiPersonProjectDetailsForm.setAwardTitle(coiPersonProjectDetails.getCoiProjectTitle());
                coiPersonProjectDetailsForm.setCoiProjectSponser(coiPersonProjectDetails.getCoiProjectSponser());
                coiPersonProjectDetailsForm.setCoiProjectStartDate((coiPersonProjectDetails.getCoiProjectStartDate()).toString());
                String moduleItemKeyAdded = coiPersonProjectDetails.getModuleItemKey();
                String projectSponser = coiPersonProjectDetails.getCoiProjectSponser();

                 if (projectType.equals(Proposal_Based)) {
                coiPersonProjectDetailsForm.setCoiProjectSponser(coiPersonProjectDetails.getCoiProjectSponser());
                String startdate = formatter.format(coiPersonProjectDetails.getCoiProjectStartDate());
                coiPersonProjectDetailsForm.setCoiProjectStartDate(startdate);
                String endDate =  formatter.format(coiPersonProjectDetails.getCoiProjectEndDate());
                coiPersonProjectDetailsForm.setCoiProjectEndDate(endDate);
                 }
               break;
            }
        }

        return "success";
    }
private Vector getFinEntityForExt(HttpServletRequest request, HashMap hmData) throws IOException, Exception {
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
       
       request.getSession().setAttribute("finEntityComboList_ext", finEntityListNew);
        return finEntityListNew;
    }
     private void removeUncheckedProjectsForExt(HttpServletRequest request,String disclNumber,Integer seqNumber) {
        CoiProjectService coiProjectService = CoiProjectService.getInstance();
        Integer moduleCode = (Integer) request.getAttribute("ModuleCode");
         Vector savedProjects = (Vector) request.getSession().getAttribute("AlreadySavedProjectsForExt");
        Vector projectDetailsList = (Vector) request.getSession().getAttribute("projectDetailsListInSeesion_ext");
     if (moduleCode == ModuleConstants.COI_EVENT_PROPOSAL) {
            for (Iterator it2 = savedProjects.iterator(); it2.hasNext();) {
                CoiProposalBean propsalBean = (CoiProposalBean) it2.next();
                boolean present = false;
                if (projectDetailsList != null) {
                    for (Iterator it = projectDetailsList.iterator(); it.hasNext();) {
                        CoiPersonProjectDetails coiPersonProjectDetails = (CoiPersonProjectDetails) it.next();
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
                        CoiPersonProjectDetails coiPersonProjectDetails = (CoiPersonProjectDetails) it.next();
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
        } else if (moduleCode == ModuleConstants.COI_EVENT_IACUC) {

            for (Iterator it3 = savedProjects.iterator(); it3.hasNext();) {
                CoiProtocolInfoBean protocolBean = (CoiProtocolInfoBean) it3.next();
                boolean present = false;
                if (projectDetailsList != null) {
                    for (Iterator it = projectDetailsList.iterator(); it.hasNext();) {
                        CoiPersonProjectDetails coiPersonProjectDetails = (CoiPersonProjectDetails) it.next();
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
        }
        else if (moduleCode == ModuleConstants.COI_EVENT_AWARD) {
            for (Iterator it2 = savedProjects.iterator(); it2.hasNext();) {
                CoiAwardInfoBean awardBean = (CoiAwardInfoBean) it2.next();
                boolean present = false;
                if (projectDetailsList != null) {
                    for (Iterator it = projectDetailsList.iterator(); it.hasNext();) {
                        CoiPersonProjectDetails coiPersonProjectDetails = (CoiPersonProjectDetails) it.next();
                        if (coiPersonProjectDetails.getModuleItemKey().equals(awardBean.getMitAwardNumber())) {
                            present = true;
                            break;
                        }
                    }
                }
                if (present == false) {
                    try {
                        coiProjectService.removeProjectAndDetails(disclNumber, seqNumber, awardBean.getMitAwardNumber(), "updateRemoveAllProject", request);
                    } catch (Exception ex) {
                        Logger.getLogger(Coi2ProjectAction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
    private String getProjectDetailsAndFinancialEntityDetails(HttpServletRequest request, ActionMapping actionMapping, ActionForm actionForm) throws Exception {
        HttpSession session = request.getSession();
        String operationType = request.getParameter("operationType");
        PersonInfoBean person = (PersonInfoBean) session.getAttribute("person");
        HashMap hmData = new HashMap();
        hmData.put("personId", person.getPersonID());
        CoiDisclosureBean currDisclosure = this.getCurrentDisclPerson(request);
        request.getSession().setAttribute("disclosureBeanSession", currDisclosure);
        String projectType = (String) request.getSession().getAttribute("projectType");
        HashMap projectDetailArg = new HashMap();
        projectDetailArg.put("disclosurenumber", currDisclosure.getCoiDisclosureNumber());
        projectDetailArg.put("sequencenumber", currDisclosure.getSequenceNumber());
        WebTxnBean webTxnBean = new WebTxnBean();
        Vector projectDetails = new Vector();
        CoiPersonProjectDetails projectDet = new CoiPersonProjectDetails();
        Integer modulecode = null;
        HashMap thisUserRights = (HashMap) request.getAttribute(RIGHT);
        Integer permissionType = (Integer) thisUserRights.get(CoiConstants.DISCL);
        if (projectType.equals(Proposal_Based)) {
            modulecode = ModuleConstants.PROPOSAL_DEV_MODULE_CODE;
            projectDetailArg.put("modulecode", modulecode);
            String[] checkedPropsalProjects = (String[]) request.getSession().getAttribute("checkedPropsalProjects");
            if ((checkedPropsalProjects == null || checkedPropsalProjects.length == 0) && permissionType == 2){
                request.removeAttribute("projectDetailsListInSeesion");
                if (operationType != null && operationType.equals("MODIFY")) {
                    request.removeAttribute("projectDetailsListInSeesion");
                    request.setAttribute("ModuleCode", modulecode);
                    removeUncheckedProjects(request);
                }
                return "continue";
            } else if (permissionType == 1 ){
                Vector projectSaved = (Vector) request.getSession().getAttribute("AlreadySavedProjectsForDiscl");
                if (projectSaved != null) {
                    int k = 0;
                    checkedPropsalProjects = new String[projectSaved.size()];
                    for (Iterator it = projectSaved.iterator(); it.hasNext();) {
                        CoiProposalBean savedpropsalBean = (CoiProposalBean) it.next();
                        checkedPropsalProjects[k] = savedpropsalBean.getProposalNumber() + ":" + savedpropsalBean.getTitle()+ ":" +savedpropsalBean.getSponsorName()+ ":" +savedpropsalBean.getStartDate()+ ":" +savedpropsalBean.getEndDate();
                        k++;
                    }
                }else {
                    request.removeAttribute("projectDetailsListInSeesion");
                    if (operationType != null && operationType.equals("MODIFY")) {
                        request.removeAttribute("projectDetailsListInSeesion");
                        request.setAttribute("ModuleCode", modulecode);
                        removeUncheckedProjects(request);
                    }
                    return "continue";
                }
            }
           else if (permissionType == 0 && (checkedPropsalProjects == null || checkedPropsalProjects.length == 0)) {
                request.removeAttribute("projectDetailsListInSeesion");
                if (operationType != null && operationType.equals("MODIFY")) {
                    request.removeAttribute("projectDetailsListInSeesion");
                    request.setAttribute("ModuleCode", modulecode);
                    removeUncheckedProjects(request);
                }
                return "continue";
            }
            //setting Proposal Based project to project List
             DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = 0; i < checkedPropsalProjects.length; i++) {
                String propVal = checkedPropsalProjects[i];
                String[] propValArr = propVal.split(";");
                projectDet = new CoiPersonProjectDetails();
                //Added By Vineeth on 10/12/2010--Start---
                projectDet.setCoiProjectSponser(propValArr[2]);
                if(propValArr.length > 3) {
                    Date date = null;
                     String d=propValArr[3];

                    try {
                        date = dateFormat.parse(d);

                       } catch (ParseException e){}
                        projectDet.setCoiProjectStartDate(date);
                    }

                    if(propValArr.length > 4) {
                         Date date1=null;
                        String d1=propValArr[4];
                        try {
                             date1 = dateFormat.parse(d1);
                           } catch (ParseException e)
                          {}
                        projectDet.setCoiProjectEndDate(date1);
                    }

//                Integer s=propValArr[3].length();
//                if(propValArr[3].length()<11){
//                Date projectStartDate = new Date();
//                Date projectEndDate = new Date();
//                projectStartDate = dateFormat.parse(propValArr[3]);
//                projectEndDate = dateFormat.parse(propValArr[4]);
//                projectDet.setCoiProjectStartDate(projectStartDate);
//                projectDet.setCoiProjectEndDate(projectEndDate);

         //       }
//                else{
//                    Date projectStartDate = new Date();
//                    Date projectEndDate = new Date();
//                    String startDateFormatted=propValArr[3].substring(0, 10);
//                   // DateUtils dt=new DateUtils();
//                   //  projectStartDate=dt.getSQLDate(startDateFormatted);
//                    String endDateFormatted=propValArr[6].substring(0, 10);
//                    projectStartDate = dateFormat.parse(startDateFormatted);
//                    projectEndDate = dateFormat.parse(endDateFormatted);
//                    projectDet.setCoiProjectStartDate(projectStartDate);
//                    projectDet.setCoiProjectEndDate(projectEndDate);
//                }
                projectDet.setCoiProjectTitle(propValArr[1]);
                projectDet.setModuleCode(modulecode);
                projectDet.setModuleItemKey(propValArr[0]);
                projectDetails.add(projectDet);
                  //--End---
            }
        } else if (projectType.equals(Protocol_Based)) {
            modulecode = ModuleConstants.PROTOCOL_MODULE_CODE;
            projectDetailArg.put("modulecode", modulecode);
            String[] checkedProtocolBasedProjects = (String[]) request.getSession().getAttribute("checkedProtocolBasedProjects");
            if ((checkedProtocolBasedProjects == null || checkedProtocolBasedProjects.length == 0) && permissionType == 2) {
                request.removeAttribute("projectDetailsListInSeesion");
                if (operationType != null && operationType.equals("MODIFY")) {
                    request.setAttribute("ModuleCode", modulecode);
                    removeUncheckedProjects(request);
                }
                return "continue";
            } else if (permissionType == 1) {
                Vector projectSaved = (Vector) request.getSession().getAttribute("AlreadySavedProjectsForDiscl");
                if (projectSaved != null) {
                    int k = 0;
                    checkedProtocolBasedProjects = new String[projectSaved.size()];
                    for (Iterator it = projectSaved.iterator(); it.hasNext();) {
                        CoiProtocolInfoBean savedprotocolBean = (CoiProtocolInfoBean) it.next();
                        checkedProtocolBasedProjects[k] = savedprotocolBean.getProtocolNumber() + ":" + savedprotocolBean.getTitle();
                        k++;
                    }
                } else {
                    request.removeAttribute("projectDetailsListInSeesion");
                    if (operationType != null && operationType.equals("MODIFY")) {
                        request.removeAttribute("projectDetailsListInSeesion");
                        request.setAttribute("ModuleCode", modulecode);
                        removeUncheckedProjects(request);
                    }
                    return "continue";
                }
            } else if (permissionType == 0  && (checkedProtocolBasedProjects == null || checkedProtocolBasedProjects.length == 0)) {
                request.removeAttribute("projectDetailsListInSeesion");
                if (operationType != null && operationType.equals("MODIFY")) {
                    request.removeAttribute("projectDetailsListInSeesion");
                    request.setAttribute("ModuleCode", modulecode);
                    removeUncheckedProjects(request);
                }
                return "continue";
            }
            //setting Protocol Based project to project List
            for (int i = 0; i < checkedProtocolBasedProjects.length; i++) {
                String protocolVal = checkedProtocolBasedProjects[i];
                String[] protocolValArr = protocolVal.split(";");
                projectDet = new CoiPersonProjectDetails();
                projectDet.setCoiProjectTitle(protocolValArr[1]);
                projectDet.setModuleCode(modulecode);
                projectDet.setModuleItemKey(protocolValArr[0]);
               if(protocolValArr.length > 2) {
                    Date date = null;
                     String d=protocolValArr[2];

                    try {
                     DateFormat formatter ;
                      formatter = new SimpleDateFormat("yyyy-MM-dd");
                      date = formatter.parse(d);

                       } catch (ParseException e){}
                        projectDet.setCoiProjectStartDate(date);
                    }else {
                        projectDet.setCoiProjectStartDate(null);
                    }

                    if(protocolValArr.length > 3) {
                         Date date1=null;
                        String d1=protocolValArr[3];
                        try {
                          DateFormat formatter ;
                          formatter = new SimpleDateFormat("yyyy-MM-dd");
                          date1 = formatter.parse(d1);
                           } catch (ParseException e)
                          {}
                        projectDet.setCoiProjectEndDate(date1);
                    } else{
                        projectDet.setCoiProjectEndDate(null);
                    }
                projectDetails.add(projectDet);
            }
        } else if (projectType.equals(IACUC_Protocol)) {
            modulecode = ModuleConstants.IACUC_MODULE_CODE;
            projectDetailArg.put("modulecode", modulecode);
            String[] checkediacucProtocolBasedProjects = (String[]) request.getSession().getAttribute("checkedIacucProtocolBasedProject");
            if ((checkediacucProtocolBasedProjects == null || checkediacucProtocolBasedProjects.length == 0) && permissionType == 2) {
                request.removeAttribute("projectDetailsListInSeesion");
                if (operationType != null && operationType.equals("MODIFY")) {
                    request.setAttribute("ModuleCode", modulecode);
                    removeUncheckedProjects(request);
                }
                return "continue";
            } else if (permissionType == 1) {
                Vector projectSaved = (Vector) request.getSession().getAttribute("AlreadySavedProjectsForDiscl");
                if (projectSaved != null) {
                    int k = 0;
                    checkediacucProtocolBasedProjects = new String[projectSaved.size()];
                    for (Iterator it = projectSaved.iterator(); it.hasNext();) {
                        CoiProtocolInfoBean savedprotocolBean = (CoiProtocolInfoBean) it.next();
                        checkediacucProtocolBasedProjects[k] = savedprotocolBean.getProtocolNumber() + ":" + savedprotocolBean.getTitle();
                        k++;
                    }
                } else {
                    request.removeAttribute("projectDetailsListInSeesion");
                    if (operationType != null && operationType.equals("MODIFY")) {
                        request.removeAttribute("projectDetailsListInSeesion");
                        request.setAttribute("ModuleCode", modulecode);
                        removeUncheckedProjects(request);
                    }
                    return "continue";
                }
            } else if (permissionType == 0 && (checkediacucProtocolBasedProjects == null || checkediacucProtocolBasedProjects.length == 0)) {
                request.removeAttribute("projectDetailsListInSeesion");
                if (operationType != null && operationType.equals("MODIFY")) {
                    request.removeAttribute("projectDetailsListInSeesion");
                    request.setAttribute("ModuleCode", modulecode);
                    removeUncheckedProjects(request);
                }
                return "continue";
            }
            //setting Protocol Based project to project List
            for (int i = 0; i < checkediacucProtocolBasedProjects.length; i++) {
                String protocolVal = checkediacucProtocolBasedProjects[i];
                String[] protocolValArr = protocolVal.split(";");
                projectDet = new CoiPersonProjectDetails();
                projectDet.setCoiProjectTitle(protocolValArr[1]);
                projectDet.setModuleCode(modulecode);
                projectDet.setModuleItemKey(protocolValArr[0]);
                if(protocolValArr.length > 2) {
                    Date date = null;
                     String d=protocolValArr[2];

                    try {
                     DateFormat formatter ;
                      formatter = new SimpleDateFormat("yyyy-MM-dd");
                      date = formatter.parse(d);

                       } catch (ParseException e){}
                        projectDet.setCoiProjectStartDate(date);
                    }else {
                        projectDet.setCoiProjectStartDate(null);
                    }

                    if(protocolValArr.length > 3) {
                         Date date1=null;
                        String d1=protocolValArr[3];
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
                projectDetails.add(projectDet);
            }
        }
        else if (projectType.equals(Other)) {
            modulecode = 0;
            projectDetailArg.put("modulecode", modulecode);
            if (permissionType == 2) {
                if (operationType != null && operationType.equals("MODIFY")) {
                    request.setAttribute("ModuleCode", modulecode);
                }
                //     return "continue";
            } else if (permissionType == 1) {
                if (operationType != null && operationType.equals("MODIFY")) {
                    request.setAttribute("ModuleCode", modulecode);
                }
                //    return "continue";

            } else if (permissionType == 0) {
                if (operationType != null && operationType.equals("MODIFY")) {
                    request.setAttribute("ModuleCode", modulecode);
                }
                //  return "continue";
            }
        } else if (projectType.equals(Award_Based)) {
            modulecode = ModuleConstants.AWARD_MODULE_CODE;
            projectDetailArg.put("modulecode", modulecode);
            String[] checkedAwardBasedProjects = (String[]) request.getSession().getAttribute("checkedAwardBasedProjects");
            if ((checkedAwardBasedProjects == null || checkedAwardBasedProjects.length == 0) && permissionType == 2) {
                request.removeAttribute("projectDetailsListInSeesion");
                if (operationType != null && operationType.equals("MODIFY")) {
                    request.setAttribute("ModuleCode", modulecode);
                    removeUncheckedProjects(request);
                }
                return "continue";
            } else if (permissionType == 1) {
                Vector projectSaved = (Vector) request.getSession().getAttribute("AlreadySavedProjectsForDiscl");
                if (projectSaved != null) {
                    int k = 0;
                    checkedAwardBasedProjects = new String[projectSaved.size()];
                    for (Iterator it = projectSaved.iterator(); it.hasNext();) {
                        CoiAwardInfoBean savedAwardBean = (CoiAwardInfoBean) it.next();
                        checkedAwardBasedProjects[k] = savedAwardBean.getMitAwardNumber() + ":" + savedAwardBean.getMitAwardNumber();
                        k++;
                    }
                } else {
                    request.removeAttribute("projectDetailsListInSeesion");
                    if (operationType != null && operationType.equals("MODIFY")) {
                        request.removeAttribute("projectDetailsListInSeesion");
                        request.setAttribute("ModuleCode", modulecode);
                        removeUncheckedProjects(request);
                    }
                    return "continue";
                }
            } else if (permissionType == 0 && (checkedAwardBasedProjects == null || checkedAwardBasedProjects.length == 0)) {
                request.removeAttribute("projectDetailsListInSeesion");
                if (operationType != null && operationType.equals("MODIFY")) {
                    request.removeAttribute("projectDetailsListInSeesion");
                    request.setAttribute("ModuleCode", modulecode);
                    removeUncheckedProjects(request);
                }
                return "continue";
            }
            //setting Award based project to project List
            for (int i = 0; i < checkedAwardBasedProjects.length; i++) {
                String awardVal = checkedAwardBasedProjects[i];
                String[] awardValValArr = awardVal.split(";");
                projectDet = new CoiPersonProjectDetails();
                projectDet.setCoiProjectTitle(awardValValArr[0]);
                projectDet.setAwardTitle(awardValValArr[1]);
                projectDet.setModuleItemKey(awardValValArr[0]);
                projectDet.setModuleCode(modulecode);
                if(awardValValArr.length > 2) {
                    Date date = null;
                     String d=awardValValArr[2];

                    try {
                     DateFormat formatter ;
                      formatter = new SimpleDateFormat("yyyy-MM-dd");
                      date = formatter.parse(d);

                       } catch (ParseException e){}
                        projectDet.setCoiProjectStartDate(date);
                    }else {
                        projectDet.setCoiProjectStartDate(null);
                    }

                    if(awardValValArr.length > 3) {
                         Date date1=null;
                        String d1=awardValValArr[3];
                        try {
                          DateFormat formatter ;
                          formatter = new SimpleDateFormat("yyyy-MM-dd");
                          date1 = formatter.parse(d1);
                           } catch (ParseException e)
                          {}
                        projectDet.setCoiProjectEndDate(date1);
                    }
                    else {
                        projectDet.setCoiProjectEndDate(null);
                    }
                projectDetails.add(projectDet);
            }
        }
        else if(projectType.equals(Travel_Based)) {
            modulecode = ModuleConstants.TRAVEL_MODULE_CODE;
            if (operationType != null && operationType.equals("MODIFY")) {
                    request.setAttribute("ModuleCode", modulecode);
            }
            projectDet = new CoiPersonProjectDetails();
            projectDet = (CoiPersonProjectDetails)session.getAttribute("travelPjtDetails");
            projectDet.setModuleCode(modulecode);
            projectDetails.add(projectDet);
        }
        request.setAttribute("projectDetailsList", projectDetails);
        request.getSession().setAttribute("projectDetailsListInSeesion", projectDetails);//keeping project list in session
        
        //Get financial Entity
        Vector finEntityCombo = new Vector();
        hmData = new HashMap();
        hmData.put("personId", person.getPersonID());
        finEntityCombo = getFinEntity(request, hmData);//calling function to get financail entity
        String[] entityCode = new String[finEntityCombo.size()];
        int i = 0;
        for (Iterator it = finEntityCombo.iterator(); it.hasNext();) {
            CoiFinancialEntityBean entity = (CoiFinancialEntityBean) it.next();
            if(entity.getStatusCode()==1){
            entityCode[i] = entity.getCode();
            i++;}
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
            CoiPersonProjectDetails coiPersonProjectDetails = new CoiPersonProjectDetails();
            coiPersonProjectDetails.setModuleCode(ModuleConstants.COI_EVENT_OTHER);
            projectDetails.add(coiPersonProjectDetails);
            request.setAttribute("projectDetailsList", projectDetails);
        }
        Format formatter = new SimpleDateFormat("dd-MMM-yy");
        List protList = new ArrayList();
        CoiProjectEntityDetailsBean bean = new CoiProjectEntityDetailsBean();
        for (Iterator it = projectDetails.iterator(); it.hasNext();) {
            CoiPersonProjectDetails coiPersonProjectDetails = (CoiPersonProjectDetails) it.next();
            if (coiPersonProjectDetails.isDataSaved() == false) {
                CoiPersonProjectDetailsForm coiPersonProjectDetailsForm = (CoiPersonProjectDetailsForm) actionForm;
                coiPersonProjectDetailsForm.setModuleItemKey(coiPersonProjectDetails.getModuleItemKey());
                coiPersonProjectDetailsForm.setCoiProjectTitle(coiPersonProjectDetails.getCoiProjectTitle());
                coiPersonProjectDetailsForm.setAwardTitle(coiPersonProjectDetails.getAwardTitle());
                coiPersonProjectDetailsForm.setEventName(coiPersonProjectDetails.getEventName());
                coiPersonProjectDetailsForm.setDestination(coiPersonProjectDetails.getDestination());
                coiPersonProjectDetailsForm.setPurpose(coiPersonProjectDetails.getPurpose());
                
                String moduleItemKeyAdded = coiPersonProjectDetails.getModuleItemKey();
                String projectSponser = coiPersonProjectDetails.getCoiProjectSponser();
                if(coiPersonProjectDetails.getCoiProjectStartDate() != null) {
                    String startdate = formatter.format(coiPersonProjectDetails.getCoiProjectStartDate());
                    coiPersonProjectDetailsForm.setCoiProjectStartDate(startdate);
                }else {
                    coiPersonProjectDetailsForm.setCoiProjectStartDate("");
                }
                if(coiPersonProjectDetails.getCoiProjectEndDate() != null) {
                    String endDate =  formatter.format(coiPersonProjectDetails.getCoiProjectEndDate());
                coiPersonProjectDetailsForm.setCoiProjectEndDate(endDate);
                } else{
                    coiPersonProjectDetailsForm.setCoiProjectEndDate("");
                }
                //Added By vineeth---Start---
                 if (projectType.equals(Proposal_Based)) {
                coiPersonProjectDetailsForm.setCoiProjectSponser(coiPersonProjectDetails.getCoiProjectSponser());
                String startdate = "",endDate = "";
                if(coiPersonProjectDetails.getCoiProjectStartDate()!=null){
                 startdate = formatter.format(coiPersonProjectDetails.getCoiProjectStartDate());
                }
                coiPersonProjectDetailsForm.setCoiProjectStartDate(startdate);
                if(coiPersonProjectDetails.getCoiProjectEndDate()!=null){
                 endDate =  formatter.format(coiPersonProjectDetails.getCoiProjectEndDate());
                }
                coiPersonProjectDetailsForm.setCoiProjectEndDate(endDate);
                 }
                if(projectType.equals(Travel_Based)) {
                    coiPersonProjectDetailsForm.setCoiProjectSponser(coiPersonProjectDetails.getCoiProjectSponser());
                    if(coiPersonProjectDetails.getCoiProjectFundingAmount() != null) {
                        coiPersonProjectDetailsForm.setCoiProjectFundingAmount(coiPersonProjectDetails.getCoiProjectFundingAmount());
                    }
                }
                //---end---
                //  String sponsor=coiPersonProjectDetailsForm.getCoiProjectSponser();
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
                   if (coiPersonProjectDetails.getModuleCode() == ModuleConstants.IACUC_MODULE_CODE) {
                        transactoinId = "getIcProtoPjtDetForDiscl";
                    }
                     if (coiPersonProjectDetails.getModuleCode() == ModuleConstants.TRAVEL_MODULE_CODE) {
                            transactoinId = "getTravelPjtDetForDiscl";
                       }
//                    if (coiPersonProjectDetails.getModuleCode() == Integer.parseInt(disclModuleCodeMap.get(ModuleCodeType.other.getValue()).toString())) {
//                        transactoinId = "getOtherProjects";
//                    }
                    Vector deatilsofProject = coiCommonService.getProjectDetails(disclNumber, seqNumber, coiPersonProjectDetails.getModuleItemKey(), request, transactoinId);
                   if (coiPersonProjectDetails.getModuleCode() == ModuleConstants.PROTOCOL_MODULE_CODE) {
                        if(deatilsofProject!=null && deatilsofProject.size()!= 0){
                            bean =  (CoiProjectEntityDetailsBean) deatilsofProject.get(0);
                            }
                           }
                    request.setAttribute("projectDetails", deatilsofProject);
                     request.setAttribute("protList", bean);
                }
                //CODE FOR UPDATION ENDS
                if(coiPersonProjectDetails.getModuleCode() == ModuleConstants.PROTOCOL_MODULE_CODE) {
                     HashMap inputData = new HashMap();
                     inputData.put("protocolNumber", coiPersonProjectDetails.getModuleItemKey());
                      Hashtable ProtoDetails = (Hashtable) webTxnBean.getResults(request, "getSelectedProtocolDetails", inputData);
                      Vector details = (Vector)ProtoDetails.get("getSelectedProtocolDetails");
                      if(details!=null && details.size()!= 0) {
                         bean =  (CoiProjectEntityDetailsBean) details.get(0);
                      }
                      request.setAttribute("protList", bean);
                }
                 if(coiPersonProjectDetails.getModuleCode() == ModuleConstants.IACUC_MODULE_CODE){
                     HashMap inputData = new HashMap();
                     inputData.put("protocolNumber", coiPersonProjectDetails.getModuleItemKey());
                      Hashtable ProtoDetails = (Hashtable) webTxnBean.getResults(request, "getSelectedIacucProtocolDetails", inputData);
                      Vector details = (Vector)ProtoDetails.get("getSelectedIacucProtocolDetails");
                      if(details!=null && details.size()!= 0) {
                         bean =  (CoiProjectEntityDetailsBean) details.get(0);
                      }
                      request.setAttribute("protList", bean);
                }

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
        if(savedProjects!=null){
        if (moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE) {

            for (Iterator it2 = savedProjects.iterator(); it2.hasNext();) {
                CoiProposalBean propsalBean = (CoiProposalBean) it2.next();
                boolean present = false;
                if (projectDetailsList != null) {
                    for (Iterator it = projectDetailsList.iterator(); it.hasNext();) {
                        CoiPersonProjectDetails coiPersonProjectDetails = (CoiPersonProjectDetails) it.next();
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
        } else if (moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE) {

            for (Iterator it2 = savedProjects.iterator(); it2.hasNext();) {
                CoiProtocolInfoBean protocolBean = (CoiProtocolInfoBean) it2.next();
                boolean present = false;
                if (projectDetailsList != null) {
                    for (Iterator it = projectDetailsList.iterator(); it.hasNext();) {
                        CoiPersonProjectDetails coiPersonProjectDetails = (CoiPersonProjectDetails) it.next();
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
        } else if (moduleCode == ModuleConstants.IACUC_MODULE_CODE) {

            for (Iterator it2 = savedProjects.iterator(); it2.hasNext();) {
                CoiProtocolInfoBean iacucprotocolBean = (CoiProtocolInfoBean) it2.next();
                boolean present = false;
                if (projectDetailsList != null) {
                    for (Iterator it = projectDetailsList.iterator(); it.hasNext();) {
                        CoiPersonProjectDetails coiPersonProjectDetails = (CoiPersonProjectDetails) it.next();
                        if (coiPersonProjectDetails.getModuleItemKey().equals(iacucprotocolBean.getProtocolNumber())) {
                            present = true;
                            break;
                        }
                    }
                }
                if (present == false) {
                    try {
                        coiProjectService.removeProjectAndDetails(disclNumber, seqNumber, iacucprotocolBean.getProtocolNumber(), "updateRemoveAllProject", request);
                    } catch (Exception ex) {
                        Logger.getLogger(Coi2ProjectAction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        else if (moduleCode == ModuleConstants.AWARD_MODULE_CODE) {

            for (Iterator it2 = savedProjects.iterator(); it2.hasNext();) {
                CoiAwardInfoBean awardBean = (CoiAwardInfoBean) it2.next();
                boolean present = false;
                if (projectDetailsList != null) {
                    for (Iterator it = projectDetailsList.iterator(); it.hasNext();) {
                        CoiPersonProjectDetails coiPersonProjectDetails = (CoiPersonProjectDetails) it.next();
                        if (coiPersonProjectDetails.getModuleItemKey().equals(awardBean.getMitAwardNumber())) {
                            present = true;
                            break;
                        }
                    }
                }
                if (present == false) {
                    try {
                        coiProjectService.removeProjectAndDetails(disclNumber, seqNumber, awardBean.getMitAwardNumber(), "updateRemoveAllProject", request);
                    } catch (Exception ex) {
                        Logger.getLogger(Coi2ProjectAction.class.getName()).log(Level.SEVERE, null, ex);
                    }
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

        // Edited by Vineetha to display  the discl details that have been selected from the pending disclosure droplist
//        if(request.getParameter("param2")!=null)
//        sequenceNumber = Integer.parseInt(request.getParameter("param2"));


        HttpSession session = request.getSession();
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
        Hashtable DisclData = (Hashtable) webTxn.getResults(request, "getDisclBySequnce", hmData);
        Vector DisclDet = (Vector) DisclData.get("getDisclBySequnce");
        if (DisclDet != null && DisclDet.size() > 0) {
            request.setAttribute("ApprovedDisclDetView", DisclDet);
     /* **
      for (Iterator it = DisclDet.iterator(); it.hasNext();) {
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
**  */
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

     /**
      * Method for saving project details in COI_PROJECT_DETAILS table and COI_DISCLOSURE_DETAILS if answers of all the questions are "NO"
      */


     private void saveProjectwithQstnAnsNo(HttpServletRequest request, String operationType, CoiPersonProjectDetailsForm coiPersonProjectDetailsForm,
            Integer moduleCode, Integer permissionType) throws Exception {
             CoiProjectService coiProjectService = CoiProjectService.getInstance();
         String[] totalComments=request.getParameterValues("comments");

            CoiDisclosureBean discl = new CoiDisclosureBean();
            if (operationType != null && !operationType.equals("MODIFY")) {
                discl = getCurrentDisclPerson(request);
                moduleCode = discl.getModuleCode();
            } else { ///Code for UPDATION STARTS
                String disclNumber = (String) request.getSession().getAttribute("DisclosureNumberInUpdateSession");
                Integer seqNumber = (Integer) request.getSession().getAttribute("SequenceNumberInUpdateSession");
              if(disclNumber==null){                  
                 disclNumber = (String) request.getSession().getAttribute("DisclNumber");         
              }
              if(seqNumber == null){
                 seqNumber= (Integer) request.getSession().getAttribute("DisclSeqNumber");
              }                
                moduleCode = (Integer) request.getSession().getAttribute("ModuleCodeInUpdateSession");
                discl.setCoiDisclosureNumber(disclNumber);
                discl.setSequenceNumber(seqNumber);
            } ///Code for UPDATION ENDS

            String moduleItemKey = coiPersonProjectDetailsForm.getModuleItemKey();
            Vector projectDetailsList = (Vector) request.getSession().getAttribute("projectDetailsListInSeesion");

            Vector finEntityComboList = (Vector) request.getSession().getAttribute("finEntityComboList");
            if(request.getSession().getAttribute("QstnAnsFlag") != null && request.getSession().getAttribute("QstnAnsFlag").toString().equals("false")) {
                finEntityComboList = new Vector();
                request.getSession().setAttribute("finEntityComboListcoi2",finEntityComboList);
            }else{
                 request.getSession().setAttribute("finEntityComboListcoi2",finEntityComboList);
            }
            if (finEntityComboList == null || finEntityComboList.isEmpty()) {
                CoiFinancialEntityBean empty = new CoiFinancialEntityBean();
                finEntityComboList = new Vector();
                finEntityComboList.add(empty);
            }

            String relationshipDescription = " ";
            String orgRelationshipDesc = " ";

            Vector saveProjectDetailsList = new Vector();
            if(projectDetailsList != null && projectDetailsList.size() > 0) {
                for (Iterator it1 = projectDetailsList.iterator(); it1.hasNext();) {
                    CoiPersonProjectDetails coiPersonProjectDetails = (CoiPersonProjectDetails) it1.next();
                    request.setAttribute("ModuleCode", coiPersonProjectDetails.getModuleCode());
                    if(coiPersonProjectDetails.getModuleItemKey()!=null){
                    if ((moduleCode != null && (moduleCode.intValue() == ModuleConstants.PROPOSAL_DEV_MODULE_CODE||moduleCode.intValue() == ModuleConstants.PROTOCOL_MODULE_CODE||moduleCode.intValue() == ModuleConstants.IACUC_MODULE_CODE ||moduleCode.intValue() == ModuleConstants.AWARD_MODULE_CODE||
                            moduleCode.intValue() == ModuleConstants.ANNUAL_COI_DISCLOSURE || moduleCode.intValue() == ModuleConstants.TRAVEL_MODULE_CODE)) ||(coiPersonProjectDetails.getModuleItemKey().equals(moduleItemKey))) {
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
                           
                            CoiPersonProjectDetails coiPersonProjectDetailsSave = new CoiPersonProjectDetails();
                            BeanUtils.copyProperties(coiPersonProjectDetailsSave, coiPersonProjectDetails);
                            coiPersonProjectDetailsSave.setEntityNumber(finEntityNumber);
                            coiPersonProjectDetailsSave.setEntitySequenceNumber(finSquenceNumber);
                            coiPersonProjectDetailsSave.setRelationShipDescription(relationshipDescription);
                            coiPersonProjectDetailsSave.setOrgRelationDescription(orgRelationshipDesc);
                           
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
                                Vector propsalList = (Vector) request.getSession().getAttribute("proposalProjectList");
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

                                }
                            } else if (coiPersonProjectDetails.getModuleCode() == ModuleConstants.PROTOCOL_MODULE_CODE) {
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
                            } else if (coiPersonProjectDetails.getModuleCode() == ModuleConstants.IACUC_MODULE_CODE) {
                                //getting protocols from session
                                Vector iacucprotocolList = (Vector) request.getSession().getAttribute("getAllIACUCProtocolList");
                                 if (iacucprotocolList != null && !iacucprotocolList.isEmpty()) {
                                for (Iterator it2 = iacucprotocolList.iterator(); it2.hasNext();) {
                                    CoiProtocolInfoBean protocolBean = (CoiProtocolInfoBean) it2.next();
                                    if (protocolBean.getProtocolNumber().equals(coiPersonProjectDetails.getModuleItemKey())) {
                                        coiPersonProjectDetailsSave.setCoiProjectId(protocolBean.getProtocolNumber());
                                        coiPersonProjectDetailsSave.setModuleItemKey(protocolBean.getProtocolNumber());
                                    }
                                }}
                            }
                            else if (coiPersonProjectDetails.getModuleCode() == ModuleConstants.AWARD_MODULE_CODE) {
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
                             else if (coiPersonProjectDetails.getModuleCode() == ModuleConstants.TRAVEL_MODULE_CODE) {
                                 CoiPersonProjectDetails pjtDet = (CoiPersonProjectDetails)request.getSession().getAttribute("travelPjtDetails");
                                 coiPersonProjectDetailsSave.setCoiProjectId(pjtDet.getCoiProjectId());
                                 coiPersonProjectDetailsSave.setModuleItemKey(pjtDet.getModuleItemKey());
                                 coiPersonProjectDetailsSave.setCoiProjectStartDate(pjtDet.getCoiProjectStartDate());
                                 coiPersonProjectDetailsSave.setCoiProjectEndDate(pjtDet.getCoiProjectEndDate());
                                 coiPersonProjectDetailsSave.setEventName(pjtDet.getEventName());
                                 coiPersonProjectDetailsSave.setDestination(pjtDet.getDestination());
                                 coiPersonProjectDetailsSave.setPurpose(pjtDet.getPurpose());
                                 coiPersonProjectDetailsSave.setCoiProjectFundingAmount(pjtDet.getCoiProjectFundingAmount());
                                 coiPersonProjectDetailsSave.setCoiProjectSponser(pjtDet.getCoiProjectSponser());
                            }

                            coiPersonProjectDetailsSave.setCoiDisclosureNumber(discl.getCoiDisclosureNumber());
                            coiPersonProjectDetailsSave.setSequenceNumber(discl.getSequenceNumber());
                            coiPersonProjectDetails.setCoiDisclosureNumber(discl.getCoiDisclosureNumber());
                            coiPersonProjectDetails.setSequenceNumber(discl.getSequenceNumber());
                            coiPersonProjectDetails.setDataSaved(true);
                            saveProjectDetailsList.add(coiPersonProjectDetailsSave);

                        }
                       // break;
                    }
                }

                }
        }
            WebTxnBean webTxnBean = new WebTxnBean();
            // Saving project details
            if ((moduleCode == null || moduleCode.intValue() != 0) && saveProjectDetailsList != null && !saveProjectDetailsList.isEmpty()) {
                String tempId = null;
                Collections.sort(saveProjectDetailsList, new ProposalProjectSortingComparator());
                boolean repeatSave = false;
                for (Iterator it = saveProjectDetailsList.iterator(); it.hasNext();) {
                    CoiPersonProjectDetails project = (CoiPersonProjectDetails) it.next();
                    if(!project.getCoiProjectId().equals(tempId)){
                         repeatSave = false;
                    }
                    if (tempId == null || !project.getCoiProjectId().equals(tempId)) {
                        tempId = project.getCoiProjectId();

//                        if (operationType != null && operationType.equals("MODIFY")) { ///Code for UPDATION START
//                              if (project.getAcType().equalsIgnoreCase("I")) {
//                              coiProjectService.removeProjectAndDetails(discl.getCoiDisclosureNumber(), discl.getSequenceNumber(), project.getModuleItemKey(), "updateRemoveAllNonProject", request);
//                              } else {
//                              coiProjectService.removeProjectAndDetails(discl.getCoiDisclosureNumber(), discl.getSequenceNumber(), project.getModuleItemKey(), "updateRemoveAllProject", request);
//                              }
//                        } ///Code for UPDATION ENDS
                    } else {
                        project.setAcType("N");
                        tempId = project.getCoiProjectId();
                    }
                    if(project.isNonIntegrated() || project.getModuleCode() == ModuleConstants.TRAVEL_MODULE_CODE){   //for non integrated
                       project.setAcType("I");
                    }else{
                       project.setAcType("N");
                    }
                    if(repeatSave){                  //for removing repeted saving on discl_project
                       project.setAcType("N");
                   }
                    if(project.getModuleCode() == ModuleConstants.TRAVEL_MODULE_CODE) {
                        project.setEventTypeCode(ModuleConstants.COI_EVENT_TRAVEL);
                    }
                    if(project.isNonIntegrated()){
                         project.setEventTypeCode(ModuleConstants.COI_EVENT_PROPOSAL);
                    }

                    //Calling function to save project details
                    if(isProjectSaved(request,project)!=1){
                    webTxnBean.getResults(request, "updateCoiProjectDetailsCoiv2", project);
                    }
                    repeatSave = true;
                }

            } 
//            else if (moduleCode != null && moduleCode == 0) {
//                boolean onceDeleted = false;
//                for (Iterator it = saveProjectDetailsList.iterator(); it.hasNext();) {
//                    CoiPersonProjectDetails project = (CoiPersonProjectDetails) it.next();
//                    if (operationType != null && operationType.equals("MODIFY") && onceDeleted == false) { ///Code for UPDATION STARTS
//                        onceDeleted = true;
//                        coiProjectService.removeProjectAndDetails(discl.getCoiDisclosureNumber(), discl.getSequenceNumber(), null, "updateRemoveAllNonProject", request);
//                    } ///Code for UPDATION ENDS
//                    project.setAcType("N");
//                    //Calling function to save project details
//                    webTxnBean.getResults(request, "updateCoiProjectDetailsCoiv2", project);
//                }
//            }
            boolean hasEntered = false;
            request.getSession().setAttribute("projectDetailsListInSeesion", projectDetailsList);
            //Getting next project from the List
            if(projectDetailsList != null) {
            for (Iterator it = projectDetailsList.iterator(); it.hasNext();) {
                CoiPersonProjectDetails coiPersonProjectDetails = (CoiPersonProjectDetails) it.next();
                if (coiPersonProjectDetails.isDataSaved() == false) {
                    coiPersonProjectDetailsForm.setModuleItemKey(coiPersonProjectDetails.getModuleItemKey());
                    coiPersonProjectDetailsForm.setCoiProjectTitle(coiPersonProjectDetails.getCoiProjectTitle());
                    String moduleItemKeyAdded = coiPersonProjectDetails.getModuleItemKey();
                    PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute("person");
                    HashMap hmData = new HashMap();
                    hmData.put("personId", person.getPersonID());
                    //Get financial Entity
                    Vector finEntityCombo = new Vector();
                    finEntityCombo = getFinEntity(request, hmData);//calling function to get financail entity
                    request.getSession().setAttribute("financialEntityList", finEntityCombo);//setting finacial entity
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
                        if (coiPersonProjectDetails.getModuleCode() == ModuleConstants.IACUC_MODULE_CODE) {
                            transactoinId = "getIacucProtoPjtDetForDiscl";
                        }
                        Vector deatilsofProject = coiCommonService.getProjectDetails(disclNumber, seqNumber, coiPersonProjectDetails.getModuleItemKey(), request, transactoinId);
                        request.setAttribute("projectDetails", deatilsofProject);
                    }
                    ///Code for UPDATION ENDS

                    break;
                }
            }
            }
               if (operationType != null && operationType.equals("MODIFY") && permissionType == 2) {
                    removeUncheckedProjects(request);

                }
                 if (operationType !=null && operationType.equals("MODIFY") && permissionType == 0) {
                    removeUncheckedProjects(request);

                }

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
 private int isProjectSaved(HttpServletRequest request,CoiPersonProjectDetails project) throws IOException, CoeusException, DBException, Exception{
      int projectCount= -1;
    int  flag = 0;
               String projectId=project.getModuleItemKey();
                if(projectId!=null&&projectId.equalsIgnoreCase("null")){
                   projectId=null;
                 }
                if(request.getSession().getAttribute("certValidPjtCount")!=null){
                    projectCount=Integer.parseInt(request.getSession().getAttribute("certValidPjtCount").toString());
                }
                WebTxnBean  webTxn = new WebTxnBean();
                HashMap hmData = new HashMap();
                hmData.put("coiDisclosureNumber", project.getCoiDisclosureNumber());
                hmData.put("sequenceNumber", project.getSequenceNumber());
                hmData.put("projectId", projectId);
                hmData.put("projectCount", projectCount);
                Hashtable projectDetailsList = (Hashtable) webTxn.getResults(request, "fnValidateCoiCertify", hmData);
                HashMap hmfinEntityList = (HashMap) projectDetailsList.get("fnValidateCoiCertify");
                if(hmfinEntityList !=null && hmfinEntityList.size()>0){
                int count = Integer.parseInt(hmfinEntityList.get("count").toString());
                if(count >0){
                 flag = 1;
                }                
                
                }
                return flag;
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