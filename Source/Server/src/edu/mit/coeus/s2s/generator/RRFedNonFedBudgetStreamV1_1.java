/*
 * RRFedNonFedBudgetStreamV1_1.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-43073
 * All rights reserved.
 */

package edu.mit.coeus.s2s.generator;

import edu.mit.coeus.s2s.generator.stream.*;

import edu.mit.coeus.organization.bean.OrganizationMaintenanceDataTxnBean;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceFormBean;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeFormBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.s2s.bean.*;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.s2s.util.S2SHashValue;

import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import gov.grants.apply.coeus.additionalequipment.AdditionalEquipmentListType;
import gov.grants.apply.coeus.extrakeyperson.ExtraKeyPersonListType;

import gov.grants.apply.forms.rr_fednonfedbudget_v1_1.*;
import gov.grants.apply.system.globallibrary_v2.*;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;
import javax.xml.bind.JAXBException;
import org.apache.fop.apps.FOPException;
import org.w3c.dom.Document;

/**
 * @author  jenlu
 **/

 public class RRFedNonFedBudgetStreamV1_1 extends S2SBaseStream{ 
    private gov.grants.apply.forms.rr_fednonfedbudget_v1_1.ObjectFactory objFactory;
//    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
    private gov.grants.apply.system.globallibrary_v2.ObjectFactory globallibraryObjFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory; 
    private gov.grants.apply.coeus.additionalequipment.ObjectFactory equipObjFactory; 
    private gov.grants.apply.coeus.extrakeyperson.ObjectFactory extraKeyPerObjFctory;    
    gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
    //txn beans
    private S2STxnBean s2sTxnBean;
    private OrganizationMaintenanceDataTxnBean orgMaintDataTxnBean;
    private ProposalDevelopmentTxnBean propDevTxnBean;
    
    //data beans
    private BudgetSummaryDataBean budgetSummaryBean;
    private CoeusVector cvPeriod;
    private ProposalDevelopmentFormBean propDevFormBean;
    private OrganizationMaintenanceFormBean orgMaintFormBean;
    
     //start case 2406
    private String organizationID;
    private String perfOrganizationID;
    //end case 2406
    
   
    private String propNumber;
    private UtilFactory utilFactory;
   
    /** Creates a new instance of RRFedNonFedBudgetStreamV1_1 */
    public RRFedNonFedBudgetStreamV1_1(){
        objFactory = new gov.grants.apply.forms.rr_fednonfedbudget_v1_1.ObjectFactory();
//        globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
        globallibraryObjFactory = new gov.grants.apply.system.globallibrary_v2.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        equipObjFactory = new gov.grants.apply.coeus.additionalequipment.ObjectFactory(); 
     
        s2sTxnBean = new S2STxnBean();
        budgetSummaryBean = new BudgetSummaryDataBean();
        propDevTxnBean = new ProposalDevelopmentTxnBean();
        
        orgMaintDataTxnBean = new OrganizationMaintenanceDataTxnBean();    
    } 
   
    private RRFedNonFedBudgetType  getRRFedNonFedBudget() throws CoeusXMLException,CoeusException,DBException{
        RRFedNonFedBudgetType rrFedNonFedBudget = null;
       
        try{
           //get proposal master info
           propDevFormBean = getPropDevData();
           //get applicant organization info
           //start case 2406
//           orgMaintFormBean = getOrgData(propDevFormBean.getOrganizationId());
            orgMaintFormBean = getOrgData();
            //end case 2406
           //get budget summary information
           budgetSummaryBean = getBudgetInfo();
           //get period information -cvPeriod is vector containing budget period data beans
           cvPeriod = budgetSummaryBean.getBudgetPeriods();
          
           //get existing attachment list  
           rrFedNonFedBudget = objFactory.createRRFedNonFedBudget();
           
           /**
            * FormVersion
            */
           rrFedNonFedBudget.setFormVersion("1.1");  
         
           rrFedNonFedBudget.setDUNSID(orgMaintFormBean.getDunsNumber());   
           
           /**BudgetType
           *  values for budget type are Project, Subaward/Consortium
           */
           rrFedNonFedBudget.setBudgetType("Project");
           
           /**
            * OrganizationName -- it is changed to min/maxlen 1-60 in v1.1 (was 1-120 in v1.0)
            */          
           rrFedNonFedBudget.setOrganizationName(getOrgName());
           
           /**
            * BudgetSummary
            */
           rrFedNonFedBudget.setBudgetSummary(getBudgetSummary());
          
           if (cvPeriod != null && cvPeriod.size() > 0 ){
               for ( int i = 0 ; i < cvPeriod.size(); i++ ){
                    //get period bean for this period   
                    BudgetPeriodDataBean periodBean = (BudgetPeriodDataBean) cvPeriod.elementAt(i);
                    if (i == 0) rrFedNonFedBudget.setBudgetYear1(getBudgetYear(periodBean));
                    if (i == 1) rrFedNonFedBudget.setBudgetYear2(getBudgetYear(periodBean));
                    if (i == 2) rrFedNonFedBudget.setBudgetYear3(getBudgetYear(periodBean));
                    if (i == 3) rrFedNonFedBudget.setBudgetYear4(getBudgetYear(periodBean));
                    if (i == 4) rrFedNonFedBudget.setBudgetYear5(getBudgetYear(periodBean));                   
               }
           }
                        
                  
         }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"RRFedNonFedBudgetStream","getRRFedNonFedBudget()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }
        return rrFedNonFedBudget;
    }
   
      
    private ProposalDevelopmentFormBean getPropDevData() throws DBException,CoeusException{
        if(propNumber==null) 
            throw new CoeusXMLException("Proposal Number is Null");
        return propDevTxnBean.getProposalDevelopmentDetails(propNumber);
    }
    
    //start case 2406
     private OrganizationMaintenanceFormBean getOrgData()
            throws CoeusXMLException, CoeusException,DBException{
        HashMap hmOrg = s2sTxnBean.getOrganizationID(propNumber,"O");
        if (hmOrg!= null && hmOrg.get("ORGANIZATION_ID") != null){
               organizationID = hmOrg.get("ORGANIZATION_ID").toString();           
        }
        return orgMaintDataTxnBean.getOrganizationMaintenanceDetails(organizationID);
    }
     
//     private OrganizationMaintenanceFormBean getOrgData(String orgID)
//       throws CoeusXMLException,CoeusException,DBException{
//        if(orgID==null) 
//            throw new CoeusXMLException("Organization id is Null");   
//        return orgMaintDataTxnBean.getOrganizationMaintenanceDetails(orgID);
//    }
     //end case 2406
    private BudgetSummaryDataBean getBudgetInfo()
       throws JAXBException,  CoeusException,DBException {     
 
     
       return s2sTxnBean.getBudgetInfo(propNumber);
   }
       
//start for budget summary    
   private  RRFedNonFedBudgetType.BudgetSummaryType getBudgetSummary()
        throws JAXBException, CoeusException {
       RRFedNonFedBudgetType.BudgetSummaryType budgetSummaryType = objFactory.createRRFedNonFedBudgetTypeBudgetSummaryType();
       SummaryDataType summaryData ;
       
       if (budgetSummaryBean.getCumTotalDirectCosts()!= null && budgetSummaryBean.getCumTotalDirectCostSharing() != null ){
           summaryData  = objFactory.createSummaryDataType();
           budgetSummaryType.setCumulativeFee(budgetSummaryBean.getCumFee());
           summaryData.setFederalSummary(budgetSummaryBean.getCumTotalDirectCosts());
           summaryData.setNonFederalSummary(budgetSummaryBean.getCumTotalDirectCostSharing());
           if (budgetSummaryBean.getCumTotalDirectCosts()!= null ){
               summaryData.setTotalFedNonFedSummary(budgetSummaryBean.getCumTotalDirectCosts().add(budgetSummaryBean.getCumTotalDirectCostSharing()));
           }else{
               summaryData.setTotalFedNonFedSummary(budgetSummaryBean.getCumTotalDirectCostSharing());
           }    
           budgetSummaryType.setCumulativeTotalFundsRequestedDirectCosts(summaryData);
       }
       
       if (budgetSummaryBean.getCumTotalIndirectCosts() != null && budgetSummaryBean.getCumTotalIndirectCostSharing() != null ) {
           summaryData  = objFactory.createSummaryDataType();
           summaryData.setFederalSummary(budgetSummaryBean.getCumTotalIndirectCosts());
           summaryData.setNonFederalSummary(budgetSummaryBean.getCumTotalIndirectCostSharing());
           if (budgetSummaryBean.getCumTotalIndirectCosts() != null){
                summaryData.setTotalFedNonFedSummary(budgetSummaryBean.getCumTotalIndirectCosts().add(budgetSummaryBean.getCumTotalIndirectCostSharing()));
           }else{
                summaryData.setTotalFedNonFedSummary(budgetSummaryBean.getCumTotalIndirectCostSharing());
           }
           budgetSummaryType.setCumulativeTotalFundsRequestedIndirectCost(summaryData);
       }
       
       if (budgetSummaryBean.getCumTotalCosts() != null && budgetSummaryBean.getCumTotalCostSharing() != null) {
           summaryData  = objFactory.createSummaryDataType();
           summaryData.setFederalSummary(budgetSummaryBean.getCumTotalCosts());
           summaryData.setNonFederalSummary(budgetSummaryBean.getCumTotalCostSharing());
           if (budgetSummaryBean.getCumTotalCosts() != null) {
                summaryData.setTotalFedNonFedSummary(budgetSummaryBean.getCumTotalCosts().add(budgetSummaryBean.getCumTotalCostSharing()));
           }else {
                summaryData.setTotalFedNonFedSummary(budgetSummaryBean.getCumTotalCostSharing());
           }

           budgetSummaryType.setCumulativeTotalFundsRequestedDirectIndirectCosts(summaryData);
       }
       summaryData  = objFactory.createSummaryDataType();
       summaryData.setFederalSummary(budgetSummaryBean.getCumEquipmentFunds());
       summaryData.setNonFederalSummary(budgetSummaryBean.getCumEquipmentNonFunds());
       if (budgetSummaryBean.getCumEquipmentFunds() != null){
            summaryData.setTotalFedNonFedSummary(budgetSummaryBean.getCumEquipmentFunds().add(budgetSummaryBean.getCumEquipmentNonFunds()));
       }else{
           summaryData.setTotalFedNonFedSummary(budgetSummaryBean.getCumEquipmentNonFunds());
       }
       
       RRFedNonFedBudgetType.BudgetSummaryType.CumulativeEquipmentsType cumEquips =
            objFactory.createRRFedNonFedBudgetTypeBudgetSummaryTypeCumulativeEquipmentsType();
       cumEquips.setCumulativeTotalFundsRequestedEquipment(summaryData);
       budgetSummaryType.setCumulativeEquipments(cumEquips);
       
       budgetSummaryType.setCumulativeOtherDirect(getCumOtherDirect(budgetSummaryBean));
       
       summaryData  = objFactory.createSummaryDataType();
       summaryData.setFederalSummary(budgetSummaryBean.getCumTotalFundsForOtherPersonnel());
       summaryData.setNonFederalSummary(budgetSummaryBean.getCumTotalNonFundsForOtherPersonnel());
       if (budgetSummaryBean.getCumTotalFundsForOtherPersonnel() != null){
            summaryData.setTotalFedNonFedSummary(budgetSummaryBean.getCumTotalFundsForOtherPersonnel().add(budgetSummaryBean.getCumTotalNonFundsForOtherPersonnel()));
       }else{
            summaryData.setTotalFedNonFedSummary(budgetSummaryBean.getCumTotalNonFundsForOtherPersonnel());
       }
       budgetSummaryType.setCumulativeTotalFundsRequestedOtherPersonnel(summaryData);
       
       summaryData  = objFactory.createSummaryDataType();
       summaryData.setFederalSummary(budgetSummaryBean.getCumTotalFundsForPersonnel());
       summaryData.setNonFederalSummary(budgetSummaryBean.getCumTotalNonFundsForPersonnel());
       if (budgetSummaryBean.getCumTotalFundsForPersonnel() != null){
            summaryData.setTotalFedNonFedSummary(budgetSummaryBean.getCumTotalFundsForPersonnel().add(budgetSummaryBean.getCumTotalNonFundsForPersonnel()));
       }else{
           summaryData.setTotalFedNonFedSummary(budgetSummaryBean.getCumTotalNonFundsForPersonnel());
       }
       budgetSummaryType.setCumulativeTotalFundsRequestedPersonnel(summaryData);
       
       summaryData  = objFactory.createSummaryDataType();
       summaryData.setFederalSummary(budgetSummaryBean.getCumTotalFundsForSrPersonnel());
       summaryData.setNonFederalSummary(budgetSummaryBean.getCumTotalNonFundsForSrPersonnel());
       if (budgetSummaryBean.getCumTotalFundsForSrPersonnel() != null ){
           summaryData.setTotalFedNonFedSummary(budgetSummaryBean.getCumTotalFundsForSrPersonnel().add(budgetSummaryBean.getCumTotalNonFundsForSrPersonnel()));
       }else{
           summaryData.setTotalFedNonFedSummary(budgetSummaryBean.getCumTotalNonFundsForSrPersonnel());
       }
       
       budgetSummaryType.setCumulativeTotalFundsRequestedSeniorKeyPerson(summaryData);
       
       budgetSummaryType.setCumulativeTotalNoOtherPersonnel(budgetSummaryBean.getCumNumOtherPersonnel());       
       budgetSummaryType.setCumulativeTrainee(getCumTrainee(budgetSummaryBean));
       budgetSummaryType.setCumulativeTravels(getCumTravel(budgetSummaryBean));
       
       return budgetSummaryType;
   }   
   
   public RRFedNonFedBudgetType.BudgetSummaryType.CumulativeOtherDirectType 
        getCumOtherDirect(BudgetSummaryDataBean budgetSummaryBean)
    throws CoeusXMLException, CoeusException, JAXBException {
        CoeusVector cvOtherDirect;   
        cvOtherDirect = budgetSummaryBean.getOtherDirectCosts();
        OtherDirectCostBean otherDirectCostBean = (OtherDirectCostBean) cvOtherDirect.elementAt(0);
    
        RRFedNonFedBudgetType.BudgetSummaryType.CumulativeOtherDirectType cumOther =
            objFactory.createRRFedNonFedBudgetTypeBudgetSummaryTypeCumulativeOtherDirectType();
        TotalDataType totalDataType  = objFactory.createTotalDataType();
        
        totalDataType.setFederal(otherDirectCostBean.getcomputer());
        totalDataType.setNonFederal(otherDirectCostBean.getComputerCostSharing());
        if (otherDirectCostBean.getcomputer() != null ){
            totalDataType.setTotalFedNonFed(otherDirectCostBean.getcomputer().add(otherDirectCostBean.getComputerCostSharing()));
        }else{
            totalDataType.setTotalFedNonFed(otherDirectCostBean.getComputerCostSharing());
        }
        cumOther.setCumulativeADPComputerServices(totalDataType);
        
        totalDataType  = objFactory.createTotalDataType();
        totalDataType.setFederal(otherDirectCostBean.getAlterations());
        totalDataType.setNonFederal(otherDirectCostBean.getAlterationsCostSharing());
        if (otherDirectCostBean.getAlterations() != null ){
            totalDataType.setTotalFedNonFed(otherDirectCostBean.getAlterations().add(otherDirectCostBean.getAlterationsCostSharing()));
        }else{
            totalDataType.setTotalFedNonFed(otherDirectCostBean.getAlterationsCostSharing());
        }
        cumOther.setCumulativeAlterationsAndRenovations(totalDataType);
        
        totalDataType  = objFactory.createTotalDataType();
        totalDataType.setFederal(otherDirectCostBean.getconsultants());
        totalDataType.setNonFederal(otherDirectCostBean.getConsultantsCostSharing());
        if (otherDirectCostBean.getconsultants() != null ){
            totalDataType.setTotalFedNonFed(otherDirectCostBean.getconsultants().add(otherDirectCostBean.getConsultantsCostSharing()));
        }else{
            totalDataType.setTotalFedNonFed(otherDirectCostBean.getConsultantsCostSharing());
        }
        cumOther.setCumulativeConsultantServices(totalDataType);
        
        totalDataType  = objFactory.createTotalDataType();
        totalDataType.setFederal(otherDirectCostBean.getEquipRental());
        totalDataType.setNonFederal(otherDirectCostBean.getEquipRentalCostSharing());
        totalDataType.setTotalFedNonFed(otherDirectCostBean.getEquipRental().add(otherDirectCostBean.getEquipRentalCostSharing()));
        cumOther.setCumulativeEquipmentFacilityRentalFees(totalDataType);       
        
        totalDataType  = objFactory.createTotalDataType();
        totalDataType.setFederal(otherDirectCostBean.getmaterials());
        totalDataType.setNonFederal(otherDirectCostBean.getMaterialsCostSharing());
        if (otherDirectCostBean.getmaterials() != null ){
            totalDataType.setTotalFedNonFed(otherDirectCostBean.getmaterials().add(otherDirectCostBean.getMaterialsCostSharing()));
        }else {
            totalDataType.setTotalFedNonFed(otherDirectCostBean.getMaterialsCostSharing());
        }
        cumOther.setCumulativeMaterialAndSupplies(totalDataType);        
        
        totalDataType  = objFactory.createTotalDataType();
        totalDataType.setFederal(otherDirectCostBean.getpublications());
        totalDataType.setNonFederal(otherDirectCostBean.getPublicationsCostSharing());
        if (otherDirectCostBean.getpublications() != null ) {
            totalDataType.setTotalFedNonFed(otherDirectCostBean.getpublications().add(otherDirectCostBean.getPublicationsCostSharing()));
        }else{
            totalDataType.setTotalFedNonFed(otherDirectCostBean.getPublicationsCostSharing());
        }
        cumOther.setCumulativePublicationCosts(totalDataType);
                
        totalDataType  = objFactory.createTotalDataType();
        totalDataType.setFederal(otherDirectCostBean.getsubAwards());
        totalDataType.setNonFederal(otherDirectCostBean.getSubAwardsCostSharing());
        if (otherDirectCostBean.getsubAwards() != null ) {
            totalDataType.setTotalFedNonFed(otherDirectCostBean.getsubAwards().add(otherDirectCostBean.getSubAwardsCostSharing()));
        }else{
            totalDataType.setTotalFedNonFed(otherDirectCostBean.getSubAwardsCostSharing());
        }
        cumOther.setCumulativeSubawardConsortiumContractualCosts(totalDataType);
        
        SummaryDataType summaryData  = objFactory.createSummaryDataType();
        summaryData.setFederalSummary(otherDirectCostBean.gettotalOtherDirect());
        summaryData.setNonFederalSummary(otherDirectCostBean.getTotalOtherDirectCostSharing());
        if (otherDirectCostBean.gettotalOtherDirect() != null ) {
            summaryData.setTotalFedNonFedSummary(otherDirectCostBean.gettotalOtherDirect().add(otherDirectCostBean.getTotalOtherDirectCostSharing()));
        }else{
            summaryData.setTotalFedNonFedSummary(otherDirectCostBean.getTotalOtherDirectCostSharing());
        }
        cumOther.setCumulativeTotalFundsRequestedOtherDirectCosts(summaryData);
        
        CoeusVector cvOtherTypes = new CoeusVector();
        cvOtherTypes = otherDirectCostBean.getOtherCosts();
        HashMap hmOthers = new HashMap();
        hmOthers = (HashMap) cvOtherTypes.get(0);
        totalDataType  = objFactory.createTotalDataType(); 
        totalDataType.setFederal(new BigDecimal(hmOthers.get("Cost") == null? "0" :hmOthers.get("Cost").toString()));
        totalDataType.setNonFederal(new BigDecimal(hmOthers.get("CostSharing") == null? "0" : hmOthers.get("CostSharing").toString()));
        totalDataType.setTotalFedNonFed(totalDataType.getFederal().add(totalDataType.getNonFederal()));
        cumOther.setCumulativeOther1DirectCost(totalDataType);  
        return cumOther;
  }
   
  public RRFedNonFedBudgetType.BudgetSummaryType.CumulativeTraineeType
                            getCumTrainee(BudgetSummaryDataBean budgetSummaryBean)
    throws CoeusXMLException, CoeusException, JAXBException {     
        
     RRFedNonFedBudgetType.BudgetSummaryType.CumulativeTraineeType cumTrainee =
            objFactory.createRRFedNonFedBudgetTypeBudgetSummaryTypeCumulativeTraineeType();
     TotalDataType totalDataType;
     cumTrainee.setCumulativeNoofTrainees(new BigInteger(Integer.toString(budgetSummaryBean.getparticipantCount())));
     
     totalDataType  = objFactory.createTotalDataType();
     totalDataType.setFederal(budgetSummaryBean.getpartOtherCost());
     totalDataType.setNonFederal(budgetSummaryBean.getPartOtherCostSharing());
     if (budgetSummaryBean.getpartOtherCost() != null ) {
        totalDataType.setTotalFedNonFed(budgetSummaryBean.getpartOtherCost().add(budgetSummaryBean.getPartOtherCostSharing()));
     }else {
         totalDataType.setTotalFedNonFed(budgetSummaryBean.getPartOtherCostSharing());
     }
     cumTrainee.setCumulativeOtherTraineeCost(totalDataType);
     
     SummaryDataType summaryData  = objFactory.createSummaryDataType();
     
     /*
     if (budgetSummaryBean.getpartOtherCost() != null &&  budgetSummaryBean.getpartStipendCost() != null ){
         summaryData.setFederalSummary(budgetSummaryBean.getpartOtherCost().add(
                                                             budgetSummaryBean.getpartStipendCost().add(
                                                             budgetSummaryBean.getpartTravelCost().add(
                                                             budgetSummaryBean.getPartSubsistence().add(
                                                             budgetSummaryBean.getPartTuition())))));
     }else if (budgetSummaryBean.getpartOtherCost() == null &&  budgetSummaryBean.getpartStipendCost() == null){
         summaryData.setFederalSummary(budgetSummaryBean.getpartTravelCost());
     }else if (budgetSummaryBean.getpartOtherCost() == null ){
         summaryData.setFederalSummary(budgetSummaryBean.getpartStipendCost().add(
                                                             budgetSummaryBean.getpartTravelCost()));
     }else{
         summaryData.setFederalSummary(budgetSummaryBean.getpartOtherCost().add(                                                            budgetSummaryBean.getpartStipendCost().add(
                                                             budgetSummaryBean.getpartTravelCost())));
     }     
     
     */
     
  
         summaryData.setFederalSummary(budgetSummaryBean.getpartOtherCost().add(
                                                             budgetSummaryBean.getpartStipendCost().add(
                                                             budgetSummaryBean.getpartTravelCost().add(
                                                             budgetSummaryBean.getPartSubsistence().add(
                                                             budgetSummaryBean.getPartTuition())))));
         
      /*   
      if(budgetSummaryBean.getPartOtherCostSharing() != null && budgetSummaryBean.getPartStipendCostSharing() != null){
         summaryData.setNonFederalSummary(budgetSummaryBean.getPartOtherCostSharing().add(
                                                             budgetSummaryBean.getPartStipendCostSharing().add(
                                                             budgetSummaryBean.getPartTravelCostSharing())));
     }else if (budgetSummaryBean.getPartOtherCostSharing() == null && budgetSummaryBean.getPartStipendCostSharing() != null){
         summaryData.setNonFederalSummary(budgetSummaryBean.getPartTravelCostSharing());
     }else if (budgetSummaryBean.getPartOtherCostSharing() == null ){
         summaryData.setNonFederalSummary(budgetSummaryBean.getPartStipendCostSharing().add(
                                                             budgetSummaryBean.getPartTravelCostSharing()));
     }else{
         summaryData.setNonFederalSummary(budgetSummaryBean.getPartOtherCostSharing().add(
                                                             budgetSummaryBean.getPartTravelCostSharing()));
     }
     */
         
      summaryData.setNonFederalSummary(budgetSummaryBean.getPartOtherCostSharing().add(
                                       budgetSummaryBean.getPartStipendCostSharing().add(
                                       budgetSummaryBean.getPartTravelCostSharing().add(
                                       budgetSummaryBean.getPartSubsistenceCostSharing().add(
                                       budgetSummaryBean.getPartTuitionCostSharing())))));
      
               
        
     if (summaryData.getFederalSummary() != null ){
         summaryData.setTotalFedNonFedSummary(summaryData.getFederalSummary().add(summaryData.getNonFederalSummary()));
     }else{
         summaryData.setTotalFedNonFedSummary(summaryData.getNonFederalSummary());
     }
      
    
     cumTrainee.setCumulativeTotalFundsRequestedTraineeCosts(summaryData);
     
     //total part stipends
     totalDataType  = objFactory.createTotalDataType();
     totalDataType.setFederal(budgetSummaryBean.getpartStipendCost());
     totalDataType.setNonFederal(budgetSummaryBean.getPartStipendCostSharing());
    
     if (budgetSummaryBean.getpartStipendCost() != null ){
        totalDataType.setTotalFedNonFed(budgetSummaryBean.getpartStipendCost().add(budgetSummaryBean.getPartStipendCostSharing()));
     }else{
        totalDataType.setTotalFedNonFed(budgetSummaryBean.getPartStipendCostSharing());
     } 
     cumTrainee.setCumulativeTraineeStipends(totalDataType);
     
     //total part travel
     totalDataType  = objFactory.createTotalDataType();
     totalDataType.setFederal(budgetSummaryBean.getpartTravelCost());
     totalDataType.setNonFederal(budgetSummaryBean.getPartTravelCostSharing());
     
     if (budgetSummaryBean.getpartTravelCost() != null ){
        totalDataType.setTotalFedNonFed(budgetSummaryBean.getpartTravelCost().add(budgetSummaryBean.getPartTravelCostSharing()));
     }else{
         totalDataType.setTotalFedNonFed(budgetSummaryBean.getPartTravelCostSharing()); 
     }
     cumTrainee.setCumulativeTraineeTravel(totalDataType);
   
     
     //total part subsistence  
     totalDataType  = objFactory.createTotalDataType();
     totalDataType.setFederal(budgetSummaryBean.getPartSubsistence());
     totalDataType.setNonFederal(budgetSummaryBean.getPartSubsistenceCostSharing());
     if (budgetSummaryBean.getPartSubsistence() != null ){
        totalDataType.setTotalFedNonFed(budgetSummaryBean.getPartSubsistence().add(budgetSummaryBean.getPartSubsistenceCostSharing()));
     }else{
         totalDataType.setTotalFedNonFed(budgetSummaryBean.getPartSubsistenceCostSharing()); 
     }
     cumTrainee.setCumulativeTraineeSubsistence(totalDataType);
     
     //total part tuition
     totalDataType  = objFactory.createTotalDataType();
     totalDataType.setFederal(budgetSummaryBean.getPartTuition());
     totalDataType.setNonFederal(budgetSummaryBean.getPartTuitionCostSharing());
     if (budgetSummaryBean.getPartTuition() != null ){
        totalDataType.setTotalFedNonFed(budgetSummaryBean.getPartTuition().add(budgetSummaryBean.getPartTuitionCostSharing()));
     }else{
         totalDataType.setTotalFedNonFed(budgetSummaryBean.getPartTuitionCostSharing()); 
     }
     cumTrainee.setCumulativeTraineeTuitionFeesHealthInsurance(totalDataType);
     
     return cumTrainee;
  }
   
   public RRFedNonFedBudgetType.BudgetSummaryType.CumulativeTravelsType
                            getCumTravel(BudgetSummaryDataBean budgetSummaryBean)
    throws CoeusXMLException, CoeusException, JAXBException {
     
    RRFedNonFedBudgetType.BudgetSummaryType.CumulativeTravelsType cumTravel =
            objFactory.createRRFedNonFedBudgetTypeBudgetSummaryTypeCumulativeTravelsType();
    TotalDataType totalDataType; 
    
    totalDataType  = objFactory.createTotalDataType();
    totalDataType.setFederal(budgetSummaryBean.getCumDomesticTravel());
    totalDataType.setNonFederal(budgetSummaryBean.getCumDomesticTravelNonFund());
    if (budgetSummaryBean.getCumDomesticTravel() != null ){
        totalDataType.setTotalFedNonFed(budgetSummaryBean.getCumDomesticTravel().add(budgetSummaryBean.getCumDomesticTravelNonFund()));
    }else{
        totalDataType.setTotalFedNonFed(budgetSummaryBean.getCumDomesticTravelNonFund());
    }
    cumTravel.setCumulativeDomesticTravelCosts(totalDataType);
     
    totalDataType  = objFactory.createTotalDataType();
    totalDataType.setFederal(budgetSummaryBean.getCumForeignTravel());
    totalDataType.setNonFederal(budgetSummaryBean.getCumForeignTravelNonFund());
    if (budgetSummaryBean.getCumForeignTravel() != null ){
        totalDataType.setTotalFedNonFed(budgetSummaryBean.getCumForeignTravel().add(budgetSummaryBean.getCumForeignTravelNonFund()));
    }else {
        totalDataType.setTotalFedNonFed(budgetSummaryBean.getCumForeignTravelNonFund());
    }
    cumTravel.setCumulativeForeignTravelCosts(totalDataType);
     
    SummaryDataType summaryData  = objFactory.createSummaryDataType();
    summaryData.setFederalSummary(budgetSummaryBean.getCumTravel());
    summaryData.setNonFederalSummary(budgetSummaryBean.getCumTravelNonFund());
    if (budgetSummaryBean.getCumTravel() != null ){
        summaryData.setTotalFedNonFedSummary(budgetSummaryBean.getCumTravel().add(budgetSummaryBean.getCumTravelNonFund()));
    }else {
        summaryData.setTotalFedNonFedSummary(budgetSummaryBean.getCumTravelNonFund());
    }
    cumTravel.setCumulativeTotalFundsRequestedTravel(summaryData);      
    return cumTravel;
  }
//end for budget summary
   
//start for budget year   
   public BudgetYear1DataType getBudgetYear( BudgetPeriodDataBean periodBean ) 
 
             throws CoeusXMLException,CoeusException,DBException,JAXBException{
        
           BudgetYear1DataType budgetYearData = objFactory.createBudgetYear1DataType();
           SummaryDataType summaryData ; 
           
           budgetYearData.setBudgetPeriod(Integer.toString(periodBean.getBudgetPeriod()));
           budgetYearData.setBudgetPeriodEndDate(getCal(periodBean.getEndDate()));
           budgetYearData.setBudgetPeriodStartDate(getCal(periodBean.getStartDate()));
           budgetYearData.setCognizantFederalAgency(periodBean.getCognizantFedAgency());
           
           summaryData  = objFactory.createSummaryDataType();
           summaryData.setFederalSummary(periodBean.getDirectCostsTotal());
           summaryData.setNonFederalSummary(periodBean.getTotalDirectCostSharing());
           if(periodBean.getDirectCostsTotal() != null ){
                summaryData.setTotalFedNonFedSummary(periodBean.getDirectCostsTotal().add(periodBean.getTotalDirectCostSharing()));
           }else{
                summaryData.setTotalFedNonFedSummary(periodBean.getTotalDirectCostSharing());
           }
           budgetYearData.setDirectCosts(summaryData);
           
           summaryData  = objFactory.createSummaryDataType();
           summaryData.setFederalSummary(periodBean.getTotalCosts());
           summaryData.setNonFederalSummary(periodBean.getCostSharingAmount());
           if (periodBean.getTotalCosts() != null){
               summaryData.setTotalFedNonFedSummary(periodBean.getTotalCosts().add(periodBean.getCostSharingAmount()));
           }else {
               summaryData.setTotalFedNonFedSummary(periodBean.getCostSharingAmount());
           }           
           budgetYearData.setTotalCosts(summaryData);
           
           summaryData  = objFactory.createSummaryDataType();
           summaryData.setFederalSummary(periodBean.getTotalCompensation());
           summaryData.setNonFederalSummary(periodBean.getTotalCompensationCostSharing());
           if (periodBean.getTotalCompensation() != null ){
                summaryData.setTotalFedNonFedSummary(periodBean.getTotalCompensation().add(periodBean.getTotalCompensationCostSharing()));
           }else{
                summaryData.setTotalFedNonFedSummary(periodBean.getTotalCompensationCostSharing());
           }
           budgetYearData.setTotalCompensation(summaryData);
          
           
           if (periodBean.getBudgetPeriod() == 1) {               
//               gov.grants.apply.system.attachments_v1.AttachedFileDataType budgJustAtt = getBudgetJustification(periodBean);
//               if (budgJustAtt.getFileName() != null) budgetYearData.setBudgetJustificationAttachment(budgJustAtt); 
              
                Attachment budgJustAttachment = getNarrative(131,periodBean,false);
                if (budgJustAttachment.getContent() != null){
                    attachedFileType = getAttachedFileType(budgJustAttachment,attachmentsObjFactory);
                    budgetYearData.setBudgetJustificationAttachment(attachedFileType);
                }
               
           }
           budgetYearData.setOtherPersonnel(getOtherPersonnel(periodBean));
           //case 2664 start
           BudgetYearDataType.IndirectCostsType indirectCostsType = getIndirectCosts(periodBean);
           if (!indirectCostsType.getIndirectCost().isEmpty())
               budgetYearData.setIndirectCosts(indirectCostsType);
//           budgetYearData.setIndirectCosts(getIndirectCosts(periodBean));
           //case 2664 end
           budgetYearData.setKeyPersons(getKeyPersons(periodBean));
           budgetYearData.setOtherDirectCosts(getOtherDirectCosts(periodBean));
           budgetYearData.setTravel(getTravel(periodBean));
           if (periodBean.getEquipment() != null && periodBean.getEquipment().size()> 0 ){
               budgetYearData.setEquipment(getEquipment(periodBean));
           }
           budgetYearData.setParticipantTraineeSupportCosts(getParticipant(periodBean));
            
        return budgetYearData;
    }
   
private  Attachment getNarrative (int narrativeTypeCodeToAttach, BudgetPeriodDataBean periodBean, boolean periodLoop)
    throws CoeusException, DBException{
        return getNarrative (narrativeTypeCodeToAttach, periodBean, periodLoop, null);
        
}

private  Attachment getNarrative (int narrativeTypeCodeToAttach, 
        BudgetPeriodDataBean periodBean, boolean periodLoop, String titleCheckStr)
    throws CoeusException, DBException{
         
     ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
     ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean ;
 
     String title = "", description="";
     int  narrativeTypeCode, moduleNum;
     String propNumber = periodBean.getProposalNumber();
   
     LinkedHashMap hmArg = new LinkedHashMap();

     Attachment attachment = new Attachment();
     
     
     Vector vctNarrative = proposalNarrativeTxnBean.getPropNarrativePDFForProposal(propNumber);
     
     int size=vctNarrative==null?0:vctNarrative.size();
  
     HashMap hmNarrative;
     for (int row=0; row < size;row++) {
        proposalNarrativePDFSourceBean =(ProposalNarrativePDFSourceBean) vctNarrative.elementAt(row);
       
        moduleNum = proposalNarrativePDFSourceBean.getModuleNumber();
        hmNarrative= new HashMap();
        hmNarrative = s2sTxnBean.getNarrativeInfo(propNumber, moduleNum);
        narrativeTypeCode = Integer.parseInt(hmNarrative.get("NARRATIVE_TYPE_CODE").toString());
        if (periodLoop)   title = hmNarrative.get("MODULE_TITLE").toString();
        description = hmNarrative.get("DESCRIPTION").toString();
     
        if(narrativeTypeCodeToAttach == narrativeTypeCode){
            //This is for handling an exceptional case of checking the title for finding the
            //right attachements if there are more than one attachement for the same type code
          if(titleCheckStr!=null && !titleCheckStr.equalsIgnoreCase(title)){
              continue;
          }
          //end patch
            hmArg.put(ContentIdConstants.MODULE_NUMBER, Integer.toString(moduleNum)); 
            hmArg.put(ContentIdConstants.DESCRIPTION,description);
            if (periodLoop)  hmArg.put(ContentIdConstants.TITLE, title);
            
            attachment = getAttachment(hmArg);
             if (attachment == null) {
             //attachment does not already exist - we need to get it and add it
                 String contentId = createContentId(hmArg);
                 
                  proposalNarrativePDFSourceBean = 
                       proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                attachment = new Attachment();
                 attachment.setContent( proposalNarrativePDFSourceBean.getFileBytes());
                String contentType = "application/octet-stream";
                attachment.setFileName(AttachedFileDataTypeStream.addExtension(contentId));
                attachment.setContentId(contentId);
                attachment.setContentType(contentType);
                                
                addAttachment(hmArg, attachment);         
                break;
            }
        
       }
        
    }       
    return attachment;
}

private BudgetYearDataType.OtherPersonnelType getOtherPersonnel(BudgetPeriodDataBean periodBean) 
     throws CoeusException , JAXBException, DBException{       
     
        OtherPersonnelBean otherPersonnelBean;
        String personnelType;
        OtherPersonnelDataType otherOtherPersonnelType;
         
        BudgetYearDataType.OtherPersonnelType otherPersonnelType =
                     objFactory.createBudgetYearDataTypeOtherPersonnelType();
        
        otherPersonnelType.setOtherPersonnelTotalNumber(periodBean.getOtherPersonnelTotalNumber());
        
        SummaryDataType summaryData  = objFactory.createSummaryDataType();
        summaryData.setFederalSummary(periodBean.getTotalOtherPersonnelFunds());
        summaryData.setNonFederalSummary(periodBean.getTotalOtherPersonnelNonFunds());
        if (periodBean.getTotalOtherPersonnelFunds() != null){
            summaryData.setTotalFedNonFedSummary(periodBean.getTotalOtherPersonnelFunds().add(periodBean.getTotalOtherPersonnelNonFunds()));
        }else {
            summaryData.setTotalFedNonFedSummary(periodBean.getTotalOtherPersonnelNonFunds());
        }
        otherPersonnelType.setTotalOtherPersonnelFund(summaryData);
    
        CoeusVector cvOtherPersonnel = periodBean.getOtherPersonnel();
        //cvOtherPersonnel is a vector of OtherPersonnelBeans
        
        BudgetYearDataType.OtherPersonnelType.PostDocAssociatesType postDocType =
                objFactory.createBudgetYearDataTypeOtherPersonnelTypePostDocAssociatesType();
        BudgetYearDataType.OtherPersonnelType.GraduateStudentsType gradStudentType =
                objFactory.createBudgetYearDataTypeOtherPersonnelTypeGraduateStudentsType();
        BudgetYearDataType.OtherPersonnelType.SecretarialClericalType secType =
                objFactory.createBudgetYearDataTypeOtherPersonnelTypeSecretarialClericalType();
        BudgetYearDataType.OtherPersonnelType.UndergraduateStudentsType underGradType =
                objFactory.createBudgetYearDataTypeOtherPersonnelTypeUndergraduateStudentsType();
     
        for (int i=0; i < cvOtherPersonnel.size();i++){
           //cvOtherPersonnel is a vector of OtherPersonnelBeans. there may be a bean for 
           // gradstudents, one for postdocs, one for secs, one for undergragds, and up to 6
           // for other types
    
           otherPersonnelBean = (OtherPersonnelBean) cvOtherPersonnel.elementAt(i);
           personnelType = otherPersonnelBean.getPersonnelType();
           if (personnelType == "PostDoc"){ 
            postDocType.setNumberOfPersonnel(otherPersonnelBean.getNumberPersonnel());
            postDocType.setProjectRole(otherPersonnelBean.getRole());
            postDocType.setCompensation(getSectBComp(otherPersonnelBean.getCompensation()));
            
            otherPersonnelType.setPostDocAssociates(postDocType);
            }
           else if (personnelType == "Grad"){
            gradStudentType.setNumberOfPersonnel(otherPersonnelBean.getNumberPersonnel());
            gradStudentType.setProjectRole(otherPersonnelBean.getRole());
            gradStudentType.setCompensation(getSectBComp(otherPersonnelBean.getCompensation()));
            
            otherPersonnelType.setGraduateStudents(gradStudentType);
           }
           else if (personnelType == "UnderGrad") {
            underGradType.setNumberOfPersonnel(otherPersonnelBean.getNumberPersonnel());
            underGradType.setProjectRole(otherPersonnelBean.getRole());
            underGradType.setCompensation(getSectBComp(otherPersonnelBean.getCompensation()));
            
            otherPersonnelType.setUndergraduateStudents(underGradType);
            }
           else if (personnelType == "Sec") {
            secType.setNumberOfPersonnel(otherPersonnelBean.getNumberPersonnel());
            secType.setProjectRole(otherPersonnelBean.getRole());
            secType.setCompensation(getSectBComp(otherPersonnelBean.getCompensation()));
            
            otherPersonnelType.setSecretarialClerical(secType);
             }
           else {
              otherOtherPersonnelType = objFactory.createOtherPersonnelDataType();
              otherOtherPersonnelType.setNumberOfPersonnel(otherPersonnelBean.getNumberPersonnel());
              otherOtherPersonnelType.setProjectRole(otherPersonnelBean.getRole());
              otherOtherPersonnelType.setCompensation(getSectBComp(otherPersonnelBean.getCompensation()));
              
              otherPersonnelType.getOther().add(otherOtherPersonnelType); 
           }
         }
             
       
  
        return otherPersonnelType;
    }

   
 public SectBCompensationDataType getSectBComp(CompensationBean compensationBean)
      throws CoeusXMLException,CoeusException,DBException{
        SectBCompensationDataType sectBcomp = null;
        try{
           sectBcomp = objFactory.createSectBCompensationDataType();
           sectBcomp.setAcademicMonths(compensationBean.getAcademicMonths());
           sectBcomp.setCalendarMonths(compensationBean.getCalendarMonths());
            //changes made for coeusqa-2532
           //sectBcomp.setFringeBenefits(compensationBean.getFringe());
           sectBcomp.setFringeBenefits(compensationBean.getFringe().add(compensationBean.getFringeCostSharing()));
           TotalDataType totalDataType  = objFactory.createTotalDataType();
           totalDataType.setFederal(compensationBean.getFundsRequested());
           totalDataType.setNonFederal(compensationBean.getNonFundsRequested());
           totalDataType.setTotalFedNonFed(compensationBean.getFundsRequested().add(compensationBean.getNonFundsRequested()));
           sectBcomp.setOtherTotal(totalDataType);
           //changes made for coeusqa-2532
       //    sectBcomp.setRequestedSalary(compensationBean.getRequestedSalary());
           sectBcomp.setRequestedSalary(compensationBean.getRequestedSalary().add(compensationBean.getCostSharingAmount()));
           sectBcomp.setSummerMonths(compensationBean.getSummerMonths());
              
           
         }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"CompensationStream","getSectBComp()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }
        return sectBcomp;
    }
 
 private BudgetYearDataType.IndirectCostsType getIndirectCosts(BudgetPeriodDataBean periodBean)
    throws JAXBException, CoeusException{
    
    BudgetYearDataType.IndirectCostsType indirectCostsType =
            objFactory.createBudgetYearDataTypeIndirectCostsType();
 
    IndirectCostBean indirectCostBean = periodBean.getIndirectCosts();
    
    SummaryDataType summaryData  = objFactory.createSummaryDataType();
    summaryData.setFederalSummary(indirectCostBean.getTotalIndirectCosts());
    summaryData.setNonFederalSummary(indirectCostBean.getTotalIndirectCostSharing());
    if (indirectCostBean.getTotalIndirectCosts() != null){
        summaryData.setTotalFedNonFedSummary(indirectCostBean.getTotalIndirectCosts().add(indirectCostBean.getTotalIndirectCostSharing()));
    }else{
        summaryData.setTotalFedNonFedSummary(indirectCostBean.getTotalIndirectCostSharing());
    }
    indirectCostsType.setTotalIndirectCosts(summaryData);
    
    BudgetYearDataType.IndirectCostsType.IndirectCostType indirectCostListType ;
    
    CoeusVector cvIndCostList = indirectCostBean.getIndirectCostDetails();
    //cvIndCostList - is a vector of IndirectCostDetailBeans
    for (int i = 0;i < cvIndCostList.size();i++){
        indirectCostListType = 
              objFactory.createBudgetYearDataTypeIndirectCostsTypeIndirectCostType();
         IndirectCostDetailBean indCostDetBean = new IndirectCostDetailBean();
         indCostDetBean = (IndirectCostDetailBean) cvIndCostList.elementAt(i);
         indirectCostListType.setBase(indCostDetBean.getBase());
        
         indirectCostListType.setCostType(indCostDetBean.getCostType());
         
         TotalDataType totalDataType  = objFactory.createTotalDataType();        
         totalDataType.setFederal(indCostDetBean.getFunds());
         totalDataType.setNonFederal(indCostDetBean.getCostSharing());
         if (indCostDetBean.getFunds() != null ){
            totalDataType.setTotalFedNonFed(indCostDetBean.getFunds().add(indCostDetBean.getCostSharing()));
         }else {
             totalDataType.setTotalFedNonFed(indCostDetBean.getCostSharing());
         }
         indirectCostListType.setFundRequested(totalDataType);
         indirectCostListType.setRate(indCostDetBean.getRate());
         
         indirectCostsType.getIndirectCost().add(indirectCostListType);
         
//     }
    }
    return indirectCostsType;
}
 
 private BudgetYearDataType.KeyPersonsType getKeyPersons(BudgetPeriodDataBean periodBean)
    throws JAXBException, CoeusException, DBException{
    
    BudgetYearDataType.KeyPersonsType keyPersons = objFactory.createBudgetYearDataTypeKeyPersonsType();
    
    SummaryDataType summaryData  = objFactory.createSummaryDataType();
    summaryData.setFederalSummary(periodBean.getTotalFundsKeyPersons());
    summaryData.setNonFederalSummary(periodBean.getTotalNonFundsKeyPersons());
    if (periodBean.getTotalFundsKeyPersons() != null){
        summaryData.setTotalFedNonFedSummary(periodBean.getTotalFundsKeyPersons().add(periodBean.getTotalNonFundsKeyPersons()));
    }else {
        summaryData.setTotalFedNonFedSummary(periodBean.getTotalNonFundsKeyPersons());
    }
    keyPersons.setTotalFundForKeyPersons(summaryData);
    
    BigDecimal extraFunds = new BigDecimal("0");
    BigDecimal extraNonFunds = new BigDecimal("0");
    CoeusVector cvExtraPersons = periodBean.getExtraKeyPersons();
    if (cvExtraPersons != null && cvExtraPersons.size() > 0 ) {
        extraKeyPerObjFctory = new gov.grants.apply.coeus.extrakeyperson.ObjectFactory();
        ExtraKeyPersonListType extraKeyPersonList = extraKeyPerObjFctory.createExtraKeyPersonList();
        extraKeyPersonList.setProposalNumber(periodBean.getProposalNumber());
        extraKeyPersonList.setBudgetPeriod(new BigInteger(Integer.toString(periodBean.getBudgetPeriod())));
       
        List extraPersonList = extraKeyPersonList.getKeyPersons();
          //case 2229 - implement multipis for version 4.3 - 
            //can delete this special handling for nih
         //case 2695 start - multiple pis for nih
        String role;
   //     int isNih = s2sTxnBean.getIsNIHSponsor(periodBean.getProposalNumber()); case 2229
        //case 2695 end - multiple pis for nih
        for (int i=0; i<cvExtraPersons.size();i++){
                KeyPersonBean keyPersonBean = (KeyPersonBean) cvExtraPersons.elementAt(i);
                extraFunds = extraFunds.add(keyPersonBean.getFundsRequested());
                extraNonFunds = extraNonFunds.add(keyPersonBean.getNonFundsRequested());
                //prepare extra key persons attachment
                  ExtraKeyPersonListType.KeyPersonsType extraKeyPersonType= extraKeyPerObjFctory.createExtraKeyPersonListTypeKeyPersonsType();
                  ExtraKeyPersonListType.KeyPersonsType.CompensationType compType = extraKeyPerObjFctory.createExtraKeyPersonListTypeKeyPersonsTypeCompensationType();
                  extraKeyPersonType.setFirstName(keyPersonBean.getFirstName());
                  extraKeyPersonType.setMiddleName(keyPersonBean.getMiddleName());
                  extraKeyPersonType.setLastName(keyPersonBean.getLastName());
                   //case 2695 start - multiple pis for nih
                  role = keyPersonBean.getRole();
         //case 2229
          //        if (isNih == 1) {
          //           if (role.equals("Co-PI"))    role = "PD/PI";
          //          }
                  extraKeyPersonType.setProjectRole(role);
              //  extraKeyPersonType.setProjectRole(keyPersonBean.getRole());
                  compType.setAcademicMonths(keyPersonBean.getAcademicMonths());
                  compType.setCalendarMonths(keyPersonBean.getCalendarMonths());
                  compType.setSummerMonths(keyPersonBean.getSummerMonths());
         
                   compType.setBaseSalary(keyPersonBean.getBaseSalary());
                 //change for coeusqa-2532
                 // compType.setFringeBenefits(keyPersonBean.getFringe());
                  compType.setFringeBenefits(keyPersonBean.getFringe().add(keyPersonBean.getFringeCostSharing()));
                  compType.setFundsRequested(keyPersonBean.getFundsRequested());
                   // change for coeusqa-2532
                  compType.setRequestedSalary(keyPersonBean.getRequestedSalary().add(keyPersonBean.getCostSharingAmount()));
                  compType.setNonFederal(keyPersonBean.getNonFundsRequested());
                  if (keyPersonBean.getFundsRequested() != null){
                      compType.setTotalFedNonFed(keyPersonBean.getFundsRequested().add(keyPersonBean.getNonFundsRequested()));
                  }else {
                      compType.setTotalFedNonFed(keyPersonBean.getNonFundsRequested());
                  }
          
                  extraKeyPersonType.setCompensation(compType);
                  extraPersonList.add(extraKeyPersonType);
                //end preparing extra key persons
        }   
        summaryData  = objFactory.createSummaryDataType();
        summaryData.setFederalSummary(extraFunds);
        summaryData.setNonFederalSummary(extraNonFunds);
        if (extraFunds != null){
            summaryData.setTotalFedNonFedSummary(extraFunds.add(extraNonFunds));
        }else {
             summaryData.setTotalFedNonFedSummary(extraNonFunds);
        }

        keyPersons.setTotalFundForAttachedKeyPersons(summaryData);
        
        //marshell it to XML doc
        CoeusXMLGenrator xmlGen = new CoeusXMLGenrator();
        Document extraKeyPerDoc = xmlGen.marshelObject( extraKeyPersonList,
                                "gov.grants.apply.coeus.extrakeyperson");
        byte[] pdfBytes = null;
        try{
            InputStream templateIS = getClass().getResourceAsStream("/edu/mit/coeus/s2s/template/ExtraKeyPersonAttachmentNonFed.xsl");
            byte[] tmplBytes = new byte[templateIS.available()];
            templateIS.read(tmplBytes);
            pdfBytes = xmlGen.generatePdfBytes(extraKeyPerDoc,tmplBytes,null,
                periodBean.getProposalNumber()+"_"+periodBean.getBudgetPeriod()+
                        "_ExKeyPerson");
        }catch(FOPException ex){
            UtilFactory.log(ex.getMessage(),ex, "RRFedNonFedBudgetStream", "getKeyPersons");
            throw new CoeusException(ex.getMessage());
        }catch(IOException ex){
            UtilFactory.log(ex.getMessage(),ex, "RRFedNonFedBudgetStream", "getKeyPersons");
            throw new CoeusException(ex.getMessage());
        }
        ProposalNarrativeFormBean propNarrBean = new ProposalNarrativeFormBean();
        propNarrBean.setProposalNumber(periodBean.getProposalNumber());
        propNarrBean.setModuleTitle("BudgetPeriod_"+periodBean.getBudgetPeriod());
        propNarrBean.setComments("Auto generated document for extra key persons");
        propNarrBean.setModuleStatusCode('C');
        propNarrBean.setNarrativeTypeCode(11);
        ProposalNarrativePDFSourceBean propNarrPDFBean = new ProposalNarrativePDFSourceBean();
        propNarrPDFBean.setProposalNumber(periodBean.getProposalNumber());
        propNarrPDFBean.setAcType("I");
        propNarrPDFBean.setFileBytes(pdfBytes);
        propNarrPDFBean.setFileName(AttachedFileDataTypeStream.addExtension(periodBean.getProposalNumber()+"_EXTRA_KEYPERSONS"));
        s2sTxnBean.insertAutoGenNarrativeDetails(propNarrBean, propNarrPDFBean,
                                periodBean.getBudgetPeriod()==1);
        //need to get key person attachment if there are more than 8 key persons
        Attachment keyPersAttachment = getNarrative(11,periodBean,true,"BudgetPeriod_"+periodBean.getBudgetPeriod());
        if (keyPersAttachment.getContent() != null){
            attachedFileType = getAttachedFileType(keyPersAttachment,attachmentsObjFactory);
            keyPersons.setAttachedKeyPersons(attachedFileType);
        }       
    }
    
    keyPersons.getKeyPerson().addAll(getKeyPerson(periodBean));
    return keyPersons;
}
   
 public CoeusVector getKeyPerson(BudgetPeriodDataBean periodBean)
        throws CoeusXMLException,CoeusException,DBException,JAXBException{
        CoeusVector cvKeyPersonsData = new CoeusVector();     
 
        CoeusVector cvKeyPersons = periodBean.getKeyPersons();
        //cvKeyPersons is vector of KeyPersonBeans
         //case 2695 start - multiple pis for nih
        //start case 2229
         String role;
//        int isNih = s2sTxnBean.getIsNIHSponsor(periodBean.getProposalNumber());
        //case 2695 end - multiple pis for nih
        //end case 2229
        
        for (int i=0; i < cvKeyPersons.size();i++){
            KeyPersonBean keyPersonBean = (KeyPersonBean) cvKeyPersons.elementAt(i);
            KeyPersonDataType keyPersonDataType = objFactory.createKeyPersonDataType();
            //case 2695 start - multiple pis for nih
            role = keyPersonBean.getRole();
            //start case 2229
//            if (isNih == 1) {
//                if (role.equals("Co-PD/PI"))    role = "PD/PI";
//            }
            //end case 2229
            keyPersonDataType.setProjectRole(role);
    //      keyPersonDataType.setProjectRole(keyPersonBean.getRole());
            keyPersonDataType.setName(getName(keyPersonBean));
            //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
            //keyPersonDataType.setCompensation(getCompensation(keyPersonBean));
            String baseSalary = "0.00";
            //If budget period is greater than 10 then display the 10th period base salary
            if(periodBean.getBudgetPeriod()>10){
                baseSalary = s2sTxnBean.getBaseSalaryForPeriod(periodBean.getProposalNumber(), periodBean.getVersion(),
                    10, keyPersonBean.getPersonId());
            }else{
                baseSalary = s2sTxnBean.getBaseSalaryForPeriod(periodBean.getProposalNumber(), periodBean.getVersion(),
                    periodBean.getBudgetPeriod(), keyPersonBean.getPersonId());
            }
            if(baseSalary == null ){
                keyPersonDataType.setCompensation(getCompensation(keyPersonBean));
            }else{
                BigDecimal bdBaseSalary = new BigDecimal("0");
                bdBaseSalary = new BigDecimal(baseSalary);
                bdBaseSalary = bdBaseSalary.setScale(2,BigDecimal.ROUND_HALF_UP);
                keyPersonDataType.setCompensation(getCompensation(keyPersonBean, bdBaseSalary));
            }
            //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
            cvKeyPersonsData.add(keyPersonDataType);            
        }
        return cvKeyPersonsData;
        
    }
 
  private  HumanNameDataType getName(KeyPersonBean keyPersonBean)
	throws CoeusException, JAXBException {
	HumanNameDataType humanNameDataType = globallibraryObjFactory.createHumanNameDataType();
        humanNameDataType.setFirstName(keyPersonBean.getFirstName());
        humanNameDataType.setLastName(keyPersonBean.getLastName());
        humanNameDataType.setMiddleName(keyPersonBean.getMiddleName());
        
        return humanNameDataType;
  }
  
  private KeyPersonCompensationDataType getCompensation(KeyPersonBean keyPersonBean)
	throws CoeusException, JAXBException {
      KeyPersonCompensationDataType keyPersonCompDataType =
                objFactory.createKeyPersonCompensationDataType();
      keyPersonCompDataType.setAcademicMonths(keyPersonBean.getAcademicMonths());
      keyPersonCompDataType.setBaseSalary(keyPersonBean.getBaseSalary());
      keyPersonCompDataType.setCalendarMonths(keyPersonBean.getCalendarMonths());
     // change for coeusqa-2532
//      keyPersonCompDataType.setFringeBenefits(keyPersonBean.getFringe());
      keyPersonCompDataType.setFringeBenefits(keyPersonBean.getFringe().add(keyPersonBean.getFringeCostSharing()));
      TotalDataType totalDataType  = objFactory.createTotalDataType();
      totalDataType.setFederal(keyPersonBean.getFundsRequested());
      totalDataType.setNonFederal(keyPersonBean.getNonFundsRequested());
      totalDataType.setTotalFedNonFed(keyPersonBean.getFundsRequested().add(keyPersonBean.getNonFundsRequested()));
      keyPersonCompDataType.setTotal(totalDataType);
         // change for coeusqa-2532
 //     keyPersonCompDataType.setRequestedSalary(keyPersonBean.getRequestedSalary());
       keyPersonCompDataType.setRequestedSalary(keyPersonBean.getRequestedSalary().add(keyPersonBean.getCostSharingAmount()));
      keyPersonCompDataType.setSummerMonths(keyPersonBean.getSummerMonths());
   
      return keyPersonCompDataType;
   }
 
private BudgetYearDataType.OtherDirectCostsType getOtherDirectCosts(BudgetPeriodDataBean periodBean)
    throws JAXBException, CoeusException{
    
    BudgetYearDataType.OtherDirectCostsType otherDirectCostsType =
            objFactory.createBudgetYearDataTypeOtherDirectCostsType();
    CoeusVector cvOtherDirectCosts = periodBean.getOtherDirectCosts();
    
    //there is only one element in the vector, which is an OtherDirectCostBean
    OtherDirectCostBean otherDirectCostBean = (OtherDirectCostBean) cvOtherDirectCosts.elementAt(0);
    TotalDataType totalDataType; 
    
    totalDataType  = objFactory.createTotalDataType();        
    totalDataType.setFederal(otherDirectCostBean.getcomputer());
    totalDataType.setNonFederal(otherDirectCostBean.getComputerCostSharing());
    if (otherDirectCostBean.getcomputer() != null ){
        totalDataType.setTotalFedNonFed(otherDirectCostBean.getcomputer().add(otherDirectCostBean.getComputerCostSharing()));
    }else {
        totalDataType.setTotalFedNonFed(otherDirectCostBean.getComputerCostSharing());
    }
    otherDirectCostsType.setADPComputerServices(totalDataType);
    
    totalDataType  = objFactory.createTotalDataType();        
    totalDataType.setFederal(otherDirectCostBean.getAlterations());
    totalDataType.setNonFederal(otherDirectCostBean.getAlterationsCostSharing());
    if (otherDirectCostBean.getAlterations() != null){
        totalDataType.setTotalFedNonFed(otherDirectCostBean.getAlterations().add(otherDirectCostBean.getAlterationsCostSharing()));
    }else{
         totalDataType.setTotalFedNonFed(otherDirectCostBean.getAlterationsCostSharing());
    }
    otherDirectCostsType.setAlterationsRenovations(totalDataType);
    
    totalDataType  = objFactory.createTotalDataType();        
    totalDataType.setFederal(otherDirectCostBean.getconsultants());
    totalDataType.setNonFederal(otherDirectCostBean.getConsultantsCostSharing());
    if (otherDirectCostBean.getconsultants() != null){
        totalDataType.setTotalFedNonFed(otherDirectCostBean.getconsultants().add(otherDirectCostBean.getConsultantsCostSharing()));
    }else {
        totalDataType.setTotalFedNonFed(otherDirectCostBean.getConsultantsCostSharing());
    }
    otherDirectCostsType.setConsultantServices(totalDataType);
    
    totalDataType  = objFactory.createTotalDataType();        
    totalDataType.setFederal(otherDirectCostBean.getEquipRental());
    totalDataType.setNonFederal(otherDirectCostBean.getEquipRentalCostSharing());
    if (otherDirectCostBean.getEquipRental() != null){
        totalDataType.setTotalFedNonFed(otherDirectCostBean.getEquipRental().add(otherDirectCostBean.getEquipRentalCostSharing()));
    }else {
        totalDataType.setTotalFedNonFed(otherDirectCostBean.getEquipRentalCostSharing());
    }
    otherDirectCostsType.setEquipmentRentalFee(totalDataType);
    
    totalDataType  = objFactory.createTotalDataType();        
    totalDataType.setFederal(otherDirectCostBean.getmaterials());
    totalDataType.setNonFederal(otherDirectCostBean.getMaterialsCostSharing());
    if (otherDirectCostBean.getmaterials() != null){
        totalDataType.setTotalFedNonFed(otherDirectCostBean.getmaterials().add(otherDirectCostBean.getMaterialsCostSharing()));
    }else {
        totalDataType.setTotalFedNonFed(otherDirectCostBean.getMaterialsCostSharing());
    }
    otherDirectCostsType.setMaterialsSupplies(totalDataType);
    
    totalDataType  = objFactory.createTotalDataType();        
    totalDataType.setFederal(otherDirectCostBean.getpublications());
    totalDataType.setNonFederal(otherDirectCostBean.getPublicationsCostSharing());
    if (otherDirectCostBean.getpublications() != null){
        totalDataType.setTotalFedNonFed(otherDirectCostBean.getpublications().add(otherDirectCostBean.getPublicationsCostSharing()));
    }else {
        totalDataType.setTotalFedNonFed(otherDirectCostBean.getPublicationsCostSharing());
    }
    otherDirectCostsType.setPublicationCosts(totalDataType);
    
    totalDataType  = objFactory.createTotalDataType();        
    totalDataType.setFederal(otherDirectCostBean.getsubAwards());
    totalDataType.setNonFederal(otherDirectCostBean.getSubAwardsCostSharing());
    if (otherDirectCostBean.getsubAwards()!= null){
        totalDataType.setTotalFedNonFed(otherDirectCostBean.getsubAwards().add(otherDirectCostBean.getSubAwardsCostSharing()));
    }else {
        totalDataType.setTotalFedNonFed(otherDirectCostBean.getSubAwardsCostSharing());
    }
    otherDirectCostsType.setSubawardConsortiumContractualCosts(totalDataType);
    
    SummaryDataType summaryData  = objFactory.createSummaryDataType();
    summaryData.setFederalSummary(otherDirectCostBean.gettotalOtherDirect());
    summaryData.setNonFederalSummary(otherDirectCostBean.getTotalOtherDirectCostSharing());
    if (otherDirectCostBean.gettotalOtherDirect() != null){
        summaryData.setTotalFedNonFedSummary(otherDirectCostBean.gettotalOtherDirect().add(otherDirectCostBean.getTotalOtherDirectCostSharing()));
    }else{
        summaryData.setTotalFedNonFedSummary(otherDirectCostBean.getTotalOtherDirectCostSharing());
    }
    otherDirectCostsType.setTotalOtherDirectCost(summaryData);     
   
    CoeusVector cvOthers = otherDirectCostBean.getOtherCosts();
    //cvOthers vector of hashmaps with cost and description   
        
    if (cvOthers != null && cvOthers.size() > 0 ){
        BudgetYearDataType.OtherDirectCostsType.OthersType othersType =
                    objFactory.createBudgetYearDataTypeOtherDirectCostsTypeOthersType();
    
        BudgetYearDataType.OtherDirectCostsType.OthersType.OtherType othersDetailsType =
                    objFactory.createBudgetYearDataTypeOtherDirectCostsTypeOthersTypeOtherType();
        for (int i = 0;i < cvOthers.size();i++){
             HashMap hmCosts = new HashMap();

             hmCosts = (HashMap) cvOthers.elementAt(i);
             othersDetailsType = objFactory.createBudgetYearDataTypeOtherDirectCostsTypeOthersTypeOtherType();
             totalDataType  = objFactory.createTotalDataType();   
             BigDecimal fedCost = new BigDecimal(hmCosts.get("Cost") == null? "0" :hmCosts.get("Cost").toString());
             BigDecimal nonFedCostSharing = new BigDecimal(hmCosts.get("CostSharing") == null? "0": hmCosts.get("CostSharing").toString());
             totalDataType.setFederal(fedCost);
             totalDataType.setNonFederal(nonFedCostSharing);
             totalDataType.setTotalFedNonFed(fedCost.add(nonFedCostSharing));
             othersDetailsType.setCost(totalDataType);
             othersDetailsType.setDescription(hmCosts.get("Description").toString());
             othersType.getOther().add(othersDetailsType);
        }

        otherDirectCostsType.setOthers(othersType);
    }
  
     return otherDirectCostsType;
}

private BudgetYearDataType.TravelType getTravel(BudgetPeriodDataBean periodBean)
    throws JAXBException, CoeusException{
    
    BudgetYearDataType.TravelType travelType = objFactory.createBudgetYearDataTypeTravelType();
    TotalDataType totalDataType; 
    
    totalDataType  = objFactory.createTotalDataType();        
    totalDataType.setFederal(periodBean.getDomesticTravelCost());
    totalDataType.setNonFederal(periodBean.getDomesticTravelCostSharing());
    if (periodBean.getDomesticTravelCost() != null){
        totalDataType.setTotalFedNonFed(periodBean.getDomesticTravelCost().add(periodBean.getDomesticTravelCostSharing()));
    }else{
        totalDataType.setTotalFedNonFed(periodBean.getDomesticTravelCostSharing());
    }
    travelType.setDomesticTravelCost(totalDataType);
    
    totalDataType  = objFactory.createTotalDataType();        
    totalDataType.setFederal(periodBean.getForeignTravelCost());
    totalDataType.setNonFederal(periodBean.getForeignTravelCostSharing());
    if (periodBean.getForeignTravelCost() != null ){
        totalDataType.setTotalFedNonFed(periodBean.getForeignTravelCost().add(periodBean.getForeignTravelCostSharing()));
    }else{
        totalDataType.setTotalFedNonFed(periodBean.getForeignTravelCostSharing());
    }
    travelType.setForeignTravelCost(totalDataType);
    
    SummaryDataType summaryData  = objFactory.createSummaryDataType();
    summaryData.setFederalSummary(periodBean.getTotalTravelCost());
    summaryData.setNonFederalSummary(periodBean.getTotalTravelCostSharing());
    if (periodBean.getTotalTravelCost() != null){
        summaryData.setTotalFedNonFedSummary(periodBean.getTotalTravelCost().add(periodBean.getTotalTravelCostSharing())); 
    }else{
        summaryData.setTotalFedNonFedSummary(periodBean.getTotalTravelCostSharing());
    }
    travelType.setTotalTravelCost(summaryData);
    
    return travelType;
}

private BudgetYearDataType.EquipmentType getEquipment(BudgetPeriodDataBean periodBean)
    throws JAXBException, CoeusException, DBException{
        
    BudgetYearDataType.EquipmentType equipmentType = objFactory.createBudgetYearDataTypeEquipmentType();
    BudgetYearDataType.EquipmentType.EquipmentListType equipmentListType ;
               
    CoeusVector cvEquipment = periodBean.getEquipment();
    CoeusVector cvEquipmentItems = new CoeusVector();
    EquipmentBean equipBean = new EquipmentBean();
    CostBean equipItemBean = new CostBean();
    TotalDataType  totalDataType;
    if (cvEquipment != null && cvEquipment.size() > 0){
      
        //cvEquipment is vector of equipmentBeans for this budget period - which actually
        // should just be one bean since costBeans are grouped by category
        equipBean = (EquipmentBean) cvEquipment.get(0);
        SummaryDataType summaryData  = objFactory.createSummaryDataType();
        summaryData.setFederalSummary(equipBean.getTotalFund());
        summaryData.setNonFederalSummary(equipBean.getTotalNonFund());
        if (equipBean.getTotalFund() != null){
            summaryData.setTotalFedNonFedSummary(equipBean.getTotalFund().add(equipBean.getTotalNonFund()));
        }else{
            summaryData.setTotalFedNonFedSummary(equipBean.getTotalNonFund());
        }
        equipmentType.setTotalFund(summaryData); 
          
        cvEquipmentItems = equipBean.getEquipmentList();
                
        for (int i = 0; i< cvEquipmentItems.size(); i++){
          
            equipItemBean = new CostBean();
            equipItemBean =(CostBean) cvEquipmentItems.get(i);
              
            equipmentListType = objFactory.createBudgetYearDataTypeEquipmentTypeEquipmentListType();
            totalDataType  = objFactory.createTotalDataType();        
            totalDataType.setFederal(equipItemBean.getCost());
            totalDataType.setNonFederal(equipItemBean.getCostSharing());
            if (equipItemBean.getCost() != null){
                totalDataType.setTotalFedNonFed(equipItemBean.getCost().add(equipItemBean.getCostSharing()));
            }else{
                totalDataType.setTotalFedNonFed(equipItemBean.getCostSharing());
            }
            equipmentListType.setFundsRequested(totalDataType);
            equipmentListType.setEquipmentItem(equipItemBean.getDescription()== null? 
                                             equipItemBean.getCategory() :  
                                             equipItemBean.getDescription());
                 
            equipmentType.getEquipmentList().add(equipmentListType);
          }
        
         //handle extra equipment. extra equipment needs to be in an attachment. 
         //create an equipment attachment and insert into narrative table
        CoeusVector additonalEquipList = equipBean.getExtraEquipmentList();
        if (additonalEquipList != null) {
            totalDataType  = objFactory.createTotalDataType();        
            totalDataType.setFederal(equipBean.getTotalExtraFund());
            totalDataType.setNonFederal(equipBean.getTotalExtraNonFund());
            if (equipBean.getTotalExtraFund() != null){
                totalDataType.setTotalFedNonFed(equipBean.getTotalExtraFund().add(equipBean.getTotalExtraNonFund()));
            }else{
                totalDataType.setTotalFedNonFed(equipBean.getTotalExtraNonFund());
            } 
            equipmentType.setTotalFundForAttachedEquipment(totalDataType);
            //Modifying to attach the autogenerated equipment attachment list
            //  equipmentType = getEquipmentAttachment(equipmentType,periodBean);
            AdditionalEquipmentListType extraEquipList = equipObjFactory.createAdditionalEquipmentList();
            List equipList = extraEquipList.getEquipmentList();
            int size = additonalEquipList.size();
            for(int i=0;i<size;i++){
                CostBean addEquipBean = (CostBean)additonalEquipList.get(i);
                AdditionalEquipmentListType.EquipmentListType equipListType = equipObjFactory.createAdditionalEquipmentListTypeEquipmentListType();
                equipListType.setFundsRequested(addEquipBean.getCost());
                equipListType.setNonFederal(addEquipBean.getCostSharing());
                if (addEquipBean.getCost() != null){
                    equipListType.setTotalFedNonFed(addEquipBean.getCost().add(addEquipBean.getCostSharing()));
                }else{
                    equipListType.setTotalFedNonFed(addEquipBean.getCostSharing());
                }
                equipListType.setEquipmentItem(addEquipBean.getDescription()== null?
                addEquipBean.getCategory() :
                    addEquipBean.getDescription());
                    equipList.add(equipListType);
               extraEquipList.setProposalNumber(periodBean.getProposalNumber());
               extraEquipList.setBudgetPeriod(new BigInteger(Integer.toString(periodBean.getBudgetPeriod())));
            }
            //marshell it to XML doc
            CoeusXMLGenrator xmlGen = new CoeusXMLGenrator();
            Document extraKeyPerDoc = xmlGen.marshelObject(extraEquipList,
            "gov.grants.apply.coeus.additionalequipment");
            byte[] pdfBytes = null;
            try{
                InputStream templateIS = getClass().getResourceAsStream("/edu/mit/coeus/s2s/template/AdditionalEquipmentAttachmentNonFed.xsl");
                byte[] tmplBytes = new byte[templateIS.available()];
                templateIS.read(tmplBytes);
                String debug = CoeusProperties.getProperty(CoeusPropertyKeys.GENERATE_XML_FOR_DEBUGGING);
                if(debug.equalsIgnoreCase("Y") || debug.equalsIgnoreCase("Yes")){
                    pdfBytes = xmlGen.generatePdfBytes(extraKeyPerDoc,tmplBytes,null,
                    periodBean.getProposalNumber()+"_"+
                    equipItemBean.getBudgetPeriod()+"_EXTRA_EQUIPMENT");
                }else{
                    pdfBytes = xmlGen.generatePdfBytes(extraKeyPerDoc,tmplBytes);
                }
            }catch(IOException ex){
                UtilFactory.log(ex.getMessage(),ex, "RRFedNonFedBudgetStream", "getEquipment");
                throw new CoeusException(ex.getMessage());
            }catch(FOPException ex){
                UtilFactory.log(ex.getMessage(),ex, "RRFedNonFedBudgetStream", "getEquipment");
                throw new CoeusException(ex.getMessage());
            }
            //Update to database
            ProposalNarrativeFormBean propNarrBean = new ProposalNarrativeFormBean();
            propNarrBean.setProposalNumber(periodBean.getProposalNumber());
            propNarrBean.setModuleTitle("BudgetPeriod_"+periodBean.getBudgetPeriod());
            propNarrBean.setComments("Auto generated document for Equipment");
            propNarrBean.setModuleStatusCode('C');
            propNarrBean.setNarrativeTypeCode(12);
            ProposalNarrativePDFSourceBean propNarrPDFBean = new ProposalNarrativePDFSourceBean();
            propNarrPDFBean.setProposalNumber(periodBean.getProposalNumber());
            propNarrPDFBean.setAcType("I");
            propNarrPDFBean.setFileBytes(pdfBytes);
            propNarrPDFBean.setFileName(AttachedFileDataTypeStream.addExtension(periodBean.getProposalNumber()+"_ADDITIONAL_EQUIPMENT"));
            s2sTxnBean.insertAutoGenNarrativeDetails(propNarrBean, propNarrPDFBean,
                        periodBean.getBudgetPeriod()==1);
            
            Attachment equipAttachment = getNarrative(12,periodBean,true,"BudgetPeriod_"+periodBean.getBudgetPeriod());    
            if (equipAttachment.getContent() != null){
                 attachedFileType = getAttachedFileType(equipAttachment, attachmentsObjFactory);
                 equipmentType.setAdditionalEquipmentsAttachment(attachedFileType);
            }
//            equipmentType = getEquipmentAttachment(equipmentType,periodBean);
            //end updation
        }//end if

//         budgetYearData.setEquipment(equipmentType);
        
     }
     return equipmentType;
   }

private BudgetYearDataType.ParticipantTraineeSupportCostsType getParticipant(BudgetPeriodDataBean periodBean)
    throws JAXBException, CoeusException{
        BudgetYearDataType.ParticipantTraineeSupportCostsType partType = objFactory.createBudgetYearDataTypeParticipantTraineeSupportCostsType();
        BudgetYearDataType.ParticipantTraineeSupportCostsType.OtherType partOther =
            objFactory.createBudgetYearDataTypeParticipantTraineeSupportCostsTypeOtherType();
        TotalDataType  totalDataType;
        
        totalDataType  = objFactory.createTotalDataType();        
        totalDataType.setFederal(periodBean.getpartStipendCost());
        totalDataType.setNonFederal(periodBean.getPartStipendCostSharing());
        if (periodBean.getpartStipendCost() != null){
            totalDataType.setTotalFedNonFed(periodBean.getpartStipendCost().add(periodBean.getPartStipendCostSharing())); 
        }else {
            totalDataType.setTotalFedNonFed(periodBean.getPartStipendCostSharing()); 
        }
        partType.setStipends(totalDataType);
        
        totalDataType  = objFactory.createTotalDataType();        
        totalDataType.setFederal(periodBean.getpartTravelCost());
        totalDataType.setNonFederal(periodBean.getPartTravelCostSharing());
        if (periodBean.getpartTravelCost() != null ){
            totalDataType.setTotalFedNonFed(periodBean.getpartTravelCost().add(periodBean.getPartTravelCostSharing()));
        }else{
            totalDataType.setTotalFedNonFed(periodBean.getPartTravelCostSharing());
        }
        partType.setTravel(totalDataType);
        
        totalDataType  = objFactory.createTotalDataType();        
        totalDataType.setFederal(periodBean.getPartSubsistence());
        totalDataType.setNonFederal(periodBean.getPartSubsistenceCostSharing());
        if (periodBean.getPartSubsistence() != null ){
            totalDataType.setTotalFedNonFed(periodBean.getPartSubsistence().add(periodBean.getPartSubsistenceCostSharing()));
        }else{
            totalDataType.setTotalFedNonFed(periodBean.getPartSubsistenceCostSharing());
        }
        partType.setSubsistence(totalDataType);
        
        totalDataType  = objFactory.createTotalDataType();        
        totalDataType.setFederal(periodBean.getPartTuition());
        totalDataType.setNonFederal(periodBean.getPartTuitionCostSharing());
        if (periodBean.getPartTuition() != null ){
            totalDataType.setTotalFedNonFed(periodBean.getPartTuition().add(periodBean.getPartTuitionCostSharing()));
        }else{
            totalDataType.setTotalFedNonFed(periodBean.getPartTuitionCostSharing());
        }
        partType.setTuitionFeeHealthInsurance(totalDataType);
        
        totalDataType  = objFactory.createTotalDataType();        
        totalDataType.setFederal(periodBean.getpartOtherCost());
        totalDataType.setNonFederal(periodBean.getPartOtherCostSharing());
        if (periodBean.getpartOtherCost() != null){
            totalDataType.setTotalFedNonFed(periodBean.getpartOtherCost().add(periodBean.getPartOtherCostSharing()));
        }else{
            totalDataType.setTotalFedNonFed(periodBean.getPartOtherCostSharing());
        }
        partOther.setCost(totalDataType);
        partOther.setDescription("Other");
        partType.setOther(partOther);
        partType.setParticipantTraineeNumber(new BigInteger(Integer.toString(periodBean.getparticipantCount())));
        
        SummaryDataType summaryData  = objFactory.createSummaryDataType();
        
        /*
        if (periodBean.getpartOtherCost() != null && periodBean.getpartStipendCost() != null){
            summaryData.setFederalSummary(periodBean.getpartOtherCost().add(periodBean.getpartStipendCost().add(
                                            periodBean.getpartTravelCost())));
        }else if (periodBean.getpartOtherCost() == null && periodBean.getpartStipendCost() == null){
            summaryData.setFederalSummary(periodBean.getpartTravelCost());
        }else if (periodBean.getpartOtherCost() == null){
            summaryData.setFederalSummary(periodBean.getpartStipendCost().add(
                                            periodBean.getpartTravelCost()));
        }else{
            summaryData.setFederalSummary(periodBean.getpartOtherCost().add(
                                            periodBean.getpartTravelCost()));
        }
        */
       
         summaryData.setFederalSummary(periodBean.getpartOtherCost().add(
                                            periodBean.getpartStipendCost().add(
                                            periodBean.getpartTravelCost().add(
                                            periodBean.getPartSubsistence().add(
                                            periodBean.getPartTuition())))));
      
        /*
        if (periodBean.getPartOtherCostSharing() != null && periodBean.getPartStipendCostSharing() != null) {
            summaryData.setNonFederalSummary(periodBean.getPartOtherCostSharing().add(periodBean.getPartStipendCostSharing().add(
                                            periodBean.getPartTravelCostSharing())));
        }else if (periodBean.getPartOtherCostSharing() == null && periodBean.getPartStipendCostSharing() == null){
            summaryData.setNonFederalSummary(periodBean.getPartTravelCostSharing());
        }else if (periodBean.getPartOtherCostSharing() == null){
            summaryData.setNonFederalSummary(periodBean.getPartStipendCostSharing().add(
                                            periodBean.getPartTravelCostSharing()));
        }else{
            summaryData.setNonFederalSummary(periodBean.getPartOtherCostSharing().add(
                                            periodBean.getPartTravelCostSharing()));
        }
         */
         
         summaryData.setNonFederalSummary(periodBean.getPartOtherCostSharing().add(
                                          periodBean.getPartStipendCostSharing().add(
                                          periodBean.getPartTravelCostSharing().add(
                                          periodBean.getPartSubsistenceCostSharing().add(
                                          periodBean.getPartTuitionCostSharing())))));
       
       
        
        if (summaryData.getFederalSummary() != null ){
            summaryData.setTotalFedNonFedSummary(summaryData.getFederalSummary().add(summaryData.getNonFederalSummary()));
        }else{
            summaryData.setTotalFedNonFedSummary(summaryData.getNonFederalSummary());
        }
        partType.setTotalCost(summaryData);
        return partType;
   }
   

private String getOrgName()
    throws JAXBException, CoeusException{
        String orgName;
         
        orgName = orgMaintFormBean.getOrganizationName();
    
        return orgName;
       
}
 
   
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
    
 
    public Object getStream(HashMap ht) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getRRFedNonFedBudget();
    }    

    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
    /**
     * Method to fetch the compensation details for the keyperson
     * @param KeyPersonBean
     * @param BigDecimal
     * @return KeyPersonCompensationDataType
     * @throws CoeusException if exception occur
     */
    private KeyPersonCompensationDataType getCompensation(KeyPersonBean keyPersonBean, BigDecimal bdBaseSalary)
    throws CoeusException, JAXBException {
        KeyPersonCompensationDataType keyPersonCompDataType =
                objFactory.createKeyPersonCompensationDataType();
        keyPersonCompDataType.setAcademicMonths(keyPersonBean.getAcademicMonths());
        //set the base salary for the respective period
        keyPersonCompDataType.setBaseSalary(bdBaseSalary);
        keyPersonCompDataType.setCalendarMonths(keyPersonBean.getCalendarMonths());
        keyPersonCompDataType.setFringeBenefits(keyPersonBean.getFringe().add(keyPersonBean.getFringeCostSharing()));
        TotalDataType totalDataType  = objFactory.createTotalDataType();
        totalDataType.setFederal(keyPersonBean.getFundsRequested());
        totalDataType.setNonFederal(keyPersonBean.getNonFundsRequested());
        totalDataType.setTotalFedNonFed(keyPersonBean.getFundsRequested().add(keyPersonBean.getNonFundsRequested()));
        keyPersonCompDataType.setTotal(totalDataType);
        keyPersonCompDataType.setRequestedSalary(keyPersonBean.getRequestedSalary().add(keyPersonBean.getCostSharingAmount()));
        keyPersonCompDataType.setSummerMonths(keyPersonBean.getSummerMonths());
        
        return keyPersonCompDataType;
    }
    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
}

