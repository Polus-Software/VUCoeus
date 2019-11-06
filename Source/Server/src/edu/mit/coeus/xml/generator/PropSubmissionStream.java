/*
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 *
 * PropSubmissionStream.java
 *
 * Created on June 15, 2006, 6:14 PM
 */

package edu.mit.coeus.xml.generator;

import edu.mit.coeus.award.bean.AwardBean;
import edu.mit.coeus.award.bean.AwardTxnBean;
import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
import edu.mit.coeus.budget.bean.BudgetDetailBean;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.bean.BudgetModularBean;
import edu.mit.coeus.budget.bean.BudgetModularIDCBean;
import edu.mit.coeus.budget.bean.BudgetPeriodBean;
import edu.mit.coeus.budget.bean.BudgetSubAwardTxnBean;
import edu.mit.coeus.budget.bean.BudgetSummaryReportBean;
import edu.mit.coeus.budget.bean.BudgetUpdateTxnBean;
import edu.mit.coeus.budget.bean.RateClassBean;
import edu.mit.coeus.exception.CoeusException;
//import edu.mit.coeus.irb.bean.ProtocolDataTxnBean;
import edu.mit.coeus.irb.bean.ProtocolInfoBean;
import edu.mit.coeus.propdev.bean.AbstractTypeFormBean;
import edu.mit.coeus.propdev.bean.InvCreditTypeBean;
import edu.mit.coeus.propdev.bean.InvestigatorCreditSplitBean;
import edu.mit.coeus.propdev.bean.MedusaTxnBean;
import edu.mit.coeus.propdev.bean.ProposalAbstractFormBean;
import edu.mit.coeus.propdev.bean.ProposalActionTxnBean;
import edu.mit.coeus.propdev.bean.ProposalBiographyFormBean;
import edu.mit.coeus.propdev.bean.ProposalCertificationFormBean;
import edu.mit.coeus.propdev.bean.ProposalCongDistrictBean;
import edu.mit.coeus.propdev.bean.ProposalCustomElementsInfoBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalInvestigatorFormBean;
import edu.mit.coeus.propdev.bean.ProposalKeyPersonFormBean;
import edu.mit.coeus.propdev.bean.ProposalLeadUnitFormBean;
import edu.mit.coeus.propdev.bean.ProposalPerDegreeFormBean;
import edu.mit.coeus.propdev.bean.ProposalPersonFormBean;
import edu.mit.coeus.propdev.bean.ProposalPersonTxnBean;
import edu.mit.coeus.propdev.bean.ProposalSiteBean;
import edu.mit.coeus.propdev.bean.ProposalYNQBean;
import edu.mit.coeus.questionnaire.bean.QuestionAnswerBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireBaseBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireBean;
import edu.mit.coeus.rates.bean.RateTypeBean;
import edu.mit.coeus.rates.bean.RatesTxnBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.routing.bean.RoutingBean;
import edu.mit.coeus.routing.bean.RoutingCommentsBean;
import edu.mit.coeus.routing.bean.RoutingDetailsBean;
import edu.mit.coeus.routing.bean.RoutingMapBean;
import edu.mit.coeus.routing.bean.RoutingTxnBean;
import edu.mit.coeus.s2s.util.Converter;
import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceDataTxnBean;
import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceFormBean;
import edu.mit.coeus.unit.bean.UnitAdministratorBean;
import edu.mit.coeus.unit.bean.UnitDataTxnBean;
import edu.mit.coeus.unit.bean.UnitDetailFormBean;
import edu.mit.coeus.unit.bean.UnitHierarchyFormBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.question.bean.QuestionListBean;
import edu.mit.coeus.utils.xml.v2.approvalrouting.LevelDetailsType;
import edu.mit.coeus.utils.xml.v2.approvalrouting.RoutingCommentsDetails;
import edu.mit.coeus.utils.xml.v2.approvalrouting.RoutingDetailsType;
import edu.mit.coeus.utils.xml.v2.approvalrouting.RoutingMapsType;
import edu.mit.coeus.utils.xml.v2.approvalrouting.RoutingType;
import edu.mit.coeus.utils.xml.v2.budget.BUDGETDETAILType;
import edu.mit.coeus.utils.xml.v2.budget.BUDGETPERIODType;
import edu.mit.coeus.utils.xml.v2.budget.BUDGETType;
import edu.mit.coeus.utils.xml.v2.lookuptypes.ABSTRACTTYPEType;
import edu.mit.coeus.utils.xml.v2.lookuptypes.ACTIVITYTYPEType;
import edu.mit.coeus.utils.xml.v2.lookuptypes.ANTICIPATEDAWARDTYPEType;
import edu.mit.coeus.utils.xml.v2.lookuptypes.APPLICABLEREVIEWTYPEType;
import edu.mit.coeus.utils.xml.v2.lookuptypes.INVCREDITTYPEType;
import edu.mit.coeus.utils.xml.v2.lookuptypes.LOCATIONType;
import edu.mit.coeus.utils.xml.v2.lookuptypes.NOTICEOFOPPORTUNITYType;
import edu.mit.coeus.utils.xml.v2.lookuptypes.NSFSCIENCECODEType;
import edu.mit.coeus.utils.xml.v2.lookuptypes.PROPOSALSTATUSType;
import edu.mit.coeus.utils.xml.v2.lookuptypes.PROPOSALTYPEType;
import edu.mit.coeus.utils.xml.v2.lookuptypes.RATETYPEType;
import edu.mit.coeus.utils.xml.v2.lookuptypes.SPECIALREVIEWType;
import edu.mit.coeus.utils.xml.v2.modularbudget.CUMULATIVEMODULARBUDGETType;
import edu.mit.coeus.utils.xml.v2.modularbudget.INDIRECTCOSTType;
import edu.mit.coeus.utils.xml.v2.propdev.CREDITSPLITCOLUMNSType;
import edu.mit.coeus.utils.xml.v2.propdev.PROPINVESTIGATORS;
import edu.mit.coeus.utils.xml.v2.propdev.PROPINVESTIGATORSBASICDETAILS;
import edu.mit.coeus.utils.xml.v2.propdev.PROPUNITCREDITSPLIT;
import edu.mit.coeus.utils.xml.v2.propdev.PropSiteCongDistricts;
import edu.mit.coeus.utils.xml.v2.propdev.ProposalSites;
import edu.mit.coeus.utils.xml.v2.propdev.SchoolInfoType;
import edu.mit.coeus.utils.xml.v2.propdev.impl.PropSiteCongDistrictsImpl;
import edu.mit.coeus.utils.xml.v2.questionnaire.AnswerHeaderType;
import edu.mit.coeus.utils.xml.v2.questionnaire.AnswerType;
import edu.mit.coeus.utils.xml.v2.questionnaire.QuestionType;
import edu.mit.coeus.utils.xml.v2.questionnaire.QuestionnaireType;
import edu.mit.coeus.utils.xml.v2.rateandbase.RATEANDBASEType;
import edu.mit.coeus.utils.xml.v2.user_unit.UNITADMINISTRATORSType;
import edu.mit.coeus.utils.xml.v2.modularbudget.MODULARBUDGETType;
import edu.mit.coeus.utils.xml.v2.organization.ORGANIZATIONType;
import edu.mit.coeus.utils.xml.v2.propdev.PROPINVESTIGATORS;
import edu.mit.coeus.utils.xml.v2.propdev.PROPOSAL;
import edu.mit.coeus.utils.xml.v2.propdev.PROPOSALMASTERType;
import edu.mit.coeus.utils.xml.v2.propdev.PROPPERCREDITSPLITType;
import edu.mit.coeus.utils.xml.v2.propdev.PROPPERCUSTOMDATA;
import edu.mit.coeus.utils.xml.v2.propdev.PROPPERSONBIO;
import edu.mit.coeus.utils.xml.v2.propdev.PROPPERSONDEGREE;
import edu.mit.coeus.utils.xml.v2.propdev.PROPPERSYNQ;
import edu.mit.coeus.utils.xml.v2.propdev.PROPSPECIALREVIEWType;
import edu.mit.coeus.utils.xml.v2.propdev.PROPUNITCREDITSPLITType;
import edu.mit.coeus.utils.xml.v2.propdev.PROPUNITSType;
import edu.mit.coeus.utils.xml.v2.propdev.PROPYNQType;
import edu.mit.coeus.utils.xml.v2.rolodex.ADDRESSType;
import edu.mit.coeus.utils.xml.v2.rolodex.NAMEType;
import edu.mit.coeus.utils.xml.v2.rolodex.ROLODEXType;
import edu.mit.coeus.utils.xml.v2.sponsor.SPONSORType;
import edu.mit.coeus.utils.xml.v2.user_unit.UNITHIERARCHYType;
import edu.mit.coeus.utils.xml.v2.user_unit.UNITType;
import edu.mit.coeus.utils.xml.v2.propdev.PROPCUSTOMDATA;
import edu.mit.coeus.utils.xml.v2.propdev.PROPKEYPERSONS;
import edu.mit.coeus.utils.xml.v2.propdev.PROPABSTRACT;
import edu.mit.coeus.utils.xml.v2.propdev.PROPPERSON;

import edu.mit.coeus.xml.generator.bean.PropSubFormTxnBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.utils.ModuleConstants;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import oracle.sql.TIMESTAMP;

/**
 *
 * @author  geo thomas
 */
public class PropSubmissionStream  extends BaseStreamGenerator{
    private edu.mit.coeus.utils.xml.v2.propdev.ObjectFactory propObjFact;
    private edu.mit.coeus.utils.xml.v2.rolodex.ObjectFactory rolObjFact;
    private edu.mit.coeus.utils.xml.v2.budget.ObjectFactory budObjFact;
    private edu.mit.coeus.utils.xml.v2.sponsor.ObjectFactory spObjFact;
    private edu.mit.coeus.utils.xml.v2.lookuptypes.ObjectFactory lkObjFact;
    private edu.mit.coeus.utils.xml.v2.organization.ObjectFactory orgObjFact;
    private edu.mit.coeus.utils.xml.v2.user_unit.ObjectFactory usrObjFact;
    private edu.mit.coeus.utils.xml.v2.questionnaire.ObjectFactory qnrObjFact;
    private edu.mit.coeus.utils.xml.v2.rateandbase.ObjectFactory rateAndBaseObjFact;
    private edu.mit.coeus.utils.xml.v2.modularbudget.ObjectFactory modBudgetObjFact;
    private edu.mit.coeus.utils.xml.v2.approvalrouting.ObjectFactory apprRoutingObjFact;
    private String proposalNumber;
    private PropSubFormTxnBean txnBean;
    //Added for COEUSQA-1682_FS_Proposal Development Schema Infrastructure start.
    private QuestionnaireTxnBean qnrTxnBean;
    private BudgetDataTxnBean budgetTxnBean;
    private BudgetUpdateTxnBean budUpdateTxnBean;
    private RatesTxnBean ratesTxnBean;
    private ProposalDevelopmentTxnBean propDevTxnBean;
    private ProposalPersonTxnBean propPerTxnbean;
    private UnitDataTxnBean unitDataTxnBean;
    private SponsorMaintenanceDataTxnBean sponsorTxnBean;
    private AwardTxnBean awardTxnBean;
    private PROPOSAL proposal;
    private MedusaTxnBean medusaTxnBean;
    private BudgetSubAwardTxnBean budSubAwardTxnBean;
   //Added for COEUSQA-1682_FS_Proposal Development Schema Infrastructure end.
    private BigDecimal dummyCode = formatToBigDesimal(0);
    private static final int LINK_TO_IRB_APPROVAL_TYPE_CODE = 5;
    private static final int LINK_TO_IACUC_APPROVAL_TYPE_CODE = 7;
    
    /** Creates a new instance of PropSubmissionStream */
    public PropSubmissionStream() {
        propObjFact = new edu.mit.coeus.utils.xml.v2.propdev.ObjectFactory();
        rolObjFact = new edu.mit.coeus.utils.xml.v2.rolodex.ObjectFactory();
        budObjFact = new edu.mit.coeus.utils.xml.v2.budget.ObjectFactory();
        spObjFact = new edu.mit.coeus.utils.xml.v2.sponsor.ObjectFactory();
        lkObjFact = new edu.mit.coeus.utils.xml.v2.lookuptypes.ObjectFactory();
        orgObjFact = new edu.mit.coeus.utils.xml.v2.organization.ObjectFactory();
        usrObjFact = new edu.mit.coeus.utils.xml.v2.user_unit.ObjectFactory();
        qnrObjFact = new edu.mit.coeus.utils.xml.v2.questionnaire.ObjectFactory();
        rateAndBaseObjFact = new edu.mit.coeus.utils.xml.v2.rateandbase.ObjectFactory();
        modBudgetObjFact = new edu.mit.coeus.utils.xml.v2.modularbudget.ObjectFactory();
        apprRoutingObjFact = new edu.mit.coeus.utils.xml.v2.approvalrouting.ObjectFactory();
        
        txnBean = new PropSubFormTxnBean();
       //Modified for COEUSQA-1682_FS_Proposal Development Schema Infrastructure start.
        qnrTxnBean = new QuestionnaireTxnBean();
        budgetTxnBean = new BudgetDataTxnBean();
        budUpdateTxnBean = new BudgetUpdateTxnBean();
        ratesTxnBean = new RatesTxnBean();
        propDevTxnBean = new ProposalDevelopmentTxnBean();
        propPerTxnbean = new ProposalPersonTxnBean();
        unitDataTxnBean = new UnitDataTxnBean();
        awardTxnBean = new AwardTxnBean();
        sponsorTxnBean = new SponsorMaintenanceDataTxnBean();
        budSubAwardTxnBean = new BudgetSubAwardTxnBean();
       //Added for COEUSQA-1682_FS_Proposal Development Schema Infrastructure end.
    }
    
    public org.w3c.dom.Document getStream(java.util.Hashtable params) throws Exception {
        
        proposal = getProposal(params);
        CoeusXMLGenrator xmlGen = new CoeusXMLGenrator();
        JAXBContext jaxbContext = JAXBContext.newInstance("edu.mit.coeus.utils.xml.v2.propdev");
        Marshaller m = jaxbContext.createMarshaller();
        return xmlGen.marshelObject(proposal, "edu.mit.coeus.utils.xml.v2.propdev");
        
    }
    
    //Modified for COEUSQA-1682_FS_Proposal Development Schema Infrastructure begin.
    /**
     * Returns the PROPOSAL type object. Creates a PROPOSAL type objects and populates its data members
     * by invoking different methods.
     * @param params Hashtable.
     * @throws JAXBException.
     * @throws Exception
     * @return proposal PROPOSAL.
     */
    
    private PROPOSAL getProposal(final java.util.Hashtable params) throws JAXBException, Exception {
        proposalNumber = (String)params.get("PROPOSAL_NUMBER");
        proposal = propObjFact.createPROPOSAL();
        //todo: Remove this trychach after the testing.
        try{
            
            String schoolName = CoeusProperties.getProperty(CoeusPropertyKeys.SCHOOL_NAME);
            String schoolAcronym = CoeusProperties.getProperty(CoeusPropertyKeys.SCHOOL_ACRONYM);
            SchoolInfoType schoolInfoType =  propObjFact.createSchoolInfoType();
            schoolInfoType.setSchoolName(schoolName);
            schoolInfoType.setAcronym(schoolAcronym);
            proposal.setSCHOOLINFOTYPE(schoolInfoType);

            proposal.setPROPOSALMASTER(getPropMaster());
            proposal.getPROPYNQ().addAll(getYnqList());
           
            final String logoPath = getClass().getClassLoader().getResource("edu/mit/coeus/xml/images/").getPath();
            proposal.setLOGOPATH(logoPath);
            
            final Timestamp curTs = new CoeusFunctions().getDBTimestamp();
            final String dateStr = DateUtils.formatDate(curTs,"MM/dd/yyyy");
            proposal.setCURDATE(dateStr);
            proposal.getPROPCUSTOMDATA().addAll(getProposalCustomDatas());
            proposal.setBUDGET(getBudget()); 
            proposal.getQUESTIONNAIRE().addAll(getQuestionnaires());
            proposal.getPROPINVESTIGATORS().addAll(getInvestigators());
            proposal.getPROPINVESTIGATORSBASICDETAILS().addAll(getAllInvestigatorsBasicDetails()); 
            proposal.getPROPPERCREDITSPLIT().addAll(getCreditSplit());
            proposal.getPROPOSALSITES().addAll(getProposalSites());
            proposal.getPROPUNITS().addAll(getAllPropUnits());
            proposal.getPROPSPECIALREVIEW().addAll(getSplReviewers());
            
            proposal.getPROPKEYPERSONS().addAll(getPropKeyPersons());
            proposal.getPROPABSTRACT().addAll(getProposalAbstracts());
            proposal.getPROPUNITCREDITSPLIT().addAll(getPropUnitCreditSplit());
            buildRoutingDetails(proposalNumber,proposal);
            buildRoutingComments(proposalNumber,proposal);
        }catch(Exception e){
            UtilFactory.log(e.getMessage(),e,"PropSubmissionStream", "getProposal");
        }
        
        return proposal;
    }
    //Modified for COEUSQA-1682_FS_Proposal Development Schema Infrastructure end.
    public static void main(String args[]) throws Exception{
        Hashtable param = new Hashtable();
        param.put("PROPOSAL_NUMBER", "00001870");
        PropSubmissionStream p = new PropSubmissionStream();
        System.out.println(Converter.doc2String(p.getStream(param)));
    }
    
    /**
     *  Returns the PROPOSALMASTERType object. Creates the object of type PROPOSALMASTERType and populate its
     *  data values form proMaterData which is passed to the method.
     *  @param proMaterData HashMap.
     *  @throws Exception.
     *  @return PROPOSALMASTERType.
     */
    //Modified for COEUSQA-1682_FS_Proposal Development Schema Infrastructure begin.
    private PROPOSALMASTERType getPropMaster() throws Exception{
        
        final HashMap proMaterData = txnBean.getProposalSubmissionDetails(proposalNumber, "GET_PROPOSAL_DETAILS");
        final ProposalDevelopmentFormBean propDevBean = propDevTxnBean.getProposalDevelopmentDetails(proposalNumber);
        final PROPOSALMASTERType propMaster = propObjFact.createPROPOSALMASTERType();
        Vector vecInvestigator = propDevBean.getInvestigators();
        if(vecInvestigator != null && !vecInvestigator.isEmpty()){
            for(Object investigatorDetails : vecInvestigator){
               ProposalInvestigatorFormBean propInvesDetails = (ProposalInvestigatorFormBean)investigatorDetails;
               if(propInvesDetails.isPrincipleInvestigatorFlag()){
                  propMaster.setPRINCIPALINVESTIGATORNAME(propInvesDetails.getPersonName());
                  propMaster.setPRINCIPALINVESTIGATORID(propInvesDetails.getPersonId());
                  break;
               }
            }
        }
       
        propMaster.setPROPOSALNUMBER(propDevBean.getProposalNumber());
        PROPOSALSTATUSType propStatusType = lkObjFact.createPROPOSALSTATUSType();
        propStatusType.setPROPOSALSTATUSCODE(formatToBigDesimal(propDevBean.getStatusCode()));
        propMaster.setPROPOSALSTATUS(propStatusType);
     
        NAMEType name = rolObjFact.createNAMEType();
        name.setFIRSTNAME((String)proMaterData.get("BUS_CONTACT_NAME_AND_INFO"));
        ROLODEXType rolodex = rolObjFact.createROLODEXType();
        rolodex.setNAME(name);
        
        ADDRESSType address = rolObjFact.createADDRESSType();
        address.setADDRESSLINE1((String)proMaterData.get("BUS_CONTACT_NAME_AND_INFO"));
        rolodex.setADDRESS(address);
        
        ORGANIZATIONType org = orgObjFact.createORGANIZATIONType();
        org.setROLODEX(rolodex);
        propMaster.setORGANIZATION(org);
        
        PROPOSALMASTERType.PRIMESPONSORType prime = propObjFact.createPROPOSALMASTERTypePRIMESPONSORType();
        prime.setSPONSOR(getSponsor(propDevBean.getPrimeSponsorCode()));
        propMaster.setPRIMESPONSOR(prime);
        
        PROPINVESTIGATORS inv = propObjFact.createPROPINVESTIGATORS();
        inv.setPERSONNAME((String)proMaterData.get("PI_NAME_AND_INFO"));
        inv.setPERSONID((String)proMaterData.get("PI_NAME"));
        propMaster.setPROPINVESTIGATORS(inv);
        
        if(propDevBean.getRequestEndDateInitial() != null ){
            propMaster.setREQUESTEDSTARTDATEINITIAL(formatDate(propDevBean.getRequestStartDateInitial()));
        }
        
        if(propDevBean.getRequestEndDateInitial() != null){
            propMaster.setREQUESTEDENDDATEINITIAL(formatDate(propDevBean.getRequestEndDateInitial()));
        }
        
        ACTIVITYTYPEType act = lkObjFact.createACTIVITYTYPEType();
        act.setACTIVITYTYPECODE(formatToBigDesimal(propDevBean.getProposalActivityTypeCode()));
        act.setDESCRIPTION(propDevBean.getProposalActivityTypeDesc());
        
        PROPOSALTYPEType propType = lkObjFact.createPROPOSALTYPEType();
        propType.setPROPOSALTYPECODE(formatToBigDesimal(propDevBean.getProposalTypeCode()));
        propType.setDESCRIPTION(propDevBean.getProposalTypeDesc());
        propMaster.setPROPOSALTYPE(propType);
        
        propMaster.setACTIVITYTYPE(act);
        AwardBean awardBeanDb = getAwardDetails(propDevBean.getCurrentAwardNumber());

        if(awardBeanDb != null){
            propMaster.setCURRENTACCOUNTNUMBER(awardBeanDb.getAccountNumber());
        }
        propMaster.setCURRENTAWARDNUMBER(propDevBean.getCurrentAwardNumber());
        ANTICIPATEDAWARDTYPEType antAwrdType = lkObjFact.createANTICIPATEDAWARDTYPEType();
        antAwrdType.setAWARDTYPECODE(formatToBigDesimal(propDevBean.getAwardTypeCode()));
        antAwrdType.setDESCRIPTION(propDevBean.getAwardTypeDesc());
        
        propMaster.setANTICIPATEDAWARDTYPE(antAwrdType);
        
        NOTICEOFOPPORTUNITYType nop = lkObjFact.createNOTICEOFOPPORTUNITYType();
        nop.setNOTICEOFOPPORTUNITYCODE(formatToBigDesimal(propDevBean.getNoticeOfOpportunitycode()));
        nop.setDESCRIPTION(propDevBean.getNoticeOfOpportunityDescription());
        propMaster.setNOTICEOFOPPORTUNITY(nop);
 
        propMaster.setPROGRAMANNOUNCEMENTNUMBER(propDevBean.getProgramAnnouncementNumber());
        propMaster.setPROGRAMANNOUNCEMENTTITLE(propDevBean.getProgramAnnouncementTitle());
        
        ROLODEXType spRolodex = rolObjFact.createROLODEXType();
        ADDRESSType spAddress = rolObjFact.createADDRESSType();
        
        spAddress.setADDRESSLINE1((String)proMaterData.get("SPONSOR_ADDRESS"));
        spRolodex.setADDRESS(spAddress);
        
        PROPOSALMASTERType.MAILADDRESSType mailAdd = propObjFact.createPROPOSALMASTERTypeMAILADDRESSType();
        mailAdd.setROLODEX(spRolodex);
        propMaster.setMAILADDRESS(mailAdd);
        
        if(propDevBean.isIntrCoopActivitiesFlag()){
            propMaster.setINTRCOOPACTIVITIESFLAG("Y");
        }else{
            propMaster.setINTRCOOPACTIVITIESFLAG("N");
        }
        propMaster.setCONTINUEDFROM(propDevBean.getContinuedFrom());
        
        if(propDevBean.isTemplateFlag()){
            propMaster.setTEMPLATEFLAG("Y");
        }else{
            propMaster.setTEMPLATEFLAG("N");
        }
        
        propMaster.setSPONSORPROPOSALNUMBER(propDevBean.getSponsorProposalNumber()) ;
        propMaster.setINTRCOUNTRYLIST(propDevBean.getIntrCountrylist());
        
        if(propDevBean.isOtherAgencyFlag()){
            propMaster.setOTHERAGENCYFLAG("Y");
        }else{
            propMaster.setOTHERAGENCYFLAG("N");
        }
        
        if(propDevBean.getRequestStartDateTotal() != null){
            propMaster.setREQUESTEDSTARTDATETOTAL(formatDate(propDevBean.getRequestStartDateTotal()));
        }
        if(propDevBean.getRequestEndDateTotal()!= null){
            propMaster.setREQUESTEDENDDATETOTAL(formatDate(propDevBean.getRequestEndDateTotal())); 
        }
        
        if(propDevBean.getDeadLineDate() != null){
            propMaster.setDEADLINEDATE(formatDate(propDevBean.getDeadLineDate()));
        }
        
        propMaster.setDURATIONMONTHS(formatToBigDesimal(propDevBean.getDurationMonth()));
        propMaster.setNUMBEROFCOPIES(propDevBean.getNumberCopies());
        propMaster.setDEADLINE(propDevBean.getDeadLineType());
        propMaster.setDEADLINETYPE(propDevBean.getDeadLineType());
        
        //setMailingAddressId
        
        propMaster.setMAILBY(propDevBean.getMailBy());
        propMaster.setMAILTYPE(propDevBean.getMailType());
        propMaster.setCARRIERCODETYPE(propDevBean.getCarrierCodeType());
        propMaster.setCARRIERCODE(propDevBean.getCarrierCode());
        propMaster.setMAILDESCRIPTION(propDevBean.getMailDescription());
        propMaster.setMAILACCOUNTNUMBER(propDevBean.getMailAccountNumber());
        
        if(propDevBean.isSubcontractFlag()){
            propMaster.setSUBCONTRACTFLAG("Y");
        }else{
            propMaster.setSUBCONTRACTFLAG("N");
        }
        
        propMaster.setNARRATIVESTATUS(propDevBean.getNarrativeStatus());
        propMaster.setBUDGETSTATUS(propDevBean.getBudgetStatus());
        propMaster.setOWNEDBYUNIT(propDevBean.getOwnedBy());
        propMaster.setCREATETIMESTAMP(formatDate(propDevBean.getCreateTimeStamp()));
        propMaster.setCREATEUSER(propDevBean.getCreateUser());
        
        if(propDevBean.getUpdateTimestamp() != null){
            propMaster.setUPDATETIMESTAMP(formatDate(propDevBean.getUpdateTimestamp()));
        }
        propMaster.setUPDATEUSER(propDevBean.getUpdateUser());
        NSFSCIENCECODEType nsfScienceCode = lkObjFact.createNSFSCIENCECODEType();
        nsfScienceCode.setNSFCODE(propDevBean.getNsfCode());
        nsfScienceCode.setDESCRIPTION(propDevBean.getNsfCodeDescription());
        propMaster.setNSFSCIENCECODE(nsfScienceCode);
        propMaster.setCFDANUMBER(propDevBean.getCfdaNumber());
        propMaster.setAGENCYPROGRAMCODE(propDevBean.getAgencyProgramCode());
        propMaster.setAGENCYDIVISIONCODE(propDevBean.getAgencyDivCode());
        propMaster.setLEADUNIT((String)proMaterData.get("LEAD_UNIT"));
        propMaster.setCOI((String)proMaterData.get("CONFLICT_OF_INTEREST"));
        propMaster.setADDLMAILINGINSTR((String)proMaterData.get("ADDL_MAILING_INSTRUCTIONS"));
        propMaster.setCENTERINST((String)proMaterData.get("CENTER_INSTITUTE"));
        propMaster.setTITLE(propDevBean.getTitle());
        propMaster.setPERIOD((String)proMaterData.get("PROJECT_PERIOD"));
        propMaster.setSPONSORCOST((String)proMaterData.get("SPONSOR_COST"));
        propMaster.setCOSTSHARING((String)proMaterData.get("COST_SHARING"));
        propMaster.setOTHERCOMMENTS((String)proMaterData.get("OTHER_COMMENTS"));
        propMaster.setSPONSOR(getSponsor(propDevBean.getSponsorCode()));
        

        return propMaster;
    }
    //Modified for COEUSQA-1682_FS_Proposal Development Schema Infrastructure end.
    
    //Added for COEUSQA-1682_FS_Proposal Development Schema Infrastructure begin.
    /**
     *  Returns the list of all PROPPERCREDITSPLITType type of objects of a proposal. Identifies all 
     *  InvestigatorCreditSplitBean objects and creates PROPPERCREDITSPLITType type object from this data.
     *  Returns the list of PROPPERCREDITSPLITType type objects
     *  @throws JAXBException.
     *  @throws CoeusException.
     *  @throws DBException.
     *  @return propPerCreditList List.
     */
    
//    private List getPropPerCreditSplit()throws JAXBException, CoeusException, DBException, ParseException {
//        
//        final List propPerCreditListDb = propDevTxnBean.getPropPerCreditSplit(proposalNumber);
//        final Vector propPerCreditList = new Vector();
//        
//        if(propPerCreditListDb != null){
//            InvestigatorCreditSplitBean invCreditSplitDb = null;
//            PROPPERCREDITSPLITType propPerCerditSplit = null;
//            
//            for(Object obj : propPerCreditListDb){
//                 invCreditSplitDb = (InvestigatorCreditSplitBean)obj;
//                 propPerCerditSplit = propObjFact.createPROPPERCREDITSPLITType();
//                 
//                 propPerCerditSplit.setPROPOSALNUMBER(invCreditSplitDb.getModuleNumber());
//                 
//                 propPerCerditSplit.setPERSONID(invCreditSplitDb.getPersonId());
//                 INVCREDITTYPEType invCreditType = lkObjFact.createINVCREDITTYPEType();
//                 invCreditType.setINVCREDITTYPECODE(formatToBigDesimal(invCreditSplitDb.getInvCreditTypeCode()));
//                 invCreditType.setDESCRIPTION(invCreditSplitDb.getDescription());
//                 
//                 propPerCerditSplit.setINVCREDITTYPE(invCreditType);
//                 if(invCreditSplitDb.getCredit() != null){
//                     propPerCerditSplit.setCREDIT(new Double(invCreditSplitDb.getCredit()).toString());
//                 }
//                 
//                 if(invCreditSplitDb.getUpdateTimestamp() != null){
//                    propPerCerditSplit.setUPDATETIMESTAMP(formatDate(invCreditSplitDb.getUpdateTimestamp()));
//                 }
//                 propPerCerditSplit.setUPDATEUSER(invCreditSplitDb.getUpdateUser());
//                 // todo:PROP_UNIT_CREDIT_SPLIT
//                 
//                 propPerCreditList.add(propPerCerditSplit);
//            }
//        }
//        
//        return propPerCreditList;
//    }
    
    /**
     *  Returns the list of all PROPUNITCREDITSPLIT type objects. Identifies all InvestigatorCreditSplitBean
     *  type objects and creates PROPUNITCREDITSPLIT type objects from this data. Returns the List of 
     *  PROPUNITCREDITSPLIT type objects.
     *  @throws JAXBException.
     *  @throws CoeusException.
     *  @throws DBException.
     */
    private List getPropUnitCreditSplit()throws JAXBException, CoeusException, DBException, ParseException{
        
        final List propUnitCreditListDb = propDevTxnBean.getPropUnitCreditSplit(proposalNumber);
        final Vector propUnitCreditList = new Vector();
        
        if(propUnitCreditListDb != null){
              InvestigatorCreditSplitBean creditSplitBeanDb = null;
              PROPUNITCREDITSPLIT creditSplitBean = null;
              Calendar calendar = null;
              
              for(Object obj : propUnitCreditListDb){
                  creditSplitBeanDb = (InvestigatorCreditSplitBean)obj;
                  creditSplitBean =  propObjFact.createPROPUNITCREDITSPLIT();
                  
                  creditSplitBean.setPROPOSALNUMBER(creditSplitBeanDb.getModuleNumber());
                  creditSplitBean.setPERSONID(creditSplitBeanDb.getPersonId());
                  
                  INVCREDITTYPEType invCreditType = lkObjFact.createINVCREDITTYPEType();
                  invCreditType.setINVCREDITTYPECODE(formatToBigDesimal(creditSplitBeanDb.getInvCreditTypeCode()));
                  invCreditType.setDESCRIPTION(creditSplitBeanDb.getDescription());
                  creditSplitBean.setINVCREDITTYPE(invCreditType);
                  
                  creditSplitBean.setCREDIT(formatToBigDesimal(creditSplitBeanDb.getCredit()));
                  
                  if(creditSplitBeanDb.getUpdateTimestamp() != null){
                      
                      creditSplitBean.setUPDATETIMESTAMP(formatDate(creditSplitBeanDb.getUpdateTimestamp()));
                  }
                  creditSplitBean.setUPDATEUSER(creditSplitBeanDb.getUpdateUser());
                  //creditSplitBean.setPRIMARY(creditSplitBeanDb.get);
                  //creditSplitBean.setSECONDARY(creditSplitBeanDb.get);
                  //UNIT
                  propUnitCreditList.add(creditSplitBean);
              }
             
        }
        
        return propUnitCreditList;
        
    }
    
    /** Identifies the AwardBean type object using awardNumber which is parameter to the method.
     *  Returns the AwardBean type object.
     *  @throws CoeusException.
     *  @throws DBException.
     *  @return awardBeanDb AwardBean.
     */
    
    private AwardBean getAwardDetails(final String awardNumber)
                                throws CoeusException, DBException{
        AwardBean awardBeanDb = awardTxnBean.getAward(awardNumber);
        
        return awardBeanDb;
    }
    /**
     *  Returns SPONSORType type object. Identifie a SponsorMaintenanceFormBean type object using sponsorId 
     *  with is passed to this method. Creates SPONSORType type object from this data. 
     *  Returns SPONSORType type object.
     *  @throws JAXBException.
     *  @throws CoeusException.
     *  @throws DBException.
     *  @return sponsorType SPONSORType.
     */
    
    private SPONSORType getSponsor(final String sponsorId)throws JAXBException, CoeusException, DBException{
        final SponsorMaintenanceFormBean sponsorBeanDb = sponsorTxnBean.getSponsorMaintenanceDetails(sponsorId);
        SPONSORType sponsorType = null;
        
        if(sponsorBeanDb != null){
            sponsorType = spObjFact.createSPONSORType(); 
            
            sponsorType.setSPONSORCODE(sponsorBeanDb.getSponsorCode());
            sponsorType.setSPONSORNAME(sponsorBeanDb.getName());
            sponsorType.setACRONYM(sponsorBeanDb.getAcronym());
            sponsorType.setDUNANDBRADSTREETNUMBER(sponsorBeanDb.getDuns());
            sponsorType.setDODACNUMBER(sponsorBeanDb.getDodc());
            sponsorType.setCAGENUMBER(sponsorBeanDb.getCage());
            sponsorType.setPOSTALCODE(sponsorBeanDb.getPostalCode());
            sponsorType.setSTATE(sponsorBeanDb.getState());
            sponsorType.setCOUNTRYCODE(sponsorBeanDb.getCountry());
            
            if(sponsorBeanDb.getRolodexID() != null){
                sponsorType.setROLODEXID(formatToBigDesimal(
                                new Double(sponsorBeanDb.getRolodexID())));
            }
           
            sponsorType.setAUDITREPORTSENTFORFY(sponsorBeanDb.getAuditReport());
            sponsorType.setOWNEDBYUNIT(sponsorBeanDb.getOwnedBy());
            sponsorType.setCREATEUSER(sponsorBeanDb.getCreateUser());
            
            if(sponsorBeanDb.getUpdateTimestamp() != null){
                sponsorType.setUPDATETIMESTAMP(formatDate(sponsorBeanDb.getUpdateTimestamp()));
            }
            sponsorType.setUPDATEUSER(sponsorBeanDb.getUpdateUser());
            
            //SPONSOR_TYPES
            //SPONSOR_CONTACT
            //SPONSOR_FORMS
            //SPONSOR_HIERARCHY
        }
                
        return sponsorType;         
    }
    
    /**
     *  Returns the list of PROPYNQType type objects.Identifies all ProposalYNQBean type objects.
     *  Creates PROPYNQType type object using this data. Returns the list of PROPYNQType type objects.
     *  @throws JAXBException.
     *  @retun ynqList PROPYNQType.
     */
    
    private List getYnqList()throws JAXBException, CoeusException, DBException, ParseException{
        
        final List propYnqListDb = propDevTxnBean.getProposalYNQ(proposalNumber);
        final Vector propYnqList = new Vector();
        
        if(propYnqListDb != null){
             ProposalYNQBean ynqBeanDb = null;
             PROPYNQType ynqBean = null;
             
             for(Object obj : propYnqListDb){
                ynqBeanDb = (ProposalYNQBean)obj;
                ynqBean = propObjFact.createPROPYNQType();
                    
                ynqBean.setPROPOSALNUMBER(ynqBeanDb.getProposalNumber());
                
                Vector vecYNQQuestionList = propDevTxnBean.getYesNoQuestionList("P",proposalNumber);
                String questionDescription = "";
                if(vecYNQQuestionList != null && !vecYNQQuestionList.isEmpty()){
                    for(Object questionDetails : vecYNQQuestionList){
                        QuestionListBean questionListBean = (QuestionListBean)questionDetails;
                        if(questionListBean.getQuestionId().equals(ynqBeanDb.getQuestionId())){
                            questionDescription = questionListBean.getDescription();
                            break;
                        }
                    }
                }
                ynqBean.setQUESTIONID(ynqBeanDb.getQuestionId());
                ynqBean.setQUESTIONDESCRIPTION(questionDescription);
                
                ynqBean.setANSWER(ynqBeanDb.getAnswer());
                ynqBean.setEXPLANATION(ynqBeanDb.getExplanation());

                if(ynqBeanDb.getReviewDate()!= null){
                    ynqBean.setREVIEWDATE(formatDate(Timestamp.valueOf(ynqBeanDb.getReviewDate())));
                }

                ynqBean.setUPDATEUSER(ynqBeanDb.getUpdateUser());
                if(ynqBeanDb.getUpdateTimeStamp()!= null){
                    ynqBean.setUPDATETIMESTAMP(formatDate(ynqBeanDb.getUpdateTimeStamp()));
                }
                
                propYnqList.add(ynqBean);
             }
        }
       
        return propYnqList;
    }
    
    /**
     * Returns list of  PROPCUSTOMDATA type objects. Creates PROPCUSTOMDATA type object from the result set
     * list which contains Hashmap with table colum name key.
     * @throws JAXBException.
     * @throws DBException.
     * @throws CoeusException.
     * @return proCustomDataList List.
     */
    
    private List getProposalCustomDatas() throws JAXBException, DBException,CoeusException{
        final List resultListDb = txnBean.getProSubDetList(proposalNumber, "GET_PROP_CUSTOM_DATA");
        final Vector proCustomDataList = new Vector();
        
        if(resultListDb != null){
            HashMap propCustomDataDb = null;
            PROPCUSTOMDATA propCustomData = null;
            
            for(int index =0; index < resultListDb.size(); index++){
                propCustomDataDb = (HashMap)resultListDb.get(index);
                propCustomData = propObjFact.createPROPCUSTOMDATA();
                
                propCustomData.setPROPOSALNUMBER((String)propCustomDataDb.get("PROPOSAL_NUMBER"));
                propCustomData.setCOLUMNNAME((String)propCustomDataDb.get("COLUMN_LABEL"));
                propCustomData.setCOLUMNVALUE((String)propCustomDataDb.get("COLUMN_VALUE"));
                propCustomData.setUPDATEUSER((String)propCustomDataDb.get("UPDATE_USER"));
                
                Date updateDate = (Date)propCustomDataDb.get("UPDATE_TIMESTAMP");
                
                if(updateDate != null){
                    propCustomData.setUPDATETIMESTAMP(formatDate(updateDate));
                }
                
                proCustomDataList.add(propCustomData);
            }
        }
        
        return proCustomDataList;
    }
    
    /**
     * Is used to get all  Questionnaire objects for a proposal.
     * @throws  DBException.
     * @throws  JAXBException.
     * @throws  CoeusException.
     */
    
    private List getQuestionnaires() throws DBException, JAXBException, CoeusException {
        
        QuestionnaireAnswerHeaderBean ansHeader = new QuestionnaireAnswerHeaderBean();
        
        ansHeader.setModuleItemCode(ModuleConstants.PROPOSAL_DEV_MODULE_CODE);
        ansHeader.setModuleItemKey(proposalNumber);
        
        //Getting all the Questionnaire objects corrousponding to a proposal form the database
        List qnrListFromDb = qnrTxnBean.fetchApplicableQuestionnairedForModule(ansHeader);
        Vector questionnaireList = new Vector();
        
        int questionnaireId = 0;
        QuestionnaireAnswerHeaderBean qstBean = null;
        CoeusVector cvData = null;
        QuestionnaireBaseBean dataBean = null;
        
        List ansHeaderListFromDb = qnrTxnBean.getQuestionnaireHeaderDetails(ansHeader);
        AnswerHeaderType ansHeaderType = null;
        
        if(qnrListFromDb != null){
            for(Object bean : qnrListFromDb){
                
                qstBean = (QuestionnaireAnswerHeaderBean)bean;
                questionnaireId = qstBean.getQuestionnaireId();
                cvData = qnrTxnBean.getQuestionnaireData(questionnaireId);
                
                if(cvData != null && cvData.size() > 0) {
                    dataBean = (QuestionnaireBaseBean)cvData.get(0);
                }
                
                if(dataBean != null){
                    QuestionnaireType qnrType = getQuestionnaireDetails(dataBean);
                    
                    if(ansHeaderListFromDb != null){
                        ansHeaderType = getAnswerHeaderType(ansHeaderListFromDb,
                                dataBean.getQuestionnaireId(),
                                dataBean.getQuestionnaireVersionNumber());
                    }
                    
                    qnrType.getANSWERHEADER().add(ansHeaderType);
                    questionnaireList.add(qnrType);
                    
                }
            }
        }
        
        return questionnaireList;
    }
    
    /**
     * It will identifies final version of budget associating with a proposal.
     * @throws JAXBException.
     * @throws DBException.
     * @throws CoeusException.
     */
    private BUDGETType getBudget() throws JAXBException, DBException, CoeusException, ParseException{
        
        final List budPropListDb = budgetTxnBean.getBudgetForProposal(proposalNumber);
        CoeusVector cvRateClass = budgetTxnBean.getOHRateClassList();
        
        final BudgetInfoBean budgetInfoDb = getFinalOrLastVersionOfBudget(budPropListDb);
        BUDGETType budget = null;
        
        if(budgetInfoDb != null){
            budget = budObjFact.createBUDGETType();
            
            budget.setBudgetMaster(budObjFact.createBUDGETTypeBudgetMasterType());
            budget.getBudgetMaster().setVERSIONNUMBER(formatToBigDesimal(budgetInfoDb.getVersionNumber()));
            
            if(budgetInfoDb.getStartDate()!= null){
                budget.getBudgetMaster().setSTARTDATE(formatDate(budgetInfoDb.getStartDate()));
            }
            
            if(budgetInfoDb.getEndDate() != null){
                budget.getBudgetMaster().setENDDATE(formatDate(budgetInfoDb.getEndDate()));
            }
            
            budget.getBudgetMaster().setTOTALCOST(formatToBigDesimal(budgetInfoDb.getTotalCost()));
            budget.getBudgetMaster().setTOTALINDIRECTCOST(formatToBigDesimal(budgetInfoDb.getTotalIndirectCost()));
            budget.getBudgetMaster().setCOSTSHARINGAMOUNT(formatToBigDesimal(budgetInfoDb.getCostSharingAmount()));
            budget.getBudgetMaster().setUNDERRECOVERYAMOUNT(formatToBigDesimal(budgetInfoDb.getUnderRecoveryAmount()));
            budget.getBudgetMaster().setTOTALDIRECTCOST(formatToBigDesimal(budgetInfoDb.getTotalDirectCost()));
            budget.getBudgetMaster().setRESIDUALFUNDS(formatToBigDesimal(budgetInfoDb.getResidualFunds()));
            budget.getBudgetMaster().setTOTALCOSTLIMIT(formatToBigDesimal(budgetInfoDb.getTotalCostLimit()));
            budget.getBudgetMaster().setTOTALDIRECTCOSTLIMIT(formatToBigDesimal(budgetInfoDb.getTotalDirectCostLimit()));
            budget.getBudgetMaster().setOHRATECLASSCODE(formatToBigDesimal(budgetInfoDb.getOhRateClassCode()));
            
            
            
            budget.getBudgetMaster().setOHRATETYPECODE(formatToBigDesimal(budgetInfoDb.getOhRateTypeCode()));
            
            Equals ohRateClass = new Equals("code", budgetInfoDb.getOhRateTypeCode()+"");
            CoeusVector cvOHFileteredRateClass = cvRateClass.filter(ohRateClass);
            if(cvOHFileteredRateClass != null && !cvOHFileteredRateClass.isEmpty()){
                RateClassBean rateClassBean = (RateClassBean)cvOHFileteredRateClass.get(0);
                budget.getBudgetMaster().setOHRATETYPEDESCRIPTION(rateClassBean.getDescription());
            }
            
            
            budget.getBudgetMaster().setCOMMENTS(budgetInfoDb.getComments());
            
            if(budgetInfoDb.isFinalVersion()){
                budget.getBudgetMaster().setFINALVERSIONFLAG("Y");
            }else{
                budget.getBudgetMaster().setFINALVERSIONFLAG("N");
            }
            
            if(budgetInfoDb.isSubmitCostSharingFlag()){
                budget.getBudgetMaster().setSUBMITCOSTSHARINGFLAG("Y");
            }else{
                budget.getBudgetMaster().setSUBMITCOSTSHARINGFLAG("N");
            }
            
            if(budgetInfoDb.isDefaultIndicator()){
                budget.getBudgetMaster().setONOFFCAMPUSFLAG("Default");
            }else if(budgetInfoDb.isOnOffCampusFlag()){
                budget.getBudgetMaster().setONOFFCAMPUSFLAG("on");
            }else{
                budget.getBudgetMaster().setONOFFCAMPUSFLAG("off");
            }
            if(budgetInfoDb.getUpdateTimestamp() != null){
                budget.getBudgetMaster().setUPDATETIMESTAMP(formatDate(budgetInfoDb.getUpdateTimestamp()));
            }
            
            budget.getBudgetMaster().setUPDATEUSER(budgetInfoDb.getUpdateUser());
            budget.getBudgetMaster().setURRATECLASSCODE(formatToBigDesimal(budgetInfoDb.getUrRateClassCode()));
            Equals urRateClass = new Equals("code", budgetInfoDb.getUrRateClassCode()+"");
            CoeusVector cvURFileteredRateClass = cvRateClass.filter(urRateClass);
            if(cvURFileteredRateClass != null && !cvURFileteredRateClass.isEmpty()){
                RateClassBean rateClassBean = (RateClassBean)cvURFileteredRateClass.get(0);
                budget.getBudgetMaster().setURRATETYPEDESCRIPTION(rateClassBean.getDescription());
            }
            
            if(budgetInfoDb.isBudgetModularFlag()){
                budget.getBudgetMaster().setMODULARBUDGETFLAG("Y");
            }else{
                budget.getBudgetMaster().setMODULARBUDGETFLAG("N");
            }
            budget.getBudgetMaster().setPROPOSALNUMBER(budgetInfoDb.getProposalNumber());
            final List budgetPeirodListDb = budgetTxnBean.getBudgetPeriods(proposalNumber, budgetInfoDb.getVersionNumber());
            
            if(budgetPeirodListDb != null){
                BudgetPeriodBean budgetPeriodDb = null;
                
                for(Object object : budgetPeirodListDb){
                    budgetPeriodDb = (BudgetPeriodBean)object;
                    budget.getBudgetMaster().getRATEANDBASE().addAll(
                            getRateAndBase(budgetInfoDb.getVersionNumber(),
                            budgetPeriodDb.getBudgetPeriod())
                            );
                }
                
            }
            budget.getBudgetMaster().getBUDGETPERIOD().addAll(getBudgetPeriod(budgetPeirodListDb,  budgetInfoDb.getVersionNumber()));
            budget.getBudgetMaster().getMODULARBUDGET().addAll(getModularBudget(budgetInfoDb.getVersionNumber()));
            budget.getBudgetMaster().getCUMULATIVEMODULARBUDGET().add(getCumulativeModularBudget(budgetInfoDb.getVersionNumber()));
        }
        
        return budget;
    }
    
    
    private BudgetInfoBean getFinalOrLastVersionOfBudget(final List budPropListDb) 
                                          throws DBException, CoeusException{
        
        BudgetInfoBean budgetInfoBean = null;
        final int finalVersionNumber = budSubAwardTxnBean.getBudgetFinalVersion(proposalNumber);
        
        if(finalVersionNumber != 0 && budPropListDb != null){
            for(Object obj : budPropListDb){
                budgetInfoBean = (BudgetInfoBean)obj;
                if(budgetInfoBean.getVersionNumber() == finalVersionNumber){
                    break;
                }
            }
        }else if(budPropListDb != null && budPropListDb.size() > 0){
            budgetInfoBean = (BudgetInfoBean)budPropListDb.get(budPropListDb.size()-1);
        }
        
        return budgetInfoBean;
    }
    /**
     *  Return java.math.BigDecimal object. Creates a java.math.BigDecimal type object from a double number
     *  which is passed to the method. Format the BigDecimal type object two pression points. Returns the 
     *  formatted BigDecimal number.
     *  @param desimalNumber double.
     *  @return formattingNumber BigDecimal.
     */
    
    private BigDecimal formatToBigDesimal(final double desimalNumber){
        
        BigDecimal formattingNumber = new BigDecimal(desimalNumber);
        formattingNumber = formattingNumber.setScale(2,BigDecimal.ROUND_HALF_DOWN);
      
        return formattingNumber;
    }
    
    /**
     *  Returns java.util.Calendar type object. Creates a calendar type object and set it time to date which is 
     *  passed to the method. Return the Calendar type object.
     *  @param date Date.
     *  @return calendar Calendar.
     */
    private Calendar formatDate(final Date date){
        Calendar calendar =null;
        if(date != null){
            calendar = calendar.getInstance();
            calendar.setTime(date);
        }
        
        return calendar;
    }
     
    /**
     *  Retuns the Date type object. Creates Date type object using string which is passed to the method.
     *  Returns the Date type object.
     *  @param dateString String.
     *  @return date Date.
     */
    
    private Date convertStringToDate(final String dateString)throws ParseException{
         Date date = null;
         
         if(dateString != null && "".equals(dateString)){
             SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
             date = formatter.parse(dateString);
         }
         
         return date;
    }
    
    /**
     * It will identifies all RateAndBase associating with a budget.
     * @param versionNumber int.
     * @throws DBException.
     * @throws CoeusException.
     */
    
    private List getRateAndBase(final int versionNumber, final int budgetPeriod)
                                throws CoeusException, DBException,ParseException,JAXBException{
        
        final List rateAndBaseList = new Vector();
        final BudgetPeriodBean  budgetPeriodBean = new BudgetPeriodBean();
        budgetPeriodBean.setProposalNumber(proposalNumber);
        budgetPeriodBean.setVersionNumber(versionNumber);
        budgetPeriodBean.setBudgetPeriod(budgetPeriod); 
        
        final Hashtable resultTable = budUpdateTxnBean.addUpdDeleteBudgetEDIRollBack(budgetPeriodBean, true);
        final List rateAndBaseListDb = getRateBaseList(resultTable);
        final List rateAndTypeListDb =  ratesTxnBean.getRateTypeList();
      
        if(rateAndBaseListDb != null){
            RATEANDBASEType rateAndBase = null;
            BudgetSummaryReportBean rateBaseBeanDb = null;
            
            for(Object obj : rateAndBaseListDb){
                rateBaseBeanDb = (BudgetSummaryReportBean)obj;
                
                rateAndBase = rateAndBaseObjFact.createRATEANDBASEType();//In object factory this object creation is invisible.
                //todo: check whether we need to set more setters.
                rateAndBase.setPROPOSALNUMBER(proposalNumber);
                rateAndBase.setBUDGETPERIOD(budgetPeriod);
                rateAndBase.setVERSIONNUMBER(formatToBigDesimal(versionNumber));
                //rateAndBase.setLINEITEMNUMBER(rateBaseBeanDb.)
                rateAndBase.setAPPLIEDRATE(formatToBigDesimal(rateBaseBeanDb.getAppliedRate()));
                if(rateBaseBeanDb.getStartDate()!= null){
                    rateAndBase.setSTARTDATE(formatDate(rateBaseBeanDb.getStartDate()));
                }
                
                if(rateBaseBeanDb.getEndDate() != null){
                    rateAndBase.setENDDATE(formatDate(rateBaseBeanDb.getEndDate()));
                }
                
                rateAndBase.setSALARYREQUESTED(formatToBigDesimal(rateBaseBeanDb.getSalaryRequested()));
                rateAndBase.setCALCULATEDCOST(formatToBigDesimal(rateBaseBeanDb.getCalculatedCost()));
               // rateAndBase.setRATECLASSCODE(rateBaseBeanDb.getRateClassDesc().toString());
                if(rateBaseBeanDb.isOnOffCampus()){
                    rateAndBase.setONOFFCAMPUSFLAG("Y");
                }else{
                    rateAndBase.setONOFFCAMPUSFLAG("N");
                }
                
                if(rateAndTypeListDb != null){
                    rateAndBase.setRATETYPE(getRateType(rateBaseBeanDb.getRateClassDesc(),
                            rateBaseBeanDb.getRateTypeDesc(), rateAndTypeListDb));
                }
                
                rateAndBaseList.add(rateAndBase);
            }
        }
        
        return rateAndBaseList;
    }
    
    /**
     * It will identifies all RateType  associating with a RateBase.
     * @param rateTypeCode int.
     * @param rateClassCode int.
     * @param rateAndTypeListDb List.
     */
    
    private RATETYPEType getRateType(final String rateClassDesc,
                                     final String rateTypeDesc,
                                     final List rateAndTypeListDb)throws JAXBException{
        RATETYPEType rateType = null;
        
        if(rateAndTypeListDb != null){
            RateTypeBean rateTypeFromDb = null;
            
            for(Object obj : rateAndTypeListDb){
                rateTypeFromDb = (RateTypeBean)obj;
             
                if((rateClassDesc != null && rateClassDesc.equals(rateTypeFromDb.getDescription()))
                   ||(rateTypeDesc != null && rateTypeDesc.equals(rateTypeFromDb.getDescription()))){
                    
                    rateType = lkObjFact.createRATETYPEType();
                    rateType.setDESCRIPTION(rateTypeFromDb.getDescription());
                    rateType.setRATECLASSCODE(rateTypeFromDb.getRateClassCode());
                    rateType.setRATETYPECODE(rateTypeFromDb.getRateTypeCode());
                }
            }
        }
        
        return rateType;
    }
    
    /**
     * It will identifies all RateBaseList associating with a proposal.
     * @param resultTable Map.
     * @return resultList List.
     */
    
    private List getRateBaseList(final Map resultTable){
        
        final Vector resultList = new Vector();
        Vector tmpList = null;
        
        tmpList = (Vector)resultTable.get("GET_BUDGET_OH_RATE_BASE");
        if(tmpList != null){
            resultList.addAll(tmpList);
            tmpList = null;
        }
        
        tmpList = (Vector)resultTable.get("GET_BUDGET_EB_RATE_BASE");
        if(tmpList != null){
            resultList.addAll(tmpList);
            tmpList = null;
        }
        
        tmpList = (Vector)resultTable.get("GET_BUDGET_LA_RATE_BASE");
        if(tmpList != null){
            resultList.addAll(tmpList);
            tmpList = null;
        }
        
        tmpList = (Vector)resultTable.get("GET_BUDGET_VAC_RATE_BASE");
        if(tmpList != null){
            resultList.addAll(tmpList);
            tmpList = null;
        }
        
        tmpList = (Vector)resultTable.get("GET_BUDGET_OTHER_RATE_BASE");
        if(tmpList != null){
            resultList.addAll(tmpList);
            tmpList = null;
        }
        
        return resultList;
    }
    
    /**
     * It will identifies all the ModularBudget associating with a budget.
     * @param versionNumber int.
     * @throws JAXBException.
     * @throws DBException.
     * @throws CoeusException.
     * @return resultList List.
     */
    
    private List getModularBudget(final int versionNumber)
                                  throws JAXBException, DBException, CoeusException{
        
        final Vector modBudDataListDb = budgetTxnBean.getBudgetModularData(proposalNumber, versionNumber);
        final Vector indirectCostListDb = budgetTxnBean.getBudgetModularIDCData(proposalNumber, versionNumber);
        final Vector modBudDataList = new Vector();
        
        if(modBudDataListDb != null){
            BudgetModularBean modularBudBeanDb = null;
            MODULARBUDGETType modularBudType = null;
             
            for(Object obj : modBudDataListDb){
                modularBudBeanDb = (BudgetModularBean)obj;
                modularBudType = modBudgetObjFact.createMODULARBUDGETType();
                
                modularBudType.setPROPOSALNUMBER(modularBudBeanDb.getProposalNumber());
                modularBudType.setVERSIONNUMBER(formatToBigDesimal(modularBudBeanDb.getVersionNumber()));
                modularBudType.setBUDGETPERIODNUMBER(modularBudBeanDb.getBudgetPeriod());
                modularBudType.setDIRECTCOSTLESSCONSORFNA(formatToBigDesimal(modularBudBeanDb.getDirectCostFA()));
                modularBudType.setCONSORTIUMFNA(formatToBigDesimal(modularBudBeanDb.getConsortiumFNA()));
                modularBudType.setTOTALDIRECTCOST(formatToBigDesimal(modularBudBeanDb.getTotalDirectCost()));
                
                if(indirectCostListDb != null){
                    modularBudType.getINDIRECTCOSTS().addAll( getModularBudgetIndirectCost(indirectCostListDb,
                            modularBudBeanDb.getBudgetPeriod()));
                }
                modBudDataList.add(modularBudType);
            }
        }
        
        return modBudDataList;
    }
    
    
    
    /**
     * It will identifies all the ModularBudgetIndirectCost associating with a ModularBudget.
     * @param indirectCostListDb List.
     * @param bugetPeriod int.
     * @throws JAXBException.
     * @throws DBException.
     * @throws CoeusException.
     * @return indirectCostList List.
     */
    
    private List getModularBudgetIndirectCost(final List indirectCostListDb, final int bugetPeriod)
                                              throws JAXBException, DBException, CoeusException{
        
        final Vector indirectCostList = new Vector();
        
        if(indirectCostListDb != null){
            BudgetModularIDCBean modularIDCBeanDb = null;
            INDIRECTCOSTType indirectCost = null;
            
            for(Object obj : indirectCostListDb){
                modularIDCBeanDb = (BudgetModularIDCBean)obj;
                
                if(modularIDCBeanDb.getBudgetPeriod() == bugetPeriod){
                    indirectCost = modBudgetObjFact.createINDIRECTCOSTType();
                    
                    indirectCost.setRATENUMBER(modularIDCBeanDb.getRateNumber());
                    indirectCost.setDESCRIPTION(modularIDCBeanDb.getDescription());
                    indirectCost.setIDCRATE(formatToBigDesimal(modularIDCBeanDb.getIdcRate()));
                    indirectCost.setIDCBASE(formatToBigDesimal(modularIDCBeanDb.getIdcBase()));
                    indirectCost.setFUNDSREQUESTED(formatToBigDesimal(modularIDCBeanDb.getFundRequested()));
                    
                    indirectCostList.add(indirectCost);
                }
            }
        }
        
        return indirectCostList;
    }
    
    /**
     * It will identifies all the BudgetPeriod associating with a Budget.
     * @param versionNumber int.
     * @throws JAXBException.
     * @throws DBException.
     * @throws CoeusException.
     * @return budgetPeiodList List.
     */
    
    private List getBudgetPeriod(final List budgetPeirodListDb, final int versionNumber)
                             throws JAXBException, DBException, CoeusException{
        
        final List budgetDetailListDb = budgetTxnBean.getBudgetDetail(proposalNumber, versionNumber);
        final  Vector budgetPeiodList  = new Vector();
        
        if(budgetPeirodListDb != null){
            BUDGETPERIODType budgetPeiodType = null;
            BudgetPeriodBean  budgetPeriodDb = null;
            
            for(Object obj : budgetPeirodListDb){
                
                budgetPeriodDb = (BudgetPeriodBean)obj;
                budgetPeiodType = budObjFact.createBUDGETPERIODType();
                budgetPeiodType.setPROPOSALNUMBER(budgetPeriodDb.getProposalNumber());
                budgetPeiodType.setVERSIONNUMBER(budgetPeriodDb.getVersionNumber());
                budgetPeiodType.setBUDGETPERIODNUMBER(budgetPeriodDb.getBudgetPeriod());
                budgetPeiodType.setNOOFMONTHS(formatToBigDesimal(budgetPeriodDb.getNoOfPeriodMonths()));
                if(budgetPeriodDb.getStartDate() != null){
                    budgetPeiodType.setSTARTDATE(formatDate(budgetPeriodDb.getStartDate()));
                }
                
                if(budgetPeriodDb.getEndDate() != null){
                    budgetPeiodType.setENDDATE(formatDate(budgetPeriodDb.getEndDate()));
                }
                
                budgetPeiodType.setTOTALCOST(formatToBigDesimal(budgetPeriodDb.getTotalCost()));
                budgetPeiodType.setTOTALDIRECTCOST(formatToBigDesimal(budgetPeriodDb.getTotalDirectCost()));
                budgetPeiodType.setTOTALINDIRECTCOST(formatToBigDesimal(budgetPeriodDb.getTotalIndirectCost()));
                budgetPeiodType.setCOSTSHARINGAMOUNT(formatToBigDesimal(budgetPeriodDb.getCostSharingAmount()));
                budgetPeiodType.setUNDERRECOVERYAMOUNT(formatToBigDesimal(budgetPeriodDb.getUnderRecoveryAmount()));
                budgetPeiodType.setTOTALCOSTLIMIT(formatToBigDesimal(budgetPeriodDb.getTotalCostLimit()));
                budgetPeiodType.setCOMMENTS(budgetPeriodDb.getComments());
                budgetPeiodType.setTOTALDIRECTCOSTLIMIT(formatToBigDesimal(budgetPeriodDb.getTotalDirectCostLimit()));
                
                if(budgetDetailListDb != null){
                    budgetPeiodType.getBUDGETDETAIL().addAll(getBudgetDetails(budgetDetailListDb,
                            budgetPeriodDb.getBudgetPeriod()));
                }
                budgetPeiodList.add(budgetPeiodType);
            }
        }
        
        return budgetPeiodList;
    }
    
    /**
     * It will identifies all the BudgetDetails associating with a BudgetPeriod.
     * @param budgetDetailListDb List.
     * @param budPeriodNumber int.
     * @throws JAXBException.
     * @return budgetDetailList List.
     */
    
    private List getBudgetDetails(final List budgetDetailListDb,
                                  final int budPeriodNumber )throws JAXBException{
        
        final Vector budgetDetailList = new Vector();
        
        if(budgetDetailListDb != null){
            BudgetDetailBean budDetailDb = null;
            BUDGETDETAILType budDetail = null;
         
            for(Object obj : budgetDetailListDb){
                budDetailDb = (BudgetDetailBean)obj;
                
                if(budDetailDb.getBudgetPeriod() == budPeriodNumber ){
                    budDetail = budObjFact.createBUDGETDETAILType();
                    
                    budDetail.setPROPOSALNUMBER(budDetailDb.getProposalNumber());
                    budDetail.setVERSIONNUMBER(budDetailDb.getVersionNumber());
                    budDetail.setBUDGETPERIODNUMBER(budDetailDb.getBudgetPeriod());
                    budDetail.setLINEITEMNUMBER(budDetailDb.getLineItemNumber());
                    budDetail.setBUDGETCATEGORYCODE(budDetailDb.getBudgetCategoryCode());
                    budDetail.setCOSTELEMENT(budDetailDb.getCostElement());
                    budDetail.setLINEITEMDESCRIPTION(budDetailDb.getLineItemDescription());
                    budDetail.setBASEDONLINEITEM(budDetailDb.getBasedOnLineItem());
                    budDetail.setLINEITEMSEQUENCE(budDetailDb.getLineItemSequence());
                    
                    if(budDetailDb.getLineItemStartDate() != null){
                  
                        budDetail.setSTARTDATE(formatDate(budDetailDb.getLineItemStartDate()));
                    }
                    
                    if(budDetailDb.getLineItemEndDate() != null){
                    
                        budDetail.setENDDATE(formatDate(budDetailDb.getLineItemEndDate()));
                    }
                    
                    budDetail.setLINEITEMCOST(formatToBigDesimal(budDetailDb.getLineItemCost()));
                    budDetail.setCOSTSHARINGAMOUNT(formatToBigDesimal(budDetailDb.getCostSharingAmount()));
                    budDetail.setUNDERRECOVERYAMOUNT(formatToBigDesimal(budDetailDb.getUnderRecoveryAmount()));
                    
                    if(budDetailDb.isOnOffCampusFlag()){
                        budDetail.setONOFFCAMPUSFLAG("Y");
                    }else{
                        budDetail.setONOFFCAMPUSFLAG("N");
                    }
                    
                    if(budDetailDb.isApplyInRateFlag()){
                        budDetail.setAPPLYINRATEFLAG("Y");
                    }else{
                        budDetail.setAPPLYINRATEFLAG("N");
                    }
                    
                    budDetail.setBUDGETJUSTIFICATION(budDetailDb.getBudgetJustification());
                    budDetail.setQUANTITY(new Double(budDetailDb.getQuantity()).intValue());
                    //budDetail.setSUBMITCOSTSHARING(budDetailDb.getCostElementDescription());
                    
                    budgetDetailList.add(budDetail);
                }
            }
        }
        
        return budgetDetailList;
    }
    
    /**
     *   Is used to set Questionnaire objects values
     *   @throws  DBException
     *   @throws  JAXBException
     *   @throws  CoeusException
     *   @return QUESTIONNAIREType
     */
    
    private QuestionnaireType getQuestionnaireDetails(final QuestionnaireBaseBean dataBean)
                                            throws JAXBException, DBException, CoeusException{
        
        final QuestionnaireType qnr = qnrObjFact.createQuestionnaireType();
        qnr.setQUESTIONNAIREID(dataBean.getQuestionnaireId());
        qnr.setQUESTIONNAIRENAME(dataBean.getName());
        qnr.setDESCRIPTION(dataBean.getDescription());
        // todo: fill these values
        qnr.setVERSIONNUMBER(dataBean.getQuestionnaireVersionNumber());
       // qnr.setMODULEUSAGE(dataBean.get);
        if(dataBean.isFinalFlag()){
            qnr.setISFINAL("Y");
        }else{
            qnr.setISFINAL("N");
        }
        
        // For the questionnaire, read the questions.
        qnr.getQUESTION().addAll(getQuestionDetails(dataBean.getQuestionnaireId(),
                                        dataBean.getQuestionnaireVersionNumber()));
        
        return qnr;
    }
    
    /**
     * Is used to get AnswerHeader for a Questionnaire
     * @param answerHeaderListFromDb List
     * @param questionnaireId int Questionnaire id
     * @param qnrVersionNumber int Questionnaire versionNumber
     * @return ANSWERHEADERType
     * @throws JAXBException
     */
    
    private AnswerHeaderType getAnswerHeaderType(final List answerHeaderListFromDb,
                                                 final int questionnaireId,
                                                 final int qnrVersionNumber) throws JAXBException{
        
        AnswerHeaderType ansHeaderType = null;
        QuestionnaireAnswerHeaderBean ansHeaderFromDB = null;
        
        if(answerHeaderListFromDb != null){
            for(Object obj : answerHeaderListFromDb){
                ansHeaderFromDB  = (QuestionnaireAnswerHeaderBean)obj;
                
                if(ansHeaderFromDB.getQuestionnaireId() == questionnaireId &&
                        ansHeaderFromDB.getQuestionnaireVersionNumber() == qnrVersionNumber){
                    
                    ansHeaderType = qnrObjFact.createAnswerHeaderType();
                    ansHeaderType.setCOMPLETEDFLAG(ansHeaderFromDB.getQuestionnaireCompletionFlag());
                    ansHeaderType.setCOMPLETIONID(ansHeaderFromDB.getQuestionnaireCompletionId());
                    ansHeaderType.setMODULECODE(ansHeaderFromDB.getModuleItemCode());
                    ansHeaderType.setMODULEDESCRIPTION(ansHeaderFromDB.getModuleItemDescription());
                    if(ansHeaderFromDB.getModuleSubItemKey() != null){
                       ansHeaderType.setSUBMODULECODE(((Integer.parseInt(ansHeaderFromDB.getModuleSubItemKey()))));  
                    }
                   
                    ansHeaderType.setSUBMODULEDESCRIPTION(ansHeaderFromDB.getModuleSubItemDescription());
                    ansHeaderType.setMODULEKEY(ansHeaderFromDB.getModuleItemKey());
                    ansHeaderType.setSUBMODULEKEY(ansHeaderFromDB.getModuleSubItemKey());
                    //ANSWERS getter and setter is not finding;
                    ansHeaderType.setVERSIONNUMBER(new Integer(ansHeaderFromDB.getQuestionnaireVersionNumber()).toString());
                
                }
            }
        }
        
        return ansHeaderType;
    }
    
    /**
     * Is used to get the question and corresponding answers information data
     * @param questionnaireId int Questionnaire id
     * @param questionnaireVersion int qnrVersion Questionnaire version
     * @throws DBException
     * @throws CoeusException
     * @throws JAXBException
     * @return Vector
     */
    
    private List getQuestionDetails(final int questionnaireId, final int qnrVersion )
                                    throws DBException, CoeusException, JAXBException{
        
        final QuestionnaireTxnBean txnBean = new QuestionnaireTxnBean();
        final List questionDetails = new Vector();
        final CoeusVector cvQuesionData = txnBean.getQuestionnaireQuestionsData(questionnaireId, qnrVersion);
        
        if(cvQuesionData != null && cvQuesionData.size() > 0) {
            Vector cvSorted = getSortedVector(cvQuesionData);
            
            if(cvSorted != null && cvSorted.size() > 0){
                QuestionnaireBean questionnaireBean = null;
                QuestionType question = null;
                
                List questionnaireAnswers = txnBean.getQuestionnaireAnswers(proposalNumber, questionnaireId,
                        ModuleConstants.PROPOSAL_DEV_MODULE_CODE);
                
                for(int index = 0; index < cvSorted.size(); index++) {
                    questionnaireBean = (QuestionnaireBean) cvSorted.get(index);
                    question = qnrObjFact.createQuestionType();
                    
                    question.setQUESTIONID(questionnaireBean.getQuestionId().intValue());
                    question.setQUESTIONNUMBER(questionnaireBean.getQuestionNumber().intValue());
                    //question.setQUESTION(uestionnaireBean);
                    //question.setANSWERDATATYPE();
                    //question.setANSWERMAXLENGTH();
                    question.setPARENTQUESTIONNUMBER(questionnaireBean.getParentQuestionNumber());
                    question.setQUESTIONSEQUENCENUMBER(questionnaireBean.getQuestionSequenceNumber());
                    
                    if(questionnaireBean.isConditionalFlag()){
                        question.setCONDITIONFLAG("Y");
                    }else{
                        question.setCONDITIONFLAG("N");
                    }
                    question.setCONDITION(questionnaireBean.getCondition());
                    question.setCONDITIONVALUE(questionnaireBean.getConditionValue());
                    //todo: in schema answer header is there but corrousponding setter is not finding
                    //Setting all answers for a question
                    question.getANSWER().addAll(getQuestionAnswers(questionnaireAnswers,
                            questionnaireBean.getQuestionNumber()));
                     questionDetails.add(question);   
                }                
            
            }
        }
        
        return questionDetails;
    }
    
    /**
     * Is used to get QuestionAnswers for a question.
     * @param List ansListFromDb
     * @param int questionNumber
     * @throws JAXBException
     * @return List ansList
     */
    
    private List getQuestionAnswers(final List ansListFromDb, final int questionNumber)
    throws JAXBException{
        
        final Vector ansList = new Vector();
        AnswerType answer = null;
        QuestionAnswerBean answerFromDb  = null;
        
        if(ansListFromDb != null){
            for(Object object : ansListFromDb){
                answerFromDb = (QuestionAnswerBean)object;
                //Selecting question specific answers from the available answers.
                if(answerFromDb.getQuestionNumber() == questionNumber){
                    answer = qnrObjFact.createAnswerType();
                    
                    answer.setANSWER(answerFromDb.getAnswer());
                    answer.setANSWERNUMBER(answerFromDb.getAnswerNumber());
                    answer.setQUESTIONID(answerFromDb.getQuestionId());
                    answer.setQUESTIONNUMBER(answerFromDb.getQuestionNumber());
                    
                    ansList.add(answer);
                }
            }
        }
        
        return ansList;
    }
    
    /**
     * Is used to sort
     * @param vector consists of details like QuestionnaireId,ParentQuestionNumber..
     */
    private Vector getSortedVector(
            Vector vector) {
        return sort(vector);
    }
    
    /**
     * Is used to sort
     * @param vector consists of details like QuestionnaireId,ParentQuestionNumber..
     */
    private Vector sort(Vector vector) {
        Vector temp = vector;
        Vector elements = new Vector();
        QuestionnaireBean questionaireBean = null;
        
        for (int i = 0; i < temp.size(); i++) {
            questionaireBean = (QuestionnaireBean)vector.get(i);
            if ( !elements.contains(questionaireBean) ) {
                //calls findChildrenAndUpdate method recursively in order to find children
                findChildrenAndUpdate(elements, temp, questionaireBean);
                i--;
            }
        }
        return elements;
    }
    /**
     * Is used to find Children
     * @param vector ,elements, parent
     * once parent is added in elemnts, it is removed from vector,so that it wont check for same QuestionId
     *
     */
    private void findChildrenAndUpdate(Vector elements, Vector vector,
                                       QuestionnaireBean parent) {
        elements.add(parent);
        vector.remove(parent);
        // FInd the children for parent
        List children = findChildren(vector, parent
                .getQuestionNumber());
        if(children != null && children.size() > 0){
            for(int index=0;index<children.size();index++){
                QuestionnaireBean childBean = (QuestionnaireBean)children.get(index);
                findChildrenAndUpdate(elements, vector, childBean);
            }
        }
    }
    /**
     * Is used to find sequence number for the childrens to display in order
     * @param vector , parent
     * once parent is added in elemnts, it is removed from vector,so that it wont check for same QuestionId
     *
     */
    private List findChildren(Vector vector, Integer parent) {
        List list = new ArrayList();
        if(vector != null && vector.size() > 0){
            for(int index=0;index<vector.size();index++){
                QuestionnaireBean bean = (QuestionnaireBean)vector.get(index);
                if(bean.getParentQuestionNumber() != null && parent != null){
                    if (bean.getParentQuestionNumber().intValue() == parent.intValue()) {
                        list.add(bean);
                    }
                }
            }
        }
        
        QuestionnaireBean firstChild = null;
        QuestionnaireBean secondChild = null;
        QuestionnaireBean temp = null;
        // To the the questions according to the questionSequenceNumber
        for (int i = 0; i < list.size(); i++) {
            for (int j = 1; j <= (list.size() - 1); j++) {
                firstChild = (QuestionnaireBean)list.get(j - 1);
                secondChild = (QuestionnaireBean)list.get(j);
                if (secondChild.getQuestionSequenceNumber() < firstChild
                        .getQuestionSequenceNumber() ) {
                    temp = firstChild;
                    list.set((j - 1), secondChild);
                    list.set(j, temp);
                }
            }
        }
        return list;
    }
    
    //Added for COEUSQA-1682_FS_Proposal Development Schema Infrastructure end.
    
    private List getSplReviewers() throws Exception{
        
        Vector splRevList = txnBean.getProSubDetList(proposalNumber, "GET_SPECIAL_REVIEW_DETAILS");
        Vector splRev = new Vector();
        
        for(int i=0;i<splRevList.size();i++){
            HashMap splReviewRow = (HashMap)splRevList.elementAt(i);
            PROPSPECIALREVIEWType reviewType = propObjFact.createPROPSPECIALREVIEWType();
            APPLICABLEREVIEWTYPEType artt = lkObjFact.createAPPLICABLEREVIEWTYPEType();
            artt.setAPPLICABLEREVIEWTYPECODE(dummyCode);
            int approvalTypeCode = 0;
            if(splReviewRow.get("APPROVAL_TYPE_CODE") != null){
                    approvalTypeCode =  Integer.parseInt(splReviewRow.get("APPROVAL_TYPE_CODE").toString());
            }
            // Modified for COEUSQA-3119  - Need to implement IACUC link to Award, IP, Prop Dev, and IRB - Start
            int splRevTypeCode = 0;
            if(splReviewRow.get("SPECIAL_REVIEW_CODE") != null){
                splRevTypeCode =  Integer.parseInt(splReviewRow.get("SPECIAL_REVIEW_CODE").toString());
            }
            // When there is protocolstatuscode exists , the description is printed else the actual approval status is printed
            // Checks the special review approval type code is equals SPL_REV_TYPE_CODE_HUMAN, then gets the protocol details from the IRB module
            // Checks the special review approval type code is equals IACUC_SPL_REV_TYPE_CODE, then gets the protocol details from the IACUC module
            CoeusFunctions coeusFunctions = new CoeusFunctions();
            String irbSplRevTypeCode =  coeusFunctions.getParameterValue("SPL_REV_TYPE_CODE_HUMAN");
            String iacucSplRevTypeCode = coeusFunctions.getParameterValue("IACUC_SPL_REV_TYPE_CODE");
            int irbSpecialRevTypeCode = 0;
            int iacucSpecialRevTypeCode = 0;   
            if(irbSplRevTypeCode != null && irbSplRevTypeCode.length() > 0 ){
                 irbSpecialRevTypeCode = Integer.parseInt(irbSplRevTypeCode);
            }
            if(iacucSplRevTypeCode != null && iacucSplRevTypeCode.length() > 0 ){
                iacucSpecialRevTypeCode = Integer.parseInt(iacucSplRevTypeCode);
            }
            if(splReviewRow.get("PROTOCOL_STATUS_CODE") != null && !splReviewRow.get("PROTOCOL_STATUS_CODE").equals("")){
                if(irbSplRevTypeCode != null && irbSplRevTypeCode.length() > 0 ){
                    if(splRevTypeCode != 0 && splRevTypeCode == irbSpecialRevTypeCode){
                        String protocolStatusDesc = "";
                        edu.mit.coeus.irb.bean.ProtocolDataTxnBean protocolDetails = new edu.mit.coeus.irb.bean.ProtocolDataTxnBean();
                        Vector vecProtocolStatus = protocolDetails.getProtocolStatus();
                        if(vecProtocolStatus != null && !vecProtocolStatus.isEmpty()){
                            for(Object protocolStatus : vecProtocolStatus){
                                ComboBoxBean comboxBean =  (ComboBoxBean)protocolStatus;
                                if(comboxBean.getCode().equals(splReviewRow.get("PROTOCOL_STATUS_CODE").toString())){
                                    protocolStatusDesc = comboxBean.getDescription();
                                    break;
                                }
                            }
                        }
                        artt.setDESCRIPTION(protocolStatusDesc);
                        String protocolNumber = (String)splReviewRow.get("PROTOCOL_NUMBER");
                        edu.mit.coeus.irb.bean.ProtocolInfoBean protocolInfoBean =  protocolDetails.getProtocolMaintenanceDetails(protocolNumber);
                        artt.setDESCRIPTION(protocolInfoBean.getProtocolStatusDesc());
                        reviewType.setAPPLICATIONDATE(
                                formatDate(protocolInfoBean.getApplicationDate() == null ? null : protocolInfoBean.getApplicationDate()));
                        reviewType.setAPPROVALDATE(
                                formatDate(protocolInfoBean.getApprovalDate() == null ? null : protocolInfoBean.getApprovalDate()));
                    }else if(iacucSplRevTypeCode != null && iacucSplRevTypeCode.length() > 0 ){
                        
                        if(splRevTypeCode != 0 && splRevTypeCode == iacucSpecialRevTypeCode){
                            String protocolStatusDesc = "";
                            edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean protocolDetails = new edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean();
                            Vector vecProtocolStatus = protocolDetails.getProtocolStatus();
                            if(vecProtocolStatus != null && !vecProtocolStatus.isEmpty()){
                                for(Object protocolStatus : vecProtocolStatus){
                                    ComboBoxBean comboxBean =  (ComboBoxBean)protocolStatus;
                                    if(comboxBean.getCode().equals(splReviewRow.get("PROTOCOL_STATUS_CODE").toString())){
                                        protocolStatusDesc = comboxBean.getDescription();
                                        break;
                                    }
                                }
                            }
                            artt.setDESCRIPTION(protocolStatusDesc);
                            String protocolNumber = (String)splReviewRow.get("PROTOCOL_NUMBER");
                            edu.mit.coeus.iacuc.bean.ProtocolInfoBean protocolInfoBean =  protocolDetails.getProtocolMaintenanceDetails(protocolNumber);
                            artt.setDESCRIPTION(protocolInfoBean.getProtocolStatusDesc());
                            reviewType.setAPPLICATIONDATE(
                                    formatDate(protocolInfoBean.getApplicationDate() == null ? null : protocolInfoBean.getApplicationDate()));
                            reviewType.setAPPROVALDATE(
                                    formatDate(protocolInfoBean.getApprovalDate() == null ? null : protocolInfoBean.getApprovalDate()));
                        }
                    }
                }
            }else if(approvalTypeCode == LINK_TO_IRB_APPROVAL_TYPE_CODE || splRevTypeCode == irbSpecialRevTypeCode){
                String protocolNumber = (String)splReviewRow.get("PROTOCOL_NUMBER");
                
                edu.mit.coeus.irb.bean.ProtocolDataTxnBean protocolDetails = new edu.mit.coeus.irb.bean.ProtocolDataTxnBean();
                edu.mit.coeus.irb.bean.ProtocolInfoBean protocolInfoBean =  protocolDetails.getProtocolMaintenanceDetails(protocolNumber);
                if(protocolInfoBean != null){
                    artt.setDESCRIPTION(protocolInfoBean.getProtocolStatusDesc());
                    reviewType.setAPPLICATIONDATE(
                            formatDate(protocolInfoBean.getApplicationDate() == null ? null : protocolInfoBean.getApplicationDate()));
                    reviewType.setAPPROVALDATE(
                            formatDate(protocolInfoBean.getApprovalDate() == null ? null : protocolInfoBean.getApprovalDate()));
                }else{
                    artt.setDESCRIPTION((String)splReviewRow.get("APPROVAL_STATUS"));
                }
            }else if(approvalTypeCode == LINK_TO_IACUC_APPROVAL_TYPE_CODE || splRevTypeCode == iacucSpecialRevTypeCode){
                String protocolNumber = (String)splReviewRow.get("PROTOCOL_NUMBER");
                edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean protocolDetails = new edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean();
                edu.mit.coeus.iacuc.bean.ProtocolInfoBean protocolInfoBean =  protocolDetails.getProtocolMaintenanceDetails(protocolNumber);
                if(protocolInfoBean != null){
                    artt.setDESCRIPTION(protocolInfoBean.getProtocolStatusDesc());
                    reviewType.setAPPLICATIONDATE(
                            formatDate(protocolInfoBean.getApplicationDate() == null ? null : protocolInfoBean.getApplicationDate()));
                    reviewType.setAPPROVALDATE(
                            formatDate(protocolInfoBean.getApprovalDate() == null ? null : protocolInfoBean.getApprovalDate()));
                }else{
                    artt.setDESCRIPTION((String)splReviewRow.get("APPROVAL_STATUS"));
                }

            }else{
                artt.setDESCRIPTION((String)splReviewRow.get("APPROVAL_STATUS"));
                reviewType.setAPPLICATIONDATE(
                        formatDate(splReviewRow.get("APPLICATION_DATE") == null ? null : new Date(((Timestamp)splReviewRow.get("APPLICATION_DATE")).getTime())));
                reviewType.setAPPROVALDATE(
                        formatDate(splReviewRow.get("APPROVAL_DATE") == null ? null : new Date(((Timestamp)splReviewRow.get("APPROVAL_DATE")).getTime())));
                
            }
            // Modified for COEUSQA-3119  - Need to implement IACUC link to Award, IP, Prop Dev, and IRB - End
            reviewType.setAPPLICABLEREVIEWTYPE(artt);
            SPECIALREVIEWType srat = lkObjFact.createSPECIALREVIEWType();
            srat.setSPECIALREVIEWCODE(dummyCode);
            srat.setDESCRIPTION((String)splReviewRow.get("SPECIAL_REVIEW"));
            //todo: set all the setters in reviewType.
            reviewType.setSPECIALREVIEW(srat);
            reviewType.setPROTOCOLNUMBER((String)splReviewRow.get("PROTOCOL_NUMBER"));
            reviewType.setPROPOSALNUMBER((String)splReviewRow.get("PROPOSAL_NUMBER"));
            reviewType.setCOMMENTS((String)splReviewRow.get("COMMENTS"));
            
            splRev.add(reviewType);
        }
        
        return splRev;
    }
  
    //Added for COEUSQA-1682_FS_Proposal Development Schema Infrastructure start.
    private List getPropInvestigators() throws CoeusException, DBException, JAXBException{
        
        List propInvestigListDb = medusaTxnBean.getProposalInvestigatorsForMedusa(proposalNumber);
        Vector propInvestigList = new Vector();
        
        if(propInvestigListDb != null){
            ProposalInvestigatorFormBean propInvBeanDb = null;
            
            PROPINVESTIGATORS propInvtype = null;        
            for(Object obj : propInvestigListDb){
                propInvBeanDb = (ProposalInvestigatorFormBean)obj;
                
                propInvtype = propObjFact.createPROPINVESTIGATORS();
                //set all data members
                propInvestigList.add(propInvtype);
            }
        }
        
        return propInvestigList;
    }
    
        private List getPropUnitCreditSplit(String personId) throws CoeusException, DBException, JAXBException{
        List creditListDb = propDevTxnBean.getProposalLeadUnitDetails(proposalNumber, personId);
        Vector  creditSplitList  = new Vector();
        
        if(creditListDb != null){
            ProposalLeadUnitFormBean leadUnitDb = null;
            PROPUNITCREDITSPLITType unitCredit = null;
            for(Object obj : creditListDb){
               leadUnitDb = (ProposalLeadUnitFormBean)obj;
               unitCredit = propObjFact.createPROPUNITCREDITSPLITType();
               unitCredit.setPROPOSALNUMBER(leadUnitDb.getProposalNumber());
               unitCredit.setPERSONID(leadUnitDb.getPersonId());
              
               creditSplitList.add(unitCredit);
            }
        }
        return creditSplitList;
    }
  
    //Added for COEUSQA-1682_FS_Proposal Development Schema Infrastructure end.
    
        
    //Modified for COEUSQA-1682_FS_Proposal Development Schema Infrastructure begin.
     private List getInvestigators() throws Exception{
        
        Vector invList = txnBean.getProSubDetList(proposalNumber, "GET_INVESTIGATOR_DETAILS");
        Vector invCreditList = txnBean.getProSubDetList(proposalNumber, "GET_INV_CREDIT_DETAILS");
        Vector invs = new Vector();
        
        for(int i=0;i<invList.size();i++){
            HashMap invRow = (HashMap)invList.elementAt(i);
            String personId = (String)invRow.get("PERSON_ID");

            PROPINVESTIGATORS invType = propObjFact.createPROPINVESTIGATORS();
            invType.setPERSONNAME((String)invRow.get("PERSON_NAME_AND_INFO"));
            //todo: set all the setters in PROPINVESTIGATORSType.
            //Check invRow containing this key
            invType.setMULTIPIFLAG((String)invRow.get("MULTI_PI_FLAG"));
            
            if(invRow.get("ACADEMIC_YEAR_EFFORT") != null){
                invType.setACADEMICYEAREFFORT(formatToBigDesimal((Double)invRow.get("ACADEMIC_YEAR_EFFORT")));
            }
            if(invRow.get("SUMMER_YEAR_EFFORT") != null){
                invType.setSUMMERYEAREFFORT(formatToBigDesimal((Double)invRow.get("SUMMER_YEAR_EFFORT")));
            }

            if(invRow.get("CALENDAR_YEAR_EFFORT")!= null){
                invType.setCALENDARYEAREFFORT(formatToBigDesimal((Double)invRow.get("CALENDAR_YEAR_EFFORT")));

            }
            invType.setPRINCIPALINVESTIGATORFLAG((String)invRow.get("PI_FLAG"));
            PROPPERCREDITSPLITType invCredit = propObjFact.createPROPPERCREDITSPLITType();
            invCredit.setCREDIT(invRow.get("CREDIT")==null?"":invRow.get("CREDIT").toString());
            invType.setPROPPERCREDITSPLIT(invCredit);
            List unitCreitList = invCredit.getPROPUNITCREDITSPLIT();
            for(int j=0;j<invCreditList.size();j++){
                HashMap invCreditRow = (HashMap)invCreditList.elementAt(j);
                String perId = (String)invCreditRow.get("PERSON_ID");
                if(perId.equals(personId)){
                    PROPUNITCREDITSPLITType unitCredit = propObjFact.createPROPUNITCREDITSPLITType();
                    unitCredit.setUNIT((String)invCreditRow.get("UNIT"));
                    unitCredit.setPRIMARY((String)invCreditRow.get("PRIMARY"));
                    unitCredit.setSECONDARY((String)invCreditRow.get("CENTER_INSTITUTE"));
                    unitCredit.setPERSONID((String)invCreditRow.get("PERSON_ID"));
                    unitCreitList.add(unitCredit);
                    //todo: set all the setters in unitCredit.
                }
            }

            invs.add(invType);
        }
        
        return invs;
    }
    
  
    /**
     * Method to get all the investigators with basic details in osp$eps_prop_investigators table
     * @throws java.lang.Exception 
     * @return invs
     */
     private List getAllInvestigatorsBasicDetails() throws Exception{
         Vector invList = txnBean.getProSubDetList(proposalNumber, "GET_INV_BASIC_DETAILS");

         Vector invs = new Vector();
         HashMap hmPropUnits = getPropUnits();
         for(int i=0;i<invList.size();i++){
             HashMap invRow = (HashMap)invList.elementAt(i);
             String personId = (String)invRow.get("PERSON_ID");
             PROPINVESTIGATORSBASICDETAILS invType = propObjFact.createPROPINVESTIGATORSBASICDETAILS();
             Vector vecPersonUnits = (Vector)hmPropUnits.get(personId);
             invType.getPROPUNITS().addAll(vecPersonUnits);
             if(invRow.get("ACADEMIC_YEAR_EFFORT") != null){
                 invType.setACADEMICYEAREFFORT((BigDecimal)invRow.get("ACADEMIC_YEAR_EFFORT"));
             }
             if(invRow.get("CALENDAR_YEAR_EFFORT")!= null){
                 invType.setCALENDARYEAREFFORT((BigDecimal)invRow.get("CALENDAR_YEAR_EFFORT"));
             }
             invType.setCONFLICTOFINTERESTFLAG((String)invRow.get("CONFLICT_OF_INTEREST_FLAG"));
             invType.setFACULTYFLAG((String)invRow.get("FACULTY_FLAG"));
             invType.setFEDRDEBRFLAG((String)invRow.get("FEDR_DEBR_FLAG"));
             invType.setFEDRDELQFLAG((String)invRow.get("FEDR_DELQ_FLAG"));
             invType.setMULTIPIFLAG((String)invRow.get("MULTI_PI_FLAG"));
             invType.setNONMITPERSONFLAG((String)invRow.get("NON_MIT_PERSON_FLAG"));
             if(invRow.get("PERCENTAGE_EFFORT") != null){
                 invType.setPERCENTAGEEFFORT((BigDecimal)invRow.get("PERCENTAGE_EFFORT"));
             }
             invType.setPERSONID(personId);
             invType.setPERSONNAME((String)invRow.get("PERSON_NAME"));
             invType.setPRINCIPALINVESTIGATORFLAG((String)invRow.get("PRINCIPAL_INVESTIGATOR_FLAG"));
             invType.setPROPOSALNUMBER((String)invRow.get("PROPOSAL_NUMBER"));
             if(invRow.get("SUMMER_YEAR_EFFORT") != null){
                 invType.setSUMMERYEAREFFORT((BigDecimal)invRow.get("SUMMER_YEAR_EFFORT"));
                 
             }
             invs.add(invType);
         }
         
         return invs;
     }

     
     
    /*
     *
     *OSP$EPS_PROP_UNITS.PROPOSAL_NUMBER,
         OSP$EPS_PROP_UNITS.PERSON_ID,
         OSP$EPS_PROP_UNITS.UNIT_NUMBER,
         OSP$EPS_PROP_UNITS.LEAD_UNIT_FLAG,
                        OSP$UNIT.UNIT_NAME
     */
    private HashMap getPropUnits() throws Exception{
        
        Vector units = txnBean.getProSubDetList(proposalNumber, "get_prop_units");
        
        HashMap hmPropUnits = new HashMap();
        for(int i=0;i<units.size();i++){
            HashMap invRow = (HashMap)units.elementAt(i);
            PROPUNITSType unit = propObjFact.createPROPUNITSType();
            String personId = invRow.get("PERSON_ID").toString();
            unit.setLEADUNITFLAG((String)invRow.get("LEAD_UNIT_FLAG"));
            unit.setPROPOSALNUMBER((String)invRow.get("PROPOSAL_NUMBER"));
            unit.setUNIT(getUnit((String)invRow.get("UNIT_NUMBER")));
            if(hmPropUnits.get(personId) != null){
                Vector vecAddedUnit = (Vector)hmPropUnits.get(personId);
                vecAddedUnit.add(unit);
            }else{
                Vector vecUnits = new Vector();
                vecUnits.add(unit);
                hmPropUnits.put(personId,vecUnits);
            }
            
            
        }
        
        return hmPropUnits;
    }
    
    
    //Modified for COEUSQA-1682_FS_Proposal Development Schema Infrastructure end.
    
        /*
         *
         *OSP$EPS_PROP_UNITS.PROPOSAL_NUMBER,
         OSP$EPS_PROP_UNITS.PERSON_ID,
         OSP$EPS_PROP_UNITS.UNIT_NUMBER,
         OSP$EPS_PROP_UNITS.LEAD_UNIT_FLAG,
                        OSP$UNIT.UNIT_NAME
         */
    private List getAllPropUnits() throws Exception{
        
        Vector units = txnBean.getProSubDetList(proposalNumber, "get_prop_units");
        Vector unitList = new Vector();
        for(int i=0;i<units.size();i++){
            HashMap invRow = (HashMap)units.elementAt(i);
            //String personId = (String)invRow.get("PERSON_ID");
            PROPUNITSType unit = propObjFact.createPROPUNITSType();
            unit.setLEADUNITFLAG((String)invRow.get("LEAD_UNIT_FLAG"));
            unit.setPROPOSALNUMBER((String)invRow.get("PROPOSAL_NUMBER"));
            
            unit.setUNIT(getUnit((String)invRow.get("UNIT_NUMBER")));
            unitList.add(unit);
        }
        
        return unitList;
    }

    
    //Added for COEUSQA-1682_FS_Proposal Development Schema Infrastructure start.
    /**
     *  Return UNITType type object. Identifies UnitDetailFormBean type object using unitNumber
     *  and creates the UNITType type object form this data. Returns the UNITType object
     *  @throws JAXBException.
     *  @throws CoeusException.
     *  @throws DBException.
     *  @return unitType UNITType;
     */
    
    
    private UNITType getUnit(String unitNumber) 
                            throws JAXBException, CoeusException, DBException{
        
        UNITType unitType = usrObjFact.createUNITType();
        UnitDetailFormBean unitDetail = unitDataTxnBean.getUnitDetails(unitNumber);
        
        List unitHierListDb = unitDataTxnBean.getUnitHierarchyDetails();
        
        unitType.setUNITNUMBER(unitDetail.getUnitNumber());
        unitType.setUNITNAME(unitDetail.getUnitName());
        unitType.setADMINISTRATIVEOFFICER(unitDetail.getAdminOfficerName());
        unitType.setOSPADMINISTRATOR(unitDetail.getOspAdminName());
        unitType.setUNITHEAD(unitDetail.getUnitHeadName());
        unitType.setDEANVP(unitDetail.getDeanVpName());
        unitType.setOTHERINDIVIDUALTONOTIFY(unitDetail.getOtherIndToNotifyName());
       
        if(unitType.getUPDATETIMESTAMP() != null){
            unitType.setUPDATETIMESTAMP(formatDate(unitType.getUPDATETIMESTAMP().getTime()));
        }
        
        unitType.setUPDATEUSER(unitType.getUPDATEUSER());
        
        unitType.getUNITADMINISTRATORS().addAll(getUnitAdministrators(unitDetail.getUnitNumber()));
        unitType.getUNITHIERARCHY().addAll(getUnitHierarchy(unitHierListDb,unitDetail.getUnitNumber()));
        //todo: implement UNIT_CORRESPONDENTS
        return unitType;
    }
    
    
    /**
     *  Returns the list of UNITADMINISTRATORSType type objects. Identifies all the UnitAdministratorBean type
     *  objects using unitNumber which is passed to this method. Creates UNITADMINISTRATORSType type object form
     *  this data. Returns the list of UNITADMINISTRATORSType objects.
     *  @param unitNumber String.
     *  @throws JAXBException.
     *  @throws CoeusException.
     *  @throws DBException.
     *  @return unitAdminsList List.
     */
    
    private List getUnitAdministrators(final String unitNumber)throws JAXBException, CoeusException, DBException{
        
        final Vector unitAdminsListDb = unitDataTxnBean.getAdminData(unitNumber);
        final Vector unitAdminsList = new Vector();
        
        if(unitAdminsListDb != null){
            UNITADMINISTRATORSType unitAdmin = null;
            UnitAdministratorBean unitAdminBeanDb = null;
            
            for(Object obj : unitAdminsListDb){
                
                unitAdminBeanDb = (UnitAdministratorBean)obj;
                unitAdmin = usrObjFact.createUNITADMINISTRATORSType();
                
                unitAdmin.setUNITNUMBER(unitAdminBeanDb.getUnitNumber());
                unitAdmin.setUNITADMINISTRATORTYPECODE(formatToBigDesimal(unitAdminBeanDb.getUnitAdminTypeCode()));
                unitAdmin.setADMINISTRATOR(unitAdminBeanDb.getAdministrator());
              
                if(unitAdminBeanDb.getUpdateTimestamp() != null){
                    unitAdmin.setUPDATETIMESTAMP(formatDate(unitAdminBeanDb.getUpdateTimestamp()));
                }
                unitAdmin.setUPDATEUSER(unitAdminBeanDb.getUpdateUser());
                unitAdminsList.add(unitAdmin);
            }
        }
        
        return unitAdminsList;
    }
    
    /**
     *  Returns the list of UNITHIERARCHYType type objects. Search UnitHierarchyFormBean in the list of object using
     *  unit number which is passed to the method. Creates UNITHIERARCHYType type object from this data. 
     *  Returns the list of UNITHIERARCHYType type objects.
     *  @param unitHierListDb List.
     *  @param unitNumber String.
     *  @throws CoeusException.
     *  @throws DBException.
     *  @throws JAXBException.
     *  @return unitHierarchyList List.
     */
  
    private List getUnitHierarchy(final List unitHierListDb, final String unitNumber) 
                                  throws CoeusException, DBException, JAXBException{
        
        //UnitHierarchyFormBean unitHierFormBeanDb= unitDataTxnBean.getHierarchyNode(unitNumber); // todo: perform requierd db operations if necessary.
        final Vector unitHierarchyList = new Vector();     
        
        if(unitHierListDb != null){
            
            UNITHIERARCHYType unitHierFormBean = null;
            UnitHierarchyFormBean unitHierFormBeanDb = null;
            
            for(Object obj : unitHierListDb){
               
                unitHierFormBeanDb = (UnitHierarchyFormBean)obj;
                if(unitNumber != null && unitNumber.equals(unitHierFormBeanDb.getNodeID())){
                    
                    unitHierFormBean = usrObjFact.createUNITHIERARCHYType();
                    unitHierFormBean.setUNITNUMBER(unitHierFormBeanDb.getNodeID());
                    unitHierFormBean.setPARENTUNITNUMBER(unitHierFormBeanDb.getParentNodeID());

                    if(unitHierFormBeanDb.hasChildren()){
                         unitHierFormBean.setHASCHILDRENFLAG("Y");
                    }else{
                        unitHierFormBean.setHASCHILDRENFLAG("N");
                    }

                    if(unitHierFormBeanDb.getUpdateTimestamp() != null){
                         unitHierFormBean.setUPDATETIMESTAMP(
                                formatDate(unitHierFormBeanDb.getUpdateTimestamp()));
                    }

                    unitHierFormBean.setUPDATEUSER(unitHierFormBeanDb.getUpdateUser());
                    unitHierarchyList.add(unitHierFormBean);
                    }
                
            }
            
        }
        
        return unitHierarchyList;
    }
    
    
    private List getUnitCorrespondace(String unitNumber, List unitCorrListDb){
        
        Vector unitCorrList = new Vector();
        
        //GET_UNIT_CORRESP_FOR_UNIT_LITE  ( IF NOT) � THEN GET_UNIT_CORRESPONDENTS

        return unitCorrList;
    }
    
    /**
     *  Retuns the list of PROPKEYPERSONS type objects.
     *  Identifies all ProposalKeyPersonFormBean, ProposalPersonFormBean and ProposalCertificationFormBean
     *  objects for a proposal number. Creates PROPKEYPERSONS type object form this data and by calling the
     *  method getProposalPersonDetails which will returns the PROPPERSON object.
     *  @throws DBException.
     *  @throws CoeusException.
     *  @throws JAXBException.
     *  @return keyPersonsList List.
     */
    
    private List getPropKeyPersons() throws DBException, CoeusException, JAXBException{
        
        final Vector keyPersonsListDb = propDevTxnBean.getProposalKeyPersonDetails(proposalNumber);
        final Vector personDetailsListDb = propPerTxnbean.getAllPersonDetails(proposalNumber);
        final Vector ynqListDb = propDevTxnBean.getProposalCertifyDetails(proposalNumber);
        
        final Vector keyPersonsList = new Vector();
        
        if(keyPersonsListDb != null){
            
            PROPKEYPERSONS propKeyPerson = null;
            ProposalKeyPersonFormBean propKeyPersonDb= null;
       
            for(Object obj : keyPersonsListDb){
                
                propKeyPersonDb = (ProposalKeyPersonFormBean)obj;
                propKeyPerson = propObjFact.createPROPKEYPERSONS();
                
                propKeyPerson.setPROPOSALNUMBER(propKeyPersonDb.getProposalNumber());
                propKeyPerson.setPERSONNAME(propKeyPersonDb.getPersonName());
                propKeyPerson.setPROJECTROLE(propKeyPersonDb.getProjectRole());
                
                if(propKeyPersonDb.isFacultyFlag()){
                    propKeyPerson.setFACULTYFLAG("Y");
                }else{
                    propKeyPerson.setFACULTYFLAG("N");
                }
                
                if(propKeyPersonDb.isNonMITPersonFlag()){
                    propKeyPerson.setNONMITPERSONFLAG("Y");
                }else{
                    propKeyPerson.setNONMITPERSONFLAG("N");
                }
                
                propKeyPerson.setPERCENTAGEEFFORT(formatToBigDesimal(propKeyPersonDb.getPercentageEffort()));
                
                if(propKeyPersonDb.getUpdateTimestamp() != null){
                    propKeyPerson.setUPDATETIMESTAMP(formatDate(propKeyPersonDb.getUpdateTimestamp()));
                }
                
                propKeyPerson.setUPDATEUSER(propKeyPersonDb.getUpdateUser());
                
                //todo: Ask about this datamember.
                propKeyPerson.setPROPPERSON(getProposalPersonDetails(personDetailsListDb, ynqListDb, propKeyPersonDb.getPersonId()));
                keyPersonsList.add(propKeyPerson);
            }
            
        }
        
        return keyPersonsList;
    }
    
    
    /**
     *  Returns the PROPPERSON type object. Search a ProposalPersonFormBean object in the list
     *  of ProposalPersonFormBean using personId which are passed to the method. Creates PROPPERSON
     *  type object form this data and my calling the methods getPersonalBioSources,gePersonalYNQs,
     *  getPersonlDegrees and getPersonalCustomDatas which will returns the required subclass data.
     *  @throws CoeusException.
     *  @throws DBException.
     *  @throws JAXBException.
     *
     *  @return propPerson PROPPERSON.
     */
    private PROPPERSON getProposalPersonDetails(final List personDetailsListDb,
                                                final List ynqListDb, final String personId)
                                          throws CoeusException, DBException,JAXBException{
        
        ProposalPersonFormBean propPersonDb = null;
        PROPPERSON propPerson = null;
        
        if(personDetailsListDb != null){
     
            for(Object obj : personDetailsListDb){
                propPersonDb = (ProposalPersonFormBean)obj;
                
                if(personId != null && personId.equals(propPersonDb.getPersonId())){
                    
                    propPerson = propObjFact.createPROPPERSON();
                    propPerson.setPROPOSALNUMBER(propPersonDb.getProposalNumber());
                    propPerson.setPERSONID(propPersonDb.getPersonId());
                    propPerson.setSSN(propPersonDb.getSsn());
                    propPerson.setLASTNAME(propPersonDb.getLastName());
                    propPerson.setFIRSTNAME(propPersonDb.getFirstName());
                    propPerson.setMIDDLENAME(propPersonDb.getMiddleName());
                    propPerson.setFULLNAME(propPersonDb.getFullName());
                    propPerson.setPRIORNAME(propPersonDb.getPriorName());
                    propPerson.setUSERNAME(propPersonDb.getUserName());
                    propPerson.setEMAILADDRESS(propPersonDb.getEmailAddress());
                    
                    if(propPersonDb.getDateOfBirth() != null){
                        propPerson.setDATEOFBIRTH(formatDate(propPersonDb.getDateOfBirth()));
                    }
                    
                    propPerson.setAGE(formatToBigDesimal(propPersonDb.getAge()));
                    propPerson.setAGEBYFISCALYEAR(formatToBigDesimal(propPersonDb.getAgeByFiscalYear()));
                    propPerson.setGENDER(propPersonDb.getGender());
                    propPerson.setRACE(propPersonDb.getRace());
                    propPerson.setEDUCATIONLEVEL(propPersonDb.getEduLevel());
                    propPerson.setDEGREE(propPersonDb.getDegree());
                    propPerson.setMAJOR(propPersonDb.getMajor());
                    if(propPersonDb.getHandicap()){
                        propPerson.setISHANDICAPPED("Y");
                    }else{
                        propPerson.setISHANDICAPPED("N");
                    }
                    
                    propPerson.setHANDICAPTYPE(propPersonDb.getHandiCapType());
                    if(propPersonDb.getVeteran()){
                        propPerson.setISVETERAN("Y");
                    }else{
                        propPerson.setISVETERAN("N");
                    }
                    propPerson.setVETERANTYPE(propPersonDb.getVeteranType());
                    propPerson.setVISACODE(propPersonDb.getVisaCode());
                    propPerson.setVISATYPE(propPersonDb.getVisaType());
                    
                    if(propPersonDb.getVisaRenDate()!= null){
                        propPerson.setVISARENEWALDATE(formatDate(propPersonDb.getVisaRenDate()));
                    }
                    if(propPersonDb.getHasVisa()){
                        propPerson.setHASVISA("Y");
                    }else{
                        propPerson.setHASVISA("N");
                    }
                    propPerson.setOFFICELOCATION(propPersonDb.getOfficeLocation());
                    propPerson.setOFFICEPHONE(propPersonDb.getOfficePhone());
                    propPerson.setSECONDRYOFFICELOCATION(propPersonDb.getSecOfficeLocation());
                    propPerson.setSECONDRYOFFICEPHONE(propPersonDb.getSecOfficePhone());
                    propPerson.setSCHOOL(propPersonDb.getSchool());
                    propPerson.setYEARGRADUATED(propPersonDb.getYearGraduated());
                    propPerson.setDIRECTORYDEPARTMENT(propPersonDb.getDirDept());
                    propPerson.setSALUTATION(propPersonDb.getSaltuation());
                    propPerson.setCOUNTRYOFCITIZENSHIP(propPersonDb.getCountryCitizenship());
                    propPerson.setPRIMARYTITLE(propPersonDb.getPrimaryTitle());
                    propPerson.setDIRECTORYTITLE(propPersonDb.getDirTitle());
                    propPerson.setHOMEUNIT(propPersonDb.getHomeUnit());
                    if(propPersonDb.getFaculty()){
                        propPerson.setISFACULTY("Y");
                    }else{
                        propPerson.setISFACULTY("N");
                    }
                    if(propPersonDb.getGraduateStudentStaff()){
                        propPerson.setISGRADUATESTUDENTSTAFF("Y");
                    }else{
                        propPerson.setISGRADUATESTUDENTSTAFF("N");
                    }
                    
                    if(propPersonDb.getResearchStaff()){
                        propPerson.setISRESEARCHSTAFF("Y");
                    }else{
                        propPerson.setISRESEARCHSTAFF("N");
                    }
                    
                    if(propPersonDb.getServiceStaff()){
                        propPerson.setISSERVICESTAFF("Y");
                    }else{
                        propPerson.setISSERVICESTAFF("N");
                    }
                    
                    if(propPersonDb.getSupportStaff()){
                        propPerson.setISSUPPORTSTAFF("Y");
                    }else{
                        propPerson.setISSUPPORTSTAFF("N");
                    }
                    
                    if(propPersonDb.getOtherAcademicGroup()){
                        propPerson.setISOTHERACCADEMICGROUP("Y");
                    }else{
                        propPerson.setISOTHERACCADEMICGROUP("N");
                    }
                    
                    if(propPersonDb.getMedicalStaff()){
                        propPerson.setISMEDICALSTAFF("Y");
                    }else{
                        propPerson.setISMEDICALSTAFF("N");
                    }
                    if(propPersonDb.getVacationAccural()){
                        propPerson.setVACATIONACCURAL("Y");
                    }else{
                        propPerson.setVACATIONACCURAL("N");
                    }
                    
                    if(propPersonDb.getOnSabbatical()){
                        propPerson.setISONSABBATICAL("Y");
                    }else{
                        propPerson.setISONSABBATICAL("Y");
                    }
                    
                    propPerson.setISONSABBATICAL(propPersonDb.getProvided());
                    propPerson.setIDVERIFIED(propPersonDb.getVerified());
                    
                    if(propPersonDb.getUpdateTimestamp()!= null){
                        propPerson.setUPDATETIMESTAMP(formatDate(propPersonDb.getUpdateTimestamp()));
                    }
                    
                    
                    propPerson.setUPDATEUSER(propPersonDb.getUpdateUser());
                    propPerson.setADDRESSLINE1(propPersonDb.getAddress1());
                    propPerson.setADDRESSLINE2(propPersonDb.getAddress2());
                    propPerson.setADDRESSLINE3(propPersonDb.getAddress3());
                    
                    propPerson.setCITY(propPersonDb.getCity());
                    propPerson.setCOUNTY(propPersonDb.getCounty());
                    propPerson.setSTATE(propPersonDb.getState());
                    propPerson.setPOSTALCODE(propPersonDb.getPostalCode());
                    propPerson.setCOUNTRYCODE(propPersonDb.getCountryCode());
                    propPerson.setFAXNUMBER(propPersonDb.getFaxNumber());
                    propPerson.setPAGERNUMBER(propPersonDb.getPagerNumber());
                    propPerson.setMOBILEPHONENUMBER(propPersonDb.getMobilePhNumber());
                    propPerson.setERACOMMONSUSERNAME(propPersonDb.getEraCommonsUsrName());
                    
                    propPerson.setSORTID(formatToBigDesimal(propPersonDb.getSortId()));
                    propPerson.getPROPPERSONBIO().addAll(getPersonalBioSources(propPersonDb.getPersonId()));
                    propPerson.getPROPPERSYNQ().addAll(gePersonalYNQs(ynqListDb, propPersonDb.getPersonId()));
                    propPerson.getPROPPERSONDEGREE().addAll(getPersonlDegrees(propPersonDb.getPersonId()));
                    propPerson.getPROPPERCUSTOMDATA().addAll(getPersonalCustomDatas(propPersonDb.getPersonId()));
                    
                    break;
                }
                
            }
        }
        
        return propPerson;
    }
    
    /**
     *  Retuns the list of PROPPERSONBIO type objects. Identifies all the ProposalBiographyFormBean objects
     *  using ProposalBiographyFormBean and personId. Cretes the PROPPERSONBIO objects using ProposalBiographyFormBean.
     *  Return the list of PROPPERSONBIO type objects.
     *  @param personId String.
     *  @throws CoeusException.
     *  @throws DBException.
     *  @throws JAXBException
     *  @return perBioSourceList List.
     */

    private List getPersonalBioSources(final String personId)
                                 throws CoeusException, DBException,JAXBException {
        
        final Vector perBioSourceListDb =  propPerTxnbean.getProposalBioGraphy(proposalNumber, personId);
        final Vector perBioSourceList = new Vector();
        
        if(perBioSourceListDb != null){
            ProposalBiographyFormBean propPerBioBeanDb = null;
            PROPPERSONBIO propPerBioBean = null;
            
            for(Object obj : perBioSourceListDb){
                propPerBioBeanDb = (ProposalBiographyFormBean)obj;
                propPerBioBean = propObjFact.createPROPPERSONBIO();
                
                propPerBioBean.setPROPOSALNUMBER(propPerBioBeanDb.getProposalNumber());
                propPerBioBean.setPERSONID(propPerBioBeanDb.getPersonId());
                propPerBioBean.setBIONUMBER(formatToBigDesimal(propPerBioBeanDb.getBioNumber()));
                propPerBioBean.setDESCRIPTION(propPerBioBeanDb.getDescription());
                
                if(propPerBioBeanDb.getUpdateTimestamp() != null){
                    propPerBioBean.setUPDATETIMESTAMP(formatDate(propPerBioBeanDb.getUpdateTimestamp()));
                }
                
                propPerBioBean.setUPDATEUSER(propPerBioBeanDb.getUpdateUser());
                perBioSourceList.add(propPerBioBean);
            }
        }
        
        return perBioSourceList;
    }
    
    /**
     *  Retuns the list of PROPPERSYNQ type objects. Creates PROPPERSYNQ type of object from the list of
     *  ProposalCertificationFormBean which is passed to the method.
     *  @param ynqListDb List.
     *  @param personId String.
     *  @throws CoeusException.
     *  @throws DBException.
     *  @throws JAXBException
     *  @retun ynqList List.
     */

    private List gePersonalYNQs(final List ynqListDb, final String personId)
                                throws CoeusException, DBException,JAXBException{
        
        Vector ynqList = new Vector();
        
        if(ynqListDb != null){
            ProposalCertificationFormBean propCertiryBeanDb = null;
            PROPPERSYNQ propCertiryBean = null;
           
            for(Object obj : ynqListDb){
                propCertiryBeanDb = (ProposalCertificationFormBean)obj;
                if(personId != null && personId.equals(propCertiryBeanDb.getPersonId())){
                    propCertiryBean = propObjFact.createPROPPERSYNQ();
                    
                    propCertiryBean.setPROPOSALNUMBER(propCertiryBeanDb.getProposalNumber());
                    propCertiryBean.setPERSONID(propCertiryBeanDb.getPersonId());
                    //propCertiryBean.setQUESTIONID(propCertiryBeanDb.); todo
                    //propCertiryBean.setANSWER(propCertiryBeanDb.ge); todo
                    
                    if(propCertiryBeanDb.getUpdateTimestamp() != null){
                        propCertiryBean.setUPDATETIMESTAMP(formatDate(propCertiryBeanDb.getUpdateTimestamp()));
                    }
                    
                    propCertiryBean.setUPDATEUSER(propCertiryBeanDb.getUpdateUser());
                    ynqList.add(propCertiryBean);
                }
            }
            
        }
        
        return ynqList;
    }
    
    /**
     *  Returns the list of PROPPERCUSTOMDATA type objects. Identifies all ProposalCustomElementsInfoBean
     *  type object using proposalNumber and personId. Creates PROPPERCUSTOMDATA type object form this data.
     *  @param personId String.
     *  @throws CoeusException.
     *  @throws DBException.
     *  @throws JAXBException
     *  @retun customDataList List.
     *
     */

    private List getPersonalCustomDatas(final String personId)
                            throws CoeusException, DBException,JAXBException{
        
        final Vector customDataListDb = propDevTxnBean.getProposalOthersDetails(proposalNumber, personId);
        Vector customDataList = new Vector();
        
        if(customDataListDb != null){
            ProposalCustomElementsInfoBean customDataDb = null;
            PROPPERCUSTOMDATA customData = null;
   
            for(Object obj : customDataListDb){
                customDataDb = (ProposalCustomElementsInfoBean)obj;
                customData = propObjFact.createPROPPERCUSTOMDATA();
                
                customData.setPROPOSALNUMBER(customDataDb.getProposalNumber());
                customData.setPERSONID(customDataDb.getPersonId());
                customData.setCOLUMNNAME(customDataDb.getColumnLabel());
                customData.setCOLUMNVALUE(customDataDb.getColumnValue());
                
                if(customDataDb.getUpdateTimestamp()!= null){
                    customData.setUPDATETIMESTAMP(formatDate(customDataDb.getUpdateTimestamp()));
                }
                
                customData.setUPDATEUSER(customDataDb.getUpdateUser());
                
                customDataList.add(customData);
            }
        }
        
        return customDataList;
    }
    
    /**
     *  Retuns the list of PROPPERSONDEGREE type objects. Identifes all ProposalPerDegreeFormBean type objects using
     *  proposalNumber and personId. Creates PROPPERSONDEGREE object form this data.
     *  @param personId String.
     *  @throws CoeusException.
     *  @throws DBException.
     *  @throws JAXBException
     *  @retun personlDegreeList List.
     *
     */

    private List getPersonlDegrees(final String personId)
                            throws CoeusException, DBException,JAXBException{
        final Vector personlDegreeListDb = propPerTxnbean.getProposalPersonDegree(proposalNumber, personId);
        Vector personlDegreeList = new Vector();
        
        if(personlDegreeListDb != null){
            
            ProposalPerDegreeFormBean perDegreeDb = null;
            PROPPERSONDEGREE  perDegree = null;
            
            for(Object obj : personlDegreeListDb){
                perDegreeDb = (ProposalPerDegreeFormBean)obj;
                perDegree = propObjFact.createPROPPERSONDEGREE();
                
                perDegree.setPROPOSALNUMBER(perDegreeDb.getProposalNumber());
                perDegree.setPERSONID(perDegreeDb.getProposalNumber());
                perDegree.setDEGREECODE(perDegreeDb.getDegreeCode());
                
                if(perDegreeDb.getGraduationDate()!= null){
                    perDegree.setGRADUATIONDATE(formatDate(perDegreeDb.getGraduationDate()));
                }
                
                perDegree.setFIELDOFSTUDY(perDegreeDb.getFieldOfStudy());
                perDegree.setSPECIALIZATION(perDegreeDb.getSpecialization());
                perDegree.setSCHOOL(perDegreeDb.getSchool());
                perDegree.setSCHOOLIDCODE(perDegreeDb.getSchoolIdCode());
                perDegree.setSCHOOLID(perDegreeDb.getSchoolId());
                
                if(perDegreeDb.getUpdateTimestamp()!=null){
                    perDegree.setUPDATETIMESTAMP(formatDate(perDegreeDb.getUpdateTimestamp()));
                }
                
                perDegree.setUPDATEUSER(perDegreeDb.getUpdateUser());
                personlDegreeList.add(perDegree);
            }
        }
        
        return personlDegreeList;
    }
    
    /**
     *  Returns the list of PROPABSTRACT type objects. Identifies all ProposalAbstractFormBean
     *  for a proposal by using proposalNumber. Creates the PROPABSTRACT type objects from this data.
     *  @param personId String.
     *  @throws CoeusException.
     *  @throws DBException.
     *  @throws JAXBException
     *  @retun propAbstractList List.
     *
     */
    
    private List getProposalAbstracts() throws CoeusException, DBException,JAXBException{
        
        final Vector propAbstractList = new Vector();
        final Vector propAbstractListDb = propDevTxnBean.getProposalAbstract(proposalNumber);
        final Vector abstractTypeListDb = propDevTxnBean.getAbstractTypes();
        
        if(propAbstractListDb != null){
            ProposalAbstractFormBean propAbstractDb = null;
            PROPABSTRACT propAbstract = null;
            ABSTRACTTYPEType abstractType = null;
            
            for(Object obj : propAbstractListDb){
                propAbstractDb = (ProposalAbstractFormBean)obj;
                propAbstract = propObjFact.createPROPABSTRACT();
                
                propAbstract.setPROPOSALNUMBER(propAbstractDb.getProposalNumber());
                abstractType = lkObjFact.createABSTRACTTYPE(); 
                abstractType.setABSTRACTTYPECODE(formatToBigDesimal(propAbstractDb.getAbstractTypeCode()));
                abstractType.setDESCRIPTION(propAbstractDb.getAbstractText());
                propAbstract.setABSTRACTTYPE(abstractType);
                
                propAbstract.setABSTRACT(propAbstractDb.getAbstractText());
                
                if(propAbstractDb.getUpdateTimestamp() != null){
                    propAbstract.setUPDATETIMESTAMP(formatDate(propAbstractDb.getUpdateTimestamp()));
                }
                
                propAbstract.setUPDATEUSER(propAbstractDb.getUpdateUser());
                propAbstract.setABSTRACTTYPE(getAbstractType(abstractTypeListDb,
                                                            propAbstractDb.getAbstractTypeCode()));
                propAbstractList.add(propAbstract);
            }
            
        }
        return propAbstractList;
    }
    
    
    /**
     *  Returns the list of ABSTRACTTYPE type object. Search AbstractTypeFormBean in the list of
     *  AbstractTypeFormBean bean object which matches a abstract using abstracttypecode which are
     *  passed to the method. Creates the ABSTRACTTYPE object using this data.
     *  @param abstractTypeListDb List.
     *  @param absTypeCode int.
     */
    
    private ABSTRACTTYPEType getAbstractType(final List abstractTypeListDb, final int absTypeCode)
                                            throws JAXBException{
        
        AbstractTypeFormBean abstractTypeDb = null;
        ABSTRACTTYPEType abstractType = null;
        
        if(abstractTypeListDb != null){
            for(Object obj : abstractTypeListDb){
                abstractTypeDb = (AbstractTypeFormBean)obj;
                
                if(absTypeCode == abstractTypeDb.getAbstractTypeId()){//todo: check this condation
                    abstractType = lkObjFact.createABSTRACTTYPEType();
                    abstractType.setABSTRACTTYPECODE(formatToBigDesimal(abstractTypeDb.getAbstractTypeId()));
                    abstractType.setDESCRIPTION(abstractTypeDb.getDescription());
                    break;
                }
                
            }
        }
        
        return abstractType;
    }
    
    private CUMULATIVEMODULARBUDGETType getCumulativeModularBudget(final int versionNumber) 
                                throws CoeusException,JAXBException, DBException{
        Map totalCostMap = new Hashtable();
        final Vector modBudDataListDb = budgetTxnBean.getBudgetModularData(proposalNumber, versionNumber);
        final Vector indirectCostListDb = budgetTxnBean.getBudgetModularIDCData(proposalNumber, versionNumber);
        float totalDirectCostFA = 0;
        float totalConsortiumFNA = 0;
        float totalDirectCost = 0;
        float totalFundRequestedIdc = 0;
        
         if(modBudDataListDb != null){
            BudgetModularBean modularBudBeanDb = null;
            for(Object obj : modBudDataListDb){
                 modularBudBeanDb = (BudgetModularBean)obj;
                 
                 totalDirectCostFA += modularBudBeanDb.getDirectCostFA();
                 totalConsortiumFNA += modularBudBeanDb.getConsortiumFNA();
                 totalDirectCost += modularBudBeanDb.getTotalDirectCost();
            }
        
         }
        if(indirectCostListDb != null){
             BudgetModularIDCBean modularIDCBeanDb = null;
             INDIRECTCOSTType indirectCost = null;
            
            for(Object obj : indirectCostListDb){
                modularIDCBeanDb = (BudgetModularIDCBean)obj;
                totalFundRequestedIdc += modularIDCBeanDb.getFundRequested();
            }
        }
        CUMULATIVEMODULARBUDGETType cumModBudType = modBudgetObjFact.createCUMULATIVEMODULARBUDGETType();
        
        cumModBudType.setVERSIONNUMBER(formatToBigDesimal(versionNumber));
        cumModBudType.setDIRECTCOSTLESSCONSORFNA(formatToBigDesimal(totalDirectCostFA));
        cumModBudType.setCONSORTIUMFNA(formatToBigDesimal(totalConsortiumFNA));
        cumModBudType.setTOTALDIRECTCOST(formatToBigDesimal(totalDirectCost));
        cumModBudType.setTOTALINDIRECTCOST(formatToBigDesimal(totalFundRequestedIdc));
        cumModBudType.setTOTALCOST(formatToBigDesimal(totalDirectCost + totalFundRequestedIdc));
        
        return cumModBudType;
    } 
    //Added for COEUSQA-1682_FS_Proposal Development Schema Infrastructure end.
    
    private List getProposalSites()throws CoeusException, JAXBException, DBException{
        CoeusVector hmProposalSites = propDevTxnBean.getProposalSites(proposalNumber);
        Vector vecPropSites = new Vector();
        if(hmProposalSites != null && !hmProposalSites.isEmpty()){
            for(Object siteDetails : hmProposalSites){
                ProposalSiteBean proposalSiteDetails = (ProposalSiteBean)siteDetails;
                ProposalSites proposalSiteTypes = propObjFact.createProposalSites();
                LOCATIONType loactionType = lkObjFact.createLOCATIONType();
                loactionType.setLOCATIONTYPECODE(proposalSiteDetails.getLocationTypeCode());
                loactionType.setDESCRIPTION(proposalSiteDetails.getLocationTypeName());
                proposalSiteTypes.setLOCATION(loactionType);
                proposalSiteTypes.setOrganizationId(proposalSiteDetails.getOrganizationId());
                proposalSiteTypes.setOrganizationName(proposalSiteDetails.getLocationName());
                RolodexDetailsBean rolodexDetails = proposalSiteDetails.getRolodexDetailsBean();
                if(rolodexDetails != null){
                    proposalSiteTypes.setRolodexId(rolodexDetails.getRolodexId());
                    StringBuffer rolodexAddress = new StringBuffer();
                    rolodexAddress.append(rolodexDetails.getOrganization() == null ? "" : rolodexDetails.getOrganization() + " ");
                    rolodexAddress.append(rolodexDetails.getAddress1() == null ? "" : rolodexDetails.getAddress1() + " ");
                    rolodexAddress.append(rolodexDetails.getAddress2() == null ? "" : rolodexDetails.getAddress2() + " ");
                    rolodexAddress.append(rolodexDetails.getAddress3() == null ? "" : rolodexDetails.getAddress3() + " ");
                    rolodexAddress.append(rolodexDetails.getCity() == null ? "" : rolodexDetails.getCity() + " ");
                    rolodexAddress.append(rolodexDetails.getCounty() == null ? "" : rolodexDetails.getCounty() + " ");
                    rolodexAddress.append(rolodexDetails.getState() == null ? "" : rolodexDetails.getState() + " - ");
                    rolodexAddress.append(rolodexDetails.getPostalCode() == null ? "" : rolodexDetails.getPostalCode() + " ");
                    rolodexAddress.append(rolodexDetails.getCountry() == null ? "" : rolodexDetails.getCountry());
                    proposalSiteTypes.setRolodexFullAddess(rolodexAddress.toString());
                }
                CoeusVector cvCongDetails = proposalSiteDetails.getCongDistricts();
                Vector vecCongDetails = new Vector();
                if(cvCongDetails != null && !cvCongDetails.isEmpty()){
                    for(Object congDistrictDetails : cvCongDetails){
                       ProposalCongDistrictBean propCongDistrictDetails = (ProposalCongDistrictBean)congDistrictDetails;
                       PropSiteCongDistricts siteCongDistrictsType =  propObjFact.createPropSiteCongDistricts();
                       siteCongDistrictsType.setCongDistrict(propCongDistrictDetails.getCongDistrict());
                       siteCongDistrictsType.setSiteNumber(propCongDistrictDetails.getSiteNumber());
                       if(siteCongDistrictsType.getUpdateTimestamp() != null){
                           siteCongDistrictsType.setUpdateTimestamp(formatDate(propCongDistrictDetails.getUpdateTimestamp()));
                       }
                       siteCongDistrictsType.setUpdateUser(propCongDistrictDetails.getUpdateUser());
                       vecCongDetails.add(siteCongDistrictsType);
                    }
                }
                proposalSiteTypes.getSiteCOngDistricts().addAll(vecCongDetails);
                vecPropSites.add(proposalSiteTypes);
            }
        }
        return vecPropSites;
    }
    
    /**
     * Method to get the column header and to assign the column header details to CREDITSPLITCOLUMNSType
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws javax.xml.bind.JAXBException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return vecColumnHeader
     */
    private Vector getCreditSplitColumns()throws CoeusException, JAXBException, DBException{
        CoeusVector cvInvCreditTypes = propDevTxnBean.getInvCreditTypes();
        Vector vecColumnHeader = new Vector();
        if(cvInvCreditTypes != null && !cvInvCreditTypes.isEmpty()){
            CREDITSPLITCOLUMNSType createSplitType = propObjFact.createCREDITSPLITCOLUMNSType();
            for(int creditTypeIndex = 0;creditTypeIndex<cvInvCreditTypes.size();creditTypeIndex++){
                InvCreditTypeBean invCreditTypeBean = (InvCreditTypeBean)cvInvCreditTypes.get(creditTypeIndex);
                String creditSplitTypeDescription = invCreditTypeBean.getDescription();
                int invCreditTypeCode = invCreditTypeBean.getInvCreditTypeCode();
                vecColumnHeader.add(invCreditTypeCode);
                if(creditTypeIndex == 0){
                    createSplitType.setColumnName1(creditSplitTypeDescription);
                }else if(creditTypeIndex == 1){
                    createSplitType.setColumnName2(creditSplitTypeDescription);
                }else if(creditTypeIndex == 2){
                    createSplitType.setColumnName3(creditSplitTypeDescription);
                }else if(creditTypeIndex == 3){
                    createSplitType.setColumnName4(creditSplitTypeDescription);
                }else if(creditTypeIndex == 4){
                    createSplitType.setColumnName5(creditSplitTypeDescription);
                }else if(creditTypeIndex == 5){
                    createSplitType.setColumnName6(creditSplitTypeDescription);
                }else if(creditTypeIndex == 6){
                    createSplitType.setColumnName7(creditSplitTypeDescription);
                }else if(creditTypeIndex == 7){
                    createSplitType.setColumnName8(creditSplitTypeDescription);
                }else if(creditTypeIndex == 8){
                    createSplitType.setColumnName9(creditSplitTypeDescription);
                }else if(creditTypeIndex == 9){
                    createSplitType.setColumnName10(creditSplitTypeDescription);
                    break;
                }
            }
            proposal.setCREDITSPLITCOLUMNS(createSplitType);
            
        }
        return vecColumnHeader;
    }
    
    /**
     * Method to get the credit split details for the investigators and their units
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws javax.xml.bind.JAXBException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return vecPerCreditForPrint
     */
    private List  getCreditSplit()throws CoeusException, JAXBException, DBException{
        Vector vecPerCreditForPrint = new Vector();
        Vector vecColumnHeader = getCreditSplitColumns();
        // Gets investigator credit split details
        CoeusVector cvInvCreditSplit = propDevTxnBean.getPropPerCreditSplit(proposalNumber);
        Hashtable hmInvPersCreditDetails = new Hashtable();
        HashSet hsCreditSplitType = new HashSet();
        // HashMap key will have the personid which is unique, value will have the collection of the InvestigatorCreditSplitBean for the personid
        if(cvInvCreditSplit != null && !cvInvCreditSplit.isEmpty()){
            for(Object personCreditSplit : cvInvCreditSplit){
                InvestigatorCreditSplitBean invCreditDetails = (InvestigatorCreditSplitBean)personCreditSplit;
                if(hmInvPersCreditDetails.get(invCreditDetails.getPersonId()) != null){
                    Vector vecPresCreditDetails = (Vector)hmInvPersCreditDetails.get(invCreditDetails.getPersonId());
                    vecPresCreditDetails.add(invCreditDetails);
                }else{
                    Vector vecPresCreditDetails = new Vector();
                    vecPresCreditDetails.add(invCreditDetails);
                    hmInvPersCreditDetails.put(invCreditDetails.getPersonId(),vecPresCreditDetails);
                }
            }
            
            Iterator personIterator = hmInvPersCreditDetails.keySet().iterator();
            // 
            while(personIterator.hasNext()){
                String personId = (String)personIterator.next();
                Vector vecPerCreditDetails = (Vector)hmInvPersCreditDetails.get(personId);
                String personName = "";
                PROPPERCREDITSPLITType propPerCreditSplitType = propObjFact.createPROPPERCREDITSPLITType();
                if(vecPerCreditDetails != null && !vecPerCreditDetails.isEmpty()){
                    InvestigatorCreditSplitBean invPersonCreditDetails = (InvestigatorCreditSplitBean)vecPerCreditDetails.get(0);
                    personName = invPersonCreditDetails.getPersonName();
                    Vector vecFilteredPerCreditSplit = new Vector();
                    String piPersonId = proposal.getPROPOSALMASTER().getPRINCIPALINVESTIGATORID();
                    if(personId.equals(piPersonId)){
                        personName = personName+"(PI)";
                    }
                    propPerCreditSplitType.setPERSONID(personId);
                    propPerCreditSplitType.setPERSONNAME(personName);
                    if(vecColumnHeader != null && !vecColumnHeader.isEmpty()){
                        // Based on the column header order, person credit bean will be added to the collection
                        for(int columneheaderIndex = 0;columneheaderIndex<vecColumnHeader.size();columneheaderIndex++){
                            int invCreditSplitTypeCode = ((Integer)vecColumnHeader.get(columneheaderIndex)).intValue();
                            for(Object perCreditDetails : vecPerCreditDetails){
                                InvestigatorCreditSplitBean personCreditSplitDetails = (InvestigatorCreditSplitBean)perCreditDetails;
                                if(personCreditSplitDetails.getInvCreditTypeCode() == invCreditSplitTypeCode){
                                    vecFilteredPerCreditSplit.add(personCreditSplitDetails);
                                }
                            }
                        }
                    }
                    
                    if(vecFilteredPerCreditSplit != null && !vecFilteredPerCreditSplit.isEmpty()){
                        for(int invCreditindex = 0; invCreditindex < vecFilteredPerCreditSplit.size();invCreditindex++){
                            InvestigatorCreditSplitBean invCreditSplitBean = (InvestigatorCreditSplitBean)vecFilteredPerCreditSplit.get(invCreditindex);
                            BigDecimal creditValue = BigDecimal.valueOf(invCreditSplitBean.getCredit());
                            if(invCreditindex == 0){
                                propPerCreditSplitType.setColumnValue1(creditValue);
                            }else if(invCreditindex == 1){
                                propPerCreditSplitType.setColumnValue2(creditValue);
                            }else if(invCreditindex == 2){
                                propPerCreditSplitType.setColumnValue3(creditValue);
                            }else if(invCreditindex == 3){
                                propPerCreditSplitType.setColumnValue4(creditValue);
                            }else if(invCreditindex == 4){
                                propPerCreditSplitType.setColumnValue5(creditValue);
                            }else if(invCreditindex == 5){
                                propPerCreditSplitType.setColumnValue6(creditValue);
                            }else if(invCreditindex == 6){
                                propPerCreditSplitType.setColumnValue7(creditValue);
                            }else if(invCreditindex == 7){
                                propPerCreditSplitType.setColumnValue8(creditValue);
                            }else if(invCreditindex == 8){
                                propPerCreditSplitType.setColumnValue9(creditValue);
                            }else if(invCreditindex == 9){
                                propPerCreditSplitType.setColumnValue10(creditValue);
                                break;
                            }
                        }
                    }
                    assignInvUnitCreditToInvCredit(personId, propPerCreditSplitType);
                    vecPerCreditForPrint.add(propPerCreditSplitType);
                }
                
                
            }
            
        }
        return vecPerCreditForPrint;
    }
     
    /**
     * Method to assign the investigator credit spilt details to the investigator type 
     * @param personId 
     * @param propPerCreditSplitType 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws javax.xml.bind.JAXBException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     */
    private void assignInvUnitCreditToInvCredit(String personId, PROPPERCREDITSPLITType propPerCreditSplitType)throws CoeusException, JAXBException, DBException{
        CoeusVector cvPropUnitCreditSplit = propDevTxnBean.getPropUnitCreditSplit(proposalNumber);
        // Fileter the investigator units credit split based on the person id
        Equals eqPersonId = new Equals("personId", personId);
        CoeusVector cvFilteredUnitSplit = cvPropUnitCreditSplit.filter(eqPersonId);
        // Unit total for each column for a investigator will be calculated
        double column1CreditTotal = 0.0;
        double column2CreditTotal = 0.0;
        double column3CreditTotal = 0.0;
        double column4CreditTotal = 0.0;
        double column5CreditTotal = 0.0;
        double column6CreditTotal = 0.0;
        double column7CreditTotal = 0.0;
        double column8CreditTotal = 0.0;
        double column9CreditTotal = 0.0;
        double column10CreditTotal = 0.0;
        // Filtered unit split will be stored in the HashMap based on the unit number
        if(cvFilteredUnitSplit != null && !cvFilteredUnitSplit.isEmpty()){
            Hashtable hmInvUnitCreditDetails = new Hashtable();
            for(Object perUnitCreditSplit : cvFilteredUnitSplit){
                InvestigatorCreditSplitBean invUnitCreditDetails = (InvestigatorCreditSplitBean)perUnitCreditSplit;
                if(hmInvUnitCreditDetails.get(invUnitCreditDetails.getUnitNumber()) != null){
                    Vector vecPerUnitCreditDetails = (Vector)hmInvUnitCreditDetails.get(invUnitCreditDetails.getUnitNumber());
                    vecPerUnitCreditDetails.add(invUnitCreditDetails);
                }else{
                    Vector vecPerUnitCreditDetails = new Vector();
                    vecPerUnitCreditDetails.add(invUnitCreditDetails);
                    hmInvUnitCreditDetails.put(invUnitCreditDetails.getUnitNumber(),vecPerUnitCreditDetails);
                }
            }
            Iterator personUnitIterator = hmInvUnitCreditDetails.keySet().iterator();
            Vector vecUnitForPrint = new Vector();
            
            while(personUnitIterator.hasNext()){
                String unitNumber = (String)personUnitIterator.next();
                Vector vecPerUnitCreditDetails = (Vector)hmInvUnitCreditDetails.get(unitNumber);
                PROPUNITCREDITSPLITType propPerUnitCreditSplitType = propObjFact.createPROPUNITCREDITSPLITType();
                propPerUnitCreditSplitType.setUNIT(unitNumber);
                for(int invUnitCreditindex = 0; invUnitCreditindex < vecPerUnitCreditDetails.size();invUnitCreditindex++){
                    InvestigatorCreditSplitBean invUnitCreditSplitBean = (InvestigatorCreditSplitBean)vecPerUnitCreditDetails.get(invUnitCreditindex);
                    double credit = invUnitCreditSplitBean.getCredit();
                    BigDecimal creditValue = BigDecimal.valueOf(credit);
                    propPerUnitCreditSplitType.setUNITNAME(invUnitCreditSplitBean.getDescription());
                    if(invUnitCreditindex == 0){
                        propPerUnitCreditSplitType.setColumnValue1(creditValue);
                        column1CreditTotal = column1CreditTotal+credit;
                    }else if(invUnitCreditindex == 1){
                        propPerUnitCreditSplitType.setColumnValue2(creditValue);
                        column2CreditTotal = column2CreditTotal+credit;
                    }else if(invUnitCreditindex == 2){
                        propPerUnitCreditSplitType.setColumnValue3(creditValue);
                        column3CreditTotal = column3CreditTotal+credit;
                    }else if(invUnitCreditindex == 3){
                        propPerUnitCreditSplitType.setColumnValue4(creditValue);
                        column4CreditTotal = column4CreditTotal+credit;
                    }else if(invUnitCreditindex == 4){
                        propPerUnitCreditSplitType.setColumnValue5(creditValue);
                        column5CreditTotal = column5CreditTotal+credit;
                    }else if(invUnitCreditindex == 5){
                        propPerUnitCreditSplitType.setColumnValue6(creditValue);
                        column6CreditTotal = column6CreditTotal+credit;
                    }else if(invUnitCreditindex == 6){
                        propPerUnitCreditSplitType.setColumnValue7(creditValue);
                        column7CreditTotal = column7CreditTotal+credit;
                    }else if(invUnitCreditindex == 7){
                        propPerUnitCreditSplitType.setColumnValue8(creditValue);
                        column8CreditTotal = column8CreditTotal+credit;
                    }else if(invUnitCreditindex == 8){
                        propPerUnitCreditSplitType.setColumnValue9(creditValue);
                        column9CreditTotal = column9CreditTotal+credit;
                    }else if(invUnitCreditindex == 9){
                        propPerUnitCreditSplitType.setColumnValue10(creditValue);
                        column10CreditTotal = column10CreditTotal+credit;
                        break;
                    }
                }
                vecUnitForPrint.add(propPerUnitCreditSplitType);
            }
            propPerCreditSplitType.getPROPUNITCREDITSPLIT().addAll(vecUnitForPrint);
        }
        // Total of the unit split columns will be assigned
        propPerCreditSplitType.setTotalUnitColumnValue1(BigDecimal.valueOf(column1CreditTotal));
        propPerCreditSplitType.setTotalUnitColumnValue2(BigDecimal.valueOf(column2CreditTotal));
        propPerCreditSplitType.setTotalUnitColumnValue3(BigDecimal.valueOf(column3CreditTotal));
        propPerCreditSplitType.setTotalUnitColumnValue4(BigDecimal.valueOf(column4CreditTotal));
        propPerCreditSplitType.setTotalUnitColumnValue5(BigDecimal.valueOf(column5CreditTotal));
        propPerCreditSplitType.setTotalUnitColumnValue6(BigDecimal.valueOf(column6CreditTotal));
        propPerCreditSplitType.setTotalUnitColumnValue7(BigDecimal.valueOf(column7CreditTotal));
        propPerCreditSplitType.setTotalUnitColumnValue8(BigDecimal.valueOf(column8CreditTotal));
        propPerCreditSplitType.setTotalUnitColumnValue9(BigDecimal.valueOf(column9CreditTotal));
        propPerCreditSplitType.setTotalUnitColumnValue10(BigDecimal.valueOf(column10CreditTotal));
        
    }
    
    /**
     * Method to build the routing details based on the map and level
     * @param proposalnumber 
     * @param proposalType 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @throws java.text.ParseException 
     * @throws javax.xml.bind.JAXBException 
     */
    private void buildRoutingDetails(String proposalnumber, PROPOSAL proposalType) throws CoeusException, DBException, ParseException, JAXBException{
      RoutingTxnBean routingTxnBean = new RoutingTxnBean();
      RoutingBean routingBean = routingTxnBean.getRoutingHeader(ModuleConstants.PROPOSAL_DEV_MODULE_CODE+"", proposalNumber,"0", 0);
      // sets the basic routing details
      RoutingType routingDetails = apprRoutingObjFact.createRoutingType();
      routingDetails.setApprovalSequence(routingBean.getApprovalSequence());
      routingDetails.setModuleCode(routingBean.getModuleCode());
      routingDetails.setModuleItemKeySequence(routingBean.getModuleItemKeySequence());
      routingDetails.setModuleItemkey(routingBean.getModuleItemKey());
      routingDetails.setRoutingStartDate(routingBean.getRoutingEndDate());
      routingDetails.setRoutingEndDate(routingBean.getRoutingEndDate());
      routingDetails.setRoutingStartUser(routingBean.getRoutingStartUser());
      routingDetails.setRoutingEndUser(routingBean.getRoutingEndUser());
      // Gets the routing map details with routing details
      CoeusVector cvRoutingMaps = routingTxnBean.getRoutingMaps(routingBean.getRoutingNumber());
      //  Orders the map collection based on the map and parent number
      Vector vecOrderedMaps  = null;
      if(cvRoutingMaps != null && !cvRoutingMaps.isEmpty()){
         vecOrderedMaps =  getOrderedMap(cvRoutingMaps, new Vector(), 0);
      }
      Vector vecRoutingMapDetails = new Vector();
      
      if(vecOrderedMaps != null && !vecOrderedMaps.isEmpty()){
          // Iterating each and every map
          for(Object routingMapDetails : vecOrderedMaps){
              RoutingMapBean routingMapBean = (RoutingMapBean)routingMapDetails;
              RoutingMapsType routingMapsType =  apprRoutingObjFact.createRoutingMapsType();
              routingMapsType.setApprovalStatus(routingMapBean.getApprovalStatus());
              routingMapsType.setDescription(routingMapBean.getDescription()) ;
              routingMapsType.setMapId(routingMapBean.getMapId());
              routingMapsType.setMapNumber(routingMapBean.getMapNumber());
              routingMapsType.setParentMapNumber(routingMapBean.getParentMapNumber());
              routingMapsType.setRoutingNumber(routingMapBean.getRoutingNumber());
              if(routingMapBean.getSystemFlag()){
                  routingMapsType.setSystemFlag("Y");
              }else{
                  routingMapsType.setSystemFlag("N");
              }
              CoeusVector cvMapDetails = routingMapBean.getRoutingMapDetails();
              // Iterating routing details for the map
              if(cvMapDetails != null && !cvMapDetails.isEmpty()){
                  Vector vecRoutingDetails = null;
                  LinkedHashMap  htLevelDetails = new LinkedHashMap ();
                  for(Object routing : cvMapDetails){
                      RoutingDetailsBean routingDetailsBean = (RoutingDetailsBean)routing;
                      RoutingDetailsType routingDetailsType = apprRoutingObjFact.createRoutingDetailsType();
                      if(routingDetailsBean.getApprovalDate() != null){
                          Calendar approvalDateCal = Calendar.getInstance();
                          approvalDateCal.setTime(routingDetailsBean.getApprovalDate());
                          routingDetailsType.setApprovalDate(approvalDateCal);
                      }
                      routingDetailsType.setApprovalStatus(getRoutingApprovalStatusDesc(routingDetailsBean.getApprovalStatus()));
                      routingDetailsType.setApproverNumber(routingDetailsBean.getApproverNumber());
                      routingDetailsType.setDescription(routingDetailsBean.getDescription());
                      routingDetailsType.setLevelNumber(routingDetailsBean.getLevelNumber());
                      routingDetailsType.setMapNumber(routingDetailsBean.getMapNumber());
                      if(routingDetailsBean.isPrimaryApproverFlag()){
                          routingDetailsType.setPrimaryApproverFlag("Y");
                      }else{
                          routingDetailsType.setPrimaryApproverFlag("N");
                      }
                      routingDetailsType.setRoutingNumber(routingDetailsBean.getRoutingNumber());
                      routingDetailsType.setStopNumber(routingDetailsBean.getStopNumber());
                      if(routingDetailsBean.getSubmissionDate() != null){
                          Calendar submissionDateCal = Calendar.getInstance();
                          submissionDateCal.setTime(routingDetailsBean.getSubmissionDate());
                          routingDetailsType.setSubmissionDate(submissionDateCal);
                      }
                      routingDetailsType.setUserId(routingDetailsBean.getUserId());
                      routingDetailsType.setUserName(routingDetailsBean.getUserName());

                      if(htLevelDetails.get(routingDetailsBean.getLevelNumber()+"") != null){
                          Vector vecRouting = (Vector)htLevelDetails.get(routingDetailsBean.getLevelNumber()+"");
                          vecRouting.add(routingDetailsType);
                      }else{
                          vecRoutingDetails = new Vector();
                          vecRoutingDetails.add(routingDetailsType);
                          htLevelDetails.put(routingDetailsBean.getLevelNumber()+"",vecRoutingDetails);
                      }
                      
                  }
                  // Create level(Sequential Stop) collection and adding the routing details to each and every level
                  Vector vecLevelDetails = new Vector();
                  Iterator levelIterator = htLevelDetails.keySet().iterator();
                  while(levelIterator.hasNext()){
                      String levelNumber = (String)levelIterator.next();
                      Vector vecRoutingInfo = (Vector)htLevelDetails.get(levelNumber);
                      if(vecRoutingInfo != null && !vecRoutingInfo.isEmpty()){
                          RoutingDetailsType routingDetailsType = (RoutingDetailsType)vecRoutingInfo.get(0);
                          LevelDetailsType levelDetailsType = apprRoutingObjFact.createLevelDetailsType();
                          levelDetailsType.setLevelNumber(routingDetailsType.getLevelNumber());
                          levelDetailsType.setMapNumber(routingDetailsType.getMapNumber());
                          levelDetailsType.setRoutingNumber(routingDetailsType.getRoutingNumber());
                          levelDetailsType.getRoutingDetails().addAll(vecRoutingInfo);
                          vecLevelDetails.add(levelDetailsType);
                      }
                  }
                   routingMapsType.getLevelDetails().addAll(vecLevelDetails);
              }
              vecRoutingMapDetails.add(routingMapsType);
          }
      }
      routingDetails.getRoutingMaps().addAll(vecRoutingMapDetails);
      proposalType.setROUTINGDETAILS(routingDetails);
    }
    
    /**
     * Method to get the approval status description for approver
     * @param approvalStatus 
     * @return approvalStatusDesc
     */
    private String getRoutingApprovalStatusDesc(String approvalStatus){
        String approvalStatusDesc = "";
        if("W".equals(approvalStatus)){
            approvalStatusDesc = "Waiting for Approval";
        }else if("A".equals(approvalStatus)){
            approvalStatusDesc = "Approved";
        }else if("R".equals(approvalStatus)){
            approvalStatusDesc = "Rejected";
        }else if("P".equals(approvalStatus)){
            approvalStatusDesc = "Passed";
        }else if("L".equals(approvalStatus)){
            approvalStatusDesc = "Passed by other";
        }else if("O".equals(approvalStatus)){
            approvalStatusDesc = "Approved by other";
        }else if("B".equals(approvalStatus)){
            approvalStatusDesc = "Bypassed";
        }else if("J".equals(approvalStatus)){
            approvalStatusDesc = "Rejected by other";
        }else if("T".equals(approvalStatus)){
            approvalStatusDesc = "To be submitted";
        }
        return approvalStatusDesc;
    }
    
  
    /**
     * Method to order the map based on the map and parent map number
     * @param cvRoutingMaps 
     * @param vecFileteredMaps 
     * @param parentMapNumber 
     * @return vecFileteredMaps
     */
    private Vector getOrderedMap(CoeusVector cvRoutingMaps, Vector vecOrderedMaps ,int parentMapNumber){
        Vector vecFileteredMaps = new Vector();
        for(int index=0;index<cvRoutingMaps.size();index++){
            RoutingMapBean routingMapBean = (RoutingMapBean)cvRoutingMaps.get(index);
            if(routingMapBean.getParentMapNumber() == parentMapNumber){
                vecFileteredMaps.add(routingMapBean);
            }
        }
        
        for(int index = 0; index < vecFileteredMaps.size(); index++){
            RoutingMapBean routingMapBean = (RoutingMapBean) vecFileteredMaps.get(index);
            vecOrderedMaps.add(routingMapBean);
            getOrderedMap(cvRoutingMaps, vecOrderedMaps,routingMapBean.getMapNumber());
        }
        
        return vecOrderedMaps;
    }

     /**
     * Method to build the routing comments based on the map  
     * @param proposalnumber 
     * @param proposalType 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @throws java.text.ParseException 
     * @throws javax.xml.bind.JAXBException 
     */
    private void buildRoutingComments(String proposalnumber, PROPOSAL proposalType) throws CoeusException, DBException, ParseException, JAXBException{
        RoutingTxnBean routingTxnBean = new RoutingTxnBean();
        RoutingBean routingBean = routingTxnBean.getRoutingHeader(ModuleConstants.PROPOSAL_DEV_MODULE_CODE+"", proposalNumber,"0", 0);
        // sets the basic routing details
        RoutingType routingDetails = apprRoutingObjFact.createRoutingType();
        routingDetails.setApprovalSequence(routingBean.getApprovalSequence());
        routingDetails.setModuleCode(routingBean.getModuleCode());
        routingDetails.setModuleItemKeySequence(routingBean.getModuleItemKeySequence());
        routingDetails.setModuleItemkey(routingBean.getModuleItemKey());
        routingDetails.setRoutingStartDate(routingBean.getRoutingEndDate());
        routingDetails.setRoutingEndDate(routingBean.getRoutingEndDate());
        routingDetails.setRoutingStartUser(routingBean.getRoutingStartUser());
        routingDetails.setRoutingEndUser(routingBean.getRoutingEndUser());
        // Gets the routing map details with routing details
        CoeusVector cvRoutingMaps = routingTxnBean.getRoutingMaps(routingBean.getRoutingNumber());
        //  Orders the map collection based on the map and parent number
        Vector vecOrderedMaps  = null;
        if(cvRoutingMaps != null && !cvRoutingMaps.isEmpty()){
            vecOrderedMaps =  getOrderedMap(cvRoutingMaps, new Vector(), 0);
        }
        Vector vecRoutingMapDetails = new Vector();
        if(vecOrderedMaps != null && !vecOrderedMaps.isEmpty()){
            // Iterating each and every map
            for(Object routingMapDetails : vecOrderedMaps){
                RoutingMapBean routingMapBean = (RoutingMapBean)routingMapDetails;
                RoutingMapsType routingMapsType =  apprRoutingObjFact.createRoutingMapsType();
                routingMapsType.setApprovalStatus(routingMapBean.getApprovalStatus());
                routingMapsType.setDescription(routingMapBean.getDescription()) ;
                routingMapsType.setMapId(routingMapBean.getMapId());
                routingMapsType.setMapNumber(routingMapBean.getMapNumber());
                routingMapsType.setParentMapNumber(routingMapBean.getParentMapNumber());
                routingMapsType.setRoutingNumber(routingMapBean.getRoutingNumber());
                if(routingMapBean.getSystemFlag()){
                    routingMapsType.setSystemFlag("Y");
                }else{
                    routingMapsType.setSystemFlag("N");
                }
                
                CoeusVector cvMapDetails = routingTxnBean.getRoutingDetailsForMap(routingMapBean.getRoutingNumber(),routingMapBean.getMapNumber());
                // Iterating routing details for the map
                if(cvMapDetails != null && !cvMapDetails.isEmpty()){
                    Vector vecRoutingDetails = new Vector();;
                    LinkedHashMap  htLevelDetails = new LinkedHashMap();
                    for(Object routing : cvMapDetails){
                        RoutingDetailsBean routingDetailsBean = (RoutingDetailsBean)routing;
                        RoutingDetailsType routingDetailsType = apprRoutingObjFact.createRoutingDetailsType();
                        if(routingDetailsBean.getApprovalDate() != null){
                            Calendar approvalDateCal = Calendar.getInstance();
                            approvalDateCal.setTime(routingDetailsBean.getApprovalDate());
                            routingDetailsType.setApprovalDate(approvalDateCal);
                        }
                        routingDetailsType.setApprovalStatus(getRoutingApprovalStatusDesc(routingDetailsBean.getApprovalStatus()));
                        routingDetailsType.setApproverNumber(routingDetailsBean.getApproverNumber());
                        routingDetailsType.setDescription(routingDetailsBean.getDescription());
                        routingDetailsType.setLevelNumber(routingDetailsBean.getLevelNumber());
                        routingDetailsType.setMapNumber(routingDetailsBean.getMapNumber());
                        if(routingDetailsBean.isPrimaryApproverFlag()){
                            routingDetailsType.setPrimaryApproverFlag("Y");
                        }else{
                            routingDetailsType.setPrimaryApproverFlag("N");
                        }
                        routingDetailsType.setRoutingNumber(routingDetailsBean.getRoutingNumber());
                        routingDetailsType.setStopNumber(routingDetailsBean.getStopNumber());
                        if(routingDetailsBean.getSubmissionDate() != null){
                            Calendar submissionDateCal = Calendar.getInstance();
                            submissionDateCal.setTime(routingDetailsBean.getSubmissionDate());
                            routingDetailsType.setSubmissionDate(submissionDateCal);
                        }
                        routingDetailsType.setUserId(routingDetailsBean.getUserId());
                        routingDetailsType.setUserName(routingDetailsBean.getUserName());
                        CoeusVector cvRoutingComments = routingTxnBean.getRoutingComments(routingDetailsBean.getRoutingNumber(),
                                                           routingDetailsBean.getMapNumber(),
                                                            routingDetailsBean.getLevelNumber(),
                                                               routingDetailsBean.getStopNumber(),
                                                                routingDetailsBean.getApproverNumber());
                        Vector vecRoutingComments = new Vector();
                        if(cvRoutingComments != null && !cvRoutingComments.isEmpty()){
                            for(Object commentsDetails : cvRoutingComments){
                                RoutingCommentsBean routingCommentsDetails = (RoutingCommentsBean)commentsDetails;
                                RoutingCommentsDetails routingCommentsType = apprRoutingObjFact.createRoutingCommentsDetails();
                                routingCommentsType.setMapNameDescription(routingMapBean.getDescription());
                                routingCommentsType.setComments(routingCommentsDetails.getComments());
                                routingCommentsType.setUpdateUser(routingCommentsDetails.getUpdateUser());
                                if(routingDetailsBean.getApprovalDate() != null){
                                    routingCommentsType.setUpdateTimestamp(formatDate(routingCommentsDetails.getUpdateTimestamp()));
                                }
                                vecRoutingComments.add(routingCommentsType);
                            }
                        }
                        if(!vecRoutingComments.isEmpty()){
                            routingDetailsType.getRoutingComments().addAll(vecRoutingComments);
                            vecRoutingDetails.add(routingDetailsType);
                        }
                    }
                    routingMapsType.getRoutingDetails().addAll(vecRoutingDetails);
                }
                vecRoutingMapDetails.add(routingMapsType);
            }
        }
        routingDetails.getRoutingMaps().addAll(vecRoutingMapDetails);
        proposalType.setROUTINGCOMMENTS(routingDetails);
    }
    
}
