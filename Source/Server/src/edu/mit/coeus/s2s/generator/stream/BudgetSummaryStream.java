/*
 * @(#)BudgetYearDataTypeStream.java November 9, 2004
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.generator.stream;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.bean.*;
import edu.mit.coeus.s2s.generator.AttachmentValidator;
import gov.grants.apply.forms.rr_budget_v1.*;
import gov.grants.apply.system.globallibrary_v1.*;
 
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.TimeZone;
import java.util.Vector;
import javax.xml.bind.JAXBException;

/**
 * @author  Eleanor Shavell
 * @Created on November 8, 2004, 10:12 AM
 */

 public class BudgetSummaryStream extends AttachmentValidator{ 
    private gov.grants.apply.forms.rr_budget_v1.ObjectFactory objFactory;
    private gov.grants.apply.system.globallibrary_v1.ObjectFactory globalObjFactory;

    private BudgetPeriodDataBean periodBean;
    private BudgetYearDataType budgetYearData = null;
    private Hashtable propData;
    private String propNumber;
    private UtilFactory utilFactory;
    private Calendar calendar;
   
   
  /** Creates a new instance of BudgetYearDataTypeStream */
    public BudgetSummaryStream() {
        objFactory = new gov.grants.apply.forms.rr_budget_v1.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.globallibrary_v1.ObjectFactory();
      
      //  utilFactory = new UtilFactory();
    } 
   
  public RRBudgetType.BudgetSummaryType getBudgetSummary (BudgetSummaryDataBean budgetSummaryBean)
             throws CoeusXMLException,CoeusException,JAXBException{
       RRBudgetType.BudgetSummaryType budgetSummaryType = objFactory.createRRBudgetTypeBudgetSummaryType();
       budgetSummaryType.setCumulativeFee(budgetSummaryBean.getCumFee());
       budgetSummaryType.setCumulativeTotalFundsRequestedDirectCosts(budgetSummaryBean.getCumTotalDirectCosts());
       budgetSummaryType.setCumulativeTotalFundsRequestedIndirectCost(budgetSummaryBean.getCumTotalIndirectCosts());
       budgetSummaryType.setCumulativeTotalFundsRequestedDirectIndirectCosts(budgetSummaryBean.getCumTotalCosts());
       budgetSummaryType.setCumulativeEquipments( getCumEquipments(budgetSummaryBean));
       budgetSummaryType.setCumulativeOtherDirect(getCumOtherDirect(budgetSummaryBean));
       budgetSummaryType.setCumulativeTotalFundsRequestedOtherPersonnel(budgetSummaryBean.getCumTotalFundsForOtherPersonnel());
       budgetSummaryType.setCumulativeTotalFundsRequestedPersonnel(budgetSummaryBean.getCumTotalFundsForPersonnel());
       budgetSummaryType.setCumulativeTotalFundsRequestedSeniorKeyPerson(budgetSummaryBean.getCumTotalFundsForSrPersonnel());
       budgetSummaryType.setCumulativeTotalNoOtherPersonnel(budgetSummaryBean.getCumNumOtherPersonnel());
       budgetSummaryType.setCumulativeTrainee(getCumTrainee(budgetSummaryBean));
      budgetSummaryType.setCumulativeTravels(getCumTravel(budgetSummaryBean));
       
       return budgetSummaryType;
                 
  }
  
  public RRBudgetType.BudgetSummaryType.CumulativeEquipmentsType 
    getCumEquipments(BudgetSummaryDataBean budgetSummaryBean)
    throws CoeusXMLException, CoeusException, JAXBException {
        RRBudgetType.BudgetSummaryType.CumulativeEquipmentsType cumEquips =
            objFactory.createRRBudgetTypeBudgetSummaryTypeCumulativeEquipmentsType();
        cumEquips.setCumulativeTotalFundsRequestedEquipment(budgetSummaryBean.getCumEquipmentFunds());
        
        return cumEquips;
  }
  
    public RRBudgetType.BudgetSummaryType.CumulativeOtherDirectType 
        getCumOtherDirect(BudgetSummaryDataBean budgetSummaryBean)
    throws CoeusXMLException, CoeusException, JAXBException {
        CoeusVector cvOtherDirect;   
        cvOtherDirect = budgetSummaryBean.getOtherDirectCosts();
        //cvOtherDirect is vector of OtherDirectCostBeans - actually just one element
        //which contains a bean with cumulative totals from all periods
        OtherDirectCostBean otherDirectCostBean = (OtherDirectCostBean) cvOtherDirect.elementAt(0);
    
        RRBudgetType.BudgetSummaryType.CumulativeOtherDirectType cumOther =
            objFactory.createRRBudgetTypeBudgetSummaryTypeCumulativeOtherDirectType();
        
        cumOther.setCumulativeADPComputerServices(otherDirectCostBean.getcomputer());
        cumOther.setCumulativeAlterationsAndRenovations(otherDirectCostBean.getAlterations());
        cumOther.setCumulativeConsultantServices(otherDirectCostBean.getconsultants());
        cumOther.setCumulativeEquipmentFacilityRentalFees(otherDirectCostBean.getEquipRental());
        cumOther.setCumulativeMaterialAndSupplies(otherDirectCostBean.getmaterials());
        cumOther.setCumulativePublicationCosts(otherDirectCostBean.getpublications());
        cumOther.setCumulativeSubawardConsortiumContractualCosts(otherDirectCostBean.getsubAwards());
        cumOther.setCumulativeTotalFundsRequestedOtherDirectCosts(otherDirectCostBean.gettotalOtherDirect());
   //     cumOther.setCumulativeTotalFundsRequestedOtherDirectCosts(new BigDecimal("0"));
        CoeusVector cvOtherTypes = new CoeusVector();
        cvOtherTypes = otherDirectCostBean.getOtherCosts();
        HashMap hmOthers = new HashMap();
        hmOthers = (HashMap) cvOtherTypes.get(0);
        cumOther.setCumulativeOther1DirectCost(new BigDecimal(hmOthers.get("Cost").toString()));
        return cumOther;
  }
    
   public RRBudgetType.BudgetSummaryType.CumulativeTraineeType
                            getCumTrainee(BudgetSummaryDataBean budgetSummaryBean)
    throws CoeusXMLException, CoeusException, JAXBException {
     
        
     RRBudgetType.BudgetSummaryType.CumulativeTraineeType cumTrainee =
            objFactory.createRRBudgetTypeBudgetSummaryTypeCumulativeTraineeType();
        
     cumTrainee.setCumulativeNoofTrainees(new BigInteger(Integer.toString(budgetSummaryBean.getparticipantCount())));
     cumTrainee.setCumulativeOtherTraineeCost(budgetSummaryBean.getpartOtherCost());
     cumTrainee.setCumulativeTotalFundsRequestedTraineeCosts(budgetSummaryBean.getpartOtherCost().add(
                                                             budgetSummaryBean.getpartStipendCost().add(
                                                             budgetSummaryBean.getpartTravelCost().add(
                                                             budgetSummaryBean.getPartTuition().add(
                                                             budgetSummaryBean.getPartSubsistence())))));
 
     cumTrainee.setCumulativeTraineeStipends(budgetSummaryBean.getpartStipendCost());
     cumTrainee.setCumulativeTraineeTravel(budgetSummaryBean.getpartTravelCost());
     cumTrainee.setCumulativeTraineeSubsistence(budgetSummaryBean.getPartSubsistence());
     cumTrainee.setCumulativeTraineeTuitionFeesHealthInsurance(budgetSummaryBean.getPartTuition());
  
     
     return cumTrainee;
  }
   
   public RRBudgetType.BudgetSummaryType.CumulativeTravelsType
                            getCumTravel(BudgetSummaryDataBean budgetSummaryBean)
    throws CoeusXMLException, CoeusException, JAXBException {
     
    RRBudgetType.BudgetSummaryType.CumulativeTravelsType cumTravel =
            objFactory.createRRBudgetTypeBudgetSummaryTypeCumulativeTravelsType();
        
    cumTravel.setCumulativeDomesticTravelCosts(budgetSummaryBean.getCumDomesticTravel());
    cumTravel.setCumulativeForeignTravelCosts(budgetSummaryBean.getCumForeignTravel());
    cumTravel.setCumulativeTotalFundsRequestedTravel(budgetSummaryBean.getCumTravel());
    
    return cumTravel;
  }
   
   
   /*
  public BudgetYearDataType getBudgetYear1( BudgetPeriodDataBean periodBean) 
             throws CoeusXMLException,CoeusException,DBException{
       
        try{
           budgetYearData = objFactory.createBudgetYearDataType();
           budgetYearData.setBudgetPeriod(Integer.toString(periodBean.getBudgetPeriod()));
           budgetYearData.setBudgetPeriodEndDate(getCal(periodBean.getEndDate()));
           budgetYearData.setBudgetPeriodStartDate(getCal(periodBean.getStartDate()));
           budgetYearData.setCognizantFederalAgency(periodBean.getCognizantFedAgency());
           budgetYearData.setDirectCosts(periodBean.getDirectCostsTotal());
           budgetYearData.setTotalCosts(periodBean.getTotalCosts());
           budgetYearData.setTotalCompensation(periodBean.getTotalCompensation());
           
         }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"BudgetYearDataTy[eStream","getBudgetYear1()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }
        return budgetYearData;
    }
   */

   
    private static BigDecimal convDoubleToBigDec(double d){
        return new BigDecimal(d);
    }
    private Calendar getCal(Date date){
        if(date==null)
            return null;
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(date);
        return cal;
    }
    
    private Calendar getTodayDate() {
      Calendar cal = Calendar.getInstance(TimeZone.getDefault()); 
      java.util.Date today = cal.getTime();
      cal.setTime(today);
      return cal;
    }

}
