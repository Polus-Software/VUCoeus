/*
 * @(#)BudgetCalculator.java January 28, 2004, 5:52 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.budget.edi;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.budget.bean.*;
import edu.mit.coeus.budget.edi.*;
import edu.mit.coeus.budget.edi.bean.*;
import edu.mit.coeus.budget.calculator.*; //For testing
import edu.mit.coeus.exception.CoeusException;
import java.util.*;

/**
 * This class is the starting point of budget edi calculation. Whenever a
 * request is received to calculate, this class needs to be invoked. Makes
 * use of LineItemEdiCalculator & LineItemEdiConvertor to calculate the Line Items,
 * PersonnelLineItemEdiCalculator & PersonnelLiEdiConvertor for calculating personnel line items
 *
 * @author  Sagin
 * @version 1.0
 * Created on January 28, 2004, 5:52 PM
 */
public class BudgetEdiCalculator {

  ///////////////////////////////////////
  // attributes

    /**
     * Represents Query Engine instance
     * 
     */
    private QueryEngine queryEngine = QueryEngine.getInstance();  

    /**
     * Represents key , Proposal Number + Version Number
     * 
     */
    private String key = "";  

    /**
     * Represents Line Item EDI Convertor
     * 
     */
    private LineItemEDIConverter lineItemEDIConverter;   

    /**
     * Represents Personnel Line Item EDI Convertor
     * 
     */
    private PersonnelLIEdiConvertor personnelLIEdiConvertor; 

    /**
     * Represents Vector containing rates not available messages
     */
    private Vector vecMessages = new CoeusVector(); 

   /*
    *Added by Geo
    * For doing the calculation period by period
    */

    private int period;
  ///////////////////////////////////////
  // operations


    /**
     * Constructor...
     * 
     * @param key 
     */
    public BudgetEdiCalculator(String key) {
            
        lineItemEDIConverter = new LineItemEDIConverter();
        personnelLIEdiConvertor = new PersonnelLIEdiConvertor();
        personnelLIEdiConvertor.setKey(key);
        lineItemEDIConverter.setKey(key);
        this.key = key;
    } // end BudgetEdiCalculator        

    /**
     * - This is the starting point of Budget EDI calculation.
     * - Use Query Engine to get all the Budget Data
     * 
     */
    public void calculate() {
        /*
         *Added by Geo
         * For doing the filtering of person details period by period. Since the
         * calculation is firing period by period from the servlet.
         */
        personnelLIEdiConvertor.setPeriod(getPeriod());
        lineItemEDIConverter.setPeriod(getPeriod());
        //Calculate personnel edi
        personnelLIEdiConvertor.calculateEDI();
        
        //Calculate line item edi
        lineItemEDIConverter.calculateEDI();
        
        //Get the list of rates not available messages
        if (lineItemEDIConverter.getVecMessages().size() > 0) {
            vecMessages.addAll(lineItemEDIConverter.getVecMessages());
        }
        if (personnelLIEdiConvertor.getVecMessages().size() > 0) {
            vecMessages.addAll(personnelLIEdiConvertor.getVecMessages());
        }
        
        //Update the edi data to database.
        //saveEDIData();
        
    } // end Calculate           

    /**
     * - Saves the EDI data into tables OSP$BUDGET_PER_DETAILS_FOR_EDI,
     *   OSP$BUDGET_PER_CAL_AMT_FOR_EDI, OSP$BUDGET_RATE_BASE_FOR_EDI.
     * - Use Query Engine to get all the Budget EDI Data that has to be updated.
     * 
     */
    /*public void saveEDIData() {
        Hashtable htBudgetEdiData = new Hashtable();
        CoeusVector cvPersonnelDetailsEdi; //Represents all the Budget Personnel Details For EDI
        CoeusVector cvPersonnelCalAmtsEdi; //Represents all the Budget Personnel Cal Amounts For EDI
        CoeusVector cvRateBaseEdi; //Represents all the Budget Personnel Cal Amounts For EDI
        try {
            //Get all the edi data in a hashtable
            cvPersonnelDetailsEdi = queryEngine.getDetails(key, BudgetPersonnelDetailsEdiBean.class);
            cvPersonnelCalAmtsEdi = queryEngine.getDetails(key, BudgetPersonnelCalAmountsEdiBean.class);
            cvRateBaseEdi = queryEngine.getDetails(key, BudgetRateBaseEdiBean.class);
            htBudgetEdiData.put(BudgetPersonnelDetailsEdiBean.class, cvPersonnelDetailsEdi);
            htBudgetEdiData.put(BudgetPersonnelCalAmountsEdiBean.class, cvPersonnelCalAmtsEdi);
            htBudgetEdiData.put(BudgetRateBaseEdiBean.class, cvRateBaseEdi);

            //Send the edi data to update
            BudgetUpdateTxnBean budgetUpdateTxnBean = new BudgetUpdateTxnBean("COEUS");
            boolean success = budgetUpdateTxnBean.addUpdDeleteBudgetEDI(htBudgetEdiData);

            if (success) {
                System.out.println("Budget EDI data saved successfully");
            }
        } catch(Exception exception) {
            System.out.println("Budget EDI data save failed");
            exception.printStackTrace();
        }
        
        
    } // end Calculate    

    /** Getter for property vecMessages.
     * @return Value of property vecMessages.
     *
     */
    public Vector getVecMessages() {
        return vecMessages;
    }
    
    /** Setter for property vecMessages.
     * @param vecMessages New value of property vecMessages.
     *
     */
    public void setVecMessages(Vector vecMessages) {
        this.vecMessages = vecMessages;
    }
    
    /** Getter for property key.
     * @return Value of property key.
     *
     */
    public String getKey() {
        return key;
    }
    
    /** Setter for property key.
     * @param key New value of property key.
     *
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Getter for property period.
     * @return Value of property period.
     */
    public int getPeriod() {
        return period;
    }    
    
    /**
     * Setter for property period.
     * @param period New value of property period.
     */
    public void setPeriod(int period) {
        this.period = period;
    }    
    
    /*public static void main(String s[]) {
        QueryEngine queryEngine = QueryEngine.getInstance();
        TestCalculator test = new TestCalculator();
        //Hashtable budgetData = test.getAllBudgetData("01100555", 10);
        //String key = "01100555" + 10;
        //Hashtable budgetData = test.getAllBudgetData("01100693", 1);
        //String key = "01100693" + 1;
        //Hashtable budgetData = test.getAllBudgetData("01100675", 3);
        //String key = "01100675" + 3;
        //Hashtable budgetData = test.getAllBudgetData("01100883", 1);
        //String key = "01100883" + 1;
        Hashtable budgetData = test.getAllBudgetData("01100889", 1);
        String key = "01100889" + 1;
        BudgetInfoBean budgetInfoBean = new BudgetInfoBean();
        CoeusVector cvLineItemDetails;
        BudgetDetailBean budgetDetails = new BudgetDetailBean();
        try {
            queryEngine.addDataCollection(key, budgetData);
            //System.out.println("before");
            CoeusVector cvBudgetInfo = queryEngine.getDetails(key,BudgetInfoBean.class);
            budgetInfoBean = (BudgetInfoBean) cvBudgetInfo.get(0);
//            cvLineItemDetails = queryEngine.getDetails(key,BudgetDetailBean.class);
//            System.out.println("cvLineItemDetails.size() - >> " + cvLineItemDetails.size());
//            budgetDetails = (BudgetDetailBean) cvLineItemDetails.get(0);
//            budgetDetails.setLineItemCost(300);
//            budgetDetails.setCostSharingAmount(200);
//            budgetDetails.setAcType("U");
//            queryEngine.update(key, budgetDetails);
//            budgetDetails = (BudgetDetailBean) cvLineItemDetails.get(1);
//            budgetDetails.setLineItemCost(1500);
//            budgetDetails.setCostSharingAmount(1000);
//            budgetDetails.setAcType("U");
//            queryEngine.update(key, budgetDetails);
            BudgetEdiCalculator budgetEdiCalculator = new BudgetEdiCalculator(key);
            budgetEdiCalculator.calculate();
            
            //print Budget Info
            //cvBudgetInfo = queryEngine.getDetails(key,BudgetInfoBean.class);
            /*for(int i=0; i < cvBudgetInfo.size(); i++) {
                budgetInfoBean = (BudgetInfoBean) cvBudgetInfo.get(i);
                System.out.println("budgetInfoBean"+i+" >>> "+ budgetInfoBean.toString());
            }*/
            /*
            //print Budget Periods
            CoeusVector cvBudgetPeriods = queryEngine.getDetails(key,BudgetPeriodBean.class);
            for(int i=0; i < cvBudgetPeriods.size(); i++) {
                BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean) cvBudgetPeriods.get(i);
                //System.out.println("BudgetPeriodBean"+i+" >>> "+ budgetPeriodBean.toString());
            }
            
            //print Budget Details
            cvLineItemDetails = queryEngine.getDetails(key,BudgetDetailBean.class);
            for(int i=0; i < cvLineItemDetails.size(); i++) {
                budgetDetails = (BudgetDetailBean) cvLineItemDetails.get(i);
                //System.out.println("budgetDetails"+i+" >>> "+ budgetDetails.toString());
            }
            
            //print Budget Details
            CoeusVector cvPersonnelLineItems = queryEngine.getDetails(key,BudgetPersonnelDetailsBean.class);
            BudgetPersonnelDetailsBean personnelDetailsBean;
            for(int i=0; i < cvPersonnelLineItems.size(); i++) {
                personnelDetailsBean = (BudgetPersonnelDetailsBean) cvPersonnelLineItems.get(i);
                //System.out.println("personnelDetailsBean"+i+" >>> "+ personnelDetailsBean.toString());
            }
            
            //print Budget Line Item Cal Amts
            CoeusVector cvLineItemCalAmts = queryEngine.getDetails(key,BudgetDetailCalAmountsBean.class);
            BudgetDetailCalAmountsBean calAmtsBean;
            for(int i=0; i < cvLineItemCalAmts.size(); i++) {
                calAmtsBean = (BudgetDetailCalAmountsBean) cvLineItemCalAmts.get(i);
                //System.out.println("calAmtsBean"+i+" >>> "+ calAmtsBean.toString());
            }
            //System.out.println("Budget Calculation Over");
            //System.out.println("LineItemCalculator: perDayCostSharing - >> " + lineItemCalculator.perDayCostSharing);
             */
   /*     } catch (Exception ex) {
            ex.printStackTrace();
        }
    }*/
    
 } // end BudgetCalculator



