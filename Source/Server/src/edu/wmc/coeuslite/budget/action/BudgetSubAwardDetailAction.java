/*
 * BudgetSubAwardDetailAction.java
 *
 * Created on July 18, 2011, 12:32 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved
 *
 */

package edu.wmc.coeuslite.budget.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.bean.BudgetSubAwardBean;
import edu.mit.coeus.budget.bean.BudgetSubAwardDetailBean;
import edu.mit.coeus.budget.bean.BudgetSubAwardTxnBean;
import edu.mit.coeus.budget.bean.BudgetUpdateTxnBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeuslite.utils.CoeusDynaBeansList;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author satheeshkumarkn
 * @version
 */

public class BudgetSubAwardDetailAction extends BudgetBaseAction {
    
    /* forward name="success" path="" */
    private final static String SUCCESS = "success";
    private final static String GET_SUB_AWARD_DETAILS_MAPPING = "/getSubAwardDetails";
    private final static String SAVE_SUB_AWARD_DETAILS_MAPPING ="/saveSubAwardDetails";
    private final static String SYNC_XML ="/syncXMLSubAwardDetails";
    /**
     * This is the action called from the Struts framework.
     * 
     * @return 
     * @param actionForm 
     * @param mapping The ActionMapping used to select this instance.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception 
     */
    public ActionForward performExecute(ActionMapping mapping, ActionForm  actionForm,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        HttpSession session = request.getSession();
        CoeusDynaBeansList coeusDynaBeanList = (CoeusDynaBeansList)actionForm;
        ActionForward actionForward = performSubAwardDetailAction(coeusDynaBeanList,request,response,mapping);
        return actionForward;
    }
    
    /**
     * Method to perform sub award detail
     * @param coeusDynaBeanList 
     * @param request 
     * @param response 
     * @param actionMapping 
     * @throws java.lang.Exception 
     * @return actionForward
     */
    private ActionForward performSubAwardDetailAction(CoeusDynaBeansList coeusDynaBeanList,
            HttpServletRequest request, HttpServletResponse response, ActionMapping actionMapping) throws Exception{
        String navigator = SUCCESS;
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        //COEUSQA-3883  Start
        BudgetInfoBean budInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
        String queryKey1 = budInfoBean.getProposalNumber()+budInfoBean.getVersionNumber();
        if ( QueryEngine.getInstance().getDataCollection(queryKey1) == null){
        Hashtable htBudData = getBudgetData(budInfoBean);
        QueryEngine.getInstance().addDataCollection(queryKey1, htBudData);
        }
        //COEUSQA-3883  End

        if(GET_SUB_AWARD_DETAILS_MAPPING.equals(actionMapping.getPath())){
            HashMap hmSubAwardDetailParam = new HashMap();
            String readOnlyMode = (String)request.getParameter("readOnly");
            String enableSync = (String)request.getParameter("enableSync");
            String organizationName = (String)request.getParameter("organizationName");
            String proposalNumber = (String)request.getParameter("proposalNumber");
            String versionNumber = (String)request.getParameter("versionNumber");
            String subAwardNumber = (String)request.getParameter("subAwardNumber");
            
            if(organizationName == null){
                proposalNumber =(String)session.getAttribute("subAwardProposalNumber");
                versionNumber =(String)session.getAttribute("subAwardVersionNumber");
                subAwardNumber =(String)session.getAttribute("subAwardNumber");
            }else{
                session.setAttribute("subAwardOrganizationName",organizationName);
                session.setAttribute("subAwardProposalNumber",proposalNumber);
                session.setAttribute("subAwardVersionNumber",versionNumber);
                session.setAttribute("subAwardNumber",subAwardNumber);
                session.setAttribute("subAwardDetailsMode",readOnlyMode);
                session.setAttribute("enableSync",enableSync);
            }
            hmSubAwardDetailParam.put("proposalNumber", proposalNumber);
            hmSubAwardDetailParam.put("versionNumber", new Integer(versionNumber));
            hmSubAwardDetailParam.put("subAwardNumber", new Integer(subAwardNumber));
            Hashtable htSubAwardDetails = (Hashtable)webTxnBean.getResults(request, "getSubAwardDetails", hmSubAwardDetailParam);
            Vector vecSubAwardDetails = (Vector)htSubAwardDetails.get("getSubAwardDetails");
            Hashtable htBudgetPeriods = (Hashtable)webTxnBean.getResults(request, "getBudgetPeriodData", hmSubAwardDetailParam);
            Vector vecBudgetPeriod = (Vector)htBudgetPeriods.get("getBudgetPeriodData");
            if(vecSubAwardDetails == null || vecSubAwardDetails.isEmpty()){
                vecSubAwardDetails =  buildSubAwardDetailsForPeriods(request,proposalNumber,versionNumber,coeusDynaBeanList,vecBudgetPeriod);
            }else if(vecSubAwardDetails != null && !vecSubAwardDetails.isEmpty()){
                  if(vecBudgetPeriod!= null && (vecBudgetPeriod.size() != vecSubAwardDetails.size())){
                      Vector vecSubAwardPeriodToCreate = new Vector();
                      for(Object periodDetail : vecBudgetPeriod){
                          DynaValidatorForm budgetPeriodForm = (DynaValidatorForm)periodDetail;
                          int budgetPeriod = ((Integer)budgetPeriodForm.get("budgetPeriod")).intValue();
                          boolean hasSubAwardDetails = false;
                          for(Object subAwardDetail : vecSubAwardDetails){
                              DynaValidatorForm subAwardDetailForm = (DynaValidatorForm)subAwardDetail;
                              int subAwardPeriod = ((Integer)subAwardDetailForm.get("budgetPeriod")).intValue();
                              if(budgetPeriod == subAwardPeriod){
                                  hasSubAwardDetails = true;
                                  break;
                              }
                          }
                          if(!hasSubAwardDetails){
                              vecSubAwardPeriodToCreate.add(budgetPeriodForm);
                          }
                          
                      }
                      if(vecSubAwardPeriodToCreate != null && !vecSubAwardPeriodToCreate.isEmpty()){
                          vecSubAwardDetails.addAll(buildSubAwardDetailsForPeriods(request,proposalNumber,versionNumber,coeusDynaBeanList,vecSubAwardPeriodToCreate));
                      }
                  }
            }
           
            coeusDynaBeanList.setBeanList(vecSubAwardDetails);
            request.setAttribute("subAwardDetailList",coeusDynaBeanList);

        }else if(SYNC_XML.equals(actionMapping.getPath())){
            String proposalNumber =(String)session.getAttribute("subAwardProposalNumber");
            int versionNumber =Integer.parseInt((String)session.getAttribute("subAwardVersionNumber"));
            int subAwardNumber =Integer.parseInt((String)session.getAttribute("subAwardNumber"));
            BudgetSubAwardTxnBean budgetSubAwardTxnBean = new BudgetSubAwardTxnBean();
            BudgetSubAwardBean subAwardBean = new BudgetSubAwardBean();
            subAwardBean.setProposalNumber(proposalNumber);
            subAwardBean.setVersionNumber(versionNumber);
            subAwardBean.setSubAwardNumber(subAwardNumber);
            Vector vecSyncXmlSubAwardDetail = budgetSubAwardTxnBean.syncXML(subAwardBean);
            BeanUtilsBean copyBean = new BeanUtilsBean();
            if(vecSyncXmlSubAwardDetail != null && !vecSyncXmlSubAwardDetail.isEmpty()){
                Vector vecSyncForm = new Vector();
                for(Object subAwardDetails : vecSyncXmlSubAwardDetail){
                    DynaActionForm subAwardDynsActionForm = coeusDynaBeanList.getDynaForm(request,"subAwardDetailsForm");
                    BudgetSubAwardDetailBean subAwardDetailBean = (BudgetSubAwardDetailBean)subAwardDetails;
                    copyBean.copyProperties(subAwardDynsActionForm,subAwardDetailBean);
                    vecSyncForm.add(subAwardDynsActionForm);
                }
                
                Vector vecSubAwardDetail = (Vector)coeusDynaBeanList.getBeanList();
                vecSubAwardDetail = formatAllCostValues(vecSubAwardDetail);
                
                coeusDynaBeanList.setBeanList(updSubAwdDetailBeanAfterSync(vecSubAwardDetail,vecSyncForm));
                request.setAttribute("subAwardDetailList",coeusDynaBeanList);
            }else{
                
                ActionMessages actionMessages = new ActionMessages();
                actionMessages.add("sync.failed",new ActionMessage("subAward_sync_exceptionCode.1000"));
                saveMessages(request, actionMessages);

                Vector vecSubAwardDetail = (Vector)coeusDynaBeanList.getBeanList();
                vecSubAwardDetail = formatAllCostValues(vecSubAwardDetail);
                
                coeusDynaBeanList.setBeanList(vecSubAwardDetail);
                request.setAttribute("subAwardDetailList",coeusDynaBeanList);
            }
        
        }else if(SAVE_SUB_AWARD_DETAILS_MAPPING.equals(actionMapping.getPath())){
            Vector vecSubAwardDetail = (Vector)coeusDynaBeanList.getBeanList();
            if(vecSubAwardDetail != null && !vecSubAwardDetail.isEmpty()){
                UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute("user"+request.getSession().getId());
                BudgetSubAwardTxnBean subAwardTxnBean = new BudgetSubAwardTxnBean(userInfoBean.getUserId());
                boolean isSubAwardDetailsUpdated = false;
                Vector vecSubAwardDetails = new Vector();
                String proposalNumber =(String)session.getAttribute("subAwardProposalNumber");
                int versionNumber =Integer.parseInt((String)session.getAttribute("subAwardVersionNumber"));
                int subAwardNumber =Integer.parseInt((String)session.getAttribute("subAwardNumber"));
                for(Object subAwardDetails : vecSubAwardDetail){
                    DynaValidatorForm subAwardDetailForm = (DynaValidatorForm)subAwardDetails;
                    String acType = (String)subAwardDetailForm.get("acType");
                    BudgetSubAwardDetailBean subAwardDetailBean = new BudgetSubAwardDetailBean();
                    subAwardDetailBean.setProposalNumber((String)subAwardDetailForm.get("proposalNumber"));
                    subAwardDetailBean.setVersionNumber(((Integer)subAwardDetailForm.get("versionNumber")).intValue());
                    subAwardDetailBean.setSubAwardNumber(subAwardNumber);
                    subAwardDetailBean.setBudgetPeriod(((Integer)subAwardDetailForm.get("budgetPeriod")).intValue());
                    subAwardDetailBean.setPeriodStartDate((java.sql.Date)subAwardDetailForm.get("periodStartDate"));
                    subAwardDetailBean.setPeriodEndDate((java.sql.Date)subAwardDetailForm.get("periodEndDate"));
                    double directCost = formatStringToDouble((String)subAwardDetailForm.get("directCost"));
                    subAwardDetailBean.setDirectCost(directCost);
                    subAwardDetailForm.set("directCost", directCost+"");
                    double indirectCost = formatStringToDouble((String)subAwardDetailForm.get("indirectCost"));
                    subAwardDetailBean.setIndirectCost(indirectCost);
                    subAwardDetailForm.set("indirectCost", indirectCost+"");
                    double costSharingAmount = formatStringToDouble((String)subAwardDetailForm.get("costSharingAmount"));
                    subAwardDetailBean.setCostSharingAmount(costSharingAmount);
                    subAwardDetailForm.set("costSharingAmount", costSharingAmount+"");
                    subAwardDetailBean.setAcType(acType);
                    if(TypeConstants.UPDATE_RECORD.equals(acType)){
                        subAwardDetailBean.setAwUpdateTimestamp(Timestamp.valueOf((String)subAwardDetailForm.get("awUpdateTimestamp")));
                        subAwardDetailBean.setUpdateUser((String)subAwardDetailForm.get("awUpdateUser"));
                    }
                    vecSubAwardDetails.add(subAwardDetailBean);
                    if(TypeConstants.INSERT_RECORD.equals(acType) || TypeConstants.UPDATE_RECORD.equals(acType)){
                       isSubAwardDetailsUpdated = true;
                       subAwardTxnBean.updateSubAwardDetails(subAwardDetailBean);
                    }
                }
                boolean isCostElementIactive = false;
                boolean subAwardCostElementIactive = false;
                if(session.getAttribute("subAwardCostElementIactive")!= null){
                    subAwardCostElementIactive = ((Boolean)session.getAttribute("subAwardCostElementIactive")).booleanValue();
                }
                BudgetUpdateTxnBean budgetUpdateTxnBean = new BudgetUpdateTxnBean();
                boolean hasSubAwardLineItem = budgetUpdateTxnBean.checkBudgetHasSubawardLineItem(proposalNumber,versionNumber,subAwardNumber);
                if(isSubAwardDetailsUpdated || !hasSubAwardLineItem){

                    String organizationName = (String)session.getAttribute("subAwardOrg" +
                            "anizationName");
                    budgetUpdateTxnBean.deleteSubAwardCostLineItem(proposalNumber,versionNumber,subAwardNumber);
                     isCostElementIactive = subAwardTxnBean.createLineItemsForSubAward(proposalNumber,versionNumber,organizationName,vecSubAwardDetails);
                    if(isCostElementIactive){
                        ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("subAwardDetailCostElementInactive.error" , new ActionMessage("subAwardDetailCostElementInactive.error"));
                        saveMessages(request, actionMessages);
                    }else{
                         // Added for COEUSQA-3420 : Lite does not recognized Cost Sharing from Sub Award Details screen - Start
                         BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
                         Hashtable htBudgetData = initializeBudgetCalculator(budgetInfoBean);
                         QueryEngine queryEngine = QueryEngine.getInstance();
                         String queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
                         queryEngine.addDataCollection(queryKey,htBudgetData);
                         calulateAndSaveBudget(budgetInfoBean,session);
                         // Added for COEUSQA-3420 : Lite does not recognized Cost Sharing from Sub Award Details screen - End
                        
                    }
                }
                session.setAttribute("subAwardCostElementIactive",new Boolean(isCostElementIactive));
                if(isCostElementIactive){
                    HashMap hmSubAwardDetailParam = new HashMap();
                    hmSubAwardDetailParam.put("proposalNumber", proposalNumber);
                    hmSubAwardDetailParam.put("versionNumber", new Integer(versionNumber));
                    hmSubAwardDetailParam.put("subAwardNumber", new Integer(subAwardNumber));
                    Hashtable htSubAwardDetails = (Hashtable)webTxnBean.getResults(request, "getSubAwardDetails", hmSubAwardDetailParam);
                    coeusDynaBeanList.setBeanList((Vector)htSubAwardDetails.get("getSubAwardDetails"));
                    request.setAttribute("subAwardDetailList",coeusDynaBeanList);
                }else{
                     request.setAttribute("closeSubAwardDetails",new Boolean(true));
                }
            }
            
        }
        ActionForward actionForward = actionMapping.findForward(navigator);
        return actionForward;
    }
    
    /**
     * Method to build Sub award details form for a period
     * @param request 
     * @param proposalNumber 
     * @param versionNumber 
     * @param coeusDynaBeanList 
     * @param vecBudgetPeriods 
     * @throws java.lang.Exception 
     * @return vecSubAwardDetails
     */
    private Vector buildSubAwardDetailsForPeriods(HttpServletRequest request,String proposalNumber, String versionNumber,
            CoeusDynaBeansList coeusDynaBeanList,Vector vecBudgetPeriods) throws Exception{
        Vector vecSubAwardDetails = new Vector();
        if(vecBudgetPeriods != null && !vecBudgetPeriods.isEmpty()){
            for(Object budgetPeriod : vecBudgetPeriods){
                DynaValidatorForm budgetPeriodForm = (DynaValidatorForm)budgetPeriod;
                DynaActionForm dynaForm = coeusDynaBeanList.getDynaForm(request,"subAwardDetailsForm");
                dynaForm.set("proposalNumber",proposalNumber);
                dynaForm.set("versionNumber",new Integer(versionNumber));
                dynaForm.set("budgetPeriod",budgetPeriodForm.get("budgetPeriod"));
                dynaForm.set("periodStartDate",budgetPeriodForm.get("startDate"));
                dynaForm.set("periodEndDate",budgetPeriodForm.get("endDate"));
                dynaForm.set("acType",TypeConstants.INSERT_RECORD);
                vecSubAwardDetails.add(dynaForm);
            }
        }
        return vecSubAwardDetails;
    }
    
    /**
     * Method to update the bean after the sync, will iterate the xml period details a
     * update the actype and the directCost, Indirect Cost and costsharing from the XML period details to the sub award details
     * @param vecCurrentSubAwardData
     * @param vecSubAwardPDFDetail
     * @return vecSubAwardPDFDetail
     */
    private Vector updSubAwdDetailBeanAfterSync(Vector vecCurrentSubAwardData, Vector vecSubAwardPDFDetail){
        for(int subAwardPDFDetailIndex=0;subAwardPDFDetailIndex<vecSubAwardPDFDetail.size();subAwardPDFDetailIndex++){
            DynaValidatorForm subAwardPdfDetail = (DynaValidatorForm)vecSubAwardPDFDetail.get(subAwardPDFDetailIndex);
            for(int subAwardDetailIndex=0;subAwardDetailIndex<vecCurrentSubAwardData.size();subAwardDetailIndex++){
                DynaValidatorForm subAwardDetailBean = (DynaValidatorForm)vecCurrentSubAwardData.get(subAwardDetailIndex);
                if(subAwardDetailBean.get("budgetPeriod").toString().equals(subAwardPdfDetail.get("budgetPeriod").toString())){
                    if(subAwardDetailBean.get("acType") == null || "".equals(subAwardDetailBean.get("acType").toString())){
                        subAwardDetailBean.set("acType",TypeConstants.UPDATE_RECORD);
                    }
                    subAwardDetailBean.set("directCost",subAwardPdfDetail.get("directCost"));
                    subAwardDetailBean.set("indirectCost",subAwardPdfDetail.get("indirectCost"));
                    subAwardDetailBean.set("costSharingAmount",subAwardPdfDetail.get("costSharingAmount"));
                    subAwardDetailBean.set("periodStartDate",subAwardPdfDetail.get("periodStartDate"));
                    subAwardDetailBean.set("periodEndDate",subAwardPdfDetail.get("periodEndDate"));
                }
            }
        }
        return vecCurrentSubAwardData;
    }
    
    /**
     * Method to remove $ symbol in all the amount properties
     * @param vecSubAwardDetail 
     * @throws java.lang.Exception 
     * @return vecSubAwardDetail
     */
    private Vector formatAllCostValues(Vector vecSubAwardDetail) throws Exception{
        if(vecSubAwardDetail != null && !vecSubAwardDetail.isEmpty()){
            for(Object subAwardDetails : vecSubAwardDetail){
                DynaValidatorForm subAwardDetailForm = (DynaValidatorForm)subAwardDetails;
                double directCost = formatStringToDouble((String)subAwardDetailForm.get("directCost"));
                subAwardDetailForm.set("directCost",directCost+"");
                double indirectCost = formatStringToDouble((String)subAwardDetailForm.get("indirectCost"));
                subAwardDetailForm.set("indirectCost",indirectCost+"");
                double costSharingAmount = formatStringToDouble((String)subAwardDetailForm.get("costSharingAmount"));
                subAwardDetailForm.set("costSharingAmount",costSharingAmount+"");
            }
        }
        return vecSubAwardDetail;
    }
    
}


