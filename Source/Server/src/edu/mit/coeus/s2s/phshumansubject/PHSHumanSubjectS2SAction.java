/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeus.s2s.phshumansubject;

import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalSpecialReviewFormBean;
import edu.mit.coeus.s2s.bean.S2STxnBean;
import edu.mit.coeus.s2s.phshumansubject.bean.PHSHumanSubjectAttachments;
import edu.mit.coeus.s2s.phshumansubject.bean.PHSHumanSubjectDelayedStudyBean;
import edu.mit.coeus.s2s.phshumansubject.bean.PHSHumanSubjectTxnBean;
import edu.utk.coeuslite.propdev.action.ProposalBaseAction;
import edu.mit.coeus.s2s.phshumansubject.bean.PHSHumanSubjectFormBean;
import edu.mit.coeus.s2s.phshumansubject.bean.PHSHumanSubjectsBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.S2SConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentIdGenerator;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
/**
 *
 * @author polusdev
 */
public class PHSHumanSubjectS2SAction extends ProposalBaseAction {

    private static final String GET_PHS_HUMAN_SUBJECT_S2S_DETAILS = "/getPHSHumanSubjectS2SDetails";
    private static final String EDIT_PHS_HUMAN_SUBJECT_S2S_DETAILS = "/editPHSHumanSubjectS2SDetails";
    private static final String SAVE_PHS_HUMAN_SUBJECT_S2S_DETAILS = "/savePHSHumanSubjectS2SDetails";
    private static final String SAVE_PHS_HUMAN_SUB_HEADER_DETAILS = "/savePHSHumanSubHeaderDetails";
    private static final String SAVE_DELAYED_ONSET_STUDIES = "/saveDelayedOnsetStudies";
    private static final String DELETE_PHS_HUMAN_SUBJECT_DETAILS = "/deletePHSHumanSubjectS2SDetails";
    private static final String VIEW_PHS_HUMAN_SUBJECT_ATTACHMENT = "/viewPHSHumanSubjectDetails";
    private static final String SAVE_HUMAN_OTHER_ATTACHMENT = "/saveHumanOtherAttachment";
    private static final String VIEW_HUMAN_OTHER_ATTACHMENT = "/viewHumanOtherAttachment";
    private static final String DOWNLOAD_HUMANSUBJECT_FORM = "/downloadHumanSubjectForm";
    private String editRow = EMPTY_STRING;
    private String deleteRow = EMPTY_STRING;
    private static final String PROPOSAL_NUMBER = "proposalNumber";
    private ProposalDevelopmentFormBean proposalBean;
    private ProposalDevelopmentTxnBean proposalDevTxnBean = new ProposalDevelopmentTxnBean();
    private S2STxnBean s2sTxnBean;
    private static final String N_NO = "N";
    private static final String Y_YES = "Y";
    private static final String MODIFY_ANY_PROPOSAL_RIGHT = "MODIFY_ANY_PROPOSAL";
    private static final String MODIFY_PROPOSAL_RIGHT="MODIFY_PROPOSAL"; 
    private static final String MODIFY_NARRATIVE_RIGHT="MODIFY_NARRATIVE";  
    private static final String MODIFY_BUDGET_RIGHT="MODIFY_BUDGET";    
    private static final String VIEW_ANY_PROPOSAL_RIGHT="VIEW_ANY_PROPOSAL";   
    private final String VIEW_RIGHT="VIEW_PROPOSAL";

    /**
     *
     * @param actionMapping
     * @param actionForm
     * @param request
     * @param response
     * @throws Exception
     * @return
     */
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String filePath = fetchParameterValue(request,"PHS_HUMAN_SUBJECT_FORM_PATH"); 
        session.setAttribute("DOWNLOAD_PATH_HUMAN_SUBJECT",filePath);
        PHSHumanSubjectsBean phsHumanSubjectBean = (PHSHumanSubjectsBean) actionForm;
        phsHumanSubjectBean.setProposalNumber((String) session.getAttribute(PROPOSAL_NUMBER + session.getId()));
        ActionForward actionForward = performUserAttachmentS2SAction(phsHumanSubjectBean, request, response, actionMapping, actionForm);
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems", CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
        mapMenuList.put("menuCode", CoeusliteMenuItems.HUMAN_SUBJECTS_CLINICAL_TRIALS);
        setSelectedMenuList(request, mapMenuList);
        readSavedStatus(request);
        return actionForward;
    }

    /**
     * This method will identify which request is comes from which path and
     * navigates to the respective ActionForward
     *
     * @returns ActionForward object
     * @param actionMapping
     * @param dynaForm
     * @throws Exception
     * @return
     */
    private ActionForward performUserAttachmentS2SAction(PHSHumanSubjectsBean phsHumanSubjectBean,
        HttpServletRequest request, HttpServletResponse response, ActionMapping actionMapping, ActionForm actionForm) throws Exception {
        HttpSession session = request.getSession();
        boolean byepassLock = false; // This is added when came form Coeus Premium
        String proposalNumber = (String) session.getAttribute(PROPOSAL_NUMBER + session.getId());
        String navigator = EMPTY_STRING;
        editRow = request.getParameter("selectedIndex");
        deleteRow = request.getParameter("deletedIndex");
        UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user" + session.getId());
        WebTxnBean webTxnBean = new WebTxnBean();
        if(request.getParameter("proposalNumber") != null){
            if(!hasProposalRight(session)){
                  ActionForward actionForward = actionMapping.findForward("noRight");
                 return actionForward;
            }  
            proposalNumber = request.getParameter("proposalNumber");
            session.setAttribute("PHSHumanFromPremium"+proposalNumber+ session.getId(),true);
            session.setAttribute(PROPOSAL_NUMBER + session.getId(),proposalNumber);
            HashMap hmProposalHeader =  new HashMap();            
            hmProposalHeader.put("proposalNumber",proposalNumber);
            Hashtable htProposalHeader = (Hashtable)webTxnBean.getResults(request, "getProposalHeaderData" , hmProposalHeader );
            Vector vecProposalHeader = (Vector)htProposalHeader.get("getProposalHeaderData");
            if(vecProposalHeader!=null && vecProposalHeader.size()>0) {
               session.setAttribute("epsProposalHeaderBean", (EPSProposalHeaderBean)vecProposalHeader.elementAt(0));
            }            
        }             
        if(session.getAttribute("PHSHumanFromPremium"+proposalNumber+ session.getId())!= null){
            byepassLock = true;
            EPSProposalHeaderBean proposalHeaderBean = (EPSProposalHeaderBean)session.getAttribute("epsProposalHeaderBean");
            checkDisplayMode(proposalHeaderBean,session);
        }        
        if (actionMapping.getPath().equals(GET_PHS_HUMAN_SUBJECT_S2S_DETAILS)) {
            navigator = getUserAttachmentS2SDetails(phsHumanSubjectBean, userInfoBean, request);
        } else if (actionMapping.getPath().equals(EDIT_PHS_HUMAN_SUBJECT_S2S_DETAILS)) {
            navigator = editUserAttachmentS2SDetails(phsHumanSubjectBean, request);
        } else if (actionMapping.getPath().equals(VIEW_PHS_HUMAN_SUBJECT_ATTACHMENT)) {
            String documentURL = displayContents(phsHumanSubjectBean, request);
            response.sendRedirect(request.getContextPath() + documentURL);
        } else if (actionMapping.getPath().equals(VIEW_HUMAN_OTHER_ATTACHMENT)) {
            String documentURL = viewHumanOtherDoc(phsHumanSubjectBean, request);
            response.sendRedirect(request.getContextPath() + documentURL);
        } else if (actionMapping.getPath().equals(DOWNLOAD_HUMANSUBJECT_FORM)) {
            navigator = downloadHumanSubjectForm(request, response);
            if ("success".equals(navigator)) {
               navigator = getUserAttachmentS2SDetails(phsHumanSubjectBean, userInfoBean, request);
            }        
        } else {
            LockBean lockBean = getLockingBean(userInfoBean, (String) session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER + session.getId()), request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            LockBean lockData = getLockedData(CoeusLiteConstants.PROP_DEV_LOCK_STR + lockBean.getModuleNumber(), request);
            if (byepassLock || (!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId()))) {
                EPSProposalHeaderBean proposalHeaderBean = (EPSProposalHeaderBean)session.getAttribute("epsProposalHeaderBean");
                checkDisplayMode(proposalHeaderBean,session);   
                if (actionMapping.getPath().equals(SAVE_PHS_HUMAN_SUB_HEADER_DETAILS)) {
                    navigator = savePHSHumanSubHeaderDetails(actionForm, userInfoBean, request);
                    if ("success".equals(navigator)) {
                        navigator = getUserAttachmentS2SDetails(phsHumanSubjectBean, userInfoBean, request);
                    }
                }
                if (actionMapping.getPath().equals(SAVE_DELAYED_ONSET_STUDIES)) {
                    navigator = saveDelayedOnsetStudies(actionForm, userInfoBean, request);
                    if ("success".equals(navigator)) {
                        navigator = getUserAttachmentS2SDetails(phsHumanSubjectBean, userInfoBean, request);
                    }
                }
                if (actionMapping.getPath().equals(SAVE_PHS_HUMAN_SUBJECT_S2S_DETAILS)) {
                    navigator = saveUserAttachmentS2SDetails(phsHumanSubjectBean, userInfoBean, request);
                    if ("success".equals(navigator)) {
                        navigator = getUserAttachmentS2SDetails(phsHumanSubjectBean, userInfoBean, request);
                    }
                } else if (actionMapping.getPath().equals(DELETE_PHS_HUMAN_SUBJECT_DETAILS)) {
                    navigator = deleteUserAttachmentS2SDetails(userInfoBean, request);
                    if ("success".equals(navigator)) {
                        navigator = getUserAttachmentS2SDetails(phsHumanSubjectBean, userInfoBean, request);
                    }
                } else if (actionMapping.getPath().equals(SAVE_HUMAN_OTHER_ATTACHMENT)) {
                    navigator = saveHumanOtherAttachment(actionForm, userInfoBean, request);
                    if ("success".equals(navigator)) {
                        navigator = getUserAttachmentS2SDetails(phsHumanSubjectBean, userInfoBean, request);
                    }
                }
            } else {
                String errMsg = "release_lock_for";
                ActionMessages messages = new ActionMessages();
                messages.add("errMsg", new ActionMessage(errMsg, lockBean.getModuleKey(), lockBean.getModuleNumber()));
                saveMessages(request, messages);
                navigator = "success";
            }
        }
        ActionForward actionForward = actionMapping.findForward(navigator);
        return actionForward;

    }

    /**
     * This method gets the user attachment s2s data by calling the
     * getUserAttachmentS2SData method
     *
     * @param dynaValidatorForm
     * @param userInfoBean
     * @param request
     * @throws Exception
     * @return String to navigator
     */
    private String getUserAttachmentS2SDetails(PHSHumanSubjectsBean phsHumanSubjectsBean,
            edu.mit.coeus.bean.UserInfoBean userInfoBean, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        phsHumanSubjectsBean = getPHSHumanAndClinicalInfo(request, phsHumanSubjectsBean);        
        syncFromSpecialReviewData(request, phsHumanSubjectsBean);        
        phsHumanSubjectsBean = getPHSHumanSubjectHeaderDetails(request, phsHumanSubjectsBean);
        request.setAttribute("phsHumanSubjectinfo", phsHumanSubjectsBean);
        List<PHSHumanSubjectFormBean> lstUserAttachmentS2SData = getUserAttachmentS2SData(phsHumanSubjectsBean, request);
        request.setAttribute("lstUserAttachmentS2S", lstUserAttachmentS2SData);
        phsHumanSubjectsBean.setPhsHumnSubjtFormBeanList(lstUserAttachmentS2SData);
        List<PHSHumanSubjectAttachments> phsHumanSubjectAttachments = getPHSHumanandOthrReqAttchmnts(request, phsHumanSubjectsBean);
        request.setAttribute("phsHumanSubjectAttachments", phsHumanSubjectAttachments);
        phsHumanSubjectsBean.setPhsHumanSubjectAttList(phsHumanSubjectAttachments);
        List<PHSHumanSubjectDelayedStudyBean> phsHumnSubjtDlyStdyList = getDelayedOnsetStudy(request, phsHumanSubjectsBean);
        phsHumanSubjectsBean.setPhsHumnSubjtDlyStdyList(phsHumnSubjtDlyStdyList);
        request.setAttribute("phsHumnSubjtDlyStdyList", phsHumnSubjtDlyStdyList);
        return "success";
    }

    /**
     * This method is used to retrieve the user attachment s2s data
     *
     * @param dynaValidatorForm
     * @param request
     * @throws Exception
     * @return String to navigator
     */
    private List<PHSHumanSubjectFormBean> getUserAttachmentS2SData(PHSHumanSubjectsBean phsHumanSubjectBean, HttpServletRequest request) throws Exception {
        String proposalNumber = "";
        List<PHSHumanSubjectFormBean> lstUserAttS2S = new ArrayList();
        proposalNumber = (String) request.getSession().getAttribute("proposalNumber" + request.getSession().getId());
        PHSHumanSubjectTxnBean phsHumanSubjectTxnBean = new PHSHumanSubjectTxnBean();
        lstUserAttS2S = phsHumanSubjectTxnBean.getUserAttachedS2SForm(proposalNumber);
        /*  dynaValidatorForm.set("acType", "");
         dynaValidatorForm.set("description", "");
         dynaValidatorForm.set("fileName", ""); */
        return lstUserAttS2S;
    }

    /**
     * This method is used to edit the user attachment s2s data
     *
     * @param dynaValidatorForm
     * @param userInfoBean
     * @param request
     * @throws Exception
     * @return String to navigator
     */
    private String editUserAttachmentS2SDetails(PHSHumanSubjectsBean dynaValidatorForm, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();

        List lstUserAttachmentS2SData = (ArrayList) session.getAttribute("lstUserAttachmentS2S");
        PHSHumanSubjectFormBean phsHumnSubjtFormBean = (PHSHumanSubjectFormBean) lstUserAttachmentS2SData.get(Integer.parseInt(editRow));
        /*dynaValidatorForm.set("description", phsHumnSubjtFormBean.getDescription());
         dynaValidatorForm.set("fileName", phsHumnSubjtFormBean.getPdfFileName());
         dynaValidatorForm.set("selectedRow", editRow);*/

        return "success";
    }

    /**
     * This method saves the user attachment s2s data by calling the
     * saveUserAttachmentS2SDetails method
     *
     * @param dynaValidatorForm
     * @param userInfoBean
     * @param request
     * @throws Exception
     * @return String to navigator
     */
    private String saveUserAttachmentS2SDetails(PHSHumanSubjectsBean dynaValidatorForm,
            edu.mit.coeus.bean.UserInfoBean userInfoBean, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        FormAttachmentExtractService extractService = new FormAttachmentExtractService();
        List lstUserAttachmentS2SData = (ArrayList) session.getAttribute("lstUserAttachmentS2S");
        String acType = dynaValidatorForm.getAcType();
        String description = dynaValidatorForm.getDescription();

        String proposalNumber = (String) request.getSession().getAttribute("proposalNumber" + request.getSession().getId());

        if (acType == null || acType.length() == 0) {
            acType = TypeConstants.INSERT_RECORD;
        }

        PHSHumanSubjectTxnBean phsHumanSubjectTxnBean = new PHSHumanSubjectTxnBean(userInfoBean.getUserId());
        if (acType != null && "I".equals(acType)) {
            PHSHumanSubjectFormBean phsHumnSubjtFormBean = new PHSHumanSubjectFormBean();
            phsHumnSubjtFormBean.setAcType(TypeConstants.INSERT_RECORD);
            phsHumnSubjtFormBean.setDescription(description);
            phsHumnSubjtFormBean.setProposalNumber(proposalNumber);
            FormFile myFile = dynaValidatorForm.getS2sFormFile();
            if (myFile != null) {
                try {
                    byte[] fileData = myFile.getFileData();
                    if (fileData.length > 0) {
                        phsHumnSubjtFormBean.setUserAttachedS2SPDF(fileData);// BLOB data
                        phsHumnSubjtFormBean.setPdfFileName(myFile.getFileName());
                        phsHumnSubjtFormBean.setPdfAcType(TypeConstants.INSERT_RECORD);
                    } else {
                        phsHumnSubjtFormBean.setUserAttachedS2SPDF(null);
                    }
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (myFile != null) {
                StringBuffer strBuff = request.getRequestURL();
                String servletPath = new String(strBuff);
                servletPath = servletPath.substring(0, servletPath.lastIndexOf('/'));
                try {

                    List formBeans = extractService.processPdfForm(phsHumnSubjtFormBean);
                    if (formBeans != null && formBeans.size() == 0) {
                        request.setAttribute("attachedS2SError", true);
                        return "success";
                    }
                    Vector vecUserAttachmentS2S = new Vector();
                    vecUserAttachmentS2S.addAll(formBeans);
                    phsHumanSubjectTxnBean.saveUserS2SForm(vecUserAttachmentS2S);
                } catch (Exception ex) {
                    request.setAttribute("attachedS2SError", true);
                    return "success";
                }
//                vecUserAttachmentS2S.add(phsHumnSubjtFormBean);
            }
        } else if (acType != null && "U".equals(acType)) {

            lstUserAttachmentS2SData = (ArrayList) session.getAttribute("lstUserAttachmentS2S");
            PHSHumanSubjectFormBean phsHumnSubjtFormBean = (PHSHumanSubjectFormBean) lstUserAttachmentS2SData.get(Integer.parseInt(editRow));

            phsHumnSubjtFormBean.setAcType(TypeConstants.UPDATE_RECORD);
            phsHumnSubjtFormBean.setDescription(description);
            phsHumnSubjtFormBean.setUpdateUser(userInfoBean.getUserId());
            FormFile myFile = null;/*(FormFile) dynaValidatorForm.get("document");*/
            if (myFile != null) {
                try {
                    byte[] fileData = myFile.getFileData();
                    if (fileData.length > 0) {
                        phsHumnSubjtFormBean.setUserAttachedS2SPDF(fileData);// BLOB data
                        phsHumnSubjtFormBean.setPdfFileName(myFile.getFileName());
                        phsHumnSubjtFormBean.setPdfAcType(TypeConstants.UPDATE_RECORD);
                    } else {
                        phsHumnSubjtFormBean.setUserAttachedS2SPDF(null);
                    }
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (myFile != null) {
                List formBeans = extractService.processPdfForm(phsHumnSubjtFormBean);
                Vector vecUserAttachmentS2S = new Vector();
                vecUserAttachmentS2S.addAll(formBeans);
                phsHumanSubjectTxnBean.saveUserS2SForm(vecUserAttachmentS2S);
            }

            editRow = EMPTY_STRING;
            /* dynaValidatorForm.set("selectedRow", "");*/
        } else if (acType != null && "D".equals(acType)) {
            lstUserAttachmentS2SData = (ArrayList) session.getAttribute("lstUserAttachmentS2S");
            PHSHumanSubjectFormBean phsHumnSubjtFormBean = (PHSHumanSubjectFormBean) lstUserAttachmentS2SData.get(Integer.parseInt(deleteRow));
            phsHumnSubjtFormBean.setAcType(TypeConstants.DELETE_RECORD);
            phsHumnSubjtFormBean.setUpdateUser(userInfoBean.getUserId());

            List deleteData = new ArrayList();
            deleteData.add(phsHumnSubjtFormBean);
            lstUserAttachmentS2SData = phsHumanSubjectTxnBean.saveUserS2SForm(deleteData);
        }
        /* dynaValidatorForm.set("acType", "");
         dynaValidatorForm.set("description", "");*/
         dynaValidatorForm.setDescription("");
        return "success";
    }
    /**
     * This method delete Human Subject form data for a specific formNumber
     * deleteUserAttachmentS2SDetails method  
     * @param userInfoBean
     * @param request
     * @throws Exception
     * @return String to navigator
     */
    private String deleteUserAttachmentS2SDetails(edu.mit.coeus.bean.UserInfoBean userInfoBean, HttpServletRequest request) throws Exception {
        String proposalNumber = (String) request.getSession().getAttribute("proposalNumber" + request.getSession().getId());
        String formNumber = request.getParameter("formNumber");
        PHSHumanSubjectTxnBean phsHumanSubjectTxnBean = new PHSHumanSubjectTxnBean(userInfoBean.getUserId()); 
        phsHumanSubjectTxnBean.deleteHumanSubjectForm(proposalNumber,formNumber);        
        return "success";
    }

    /**
     * displays the contents of the PDF or XMl file
     *
     * @param content specified either XML or PDF
     * @param phsHumnSubjtFormBean user attachment s2s data
     */
    private String displayContents(PHSHumanSubjectsBean phsHumanSubjectBean, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        String file = request.getParameter("contentType");
        int formNumber = Integer.parseInt(request.getParameter("formNumber"));
        DocumentBean documentBean = new DocumentBean();
        Map map = new HashMap();
        map.put(S2SConstants.PROPOSAL_NUMBER, phsHumanSubjectBean.getProposalNumber());
        map.put(S2SConstants.PHS_HUMANSUBJECT_FORM_NUMBER, "" + formNumber);
        map.put(S2SConstants.FILE, file);
        map.put("isPhsHumanForm", "Y");
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.s2s.phshumansubject.PHSHumanSubjectS2SDocReader");
        documentBean.setParameterMap(map);
        String docId = DocumentIdGenerator.generateDocumentId();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("/StreamingServlet");
        stringBuffer.append("?");
        stringBuffer.append(DocumentConstants.DOC_ID);
        stringBuffer.append("=");
        stringBuffer.append(docId);
        session.setAttribute(docId, documentBean);

        String templateURL = stringBuffer.toString();
        session.setAttribute("url", templateURL);
        return templateURL;
    }

    private String viewHumanOtherDoc(PHSHumanSubjectsBean phsHumanSubjectBean, HttpServletRequest request) throws Exception {
        //Modified for Case#3036 - Didn't recognize document file type in Protocol attachments -End
        DocumentBean documentBean = new DocumentBean();
        Map map = new HashMap();
        HttpSession session = request.getSession();
        int attachmentNumber = Integer.parseInt(request.getParameter("attachmentNumber"));
        int attachmentType = Integer.parseInt(request.getParameter("attachmentType"));
        String file = request.getParameter("contentType");
        map.put(S2SConstants.PROPOSAL_NUMBER, phsHumanSubjectBean.getProposalNumber());
        map.put(S2SConstants.PHS_HUMANSUBJECT_ATTMNT_NUMBER, "" + attachmentNumber);
        map.put(S2SConstants.PHS_ATTMNT_TYPE, "" + attachmentType);
        map.put(S2SConstants.FILE, file);
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.s2s.phshumansubject.PHSHumanSubjectS2SDocReader");
        map.put("isPhsHumanForm", "N");

        documentBean.setParameterMap(map);

        String docId = DocumentIdGenerator.generateDocumentId();

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("/StreamingServlet");
        stringBuffer.append("?");
        stringBuffer.append(DocumentConstants.DOC_ID);
        stringBuffer.append("=");
        stringBuffer.append(docId);
        session.setAttribute(docId, documentBean);

        String templateURL = stringBuffer.toString();
        session.setAttribute("url", templateURL);
        return templateURL;
    }

    private PHSHumanSubjectsBean getPHSHumanAndClinicalInfo(HttpServletRequest request, PHSHumanSubjectsBean phsHumanSubjectsBean) throws Exception {
        HttpSession session = request.getSession();
        s2sTxnBean = new S2STxnBean();
        String proposalNumber = request.getParameter("proposalNumber");
        if (proposalNumber == null || "".equals(proposalNumber)) {
            proposalNumber = (String) session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER + session.getId());
        }
        proposalBean = proposalDevTxnBean.getProposalDevelopmentDetails(proposalNumber);
        ProposalSpecialReviewFormBean specialReviewBean;
        int approvalCode = 0;
        String protocolNumber = null;
        List<String> exemptionNumberList = new ArrayList<String>();
        String description;

        phsHumanSubjectsBean.setIsHuman(N_NO);
        Vector vecSpecialReview = proposalBean.getPropSpecialReviewFormBean();
        if (vecSpecialReview != null) {
            for (int vecCount = 0; vecCount < vecSpecialReview.size(); vecCount++) {
                specialReviewBean = (ProposalSpecialReviewFormBean) vecSpecialReview.get(vecCount);

                switch (specialReviewBean.getSpecialReviewCode()) {
                    case 1:
                        phsHumanSubjectsBean.setIsHuman(Y_YES);
                        break;
                    default:
                        break;
                }   //switch
                phsHumanSubjectsBean.setExemptFedReg(N_NO);
                HashMap hmAppCode = new HashMap();
                hmAppCode = s2sTxnBean.get_specRev_app_code(proposalNumber, 1, specialReviewBean.getApprovalCode());

                approvalCode = Integer.parseInt(hmAppCode.get("APPROVAL_CODE").toString());
                String linkEnabled = (hmAppCode.get("LINK_ENABLED") != null ? (String) hmAppCode.get("LINK_ENABLED").toString() : "");
                description = specialReviewBean.getComments();
                if (linkEnabled.equals("1")) {
                    if (hmAppCode.get("PROTOCOL_NUMBER") != null) {
                        protocolNumber = (String) hmAppCode.get("PROTOCOL_NUMBER").toString();
                    }
                }
                if (approvalCode == 4) {
                    if (linkEnabled.equals("1")) {
                        String exemptionNumbers = null;
                        HashMap hmExempt = new HashMap();
                        Vector vExempt = s2sTxnBean.getExemptionNumbers(protocolNumber);
                        int listSize = vExempt.size();

                        if (listSize > 0) {
                            for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
                                hmExempt = (HashMap) vExempt.elementAt(rowIndex);
                                exemptionNumbers = (String) hmExempt.get("EXEMPTION_NUMBERS").toString();
                                exemptionNumberList.add(exemptionNumbers);
                            }
                            phsHumanSubjectsBean.setExemptionNumberList(exemptionNumberList);
                        }
                    } else {
                        if (description != null) {
                            //parse description for exemption numbers separated by commas. 
                            //valid values are E1,E2,E3,E4,E5,E6

                            String[] exemptions = description.split(",");
                            String exemptionNumbers;
                            for (int i = 0; i < exemptions.length; i++) {
                                exemptionNumbers = exemptions[i].trim();
                                exemptionNumberList.add(exemptionNumbers);
                            }
                            phsHumanSubjectsBean.setExemptionNumberList(exemptionNumberList);
                        }
                    }
                    //new for version 1.2
                    phsHumanSubjectsBean.setExemptFedReg(Y_YES);
                }
            }//for
        } //if
        return phsHumanSubjectsBean;
    }

    private PHSHumanSubjectsBean getPHSHumanSubjectHeaderDetails(HttpServletRequest request, PHSHumanSubjectsBean phsHumanSubjectsBean) throws Exception {
        String proposalNumber = "";
        proposalNumber = (String) request.getSession().getAttribute("proposalNumber" + request.getSession().getId());
        HttpSession session = request.getSession();
        PHSHumanSubjectTxnBean phsHumanSubjectTxnBean = new PHSHumanSubjectTxnBean();
        phsHumanSubjectsBean = phsHumanSubjectTxnBean.getPHSHumanSubjectHeaderDetails(proposalNumber, phsHumanSubjectsBean);
        return phsHumanSubjectsBean;
    }

    private String savePHSHumanSubHeaderDetails(ActionForm actionForm, UserInfoBean userInfoBean, HttpServletRequest request) throws Exception {
        PHSHumanSubjectsBean phsHumanSubjectsBean = (PHSHumanSubjectsBean) actionForm;
        PHSHumanSubjectTxnBean phsHumanSubjectTxnBean = new PHSHumanSubjectTxnBean();
        if(phsHumanSubjectsBean != null && phsHumanSubjectsBean.getIsInvolveHumanSpecimen() != null){
            phsHumanSubjectTxnBean.savePHSHumanSubHeaderDetails(phsHumanSubjectsBean, userInfoBean);
        } 
        return "success";
    }

    private List<PHSHumanSubjectAttachments> getPHSHumanandOthrReqAttchmnts(HttpServletRequest request, PHSHumanSubjectsBean phsHumanSubjectsBean)
            throws Exception {
        HttpSession session = request.getSession();
        String proposalNumber = request.getParameter("proposalNumber");
        List<PHSHumanSubjectAttachments> phsHumanSubjectAttachmentList = null;
        if (proposalNumber == null || "".equals(proposalNumber)) {
            proposalNumber = (String) session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER + session.getId());
        }
        PHSHumanSubjectTxnBean phsHumanSubjectTxnBean = new PHSHumanSubjectTxnBean();
        phsHumanSubjectAttachmentList = phsHumanSubjectTxnBean.getPHSHumanandOthrReqAttchmnts(proposalNumber);
        String showOtherAttachment = "false";
        String showAddAttachment = "false";
        for (PHSHumanSubjectAttachments phsHumanSubjectAttachments : phsHumanSubjectAttachmentList) {
            if (phsHumanSubjectAttachments.getPhsHumnsubjtAttachmentType() == 2) {
                showOtherAttachment = "true";
            } else if (phsHumanSubjectAttachments.getPhsHumnsubjtAttachmentType() == 1) {
                showAddAttachment = "true";
            }

//           showOtherAttachment =  phsHumanSubjectAttachments.getPhsHumnsubjtAttachmentType() == 2 ? "true" : "false";
//           showAddAttachment =  phsHumanSubjectAttachments.getPhsHumnsubjtAttachmentType() == 1 ? "true" : "false";
//           if(showOtherAttachment.equals("true")) {
//               break;         
//           }
        }
        request.setAttribute("showOtherAttchment", showOtherAttachment);
        request.setAttribute("showAddAttachment", showAddAttachment);
        return phsHumanSubjectAttachmentList;
    }

    private List<PHSHumanSubjectDelayedStudyBean> getDelayedOnsetStudy(HttpServletRequest request, PHSHumanSubjectsBean phsHumanSubjectsBean)
            throws Exception {
        HttpSession session = request.getSession();
        String proposalNumber = request.getParameter("proposalNumber");
        List< PHSHumanSubjectDelayedStudyBean> phsHumnSubjtDlyStdyList = null;
        if (proposalNumber == null || "".equals(proposalNumber)) {
            proposalNumber = (String) session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER + session.getId());
        }
        PHSHumanSubjectTxnBean phsHumanSubjectTxnBean = new PHSHumanSubjectTxnBean();
        phsHumnSubjtDlyStdyList = phsHumanSubjectTxnBean.getgetDelayedOnsetStudy(proposalNumber);

        return phsHumnSubjtDlyStdyList;
    }

    private String saveDelayedOnsetStudies(ActionForm actionForm, UserInfoBean userInfoBean, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        PHSHumanSubjectTxnBean phsHumanSubjectTxnBean = new PHSHumanSubjectTxnBean(userInfoBean.getUserId());
        PHSHumanSubjectsBean phsHumnSubjtsBean = (PHSHumanSubjectsBean) actionForm;
        String acType = phsHumnSubjtsBean.getAcType();
        String proposalNumber = (String) request.getSession().getAttribute("proposalNumber" + request.getSession().getId());
        if(phsHumnSubjtsBean.getStudyNumber() == null){
           phsHumnSubjtsBean.setAcType(TypeConstants.INSERT_RECORD); 
        }else if(phsHumnSubjtsBean.getAcType() == null){
         phsHumnSubjtsBean.setAcType(TypeConstants.INSERT_RECORD);
         savePHSHumanSubHeaderDetails(actionForm, userInfoBean, request);
      } 
         if (acType != null && "I".equals(acType)) {
                   phsHumnSubjtsBean.setAcType(TypeConstants.INSERT_RECORD);
                   phsHumnSubjtsBean.setStudyNumber(0);
                   phsHumnSubjtsBean.setUpdateUser(userInfoBean.getUserId());
                   if(phsHumnSubjtsBean.getIsAnticipatedCt() == null || phsHumnSubjtsBean.getIsAnticipatedCt().isEmpty()){
                       phsHumnSubjtsBean.setIsAnticipatedCt("N");
                   }
                   if(phsHumnSubjtsBean.getDelayedFormFile() != null){
                       phsHumanSubjectTxnBean.saveDelayedOnsetStudyDetails(phsHumnSubjtsBean);
                   } 
                   
         }else if (acType != null && "U".equals(acType)) {
                   phsHumnSubjtsBean.setAcType(TypeConstants.UPDATE_RECORD);
//                   phsHumnSubjtsBean.setStudyNumber((Integer) session.getAttribute("studyNumber"));
                   phsHumnSubjtsBean.setUpdateUser(userInfoBean.getUserId());
                   if(phsHumnSubjtsBean.getIsAnticipatedCt() == null || phsHumnSubjtsBean.getIsAnticipatedCt().isEmpty()){
                       phsHumnSubjtsBean.setIsAnticipatedCt("N");
                   }
                   if(phsHumnSubjtsBean.getDelayedFormFile() != null){
                       phsHumanSubjectTxnBean.saveDelayedOnsetStudyDetails(phsHumnSubjtsBean);
                   } 
                   
         }else if(acType != null && "D".equals(acType)){
             phsHumnSubjtsBean.setUpdateUser(userInfoBean.getUserId());
             if(phsHumnSubjtsBean.getIsAnticipatedCt() == null || phsHumnSubjtsBean.getIsAnticipatedCt().isEmpty()){
                   phsHumnSubjtsBean.setIsAnticipatedCt("N");
             }
             phsHumanSubjectTxnBean.saveDelayedOnsetStudyDetails(phsHumnSubjtsBean);
         }        
         phsHumnSubjtsBean.setAcType("");
         phsHumnSubjtsBean.setStudyTitle("");
         phsHumnSubjtsBean.setIsAnticipatedCt("");

        return "success";
    }

    private String saveHumanOtherAttachment(ActionForm actionForm, UserInfoBean userInfoBean, HttpServletRequest request) throws DBException, CoeusException, Exception {
        HttpSession session = request.getSession();
        PHSHumanSubjectTxnBean phsHumanSubjectTxnBean = new PHSHumanSubjectTxnBean(userInfoBean.getUserId());
        PHSHumanSubjectsBean phsHumnSubjtsBean = (PHSHumanSubjectsBean) actionForm;
        String acType = phsHumnSubjtsBean.getAcType();
        String proposalNumber = (String) request.getSession().getAttribute("proposalNumber" + request.getSession().getId());
        if (!acType.isEmpty()) {
            if (acType.equalsIgnoreCase("I")) {
                phsHumnSubjtsBean.setAttachmentNumber(0);
            }
            phsHumnSubjtsBean.setUpdateUser(userInfoBean.getUserId());
            phsHumanSubjectTxnBean.saveHumanAttachment(phsHumnSubjtsBean);
            //}else if (!acType.isEmpty() && acType.equalsIgnoreCase("N") && phsHumnSubjtsBean.getAttachmentAcType().equals(2)) {
            // phsHumanSubjectTxnBean.saveOtherAttachment(phsHumnSubjtsBean);
        } else if (acType.isEmpty()) {
            phsHumnSubjtsBean.setAcType(TypeConstants.INSERT_RECORD);
            savePHSHumanSubHeaderDetails(actionForm, userInfoBean, request);
            phsHumnSubjtsBean.setAttachmentNumber(0);
            phsHumnSubjtsBean.setUpdateUser(userInfoBean.getUserId());
            phsHumanSubjectTxnBean.saveHumanAttachment(phsHumnSubjtsBean);
        }
        if (acType != null && "I".equals(acType)) {
            phsHumnSubjtsBean.setAcType(TypeConstants.INSERT_RECORD);
            phsHumnSubjtsBean.setProposalNumber(proposalNumber);           
 if (phsHumnSubjtsBean.getPhsHumanSubjectAttList() != null || phsHumnSubjtsBean.getAttachmentNumber() == null) {                phsHumnSubjtsBean.setAttachmentNumber(0);
                phsHumnSubjtsBean.setAcType(TypeConstants.INSERT_RECORD);
                phsHumnSubjtsBean.setUpdateUser(userInfoBean.getUserId());
                if (phsHumnSubjtsBean.getPhsHumnsubjtAttachmentType().equals(1)) {
                    if (phsHumnSubjtsBean.getHumanSbjtFormFile() != null && !phsHumnSubjtsBean.getHumanSbjtFormFile().equals(EMPTY_STRING)) {
                        phsHumanSubjectTxnBean.saveHumanAttachment(phsHumnSubjtsBean);
                    }
                } else if (phsHumnSubjtsBean.getPhsHumnsubjtAttachmentType().equals(2)) {
                    if (phsHumnSubjtsBean.getOtherAttmntFormFile() != null && !phsHumnSubjtsBean.getOtherAttmntFormFile().equals(EMPTY_STRING)) {
                        //   phsHumanSubjectTxnBean.saveOtherAttachment(phsHumnSubjtsBean); 
                    }
                }

            }
             if(phsHumnSubjtsBean.getPhsHumnsubjtAttachmentType().equals(1)){   
            phsHumanSubjectTxnBean.savePHSHumanSubHeaderDetails(phsHumnSubjtsBean, userInfoBean); //update human specimen
             }
        } else if (acType != null && "U".equals(acType)) {
            phsHumnSubjtsBean.setAcType(TypeConstants.UPDATE_RECORD);
            phsHumnSubjtsBean.setUpdateUser(userInfoBean.getUserId());
            if (phsHumnSubjtsBean.getPhsHumnsubjtAttachmentType().equals(1)) {
                if (phsHumnSubjtsBean.getHumanSbjtFormFile() != null && !phsHumnSubjtsBean.getHumanSbjtFormFile().equals(EMPTY_STRING)) {
                    phsHumanSubjectTxnBean.saveHumanAttachment(phsHumnSubjtsBean);
                }
            } else if (phsHumnSubjtsBean.getPhsHumnsubjtAttachmentType().equals(2)) {
                if (phsHumnSubjtsBean.getOtherAttmntFormFile() != null && !phsHumnSubjtsBean.getOtherAttmntFormFile().equals(EMPTY_STRING)) {
                    //  phsHumanSubjectTxnBean.saveOtherAttachment(phsHumnSubjtsBean); 
                }
            }
        } else if (acType != null && "D".equals(acType)) {
            phsHumnSubjtsBean.setAcType((TypeConstants.DELETE_RECORD));
            phsHumnSubjtsBean.setUpdateUser(userInfoBean.getUserId());
            if (phsHumnSubjtsBean.getPhsHumnsubjtAttachmentType().equals(1)) {
                phsHumanSubjectTxnBean.saveHumanAttachment(phsHumnSubjtsBean);
            } else if (phsHumnSubjtsBean.getPhsHumnsubjtAttachmentType().equals(2)) {
                //  phsHumanSubjectTxnBean.saveOtherAttachment(phsHumnSubjtsBean);    
            }
        }
        phsHumnSubjtsBean.setAcType("");
        return "success";
    }

    private String downloadHumanSubjectForm(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, IOException {
        try {
            String filePath = fetchParameterValue(request,"PHS_HUMAN_SUBJECT_FORM_PATH"); 
            File DOWNLOAD_PATH_HUMAN_SUBJECT = new File(filePath);
            FileInputStream inStream = new FileInputStream(DOWNLOAD_PATH_HUMAN_SUBJECT);
            String mimeType = "application/pdf";
            System.out.println("MIME type: " + mimeType);
            response.setContentType(mimeType);
            response.setContentLength((int) DOWNLOAD_PATH_HUMAN_SUBJECT.length());
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", DOWNLOAD_PATH_HUMAN_SUBJECT.getName());
            response.setHeader(headerKey, headerValue);
            OutputStream outStream = response.getOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            inStream.close();
            outStream.close();
        } catch (FileNotFoundException f) {
            request.setAttribute("phsHumanSubjectFileNotFound", true);
            return "success";
        } catch (Exception e) {
            request.setAttribute("phsHumanSubjectFileError", true);
            return "success";
        }
        return null;
    }

    private boolean hasProposalRight(HttpSession session) throws Exception {
        EPSProposalHeaderBean epsProposalHeaderBean = (EPSProposalHeaderBean) session.getAttribute("epsProposalHeaderBean");
        if (epsProposalHeaderBean != null) {
            boolean hasModifyRight = hasModifyProposalRights(epsProposalHeaderBean, session);
            if (!hasModifyRight) {
                boolean hasViewRight = hasDisplayProposalRights(epsProposalHeaderBean, session);
                session.setAttribute("mode" + session.getId(), "display");
                if (!hasViewRight) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This method checks whether the user has rights to modify a particular
     * proposal returns true if the user has modify rights
     */
    private boolean hasModifyProposalRights(EPSProposalHeaderBean epsProposalHeaderBean,
            HttpSession session) throws Exception {
        boolean hasRights = false;
        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
        UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user" + session.getId());
        String userId = userInfoBean.getUserId().toUpperCase();
        String unitNumber = (String) epsProposalHeaderBean.getUnitNumber();
        String proposalNumber = (String) epsProposalHeaderBean.getProposalNumber();
        hasRights = userMaintDataTxnBean.getUserHasRight(userId, MODIFY_ANY_PROPOSAL_RIGHT, unitNumber);
        if (!hasRights) {
            hasRights = userMaintDataTxnBean.getUserHasProposalRight(userId, proposalNumber, MODIFY_PROPOSAL_RIGHT);
        }
        if (!hasRights) {
            hasRights = userMaintDataTxnBean.getUserHasProposalRight(userId, proposalNumber, MODIFY_NARRATIVE_RIGHT);
        }
        if (!hasRights) {
            hasRights = userMaintDataTxnBean.getUserHasProposalRight(userId, proposalNumber, MODIFY_BUDGET_RIGHT);
        }
        return hasRights;

    }

    /**
     * This method checks whether the user has rights to view a particular
     * proposal returns true if the user has view rights
     */
    private boolean hasDisplayProposalRights(EPSProposalHeaderBean epsProposalHeaderBean,
            HttpSession session) throws Exception {
        UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user" + session.getId());
        String userId = userInfoBean.getUserId().toUpperCase();
        String unitNumber = (String) epsProposalHeaderBean.getUnitNumber();
        String proposalNumber = (String) epsProposalHeaderBean.getProposalNumber();
        String statusCode = (String) epsProposalHeaderBean.getProposalStatusCode();
        boolean hasRights = false;
        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
        UserDetailsBean userDetailsBean = new UserDetailsBean();
        int rightExists = userDetailsBean.getUserHasAnyOSPRights(userId);
        if (rightExists == 1) {
            if (statusCode.equals("2") || statusCode.equals("4") || statusCode.equals("5")
                    || statusCode.equals("6") || statusCode.equals("7")) {
                hasRights = true;
            }
        }
        if (!hasRights) {
            hasRights = userMaintDataTxnBean.getUserHasRight(userId, VIEW_ANY_PROPOSAL_RIGHT, unitNumber);
        }
        if (!hasRights) {
            hasRights = userMaintDataTxnBean.getUserHasProposalRight(userId, proposalNumber, VIEW_RIGHT);
        }

        if (!hasRights) {
            String principalInvName = epsProposalHeaderBean.getPersonName();
            String loggedUser = userInfoBean.getUserName();
            if ((loggedUser != null && loggedUser.equals(principalInvName))) {
                hasRights = true;
            }
        }
        return hasRights;
    }

    private void syncFromSpecialReviewData(HttpServletRequest request, PHSHumanSubjectsBean phsHumanSubjectsBean) {
        try {
            HttpSession session = request.getSession();
            UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user" + session.getId());
            PHSHumanSubjectTxnBean phsHumanSubjectTxnBean = new PHSHumanSubjectTxnBean();
            phsHumanSubjectsBean.setHeaderAcType("U");
            phsHumanSubjectTxnBean.savePHSHumanSubHeaderDetails(phsHumanSubjectsBean, userInfoBean);
        } catch (Exception e) {
        }
    }    

    private void checkDisplayMode(EPSProposalHeaderBean proposalHeaderBean,HttpSession session) {
        int creationStatusCode = Integer.parseInt(proposalHeaderBean.getProposalStatusCode());
        session.setAttribute("mode"+session.getId(),EMPTY_STRING);
         switch (creationStatusCode) {
                    case PROPOSAL_APPROVAL_IN_PROGRESS:                                   
                    session.setAttribute("mode"+session.getId(),"display");
                    break;
                    case PROPOSAL_APPROVED:                       
                    session.setAttribute("mode"+session.getId(),"display");

                    break;
                    case PROPOSAL_SUBMITTED:                                                                
                    session.setAttribute("mode"+session.getId(),"display");                                  
                    break;

                    case PROPOSAL_POST_SUB_APPROVAL:                                                        
                    session.setAttribute("mode"+session.getId(),"display");                                   
                    break;

                    case PROPOSAL_POST_SUB_REJECTION:                              
                    session.setAttribute("mode"+session.getId(),"display");
                    break;
        }
    }
}
