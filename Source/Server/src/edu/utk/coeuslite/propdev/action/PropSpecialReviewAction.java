/*
 * PropSpecialReviewAction.java
 *
 * Created on August 9, 2006
 *
 */
 
/* PMD check performed, and commented unused imports and variables on 13-JULY-2011
 * by Bharati 
 */

package edu.utk.coeuslite.propdev.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.utils.TypeConstants;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;
//import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpSession;
import java.util.Vector;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
//import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.mit.coeuslite.utils.DateUtils;
import edu.mit.coeuslite.utils.LockBean;
//import edu.mit.coeuslite.utils.WebUtilities;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionMessage;
import edu.mit.coeus.irb.bean.ProtocolReviewTypeCheckListBean;
import edu.mit.coeus.irb.bean.ProtocolSubmissionInfoBean;
import edu.mit.coeus.irb.bean.SubmissionDetailsTxnBean;
import edu.mit.coeus.utils.ModuleConstants;

/**
 *
 * @author  UTK
 */
public class PropSpecialReviewAction extends ProposalBaseAction {
    public static final String EMPTY_STRING = "";
    //Removing instance variable case# 2960
//    private DateUtils dateUtils = new edu.mit.coeuslite.utils.DateUtils();
    
    private static final String APPLICATION_DATE="applicationDate";
    private static final String APPROVAL_DATE="approvalDate";
    private static final String SPECIAL_REVIEW_CODE="specialReviewCode";
    private static final String AC_TYPE="acType";
    private static final String AC_TYPE_INSERT="I";
    private static final String AC_TYPE_DELETE="D";
    private static final String PROPOSAL_NUMBER="proposalNumber";
    private static final String GET_PROPOSAL_SPECIAL_REVIEW="getProposalSpecialReview";
    private static final String UPDATE_TIMESTAMP="pdSpTimestamp";
    private static final String GET_SPECIAL_REVIEW_CODE="getSpeciaReviewCode";
    private static final String GET_REVIEW_APPROVAL_TYPE="getReviewApprovalType";
    private static final String SPECIAL_REVIEW_NUMBER="specialReviewNumber";
    private static final String GET_SPECIAL_REVIEW_NMUBER="getNextProposalSpecialReviewNumber";
    private static final String UPDATE_PROPDEVMENU_CHECKLIST="updatePropdevMenuCheckList";
    private static final String UPDATE_SPECIAL_REVIEW="updateProposalSpecialReview";
    
    private static final String SPECIAL_REVIEW_MENU_CODE = "P005";
    
    // the column name in the OSP$PROPDEV_WEB_MENU_CHKLST table for Special Review (Research Subjects)
    private static final String INV_MENU_FIELD_NAME = "RESEARCH_SUBJECTS";
    
    //Added for case#2990 - Proposal Special Review Enhancement
    private static final String APPROVAL_CODE = "approvalCode";    
    
    //Added for Case#4354 - When ENABLE_PROTOCOL_TO_DEV_PROPOSAL is set to ON - Start
    private static final String EMPTY_SPACE = " ";
    //Case#4354 - End
    
    /** Creates a new instance of PropSpecialReviewAction */
    public PropSpecialReviewAction() {
    }
    
    public org.apache.struts.action.ActionForward performExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse res) throws Exception{
        String proposalNumber=EMPTY_STRING;
        DynaValidatorForm specialReviewForm = (DynaValidatorForm)form;
        HttpSession session = request.getSession();
        
        WebTxnBean txnBean = new WebTxnBean();
        boolean isAllDeleted = false;
        boolean isMenuSaved = false;
        //Added for case#2990 - Proposal Special Review Enhancement
        boolean isSuccess=false;
        
        HashMap hmpSpRev = new HashMap();
        proposalNumber = (String) session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId());
        
        hmpSpRev.put("proposalNumber",proposalNumber);
        // COEUSQA-2320 Show in Lite for Special Review in Code table
        // Add module code to the map.
        hmpSpRev.put("moduleCode", ModuleConstants.PROPOSAL_DEV_MODULE_CODE);
        
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String unitNumber=userInfoBean.getUnitNumber();
        String loggedinUser=userInfoBean.getUserId();
        String userName=userInfoBean.getUserName();
        //Added for Case#4354 - When ENABLE_PROTOCOL_TO_DEV_PROPOSAL is set to ON  - Start
        //If ExemptCheckList request parameter reutns true get the exempt protocol check list details
        String exemptCheckList = request.getParameter("ExemptCheckList");
        String protocolNumber = request.getParameter("protocolNumber");
        if(exemptCheckList != null && exemptCheckList.equals("true")){
            SubmissionDetailsTxnBean  submissionDetailsTxnBean = new SubmissionDetailsTxnBean();
            ProtocolSubmissionInfoBean protocolSubmissionInfoBean = submissionDetailsTxnBean.getProtocolSubmissionDetails(protocolNumber);
            if(protocolSubmissionInfoBean != null && protocolSubmissionInfoBean.getProtocolExemptCheckList() != null
                    &&protocolSubmissionInfoBean.getProtocolExemptCheckList().size() > 0){
                Vector vcExemptCheclList = protocolSubmissionInfoBean.getProtocolExemptCheckList();
                StringBuffer checkListForComments = new StringBuffer();
                //Append's the checklist code with 'E' before to display in the special review comments field
                if(vcExemptCheclList != null && vcExemptCheclList.size() > 0){
                    for(int index = 0; index<vcExemptCheclList.size();index++){
                        ProtocolReviewTypeCheckListBean protoExemptCheckListBean =  (ProtocolReviewTypeCheckListBean)vcExemptCheclList.get(index);
                        //Appends 'E' to start of the check list code
                        checkListForComments.append("E");
                        checkListForComments.append(protoExemptCheckListBean.getCheckListCode());
                        if(index != vcExemptCheclList.size()-1){
                            checkListForComments.append(","+EMPTY_SPACE);
                        }
                    }
                    request.setAttribute("comments",checkListForComments.toString());
                }
            }
         
            return mapping.findForward("success");
        }
        //Case#4354 - End

        //Added for case#2990 - Proposal Special Review Enhancement - start
        String enableProtocolToDevPropLink = (String)session.getAttribute("enableProtocolToDevPropLink");
        if(specialReviewForm.get(APPROVAL_CODE) != null && specialReviewForm.get(APPROVAL_CODE).toString().equals("")){
            specialReviewForm.set(APPROVAL_CODE, specialReviewForm.get("tempApprovalCode"));            
        }
        //Added for case#2990 - Proposal Special Review Enhancement - end
        
        //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
        //get the value for the ENABLE_IACUC_TO_DEV_PROPOSAL_LINK parameter
        String enableIacucProtocolToDevPropLink = (String)session.getAttribute("enableIacucProtocolToDevPropLink");
        if(specialReviewForm.get(APPROVAL_CODE) != null && specialReviewForm.get(APPROVAL_CODE).toString().equals("")){
            specialReviewForm.set(APPROVAL_CODE, specialReviewForm.get("tempApprovalCode"));            
        }
        //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
        
        if(specialReviewForm.get(AC_TYPE)!=null){
            // Check if lock exists or not
            LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(PROPOSAL_NUMBER+session.getId()), request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            LockBean lockData = getLockedData(CoeusLiteConstants.PROP_DEV_LOCK_STR+lockBean.getModuleNumber(), request);
            if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
                //Modified for COEUSDEV-1144 : Unable to edit Special review comments RT #2022030 - Start
                // Modified to check for 'U' acType
                if(specialReviewForm.get(AC_TYPE).toString().equalsIgnoreCase(AC_TYPE_INSERT) ||
                        TypeConstants.UPDATE_RECORD.equals(specialReviewForm.get(AC_TYPE).toString())){
                    //Added/Modified for case#2990 - Proposal Special Review Enhancement - start
                    boolean isApprovalPresent = true;
                    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                    boolean isApprovalPresentForAnimal = true;
                    CoeusFunctions coeusFunctions = new CoeusFunctions();
                    boolean verifyIRBApprovalCode = false;
                    boolean verifyIACUCApprovalCode = false;
                    boolean canSaveReview =false;
                    enableProtocolToDevPropLink = coeusFunctions.getParameterValue("ENABLE_PROTOCOL_TO_DEV_PROPOSAL_LINK");
                    enableIacucProtocolToDevPropLink = coeusFunctions.getParameterValue("ENABLE_IACUC_TO_DEV_PROPOSAL_LINK");
                            
                    if("1".equals(enableProtocolToDevPropLink)){
                        boolean isHumanSubjects = false;
                        if(!specialReviewForm.get(SPECIAL_REVIEW_CODE).toString().equals("1")){
                            String irbSplRevCode = coeusFunctions.getParameterValue("SPL_REV_TYPE_CODE_HUMAN");
                            if(irbSplRevCode != null && !"".equals(irbSplRevCode)){
                                if(!irbSplRevCode.equals(specialReviewForm.get("specialReviewCode").toString())){
                                    verifyIRBApprovalCode = true;
                                }
                            }
                        }
                         
                    }else{
                       verifyIRBApprovalCode = true;
                    }
                    //If link is eanbled between IACUC and dev proposal then check for the approval code
                    if("1".equals(enableIacucProtocolToDevPropLink)){
                        boolean isAnimalCare = false;
                        String iacucSplRevCode = coeusFunctions.getParameterValue("IACUC_SPL_REV_TYPE_CODE");
                        if(iacucSplRevCode != null && !"".equals(iacucSplRevCode)){
                            if(!iacucSplRevCode.equals(specialReviewForm.get("specialReviewCode").toString())){
                                verifyIACUCApprovalCode = true;
                            }
                        }
                    } else{
                        verifyIACUCApprovalCode = true;
                        
                    }
                    
                    if(verifyIRBApprovalCode && verifyIACUCApprovalCode){
                        canSaveReview = validateApproval(request, (String)specialReviewForm.get(APPROVAL_CODE));
                    }else{
                        canSaveReview = true;
                    }
                            
                    //If approval code exist for the inserted special review then save the special review
                    if(canSaveReview){                        
                        isSuccess=saveSpecialReview(session, request, specialReviewForm, hmpSpRev, proposalNumber);
                        if(!isSuccess){
                            return mapping.findForward("success");
                        }
                    }else{
                        return mapping.findForward("success");
                    }
                    //Added/Modified for case#2990 - Proposal Special Review Enhancement - end
                    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
                }else if(specialReviewForm.get(AC_TYPE).toString().equalsIgnoreCase(AC_TYPE_DELETE)){                    
                    isSuccess=deleteSpecialReview(session, request, specialReviewForm, hmpSpRev, proposalNumber);
                } 
            }else{
                String errMsg = "release_lock_for";
                ActionMessages messages = new ActionMessages();
                messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                saveMessages(request, messages);
            }
        }
        
  
   
        
        specialReviewForm.reset(mapping,request);
        //Added/Modified for case#2990 - Proposal Special Review Enhancement - start
        if(isSuccess){
            specialReviewForm.set("specialReviewCode", EMPTY_STRING);
            specialReviewForm.set("approvalCode", EMPTY_STRING);
            specialReviewForm.set("spRevProtocolNumber", EMPTY_STRING);
            specialReviewForm.set("applicationDate", EMPTY_STRING);
            specialReviewForm.set("approvalDate", EMPTY_STRING);
            specialReviewForm.set("comments", EMPTY_STRING);            
            specialReviewForm.set("tempApprovalCode", EMPTY_STRING);
        }
        //Added/Modified for case#2990 - Proposal Special Review Enhancement - end
        readSavedStatus(request);
        return mapping.findForward("success");
    }
    
    /*This method saves the Special Review
     */
    private boolean saveSpecialReview(HttpSession session,HttpServletRequest request,
            DynaValidatorForm specialReviewForm,
            HashMap hmpSpRev,
            String proposalNumber)throws Exception{
        boolean isAllDeleted = false;
        boolean isMenuSaved = false;
        boolean isSuccess=false;
        WebTxnBean txnBean = new WebTxnBean();
        //Modified for instance variable case#2960.
        DateUtils dateUtils = new DateUtils();
        String splRevCode = (String)specialReviewForm.get(SPECIAL_REVIEW_CODE);
        specialReviewForm.set(PROPOSAL_NUMBER,proposalNumber);
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        specialReviewForm.set(UPDATE_TIMESTAMP,dbTimestamp.toString());
        
        //Added for case#2990 - Proposal Special Review Enhancement - start
        String date=specialReviewForm.get("applicationDate").toString();
        date = dateUtils.formatDate(date, ":/.,|-", "MM/dd/yyyy");
        specialReviewForm.set("applicationDate", date);
        date=specialReviewForm.get("approvalDate").toString();
        date = dateUtils.formatDate(date, ":/.,|-", "MM/dd/yyyy");
        specialReviewForm.set("approvalDate", date);
        String protocolNumber = ((String)specialReviewForm.get("spRevProtocolNumber")).trim();
        specialReviewForm.set("spRevProtocolNumber", protocolNumber);
        //Added for case#2990 - Proposal Special Review Enhancement - end

        // Check for the Duplicate Special REview code - start
        Hashtable htSplData = (Hashtable)txnBean.getResults(request, GET_PROPOSAL_SPECIAL_REVIEW, hmpSpRev);
        Vector vecSplRev=(Vector)htSplData.get(GET_PROPOSAL_SPECIAL_REVIEW);
        /**   if(vecSplRev!= null && vecSplRev.size() > 0){
           boolean noDup = checkDuplicateSpecialReview(vecSplRev,request,splRevCode);
           if(!noDup){
               if(vecSplRev!= null && vecSplRev.size() >0){
                   for(int i=0;i<vecSplRev.size();i++){
                       DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)vecSplRev.get(i);
                       String applDate = (String)dynaValidatorForm.get(APPLICATION_DATE);
                       String approvalDate = (String)dynaValidatorForm.get(APPROVAL_DATE);
                       if(applDate != null){
                           String value = dateUtils.formatDate(applDate,edu.mit.coeuslite.utils.DateUtils.MM_DD_YYYY);
                           dynaValidatorForm.set(APPLICATION_DATE,value);
      
                       }else{
                           dynaValidatorForm.set(APPLICATION_DATE,"");
                       }
                       if(approvalDate != null){
                           String dateValue = dateUtils.formatDate(approvalDate,edu.mit.coeuslite.utils.DateUtils.MM_DD_YYYY);
                           dynaValidatorForm.set(APPROVAL_DATE,dateValue);
      
                       }else{
                           dynaValidatorForm.set(APPROVAL_DATE,"");
                       }
                   }
               }
               request.setAttribute("pdReviewList",vecSplRev);
               isSuccess=true;
               return isSuccess;
           }
        }*/// check for the Special REview - End
        
        // if no duplication of the special review then insert it
        Hashtable htSplValidate = (Hashtable)txnBean.getResults(request, "getSpecialReviewValidData", specialReviewForm);
        HashMap hmSplValidate = (HashMap)htSplValidate.get("getSpecialReviewValidData");
        if(hmSplValidate!=null){
            ActionMessages messages = new ActionMessages();
            if(hmSplValidate.get("as_protocol_number_flag")!=null && hmSplValidate.get("as_protocol_number_flag").equals("Y")){
                if(specialReviewForm.get("spRevProtocolNumber")==null ||
                        specialReviewForm.get("spRevProtocolNumber").toString().trim().equals(EMPTY_STRING)){
                    messages.add("error.specialreview", new ActionMessage("error.specialreview", "Protocol Number"));
                    saveMessages(request, messages);
                    isSuccess = true;
                }
            }
            if(hmSplValidate.get("as_application_date_flag")!=null && hmSplValidate.get("as_application_date_flag").equals("Y")){
                if(specialReviewForm.get("applicationDate")==null ||
                        specialReviewForm.get("applicationDate").toString().trim().equals(EMPTY_STRING)){
                    messages.add("error.specialreview", new ActionMessage("error.specialreview", "Application Date"));
                    saveMessages(request, messages);
                    isSuccess = true;
                }
            }
            if(hmSplValidate.get("as_approval_date_flag")!=null && hmSplValidate.get("as_approval_date_flag").equals("Y")){
                if(specialReviewForm.get("approvalDate")==null ||
                        specialReviewForm.get("approvalDate").toString().trim().equals(EMPTY_STRING)){
                    messages.add("error.specialreview", new ActionMessage("error.specialreview", "Approval Date"));
                    saveMessages(request, messages);
                    isSuccess = true;
                }
            }
            if(isSuccess){
                return isSuccess;
            }
        }
        
        //Added for case#2990 - Proposal Special Review Enhancement - start        
        // Modified for COEUSQA-3726 : Issue with IRB linking - while adding a special review in proposal dev application is incorrectly updating the approval type code - Start
//        String enableProtocolToDevPropLink = (String)session.getAttribute("enableProtocolToDevPropLink");
        String enableProtocolToDevPropLink = coeusFunctions.getParameterValue("ENABLE_PROTOCOL_TO_DEV_PROPOSAL_LINK");
        // Modified for COEUSQA-3726 : Issue with IRB linking - while adding a special review in proposal dev application is incorrectly updating the approval type code - End
        if(enableProtocolToDevPropLink != null && enableProtocolToDevPropLink.equals("1")){
            if(splRevCode.equals("1")){
                ActionMessages actionMessages = new ActionMessages();                             
                if(protocolNumber != null && !protocolNumber.equals("")){
                    if(isValidProtocolNumber(request, protocolNumber)){
                        if(!checkIfProtocolExists(request, protocolNumber, proposalNumber)){
                            HashMap hmProtocolDetails = getProtocolDetails(request, protocolNumber);
                            String applicationDate = (String)hmProtocolDetails.get(APPLICATION_DATE);
                            if(applicationDate != null && !applicationDate.equals("")){
                                applicationDate = dateUtils.formatDate(applicationDate, "MM/dd/yyyy");
                                specialReviewForm.set(APPLICATION_DATE, applicationDate);
                            }else{
                                specialReviewForm.set(APPLICATION_DATE, "");
                            }
                            String approvalDate = (String)hmProtocolDetails.get(APPROVAL_DATE);
                            if(approvalDate != null && !approvalDate.equals("")){
                                approvalDate = dateUtils.formatDate(approvalDate, "MM/dd/yyyy");                        
                                specialReviewForm.set(APPROVAL_DATE, approvalDate);
                            }else{
                                specialReviewForm.set(APPROVAL_DATE, "");
                            }
                            // Modified for COEUSQA-3726 : Issue with IRB linking - while adding a special review in proposal dev application is incorrectly updating the approval type code -Start
//                            specialReviewForm.set(APPROVAL_CODE, "2");
                            String linkedIrbCode = coeusFunctions.getParameterValue("LINKED_TO_IRB_CODE");
                            if(linkedIrbCode != null && !"".equals(linkedIrbCode)){
                                int irbLinkedCode = Integer.parseInt(linkedIrbCode);
                                specialReviewForm.set(APPROVAL_CODE, irbLinkedCode+"");
                            }
                            // Modified for COEUSQA-3726 : Issue with IRB linking - while adding a special review in proposal dev application is incorrectly updating the approval type code -End
                            
                        }else{
                            actionMessages.add("duplicateProtocol", new ActionMessage("error.specialReview.dupProtoNum"));
                            saveMessages(request, actionMessages);  
                            return isSuccess;                            
                        }
                    }else{
                        return isSuccess;
                    }
                }else{                    
                    actionMessages.add("protocolNumberRequired", new ActionMessage("error.specialReview.ProtocolNumber"));
                    saveMessages(request, actionMessages);  
                    return isSuccess;
                }
            }
        }else if(enableProtocolToDevPropLink != null && enableProtocolToDevPropLink.equals("0")){
            if(splRevCode.equals("1")){
                if(protocolNumber != null && !protocolNumber.equals("")){                    
                    HashMap hmProtocolDetails = getProtocolDetails(request, protocolNumber);
                    String applicationDate = (String)hmProtocolDetails.get(APPLICATION_DATE);
//                    if(applicationDate != null && !applicationDate.equals("")){
//                        applicationDate = dateUtils.formatDate(applicationDate, "MM/dd/yyyy");
//                        specialReviewForm.set(APPLICATION_DATE, applicationDate);
//                    }
                    String approvalDate = (String)hmProtocolDetails.get(APPROVAL_DATE);
//                    if(approvalDate != null && !approvalDate.equals("")){
//                        approvalDate = dateUtils.formatDate(approvalDate, "MM/dd/yyyy");                        
//                        specialReviewForm.set(APPROVAL_DATE, approvalDate);
//        }
        }
        }
        }
        //Added for case#2990 - Proposal Special Review Enhancement - end
        
        //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
        //if parameter ENABLE_IACUC_TO_DEV_PROPOSAL_LINK is enabled and special review type is Animal Usage
        //then check for the valid protocol and display the details of protocol
        // Modified for COEUSQA-3726 : Issue with IRB linking - while adding a special review in proposal dev application is incorrectly updating the approval type code - Start
//      String enableIacucProtocolToDevPropLink = (String)session.getAttribute("enableIacucProtocolToDevPropLink");
        String enableIacucProtocolToDevPropLink = coeusFunctions.getParameterValue("ENABLE_IACUC_TO_DEV_PROPOSAL_LINK");
        // Modified for COEUSQA-3726 : Issue with IRB linking - while adding a special review in proposal dev application is incorrectly updating the approval type code - End
        if(enableIacucProtocolToDevPropLink != null && enableIacucProtocolToDevPropLink.equals("1")){
            if(splRevCode.equals("2")){
                ActionMessages actionMessages = new ActionMessages();
                if(protocolNumber != null && !protocolNumber.equals("")){
                    if(isValidIacucProtocolNumber(request, protocolNumber)){
                        if(!checkIfProtocolExists(request, protocolNumber, proposalNumber)){
                            HashMap hmIacucProtoDetails = getIacucProtocolDetails(request, protocolNumber);
                            String applicationDate = (String)hmIacucProtoDetails.get(APPLICATION_DATE);
                            if(applicationDate != null && !applicationDate.equals("")){
                                applicationDate = dateUtils.formatDate(applicationDate, "MM/dd/yyyy");
                                specialReviewForm.set(APPLICATION_DATE, applicationDate);
                            }else{
                                specialReviewForm.set(APPLICATION_DATE, "");
                            }
                            String approvalDate = (String)hmIacucProtoDetails.get(APPROVAL_DATE);
                            if(approvalDate != null && !approvalDate.equals("")){
                                approvalDate = dateUtils.formatDate(approvalDate, "MM/dd/yyyy");
                                specialReviewForm.set(APPROVAL_DATE, approvalDate);
                            }else{
                                specialReviewForm.set(APPROVAL_DATE, "");
                            }
                            // Modified for COEUSQA-3726 : Issue with IRB linking - while adding a special review in proposal dev application is incorrectly updating the approval type code -Start
//                            specialReviewForm.set(APPROVAL_CODE, "2");
                            String linkedIacucCode = coeusFunctions.getParameterValue("LINKED_TO_IACUC_CODE");
                            if(linkedIacucCode != null && !"".equals(linkedIacucCode)){
                                int iacucLinkedCode = Integer.parseInt(linkedIacucCode);
                                specialReviewForm.set(APPROVAL_CODE, iacucLinkedCode+"");
                            }
                            // Modified for COEUSQA-3726 : Issue with IRB linking - while adding a special review in proposal dev application is incorrectly updating the approval type code -End                            
                        }else{
                            actionMessages.add("duplicateProtocol", new ActionMessage("error.specialReview.dupProtoNum"));
                            saveMessages(request, actionMessages);
                            return isSuccess;
                        }
                    }else{
                        return isSuccess;
                    }
                }else{
                    actionMessages.add("protocolNumberRequired", new ActionMessage("error.specialReview.ProtocolNumber"));
                    saveMessages(request, actionMessages);
                    return isSuccess;
                }
            }
            //if parameter ENABLE_IACUC_TO_DEV_PROPOSAL_LINK is disabled and special review of type is Animal Usage
            //then display the protocol details
        }else if(enableIacucProtocolToDevPropLink != null && enableIacucProtocolToDevPropLink.equals("0")){
            if(splRevCode.equals("2")){
                if(protocolNumber != null && !protocolNumber.equals("")){
                    HashMap hmIacucProtoDetails = getIacucProtocolDetails(request, protocolNumber);
                    String applicationDate = (String)hmIacucProtoDetails.get(APPLICATION_DATE);
//                    if(applicationDate != null && !applicationDate.equals("")){
//                        applicationDate = dateUtils.formatDate(applicationDate, "MM/dd/yyyy");
//                        specialReviewForm.set(APPLICATION_DATE, applicationDate);
//                    }
                    String approvalDate = (String)hmIacucProtoDetails.get(APPROVAL_DATE);
//                    if(approvalDate != null && !approvalDate.equals("")){
//                        approvalDate = dateUtils.formatDate(approvalDate, "MM/dd/yyyy");
//                        specialReviewForm.set(APPROVAL_DATE, approvalDate); 
//            }
        }
        }
        }
        //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
        //Modified for COEUSDEV-1144 : Unable to edit Special review comments RT #2022030 - Start
        // For updating the special review and insert
//        Hashtable spRevData = (Hashtable)txnBean.getResults(request, GET_SPECIAL_REVIEW_NMUBER, hmpSpRev);
//        HashMap hm = (HashMap)spRevData.get(GET_SPECIAL_REVIEW_NMUBER);
//        int spRevNumber = Integer.parseInt(hm.get("pdSpRevNumber").toString());
//        specialReviewForm.set(SPECIAL_REVIEW_NUMBER,new Integer(spRevNumber+1));
        String acType = specialReviewForm.get(AC_TYPE).toString();
        if(TypeConstants.UPDATE_RECORD.equalsIgnoreCase(acType)){
            UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute("user"+request.getSession().getId());
            specialReviewForm.set("updateUser",userInfoBean.getUserId());
            specialReviewForm.set("pdSpTimestamp",prepareTimeStamp().toString());
        }else{
            Hashtable spRevData = (Hashtable)txnBean.getResults(request, GET_SPECIAL_REVIEW_NMUBER, hmpSpRev);
            HashMap hm = (HashMap)spRevData.get(GET_SPECIAL_REVIEW_NMUBER);
            int spRevNumber = Integer.parseInt(hm.get("pdSpRevNumber").toString());
            specialReviewForm.set(SPECIAL_REVIEW_NUMBER,new Integer(spRevNumber+1));
        }
        txnBean.getResults(request, UPDATE_SPECIAL_REVIEW, specialReviewForm);
        specialReviewForm.set("pdSpTimestamp","");
        //Modified for COEUSDEV-1144 : Unable to edit Special review comments RT #2022030 - End
        // Update the proposal hierarchy sync flag
        updateProposalSyncFlags(request, proposalNumber);
        Hashtable hsSpRev = (Hashtable)txnBean.getResults(request, "getPropDevSpecialReview", hmpSpRev);
        // COEUSQA-2320 Show in Lite for Special Review in Code table - Start
        // Filter the special reivew entries based on show in lite flag.
        //Vector vecSpecialReviewDetails = (Vector) hsSpRev.get(GET_SPECIAL_REVIEW_CODE);
        Vector vecSpecialReviewDetails = (Vector) hsSpRev.get(GET_SPECIAL_REVIEW_CODE);
        Vector vecFilteredData = filterSpecialReview(vecSpecialReviewDetails);
        //session.setAttribute("splReview", hsSpRev.get(GET_SPECIAL_REVIEW_CODE));
        session.setAttribute("splReview", vecFilteredData);
        // COEUSQA-2320 Show in Lite for Special Review in Code table - End
        
        session.setAttribute("approval", hsSpRev.get(GET_REVIEW_APPROVAL_TYPE));
        Vector vecData = (Vector)hsSpRev.get(GET_PROPOSAL_SPECIAL_REVIEW);
        if(vecData!= null && vecData.size() >0){
            for(int i=0;i<vecData.size();i++){
                DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)vecData.get(i);
                String applDate = (String)dynaValidatorForm.get(APPLICATION_DATE);
                String approvalDate = (String)dynaValidatorForm.get(APPROVAL_DATE);
                if(applDate != null){
                    String value = dateUtils.formatDate(applDate,edu.mit.coeuslite.utils.DateUtils.MM_DD_YYYY);
                    dynaValidatorForm.set(APPLICATION_DATE,value);                    
                }else{
                    dynaValidatorForm.set(APPLICATION_DATE,"");
                }
                if(approvalDate != null){
                    String dateValue = dateUtils.formatDate(approvalDate,edu.mit.coeuslite.utils.DateUtils.MM_DD_YYYY);
                    dynaValidatorForm.set(APPROVAL_DATE,dateValue);                    
                }else{
                    dynaValidatorForm.set(APPROVAL_DATE,"");
                }
                //Added for case#2990 - Proposal Special Review Enhancement - start                                
                String specialReviewCode = (String)dynaValidatorForm.get("specialReviewCode");
                if(specialReviewCode.equals("1")){
                    String spRevProtocolNumber = (String)dynaValidatorForm.get("spRevProtocolNumber");
                    if(spRevProtocolNumber != null && !spRevProtocolNumber.equals("")){                        
                        HashMap hmProtocolDetails = getProtocolDetails(request, spRevProtocolNumber);
                        String protocolStatusDesc = (String)hmProtocolDetails.get("protocolStatusDesc");
//                        if(protocolStatusDesc != null && !protocolStatusDesc.equals("")){
//                            dynaValidatorForm.set("approvalDescription", protocolStatusDesc);
//                        }
                    }
                }                
                //Added for case#2990 - Proposal Special Review Enhancement - end   
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                //If special review is of type Animal Usage
                if(specialReviewCode.equals("2")){
                    String spRevProtocolNumber = (String)dynaValidatorForm.get("spRevProtocolNumber");
                    if(spRevProtocolNumber != null && !spRevProtocolNumber.equals("")){                        
                        HashMap hmIacucProtoDetails = getIacucProtocolDetails(request, spRevProtocolNumber);
                        String protocolStatusDesc = (String)hmIacucProtoDetails.get("protocolStatusDesc");
//                        if(protocolStatusDesc != null && !protocolStatusDesc.equals("")){
//                            dynaValidatorForm.set("approvalDescription", protocolStatusDesc);
//                        }
                    }
                }     
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
            }
        }
        
        request.getSession().setAttribute("pdReviewList",vecData);
        isSuccess = true;
        return isSuccess;
    }
    
      
    /*This method deletes the Special Review
     */
    private boolean deleteSpecialReview(HttpSession session, HttpServletRequest request,
            DynaValidatorForm specialReviewForm,
            HashMap hmpSpRev,
            String proposalNumber)throws Exception{
        boolean isAllDeleted = false;
        boolean isMenuSaved = false;
        boolean isSuccess=false;

        String protocolNumber = specialReviewForm.getString("spRevProtocolNumber");
        HashMap hmpProtocolData = new HashMap();
        hmpProtocolData.put("protocolNumber", protocolNumber);

        WebTxnBean txnBean = new WebTxnBean();
        //Modified for instance variable case#2960.
        DateUtils dateUtils = new DateUtils();
        specialReviewForm.set(PROPOSAL_NUMBER,proposalNumber);
        // Modified for COEUSQA-3726 : Issue with IRB linking - while adding a special review in proposal dev application is incorrectly updating the approval type code - Start
        if(specialReviewForm.get(APPROVAL_CODE) == null || "".equals((String)specialReviewForm.get(APPROVAL_CODE))){
            specialReviewForm.set(APPROVAL_CODE,"0");
        }
        // Modified for COEUSQA-3726 : Issue with IRB linking - while adding a special review in proposal dev application is incorrectly updating the approval type code - End
        txnBean.getResults(request, UPDATE_SPECIAL_REVIEW, specialReviewForm);
        // Update the proposal hierarchy sync flag
        updateProposalSyncFlags(request, proposalNumber);
        Hashtable hsSpRev = (Hashtable)txnBean.getResults(request, "getPropDevSpecialReview", hmpSpRev);
        // Vector formData = (Vector)hsSpRev.get(GET_PROPOSAL_SPECIAL_REVIEW);
        
        // COEUSQA-2320 Show in Lite for Special Review in Code table - Start
        // Filter the special reivew entries based on show in lite flag.
        Vector vecSpecialReviewDetails = (Vector) hsSpRev.get(GET_SPECIAL_REVIEW_CODE);
        Vector vecFilteredData = filterSpecialReview(vecSpecialReviewDetails);
        // session.setAttribute("splReview", hsSpRev.get(GET_SPECIAL_REVIEW_CODE));
        session.setAttribute("splReview", vecFilteredData);
        // COEUSQA-2320 Show in Lite for Special Review in Code table - End

        session.setAttribute("approval", hsSpRev.get(GET_REVIEW_APPROVAL_TYPE));
        Vector vecData = (Vector)hsSpRev.get(GET_PROPOSAL_SPECIAL_REVIEW);
        if(vecData!= null && vecData.size() >0){
            for(int i=0;i<vecData.size();i++){
                DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)vecData.get(i);
                String applDate = (String)dynaValidatorForm.get(APPLICATION_DATE);
                String approvalDate = (String)dynaValidatorForm.get(APPROVAL_DATE);
                if(applDate != null){
                    String value = dateUtils.formatDate(applDate,edu.mit.coeuslite.utils.DateUtils.MM_DD_YYYY);
                    dynaValidatorForm.set(APPLICATION_DATE,value);
                }else{
                    dynaValidatorForm.set(APPLICATION_DATE,"");
                }
                if(approvalDate != null){
                    String dateValue = dateUtils.formatDate(approvalDate,edu.mit.coeuslite.utils.DateUtils.MM_DD_YYYY);
                    dynaValidatorForm.set(APPROVAL_DATE,dateValue);
                    
                }else{
                    dynaValidatorForm.set(APPROVAL_DATE,"");
                } 
                //Added for case#2990 - Proposal Special Review Enhancement - start
                String specialReviewCode = (String)dynaValidatorForm.get("specialReviewCode");
                if(specialReviewCode.equals("1")){
                    String spRevProtocolNumber = (String)dynaValidatorForm.get("spRevProtocolNumber");
                    if(spRevProtocolNumber != null && !spRevProtocolNumber.equals("")){                        
                        HashMap hmProtocolDetails = getProtocolDetails(request, spRevProtocolNumber);
                        String protocolStatusDesc = (String)hmProtocolDetails.get("protocolStatusDesc");
//                        if(protocolStatusDesc != null && !protocolStatusDesc.equals("")){
//                            dynaValidatorForm.set("approvalDescription", protocolStatusDesc);   
//                        }
                    }
                }                
                //Added for case#2990 - Proposal Special Review Enhancement - end   
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                //If special review is of type Animal Usage
                if(specialReviewCode.equals("2")){
                    String spRevProtocolNumber = (String)dynaValidatorForm.get("spRevProtocolNumber");
                    if(spRevProtocolNumber != null && !spRevProtocolNumber.equals("")){                        
                        HashMap hmIacucProtoDetails = getIacucProtocolDetails(request, spRevProtocolNumber);
                        String protocolStatusDesc = (String)hmIacucProtoDetails.get("protocolStatusDesc");
//                        if(protocolStatusDesc != null && !protocolStatusDesc.equals("")){
//                            dynaValidatorForm.set("approvalDescription", protocolStatusDesc);   
//                        }
                    }
                }   
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
            }
        }

       // deleteProtocol(hmpProtocolData, request);
        request.getSession().setAttribute("pdReviewList",vecData);
        isSuccess = true;
        return isSuccess;
        
    }
    
    public void cleanUp() {
    }
    
    public boolean checkDuplicateSpecialReview(Vector data, HttpServletRequest request, String splRevCode){
        boolean noDup = true;
        for(int index = 0; index <data.size();index++){
            DynaValidatorForm form = (DynaValidatorForm)data.get(index);
            String specialRevCode=(String)form.get(SPECIAL_REVIEW_CODE);
            if(splRevCode.trim().equals(specialRevCode)){
                noDup = false;
                ActionMessages messages = new ActionMessages();
                messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.special_review_code_id"));
                saveMessages(request, messages);
            }
        }
        return noDup;
    }
    //Added for case#2990 - Proposal Special Review Enhancement - start
    /**
     * This method checks whether a given protocol number is valid or not
     * @param request HttpServletRequest
     * @param protocolNumber String
     * @throws Exception
     * @return validProtocolNumber boolean indicating valid/invalid protocol
     */
    private boolean isValidProtocolNumber(HttpServletRequest request, String protocolNumber) throws Exception{
        boolean validProtocolNumber = true;
        ActionMessages actionMessages = new ActionMessages();
        Map hmNumber  = new HashMap();
        String strNumber, count;
        WebTxnBean webTxnBean = new WebTxnBean();
        //Execute the stored function FN_IS_VALID_PROTOCOL_NUMBER to get the count
        hmNumber.put("spRevProtocolNumber", protocolNumber);
        hmNumber =(Hashtable)webTxnBean.getResults(request, "checkProtocolNumber", hmNumber);
        hmNumber = (HashMap)hmNumber.get("checkProtocolNumber");
        count = (String)hmNumber.get("ll_count");
        if(Integer.parseInt(count) < 1){
            actionMessages.add("error.invalidProtocolNo", new ActionMessage("error.invalidProtocolNo"));
            saveMessages(request, actionMessages);
            validProtocolNumber = false;
        }
        return validProtocolNumber;        
    }
    
    /**
     * Server side validation for Approval drop down
     * @param request HttpServletRequest
     * @param approvalCode String
     * @throws Exception
     * @return isApprovalPresent boolean indicating present/absent
     */
    private boolean validateApproval(HttpServletRequest request, String approvalCode){
        boolean isApprovalPresent = true;
        ActionMessages actionMessages = new ActionMessages();
        if(approvalCode == null || approvalCode.equals("")){
            actionMessages.add("approvalRequired", new ActionMessage("error.specialReview.approvalCode"));
            saveMessages(request, actionMessages);
            isApprovalPresent = false;            
        }
        return isApprovalPresent;
        
    }
    
    /**
     * This method gets the protocol details for a given protocol number
     * @param request HttpServletRequest
     * @param protocolNumber String
     * @throws Exception
     * @return hmProtocolDetails HashMap containing details like applicationDate, ...
     */
    private HashMap getProtocolDetails(HttpServletRequest request, String protocolNumber) throws Exception{        
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmProtocolDetails = new HashMap();
        hmProtocolDetails.put("protocolNumber", protocolNumber);
        Hashtable htProtocolDetails = (Hashtable)webTxnBean.getResults(request, "getProtocolInfo", hmProtocolDetails);
        Vector vecProtocolDetails = (Vector)htProtocolDetails.get("getProtocolInfo");
        DynaActionForm dynaForm = null;
        if(vecProtocolDetails != null && vecProtocolDetails.size() > 0){
            dynaForm = (DynaActionForm)vecProtocolDetails.get(0);        
            hmProtocolDetails.put(APPLICATION_DATE, dynaForm.get(APPLICATION_DATE));
            hmProtocolDetails.put(APPROVAL_DATE, dynaForm.get(APPROVAL_DATE));
            hmProtocolDetails.put("protocolStatusDesc", dynaForm.get("protocolStatusDesc"));        
        }else{
            hmProtocolDetails.put(APPLICATION_DATE, "");
            hmProtocolDetails.put(APPROVAL_DATE, "");
            hmProtocolDetails.put("protocolStatusDesc", "");            
        }
        return hmProtocolDetails;
    }    
    
    /**
     * This method checks, whether a given protocol, for a given special review(ie proposal), 
     * already exists or not, when special review type is 'Human Subjects' and 'Animal Usage'
     * @param request HttpServletRequest
     * @param protocolNumber String
     * @param proposalNumber String
     * @throws Exception
     * @return isProtocolPresent boolean indicating whether the protocol already exits,
     * for a given special review when type is human subjects
     */
    private boolean checkIfProtocolExists(HttpServletRequest request, String protocolNumber, String proposalNumber) throws Exception{
        boolean isProtocolPresent = false;
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmPropSpRevDetails = new HashMap();        
        hmPropSpRevDetails.put("proposalNumber", proposalNumber);
        // COEUSQA-2320 Show in Lite for Special Review in Code table
        // Add module code to the map.
        hmPropSpRevDetails.put("moduleCode",ModuleConstants.PROPOSAL_DEV_MODULE_CODE);
        Hashtable htPropSpRevDetails = (Hashtable)webTxnBean.getResults(request, "getPropDevSpecialReview", hmPropSpRevDetails);
        Vector vecPropSpRevDetails = (Vector)htPropSpRevDetails.get(GET_PROPOSAL_SPECIAL_REVIEW);
        if(vecPropSpRevDetails != null && vecPropSpRevDetails.size() > 0){
            for(int index = 0; index < vecPropSpRevDetails.size(); index ++){
                DynaActionForm dynaForm = (DynaActionForm)vecPropSpRevDetails.get(index);
                String specialReviewCode = (String)dynaForm.get("specialReviewCode");
                if(specialReviewCode.equals("1")){
                    String spRevProtocolNumber = (String)dynaForm.get("spRevProtocolNumber");
                    String splReviewNumForUpd = request.getParameter("specialReviewNumber");
                    if(splReviewNumForUpd != null && splReviewNumForUpd.equalsIgnoreCase(dynaForm.get("specialReviewNumber").toString())){
                        continue;
                    }else if(spRevProtocolNumber != null && !spRevProtocolNumber.equals("")){
                        if(spRevProtocolNumber.equals(protocolNumber)){
                            isProtocolPresent = true;
                            //break;
                        }
                    }
                    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                }else if(specialReviewCode.equals("2")){
                    String spRevProtocolNumber = (String)dynaForm.get("spRevProtocolNumber");
                    String splReviewNumForUpd = request.getParameter("specialReviewNumber");
                    if(splReviewNumForUpd != null && splReviewNumForUpd.equalsIgnoreCase(dynaForm.get("specialReviewNumber").toString())){
                        continue;
                    }else if(spRevProtocolNumber != null && !spRevProtocolNumber.equals("")){
                        if(spRevProtocolNumber.equals(protocolNumber)){
                            isProtocolPresent = true;
                            break; // chk whether it shud work if thr more than 2 sp rev
                        }
                    }
                }
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
            }
        }        
        return isProtocolPresent;
    }
    //Added for case#2990 - Proposal Special Review Enhancement - end

    /**
     * Filter the special reivew entries based on show in lite flag.
     * Added for COEUSQA-2320 Show in Lite for Special Review in Code table
     * @param vecSpecialReviewDetails, unfiltered special review details
     * @return Special review details with showInLite flag set to Y
     */
    private Vector filterSpecialReview(Vector vecSpecialReviewDetails) {
        Vector vecFilteredData = new Vector();
        for (int index = 0; index < vecSpecialReviewDetails.size(); index++) {
            DynaValidatorForm form = (DynaValidatorForm) vecSpecialReviewDetails.get(index);
            if ("Y".equals(form.get("showInLite"))) {
                vecFilteredData.add(form);
            }
        }
        return vecFilteredData;
    }

    private void deleteProtocol(HashMap hmpProtocolData, HttpServletRequest request)throws Exception {
        String navigator = EMPTY_STRING;
        WebTxnBean webTxnBean = null;
        String protocolNumber = (String)hmpProtocolData.get("protocolNumber");
        UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute("user"+request.getSession().getId());
        webTxnBean = new WebTxnBean();

        HashMap hmValidProtocol = new HashMap();
        hmValidProtocol.put("spRevProtocolNumber",protocolNumber);
        Hashtable htValidProtocol = (Hashtable)webTxnBean.getResults(request, "checkProtocolNumber", hmValidProtocol);
        HashMap hmValid = (HashMap)htValidProtocol.get("checkProtocolNumber");
        int validProtocol = Integer.parseInt(hmValid.get("ll_count").toString());

        if(validProtocol == 1){
            // If  Protocol exists, Check lock exists or not
            LockBean lockBean = getLockingBean(userInfoBean, (String)request.getSession().getAttribute(
                    CoeusLiteConstants.PROTOCOL_NUMBER+request.getSession().getId()), request);
//            boolean alreadyLocked = isLockExists(lockBean, lockBean.getModuleKey());
//            LockBean lockData = getLockedData(CoeusLiteConstants.PROTOCOL_LOCK_STR+lockBean.getModuleNumber(),request);
                      
                webTxnBean = new WebTxnBean();
                Hashtable htDeleteProtocol = (Hashtable)webTxnBean.getResults(request, "deleteProtocol", hmpProtocolData);
                HashMap hm = (HashMap)htDeleteProtocol.get("deleteProtocol");
                int deleteSuccess = Integer.parseInt(hm.get("deleteSuccessful").toString());

                if(deleteSuccess == 0){
                    // If the Protocol is deleted successfully
                    releaseLock(lockBean, request);
                    navigator = "success";
                } else{
                    // If not able to Deletion is not success
                    navigator = "error";
                }
            }
    }
    
    
    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
    /**
     * This method checks whether a given iacuc protocol number is valid or not
     * @param request HttpServletRequest
     * @param protocolNumber String
     * @throws Exception
     * @return validIacucProtoNumber boolean indicating valid/invalid protocol
     */
    private boolean isValidIacucProtocolNumber(HttpServletRequest request, String protocolNumber) throws Exception{
        boolean validIacucProtoNumber = true;
        ActionMessages actionMessages = new ActionMessages();
        Map hmNumber  = new HashMap();
        String strNumber, count;
        WebTxnBean webTxnBean = new WebTxnBean();
        //Execute the stored function FN_IS_VALID_AC_PROTOCOL_NUMBER to get the count
        hmNumber.put("spRevProtocolNumber", protocolNumber);
        hmNumber =(Hashtable)webTxnBean.getResults(request, "checkIacucNumber", hmNumber);
        hmNumber = (HashMap)hmNumber.get("checkIacucNumber");
        count = (String)hmNumber.get("ll_count");
        if(Integer.parseInt(count) < 1){
            actionMessages.add("error.invalidProtocolNo", new ActionMessage("error.invalidProtocolNo"));
            saveMessages(request, actionMessages);
            validIacucProtoNumber = false;
        }
        return validIacucProtoNumber;
    }
    
    /**
     * This method gets the protocol details for a given Iacuc protocol number
     * @param request HttpServletRequest
     * @param protocolNumber String
     * @throws Exception
     * @return hmIacucProtoDetails HashMap containing details like applicationDate, ...
     */
    private HashMap getIacucProtocolDetails(HttpServletRequest request, String protocolNumber) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmIacucProtoDetails = new HashMap();
        hmIacucProtoDetails.put("protocolNumber", protocolNumber);
        //Executes the procedure GET_AC_PROTOCOL_INFO to get the Iacuc protocol details
        Hashtable htIacucProtoDetails = (Hashtable)webTxnBean.getResults(request, "getIacucInfo", hmIacucProtoDetails);
        Vector vecProtocolDetails = (Vector)htIacucProtoDetails.get("getIacucInfo");
        DynaActionForm dynaForm = null;
        if(vecProtocolDetails != null && vecProtocolDetails.size() > 0){
            dynaForm = (DynaActionForm)vecProtocolDetails.get(0);
            hmIacucProtoDetails.put(APPLICATION_DATE, dynaForm.get(APPLICATION_DATE));
            hmIacucProtoDetails.put(APPROVAL_DATE, dynaForm.get(APPROVAL_DATE));
            hmIacucProtoDetails.put("protocolStatusDesc", dynaForm.get("protocolStatusDesc"));
        }else{
            hmIacucProtoDetails.put(APPLICATION_DATE, "");
            hmIacucProtoDetails.put(APPROVAL_DATE, "");
            hmIacucProtoDetails.put("protocolStatusDesc", "");
        }
        return hmIacucProtoDetails;
    }
    
    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
}
