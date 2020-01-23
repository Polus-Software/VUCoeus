/*
 * ProtocolGetAction.java
 *
 * Created on May 10, 2005, 11:27 AM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 14-MARCH-2011
 * by Md.Ehtesham Ansari
 */

package edu.mit.coeuslite.iacuc.action;

import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.iacuc.bean.ProtocolUpdateTxnBean;
import edu.mit.coeus.irb.bean.CommitteeMaintenanceFormBean;
import edu.mit.coeus.iacuc.bean.CommitteeTxnBean;
import edu.mit.coeus.iacuc.bean.ProtocolAuthorizationBean;
import edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean;
import edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean;
import edu.mit.coeus.iacuc.bean.ProtocolSubmissionTxnBean;
import edu.mit.coeus.iacuc.bean.UploadDocumentBean;
import edu.mit.coeus.questionnaire.bean.ModuleDataBean;
import edu.mit.coeus.questionnaire.bean.SubModuleDataBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeuslite.iacuc.bean.ProtocolHeaderDetailsBean;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.mit.coeuslite.utils.DateUtils;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.MenuBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;
import edu.mit.coeuslite.utils.xmlReader.ReadJSPPlaceHolder;
import edu.wmc.coeuslite.budget.bean.ReadXMLData;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import org.apache.struts.action.ActionForm;
import org.apache.struts.util.MessageResources;
import edu.mit.coeus.utils.ModuleConstants;

/**
 *
 * @author  chandrashekara
 */
public class ProtocolGetAction extends ProtocolBaseAction{
    //    private ActionForward actionForward = null;
    //    private WebTxnBean webTxnBean ;
    //    private HttpServletRequest request;
    //    private HttpServletResponse response;
    //    private ActionMapping mapping;
    //    private HttpSession session;
    //    private DateUtils dateUtils = new edu.mit.coeuslite.utils.DateUtils();
    private static final String PLACEHOLDER_1 = "cwProtocolOrganizations";
    private static final String XML_PATH = "/edu/mit/coeuslite/iacuc/xml/JSPBuilder.xml";
    private static final String IACUC_HEADER_BEAN="iacucHeaderBean";

    //Added for Case#4275 - upload attachments until in agenda - Start
    private static final String UPLOAD_ATTACHMENTS = "AC008";
    private static final int SUBMITTED_TO_IACUC = 101;
    private static final String AMEND_RENEWAL_MODULES = "amendRenewModules";
    //Case#4275 - End

    //Added for COEUSQA-3275 For renewals, IRB approvers and reviewers can modify or remove attachments at approval/reviewing stage - start
    private int PENDING_IN_PROGRESS = 100;
    //Added for COEUSQA-3275 For renewals, IRB approvers and reviewers can modify or remove attachments at approval/reviewing stage - END

    //Added for  COEUSDEV-237 : Investigator cannot see review comments - Start
//    private static final String  REVIEW_IACUC_PROTOCOL = "REVIEW_IACUC_PROTOCOL";
    //COEUSDEV-237 : End
    /*COEUSQA-1724-New Constants created for Amendment/Renewal - Start */
    private static final String NEW_AMENDMENT = "NA";
    private static final String NEW_RENEWAL = "NR";
    private static final String NEW_RENEWAL_AMENDMENT = "RA";
    private static final String NEW_CONTINUATION_REVIEW = "CR";
    private static final String NEW_CONTINUATION_REVIEW_AMEND = "CA";
    private static final String NEW_SEQUENCE_NUMBER = "1";
    /*COEUSQA-1724-New Constants created for Amendment/Renewal - End */
    //COEUSQA-3042 Allow users to add an attachment for a renewal - Start
    private static final int RENEWAL_PROTOCOL = 2;
    //COEUSQA-3042 Allow users to add an attachment for a renewal - End

     //send notification email notification modification starts
    private static final String SEND_NOTFI_PARAM_FLAG="FROM_EMAIL";
    private static final String PROTOCOL_NUMBER="protocolNumber";
    private static final String SEQUENCE_NUMBER="sequenceNumber";
     //send notification email notification modification ends

    /** Creates a new instance of ProtocolGetAction */
    public ProtocolGetAction() {
    }

    public ActionForward performExecute(ActionMapping mapping , ActionForm actionForm ,
    HttpServletRequest request , HttpServletResponse response) throws Exception {

        HttpSession session= request.getSession();
//                DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)actionForm;
        session.removeAttribute("protocolColumnNames");
        session.removeAttribute("protocolList");
        session.removeAttribute("ProtocolTypes");
        session.removeAttribute("menuItemsVector");
        session.removeAttribute("ProtocolStatusTypes");
        session.removeAttribute("newProtocolFlag");
        session.removeAttribute("protocolStatusCode");
        session.removeAttribute("parentProtoData");
        session.removeAttribute("vecAddedAlternativeSearches");

        String protocolNumber = EMPTY_STRING;
        String seq = EMPTY_STRING;
        int sequenceNumber = -1;
        //send notification email notification modification starts
        if(request.getParameter(SEND_NOTFI_PARAM_FLAG)!=null){
            if(Boolean.valueOf(request.getParameter(SEND_NOTFI_PARAM_FLAG))){
                //flag show the link is from email. fetching the protocol number and sequence number
                protocolNumber=request.getParameter(PROTOCOL_NUMBER);
                seq=request.getParameter(SEQUENCE_NUMBER);
                session.setAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId(),protocolNumber);
                session.setAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId(),seq);
                setSelectedCoeusHeaderPath("008", request);
            }
        }
        //send notification email notification modification ends


        String  page  = request.getParameter(CoeusLiteConstants.PAGE);
        String createProto= request.getParameter("NEW_PROTOCOL");
        //        WebTxnBean webTxnBean=new WebTxnBean();
        HashMap hmpProtocolData = new HashMap();

        //Added for COEUSQA-2812 Questionnaire for amendment does not appear initially start

        String isFromAmendRenewHistroy =(String)request.getParameter("amendRenewHistroy");
        if("true".equals(isFromAmendRenewHistroy)){
            protocolNumber = (String)request.getParameter("protocolNumber");
            session.setAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId(),protocolNumber);

            Vector vecProtocolHeader = getProtocolHeader(protocolNumber,request);
            if(vecProtocolHeader!=null && vecProtocolHeader.size()>0){
                ProtocolHeaderDetailsBean bean = (ProtocolHeaderDetailsBean) vecProtocolHeader.get(0);
                session.setAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId(), bean.getSequenceNumber());
            }

        }

        //Added for COEUSQA-2812 Questionnaire for amendment does not appear initially end
        protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        seq = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());

        if(seq!= null){
            sequenceNumber = Integer.parseInt(seq);
        }

        hmpProtocolData.put("protocolNumber",protocolNumber);
        hmpProtocolData.put("sequenceNumber",new Integer(sequenceNumber));
        ActionForward actionForward = null;
        Map mapMenuList = null;
        if(page!= null){
            //Added for case#3057 - red asterisks on left side menu labels for mandatory custom fields - start
            boolean othersMandatory = othersHasMandatory(request);
            if(othersMandatory){
                session.setAttribute("othersMandatory", "Y");
            }else{
                session.removeAttribute("othersMandatory");
            }
            //Added for case#3057 - red asterisks on left side menu labels for mandatory custom fields - end
            //Added for CoeusLite4.3 header changes enhacement - Start
            session.removeAttribute("newIacucFlag");
            session.removeAttribute("IACUC_SUBMISSION_DETAILS");
            //Added for CoeusLite4.3 header changes enhacement - end
            // 3282: Reviewer view of Protocol Materials - Start
//            if( page.trim().equalsIgnoreCase(CoeusLiteConstants.GENERAL_INFO_PAGE)) {
            if( page.trim().equalsIgnoreCase(CoeusLiteConstants.GENERAL_INFO_PAGE)
                || page.trim().equalsIgnoreCase(CoeusLiteConstants.REVIEW_COMMENTS)) {
            // 3282: Reviewer view of Protocol Materials - End
                //Code Added for coeus4.3 enhancements for concurrent Amendments and Renewal
                //Code modified for Case#2785 - Protocol Routing
//                setMenuForAmendRenew(protocolNumber, request);
                setMenuForAmendRenew(protocolNumber, ""+sequenceNumber, request);
                //Code commented for coeus4.3 enhancements for concurrent Amendments and Renewal - starts
//                Vector menuItemsVector  = null;
//                HashMap hmData = new HashMap();
//                //                ReadProtocolDetails readProtocolDetails = new ReadProtocolDetails();
//                //                menuItemsVector = readProtocolDetails.readXMLDataForMenu("/edu/mit/coeuslite/irb/xml/ProtocolMenu.xml");
//                ReadXMLData readXMLData = new ReadXMLData();
//                menuItemsVector = readXMLData.readXMLDataForMenu("/edu/mit/coeuslite/irb/xml/ProtocolMenu.xml");
//                hmData.put(ModuleDataBean.class , new Integer(7));
//                hmData.put(SubModuleDataBean.class , new Integer(0));
//                hmData.put("link" , "/getQuestionnaire.do");
//                hmData.put("actionFrom" ,"PROTOCOL");
//                menuItemsVector = getQuestionnaireMenuData(menuItemsVector,request ,hmData);
//                //commented for making "New Amendment" "New Renewal" menu visible -start
////                session.setAttribute("menuItemsVector", menuItemsVector);
//                //commented for making "New Amendment" "New Renewal" menu visible -end
//                //Added for "New Amendment" "New Renewal" menus visible -start
//                HashMap hmStatus =  new HashMap();
//                hmStatus.put("protocolNumber",protocolNumber);
//                int protoStatusCode = 0;
//                Vector vecStatus = new Vector();
//                Vector vecProtocolHeader = (Vector)getProtocolHeader(protocolNumber, request);
//                if(vecProtocolHeader != null && vecProtocolHeader.size() >0){
//                    ProtocolHeaderDetailsBean bean = (ProtocolHeaderDetailsBean)vecProtocolHeader.elementAt(0);
//                    protoStatusCode = bean.getProtocolStatusCode();
//                }
//                if(protoStatusCode == 200 || protoStatusCode == 201 || protoStatusCode == 202 ||
//                        protoStatusCode == 203 || protoStatusCode == 300 || protoStatusCode == 301||
//                        protoStatusCode == 302 ||  protoStatusCode == 308){
//                    for(int ind = 0 ; ind < menuItemsVector.size(); ind++){
//                        MenuBean menuBean = (MenuBean)menuItemsVector.get(ind);
//                        String menuCode = menuBean.getMenuId();
//                        if(menuCode.equals("021") || menuCode.equals("022")){
//                            menuBean.setVisible(true);
//                        }
//                    }
//                }
//                session.setAttribute("menuItemsVector", menuItemsVector);
                //commented for making "New Amendment" "New Renewal" menu visible -end
                //Code commented for coeus4.3 enhancements for concurrent Amendments and Renewal. - ends
                // COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions - Start
                // 3282: Reviewer view of protocols - Start
//                if(protocolNumber.length()>10){
//                if(protocolNumber.length()>10 && ! CoeusLiteConstants.REVIEW_COMMENTS.equals(page)){
                    if(protocolNumber.length()>10 ){
                    // 3282: Reviewer view of protocols - End
                    // COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions - End
                    AmendmentRenewalAction amendmentRenewalAction = new AmendmentRenewalAction();
                    String menuCode = amendmentRenewalAction.getMenuId(protocolNumber, request);
                    amendmentRenewalAction.setSelectedMenu(menuCode, request);
                    //Code commented for coeus4.3 enhancements for concurrent Amendments and Renewal - starts
                    // If it is Amendment or Renewal protocol Amendment/Renewal summary page should be opened.
                    if(request.getParameter("SEARCH_ACTION") != null &&
                            !request.getParameter("SEARCH_ACTION").equals(EMPTY_STRING)
                            // COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions
                            && ! CoeusLiteConstants.REVIEW_COMMENTS.equals(page)){
                        String url =  "getIacucAmendmentRenewal.do?page="+protocolNumber.substring(10,11)+"S"+"&SEARCH_ACTION=";
                        RequestDispatcher rd = request.getRequestDispatcher(url);
                        rd.forward(request,response);
                        return null;
                    }
                    //Code commented for coeus4.3 enhancements for concurrent Amendments and Renewal - ends
                }

                mapMenuList = new HashMap();
                mapMenuList.put("menuItems",CoeusliteMenuItems.IACUC_MENU_ITEMS);
                mapMenuList.put("menuCode",CoeusliteMenuItems.IACUC_PROTOCOL_GENERAL_INFO_MENU);
                setSelectedMenuList(request, mapMenuList);
                actionForward = getGeneralInfoForm(hmpProtocolData, actionForm, request, mapping);
            }else if(page.equalsIgnoreCase(CoeusLiteConstants.SPECIAL_REVIEW_PAGE)){
                mapMenuList = new HashMap();
                mapMenuList.put("menuItems",CoeusliteMenuItems.IACUC_MENU_ITEMS);
                mapMenuList.put("menuCode",CoeusliteMenuItems.IACUC_PROTOCOL_SPECIAL_REVIEW_MENU);
                setSelectedMenuList(request, mapMenuList);
                actionForward = getSpecialReview(hmpProtocolData,request, mapping);
            }else if(page.equalsIgnoreCase(CoeusLiteConstants.AREA_OF_RESEARCH_PAGE)){
                mapMenuList = new HashMap();
                mapMenuList.put("menuItems",CoeusliteMenuItems.IACUC_MENU_ITEMS);
                mapMenuList.put("menuCode",CoeusliteMenuItems.IACUC_PROTOCOL_AREA_OF_RESEARCH_MENU);
                setSelectedMenuList(request, mapMenuList);
                actionForward = getAreaOfResearch(hmpProtocolData,request, mapping);
            }else if(page.equalsIgnoreCase(CoeusLiteConstants.FUNDING_SOURCE_PAGE)){
                mapMenuList = new HashMap();
                mapMenuList.put("menuItems",CoeusliteMenuItems.IACUC_MENU_ITEMS);
                mapMenuList.put("menuCode",CoeusliteMenuItems.IACUC_PROTOCOL_FUNDING_SOURCE_MENU);
                setSelectedMenuList(request, mapMenuList);
                actionForward = getFundingSourcesData(hmpProtocolData,request, mapping);
            }else if(page.equalsIgnoreCase(CoeusLiteConstants.VULNERABLE_SUBJECTS_PAGE)){
                mapMenuList = new HashMap();
                mapMenuList.put("menuItems",CoeusliteMenuItems.IACUC_MENU_ITEMS);
                mapMenuList.put("menuCode",CoeusliteMenuItems.SUBJECTS_MENU);
                setSelectedMenuList(request, mapMenuList);
                session.setAttribute("subjectCountExists", "NO");
                actionForward = getVulnerableSubjects(hmpProtocolData,request, mapping);
            }else if(page.equalsIgnoreCase(CoeusLiteConstants.UPLOAD_DOCUMENT_PAGE)){
                mapMenuList = new HashMap();
                mapMenuList.put("menuItems",CoeusliteMenuItems.IACUC_MENU_ITEMS);
                mapMenuList.put("menuCode",CoeusliteMenuItems.IACUC_PROTOCOL_UPLOAD_DOCUMENTS_MENU);
                setSelectedMenuList(request, mapMenuList);
                actionForward = getUploadDocument(hmpProtocolData,request, mapping);
            }else if(page.equalsIgnoreCase(CoeusLiteConstants.COPY_PROTOCOL)){
                mapMenuList = new HashMap();
                mapMenuList.put("menuItems",CoeusliteMenuItems.IACUC_MENU_ITEMS);
                mapMenuList.put("menuCode",CoeusliteMenuItems.COPY_PROTOCOL_MNEU);
                setSelectedMenuList(request, mapMenuList);
                actionForward = copyProtocolDetails(hmpProtocolData,request, mapping, response);
            }else if(page.equalsIgnoreCase(CoeusLiteConstants.NEW_AMENDMENT)){
                mapMenuList = new HashMap();
                mapMenuList.put("menuItems",CoeusliteMenuItems.IACUC_MENU_ITEMS);
                mapMenuList.put("menuCode",CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_SUMMARY_MENU);
                setSelectedMenuList(request, mapMenuList);
                actionForward = newAmendmentRenewal(hmpProtocolData,request, mapping, response,CoeusLiteConstants.NEW_AMENDMENT);
            }else if(page.equalsIgnoreCase(CoeusLiteConstants.NEW_RENEWAL)){
                mapMenuList = new HashMap();
                mapMenuList.put("menuItems",CoeusliteMenuItems.IACUC_MENU_ITEMS);
                mapMenuList.put("menuCode",CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_SUMMARY_MENU);
                setSelectedMenuList(request, mapMenuList);
                actionForward = newAmendmentRenewal(hmpProtocolData,request, mapping, response,CoeusLiteConstants.NEW_RENEWAL);
            }
            //Code modified for the case#4330-Use consistent name for Renewal with Amendment  - start
            else if(page.equalsIgnoreCase(CoeusLiteConstants.NEW_RENEWAL_WITH_AMENDMENT) ){
                mapMenuList = new HashMap();
                mapMenuList.put("menuItems",CoeusliteMenuItems.IACUC_MENU_ITEMS);
                mapMenuList.put("menuCode",CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_SUMMARY_MENU);
                setSelectedMenuList(request, mapMenuList);
                actionForward = newAmendmentRenewal(hmpProtocolData,request, mapping, response,CoeusLiteConstants.NEW_RENEWAL_WITH_AMENDMENT);
            }
           //Code modified for the case#4330-Use consistent name for Renewal with Amendment  - end
           /*COEUSQA-1724-Condition Added for IACUC New Amendment type - Start*/
            else if(CoeusLiteConstants.NEW_CONTINUATION_CONTINUING_REVIEW.equalsIgnoreCase(page)){
                mapMenuList = new HashMap();
                mapMenuList.put("menuItems",CoeusliteMenuItems.IACUC_MENU_ITEMS);
                mapMenuList.put("menuCode",CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_SUMMARY_MENU);
                setSelectedMenuList(request, mapMenuList);
                actionForward = newAmendmentRenewal(hmpProtocolData,request, mapping, response,CoeusLiteConstants.NEW_CONTINUATION_CONTINUING_REVIEW);
            } else if(CoeusLiteConstants.NEW_CONTINUATION_CONTINUING_REVIEW_WITH_AMENDMENT.equalsIgnoreCase(page)){
                mapMenuList = new HashMap();
                mapMenuList.put("menuItems",CoeusliteMenuItems.IACUC_MENU_ITEMS);
                mapMenuList.put("menuCode",CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_SUMMARY_MENU);
                setSelectedMenuList(request, mapMenuList);
                actionForward = newAmendmentRenewal(hmpProtocolData,request, mapping, response,CoeusLiteConstants.NEW_CONTINUATION_CONTINUING_REVIEW_WITH_AMENDMENT);
            }
            /*COEUSQA-1724-Condition Added for IACUC New Amendment type - End*/
        }
        if(createProto!= null && !createProto.equals(EMPTY_STRING)){
            //Read Protocol menu details starts
            Vector menuItemsVector  = null;
            // Code Modified for coeus 4.3 enhancement - starts
            //                ReadProtocolDetails readProtocolDetails = new ReadProtocolDetails();
            HashMap hmData = new HashMap();
            //                menuItemsVector = readProtocolDetails.readXMLDataForMenu("/edu/mit/coeuslite/irb/xml/ProtocolMenu.xml");
            ReadXMLData readXMLData = new ReadXMLData();
            menuItemsVector = readXMLData.readXMLDataForMenu("/edu/mit/coeuslite/iacuc/xml/IacucMenu.xml");
            // Code Modified for coeus 4.3 enhancement - ends
            // COEUSDEV-86: Questionnaire for a Submission - Start
            protocolNumber = protocolNumber==null? EMPTY_STRING: protocolNumber;
            hmData.put(ModuleDataBean.class , new Integer(ModuleConstants.IACUC_MODULE_CODE));

//            hmData.put(SubModuleDataBean.class , new Integer(0));
            int subModuleItemCode = 0;
            if(protocolNumber.length() > 10 && isIacucProtoAmendmentRenewal(protocolNumber.charAt(10))){
                subModuleItemCode = CoeusLiteConstants.IRB_SUB_MODULE_CODE_FOR_AMENDMENT_RENEWAL;
            } else {
                subModuleItemCode = 0;
            }
            hmData.put(SubModuleDataBean.class , new Integer(subModuleItemCode));

            hmData.put("link" , "/getQuestionnaire.do");
            hmData.put("actionFrom" ,"IACUC_PROTOCOL");
            //Modified for Case#3941 -Questionnaire answers missing after deleting the module - Start
//            protocolNumber = protocolNumber==null? EMPTY_STRING: protocolNumber;
            // COEUSDEV-86: Questionnaire for a Submission - End
            seq = seq == null ? EMPTY_STRING : seq;
            hmData.put("moduleItemKey",protocolNumber);
            hmData.put("moduleSubItemKey",seq);
            //Modified for Case#3941 -Questionnaire answers missing after deleting the module - End
            Vector modifiedVector = new Vector();
            for (int index=0; index<menuItemsVector.size();index++) {
                MenuBean meanuBean = (MenuBean)menuItemsVector.get(index);
                String menuId = meanuBean.getMenuId();
                if (menuId.equals("AC001")) {
                    meanuBean.setSelected(true);
                } else {
                    meanuBean.setSelected(false);
                }
                modifiedVector.add(meanuBean);
            }
            modifiedVector = getQuestionnaireMenuData(modifiedVector,request ,hmData);
            session.setAttribute("iacucmenuItemsVector", modifiedVector);
            //Added for CoeusLite4.3 header changes enhacement - Start
            session.setAttribute("newIacucFlag", "YES");
            session.removeAttribute("approvalDateFlag");
            //Added for CoeusLite4.3 header changes enhacement - end
            actionForward = getGeneralInfoForm(hmpProtocolData,actionForm,request, mapping);
            String subHeaderId =  request.getParameter("SUBHEADER_ID");
            readNavigationPath(subHeaderId,request);
            //                //Release the old lock if it exists  - Start
            //                String oldProtocolNumber = (String)session.getAttributUserInfoBeane(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
            //                if(oldProtocolNumber!= null){
            //                    UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user");
            //                    LockBean lockBean = getLockingBean(userInfoBean, oldProtocolNumber,request);
            //                    releaseLock(lockBean, request);
            //                        session.removeAttribute(CoeusLiteConstants.PROTOCOL_SEARCH_ACTION+session.getId());
            //                    session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(),
            //                            new Boolean(false));
            //                   // session.removeAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId());
            //                }
            ////                //Release the old lock if it exists  - End
        }
        //COEUSQA-3035-Indicator Logic Implementation for new IACUC Screens-Start
        session.setAttribute("oldSequenceNumber",sequenceNumber);
        //COEUSQA-3035-Indicator Logic Implementation for new IACUC Screens-Start
        readSavedStatus(request);
        return actionForward;
    }

    // Commented for PMD Check
    /*To get the KeyStudyPerson Data
     * @param hmpProtocolData contains protocolNumber ,sequenceNumber
     * @throws Exception
     * @return actionForward
     */
//    private ActionForward getKeyStudyPersonData(HashMap hmpProtocolData, HttpServletRequest request, ActionMapping mapping) throws Exception{
//        HttpSession session = request.getSession();
//        WebTxnBean webTxnBean = new WebTxnBean();
//        Hashtable htAffiliationTypes = (Hashtable)webTxnBean.getResults(request,"keyStudyPersonIacuc",hmpProtocolData);
//        session.setAttribute("affiliationTypes",htAffiliationTypes.get("getAffiliationTypes"));
//        //session.setAttribute("keyPersonData",htAffiliationTypes.get("getIacucKeyPersonList"));
//        request.setAttribute("keyPersonData",htAffiliationTypes.get("getIacucKeyPersonList"));
//
//        return mapping.findForward("keyStudyPerson");
//    }

    /*To get the Funding Source Data
     * @param hmpProtocolData contains protocolNumber ,sequenceNumber
     * @throws Exception
     * @return actionForward
     */
    private ActionForward getFundingSourcesData(HashMap hmpProtocolData,
    HttpServletRequest request,ActionMapping mapping) throws Exception{
        WebTxnBean webTxnBean  = new WebTxnBean();
        Hashtable htFundingSourceTypes = (Hashtable)webTxnBean.getResults(request,"fundingSourceFrmIacuc",hmpProtocolData);
        HttpSession session = request.getSession();
        Vector vecFundingSrcTypes = (Vector) htFundingSourceTypes.get("getFundSourceTypes");

        /** Added for Coeus 4.3 Enhancement -Start
         *  Added fundingSourceTypeFlag for Customization, these flags can be set from CodeTables.
         *  filter if fundingSourceTypeFlag is Y
         */
        Vector vecFilterFundingSrcTypes = new Vector();
        if( vecFundingSrcTypes !=null && vecFundingSrcTypes.size()>0 ){
            for(int index=0; index < vecFundingSrcTypes.size(); index++ ){
                DynaValidatorForm dynaForm = (DynaValidatorForm) vecFundingSrcTypes.get(index);
                String fundingSourceTypeFlag = (String) dynaForm.get("fundingSourceTypeFlag");
                if(fundingSourceTypeFlag !=null && fundingSourceTypeFlag.equals("Y")){
                    vecFilterFundingSrcTypes.addElement(dynaForm);
                }
            }
        }
        session.setAttribute("fundingTypes",  vecFilterFundingSrcTypes);
        //Added for Coeus 4.3 Enhancement - End

        session.setAttribute("iacucFundingSources",htFundingSourceTypes.get("getIacucFundingSources"));
        return mapping.findForward("fundingSource");
    }

    /**
     *This method id to upload protocl document data
     * @param hmpProtocolData contains protocolNumber ,sequenceNumber
     * @throws Exception
     * @return actionForward
     */
    private ActionForward getUploadDocument(HashMap hmpProtocolData,HttpServletRequest request,
    ActionMapping mapping) throws Exception{
        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
        String protocolNumber =
        (String)hmpProtocolData.get("protocolNumber");
        HttpSession session = request.getSession();
        String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
        if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
            //Added for Case#4275 - upload attachments until in agenda - Start
            boolean isEditable = checkAttachmentIsEditable(request,protocolNumber);
            String lockMode = (String)session.getAttribute(CoeusLiteConstants.LOCK_MODE+session.getId());
            //If isEditable true, then set Attachment page in modify mode
            if(isEditable){
                session.setAttribute(CoeusLiteConstants.MODIFY_PROTOCOL_ATTACHMENT+session.getId(),CoeusLiteConstants.MODIFY_MODE);
            }else if(!CoeusLiteConstants.LOCK_MODE.equalsIgnoreCase(lockMode)){
                session.setAttribute(CoeusLiteConstants.MODIFY_PROTOCOL_ATTACHMENT+session.getId(),CoeusLiteConstants.DISPLAY_MODE);
            }
        }
        //Case#4275 - end
        CoeusVector docTypes = protocolDataTxnBean.getProtocolDocumetTypes();
        session.setAttribute("DocTypes", docTypes);
        //        Vector vecUploadHistoryData = null;
        Vector vecUploadData = null;
        HashMap hmFromUsers = new HashMap();
        UserMaintDataTxnBean userTxnBean = new UserMaintDataTxnBean();
        if(protocolNumber != null && protocolNumber.length() > 10){
            String protoAmendRenew = protocolNumber.substring(0,10);
            Vector vecParentDocs = protocolDataTxnBean.getUploadDocumentForProtocol(protoAmendRenew);
            //Code added for coeus4.3 enhancements - starts
            //To filter the Active documents for the parent protocol.
            if(vecParentDocs!=null && vecParentDocs.size()>0){
                for(int index=0; index<vecParentDocs.size(); index++){
                    UploadDocumentBean bean = (UploadDocumentBean) vecParentDocs.get(index);
                    if(!hmFromUsers.containsKey(bean.getUpdateUser())){
                        String COIUserName = userTxnBean.getUserName(bean.getUpdateUser());
                        hmFromUsers.put(bean.getUpdateUser(), COIUserName);
                    }
                    if(bean.getStatusCode()==3){
                        vecParentDocs.remove(index--);
                        for(int count=0; count<vecParentDocs.size(); count++){
                            UploadDocumentBean beanData = (UploadDocumentBean) vecParentDocs.get(count);
                            if(bean.getDocumentId() == beanData.getDocumentId()){
                                vecParentDocs.remove(count--);
                            }
                        }
                    }
                }
            }
            //Code added for coeus4.3 enhancements - ends
            session.setAttribute("parentIacucData", vecParentDocs);
        }

        //        vecUploadHistoryData = (Vector)protocolDataTxnBean.getProtocolHistoryData(protocolNumber);
        //        vecUploadData = (Vector)protocolDataTxnBean.getUploadDocumentForProtocol(protocolNumber);
        //        session.setAttribute("historyData", (vecUploadHistoryData != null
        //                                                && vecUploadHistoryData.size() > 0 ) ?
        //                                                (Vector)vecUploadHistoryData :new Vector() );
        //        session.setAttribute("uploadAllData", vecUploadHistoryData);
        //Code added for coeus4.3 enhancements - starts
        //To filter the Active documents.
        vecUploadData = (Vector)protocolDataTxnBean.getUploadDocumentForProtocol(protocolNumber);
        Vector vecListData = new Vector();
        if(vecUploadData!=null && vecUploadData.size()>0){
            for(int index=0; index<vecUploadData.size(); index++){
                UploadDocumentBean bean = (UploadDocumentBean) vecUploadData.get(index);
                if(!hmFromUsers.containsKey(bean.getUpdateUser())){
                    String COIUserName = userTxnBean.getUserName(bean.getUpdateUser());
                    hmFromUsers.put(bean.getUpdateUser(), COIUserName);
                }
                if(bean.getStatusCode()==3){
                    vecUploadData.remove(index--);
                    vecListData.add(bean);
                }
            }
            vecUploadData.addAll(vecListData);
        }
        //Code added for coeus4.3 enhancements - ends
        //Commented/Modified for COEUSQA-3042 Allow users to add an attachment for a renewal - Start
        //The check is done whether the protocol is a renewal one, if it is a renewal protocol then the session attribute is
        //set to true or else the attribute is set to false
        //ProtocolDataTxnBean txnBean = new ProtocolDataTxnBean();
//        if(txnBean.isProtocolRenewalAmendment(protocolNumber) == RENEWAL_PROTOCOL ){
//                session.setAttribute("setAttachmentModifyForRenewal",new Boolean(true));
//        }else{
//            session.setAttribute("setAttachmentModifyForRenewal",new Boolean(false));
//        }
        //commented and Added for COEUSQA-3275 For renewals, IRB approvers and reviewers can modify or remove attachments at approval/reviewing stage - start
        ProtocolHeaderDetailsBean headerBean = (ProtocolHeaderDetailsBean)session.getAttribute(IACUC_HEADER_BEAN);
        ProtocolDataTxnBean txnBean = new ProtocolDataTxnBean();
        //Users should allow to add/modify/remove the attachments only when protocol status will be Pending in Progress
        //100 - Pending in Progress
        // if(isProtocolRenewalContinuation(protocolNumber)){
          if(isProtocolRenewalContinuation(protocolNumber) && headerBean.getProtocolStatusCode() == PENDING_IN_PROGRESS){
            session.setAttribute("setAttachmentModifyForRenewal",new Boolean(true));
          }else{
            session.setAttribute("setAttachmentModifyForRenewal",new Boolean(false));
          }
        //Commented/Modified for COEUSQA-3042 Allow users to add an attachment for a renewal - End
        session.setAttribute("uploadLatestData", vecUploadData);
        session.setAttribute("hmFromUsers", hmFromUsers);
        return mapping.findForward("uploadDocuments");
    }

    /** This method is to get the Areas of Research Data
     ** @param hmpProtocolData contains protocolNumber ,sequenceNumber
     * @throws Exception
     * @return actionForward
     */
    private ActionForward getAreaOfResearch(HashMap hmpProtocolData,HttpServletRequest request,
    ActionMapping mapping) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        Hashtable htAreaOfResearch = (Hashtable)webTxnBean.getResults(request,"iacucAreaResearch",hmpProtocolData);
        session.setAttribute("iacucAreaData",htAreaOfResearch.get("getIacucResearchAreas"));
        return mapping.findForward("areaOfResearch");
    }

    /*To get the Vulnerable Subjects data
     ** @param hmpProtocolData contains protocolNumber, sequenceNumber
     * @throws Exception
     * @return actionForward
     */
    private ActionForward getVulnerableSubjects(HashMap hmpProtocolData,HttpServletRequest request,
    ActionMapping mapping) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htInvestigator = (Hashtable)webTxnBean.getResults(request,"iacucVulnerableSubjects",hmpProtocolData);
        HttpSession session = request.getSession();
        session.setAttribute("Subject", htInvestigator.get("getVulnerableSubTypes"));
        request.setAttribute("VulnerableData", htInvestigator.get("getIacucVulnerableSubList"));
        //Added for Coeus4.3 subject count enhancement - Start
        //String subjectCount = getSubjectCountCode(request);
        MessageResources irbMessages = MessageResources.getMessageResources("coeus");
        String subjectCount = irbMessages.getMessage("PROTOCOL_SUBJECT_COUNT_DISPLAY_ENABLED");
        session.setAttribute("subjectCount", subjectCount);
        //Added for Coeus4.3 subject count enhancement - End
        return mapping.findForward("vulnerableSubjects");
    }


    /* To get the SpecialReview data
     * @param hmpProtocolData contains protocolNumber ,sequenceNumber
     * @throws Exception
     * @return actionForward
     */
    private ActionForward getSpecialReview(HashMap hmpProtocolData,HttpServletRequest request,
    ActionMapping mapping) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        DateUtils dateUtils = new edu.mit.coeuslite.utils.DateUtils();
        // COEUSQA-2320 Show in Lite for Special Review in Code table
        // Add module code to the map.
        hmpProtocolData.put("moduleCode", ModuleConstants.IACUC_MODULE_CODE);
        Hashtable htSpecialReview = (Hashtable)webTxnBean.getResults(request,"iacucSpecialReview",hmpProtocolData);
        HttpSession session = request.getSession();
        /** Added for Coeus 4.3 Enhancement -Start
         *  Added specialReviewFlag for Customization, these flags can be set from CodeTables.
         *  filter if specialReviewTypeFlag is Y
         */
        Vector vecSpecialReviewDetails = (Vector) htSpecialReview.get("getSpeciaReviewCode");
        Vector vecFilteredData = new Vector();
        for(int index=0; index<vecSpecialReviewDetails.size(); index++){
            DynaValidatorForm form = (DynaValidatorForm) vecSpecialReviewDetails.get(index);
            // COEUSQA-2320 Show in Lite for Special Review in Code table
            // if(form.get("specialReviewFlag")!=null && form.get("specialReviewFlag").equals("Y")){
            if("Y".equals(form.get("showInLite"))){
                vecFilteredData.add(form);
            }
        }
        session.setAttribute("iacucSplReview", vecFilteredData);
        //        session.setAttribute("splReview", htSpecialReview.get("getSpeciaReviewCode"));
        //Added for Coeus 4.3 Enhancement -ends
        session.setAttribute("approval", htSpecialReview.get("getReviewApprovalType"));
        Vector htInvestigator = (Vector)htSpecialReview.get("getIacucSpecialReview");
        if(htInvestigator!= null && htInvestigator.size() >0){
            for(int i=0;i<htInvestigator.size();i++){
                DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)htInvestigator.get(i);
                String applDate = (String)dynaValidatorForm.get("applicationDate");
                String approvalDate = (String)dynaValidatorForm.get("approvalDate");
                //COEUSQA-1724-Added for Expiration date column-start
                String expirationDate = (String)dynaValidatorForm.get("expirationDate");
                //COEUSQA-1724-Added for Expiration date column-end

                if(applDate != null){
                    String value = dateUtils.formatDate(applDate,edu.mit.coeuslite.utils.DateUtils.MM_DD_YYYY);
                    dynaValidatorForm.set("applicationDate",value);
                }else{
                    dynaValidatorForm.set("applicationDate","");
                }
                if(approvalDate != null){
                    String dateValue = dateUtils.formatDate(approvalDate,edu.mit.coeuslite.utils.DateUtils.MM_DD_YYYY);
                    dynaValidatorForm.set("approvalDate",dateValue);

                }else{
                    dynaValidatorForm.set("approvalDate","");
                }
                //COEUSQA-1724-Added for Expiration date column-start
                if(expirationDate != null){
                    String expirationDateValue = dateUtils.formatDate(expirationDate,edu.mit.coeuslite.utils.DateUtils.MM_DD_YYYY);
                    dynaValidatorForm.set("expirationDate",expirationDateValue);

                }else{
                    dynaValidatorForm.set("expirationDate","");
                }
                //COEUSQA-1724-Added for Expiration date column-end
            }
        }
        session.setAttribute("ReviewList",htInvestigator);
        return mapping.findForward("iacucSpecialReview");
    }


    /*To get the Investigators data
     * @param hmpProtocolData contains protocolNumber ,sequenceNumber
     * @throws Exception
     * @return actionForward
     */
    /*private ActionForward getInvestigators(HashMap hmpProtocolData) throws Exception{
        HttpSession session = request.getSession();
        Hashtable htInvestigator = (Hashtable)webTxnBean.getResults(request,"investigators",hmpProtocolData);
        session.setAttribute("affiliationTypes", htInvestigator.get("getAffiliationTypes"));
        Vector invData=(Vector)htInvestigator.get("getProtocolInvestigators");
        if(invData==null || invData.size() <=0){
            PersonInfoBean personBean = (PersonInfoBean)session.getAttribute("person");
            String Id = personBean.getPersonID();
            HashMap hmperson = new HashMap();
            hmperson.put("personId", Id);
            Hashtable htpersonData = (Hashtable)webTxnBean.getResults(request,"getPersonInfo",hmperson);
            Vector vcpersonData=(Vector)htpersonData.get("getPersonInfo");
            DynaValidatorForm investigatorForm = (DynaValidatorForm)vcpersonData.get(0);
            request.setAttribute("investigatorForm", investigatorForm);
        }
        for(int i=0;i<invData.size();i++) {
            DynaValidatorForm invForm=(DynaValidatorForm)invData.get(i);
            String personId=(String)invForm.get("personId");
            HashMap hmpInvData = new HashMap();
            hmpInvData.put("protocolNumber",(String)hmpProtocolData.get("protocolNumber"));
            hmpInvData.put("sequenceNumber",(Integer)hmpProtocolData.get("sequenceNumber"));
            //            hmpInvData.put("protocolNumber","0311000014");
            //            hmpInvData.put("sequenceNumber",new Integer(1));
            hmpInvData.put("personId",personId);
            Hashtable hInvUnits=(Hashtable)webTxnBean.getResults(request,"getProtoInvestigatorUnits",hmpInvData);
            Vector cvInvUnits=(Vector)hInvUnits.get("getProtoInvestigatorUnits");
            ArrayList invUnitList=new ArrayList(cvInvUnits);
            invForm.set("investigatorUnits",invUnitList);
        }
        request.setAttribute("protocolInvData" , invData);
        // This vector is not used in the JSP. The code corresponding this is not
        // used anywhere in user interactive JSP
        //UnComment the code for display role when user click on investigors link start - 1
        request.setAttribute("investigatorRoles",getInvestigatorRoles(invData));
        //UnComment the code for display role when user click on investigors link end - 1
        return mapping.findForward("investigator");
    }*/


    /*To get the General info form details *
     * @param hmpProtocolData contains protocolNumber ,sequenceNumber
     * @throws Exception
     * @return actionForward
     */
    private ActionForward getGeneralInfoForm(HashMap hmpProtocolData , ActionForm actionForm,
    HttpServletRequest request, ActionMapping mapping)
    throws Exception{
        HttpSession session = request.getSession();
        String protocolNumber =
        (String)hmpProtocolData.get("protocolNumber");
        //Added for COEUSDEV-237 : Investigator cannot see review comments  - Start
        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
        //COEUSDEV-237 :End
        DynaValidatorForm form = (DynaValidatorForm)actionForm ;
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        Hashtable htGeneralInfo = null;

        ReadJSPPlaceHolder holder = new ReadJSPPlaceHolder(XML_PATH);
        Hashtable data  = holder.readXMLData(PLACEHOLDER_1);

        request.setAttribute("placeholder", data );
        session.setAttribute(CoeusLiteConstants.LOCK_MODE+session.getId(), EMPTY_STRING);
        //        canLock(mode, protocolNumber, userInfoBean.getUnitNumber(), userInfoBean.getUserId());
        // COEUSDEV-156: Investigator can add and modify review comments in IRB Lite - Start
//        if(protocolNumber!=null && protocolNumber.length() <= 10){
        if(protocolNumber!=null){

        // COEUSDEV-156: Investigator can add and modify review comments in IRB Lite - End
            //Added for COEUSDEV-237 : Investigator cannot see review comments
            String seqNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
            int sequenceNumber = -1;
            if(seqNumber!=null){
                sequenceNumber = Integer.parseInt(seqNumber);
            }
            //COEUSDEV-237 : End
            boolean isEdit =  checkIsProtocolEditable(request,protocolNumber.trim());
            // 3282: Reviewer View of Protocol materials - Start
//            if(isEdit){
            if(!isEdit){
            // 3282: Reviewer View of Protocol materials - End
                // 4361: Rights checking issue in protocol in Lite - Start
                // Check if the user can view this particular Protocol
                boolean hasViewRights = checkCanViewProtocol(request, protocolNumber.trim());
                if(!hasViewRights){
                    //Added for COEUSDEV-237 : Investigator cannot see review comments
                    //To check user is has REVIEW_IACUC_PROTOCOL right/active member in protocol committee/added as review for the protocol
                    UserDetailsBean userDetailsBean = new UserDetailsBean();
                    String personId = userDetailsBean.getPersonID(userInfoBean.getUserId());
                    boolean isUserActiveCommitteeMember = false;
                    boolean hasReviewerRight = false;
                    String leadUnit = EMPTY_STRING;
                    ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
                    ProtocolSubmissionInfoBean protocolSubmissionInfoBean = protocolSubmissionTxnBean.getProtocolSubmissionDetails(protocolNumber.trim());
                    if(protocolSubmissionInfoBean != null){
                        ProtocolAuthorizationBean protocolAuthorizationBean = new ProtocolAuthorizationBean();
                        if(protocolSubmissionInfoBean != null){
                            leadUnit = protocolDataTxnBean.getLeadUnitForProtocol(protocolNumber,sequenceNumber);
                        }
                        boolean hasIRBAdminRight = protocolAuthorizationBean.hasIRBAdminRights(userInfoBean.getUserId(),leadUnit);
                        if(!hasIRBAdminRight){
                            if(protocolSubmissionInfoBean.getCommitteeId() != null){
                                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                                CommitteeTxnBean committeeTxnBean = new CommitteeTxnBean();
                                CommitteeMaintenanceFormBean committeeMaintenanceFormBean = committeeTxnBean.getCommitteeDetails(protocolSubmissionInfoBean.getCommitteeId());
                                //Checks REVIEW_IACUC_PROTOCOL right in committee home unit
                                hasReviewerRight = userMaintDataTxnBean.getUserHasRight(userInfoBean.getUserId(),"REVIEW_IACUC_PROTOCOL",committeeMaintenanceFormBean.getUnitNumber());
                                if(!hasReviewerRight){
                                    Vector vecReviewers = getReviewers(request,protocolNumber,protocolSubmissionInfoBean.getSequenceNumber(),protocolSubmissionInfoBean.getSubmissionNumber());
                                    if(vecReviewers.size() > 0){
                                        for(int index=0;index<vecReviewers.size();index++){
                                            DynaValidatorForm irbProtocolActionsForm = (DynaValidatorForm)vecReviewers.get(index);
                                            String reviewerPersonId = (String)irbProtocolActionsForm.get("personId");
                                            //Checks logged in user personid is available in reviewers list of the protocol
                                            if(personId != null && personId.equals(reviewerPersonId)){
                                                hasReviewerRight = true;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if(!hasIRBAdminRight && !hasReviewerRight){
                            //Checks user exist in committee to which protocol is submitted
                            if(protocolSubmissionInfoBean.getCommitteeId() != null){
                                isUserActiveCommitteeMember = protocolDataTxnBean.isPersonActiveCommitteeMember(personId,protocolSubmissionInfoBean.getCommitteeId());
                            }
                        }
                        boolean isUserProtoInvestigator = false;
                        if(!hasIRBAdminRight && !hasReviewerRight && !isUserActiveCommitteeMember){
                            isUserProtoInvestigator = protocolDataTxnBean.isUserProtocolInvestigator(protocolNumber,sequenceNumber,userInfoBean.getUserId());
                        }
                        //If user is part of the committee/has REVIEW_IACUC_PROTOCOL right/reviewer of the protocol
                        //able to view the protocol in display mode
                        if(hasIRBAdminRight || isUserActiveCommitteeMember || hasReviewerRight || isUserProtoInvestigator){
                            hasViewRights = true;
                        }

                    }
                    if(!hasViewRights){//COEUSDEV-237 : End
                        // COEUSDEV-156: Investigator can add and modify review comments in IRB Lite - Start
                        String fromReviewer = request.getParameter("fromIsReviewer");
                        if("true".equalsIgnoreCase(fromReviewer)){
                            return mapping.findForward("noRightsToViewFromReviewer");
                        }
                        // COEUSDEV-156: Investigator can add and modify review comments in IRB Lite - End
                        return mapping.findForward("noRights");
                    }
                }
                // 4361: Rights checking issue in protocol in Lite - End
                session.setAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId(),CoeusLiteConstants.DISPLAY_MODE);
                String mode = (String) session.getAttribute("mode"+session.getId());
                if(mode!=null && mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
                    LockBean lockBean = getLockingBean(userInfoBean,
                    (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()), request);
                    releaseLock(lockBean, request);
                    lockBean = getLockingBean(userInfoBean,
                    (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId())+CoeusConstants.IACUC_AMENDMENT, request);
                    releaseLock(lockBean, request);
                    lockBean = getLockingBean(userInfoBean,
                    (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId())+CoeusConstants.IACUC_RENEWAL, request);
                    releaseLock(lockBean, request);
                    lockBean = getLockingBean(userInfoBean,
                    (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId())+CoeusConstants.IACUC_RENEWAL_WITH_AMENDMENT, request);
                    releaseLock(lockBean, request);
                    //COEUSQA-1724-Added for new Continuation/continuing-Start
                    lockBean = getLockingBean(userInfoBean,
                    (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId())+CoeusConstants.IACUC_CONTINUATION_CONTINUING_REVIEW, request);
                    releaseLock(lockBean, request);
                    lockBean = getLockingBean(userInfoBean,
                    (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId())+CoeusConstants.IACUC_CONTI_CONTINUING_REVIEW_WITH_AMEND, request);
                    releaseLock(lockBean, request);
                    //COEUSQA-1724-Added for new Continuation/continuing-End
                }
            }else{
                session.setAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId(),CoeusLiteConstants.MODIFY_MODE);
            }
            boolean isValid = prepareLock(protocolNumber, request);
            if(!isValid){
                session.setAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId(),"D");
                session.setAttribute(CoeusLiteConstants.LOCK_MODE+session.getId(), CoeusLiteConstants.DISPLAY_MODE);
            }else{
                // Added to fix the Amendment/renewal lock issue during submission
                session.setAttribute(CoeusLiteConstants.LOCK_MODE+session.getId(), CoeusLiteConstants.MODIFY_MODE);
            }
        }
        WebTxnBean webTxnBean = new WebTxnBean();
        //        String mode = (String)session.getAttribute(CoeusLiteConstants.IACUC_MODE_DETAILS+session.getId());
        htGeneralInfo = (Hashtable)webTxnBean.getResults(request, "generalIacucInfo", hmpProtocolData);

        Vector protoTypes =(Vector)htGeneralInfo.get("getIacucTypes");
        Vector projectTypes =(Vector)htGeneralInfo.get("getIacucProtoProjectTypes");

        if(projectTypes == null){
            projectTypes = new Vector();
        }
        session.setAttribute("IacucProtoProjectTypes", projectTypes);
        //Modified for case# 3016 - Ability to filter Protocol Type  - Start
        Vector vecProtoTypeFiltData = new Vector();
        if(protoTypes != null && protoTypes.size() >0){
            for(int index=0; index<protoTypes.size(); index++){
                DynaValidatorForm protoTypeFlagform = (DynaValidatorForm) protoTypes.get(index);
                if(protoTypeFlagform.get("protocolTypeFlag")!=null && protoTypeFlagform.get("protocolTypeFlag").equals("Y")){
                    ComboBoxBean comboBoxBean = new ComboBoxBean();
                    comboBoxBean.setCode((String)protoTypeFlagform.get("protocolTypeCode"));
                    comboBoxBean.setDescription((String)protoTypeFlagform.get("protocolTypeDesc"));
                    vecProtoTypeFiltData.add(comboBoxBean);
                }
            }
            if(vecProtoTypeFiltData != null && vecProtoTypeFiltData.size() >0){
                session.setAttribute("IacucTypes", vecProtoTypeFiltData);
            }else{
                vecProtoTypeFiltData = new Vector();
                session.setAttribute("IacucTypes", vecProtoTypeFiltData);
            }
        }else{
            session.setAttribute("IacucTypes", new Vector());
        }
//        session.setAttribute("ProtocolTypes", protoTypes);// commented for case#3016
        //Modified for case# 3016 - Ability to filter Protocol Type  - End
        Vector protoStatus =(Vector)htGeneralInfo.get("getIacucStatus");
         session.setAttribute("IacucStatusTypes", protoStatus);
        Vector protoOrg =(Vector)htGeneralInfo.get("getIacucOrgTypes");
        session.setAttribute("OrganizationTypes", protoOrg);
        Vector cvProtoData=(Vector)htGeneralInfo.get("getIacucInfo");
        HashMap hmpDefaultOrgVal = new HashMap();
        String defaultOrgId = "DEFAULT_ORGANIZATION_ID";
        hmpDefaultOrgVal.put("defaultOrgId",defaultOrgId);
        Hashtable htOrg = new Hashtable();
        htOrg = (Hashtable)webTxnBean.getResults(request, "getDefaultOrganizationId", hmpDefaultOrgVal);
        HashMap hm = (HashMap)htOrg.get("getDefaultOrganizationId");
        String orgId = (String)hm.get("ls_value");

        HashMap hmpDefaultOrgName = new HashMap();
        hmpDefaultOrgName.put("organizationId",orgId);
        Hashtable htOrgName = new Hashtable();
        htOrgName = (Hashtable)webTxnBean.getResults(request, "getOrganizationName", hmpDefaultOrgName);
        HashMap hmp = (HashMap)htOrgName.get("getOrganizationName");
        String organizationName = (String)hmp.get("as_organization_name");
        session.setAttribute("organizationName", organizationName);
        session.setAttribute("organizationId",orgId);
        if(cvProtoData!= null && cvProtoData.size() >0){
            DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)cvProtoData.get(0);
            request.setAttribute("statusCode",dynaValidatorForm.get("protocolStatusCode"));
            InitialiseIndicators(dynaValidatorForm,session);
            //Added for case# 3016 - Ability to filter Protocol Type  - Start
            String protoTypeCode = (String)dynaValidatorForm.get("protocolTypeCode");
            String protoTypeDesc = (String)dynaValidatorForm.get("protocolTypeDesc");
            //Vector vecFilData  = new Vector();
            boolean protoTypePresent = false;
            if(vecProtoTypeFiltData != null){// if the vector is null then it fails here
                                ComboBoxBean protoCmb = new ComboBoxBean();
                                for(int typeIndex = 0;typeIndex < vecProtoTypeFiltData.size();typeIndex++){// if there are no elements in the vector,then condition fails here
                                    ComboBoxBean comboBoxBean = (ComboBoxBean)vecProtoTypeFiltData.get(typeIndex);
                                    if(protoTypeCode.equals(comboBoxBean.getCode())){
                                        protoTypePresent = true;
                                    }
                                }
                                if(!protoTypePresent){
                                    protoCmb.setCode(protoTypeCode);
                                    protoCmb.setDescription(protoTypeDesc);
                                    vecProtoTypeFiltData.add(protoCmb);
                                }
                            }
                            session.setAttribute("IacucTypes", vecProtoTypeFiltData);
            //Added for case# 3016 - Ability to filter Protocol Type  - End
            String applDate = (String)dynaValidatorForm.get("applicationDate");
            String approvalDate = (String)dynaValidatorForm.get("approvalDate");
            String lastApprovalDate = (String)dynaValidatorForm.get("lastApprovalDate");
            String expirationDate = (String)dynaValidatorForm.get("expirationDate");
            dynaValidatorForm.set("applicationDate",formatedDate(applDate));
            dynaValidatorForm.set("approvalDate", formatedDate(approvalDate));
            dynaValidatorForm.set("lastApprovalDate", formatedDate(lastApprovalDate));
            dynaValidatorForm.set("expirationDate", formatedDate(expirationDate));

            dynaValidatorForm.set("organizationName",organizationName);
            dynaValidatorForm.set("organizationId",orgId);
            //Added for Coeus4.3 header changes enhacement - Start
            if (dynaValidatorForm.get("approvalDate").equals(EMPTY_STRING)){
                session.setAttribute("approvalDateFlag", "NO");
            }else{
                session.removeAttribute("approvalDateFlag");
            }
            //Added for Coeus4.3 header changes enhacement - end
            session.setAttribute("statusCode", dynaValidatorForm.get("protocolStatusCode"));
            request.setAttribute("iacucGeneralInfoForm", dynaValidatorForm);
        }else{
            form = (DynaValidatorForm)form.getDynaClass().newInstance();
            request.setAttribute("statusCode",CoeusLiteConstants.PROTOCOL_INCOMPLETE_STATUS_CODE);
            form.set("organizationName",organizationName);
            session.setAttribute("statusCode", form.get("protocolStatusCode"));
            request.setAttribute("iacucGeneralInfoForm", form);
        }

         /* COEUSQA 3909*/
        ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
                    if(protocolNumber!=null){
                    ProtocolSubmissionInfoBean protocolSubmissionInfoBean = protocolSubmissionTxnBean.getProtocolSubmissionDetails(protocolNumber.trim());
                    if(protocolSubmissionInfoBean != null){
                     session.setAttribute("statusCodeForMap",Integer.toString(protocolSubmissionInfoBean.getSubmissionStatusCode()));
                    }
                    else{
                      session.removeAttribute("statusCodeForMap");
                    }
                    }

        /**getting protocol header details**/
        Vector vecProtocolHeader = (Vector)getProtocolHeader(protocolNumber, request);
        if(protocolNumber!=null && !protocolNumber.equals(EMPTY_STRING)){
            if(vecProtocolHeader != null && vecProtocolHeader.size() > 0){
                session.setAttribute(IACUC_HEADER_BEAN, (ProtocolHeaderDetailsBean)vecProtocolHeader.elementAt(0));
            }
        }else{
            /*while creating new protocol bean removed from session*/
            session.removeAttribute(IACUC_HEADER_BEAN);
        }
        Vector cvOrg=(Vector)htGeneralInfo.get("getIacucList");
        request.setAttribute("organizations", cvOrg);

        // Enable and Disabling Submit for Review menu
        ProtocolSubmissionAction protocolSubmissionAction = new ProtocolSubmissionAction();
        protocolSubmissionAction.setSubmitForReviewMenu(request);
        // 3282: Reviewer view of Protocol Materials - Start
        String  page  = request.getParameter(CoeusLiteConstants.PAGE);
        if(CoeusLiteConstants.REVIEW_COMMENTS.equalsIgnoreCase(page)){
            return mapping.findForward("reviewComments");
        }
        // 3282: Reviewer view of Protocol Materials - End
        return mapping.findForward("iacucGeneralInfo");
    }

    /**
     * This methood is to initialize the protocol indicators in session
     * @param form
     * @param session
     */
    private void InitialiseIndicators(DynaValidatorForm form,HttpSession session){
        if(form!= null){
            String acType = (String)form.get("acType");
            if(acType== null|| acType.trim().equals("")){
                HashMap indicatorMap = new HashMap();

                String specialReview = (String)form.get(CoeusLiteConstants.SPECIAL_REVIEW_INDICATOR);
                String vulerable = (String)form.get(CoeusLiteConstants.VULNERABLE_SUBJECT_INDICATOR);
                String keyStudy = (String)form.get(CoeusLiteConstants.KEY_STUDY_PERSON_INDICATOR);
                String fundingSource = (String)form.get(CoeusLiteConstants.FUNDING_SOURCE_INDICATOR);
                String correspondent = (String)form.get(CoeusLiteConstants.CORRESPONDENT_INDICATOR);
                String reference = (String)form.get(CoeusLiteConstants.REFERENCE_INDICATOR);
                String related = (String)form.get(CoeusLiteConstants.RELATED_PROJECTS_INDICATOR);
                //Added for indicator logic start
                String scientific = (String)form.get(CoeusLiteConstants.SCIENTIFIC_JUSTIF_INDICATOR);
                String alterSearch = (String)form.get(CoeusLiteConstants.ALTERNATIVE_SEARCH_INDICATOR);
                String speciesStudyGroupIndicator = (String)form.get(CoeusLiteConstants.SPECIES_STUDY_GROUP_INDICATOR);
                //Added for indicator logic end
                indicatorMap.put(CoeusLiteConstants.SPECIAL_REVIEW_INDICATOR,specialReview);
                indicatorMap.put(CoeusLiteConstants.VULNERABLE_SUBJECT_INDICATOR,vulerable);
                indicatorMap.put(CoeusLiteConstants.KEY_STUDY_PERSON_INDICATOR,keyStudy);
                indicatorMap.put(CoeusLiteConstants.FUNDING_SOURCE_INDICATOR,fundingSource);
                indicatorMap.put(CoeusLiteConstants.CORRESPONDENT_INDICATOR,correspondent);
                indicatorMap.put(CoeusLiteConstants.REFERENCE_INDICATOR,reference);
                indicatorMap.put(CoeusLiteConstants.RELATED_PROJECTS_INDICATOR,related);
                //Added for indicator logic start
                indicatorMap.put(CoeusLiteConstants.SCIENTIFIC_JUSTIF_INDICATOR,scientific);
                indicatorMap.put(CoeusLiteConstants.ALTERNATIVE_SEARCH_INDICATOR,alterSearch);
                indicatorMap.put(CoeusLiteConstants.SPECIES_STUDY_GROUP_INDICATOR,speciesStudyGroupIndicator);
                //Added for indicator logic end
                session.setAttribute(CoeusLiteConstants.IACUC_PROTOCOL_INDICATORS,indicatorMap);

            }
        }
    }

    // Commented for PMD Check
    /**This method is to get the Investigator Roles
     * @param invData
     * @return
     */
//    private Vector getInvestigatorRoles(Vector invData) {
//        boolean hasPI = false;
//        if(invData != null && invData.size()>0) {
//            for (int index=0;index<invData.size();index++) {
//                DynaValidatorForm invForm = (DynaValidatorForm)invData.get(index);
//                String principalInvestigator = (String)invForm.get("principalInvestigatorFlag");
//                if(principalInvestigator.equals("Y"))
//                    hasPI = true;
//            }
//        }
//
//        Vector vecInvestigatorRoles = new Vector();
//        ComboBoxBean invRole = new ComboBoxBean();
//        if(!hasPI) {
//            invRole.setCode("0");
//            invRole.setDescription("Principal Investigator");
//            vecInvestigatorRoles.addElement(invRole);
//        }
//        invRole = new ComboBoxBean();
//        invRole.setCode("1");
//        invRole.setDescription("Co-Investigator");
//        vecInvestigatorRoles.addElement(invRole);
//        return vecInvestigatorRoles;
//    }

    public void cleanUp() {
    }

    /**
     * This method is used to format the date into  MM/DD/YYYY
     * @return formated date
     * @param varDate
     * @throws Exception
     */
    private String formatedDate(String varDate)throws Exception{
        String formatDate = EMPTY_STRING;
        if(varDate != null){
            DateUtils dateUtils = new edu.mit.coeuslite.utils.DateUtils();
            formatDate = dateUtils.formatDate(varDate,
            edu.mit.coeuslite.utils.DateUtils.MM_DD_YYYY);
        }
        return formatDate;
    }


    /**
     * This method is used to Copy  all the Protocol details
     * @param hmpProtocolData
     * @throws Exception
     * @return
     */
    private ActionForward copyProtocolDetails(HashMap hmpProtocolData,HttpServletRequest request,
    ActionMapping mapping, HttpServletResponse response ) throws Exception{
        LockBean lockBean = null;
        String protocolNumber = (String)hmpProtocolData.get("protocolNumber");
        request.setAttribute(CoeusLiteConstants.RELEASE_LOCK, CoeusLiteConstants.YES);
        String  sequenceNumber = String.valueOf( hmpProtocolData.get("sequenceNumber"));
        String newProtocolNumber = EMPTY_STRING;
        boolean isSuccess = false;
        if(protocolNumber !=null && protocolNumber.length()==10 ){
            WebTxnBean webTxnBean = new WebTxnBean();
            Hashtable htProtocolNumber =
            (Hashtable)webTxnBean.getResults(request, "generateIacucNumber", null);
            HashMap protocolData =
            (HashMap)htProtocolNumber.get("generateIacucNumber");
            newProtocolNumber = (String)protocolData.get("protocolNumber");
            isSuccess = true;
        }
        //These piece of code is commented because Their is no functionality to copy an amendment/renewals-Start
        /*
        else if(protocolNumber !=null &&
        (protocolNumber.length()>10 && protocolNumber.length()<20) &&
        (protocolNumber.charAt(10)== CoeusConstants.IACUC_AMENDMENT)){
            newProtocolNumber = protocolNumber+"A001";
            isSuccess = true;

        }else if(protocolNumber !=null &&
        (protocolNumber.length()>10 && protocolNumber.length()<20) &&
        (protocolNumber.charAt(10)== CoeusConstants.IACUC_RENEWAL)){
            newProtocolNumber = protocolNumber+"R001";
            isSuccess = true;
        }*/
        //These piece of code is commented because Their is no functionality to copy an amendment/renewals-Start
        if(isSuccess){
            WebTxnBean webTxnBean = new WebTxnBean();
            hmpProtocolData.put("targetProtocolNumber", newProtocolNumber);
            hmpProtocolData.put("summary","");
            webTxnBean.getResults(request, "copyIacuc", hmpProtocolData);

            //COEUSQA:3503 - Protocol Copying - Option to Copy Attachments and Questionnaires - Start
            ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
            HttpSession session = request.getSession();
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean(userInfoBean.getUserId());
            
            if("Y".equals(request.getParameter("attachment"))){

                Vector protoAttachments = protocolDataTxnBean.getUploadDocumentForProtocol(protocolNumber);
                if(protoAttachments != null && !protoAttachments.isEmpty()){
                    for(Object protoattachments : protoAttachments){
                        UploadDocumentBean uploadDocBean =(UploadDocumentBean)protoattachments;

                        uploadDocBean = protocolDataTxnBean.getUploadDocumentForVersionNumber(uploadDocBean.getProtocolNumber(), uploadDocBean.getSequenceNumber(),
                                uploadDocBean.getDocCode(),uploadDocBean.getVersionNumber(), uploadDocBean.getDocumentId());

                        uploadDocBean.setProtocolNumber(newProtocolNumber);
                        uploadDocBean.setSequenceNumber(Integer.parseInt(sequenceNumber));
                        uploadDocBean.setStatusCode(1);
                        uploadDocBean.setAcType(TypeConstants.INSERT_RECORD);
                        uploadDocBean.setAmended(false);
                        int versionNumber = protocolUpdateTxnBean.getNextVersionNumber(newProtocolNumber ,
                                Integer.parseInt(sequenceNumber) , uploadDocBean.getDocCode(), uploadDocBean.getDocumentId());
                        uploadDocBean.setVersionNumber(versionNumber);

                        protocolUpdateTxnBean.addUpdProtocolUpload(uploadDocBean);
                    }
                }
            }
            if("Y".equals(request.getParameter("otherAttchment"))){

                Vector protoOtherAttachments =protocolDataTxnBean.getProtoOtherAttachments(protocolNumber);

                if(protoOtherAttachments != null && !protoOtherAttachments.isEmpty()){
                    for(Object protoOtherattachments : protoOtherAttachments){
                        UploadDocumentBean uploadDocBean =(UploadDocumentBean)protoOtherattachments;
                        int docCode = uploadDocBean.getDocCode();

                        uploadDocBean = protocolDataTxnBean. getProtoOtherAttachment(uploadDocBean.getProtocolNumber(), uploadDocBean.getDocumentId());

                        uploadDocBean.setProtocolNumber(newProtocolNumber);
                        uploadDocBean.setSequenceNumber(Integer.parseInt(sequenceNumber));
                        uploadDocBean.setDocCode(docCode);
                        uploadDocBean.setAcType(TypeConstants.INSERT_RECORD);

                        protocolUpdateTxnBean.addUpdDelProtoOtherAttachment(uploadDocBean);
                    }
                }
            }
            if("Y".equals(request.getParameter("questionnaire"))){
                protocolUpdateTxnBean.copyProtocolQuestionnaire(protocolNumber, newProtocolNumber, sequenceNumber);
            }
            //COEUSQA:3503 - End
            //COEUSQA-3848-start
            lockBean = getLockingBean(userInfoBean,protocolNumber,request);
            releaseLock(lockBean,request);
            //COEUSQA-3848-End
        }
        if(newProtocolNumber !=null && !newProtocolNumber.equals(EMPTY_STRING)){
            protocolNumber= newProtocolNumber;
            sequenceNumber = NEW_SEQUENCE_NUMBER;
        }
        String url =  "/getIacucData.do?SEARCH_ACTION=SEARCH_ACTION&PAGE=G&protocolNumber=";
        request.setAttribute("CopyProtocol","CopyProtocol");
        url += protocolNumber;
        url +="&sequenceNumber=";
        url += sequenceNumber;

        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request,response);
        return null ;
    }

    /**
     * In this method rights are checked for creating new Amendment/Renewal and redirected to
     * respective pages
     * @param hmpProtocolData
     * @throws Exception
     * @return
     */
    private ActionForward newAmendmentRenewal(HashMap hmpProtocolData,HttpServletRequest request,
            ActionMapping mapping, HttpServletResponse response , String amendRenew) throws Exception{
        // Modified for  COEUSQA-3535 : CoeusLite IACUC "Double Amendment" Issue - Start
//        String protocolNumber = (String)hmpProtocolData.get("protocolNumber");
//        String  sequenceNumber = String.valueOf( hmpProtocolData.get("sequenceNumber"));
        String protocolNumber = request.getParameter("protocolNumber");
        String  sequenceNumber =  request.getParameter("sequenceNumber");
        // Modified for  COEUSQA-3535 : CoeusLite IACUC "Double Amendment" Issue - End
//        String newProtocolNumber = EMPTY_STRING;
        HttpSession session = request.getSession();
//        boolean isSuccess = false;
        WebTxnBean txnBean = new WebTxnBean();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String unitNumber = userInfoBean.getUnitNumber();
        String loggedUser = userInfoBean.getUserId();
        UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
        boolean isAuthorised = false;
        String noRightToCreate = "hasRights";
        String pendingARs = "noPending";
        String navigator = EMPTY_STRING;
        request.setAttribute("haveNoCreationRights",noRightToCreate);
        if(NEW_AMENDMENT.equals(amendRenew)){
            isAuthorised = txnData.getUserHasIACUCProtocolRight(loggedUser, CoeusLiteConstants.ADD_AMENDMENT, protocolNumber);
            // Protocol level rights check
            if(!isAuthorised){
                isAuthorised = txnData.getUserHasRight(loggedUser, CoeusLiteConstants.CREATE_ANY_IACUC_AMENDMENT, unitNumber);
                //Unit level right
            }
        }
        //if(amendRenew.equals("NR")){
        //Code modified for the case#4330-Use consistent name for Renewal with Amendment  - start
        else if(NEW_RENEWAL.equals(amendRenew)){
        //Code modified for the case#4330-Use consistent name for Renewal with Amendment  - end
            isAuthorised = txnData.getUserHasIACUCProtocolRight(loggedUser, CoeusLiteConstants.ADD_RENEWAL, protocolNumber);
            // Protocol level rights check
            if(!isAuthorised){
                isAuthorised = txnData.getUserHasRight(loggedUser, CoeusLiteConstants.CREATE_ANY_IACUC_RENEWAL, unitNumber);
                //Unit level right
            }
        }
        /*COEUSQA-1724-Condition Added to Check Right for New Amendment type - Start*/
        else if(NEW_RENEWAL_AMENDMENT.equals(amendRenew)){
            isAuthorised = txnData.getUserHasIACUCProtocolRight(loggedUser, CoeusLiteConstants.CREATE_IACUC_RENEWAL_AMENDMENT, protocolNumber);
            // Protocol level rights check
            if(!isAuthorised){
                isAuthorised = txnData.getUserHasRight(loggedUser, CoeusLiteConstants.CREATE_ANY_IACUC_REN_AMEN, unitNumber);
            //Unit level right
            }
        }else if(NEW_CONTINUATION_REVIEW.equals(amendRenew)){
            isAuthorised = txnData.getUserHasIACUCProtocolRight(loggedUser, CoeusLiteConstants.CREATE_IACUC_CONT_REVIEW, protocolNumber);
            // Protocol level rights check
            if(!isAuthorised){
                isAuthorised = txnData.getUserHasRight(loggedUser, CoeusLiteConstants.CREATE_ANY_IACUC_CONT_REVIEW, unitNumber);
            //Unit level right
            }
        }else if(NEW_CONTINUATION_REVIEW_AMEND.equals(amendRenew)){
            isAuthorised = txnData.getUserHasIACUCProtocolRight(loggedUser, CoeusLiteConstants.CREATE_IACUC_CONT_REV_AM, protocolNumber);
            // Protocol level rights check
            if(!isAuthorised){
                isAuthorised = txnData.getUserHasRight(loggedUser, CoeusLiteConstants.CREATE_ANY_IACUC_CONT_REV_AM, unitNumber);
           //Unit level right
            }
        }
         /*COEUSQA-1724-Condition Added to Check Right for New Amendment type - End*/
        //Added for Case#4369 -  PI to create amendment/renewal  - start
        //Check's if user doesn't have any right to create new Ammendment,
        //If he is th PI for the protocol,allows user to create new Ammendment/Renewal
        if(!isAuthorised){
            ProtocolAuthorizationBean protocolAuthorizationBean = new ProtocolAuthorizationBean();
            isAuthorised = protocolAuthorizationBean.hasCreateAmenRenewRights(userInfoBean.getUserId(),protocolNumber);
        }
        //Case#4369 - End

        //If Authorised check whether he can perform amendment/renewal action
        if(isAuthorised){
            HashMap hmProto = new HashMap();
            hmProto.put("protocolNumber",protocolNumber);
            hmProto.put("actionCode", "N");
            // getting the count of Amendments/Renewals "in Pending" for the protcol
            Hashtable htIsPendingAR = (Hashtable)txnBean.getResults(request,"getIacucPendingAmendRenew",hmProto);
            HashMap hmIsPendingAR = (HashMap)htIsPendingAR.get("getIacucPendingAmendRenew");
            int numOfPendingAR = Integer.parseInt(hmIsPendingAR.get("ll_count").toString());
            //if there are one or more than one Amendment/Renewal "in Pending"
            //a message is raised in the general info page
            LockBean lockBean = null;
            if(NEW_AMENDMENT.equals(amendRenew)){
                session.setAttribute("summary","Amendment");
                lockBean = getLockingBean(userInfoBean, protocolNumber+CoeusConstants.IACUC_AMENDMENT, request);
            }
            //else if(amendRenew.equals("NR")){
            //Code modified for the case#4330-Use consistent name for Renewal with Amendment  - start
            else if(NEW_RENEWAL.equals(amendRenew)){
            //Code modified for the case#4330-Use consistent name for Renewal with Amendment  -end
                session.setAttribute("summary","Renewal");
                lockBean = getLockingBean(userInfoBean, protocolNumber+CoeusConstants.IACUC_RENEWAL, request);
            }
            else if(NEW_RENEWAL_AMENDMENT.equals(amendRenew)){
            //Code modified for the case#4330-Use consistent name for Renewal with Amendment  -end
                session.setAttribute("summary","Renewal/Amendment");
                lockBean = getLockingBean(userInfoBean, protocolNumber+CoeusConstants.IACUC_RENEWAL_WITH_AMENDMENT, request);
            }
            /*COEUSQA-1724-Condition Added for IACUC New Amendment Type - Start*/
            else if(NEW_CONTINUATION_REVIEW.equals(amendRenew)){
                session.setAttribute("summary","Continuation/Continuing Review");
                lockBean = getLockingBean(userInfoBean, protocolNumber+CoeusConstants.IACUC_CONTINUATION_CONTINUING_REVIEW, request);
            }else if(NEW_CONTINUATION_REVIEW_AMEND.equals(amendRenew)){
                session.setAttribute("summary","Continuation/Continuing Review with Amendment");
                lockBean = getLockingBean(userInfoBean, protocolNumber+CoeusConstants.IACUC_CONTI_CONTINUING_REVIEW_WITH_AMEND, request);
            }
            /*COEUSQA-1724-Condition Added for IACUC New Amendment Type - End*/
            lockBean.setMode("M");
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            LockBean lockData = getLockedData(CoeusLiteConstants.IACUC_PROTO_LOCK_STR+lockBean.getModuleNumber(),request);
            if(!isLockExists) {
                String lockId = "";
                if(NEW_AMENDMENT.equals(amendRenew)){
                    lockId = CoeusLiteConstants.IACUC_PROTO_LOCK_STR+protocolNumber+CoeusConstants.IACUC_AMENDMENT;
                }
                //else if(amendRenew.equals("NR")){
                //Code modified for the case#4330-Use consistent name for Renewal with Amendment  - start
                else if(NEW_RENEWAL.equals(amendRenew)){
                //Code modified for the case#4330-Use consistent name for Renewal with Amendment  - end
                    lockId = CoeusLiteConstants.IACUC_PROTO_LOCK_STR+protocolNumber+CoeusConstants.IACUC_RENEWAL;
                }
                else if(NEW_RENEWAL_AMENDMENT.equals(amendRenew)){
                //Code modified for the case#4330-Use consistent name for Renewal with Amendment  - end
                    lockId = CoeusLiteConstants.IACUC_PROTO_LOCK_STR+protocolNumber+CoeusConstants.IACUC_RENEWAL_WITH_AMENDMENT;
                }
                 /*COEUSQA-1724-Condition Added for IACUC New Amendment Type - Start*/
                else if(NEW_CONTINUATION_REVIEW.equals(amendRenew)){
                    lockId = CoeusLiteConstants.IACUC_PROTO_LOCK_STR+protocolNumber+CoeusConstants.IACUC_CONTINUATION_CONTINUING_REVIEW;
                }else if(NEW_CONTINUATION_REVIEW_AMEND.equals(amendRenew)){
                    lockId = CoeusLiteConstants.IACUC_PROTO_LOCK_STR+protocolNumber+CoeusConstants.IACUC_CONTI_CONTINUING_REVIEW_WITH_AMEND;
                }
                 /*COEUSQA-1724-Condition Added for IACUC New Amendment Type - End*/
                LockBean serverLockedBean = getLockedData(lockId,request);
                if(serverLockedBean!= null){
                    serverLockedBean.setModuleKey(CoeusLiteConstants.PROTOCOL_MODULE);
                    if(NEW_AMENDMENT.equals(amendRenew)){
                        serverLockedBean.setModuleNumber(protocolNumber+CoeusConstants.IACUC_AMENDMENT);
                        navigator = "createIacucAmendment";
                    }
                    else if(NEW_RENEWAL.equals(amendRenew)){
                        serverLockedBean.setModuleNumber(protocolNumber+CoeusConstants.IACUC_RENEWAL);
                        navigator = "createIacucRenewal";
                    }
                    //COEUSQA-1724-Code modified for the case#4330-Use consistent name for Renewal with Amendment  - start
                    else if(NEW_RENEWAL_AMENDMENT.equals(amendRenew)){
                        serverLockedBean.setModuleNumber(protocolNumber+CoeusConstants.IACUC_RENEWAL_WITH_AMENDMENT);
                        navigator = "createIacucRenewalWithAmendment";
                    }
                    /*COEUSQA-1724-Condition Added for IACUC New Amendment Type - Start*/
                    else if(NEW_CONTINUATION_REVIEW.equals(amendRenew)){
                        serverLockedBean.setModuleNumber(protocolNumber+CoeusConstants.IACUC_CONTINUATION_CONTINUING_REVIEW);
                        navigator = "createIacucContinuationContinuingReview";
                    }else if(NEW_CONTINUATION_REVIEW_AMEND.equals(amendRenew)){
                        serverLockedBean.setModuleNumber(protocolNumber+CoeusConstants.IACUC_CONTI_CONTINUING_REVIEW_WITH_AMEND);
                        navigator = "createIacucContinuationContinuingRevAmend";
                    }
                    /*COEUSQA-1724-Condition Added for IACUC New Amendment Type - End*/
                    //COEUSQA-1724-Code modified for the case#4330-Use consistent name for Renewal with Amendment  - end
                    String lockUserId = serverLockedBean.getUserId();
                    //UserMaintDataTxnBean userTxnBean = new UserMaintDataTxnBean();
                    //String lockUserName = userTxnBean.getUserName(lockUserId);
                    //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-start
                    String lockUserName = EMPTY_STRING;
                    lockUserName =  viewRestrictionOfUser(loggedUser,lockUserId);
                    //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-end
                    String acqLock = "acquired_lock";
                    ActionMessages messages = new ActionMessages();
                    messages.add("acqLock", new ActionMessage(acqLock,lockUserName,serverLockedBean.getModuleKey(),serverLockedBean.getModuleNumber()));
                    saveMessages(request, messages);
                    session.setAttribute("iacucNewAmendRenew"," ");
                    // Added for  COEUSQA-3535 : CoeusLite IACUC "Double Amendment" Issue - Start
                    session.setAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId(),protocolNumber);
                    // Added for  COEUSQA-3535 : CoeusLite IACUC "Double Amendment" Issue - End
                    return mapping.findForward(navigator);
                }
            }
            if(numOfPendingAR >= 1){
                pendingARs = "morePending";
                request.setAttribute("manyPendingAmendRenews",pendingARs);
                request.setAttribute("amendRenewalProto",amendRenew);
                String url =  "/getIacucData.do?PAGE=G&protocolNumber=";
                url += protocolNumber;
                url +="&sequenceNumber=";
                url += sequenceNumber;
                RequestDispatcher rd = request.getRequestDispatcher(url);
                // Added for  COEUSQA-3535 : CoeusLite IACUC "Double Amendment" Issue - Start
                session.setAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId(),protocolNumber);
                // Added for  COEUSQA-3535 : CoeusLite IACUC "Double Amendment" Issue - End
                rd.forward(request,response);
                return null;
            }else{
                // if no Amendments/ Renewals "in Pending" then a new Amendment/ Renewal is created
                if(NEW_AMENDMENT.equals(amendRenew)){
                    navigator = "createIacucAmendment";
                } else if(NEW_RENEWAL.equals(amendRenew)){
                    navigator = "createIacucRenewal";
                }
                //Code modified for the case#4330-Use consistent name for Renewal with Amendment  - start
                else if(NEW_RENEWAL_AMENDMENT.equals(amendRenew)){
                    navigator = "createIacucRenewalWithAmendment";
                }
                //Code modified for the case#4330-Use consistent name for Renewal with Amendment  - end
                /*COEUSQA-1724-Condition Added for IACUC New Amendment Type - Start*/
                else if(NEW_CONTINUATION_REVIEW.equals(amendRenew)){
                   navigator = "createIacucContinuationContinuingReview";
                }else if(NEW_CONTINUATION_REVIEW_AMEND.equals(amendRenew)){
                    navigator = "createIacucContinuationContinuingRevAmend";
                }
              /*COEUSQA-1724-Condition Added for IACUC New Amendment Type - End*/
            }
        }else{
            // if not a authorized user, then a message is raised in the General info page
            noRightToCreate = "noRights";
            request.setAttribute("manyPendingAmendRenews",pendingARs);
            request.setAttribute("amendRenewalProto",amendRenew);
            request.setAttribute("haveNoCreationRights",noRightToCreate);
            String url =  "/getIacucData.do?PAGE=G&protocolNumber=";
            url += protocolNumber;
            url +="&sequenceNumber=";
            url += sequenceNumber;
            RequestDispatcher rd = request.getRequestDispatcher(url);
            // Added for  COEUSQA-3535 : CoeusLite IACUC "Double Amendment" Issue - Start
            session.setAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId(),protocolNumber);
            // Added for  COEUSQA-3535 : CoeusLite IACUC "Double Amendment" Issue - End
            rd.forward(request,response);
            return null;
        }
        // Added for  COEUSQA-3535 : CoeusLite IACUC "Double Amendment" Issue - Start
        session.setAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId(),protocolNumber);
        // Added for  COEUSQA-3535 : CoeusLite IACUC "Double Amendment" Issue - End
        return mapping.findForward(navigator);
    }

    /**
     * Coeus4.3 subject count enhancement
     * This method gets the PROTOCOL_SUBJECT_COUNT_DISPLAY_ENABLED value from the Paramater table
     * @throws Exception
     * @return String subjectCountCode
     */
    //    private String getSubjectCountCode(HttpServletRequest request)throws Exception{
    //        HashMap hmSubjectCount = new HashMap();
    //        hmSubjectCount.put("parameterName", "PROTOCOL_SUBJECT_COUNT_DISPLAY_ENABLED");
    //        WebTxnBean webTxnBean = new WebTxnBean();
    //        Hashtable htSubjectCount =
    //            (Hashtable)webTxnBean.getResults(request, "getParameterValue", hmSubjectCount);
    //        hmSubjectCount = (HashMap)htSubjectCount.get("getParameterValue");
    //        String subjectCountCode = hmSubjectCount.get("parameterValue").toString();
    //        return subjectCountCode;
    //    }

    //Added for case#3057 - red asterisks on left side menu labels for mandatory custom fields - start
    /**
     * This method checks whether the Others tab contains any mandatory fields
     * @param request HttpServletRequest
     * @exception Exception
     * @return othersMandatory boolean
     */
    private boolean othersHasMandatory(HttpServletRequest request) throws Exception{
        boolean othersMandatory = false;
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap inputData = new HashMap();
        HttpSession session = request.getSession();
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());

        //Get the existing groups associated with the module
        inputData.put("moduleId", ModuleConstants.IACUC_MODULE_CODE+"");
        Hashtable htGroupData = (Hashtable) webTxnBean.getResults(request, "getCustColsForProtocolModule", inputData);
        Vector vecGroupData = (Vector)htGroupData.get("getCustColsForProtocolModule");

        //Get the previous groups associated with the module which had data
        //but not associated with the module currently
        inputData.clear();
        inputData.put("protocolNumber", protocolNumber);
        inputData.put("sequenceNumber", new Integer(sequenceNumber));
        inputData.put("moduleId", ModuleConstants.IACUC_MODULE_CODE+"");
        Hashtable htGroupData2 = (Hashtable)webTxnBean.getResults(request, "getIacucCustomData", inputData);
        Vector vecGroupData2 = (Vector)htGroupData2.get("getIacucCustomData");

        //Merge both
        if(vecGroupData2 != null && vecGroupData2.size() > 0){
            if(vecGroupData == null){
                vecGroupData = new Vector();
            }
            vecGroupData.addAll(vecGroupData2);
        }
        //Case#4494 - In Protocol, Error on Other tab when custom elements are not defined  - Start
        //isCustomElementsPresent session attribute value is set to false,if there is no custom elements
        if(vecGroupData == null || vecGroupData.size()<1){
            session.setAttribute("isCustomElementsPresent",new Boolean(false));
        }else{
            session.setAttribute("isCustomElementsPresent",new Boolean(true));
        }
        //Case#4494 - End
        if(vecGroupData != null && vecGroupData.size() > 0) {
            for(int index = 0; index < vecGroupData.size(); index++){
                DynaActionForm form = (DynaActionForm)vecGroupData.get(index);
                String isRequired = (String)form.get("isRequired");
                if(isRequired.equals("Y")){
                    othersMandatory = true;
                    break;
                }
            }
        }
        return othersMandatory;
    }
    //Added for case#3057 - red asterisks on left side menu labels for mandatory custom fields - end

    //Added for Case#4275 - upload attachments until in agenda - Start
    /**
     * This method checks whether attachment can be editable
     * @param request HttpServletRequest
     * @exception Exception
     * @return canEditAttachment - return true, if user can modify attachment
     */
    protected boolean checkAttachmentIsEditable(HttpServletRequest request,String protocolNumber) throws Exception{
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String loggedinUser = userInfoBean.getUserId().toUpperCase();
        boolean isAmendRenewal = false;
        boolean amendRenewalAttach = false;

        //Checks Protocol/Amendment/Renewal
        if(protocolNumber.length() > 10 && isIacucProtoAmendmentRenewal(protocolNumber.charAt(10))){
            HashMap hmProtoAmendRenewModules = (HashMap)session.getAttribute(AMEND_RENEWAL_MODULES+session.getId());
            //Added for COEUSQA-3458 : IACUC Protocol Reviewer Attachment Error Recieved - start
            if(hmProtoAmendRenewModules != null){
                String amendRenewalNumber = (String)hmProtoAmendRenewModules.get(UPLOAD_ATTACHMENTS);
                isAmendRenewal = true;
                if(amendRenewalNumber!=null && amendRenewalNumber.equals(protocolNumber)){
                    amendRenewalAttach = true;
                }
            }
            //Added for COEUSQA-3458 : IACUC Protocol Reviewer Attachment Error Recieved - end
        }

        ProtocolHeaderDetailsBean headerBean = (ProtocolHeaderDetailsBean)session.getAttribute(IACUC_HEADER_BEAN);
        String leadUnitNumber = headerBean.getUnitNumber();
        boolean canEditAttachment = false;
        if(headerBean.getProtocolStatusCode() == SUBMITTED_TO_IACUC){
            ProtocolAuthorizationBean protocolAuthorizationBean = new ProtocolAuthorizationBean();
            if(isAmendRenewal){
                //If Add/Modify Attachment is checked, then check for the modify attachment rights
                if(amendRenewalAttach){
                    canEditAttachment = protocolAuthorizationBean.hasModifyAttachmentRights(loggedinUser,protocolNumber,leadUnitNumber);
                }
            }else{
                canEditAttachment = protocolAuthorizationBean.hasModifyAttachmentRights(loggedinUser,protocolNumber,leadUnitNumber);

            }
        }

        return canEditAttachment;
    }
    //Case#4275 - end

}
