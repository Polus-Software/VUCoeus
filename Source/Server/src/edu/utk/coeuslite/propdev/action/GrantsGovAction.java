/*
 * GrantsGovAction.java
 *
 * Created on July 12, 2006, 10:45 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.utk.coeuslite.propdev.action;

import edu.mit.coeus.bean.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.s2s.*;
import edu.mit.coeus.s2s.bean.*;
import edu.mit.coeus.s2s.validator.*;
import edu.mit.coeus.user.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentIdGenerator;
import edu.mit.coeuslite.utils.*;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.wmc.coeuslite.utils.*;
import gov.grants.apply.soap.util.SoapUtils;
import java.io.*;
import java.sql.Timestamp;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.config.*;
import org.apache.struts.util.*;

/**
 *
 * @author sharathk
 */
public class GrantsGovAction extends ProposalBaseAction{
    
    private static final String DATE_FORMAT = "dd-MMM-yyyy hh:mm:ss a";
    public static final String SAVE = "Save";
    public static final String DELETE_OPPORTUNITY = "DeleteOpportunity";
    public static final String SELECT_ANOTHER_OPPORTUNITY = "SelectAnotherOpportunity";
    public static final String VALIDATE = "Validate";
    public static final String SUBMIT_GRANTS_GOV = "SubmitToGrantsGov";
    public static final String REFRESH = "Refresh";
    public static final String PRINT = "Print";
    
    private static final String EMPTY_STRING = "";
    
    //Revision Code Constants
    private char INC_AWARD = 'A';
    private char DEC_AWARD = 'B';
    private char INC_DURATION = 'C';
    private char DEC_DURATION = 'D';
    private char OTHER = 'E';
    
    private boolean configured = false;
    
    
    /**
     * Creates a new instance of GrantsGovAction
     */
    public GrantsGovAction(){
        
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward actionForward = null;
        
        UserMaintDataTxnBean usrTxn = new UserMaintDataTxnBean();
        
        if(!configured) {
            configureSSL();
            configureSoap();
            configured = true;
        }
        
        String action = request.getParameter("action");
        if(action != null && action.equals("searchPage")) {
            actionForward = actionMapping.findForward("opportunitySearchPage");
            request.setAttribute("searchResults", new Boolean(false));
            return actionForward;
        }else if(actionForm instanceof DynaActionForm || (action != null && action.equals("search"))) {
            //Opportunity Search
            String cfdaNumber, programNumber;
            cfdaNumber = request.getParameter("cfdaNumber");
            programNumber = request.getParameter("programNumber");
            actionForward = actionMapping.findForward("opportunitySearchPage");
            S2SHeader headerParam = new S2SHeader();
            headerParam.setCfdaNumber(cfdaNumber.length() == 0 ? null : cfdaNumber);
            headerParam.setOpportunityId(programNumber.length() == 0 ? null : programNumber);
            try{
            if((cfdaNumber == null || cfdaNumber.trim().length() == 0) &&
               (programNumber == null || programNumber.trim().length() == 0)) {
                MessageResources messages = MessageResources.getMessageResources("ProposalMessages");
                throw new CoeusException(messages.getMessage("opportunity.enter.cfdaOrprogramId"));
            }
            ArrayList oppList = GetOpportunity.getInstance().searchOpportunityList(headerParam);
            request.setAttribute("opportunity", oppList);
            request.setAttribute("searchResults", new Boolean(true));
            //JIRA COEUSDEV 61 - START
            //Check if User has Create Proposal Right.
            String user = null;
            UserInfoBean userInfoBean = (UserInfoBean) request.getSession().getAttribute("user" + request.getSession().getId());
            user = userInfoBean.getUserId();
            boolean isRightExists = usrTxn.getUserHasRightInAnyUnit(user, "CREATE_PROPOSAL");
            if(isRightExists){
                request.getSession().setAttribute("opportunity", oppList);
            }
            request.setAttribute("CREATE_PROPOSAL_RIGHT", new Boolean(isRightExists));
            if(cfdaNumber == null || cfdaNumber.trim().length() == 0){
                //User Searched by Opportunity Id. get the CFDA Number from oppList
                if(oppList != null){
                    OpportunityInfoBean oppInfoBean;
                    for(int index=0; index < oppList.size(); index++){
                         oppInfoBean = (OpportunityInfoBean)oppList.get(index);
                         if(programNumber!= null && programNumber.equals(oppInfoBean.getOpportunityId())) {
                            cfdaNumber = oppInfoBean.getCfdaNumber();
                            break;
                         }
                    }//End For
                }
            }
            request.getSession().setAttribute("cfdaNumber", cfdaNumber);
            //JIRA COEUSDEV 61 - END
            }catch (CoeusException coeusException) {
                String message = coeusException.getMessage();
                if(message.startsWith("java.lang.IllegalArgumentException")) {
                    //This is the case if both cfda and program Id is empty.
                    message = message.substring(35);
                }
                request.setAttribute("Exception", message);
            }
            return actionForward;
        } 
        
        DynaBeanList dynaBeanList = (DynaBeanList)actionForm;
        CoeusWebList list = dynaBeanList.getBeanList();
        String proposalNumber = null;
        boolean selectOpportunity = false;
        
        String loggedinUser = null;
        UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute("user"+request.getSession().getId());
        loggedinUser = userInfoBean.getUserId();
        
        if(list != null && list.size() > 0) {                   
            DynaActionForm opportunityForm = (DynaActionForm)list.get(0);
            String submitType = EMPTY_STRING;
            if(opportunityForm != null) {
                submitType = opportunityForm.get("submitType").toString();
            }
            
            CoeusWebList oppForm = dynaBeanList.getBeanList();
            DynaActionForm dynaActionForm = null;
            DBOpportunityInfoBean dBOpportunityInfoBean = null;
            if(oppForm != null) {
                dynaActionForm = (DynaActionForm)oppForm.get(0);
                if(dynaActionForm != null) {
                    dBOpportunityInfoBean = getOpportunity(dynaActionForm);
                    proposalNumber = dBOpportunityInfoBean.getProposalNumber();
                }
            }
            S2SSubmissionDataTxnBean txnBean = new S2SSubmissionDataTxnBean();
            
            if(submitType.equals(SAVE)) {
                //Create Beans
                Vector vector = new Vector();
                //List oppForm = dynaBeanList.getBeanList();
                CoeusWebList forms = dynaBeanList.getList();
                Vector vecForms = new Vector();
                //DynaActionForm dynaActionForm;
                FormInfoBean formInfoBean;
                for(int index = 0; index < forms.size(); index++) {
                    dynaActionForm = (DynaActionForm)forms.get(index);
                    formInfoBean = getFormInfoBean(dynaActionForm, loggedinUser, proposalNumber);
                    vecForms.add(formInfoBean);
                }
                vector.add(vecForms);
                
                vector.add(dBOpportunityInfoBean);
                
                //Save the form;
                txnBean.addUpdDelOppForms(vector);
                
            }//End if Save
            else if(submitType.equals(DELETE_OPPORTUNITY)) {
                dBOpportunityInfoBean.setAcType('D');
                txnBean.updateDelOpportunity(dBOpportunityInfoBean);
                //txnBean.addOpportunity(dBOpportunityInfoBean);
                actionForward = new ActionForward("/getGeneralInfo.do?proposalNumber="+dBOpportunityInfoBean.getProposalNumber());
                return actionForward;
            }//End if DeleteOpportunity
            else if(submitType.equals(SELECT_ANOTHER_OPPORTUNITY)) {
                selectOpportunity = true;
                //place opportunity timestamp in session for updation
                request.getSession().setAttribute("SelectOpportunityTS",dBOpportunityInfoBean.getAwUpdateTimestamp());
            }//End if SelectAnotheropportunity
            else if(submitType.startsWith(VALIDATE)) {
                Vector vector = new Vector();
                
                CoeusWebList forms = dynaBeanList.getList();
                FormInfoBean formInfoBean;
                Vector vecForms = new Vector();
                for(int index = 0; index < forms.size(); index++) {
                    dynaActionForm = (DynaActionForm)forms.get(index);
                    formInfoBean = getFormInfoBean(dynaActionForm, loggedinUser, proposalNumber);
                    vecForms.add(formInfoBean);
                }
                vector.add(vecForms);
                vector.add(dBOpportunityInfoBean);
                txnBean.addUpdDelOppForms(vector);
                SubmissionEngine subEngine = SubmissionEngine.getInstance();
                
                subEngine.setLoggedInUser(loggedinUser);
                
                S2SHeader headerParam = new S2SHeader();
                
                //headerParam.setAgencyTitle();
                String cfdaNum = dBOpportunityInfoBean.getCfdaNumber();
                headerParam.setCfdaNumber((cfdaNum != null && cfdaNum.length() > 0) ? cfdaNum : null );
                headerParam.setCompetitionId(dBOpportunityInfoBean.getCompetitionId());
                //coeus-675 start
//                headerParam.setOpportunityId(dBOpportunityInfoBean.getOpportunityId());
                headerParam.setOpportunityId(dBOpportunityInfoBean.getOpportunityId().toUpperCase().trim().replaceAll(" ", ""));
                //coeus-675 end
                HashMap hashMap = new HashMap();
                hashMap.put("PROPOSAL_NUMBER", dBOpportunityInfoBean.getProposalNumber());
                headerParam.setStreamParams(hashMap);
                headerParam.setSubmissionTitle(dBOpportunityInfoBean.getProposalNumber());
                
                try{
                    subEngine.validateData(headerParam);
                    
                    if(submitType.indexOf(SUBMIT_GRANTS_GOV) != -1){
                        subEngine.submitApplication(headerParam);
                    }else {
                        //Display validation success message
                        //case 3311 start
//                        request.setAttribute("Message", "All Validation Passesd Successfully");
                        request.setAttribute("Message", "All Validations Passed Successfully");                     
                        //case 3311 end                         
                    }
                }catch (Exception exception) {
                    UtilFactory.log( exception.getMessage(), exception, "GrantsGovAction", "performExecute");
                    request.setAttribute("Exception", exception);
                }
                
            }//End if Validate
            else if(submitType.equals(REFRESH)) {
                
                S2SHeader headerParam = new S2SHeader();
                
                //headerParam.setAgencyTitle();
                headerParam.setCfdaNumber(dBOpportunityInfoBean.getCfdaNumber());
                headerParam.setCompetitionId(dBOpportunityInfoBean.getCompetitionId());
                headerParam.setOpportunityId(dBOpportunityInfoBean.getOpportunityId());
                HashMap hashMap = new HashMap();
                hashMap.put("PROPOSAL_NUMBER", dBOpportunityInfoBean.getProposalNumber());
                headerParam.setStreamParams(hashMap);
                headerParam.setSubmissionTitle(dBOpportunityInfoBean.getProposalNumber());
                
                SubmissionDetailInfoBean localSubInfo = (SubmissionDetailInfoBean)txnBean.getSubmissionDetails(headerParam);
                GetSubmission appReq = GetSubmission.getInstance();
                SubmissionInfoBean[] appList = appReq.getSubmissionList(headerParam);
                localSubInfo.setAcType('U');
                
                localSubInfo.setUpdateUser(loggedinUser);
                
                CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
                if(appList==null || appList.length==0){//Need to check whether there is any error during the submission
                    //by calling getApplicationStatusDetail web service
                    Object statusDetail = GetApplicationInfo.getInstance().getStatusDetails(localSubInfo.getGrantsGovTrackingNumber());
                    if(statusDetail==null) {
                        throw new Exception(coeusMessageResourcesBean.parseMessageKey("exceptionCode.90001"));
                    }
                    
                    localSubInfo.setComments(statusDetail.toString());
                    localSubInfo.setStatus(coeusMessageResourcesBean.parseMessageKey("exceptionCode.90009"));
                }else for(SubmissionInfoBean latestInfo:appList){
                    //Case# COEUSDEV-1101 
                    //BEGIN
                    //ApplicationInfoBean latestInfo = application;
                    if(latestInfo.getGrantsGovTrackingNumber()!=null && 
                            latestInfo.getGrantsGovTrackingNumber().equals(localSubInfo.getGrantsGovTrackingNumber())){
                        localSubInfo.setStatus(latestInfo.getStatus());
                        localSubInfo.setStatusDate(latestInfo.getStatusDate());
                        localSubInfo.setAgencyTrackingNumber(latestInfo.getAgencyTrackingNumber());
                        if(latestInfo.getAgencyTrackingNumber()!=null && latestInfo.getAgencyTrackingNumber().length()>0){
                            localSubInfo.setComments(coeusMessageResourcesBean.parseMessageKey("exceptionCode.90003"));
                        }
                        break;
                    }
                }
                txnBean.addUpdDeleteSubmissionDetails(localSubInfo);
                Object[] s2sDetails = null;
                Object[] tmpArray = null;
                HashMap rightFlags = null;
                s2sDetails = new Object[4];
                tmpArray = txnBean.getS2SDetails(headerParam);
                for(int i=0;i<3;i++) s2sDetails[i] = tmpArray[i];
                rightFlags = new HashMap();
                //Added for case 3587; Multicampus enhancements
                ProposalDevelopmentTxnBean propTxnBean = new ProposalDevelopmentTxnBean();//3587
                String unitNumber = propTxnBean.getProposalLeadUnit(dBOpportunityInfoBean.getProposalNumber());
//                rightFlags.put(S2SConstants.SUBMIT_TO_SPONSOR, new Boolean(usrTxn.getUserHasOSPRight(loggedinUser, S2SConstants.SUBMIT_TO_SPONSOR)));
                rightFlags.put(S2SConstants.SUBMIT_TO_SPONSOR, new Boolean(usrTxn.getUserHasRight(loggedinUser, S2SConstants.SUBMIT_TO_SPONSOR,unitNumber)));
                //3587 End
                rightFlags.put(S2SConstants.IS_READY_TO_SUBMIT,new Boolean(txnBean.isProposalReadyForS2S(headerParam.getSubmissionTitle())));
                rightFlags.put(S2SConstants.IS_ATTR_MATCH,new Boolean(txnBean.isS2SAttrMatch(headerParam.getSubmissionTitle())));
                s2sDetails[3] = rightFlags;
                //responder.setDataObject(txnBean.getS2SDetails(headerParam));
                
            }//End if Refresh
            else if(submitType.equals(PRINT)){
                //dynaBeanList = (DynaBeanList)request.getSession().getAttribute("grantsGov");
                //String selectedForms = request.getParameter("selForms");
                //String formIndexes[] = selectedForms.split(",");
                
                CoeusWebList forms = dynaBeanList.getList();
                FormInfoBean formInfoBean;
                Vector vecForms = new Vector();
                Boolean booleanObj;
                //DynaActionForm dynaActionForm;
                //int formIndex = -1;
                for(int index = 0; index < forms.size(); index++) {
                    dynaActionForm = (DynaActionForm)forms.get(index);
                    formInfoBean = getFormInfoBean(dynaActionForm, loggedinUser, proposalNumber);
                    booleanObj = (Boolean)dynaActionForm.get("print");
                    if(booleanObj != null && booleanObj.booleanValue()) {
                        vecForms.add(formInfoBean);
                    }
                    
                    /*for(int subIndex = 0; subIndex < formIndexes.length; subIndex++) {
                        formIndex = Integer.parseInt(formIndexes[subIndex]);
                        if(index == formIndex){
                            vecForms.add(formInfoBean);
                        }
                    }*/
                }
                
                dynaActionForm = (DynaActionForm)dynaBeanList.getBeanList().get(0);
                /*DBOpportunityInfoBean*/ dBOpportunityInfoBean = getOpportunity(dynaActionForm);
                
                S2SHeader headerParam = new S2SHeader();
                //headerParam.setAgencyTitle();
                headerParam.setCfdaNumber(dBOpportunityInfoBean.getCfdaNumber());
                headerParam.setCompetitionId(dBOpportunityInfoBean.getCompetitionId());
                headerParam.setOpportunityId(dBOpportunityInfoBean.getOpportunityId());
                HashMap hashMap = new HashMap();
                hashMap.put("PROPOSAL_NUMBER", dBOpportunityInfoBean.getProposalNumber());
                headerParam.setStreamParams(hashMap);
                headerParam.setSubmissionTitle(dBOpportunityInfoBean.getProposalNumber());
                
//                request.setAttribute("Forms", vecForms);
//                request.setAttribute("S2SHeader", headerParam);
//                request.setAttribute(DocumentConstants.READER_CLASS, "edu.mit.coeus.s2s.S2SDocumentReader");
//                
//                actionForward = new ActionForward("/StreamingServlet");
//                return actionForward;
                
                DocumentBean documentBean = new DocumentBean();
                Map map = new HashMap();
                map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.s2s.S2SDocumentReader");
                map.put("Forms", vecForms);
                map.put("S2SHeader", headerParam);
                documentBean.setParameterMap(map);
//                String docId = request.getSession().getId();
                String docId = DocumentIdGenerator.generateDocumentId();
                request.getSession().setAttribute(docId, documentBean);
                
                actionForward = new ActionForward("/StreamingServlet?"+DocumentConstants.DOC_ID+"="+docId,true);
                return actionForward;
            }//End if Print
        }
        
        //String action = request.getParameter("action");
                

        if(proposalNumber == null || proposalNumber.trim().length() == 0) {
            proposalNumber = (String)request.getSession().getAttribute("proposalNumber");
        }
        
        if(request.getParameter("proposalNumber") != null) {
            proposalNumber = request.getParameter("proposalNumber");
            request.getSession().setAttribute("proposalNumber", proposalNumber);
        }        
        //Commented because Vanderbilt is not using Coeus PHSHumanSubjectCTFormIncluded web Form
        //HttpSession session = request.getSession();
        // session.setAttribute("isPHSHumanSubjectCTFormIncluded"+session.getId(), isPHSHumanSubjectCTFormIncluded(proposalNumber, request));

        request.getSession().removeAttribute("grantsGov");
        
        S2SSubmissionDataTxnBean s2SSubmissionDataTxnBean = new S2SSubmissionDataTxnBean();
        boolean s2sCandidate = s2SSubmissionDataTxnBean.isS2SCandidate(proposalNumber);
        
        /*Object ggExist = request.getSession().getAttribute("grantsGovExist");
        if(ggExist != null && ggExist.toString().trim().equals("1")) {
            s2sCandidate = true;
        }*/
        
        if(s2sCandidate && !selectOpportunity) {
            
            S2SSubmissionDataTxnBean txnBean = new S2SSubmissionDataTxnBean();
            S2SHeader headerParam = new S2SHeader();
            
            WebTxnBean webTxnBean = new WebTxnBean();
            Map map = new HashMap();
            map.put("proposalNumber", proposalNumber);
            Hashtable result = (Hashtable)webTxnBean.getResults(request, "getProposalSummaryDetails", map);
            List lstPropSummary = (List)result.get("getProposalSummaryDetails");
            DynaActionForm propSummaryForm  = (DynaActionForm)lstPropSummary.get(0);
            String cdfa = (String)propSummaryForm.get("cfdaCode");
            if(cdfa != null && cdfa.length() == 5){
                //Put a period after 2nd character
                cdfa = cdfa.substring(0, 2) + "." + cdfa.substring(2);
            }
            headerParam.setCfdaNumber((cdfa != null && cdfa.length() == 0) ? null : cdfa);
                    
            headerParam.setSubmissionTitle(proposalNumber);
            //coeus-675 start
//            String oppId = (String)propSummaryForm.get("programAnnouncementNumber");
            String oppId = ((String)propSummaryForm.get("programAnnouncementNumber") == null?"":
                            ((String)propSummaryForm.get("programAnnouncementNumber")).toUpperCase().trim());
            oppId = oppId.replaceAll(" ", "");
            //coeus-675 end
            headerParam.setOpportunityId(oppId.length() == 0 ? null : oppId);
                        
            Object s2sDetails[] = new Object[6];
            Object tmpArray[] = txnBean.getS2SDetails(headerParam);
            
            for(int i=0;i<3;i++) s2sDetails[i] = tmpArray[i];
            
            OpportunityInfoBean oppInfo = null;
            
            if(s2sDetails[0]==null || selectOpportunity){
                try{
                    request.getSession().removeAttribute("opportunity");
                    ArrayList oppList = GetOpportunity.getInstance().searchOpportunityList(headerParam);                   
                    request.getSession().setAttribute("opportunity", oppList);
                    //request.setAttribute("opportunity", oppList);
                }catch (CoeusException coeusException) {
                    UtilFactory.log( coeusException.getMessage(), coeusException, "GrantsGovAction", "performExecute");
                    request.setAttribute("Exception", coeusException);
                }
                actionForward = actionMapping.findForward("opportunity");
            }else if(s2sDetails[1]==null){
                oppInfo = (OpportunityInfoBean)s2sDetails[0];
                try{
                    s2sDetails[1] = new OpportunitySchemaParser().getFormsList(proposalNumber,oppInfo.getSchemaUrl());
                }catch(FileNotFoundException fnfEx){
                    //ArrayList oppList = new GetOpportunity().searchOpportunity(headerParam);
                    String message = "The Selected Opportunity "+oppInfo.getSchemaUrl() +
                            " is not found at the specified location."+
                            "Please select another opportunity from the list";
                    Exception exception = new Exception(message);
                    
                    //exception.setOpportunityList(oppList);
                    throw exception;
                }
            }
            oppInfo = (OpportunityInfoBean)s2sDetails[0];
            
            HashMap rightFlags = new HashMap();
            //Modified with case 3587: Multicampus enhancement
            ProposalDevelopmentTxnBean propTxnBean = new ProposalDevelopmentTxnBean();
            String unitNumber = propTxnBean.getProposalLeadUnit(proposalNumber);
//            rightFlags.put(S2SConstants.SUBMIT_TO_SPONSOR, new Boolean(usrTxn.getUserHasOSPRight(loggedinUser, S2SConstants.SUBMIT_TO_SPONSOR)));
            rightFlags.put(S2SConstants.SUBMIT_TO_SPONSOR, new Boolean(usrTxn.getUserHasRight(loggedinUser, S2SConstants.SUBMIT_TO_SPONSOR,unitNumber)));
            //3587 End
            rightFlags.put(S2SConstants.IS_READY_TO_SUBMIT, new Boolean(txnBean.isProposalReadyForS2S(headerParam.getSubmissionTitle())));
            rightFlags.put(S2SConstants.IS_ATTR_MATCH,new Boolean(txnBean.isS2SAttrMatch(headerParam.getSubmissionTitle())));
            s2sDetails[3] = rightFlags;
            
            //modification for new columns in OSP$S2S_OPPORTUNITY, S2S_SUBMISSION_TYPE_CODE, REVISION_CODE, REVISION_OTHER_DESCRIPTION - START
            List submissionTypes = txnBean.getSubmissionTypes();
            s2sDetails[4] = submissionTypes;
            CoeusFunctions coeusFunctions = new CoeusFunctions();
            String defaultSelect = coeusFunctions.getParameterValue("DEFAULT_S2S_SUBMISSION_TYPE");
            s2sDetails[5] = defaultSelect;
            //modification for new columns in OSP$S2S_OPPORTUNITY, S2S_SUBMISSION_TYPE_CODE, REVISION_CODE, REVISION_OTHER_DESCRIPTION - END
            
            DynaActionForm opportunityForm = getOpportunity(request, oppInfo);
            list = new CoeusWebList();
            list.add(opportunityForm);
            dynaBeanList.setBeanList(list);
            
            CoeusWebList formList = new CoeusWebList();
            if(s2sDetails[1] instanceof CoeusWebList) {
                formList = (CoeusWebList)s2sDetails[1];
            }else {
                List lst = (List)s2sDetails[1];
                if(lst != null) {
                    formList.addAll(lst);
                }
            }
            DynaActionForm dynaFormInfobean;
            FormInfoBean formInfoBean;
            if(formList != null) {
                for(int index = 0; index < formList.size(); index++) {
                    formInfoBean = (FormInfoBean)formList.get(index);
                    dynaFormInfobean = getFormInfoBean(request, formInfoBean);
                    formList.set(index, dynaFormInfobean);
                }
            }
            dynaBeanList.setList(formList);
            
            request.setAttribute("grantsGovDetails", s2sDetails);
            
            request.setAttribute("rightFlags", rightFlags);
            
            request.setAttribute("submissionDetails", s2sDetails[2]);
            
            request.setAttribute("submissionTypes", s2sDetails[4]);
            
            //request.getSession().setAttribute("grantsGov", dynaBeanList);
            request.setAttribute("grantsGov", dynaBeanList);
            
            if(actionForward == null) {
                actionForward = actionMapping.findForward("grantsGov");
            }//End if actionForward == null
            
            Map mapMenuList = new HashMap();
            mapMenuList.put("menuItems",CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
            mapMenuList.put("menuCode",CoeusliteMenuItems.GRANTS_GOV_MENU_CODE); 
            setSelectedMenuList(request, mapMenuList);
            
        }else if(selectOpportunity){
            
            List oppForm = dynaBeanList.getBeanList();
            DynaActionForm dynaActionForm = (DynaActionForm)oppForm.get(0);
            DBOpportunityInfoBean dBOpportunityInfoBean = getOpportunity(dynaActionForm);
            //S2SSubmissionDataTxnBean txnBean = new S2SSubmissionDataTxnBean();
            proposalNumber = dBOpportunityInfoBean.getProposalNumber();
            
            S2SHeader headerParam = new S2SHeader();
            
            //headerParam.setAgencyTitle();
            headerParam.setCfdaNumber(dBOpportunityInfoBean.getCfdaNumber());
            headerParam.setCompetitionId(dBOpportunityInfoBean.getCompetitionId());
            headerParam.setOpportunityId(dBOpportunityInfoBean.getOpportunityId());
            HashMap hashMap = new HashMap();
            hashMap.put("PROPOSAL_NUMBER", dBOpportunityInfoBean.getProposalNumber());
            headerParam.setStreamParams(hashMap);
            headerParam.setSubmissionTitle(dBOpportunityInfoBean.getProposalNumber());
            
            try{
                ArrayList oppList = GetOpportunity.getInstance().searchOpportunityList(headerParam);                
                request.getSession().setAttribute("opportunity", oppList);
                //request.setAttribute("opportunity", oppList);
            }catch (CoeusException coeusException) {
                UtilFactory.log( coeusException.getMessage(), coeusException, "GrantsGovAction", "performExecute");
                request.setAttribute("Exception", coeusException);
            }
            actionForward = actionMapping.findForward("opportunity");
        }else {
            CoeusException coeusException = new CoeusException("Not a S2S Candidate");
            request.setAttribute("Exception", coeusException);
            actionForward = actionMapping.findForward("opportunity");
            //Remove Opportunity in session
            request.getSession().removeAttribute("opportunity");
            
            Map mapMenuList = new HashMap();
            mapMenuList.put("menuItems",CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
            mapMenuList.put("menuCode",CoeusliteMenuItems.GRANTS_GOV_MENU_CODE); 
            setSelectedMenuList(request, mapMenuList);
        }
        
        return actionForward;
    }
    
    protected DynaActionForm getOpportunity(HttpServletRequest request, OpportunityInfoBean opportunityInfoBean) throws Exception{
        if(opportunityInfoBean == null) {
            return null;
        }
        ServletContext servletContext = request.getSession().getServletContext();
        ModuleConfig moduleConfig = RequestUtils.getModuleConfig(request, servletContext);
        FormBeanConfig formConfig = moduleConfig.findFormBeanConfig("grantsGovOpportunity");
        DynaActionFormClass dynaClass = DynaActionFormClass.createDynaActionFormClass(formConfig);
        DynaActionForm dynaActionForm = (DynaActionForm)dynaClass.newInstance();
        
        dynaActionForm.set("proposalNumber", ((DBOpportunityInfoBean)opportunityInfoBean).getProposalNumber());
        dynaActionForm.set("opportunityId", opportunityInfoBean.getOpportunityId());
        dynaActionForm.set("opportunityTitle", opportunityInfoBean.getOpportunityTitle());
        dynaActionForm.set("cfdaNumber", opportunityInfoBean.getCfdaNumber());
        dynaActionForm.set("competitionId", opportunityInfoBean.getCompetitionId());
        dynaActionForm.set("schemaUrl", opportunityInfoBean.getSchemaUrl());
        dynaActionForm.set("instructionUrl", opportunityInfoBean.getInstructionUrl());
        
        String formattedDate = formatDate(opportunityInfoBean.getOpeningDate(), DATE_FORMAT);
        dynaActionForm.set("openingDate", formattedDate);
        formattedDate = formatDate(opportunityInfoBean.getClosingDate(), DATE_FORMAT);
        dynaActionForm.set("closingDate", formattedDate);
        
        dynaActionForm.set("opportunity", opportunityInfoBean.getOpportunity());
        String revisionCode = opportunityInfoBean.getRevisionCode();
        dynaActionForm.set("revisionCode", opportunityInfoBean.getRevisionCode());
        String revisionCodeAward = EMPTY_STRING, revisionCodeDuration = EMPTY_STRING, revisionCodeOther = EMPTY_STRING;
        if(revisionCode != null && (revisionCode.indexOf(INC_AWARD) != -1 || revisionCode.indexOf(DEC_AWARD) != -1 )) {
            //Award has been Selected.
            revisionCodeAward = EMPTY_STRING + revisionCode.charAt(0);
        }
        dynaActionForm.set("revisionCodeAward", revisionCodeAward);
        
        if(revisionCode != null && (revisionCode.indexOf(INC_DURATION) != -1 || revisionCode.indexOf(DEC_DURATION) != -1 )) {
            //Duration has been Selected.
            int charIndex = 0;
            if(revisionCodeAward.length() > 0) {
                charIndex = 1;
            }
            revisionCodeDuration = EMPTY_STRING + revisionCode.charAt(charIndex);
        }
        dynaActionForm.set("revisionCodeDuration", revisionCodeDuration);
        
        if(revisionCode != null && revisionCode.indexOf(OTHER) != -1) {
            revisionCodeOther = EMPTY_STRING + OTHER;
        }
        dynaActionForm.set("revisionCodeOther", revisionCodeOther);
        
        dynaActionForm.set("revisionOtherDescription", opportunityInfoBean.getRevisionOtherDescription());
        
        dynaActionForm.set("submissionTypeCode", ""+opportunityInfoBean.getSubmissionTypeCode());
        
        formattedDate = formatDate(((DBOpportunityInfoBean)opportunityInfoBean).getUpdateTimestamp(), DATE_FORMAT);
        dynaActionForm.set("updateTimestamp", formattedDate);
        
        dynaActionForm.set("submitType", "");
        return dynaActionForm;
    }
    
    private DBOpportunityInfoBean getOpportunity(DynaActionForm dynaActionForm)throws ParseException{
        DBOpportunityInfoBean dBOpportunityInfoBean = new DBOpportunityInfoBean();
        dBOpportunityInfoBean.setProposalNumber((String)dynaActionForm.get("proposalNumber"));
        dBOpportunityInfoBean.setAwProposalNumber((String)dynaActionForm.get("proposalNumber"));
        dBOpportunityInfoBean.setOpportunityId((String)dynaActionForm.get("opportunityId"));
        dBOpportunityInfoBean.setOpportunityTitle((String)dynaActionForm.get("opportunityTitle"));
        dBOpportunityInfoBean.setCfdaNumber((String)dynaActionForm.get("cfdaNumber"));
        dBOpportunityInfoBean.setCompetitionId((String)dynaActionForm.get("competitionId"));
        dBOpportunityInfoBean.setSchemaUrl((String)dynaActionForm.get("schemaUrl"));
        dBOpportunityInfoBean.setInstructionUrl((String)dynaActionForm.get("instructionUrl"));
        Date date = getDate((String)dynaActionForm.get("openingDate"), DATE_FORMAT);
        Timestamp timestamp = null;
        if(date != null){
            timestamp = new Timestamp(date.getTime());
        }
        dBOpportunityInfoBean.setOpeningDate(timestamp);
        
        date = getDate((String)dynaActionForm.get("closingDate"), DATE_FORMAT);
        timestamp = null;
        if(date != null){
            timestamp = new Timestamp(date.getTime());
        }
        dBOpportunityInfoBean.setClosingDate(timestamp);
        
        dBOpportunityInfoBean.setOpportunity((String)dynaActionForm.get("opportunity"));
        String revisionCode = EMPTY_STRING;
        String revisionCodeAward = EMPTY_STRING, revisionCodeDuration = EMPTY_STRING, revisionCodeOther = EMPTY_STRING;
        revisionCodeAward = (String)dynaActionForm.get("revisionCodeAward");
        revisionCodeDuration = (String)dynaActionForm.get("revisionCodeDuration");
        revisionCodeOther = (String)dynaActionForm.get("revisionCodeOther");
        if(revisionCodeOther != null && revisionCodeOther.equals(EMPTY_STRING+OTHER)) {
            revisionCode = EMPTY_STRING+OTHER;
        }else {
            revisionCode = revisionCodeAward + revisionCodeDuration;
        }
        
        dBOpportunityInfoBean.setRevisionCode(revisionCode);
        if(revisionCode.equals(EMPTY_STRING+OTHER)) {
            dBOpportunityInfoBean.setRevisionOtherDescription((String)dynaActionForm.get("revisionOtherDescription"));
        }else {
            dBOpportunityInfoBean.setRevisionOtherDescription(EMPTY_STRING);
        }
        
        date = getDate((String)dynaActionForm.get("updateTimestamp"), DATE_FORMAT);
        timestamp = null;
        if(date != null){
            timestamp = new Timestamp(date.getTime());
        }
        String strTypeCode = (String)dynaActionForm.get("submissionTypeCode");
        int typeCode = Integer.parseInt(strTypeCode);
        dBOpportunityInfoBean.setSubmissionTypeCode(typeCode);
        
        dBOpportunityInfoBean.setAwUpdateTimestamp(timestamp);
        return dBOpportunityInfoBean;
    }
    
    protected DynaActionForm getFormInfoBean(HttpServletRequest request, FormInfoBean formInfoBean) throws Exception{
        ServletContext servletContext = request.getSession().getServletContext();
        ModuleConfig moduleConfig = RequestUtils.getModuleConfig(request, servletContext);
        FormBeanConfig formConfig = moduleConfig.findFormBeanConfig("formInfoBean");
        DynaActionFormClass dynaClass = DynaActionFormClass.createDynaActionFormClass(formConfig);
        DynaActionForm dynaActionForm = (DynaActionForm)dynaClass.newInstance();
        
        if(formInfoBean.getProposalNumber() == null) {
            dynaActionForm.set("proposalNumber", request.getParameter("proposalNumber"));
            dynaActionForm.set("acType", "I");
        }else {
            dynaActionForm.set("proposalNumber", formInfoBean.getProposalNumber());
            dynaActionForm.set("acType", ""+formInfoBean.getAcType());
        }
        
        dynaActionForm.set("ns", formInfoBean.getNs());
        dynaActionForm.set("schUrl", formInfoBean.getSchUrl());
        dynaActionForm.set("formName", formInfoBean.getFormName());
        dynaActionForm.set("mandatory", new Boolean(formInfoBean.isMandatory()));
        dynaActionForm.set("available", new Boolean(formInfoBean.isAvailable()));
        dynaActionForm.set("include", new Boolean(formInfoBean.isInclude()));
        
        String formattedDate = formatDate(formInfoBean.getUpdateTimestamp(), DATE_FORMAT);
        dynaActionForm.set("updateTimestamp", formattedDate);
        
        dynaActionForm.set("updateUser", formInfoBean.getUpdateUser());
        
        
        return dynaActionForm;
    }
    
    private FormInfoBean getFormInfoBean(DynaActionForm dynaActionForm, String loggedinUser, String propNum)throws ParseException {
        FormInfoBean formInfoBean = new FormInfoBean();
        String proposalNumber = dynaActionForm.get("proposalNumber").toString();
        if(proposalNumber == null || proposalNumber.length() == 0){
            proposalNumber = propNum;
        }
        formInfoBean.setProposalNumber(proposalNumber);
        formInfoBean.setAwProposalNumber(proposalNumber);
        formInfoBean.setNs((String)dynaActionForm.get("ns"));
        formInfoBean.setSchUrl((String)dynaActionForm.get("schUrl"));
        formInfoBean.setFormName((String)dynaActionForm.get("formName"));
        Boolean booleanObj = (Boolean)dynaActionForm.get("mandatory");
        formInfoBean.setMandatory(booleanObj==null ? false : booleanObj.booleanValue());
        booleanObj = (Boolean)dynaActionForm.get("available");
        try {
        	if(booleanObj==null || !booleanObj){
        		booleanObj = new UserAttachedS2STxnBean().isFormAvailable(formInfoBean.getProposalNumber(),formInfoBean.getNs());
        	}
		} catch (Exception e) {}

        formInfoBean.setAvailable(booleanObj==null ? false : booleanObj.booleanValue());
        booleanObj = (Boolean)dynaActionForm.get("includeSelected");
        formInfoBean.setInclude(booleanObj==null ? false : booleanObj.booleanValue());
        
        Date date = getDate((String)dynaActionForm.get("updateTimestamp"), DATE_FORMAT);
        Timestamp timestamp = null;
        if(date != null){
            timestamp = new Timestamp(date.getTime());
        }
        formInfoBean.setAwUpdateTimestamp(timestamp);
        //formInfoBean.setUpdateTimestamp();
        
        formInfoBean.setUpdateUser(loggedinUser);
        
        String acType = (String)dynaActionForm.get("acType");
        if(acType != null && acType.trim().length() > 0) {
            char ac = acType.charAt(0);
            formInfoBean.setAcType(ac);
        }else {
            formInfoBean.setAcType('U');
        }
        
        System.out.println("PropNum:"+proposalNumber+" ; Ns:"+dynaActionForm.get("ns")+" ; acType"+acType);
        
        return formInfoBean;
    }
    
    private void configureSSL() throws Exception{
        try{
            String path= getServlet().getServletContext().getRealPath("/").replace( '\\', '/');
            String soapServerPropertyFile =path+"/"+"WEB-INF"+"/classes/"+S2SConstants.SOAP_SERVER_PROPERTY_FILE;
            SoapUtils.setSoapServerPropFile( soapServerPropertyFile );
            System.setProperty("javax.net.ssl.keyStore", SoapUtils
                    .getProperty("javax.net.ssl.keyStore"));
            System.setProperty("javax.net.ssl.keyStorePassword", SoapUtils
                    .getProperty("javax.net.ssl.keyStorePassword"));
            System.setProperty("javax.net.ssl.trustStore", SoapUtils
                    .getProperty("javax.net.ssl.trustStore"));
            System.setProperty("javax.net.ssl.trustStorePassword", SoapUtils
                    .getProperty("javax.net.ssl.trustStorePassword"));
        }catch(IOException ioEx){
            UtilFactory.log(ioEx.getMessage(),ioEx,"DisplayGrantsGovAction","ConfigureSSL");
            throw new Exception(ioEx.getMessage());
        }
    }
    
    private void configureSoap() {
        java.lang.System.setProperty("javax.xml.soap.MessageFactory",
                "org.apache.axis.soap.MessageFactoryImpl");
        
        java.lang.System.setProperty("javax.xml.soap.SOAPConnectionFactory",
                "org.apache.axis.soap.SOAPConnectionFactoryImpl");
        
    }
    
    private String formatDate(java.util.Date date, String dateFormat) {
        String formattedDate;
        if(date != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
            formattedDate = simpleDateFormat.format(date);
        }else {
            formattedDate = "";
        }
        return formattedDate;
    }
    
    private Date getDate(String strDate, String dateFormat) throws ParseException {
        Date date = null;
        if(strDate != null && strDate.trim().length() > 0) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
            date = simpleDateFormat.parse(strDate);
        }
        return date;
    }
}
