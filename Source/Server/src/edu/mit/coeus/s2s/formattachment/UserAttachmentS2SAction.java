/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package edu.mit.coeus.s2s.formattachment;

import edu.mit.coeus.bean.UserInfoBean;
import edu.utk.coeuslite.propdev.action.ProposalBaseAction;
import edu.mit.coeus.s2s.bean.UserAttachedS2SFormBean;
import edu.mit.coeus.s2s.bean.UserAttachedS2STxnBean;
import edu.mit.coeus.s2s.validator.S2SValidationException;
import edu.mit.coeus.utils.S2SConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentIdGenerator;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.LockBean;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author  polusdev
 */
public class UserAttachmentS2SAction extends ProposalBaseAction {
    
    private static final String GET_USER_ATT_S2S_DETAILS = "/getUserAttS2SDetails";
    private static final String EDIT_USER_ATT_S2S_DETAILS = "/editUserAttS2SDetails";
    private static final String SAVE_USER_ATT_S2S_DETAILS = "/saveUserAttS2SDetails";
    private static final String DELETE_USER_ATT_S2S_DETAILS = "/deleteUserAttS2SDetails";
    private static final String VIEW_USER_ATT_S2S_ATTACHMENT = "/viewUserAttS2SDetails";
    private String editRow = EMPTY_STRING;
    private String deleteRow = EMPTY_STRING;
    private static final String PROPOSAL_NUMBER = "proposalNumber";

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
        DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)actionForm;
        dynaValidatorForm.set(PROPOSAL_NUMBER,session.getAttribute(PROPOSAL_NUMBER+session.getId()));
        ActionForward actionForward = performUserAttachmentS2SAction(dynaValidatorForm, request, response, actionMapping);
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems",CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
        mapMenuList.put("menuCode",CoeusliteMenuItems.USER_ATT_S2S_FORM);
        setSelectedMenuList(request, mapMenuList);
        readSavedStatus(request);
        return actionForward;
    }
    
    /**
     * This method will identify which request is comes from which path and
     * navigates to the respective ActionForward
     * @returns ActionForward object
     * @param actionMapping
     * @param dynaForm
     * @throws Exception
     * @return
     */
    
    private ActionForward performUserAttachmentS2SAction(DynaValidatorForm dynaValidatorForm,
    HttpServletRequest request, HttpServletResponse response, ActionMapping actionMapping) throws Exception{
        HttpSession session = request.getSession();     
        String proposalNumber = (String) session.getAttribute(PROPOSAL_NUMBER+session.getId());
        String navigator = EMPTY_STRING;
        editRow = request.getParameter("selectedIndex");
        deleteRow = request.getParameter("deletedIndex");
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        if(actionMapping.getPath().equals(GET_USER_ATT_S2S_DETAILS)){
            navigator = getUserAttachmentS2SDetails(dynaValidatorForm, userInfoBean, request);
        } else if(actionMapping.getPath().equals(EDIT_USER_ATT_S2S_DETAILS)){
            navigator = editUserAttachmentS2SDetails(dynaValidatorForm, request);
        } else if(actionMapping.getPath().equals(VIEW_USER_ATT_S2S_ATTACHMENT)){
            String documentURL = displayContents(request);
            response.sendRedirect(request.getContextPath()+documentURL);
        } else {
            LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId()), request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            LockBean lockData = getLockedData(CoeusLiteConstants.PROP_DEV_LOCK_STR+lockBean.getModuleNumber(), request);
            if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
                if(actionMapping.getPath().equals(SAVE_USER_ATT_S2S_DETAILS)){                   
                    navigator = saveUserAttachmentS2SDetails(dynaValidatorForm, userInfoBean, request);
                    if("success".equals(navigator)){
                        navigator = getUserAttachmentS2SDetails(dynaValidatorForm, userInfoBean, request);
                    }
                }else if(actionMapping.getPath().equals(DELETE_USER_ATT_S2S_DETAILS)){
                    navigator = saveUserAttachmentS2SDetails(dynaValidatorForm, userInfoBean, request);
                    if("success".equals(navigator)){
                        navigator = getUserAttachmentS2SDetails(dynaValidatorForm, userInfoBean, request);
                    }
                }
            } else {
                String errMsg = "release_lock_for";
                ActionMessages messages = new ActionMessages();
                messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                saveMessages(request, messages);
                navigator = "success";
            }
        }
        ActionForward actionForward = actionMapping.findForward(navigator);
        return actionForward;
        
    }
    
    /**
     * This method gets the user attachment s2s data by calling the getUserAttachmentS2SData method     
     * @param dynaValidatorForm
     * @param userInfoBean
     * @param request
     * @throws Exception
     * @return String to navigator
     */
    private String getUserAttachmentS2SDetails(DynaValidatorForm dynaValidatorForm,
    edu.mit.coeus.bean.UserInfoBean userInfoBean, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        List lstUserAttachmentS2SData = getUserAttachmentS2SData(dynaValidatorForm, request);
        session.setAttribute("lstUserAttachmentS2S",lstUserAttachmentS2SData);
        
        return "success";
    }
    
    /**
     * This method is used to retrieve the user attachment s2s data
     * @param dynaValidatorForm
     * @param request
     * @throws Exception
     * @return String to navigator
     */
    private List getUserAttachmentS2SData(DynaValidatorForm dynaValidatorForm, HttpServletRequest request) throws Exception{      
        String proposalNumber = "";      
        List lstUserAttS2S = new ArrayList();
        proposalNumber = (String)request.getSession().getAttribute("proposalNumber"+request.getSession().getId());              
        UserAttachedS2STxnBean userAttachedS2STxnBean = new UserAttachedS2STxnBean();
        lstUserAttS2S = userAttachedS2STxnBean.getUserAttachedS2SForm(proposalNumber);        
        dynaValidatorForm.set("acType","");      
        dynaValidatorForm.set("description","");
        dynaValidatorForm.set("fileName","");
        
        return lstUserAttS2S;
    }
    
    /**
     * This method is used to edit the user attachment s2s data
     * @param dynaValidatorForm
     * @param userInfoBean
     * @param request
     * @throws Exception
     * @return String to navigator
     */
    private String editUserAttachmentS2SDetails(DynaValidatorForm dynaValidatorForm,HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        
        List lstUserAttachmentS2SData = (ArrayList)session.getAttribute("lstUserAttachmentS2S");
        UserAttachedS2SFormBean userAttachedS2SFormBean = (UserAttachedS2SFormBean)lstUserAttachmentS2SData.get(Integer.parseInt(editRow));
        dynaValidatorForm.set("description",userAttachedS2SFormBean.getDescription());     
        dynaValidatorForm.set("fileName",userAttachedS2SFormBean.getPdfFileName());
        dynaValidatorForm.set("selectedRow",editRow);
        
        return "success";
    }
    
    /**
     * This method saves the user attachment s2s data by calling the saveUserAttachmentS2SDetails method
     * @param dynaValidatorForm
     * @param userInfoBean
     * @param request
     * @throws Exception
     * @return String to navigator
     */
    private String  saveUserAttachmentS2SDetails(DynaValidatorForm dynaValidatorForm,
    edu.mit.coeus.bean.UserInfoBean userInfoBean, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        FormAttachmentExtractService extractService = new FormAttachmentExtractService();
        List lstUserAttachmentS2SData = (ArrayList)session.getAttribute("lstUserAttachmentS2S");
        String acType = (String)dynaValidatorForm.get("acType");        
        String description = (String)dynaValidatorForm.get("description");
        
        String proposalNumber = (String)request.getSession().getAttribute("proposalNumber"+request.getSession().getId());
        
        if(acType==null || acType.length()==0){
            acType = TypeConstants.INSERT_RECORD;
        }
        
        UserAttachedS2STxnBean userAttachedS2STxnBean = new UserAttachedS2STxnBean(userInfoBean.getUserId());
        if(acType!=null && "I".equals(acType)){
            UserAttachedS2SFormBean userAttachedS2SFormBean = new UserAttachedS2SFormBean();
            userAttachedS2SFormBean.setAcType(TypeConstants.INSERT_RECORD);            
            userAttachedS2SFormBean.setDescription(description);
            userAttachedS2SFormBean.setProposalNumber(proposalNumber);          
            FormFile myFile = (FormFile)dynaValidatorForm.get("document");
            if(myFile != null){
                try {
                    byte[] fileData = myFile.getFileData();
                    if(fileData.length >0){
                        userAttachedS2SFormBean.setUserAttachedS2SPDF(fileData);// BLOB data
                        userAttachedS2SFormBean.setPdfFileName(myFile.getFileName());
                        userAttachedS2SFormBean.setPdfAcType(TypeConstants.INSERT_RECORD);
                    }else{
                        userAttachedS2SFormBean.setUserAttachedS2SPDF(null);
                    }
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if(description!=null && myFile != null){
                StringBuffer strBuff = request.getRequestURL();
                String servletPath = new String(strBuff);
                servletPath = servletPath.substring(0,servletPath.lastIndexOf('/')); 
                try{
                  
                    List formBeans = extractService.processPdfForm(userAttachedS2SFormBean);
                   if (formBeans!= null && formBeans.size()==0){
                       request.setAttribute("attachedS2SError", true);
                        return "success";
                   }
                    Vector vecUserAttachmentS2S = new Vector();
                    vecUserAttachmentS2S.addAll(formBeans);
                    userAttachedS2STxnBean.saveUserS2SForm(vecUserAttachmentS2S);
                }catch(Exception ex){
                	request.setAttribute("attachedS2SError", true);
                    return "success";
                }
//                vecUserAttachmentS2S.add(userAttachedS2SFormBean);
            }
        }else if(acType!=null && "U".equals(acType)){
           
            lstUserAttachmentS2SData = (ArrayList)session.getAttribute("lstUserAttachmentS2S");
            UserAttachedS2SFormBean userAttachedS2SFormBean = (UserAttachedS2SFormBean)lstUserAttachmentS2SData.get(Integer.parseInt(editRow));
            
            userAttachedS2SFormBean.setAcType(TypeConstants.UPDATE_RECORD);           
            userAttachedS2SFormBean.setDescription(description);            
            userAttachedS2SFormBean.setUpdateUser(userInfoBean.getUserId());
            FormFile myFile = (FormFile)dynaValidatorForm.get("document");
            if(myFile != null){
                try {
                    byte[] fileData = myFile.getFileData();
                    if(fileData.length >0){
                        userAttachedS2SFormBean.setUserAttachedS2SPDF(fileData);// BLOB data
                        userAttachedS2SFormBean.setPdfFileName(myFile.getFileName());
                        userAttachedS2SFormBean.setPdfAcType(TypeConstants.UPDATE_RECORD);
                    }else{
                        userAttachedS2SFormBean.setUserAttachedS2SPDF(null);
                    }
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if(description!=null && myFile != null){   
                List formBeans = extractService.processPdfForm(userAttachedS2SFormBean);
                Vector vecUserAttachmentS2S = new Vector();
                vecUserAttachmentS2S.addAll(formBeans);
                userAttachedS2STxnBean.saveUserS2SForm(vecUserAttachmentS2S);
            }                     
        
            editRow = EMPTY_STRING;
            dynaValidatorForm.set("selectedRow","");
        }else if(acType!=null && "D".equals(acType)){
            lstUserAttachmentS2SData = (ArrayList)session.getAttribute("lstUserAttachmentS2S");
            UserAttachedS2SFormBean userAttachedS2SFormBean = (UserAttachedS2SFormBean)lstUserAttachmentS2SData.get(Integer.parseInt(deleteRow));
            userAttachedS2SFormBean.setAcType(TypeConstants.DELETE_RECORD);
            userAttachedS2SFormBean.setUpdateUser(userInfoBean.getUserId());
            
            List deleteData = new ArrayList();
            deleteData.add(userAttachedS2SFormBean);
            lstUserAttachmentS2SData = userAttachedS2STxnBean.saveUserS2SForm(deleteData);
        }
        dynaValidatorForm.set("acType","");   
        dynaValidatorForm.set("description","");
        return "success";
    }
    
    /**
     * displays the contents of the PDF or XMl file
     * @param content specified either XML or PDF
     * @param userAttachedS2SFormBean user attachment s2s data
     */
    private String displayContents(HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        String selectedRow = request.getParameter("selectedRow");
        String file = request.getParameter("fileType");
        
        List lstUserAttachmentS2SData = (ArrayList)session.getAttribute("lstUserAttachmentS2S");
        UserAttachedS2SFormBean userAttachedS2SFormBean = (UserAttachedS2SFormBean)lstUserAttachmentS2SData.get(Integer.parseInt(selectedRow));
        
        Object data = null;
        DocumentBean documentBean = new DocumentBean();
        Map map = new HashMap();
        map.put(S2SConstants.PROPOSAL_NUMBER, userAttachedS2SFormBean.getProposalNumber());        
        map.put(S2SConstants.USER_ATTACHED_FORM_NUMBER, ""+userAttachedS2SFormBean.getUserAttachedFormNumber());
        map.put(S2SConstants.FILE, file);
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.s2s.formattachment.UserAttachmentS2SDocReader");
        map.put(S2SConstants.NAMESPACE, userAttachedS2SFormBean.getNamespace());
        
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
}
