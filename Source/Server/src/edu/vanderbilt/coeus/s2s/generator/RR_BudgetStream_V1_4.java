/**
 * RR_BudgetStream_V1_4.java
 */
package edu.vanderbilt.coeus.s2s.generator;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceDataTxnBean;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.s2s.bean.BudgetPeriodDataBean;
import edu.mit.coeus.s2s.bean.BudgetSummaryDataBean;
import edu.mit.coeus.s2s.bean.OtherDirectCostBean;
import edu.mit.coeus.s2s.bean.S2STxnBean;
import edu.vanderbilt.coeus.s2s.generator.BudgetYearDataTypeStream_V1_4;
import edu.mit.coeus.s2s.generator.S2SBaseStream;
import edu.mit.coeus.s2s.generator.stream.AttachedFileDataTypeStream;
import edu.mit.coeus.s2s.util.S2SHashValue;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import gov.grants.apply.forms.rr_budget_1_4_v1.BudgetYearDataType;
import gov.grants.apply.forms.rr_budget_1_4_v1.ObjectFactory;
import gov.grants.apply.forms.rr_budget_1_4_v1.RRBudget14;
import gov.grants.apply.forms.rr_budget_1_4_v1.RRBudget14Type;
import gov.grants.apply.system.attachments_v1.AttachedFileDataType;
import gov.grants.apply.system.global_v1.HashValueType;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.TimeZone;
import java.util.Vector;
import javax.xml.bind.JAXBException;

public class RR_BudgetStream_V1_4 extends S2SBaseStream {
    private ObjectFactory objFactory = new ObjectFactory();
    private gov.grants.apply.system.globallibrary_v2.ObjectFactory globalv2ObjFactory = new gov.grants.apply.system.globallibrary_v2.ObjectFactory();
    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
    private CoeusXMLGenrator xmlGenerator = new CoeusXMLGenrator();
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
    private S2STxnBean s2sTxnBean = new S2STxnBean();
    private OrganizationMaintenanceDataTxnBean orgMaintDataTxnBean = new OrganizationMaintenanceDataTxnBean();
    private ProposalDevelopmentTxnBean propDevTxnBean = new ProposalDevelopmentTxnBean();
    private BudgetSummaryDataBean budgetSummaryBean = new BudgetSummaryDataBean();
    private CoeusVector cvPeriod;
    private ProposalDevelopmentFormBean propDevFormBean;
    private OrganizationMaintenanceFormBean orgMaintFormBean;
    private HashMap attachmentMap;
    private Vector vecContentIds;
    private Hashtable propData;
    private String propNumber;
    private UtilFactory utilFactory;
    private Calendar calendar;
    private Vector extraAttachments = new Vector();
    private String organizationID;
    private String perfOrganizationID;

    private RRBudget14Type getRRBudget() throws CoeusXMLException, CoeusException, DBException {
        RRBudget14 rrBudget = null;
        try {
            this.propDevFormBean = this.getPropDevData();
            this.orgMaintFormBean = this.getOrgData();
            this.budgetSummaryBean = this.getBudgetInfo();
            this.cvPeriod = this.budgetSummaryBean.getBudgetPeriods();
            this.attachmentMap = this.getAttachmentMap();
            rrBudget = this.objFactory.createRRBudget14();
            rrBudget.setFormVersion("1.4");
            rrBudget.setDUNSID(this.orgMaintFormBean.getDunsNumber());
            rrBudget.setBudgetType("Project");
            rrBudget.setOrganizationName(this.getOrgName());
            CoeusVector cvOtherDirectCosts = this.budgetSummaryBean.getOtherDirectCosts();
            OtherDirectCostBean otherDirectCostBean = (OtherDirectCostBean)cvOtherDirectCosts.get(0);
            RRBudget14Type.BudgetSummaryType budgetSummaryType = this.objFactory.createRRBudget14TypeBudgetSummaryType();
            budgetSummaryType.setCumulativeADPComputerServices(otherDirectCostBean.getcomputer());
            budgetSummaryType.setCumulativeAlterationsAndRenovations(otherDirectCostBean.getAlterations());
            budgetSummaryType.setCumulativeConsultantServices(otherDirectCostBean.getconsultants());
            budgetSummaryType.setCumulativeDomesticTravelCosts(this.budgetSummaryBean.getCumDomesticTravel());
            budgetSummaryType.setCumulativeEquipmentFacilityRentalFees(otherDirectCostBean.getEquipRental());
            budgetSummaryType.setCumulativeFee(this.budgetSummaryBean.getCumFee());
            budgetSummaryType.setCumulativeForeignTravelCosts(this.budgetSummaryBean.getCumForeignTravel());
            budgetSummaryType.setCumulativeMaterialAndSupplies(otherDirectCostBean.getmaterials());
            budgetSummaryType.setCumulativeNoofTrainees(this.budgetSummaryBean.getparticipantCount());
            CoeusVector cvOthers = otherDirectCostBean.getOtherCosts();
            for (int i = 0; i < cvOthers.size(); ++i) {
                HashMap hmCosts = new HashMap();
                hmCosts = (HashMap)cvOthers.elementAt(i);
                if (i == 0) {
                    budgetSummaryType.setCumulativeOther1DirectCost(new BigDecimal(hmCosts.get("Cost").toString()));
                    continue;
                }
                if (i == 1) {
                    budgetSummaryType.setCumulativeOther2DirectCost(new BigDecimal(hmCosts.get("Cost").toString()));
                    continue;
                }
                budgetSummaryType.setCumulativeOther3DirectCost(new BigDecimal(hmCosts.get("Cost").toString()));
            }
            budgetSummaryType.setCumulativeTotalFundsRequestedOtherDirectCosts(otherDirectCostBean.gettotalOtherDirect());
            budgetSummaryType.setCumulativeOtherTraineeCost(this.budgetSummaryBean.getpartOtherCost());
            budgetSummaryType.setCumulativePublicationCosts(otherDirectCostBean.getpublications());
            budgetSummaryType.setCumulativeSubawardConsortiumContractualCosts(otherDirectCostBean.getsubAwards());
            budgetSummaryType.setCumulativeTotalFundsRequestedDirectCosts(this.budgetSummaryBean.getCumTotalDirectCosts());
            budgetSummaryType.setCumulativeTotalFundsRequestedDirectIndirectCosts(this.budgetSummaryBean.getCumTotalCosts());
            budgetSummaryType.setCumulativeTotalFundsRequestedEquipment(this.budgetSummaryBean.getCumEquipmentFunds());
            budgetSummaryType.setCumulativeTotalFundsRequestedIndirectCost(this.budgetSummaryBean.getCumTotalIndirectCosts());
            budgetSummaryType.setCumulativeTotalFundsRequestedOtherPersonnel(this.budgetSummaryBean.getCumTotalFundsForOtherPersonnel());
            budgetSummaryType.setCumulativeTotalFundsRequestedPersonnel(this.budgetSummaryBean.getCumTotalFundsForPersonnel());
            budgetSummaryType.setCumulativeTotalFundsRequestedSeniorKeyPerson(this.budgetSummaryBean.getCumTotalFundsForSrPersonnel());
            budgetSummaryType.setCumulativeTotalFundsRequestedTraineeCosts(otherDirectCostBean.getParticipantTotal());
            budgetSummaryType.setCumulativeTotalFundsRequestedTravel(this.budgetSummaryBean.getCumTravel());
            budgetSummaryType.setCumulativeTotalNoOtherPersonnel(this.budgetSummaryBean.getCumNumOtherPersonnel().intValue());
            budgetSummaryType.setCumulativeTraineeStipends(otherDirectCostBean.getPartStipends());
            budgetSummaryType.setCumulativeTraineeSubsistence(otherDirectCostBean.getPartSubsistence());
            budgetSummaryType.setCumulativeTraineeTravel(otherDirectCostBean.getPartTravel());
            budgetSummaryType.setCumulativeTraineeTuitionFeesHealthInsurance(otherDirectCostBean.getPartTuition());
            
            // new for version 1.4
            budgetSummaryType.setCumulativeTotalCostsFee(this.budgetSummaryBean.getCumTotalCosts().add(this.budgetSummaryBean.getCumFee()));
            
            rrBudget.setBudgetSummary(budgetSummaryType);
            rrBudget.setBudgetJustificationAttachment(this.getBudgetJustification());
            int numPeriods = 0;
            if (this.cvPeriod != null) {
                numPeriods = this.cvPeriod.size();
                for (int i2 = 1; i2 < numPeriods + 1; ++i2) {
                    rrBudget.getBudgetYear().add(this.getBudgetYear(i2, this.cvPeriod));
                }
            }
        }
        catch (JAXBException jaxbEx) {
            UtilFactory.log((String)jaxbEx.getMessage(), (Throwable)jaxbEx, (String)"RR_BudgetStream_V1_4", (String)"getRRBudget()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }
        return rrBudget;
    }

    private ProposalDevelopmentFormBean getPropDevData() throws DBException, CoeusException {
        if (this.propNumber == null) {
            throw new CoeusXMLException("Proposal Number is Null");
        }
        return this.propDevTxnBean.getProposalDevelopmentDetails(this.propNumber);
    }

    private OrganizationMaintenanceFormBean getOrgData() throws CoeusXMLException, CoeusException, DBException {
        HashMap hmOrg = this.s2sTxnBean.getOrganizationID(this.propNumber, "O");
        if (hmOrg != null && hmOrg.get("ORGANIZATION_ID") != null) {
            this.organizationID = hmOrg.get("ORGANIZATION_ID").toString();
        }
        return this.orgMaintDataTxnBean.getOrganizationMaintenanceDetails(this.organizationID);
    }

    private BudgetSummaryDataBean getBudgetInfo() throws JAXBException, CoeusException, DBException {
        return this.s2sTxnBean.getBudgetInfo(this.propNumber);
    }

    private AttachedFileDataType getBudgetJustification() throws JAXBException, CoeusException, DBException {
        AttachedFileDataType budgetJustAttachmentType = this.attachmentsObjFactory.createAttachedFileDataType();
        AttachedFileDataType.FileLocationType fileLocation = this.attachmentsObjFactory.createAttachedFileDataTypeFileLocationType();
        HashValueType hashValueType = this.globalObjFactory.createHashValueType();
        Attachment budgJustAttachment = this.getNarrative(132);
        if (budgJustAttachment.getContent() != null) {
            budgetJustAttachmentType.setFileName(AttachedFileDataTypeStream.addExtension((String)budgJustAttachment.getFileName()));
            try {
                budgetJustAttachmentType.setHashValue(S2SHashValue.getValue((byte[])budgJustAttachment.getContent()));
            }
            catch (Exception ex) {
                ex.printStackTrace();
                UtilFactory.log((String)ex.getMessage(), (Throwable)ex, (String)"RR_BudgetStream_V1_4", (String)"getBudgetJustification");
                throw new JAXBException(ex);
            }
            budgetJustAttachmentType.setMimeType("application/octet-stream");
            fileLocation = this.attachmentsObjFactory.createAttachedFileDataTypeFileLocationType();
            fileLocation.setHref(budgJustAttachment.getContentId());
            budgetJustAttachmentType.setFileLocation(fileLocation);
        }
        return budgetJustAttachmentType;
    }

    private Attachment getNarrative(int narrativeTypeCodeToAttach) throws CoeusException, DBException {
        ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
        String title = "";
        String description = "";
        LinkedHashMap<String, String> hmArg = new LinkedHashMap<String, String>();
        Attachment attachment = new Attachment();
        Vector vctNarrative = proposalNarrativeTxnBean.getPropNarrativePDFForProposal(this.propNumber);
        int size = vctNarrative == null ? 0 : vctNarrative.size();
        for (int row = 0; row < size; ++row) {
            ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean = (ProposalNarrativePDFSourceBean)vctNarrative.elementAt(row);
            int moduleNum = proposalNarrativePDFSourceBean.getModuleNumber();
            HashMap hmNarrative = new HashMap();
            hmNarrative = this.s2sTxnBean.getNarrativeInfo(this.propNumber, moduleNum);
            int narrativeTypeCode = Integer.parseInt(hmNarrative.get("NARRATIVE_TYPE_CODE").toString());
            title = hmNarrative.get("MODULE_TITLE").toString();
            description = hmNarrative.get("DESCRIPTION").toString();
            if (narrativeTypeCodeToAttach != narrativeTypeCode) continue;
            hmArg.put("M", Integer.toString(moduleNum));
            hmArg.put("DESCRIPTION", description);
            attachment = this.getAttachment(hmArg);
            if (attachment != null) continue;
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

    private BudgetYearDataType getBudgetYear(CoeusVector cvPeriod) throws JAXBException, CoeusException, DBException {
        BudgetPeriodDataBean periodBean = (BudgetPeriodDataBean)cvPeriod.elementAt(0);
        BudgetYearDataTypeStream_V1_4 budgetYearDataStream = new BudgetYearDataTypeStream_V1_4();
        this.attachmentMap = this.getAttachmentMap();
        budgetYearDataStream.setAttachmentMap(this.attachmentMap);
        BudgetYearDataType budgetYearDataType = this.objFactory.createBudgetYearDataType();
        budgetYearDataType = budgetYearDataStream.getBudgetYear(periodBean, this.attachmentMap);
        this.extraAttachments.addAll(budgetYearDataStream.getExtraAttachments());
        return budgetYearDataType;
    }

    private BudgetYearDataType getBudgetYear(int period, CoeusVector cvPeriod) throws JAXBException, CoeusException, DBException {
        BudgetPeriodDataBean periodBean = (BudgetPeriodDataBean)cvPeriod.elementAt(period - 1);
        BudgetYearDataTypeStream_V1_4 budgetYearDataStream = new BudgetYearDataTypeStream_V1_4();
        budgetYearDataStream.setAttachmentMap(this.attachmentMap);
        BudgetYearDataType budgetYearDataType = this.objFactory.createBudgetYearDataType();
        budgetYearDataType = budgetYearDataStream.getBudgetYear(periodBean, this.attachmentMap);
        this.extraAttachments.addAll(budgetYearDataStream.getExtraAttachments());
        return budgetYearDataType;
    }

    private String getOrgName() throws JAXBException, CoeusException {
        String orgName = this.orgMaintFormBean.getOrganizationName();
        return orgName;
    }

    private static BigDecimal convDoubleToBigDec(double d) {
        return new BigDecimal(d);
    }

    private Calendar getCal(Date date) {
        if (date == null) {
            return null;
        }
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

    public Object getStream(HashMap ht) throws JAXBException, CoeusException, DBException {
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return this.getRRBudget();
    }
}