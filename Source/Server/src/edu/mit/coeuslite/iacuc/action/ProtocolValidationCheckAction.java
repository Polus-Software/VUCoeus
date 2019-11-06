/*
 * ProtocolValidationCheckAction.java
 *
 * Created on July 8, 2010, 9:53 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeuslite.iacuc.action;

import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.DynaActionFormClass;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.RequestUtils;

/**
 *
 * @author divyasusendran
 */
public class ProtocolValidationCheckAction extends ProtocolBaseAction {
    
    /** Creates a new instance of ProtocolValidationCheckAction */
    public ProtocolValidationCheckAction() {
    }
    
    private static final String VALIDATE_CHECK_FORM = "iacucProtocolValidationCheck";
    private static final String GET_PROTOCOL_VALIDATION="/iacucProtocolValidationType";
    private static final String PROTOCOL_RULES_MAP  = "IACUC_PROTOCOL_RULES_MAP";
    private static final String MENU_ITEMS = "menuItems";
    private static final String MENU_CODE = "menuCode";
    private static final String PROTOCOL_VALIDATION_FLAG = "protocolValidationFlag";
    private static final String RESPONSE_OBJECT = "responseObject";
    
    public ActionForward performExecute(ActionMapping mapping, ActionForm form, 
            HttpServletRequest req, HttpServletResponse res) throws Exception {
        
        HttpSession session = req.getSession();
        session.removeAttribute(PROTOCOL_RULES_MAP);
        String navigator = EMPTY_STRING;
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
        Map mapMenuList = new HashMap();
        mapMenuList.put(MENU_ITEMS,CoeusliteMenuItems.IACUC_MENU_ITEMS);
        mapMenuList.put(MENU_CODE,CoeusliteMenuItems.IACUC_PROTOCOL_VALIDATION_CHECK);
        setSelectedMenuList(req, mapMenuList);
        if(GET_PROTOCOL_VALIDATION.equals(mapping.getPath())){
            Vector vecResponseObject = null;
            HashMap  hmData = checkProtocolValidationRules(protocolNumber,sequenceNumber,session,req);
            if(!((Boolean)hmData.get(PROTOCOL_VALIDATION_FLAG)).booleanValue()) {
                vecResponseObject = (Vector) hmData.get(RESPONSE_OBJECT);
            }
            if(vecResponseObject != null && vecResponseObject.size()>0  ){
                session.setAttribute(PROTOCOL_RULES_MAP,vecResponseObject);
            }
            navigator =  "success";
        }
        return mapping.findForward(navigator);
        
    }
    
     /* Get all the validation rules present
      * @param protocolNumber
      * @param sequenceNumber
      * @param HttpSession
      * @param HttpServletRequest
      * @return hmMsg map containing validation rules
      * @throws Exception 
      */
    private HashMap checkProtocolValidationRules(String protocolNumber,String sequenceNumber,
            HttpSession session,HttpServletRequest request) throws Exception{
        boolean protocolValidationFlag = true;
        ServletContext servletContext = request.getSession().getServletContext();
        ModuleConfig moduleConfig = RequestUtils.getModuleConfig(request, servletContext);
        FormBeanConfig formConfig = moduleConfig.findFormBeanConfig(VALIDATE_CHECK_FORM);
        DynaActionFormClass dynaClass = DynaActionFormClass.createDynaActionFormClass(formConfig);
        DynaActionForm dynaForm = (DynaActionForm)dynaClass.newInstance();
        Vector protocolValidationRules = checkValidationProtocol(protocolNumber,Integer.parseInt(sequenceNumber), dynaForm, session);
        if(protocolValidationRules.size() > 0){
            protocolValidationFlag = false;
        }
        HashMap hmMsg = new HashMap();
        hmMsg.put(PROTOCOL_VALIDATION_FLAG, new Boolean(protocolValidationFlag));
        hmMsg.put(RESPONSE_OBJECT, protocolValidationRules);
        return hmMsg;
    }   

    public void cleanUp() {
    }
    
}
