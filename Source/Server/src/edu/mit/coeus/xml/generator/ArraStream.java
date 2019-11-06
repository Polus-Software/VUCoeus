/*
 * ArraStream.java
 *
 * Created on September 22, 2009, 12:22 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.xml.generator;

import edu.mit.coeus.arra.bean.ArraAwardDetailsBean;
import edu.mit.coeus.arra.bean.ArraAwardSubcontractBean;
import edu.mit.coeus.arra.bean.ArraHighlyCompensatedBean;
import edu.mit.coeus.arra.bean.ArraReportTxnBean;
import edu.mit.coeus.arra.bean.ArraVendorBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexMaintenanceDataTxnBean;
import edu.mit.coeus.s2s.util.Converter;
import edu.mit.coeus.utils.Utils;  
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.xml.bean.arra.AddressType;
import edu.mit.coeus.utils.xml.bean.arra.AgencyCodeType;
import edu.mit.coeus.utils.xml.bean.arra.AwardCategoryType;
import edu.mit.coeus.utils.xml.bean.arra.AwardDetailType;
import edu.mit.coeus.utils.xml.bean.arra.CompensatedOfficer;
import edu.mit.coeus.utils.xml.bean.arra.CompensatedOfficerIndicatorType;
import edu.mit.coeus.utils.xml.bean.arra.CompleteTreasuryAccountSymbol;
import edu.mit.coeus.utils.xml.bean.arra.ContractActivityCode;
import edu.mit.coeus.utils.xml.bean.arra.ContractAwardDetailType;
import edu.mit.coeus.utils.xml.bean.arra.ContractPrimeRecipientReportType;
import edu.mit.coeus.utils.xml.bean.arra.ContractReport;
import edu.mit.coeus.utils.xml.bean.arra.ContractReportHeaderType;
import edu.mit.coeus.utils.xml.bean.arra.Date;
import edu.mit.coeus.utils.xml.bean.arra.FinalReportIndicatorType;
import edu.mit.coeus.utils.xml.bean.arra.FullTelephoneNumberType;
import edu.mit.coeus.utils.xml.bean.arra.GrantLoanActivityCodeType;
import edu.mit.coeus.utils.xml.bean.arra.GrantLoanAwardDetailType;
import edu.mit.coeus.utils.xml.bean.arra.GrantLoanPrimeRecipientReportType;
import edu.mit.coeus.utils.xml.bean.arra.GrantLoanReport;
import edu.mit.coeus.utils.xml.bean.arra.GrantLoanReportHeaderType;
import edu.mit.coeus.utils.xml.bean.arra.GrantLoanSubRecipientReportType;
import edu.mit.coeus.utils.xml.bean.arra.InfrastructureAddressType;
import edu.mit.coeus.utils.xml.bean.arra.InfrastructureContactType;
import edu.mit.coeus.utils.xml.bean.arra.LocationCountry;
import edu.mit.coeus.utils.xml.bean.arra.PlaceOfPerformanceType;
import edu.mit.coeus.utils.xml.bean.arra.PrimeRecipientType;
import edu.mit.coeus.utils.xml.bean.arra.ProjectStatusType;
import edu.mit.coeus.utils.xml.bean.arra.ReportHeaderType;
import edu.mit.coeus.utils.xml.bean.arra.SubAccountCode;
import edu.mit.coeus.utils.xml.bean.arra.SubRecipientReportType;
import edu.mit.coeus.utils.xml.bean.arra.SubRecipientType;
import edu.mit.coeus.utils.xml.bean.arra.TreasuryAccountSymbol;
import edu.mit.coeus.utils.xml.bean.arra.USStateCodeType;
import edu.mit.coeus.utils.xml.bean.arra.VendorType;
import edu.mit.coeus.utils.xml.bean.arra.impl.AddressTypeImpl;
import edu.mit.coeus.utils.xml.bean.arra.impl.AgencyCodeTypeImpl;
import edu.mit.coeus.utils.xml.bean.arra.impl.AwardCategoryTypeImpl;
import edu.mit.coeus.utils.xml.bean.arra.impl.CompensatedOfficerImpl;
import edu.mit.coeus.utils.xml.bean.arra.impl.CompensatedOfficerIndicatorTypeImpl;
import edu.mit.coeus.utils.xml.bean.arra.impl.CompleteTreasuryAccountSymbolImpl;
import edu.mit.coeus.utils.xml.bean.arra.impl.ContractActivityCodeImpl;
import edu.mit.coeus.utils.xml.bean.arra.impl.ContractAwardDetailTypeImpl;
import edu.mit.coeus.utils.xml.bean.arra.impl.ContractPrimeRecipientReportTypeImpl;
import edu.mit.coeus.utils.xml.bean.arra.impl.ContractReportHeaderTypeImpl;
import edu.mit.coeus.utils.xml.bean.arra.impl.ContractReportImpl;
import edu.mit.coeus.utils.xml.bean.arra.impl.DateImpl;
import edu.mit.coeus.utils.xml.bean.arra.impl.FinalReportIndicatorTypeImpl;
import edu.mit.coeus.utils.xml.bean.arra.impl.FullTelephoneNumberTypeImpl;
import edu.mit.coeus.utils.xml.bean.arra.impl.GrantLoanActivityCodeTypeImpl;
import edu.mit.coeus.utils.xml.bean.arra.impl.GrantLoanAwardDetailTypeImpl;
import edu.mit.coeus.utils.xml.bean.arra.impl.GrantLoanPrimeRecipientReportTypeImpl;
import edu.mit.coeus.utils.xml.bean.arra.impl.GrantLoanReportHeaderTypeImpl;
import edu.mit.coeus.utils.xml.bean.arra.impl.GrantLoanReportImpl;
import edu.mit.coeus.utils.xml.bean.arra.impl.GrantLoanSubRecipientReportTypeImpl;
import edu.mit.coeus.utils.xml.bean.arra.impl.InfrastructureAddressTypeImpl;
import edu.mit.coeus.utils.xml.bean.arra.impl.InfrastructureContactTypeImpl;
import edu.mit.coeus.utils.xml.bean.arra.impl.LocationCountryImpl;
import edu.mit.coeus.utils.xml.bean.arra.impl.PlaceOfPerformanceTypeImpl;
import edu.mit.coeus.utils.xml.bean.arra.impl.PrimeRecipientTypeImpl;
import edu.mit.coeus.utils.xml.bean.arra.impl.ProjectStatusTypeImpl;
import edu.mit.coeus.utils.xml.bean.arra.impl.SubAccountCodeImpl;
import edu.mit.coeus.utils.xml.bean.arra.impl.SubRecipientReportTypeImpl;
import edu.mit.coeus.utils.xml.bean.arra.impl.SubRecipientTypeImpl;
import edu.mit.coeus.utils.xml.bean.arra.impl.TreasuryAccountSymbolImpl;
import edu.mit.coeus.utils.xml.bean.arra.impl.USStateCodeTypeImpl;
import edu.mit.coeus.utils.xml.bean.arra.impl.VendorTypeImpl;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

/**
 *
 * @author keerthyjayaraj
 */
public class ArraStream extends ReportBaseStream {
    
    private static final String packageName = "edu.mit.coeus.utils.xml.bean.arra";
    private static final String EMPTY_STRING = "";
    private static final String ARRA_HIGHLY_COMP_ORG_ID = "000001";
    private static final String CONTRACT_REPORT = "CONTRACT_REPORT";
    private static final String GRANT_LOAN_REPORT = "GRANT_LOAN_REPORT";
    private static final String YES = "Yes";
    private static final String NO = "No";
    HashMap hmCountryCodes = new HashMap();
    Vector subContractVendors = null;
    // Added for COEUSDEV-624: ARRA - XML file generated for contracts has wrong award type
    public static final String CONTRACT_AWARD_TYPE = "Federally Awarded Contract";
    public static final String GRANT_AWARD_TYPE = "Grant";
    // COEUSDEV-624: End
    /** Creates a new instance of ArraStream */
    public ArraStream() {
    }
    
    /**
     * Is used to get the protocol object in the form of org.w3c.dom.Document
     * @param Hashtable - expects the following keys - REPORT_TYPE,ARRA_REPORT_NUMBER,MIT_AWARD_NUMBER
     * @throws DBException, CoeusException
     */
    public org.w3c.dom.Document getStream(Hashtable params) throws DBException, CoeusException {
        
        String reportType = (String)params.get("REPORT_TYPE");
        if(CONTRACT_REPORT.equals(reportType)){
            ContractReport contractReportType = new ContractReportImpl();
            contractReportType = getContractReportData(contractReportType, params);
            CoeusXMLGenrator xmlGenerator = new CoeusXMLGenrator();
            return xmlGenerator.marshelObject(contractReportType, packageName);
        }else if(GRANT_LOAN_REPORT.equals(reportType)){
            GrantLoanReport grantLoanReportType = new GrantLoanReportImpl();
            grantLoanReportType = getGrantLoanReportData(grantLoanReportType, params);
            CoeusXMLGenrator xmlGenerator = new CoeusXMLGenrator();
            return xmlGenerator.marshelObject(grantLoanReportType, packageName);
        }else return null;
    }
    
    /**
     * Is used to get the QuestionnaireType object after populating all the
     * required data.
     * @param params Hashtable - expects the following keys - REPORT_TYPE,ARRA_REPORT_NUMBER,MIT_AWARD_NUMBER
     * @return Object
     * @throws DBException
     * @throws CoeusException
     */
    public Object getObjectStream(Hashtable params) throws DBException, CoeusException{
        String reportType = (String)params.get("REPORT_TYPE");
        if(CONTRACT_REPORT.equals(reportType)){
            ContractReport contractReportType = new ContractReportImpl();
            return getContractReportData(contractReportType, params);
        }else if(GRANT_LOAN_REPORT.equals(reportType)){
            GrantLoanReport grantLoanReportType = new GrantLoanReportImpl();
            return getGrantLoanReportData(grantLoanReportType, params);
        }else return null;
    }
    
    /**
     * Is used to populate the data to ContractReport object.
     * @param params Hashtable
     * @param contractReportType ContractReport object
     * @return ContractReport object
     * @throws DBException
     * @throws CoeusException
     */
    private ContractReport getContractReportData(ContractReport contractReportType, Hashtable params) throws DBException, CoeusException {
        
        int arraReportNumber = Integer.parseInt(params.get("ARRA_REPORT_NUMBER").toString());
        String mitAwardNumber = params.get("MIT_AWARD_NUMBER").toString();
        
        ArraReportTxnBean txnBean = new ArraReportTxnBean();
        ArraAwardDetailsBean bean = txnBean.getArraAwardDetails(arraReportNumber,mitAwardNumber);
        hmCountryCodes = txnBean.getCountryCodes();
        if(bean!=null){
            //Add the three basic blocks
            contractReportType.setContractReportHeader(getContractReportHeader(bean));
            contractReportType.setContractPrimeRecipientReport(getContractPrimeRecipientReport(bean));
            Vector vctSubs = getContractSubrecipientReports(bean);
            if(vctSubs!=null && !vctSubs.isEmpty()){
                contractReportType.getSubRecipientReport().addAll(vctSubs);
            }
        }
        return contractReportType;
    }
    
    /**
     * Is used to populate the data to GrantLoanReport object.
     * @param params Hashtable
     * @param grantLoanReportType GrantLoanReport object
     * @return GrantLoanReport object
     * @throws DBException
     * @throws CoeusException
     */
    private GrantLoanReport getGrantLoanReportData(GrantLoanReport grantLoanReportType, Hashtable params) throws DBException, CoeusException {
        
        int arraReportNumber = Integer.parseInt(params.get("ARRA_REPORT_NUMBER").toString());
        String mitAwardNumber = params.get("MIT_AWARD_NUMBER").toString();
        int versionNumber = 0;
        if(params.get("VERSION_NUMBER") != null){
            versionNumber = Integer.parseInt(params.get("VERSION_NUMBER").toString());
        }
        ArraReportTxnBean txnBean = new ArraReportTxnBean();
        ArraAwardDetailsBean bean = new ArraAwardDetailsBean();
        if(versionNumber != 0){
            bean = txnBean.getArraAwardDetailsForVersion(arraReportNumber,mitAwardNumber,versionNumber);
        }else{
            bean = txnBean.getArraAwardDetails(arraReportNumber,mitAwardNumber);
        }
        
        hmCountryCodes = txnBean.getCountryCodes();
        if(bean!=null){
            //Add the four basic blocks
            grantLoanReportType.setGrantLoanReportHeader(getGrantLoanReportHeader(bean));
            grantLoanReportType.setGrantLoanPrimeRecipientReport(getGrantLoanPrimeRecipientReport(bean));
            Vector vctData = getGrantLoanSubrecipientReports(bean);
            if(vctData!=null && !vctData.isEmpty()){
                grantLoanReportType.getGrantLoanSubRecipientReport().addAll(vctData);
            }
            vctData = getGrantLoanVendors(bean);
            if(vctData!=null && !vctData.isEmpty()){
                grantLoanReportType.getVendor().addAll(vctData);
            }
        }
        return grantLoanReportType;
    }
    
    
    /* Contract Report Header block - Start */
    private ContractReportHeaderType getContractReportHeader(ArraAwardDetailsBean bean) {
        ContractReportHeaderType contractReportHeaderType = new ContractReportHeaderTypeImpl();
        
        AwardCategoryType awardCategoryType = new AwardCategoryTypeImpl();
//        String awardType = bean.getAwardType()==null?"Federally Awarded Contract":bean.getAwardType();//award type is not nillable
        // Added for COEUSDEV-624: ARRA - XML file generated for contracts has wrong award type
        String awardType = bean.getAwardType();
        if(GRANT_AWARD_TYPE.equalsIgnoreCase(awardType)){
            awardType = GRANT_AWARD_TYPE;
        }else{
            awardType = CONTRACT_AWARD_TYPE;
        }
        // COEUSDEV-624:End
        awardCategoryType.setValue(awardType);
        contractReportHeaderType.setContractAwardCategory(awardCategoryType);
        contractReportHeaderType.setOrderNumber(bean.getOrderNumber());
        
        getReportHeader(contractReportHeaderType,bean);
        return contractReportHeaderType;
    }
    
    /* Grant Loan Report Header block - Start */
    private GrantLoanReportHeaderType getGrantLoanReportHeader(ArraAwardDetailsBean bean) {
        GrantLoanReportHeaderType grantLoanReportHeaderType = new GrantLoanReportHeaderTypeImpl();
        
        AwardCategoryType awardCategoryType = new AwardCategoryTypeImpl();
//        String awardType = bean.getAwardType()==null?EMPTY_STRING:bean.getAwardType();//award type is not nillable
        // Added for COEUSDEV-624: ARRA - XML file generated for contracts has wrong award type
        String awardType = bean.getAwardType();
        if(GRANT_AWARD_TYPE.equalsIgnoreCase(awardType)){
            awardType = GRANT_AWARD_TYPE;
        }else{
            awardType = CONTRACT_AWARD_TYPE;
        }
        // COEUSDEV-624: End
        awardCategoryType.setValue(awardType);
        grantLoanReportHeaderType.setGrantLoanAwardCategory(awardCategoryType);
        
        getReportHeader(grantLoanReportHeaderType,bean);
        return grantLoanReportHeaderType;
    }
    
    private ReportHeaderType getReportHeader(ReportHeaderType reportHeaderType,ArraAwardDetailsBean bean){
        String recipientDuns = bean.getRecipientDUNSNumber()==null?EMPTY_STRING:bean.getRecipientDUNSNumber();//recipient duns is not nillable
        reportHeaderType.setPrimeRecipientDUNS(recipientDuns);
        String AwardIdNumber = bean.getAwardNumber()==null?EMPTY_STRING:bean.getAwardNumber();//award no is not nillable
        reportHeaderType.setAwardIdNumber(AwardIdNumber);
        return reportHeaderType;
    }
    /* Report Header block - End */
    
    /* Prime recipient block for contract report block - Start */
    private ContractPrimeRecipientReportType getContractPrimeRecipientReport(ArraAwardDetailsBean bean) throws DBException, CoeusException {
        
        ContractPrimeRecipientReportType contractPrimeRecipientReportType = new ContractPrimeRecipientReportTypeImpl();
        contractPrimeRecipientReportType.setContractAwardDetail(getContractAwardDetail(bean));
        contractPrimeRecipientReportType.setPrimeRecipient(getPrimeRecipient(bean));
        contractPrimeRecipientReportType.setPlaceOfPerformance(getPlaceOfPerformance(bean.getPrimPlaceOfPerfId(),bean.getPrimPlaceCongDistrict()));
        return contractPrimeRecipientReportType;
    }
    
    /* Prime recipient block for Grant Loan Report - Start */
    private GrantLoanPrimeRecipientReportType getGrantLoanPrimeRecipientReport(ArraAwardDetailsBean bean) throws DBException, CoeusException {
        
        GrantLoanPrimeRecipientReportType grantLoanPrimeRecipientReportType = new GrantLoanPrimeRecipientReportTypeImpl();
        grantLoanPrimeRecipientReportType.setGrantLoanAwardDetail(getGrantLoanAwardDetail(bean));
        grantLoanPrimeRecipientReportType.setPrimeRecipient(getPrimeRecipient(bean));
        grantLoanPrimeRecipientReportType.setPlaceOfPerformance(getPlaceOfPerformance(bean.getPrimPlaceOfPerfId(),bean.getPrimPlaceCongDistrict()));
        return grantLoanPrimeRecipientReportType;
    }
    
    /*Award Detail block for Contract Report*/
    private ContractAwardDetailType getContractAwardDetail(ArraAwardDetailsBean bean) {
        ContractAwardDetailType contractAwardDetailType = new ContractAwardDetailTypeImpl();
        contractAwardDetailType.setGovernmentContractingOfficeCode(bean.getGovContractingOfficeCode());
        String actCode = bean.getActivityCode();
        if(actCode!=null){
            ContractActivityCode contractActivityCode = new ContractActivityCodeImpl();
            contractActivityCode.setValue(actCode);
            contractAwardDetailType.getContractActivityCode().add(contractActivityCode);
        }
        getAwardDetail(contractAwardDetailType,bean);
        return contractAwardDetailType;
    }
    
    /*Award Detail block for Grant Loan Report*/
    private GrantLoanAwardDetailType getGrantLoanAwardDetail(ArraAwardDetailsBean bean) throws CoeusException, DBException {
        GrantLoanAwardDetailType grantLoanAwardDetailType = new GrantLoanAwardDetailTypeImpl();
        grantLoanAwardDetailType.setProjectName(bean.getProjectTitle());
        grantLoanAwardDetailType.setCFDAProgramNumber(bean.getCfdaNumber());
        String actCode = bean.getActivityCode();
        if(actCode!=null){
            GrantLoanActivityCodeType grantLoanActivityCode = new GrantLoanActivityCodeTypeImpl();
            grantLoanActivityCode.setValue(actCode);
            grantLoanAwardDetailType.getGrantLoanActivityCode().add(grantLoanActivityCode);
        }
        grantLoanAwardDetailType.setTotalFederalARRAExpenditure(convDoubleToBigDec(bean.getTotalExpenditure()));
        grantLoanAwardDetailType.setTotalFederalARRAInfraExpenditure(convDoubleToBigDec(bean.getTotalInfraExpenditure()));
        grantLoanAwardDetailType.setInfrastructureRationale(bean.getInfrastructureRationale());
        String infraContactId = bean.getInfraContactId();
        if(infraContactId!=null){
            grantLoanAwardDetailType.setInfrastructureContact(getInfraStructureContact(infraContactId));
        }
        grantLoanAwardDetailType.setTotalNumberVendorPayments(new BigDecimal(bean.getVendorLess25K()));
        grantLoanAwardDetailType.setTotalAmountVendorPayments(convDoubleToBigDec(bean.getVendorLess25KAmount()));
        getAwardDetail(grantLoanAwardDetailType,bean);
        return grantLoanAwardDetailType;
    }
    
    /*Common award details Block */
    private AwardDetailType getAwardDetail(AwardDetailType contractAwardDetailType,ArraAwardDetailsBean bean ){
        String fundingAgencyCode = bean.getFundingAgencyCode();
        AgencyCodeType agencyCodeType;
        if(fundingAgencyCode!=null){
            agencyCodeType = new AgencyCodeTypeImpl();
            agencyCodeType.setValue(fundingAgencyCode);
            contractAwardDetailType.setFundingAgencyCode(agencyCodeType);
        }
        String awardingAgency = bean.getAwardingAgencyCode();
        if(awardingAgency!=null){
            agencyCodeType = new AgencyCodeTypeImpl();
            agencyCodeType.setValue(awardingAgency);
            contractAwardDetailType.setAwardingAgencyCode(agencyCodeType);
        }
        
        String finalReportIndicator = bean.getFinalReportIndicator();
        if(finalReportIndicator!=null){
            FinalReportIndicatorType finalIndicator = new FinalReportIndicatorTypeImpl();
            finalIndicator.setValue(finalReportIndicator);
            contractAwardDetailType.setFinalReportIndicator(finalIndicator);
        }
        
        String sTAS = bean.getAgencyTAS();
        if(sTAS!=null){
            CompleteTreasuryAccountSymbol treasuryAccountSymbol = new CompleteTreasuryAccountSymbolImpl();
            TreasuryAccountSymbol TAS = new TreasuryAccountSymbolImpl();
            TAS.setValue(sTAS);
            treasuryAccountSymbol.setTreasuryAccountSymbol(TAS);
            contractAwardDetailType.setCompleteTreasuryAccountSymbol(treasuryAccountSymbol);
            if(bean.getTasSubCode() != null && bean.getTasSubCode().trim().length() > 0) {
                treasuryAccountSymbol.setSubAccountCode(bean.getTasSubCode());
            }
        }
        
        if(bean.getAwardDate()!=null){
            Calendar cal = Calendar.getInstance();
            cal.setTime(bean.getAwardDate());
            
            Date date = new DateImpl();
            date.setValue(cal);
            contractAwardDetailType.setAwardDate(date);
        }
        //Setting AwardDescription and ActivityDescription
        String awardDesc = bean.getAwardDescription();
        if(awardDesc!=null){
            /* A newline (line feed) character ('\n'),
             * A carriage-return character followed immediately by a newline character ("\r\n"),
             * A standalone carriage-return character ('\r'),
             * A next-line character ('\u0085'),
             * A line-separator character ('\u2028'), or
             * A paragraph-separator character ('\u2029).
             */
            //JIRA COEUSDEV-637 - START
            awardDesc = awardDesc.replaceAll("[\n\r\r\n\u0085\u2028\u2029]"," ");
            //JIRA COEUSDEV-637 - END
            contractAwardDetailType.setAwardDescription(awardDesc);
            
        }        
        contractAwardDetailType.setAwardAmount(convDoubleToBigDec(bean.getAwardAmount()));
        String activityDesc = bean.getActivityDescription();
        if(activityDesc!=null){
            //JIRA COEUSDEV-637 - START
            activityDesc = activityDesc.replaceAll("[\n\r\r\n\u0085\u2028\u2029]"," ");
            //JIRA COEUSDEV-637 - END
            contractAwardDetailType.setProjectDescription(activityDesc);
        }
        String projectStatus = bean.getProjectStatus();
        if(projectStatus!=null){
            ProjectStatusType statusType = new ProjectStatusTypeImpl();
            statusType.setValue(projectStatus);
            contractAwardDetailType.setProjectStatus(statusType);
        }
        double noOfJobs = bean.getNoOfJobs();
        double jobsAtSubs =bean.getJobsAtSubs();
        double jobsCreated= noOfJobs + jobsAtSubs;
        contractAwardDetailType.setJobCreationNumber(convDoubleToBigDec(jobsCreated));
        contractAwardDetailType.setJobCreationNarrative(bean.getEmploymentImpact() == null || bean.getEmploymentImpact().trim().length() == 0 ? "No jobs to report at this time" : bean.getEmploymentImpact().trim());
        contractAwardDetailType.setTotalFederalARRAReceived(convDoubleToBigDec(bean.getTotalFederalInvoiced()));
        contractAwardDetailType.setTotalNumberSubawardsIndividuals(new BigDecimal(bean.getIndSubAwards()));
        contractAwardDetailType.setTotalAmountSubawardsIndividuals(convDoubleToBigDec(bean.getIndSubAwardAmount()));
        contractAwardDetailType.setTotalNumberSmallSubawards(new BigDecimal(bean.getSubAwdLess25K()));
        contractAwardDetailType.setTotalAmountSmallSubawards(convDoubleToBigDec(bean.getSubAwdLess25KAmount()));
        return contractAwardDetailType;
    }
    
    private InfrastructureContactType getInfraStructureContact(String infraContactId) throws CoeusException, DBException {
        
        RolodexMaintenanceDataTxnBean rolodexTxnBean = new RolodexMaintenanceDataTxnBean();
        InfrastructureContactType infraContact = new InfrastructureContactTypeImpl();
        RolodexDetailsBean rolodexDetails = rolodexTxnBean.getRolodexMaintenanceDetails(infraContactId);
        
        if(rolodexDetails!=null){
            String contactName =  EMPTY_STRING;
            String tempContact = rolodexDetails.getLastName();
            if(tempContact != null && !tempContact.equals(EMPTY_STRING)){ contactName = contactName+tempContact;}
            tempContact = rolodexDetails.getSuffix();
            if(tempContact != null && !tempContact.equals(EMPTY_STRING)){ contactName = contactName+" "+tempContact;}
            tempContact =rolodexDetails.getPrefix();
            if(tempContact != null && !tempContact.equals(EMPTY_STRING)){ contactName = contactName+" "+tempContact;}
            tempContact = rolodexDetails.getFirstName();
            if(tempContact != null && !tempContact.equals(EMPTY_STRING)){ contactName = contactName+" "+tempContact;}
            tempContact = rolodexDetails.getMiddleName();
            if(tempContact != null && !tempContact.equals(EMPTY_STRING)){ contactName = contactName+" "+tempContact+" ";}
            //setting name
            infraContact.setContactName(contactName);
            //setting address
            InfrastructureAddressType addressType = new InfrastructureAddressTypeImpl();
            //addressDeliveryPointText maxoccurs = 3
            tempContact = rolodexDetails.getAddress1();
            if(tempContact != null && !tempContact.equals(EMPTY_STRING)){
                tempContact = (tempContact.length()>55)?tempContact.substring(0,55):tempContact;
                addressType.getAddressDeliveryPointText().add(tempContact);
            }
            tempContact = rolodexDetails.getAddress2();
            if(tempContact != null && !tempContact.equals(EMPTY_STRING)){
                tempContact = (tempContact.length()>55)?tempContact.substring(0,55):tempContact;
                addressType.getAddressDeliveryPointText().add(tempContact);
            }
            tempContact = rolodexDetails.getAddress3();
            if(tempContact != null && !tempContact.equals(EMPTY_STRING)){
                tempContact = (tempContact.length()>55)?tempContact.substring(0,55):tempContact;
                addressType.getAddressDeliveryPointText().add(tempContact);
            }
            addressType.setLocationCityName(rolodexDetails.getCity());
            String[] postalCodes = getPostalCodes(rolodexDetails.getPostalCode());
            addressType.setLocationPostalCode(postalCodes[0]);
            addressType.setLocationPostalExtensionCode(postalCodes[1]);
            String state = rolodexDetails.getState();
            if(state!=null){
                USStateCodeType usStateCodeType = new USStateCodeTypeImpl();
                usStateCodeType.setValue(rolodexDetails.getState());
                addressType.setLocationState(usStateCodeType);
            }
            infraContact.setInfrastructureAddress(addressType);
            //setting emailid
            infraContact.setContactEmailID(rolodexDetails.getEMail());
            //Setting full telephone no
            String fullTelephone = rolodexDetails.getPhone();
            if(fullTelephone!=null){
                fullTelephone = fullTelephone.replaceAll("[-,\" \"]","");
            }
            FullTelephoneNumberType fullTelephoneNumberType = new FullTelephoneNumberTypeImpl();
            fullTelephoneNumberType.setTelephoneNumberFullID(fullTelephone);
//            fullTelephoneNumberType.setTelephoneSuffixID(rolodexDetails.getPhone());
            infraContact.setFullTelephoneNumber(fullTelephoneNumberType);
        }
        return infraContact;
    }
    
    private PrimeRecipientType getPrimeRecipient(ArraAwardDetailsBean bean) throws DBException, CoeusException {
        PrimeRecipientType primeRecipientType = new PrimeRecipientTypeImpl();
        primeRecipientType.setCongressionalDistrict(bean.getRecipientCongDistrict());
        primeRecipientType.setAccountNumber(bean.getAccountNumber());
        primeRecipientType.getCompensatedOfficer().addAll(getCompensatedOfficer(ARRA_HIGHLY_COMP_ORG_ID));
        
//        //String repIndication = bean.getIndicationOfReporting() == null ? "No":bean.getIndicationOfReporting();
        String compensatedOfficerIndicator = NO;
        boolean compensatedOfficePresent = checkCompensatedOfficerIsPresent(ARRA_HIGHLY_COMP_ORG_ID);
        if(compensatedOfficePresent){
            compensatedOfficerIndicator = YES;
        }
        
        if(compensatedOfficerIndicator!=null){
            CompensatedOfficerIndicatorType compensatedOfficerIndicatorType = new CompensatedOfficerIndicatorTypeImpl();
            compensatedOfficerIndicatorType.setValue(compensatedOfficerIndicator);
            primeRecipientType.setCompensatedOfficerIndicator(compensatedOfficerIndicatorType);
        }
        return primeRecipientType;
    }
    
    private Vector getCompensatedOfficer(String orgId) throws DBException, CoeusException {
        Vector compOfficers = new Vector();
        
        CompensatedOfficer compensatedOfficer;
        ArraHighlyCompensatedBean highlyCompensatedBean;
        ArraReportTxnBean txnBean = new ArraReportTxnBean();
        Vector vctCom = txnBean.getArraHighlyCompensated(orgId);
        if(vctCom!=null && !vctCom.isEmpty()){
            int recCount = vctCom.size()>5?5:vctCom.size();
            for(int i=0; i<recCount; i++){//top 5 highly compensated individuals(maxOccurs = 5)
                highlyCompensatedBean = (ArraHighlyCompensatedBean)vctCom.get(i);
                compensatedOfficer = new CompensatedOfficerImpl();
                compensatedOfficer.setPersonFullName(highlyCompensatedBean.getPersonName());
                compensatedOfficer.setOfficerCompensation(convDoubleToBigDec(highlyCompensatedBean.getCompensation()));
                compOfficers.add(compensatedOfficer);
            }
        }
        return compOfficers;
    }
    
    
    private boolean checkCompensatedOfficerIsPresent(String orgId) throws DBException, CoeusException {
        boolean present = false;
        int noOfOfficers = 0;
       
        ArraReportTxnBean txnBean = new ArraReportTxnBean();
        Vector vecOfficers = txnBean.getArraHighlyCompensated(orgId);
        if(vecOfficers!=null && !vecOfficers.isEmpty()){
            noOfOfficers = vecOfficers.size();
        }
        
        if(noOfOfficers > 0){
            present = true;
        }
        return present;
    }
    
    
    /* To get the place of perfomance details */
    private PlaceOfPerformanceType getPlaceOfPerformance(String placeId, String congDistrict) throws DBException, CoeusException {
        PlaceOfPerformanceType placeOfPerformanceType = new PlaceOfPerformanceTypeImpl();
        if(placeId!=null){
            placeOfPerformanceType.setAddress(getAddress(placeId));
        }
        placeOfPerformanceType.setCongressionalDistrict(congDistrict);
        return placeOfPerformanceType;
    }
    
    /* To get the address info */
    private AddressType getAddress(String rolodexID) throws DBException, CoeusException {
        RolodexMaintenanceDataTxnBean rolodexTxnBean = new RolodexMaintenanceDataTxnBean();
        AddressType addressType = new AddressTypeImpl();
        RolodexDetailsBean rolodexDetails = rolodexTxnBean.getRolodexMaintenanceDetails(rolodexID);
        if(rolodexDetails!=null){
            //addressDeliveryPointText maxoccurs = 2
            String tempContact = rolodexDetails.getAddress1();
            if(tempContact != null && !tempContact.equals(EMPTY_STRING)){
                tempContact = (tempContact.length()>55)?tempContact.substring(0,55):tempContact;
                addressType.getAddressDeliveryPointText().add(tempContact);
            }
            tempContact = rolodexDetails.getAddress2();
            if(tempContact != null && !tempContact.equals(EMPTY_STRING)){
                tempContact = (tempContact.length()>55)?tempContact.substring(0,55):tempContact;
                addressType.getAddressDeliveryPointText().add(tempContact);
            }
            addressType.setLocationCityName(rolodexDetails.getCity());
            
            String countryCode = rolodexDetails.getCountry();
            if(countryCode!=null && hmCountryCodes!=null){
                String arraCountryCode = (String)hmCountryCodes.get(countryCode);
                if(arraCountryCode!=null){
                    LocationCountry locationCountry = new LocationCountryImpl();
                    locationCountry.setValue(arraCountryCode);
                    addressType.setLocationCountry(locationCountry);
                }
            }
            String[] postalCodes = getPostalCodes(rolodexDetails.getPostalCode());
            addressType.setLocationPostalCode(postalCodes[0]);
            addressType.setLocationPostalExtensionCode(postalCodes[1]);
            String state = rolodexDetails.getState();
            if(state!=null){
                USStateCodeType usStateCodeType = new USStateCodeTypeImpl();
                usStateCodeType.setValue(rolodexDetails.getState());
                addressType.setLocationState(usStateCodeType);
            }
        }
        return addressType;
    }
    
    private Vector getContractSubrecipientReports(ArraAwardDetailsBean bean) throws DBException, CoeusException {
        Vector subrecipientReports = new Vector();
        ArraReportTxnBean txnBean = new ArraReportTxnBean();
        ArraAwardSubcontractBean subcontract;
        SubRecipientReportType subRecipientReport ;
        
        Vector vctSubs = txnBean.getArraAwardSubcontracts(bean.getArraReportNumber(),bean.getMitAwardNumber());
        if(vctSubs!=null && !vctSubs.isEmpty()){
            for(int i=0;i<vctSubs.size();i++){
                subcontract = (ArraAwardSubcontractBean)vctSubs.get(i);
                subRecipientReport = new SubRecipientReportTypeImpl();
                setSubcontractRecipientDetails(subcontract,subRecipientReport);
                subrecipientReports.add(subRecipientReport);
            }
        }
        
        return subrecipientReports;
    }
    
    /**Subcontract Recipient details for grant loan Report*/
    private Vector getGrantLoanSubrecipientReports(ArraAwardDetailsBean bean) throws DBException, CoeusException {
        Vector subrecipientReports = new Vector();
        ArraReportTxnBean txnBean = new ArraReportTxnBean();
        ArraAwardSubcontractBean subcontract;
        GrantLoanSubRecipientReportType subRecipientReport ;
        int versionNumber = bean.getVersionNumber();
        Vector vctSubs = null;
        if(versionNumber != 0){
            vctSubs = txnBean.getArraAwardSubcontractsForVersion(bean.getArraReportNumber(),bean.getMitAwardNumber(),versionNumber);
        }else{
            vctSubs = txnBean.getArraAwardSubcontracts(bean.getArraReportNumber(),bean.getMitAwardNumber());
        }
        vctSubs = txnBean.getArraAwardSubcontracts(bean.getArraReportNumber(),bean.getMitAwardNumber());
        if(vctSubs!=null && !vctSubs.isEmpty()){
            subContractVendors = new Vector();
            for(int i=0;i<vctSubs.size();i++){
                subcontract = (ArraAwardSubcontractBean)vctSubs.get(i);
                String subcontractCode = subcontract.getSubcontractCode();
                if(subcontractCode!=null){
                    subContractVendors.addAll(getVendorList(bean,subcontractCode,subcontract.getSubcontractNo()));
                }
                subRecipientReport = new GrantLoanSubRecipientReportTypeImpl();
                subRecipientReport.setSubAwardAmountDisbursed(convDoubleToBigDec(subcontract.getSubAwardAmtDispursed()));
                setSubcontractRecipientDetails(subcontract,subRecipientReport);
                subrecipientReports.add(subRecipientReport);
            }
        }
        return subrecipientReports;
    }
    
    /*Common subcontract recipient details*/
    private SubRecipientReportType setSubcontractRecipientDetails( ArraAwardSubcontractBean subcontract, SubRecipientReportType subRecipientReport) throws DBException, CoeusException{
        
        subRecipientReport.setSubAwardNumber(subcontract.getSubcontractNo());
        subRecipientReport.setAwardAmount(convDoubleToBigDec(subcontract.getSubAwardAmount()));
        if(subcontract.getSubAwardDate()!=null){
            Calendar cal = Calendar.getInstance();
            cal.setTime(subcontract.getSubAwardDate());
            Date date = new DateImpl();
            date.setValue(cal);
            subRecipientReport.setAwardDate(date);
        }
        subRecipientReport.setSubRecipient(getSubRecipient(subcontract));
        subRecipientReport.setPlaceOfPerformance(getPlaceOfPerformance(subcontract.getPrimPlaceOfPerfId(),subcontract.getPrimPlaceCongDist()));
        return subRecipientReport;
    }
    
     /*Common subcontract recipient details*/
    private SubRecipientType getSubRecipient(ArraAwardSubcontractBean subcontract) throws DBException, CoeusException {
        SubRecipientType subRecipientType = new SubRecipientTypeImpl();
        subRecipientType.setCongressionalDistrict(subcontract.getSubRecipientCongDist());
        subRecipientType.getCompensatedOfficer().addAll(getCompensatedOfficer(subcontract.getSubcontractorID()));
        subRecipientType.setSubRecipientDUNS(subcontract.getSubRecipientDUNS());
        //String compensatedOfficerIndicator = subcontract.getIndicationOfReporting() == null ? "No":subcontract.getIndicationOfReporting();
        boolean compensatedOfficerPresent = checkCompensatedOfficerIsPresent(subcontract.getSubcontractorID());
        String compensatedOfficerIndicator = NO;
        if(compensatedOfficerPresent){
            compensatedOfficerIndicator = YES;
        }
        
        if(compensatedOfficerIndicator!=null){
            CompensatedOfficerIndicatorType compensatedOfficerIndicatorType = new CompensatedOfficerIndicatorTypeImpl();
            compensatedOfficerIndicatorType.setValue(compensatedOfficerIndicator);
            subRecipientType.setCompensatedOfficerIndicator(compensatedOfficerIndicatorType);
        }
        return subRecipientType;
    }
    
    /* Subcontract recipients block - End */
     /*Vendor Details for grant loan report*/
    private Vector getGrantLoanVendors(ArraAwardDetailsBean bean) throws DBException {
        Vector awardVendors = getVendorList(bean,null,null);
        if(subContractVendors!=null && !subContractVendors.isEmpty()){
            awardVendors.addAll(subContractVendors);
        }
        return awardVendors;
    }
    
    /* To fetch vendor data from table */
    private Vector getVendorList(ArraAwardDetailsBean bean,String subContractCode,String subContractNumber) throws DBException {
        Vector vctVendors = new Vector();
        ArraReportTxnBean txnBean = new ArraReportTxnBean();
        ArraVendorBean vendor = null;
        VendorType vendorType = null;
        Vector vctData = new Vector();
        String AwardIdNumber = bean.getAwardNumber()==null?EMPTY_STRING:bean.getAwardNumber();//award no is not nillable
        int versionNumber = bean.getVersionNumber();
        if(versionNumber != 0){
            vctData = txnBean.getArraVendorsForVersion(bean.getArraReportNumber(),bean.getMitAwardNumber(),versionNumber,subContractCode);
        }else{
            vctData = txnBean.getArraVendors(bean.getArraReportNumber(),bean.getMitAwardNumber(),subContractCode);
        }
        if(vctData!=null && !vctData.isEmpty()){
            String[] postalCodes;
            for(int i=0;i<vctData.size();i++){
                vendor = (ArraVendorBean)vctData.get(i);
                vendorType = new VendorTypeImpl();
                vendorType.setAwardIdNumber(AwardIdNumber);
                vendorType.setSubAwardNumber(subContractNumber);
                vendorType.setOrganizationName(vendor.getVendorName());
                postalCodes = getPostalCodes(vendor.getVendorHQZipCode());
                vendorType.setLocationPostalCode(postalCodes[0]);
                vendorType.setLocationPostalExtensionCode(postalCodes[1]);
                vendorType.setVendorDUNS(vendor.getVendorDUNS());
                vendorType.setProductServiceDescription(vendor.getServiceDescription());
                vendorType.setPaymentAmount(convDoubleToBigDec(vendor.getPaymentAmount()));
                vctVendors.add(vendorType);
            }
        }
        return vctVendors;
    }
    
    /* This function is to convert double value to bigdecimal
     * with 2 precision digits.
     */
    private static BigDecimal convDoubleToBigDec(double d){
        return (new BigDecimal(d)).setScale(2,BigDecimal.ROUND_HALF_DOWN);
    }
    
    /* This function is to extract the postal code details.
     * @ param fullPostalCode - The full postal code as specified in Coeus.
     * fullPostalCode can be null or of the format xxxxx or xxxxx-yyyy
     * @ return postalCodes[] - where postalCodes[0] holds the location postal code - the first 5 chars
     *                           and postalCodes[1] holds the postal extension code - the last 4 chars
     */
    private String[] getPostalCodes(String fullPostalCode){
        String[] postalCodes = {null,null};
        if(fullPostalCode!=null && !EMPTY_STRING.equals(fullPostalCode.trim())){
            fullPostalCode = fullPostalCode.trim();
            postalCodes[0] = (fullPostalCode.length()>=5)?fullPostalCode.substring(0,5):fullPostalCode.substring(0);
            postalCodes[1] = (fullPostalCode.length()>5)?Utils.replaceString(fullPostalCode.substring(5),"-",""):"0000";
        }
        return postalCodes;
        
    }
    
    public synchronized String replaceAll(Document doc) throws CoeusException{
        String tempAppXml = doc2String(doc);
        tempAppXml = Converter.replaceAll(tempAppXml,"\\+00:00","");
        return tempAppXml;
    }
    
    private synchronized String doc2String(Document node)
    throws CoeusException{
        try {
            DOMSource domSource = new DOMSource(node);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            return writer.toString();
            
        } catch (Exception e) {
            e.printStackTrace();
            UtilFactory.log(e.getMessage(),e,"ArraValidator", "doc2bytes");
            throw new CoeusException(e.getMessage());
        }
    }
}
