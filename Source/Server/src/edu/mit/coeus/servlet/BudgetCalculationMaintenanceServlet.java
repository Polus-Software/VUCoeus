/*
 * @(#)BudgetCalculationMaintenanceServlet.java 1.0 10/15/03 15:15 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.servlet;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.bean.*;

import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.TypeConstants;

import edu.mit.coeus.budget.bean.*;
import edu.mit.coeus.budget.calculator.*;
import edu.mit.coeus.utils.CoeusConstants;
//import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ParameterUtils;
import edu.mit.coeus.utils.query.*;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author Sagin
 * @version :1.0 October 15, 2003 15:15 PM
 *
 */

public class BudgetCalculationMaintenanceServlet extends CoeusBaseServlet 
implements TypeConstants, SingleThreadModel {
    
    private final char CALCULATE_ALL_PERIODS = 'A';
    private final char CALCULATE_CURRENT_PERIOD = 'C';
    private final char CALCULATE_SALARY = 'S';
    private final char CALCULATE_PERSONNEL_LINE_ITEM = 'P';
    private final char GET_VALID_CALC_TYPES = 'B';
    // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
    private final char CALCULATE_LINE_ITEM = 'L';
    // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
    
//    private UtilFactory UtilFactory = new UtilFactory();

    /**
     * Represents Query Engine instance
     * 
     */
    private QueryEngine queryEngine = QueryEngine.getInstance(); 
    
    /**
     *  This method is used for applets.
     *  Post the information into server using object serialization.
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
        String unitNumber = "";
        int budgetPeriod = 0;
        BudgetCalculator budgetCalculator = null;
        String key = "";
        SalaryCalculator salaryCalculator = null;
        BudgetPersonnelDetailsBean personnelDetailsBean = null;
        BudgetInfoBean budgetInfoBean;
        
        try {
            // get an input stream
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            // keep all the beans into vector
            Vector dataObjects = new Vector();
            
            //get the function type
            char functionType = requester.getFunctionType();
            
            // get the budget data
            Hashtable htBudgetData = (Hashtable) requester.getDataObject();
            
            //get the data objects
            Vector vecDataObjects = (Vector) requester.getDataObjects();
            
            if (vecDataObjects != null && vecDataObjects.size() > 0) {
                budgetPeriod = ((Integer) vecDataObjects.get(0)).intValue();
            }
            
            if(functionType == CALCULATE_SALARY){
                //Get the key
                CoeusVector cvBudgetPersonnelDetails = (CoeusVector) htBudgetData.get(BudgetPersonnelDetailsBean.class);

                personnelDetailsBean = (BudgetPersonnelDetailsBean) cvBudgetPersonnelDetails.get(0);
                key = personnelDetailsBean.getProposalNumber() + personnelDetailsBean.getVersionNumber();

                //keep the budget data in the queryEngine
                //queryEngine.addDataCollection(key, htBudgetData);
                
                budgetInfoBean = new BudgetInfoBean();
                
//                //Initialize budget calculator
//                salaryCalculator = new SalaryCalculator(personnelDetailsBean);
                
            } else {
                //Get the key
                //if (cvBudgetInfo != null && cvBudgetInfo.size() > 0) {
                CoeusVector cvBudgetInfo = (CoeusVector) htBudgetData.get(BudgetInfoBean.class);
                cvBudgetInfo.remove(null);
                budgetInfoBean = (BudgetInfoBean) cvBudgetInfo.get(0);
                key = budgetInfoBean.getProposalNumber() + budgetInfoBean.getVersionNumber();
            }
          
            //keep the budget data in the queryEngine
            queryEngine.addDataCollection(key, htBudgetData);

            //Initialize budget calculator
            budgetCalculator = new BudgetCalculator(budgetInfoBean);
            
            //Set the key for calculator
            budgetCalculator.setKey(key);
            
            
            // Calculate All Budgets
            if (functionType == CALCULATE_ALL_PERIODS) {
                budgetCalculator.calculate();
                responder.setDataObject(queryEngine.getDataCollection(key));
                responder.setDataObjects(budgetCalculator.getVecMessages());
                responder.setResponseStatus(true);
                
                //Calculate Current Period
            }else if(functionType == CALCULATE_CURRENT_PERIOD){
                budgetCalculator.calculatePeriod(budgetPeriod);
                responder.setDataObject(queryEngine.getDataCollection(key));
                responder.setDataObjects(budgetCalculator.getVecMessages());
                responder.setResponseStatus(true);
                
            //Calculate person Salary
            }else if(functionType == CALCULATE_SALARY){
//                salaryCalculator.distributeCalculatedSal();
//                if(personnelDetailsBean.getAcType() == null) {
//                    personnelDetailsBean.setAcType("U");
//                }
//                queryEngine.update(key, personnelDetailsBean);
                budgetCalculator.calculateSalary();
                responder.setDataObject(queryEngine.getDataCollection(key));
                responder.setDataObjects(budgetCalculator.getVecMessages());
                responder.setResponseStatus(true);
                
            //Calculate Personnel Line Item
            }else if(functionType == CALCULATE_PERSONNEL_LINE_ITEM){
                //Get the key
                CoeusVector cvPersonnelDetails = (CoeusVector) htBudgetData.get(BudgetPersonnelDetailsBean.class);
                if (cvPersonnelDetails != null && cvPersonnelDetails.size() > 0) {
                    personnelDetailsBean = (BudgetPersonnelDetailsBean) cvPersonnelDetails.get(0);
                    budgetPeriod = personnelDetailsBean.getBudgetPeriod();
                    int lineItemNo = personnelDetailsBean.getLineItemNumber();
                    
                    //Enhancement ID : 709 Case 3 - Starts here
                    //Get the Cost Element from the Budget Detail bean
                    
                    Equals eqPeriod = new Equals("budgetPeriod", new Integer(budgetPeriod));
                    Equals eqLineItem = new Equals("lineItemNumber", new Integer(lineItemNo));
                    And eqPeriodAndeqLineItem = new And(eqPeriod, eqLineItem);
                    String costElement = "";
                    CoeusVector cvLineItems = queryEngine.getActiveData(key, 
                        BudgetDetailBean.class, eqPeriodAndeqLineItem);
                    
                    if (cvLineItems != null && cvLineItems.size() > 0) {
                        BudgetDetailBean detailBean = (BudgetDetailBean) cvLineItems.get(0);
                        costElement = detailBean.getCostElement();
                    }
                    //budgetCalculator.calculatePersonnelLineItem(budgetPeriod, lineItemNo);
                    budgetCalculator.calculatePersonnelLineItem(budgetPeriod, 
                        lineItemNo, costElement);
                    //Enhancement ID : 709 Case 3 - Ends here
                }
                responder.setDataObject(queryEngine.getDataCollection(key));
                responder.setDataObjects(budgetCalculator.getVecMessages());
                responder.setResponseStatus(true);
                
            }else if(functionType == GET_VALID_CALC_TYPES){
                String calcTypeId = (String)requester.getDataObject();
                BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
                dataObjects = budgetDataTxnBean.getValidCalcTypes(calcTypeId);
                responder.setDataObjects(dataObjects);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
            }else if (functionType == CALCULATE_LINE_ITEM){
                BudgetDetailBean budgetDetailBean = (BudgetDetailBean)vecDataObjects.get(1);
                LineItemCalculator lineItemCalculator = new LineItemCalculator();
                lineItemCalculator.calculate(budgetDetailBean);
                String queryKey = budgetDetailBean.getProposalNumber()+budgetDetailBean.getVersionNumber();
                
                Equals eqProposalNumber = new Equals("proposalNumber", budgetDetailBean.getProposalNumber());
                Equals eqVersionNumber = new Equals("versionNumber", new Integer(budgetDetailBean.getVersionNumber()));
                Equals eqBudgetPeriod = new Equals("budgetPeriod", new Integer(budgetDetailBean.getBudgetPeriod()));
                Equals eqLineItemNumber = new Equals("lineItemNumber", new Integer(budgetDetailBean.getLineItemNumber()));
                And andProposalVersion = new And(eqProposalNumber,eqVersionNumber);
                And andBudgetPeriods = new And(andProposalVersion,eqBudgetPeriod);
                And andBudgetLineItem = new And(andBudgetPeriods,eqLineItemNumber);
                 QueryEngine.getInstance().getDetails(queryKey,BudgetFormulatedCostDetailsBean.class);
                CoeusVector cvCalAmtsTAble = QueryEngine.getInstance().executeQuery(queryKey, BudgetDetailCalAmountsBean.class, andBudgetLineItem);
                responder.setDataObject(cvCalAmtsTAble);
                responder.setResponseStatus(true);
            }
            // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
            //Added for Case#2402- use a parameter to set the length of the account number throughout app - Start
            //To get MAX_ACCOUNT_NUMBER_LENGTH paramter details
            CoeusVector cvParameters = new CoeusVector();
            CoeusParameterBean coeusParameterBean = new CoeusParameterBean();
            coeusParameterBean.setParameterName(CoeusConstants.MAX_ACCOUNT_NUMBER_LENGTH);
            coeusParameterBean.setParameterValue(ParameterUtils.getMaxAccountNumberLength());
            cvParameters.addElement(coeusParameterBean);
            Hashtable parameterCollection = new Hashtable();
            parameterCollection.put(CoeusParameterBean.class,cvParameters);
            queryEngine.addCollection(key,CoeusParameterBean.class,cvParameters);
            //Case#2402 - End
        }catch(Exception e) {
            //e.printStackTrace();
            responder.setResponseStatus(false);
            responder.setMessage(e.getMessage());
            responder.setException(e);
            UtilFactory.log( e.getMessage(), e,
            "BudgetCalculationMaintenanceServlet", "perform");
            
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "BudgetCalculationMaintenanceServlet", "doPost");
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
                "BudgetCalculationMaintenanceServlet", "perform");
            }
        }
    }
}