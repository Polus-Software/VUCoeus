/**
 * BudgetYearDataTypeStream_V1_4.java
 */
package edu.vanderbilt.coeus.s2s.generator;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalNarrativeFormBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.s2s.bean.BudgetPeriodDataBean;
import edu.mit.coeus.s2s.bean.CostBean;
import edu.mit.coeus.s2s.bean.EquipmentBean;
import edu.mit.coeus.s2s.bean.IndirectCostBean;
import edu.mit.coeus.s2s.bean.IndirectCostDetailBean;
import edu.mit.coeus.s2s.bean.OtherDirectCostBean;
import edu.mit.coeus.s2s.bean.S2STxnBean;
import edu.mit.coeus.s2s.generator.AttachmentValidator;
import edu.mit.coeus.s2s.generator.stream.AttachedFileDataTypeStream;
import edu.vanderbilt.coeus.s2s.generator.stream.KeyPersonStreamForBud_V1_4;
import edu.vanderbilt.coeus.s2s.generator.stream.OtherPersonnelStreamforBud_V1_4;
import edu.mit.coeus.s2s.util.S2SHashValue;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import gov.grants.apply.coeus.additionalequipment.AdditionalEquipmentList;
import gov.grants.apply.coeus.additionalequipment.AdditionalEquipmentListType;
import gov.grants.apply.forms.rr_budget_1_4_v1.BudgetYearDataType;
import gov.grants.apply.forms.rr_budget_1_4_v1.ObjectFactory;
import gov.grants.apply.system.attachments_v1.AttachedFileDataType;
import gov.grants.apply.system.global_v1.HashValueType;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;
import javax.xml.bind.JAXBException;
import org.apache.fop.apps.FOPException;
import org.w3c.dom.Document;

public class BudgetYearDataTypeStream_V1_4 extends AttachmentValidator {
    private ObjectFactory objFactory = new ObjectFactory();
    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
    private gov.grants.apply.coeus.additionalequipment.ObjectFactory equipObjFactory = new gov.grants.apply.coeus.additionalequipment.ObjectFactory();
    private Calendar calendar;
    private BudgetYearDataType budgetYearData = null;
    private CoeusVector cvBudgetYear;
    private S2STxnBean s2sTxnBean = new S2STxnBean();
    private Vector extraAttachments = new Vector();

    public BudgetYearDataType getBudgetYear(BudgetPeriodDataBean periodBean, HashMap attachmentMap) throws CoeusXMLException, CoeusException, DBException, JAXBException {
        this.budgetYearData = this.objFactory.createBudgetYearDataType();
        this.budgetYearData.setBudgetPeriodEndDate(this.getCal(periodBean.getEndDate()));
        this.budgetYearData.setBudgetPeriodStartDate(this.getCal(periodBean.getStartDate()));
        this.budgetYearData.setCognizantFederalAgency(periodBean.getCognizantFedAgency());
        this.budgetYearData.setDirectCosts(periodBean.getDirectCostsTotal());
        this.budgetYearData.setTotalCosts(periodBean.getTotalCosts());
        this.budgetYearData.setTotalCompensation(periodBean.getTotalCompensation());
        this.budgetYearData.setFee(BigDecimal.ZERO);
        
        // new for version 1.4
        this.budgetYearData.setTotalCostsFee(this.budgetYearData.getTotalCosts().add(this.budgetYearData.getFee()));
        
        this.setOtherPersonnel(periodBean);
        this.setIndirectCosts(periodBean);
        this.setKeyPersons(periodBean);
        this.setOtherDirectCosts(periodBean);
        this.setTravel(periodBean);
        this.setEquipment(periodBean);
        this.setParticipant(periodBean);
        return this.budgetYearData;
    }

    private void setOtherPersonnel(BudgetPeriodDataBean periodBean) throws CoeusException, JAXBException, DBException {
        BudgetYearDataType.OtherPersonnelType otherPersonnelType = this.objFactory.createBudgetYearDataTypeOtherPersonnelType();
        OtherPersonnelStreamforBud_V1_4 otherPersonnelStream = new OtherPersonnelStreamforBud_V1_4();
        BudgetYearDataType.OtherPersonnelType budgetOtherPersType = otherPersonnelStream.getOtherPersonnel(periodBean);
        this.budgetYearData.setOtherPersonnel(budgetOtherPersType);
    }

    private void setOtherDirectCosts(BudgetPeriodDataBean periodBean) throws JAXBException, CoeusException {
        BudgetYearDataType.OtherDirectCostsType otherDirectCostsType = this.objFactory.createBudgetYearDataTypeOtherDirectCostsType();
        CoeusVector cvOtherDirectCosts = periodBean.getOtherDirectCosts();
        OtherDirectCostBean otherDirectCostBean = (OtherDirectCostBean)cvOtherDirectCosts.elementAt(0);
        otherDirectCostsType.setADPComputerServices(otherDirectCostBean.getcomputer());
        otherDirectCostsType.setAlterationsRenovations(otherDirectCostBean.getAlterations());
        otherDirectCostsType.setConsultantServices(otherDirectCostBean.getconsultants());
        otherDirectCostsType.setEquipmentRentalFee(otherDirectCostBean.getEquipRental());
        otherDirectCostsType.setMaterialsSupplies(otherDirectCostBean.getmaterials());
        otherDirectCostsType.setPublicationCosts(otherDirectCostBean.getpublications());
        otherDirectCostsType.setSubawardConsortiumContractualCosts(otherDirectCostBean.getsubAwards());
        otherDirectCostsType.setTotalOtherDirectCost(otherDirectCostBean.gettotalOtherDirect());
        CoeusVector cvOthers = otherDirectCostBean.getOtherCosts();
        BudgetYearDataType.OtherDirectCostsType.OtherType otherTypes = this.objFactory.createBudgetYearDataTypeOtherDirectCostsTypeOtherType();
        for (int i = 0; i < cvOthers.size(); ++i) {
            HashMap hmCosts = new HashMap();
            hmCosts = (HashMap)cvOthers.elementAt(i);
            otherTypes = this.objFactory.createBudgetYearDataTypeOtherDirectCostsTypeOtherType();
            otherTypes.setCost(new BigDecimal(hmCosts.get("Cost").toString()));
            otherTypes.setDescription(hmCosts.get("Description").toString());
            otherDirectCostsType.getOther().add(otherTypes);
        }
        this.budgetYearData.setOtherDirectCosts(otherDirectCostsType);
    }

    private void setTravel(BudgetPeriodDataBean periodBean) throws JAXBException, CoeusException {
        BudgetYearDataType.TravelType travelType = this.objFactory.createBudgetYearDataTypeTravelType();
        travelType.setDomesticTravelCost(periodBean.getDomesticTravelCost());
        travelType.setForeignTravelCost(periodBean.getForeignTravelCost());
        travelType.setTotalTravelCost(periodBean.getTotalTravelCost());
        this.budgetYearData.setTravel(travelType);
    }

    private void setParticipant(BudgetPeriodDataBean periodBean) throws JAXBException, CoeusException {
        BudgetYearDataType.ParticipantTraineeSupportCostsType partType = this.objFactory.createBudgetYearDataTypeParticipantTraineeSupportCostsType();
        BudgetYearDataType.ParticipantTraineeSupportCostsType.OtherType partOther = this.objFactory.createBudgetYearDataTypeParticipantTraineeSupportCostsTypeOtherType();
        partType.setStipends(periodBean.getpartStipendCost());
        partType.setTravel(periodBean.getpartTravelCost());
        partType.setSubsistence(periodBean.getPartSubsistence());
        partType.setTuitionFeeHealthInsurance(periodBean.getPartTuition());
        partOther.setCost(periodBean.getpartOtherCost());
        partOther.setDescription("Other");
        partType.setOther(partOther);
        partType.setParticipantTraineeNumber(periodBean.getparticipantCount());
        partType.setTotalCost(periodBean.getpartOtherCost().add(periodBean.getpartStipendCost().add(periodBean.getpartTravelCost().add(periodBean.getPartSubsistence().add(periodBean.getPartTuition())))));
        this.budgetYearData.setParticipantTraineeSupportCosts(partType);
    }

    private AttachedFileDataType getBudgetJustification(BudgetPeriodDataBean periodBean) throws JAXBException, CoeusException, DBException {
        AttachedFileDataType budgetJustAttachmentType = this.attachmentsObjFactory.createAttachedFileDataType();
        AttachedFileDataType.FileLocationType fileLocation = this.attachmentsObjFactory.createAttachedFileDataTypeFileLocationType();
        HashValueType hashValueType = this.globalObjFactory.createHashValueType();
        Attachment budgJustAttachment = this.getNarrative(7, periodBean, false);
        if (budgJustAttachment.getContent() != null) {
            budgetJustAttachmentType.setFileName(AttachedFileDataTypeStream.addExtension((String)budgJustAttachment.getFileName()));
            try {
                budgetJustAttachmentType.setHashValue(S2SHashValue.getValue((byte[])budgJustAttachment.getContent()));
            }
            catch (Exception ex) {
                ex.printStackTrace();
                UtilFactory.log((String)ex.getMessage(), (Throwable)ex, (String)"BudgetYearDataStream", (String)"getBudgetJustification");
                throw new JAXBException(ex);
            }
            budgetJustAttachmentType.setMimeType("application/octet-stream");
            fileLocation = this.attachmentsObjFactory.createAttachedFileDataTypeFileLocationType();
            fileLocation.setHref(budgJustAttachment.getContentId());
            budgetJustAttachmentType.setFileLocation(fileLocation);
        }
        return budgetJustAttachmentType;
    }

    private void setEquipment(BudgetPeriodDataBean periodBean) throws JAXBException, CoeusException, DBException {
        CoeusVector additonalEquipList;
        BudgetYearDataType.EquipmentType equipmentType = this.objFactory.createBudgetYearDataTypeEquipmentType();
        BudgetYearDataType.EquipmentType.EquipmentListType equipmentListType = this.objFactory.createBudgetYearDataTypeEquipmentTypeEquipmentListType();
        CoeusVector cvEquipment = periodBean.getEquipment();
        CoeusVector cvEquipmentItems = new CoeusVector();
        EquipmentBean equipBean = new EquipmentBean();
        CostBean equipItemBean = new CostBean();
        if (cvEquipment.size() > 0) {
            equipBean = (EquipmentBean)cvEquipment.get(0);
            equipmentType.setTotalFund(equipBean.getTotalFund());
            if (equipBean.getExtraEquipmentList() != null) {
                equipmentType.setTotalFundForAttachedEquipment(equipBean.getTotalExtraFund());
            }
        }
        if ((cvEquipmentItems = equipBean.getEquipmentList()) != null) {
            for (int i = 0; i < cvEquipmentItems.size(); ++i) {
                equipItemBean = new CostBean();
                equipItemBean = (CostBean)cvEquipmentItems.get(i);
                equipmentListType = this.objFactory.createBudgetYearDataTypeEquipmentTypeEquipmentListType();
                equipmentListType.setFundsRequested(equipItemBean.getCost());
                equipmentListType.setEquipmentItem(equipItemBean.getDescription() == null ? equipItemBean.getCategory() : equipItemBean.getDescription());
                equipmentType.getEquipmentList().add(equipmentListType);
            }
        }
        if ((additonalEquipList = equipBean.getExtraEquipmentList()) != null) {
            AdditionalEquipmentList extraEquipList = this.equipObjFactory.createAdditionalEquipmentList();
            List equipList = extraEquipList.getEquipmentList();
            int size = additonalEquipList.size();
            for (int i = 0; i < size; ++i) {
                CostBean addEquipBean = (CostBean)additonalEquipList.get(i);
                AdditionalEquipmentListType.EquipmentListType equipListType = this.equipObjFactory.createAdditionalEquipmentListTypeEquipmentListType();
                equipListType.setFundsRequested(addEquipBean.getCost());
                equipListType.setEquipmentItem(addEquipBean.getDescription() == null ? addEquipBean.getCategory() : addEquipBean.getDescription());
                equipList.add(equipListType);
                extraEquipList.setProposalNumber(periodBean.getProposalNumber());
                extraEquipList.setBudgetPeriod(new BigInteger(Integer.toString(periodBean.getBudgetPeriod())));
            }
            CoeusXMLGenrator xmlGen = new CoeusXMLGenrator();
            Document extraKeyPerDoc = xmlGen.marshelObject((Object)extraEquipList, "gov.grants.apply.coeus.additionalequipment");
            byte[] pdfBytes = null;
            try {
                InputStream templateIS = this.getClass().getResourceAsStream("/edu/mit/coeus/s2s/template/AdditionalEquipmentAttachment.xsl");
                byte[] tmplBytes = new byte[templateIS.available()];
                templateIS.read(tmplBytes);
                String debug = CoeusProperties.getProperty((String)"GENERATE_XML_FOR_DEBUGGING");
                pdfBytes = debug.equalsIgnoreCase("Y") || debug.equalsIgnoreCase("Yes") ? xmlGen.generatePdfBytes(extraKeyPerDoc, tmplBytes, null, periodBean.getProposalNumber() + "_" + equipItemBean.getBudgetPeriod() + "_EXTRA_EQUIPMENT") : xmlGen.generatePdfBytes(extraKeyPerDoc, tmplBytes);
            }
            catch (IOException ex) {
                UtilFactory.log((String)ex.getMessage(), (Throwable)ex, (String)"KeyPersonStream", (String)"getKeyPersons");
                throw new CoeusException(ex.getMessage());
            }
            catch (FOPException ex) {
                UtilFactory.log((String)ex.getMessage(), (Throwable)ex, (String)"KeyPersonStream", (String)"getKeyPersons");
                throw new CoeusException(ex.getMessage());
            }
            ProposalNarrativeFormBean propNarrBean = new ProposalNarrativeFormBean();
            propNarrBean.setProposalNumber(periodBean.getProposalNumber());
            propNarrBean.setModuleTitle("BudgetPeriod_" + periodBean.getBudgetPeriod());
            propNarrBean.setComments("Auto generated document for Equipment");
            propNarrBean.setModuleStatusCode('C');
            propNarrBean.setNarrativeTypeCode(12);
            ProposalNarrativePDFSourceBean propNarrPDFBean = new ProposalNarrativePDFSourceBean();
            propNarrPDFBean.setProposalNumber(periodBean.getProposalNumber());
            propNarrPDFBean.setAcType("I");
            propNarrPDFBean.setFileBytes(pdfBytes);
            propNarrPDFBean.setFileName(AttachedFileDataTypeStream.addExtension((String)(periodBean.getProposalNumber() + "_ADDITIONAL_EQUIPMENT")));
            this.s2sTxnBean.insertAutoGenNarrativeDetails(propNarrBean, propNarrPDFBean, periodBean.getBudgetPeriod() == 1);
            AttachedFileDataType extraEquipmentAttachment = this.getEquipmentAttachment(equipmentType, periodBean);
            if (extraEquipmentAttachment != null) {
                equipmentType.setAdditionalEquipmentsAttachment(extraEquipmentAttachment);
            }
        }
        if (equipmentType.getTotalFund() != null) {
            this.budgetYearData.setEquipment(equipmentType);
        }
    }

    private void setIndirectCosts(BudgetPeriodDataBean periodBean) throws JAXBException, CoeusException {
        BudgetYearDataType.IndirectCostsType indirectCostsType = this.objFactory.createBudgetYearDataTypeIndirectCostsType();
        IndirectCostBean indirectCostBean = periodBean.getIndirectCosts();
        indirectCostsType.setTotalIndirectCosts(indirectCostBean.getTotalIndirectCosts());
        CoeusVector cvIndCostList = indirectCostBean.getIndirectCostDetails();
        if (cvIndCostList != null && !cvIndCostList.isEmpty()) {
            indirectCostsType.setTotalIndirectCosts(indirectCostBean.getTotalIndirectCosts());
            for (int i = 0; i < cvIndCostList.size(); ++i) {
                BudgetYearDataType.IndirectCostsType.IndirectCostType indirectCostListType = this.objFactory.createBudgetYearDataTypeIndirectCostsTypeIndirectCostType();
                IndirectCostDetailBean indCostDetBean = new IndirectCostDetailBean();
                indCostDetBean = (IndirectCostDetailBean)cvIndCostList.elementAt(i);
                indirectCostListType.setBase(indCostDetBean.getBase());
                indirectCostListType.setCostType(indCostDetBean.getCostType());
                indirectCostListType.setFundRequested(indCostDetBean.getFunds());
                indirectCostListType.setRate(indCostDetBean.getRate());
                indirectCostsType.getIndirectCost().add(indirectCostListType);
            }
            this.budgetYearData.setIndirectCosts(indirectCostsType);
        }
    }

    private void setKeyPersons(BudgetPeriodDataBean periodBean) throws JAXBException, CoeusException, DBException {
        AttachedFileDataType extraKeyPersonsAttachment;
        KeyPersonStreamForBud_V1_4 keyPersonStream = new KeyPersonStreamForBud_V1_4();
        keyPersonStream.setAttachmentMap(this.getAttachmentMap());
        BudgetYearDataType.KeyPersonsType keyPersons = keyPersonStream.getKeyPersons(periodBean);
        if (keyPersons.getTotalFundForAttachedKeyPersons() != null && (extraKeyPersonsAttachment = this.getKeyPersonAttachment(keyPersons, periodBean)) != null) {
            keyPersons.setAttachedKeyPersons(extraKeyPersonsAttachment);
        }
        this.extraAttachments.addAll(keyPersonStream.getExtraAttachments());
        this.budgetYearData.setKeyPersons(keyPersons);
    }

    private AttachedFileDataType getKeyPersonAttachment(BudgetYearDataType.KeyPersonsType keyPersonType, BudgetPeriodDataBean periodBean) throws JAXBException, CoeusException, DBException {
        AttachedFileDataType extraKeyPersonsAttachmentType = this.attachmentsObjFactory.createAttachedFileDataType();
        Attachment keyPersonsAttachment = this.getNarrative(11, periodBean, true, "BudgetPeriod_" + periodBean.getBudgetPeriod());
        AttachedFileDataType.FileLocationType fileLocation = this.attachmentsObjFactory.createAttachedFileDataTypeFileLocationType();
        HashValueType hashValueType = this.globalObjFactory.createHashValueType();
        if (keyPersonsAttachment.getContent() != null) {
            extraKeyPersonsAttachmentType.setFileName(AttachedFileDataTypeStream.addExtension((String)keyPersonsAttachment.getFileName()));
            try {
                extraKeyPersonsAttachmentType.setHashValue(S2SHashValue.getValue((byte[])keyPersonsAttachment.getContent()));
            }
            catch (Exception ex) {
                ex.printStackTrace();
                UtilFactory.log((String)ex.getMessage(), (Throwable)ex, (String)"BudgetYearDataTypeStream_V1_4", (String)"getKeyPersonAttachment");
                throw new JAXBException(ex);
            }
            extraKeyPersonsAttachmentType.setMimeType("application/octet-stream");
            fileLocation = this.attachmentsObjFactory.createAttachedFileDataTypeFileLocationType();
            fileLocation.setHref(keyPersonsAttachment.getContentId());
            extraKeyPersonsAttachmentType.setFileLocation(fileLocation);
        }
        return extraKeyPersonsAttachmentType;
    }

    private AttachedFileDataType getEquipmentAttachment(BudgetYearDataType.EquipmentType equipType, BudgetPeriodDataBean periodBean) throws JAXBException, CoeusException, DBException {
        AttachedFileDataType extraEquipmentAttachmentType = this.attachmentsObjFactory.createAttachedFileDataType();
        Attachment equipAttachment = this.getNarrative(12, periodBean, true, "BudgetPeriod_" + periodBean.getBudgetPeriod());
        AttachedFileDataType.FileLocationType fileLocation = this.attachmentsObjFactory.createAttachedFileDataTypeFileLocationType();
        HashValueType hashValueType = this.globalObjFactory.createHashValueType();
        if (equipAttachment.getContent() != null) {
            extraEquipmentAttachmentType.setFileName(AttachedFileDataTypeStream.addExtension((String)equipAttachment.getFileName()));
            try {
                extraEquipmentAttachmentType.setHashValue(S2SHashValue.getValue((byte[])equipAttachment.getContent()));
            }
            catch (Exception ex) {
                ex.printStackTrace();
                UtilFactory.log((String)ex.getMessage(), (Throwable)ex, (String)"BudgetYearDataTypeStream_V1_4", (String)"getEquipmentAttachment");
                throw new JAXBException(ex);
            }
            extraEquipmentAttachmentType.setMimeType("application/octet-stream");
            fileLocation = this.attachmentsObjFactory.createAttachedFileDataTypeFileLocationType();
            fileLocation.setHref(equipAttachment.getContentId());
            extraEquipmentAttachmentType.setFileLocation(fileLocation);
        }
        return extraEquipmentAttachmentType;
    }

    private Attachment getNarrative(int narrativeTypeCodeToAttach, BudgetPeriodDataBean periodBean, boolean periodLoop) throws CoeusException, DBException {
        return this.getNarrative(narrativeTypeCodeToAttach, periodBean, periodLoop, null);
    }

    private Attachment getNarrative(int narrativeTypeCodeToAttach, BudgetPeriodDataBean periodBean, boolean periodLoop, String titleCheckStr) throws CoeusException, DBException {
        ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
        String title = "";
        String description = "";
        String propNumber = periodBean.getProposalNumber();
        LinkedHashMap<String, String> hmArg = new LinkedHashMap<String, String>();
        Attachment attachment = new Attachment();
        Vector vctNarrative = proposalNarrativeTxnBean.getPropNarrativePDFForProposal(propNumber);
        int size = vctNarrative == null ? 0 : vctNarrative.size();
        for (int row = 0; row < size; ++row) {
            ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean = (ProposalNarrativePDFSourceBean)vctNarrative.elementAt(row);
            int moduleNum = proposalNarrativePDFSourceBean.getModuleNumber();
            HashMap hmNarrative = new HashMap();
            hmNarrative = this.s2sTxnBean.getNarrativeInfo(propNumber, moduleNum);
            int narrativeTypeCode = Integer.parseInt(hmNarrative.get("NARRATIVE_TYPE_CODE").toString());
            if (periodLoop) {
                title = hmNarrative.get("MODULE_TITLE").toString();
            }
            description = hmNarrative.get("DESCRIPTION").toString();
            if (narrativeTypeCodeToAttach != narrativeTypeCode || titleCheckStr != null && !titleCheckStr.equalsIgnoreCase(title)) continue;
            hmArg.put("M", Integer.toString(moduleNum));
            hmArg.put("DESCRIPTION", description);
            if (periodLoop) {
                hmArg.put("TITLE", title);
            }
            if ((attachment = this.getAttachment(hmArg)) != null) continue;
            String contentId = this.createContentId(hmArg);
            proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
            attachment = new Attachment();
            attachment.setContent(proposalNarrativePDFSourceBean.getFileBytes());
            String contentType = "application/octet-stream";
            attachment.setFileName(AttachedFileDataTypeStream.addExtension((String)contentId));
            attachment.setContentId(contentId);
            attachment.setContentType(contentType);
            this.addAttachment(hmArg, attachment);
            break;
        }
        return attachment;
    }

    private void getTravel(BudgetPeriodDataBean periodBean) throws JAXBException, CoeusException {
        BudgetYearDataType.TravelType travelType = this.objFactory.createBudgetYearDataTypeTravelType();
        travelType.setDomesticTravelCost(periodBean.getDomesticTravelCost());
        travelType.setForeignTravelCost(periodBean.getForeignTravelCost());
        travelType.setTotalTravelCost(periodBean.getTotalTravelCost());
        this.budgetYearData.setTravel(travelType);
    }

    private AttachedFileDataType getAttachedFileType(Attachment attachment) throws JAXBException {
        AttachedFileDataType attachedFileType = this.attachmentsObjFactory.createAttachedFileDataType();
        AttachedFileDataType.FileLocationType fileLocation = this.attachmentsObjFactory.createAttachedFileDataTypeFileLocationType();
        HashValueType hashValueType = this.globalObjFactory.createHashValueType();
        fileLocation.setHref(attachment.getContentId());
        attachedFileType.setFileLocation(fileLocation);
        attachedFileType.setFileName(AttachedFileDataTypeStream.addExtension((String)attachment.getFileName()));
        attachedFileType.setMimeType("application/octet-stream");
        try {
            attachedFileType.setHashValue(S2SHashValue.getValue((byte[])attachment.getContent()));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            UtilFactory.log((String)ex.getMessage(), (Throwable)ex, (String)"BudgetYearDataTypeStream_V1_4", (String)"getAttachedFile");
            throw new JAXBException(ex);
        }
        return attachedFileType;
    }

    private Calendar getCal(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(date);
        return cal;
    }

    public Vector getExtraAttachments() {
        return this.extraAttachments;
    }

    public void setExtraAttachments(Vector extraAttachments) {
        this.extraAttachments = extraAttachments;
    }
}