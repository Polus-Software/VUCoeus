/*
 * Controller.java
 *
 * Created on September 29, 2003, 2:29 PM
 */

package edu.mit.coeus.budget.controller;

/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author sharathk
 */
import edu.mit.coeus.exception.CoeusClientException;
import java.awt.Component;
import javax.swing.JOptionPane;

import java.util.*;

import edu.mit.coeus.budget.gui.BudgetMessageForm;
import edu.mit.coeus.budget.gui.StatusWindow;
import edu.mit.coeus.budget.bean.*;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;

/** This is the Base Class for GUI Controller.
 * All the operations/Functionalities for the GUI
 * should contain within this class.
 */
public abstract class Controller extends edu.mit.coeus.gui.event.Controller{
    
    private static final String SERVLET = "/BudgetCalculationMaintenanceServlet";
    private static final char CALCULATE_ALL_PERIODS = 'A';
    private static final char CALCULATE_CURRENT_PERIOD = 'C';
    private static final int ALL_PERIODS = -1;
    private static final String EMPTY_STRING = "";
    
    
    private QueryEngine queryEngine = QueryEngine.getInstance();
    
    private boolean calculate;
    private StatusWindow statusWindow;
    
    private boolean fromCostShare;
    private boolean fromUnderRecovery;
    private boolean parentProposal;
    //COEUSQA-2452_Proposal Hierarchy quirk: errant underrecovery alert at parent budget_Start
    private double underRecoveryAmount;
    private double costSharingAmount;
    //COEUSQA-2452_Proposal Hierarchy quirk: errant underrecovery alert at parent budget_End
    //For Budget Modular Enhancement case #2087 start 1
    private Hashtable modularDataObject;
    //For Budget Modular Enhancement case #2087 end 1
    /** Case #1801 :Parameterize Under-recovery and cost-sharing distribution Start1
     */
    /** holds true if FORCE_UNDER_RECOVERY_DISTRIBUTION in parameter table is set to 1
     */
    private boolean forceUnderRecovery;
    /**holds true if FORCE_COST_SHARING_DISTRIBUTION in parameter table is set to 1.
     */
    private boolean forceCostSharing;
    /** Case #1801 :Parameterize Under-recovery and cost-sharing distribution End1
     **/

    
    /**
     * Represents Vector containing rates not available messages
     */
    private Vector vecMessages;
    
    private BudgetInfoBean budgetInfoBean;
    /** for Modular Budget enhancement*/
    public  static final String BUDGET_SERVLET = "/BudgetMaintenanceServlet";
    private final String connectTo = CoeusGuiConstants.CONNECTION_URL+ BUDGET_SERVLET;
    private static final char SYNC_MODULAR_BUDGET = 'q'; 
    
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
    private final char GET_PERIOD_TYPE ='n';
    private final char GET_ACTIVE_PERIOD_TYPE ='o';
    private final String connectURL = "/BudgetMaintenanceServlet";
    private static final String COULD_NOT_CONTACT_SERVER = "Could not contact server";
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End 
    
    /**Modular Budget end  */
    /** creates a new instance of Controller.
     * @param budgetBean BudgetBean
     */
    public Controller(BudgetBean budgetBean){
        this.budgetInfoBean = budgetInfoBean;
    }
    
    /** creates a new instance of Controller.
     */
    public Controller() {
    }
    
    /** This is used to notify whether Save is required */
    private boolean saveRequired;
    
    /** This is used to hold the mode .
     *D for Display, I for Add, U for Modify
     */
    private char functionType;
    
    static {
        //Here the Event Manager and Event Trigger Objects will be instantiated.
    }
    
    /** returns the Component which is being controlled by this Controller.
     * @return Component which is being controlled by this Controller.
     */
    public abstract Component getControlledUI();
    
    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data to set to the form
     */
    public abstract void setFormData(Object data) throws CoeusException;
    
    
    /** returns the form data
     * @return the form data
     */
    public abstract Object getFormData();
    
    /** perform field formatting.
     * enabling, disabling components depending on the
     * function type.
     */
    public abstract void formatFields();
    
    /** validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if
     * validation is through else returns false.
     */
    public abstract boolean validate() throws CoeusUIException;
    
    /** registers GUI Components with event Listeners.
     */
    public abstract void registerComponents();
    
    /** saves the Form Data.
     */
    public abstract void saveFormData();
    
    /** displays the Form which is being controlled.
     */
    public abstract void display();
    
    
    
    /** Calculates the budget data present in the Query Engine. Gets the data from
     * QueryEngine, send to server for calculation, gets back the data after calculation
     * and sets it back to the QueryEngine data collection.
     * @param key key - The key used to get the budget data hashtable
     * @param budgetPeriod Indicates whether current period or all periods
     *      has to be calculated.
     * @return boolean - Indiates whether the calculation was successfully completed or not
     */
    public boolean calculate(String key, int budgetPeriod) {
        String connectTo = CoeusGuiConstants.CONNECTION_URL + SERVLET;
        RequesterBean request = new RequesterBean();
        
        //Set the Budget Data by getting from Query Engine
        request.setDataObject(queryEngine.getDataCollection(key));
        
        //Set function type by checking whether all periods or only current period has to be calculated
        if (budgetPeriod == ALL_PERIODS) {
            request.setFunctionType(CALCULATE_ALL_PERIODS);
        } else {
            request.setFunctionType(CALCULATE_CURRENT_PERIOD);
            Vector vecBudgetPeriod = new Vector();
            vecBudgetPeriod.add(new Integer(budgetPeriod));
            request.setDataObjects(vecBudgetPeriod);
        }
        ResponderBean response;
        try {
            //Send the data to calculate at the server side
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
            comm.send();
            response = comm.getResponse();
            if(response==null){
                CoeusOptionPane.showErrorDialog("Could Not Fetch Data From Server After Calculation ");
                return false;
            }
            if(! response.isSuccessfulResponse()) {
                //CoeusOptionPane.showErrorDialog(response.getMessage());
                System.out.println("Could Not Fetch Data From Server After Calculation");
                return false;
            }
        } catch (Exception exception) {
            return false;
        }
        
        //Get the Budget Data and messages vector
        Hashtable htBudgetData = (Hashtable) response.getDataObject();
        vecMessages = response.getDataObjects();
        
        //Set the budget data back to QueryEngine
        queryEngine.addDataCollection(key, htBudgetData);
        
        //If rates not available messages are there then display the messages.
        if (vecMessages != null && vecMessages.size() > 0) {
            displayRatesNotAvailableMessages();
        }
        
        checkCostSharingAndUnderRecovery(key);
        return true;
    }
    
    /** Calculates the budget data present in the Query Engine. Gets the data from
     * QueryEngine, send to server for calculation, gets back the data after calculation
     * and sets it back to the QueryEngine data collection.
     * @return boolean - Indiates whether the calculation was successfully completed or not
     * @param displayStatusWindow if true displays calculation status as a modal window.
     * @param key - The key used to get the budget data hashtable
     * @param budgetPeriod - Indicates whether current period or all periods
     *      has to be calculated.
     */
    public boolean calculate(final String key, final int budgetPeriod, boolean displayStatusWindow) {
        if(! displayStatusWindow) {
            return calculate(key, budgetPeriod);
        }
        
        if(statusWindow == null) {
            statusWindow = new StatusWindow(CoeusGuiConstants.getMDIForm(), true);
        }
        statusWindow.setHeader("Calculating Budget");
        statusWindow.setFooter("Please Wait...");
        statusWindow.display();
        
        calculate = false;
        
        Thread thread = new Thread(new Runnable() {
            public void run() {
                calculate = calculate(key, budgetPeriod);
                statusWindow.setVisible(false);
            }
        });
        thread.start();
        
        return calculate;
    }
    
    public void displayRatesNotAvailableMessages() {
        BudgetMessageForm budgetMessageForm = new BudgetMessageForm();
        budgetMessageForm.setFormData(vecMessages);
        budgetMessageForm.display();
    }
    
     /*
      *   Method to find all the Rates with in the a Proposal Period
      * @param vecRateBean CoeusVector containing all the Rates from the Master Table
      * @param budgetInfoBean BudgetInfoBean for getting Start Date and End Date of Proposal
      */
    
    public CoeusVector getRatesWithInPeriod(CoeusVector vecRateBean,BudgetInfoBean budgetInfoBean) {
        /* // Commented for Case 3447
        long longTime = budgetInfoBean.getStartDate().getTime();
        GreaterThan greaterThanStartDateObj =  new GreaterThan("startDate",new java.sql.Date(longTime));
        longTime = budgetInfoBean.getEndDate().getTime();
        LesserThan lesserThanEndDateObj =  new LesserThan("startDate",new java.sql.Date(longTime));
        Equals equalsEndDateObj = new Equals("startDate",new java.sql.Date(longTime));
        Or lsEqualEndDate = new Or(equalsEndDateObj,lesserThanEndDateObj);
        And dateGreaterAndEquals = new And(greaterThanStartDateObj,lsEqualEndDate);
        CoeusVector vecFiltered = vecRateBean.filter(dateGreaterAndEquals);
        return vecFiltered;
         */
        return getRatesWithInPeriod(vecRateBean, budgetInfoBean, false);
    }
    
    //Case 3447 - START
    public CoeusVector getRatesWithInPeriod(CoeusVector vecRateBean,BudgetInfoBean budgetInfoBean, boolean inflation) {
        long longTime;
        if(inflation) {
            longTime = getStartDateForInflationRates(budgetInfoBean).getTime();
        }else {
            longTime = budgetInfoBean.getStartDate().getTime();
        }
        GreaterThan greaterThanStartDateObj =  new GreaterThan("startDate",new java.sql.Date(longTime));
        longTime = budgetInfoBean.getEndDate().getTime();
        LesserThan lesserThanEndDateObj =  new LesserThan("startDate",new java.sql.Date(longTime));
        Equals equalsEndDateObj = new Equals("startDate",new java.sql.Date(longTime));
        Or lsEqualEndDate = new Or(equalsEndDateObj,lesserThanEndDateObj);
        And dateGreaterAndEquals = new And(greaterThanStartDateObj,lsEqualEndDate);
        CoeusVector vecFiltered = vecRateBean.filter(dateGreaterAndEquals);
        return vecFiltered;
        
    }
    
    
    /*
     *  Method to find all the Rates just before the Start Date of Proposal
     * @param vecRateBean CoeusVector containing all the Rates from the Master Table
     * @param budgetInfoBean BudgetInfoBean for getting Start Date and End Date of Proposal
     */
    
    public CoeusVector getRatesOnThePeriod(CoeusVector vecRateBean,BudgetInfoBean budgetInfoBean) {
        /*
        long longTime = budgetInfoBean.getStartDate().getTime();
        Equals equalsStartDate = new Equals("startDate", new java.sql.Date(longTime));
        LesserThan lesserThanStartDate =  new LesserThan("startDate", new java.sql.Date(longTime));
        Or equalsOrlesserThanStartDate = new Or(equalsStartDate, lesserThanStartDate);
        Equals equalsOnCampus = new Equals("onOffCampusFlag", true);
        Equals equalsOffCampus = new Equals("onOffCampusFlag", false);
        And equalsOrlesserThanStartDateAndEqualsOnCampus = new And(equalsOrlesserThanStartDate, equalsOnCampus);
        And equalsOrlesserThanStartDateAndEqualsOffCampus = new And(equalsOrlesserThanStartDate, equalsOffCampus);
        CoeusVector lessThanOrEqOffCampusVector;
        CoeusVector lessThanOrEqOnCampusVector;
        CoeusVector combinedRates = new CoeusVector();
        
        lessThanOrEqOffCampusVector = vecRateBean.filter(equalsOrlesserThanStartDateAndEqualsOffCampus);
        if(lessThanOrEqOffCampusVector != null && lessThanOrEqOffCampusVector.size() > 0) {
            lessThanOrEqOffCampusVector.sort("startDate",false);
            combinedRates.add(lessThanOrEqOffCampusVector.get(0));
        }
        
        lessThanOrEqOnCampusVector = vecRateBean.filter(equalsOrlesserThanStartDateAndEqualsOnCampus);
        if(lessThanOrEqOnCampusVector != null && lessThanOrEqOnCampusVector.size() > 0) {
            lessThanOrEqOnCampusVector.sort("startDate",false);
            combinedRates.add(lessThanOrEqOnCampusVector.get(0));
        }
        
        combinedRates.sort("startDate", true);
        return combinedRates;
        */
        return getRatesOnThePeriod(vecRateBean, budgetInfoBean, false);
    }
    
    /**
     * Infaltion Rate uses earliest effective Date of Budget Persons
     */
    private Date getStartDateForInflationRates(BudgetInfoBean budgetInfoBean) {
        String key = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        CoeusVector coeusVector = null;
        try{
            coeusVector = queryEngine.getDetails(key, BudgetPersonsBean.class);
        }catch(CoeusException coeusException) {
            //Exceeption occured while retreiving BudgetPersons for inflation rates.
            coeusException.printStackTrace();
            return budgetInfoBean.getStartDate();
        }
        Date earlyDate = null;
        //Check for earliest effective date
        if(coeusVector != null && coeusVector.size() > 0) {
            int size = coeusVector.size();
            BudgetPersonsBean budgetPersonsBean;
            
            for(int index=0; index < size; index++) {
                budgetPersonsBean = (BudgetPersonsBean)coeusVector.get(index);
                if(index == 0) {
                    earlyDate = budgetPersonsBean.getEffectiveDate();
                }else if(budgetPersonsBean.getEffectiveDate() != null) {
                    if(earlyDate == null) earlyDate = budgetPersonsBean.getEffectiveDate();
                    else earlyDate = earlyDate.after(budgetPersonsBean.getEffectiveDate()) ? budgetPersonsBean.getEffectiveDate() : earlyDate;
                }
                
            }
        }
        if(earlyDate == null) {
            return budgetInfoBean.getStartDate();
        }else {
            earlyDate = earlyDate.after(budgetInfoBean.getStartDate()) ? budgetInfoBean.getStartDate() : earlyDate;
        }
        return earlyDate;
    }
    //Case 3447 - END
    
    public CoeusVector getRatesOnThePeriod(CoeusVector vecRateBean,BudgetInfoBean budgetInfoBean, boolean inflation){
        //Case 3447 - START
        long longTime;
        if(inflation) {
            longTime = getStartDateForInflationRates(budgetInfoBean).getTime();
        }else {
            longTime = budgetInfoBean.getStartDate().getTime();
        }
        //Case 3447 - END
        Equals equalsStartDate = new Equals("startDate", new java.sql.Date(longTime));
        LesserThan lesserThanStartDate =  new LesserThan("startDate", new java.sql.Date(longTime));
        Or equalsOrlesserThanStartDate = new Or(equalsStartDate, lesserThanStartDate);
        Equals equalsOnCampus = new Equals("onOffCampusFlag", true);
        Equals equalsOffCampus = new Equals("onOffCampusFlag", false);
        And equalsOrlesserThanStartDateAndEqualsOnCampus = new And(equalsOrlesserThanStartDate, equalsOnCampus);
        And equalsOrlesserThanStartDateAndEqualsOffCampus = new And(equalsOrlesserThanStartDate, equalsOffCampus);
        CoeusVector lessThanOrEqOffCampusVector;
        CoeusVector lessThanOrEqOnCampusVector;
        CoeusVector combinedRates = new CoeusVector();
        
        lessThanOrEqOffCampusVector = vecRateBean.filter(equalsOrlesserThanStartDateAndEqualsOffCampus);
        if(lessThanOrEqOffCampusVector != null && lessThanOrEqOffCampusVector.size() > 0) {
            lessThanOrEqOffCampusVector.sort("startDate",false);
            combinedRates.add(lessThanOrEqOffCampusVector.get(0));
        }
        
        lessThanOrEqOnCampusVector = vecRateBean.filter(equalsOrlesserThanStartDateAndEqualsOnCampus);
        if(lessThanOrEqOnCampusVector != null && lessThanOrEqOnCampusVector.size() > 0) {
            lessThanOrEqOnCampusVector.sort("startDate",false);
            combinedRates.add(lessThanOrEqOnCampusVector.get(0));
        }
        
        combinedRates.sort("startDate", true);
        return combinedRates;
        
    }
    
    private void checkCostSharingAndUnderRecovery(String key) {
        try {
            
            CoeusVector vecBudgetInfoBean = queryEngine.getDetails(key,BudgetInfoBean.class);
            BudgetInfoBean budgetInfoBean = (BudgetInfoBean) vecBudgetInfoBean.get(0);
            
            if(!isFromCostShare()) {
                
                CoeusVector vecCostSharingBeans = queryEngine.executeQuery(key,ProposalCostSharingBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
                
                if(vecCostSharingBeans != null && vecCostSharingBeans.size() > 0) {
                //COEUSQA-2452_Proposal Hierarchy quirk: errant underrecovery alert at parent budget_start                    
                    if(costSharingAmount > 0){
                        budgetInfoBean.setCostSharingAmount(costSharingAmount);
                    }
                    //COEUSQA-2452_Proposal Hierarchy quirk: errant underrecovery alert at parent budget_End
                    if (budgetInfoBean.getCostSharingAmount() == 0) {
                        
                        DollarCurrencyTextField dollarCurrencyTextField = new DollarCurrencyTextField();
                        dollarCurrencyTextField.setValue(vecCostSharingBeans.sum("amount"));
                        //COEUSQA-2452_Proposal Hierarchy quirk: errant underrecovery alert at parent budget_start
                        costSharingAmount = vecCostSharingBeans.sum("amount");
                        //COEUSQA-2452_Proposal Hierarchy quirk: errant underrecovery alert at parent budget_End
                        String message = "You have distributed "+dollarCurrencyTextField.getText()+" as cost sharing. \nThis version of the budget does not have any cost sharing. Do you want to delete the cost sharing distribution.";
                        //CoeusOptionPane.showInfoDialog(message);
                        //COEUSQA-2452_Proposal Hierarchy quirk: errant underrecovery alert at parent budget_start
                        if(!isParentProposal()){// added this if condition for COEUSQA_2452
                            int option = CoeusOptionPane.showQuestionDialog(message,CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);
                            switch(option) {
                                case ( JOptionPane.YES_OPTION ):
                                    try{
                                        int size = vecCostSharingBeans.size();
                                        ProposalCostSharingBean costSharingBean;
                                        for(int index = 0; index < size; index++) {
                                            costSharingBean = (ProposalCostSharingBean) vecCostSharingBeans.get(0);
                                            costSharingBean.setAcType(TypeConstants.DELETE_RECORD);
                                            queryEngine.delete(key, costSharingBean);
                                        }
                                        
                                    }catch(Exception e){
                                        CoeusOptionPane.showErrorDialog(e.getMessage());
                                    }
                                    break;
                            }
                        }//COEUSQA_2452_end
                        
                    }
                        
                }
                
            }
            if(!isFromUnderRecovery()) {
                //proposalIDCRateBean
                CoeusVector vecProposalIDCRateBean = queryEngine.executeQuery(key,ProposalIDCRateBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
                
                if(vecProposalIDCRateBean != null && vecProposalIDCRateBean.size() > 0) {
                    //COEUSQA-2452_Proposal Hierarchy quirk: errant underrecovery alert at parent budget_start
                    /* New instance variable ,underRecoveryAmount, is used for Case#COEUSQA-2452
                     * We are setting the Underrecovery value again to avoid the warning message displayed repeatedly
                     * when Sync from Parent is performed.i.e for first time the warning is displayed , on choosing
                     * Yes or No, it is not displayed during subsequent Sync's repeatedly.
                     */
                    if(underRecoveryAmount > 0){
                        budgetInfoBean.setUnderRecoveryAmount(underRecoveryAmount);
                    }
                    //COEUSQA-2452_Proposal Hierarchy quirk: errant underrecovery alert at parent budget_End
                    if (budgetInfoBean.getUnderRecoveryAmount() == 0) {

                        DollarCurrencyTextField dollarCurrencyTextField = new DollarCurrencyTextField();
                        dollarCurrencyTextField.setValue(vecProposalIDCRateBean.sum("underRecoveryIDC"));
                        //COEUSQA-2452_Proposal Hierarchy quirk: errant underrecovery alert at parent budget_start
                        underRecoveryAmount = vecProposalIDCRateBean.sum("underRecoveryIDC");
                        //COEUSQA-2452_Proposal Hierarchy quirk: errant underrecovery alert at parent budget_End
                        String message = "You have distributed "+dollarCurrencyTextField.getText()+" as underrecovery of indirect cost. \nThis version of the budget does not have " +"any underrecovery of indirect cost now. Do you want to delete the \nunderrecovery distribution.";
                        //COEUSQA-2452_Proposal Hierarchy quirk: errant underrecovery alert at parent budget_start
                        if(!isParentProposal()){//Added this if condition for COEUSQA_2452
                            int option = CoeusOptionPane.showQuestionDialog(message,CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);
                            
                            switch(option) {
                                case ( JOptionPane.YES_OPTION ):
                                    try{
                                        int size = vecProposalIDCRateBean.size();
                                        ProposalIDCRateBean proposalIDCRateBean;
                                        for(int index = 0; index < size; index++) {
                                            proposalIDCRateBean = (ProposalIDCRateBean) vecProposalIDCRateBean.get(0);
                                            proposalIDCRateBean.setAcType(TypeConstants.DELETE_RECORD);
                                            queryEngine.delete(key, proposalIDCRateBean);
                                        }
                                        
                                    }catch(Exception e){
                                        CoeusOptionPane.showErrorDialog(e.getMessage());
                                    }
                                    break;
                            }
                        }//COEUSQA_2452_end
                    }
                }
                
            }
            
        }catch(Exception exp) {
            exp.printStackTrace();
        }
        
    }
    
    /** Getter for property fromCostShare.
     * @return Value of property fromCostShare.
     *
     */
    public boolean isFromCostShare() {
        return fromCostShare;
    }
    
    /** Setter for property fromCostShare.
     * @param fromCostShare New value of property fromCostShare.
     *
     */
    public void setFromCostShare(boolean fromCostShare) {
        this.fromCostShare = fromCostShare;
    }
    
    /** Getter for property fromUnderRecovery.
     * @return Value of property fromUnderRecovery.
     *
     */
    public boolean isFromUnderRecovery() {
        return fromUnderRecovery;
    }
    
    /** Setter for property fromUnderRecovery.
     * @param fromUnderRecovery New value of property fromUnderRecovery.
     *
     */
    public void setFromUnderRecovery(boolean fromUnderRecovery) {
        this.fromUnderRecovery = fromUnderRecovery;
    }
    
     /** Case #1801 :Parameterize Under-recovery and cost-sharing distribution Start2
      */
    
    /**
     * Getter for property forceUnderRecovery.
     * @return Value of property forceUnderRecovery.
     */
    public boolean isForceUnderRecovery() {
        return forceUnderRecovery;
    }
    
    /**
     * Setter for property forceUnderRecovery.
     * @param forceUnderRecovery New value of property forceUnderRecovery.
     */
    public void setForceUnderRecovery(boolean forceUnderRecovery) {
        this.forceUnderRecovery = forceUnderRecovery;
    }
    
    /**
     * Getter for property forceCostSharing.
     * @return Value of property forceCostSharing.
     */
    public boolean isForceCostSharing() {
        return forceCostSharing;
    }
    
    /**
     * Setter for property forceCostSharing.
     * @param forceCostSharing New value of property forceCostSharing.
     */
    public void setForceCostSharing(boolean forceCostSharing) {
        this.forceCostSharing = forceCostSharing;
    }
    
   
  /** Case #1801 :Parameterize Under-recovery and cost-sharing distribution End2
   */
    
    
    
    
    
    
    
    
    
    
    
    /** Adjust the dates in BudgetPeriod, Budget Line Ite, Personnel Budget.
     *If the old period changed to new Period, adjust the dates in Line item dates,
     *Personnel Line Iten dates.Delete, Update or insert the dates as dates are
     *changed in the adjust period.
     */
    public void adjustBudgetDates(){
        String key = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        try{
            CoeusVector vecPeriodDetails  = null;
            
            Equals eqPropNo = new Equals("proposalNumber", budgetInfoBean.getProposalNumber());
            Equals eqVersionNo = new Equals("versionNumber", new Integer(budgetInfoBean.getVersionNumber()));
            And eqPropNoAndEqVersionNo = new And(eqPropNo, eqVersionNo);
            And eqPropNoAndEqVersionNoAndActivePeriods = new And(eqPropNoAndEqVersionNo, CoeusVector.FILTER_ACTIVE_BEANS);
            
            vecPeriodDetails = queryEngine.executeQuery(key,BudgetPeriodBean.class, eqPropNoAndEqVersionNoAndActivePeriods);
            
            
            
            CoeusVector cvLineItemDetails = new CoeusVector();
            CoeusVector cvPersonnelDetails = new CoeusVector();
            CoeusVector cvTotalLineItemDetails = new CoeusVector();
            CoeusVector cvTotalPersonnelDetails = new CoeusVector();
            CoeusVector cvOldPeriodDetails;
            cvOldPeriodDetails = queryEngine.executeQuery(key, BudgetPeriodBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            Calendar calendar = Calendar.getInstance();
            
            long periodAndLineItemStDateDifference;
            long oldLiEndDateDifference;
            
            long liAndPersonnelStDateDifference;
            long personnelStDateEndDateDiff;
            
            int periodSize = vecPeriodDetails.size();
            BudgetPeriodBean newBudgetPeriodBean;
            BudgetPeriodBean oldBudgetPeriodBean;
            BudgetDetailBean budgetDetailBean;
            BudgetPersonnelDetailsBean personnelDetailsBean;
            Equals eqBudgetPeriod;
            Equals eqLineItemNo;
            int budgetPeriod = 0;
            Date oldPeriodStDate, oldPeriodEndDate, newPeriodStDate, newPeriodEndDate;
            Date oldLiStDate, oldLiEndDate, newLiStDate=null, newLiEndDate=null;
            Date oldPersonnelStDate, oldPersonnelEndDate, newPersonnelStDate=null, newPersonnelEndDate=null;
            for (int periodIndex = 0; periodIndex < periodSize; periodIndex++) {
                
                //Get the new budget period
                newBudgetPeriodBean = (BudgetPeriodBean) vecPeriodDetails.get(periodIndex);
                newPeriodStDate = newBudgetPeriodBean.getStartDate();
                newPeriodEndDate = newBudgetPeriodBean.getEndDate();
                
                budgetPeriod = newBudgetPeriodBean.getBudgetPeriod();
                eqBudgetPeriod = new Equals("budgetPeriod", new Integer(budgetPeriod));
                
                //Check whether the period has line items
                cvLineItemDetails = queryEngine.getActiveData(key, BudgetDetailBean.class, eqBudgetPeriod);
                
                if (cvLineItemDetails != null && cvLineItemDetails.size() > 0) {
                    //Get the old budget period
                    oldBudgetPeriodBean = (BudgetPeriodBean) cvOldPeriodDetails.get(periodIndex);
                    oldPeriodStDate = oldBudgetPeriodBean.getStartDate();
                    oldPeriodEndDate = oldBudgetPeriodBean.getEndDate();
                    int liSize = cvLineItemDetails.size();
                    
                    for (int liIndex = 0; liIndex < liSize; liIndex++) {
                        budgetDetailBean = (BudgetDetailBean) cvLineItemDetails.get(liIndex);
                        oldLiStDate = budgetDetailBean.getLineItemStartDate();
                        oldLiEndDate = budgetDetailBean.getLineItemEndDate();
                        
                        /**
                         *If the Line item Start Date equals old Period start date and
                         *if line item End Date equals old Period End Date then set the
                         *new line item Start Date to new Period Start Date and
                         *new line item End Date to new Period End Date
                         */
                        if (oldPeriodStDate.compareTo(oldLiStDate) == 0 &&
                        oldPeriodEndDate.compareTo(oldLiEndDate) == 0) {
                            newLiStDate = newPeriodStDate;
                            newLiEndDate = newPeriodEndDate;
                        } else {
                            /** get the Period and Line item start dates difference and
                             *add this to the new Period start date and set the new start date
                             *to the new line item start date
                             */
                            
                            //Set Line Item Start Date
                            periodAndLineItemStDateDifference = (oldLiStDate.getTime() - oldPeriodStDate.getTime()) / 86400000;
                            
                            calendar.setTime(newPeriodStDate);
                            calendar.add(Calendar.DATE, (int)periodAndLineItemStDateDifference);
                            newLiStDate = calendar.getTime();
                            
                            if (newLiStDate.compareTo(newPeriodEndDate) > 0) {
                                newLiStDate = newPeriodStDate;
                            }
                            
                            //Set Line Item End Date
                            oldLiEndDateDifference = (oldLiEndDate.getTime() - oldLiStDate.getTime()) / 86400000;
                            calendar.setTime(newLiStDate);
                            calendar.add(Calendar.DATE, (int) oldLiEndDateDifference);
                            newLiEndDate = calendar.getTime();
                            
                            if (newLiEndDate.compareTo(newPeriodEndDate) > 0) {
                                newLiEndDate = newPeriodEndDate;
                            }
                            
                        }
                        
                        //Set the line item with new Start Date & End Dates
                        budgetDetailBean.setLineItemStartDate(new java.sql.Date(newLiStDate.getTime()));
                        budgetDetailBean.setLineItemEndDate(new java.sql.Date(newLiEndDate.getTime()));
                        
                        //Check whether the line item has personnel details
                        eqLineItemNo = new Equals("lineItemNumber",
                        new Integer(budgetDetailBean.getLineItemNumber()));
                        cvPersonnelDetails = queryEngine.getActiveData(key, BudgetPersonnelDetailsBean.class, eqLineItemNo);
                        if (cvPersonnelDetails != null && cvPersonnelDetails.size() > 0) {
                            int personnelSize = cvPersonnelDetails.size();
                            
                            for (int personnelIndex = 0; personnelIndex < personnelSize; personnelIndex++) {
                                personnelDetailsBean = (BudgetPersonnelDetailsBean) cvPersonnelDetails.get(personnelIndex);
                                oldPersonnelStDate = personnelDetailsBean.getStartDate();
                                oldPersonnelEndDate = personnelDetailsBean.getEndDate();
                                
                                /**
                                 *If the Personnel Line item Start Date equals old Line Item start date and
                                 *if Personnel line item End Date equals old Line Item End Date then set the
                                 *new Personnel line item Start Date to new Line Item Start Date and
                                 *new Personnel line item End Date to new Line Item End Date
                                 */
                                if (oldLiStDate.compareTo(oldPersonnelStDate) == 0 &&
                                oldLiEndDate.compareTo(oldPersonnelEndDate) == 0) {
                                    newPersonnelStDate = newLiStDate;
                                    newPersonnelEndDate = newLiEndDate;
                                } else {
                                    //Set Personnel Line Item Start Date
                                    liAndPersonnelStDateDifference = (oldPersonnelStDate.getTime() - oldLiStDate.getTime()) / 86400000;
                                    
                                    calendar.setTime(newLiStDate);
                                    calendar.add(Calendar.DATE, (int)liAndPersonnelStDateDifference);
                                    newPersonnelStDate = calendar.getTime();
                                    
                                    if (newPersonnelStDate.compareTo(newLiEndDate) > 0) {
                                        newPersonnelStDate = newLiStDate;
                                    }
                                    
                                    //Set Personnel Line Item End Date
                                    personnelStDateEndDateDiff = (oldPersonnelEndDate.getTime() - oldPersonnelStDate.getTime()) / 86400000;
                                    calendar.setTime(newPersonnelStDate);
                                    calendar.add(Calendar.DATE, (int) personnelStDateEndDateDiff);
                                    newPersonnelEndDate = calendar.getTime();
                                    
                                    if (newPersonnelEndDate.compareTo(newLiEndDate) > 0) {
                                        newPersonnelEndDate = newLiEndDate;
                                    }
                                }
                                
                                //Set the Personnel line item with new Start Date & End Dates
                                personnelDetailsBean.setStartDate(new java.sql.Date(newPersonnelStDate.getTime()));
                                personnelDetailsBean.setEndDate(new java.sql.Date(newPersonnelEndDate.getTime()));
                                
                            }
                            
                            if (cvPersonnelDetails.size() > 0) {
                                cvTotalPersonnelDetails.addAll(cvPersonnelDetails);
                            }
                        }
                    }
                    if (cvLineItemDetails.size() > 0) {
                        cvTotalLineItemDetails.addAll(cvLineItemDetails);
                    }
                }
            }
            updateBudgetDates(cvTotalLineItemDetails,cvTotalPersonnelDetails,key);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /** Update the respective dates, in the respective beans. Update BudgetPeriodBean,
     *budgetDetailBean, and BudgetPersonnelDetailsBean. Update these beans to the base widndow
     *through query engine
     */
    private void updateBudgetDates(CoeusVector cvLineItemDetails, CoeusVector cvPersonnelDetails,String key)
    throws CoeusException {
        
        //Update Budget Period Details to the base window
        int size = 0;
        int index = 0;
        String acType=EMPTY_STRING;
        // Update BudgetDetailBean to the base widndow. update the Line item start date and end date
        if(cvLineItemDetails!=null || cvLineItemDetails.size() > 0){
            for(int liIndex = 0 ; liIndex < cvLineItemDetails.size(); liIndex++){
                BudgetDetailBean budgetDetailBean = (BudgetDetailBean)cvLineItemDetails.get(liIndex);
                acType = budgetDetailBean.getAcType();
                if(acType==null || acType.equals(TypeConstants.UPDATE_RECORD)){
                    queryEngine.update(key, budgetDetailBean);
                } else if(acType==null || acType.equals(TypeConstants.INSERT_RECORD)){
                    queryEngine.insert(key, budgetDetailBean);
                }
            }
        }
        /* Update BudgetPersonnelDetailsBean to the base window.
         update the Personnel Line item start date and Personnel Line item end date*/
        if(cvPersonnelDetails!=null || cvPersonnelDetails.size() > 0){
            for(int pIndex = 0 ; pIndex < cvPersonnelDetails.size(); pIndex++){
                BudgetPersonnelDetailsBean budgetPersonnelDetailsBean =
                (BudgetPersonnelDetailsBean)cvPersonnelDetails.get(pIndex);
                acType = budgetPersonnelDetailsBean.getAcType();
                if(acType==null || acType.equals(TypeConstants.UPDATE_RECORD)){
                    queryEngine.update(key, budgetPersonnelDetailsBean);
                }else if(acType==null || acType.equals(TypeConstants.INSERT_RECORD)){
                    queryEngine.insert(key, budgetPersonnelDetailsBean);
                }
            }
        }
    }
    
    /**
     * Getter for property parentProposal.
     * @return Value of property parentProposal.
     */
    public boolean isParentProposal() {
        return parentProposal;
    }    
    
    /**
     * Setter for property parentProposal.
     * @param parentProposal New value of property parentProposal.
     */
    public void setParentProposal(boolean parentProposal) {
        this.parentProposal = parentProposal;
    } 
    //For Budget Modular Enhancement case #2087 start 2
    /**
     * Method to return syncModular Budget data
     *@return hashMap
     */
     public Hashtable syncModularBudget(String propNumber, int versionNumber) throws CoeusException{
        RequesterBean requester = new RequesterBean();
        Vector budgetData = new Vector();
        budgetData.addElement(propNumber);
        budgetData.addElement(new Integer(versionNumber));
        requester.setFunctionType(SYNC_MODULAR_BUDGET);
        requester.setDataObjects(budgetData);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        comm.send();
       ResponderBean responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            return  (Hashtable)responder.getDataObject();
        }else{
            throw new CoeusException(responder.getMessage());
        }
    }
    
     /**
      * Getter for property modularDataObject.
      * @return Value of property modularDataObject.
      */
     public java.util.Hashtable getModularDataObject() {
         return modularDataObject;
     }     
    
     /**
      * Setter for property modularDataObject.
      * @param modularDataObject New value of property modularDataObject.
      */
     public void setModularDataObject(java.util.Hashtable modularDataObject) {
         this.modularDataObject = modularDataObject;
     }     
    //For Budget Modular Enhancement case #2087 end 2
     
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
    /**
     * Fetch all the period types from the database
     * @return String [][] arrPeriodType
     * @throws Exception CoeusClientException
     */
    public String [][] fetchPeriodTypes() throws CoeusClientException{
        String [] [] arrPeriodType = null;
        /* JM -12-16-2013 replaced with custom code returning sorted array
        HashMap<String,String> hmdata = new HashMap<String,String>();
        RequesterBean requesterBean  = new RequesterBean();
        requesterBean.setFunctionType(GET_PERIOD_TYPE);
        String connectTo = CoeusGuiConstants.CONNECTION_URL + connectURL;
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requesterBean );
        comm.send();
        ResponderBean responderBean = comm.getResponse();
         if ( responderBean !=null ){
            if(responderBean.isSuccessfulResponse()) {
                hmdata = (HashMap)responderBean.getDataObject();
                if (hmdata  != null && hmdata.size() > 0) {
                    arrPeriodType = storeDataToArray(hmdata);
                }
            }else {
                throw new CoeusClientException(responderBean.getMessage(), CoeusClientException.ERROR_MESSAGE);
            }
         }else{
             throw new CoeusClientException(COULD_NOT_CONTACT_SERVER);
         }
        */
        arrPeriodType = fetchSortedActivePeriodTypes(); // JM 12-16-2013
        return arrPeriodType;
    }

    /**
     * Fetch the active period types from the database
     * @return String [][] arrPeriodType
     * @throws Exception CoeusClientException
     */
    public String [][] fetchActivePeriodTypes() throws CoeusClientException{
        String [] [] arrPeriodType = null;
        /* JM -12-16-2013 replaced with custom code returning sorted array        
        HashMap<String,String> hmdata = new HashMap<String,String>();
        RequesterBean requesterBean  = new RequesterBean();
        requesterBean.setFunctionType(GET_ACTIVE_PERIOD_TYPE);
        String connectTo = CoeusGuiConstants.CONNECTION_URL + connectURL;
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requesterBean );
        comm.send();
        ResponderBean responderBean = comm.getResponse();
         if ( responderBean !=null ){
            if(responderBean.isSuccessfulResponse()) {
                hmdata = (HashMap)responderBean.getDataObject();
                if (hmdata  != null && hmdata.size() > 0) {
                    arrPeriodType = storeDataToArray(hmdata);
                }
            }else {
                throw new CoeusClientException(responderBean.getMessage(), CoeusClientException.ERROR_MESSAGE);
            }
         }else{
             throw new CoeusClientException(COULD_NOT_CONTACT_SERVER);
         }
         */
        arrPeriodType = fetchSortedActivePeriodTypes(); // JM 12-16-2013
        return arrPeriodType;
    }
    
    // JM 12-16-2013 gets active period types
    /**
     * Fetch sorted active period types from the database
     * @return String [][] arrPeriodType
     * @throws Exception CoeusClientException
     */
    public String[][] fetchSortedActivePeriodTypes() throws CoeusClientException {
        edu.vanderbilt.coeus.budget.controller.Controller vController = 
        	new edu.vanderbilt.coeus.budget.controller.Controller();
        String [][] sortedPeriodTypes = vController.fetchActivePeriodTypes();
        return sortedPeriodTypes;    	
    }
    // JM END
    
    /**
     * Stores all the period types from HashMap to two dimesional array
     * @param HashMap hmPeriodData
     * @return String [][] arrPeriodData
     */
    private String [][] storeDataToArray(HashMap hmPeriodData){
        int leftincrement = 0;
        Set<Map.Entry<String, String>> setData = hmPeriodData.entrySet();
        int totSize = hmPeriodData.size();
        String [] [] arrPeriodData = new String[totSize][2];
        for(Map.Entry<String,String> mapData : setData){
            int rightincrement = 0;
            arrPeriodData[leftincrement][rightincrement] = mapData.getKey();
            rightincrement++;
            arrPeriodData[leftincrement][rightincrement] = mapData.getValue();
            leftincrement++;
        }
        return arrPeriodData;
    }
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
}
