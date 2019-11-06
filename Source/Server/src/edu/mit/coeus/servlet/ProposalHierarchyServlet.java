/*
 * ProposalHierarchyServlet.java
 *
 * Created on August 11, 2005, 4:07 PM
 */
/*
 * PMD check performed, and commented unused imports and variables on 01-MAR-2011
 * by Maharaja Palanichamy
 */
package edu.mit.coeus.servlet;

import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.MedusaTxnBean;
import edu.mit.coeus.propdev.bean.ProposalBudgetBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalHierarchyBean;
import edu.mit.coeus.propdev.bean.ProposalHierarchyChildTypeBean;
import edu.mit.coeus.budget.bean.*;
import edu.mit.coeus.budget.calculator.bean.ValidCalcTypesBean;

import edu.mit.coeus.propdev.bean.ProposalHierarchyTxnBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeFormBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTypeBean;
import edu.mit.coeus.rates.bean.RatesTxnBean;
import edu.mit.coeus.routing.bean.RoutingTxnBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.locking.LockingException;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.utils.xml.bean.proposal.bean.ProposalPrintingTxnBean;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author  chandrashekara
 */
public class ProposalHierarchyServlet extends CoeusBaseServlet implements TypeConstants{
    private static final char GET_PARENT_DATA = 'A' ;
    private static final char GET_HIERARCHY_DATA = 'B';
    private static final char CHECK_RIGHT = 'C';
    private static final char CAN_BUILD_HIERARCHY = 'D'; 
    private static final char CREATE_PARENT_PROPOSAL = 'E'; 
    private static final char GET_PROPOSAL_DETAILS = 'F'; 
    private static final char GET_BUDGET_PERSONS = 'G';
    private static final char JOIN_PROPOSAL = 'H'; 
    private static final char SYNC_ALL_PROPOSAL = 'I';
    private static final char GET_BUDGET_HIERARCHY_DETAILS = 'J';
    private static final char SYNC_BUDGET = 'K';
    private static final char CHECK_VIEW_RIGHT = 'L';
    private static final char GET_PROP_HIER_CHILD_TYPE = 'M';
    private static final char REMOVE_PROP_FROM_HIERARCHY = 'O';
    private static final char REFRESH_HIERARCHY = 'P';
    private static final char IS_BUDGET_COMPLETE = 'Q'; 
    private static final char CAN_LINK_TO_HIERARCHY = 'R';
    
    //COEUSQA-1689 Role Restrictions for Budget Rates - Start
    private static final char CHECK_MODIFY_PROPOSAL_RATES_RIGHT = 'T';
    private static final String MODIFY_PROPOSAL_RATES = "MODIFY_PROPOSAL_RATES";
    private static final String MODIFY_ANY_PROPOSAL_RATES = "MODIFY_ANY_PROPOSAL_RATES";
    //COEUSQA-1689 Role Restrictions for Budget Rates - End
    //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
    private static final char CHECK_VIEW_INSTITUTIONAL_SALARIES_RIGHT = 'U';
    private static final String VIEW_INSTITUTIONAL_SALARIES = "VIEW_INSTITUTIONAL_SALARIES";
    private static final String VIEW_PROP_PERSON_INST_SALARIES = "VIEW_PROP_PERSON_INST_SALARIES";    
    //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End
    
    private static final String EMPTY_STRING = "";
    private static final String CREATE_HIERARCHY = "create";
    private static final String JOIN_HIERARCHY = "join";
    private static final String REMOVE_HIERARCHY = "remove";
    private static final int PROPSOAL_AGGREGATOR_ROLE = 100;
    private static final String MAINTAIN_PROPOSAL_HIERARCHY = "MAINTAIN_PROPOSAL_HIERARCHY";
    //Added with COEUSDEV 197,198 - Validation Checks for syncing Proposals
    private static final char HIERARCHY_VALIDATION_CHECKS = 'S';
    //COEUSDEV 197,198 End    
    
    /** Creates a new instance of ProposalHierarchyServlet */
    public ProposalHierarchyServlet() {
    }
    
     /**
     * This method handles all the POST requests from the Client
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if any ServletException
     * @throws IOException if any IOException
     */
    public void doPost(HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        // the request object from applet
        RequesterBean requester = null;
        // the response object to applet
        ResponderBean responder = new ResponderBean();
        
        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        
        String loggedinUser =EMPTY_STRING;
        String loggedUnitNumber = EMPTY_STRING;
        String userId = EMPTY_STRING;
        String proposalNumber = EMPTY_STRING;
        //ProposalHierarchyTxnBean proposalHierarchyTxnBean;
        CoeusVector cvChildPropData = null;
        try {
            // get an input stream
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            loggedinUser = requester.getUserName();
            
            // get the user
            UserInfoBean userBean = (UserInfoBean)new
            UserDetailsBean().getUserInfo(requester.getUserName());
            
            loggedUnitNumber = userBean.getUnitNumber();
            userId = userBean.getUserId();
            char functionType = requester.getFunctionType();
            ProposalHierarchyTxnBean txnBean = null;
            ProposalDevelopmentTxnBean proposalDataTxnBean = null;
            ProposalDevelopmentFormBean proposalDevelopmentFormBean = null;
            
            if(functionType==GET_PARENT_DATA){
                proposalNumber = requester.getId();
                txnBean = new ProposalHierarchyTxnBean();
                HashMap data = txnBean.getParentProposalData(proposalNumber);
                responder.setDataObject(data);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType==GET_HIERARCHY_DATA){
                proposalNumber = requester.getId();
                txnBean = new ProposalHierarchyTxnBean();
                edu.mit.coeus.propdev.bean.ProposalHierarchyBean proposalHierarchyBean = 
                    txnBean.getHierarchyData(proposalNumber);
                responder.setDataObject(proposalHierarchyBean);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType==CHECK_RIGHT){
                
            }else if(functionType==CAN_BUILD_HIERARCHY){
                Vector data = new Vector();
                proposalNumber = (String)requester.getDataObject();
                String actionType = requester.getId();
                txnBean = new ProposalHierarchyTxnBean();
                UserMaintDataTxnBean userTxnBean = new UserMaintDataTxnBean();
                int code = txnBean.canBuildHierarchy(proposalNumber,actionType);
                boolean hasRight = userTxnBean.getUserHasProposalRight(loggedinUser,proposalNumber, MAINTAIN_PROPOSAL_HIERARCHY);
                data.add(0, new Boolean(hasRight));
                data.add(1,new Integer(code));
                responder.setDataObjects(data);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType== CREATE_PARENT_PROPOSAL){
                Vector data = new Vector();
                 BudgetDataTxnBean budgetDataTxnBean = null;
                Vector dataObjects = (Vector) requester.getDataObjects();
                proposalNumber = (String)dataObjects.elementAt(0);
                String unitNumber = (String)dataObjects.elementAt(1);
                proposalDataTxnBean = new ProposalDevelopmentTxnBean();
                String parentProposalNumber =  proposalDataTxnBean.getNextProposalNumber();
                txnBean = new ProposalHierarchyTxnBean(loggedinUser);
                
               //Added for case: Budget Summary Changes in Proposal Hierarchy -Start 
                if(!checkPersonRateAndBaseExist(proposalNumber)){                
                // Added for Case 3131 : Proposal Hierarchy - Salary details from child proposals are not printed in parent summary -Start
                cvChildPropData = prepareBudgetEDICalculation(userId, parentProposalNumber, proposalNumber, CREATE_PARENT_PROPOSAL);
                }
                //Added for case: Budget Summary Changes in Proposal Hierarchy -End
                int createFlag = txnBean.createProposal(proposalNumber, parentProposalNumber, loggedinUser,unitNumber);
                if (cvChildPropData != null && cvChildPropData.size() > 0){
                    for(int index = 0; index < cvChildPropData.size(); index++){
                        budgetDataTxnBean = new BudgetDataTxnBean();
                        String childPropNumber = (String) cvChildPropData.get(index);
                        budgetDataTxnBean.cleanUpEdiTempData(childPropNumber);
                    }
                }                
                // Added for Case 3131 : Proposal Hierarchy - Salary details from child proposals are not printed in parent summary -End
                proposalDevelopmentFormBean = proposalDataTxnBean.getProposalDevelopmentDetails(parentProposalNumber);
                data.addElement(new Integer(createFlag));
                data.addElement(proposalDevelopmentFormBean);
                responder.setResponseStatus(true);
                responder.setDataObjects(data);
            }else if(functionType==GET_PROPOSAL_DETAILS){
                HashMap dataObjects = new HashMap();
                proposalNumber = requester.getId();
                MedusaTxnBean medusaTxnBean = new MedusaTxnBean();
                proposalDevelopmentFormBean = medusaTxnBean.getDevProposalDetails(proposalNumber);
                proposalDataTxnBean = new ProposalDevelopmentTxnBean();
                Vector investigatorDetails  = proposalDataTxnBean.getProposalInvestigatorDetails(proposalNumber);
                dataObjects.put(ProposalDevelopmentFormBean.class,proposalDevelopmentFormBean);
                dataObjects.put(edu.mit.coeus.propdev.bean.ProposalInvestigatorFormBean.class,investigatorDetails);
                responder.setResponseStatus(true);
                responder.setDataObject(dataObjects);
            }else if(functionType==GET_BUDGET_PERSONS){
                HashMap dataMap = null;
                CoeusVector budgetPersonsData = null;
                CoeusVector clientData = (CoeusVector)requester.getDataObjects();
                proposalNumber = (String)clientData.get(0);
                int versionNumber = ((Integer)clientData.get(1)).intValue();
                edu.mit.coeus.budget.bean.BudgetDataTxnBean budgetDataTxnBean = 
                    new edu.mit.coeus.budget.bean.BudgetDataTxnBean();
                if(proposalNumber!= null && versionNumber!= -1){
                    budgetPersonsData = budgetDataTxnBean.getBudgetPersons(proposalNumber, versionNumber);
                    BudgetInfoBean budgetInfoBean  = budgetDataTxnBean.getBudgetForProposal(proposalNumber, versionNumber);
                    dataMap = new HashMap();
                    dataMap.put(BudgetInfoBean.class,budgetInfoBean);
                    dataMap.put(edu.mit.coeus.budget.bean.BudgetPersonsBean.class,
                        budgetPersonsData!= null ? budgetPersonsData : new CoeusVector());
                    responder.setResponseStatus(true);
                    responder.setDataObject(dataMap);
                }
            }else if(functionType== JOIN_PROPOSAL){
                CoeusVector data = new CoeusVector();
                
                Vector dataObjects = requester.getDataObjects();
                String childProposalNumber = (String)dataObjects.elementAt(0);
                String parentProposalNumber = (String)dataObjects.elementAt(1);
                int childTypeCode = ((Integer)dataObjects.elementAt(2)).intValue();
                //Added for case id : 3183 -start
                boolean isParentProposal = ((Boolean)dataObjects.elementAt(3)).booleanValue();
                //Added for case id : 3183 - end
                proposalDataTxnBean = new ProposalDevelopmentTxnBean();
                txnBean = new ProposalHierarchyTxnBean(loggedinUser);
                BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
                //Added for case id : 3183 -start
                txnBean.checkLocksAvailable(parentProposalNumber, isParentProposal);  
                //Added for case id : 3183 - end
                //Added for case: Budget Summary Changes in Proposal Hierarchy -Start 
                if(!checkPersonRateAndBaseExist(childProposalNumber)){
                // Added for Case 3131 : Proposal Hierarchy - Salary details from child proposals are not printed in parent summary -Start
                cvChildPropData = prepareBudgetEDICalculation(userId, parentProposalNumber, childProposalNumber, JOIN_PROPOSAL);
                }
                //Added for case: Budget Summary Changes in Proposal Hierarchy -End
                int joinFlag = txnBean.joinProposal(parentProposalNumber,childProposalNumber,loggedinUser,childTypeCode);
                
                if (cvChildPropData != null && cvChildPropData.size() > 0){
                    for(int index = 0; index < cvChildPropData.size(); index++){
                        String childPropNumber = (String) cvChildPropData.get(index);
                        budgetDataTxnBean.cleanUpEdiTempData(childPropNumber);
                    }
                }
                 // Added for Case 3131 : Proposal Hierarchy - Salary details from child proposals are not printed in parent summary -End
                proposalDevelopmentFormBean = proposalDataTxnBean.getProposalDevelopmentDetails(parentProposalNumber);
                
                ProposalHierarchyTxnBean txn = new ProposalHierarchyTxnBean();
                HashMap hmData = txn.getParentProposalData(childProposalNumber);
                String rootProposal = (String)hmData.get("PARENT_PROPOSAL");
                
                ProposalHierarchyBean proposalHierarchyBean = null;
                if(rootProposal != null){
                    proposalHierarchyBean = txn.getHierarchyData(rootProposal);
                }

                data.addElement(new Integer(joinFlag));
                data.addElement(proposalDevelopmentFormBean);
                
                data.addElement(hmData);
                data.addElement(proposalHierarchyBean);

                responder.setResponseStatus(true);
                responder.setDataObjects(data);
            }else if(functionType==SYNC_ALL_PROPOSAL){
                /*String parentProposalNumner = (String)requester.getDataObject();
                txnBean = new ProposalHierarchyTxnBean(loggedinUser);
                String message = txnBean.checkLockBeforeSyncAllChilds(parentProposalNumner);
                responder.setDataObject(message);
                responder.setResponseStatus(true);*/
                
                BudgetDataTxnBean budgetDataTxnBean = null;
                Vector dataObjects = null;
                String parentProposalNumber = "";
                String childPropNo = "";
                CoeusVector clientData = (CoeusVector)requester.getDataObject();
                if(clientData!= null && clientData.size() >0){
                    
                    parentProposalNumber = (String)clientData.get(0);
                    childPropNo = (String)clientData.get(1);
                    boolean isParent = ((Boolean)clientData.get(2)).booleanValue();
                     // Added for Case 3131 : Proposal Hierarchy - Salary details from child proposals are not printed in parent summary -Start
                    budgetDataTxnBean = new BudgetDataTxnBean();
                     // Added for Case 3131 : Proposal Hierarchy - Salary details from child proposals are not printed in parent summary - End
                    txnBean = new ProposalHierarchyTxnBean(loggedinUser);
                    //Added for case id: 3183 - start
                    if(isParent){
                        txnBean.checkLocksAvailable(parentProposalNumber, isParent);
                    }
                    //Added for case id: 3183 - end
                    /*int syncFlag = txnBean.syncProposal(parentProposalNumber,childPropNo,isParent);
                    
                    proposalDataTxnBean = new ProposalDevelopmentTxnBean();
                    
                    proposalDevelopmentFormBean = 
                            proposalDataTxnBean.getProposalDevelopmentDetails(parentProposalNumber);
                    
                    dataObjects = new Vector();
                    dataObjects.addElement(new Integer(syncFlag));
                    dataObjects.addElement(proposalDevelopmentFormBean);
                    responder.setDataObjects(dataObjects);
                    responder.setResponseStatus(true);*/
                    int count;
                    
                    if(isParent){
                        proposalNumber = parentProposalNumber;
                        count = txnBean.checkPorposalLocks(parentProposalNumber , isParent);
                    }else{
                        proposalNumber = childPropNo;
                        count = txnBean.checkPorposalLocks(childPropNo , isParent);
                    }

                    try{
                        if(count == 1){
                            proposalDataTxnBean = new ProposalDevelopmentTxnBean();
                            //Added for case: Budget Summary Changes in Proposal Hierarchy -Start 
                            if(!checkPersonRateAndBaseExist(proposalNumber)){    
                            // Added for Case 3131 : Proposal Hierarchy - Salary details from child proposals are not printed in parent summary -Start
                            cvChildPropData = prepareBudgetEDICalculation(userId, parentProposalNumber, childPropNo, SYNC_ALL_PROPOSAL);
                            }
                            //Added for case: Budget Summary Changes in Proposal Hierarchy - End
                            // Added for Case 3131 : Proposal Hierarchy - Salary details from child proposals are not printed in parent summary - End
                            int syncFlag = txnBean.syncProposal(parentProposalNumber);
                            //Added for Proposal Hierarchy Enhancement Case# 3183 - starts
                            //To insert the blob datas to the respective tables
                            ProposalHierarchyBean hierarchyBean = txnBean.getHierarchyData(parentProposalNumber);
                            if(hierarchyBean != null){
                                CoeusVector cvChildProposals = hierarchyBean.getProposalData();
                                if(cvChildProposals != null && cvChildProposals.size() >0){
                                    for(int index = 0; index < cvChildProposals.size(); index++){ 
                                        ProposalBudgetBean proposalBudgetBean =  
                                            (ProposalBudgetBean) cvChildProposals.get(index);
                                        txnBean.syncBlobData(parentProposalNumber, proposalBudgetBean.getProposalNumber());
                                    }
                                }
                            }
                            //Added for Proposal Hierarchy Enhancement Case# 3183 - ends
 			// Added for Case 3131 : Proposal Hierarchy - Salary details from child proposals are not printed in parent summary -Start
                           if (cvChildPropData != null && cvChildPropData.size() > 0){
                                for(int index = 0; index < cvChildPropData.size(); index++){
                                    String childProposalNumber = (String) cvChildPropData.get(index);
                                    budgetDataTxnBean.cleanUpEdiTempData(childProposalNumber);
                                }
                            }
                         // Added for Case 3131 : Proposal Hierarchy - Salary details from child proposals are not printed in parent summary - End
                        //int relLock = txnBean.releasePorposalLocks(parentProposalNumber);                            //int relLock = txnBean.releasePorposalLocks(parentProposalNumber);
                            proposalDevelopmentFormBean = proposalDataTxnBean.getProposalDevelopmentDetails(proposalNumber);
                            
                            //Get proposal Hierarchy Data
                            HashMap hmData = txnBean.getParentProposalData(proposalNumber);
                            String rootProposal = (String)hmData.get("PARENT_PROPOSAL");

                            ProposalHierarchyBean proposalHierarchyBean = null;
                            if(rootProposal != null){
                                proposalHierarchyBean = txnBean.getHierarchyData(rootProposal);
                            }
                            
                            dataObjects = new Vector();
                            dataObjects.addElement(new Integer(syncFlag));
                            dataObjects.addElement(proposalDevelopmentFormBean);
                            dataObjects.addElement(hmData);
                            dataObjects.addElement(proposalHierarchyBean);
                            
                            responder.setDataObjects(dataObjects);
                            responder.setResponseStatus(true);

                        }else{
                            responder.setResponseStatus(false);
                        }
                    }finally{
                        if(isParent){
                            txnBean.releasePorposalLocks(parentProposalNumber , isParent);
                        }else{
                            txnBean.releasePorposalLocks(childPropNo , isParent);
                        }            
                    }
                    
                }
            }else if(functionType==GET_BUDGET_HIERARCHY_DETAILS){
                Vector clientData = requester.getDataObjects();
                HashMap dataObjects = null;
                CoeusVector cvBudgetPeriod = null;
                CoeusVector cvBudgetDetails = null;
                CoeusVector cvDetailCalAmounts = null;
                BudgetInfoBean budgetInfoBean = null;
                BudgetDataTxnBean  budgetDataTxnBean  = null;
                int versionNumber = 0;
                if(clientData != null && clientData.size() > 0){
                    proposalNumber  = (String)clientData.get(0);
                    versionNumber =  ((Integer)clientData.get(1)).intValue();
                    budgetDataTxnBean = new BudgetDataTxnBean();
                    cvBudgetPeriod = budgetDataTxnBean.getBudgetPeriods(proposalNumber,versionNumber);
                    cvBudgetDetails = budgetDataTxnBean.getBudgetDetail(proposalNumber,versionNumber);
                    cvDetailCalAmounts = budgetDataTxnBean.getBudgetDetailCalAmounts(proposalNumber,versionNumber);
                    budgetInfoBean = budgetDataTxnBean.getBudgetForProposal(proposalNumber,versionNumber);
                    // set the values to the client
                    dataObjects = new HashMap();
                    dataObjects.put(edu.mit.coeus.budget.bean.BudgetInfoBean.class,budgetInfoBean);
                    if(cvBudgetPeriod!= null && cvBudgetPeriod.size() > 0){
                        dataObjects.put(edu.mit.coeus.budget.bean.BudgetPeriodBean.class,cvBudgetPeriod);
                    }else{
                        dataObjects.put(edu.mit.coeus.budget.bean.BudgetPeriodBean.class,new CoeusVector());
                    }
                    if(cvBudgetDetails!= null &&cvBudgetDetails.size() >0){
                        dataObjects.put(edu.mit.coeus.budget.bean.BudgetDetailBean.class,cvBudgetDetails);
                    }else{
                        dataObjects.put(edu.mit.coeus.budget.bean.BudgetDetailBean.class,new CoeusVector());
                    }
                    if(cvDetailCalAmounts!= null && cvDetailCalAmounts.size() >0){
                        dataObjects.put(edu.mit.coeus.budget.bean.BudgetDetailCalAmountsBean.class,cvDetailCalAmounts);
                    }else{
                        dataObjects.put(edu.mit.coeus.budget.bean.BudgetDetailCalAmountsBean.class,new CoeusVector());
                    }
                    responder.setDataObject(dataObjects);
                    responder.setResponseStatus(true);
                }else{
                    responder.setResponseStatus(false);
                }

            }/*else if(functionType == CHECK_LOCK_FOR_SYNC_ALL){
                String parentProposalNumner = (String)requester.getDataObject();
                txnBean = new ProposalHierarchyTxnBean(loggedinUser);
                String message = txnBean.checkLockBeforeSyncAllChilds(parentProposalNumner);
                responder.setDataObject(message);
                responder.setResponseStatus(true);
            }*/else if(functionType == CHECK_VIEW_RIGHT){
                CoeusVector cvData = (CoeusVector)requester.getDataObject();
                proposalNumber = (String)cvData.get(0);
                String unitNo = (String)cvData.get(1);
                boolean isRoot = ((Boolean)cvData.get(2)).booleanValue();
                
                if(isRoot){
                    txnBean = new ProposalHierarchyTxnBean(loggedinUser);
                    unitNo = txnBean.getParentPropUnit(proposalNumber);
                }
                
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                proposalDataTxnBean= new ProposalDevelopmentTxnBean();
                boolean hasRight= false;
                
                //Check user has MODIFY_ANY_PROPOSAL right at lead unit
                hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser,"MODIFY_ANY_PROPOSAL",unitNo);
               
                //If not check user has MODIFY_BUDGET right
                if(!hasRight){
                    hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, "MODIFY_BUDGET");
                    
                    //If not present user has VIEW_ANY_PROPOSAL right at lead unit level
                    if(!hasRight) {
                        hasRight=userMaintDataTxnBean.getUserHasRight(loggedinUser,"VIEW_ANY_PROPOSAL",unitNo);
                        
                        //If not check user has VIEW_BUDGET right
                        if(!hasRight) {
                            hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, "VIEW_BUDGET");
                            
                            //IF user has any OSP right, and the proposal status is 
                           if(!hasRight) {                                
                               hasRight =  userMaintDataTxnBean.getUserHasAnyOSPRight(loggedinUser);
                            }//any OSP right
                        }//VIEW_BUDGET right
                    }//VIEW_ANY_PROPOSAL right 
                }//MODIFY_BUDGET right
                
                responder.setDataObject(new Boolean(hasRight));
                responder.setResponseStatus(true);
            }else if(functionType == GET_PROP_HIER_CHILD_TYPE){
                HashMap propHierChildType = new HashMap();
                txnBean = new ProposalHierarchyTxnBean(loggedinUser);
                CoeusVector cvChildType = txnBean.getPropHierChildType();
                propHierChildType.put(ProposalHierarchyChildTypeBean.class, cvChildType);
                responder.setResponseStatus(true);
                responder.setMessage(null);
                responder.setDataObject(propHierChildType);
            }else  if(functionType == SYNC_BUDGET){
                CoeusVector cvData = null;
                CoeusVector cvClientData = (CoeusVector)requester.getDataObject();
                BudgetDataTxnBean budgetDataTxnBean = null;
                if(cvClientData!= null && cvClientData.size() >0){
                    
                    String parentPropNumber = (String)cvClientData.get(0);
                    String childPropNumber = (String)cvClientData.get(1);
                    boolean isParent = ((Boolean)cvClientData.get(2)).booleanValue();
                    BudgetInfoBean budgetInfoBean = (BudgetInfoBean)cvClientData.get(3);
                    //Added for Case Id 3131  - EDI Calculation for to print budget summary details - Start
                    budgetDataTxnBean = new BudgetDataTxnBean();
                    //Added for Case Id 3131 - EDI Calculation for to print budget summary details - End
                    txnBean = new ProposalHierarchyTxnBean(loggedinUser);
                    /*int syncFlag = txnBean.syncBudget(parentPropNumber , childPropNumber , isParent);

                    Hashtable htBudgetData = getBudgetData(budgetInfoBean);
                    cvData = new CoeusVector();
                    cvData.addElement(new Integer(syncFlag));
                    cvData.addElement(htBudgetData);
                    responder.setDataObjects(cvData);
                    responder.setResponseStatus(true);*/
                    int count;
                    if(isParent){
                        proposalNumber = parentPropNumber;
                        count = txnBean.checkBudgetLocks(parentPropNumber , isParent);
                    }else{
                        proposalNumber = childPropNumber;
                        count = txnBean.checkBudgetLocks(childPropNumber , isParent);
                    }
                    try{
                        if(count == 1){
                            proposalDataTxnBean = new ProposalDevelopmentTxnBean();
                             // Added for Case 3131 : Proposal Hierarchy - Salary details from child proposals are not printed in parent summary - Start
                             cvChildPropData =  new CoeusVector();
                             
                             //Added for case: Budget Summary Changes in Proposal Hierarchy -Start 
                            if(!checkPersonRateAndBaseExist(proposalNumber)){ 
                                 cvChildPropData = prepareBudgetEDICalculation(userId, parentPropNumber, childPropNumber, SYNC_BUDGET);
                            }
                             //Added for case: Budget Summary Changes in Proposal Hierarchy - End
                             
                             // Added for Case 3131 : Proposal Hierarchy - Salary details from child proposals are not printed in parent summary - End
                            int syncFlag = txnBean.syncBudget(parentPropNumber);
                             // Added for Case 3131 : Proposal Hierarchy - Salary details from child proposals are not printed in parent summary - Start

                                if (cvChildPropData != null && cvChildPropData.size() > 0){
                                    for(int index = 0; index < cvChildPropData.size(); index++){
                                        String childProposalNumber = (String) cvChildPropData.get(index);
                                        budgetDataTxnBean.cleanUpEdiTempData(childProposalNumber);
                                    }
                                }
                             // Added for Case 3131 : Proposal Hierarchy - Salary details from child proposals are not printed in parent summary - End
                            
                            //int relLock = txnBean.releaseBudgetLocks(proposalNumber , isParent);
                            Hashtable htBudgetData = getBudgetData(budgetInfoBean);

                            //Get proposal Hierarchy Data
                            HashMap hmData = txnBean.getParentProposalData(proposalNumber);
                            String rootProposal = (String)hmData.get("PARENT_PROPOSAL");

                            ProposalHierarchyBean proposalHierarchyBean = null;
                            if(rootProposal != null){
                                proposalHierarchyBean = txnBean.getHierarchyData(rootProposal);
                            }
                            
                            cvData = new CoeusVector();
                            cvData.addElement(new Integer(syncFlag));
                            cvData.addElement(htBudgetData);
                            cvData.addElement(hmData);
                            cvData.addElement(proposalHierarchyBean);
                            
                            responder.setDataObject(cvData);
                            responder.setResponseStatus(true);

                        }else{
                            responder.setResponseStatus(false);
                        }
                    }finally{
                        if(isParent){
                            txnBean.releaseBudgetLocks(parentPropNumber , isParent);
                        }else{
                            txnBean.releaseBudgetLocks(childPropNumber , isParent);
                        }
                    }
                }
            }else if(functionType == REMOVE_PROP_FROM_HIERARCHY){
                CoeusVector cvClientData = (CoeusVector)requester.getDataObject();
                BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
                if(cvClientData!= null && cvClientData.size() >0){
                    String parentPropNumber = (String)cvClientData.get(0);
                    String childPropNumber = (String)cvClientData.get(1);
                    txnBean = new ProposalHierarchyTxnBean(loggedinUser);
                    //Added for case: Budget Summary Changes in Proposal Hierarchy -Start 
                      if(!checkPersonRateAndBaseExist(childPropNumber)){ 
                     // Added for Case 3131 : Proposal Hierarchy - Salary details from child proposals are not printed in parent summary - Start
                        cvChildPropData = prepareBudgetEDICalculation(userId, parentPropNumber, childPropNumber, REMOVE_PROP_FROM_HIERARCHY);
                      }
                     //Added for case: Budget Summary Changes in Proposal Hierarchy -End 
                    txnBean.removePropFromHierarchy(parentPropNumber , childPropNumber);
                    if (cvChildPropData != null && cvChildPropData.size() > 0){
                        for(int index = 0; index < cvChildPropData.size(); index++){
                            String childProposalNumber = (String) cvChildPropData.get(index);
                            budgetDataTxnBean.cleanUpEdiTempData(childProposalNumber);
                        }
                    }
                    // Added for Case 3131 : Proposal Hierarchy - Salary details from child proposals are not printed in parent summary - End
                    //Added for Proposal Hierarchy Enhancement Case# 3183 - starts
                    //To insert the blob datas to the respective tables
                    ProposalHierarchyBean hierarchyBean = txnBean.getHierarchyData(parentPropNumber);
                    if(hierarchyBean != null){
                        CoeusVector cvChildProposals = hierarchyBean.getProposalData();
                        if(cvChildProposals != null && cvChildProposals.size() >0){
                            for(int index = 0; index < cvChildProposals.size(); index++){
                                ProposalBudgetBean proposalBudgetBean = 
                                    (ProposalBudgetBean) cvChildProposals.get(index);
                                txnBean.syncBlobData(parentPropNumber, proposalBudgetBean.getProposalNumber());
                            }
                        }
                    }
                    //Added for Proposal Hierarchy Enhancement Case# 3183 - ends
                    responder.setResponseStatus(true);
                }
            }else if(functionType == REFRESH_HIERARCHY){
                CoeusVector cvData = new CoeusVector();
                txnBean = new ProposalHierarchyTxnBean(loggedinUser);
                
                proposalNumber = (String)requester.getDataObject();
                
                HashMap hmData = txnBean.getParentProposalData(proposalNumber);
                String rootProposal = (String)hmData.get("PARENT_PROPOSAL");
                
                ProposalHierarchyBean proposalHierarchyBean = null;
                
                if(rootProposal != null){
                    proposalHierarchyBean = txnBean.getHierarchyData(rootProposal);
                }
                cvData.add(hmData);
                cvData.add(proposalHierarchyBean);
                responder.setDataObjects(cvData);
                responder.setResponseStatus(true);
            }else if(functionType == IS_BUDGET_COMPLETE){
                 proposalNumber = (String)requester.getDataObject();
                txnBean = new ProposalHierarchyTxnBean();
                boolean value = txnBean.isBudgetComplete(proposalNumber);
                responder.setDataObject(new Boolean(value));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            //Added for case id 3183  - start
            else if(functionType == CAN_LINK_TO_HIERARCHY){
                Vector serverDataObjects = (Vector)requester.getDataObjects();
                proposalNumber = (String)serverDataObjects.get(0);
                String parentProposalNumber = (String)serverDataObjects.get(1);
                
                ProposalNarrativeTxnBean propNarrativeTxnBean = new ProposalNarrativeTxnBean();
                Vector vecNarratives = propNarrativeTxnBean.getProposalNarrativeDetails(proposalNumber);
                boolean canLinkToHierarchy = true;
                ProposalNarrativeFormBean propNarrativeFormBean = null;
                
                if(vecNarratives!=null){
                    for(int i=0; i<vecNarratives.size(); i++){
                        propNarrativeFormBean = (ProposalNarrativeFormBean)vecNarratives.get(i);
                        propNarrativeFormBean.setParentProposal(true);
                        propNarrativeFormBean.setProposalNumber(parentProposalNumber);
                        canLinkToHierarchy = propNarrativeTxnBean.canAddNarrativeType(propNarrativeFormBean);
                        if(!canLinkToHierarchy){
                            break;
                        }
                    }
                }
                serverDataObjects.removeAllElements();
                serverDataObjects.add(new Boolean(canLinkToHierarchy));
                if(!canLinkToHierarchy){
                    String narrativeDescription = "";
                    Vector narrativeTypes = propNarrativeTxnBean.getProposalNarrativeTypes();
                    if(narrativeTypes!=null){
                        for(int i=0;i<narrativeTypes.size();i++){
                            ProposalNarrativeTypeBean narrBean = (ProposalNarrativeTypeBean)narrativeTypes.get(i);
                            if(propNarrativeFormBean.getNarrativeTypeCode() == narrBean.getNarrativeTypeCode()){
                                narrativeDescription = narrBean.getDescription();
                                break;
                            }
                        }
                    }
                    serverDataObjects.add(narrativeDescription);
                }
                responder.setDataObjects(serverDataObjects);
                responder.setResponseStatus(true);
            }
            //Added with COEUSDEV 197,198 - Validation Checks for syncing Proposals
            else if(functionType == HIERARCHY_VALIDATION_CHECKS){
                Vector dataObjects = requester.getDataObjects();
                proposalNumber    = (String)dataObjects.elementAt(0);
                String unitNumber = (String)dataObjects.elementAt(1);
                boolean isParent  = ((Boolean)dataObjects.elementAt(2)).booleanValue();
                HashMap hmRet     = new HashMap();
                int budgetVersion;
                RoutingTxnBean routingTxnBean = new RoutingTxnBean();
                ProposalPrintingTxnBean propPrintTxnBean = new ProposalPrintingTxnBean();
                if(isParent){
                    txnBean = new ProposalHierarchyTxnBean();
                    ProposalHierarchyBean hierarchyBean = txnBean.getHierarchyData(proposalNumber);
                    CoeusVector cvChildData = hierarchyBean.getProposalData();
                    if(cvChildData!=null && !cvChildData.isEmpty()){
                        ProposalBudgetBean proposalBudgetBean = null;
                        for(int i=0; i<cvChildData.size();i++){
                            proposalBudgetBean = (ProposalBudgetBean)cvChildData.get(i);
                            proposalNumber     = proposalBudgetBean.getProposalNumber();
                            unitNumber         = proposalBudgetBean.getUnitNumber();
                            budgetVersion      =  propPrintTxnBean.getVersion(proposalNumber);
                            dataObjects = routingTxnBean.validateForRouting(ModuleConstants.PROPOSAL_DEV_MODULE_CODE, ModuleConstants.PROPOSAL_DEV_BUDGET_SUB_MODULE , proposalNumber,
                                    budgetVersion, 1 ,unitNumber, loggedinUser);
                            if(dataObjects!=null && !dataObjects.isEmpty()){
                                hmRet.put(proposalNumber,dataObjects);
                            }
                        }
                    }
                }else{
                    budgetVersion =  propPrintTxnBean.getVersion(proposalNumber);
                    dataObjects = routingTxnBean.validateForRouting(ModuleConstants.PROPOSAL_DEV_MODULE_CODE, ModuleConstants.PROPOSAL_DEV_BUDGET_SUB_MODULE , proposalNumber,
                            budgetVersion, 1 ,unitNumber, loggedinUser);
                    if(dataObjects!=null && !dataObjects.isEmpty()){
                        hmRet.put(proposalNumber,dataObjects);
                    }
                }
                responder.setDataObject(hmRet);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            //COEUSQA-1689 Role Restrictions for Budget Rates - Start
            else if(functionType == CHECK_MODIFY_PROPOSAL_RATES_RIGHT){
                CoeusVector cvData = (CoeusVector)requester.getDataObject();
                proposalNumber = (String)cvData.get(0);
                String leadUnitNumber = (String)cvData.get(1);                            
                boolean isRoot = ((Boolean)cvData.get(2)).booleanValue();
                
                if(isRoot){
                    txnBean = new ProposalHierarchyTxnBean(loggedinUser);
                    //unitNo = txnBean.getParentPropUnit(proposalNumber);
                }
                
                //To fetch the userInfoBean
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();

                boolean hasRight= false;
              
                //Check user has MODIFY_PROPOSAL_RATES right at proposal
                hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, MODIFY_PROPOSAL_RATES);
                if(!hasRight){
                    //Check user has MODIFY_PROPOSAL_RATES right at unit level
                    hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MODIFY_ANY_PROPOSAL_RATES, leadUnitNumber);
                }
                
                responder.setDataObject(new Boolean(hasRight));
                responder.setResponseStatus(true);
            }
            //COEUSQA-1689 Role Restrictions for Budget Rates - End
            //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start            
            //To check the rights to view the salaries
            else if(functionType == CHECK_VIEW_INSTITUTIONAL_SALARIES_RIGHT){
                CoeusVector cvData = (CoeusVector)requester.getDataObject();
                proposalNumber = (String)cvData.get(0);
                boolean isRoot = ((Boolean)cvData.get(1)).booleanValue();
                String appointmentPersonId = (String)cvData.get(3);
                String unitNo = null;
                Vector vecPersonInfo = new Vector();
                boolean hasRight= false;
                
                if(isRoot){
                    txnBean = new ProposalHierarchyTxnBean(loggedinUser);
                    unitNo = txnBean.getParentPropUnit(proposalNumber);
                }
                //To fetch the userInfoBean
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                //If the userInfoBean is not null then fetch the person details
                if(appointmentPersonId!=null && appointmentPersonId.trim().length()>0){
                    //If the person id is not empty then fetch the person details
                    vecPersonInfo = userMaintDataTxnBean.getPersonInfo(appointmentPersonId);
                }
                //To fetch the last value which is home unit
                int decrementor = 1;
                int lastIndex = vecPersonInfo.size()-decrementor;
                //To fetch the home unit
                if(vecPersonInfo!=null && vecPersonInfo.size()>0){
                    unitNo = (String)vecPersonInfo.get(lastIndex);
                }else{
                    unitNo = EMPTY_STRING;
                }
                //Check user has VIEW_PROP_PERSON_INST_SALARIES right at proposal
                hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser,proposalNumber,VIEW_PROP_PERSON_INST_SALARIES);
                if(!hasRight){
                    //Check user has VIEW_INSTITUTIONAL_SALARIES right at unit level
                    hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser,VIEW_INSTITUTIONAL_SALARIES,unitNo);
                }
                responder.setDataObject(new Boolean(hasRight));
                responder.setResponseStatus(true);
            }
            //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End            
            //COEUSDEV 197,198: End
        } 
        catch( LockingException lockEx ) {
            //lockEx.printStackTrace();
            LockingBean lockingBean = lockEx.getLockingBean();
            String errMsg = lockEx.getErrorMessage();
            CoeusMessageResourcesBean coeusMessageResourcesBean
            =new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            responder.setException(lockEx);
            responder.setResponseStatus(false);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, lockEx, "ProposalHierachyServlet",
            "perform");
        
        }
         //Added for case id 3183  - end
        catch( CoeusException coeusEx ) {
            //coeusEx.printStackTrace();
            int index=0;
            String errMsg;
            errMsg = coeusEx.toString();
            if(coeusEx.getErrorId()==999999){
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
                responder.setLocked(true);
            }else{
                //Code modified for Case#3183 proposal hierarchy.
//                errMsg = coeusEx.toString();
                errMsg = coeusEx.getMessage();
            }
            CoeusMessageResourcesBean coeusMessageResourcesBean
            =new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            responder.setException(coeusEx);
            responder.setResponseStatus(false);
            
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, coeusEx,
            "ProposalHierarchyServlet", "doPost");
                 
try{
             // Added for Case 3131 : Proposal Hierarchy - Salary details from child proposals are not printed in parent summary -Start
            if (cvChildPropData != null && cvChildPropData.size() > 0){
                    BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
                    for(int index1 = 0; index1 < cvChildPropData.size(); index1++){
                        String childPropNumber = (String) cvChildPropData.get(index1);
                        budgetDataTxnBean.cleanUpEdiTempData(childPropNumber);
                    }
                }                 
            }catch(Exception e){
                //e.printStackTrace();
                UtilFactory.log( e.getMessage(), e,
                "ProposalHierarchyServlet", "doPost");
            }
            // Added for Case 3131 : Proposal Hierarchy - Salary details from child proposals are not printed in parent summary -End                
            
        }catch( DBException dbEx ) {
            //dbEx.printStackTrace();
            int index=0;
            String errMsg = dbEx.getUserMessage();
            //String errMsg = dbEx.getMessage();
            if (dbEx.getErrorId() == 20102 ) {
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
            }
            if (errMsg.equals("db_exceptionCode.1111")) {
                responder.setCloseRequired(true);
            }
            CoeusMessageResourcesBean coeusMessageResourcesBean
                = new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);

            responder.setResponseStatus(false);

            //print the error message at client side
            responder.setException(dbEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, dbEx,
                "ProposalHierarchyServlet", "perform");

        }catch(Exception e) {
            //e.printStackTrace();
            //print the error message at server side
            responder.setResponseStatus(false);
            //print the error message at client side
            responder.setException(e);
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e,
                "ProposalHierarchyServlet", "perform");

        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "ProposalHierarchyServlet", "doPost");
        //Case 3193 - END
            
        } finally {
            try{
                outputToApplet
                = new ObjectOutputStream(response.getOutputStream());
                outputToApplet.writeObject(responder);
                // close the streams
                if (inputFromApplet!=null){
                    inputFromApplet.close();
                }
                if (outputToApplet!=null){
                    outputToApplet.flush();
                    outputToApplet.close();
                }
            }catch (IOException ioe){
                UtilFactory.log( ioe.getMessage(), ioe,
                "ProposalHierarchyServlet", "doPost");
            }
        }
    }
    
    
    private Hashtable getBudgetData(BudgetInfoBean budgetInfoBean) throws DBException, CoeusException{
        Hashtable budget = new Hashtable();
        
        String proposalNumber = budgetInfoBean.getProposalNumber();
        int versionNumber = budgetInfoBean.getVersionNumber();        
        String unitNumber = budgetInfoBean.getUnitNumber();
        int activityTypeCode = budgetInfoBean.getActivityTypeCode();
        
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        
        CoeusVector coeusVector = null;
        //Budget Info Bean
        budgetInfoBean = budgetDataTxnBean.getBudgetForProposal(proposalNumber, versionNumber);
        coeusVector = new CoeusVector();
        coeusVector.addElement(budgetInfoBean);
        budget.put(BudgetInfoBean.class,coeusVector);
        //Budget Periods
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getBudgetPeriods(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(BudgetPeriodBean.class,coeusVector);
        //Budget Details
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getBudgetDetail(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }                
        budget.put(BudgetDetailBean.class,coeusVector);
        //Budget Personnel Detail
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getBudgetPersonnelDetail(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }                
        budget.put(BudgetPersonnelDetailsBean.class,coeusVector);
        
        // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
        // Budget formualted cost details
        coeusVector = budgetDataTxnBean.getBudgetFormulatedDetail(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(BudgetFormulatedCostDetailsBean.class,coeusVector);
        // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
        
        //Budget Budget Detail Cal Amounts
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getBudgetDetailCalAmounts(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }                
        budget.put(BudgetDetailCalAmountsBean.class,coeusVector);
        //Budget Budget Personnel Detail Cal Amounts
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getBudgetPersonnelCalAmounts(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }                
        budget.put(BudgetPersonnelCalAmountsBean.class,coeusVector);
        //Budget Budget Persons
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getBudgetPersons(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }                
        budget.put(BudgetPersonsBean.class,coeusVector);
        
        //Budget Proposal Institute Rates
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getProposalInstituteRates(proposalNumber, versionNumber);
        if(coeusVector==null){
            if(versionNumber!=1){
                coeusVector = budgetDataTxnBean.getProposalInstituteRates(proposalNumber, versionNumber-1);
            }
        }
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(ProposalRatesBean.class,coeusVector);

        //Budget Proposal Institute LA Rates
        coeusVector = new CoeusVector();        
        coeusVector = budgetDataTxnBean.getProposalInstituteLARates(proposalNumber, versionNumber);
        if(coeusVector==null){
            if(versionNumber!=1){
                coeusVector = budgetDataTxnBean.getProposalInstituteLARates(proposalNumber, versionNumber-1);
            }
        }
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }

        budget.put(ProposalLARatesBean.class,coeusVector);
        
        //Budget Justification
        coeusVector = new CoeusVector();
        BudgetJustificationBean budgetJustificationBean = budgetDataTxnBean.getBudgetJustification(proposalNumber, versionNumber);
        if(budgetJustificationBean!=null){
            coeusVector.addElement(budgetJustificationBean);    
        }                
        budget.put(BudgetJustificationBean.class,coeusVector);
        //Budget Institute Rate
        /*
         * Updated by Geo on 16-Sep-2004
         *  To pass the top level unit to get the institute rates
         */
        RatesTxnBean ratesTxnBean = new RatesTxnBean();
        String topLevelUnitNum = ratesTxnBean.getTopLevelUnit(unitNumber);
        //System.out.println("Top Level Unit=>"+topLevelUnitNum);
        coeusVector = new CoeusVector();                                
//        coeusVector = budgetDataTxnBean.getInstituteRates(activityTypeCode);
        coeusVector = budgetDataTxnBean.getInstituteRates(activityTypeCode, topLevelUnitNum);
        //System.out.println("Institute rates=>"+coeusVector.toString());
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }                                
        budget.put(InstituteRatesBean.class,coeusVector);
        //Budget Institute LA Rate
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getInstituteLARates(unitNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }                
        budget.put(InstituteLARatesBean.class,coeusVector);
        //Budget Category List
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getBudgetCategoryList();
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }                
        budget.put(BudgetCategoryBean.class, coeusVector);
        //Rate Class List
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getOHRateClassList();
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }                
        budget.put(RateClassBean.class, coeusVector);
        //Valid Calc Types for E and V
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getValidCalcTypesForEV();
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(ValidCalcTypesBean.class, coeusVector);
        
        //Get Proposal Data for Budget
        coeusVector = new CoeusVector();
        ProposalDevelopmentFormBean proposalDevelopmentFormBean = budgetDataTxnBean.getProposalDetailsForBudget(proposalNumber);
        coeusVector.addElement(proposalDevelopmentFormBean);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(ProposalDevelopmentFormBean.class, coeusVector);     
        
        //Get Proposal Cost Sharing 
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getProposalCostSharing(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(ProposalCostSharingBean.class, coeusVector);             
        
        //Get Proposal IDC Rates
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getProposalIDCRate(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(ProposalIDCRateBean.class, coeusVector);
        
        //Get Project Income Details
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getProjectIncomeDetails(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(ProjectIncomeBean.class, coeusVector);
        
        //Get Budget Modular Details
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getBudgetModularData(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(BudgetModularBean.class, coeusVector);
        //Code added for case#2938 - Proposal Hierarchy enhancement - starts
        //To get the budget modular IDC data.
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getBudgetModularIDCData(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(BudgetModularIDCBean.class, coeusVector);
        //Code added for case#2938 - Proposal Hierarchy enhancement - ends
        
        //Budget Summary Parameter for generating Report in AWT or PDF
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        String budgetSummaryDisplayOption = coeusFunctions.getParameterValue(CoeusConstants.BUDGET_SUMMARY_DISPLAY_OPTION);        
        budget.put(CoeusConstants.BUDGET_SUMMARY_DISPLAY_OPTION, budgetSummaryDisplayOption==null ? null : new Integer(budgetSummaryDisplayOption));
        
        // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - start
        String enableFormualtedCost = coeusFunctions.getParameterValue(CoeusConstants.ENABLE_FORMULATED_COST_CALC);
        budget.put(CoeusConstants.ENABLE_FORMULATED_COST_CALC, enableFormualtedCost==null ? null : new Integer(enableFormualtedCost));
        
        String formulatedCostElements = coeusFunctions.getParameterValue(CoeusConstants.FORMULATED_COST_ELEMENTS);
        if(formulatedCostElements != null){
            formulatedCostElements = formulatedCostElements.trim();
            budget.put(CoeusConstants.FORMULATED_COST_ELEMENTS, formulatedCostElements);
        }else{
            budget.put(CoeusConstants.FORMULATED_COST_ELEMENTS, "");
        }
        
        CoeusVector cvFormualetdTypes = budgetDataTxnBean.getFormulatedTypes();
        if(cvFormualetdTypes != null && !cvFormualetdTypes.isEmpty()){
            budget.put(CoeusConstants.FORMULATED_TYPES, cvFormualetdTypes);
        }
        
        // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
        
        return budget;
    }//End of getBudgetData
    
 // Added for Case 3131  Proposal Hierarchy - Salary details from child proposals are not printed in parent summary -Start
/** The following method has been written to calculate Budget EDI
 * @param proposal number and user Id as input
 *  @exception DBException if any error during database transaction.
 *  @exception CoeusException if the instance of dbEngine is not available.
 * @return void
 */
private void calculateBudgetEDI(String userId , String proposalNumber)  throws DBException, CoeusException{
    
    BudgetUpdateTxnBean budgetUpdateTxnBean = new BudgetUpdateTxnBean(userId);
    BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
    BudgetPeriodBean budgetPeriodBean = new BudgetPeriodBean();
    BudgetInfoBean budgetInfoBean = new BudgetInfoBean();
    
    ProposalIDCRateBean proposalIDCRateBean;
    boolean success = false;
    int versionNumber = 0;
    String unitNumber ="";
    int activityTypeCode = 0;
    
    Hashtable budget = new Hashtable();
    CoeusVector coeusVector = null;
    CoeusVector cvBudgetForProp = new CoeusVector();
    boolean finalVersionFlag = false;
    //Budget Info Bean
    cvBudgetForProp =  budgetDataTxnBean.getBudgetForProposal(proposalNumber);
    if(cvBudgetForProp !=null && cvBudgetForProp.size() > 0){
        for(int index =0; index < cvBudgetForProp.size(); index++){
            budgetInfoBean = (BudgetInfoBean)cvBudgetForProp.get(index);
            if (budgetInfoBean.isFinalVersion()){
                versionNumber = budgetInfoBean.getVersionNumber();
                finalVersionFlag = true;
                break;
            }
        }
        if(!finalVersionFlag){
            versionNumber = cvBudgetForProp.size();
        }
    }
    QueryEngine queryEngine = QueryEngine.getInstance();
    String key = proposalNumber + versionNumber;
    
    budgetInfoBean = budgetDataTxnBean.getBudgetForProposal(proposalNumber, versionNumber);
    coeusVector = new CoeusVector();
    coeusVector.addElement(budgetInfoBean);
    budget.put(BudgetInfoBean.class,coeusVector);
    //Budget Periods
    coeusVector = new CoeusVector();
    coeusVector = budgetDataTxnBean.getBudgetPeriods(proposalNumber, versionNumber);
    if(coeusVector==null){
        coeusVector = new CoeusVector();
    }
    budget.put(BudgetPeriodBean.class,coeusVector);
    //Budget Details
    coeusVector = new CoeusVector();
    coeusVector = budgetDataTxnBean.getBudgetDetail(proposalNumber, versionNumber);
    if(coeusVector==null){
        coeusVector = new CoeusVector();
    }
    budget.put(BudgetDetailBean.class,coeusVector);
    //Budget Personnel Detail
    coeusVector = new CoeusVector();
    coeusVector = budgetDataTxnBean.getBudgetPersonnelDetail(proposalNumber, versionNumber);
    if(coeusVector==null){
        coeusVector = new CoeusVector();
    }
    budget.put(BudgetPersonnelDetailsBean.class,coeusVector);
    
    // Budget formualted cost details
    coeusVector = budgetDataTxnBean.getBudgetFormulatedDetail(proposalNumber, versionNumber);
    if(coeusVector==null){
        coeusVector = new CoeusVector();
    }
    budget.put(BudgetFormulatedCostDetailsBean.class,coeusVector);

    //Budget Budget Detail Cal Amounts
    coeusVector = new CoeusVector();
    coeusVector = budgetDataTxnBean.getBudgetDetailCalAmounts(proposalNumber, versionNumber);
    if(coeusVector==null){
        coeusVector = new CoeusVector();
    }
    budget.put(BudgetDetailCalAmountsBean.class,coeusVector);
    //Budget Budget Personnel Detail Cal Amounts
    coeusVector = new CoeusVector();
    coeusVector = budgetDataTxnBean.getBudgetPersonnelCalAmounts(proposalNumber, versionNumber);
    if(coeusVector==null){
        coeusVector = new CoeusVector();
    }
    budget.put(BudgetPersonnelCalAmountsBean.class,coeusVector);
    
    //Budget Budget Persons
    coeusVector = new CoeusVector();
    coeusVector = budgetDataTxnBean.getBudgetPersons(proposalNumber, versionNumber);
    if(coeusVector==null){
        coeusVector = new CoeusVector();
    }
    budget.put(BudgetPersonsBean.class,coeusVector);
    
    //Budget Proposal Institute Rates
    coeusVector = new CoeusVector();
    coeusVector = budgetDataTxnBean.getProposalInstituteRates(proposalNumber, versionNumber);
    if(coeusVector==null){
        if(versionNumber!=1){
            coeusVector = budgetDataTxnBean.getProposalInstituteRates(proposalNumber, versionNumber-1);
        }
    }
    if(coeusVector==null){
        coeusVector = new CoeusVector();
    }
    budget.put(ProposalRatesBean.class,coeusVector);
    
    //Budget Proposal Institute LA Rates
    coeusVector = new CoeusVector();
    coeusVector = budgetDataTxnBean.getProposalInstituteLARates(proposalNumber, versionNumber);
    if(coeusVector==null){
        if(versionNumber!=1){
            coeusVector = budgetDataTxnBean.getProposalInstituteLARates(proposalNumber, versionNumber-1);
        }
    }
    if(coeusVector==null){
        coeusVector = new CoeusVector();
    }
    
    budget.put(ProposalLARatesBean.class,coeusVector);
    
    //Budget Justification
    coeusVector = new CoeusVector();
    BudgetJustificationBean budgetJustificationBean = budgetDataTxnBean.getBudgetJustification(proposalNumber, versionNumber);
    if(budgetJustificationBean!=null){
        coeusVector.addElement(budgetJustificationBean);
    }
    budget.put(BudgetJustificationBean.class,coeusVector);
    //Get Proposal Data for Budget
    coeusVector = new CoeusVector();
    ProposalDevelopmentFormBean proposalDevelopmentFormBean = budgetDataTxnBean.getProposalDetailsForBudget(proposalNumber);
    coeusVector.addElement(proposalDevelopmentFormBean);
    if(coeusVector==null){
        coeusVector = new CoeusVector();
    }
    budget.put(ProposalDevelopmentFormBean.class, coeusVector);
    
    queryEngine.addDataCollection(key, budget);
    
    CoeusVector cvBudgetPeriod = budgetDataTxnBean.getBudgetPeriods(proposalNumber,versionNumber);
    if (cvBudgetPeriod !=null && cvBudgetPeriod.size() >0){
        budgetPeriodBean = (BudgetPeriodBean) cvBudgetPeriod.get(0);
        budgetUpdateTxnBean.addBudgetEDIForPropHierarchy(budgetPeriodBean);
        
    }
}
/** The following method has been written to prepare the Budget EDI Calculation
 * @param child proposal number, Parent proposal number , function type and user Id as input
 *  @exception DBException if any error during database transaction.
 *  @exception CoeusException if the instance of dbEngine is not available.
 * @return void
 */
private CoeusVector prepareBudgetEDICalculation(String userId , String parentproposalNumber, String childProposalNumber , char functionType)throws DBException, CoeusException{
    ProposalHierarchyTxnBean txnBean = new ProposalHierarchyTxnBean();
    //Get the Parent Proposal Number for the child proposal
    HashMap hierarchydata = txnBean.getParentProposalData(childProposalNumber);
    boolean isparentProposal = false;
    String parentProposalNumber = "";
    boolean inHierarchy = false;
    CoeusVector cvChildProposalData = new CoeusVector();
    if( hierarchydata !=null && hierarchydata.size() > 0){
        isparentProposal = ((Boolean) hierarchydata.get("IS_PARENT")).booleanValue();
        parentProposalNumber = (String) hierarchydata.get("PARENT_PROPOSAL");
        inHierarchy = ((Boolean) hierarchydata.get("IN_HIERARCHY")).booleanValue();
        // Get all the child proposal for Parent Proposal
        if(parentProposalNumber !=null){
            edu.mit.coeus.propdev.bean.ProposalHierarchyBean proposalHierarchyBean =
            txnBean.getHierarchyData(parentproposalNumber);
            if(proposalHierarchyBean != null){
                CoeusVector cvChildProposals = proposalHierarchyBean.getProposalData();
                if(cvChildProposals != null && cvChildProposals.size() >0){
                    for(int index = 0; index < cvChildProposals.size(); index++){
                        ProposalBudgetBean proposalBudgetBean =
                        (ProposalBudgetBean) cvChildProposals.get(index);
                        String proposalNumber = proposalBudgetBean.getProposalNumber();
                        if(functionType == REMOVE_PROP_FROM_HIERARCHY){
                            if(proposalNumber.equals(childProposalNumber)){
                                continue;
                            }
                        }
                        cvChildProposalData.add(proposalNumber);
                        calculateBudgetEDI(userId, proposalNumber );
                    }
                    
                }
            }
            
        }
        // When Create Hierarchy option the new child/source proposal does not have parent,  Calculate EDI for the Source/Child Proposal
        if(!inHierarchy){
            calculateBudgetEDI(userId, childProposalNumber );
            cvChildProposalData.add(childProposalNumber);
        }
    }
    return cvChildProposalData;
    
}
// Added for Case 3131  Proposal Hierarchy - Salary details from child proposals are not printed in parent summary -End

//Added for case: Budget Summary Changes in Proposal Hierarchy -Start
private boolean checkPersonRateAndBaseExist(String proposalNumber)throws DBException, CoeusException {
    BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
    boolean isPerRateAndBaseExist = false;
    boolean finalVersionFlag = false;
    int versionNumber = 1 ;
    CoeusVector cvBudgetForProp = new CoeusVector();
    //Budget Info Bean
    cvBudgetForProp =  budgetDataTxnBean.getBudgetForProposal(proposalNumber);
    if(cvBudgetForProp !=null && cvBudgetForProp.size() > 0){
        for(int index =0; index < cvBudgetForProp.size(); index++){
            BudgetInfoBean budgetInfoBean = (BudgetInfoBean)cvBudgetForProp.get(index);
            if (budgetInfoBean.isFinalVersion()){
                versionNumber = budgetInfoBean.getVersionNumber();
                finalVersionFlag = true;
                break;
            }
        }
        if(!finalVersionFlag){
            versionNumber = cvBudgetForProp.size();
        }
    }
    isPerRateAndBaseExist = budgetDataTxnBean.checkPerRateAndBaseExists(proposalNumber,versionNumber);
    return isPerRateAndBaseExist;
}
//Added for case: Budget Summary Changes in Proposal Hierarchy -End

}
