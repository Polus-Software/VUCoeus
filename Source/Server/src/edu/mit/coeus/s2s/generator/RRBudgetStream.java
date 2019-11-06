/*
 * @(#)RRBudget.java October 19, 2004, 10:12 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.generator;

import edu.mit.coeus.s2s.generator.stream.*;

import edu.mit.coeus.organization.bean.OrganizationMaintenanceDataTxnBean;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceFormBean;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.s2s.bean.*;

import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;

import gov.grants.apply.forms.rr_budget_v1.*;
import gov.grants.apply.system.globallibrary_v1.*;
import java.math.BigDecimal;
import java.sql.Date;
//import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.TimeZone;
import java.util.Vector;
import javax.xml.bind.JAXBException;

/**
 * @author  Eleanor Shavell
 * @Created on November 8, 2004, 10:12 AM
 * Class for generating the object stream for grants.gov RR_Budget. It uses jaxb classes
 * which have been created under gov.grants.apply package. Fetch the data 
 * from database and attach with the jaxb beans which have been derived from 
 * RR_Budgetschema.
 */

 public class RRBudgetStream extends S2SBaseStream{ 
    private gov.grants.apply.forms.rr_budget_v1.ObjectFactory objFactory;
    private gov.grants.apply.system.globallibrary_v1.ObjectFactory globalObjFactory;
    private CoeusXMLGenrator xmlGenerator;

    //txn beans
    private S2STxnBean s2sTxnBean;
    private OrganizationMaintenanceDataTxnBean orgMaintDataTxnBean;
     private ProposalDevelopmentTxnBean propDevTxnBean;
    
    //data beans
    private BudgetSummaryDataBean budgetSummaryBean;
    private  CoeusVector cvPeriod;
    private ProposalDevelopmentFormBean propDevFormBean;
    private OrganizationMaintenanceFormBean orgMaintFormBean;
    
//    private ArrayList attachmentList;
    private HashMap attachmentMap;
    private Vector vecContentIds;
    private Hashtable propData;
    private String propNumber;
    private UtilFactory utilFactory;
    private Calendar calendar;
   
     //start case 2406
    private String organizationID;
    private String perfOrganizationID;
    //end case 2406
    
    private Vector extraAttachments;
   
    /** Creates a new instance of RR_BudgetStream */
    public RRBudgetStream(){
        objFactory = new gov.grants.apply.forms.rr_budget_v1.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.globallibrary_v1.ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();
     
        s2sTxnBean = new S2STxnBean();
        budgetSummaryBean = new BudgetSummaryDataBean();
        propDevTxnBean = new ProposalDevelopmentTxnBean();
        
        orgMaintDataTxnBean = new OrganizationMaintenanceDataTxnBean();
        extraAttachments = new Vector();
    
    } 
   
    private RRBudgetType getRRBudget() throws CoeusXMLException,CoeusException,DBException{
        RRBudgetType rrBudget = null;
       
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
           attachmentMap = getAttachmentMap();
           
           rrBudget = objFactory.createRRBudget();
         
           /**BudgetType
           *  values for budget type are Project, Subaward/Consortium
           */
           rrBudget.setBudgetType("Project");
           
           /**
            * OrganizationName
            */
           rrBudget.setOrganizationName(getOrgName());
           
           /**
            * BudgetSummary
            */
           rrBudget.setBudgetSummary(getBudgetSummary());
          
           //change for march 7
           int numPeriods = 0;
           if (cvPeriod != null){
              numPeriods = cvPeriod.size();
          
           /*
            *Added by Geo on 11/18/2005
            *Delete all extra narratives which created automatically before starting the streaming
            */
//           int[] narrTypes = {11,12};
//           s2sTxnBean.deleteAutoGenNarrativeType(propDevFormBean.getProposalNumber(), narrTypes);
           
           /**
            *BudgetYear1 - 5
            */
           
           rrBudget.setBudgetYear1(getBudgetYear1(cvPeriod));
           
           if (numPeriods >= 2) {
               rrBudget.setBudgetYear2(getBudgetYear(2,cvPeriod));
           } 
           if (numPeriods >= 3) {
               rrBudget.setBudgetYear3(getBudgetYear(3,cvPeriod));
           } 
           if (numPeriods >= 4) {
                rrBudget.setBudgetYear4(getBudgetYear(4,cvPeriod));
           }
           if (numPeriods >=5) {
                rrBudget.setBudgetYear5(getBudgetYear(5,cvPeriod));
           }
           
          } // end if null
          
           /*
            *Update all the extra attachments to the narrative table
            */
//           s2sTxnBean.insertAutoGenNarrativeDetails(extraAttachments);
            /**
            * DUNS
            */
           rrBudget.setDUNSID(orgMaintFormBean.getDunsNumber());
           /**
            * FormVersion
            */
           rrBudget.setFormVersion("1.0");  // ????
          
          
       
           
         }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"RRBudgetStream","getRRBudget()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }
        return rrBudget;
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
       
   private  RRBudgetType.BudgetSummaryType getBudgetSummary()
        throws JAXBException, CoeusException {
        BudgetSummaryStream budgetSummaryStream = new BudgetSummaryStream();
        RRBudgetType.BudgetSummaryType budgetSummaryType = 
                  budgetSummaryStream.getBudgetSummary(budgetSummaryBean);   
        return budgetSummaryType;
   }
   
   private BudgetYear1DataType getBudgetYear1( CoeusVector cvPeriod)
        throws JAXBException, CoeusException, DBException {

        //get period bean for first period   
        BudgetPeriodDataBean periodBean = (BudgetPeriodDataBean) cvPeriod.elementAt(0);
        
        BudgetYearDataTypeStream budgetYearDataStream = new BudgetYearDataTypeStream();
        attachmentMap = getAttachmentMap();
        budgetYearDataStream.setAttachmentMap(attachmentMap);
        
        BudgetYear1DataType period1DataType = objFactory.createBudgetYear1DataType();
        //call budgetYearStream to get the period info and set into the periodDataType
       //budgetYearDataStream will return a vector containing a period1DataType
  
        period1DataType = budgetYearDataStream.getBudgetYear(periodBean, attachmentMap);
   
        extraAttachments.addAll(budgetYearDataStream.getExtraAttachments());
       return period1DataType;
        
   }
      
     private BudgetYearDataType getBudgetYear(int period, CoeusVector cvPeriod)
        throws JAXBException, CoeusException, DBException {
        
        //get period bean for this period   
        BudgetPeriodDataBean periodBean = (BudgetPeriodDataBean) cvPeriod.elementAt(period-1);
        
        BudgetYearDataTypeStream budgetYearDataStream = new BudgetYearDataTypeStream();
        
        budgetYearDataStream.setAttachmentMap(attachmentMap);
         
        BudgetYearDataType periodDataType = objFactory.createBudgetYearDataType();
      
        //call budgetYearStream to get the period info and set into the periodDataType

        periodDataType = budgetYearDataStream.getBudgetYear(periodBean,attachmentMap);
      
        extraAttachments.addAll(budgetYearDataStream.getExtraAttachments());
       return periodDataType;
        
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
        return getRRBudget();
    }    
     
  
    
}
