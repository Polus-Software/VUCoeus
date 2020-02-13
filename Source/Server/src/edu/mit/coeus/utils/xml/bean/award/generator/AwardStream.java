/*
 * AwardStream.java
 * Created on August 18, 2004, 4:33 PM
 * @author   bijosht
 */

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.utils.xml.bean.award.generator;



import edu.mit.coeus.customelements.bean.CustomElementsInfoBean;
import edu.mit.coeus.irb.bean.ProtocolDataTxnBean;
import edu.mit.coeus.irb.bean.ProtocolInfoBean;
import edu.mit.coeus.unit.bean.UnitAdministratorBean;
import edu.mit.coeus.unit.bean.UnitDataTxnBean;
import edu.mit.coeus.unit.bean.UnitDetailFormBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.bean.CoeusParameterBean;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.bean.CoiDisclForItemBean;
import edu.mit.coeus.bean.CommentTypeBean;
import edu.mit.coeus.bean.FrequencyBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexMaintenanceDataTxnBean;
import edu.mit.coeus.irb.bean.PersonInfoTxnBean;
import edu.mit.coeus.utils.xml.bean.award.*;
import edu.mit.coeus.utils.xml.bean.award.KeyPersonType;
import edu.mit.coeus.utils.xml.bean.award.impl.*;
//import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.instprop.bean.InstituteProposalLookUpDataTxnBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.irb.bean.PersonInfoFormBean;
import edu.mit.coeus.subcontract.bean.SubContractFundingSourceBean;
import edu.mit.coeus.subcontract.bean.SubContractTxnBean;
import edu.mit.coeus.xml.generator.ReportBaseStream;

import java.util.*;
import java.io.*;
import java.math.BigDecimal;
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.Marshaller;

public class AwardStream extends ReportBaseStream{

//    private static final String DATE_FORMAT = "dd MMM yyyy";
//    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yy";
    private DateUtils dateUtils;

//    private Vector vecMessages;
    private AwardLookUpDataTxnBean awardLookUpDataTxnBean;

    private static final int FISCAL_REPORT_COMMENT_CODE = 3;
    private static final int GENEREAL_COMMENT_CODE = 2;
    private static final int INTELLECTUAL_PROPERTY_COMMENT_CODE = 4;
    private static final int  PROCUREMENT_COMMENT_CODE = 5;
    private static final int PROPERTY_COMMENT_CODE =6;


    private static final String FISCAL_REPORT = "Fiscal Report";
    private static final String GENEREAL_COMMENT = "General";
    private static final String INTELLECTUAL_PROPERTY = "Intellectual Property";
    private static final String PROCUREMENT_COMMENT = "Procurement";
    private static final String PROPERTY_COMMENT = "Property";

    private static final int COST_SHARING_COMMENT = 9;
    private static final int IDC_COMMENT = 8;
    private static final int SPECIAL_RATE_COMMENT = 7;

    private static final String ADDRESS_LIST = "ADDRESS_LIST";
    private static final String CLOSEOUT = "CLOSEOUT";
    private static final String COMMENTS = "COMMENTS";
    private static final String COST_SHARING = "COST_SHARING";
    private static final String EQUIPMENT = "EQUIPMENT";
    private static final String FLOW_THRU = "FLOW_THRU";
    private static final String FOREIGN_TRAVEL = "FOREIGN_TRAVEL";
    private static final String HIERARCHY_INFO = "HIERARCHY_INFO";
    private static final String INDIRECT_COST = "INDIRECT_COST";
    private static final String PAYEMENT = "PAYEMENT";
    private static final String PROPOSAL_DUE = "PROPOSAL_DUE";
    private static final String REPORTING = "REPORTING";
    private static final String SIGNATURE_REQUIRED ="SIGNATURE_REQUIRED";
    private static final String SCIENCE_CODE = "SCIENCE_CODE";
    private static final String SPECIAL_REVIEW = "SPECIAL_REVIEW";
    private static final String SUBCONTRACT = "SUBCONTRACT";
    private static final String TECH_REPORTING = "TECH_REPORTING";
    private static final String TERMS = "TERMS";
    //start csse 2010
    private static final String OTHER_DATA = "OTHER_DATA";
    //end case 2010
    //Added for Case 3122 - Award Notice Enhancement -Start
    private static final String FUNDING_SUMMARY = "FUNDING_SUMMARY";
     //Added for Case 3122 - Award Notice Enhancement -End
//    private UtilFactory UtilFactory;
    /** Creates a new instance of AwardStream */
    public AwardStream() {
        dateUtils = new DateUtils();
        awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
//        UtilFactory = new UtilFactory();
    }

    public Object getObjectStream(Hashtable htData) throws DBException,CoeusException{
        AwardNotice awardNotice = null;
        try{

        String loggedInUser = (String)htData.get("LOGGED_IN_USER");
        String reportType = (String)htData.get("REPORT_TYPE");

        if(loggedInUser == null || reportType == null) {
            CoeusException coeusException = new CoeusException();
            coeusException.setMessage("No data found");
            throw coeusException;
        }

        Object object = null;

        if(reportType.equals("AwardNotice")) {
            AwardBean awBean = (AwardBean)htData.get("AWARD_BEAN");
            object = getAwardNoticeReportStream(awBean, htData, loggedInUser);
        }else if(reportType.equals("DeltaReport")) {
            CoeusVector cvData = (CoeusVector)htData.get("DATA");
            AwardBean awardBean = (AwardBean)cvData.get(0);
            Integer selectedSeqNumber = (Integer)cvData.get(1);
            Integer amtSeqNumber = (Integer)cvData.get(2);
            Boolean boolSigantureRequired = (Boolean) cvData.get(3);
            String mitAwardNumber = awardBean.getMitAwardNumber();
            object = getDeltaReportStream(mitAwardNumber, selectedSeqNumber.intValue(), amtSeqNumber.intValue(), awardBean.getSequenceNumber(), boolSigantureRequired.booleanValue(), loggedInUser);
        }

        return object;

        }catch (Exception exception) {
            throw new CoeusException(exception.getMessage());
        }
    }

    //public ByteArrayOutputStream getAwardNoticeReportStream(AwardBean awBean,Hashtable htData,String loggedInUser)
    public Object getAwardNoticeReportStream(AwardBean awBean,Hashtable htData,String loggedInUser)
    throws CoeusException{
        try {
            String unitNumber=null;
            AwardType awardType = new AwardTypeImpl();
            AwardNotice awardNotice = new AwardNoticeImpl();
            AwardNoticeType.PrintRequirementType printRequirementType = new AwardNoticeTypeImpl.PrintRequirementTypeImpl();
            awardNotice.setPrintRequirement(printRequirementType);
            if (((Boolean)htData.get(ADDRESS_LIST)).booleanValue()) {
                awardNotice.getPrintRequirement().setAddressListRequired("1");
            } else {
                awardNotice.getPrintRequirement().setAddressListRequired("0");
            }
            if (((Boolean)htData.get(CLOSEOUT)).booleanValue()) {
                awardNotice.getPrintRequirement().setCloseoutRequired("1");
            } else {
                awardNotice.getPrintRequirement().setCloseoutRequired("0");
            }
            if (((Boolean)htData.get(COMMENTS)).booleanValue()) {
                awardNotice.getPrintRequirement().setCommentsRequired("1");
            } else {
                awardNotice.getPrintRequirement().setCommentsRequired("0");
            }
            if (((Boolean)htData.get(COST_SHARING)).booleanValue()) {
                awardNotice.getPrintRequirement().setCostSharingRequired("1");
            } else {
                awardNotice.getPrintRequirement().setCostSharingRequired("0");
            }
            if (((Boolean)htData.get(EQUIPMENT)).booleanValue()) {
                awardNotice.getPrintRequirement().setEquipmentRequired("1");
            } else {
                awardNotice.getPrintRequirement().setEquipmentRequired("0");
            }
            if (((Boolean)htData.get(FLOW_THRU)).booleanValue()) {
                awardNotice.getPrintRequirement().setFlowThruRequired("1");
            } else {
                awardNotice.getPrintRequirement().setFlowThruRequired("0");
            }
            if (((Boolean)htData.get(FOREIGN_TRAVEL)).booleanValue()) {
                awardNotice.getPrintRequirement().setForeignTravelRequired("1");
            } else {
                awardNotice.getPrintRequirement().setForeignTravelRequired("0");
            }
            if (((Boolean)htData.get(HIERARCHY_INFO)).booleanValue()) {
                awardNotice.getPrintRequirement().setHierarchyInfoRequired("1");
            } else {
                awardNotice.getPrintRequirement().setHierarchyInfoRequired("0");
            }
            if (((Boolean)htData.get(INDIRECT_COST)).booleanValue()) {
                awardNotice.getPrintRequirement().setIndirectCostRequired("1");
            } else {
                awardNotice.getPrintRequirement().setIndirectCostRequired("0");
            }
            if (((Boolean)htData.get(PAYEMENT)).booleanValue()) {
                awardNotice.getPrintRequirement().setPaymentRequired("1");
            } else {
                awardNotice.getPrintRequirement().setPaymentRequired("0");
            }
            if (((Boolean)htData.get(PROPOSAL_DUE)).booleanValue()) {
                awardNotice.getPrintRequirement().setProposalDueRequired("1");
            } else {
                awardNotice.getPrintRequirement().setProposalDueRequired("0");
            }
            if (((Boolean)htData.get(SCIENCE_CODE)).booleanValue()) {
                awardNotice.getPrintRequirement().setScienceCodeRequired("1");
            } else {
                awardNotice.getPrintRequirement().setScienceCodeRequired("0");
            }
            if (((Boolean)htData.get(SIGNATURE_REQUIRED)).booleanValue()) {
                awardNotice.getPrintRequirement().setSignatureRequired("1");
            } else {
                awardNotice.getPrintRequirement().setSignatureRequired("0");
            }
            if (((Boolean)htData.get(SPECIAL_REVIEW)).booleanValue()) {
                awardNotice.getPrintRequirement().setSpecialReviewRequired("1");
            } else {
                awardNotice.getPrintRequirement().setSpecialReviewRequired("0");
            }
            if (((Boolean)htData.get(SUBCONTRACT)).booleanValue()) {
                awardNotice.getPrintRequirement().setSubcontractRequired("1");
            } else {
                awardNotice.getPrintRequirement().setSubcontractRequired("0");
            }
            if (((Boolean)htData.get(TECH_REPORTING)).booleanValue()) {
                awardNotice.getPrintRequirement().setTechnicalReportingRequired("1");
            } else {
                awardNotice.getPrintRequirement().setTechnicalReportingRequired("0");
            }
            if (((Boolean)htData.get(TERMS)).booleanValue()) {
                awardNotice.getPrintRequirement().setTermsRequired("1");
            } else {
                awardNotice.getPrintRequirement().setTermsRequired("0");
            }
            if (((Boolean)htData.get(REPORTING)).booleanValue()) {
                awardNotice.getPrintRequirement().setReportingRequired("1");
            } else {
                awardNotice.getPrintRequirement().setReportingRequired("0");
            }
            //start case 2010
            if (((Boolean)htData.get(OTHER_DATA)).booleanValue()) {
                awardNotice.getPrintRequirement().setOtherDataRequired("1");
            } else {
                awardNotice.getPrintRequirement().setOtherDataRequired("0");
            }
            //end case 2010
            //Added for Case 3122 - Award Notice Enhancement -Start
            if (((Boolean)htData.get(FUNDING_SUMMARY)).booleanValue()) {
                  awardNotice.getPrintRequirement().setFundingSummaryRequired("1");
            } else {
                awardNotice.getPrintRequirement().setFundingSummaryRequired("0");
            }
           //Added for Case 3122 - Award Notice Enhancement -End

            Calendar currentDate = Calendar.getInstance();
            currentDate.setTime(new Date());
            awardNotice.getPrintRequirement().setCurrentDate(currentDate);

            // get award details
            AwardTxnBean awardTxnBean = new AwardTxnBean();
            AwardReportTxnBean awardReportTxnBean = new AwardReportTxnBean();
            String mitAwardNumber = awBean.getMitAwardNumber();
            AwardBean awardBean = awardTxnBean.getAwardDetails(mitAwardNumber,-1);
            //case 1438 begin
//            int accountNumber = awardReportTxnBean.getAccountForAward(mitAwardNumber);
            String accountNumber = awardReportTxnBean.getAccountForAward(mitAwardNumber);
            //case 1438 end

            CoeusVector cvInvestigators = awardTxnBean.getAwardInvestigators(mitAwardNumber);
            //From servlet

            int seqNumber = awardBean.getSequenceNumber();

            //Added for Case 3823 - Key Person Records Needed in Inst Proposal and Award  -start
            CoeusVector cvKeyPersons = awardTxnBean.getAwardKeyPersons(mitAwardNumber, seqNumber);
            // Case 3823 - end

            //checking whether the signature checkbox is required or not.
            //int isSignatureRequired = awardReportTxnBean.getSignatureCheck(mitAwardNumber, seqNumber);

            //= awardReportTxnBean.getAwardComments(mitAwardNumber);
            CoeusVector cvAwardComments = new CoeusVector();
            //Commented for Case 3122 - Award Notice Enhancement -Start
           //Commented hardcoded comment types for only 2 to 7
//            for (int idx=2;idx<7;idx++) {
//                CoeusVector cvComments = awardTxnBean.getAwardCommentsForCommentCode(mitAwardNumber, idx);
//                if (cvComments!=null && cvComments.size()>0 ) {
//                    cvAwardComments.add(cvComments.get(0));
//                }
//            }
            //Commented for Case 3122 - Award Notice Enhancement -End
            //Added for Case 3122 - Award Notice Enhancement -Start
            // Take all the Award Comment types from osp$comment where osp$comment.award_comment_screen_flag = 'Y' using procedure DW_GET_AWARD_COMMENT_TYPE
            AwardCommentsBean awardCommentsBean;
            AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
            CoeusVector cvCommentsTypes = awardLookUpDataTxnBean.getAwardCommentType();
            CommentTypeBean commentTypeBean;
            HashMap hmCommentTypewithDesc = new HashMap();
            if (cvCommentsTypes !=null && cvCommentsTypes.size() > 0){
                for( int index =0; index < cvCommentsTypes.size() ; index++){
                    commentTypeBean = (CommentTypeBean) cvCommentsTypes.get(index);
                    hmCommentTypewithDesc.put(new Integer(commentTypeBean.getCommentCode()), commentTypeBean.getDescription());
                    CoeusVector cvComments = awardTxnBean.getAwardCommentsForCommentCode(mitAwardNumber, commentTypeBean.getCommentCode());
                    if (cvComments!=null && cvComments.size()>0 ) {
                        cvAwardComments.add(cvComments.get(0));
                    }
                }
            }
            //Modified for Case 3122 - Award Notice Enhancement -End
            cvAwardComments.sort("commentCode",true);
            CommentDetailsType commentDetailsType;
            CommentType commentType;
            AwardType.AwardCommentsType awardCommentsType = new AwardTypeImpl.AwardCommentsTypeImpl();
            awardType.setAwardComments(awardCommentsType);
            for (int indx=0;indx<cvAwardComments.size();indx++) {
                awardCommentsBean = (AwardCommentsBean) cvAwardComments.get(indx);
                commentDetailsType = new CommentDetailsTypeImpl();
                commentDetailsType.setAwardNumber(mitAwardNumber);
                commentDetailsType.setCommentCode(awardCommentsBean.getCommentCode());
                commentDetailsType.setComments(awardCommentsBean.getComments());
                commentDetailsType.setPrintChecklist(awardCommentsBean.isCheckListPrintFlag());
                commentDetailsType.setSequenceNumber(seqNumber);
                commentType = new CommentTypeImpl();
                commentType.setCommentDetails(commentDetailsType);
                 //Added for Case 3122 - Award Notice Enhancement -Start
                String commentDesc = (String) hmCommentTypewithDesc.get(new Integer(awardCommentsBean.getCommentCode()));
                commentType.setDescription(commentDesc);
                //Added for Case 3122 - Award Notice Enhancement -End
                //Commented for Case 3122 - Award Notice Enhancement -Start
//                switch (awardCommentsBean.getCommentCode()) {
//                    case FISCAL_REPORT_COMMENT_CODE:
//                        commentType.setDescription(FISCAL_REPORT);
//                        break;
//                    case GENEREAL_COMMENT_CODE :
//                        commentType.setDescription(GENEREAL_COMMENT);
//                        break;
//                    case INTELLECTUAL_PROPERTY_COMMENT_CODE:
//                        commentType.setDescription(INTELLECTUAL_PROPERTY);
//                        break;
//                    case PROCUREMENT_COMMENT_CODE:
//                        commentType.setDescription(PROCUREMENT_COMMENT);
//                        break;
//                    case PROPERTY_COMMENT_CODE:
//                        commentType.setDescription(PROPERTY_COMMENT);
//                }
                 //Commented for Case 3122 - Award Notice Enhancement -End
                awardType.getAwardComments().getComment().add(commentType);
            }

            // AwardHeader
            AwardHeader awardHeader = new AwardHeaderImpl();
            //case 1438 begin
//            awardHeader.setAccountNumber(String.valueOf(accountNumber));
            awardHeader.setAccountNumber(accountNumber);
            //case 1438 end
            awardHeader.setAwardNumber(mitAwardNumber);
            awardHeader.setModificationNumber(awardBean.getModificationNumber());
            awardHeader.setNSFCode(awardBean.getNsfCode());
            awardHeader.setNSFDescription(awardBean.getNsfDescription());
            String principleInvestigator = null;
            String piAddress = null;
            PersonInfoTxnBean personInfoTxnBean = new PersonInfoTxnBean();
            for (int index=0;index<cvInvestigators.size();index++) {
                AwardInvestigatorsBean awardInvestigatorsBean = (AwardInvestigatorsBean)cvInvestigators.get(index);
                if (awardInvestigatorsBean.isPrincipalInvestigatorFlag()) {
                    principleInvestigator = awardInvestigatorsBean.getPersonName();
                    PersonInfoFormBean personInfoFormBean = personInfoTxnBean.getPersonInfo(awardInvestigatorsBean.getPersonId());
                    piAddress = personInfoFormBean.getOffLocation();
                    awardHeader.setPIName(principleInvestigator);
                    break;
                }
            }
            awardHeader.setSequenceNumber(seqNumber);
            awardHeader.setSponsorAwardNumber(awardBean.getSponsorAwardNumber());
            awardHeader.setSponsorCode(awardBean.getSponsorCode());
            awardHeader.setSponsorDescription(awardBean.getSponsorName());
            awardHeader.setStatusCode(String.valueOf(awardBean.getStatusCode()));
            awardHeader.setStatusDescription(awardBean.getStatusDescription());
            AwardHeaderBean awardHeaderBean = awardBean.getAwardHeaderBean();

            //AmountInfoType

            //getMoneyAndEndDatesTree
            AwardAmountInfoBean awardAmountInfoBean;
            awardAmountInfoBean= new AwardAmountInfoBean();
            awardAmountInfoBean.setMitAwardNumber(mitAwardNumber);
            // Modified for Case 4498 - NOA Print Issues. If value is null, default value -1 is changed to 0.0
            awardAmountInfoBean = awardTxnBean.getMoneyAndEndDates(awardAmountInfoBean, 0.0);

            AmountInfoType amountInfoType = new AmountInfoTypeImpl();
            amountInfoType.setAmountSequenceNumber(seqNumber);
            BigDecimal bdecAmtObligatedToDate = new BigDecimal(awardAmountInfoBean.getAmountObligatedToDate());
            amountInfoType.setAmtObligatedToDate(bdecAmtObligatedToDate.setScale(2,BigDecimal.ROUND_HALF_DOWN));
            BigDecimal bdecAnticipatedChange = new BigDecimal(awardAmountInfoBean.getAnticipatedChange());
            amountInfoType.setAnticipatedChange(bdecAnticipatedChange.setScale(2,BigDecimal.ROUND_HALF_DOWN));
            BigDecimal bdecAnticipatedDistributableAmt = new BigDecimal(awardAmountInfoBean.getAnticipatedDistributableAmount());
            amountInfoType.setAnticipatedDistributableAmt(bdecAnticipatedDistributableAmt.setScale(2,BigDecimal.ROUND_HALF_DOWN));
            BigDecimal bdecAnticipatedTotalAmt = new BigDecimal(awardAmountInfoBean.getAnticipatedTotalAmount());
            amountInfoType.setAnticipatedTotalAmt(bdecAnticipatedTotalAmt.setScale(2,BigDecimal.ROUND_HALF_DOWN));
            amountInfoType.setAwardNumber(mitAwardNumber);
            Calendar currentFundEffectiveDate =null;
            if (awardAmountInfoBean.getCurrentFundEffectiveDate()!=null) {
                currentFundEffectiveDate = Calendar.getInstance();
                currentFundEffectiveDate.setTime(awardAmountInfoBean.getCurrentFundEffectiveDate());
                amountInfoType.setCurrentFundEffectiveDate(currentFundEffectiveDate);
            }
            amountInfoType.setEOMProcess(true);
            amountInfoType.setEntryType(awardAmountInfoBean.getEntryType());
            Calendar finalExpirationDate= null;
            if (awardAmountInfoBean.getFinalExpirationDate()!=null) {
                finalExpirationDate = Calendar.getInstance();
                finalExpirationDate.setTime(awardAmountInfoBean.getFinalExpirationDate());
                amountInfoType.setFinalExpirationDate(finalExpirationDate);
            }
            BigDecimal bdecObligatedChange = new BigDecimal(awardAmountInfoBean.getObligatedChange());
            amountInfoType.setObligatedChange(bdecObligatedChange.setScale(2,BigDecimal.ROUND_HALF_DOWN));
            BigDecimal bdecObligatedDistributableAmt = new BigDecimal(awardAmountInfoBean.getObliDistributableAmount());
            amountInfoType.setObligatedDistributableAmt(bdecObligatedDistributableAmt.setScale(2,BigDecimal.ROUND_HALF_DOWN));
            Calendar obligationExpirationDate = null;
            if (awardAmountInfoBean.getObligationExpirationDate()!=null) {
                obligationExpirationDate = Calendar.getInstance();
                obligationExpirationDate.setTime(awardAmountInfoBean.getObligationExpirationDate());
                amountInfoType.setObligationExpirationDate(obligationExpirationDate);
            }
            amountInfoType.setSequenceNumber(seqNumber);
            amountInfoType.setTransactionId(awardAmountInfoBean.getTransactionId());

            //Added for Case 4156 - direct/indirect amounts don't appear on NOA -Start
            CoeusFunctions coeusFunctions = new CoeusFunctions();
            String directIndirectParamValue = "0";
            directIndirectParamValue = coeusFunctions.getParameterValue("ENABLE_AWD_ANT_OBL_DIRECT_INDIRECT_COST");

                BigDecimal bdecObligatedChangeDirect = new BigDecimal(awardAmountInfoBean.getDirectObligatedChange());
                amountInfoType.setObligatedChangeDirect(bdecObligatedChangeDirect.setScale(2,BigDecimal.ROUND_HALF_DOWN));

                BigDecimal bdecObligatedChangeIndirect = new BigDecimal(awardAmountInfoBean.getIndirectObligatedChange());
                amountInfoType.setObligatedChangeIndirect(bdecObligatedChangeIndirect.setScale(2,BigDecimal.ROUND_HALF_DOWN));

                BigDecimal bdecAnticipatedChangeDirect = new BigDecimal(awardAmountInfoBean.getDirectAnticipatedChange());
                amountInfoType.setAnticipatedChangeDirect(bdecAnticipatedChangeDirect.setScale(2,BigDecimal.ROUND_HALF_DOWN));

                BigDecimal bdecAnticipatedChangeIndirect = new BigDecimal(awardAmountInfoBean.getIndirectAnticipatedChange());
                amountInfoType.setAnticipatedChangeIndirect(bdecAnticipatedChangeIndirect.setScale(2,BigDecimal.ROUND_HALF_DOWN));

                BigDecimal bdecAnticipatedTotalDirect = new BigDecimal(awardAmountInfoBean.getDirectAnticipatedTotal());
                amountInfoType.setAnticipatedTotalDirect(bdecAnticipatedTotalDirect.setScale(2,BigDecimal.ROUND_HALF_DOWN));

                BigDecimal bdecAnticipatedTotalIndirect = new BigDecimal(awardAmountInfoBean.getIndirectAnticipatedTotal());
                amountInfoType.setAnticipatedTotalIndirect(bdecAnticipatedTotalIndirect.setScale(2,BigDecimal.ROUND_HALF_DOWN));

                BigDecimal bdecObligatedTotalDirect = new BigDecimal(awardAmountInfoBean.getDirectObligatedTotal());
                amountInfoType.setObligatedTotalDirect(bdecObligatedTotalDirect.setScale(2,BigDecimal.ROUND_HALF_DOWN));

                BigDecimal bdecObligatedTotalIndirect = new BigDecimal(awardAmountInfoBean.getIndirectObligatedTotal());
                amountInfoType.setObligatedTotalIndirect(bdecObligatedTotalIndirect.setScale(2,BigDecimal.ROUND_HALF_DOWN));

                amountInfoType.setEnableAwdAntOblDirectIndirectCost(directIndirectParamValue);

            //Added for Case 4156 - direct/indirect amounts don't appear on NOA -End

            AwardType.AwardAmountInfoType awardAmountInfoType = new AwardTypeImpl.AwardAmountInfoTypeImpl();
            awardType.setAwardAmountInfo(awardAmountInfoType);
            awardType.getAwardAmountInfo().getAmountInfo().add(amountInfoType);

            // Added for Case 4416 - Change NOA and delta stylesheets to include data items related to transaction type - Start

            AwardAmountTransactionBean awardAmountTransactionBean = awardAmountInfoBean.getAwardAmountTransaction();
            AwardTransactionType awardTransactionType = new AwardTransactionTypeImpl();
            awardTransactionType.setAwardNumber(awardAmountTransactionBean.getMitAwardNumber());
            awardTransactionType.setTransactionTypeCode(awardAmountTransactionBean.getTransactionTypeCode());
            awardTransactionType.setTransactionTypeDesc(awardAmountTransactionBean.getTransactionTypeDescription());
            awardTransactionType.setComments(awardAmountTransactionBean.getComments());
            Calendar calNoticeDate =null;
            if (awardAmountTransactionBean.getNoticeDate()!=null) {
                calNoticeDate = Calendar.getInstance();
                calNoticeDate.setTime(awardAmountTransactionBean.getNoticeDate());
                awardTransactionType.setNoticeDate(calNoticeDate);
            }
            AwardType.AwardTransactionInfoType awardTransactionInfoType = new AwardTypeImpl.AwardTransactionInfoTypeImpl();
            awardType.setAwardTransactionInfo(awardTransactionInfoType);
            awardType.getAwardTransactionInfo().getTransactionInfo().add(awardTransactionType);

            // Added for Case 4416 - Change NOA and delta stylesheets to include data items related to transaction type - End

            //Added for Case 3122 - Award Notice Enhancement -Start
            //GetAllMoneyAndEndDates for all sequences
            awardAmountInfoBean= new AwardAmountInfoBean();
            awardAmountInfoBean.setMitAwardNumber(mitAwardNumber);
            //Modified for Case 3122 - Award Notice Enhancement -Start
//            CoeusVector cvMoneyAndEndDates = awardTxnBean.getAllMoneyAndEndDates(awardAmountInfoBean, 0);
            CoeusVector cvMoneyAndEndDates = awardTxnBean.getAwardAmountHistory(mitAwardNumber);
            //Modified for Case 3122 - Award Notice Enhancement -End
            AwardType.AwardFundingSummaryType awardFundingSummaryType = new AwardTypeImpl.AwardFundingSummaryTypeImpl();
            awardType.setAwardFundingSummary(awardFundingSummaryType);
            boolean totalPeriodFlag = true;
            boolean accountNumberFlag = true;
            if(cvMoneyAndEndDates !=null){
                for(int index=0; index < cvMoneyAndEndDates.size(); index++){
                    awardAmountInfoBean = (AwardAmountInfoBean) cvMoneyAndEndDates.get(index);
                    amountInfoType = new AmountInfoTypeImpl();
                    //Set Award Effective Date to Total Start Date
                    if (totalPeriodFlag && awardBean !=null && awardBean.getAwardEffectiveDate() !=null){
                        Calendar effectiveDate = Calendar.getInstance();
                        effectiveDate.setTime(awardBean.getAwardEffectiveDate());
                        amountInfoType.setTotalStartDate(effectiveDate);
                    }
                    //Set Total Project Period End Date:  Money and End date tab,Final Expiration date.
                    if (totalPeriodFlag && finalExpirationDate!=null) {
//                        Calendar expirationDate = Calendar.getInstance();
//                        expirationDate.setTime(awardAmountInfoBean.getFinalExpirationDate());
                        amountInfoType.setTotalEndDate(finalExpirationDate);
                    }
                    totalPeriodFlag =false;
                    // Modified for case 3122 - Change the variable name to ObligatedChange - Start
                    //Set Total Amount:  ObligatedAmountChange
                    BigDecimal bdecAmtOblToChange = new BigDecimal(awardAmountInfoBean.getObligatedChange());
                    amountInfoType.setObligatedChange(bdecAmtOblToChange.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                     // Modified for case 3122 - End
                    //Set Sequence Number
                    amountInfoType.setSequenceNumber(awardAmountInfoBean.getSequenceNumber());
//                    //Set Account Number to awardNumber field
                    if( accountNumberFlag && accountNumber !=null){
                        amountInfoType.setAwardNumber(accountNumber);
                    }
                    accountNumberFlag = false;
                    //Set StartDate: CurrentFundEffectiveDate
                    Calendar currentFundEffDate =null;
                    if (awardAmountInfoBean.getCurrentFundEffectiveDate()!=null) {
                        currentFundEffDate = Calendar.getInstance();
                        currentFundEffDate.setTime(awardAmountInfoBean.getCurrentFundEffectiveDate());
                        amountInfoType.setCurrentFundEffectiveDate(currentFundEffDate);
                    }
                    //Set EndDate: ObligationExpirationDate
                    Calendar obligationExpDate = null;
                    if (awardAmountInfoBean.getObligationExpirationDate()!=null) {
                        obligationExpDate = Calendar.getInstance();
                        obligationExpDate.setTime(awardAmountInfoBean.getObligationExpirationDate());
                        amountInfoType.setObligationExpirationDate(obligationExpDate);
                    }

                    // Modified for case 3122 - Start
                    // Check if index is latest award Sequence and Set the Obligated Distributable amount, Anticipated Total Amount and Amount Obligated to Date data
                    if(index == cvMoneyAndEndDates.size()-1 ){
                        // Set Amount: Total Distributable Amount
                        BigDecimal bdecOblDistAmt = new BigDecimal(awardAmountInfoBean.getObliDistributableAmount());
                        amountInfoType.setObligatedDistributableAmt(bdecOblDistAmt.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                        //Set Remaining Anticipated Amount: total anticipated – total_obligated Amount
                        BigDecimal bdecAnticipatedAmt = new BigDecimal(awardAmountInfoBean.getAnticipatedTotalAmount());
                        amountInfoType.setAnticipatedTotalAmt(bdecAnticipatedAmt.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                        //Set Amount Obligated to date:
                        BigDecimal bdecAmtOblToDate = new BigDecimal(awardAmountInfoBean.getAmountObligatedToDate());
                        amountInfoType.setAmtObligatedToDate(bdecAmtOblToDate.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                    }// Modified for case 3122 - End

                    awardType.getAwardFundingSummary().getFundingSummary().add(amountInfoType);
                }
            }
            //Added for Case 3122 - Award Notice Enhancement -End

            //AwardHierarchy
            AwardDetailsBean awardDetailsBean = awardBean.getAwardDetailsBean();

            AwardHierarchyType awardHierarchyType = new AwardHierarchyTypeImpl();
            awardHierarchyType.setAwardNumber(mitAwardNumber);
            //awardAmountInfoBean.getParentMitAwardNumber());
            //awardHierarchyType.setRootAwardNumber( awardAmountInfoBean.getRootMitAwardNumber());

            //AwardType.ChildAwardDetailsType
            CoeusVector cvChildDetails = awardTxnBean.getPrincipalInvestigator(mitAwardNumber,-1);
            ChildAwardType childAwardType;
            AwardType.ChildAwardDetailsType childAwardDetailsType = new AwardTypeImpl.ChildAwardDetailsTypeImpl();
            awardType.setChildAwardDetails(childAwardDetailsType);
            for (int index=0;index<cvChildDetails.size();index++) {
                AwardInvestPersonNameBean childBean = (AwardInvestPersonNameBean)cvChildDetails.get(index);
                childAwardType = new ChildAwardTypeImpl();
                childAwardType.setAccountNumber(childBean.getAccountNumber());
                double d = childBean.getAmountObligatedToDate();

                BigDecimal bdecChildAmtObligatedToDate = new BigDecimal(d);
                childAwardType.setAmtObligatedToDate(bdecChildAmtObligatedToDate.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                BigDecimal bdecChildAnticipatedTotalAmt = new BigDecimal(childBean.getAnticipatedTotalAmount());
                childAwardType.setAnticipatedTotalAmt(bdecChildAnticipatedTotalAmt.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                AwardHierarchyType hierarchyType = new AwardHierarchyTypeImpl();
                hierarchyType.setAwardNumber(childBean.getMitAwardNumber());
                hierarchyType.setParentAwardNumber(childBean.getParentMitAwardNumber());
                hierarchyType.setRootAwardNumber(childBean.getRootMitAwardNumber());
                childAwardType.setAwardHierarchy(hierarchyType);
                if (childBean.getCurrentFundEffectiveDate() !=null) {
                    Calendar currentFEDate = Calendar.getInstance();
                    currentFEDate.setTime(childBean.getCurrentFundEffectiveDate());
                    childAwardType.setCurrentFundEffectiveDate(currentFEDate);
                }
                if (childBean.getFinalExpirationDate() != null) {
                    Calendar finalExpDate = Calendar.getInstance();
                    finalExpDate.setTime(childBean.getFinalExpirationDate());
                    childAwardType.setFinalExpirationDate(finalExpDate);
                }
                if (childBean.getObligationExpirationDate() != null) {
                    Calendar oblExpDate = Calendar.getInstance();
                    oblExpDate.setTime(childBean.getObligationExpirationDate());
                    childAwardType.setObligationExpirationDate(oblExpDate);
                }
                childAwardType.setPIName(childBean.getPersonName());
                awardType.getChildAwardDetails().getChildAward().add(childAwardType);
            }
            //AwardHeaderType
            AwardHeaderType awardHeaderType = new AwardHeaderTypeImpl();
            //case 1438 begin
//            awardHeaderType.setAccountNumber(String.valueOf(accountNumber));
            awardHeaderType.setAccountNumber(accountNumber);
            //case 1438 end
            awardHeaderType.setAwardNumber(mitAwardNumber);
            awardHeaderType.setModificationNumber(awardBean.getModificationNumber());
            awardHeaderType.setNSFCode(awardBean.getNsfCode());
            awardHeaderType.setNSFDescription(awardBean.getNsfDescription());

            for (int index=0;index<cvInvestigators.size();index++) {
                AwardInvestigatorsBean awardInvestigatorsBean = (AwardInvestigatorsBean)cvInvestigators.get(index);
                if (awardInvestigatorsBean.isPrincipalInvestigatorFlag()) {
                    awardHeaderType.setPIName(awardInvestigatorsBean.getPersonName());
                    break;
                }
            }
            awardHeaderType.setSequenceNumber(seqNumber);
            awardHeaderType.setSponsorAwardNumber(awardBean.getSponsorAwardNumber());
            awardHeaderType.setSponsorDescription(awardBean.getSponsorName());
            awardHeaderType.setStatusDescription(awardBean.getStatusDescription());
            awardHeaderType.setStatusCode(""+awardBean.getStatusCode());
            awardHeaderType.setTitle(awardHeaderBean.getTitle());
            AwardType.AwardDetailsType awardDetailsType = new AwardTypeImpl.AwardDetailsTypeImpl();

            awardType.setAwardDetails(awardDetailsType);
            awardDetailsType.setAwardHeader(awardHeaderType);
            //case 1438 begin
//            awardType.getAwardDetails().getAwardHeader().setAccountNumber(String.valueOf(accountNumber));
            awardType.getAwardDetails().getAwardHeader().setAccountNumber(accountNumber);
            //case 1438 end
            awardType.getAwardDetails().getAwardHeader().setAwardNumber(mitAwardNumber);
            awardType.getAwardDetails().getAwardHeader().setModificationNumber(awardBean.getModificationNumber());
            awardType.getAwardDetails().getAwardHeader().setNSFCode(awardBean.getNsfCode());
            awardType.getAwardDetails().getAwardHeader().setNSFDescription(awardBean.getNsfDescription());

            for (int index=0;index<cvInvestigators.size();index++) {
                AwardInvestigatorsBean awardInvestigatorsBean = (AwardInvestigatorsBean)cvInvestigators.get(index);
                if (awardInvestigatorsBean.isPrincipalInvestigatorFlag()) {
                    awardType.getAwardDetails().getAwardHeader().setPIName(awardInvestigatorsBean.getPersonName());
                    break;
                }
            }

            awardType.getAwardDetails().getAwardHeader().setSequenceNumber(seqNumber);
            awardType.getAwardDetails().getAwardHeader().setSponsorAwardNumber(awardBean.getSponsorAwardNumber());
            awardType.getAwardDetails().getAwardHeader().setSponsorCode(awardBean.getSponsorCode());
            awardType.getAwardDetails().getAwardHeader().setSponsorDescription(awardBean.getSponsorName());
            awardType.getAwardDetails().getAwardHeader().setStatusCode(String.valueOf(awardBean.getStatusCode()));
            awardType.getAwardDetails().getAwardHeader().setStatusDescription(awardBean.getStatusDescription());
            //AwardHeaderBean awardHeaderBean = awardBean.getAwardHeaderBean();
            awardType.getAwardDetails().setAwardHeader(awardHeaderType);
            awardType.getAwardDetails().getAwardHeader().setTitle(awardHeaderBean.getTitle());

            //OtherHeaderDetailsType

            AwardType.AwardDetailsType.OtherHeaderDetailsType otherHeaderDetailsType =
            new AwardTypeImpl.AwardDetailsTypeImpl.OtherHeaderDetailsTypeImpl();
            otherHeaderDetailsType.setAccountTypeCode(""+awardHeaderBean.getAccountTypeCode());
            otherHeaderDetailsType.setAccountTypeDesc(awardHeaderBean.getAccountTypeDescription());
            otherHeaderDetailsType.setActivityTypeCode(awardHeaderBean.getActivityTypeCode());
            otherHeaderDetailsType.setActivityTypeDesc(awardHeaderBean.getActivityTypeDescription());
            otherHeaderDetailsType.setAwardTypeCode(awardHeaderBean.getAwardTypeCode());
            otherHeaderDetailsType.setAwardTypeDesc(awardHeaderBean.getAwardTypeDescription());
            otherHeaderDetailsType.setBasisPaymentCode(String.valueOf(awardHeaderBean.getBasisOfPaymentCode()));
            // Added for Case 3122 - Award Notice Changes - Start
            UserMaintDataTxnBean userMainDataTxnBean = new UserMaintDataTxnBean();
             otherHeaderDetailsType.setUpdateUser(userMainDataTxnBean.getUserName(awardBean.getUpdateUser()));
             Calendar lastUpdatedDate = Calendar.getInstance();
              lastUpdatedDate.setTime(awardBean.getUpdateTimestamp());
            otherHeaderDetailsType.setLastUpdate(lastUpdatedDate);
            // Added for Case 3122 - Award Notice Changes - End

            DepartmentPersonTxnBean departmentPersonTxnBean =  new DepartmentPersonTxnBean();
            Vector basisPayementDescVec = departmentPersonTxnBean.getArgumentCodeDescription("BASIS OF PAYMENT");
            CoeusVector cvBasiPayementDesc=new CoeusVector();
            cvBasiPayementDesc.addAll(basisPayementDescVec);
            Equals eqBasis = new Equals("code",""+awardHeaderBean.getBasisOfPaymentCode());
            CoeusVector cvFilteredBasisPay = cvBasiPayementDesc.filter(eqBasis);
            if (cvFilteredBasisPay!=null && cvFilteredBasisPay.size()>0) {
                ComboBoxBean basisPaymemtBean= (ComboBoxBean)cvFilteredBasisPay.get(0);
                otherHeaderDetailsType.setBasisPaymentDesc(basisPaymemtBean.getDescription());
            }
            otherHeaderDetailsType.setCFDANumber(awardHeaderBean.getCfdaNumber());
            otherHeaderDetailsType.setCompetingRenewalCode(String.valueOf(awardHeaderBean.getCompetingRenewalPrpslDue()));
            otherHeaderDetailsType.setDFAFSNumber(awardHeaderBean.getDfafsNumber());
            otherHeaderDetailsType.setFinalInvoiceDue(awardHeaderBean.getFinalInvoiceDue()==null?0:awardHeaderBean.getFinalInvoiceDue().intValue());
            otherHeaderDetailsType.setInvoiceCopies(awardHeaderBean.getInvoiceNoOfCopies());

            CoeusVector cvInvoiceInstrnComments = awardTxnBean.getAwardCommentsForCommentCode(mitAwardNumber, 1);
            AwardCommentsBean invoiceInstrnCommentsBean=null;
            if (cvInvoiceInstrnComments!=null && cvInvoiceInstrnComments.size()>0) {
                invoiceInstrnCommentsBean = (AwardCommentsBean)cvInvoiceInstrnComments.get(0);
                otherHeaderDetailsType.setInvoiceInstructions(invoiceInstrnCommentsBean.getComments());
            }


            otherHeaderDetailsType.setNonCompetingContCode(""+awardHeaderBean.getNonCompetingContPrpslDue());
//            AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
            CoeusVector cvAllCompeting = awardLookUpDataTxnBean.getFrequency();

            Equals eqNoncompeting = new Equals("code",""+awardHeaderBean.getNonCompetingContPrpslDue());
            CoeusVector cvNonCompeting = cvAllCompeting.filter(eqNoncompeting);
            if (cvNonCompeting!=null && cvNonCompeting.size()>0) {
                ComboBoxBean nonCompetingBean = (ComboBoxBean)cvNonCompeting.get(0);
                otherHeaderDetailsType.setNonCompetingContDesc(nonCompetingBean.getDescription());
            }

            Equals eqCompeting = new Equals("code",""+awardHeaderBean.getCompetingRenewalPrpslDue());
            CoeusVector cvCompeting = cvAllCompeting.filter(eqCompeting);
            if (cvCompeting!=null && cvCompeting.size()>0) {
                ComboBoxBean competingBean = (ComboBoxBean)cvCompeting.get(0);
                otherHeaderDetailsType.setCompetingRenewalDesc(competingBean.getDescription());
            }
            otherHeaderDetailsType.setPaymentFreqCode(""+awardHeaderBean.getPaymentInvoiceFreqCode());

            Vector freqDescVec = departmentPersonTxnBean.getArgumentCodeDescription("FREQUENCY");
            CoeusVector cvFreqDesc=new CoeusVector();
            cvFreqDesc.addAll(freqDescVec);
            Equals eqFreq = new Equals("code",""+awardHeaderBean.getPaymentInvoiceFreqCode());
            CoeusVector cvFilteredFreqDesc = cvFreqDesc.filter(eqFreq);
            if (cvFilteredFreqDesc!=null && cvFilteredFreqDesc.size()>0) {
                ComboBoxBean freqDescBean= (ComboBoxBean)cvFilteredFreqDesc.get(0);
                otherHeaderDetailsType.setPaymentFreqDesc(freqDescBean.getDescription());
            }
            otherHeaderDetailsType.setPaymentMethodCode(""+awardHeaderBean.getMethodOfPaymentCode());
            Vector paymentMethodDescVec = departmentPersonTxnBean.getArgumentCodeDescription("METHOD OF PAYMENT");
            CoeusVector cvPaymentMethodDesc=new CoeusVector();
            cvPaymentMethodDesc.addAll(paymentMethodDescVec);
            Equals eqMethod = new Equals("code",""+awardHeaderBean.getMethodOfPaymentCode());
            CoeusVector cvFilteredPaymentMethod = cvPaymentMethodDesc.filter(eqMethod);
            if (cvFilteredPaymentMethod!=null && cvFilteredPaymentMethod.size()>0) {
                ComboBoxBean paymemtMethodBean= (ComboBoxBean)cvFilteredPaymentMethod.get(0);
                otherHeaderDetailsType.setPaymentMethodDesc(paymemtMethodBean.getDescription());
            }
            BigDecimal bdecPreAwardAuthorizedAmt = new BigDecimal(awardHeaderBean.getPreAwardAuthorizedAmount()==null?0:awardHeaderBean.getPreAwardAuthorizedAmount().doubleValue());
            otherHeaderDetailsType.setPreAwardAuthorizedAmt(bdecPreAwardAuthorizedAmt.setScale(2,BigDecimal.ROUND_HALF_DOWN));
            if (awardHeaderBean.getPreAwardEffectiveDate()!=null) {
                Calendar preAwardEffectiveDate = Calendar.getInstance();
                preAwardEffectiveDate.setTime(awardHeaderBean.getPreAwardEffectiveDate());
                otherHeaderDetailsType.setPreAwardEffectiveDate(preAwardEffectiveDate);
            }

            otherHeaderDetailsType.setPrimeSponsorCode(awardHeaderBean.getPrimeSponsorCode());
            //String primeSponsorName = awardTxnBean.getSponsorName(awardHeaderBean.getPrimeSponsorCode());

            RolodexMaintenanceDataTxnBean rolodexMaintenanceDataTxnBean = new RolodexMaintenanceDataTxnBean();
            String primeSponsorName = rolodexMaintenanceDataTxnBean.getSponsorName(awardHeaderBean.getPrimeSponsorCode());
            if (primeSponsorName!=null)  {
                otherHeaderDetailsType.setPrimeSponsorDescription(primeSponsorName);
            }
            otherHeaderDetailsType.setProcurementPriorityCode(awardHeaderBean.getProcurementPriorityCode());
            otherHeaderDetailsType.setProposalNumber(awardHeaderBean.getProposalNumber());
            otherHeaderDetailsType.setRootAccountNumber(awardReportTxnBean.getRootAccountForAward(mitAwardNumber));
            BigDecimal bdecSpecialEBRateOffCampus = new BigDecimal(awardHeaderBean.getSpecialEBRateOffCampus()==null?0.0:awardHeaderBean.getSpecialEBRateOffCampus().doubleValue());
            otherHeaderDetailsType.setSpecialEBRateOffCampus(bdecSpecialEBRateOffCampus.setScale(2,BigDecimal.ROUND_HALF_DOWN));
            BigDecimal bdecSpecialEBRateOnCampus = new BigDecimal(awardHeaderBean.getSpecialEBRateOnCampus()==null?0.0:awardHeaderBean.getSpecialEBRateOnCampus().doubleValue());
            otherHeaderDetailsType.setSpecialEBRateOnCampus(bdecSpecialEBRateOnCampus.setScale(2,BigDecimal.ROUND_HALF_DOWN));

            CoeusVector cvSpecialRate = awardTxnBean.getAwardCommentsForCommentCode(mitAwardNumber, 7);
            if (cvSpecialRate !=null && cvSpecialRate.size()>0) {
                AwardCommentsBean specialRateDescBean = (AwardCommentsBean)cvSpecialRate.get(0);
                otherHeaderDetailsType.setSpecialRateComments(specialRateDescBean.getComments());
            }

            CoeusVector cvSRComments = awardTxnBean.getAwardCommentsForCommentCode(mitAwardNumber, SPECIAL_RATE_COMMENT);
            if (cvSRComments!=null && cvSRComments.size()>0) {
                AwardCommentsBean specialRateCommentBean = (AwardCommentsBean)cvSRComments.get(0);
                otherHeaderDetailsType.setSpecialRateComments(specialRateCommentBean.getComments());
            }
            otherHeaderDetailsType.setSubPlan(awardHeaderBean.getSubPlanFlag());

            //set fellowshipAdminName

            otherHeaderDetailsType.setFellowshipAdminName(awardReportTxnBean.getParameterValue("FELLOWSHIP_OSP_ADMIN"));

            awardType.getAwardDetails().setOtherHeaderDetails(otherHeaderDetailsType);

            //AwardDetails

            awardDetailsType.setApprvdEquipmentIndicator(awardBean.getApprvdEquipmentIndicator());
            awardDetailsType.setApprvdForeginTripIndicator(awardBean.getApprvdForeignTripIndicator());
            awardDetailsType.setApprvdSubcontractIndicator(awardBean.getApprvdSubcontractIndicator());
            awardDetailsType.setAwardHeader(awardHeaderType);
            awardDetailsType.setCostSharingIndicator(awardBean.getCostSharingIndicator());
            if (awardDetailsBean.getAwardEffectiveDate()!=null) {
                Calendar effectiveDate = Calendar.getInstance();
                effectiveDate.setTime(awardDetailsBean.getAwardEffectiveDate());
                awardDetailsType.setEffectiveDate(effectiveDate);
            }
            //case 2431 begin
            if (awardDetailsBean.getBeginDate()!=null) {
                Calendar beginDate = Calendar.getInstance();
                beginDate.setTime(awardDetailsBean.getBeginDate());
                awardDetailsType.setBeginDate(beginDate);
            }
            //case 2431 end
            if (awardDetailsBean.getAwardExecutionDate()!=null) {
                Calendar executionDate = Calendar.getInstance();
                executionDate.setTime(awardDetailsBean.getAwardExecutionDate());
                awardDetailsType.setExecutionDate(executionDate);
            }

            awardDetailsType.setIDCIndicator(awardBean.getIdcIndicator());
            awardDetailsType.setOtherHeaderDetails(otherHeaderDetailsType);
            awardDetailsType.setPaymentScheduleIndicator(awardBean.getPaymentScheduleIndicator());
            awardDetailsType.setScienceCodeIndicator(awardBean.getScienceCodeIndicator());
            awardDetailsType.setSpecialReviewIndicator(awardBean.getSpecialReviewIndicator());
            awardDetailsType.setTransferSponsorIndicator(awardBean.getTransferSponsorIndicator());


            //InvestigatorType
            InvestigatorType investigatorType;
            AwardType.AwardInvestigatorsType awardInvestigatorsType = new AwardTypeImpl.AwardInvestigatorsTypeImpl();
            awardType.setAwardInvestigators(awardInvestigatorsType);

            for (int indx = 0;indx<cvInvestigators.size();indx++) {
                AwardInvestigatorsBean awardInvestigatorsBean = (AwardInvestigatorsBean) cvInvestigators.get(indx);
                investigatorType = new InvestigatorTypeImpl();
                investigatorType.setAwardNumber(mitAwardNumber);
                investigatorType.setCOIFlag(true);
                investigatorType.setFEDRDEBRFlag(awardInvestigatorsBean.isFedrDebrFlag());
                investigatorType.setFEDRDELQFlag(awardInvestigatorsBean.isFedrDelqFlag());
                investigatorType.setFaculty(awardInvestigatorsBean.isFacultyFlag());
                investigatorType.setNonEmployee(awardInvestigatorsBean.isNonMITPersonFlag());
                BigDecimal bdecPercentEffort = new BigDecimal(awardInvestigatorsBean.getPercentageEffort());
                investigatorType.setPercentEffort(bdecPercentEffort.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                //awardTxnBean.getPrincipalInvestigator(mitAwardNumber);
                //awardTxnBean.getPrincipalInvestigator(mitAwardNumber);
                PersonInfoFormBean personInfoFormBean = personInfoTxnBean.getPersonInfo(awardInvestigatorsBean.getPersonId());
                investigatorType.setPersonAddress(personInfoFormBean.getOffLocation());
                investigatorType.setPersonId(awardInvestigatorsBean.getPersonId());
                investigatorType.setPersonName(awardInvestigatorsBean.getPersonName());
                investigatorType.setPrincipalInvestigator(awardInvestigatorsBean.isPrincipalInvestigatorFlag());
                investigatorType.setSequenceNumber(awardInvestigatorsBean.getSequenceNumber());
                awardType.getAwardInvestigators().getInvestigator().add(investigatorType);

                CoeusVector cvUnits = awardTxnBean.getAwardUnits(mitAwardNumber, awardInvestigatorsBean.getPersonId());
                AwardUnitBean awardUnitBean;
                InvestigatorUnitsType investigatorUnitsType;
                InvestigatorUnitsType.UnitAdministratorType unitAdministratorType;
                UnitDataTxnBean unitDataTxnBean = new UnitDataTxnBean();
                for (int idx=0;idx < cvUnits.size();idx++) {
                    awardUnitBean = (AwardUnitBean)cvUnits.get(idx);
                    investigatorUnitsType = new InvestigatorUnitsTypeImpl();

                    investigatorUnitsType.setAwardNumber(mitAwardNumber);
                    if (awardUnitBean.isLeadUnitFlag()) {
                        unitNumber = awardUnitBean.getUnitNumber();
                        //for Signature
                        //coeusdev-923 start
                        //new condition will be Account Type is “Draper Fellowship” OR Award Type is “Fellowship”
                        //OR Activity Type is “Fellowship- Pre-Doctoral” OR “Fellowship – Post-Doctoral”
                        //then set OspAdminName to FELLOWSHIP_OSP_ADMIN (contract administrator)
                         if (awardHeaderBean.getAccountTypeCode() != 3 && awardHeaderBean.getAwardTypeCode() != 7
                                && (awardHeaderBean.getActivityTypeCode()!= 3 && awardHeaderBean.getActivityTypeCode()!= 7)) {
                        //if (awardHeaderBean.getActivityTypeCode()!= 3 && awardHeaderBean.getActivityTypeCode()!= 7){
                        //coeusdev-923 end
                            String OspAdminName = awardUnitBean.getOspAdministratorName();
                           //case 1916 start
                            if (OspAdminName != null){
                                String lastName = null;
                                String firstName = null;
                                int pos;
                                pos = OspAdminName.indexOf(",");
                                if (!(pos>0)) pos = OspAdminName.indexOf(" ");
                                if (pos >= 0 ){
                                    lastName = OspAdminName.substring(0,pos).trim();
                                    firstName = OspAdminName.substring(pos+1).trim();
                                }

                                if (lastName!=null && firstName != null){
                                    OspAdminName = firstName + " " + lastName;
                                }else if (lastName!=null){
                                    OspAdminName = firstName ;
                                }
                                else if (firstName!=null){
                                    OspAdminName = lastName ;
                                }
//                                if (firstName != null){
//                                    pos = firstName.indexOf(" ");
//                                    if (pos >= 0 ){
//                                        middleNmae = " "+ firstName.substring(pos+1).trim()+ " ";
//                                        firstName = firstName.substring(0,pos).trim();
//                                    }else{
//                                        middleNmae = " ";
//                                    }
//                                }
//                                if (lastName!=null && firstName!= null && middleNmae!= null){
//                                    OspAdminName = firstName + middleNmae + lastName;
//                                }
                                //case 1916 end
                            }

                            otherHeaderDetailsType.setFellowshipAdminName(OspAdminName);
                            awardType.getAwardDetails().setOtherHeaderDetails(otherHeaderDetailsType);
                        }
                    }
                    investigatorUnitsType.setLeadUnit(awardUnitBean.isLeadUnitFlag());
                    investigatorUnitsType.setOSPAdminName(awardUnitBean.getOspAdministratorName());
                    investigatorUnitsType.setPersonId(awardUnitBean.getPersonId());
                    investigatorUnitsType.setSequenceNumber(awardUnitBean.getSequenceNumber());
                    investigatorUnitsType.setUnitName(awardUnitBean.getUnitName());
                    investigatorUnitsType.setUnitNumber(awardUnitBean.getUnitNumber());
                    //Added for Case 2778 - Award Notice Schema Enhancement - Start

                     UnitDetailFormBean unitDetailFormBean = (UnitDetailFormBean)unitDataTxnBean.getUnitDetails(awardUnitBean.getUnitNumber());
                        if(unitDetailFormBean.getAdminOfficerName() !=null){
                            investigatorUnitsType.setAdministrativeOfficerName(unitDetailFormBean.getAdminOfficerName());
                        }
                        if(unitDetailFormBean.getAdminOfficerId() !=null){
                            investigatorUnitsType.setAdministrativeOfficer(unitDetailFormBean.getAdminOfficerId());
                        }
                        if(unitDetailFormBean.getUnitHeadId() !=null){
                            investigatorUnitsType.setUnitHead(unitDetailFormBean.getUnitHeadId());
                        }
                        if(unitDetailFormBean.getUnitHeadName() !=null){
                            investigatorUnitsType.setUnitHeadName(unitDetailFormBean.getUnitHeadName());
                        }
                        if(unitDetailFormBean.getDeanVpId() !=null){
                            investigatorUnitsType.setDeanVp(unitDetailFormBean.getDeanVpId());
                        }
                        if(unitDetailFormBean.getDeanVpName() !=null){
                            investigatorUnitsType.setDeanVpName(unitDetailFormBean.getDeanVpName());
                        }
                        if(unitDetailFormBean.getOtherIndToNotifyId() !=null){
                            investigatorUnitsType.setOtherIndividualToNotify(unitDetailFormBean.getOtherIndToNotifyId());
                        }
                         if(unitDetailFormBean.getOtherIndToNotifyName() !=null){
                            investigatorUnitsType.setOtherIndividualToNotifyName(unitDetailFormBean.getOtherIndToNotifyName());
                        }

                    CoeusVector cvUnitData = (CoeusVector)unitDataTxnBean.getAdminData(awardUnitBean.getUnitNumber());
                    if(cvUnitData !=null && cvUnitData.size() > 0){
                        for(int index = 0; index < cvUnitData.size() ; index++){
                            UnitAdministratorBean unitAdministratorBean = (UnitAdministratorBean) cvUnitData.get(index);
                            unitAdministratorType = new InvestigatorUnitsTypeImpl.UnitAdministratorTypeImpl();
                            if(unitAdministratorBean.getAdministrator() !=null){
                                unitAdministratorType.setAdministrator(unitAdministratorBean.getAdministrator());
                                unitAdministratorType.setAdministratorName(unitAdministratorBean.getPersonName());
                            }
                          unitAdministratorType.setUnitAdministratorTypeCode(new Integer(unitAdministratorBean.getUnitAdminTypeCode()));

                          investigatorUnitsType.getUnitAdministrator().add(unitAdministratorType);
                          }
                    }
                    //Added for Case 2778 - Award Notice Schema Enhancement - End

                    investigatorType.getInvestigatorUnit().add(investigatorUnitsType);
                }

            }

            //Added for Case 3823 - Key Person Records Needed in Inst Proposal and Award  -start
            KeyPersonType keyPersonType;
            AwardType.AwardKeyPersonsType awardKeyPersonsType = new AwardTypeImpl.AwardKeyPersonsTypeImpl();
            awardType.setAwardKeyPersons(awardKeyPersonsType);
             // Added for the case COEUSDEV-152-Formatting of the dollars on the printed NOA and Money and End Dates History screen-start
            if(cvKeyPersons!= null && cvKeyPersons.size()>0){
            // Added for the case COEUSDEV-152-Formatting of the dollars on the printed NOA and Money and End Dates History screen-end
                for (int index = 0;index<cvKeyPersons.size();index++) {
                    AwardKeyPersonBean awardKeyPersonBean = (AwardKeyPersonBean) cvKeyPersons.get(index);
                    keyPersonType = new KeyPersonTypeImpl();
                    keyPersonType.setAwardNumber(mitAwardNumber);
                    keyPersonType.setSequenceNumber(awardKeyPersonBean.getSequenceNumber());
                    keyPersonType.setPersonId(awardKeyPersonBean.getPersonId());
                    keyPersonType.setPersonName(awardKeyPersonBean.getPersonName());
                    keyPersonType.setFaculty(awardKeyPersonBean.isFacultyFlag());
                    BigDecimal bdecAmount = new BigDecimal(awardKeyPersonBean.getPercentageEffort());
                    keyPersonType.setPercentEffort(bdecAmount.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                    keyPersonType.setRoleName(awardKeyPersonBean.getProjectRole());
                    awardType.getAwardKeyPersons().getKeyPerson().add(keyPersonType);
                }
            }
            //3823 -key persons - end

            //ScienceCodeDetailType
            CoeusVector cvScienceCode = awardReportTxnBean.getScienceCodeForMan(mitAwardNumber);
            AwardType.AwardScienceCodesType awardScienceCodesType = new AwardTypeImpl.AwardScienceCodesTypeImpl();
            awardType.setAwardScienceCodes(awardScienceCodesType);
            AwardScienceCodeBean awardScienceCodeBean;
            ScienceCodeDetailType scienceCodeDetailType;
            if(cvScienceCode!=null) {
                for (int index=0;index<cvScienceCode.size();index++) {
                    awardScienceCodeBean = (AwardScienceCodeBean)cvScienceCode.get(index);
                    scienceCodeDetailType = new ScienceCodeDetailTypeImpl();

                    scienceCodeDetailType.setAwardNumber(mitAwardNumber);
                    scienceCodeDetailType.setCode(awardScienceCodeBean.getScienceCode());
                    scienceCodeDetailType.setDescription(awardScienceCodeBean.getDescription());
                    scienceCodeDetailType.setSequenceNumber(awardScienceCodeBean.getSequenceNumber());

                    awardType.getAwardScienceCodes().getScienceCodeDetail().add(scienceCodeDetailType);
                }
            }

            //SpecialReviewType

            //getting special review
            CoeusVector cvSpecialReview  = awardTxnBean.getAwardSpecialReview(mitAwardNumber);
            AwardType.AwardSpecialReviewsType awardSpecialReviewsType = new AwardTypeImpl.AwardSpecialReviewsTypeImpl();
            awardType.setAwardSpecialReviews(awardSpecialReviewsType);
            SpecialReviewType specialReviewType;
            AwardSpecialReviewBean awardSpecialReviewBean;
            if (cvSpecialReview!=null) {
                for (int indx=0;indx<cvSpecialReview.size();indx++) {
                    awardSpecialReviewBean = (AwardSpecialReviewBean) cvSpecialReview.get(indx);
                    specialReviewType = new SpecialReviewTypeImpl();

                    //case 4525 start

                    int linkedToIRBCode = Integer.parseInt(coeusFunctions.getParameterValue("LINKED_TO_IRB_CODE"));
                    String linkage = coeusFunctions.getParameterValue("ENABLE_PROTOCOL_TO_AWARD_LINK");

                    if(awardSpecialReviewBean != null && linkage != null){
                        if ( linkage.equals("1")){//linkage is on
                            if(awardSpecialReviewBean.getApprovalCode() == linkedToIRBCode){
                                ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
                                ProtocolInfoBean protocolInfoBean = protocolDataTxnBean.getProtocolInfo(awardSpecialReviewBean.getProtocolSPRevNumber());
                                if(protocolInfoBean != null){
                                    specialReviewType.setApplicationDate(dateUtils.getCalendar(protocolInfoBean.getApplicationDate()));
                                    specialReviewType.setApprovalDate(dateUtils.getCalendar(protocolInfoBean.getApprovalDate()));
                                    specialReviewType.setApprovalTypeDesc(UtilFactory.convertNull(protocolInfoBean. getProtocolStatusDesc()));
                                }
                            }else{
                                specialReviewType.setApplicationDate(dateUtils.getCalendar(awardSpecialReviewBean.getApplicationDate()));
                                specialReviewType.setApprovalDate(dateUtils.getCalendar(awardSpecialReviewBean.getApprovalDate()));
                                specialReviewType.setApprovalTypeDesc(UtilFactory.convertNull(awardSpecialReviewBean.getApprovalDescription()));
                            }
                        }else{//linkage is off
                            specialReviewType.setApplicationDate(dateUtils.getCalendar(awardSpecialReviewBean.getApplicationDate()));
                            specialReviewType.setApprovalDate(dateUtils.getCalendar(awardSpecialReviewBean.getApprovalDate()));
                            specialReviewType.setApprovalTypeDesc(UtilFactory.convertNull(awardSpecialReviewBean.getApprovalDescription()));
                        }
                    }
                    //case 4525 comment out
//                    // Case# 3110:Special review in prop dev linked to protocols - Start
//                    // Set the Approval and Application Date of Protocol
//                    ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
//                    ProtocolInfoBean protocolInfoBean = protocolDataTxnBean.getProtocolInfo(awardSpecialReviewBean.getProtocolSPRevNumber());
////                    if (awardSpecialReviewBean.getApplicationDate()!=null) {
////                        Calendar applicationDate = Calendar.getInstance();
////                        applicationDate.setTime(awardSpecialReviewBean.getApplicationDate());
////                        specialReviewType.setApplicationDate(applicationDate);
////                    }
////
////                    if (awardSpecialReviewBean.getApprovalDate()!=null) {
////                        Calendar approvalDate = Calendar.getInstance();
////                        approvalDate.setTime(awardSpecialReviewBean.getApprovalDate());
////                        specialReviewType.setApprovalDate(approvalDate);
////                    }
//                    if(protocolInfoBean != null){
//                        specialReviewType.setApplicationDate(dateUtils.getCalendar(protocolInfoBean.getApplicationDate()));
//                        specialReviewType.setApprovalDate(dateUtils.getCalendar(protocolInfoBean.getApprovalDate()));
//                        specialReviewType.setApprovalTypeDesc(protocolInfoBean. getProtocolStatusDesc());
//                    } else{
//                        specialReviewType.setApplicationDate(dateUtils.getCalendar(awardSpecialReviewBean.getApplicationDate()));
//                        specialReviewType.setApprovalDate(dateUtils.getCalendar(awardSpecialReviewBean.getApprovalDate()));
//                        specialReviewType.setApprovalTypeDesc(awardSpecialReviewBean.getApprovalDescription());
//                    }
                    //case 4525 end
                    // Case# 3110:Special review in prop dev linked to protocols - End
                    specialReviewType.setApprovalType(awardSpecialReviewBean.getApprovalCode());
                    // Case# 3110:Special review in prop dev linked to protocols - Start
                    // Get the Status of the Protocol and set it as the status of the Special Review.
//                    specialReviewType.setApprovalTypeDesc(awardSpecialReviewBean.getApprovalDescription());
                    // Case# 3110:Special review in prop dev linked to protocols - End
                    specialReviewType.setAwardNumber(mitAwardNumber);
                    specialReviewType.setComments(awardSpecialReviewBean.getComments());
                    specialReviewType.setProtocolNumber(awardSpecialReviewBean.getProtocolSPRevNumber());
                    specialReviewType.setReviewType(awardSpecialReviewBean.getSpecialReviewCode());
                    specialReviewType.setSequenceNumber(awardSpecialReviewBean.getSequenceNumber());
                    specialReviewType.setReviewTypeDesc(awardSpecialReviewBean.getSpecialReviewDescription());
                    awardType.getAwardSpecialReviews().getSpecialReview().add(specialReviewType);
                }
            }

            //Award Report terms
            CoeusVector cvReportTerms = awardTxnBean.getAwardReportTerms(mitAwardNumber);
            if (cvReportTerms !=null) {
                cvReportTerms.sort("reportClassCode");
                AwardAddRepReqTxnBean awardAddRepReqTxnBean  = new AwardAddRepReqTxnBean();
                CoeusVector awardReportClassData = awardAddRepReqTxnBean.getRepClassInAwardRep(mitAwardNumber);

                AwardReportTermsBean awardReportTermsBean;
                ReportTermDetailsType reportTermDetailsType;
                ReportTermType reportTermType;
                AwardType.AwardReportingDetailsType awardReportingDetailsType = new AwardTypeImpl.AwardReportingDetailsTypeImpl();
                awardType.setAwardReportingDetails(awardReportingDetailsType);
                int classCode=0;
                for (int index=0;index<cvReportTerms.size();index++) {
                    awardReportTermsBean =(AwardReportTermsBean) cvReportTerms.get(index);
                    reportTermDetailsType = new ReportTermDetailsTypeImpl();
                    reportTermDetailsType.setAwardNumber(mitAwardNumber);
                    if (awardReportTermsBean.getDueDate()!= null){
                        Calendar dueDate = Calendar.getInstance();
                        dueDate.setTime(awardReportTermsBean.getDueDate());
                        reportTermDetailsType.setDueDate(dueDate);
                    }
                    reportTermDetailsType.setFrequencyBaseCode(awardReportTermsBean.getFrequencyBaseCode());
                    reportTermDetailsType.setFrequencyBaseDesc(awardReportTermsBean.getFrequencyBaseDescription());
                    reportTermDetailsType.setFrequencyCode(awardReportTermsBean.getFrequencyCode());
                    reportTermDetailsType.setFrequencyCodeDesc(awardReportTermsBean.getFrequencyDescription());
                    reportTermDetailsType.setOSPDistributionCode(awardReportTermsBean.getOspDistributionCode());
                    reportTermDetailsType.setOSPDistributionDesc(awardReportTermsBean.getOspDistributionDescription());
                    reportTermDetailsType.setReportClassCode(awardReportTermsBean.getReportClassCode());
                    reportTermDetailsType.setReportCode(awardReportTermsBean.getReportCode());
                    reportTermDetailsType.setReportCodeDesc(awardReportTermsBean.getReportDescription());
                    reportTermDetailsType.setSequenceNumber(seqNumber);
                    Vector classDescVec = departmentPersonTxnBean.getArgumentCodeDescription("REPORT CLASS");
                    CoeusVector cvClassDesc=new CoeusVector();
                    cvClassDesc.addAll(classDescVec);
                    Equals eqClass = new Equals("code",""+awardReportTermsBean.getReportClassCode());
                    CoeusVector cvClass = cvClassDesc.filter(eqClass);
                    reportTermType = new ReportTermTypeImpl();
                    ComboBoxBean classBean = (ComboBoxBean)cvClass.get(0);
                    if (awardReportTermsBean.getReportClassCode()==classCode) {
                        reportTermType.setDescription("");
                    } else {
                        reportTermType.setDescription(classBean.getDescription());
                        classCode = awardReportTermsBean.getReportClassCode();
                    }
                    reportTermDetailsType.setReportClassDesc(classBean.getDescription());
                    // for mailCopiesType need be group.
                    //ReportTermDetailsType.MailCopiesType mailCopiesType = new ReportTermDetailsTypeImpl.MailCopiesTypeImpl();
                    Equals eqReportCode = new Equals("aw_ReportCode", new Integer(awardReportTermsBean.getReportCode()));
                    Equals eqFrequencyCode = new Equals("aw_FrequencyCode",new Integer(awardReportTermsBean.getFrequencyCode()));
                    Equals eqFrequencyBaseCode = new Equals("aw_FrequencyBaseCode",new Integer(awardReportTermsBean.getFrequencyBaseCode()));
                    Equals eqOspDistributionCode = new Equals("aw_OspDistributionCode", new Integer(awardReportTermsBean.getOspDistributionCode()));
                    Equals eqDueDate = new Equals("dueDate", awardReportTermsBean.getDueDate());
                    Equals eqReportClassCode = new Equals("aw_ReportClassCode", new Integer(awardReportTermsBean.getReportClassCode()));
                    And eqAll = new And(new And(new And(eqReportCode, eqReportClassCode),new And(eqFrequencyBaseCode, eqOspDistributionCode)), new And(eqDueDate,eqFrequencyCode));
                    CoeusVector cvMailCopyType = new CoeusVector();
                    cvMailCopyType = cvReportTerms.filter(eqAll);

                    if (cvMailCopyType !=null && cvMailCopyType.size()>0){
                        for (int mailIndex =0; mailIndex < cvMailCopyType.size(); mailIndex++){
                            ReportTermDetailsType.MailCopiesType mailCopiesType = new ReportTermDetailsTypeImpl.MailCopiesTypeImpl();
                            awardReportTermsBean =(AwardReportTermsBean) cvMailCopyType.get(mailIndex);
                            mailCopiesType.setNumberOfCopies(""+awardReportTermsBean.getNumberOfCopies());
                            mailCopiesType.setRolodexId(""+awardReportTermsBean.getRolodexId());
                            mailCopiesType.setContactTypeCode(awardReportTermsBean.getContactTypeCode());
                            mailCopiesType.setContactTypeDesc(awardReportTermsBean.getContactTypeDescription());

                            // Added for Case 2355 - Award Notice Enhancement  - Start
                            // Added Rolodex name and Organization to Reporting , Technical Reporting section

                            StringBuffer name = new StringBuffer();
                            if (awardReportTermsBean.getLastName() != null) {
                                name.append(awardReportTermsBean.getLastName());
                            }
                            if (awardReportTermsBean.getSuffix() != null) {
                                name.append(" "+awardReportTermsBean.getSuffix());
                            }
                            if (awardReportTermsBean.getPrefix() != null) {
                                name.append(", "+awardReportTermsBean.getPrefix());
                                if (awardReportTermsBean.getFirstName() != null) {
                                    name.append(" "+awardReportTermsBean.getFirstName());
                                }
                            }else if (awardReportTermsBean.getFirstName() != null) {
                                name.append(", "+awardReportTermsBean.getFirstName());
                            }

                            if (awardReportTermsBean.getMiddleName() != null) {
                                name.append(" "+awardReportTermsBean.getMiddleName());
                            }
                            mailCopiesType.setRolodexName(name.toString());
                            mailCopiesType.setRolodexOrganization(awardReportTermsBean.getOrganization());

                            // check report distribution instruction the number of mail copies and rolodex id should be greater than zero
                            if(awardReportTermsBean.getNumberOfCopies() > 0 && awardReportTermsBean.getRolodexId() > 0 ){
                                reportTermDetailsType.getMailCopies().add(mailCopiesType);
                            }
                             // Added for Case 2355 - Award Notice Enhancement - End
                        }

                        NotEquals notReportCode = new NotEquals("aw_ReportCode", new Integer(awardReportTermsBean.getReportCode()));
                        NotEquals notFrequencyCode = new NotEquals("aw_FrequencyCode",new Integer(awardReportTermsBean.getFrequencyCode()));
                        NotEquals notFrequencyBaseCode = new NotEquals("aw_FrequencyBaseCode",new Integer(awardReportTermsBean.getFrequencyBaseCode()));
                        NotEquals notOspDistributionCode = new NotEquals("aw_OspDistributionCode", new Integer(awardReportTermsBean.getOspDistributionCode()));
                        NotEquals notDueDate = new NotEquals("dueDate", awardReportTermsBean.getDueDate());
                        NotEquals notReportClassCode = new NotEquals("aw_ReportClassCode", new Integer(awardReportTermsBean.getReportClassCode()));
                        Or notAll = new Or(new Or(new Or(notReportCode, notReportClassCode),new Or(notFrequencyBaseCode, notOspDistributionCode)), new Or(notDueDate,notFrequencyCode));
                        cvReportTerms = cvReportTerms.filter(notAll);
                        index = -1;
                        cvReportTerms.sort("reportClassCode");
                    }
                   // ReportTermDetailsType.MailCopiesType mailCopiesType = new ReportTermDetailsTypeImpl.MailCopiesTypeImpl();
                    //mailCopiesType.setNumberOfCopies(""+awardReportTermsBean.getNumberOfCopies());
                   // mailCopiesType.setRolodexId(""+awardReportTermsBean.getRolodexId());
                   // mailCopiesType.setContactTypeCode(awardReportTermsBean.getContactTypeCode());
                   // mailCopiesType.setContactTypeDesc(awardReportTermsBean.getContactTypeDescription());
                   //reportTermDetailsType.getMailCopies().add(mailCopiesType);
                    reportTermType.getReportTermDetails().add(reportTermDetailsType);
                    awardType.getAwardReportingDetails().getReportDetails().add(reportTermType);
                }
            }

            //getting the cost sharing
            CoeusVector cvCostSharing =awardTxnBean.getAwardCostSharing(mitAwardNumber);
            AwardCostSharingBean awardCostSharingBean;
            AwardType.AwardCostSharingType.CostSharingItemType costSharingItemType;
            AwardType.AwardCostSharingType awardCostSharingType = new AwardTypeImpl.AwardCostSharingTypeImpl();
            awardType.setAwardCostSharing(awardCostSharingType);
            if (cvCostSharing != null) {
                for (int index=0;index<cvCostSharing.size();index++) {
                    awardCostSharingBean= (AwardCostSharingBean)cvCostSharing.get(index);
                    costSharingItemType = new AwardTypeImpl.AwardCostSharingTypeImpl.CostSharingItemTypeImpl();
                    costSharingItemType.setAwardNumber(mitAwardNumber);
                    BigDecimal bdecAmount = new BigDecimal(awardCostSharingBean.getAmount());
                    costSharingItemType.setAmount(bdecAmount.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                    //costSharingItemType.setCostSharingDescription(awardCostSharingBean.getCostSharingType());
                    InstituteProposalLookUpDataTxnBean instituteProposalLookUpDataTxnBean = new InstituteProposalLookUpDataTxnBean();
                    CoeusVector cvCostSharingTypes = instituteProposalLookUpDataTxnBean.getCostSharingType();
                    Equals eqCSTypeCode = new Equals("code",""+awardCostSharingBean.getCostSharingType());
                    CoeusVector cvFilteredCSTypes= cvCostSharingTypes.filter(eqCSTypeCode);
                    ComboBoxBean csTypeBean = null;
                    if (cvFilteredCSTypes!=null && cvFilteredCSTypes.size()>0) {
                        csTypeBean = (ComboBoxBean)cvFilteredCSTypes.get(0);
                        costSharingItemType.setCostSharingDescription(csTypeBean.getDescription());
                    }
                    costSharingItemType.setCostSharingType(awardCostSharingBean.getCostSharingType());
                    costSharingItemType.setDestinationAccount(awardCostSharingBean.getDestinationAccount());
                    costSharingItemType.setFiscalYear(awardCostSharingBean.getFiscalYear());
                    costSharingItemType.setPercentage(awardCostSharingBean.getCostSharingPercentage());
                    costSharingItemType.setSequenceNumber(seqNumber);
                    costSharingItemType.setSourceAccount(awardCostSharingBean.getSourceAccount());
                    awardType.getAwardCostSharing().getCostSharingItem().add(costSharingItemType);
                }
            }

            CoeusVector cvCSComments = awardTxnBean.getAwardCommentsForCommentCode(mitAwardNumber, COST_SHARING_COMMENT);
            if (cvCSComments!=null && cvCSComments.size()>0) {
                AwardCommentsBean costSharingCommentBean = (AwardCommentsBean)cvCSComments.get(0);
                awardType.getAwardCostSharing().setComments(costSharingCommentBean.getComments());
            }

            //IDC Rates
            //getting the Award IDC Rates
            CoeusVector cvIDCRates = awardTxnBean.getAwardIDCRate(mitAwardNumber);
            AwardType.AwardIndirectCostsType.IndirectCostSharingItemType indirectCostSharingItemType;
            AwardType.AwardIndirectCostsType awardIndirectCostsType = new AwardTypeImpl.AwardIndirectCostsTypeImpl();
            awardType.setAwardIndirectCosts(awardIndirectCostsType);
            if (cvIDCRates != null) {
                for (int index=0;index<cvIDCRates.size();index++) {
                    AwardIDCRateBean awardIDCRateBean = (AwardIDCRateBean)cvIDCRates.get(index);
                    indirectCostSharingItemType =
                    new AwardTypeImpl.AwardIndirectCostsTypeImpl.IndirectCostSharingItemTypeImpl();
                    BigDecimal bdecApplicableRate = new BigDecimal(awardIDCRateBean.getApplicableIDCRate());
                    indirectCostSharingItemType.setApplicableRate(bdecApplicableRate.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                    indirectCostSharingItemType.setAwardNumber(awardIDCRateBean.getMitAwardNumber());
                    indirectCostSharingItemType.setCampus(awardIDCRateBean.isOnOffCampusFlag());
                    indirectCostSharingItemType.setDestinationAccount(awardIDCRateBean.getDestinationAccount());
                    if(awardIDCRateBean.getEndDate()!=null) {
                        Calendar endDate = Calendar.getInstance();
                        endDate.setTime(awardIDCRateBean.getEndDate());
                        indirectCostSharingItemType.setEndDate(endDate);
                    }
                    indirectCostSharingItemType.setFiscalYear(awardIDCRateBean.getFiscalYear());
                    indirectCostSharingItemType.setIDCRateType(awardIDCRateBean.getIdcRateTypeCode());
                    InstituteProposalLookUpDataTxnBean instituteProposalLookUpDataTxnBean = new InstituteProposalLookUpDataTxnBean();
                    CoeusVector cvIDCRateTypes = instituteProposalLookUpDataTxnBean.getIDCRateType();
                    Equals eqRateTypeCode = new Equals("code",""+awardIDCRateBean.getIdcRateTypeCode());
                    CoeusVector cvFilteredRateTypes= cvIDCRateTypes.filter(eqRateTypeCode);
                    ComboBoxBean ratesTypeBean = null;
                    if (cvFilteredRateTypes!=null && cvFilteredRateTypes.size()>0) {
                        ratesTypeBean = (ComboBoxBean)cvFilteredRateTypes.get(0);
                        indirectCostSharingItemType.setIDCRateDescription(ratesTypeBean.getDescription());
                    }
                    indirectCostSharingItemType.setSequenceNumber(awardIDCRateBean.getSequenceNumber());
                    indirectCostSharingItemType.setSourceAccount(awardIDCRateBean.getSourceAccount());
                    if (awardIDCRateBean.getStartDate()!=null){
                        Calendar startDate = Calendar.getInstance();
                        startDate.setTime(awardIDCRateBean.getStartDate());
                        indirectCostSharingItemType.setStartDate(startDate);
                    }
                    BigDecimal bdecUnderRecoveryAmount = new BigDecimal(awardIDCRateBean.getUnderRecoveryOfIDC());
                    indirectCostSharingItemType.setUnderRecoveryAmount(bdecUnderRecoveryAmount.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                    awardType.getAwardIndirectCosts().getIndirectCostSharingItem().add(indirectCostSharingItemType);
                }
            }
            CoeusVector cvIDCComments = awardTxnBean.getAwardCommentsForCommentCode(mitAwardNumber, IDC_COMMENT);
            if (cvIDCComments!=null && cvIDCComments.size()>0) {
                AwardCommentsBean idcCommentBean = (AwardCommentsBean)cvIDCComments.get(0);
                awardType.getAwardIndirectCosts().setComments(idcCommentBean.getComments());
            }
            //Transferring Sponsors
            //getting the award sponsor funding
            CoeusVector cvTransferingSponsor = awardTxnBean.getAwardSponsorFunding(mitAwardNumber);
            AwardTransferingSponsorBean awardTransferingSponsorBean=null;
            if (cvTransferingSponsor!=null && cvTransferingSponsor.size()>0) {

                AwardType.AwardTransferringSponsorsType awardTransferringSponsorsType = new AwardTypeImpl.AwardTransferringSponsorsTypeImpl();
                awardType.setAwardTransferringSponsors(awardTransferringSponsorsType);
                for (int index = 0; index < cvTransferingSponsor.size(); index++){
                    awardTransferingSponsorBean= (AwardTransferingSponsorBean)cvTransferingSponsor.get(index);
                    AwardType.AwardTransferringSponsorsType.TransferringSponsorType transferringSponsorType =
                        new AwardTypeImpl.AwardTransferringSponsorsTypeImpl.TransferringSponsorTypeImpl();
                    transferringSponsorType.setAwardNumber(mitAwardNumber);
                    transferringSponsorType.setSequenceNumber(seqNumber);
                    transferringSponsorType.setSponsorCode(awardTransferingSponsorBean.getSponsorCode());
                    transferringSponsorType.setSponsorDescription(awardTransferingSponsorBean.getSponsorName());

                    awardType.getAwardTransferringSponsors().getTransferringSponsor().add(transferringSponsorType);
                }
            }

            //Added for Case 3122 - Award Notice Enhancement -Start
            //AwardCloseout
            AwardCloseOutBean awardCloseOutBean;
            Hashtable htAwardCloseoutDetail =   getAwardCloseOutDetails(mitAwardNumber);
            CoeusVector cvCloseoutDetail = (CoeusVector)htAwardCloseoutDetail.get(AwardCloseOutBean.class);
            if( cvCloseoutDetail !=null && cvCloseoutDetail.size() > 0) {
                for(int index = 0; index < cvCloseoutDetail.size(); index++){
                    awardCloseOutBean = (AwardCloseOutBean) cvCloseoutDetail.get(index);
                    if (awardCloseOutBean != null) {
                        AwardType.CloseOutDeadlinesType closeOutDeadlinesType = new AwardTypeImpl.CloseOutDeadlinesTypeImpl();
                        awardType.setCloseOutDeadlines(closeOutDeadlinesType);
                        if (awardCloseOutBean != null) {
                            awardType.getCloseOutDeadlines().setArchiveLocation(awardCloseOutBean.getArchiveLocation());
                        }
                        awardType.getCloseOutDeadlines().setAwardNumber(mitAwardNumber);
                        if (awardCloseOutBean.getCloseOutDate()!=null) {
                            Calendar closeOutDate = Calendar.getInstance();
                            closeOutDate.setTime(awardCloseOutBean.getCloseOutDate());
                            awardType.getCloseOutDeadlines().setCloseoutDate(closeOutDate);
                        }
                        // Modified for COEUSDEV-496 : Outstanding issues/wishlist for prinouts - NOA Delta Report, Inst Proposal summary - Start
//                        if (awardCloseOutBean.getInvoiceDueDate() !=null) {
//                            awardType.getCloseOutDeadlines().setFinalInvSubDateModified(awardCloseOutBean.getInvoiceDueDate());
//                        }
//
//                        if (awardCloseOutBean.getPatentDueDate() !=null) {
//                            awardType.getCloseOutDeadlines().setFinalPatentSubDateModified(awardCloseOutBean.getPatentDueDate());
//                        }
//                        if (awardCloseOutBean.getPropertyDueDate() !=null) {
//                             awardType.getCloseOutDeadlines().setFinalPropSubDateModified(awardCloseOutBean.getPropertyDueDate());
//                        }
//                        if (awardCloseOutBean.getTechnicalDueDate()!=null) {
//                            awardType.getCloseOutDeadlines().setFinalTechSubDateModified(awardCloseOutBean.getTechnicalDueDate());
//                        }
                        DateUtils dateUtils = new DateUtils();
                        if (awardCloseOutBean.getFinalInvSubmissionDate() !=null) {

                            awardType.getCloseOutDeadlines().setFinalInvSubDateModified(
                                    dateUtils.formatDate(awardCloseOutBean.getFinalInvSubmissionDate().toString(),"dd-MMM-yyyy"));
                        }

                        if (awardCloseOutBean.getFinalPatentSubmissionDate() !=null) {
                            awardType.getCloseOutDeadlines().setFinalPatentSubDateModified(
                                     dateUtils.formatDate(awardCloseOutBean.getFinalPatentSubmissionDate().toString(),"dd-MMM-yyyy"));

                        }
                        if (awardCloseOutBean.getFinalPropSubmissionDate() !=null) {
                            awardType.getCloseOutDeadlines().setFinalPropSubDateModified(
                                    dateUtils.formatDate(awardCloseOutBean.getFinalPropSubmissionDate().toString(),"dd-MMM-yyyy"));
                        }
                        if (awardCloseOutBean.getFinalTechSubmissionDate()!=null) {
                            awardType.getCloseOutDeadlines().setFinalTechSubDateModified(
                                    dateUtils.formatDate(awardCloseOutBean.getFinalTechSubmissionDate().toString(),"dd-MMM-yyyy"));
                        }
                        // Due Date Details
                        if (awardCloseOutBean.getInvoiceDueDate() !=null) {
                            awardType.getCloseOutDeadlines().setFinalInvDueDateModified(awardCloseOutBean.getInvoiceDueDate());
                        }

                        if (awardCloseOutBean.getPatentDueDate() !=null) {
                            awardType.getCloseOutDeadlines().setFinalPatentDueDateModified(awardCloseOutBean.getPatentDueDate());
                        }
                        if (awardCloseOutBean.getPropertyDueDate() !=null) {
                            awardType.getCloseOutDeadlines().setFinalPropDueDateModified(awardCloseOutBean.getPropertyDueDate());
                        }
                        if (awardCloseOutBean.getTechnicalDueDate()!=null) {
                            awardType.getCloseOutDeadlines().setFinalTechDueDateModified(awardCloseOutBean.getTechnicalDueDate());
                        }
                        // Modified for COEUSDEV-496 : Outstanding issues/wishlist for prinouts - NOA Delta Report, Inst Proposal summary - Start
                        awardType.getCloseOutDeadlines().setSequenceNumber(seqNumber);
                    }
                }

            }
        //Added for Case 3122 - Award Notice Enhancement -End
//Commented for Case 3122 - Award Notice Enhancement -Start
//             awardCloseOutBean  = awardTxnBean.getAwardCloseOut(mitAwardNumber);
//            if (awardCloseOutBean != null) {
//                AwardType.CloseOutDeadlinesType closeOutDeadlinesType = new AwardTypeImpl.CloseOutDeadlinesTypeImpl();
//                awardType.setCloseOutDeadlines(closeOutDeadlinesType);
//                if (awardCloseOutBean != null) {
//                    awardType.getCloseOutDeadlines().setArchiveLocation(awardCloseOutBean.getArchiveLocation());
//                }
//                awardType.getCloseOutDeadlines().setAwardNumber(mitAwardNumber);
//                if (awardCloseOutBean.getCloseOutDate()!=null) {
//                    Calendar closeOutDate = Calendar.getInstance();
//                    closeOutDate.setTime(awardCloseOutBean.getCloseOutDate());
//                    awardType.getCloseOutDeadlines().setCloseoutDate(closeOutDate);
//                }
//
//                if (awardCloseOutBean.getFinalInvSubmissionDate()!=null) {
//                    Calendar finalInvSubDate = Calendar.getInstance();
//                    finalInvSubDate.setTime(awardCloseOutBean.getFinalInvSubmissionDate());
//                    awardType.getCloseOutDeadlines().setFinalInvSubDate(finalInvSubDate);
//                }
//
//                if (awardCloseOutBean.getFinalPatentSubmissionDate()!=null) {
//                    Calendar finalPatentSubDate = Calendar.getInstance();
//                    finalPatentSubDate.setTime(awardCloseOutBean.getFinalPatentSubmissionDate());
//                    awardType.getCloseOutDeadlines().setFinalPatentSubDate(finalPatentSubDate);
//                }
//                if (awardCloseOutBean.getFinalPropSubmissionDate()!=null) {
//                    Calendar finalPropSubDate = Calendar.getInstance();
//                    finalPropSubDate.setTime(awardCloseOutBean.getFinalPropSubmissionDate());
//                    awardType.getCloseOutDeadlines().setFinalPropSubDate(finalPropSubDate);
//                }
//                if (awardCloseOutBean.getFinalTechSubmissionDate()!=null) {
//                    Calendar finalTechSubDate = Calendar.getInstance();
//                    finalTechSubDate.setTime(awardCloseOutBean.getFinalTechSubmissionDate());
//                    awardType.getCloseOutDeadlines().setFinalTechSubDate(finalTechSubDate);
//                }
//
//                awardType.getCloseOutDeadlines().setSequenceNumber(seqNumber);
//            }
            //Commented for Case 3122 - Award Notice Enhancement -End
            //Award Contacts
            CoeusVector cvAwardContacts = awardTxnBean.getAwardContacts(mitAwardNumber);
            AwardContactDetailsBean awardContactDetailsBean;
            AwardType.AwardContactsType awardContactsType = new AwardTypeImpl.AwardContactsTypeImpl();
            awardType.setAwardContacts(awardContactsType);
            RolodexDetailsType rolodexDetailsType;
            if (cvAwardContacts!=null){
                for (int index=0;index<cvAwardContacts.size();index++) {
                    awardContactDetailsBean = (AwardContactDetailsBean)cvAwardContacts.get(index);
                    rolodexDetailsType = new RolodexDetailsTypeImpl();
                    rolodexDetailsType.setAddress1(awardContactDetailsBean.getAddress1());
                    rolodexDetailsType.setAddress2(awardContactDetailsBean.getAddress2());
                    rolodexDetailsType.setAddress3(awardContactDetailsBean.getAddress3());
                    rolodexDetailsType.setCity(awardContactDetailsBean.getCity());
                    rolodexDetailsType.setComments(awardContactDetailsBean.getComments());
                    rolodexDetailsType.setCountryCode(awardContactDetailsBean.getCountryCode());
                    rolodexDetailsType.setCountryDescription(awardContactDetailsBean.getCountryName());
                    rolodexDetailsType.setCounty(awardContactDetailsBean.getCounty());
                    rolodexDetailsType.setEmail(awardContactDetailsBean.getEmailAddress());
                    rolodexDetailsType.setFax(awardContactDetailsBean.getFaxNumber());
                    StringBuffer name = new StringBuffer();
                    if (awardContactDetailsBean.getLastName() != null) {
                        name.append(awardContactDetailsBean.getLastName());
                    }
                    //case 1916 start
                    if (awardContactDetailsBean.getSuffix() != null) {
                        name.append(" "+awardContactDetailsBean.getSuffix());
                    }
                    if (awardContactDetailsBean.getPrefix() != null) {
                        name.append(", "+awardContactDetailsBean.getPrefix());
                        if (awardContactDetailsBean.getFirstName() != null) {
                            name.append(" "+awardContactDetailsBean.getFirstName());
                        }
                    }else if (awardContactDetailsBean.getFirstName() != null) {
                        name.append(", "+awardContactDetailsBean.getFirstName());
                    }
                    //case 1916 end
                    if (awardContactDetailsBean.getMiddleName() != null) {
                        name.append(" "+awardContactDetailsBean.getMiddleName());
                    }
                    rolodexDetailsType.setLastName(name.toString());
                    rolodexDetailsType.setFirstName(awardContactDetailsBean.getFirstName());

                    rolodexDetailsType.setMiddleName(awardContactDetailsBean.getMiddleName());
                    rolodexDetailsType.setOrganization(awardContactDetailsBean.getOrganization());
                    //rolodexDetailsType.setOwnedByUnit(awardContactDetailsBean.get
                    //rolodexDetailsType.setOwnedByUnitName();
                    //rolodexDetailsType.setOwnedByUnitName();
                    rolodexDetailsType.setPhoneNumber(awardContactDetailsBean.getPhoneNumber());
                    rolodexDetailsType.setPincode(awardContactDetailsBean.getPostalCode());
                    rolodexDetailsType.setPrefix(awardContactDetailsBean.getPrefix());
                    rolodexDetailsType.setRolodexId(String.valueOf(awardContactDetailsBean.getRolodexId()));
                    rolodexDetailsType.setSponsorCode(awardContactDetailsBean.getSponsorCode());
                    rolodexDetailsType.setSponsorName(awardContactDetailsBean.getSponsorName());
                    rolodexDetailsType.setStateCode(awardContactDetailsBean.getState());
                    rolodexDetailsType.setStateDescription(awardContactDetailsBean.getStateName());
                    rolodexDetailsType.setSuffix(awardContactDetailsBean.getSuffix());
                    rolodexDetailsType.setTitle(awardContactDetailsBean.getTitle());
                    //awardType.getAwardContacts().getContactDetails().

                    AwardType.AwardContactsType.ContactDetailsType contactDetailsType  =
                    new AwardTypeImpl.AwardContactsTypeImpl.ContactDetailsTypeImpl();
                    contactDetailsType.setAwardNumber(mitAwardNumber);
                    contactDetailsType.setContactType(awardContactDetailsBean.getContactTypeCode());
                    contactDetailsType.setContactTypeDesc(awardContactDetailsBean.getContactTypeDescription());
                    contactDetailsType.setRolodexDetails(rolodexDetailsType);
                    contactDetailsType.setSequenceNumber(seqNumber);
                    awardContactsType.getContactDetails().add(contactDetailsType);
                }
            }

            CoeusVector cvAwardBudget = awardTxnBean.getAwardBudget(mitAwardNumber);
            if (cvAwardBudget !=null && cvAwardBudget.size()>0) {
                AwardBudgetBean awardBudgetBean = (AwardBudgetBean) cvAwardBudget.get(0);
                AwardType.AwardBudgetDetailsType awardBudgetDetailsType = new AwardTypeImpl.AwardBudgetDetailsTypeImpl();
                AwardType.AwardBudgetDetailsType.BudgetDetailsType budgetDetailsType =
                new AwardTypeImpl.AwardBudgetDetailsTypeImpl.BudgetDetailsTypeImpl();
                BigDecimal bdecAnticipatedAmount = new BigDecimal(awardBudgetBean.getAnticipatedAmount());
                budgetDetailsType.setAnticipatedAmount(bdecAnticipatedAmount.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                budgetDetailsType.setAwardNumber(awardBudgetBean.getMitAwardNumber());
                budgetDetailsType.setCostElementCode(awardBudgetBean.getCostElement());
                budgetDetailsType.setCostElementDescription(awardBudgetBean.getCostElementDescription());
                budgetDetailsType.setLineItemDescription(awardBudgetBean.getLineItemDescription());
                budgetDetailsType.setLineItemNumber(awardBudgetBean.getLineItemNumber());
                BigDecimal bdecObligatedAmount = new BigDecimal(awardBudgetBean.getObligatedAmount());
                budgetDetailsType.setObligatedAmount(bdecObligatedAmount.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                budgetDetailsType.setSequenceNumber(awardBudgetBean.getSequenceNumber());
                awardBudgetDetailsType.getBudgetDetails().add(budgetDetailsType);
                awardType.setAwardBudgetDetails(awardBudgetDetailsType);
            }

            //PaymentDetails

            CoeusVector cvPaymentSchedule= awardTxnBean.getAwardPaymentSchedule(mitAwardNumber);
            if (cvPaymentSchedule!=null) {
                AwardPaymentScheduleBean awardPaymentScheduleBean;
                AwardType.AwardPaymentSchedulesType.PaymentScheduleType paymentScheduleType;
                AwardType.AwardPaymentSchedulesType awardPaymentSchedulesType = new AwardTypeImpl.AwardPaymentSchedulesTypeImpl();
                awardType.setAwardPaymentSchedules(awardPaymentSchedulesType);
                for (int index=0;index<cvPaymentSchedule.size();index++) {
                    awardPaymentScheduleBean = (AwardPaymentScheduleBean)cvPaymentSchedule.get(index);
                    paymentScheduleType = new AwardTypeImpl.AwardPaymentSchedulesTypeImpl.PaymentScheduleTypeImpl();
                    BigDecimal bdecAmount = new BigDecimal(awardPaymentScheduleBean.getAmount());
                    paymentScheduleType.setAmount(bdecAmount.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                    paymentScheduleType.setAwardNumber(awardPaymentScheduleBean.getMitAwardNumber());
                    if (awardPaymentScheduleBean.getDueDate()!=null){
                        Calendar paymentDueDate = Calendar.getInstance();
                        paymentDueDate.setTime(awardPaymentScheduleBean.getDueDate());
                        paymentScheduleType.setDueDate(paymentDueDate);
                    }
                    paymentScheduleType.setInvoiceNumber(awardPaymentScheduleBean.getInvoiceNumber());
                    paymentScheduleType.setSequenceNumber(awardPaymentScheduleBean.getSequenceNumber());
                    paymentScheduleType.setStatusDescription(awardPaymentScheduleBean.getStatusDescription());
                    if (awardPaymentScheduleBean.getSubmitDate()!=null) {
                        Calendar paymentSubDate = Calendar.getInstance();
                        paymentSubDate.setTime(awardPaymentScheduleBean.getSubmitDate());
                        paymentScheduleType.setSubmitDate(paymentSubDate);
                    }
                    paymentScheduleType.setSubmittedBy(awardPaymentScheduleBean.getSubmitBy());
                    awardType.getAwardPaymentSchedules().getPaymentSchedule().add(paymentScheduleType);
                }
            }
//            InputStream is = getClass().getResourceAsStream("/coeus.properties");
//            Properties coeusProps = new Properties();
//            coeusProps.load(is);
            String schoolName = CoeusProperties.getProperty(CoeusPropertyKeys.SCHOOL_NAME);
            String schoolAcronym = CoeusProperties.getProperty(CoeusPropertyKeys.SCHOOL_ACRONYM);
            // SchoolInfoType
            SchoolInfoType schoolInfoType = new SchoolInfoTypeImpl();
            schoolInfoType.setSchoolName(schoolName);
            schoolInfoType.setAcronym(schoolAcronym);

            //FundingProposal
            CoeusVector cvFundingProposal = awardTxnBean.getFundingProposalsForAward(mitAwardNumber);
            AwardFundingProposalBean awardFundingProposalBean;
            AwardType.AwardFundingProposalsType.FundingProposalType fundingProposalType;
            AwardType.AwardFundingProposalsType awardFundingProposalsType = new AwardTypeImpl.AwardFundingProposalsTypeImpl();
            awardType.setAwardFundingProposals(awardFundingProposalsType);
            if (cvFundingProposal != null) {
                for (int index=0;index<cvFundingProposal.size();index++) {
                    awardFundingProposalBean = (AwardFundingProposalBean)cvFundingProposal.get(index);
                    fundingProposalType = new AwardTypeImpl.AwardFundingProposalsTypeImpl.FundingProposalTypeImpl();
                    fundingProposalType.setAwardNumber(mitAwardNumber);
                    BigDecimal bdecDirectCostTotal = new BigDecimal(awardFundingProposalBean.getTotalDirectCostTotal());
                    fundingProposalType.setDirectCostTotal(bdecDirectCostTotal.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                    BigDecimal bdecIndirectCostTotal = new BigDecimal(awardFundingProposalBean.getTotalInDirectCostTotal());
                    fundingProposalType.setIndirectCostTotal(bdecIndirectCostTotal.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                    fundingProposalType.setProopsalTypeCode(awardFundingProposalBean.getProposalTypeCode());
                    fundingProposalType.setProposalNumber(awardFundingProposalBean.getProposalNumber());
                    fundingProposalType.setProposalSequenceNumber(awardFundingProposalBean.getSequenceNumber());
                    fundingProposalType.setProposalStatusCode(awardFundingProposalBean.getProposalStatusCode());
                    fundingProposalType.setProposalStatusDescription(awardFundingProposalBean.getProposalTypeDescription());
                    fundingProposalType.setProposalTypeDescription(awardFundingProposalBean.getProposalTypeDescription());
                    if (awardFundingProposalBean.getRequestEndDateTotal()!=null) {
                        Calendar reqEndDateTotal = Calendar.getInstance();
                        reqEndDateTotal.setTime(awardFundingProposalBean.getRequestEndDateTotal());
                        fundingProposalType.setRequestedEndDateTotal(reqEndDateTotal);
                    }
                    if (awardFundingProposalBean.getRequestStartDateTotal()!=null) {
                        Calendar reqStartDateTotal = Calendar.getInstance();
                        reqStartDateTotal.setTime(awardFundingProposalBean.getRequestStartDateTotal());
                        fundingProposalType.setRequestedStartDateTotal(reqStartDateTotal);
                    }
                    fundingProposalType.setSequenceNumber(awardFundingProposalBean.getSequenceNumber());
                    awardType.getAwardFundingProposals().getFundingProposal().add(fundingProposalType);
                }
            }

            // get the user
            UserInfoBean userBean = (UserInfoBean)new
            UserDetailsBean().getUserInfo(loggedInUser);
            PersonInfoBean personInfoBean = awardReportTxnBean.getAoInfo(unitNumber);

            CoeusVector cvAwardFundingProp = awardTxnBean.getFundingProposalsForAward(mitAwardNumber);

            /*String moduleItemKey = "";
            int moduleCode = 0;
            int retStatusNumber = awardReportTxnBean.getIsAllDiscStatusComplete(moduleItemKey,moduleCode)


            CoiDisclForItemBean coiDisclForItemBean = awardReportTxnBean.getCoiDisclForItem(moduleItemKey, moduleCode);

            String sponsorCode = "";
            RolodexMaintenanceDataTxnBean rolodexMaintenanceDataTxnBean = new RolodexMaintenanceDataTxnBean();
            String sponsorName = rolodexMaintenanceDataTxnBean.getSponsorName(sponsorCode);

            String parentMitAwardNumber = awardTxnBean.getRootAward(mitAwardNumber);
            AwardAmountInfoBean amountInfoBean = awardReportTxnBean.getChildAward(parentMitAwardNumber);
            reportsMap.put(AwardAmountInfoBean.class, amountInfoBean);*/

            AwardType.AwardTermsDetailsType awardTermsDetailsType = new AwardTypeImpl.AwardTermsDetailsTypeImpl();
            awardType.setAwardTermsDetails(awardTermsDetailsType);

            //Get Equipment Approval
            CoeusVector cvTermsData = new CoeusVector();
            CoeusVector cvTermsCodes = new CoeusVector();
            AwardTermsBean awardTermsBean = new AwardTermsBean();
            AwardTermsTxnBean awardTermsTxnBean = new AwardTermsTxnBean();
            cvTermsData = awardLookUpDataTxnBean.getEquipmentApproval();
            cvTermsCodes = awardTermsTxnBean.getAwardEquipmentTerms(mitAwardNumber);
            TermType equipmentTermType = new TermTypeImpl();
            equipmentTermType.setDescription("Equipment");
            Equals eqTerms=null;
            if (cvTermsCodes!=null) {
                for (int indx=0;indx<cvTermsCodes.size();indx++) {
                    awardTermsBean = (AwardTermsBean)cvTermsCodes.get(indx);
                    eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                    CoeusVector cvFilteredEquipmentData = cvTermsData.filter(eqTerms);
                    AwardTermsBean equipmentAwardTermsBean = (AwardTermsBean)cvFilteredEquipmentData.get(0);
                    //AwardType.AwardTermsDetailsType awardTermsDetailsType = new AwardTypeImpl.AwardTermsDetailsTypeImpl();
                    //awardType.setAwardTermsDetails(awardTermsDetailsType);
                    TermDetailsType equipmentTermDetailsType = new TermDetailsTypeImpl() ;
                    equipmentTermDetailsType.setAwardNumber(mitAwardNumber);
                    equipmentTermDetailsType.setSequenceNumber(equipmentAwardTermsBean.getSequenceNumber());
                    equipmentTermDetailsType.setTermCode(equipmentAwardTermsBean.getTermsCode());
                    equipmentTermDetailsType.setTermDescription(equipmentAwardTermsBean.getTermsDescription());
                    equipmentTermType.getTermDetails().add(equipmentTermDetailsType);
                }
                awardType.getAwardTermsDetails().getTerm().add(equipmentTermType);
            }

            //Get Invention
            cvTermsData = null;
            cvTermsData = awardLookUpDataTxnBean.getInvention();
            cvTermsCodes = awardTermsTxnBean.getAwardInventionTerms(mitAwardNumber);
            TermType inventionTermType = new TermTypeImpl();
            inventionTermType.setDescription("Invention");
            if (cvTermsCodes !=null) {
                for (int indx=0;indx<cvTermsCodes.size();indx++) {
                    awardTermsBean = (AwardTermsBean)cvTermsCodes.get(indx);
                    eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                    CoeusVector cvFilteredInventionData = cvTermsData.filter(eqTerms);
                    AwardTermsBean inventionTermsBean = (AwardTermsBean)cvFilteredInventionData.get(0);
                    TermDetailsType inventionTermDetailsType = new TermDetailsTypeImpl();
                    inventionTermDetailsType.setAwardNumber(mitAwardNumber);
                    inventionTermDetailsType.setSequenceNumber(seqNumber);
                    inventionTermDetailsType.setTermCode(inventionTermsBean.getTermsCode());
                    inventionTermDetailsType.setTermDescription(inventionTermsBean.getTermsDescription());
                    inventionTermType.getTermDetails().add(inventionTermDetailsType);
                }
                awardType.getAwardTermsDetails().getTerm().add(inventionTermType);
            }

            //Get Prior Approval
            cvTermsData = null;
            cvTermsData = awardLookUpDataTxnBean.getPriorApproval();
            cvTermsCodes = awardTermsTxnBean.getAwardPriorApprovalTerms(mitAwardNumber);
            TermType priorAprrovalTermType = new TermTypeImpl();
            priorAprrovalTermType.setDescription("Other Approvals/Notification");
            if (cvTermsCodes !=null) {
                for (int indx=0;indx<cvTermsCodes.size();indx++) {
                    awardTermsBean = (AwardTermsBean)cvTermsCodes.get(indx);
                    eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                    CoeusVector cvFilteredPriorApprlData = cvTermsData.filter(eqTerms);
                    AwardTermsBean priorAprrovalTermsBean = (AwardTermsBean)cvFilteredPriorApprlData.get(0);

                    TermDetailsType priorAprrovalTermDetailsType = new TermDetailsTypeImpl() ;
                    priorAprrovalTermDetailsType.setAwardNumber(mitAwardNumber);
                    priorAprrovalTermDetailsType.setSequenceNumber(seqNumber);
                    priorAprrovalTermDetailsType.setTermCode(priorAprrovalTermsBean.getTermsCode());
                    priorAprrovalTermDetailsType.setTermDescription(priorAprrovalTermsBean.getTermsDescription());
                    priorAprrovalTermType.getTermDetails().add(priorAprrovalTermDetailsType);
                }

                awardType.getAwardTermsDetails().getTerm().add(priorAprrovalTermType);
            }


            //Get Property Terms
            cvTermsData = null;
            cvTermsData = awardLookUpDataTxnBean.getProperty();
            cvTermsCodes = awardTermsTxnBean.getAwardPropertyTerms(mitAwardNumber);
            TermType propertyTermType = new TermTypeImpl();
            propertyTermType.setDescription("Property");
            if (cvTermsCodes!=null) {
                for (int indx=0;indx<cvTermsCodes.size();indx++) {
                    awardTermsBean = (AwardTermsBean)cvTermsCodes.get(indx);
                    eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                    CoeusVector cvFilteredPropertyData = cvTermsData.filter(eqTerms);
                    AwardTermsBean propertyTermsBean = (AwardTermsBean)cvFilteredPropertyData.get(0);

                    TermDetailsType propertyTermDetailsType = new TermDetailsTypeImpl() ;
                    propertyTermDetailsType.setAwardNumber(mitAwardNumber);
                    propertyTermDetailsType.setSequenceNumber(seqNumber);
                    propertyTermDetailsType.setTermCode(propertyTermsBean.getTermsCode());
                    propertyTermDetailsType.setTermDescription(propertyTermsBean.getTermsDescription());
                    propertyTermType.getTermDetails().add(propertyTermDetailsType);
                }

                awardType.getAwardTermsDetails().getTerm().add(propertyTermType);
            }

            //Get Publication Terms
            cvTermsData = null;
            cvTermsData = awardLookUpDataTxnBean.getPublication();
            cvTermsCodes = awardTermsTxnBean.getAwardPublicationTerms(mitAwardNumber);
            TermType publicationTermType = new TermTypeImpl();
            publicationTermType.setDescription("Publication");
            if (cvTermsCodes!=null) {
                for (int indx=0;indx<cvTermsCodes.size();indx++) {
                    awardTermsBean = (AwardTermsBean)cvTermsCodes.get(indx);
                    eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                    CoeusVector cvFilteredPublicationData = cvTermsData.filter(eqTerms);
                    AwardTermsBean publicationTermsBean = (AwardTermsBean)cvFilteredPublicationData.get(0);

                    TermDetailsType publicationTermDetailsType = new TermDetailsTypeImpl() ;
                    publicationTermDetailsType.setAwardNumber(mitAwardNumber);
                    publicationTermDetailsType.setSequenceNumber(seqNumber);
                    publicationTermDetailsType.setTermCode(publicationTermsBean.getTermsCode());
                    publicationTermDetailsType.setTermDescription(publicationTermsBean.getTermsDescription());
                    publicationTermType.getTermDetails().add(publicationTermDetailsType);
                }

                awardType.getAwardTermsDetails().getTerm().add(publicationTermType);
            }

            //Get Reference Documents Terms
            cvTermsData = null;
            cvTermsData = awardLookUpDataTxnBean.getReferencedDocument();
            cvTermsCodes = awardTermsTxnBean.getAwardReferencedDocumentTerms(mitAwardNumber);
            TermType referencedDocTermType = new TermTypeImpl();
            referencedDocTermType.setDescription("Referenced Documents");
            if (cvTermsCodes !=null) {
                for (int indx=0;indx<cvTermsCodes.size();indx++) {
                    awardTermsBean = (AwardTermsBean)cvTermsCodes.get(indx);
                    eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                    CoeusVector cvFilteredRefDocData = cvTermsData.filter(eqTerms);
                    AwardTermsBean referencedDocTermsBean = (AwardTermsBean)cvFilteredRefDocData.get(0);

                    TermDetailsType referencedDocTermDetailsType = new TermDetailsTypeImpl() ;
                    referencedDocTermDetailsType.setAwardNumber(mitAwardNumber);
                    referencedDocTermDetailsType.setSequenceNumber(seqNumber);
                    referencedDocTermDetailsType.setTermCode(referencedDocTermsBean.getTermsCode());
                    referencedDocTermDetailsType.setTermDescription(referencedDocTermsBean.getTermsDescription());
                    referencedDocTermType.getTermDetails().add(referencedDocTermDetailsType);
                }

                awardType.getAwardTermsDetails().getTerm().add(referencedDocTermType);
            }



            //Get Rights in data Terms
            cvTermsData = null;
            cvTermsData = awardLookUpDataTxnBean.getRightsInData();
            cvTermsCodes = awardTermsTxnBean.getAwardRightsInDataTerms(mitAwardNumber);
            TermType rightsInDataTermType = new TermTypeImpl();
            rightsInDataTermType.setDescription("Rights In Data");
            if (cvTermsCodes !=null) {
                for (int indx=0;indx<cvTermsCodes.size();indx++) {
                    awardTermsBean = (AwardTermsBean)cvTermsCodes.get(indx);
                    eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                    CoeusVector cvFilteredRightsData = cvTermsData.filter(eqTerms);

                    AwardTermsBean rightsInDataTermsBean = (AwardTermsBean)cvFilteredRightsData.get(0);

                    TermDetailsType rightsInDataTermDetailsType = new TermDetailsTypeImpl() ;
                    rightsInDataTermDetailsType.setAwardNumber(mitAwardNumber);
                    rightsInDataTermDetailsType.setSequenceNumber(seqNumber);
                    rightsInDataTermDetailsType.setTermCode(rightsInDataTermsBean.getTermsCode());
                    rightsInDataTermDetailsType.setTermDescription(rightsInDataTermsBean.getTermsDescription());
                    rightsInDataTermType.getTermDetails().add(rightsInDataTermDetailsType);
                }

                awardType.getAwardTermsDetails().getTerm().add(rightsInDataTermType);
            }

            //Get Subcontract Approval Terms
            cvTermsData = null;
            cvTermsData = awardLookUpDataTxnBean.getSubcontractApproval();
            cvTermsCodes = awardTermsTxnBean.getAwardSubcontractTerms(mitAwardNumber);
            TermType subcontractApprovalTermType = new TermTypeImpl();
            subcontractApprovalTermType.setDescription("Subcontracting");
            if (cvTermsCodes != null) {
                for (int indx=0;indx<cvTermsCodes.size();indx++) {
                    awardTermsBean = (AwardTermsBean)cvTermsCodes.get(indx);
                    eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                    CoeusVector cvFilteredSubContractData = cvTermsData.filter(eqTerms);
                    AwardTermsBean subcontractApprovalTermsBean = (AwardTermsBean)cvFilteredSubContractData.get(0);
                    TermDetailsType subcontractApprovalTermDetailsType = new  TermDetailsTypeImpl() ;
                    subcontractApprovalTermDetailsType.setAwardNumber(mitAwardNumber);
                    subcontractApprovalTermDetailsType.setSequenceNumber(seqNumber);
                    subcontractApprovalTermDetailsType.setTermCode(subcontractApprovalTermsBean.getTermsCode());
                    subcontractApprovalTermDetailsType.setTermDescription(subcontractApprovalTermsBean.getTermsDescription());
                    subcontractApprovalTermType.getTermDetails().add(subcontractApprovalTermDetailsType);
                }
                awardType.getAwardTermsDetails().getTerm().add(subcontractApprovalTermType);
            }

            //Get Travel Terms
            cvTermsData = null;

            cvTermsData = awardLookUpDataTxnBean.getTravelRestriction();
            cvTermsCodes = awardTermsTxnBean.getAwardTravelTerms(mitAwardNumber);
            TermType travelRestrictionTermType = new TermTypeImpl();
            travelRestrictionTermType.setDescription("Travel");
            if (cvTermsCodes != null) {
                for (int indx=0;indx<cvTermsCodes.size();indx++) {
                    awardTermsBean = (AwardTermsBean)cvTermsCodes.get(indx);
                    eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                    CoeusVector cvFilteredTravelRestrictionData = cvTermsData.filter(eqTerms);

                    AwardTermsBean travelRestrictionTermsBean = (AwardTermsBean)cvFilteredTravelRestrictionData.get(0);

                    TermDetailsType travelRestrictionTermDetailsType = new TermDetailsTypeImpl();
                    travelRestrictionTermDetailsType.setAwardNumber(mitAwardNumber);
                    travelRestrictionTermDetailsType.setSequenceNumber(seqNumber);
                    travelRestrictionTermDetailsType.setTermCode(travelRestrictionTermsBean.getTermsCode());
                    travelRestrictionTermDetailsType.setTermDescription(travelRestrictionTermsBean.getTermsDescription());
                    travelRestrictionTermType.getTermDetails().add(travelRestrictionTermDetailsType);
                }
                awardType.getAwardTermsDetails().getTerm().add(travelRestrictionTermType);
            }

            AwardType.AwardSpecialItemsType awardSpecialItemsType = new AwardTypeImpl.AwardSpecialItemsTypeImpl();
            CoeusVector cvApprovedEquipment = (CoeusVector) awardTxnBean.getAwardApprovedEquipment(mitAwardNumber);
            if (cvApprovedEquipment!=null && cvApprovedEquipment.size()>0) {
                AwardType.AwardSpecialItemsType.EquipmentType equipmentType;
                for (int index=0; index<cvApprovedEquipment.size();index++) {
                    AwardApprovedEquipmentBean awardApprovedEquipmentBean = (AwardApprovedEquipmentBean) cvApprovedEquipment.get(index);
                    equipmentType = new AwardTypeImpl.AwardSpecialItemsTypeImpl.EquipmentTypeImpl();
                    BigDecimal bdecAmount = new BigDecimal(awardApprovedEquipmentBean.getAmount());
                    equipmentType.setAmount(bdecAmount.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                    equipmentType.setAwardNumber(awardApprovedEquipmentBean.getMitAwardNumber());
                    equipmentType.setItem(awardApprovedEquipmentBean.getItem());
                    equipmentType.setModel(awardApprovedEquipmentBean.getModel());
                    equipmentType.setSequenceNumber(awardApprovedEquipmentBean.getSequenceNumber());
                    equipmentType.setVendor(awardApprovedEquipmentBean.getVendor());
                    awardSpecialItemsType.getEquipment().add(equipmentType);
                }
            }

            CoeusVector cvApprovedForeignTrip = (CoeusVector) awardTxnBean.getAwardApprovedForeignTrip(mitAwardNumber);
            if (cvApprovedForeignTrip!=null && cvApprovedForeignTrip.size()>0) {
                AwardType.AwardSpecialItemsType.ForeignTravelType foreignTravelType;
                for (int index=0; index<cvApprovedForeignTrip.size();index++) {
                    AwardApprovedForeignTripBean awardApprovedForeignTripBean = (AwardApprovedForeignTripBean) cvApprovedForeignTrip.get(index);
                    foreignTravelType= new AwardTypeImpl.AwardSpecialItemsTypeImpl.ForeignTravelTypeImpl();
                    BigDecimal bdecAmount = new BigDecimal(awardApprovedForeignTripBean.getAmount());
                    foreignTravelType.setAmount(bdecAmount.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                    foreignTravelType.setAwardNumber(awardApprovedForeignTripBean.getMitAwardNumber());
                    if (awardApprovedForeignTripBean.getDateFrom()!=null) {
                        Calendar dateFrom = Calendar.getInstance();
                        dateFrom.setTime(awardApprovedForeignTripBean.getDateFrom());
                        foreignTravelType.setDateFrom(dateFrom);
                    }
                    if (awardApprovedForeignTripBean.getDateTo() !=null) {
                        Calendar dateTo = Calendar.getInstance();
                        dateTo.setTime(awardApprovedForeignTripBean.getDateTo());
                        foreignTravelType.setDateTo(dateTo);
                    }
                    foreignTravelType.setDestination(awardApprovedForeignTripBean.getDestination());
                    foreignTravelType.setPersonId(awardApprovedForeignTripBean.getPersonId());
                    foreignTravelType.setPersonName(awardApprovedForeignTripBean.getPersonName());
                    foreignTravelType.setSequenceNumber(awardApprovedForeignTripBean.getSequenceNumber());
                    awardSpecialItemsType.getForeignTravel().add(foreignTravelType);
                }
            }

            CoeusVector cvApprovedSubcontract = (CoeusVector) awardTxnBean.getAwardApprovedSubcontract(mitAwardNumber);
            if (cvApprovedSubcontract!=null && cvApprovedSubcontract.size()>0) {
                AwardType.AwardSpecialItemsType.SubcontractType subcontractType;
                AwardApprovedSubcontractBean awardApprovedSubcontractBean;
                for (int index=0; index<cvApprovedSubcontract.size();index++) {
                    awardApprovedSubcontractBean = (AwardApprovedSubcontractBean) cvApprovedSubcontract.get(index);
                    subcontractType = new AwardTypeImpl.AwardSpecialItemsTypeImpl.SubcontractTypeImpl();
                    BigDecimal bdecAmount = new BigDecimal(awardApprovedSubcontractBean.getAmount());
                    subcontractType.setAmount(bdecAmount.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                    subcontractType.setAwardNumber(awardApprovedSubcontractBean.getMitAwardNumber());
                    subcontractType.setSequenceNumber(awardApprovedSubcontractBean.getSequenceNumber());
                    subcontractType.setSubcontractorName(awardApprovedSubcontractBean.getSubcontractName());
                    awardSpecialItemsType.getSubcontract().add(subcontractType);
                }
            }
            //Added for  Case 3338 - Subcontract Schema Changes -Start
            SubContractTxnBean subContractTxnBean = new SubContractTxnBean();
            CoeusVector cvFundingSouurceSubcontract = (CoeusVector) subContractTxnBean.getAwardSubContracts(mitAwardNumber);
            CoeusVector cvSubContractStatus = (CoeusVector) subContractTxnBean.getSubContractStatus();
            if (cvFundingSouurceSubcontract!=null && cvFundingSouurceSubcontract.size()>0) {
                AwardType.AwardSpecialItemsType.SubcontractFundingSourceType subcontractFundingSourceType;
                SubContractFundingSourceBean subContractFundingSourceBean ;
                for (int index=0; index < cvFundingSouurceSubcontract.size(); index++) {
//                    subContractFundingSourceBean = new SubContractFundingSourceBean();
                    subContractFundingSourceBean = (SubContractFundingSourceBean) cvFundingSouurceSubcontract.get(index);
                    subcontractFundingSourceType = new AwardTypeImpl.AwardSpecialItemsTypeImpl.SubcontractFundingSourceTypeImpl();
                    BigDecimal bdecAmount = new BigDecimal(subContractFundingSourceBean.getObligatedAmount());

                    subcontractFundingSourceType.setAwardNumber(subContractFundingSourceBean.getMitAwardNumber());
                    subcontractFundingSourceType.setSequenceNumber(subContractFundingSourceBean.getSequenceNumber());
                    subcontractFundingSourceType.setSubcontractorName(subContractFundingSourceBean.getOrganizationName());
                    subcontractFundingSourceType.setSubcontractCode(subContractFundingSourceBean.getSubContractCode());
                    subcontractFundingSourceType.setAmount(bdecAmount.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                    if(cvSubContractStatus !=null && cvSubContractStatus.size() > 0 ){
                      for( int index1 =0; index1 < cvSubContractStatus.size(); index1++){
                            ComboBoxBean comboBoxBean = (ComboBoxBean) cvSubContractStatus.get(index1);
                            String code = comboBoxBean.getCode();
                            if(code.equalsIgnoreCase(String.valueOf(subContractFundingSourceBean.getStatusCode()))){
                                subcontractFundingSourceType.setStatus(comboBoxBean.getDescription());
                                break;
                            }
                      }
                    }
                    awardSpecialItemsType.getSubcontractFundingSource().add(subcontractFundingSourceType);
                }
            }
            //Added for  Case 3338 - Subcontract Schema Changes -End
            awardType.setAwardSpecialItems(awardSpecialItemsType);

            //getting the frequency
            CoeusVector cvFrequencyData = awardLookUpDataTxnBean.getFrequency();

            //getting the frequencybase
            CoeusVector cvFrequencyBaseData = awardLookUpDataTxnBean.getFrequencyBase();

            //getting distribution
            CoeusVector cvDistribution = awardLookUpDataTxnBean.getDistribution();

            //getting the approved equipments
            AwardApprovedEquipmentBean awardApprovedEquipmentBean = awardReportTxnBean.getApprovedEquipments(mitAwardNumber);

            //getting the approved foreign equipments
            AwardApprovedForeignTripBean awardApprovedForeignTripBean = awardReportTxnBean.getApprovedForeignTripForMan(mitAwardNumber);

            //getting the valid basis of payment
            CoeusVector cvValidbasis = awardReportTxnBean.getValidBasisOfPayment();

            //Disclosure Notice
            //Check for any OSP right for the user
            // To access the user rights
            AwardDisclosureType awardDisclosureType=null;
            UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
            boolean hasAnyOSPRight = userMaintDataTxnBean.getUserHasAnyOSPRight(loggedInUser);
            if (hasAnyOSPRight) {
                //coeusdev-980
                //for award validation need to check osp right?
                AwardUpdateTxnBean awardUpdateTxnBean = new AwardUpdateTxnBean(loggedInUser);
                String awardValidation = coeusFunctions.getParameterValue("ENABLE_AWARD_VALIDATIONS");
                awardDisclosureType = new AwardDisclosureTypeImpl();
                awardDisclosureType.setAwardHeader(awardHeaderType);
                awardDisclosureType.setEnableAwardValidation(awardValidation);
                if (awardValidation.equals("1")){
                   if (awardReportTxnBean.getIsAllDiscStatusComplete(mitAwardNumber, 1) != 1 ){
                       awardDisclosureType.setDisclosureValidation("1");
                       CoeusVector cvCoiDisclForItem = awardReportTxnBean.getCoiDisclForAward(mitAwardNumber,1);

                      if (cvCoiDisclForItem!=null && cvCoiDisclForItem.size()>0) {
                        CoiDisclForItemBean coiDisclForItemBean;
                        for (int index = 0; index < cvCoiDisclForItem.size(); index++) {
                        coiDisclForItemBean = (CoiDisclForItemBean)cvCoiDisclForItem.get(index);
                        DisclosureItemType disclosureItemType = new DisclosureItemTypeImpl();
                        disclosureItemType.setDisclosureNumber(coiDisclForItemBean.getCoiDisclosureNumber());
                        disclosureItemType.setDisclosureStatusCode(coiDisclForItemBean.getCoiDisclosureStatusCode());
                        disclosureItemType.setDisclosureStatusDesc(coiDisclForItemBean.getCoiDisclosureStatusCode());
                        disclosureItemType.setDisclosureTypeCode(coiDisclForItemBean.getDisclosureType());
                        disclosureItemType.setDisclosureTypeDesc(coiDisclForItemBean.getDisclosureType());
                        disclosureItemType.setPersonId(coiDisclForItemBean.getPersonId());
                        disclosureItemType.setPersonName(coiDisclForItemBean.getPersonId());
                        awardDisclosureType.getDisclosureItem().add(disclosureItemType);
                        }
                      }

                   }else {
                       awardDisclosureType.setDisclosureValidation("0");
                   }
//
                    Vector vecValidations = awardUpdateTxnBean.performAwardValidations(mitAwardNumber);
                    if (vecValidations != null && vecValidations.size() > 0){
                        for (int index = 0; index < vecValidations.size(); index++) {
                            AwardValidationType awardValidationType = new AwardValidationTypeImpl();
                            awardValidationType.setValidationDetails(vecValidations.get(index).toString());
                            awardDisclosureType.getAwardValidation().add(awardValidationType);

                        }
                    }
                }

                /*if (awardReportTxnBean.getIsAllDiscStatusComplete(mitAwardNumber, 1) != 1 ){
                    CoeusVector cvCoiDisclForItem = awardReportTxnBean.getCoiDisclForAward(mitAwardNumber,1);

                    if (cvCoiDisclForItem!=null && cvCoiDisclForItem.size()>0) {

                        awardDisclosureType = new AwardDisclosureTypeImpl();
                        awardDisclosureType.setAwardHeader(awardHeaderType);
                        CoiDisclForItemBean coiDisclForItemBean;
                        for (int index = 0; index < cvCoiDisclForItem.size(); index++) {
                        coiDisclForItemBean = (CoiDisclForItemBean)cvCoiDisclForItem.get(index);
                        DisclosureItemType disclosureItemType = new DisclosureItemTypeImpl();
                        disclosureItemType.setDisclosureNumber(coiDisclForItemBean.getCoiDisclosureNumber());
                        disclosureItemType.setDisclosureStatusCode(coiDisclForItemBean.getCoiDisclosureStatusCode());
                        disclosureItemType.setDisclosureStatusDesc(coiDisclForItemBean.getCoiDisclosureStatusCode());
                        disclosureItemType.setDisclosureTypeCode(coiDisclForItemBean.getDisclosureType());
                        disclosureItemType.setDisclosureTypeDesc(coiDisclForItemBean.getDisclosureType());
                        disclosureItemType.setPersonId(coiDisclForItemBean.getPersonId());
                        disclosureItemType.setPersonName(coiDisclForItemBean.getPersonId());
                        awardDisclosureType.getDisclosureItem().add(disclosureItemType);
                        }
                    }
                }*/
                 //coeusdev-980
            }

            // get the user

            PersonInfoBean personBean = awardReportTxnBean.getAoInfo(unitNumber);
            AwardNoticeType.AODetailsType aODetailsType = new AwardNoticeTypeImpl.AODetailsTypeImpl();
            if (personBean.getOffLocation()!=null) {
                aODetailsType.setAOAddress(personBean.getOffLocation());
            }
            if (personBean.getFullName()!=null) {
                aODetailsType.setAOName(personBean.getFullName());
            }
            //aODetailsType.setAONumber(personBean.get);

             //start case 2010
            // Commented for Case 2355 - Award Notice Enhancement - Custom Elements are grouped by groups names - Start
//           if (awardNotice.getPrintRequirement().getOtherDataRequired().equals("1")){
//            CoeusVector cvOtherData = (CoeusVector)awardTxnBean.getAwardCustomData(mitAwardNumber);
//
//            AwardCustomDataBean awardCustomDataBean;
//            AwardType.AwardOtherDatasType.OtherDataType otherDataType;
//            AwardType.AwardOtherDatasType awardOtherDatasType = new AwardTypeImpl.AwardOtherDatasTypeImpl();
//
//
//            awardType.setAwardOtherDatas(awardOtherDatasType);
//            if (cvOtherData != null && cvOtherData.size() > 0 ) {
//                for (int idexData = 0 ; idexData < cvOtherData.size(); idexData++){
//                    otherDataType = new AwardTypeImpl.AwardOtherDatasTypeImpl.OtherDataTypeImpl();
//                    awardCustomDataBean = (AwardCustomDataBean)cvOtherData.get(idexData);
//                    otherDataType.setColumnName(UtilFactory.convertNull(awardCustomDataBean.getColumnLabel()));
//                    if (awardCustomDataBean.getColumnValue() != null && awardCustomDataBean.getDescription()== null){
//                        otherDataType.setColumnValue(UtilFactory.convertNull(awardCustomDataBean.getColumnValue()));
//                    }else {
//                        otherDataType.setColumnValue(UtilFactory.convertNull(awardCustomDataBean.getDescription()));
//                    }
//                    awardType.getAwardOtherDatas().getOtherData().add(otherDataType);
//
//                }
//            }
//           }
            //end case 2010
            // Commented for Case 2355 - Award Notice Enhancement - Custom Elements are grouped by groups names - End


             // Added for Case 2355 - Award Notice Enhancement - Custom Elements are grouped by groups names - Start
            // Modified the implementation, OtherGroupType is added to Other Details, OtherDetails element is added to Other Data and Other Data is added to AwardOther Datas.
            if (awardNotice.getPrintRequirement().getOtherDataRequired().equals("1")){
                CoeusVector cvOtherData = (CoeusVector)awardTxnBean.getAwardCustomData(mitAwardNumber);
                AwardCustomDataBean awardCustomDataBean;
                AwardType.AwardOtherDatasType awardOtherDatasType = new AwardTypeImpl.AwardOtherDatasTypeImpl();
                awardType.setAwardOtherDatas(awardOtherDatasType);
                AwardType.AwardOtherDatasType.OtherDataType otherDataType = new AwardTypeImpl.AwardOtherDatasTypeImpl.OtherDataTypeImpl();
                CustomElementsInfoBean customElementsInfoBean = new CustomElementsInfoBean();
                OtherGroupType otherGroupType ;
                OtherGroupDetailsType otherGroupDetailsType;
                AwardCustomDataBean distinctCustomDataBean;
                if (cvOtherData != null && cvOtherData.size() > 0 ) {
                    Vector cvDistinctData = getDistinctGroups(cvOtherData);
                    for(int index = 0; index < cvDistinctData.size(); index++){
                        otherGroupType = new OtherGroupTypeImpl();
                        distinctCustomDataBean = (AwardCustomDataBean) cvDistinctData.get(index);
                        for (int idexData = 0 ; idexData < cvOtherData.size(); idexData++){
                            awardCustomDataBean = (AwardCustomDataBean)cvOtherData.get(idexData);
                            otherGroupDetailsType = new OtherGroupDetailsTypeImpl();
                            if(distinctCustomDataBean.getGroupCode() !=null && awardCustomDataBean.getGroupCode() !=null && distinctCustomDataBean.getGroupCode().equals(awardCustomDataBean.getGroupCode())){
                                if (awardCustomDataBean.getColumnValue() != null && awardCustomDataBean.getDescription()== null){
                                    otherGroupDetailsType.setColumnValue(UtilFactory.convertNull(awardCustomDataBean.getColumnValue()));
                                }else {
                                    otherGroupDetailsType.setColumnValue(UtilFactory.convertNull(awardCustomDataBean.getDescription()));
                                }
                                //Set Column Name
                                otherGroupDetailsType.setColumnName(UtilFactory.convertNull(awardCustomDataBean.getColumnLabel()));

                                // Set the Group Name
                                otherGroupType.setDescription(UtilFactory.convertNull(awardCustomDataBean.getGroupCode()));
                                otherGroupType.getOtherGroupDetails().add(otherGroupDetailsType);
                            }

                        }
                        otherDataType.getOtherDetails().add(otherGroupType);

                    }
                    awardType.getAwardOtherDatas().getOtherData().add(otherDataType);
                }
            }
            // Added for Case 2355 - Notice of Award Enhanment - End

            awardNotice.setAward(awardType);
            awardNotice.setAODetails(aODetailsType);
            if (awardDisclosureType!=null) {
                awardNotice.setAwardDisclosure(awardDisclosureType);
            }
            awardNotice.setSchoolInfo(schoolInfoType);

//            JAXBContext jaxbContext = JAXBContext.newInstance("edu.mit.coeus.utils.xml.bean.award");
//            Marshaller marshaller = jaxbContext.createMarshaller();
//            ObjectFactory objFactory = new ObjectFactory();
//            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            marshaller.marshal(awardNotice, byteArrayOutputStream);
//            return byteArrayOutputStream;
            return awardNotice;
        } catch (Exception e) {
            UtilFactory.log( e.getMessage(), e,"AwardStream", "getAwardNoticeReportStream");
            throw new CoeusException(e.getMessage());
        }
    } // End of getAwardNoticeReportStream


    /**
     * Method which fills the jaxb beans and creates a stream for Delta Report.
     */
    //public ByteArrayOutputStream getDeltaReportStream(String mitAwardNumber,int seqNumber,int amountSeqNumber,int currentSeqNumber,boolean isSignatureRequired,String loggedInUser)
    public Object getDeltaReportStream(String mitAwardNumber,int seqNumber,int amountSeqNumber,int currentSeqNumber,boolean isSignatureRequired,String loggedInUser)
    throws CoeusException {
        try {
            AwardType awardType = new AwardTypeImpl();
            AwardNotice awardNotice = new AwardNoticeImpl();
            AwardNoticeType.PrintRequirementType printRequirementType = new AwardNoticeTypeImpl.PrintRequirementTypeImpl();
            awardNotice.setPrintRequirement(printRequirementType);
            awardNotice.getPrintRequirement().setAddressListRequired("1");
            awardNotice.getPrintRequirement().setCloseoutRequired("1");
            awardNotice.getPrintRequirement().setCommentsRequired("1");
            awardNotice.getPrintRequirement().setCostSharingRequired("1");
            awardNotice.getPrintRequirement().setEquipmentRequired("1");
            awardNotice.getPrintRequirement().setFlowThruRequired("1");
            awardNotice.getPrintRequirement().setForeignTravelRequired("1");
            awardNotice.getPrintRequirement().setHierarchyInfoRequired("1");
            awardNotice.getPrintRequirement().setIndirectCostRequired("1");
            awardNotice.getPrintRequirement().setPaymentRequired("1");
            awardNotice.getPrintRequirement().setProposalDueRequired("1");
            awardNotice.getPrintRequirement().setScienceCodeRequired("1");

            awardNotice.getPrintRequirement().setSpecialReviewRequired("1");
            awardNotice.getPrintRequirement().setSubcontractRequired("1");
            awardNotice.getPrintRequirement().setTechnicalReportingRequired("1");
            awardNotice.getPrintRequirement().setTermsRequired("1");
            awardNotice.getPrintRequirement().setReportingRequired("1");
            Calendar currentDate = Calendar.getInstance();
            currentDate.setTime(new Date());
            awardNotice.getPrintRequirement().setCurrentDate(currentDate);
            if (isSignatureRequired) {
                awardNotice.getPrintRequirement().setSignatureRequired("1");
            } else {
                awardNotice.getPrintRequirement().setSignatureRequired("0");
            }


            AwardDeltaReportTxnBean awardDeltaReportTxnBean = new AwardDeltaReportTxnBean();
            AwardReportTxnBean awardReportTxnBean = new AwardReportTxnBean();
            String sequnceNumber = ""+seqNumber;
            //should consider with seq# to get account number.so will get account num after get awrdBean
            //int accountNumber = awardReportTxnBean.getAccountForAward(mitAwardNumber);

            //make sure using right seqNumber and AmtSequence for getting prev amountInfor.
            int seqNumForAmt = seqNumber;
            CoeusVector cvAwardDetails = awardDeltaReportTxnBean.getAwardForSeq(mitAwardNumber,sequnceNumber,""+amountSeqNumber, ""+seqNumForAmt,-1);
            if (amountSeqNumber > 1) {
                amountSeqNumber = amountSeqNumber - 1;
            } else {
                amountSeqNumber = awardDeltaReportTxnBean.getMaxAmountSeq(mitAwardNumber,""+(seqNumber-1));
                seqNumForAmt = seqNumForAmt - 1;
            }
            CoeusVector cvPrevAwardDetails = awardDeltaReportTxnBean.getAwardForSeq(mitAwardNumber,""+(seqNumber-1),""+amountSeqNumber, ""+seqNumForAmt,-1);

            AwardBean awardBean= null;
            if (cvAwardDetails != null && cvAwardDetails.size()>0) {
                awardBean = (AwardBean)cvAwardDetails.get(0);
            }
            AwardBean prevAwardBean= null;
            if (cvPrevAwardDetails != null && cvPrevAwardDetails.size()>0) {
                prevAwardBean = (AwardBean)cvPrevAwardDetails.get(0);
            }
            //get the investigators
            CoeusVector cvInvestigators = awardDeltaReportTxnBean.getAwardInvestigatorsForSeq(mitAwardNumber, sequnceNumber);
            CoeusVector cvPrevInvestigators = awardDeltaReportTxnBean.getAwardInvestigatorsForSeq(mitAwardNumber, ""+(seqNumber-1));
            AwardHeaderBean awardHeaderBean = awardBean.getAwardHeaderBean();
            AwardHeaderBean prevAwardHeaderBean = prevAwardBean.getAwardHeaderBean();

            String accountNumber = awardBean.getAccountNumber();
            //AwardHeaderType
            AwardHeaderType awardHeaderType = new AwardHeaderTypeImpl();
            awardHeaderType.setAccountNumber(accountNumber);
            //awardHeaderType.setAccountNumber(String.valueOf(accountNumber));
            awardHeaderType.setAwardNumber(mitAwardNumber);
            awardHeaderType.setModificationNumber(awardBean.getModificationNumber());
            awardHeaderType.setNSFCode(awardBean.getNsfCode());
            awardHeaderType.setNSFDescription(awardBean.getNsfDescription());

            for (int index=0;index<cvInvestigators.size();index++) {
                AwardInvestigatorsBean awardInvestigatorsBean = (AwardInvestigatorsBean)cvInvestigators.get(index);
                if (awardInvestigatorsBean.isPrincipalInvestigatorFlag()) {
                    awardHeaderType.setPIName(awardInvestigatorsBean.getPersonName());
                    break;
                }
            }
            awardHeaderType.setSequenceNumber(seqNumber);
            // Checks with previous sequence number value and if modified appends " *" to it
            //check modification
            if(awardBean.getSponsorAwardNumber() !=null) {
                if (prevAwardBean.getSponsorAwardNumber() == null || !(awardBean.getSponsorAwardNumber().equals(prevAwardBean.getSponsorAwardNumber()))) {
                    awardHeaderType.setSponsorAwardNumber(awardBean.getSponsorAwardNumber()+ " *");
                }else{
                    awardHeaderType.setSponsorAwardNumber(awardBean.getSponsorAwardNumber());
                }
            }else{
                if (prevAwardBean.getSponsorAwardNumber() != null) {
                    awardHeaderType.setSponsorAwardNumber("* ");
                }
            }

            if (awardBean.getSponsorCode() != null){
                if (prevAwardBean.getSponsorCode() == null || !(awardBean.getSponsorCode().equals(prevAwardBean.getSponsorCode()))){
                    awardHeaderType.setSponsorDescription(awardBean.getSponsorName()+" *");
                }else {
                    awardHeaderType.setSponsorDescription(awardBean.getSponsorName());
                }
            }else {
                if (prevAwardBean.getSponsorCode() != null) {
                    awardHeaderType.setSponsorDescription("* ");
                }
            }

             if (awardBean.getNsfDescription() != null){
                  if (prevAwardBean.getNsfDescription() ==null ||!(awardBean.getNsfDescription().equals(prevAwardBean.getNsfDescription()))) {
                      awardHeaderType.setNSFDescription(awardBean.getNsfDescription()+" *");
                  }else {
                      awardHeaderType.setNSFDescription(awardBean.getNsfDescription());
                  }
             }else {
                  if (prevAwardBean.getNsfDescription() != null ) {
                      awardHeaderType.setNSFDescription("* ") ;
                  }
             }

            awardHeaderType.setStatusDescription(awardBean.getStatusDescription());
            awardHeaderType.setStatusCode(""+awardBean.getStatusCode());
            if (prevAwardHeaderBean.getTitle().equals(awardHeaderBean.getTitle())) {
                awardHeaderType.setTitle(awardHeaderBean.getTitle());
            } else {
                awardHeaderType.setTitle(awardHeaderBean.getTitle() + " *");
            }
            AwardType.AwardDetailsType awardDetailsType = new AwardTypeImpl.AwardDetailsTypeImpl();

            awardType.setAwardDetails(awardDetailsType);
            awardDetailsType.setAwardHeader(awardHeaderType);
            awardType.getAwardDetails().getAwardHeader().setAccountNumber(accountNumber);
            //awardType.getAwardDetails().getAwardHeader().setAccountNumber(String.valueOf(accountNumber));
            awardType.getAwardDetails().getAwardHeader().setAwardNumber(mitAwardNumber);
            awardType.getAwardDetails().getAwardHeader().setModificationNumber(awardBean.getModificationNumber());
            awardType.getAwardDetails().getAwardHeader().setNSFCode(awardBean.getNsfCode());
            // checked the modification and set before. so can not reset here.
            //awardType.getAwardDetails().getAwardHeader().setNSFDescription(awardBean.getNsfDescription());

            for (int index=0;index<cvInvestigators.size();index++) {
                AwardInvestigatorsBean awardInvestigatorsBean = (AwardInvestigatorsBean)cvInvestigators.get(index);
                if (awardInvestigatorsBean.isPrincipalInvestigatorFlag()) {
                    awardType.getAwardDetails().getAwardHeader().setPIName(awardInvestigatorsBean.getPersonName());
                    break;
                }
            }
            awardType.getAwardDetails().getAwardHeader().setSequenceNumber(seqNumber);
            // checked the modification and set before. so can not reset here.
            //awardType.getAwardDetails().getAwardHeader().setSponsorAwardNumber(awardBean.getSponsorAwardNumber());
            awardType.getAwardDetails().getAwardHeader().setSponsorCode(awardBean.getSponsorCode());
            // checked the modification and set before. so can not reset here.
            // awardType.getAwardDetails().getAwardHeader().setSponsorDescription(awardBean.getSponsorName());
            awardType.getAwardDetails().getAwardHeader().setStatusCode(String.valueOf(awardBean.getStatusCode()));
            awardType.getAwardDetails().getAwardHeader().setStatusDescription(awardBean.getStatusDescription());
            // checked the modification and set before. so can not reset here.
            //awardType.getAwardDetails().setAwardHeader(awardHeaderType);
            //awardType.getAwardDetails().getAwardHeader().setTitle(awardHeaderBean.getTitle());

            //Getting all the commnets to a CoeusVector
            CoeusVector cvAwardCommentsForSeq = awardDeltaReportTxnBean.getAwardCommentsForSeq(mitAwardNumber, sequnceNumber);
            CoeusVector cvPrevAwardCommentsForSeq = awardDeltaReportTxnBean.getAwardCommentsForSeq(mitAwardNumber, ""+(seqNumber-1));
            //Other header details

            AwardType.AwardDetailsType.OtherHeaderDetailsType otherHeaderDetailsType =
            new AwardTypeImpl.AwardDetailsTypeImpl.OtherHeaderDetailsTypeImpl();

            //check for accountType Modification.
            if (awardHeaderBean.getAccountTypeDescription() != null){
                if (prevAwardHeaderBean.getAccountTypeDescription() == null || !(awardHeaderBean.getAccountTypeDescription().equals(prevAwardHeaderBean.getAccountTypeDescription()))){
                    otherHeaderDetailsType.setAccountTypeDesc(awardHeaderBean.getAccountTypeDescription()+" *");
                }else{
                    otherHeaderDetailsType.setAccountTypeDesc(awardHeaderBean.getAccountTypeDescription());
                }
            }else {
                if (prevAwardHeaderBean.getAccountTypeDescription() != null) {
                    otherHeaderDetailsType.setAccountTypeDesc("* ");
                }
            }
            otherHeaderDetailsType.setAccountTypeCode(""+awardHeaderBean.getAccountTypeCode());
            //otherHeaderDetailsType.setAccountTypeDesc(awardHeaderBean.getAccountTypeDescription());

            //check for activityType Modification
            if (awardHeaderBean.getActivityTypeDescription() != null) {
                if (prevAwardHeaderBean.getActivityTypeDescription() == null ||!(awardHeaderBean.getActivityTypeDescription().equals(prevAwardHeaderBean.getActivityTypeDescription()))){
                    otherHeaderDetailsType.setActivityTypeDesc(awardHeaderBean.getActivityTypeDescription()+" *");
                }else {
                    otherHeaderDetailsType.setActivityTypeDesc(awardHeaderBean.getActivityTypeDescription());
                }
            }else {
                if (prevAwardHeaderBean.getActivityTypeDescription() != null) {
                    otherHeaderDetailsType.setActivityTypeDesc("* ");
                }
            }
            otherHeaderDetailsType.setActivityTypeCode(awardHeaderBean.getActivityTypeCode());
            //otherHeaderDetailsType.setActivityTypeDesc(awardHeaderBean.getActivityTypeDescription());

            //check for awardType Modification
             if (awardHeaderBean.getAwardTypeDescription() != null) {
                if (prevAwardHeaderBean.getAwardTypeDescription() == null ||!(awardHeaderBean.getAwardTypeDescription().equals(prevAwardHeaderBean.getAwardTypeDescription()))){
                    otherHeaderDetailsType.setAwardTypeDesc(awardHeaderBean.getAwardTypeDescription()+" *");
                }else {
                    otherHeaderDetailsType.setAwardTypeDesc(awardHeaderBean.getAwardTypeDescription());
                }
            }else {
                if (prevAwardHeaderBean.getAwardTypeDescription() != null) {
                    otherHeaderDetailsType.setAwardTypeDesc("* ");
                }
            }
            otherHeaderDetailsType.setAwardTypeCode(awardHeaderBean.getAwardTypeCode());
            // otherHeaderDetailsType.setAwardTypeDesc(awardHeaderBean.getAwardTypeDescription());
            otherHeaderDetailsType.setBasisPaymentCode(String.valueOf(awardHeaderBean.getBasisOfPaymentCode()));

            DepartmentPersonTxnBean departmentPersonTxnBean =  new DepartmentPersonTxnBean();
            Vector basisPayementDescVec = departmentPersonTxnBean.getArgumentCodeDescription("BASIS OF PAYMENT");
            CoeusVector cvBasiPayementDesc = new CoeusVector();
            cvBasiPayementDesc.addAll(basisPayementDescVec);
            Equals eqBasis = new Equals("code",""+awardHeaderBean.getBasisOfPaymentCode());
            CoeusVector cvFilteredBasisPay = cvBasiPayementDesc.filter(eqBasis);
            if (cvFilteredBasisPay!=null && cvFilteredBasisPay.size()>0) {
                ComboBoxBean basisPaymemtBean= (ComboBoxBean)cvFilteredBasisPay.get(0);
                int prevBasisCode = prevAwardHeaderBean.getBasisOfPaymentCode();
                if (prevBasisCode == awardHeaderBean.getBasisOfPaymentCode()) {
                    otherHeaderDetailsType.setBasisPaymentDesc(basisPaymemtBean.getDescription());
                } else {
                    otherHeaderDetailsType.setBasisPaymentDesc("* "+basisPaymemtBean.getDescription());
                }
            }
            //check modification.
            if (awardHeaderBean.getCfdaNumber() != null && (prevAwardHeaderBean.getCfdaNumber() == null || !(awardHeaderBean.getCfdaNumber().equals(prevAwardHeaderBean.getCfdaNumber()))) ){
                otherHeaderDetailsType.setCFDANumber(awardHeaderBean.getCfdaNumber());
            }
            if (awardHeaderBean.getDfafsNumber() != null && (prevAwardHeaderBean.getDfafsNumber() == null || !(awardHeaderBean.getDfafsNumber().equals(prevAwardHeaderBean.getDfafsNumber()))) ){
                otherHeaderDetailsType.setDFAFSNumber("* " +awardHeaderBean.getDfafsNumber());
            }

            otherHeaderDetailsType.setCompetingRenewalCode(String.valueOf(awardHeaderBean.getCompetingRenewalPrpslDue()));

            //check finalInvoiceDue modification.
            if (awardHeaderBean.getFinalInvoiceDue() !=null && (prevAwardHeaderBean.getFinalInvoiceDue()==null || !(awardHeaderBean.getFinalInvoiceDue().equals(prevAwardHeaderBean.getFinalInvoiceDue())))) {
                otherHeaderDetailsType.setFinalInvoiceDue(awardHeaderBean.getFinalInvoiceDue()==null?0:awardHeaderBean.getFinalInvoiceDue().intValue());
            }
            //check invoiceCopies modification.
            if (awardHeaderBean.getInvoiceNoOfCopies() != 0 && (awardHeaderBean.getInvoiceNoOfCopies() != prevAwardHeaderBean.getInvoiceNoOfCopies())){
                otherHeaderDetailsType.setInvoiceCopies(awardHeaderBean.getInvoiceNoOfCopies());
            }
            Equals eqPrevSpecialInvoceComment = new Equals("commentCode",new Integer(1));
            CoeusVector cvFilteredPrevSpecialInvoceComment = cvPrevAwardCommentsForSeq.filter(eqPrevSpecialInvoceComment);
            AwardCommentsBean prevSpecialInvoceComment = null;
            if (cvFilteredPrevSpecialInvoceComment != null && cvFilteredPrevSpecialInvoceComment.size()>0) {
                prevSpecialInvoceComment=(AwardCommentsBean)cvFilteredPrevSpecialInvoceComment.get(0);
            }
            AwardCommentsBean awardCommentsBean = null;
            if (cvAwardCommentsForSeq != null && cvAwardCommentsForSeq.size()> 0) {
                for (int index=0;index<cvAwardCommentsForSeq.size();index++) {
                    awardCommentsBean = (AwardCommentsBean) cvAwardCommentsForSeq.get(index);
                    if (awardCommentsBean.getCommentCode() == 1) { // special invoice
                        if (prevSpecialInvoceComment != null) {
                            if (awardCommentsBean.getComments() !=null)  {
                                if (prevSpecialInvoceComment.getComments() != null && prevSpecialInvoceComment.getComments().equals(awardCommentsBean.getComments())) {
                                    otherHeaderDetailsType.setInvoiceInstructions(awardCommentsBean.getComments());
                                } else {
                                    otherHeaderDetailsType.setInvoiceInstructions("* "+awardCommentsBean.getComments());
                                }
                            }
                        } else {
                            otherHeaderDetailsType.setInvoiceInstructions("* "+awardCommentsBean.getComments());
                        }
                        break;
                    }
                }
            }
            otherHeaderDetailsType.setNonCompetingContCode(""+awardHeaderBean.getNonCompetingContPrpslDue());
            AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
            CoeusVector cvAllCompeting = awardLookUpDataTxnBean.getFrequency();

            Equals eqNoncompeting = new Equals("code",""+awardHeaderBean.getNonCompetingContPrpslDue());
            CoeusVector cvNonCompeting = cvAllCompeting.filter(eqNoncompeting);
            if (cvNonCompeting!=null && cvNonCompeting.size()>0) {
                //need to check modification.
                if (String.valueOf(prevAwardHeaderBean.getNonCompetingContPrpslDue()) == null || !(String.valueOf(prevAwardHeaderBean.getNonCompetingContPrpslDue()).equals(String.valueOf(awardHeaderBean.getNonCompetingContPrpslDue())))){
                    ComboBoxBean nonCompetingBean = (ComboBoxBean)cvNonCompeting.get(0);
                    otherHeaderDetailsType.setNonCompetingContDesc("* "+nonCompetingBean.getDescription());
                }
            }
            /**
             * if ( String.valueOf(prevAwardHeaderBean.getNonCompetingContPrpslDue()) != null) {
             * }
             * Equals eqPrevNoncompeting = new Equals("code",""+prevAwardHeaderBean.getNonCompetingContPrpslDue());
             * CoeusVector cvPrevNonCompeting = cvAllCompeting.filter(eqPrevNoncompeting);
             * if (cvPrevNonCompeting !=null && cvPrevNonCompeting.size() >0){
             * ComboBoxBean nonPrevCompetingBean = (ComboBoxBean)cvPrevNonCompeting.get(0);
             * if (!(nonPrevCompetingBean.getDescription().equals(nonCompetingBean.getDescription()))) {
             * otherHeaderDetailsType.setNonCompetingContDesc("* "+nonCompetingBean.getDescription());
             * }else {
             * otherHeaderDetailsType.setNonCompetingContDesc(nonCompetingBean.getDescription());
             * }
             *
             * }else {
             * otherHeaderDetailsType.setNonCompetingContDesc(nonCompetingBean.getDescription());
             * }
             **/

            Equals eqCompeting = new Equals("code",""+awardHeaderBean.getCompetingRenewalPrpslDue());
            CoeusVector cvCompeting = cvAllCompeting.filter(eqCompeting);
            if (cvCompeting!=null && cvCompeting.size()>0) {
                //need to check modification.
                if (String.valueOf(prevAwardHeaderBean.getCompetingRenewalPrpslDue()) == null || !(String.valueOf(prevAwardHeaderBean.getCompetingRenewalPrpslDue()).equals(String.valueOf(awardHeaderBean.getCompetingRenewalPrpslDue())))){
                    ComboBoxBean competingBean = (ComboBoxBean)cvCompeting.get(0);
                    otherHeaderDetailsType.setCompetingRenewalDesc("* "+competingBean.getDescription());
                }
            }

            otherHeaderDetailsType.setPaymentFreqCode(""+awardHeaderBean.getPaymentInvoiceFreqCode());

            Vector freqDescVec = departmentPersonTxnBean.getArgumentCodeDescription("FREQUENCY");
            CoeusVector cvFreqDesc=new CoeusVector();
            cvFreqDesc.addAll(freqDescVec);
            Equals eqFreq = new Equals("code",""+awardHeaderBean.getPaymentInvoiceFreqCode());
            CoeusVector cvFilteredFreqDesc = cvFreqDesc.filter(eqFreq);
            if (cvFilteredFreqDesc!=null && cvFilteredFreqDesc.size()>0) {
                ComboBoxBean freqDescBean= (ComboBoxBean)cvFilteredFreqDesc.get(0);
                int prevFrequencyCode = prevAwardHeaderBean.getPaymentInvoiceFreqCode();
                if (prevFrequencyCode == awardHeaderBean.getPaymentInvoiceFreqCode()) {
                    otherHeaderDetailsType.setPaymentFreqDesc(freqDescBean.getDescription());
                } else {
                    otherHeaderDetailsType.setPaymentFreqDesc("* "+freqDescBean.getDescription());
                }
            }
            otherHeaderDetailsType.setPaymentMethodCode(""+awardHeaderBean.getMethodOfPaymentCode());
            Vector paymentMethodDescVec = departmentPersonTxnBean.getArgumentCodeDescription("METHOD OF PAYMENT");
            CoeusVector cvPaymentMethodDesc=new CoeusVector();
            cvPaymentMethodDesc.addAll(paymentMethodDescVec);
            Equals eqMethod = new Equals("code",""+awardHeaderBean.getMethodOfPaymentCode());
            CoeusVector cvFilteredPaymentMethod = cvPaymentMethodDesc.filter(eqMethod);
            if (cvFilteredPaymentMethod!=null && cvFilteredPaymentMethod.size()>0) {
                ComboBoxBean paymemtMethodBean= (ComboBoxBean)cvFilteredPaymentMethod.get(0);
                int prevMethodPaymentCode = prevAwardHeaderBean.getMethodOfPaymentCode();
                if (prevMethodPaymentCode == awardHeaderBean.getMethodOfPaymentCode()) {
                    otherHeaderDetailsType.setPaymentMethodDesc(paymemtMethodBean.getDescription());
                } else {
                    otherHeaderDetailsType.setPaymentMethodDesc("* "+paymemtMethodBean.getDescription());
                }
            }
            //need to set preAwardAuthorizedAmt modified node and PreAwardEffectiveDateModified node.
            double prevPreAwardAuthorizedAmt=0.0;
            if (prevAwardHeaderBean  !=null) {
                prevPreAwardAuthorizedAmt = prevAwardHeaderBean.getPreAwardAuthorizedAmount()==null?0.0:prevAwardHeaderBean.getPreAwardAuthorizedAmount().doubleValue();
                if (awardHeaderBean.getPreAwardAuthorizedAmount()!=null && prevPreAwardAuthorizedAmt != awardHeaderBean.getPreAwardAuthorizedAmount().doubleValue()) {
                    otherHeaderDetailsType.setPreAwardAuthorizedAmtModifeid(" *");
                }
            }
            BigDecimal bdecPreAwardAuthorizedAmt = awardHeaderBean.getPreAwardAuthorizedAmount()==null?new BigDecimal(0.0):new BigDecimal(awardHeaderBean.getPreAwardAuthorizedAmount().doubleValue());
            otherHeaderDetailsType.setPreAwardAuthorizedAmt(bdecPreAwardAuthorizedAmt.setScale(2,BigDecimal.ROUND_HALF_DOWN));

            if (awardHeaderBean.getPreAwardEffectiveDate()!=null) {
                Calendar preAwardEffectiveDate = Calendar.getInstance();
                preAwardEffectiveDate.setTime(awardHeaderBean.getPreAwardEffectiveDate());
                otherHeaderDetailsType.setPreAwardEffectiveDate(preAwardEffectiveDate);
                if (prevAwardHeaderBean !=null && (prevAwardHeaderBean.getPreAwardEffectiveDate() == null ||!(prevAwardHeaderBean.getPreAwardEffectiveDate().equals(awardHeaderBean.getPreAwardEffectiveDate())))) {
                    otherHeaderDetailsType.setPreAwardEffectiveDateModifeid(" *");
                }

            }else {
                if (prevAwardHeaderBean.getPreAwardEffectiveDate() != null){
                    otherHeaderDetailsType.setPreAwardEffectiveDateModifeid(" *");
                }
            }
            //need to check modification.
            if (awardHeaderBean.getPrimeSponsorCode() !=null) {
                if (prevAwardHeaderBean.getPrimeSponsorCode()== null || !(awardHeaderBean.getPrimeSponsorCode().equals(prevAwardHeaderBean.getPrimeSponsorCode())) ){
                    otherHeaderDetailsType.setPrimeSponsorCode("* " +awardHeaderBean.getPrimeSponsorCode());
                }
            } else {
                if (prevAwardHeaderBean.getPrimeSponsorCode()!= null){
                    otherHeaderDetailsType.setPrimeSponsorCode("* ");
                }
            }
            //String primeSponsorName = awardTxnBean.getSponsorName(awardHeaderBean.getPrimeSponsorCode());

            RolodexMaintenanceDataTxnBean rolodexMaintenanceDataTxnBean = new RolodexMaintenanceDataTxnBean();
            String primeSponsorName = rolodexMaintenanceDataTxnBean.getSponsorName(awardHeaderBean.getPrimeSponsorCode());

            if (primeSponsorName!=null)  {
                otherHeaderDetailsType.setPrimeSponsorDescription(primeSponsorName);
            }
            //check modificatoin of procurementPriorityCode.
            if (awardHeaderBean.getProcurementPriorityCode()!= null &&(prevAwardHeaderBean.getProcurementPriorityCode() == null || !(awardHeaderBean.getProcurementPriorityCode().equals(prevAwardHeaderBean.getProcurementPriorityCode())))){
                otherHeaderDetailsType.setProcurementPriorityCode("* " +awardHeaderBean.getProcurementPriorityCode());
            }

            otherHeaderDetailsType.setProposalNumber(awardHeaderBean.getProposalNumber());
            otherHeaderDetailsType.setRootAccountNumber(awardReportTxnBean.getRootAccountForAward(mitAwardNumber));
            BigDecimal bdecSpecialEBRateOffCampus = awardHeaderBean.getSpecialEBRateOffCampus()==null?new BigDecimal(0.0):new BigDecimal(awardHeaderBean.getSpecialEBRateOffCampus().doubleValue());
            otherHeaderDetailsType.setSpecialEBRateOffCampus(bdecSpecialEBRateOffCampus.setScale(2,BigDecimal.ROUND_HALF_DOWN));
            BigDecimal bdecSpecialEBRateOnCampus = awardHeaderBean.getSpecialEBRateOnCampus()==null?new BigDecimal(0.0):new BigDecimal(awardHeaderBean.getSpecialEBRateOnCampus().doubleValue());
            otherHeaderDetailsType.setSpecialEBRateOnCampus(bdecSpecialEBRateOnCampus.setScale(2,BigDecimal.ROUND_HALF_DOWN));
            Equals eqSRComment = new Equals("commentCode",new Integer(7));
            CoeusVector cvSpecialRate = cvAwardCommentsForSeq.filter(eqSRComment);
            if (cvSpecialRate !=null && cvSpecialRate.size()>0) {
                AwardCommentsBean specialRateDescBean = (AwardCommentsBean)cvSpecialRate.get(0);
                //check the seq#.
                if (specialRateDescBean.getSequenceNumber() == seqNumber){
                    otherHeaderDetailsType.setSpecialRateComments(specialRateDescBean.getComments());
                }

            }
            //check modification of subPlan
            if (awardHeaderBean.getSubPlanFlag() != null && (prevAwardHeaderBean.getSubPlanFlag()== null || !(awardHeaderBean.getSubPlanFlag().equals(prevAwardHeaderBean.getSubPlanFlag())))) {
                otherHeaderDetailsType.setSubPlan(awardHeaderBean.getSubPlanFlag());
            }

            //set fellowshipAdminName
            otherHeaderDetailsType.setFellowshipAdminName(awardReportTxnBean.getParameterValue("FELLOWSHIP_OSP_ADMIN"));

            awardType.getAwardDetails().setOtherHeaderDetails(otherHeaderDetailsType);

            AwardDetailsBean awardDetailsBean = awardBean.getAwardDetailsBean();
            AwardDetailsBean prevAwardDetailsBean = prevAwardBean.getAwardDetailsBean();
            //AwardDetails
            awardDetailsType.setApprvdEquipmentIndicator(awardBean.getApprvdEquipmentIndicator());
            awardDetailsType.setApprvdForeginTripIndicator(awardBean.getApprvdForeignTripIndicator());
            awardDetailsType.setApprvdSubcontractIndicator(awardBean.getApprvdSubcontractIndicator());
            awardDetailsType.setAwardHeader(awardHeaderType);
            awardDetailsType.setCostSharingIndicator(awardBean.getCostSharingIndicator());

            if (awardDetailsBean.getAwardEffectiveDate()!=null) {
                Calendar effectiveDate = Calendar.getInstance();
                effectiveDate.setTime(awardDetailsBean.getAwardEffectiveDate());
                awardDetailsType.setEffectiveDate(effectiveDate);
                if (prevAwardDetailsBean.getAwardEffectiveDate() == null || !(prevAwardDetailsBean.getAwardEffectiveDate().equals(awardDetailsBean.getAwardEffectiveDate()))){
                    awardDetailsType.setEffectiveDateModified(" *");
                }
            }else{
                if (prevAwardDetailsBean.getAwardEffectiveDate() != null){
                    awardDetailsType.setEffectiveDateModified(" *");
                }
            }

            //case 2431 begin
            if (awardDetailsBean.getBeginDate()!=null) {
                Calendar beginDate = Calendar.getInstance();
                beginDate.setTime(awardDetailsBean.getBeginDate());
                awardDetailsType.setBeginDate(beginDate);
                if (prevAwardDetailsBean.getBeginDate() == null || !(prevAwardDetailsBean.getBeginDate().equals(awardDetailsBean.getBeginDate()))){
                    awardDetailsType.setBeginDateModified(" *");
                }
            }else{
                if (prevAwardDetailsBean.getBeginDate() != null){
                    awardDetailsType.setBeginDateModified(" *");
                }
            }
            //case 2431 end

            if (awardDetailsBean.getAwardExecutionDate()!=null) {
                Calendar executionDate = Calendar.getInstance();
                executionDate.setTime(awardDetailsBean.getAwardExecutionDate());
                awardDetailsType.setExecutionDate(executionDate);
                //start case 1833
//                if (prevAwardDetailsBean.getAwardExecutionDate() == null && !(prevAwardDetailsBean.getAwardExecutionDate().equals(awardDetailsBean.getAwardExecutionDate()))) {
//                    awardDetailsType.setExecutionDateModified(" *");
//                }
                if (prevAwardDetailsBean.getAwardExecutionDate() == null || !(prevAwardDetailsBean.getAwardExecutionDate().equals(awardDetailsBean.getAwardExecutionDate()))) {
                    awardDetailsType.setExecutionDateModified(" *");
                }

                //end case 1833
            }else {
                if (prevAwardDetailsBean.getAwardExecutionDate() != null) {
                    awardDetailsType.setExecutionDateModified(" *");
                }
            }


            awardDetailsType.setIDCIndicator(awardBean.getIdcIndicator());
            awardDetailsType.setOtherHeaderDetails(otherHeaderDetailsType);
            awardDetailsType.setPaymentScheduleIndicator(awardBean.getPaymentScheduleIndicator());
            awardDetailsType.setScienceCodeIndicator(awardBean.getScienceCodeIndicator());
            awardDetailsType.setSpecialReviewIndicator(awardBean.getSpecialReviewIndicator());
            awardDetailsType.setTransferSponsorIndicator(awardBean.getTransferSponsorIndicator());

            // Money and End Dates
            //getMoneyAndEndDatesTree
            AwardAmountInfoBean awardAmountInfoBean;
            awardAmountInfoBean= new AwardAmountInfoBean();
            awardAmountInfoBean.setMitAwardNumber(mitAwardNumber);

            CoeusVector cvAmountInfo = (CoeusVector)awardBean.getAwardAmountInfo();
            CoeusVector cvPrevAmountInfo = (CoeusVector)prevAwardBean.getAwardAmountInfo();
            awardAmountInfoBean = (AwardAmountInfoBean)cvAmountInfo.get(0);
            AwardAmountInfoBean prevAwardAmountInfoBean = null;
            if (cvPrevAmountInfo !=null && cvPrevAmountInfo.size()>0) {
                prevAwardAmountInfoBean = (AwardAmountInfoBean)cvPrevAmountInfo.get(0);
            }

            AmountInfoType amountInfoType = new AmountInfoTypeImpl();
            amountInfoType.setAmountSequenceNumber(seqNumber);
            BigDecimal bdecAmtObligatedToDate = new BigDecimal(awardAmountInfoBean.getAmountObligatedToDate());
            amountInfoType.setAmtObligatedToDate(bdecAmtObligatedToDate.setScale(2,BigDecimal.ROUND_HALF_DOWN));
            BigDecimal bdecAnticipatedChange = new BigDecimal(awardAmountInfoBean.getAnticipatedChange());
            amountInfoType.setAnticipatedChange(bdecAnticipatedChange.setScale(2,BigDecimal.ROUND_HALF_DOWN));
            BigDecimal bdecAnticipatedDistributableAmt = new BigDecimal(awardAmountInfoBean.getAnticipatedDistributableAmount());
            amountInfoType.setAnticipatedDistributableAmt(bdecAnticipatedDistributableAmt.setScale(2,BigDecimal.ROUND_HALF_DOWN));
            /** check it in stylesheet Designer
            double prevAnticipatedTotalAmt=0.0;
            if (prevAwardAmountInfoBean !=null) {
                prevAnticipatedTotalAmt = prevAwardAmountInfoBean.getAnticipatedTotalAmount();
                if (prevAnticipatedTotalAmt != awardAmountInfoBean.getAnticipatedTotalAmount()) {
                    amountInfoType.setAnticipatedTotalAmtModified(" *");
                }
            }
             **/
            BigDecimal bdecAnticipatedTotalAmt = new BigDecimal(awardAmountInfoBean.getAnticipatedTotalAmount());
            amountInfoType.setAnticipatedTotalAmt(bdecAnticipatedTotalAmt.setScale(2,BigDecimal.ROUND_HALF_DOWN));
            amountInfoType.setAwardNumber(mitAwardNumber);
            Calendar currentFundEffectiveDate =null;
            if (awardAmountInfoBean.getCurrentFundEffectiveDate()!=null) {
                currentFundEffectiveDate = Calendar.getInstance();
                currentFundEffectiveDate.setTime(awardAmountInfoBean.getCurrentFundEffectiveDate());
                amountInfoType.setCurrentFundEffectiveDate(currentFundEffectiveDate);
                //set modified node
                if (prevAwardAmountInfoBean !=null && (prevAwardAmountInfoBean.getCurrentFundEffectiveDate()== null || !(prevAwardAmountInfoBean.getCurrentFundEffectiveDate().equals(awardAmountInfoBean.getCurrentFundEffectiveDate())))) {
                    amountInfoType.setCurrentFundEffectiveDateModified(" *");
                }
//                if (prevAwardAmountInfoBean !=null) {
//                    if (!(prevAwardAmountInfoBean.getCurrentFundEffectiveDate() !=null && prevAwardAmountInfoBean.getCurrentFundEffectiveDate().equals(awardAmountInfoBean.getCurrentFundEffectiveDate()))) {
//                        amountInfoType.setCurrentFundEffectiveDateModified(" *");
//                    }
//                }
            }else {
                if (prevAwardAmountInfoBean !=null && prevAwardAmountInfoBean.getCurrentFundEffectiveDate()!= null){
                    amountInfoType.setCurrentFundEffectiveDateModified(" *");
                }
            }
            amountInfoType.setEOMProcess(true);
            amountInfoType.setEntryType(awardAmountInfoBean.getEntryType());
            Calendar finalExpirationDate= null;
            if (awardAmountInfoBean.getFinalExpirationDate()!=null) {
                Date finalExp= awardAmountInfoBean.getFinalExpirationDate();
                finalExpirationDate = Calendar.getInstance();
                finalExpirationDate.setTime(finalExp);
                amountInfoType.setFinalExpirationDate(finalExpirationDate);
                if (prevAwardAmountInfoBean !=null && (prevAwardAmountInfoBean.getFinalExpirationDate() == null ||!(prevAwardAmountInfoBean.getFinalExpirationDate().equals(finalExp)))) {
                    amountInfoType.setFinalExpDateModified(" *");
                }
            }else{
                if (prevAwardAmountInfoBean !=null && prevAwardAmountInfoBean.getFinalExpirationDate() != null) {
                    amountInfoType.setFinalExpDateModified(" *");
                }
            }
            BigDecimal bdecObligatedChange = new BigDecimal(awardAmountInfoBean.getObligatedChange());
            amountInfoType.setObligatedChange(bdecObligatedChange.setScale(2,BigDecimal.ROUND_HALF_DOWN));
            double prevObligatedDistributableAmt = 0.0;
            if (prevAwardAmountInfoBean !=null) {
                prevObligatedDistributableAmt = prevAwardAmountInfoBean.getObliDistributableAmount();
                if (prevObligatedDistributableAmt != awardAmountInfoBean.getObliDistributableAmount()) {
                    amountInfoType.setObligatedDistributableAmtModified(" *");
                }
            }
            BigDecimal bdecObligatedDistributableAmt = new BigDecimal(awardAmountInfoBean.getObliDistributableAmount());
            amountInfoType.setObligatedDistributableAmt(bdecObligatedDistributableAmt.setScale(2,BigDecimal.ROUND_HALF_DOWN));
            Calendar obligationExpirationDate = null;
            if (awardAmountInfoBean.getObligationExpirationDate()!=null) {
                obligationExpirationDate = Calendar.getInstance();
                obligationExpirationDate.setTime(awardAmountInfoBean.getObligationExpirationDate());
                amountInfoType.setObligationExpirationDate(obligationExpirationDate);
                if (prevAwardAmountInfoBean !=null &&(prevAwardAmountInfoBean.getObligationExpirationDate() == null || !(prevAwardAmountInfoBean.getObligationExpirationDate().equals(awardAmountInfoBean.getObligationExpirationDate())))) {
                    amountInfoType.setObligationExpDateModified(" *");
                }
            }else {
                if (prevAwardAmountInfoBean !=null && prevAwardAmountInfoBean.getObligationExpirationDate() != null){
                    amountInfoType.setObligationExpDateModified(" *");
                }
            }

            amountInfoType.setSequenceNumber(seqNumber);
            amountInfoType.setTransactionId(awardAmountInfoBean.getTransactionId());
              // Added for Case 4416 - Change NOA and delta stylesheets to include data items related to transaction type - Start
            CoeusFunctions coeusFunctions = new CoeusFunctions();
            String directIndirectParamValue = "0";
            directIndirectParamValue = coeusFunctions.getParameterValue("ENABLE_AWD_ANT_OBL_DIRECT_INDIRECT_COST");

            BigDecimal bdecObligatedChangeDirect = new BigDecimal(awardAmountInfoBean.getDirectObligatedChange());
            amountInfoType.setObligatedChangeDirect(bdecObligatedChangeDirect.setScale(2,BigDecimal.ROUND_HALF_DOWN));

            BigDecimal bdecObligatedChangeIndirect = new BigDecimal(awardAmountInfoBean.getIndirectObligatedChange());
            amountInfoType.setObligatedChangeIndirect(bdecObligatedChangeIndirect.setScale(2,BigDecimal.ROUND_HALF_DOWN));

            BigDecimal bdecAnticipatedChangeDirect = new BigDecimal(awardAmountInfoBean.getDirectAnticipatedChange());
            amountInfoType.setAnticipatedChangeDirect(bdecAnticipatedChangeDirect.setScale(2,BigDecimal.ROUND_HALF_DOWN));

            BigDecimal bdecAnticipatedChangeIndirect = new BigDecimal(awardAmountInfoBean.getIndirectAnticipatedChange());
            amountInfoType.setAnticipatedChangeIndirect(bdecAnticipatedChangeIndirect.setScale(2,BigDecimal.ROUND_HALF_DOWN));

            BigDecimal bdecAnticipatedTotalDirect = new BigDecimal(awardAmountInfoBean.getDirectAnticipatedTotal());
            amountInfoType.setAnticipatedTotalDirect(bdecAnticipatedTotalDirect.setScale(2,BigDecimal.ROUND_HALF_DOWN));

            BigDecimal bdecAnticipatedTotalIndirect = new BigDecimal(awardAmountInfoBean.getIndirectAnticipatedTotal());
            amountInfoType.setAnticipatedTotalIndirect(bdecAnticipatedTotalIndirect.setScale(2,BigDecimal.ROUND_HALF_DOWN));

            BigDecimal bdecObligatedTotalDirect = new BigDecimal(awardAmountInfoBean.getDirectObligatedTotal());
            amountInfoType.setObligatedTotalDirect(bdecObligatedTotalDirect.setScale(2,BigDecimal.ROUND_HALF_DOWN));

            BigDecimal bdecObligatedTotalIndirect = new BigDecimal(awardAmountInfoBean.getIndirectObligatedTotal());
            amountInfoType.setObligatedTotalIndirect(bdecObligatedTotalIndirect.setScale(2,BigDecimal.ROUND_HALF_DOWN));

            amountInfoType.setEnableAwdAntOblDirectIndirectCost(directIndirectParamValue);

             // Added for Case 4416 - Change NOA and delta stylesheets to include data items related to transaction type - End

            AwardType.AwardAmountInfoType awardAmountInfoType = new AwardTypeImpl.AwardAmountInfoTypeImpl();
            awardType.setAwardAmountInfo(awardAmountInfoType);
            awardType.getAwardAmountInfo().getAmountInfo().add(amountInfoType);
            // Added for Case 4416 - Change NOA and delta stylesheets to include data items related to transaction type - Start

            AwardAmountTransactionBean awardAmountTransactionBean = awardAmountInfoBean.getAwardAmountTransaction();
            AwardTransactionType awardTransactionType = new AwardTransactionTypeImpl();
            awardTransactionType.setAwardNumber(awardAmountTransactionBean.getMitAwardNumber());
            awardTransactionType.setTransactionTypeCode(awardAmountTransactionBean.getTransactionTypeCode());
            awardTransactionType.setTransactionTypeDesc(awardAmountTransactionBean.getTransactionTypeDescription());
            awardTransactionType.setComments(awardAmountTransactionBean.getComments());
            Calendar calNoticeDate =null;
            if (awardAmountTransactionBean.getNoticeDate()!=null) {
                calNoticeDate = Calendar.getInstance();
                calNoticeDate.setTime(awardAmountTransactionBean.getNoticeDate());
                awardTransactionType.setNoticeDate(calNoticeDate);
            }
            AwardType.AwardTransactionInfoType awardTransactionInfoType = new AwardTypeImpl.AwardTransactionInfoTypeImpl();
            awardType.setAwardTransactionInfo(awardTransactionInfoType);
            awardType.getAwardTransactionInfo().getTransactionInfo().add(awardTransactionType);

            // Added for Case 4416 - Change NOA and delta stylesheets to include data items related to transaction type - End

            //Money and End Dates

            //InvestigatorType
            InvestigatorType investigatorType;
            AwardType.AwardInvestigatorsType awardInvestigatorsType = new AwardTypeImpl.AwardInvestigatorsTypeImpl();
            awardType.setAwardInvestigators(awardInvestigatorsType);

            CoeusVector cvUnits = awardDeltaReportTxnBean.getAwardUnitsForSeq(mitAwardNumber, sequnceNumber);
            CoeusVector cvPrevUnits = awardDeltaReportTxnBean.getAwardUnitsForSeq(mitAwardNumber, ""+(seqNumber-1));
            String unitNumber=null;
            PersonInfoTxnBean personInfoTxnBean = new PersonInfoTxnBean();
            for (int indx = 0;indx<cvInvestigators.size();indx++) {
                AwardInvestigatorsBean awardInvestigatorsBean = (AwardInvestigatorsBean) cvInvestigators.get(indx);
                investigatorType = new InvestigatorTypeImpl();
                investigatorType.setAwardNumber(mitAwardNumber);
                investigatorType.setCOIFlag(true);
                investigatorType.setFEDRDEBRFlag(awardInvestigatorsBean.isFedrDebrFlag());
                investigatorType.setFEDRDELQFlag(awardInvestigatorsBean.isFedrDelqFlag());
                investigatorType.setFaculty(awardInvestigatorsBean.isFacultyFlag());
                investigatorType.setNonEmployee(awardInvestigatorsBean.isNonMITPersonFlag());
                BigDecimal bdecPercentEffort = new BigDecimal(awardInvestigatorsBean.getPercentageEffort());
                investigatorType.setPercentEffort(bdecPercentEffort.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                PersonInfoFormBean personInfoFormBean = personInfoTxnBean.getPersonInfo(awardInvestigatorsBean.getPersonId());
                investigatorType.setPersonAddress(personInfoFormBean.getOffLocation());
                investigatorType.setPersonId(awardInvestigatorsBean.getPersonId());
                StringBuffer personName = new StringBuffer();
                personName.append(awardInvestigatorsBean.getPersonName());
                Equals eqPrevInvestigator = new Equals("personId",awardInvestigatorsBean.getPersonId());
                CoeusVector cvFilteredPrevInvestigator = cvPrevInvestigators.filter(eqPrevInvestigator);
                //Checking whether the person is a PI
                if (awardInvestigatorsBean.isPrincipalInvestigatorFlag()) {
                    personName.append(" (PI)");
                }
                if (cvFilteredPrevInvestigator.size()==0) {
                    personName.append(" *");
                }
                investigatorType.setPersonName(personName.toString());
                investigatorType.setPrincipalInvestigator(awardInvestigatorsBean.isPrincipalInvestigatorFlag());
                investigatorType.setSequenceNumber(awardInvestigatorsBean.getSequenceNumber());
                awardType.getAwardInvestigators().getInvestigator().add(investigatorType);

                Equals eqCurrentUnits = new Equals("personId",""+awardInvestigatorsBean.getPersonId());
                CoeusVector cvCurrentUnits=cvUnits.filter(eqCurrentUnits);
                CoeusVector cvPrevUnitsForCurrentPerson =cvPrevUnits.filter(eqCurrentUnits);
                AwardUnitBean awardUnitBean;
                InvestigatorUnitsType investigatorUnitsType;
                for (int idx=0;idx < cvCurrentUnits.size();idx++) {
                    awardUnitBean = (AwardUnitBean)cvCurrentUnits.get(idx);
                    investigatorUnitsType = new InvestigatorUnitsTypeImpl();
                    investigatorUnitsType.setAwardNumber(mitAwardNumber);
                    if (awardUnitBean.isLeadUnitFlag()) {
                        unitNumber = awardUnitBean.getUnitNumber();
                        //for Signature
                        //coeusdev-923 start
                        //new condition will be Account Type is “Draper Fellowship” OR Award Type is “Fellowship”
                        //OR Activity Type is “Fellowship- Pre-Doctoral” OR “Fellowship – Post-Doctoral”
                        //then set OspAdminName to FELLOWSHIP_OSP_ADMIN (contract administrator)
                         if (awardHeaderBean.getAccountTypeCode() != 3 && awardHeaderBean.getAwardTypeCode() != 7
                                && (awardHeaderBean.getActivityTypeCode()!= 3 && awardHeaderBean.getActivityTypeCode()!= 7)) {
                        //if (awardHeaderBean.getActivityTypeCode()!= 3 && awardHeaderBean.getActivityTypeCode()!= 7){
                        //coeusdev-923 end
                            String OspAdminName = awardUnitBean.getOspAdministratorName();
                            if (OspAdminName != null){
                                String lastName = null;
                                String firstName = null;
                                String middleNmae = null;
                                int pos;
                                pos = OspAdminName.indexOf(",");
                                if (!(pos>0)) pos = OspAdminName.indexOf(" ");
                                if (pos >= 0 ){
                                    lastName = OspAdminName.substring(0,pos).trim();
                                    firstName = OspAdminName.substring(pos+1).trim();
                                }
                                if (firstName != null){
                                    pos = firstName.indexOf(" ");
                                    if (pos >= 0 ){
                                        middleNmae = " "+ firstName.substring(pos+1).trim()+ " ";
                                        firstName = firstName.substring(0,pos).trim();
                                    }else{
                                        middleNmae = " ";
                                    }
                                }
                                if (lastName!=null && firstName!= null && middleNmae!= null){
                                    OspAdminName = firstName + middleNmae + lastName;
                                }
                            }
                            otherHeaderDetailsType.setFellowshipAdminName(OspAdminName);
                            awardType.getAwardDetails().setOtherHeaderDetails(otherHeaderDetailsType);
                        }
                    }
                    investigatorUnitsType.setLeadUnit(awardUnitBean.isLeadUnitFlag());
                    investigatorUnitsType.setOSPAdminName(awardUnitBean.getOspAdministratorName());
                    investigatorUnitsType.setPersonId(awardUnitBean.getPersonId());
                    investigatorUnitsType.setSequenceNumber(awardUnitBean.getSequenceNumber());
                    investigatorUnitsType.setUnitNumber(awardUnitBean.getUnitNumber());
                    StringBuffer unitName = new StringBuffer();
                    unitName.append(awardUnitBean.getUnitName());
                    if (awardUnitBean.isLeadUnitFlag()) {
                        unitName.append(" (LU)");
                    }
                    Equals eqPrevUnit = new Equals("unitNumber",awardUnitBean.getUnitNumber());
                    CoeusVector cvPrevUnit = cvPrevUnitsForCurrentPerson.filter(eqPrevUnit);
                    if (cvPrevUnit.size()==0) {
                        unitName.append(" *");
                    }
                    investigatorUnitsType.setUnitName(unitName.toString());
                    investigatorType.getInvestigatorUnit().add(investigatorUnitsType);
                }
            }

           //Added for Case 3823 - Key Person Records Needed in Inst Proposal and Award  -start

            AwardTxnBean awardTxnBean = new AwardTxnBean();
            CoeusVector cvKeyPersons = awardTxnBean.getAwardKeyPersons(mitAwardNumber, seqNumber);
            KeyPersonType keyPersonType;
            AwardType.AwardKeyPersonsType awardKeyPersonsType = new AwardTypeImpl.AwardKeyPersonsTypeImpl();
            awardType.setAwardKeyPersons(awardKeyPersonsType);
            //MOdified for the case# COEUSDV-270-Formatting of the dollars on the printed NOA and Money and End Dates History screen
            if(cvKeyPersons != null && cvKeyPersons.size()>0){
                for (int index = 0;index<cvKeyPersons.size();index++) {
                    AwardKeyPersonBean awardKeyPersonBean = (AwardKeyPersonBean) cvKeyPersons.get(index);
                    keyPersonType = new KeyPersonTypeImpl();
                    keyPersonType.setAwardNumber(mitAwardNumber);
                    keyPersonType.setSequenceNumber(awardKeyPersonBean.getSequenceNumber());
                    keyPersonType.setPersonId(awardKeyPersonBean.getPersonId());
                    keyPersonType.setPersonName(awardKeyPersonBean.getPersonName());
                    keyPersonType.setFaculty(awardKeyPersonBean.isFacultyFlag());
                    BigDecimal bdecAmount = new BigDecimal(awardKeyPersonBean.getPercentageEffort());
                    keyPersonType.setPercentEffort(bdecAmount.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                    keyPersonType.setRoleName(awardKeyPersonBean.getProjectRole());
                    awardType.getAwardKeyPersons().getKeyPerson().add(keyPersonType);
                }
            }
            //3823 - key persons - end

            //ScienceCodeDetailType
            String scienceCodeIndicator = awardBean.getScienceCodeIndicator();
            String sciencCodePresent = scienceCodeIndicator.substring(1,2);
            if (sciencCodePresent.equals("1")) {
                CoeusVector cvScienceCode = awardDeltaReportTxnBean.getAwardScienceCodeForSeq(mitAwardNumber, sequnceNumber);
                AwardType.AwardScienceCodesType awardScienceCodesType = new AwardTypeImpl.AwardScienceCodesTypeImpl();
                awardType.setAwardScienceCodes(awardScienceCodesType);
                AwardScienceCodeBean awardScienceCodeBean;
                ScienceCodeDetailType scienceCodeDetailType;
                if(cvScienceCode!=null && cvScienceCode.size() > 0) {
                    for (int index=0;index<cvScienceCode.size();index++) {
                        awardScienceCodeBean = (AwardScienceCodeBean)cvScienceCode.get(index);
                        scienceCodeDetailType = new ScienceCodeDetailTypeImpl();

                        scienceCodeDetailType.setAwardNumber(mitAwardNumber);
                        scienceCodeDetailType.setCode(awardScienceCodeBean.getScienceCode());
                        scienceCodeDetailType.setDescription(awardScienceCodeBean.getDescription());
                        scienceCodeDetailType.setSequenceNumber(awardScienceCodeBean.getSequenceNumber());

                        awardType.getAwardScienceCodes().getScienceCodeDetail().add(scienceCodeDetailType);
                    }
                }else if(scienceCodeIndicator.charAt(0)== 'P'){
                    // set to 2 to indicater Science Code information deleted.
                    awardNotice.getPrintRequirement().setScienceCodeRequired("2"); }

            } else {
                awardNotice.getPrintRequirement().setScienceCodeRequired("0");
            }


            //getting special review
            String splReviewIndicator=awardBean.getSpecialReviewIndicator();
            String splReviewModified = splReviewIndicator.substring(1,2);
            if (splReviewModified.equals("1")) {
                CoeusVector cvSpecialReview  = awardDeltaReportTxnBean.getAwardSpecialReviewForSeq(mitAwardNumber,sequnceNumber);
                AwardType.AwardSpecialReviewsType awardSpecialReviewsType = new AwardTypeImpl.AwardSpecialReviewsTypeImpl();
                awardType.setAwardSpecialReviews(awardSpecialReviewsType);
                SpecialReviewType specialReviewType;
                AwardSpecialReviewBean awardSpecialReviewBean;
                if (cvSpecialReview!=null && cvSpecialReview.size() > 0) {
                    for (int indx=0;indx<cvSpecialReview.size();indx++) {
                        awardSpecialReviewBean = (AwardSpecialReviewBean) cvSpecialReview.get(indx);
                        specialReviewType = new SpecialReviewTypeImpl();

                           //case 4525 start

                        int linkedToIRBCode = Integer.parseInt(coeusFunctions.getParameterValue("LINKED_TO_IRB_CODE"));
                        String linkage = coeusFunctions.getParameterValue("ENABLE_PROTOCOL_TO_AWARD_LINK");

                        if(awardSpecialReviewBean != null && linkage != null){
                            if ( linkage.equals("1")){//linkage is on
                                if(awardSpecialReviewBean.getApprovalCode() == linkedToIRBCode){
                                    ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
                                    ProtocolInfoBean protocolInfoBean = protocolDataTxnBean.getProtocolInfo(awardSpecialReviewBean.getProtocolSPRevNumber());
                                    if(protocolInfoBean != null){
                                        specialReviewType.setApplicationDate(dateUtils.getCalendar(protocolInfoBean.getApplicationDate()));
                                        specialReviewType.setApprovalDate(dateUtils.getCalendar(protocolInfoBean.getApprovalDate()));
                                        specialReviewType.setApprovalTypeDesc(UtilFactory.convertNull(protocolInfoBean. getProtocolStatusDesc()));
                                    }
                                }else{
                                    specialReviewType.setApplicationDate(dateUtils.getCalendar(awardSpecialReviewBean.getApplicationDate()));
                                    specialReviewType.setApprovalDate(dateUtils.getCalendar(awardSpecialReviewBean.getApprovalDate()));
                                    specialReviewType.setApprovalTypeDesc(UtilFactory.convertNull(awardSpecialReviewBean.getApprovalDescription()));
                                }
                            }else{//linkage is off
                                specialReviewType.setApplicationDate(dateUtils.getCalendar(awardSpecialReviewBean.getApplicationDate()));
                                specialReviewType.setApprovalDate(dateUtils.getCalendar(awardSpecialReviewBean.getApprovalDate()));
                                specialReviewType.setApprovalTypeDesc(UtilFactory.convertNull(awardSpecialReviewBean.getApprovalDescription()));
                            }
                        }
                       //case 4525 comment out

                        specialReviewType.setApprovalType(awardSpecialReviewBean.getApprovalCode());
//                        specialReviewType.setApprovalTypeDesc(awardSpecialReviewBean.getApprovalDescription());
                        specialReviewType.setAwardNumber(mitAwardNumber);
                        specialReviewType.setComments(awardSpecialReviewBean.getComments());
                        specialReviewType.setProtocolNumber(awardSpecialReviewBean.getProtocolSPRevNumber());
                        specialReviewType.setReviewType(awardSpecialReviewBean.getSpecialReviewCode());
                        specialReviewType.setSequenceNumber(awardSpecialReviewBean.getSequenceNumber());
                        specialReviewType.setReviewTypeDesc(awardSpecialReviewBean.getSpecialReviewDescription());
                        awardType.getAwardSpecialReviews().getSpecialReview().add(specialReviewType);
                    }
                }
            } else {
                awardNotice.getPrintRequirement().setSpecialReviewRequired("0");
            }


            /*********************************************/


            //    if (seqNumber == currentSeqNumber) {
            //Get Equipment Approval
            AwardType.AwardTermsDetailsType awardTermsDetailsType = new AwardTypeImpl.AwardTermsDetailsTypeImpl();
            awardType.setAwardTermsDetails(awardTermsDetailsType);
            CoeusVector cvTermsData = new CoeusVector();
            CoeusVector cvTermsCodes = new CoeusVector();
            AwardTermsBean awardTermsBean = new AwardTermsBean();
            AwardTermsTxnBean awardTermsTxnBean = new AwardTermsTxnBean();
            cvTermsData = awardLookUpDataTxnBean.getEquipmentApproval();
            cvTermsCodes = awardDeltaReportTxnBean.getAwardEquipmentTermsForSeq(mitAwardNumber, sequnceNumber);
            TermType equipmentTermType = new TermTypeImpl();
            equipmentTermType.setDescription("Equipment");
            Equals eqTerms=null;
            if (cvTermsCodes!=null && cvTermsCodes.size() > 0 ) {
                // Get previous sequence number terms
                CoeusVector cvPrevTermsCodes = awardDeltaReportTxnBean.
                getAwardEquipmentTermsForSeq(mitAwardNumber, ""+(seqNumber-1));
                if (cvTermsCodes.size() ==1) { // if there is only one term
                    AwardTermsBean firstTermsBean = (AwardTermsBean)cvTermsCodes.get(0);
                    if (firstTermsBean.getTermsCode() !=1) { // the only one term available has a term code not equal to 1
                        eqTerms = new Equals("termsCode",new Integer(firstTermsBean.getTermsCode()));
                        CoeusVector cvCheckPrev = cvPrevTermsCodes.filter(eqTerms);
                        if (cvCheckPrev != null && cvCheckPrev.size()==0) { // checking whether the term was there in the previous seq number
                            CoeusVector cvFilteredEquipmentData = cvTermsData.filter(eqTerms);
                            if (cvFilteredEquipmentData != null && cvFilteredEquipmentData.size() > 0){
                                AwardTermsBean equipmentAwardTermsBean = (AwardTermsBean)cvFilteredEquipmentData.get(0);
                                //AwardType.AwardTermsDetailsType awardTermsDetailsType = new AwardTypeImpl.AwardTermsDetailsTypeImpl();
                                //awardType.setAwardTermsDetails(awardTermsDetailsType);
                                TermDetailsType equipmentTermDetailsType = new TermDetailsTypeImpl() ;
                                equipmentTermDetailsType.setAwardNumber(mitAwardNumber);
                                equipmentTermDetailsType.setSequenceNumber(equipmentAwardTermsBean.getSequenceNumber());
                                equipmentTermDetailsType.setTermCode(equipmentAwardTermsBean.getTermsCode());
                                equipmentTermDetailsType.setTermDescription("ADDED "+equipmentAwardTermsBean.getTermsDescription());
                                equipmentTermType.getTermDetails().add(equipmentTermDetailsType);
                            }
                        } //"if" for checking whether the term was present in the previoous sequence number ends
                    }// "if" for only one term available ends
                } else { //there are more than one term
                    for (int indx=0;indx<cvTermsCodes.size();indx++) {
                        awardTermsBean = (AwardTermsBean)cvTermsCodes.get(indx);
                        eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                        CoeusVector cvCheckPrev = cvPrevTermsCodes.filter(eqTerms);
                        if (cvCheckPrev == null || cvCheckPrev.size()==0) { // checking whether the term was there in the previous seq number
                            CoeusVector cvFilteredEquipmentData = cvTermsData.filter(eqTerms);
                            if (cvFilteredEquipmentData != null && cvFilteredEquipmentData.size() > 0){
                                AwardTermsBean equipmentAwardTermsBean = (AwardTermsBean)cvFilteredEquipmentData.get(0);
                                //AwardType.AwardTermsDetailsType awardTermsDetailsType = new AwardTypeImpl.AwardTermsDetailsTypeImpl();
                                //awardType.setAwardTermsDetails(awardTermsDetailsType);
                                TermDetailsType equipmentTermDetailsType = new TermDetailsTypeImpl() ;
                                equipmentTermDetailsType.setAwardNumber(mitAwardNumber);
                                equipmentTermDetailsType.setSequenceNumber(equipmentAwardTermsBean.getSequenceNumber());
                                equipmentTermDetailsType.setTermCode(equipmentAwardTermsBean.getTermsCode());
                                equipmentTermDetailsType.setTermDescription("ADDED "+equipmentAwardTermsBean.getTermsDescription());
                                equipmentTermType.getTermDetails().add(equipmentTermDetailsType);
                            }
                        }
                    } //for loop ends
                    //awardType.getAwardTermsDetails().getTerm().add(equipmentTermType);
                }  // more than one term available ends
                for (int indx=0;indx <cvPrevTermsCodes.size();indx++) {
                    awardTermsBean = (AwardTermsBean)cvPrevTermsCodes.get(indx);
                    eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                    CoeusVector cvCheck = cvTermsCodes.filter(eqTerms);
                    if (cvCheck.size()==0) { // checking whether the term is there in the current seq number
                        CoeusVector cvFilteredEquipmentData = cvTermsData.filter(eqTerms);
                        AwardTermsBean equipmentAwardTermsBean = (AwardTermsBean)cvFilteredEquipmentData.get(0);
                        //AwardType.AwardTermsDetailsType awardTermsDetailsType = new AwardTypeImpl.AwardTermsDetailsTypeImpl();
                        awardType.setAwardTermsDetails(awardTermsDetailsType);
                        TermDetailsType equipmentTermDetailsType = new TermDetailsTypeImpl() ;
                        equipmentTermDetailsType.setAwardNumber(mitAwardNumber);
                        equipmentTermDetailsType.setSequenceNumber(equipmentAwardTermsBean.getSequenceNumber());
                        equipmentTermDetailsType.setTermCode(equipmentAwardTermsBean.getTermsCode());
                        equipmentTermDetailsType.setTermDescription("DELETED "+equipmentAwardTermsBean.getTermsDescription());
                        equipmentTermType.getTermDetails().add(equipmentTermDetailsType);
                    }
                }
                if (equipmentTermType.getTermDetails()!= null && equipmentTermType.getTermDetails().size()>0) {
                    awardType.getAwardTermsDetails().getTerm().add(equipmentTermType);
               // if (awardType.getAwardTermsDetails()!=null && awardType.getAwardTermsDetails().getTerm() !=null) {
               //     awardType.getAwardTermsDetails().getTerm().add(equipmentTermType);
                }
            }//end equipment

            //Get Invention
            cvTermsData = null;
            cvTermsData = awardLookUpDataTxnBean.getInvention();
            cvTermsCodes = awardDeltaReportTxnBean.getAwardInventionTermsForSeq(mitAwardNumber, sequnceNumber);
            TermType inventionTermType = new TermTypeImpl();
            inventionTermType.setDescription("Invention");
            // invention
            if (cvTermsCodes!=null) {
                // Get previous sequence number terms
                CoeusVector cvPrevTermsCodes = awardDeltaReportTxnBean.
                getAwardInventionTermsForSeq(mitAwardNumber, ""+(seqNumber-1));
                if (cvTermsCodes.size() ==1) { // if there is only one term
                    AwardTermsBean firstTermsBean = (AwardTermsBean)cvTermsCodes.get(0);
                    if (firstTermsBean.getTermsCode() !=1) { // the only one term available has a term code not equal to 1
                        eqTerms = new Equals("termsCode",new Integer(firstTermsBean.getTermsCode()));
                        CoeusVector cvCheckPrev = cvPrevTermsCodes.filter(eqTerms);
                        if (cvCheckPrev.size()==0) { // checking whether the term was there isn the previous seq number
                            CoeusVector cvFilteredInventionData = cvTermsData.filter(eqTerms);
                            AwardTermsBean inventionTermsBean = (AwardTermsBean)cvFilteredInventionData.get(0);
                            //should not create this again.
                            //AwardType.AwardTermsDetailsType awardTermsDetailsType = new AwardTypeImpl.AwardTermsDetailsTypeImpl();
                            //awardType.setAwardTermsDetails(awardTermsDetailsType);
                            TermDetailsType inventionTermDetailsType = new TermDetailsTypeImpl() ;
                            inventionTermDetailsType.setAwardNumber(mitAwardNumber);
                            inventionTermDetailsType.setSequenceNumber(inventionTermsBean.getSequenceNumber());
                            inventionTermDetailsType.setTermCode(inventionTermsBean.getTermsCode());
                            inventionTermDetailsType.setTermDescription("ADDED "+inventionTermsBean.getTermsDescription());
                            inventionTermType.getTermDetails().add(inventionTermDetailsType);
                            //awardType.getAwardTermsDetails().getTerm().add(equipmentTermType);
                        } //"if" for checking whether the term was present in the previoous sequence number ends
                    }// "if" for only one term available ends
                } else { //there are more than one term
                    for (int indx=0;indx<cvTermsCodes.size();indx++) {
                        awardTermsBean = (AwardTermsBean)cvTermsCodes.get(indx);
                        eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                        CoeusVector cvCheckPrev = cvPrevTermsCodes.filter(eqTerms);
                        if (cvCheckPrev.size()==0) { // checking whether the term was there isn the previous seq number
                            CoeusVector cvFilteredInventionData = cvTermsData.filter(eqTerms);
                            AwardTermsBean inventionTermsBean = (AwardTermsBean)cvFilteredInventionData.get(0);
                            //should not create this again.
                            //AwardType.AwardTermsDetailsType awardTermsDetailsType = new AwardTypeImpl.AwardTermsDetailsTypeImpl();
                            //awardType.setAwardTermsDetails(awardTermsDetailsType);
                            TermDetailsType inventionTermDetailsType = new TermDetailsTypeImpl() ;
                            inventionTermDetailsType.setAwardNumber(mitAwardNumber);
                            inventionTermDetailsType.setSequenceNumber(inventionTermsBean.getSequenceNumber());
                            inventionTermDetailsType.setTermCode(inventionTermsBean.getTermsCode());
                            inventionTermDetailsType.setTermDescription("ADDED "+inventionTermsBean.getTermsDescription());
                            inventionTermType.getTermDetails().add(inventionTermDetailsType);
                        }
                    } //for loop ends
               //fix case 1728 begin
                }  // more than one term available ends
               //fix case 1728 end
                    for (int indx=0;indx <cvPrevTermsCodes.size();indx++) {
                        awardTermsBean = (AwardTermsBean)cvPrevTermsCodes.get(indx);
                        eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                        CoeusVector cvCheck = cvTermsCodes.filter(eqTerms);
                        if (cvCheck.size()==0) { // checking whether the term is there in the current seq number
                            CoeusVector cvFilteredInventionData = cvTermsData.filter(eqTerms);
                            AwardTermsBean inventionTermsBean = (AwardTermsBean)cvFilteredInventionData.get(0);
                            //should not create this again.
                            //AwardType.AwardTermsDetailsType awardTermsDetailsType = new AwardTypeImpl.AwardTermsDetailsTypeImpl();
                            //awardType.setAwardTermsDetails(awardTermsDetailsType);
                            TermDetailsType inventionTermDetailsType = new TermDetailsTypeImpl() ;
                            inventionTermDetailsType.setAwardNumber(mitAwardNumber);
                            inventionTermDetailsType.setSequenceNumber(inventionTermsBean.getSequenceNumber());
                            inventionTermDetailsType.setTermCode(inventionTermsBean.getTermsCode());
                            inventionTermDetailsType.setTermDescription("DELETED "+inventionTermsBean.getTermsDescription());
                            inventionTermType.getTermDetails().add(inventionTermDetailsType);
                        }
                    }

                    //awardType.getAwardTermsDetails().getTerm().add(equipmentTermType);
                //fix case 1728 begin
//                }  // more than one term available ends
               //fix case 1728 end
                //eqTerms = new Equals("termsCode",new Integer(firstTermsBean.getTermsCode()));
                //CoeusVector cvCheckPrev = cvPrevTermsCodes.filter(eqTerms);
                if (inventionTermType.getTermDetails() != null && inventionTermType.getTermDetails().size()>0) {
                    awardType.getAwardTermsDetails().getTerm().add(inventionTermType);
                }
            }

            //invention

            //Get Prior Approval
            cvTermsData = null;
            cvTermsData = awardLookUpDataTxnBean.getPriorApproval();
            cvTermsCodes = awardDeltaReportTxnBean.getAwardPriorApprovalTermsForSeq(mitAwardNumber,sequnceNumber);
            TermType priorAprrovalTermType = new TermTypeImpl();
            priorAprrovalTermType.setDescription("Other Approvals/Notification");
            // other approval
            if (cvTermsCodes!=null) {
                // Get previous sequence number terms
                CoeusVector cvPrevTermsCodes = awardDeltaReportTxnBean.
                getAwardPriorApprovalTermsForSeq(mitAwardNumber, ""+(seqNumber-1));
                if (cvTermsCodes.size() ==1) { // if there is only one term
                    AwardTermsBean firstTermsBean = (AwardTermsBean)cvTermsCodes.get(0);
                    if (firstTermsBean.getTermsCode() !=1) { // the only one term available has a term code not equal to 1
                        eqTerms = new Equals("termsCode",new Integer(firstTermsBean.getTermsCode()));
                        CoeusVector cvCheckPrev = cvPrevTermsCodes.filter(eqTerms);
                        if (cvCheckPrev.size()==0) { // checking whether the term was there isn the previous seq number
                            CoeusVector cvFilteredPriorApprlData = cvTermsData.filter(eqTerms);
                            AwardTermsBean priorAprrovalTermsBean = (AwardTermsBean)cvFilteredPriorApprlData.get(0);
                            //should not create this again.
                            //AwardType.AwardTermsDetailsType awardTermsDetailsType = new AwardTypeImpl.AwardTermsDetailsTypeImpl();
                            //awardType.setAwardTermsDetails(awardTermsDetailsType);
                            TermDetailsType priorAprrovalTermDetailsType = new TermDetailsTypeImpl() ;
                            priorAprrovalTermDetailsType.setAwardNumber(mitAwardNumber);
                            priorAprrovalTermDetailsType.setSequenceNumber(priorAprrovalTermsBean.getSequenceNumber());
                            priorAprrovalTermDetailsType.setTermCode(priorAprrovalTermsBean.getTermsCode());
                            priorAprrovalTermDetailsType.setTermDescription("ADDED "+priorAprrovalTermsBean.getTermsDescription());
                            priorAprrovalTermType.getTermDetails().add(priorAprrovalTermDetailsType);
                            //awardType.getAwardTermsDetails().getTerm().add(equipmentTermType);
                        } //"if" for checking whether the term was present in the previoous sequence number ends
                    }// "if" for only one term available ends
                } else { //there are more than one term
                    for (int indx=0;indx<cvTermsCodes.size();indx++) {
                        awardTermsBean = (AwardTermsBean)cvTermsCodes.get(indx);
                        eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                        CoeusVector cvCheckPrev = cvPrevTermsCodes.filter(eqTerms);
                        if (cvCheckPrev.size()==0) { // checking whether the term was there isn the previous seq number
                            CoeusVector cvFilteredPriorApprlData = cvTermsData.filter(eqTerms);
                            AwardTermsBean priorAprrovalTermsBean = (AwardTermsBean)cvFilteredPriorApprlData.get(0);
                            //should not create this again.
                            //AwardType.AwardTermsDetailsType awardTermsDetailsType = new AwardTypeImpl.AwardTermsDetailsTypeImpl();
                            //awardType.setAwardTermsDetails(awardTermsDetailsType);
                            TermDetailsType priorAprrovalTermDetailsType = new TermDetailsTypeImpl() ;
                            priorAprrovalTermDetailsType.setAwardNumber(mitAwardNumber);
                            priorAprrovalTermDetailsType.setSequenceNumber(priorAprrovalTermsBean.getSequenceNumber());
                            priorAprrovalTermDetailsType.setTermCode(priorAprrovalTermsBean.getTermsCode());
                            priorAprrovalTermDetailsType.setTermDescription("ADDED "+priorAprrovalTermsBean.getTermsDescription());
                            priorAprrovalTermType.getTermDetails().add(priorAprrovalTermDetailsType);
                        }
                    } //for loop ends
               //fix case 1728 begin
                }  // more than one term available ends
               //fix case 1728 end
                    for (int indx=0;indx <cvPrevTermsCodes.size();indx++) {
                        awardTermsBean = (AwardTermsBean)cvPrevTermsCodes.get(indx);
                        eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                        CoeusVector cvCheck = cvTermsCodes.filter(eqTerms);
                        if (cvCheck.size()==0) { // checking whether the term is there in the current seq number
                            CoeusVector cvFilteredPriorApprlData = cvTermsData.filter(eqTerms);
                            AwardTermsBean priorAprrovalTermsBean = (AwardTermsBean)cvFilteredPriorApprlData.get(0);
                            //should not create this again.
                            //AwardType.AwardTermsDetailsType awardTermsDetailsType = new AwardTypeImpl.AwardTermsDetailsTypeImpl();
                            //awardType.setAwardTermsDetails(awardTermsDetailsType);
                            TermDetailsType priorAprrovalTermDetailsType = new TermDetailsTypeImpl() ;
                            priorAprrovalTermDetailsType.setAwardNumber(mitAwardNumber);
                            priorAprrovalTermDetailsType.setSequenceNumber(priorAprrovalTermsBean.getSequenceNumber());
                            priorAprrovalTermDetailsType.setTermCode(priorAprrovalTermsBean.getTermsCode());
                            priorAprrovalTermDetailsType.setTermDescription("DELETED "+priorAprrovalTermsBean.getTermsDescription());
                            priorAprrovalTermType.getTermDetails().add(priorAprrovalTermDetailsType);
                        }
                    }

                    //awardType.getAwardTermsDetails().getTerm().add(equipmentTermType);
                //fix case 1728 begin
//                }  // more than one term available ends
               //fix case 1728 end
                //eqTerms = new Equals("termsCode",new Integer(firstTermsBean.getTermsCode()));
                //CoeusVector cvCheckPrev = cvPrevTermsCodes.filter(eqTerms);
                if (priorAprrovalTermType.getTermDetails() != null && priorAprrovalTermType.getTermDetails().size()>0) {
                    awardType.getAwardTermsDetails().getTerm().add(priorAprrovalTermType);
                }
            }

            //other approval

                /*if (cvTermsCodes !=null) {
                    for (int indx=0;indx<cvTermsCodes.size();indx++) {
                        awardTermsBean = (AwardTermsBean)cvTermsCodes.get(indx);
                        eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                        CoeusVector cvFilteredPriorApprlData = cvTermsData.filter(eqTerms);
                        AwardTermsBean priorAprrovalTermsBean = (AwardTermsBean)cvFilteredPriorApprlData.get(0);

                        TermDetailsType priorAprrovalTermDetailsType = new TermDetailsTypeImpl() ;
                        priorAprrovalTermDetailsType.setAwardNumber(mitAwardNumber);
                        priorAprrovalTermDetailsType.setSequenceNumber(seqNumber);
                        priorAprrovalTermDetailsType.setTermCode(priorAprrovalTermsBean.getTermsCode());
                        priorAprrovalTermDetailsType.setTermDescription(priorAprrovalTermsBean.getTermsDescription());
                        priorAprrovalTermType.getTermDetails().add(priorAprrovalTermDetailsType);
                    }

                    awardType.getAwardTermsDetails().getTerm().add(priorAprrovalTermType);
                }*/


            //Get Property Terms
            cvTermsData = null;
            cvTermsData = awardLookUpDataTxnBean.getProperty();
            cvTermsCodes = awardDeltaReportTxnBean.getAwardPropertyTermsForSeq(mitAwardNumber, sequnceNumber);
            TermType propertyTermType = new TermTypeImpl();
            propertyTermType.setDescription("Property");

            //Property
            if (cvTermsCodes!=null) {
                // Get previous sequence number terms
                CoeusVector cvPrevTermsCodes = awardDeltaReportTxnBean.
                getAwardPropertyTermsForSeq(mitAwardNumber, ""+(seqNumber-1));
                if (cvTermsCodes.size() ==1) { // if there is only one term
                    AwardTermsBean firstTermsBean = (AwardTermsBean)cvTermsCodes.get(0);
                    if (firstTermsBean.getTermsCode() !=1) { // the only one term available has a term code not equal to 1
                        eqTerms = new Equals("termsCode",new Integer(firstTermsBean.getTermsCode()));
                        CoeusVector cvCheckPrev = cvPrevTermsCodes.filter(eqTerms);
                        if (cvCheckPrev.size()==0) { // checking whether the term was there isn the previous seq number
                            CoeusVector cvFilteredPropertyData = cvTermsData.filter(eqTerms);
                            AwardTermsBean propertyTermsBean = (AwardTermsBean)cvFilteredPropertyData.get(0);
                            //should not create this again.
                            //AwardType.AwardTermsDetailsType awardTermsDetailsType = new AwardTypeImpl.AwardTermsDetailsTypeImpl();
                            //awardType.setAwardTermsDetails(awardTermsDetailsType);
                            TermDetailsType propertyTermDetailsType = new TermDetailsTypeImpl() ;
                            propertyTermDetailsType.setAwardNumber(mitAwardNumber);
                            propertyTermDetailsType.setSequenceNumber(propertyTermsBean.getSequenceNumber());
                            propertyTermDetailsType.setTermCode(propertyTermsBean.getTermsCode());
                            propertyTermDetailsType.setTermDescription("ADDED "+propertyTermsBean.getTermsDescription());
                            propertyTermType.getTermDetails().add(propertyTermDetailsType);
                            //awardType.getAwardTermsDetails().getTerm().add(equipmentTermType);
                        } //"if" for checking whether the term was present in the previoous sequence number ends
                    }// "if" for only one term available ends
                } else { //there are more than one term
                    for (int indx=0;indx<cvTermsCodes.size();indx++) {
                        awardTermsBean = (AwardTermsBean)cvTermsCodes.get(indx);
                        eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                        CoeusVector cvCheckPrev = cvPrevTermsCodes.filter(eqTerms);
                        if (cvCheckPrev.size()==0) { // checking whether the term was there isn the previous seq number
                            CoeusVector cvFilteredPropertyData = cvTermsData.filter(eqTerms);
                            AwardTermsBean propertyTermsBean = (AwardTermsBean)cvFilteredPropertyData.get(0);
                            //should not create this again.
                            //AwardType.AwardTermsDetailsType awardTermsDetailsType = new AwardTypeImpl.AwardTermsDetailsTypeImpl();
                            //awardType.setAwardTermsDetails(awardTermsDetailsType);
                            TermDetailsType propertyTermDetailsType = new TermDetailsTypeImpl() ;
                            propertyTermDetailsType.setAwardNumber(mitAwardNumber);
                            propertyTermDetailsType.setSequenceNumber(propertyTermsBean.getSequenceNumber());
                            propertyTermDetailsType.setTermCode(propertyTermsBean.getTermsCode());
                            propertyTermDetailsType.setTermDescription("ADDED "+propertyTermsBean.getTermsDescription());
                            propertyTermType.getTermDetails().add(propertyTermDetailsType);
                        }
                    } //for loop ends
               //fix case 1728 begin
                }  // more than one term available ends
               //fix case 1728 end
                    for (int indx=0;indx <cvPrevTermsCodes.size();indx++) {
                        awardTermsBean = (AwardTermsBean)cvPrevTermsCodes.get(indx);
                        eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                        CoeusVector cvCheck = cvTermsCodes.filter(eqTerms);
                        if (cvCheck.size()==0) { // checking whether the term is there in the current seq number
                            CoeusVector cvFilteredPropertyData = cvTermsData.filter(eqTerms);
                            AwardTermsBean propertyTermsBean = (AwardTermsBean)cvFilteredPropertyData.get(0);
                            //should not create this again.
                            //AwardType.AwardTermsDetailsType awardTermsDetailsType = new AwardTypeImpl.AwardTermsDetailsTypeImpl();
                            //awardType.setAwardTermsDetails(awardTermsDetailsType);
                            TermDetailsType propertyTermDetailsType = new TermDetailsTypeImpl() ;
                            propertyTermDetailsType.setAwardNumber(mitAwardNumber);
                            propertyTermDetailsType.setSequenceNumber(propertyTermsBean.getSequenceNumber());
                            propertyTermDetailsType.setTermCode(propertyTermsBean.getTermsCode());
                            propertyTermDetailsType.setTermDescription("DELETED "+propertyTermsBean.getTermsDescription());
                            propertyTermType.getTermDetails().add(propertyTermDetailsType);
                        }
                    }

                    //awardType.getAwardTermsDetails().getTerm().add(equipmentTermType);
                //fix case 1728 begin
//                }  // more than one term available ends
               //fix case 1728 end
                //eqTerms = new Equals("termsCode",new Integer(firstTermsBean.getTermsCode()));
                //CoeusVector cvCheckPrev = cvPrevTermsCodes.filter(eqTerms);
                if (propertyTermType.getTermDetails() != null && propertyTermType.getTermDetails().size()>0) {
                    awardType.getAwardTermsDetails().getTerm().add(propertyTermType);
                }
            }
            //Property


               /* if (cvTermsCodes!=null) {
                    for (int indx=0;indx<cvTermsCodes.size();indx++) {
                        awardTermsBean = (AwardTermsBean)cvTermsCodes.get(indx);
                        eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                        CoeusVector cvFilteredPropertyData = cvTermsData.filter(eqTerms);
                        AwardTermsBean propertyTermsBean = (AwardTermsBean)cvFilteredPropertyData.get(0);

                        TermDetailsType propertyTermDetailsType = new TermDetailsTypeImpl() ;
                        propertyTermDetailsType.setAwardNumber(mitAwardNumber);
                        propertyTermDetailsType.setSequenceNumber(seqNumber);
                        propertyTermDetailsType.setTermCode(propertyTermsBean.getTermsCode());
                        propertyTermDetailsType.setTermDescription(propertyTermsBean.getTermsDescription());
                        propertyTermType.getTermDetails().add(propertyTermDetailsType);
                    }
                    awardType.getAwardTermsDetails().getTerm().add(propertyTermType);
                }*/

            //Comments
            //cvAwardCommentsForSeq


            cvAwardCommentsForSeq.sort("commentCode",true);
            CommentDetailsType commentDetailsType;
            CommentType commentType;
            AwardType.AwardCommentsType awardCommentsType = new AwardTypeImpl.AwardCommentsTypeImpl();
            awardType.setAwardComments(awardCommentsType);
            for (int indx=0;indx<cvAwardCommentsForSeq.size();indx++) {

                awardCommentsBean = (AwardCommentsBean) cvAwardCommentsForSeq.get(indx);
                // need to take care of seqNum too.
                if (awardCommentsBean.getCommentCode()<2 || awardCommentsBean.getCommentCode()>7 ||awardCommentsBean.getSequenceNumber() != seqNumber ) {
                    continue;
                }
                // if (awardCommentsBean.getCommentCode()<2 || awardCommentsBean.getCommentCode()>7 ) {
                //    continue;
                //}
                commentDetailsType = new CommentDetailsTypeImpl();
                commentDetailsType.setAwardNumber(mitAwardNumber);
                commentDetailsType.setCommentCode(awardCommentsBean.getCommentCode());
                commentDetailsType.setComments(awardCommentsBean.getComments());
                commentDetailsType.setPrintChecklist(awardCommentsBean.isCheckListPrintFlag());
                commentDetailsType.setSequenceNumber(seqNumber);
                commentType = new CommentTypeImpl();
                commentType.setCommentDetails(commentDetailsType);

                switch (awardCommentsBean.getCommentCode()) {
                    case FISCAL_REPORT_COMMENT_CODE:
                        commentType.setDescription(FISCAL_REPORT);
                        break;
                    case GENEREAL_COMMENT_CODE :
                        commentType.setDescription(GENEREAL_COMMENT);
                        break;
                    case INTELLECTUAL_PROPERTY_COMMENT_CODE:
                        commentType.setDescription(INTELLECTUAL_PROPERTY);
                        break;
                    case PROCUREMENT_COMMENT_CODE:
                        commentType.setDescription(PROCUREMENT_COMMENT);
                        break;
                    case PROPERTY_COMMENT_CODE:
                        commentType.setDescription(PROPERTY_COMMENT);
                }
                awardType.getAwardComments().getComment().add(commentType);
            }
            //Comments

            //Get Publication Terms
            cvTermsData = null;
            cvTermsData = awardLookUpDataTxnBean.getPublication();
            cvTermsCodes = awardDeltaReportTxnBean.getAwardPublicationTermsForSeq(mitAwardNumber, sequnceNumber);
            TermType publicationTermType = new TermTypeImpl();
            publicationTermType.setDescription("Publication");


            // Publication
            if (cvTermsCodes!=null) {
                // Get previous sequence number terms
                CoeusVector cvPrevTermsCodes = awardDeltaReportTxnBean.
                getAwardPublicationTermsForSeq(mitAwardNumber, ""+(seqNumber-1));
                if (cvTermsCodes.size() ==1) { // if there is only one term
                    AwardTermsBean firstTermsBean = (AwardTermsBean)cvTermsCodes.get(0);
                    if (firstTermsBean.getTermsCode() !=1) { // the only one term available has a term code not equal to 1
                        eqTerms = new Equals("termsCode",new Integer(firstTermsBean.getTermsCode()));
                        CoeusVector cvCheckPrev = cvPrevTermsCodes.filter(eqTerms);
                        if (cvCheckPrev.size()==0) { // checking whether the term was there isn the previous seq number
                            CoeusVector cvFilteredPublicationData = cvTermsData.filter(eqTerms);
                            AwardTermsBean publicationTermsBean = (AwardTermsBean)cvFilteredPublicationData.get(0);
                            //should not create this again.
                            //AwardType.AwardTermsDetailsType awardTermsDetailsType = new AwardTypeImpl.AwardTermsDetailsTypeImpl();
                            //awardType.setAwardTermsDetails(awardTermsDetailsType);
                            TermDetailsType publicationTermDetailsType = new TermDetailsTypeImpl() ;
                            publicationTermDetailsType.setAwardNumber(mitAwardNumber);
                            publicationTermDetailsType.setSequenceNumber(publicationTermsBean.getSequenceNumber());
                            publicationTermDetailsType.setTermCode(publicationTermsBean.getTermsCode());
                            publicationTermDetailsType.setTermDescription("ADDED "+publicationTermsBean.getTermsDescription());
                            publicationTermType.getTermDetails().add(publicationTermDetailsType);
                            //awardType.getAwardTermsDetails().getTerm().add(equipmentTermType);
                        } //"if" for checking whether the term was present in the previoous sequence number ends
                    }// "if" for only one term available ends
                } else { //there are more than one term
                    for (int indx=0;indx<cvTermsCodes.size();indx++) {
                        awardTermsBean = (AwardTermsBean)cvTermsCodes.get(indx);
                        eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                        CoeusVector cvCheckPrev = cvPrevTermsCodes.filter(eqTerms);
                        if (cvCheckPrev.size()==0) { // checking whether the term was there isn the previous seq number
                            CoeusVector cvFilteredPublicationData = cvTermsData.filter(eqTerms);
                            AwardTermsBean publicationTermsBean = (AwardTermsBean)cvFilteredPublicationData.get(0);
                            //should not create this again.
                            //AwardType.AwardTermsDetailsType awardTermsDetailsType = new AwardTypeImpl.AwardTermsDetailsTypeImpl();
                            //awardType.setAwardTermsDetails(awardTermsDetailsType);
                            TermDetailsType publicationTermDetailsType = new TermDetailsTypeImpl() ;
                            publicationTermDetailsType.setAwardNumber(mitAwardNumber);
                            publicationTermDetailsType.setSequenceNumber(publicationTermsBean.getSequenceNumber());
                            publicationTermDetailsType.setTermCode(publicationTermsBean.getTermsCode());
                            publicationTermDetailsType.setTermDescription("ADDED "+publicationTermsBean.getTermsDescription());
                            publicationTermType.getTermDetails().add(publicationTermDetailsType);
                        }
                    } //for loop ends
                //fix case 1728 begin
                }  // more than one term available ends
               //fix case 1728 end
                    for (int indx=0;indx <cvPrevTermsCodes.size();indx++) {
                        awardTermsBean = (AwardTermsBean)cvPrevTermsCodes.get(indx);
                        eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                        CoeusVector cvCheck = cvTermsCodes.filter(eqTerms);
                        if (cvCheck.size()==0) { // checking whether the term is there in the current seq number
                            CoeusVector cvFilteredPublicationData = cvTermsData.filter(eqTerms);
                            AwardTermsBean publicationTermsBean = (AwardTermsBean)cvFilteredPublicationData.get(0);
                            //should not create this again.
                            //AwardType.AwardTermsDetailsType awardTermsDetailsType = new AwardTypeImpl.AwardTermsDetailsTypeImpl();
                            //awardType.setAwardTermsDetails(awardTermsDetailsType);
                            TermDetailsType publicationTermDetailsType = new TermDetailsTypeImpl() ;
                            publicationTermDetailsType.setAwardNumber(mitAwardNumber);
                            publicationTermDetailsType.setSequenceNumber(publicationTermsBean.getSequenceNumber());
                            publicationTermDetailsType.setTermCode(publicationTermsBean.getTermsCode());
                            publicationTermDetailsType.setTermDescription("DELETED "+publicationTermsBean.getTermsDescription());
                            publicationTermType.getTermDetails().add(publicationTermDetailsType);
                        }
                    }

                    //awardType.getAwardTermsDetails().getTerm().add(equipmentTermType);
                //fix case 1728 begin
//                }  // more than one term available ends
               //fix case 1728 end
                //eqTerms = new Equals("termsCode",new Integer(firstTermsBean.getTermsCode()));
                //CoeusVector cvCheckPrev = cvPrevTermsCodes.filter(eqTerms);
                if (publicationTermType.getTermDetails() != null && publicationTermType.getTermDetails().size()>0) {
                    awardType.getAwardTermsDetails().getTerm().add(publicationTermType);
                }
            }
            // Publication


                /*if (cvTermsCodes!=null) {
                    for (int indx=0;indx<cvTermsCodes.size();indx++) {
                        awardTermsBean = (AwardTermsBean)cvTermsCodes.get(indx);
                        eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                        CoeusVector cvFilteredPublicationData = cvTermsData.filter(eqTerms);
                        AwardTermsBean publicationTermsBean = (AwardTermsBean)cvFilteredPublicationData.get(0);

                        TermDetailsType publicationTermDetailsType = new TermDetailsTypeImpl() ;
                        publicationTermDetailsType.setAwardNumber(mitAwardNumber);
                        publicationTermDetailsType.setSequenceNumber(seqNumber);
                        publicationTermDetailsType.setTermCode(publicationTermsBean.getTermsCode());
                        publicationTermDetailsType.setTermDescription(publicationTermsBean.getTermsDescription());
                        publicationTermType.getTermDetails().add(publicationTermDetailsType);
                    }

                    awardType.getAwardTermsDetails().getTerm().add(publicationTermType);
                }*/

            //Get Reference Documents Terms
            cvTermsData = null;
            cvTermsData = awardLookUpDataTxnBean.getReferencedDocument();
            cvTermsCodes = awardDeltaReportTxnBean.getAwardReferencedDocumentTermsForSeq(mitAwardNumber, sequnceNumber);
            TermType referencedDocTermType = new TermTypeImpl();
            referencedDocTermType.setDescription("Referenced Documents");

            // Referenced Docs
            if (cvTermsCodes!=null) {
                // Get previous sequence number terms
                CoeusVector cvPrevTermsCodes = awardDeltaReportTxnBean.
                getAwardReferencedDocumentTermsForSeq(mitAwardNumber, ""+(seqNumber-1));
                if (cvTermsCodes.size() ==1) { // if there is only one term
                    AwardTermsBean firstTermsBean = (AwardTermsBean)cvTermsCodes.get(0);
                    if (firstTermsBean.getTermsCode() !=1) { // the only one term available has a term code not equal to 1
                        eqTerms = new Equals("termsCode",new Integer(firstTermsBean.getTermsCode()));
                        CoeusVector cvCheckPrev = cvPrevTermsCodes.filter(eqTerms);
                        if (cvCheckPrev.size()==0) { // checking whether the term was there isn the previous seq number
                            CoeusVector cvFilteredRefDocData = cvTermsData.filter(eqTerms);
                            AwardTermsBean referencedDocTermsBean = (AwardTermsBean)cvFilteredRefDocData.get(0);
                            //should not create this again.
                            //AwardType.AwardTermsDetailsType awardTermsDetailsType = new AwardTypeImpl.AwardTermsDetailsTypeImpl();
                            //awardType.setAwardTermsDetails(awardTermsDetailsType);
                            TermDetailsType referencedDocTermDetailsType = new TermDetailsTypeImpl() ;
                            referencedDocTermDetailsType.setAwardNumber(mitAwardNumber);
                            referencedDocTermDetailsType.setSequenceNumber(referencedDocTermsBean.getSequenceNumber());
                            referencedDocTermDetailsType.setTermCode(referencedDocTermsBean.getTermsCode());
                            referencedDocTermDetailsType.setTermDescription("ADDED "+referencedDocTermsBean.getTermsDescription());
                            referencedDocTermType.getTermDetails().add(referencedDocTermDetailsType);
                            //awardType.getAwardTermsDetails().getTerm().add(equipmentTermType);
                        } //"if" for checking whether the term was present in the previoous sequence number ends
                    }// "if" for only one term available ends
                } else { //there are more than one term
                    for (int indx=0;indx<cvTermsCodes.size();indx++) {
                        awardTermsBean = (AwardTermsBean)cvTermsCodes.get(indx);
                        eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                        CoeusVector cvCheckPrev = cvPrevTermsCodes.filter(eqTerms);
                        if (cvCheckPrev.size()==0) { // checking whether the term was there isn the previous seq number
                            CoeusVector cvFilteredRefDocData = cvTermsData.filter(eqTerms);
                            AwardTermsBean referencedDocTermsBean = (AwardTermsBean)cvFilteredRefDocData.get(0);
                            //should not create this again.
                            //AwardType.AwardTermsDetailsType awardTermsDetailsType = new AwardTypeImpl.AwardTermsDetailsTypeImpl();
                            //awardType.setAwardTermsDetails(awardTermsDetailsType);
                            TermDetailsType referencedDocTermDetailsType = new TermDetailsTypeImpl() ;
                            referencedDocTermDetailsType.setAwardNumber(mitAwardNumber);
                            referencedDocTermDetailsType.setSequenceNumber(referencedDocTermsBean.getSequenceNumber());
                            referencedDocTermDetailsType.setTermCode(referencedDocTermsBean.getTermsCode());
                            referencedDocTermDetailsType.setTermDescription("ADDED "+referencedDocTermsBean.getTermsDescription());
                            referencedDocTermType.getTermDetails().add(referencedDocTermDetailsType);
                        }
                    } //for loop ends
                //fix case 1728 begin
                }  // more than one term available ends
               //fix case 1728 end
                    for (int indx=0;indx <cvPrevTermsCodes.size();indx++) {
                        awardTermsBean = (AwardTermsBean)cvPrevTermsCodes.get(indx);
                        eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                        CoeusVector cvCheck = cvTermsCodes.filter(eqTerms);
                        if (cvCheck.size()==0) { // checking whether the term is there in the current seq number
                            CoeusVector cvFilteredRefDocData = cvTermsData.filter(eqTerms);
                            AwardTermsBean referencedDocTermsBean = (AwardTermsBean)cvFilteredRefDocData.get(0);
                            //should not create this again.
                            //AwardType.AwardTermsDetailsType awardTermsDetailsType = new AwardTypeImpl.AwardTermsDetailsTypeImpl();
                            //awardType.setAwardTermsDetails(awardTermsDetailsType);
                            TermDetailsType referencedDocTermDetailsType = new TermDetailsTypeImpl() ;
                            referencedDocTermDetailsType.setAwardNumber(mitAwardNumber);
                            referencedDocTermDetailsType.setSequenceNumber(referencedDocTermsBean.getSequenceNumber());
                            referencedDocTermDetailsType.setTermCode(referencedDocTermsBean.getTermsCode());
                            referencedDocTermDetailsType.setTermDescription("DELETED "+referencedDocTermsBean.getTermsDescription());
                            referencedDocTermType.getTermDetails().add(referencedDocTermDetailsType);
                        }
                    }

                    //awardType.getAwardTermsDetails().getTerm().add(equipmentTermType);
               //fix case 1728 begin
//                }  // more than one term available ends
               //fix case 1728 end
                //eqTerms = new Equals("termsCode",new Integer(firstTermsBean.getTermsCode()));
                //CoeusVector cvCheckPrev = cvPrevTermsCodes.filter(eqTerms);
                if ( referencedDocTermType.getTermDetails() != null && referencedDocTermType.getTermDetails().size()>0) {
                    awardType.getAwardTermsDetails().getTerm().add(referencedDocTermType);
                }
            }
            // Referenced Docs

                /*if (cvTermsCodes !=null) {
                    for (int indx=0;indx<cvTermsCodes.size();indx++) {
                        awardTermsBean = (AwardTermsBean)cvTermsCodes.get(indx);
                        eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                        CoeusVector cvFilteredRefDocData = cvTermsData.filter(eqTerms);
                        AwardTermsBean referencedDocTermsBean = (AwardTermsBean)cvFilteredRefDocData.get(0);

                        TermDetailsType referencedDocTermDetailsType = new TermDetailsTypeImpl() ;
                        referencedDocTermDetailsType.setAwardNumber(mitAwardNumber);
                        referencedDocTermDetailsType.setSequenceNumber(seqNumber);
                        referencedDocTermDetailsType.setTermCode(referencedDocTermsBean.getTermsCode());
                        referencedDocTermDetailsType.setTermDescription(referencedDocTermsBean.getTermsDescription());
                        referencedDocTermType.getTermDetails().add(referencedDocTermDetailsType);
                    }

                    awardType.getAwardTermsDetails().getTerm().add(referencedDocTermType);
                }*/

            //Get Rights in data Terms
            cvTermsData = null;
            cvTermsData = awardLookUpDataTxnBean.getRightsInData();
            cvTermsCodes = awardDeltaReportTxnBean.getAwardRightsInDataTermsForSeq(mitAwardNumber, sequnceNumber);
            TermType rightsInDataTermType = new TermTypeImpl();
            rightsInDataTermType.setDescription("Rights In Data");
            //Rights in Data
            if (cvTermsCodes!=null) {
                // Get previous sequence number terms
                CoeusVector cvPrevTermsCodes = awardDeltaReportTxnBean.
                getAwardRightsInDataTermsForSeq(mitAwardNumber, ""+(seqNumber-1));
                if (cvTermsCodes.size() ==1) { // if there is only one term
                    AwardTermsBean firstTermsBean = (AwardTermsBean)cvTermsCodes.get(0);
                    if (firstTermsBean.getTermsCode() !=1) { // the only one term available has a term code not equal to 1
                        eqTerms = new Equals("termsCode",new Integer(firstTermsBean.getTermsCode()));
                        CoeusVector cvCheckPrev = cvPrevTermsCodes.filter(eqTerms);
                        if (cvCheckPrev.size()==0) { // checking whether the term was there isn the previous seq number
                            CoeusVector cvFilteredRightsData = cvTermsData.filter(eqTerms);
                            AwardTermsBean rightsInDataTermsBean = (AwardTermsBean)cvFilteredRightsData.get(0);
                            //should not create this again.
                            //AwardType.AwardTermsDetailsType awardTermsDetailsType = new AwardTypeImpl.AwardTermsDetailsTypeImpl();
                            //awardType.setAwardTermsDetails(awardTermsDetailsType);
                            TermDetailsType rightsInDataTermDetailsType = new TermDetailsTypeImpl() ;
                            rightsInDataTermDetailsType.setAwardNumber(mitAwardNumber);
                            rightsInDataTermDetailsType.setSequenceNumber(rightsInDataTermsBean.getSequenceNumber());
                            rightsInDataTermDetailsType.setTermCode(rightsInDataTermsBean.getTermsCode());
                            rightsInDataTermDetailsType.setTermDescription("ADDED "+rightsInDataTermsBean.getTermsDescription());
                            rightsInDataTermType.getTermDetails().add(rightsInDataTermDetailsType);
                            //awardType.getAwardTermsDetails().getTerm().add(equipmentTermType);
                        } //"if" for checking whether the term was present in the previoous sequence number ends
                    }// "if" for only one term available ends
                } else { //there are more than one term
                    for (int indx=0;indx<cvTermsCodes.size();indx++) {
                        awardTermsBean = (AwardTermsBean)cvTermsCodes.get(indx);
                        eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                        CoeusVector cvCheckPrev = cvPrevTermsCodes.filter(eqTerms);
                        if (cvCheckPrev.size()==0) { // checking whether the term was there isn the previous seq number
                            CoeusVector cvFilteredRightsData = cvTermsData.filter(eqTerms);
                            AwardTermsBean rightsInDataTermsBean = (AwardTermsBean)cvFilteredRightsData.get(0);
                            //should not create this again.
                            //AwardType.AwardTermsDetailsType awardTermsDetailsType = new AwardTypeImpl.AwardTermsDetailsTypeImpl();
                            //awardType.setAwardTermsDetails(awardTermsDetailsType);
                            TermDetailsType rightsInDataTermDetailsType = new TermDetailsTypeImpl() ;
                            rightsInDataTermDetailsType.setAwardNumber(mitAwardNumber);
                            rightsInDataTermDetailsType.setSequenceNumber(rightsInDataTermsBean.getSequenceNumber());
                            rightsInDataTermDetailsType.setTermCode(rightsInDataTermsBean.getTermsCode());
                            rightsInDataTermDetailsType.setTermDescription("ADDED "+rightsInDataTermsBean.getTermsDescription());
                            rightsInDataTermType.getTermDetails().add(rightsInDataTermDetailsType);
                        }
                    } //for loop ends
                //fix case 1728 begin
                }  // more than one term available ends
               //fix case 1728 end
                    for (int indx=0;indx <cvPrevTermsCodes.size();indx++) {
                        awardTermsBean = (AwardTermsBean)cvPrevTermsCodes.get(indx);
                        eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                        CoeusVector cvCheck = cvTermsCodes.filter(eqTerms);
                        if (cvCheck.size()==0) { // checking whether the term is there in the current seq number
                            CoeusVector cvFilteredRightsData = cvTermsData.filter(eqTerms);
                            AwardTermsBean rightsInDataTermsBean = (AwardTermsBean)cvFilteredRightsData.get(0);
                            //should not create this again.
                            //AwardType.AwardTermsDetailsType awardTermsDetailsType = new AwardTypeImpl.AwardTermsDetailsTypeImpl();
                            //awardType.setAwardTermsDetails(awardTermsDetailsType);
                            TermDetailsType rightsInDataTermDetailsType = new TermDetailsTypeImpl() ;
                            rightsInDataTermDetailsType.setAwardNumber(mitAwardNumber);
                            rightsInDataTermDetailsType.setSequenceNumber(rightsInDataTermsBean.getSequenceNumber());
                            rightsInDataTermDetailsType.setTermCode(rightsInDataTermsBean.getTermsCode());
                            rightsInDataTermDetailsType.setTermDescription("DELETED "+rightsInDataTermsBean.getTermsDescription());
                            rightsInDataTermType.getTermDetails().add(rightsInDataTermDetailsType);
                        }
                    }

                    //awardType.getAwardTermsDetails().getTerm().add(equipmentTermType);
                //fix case 1728 begin
//                }  // more than one term available ends
               //fix case 1728 end
                //eqTerms = new Equals("termsCode",new Integer(firstTermsBean.getTermsCode()));
                //CoeusVector cvCheckPrev = cvPrevTermsCodes.filter(eqTerms);
                if (rightsInDataTermType.getTermDetails() != null && rightsInDataTermType.getTermDetails().size()>0) {
                    awardType.getAwardTermsDetails().getTerm().add(rightsInDataTermType);
                }
            }
            //Rights in Data
               /* if (cvTermsCodes !=null) {
                    for (int indx=0;indx<cvTermsCodes.size();indx++) {
                        awardTermsBean = (AwardTermsBean)cvTermsCodes.get(indx);
                        eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                        CoeusVector cvFilteredRightsData = cvTermsData.filter(eqTerms);

                        AwardTermsBean rightsInDataTermsBean = (AwardTermsBean)cvFilteredRightsData.get(0);

                        TermDetailsType rightsInDataTermDetailsType = new TermDetailsTypeImpl() ;
                        rightsInDataTermDetailsType.setAwardNumber(mitAwardNumber);
                        rightsInDataTermDetailsType.setSequenceNumber(seqNumber);
                        rightsInDataTermDetailsType.setTermCode(rightsInDataTermsBean.getTermsCode());
                        rightsInDataTermDetailsType.setTermDescription(rightsInDataTermsBean.getTermsDescription());
                        rightsInDataTermType.getTermDetails().add(rightsInDataTermDetailsType);
                    }

                    awardType.getAwardTermsDetails().getTerm().add(rightsInDataTermType);
                }*/

            //Get Subcontract Approval Terms
            cvTermsData = null;
            cvTermsData = awardLookUpDataTxnBean.getSubcontractApproval();
            cvTermsCodes = awardDeltaReportTxnBean.getAwardSubcontractTermsForSeq(mitAwardNumber,sequnceNumber);
            TermType subcontractApprovalTermType = new TermTypeImpl();
            subcontractApprovalTermType.setDescription("Subcontracting");


            //Subcontracting
            if (cvTermsCodes!=null) {
                // Get previous sequence number terms
                CoeusVector cvPrevTermsCodes = awardDeltaReportTxnBean.
                getAwardSubcontractTermsForSeq(mitAwardNumber, ""+(seqNumber-1));
                if (cvTermsCodes.size() ==1) { // if there is only one term
                    AwardTermsBean firstTermsBean = (AwardTermsBean)cvTermsCodes.get(0);
                    if (firstTermsBean.getTermsCode() !=1) { // the only one term available has a term code not equal to 1
                        eqTerms = new Equals("termsCode",new Integer(firstTermsBean.getTermsCode()));
                        CoeusVector cvCheckPrev = cvPrevTermsCodes.filter(eqTerms);
                        if (cvCheckPrev.size()==0) { // checking whether the term was there isn the previous seq number
                            CoeusVector cvFilteredSubContractData = cvTermsData.filter(eqTerms);
                            AwardTermsBean subcontractApprovalTermsBean = (AwardTermsBean)cvFilteredSubContractData.get(0);
                            //should not create this again.                              //AwardType.AwardTermsDetailsType awardTermsDetailsType = new AwardTypeImpl.AwardTermsDetailsTypeImpl();
                            //awardType.setAwardTermsDetails(awardTermsDetailsType);
                            TermDetailsType subcontractApprovalTermDetailsType = new TermDetailsTypeImpl() ;
                            subcontractApprovalTermDetailsType.setAwardNumber(mitAwardNumber);
                            subcontractApprovalTermDetailsType.setSequenceNumber(subcontractApprovalTermsBean.getSequenceNumber());
                            subcontractApprovalTermDetailsType.setTermCode(subcontractApprovalTermsBean.getTermsCode());
                            subcontractApprovalTermDetailsType.setTermDescription("ADDED "+subcontractApprovalTermsBean.getTermsDescription());
                            subcontractApprovalTermType.getTermDetails().add(subcontractApprovalTermDetailsType);
                            //awardType.getAwardTermsDetails().getTerm().add(equipmentTermType);
                        } //"if" for checking whether the term was present in the previoous sequence number ends
                    }// "if" for only one term available ends
                } else { //there are more than one term
                    for (int indx=0;indx<cvTermsCodes.size();indx++) {
                        awardTermsBean = (AwardTermsBean)cvTermsCodes.get(indx);
                        eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                        CoeusVector cvCheckPrev = cvPrevTermsCodes.filter(eqTerms);
                        if (cvCheckPrev.size()==0) { // checking whether the term was there isn the previous seq number
                            CoeusVector cvFilteredSubContractData = cvTermsData.filter(eqTerms);
                            AwardTermsBean subcontractApprovalTermsBean = (AwardTermsBean)cvFilteredSubContractData.get(0);
                            //should not create this again.                              //AwardType.AwardTermsDetailsType awardTermsDetailsType = new AwardTypeImpl.AwardTermsDetailsTypeImpl();
                            //awardType.setAwardTermsDetails(awardTermsDetailsType);
                            TermDetailsType subcontractApprovalTermDetailsType = new TermDetailsTypeImpl() ;
                            subcontractApprovalTermDetailsType.setAwardNumber(mitAwardNumber);
                            subcontractApprovalTermDetailsType.setSequenceNumber(subcontractApprovalTermsBean.getSequenceNumber());
                            subcontractApprovalTermDetailsType.setTermCode(subcontractApprovalTermsBean.getTermsCode());
                            subcontractApprovalTermDetailsType.setTermDescription("ADDED "+subcontractApprovalTermsBean.getTermsDescription());
                            subcontractApprovalTermType.getTermDetails().add(subcontractApprovalTermDetailsType);
                        }
                    } //for loop ends
               //fix case 1728 begin
                }  // more than one term available ends
               //fix case 1728 end
                    for (int indx=0;indx <cvPrevTermsCodes.size();indx++) {
                        awardTermsBean = (AwardTermsBean)cvPrevTermsCodes.get(indx);
                        eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                        CoeusVector cvCheck = cvTermsCodes.filter(eqTerms);
                        if (cvCheck.size()==0) { // checking whether the term is there in the current seq number
                            CoeusVector cvFilteredSubContractData = cvTermsData.filter(eqTerms);
                            AwardTermsBean subcontractApprovalTermsBean = (AwardTermsBean)cvFilteredSubContractData.get(0);
                            //should not create this again.                              //AwardType.AwardTermsDetailsType awardTermsDetailsType = new AwardTypeImpl.AwardTermsDetailsTypeImpl();
                            //awardType.setAwardTermsDetails(awardTermsDetailsType);
                            TermDetailsType subcontractApprovalTermDetailsType = new TermDetailsTypeImpl() ;
                            subcontractApprovalTermDetailsType.setAwardNumber(mitAwardNumber);
                            subcontractApprovalTermDetailsType.setSequenceNumber(subcontractApprovalTermsBean.getSequenceNumber());
                            subcontractApprovalTermDetailsType.setTermCode(subcontractApprovalTermsBean.getTermsCode());
                            subcontractApprovalTermDetailsType.setTermDescription("DELETED "+subcontractApprovalTermsBean.getTermsDescription());
                            subcontractApprovalTermType.getTermDetails().add(subcontractApprovalTermDetailsType);
                        }
                    }

                    //awardType.getAwardTermsDetails().getTerm().add(equipmentTermType);
                //fix case 1728 begin
//                }  // more than one term available ends
               //fix case 1728 end
                //eqTerms = new Equals("termsCode",new Integer(firstTermsBean.getTermsCode()));
                //CoeusVector cvCheckPrev = cvPrevTermsCodes.filter(eqTerms);
                if (subcontractApprovalTermType.getTermDetails() !=  null && subcontractApprovalTermType.getTermDetails().size()>0) {
                    awardType.getAwardTermsDetails().getTerm().add(subcontractApprovalTermType);
                }
            }
            //SubContracting


                /*if (cvTermsCodes != null) {
                    for (int indx=0;indx<cvTermsCodes.size();indx++) {
                        awardTermsBean = (AwardTermsBean)cvTermsCodes.get(indx);
                        eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                        CoeusVector cvFilteredSubContractData = cvTermsData.filter(eqTerms);
                        AwardTermsBean subcontractApprovalTermsBean = (AwardTermsBean)cvFilteredSubContractData.get(0);
                        TermDetailsType subcontractApprovalTermDetailsType = new  TermDetailsTypeImpl() ;
                        subcontractApprovalTermDetailsType.setAwardNumber(mitAwardNumber);
                        subcontractApprovalTermDetailsType.setSequenceNumber(seqNumber);
                        subcontractApprovalTermDetailsType.setTermCode(subcontractApprovalTermsBean.getTermsCode());
                        subcontractApprovalTermDetailsType.setTermDescription(subcontractApprovalTermsBean.getTermsDescription());
                        subcontractApprovalTermType.getTermDetails().add(subcontractApprovalTermDetailsType);
                    }
                    awardType.getAwardTermsDetails().getTerm().add(subcontractApprovalTermType);
                }*/

            //Get Travel Terms
            cvTermsData = null;

            cvTermsData = awardLookUpDataTxnBean.getTravelRestriction();
            cvTermsCodes = awardDeltaReportTxnBean.getAwardTravelTermsForSeq(mitAwardNumber, sequnceNumber);
            TermType travelRestrictionTermType = new TermTypeImpl();
            travelRestrictionTermType.setDescription("Travel");

            //Travel
            if (cvTermsCodes!=null) {
                // Get previous sequence number terms
                CoeusVector cvPrevTermsCodes = awardDeltaReportTxnBean.
                getAwardTravelTermsForSeq(mitAwardNumber, ""+(seqNumber-1));
                if (cvTermsCodes.size() ==1) { // if there is only one term
                    AwardTermsBean firstTermsBean = (AwardTermsBean)cvTermsCodes.get(0);
                    if (firstTermsBean.getTermsCode() !=1) { // the only one term available has a term code not equal to 1
                        eqTerms = new Equals("termsCode",new Integer(firstTermsBean.getTermsCode()));
                        CoeusVector cvCheckPrev = cvPrevTermsCodes.filter(eqTerms);
                        if (cvCheckPrev.size()==0) { // checking whether the term was there isn the previous seq number
                            CoeusVector cvFilteredTravelRestrictionData = cvTermsData.filter(eqTerms);
                            AwardTermsBean travelRestrictionTermsBean = (AwardTermsBean)cvFilteredTravelRestrictionData.get(0);
                            //should not create this again.
                            //AwardType.AwardTermsDetailsType awardTermsDetailsType = new AwardTypeImpl.AwardTermsDetailsTypeImpl();
                            //awardType.setAwardTermsDetails(awardTermsDetailsType);
                            TermDetailsType travelRestrictionTermDetailsType = new TermDetailsTypeImpl() ;
                            travelRestrictionTermDetailsType.setAwardNumber(mitAwardNumber);
                            travelRestrictionTermDetailsType.setSequenceNumber(travelRestrictionTermsBean.getSequenceNumber());
                            travelRestrictionTermDetailsType.setTermCode(travelRestrictionTermsBean.getTermsCode());
                            travelRestrictionTermDetailsType.setTermDescription("ADDED "+travelRestrictionTermsBean.getTermsDescription());
                            travelRestrictionTermType.getTermDetails().add(travelRestrictionTermDetailsType);
                            //awardType.getAwardTermsDetails().getTerm().add(equipmentTermType);
                        } //"if" for checking whether the term was present in the previoous sequence number ends
                    }// "if" for only one term available ends
                } else { //there are more than one term
                    for (int indx=0;indx<cvTermsCodes.size();indx++) {
                        awardTermsBean = (AwardTermsBean)cvTermsCodes.get(indx);
                        eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                        CoeusVector cvCheckPrev = cvPrevTermsCodes.filter(eqTerms);
                        if (cvCheckPrev.size()==0) { // checking whether the term was there isn the previous seq number
                            CoeusVector cvFilteredTravelRestrictionData = cvTermsData.filter(eqTerms);
                            AwardTermsBean travelRestrictionTermsBean = (AwardTermsBean)cvFilteredTravelRestrictionData.get(0);
                            //should not create this again.
                            //AwardType.AwardTermsDetailsType awardTermsDetailsType = new AwardTypeImpl.AwardTermsDetailsTypeImpl();
                            //awardType.setAwardTermsDetails(awardTermsDetailsType);
                            TermDetailsType travelRestrictionTermDetailsType = new TermDetailsTypeImpl() ;
                            travelRestrictionTermDetailsType.setAwardNumber(mitAwardNumber);
                            travelRestrictionTermDetailsType.setSequenceNumber(travelRestrictionTermsBean.getSequenceNumber());
                            travelRestrictionTermDetailsType.setTermCode(travelRestrictionTermsBean.getTermsCode());
                            travelRestrictionTermDetailsType.setTermDescription("ADDED "+travelRestrictionTermsBean.getTermsDescription());
                            travelRestrictionTermType.getTermDetails().add(travelRestrictionTermDetailsType);
                        }
                    } //for loop ends
                    //fix case 1728 begin
                }  // more than one term available ends
                //fix case 1728 end
                for (int indx=0;indx <cvPrevTermsCodes.size();indx++) {
                        awardTermsBean = (AwardTermsBean)cvPrevTermsCodes.get(indx);
                        eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                        CoeusVector cvCheck = cvTermsCodes.filter(eqTerms);
                        if (cvCheck.size()==0) { // checking whether the term is there in the current seq number
                            CoeusVector cvFilteredTravelRestrictionData = cvTermsData.filter(eqTerms);
                            AwardTermsBean travelRestrictionTermsBean = (AwardTermsBean)cvFilteredTravelRestrictionData.get(0);
                            //should not create this again.
                            //AwardType.AwardTermsDetailsType awardTermsDetailsType = new AwardTypeImpl.AwardTermsDetailsTypeImpl();
                            //awardType.setAwardTermsDetails(awardTermsDetailsType);
                            TermDetailsType travelRestrictionTermDetailsType = new TermDetailsTypeImpl() ;
                            travelRestrictionTermDetailsType.setAwardNumber(mitAwardNumber);
                            travelRestrictionTermDetailsType.setSequenceNumber(travelRestrictionTermsBean.getSequenceNumber());
                            travelRestrictionTermDetailsType.setTermCode(travelRestrictionTermsBean.getTermsCode());
                            travelRestrictionTermDetailsType.setTermDescription("DELETED "+travelRestrictionTermsBean.getTermsDescription());
                            travelRestrictionTermType.getTermDetails().add(travelRestrictionTermDetailsType);
                        }
                    }

                    //awardType.getAwardTermsDetails().getTerm().add(equipmentTermType);
                //fix case 1728 begin
//                }  // more than one term available ends
                //fix case 1728 end
                //eqTerms = new Equals("termsCode",new Integer(firstTermsBean.getTermsCode()));
                //CoeusVector cvCheckPrev = cvPrevTermsCodes.filter(eqTerms);
//                if (subcontractApprovalTermType.getTermDetails().size()>0) {
//                    awardType.getAwardTermsDetails().getTerm().add(subcontractApprovalTermType);
//                }
                if (travelRestrictionTermType.getTermDetails() != null && travelRestrictionTermType.getTermDetails().size()>0) {
                    awardType.getAwardTermsDetails().getTerm().add(travelRestrictionTermType);
                }
            }
            //Travel end

                /*if (cvTermsCodes != null) {
                    for (int indx=0;indx<cvTermsCodes.size();indx++) {
                        awardTermsBean = (AwardTermsBean)cvTermsCodes.get(indx);
                        eqTerms = new Equals("termsCode",new Integer(awardTermsBean.getTermsCode()));
                        CoeusVector cvFilteredTravelRestrictionData = cvTermsData.filter(eqTerms);

                        AwardTermsBean travelRestrictionTermsBean = (AwardTermsBean)cvFilteredTravelRestrictionData.get(0);

                        TermDetailsType travelRestrictionTermDetailsType = new TermDetailsTypeImpl();
                        travelRestrictionTermDetailsType.setAwardNumber(mitAwardNumber);
                        travelRestrictionTermDetailsType.setSequenceNumber(seqNumber);
                        travelRestrictionTermDetailsType.setTermCode(travelRestrictionTermsBean.getTermsCode());
                        travelRestrictionTermDetailsType.setTermDescription(travelRestrictionTermsBean.getTermsDescription());
                        travelRestrictionTermType.getTermDetails().add(travelRestrictionTermDetailsType);
                    }
                    awardType.getAwardTermsDetails().getTerm().add(travelRestrictionTermType);
                }*/
            // } //end of if (seqNumber == currentSeqNumber)

            AwardType.AwardSpecialItemsType awardSpecialItemsType = new AwardTypeImpl.AwardSpecialItemsTypeImpl();
            String approvedEquipmentIndicator=awardBean.getApprvdEquipmentIndicator();
            String approvedEquipmentModified = approvedEquipmentIndicator.substring(1,2);
            if (approvedEquipmentModified.equals("1")) {
                CoeusVector cvApprovedEquipment = (CoeusVector) awardDeltaReportTxnBean.getAwardApprovedEquipmentForSeq(mitAwardNumber, sequnceNumber);
                if (cvApprovedEquipment!=null && cvApprovedEquipment.size()>0) {
                    AwardType.AwardSpecialItemsType.EquipmentType equipmentType;
                    for (int index=0; index<cvApprovedEquipment.size();index++) {
                        AwardApprovedEquipmentBean awardApprovedEquipmentBean = (AwardApprovedEquipmentBean) cvApprovedEquipment.get(index);
                        equipmentType = new AwardTypeImpl.AwardSpecialItemsTypeImpl.EquipmentTypeImpl();
                        BigDecimal bdecAmount = new  BigDecimal(awardApprovedEquipmentBean.getAmount());
                        equipmentType.setAmount(bdecAmount.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                        equipmentType.setAwardNumber(awardApprovedEquipmentBean.getMitAwardNumber());
                        equipmentType.setItem(awardApprovedEquipmentBean.getItem());
                        equipmentType.setModel(awardApprovedEquipmentBean.getModel());
                        equipmentType.setSequenceNumber(awardApprovedEquipmentBean.getSequenceNumber());
                        equipmentType.setVendor(awardApprovedEquipmentBean.getVendor());
                        awardSpecialItemsType.getEquipment().add(equipmentType);
                    }
                }else {
                    awardNotice.getPrintRequirement().setEquipmentRequired("2"); //to indicator deleted
                }
            } else {
                awardNotice.getPrintRequirement().setEquipmentRequired("0");
            }
            String foreignTripIndicator=awardBean.getApprvdForeignTripIndicator();
            String foreignTripModified = foreignTripIndicator.substring(1,2);
            if (foreignTripModified.equals("1")) {
                CoeusVector cvApprovedForeignTrip = (CoeusVector) awardDeltaReportTxnBean.getAwardApprovedForeignTripForSeq(mitAwardNumber, sequnceNumber);
                if (cvApprovedForeignTrip!=null && cvApprovedForeignTrip.size()>0) {
                    AwardType.AwardSpecialItemsType.ForeignTravelType foreignTravelType;
                    for (int index=0; index<cvApprovedForeignTrip.size();index++) {
                        AwardApprovedForeignTripBean awardApprovedForeignTripBean = (AwardApprovedForeignTripBean) cvApprovedForeignTrip.get(index);
                        foreignTravelType= new AwardTypeImpl.AwardSpecialItemsTypeImpl.ForeignTravelTypeImpl();
                        BigDecimal bdecAmount = new BigDecimal(awardApprovedForeignTripBean.getAmount());
                        foreignTravelType.setAmount(bdecAmount.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                        foreignTravelType.setAwardNumber(awardApprovedForeignTripBean.getMitAwardNumber());
                        if (awardApprovedForeignTripBean.getDateFrom()!=null) {
                            Calendar dateFrom = Calendar.getInstance();
                            dateFrom.setTime(awardApprovedForeignTripBean.getDateFrom());
                            foreignTravelType.setDateFrom(dateFrom);
                        }
                        if (awardApprovedForeignTripBean.getDateTo() !=null) {
                            Calendar dateTo = Calendar.getInstance();
                            dateTo.setTime(awardApprovedForeignTripBean.getDateTo());
                            foreignTravelType.setDateTo(dateTo);
                        }
                        foreignTravelType.setDestination(awardApprovedForeignTripBean.getDestination());
                        foreignTravelType.setPersonId(awardApprovedForeignTripBean.getPersonId());
                        foreignTravelType.setPersonName(awardApprovedForeignTripBean.getPersonName());
                        foreignTravelType.setSequenceNumber(awardApprovedForeignTripBean.getSequenceNumber());
                        awardSpecialItemsType.getForeignTravel().add(foreignTravelType);
                    }
                }else{
                    awardNotice.getPrintRequirement().setForeignTravelRequired("2");//to indicator deleted
                }
            } else {
                awardNotice.getPrintRequirement().setForeignTravelRequired("0");
            }

            String approvedSubcontractIndicator=awardBean.getApprvdSubcontractIndicator();
            String approvedSubcontractModified = approvedSubcontractIndicator.substring(1,2);
            if (approvedSubcontractModified.equals("1")) {
                CoeusVector cvApprovedSubcontract = (CoeusVector) awardDeltaReportTxnBean.getAwardApprovedSubcontractForSeq(mitAwardNumber,sequnceNumber);
                if (cvApprovedSubcontract!=null && cvApprovedSubcontract.size()>0) {
                    AwardType.AwardSpecialItemsType.SubcontractType subcontractType;
                    AwardApprovedSubcontractBean awardApprovedSubcontractBean;
                    for (int index=0; index<cvApprovedSubcontract.size();index++) {
                        awardApprovedSubcontractBean = (AwardApprovedSubcontractBean) cvApprovedSubcontract.get(index);
                        subcontractType = new AwardTypeImpl.AwardSpecialItemsTypeImpl.SubcontractTypeImpl();
                        BigDecimal bdecAmount = new BigDecimal(awardApprovedSubcontractBean.getAmount());
                        subcontractType.setAmount(bdecAmount.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                        subcontractType.setAwardNumber(awardApprovedSubcontractBean.getMitAwardNumber());
                        subcontractType.setSequenceNumber(awardApprovedSubcontractBean.getSequenceNumber());
                        subcontractType.setSubcontractorName(awardApprovedSubcontractBean.getSubcontractName());
                        awardSpecialItemsType.getSubcontract().add(subcontractType);
                    }
                }else{
                    awardNotice.getPrintRequirement().setSubcontractRequired("2");//to indicator Subcontract information deleted
                }
            } else {
                awardNotice.getPrintRequirement().setSubcontractRequired("0");
            }
            awardType.setAwardSpecialItems(awardSpecialItemsType);
                /*String moduleId = awardDeltaReportTxnBean.getParameterValue("TECHNICAL_MANAGEMENT_CLASS_CODE");
                if (moduleId == null) {
                    CoeusOptionPane.showInfoDialog("Parameter value for TECHNICAL_MANAGEMENT_CLASS_CODE missing in osp$parameter table");
                }*/
            //Award Report terms
            CoeusVector cvReportTerms = awardDeltaReportTxnBean.getAwardReportTermsForSeq(mitAwardNumber, sequnceNumber);
            if (cvReportTerms !=null) {
                cvReportTerms.sort("reportClassCode");
                AwardAddRepReqTxnBean awardAddRepReqTxnBean  = new AwardAddRepReqTxnBean();
                CoeusVector awardReportClassData = awardAddRepReqTxnBean.getRepClassInAwardRep(mitAwardNumber);

                AwardReportTermsBean awardReportTermsBean;
                ReportTermDetailsType reportTermDetailsType;
                ReportTermType reportTermType;
                AwardType.AwardReportingDetailsType awardReportingDetailsType = new AwardTypeImpl.AwardReportingDetailsTypeImpl();
                awardType.setAwardReportingDetails(awardReportingDetailsType);
                int classCode=0;
                for (int index=0;index<cvReportTerms.size();index++) {
                    awardReportTermsBean =(AwardReportTermsBean) cvReportTerms.get(index);
                    reportTermDetailsType = new ReportTermDetailsTypeImpl();
                    reportTermDetailsType.setAwardNumber(mitAwardNumber);
                    if (awardReportTermsBean.getDueDate()!=null){
                        Calendar dueDate = Calendar.getInstance();
                        dueDate.setTime(awardReportTermsBean.getDueDate());
                        reportTermDetailsType.setDueDate(dueDate);
                    }
                    reportTermDetailsType.setFrequencyBaseCode(awardReportTermsBean.getFrequencyBaseCode());
                    reportTermDetailsType.setFrequencyBaseDesc(awardReportTermsBean.getFrequencyBaseDescription());
                    reportTermDetailsType.setFrequencyCode(awardReportTermsBean.getFrequencyCode());
                    reportTermDetailsType.setFrequencyCodeDesc(awardReportTermsBean.getFrequencyDescription());
                    reportTermDetailsType.setOSPDistributionCode(awardReportTermsBean.getOspDistributionCode());
                    reportTermDetailsType.setOSPDistributionDesc(awardReportTermsBean.getOspDistributionDescription());
                    reportTermDetailsType.setReportClassCode(awardReportTermsBean.getReportClassCode());

                    reportTermDetailsType.setReportCode(awardReportTermsBean.getReportCode());
                    reportTermDetailsType.setReportCodeDesc(awardReportTermsBean.getReportDescription());
                    reportTermDetailsType.setSequenceNumber(seqNumber);
                    Vector classDescVec = departmentPersonTxnBean.getArgumentCodeDescription("REPORT CLASS");
                    CoeusVector cvClassDesc=new CoeusVector();
                    cvClassDesc.addAll(classDescVec);
                    Equals eqClass = new Equals("code",""+awardReportTermsBean.getReportClassCode());
                    CoeusVector cvClass = cvClassDesc.filter(eqClass);
                    reportTermType = new ReportTermTypeImpl();
                    ComboBoxBean classBean = (ComboBoxBean)cvClass.get(0);

                    if (awardReportTermsBean.getReportClassCode()==classCode) {
                        reportTermType.setDescription("");
                    } else {
                        reportTermType.setDescription(classBean.getDescription());
                        classCode = awardReportTermsBean.getReportClassCode();
                    }

                    reportTermDetailsType.setReportClassDesc(classBean.getDescription());

                    // for mailCopiesType need be group.
                    //ReportTermDetailsType.MailCopiesType mailCopiesType = new ReportTermDetailsTypeImpl.MailCopiesTypeImpl();
                    Equals eqReportCode = new Equals("aw_ReportCode", new Integer(awardReportTermsBean.getReportCode()));
                    Equals eqFrequencyCode = new Equals("aw_FrequencyCode",new Integer(awardReportTermsBean.getFrequencyCode()));
                    Equals eqFrequencyBaseCode = new Equals("aw_FrequencyBaseCode",new Integer(awardReportTermsBean.getFrequencyBaseCode()));
                    Equals eqOspDistributionCode = new Equals("aw_OspDistributionCode", new Integer(awardReportTermsBean.getOspDistributionCode()));
                    Equals eqDueDate = new Equals("dueDate", awardReportTermsBean.getDueDate());
                    Equals eqReportClassCode = new Equals("aw_ReportClassCode", new Integer(awardReportTermsBean.getReportClassCode()));
                    And eqAll = new And(new And(new And(eqReportCode, eqReportClassCode),new And(eqFrequencyBaseCode, eqOspDistributionCode)), new And(eqDueDate,eqFrequencyCode));
                    CoeusVector cvMailCopyType = new CoeusVector();
                   // CoeusVector cvMailCopyType1 = new CoeusVector();
                    cvMailCopyType = cvReportTerms.filter(eqAll);

                    if (cvMailCopyType !=null && cvMailCopyType.size()>0){
                        for (int mailIndex =0; mailIndex < cvMailCopyType.size(); mailIndex++){
                            ReportTermDetailsType.MailCopiesType mailCopiesType = new ReportTermDetailsTypeImpl.MailCopiesTypeImpl();
                            awardReportTermsBean =(AwardReportTermsBean) cvMailCopyType.get(mailIndex);
                            mailCopiesType.setNumberOfCopies(""+awardReportTermsBean.getNumberOfCopies());
                            mailCopiesType.setRolodexId(""+awardReportTermsBean.getRolodexId());
                            mailCopiesType.setContactTypeCode(awardReportTermsBean.getContactTypeCode());
                            mailCopiesType.setContactTypeDesc(awardReportTermsBean.getContactTypeDescription());
                            reportTermDetailsType.getMailCopies().add(mailCopiesType);
                        }

                        NotEquals notReportCode = new NotEquals("aw_ReportCode", new Integer(awardReportTermsBean.getReportCode()));
                        NotEquals notFrequencyCode = new NotEquals("aw_FrequencyCode",new Integer(awardReportTermsBean.getFrequencyCode()));
                        NotEquals notFrequencyBaseCode = new NotEquals("aw_FrequencyBaseCode",new Integer(awardReportTermsBean.getFrequencyBaseCode()));
                        NotEquals notOspDistributionCode = new NotEquals("aw_OspDistributionCode", new Integer(awardReportTermsBean.getOspDistributionCode()));
                        NotEquals notDueDate = new NotEquals("dueDate", awardReportTermsBean.getDueDate());
                        NotEquals notReportClassCode = new NotEquals("aw_ReportClassCode", new Integer(awardReportTermsBean.getReportClassCode()));
                        Or notAll = new Or(new Or(new Or(notReportCode, notReportClassCode),new Or(notFrequencyBaseCode, notOspDistributionCode)), new Or(notDueDate,notFrequencyCode));
                        cvReportTerms = cvReportTerms.filter(notAll);
                        index = -1;
                        cvReportTerms.sort("reportClassCode");
                    }
                    //end
                    //mailCopiesType.setNumberOfCopies(""+awardReportTermsBean.getNumberOfCopies());
                    // mailCopiesType.setRolodexId(""+awardReportTermsBean.getRolodexId());
                    //mailCopiesType.setContactTypeCode(awardReportTermsBean.getContactTypeCode());
                    //mailCopiesType.setContactTypeDesc(awardReportTermsBean.getContactTypeDescription());
                    //reportTermDetailsType.getMailCopies().add(mailCopiesType);
                    reportTermType.getReportTermDetails().add(reportTermDetailsType);
                    awardType.getAwardReportingDetails().getReportDetails().add(reportTermType);
                }
            }


            //getting the cost sharing
            CoeusVector cvCostSharing =awardDeltaReportTxnBean.getCostSharingForSeq(mitAwardNumber, sequnceNumber);
            AwardCostSharingBean awardCostSharingBean;
            AwardType.AwardCostSharingType.CostSharingItemType costSharingItemType;
            AwardType.AwardCostSharingType awardCostSharingType = new AwardTypeImpl.AwardCostSharingTypeImpl();
            awardType.setAwardCostSharing(awardCostSharingType);
            if (cvCostSharing != null) {
                for (int index=0;index<cvCostSharing.size();index++) {
                    awardCostSharingBean= (AwardCostSharingBean)cvCostSharing.get(index);
                    costSharingItemType = new AwardTypeImpl.AwardCostSharingTypeImpl.CostSharingItemTypeImpl();
                    costSharingItemType.setAwardNumber(mitAwardNumber);
                    BigDecimal bdecAmount = new BigDecimal(awardCostSharingBean.getAmount());
                    costSharingItemType.setAmount(bdecAmount.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                    //costSharingItemType.setCostSharingDescription(awardCostSharingBean.getCostSharingType());
                    InstituteProposalLookUpDataTxnBean instituteProposalLookUpDataTxnBean = new InstituteProposalLookUpDataTxnBean();
                    CoeusVector cvCostSharingTypes = instituteProposalLookUpDataTxnBean.getCostSharingType();
                    Equals eqCSTypeCode = new Equals("code",""+awardCostSharingBean.getCostSharingType());
                    CoeusVector cvFilteredCSTypes= cvCostSharingTypes.filter(eqCSTypeCode);
                    ComboBoxBean csTypeBean = null;
                    if (cvFilteredCSTypes!=null && cvFilteredCSTypes.size()>0) {
                        csTypeBean = (ComboBoxBean)cvFilteredCSTypes.get(0);
                        costSharingItemType.setCostSharingDescription(csTypeBean.getDescription());
                    }
                    costSharingItemType.setCostSharingType(awardCostSharingBean.getCostSharingType());
                    costSharingItemType.setDestinationAccount(awardCostSharingBean.getDestinationAccount());
                    costSharingItemType.setFiscalYear(awardCostSharingBean.getFiscalYear());
                    costSharingItemType.setPercentage(awardCostSharingBean.getCostSharingPercentage());
                    costSharingItemType.setSequenceNumber(seqNumber);
                    costSharingItemType.setSourceAccount(awardCostSharingBean.getSourceAccount());
                    awardType.getAwardCostSharing().getCostSharingItem().add(costSharingItemType);
                }
            }

            Equals eqCSComment = new Equals("commentCode",new Integer(COST_SHARING_COMMENT));
            CoeusVector cvCSComments = cvAwardCommentsForSeq.filter(eqCSComment);
            if (cvCSComments!=null && cvCSComments.size()>0) {
                AwardCommentsBean costSharingCommentBean = (AwardCommentsBean)cvCSComments.get(0);
                //need check seqNum of the costSharingComment.
                if (costSharingCommentBean != null && costSharingCommentBean.getSequenceNumber() == seqNumber){
                    awardType.getAwardCostSharing().setComments(costSharingCommentBean.getComments());
                }
            }

            //IDC Rates
            //getting the Award IDC Rates
            String idcIndicator = awardBean.getIdcIndicator();
            String idcModified=idcIndicator.substring(1,2);

            CoeusVector cvIDCRates = awardDeltaReportTxnBean.getAwardIDCRateForSeq(mitAwardNumber, sequnceNumber);
            AwardType.AwardIndirectCostsType.IndirectCostSharingItemType indirectCostSharingItemType;
            AwardType.AwardIndirectCostsType awardIndirectCostsType = new AwardTypeImpl.AwardIndirectCostsTypeImpl();
            awardType.setAwardIndirectCosts(awardIndirectCostsType);
            if (idcModified.equals("1")) {
                if (cvIDCRates != null) {
                    for (int index=0;index<cvIDCRates.size();index++) {
                        AwardIDCRateBean awardIDCRateBean = (AwardIDCRateBean)cvIDCRates.get(index);
                        indirectCostSharingItemType =
                        new AwardTypeImpl.AwardIndirectCostsTypeImpl.IndirectCostSharingItemTypeImpl();
                        BigDecimal bdecApplicableRate = new BigDecimal(awardIDCRateBean.getApplicableIDCRate());
                        indirectCostSharingItemType.setApplicableRate(bdecApplicableRate.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                        indirectCostSharingItemType.setAwardNumber(awardIDCRateBean.getMitAwardNumber());
                        indirectCostSharingItemType.setCampus(awardIDCRateBean.isOnOffCampusFlag());
                        indirectCostSharingItemType.setDestinationAccount(awardIDCRateBean.getDestinationAccount());
                        Calendar endDate = Calendar.getInstance();
                        if(awardIDCRateBean.getEndDate()!=null){
                            endDate.setTime(awardIDCRateBean.getEndDate());
                            indirectCostSharingItemType.setEndDate(endDate);
                        }
                        indirectCostSharingItemType.setFiscalYear(awardIDCRateBean.getFiscalYear());
                        indirectCostSharingItemType.setIDCRateType(awardIDCRateBean.getIdcRateTypeCode());
                        InstituteProposalLookUpDataTxnBean instituteProposalLookUpDataTxnBean = new InstituteProposalLookUpDataTxnBean();
                        CoeusVector cvIDCRateTypes = instituteProposalLookUpDataTxnBean.getIDCRateType();
                        Equals eqRateTypeCode = new Equals("code",""+awardIDCRateBean.getIdcRateTypeCode());
                        CoeusVector cvFilteredRateTypes= cvIDCRateTypes.filter(eqRateTypeCode);
                        ComboBoxBean ratesTypeBean = null;
                        if (cvFilteredRateTypes!=null && cvFilteredRateTypes.size()>0) {
                            ratesTypeBean = (ComboBoxBean)cvFilteredRateTypes.get(0);
                            indirectCostSharingItemType.setIDCRateDescription(ratesTypeBean.getDescription());
                        }
                        indirectCostSharingItemType.setSequenceNumber(awardIDCRateBean.getSequenceNumber());
                        indirectCostSharingItemType.setSourceAccount(awardIDCRateBean.getSourceAccount());
                        if (awardIDCRateBean.getStartDate()!=null){
                            Calendar startDate = Calendar.getInstance();
                            startDate.setTime(awardIDCRateBean.getStartDate());
                            indirectCostSharingItemType.setStartDate(startDate);
                        }
                        BigDecimal bdecUnderRecoveryAmount = new BigDecimal(awardIDCRateBean.getUnderRecoveryOfIDC());
                        indirectCostSharingItemType.setUnderRecoveryAmount(bdecUnderRecoveryAmount.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                        awardType.getAwardIndirectCosts().getIndirectCostSharingItem().add(indirectCostSharingItemType);
                    }
                }
            }
            Equals eqIDCComment = new Equals("commentCode",new Integer(IDC_COMMENT));
            CoeusVector cvIDCComments = cvAwardCommentsForSeq.filter(eqIDCComment);
            if (cvIDCComments!=null && cvIDCComments.size()>0) {
                AwardCommentsBean idcCommentBean = (AwardCommentsBean)cvIDCComments.get(0);
                //need check seqNum of the idcComment.
                if (idcCommentBean != null && idcCommentBean.getSequenceNumber() == seqNumber){
                    awardType.getAwardIndirectCosts().setComments(idcCommentBean.getComments());
                }
            }
            //getting payment schedule

            //added PaymentDetails following code.
            String paymentScheduleIndicator = awardBean.getPaymentScheduleIndicator();
            String paymentScheduleModified = paymentScheduleIndicator.substring(1,2);
            //AwardType.AwardIndirectCostsType awardIndirectCostsType = new AwardTypeImpl.AwardIndirectCostsTypeImpl();
            //awardType.setAwardIndirectCosts(awardIndirectCostsType);
            if (paymentScheduleModified.equals("1")) {
                CoeusVector cvPaymentSchedule= awardDeltaReportTxnBean.getAwardPaymentScheduleForSeq(mitAwardNumber,sequnceNumber);
                if (cvPaymentSchedule!=null) {
                    if (cvPaymentSchedule.size() > 0 ) {
                        if (((AwardPaymentScheduleBean)cvPaymentSchedule.get(0)).getSequenceNumber() != seqNumber){
                            awardNotice.getPrintRequirement().setPaymentRequired("2"); //to indicator deleted
                        }else {
                            AwardPaymentScheduleBean awardPaymentScheduleBean;
                            AwardType.AwardPaymentSchedulesType.PaymentScheduleType paymentScheduleType;
                            AwardType.AwardPaymentSchedulesType awardPaymentSchedulesType = new AwardTypeImpl.AwardPaymentSchedulesTypeImpl();
                            awardType.setAwardPaymentSchedules(awardPaymentSchedulesType);
                            for (int index=0;index<cvPaymentSchedule.size();index++) {
                                awardPaymentScheduleBean = (AwardPaymentScheduleBean)cvPaymentSchedule.get(index);
                                paymentScheduleType = new AwardTypeImpl.AwardPaymentSchedulesTypeImpl.PaymentScheduleTypeImpl();
                                BigDecimal bdecAmount = new BigDecimal(awardPaymentScheduleBean.getAmount());
                                paymentScheduleType.setAmount(bdecAmount.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                                paymentScheduleType.setAwardNumber(awardPaymentScheduleBean.getMitAwardNumber());
                                if (awardPaymentScheduleBean.getDueDate()!= null){
                                    Calendar paymentDueDate = Calendar.getInstance();
                                    paymentDueDate.setTime(awardPaymentScheduleBean.getDueDate());
                                    paymentScheduleType.setDueDate(paymentDueDate);
                                }
                                paymentScheduleType.setInvoiceNumber(awardPaymentScheduleBean.getInvoiceNumber());
                                paymentScheduleType.setSequenceNumber(awardPaymentScheduleBean.getSequenceNumber());
                                paymentScheduleType.setStatusDescription(awardPaymentScheduleBean.getStatusDescription());
                                if (awardPaymentScheduleBean.getSubmitDate()!=null) {
                                    Calendar paymentSubDate = Calendar.getInstance();
                                    paymentSubDate.setTime(awardPaymentScheduleBean.getSubmitDate());
                                    paymentScheduleType.setSubmitDate(paymentSubDate);
                                }
                                paymentScheduleType.setSubmittedBy(awardPaymentScheduleBean.getSubmitBy());
                                awardType.getAwardPaymentSchedules().getPaymentSchedule().add(paymentScheduleType);
                            }
                        }
                    }

                }
            }

           //Disclosure Notice
            //Check for any OSP right for the user
            // To access the user rights
            AwardDisclosureType awardDisclosureType=null;
            UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
            boolean hasAnyOSPRight = userMaintDataTxnBean.getUserHasAnyOSPRight(loggedInUser);
            if (hasAnyOSPRight) {
              //coeusdev-980
                //for award validation need to check osp right?
                AwardUpdateTxnBean awardUpdateTxnBean = new AwardUpdateTxnBean(loggedInUser);
                String awardValidation = coeusFunctions.getParameterValue("ENABLE_AWARD_VALIDATIONS");
                awardDisclosureType = new AwardDisclosureTypeImpl();
                awardDisclosureType.setAwardHeader(awardHeaderType);
                awardDisclosureType.setEnableAwardValidation(awardValidation);
                if (awardValidation.equals("1")){
                   if (awardReportTxnBean.getIsAllDiscStatusComplete(mitAwardNumber, 1) != 1 ){
                       awardDisclosureType.setDisclosureValidation("1");
                       CoeusVector cvCoiDisclForItem = awardReportTxnBean.getCoiDisclForAward(mitAwardNumber,1);

                      if (cvCoiDisclForItem!=null && cvCoiDisclForItem.size()>0) {
                        CoiDisclForItemBean coiDisclForItemBean;
                        for (int index = 0; index < cvCoiDisclForItem.size(); index++) {
                        coiDisclForItemBean = (CoiDisclForItemBean)cvCoiDisclForItem.get(index);
                        DisclosureItemType disclosureItemType = new DisclosureItemTypeImpl();
                        disclosureItemType.setDisclosureNumber(coiDisclForItemBean.getCoiDisclosureNumber());
                        disclosureItemType.setDisclosureStatusCode(coiDisclForItemBean.getCoiDisclosureStatusCode());
                        disclosureItemType.setDisclosureStatusDesc(coiDisclForItemBean.getCoiDisclosureStatusCode());
                        disclosureItemType.setDisclosureTypeCode(coiDisclForItemBean.getDisclosureType());
                        disclosureItemType.setDisclosureTypeDesc(coiDisclForItemBean.getDisclosureType());
                        disclosureItemType.setPersonId(coiDisclForItemBean.getPersonId());
                        disclosureItemType.setPersonName(coiDisclForItemBean.getPersonId());
                        awardDisclosureType.getDisclosureItem().add(disclosureItemType);
                        }
                      }

                   }else {
                       awardDisclosureType.setDisclosureValidation("0");
                   }
//
                    Vector vecValidations = awardUpdateTxnBean.performAwardValidations(mitAwardNumber);
                    if (vecValidations != null && vecValidations.size() > 0){
                        for (int index = 0; index < vecValidations.size(); index++) {
                            AwardValidationType awardValidationType = new AwardValidationTypeImpl();
                            awardValidationType.setValidationDetails(vecValidations.get(index).toString());
                            awardDisclosureType.getAwardValidation().add(awardValidationType);

                        }
                    }
                }

                /*if (awardReportTxnBean.getIsAllDiscStatusComplete(mitAwardNumber, 1) != 1 ){
                    CoeusVector cvCoiDisclForItem = awardReportTxnBean.getCoiDisclForAward(mitAwardNumber,1);

                    if (cvCoiDisclForItem!=null && cvCoiDisclForItem.size()>0) {

                        awardDisclosureType = new AwardDisclosureTypeImpl();
                        awardDisclosureType.setAwardHeader(awardHeaderType);
                        CoiDisclForItemBean coiDisclForItemBean;
                        for (int index = 0; index < cvCoiDisclForItem.size(); index++) {
                            coiDisclForItemBean = (CoiDisclForItemBean)cvCoiDisclForItem.get(index);
                            DisclosureItemType disclosureItemType = new DisclosureItemTypeImpl();
                            disclosureItemType.setDisclosureNumber(coiDisclForItemBean.getCoiDisclosureNumber());
                            disclosureItemType.setDisclosureStatusCode(coiDisclForItemBean.getCoiDisclosureStatusCode());
                            disclosureItemType.setDisclosureStatusDesc(coiDisclForItemBean.getCoiDisclosureStatusCode());
                            disclosureItemType.setDisclosureTypeCode(coiDisclForItemBean.getDisclosureType());
                            disclosureItemType.setDisclosureTypeDesc(coiDisclForItemBean.getDisclosureType());
                            disclosureItemType.setPersonId(coiDisclForItemBean.getPersonId());
                            disclosureItemType.setPersonName(coiDisclForItemBean.getPersonId());
                            awardDisclosureType.getDisclosureItem().add(disclosureItemType);
                        }
                    }
                }*/
                 //coeusdev-980
            }

            //Award Contacts
            CoeusVector cvAwardContacts = awardDeltaReportTxnBean.getAwardContactsForSeq(mitAwardNumber,sequnceNumber);
            AwardContactDetailsBean awardContactDetailsBean;
            AwardType.AwardContactsType awardContactsType = new AwardTypeImpl.AwardContactsTypeImpl();
            awardType.setAwardContacts(awardContactsType);
            RolodexDetailsType rolodexDetailsType;
            if (cvAwardContacts!=null){
                for (int index=0;index<cvAwardContacts.size();index++) {
                    awardContactDetailsBean = (AwardContactDetailsBean)cvAwardContacts.get(index);
                    rolodexDetailsType = new RolodexDetailsTypeImpl();
                    rolodexDetailsType.setAddress1(awardContactDetailsBean.getAddress1());
                    rolodexDetailsType.setAddress2(awardContactDetailsBean.getAddress2());
                    rolodexDetailsType.setAddress3(awardContactDetailsBean.getAddress3());
                    rolodexDetailsType.setCity(awardContactDetailsBean.getCity());
                    rolodexDetailsType.setComments(awardContactDetailsBean.getComments());
                    rolodexDetailsType.setCountryCode(awardContactDetailsBean.getCountryCode());
                    rolodexDetailsType.setCountryDescription(awardContactDetailsBean.getCountryName());
                    rolodexDetailsType.setCounty(awardContactDetailsBean.getCounty());
                    rolodexDetailsType.setEmail(awardContactDetailsBean.getEmailAddress());
                    rolodexDetailsType.setFax(awardContactDetailsBean.getFaxNumber());
                    StringBuffer name = new StringBuffer();
                    if (awardContactDetailsBean.getLastName() != null) {
                        name.append(awardContactDetailsBean.getLastName());
                    }
                    //case 1916 start
                    if (awardContactDetailsBean.getSuffix() != null) {
                        name.append(" "+awardContactDetailsBean.getSuffix());
                    }
                    if (awardContactDetailsBean.getPrefix() != null) {
                        name.append(", "+awardContactDetailsBean.getPrefix());
                        if (awardContactDetailsBean.getFirstName() != null) {
                            name.append(" "+awardContactDetailsBean.getFirstName());
                        }
                    }else if (awardContactDetailsBean.getFirstName() != null) {
                        name.append(", "+awardContactDetailsBean.getFirstName());
                    }
                    //case 1916 end
                    if (awardContactDetailsBean.getMiddleName() != null) {
                        name.append(" "+awardContactDetailsBean.getMiddleName());
                    }
                    rolodexDetailsType.setLastName(name.toString());
                    rolodexDetailsType.setFirstName(awardContactDetailsBean.getFirstName());

                    rolodexDetailsType.setMiddleName(awardContactDetailsBean.getMiddleName());
                    rolodexDetailsType.setOrganization(awardContactDetailsBean.getOrganization());
                    //rolodexDetailsType.setOwnedByUnit(awardContactDetailsBean.get
                    //rolodexDetailsType.setOwnedByUnitName();
                    //rolodexDetailsType.setOwnedByUnitName();
                    rolodexDetailsType.setPhoneNumber(awardContactDetailsBean.getPhoneNumber());
                    rolodexDetailsType.setPincode(awardContactDetailsBean.getPostalCode());
                    rolodexDetailsType.setPrefix(awardContactDetailsBean.getPrefix());
                    rolodexDetailsType.setRolodexId(String.valueOf(awardContactDetailsBean.getRolodexId()));
                    rolodexDetailsType.setSponsorCode(awardContactDetailsBean.getSponsorCode());
                    rolodexDetailsType.setSponsorName(awardContactDetailsBean.getSponsorName());
                    rolodexDetailsType.setStateCode(awardContactDetailsBean.getState());
                    rolodexDetailsType.setStateDescription(awardContactDetailsBean.getStateName());
                    rolodexDetailsType.setSuffix(awardContactDetailsBean.getSuffix());
                    rolodexDetailsType.setTitle(awardContactDetailsBean.getTitle());
                    //awardType.getAwardContacts().getContactDetails().

                    AwardType.AwardContactsType.ContactDetailsType contactDetailsType  =
                    new AwardTypeImpl.AwardContactsTypeImpl.ContactDetailsTypeImpl();
                    contactDetailsType.setAwardNumber(mitAwardNumber);
                    contactDetailsType.setContactType(awardContactDetailsBean.getContactTypeCode());
                    contactDetailsType.setContactTypeDesc(awardContactDetailsBean.getContactTypeDescription());
                    contactDetailsType.setRolodexDetails(rolodexDetailsType);
                    contactDetailsType.setSequenceNumber(seqNumber);
                    awardContactsType.getContactDetails().add(contactDetailsType);
                }
            }

            //begin case 1390
            //add funding proposal info to the delta report
            CoeusVector cvFundingProposal = awardDeltaReportTxnBean.getFundingProposalsForAward(mitAwardNumber,sequnceNumber);

             if (cvFundingProposal != null && cvFundingProposal.size() > 0 ) {
                if (((AwardFundingProposalBean)cvFundingProposal.get(0)).getSequenceNumber() == seqNumber){
                    AwardFundingProposalBean awardFundingProposalBean;
                    AwardType.AwardFundingProposalsType.FundingProposalType fundingProposalType;
                    AwardType.AwardFundingProposalsType awardFundingProposalsType = new AwardTypeImpl.AwardFundingProposalsTypeImpl();
                    awardType.setAwardFundingProposals(awardFundingProposalsType);

                    for (int index=0;index<cvFundingProposal.size();index++) {
                        awardFundingProposalBean = (AwardFundingProposalBean)cvFundingProposal.get(index);
                        fundingProposalType = new AwardTypeImpl.AwardFundingProposalsTypeImpl.FundingProposalTypeImpl();
                        fundingProposalType.setProposalNumber(awardFundingProposalBean.getProposalNumber());
                        fundingProposalType.setSequenceNumber(awardFundingProposalBean.getSequenceNumber());
                        awardType.getAwardFundingProposals().getFundingProposal().add(fundingProposalType);
                    }//end for loop
                }
            }
            // end case 1390
            // get the user

            PersonInfoBean personBean = awardReportTxnBean.getAoInfo(unitNumber);
            AwardNoticeType.AODetailsType aODetailsType = new AwardNoticeTypeImpl.AODetailsTypeImpl();
            if (personBean.getOffLocation()!=null) {
                aODetailsType.setAOAddress(personBean.getOffLocation());
            }
            if (personBean.getFullName()!=null) {
                aODetailsType.setAOName(personBean.getFullName());
            }

            awardNotice.setAward(awardType);

            awardNotice.setAODetails(aODetailsType);
            if (awardDisclosureType!=null) {
                awardNotice.setAwardDisclosure(awardDisclosureType);
            }
            InputStream is = getClass().getResourceAsStream("/coeus.properties");
            Properties coeusProps = new Properties();
            coeusProps.load(is);
            String schoolName = coeusProps.getProperty("SCHOOL_NAME");
            String schoolAcronym = coeusProps.getProperty("SCHOOL_ACRONYM");
            // SchoolInfoType
            SchoolInfoType schoolInfoType = new SchoolInfoTypeImpl();
            schoolInfoType.setSchoolName(schoolName);
            schoolInfoType.setAcronym(schoolAcronym);
            awardNotice.setSchoolInfo(schoolInfoType);

            // Transfering funds
            //getting the award sponsor funding
            //use indicator to check modification of TransferringSponsors.
            String transferSponsorIndicator = awardBean.getTransferSponsorIndicator();
            String transferSponsorModified=transferSponsorIndicator.substring(1,2);
            AwardType.AwardTransferringSponsorsType awardTransferringSponsorsType = new AwardTypeImpl.AwardTransferringSponsorsTypeImpl();
            if (transferSponsorModified.equals("1")) {
                CoeusVector cvTransferingSponsors = awardDeltaReportTxnBean.getAwardSponsorFundingForSeq(mitAwardNumber, sequnceNumber);
                CoeusVector cvPrevTransferingSponsors = awardDeltaReportTxnBean.getAwardSponsorFundingForSeq(mitAwardNumber, ""+(seqNumber-1));

                AwardTransferingSponsorBean awardTransferingSponsorBean=null;
                if (cvTransferingSponsors!=null && cvTransferingSponsors.size()>0) {
                    if (((AwardTransferingSponsorBean)cvTransferingSponsors.get(0)).getSequenceNumber() != seqNumber){
                        awardNotice.getPrintRequirement().setFlowThruRequired("2"); //to indicator deleted
                    }else {
                        for (int index=0;index<cvTransferingSponsors.size();index++) {
                            awardTransferingSponsorBean= (AwardTransferingSponsorBean)cvTransferingSponsors.get(index);
                            if (cvPrevTransferingSponsors!=null && cvPrevTransferingSponsors.size()>0) {
                                Equals eqSponsorCode = new Equals("sponsor_code",""+awardTransferingSponsorBean.getSponsorCode());
                                CoeusVector cvCheckPrev = cvPrevTransferingSponsors.filter(eqSponsorCode);
                                if (cvCheckPrev.size()!= 0){
                                    continue;
                                }
                            }

                            AwardType.AwardTransferringSponsorsType.TransferringSponsorType transferringSponsorType =
                            new AwardTypeImpl.AwardTransferringSponsorsTypeImpl.TransferringSponsorTypeImpl();
                            transferringSponsorType.setAwardNumber(mitAwardNumber);
                            transferringSponsorType.setSequenceNumber(seqNumber);
                            transferringSponsorType.setSponsorCode("ADDED - " +awardTransferingSponsorBean.getSponsorCode());
                            transferringSponsorType.setSponsorDescription(awardTransferingSponsorBean.getSponsorName());
                            awardType.setAwardTransferringSponsors(awardTransferringSponsorsType);
                            awardType.getAwardTransferringSponsors().getTransferringSponsor().add(transferringSponsorType);
                        }
                        // search for prev
                        if (cvPrevTransferingSponsors!=null && cvPrevTransferingSponsors.size()>0) {
                            for (int index=0;index<cvPrevTransferingSponsors.size();index++) {
                                awardTransferingSponsorBean= (AwardTransferingSponsorBean)cvPrevTransferingSponsors.get(index);
                                if (cvTransferingSponsors!=null && cvTransferingSponsors.size()>0) {
                                    Equals eqSponsorCode = new Equals("sponsor_code",""+awardTransferingSponsorBean.getSponsorCode());
                                    CoeusVector cvCheckPrev = cvTransferingSponsors.filter(eqSponsorCode);
                                    if (cvCheckPrev.size()!= 0){
                                        continue;
                                    }
                                }

                                AwardType.AwardTransferringSponsorsType.TransferringSponsorType transferringSponsorType =
                                new AwardTypeImpl.AwardTransferringSponsorsTypeImpl.TransferringSponsorTypeImpl();
                                transferringSponsorType.setAwardNumber(mitAwardNumber);
                                transferringSponsorType.setSequenceNumber(seqNumber-1); //??? need it??
                                transferringSponsorType.setSponsorCode("DELETED - " +awardTransferingSponsorBean.getSponsorCode());
                                transferringSponsorType.setSponsorDescription(awardTransferingSponsorBean.getSponsorName());
                                awardType.setAwardTransferringSponsors(awardTransferringSponsorsType);
                                awardType.getAwardTransferringSponsors().getTransferringSponsor().add(transferringSponsorType);

                            }
                        }
                    }

                }


            }
            //end use indicator to check modification of TransferringSponsors.
            /****
             * // Transfering funds
             * //getting the award sponsor funding
             * CoeusVector cvTransferingSponsor = awardDeltaReportTxnBean.getAwardSponsorFundingForSeq(mitAwardNumber, sequnceNumber);
             * CoeusVector cvPrevTransferingSponsor = awardDeltaReportTxnBean.getAwardSponsorFundingForSeq(mitAwardNumber, ""+(seqNumber-1));
             * AwardTransferingSponsorBean prevAwardTransferingSponsorBean=null;
             * if (cvPrevTransferingSponsor!=null && cvPrevTransferingSponsor.size()>0) {
             * prevAwardTransferingSponsorBean= (AwardTransferingSponsorBean)cvPrevTransferingSponsor.get(0);
             * }
             * AwardTransferingSponsorBean awardTransferingSponsorBean=null;
             * if (cvTransferingSponsor!=null && cvTransferingSponsor.size()>0) {
             *
             * awardTransferingSponsorBean= (AwardTransferingSponsorBean)cvTransferingSponsor.get(0);
             * AwardType.AwardTransferringSponsorsType.TransferringSponsorType transferringSponsorType =
             * new AwardTypeImpl.AwardTransferringSponsorsTypeImpl.TransferringSponsorTypeImpl();
             * transferringSponsorType.setAwardNumber(mitAwardNumber);
             * transferringSponsorType.setSequenceNumber(seqNumber);
             * transferringSponsorType.setSponsorCode(awardTransferingSponsorBean.getSponsorCode());
             * if (prevAwardTransferingSponsorBean != null && prevAwardTransferingSponsorBean.getSponsorCode() !=null) {
             * if (awardTransferingSponsorBean.getSponsorCode().equals(prevAwardTransferingSponsorBean.getSponsorCode())) {
             * transferringSponsorType.setSponsorCode(awardTransferingSponsorBean.getSponsorCode());
             * }  else {
             * if (awardTransferingSponsorBean.getSponsorCode() !=null) {
             * transferringSponsorType.setSponsorCode("* "+awardTransferingSponsorBean.getSponsorCode());
             * }
             * }
             * }
             * transferringSponsorType.setSponsorDescription(awardTransferingSponsorBean.getSponsorName());
             * AwardType.AwardTransferringSponsorsType awardTransferringSponsorsType = new AwardTypeImpl.AwardTransferringSponsorsTypeImpl();
             * awardType.setAwardTransferringSponsors(awardTransferringSponsorsType);
             * awardType.getAwardTransferringSponsors().getTransferringSponsor().add(transferringSponsorType);
             * }
             ****/

            //AwardCloseout
            CoeusVector cvCloseOut = awardDeltaReportTxnBean.getCloseOutForSeq(mitAwardNumber, sequnceNumber);
            CoeusVector cvPrevCloseOut = awardDeltaReportTxnBean.getCloseOutForSeq(mitAwardNumber, ""+(seqNumber-1));

            AwardCloseOutBean awardCloseOutBean;
            AwardCloseOutBean prevAwardCloseOutBean= null;
            if (cvCloseOut!=null && cvCloseOut.size()>0) {
                awardCloseOutBean= (AwardCloseOutBean)cvCloseOut.get(0);
                //if closeOut seqNum not equls current report sequnceNumber, nothing changed. no data need to print.
                if (awardCloseOutBean != null && awardCloseOutBean.getSequenceNumber() == seqNumber){

                    if (cvPrevCloseOut!=null && cvPrevCloseOut.size()>0) {
                        prevAwardCloseOutBean = (AwardCloseOutBean)cvPrevCloseOut.get(0);
                    }
                    AwardType.CloseOutDeadlinesType closeOutDeadlinesType = new AwardTypeImpl.CloseOutDeadlinesTypeImpl();
                    awardType.setCloseOutDeadlines(closeOutDeadlinesType);

                    awardType.getCloseOutDeadlines().setArchiveLocation(awardCloseOutBean.getArchiveLocation());

                    awardType.getCloseOutDeadlines().setAwardNumber(mitAwardNumber);

                    if (awardCloseOutBean.getCloseOutDate()!=null) {
                        Calendar closeOutDate = Calendar.getInstance();
                        closeOutDate.setTime(awardCloseOutBean.getCloseOutDate());
                        awardType.getCloseOutDeadlines().setCloseoutDate(closeOutDate);
                        if (prevAwardCloseOutBean != null) {
			    if ( prevAwardCloseOutBean.getCloseOutDate() == null||!(prevAwardCloseOutBean.getCloseOutDate().equals(awardCloseOutBean.getCloseOutDate()))) {
                               awardType.getCloseOutDeadlines().setCloseOutDateModified(" *");
                            }
                        }else {
                            awardType.getCloseOutDeadlines().setCloseOutDateModified(" *");
                        }
                    }else {
                        if (prevAwardCloseOutBean != null && prevAwardCloseOutBean.getCloseOutDate() != null){
                            awardType.getCloseOutDeadlines().setCloseOutDateModified(" *");
                        }
                    }

                    if (awardCloseOutBean.getFinalInvSubmissionDate()!=null) {
                        Calendar finalInvSubDate = Calendar.getInstance();
                        finalInvSubDate.setTime(awardCloseOutBean.getFinalInvSubmissionDate());
                        awardType.getCloseOutDeadlines().setFinalInvSubDate(finalInvSubDate);
                        if (prevAwardCloseOutBean != null ) {
                            if (prevAwardCloseOutBean.getFinalInvSubmissionDate()==null ||!(prevAwardCloseOutBean.getFinalInvSubmissionDate().equals(awardCloseOutBean.getFinalInvSubmissionDate()))){
                                awardType.getCloseOutDeadlines().setFinalInvSubDateModified(" *");
                            }

                        } else {
                            awardType.getCloseOutDeadlines().setFinalInvSubDateModified(" *");
                        }
                    }else {
                        if (prevAwardCloseOutBean != null && prevAwardCloseOutBean.getFinalInvSubmissionDate()!=null){
                            awardType.getCloseOutDeadlines().setFinalInvSubDateModified(" *");
                        }
                    }

                    if (awardCloseOutBean.getFinalPatentSubmissionDate()!=null) {
                        Calendar finalPatentSubDate = Calendar.getInstance();
                        finalPatentSubDate.setTime(awardCloseOutBean.getFinalPatentSubmissionDate());
                        awardType.getCloseOutDeadlines().setFinalPatentSubDate(finalPatentSubDate);
                        if (prevAwardCloseOutBean != null ){
                            if (prevAwardCloseOutBean.getFinalPatentSubmissionDate() == null ||!(prevAwardCloseOutBean.getFinalPatentSubmissionDate().equals(awardCloseOutBean.getFinalPatentSubmissionDate()))) {
                                awardType.getCloseOutDeadlines().setFinalPatentSubDateModified(" *");
                            }
                        }else {
                            awardType.getCloseOutDeadlines().setFinalPatentSubDateModified(" *");
                        }
                    }else {
                        if (prevAwardCloseOutBean != null && prevAwardCloseOutBean.getFinalPatentSubmissionDate()!=null) {
                            awardType.getCloseOutDeadlines().setFinalPatentSubDateModified(" *");
                        }
                    }

                    if (awardCloseOutBean.getFinalPropSubmissionDate()!=null) {
                        Calendar finalPropSubDate = Calendar.getInstance();
                        finalPropSubDate.setTime(awardCloseOutBean.getFinalPropSubmissionDate());
                        awardType.getCloseOutDeadlines().setFinalPropSubDate(finalPropSubDate);
                        if (prevAwardCloseOutBean != null ){
                            if (prevAwardCloseOutBean.getFinalPropSubmissionDate() == null || !(prevAwardCloseOutBean.getFinalPropSubmissionDate().equals(awardCloseOutBean.getFinalPropSubmissionDate()))) {
                                awardType.getCloseOutDeadlines().setFinalPropSubDateModified(" *");
                            }
                        }else {
                            awardType.getCloseOutDeadlines().setFinalPropSubDateModified(" *");
                        }

                    }else {
                        if (prevAwardCloseOutBean != null && prevAwardCloseOutBean.getFinalPropSubmissionDate()!=null) {
                            awardType.getCloseOutDeadlines().setFinalPropSubDateModified(" *");
                        }
                    }

                    if (awardCloseOutBean.getFinalTechSubmissionDate()!=null) {
                        Calendar finalTechSubDate = Calendar.getInstance();
                        finalTechSubDate.setTime(awardCloseOutBean.getFinalTechSubmissionDate());
                        awardType.getCloseOutDeadlines().setFinalTechSubDate(finalTechSubDate);
                        if (prevAwardCloseOutBean != null ){
                            if (prevAwardCloseOutBean.getFinalTechSubmissionDate() == null || !(prevAwardCloseOutBean.getFinalTechSubmissionDate().equals(awardCloseOutBean.getFinalTechSubmissionDate()))) {
                                awardType.getCloseOutDeadlines().setFinalTechSubDateModified(" *");
                            }
                        }else {
                            awardType.getCloseOutDeadlines().setFinalTechSubDateModified(" *");
                        }
                    }else {
                        if (prevAwardCloseOutBean != null && prevAwardCloseOutBean.getFinalTechSubmissionDate()!=null) {
                            awardType.getCloseOutDeadlines().setFinalTechSubDateModified(" *");
                        }
                    }

                    awardType.getCloseOutDeadlines().setSequenceNumber(seqNumber);
                }
            }else {
                awardNotice.getPrintRequirement().setCloseoutRequired("0");
            }

            //start case 2010
            //for awardOtherData
            CoeusVector cvOtherData = awardDeltaReportTxnBean.getAwardCustomDataForSeq(mitAwardNumber, sequnceNumber);
            CoeusVector cvPrevcvOtherDatat = awardDeltaReportTxnBean.getAwardCustomDataForSeq(mitAwardNumber, ""+(seqNumber-1));

            AwardCustomDataBean awardCustomDataBean;
            AwardCustomDataBean prevaAardCustomDataBean = null;
            AwardType.AwardOtherDatasType.OtherDataType otherDataType;
            if (cvOtherData!=null && cvOtherData.size()>0) {
                awardCustomDataBean= (AwardCustomDataBean)cvOtherData.get(0);
              //if awardOtherData seqNum not equls current report sequnceNumber, nothing changed. no data need to print.
              if (awardCustomDataBean != null && awardCustomDataBean.getSequenceNumber() == seqNumber){
                AwardType.AwardOtherDatasType awardOtherDatasType = new AwardTypeImpl.AwardOtherDatasTypeImpl();
                awardType.setAwardOtherDatas(awardOtherDatasType);
                otherDataType = new AwardTypeImpl.AwardOtherDatasTypeImpl.OtherDataTypeImpl();
                CoeusVector cvFilteledPrev = null;
                for (int i = 0; i < cvOtherData.size(); i++ ){
                    awardCustomDataBean = (AwardCustomDataBean)cvOtherData.get(i);
                    if (awardCustomDataBean != null){
                        otherDataType = new AwardTypeImpl.AwardOtherDatasTypeImpl.OtherDataTypeImpl();
                        prevaAardCustomDataBean = null;
                        Equals eqPrevDataColumnLabel = new Equals("columnName",awardCustomDataBean.getColumnName());
                        if (cvPrevcvOtherDatat != null && cvPrevcvOtherDatat.size() > 0 ) {
                            cvFilteledPrev = cvPrevcvOtherDatat.filter(eqPrevDataColumnLabel);
                        }
                        if (cvFilteledPrev != null && cvFilteledPrev.size() > 0 ) {
                            prevaAardCustomDataBean = (AwardCustomDataBean)cvFilteledPrev.get(0);
                        }
                        otherDataType.setColumnName(UtilFactory.convertNull(awardCustomDataBean.getColumnLabel()));
                        if (prevaAardCustomDataBean != null){
                            if (awardCustomDataBean.getColumnValue() != null && awardCustomDataBean.getColumnValue().equals(prevaAardCustomDataBean.getColumnValue())){
                                if (awardCustomDataBean.getDescription()== null){
                                    otherDataType.setColumnValue(UtilFactory.convertNull(awardCustomDataBean.getColumnValue()));
                                }else if ( awardCustomDataBean.getDescription()!= null){ //for look up table
                                    otherDataType.setColumnValue(UtilFactory.convertNull(awardCustomDataBean.getDescription()));
                                }
                            }else {
                                if (awardCustomDataBean.getColumnValue() != null && awardCustomDataBean.getDescription()== null){
                                    otherDataType.setColumnValue("* " + UtilFactory.convertNull(awardCustomDataBean.getColumnValue()));
                                }else  if ( awardCustomDataBean.getDescription()!= null){ //for look up table
                                    otherDataType.setColumnValue("* " + UtilFactory.convertNull(awardCustomDataBean.getDescription()));
                                }else if (prevaAardCustomDataBean.getColumnValue() != null || prevaAardCustomDataBean.getDescription() != null){
                                    //current has no data, but prev has data
                                    otherDataType.setColumnValue("* " );
                                }
                            }
                        }else{// there is no prevData, new column is added in current data
                            if (awardCustomDataBean.getColumnValue() != null && awardCustomDataBean.getDescription()== null){
                                    otherDataType.setColumnValue("* " + UtilFactory.convertNull(awardCustomDataBean.getColumnValue()));
                                }else if ( awardCustomDataBean.getDescription()!= null){//for look up table
                                    otherDataType.setColumnValue("* " + UtilFactory.convertNull(awardCustomDataBean.getDescription()));
                                }
                        }
//                        if (otherDataType.getColumnValue() != null)
//                            awardType.getAwardOtherDatas().getOtherData().add(otherDataType);
                        awardType.getAwardOtherDatas().getOtherData().add(otherDataType);
                    }
                }
              }
            }

//            else{
//                awardNotice.getPrintRequirement().setOtherDataRequired("0");
//            }

            //end case 2010

//            JAXBContext jaxbContext = JAXBContext.newInstance("edu.mit.coeus.utils.xml.bean.award");
//            Marshaller marshaller = jaxbContext.createMarshaller();
//            ObjectFactory objFactory = new ObjectFactory();
//            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            marshaller.marshal(awardNotice, byteArrayOutputStream);
//            return byteArrayOutputStream;
            return awardNotice;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new CoeusException(ex.getMessage());
        }
    } // End of getDeltaReportStream()
    public double convDouble(double d, int scale){
        return (Math.round(d*Math.pow(10,scale)))/(Math.pow(10,scale));
    }

    // Added for Case 2355 - Award Notice Enhancement - Custom Elements are grouped by groups names - Start
    /**
     * This method gets distinct group names from the collection
     * containing all the custom elements as well as data associated with them previously
     * @param vecGroupData Vector  having all the Custom Element Group names
     * @return vecFinal Vector  returns only distinct group names
     */
    private Vector getDistinctGroups(Vector vecGroupData){

        //Take the first bean data from list, extract its group name,
        //if its null, set it to Miscellaneous and add the form to the
        //final vector, supposed to contain only distinct groups dyna forms.
        AwardCustomDataBean awardCustomDataBean;
        AwardCustomDataBean firstForm = (AwardCustomDataBean) vecGroupData.get(0);
        String firstGroup = (String) firstForm.getGroupCode();
        if(firstGroup == null){
            firstGroup = "Miscellaneous";
            firstForm.setGroupCode(firstGroup);
        }
        Vector vecFinal = new Vector();
        vecFinal.add(firstForm);

        //Iterate through the list and put only distinct
        //group name dyna froms in the final vector
        for(int i = 0; i < vecGroupData.size(); i++){
            awardCustomDataBean = (AwardCustomDataBean)vecGroupData.get(i);
            String outerGroup = (String) awardCustomDataBean.getGroupCode();
            if(outerGroup == null){
                outerGroup ="Miscellaneous";
                awardCustomDataBean.setGroupCode(outerGroup);
            }
            boolean toAdd = true;
            if(vecFinal != null && vecFinal.size() > 0){
                for(int j = 0; j < vecFinal.size(); j++){
                    AwardCustomDataBean customBean = (AwardCustomDataBean) vecFinal.get(j);
                    String innerGroup = (String) customBean.getGroupCode();
                    if(innerGroup.equals(outerGroup)){
                        toAdd = false;
                        break;
                    }
                }
                if(toAdd){
                    vecFinal.add(awardCustomDataBean);
                }
            }
        }
        return vecFinal;
    }
    // Added for Case 2355 - Award Notice Enhancement - Custom Elements are grouped by groups names - End

//Added for Case 3122 - Award Notice Enhancement -Start
     /**
     * This method gets AwardCloseOut data for the given Award Number
     * It calulates Due Date for 4 Report Class Code
     *
     * @return AwardCloseOutBean
     * @param mitAwardNumber String
     * @throws CoeusException
     * @throws DBException
     **/
    public Hashtable getAwardCloseOutDetails(String mitAwardNumber)
    throws CoeusException, DBException{
        Hashtable hshCloseOutData = new Hashtable();
        AwardTxnBean awardTxnBean = new AwardTxnBean();
        AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
        AwardReportTermsBean awardReportTermsBean = new AwardReportTermsBean();
        DateUtils dateUtils = new DateUtils();

        AwardCloseOutBean awardCloseOutBean = awardTxnBean.getAwardCloseOut(mitAwardNumber);
        if(awardCloseOutBean==null){
            awardCloseOutBean = new AwardCloseOutBean();
            awardCloseOutBean.setMitAwardNumber(mitAwardNumber);
        }

        DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
        Date dueDate = null;
        int mulipleEntries = 0;
        CoeusVector cvFilteredData = null;
        Equals eqReportClassCode = null;
        Equals eqFinalReport = null;
        And eqReportClassCodeAndFinalReport = null;

        //Get Report Terms
        CoeusVector awardReportTerms = awardTxnBean.getAwardReportTerms(mitAwardNumber);
        if(awardReportTerms==null){
            awardReportTerms = new CoeusVector();
        }
        //CoeusVector awardReportTerms = ObjectCloner.deepCopy(awardReportTerms);

        //Get All Frequency
        CoeusVector cvFrequency = awardLookUpDataTxnBean.getFrequency();
        CoeusVector cvFilteredFrequency = null;
        FrequencyBean frequencyBean = null;
        int frequencyDays;
        int frequencyMonths;

        //Get Money and End Dates Data
        AwardAmountInfoBean awardAmountInfoBean = new AwardAmountInfoBean();
        awardAmountInfoBean.setMitAwardNumber(mitAwardNumber);
        awardAmountInfoBean = awardTxnBean.getMoneyAndEndDates(awardAmountInfoBean);

        //Get Award Header Data
        AwardHeaderBean awardHeaderBean = awardTxnBean.getAwardHeader(mitAwardNumber);
        //Holds all class codes
        CoeusVector cvClassCodes = new CoeusVector();
        CoeusParameterBean coeusParameterBean = new CoeusParameterBean();

        //Set Invoice Due Date - Exception case
        String paramValue = departmentPersonTxnBean.getParameterValues(CoeusConstants.FISCAL_CLASS_CODE);
        if(paramValue!=null){
            coeusParameterBean.setParameterName(CoeusConstants.FISCAL_CLASS_CODE);
            coeusParameterBean.setParameterValue(paramValue);
            cvClassCodes.addElement(coeusParameterBean);

            eqReportClassCode = new Equals("reportClassCode", new Integer(paramValue));
            eqFinalReport = new Equals("finalReport", true);
            eqReportClassCodeAndFinalReport = new And(eqReportClassCode, eqFinalReport);
            cvFilteredData = awardReportTerms.filter(eqReportClassCodeAndFinalReport);
            if(cvFilteredData.size() > 0){
                awardReportTermsBean = (AwardReportTermsBean)cvFilteredData.elementAt(0);
                cvFilteredData = getDistinctRows(cvFilteredData);
                if(cvFilteredData.size() > 0){
                    calculateDueDate(cvFilteredData, cvFrequency, awardAmountInfoBean.getFinalExpirationDate());
                }
                cvFilteredData = removeDuplicateDueDateRows(cvFilteredData);

            }else if(cvFilteredData.size() == 0){
                awardCloseOutBean.setInvoiceDueDate("");
            }
            if(awardAmountInfoBean != null &&
            awardAmountInfoBean.getFinalExpirationDate()!=null){
                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.setTime(awardAmountInfoBean.getFinalExpirationDate());
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)+(awardHeaderBean.getFinalInvoiceDue()==null?0:awardHeaderBean.getFinalInvoiceDue().intValue()));
                dueDate = new java.sql.Date(calendar.getTime().getTime());

                if(cvFilteredData.size() == 0){
                    awardCloseOutBean.setInvoiceDueDate(dateUtils.formatDate(dueDate.toString(),"dd-MMM-yyyy"));
                }else{
                    cvFilteredData = cvFilteredData.filter(new Equals("dueDate", dueDate));
                    if(cvFilteredData.size() == 0){
                        awardCloseOutBean.setInvoiceDueDate("MULTIPLE");
                    }else{
                        awardCloseOutBean.setInvoiceDueDate(dateUtils.formatDate(dueDate.toString(),"dd-MMM-yyyy"));
                    }
                }
            }else{
                awardCloseOutBean.setInvoiceDueDate("");
            }
        }else{
            awardCloseOutBean.setInvoiceDueDate("");
        }

        //Set Technical date
        paramValue = departmentPersonTxnBean.getParameterValues(CoeusConstants.TECHNICAL_MANAGEMENT_CLASS_CODE);
        if(paramValue!=null){
            coeusParameterBean = new CoeusParameterBean();
            coeusParameterBean.setParameterName(CoeusConstants.TECHNICAL_MANAGEMENT_CLASS_CODE);
            coeusParameterBean.setParameterValue(paramValue);
            cvClassCodes.addElement(coeusParameterBean);

            eqReportClassCode = new Equals("reportClassCode", new Integer(paramValue));
            eqFinalReport = new Equals("finalReport", true);
            eqReportClassCodeAndFinalReport = new And(eqReportClassCode, eqFinalReport);
            cvFilteredData = awardReportTerms.filter(eqReportClassCodeAndFinalReport);
            if(cvFilteredData.size() > 0){
                awardReportTermsBean = (AwardReportTermsBean)cvFilteredData.elementAt(0);
                cvFilteredData = getDistinctRows(cvFilteredData);
                if(cvFilteredData.size() > 0){
                    calculateDueDate(cvFilteredData, cvFrequency, awardAmountInfoBean.getFinalExpirationDate());
                }
                cvFilteredData = removeDuplicateDueDateRows(cvFilteredData);
                if(cvFilteredData.size() > 1){
                    awardCloseOutBean.setTechnicalDueDate("MULTIPLE");
                }else if(cvFilteredData.size() == 1){
                    awardCloseOutBean.setTechnicalDueDate(dateUtils.formatDate(((AwardReportTermsBean)cvFilteredData.elementAt(0)).getDueDate().toString(),"dd-MMM-yyyy"));
                }else{
                    awardCloseOutBean.setTechnicalDueDate("");
                }
            }else if(cvFilteredData.size() == 0){
                awardCloseOutBean.setTechnicalDueDate("");
            }
        }else{
            awardCloseOutBean.setTechnicalDueDate(null);
        }

        //Set Patent date
        cvFilteredData = null;
        paramValue = departmentPersonTxnBean.getParameterValues(CoeusConstants.INTELLECTUAL_PROPERTY_CLASS_CODE);
        if(paramValue!=null){
            coeusParameterBean = new CoeusParameterBean();
            coeusParameterBean.setParameterName(CoeusConstants.INTELLECTUAL_PROPERTY_CLASS_CODE);
            coeusParameterBean.setParameterValue(paramValue);
            cvClassCodes.addElement(coeusParameterBean);

            eqReportClassCode = new Equals("reportClassCode", new Integer(paramValue));
            eqFinalReport = new Equals("finalReport", true);
            eqReportClassCodeAndFinalReport = new And(eqReportClassCode, eqFinalReport);
            cvFilteredData = awardReportTerms.filter(eqReportClassCodeAndFinalReport);
            if(cvFilteredData.size() > 0){
                awardReportTermsBean = (AwardReportTermsBean)cvFilteredData.elementAt(0);
                cvFilteredData = getDistinctRows(cvFilteredData);
                if(cvFilteredData.size() > 0){
                    calculateDueDate(cvFilteredData, cvFrequency, awardAmountInfoBean.getFinalExpirationDate());
                }
                cvFilteredData = removeDuplicateDueDateRows(cvFilteredData);
                if(cvFilteredData.size() > 1){
                    awardCloseOutBean.setPatentDueDate("MULTIPLE");
                }else if(cvFilteredData.size() == 1){
                    awardCloseOutBean.setPatentDueDate(dateUtils.formatDate(((AwardReportTermsBean)cvFilteredData.elementAt(0)).getDueDate().toString(),"dd-MMM-yyyy"));
                }else{
                    awardCloseOutBean.setPatentDueDate("");
                }
            }else if(cvFilteredData.size() == 1){
                awardCloseOutBean.setPatentDueDate("");
            }
        }else{
            awardCloseOutBean.setPatentDueDate("");
        }

        //Set Property due date
        paramValue = departmentPersonTxnBean.getParameterValues(CoeusConstants.PROPERTY_CLASS_CODE);
        if(paramValue!=null){
            coeusParameterBean = new CoeusParameterBean();
            coeusParameterBean.setParameterName(CoeusConstants.PROPERTY_CLASS_CODE);
            coeusParameterBean.setParameterValue(paramValue);
            cvClassCodes.addElement(coeusParameterBean);

            eqReportClassCode = new Equals("reportClassCode", new Integer(paramValue));
            eqFinalReport = new Equals("finalReport", true);
            eqReportClassCodeAndFinalReport = new And(eqReportClassCode, eqFinalReport);
            cvFilteredData = awardReportTerms.filter(eqReportClassCodeAndFinalReport);
            if(cvFilteredData.size() > 0){
                awardReportTermsBean = (AwardReportTermsBean)cvFilteredData.elementAt(0);
                cvFilteredData = getDistinctRows(cvFilteredData);
                if(cvFilteredData.size() > 0){
                    calculateDueDate(cvFilteredData, cvFrequency, awardAmountInfoBean.getFinalExpirationDate());
                }
                cvFilteredData = removeDuplicateDueDateRows(cvFilteredData);
                if(cvFilteredData.size() > 1){
                    awardCloseOutBean.setPropertyDueDate("MULTIPLE");
                }else if(cvFilteredData.size() == 1){
                    awardCloseOutBean.setPropertyDueDate(dateUtils.formatDate(((AwardReportTermsBean)cvFilteredData.elementAt(0)).getDueDate().toString(),"dd-MMM-yyyy"));
                }else{
                    awardCloseOutBean.setPropertyDueDate("");
                }
            }else if(cvFilteredData.size() == 0){
                awardCloseOutBean.setPropertyDueDate("");
            }
        }else{
            awardCloseOutBean.setPropertyDueDate("");
        }
        CoeusVector cvAwardCloseOut = new CoeusVector();
        cvAwardCloseOut.add(awardCloseOutBean);
        hshCloseOutData.put(AwardCloseOutBean.class, cvAwardCloseOut);
        hshCloseOutData.put(CoeusParameterBean.class, cvClassCodes);
        return hshCloseOutData;
    }

    //
    private void calculateDueDate(CoeusVector cvReportTerms, CoeusVector cvFrequency, java.sql.Date finalExpirationDate)
    throws CoeusException{
        try{
            AwardReportTermsBean awardReportTermsBean = null;
            CoeusVector cvFilteredFrequency = null;
            FrequencyBean frequencyBean = null;
            int frequencyDays = 0;
            int frequencyMonths = 0;
            java.sql.Date dueDate = null;
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(1900, 0, 1);
            java.sql.Date calendarDate = new java.sql.Date(calendar.getTime().getTime());

            java.sql.Date clonedDate = (java.sql.Date)ObjectCloner.deepCopy(finalExpirationDate);

            for(int row = 0; row < cvReportTerms.size(); row++){
                awardReportTermsBean = (AwardReportTermsBean)cvReportTerms.elementAt(row);

                if(awardReportTermsBean.getDueDate().equals(calendarDate)){
                    cvFilteredFrequency = cvFrequency.filter(new Equals("code", ""+awardReportTermsBean.getFrequencyCode()));
                    if(cvFilteredFrequency.size() > 0){
                        frequencyBean = (FrequencyBean)cvFilteredFrequency.elementAt(0);
                        frequencyDays = frequencyBean.getNumberOfDays();
                        frequencyMonths = frequencyBean.getNumberOfMonths();
                        calendar.setTime(finalExpirationDate);
                        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)+frequencyDays);
                        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)+frequencyMonths);
                        dueDate = new java.sql.Date(calendar.getTime().getTime());
                        awardReportTermsBean.setDueDate(dueDate);
                    }
                }
            }
            //Reset the date
            finalExpirationDate = clonedDate;
        }catch(Exception ex){
            throw new CoeusException(ex.getMessage());
        }
    }

    private CoeusVector getDistinctRows(CoeusVector cvReportTerms){
        CoeusVector cvDistict = new CoeusVector();
        Equals eqReportCode = null;
        Equals eqFrequencyCode = null;
        Equals eqDueDate = null;
        NotEquals notRowId = null;
        And reportCodeAndFrequencyCode = null;
        And reportCodeAndFrequencyCodeAndDueDate = null;
        And rowId = null;

        AwardReportTermsBean awardReportTermsBean = null;
        CoeusVector filteredData = null;
        CoeusVector distinctData = new CoeusVector();

        for(int row=0; row < cvReportTerms.size(); row++){
            awardReportTermsBean = (AwardReportTermsBean)cvReportTerms.elementAt(row);
            eqReportCode = new Equals("reportCode", new Integer(awardReportTermsBean.getReportCode()));
            eqFrequencyCode = new Equals("frequencyCode", new Integer(awardReportTermsBean.getFrequencyCode()));
            eqDueDate = new Equals("dueDate", awardReportTermsBean.getDueDate());
            notRowId = new NotEquals("rowId", new Integer(awardReportTermsBean.getRowId()));

            reportCodeAndFrequencyCode = new And(eqReportCode, eqFrequencyCode);
            reportCodeAndFrequencyCodeAndDueDate = new And(reportCodeAndFrequencyCode, eqDueDate);
            rowId = new And(reportCodeAndFrequencyCodeAndDueDate, notRowId);
            filteredData = cvReportTerms.filter(rowId);
            if(filteredData.size() > 0){
                cvReportTerms.remove(row);
                row--;
            }
        }
        return cvReportTerms;
    }

    private CoeusVector removeDuplicateDueDateRows(CoeusVector cvReportTerms){
        Equals eqDueDate = null;
        NotEquals notRowId = null;
        And rowId = null;

        AwardReportTermsBean awardReportTermsBean = null;
        CoeusVector filteredData = null;
        for(int row=0; row < cvReportTerms.size(); row++){
            awardReportTermsBean = (AwardReportTermsBean)cvReportTerms.elementAt(row);
            eqDueDate = new Equals("dueDate", awardReportTermsBean.getDueDate());
            notRowId = new NotEquals("rowId", new Integer(awardReportTermsBean.getRowId()));
            rowId = new And(eqDueDate, notRowId);
            filteredData = cvReportTerms.filter(rowId);
            if(filteredData.size() > 0){
                cvReportTerms.remove(row);
                row--;
            }
        }
        return cvReportTerms;
    }
    //Added for Case 3122 - Award Notice Enhancement -End
}//End AwardStream class





