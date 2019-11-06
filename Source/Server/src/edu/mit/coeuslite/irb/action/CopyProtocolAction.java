/*
 * CopyProtocolAction.java
 *
 * Created on April 18, 2012, 4:53 PM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeuslite.irb.action;

import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.irb.bean.CommitteeMaintenanceFormBean;
import edu.mit.coeus.irb.bean.CommitteeTxnBean;
import edu.mit.coeus.irb.bean.ProtocolAuthorizationBean;
import edu.mit.coeus.irb.bean.ProtocolDataTxnBean;
import edu.mit.coeus.irb.bean.ProtocolSubmissionInfoBean;
import edu.mit.coeus.irb.bean.ProtocolSubmissionTxnBean;
import edu.mit.coeus.irb.bean.UploadDocumentBean;
import edu.mit.coeus.questionnaire.bean.ModuleDataBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean;
import edu.mit.coeus.questionnaire.bean.SubModuleDataBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeuslite.irb.bean.ProtocolHeaderDetailsBean;
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
import edu.mit.coeus.utils.dbengine.DBException;
/**
 *
 * @author manjunathabn
 */
public class CopyProtocolAction extends ProtocolBaseAction{
       
    private static final String HAS_ATTACHMENTS  = "strHasAttachments";
    private static final String HAS_OTHER_ATTACHMENTS  = "strHasOtherAttach";
    private static final String HAS_QNR  = "strHasQnr";
    private static final String SUCCESS = "success";
    private static final String COPY_PROTOCOL = "copyProtocol";    
    private static final String ENABLE_COPY_PROTO_QNR = "ENABLE_COPY_PROTO_QNR";
    private static final String ENABLE_COPY_PROTO_ATTACHMENT = "ENABLE_COPY_PROTO_ATTACHMENT";
    private static final String ENABLE_COPY_PROTO_OTHER_ATTACHMENT = "ENABLE_COPY_PROTO_OTHER_ATTACHMENT";

    
    private static final String GET_PARAMETER_VALUE = "GET_PARAMETER_VALUE";
    /** Creates a new instance of CopyProtocolAction */
    public CopyProtocolAction() {
    }

    
    public ActionForward performExecute(ActionMapping mapping , ActionForm actionForm ,
    HttpServletRequest request , HttpServletResponse response) throws Exception {
        
        String navigator = SUCCESS;
        HttpSession session= request.getSession();        
        String protocolNumber = EMPTY_STRING;
        String seq = EMPTY_STRING;
        int sequenceNumber = -1;
        String  page  = request.getParameter(CoeusLiteConstants.PAGE);
        ActionForward actionForward = null;
        
        String copyQnr = getParameterValue(ENABLE_COPY_PROTO_QNR);
        String copyAttachments = getParameterValue(ENABLE_COPY_PROTO_ATTACHMENT);
        String copyOtherAttach = getParameterValue(ENABLE_COPY_PROTO_OTHER_ATTACHMENT);
        
        if("1".equals(copyQnr) || "1".equals(copyAttachments) || "1".equals(copyOtherAttach)) {
            navigator = SUCCESS;
            String strHasAttachments = "NO";
            String strHasOtherAttach = "NO";
            String strHasQnr = "NO";
            
            protocolNumber = (String) session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
            seq = (String) session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
            
            Map mapMenuList = null;
            mapMenuList = new HashMap();
            mapMenuList.put("menuItems",CoeusliteMenuItems.PROTOCOL_MENU_ITEMS);
            mapMenuList.put("menuCode",CoeusliteMenuItems.COPY_PROTOCOL_MNEU);
            
            setSelectedMenuList(request, mapMenuList);
            
            
            readSavedStatus(request);
            
            ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
            
            Vector vecUpldData = protocolDataTxnBean.getUploadDocumentForProtocol(protocolNumber);
            if("1".equals(copyAttachments)) {
                if(vecUpldData != null && !vecUpldData.isEmpty()){
                    strHasAttachments = "YES";
                }
            } 
            
            Vector vecOtherData = protocolDataTxnBean.getProtoOtherAttachments(protocolNumber);
            if("1".equals(copyOtherAttach)) {
                if(vecOtherData != null && !vecOtherData.isEmpty()){
                    strHasOtherAttach = "YES";
                }
            }
            
            QuestionnaireTxnBean questionnaireTxnBean = new QuestionnaireTxnBean();
            
            boolean isQnrPresent = questionnaireTxnBean.checkAnyQuestionIsAnsweredInModule(ModuleConstants.PROTOCOL_MODULE_CODE, protocolNumber, 0, seq);
            if("1".equals(copyQnr)) {
                if(isQnrPresent == true){
                    strHasQnr = "YES";
                }
            }
            
            boolean canCopyQnr = questionnaireTxnBean.checkCanCopyQuestionnaireFromModule(ModuleConstants.PROTOCOL_MODULE_CODE, 0, protocolNumber, seq);
            request.setAttribute("canCopyQnr", Boolean.valueOf(canCopyQnr));
            
            request.setAttribute(HAS_ATTACHMENTS, strHasAttachments);
            request.setAttribute(HAS_OTHER_ATTACHMENTS, strHasOtherAttach);
            request.setAttribute(HAS_QNR, strHasQnr);
            
            actionForward = mapping.findForward(navigator);                        
           
        } else{
             navigator = COPY_PROTOCOL;
             actionForward = mapping.findForward(navigator);
        }     
        return actionForward;
    }

    public void cleanUp() {
    }
    
    public String getParameterValue(String parameter) throws CoeusException, DBException{
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        String value = null;
        value = coeusFunctions.getParameterValue(parameter);              
        return value;
    }

    
}
