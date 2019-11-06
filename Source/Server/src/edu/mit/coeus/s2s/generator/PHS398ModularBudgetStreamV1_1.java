/*
 * @(#)PHS398ModularBudgetStreamV1_1.java June 6, 2006, 10:12 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.generator;

import edu.mit.coeus.s2s.generator.stream.*;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.bean.*;

import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;

import gov.grants.apply.forms.phs398_modularbudget_v1_1.*;
//import gov.grants.apply.system.globallibrary_v1.*;
import gov.grants.apply.system.globallibrary_v2.*;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.propdev.bean.web.GetNarrativeDocumentBean;
import edu.mit.coeus.s2s.bean.*;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.s2s.util.S2SHashValue;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import edu.mit.coeus.utils.DateUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.TimeZone;
import java.util.Vector;
import javax.xml.bind.JAXBException;


/**
 * @author  Eleanor Shavell
 * @Created on June 6, 2006, 10:12 AM
 * Class for generating the object stream for grants.gov PHS398_ModularBudgetV1_1. It uses jaxb classes
 * which have been created under gov.grants.apply package. 
 */

 public class PHS398ModularBudgetStreamV1_1 extends S2SBaseStream{ 
    private gov.grants.apply.forms.phs398_modularbudget_v1_1.ObjectFactory objFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;
//    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
    private gov.grants.apply.system.globallibrary_v2.ObjectFactory globalObjFactory;
    private CoeusXMLGenrator xmlGenerator;

    gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
  
    //txn bean
    private S2SPHS398ModBudTxnBean s2sPHS398ModBudTxnBean;
 
    private String propNumber;
    private HashMap hmInfo ;
    private UtilFactory utilFactory;
    private Calendar calendar;
   
    private int version;
    private BigDecimal cumulativeConsortiumFandA = new BigDecimal("0");
    private BigDecimal cumulativeDirectCostLessConsortiumFandA = new BigDecimal("0");
    private BigDecimal cumulativeTotalFundsRequestedDirectCosts = new BigDecimal("0");
    private BigDecimal cumulativeTotalFundsRequestedDirectIndirectCosts = new BigDecimal("0");
    private BigDecimal cumulativeTotalFundsRequestedIndirectCost = new BigDecimal("0");
 
 
   
    /** Creates a new instance of PHS398ModularBudgetStreamV1_1*/
    public PHS398ModularBudgetStreamV1_1(){
        objFactory = new gov.grants.apply.forms.phs398_modularbudget_v1_1.ObjectFactory();
//        globalObjFactory = new  gov.grants.apply.system.global_v1.ObjectFactory ();
        globalObjFactory = new gov.grants.apply.system.globallibrary_v2.ObjectFactory ();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
      
        xmlGenerator = new CoeusXMLGenrator();
     
        s2sPHS398ModBudTxnBean = new S2SPHS398ModBudTxnBean();
        hmInfo = new HashMap();
    
    } 
   
    private PHS398ModularBudgetType getPHS398ModBudget() throws CoeusXMLException,CoeusException,DBException{
        PHS398ModularBudgetType modBudgetType = null;
       
      
        try{
           
           modBudgetType = objFactory.createPHS398ModularBudget();
         
           hmInfo = getSimpleInfo();

 //          modBudgetType.setFormVersion  (hmInfo.get("FormVersion").toString());
           modBudgetType.setFormVersion("1.1");
           version = Integer.parseInt(hmInfo.get("version").toString());
           int numPeriods = Integer.parseInt(hmInfo.get("numPeriods").toString());
           
           if (numPeriods > 0){
             modBudgetType.setPeriods(getPeriods());
           }
           if (numPeriods > 1 ){
               modBudgetType.setPeriods2(getPeriod2());
           }
           if (numPeriods > 2 ){
              modBudgetType.setPeriods3(getPeriod3());
           }
           if (numPeriods > 3 ){
              modBudgetType.setPeriods4(getPeriod4());
           }
           if (numPeriods > 4 ){
              modBudgetType.setPeriods5(getPeriod5());
           }
           modBudgetType.setCummulativeBudgetInfo(getCumBudget());
           
        }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"PHS398ModularBudgetStreamV1_1","getPHS398ModBudget()");
            throw new CoeusXMLException(jaxbEx.getMessage());
     
        }
        
        return modBudgetType;
    }
   /*****************************************************
       getInfo
    *****************************************************/
     public HashMap getSimpleInfo() throws CoeusXMLException, CoeusException, DBException, JAXBException{
            
       HashMap hmInfo = new HashMap();
       hmInfo = s2sPHS398ModBudTxnBean.getModularBudgetInfo(propNumber);
       return hmInfo;
     }
     
     /*****************************
       getCumBudget
     *****************************/
  //  private PHS398ModularBudgetType.CummulativeBudgetInfoType getCumBudget ()
     private CumBudgetType getCumBudget ()
     throws CoeusXMLException,CoeusException,DBException,JAXBException{
  
      CumBudgetType cumType =
          objFactory.createCumBudgetType();
//    PHS398ModularBudgetType.CummulativeBudgetInfoType cumType =
//        objFactory.createPHS398ModularBudgetTypeCummulativeBudgetInfoType();
      CumBudgetType.EntirePeriodTotalCostType entireCostType =
              objFactory.createCumBudgetTypeEntirePeriodTotalCostType();
//    PHS398ModularBudgetType.CummulativeBudgetInfoType.EntirePeriodTotalCostType entireCostType =
//        objFactory.createPHS398ModularBudgetTypeCummulativeBudgetInfoTypeEntirePeriodTotalCostType();
         CumBudgetType.BudgetJustificationsType budgJustType =
                 objFactory.createCumBudgetTypeBudgetJustificationsType();
//      PHS398ModularBudgetType.CummulativeBudgetInfoType.BudgetJustificationsType budgJustType =
//        objFactory.createPHS398ModularBudgetTypeCummulativeBudgetInfoTypeBudgetJustificationsType();

     if (!cumulativeTotalFundsRequestedDirectIndirectCosts.toString().equals("0")){
        entireCostType.setCumulativeDirectCostLessConsortiumFandA(cumulativeDirectCostLessConsortiumFandA);
  
        entireCostType.setCumulativeTotalFundsRequestedDirectCosts(cumulativeTotalFundsRequestedDirectCosts );
   
        entireCostType.setCumulativeConsortiumFandA(cumulativeConsortiumFandA);
  
        entireCostType.setCumulativeTotalFundsRequestedDirectIndirectCosts(cumulativeTotalFundsRequestedDirectIndirectCosts);
   
         entireCostType.setCumulativeTotalFundsRequestedIndirectCost(cumulativeTotalFundsRequestedIndirectCost);
    
         cumType.setEntirePeriodTotalCost(entireCostType);
    
         budgJustType = getBudgetJustifications();
    
         if (budgJustType != null) {
                cumType.setBudgetJustifications(budgJustType);
          }
     }
    return cumType;
               
     }
     
    /**************************************
     * getBudgetJustifications
     **************************************/
//     private PHS398ModularBudgetType.CummulativeBudgetInfoType.BudgetJustificationsType  getBudgetJustifications ()
     private CumBudgetType.BudgetJustificationsType getBudgetJustifications ()
     throws CoeusXMLException,CoeusException,DBException,JAXBException{
           
      CumBudgetType.BudgetJustificationsType budgJustType =
              objFactory.createCumBudgetTypeBudgetJustificationsType();
//      PHS398ModularBudgetType.CummulativeBudgetInfoType.BudgetJustificationsType budgJustType =
//        objFactory.createPHS398ModularBudgetTypeCummulativeBudgetInfoTypeBudgetJustificationsType();
        CumBudgetType.BudgetJustificationsType.AdditionalNarrativeJustificationType narrjustType=
               objFactory.createCumBudgetTypeBudgetJustificationsTypeAdditionalNarrativeJustificationType();
//      PHS398ModularBudgetType.CummulativeBudgetInfoType.BudgetJustificationsType.AdditionalNarrativeJustificationType narrjustType =
//        objFactory.createPHS398ModularBudgetTypeCummulativeBudgetInfoTypeBudgetJustificationsTypeAdditionalNarrativeJustificationType();
        CumBudgetType.BudgetJustificationsType.ConsortiumJustificationType consortJustType =
                objFactory.createCumBudgetTypeBudgetJustificationsTypeConsortiumJustificationType();
//      PHS398ModularBudgetType.CummulativeBudgetInfoType.BudgetJustificationsType.ConsortiumJustificationType consortJustType =
//        objFactory.createPHS398ModularBudgetTypeCummulativeBudgetInfoTypeBudgetJustificationsTypeConsortiumJustificationType();
        CumBudgetType.BudgetJustificationsType.PersonnelJustificationType persJustType =
                objFactory.createCumBudgetTypeBudgetJustificationsTypePersonnelJustificationType();
//      PHS398ModularBudgetType.CummulativeBudgetInfoType.BudgetJustificationsType.PersonnelJustificationType persJustType=
//        objFactory.createPHS398ModularBudgetTypeCummulativeBudgetInfoTypeBudgetJustificationsTypePersonnelJustificationType();
   
      
    
        try{
          

          String description;
          int narrativeType;
          int moduleNum;
          ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
          ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean ;
                      
          Vector vctNarrative = proposalNarrativeTxnBean.getPropNarrativePDFForProposal(propNumber);
              
          S2STxnBean s2sTxnBean = new S2STxnBean();
          LinkedHashMap hmArg = new LinkedHashMap();
                     
          HashMap hmNarrative = new HashMap();
                  
           int size=vctNarrative==null?0:vctNarrative.size();
           for (int row=0; row < size;row++) {
               proposalNarrativePDFSourceBean = (ProposalNarrativePDFSourceBean) vctNarrative.elementAt(row);
                           
               moduleNum = proposalNarrativePDFSourceBean.getModuleNumber();   
  
               String fileNameForOtherType = proposalNarrativePDFSourceBean.getFileName();
               
               hmNarrative = new HashMap();
               hmNarrative = s2sTxnBean.getNarrativeInfo(propNumber,moduleNum);
               narrativeType = Integer.parseInt(hmNarrative.get("NARRATIVE_TYPE_CODE").toString());
               description = hmNarrative.get("DESCRIPTION").toString();
      
               hmArg.put(ContentIdConstants.MODULE_NUMBER, Integer.toString(moduleNum));            
               hmArg.put(ContentIdConstants.DESCRIPTION, description);
               
               attachment = getAttachment(hmArg);
             
              
               if ( narrativeType == 35 ) {
                   //personnel justification
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                    Attachment personnelJustAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                    if (personnelJustAttachment.getContent() != null){
                       
                        attachedFileType = getAttachedFileType(personnelJustAttachment);
                    
                     //   personnelJustAttachment.setFileName(AttachedFileDataTypeStream.addExtension(fileNameForOtherType));
                        persJustType.setAttFile(attachedFileType);
                        budgJustType.setPersonnelJustification(persJustType);
                     }
                  } 
               } else if (narrativeType == 36){
                   // consortium justification
                    if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                    Attachment consortJustAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                    if (consortJustAttachment.getContent() != null){
                       
                        attachedFileType = getAttachedFileType(consortJustAttachment);
                    
                 //       consortJustAttachment.setFileName(AttachedFileDataTypeStream.addExtension(fileNameForOtherType));
                        consortJustType.setAttFile(attachedFileType);
                        budgJustType.setConsortiumJustification(consortJustType);
                     }
                  } 
               } else if (narrativeType == 37) {
                   //narrative justification  
                    if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                    Attachment narrJustAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                    if (narrJustAttachment.getContent() != null){
                       
                        attachedFileType = getAttachedFileType(narrJustAttachment);
                    
              //          narrJustAttachment.setFileName(AttachedFileDataTypeStream.addExtension(fileNameForOtherType));
                        narrjustType.setAttFile(attachedFileType);
                        budgJustType.setAdditionalNarrativeJustification(narrjustType);
                     }
                  } 
               }
           }  //end for
             
            
            return budgJustType;
     
     
         }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"OtherAttachmentsStream","getOtherAttachments()");
            throw new CoeusXMLException(jaxbEx.getMessage());
         }
    }
    
    
    /**
     * getAndAddNarrative. get the narrative content from database and call base method to add attachment
    
    private  Attachment getAndAddNarrative (LinkedHashMap hmArg,ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean)
    throws CoeusException, DBException{
 
        String contentId = createContentId(hmArg);
        Attachment attachment = new Attachment();
        attachment.setContent( proposalNarrativePDFSourceBean.getFileBytes());
        String contentType = "application/octet-stream";
        attachment.setFileName(AttachedFileDataTypeStream.addExtension(contentId));
        attachment.setContentId(contentId);
        attachment.setContentType(contentType);
                   
        //no need for this to return an attachment,     
        addAttachment(hmArg, attachment);
                 
     return attachment;
    }
     */
    
      
private gov.grants.apply.system.attachments_v1.AttachedFileDataType getAttachedFileType(Attachment attachment) 
     throws JAXBException {
    
    gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
    gov.grants.apply.system.attachments_v1.AttachedFileDataType.FileLocationType fileLocation;
    gov.grants.apply.system.global_v1.HashValueType hashValueType;

    attachedFileType = attachmentsObjFactory.createAttachedFileDataType();
    fileLocation = attachmentsObjFactory.createAttachedFileDataTypeFileLocationType();
//    hashValueType = globalObjFactory.createHashValueType();
    
    fileLocation.setHref(attachment.getContentId());
    attachedFileType.setFileLocation(fileLocation);
    attachedFileType.setFileName(AttachedFileDataTypeStream.addExtension(attachment.getFileName()));
    attachedFileType.setMimeType("application/octet-stream");
    try{
        attachedFileType.setHashValue(S2SHashValue.getValue(attachment.getContent()));
    }catch(Exception ex){
        ex.printStackTrace();
        UtilFactory.log(ex.getMessage(),ex, "PHSModularBudgetStream", "getAttachedFile");
        throw new JAXBException(ex);
    }

    return attachedFileType;
           
}
   
  
    /**************************************
    Period 1
    ***************************************/
 //    private PHS398ModularBudgetType.PeriodsType getPeriods ()
     private PeriodType getPeriods ()
      throws CoeusXMLException,CoeusException,DBException,JAXBException{
      PeriodType periodsType =objFactory.createPeriodType();
//      PHS398ModularBudgetType.PeriodsType periodsType = 
//          objFactory.createPHS398ModularBudgetTypePeriodsType();
      PeriodType.DirectCostType directCostType=objFactory.createPeriodTypeDirectCostType();
//      PHS398ModularBudgetType.PeriodsType.DirectCostType directCostType =
//          objFactory.createPHS398ModularBudgetTypePeriodsTypeDirectCostType();
      PeriodType.IndirectCostType indirectCostType =objFactory.createPeriodTypeIndirectCostType();
//      PHS398ModularBudgetType.PeriodsType.IndirectCostType indirectCostType =
//          objFactory.createPHS398ModularBudgetTypePeriodsTypeIndirectCostType();
      PeriodType.IndirectCostType.IndirectCostItemsType indirectCostItemsType=
             objFactory.createPeriodTypeIndirectCostTypeIndirectCostItemsType();
//      PHS398ModularBudgetType.PeriodsType.IndirectCostType.IndirectCostItemsType indirectCostItemsType =
//          objFactory.createPHS398ModularBudgetTypePeriodsTypeIndirectCostTypeIndirectCostItemsType();
      
      BigDecimal consortiumFandA = null;
      BigDecimal directCostLessConsortiumFandA = null;
      BigDecimal totalDirectCosts = null;
       
      ////////////////
      //BudgetPeriod
      ////////////////
      periodsType.setBudgetPeriod(1);
   
      /////////////////
      //StartDate and End Date
      ////////////////
      HashMap hmDates = new HashMap();
      hmDates = s2sPHS398ModBudTxnBean.getDates(propNumber, version, 1);
      
      if (hmDates.get("START_DATE") != null)
        periodsType.setBudgetPeriodStartDate(getCal(new Date( ((Timestamp) hmDates.get("START_DATE")).getTime())));
      if (hmDates.get("END_DATE") != null)
        periodsType.setBudgetPeriodEndDate(getCal(new Date( ((Timestamp) hmDates.get("END_DATE")).getTime())));
      
      /////////////////////////
      //TotalDirectAndIndirect
      //////////////////////
      HashMap hmTotalCost = new HashMap();
      hmTotalCost = s2sPHS398ModBudTxnBean.getTotalCost(propNumber, 1,version);
      
      BigDecimal directIndirect = new BigDecimal(hmTotalCost.get("TOTAL_COST").toString());
      periodsType.setTotalFundsRequestedDirectIndirectCosts(directIndirect);
      cumulativeTotalFundsRequestedDirectIndirectCosts = cumulativeTotalFundsRequestedDirectIndirectCosts.add(directIndirect);
      
      //////////////
      // DirectCosts
      ///////////////
      HashMap hmDirectCosts = new HashMap();
      hmDirectCosts = s2sPHS398ModBudTxnBean.getDirectCost(propNumber, 1,version);
      if(hmDirectCosts != null) {
       if (hmDirectCosts.get("CONSORTIUM_FNA") != null) {
         consortiumFandA = new BigDecimal(hmDirectCosts.get("CONSORTIUM_FNA").toString());
         directCostType.setConsortiumFandA(consortiumFandA);
         cumulativeConsortiumFandA = cumulativeConsortiumFandA.add(consortiumFandA);
       }
      
       if (hmDirectCosts.get("DIRECT_COST_LESS_CONSOR_FNA") != null) {
          directCostLessConsortiumFandA = new BigDecimal
                          (hmDirectCosts.get("DIRECT_COST_LESS_CONSOR_FNA").toString());
          directCostType.setDirectCostLessConsortiumFandA(directCostLessConsortiumFandA);
          cumulativeDirectCostLessConsortiumFandA = cumulativeDirectCostLessConsortiumFandA.add(directCostLessConsortiumFandA);
       }
      
       if (hmDirectCosts.get("TOTAL_DIRECT_COST") != null) {
         totalDirectCosts = new BigDecimal(hmDirectCosts.get("TOTAL_DIRECT_COST").toString());
         directCostType.setTotalFundsRequestedDirectCosts(totalDirectCosts);
         cumulativeTotalFundsRequestedDirectCosts = cumulativeTotalFundsRequestedDirectCosts.add(totalDirectCosts);
       }
      }
      periodsType.setDirectCost(directCostType);
      
      
      ////////////////
      //IndirectCosts   
      ////////////////
      CoeusVector cvIndCosts = s2sPHS398ModBudTxnBean.getInDirectCosts(propNumber,1,version);
      //cvIndCosts is a vector of hashmaps. there is one hashmap per indirect cost type
      //hashmap contains RATE_NUMBER,DESCRIPTION,IDC_RATE,IDC_BASE,FUNDS_REQUESTED
      //all are optional
      
      BigDecimal bdTotalIndirectCost = new BigDecimal("0");
      BigDecimal bdCost = null;
      BigDecimal bdBaseCost = null;
      BigDecimal bdRate = null;
      String costType = null;
      
      int listSize = cvIndCosts.size();
      HashMap hmIndCosts = new HashMap();
        
      for (int i=0; i<listSize; i++){
          costType = null;
          indirectCostItemsType =
              objFactory.createPeriodTypeIndirectCostTypeIndirectCostItemsType();
       //       objFactory.createPHS398ModularBudgetTypePeriodsTypeIndirectCostTypeIndirectCostItemsType();
          hmIndCosts = (HashMap) cvIndCosts.get(i);
          bdCost = new BigDecimal(hmIndCosts.get("FUNDS_REQUESTED").toString());
          bdBaseCost = new BigDecimal(hmIndCosts.get("IDC_BASE").toString());
          if (hmIndCosts.get("IDC_RATE") != null)
            bdRate = new BigDecimal(hmIndCosts.get("IDC_RATE").toString());
          if (hmIndCosts.get("DESCRIPTION") != null)
            costType = hmIndCosts.get("DESCRIPTION").toString();
          
          if (bdCost != null){
               indirectCostItemsType.setIndirectCostFundsRequested(bdCost);
               bdTotalIndirectCost = bdTotalIndirectCost.add(bdCost);
          }
            
          if (bdBaseCost != null){
               indirectCostItemsType.setIndirectCostBase(bdBaseCost);
          }
    
          if (bdRate != null){
               indirectCostItemsType.setIndirectCostRate(bdRate);
          }
        
          if (costType != null){
               indirectCostItemsType.setIndirectCostTypeDescription(costType);
          }
         
          indirectCostType.getIndirectCostItems().add(indirectCostItemsType);
  
      }
      
      //////////////////
      // CognizantFederalAgency
      //////////////////
      HashMap hmAgency = new HashMap();
      hmAgency = s2sPHS398ModBudTxnBean.getCognizantAgency(propNumber);
      if (hmAgency != null){
        if (hmAgency.get("AGENCY") != null)
            indirectCostType.setCognizantFederalAgency(hmAgency.get("AGENCY").toString());
        if (hmAgency.get("INDIRECT_COST_RATE_AGREEMENT")  != null) {
              indirectCostType.setIndirectCostAgreementDate(convertDateStringToCalendar(
                 hmAgency.get("INDIRECT_COST_RATE_AGREEMENT").toString()));
        }
      }
            
      
      ///////////////////////////////
      //TotalFundsRequestedIndirectCost
      ///////////////////////////////
      indirectCostType.setTotalFundsRequestedIndirectCost(bdTotalIndirectCost);
      cumulativeTotalFundsRequestedIndirectCost = cumulativeTotalFundsRequestedIndirectCost.add(bdTotalIndirectCost);
      periodsType.setIndirectCost(indirectCostType);
    
      return periodsType;
     }
     
     
    
     
    /************************************************************
    Period 2
    **************************************************/
 //    private PHS398ModularBudgetType.Periods2Type getPeriod2 ()
     private Period2Type getPeriod2 ()
      throws CoeusXMLException,CoeusException,DBException,JAXBException{
           
//      PHS398ModularBudgetType.Periods2Type period2Type = objFactory.createPHS398ModularBudgetTypePeriods2Type();      
//      PHS398ModularBudgetType.Periods2Type.DirectCost2Type directCostType =
//          objFactory.createPHS398ModularBudgetTypePeriods2TypeDirectCost2Type();
//      PHS398ModularBudgetType.Periods2Type.IndirectCost2Type indirectCostType =
//          objFactory.createPHS398ModularBudgetTypePeriods2TypeIndirectCost2Type();
//      PHS398ModularBudgetType.Periods2Type.IndirectCost2Type.IndirectCostItems2Type indirectCostItemsType =
//          objFactory.createPHS398ModularBudgetTypePeriods2TypeIndirectCost2TypeIndirectCostItems2Type();
    
      Period2Type period2Type =objFactory.createPeriod2Type();
      Period2Type.DirectCost2Type directCostType=objFactory.createPeriod2TypeDirectCost2Type();
      Period2Type.IndirectCost2Type indirectCostType =objFactory.createPeriod2TypeIndirectCost2Type();
      Period2Type.IndirectCost2Type.IndirectCostItems2Type indirectCostItemsType=
             objFactory.createPeriod2TypeIndirectCost2TypeIndirectCostItems2Type();
    
      
      
      ////////////////
      //BudgetPeriod
      ////////////////
      period2Type.setBudgetPeriod2(2);
      
       /////////////////
      //StartDate and End Date
      ////////////////
      HashMap hmDates = new HashMap();
      hmDates = s2sPHS398ModBudTxnBean.getDates(propNumber, version, 2);
      
      if (hmDates.get("START_DATE") != null)
       period2Type.setBudgetPeriodStartDate2(getCal(new Date( ((Timestamp) hmDates.get("START_DATE")).getTime())));
      if (hmDates.get("END_DATE") != null)
       period2Type.setBudgetPeriodEndDate2(getCal(new Date( ((Timestamp) hmDates.get("END_DATE")).getTime())));
      
      /////////////////////////
      //TotalDirectAndIndirect
      //////////////////////
      HashMap hmTotalCost = new HashMap();
      hmTotalCost = s2sPHS398ModBudTxnBean.getTotalCost(propNumber,2,version);
      
      BigDecimal directIndirect = new BigDecimal(hmTotalCost.get("TOTAL_COST").toString());
      period2Type.setTotalFundsRequestedDirectIndirectCosts2(directIndirect);
      cumulativeTotalFundsRequestedDirectIndirectCosts = cumulativeTotalFundsRequestedDirectIndirectCosts.add(directIndirect);
      
      //////////////
      // DirectCosts
      ///////////////
      HashMap hmDirectCosts = new HashMap();
      hmDirectCosts = s2sPHS398ModBudTxnBean.getDirectCost(propNumber, 2,version);
      
      if (hmDirectCosts != null) {
        BigDecimal consortiumFandA = new BigDecimal(hmDirectCosts.get("CONSORTIUM_FNA").toString());
        directCostType.setConsortiumFandA2(consortiumFandA);
        cumulativeConsortiumFandA=cumulativeConsortiumFandA.add(consortiumFandA);
   
        BigDecimal directCostLessConsortiumFandA = new BigDecimal
                          (hmDirectCosts.get("DIRECT_COST_LESS_CONSOR_FNA").toString());                
        directCostType.setDirectCostLessConsortiumFandA2(directCostLessConsortiumFandA);
        cumulativeDirectCostLessConsortiumFandA = cumulativeDirectCostLessConsortiumFandA.add(directCostLessConsortiumFandA);

                    
        BigDecimal totalDirectCosts = new BigDecimal
                          (hmDirectCosts.get("TOTAL_DIRECT_COST").toString());
        directCostType.setTotalFundsRequestedDirectCosts2(totalDirectCosts);
        cumulativeTotalFundsRequestedDirectCosts = cumulativeTotalFundsRequestedDirectCosts.add(totalDirectCosts);
      }
      period2Type.setDirectCost2(directCostType);
         
        
      
      ////////////////
      //IndirectCosts   
      ////////////////
      CoeusVector cvIndCosts = s2sPHS398ModBudTxnBean.getInDirectCosts(propNumber,2,version);
      //cvIndCosts is a vector of hashmaps. there is one hashmap per indirect cost type
      BigDecimal bdTotalIndirectCost = new BigDecimal("0");
      BigDecimal bdCost = null;
      BigDecimal bdBaseCost = null;
      BigDecimal bdRate = null;
      String costType = null;
      
      int listSize = cvIndCosts.size();
      HashMap hmIndCosts = new HashMap();
        
      
      for (int i=0; i<listSize; i++){
          
          costType = null;
          indirectCostItemsType =
     //         objFactory.createPHS398ModularBudgetTypePeriods2TypeIndirectCost2TypeIndirectCostItems2Type();
              objFactory.createPeriod2TypeIndirectCost2TypeIndirectCostItems2Type();
          hmIndCosts = (HashMap) cvIndCosts.get(i);
          bdCost = new BigDecimal(hmIndCosts.get("FUNDS_REQUESTED").toString());
          bdBaseCost = new BigDecimal(hmIndCosts.get("IDC_BASE").toString());
          
          if(hmIndCosts.get("IDC_RATE") != null)
            bdRate = new BigDecimal(hmIndCosts.get("IDC_RATE").toString());
          if (hmIndCosts.get("DESCRIPTION") != null)
            costType = hmIndCosts.get("DESCRIPTION").toString();
          
          if (bdCost != null){
               indirectCostItemsType.setIndirectCostFundsRequested(bdCost);
               bdTotalIndirectCost = bdTotalIndirectCost.add(bdCost);
          }
            
          if (bdBaseCost != null){
               indirectCostItemsType.setIndirectCostBase(bdBaseCost);
          }
    
          if (bdRate != null){
               indirectCostItemsType.setIndirectCostRate(bdRate);
          }
        
          if (costType != null) {
               indirectCostItemsType.setIndirectCostTypeDescription(costType);
          }
         
     
          indirectCostType.getIndirectCostItems2().add(indirectCostItemsType);
  
      }
      
      //////////////////
      // CognizantFederalAgency
      //////////////////
      HashMap hmAgency = new HashMap();
      hmAgency = s2sPHS398ModBudTxnBean.getCognizantAgency(propNumber);
      if (hmAgency != null) {
        if (hmAgency.get("AGENCY") != null) 
            indirectCostType.setCognizantFederalAgency2(hmAgency.get("AGENCY").toString());
        if (hmAgency.get("INDIRECT_COST_RATE_AGREEMENT")  != null) {
            indirectCostType.setIndirectCostAgreementDate2(convertDateStringToCalendar(
                 hmAgency.get("INDIRECT_COST_RATE_AGREEMENT").toString()));
        }
      }
           
      
      ///////////////////////////////
      //TotalFundsRequestedIndirectCost
      ///////////////////////////////
      indirectCostType.setTotalFundsRequestedIndirectCost2(bdTotalIndirectCost);
      cumulativeTotalFundsRequestedIndirectCost = cumulativeTotalFundsRequestedIndirectCost.add(bdTotalIndirectCost);
   
      period2Type.setIndirectCost2(indirectCostType);
    
      
      return period2Type;
     }
     
    /************************************************************
    Period 3
    **************************************************/
     //private PHS398ModularBudgetType.Periods3Type getPeriod3 ()
     private Period3Type getPeriod3 ()
      throws CoeusXMLException,CoeusException,DBException,JAXBException{
           
//      PHS398ModularBudgetType.Periods3Type period3Type = objFactory.createPHS398ModularBudgetTypePeriods3Type();
//      PHS398ModularBudgetType.Periods3Type.DirectCost3Type directCostType =
//          objFactory.createPHS398ModularBudgetTypePeriods3TypeDirectCost3Type();
//      PHS398ModularBudgetType.Periods3Type.IndirectCost3Type indirectCostType =
//          objFactory.createPHS398ModularBudgetTypePeriods3TypeIndirectCost3Type();
//      PHS398ModularBudgetType.Periods3Type.IndirectCost3Type.IndirectCostItems3Type indirectCostItemsType =
//          objFactory.createPHS398ModularBudgetTypePeriods3TypeIndirectCost3TypeIndirectCostItems3Type();
       
      Period3Type period3Type =objFactory.createPeriod3Type();
      Period3Type.DirectCost3Type directCostType=objFactory.createPeriod3TypeDirectCost3Type();
      Period3Type.IndirectCost3Type indirectCostType =objFactory.createPeriod3TypeIndirectCost3Type();
      Period3Type.IndirectCost3Type.IndirectCostItems3Type indirectCostItemsType=
             objFactory.createPeriod3TypeIndirectCost3TypeIndirectCostItems3Type();
    

        
      ////////////////
      //BudgetPeriod
      ////////////////
      period3Type.setBudgetPeriod3(3);
      
       /////////////////
      //StartDate and End Date
      ////////////////
      HashMap hmDates = new HashMap();
      hmDates = s2sPHS398ModBudTxnBean.getDates(propNumber, version,3);
      
      if (hmDates.get("START_DATE") != null)
        period3Type.setBudgetPeriodStartDate3(getCal(new Date( ((Timestamp) hmDates.get("START_DATE")).getTime())));
      if (hmDates.get("END_DATE") != null)
        period3Type.setBudgetPeriodEndDate3(getCal(new Date( ((Timestamp) hmDates.get("END_DATE")).getTime())));
      
      /////////////////////////
      //TotalDirectAndIndirect
      //////////////////////
      HashMap hmTotalCost = new HashMap();
      hmTotalCost = s2sPHS398ModBudTxnBean.getTotalCost(propNumber,3,version);
      
     
      BigDecimal directIndirect = new BigDecimal(hmTotalCost.get("TOTAL_COST").toString());
      period3Type.setTotalFundsRequestedDirectIndirectCosts3(directIndirect);
      cumulativeTotalFundsRequestedDirectIndirectCosts = cumulativeTotalFundsRequestedDirectIndirectCosts.add(directIndirect);
   
      
      //////////////
      // DirectCosts
      ///////////////
      HashMap hmDirectCosts = new HashMap();
      hmDirectCosts = s2sPHS398ModBudTxnBean.getDirectCost(propNumber, 3,version);
      
      if (hmDirectCosts != null) {
       BigDecimal consortiumFandA = new BigDecimal(hmDirectCosts.get("CONSORTIUM_FNA").toString());
       directCostType.setConsortiumFandA3(consortiumFandA);
       cumulativeConsortiumFandA=cumulativeConsortiumFandA.add(consortiumFandA);
   
       BigDecimal directCostLessConsortiumFandA = new BigDecimal
                          (hmDirectCosts.get("DIRECT_COST_LESS_CONSOR_FNA").toString());
       directCostType.setDirectCostLessConsortiumFandA3(directCostLessConsortiumFandA);
       cumulativeDirectCostLessConsortiumFandA =  cumulativeDirectCostLessConsortiumFandA .add(directCostLessConsortiumFandA);
       
       BigDecimal totalDirectCosts = new BigDecimal
                          (hmDirectCosts.get("TOTAL_DIRECT_COST").toString());
       directCostType.setTotalFundsRequestedDirectCosts3(totalDirectCosts);
       cumulativeTotalFundsRequestedDirectCosts=cumulativeTotalFundsRequestedDirectCosts.add(totalDirectCosts);
      }
      period3Type.setDirectCost3(directCostType);
      
      
      ////////////////
      //IndirectCosts   
      ////////////////
      CoeusVector cvIndCosts = s2sPHS398ModBudTxnBean.getInDirectCosts(propNumber,3,version);
      //cvIndCosts is a vector of hashmaps. there is one hashmap per indirect cost type
      BigDecimal bdTotalIndirectCost = new BigDecimal("0");
      BigDecimal bdCost = null;
      BigDecimal bdBaseCost = null;
      BigDecimal bdRate = null;
      String costType = null;
      
      int listSize = cvIndCosts.size();
      HashMap hmIndCosts = new HashMap();
        
      
      for (int i=0; i<listSize; i++){
          costType = null;
          indirectCostItemsType =
     //         objFactory.createPHS398ModularBudgetTypePeriods3TypeIndirectCost3TypeIndirectCostItems3Type();
              objFactory.createPeriod3TypeIndirectCost3TypeIndirectCostItems3Type();
          hmIndCosts = (HashMap) cvIndCosts.get(i);
          bdCost = new BigDecimal(hmIndCosts.get("FUNDS_REQUESTED").toString());
          bdBaseCost = new BigDecimal(hmIndCosts.get("IDC_BASE").toString());
          if (hmIndCosts.get("IDC_RATE") != null)
            bdRate = new BigDecimal(hmIndCosts.get("IDC_RATE").toString());
          if ( hmIndCosts.get("DESCRIPTION") != null)
            costType = hmIndCosts.get("DESCRIPTION").toString();
          
          if (bdCost != null){
               indirectCostItemsType.setIndirectCostFundsRequested(bdCost);
               bdTotalIndirectCost = bdTotalIndirectCost.add(bdCost);
          }
            
          if (bdBaseCost != null){
               indirectCostItemsType.setIndirectCostBase(bdBaseCost);
          }
    
          if (bdRate != null){
               indirectCostItemsType.setIndirectCostRate(bdRate);
          }
        
          if (costType != null) {
               indirectCostItemsType.setIndirectCostTypeDescription(costType);
          }
         
     
          indirectCostType.getIndirectCostItems3().add(indirectCostItemsType);
  
      }
      
      //////////////////
      // CognizantFederalAgency
      //////////////////
      HashMap hmAgency = new HashMap();
      hmAgency = s2sPHS398ModBudTxnBean.getCognizantAgency(propNumber);
      if (hmAgency != null) {
      if (hmAgency.get("AGENCY") != null)
        indirectCostType.setCognizantFederalAgency3(hmAgency.get("AGENCY").toString());
      if (hmAgency.get("INDIRECT_COST_RATE_AGREEMENT")  != null) {
          indirectCostType.setIndirectCostAgreementDate3(convertDateStringToCalendar(
                 hmAgency.get("INDIRECT_COST_RATE_AGREEMENT").toString()));
      }
      }
           
      
      ///////////////////////////////
      //TotalFundsRequestedIndirectCost
      ///////////////////////////////
      indirectCostType.setTotalFundsRequestedIndirectCost3(bdTotalIndirectCost);
      cumulativeTotalFundsRequestedIndirectCost = cumulativeTotalFundsRequestedIndirectCost.add(bdTotalIndirectCost);
   
      period3Type.setIndirectCost3(indirectCostType);
    
      
      return period3Type;
     }
     
    
    /************************************************************
    Period 4
    **************************************************/
 //    private PHS398ModularBudgetType.Periods4Type getPeriod4 ()
     private Period4Type getPeriod4 ()
      throws CoeusXMLException,CoeusException,DBException,JAXBException{
           
//      PHS398ModularBudgetType.Periods4Type period4Type = objFactory.createPHS398ModularBudgetTypePeriods4Type();
//      PHS398ModularBudgetType.Periods4Type.DirectCost4Type directCostType =
//          objFactory.createPHS398ModularBudgetTypePeriods4TypeDirectCost4Type();
//      PHS398ModularBudgetType.Periods4Type.IndirectCost4Type indirectCostType =
//          objFactory.createPHS398ModularBudgetTypePeriods4TypeIndirectCost4Type();
//      PHS398ModularBudgetType.Periods4Type.IndirectCost4Type.IndirectCostItems4Type indirectCostItemsType =
//          objFactory.createPHS398ModularBudgetTypePeriods4TypeIndirectCost4TypeIndirectCostItems4Type();
// 
        
      Period4Type period4Type =objFactory.createPeriod4Type();
      Period4Type.DirectCost4Type directCostType=objFactory.createPeriod4TypeDirectCost4Type();
      Period4Type.IndirectCost4Type indirectCostType =objFactory.createPeriod4TypeIndirectCost4Type();
      Period4Type.IndirectCost4Type.IndirectCostItems4Type indirectCostItemsType=
             objFactory.createPeriod4TypeIndirectCost4TypeIndirectCostItems4Type();
    

      
      ////////////////
      //BudgetPeriod
      ////////////////
      period4Type.setBudgetPeriod4(4);
      
       /////////////////
      //StartDate and End Date
      ////////////////
      HashMap hmDates = new HashMap();
      hmDates = s2sPHS398ModBudTxnBean.getDates(propNumber, version,4);
      
      if (hmDates.get("START_DATE") != null)
        period4Type.setBudgetPeriodStartDate4(getCal(new Date( ((Timestamp) hmDates.get("START_DATE")).getTime())));
      if (hmDates.get("END_DATE") != null)
        period4Type.setBudgetPeriodEndDate4(getCal(new Date( ((Timestamp) hmDates.get("END_DATE")).getTime())));
      
      /////////////////////////
      //TotalDirectAndIndirect
      //////////////////////
      HashMap hmTotalCost = new HashMap();
      hmTotalCost = s2sPHS398ModBudTxnBean.getTotalCost(propNumber,4,version);
      
       BigDecimal directIndirect = new BigDecimal(hmTotalCost.get("TOTAL_COST").toString());
       period4Type.setTotalFundsRequestedDirectIndirectCosts4(new BigDecimal(hmTotalCost.get("TOTAL_COST").toString()));
       cumulativeTotalFundsRequestedDirectIndirectCosts = cumulativeTotalFundsRequestedDirectIndirectCosts.add(directIndirect);
   
      
      //////////////
      // DirectCosts
      ///////////////
      HashMap hmDirectCosts = new HashMap();
      hmDirectCosts = s2sPHS398ModBudTxnBean.getDirectCost(propNumber, 4,version);
      
       if (hmDirectCosts != null) {
        BigDecimal consortiumFandA = new BigDecimal(hmDirectCosts.get("CONSORTIUM_FNA").toString());
        directCostType.setConsortiumFandA4(consortiumFandA);
        cumulativeConsortiumFandA=cumulativeConsortiumFandA.add(consortiumFandA);
   
        BigDecimal directCostLessConsortiumFandA = new BigDecimal
                          (hmDirectCosts.get("DIRECT_COST_LESS_CONSOR_FNA").toString());
        directCostType.setDirectCostLessConsortiumFandA4(directCostLessConsortiumFandA);
        cumulativeDirectCostLessConsortiumFandA =  cumulativeDirectCostLessConsortiumFandA .add(directCostLessConsortiumFandA);

        BigDecimal totalDirectCosts = new BigDecimal (hmDirectCosts.get("TOTAL_DIRECT_COST").toString());
        directCostType.setTotalFundsRequestedDirectCosts4(totalDirectCosts);
        cumulativeTotalFundsRequestedDirectCosts=cumulativeTotalFundsRequestedDirectCosts.add(totalDirectCosts);
       }
      period4Type.setDirectCost4(directCostType);
      
      
      ////////////////
      //IndirectCosts   
      ////////////////
      CoeusVector cvIndCosts = s2sPHS398ModBudTxnBean.getInDirectCosts(propNumber,4,version);
      //cvIndCosts is a vector of hashmaps. there is one hashmap per indirect cost type
      BigDecimal bdTotalIndirectCost = new BigDecimal("0");
      BigDecimal bdCost = null;
      BigDecimal bdBaseCost = null;
      BigDecimal bdRate = null;
      String costType = null;
      
      int listSize = cvIndCosts.size();
      HashMap hmIndCosts = new HashMap();
        
      
      for (int i=0; i<listSize; i++){
          costType=null;
          indirectCostItemsType =
          //    objFactory.createPHS398ModularBudgetTypePeriods4TypeIndirectCost4TypeIndirectCostItems4Type();
                         objFactory.createPeriod4TypeIndirectCost4TypeIndirectCostItems4Type();
          hmIndCosts = (HashMap) cvIndCosts.get(i);
         bdCost = new BigDecimal(hmIndCosts.get("FUNDS_REQUESTED").toString());
          bdBaseCost = new BigDecimal(hmIndCosts.get("IDC_BASE").toString());
          if (hmIndCosts.get("IDC_RATE") != null)
            bdRate = new BigDecimal(hmIndCosts.get("IDC_RATE").toString());
          if (hmIndCosts.get("DESCRIPTION") !=null)
            costType = hmIndCosts.get("DESCRIPTION").toString();
          if (bdCost != null){
               indirectCostItemsType.setIndirectCostFundsRequested(bdCost);
               bdTotalIndirectCost = bdTotalIndirectCost.add(bdCost);
          }
            
          if (bdBaseCost != null){
               indirectCostItemsType.setIndirectCostBase(bdBaseCost);
          }
    
          if (bdRate != null){
               indirectCostItemsType.setIndirectCostRate(bdRate);
          }
        
          if (costType != null) {
               indirectCostItemsType.setIndirectCostTypeDescription(costType);
          }
         
     
          indirectCostType.getIndirectCostItems4().add(indirectCostItemsType);
  
      }
      
      //////////////////
      // CognizantFederalAgency
      //////////////////
      HashMap hmAgency = new HashMap();
      hmAgency = s2sPHS398ModBudTxnBean.getCognizantAgency(propNumber);
      if (hmAgency != null) {
      if (hmAgency.get("AGENCY") != null)
        indirectCostType.setCognizantFederalAgency4(hmAgency.get("AGENCY").toString());
      if (hmAgency.get("INDIRECT_COST_RATE_AGREEMENT")  != null) {
          indirectCostType.setIndirectCostAgreementDate4(convertDateStringToCalendar(
                 hmAgency.get("INDIRECT_COST_RATE_AGREEMENT").toString()));
      }
           
      }
      ///////////////////////////////
      //TotalFundsRequestedIndirectCost
      ///////////////////////////////
      indirectCostType.setTotalFundsRequestedIndirectCost4(bdTotalIndirectCost);
      cumulativeTotalFundsRequestedIndirectCost = cumulativeTotalFundsRequestedIndirectCost.add(bdTotalIndirectCost);
   
      period4Type.setIndirectCost4(indirectCostType);
    
      
      return period4Type;
     }
     
    /************************************************************
    Period 5
    **************************************************/
 //    private PHS398ModularBudgetType.Periods5Type getPeriod5 ()
     private Period5Type getPeriod5 ()
      throws CoeusXMLException,CoeusException,DBException,JAXBException{
           
//      PHS398ModularBudgetType.Periods5Type period5Type = objFactory.createPHS398ModularBudgetTypePeriods5Type();
//      PHS398ModularBudgetType.Periods5Type.DirectCost5Type directCostType =
//          objFactory.createPHS398ModularBudgetTypePeriods5TypeDirectCost5Type();
//      PHS398ModularBudgetType.Periods5Type.IndirectCost5Type indirectCostType =
//          objFactory.createPHS398ModularBudgetTypePeriods5TypeIndirectCost5Type();
//      PHS398ModularBudgetType.Periods5Type.IndirectCost5Type.IndirectCostItems5Type indirectCostItemsType =
//          objFactory.createPHS398ModularBudgetTypePeriods5TypeIndirectCost5TypeIndirectCostItems5Type();
 
      Period5Type period5Type =objFactory.createPeriod5Type();
      Period5Type.DirectCost5Type directCostType=objFactory.createPeriod5TypeDirectCost5Type();
      Period5Type.IndirectCost5Type indirectCostType =objFactory.createPeriod5TypeIndirectCost5Type();
      Period5Type.IndirectCost5Type.IndirectCostItems5Type indirectCostItemsType=
             objFactory.createPeriod5TypeIndirectCost5TypeIndirectCostItems5Type();
    

        
      ////////////////
      //BudgetPeriod
      ////////////////
      period5Type.setBudgetPeriod5(5);
      
       /////////////////
      //StartDate and End Date
      ////////////////
      HashMap hmDates = new HashMap();
      hmDates = s2sPHS398ModBudTxnBean.getDates(propNumber, version,5);
      
      if (hmDates.get("START_DATE") != null)
       period5Type.setBudgetPeriodStartDate5(getCal(new Date( ((Timestamp) hmDates.get("START_DATE")).getTime())));
      if (hmDates.get("END_DATE") != null)
       period5Type.setBudgetPeriodEndDate5(getCal(new Date( ((Timestamp) hmDates.get("END_DATE")).getTime())));
      
      /////////////////////////
      //TotalDirectAndIndirect
      //////////////////////
      HashMap hmTotalCost = new HashMap();
      hmTotalCost = s2sPHS398ModBudTxnBean.getTotalCost(propNumber,5,version);
      
      BigDecimal directIndirect = new BigDecimal(hmTotalCost.get("TOTAL_COST").toString());
      period5Type.setTotalFundsRequestedDirectIndirectCosts5(directIndirect);
      cumulativeTotalFundsRequestedDirectIndirectCosts = cumulativeTotalFundsRequestedDirectIndirectCosts.add(directIndirect);
  
      
      //////////////
      // DirectCosts
      ///////////////
      HashMap hmDirectCosts = new HashMap();
      hmDirectCosts = s2sPHS398ModBudTxnBean.getDirectCost(propNumber, 5,version);
      if (hmDirectCosts != null) { 
        BigDecimal consortiumFandA = new BigDecimal
                          (hmDirectCosts.get("CONSORTIUM_FNA").toString());
        directCostType.setConsortiumFandA5(consortiumFandA);
        cumulativeConsortiumFandA=cumulativeConsortiumFandA.add(consortiumFandA);
   
      
        BigDecimal directCostLessConsortiumFandA = new BigDecimal
                          (hmDirectCosts.get("DIRECT_COST_LESS_CONSOR_FNA").toString());
        directCostType.setDirectCostLessConsortiumFandA5(directCostLessConsortiumFandA);
        cumulativeDirectCostLessConsortiumFandA = cumulativeDirectCostLessConsortiumFandA.add(directCostLessConsortiumFandA);
      
        BigDecimal totalDirectCosts = new BigDecimal((hmDirectCosts.get("TOTAL_DIRECT_COST").toString()));
        directCostType.setTotalFundsRequestedDirectCosts5(totalDirectCosts);
        cumulativeTotalFundsRequestedDirectCosts=cumulativeTotalFundsRequestedDirectCosts.add(totalDirectCosts);
      }
      period5Type.setDirectCost5(directCostType);
      
      
      ////////////////
      //IndirectCosts   
      ////////////////
      CoeusVector cvIndCosts = s2sPHS398ModBudTxnBean.getInDirectCosts(propNumber,5,version);
      //cvIndCosts is a vector of hashmaps. there is one hashmap per indirect cost type
      BigDecimal bdTotalIndirectCost = new BigDecimal("0");
      BigDecimal bdCost = null;
      BigDecimal bdBaseCost = null;
      BigDecimal bdRate = null;
      String costType = null;
      
      int listSize = cvIndCosts.size();
      HashMap hmIndCosts = new HashMap();
        
      
      for (int i=0; i<listSize; i++){
          costType=null;
          indirectCostItemsType =
          //    objFactory.createPHS398ModularBudgetTypePeriods5TypeIndirectCost5TypeIndirectCostItems5Type();
                objFactory.createPeriod5TypeIndirectCost5TypeIndirectCostItems5Type();

          hmIndCosts = (HashMap) cvIndCosts.get(i);
          bdCost = new BigDecimal(hmIndCosts.get("FUNDS_REQUESTED").toString());
          bdBaseCost = new BigDecimal(hmIndCosts.get("IDC_BASE").toString());
          if (hmIndCosts.get("IDC_RATE") != null)
             bdRate = new BigDecimal(hmIndCosts.get("IDC_RATE").toString());
          if (hmIndCosts.get("DESCRIPTION") != null)
             costType = hmIndCosts.get("DESCRIPTION").toString();
          
          if (bdCost != null){
               indirectCostItemsType.setIndirectCostFundsRequested(bdCost);
               bdTotalIndirectCost = bdTotalIndirectCost.add(bdCost);
          }
            
          if (bdBaseCost != null){
               indirectCostItemsType.setIndirectCostBase(bdBaseCost);
          }
    
          if (bdRate != null){
               indirectCostItemsType.setIndirectCostRate(bdRate);
          }
        
          if (costType != null) {
               indirectCostItemsType.setIndirectCostTypeDescription(costType);
          }
         
     
          indirectCostType.getIndirectCostItems5().add(indirectCostItemsType);
  
      }
      
      //////////////////
      // CognizantFederalAgency
      //////////////////
      HashMap hmAgency = new HashMap();
      hmAgency = s2sPHS398ModBudTxnBean.getCognizantAgency(propNumber);
      if (hmAgency != null) {
      if (hmAgency.get("AGENCY") != null )
        indirectCostType.setCognizantFederalAgency5(hmAgency.get("AGENCY").toString());
      if (hmAgency.get("INDIRECT_COST_RATE_AGREEMENT")  != null) {
          indirectCostType.setIndirectCostAgreementDate5(convertDateStringToCalendar(
                 hmAgency.get("INDIRECT_COST_RATE_AGREEMENT").toString()));
      }
      }
      
      ///////////////////////////////
      //TotalFundsRequestedIndirectCost
      ///////////////////////////////
      indirectCostType.setTotalFundsRequestedIndirectCost5(bdTotalIndirectCost);
      cumulativeTotalFundsRequestedIndirectCost = cumulativeTotalFundsRequestedIndirectCost.add(bdTotalIndirectCost);
   
      period5Type.setIndirectCost5(indirectCostType);
    
      
      return period5Type;
     }
     
     
       //for attachments
        GetNarrativeDocumentBean narrativeDocBean;
        Attachment attachment = null;
        String contentId;
        String contentType;
     
        
        
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
    private Calendar getTodayDate() {
      Calendar cal = Calendar.getInstance(TimeZone.getDefault()); 
      java.util.Date today = cal.getTime();
      cal.setTime(today);
      return cal;
    }
    
 
    public Object getStream(HashMap ht) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getPHS398ModBudget();
    }    
     
 }
    

