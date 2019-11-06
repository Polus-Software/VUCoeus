/*
 * UploadDocumentAction.java
 *
 * Created on May 17, 2005, 2:26 PM
 ** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 26-JULY-2010
 * by Divya Susendran
 */
package edu.mit.coeuslite.iacuc.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentIdGenerator;
import edu.mit.coeuslite.iacuc.bean.ProtocolHeaderDetailsBean;
import java.util.Map;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtilsBean;
import java.sql.Timestamp;
import java.util.Vector;
import edu.mit.coeuslite.utils.bean.MenuBean;
//import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.iacuc.bean.ProtocolAuthorizationBean;
import edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean;
import edu.mit.coeus.iacuc.bean.ProtocolUpdateTxnBean;
import edu.mit.coeus.iacuc.bean.UploadDocumentBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
//import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
//import edu.mit.coeus.utils.CoeusProperties;
//import edu.mit.coeus.utils.CoeusPropertyKeys;
//import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
//import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.mit.coeuslite.iacuc.form.UploadForm;
import edu.mit.coeuslite.utils.LockBean;
import java.io.File;
import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
//import java.util.Date;
import java.util.HashMap;
//import java.util.Hashtable;
//import java.util.Iterator;
import java.util.List;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;



/**
 *
 * @author  shijiv
 */
public class UploadDocumentAction extends ProtocolBaseAction{
    
    //    private UploadDocumentBean dataBean;
    //    private Timestamp dbTimestamp;
    //    private ProtocolDataTxnBean protocolDataTxnBean;
    //    private DBEngineImpl dbEngine;
    //    private WebTxnBean webTxnBean;
    //    private HttpServletRequest request;
    //    private ActionForward actionForward;
    //    private BeanUtilsBean beanUtilsBean;
    //    private HttpSession session;
    private static final String UPLOAD_DOCUMENT_SAVE = "AC008";
    private static final String MENU_ITEMS ="iacucmenuItemsVector";
    //Removing instance variable case# 2960
    //private boolean isAllDeleted= false;
    //private boolean isMenuSaved = false;
    //    private static final String DATE_FORMAT="MMddyyyy-hhmmss";
    private static final String UPLOAD_DOCUMENTS_FIELD="UPLOAD_DOCUMENTS";
    //    private static final String UPDATE_MENU_CHECKlIST="updateMenuCheckList";
    private static final String AC_TYPE_INSERT="I";
    private static final String AC_TYPE_DELETE="D";
    private static final String AC_TYPE_UPDATE="U";
    private static final String EMPTY_STRING = "";
    // Code added for coeus 4.3 enhancement
    // Added for edit and show history functionality
    private static final String AC_TYPE_EDIT="E";
    private static final String AC_TYPE_HISTORY="H";
    //Case#4275 - upload attachments until in agenda - Start
    private static final int SUBMITTED_TO_IRB = 101;
    private static final int DOCUMENT_STATUS_FINALIZED = 2;
    boolean hasModifyAttachmentRight = false;
    //Case#4275 - End
    /** Creates a new instance of UploadDocumentAction */
    public UploadDocumentAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        edu.mit.coeuslite.iacuc.form.UploadForm uploadForm = (edu.mit.coeuslite.iacuc.form.UploadForm)form;
        //        UploadDocumentBean uploadDocumentBean = new UploadDocumentBean();
        String protocolNumber = null;
        //        int sequenceNum = 1;
        int seqNum = -1;
        String sequenceNumber = null;
        HttpSession session = request.getSession();
        //        WebTxnBean webTxnBean = new WebTxnBean();
        UserMaintDataTxnBean userTxnBean = new UserMaintDataTxnBean();
        // Code added for coeus 4.3 enhancement
        ActionForward actionForward = actionMapping.findForward("success");
        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
        if(session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId())!=null &&
        session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId())!=null){
            protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
            sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
        }
        //added start
        int versionNumber = -1;
        int documentType = -1;
        //added end
        
        //Added for Case#3036 - Didn't recognize document file type in Protocol attachments -Start
        int protoDocumentId = 0;
        //Added for Case#3036 - Didn't recognize document file type in Protocol attachments -End
        
        if(sequenceNumber!= null){
            seqNum = Integer.parseInt(sequenceNumber);
        }
        
        Vector vecDocTypes = (Vector)protocolDataTxnBean.getProtocolDocumetTypes();
        vecDocTypes = (vecDocTypes != null)?vecDocTypes:new Vector();
        session.setAttribute("DocTypes", vecDocTypes);
        Vector vecInsertData = new Vector();
        if(uploadForm.getAcType() != null){
            // To get the history for that particular document.
            if(uploadForm.getAcType()!=null && uploadForm.getAcType().equalsIgnoreCase(AC_TYPE_HISTORY)){
                String protoNum = request.getParameter("protocolNumber");
                if(protoNum != null && !protoNum.equals(EMPTY_STRING)){
                    String docId = new Integer(uploadForm.getDocumentId()).toString();
                    int docVersionNum = uploadForm.getVersionNumber();// Added to filter the history docs
                    Vector vecUploadHistoryData = (Vector)protocolDataTxnBean.getProtocolHistoryData(protoNum, docId);
                    // filtering docs from the vector vecUploadHistoryData  -start
                    Vector vecNonRepeatedDocs = new Vector();
                    if(vecUploadHistoryData != null && vecUploadHistoryData.size() >0){
                        for(int index =0 ; index< vecUploadHistoryData.size();index++){
                            UploadDocumentBean historyDataBean = (UploadDocumentBean)vecUploadHistoryData.get(index);
                            if(Integer.parseInt(docId) ==  historyDataBean.getDocumentId() && docVersionNum != historyDataBean.getVersionNumber()){
                                vecNonRepeatedDocs.add(historyDataBean);
                            }
                        }
                    }
                    request.setAttribute("historyData", vecNonRepeatedDocs);
                    //request.setAttribute("historyData", vecUploadHistoryData);
                    // filtering docs from the vector vecUploadHistoryData  - End
                    request.setAttribute("historyDataDocId", new Integer(uploadForm.getDocumentId()));
                }
                return actionForward;
            }
            // Code added and modified for coeus 4.3 enhancement
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            ProtocolUpdateTxnBean updateBean = new ProtocolUpdateTxnBean(userInfoBean.getUserId());
            LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()), request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            //Added for Case#4275 - upload attachments until in agenda - Start
            boolean lockedInDisplay = false;
            if(isLockExists){
                lockProtocol(protocolNumber, request);
                lockedInDisplay = true;
            }
            //Case#4275 - End
            LockBean lockData = getLockedData(CoeusLiteConstants.IACUC_PROTO_LOCK_STR+lockBean.getModuleNumber(), request);
            //Modified for Case#4275 - upload attachments until in agenda
            //if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId()) &&
            if(lockBean.getSessionId().equals(lockData.getSessionId()) &&
                    !uploadForm.getAcType().equalsIgnoreCase(AC_TYPE_HISTORY)) {
                
                if(uploadForm.getAcType().equalsIgnoreCase(AC_TYPE_INSERT)){
                    uploadForm.setProtocolNumber(protocolNumber);
                    uploadForm.setSequenceNumber(seqNum);
                    uploadForm.setDocument(uploadForm.getDocument());
                    uploadForm.setFileBytes(uploadForm.getDocument().getFileData());
                    uploadForm.setFileName(uploadForm.getDocument().getFileName());
                    //Added for Case#3533 Losing attachments - Start
                    uploadForm.setFileNameHidden(uploadForm.getDocument().getFileName());
                    //Added for Case#3533 Losing attachments - End
                    //added  for enhancement start
                    uploadForm.setDocCode(uploadForm.getDocCode());
                    uploadForm.setDescription(uploadForm.getDescription().trim());
                    uploadForm.setStatusCode(1);
                    uploadForm.setAcType(AC_TYPE_INSERT);
                    // Code commented for coeus 4.3 enhancement - starts
                    //                    uploadDocumentBean.setDocCode(Integer.parseInt(uploadForm.getDocCode()));
                    //                    uploadDocumentBean.setStatusCode(1);
                    //                    List vecData = checkDraftDocAvailable(uploadDocumentBean.getDocCode(), request);
                    //                    if(vecData != null && vecData.size() > 0 ) {
                    //                        boolean isDraftExist = ((Boolean)vecData.get(0)).booleanValue();
                    //                        if(isDraftExist) {
                    //                            uploadForm.setAcType(AC_TYPE_UPDATE);
                    //                            uploadForm.setDocCode(uploadForm.getDocCode());
                    //                            uploadForm.setFileName(uploadForm.getDocument().getFileName());
                    //                            uploadForm.setDocument(uploadForm.getDocument());
                    //                            uploadForm.setDescription(uploadForm.getDescription().trim());
                    //                            uploadForm.setProtocolNumber(protocolNumber);
                    //                            uploadForm.setSequenceNumber(seqNum);
                    //                            uploadForm.setVersionNumber(((Integer)vecData.get(1)).intValue());
                    //
                    //                        }
                    //                    }
                    // Code commented for coeus 4.3 enhancement - ends
                    // Code added for coeus 4.3 enhancement - starts.
                    // To insert the document with updated versionNumber and documentId.
                    int docId = updateBean.getNextUploadID(uploadForm.getProtocolNumber(),
                            uploadForm.getSequenceNumber());
                    int versionNo = updateBean.getNextVersionNumber(uploadForm.getProtocolNumber(),
                            uploadForm.getSequenceNumber(), new Integer(uploadForm.getDocCode()).intValue(), docId);
                    uploadForm.setVersionNumber(versionNo);
                    // Code added for coeus 4.3 enhancement - ends
                    vecInsertData = insertDocoment(uploadForm,protocolNumber,seqNum, request);
                    //                    vecInsertData = (vecInsertData != null && vecInsertData.size() > 0 )?vecInsertData: new Vector();
                    //                    Vector vecUploadHistoryData = (Vector)protocolDataTxnBean.getProtocolHistoryData(protocolNumber);
                    //                    session.setAttribute("historyData", (vecUploadHistoryData != null && vecUploadHistoryData.size() > 0) ?vecUploadHistoryData: new Vector());
                    //                vecInsertData = (Vector) getDocumentForVersion(vecInsertData);
                    //                    session.setAttribute("uploadLatestData", (vecInsertData != null && vecInsertData.size() > 0) ?vecInsertData: new Vector());
                    // To filter the active documents for the protocol.
                    //                    session.setAttribute("uploadAllData", protocolDataTxnBean.getProtocolHistoryData(protocolNumber));
                    HashMap hmFromUsers = new HashMap();
                    // To filter the Active records
                    if(vecInsertData!=null && vecInsertData.size()>0){
                        for(int index=0; index<vecInsertData.size(); index++){
                            UploadDocumentBean bean = (UploadDocumentBean) vecInsertData.get(index);
                            if(!hmFromUsers.containsKey(bean.getUpdateUser())){
                                String COIUserName = userTxnBean.getUserName(bean.getUpdateUser());
                                hmFromUsers.put(bean.getUpdateUser(), COIUserName);
                            }
                            if(bean.getStatusCode()==3){
                                vecInsertData.remove(index--);
                                for(int count=0; count<vecInsertData.size(); count++){
                                    UploadDocumentBean beanData = (UploadDocumentBean) vecInsertData.get(count);
                                    if(bean.getDocumentId() == beanData.getDocumentId()){
                                        vecInsertData.remove(count--);
                                    }
                                }
                            }
                        }
                    }
                    // Add the non active records at the bottom of the active records.
                    Vector vecListData = (Vector)protocolDataTxnBean.getUploadDocumentForProtocol(protocolNumber);
                    if(vecListData!=null && vecListData.size()>0){
                        for(int index=0; index<vecListData.size(); index++){
                            UploadDocumentBean bean = (UploadDocumentBean) vecListData.get(index);
                            if(bean.getStatusCode()==3){
                                vecInsertData.add(bean);
                            }
                        }
                    }
                    session.setAttribute("uploadLatestData", vecInsertData);
                    session.setAttribute("hmFromUsers", hmFromUsers);
                    //added  for enhancement end
                    
                    //commented for enhancement start
                    //request.setAttribute("uploadData", cvInsertData);
                    //commented for enhancement end
                    
                }else if(uploadForm.getAcType().equalsIgnoreCase(AC_TYPE_DELETE)){
                    int seqNumber = 0;
                    //commented for enhancement start
                    //Vector vcData = deleteDocument(uploadForm.getUploadId(),protocolNumber,seqNum);
                    //commented for enhancement end
                    if((request.getParameter("versionNumber")) != null && (request.getParameter("docType") != null)){
                        versionNumber = Integer.parseInt(request.getParameter("versionNumber"));
                        documentType = Integer.parseInt(request.getParameter("docType"));
                        seqNumber = (request.getParameter("seqNum"))!=null ? Integer.parseInt(request.getParameter("seqNum")):seqNum;
                    }
                    Vector vecDeleteData = new Vector();
                    //                    vecDeleteData = deleteDocument(versionNumber,documentType,protocolNumber,seqNum, request, uploadForm.getDocumentId());
                    //vecDeleteData = (Vector) getDocumentForVersion(vecDeleteData);
                    int status = (request.getParameter("status"))!=null ? Integer.parseInt(request.getParameter("status")): 0;
                    // if the status  is finalized then insert one record with the status deleted.
                    if(status == 2){
                        Vector vecLatest = (Vector) session.getAttribute("uploadLatestData");
                        if(vecLatest!=null && vecLatest.size()>0){
                            for(int index=0; index<vecLatest.size(); index++){
                                UploadDocumentBean bean = (UploadDocumentBean) vecLatest.get(index);
                                if(bean.getProtocolNumber().equals(protocolNumber) &&
                                        bean.getSequenceNumber() == seqNumber &&
                                        bean.getVersionNumber() == versionNumber &&
                                        bean.getDocumentId() == uploadForm.getDocumentId()){
                                    bean.setVersionNumber(bean.getVersionNumber()+1);
                                    bean.setStatusCode(3);
                                    bean.setDocument(new byte[1]);
                                    bean.setAcType(AC_TYPE_INSERT);
                                    bean.setAmended(true);
                                    vecDeleteData = (Vector) updUploadDocument(bean, protocolNumber, seqNum, request);
                                }
                            }
                        }
                        // Delete the draft record
                    } else {
                        vecDeleteData = deleteDocument(versionNumber,documentType,protocolNumber,seqNumber, request, uploadForm.getDocumentId());
                    }
                    //                    session.setAttribute("uploadLatestData", (vecDeleteData != null && vecDeleteData.size() > 0 ) ? (Vector)vecDeleteData :new Vector());
                    
                    //                    session.setAttribute("uploadAllData", protocolDataTxnBean.getProtocolHistoryData(protocolNumber));
                    //                    vecDeleteData = (Vector)protocolDataTxnBean.getUploadDocumentForProtocol(protocolNumber);
                    // To filter the active documents for the protocol.
                    HashMap hmFromUsers = new HashMap();
                    // To filter the Active records
                    if(vecDeleteData!=null && vecDeleteData.size()>0){
                        for(int index=0; index<vecDeleteData.size(); index++){
                            UploadDocumentBean bean = (UploadDocumentBean) vecDeleteData.get(index);
                            if(!hmFromUsers.containsKey(bean.getUpdateUser())){
                                String COIUserName = userTxnBean.getUserName(bean.getUpdateUser());
                                hmFromUsers.put(bean.getUpdateUser(), COIUserName);
                            }
                            if(bean.getStatusCode()==3){
                                vecDeleteData.remove(index--);
                                for(int count=0; count<vecDeleteData.size(); count++){
                                    UploadDocumentBean beanData = (UploadDocumentBean) vecDeleteData.get(count);
                                    if(bean.getDocumentId() == beanData.getDocumentId()){
                                        vecDeleteData.remove(count--);
                                    }
                                }
                            }
                        }
                    }
                    // Add the non active records at the bottom of the active records.
                    Vector vecListData = (Vector)protocolDataTxnBean.getUploadDocumentForProtocol(protocolNumber);
                    if(vecListData!=null && vecListData.size()>0){
                        for(int index=0; index<vecListData.size(); index++){
                            UploadDocumentBean bean = (UploadDocumentBean) vecListData.get(index);
                            if(bean.getStatusCode()==3){
                                vecDeleteData.add(bean);
                            }
                        }
                    }
                    session.setAttribute("uploadLatestData", vecDeleteData);
                    session.setAttribute("hmFromUsers", hmFromUsers);
                    //                    Vector vecUploadHistoryData = (Vector)protocolDataTxnBean.getProtocolHistoryData(protocolNumber);
                    //                    session.setAttribute("historyData", (vecUploadHistoryData != null && vecUploadHistoryData.size() > 0) ?vecUploadHistoryData: new Vector());
                    //Commented for instance variable case#2960.
//                    if(vecDeleteData==null){
//                        isAllDeleted = true;
//                        isMenuSaved = false;
//                    }else{
//                        isMenuSaved = true;
//                        isAllDeleted = false;
//                    }
                    // HashMap hmSaveStatus = updateSaveStatus(uploadForm,session,protocolNumber,isMenuSaved);
                    // Update Save Status for the Menu check
                    //Hashtable updateSaveStatsu=(Hashtable)webTxnBean.getResults(request, UPDATE_MENU_CHECKlIST, hmSaveStatus);
                    //updateSaveStatusToSession(session,isMenuSaved);
                    
                    //commented for enhancement start
                    //                    request.setAttribute("uploadData", vcData);
                    //commented for enhancement End
                    
                    uploadForm.setAcType(null);
                    // Code added and modified for coeus 4.3 enhancement - starts
                }else if(uploadForm.getAcType().equalsIgnoreCase(AC_TYPE_EDIT)){
                    Vector vecUploadList = new Vector();
                    if(uploadForm.getParent()!=null && uploadForm.getParent().equals("P")){
                        // Check the current protocol is amendable or not.
                        //boolean isAmendable = protocolDataTxnBean.isDocumentAmendable(protocolNumber.substring(0,10), uploadForm.getDocumentId());  
                        //COEUSQA-2431 checking whether the document is already amended - start
                        boolean isAmendable = protocolDataTxnBean.isProtocolDocumentAmend(protocolNumber, seqNum, uploadForm.getDocumentId());
                        //COEUSQA-2431 - end
                        if(!isAmendable){
                            uploadForm.setDocType(EMPTY_STRING);
                            ActionMessages messages = new ActionMessages();
                            messages.add("amendMsg", new ActionMessage("error.upload_amend"));
                            saveMessages(request, messages);
                            uploadForm.setAcType(AC_TYPE_INSERT);
                            uploadForm.setParent("N");
                            uploadForm.setDescription(EMPTY_STRING);
                            uploadForm.setFileName(EMPTY_STRING);
                            //Added for Case#3533 Losing attachments - Start
                            uploadForm.setFileNameHidden(EMPTY_STRING);
                            //Added for Case#3533 Losing attachments - End
                            uploadForm.setDocType(EMPTY_STRING);
                            return actionForward;
                        }
                        vecUploadList = (Vector) session.getAttribute("parentIacucData");
                    } else if(uploadForm.getParent()!=null && uploadForm.getParent().equals("N")){
                        vecUploadList = (Vector) session.getAttribute("uploadLatestData");
                    }
                    // set the field values for the edited document
                    for(int index=0; index<vecUploadList.size(); index++){
                        UploadDocumentBean updBean = (UploadDocumentBean) vecUploadList.get(index);
                        if(updBean.getDocumentId()==uploadForm.getDocumentId() &&
                                updBean.getVersionNumber()==uploadForm.getVersionNumber()){
                            uploadForm.setDocCode((new Integer(updBean.getDocCode())).toString());
                            uploadForm.setDescription(updBean.getDescription());
                            uploadForm.setFileName(updBean.getFileName());
                            //Added for Case#3533 Losing attachments - Start
                            uploadForm.setFileNameHidden(updBean.getFileName());
                            //Added for Case#3533 Losing attachments - End
                            uploadForm.setDocType((new Integer(updBean.getDocCode())).toString());
                            uploadForm.setStatusCode(updBean.getStatusCode());
                            uploadForm.setAcType(AC_TYPE_UPDATE);
                            /*COEUSQA_1724-2596_Attachment Modifications Unable to be Saved when a New Protocol is 
                             Rejected in Routing or Requires Specific Minor Revisions (Lite 4.4.1 TEST)_start*/
                            // Setting the sequence number to the selected record                            
                            uploadForm.setSequenceNumber(updBean.getSequenceNumber());
                            /*COEUSQA_1724-2596_Attachment Modifications Unable to be Saved when a New Protocol is 
                             Rejected in Routing or Requires Specific Minor Revisions (Lite 4.4.1 TEST)_end */                          
                            return actionForward;
                        }
                    }
                }else if(uploadForm.getAcType().equalsIgnoreCase(AC_TYPE_UPDATE)){
                    uploadForm.setProtocolNumber(protocolNumber);
                    uploadForm.setSequenceNumber(seqNum);
                    /*COEUSQA-2954 Attachment history should keep previous version of the attachment after it 
                                                   is changed in response to a request for revisions-Start */
                    if(uploadForm.getStatusCode() == DOCUMENT_STATUS_FINALIZED){
                        uploadForm.setAcType(AC_TYPE_INSERT);
                        uploadForm.setVersionNumber(uploadForm.getVersionNumber()+1);
                    }
                    /*COEUSQA-2954 Attachment history should keep previous version of the attachment after it 
                                                   is changed in response to a request for revisions-End */
                    if(uploadForm.getDocument()!=null && uploadForm.getDocument().getFileSize()>0){
                        uploadForm.setFileBytes(uploadForm.getDocument().getFileData());
                        uploadForm.setFileName(uploadForm.getDocument().getFileName());
                        //Added for Case#3533 Losing attachments - Start
                        uploadForm.setFileNameHidden(uploadForm.getDocument().getFileName());
                        //Added for Case#3533 Losing attachments - End                        
                        /*COEUSQA_1724-2596_Attachment Modifications Unable to be Saved when a New Protocol is 
                         Rejected in Routing or Requires Specific Minor Revisions (Lite 4.4.1 TEST)_Start*/
                         //Setting the sequence number to the selected record                        
                        Vector vecUploadList = new Vector();
                        if(uploadForm.getParent()!=null && uploadForm.getParent().equals("N")){
                            vecUploadList = (Vector) session.getAttribute("uploadLatestData");
                        }                        
                        if(vecUploadList != null){
                            UploadDocumentBean updBean = null;
                            for(Object uploadDocbean:vecUploadList){
                                updBean = (UploadDocumentBean)uploadDocbean;
                                if(updBean != null){
                                    if(updBean.getDocumentId()==uploadForm.getDocumentId() &&
                                            updBean.getVersionNumber()==uploadForm.getVersionNumber()){
                                        uploadForm.setDescription(uploadForm.getDescription().trim());
                                        uploadForm.setSequenceNumber(updBean.getSequenceNumber());
                                        break;
                                    }
                                }
                            }                            
                        }
                        /*COEUSQA_1724-2596_Attachment Modifications Unable to be Saved when a New Protocol is 
                         Rejected in Routing or Requires Specific Minor Revisions (Lite 4.4.1 TEST)_end*/
                    } else {
                        Vector vecUploadList = new Vector();
                        if(uploadForm.getParent()!=null && uploadForm.getParent().equals("P")){
                            vecUploadList = (Vector) session.getAttribute("parentIacucData");
                        } else if(uploadForm.getParent()!=null && uploadForm.getParent().equals("N")){
                            vecUploadList = (Vector) session.getAttribute("uploadLatestData");
                        }
                        for(int index=0; index<vecUploadList.size(); index++){
                            UploadDocumentBean updBean = (UploadDocumentBean) vecUploadList.get(index);
                            if(updBean.getDocumentId()==uploadForm.getDocumentId() &&
                                    updBean.getVersionNumber()==uploadForm.getVersionNumber()){
                                uploadForm.setFileName(updBean.getFileName());
                                //Added for Case#3533 Losing attachments - Start
                                uploadForm.setFileNameHidden(updBean.getFileName());
                                //Added for Case#3533 Losing attachments - End
                                uploadForm.setFileBytes(updBean.getDocument());
                                /*COEUSQA_1724-2596_Attachment Modifications Unable to be Saved when a New Protocol is 
                                 Rejected in Routing or Requires Specific Minor Revisions (Lite 4.4.1 TEST)_start*/
                                 // Setting the sequence number to the selected record
                                uploadForm.setDescription(uploadForm.getDescription().trim());
                                uploadForm.setSequenceNumber(updBean.getSequenceNumber());
                                /*COEUSQA_1724-2596_Attachment Modifications Unable to be Saved when a New Protocol is 
                                 Rejected in Routing or Requires Specific Minor Revisions (Lite 4.4.1 TEST)_End*/
                                break;
                            }
                        }
                    }
                    
                    uploadForm.setDocCode(uploadForm.getDocCode());
                    uploadForm.setDescription(uploadForm.getDescription().trim());
                    // If it is Amendment protocol then set the incremented version number.
                    //Modified  for COEUSDEV-322 :  Premium - Protocol attachments - Delete Document line when a Document was removed resulting in no document being stored - Start                    
//                    if(uploadForm.getStatusCode()==2 || uploadForm.getStatusCode()==3 ||
                    if(uploadForm.getStatusCode()==3 || //COEUSDEV-322 : End
                            (uploadForm.getParent()!=null && uploadForm.getParent().equals("P"))){
                        uploadForm.setAcType(AC_TYPE_INSERT);
                        int versionNo = updateBean.getNextVersionNumber(uploadForm.getProtocolNumber(),
                                uploadForm.getSequenceNumber(), new Integer(uploadForm.getDocCode()).intValue(), uploadForm.getDocumentId());
                        uploadForm.setVersionNumber(versionNo);
                        uploadForm.setStatusCode(1);
                        uploadForm.setParent("P");
                    }
                    vecInsertData = insertDocoment(uploadForm,protocolNumber,seqNum, request);
                    vecInsertData = (vecInsertData != null && vecInsertData.size() > 0 )?vecInsertData: new Vector();
                    //                    Vector vecUploadHistoryData = (Vector)protocolDataTxnBean.getProtocolHistoryData(protocolNumber);
                    //                    session.setAttribute("historyData", (vecUploadHistoryData != null && vecUploadHistoryData.size() > 0) ?vecUploadHistoryData: new Vector());
                    //                vecInsertData = (Vector) getDocumentForVersion(vecInsertData);
                    //                    session.setAttribute("uploadLatestData", (vecInsertData != null && vecInsertData.size() > 0) ?vecInsertData: new Vector());
                    
                    //                    session.setAttribute("uploadAllData", protocolDataTxnBean.getProtocolHistoryData(protocolNumber));
                    //                    vecInsertData = (Vector)protocolDataTxnBean.getUploadDocumentForProtocol(protocolNumber);
                    // To filter the active documents for the protocol.
                    HashMap hmFromUsers = new HashMap();
                    // To filter the Active records
                    if(vecInsertData!=null && vecInsertData.size()>0){
                        for(int index=0; index<vecInsertData.size(); index++){
                            UploadDocumentBean bean = (UploadDocumentBean) vecInsertData.get(index);
                            if(!hmFromUsers.containsKey(bean.getUpdateUser())){
                                String COIUserName = userTxnBean.getUserName(bean.getUpdateUser());
                                hmFromUsers.put(bean.getUpdateUser(), COIUserName);
                            }
                            if(bean.getStatusCode()==3){
                                vecInsertData.remove(index--);
                                for(int count=0; count<vecInsertData.size(); count++){
                                    UploadDocumentBean beanData = (UploadDocumentBean) vecInsertData.get(count);
                                    if(bean.getDocumentId() == beanData.getDocumentId()){
                                        vecInsertData.remove(count--);
                                    }
                                }
                            }
                        }
                    }
                    // Add the non active records at the bottom of the active records.
                    Vector vecListData = (Vector)protocolDataTxnBean.getUploadDocumentForProtocol(protocolNumber);
                    if(vecListData!=null && vecListData.size()>0){
                        for(int index=0; index<vecListData.size(); index++){
                            UploadDocumentBean bean = (UploadDocumentBean) vecListData.get(index);
                            if(bean.getStatusCode()==3){
                                vecInsertData.add(bean);
                            }
                        }
                    }
                    session.setAttribute("uploadLatestData", vecInsertData);
                    session.setAttribute("hmFromUsers", hmFromUsers);
                    //added  for enhancement end
                    
                    //commented for enhancement start
                    //request.setAttribute("uploadData", cvInsertData);
                    //commented for enhancement end
                    
                }
                //Case#4275 - upload attachments until in agenda - Start
                if(lockedInDisplay){
                    releaseLock(lockBean, request);
                }
                //Case#4275 - End
            } else {
                String errMsg = "release_lock_for";
                ActionMessages messages = new ActionMessages();
                messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                saveMessages(request, messages);
            }
            if(request.getParameter("acType").equalsIgnoreCase("V")) {
                if((request.getParameter("versionNumber")) != null && (request.getParameter("docType") != null)){
                    versionNumber = Integer.parseInt(request.getParameter("versionNumber"));
                    documentType = Integer.parseInt(request.getParameter("docType"));
                }
                
                ProtocolHeaderDetailsBean headerBean = (ProtocolHeaderDetailsBean)session.getAttribute("iacucHeaderBean");
                int protocolStatusCode  = 0;
                if(headerBean != null) {
                    protocolStatusCode = headerBean.getProtocolStatusCode();
                }
                
                //Added for Case#3036 - Didn't recognize document file type in Protocol attachments -Start
                if((request.getParameter("protocDocId")) != null){
                    protoDocumentId = Integer.parseInt(request.getParameter("protocDocId"));
                }
                //Added for Case#3036 - Didn't recognize document file type in Protocol attachments -End
                
                if(request.getParameter("Parent") != null && request.getParameter("Parent").equals("P")){
                    protocolNumber = protocolNumber.substring(0,10);
                }
                int viewSequenceNumber = Integer.parseInt(request.getParameter("SeqNumber"));
                //Modified for Case#3036 - Didn't recognize document file type in Protocol attachments -Start
//                String templateURL= viewDocument(versionNumber,documentType,request,protocolNumber,viewSequenceNumber );
                String templateURL= viewDocument(versionNumber,documentType,request,protocolNumber,viewSequenceNumber,protocolStatusCode,protoDocumentId );
                //Modified for Case#3036 - Didn't recognize document file type in Protocol attachments -End
                session.setAttribute("url", templateURL);
                response.sendRedirect(request.getContextPath()+templateURL);
                uploadForm.setAcType(null);
                return null;
            }
        }
        //Added for Case#3533 Losing attachments - Start
        session.removeAttribute("protocolUploadDocument");
        //Added for Case#3533 Losing attachments - End
        uploadForm = resetData(uploadForm);
        readSavedStatus(request);
        uploadForm.reset(actionMapping,request);
        return actionForward;
    }
    
    //
    //    private String viewDocument(int uploadId,HttpServletRequest request,String protocolNumber,int sequenceNum) throws Exception{
    //        String docType=null;
    //        String templateURL=null;
    //        byte[] fileData =null;
    //        UploadDocumentBean deleteBean = protocolDataTxnBean.getUploadDocumentForUpLoadId(protocolNumber,sequenceNum,uploadId);
    //        if(deleteBean == null) {
    //            throw new Exception("Requested file is not found");
    //        }
    //            docType= deleteBean.getDocType();
    //            fileData=deleteBean.getDocument();
    //            String serverPath = session.getServletContext().getRealPath("/");
    //            String reportPath = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH);
    //            String filePath = serverPath+/*File.separator+*/reportPath;
    //            File reportDir = new File(filePath);
    //            if(!reportDir.exists()){
    //                reportDir.mkdirs();
    //            }
    //            File reportFile=null;
    //            SimpleDateFormat dateFormat= new SimpleDateFormat(DATE_FORMAT);
    //            String ext = deleteBean.getFileName();
    //            ext = ext.substring(ext.lastIndexOf('.')+1);
    //            reportFile = new File(reportDir + File.separator + "document"+dateFormat.format(new Date())+"."+ext);
    //            reportFile.deleteOnExit();
    //            FileOutputStream fos = new FileOutputStream(reportFile);
    //            fos.write( fileData,0,fileData.length );
    //            fos.close();
    //            String url="/"+reportPath + "/" + reportFile.getName();
    //            templateURL = url;
    //         return templateURL;
    //    }
    //
    /*
    private String viewDocument(int versionNumber,int docType,HttpServletRequest request,
        String protocolNumber,int sequenceNum) throws Exception{
        String templateURL=null;
        byte[] fileData =null;
        HttpSession session = request.getSession();
        //commented for enhancement start
        //        UploadDocumentBean deleteBean = protocolDataTxnBean.getUploadDocumentForUpLoadId(protocolNumber,sequenceNum,uploadId);
        //commented for enhancement start
     
        //Added for enhancement start
        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
        UploadDocumentBean deleteBean = protocolDataTxnBean.getUploadDocumentForVersionNumber(protocolNumber,sequenceNum,docType,versionNumber);
        //Added for enhancement end
     
        if(deleteBean == null) {
            throw new Exception("Requested file is not found");
        }
        fileData=deleteBean.getDocument();
        String serverPath = session.getServletContext().getRealPath("/");
        String reportPath = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH);
        String filePath = serverPath+reportPath;
        File reportDir = new File(filePath);
        if(!reportDir.exists()){
            reportDir.mkdirs();
        }
        File reportFile=null;
        SimpleDateFormat dateFormat= new SimpleDateFormat(DATE_FORMAT);
        String ext = deleteBean.getFileName();
        ext = ext.substring(ext.lastIndexOf('.')+1);
        reportFile = new File(reportDir + File.separator + "document"+dateFormat.format(new Date())+"."+ext);
        reportFile.deleteOnExit();
        FileOutputStream fos = new FileOutputStream(reportFile);
        fos.write( fileData,0,fileData.length );
        fos.close();
        String url="/"+reportPath + "/" + reportFile.getName();
        templateURL = url;
        return templateURL;
    }*/
    //Modified for Case#3036 - Didn't recognize document file type in Protocol attachments -Start
//    private String viewDocument(int versionNumber,int docType,HttpServletRequest request,
//    String protocolNumber,int sequenceNum) throws Exception{
    private String viewDocument(int versionNumber,int docType,HttpServletRequest request,
            String protocolNumber,int sequenceNum, int statusCode, int viewDocId) throws Exception{
        //Modified for Case#3036 - Didn't recognize document file type in Protocol attachments -End
        DocumentBean documentBean = new DocumentBean();
        Map map = new HashMap();
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.iacuc.ProtocolDocumentReader");
        
        UploadDocumentBean uploadDocumentBean = new UploadDocumentBean();
        uploadDocumentBean.setProtocolNumber(protocolNumber);
        uploadDocumentBean.setDocCode(docType);
        uploadDocumentBean.setVersionNumber(versionNumber);
        uploadDocumentBean.setSequenceNumber(sequenceNum);
        //Added for Case#3036 - Didn't recognize document file type in Protocol attachments -Start
        uploadDocumentBean.setDocumentId(viewDocId);
        //Added for Case#3036 - Didn't recognize document file type in Protocol attachments -End
        uploadDocumentBean.setStatusCode(statusCode);
        map.put("UPLOAD_DOC_BEAN", uploadDocumentBean);
        
        documentBean.setParameterMap(map);
        
        String docId = DocumentIdGenerator.generateDocumentId();
        
        //StringBuffer strBuff = request.getRequestURL();
        StringBuffer stringBuffer = new StringBuffer();
        //String strPath = new String(strBuff);
        //strPath = strPath.substring(0,strPath.lastIndexOf('/'));
        //stringBuffer.append(strPath);
        stringBuffer.append("/StreamingServlet");
        stringBuffer.append("?");
        stringBuffer.append(DocumentConstants.DOC_ID);
        stringBuffer.append("=");
        stringBuffer.append(docId);
        
        request.getSession().setAttribute(docId, documentBean);
        
        return stringBuffer.toString();
    }
    
    
    /* This method deltes the selectes document*/
    private Vector deleteDocument(int versionNumber,int documentType,
            String protocolNumber,int sequenceNum, HttpServletRequest request, int docId) throws DBException,CoeusException{
        //added for enhancement start
        Vector vecDeleteData  = new Vector();
        UploadDocumentBean deleteBean = new UploadDocumentBean() ;
        deleteBean.setProtocolNumber(protocolNumber);
        deleteBean.setDocCode(documentType);
        deleteBean.setVersionNumber(versionNumber);
        deleteBean.setSequenceNumber(sequenceNum);
        deleteBean.setAcType(AC_TYPE_DELETE);
        deleteBean.setAw_docCode(documentType);
        deleteBean.setAw_versionNumber(versionNumber);
        deleteBean.setAw_protocolNumber(protocolNumber);
        deleteBean.setAw_sequenceNumber(sequenceNum);
        deleteBean.setAw_documentId(docId);
        vecDeleteData = (Vector)updUploadDocument(deleteBean,protocolNumber,sequenceNum, request);
        return vecDeleteData;
        //added for enhancement end
        
        //code commented for enhancement start
        //         Vector vecUploadData = new Vector();
        //        webTxnBean=new WebTxnBean();
        //        Vector cvUploadData = (Vector)protocolDataTxnBean.getUploadDocumentForProtocol(protocolNumber,sequenceNum);
        //        for(int index=0; index<cvUploadData.size(); index++) {
        //            UploadDocumentBean deleteBean = (UploadDocumentBean)cvUploadData.get(index);
        //            if(deleteBean.getUploadId()== uploadId) {
        //                deleteBean.setAcType(AC_TYPE_DELETE);
        //                vcUploadData = (Vector)updUploadDocument(deleteBean,protocolNumber,sequenceNum);
        //                break;
        //            }
        //        }
        //        return vecUploadData;
        //code commented for enhancement end
    }
    
    /*This method adds the document into the database */
    private Vector insertDocoment(UploadForm uploadForm,String protocolNumber,
            int seqNum, HttpServletRequest request) throws DBException, CoeusException,NumberFormatException,Exception{
        Vector vecUploadData = null;
        HttpSession session = request.getSession();
        //        WebTxnBean webTxnBean=new WebTxnBean();
        //Commented for instance variable case#2960.
//        isAllDeleted= false;
//        isMenuSaved = false;
        
        //Modified for Case#4275 - - upload attachments until in agenda - Start
        //Document status is set to Finalized when the document is upload in display mode
        ProtocolHeaderDetailsBean headerBean = (ProtocolHeaderDetailsBean)session.getAttribute("iacucHeaderBean");
        if(headerBean != null && headerBean.getProtocolStatusCode() == SUBMITTED_TO_IRB){
            String ProtocolLeadUnit = headerBean.getUnitNumber();
            //Checks whether user can upload documents in display mode,
            UserInfoBean  userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            ProtocolAuthorizationBean protocolAuthorizationBean = new ProtocolAuthorizationBean();
            boolean hasModifyAttachmentRight =  protocolAuthorizationBean.hasModifyAttachmentRights(userInfoBean.getUserId(), protocolNumber,ProtocolLeadUnit);
            //If user can upload the documents then set document status code as Finalized
            if(hasModifyAttachmentRight && uploadForm.getStatusCode() == 1){
                uploadForm.setStatusCode(DOCUMENT_STATUS_FINALIZED);
            }
        }
        //Case#4275 - End
        BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
        UploadDocumentBean uploadDocumentBean = new UploadDocumentBean();
        beanUtilsBean.copyProperties(uploadDocumentBean,uploadForm);
        uploadDocumentBean.setDocument(uploadForm.getFileBytes());
        uploadDocumentBean.setAw_protocolNumber(uploadDocumentBean.getProtocolNumber());
        uploadDocumentBean.setAw_sequenceNumber(uploadDocumentBean.getSequenceNumber());
        uploadDocumentBean.setAw_docCode(uploadDocumentBean.getDocCode());
        uploadDocumentBean.setAw_versionNumber(uploadForm.getVersionNumber());
        uploadDocumentBean.setAw_documentId(uploadForm.getDocumentId());
        if(uploadForm.getParent()!=null && uploadForm.getParent().equals("P")){
            uploadDocumentBean.setAmended(true);
        }
        /*COEUSQA-2954 Attachment history should keep previous version of the attachment after it 
                                       is changed in response to a request for revisions-Start */
        else if(uploadForm.getParent()!=null && uploadForm.getParent().equals("N")){
            uploadDocumentBean.setAmended(true);
        }
        /*COEUSQA-2954 Attachment history should keep previous version of the attachment after it 
                                         is changed in response to a request for revisions-End */
        //commented for enhancement start
        //uploadDocumentBean.setAcType(AC_TYPE_INSERT);
        //commented for enhancement end
        //Modified for instance variable case#2960.
//        isMenuSaved=true;
        boolean isMenuSaved=true;
        vecUploadData = (Vector)updUploadDocument(uploadDocumentBean,protocolNumber,seqNum, request);
        HashMap hmSaveStatus = updateSaveStatus(uploadForm,protocolNumber,isMenuSaved, request);
        // Update Save Status for the Menu check
        // webTxnBean.getResults(request, UPDATE_MENU_CHECKlIST, hmSaveStatus);
        // Update the Menu status to the session.
        updateSaveStatusToSession(session,isMenuSaved);
        return ((vecUploadData != null && vecUploadData.size() > 0 )? (Vector)vecUploadData : new Vector());
    }
    
    
    /*This method uploads the selected document */
    private Collection updUploadDocument(UploadDocumentBean uploadDocumentBean,
            String protocolNumber, int seqNum, HttpServletRequest request) throws DBException, CoeusException {
        HttpSession session = request.getSession();
        Vector vcProcedures = new Vector(3,2);
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        UserInfoBean  userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        ProtocolUpdateTxnBean updateBean = new ProtocolUpdateTxnBean(userInfoBean.getUserId());
        updateBean.setDbTimestamp(dbTimestamp);
        vcProcedures.addElement(updateBean.addUpdUploadDocument(uploadDocumentBean));
        DBEngineImpl dbEngine = new DBEngineImpl();
        if(dbEngine!=null) {
            java.sql.Connection conn = null;
            conn = dbEngine.beginTxn();
            if((vcProcedures != null) && (vcProcedures.size() > 0)){
                dbEngine.batchSQLUpdate(vcProcedures,conn);
            }
            dbEngine.commit(conn);
            
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
        
        return protocolDataTxnBean.getUploadDocumentForProtocol(protocolNumber);
    }
    
    public void cleanUp() {
    }
    
    public byte[] getFile(UploadForm uploadForm) throws Exception{
        byte[] fileContents=null;
        File file=new File(uploadForm.getFileName());
        int fileSize = (int)file.length();
        fileContents = new byte[fileSize];
        FileInputStream fileInputStream= new FileInputStream(file);
        fileInputStream.read(fileContents);
        file.delete();
        
        return fileContents;
        
    }
    
    /*This method returns a HashMap which is used to update the Save Status */
    private HashMap updateSaveStatus(UploadForm actionForm,
            String protocolNumber,boolean isAllDeleted, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        Timestamp dbTimestamp = null;
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId();
        HashMap hmpSaveMap=new HashMap();
        hmpSaveMap.put(CoeusLiteConstants.FIELD,UPLOAD_DOCUMENTS_FIELD);
        hmpSaveMap.put(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER,protocolNumber);
        if (isAllDeleted) {
            hmpSaveMap.put(CoeusLiteConstants.AV_SAVED,"Y");
        } else {
            hmpSaveMap.put(CoeusLiteConstants.AV_SAVED,"N");
        }
        hmpSaveMap.put(CoeusLiteConstants.KEY_STUDY_TIMESTAMP,dbTimestamp.toString());
        hmpSaveMap.put(CoeusLiteConstants.AC_TYPE,"U");
        hmpSaveMap.put(CoeusLiteConstants.USER,userId);
        return hmpSaveMap;
    }
    
      /* This method maintains the status of the page
       * whether it is saved or not in a session
       */
    
    private void  updateSaveStatusToSession(HttpSession session, boolean isMenuSaved) {
        Vector vecMenuItems = (Vector)session.getAttribute(MENU_ITEMS);
        Vector modifiedVector = new Vector();
        for (int index=0; index<vecMenuItems.size();index++) {
            MenuBean meanuBean = (MenuBean)vecMenuItems.get(index);
            String menuId = meanuBean.getMenuId();
            if (menuId.equals(UPLOAD_DOCUMENT_SAVE)) {
                if (isMenuSaved) {
                    meanuBean.setDataSaved(true);
                } else {
                    meanuBean.setDataSaved(false);
                }
            }
            modifiedVector.add(meanuBean);
        }
        session.setAttribute(MENU_ITEMS, modifiedVector);
    }
    
    /**this method returns a vector containing a boolean indicating whether the document is in draft status
     * and the version number for the document
     * @param document type code
     * returns Vector
     */
    private List checkDraftDocAvailable(int docTypeCode, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        boolean isDraftDoc = false;
        List vecData = new ArrayList();
        Vector vecHistory = (Vector)session.getAttribute("historyData");
        if(vecHistory != null && vecHistory.size() > 0 ) {
            for(int index = 0 ; index < vecHistory.size() ; index ++ ) {
                UploadDocumentBean uploadDocumentBean  = (UploadDocumentBean)vecHistory.get(index);
                if(uploadDocumentBean.getDocCode() == docTypeCode ) {
                    if(uploadDocumentBean.getStatusCode() == 1){
                        isDraftDoc = true;
                        vecData.add(0,new Boolean(isDraftDoc));
                        vecData.add(1,new Integer(uploadDocumentBean.getVersionNumber()));
                        break;
                    }
                }
            }
        }
        return vecData;
    }
    
    /**
     * To reset the properties of the form.
     * @param uploadForm
     * @throws Exception
     * @return UploadForm
     */
    private UploadForm resetData(UploadForm uploadForm)throws Exception{
        uploadForm.setAcType(EMPTY_STRING);
        uploadForm.setDescription(EMPTY_STRING);
        uploadForm.setDocCode(EMPTY_STRING);
        uploadForm.setDocument(null);
        uploadForm.setFileBytes(null);
        uploadForm.setDocType(EMPTY_STRING);
        uploadForm.setFileName(EMPTY_STRING);
        uploadForm.setParent(EMPTY_STRING);
        //Added for Case#3533 Losing attachments - Start
        uploadForm.setFileNameHidden(EMPTY_STRING);
        //Added for Case#3533 Losing attachments - End
        
        return uploadForm;
    }
    
    //Added for Case#4275 - upload attachments until in agenda - Start
    /**
     * This method locks a particular protcol
     * @throws Exception
     */
//    private void lockProtocol(String protocolNumber, HttpServletRequest request) throws Exception {
//        HttpSession session = request.getSession();
//        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
//        LockBean lockBean = getLockingBean(userInfoBean, protocolNumber,request);
//        lockBean.setMode("M");
//        lockModule(lockBean,request);
//        session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(),new Boolean(true));
//    }
    //Case#4275 - End
}
