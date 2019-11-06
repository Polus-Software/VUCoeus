/*
 * @(#)BudgetYearDataTypeStream.java November 9, 2004
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.generator.stream;

import edu.mit.coeus.s2s.generator.AttachmentValidator;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalNarrativeFormBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.s2s.Attachment;

import edu.mit.coeus.s2s.bean.*;
import edu.mit.coeus.s2s.util.S2SHashValue;
import edu.mit.coeus.s2s.generator.ContentIdConstants;
import edu.mit.coeus.s2s.generator.stream.bean.ExAttQueryParams;
import gov.grants.apply.forms.rr_budget_v1.*;
 
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
import gov.grants.apply.forms.rr_otherprojectinfo_v1.RROtherProjectInfoType;
import gov.grants.apply.system.attachments_v1.AttachedFileDataType;
import java.io.File;
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

/**
 * @author  Eleanor Shavell
 * @Created on November 8, 2004, 10:12 AM
 */

 public class BudgetYearDataTypeStream  extends AttachmentValidator{ 
    private gov.grants.apply.forms.rr_budget_v1.ObjectFactory objFactory;
    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory; 
    private gov.grants.apply.coeus.additionalequipment.ObjectFactory equipObjFactory;
    private Calendar calendar;
    
    private BudgetYear1DataType budgetYearData = null;
    private CoeusVector cvBudgetYear ;
    private S2STxnBean s2sTxnBean;
    private Vector extraAttachments;
  /** Creates a new instance of BudgetYearDataTypeStream */
  public BudgetYearDataTypeStream(){
        objFactory = new gov.grants.apply.forms.rr_budget_v1.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        equipObjFactory = new gov.grants.apply.coeus.additionalequipment.ObjectFactory();
        s2sTxnBean = new S2STxnBean();
        extraAttachments = new Vector();
    } 
   

  public BudgetYear1DataType getBudgetYear( BudgetPeriodDataBean periodBean, HashMap attachmentMap) 
 
             throws CoeusXMLException,CoeusException,DBException,JAXBException{
        
           budgetYearData = objFactory.createBudgetYear1DataType();
        
           budgetYearData.setBudgetPeriod(Integer.toString(periodBean.getBudgetPeriod()));
           budgetYearData.setBudgetPeriodEndDate(getCal(periodBean.getEndDate()));
           budgetYearData.setBudgetPeriodStartDate(getCal(periodBean.getStartDate()));
           budgetYearData.setCognizantFederalAgency(periodBean.getCognizantFedAgency());
           budgetYearData.setDirectCosts(periodBean.getDirectCostsTotal());
           budgetYearData.setTotalCosts(periodBean.getTotalCosts());
           budgetYearData.setTotalCompensation(periodBean.getTotalCompensation());
           
           if (periodBean.getBudgetPeriod() == 1) {
               
               AttachedFileDataType budgJustAtt = getBudgetJustification(periodBean);
               if (budgJustAtt.getFileName() != null) budgetYearData.setBudgetJustificationAttachment(budgJustAtt);
                
           }
           setOtherPersonnel(periodBean);
           setIndirectCosts(periodBean);
           setKeyPersons(periodBean);
           setOtherDirectCosts(periodBean);
           setTravel(periodBean);
           setEquipment(periodBean);
           setParticipant(periodBean);
          
        return budgetYearData;
    }
   
  private void setOtherPersonnel(BudgetPeriodDataBean periodBean) 
     throws CoeusException , JAXBException, DBException{ 
      
      BudgetYearDataType.OtherPersonnelType otherPersonnelType;
      OtherPersonnelStream otherPersonnelStream = new OtherPersonnelStream();
      otherPersonnelType = otherPersonnelStream.getOtherPersonnel(periodBean);
      budgetYearData.setOtherPersonnel(otherPersonnelType);
     
  }

  private void setOtherDirectCosts(BudgetPeriodDataBean periodBean)
    throws JAXBException, CoeusException{
    
    BudgetYearDataType.OtherDirectCostsType otherDirectCostsType =
            objFactory.createBudgetYearDataTypeOtherDirectCostsType();
    CoeusVector cvOtherDirectCosts = periodBean.getOtherDirectCosts();
     
    //there is only one element in the vector, which is an OtherDirectCostBean
    OtherDirectCostBean otherDirectCostBean = (OtherDirectCostBean) cvOtherDirectCosts.elementAt(0);
  
    otherDirectCostsType.setADPComputerServices(otherDirectCostBean.getcomputer());
    otherDirectCostsType.setAlterationsRenovations(otherDirectCostBean.getAlterations());
    otherDirectCostsType.setConsultantServices(otherDirectCostBean.getconsultants());
    otherDirectCostsType.setEquipmentRentalFee(otherDirectCostBean.getEquipRental());
    otherDirectCostsType.setMaterialsSupplies(otherDirectCostBean.getmaterials());
    otherDirectCostsType.setPublicationCosts(otherDirectCostBean.getpublications());
    otherDirectCostsType.setSubawardConsortiumContractualCosts(otherDirectCostBean.getsubAwards());
    otherDirectCostsType.setTotalOtherDirectCost(otherDirectCostBean.gettotalOtherDirect());
    
   
    CoeusVector cvOthers = otherDirectCostBean.getOtherCosts();
    //cvOthers vector of hashmaps with cost and description
    
    BudgetYearDataType.OtherDirectCostsType.OthersType othersType =
       objFactory.createBudgetYearDataTypeOtherDirectCostsTypeOthersType();
    
    BudgetYearDataType.OtherDirectCostsType.OthersType.OtherType othersDetailsType =
        objFactory.createBudgetYearDataTypeOtherDirectCostsTypeOthersTypeOtherType();
    
    for (int i = 0;i < cvOthers.size();i++){
         HashMap hmCosts = new HashMap();
         
         hmCosts = (HashMap) cvOthers.elementAt(i);
         othersDetailsType = objFactory.createBudgetYearDataTypeOtherDirectCostsTypeOthersTypeOtherType();
         othersDetailsType.setCost(new BigDecimal(hmCosts.get("Cost").toString()));
         othersDetailsType.setDescription(hmCosts.get("Description").toString());
         othersType.getOther().add(othersDetailsType);
     }
   
     otherDirectCostsType.setOthers(othersType);
     
     budgetYearData.setOtherDirectCosts(otherDirectCostsType);
    
}
  
   private void setTravel(BudgetPeriodDataBean periodBean)
    throws JAXBException, CoeusException{
    
    BudgetYearDataType.TravelType travelType = objFactory.createBudgetYearDataTypeTravelType();
    
    travelType.setDomesticTravelCost(periodBean.getDomesticTravelCost());
    travelType.setForeignTravelCost(periodBean.getForeignTravelCost());
    travelType.setTotalTravelCost(periodBean.getTotalTravelCost());
   
    budgetYearData.setTravel(travelType);
}
   
   private void setParticipant(BudgetPeriodDataBean periodBean)
    throws JAXBException, CoeusException{
        BudgetYearDataType.ParticipantTraineeSupportCostsType partType = objFactory.createBudgetYearDataTypeParticipantTraineeSupportCostsType();
        BudgetYearDataType.ParticipantTraineeSupportCostsType.OtherType partOther =
            objFactory.createBudgetYearDataTypeParticipantTraineeSupportCostsTypeOtherType();
        partType.setStipends(periodBean.getpartStipendCost());
        partType.setTravel(periodBean.getpartTravelCost());
        //addition for participant costs
        partType.setSubsistence(periodBean.getPartSubsistence());
        partType.setTuitionFeeHealthInsurance(periodBean.getPartTuition());
        partOther.setCost(periodBean.getpartOtherCost());
        partOther.setDescription("Other");
        partType.setOther(partOther);
        partType.setParticipantTraineeNumber(new BigInteger(Integer.toString(periodBean.getparticipantCount())));
        partType.setTotalCost(periodBean.getpartOtherCost().add(periodBean.getpartStipendCost().add(
                                            periodBean.getpartTravelCost().add(
                                            periodBean.getPartSubsistence().add(
                                            periodBean.getPartTuition())))));
        budgetYearData.setParticipantTraineeSupportCosts(partType);
   }
   
   private AttachedFileDataType getBudgetJustification(BudgetPeriodDataBean periodBean)
        throws JAXBException, CoeusException, DBException{
         
    AttachedFileDataType budgetJustAttachmentType;
    AttachedFileDataType.FileLocationType fileLocation;
    gov.grants.apply.system.global_v1.HashValueType hashValueType;

    budgetJustAttachmentType = attachmentsObjFactory.createAttachedFileDataType();
    fileLocation = attachmentsObjFactory.createAttachedFileDataTypeFileLocationType();
    hashValueType = globalObjFactory.createHashValueType();
    
 // Attachment budgJustAttachment = getNarrative("BUDGET_JUSTIFICATION",periodBean,false);
    Attachment budgJustAttachment = getNarrative(7,periodBean,false);
 
    
    if (budgJustAttachment.getContent() != null){
        //populate attachment info in schema element
    
        budgetJustAttachmentType.setFileName(AttachedFileDataTypeStream.addExtension(budgJustAttachment.getFileName()));
        try{
         budgetJustAttachmentType.setHashValue(S2SHashValue.getValue(budgJustAttachment.getContent())); 
         } catch(Exception ex){
            ex.printStackTrace();
            UtilFactory.log(ex.getMessage(),ex, "BudgetYearDataStream", "get BudgetJustification");
            throw new JAXBException(ex);
        }
        budgetJustAttachmentType.setMimeType("application/octet-stream");
        fileLocation = attachmentsObjFactory.createAttachedFileDataTypeFileLocationType();
        fileLocation.setHref(budgJustAttachment.getContentId());
        budgetJustAttachmentType.setFileLocation(fileLocation);
    }
    
       return budgetJustAttachmentType;
   }
   private void setEquipment(BudgetPeriodDataBean periodBean)
    throws JAXBException, CoeusException, DBException{
        
    BudgetYearDataType.EquipmentType equipmentType = objFactory.createBudgetYearDataTypeEquipmentType();
    BudgetYearDataType.EquipmentType.EquipmentListType equipmentListType ;
               
    CoeusVector cvEquipment = periodBean.getEquipment();
    CoeusVector cvEquipmentItems = new CoeusVector();
    EquipmentBean equipBean = new EquipmentBean();
    CostBean equipItemBean = new CostBean();
    
    if (cvEquipment.size() > 0){
      
        //cvEquipment is vector of equipmentBeans for this budget period - which actually
        // should just be one bean since costBeans are grouped by category
        equipBean = (EquipmentBean) cvEquipment.get(0);
        equipmentType.setTotalFund(equipBean.getTotalFund()); 
        if (equipBean.getExtraEquipmentList() != null) {
            BudgetYearDataType.EquipmentType.TotalFundForAttachedEquipmentType additionalEquipFundType =
             objFactory.createBudgetYearDataTypeEquipmentTypeTotalFundForAttachedEquipmentType();

            additionalEquipFundType.setTotalFundForAttachedEquipmentExist("Yes");
            additionalEquipFundType.setValue(equipBean.getTotalExtraFund());
            equipmentType.setTotalFundForAttachedEquipment(additionalEquipFundType);
        }
       
        cvEquipmentItems = equipBean.getEquipmentList();
                
        for (int i = 0; i< cvEquipmentItems.size(); i++){
          
            equipItemBean = new CostBean();
            equipItemBean =(CostBean) cvEquipmentItems.get(i);
              
            equipmentListType = objFactory.createBudgetYearDataTypeEquipmentTypeEquipmentListType();
            equipmentListType.setFundsRequested(equipItemBean.getCost());
            equipmentListType.setEquipmentItem(equipItemBean.getDescription()== null? 
                                             equipItemBean.getCategory() :  
                                             equipItemBean.getDescription());
                 
            equipmentType.getEquipmentList().add(equipmentListType);
          }
        
         //handle extra equipment. extra equipment needs to be in an attachment. 
         //create an equipment attachment and insert into narrative table
        CoeusVector additonalEquipList = equipBean.getExtraEquipmentList();
        if (additonalEquipList != null) {
            //Modifying to attach the autogenerated equipment attachment list
            //  equipmentType = getEquipmentAttachment(equipmentType,periodBean);
            AdditionalEquipmentListType extraEquipList = equipObjFactory.createAdditionalEquipmentList();
            List equipList = extraEquipList.getEquipmentList();
            int size = additonalEquipList.size();
            for(int i=0;i<size;i++){
                CostBean addEquipBean = (CostBean)additonalEquipList.get(i);
                AdditionalEquipmentListType.EquipmentListType equipListType = equipObjFactory.createAdditionalEquipmentListTypeEquipmentListType();
                equipListType.setFundsRequested(addEquipBean.getCost());
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
                InputStream templateIS = getClass().getResourceAsStream("/edu/mit/coeus/s2s/template/AdditionalEquipmentAttachment.xsl");
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
                UtilFactory.log(ex.getMessage(),ex, "KeyPersonStream", "getKeyPersons");
                throw new CoeusException(ex.getMessage());
            }catch(FOPException ex){
                UtilFactory.log(ex.getMessage(),ex, "KeyPersonStream", "getKeyPersons");
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
//            extraAttachments.add(new ExAttQueryParams(
//                                        propNarrBean, propNarrPDFBean,
//                                        periodBean.getBudgetPeriod()==1));
            s2sTxnBean.insertAutoGenNarrativeDetails(propNarrBean, propNarrPDFBean,
                        periodBean.getBudgetPeriod()==1);
            equipmentType = getEquipmentAttachment(equipmentType,periodBean);
            //end updation
        }//end if

         budgetYearData.setEquipment(equipmentType);
        
     }

   }
 
         

  private void setIndirectCosts(BudgetPeriodDataBean periodBean)
    throws JAXBException, CoeusException{
    
    BudgetYearDataType.IndirectCostsType indirectCostsType =
            objFactory.createBudgetYearDataTypeIndirectCostsType();
 
    IndirectCostBean indirectCostBean = periodBean.getIndirectCosts();
  
    indirectCostsType.setTotalIndirectCosts(indirectCostBean.getTotalIndirectCosts());
    
  BudgetYearDataType.IndirectCostsType.IndirectCostType indirectCostListType ;
    
    CoeusVector cvIndCostList = indirectCostBean.getIndirectCostDetails();
    
    //cvIndCostList - is a vector of IndirectCostDetailBeans
    if(cvIndCostList!=null && !cvIndCostList.isEmpty()){
        indirectCostsType.setTotalIndirectCosts(indirectCostBean.getTotalIndirectCosts());
        for (int i = 0;i < cvIndCostList.size();i++){
            indirectCostListType =
            objFactory.createBudgetYearDataTypeIndirectCostsTypeIndirectCostType();
            IndirectCostDetailBean indCostDetBean = new IndirectCostDetailBean();
            indCostDetBean = (IndirectCostDetailBean) cvIndCostList.elementAt(i);
            indirectCostListType.setBase(indCostDetBean.getBase());
            
            indirectCostListType.setCostType(indCostDetBean.getCostType());
            indirectCostListType.setFundRequested(indCostDetBean.getFunds());
            indirectCostListType.setRate(indCostDetBean.getRate());
            
            indirectCostsType.getIndirectCost().add(indirectCostListType);
            
        }
        budgetYearData.setIndirectCosts(indirectCostsType);
    }
}

  
private void setKeyPersons(BudgetPeriodDataBean periodBean)
    throws JAXBException, CoeusException, DBException{
    
    BudgetYearDataType.KeyPersonsType keyPersons;
    KeyPersonStream keyPersonStream = new KeyPersonStream();
    keyPersonStream.setAttachmentMap(getAttachmentMap());
    keyPersons = keyPersonStream.getKeyPersons(periodBean);
    
    //need to get key person attachment if there are more than 8 key persons
    if (keyPersons.getTotalFundForAttachedKeyPersons() != null){
       keyPersons = getKeyPersonAttachment(keyPersons,periodBean);
    }
    extraAttachments.addAll(keyPersonStream.getExtraAttachments());
    budgetYearData.setKeyPersons( keyPersons);
    
}
   
/**************************************************************
 * getKeyPersonAttachment will handle narratives of type additional Key Persons
 *
 **************************************************************/
private BudgetYearDataType.KeyPersonsType getKeyPersonAttachment 
                                                (BudgetYearDataType.KeyPersonsType keyPersonType,
                                                 BudgetPeriodDataBean periodBean)
    throws JAXBException, CoeusException , DBException{
                                
    BudgetYearDataType.KeyPersonsType.AttachedKeyPersonsType keyPersonAttachmentType =
                      objFactory.createBudgetYearDataTypeKeyPersonsTypeAttachedKeyPersonsType();
    gov.grants.apply.system.attachments_v1.AttachedFileDataType.FileLocationType fileLocation;
    
    Attachment keyPersAttachment = getNarrative(11,periodBean,true,"BudgetPeriod_"+periodBean.getBudgetPeriod());
    if (keyPersAttachment.getContent() != null){
     
        //populate attachment info in schema element     
        keyPersonAttachmentType.setFileName(AttachedFileDataTypeStream.addExtension(keyPersAttachment.getFileName()));
        try{
           keyPersonAttachmentType.setHashValue(S2SHashValue.getValue(keyPersAttachment.getContent())); 
        } catch(Exception ex){
            ex.printStackTrace();
            UtilFactory.log(ex.getMessage(),ex, "BudgetYearDataStream", "getKeyPersonAttachment");
            throw new JAXBException(ex);
        }
        keyPersonAttachmentType.setMimeType("application/octet-stream");
        fileLocation = attachmentsObjFactory.createAttachedFileDataTypeFileLocationType();
        fileLocation.setHref(keyPersAttachment.getContentId());
        keyPersonAttachmentType.setFileLocation(fileLocation);
    
        keyPersonAttachmentType.setTotalFundForAttachedKeyPersonsExist("Yes");
            
        keyPersonType.setAttachedKeyPersons(keyPersonAttachmentType);
 
 
    }
    return keyPersonType;

 }

/**************************************************************
 * getEquipmentAttachment will handle  narratives of type Additional_Equipment
 *
 * gets the equipment items over 10 and generates
 * on the fly a pdf to be stored in the narrative 
 **************************************************************/
private BudgetYearDataType.EquipmentType  getEquipmentAttachment 
                                                (BudgetYearDataType.EquipmentType  equipType,
                                                 BudgetPeriodDataBean periodBean)
    throws JAXBException, CoeusException , DBException{
                                
    BudgetYearDataType.EquipmentType.AdditionalEquipmentsAttachmentType equipAttachmentType =
                      objFactory.createBudgetYearDataTypeEquipmentTypeAdditionalEquipmentsAttachmentType();
    gov.grants.apply.system.attachments_v1.AttachedFileDataType.FileLocationType fileLocation;
     
    Attachment equipAttachment = getNarrative(12,periodBean,true,"BudgetPeriod_"+periodBean.getBudgetPeriod());
  
    
    if (equipAttachment.getContent() != null){
        //populate attachment info in schema element
    
        equipAttachmentType.setFileName(AttachedFileDataTypeStream.addExtension(equipAttachment.getFileName()));
        try{
         equipAttachmentType.setHashValue(S2SHashValue.getValue(equipAttachment.getContent())); 
         } catch(Exception ex){
            ex.printStackTrace();
            UtilFactory.log(ex.getMessage(),ex, "BudgetYearDataStream", "getEquipmentAttachment");
            throw new JAXBException(ex);
        }
        equipAttachmentType.setMimeType("application/octet-stream");
        fileLocation = attachmentsObjFactory.createAttachedFileDataTypeFileLocationType();
        fileLocation.setHref(equipAttachment.getContentId());
        equipAttachmentType.setFileLocation(fileLocation);
    
        equipAttachmentType.setTotalFundForAttachedEquipmentExist("Yes");
        
        equipType.setAdditionalEquipmentsAttachment(equipAttachmentType);
 
    }
    return equipType;

} 
/******************************************************
 * getNarrative 
 *    arguments: int narrativeTypeCodeToAttach  
 *                    (can be additional equipment (12) or 
 *                     additional keypersons (11) or budget justification(7))
 *               BudgetPeriodDataBean periodBean
 *               boolean periodLoop - true if narrative type can exist for each period
 *    returns:  Attachment
 *******************************************************/
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
private void getTravel(BudgetPeriodDataBean periodBean)
   throws JAXBException, CoeusException{
       BudgetYearDataType.TravelType travelType = objFactory.createBudgetYearDataTypeTravelType();
       travelType.setDomesticTravelCost(periodBean.getDomesticTravelCost());
       travelType.setForeignTravelCost(periodBean.getForeignTravelCost());
       travelType.setTotalTravelCost(periodBean.getTotalTravelCost());
       
       budgetYearData.setTravel(travelType);
}

   
private gov.grants.apply.system.attachments_v1.AttachedFileDataType getAttachedFileType(Attachment attachment) 
     throws JAXBException {
    
    gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
    gov.grants.apply.system.attachments_v1.AttachedFileDataType.FileLocationType fileLocation;
    gov.grants.apply.system.global_v1.HashValueType hashValueType;

    attachedFileType = attachmentsObjFactory.createAttachedFileDataType();
    fileLocation = attachmentsObjFactory.createAttachedFileDataTypeFileLocationType();
    hashValueType = globalObjFactory.createHashValueType();
    
    fileLocation.setHref(attachment.getContentId());
    attachedFileType.setFileLocation(fileLocation);
    attachedFileType.setFileName(AttachedFileDataTypeStream.addExtension(attachment.getFileName()));
    attachedFileType.setMimeType("application/octet-stream");
    try{
        attachedFileType.setHashValue(S2SHashValue.getValue(attachment.getContent()));
    }catch(Exception ex){
        ex.printStackTrace();
        UtilFactory.log(ex.getMessage(),ex, "RROtherProjectInfoStream", "getAttachedFile");
        throw new JAXBException(ex);
    }

    return attachedFileType;
           
}
   
    private Calendar getCal(Date date){
        if(date==null)
            return null;
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(date);
        return cal;
    }
    
    /**
     * Getter for property extraAttachments.
     * @return Value of property extraAttachments.
     */
    public java.util.Vector getExtraAttachments() {
        return extraAttachments;
    }
    
    /**
     * Setter for property extraAttachments.
     * @param extraAttachments New value of property extraAttachments.
     */
    public void setExtraAttachments(java.util.Vector extraAttachments) {
        this.extraAttachments = extraAttachments;
    }
    
}
