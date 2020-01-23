/*
 * UploadAttachments.java
 *
 * Created on July 3, 2006, 10:20 AM
 */

/* PMD check performed, and commented unused imports and variables on 04-August-2011
 * by Maharaja Palanichamy
 */

package edu.utk.coeuslite.propdev.action;

//import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.MailActions;
import edu.mit.coeus.propdev.bean.InboxBean;
import edu.mit.coeus.propdev.bean.MedusaTxnBean;
import edu.mit.coeus.propdev.bean.MessageBean;
import edu.mit.coeus.propdev.bean.NotepadBean;
import edu.mit.coeus.propdev.bean.ProposalActionTxnBean;
import edu.mit.coeus.propdev.bean.ProposalActionUpdateTxnBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalInvestigatorFormBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeModuleUsersFormBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.propdev.bean.ProposalPersonBioPDFBean;
import edu.mit.coeus.propdev.bean.ProposalPersonTxnBean;
import edu.mit.coeus.routing.bean.RoutingBean;
import edu.mit.coeus.routing.bean.RoutingDetailsBean;
import edu.mit.coeus.routing.bean.RoutingMapBean;
import edu.mit.coeus.routing.bean.RoutingTxnBean;
import edu.mit.coeus.routing.bean.RoutingUpdateTxnBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.documenttype.DocumentTypeChecker;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.ComboBoxBean;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.utk.coeuslite.propdev.bean.DocumentTypeBean;
import edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean;
import edu.utk.coeuslite.propdev.bean.NarrativeTypeBean;
import edu.utk.coeuslite.propdev.form.ProposalUploadForm;
import java.sql.Timestamp;
//import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 *
 * @author noorula
 */
public class UploadAttachmentAction extends ProposalBaseAction {
    
    private static final String NARRATIVE_TYPE_DATA = "getNarrativeTypeData";
    
    private static final String PERSONNEL_DOCUMENT_STATEMENT = "getPersonnelNarrativeTypeDocumentData";
    private static final String PERSONNEL_NARRATIVE_TYPE_DATA = "getPersonnelNarrativeTypeData";
    private static final String PERSONNEL_PERSONS = "getPersonnelPersonsData";
    private static final String PERSONNEL_NARRATIVE_TYPE = "personnelNarrativeType";
    
    private static final String SUCCESS = "success";
    private static final String PAGE_PARAMETER ="page";
    private static final String PERSONNEL_DOCUMENT_TYPE = "I";
    private static final String DELETE_ACTION = "D";
    private static final String MODULE_STATUS_CODE = "C";
    private static final String SUBMIT = "S";
    private static final String AC_TYPE = "acType";
//    private static final String UPLOAD_ATTACHMENTS_CODE = "P009";
    private static final String PROPOSAL_NUMBER = "proposalNumber";
    private static final String PERSON_ID = "personId";
    private static final String VECTOR_ATTACHMENTS_DATAS = "vecAttachmentsData";
    private static final String MAX_BIO_NUM = "maxBioNumber";
    private static final String UPLOAD_ATTACHMENTS = "/upLoadProposal";
    private static final String UPLOAD_ATTACHMENTS_SUBMIT = "/upLoadProposalAction";
    private static final String UPLOAD_ATTACHMENTS_DELETE_VIEW = "/upLoadProposalRemoveView";
    //Case#4572 - Writing notepad entries and sending notifications irrespective of proposal status - Start
    private static final String APPROVEL_IN_PROGRESS = "2";
    //Case#4572 - End
    /** Creates a new instance of UploadAttachments */
    public UploadAttachmentAction() {
    }
    
    /**
     * Method to perform action
     * @param actionMapping instance of ActionMapping
     * @param actionForm instance of ActionForm
     * @param request instance of Request
     * @param response instance of Response
     * @throws Exception if exception occur
     * @return instance of ActionForward
     */  
    public ActionForward performExecute(ActionMapping actionMapping,
    ActionForm actionForm,
    HttpServletRequest request,
    HttpServletResponse response) throws Exception {
        ProposalUploadForm proposalUploadForm = (ProposalUploadForm)actionForm;
        //UploadForm uploadForm = (UploadForm)actionForm;
        HttpSession session = request.getSession();
        Map mapMenuList = new HashMap();
        Timestamp dbTimestamp = prepareTimeStamp();
        mapMenuList.put("menuItems",CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
        mapMenuList.put("menuCode",CoeusliteMenuItems.UPLOAD_ATTACHMENTS_CODE); 
        setSelectedMenuList(request, mapMenuList);
//        setSelectedStatusMenu(UPLOAD_ATTACHMENTS_CODE);
        String proposalNumber =(String) session.getAttribute(PROPOSAL_NUMBER+session.getId());
        String page = (String) request.getParameter(PAGE_PARAMETER);
        proposalUploadForm.setProposalNumber(proposalNumber);
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String acType = request.getParameter(AC_TYPE);
        boolean isValidVlaue = true;        
        //Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - Start
        session.removeAttribute("propPersonnelVisible");               
        String message= "";
        //Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - End
        if(acType!=null && acType.equals("E")){
            //Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress  - Start          
            EPSProposalHeaderBean headerBean = (EPSProposalHeaderBean)session.getAttribute("epsProposalHeaderBean");
            String statusCode = null;
            if(headerBean!= null){
                statusCode = headerBean.getProposalStatusCode();
                if((statusCode.equals("2") || statusCode.equals("4")) &&
                       (page!=null && !page.equals(EMPTY_STRING)&&page.equals(PERSONNEL_DOCUMENT_TYPE))){
                     setEditDetails(request, proposalUploadForm); 
                    getPropPersonnelRights(request, userInfoBean.getUserId(),acType,proposalUploadForm);
                }else{
                    setEditDetails(request, proposalUploadForm);
                }
            }            
//            setEditDetails(request, proposalUploadForm);
            //Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - End
            return actionMapping.findForward(SUCCESS);
        }
        /**Initially the form will load the Proposal Attachments as Default
         *For personnel atachments the request parameter will be "I"
         *For Instituational attachments the request parameter will be "O"
         */
         //COEUS-678
            EPSProposalHeaderBean proposalheaderBean = (EPSProposalHeaderBean)session.getAttribute("epsProposalHeaderBean");
            String proposalstatusCode = null;
            if( proposalheaderBean != null){
                proposalstatusCode = proposalheaderBean.getProposalStatusCode();
            }
         //COEUS-678
        if(UPLOAD_ATTACHMENTS.equals(actionMapping.getPath())) {
            Vector vecNarrativeType = getNarrativeType(request);           
            if(page!=null && !page.equals(EMPTY_STRING)){
                proposalUploadForm.setNarrativeTypeGroup(page);
                if(!page.equals(PERSONNEL_DOCUMENT_TYPE)){
                    vecNarrativeType = getPropNarrativeDocument(vecNarrativeType,page);
                    session.setAttribute("vecDocumentType" , vecNarrativeType);
                    //Added for COEUSQA-2697-Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-start
                    session.setAttribute("status","LOAD");
                    //Added for COEUSQA-2697-Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-end
                }               
                
            }
        } else if(UPLOAD_ATTACHMENTS_SUBMIT.equals(actionMapping.getPath())) {
             //COEUS-678
            //save enable for proposal status :In progress, Rejected, Appoval In progress, Approved ,Recalled 
            if( session.getAttribute("mode"+session.getId()) != "display"
                || ( proposalstatusCode != null 
                        && proposalstatusCode.equals("2") //Approval in progress
                        || proposalstatusCode.equals("4") //Approved
                    ) 
             ){
                /*Added for COEUSQA-3998 check whether the proposalNumber to save and active proposal number are same*/
                String currentProposalNumberForSave = request.getParameter("currentProposalNumber");
                if(currentProposalNumberForSave != null && !currentProposalNumberForSave.equals("") && currentProposalNumberForSave.equals(proposalNumber) ) {
                
            proposalUploadForm.setAwUpdateUser(proposalUploadForm.getUpdateUser());
            proposalUploadForm.setUpdateUser(userInfoBean.getUserId());
            proposalUploadForm.setProposalNumber(proposalNumber);
            //Added For the Case # COEUSQA-1679-Modification for final flag indicator -start
            //proposalUploadForm.setModuleStatusCode(MODULE_STATUS_CODE);
            //set MODULE_STATUS_CODE value from the form
            String moduleStatusCode = proposalUploadForm.getModuleStatusCode();
            if("on".equalsIgnoreCase(moduleStatusCode)){
                proposalUploadForm.setModuleStatusCode(MODULE_STATUS_CODE);
            }else{
                proposalUploadForm.setModuleStatusCode("I");
            }
            //Added For the Case # COEUSQA-1679-Modification for final flag indicator -end
            //Added for case#3450 - Displaying description instead of file name of the biosketch
            proposalUploadForm.setDescription(proposalUploadForm.getDescription());
            String replacedFileName = proposalUploadForm.getFileName();
            if(proposalUploadForm.getAcType().equalsIgnoreCase(TypeConstants.INSERT_RECORD) ||
                    proposalUploadForm.getAcType().equalsIgnoreCase(TypeConstants.UPDATE_RECORD)) {
                proposalUploadForm.setUpdateTimestamp(dbTimestamp.toString());
                if(proposalUploadForm.getDocument()!=null && !proposalUploadForm.getDocument().equals(EMPTY_STRING)){
                    proposalUploadForm.setFileName(proposalUploadForm.getDocument().getFileName());
                }
                if(page.equals(PERSONNEL_DOCUMENT_TYPE)){
                    boolean isValid = true;
                    if(!proposalUploadForm.getAcType().equals(TypeConstants.UPDATE_RECORD)){                    
                        int bioNumber = getMaxNarrativeBioNumber(proposalUploadForm.getProposalNumber(),
                                            proposalUploadForm.getPersonId(), request);
                        proposalUploadForm.setBioNumber(bioNumber);                        
                    }
                    //Moved for case#3450
                    if(acType != null && !acType.equals("U")){
                        isValid = validatePersonnelAttachments(proposalUploadForm, request);
                    }
                    if(isValid){
                        if(isLockExistsForSave(request)){
                            addUpdPersonnelNarrative(proposalUploadForm, request);
                            addUpdPersonnelNarrPDF(proposalUploadForm, request);
                            // Update the proposal hierarchy sync flag
                            updateProposalSyncFlags(request, proposalNumber);
                            //Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - Start
                                EPSProposalHeaderBean headerBean = (EPSProposalHeaderBean)session.getAttribute("epsProposalHeaderBean");
                                
                                String status = headerBean.getProposalStatusCode();
                                if(status.equals("2") || status.equals("4")){
                                    if(replacedFileName != null && !replacedFileName.trim().equals("")){
                                        message = "File  '"+replacedFileName+"'  has been replaced by the user "+userInfoBean.getUserName();    
                                        //Added for COEUSDEV-340 :Email that is generated when a narrative is changes does not have proposal and narrative details - start
                                        String uploadStatusCode  = proposalUploadForm.getUploadStatusCode();
                                        Vector vecDocumentType = (Vector)session.getAttribute("vecDocumentType");
                                        // Modified for COEUSQA-2416 : Error updating personnel attachments from lite after a proposal is submitted for approval - Start
//                                        message = message+"\r\n\r\n\r\n\r\n\r\nNarrative Type:          "+getNarrativeTypeDescription(vecDocumentType,uploadStatusCode);
                                        message = message+"\r\n\r\n\r\n\r\n\r\nNarrative Type:          "+getAttachmentTypeDescription(vecDocumentType,uploadStatusCode);
                                        // Modified for COEUSQA-2416 : Error updating personnel attachments from lite after a proposal is submitted for approval - End
                                        message = message+"\r\nModule Description:      "+proposalUploadForm.getDescription();
                                        //COEUSDEV-340 : End
                                        notification( userInfoBean.getUserId(),request,message);
                                    }
                             }
                            //Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - End
                            String mode = (String) session.getAttribute("mode"+session.getId());
                            if(mode!=null && mode.equals("display")){
                                LockBean lockBean = getLockingBean(userInfoBean, 
                                    (String)session.getAttribute(PROPOSAL_NUMBER+session.getId()), request);
                                releaseLock(lockBean, request);
                            }                                
                        }
                    } else {
                        isValidVlaue = false;
                    }
                }else {
                    if(!proposalUploadForm.getAcType().equals(TypeConstants.UPDATE_RECORD)){
                        int moduleNumber = getMaxNarrativeModuleNumber(proposalNumber,request);
                        proposalUploadForm.setModuleNumber(moduleNumber);
                    }
                    if(validateNarrativeAttachments(proposalUploadForm, request)){
                        if(isLockExistsForSave(request)){                            
                            addUpdProposalNarrative(proposalUploadForm,request);
                            addUpdProposalNarrPDF(proposalUploadForm);
                            //Added for COEUSQA-2697-Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-start
                            session.setAttribute("status","F");
                            //Added for COEUSQA-2697-Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-end
                           // COEUSDEV-308: Application not checking module level rights for a user in Narrative
                           // Insert roles only if a new narrative is being added.
                           if(!proposalUploadForm.getAcType().equals(TypeConstants.UPDATE_RECORD)){
                                addNarrativeRightsForProp(proposalUploadForm, request);
                           }
                            // Update the proposal hierarchy sync flag
                            updateProposalSyncFlags(request, proposalNumber);
                            //Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - Start
                            // sending email, inbox msg and notepad entry  
                                String file = null;
                                if(proposalUploadForm.getDocument() != null){
                                    file = proposalUploadForm.getDocument().getFileName();
                                } else {
                                    file = proposalUploadForm.getFileName();
                                }
                                if(file != null && !file.trim().equals("")){
                                        int index = file.lastIndexOf('.');
                                        if(index != -1 && index != file.length()){
                                            String extension = file.substring(index+1,file.length());
                                            //Modified for COEUSDEV-340 : Email that is generated when a narrative is changes does not have proposal and narrative details - Start
//                                            String msgNarr = "A new "+extension+" file is uploaded for proposal \'"+proposalNumber+ "\' for Module No."+proposalUploadForm.getModuleNumber();
//                                            message = message+ msgNarr;
                                            Properties messagesBundle = (Properties)getObjectsFromBundle();
                                            String msgNarr = messagesBundle.getProperty("proposal_narr_document_change");
                                            msgNarr = edu.mit.coeus.utils.Utils.replaceString(msgNarr,"{0}",extension);
                                            msgNarr = edu.mit.coeus.utils.Utils.replaceString(msgNarr,"{1}",proposalNumber);
                                            msgNarr = edu.mit.coeus.utils.Utils.replaceString(msgNarr,"{2}",proposalUploadForm.getModuleNumber()+EMPTY_STRING);
                                            message = message+msgNarr;
                                            
                                            Vector vecDocumentType = (Vector)session.getAttribute("vecDocumentType");
                                            String uploadStatusCode  = proposalUploadForm.getUploadStatusCode();
                                            message = message+"\r\n\r\n\r\n\r\n\r\nNarrative Type:          "+getNarrativeTypeDescription(vecDocumentType,uploadStatusCode);
                                            message = message+"\r\nModule Description:      "+proposalUploadForm.getDescription();
                                            //COEUSDEV-340 : End
                                        }
                                }
                            notification( userInfoBean.getUserId(),request,message);
                            //Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - End
                            String mode = (String) session.getAttribute("mode"+session.getId());
                            if(mode!=null && mode.equals("display")){
                                LockBean lockBean = getLockingBean(userInfoBean, 
                                    (String)session.getAttribute(PROPOSAL_NUMBER+session.getId()), request);
                                releaseLock(lockBean, request);
                            }
                        }
                    } else {
                        isValidVlaue = false;
                    }
                }
            }
        }/*Added for COEUSQA-3998- end if for proposal number check*/ 
            }
        } else if(UPLOAD_ATTACHMENTS_DELETE_VIEW.equals(actionMapping.getPath())) {
            //COEUS-678
            //delete enable for proposal status :In progress, Rejected, Appoval In progress, Approved ,Recalled 
            if( session.getAttribute("mode"+session.getId()) != "display"
                || ( proposalstatusCode != null 
                        && proposalstatusCode.equals("2") //Approval in progress
                        || proposalstatusCode.equals("4") //Approved
                    ) 
             ){ 
            proposalUploadForm.setUpdateUser(userInfoBean.getUserId());
            proposalUploadForm.setProposalNumber(proposalNumber);
            proposalUploadForm.setModuleStatusCode(MODULE_STATUS_CODE);
            proposalUploadForm.setAwUpdateUser(proposalUploadForm.getUpdateUser());
            dbTimestamp = prepareTimeStamp();
            proposalUploadForm.setAcType(acType);
            if(proposalUploadForm.getAcType().equalsIgnoreCase(DELETE_ACTION)) {
                if(request.getParameter("number")!= null){
                int number = Integer.parseInt(request.getParameter("number"));
                proposalUploadForm.setAcType(DELETE_ACTION);                
                if(!page.equals(PERSONNEL_DOCUMENT_TYPE)) {
                    proposalUploadForm.setModuleNumber(number);
                    if(isLockExistsForSave(request)){
                        addUpdProposalNarrative(proposalUploadForm, request);   
                        // Update the proposal hierarchy sync flag
                        updateProposalSyncFlags(request, proposalNumber);                         
                    }
                } else {
                    proposalUploadForm.setBioNumber(number);
                    proposalUploadForm.setPersonId(request.getParameter(PERSON_ID));
                    if(isLockExistsForSave(request)){
                        deleteUpdPersonnelNarrative(proposalUploadForm, request);
                        // Update the proposal hierarchy sync flag
                        updateProposalSyncFlags(request, proposalNumber);                         
                    }
                }
                }
                
            }
        }
        }
        
        if(!page.equals(PERSONNEL_DOCUMENT_TYPE)){
            Vector vecNarrativeAttachment =  getNarrativeAttachMents(proposalNumber,
                                proposalUploadForm.getNarrativeTypeGroup(),request);
            session.setAttribute(VECTOR_ATTACHMENTS_DATAS, vecNarrativeAttachment);
        } else {
            getPersonnelNarrativeDocument(proposalUploadForm, request);
            getPropPersonnelRights(request, userInfoBean.getUserId(), "",proposalUploadForm);
        }
        // Update the save status
        readSavedStatus(request);
        getNarrativeRights(request, userInfoBean.getUserId());        
        if(isValidVlaue){
            proposalUploadForm.setPersonId("0");
            proposalUploadForm.setUploadStatusCode("0");
            proposalUploadForm.setDescription(EMPTY_STRING);
            proposalUploadForm.setAcType(EMPTY_STRING);
            proposalUploadForm.setFileName(EMPTY_STRING);
            //Added For the Case # COEUSQA-1679-Modification for final flag indicator -start
            proposalUploadForm.setModuleStatusCode("on");
            //Added For the Case # COEUSQA-1679-Modification for final flag indicator -end
        }
        return actionMapping.findForward(SUCCESS);
    }
    
    /** Check whether lock is available for save or not. If not, then 
     *show the message
     */
    private boolean isLockExistsForSave(HttpServletRequest request) throws Exception{
        String errMsg = EMPTY_STRING;
        boolean isLocked = true;
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        String mode = (String) session.getAttribute("mode"+session.getId());
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(PROPOSAL_NUMBER+session.getId()), request);
        boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
        LockBean lockData = getLockedData(CoeusLiteConstants.PROP_DEV_LOCK_STR+lockBean.getModuleNumber(), request);        
        if(isLockExists || !lockBean.getSessionId().equals(lockData.getSessionId())) {
            if(mode!=null && mode.equals("display")){
                isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
                if(isLockExists){
                    lockModule(lockBean, request);
                    return isLocked;
                }
            }
            errMsg = "release_lock_for";
            ActionMessages messages = new ActionMessages();
            messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
            saveMessages(request, messages);
            isLocked = false;
        }
        return isLocked;
    }
    
    /**
     * To get the personnel document type details from data base
     * @param proposalUploadForm
     * @throws Exception
     * return Vector
     */
    public Vector getPersonnelNarrativeDocument(ProposalUploadForm proposalUploadFormData,
        HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        Hashtable htDocumentType =
            (Hashtable)webTxnBean.getResults(request,PERSONNEL_NARRATIVE_TYPE,proposalUploadFormData);
        Vector vecPersonnelPersonDetails = (Vector)htDocumentType.get(PERSONNEL_DOCUMENT_STATEMENT);
        session.setAttribute("vecPersonnelPersonDetails", vecPersonnelPersonDetails);
        Vector vecDocumentType = (Vector)htDocumentType.get(PERSONNEL_PERSONS);
        session.setAttribute("vecDocumentType",vecDocumentType);
        htDocumentType = (Hashtable) webTxnBean.getResults(request,PERSONNEL_NARRATIVE_TYPE_DATA,proposalUploadFormData);
        Vector vecPersonnelAttachmentsData = (Vector)htDocumentType.get(PERSONNEL_NARRATIVE_TYPE_DATA);
        Vector vecAttachmentsData = new Vector();
        if(vecPersonnelAttachmentsData!=null && vecPersonnelAttachmentsData.size() > 0) {
            for(int count=0 ; count < vecPersonnelAttachmentsData.size(); count++){
                ProposalUploadForm proposalUploadForm = new ProposalUploadForm();
                proposalUploadForm =(ProposalUploadForm) vecPersonnelAttachmentsData.get(count);
                if(proposalUploadForm!=null) {
                    if(vecPersonnelPersonDetails!=null && vecPersonnelPersonDetails.size() > 0) {
                        for(int index=0 ; index < vecPersonnelPersonDetails.size(); index++){
                            ComboBoxBean comboBoxBean =(ComboBoxBean) vecPersonnelPersonDetails.get(index);
                            if(comboBoxBean.getCode().equals(proposalUploadForm.getPersonId())) {
                                proposalUploadForm.setPersonName(comboBoxBean.getDescription());
                            }
                        }
                    }
                }
                vecAttachmentsData.add(proposalUploadForm);
            }
        }
        session.setAttribute("vecPersonnelNarrativeDatas", vecPersonnelAttachmentsData);
        vecAttachmentsData = filterPersonnelDatas(vecAttachmentsData, request);
        session.setAttribute(VECTOR_ATTACHMENTS_DATAS,vecAttachmentsData);
        return vecPersonnelAttachmentsData;
    }
    
    /**
     * To filter the personnel document type list datas from vecAttachmentsData
     * that is taken from the data base
     * @param vector of personnel list datas
     * @throws Exception
     * @return instance of Vector which is having filtered datas
     */
    private Vector filterPersonnelDatas(Vector vecAttachmentsData, 
        HttpServletRequest request)throws Exception {
        Vector vecFilteredDatas = new Vector();
        //COEUSDEV:783 - Cannot upload attachment in Lite to a Placeholder Personnel Type created in Premium - Start
        HttpSession session = request.getSession();
        String proposalNumber =(String) session.getAttribute(PROPOSAL_NUMBER+session.getId());
        //COEUSDEV:783 - End
        //Added for case#3450 - Displaying description instead of file name of the biosketch
        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();                
        if(vecAttachmentsData!=null && vecAttachmentsData.size() > 0) {
            for(int index=0 ; index < vecAttachmentsData.size() ; index++) {
                ProposalUploadForm proposalUploadForm =(ProposalUploadForm) vecAttachmentsData.get(index);
                Vector vecDocumentDetails = new Vector();
                DocumentTypeBean documentTypeBean = new DocumentTypeBean();
                if(proposalUploadForm!=null) {
                    documentTypeBean.setDocumentType(proposalUploadForm.getDocumentTypeCode());
                    //Modified for case#3450 - Displaying description instead of file name of the biosketch
                    //documentTypeBean.setDescription(getDocumentType(proposalUploadForm.getDocumentTypeCode(), request));
                    documentTypeBean.setDescription(proposalUploadForm.getDescription());
                    documentTypeBean.setBioNumber(String.valueOf(proposalUploadForm.getBioNumber()));
                    documentTypeBean.setPersonId(proposalUploadForm.getPersonId());
                    documentTypeBean.setPersonName(proposalUploadForm.getPersonName());
                    documentTypeBean.setTimeStamp(proposalUploadForm.getAwUpdateTimestamp());
                    //Modified for case#3450 - Displaying description instead of file name of the biosketch
                    //documentTypeBean.setUpdateUser(proposalUploadForm.getUpdateUser());
                    documentTypeBean.setUpdateUser(userMaintDataTxnBean.getUserName(proposalUploadForm.getUpdateUser()));
                    documentTypeBean.setFileName(proposalUploadForm.getFileName());
                    //COEUSDEV:783 - Cannot upload attachment in Lite to a Placeholder Personnel Type created in Premium - Start
                    ProposalPersonTxnBean proposalPersonTxnBean = new ProposalPersonTxnBean();
                    ProposalPersonBioPDFBean proposalPersonBioPDFBean = new ProposalPersonBioPDFBean();
                    proposalPersonBioPDFBean.setProposalNumber(proposalNumber);
                    proposalPersonBioPDFBean.setBioNumber(proposalUploadForm.getBioNumber());
                    proposalPersonBioPDFBean.setPersonId(proposalUploadForm.getPersonId());
                    proposalPersonBioPDFBean = proposalPersonTxnBean.getProposalPersonBioPDF(proposalPersonBioPDFBean);
                    if(proposalPersonBioPDFBean != null) {
                        documentTypeBean.setIsDocumentPresent(true);
                    } else {
                        documentTypeBean.setIsDocumentPresent(false);
                    }
                    //COEUSDEV:783 - End
                    vecDocumentDetails .addElement(documentTypeBean);
                }

                if(proposalUploadForm!=null) {
                    for(int count=index+1 ; count < vecAttachmentsData.size() ; count++) {
                        ProposalUploadForm proposalUploadFormData =(ProposalUploadForm) vecAttachmentsData.get(count);
                        if(proposalUploadFormData!=null && proposalUploadFormData.getDocumentTypeCode()!=null &&
                                                proposalUploadForm.getPersonId().equals(proposalUploadFormData.getPersonId())) {
                            documentTypeBean = new DocumentTypeBean();
                            documentTypeBean.setDocumentType(proposalUploadFormData.getDocumentTypeCode());
                            //Modified for case#3450 - Displaying description instead of file name of the biosketch
                            //documentTypeBean.setDescription(getDocumentType(proposalUploadFormData.getDocumentTypeCode(), request));
                            documentTypeBean.setDescription(proposalUploadFormData.getDescription());
                            documentTypeBean.setBioNumber(String.valueOf(proposalUploadFormData.getBioNumber()));
                            documentTypeBean.setPersonId(proposalUploadFormData.getPersonId());
                            documentTypeBean.setPersonName(proposalUploadFormData.getPersonName());
                            documentTypeBean.setTimeStamp(proposalUploadFormData.getAwUpdateTimestamp());
                            //Modified for case#3450 - Displaying description instead of file name of the biosketch
                            //documentTypeBean.setUpdateUser(proposalUploadFormData.getUpdateUser());
                            documentTypeBean.setUpdateUser(userMaintDataTxnBean.getUserName(proposalUploadFormData.getUpdateUser()));
                            documentTypeBean.setFileName(proposalUploadFormData.getFileName());
                            //COEUSDEV:783 - Cannot upload attachment in Lite to a Placeholder Personnel Type created in Premium - Start
                            ProposalPersonTxnBean proposalPersonTxnBean = new ProposalPersonTxnBean();
                            ProposalPersonBioPDFBean proposalPersonBioPDFBean = new ProposalPersonBioPDFBean();
                            proposalPersonBioPDFBean.setProposalNumber(proposalNumber);
                            proposalPersonBioPDFBean.setBioNumber(proposalUploadFormData.getBioNumber());
                            proposalPersonBioPDFBean.setPersonId(proposalUploadFormData.getPersonId());
                            proposalPersonBioPDFBean = proposalPersonTxnBean.getProposalPersonBioPDF(proposalPersonBioPDFBean);
                            if(proposalPersonBioPDFBean != null) {
                                documentTypeBean.setIsDocumentPresent(true);
                            } else {
                                documentTypeBean.setIsDocumentPresent(false);
                            }
                            //COEUSDEV:783 - End
                            vecDocumentDetails .addElement(documentTypeBean);
                            vecAttachmentsData.remove(count--);
                        }
                    }
                }
                proposalUploadForm.setVecDocumentDetails(vecDocumentDetails);
                vecFilteredDatas.add(proposalUploadForm);
            }
        }
        return vecFilteredDatas;
    }
    
    private String getDocumentType(String documentTypeCode, HttpServletRequest request)throws Exception {
        String description = EMPTY_STRING;
        HttpSession session = request.getSession();
        Vector vecDocType = (Vector) session.getAttribute("vecDocumentType");
        //Getting the corresponding data for uploadStatusCode from ComboBox bean 
        //having all the documentTypes.
        if(vecDocType!=null && vecDocType.size() > 0){
            for(int index=0 ;index < vecDocType.size() ; index++) {
                ComboBoxBean comboBoxBean =(ComboBoxBean) vecDocType.get(index);
                if(comboBoxBean.getCode().equals(documentTypeCode)){
                    description = comboBoxBean.getDescription();
                    break;
                }
            }
        }
        return description;
    }
   
    /**
     * Method to get the max module number
     * @param proposalNumber
     * @throws Exception
     * @return int moduleNumber
     */
    private int getMaxNarrativeModuleNumber(String proposalNumber, HttpServletRequest request)throws Exception {
        HashMap hmNarrativeModule = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        hmNarrativeModule.put(PROPOSAL_NUMBER,proposalNumber);
        Hashtable htNarrativeModule = (Hashtable)webTxnBean.getResults(request, "getMaxNarrativeModuleNum", hmNarrativeModule );
        HashMap hmNarrativeData = (HashMap)htNarrativeModule.get("getMaxNarrativeModuleNum");
        int moduleNumber = Integer.parseInt(hmNarrativeData.get("maxModuleNumber").toString());
        return moduleNumber;
    }
    
    
    /**
     *This method is to update the Narrative Master table
     * @param proposalUploadForm
     * @throws Exception
     */
    private void addUpdProposalNarrative(ProposalUploadForm proposalUploadForm,
        HttpServletRequest request)throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htProposalNarrative = 
            (Hashtable) webTxnBean.getResults(request, "addUpdProposalNarrative", proposalUploadForm );        
    }
    
    /**
     * This method is to add the Proposal Narrative PDF
     * this updation is done with java codes and not with webTxn
     * @param proposalUploadForm
     * @throws Exception     
     */
    private void addUpdProposalNarrPDF(ProposalUploadForm proposalUploadForm) throws Exception{
        Timestamp dbTimestamp = prepareTimeStamp();
        ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean = new ProposalNarrativePDFSourceBean();
        proposalNarrativePDFSourceBean.setProposalNumber(proposalUploadForm.getProposalNumber());
        proposalNarrativePDFSourceBean.setModuleNumber(proposalUploadForm.getModuleNumber());
        if(proposalUploadForm.getDocument()!=null && !proposalUploadForm.getDocument().equals(EMPTY_STRING)){
            proposalNarrativePDFSourceBean.setFileBytes(proposalUploadForm.getDocument().getFileData());
        } //Added for COEUSQA-2550 - CLONE -problem refreshing narrative in Lite - Will loose attachment if user does notupload a new attachment - start
        else if (proposalUploadForm.getDocument() == null && proposalUploadForm.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
            ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean(); 
            proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
             if(proposalNarrativePDFSourceBean != null){
                proposalUploadForm.setFileBytes(proposalNarrativePDFSourceBean.getFileBytes());
            }
        }//Added for COEUSQA-2550 - CLONE -problem refreshing narrative in Lite - Will loose attachment if user does notupload a new attachment - end
        else {
            proposalNarrativePDFSourceBean.setFileBytes(proposalUploadForm.getFileBytes());
        }
        proposalNarrativePDFSourceBean.setFileName(proposalUploadForm.getFileName());
        //Added with case 4007: icon based on mime type - Start
        DocumentTypeChecker typeChecker = new DocumentTypeChecker();
        proposalNarrativePDFSourceBean.setMimeType(typeChecker.getDocumentMimeType(proposalNarrativePDFSourceBean));
        //4007 End
        proposalNarrativePDFSourceBean.setUpdateUser(proposalUploadForm.getUpdateUser());
        proposalNarrativePDFSourceBean.setUpdateTimestamp(dbTimestamp);
        proposalNarrativePDFSourceBean.setAcType(proposalUploadForm.getAcType());
        ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean(proposalUploadForm.getUpdateUser());
        if(proposalUploadForm.getAcType().equals(TypeConstants.INSERT_RECORD)){
            boolean isUpdated = proposalNarrativeTxnBean.addUpdProposalPDF(proposalNarrativePDFSourceBean);        
        } else {
            //Added for COEUSQA-2697-Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-start
            boolean isDocumentExists = proposalNarrativeTxnBean.isNarrativeExistsForModule(proposalUploadForm.getProposalNumber(),proposalUploadForm.getModuleNumber());
            if(isDocumentExists){
                proposalNarrativeTxnBean.uploadNarrativeBlob(proposalNarrativePDFSourceBean);
                
                
            }else{
                proposalUploadForm.setAcType(TypeConstants.INSERT_RECORD);
                proposalNarrativePDFSourceBean.setAcType(TypeConstants.INSERT_RECORD);
                boolean isUpdated = proposalNarrativeTxnBean.addUpdProposalPDF(proposalNarrativePDFSourceBean);
            }
            //Added for COEUSQA-2697-Cannot upload attach. in Lite to a Placeholder Nar. Type created in Premium-end

        }       
    }
    
    /** This method is to add Narrative rights
     * @param proposalUploadForm
     * @throws Exception     
     */
    private void addNarrativeRightsForProp(ProposalUploadForm proposalUploadForm,
        HttpServletRequest request)throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();            
        Hashtable htProposalNarrative = (Hashtable) webTxnBean.getResults(request, "addNarrativeRightsForProp", proposalUploadForm );
    }
    
    /**
     * This method is to get the narrative types
     * @throws Exception
     * @return Vector vecNarrativeType
     */
    private Vector getNarrativeType(HttpServletRequest request)throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();    
        //case 2333 start - add proposal number as argument
        HttpSession session = request.getSession();
        String proposalNumber = (String) session.getAttribute(PROPOSAL_NUMBER+session.getId());
        HashMap param = new HashMap();
        param.put(PROPOSAL_NUMBER,proposalNumber);
        
//        Hashtable htNarrativeType = (Hashtable)webTxnBean.getResults(request, "getNarrativeType", null);
        Hashtable htNarrativeType = (Hashtable)webTxnBean.getResults(request, "getNarrativeType", param);
     // end case 2333
        Vector vecNarrativeType = (Vector)htNarrativeType.get("getNarrativeType");
        return vecNarrativeType;
    }
    
    /**This method is to get proposal narrative documents for the particular  narrative group
     * @param vecNarrativeType
     * @param narrativeGroup
     * @throws Exception
     * @return
     */
    private Vector getPropNarrativeDocument(Vector vecNarrativeType,String narrativeGroup)throws Exception{
        Vector vecPropNarrative = new Vector();
        if(vecNarrativeType!=null && vecNarrativeType.size() > 0){            
            for(int index = 0 ; index < vecNarrativeType.size() ; index ++){
                NarrativeTypeBean narrativeTypeBean = (NarrativeTypeBean)vecNarrativeType.get(index);
                if(narrativeTypeBean.getNarrativeTypeGroup().equals(narrativeGroup) && 
                    narrativeTypeBean.getSystemGenerated().equals("N")){
                    vecPropNarrative.addElement(narrativeTypeBean);
                }
            }//End For
        }//End If
        return vecPropNarrative ;
    }
    
    /**
     * This method is to get the narrative attachments     
     * @param proposalNumber
     * @param narrativeGroup
     ** @return Vector
     * @throws Exception
     */    
    private Vector getNarrativeAttachMents(String proposalNumber,String narrativeGroup,
        HttpServletRequest request)throws Exception{
        HashMap hmNarrativeAttachment = new HashMap();
        Vector vecFilterdNarrative = new Vector();
        WebTxnBean webTxnBean = new WebTxnBean();
        
        HttpSession session = request.getSession();
        ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        EPSProposalHeaderBean proposalHeaderBean=(EPSProposalHeaderBean)session.getAttribute("epsProposalHeaderBean");
        String unitNumber = proposalHeaderBean.getLeadUnitNumber();
        String isInHeirarchy = proposalHeaderBean.getIsHierarchy();
        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
        //Added For the Case # COEUSQA-1679-Modification for final flag indicator -start
        boolean complete = true;
        String narrativeStatus = EMPTY_STRING;
        //Added For the Case # COEUSQA-1679-Modification for final flag indicator -end
        hmNarrativeAttachment.put(PROPOSAL_NUMBER,proposalNumber);
        Hashtable htNarrativeAttachMent = (Hashtable)webTxnBean.getResults(request, NARRATIVE_TYPE_DATA, hmNarrativeAttachment);
        Vector vecNarrativeAttachment = (Vector)htNarrativeAttachMent.get(NARRATIVE_TYPE_DATA);
        if(vecNarrativeAttachment!=null && vecNarrativeAttachment.size() > 0){
             
            // COEUSDEV-308: Application not checking module level rights for a user in Narrative - Start
            boolean canView = false;
            boolean canModify = false;
            
            // Check if the user has dept. level rights
            boolean hasDeptLevelViewRight = userMaintDataTxnBean.getUserHasRight(userInfoBean.getUserId(), "VIEW_ANY_PROPOSAL", unitNumber);          
            boolean hasDeptLevelModifyRight = userMaintDataTxnBean.getUserHasRight(userInfoBean.getUserId() ,"MODIFY_ANY_PROPOSAL",unitNumber);
            // If the user doesnt have dept. level modify right
            MedusaTxnBean medusaTxnBean = new MedusaTxnBean();
            ProposalDevelopmentFormBean proposalDevtFormBean = medusaTxnBean.getDevProposalDetails(proposalNumber);

            //COEUSQA-3964 - A Coeus 4.5.1p1 - PI PropDev Narrative Display
            Vector investigatorsList = proposalDevtFormBean.getInvestigators();

            String personId = "";

            if(investigatorsList != null && investigatorsList.size() > 0) {
                for(int i = 0; i < investigatorsList.size(); i++) {
                    ProposalInvestigatorFormBean investBean = (ProposalInvestigatorFormBean) investigatorsList.get(i);
                    if(investBean.isPrincipleInvestigatorFlag()){
                        personId = investBean.getPersonId();
                    }
                }
            }

            int statusCode = proposalDevtFormBean.getCreationStatusCode();
            if(! hasDeptLevelModifyRight){
                if(statusCode == 2 || statusCode== 4 || statusCode == 5 || statusCode == 6 || statusCode == 7) {
                    // If the protocol creation status is 
                    // Approval In Progress Or
                    // Approved Or
                    // Submitted Or 
                    // Post-Submission Approval Or
                    // Post-Submission Rejection
                    hasDeptLevelModifyRight =  userMaintDataTxnBean.getUserHasAnyOSPRight(userInfoBean.getUserId());
                }
            }
            
            // Check if the user has Proposal level narrative rights
            boolean hasPropLevelViewRight = userMaintDataTxnBean.getUserHasProposalRight(userInfoBean.getUserId(), proposalNumber, "VIEW_NARRATIVE");
           
            //COEUSQA-3964 - A Coeus 4.5.1p1 - PI PropDev Narrative Display
            if(personId != null && personId.equals(userInfoBean.getPersonId())){
                hasPropLevelViewRight = true;
            }
            boolean hasPropLevelModifyRight = userMaintDataTxnBean.getUserHasProposalRight(userInfoBean.getUserId(), proposalNumber, "MODIFY_NARRATIVE");
                                    
            if(hasDeptLevelModifyRight){
                // If the user has Dept. level modify narrative right
                canView = true;
                canModify = true;
            }
            
            for( int index = 0 ; index < vecNarrativeAttachment.size() ; index ++ ){
                
                ProposalUploadForm proposalUploadForm = (ProposalUploadForm)vecNarrativeAttachment.get(index);
                
                if(proposalUploadForm.getNarrativeTypeGroup()!= null && proposalUploadForm.getNarrativeTypeGroup().equalsIgnoreCase(narrativeGroup)){
                    
                    if(!hasDeptLevelModifyRight){
                        // If the user doesn't have Dept. level modify narrative right
                        char moduleRight = '\0' ;
                        ProposalNarrativeModuleUsersFormBean proposalNarrativeModuleUsersFormBean;
                        // Get all the Narrative users for a module
                        Vector vecUserRights = proposalNarrativeTxnBean.getPropNarrativeModuleUsers(proposalNumber, proposalUploadForm.getModuleNumber());
                        if(vecUserRights != null && !vecUserRights.isEmpty()){
                            int totalUsers = vecUserRights.size();
                            for(int userIndex = 0; userIndex < totalUsers; userIndex++){
                                canView = false;
                                canModify = false;
                                proposalNarrativeModuleUsersFormBean
                                        = (ProposalNarrativeModuleUsersFormBean) vecUserRights.get(userIndex);
                                if(proposalNarrativeModuleUsersFormBean.getProposalNumber().equals(proposalNumber)
                                && proposalNarrativeModuleUsersFormBean.getModuleNumber() == proposalUploadForm.getModuleNumber()
                                && proposalNarrativeModuleUsersFormBean.getUserId().equalsIgnoreCase(userInfoBean.getUserId())){
                                    if(hasDeptLevelModifyRight){
                                        // If the user has Dept. Level Modify right
                                        canView = true;
                                        canModify = true;
                                    } else if(hasDeptLevelViewRight){
                                        // If the user has Dept. level View right
                                        canView = true;
                                    }
                                    // get the module level right for the user
                                    moduleRight = proposalNarrativeModuleUsersFormBean.getAccessType();
                                    break;
                                }
                            }
                            
                            if(hasPropLevelModifyRight){
                                // If the user has Proposal level modify right
                                if(moduleRight == 'M'){
                                    // If the user has module level modify right
                                    canView = true;
                                    canModify = true;
                                } else if(moduleRight == 'R'){
                                    // If the user has module level view right
                                    canView = true;
                                }
                            } else if(!canView && hasPropLevelViewRight && moduleRight == 'R'){
                                // If the user has both Proposal level view right snd module level view right
                                canView = true;
                            }
                        }
                       
                       
                    }
                    //COEUSQA-2417 : File refresh during routing not working for Premium Proposal Personnel documents and not working in Lite
                    if(hasDeptLevelViewRight){                
                        canView = true;
                    }

                    //COEUSQA-3964 - A Coeus 4.5.1p1 - PI PropDev Narrative Display
                    if(personId != null && personId.equals(userInfoBean.getPersonId())){
                        canView = true;
                    }
                    
                    // Narrative can be modified Only if the Proposal status is
                    // In progress, Rejected, Appoval In progress, or Approved
                    //COEUSQA-1433 Allow Recall for Routing - Start
                    /*
                    if(!(statusCode == 1 || statusCode == 3
                            || statusCode == 2 || statusCode == 4)) {*/
                    if(!(statusCode == CoeusConstants.PROPOSAL_IN_PROGRESS_STATUS_CODE || statusCode == CoeusConstants.PROPOSAL_REJECTED_STATUS_CODE
                            || statusCode == CoeusConstants.PROPOSAL_APPROVAL_IN_PROGRESS_STATUS_CODE || statusCode == CoeusConstants.PROPOSAL_APPROVED_STATUS_CODE
                            || statusCode == CoeusConstants.PROPOSAL_RECALLED_STATUS_CODE)) {
                    //COEUSQA-1433 Allow Recall for Routing - End
                        canModify = false;
                    }
                    
                    // Do not edit the narrative if Proposal is in Hierarchy
                    if("Y".equalsIgnoreCase(isInHeirarchy)){
                        canModify = false;
                    }
                    
                    char right;
                    if(canModify){
                        // If the user can modify the narrative module
                        right = 'M';
                    } else if(canView){
                        // If the user can view the narrative module
                        right = 'R';
                    } else {
                        right = 'N';
                    }
                    //Added for COEUSDEV-470 : Application is inconsistent in assigning users narrative user rights while adding them as approvers - Start
                    if(!canModify && !canView){
                        char userModuleRight = proposalNarrativeTxnBean.getNarrativeUserRightforModule(proposalNumber,
                                proposalUploadForm.getModuleNumber(),userInfoBean.getUserId());
                        if(userModuleRight == 'R' || userModuleRight == 'M'){
                            right =  'R';
                        }
                    }
                    proposalUploadForm.setAccessType(right);
                    // COEUSDEV-308: Application not checking module level rights for a user in Narrative - End
                    vecFilterdNarrative.add(proposalUploadForm);
                    
                   //Added For the Case # COEUSQA-1679-Modification for final flag indicator -start
                    if(proposalUploadForm.getModuleStatusCode()!= null && "I".equalsIgnoreCase(proposalUploadForm.getModuleStatusCode())){
                        complete = false;
                    }
                   //Added For the Case # COEUSQA-1679-Modification for final flag indicator -end
                }
            }
            //Added For the Case # COEUSQA-1679-Modification for final flag indicator -start
            if( complete ) {
                narrativeStatus = "C";
            }else{
                narrativeStatus = "I";
            }
           
            //hmNarrativeAttachment.put("proposalStatus", "C");       
            hmNarrativeAttachment.put("proposalStatus", narrativeStatus);       
            setProposalStatus(hmNarrativeAttachment, request);    
             //Added For the Case # COEUSQA-1679-Modification for final flag indicator -end
        } else {
            //Added For the Case # COEUSQA-1679-Modification for final flag indicator -end
            narrativeStatus = "N";
            //hmNarrativeAttachment.put("proposalStatus", null);            
            hmNarrativeAttachment.put("proposalStatus", narrativeStatus);    
            setProposalStatus(hmNarrativeAttachment, request);
            //Added For the Case # COEUSQA-1679-Modification for final flag indicator -end
        }
        return vecFilterdNarrative;
    }
    
    /**
     * This method is to check whether multiple document type can be
     * uploaded
     * @return boolean
     * @param proposalUploadForm
     * @throws Exception
     */
    private boolean validateNarrativeAttachments(ProposalUploadForm proposalUploadForm,
        HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();
        ActionMessages actionMessages = new ActionMessages();
        Vector vecDocumentType = (Vector)session.getAttribute("vecDocumentType");
        Vector vecNarrativeAttachment =  
            getNarrativeAttachMents(proposalUploadForm.getProposalNumber(),
                                    proposalUploadForm.getNarrativeTypeGroup(), request);
        boolean isAllowed = true ;
        if((vecDocumentType!=null && vecDocumentType.size() > 0) && 
            !proposalUploadForm.getAcType().equals(TypeConstants.UPDATE_RECORD)){
            for(int index = 0 ; index < vecDocumentType.size() ; index ++  ){
                NarrativeTypeBean narrativeTypeBean = (NarrativeTypeBean) vecDocumentType.get(index);
                if(narrativeTypeBean.getNarrativeTypeCode() == Integer.parseInt(proposalUploadForm.getUploadStatusCode())) {
                    if(narrativeTypeBean.getAllowMultiple().equalsIgnoreCase("N")){
                        if(vecNarrativeAttachment!=null && vecNarrativeAttachment.size() > 0){
                            for(int count = 0 ;count < vecNarrativeAttachment.size() ; count++){
                                ProposalUploadForm propUploadForm = (ProposalUploadForm) vecNarrativeAttachment.get(count);
                                if(propUploadForm.getUploadStatusCode().equals(proposalUploadForm.getUploadStatusCode())){
                                    isAllowed = false ;
                                    actionMessages.add("duplicateRecordsNotAllowed",
                                    new ActionMessage("uploadAttachments.duplicateRecordsNotAllowed"));
                                    saveMessages(request, actionMessages);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        
        int narrativeTypeOtherCode = getNarrativeTypeOtherCode(request);
        if(Integer.parseInt(proposalUploadForm.getUploadStatusCode()) == narrativeTypeOtherCode){
            if(proposalUploadForm.getDescription()==null || proposalUploadForm.getDescription().equals(EMPTY_STRING)) {
                isAllowed = false ;
                actionMessages.add("descriptionRequired",
                                new ActionMessage("error.uploadAttachments.descriptionRequired"));
                saveMessages(request, actionMessages);
            }
        }
        return isAllowed;
    }
    
   
    
    /**This method gets the NARRATIVE_TYPE_OTHER_CODE value from the Paramater table     
     * @throws Exception
     * @return int narrativeTypeOtherCode
     */     
    private int getNarrativeTypeOtherCode(HttpServletRequest request)throws Exception{
        HashMap hmNarrativeTypeOther = new HashMap();
        hmNarrativeTypeOther.put("narrativeTypeOtherCode" , "NARRATIVE_TYPE_OTHER_CODE");
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htNarrativeTypeOther = 
            (Hashtable)webTxnBean.getResults(request, "getNarrativeTypeOtherCode", hmNarrativeTypeOther);
        HashMap hmNarrativeOtherCode = (HashMap)htNarrativeTypeOther.get("getNarrativeTypeOtherCode");
        int narrativeTypeOtherCode = Integer.parseInt(hmNarrativeOtherCode.get("parameterValue").toString());
        return narrativeTypeOtherCode;
    }
    
    /**
     * Method to get the max bio number
     * @param proposalNumber
     * @param personId
     * @throws Exception
     * @return int bioNumber
     */    
    private int getMaxNarrativeBioNumber(String proposalNumber, String personId,
        HttpServletRequest request)throws Exception {
        HashMap hmNarrativeBio = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        hmNarrativeBio.put(PROPOSAL_NUMBER,proposalNumber);
        hmNarrativeBio.put(PERSON_ID,personId);
        Hashtable htNarrativeBio = (Hashtable)webTxnBean.getResults(request, "getMaxNarrativeBioNum", hmNarrativeBio );
        HashMap hmNarrativeData = (HashMap)htNarrativeBio.get("getMaxNarrativeBioNum");
        int bioNumber=1;
        if(hmNarrativeData.get(MAX_BIO_NUM)!=null && !hmNarrativeData.get(MAX_BIO_NUM).equals(EMPTY_STRING)){
            bioNumber = Integer.parseInt(hmNarrativeData.get(MAX_BIO_NUM).toString());
            bioNumber++;
        }
        return bioNumber;
    }
   
    /**
     *This method is to update the Narrative Personnel Master table
     * @param proposalUploadForm
     * @throws Exception
     */    
    private void addUpdPersonnelNarrative(ProposalUploadForm proposalUploadForm,
        HttpServletRequest request)throws Exception {
        String docType = proposalUploadForm.getUploadStatusCode();
        String persons = proposalUploadForm.getPersonId();
        //Commented for case#3450 - Displaying description instead of file name of the biosketch
        //String description = getDocumentType(docType, request);
        HttpSession session = request.getSession();
        Vector vecPersons = (Vector) session.getAttribute("vecPersonnelPersonDetails");
        WebTxnBean webTxnBean = new WebTxnBean();
        //Commented for case#3450 - Displaying description instead of file name of the biosketch
        //proposalUploadForm.setDescription(description);
        Hashtable htPersonnelNarrative = 
            (Hashtable) webTxnBean.getResults(request, "addUpdPersonnelNarrative", proposalUploadForm ); 
    }
    
    //Modified for case#3450 - Displaying description instead of file name of the biosketch - start
    /**
     * This method is to add the Personnel Narrative PDF
     * this updation is done with java codes and not with webTxn
     * @param proposalUploadForm
     * @throws Exception     
     */    
    private void addUpdPersonnelNarrPDF(ProposalUploadForm proposalUploadForm, HttpServletRequest request)throws Exception {
        Timestamp dbTimestamp = prepareTimeStamp();
        HttpSession session = request.getSession();
        Vector vecAttachmentsData = (Vector) session.getAttribute("vecPersonnelNarrativeDatas");        
        ProposalPersonTxnBean proposalPersonTxnBean = new ProposalPersonTxnBean();
        ProposalPersonBioPDFBean proposalPersonBioPDFBean = new ProposalPersonBioPDFBean();
        proposalPersonBioPDFBean.setProposalNumber(proposalUploadForm.getProposalNumber());
        proposalPersonBioPDFBean.setPersonId(proposalUploadForm.getPersonId());
        proposalPersonBioPDFBean.setBioNumber(proposalUploadForm.getBioNumber());        
        if(proposalUploadForm.getDocument() != null && proposalUploadForm.getDocument().getFileSize() > 0){
            proposalPersonBioPDFBean.setFileBytes(proposalUploadForm.getDocument().getFileData());
            proposalPersonBioPDFBean.setFileName(proposalUploadForm.getDocument().getFileName());
        }else{
            for(int index=0; index < vecAttachmentsData.size(); index++){
                ProposalUploadForm updBean = (ProposalUploadForm) vecAttachmentsData.get(index);
                if(updBean.getBioNumber() == proposalUploadForm.getBioNumber()
                    && updBean.getPersonId().equals(proposalUploadForm.getPersonId())){
                    proposalPersonBioPDFBean.setFileName(updBean.getFileName());
                    proposalPersonBioPDFBean.setFileBytes(updBean.getFileBytes());
                    break;
                }
            }            
        }
        //Added with case 4007: icon based on mime type - Start
        DocumentTypeChecker typeChecker = new DocumentTypeChecker();
        proposalPersonBioPDFBean.setMimeType(typeChecker.getDocumentMimeType(proposalPersonBioPDFBean));
        //4007 End
        //proposalPersonBioPDFBean.setFileBytes(proposalUploadForm.getDocument().getFileData());
        //proposalPersonBioPDFBean.setFileName(proposalUploadForm.getFileName());
        proposalPersonBioPDFBean.setUpdateUser(proposalUploadForm.getUpdateUser());
        proposalPersonBioPDFBean.setUpdateTimestamp(dbTimestamp);
        if(proposalUploadForm.getAcType().equals(TypeConstants.INSERT_RECORD)){
            proposalPersonBioPDFBean.setAcType(PERSONNEL_DOCUMENT_TYPE);
            proposalPersonTxnBean.addUpdProposalPersonBioPDF(proposalPersonBioPDFBean);
        } else {
            //COEUSDEV:783 - Cannot upload attachment in Lite to a Placeholder Personnel Type created in Premium - Start
            //proposalPersonBioPDFBean.setAcType(TypeConstants.UPDATE_RECORD);
            // proposalPersonTxnBean.updProposalPersonBioPDF(proposalPersonBioPDFBean);
            ProposalPersonBioPDFBean proposalPersonBioPDFBeanAttach = new ProposalPersonBioPDFBean();
            proposalPersonBioPDFBeanAttach.setProposalNumber(proposalUploadForm.getProposalNumber());
            proposalPersonBioPDFBeanAttach.setBioNumber(proposalUploadForm.getBioNumber());
            proposalPersonBioPDFBeanAttach.setPersonId(proposalUploadForm.getPersonId());
            proposalPersonBioPDFBeanAttach = proposalPersonTxnBean.getProposalPersonBioPDF(proposalPersonBioPDFBeanAttach);
            if(proposalPersonBioPDFBeanAttach != null) {
                proposalPersonBioPDFBean.setAcType(TypeConstants.UPDATE_RECORD);
                proposalPersonTxnBean.updProposalPersonBioPDF(proposalPersonBioPDFBean);
            } else {
                proposalPersonBioPDFBean.setAcType(TypeConstants.INSERT_RECORD);
                proposalPersonTxnBean.addUpdProposalPersonBioPDF(proposalPersonBioPDFBean);
            }
            //COEUSDEV:783 - End
        }
    }
    //Modified for case#3450 - Displaying description instead of file name of the biosketch - end
    
    /**
     * This method is to delete the Personnel Narrative datas
     * @param proposalUploadForm
     * @throws Exception     
     */    
    private void  deleteUpdPersonnelNarrative(ProposalUploadForm proposalUploadForm,
        HttpServletRequest request)throws Exception {
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htNarrativeAttachMent = 
                    (Hashtable)webTxnBean.getResults(request, "deleteUpdPersonnelNarrative", proposalUploadForm);
        addUpdPersonnelNarrative(proposalUploadForm, request);
    }
    
    /**
     * This method is to check whether documentType and Person having 
     * document or not
     * @return boolean
     * @param proposalUploadForm
     * @throws Exception
     */    
    private boolean validatePersonnelAttachments(ProposalUploadForm proposalUploadForm,
        HttpServletRequest request)throws Exception {
        boolean isAllowed = true ;
        ActionMessages actionMessages = new ActionMessages();
        HttpSession session = request.getSession();
        Vector vecAttachmentsData = (Vector) session.getAttribute("vecPersonnelNarrativeDatas");
        if(vecAttachmentsData!=null && vecAttachmentsData.size() > 0){
            for(int index=0 ; index<vecAttachmentsData.size() ; index++){
                ProposalUploadForm proposalUploadFormData =(ProposalUploadForm) vecAttachmentsData.get(index);
                if(proposalUploadForm.getUploadStatusCode().equals(proposalUploadFormData.getDocumentTypeCode())) {
                   if(proposalUploadForm.getPersonId().equals(proposalUploadFormData.getPersonId())){
                        isAllowed = false ;
                        actionMessages.add("duplicateRecordsNotAllowed",
                        new ActionMessage("uploadAttachments.duplicateRecordsNotAllowed"));
                        saveMessages(request, actionMessages);
                        break;
                   }
                }
            }
        }
        return isAllowed;
    }
    
    private void setEditDetails(HttpServletRequest request, 
            ProposalUploadForm proposalUploadForm)throws Exception{
        HttpSession session = request.getSession();
        String page = request.getParameter(PAGE_PARAMETER);
        String number = request.getParameter("number");
        number = (number==null)? EMPTY_STRING : number;
        String proposalNumber = (String) session.getAttribute(PROPOSAL_NUMBER+session.getId());
        page = (page==null)? EMPTY_STRING : page;
        proposalUploadForm.setAcType(TypeConstants.UPDATE_RECORD);
        ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean = new ProposalNarrativePDFSourceBean();
        ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean(); 
        if(!page.equals(PERSONNEL_DOCUMENT_TYPE)){
            Vector vecNarrativeAttachment =  getNarrativeAttachMents(proposalNumber,
                                                        page,request);
            if(vecNarrativeAttachment!=null && vecNarrativeAttachment.size()>0){
                for(int index=0 ; index<vecNarrativeAttachment.size() ; index++){
                    ProposalUploadForm form = (ProposalUploadForm)vecNarrativeAttachment.get(index);
                    if(number.equals((new Integer(form.getModuleNumber())).toString())){
                        proposalUploadForm.setAwUpdateTimestamp(form.getAwUpdateTimestamp());
                        proposalUploadForm.setUploadStatusCode(form.getUploadStatusCode());
                        proposalUploadForm.setDescription(form.getDescription());
                        proposalUploadForm.setFileName(form.getFileName());
                        proposalUploadForm.setUpdateUser(form.getUpdateUser());
                        proposalUploadForm.setNarrativeTypeGroup(form.getNarrativeTypeGroup());
                        proposalUploadForm.setModuleNumber(form.getModuleNumber());
                        //Added For the Case # COEUSQA-1679-Modification for final flag indicator -start
                        String moduleStatusCode = form.getModuleStatusCode();
                        if(MODULE_STATUS_CODE.equalsIgnoreCase(moduleStatusCode)){
                            proposalUploadForm.setModuleStatusCode("on");
                        }else{
                            proposalUploadForm.setModuleStatusCode("off");
                        }
                        //Added For the Case # COEUSQA-1679-Modification for final flag indicator -end
                        proposalNarrativePDFSourceBean.setProposalNumber(proposalUploadForm.getProposalNumber());
                        proposalNarrativePDFSourceBean.setModuleNumber(form.getModuleNumber());
                        proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                        //Modified For the Case # COEUSQA-1679-Modification for final flag indicator -start
                        // Chekcing  for Not null
                        if(proposalNarrativePDFSourceBean != null){
                            proposalUploadForm.setFileBytes(proposalNarrativePDFSourceBean.getFileBytes());
                        }
                        //Added For the Case # COEUSQA-1679-Modification for final flag indicator -start
                        break;
                    }
                }
            }
        } else {
            Vector vecNarrativeAttachment = getPersonnelNarrativeDocument(proposalUploadForm, request);
            if(vecNarrativeAttachment!=null && vecNarrativeAttachment.size()>0){
                for(int index=0 ; index<vecNarrativeAttachment.size() ; index++){
                    ProposalUploadForm form = (ProposalUploadForm)vecNarrativeAttachment.get(index);
                    Vector vecDocDetails = (Vector) form.getVecDocumentDetails();
                    if(vecDocDetails!=null && vecDocDetails.size()>0){
                        for(int count=0 ; count < vecDocDetails.size() ; count++){
                            DocumentTypeBean formBean = (DocumentTypeBean)vecDocDetails.get(count);
                            if(number.equals(formBean.getBioNumber()) && 
                                    proposalUploadForm.getPersonId().equals(formBean.getPersonId())){
                                proposalUploadForm.setAwUpdateTimestamp(formBean.getTimeStamp());
                                proposalUploadForm.setBioNumber(Integer.parseInt(formBean.getBioNumber()));
                                proposalUploadForm.setCode(form.getCode());
                                proposalUploadForm.setDescription(formBean.getDescription());
                                proposalUploadForm.setUploadStatusCode(formBean.getDocumentType());
                                proposalUploadForm.setFileName(formBean.getFileName());
                                proposalUploadForm.setPersonId(formBean.getPersonId());
                                proposalUploadForm.setPersonName(formBean.getPersonName());
                                proposalUploadForm.setUpdateUser(formBean.getUpdateUser());
                                ProposalPersonTxnBean proposalPersonTxnBean = new ProposalPersonTxnBean();
                                ProposalPersonBioPDFBean proposalPersonBioPDFBean = new ProposalPersonBioPDFBean();
                                proposalPersonBioPDFBean.setProposalNumber(proposalUploadForm.getProposalNumber());
                                proposalPersonBioPDFBean.setBioNumber(proposalUploadForm.getBioNumber());
                                proposalPersonBioPDFBean.setPersonId(proposalUploadForm.getPersonId());
                                proposalPersonBioPDFBean = proposalPersonTxnBean.getProposalPersonBioPDF(proposalPersonBioPDFBean);
                                //COEUSDEV:783 - Cannot upload attachment in Lite to a Placeholder Personnel Type created in Premium - Start
                                //proposalUploadForm.setFileBytes(proposalPersonBioPDFBean.getFileBytes());
                                if(proposalPersonBioPDFBean != null) {
                                    proposalUploadForm.setFileBytes(proposalPersonBioPDFBean.getFileBytes());
                                }
                                //COEUSDEV:783 - End
                                break;
                            }
                        }
                    }
                }
            }
        }        
        
    }
    
//Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - Start
//Commented for Case#3577    
//    private void getNarrativeRights(HttpServletRequest request, String userId)throws Exception{
//        HttpSession session = request.getSession();
//        String proposalNumber = (String) session.getAttribute("proposalNumber"+session.getId());
//        String mode=(String)session.getAttribute("mode"+session.getId());
//        boolean modeValue=false;
//        if(mode!=null && !mode.equals("")) {
//            if(mode.equalsIgnoreCase("display")){
//                modeValue=true;
//            }
//        }
//        boolean isVisible = false;
//        if(modeValue){
//            EPSProposalHeaderBean headerBean = (EPSProposalHeaderBean)session.getAttribute("epsProposalHeaderBean");
//            String statusCode = null;
//            if(headerBean!= null){
//                statusCode = headerBean.getProposalStatusCode();
//                if(statusCode!= null){
//                    if(statusCode.equals("2") || statusCode.equals("4")){
//                        ProposalNarrativeTxnBean bean = new ProposalNarrativeTxnBean();
//                        int count = bean.getUserHasNarrativeModRights(userId, proposalNumber);
//                        if(count==0){
//                            ProposalDevelopmentTxnBean txnBean = new ProposalDevelopmentTxnBean();
//                            count = txnBean.getUserHasOSPRight(userId, "ALTER_PROPOSAL_DATA");
//                            if(count==0){
//                                isVisible = true;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        session.setAttribute("isVisible", new Boolean(isVisible));
//    }    
    
    
    /**
     * To check the narrative rights
     * @param loggedinUser 
     * @param request      
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @throws org.okip.service.shared.api.Exception      
     */
    private void getNarrativeRights(HttpServletRequest request,String loggedinUser)throws CoeusException, 
            DBException, org.okip.service.shared.api.Exception {
        
        String proposalNumber = (String) request.getSession().getAttribute("proposalNumber"+request.getSession().getId());
        
        EPSProposalHeaderBean headerBean = (EPSProposalHeaderBean)request.getSession().getAttribute("epsProposalHeaderBean");            
            String statusCode = headerBean.getProposalStatusCode();
            String unitNumber = headerBean.getUnitNumber();
            String isInHeirarchy = headerBean.getIsHierarchy();
            isInHeirarchy = isInHeirarchy != null ? isInHeirarchy : "";
        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
        boolean canModifyNarrative = false;
        boolean canViewNarrative = false;     
        boolean modifyAnyProposal = userMaintDataTxnBean.getUserHasRight(loggedinUser,"MODIFY_ANY_PROPOSAL",unitNumber);
        boolean modifyNarrative = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, "MODIFY_NARRATIVE");
        boolean isVisible = false;
        
        String mode=(String)request.getSession().getAttribute("mode"+request.getSession().getId());
        boolean modeValue=false;
        if(mode!=null && !mode.equals("")) {
            if(mode.equalsIgnoreCase("display")){
                modeValue=true;
            }
        }
        // 4089: Lite - Proposal Roles - Budget Creators can create narratives - Start
        if(!modeValue){
            if(modifyNarrative || modifyAnyProposal){
                canModifyNarrative = true;
            } else {
                canViewNarrative = true;
                isVisible = true;
            }           
        } else {
        // 4089: Lite - Proposal Roles - Budget Creators can create narratives - End
        //For Narrative Modify right checking

        //If the user is having Modify Any proposal rights
        //then check for the proposal status
        if(modifyAnyProposal){
            //If the status code is 1 (In Progress) or 3(Rejected) or 2 (Approval In Progress) or 4(Approved) or 8(Recalled)
            //set modify narrative right to true.
            //COEUSQA-1433 Allow Recall for Routing - Start
            //if(statusCode.equals("1") || statusCode.equals("3")
            //        || statusCode.equals("2")|| statusCode.equals("4")) {
            if(statusCode.equals("1") || statusCode.equals("3") || statusCode.equals("8")
                    || statusCode.equals("2")|| statusCode.equals("4")) {
            //COEUSQA-1433 Allow Recall for Routing - End
                canModifyNarrative = true;
            }
        }
        
        //If the user is having Modify Narrative right
        //then check for the proposal status
        if(!canModifyNarrative && modifyNarrative){
            //If the status code is 1 (In Progress) or 3(Rejected) or 2 (Approval In Progress) or 4(Approved) or 8(Recalled)
            //set modify narrative right to true.
            //if(statusCode.equals("1") || statusCode.equals("3")
            //        || statusCode.equals("2")|| statusCode.equals("4")) {
            if(statusCode.equals("1") || statusCode.equals("3") || statusCode.equals("8")
                    || statusCode.equals("2")|| statusCode.equals("4")) {
            //COEUSQA-1433 Allow Recall for Routing - End
                canModifyNarrative = true;
            }
        }
        
        //If the status code is 2 (Approval In Progress) or 4(Approved)
        //and has alter proposal right set modify narrative right to true.
        if(!canModifyNarrative && (statusCode.equals("2") || statusCode.equals("4"))){
            //Modified with case 3587: Multicampus Enhancement
//            boolean hasAlterRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, "ALTER_PROPOSAL_DATA");
            boolean hasAlterRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, "ALTER_PROPOSAL_DATA",unitNumber);
            //3587 End
            if(hasAlterRight) {
                canModifyNarrative = true;
            }
        }
        
        //For Narrative viewing right checking
        //If the user is not having modify narrative then check for view narrative rights
        if (canModifyNarrative) {
            canViewNarrative = true;
        }
        
        if(!canViewNarrative){
            boolean viewAnyProposal = userMaintDataTxnBean.getUserHasRight(loggedinUser,"VIEW_ANY_PROPOSAL",unitNumber);
            boolean viewNarrative = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, "VIEW_NARRATIVE");

            //If the user is having Modify Any proposal rights
            //then check for the proposal status            
            if(modifyAnyProposal){
                //If the status code is not equal to 1 (In Progress) and 3(Rejected) and 2 (Approval In Progress) and 4(Approved) and 8(Recalled)
                //set view narrative right to true.
                //COEUSQA-1433 Allow Recall for Routing - Start
                //if(!statusCode.equals("1") && !statusCode.equals("3")
                //        && !statusCode.equals("2") && !statusCode.equals("4")) {                
                if(!statusCode.equals("1") && !statusCode.equals("3") && !statusCode.equals("8")
                    && !statusCode.equals("2")&& !statusCode.equals("4")) {
                //COEUSQA-1433 Allow Recall for Routing - End
                    canViewNarrative = true;
                }
            }
            
            //If the user is having Modify Narrative right
            //then check for the proposal status
            if(!canViewNarrative && modifyNarrative){
                //If the status code is not equal to 1 (In Progress) and 3(Rejected) and 2 (Approval In Progress) and 4(Approved) and 8(Recalled)
                //set view narrative right to true.
                //COEUSQA-1433 Allow Recall for Routing - Start
                //if(!statusCode.equals("1") && !statusCode.equals("3")
                //        && !statusCode.equals("2") && !statusCode.equals("4")) {                
                if(!statusCode.equals("1") && !statusCode.equals("3") && !statusCode.equals("8")
                    && !statusCode.equals("2")&& !statusCode.equals("4")) {
                //COEUSQA-1433 Allow Recall for Routing - End
                    canViewNarrative = true;
                }
            }
            
            //If the user is having View Any proposal rights
            //then set view narrative right to true.
            if(!canViewNarrative && viewAnyProposal){
                canViewNarrative = true;
            }
            
            //If the user is having View Narrative right
            //then set view narrative right to true.
            if(!canViewNarrative && viewNarrative){
                canViewNarrative = true;
            }
            
            if(!canViewNarrative){
                //If the user is having any OSP right and 
                //the status is not equal to 1 (In Progress) and 3(Rejected) and 8(Recalled)
                //then set view narrative right to true.                
                boolean userHasAnyOspRights = userMaintDataTxnBean.getUserHasAnyOSPRight(loggedinUser);
                //COEUSQA-1433 Allow Recall for Routing - Start
                //if(userHasAnyOspRights && !statusCode.equals("1") && !statusCode.equals("3")){
                if(userHasAnyOspRights && !statusCode.equals("1") && !statusCode.equals("3") && !statusCode.equals("8")){
                //COEUSQA-1433 Allow Recall for Routing - End
                    canViewNarrative = true;
                }
            }
          }
         
        //setting modify and view narrative flag.        
        if(!canModifyNarrative || (!isInHeirarchy.equals("") && isInHeirarchy.equals("Y"))){
            isVisible = true;
        }
      }
        request.getSession().setAttribute("isVisible", new Boolean(isVisible));
        
        // This part of the block is for chekcing if the user can edit narrative in Display mode
        
        
        
        
        
    }
    
    /**
     * To check the proposal personnel rights
     * @param loggedinUser 
     * @param request      
     * @param userId
     * @param acType
     * @param proposalUploadForm
     * @throws Exception 
     */
    private void getPropPersonnelRights( HttpServletRequest request, 
            String userId,String acType,
            ProposalUploadForm proposalUploadForm)throws Exception{
        HttpSession session = request.getSession();        
        String proposalNumber = (String) session.getAttribute("proposalNumber"+session.getId());
        String mode=(String)session.getAttribute("mode"+session.getId());
        boolean modeValue=false;
        if(mode!=null && !mode.equals("")) {
            if(mode.equalsIgnoreCase("display")){
                modeValue=true;
            }
        }
        String isVisible = "Y";
        if(modeValue){
            EPSProposalHeaderBean headerBean = (EPSProposalHeaderBean)session.getAttribute("epsProposalHeaderBean");
            String statusCode = null;
            if(headerBean!= null){
                statusCode = headerBean.getProposalStatusCode();
                String unitNumber = headerBean.getUnitNumber();
                if(statusCode!= null){
                    if(statusCode.equals("2") || statusCode.equals("4")){
                        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                        boolean hasModifyAnyProp = userMaintDataTxnBean.getUserHasRight(userId,"MODIFY_ANY_PROPOSAL",unitNumber);
                        boolean hasModifyProp = userMaintDataTxnBean.getUserHasProposalRight(userId, proposalNumber, "MODIFY_PROPOSAL");
                        //Modified with case 3587: MultiCampus Enhancement
//                        boolean hasAlterPropData = userMaintDataTxnBean.getUserHasOSPRight(userId, "ALTER_PROPOSAL_DATA");
                        boolean hasAlterPropData = userMaintDataTxnBean.getUserHasRight(userId, "ALTER_PROPOSAL_DATA",unitNumber);
                        //3587; End
                        if((hasModifyAnyProp || hasModifyProp || hasAlterPropData) && !headerBean.getIsHierarchy().equals("Y")){
                            isVisible = "N";
                        }
                    }
                }
            }
        }
        session.setAttribute("propPersonnelVisible", isVisible);
    }
    /*
     * To send Notifications to all users who have already approved the proposal when a change is made.
     * Sending inbox message to the approved users and making a Notepad entry regarding the changes made.
     * @param userId is the user making the changes as a result of which a notification is sent.
     * @param request is the request to notify.
     * @param message is the message sent during notification
     * @throws Exception
     */
    private void notification(String userId,HttpServletRequest request,String message) throws Exception{
        
        CoeusVector cvApprovalMaps = null;
        CoeusVector cvApprovers = new CoeusVector();
        CoeusVector cvRoutingDetails = new CoeusVector();
        ProposalActionTxnBean proposalActionTxnBean = new ProposalActionTxnBean();
        HttpSession session = request.getSession();
        String proposalNumber =(String) session.getAttribute(PROPOSAL_NUMBER+session.getId());
        //Modified for Case#4572 - Writing notepad entries and sending notifications irrespective of proposal status - Start
        //Mails are send only to approvers when proposal status is approvel in progress
        EPSProposalHeaderBean headerBean = (EPSProposalHeaderBean)session.getAttribute("epsProposalHeaderBean");
        if(headerBean != null && APPROVEL_IN_PROGRESS.equals(headerBean.getProposalStatusCode())){
            RoutingTxnBean routingTxnBean = new RoutingTxnBean();
            RoutingBean routingBean = new RoutingBean();
            routingBean = routingTxnBean.getRoutingHeader("3", proposalNumber, "0", 0);
            if(routingBean != null){
                cvApprovalMaps = (CoeusVector)routingTxnBean.getRoutingMaps(routingBean.getRoutingNumber());
            }
            if(cvApprovalMaps != null && cvApprovalMaps.size() > 0){
                for(int index = 0; index < cvApprovalMaps.size(); index++){
                    RoutingMapBean routingMapBean = (RoutingMapBean)cvApprovalMaps.get(index);
                    cvRoutingDetails.add(routingMapBean.getRoutingMapDetails());
                }
            }
            if(cvRoutingDetails != null && cvRoutingDetails.size() > 0){
                for(int index = 0; index < cvRoutingDetails.size(); index++){
                    CoeusVector cvRoutingDetail = (CoeusVector)cvRoutingDetails.get(index);
                    if(cvRoutingDetail != null && cvRoutingDetail.size() > 0){
                        for(int index1 = 0; index1 < cvRoutingDetail.size(); index1++){
                            RoutingDetailsBean routingDetailsBean = (RoutingDetailsBean)cvRoutingDetail.get(index1);
                            cvApprovers.add(routingDetailsBean);
                        }
                    }
                }
            }
            sendNotification(cvApprovers,message,proposalNumber,userId);
        }
    }
    
    /*
     * Notify the users through inbox message and email and also to make a Notepad entry for the current proposal number
     * @param cvApprovers contains users who have already approved the proposal or 
       users for whom the proposal has been approved by others or user who have bypassed the proposal 
     * @param message is the message sent during notification
     * @param proposalNumber is the proposal number 
     * @param userId is the user making the changes as a result of which a notification is sent.
     * @throws Exception
     */
    private void sendNotification(CoeusVector cvApprovers,String message,String proposalNumber,String userId) throws Exception{
        Vector vecInbox = new Vector();
        Vector vecNotePad = new Vector();
        MessageBean messageBean = new MessageBean();
        CoeusVector cvApprove = new CoeusVector(); 
        //Case#4572 - Writing notepad entries and sending notifications irrespective of proposal status 
        CoeusVector cvPropApprovedApprovers = new CoeusVector();
        //Case#4572 - End
        for(int index=0;index<cvApprovers.size();index++) {
            InboxBean inboxBean = new InboxBean();
            RoutingDetailsBean routingDetailsBean = (RoutingDetailsBean)cvApprovers.get(index);
            if(routingDetailsBean.getApprovalStatus().equals("A") 
                || routingDetailsBean.getApprovalStatus().equals("O") 
                || routingDetailsBean.getApprovalStatus().equals("B")){
                String toUser = routingDetailsBean.getUserId();
                if(!cvApprove.contains(toUser)) {
                    //Case#4572 - Writing notepad entries and sending notifications irrespective of proposal status 
                    cvPropApprovedApprovers.add(routingDetailsBean);
                    //Case#4572 - End
                    messageBean.setAcType("I");
                    messageBean.setMessage(message);
                    
                    inboxBean.setMessageBean(messageBean);
                    inboxBean.setAcType("I");
                    inboxBean.setOpenedFlag('Y');
                    inboxBean.setFromUser(userId);
                    inboxBean.setProposalNumber(proposalNumber);
                    inboxBean.setModuleCode(3);
                    inboxBean.setToUser(toUser);
                    inboxBean.setSubjectType('N');
                    vecInbox.add(inboxBean);
                    cvApprove.add(toUser);
                }
            }
        }
        NotepadBean notepadBean = new NotepadBean();
        notepadBean.setAcType("I");
        notepadBean.setComments(message);
        notepadBean.setProposalAwardNumber(proposalNumber);
        notepadBean.setUpdateUser(userId);
        vecNotePad.add(notepadBean);
        
        ProposalActionUpdateTxnBean proposalActionUpdateTxnBean = new ProposalActionUpdateTxnBean();
        boolean isSuccess = proposalActionUpdateTxnBean.sendNotificationForNarrative(vecInbox, vecNotePad);
        if(isSuccess){
            //Modified for COEUSDEV-340 : Email that is generated when a narrative is changes does not have proposal and narrative details - Start
//            String subject = "Notification";
            //Commented with COEUSDEV-75:Rework email engine so the email body is picked up from one place
//            CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
//            MessageFormat formatter = new MessageFormat("");
//            String subject = formatter.format(coeusMessageResourcesBean.parseMessageKey("proposal_nar_mail_sub_exceptionCode.2000"),
//                    proposalNumber);
            //COEUSDEV-75:End
            //COEUSDEV-340 : end
            RoutingUpdateTxnBean routingUpdateTxnBean = new RoutingUpdateTxnBean(userId);
            //Modified for Case#4572 - Writing notepad entries and sending notifications irrespective of proposal status
            //Mail is send only to the proposal approved approvers
            //routingUpdateTxnBean.sendMailToApprovers(cvApprovers,message,subject);
            String messageBody = message;    
            if(cvPropApprovedApprovers != null && cvPropApprovedApprovers.size() >0 ){
                RoutingDetailsBean routingDetailsBean = (RoutingDetailsBean)cvPropApprovedApprovers.get(0);
                //Added for Case#4393 - make sure all Emails sent from Coeus are set thru the new JavaMail email engine  - Start
                RoutingTxnBean routingTxnBean = new RoutingTxnBean();
                //Modified for COEUSDEV-340 : Email that is generated when a narrative is changes does not have proposal and narrative details - Start
                //To append Narrative type and module description in the message
//                 messageBody = message+routingTxnBean.getMailBodyContent(
//                         3,proposalNumber,-1,
//                         routingDetailsBean.getRoutingNumber(),
//                         routingDetailsBean.getMapNumber(),
//                         routingDetailsBean.getStopNumber(),
//                         routingDetailsBean.getLevelNumber(),
//                         routingDetailsBean.getApproverNumber(),
//                         "","");
                 String msgBody = routingTxnBean.getMailBodyContent(
                        3,proposalNumber,-1,
                        routingDetailsBean.getRoutingNumber(),
                        routingDetailsBean.getMapNumber(),
                        routingDetailsBean.getStopNumber(),
                        routingDetailsBean.getLevelNumber(),
                        routingDetailsBean.getApproverNumber(),
                        "","");
                msgBody = msgBody.substring(8,msgBody.length());
                messageBody = messageBody+msgBody;
                //COEUSDEV-340 : end
                //Case#4393 - End
            }
            //COEUSDEV-75:Rework email engine so the email body is picked up from one place
//            routingUpdateTxnBean.sendMailToApprovers(cvPropApprovedApprovers,messageBody,subject);
            routingUpdateTxnBean.sendMailToApprovers(ModuleConstants.PROPOSAL_DEV_MODULE_CODE,MailActions.NARRATIVE_CHANGE,
                    proposalNumber,-1,messageBody, cvPropApprovedApprovers);
            //COEUSDEV:75 End
            //Case#4572 - End
        }
    }
    //Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - End
    
    private void setProposalStatus(HashMap hmProposalStatus, HttpServletRequest request)throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        webTxnBean.getResults(request, "setProposalStatus", hmProposalStatus);
    }
    
    //Added for COEUSDEV-340 : Email that is generated when a narrative is changes does not have proposal and narrative details - Start
    /*
     * Method to get narrative type description based on the narrative type code
     * @param vecNarrativeType
     * @param narrativeTypeCode
     * @return narrativeTypeDescription
     */
    private String getNarrativeTypeDescription(Vector vecNarrativeType,String narrativeTypeCode){
        if(vecNarrativeType!=null && vecNarrativeType.size() > 0){
            for(int index = 0 ; index < vecNarrativeType.size() ; index ++  ){
                NarrativeTypeBean narrativeTypeBean = (NarrativeTypeBean) vecNarrativeType.get(index);
                if(narrativeTypeBean.getNarrativeTypeCode() == Integer.parseInt(narrativeTypeCode)) {
                 return  narrativeTypeBean.getDescription();
                }
            }
        }
        return EMPTY_STRING;
    }
    //COEUSDEV-340 : End
    
    // Added COEUSQA-2416 : Error updating personnel attachments from lite after a proposal is submitted for approval - Start
    /*
     * Method to get attachment type description based on the attachment type code
     * @param vecAttachmentType
     * @param attachmentTypeCode
     * @return attachmentTypeDescription
     */
    private String getAttachmentTypeDescription(Vector vecAttachmentType,String attachmentTypeCode){
        if(vecAttachmentType!=null && vecAttachmentType.size() > 0){
            for(int index = 0 ; index < vecAttachmentType.size() ; index ++  ){
                ComboBoxBean attachmentTypeDetails = (ComboBoxBean) vecAttachmentType.get(index);
                if(attachmentTypeDetails.getCode().equals(attachmentTypeDetails)) {
                    return  attachmentTypeDetails.getDescription();
                }
            }
        }
        return EMPTY_STRING;
    }
    // Added COEUSQA-2416 : Error updating personnel attachments from lite after a proposal is submitted for approval - End

}
