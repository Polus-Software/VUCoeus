/*
 * AwardBudgetMaintainanceServlet.java
 *
 * Created on July 14, 2005, 1:22 PM
 */

/*
 * PMD check performed, and commented unused imports and variables on 24-AUG-2011
 * by Bharati
 */

package edu.mit.coeus.servlet;

import edu.mit.coeus.award.bean.AwardBudgetBean;
import edu.mit.coeus.award.bean.AwardBudgetCopyBean;
import edu.mit.coeus.award.bean.AwardBudgetDataTxnBean;
import edu.mit.coeus.award.bean.AwardBudgetDetailBean;
import edu.mit.coeus.award.bean.AwardBudgetHeaderBean;
import edu.mit.coeus.award.bean.AwardBudgetRatesBean;
import edu.mit.coeus.award.bean.AwardBudgetSummaryBean;
import edu.mit.coeus.award.bean.AwardBudgetUpdateTxnBean;
import edu.mit.coeus.award.bean.AwardTxnBean;
import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.budget.bean.BudgetDetailBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.budget.bean.CostElementsBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;

//import edu.mit.coeus.utils.locking.LockingException;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
//import edu.mit.coeus.utils.query.NotEquals;
//import edu.mit.coeus.utils.query.Or;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author  chandrashekara
 */
public class AwardBudgetMaintainanceServlet extends CoeusBaseServlet implements TypeConstants{
    private static final char GET_AWARD_BUDGET_SUMMARY = 'A';
    private static final char GET_AWARD_BUDGET_COPY = 'B';
    //private static final char CHECK_AWARD_BUDGET_RIGHT = 'C';
    private static final char GET_AWARD_BUDGET_RIGHTS = 'C';
    private static final char COPY_AWARD_BUDGET ='D';
    private static final char GET_AWARD_BUDGET_DATA = 'E';
    private static final char SAVE_AWARD_BUDGET = 'F';
    private static final char GET_AWARD_BUDGET_CE_DETAILS = 'G';
    private static final char SAVE_SUMMARY_DATA = 'H';
    private static final char CALCULATE_COST_ELEMENT = 'I';
    private static final char GET_PARAMETER_FOR_BUDGET = 'J';
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
    private static final char GET_INACTIVE_COST_ELEMENT_DETAILS = 'k';
    //COEUSQA-3273 - end
    private static final char GET_PARAMETER_VALUE='r';
    //COEUSQA-3937
    private static final char CHECK_UPD_SEQ_IN_BUD_INFO = '1';
    //COEUSQA-3937
    
    /** Creates a new instance of AwardBudgetMaintainanceServlet */
    public AwardBudgetMaintainanceServlet() {
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
        
        String loggedinUser ="";
        String loggedUnitNumber = "";
        String userId = "";
        AwardBudgetDataTxnBean awardBudgetDataTxnBean = null;
        
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
            
            if(functionType==GET_AWARD_BUDGET_SUMMARY){
                CoeusVector cvSummaryData = new CoeusVector();
                awardBudgetDataTxnBean = new AwardBudgetDataTxnBean();
                Hashtable formData = new Hashtable();
                
                String awardNumber = requester.getId();
                
                //Hashtable rightsData = (Hashtable)requester.getDataObject();
                /** Get the Award Summary Data required to populate the data for
                 *AwardSummaryBean
                 */
                cvSummaryData =  awardBudgetDataTxnBean.getAwardBudgetSummary(awardNumber);
                
                /** Pass the OSP level rights to the DB and get the status of the
                 *rights available for the user
                 */
//                Hashtable ospRightData = checkRight((Hashtable)rightsData.get(
//                KeyConstants.AWARD_BUDGET_OSP_LEVEL_RIGHT),null,loggedinUser);
                
                /** Pass the Unit Level rights to the DB and get the right status
                 *for the unit number
                 */
//                Hashtable unitRightData = checkRight((Hashtable)rightsData.get(
//                KeyConstants.AWARD_BUDGET_UNIT_LEVEL_RIGHT),loggedUnitNumber,loggedinUser);
                
                
                if(cvSummaryData!= null && cvSummaryData.size() >0){
                    formData.put(KeyConstants.AWARD_BUDGET_SUMMARY_DATA,cvSummaryData);
                }
                
//                formData.put(KeyConstants.AWARD_BUDGET_OSP_LEVEL_RIGHT,ospRightData);
//                formData.put(KeyConstants.AWARD_BUDGET_UNIT_LEVEL_RIGHT,unitRightData);
                
                responder.setDataObject(formData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType==GET_AWARD_BUDGET_COPY){
                CoeusVector cvCopyData = new CoeusVector();
                awardBudgetDataTxnBean = new AwardBudgetDataTxnBean();
                String awardNumber = requester.getId();
                cvCopyData =  awardBudgetDataTxnBean.getAwardBudgetCopyData(awardNumber);
                /*Commented based on users requirement for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets
                //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
                CoeusVector cvBudgetDetails = new CoeusVector();
                Vector vecCostElements = new Vector();
                BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
                BudgetDetailBean budgetDetailBean = new BudgetDetailBean();
                Vector inActiveCE = new Vector();
                if(cvCopyData!=null && cvCopyData.size() > 0){
                    for(int index=0;index<cvCopyData.size();index++){
                        AwardBudgetCopyBean awardBudgetCopyBean = (AwardBudgetCopyBean)cvCopyData.get(index);
                        int versionNumber = awardBudgetCopyBean.getVersionNumber();
                        String proposalNumber = awardBudgetCopyBean.getProposalNumber();
                        int period = awardBudgetCopyBean.getBudgetPeriod();
                        //get the budget details for the proposal
                        cvBudgetDetails = budgetDataTxnBean.getBudgetDetail(proposalNumber,versionNumber);
                        if(cvBudgetDetails!=null && cvBudgetDetails.size()>0){
                            for (int count = 0; count < cvBudgetDetails.size(); count ++ ){
                                budgetDetailBean = (BudgetDetailBean) cvBudgetDetails.get(count);
                                int budgetPeriod = budgetDetailBean.getBudgetPeriod();
                                if(period == budgetPeriod){
                                    vecCostElements.add(budgetDetailBean.getCostElement());
                                }
                            }
                            
                            //remove duplicate cost elements from the vector vecCostElements
                            for(int costElementindex=0; costElementindex<vecCostElements.size(); costElementindex++) {
                                //elementIndex Returns the index of the last occurrence of the specified object from the vector vecCostElements.
                                int elementIndex = vecCostElements.lastIndexOf(vecCostElements.get(costElementindex));
                                //if both elementIndex and costElementIndex holding the same value then remove the costelement form the vector.
                                if(elementIndex != costElementindex) {
                                    vecCostElements.remove(elementIndex);
                                    costElementindex=costElementindex-1;
                                }
                            }
                            //If status of cost element is 'N' then add to vector inActivecostElements
                            if(vecCostElements!=null && vecCostElements.size()>0){
                                for(int costElementindex = 0 ; costElementindex <vecCostElements.size() ; costElementindex++){
                                    String costElement = (String) vecCostElements.get(costElementindex);
                                    CostElementsBean costElementsBean = budgetDataTxnBean.getCostElementsDetails(costElement);
                                    if("N".equals(costElementsBean.getActive())){
                                        inActiveCE.addElement(costElementsBean.getActive());
                                        inActiveCE.addElement(costElement);
                                    }
                                }
                                vecCostElements.clear();
                            }
                            //if status of cost element is inactive then remove from cvCopyData
                            if(inActiveCE!=null && inActiveCE.size()>0){
                                cvCopyData.remove(index--);
                                inActiveCE.clear();
                            }
                        }
                    }
                } 
                //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end*/
                responder.setDataObjects(cvCopyData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType == GET_AWARD_BUDGET_RIGHTS){
                CoeusVector cvSummaryData = new CoeusVector();
                awardBudgetDataTxnBean = new AwardBudgetDataTxnBean();
                Hashtable formData = new Hashtable();
                
                String awardNumber = requester.getId();
                Hashtable rightsData = (Hashtable)requester.getDataObject();
                /** Pass the OSP level rights to the DB and get the status of the
                 *rights available for the user
                 */
                Hashtable ospRightData = checkRight((Hashtable)rightsData.get(
                KeyConstants.AWARD_BUDGET_OSP_LEVEL_RIGHT),null,loggedinUser);
                
                /** Pass the Unit Level rights to the DB and get the right status
                 *for the unit number
                 */
                /*
                 *BUG FIX by Geo on 03-Oct-2005
                 *unit number should be lead unit of award not home unit number of logged in user
                 */
//                Hashtable unitRightData = checkRight((Hashtable)rightsData.get(
//                KeyConstants.AWARD_BUDGET_UNIT_LEVEL_RIGHT),loggedUnitNumber,loggedinUser);
                String leadUnit = (String)rightsData.get("AWARD_BUDGET_LEAD_UNIT");
                Hashtable unitRightData = checkRight((Hashtable)rightsData.get(
                KeyConstants.AWARD_BUDGET_UNIT_LEVEL_RIGHT),leadUnit,loggedinUser);
                
                /*
                 *  End FIX
                 */
                
                formData.put(KeyConstants.AWARD_BUDGET_OSP_LEVEL_RIGHT,ospRightData);
                formData.put(KeyConstants.AWARD_BUDGET_UNIT_LEVEL_RIGHT,unitRightData);
                
                responder.setDataObject(formData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType == COPY_AWARD_BUDGET ){
                CoeusVector cvData = (CoeusVector)requester.getDataObject();
                
                CoeusVector cvCopyData = prepareBeanForCopy(cvData);
                        
                AwardBudgetUpdateTxnBean awardBudgetUpdateTxnBean =
                        new AwardBudgetUpdateTxnBean(loggedinUser);
                
                int newVersionNo =
                    awardBudgetUpdateTxnBean.copyAwardBudget(cvCopyData);
                responder.setDataObject(new Integer(newVersionNo));
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }
            //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
            else if(functionType == GET_INACTIVE_COST_ELEMENT_DETAILS){
                CoeusVector cvCopyData = new CoeusVector();
                awardBudgetDataTxnBean = new AwardBudgetDataTxnBean();
                String awardNumber = requester.getId();
                AwardTxnBean awardTxnBean= new AwardTxnBean();
                boolean isInacTiveCEPresent = awardTxnBean.isAwardBudgetHasInactiveCE(awardNumber);
                responder.setDataObject(new Boolean(isInacTiveCEPresent));
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }//Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end       
        else if(functionType == GET_AWARD_BUDGET_DATA){
                CoeusVector cvData = new CoeusVector();
                awardBudgetDataTxnBean = new AwardBudgetDataTxnBean ();
                
                CoeusVector cvAwardBudData = (CoeusVector)requester.getDataObject();
                
                AwardBudgetSummaryBean awardBudgetSummaryBean
                        = (AwardBudgetSummaryBean)cvAwardBudData.get(0);
                
                char mode = ((Character)cvAwardBudData.get(1)).charValue();
               
                //Gets the award header data
                AwardBudgetHeaderBean awardBudgetHeaderBean = 
                    awardBudgetDataTxnBean.getAwardBudgetHeader(awardBudgetSummaryBean);
                cvData.add(awardBudgetHeaderBean);
                
                
                if(mode == TypeConstants.NEW_MODE){
                    if(awardBudgetHeaderBean.getVersionNo() > 0){
                        //Since awardBudgetSummaryBean got from the client for new mode
                        //has version no as 0, set the version no got from db.
                        awardBudgetSummaryBean.setBudgetVersion(awardBudgetHeaderBean.getVersionNo());

                        //awardBudgetDataTxnBean.getAwardBudgetDetails(awardBudgetSummaryBean, mode);
                        //cvData.add(new CoeusVector());

                        //Gets the award details data
                        CoeusVector cvAwardDetails = 
                        awardBudgetDataTxnBean.getAwardBudgetDetails(awardBudgetSummaryBean, mode);
                        cvData.add(cvAwardDetails);
                    }
                }else{
                
                    //Gets the award details data
                    CoeusVector cvAwardDetails = 
                    awardBudgetDataTxnBean.getAwardBudgetDetails(awardBudgetSummaryBean, mode);
                    cvData.add(cvAwardDetails);
                }
                
                //Gets the Award Budget Type data for the Dropdown
                CoeusVector cvAwdBudgetType = awardBudgetDataTxnBean.getAwardBudgetType();
                cvData.add(cvAwdBudgetType);
                
                //Gets the Award Budget CE's For Calulation from Code Table
                CoeusVector cvCalCE = awardBudgetDataTxnBean.getAwardBudgetCalCE();
                cvData.add(cvCalCE);
                
                //Gets the data for the CE lookup window
                CoeusVector cvCEList = awardBudgetDataTxnBean.getAwardBudgetCEList();
                cvData.add(cvCEList);
                
                //Gets the award budget status 
                CoeusVector cvBudgetStatus = awardBudgetDataTxnBean.getAwardBudgetStatus();
                cvData.add(cvBudgetStatus);
                
                //Gets the Oh Rates Class list for the Rate Dropdown
                CoeusVector cvOHRates = awardBudgetDataTxnBean.getOHRateClassList();
                cvData.add(cvOHRates);
                
                //Gets the AWARD_BUDGET_CAMPUS_BASED_ON_CE Parameter
                Integer awdBudCampusBasedOnCE = new Integer(
                            awardBudgetDataTxnBean.getParameterValue("AWARD_BUDGET_CAMPUS_BASED_ON_CE"));
                cvData.add(awdBudCampusBasedOnCE);
                
                //Gets the AWARD_BUDGET_POST_ENABLED Parameter
                String postEnabled = awardBudgetDataTxnBean.getParameterValue("AWARD_BUDGET_POST_ENABLED");
                cvData.add(postEnabled);
                
                responder.setDataObject(cvData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType == SAVE_AWARD_BUDGET){
                CoeusVector cvData = null;
                AwardBudgetUpdateTxnBean awardBudgetUpdateTxnBean = 
                    new AwardBudgetUpdateTxnBean(loggedinUser);
                
                CoeusVector cvAwdBudData  = (CoeusVector)requester.getDataObject();
                boolean success = awardBudgetUpdateTxnBean.addUpdAwardBudget(cvAwdBudData);
                
                //Send the master data if the save is Sucess
                if(success){
                    awardBudgetDataTxnBean = new AwardBudgetDataTxnBean ();
                    cvData = new CoeusVector();
                    
                    /*AwardBudgetSummaryBean awardBudgetSummaryBean = 
                                        (AwardBudgetSummaryBean)cvAwdBudData.get(2);*/
                    AwardBudgetSummaryBean awardBudgetSummaryBean = new AwardBudgetSummaryBean();
                    
                    AwardBudgetHeaderBean awardBudgetHdrBean = 
                                        (AwardBudgetHeaderBean)cvAwdBudData.get(0);
                    
                    awardBudgetSummaryBean.setMitAwardNumber(awardBudgetHdrBean.getMitAwardNumber());
                    awardBudgetSummaryBean.setSequenceNumber(awardBudgetHdrBean.getSequenceNumber());
                    awardBudgetSummaryBean.setAmountSequenceNumber(awardBudgetHdrBean.getAmountSequenceNo());
                    awardBudgetSummaryBean.setBudgetVersion(awardBudgetHdrBean.getVersionNo());
                   
                    //Gets the award header data
                    AwardBudgetHeaderBean awardBudgetHeaderBean = 
                        awardBudgetDataTxnBean.getAwardBudgetHeader(awardBudgetSummaryBean);
                    cvData.add(awardBudgetHeaderBean);

                    //Gets the award details data
                    CoeusVector cvAwardDetails = 
                    awardBudgetDataTxnBean.getAwardBudgetDetails(awardBudgetSummaryBean, TypeConstants.MODIFY_MODE);
                    cvData.add(cvAwardDetails);
                }
                
                responder.setDataObject(cvData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType == GET_AWARD_BUDGET_CE_DETAILS){
                String costElement = (String)requester.getDataObject();
                awardBudgetDataTxnBean = new AwardBudgetDataTxnBean ();
                CostElementsBean costElementsBean = 
                            awardBudgetDataTxnBean.getAwardBudgetCEDetails(costElement);
                responder.setDataObject(costElementsBean);
                responder.setMessage(null);
                responder.setResponseStatus(true);
                
            }else if(functionType == SAVE_SUMMARY_DATA){
                AwardBudgetSummaryBean awardBudgetSummaryBean 
                            = (AwardBudgetSummaryBean)requester.getDataObject();
                
                AwardBudgetUpdateTxnBean awardBudgetUpdateTxnBean = 
                                    new AwardBudgetUpdateTxnBean(loggedinUser);
                
                boolean sucess = 
                        awardBudgetUpdateTxnBean.updDeleteSummaryData(awardBudgetSummaryBean);
                
                responder.setMessage(null);
                responder.setResponseStatus(sucess);
            }else if(functionType == CALCULATE_COST_ELEMENT){
                CoeusVector cvCalcData = (CoeusVector)requester.getDataObject();
                AwardBudgetHeaderBean awardBudgetHeaderBean = 
                                        (AwardBudgetHeaderBean)cvCalcData.get(0);
                CoeusVector cvCostElements = (CoeusVector)cvCalcData.get(1);
                //CoeusVector cvCodeTableCE = (CoeusVector)cvCalcData.get(1);
                
                awardBudgetDataTxnBean = new AwardBudgetDataTxnBean ();
                CoeusVector cvCaculatedData  = awardBudgetDataTxnBean.getRates(cvCalcData);
                
                //claculateRatesForCE(cvCaculatedData , cvCostElements, cvCodeTableCE);
                CoeusVector cvChangeTotals = claculateRatesForCE(
                            cvCaculatedData , cvCostElements, awardBudgetHeaderBean);
                responder.setDataObject(cvChangeTotals);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType == GET_PARAMETER_FOR_BUDGET){
                awardBudgetDataTxnBean = new AwardBudgetDataTxnBean ();
                
                Integer mitAwardBudget = new Integer(
                        awardBudgetDataTxnBean.getParameterValue("MIT_AWARD_BUDGET"));
                responder.setDataObject(mitAwardBudget);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }
            //checking parameter for raft

         else if(functionType == GET_PARAMETER_VALUE){
                awardBudgetDataTxnBean = new AwardBudgetDataTxnBean ();                
                Integer raftAwardBudget = new Integer(
                        awardBudgetDataTxnBean.getParameterValue("ENABLE_RAFT_AWARD_BUDGET"));
                if(raftAwardBudget==1)
                responder.setParameterValue(true);
                else
                responder.setParameterValue(false);

                responder.setResponseStatus(true);
                responder.setMessage(null);                
                
            }
         //COEUSQA-3937
        else if(functionType == CHECK_UPD_SEQ_IN_BUD_INFO){           
                awardBudgetDataTxnBean = new AwardBudgetDataTxnBean();                
                String awardNumber = requester.getId();
                awardBudgetDataTxnBean = new AwardBudgetDataTxnBean ();                
                Integer raftAwardBudget = new Integer(
                        awardBudgetDataTxnBean.UpdSeqInBugInfo(awardNumber));
                if(raftAwardBudget==1){
                responder.setParameterValue(true);
                }else{
                responder.setParameterValue(false);
                }
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }   
         //COEUSQA-3937   

        }catch( CoeusException coeusEx ) {
            //coeusEx.printStackTrace();
            int index=0;
            String errMsg;
            if(coeusEx.getErrorId()==999999){
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
                responder.setLocked(true);
            }else{
                errMsg = coeusEx.toString();
            }
            CoeusMessageResourcesBean coeusMessageResourcesBean
            =new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            responder.setException(coeusEx);
            responder.setResponseStatus(false);
            
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, coeusEx,
            "AwardBudgetMaintainanceServlet", "doPost");
            
        }catch( DBException dbEx ) {
            //dbEx.printStackTrace();
            int index=0;
            String errMsg = dbEx.getUserMessage();
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
            "AwardBudgetMaintainanceServlet", "perform");
            
        }catch(Exception e) {
            //e.printStackTrace();
            //print the error message at server side
            responder.setResponseStatus(false);
            //print the error message at client side
            responder.setException(e);
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e,
            "AwardBudgetMaintainanceServlet", "perform");
            
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "AwardBudgetMaintainanceServlet", "doPost");
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
                "AwardBudgetMaintainanceServlet", "doPost");
            }
        }
    }
    
    /** check for the Award budget right
     *@param Hashtable contains the Right Id's for both OSP Level and Unit Level
     *a) Extract the OSP level rights frm the hashtable results a Hashtable contains Rights
     *b)Extract the Unit level rights frm the hashtable results a Hashtable contains Rights
     *c) Making a DB call gets the Unit Level right and results a Hashtable
     *d)Making a DB call gets the OSP Level right and results a Hashtable
     *Get the hastable contains OSP and UNIT and put ii into a Hashtable
     *@ returns Hashtable - contains the OSP and Unit Level rights
     */
    private Hashtable checkRight(Hashtable rightData,String leadUnitNumber,String loggedinUser) throws DBException, CoeusException,Exception{
        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
        Set rightsSet = null;
        Hashtable rightResultData = new Hashtable();
        boolean hasRight = false;
        /** check for the OSP level and Unit Level rights. Extract the right Id and pass to the
         *DB to get the right information. Put the result into the Hashtable
         */
        rightsSet =  rightData.keySet();
        String key = "";
        String value = "";
        Iterator iterator= rightsSet.iterator();
        
        if(leadUnitNumber != null){/** Check for UNIT level right, where unit number is not required  */
            for(;iterator.hasNext();) {
                key = (String)iterator.next();
                value = (String)rightData.get(key);
                hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, value,leadUnitNumber);
                rightResultData.put(key, new Boolean(hasRight));
            }
        }else{/** Check for UNIT level right, where unit number is not required */
            for(;iterator.hasNext();) {
                key = (String)iterator.next();
                value = (String)rightData.get(key);
                hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, value);
                rightResultData.put(key, new Boolean(hasRight));
            }
        }
        return rightResultData;
    }//end checkRight
    
    private CoeusVector prepareBeanForCopy(CoeusVector cvData){
        
        String proposalNumber = "";
        String proposalVerNumber = "";
        String budgetPeriod = "";
        CoeusVector cvCopyData = new CoeusVector();
        int size = cvData.size();
        
        for (int index = 0 ; index < size; index++){        
            AwardBudgetCopyBean awardBudgetCopyBean = (AwardBudgetCopyBean)cvData.get(index);
            
            if(proposalNumber.equals("")){
                proposalNumber = awardBudgetCopyBean.getProposalNumber();
            }else{
                proposalNumber = proposalNumber+","+awardBudgetCopyBean.getProposalNumber();
            }
            
            if(proposalVerNumber.equals("")){
                proposalVerNumber = ""+awardBudgetCopyBean.getVersionNumber();
            }else{
                proposalVerNumber = proposalVerNumber+","+awardBudgetCopyBean.getVersionNumber();
            }
            

            
            if(budgetPeriod.equals("")){
                budgetPeriod = ""+awardBudgetCopyBean.getBudgetPeriod();
            }else{
                budgetPeriod = budgetPeriod+","+awardBudgetCopyBean.getBudgetPeriod();
            }
        }
        
        AwardBudgetCopyBean copyBean = (AwardBudgetCopyBean)cvData.get(0);
        
        cvCopyData.add(copyBean.getMitAwardNumber());
        cvCopyData.add(""+copyBean.getSequenceNumber());
        cvCopyData.add(""+copyBean.getAmountSequenceNumber());
        cvCopyData.add(proposalNumber);
        cvCopyData.add(proposalVerNumber);
        cvCopyData.add(budgetPeriod);
        //cvCopyData.add(""+size);
        
        return cvCopyData;
    }
    
    private CoeusVector claculateRatesForCE(
    CoeusVector cvRates , CoeusVector cvCostElements, AwardBudgetHeaderBean awardBudgetHeaderBean)throws CoeusException{
        
        CoeusVector cvData = new CoeusVector();
        
        //Holds the EB Line Items
        CoeusVector cvEBData = new CoeusVector();
        
        //Holds the OH Line Items
        //CoeusVector cvOHData= new CoeusVector();
        
        //Holds the EB rates
        CoeusVector cvOHRates = (CoeusVector)cvRates.get(0);
        
        //Holds the OH rates
        CoeusVector cvEBRates = (CoeusVector)cvRates.get(1);
        
        //Holds the parameter
        int awdBudCampusBasedOnCE = Integer.parseInt((String)cvRates.get(2));
        
        //Used if awdBudCampusBasedOnCE = 1
        double totalOnEBChangeAmount = 0;
        double totalOffEBChangeAmount = 0;
        
        double totalOnOHChangeAmount = 0;
        double totalOffOHChangeAmount = 0;
        
         //Used if awdBudCampusBasedOnCE = 0
        double totalEBChangeAmount = 0;
        double totalOHChangeAmount = 0;
        
        
        boolean onOffCmapusFlag = awardBudgetHeaderBean.isOnOffCampusFlag();
        
        
        //Remove the Calculated OH (Fringe benifit) LI from LIs passed from the client
        /*int size = cvCostElements.size();
        NotEquals neRateClassType = new NotEquals("rateClassType", "O");
        Equals eqNull = new Equals("rateClassType", null);
        Or eqNullORneRateClassType = new Or(eqNull , neRateClassType);
        cvEBData = cvCostElements.filter(neRateClassType);*/
        
        /*for(int i = 0; i < cvCostElements.size(); i++){
            AwardBudgetDetailBean awardBudgetDetailBean = 
                                (AwardBudgetDetailBean)cvCostElements.get(i);
            String rateClassType = awardBudgetDetailBean.getRateClassType();
            if(rateClassType == null || rateClassType.equals("E")){
                //cvOHData.add(awardBudgetDetailBean);
                //cvCostElements.remove(i);
                cvEBData.add(awardBudgetDetailBean);
            }
        }*/
        
        cvEBData = cvCostElements;
        
        
        //Calculation of the EB items 
        for(int index = 0; index < cvEBData.size() ; index++){
            
            AwardBudgetDetailBean awardBudgetDetailBean = 
                                (AwardBudgetDetailBean)cvEBData.get(index);
            
            Equals eqCE = new Equals("costElement", awardBudgetDetailBean.getCostElement());
            
            //Used if awdBudCampusBasedOnCE = 1
            Equals eqONCmapus = new Equals("onOffCampusFlag", true);
            Equals eqOFFCmapus = new Equals("onOffCampusFlag", false);
            
            And eqCEAndeqONCmapus = new And(eqCE , eqONCmapus);
            And eqCEAndeqOFFCmapus = new And(eqCE , eqOFFCmapus);
            
            //Used if awdBudCampusBasedOnCE = 0
            Equals eqCampusFlag = new Equals("onOffCampusFlag", onOffCmapusFlag);
            And eqCEAndeqCampusFlag = new And(eqCE ,eqCampusFlag );
            
            //Claculate the EB rates
            if(awdBudCampusBasedOnCE == 1){
                
                //Calculate rates for ON campus  
                CoeusVector cvFiltered = cvEBRates.filter(eqCEAndeqONCmapus);
                if(cvFiltered != null && cvFiltered.size()>0){
                    double rate = 0;
                    double changeAmount = 0;

                    AwardBudgetRatesBean awardBudgetRatesBean = 
                                            (AwardBudgetRatesBean)cvFiltered.get(0);
                    rate = awardBudgetRatesBean.getRate();
                    changeAmount = awardBudgetDetailBean.getOblChangeAmount();
                    changeAmount = changeAmount*(rate/100);

                    //Set the value to the On EB for calcuation of OH
                    awardBudgetDetailBean.setCalcOnEBAmount(changeAmount);
                    
                    totalOnEBChangeAmount = totalOnEBChangeAmount + changeAmount;
                }
                
                //Calculate rates for OFF campus  
                cvFiltered = cvEBRates.filter(eqCEAndeqOFFCmapus);
                if(cvFiltered != null && cvFiltered.size()>0){
                    double rate = 0;
                    double changeAmount = 0;

                    AwardBudgetRatesBean awardBudgetRatesBean = 
                                            (AwardBudgetRatesBean)cvFiltered.get(0);
                    rate = awardBudgetRatesBean.getRate();
                    changeAmount = awardBudgetDetailBean.getOblChangeAmount();
                    changeAmount = changeAmount*(rate/100);

                    //Set the value to the Off EB for calcuation of OH
                    awardBudgetDetailBean.setCalcOffEBAmount(changeAmount);
                    
                    totalOffEBChangeAmount = totalOffEBChangeAmount + changeAmount;
                }
                
            }else{
                CoeusVector cvFiltered = cvEBRates.filter(eqCEAndeqCampusFlag);
                if(cvFiltered != null && cvFiltered.size()>0){
                    double rate = 0;
                    double changeAmount = 0;

                    AwardBudgetRatesBean awardBudgetRatesBean = 
                                            (AwardBudgetRatesBean)cvFiltered.get(0);
                    rate = awardBudgetRatesBean.getRate();
                    changeAmount = awardBudgetDetailBean.getOblChangeAmount();
                    changeAmount = changeAmount*(rate/100);
                    
                    //Set the value to the EB for calcuation of OH
                    awardBudgetDetailBean.setCalcEBAmount(changeAmount);
                    
                    totalEBChangeAmount = totalEBChangeAmount + changeAmount;
                }//End if
            }//End else
        }//End for
        
        //Add up the amounts of the CEs to the EB Over Head since its is required
        //for OH (Fringe Benifit) calculation.
        
        /*
        Equals eqEBLi = new Equals("rateClassType" , "E" );
        Equals eqCampusFlag = new Equals("onOffCampusFlag", onOffCmapusFlag);
        And eqEBLiAndeqCampusFlag = new And(eqEBLi ,eqCampusFlag );
        CoeusVector cvFilData = cvEBData.filter(eqEBLiAndeqCampusFlag);*/
        /*if(cvFilData != null && cvFilData.size()>0){
            AwardBudgetDetailBean budDetailBean = (AwardBudgetDetailBean)cvFilData.get(0);
            budDetailBean.setOblChangeAmount(totalEBChangeAmount);
        }*/
        
        //Ajay
        /*for(int i = 0; i < cvEBData.size() ; i++){
            AwardBudgetDetailBean awdBudDetailBean = 
                                (AwardBudgetDetailBean)cvEBData.get(i);
            String rateClassType = awdBudDetailBean.getRateClassType();
            
            if(awdBudCampusBasedOnCE == 1){
                if(rateClassType != null && rateClassType.equals("E") 
                   && awdBudDetailBean.isOnOffCampusFlag()){
                       
                       awdBudDetailBean.setOblChangeAmount(totalOnEBChangeAmount);
                }else if(rateClassType != null && rateClassType.equals("E") 
                   && !awdBudDetailBean.isOnOffCampusFlag()){
                       
                    awdBudDetailBean.setOblChangeAmount(totalOffEBChangeAmount);
                }
            }else{
                if(rateClassType != null && rateClassType.equals("E") && 
                    awdBudDetailBean.isOnOffCampusFlag() == onOffCmapusFlag){
                        awdBudDetailBean.setOblChangeAmount(totalEBChangeAmount);
                }
            }//End else
        }//end for*/
        //Ajay
        
        
        //Calculation of the OH items 
        for(int index = 0; index < cvEBData.size() ; index++){
            AwardBudgetDetailBean awardBudgetDetailBean = 
                                (AwardBudgetDetailBean)cvEBData.get(index);

            Equals eqCE = new Equals("costElement", awardBudgetDetailBean.getCostElement());
            
            
            //Used if awdBudCampusBasedOnCE = 1
            Equals eqONCmapus = new Equals("onOffCampusFlag", true);
            Equals eqOFFCmapus = new Equals("onOffCampusFlag", false);

            And eqCEAndeqONCmapus = new And(eqCE , eqONCmapus);
            And eqCEAndeqOFFCmapus = new And(eqCE , eqOFFCmapus);

            //Used if awdBudCampusBasedOnCE = 0
            Equals eqCampusFlag = new Equals("onOffCampusFlag", onOffCmapusFlag);
            And eqCEAndeqCampusFlag = new And(eqCE ,eqCampusFlag );
            
            if(awdBudCampusBasedOnCE == 1){
                String rateClassType = awardBudgetDetailBean.getRateClassType();
                
                //Calculate rates for ON campus 
                CoeusVector cvFiltered = cvOHRates.filter(eqCEAndeqONCmapus);
                if(cvFiltered != null && cvFiltered.size()>0){
                    double rate = 0;
                    double changeAmount = 0;
                    double calcOnEBAmount = 0;
                    
                    /*if(rateClassType != null && rateClassType.equals("E") 
                           && !awardBudgetDetailBean.isOnOffCampusFlag()){
                               continue;
                    }*/
                    
                    //Ajay
                    /*if((rateClassType == null) || 
                       (rateClassType.equals("E") && awardBudgetDetailBean.isOnOffCampusFlag() ) ){*/
                    //Ajay
                    
                    
                    AwardBudgetRatesBean awardBudgetRatesBean = 
                                            (AwardBudgetRatesBean)cvFiltered.get(0);
                    
                    rate = awardBudgetRatesBean.getRate();
                    changeAmount = awardBudgetDetailBean.getOblChangeAmount();
                    calcOnEBAmount = awardBudgetDetailBean.getCalcOnEBAmount();
                    
                    changeAmount = changeAmount*(rate/100) + calcOnEBAmount*(rate/100);

                    totalOnOHChangeAmount = totalOnOHChangeAmount + changeAmount;
                    //}
                    
                }
                
                //Calculate rates for OFF campus 
                cvFiltered = cvOHRates.filter(eqCEAndeqOFFCmapus);
                if(cvFiltered != null && cvFiltered.size()>0){
                    double rate = 0;
                    double changeAmount = 0;
                    double calcOffEBAmount = 0;
                    
                    /*if(rateClassType == null || 
                       (rateClassType.equals("E") && !awardBudgetDetailBean.isOnOffCampusFlag())){*/
                         
                    AwardBudgetRatesBean awardBudgetRatesBean = 
                                            (AwardBudgetRatesBean)cvFiltered.get(0);
                    rate = awardBudgetRatesBean.getRate();
                    changeAmount = awardBudgetDetailBean.getOblChangeAmount();
                    calcOffEBAmount = awardBudgetDetailBean.getCalcOffEBAmount();
                    
                    changeAmount = changeAmount*(rate/100) + calcOffEBAmount*(rate/100);

                    totalOffOHChangeAmount = totalOffOHChangeAmount + changeAmount;
                    //}
                }
            }else{
                CoeusVector cvFiltered = cvOHRates.filter(eqCEAndeqCampusFlag);
                if(cvFiltered != null && cvFiltered.size()>0){
                    double rate = 0;
                    double changeAmount = 0;
                    double calcEBAmount = 0;
                    
                    AwardBudgetRatesBean awardBudgetRatesBean = 
                                            (AwardBudgetRatesBean)cvFiltered.get(0);
                    rate = awardBudgetRatesBean.getRate();
                    changeAmount = awardBudgetDetailBean.getOblChangeAmount();
                    calcEBAmount = awardBudgetDetailBean.getCalcEBAmount();
                    
                    changeAmount = changeAmount*(rate/100) + calcEBAmount*(rate/100);

                    totalOHChangeAmount = totalOHChangeAmount + changeAmount;
                }//End if
            }//End else
        }//End for
        
        /*Equals eqCE = new Equals("costElement", awardBudgetDetailOh.getCostElement());
        //Equals eqOHType = new Equals("ohType", "OH");
        //And eqCEAndeqOHType = new And(eqCE,eqOHType);
        
        CoeusVector cvFilteredOH = cvOHRates.filter(eqCE);
        
        //Apply rate to OH cost element and add the Eb rates to OH
        if(cvFilteredOH != null && cvFilteredOH.size()>0){
            double rate = 0;
            double changeAmount = 0;
            
            AwardBudgetRatesBean awardBudgetRatesBean = 
                                        (AwardBudgetRatesBean)cvFilteredOH.get(0);
            
            rate = awardBudgetRatesBean.getRate();
            changeAmount = awardBudgetDetailOh.getOblChangeAmount();
            changeAmount = changeAmount*rate;
            
            totalOHChangeAmount = totalEBChangeAmount + changeAmount;
        }*/
        
        cvData.add(new Integer(awdBudCampusBasedOnCE));
        if(awdBudCampusBasedOnCE == 1){
            cvData.add(new Double(totalOnEBChangeAmount));
            cvData.add(new Double(totalOffEBChangeAmount));
            cvData.add(new Double(totalOnOHChangeAmount));
            cvData.add(new Double(totalOffOHChangeAmount));
        }else{
            cvData.add(new Double(totalEBChangeAmount));
            cvData.add(new Double(totalOHChangeAmount));
        }
        return cvData;
    }
}
