/*
 * BudgetSubAwardCostSharingAction.java
 *
 * Created on July 22, 2011, 5:05 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.wmc.coeuslite.budget.action;

import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
import edu.mit.coeus.budget.bean.BudgetPeriodBean;
import edu.mit.coeus.budget.bean.BudgetSubAwardDetailBean;
import edu.mit.coeus.budget.bean.BudgetSubAwardTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.wmc.coeuslite.budget.bean.ProposalBudgetHeaderBean;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author maharajap
 */
public class BudgetSubAwardCostSharingAction  extends BudgetBaseAction {
    
    private static final String GET_SUB_AWARD_DETAILS = "/getSubAwardCostSharing";    
    
    /** Creates a new instance of BudgetSubAwardCostSharingAction */
    public BudgetSubAwardCostSharingAction() {
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
        HttpSession session = request.getSession();
        DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)actionForm;
        ActionForward actionForward = performSubAwardCostSharingAction(dynaValidatorForm, request, response, actionMapping);
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
    
    private ActionForward performSubAwardCostSharingAction(DynaValidatorForm dynaValidatorForm,
    HttpServletRequest request, HttpServletResponse response, ActionMapping actionMapping) throws Exception{
        HttpSession session = request.getSession();
        String navigator = EMPTY_STRING;
        edu.mit.coeus.bean.UserInfoBean userInfoBean = (edu.mit.coeus.bean.UserInfoBean)session.getAttribute("user"+session.getId());
        WebTxnBean webTxnBean = new WebTxnBean();
        if(actionMapping.getPath().equals(GET_SUB_AWARD_DETAILS)){
            navigator = getSubAwardCostSharingDetails(dynaValidatorForm, userInfoBean, request);
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
    private String getSubAwardCostSharingDetails(DynaValidatorForm dynaValidatorForm,
    edu.mit.coeus.bean.UserInfoBean userInfoBean, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        List lstSubAwardCostSharing = getSubAwardCostSharingData(dynaValidatorForm, request);
        
        session.setAttribute("lstSubAwardCostSharing",lstSubAwardCostSharing);
        
        return "success";
    }
    
    /**
     * This method is used to retrieve the sub award data
     * @param dynaValidatorForm
     * @param request
     * @throws Exception
     * @return String to navigator
     */
    private List getSubAwardCostSharingData(DynaValidatorForm dynaValidatorForm, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        String proposalNumber = EMPTY_STRING;
        int version = 0;
        List lstSubAwardCostSharing = new ArrayList();
        
        ProposalBudgetHeaderBean headerBean = (ProposalBudgetHeaderBean)request.getSession().getAttribute("ProposalBudgetHeaderBean");
        proposalNumber = (String)request.getSession().getAttribute("proposalNumber"+request.getSession().getId());
        version = headerBean.getVersionNumber();
        
        BudgetSubAwardTxnBean budgetSubAwardTxnBean = new BudgetSubAwardTxnBean();
        lstSubAwardCostSharing = budgetSubAwardTxnBean.getBudgetSubAwardCostSharingAmount(proposalNumber,version);
        BudgetDataTxnBean txnBean = new BudgetDataTxnBean();
        CoeusVector vecBudgetPeriodBean = txnBean.getBudgetPeriods(proposalNumber,version);
        
        //to put the cost sharing data in HashMap based on organization
        List lstSubAwardCostSharingData;
        if(lstSubAwardCostSharing!=null){
           lstSubAwardCostSharingData = prepareSubAwardCostSharingData(lstSubAwardCostSharing, vecBudgetPeriodBean, request); 
        }else{
            lstSubAwardCostSharingData = new ArrayList();
        }
        
        return lstSubAwardCostSharingData;
    }
    
    /**
     * This method is used to retrieve the sub award data
     * @param lstSubAwardCostSharing
     * @param vecBudgetPeriodBean
     * @param request
     * @throws Exception
     * @return String to navigator
     */
    private List prepareSubAwardCostSharingData(List lstSubAwardCostSharing, CoeusVector vecBudgetPeriodBean, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        List lstCostSharingData = new ArrayList();
        Set subAwardUniqueData = new TreeSet();
        List lstSubAwardOrganizationList = new ArrayList(10);
        //to create unique list of organization for the sub award
        for(Object subAwardData:lstSubAwardCostSharing){
            BudgetSubAwardDetailBean budgetSubAwardDetailBean = (BudgetSubAwardDetailBean)subAwardData;
            int subAwardNumber = budgetSubAwardDetailBean.getSubAwardNumber();
            if(subAwardUniqueData.add(new Integer(subAwardNumber))){
                lstSubAwardOrganizationList.add(budgetSubAwardDetailBean.getOrganizationName());
            }
        }
        //to fetch the sub award cost sharing data and group them if they belong to same sub award
        for(Object subAward:subAwardUniqueData){
            int subAwardNumber = (((Integer)subAward).intValue());
            List lstSubAwardCostSharingList = new ArrayList(10);
            for(Object subAwardData:lstSubAwardCostSharing){
                BudgetSubAwardDetailBean budgetSubAwardDetailBean = (BudgetSubAwardDetailBean)subAwardData;
                int beanSubAwardNumber = budgetSubAwardDetailBean.getSubAwardNumber();
                if(subAwardNumber==beanSubAwardNumber){
                    lstSubAwardCostSharingList.add(budgetSubAwardDetailBean);
                }
            }
            List lstSubAwardSharingData = validateForBudgetPeriods(lstSubAwardCostSharingList,vecBudgetPeriodBean);
            lstCostSharingData.add(lstSubAwardSharingData);
        }        
        return lstCostSharingData;
    }
    
    /**
     * This method is used to add the extra periods whose cost sharing amount is not added
     * @param List lstSubAwardCostSharingList
     * @param CoeusVector vecBudgetPeriodBean     
     * @return lstSubAwardCostSharingList
     */
    public List validateForBudgetPeriods(List lstSubAwardCostSharingList, CoeusVector vecBudgetPeriodBean){
        if(lstSubAwardCostSharingList.size()==vecBudgetPeriodBean.size()){
            //do nothing
            return lstSubAwardCostSharingList;
        }else{
            int totSize = lstSubAwardCostSharingList.size();
            for(int counter=0;counter<vecBudgetPeriodBean.size();counter++){
                if(counter+1>totSize){
                    BudgetSubAwardDetailBean subBudgetSubAwardBean = new BudgetSubAwardDetailBean();
                    BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean)vecBudgetPeriodBean.get(counter);
                    subBudgetSubAwardBean.setCostSharingAmount(new Double(0.00));
                    subBudgetSubAwardBean.setPeriodStartDate(budgetPeriodBean.getStartDate());
                    subBudgetSubAwardBean.setBudgetPeriod(budgetPeriodBean.getBudgetPeriod());
                    subBudgetSubAwardBean.setProposalNumber(budgetPeriodBean.getProposalNumber());
                    subBudgetSubAwardBean.setVersionNumber(budgetPeriodBean.getVersionNumber());
                    lstSubAwardCostSharingList.add(subBudgetSubAwardBean);
                }
            }
        }
        return lstSubAwardCostSharingList;
    }
    
}
