/*
 * BudgetSubAwardServlet.java
 *
 * Created on May 19, 2006, 2:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.servlet;

import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.budget.BudgetSubAwardConstants;
import edu.mit.coeus.budget.bean.*;
import edu.mit.coeus.propdev.bean.ProposalHierarchyLinkBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sharathk
 */
public class BudgetSubAwardServlet extends CoeusBaseServlet
        implements TypeConstants, BudgetSubAwardConstants {
    
    /** Creates a new instance of BudgetSubAwardServlet */
    public BudgetSubAwardServlet() {
    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        RequesterBean requester = null;
        ResponderBean responder = new ResponderBean();
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        String loggedinUser = "";
        String unitNumber = "";
        try {
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            requester = (RequesterBean)inputFromApplet.readObject();
            isValidRequest(requester);
            UserInfoBean userBean = (new UserDetailsBean()).getUserInfo(requester.getUserName());
            loggedinUser = requester.getUserName();
            unitNumber = userBean.getUnitNumber();
//            BudgetSubAwardTxnBean budgetSubAwardTxnBean = new BudgetSubAwardTxnBean(requester.getUserName());
            BudgetSubAwardTxnBean budgetSubAwardTxnBean = new BudgetSubAwardTxnBean(requester);
            Vector dataObjects = new Vector();
            char functionType = requester.getFunctionType();
            if(functionType == GET_BUDGET_SUB_AWARD) {//Get BudgetSubAward
                BudgetBean budgetbean = (BudgetBean)requester.getDataObject();                
                //Commented and Added for Case#3404-Outstanding proposal hierarchy changes - Start
//                responder.setDataObject(budgetSubAwardTxnBean.getBudgetSubAward(budgetbean.getProposalNumber(), budgetbean.getVersionNumber()));
                Vector vecResult = new Vector();
                vecResult.add(budgetSubAwardTxnBean.getBudgetSubAward(budgetbean.getProposalNumber(), budgetbean.getVersionNumber()));                                
                ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
                List lstPropHierLink = proposalNarrativeTxnBean.getPropHierLinkData(budgetbean.getProposalNumber());
                Vector vecPropHierLink = new Vector();
                //Filtering only those data from lstPropHierLink which have link type as 'S'(Sub Award Attachment)
                if(lstPropHierLink != null && lstPropHierLink.size() > 0){
                    for(int i =0 ; i<lstPropHierLink.size();i++){
                       ProposalHierarchyLinkBean proposalHierarchyLinkBean = (ProposalHierarchyLinkBean)lstPropHierLink.get(i) ;
                       if("S".equalsIgnoreCase(proposalHierarchyLinkBean.getLinkType())){
                           vecPropHierLink.add(proposalHierarchyLinkBean);
                       }
                    }
                }
                vecResult.add(vecPropHierLink);
                responder.setDataObjects(vecResult);
                //Commented Added for Case#3404-Outstanding proposal hierarchy changes -End
                responder.setResponseStatus(true);
            // JM 6-25-2013 get organization list for subawards
            } else if(functionType == GET_PROP_ORGS_FOR_SUB) {
	            String proposalNumber = (String)requester.getDataObject();
	            //edu.mit.coeus.utils.CoeusVector cvOrganizations = 
	            Vector cvOrganizations = 
	            	budgetSubAwardTxnBean.getPropOrganizationsForSubaward(proposalNumber);
	            responder.setDataObject(cvOrganizations);
	            responder.setResponseStatus(true);
            // JM END
            } else if(functionType == SAVE_BUDGET_SUB_AWARD) { //Save BudgetSubAward
                List list = (List)requester.getDataObject();
                
                StringBuffer strBuff = request.getRequestURL();
                String servletPath = new String(strBuff);
                servletPath = servletPath.substring(0,servletPath.lastIndexOf('/'));
                list = budgetSubAwardTxnBean.checkAndUpdate(list, servletPath+"/S2SServlet");
                list = budgetSubAwardTxnBean.saveBudgetSubAward(list);
                responder.setDataObject(list);
                responder.setResponseStatus(true);
            } else if(functionType == TRANSLATE) {
                BudgetSubAwardBean budgetSubAwardBean = (BudgetSubAwardBean)requester.getDataObject();
                StringBuffer strBuff = request.getRequestURL();
                String servletPath = new String(strBuff);
                servletPath = servletPath.substring(0,servletPath.lastIndexOf('/'));
                budgetSubAwardBean = budgetSubAwardTxnBean.checkAndUpdate(budgetSubAwardBean, servletPath+"/S2SServlet");
                budgetSubAwardBean = budgetSubAwardTxnBean.saveBudgetSubAward(budgetSubAwardBean);
                responder.setDataObject(budgetSubAwardBean);
                responder.setResponseStatus(true);
                
            } //COEUSQA-2735 Cost sharing distribution for Sub awards - Start
            else if(functionType == GET_SUB_AWARD_COST_SHARING) {
                Vector vecBudgetData = (Vector)requester.getDataObjects();
                String proposalNumber = (String)vecBudgetData.get(0);
                int versionNumber = (Integer)vecBudgetData.get(1);
                List lstSubAwardCostSharing = budgetSubAwardTxnBean.getBudgetSubAwardCostSharingAmount(proposalNumber,versionNumber);
                responder.setDataObject(lstSubAwardCostSharing);
                responder.setResponseStatus(true);
            }//COEUSQA-2735 Cost sharing distribution for Sub awards - End
            // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
            else if(functionType == SYNC_XML_FOR_SUB_AWARD_DETAILS){
                BudgetSubAwardBean budgetSubAwardBean = (BudgetSubAwardBean)requester.getDataObject();
                Vector vecSubAwardDetails = budgetSubAwardTxnBean.syncXML(budgetSubAwardBean);
                if(vecSubAwardDetails != null && !vecSubAwardDetails.isEmpty()){
                    responder.setDataObjects(vecSubAwardDetails);
                    responder.setResponseStatus(true);
                }else{
                    CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
                    String errMsg = coeusMessageResourcesBean.parseMessageKey("subAward_sync_exceptionCode.1000");
                    responder.setMessage(errMsg);
                    responder.setResponseStatus(false);
                    
                }
            }
            // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - End
        } catch(Exception e) {
            //e.printStackTrace();
            responder.setResponseStatus(false);
            responder.setException(e);
            responder.setMessage(e.getMessage());
            UtilFactory.log(e.getMessage(), e, "BudgetSubAwardServlet", "doPost");
            
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "BudgetSubAwardServlet", "doPost");
        //Case 3193 - END
            
        } finally {
            try {
                outputToApplet = new ObjectOutputStream(response.getOutputStream());
                outputToApplet.writeObject(responder);
                if(inputFromApplet != null)
                    inputFromApplet.close();
                if(outputToApplet != null) {
                    outputToApplet.flush();
                    outputToApplet.close();
                }
            } catch(IOException ioe) {
                UtilFactory.log(ioe.getMessage(), ioe, "BudgetSubAwardServlet", "doPost");
            }
        }
    }
}