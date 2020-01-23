/*
 * @(#)SalaryCalculator.java October 14, 2003, 12:58 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.budget.calculator;

import edu.mit.coeus.budget.bean.BudgetPeriodBean;
import edu.mit.coeus.budget.bean.BudgetPersonnelDetailsBean;
import edu.mit.coeus.budget.bean.BudgetPersonsBean;
import edu.mit.coeus.budget.bean.BudgetPersonnelCalAmountsBean;
import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
import edu.mit.coeus.budget.bean.BudgetDetailBean;
import edu.mit.coeus.budget.bean.ProposalRatesBean;
import edu.mit.coeus.budget.bean.ValidCERateTypesBean;
import edu.mit.coeus.budget.bean.SortBean;
import edu.mit.coeus.budget.calculator.bean.SalaryCalculationBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.bean.BaseBean;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;
import java.text.SimpleDateFormat;

import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;

/**
 * Calculates the Salary for the Budget person.
 * This class instance will be used for calculating the salary from budget details screen.
 * Calculate the salary while tabbing out from each Personnel Line Item after 
 * applying the proper inflation on base salary.
 * <li>To achieve this calculation, its using the following algorithm.
 * <P> Step 1:  Split this into small periods based on the effective dates of the base salary and the 
    inflation rates. Now for each base salary found do the following steps
 * <P> Step 2: If effective date of base salary is prior to start date then
	calculate inflated base salary by applying all applicable inflation rates
	to Base Salary.
 * <P> Step 3: If Inflation flag is applicable, then apply the inflation rate on Base Salary for the 
    period StartDate to TempEnddate, where TempEnddate is formed in Step 1 which indicates the EndDate for the current base salary.
 * <P> Step 4: Calculate Salary for the period StartDate to TempEndDate as described below
    Calculation is based on StartDate, TempEndDate,  & AppointmentType for the person.
 *
 *
 */
public class SalaryCalculator {

  // attributes

    /**
     * Represents Budget Personnel Details
     * 
     */
    private BudgetPersonnelDetailsBean personDetails; 

    /**
     * Represents Budget Persons
     * 
     */
    private CoeusVector cvBudgetPersons; 

    /**
     * Represents Inflation rates
     * 
     */
    private CoeusVector cvInflationRates; 

    /**
     * Represents the Salary calculation beans for each breakup interval
     * 
     */
//    private CoeusVector breakUpIntervals; 
    
    /**
     *  Holds the salary amound after the calculation for each personnel line item
     */
    private double requestedSalary; 

    /**
     *  Holds the salary amound after the calculation for each personnel line item
     */
    private double costSharing;
    
    /**
     *  Query engine instance
     */
    private static QueryEngine queryEngine;
    
    /**
     *  The instance to be used to get all the valid rate types for a cost element
     */
    private BudgetDataTxnBean budgetDataTxnBean;

    /**
     * Represents Vector containing base salary not available messages
     */
    private Vector vecMessages;  

    /**
     * Represents Vector containing salary not available messages for persons.
     * This is used to diplay while opening Budget Summary report
     */
    protected Vector vecErrMessages;
    //Added for Case 2918 � Use of Salary Anniversary Date for calculating inflation in budget development module-Start    
    private CoeusVector cvSalaryAnnivInflationRates; 
    //Added for Case 2918 � Use of Salary Anniversary Date for calculating inflation in budget development module-End
    ///////////////////////////////////////
  // operations

/**
 * <p>
 * Constructor....sets the Personnel Details
 * </p>
 * 
 * @param personDetails 
 */
    public  SalaryCalculator(BudgetPersonnelDetailsBean personDetails) 
                throws CoeusException{        
        this.personDetails = personDetails;
        budgetDataTxnBean = new BudgetDataTxnBean();
        queryEngine = QueryEngine.getInstance();
        initValues();
    } // end SalaryCalculator        

    /**
     * Initialize the data required for the calculation 
     */
    private void initValues() throws CoeusException{
        //initialize the warning message vector
        vecMessages = new Vector(2,1);
        vecErrMessages = new Vector();
        //Get the calculation bases for a particular person by using query engine
        getCalcBasesForAPerson();
        //Get the inflation rates for a proposal and the version
        getInflationRates();
        //Sort the person vector and filter out the elemnts which are out of boundary
        sortAndFilterBudgetPersons();
        //Sort the inflation rates and filter out the elements which are out of boundary
        sortAndFilterInflationRates();
        //Added for Case 2918 � Use of Salary Anniversary Date for calculating inflation in budget development module-Start    
        sortAndFilterSalAnnivInflationRates();
        //Added for Case 2918 � Use of Salary Anniversary Date for calculating inflation in budget development module-End    
    }
    /**
     *  Get the Calculation bases for a person
     */
    private void getCalcBasesForAPerson()throws CoeusException{
        //forming the key
        String key = personDetails.getProposalNumber()+
                        personDetails.getVersionNumber();
        CoeusVector cvValidBudgetPersons = queryEngine.getDetails(key,BudgetPersonsBean.class);
        //creating budget person bean for the filter
        BudgetPersonsBean fltrCndnBean = new BudgetPersonsBean();
        fltrCndnBean.setProposalNumber(personDetails.getProposalNumber());
        fltrCndnBean.setVersionNumber(personDetails.getVersionNumber());
        fltrCndnBean.setPersonId(personDetails.getPersonId());
        fltrCndnBean.setJobCode(personDetails.getJobCode());
        //filtering the valid budget persons
        cvBudgetPersons = cvValidBudgetPersons.isLike(fltrCndnBean);
        cvBudgetPersons = cvBudgetPersons.filter(CoeusVector.FILTER_ACTIVE_BEANS);
//        System.out.println("CalcBase person beans=>"+cvBudgetPersons.toString());
    }
/**
 * - Uses Query Engine to get the Inflation Rate Class(RC) &amp; Rate Type(RT)
 * - Use the above RC &amp; RT to get all the rates from Proposal Rates sorted
 * by StartDate in ascending.
 * 
 */
    private void getInflationRates() throws CoeusException{        
        String key = personDetails.getProposalNumber()+
                        personDetails.getVersionNumber();
        //fetching the budget details
        CoeusVector cvBudgetDetails = queryEngine.getDetails(key, 
                                            BudgetDetailBean.class);
        //forming the filter bean
        BudgetDetailBean filterBudgetDetailBean = new BudgetDetailBean();
        filterBudgetDetailBean.setProposalNumber(personDetails.getProposalNumber());
        filterBudgetDetailBean.setVersionNumber(personDetails.getVersionNumber());
        filterBudgetDetailBean.setBudgetPeriod(personDetails.getBudgetPeriod());
        filterBudgetDetailBean.setLineItemNumber(personDetails.getLineItemNumber());
        //filtering the required budgetdetails bean
        CoeusVector fltdBudgetDetails = cvBudgetDetails.isLike(filterBudgetDetailBean);
        fltdBudgetDetails = fltdBudgetDetails.filter(CoeusVector.FILTER_ACTIVE_BEANS);
//        System.out.println("budget details=>"+fltdBudgetDetails.toString());
        BudgetDetailBean budgetDetailBean = (BudgetDetailBean)fltdBudgetDetails.elementAt(0);
        //fetching valid rate types
        CoeusVector validRateTypes = this.getValidRateTypes(budgetDetailBean.getCostElement());
        //making filter bean just fetch only inflation rates types
        ValidCERateTypesBean rateTypeBean = new ValidCERateTypesBean();
        rateTypeBean.setRateClassType("I");
        //filtering inflation rates
        // Bug Fix #1902 - start
        if(validRateTypes!= null && validRateTypes.size() > 0){
            // Bug Fix #1902 - End
            CoeusVector cvValidInflationRate = validRateTypes.isLike(rateTypeBean);

            if(cvValidInflationRate!= null && cvValidInflationRate.size()> 0){

                ValidCERateTypesBean validRateTypesBean = 
                                        (ValidCERateTypesBean)cvValidInflationRate.elementAt(0);
        //        System.out.println("Valid rate types=>"+validRateTypesBean.toString());
                //fetching the proposal rates for the cost elemnt
        //        CoeusVector cvPropRates = queryEngine.getDetails(key, ProposalRatesBean.class);
        //        System.out.println("before filtering the rates=>"+cvPropRates.toString());

                Equals equalsRC = new Equals("rateClassCode", new Integer(validRateTypesBean.getRateClassCode()));
                Equals equalsRT = new Equals("rateTypeCode", new Integer(validRateTypesBean.getRateTypeCode()));
        //        Equals equalsRCT = new Equals("rateClassType", validRateTypesBean.getRateClassType());

                And RCandRT = new And(equalsRC, equalsRT);
        //        And RCandRTandRCT = new And(RCandRT,equalsRCT);

                boolean onOffCampusFlag = budgetDetailBean.isOnOffCampusFlag();
                if(budgetDetailBean.isApplyInRateFlag()){
                    Equals equalsOnOff = new Equals("onOffCampusFlag", onOffCampusFlag);
                    And RCRTandOnOff = new And(RCandRT, equalsOnOff);
                    cvInflationRates = queryEngine.getActiveData(key, 
                                                 ProposalRatesBean.class, RCRTandOnOff);
                    //Added for Case 2918 � Use of Salary Anniversary Date for calculating inflation in budget development module-Start
                    cvSalaryAnnivInflationRates = queryEngine.getActiveData(key, 
                                                 ProposalRatesBean.class, RCRTandOnOff);                    
                }else{
                    cvInflationRates = new CoeusVector();
                    cvSalaryAnnivInflationRates = new CoeusVector();
                    //Added for Case 2918 � Use of Salary Anniversary Date for calculating inflation in budget development module-End
                }
            }
        }
//        System.out.println("fltd rates vector=>"+cvInflationRates.toString());
        
    } // end getInflationRates        

    /**
     *  Method to fetch the valid rate types from database
     */
    private CoeusVector getValidRateTypes(String costElement) throws BudgetCalculateException{
        CoeusVector validRateTypes = null;
        try{
            validRateTypes = budgetDataTxnBean.getValidCERateTypes(costElement);
        }catch(DBException dbEx){
            throw new BudgetCalculateException(dbEx.getMessage());
        }catch(CoeusException coeusEx){
            throw new BudgetCalculateException(coeusEx.getMessage());
        }
        return validRateTypes;
    }
/**
 * - Sort all the Budget Persons based on Effective Date in ascending
 * - Filter out all base salaries whose Eff. Date lies outside the Start &amp;
 * End dates of the Person Details. Make sure to include latest base salary
 * that comes just before the Start Date.
 * 
 */
    private void sortAndFilterBudgetPersons() throws BudgetCalculateException{
        cvBudgetPersons.sort("effectiveDate");
        cvBudgetPersons = filter(cvBudgetPersons,"effectiveDate",true);
    } // end sortAndFilterBudgetPersons    
    
    
/**
 * - Sort all the Inflation rates based on Start Date in ascending
 * - Filter out all Inflation rates whose Start Date lies outside the Start
 * &amp; End dates of the Person Details. If the Effective date of the 1st Base
 * Salary comes before the Start Date for Person, then make sure to include
 * all the rates coming after Effective Date.
 * 
 */
    private void sortAndFilterInflationRates() throws BudgetCalculateException{        
        if(cvInflationRates!= null && cvInflationRates.size() > 0){
            cvInflationRates.sort("startDate");
            cvInflationRates = filter(cvInflationRates,"startDate");
        }
    } // end sortAndFilterInflationRates        

    /**
     * Method to filter out the data which is out of boundary. And include 
     * the element which is just before the start date of the personnel details.
     */
    private CoeusVector filter(CoeusVector listToBeFiltered, 
                                String filterDateString, boolean personFlag)
                    throws BudgetCalculateException{
        boolean noCalcBase = false;
        Date startPersonEffDate = null;
        if(cvBudgetPersons.isEmpty()){
            startPersonEffDate = personDetails.getStartDate();
            noCalcBase = true;
        }else{
            startPersonEffDate = ((BudgetPersonsBean)cvBudgetPersons.firstElement()).getEffectiveDate();
        }
        Date personStartDate = personFlag?personDetails.getStartDate():startPersonEffDate;
        Date personEndDate = personDetails.getEndDate();
        CoeusVector cvFltdVector = new CoeusVector();
        /*
         *  Forming a condition for getting all the lements which is lesser than or
         *  equals to the start date. Sort the filetered vector, get the last element 
         *  and add that as the first element in the main filtered elements.
         */
//        LesserThan lesserThanStDt = new LesserThan(filterDateString, personStartDate);
//        Equals lesserEqualsStDt = new Equals(filterDateString, personStartDate);
//        Or lesserDatesOr = new Or(lesserThanStDt, lesserEqualsStDt);
//        
//        CoeusVector cvFltdLesserElements = listToBeFiltered.filter(lesserDatesOr);
//        cvFltdLesserElements.sort(filterDateString);
//        
//        boolean noCalcBase = false;
//        if(cvFltdLesserElements.isEmpty()){
//            noCalcBase = true;
//        }else{
//            cvFltdVector.add(cvFltdLesserElements.lastElement());
//        }
        if(personFlag){
            LesserThan lesserThanStDt = new LesserThan(filterDateString, personStartDate);
            Equals lesserEqualsStDt = new Equals(filterDateString, personStartDate);
            Or lesserDatesOr = new Or(lesserThanStDt, lesserEqualsStDt);

            CoeusVector cvFltdLesserElements = listToBeFiltered.filter(lesserDatesOr);
            cvFltdLesserElements.sort(filterDateString);
        
            if(cvFltdLesserElements.isEmpty()){
                noCalcBase = true;
            }else{
                cvFltdVector.add(cvFltdLesserElements.lastElement());
            }
        }
        
        /*
         *  Forming the filter condition for fetching the elements which are in between 
         *  the personnel start date and end date. And add the filtered elements to 
         *  actual one.
         */
        GreaterThan greaterThanStDt = new GreaterThan(filterDateString, personStartDate);
        LesserThan lesserThan = new LesserThan(filterDateString, personEndDate);
        Equals lesserEquals = new Equals(filterDateString, personEndDate);
        Or endDateOr = new Or(lesserThan, lesserEquals);
        And and = new And(greaterThanStDt,endDateOr);
        cvFltdVector.addAll(cvFltdVector.size(),listToBeFiltered.filter(and));
        /*
         *  Forming a warning message if there is no calculation base before the start date
         */ 
        if(personFlag && noCalcBase){
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            StringBuffer warningMsg = new StringBuffer("Base salary information is not available for the person ");
            StringBuffer errMsg = new StringBuffer("Error finding the calculation base for the person ");
            errMsg.append(this.personDetails.getPersonId());
            errMsg.append(" with Job Code ");
            errMsg.append(this.personDetails.getJobCode());
            
            warningMsg.append(this.personDetails.getFullName());
            warningMsg.append(" with Job Code ");
            warningMsg.append(this.personDetails.getJobCode());
            warningMsg.append(" for the period ");
            warningMsg.append(dateFormat.format(personDetails.getStartDate()));
            warningMsg.append(" to ");
            if(!cvFltdVector.isEmpty()){
                warningMsg.append(dateFormat.format(this.minus(
                                        ((BudgetPersonsBean)cvFltdVector.
                                        firstElement()).getEffectiveDate(),1)));
            }else{
                warningMsg.append(dateFormat.format(personDetails.getEndDate()));
            }
            warningMsg.append(".\n");
            warningMsg.append("Salary for this period will be set to 0.\n");
            warningMsg.append("Please make changes to budget person details and recalculate the budget.");
            this.vecMessages.addElement(warningMsg.toString());
            
            this.vecErrMessages.addElement(errMsg.toString());
        }
        
        return cvFltdVector;
    }
    /**
     *  Overidden method to filter the coeus vector without the person flag
     */
    private CoeusVector filter(CoeusVector listToBeFiltered, String filterDateString)
                    throws BudgetCalculateException{
        return this.filter(listToBeFiltered, filterDateString, false);
    }
/**
 * -Uses sorted Budget Persons list and Inflation rates list to create
 * salary breakup periods, each period consisting of a SalaryCalculationBean
 * -Call calculate method of each bean to calculate salary
 * 
 */
    private CoeusVector createSalBreakupIntervals() {        
        CoeusVector combinedVector = new CoeusVector();
        combinedVector.addAll(this.cvBudgetPersons);
        if(this.cvInflationRates!= null && this.cvInflationRates.size()> 0){
            combinedVector.addAll(cvBudgetPersons.size(),this.cvInflationRates);
        }
        /*
         *  Sorting the combined list of BudgetPersonsBean and ProposalRatesBean 
         * in ascending order
         */
        sortCombinedList(combinedVector,true);
        //for storing effective date and inflation rate
        Date effectiveDate = null,inflStartDate = null;
        Date personStartDate = personDetails.getStartDate();
        Date personEndDate = personDetails.getEndDate();
        //initializing the break up intervals
        CoeusVector breakUpIntervals = new CoeusVector();
        boolean started = false;
        boolean personFlag = false;
        Date baseStartDate = null;
        /*
         *  Creating an instance for storing the base values
         */
        SalaryCalculationBean salaryCalcBaseBean = new SalaryCalculationBean();
        /*
         *  Creating an instance for storing next values
         */
        SalaryCalculationBean nextSalaryCalcBean = new SalaryCalculationBean();
        
        BudgetPersonsBean person = null;
        ProposalRatesBean propRate = null;
        /*
         *  Looping for creating the break up intervals
         */
        boolean setCalcBaseFlag = false;
        boolean setEffectiveFlag = false;
        
        for(int index=0;index<combinedVector.size();index++){
            Date startDate,endDate,brkUpStartDate,brkUpEndDate = null,nextStartDate;
            //checking whether the person has got any calculation base before
            //start date. If there is no cal base, keep that as a warning message 
            // and throw to the users at the end.
            BaseBean baseBean = (BaseBean)combinedVector.elementAt(index);
//            Boundary boundary = new Boundary();
            /*
             *  Temporary calculation bean used for swaping purpose
             */
            SalaryCalculationBean salaryCalcBean = new SalaryCalculationBean();
            if(baseBean instanceof ProposalRatesBean){
                propRate = (ProposalRatesBean)baseBean;
                inflStartDate = propRate.getStartDate();
                if(effectiveDate!=null && effectiveDate.compareTo(inflStartDate)==0)
                    continue;
                personFlag = false;
            }else{//it will be an instance of BudgetPersonBean
                person = (BudgetPersonsBean)baseBean;
                effectiveDate = person.getEffectiveDate();
                personFlag = true;
            }
            if(effectiveDate==null){//continue till gets the effective date
                continue;
            }
            /*
             *  Check for the first effective date to get the base salary.
             *  Once it gets the base salary, it sets the corresponding bean details
             *  to base calulation bean and start breaking up the periods to smaller ones
             */
            
            if(!started || 
                    (!personFlag && inflStartDate!=null && personStartDate.compareTo(inflStartDate)>=0 )){
                        
                int compareInflEff = 0,comparePersonInfl = 0;
                // If no inlfation is not present then don't get.
                if(cvInflationRates!= null && cvInflationRates.size()> 0){
                Date firstInflDate = cvInflationRates.isEmpty()?null:
                        ((ProposalRatesBean)cvInflationRates.elementAt(0)).getStartDate();
                }
                int comparePersonEff = personStartDate.compareTo(effectiveDate);
                /*
                 *  If effective date is greater than person start date, set effective date
                 *  as actual start date
                 */
                startDate = comparePersonEff>=0?personStartDate:effectiveDate;
                endDate = personEndDate;
                if(inflStartDate!=null){
                    compareInflEff = inflStartDate.compareTo(effectiveDate);
                    comparePersonInfl = personStartDate.compareTo(inflStartDate);
                }/*else if(firstInflDate!=null && startDate.compareTo(firstInflDate)>=0){
                    continue;
                }
                  */
                salaryCalcBaseBean.setAppointmentType(person.getAppointmentType());
                
                if(compareInflEff>0){
                    if(comparePersonEff>0 && comparePersonInfl >=0){
                        double actualBaseSal = setCalcBaseFlag?
                            salaryCalcBaseBean.getActualBaseSalary():person.getCalculationBase();
                        salaryCalcBaseBean.setActualBaseSalary(
                                this.calculateBaseSalary(actualBaseSal,
                                propRate.getApplicableRate()));
                        setCalcBaseFlag = true;
                    }
                }else{
                    salaryCalcBaseBean.setActualBaseSalary(person.getCalculationBase());
                }
                
//                setEffectiveFlag = personFlag;
                
                salaryCalcBaseBean.setBoundary(new Boundary(startDate,endDate));
                nextStartDate = startDate;
                copy(salaryCalcBaseBean,salaryCalcBean);
                copy(salaryCalcBaseBean,nextSalaryCalcBean);
                started = true;
                continue;
            }
//            else if(!started){//continue till gets the effective date
//                continue;
//            }
            else{//start breaking up once the base bean is formed
                
                if(breakUpIntervals.isEmpty()){
                    copy(salaryCalcBaseBean,salaryCalcBean);
                    copy(salaryCalcBaseBean,nextSalaryCalcBean);
                    baseStartDate = salaryCalcBean.getBoundary().getStartDate();
                }else{
//                    SalaryCalculationBean lastAddedBean = 
//                            (SalaryCalculationBean)breakUpIntervals.lastElement();
                    copy(nextSalaryCalcBean,salaryCalcBean);
                    baseStartDate = salaryCalcBean.getBoundary().getStartDate();
//                    baseStartDate = plus(salaryCalcBean.getBoundary().getEndDate(),1);
                }
                if(personFlag){//it will be an instance of BudgetPersonBean
                    if(effectiveDate.compareTo(baseStartDate)>0){
                        //brkUpStartDate = nextStartDate;
                        brkUpEndDate = minus(effectiveDate,1);
                    }
                    nextSalaryCalcBean.setBoundary(new Boundary(effectiveDate,null));
                    nextSalaryCalcBean.setActualBaseSalary(person.getCalculationBase());
                    nextSalaryCalcBean.setAppointmentType(person.getAppointmentType());
                    //salaryCalcBean.setBoundary(new Boundary(brkUpStartDate,brkUpEndDate));
                }else{
                    int compareInflBaseStart = inflStartDate.compareTo(baseStartDate);
                    if(compareInflBaseStart>=0){
                        if(compareInflBaseStart>0){
                            brkUpEndDate = minus(inflStartDate,1);
                        }
                        nextSalaryCalcBean.setBoundary(new Boundary(inflStartDate,null));
                        nextSalaryCalcBean.setActualBaseSalary(calculateBaseSalary(
                                salaryCalcBean.getActualBaseSalary(),
                                propRate.getApplicableRate()));
                    }
                }
            }
            Boundary brkUpBoundary = null;
            if(brkUpEndDate!=null){
                brkUpBoundary = new Boundary(salaryCalcBean.getBoundary().getStartDate(),brkUpEndDate);
                salaryCalcBean.setBoundary(brkUpBoundary);
                breakUpIntervals.add(salaryCalcBean);
            }
        }//end for loop
        if(nextSalaryCalcBean!=null && nextSalaryCalcBean.getBoundary()!=null &&
                nextSalaryCalcBean.getBoundary().getStartDate().compareTo(personEndDate)<=0){
             nextSalaryCalcBean.setBoundary(new Boundary(nextSalaryCalcBean.getBoundary().getStartDate(),
                personEndDate));
             breakUpIntervals.add(nextSalaryCalcBean);
        }
//        System.out.println("Break up intervals=>\n"+breakUpIntervals.toString());
        return breakUpIntervals;
        
    } // end createSalBreakupIntervals   
    

    /**
     *  Copy the contents from one bean to another
     */
    private void copy(SalaryCalculationBean source, SalaryCalculationBean dest){
        if(source.getAppointmentType()!=null) dest.setAppointmentType(source.getAppointmentType());
        if(source.getBoundary()!=null) dest.setBoundary(source.getBoundary());
        if(source.getActualBaseSalary()!=-1) dest.setActualBaseSalary(source.getActualBaseSalary());
        if(source.getCalculatedSalary()!=-1) dest.setCalculatedSalary(source.getCalculatedSalary());
    }
    
    /**
     *  Calculate the salary by using base salary and inflation rate
     */
    private double calculateBaseSalary(double baseSalary,double inflationRate){
        return (baseSalary+((baseSalary*inflationRate)/100));
    }
    /**
     *  Subtract no of days from the given date
     */
    private Date minus(Date date,int days){
        long millis = date.getTime();
	Calendar cal  = Calendar.getInstance();
        cal.setTimeInMillis(millis-(days*24*60*60*1000));
        return cal.getTime();
        //Commented by Geo to fix the salary calculation
//        return new Date((millis-(days*24*60*1000)));
    }
    /**
     *  Add the no of days to the given date
     */
//    private Date plus(Date date, int days){
//        long millis = date.getTime();
//        return new Date((millis+(days*24*60*1000)));
//    }
    /*
     *  Sort the list by taking the common filter string <code>filetrToBeSorted</code>
     */
    private void sortCombinedList(CoeusVector combinedList , boolean ascending){
        int compareValue = 0;
        for(int index = 0; index < combinedList.size()-1; index++) {
            for(int nextIndex = index+1; nextIndex < combinedList.size(); nextIndex++){
                SortBean current  = (SortBean)combinedList.get(index);
                SortBean next = (SortBean)combinedList.get(nextIndex);
                compareValue = ((Comparable)current.getFieldToBeSorted()).compareTo((Comparable)next.getFieldToBeSorted());
                if(ascending && compareValue > 0) {
                    SortBean temp = (SortBean)combinedList.get(index);
                    combinedList.set(index, combinedList.get(nextIndex));
                    combinedList.set(nextIndex, temp);
                }else if(! ascending && compareValue < 0) {
                    SortBean temp = (SortBean)combinedList.get(index);
                    combinedList.set(index, combinedList.get(nextIndex));
                    combinedList.set(nextIndex, temp);
                }
            }//End For - Inner
        }//End For - Outer
        
    }
    
    /**
     *  Calculate total salary
     *
     */
    public double calculateTotalSalary(){
                 
         double totalSalary = 0.0d;
         Integer isSalaryAnnivEnable = null;
          BudgetPersonsBean personPersonBean = null;
          CoeusVector breakUpIntervals = null;
        //Modified for Case 2918 � Use of Salary Anniversary Date for calculating inflation in budget development module-Start 
        try{
          Hashtable htSalaryInfo = getSalaryAnniversaryParameterInfo();
          if(htSalaryInfo !=null && htSalaryInfo.size() > 0 && this.cvBudgetPersons !=null && this.cvBudgetPersons.size() > 0){
               isSalaryAnnivEnable = (Integer) htSalaryInfo.get(CoeusConstants.ENABLE_SALARY_INFLATION_ANNIV_DATE);
               personPersonBean = (BudgetPersonsBean) this.cvBudgetPersons.get(0);
          }          
          if( isSalaryAnnivEnable !=null && isSalaryAnnivEnable.equals(new Integer(1)) && personPersonBean.getSalaryAnniversaryDate() !=null ){
              breakUpIntervals = createSalaryAnnivBreakupIntervals();
          }else{
              breakUpIntervals = createSalBreakupIntervals();
          }
          //Modified for Case 2918 � Use of Salary Anniversary Date for calculating inflation in budget development module-End
//        CoeusVector breakUpIntervals = createSalBreakupIntervals();
           
        for(int index=0;index<breakUpIntervals.size();index++){
            SalaryCalculationBean salCalcBean = (SalaryCalculationBean)breakUpIntervals.elementAt(index);
//            salCalcBean.calculateSalary();
            totalSalary+=salCalcBean.calculateSalary();
        }
        // Added and Commented by chandra to Fix #1102 - 10-Sept-2004 - start
         costSharing = totalSalary*(personDetails.getCostSharingPercent())/100;
//        if (personDetails.getPercentEffort()-personDetails.getPercentCharged() > 0) {
//            costSharing = totalSalary*(personDetails.getPercentEffort() - personDetails.getPercentCharged())/100;
//        } else {
//            costSharing = 0;
//        }
        }catch(Exception e){
            e.printStackTrace();
        }
        
   // Added by chandra to Fix #1102 - 10-Sept-2004 - End    
        
        double charged = this.personDetails.getPercentCharged();
        this.requestedSalary = truncate(totalSalary*charged/100,2);
//        System.out.println("Total salary=>"+totalSalary);
        return requestedSalary;
    }
    
/**
 * - Sum up all the breakup salaries and apply the %Charged, then set the
 * Requested Salary for the Person Details, apply %CostSharing to set the
 * Cost Sharing amount
 * 
 */
    public void distributeCalculatedSal() {        
        // your code here
//        double charged = this.personDetails.getPercentCharged();
        calculateTotalSalary();
//        System.out.println("before rounding"+Math.round(this.requestedSalary*(charged/100)*100));
        personDetails.setSalaryRequested(this.requestedSalary);
        personDetails.setCostSharingAmount(costSharing);
//        System.out.println("Person details after calculation=>"+personDetails.toString());
    } // end distributeCalculatedSal        
    
    public BudgetPersonnelDetailsBean getPersonAfterCalculation(){
        distributeCalculatedSal();
        return this.personDetails;
    }
    private double calculateCostSharingAmt(){
        // Added by chandra to Fix #1102 - 10-Sept-2004 - Start
//        return requestedSalary*(personDetails.getPercentEffort()-personDetails.getPercentCharged())/100;
          return requestedSalary*(personDetails.getCostSharingPercent())/100;
          // Added by chandra to Fix #1102 - 10-Sept-2004 - End
    }
    public static double truncate(double value,int decimalPos){
//        System.out.println("value"+(value*Math.pow(10.0,decimalPos)));
        return ((double)Math.round(value*Math.pow(10.0,decimalPos)))/100;
//        return value;
    }
    
    
 //Added for Case 2918 � Use of Salary Anniversary Date for calculating inflation in budget development module-Start 
 /*** Uses sorted Budget Persons list , Inflation rates list and Salary Anniversary Date to create
 * Salary breakup periods, each period consisting of a SalaryCalculationBean
 * Call calculate method of each bean to calculate salary
 * */
    private CoeusVector createSalaryAnnivBreakupIntervals()throws CoeusException {
        CoeusVector combinedVector = new CoeusVector();
        combinedVector.addAll(this.cvBudgetPersons);
        if(this.cvSalaryAnnivInflationRates!= null && this.cvSalaryAnnivInflationRates.size()> 0){
            sortCombinedList(this.cvSalaryAnnivInflationRates, true);
            combinedVector.addAll(cvBudgetPersons.size(),this.cvSalaryAnnivInflationRates);
        }
        /*
         *  Sorting the combined list of BudgetPersonsBean and ProposalRatesBean
         * in ascending order
         */
//        sortCombinedList(combinedVector,true);
        //for storing effective date and inflation rate
        Date effectiveDate = null,inflStartDate = null  , inflEndDate = null;
        Date personStartDate = personDetails.getStartDate();
        Date personEndDate = personDetails.getEndDate();       
        Date salaryAnnivDate = null;   
        //COEUSQA-3945
        Date persnSalaryAnnivDate = null;
        //COEUSQA-3945
        //initializing the break up intervals
        CoeusVector breakUpIntervals = new CoeusVector();
        boolean started = false;
        boolean personFlag = false;
        Date baseStartDate = null;
        /*
         *  Creating an instance for storing the base values
         */
        SalaryCalculationBean salaryCalcBaseBean = new SalaryCalculationBean();
        /*
         *  Creating an instance for storing next values
         */
        SalaryCalculationBean nextSalaryCalcBean = new SalaryCalculationBean();        
        BudgetPersonsBean person = null;
        ProposalRatesBean propRate = null;
        /*
         *  Looping for creating the break up intervals
         */
        boolean setCalcBaseFlag = false;
        boolean setEffectiveFlag = false;
        HashMap hmRatesApplied = new HashMap();
        for(int index=0;index<combinedVector.size();index++){
            Date startDate,endDate,brkUpStartDate,brkUpEndDate = null,nextStartDate;
            Date filterAnnInfDate = null;
            //checking whether the person has got any calculation base before
            //start date. If there is no cal base, keep that as a warning message
            // and throw to the users at the end.
            BaseBean baseBean = (BaseBean)combinedVector.elementAt(index);
            // Temporary calculation bean used for swaping purpose
            
            SalaryCalculationBean salaryCalcBean = new SalaryCalculationBean();
            if(baseBean instanceof ProposalRatesBean){
                propRate = (ProposalRatesBean)baseBean;
                inflStartDate = propRate.getStartDate();
//                if(effectiveDate!=null && effectiveDate.compareTo(inflStartDate)==0)  
//                    continue;
                personFlag = false;
            }else{//it will be an instance of BudgetPersonBean
                person = (BudgetPersonsBean)baseBean;
                effectiveDate = person.getEffectiveDate();                
                salaryAnnivDate = person.getSalaryAnniversaryDate();
                //COEUSQA-3945
                persnSalaryAnnivDate = person.getSalaryAnniversaryDate();
                //COEUSQA-3945
                int effDateYear = getSalAnnivDateYear(effectiveDate);
                salaryAnnivDate = mergeSalAnnivDateYear(salaryAnnivDate, effDateYear);
                personFlag = true;
            }
            //continue till gets the effective date
            if(effectiveDate==null){
                continue;
            }
            // Modified for Case 4315 - Problem in salary anniversary date functionality - Start
            // Check Inflation Start Date should be greater than or equal to Effective Date
//            if(inflStartDate!=null && inflStartDate.compareTo(effectiveDate) >=0 ){
             if(inflStartDate!=null ){
                int inflStartYear = getSalAnnivDateYearWithPersonDate(inflStartDate, salaryAnnivDate, effectiveDate, personStartDate, personEndDate);
                salaryAnnivDate = mergeSalAnnivDateYear(salaryAnnivDate, inflStartYear);               
            }
            // Modified for Case 4315 - Problem in salary anniversary date functionality - End
            /*
             *  Check for the first Effective date and Salary Anniversary date to get the base salary.
             *  Once it gets the base salary, it sets the corresponding bean details
             *  to base calulation bean and start breaking up the periods to smaller ones
             */
            if(!started ||
                    (!personFlag && inflStartDate!=null && personStartDate.compareTo(inflStartDate)>=0 && personStartDate.compareTo(salaryAnnivDate) >= 0)){
                //Modified for Case 4177 - Period 1 not inflating with project start date in budget development module -Start 
                int compareInflEff = -1,comparePersonInfl = 0;
                int compareInflAnnv = 0, comparePersonAnnv = 0;
                int   compareInflStartEndEffective = -1;
                //COEUSQA-3945
                int comparePerSalAnnDateSalAnniDate = -1;
                comparePerSalAnnDateSalAnniDate = salaryAnnivDate.compareTo(persnSalaryAnnivDate);
                //COEUSQA-3945
                Date filterSalaryAnnInfDate = null;
                int comparePersonEff = personStartDate.compareTo(effectiveDate);
                /*
                 *  If effective date is greater than person start date, set effective date
                 *  as actual start date
                 */
                startDate = comparePersonEff>=0?personStartDate:effectiveDate;
                endDate = personEndDate;
                int compareSalAnnivDateAndInflStrDate = 1;  
                int compareAnnivDateFilterInflDate = 0;
               // Added for Case 4315 - Problem in salary anniversary date functionality - Start
                int compareAnnivEffDate = -1;
               // Added for Case 4315 - Problem in salary anniversary date functionality - End
                if(inflStartDate!=null){
                    compareInflEff = inflStartDate.compareTo(effectiveDate);
                    inflEndDate    = getInflEndDate(inflStartDate); 
                    compareInflStartEndEffective =  (inflStartDate.compareTo(effectiveDate)>=0 ? 1 : inflEndDate.compareTo(effectiveDate));
                    
                    comparePersonInfl = personStartDate.compareTo(inflStartDate);
                    comparePersonAnnv = personStartDate.compareTo(salaryAnnivDate);
                    compareInflAnnv = inflStartDate.compareTo(salaryAnnivDate);
                    filterSalaryAnnInfDate = getNearestInflationDate(salaryAnnivDate);
                  // Added for Case 4315 - Problem in salary anniversary date functionality - Start
                    compareAnnivEffDate = salaryAnnivDate.compareTo(effectiveDate);
                  // Added for Case 4315 - Problem in salary anniversary date functionality - End
                    if(filterSalaryAnnInfDate !=null){
                        compareSalAnnivDateAndInflStrDate = filterSalaryAnnInfDate.compareTo(inflStartDate);
                        compareAnnivDateFilterInflDate = salaryAnnivDate.compareTo(filterSalaryAnnInfDate);
                    }
                }
                salaryCalcBaseBean.setAppointmentType(person.getAppointmentType());
                // To Apply Anniversary Salary Inflation, Check Inflation start date should be greater than Effective date and
                // PersonStartDate should be greater than Anniversary Date
//                if(compareInflEff>=0 && comparePersonAnnv >=0){
                if(comparePersonAnnv >=0){
                    // Modified 4177 - Period 1 not inflating with project start date in budget development module -End
                    // To Apply Rates to Salary by PersonStartDate should be greater than Effective Date,
                    // PersonStartdate should be greater than or equal to Infolation date and
                    //Inflation should be lessthan or equal to Anniversary Date. && compareInflEff > 0
                     // Added for Case 4315 - Problem in salary anniversary date functionality  
                     // Inflate only if Salary Anniversary date should be greater than Effective date
                    if(comparePersonEff>0 && comparePersonInfl >=0 && compareInflAnnv <=0 && compareSalAnnivDateAndInflStrDate == 0 && compareAnnivEffDate>0 && compareInflStartEndEffective >= 1){
                        double actualBaseSal = setCalcBaseFlag?
                            salaryCalcBaseBean.getActualBaseSalary():person.getCalculationBase();
                        salaryCalcBaseBean.setActualBaseSalary(
                                this.calculateBaseSalary(actualBaseSal,
                                propRate.getApplicableRate()));
                        hmRatesApplied.put(inflStartDate,inflStartDate);
                        setCalcBaseFlag = true;
                    }// If rates not available for current year,  availble only for previous year and Need to that apply the rate
                            else if(compareSalAnnivDateAndInflStrDate < 0 && compareAnnivDateFilterInflDate > 0 && hmRatesApplied.get(filterSalaryAnnInfDate) == null && compareAnnivEffDate> 0){
                               double actualBaseSal = setCalcBaseFlag?
                               salaryCalcBaseBean.getActualBaseSalary():person.getCalculationBase();
                                Equals eqInflStartDate = new Equals("startDate", filterSalaryAnnInfDate);
                                CoeusVector cvfilterRateBase = this.cvSalaryAnnivInflationRates.filter(eqInflStartDate);
                                ProposalRatesBean propRatesBean = null;
                                if (cvfilterRateBase !=null && cvfilterRateBase.size() >0){
                                    propRatesBean = (ProposalRatesBean) cvfilterRateBase.get(0);
                                    salaryCalcBaseBean.setActualBaseSalary(calculateBaseSalary(
                                            actualBaseSal, propRatesBean.getApplicableRate()));
                                    hmRatesApplied.put(filterSalaryAnnInfDate,filterSalaryAnnInfDate);
                                    setCalcBaseFlag = true;
                                }                                
                            }else if(hmRatesApplied == null || hmRatesApplied.isEmpty()){
                             salaryCalcBaseBean.setActualBaseSalary(person.getCalculationBase());
                            }
                }else{ // If false, set Calculation base without applying any rates.
                    salaryCalcBaseBean.setActualBaseSalary(person.getCalculationBase());
                }                
                salaryCalcBaseBean.setBoundary(new Boundary(startDate,endDate));
                nextStartDate = startDate;
                copy(salaryCalcBaseBean,salaryCalcBean);
                copy(salaryCalcBaseBean,nextSalaryCalcBean);
                started = true;
                continue;
            } else{//start breaking up once the base bean is formed
                if(breakUpIntervals.isEmpty()){
                    copy(salaryCalcBaseBean,salaryCalcBean);
                    copy(salaryCalcBaseBean,nextSalaryCalcBean);
                    baseStartDate = salaryCalcBean.getBoundary().getStartDate();
                }else{                    
                    copy(nextSalaryCalcBean,salaryCalcBean);
                    baseStartDate = salaryCalcBean.getBoundary().getStartDate();                    
                }
                if(personFlag){//it will be an instance of BudgetPersonBean  /// This condition need to taken care
                    if(effectiveDate.compareTo(baseStartDate)>0){
                        brkUpEndDate = minus(effectiveDate,1);
                    }
                    nextSalaryCalcBean.setBoundary(new Boundary(effectiveDate,null));
                    nextSalaryCalcBean.setActualBaseSalary(person.getCalculationBase());
                    nextSalaryCalcBean.setAppointmentType(person.getAppointmentType());
                }else{
                     // Added for Case 4315 - Problem in salary anniversary date functionality - Start
                     // Get the Salary Anniverssary Year based on Person start date and end date.
                    int inflStartYear = getSalAnnivDateYearWithPersonDate(inflStartDate, salaryAnnivDate, effectiveDate,personStartDate, personEndDate);
                     // Added for Case 4315 - Problem in salary anniversary date functionality - End
                    salaryAnnivDate = mergeSalAnnivDateYear(salaryAnnivDate, inflStartYear);
                    filterAnnInfDate = getNearestInflationDate(salaryAnnivDate);                    
                    int compareAnnvBaseStart = salaryAnnivDate.compareTo(baseStartDate);
                    int compareInflBaseStart = inflStartDate.compareTo(baseStartDate);
                    int compareInflAnnv = inflStartDate.compareTo(salaryAnnivDate);
                    int compareAnnvPersonEndDate = salaryAnnivDate.compareTo(personEndDate);
                    int comparefilterDateAndInflStrDate = 1;
                   //Added for Case 4177 - Period 1 not inflating with project start date in budget development module -Start
                    if(effectiveDate!=null && effectiveDate.compareTo(salaryAnnivDate)==0)  
                      continue;
                    //Added for Case 4177 - Period 1 not inflating with project start date in budget development module -End
                    if(filterAnnInfDate !=null){
                        comparefilterDateAndInflStrDate = filterAnnInfDate.compareTo(inflStartDate);
                    }                    
                    if(compareAnnvBaseStart>=0 && compareAnnvPersonEndDate <=0){
                        if(compareAnnvBaseStart>0){
                            brkUpEndDate = minus(salaryAnnivDate,1);
                        }
                        nextSalaryCalcBean.setBoundary(new Boundary(salaryAnnivDate,null));
                        if(comparefilterDateAndInflStrDate <= 0 ){
                            //If filter rates is available rates and apply the rate
                            if(compareInflAnnv <=0 &&comparefilterDateAndInflStrDate == 0){
                                nextSalaryCalcBean.setActualBaseSalary(calculateBaseSalary(
                                        salaryCalcBean.getActualBaseSalary(),
                                        propRate.getApplicableRate()));
                            }// If rates not available for current year,  availble only for previous year and Need to that apply the rate
                            else if(comparefilterDateAndInflStrDate < 0 && compareAnnvBaseStart > 0){
                                Equals eqInflStartDate = new Equals("startDate", filterAnnInfDate);
                                CoeusVector cvfilterRateBase = this.cvSalaryAnnivInflationRates.filter(eqInflStartDate);
                                ProposalRatesBean propRatesBean = null;
                                if (cvfilterRateBase !=null && cvfilterRateBase.size() >0){
                                    propRatesBean = (ProposalRatesBean) cvfilterRateBase.get(0);
                                    nextSalaryCalcBean.setActualBaseSalary(calculateBaseSalary(
                                            salaryCalcBean.getActualBaseSalary(),
                                            propRatesBean.getApplicableRate()));
                                }                                
                            }                            
                        }
                    }
                }
            }
            //If there is any Break up date and set new period End date as a BreakUpDate
            Boundary brkUpBoundary = null;
            if(brkUpEndDate!=null){
                brkUpBoundary = new Boundary(salaryCalcBean.getBoundary().getStartDate(),brkUpEndDate);
                salaryCalcBean.setBoundary(brkUpBoundary);
                breakUpIntervals.add(salaryCalcBean);
            }
        }//end for loop
        if(nextSalaryCalcBean!=null && nextSalaryCalcBean.getBoundary()!=null &&
                nextSalaryCalcBean.getBoundary().getStartDate().compareTo(personEndDate)<=0){
            nextSalaryCalcBean.setBoundary(new Boundary(nextSalaryCalcBean.getBoundary().getStartDate(),
                    personEndDate));
            breakUpIntervals.add(nextSalaryCalcBean);
        }
        return breakUpIntervals;        
    } // end createSalBreakupIntervals
    /**
     * This method is used to get the Year value on the Date.
     * Used to set the Annivesary Date based on Effective date and Inflation date.
     * @param Date StartDate
     * @return int YEAR value
     */
    private int getSalAnnivDateYear(Date startDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        return calendar.get(Calendar.YEAR);
    } // end getSalAnnivDateYear
    /**
     * This method is to merge the Year with source date.
     * This is used for to set the Annivarsary Year based on the Effective date and Annivesary date
     * @param StartDate
     * @param Year
     * @return Date 
     */
    private Date mergeSalAnnivDateYear(Date startDate, int year){
        Calendar calendar = Calendar.getInstance();
        String DATE_FORMAT = "yyyy-MM-dd";
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat(DATE_FORMAT);
        calendar.setTime(startDate);       
        //COEUSQA-2997/COEUSQA-3945
        calendar.set(Calendar.YEAR, year);
        //COEUSQA-2997/COEUSQA-3945
        return calendar.getTime();
    }
    
    // Added for Case 4315 - Problem in salary anniversary date functionality - Start
    /**
     * This method is used to return Salary Anniversary Year based on Inflation date, Person Start date and End date 
     * @param inflStartDate
     * @param salAnnivDate
     * @param effectiveDate
     * @return int -Salary Anniversary Year 
     */
    private int getSalAnnivDateYearWithPersonDate(Date inflStartDate, Date salAnnivDate, Date effectiveDate,Date budgetPersonStartDate,Date budgetPersonEndDate)throws CoeusException{
        long dateDiff = 0;
        long infPersonStrdateDiff = 0;
        long PersonStrEnddateDiff = 0;
        int validYear = 0;
        Date personStrDate = null;
        Date personEndDate = null;
        DateUtils dtUtils = new DateUtils();
        //forming the key
        String key = personDetails.getProposalNumber()+personDetails.getVersionNumber();
        int currentPeriod = personDetails.getBudgetPeriod();
        CoeusVector cvBudgetPeriods = queryEngine.executeQuery(key,
                BudgetPeriodBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
        HashMap hmSalDate = new HashMap();
        if(cvBudgetPeriods != null && cvBudgetPeriods.size() > 0){
            for(int index = 0; index < cvBudgetPeriods.size() ; index++){
                BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean) cvBudgetPeriods.get(index);
                int period = budgetPeriodBean.getBudgetPeriod();
                //Check up to current period.
                if(period <= currentPeriod){
                    // Effective date is less than  first period, then check Anniversary date, nearest inflation date and Year
                    if(effectiveDate.compareTo(budgetPeriodBean.getStartDate()) < 0 && period == 1){
                        //Check how many Annivesary persent before Budget Period one.
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(effectiveDate);
                        int effectiveDateYear =  calendar.get(Calendar.YEAR);
                        Date salDate =  mergeSalAnnivDateYear(salAnnivDate,effectiveDateYear);
                        
                        //Check How many Anniversary is persent before Period Start Date, put in Hashmap
                        Calendar salCalendar = Calendar.getInstance();
                        salCalendar.setTime(salDate);
                        int salDateYear =  calendar.get(Calendar.YEAR);
                        Date nearestInflDate = getNearestInflationDate(salDate);
                        if( nearestInflDate !=null){
                            hmSalDate.put(nearestInflDate, new Integer(salDateYear));
                        }
                        Vector vecSal = new Vector();
                        while(salDate.compareTo(budgetPeriodBean.getStartDate()) < 0 ){
                            salDate =  mergeSalAnnivDateYear(salAnnivDate,++salDateYear);
                            if(salDate.compareTo(budgetPeriodBean.getStartDate()) < 0){
                                Date nearestInfDate = getNearestInflationDate(salDate);
                                if( nearestInfDate !=null){
                                    hmSalDate.put(nearestInfDate, new Integer(salDateYear));
                                }
                            }
                        }
                    } // End of Effective date is less than first period.
                    // Effective date same as or  Greater than  first period, then check Anniversary date, nearest inflation date and Year
//                    if(effectiveDate.compareTo(budgetPeriodBean.getStartDate()) >= 0 ){
                    // Get the Person Start End Year
                    Calendar strDateCalendar = Calendar.getInstance();
                    strDateCalendar.setTime(budgetPeriodBean.getStartDate());
                    int personStartDateMonth = strDateCalendar.get(Calendar.MONTH);
                    int personStartDateYear = strDateCalendar.get(Calendar.YEAR);
                    //COEUSQA-4132
                    int personStartDateDay = strDateCalendar.get(Calendar.DAY_OF_MONTH);
                    //COEUSQA-4132
                    Calendar endDatecalendar = Calendar.getInstance();
                    endDatecalendar.setTime(budgetPeriodBean.getEndDate());
                    int personEndDateMonth = endDatecalendar.get(Calendar.MONTH);
                    int personEndDateYear = endDatecalendar.get(Calendar.YEAR);
                    //COEUSQA-4132
                    int personEndDateDay = endDatecalendar.get(Calendar.DAY_OF_MONTH);
                    //COEUSQA-4132
                    int annivYear = 0;
                    Vector vecAnivYear = new Vector();
                    Vector vecAnnivPossibleYears = new Vector();
                    
                    Calendar annivCalendar = Calendar.getInstance();
                    annivCalendar.setTime(salAnnivDate);
                    int annivMonth = annivCalendar.get(Calendar.MONTH);
                    
                     //Modified for Case 3197 - Allow for the generation of project period greater than 12 months -Start
                    // If Person Start Year and Person End Date Year are not same and check the anniversary Date
                    if(personStartDateYear != personEndDateYear){
                        //If Anniversary Month is greter than Person Start Date Month and less than or equal to December, Apply PersonStartDate Year.
//                        if(annivMonth >= personStartDateMonth && annivMonth < 12 ){
//                            annivYear = personStartDateYear;
//                        } else if(annivMonth < personEndDateMonth){
//                            annivYear = personEndDateYear;
//                        }
                        for(int endYear = personStartDateYear+1; endYear<= personEndDateYear; endYear++){
                            vecAnnivPossibleYears.addElement(new Integer(endYear));
                        }
                        if(vecAnnivPossibleYears !=null && vecAnnivPossibleYears.size() > 0){
                            //If Anniversary Month is greter than Person Start Date Month and less than or equal to December, Apply PersonStartDate Year.
                                                       
                            if(annivMonth >= personStartDateMonth && annivMonth < 12 ){
                                //COEUSQA-4132  
                                //If months are same then check the days
                                if (annivMonth == personStartDateMonth && personStartDateDay > personEndDateDay ){
                                    
                                    vecAnivYear = vecAnnivPossibleYears;
                                    
                                }else{
                                
                                    vecAnivYear.add(new Integer(personStartDateYear));
                                
                                }
                             //COEUSQA-4132   
                             //Added for COEUSQA-2300 Salary anniversary date - Inflation not working correctly when anniversary and period end date is in same month -Start   
                             //If Annivesary month is same as person end date month, apply the inflation     
                            }else if(annivMonth <= personEndDateMonth){
                                vecAnivYear = vecAnnivPossibleYears;
                            }
                            //Added for COEUSQA-2300 -End
                        }
                    }else{ // If both are equal
//                        annivYear = personStartDateYear;
                        vecAnivYear.add(new Integer(personStartDateYear));
                    }
//                    Date salAnnDate =  mergeSalAnnivDateYear(salAnnivDate,annivYear);
//                    Date nearestInfDate1 = getNearestInflationDate(salAnnDate);
//                    if( nearestInfDate1 !=null){
//                        hmSalDate.put(nearestInfDate1, new Integer(annivYear));
//                    }
                    if(vecAnivYear !=null && vecAnivYear.size() > 0){
                        for(int index1= 0 ; index1 < vecAnivYear.size(); index1++){
                            int salAnnivYear = 0;
                            Integer salAnnYear = (Integer) vecAnivYear.get(index1);
                            salAnnivYear = salAnnYear.intValue();
                            Date salAnnDate =  mergeSalAnnivDateYear(salAnnivDate,salAnnivYear);
                            Date nearestInfDate1 = getNearestInflationDate(salAnnDate);
                            if( nearestInfDate1 !=null){
                                hmSalDate.put(nearestInfDate1, new Integer(salAnnivYear));
                            }
                        }
                    }
                     //Modified for Case 3197 - Allow for the generation of project period greater than 12 months -End
//                    }
                    
                }else {
                    break;
                }
            }
        }else{
            // Added for Anniversay calculation problem, if Period and Rates start date are same and anniversay date span to next year those person salary is not inflated.
            //If period information is not available this block will execute
            int periodIndex = 0;
            for( int i = currentPeriod ; i > 0; i--){
                Date perStartDate = dtUtils.dateAdd(Calendar.YEAR, personDetails.getStartDate(), periodIndex);
                Date perEndDate = dtUtils.dateAdd(Calendar.YEAR, personDetails.getEndDate(), periodIndex);
                
                // Get the Person Start End Year
                Calendar stDateCalendar = Calendar.getInstance();
                stDateCalendar.setTime(perStartDate);
                int perStartDateMonth = stDateCalendar.get(Calendar.MONTH);
                int perStartDateYear = stDateCalendar.get(Calendar.YEAR);
                
                Calendar endDtcalendar = Calendar.getInstance();
                endDtcalendar.setTime(perEndDate);
                int perEndDateMonth = endDtcalendar.get(Calendar.MONTH);
                int perEndDateYear = endDtcalendar.get(Calendar.YEAR);
                
                int annivYear = 0;
                Vector vecAnivYear = new Vector();
                Calendar annivCalendar = Calendar.getInstance();
                annivCalendar.setTime(salAnnivDate);
                int annivMonth = annivCalendar.get(Calendar.MONTH);
                Vector vecAnnivPossibleYears = new Vector();
                // If Person Start Year and Person End Date Year are not same and check the anniversary Date
                //Modified for Case 3197 - Allow for the generation of project period greater than 12 months -Start
                if(perStartDateYear != perEndDateYear){
                    for(int endYear = perStartDateYear+1; endYear<= perEndDateYear; endYear++){
                        vecAnnivPossibleYears.addElement(new Integer(endYear));
                    }
                    if(vecAnnivPossibleYears !=null && vecAnnivPossibleYears.size() > 0){
                        //If Anniversary Month is greter than Person Start Date Month and less than or equal to December, Apply PersonStartDate Year.
                        if(annivMonth >= perStartDateMonth && annivMonth < 12 ){
                            vecAnivYear.add(new Integer(perStartDateYear));
                       //Added for COEUSQA-2300 Salary anniversary date - Inflation not working correctly when anniversary and period end date is in same month -Start   
                       //If Annivesary month is same as person end date month, apply the inflation     
                        }else if(annivMonth <= perEndDateMonth){
                            vecAnivYear = vecAnnivPossibleYears;
                        }
                    }
                    //If Anniversary Month is greter than Person Start Date Month and less than or equal to December, Apply PersonStartDate Year.
//                    if(annivMonth >= perStartDateMonth && annivMonth < 12 ){
//                        annivYear = perStartDateYear;
//                    } else if(annivMonth < perEndDateMonth){
//                        annivYear = perEndDateYear;
//                    }
                }else{ // If both are equal
                    vecAnivYear.add(new Integer(perStartDateYear));
//                    annivYear = perStartDateYear;
                }
                if(vecAnivYear !=null && vecAnivYear.size() > 0){
                    for(int index1= 0 ; index1 < vecAnivYear.size(); index1++){
                        int salAnnivYear = 0;
                        Integer salAnnYear = (Integer) vecAnivYear.get(index1);
                        salAnnivYear = salAnnYear.intValue();
                        Date salAnnDate =  mergeSalAnnivDateYear(salAnnivDate,salAnnivYear);
                        Date nearestInfDate1 = getNearestInflationDate(salAnnDate);
                        if( nearestInfDate1 !=null){
                            hmSalDate.put(nearestInfDate1, new Integer(salAnnivYear));
                        }
                    }
                    
                }
                
                periodIndex--;
                
            }
            //Modified for Case 3197 - Allow for the generation of project period greater than 12 months -End
        }
        // Based on the Inflation Date , return the Salary Anniversary Year
        Calendar inflCalendar = Calendar.getInstance();
        inflCalendar.setTime(inflStartDate);
        int inflAnnYear = inflCalendar.get(Calendar.YEAR);
        if( hmSalDate !=null && hmSalDate.size() > 0){
            if(hmSalDate.get(inflStartDate) != null){
                Integer intAnnYear = (Integer) hmSalDate.get(inflStartDate);
                inflAnnYear = intAnnYear.intValue();
                
                // Added for COEUSQA-2997 Inflation and Salary Anniversary Date functionality having problem with fringe benefit calculation engine -Start
                
                if(budgetPersonEndDate != null){
                    Date salAnnDate =  mergeSalAnnivDateYear(salAnnivDate,inflAnnYear);
                    Calendar annivCalendar = Calendar.getInstance();
                    annivCalendar.setTime(salAnnDate);
                    int anniversayYear = annivCalendar.get(Calendar.YEAR);
                    
                    //If Salary Anniversary date is greater than Budget Person End Date, decrease one anniversary year.
                    if(salAnnDate.compareTo(budgetPersonEndDate) > 0 ){
                        inflAnnYear =  anniversayYear-1;
                    }
                }
                // Added for COEUSQA-2997 -End
            }
        }
        return inflAnnYear;
        
    }
   // Added for Case 4315 - Problem in salary anniversary date functionality - End
    
    /**
     * This method is used to get the nearest Inflation date based on Salary Anniversary date
     * @param Date SalaryAnniversaryDate
     * @return Date
     */
    private Date getNearestInflationDate(Date salaryAnnivDate) {
        ProposalRatesBean propRate = null;
        Date infStartDate , filterInflDate = null;
        long dateDiff =0;
        long comparedValue = 0;
        Map hmDateList = new HashMap();
        Vector vecDateDiff = new Vector();
        for(int index=0;index<this.cvSalaryAnnivInflationRates.size();index++){
            propRate = (ProposalRatesBean)cvSalaryAnnivInflationRates.get(index);
            infStartDate = propRate.getStartDate();
            dateDiff =  Math.round((infStartDate.getTime() - salaryAnnivDate.getTime())/86400000.0d + 1);
            hmDateList.put( new Long(dateDiff),infStartDate);
            vecDateDiff.addElement(new Long(dateDiff));
            if(dateDiff ==1){
                break;
            }
        }
        for(int index2 = 0; index2 < vecDateDiff.size(); index2++){
            Long value1 = (Long)vecDateDiff.get(index2);
            long value2 = value1.longValue();
            if(value2 >1){
                continue;
            }
            if(comparedValue == 0){
                comparedValue = value2 ;
            }else{
                if(value2 > comparedValue  ){
                    comparedValue = value2;
                }
            }
        }
        if(hmDateList !=null && hmDateList.size() > 0 && hmDateList.get(new Long(comparedValue)) !=null){
            filterInflDate = (Date) hmDateList.get(new Long(comparedValue));
        }
        
        return filterInflDate;
    }
    /**
     *  Method to get Salary Anniversary Information from OSP$Parameter table
     */
    private Hashtable getSalaryAnniversaryParameterInfo() throws BudgetCalculateException{
        Hashtable htAnnivInfo = new Hashtable();
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        try{
            String salaryInflationAnniversaryDate = coeusFunctions.getParameterValue(CoeusConstants.ENABLE_SALARY_INFLATION_ANNIV_DATE);
            htAnnivInfo.put(CoeusConstants.ENABLE_SALARY_INFLATION_ANNIV_DATE, salaryInflationAnniversaryDate==null ? null : new Integer(salaryInflationAnniversaryDate));
        }catch(DBException dbEx){
            throw new BudgetCalculateException(dbEx.getMessage());
        }catch(CoeusException coeusEx){
            throw new BudgetCalculateException(coeusEx.getMessage());
        }
        return htAnnivInfo;
    }
    /**
 * - Sort all the Inflation rates based on Start Date in ascending
 * - Filter out all Inflation rates whose Start Date lies outside the Start
 * &amp; End dates of the Person Details. If the Effective date of the 1st Base
 * Salary comes before the Start Date for Person, then make sure to include
 * all the rates coming after Effective Date.
 * 
 */
    private void sortAndFilterSalAnnivInflationRates() throws BudgetCalculateException{        
        if(cvSalaryAnnivInflationRates!= null && cvSalaryAnnivInflationRates.size() > 0){
            cvSalaryAnnivInflationRates.sort("startDate");
            cvSalaryAnnivInflationRates = filterSalAnnivInflRates(cvSalaryAnnivInflationRates,"startDate",false);
        }
        
    } // end sortAndFilterInflationRates 
    
    /**
     * Method to filter out the data which is out of boundary. And include 
     * the element which is just before the start date of the personnel details.
     */
    private CoeusVector filterSalAnnivInflRates(CoeusVector listToBeFiltered, 
                                String filterDateString, boolean personFlag)
                    throws BudgetCalculateException{
        boolean noCalcBase = false;
        Date startPersonEffDate = null;
        if(cvBudgetPersons.isEmpty()){
            startPersonEffDate = personDetails.getStartDate();
            noCalcBase = true;
        }else{
            startPersonEffDate = ((BudgetPersonsBean)cvBudgetPersons.firstElement()).getEffectiveDate();
        }
        Date personStartDate = personFlag?personDetails.getStartDate():startPersonEffDate;
        Date personEndDate = personDetails.getEndDate();
        CoeusVector cvFltdVector = new CoeusVector();
        
        if(personFlag){
            LesserThan lesserThanStDt = new LesserThan(filterDateString, personStartDate);
            Equals lesserEqualsStDt = new Equals(filterDateString, personStartDate);
            Or lesserDatesOr = new Or(lesserThanStDt, lesserEqualsStDt);

            CoeusVector cvFltdLesserElements = listToBeFiltered.filter(lesserDatesOr);
            cvFltdLesserElements.sort(filterDateString);
        
            if(cvFltdLesserElements.isEmpty()){
                noCalcBase = true;
            }else{
                cvFltdVector.add(cvFltdLesserElements.lastElement());
            }
        }
        
        /*
         *  Forming the filter condition for fetching the elements which are in between 
         *  the personnel start date and end date. And add the filtered elements to 
         *  actual one.
         */
//        GreaterThan greaterThanStDt = new GreaterThan(filterDateString, personStartDate);
        LesserThan lesserThan = new LesserThan(filterDateString, personEndDate);
        Equals lesserEquals = new Equals(filterDateString, personEndDate);
        Or endDateOr = new Or(lesserThan, lesserEquals);
//        And and = new And(greaterThanStDt,endDateOr);
        cvFltdVector.addAll(cvFltdVector.size(),listToBeFiltered.filter(endDateOr));
        
        
        return cvFltdVector;
    }
    //Added for Case 2918 � Use of Salary Anniversary Date for calculating inflation in budget development module-End
    
   /* public static void main(String args[]){
        String propNumber = "01100550";
        TestCalculator test = new TestCalculator();
        Hashtable budgetData = test.getAllBudgetData(propNumber, 1);
        String key = propNumber + 1;
        try {
            queryEngine = QueryEngine.getInstance();
            queryEngine.addDataCollection(key, budgetData);
            CoeusVector cvPersonDetails = queryEngine.getDetails(key,BudgetPersonnelDetailsBean.class);
            BudgetPersonnelDetailsBean  tmpBean = new BudgetPersonnelDetailsBean();
            tmpBean.setProposalNumber(propNumber);
            tmpBean.setVersionNumber(1);
            tmpBean.setBudgetPeriod(1);
            tmpBean.setLineItemNumber(1);
            

                BudgetPersonnelDetailsBean personnelDetails = 
                        (BudgetPersonnelDetailsBean)(cvPersonDetails.isLike(tmpBean)).elementAt(0);
                
                SalaryCalculator salCalc = new SalaryCalculator(personnelDetails);
                System.out.println("Person bean after calculation=>"+salCalc.getPersonAfterCalculation());
                System.out.println("**********************************************");
                System.out.println("Salary=>"+salCalc.getPersonAfterCalculation().getSalaryRequested());
                System.out.println("**********************************************");
                System.out.println("Messages=>"+salCalc.getVecMessages().toString());
        }catch (CoeusException ex){
            ex.printStackTrace();
        }
    }
	*/
    
    /** Getter for property requestedSalary.
     * @return Value of property requestedSalary.
     */
    public double getRequestedSalary() {
        return requestedSalary;
    }
    
    /** Setter for property requestedSalary.
     * @param requestedSalary New value of property requestedSalary.
     */
    public void setRequestedSalary(double requestedSalary) {
        this.requestedSalary = requestedSalary;
    }
    
    /** Getter for property vecMessages.
     * @return Value of property vecMessages.
     */
    public Vector getVecMessages() {
        return vecMessages;
    }
    
    /** Setter for property vecMessages.
     * @param vecMessages New value of property vecMessages.
     */
    public void setVecMessages(Vector vecMessages) {
        this.vecMessages = vecMessages;
    }
    
    /** Getter for property vecErrMessages.
     * @return Value of property vecErrMessages.
     *
     */
    public Vector getVecErrMessages() {
        return vecErrMessages;
    }
    
    /** Setter for property vecErrMessages.
     * @param vecErrMessages New value of property vecErrMessages.
     *
     */
    public void setVecErrMessages(Vector vecErrMessages) {
        this.vecErrMessages = vecErrMessages;
    }
    public Date getInflEndDate(Date inflStartDate){
        Calendar cal=Calendar.getInstance();        
        cal.setTime(inflStartDate);
        cal.add(Calendar.MONTH, 12);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }
 } // end SalaryCalculator



