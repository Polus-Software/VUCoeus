/*
 * @(#)InstituteProposalStream.java October 4, 2004, 10:12 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and
 * variables on 24-SEPT-2007 by Divya
 */


package edu.mit.coeus.xml.generator;

import edu.mit.coeus.bean.AddressBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.instprop.bean.*;
import edu.mit.coeus.irb.bean.ProtocolDataTxnBean;
import edu.mit.coeus.irb.bean.ProtocolInfoBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.award.bean.AwardReportTxnBean;
import edu.mit.coeus.bean.CoiDisclForItemBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.instprop.*;
import java.math.BigDecimal;
import java.util.*;
import javax.xml.bind.JAXBException;

/**
 * @author  Geo Thomas
 * @Created on October 4, 2004, 10:12 AM
 * Class for generating the object stream for Institute Propsal. It uses jaxb classes
 * which have been created under edu.mit.coeus.xml.instprop package. Fetch the data 
 * from database and attach with the jaxb beans which have been derived from 
 * institute proposal schema.
 */
public class InstituteProposalStream  extends ReportBaseStream{

    private InstituteProposalTxnBean instPropTxnBean;
    private ObjectFactory objFactory;
    private CoeusXMLGenrator xmlGenerator;
    private InstituteProposalBean instPropBean;  
    private String propNumber;
    private String loggedinUser;
    
    private static final String packageName = "edu.mit.coeus.xml.instprop";
    /** Creates a new instance of InstituteProposalStream */
    public InstituteProposalStream( String propNumber){
        this.propNumber = propNumber;
        objFactory = new ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();
        instPropTxnBean = new InstituteProposalTxnBean();
    }
    
    /** Creates a new instance of InstituteProposalStream */
    public InstituteProposalStream() {
        objFactory = new ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();
        instPropTxnBean = new InstituteProposalTxnBean();
    }
  
    public org.w3c.dom.Document getStream(java.util.Hashtable params) throws DBException,CoeusException {
        this.propNumber = (String)params.get("PROPOSAL_NUMBER");
        this.loggedinUser = (String)params.get("USER_ID");
        instPropBean = getInstPropData();       
        InstituteProposalType instProposalType = getInstProposal();
        return xmlGenerator.marshelObject(instProposalType,packageName);
    }
    
    public Object getObjectStream(Hashtable params) throws DBException,CoeusException{
        this.propNumber = (String)params.get("PROPOSAL_NUMBER");
        this.loggedinUser = (String)params.get("USER_ID");
        instPropBean = getInstPropData();
        InstituteProposalType instProposalType = getInstProposal();
        return instProposalType;
    }
    
    private InstituteProposalBean getInstPropData() throws DBException,CoeusException{
        if(propNumber==null) 
            throw new CoeusXMLException("Institute Proposal Number is Null");         
        return instPropTxnBean.getInstituteProposalDetails(propNumber);
    }
    private InstituteProposalType getInstProposal() throws CoeusXMLException,CoeusException,DBException{
        InstituteProposalType instProposal = null;
        try{
            instProposal = objFactory.createInstituteProposal();
            instProposal.getInvestigators().addAll(getInvestigatorTypes());
            //Added for Case 3823 - Key Person Records Needed in Inst Proposal and Award  -start
            instProposal.getKeyPersons().addAll(getKeyPersonTypes());
            //3823 - End
            //coeusdev-155 start
            instProposal.setOtherData(getCustData());
            instProposal.setOpportunityID(instPropBean.getOpportunity());
            instProposal.setCFDANum(instPropBean.getCfdaNumber());
            //coeusdev-155 end
            instProposal.setBudgetData(getBudgetDataType());
            instProposal.setInstProposalMaster(getInstProposalMasterData());
            instProposal.setMailingInfo(getMailingInfoType());
            instProposal.getIDCRates().addAll(getIDCRateTypes());
            instProposal.getCostSharingInfo().addAll(getCostSharingTypes());
            instProposal.getSpecialReviews().addAll(getSpecialReviewTypes());
            instProposal.getScienceCode().addAll(getScienceCodes());
            instProposal.setIDCRatesComments(getComment(CoeusConstants.INDIRECT_COST_COMMENT_CODE));
            instProposal.setCostSharingComments(getComment(CoeusConstants.COST_SHARING_COMMENT_CODE));
            instProposal.getDisclosureItem().addAll(getDisclosureItemTypes());
            instProposal.setSchoolInfo(getSchoolInfoType());
        }catch (JAXBException jaxbEx){
            UtilFactory.log(jaxbEx.getMessage(),jaxbEx,"InstituteProposalStream","getStream()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }
        return instProposal;
    }
    //coeusdev-155 start
    private OtherGroupType getCustData() throws JAXBException,DBException,CoeusException{
        OtherGroupType otherGroup = objFactory.createOtherGroupType();
        CoeusVector cvCustomData = instPropTxnBean.getCustomData(propNumber);
        boolean custDataFlag = false;
        if(cvCustomData != null && cvCustomData.size() > 0){
            for(int index = 0; index < cvCustomData.size(); index++){
                InstituteProposalCustomDataBean instProposalCustomDataBean = (InstituteProposalCustomDataBean)cvCustomData.get(index);
                OtherGroupDetailsType otherGroupDetail = objFactory.createOtherGroupDetailsType();
                otherGroupDetail.setColumnName(instProposalCustomDataBean.getColumnLabel());
                otherGroupDetail.setColumnValue(instProposalCustomDataBean.getColumnValue());
                otherGroupDetail.setColumnDesc(instProposalCustomDataBean.getDescription());
                otherGroup.getOtherGroupDetails().add(otherGroupDetail);
                if ((instProposalCustomDataBean.getColumnValue()!= null) || (instProposalCustomDataBean.getDescription()!= null)) {
                    custDataFlag = true;
                }
            }
        }
        if (! custDataFlag ) otherGroup = null;
        return otherGroup;
    }
    //coeusdev-155 end
    private Vector getScienceCodes() throws JAXBException,
                            CoeusException,DBException{
        Vector jxbScienceCodes = new Vector();
        Vector scienceCodes = instPropTxnBean.getInstituteProposalScienceCode(propNumber);
        int scCodeSize = scienceCodes==null?0:scienceCodes.size();
        for(int scIndex = 0;scIndex<scCodeSize; scIndex++){
            InstituteProposalScienceCodeBean scienceCodeBean = 
                (InstituteProposalScienceCodeBean)scienceCodes.elementAt(scIndex);
            ScienceCodeType jxbScienceCode = objFactory.createScienceCodeType();
            jxbScienceCode.setScienceCode(UtilFactory.convertNull(scienceCodeBean.getScienceCode()));
            jxbScienceCode.setScienceCodeDesc(UtilFactory.convertNull(scienceCodeBean.getScienceDescription()));
            jxbScienceCodes.addElement(jxbScienceCode);
        }        
        return jxbScienceCodes;
    }
    private Vector getInvestigatorTypes() throws JAXBException{
        Vector jxbInvestigators = new Vector();
        Vector investigators = instPropBean.getInvestigators();
        int invSize = investigators==null?0:investigators.size();
        for(int invIndex = 0;invIndex<invSize; invIndex++){
            InstituteProposalInvestigatorBean invBean = 
                (InstituteProposalInvestigatorBean)investigators.elementAt(invIndex);
            InvestigatorType investigator = objFactory.createInvestigatorType();
            investigator.setFacultyFlag(invBean.isFacultyFlag());
            PersonType person = objFactory.createPersonType();
            person.setFullName(UtilFactory.convertNull(invBean.getPersonName()));
            investigator.setPIName(person);
            investigator.setPrincipalInvFlag(invBean.isPrincipalInvestigatorFlag());
            investigator.getUnit().addAll(getUnit(invBean));
            jxbInvestigators.add(investigator);
        }
        return jxbInvestigators;
    }
  //Added for Case 3823 - Key Person Records Needed in Inst Proposal and Award  -start
    private Vector getKeyPersonTypes() throws JAXBException, DBException,CoeusException{
        Vector jxbKeyPersons = new Vector();
        int SeqNumber = instPropBean.getSequenceNumber();
        Vector keyPersons = instPropTxnBean.getInstituteProposalKeyPersons(propNumber, SeqNumber);
        int invSize = keyPersons==null?0:keyPersons.size();
        for(int invIndex = 0;invIndex<invSize; invIndex++){
            InstituteProposalKeyPersonBean instProposalKeyPersonBean =
                    (InstituteProposalKeyPersonBean)keyPersons.elementAt(invIndex);
            KeyPersonType keyPersonType = objFactory.createKeyPersonType();
            keyPersonType.setFaculty(instProposalKeyPersonBean.isFacultyFlag());
            BigDecimal bdecAmount = new BigDecimal(instProposalKeyPersonBean.getPercentageEffort());
            keyPersonType.setPercentEffort(bdecAmount.setScale(2,BigDecimal.ROUND_HALF_DOWN));
            keyPersonType.setPersonName(instProposalKeyPersonBean.getPersonName());
            keyPersonType.setPersonId(instProposalKeyPersonBean.getPersonId());
            keyPersonType.setRoleName(instProposalKeyPersonBean.getProjectRole());
            jxbKeyPersons.add(keyPersonType);
        }
        return jxbKeyPersons;
    }
//3823 - End
    
    private PersonType getPersonType() throws JAXBException,DBException,CoeusException{
     if (this.instPropBean.getRolodexId()!= 0){
        PersonType person = objFactory.createPersonType();
        CoeusFunctions coeusFunction = new CoeusFunctions();
        AddressBean address = coeusFunction.getRolodexAddress(""+this.instPropBean.getRolodexId());
        person.setFullName(UtilFactory.convertNull(instPropBean.getRolodexName()));
        StringBuffer strBfr = new StringBuffer();
        strBfr.append(UtilFactory.convertNull(address.getAddressLine_1()));
        if(!strBfr.toString().equals("") && address.getAddressLine_2()!=null){
            strBfr.append(",");
            strBfr.append(address.getAddressLine_2());
        }
        if(!strBfr.toString().equals("") && address.getAddressLine_3()!=null){
            strBfr.append(",");
            strBfr.append(address.getAddressLine_3());
        }
        person.setAddress(strBfr.toString());
        person.setCity(UtilFactory.convertNull(address.getCity()));
        person.setState(UtilFactory.convertNull(address.getStateCode()));
        person.setPhone(UtilFactory.convertNull(address.getPhoneNumber()));
        person.setZip(UtilFactory.convertNull(address.getPostalCode()));
        return person;
     }else{
        return null;
     }
    }
    
    private Vector getUnit(InstituteProposalInvestigatorBean invBean)  throws JAXBException{
        Vector units = new Vector();
        Vector invUnitBeans = invBean.getInvestigatorUnits();
        int size = invUnitBeans==null?0:invUnitBeans.size();
        for (int index=0;index<size;index++){
            InstituteProposalUnitBean invUnit = (InstituteProposalUnitBean)invUnitBeans.elementAt(index);
            UnitType unit = objFactory.createUnitType();
            unit.setLeadUnitFlag(invUnit.isLeadUnitFlag());
            unit.setUnitName(UtilFactory.convertNull(invUnit.getUnitName()));
            unit.setUnitNumber(UtilFactory.convertNull(invUnit.getUnitNumber()));
            units.add(unit);
        }
        return units;
    }
    
    private InstProposalMasterData getInstProposalMasterData() throws JAXBException,CoeusException,DBException{
        InstProposalMasterData instPropMasterData = objFactory.createInstProposalMasterData();
        
        instPropMasterData.setAccountNumber(UtilFactory.convertNull(instPropBean.getCurrentAccountNumber()));
        instPropMasterData.setAccountType(UtilFactory.convertNull(instPropBean.getTypeOfAccount()));
        ActivityType activityType = objFactory.createActivityType();
        activityType.setActivityTypeCode(instPropBean.getProposalActivityTypeCode());
        activityType.setActivityTypeDesc(UtilFactory.convertNull(instPropBean.getProposalActivityTypeDescription()));
        instPropMasterData.setActivityType(activityType);
        instPropMasterData.setAwardNumber(UtilFactory.convertNull(instPropBean.getCurrentAwardNumber()));
        instPropMasterData.setComments(getComment(CoeusConstants.PROPOSAL_SUMMARY_COMMENT_CODE));
        instPropMasterData.setGradStudentCount(instPropBean.getGradStudHeadCount());
        instPropMasterData.setGradStudentmonths(instPropBean.getGradStudPersonMonths());
        instPropMasterData.setHasSubcontracts(instPropBean.isSubcontractFlag());
        instPropMasterData.setSequenceNumber(instPropBean.getSequenceNumber());
        if(instPropBean.getNsfCode()!=null){
            NSFcodeType nsfCodeType = objFactory.createNSFcodeType();
            nsfCodeType.setNSFcode(UtilFactory.convertNull(instPropBean.getNsfCode()));
            nsfCodeType.setNSFcodeDesc(UtilFactory.convertNull(instPropBean.getNsfCodeDescription()));
            instPropMasterData.setNSFcode(nsfCodeType);
        }
        
        if(instPropBean.getNoticeOfOpportunityCode()!=0){
            NoticeOfOppType noticeOfOpp = objFactory.createNoticeOfOppType();
            noticeOfOpp.setNoticeOfOppcode(""+instPropBean.getNoticeOfOpportunityCode());
            noticeOfOpp.setNoticeOfOppDesc(UtilFactory.convertNull(instPropBean.getNoticeOfOpportunityDescription()));
            instPropMasterData.setNoticeOfOpportunity(noticeOfOpp);
        }
        

        if(instPropBean.getPrimeSponsorCode()!=null){
            SponsorType primeSponsor = objFactory.createSponsorType();
            primeSponsor.setSponsorCode(UtilFactory.convertNull(instPropBean.getPrimeSponsorCode()));
            primeSponsor.setSponsorName(UtilFactory.convertNull(instPropBean.getPrimeSponsorName()));
            instPropMasterData.setPrimeSponsor(primeSponsor);
        }

        instPropMasterData.setProposalNumber(UtilFactory.convertNull(instPropBean.getProposalNumber()));
        
        if(instPropBean.getStatusCode()!=0){
            ProposalStatusType proposalStatus = objFactory.createProposalStatusType();
            proposalStatus.setStatusCode(instPropBean.getStatusCode());
            proposalStatus.setStatusDesc(UtilFactory.convertNull(instPropBean.getStatusDescription()));
            instPropMasterData.setProposalStatus(proposalStatus);
        }
        if(instPropBean.getProposalTypeCode()!=0){
            ProposalType proposalType = objFactory.createProposalType();
            proposalType.setProposalTypeCode(instPropBean.getProposalTypeCode());
            proposalType.setProposalTypeDesc(UtilFactory.convertNull(instPropBean.getProposalTypeDescription()));
            instPropMasterData.setProposalType(proposalType);
        }
        //Added for Case 2162 -Start        
        if(instPropBean.getAwardTypeCode() !=0){
            AnticipatedAwardType anticipatedAwardType = objFactory.createAnticipatedAwardType();
            anticipatedAwardType.setAnticipatedAwardTypeCode(instPropBean.getAwardTypeCode());
            anticipatedAwardType.setAnticipatedAwardTypeDesc(instPropBean.getAwardTypeDesc());
            instPropMasterData.setAnticipatedAwardType(anticipatedAwardType);            
        }              
        // Added for Case 2162 -End
        if(instPropBean.getSponsorCode()!=null){
            SponsorType sponsor = objFactory.createSponsorType();
            sponsor.setSponsorCode(UtilFactory.convertNull(instPropBean.getSponsorCode()));
            sponsor.setSponsorName(UtilFactory.convertNull(instPropBean.getSponsorName()));
            instPropMasterData.setSponsor(sponsor);
        }
        
        instPropMasterData.setSponsorProposalNumber(UtilFactory.convertNull(instPropBean.getSponsorProposalNumber()));
        instPropMasterData.setTitle(UtilFactory.convertNull(instPropBean.getTitle()));
         // Added for COEUSQA-1471_show institute proposal for merged proposal logs - Start
        String mergedWith = "";
        StringBuffer mergedWithProposal = new StringBuffer();
        CoeusVector cvMergedData = instPropTxnBean.getMergedDataForProposal(instPropBean.getProposalNumber());
        if(cvMergedData != null && !cvMergedData.isEmpty()){
            for(int index=0;index<cvMergedData.size();index++){
                TempProposalMergeLogBean proposalMergedlogBean = (TempProposalMergeLogBean)cvMergedData.get(index);
                mergedWithProposal.append(proposalMergedlogBean.getTempProposalNumber());
                if(index < cvMergedData.size()-1){ // For the last proposal ',' wont be appended
                    mergedWithProposal.append(", ");
                }
                
            }
            mergedWith = mergedWithProposal.toString();
        }
        instPropMasterData.setMergedWith(mergedWith);
        // Added for COEUSQA-1471_show institute proposal for merged proposal logs - End
        return instPropMasterData;
    }
    
    private String getComment(String commentCode) throws JAXBException,CoeusException,DBException{
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        String paramCodeValue = coeusFunctions.getParameterValue(commentCode);
        Vector comments = instPropTxnBean.getInstituteProposalComments(propNumber,Integer.parseInt(paramCodeValue));
        if(comments==null || comments.isEmpty()){
            return "";
        }
        InstituteProposalCommentsBean commentBean = (InstituteProposalCommentsBean)comments.elementAt(0);
        return commentBean.getComments();
    }
//    private static final char OSP='O';
    private MailingInfoType getMailingInfoType() throws JAXBException,
                                        CoeusException,DBException{
        MailingInfoType mailingInfo = objFactory.createMailingInfoType();
        mailingInfo.setComments(getComment(CoeusConstants.PROPOSAL_COMMENT_CODE));
        // Modified because of  Method getCal() is commented.
//        mailingInfo.setDeadlineDate(getCal(instPropBean.getDeadLineDate()));
        mailingInfo.setDeadlineDate(DateUtils.getCalendar(instPropBean.getDeadLineDate()));
        
        mailingInfo.setDeadlineType(""+instPropBean.getDeadLineType());
        mailingInfo.setMailByOSP(""+instPropBean.getMailBy());
        String str = instPropBean.getNumberOfCopies();
        int numCopy = Integer.parseInt(str==null?"0":str);
        if (numCopy != 0){
            mailingInfo.setNumberCopies(numCopy);
        }
//        mailingInfo.setNumberCopies(Integer.parseInt(str==null?"0":str));
        mailingInfo.setMailToPerson(getPersonType());
        //case 3918 : Mail Types in Mailing Information - Start
        mailingInfo.setMailType(""+instPropBean.getMailType());
        mailingInfo.setMailAccount(instPropBean.getMailAccountNumber());
        //3918 End
        return mailingInfo;
    }
    private Vector getIDCRateTypes() throws JAXBException,
                                        CoeusException,DBException{
        Vector jxbIDCRates = new Vector();
        Vector idcRates = instPropTxnBean.getInstituteProposalIDCRate(propNumber);
        int idcSize = idcRates==null?0:idcRates.size();
        InstituteProposalIDCRateBean instituteProposalIDCRateBean = null;
        for(int idcIndex = 0;idcIndex<idcSize; idcIndex++){
            instituteProposalIDCRateBean = (InstituteProposalIDCRateBean)idcRates.elementAt(idcIndex);
            jxbIDCRates.add(getIDCRateType(instituteProposalIDCRateBean));
        }
        return jxbIDCRates;
    }
    private IDCRateType getIDCRateType(InstituteProposalIDCRateBean idcRateBean) throws JAXBException,
                                        CoeusException,DBException{
        IDCRateType idcRateType = objFactory.createIDCRateType();
        idcRateType.setFY(UtilFactory.convertNull(idcRateBean.getFiscalYear()));
        idcRateType.setOnCampus(idcRateBean.isOnOffCampusFlag());
        idcRateType.setRate(idcRateBean.getApplicableIDCRate());
        idcRateType.setRateType(UtilFactory.convertNull(idcRateBean.getIdcRateTypeDesc()));
        idcRateType.setSourceAccount(UtilFactory.convertNull(idcRateBean.getSourceAccount()));
        idcRateType.setUnderRecovery(idcRateBean.getUnderRecoveryIDC());
        return idcRateType;
    }
    private BudgetDataType getBudgetDataType() throws JAXBException,
                                        CoeusException,DBException{
        BudgetDataType budgetData = objFactory.createBudgetDataType();
        budgetData.setAccountType(instPropBean.getTypeOfAccount());
        // Modified because of  Method getCal() is commented. 
//        budgetData.setRequestedStartDateInitial(getCal(instPropBean.getRequestStartDateInitial()));
//        budgetData.setRequestedEndDateInitial(getCal(instPropBean.getRequestEndDateInitial()));
//        budgetData.setRequestedStartDateTotal(getCal(instPropBean.getRequestStartDateTotal()));
//        budgetData.setRequestedEndDateTotal(getCal(instPropBean.getRequestEndDateTotal()));
        budgetData.setRequestedStartDateInitial(DateUtils.getCalendar(instPropBean.getRequestStartDateInitial()));
        budgetData.setRequestedEndDateInitial(DateUtils.getCalendar(instPropBean.getRequestEndDateInitial()));
        budgetData.setRequestedStartDateTotal(DateUtils.getCalendar(instPropBean.getRequestStartDateTotal()));
        budgetData.setRequestedEndDateTotal(DateUtils.getCalendar(instPropBean.getRequestEndDateTotal()));
        
        budgetData.setTotalDirectCostInitial(convDoubleToBigDec(instPropBean.getTotalDirectCostInitial()));
        budgetData.setTotalDirectCostTotal(convDoubleToBigDec(instPropBean.getTotalDirectCostTotal()));
        budgetData.setTotalIndirectCostInitial(convDoubleToBigDec(instPropBean.getTotalInDirectCostInitial()));
        budgetData.setTotalIndirectCostTotal(convDoubleToBigDec(instPropBean.getTotalInDirectCostTotal()));
        double totalCostInitial = instPropBean.getTotalDirectCostInitial()+instPropBean.getTotalInDirectCostInitial();
        double totalCostTotal = instPropBean.getTotalDirectCostTotal()+instPropBean.getTotalInDirectCostTotal();
        budgetData.setTotalCostInitial(convDoubleToBigDec(totalCostInitial));
        budgetData.setTotalCostTotal(convDoubleToBigDec(totalCostTotal));
        return budgetData;
    }
    private static BigDecimal convDoubleToBigDec(double d){
        return new BigDecimal(d);
    }
    
    // This Method moved to DateUtils Class
    // New Method name- getCalendar()
    // Modifies for Case#3110:Special review in prop dev linked to protocols
    
//    private Calendar getCal(Date date){
//        if(date==null)
//            return null;
//        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
//        cal.setTime(date);
//        return cal;
//    }
    
    private Vector getSpecialReviewTypes() throws JAXBException,
                                        CoeusException,DBException{
        Vector jxbSplReviews = new Vector();
        Vector splReviews = instPropTxnBean.getInstituteProposalSpecialReview(propNumber);
        int splRSize = splReviews==null?0:splReviews.size();
        InstituteProposalSpecialReviewBean instPropSplRBean = null;
        for(int splRIndex = 0;splRIndex<splRSize; splRIndex++){
            instPropSplRBean = (InstituteProposalSpecialReviewBean)splReviews.elementAt(splRIndex);
            jxbSplReviews.add(getSplReviewType(instPropSplRBean));
        }
        return jxbSplReviews;
    }
    private SpecialReviewType getSplReviewType(InstituteProposalSpecialReviewBean 
                            instPropSpReBean) throws JAXBException,
                                        CoeusException,DBException{
        SpecialReviewType splRevType = objFactory.createSpecialReviewType();
        //case 4525 start
        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
       
        int linkedToIRBCode = Integer.parseInt(coeusFunctions.getParameterValue("LINKED_TO_IRB_CODE"));
        String linkage = coeusFunctions.getParameterValue("ENABLE_PROTOCOL_TO_PROPOSAL_LINK");
                    
        if(linkage != null){
            if ( linkage.equals("1")){//linkage is on
                if(instPropSpReBean.getApprovalCode() == linkedToIRBCode){
                    String protocolNumber = instPropSpReBean.getProtocolSPRevNumber();
                    if(protocolNumber != null && !"".equals(protocolNumber)){
                        if(protocolNumber.charAt(0) == 'X'){
                            protocolNumber = protocolNumber.substring(1);
                        }
                        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
                        ProtocolInfoBean protocolInfoBean = protocolDataTxnBean.getProtocolInfo(protocolNumber);
                        if(protocolInfoBean != null){
                            splRevType.setApplicationDate(DateUtils.getCalendar(protocolInfoBean.getApplicationDate()));
                            splRevType.setApprovalDate(DateUtils.getCalendar(protocolInfoBean.getApprovalDate()));
                            splRevType.setSpecialReviewStatus(UtilFactory.convertNull(protocolInfoBean.getProtocolStatusDesc()));
                        }
                    }
                }else {
                    splRevType.setApplicationDate(DateUtils.getCalendar(instPropSpReBean.getApplicationDate()));
                    splRevType.setApprovalDate(DateUtils.getCalendar(instPropSpReBean.getApprovalDate()));
                    splRevType.setSpecialReviewStatus(UtilFactory.convertNull(instPropSpReBean.getApprovalDescription()));
                }
            }else{//linkage is off
                splRevType.setApplicationDate(DateUtils.getCalendar(instPropSpReBean.getApplicationDate()));
                splRevType.setApprovalDate(DateUtils.getCalendar(instPropSpReBean.getApprovalDate()));
                splRevType.setSpecialReviewStatus(UtilFactory.convertNull(instPropSpReBean.getApprovalDescription()));
            }
        splRevType.setComments(UtilFactory.convertNull(instPropSpReBean.getComments()));
        splRevType.setProtocolNumber(UtilFactory.convertNull(instPropSpReBean.getProtocolSPRevNumber()));
        splRevType.setSpecialReviewType(UtilFactory.convertNull(instPropSpReBean.getSpecialReviewDescription()));
        }
        //need to check the linkage of proposal and protocol is on or off       
//        String linkage = coeusFunctions.getParameterValue("ENABLE_PROTOCOL_TO_PROPOSAL_LINK");
//        if (linkage != null && linkage.equals("1")){
        // when the linkage is on
        //case 4525 comment out
//        // Case# 3110:Special review in prop dev linked to protocols - Start
//        String protocolNumber = instPropSpReBean.getProtocolSPRevNumber();
//        // 4469: Print Proposal Notice fails when special review present but no protocol number 
//        if(protocolNumber != null && !"".equals(protocolNumber)){
//            if(protocolNumber.charAt(0) == 'X'){
//                protocolNumber = protocolNumber.substring(1);
//            }
//            ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
////        ProtocolInfoBean protocolInfoBean = protocolDataTxnBean.getProtocolInfo(instPropSpReBean.getProtocolSPRevNumber());
//            ProtocolInfoBean protocolInfoBean = protocolDataTxnBean.getProtocolInfo(protocolNumber);
////        splRevType.setApplicationDate(getCal(instPropSpReBean.getApplicationDate()));
////        splRevType.setApprovalDate(getCal(instPropSpReBean.getApprovalDate()));
//            if(protocolInfoBean != null){
//                splRevType.setApplicationDate(DateUtils.getCalendar(protocolInfoBean.getApplicationDate()));
//                splRevType.setApprovalDate(DateUtils.getCalendar(protocolInfoBean.getApprovalDate()));
//                if(instPropSpReBean.getApprovalCode() == 5){
//                    splRevType.setSpecialReviewStatus(UtilFactory.convertNull(protocolInfoBean.getProtocolStatusDesc()));
//                } else {
//                    splRevType.setSpecialReviewStatus(UtilFactory.convertNull(instPropSpReBean.getApprovalDescription()));
//                }
//            } else{
//                splRevType.setApplicationDate(DateUtils.getCalendar(instPropSpReBean.getApplicationDate()));
//                splRevType.setApprovalDate(DateUtils.getCalendar(instPropSpReBean.getApprovalDate()));
//                splRevType.setSpecialReviewStatus(UtilFactory.convertNull(instPropSpReBean.getApprovalDescription()));
//            }
//        // 4469: Print Proposal Notice fails when special review present but no protocol number  - Start    
//        }else{
//            splRevType.setApplicationDate(DateUtils.getCalendar(instPropSpReBean.getApplicationDate()));
//            splRevType.setApprovalDate(DateUtils.getCalendar(instPropSpReBean.getApprovalDate()));
//            splRevType.setSpecialReviewStatus(UtilFactory.convertNull(instPropSpReBean.getApprovalDescription()));
//        }
//        // 4469: Print Proposal Notice fails when special review present but no protocol number  - End
//        // Case# 3110:Special review in prop dev linked to protocols - End
//        //case 3111 start
//        splRevType.setComments(UtilFactory.convertNull(instPropSpReBean.getComments()));
////        splRevType.setComments(getComment(CoeusConstants.SPECIAL_RATE_COMMENT_CODE));
//        //case 3111 end
//        splRevType.setProtocolNumber(UtilFactory.convertNull(instPropSpReBean.getProtocolSPRevNumber()));
//        splRevType.setSpecialReviewType(UtilFactory.convertNull(instPropSpReBean.getSpecialReviewDescription()));
//        // Case# 3110:Special review in prop dev linked to protocols - Start
//        // Get the Status of the Protocol and set it as the status of the Special Review.
////       splRevType.setSpecialReviewStatus(UtilFactory.convertNull(instPropSpReBean.getApprovalDescription()));
//        // Case# 3110:Special review in prop dev linked to protocols - End
//case 4525 end    
        return splRevType;
    }
    private Vector getCostSharingTypes() throws JAXBException,
                                        CoeusException,DBException{
        Vector jxbCostSharings = new Vector();
        Vector costSharings = instPropTxnBean.getInstituteProposalCostSharing(propNumber);
        int costShSize = costSharings==null?0:costSharings.size();
        InstituteProposalCostSharingBean instPropCostShBean = null;
        for(int costShIndex = 0;costShIndex<costShSize; costShIndex++){
            instPropCostShBean = (InstituteProposalCostSharingBean)costSharings.elementAt(costShIndex);
            jxbCostSharings.add(getCostSharingType(instPropCostShBean));
        }
        return jxbCostSharings;
    }
    private CostSharingType getCostSharingType(InstituteProposalCostSharingBean 
                    instPropCostShBean) throws JAXBException{
        CostSharingType jxbCostSharing = objFactory.createCostSharingType();
        jxbCostSharing.setAmount(instPropCostShBean.getAmount());
        jxbCostSharing.setCostSharingType(UtilFactory.convertNull(instPropCostShBean.getCostSharingTypeDesc()));
        jxbCostSharing.setFY(UtilFactory.convertNull(instPropCostShBean.getFiscalYear()));
        jxbCostSharing.setPercentage(instPropCostShBean.getCostSharingPercentage());
        jxbCostSharing.setSourceAccount(UtilFactory.convertNull(instPropCostShBean.getSourceAccount()));
        return jxbCostSharing;
    }
    
     private Vector getDisclosureItemTypes() throws JAXBException,
                                        CoeusException,DBException{
      try{
        Vector jxbDisclosures = new Vector();
        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
        boolean hasAnyOSPRight = userMaintDataTxnBean.getUserHasAnyOSPRight(loggedinUser);
        if (hasAnyOSPRight) {  
            AwardReportTxnBean awardReportTxnBean = new AwardReportTxnBean();
            if (awardReportTxnBean.getIsAllDiscStatusComplete(propNumber, 2) != 1 ){
                 CoeusVector cvCoiDisclForItem = awardReportTxnBean.getCoiDisclForItem(propNumber,2);
                 if (cvCoiDisclForItem!=null && cvCoiDisclForItem.size()>0) {
                     CoiDisclForItemBean coiDisclForItemBean;
                     
                     for (int index = 0; index < cvCoiDisclForItem.size(); index++) {
                        coiDisclForItemBean = (CoiDisclForItemBean)cvCoiDisclForItem.get(index); 
                        DisclosureItemType disclosureItemType = objFactory.createDisclosureItemType();
                        disclosureItemType.setDisclosureNumber(coiDisclForItemBean.getCoiDisclosureNumber());
                        disclosureItemType.setDisclosureStatusCode(coiDisclForItemBean.getCoiDisclosureStatusCode());
                        disclosureItemType.setDisclosureStatusDesc(coiDisclForItemBean.getCoiDisclosureStatusCode());
                        disclosureItemType.setDisclosureTypeCode(coiDisclForItemBean.getDisclosureType());
                        disclosureItemType.setDisclosureTypeDesc(coiDisclForItemBean.getDisclosureType());
                        disclosureItemType.setPersonId(coiDisclForItemBean.getPersonId());
                        disclosureItemType.setPersonName(coiDisclForItemBean.getPersonId());
                        jxbDisclosures.add(disclosureItemType);
                     }
                 }

            }
        }
        return jxbDisclosures;
      }catch (Exception ex) {
          // commented for using UtilFactory.log instead of printStackTrace
            UtilFactory.log(ex.getMessage(),ex,"InstituteProposalStream", "getDisclosureItemTypes");
//         ex.printStackTrace();
         throw new CoeusException(ex.getMessage());
            
      }
    }
   
     private SchoolInfoType getSchoolInfoType() throws JAXBException,
                                        CoeusException,DBException{
        try{
        SchoolInfoType schoolInfoType = objFactory.createSchoolInfoType();
//        InputStream is = getClass().getResourceAsStream("/coeus.properties");
//        Properties coeusProps = new Properties();
//        
//        coeusProps.load(is);
        String schoolName = CoeusProperties.getProperty(CoeusPropertyKeys.SCHOOL_NAME);
        String schoolAcronym = CoeusProperties.getProperty(CoeusPropertyKeys.SCHOOL_ACRONYM);
        // SchoolInfoType
        schoolInfoType.setSchoolName(schoolName);
        schoolInfoType.setAcronym(schoolAcronym);
        return schoolInfoType;
        }catch (Exception ex) {
            UtilFactory.log(ex.getMessage(),ex,"InstituteProposalStream","getSchoolInfoType()");
            throw new CoeusException(ex.getMessage());
        }
    }
   
                
}
