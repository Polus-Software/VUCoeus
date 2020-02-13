/*
 * ProtocolValidationCheckAction.java
 *
 * Created on April 19, 2010, 3:17 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeuslite.irb.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.mapsrules.bean.BusinessRuleBean;
import edu.mit.coeus.mapsrules.bean.BusinessRuleConditionsBean;
import edu.mit.coeus.routing.bean.RoutingTxnBean;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeuslite.irb.bean.ProtocolHeaderDetailsBean;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.DynaBean;
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
    
    private static final String VALIDATE_CHECK_FORM = "protocolValidationCheck";
    private static final String USER = "user";
    private static final String GET_PROTOCOL_VALIDATION="/protocolValidationType";
    private static final String PROTOCOL_RULES_MAP  = "PROTOCOL_RULES_MAP";
    private static final String MENU_ITEMS = "menuItems";
    private static final String MENU_CODE = "menuCode";
    
    public ActionForward performExecute(ActionMapping mapping, ActionForm form,
            HttpServletRequest req, HttpServletResponse res) throws Exception {        
        HttpSession session = req.getSession();
        session.removeAttribute(PROTOCOL_RULES_MAP);
        String navigator = EMPTY_STRING;
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
        Map mapMenuList = new HashMap();
        mapMenuList.put(MENU_ITEMS,CoeusliteMenuItems.PROTOCOL_MENU_ITEMS);
        mapMenuList.put(MENU_CODE,CoeusliteMenuItems.PROTOCOL_VALIDATION_CHECK);
        setSelectedMenuList(req, mapMenuList);
        if(mapping.getPath().equals(GET_PROTOCOL_VALIDATION)){
            Vector vecResponseObject = null;
            HashMap  hmData = checkProtocolValidationRules(protocolNumber,sequenceNumber,session,req);
            if(!((Boolean)hmData.get("protocolValidationFlag")).booleanValue()) {
                vecResponseObject = (Vector) hmData.get("vecResponseObject");
            }
            if(vecResponseObject != null && vecResponseObject.size()>0  ){
                session.setAttribute(PROTOCOL_RULES_MAP,vecResponseObject);
            }
            navigator =  "success";
        }
        return mapping.findForward(navigator);
    }
    
    /* Get all the validation rules present
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
        hmMsg.put("protocolValidationFlag", new Boolean(protocolValidationFlag));
        hmMsg.put("vecResponseObject", protocolValidationRules);
        return hmMsg;
    }
    /**
     * To check the Validation for protocol     
     * @throws Exception
     * @return Vector. Conatins the objects from DynaValidatorForm
     */
    public Vector checkValidationProtocol(String protocolNumber, int sequenceNumber,
            DynaActionForm dynaForm, HttpSession session) throws Exception{
        UserInfoBean userInfoBean   = (UserInfoBean)session.getAttribute(USER);
        String userId               = userInfoBean.getUserId();
        ProtocolHeaderDetailsBean headerBean = (ProtocolHeaderDetailsBean)session.getAttribute("protocolHeaderBean");
        RoutingTxnBean routingTxnBean = new RoutingTxnBean();
        return getDynaData(routingTxnBean.validateForRouting(ModuleConstants.PROTOCOL_MODULE_CODE,0, protocolNumber,sequenceNumber, 1, headerBean.getUnitNumber(), userId), dynaForm);
    }
    /**
     * To process the vector obtained from calling the PLSQL function with the DynaValidatorForm
     * @throws Exception
     * @return Vector. Conatins the objects from DynaValidatorForm
     */
    private Vector getDynaData(Vector vecValidationProposal, DynaActionForm  dynaForm) throws Exception{
        Vector dynaVector = new Vector();
        Vector vecErrors = new Vector();
        if(vecValidationProposal!= null && vecValidationProposal.size() > 0){
            for(int index = 0; index < vecValidationProposal.size(); index++){
                BusinessRuleBean businessRulesBean = (BusinessRuleBean)vecValidationProposal.get(index);
                Vector vecRulesConditions = businessRulesBean.getBusinessRuleConditions();
                if(vecRulesConditions != null && vecRulesConditions.size() > 0){
                    for(int count = 0; count < vecRulesConditions.size(); count++){
                        BeanUtilsBean copyBean = new BeanUtilsBean();
                        DynaBean dynaData = (DynaBean)dynaForm.getDynaClass().newInstance();
                        copyBean.copyProperties(dynaData,businessRulesBean);
                        BusinessRuleConditionsBean conditionBean =
                                (BusinessRuleConditionsBean)vecRulesConditions.get(count);
                        dynaData.set("description", conditionBean.getUserMessage());
                        dynaData.set("ruleCategory", businessRulesBean.getRuleCategory());
                        dynaData.set("unitName", businessRulesBean.getUnitName());
                        if(businessRulesBean.getRuleCategory().equals("E")){
                            vecErrors.addElement(dynaData);
                        } else {
                            dynaVector.addElement(dynaData);
                        }
                    }
                } else {
                    BeanUtilsBean copyBean = new BeanUtilsBean();
                    DynaBean dynaData = (DynaBean)dynaForm.getDynaClass().newInstance();
                    copyBean.copyProperties(dynaData,businessRulesBean);
                    if(businessRulesBean.getRuleCategory().equals("E")){
                        vecErrors.addElement(dynaData);
                    } else {
                        dynaVector.addElement(dynaData);
                    }
                }
            }
        }
        vecErrors.addAll(dynaVector);
        return  vecErrors;
    }    

    public void cleanUp() {
    }
    
}
