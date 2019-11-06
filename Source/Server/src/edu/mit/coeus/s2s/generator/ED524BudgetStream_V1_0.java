/*
 * ED524BudgetStream_V1_0.java
 *
 * Created on November 13, 2006
 */

package edu.mit.coeus.s2s.generator;


import edu.mit.coeus.s2s.bean.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;

import gov.grants.apply.forms.ed_524_budget_v1.*;
import gov.grants.apply.system.globallibrary_v1.*;
//import gov.grants.apply.system.global_v1.*;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;

import java.util.TimeZone;

import javax.xml.bind.JAXBException;


/**
 *
 * @author  ele
 */
public class ED524BudgetStream_V1_0 extends S2SBaseStream{ 
    private gov.grants.apply.forms.ed_524_budget_v1.ObjectFactory objFactory;
    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
    private gov.grants.apply.system.globallibrary_v1.ObjectFactory globallibraryObjFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;
    private CoeusXMLGenrator xmlGenerator;
    gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
    
    //txn beans
    private Ed524BudgetV11TxnBean ed524BudgetV11TxnBean;

    
    private HashMap hmInfo;
    private String propNumber;
    private Calendar calendar;
    private UtilFactory utilFactory;   
    
 
    /** Creates a new instance of ED524BudgetStream_V1_0 */
    public ED524BudgetStream_V1_0() {
        objFactory = new gov.grants.apply.forms.ed_524_budget_v1.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
        globallibraryObjFactory = new gov.grants.apply.system.globallibrary_v1.ObjectFactory();
 
        xmlGenerator = new CoeusXMLGenrator();
        
        ed524BudgetV11TxnBean = new Ed524BudgetV11TxnBean();
        hmInfo = new HashMap();
    }
    
    private ED524BudgetType  getEd524Budget() throws CoeusXMLException,CoeusException,DBException{
        ED524BudgetType ed524Budget = null;
       
      
        hmInfo = ed524BudgetV11TxnBean.getSimpleInfo(propNumber);
        
         BigDecimal zero = new BigDecimal(0.00);
         BigDecimal totalCost = new BigDecimal(0.00);
         BigDecimal totalDirectCost = new BigDecimal(0.00);
         BigDecimal totalIndirectCost = new BigDecimal(0.00);
         BigDecimal indirectCS = new BigDecimal(0.00);
         BigDecimal totalCostSharing = new BigDecimal(0.00);
         BigDecimal categoryCost = new BigDecimal(0.00);
         BigDecimal categoryCostCS = new BigDecimal(0.00);
         BigDecimal totalCategoryCS = new BigDecimal(0.00);
               
         BigDecimal totalPersonnel = new BigDecimal(0.00);
         BigDecimal totalPersonnelCS = new BigDecimal(0.00);
         BigDecimal totalFringe= new BigDecimal(0.00);
         BigDecimal totalFringeCS = new BigDecimal(0.00);
         BigDecimal totalTravel = new BigDecimal(0.00);
         BigDecimal totalTravelCS = new BigDecimal(0.00);
         BigDecimal totalEquip= new BigDecimal(0.00);
         BigDecimal totalEquipCS = new BigDecimal(0.00);
         BigDecimal totalSupplies = new BigDecimal(0.00);
         BigDecimal totalSuppliesCS = new BigDecimal(0.00);
         BigDecimal totalContractual = new BigDecimal(0.00);
         BigDecimal totalContractualCS = new BigDecimal(0.00);
         BigDecimal totalConstruction = new BigDecimal(0.00);
         BigDecimal totalConstructionCS = new BigDecimal(0.00);
         BigDecimal totalOther = new BigDecimal(0.00);
         BigDecimal totalOtherCS = new BigDecimal(0.00);
         BigDecimal totalTraining = new BigDecimal(0.00);
         BigDecimal totalTrainingCS = new BigDecimal(0.00);
         BigDecimal totalCostAllYrs = new BigDecimal(0.00);
         BigDecimal totalCostSharingAllYrs = new BigDecimal(0.00);
         BigDecimal totalDirectCostAllYrs = new BigDecimal(0.00);
         BigDecimal totalDirectCostAllYrsCS = new BigDecimal(0.00);
         BigDecimal totalIndirectCostAllYrs = new BigDecimal(0.00);
         BigDecimal totalIndirectCostAllYrsCS = new BigDecimal(0.00);
         
         HashMap hmCategory = new HashMap();
         HashMap hmsupplies = new HashMap();
         HashMap hmIDCCostShare = new HashMap();
         
        try{
           ed524Budget = objFactory.createED524Budget();
           
           ed524Budget.setFormVersion("1.0");    
           ed524Budget.setLEGALNAME(hmInfo.get("LEGALNAME").toString());
           
           int version = Integer.parseInt(hmInfo.get("VERSION").toString());
           if (version > 0){
               
           int numPeriods = Integer.parseInt(hmInfo.get("NUMPERIODS").toString());
            
           CoeusVector cvPeriodAmounts = new CoeusVector();
           cvPeriodAmounts = ed524BudgetV11TxnBean.getPeriodAmts(propNumber, version, numPeriods);
           
           // cvPeriodAmounts holds HashMaps - one for each budget year
           
           HashMap hmFirstYearAmounts;
           
           /**************
            **FIRST YEAR
            **************/
             //case 2777
           if (numPeriods > 0) {
           if (cvPeriodAmounts.get(0) != null) {
                hmFirstYearAmounts = (HashMap) cvPeriodAmounts.get(0);
                totalCategoryCS = new BigDecimal(0.0);
                
                /************
                ** total cost
                *************/
                totalCost = new BigDecimal(hmFirstYearAmounts.get("TOTAL_COST").toString());
                totalCostSharing = new BigDecimal(hmFirstYearAmounts.get("COST_SHARING").toString());
                ed524Budget.setBudgetFederalFirstYearAmount(totalCost);
                ed524Budget.setBudgetNonFederalFirstYearAmount(totalCostSharing);
                                        
                totalCostAllYrs = totalCostAllYrs.add(totalCost);
                totalCostSharingAllYrs = totalCostSharingAllYrs.add(totalCostSharing);
                 
                /************
                ** total direct cost
                *************/
                 totalDirectCost = new BigDecimal(hmFirstYearAmounts.get("TOTAL_DIRECT_COST").toString());
                ed524Budget.setBudgetFederalDirectFirstYearAmount(totalDirectCost);
                totalDirectCostAllYrs = totalDirectCostAllYrs.add(totalDirectCost);
                
                /************
                ** total indirect cost
                *************/
                totalIndirectCost = new BigDecimal(hmFirstYearAmounts.get("TOTAL_INDIRECT_COST").toString()) == null ?
                               zero : new BigDecimal(hmFirstYearAmounts.get("TOTAL_INDIRECT_COST").toString());
                ed524Budget.setBudgetFederalIndirectFirstYearAmount(totalIndirectCost);
               
                totalIndirectCostAllYrs = totalIndirectCostAllYrs.add(totalIndirectCost);
                
                /******************
                 * total indirect cost sharing
                 ******************/
                
         
                
                hmIDCCostShare = ed524BudgetV11TxnBean.getIDCCostSharing(propNumber,1,version);
                if (hmIDCCostShare != null) {
                  indirectCS =  new BigDecimal(hmIDCCostShare.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmIDCCostShare.get("COST_SHARING").toString());
                }else
                    indirectCS = new BigDecimal("0");
                ed524Budget.setBudgetNonFederalIndirectFirstYearAmount(indirectCS);
                
               totalIndirectCostAllYrsCS = totalIndirectCostAllYrsCS.add(indirectCS);
              
                
                /************
                ** supplies
                *************/
                hmCategory = ed524BudgetV11TxnBean.getSupplies(propNumber,version, 1);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString());                
                ed524Budget.setBudgetFederalSuppliesFirstYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalSuppliesFirstYearAmount(categoryCostCS);
                totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                
                 totalSupplies = totalSupplies.add(categoryCost);
                 totalSuppliesCS = totalSuppliesCS.add(categoryCostCS);
                }
                
                /************
                ** construction
                *************/
                hmCategory = ed524BudgetV11TxnBean.getConstruction(propNumber,version, 1);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString());                
                ed524Budget.setBudgetFederalConstructionFirstYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalConstructionFirstYearAmount(categoryCostCS);
                totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                
                totalConstruction = totalConstruction.add(categoryCost);
                 totalConstructionCS = totalConstructionCS.add(categoryCostCS);
                }
                
                /************
                ** other
                *************/
                hmCategory = ed524BudgetV11TxnBean.getOther(propNumber,version, 1);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString());    
                ed524Budget.setBudgetFederalOtherFirstYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalOtherFirstYearAmount(categoryCostCS);
                 totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                 
                 totalOther = totalOther.add(categoryCost);
                 totalOtherCS = totalOtherCS.add(categoryCostCS);
                }
                
                /************
                ** equipment
                *************/
                hmCategory = ed524BudgetV11TxnBean.getEquipment(propNumber,version, 1);
                 if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString()); 
                ed524Budget.setBudgetFederalEquipmentFirstYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalEquipmentFirstYearAmount(categoryCostCS);
                 totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                 
                 totalEquip = totalEquip.add(categoryCost);
                 totalEquipCS = totalEquipCS.add(categoryCostCS);
                 }
                
                /************
                ** contractual
                *************/
                hmCategory = ed524BudgetV11TxnBean.getContractual(propNumber,version, 1);
                 if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString()); 
                ed524Budget.setBudgetFederalContractualFirstYearAmount(categoryCost);
                 ed524Budget.setBudgetNonFederalContractualFirstYearAmount(categoryCostCS);
                 totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                  
                 totalContractual = totalContractual.add(categoryCost);
                 totalContractualCS = totalContractualCS.add(categoryCostCS);
                 }
                
                /************
                ** travel
                *************/
                hmCategory = ed524BudgetV11TxnBean.getTravel(propNumber,version, 1);
                 if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString()); 
                ed524Budget.setBudgetFederalTravelFirstYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalTravelFirstYearAmount(categoryCostCS);
                 totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                 
                 totalTravel = totalTravel.add(categoryCost);
                 totalTravelCS = totalTravelCS.add(categoryCostCS);
                 }
                
                /************
                ** training
                *************/
                hmCategory = ed524BudgetV11TxnBean.getTraining(propNumber,version, 1);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString()); 
                ed524Budget.setBudgetFederalTrainingFirstYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalTrainingFirstYearAmount(categoryCostCS);
                
                totalTraining = totalTraining.add(categoryCost);
                 totalTrainingCS = totalTrainingCS.add(categoryCostCS);
                }
                
                /************
                ** fringe
                *************/
                hmCategory = ed524BudgetV11TxnBean.get_fringe(propNumber,version, 1);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("FRINGE").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("FRINGE").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("FRINGE_COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("FRINGE_COST_SHARING").toString()); 
                ed524Budget.setBudgetFederalFringeFirstYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalFringeFirstYearAmount(categoryCostCS);
                 totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                 
                 totalFringe = totalFringe.add(categoryCost);
                 totalFringeCS = totalFringeCS.add(categoryCostCS);
                }
                
                /************
                ** personnel
                *************/
                hmCategory = ed524BudgetV11TxnBean.getPersonnelCosts(propNumber,version, 1);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString()); 
                ed524Budget.setBudgetFederalPersonnelFirstYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalPersonnelFirstYearAmount(categoryCostCS);
                 totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                 
                 totalPersonnel = totalPersonnel.add(categoryCost);
                 totalPersonnelCS = totalPersonnelCS.add(categoryCostCS);
                }
                
                 ed524Budget.setBudgetNonFederalDirectFirstYearAmount(totalCategoryCS);
               
           }
           }
           
           /**************
            **SECOND YEAR
            **************/
           
           HashMap hmSecondYearAmounts ;
             //case 2777
           if (numPeriods > 1) {
           if (cvPeriodAmounts.get(1) != null) {
                totalCategoryCS = new BigDecimal(0.0);
                
                hmSecondYearAmounts = (HashMap) cvPeriodAmounts.get(1);
        
                /************
                ** total cost
                *************/
                totalCost = new BigDecimal(hmSecondYearAmounts.get("TOTAL_COST").toString());
                totalCostSharing = new BigDecimal(hmSecondYearAmounts.get("COST_SHARING").toString());
                ed524Budget.setBudgetFederalSecondYearAmount(totalCost);
                ed524Budget.setBudgetNonFederalSecondYearAmount(totalCostSharing);
                                       
                 totalCostAllYrs = totalCostAllYrs.add(totalCost);
                totalCostSharingAllYrs = totalCostSharingAllYrs.add(totalCostSharing);
                 
                /************
                ** total direct cost
                *************/
                totalDirectCost = new BigDecimal(hmSecondYearAmounts.get("TOTAL_DIRECT_COST").toString());
                ed524Budget.setBudgetFederalDirectSecondYearAmount(totalDirectCost);
                totalDirectCostAllYrs = totalDirectCostAllYrs.add(totalDirectCost);
             
                /************
                ** total indirect cost
                *************/
                totalIndirectCost = new BigDecimal(hmSecondYearAmounts.get("TOTAL_INDIRECT_COST").toString()) == null ?
                               zero : new BigDecimal(hmSecondYearAmounts.get("TOTAL_INDIRECT_COST").toString());
                ed524Budget.setBudgetFederalIndirectSecondYearAmount(totalIndirectCost);
               totalIndirectCostAllYrs = totalIndirectCostAllYrs.add(totalIndirectCost);
              
               
                /******************
                 * total indirect cost sharing
                 ******************/
                hmIDCCostShare = ed524BudgetV11TxnBean.getIDCCostSharing(propNumber,2,version);
                if (hmIDCCostShare != null) {
                  indirectCS =  new BigDecimal(hmIDCCostShare.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmIDCCostShare.get("COST_SHARING").toString());
                }else
                    indirectCS = new BigDecimal("0");
               
                ed524Budget.setBudgetNonFederalIndirectSecondYearAmount(indirectCS);
                
               totalIndirectCostAllYrsCS = totalIndirectCostAllYrsCS.add(indirectCS);
               
                
                /************
                ** supplies
                *************/
                hmCategory = ed524BudgetV11TxnBean.getSupplies(propNumber,version, 2);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString());                
                ed524Budget.setBudgetFederalSuppliesSecondYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalSuppliesSecondYearAmount(categoryCostCS);
                 totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                 
                  totalSupplies = totalSupplies.add(categoryCost);
                 totalSuppliesCS = totalSuppliesCS.add(categoryCostCS);
                }
                
                /************
                ** construction
                *************/
                hmCategory = ed524BudgetV11TxnBean.getConstruction(propNumber,version, 2);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString());                
                ed524Budget.setBudgetFederalConstructionSecondYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalConstructionSecondYearAmount(categoryCostCS);
                 totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                 
                 totalConstruction = totalConstruction.add(categoryCost);
                 totalConstructionCS = totalConstructionCS.add(categoryCostCS);
                }
                /************
                ** other
                *************/
                hmCategory = ed524BudgetV11TxnBean.getOther(propNumber,version, 2);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString());    
                ed524Budget.setBudgetFederalOtherSecondYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalOtherSecondYearAmount(categoryCostCS);
                 totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                 
                 totalOther = totalOther.add(categoryCost);
                 totalOtherCS = totalOtherCS.add(categoryCostCS);
                }
                /************
                ** equipment
                *************/
                hmCategory = ed524BudgetV11TxnBean.getEquipment(propNumber,version, 2);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString()); 
                ed524Budget.setBudgetFederalEquipmentSecondYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalEquipmentSecondYearAmount(categoryCostCS);
                 totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                 
                  totalEquip = totalEquip.add(categoryCost);
                 totalEquipCS = totalEquipCS.add(categoryCostCS);
                }
                /************
                ** contractual
                *************/
                hmCategory = ed524BudgetV11TxnBean.getContractual(propNumber,version, 2);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString()); 
                ed524Budget.setBudgetFederalContractualSecondYearAmount(categoryCost);
                 ed524Budget.setBudgetNonFederalContractualSecondYearAmount(categoryCostCS);
                 totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                 
                 totalContractual = totalContractual.add(categoryCost);
                 totalContractualCS = totalContractualCS.add(categoryCostCS);
                }
                /************
                ** travel
                *************/
                hmCategory = ed524BudgetV11TxnBean.getTravel(propNumber,version, 2);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString()); 
                ed524Budget.setBudgetFederalTravelSecondYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalTravelSecondYearAmount(categoryCostCS);
                totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                
                 totalTravel = totalTravel.add(categoryCost);
                 totalTravelCS = totalTravelCS.add(categoryCostCS);
                }
                /************
                ** training
                *************/
                hmCategory = ed524BudgetV11TxnBean.getTraining(propNumber,version, 2);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString()); 
                ed524Budget.setBudgetFederalTrainingSecondYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalTrainingSecondYearAmount(categoryCostCS);
                
                totalTraining = totalTraining.add(categoryCost);
                 totalTrainingCS = totalTrainingCS.add(categoryCostCS);
                }
                /************
                ** fringe
                *************/
                hmCategory = ed524BudgetV11TxnBean.get_fringe(propNumber,version, 2);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("FRINGE").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("FRINGE").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("FRINGE_COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("FRINGE_COST_SHARING").toString()); 
                ed524Budget.setBudgetFederalFringeSecondYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalFringeSecondYearAmount(categoryCostCS);
                 totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                 
                  totalFringe = totalFringe.add(categoryCost);
                 totalFringeCS = totalFringeCS.add(categoryCostCS);
                }
                /************
                ** personnel
                *************/
                hmCategory = ed524BudgetV11TxnBean.getPersonnelCosts(propNumber,version, 2);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString()); 
                ed524Budget.setBudgetFederalPersonnelSecondYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalPersonnelSecondYearAmount(categoryCostCS);
                totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                
                totalPersonnel = totalPersonnel.add(categoryCost);
                totalPersonnelCS = totalPersonnelCS.add(categoryCostCS);
                }
                ed524Budget.setBudgetNonFederalDirectSecondYearAmount(totalCategoryCS);
               //case 3210    totalCostSharingAllYrs = totalCostSharingAllYrs.add(totalCategoryCS);
             }
           }
            /**************
            **THIRD YEAR
            **************/
           
           HashMap hmThirdYearAmounts ;
             //case 2777
           if (numPeriods > 2) {
           if (cvPeriodAmounts.get(2) != null) {
               totalCategoryCS = new BigDecimal(0.0);
               
                hmThirdYearAmounts = (HashMap) cvPeriodAmounts.get(2);
        
                /************
                ** total cost
                *************/
                totalCost = new BigDecimal(hmThirdYearAmounts.get("TOTAL_COST").toString());
                totalCostSharing = new BigDecimal(hmThirdYearAmounts.get("COST_SHARING").toString());
                ed524Budget.setBudgetFederalThirdYearAmount(totalCost);
                ed524Budget.setBudgetNonFederalThirdYearAmount(totalCostSharing);
                 
                 totalCostAllYrs = totalCostAllYrs.add(totalCost);
                 totalCostSharingAllYrs = totalCostSharingAllYrs.add(totalCostSharing);
                 
                /************
                ** total direct cost
                *************/
                totalDirectCost = new BigDecimal(hmThirdYearAmounts.get("TOTAL_DIRECT_COST").toString());
                ed524Budget.setBudgetFederalDirectThirdYearAmount(totalDirectCost);
                totalDirectCostAllYrs = totalDirectCostAllYrs.add(totalDirectCost);
                
                /******************
                 * total indirect cost sharing
                 ******************/
                hmIDCCostShare = ed524BudgetV11TxnBean.getIDCCostSharing(propNumber,3,version);
                if (hmIDCCostShare != null) {
                  indirectCS =  new BigDecimal(hmIDCCostShare.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmIDCCostShare.get("COST_SHARING").toString());
                }else
                    indirectCS = new BigDecimal("0");
               
                ed524Budget.setBudgetNonFederalIndirectThirdYearAmount(indirectCS);
                   
               totalIndirectCostAllYrsCS = totalIndirectCostAllYrsCS.add(indirectCS);
             
                /************
                ** total indirect cost
                *************/
                totalIndirectCost = new BigDecimal(hmThirdYearAmounts.get("TOTAL_INDIRECT_COST").toString()) == null ?
                               zero : new BigDecimal(hmThirdYearAmounts.get("TOTAL_INDIRECT_COST").toString());
                ed524Budget.setBudgetFederalIndirectThirdYearAmount(totalIndirectCost);
               totalIndirectCostAllYrs = totalIndirectCostAllYrs.add(totalIndirectCost);
                
                /************
                ** supplies
                *************/
                hmCategory = ed524BudgetV11TxnBean.getSupplies(propNumber,version, 3);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString());                
                ed524Budget.setBudgetFederalSuppliesThirdYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalSuppliesThirdYearAmount(categoryCostCS);
                totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                
                 totalSupplies = totalSupplies.add(categoryCost);
                 totalSuppliesCS = totalSuppliesCS.add(categoryCostCS);
                }
                /************
                ** construction
                *************/
                hmCategory = ed524BudgetV11TxnBean.getConstruction(propNumber,version, 3);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString());                
                ed524Budget.setBudgetFederalConstructionThirdYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalConstructionThirdYearAmount(categoryCostCS);
                totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                
                totalConstruction = totalConstruction.add(categoryCost);
                 totalConstructionCS = totalConstructionCS.add(categoryCostCS);
                }
                /************
                ** other
                *************/
                hmCategory = ed524BudgetV11TxnBean.getOther(propNumber,version, 3);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString());    
                ed524Budget.setBudgetFederalOtherThirdYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalOtherThirdYearAmount(categoryCostCS);
                totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                
                totalOther = totalOther.add(categoryCost);
                 totalOtherCS = totalOtherCS.add(categoryCostCS);
                }
                /************
                ** equipment
                *************/
                hmCategory = ed524BudgetV11TxnBean.getEquipment(propNumber,version, 3);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString()); 
                ed524Budget.setBudgetFederalEquipmentThirdYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalEquipmentThirdYearAmount(categoryCostCS);
                totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                
                 totalEquip = totalEquip.add(categoryCost);
                 totalEquipCS = totalEquipCS.add(categoryCostCS);
                }
                /************
                ** contractual
                *************/
                hmCategory = ed524BudgetV11TxnBean.getContractual(propNumber,version, 3);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString()); 
                ed524Budget.setBudgetFederalContractualThirdYearAmount(categoryCost);
                 ed524Budget.setBudgetNonFederalContractualThirdYearAmount(categoryCostCS);
                totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                
                 totalContractual = totalContractual.add(categoryCost);
                 totalContractualCS = totalContractualCS.add(categoryCostCS);
                }
                /************
                ** travel
                *************/
                hmCategory = ed524BudgetV11TxnBean.getTravel(propNumber,version, 3);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString()); 
                ed524Budget.setBudgetFederalTravelThirdYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalTravelThirdYearAmount(categoryCostCS);
                totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                
                 totalTravel = totalTravel.add(categoryCost);
                 totalTravelCS = totalTravelCS.add(categoryCostCS);
                }
                /************
                ** training
                *************/
                hmCategory = ed524BudgetV11TxnBean.getTraining(propNumber,version, 3);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString()); 
                ed524Budget.setBudgetFederalTrainingThirdYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalTrainingThirdYearAmount(categoryCostCS);
                
                totalTraining = totalTraining.add(categoryCost);
                 totalTrainingCS = totalTrainingCS.add(categoryCostCS);
                }
                /************
                ** fringe
                *************/
                hmCategory = ed524BudgetV11TxnBean.get_fringe(propNumber,version, 3);
                
                if (hmCategory != null) {categoryCost = new BigDecimal(hmCategory.get("FRINGE").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("FRINGE").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("FRINGE_COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("FRINGE_COST_SHARING").toString()); 
                ed524Budget.setBudgetFederalFringeThirdYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalFringeThirdYearAmount(categoryCostCS);
                totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                
                 totalFringe = totalFringe.add(categoryCost);
                 totalFringeCS = totalFringeCS.add(categoryCostCS);
                }
                /************
                ** personnel
                *************/
                hmCategory = ed524BudgetV11TxnBean.getPersonnelCosts(propNumber,version, 3);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString()); 
                ed524Budget.setBudgetFederalPersonnelThirdYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalPersonnelThirdYearAmount(categoryCostCS);
                totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                
                totalPersonnel = totalPersonnel.add(categoryCost);
                 totalPersonnelCS = totalPersonnelCS.add(categoryCostCS);
                }
                
                ed524Budget.setBudgetNonFederalDirectThirdYearAmount(totalCategoryCS);
           //case 3210       totalCostSharingAllYrs = totalCostSharingAllYrs.add(totalCategoryCS);
             }
           }
           /**************
            **FOURTH YEAR
            **************/
           
           HashMap hmFourthYearAmounts ;
             //case 2777
           if (numPeriods > 3) {
           if (cvPeriodAmounts.get(3) != null) {
                totalCategoryCS = new BigDecimal(0.0);
                hmFourthYearAmounts = (HashMap) cvPeriodAmounts.get(3);
        
                /************
                ** total cost
                *************/
                totalCost = new BigDecimal(hmFourthYearAmounts.get("TOTAL_COST").toString());
                totalCostSharing = new BigDecimal(hmFourthYearAmounts.get("COST_SHARING").toString());
                ed524Budget.setBudgetFederalFourthYearAmount(totalCost);
                ed524Budget.setBudgetNonFederalFourthYearAmount(totalCostSharing);
                  
                 totalCostAllYrs = totalCostAllYrs.add(totalCost);
                 totalCostSharingAllYrs = totalCostSharingAllYrs.add(totalCostSharing);
                 
                /************
                ** total direct cost
                *************/
                totalDirectCost = new BigDecimal(hmFourthYearAmounts.get("TOTAL_DIRECT_COST").toString());
                ed524Budget.setBudgetFederalDirectFourthYearAmount(totalDirectCost);
               totalDirectCostAllYrs = totalDirectCostAllYrs.add(totalDirectCost);
             
                /************
                ** total indirect cost
                *************/
                totalIndirectCost = new BigDecimal(hmFourthYearAmounts.get("TOTAL_INDIRECT_COST").toString()) == null ?
                               zero : new BigDecimal(hmFourthYearAmounts.get("TOTAL_INDIRECT_COST").toString());
                ed524Budget.setBudgetFederalIndirectFourthYearAmount(totalIndirectCost);
                totalIndirectCostAllYrs = totalIndirectCostAllYrs.add(totalIndirectCost);
                
                /******************
                 * total indirect cost sharing
                 ******************/
                hmIDCCostShare = ed524BudgetV11TxnBean.getIDCCostSharing(propNumber,4,version);
                if (hmIDCCostShare != null) {
                  indirectCS =  new BigDecimal(hmIDCCostShare.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmIDCCostShare.get("COST_SHARING").toString());
                }else
                    indirectCS = new BigDecimal("0");  
               
                ed524Budget.setBudgetNonFederalIndirectFourthYearAmount(indirectCS);
           
                 totalIndirectCostAllYrsCS = totalIndirectCostAllYrsCS.add(indirectCS);
                
                /************
                ** supplies
                *************/
                hmCategory = ed524BudgetV11TxnBean.getSupplies(propNumber,version, 4);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString());                
                ed524Budget.setBudgetFederalSuppliesFourthYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalSuppliesFourthYearAmount(categoryCostCS);
                 totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                 
                  totalSupplies = totalSupplies.add(categoryCost);
                 totalSuppliesCS = totalSuppliesCS.add(categoryCostCS);
                }
                /************
                ** construction
                *************/
                hmCategory = ed524BudgetV11TxnBean.getConstruction(propNumber,version, 4);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString());                
                ed524Budget.setBudgetFederalConstructionFourthYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalConstructionFourthYearAmount(categoryCostCS);
                 totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                 
                 totalConstruction = totalConstruction.add(categoryCost);
                 totalConstructionCS = totalConstructionCS.add(categoryCostCS);
                }
                /************
                ** other
                *************/
                hmCategory = ed524BudgetV11TxnBean.getOther(propNumber,version, 4);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString());    
                ed524Budget.setBudgetFederalOtherFourthYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalOtherFourthYearAmount(categoryCostCS);
                 totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                 
                 totalOther = totalOther.add(categoryCost);
                 totalOtherCS = totalOtherCS.add(categoryCostCS);
                }
                /************
                ** equipment
                *************/
                hmCategory = ed524BudgetV11TxnBean.getEquipment(propNumber,version, 4);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString()); 
                ed524Budget.setBudgetFederalEquipmentFourthYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalEquipmentFourthYearAmount(categoryCostCS);
                 totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                 
                  totalEquip = totalEquip.add(categoryCost);
                 totalEquipCS = totalEquipCS.add(categoryCostCS);
                }
                /************
                ** contractual
                *************/
                hmCategory = ed524BudgetV11TxnBean.getContractual(propNumber,version, 4);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString()); 
                ed524Budget.setBudgetFederalContractualFourthYearAmount(categoryCost);
                 ed524Budget.setBudgetNonFederalContractualFourthYearAmount(categoryCostCS);
                 totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                 
                 totalContractual = totalContractual.add(categoryCost);
                 totalContractualCS = totalContractualCS.add(categoryCostCS);
                }
                /************
                ** travel
                *************/
                hmCategory = ed524BudgetV11TxnBean.getTravel(propNumber,version, 4);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString()); 
                ed524Budget.setBudgetFederalTravelFourthYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalTravelFourthYearAmount(categoryCostCS);
                totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                
                 totalTravel = totalTravel.add(categoryCost);
                 totalTravelCS = totalTravelCS.add(categoryCostCS);
                }
                /************
                ** training
                *************/
                hmCategory = ed524BudgetV11TxnBean.getTraining(propNumber,version, 4);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString()); 
                ed524Budget.setBudgetFederalTrainingFourthYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalTrainingFourthYearAmount(categoryCostCS);
                
                totalTraining = totalTraining.add(categoryCost);
                 totalTrainingCS = totalTrainingCS.add(categoryCostCS);
                }
                /************
                ** fringe
                *************/
                hmCategory = ed524BudgetV11TxnBean.get_fringe(propNumber,version, 4);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("FRINGE").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("FRINGE").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("FRINGE_COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("FRINGE_COST_SHARING").toString()); 
                ed524Budget.setBudgetFederalFringeFourthYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalFringeFourthYearAmount(categoryCostCS);
                 totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                 
                  totalFringe = totalFringe.add(categoryCost);
                 totalFringeCS = totalFringeCS.add(categoryCostCS);
                }
                /************
                ** personnel
                *************/
                hmCategory = ed524BudgetV11TxnBean.getPersonnelCosts(propNumber,version, 4);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString()); 
                ed524Budget.setBudgetFederalPersonnelFourthYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalPersonnelFourthYearAmount(categoryCostCS);
                totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                
                totalPersonnel = totalPersonnel.add(categoryCost);
                 totalPersonnelCS = totalPersonnelCS.add(categoryCostCS);
                }
                
                ed524Budget.setBudgetNonFederalDirectFourthYearAmount(totalCategoryCS);
         //case 3210          totalCostSharingAllYrs = totalCostSharingAllYrs.add(totalCategoryCS);
           }
           }
            /**************
            **FIFTH YEAR
            **************/
           
           HashMap hmFifthYearAmounts ;
             //case 2777
           if (numPeriods > 4) {
           if (cvPeriodAmounts.get(4) != null) {
               totalCategoryCS = new BigDecimal(0.0);
                hmFifthYearAmounts = (HashMap) cvPeriodAmounts.get(4);
        
                /************
                ** total cost
                *************/
                totalCost = new BigDecimal(hmFifthYearAmounts.get("TOTAL_COST").toString());
                totalCostSharing = new BigDecimal(hmFifthYearAmounts.get("COST_SHARING").toString());
                ed524Budget.setBudgetFederalFifthYearAmount(totalCost);
                ed524Budget.setBudgetNonFederalFifthYearAmount(totalCostSharing);
                   
                 totalCostAllYrs = totalCostAllYrs.add(totalCost);
                totalCostSharingAllYrs = totalCostSharingAllYrs.add(totalCostSharing);
                 
                /************
                ** total direct cost
                *************/
                totalDirectCost = new BigDecimal(hmFifthYearAmounts.get("TOTAL_DIRECT_COST").toString());
                ed524Budget.setBudgetFederalDirectFifthYearAmount(totalDirectCost);
                 totalDirectCostAllYrs = totalDirectCostAllYrs.add(totalDirectCost);
             
                /************
                ** total indirect cost
                *************/
                totalIndirectCost = new BigDecimal(hmFifthYearAmounts.get("TOTAL_INDIRECT_COST").toString()) == null ?
                               zero : new BigDecimal(hmFifthYearAmounts.get("TOTAL_INDIRECT_COST").toString());
                ed524Budget.setBudgetFederalIndirectFifthYearAmount(totalIndirectCost);
               totalIndirectCostAllYrs = totalIndirectCostAllYrs.add(totalIndirectCost);
               
                 /******************
                 * total indirect cost sharing
                 ******************/
                hmIDCCostShare = ed524BudgetV11TxnBean.getIDCCostSharing(propNumber,5,version);
                if (hmIDCCostShare != null) {
                  indirectCS =  new BigDecimal(hmIDCCostShare.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmIDCCostShare.get("COST_SHARING").toString());
                }else
                    indirectCS = new BigDecimal("0");  
               
                ed524Budget.setBudgetNonFederalIndirectFifthYearAmount(indirectCS);
           
                 totalIndirectCostAllYrsCS = totalIndirectCostAllYrsCS.add(indirectCS);
                
                /************
                ** supplies
                *************/
                hmCategory = ed524BudgetV11TxnBean.getSupplies(propNumber,version, 5);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString());                
                ed524Budget.setBudgetFederalSuppliesFifthYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalSuppliesFifthYearAmount(categoryCostCS);
                totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                
                 totalSupplies = totalSupplies.add(categoryCost);
                 totalSuppliesCS = totalSuppliesCS.add(categoryCostCS);
                }
                /************
                ** construction
                *************/
                hmCategory = ed524BudgetV11TxnBean.getConstruction(propNumber,version, 5);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString());                
                ed524Budget.setBudgetFederalConstructionFifthYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalConstructionFifthYearAmount(categoryCostCS);
                totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                
                totalConstruction = totalConstruction.add(categoryCost);
                 totalConstructionCS = totalConstructionCS.add(categoryCostCS);
                }
                /************
                ** other
                *************/
                hmCategory = ed524BudgetV11TxnBean.getOther(propNumber,version, 5);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString());    
                ed524Budget.setBudgetFederalOtherFifthYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalOtherFifthYearAmount(categoryCostCS);
                totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                
                totalOther = totalOther.add(categoryCost);
                 totalOtherCS = totalOtherCS.add(categoryCostCS);
                }
                /************
                ** equipment
                *************/
                hmCategory = ed524BudgetV11TxnBean.getEquipment(propNumber,version, 5);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString()); 
                ed524Budget.setBudgetFederalEquipmentFifthYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalEquipmentFifthYearAmount(categoryCostCS);
                totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                
                 totalEquip = totalEquip.add(categoryCost);
                 totalEquipCS = totalEquipCS.add(categoryCostCS);
                }
                /************
                ** contractual
                *************/
                hmCategory = ed524BudgetV11TxnBean.getContractual(propNumber,version, 5);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString()); 
                ed524Budget.setBudgetFederalContractualFifthYearAmount(categoryCost);
                 ed524Budget.setBudgetNonFederalContractualFifthYearAmount(categoryCostCS);
                totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                
                 totalContractual = totalContractual.add(categoryCost);
                 totalContractualCS = totalContractualCS.add(categoryCostCS);
                }
                /************
                ** travel
                *************/
                hmCategory = ed524BudgetV11TxnBean.getTravel(propNumber,version, 5);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString()); 
                ed524Budget.setBudgetFederalTravelFifthYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalTravelFifthYearAmount(categoryCostCS);
                totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                
                 totalTravel = totalTravel.add(categoryCost);
                 totalTravelCS = totalTravelCS.add(categoryCostCS);
                }
                /************
                ** training
                *************/
                hmCategory = ed524BudgetV11TxnBean.getTraining(propNumber,version, 5);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString()); 
                ed524Budget.setBudgetFederalTrainingFifthYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalTrainingFifthYearAmount(categoryCostCS);
                
                totalTraining = totalTraining.add(categoryCost);
                 totalTrainingCS = totalTrainingCS.add(categoryCostCS);
                }
                /************
                ** fringe
                *************/
                hmCategory = ed524BudgetV11TxnBean.get_fringe(propNumber,version, 5);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("FRINGE").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("FRINGE").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("FRINGE_COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("FRINGE_COST_SHARING").toString()); 
                ed524Budget.setBudgetFederalFringeFifthYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalFringeFifthYearAmount(categoryCostCS);
                totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                
                 totalFringe = totalFringe.add(categoryCost);
                 totalFringeCS = totalFringeCS.add(categoryCostCS);
                }
                /************
                ** personnel
                *************/
                hmCategory = ed524BudgetV11TxnBean.getPersonnelCosts(propNumber,version, 5);
                if (hmCategory != null) {
                categoryCost = new BigDecimal(hmCategory.get("COST").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST").toString());
                categoryCostCS = new BigDecimal(hmCategory.get("COST_SHARING").toString()) == null ?
                        zero : new BigDecimal(hmCategory.get("COST_SHARING").toString()); 
                ed524Budget.setBudgetFederalPersonnelFifthYearAmount(categoryCost);
                ed524Budget.setBudgetNonFederalPersonnelFifthYearAmount(categoryCostCS);
                totalCategoryCS = totalCategoryCS.add(categoryCostCS);
                
                totalPersonnel = totalPersonnel.add(categoryCost);
                 totalPersonnelCS = totalPersonnelCS.add(categoryCostCS);
                }
                
                ed524Budget.setBudgetNonFederalDirectFifthYearAmount(totalCategoryCS);
       //case 3210            totalCostSharingAllYrs = totalCostSharingAllYrs.add(totalCategoryCS);
             }
           }
           ed524Budget.setBudgetFederalPersonnelTotalAmount(totalPersonnel);
           ed524Budget.setBudgetNonFederalPersonnelTotalAmount(totalPersonnelCS);
           ed524Budget.setBudgetFederalFringeTotalAmount(totalFringe);
           ed524Budget.setBudgetNonFederalFringeTotalAmount(totalFringeCS);
           ed524Budget.setBudgetFederalTravelTotalAmount(totalTravel);
           ed524Budget.setBudgetNonFederalTravelTotalAmount(totalTravelCS);
           ed524Budget.setBudgetFederalEquipmentTotalAmount(totalEquip);
           ed524Budget.setBudgetNonFederalEquipmentTotalAmount(totalEquipCS);
           ed524Budget.setBudgetFederalContractualTotalAmount(totalContractual);
           ed524Budget.setBudgetNonFederalContractualTotalAmount(totalContractualCS);
           ed524Budget.setBudgetFederalSuppliesTotalAmount(totalSupplies);
           ed524Budget.setBudgetNonFederalSuppliesTotalAmount(totalSuppliesCS);
           ed524Budget.setBudgetFederalConstructionTotalAmount(totalConstruction);
           ed524Budget.setBudgetNonFederalConstructionTotalAmount(totalConstructionCS);
           ed524Budget.setBudgetFederalTrainingTotalAmount(totalTraining);
           ed524Budget.setBudgetNonFederalTrainingTotalAmount(totalTrainingCS);
           ed524Budget.setBudgetFederalOtherTotalAmount(totalOther);
           ed524Budget.setBudgetNonFederalOtherTotalAmount(totalOtherCS);
           
           totalDirectCostAllYrsCS = 
              totalPersonnelCS.add(totalFringeCS).add(totalTravelCS).add(totalEquipCS).add(totalContractualCS).add(totalConstructionCS).add(totalSuppliesCS).add(totalOtherCS);
           ed524Budget.setBudgetNonFederalDirectTotalAmount(totalDirectCostAllYrsCS);
           
           ed524Budget.setBudgetNonFederalIndirectTotalAmount(totalIndirectCostAllYrsCS);
           
           
           ed524Budget.setBudgetFederalIndirectTotalAmount(totalIndirectCostAllYrs);
           ed524Budget.setBudgetFederalTotalAmount(totalCostAllYrs);
           
           ed524Budget.setBudgetFederalDirectTotalAmount(totalDirectCostAllYrs);
           ed524Budget.setBudgetNonFederalTotalAmount(totalCostSharingAllYrs);
            
           /********************
            *** INDIRECT COST INFO
            *******************/
           
           ED524BudgetType.IndirectCostType indirectCostType = objFactory.createED524BudgetTypeIndirectCostType();
           ED524BudgetType.IndirectCostType.ApprovingFederalAgencyType agency = objFactory.createED524BudgetTypeIndirectCostTypeApprovingFederalAgencyType();
           ED524BudgetType.IndirectCostType.OtherApprovingFederalAgencyType otherAgency =
                    objFactory.createED524BudgetTypeIndirectCostTypeOtherApprovingFederalAgencyType();
           ED524BudgetType.IndirectCostType.IndirectCostRateAgreementFromDateType fromDateType =
                    objFactory.createED524BudgetTypeIndirectCostTypeIndirectCostRateAgreementFromDateType();
           Date   fromDateDt;
           HashMap hmIDC = ed524BudgetV11TxnBean.getIDCInfo(propNumber);
           String fromDate = (String) hmIDC.get("IDC_FROM_DATE");
           String agencyName = (String) hmIDC.get("AGENCY");
           String restrictedQuestion = (String) hmIDC.get("RESTRICTED");
            
           if (fromDate.equals("NONE")){
               agency.setIsIndirectCostRateAgreementApproved("No");
              
           } else {
               fromDateType.setValue(convertDateStringToCalendar(fromDate));
               fromDateType.setIsIndirectCostRateAgreementApproved("Yes");
               indirectCostType.setIndirectCostRateAgreementFromDate(fromDateType);
               agency.setIsIndirectCostRateAgreementApproved("Yes");
                if (agencyName.equals("ED")){
                     agency.setValue("ED");
                }else {
                    agency.setValue("Other");
                    otherAgency.setApprovingFederalAgency("Other");
                    otherAgency.setValue(agencyName);
                    indirectCostType.setOtherApprovingFederalAgency(otherAgency);
                    indirectCostType.setIsIndirectCostRateAgreementApproved("Yes");
                    if (restrictedQuestion.length() > 1)
                        indirectCostType.setRestrictedIndirectCostRate(restrictedQuestion);
           
                    indirectCostType.setApprovingFederalAgency(agency);
                    ed524Budget.setIndirectCost(indirectCostType);
                }
               
           }
           }
        }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"SF424V2Stream","getSF424V2()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }    
        
        return ed524Budget;
    }
    
    private Calendar getTodayDate() {
      Calendar cal = Calendar.getInstance(TimeZone.getDefault()); 
      java.util.Date today = cal.getTime();
      cal.setTime(today);
      return cal;
    }
    
     private Calendar getCal(Date date){
        if(date==null)
            return null;
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(date);
        return cal;
    }
     
    public Calendar convertDateStringToCalendar(String dateStr)
    {
        java.util.GregorianCalendar calDate = new java.util.GregorianCalendar();
        DateUtils dtUtils = new DateUtils();
        if (dateStr != null)
        {    
            if (dateStr.indexOf('-')!= -1)
            { // if the format obtd is YYYY-MM-DD
              dateStr = dtUtils.formatDate(dateStr,"MM/dd/yyyy");
            }    
            calDate.set(Integer.parseInt(dateStr.substring(6,10)),
                        Integer.parseInt(dateStr.substring(0,2)) - 1,
                        Integer.parseInt(dateStr.substring(3,5))) ;
            
            return calDate ;
        }
        return null ;
     }
    
    private static BigDecimal convDoubleToBigDec(double dblCost){
    
         DecimalFormat myFormatter = new DecimalFormat("############.##");  
         BigDecimal bdCost = new BigDecimal(myFormatter.format(dblCost));
         return bdCost; 
    } 
 
 
    public Object getStream(java.util.HashMap ht) throws JAXBException, CoeusException, DBException {
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getEd524Budget();
    }
    
   }
    

