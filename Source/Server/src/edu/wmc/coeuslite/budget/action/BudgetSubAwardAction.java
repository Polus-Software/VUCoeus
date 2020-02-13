/*
 * BudgetSubAwardAction.java
 *
 * Created on July 6, 2011, 12:30 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.wmc.coeuslite.budget.action;

import edu.mit.coeus.budget.BudgetSubAwardConstants;
import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.bean.BudgetSubAwardBean;
import edu.mit.coeus.budget.bean.BudgetSubAwardDetailBean;
import edu.mit.coeus.budget.bean.BudgetSubAwardTxnBean;
import edu.mit.coeus.budget.bean.BudgetUpdateTxnBean;
import edu.mit.coeus.budget.calculator.BudgetCalculator;
import edu.mit.coeus.exception.CoeusException; // JM
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBException; // JM
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentIdGenerator;
import edu.mit.coeus.utils.query.GreaterThan;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.wmc.coeuslite.budget.bean.ProposalBudgetHeaderBean;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author maharajap
 */
public class BudgetSubAwardAction extends BudgetBaseAction {
    
    private static final String GET_SUB_AWARD_DETAILS = "/getSubAwardBudget";
    private static final String EDIT_SUB_AWARD_DETAILS = "/editSubAwardBudget";
    private static final String SAVE_SUB_AWARD_DETAILS = "/saveSubAwardBudget";
    private static final String DELETE_SUB_AWARD_DETAILS = "/deleteSubAwardBudget";
    private static final String VIEW_SUB_AWARD_ATTACHMENT = "/viewSubAwardBudget";
    private String editRow = EMPTY_STRING;
    private String deleteRow = EMPTY_STRING;
    
    /** Creates a new instance of BudgetSubAwardAction */
    public BudgetSubAwardAction() {
    }

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
        DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)actionForm;
        ActionForward actionForward = performSubAwardBudgetAction(dynaValidatorForm, request, response, actionMapping);
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems",CoeusliteMenuItems.BUDGET_MENU_ITEMS);
        mapMenuList.put("menuCode",CoeusliteMenuItems.SUB_AWARD_BUDGET_CODE);
        setSelectedMenuList(request, mapMenuList);
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
    
    private ActionForward performSubAwardBudgetAction(DynaValidatorForm dynaValidatorForm,
    HttpServletRequest request, HttpServletResponse response, ActionMapping actionMapping) throws Exception{
        HttpSession session = request.getSession();
        String navigator = EMPTY_STRING;
        editRow = request.getParameter("selectedIndex");
        deleteRow = request.getParameter("deletedIndex");
        edu.mit.coeus.bean.UserInfoBean userInfoBean = (edu.mit.coeus.bean.UserInfoBean)session.getAttribute("user"+session.getId());
        if(actionMapping.getPath().equals(GET_SUB_AWARD_DETAILS)){
            navigator = getBudgetSubAwardDetails(dynaValidatorForm, userInfoBean, request);
        } else if(actionMapping.getPath().equals(EDIT_SUB_AWARD_DETAILS)){
            navigator = editBudgetSubAwardDetails(dynaValidatorForm, userInfoBean, request);
        } else if(actionMapping.getPath().equals(VIEW_SUB_AWARD_ATTACHMENT)){
            String documentURL = displayContents(dynaValidatorForm, userInfoBean, request);
            response.sendRedirect(request.getContextPath()+documentURL);
        } else {
            LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId()), request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            LockBean lockData = getLockedData(CoeusLiteConstants.BUDGET_LOCK_STR+lockBean.getModuleNumber(), request);
            if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
                if(actionMapping.getPath().equals(SAVE_SUB_AWARD_DETAILS)){
                    // Added for COEUSQA-3439 : New Subaward Upload tool inserted line items are not parsed into the F&A base properly - Start
                    BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
                    Hashtable htBudgetData = getBudgetData(budgetInfoBean);
                    String queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
                    QueryEngine.getInstance().addDataCollection(queryKey, htBudgetData);
                    // Added for COEUSQA-3439 : New Subaward Upload tool inserted line items are not parsed into the F&A base properly - End
                    navigator = saveBudgetSubAwardDetails(dynaValidatorForm, userInfoBean, request);
                    if("success".equals(navigator)){
                        navigator = getBudgetSubAwardDetails(dynaValidatorForm, userInfoBean, request);
                    }
                }else if(actionMapping.getPath().equals(DELETE_SUB_AWARD_DETAILS)){
                    navigator = saveBudgetSubAwardDetails(dynaValidatorForm, userInfoBean, request);
                    if("success".equals(navigator)){
                        navigator = getBudgetSubAwardDetails(dynaValidatorForm, userInfoBean, request);
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
     * This method gets the sub award data by calling the getBudgetSubAwardData method     
     * @param dynaValidatorForm
     * @param userInfoBean
     * @param request
     * @throws Exception
     * @return String to navigator
     */
    private String getBudgetSubAwardDetails(DynaValidatorForm dynaValidatorForm,
    edu.mit.coeus.bean.UserInfoBean userInfoBean, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        List lstSubAwardData = getBudgetSubAwardData(dynaValidatorForm, request);
        session.setAttribute("lstSubAwardBudget",lstSubAwardData);
        
        // JM 07-22-2013 get organization list to pick from
        String proposalNumber = (String)request.getSession().getAttribute("proposalNumber"+request.getSession().getId());
        Vector organizations = getProposalOrganizations(proposalNumber);
        session.setAttribute("organizations", organizations);
        // JM END
        return "success";
    }
    
    /**
     * This method is used to retrieve the sub award data
     * @param dynaValidatorForm
     * @param request
     * @throws Exception
     * @return String to navigator
     */
    private List getBudgetSubAwardData(DynaValidatorForm dynaValidatorForm, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        Map hmSubAwardData = new HashMap();
        String proposalNumber = "";
        int version = 0;
        List lstBudgetSubAward = new ArrayList();
        
        ProposalBudgetHeaderBean headerBean = (ProposalBudgetHeaderBean)request.getSession().getAttribute("ProposalBudgetHeaderBean");
        proposalNumber = (String)request.getSession().getAttribute("proposalNumber"+request.getSession().getId());
        version = headerBean.getVersionNumber();
        
        BudgetSubAwardTxnBean budgetSubAwardTxnBean = new BudgetSubAwardTxnBean();
        lstBudgetSubAward = budgetSubAwardTxnBean.getBudgetSubAward(proposalNumber,version);
        
        dynaValidatorForm.set("acType","");
        dynaValidatorForm.set("comments","");
        dynaValidatorForm.set("organizationName","");
        dynaValidatorForm.set("fileName","");
        BudgetDataTxnBean budgetTxnBean = new BudgetDataTxnBean();
/* Commented for COEUSQA-4108
        CoeusVector cvBudgetPeriod = budgetTxnBean.getBudgetDetail(proposalNumber,version);
        GreaterThan gt2 = new GreaterThan("budgetPeriod",new Integer(1));
        if(cvBudgetPeriod != null)
            cvBudgetPeriod = cvBudgetPeriod.filter(gt2);
        boolean isPeriodGenerated = false;
        if(cvBudgetPeriod != null && !cvBudgetPeriod.isEmpty()){
            isPeriodGenerated = true;
        }*/
        /*checks whether all periods are generated for budget*/
        boolean isPeriodGenerated = budgetTxnBean.isPeriodsGenerated(proposalNumber, version);
        
        /*COEUSQA-4103-START*/
         BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
         if(budgetInfoBean != null){
             QueryEngine queryEngine = QueryEngine.getInstance();
             String queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
             Hashtable htBudgetData = initializeBudgetCalculator(budgetInfoBean);
             queryEngine.addDataCollection(queryKey, htBudgetData);
             calulateAndSaveBudget(budgetInfoBean,session);
        }
         /*COEUSQA-4103-ENDS*/
        session.setAttribute("isPeriodGenerated",new Boolean(isPeriodGenerated));
        
        return lstBudgetSubAward;
    }
    
    /**
     * This method is used to edit the sub award data
     * @param dynaValidatorForm
     * @param userInfoBean
     * @param request
     * @throws Exception
     * @return String to navigator
     */
    private String editBudgetSubAwardDetails(DynaValidatorForm dynaValidatorForm,
    edu.mit.coeus.bean.UserInfoBean userInfoBean, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        
        List lstSubAwardData = (ArrayList)session.getAttribute("lstSubAwardBudget");
        BudgetSubAwardBean budgetSubAwardBean = (BudgetSubAwardBean)lstSubAwardData.get(Integer.parseInt(editRow));
        dynaValidatorForm.set("organizationName",budgetSubAwardBean.getOrganizationName());
        dynaValidatorForm.set("comments",budgetSubAwardBean.getComments());
        dynaValidatorForm.set("fileName",budgetSubAwardBean.getPdfFileName());
        dynaValidatorForm.set("selectedRow",editRow);
        
        return "success";
    }
    
    // JM 7-11-2013 method to get proposal organization list
    private Vector getProposalOrganizations(String proposalNumber) {
	    Vector vecOrganizations = new Vector();
	    Vector result = new Vector();
	    HashMap row = new HashMap();
    	edu.mit.coeus.propdev.bean.ProposalSiteBean bean;
    	BudgetSubAwardTxnBean txnBean = new BudgetSubAwardTxnBean();
    	try {
    		result = (Vector) txnBean.getPropOrganizationsForSubaward(proposalNumber);
		} catch (CoeusException e) {
			System.out.println("Exception trying to retrieve proposal organizations");
			e.printStackTrace();
		} catch (DBException e) {
			System.out.println("DB exception trying to retrieve proposal organizations");
			e.printStackTrace();
		}
		
        int listSize = result.size();
        if (listSize > 0) {
        	bean = null;
        	vecOrganizations = new Vector();
	        for(int i = 0; i < result.size(); i++){
	        	bean = new edu.mit.coeus.propdev.bean.ProposalSiteBean();
	            row = (HashMap) result.elementAt(i);
	            bean.setProposalNumber((String)row.get("PROPOSAL_NUMBER"));
	            bean.setLocationName((String)row.get("LOCATION_NAME"));
	            bean.setOrganizationId((String)row.get("ORGANIZATION_ID"));
	            vecOrganizations.add(bean);
	         }
        }
		
	    return vecOrganizations;
    }
    // JM END
    
    /**
     * This method saves the sub award data by calling the saveBudgetSubAward method
     * @param dynaValidatorForm
     * @param userInfoBean
     * @param request
     * @throws Exception
     * @return String to navigator
     */
    private String saveBudgetSubAwardDetails(DynaValidatorForm dynaValidatorForm,
    edu.mit.coeus.bean.UserInfoBean userInfoBean, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        
        Vector organizations = (Vector)session.getAttribute("organizations"); // JM 7-23-2013 need org list
        List lstSubAwardData = (ArrayList)session.getAttribute("lstSubAwardBudget");
        String acType = (String)dynaValidatorForm.get("acType");
        String comments = (String)dynaValidatorForm.get("comments");
        String organizationName = (String)dynaValidatorForm.get("organizationName");
        // JM 7-23-2013 adding organization ID to save
        String organizationId = "";
        edu.mit.coeus.propdev.bean.ProposalSiteBean bean = new edu.mit.coeus.propdev.bean.ProposalSiteBean();
        for (int v=0; v < organizations.size(); v++) {
        	bean = (edu.mit.coeus.propdev.bean.ProposalSiteBean) organizations.get(v);
        	if (bean.getLocationName().equals(organizationName)) {
        		organizationId = bean.getOrganizationId();
        	}
        }
        // JM END
        
        ProposalBudgetHeaderBean headerBean = (ProposalBudgetHeaderBean)request.getSession().getAttribute("ProposalBudgetHeaderBean");
        String proposalNumber = (String)request.getSession().getAttribute("proposalNumber"+request.getSession().getId());
        int version = headerBean.getVersionNumber();
        
        if(acType==null || acType.length()==0){
            acType = TypeConstants.INSERT_RECORD;
        }
        
        BudgetSubAwardTxnBean budgetSubAwardTxnBean = new BudgetSubAwardTxnBean(userInfoBean.getUserId());
        if(acType!=null && "I".equals(acType)){
            BudgetSubAwardBean budgetSubAwardBean = new BudgetSubAwardBean();
            
            budgetSubAwardBean.setAcType(TypeConstants.INSERT_RECORD);
            budgetSubAwardBean.setComments(comments);
            // JM 7-23-2013 adding organization ID to save
            budgetSubAwardBean.setOrganizationId(organizationId);
            // JM END
            budgetSubAwardBean.setOrganizationName(organizationName);
            budgetSubAwardBean.setProposalNumber(proposalNumber);
            budgetSubAwardBean.setVersionNumber(version);
            FormFile myFile = (FormFile)dynaValidatorForm.get("document");
            if(myFile != null){
                try {
                    byte[] fileData = myFile.getFileData();
                    if(fileData.length >0){
                        budgetSubAwardBean.setSubAwardPDF(fileData);// BLOB data
                        budgetSubAwardBean.setPdfFileName(myFile.getFileName());
                        budgetSubAwardBean.setPdfAcType(TypeConstants.INSERT_RECORD);
                    }else{
                        budgetSubAwardBean.setSubAwardPDF(null);
                    }
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if(organizationName!=null && myFile != null){
                StringBuffer strBuff = request.getRequestURL();
                String servletPath = new String(strBuff);
                servletPath = servletPath.substring(0,servletPath.lastIndexOf('/'));
                budgetSubAwardBean = budgetSubAwardTxnBean.checkAndUpdate(budgetSubAwardBean, servletPath+"/S2SServlet");
                Vector vecBudgetSubAward = new Vector();
                vecBudgetSubAward.add(budgetSubAwardBean);
                budgetSubAwardTxnBean.saveBudgetSubAward(vecBudgetSubAward);
            }
        }else if(acType!=null && "U".equals(acType)){
           
            lstSubAwardData = (ArrayList)session.getAttribute("lstSubAwardBudget");
            BudgetSubAwardBean budgetSubAwardBean = (BudgetSubAwardBean)lstSubAwardData.get(Integer.parseInt(editRow));
            
            budgetSubAwardBean.setAcType(TypeConstants.UPDATE_RECORD);
            budgetSubAwardBean.setComments(comments);
            // JM 7-23-2013 adding organization ID to save
            budgetSubAwardBean.setOrganizationId(organizationId);
            // JM END
            budgetSubAwardBean.setOrganizationName(organizationName);            
            budgetSubAwardBean.setUpdateUser(userInfoBean.getUserId());
            FormFile myFile = (FormFile)dynaValidatorForm.get("document");
            if(myFile != null){
                try {
                    byte[] fileData = myFile.getFileData();
                    if(fileData.length >0){
                        budgetSubAwardBean.setSubAwardPDF(fileData);// BLOB data
                        budgetSubAwardBean.setPdfFileName(myFile.getFileName());
                        budgetSubAwardBean.setPdfAcType(TypeConstants.UPDATE_RECORD);
                    }else{
                        budgetSubAwardBean.setSubAwardPDF(null);
                    }
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if(organizationName!=null && myFile != null){
                StringBuffer strBuff = request.getRequestURL();
                String servletPath = new String(strBuff);
                servletPath = servletPath.substring(0,servletPath.lastIndexOf('/'));
                budgetSubAwardBean = budgetSubAwardTxnBean.checkAndUpdate(budgetSubAwardBean, servletPath+"/S2SServlet");
                Vector vecBudgetSubAward = new Vector();
                vecBudgetSubAward.add(budgetSubAwardBean);
                budgetSubAwardTxnBean.saveBudgetSubAward(vecBudgetSubAward);
            }
            // Update the subaward description when the organization name is changed
            
            BudgetUpdateTxnBean budgetUpdateTxnBean = new BudgetUpdateTxnBean(userInfoBean.getUserId());
            budgetUpdateTxnBean.updateSubAwdLineItemDescription(budgetSubAwardBean);
            editRow = EMPTY_STRING;
            dynaValidatorForm.set("selectedRow","");
        }else if(acType!=null && "D".equals(acType)){
            lstSubAwardData = (ArrayList)session.getAttribute("lstSubAwardBudget");
            BudgetSubAwardBean budgetSubAwardBean = (BudgetSubAwardBean)lstSubAwardData.get(Integer.parseInt(deleteRow));
            budgetSubAwardBean.setAcType(TypeConstants.DELETE_RECORD);
            budgetSubAwardBean.setUpdateUser(userInfoBean.getUserId());
            
            List deleteData = new ArrayList();
            deleteData.add(budgetSubAwardBean);
            // Deletes the sub award line items created in the OSP$BUDGET_DETAILS
            BudgetUpdateTxnBean budgetUpdateTxnBean = new BudgetUpdateTxnBean(userInfoBean.getUserId());
            budgetUpdateTxnBean.deleteSubAwardCostLineItem(budgetSubAwardBean.getProposalNumber(),
                    budgetSubAwardBean.getVersionNumber(),budgetSubAwardBean.getSubAwardNumber());
            // Deletes the sub award details
            Vector vecSubAwardDetails = budgetSubAwardBean.getSubAwardPeriodDetails();
            if(vecSubAwardDetails != null && !vecSubAwardDetails.isEmpty()){
                for(Object subAwardDetails : vecSubAwardDetails){
                    BudgetSubAwardDetailBean subAwardDetailBean = (BudgetSubAwardDetailBean)subAwardDetails;
                    subAwardDetailBean.setAcType(TypeConstants.DELETE_RECORD);
                    budgetSubAwardTxnBean.updateSubAwardDetails(subAwardDetailBean);
                }
            }

            lstSubAwardData = budgetSubAwardTxnBean.saveBudgetSubAward(deleteData);
        }
        dynaValidatorForm.set("acType","");
        dynaValidatorForm.set("comments","");
        dynaValidatorForm.set("organizationName","");
        // JM 7-23-2013 adding organization ID to save
        dynaValidatorForm.set("organizationId","");
        // JM END
        return "success";
    }
    
    /**
     * displayes the contents of the PDF or XMl file
     * @param content specified either XML or PDF
     * @param budgetSubAwardBean budget sub award bean
     */
    private String displayContents(DynaValidatorForm dynaValidatorForm, edu.mit.coeus.bean.UserInfoBean userInfoBean,
            HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        String selectedRow = request.getParameter("selectedRow");
        String file = request.getParameter("fileType");
        
        List lstSubAwardData = (ArrayList)session.getAttribute("lstSubAwardBudget");
        BudgetSubAwardBean budgetSubAwardBean = (BudgetSubAwardBean)lstSubAwardData.get(Integer.parseInt(selectedRow));
        
        Object data = null;
        DocumentBean documentBean = new DocumentBean();
        Map map = new HashMap();
        map.put(BudgetSubAwardConstants.PROPOSAL_NUMBER, budgetSubAwardBean.getProposalNumber());
        map.put(BudgetSubAwardConstants.VERSION_NUMBER, ""+budgetSubAwardBean.getVersionNumber());
        map.put(BudgetSubAwardConstants.SUB_AWARD_NUM, ""+budgetSubAwardBean.getSubAwardNumber());
        map.put(BudgetSubAwardConstants.FILE, file);
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.budget.report.SubAwardDocumentReader");
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
