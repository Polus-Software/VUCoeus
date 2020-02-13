/*
 * RR_FedNonFedBudgetStream_V1_2.java
 */

package edu.vanderbilt.coeus.s2s.generator;

import edu.mit.coeus.s2s.generator.ContentIdConstants;
import edu.mit.coeus.s2s.generator.S2SBaseStream;
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
import gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.*;
import gov.grants.apply.system.globallibrary_v2.*;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
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


public class RR_FedNonFedBudgetStream_V1_2 extends S2SBaseStream { 
    private gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.ObjectFactory objFactory;
    private gov.grants.apply.system.globallibrary_v2.ObjectFactory globallibraryObjFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory; 
    private gov.grants.apply.coeus.additionalequipment.ObjectFactory equipObjFactory; 
    private gov.grants.apply.coeus.extrakeyperson.ObjectFactory extraKeyPerObjFctory;    
    private gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;

    // txn beans
    private S2STxnBean s2sTxnBean;
    private OrganizationMaintenanceDataTxnBean orgMaintDataTxnBean;
    private ProposalDevelopmentTxnBean propDevTxnBean;
    
    // data beans
    private BudgetSummaryDataBean budgetSummaryDataBean;
    private CoeusVector cvPeriod;
    private ProposalDevelopmentFormBean propDevFormBean;
    private OrganizationMaintenanceFormBean orgMaintFormBean;
    
    private String organizationID;
    private String propNumber;
    private static final BigDecimal ZERO = new BigDecimal(0); // JM hard-coded fee amount
   
    /** Creates a new instance of RR_FedNonFedBudgetStream_V1_2 */
    public RR_FedNonFedBudgetStream_V1_2(){
        objFactory = new gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.ObjectFactory();
        globallibraryObjFactory = new gov.grants.apply.system.globallibrary_v2.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        equipObjFactory = new gov.grants.apply.coeus.additionalequipment.ObjectFactory(); 
     
        s2sTxnBean = new S2STxnBean();
        budgetSummaryDataBean = new BudgetSummaryDataBean();
        propDevTxnBean = new ProposalDevelopmentTxnBean();
        orgMaintDataTxnBean = new OrganizationMaintenanceDataTxnBean();  
    } 
   
    private RRFedNonFedBudget12 getRRFedNonFedBudget() throws CoeusXMLException,CoeusException,DBException {
        RRFedNonFedBudget12 rrFedNonFedBudgetType = null;
       
        try {
           // get proposal master info
           propDevFormBean = getPropDevData();

           // get applicant organization info
           orgMaintFormBean = getOrgData();

           // get budget summary information
           budgetSummaryDataBean = getBudgetInfo();
           
           // get period information - cvPeriod is vector containing budget period data beans
           cvPeriod = budgetSummaryDataBean.getBudgetPeriods();
          
           // get existing attachment list  
           rrFedNonFedBudgetType = objFactory.createRRFedNonFedBudget12();
           
           // Form version
           rrFedNonFedBudgetType.setFormVersion("1.2");  
         
           // DUNS number
           rrFedNonFedBudgetType.setDUNSID(orgMaintFormBean.getDunsNumber());   
           
           /**
            * BudgetType
            *  values for budget type are Project, Subaward/Consortium
            */
           rrFedNonFedBudgetType.setBudgetType("Project");
           
           /**
            * OrganizationName -- it is min/maxlen 1-60 in v1.2 (was 1-120 in v1.0)
            */          
           rrFedNonFedBudgetType.setOrganizationName(getOrgName());
           
           /**
            * BudgetSummary
            */
           rrFedNonFedBudgetType.setBudgetSummary(getBudgetSummary());
          
           if (cvPeriod != null && cvPeriod.size() > 0 ){
               for (int i=0 ; i < cvPeriod.size(); i++ ){
                    //get period bean for this period   
                    BudgetPeriodDataBean periodBean = (BudgetPeriodDataBean) cvPeriod.elementAt(i);
                    rrFedNonFedBudgetType.getBudgetYear().add(getBudgetYear(periodBean));
                    
                    // Get budget justification from bean
             	   	if (periodBean.getBudgetPeriod() == 1) {               
             	   		Attachment budgJustAttachment = getNarrative(131,periodBean,false);
             	   		if (budgJustAttachment.getContent() != null){
             	   			attachedFileType = getAttachedFileType(budgJustAttachment,attachmentsObjFactory);
             	   			rrFedNonFedBudgetType.setBudgetJustificationAttachment(attachedFileType);
             	   		}
             	   	}
               }
           }
        }
        catch (JAXBException jaxbEx) {
            UtilFactory.log(jaxbEx.getMessage(),jaxbEx,"RR_FedNonFedBudgetStream_V1_2","getRRFedNonFedBudget()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }
        return rrFedNonFedBudgetType;
    }
      
    private ProposalDevelopmentFormBean getPropDevData() throws DBException,CoeusException{
        if(propNumber==null) 
            throw new CoeusXMLException("Proposal Number is Null");
        return propDevTxnBean.getProposalDevelopmentDetails(propNumber);
    }
    
    private OrganizationMaintenanceFormBean getOrgData() throws CoeusXMLException, CoeusException,DBException{
    	HashMap hmOrg = s2sTxnBean.getOrganizationID(propNumber,"O");
        if (hmOrg!= null && hmOrg.get("ORGANIZATION_ID") != null){
               organizationID = hmOrg.get("ORGANIZATION_ID").toString();           
        }
        return orgMaintDataTxnBean.getOrganizationMaintenanceDetails(organizationID);
    }
     
    private BudgetSummaryDataBean getBudgetInfo() throws JAXBException, CoeusException, DBException {     
    	return s2sTxnBean.getBudgetInfo(propNumber);
    }
    
    /* Cumulative Budget */
    private RRFedNonFedBudget12Type.BudgetSummaryType getBudgetSummary() throws JAXBException, CoeusException {
    	RRFedNonFedBudget12Type.BudgetSummaryType budgetSummaryType = objFactory.createRRFedNonFedBudget12TypeBudgetSummaryType();
    	SummaryDataType summaryData;
    	TotalDataType totalData;
       
    	// Senior Personnel (A) - one person is always required and both fed and nonfed must be populated; null check not needed
    	summaryData = objFactory.createSummaryDataType();
    	summaryData.setFederalSummary(budgetSummaryDataBean.getCumTotalFundsForSrPersonnel());
    	summaryData.setNonFederalSummary(budgetSummaryDataBean.getCumTotalNonFundsForSrPersonnel());
    	summaryData.setTotalFedNonFedSummary(budgetSummaryDataBean.getCumTotalFundsForSrPersonnel().add(budgetSummaryDataBean.getCumTotalNonFundsForSrPersonnel()));
    	budgetSummaryType.setCumulativeTotalFundsRequestedSeniorKeyPerson(summaryData);

    	// Other Personnel (B) 
    	summaryData = objFactory.createSummaryDataType();
    	summaryData.setFederalSummary(budgetSummaryDataBean.getCumTotalFundsForOtherPersonnel());
    	summaryData.setNonFederalSummary(budgetSummaryDataBean.getCumTotalNonFundsForOtherPersonnel());
    	if (budgetSummaryDataBean.getCumTotalFundsForOtherPersonnel() != null) {
            summaryData.setTotalFedNonFedSummary(budgetSummaryDataBean.getCumTotalFundsForOtherPersonnel().add(budgetSummaryDataBean.getCumTotalNonFundsForOtherPersonnel()));
       	}
       	else {
            summaryData.setTotalFedNonFedSummary(budgetSummaryDataBean.getCumTotalNonFundsForOtherPersonnel());
       	}
       	budgetSummaryType.setCumulativeTotalFundsRequestedOtherPersonnel(summaryData);
       
       	// Total Number Other Personnel - not required, check for nulls
       	budgetSummaryType.setCumulativeTotalNoOtherPersonnel(budgetSummaryDataBean.getCumNumOtherPersonnel());   
       	
       	// Total For Personnel (A + B)  - will never be null (see Section A above)
       	summaryData  = objFactory.createSummaryDataType();
       	summaryData.setFederalSummary(budgetSummaryDataBean.getCumTotalFundsForPersonnel());
       	summaryData.setNonFederalSummary(budgetSummaryDataBean.getCumTotalNonFundsForPersonnel());
       	if (budgetSummaryDataBean.getCumTotalFundsForPersonnel() != null) {
            summaryData.setTotalFedNonFedSummary(budgetSummaryDataBean.getCumTotalFundsForPersonnel().add(budgetSummaryDataBean.getCumTotalNonFundsForPersonnel()));
       	}
       	else {
           summaryData.setTotalFedNonFedSummary(budgetSummaryDataBean.getCumTotalNonFundsForPersonnel());
       	}
       	budgetSummaryType.setCumulativeTotalFundsRequestedPersonnel(summaryData);
       	
       	// Equipment (C) - check for nulls
       	summaryData = objFactory.createSummaryDataType();
       	summaryData.setFederalSummary(budgetSummaryDataBean.getCumEquipmentFunds());
       	summaryData.setNonFederalSummary(budgetSummaryDataBean.getCumEquipmentNonFunds());
       	if (budgetSummaryDataBean.getCumEquipmentFunds() != null) {
       		summaryData.setTotalFedNonFedSummary(budgetSummaryDataBean.getCumEquipmentFunds().add(budgetSummaryDataBean.getCumEquipmentNonFunds()));
       	} 
       	else {
       		summaryData.setTotalFedNonFedSummary(budgetSummaryDataBean.getCumEquipmentNonFunds());
       	}
       	budgetSummaryType.setCumulativeTotalFundsRequestedEquipment(summaryData);
       	
       	// Travel (D) - check for nulls
       	summaryData = objFactory.createSummaryDataType();
       	summaryData.setFederalSummary(budgetSummaryDataBean.getCumTravel());
       	summaryData.setNonFederalSummary(budgetSummaryDataBean.getCumTravelNonFund());
       	if (budgetSummaryDataBean.getCumTravel() != null) {
       		summaryData.setTotalFedNonFedSummary(budgetSummaryDataBean.getCumTravel().add(budgetSummaryDataBean.getCumTravelNonFund()));
       	} 
       	else {
       		summaryData.setTotalFedNonFedSummary(budgetSummaryDataBean.getCumTravelNonFund());
       	}
       	budgetSummaryType.setCumulativeTotalFundsRequestedTravel(summaryData);
       	
       	// D1 - Domestic Travel
       	totalData = objFactory.createTotalDataType();
       	totalData.setFederal(budgetSummaryDataBean.getCumDomesticTravel());
       	totalData.setNonFederal(budgetSummaryDataBean.getCumDomesticTravelNonFund());
       	if (budgetSummaryDataBean.getCumDomesticTravel() != null) {
       		totalData.setTotalFedNonFed(budgetSummaryDataBean.getCumDomesticTravel().add(budgetSummaryDataBean.getCumDomesticTravelNonFund()));
       	} 
       	else {
       		totalData.setTotalFedNonFed(budgetSummaryDataBean.getCumDomesticTravelNonFund());
       	}
       	budgetSummaryType.setCumulativeDomesticTravelCosts(totalData);
       	
       	// D2 - Foreign Travel
       	totalData = objFactory.createTotalDataType();
       	totalData.setFederal(budgetSummaryDataBean.getCumForeignTravel());
       	totalData.setNonFederal(budgetSummaryDataBean.getCumForeignTravelNonFund());
       	if (budgetSummaryDataBean.getCumForeignTravel() != null) {
       		totalData.setTotalFedNonFed(budgetSummaryDataBean.getCumForeignTravel().add(budgetSummaryDataBean.getCumForeignTravelNonFund()));
       	} 
       	else {
       		totalData.setTotalFedNonFed(budgetSummaryDataBean.getCumForeignTravelNonFund());
       	}
       	budgetSummaryType.setCumulativeForeignTravelCosts(totalData);

       	// Participant/Trainee Costs (E) - check for nulls
	    summaryData  = objFactory.createSummaryDataType();
	    summaryData.setFederalSummary(budgetSummaryDataBean.getpartOtherCost().add(
                                      budgetSummaryDataBean.getpartStipendCost().add(
                            		  budgetSummaryDataBean.getpartTravelCost().add(
                                      budgetSummaryDataBean.getPartSubsistence().add(
                                      budgetSummaryDataBean.getPartTuition())))));
	    summaryData.setNonFederalSummary(budgetSummaryDataBean.getPartOtherCostSharing().add(
	                                     budgetSummaryDataBean.getPartStipendCostSharing().add(
	                                     budgetSummaryDataBean.getPartTravelCostSharing().add(
	                                     budgetSummaryDataBean.getPartSubsistenceCostSharing().add(
	                                     budgetSummaryDataBean.getPartTuitionCostSharing())))));
	    if (summaryData.getFederalSummary() != null ) {
	    	summaryData.setTotalFedNonFedSummary(summaryData.getFederalSummary().add(summaryData.getNonFederalSummary()));
	    }
	    else {
	    	summaryData.setTotalFedNonFedSummary(summaryData.getNonFederalSummary());
	    }
	    budgetSummaryType.setCumulativeTotalFundsRequestedTraineeCosts(summaryData);
       	
       	// E1 - Tuition, fees, health insurance
       	totalData = objFactory.createTotalDataType();
       	totalData.setFederal(budgetSummaryDataBean.getPartTuition());
       	totalData.setNonFederal(budgetSummaryDataBean.getPartTuitionCostSharing());
       	if (budgetSummaryDataBean.getPartTuition() != null) {
       		totalData.setTotalFedNonFed(budgetSummaryDataBean.getPartTuition().add(budgetSummaryDataBean.getPartTuitionCostSharing()));
       	} 
       	else {
       		totalData.setTotalFedNonFed(budgetSummaryDataBean.getPartTuitionCostSharing());
       	}
       	budgetSummaryType.setCumulativeTraineeTuitionFeesHealthInsurance(totalData);
       	
       	// E2 - Stipends
       	totalData = objFactory.createTotalDataType();
       	totalData.setFederal(budgetSummaryDataBean.getpartStipendCost());
       	totalData.setNonFederal(budgetSummaryDataBean.getPartStipendCostSharing());
       	if (budgetSummaryDataBean.getpartStipendCost() != null) {
       		totalData.setTotalFedNonFed(budgetSummaryDataBean.getpartStipendCost().add(budgetSummaryDataBean.getPartStipendCostSharing()));
       	} 
       	else {
       		totalData.setTotalFedNonFed(budgetSummaryDataBean.getPartStipendCostSharing());
       	}
       	budgetSummaryType.setCumulativeTraineeStipends(totalData);       	
       	
       	// E3 - Travel
       	totalData = objFactory.createTotalDataType();
       	totalData.setFederal(budgetSummaryDataBean.getpartTravelCost());
       	totalData.setNonFederal(budgetSummaryDataBean.getPartTravelCostSharing());
       	if (budgetSummaryDataBean.getpartTravelCost() != null) {
       		totalData.setTotalFedNonFed(budgetSummaryDataBean.getpartTravelCost().add(budgetSummaryDataBean.getPartTravelCostSharing()));
       	} 
       	else {
       		totalData.setTotalFedNonFed(budgetSummaryDataBean.getPartTravelCostSharing());
       	}
       	budgetSummaryType.setCumulativeTraineeTravel(totalData);    
       	
       	// E4 - Subsistence
       	totalData = objFactory.createTotalDataType();
       	totalData.setFederal(budgetSummaryDataBean.getPartSubsistence());
       	totalData.setNonFederal(budgetSummaryDataBean.getPartSubsistenceCostSharing());
       	if (budgetSummaryDataBean.getPartSubsistence() != null) {
       		totalData.setTotalFedNonFed(budgetSummaryDataBean.getPartSubsistence().add(budgetSummaryDataBean.getPartSubsistenceCostSharing()));
       	} 
       	else {
       		totalData.setTotalFedNonFed(budgetSummaryDataBean.getPartSubsistenceCostSharing());
       	}
       	budgetSummaryType.setCumulativeTraineeSubsistence(totalData);   
       	
       	// E5 - Other
       	totalData = objFactory.createTotalDataType();
       	totalData.setFederal(budgetSummaryDataBean.getpartOtherCost());
       	totalData.setNonFederal(budgetSummaryDataBean.getPartOtherCostSharing());
       	if (budgetSummaryDataBean.getpartOtherCost() != null) {
       		totalData.setTotalFedNonFed(budgetSummaryDataBean.getpartOtherCost().add(budgetSummaryDataBean.getPartOtherCostSharing()));
       	} 
       	else {
       		totalData.setTotalFedNonFed(budgetSummaryDataBean.getPartOtherCostSharing());
       	}
       	budgetSummaryType.setCumulativeOtherTraineeCost(totalData);  
       	
       	// E6 - Number of Trainees - not required, check for nulls
       	budgetSummaryType.setCumulativeNoofTrainees(new BigInteger(Integer.toString(budgetSummaryDataBean.getparticipantCount()))); 
       	
       
       	// Other Direct Costs (F)
       	CoeusVector cvOtherDirect = budgetSummaryDataBean.getOtherDirectCosts();
	   	OtherDirectCostBean otherDirectCostBean = (OtherDirectCostBean) cvOtherDirect.elementAt(0);
	   	budgetSummaryType.setCumulativeMaterialAndSupplies(getCumulativeOtherDirectLineItem(otherDirectCostBean,0));
	   	budgetSummaryType.setCumulativePublicationCosts(getCumulativeOtherDirectLineItem(otherDirectCostBean,1));
	   	budgetSummaryType.setCumulativeConsultantServices(getCumulativeOtherDirectLineItem(otherDirectCostBean,2));
	   	budgetSummaryType.setCumulativeADPComputerServices(getCumulativeOtherDirectLineItem(otherDirectCostBean,3));
	   	budgetSummaryType.setCumulativeSubawardConsortiumContractualCosts(getCumulativeOtherDirectLineItem(otherDirectCostBean,4));
	   	budgetSummaryType.setCumulativeEquipmentFacilityRentalFees(getCumulativeOtherDirectLineItem(otherDirectCostBean,5));
	   	budgetSummaryType.setCumulativeAlterationsAndRenovations(getCumulativeOtherDirectLineItem(otherDirectCostBean,6));
	   	budgetSummaryType.setCumulativeOther1DirectCost(getCumulativeOtherDirectLineItem(otherDirectCostBean,7));
	   	budgetSummaryType.setCumulativeOther2DirectCost(getCumulativeOtherDirectLineItem(otherDirectCostBean,8));
	   	budgetSummaryType.setCumulativeOther3DirectCost(getCumulativeOtherDirectLineItem(otherDirectCostBean,9));
	   	
       	summaryData = objFactory.createSummaryDataType();
       	summaryData.setFederalSummary(otherDirectCostBean.gettotalOtherDirect());
       	summaryData.setNonFederalSummary(otherDirectCostBean.getTotalOtherDirectCostSharing());
  		summaryData.setTotalFedNonFedSummary(otherDirectCostBean.gettotalOtherDirect().add(otherDirectCostBean.getTotalOtherDirectCostSharing()));
       	budgetSummaryType.setCumulativeTotalFundsRequestedOtherDirectCosts(summaryData);
       	
       	// Total Direct Costs (G)
       	budgetSummaryType.setCumulativeTotalFundsRequestedDirectCosts(getCumulativeDirectCosts(budgetSummaryDataBean));	   
       	
       	// Total Indirect Costs (H)
       	budgetSummaryType.setCumulativeTotalFundsRequestedIndirectCost(getCumulativeIndirectCosts(budgetSummaryDataBean));
	    	   
       	// Total Direct and Indirect Costs (I)
       	budgetSummaryType.setCumulativeTotalFundsRequestedDirectIndirectCosts(getCumulativeDirectIndirectCosts(budgetSummaryDataBean));
	   
       	// Fee (J)
       	budgetSummaryType.setCumulativeFee(budgetSummaryDataBean.getCumFee());
 	   
       	// Total Costs and Fee (K)
       	budgetSummaryType.setCumulativeTotalCostsFee(getCumulativeTotalCostsFees(budgetSummaryDataBean));
 	   
       	return budgetSummaryType;
    }  
    
    public TotalDataType getCumulativeOtherDirectLineItem(OtherDirectCostBean otherDirectCostBean, int lineIndex) throws JAXBException {
    	TotalDataType totalDataType = objFactory.createTotalDataType();
    	CoeusVector cvOtherCosts = otherDirectCostBean.getOtherCosts();
    	BigDecimal federal = new BigDecimal(0);
    	BigDecimal nonfederal = new BigDecimal(0);
    	BigDecimal fednonfedtotal = new BigDecimal(0);
    	switch (lineIndex) {
    		// Materials and Supplies
	    	case 0:
	    		if (otherDirectCostBean.getmaterials() != null) {
		    		federal = otherDirectCostBean.getmaterials();
		    		nonfederal = otherDirectCostBean.getMaterialsCostSharing();
		    		fednonfedtotal = otherDirectCostBean.getmaterials().add(otherDirectCostBean.getMaterialsCostSharing());
		    		break;
	    		}
		    // Publication Costs
	    	case 1:
	    		if (otherDirectCostBean.getpublications() != null) {
		    		federal = otherDirectCostBean.getpublications();
		    		nonfederal = otherDirectCostBean.getPublicationsCostSharing();
		    		fednonfedtotal = otherDirectCostBean.getpublications().add(otherDirectCostBean.getPublicationsCostSharing());
		    		break;
	    		}
	    	// Consultant Services
	    	case 2:
	    		federal = otherDirectCostBean.getconsultants();
	    		nonfederal = otherDirectCostBean.getConsultantsCostSharing();
	    		fednonfedtotal = otherDirectCostBean.getconsultants().add(otherDirectCostBean.getConsultantsCostSharing());
	    		break;
	    	// ADP/Computer Services
	    	case 3:
	    		federal = otherDirectCostBean.getcomputer();
	    		nonfederal = otherDirectCostBean.getComputerCostSharing();
	    		fednonfedtotal = otherDirectCostBean.getcomputer().add(otherDirectCostBean.getComputerCostSharing());
	    		break;
		    // Subawards/Consortium/Contractual Costs
	    	case 4:
	    		federal = otherDirectCostBean.getsubAwards();
	    		nonfederal = otherDirectCostBean.getSubAwardsCostSharing();
	    		fednonfedtotal = otherDirectCostBean.getsubAwards().add(otherDirectCostBean.getSubAwardsCostSharing());
	    		break;
	    	// Equipment or Facility Rental/User Fees
	    	case 5:
	    		federal = otherDirectCostBean.getEquipRental();
	    		nonfederal = otherDirectCostBean.getEquipRentalCostSharing();
	    		fednonfedtotal = otherDirectCostBean.getEquipRental().add(otherDirectCostBean.getEquipRentalCostSharing());
	    		break;
	    	// Alterations or Renovations
	    	case 6:
	    		federal = otherDirectCostBean.getAlterations();
	    		nonfederal = otherDirectCostBean.getAlterationsCostSharing();
	    		fednonfedtotal = otherDirectCostBean.getAlterations().add(otherDirectCostBean.getAlterationsCostSharing());
	    		break;
		    // Other 1
	    	case 7:
	    		HashMap hmOthers = (HashMap) cvOtherCosts.get(0);
	    		federal = new BigDecimal (hmOthers.get("Cost") == null? "0" :hmOthers.get("Cost").toString());
	    		nonfederal = new BigDecimal(hmOthers.get("CostSharing") == null? "0" : hmOthers.get("CostSharing").toString());
	    		fednonfedtotal = (BigDecimal) federal.add(nonfederal);
	    		break;
	    	// Other 2
	    	case 8:
	    		// do nothing
	    		break;
	    	// Other 3
	    	case 9:
	    		// do nothing
	    		break;
	    	default:
	    		UtilFactory.log("Other Direct Cost index out of bounds");
    	}
    	
		totalDataType.setFederal(federal);
		totalDataType.setNonFederal(nonfederal);
		totalDataType.setTotalFedNonFed(fednonfedtotal);
    	return totalDataType;
    }
    
    private SummaryDataType getCumulativeDirectCosts(BudgetSummaryDataBean budgetSummaryDataBean) throws JAXBException {
    	// Since at least one person is required, and fed and nonfed are required for that person, total directs will always have a value;
    	// therefore, no need to check for nulls
    	SummaryDataType summaryDataType  = objFactory.createSummaryDataType();
    	summaryDataType.setFederalSummary(budgetSummaryDataBean.getCumTotalDirectCosts());
		summaryDataType.setNonFederalSummary(budgetSummaryDataBean.getCumTotalDirectCostSharing());
		summaryDataType.setTotalFedNonFedSummary(budgetSummaryDataBean.getCumTotalDirectCosts().add(budgetSummaryDataBean.getCumTotalDirectCostSharing()));
    	return summaryDataType;
    }
    
    private SummaryDataType getCumulativeIndirectCosts(BudgetSummaryDataBean budgetSummaryDataBean) throws JAXBException {
    	SummaryDataType summaryDataType  = objFactory.createSummaryDataType();
    	if (budgetSummaryDataBean.getCumTotalIndirectCosts() != null && budgetSummaryDataBean.getCumTotalIndirectCostSharing() != null) {
    		summaryDataType.setFederalSummary(budgetSummaryDataBean.getCumTotalIndirectCosts());
    		summaryDataType.setNonFederalSummary(budgetSummaryDataBean.getCumTotalIndirectCostSharing());
    		summaryDataType.setTotalFedNonFedSummary(budgetSummaryDataBean.getCumTotalIndirectCosts().add(budgetSummaryDataBean.getCumTotalIndirectCostSharing()));
    	}
    	else if (budgetSummaryDataBean.getCumTotalIndirectCosts() != null) {
    		summaryDataType.setFederalSummary(budgetSummaryDataBean.getCumTotalIndirectCosts());
    		summaryDataType.setTotalFedNonFedSummary(budgetSummaryDataBean.getCumTotalIndirectCosts());
    	}
    	else if (budgetSummaryDataBean.getCumTotalIndirectCostSharing() != null) {
    		summaryDataType.setNonFederalSummary(budgetSummaryDataBean.getCumTotalIndirectCostSharing());
    		summaryDataType.setTotalFedNonFedSummary(budgetSummaryDataBean.getCumTotalIndirectCostSharing());
    	}
    	return summaryDataType;
    }
    
    private SummaryDataType getCumulativeDirectIndirectCosts(BudgetSummaryDataBean budgetSummaryDataBean) throws JAXBException {
    	SummaryDataType summaryDataType  = objFactory.createSummaryDataType();
    	summaryDataType.setFederalSummary(budgetSummaryDataBean.getCumTotalCosts());
		summaryDataType.setNonFederalSummary(budgetSummaryDataBean.getCumTotalCostSharing());
		summaryDataType.setTotalFedNonFedSummary(budgetSummaryDataBean.getCumTotalCosts().add(budgetSummaryDataBean.getCumTotalCostSharing()));
    	return summaryDataType;
    }
    
    private SummaryDataType getCumulativeTotalCostsFees(BudgetSummaryDataBean budgetSummaryDataBean) throws JAXBException {
    	SummaryDataType summaryDataType  = objFactory.createSummaryDataType();
    	summaryDataType.setFederalSummary(budgetSummaryDataBean.getCumTotalCosts().add(budgetSummaryDataBean.getCumFee()));
		summaryDataType.setNonFederalSummary(budgetSummaryDataBean.getCumTotalCostSharing());
		summaryDataType.setTotalFedNonFedSummary(budgetSummaryDataBean.getCumTotalCosts().add(budgetSummaryDataBean.getCumFee()).add(budgetSummaryDataBean.getCumTotalCostSharing()));
    	return summaryDataType;
    }
    
    
/*   
    public void getCumulativeOtherDirectOld(BudgetSummaryDataBean budgetSummaryDataBean)
		   throws CoeusXMLException, CoeusException, JAXBException {
    	
    	CoeusVector cvOtherDirect;   
        cvOtherDirect = budgetSummaryDataBean.getOtherDirectCosts();
        OtherDirectCostBean otherDirectCostBean = (OtherDirectCostBean) cvOtherDirect.elementAt(0);
        TotalDataType totalDataType = objFactory.createTotalDataType();
        
        totalDataType.setFederal(otherDirectCostBean.getcomputer());
        totalDataType.setNonFederal(otherDirectCostBean.getComputerCostSharing());
        if (otherDirectCostBean.getcomputer() != null ){
            totalDataType.setTotalFedNonFed(otherDirectCostBean.getcomputer().add(otherDirectCostBean.getComputerCostSharing()));
        }else{
            totalDataType.setTotalFedNonFed(otherDirectCostBean.getComputerCostSharing());
        }
        rrFedNonFedBudget.setCumulativeADPComputerServices(totalDataType);
        
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
*/   
    
    //end for budget summary
   
    // Budget Year  
    public BudgetYearDataType getBudgetYear(BudgetPeriodDataBean periodBean) throws CoeusXMLException,CoeusException,DBException,JAXBException{
        
    	BudgetYearDataType budgetYearData = objFactory.createBudgetYearDataType();
        SummaryDataType summaryData; 
           
        // Header fields
        budgetYearData.setBudgetPeriod(Integer.toString(periodBean.getBudgetPeriod()));
        budgetYearData.setBudgetPeriodStartDate(getCal(periodBean.getStartDate()));
        budgetYearData.setBudgetPeriodEndDate(getCal(periodBean.getEndDate()));
        budgetYearData.setCognizantFederalAgency(periodBean.getCognizantFedAgency());
       
        summaryData = objFactory.createSummaryDataType();
        summaryData.setFederalSummary(periodBean.getDirectCostsTotal());
        summaryData.setNonFederalSummary(periodBean.getTotalDirectCostSharing());
        
        if (periodBean.getDirectCostsTotal() != null) {
        	summaryData.setTotalFedNonFedSummary(periodBean.getDirectCostsTotal().add(periodBean.getTotalDirectCostSharing()));
        }
        else {
        	summaryData.setTotalFedNonFedSummary(periodBean.getTotalDirectCostSharing());
        }
        budgetYearData.setDirectCosts(summaryData);
        
        summaryData = objFactory.createSummaryDataType();
        summaryData.setFederalSummary(periodBean.getTotalCosts());
        summaryData.setNonFederalSummary(periodBean.getCostSharingAmount());
        if (periodBean.getTotalCosts() != null) {
        	summaryData.setTotalFedNonFedSummary(periodBean.getTotalCosts().add(periodBean.getCostSharingAmount()));
        }
        else {
        	summaryData.setTotalFedNonFedSummary(periodBean.getCostSharingAmount());
        }           
        budgetYearData.setTotalCosts(summaryData);
        
        summaryData  = objFactory.createSummaryDataType();
        summaryData.setFederalSummary(periodBean.getTotalCompensation());
        summaryData.setNonFederalSummary(periodBean.getTotalCompensationCostSharing());
        if (periodBean.getTotalCompensation() != null) {
        	summaryData.setTotalFedNonFedSummary(periodBean.getTotalCompensation().add(periodBean.getTotalCompensationCostSharing()));
        }
        else {
        	summaryData.setTotalFedNonFedSummary(periodBean.getTotalCompensationCostSharing());
        }
        budgetYearData.setTotalCompensation(summaryData);

        budgetYearData.setOtherPersonnel(getOtherPersonnel(periodBean));
        budgetYearData.setKeyPersons(getKeyPersons(periodBean));
        budgetYearData.setOtherDirectCosts(getOtherDirectCosts(periodBean));
        budgetYearData.setTravel(getTravel(periodBean));
        if (periodBean.getEquipment() != null && periodBean.getEquipment().size()> 0 ){
        	budgetYearData.setEquipment(getEquipment(periodBean));
        }
        budgetYearData.setParticipantTraineeSupportCosts(getParticipant(periodBean));
        
 	   	// Total Direct Costs
        budgetYearData.setDirectCosts(getBudgetPeriodDirectCosts(periodBean)); 
        
    	// Indirect Costs
        BudgetYearDataType.IndirectCostsType indirectCostsType = getIndirectCosts(periodBean);
        if (!indirectCostsType.getIndirectCost().isEmpty()) {
        	budgetYearData.setIndirectCosts(indirectCostsType);
        }
        
    	// Total Direct and Indirect Costs
        budgetYearData.setTotalCosts(getBudgetPeriodDirectAndIndirectCosts(periodBean));
 	   
        // Fee
        budgetYearData.setFee(ZERO);
        
        // Total Costs and Fee
        budgetYearData.setTotalCostsFee(getBudgetPeriodTotalCostsFees(periodBean));
 	   
        return budgetYearData;
    }
    
    private SummaryDataType getBudgetPeriodDirectCosts(BudgetPeriodDataBean periodBean) throws JAXBException {
    	SummaryDataType summaryDataType = objFactory.createSummaryDataType();
    	summaryDataType.setFederalSummary(periodBean.getDirectCostsTotal());
    	summaryDataType.setNonFederalSummary(periodBean.getTotalDirectCostSharing());
    	summaryDataType.setTotalFedNonFedSummary(periodBean.getDirectCostsTotal().add(periodBean.getTotalDirectCostSharing()));
    	return summaryDataType;
    }
   
    private SummaryDataType getBudgetPeriodDirectAndIndirectCosts(BudgetPeriodDataBean periodBean) throws JAXBException {
    	SummaryDataType summaryDataType = objFactory.createSummaryDataType();
    	summaryDataType.setFederalSummary(periodBean.getTotalCosts());
    	summaryDataType.setNonFederalSummary(periodBean.getTotalDirectCostSharing().add(periodBean.getTotalIndirectCostSharing()));
    	summaryDataType.setTotalFedNonFedSummary(periodBean.getTotalCosts().add(periodBean.getTotalDirectCostSharing()).add(periodBean.getTotalIndirectCostSharing()));
    	return summaryDataType;
    }
    
    private SummaryDataType getBudgetPeriodTotalCostsFees(BudgetPeriodDataBean periodBean) throws JAXBException {
    	SummaryDataType summaryDataType  = objFactory.createSummaryDataType();
    	summaryDataType.setFederalSummary(periodBean.getTotalCosts().add(ZERO));
    	summaryDataType.setNonFederalSummary(periodBean.getTotalDirectCostSharing().add(periodBean.getTotalIndirectCostSharing()));
    	summaryDataType.setTotalFedNonFedSummary(periodBean.getTotalCosts().add(periodBean.getTotalDirectCostSharing()).add(periodBean.getTotalIndirectCostSharing()).add(ZERO));
    	return summaryDataType;
    }
    
    
    
    
    
    private Attachment getNarrative(int narrativeTypeCodeToAttach, BudgetPeriodDataBean periodBean, boolean periodLoop)
    		throws CoeusException, DBException{
    	return getNarrative (narrativeTypeCodeToAttach, periodBean, periodLoop, null);
    }

    private Attachment getNarrative(int narrativeTypeCodeToAttach, BudgetPeriodDataBean periodBean, boolean periodLoop, String titleCheckStr)
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
	    		if(titleCheckStr!=null && !titleCheckStr.equalsIgnoreCase(title)){
	    			continue;
	    		}
	            hmArg.put(ContentIdConstants.MODULE_NUMBER, Integer.toString(moduleNum)); 
	            hmArg.put(ContentIdConstants.DESCRIPTION,description);
	            if (periodLoop)  hmArg.put(ContentIdConstants.TITLE, title);
	            
	            attachment = getAttachment(hmArg);
	            if (attachment == null) {
	            //attachment does not already exist - we need to get it and add it
	            	String contentId = createContentId(hmArg);
	                 
	            	proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
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

    // Other Personnel
	private BudgetYearDataType.OtherPersonnelType getOtherPersonnel(BudgetPeriodDataBean periodBean) 
	     throws CoeusException, JAXBException, DBException{       
		
		OtherPersonnelBean otherPersonnelBean;
        String personnelType;
        OtherPersonnelDataType otherOtherPersonnelType;
	         
        BudgetYearDataType.OtherPersonnelType otherPersonnelType = objFactory.createBudgetYearDataTypeOtherPersonnelType();
	        
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
              otherPersonnelType.getOther().add(getOtherOtherPersonalType(otherPersonnelBean)); 
           }
        }
        
        return otherPersonnelType;
    }
	
	private OtherPersonnelDataType getOtherOtherPersonalType(OtherPersonnelBean otherPersonnelBean) throws JAXBException {
		OtherPersonnelDataType otherOtherPersonnelType = objFactory.createOtherPersonnelDataType();
		
		CompensationBean compensationBean = otherPersonnelBean.getCompensation();
        otherOtherPersonnelType.setNumberOfPersonnel(otherPersonnelBean.getNumberPersonnel());
        otherOtherPersonnelType.setProjectRole(otherPersonnelBean.getRole());

        otherOtherPersonnelType.setCalendarMonths(compensationBean.getCalendarMonths());
        otherOtherPersonnelType.setAcademicMonths(compensationBean.getAcademicMonths());
        otherOtherPersonnelType.setSummerMonths(compensationBean.getSummerMonths());
        otherOtherPersonnelType.setRequestedSalary(compensationBean.getRequestedSalary().add(compensationBean.getCostSharingAmount()));
        otherOtherPersonnelType.setFringeBenefits(compensationBean.getFringe().add(compensationBean.getFringeCostSharing()));
        
        TotalDataType totalDataType = objFactory.createTotalDataType();
        totalDataType.setFederal(compensationBean.getFundsRequested());
        totalDataType.setNonFederal(compensationBean.getNonFundsRequested());
        totalDataType.setTotalFedNonFed(compensationBean.getFundsRequested().add(compensationBean.getNonFundsRequested()));
        
        otherOtherPersonnelType.setOtherTotal(totalDataType);
        
		return otherOtherPersonnelType;
	}
	
	public SectBCompensationDataType getSectBComp(CompensationBean compensationBean) throws CoeusXMLException,CoeusException,DBException {
        SectBCompensationDataType sectBcomp = null;
        try{
           sectBcomp = objFactory.createSectBCompensationDataType();
           
           sectBcomp.setAcademicMonths(compensationBean.getAcademicMonths());
           sectBcomp.setCalendarMonths(compensationBean.getCalendarMonths());
           sectBcomp.setFringeBenefits(compensationBean.getFringe().add(compensationBean.getFringeCostSharing()));
           
           TotalDataType totalDataType  = objFactory.createTotalDataType();
           totalDataType.setFederal(compensationBean.getFundsRequested());
           totalDataType.setNonFederal(compensationBean.getNonFundsRequested());
           totalDataType.setTotalFedNonFed(compensationBean.getFundsRequested().add(compensationBean.getNonFundsRequested()));
           
           sectBcomp.setOtherTotal(totalDataType);
           sectBcomp.setRequestedSalary(compensationBean.getRequestedSalary().add(compensationBean.getCostSharingAmount()));
           sectBcomp.setSummerMonths(compensationBean.getSummerMonths());
           
         }catch (JAXBException jaxbEx){
            UtilFactory.log(jaxbEx.getMessage(),jaxbEx,"CompensationStream","getSectBComp()");
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
	    }

	    return indirectCostsType;
	}
	 
	 private BudgetYearDataType.KeyPersonsType getKeyPersons(BudgetPeriodDataBean periodBean)
	    throws JAXBException, CoeusException, DBException{
	    
	    BudgetYearDataType.KeyPersonsType keyPersons = objFactory.createBudgetYearDataTypeKeyPersonsType();
	    
	    SummaryDataType summaryData  = objFactory.createSummaryDataType();
	    summaryData.setFederalSummary(periodBean.getTotalFundsKeyPersons());
	    summaryData.setNonFederalSummary(periodBean.getTotalNonFundsKeyPersons());
	    if (periodBean.getTotalFundsKeyPersons() != null) {
	        summaryData.setTotalFedNonFedSummary(periodBean.getTotalFundsKeyPersons().add(periodBean.getTotalNonFundsKeyPersons()));
	    }
	    else {
	        summaryData.setTotalFedNonFedSummary(periodBean.getTotalNonFundsKeyPersons());
	    }
	    keyPersons.setTotalFundForKeyPersons(summaryData);
	    
	    // Get extra key persons (required when more than 8 key persons)
	    BigDecimal extraFunds = new BigDecimal("0");
	    BigDecimal extraNonFunds = new BigDecimal("0");
	    CoeusVector cvExtraPersons = periodBean.getExtraKeyPersons();
	    if (cvExtraPersons != null && cvExtraPersons.size() > 0 ) {
	        extraKeyPerObjFctory = new gov.grants.apply.coeus.extrakeyperson.ObjectFactory();
	        ExtraKeyPersonListType extraKeyPersonList = extraKeyPerObjFctory.createExtraKeyPersonList();
	        extraKeyPersonList.setProposalNumber(periodBean.getProposalNumber());
	        extraKeyPersonList.setBudgetPeriod(new BigInteger(Integer.toString(periodBean.getBudgetPeriod())));
	       
	        List extraPersonList = extraKeyPersonList.getKeyPersons();
	        String role;
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
                role = keyPersonBean.getRole();
	
                extraKeyPersonType.setProjectRole(role);
                compType.setAcademicMonths(keyPersonBean.getAcademicMonths());
                compType.setCalendarMonths(keyPersonBean.getCalendarMonths());
                compType.setSummerMonths(keyPersonBean.getSummerMonths());
	         
                compType.setBaseSalary(keyPersonBean.getBaseSalary());
                compType.setFringeBenefits(keyPersonBean.getFringe().add(keyPersonBean.getFringeCostSharing()));
                compType.setFundsRequested(keyPersonBean.getFundsRequested());
                compType.setRequestedSalary(keyPersonBean.getRequestedSalary().add(keyPersonBean.getCostSharingAmount()));
                compType.setNonFederal(keyPersonBean.getNonFundsRequested());
                if (keyPersonBean.getFundsRequested() != null) {
                	compType.setTotalFedNonFed(keyPersonBean.getFundsRequested().add(keyPersonBean.getNonFundsRequested()));
                }
                else {
                	compType.setTotalFedNonFed(keyPersonBean.getNonFundsRequested());
                }
	          
                extraKeyPersonType.setCompensation(compType);
                extraPersonList.add(extraKeyPersonType);
	        }   
	        summaryData  = objFactory.createSummaryDataType();
	        summaryData.setFederalSummary(extraFunds);
	        summaryData.setNonFederalSummary(extraNonFunds);
	        if (extraFunds != null) {
	            summaryData.setTotalFedNonFedSummary(extraFunds.add(extraNonFunds));
	        }
	        else {
	             summaryData.setTotalFedNonFedSummary(extraNonFunds);
	        }
	
	        keyPersons.setTotalFundForAttachedKeyPersons(summaryData);
	        
	        //marshall it to XML doc
	        CoeusXMLGenrator xmlGen = new CoeusXMLGenrator();
	        Document extraKeyPerDoc = xmlGen.marshelObject( extraKeyPersonList,"gov.grants.apply.coeus.extrakeyperson");
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
        String role;
        
        for (int i=0; i < cvKeyPersons.size();i++){
            KeyPersonBean keyPersonBean = (KeyPersonBean) cvKeyPersons.elementAt(i);
            KeyPersonDataType keyPersonDataType = objFactory.createKeyPersonDataType();
            role = keyPersonBean.getRole();

            keyPersonDataType.setProjectRole(role);
            keyPersonDataType.setName(getName(keyPersonBean));
            String baseSalary = "0.00";
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
            cvKeyPersonsData.add(keyPersonDataType);            
        }
        return cvKeyPersonsData;
        
    }
 
	 private HumanNameDataType getName(KeyPersonBean keyPersonBean)
			 throws CoeusException, JAXBException {
		 HumanNameDataType humanNameDataType = globallibraryObjFactory.createHumanNameDataType();
		 humanNameDataType.setFirstName(keyPersonBean.getFirstName());
		 humanNameDataType.setLastName(keyPersonBean.getLastName());
		 humanNameDataType.setMiddleName(keyPersonBean.getMiddleName());
        
		 return humanNameDataType;
	 }
  
    private KeyPersonCompensationDataType getCompensation(KeyPersonBean keyPersonBean)
    		throws CoeusException, JAXBException {
    	KeyPersonCompensationDataType keyPersonCompDataType = objFactory.createKeyPersonCompensationDataType();
    	keyPersonCompDataType.setAcademicMonths(keyPersonBean.getAcademicMonths());
    	keyPersonCompDataType.setBaseSalary(keyPersonBean.getBaseSalary());
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
 
    private BudgetYearDataType.OtherDirectCostsType getOtherDirectCosts(BudgetPeriodDataBean periodBean)
    		throws JAXBException, CoeusException{
    
    	BudgetYearDataType.OtherDirectCostsType otherDirectCostsType = objFactory.createBudgetYearDataTypeOtherDirectCostsType();
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
	        }
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
	        totalDataType.setFederal(periodBean.getPartTuition());
	        totalDataType.setNonFederal(periodBean.getPartTuitionCostSharing());
	        if (periodBean.getPartTuition() != null ){
	            totalDataType.setTotalFedNonFed(periodBean.getPartTuition().add(periodBean.getPartTuitionCostSharing()));
	        }else{
	            totalDataType.setTotalFedNonFed(periodBean.getPartTuitionCostSharing());
	        }
	        partType.setTuitionFeeHealthInsurance(totalDataType);
	        
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
	        partType.setParticipantTravel(totalDataType);
	        
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
	        totalDataType.setFederal(periodBean.getpartOtherCost());
	        totalDataType.setNonFederal(periodBean.getPartOtherCostSharing());
	        if (periodBean.getpartOtherCost() != null){
	            totalDataType.setTotalFedNonFed(periodBean.getpartOtherCost().add(periodBean.getPartOtherCostSharing()));
	        }else{
	            totalDataType.setTotalFedNonFed(periodBean.getPartOtherCostSharing());
	        }
	        partOther.setCost(totalDataType);
	        partOther.setDescription("Miscellaneous");
	        partType.setOther(partOther);
	        partType.setParticipantTraineeNumber(new BigInteger(Integer.toString(periodBean.getparticipantCount())));
	        
	        SummaryDataType summaryData  = objFactory.createSummaryDataType();
	        summaryData.setFederalSummary(periodBean.getpartOtherCost().add(
	                                            periodBean.getpartStipendCost().add(
	                                            periodBean.getpartTravelCost().add(
	                                            periodBean.getPartSubsistence().add(
	                                            periodBean.getPartTuition())))));
	         
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

	private String getOrgName() {
        return orgMaintFormBean.getOrganizationName();
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
}

