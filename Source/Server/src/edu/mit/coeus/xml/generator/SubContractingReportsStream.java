/*
 * @(#)SubContractingReportsStream.java August 2, 2005, 10:12 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */ 

package edu.mit.coeus.xml.generator;

import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.xml.bean.subcontractingReports.*;
import edu.mit.coeus.utils.CoeusVector;
import java.math.BigDecimal;
import java.util.*;
import javax.xml.bind.JAXBException;


import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.xml.generator.bean.SubReportsAmountsBean;
import edu.mit.coeus.xml.generator.bean.SubReportsTxnBean;
import edu.mit.coeus.xml.generator.bean.SubcontractingReportsDetailBean;
import edu.mit.coeus.xml.generator.bean.SubcontractingReportsInfoBean;


import java.sql.Date;


/**
 * @author  Eleanor Shavell
 * @Created on August 24, 2005, 10:12 AM
 * Class for generating the object stream for SubcontractingReports (294 and 295). It uses jaxb classes
 * which have been created under edu.mit.coeus.utils.xml.bean.subcontractingReports package. Fetch the data 
 * from database and attach with the jaxb beans which have been derived from 
 * subcontractingReports schema.
 */
public class SubContractingReportsStream extends ReportBaseStream{
    private ObjectFactory objFactory;
    private CoeusXMLGenrator xmlGenerator;
    private String awardNumber;
    private String formID;
    private String printType;
    private String startDate;
    private String endDate;
  
    private String sponsorGroup;
    private SubReportsTxnBean subReportsTxnBean;
    private SubcontractingReportsInfoBean subcontractingReportsInfoBean;
    private static final String packageName = "edu.mit.coeus.utils.xml.bean.subcontractingReports";
   
 
    /** Creates a new instance of SubContractingReportsStream */
    public SubContractingReportsStream(){
        
        objFactory = new ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();
        SubReportsTxnBean subReportsTxnBean = new SubReportsTxnBean();
  
    }
    

    public org.w3c.dom.Document getStream(java.util.Hashtable params) throws DBException,CoeusException {
        //get printType = REG, FIN, or REV
        this.printType = (String)params.get("PRINT_TYPE");
        //get formID = 294 or 295
        this.formID = (String)params.get("FORM_ID");
        
        if (formID.equals("294")) {
             this.awardNumber = (String)params.get("AWARD_NUM");
        }else {
             this.startDate = (String)params.get("START_DATE");
             this.endDate = (String)params.get("END_DATE");
        }
         
       
        SubcontractingReportsInfoBean subReportsBean = new SubcontractingReportsInfoBean();
        subReportsBean = getSubReportsData();
        
        SubcontractReportsType subcontractReportsType = getSubReportType(subReportsBean);
        
        return xmlGenerator.marshelObject(subcontractReportsType,packageName);
    }
    
//    public Object getObjectStream(Hashtable params) throws DBException,CoeusException{
//        //get printType = REG, FIN, or REV
//        this.printType = (String)params.get("PRINT_TYPE");
//        //get formID = 294 or 295
//        this.formID = (String)params.get("FORM_ID");
//        
//        if (formID.equals("294")) {
//             this.awardNumber = (String)params.get("AWARD_NUM");
//        }else {
//             this.startDate = (String)params.get("START_DATE");
//             this.endDate = (String)params.get("END_DATE");
//        }
//        
//        SubcontractingReportsInfoBean subReportsBean = new SubcontractingReportsInfoBean();
//        subReportsBean = getSubReportsData();
//        
//        SubcontractReportsType subcontractReportsType = getSubReportType(subReportsBean);
//        return subcontractReportsType;
//    }
    
    public Object getObjectStream(Hashtable params) throws DBException,CoeusException{
        Object retObject = null;
        
        //get printType = REG, FIN, or REV
        this.printType = (String)params.get("PRINT_TYPE");
        //get formID = 294 or 295
        this.formID = (String)params.get("FORM_ID");
        
        if (formID.equals("294")) {
             this.awardNumber = (String)params.get("AWARD_NUM");
        }else {
             this.startDate = (String)params.get("START_DATE");
             this.endDate = (String)params.get("END_DATE");
        }
        
        if(params.get("AWARD_NUMS") != null) {
            HashMap hashMap = new HashMap();
            SubcontractingReportsInfoBean subReportsBean;
            SubcontractReportsType subcontractReportsType;
            String awardNums[] = (String[])params.get("AWARD_NUMS");
            for(int index=0; index < awardNums.length; index++) {
                this.awardNumber = awardNums[index];
                subReportsBean = getSubReportsData();
                subcontractReportsType = getSubReportType(subReportsBean);
                hashMap.put(awardNums[index], subcontractReportsType);
            }
            LinkedHashMap linkedHashMap = new LinkedHashMap(hashMap);
            retObject = linkedHashMap;
        }else {
            
            SubcontractingReportsInfoBean subReportsBean = new SubcontractingReportsInfoBean();
            subReportsBean = getSubReportsData();
            
            SubcontractReportsType subcontractReportsType = getSubReportType(subReportsBean);
            retObject = subcontractReportsType;
        }
        
        return retObject;
        
    }
    
    
    private SubcontractingReportsInfoBean getSubReportsData() throws DBException,CoeusException{
       //call getSubReportsInfo, passing reportNumber,reportType,awardNumber,startDate,endDate
         subReportsTxnBean = new SubReportsTxnBean();
        if(formID.equals("294")) {
           
            return subReportsTxnBean.getSubReportsInfo("294",printType,awardNumber,"none","none");
    
        }else {    
        
            return subReportsTxnBean.getSubReportsInfo("295", printType, "none",startDate, endDate);
            }
    }
    
    private SubcontractReportsType getSubReportType(SubcontractingReportsInfoBean subReportsBean)
           throws CoeusXMLException,CoeusException,DBException{
               
        SubcontractingReportsDetailBean detailBean;
        
        SubcontractReportsType subReportsType = null;
        try{
            subReportsType = objFactory.createSubcontractReports();
            SubcontractReportsType.ReportingPeriodType repPeriodType = objFactory.createSubcontractReportsTypeReportingPeriodType();
            SubcontractReportsType.CompanyInfoType companyType = objFactory.createSubcontractReportsTypeCompanyInfoType();
            NameAndAddressTypeType compNameType = objFactory.createNameAndAddressTypeType();
            SubcontractReportsType.AdministeringOfficialType adminOfficial =objFactory.createSubcontractReportsTypeAdministeringOfficialType();
            SubcontractReportsType.CEOType ceo = objFactory.createSubcontractReportsTypeCEOType();
            SubcontractReportsType.ContractorProductsType contractorProd = objFactory.createSubcontractReportsTypeContractorProductsType();
            SubcontractReportsType.ContractorTypeType contractorType = objFactory.createSubcontractReportsTypeContractorTypeType();
            SubcontractReportsType.PlanTypeType planType = objFactory.createSubcontractReportsTypePlanTypeType();
            
            subReportsType.setReportNumber(formID);
            
            repPeriodType.setReportType(subReportsBean.getReportType());
            repPeriodType.setIsMarchReport(subReportsBean.getIsMarchReport());
            repPeriodType.setIsSeptReport(subReportsBean.getIsSeptReport());
            subReportsType.setReportingPeriod(repPeriodType);
            
            subReportsType.setIncludeIndirectCosts(false);
            subReportsType.setContractorIDNumber(subReportsBean.getContractorID());
            
            compNameType.setName(subReportsBean.getCompanyName());
            compNameType.setStreetAddress(subReportsBean.getCompanyStreetAddress());
            compNameType.setCity(subReportsBean.getCompanyCity());
            compNameType.setState(subReportsBean.getCompanyState());
            compNameType.setZipCode(subReportsBean.getCompanyZip());
            
            companyType.setNameAndAddressType(compNameType);
            subReportsType.setCompanyInfo(companyType);
            
            adminOfficial.setName(subReportsBean.getOfficialName());
            adminOfficial.setTitle(subReportsBean.getOfficialTitle());
            adminOfficial.setPhoneAreaCode(subReportsBean.getOfficialAreaCode());
            adminOfficial.setPhoneNumber(subReportsBean.getOfficialPhone());
            subReportsType.setAdministeringOfficial(adminOfficial);
            
            ceo.setName(subReportsBean.getCeoName());
            ceo.setTitle(subReportsBean.getCeoTitle());
            subReportsType.setCEO(ceo);
            
            contractorProd.setALine(" ");
            contractorProd.setBLine(" ");
            subReportsType.setContractorProducts(contractorProd);
            
            contractorType.setIsPrime(subReportsBean.getIsPrime());
            contractorType.setIsSub(subReportsBean.getIsSub());
            contractorType.setPrimeContractNumber(subReportsBean.getPrimeContractNumber());
            contractorType.setSubContractNumber(subReportsBean.getSubContractNumber());
            subReportsType.setContractorType(contractorType);
            
       
            subReportsType.setDateSubmitted(getCal(subReportsBean.getDateSubmitted()) );  
            //following might be start and end dates ..?
            subReportsType.setFiscalYearReportEnd(subReportsBean.getReportFY());
            subReportsType.setFiscalYearReportStart(subReportsBean.getReportFY());

            planType.setTypeofPlan(" ");
            planType.setPercentage(new BigDecimal("0"));
            subReportsType.setPlanType(planType);

            
            CoeusVector cvDetails = new CoeusVector();
            cvDetails = subReportsBean.getDetailInfo();
         
            //cvDetails is vector of SubcontractingReportsDetailBeans
            
            //for 294 there will be only one element in vector
            //for 295 there will me more than one - one for each administering activity
        
            for (int i=0; i<cvDetails.size(); i++){
                 detailBean = new SubcontractingReportsDetailBean();
                 detailBean = (SubcontractingReportsDetailBean) cvDetails.get(i);
                 subReportsType.getSubcontractReportPage().add(getDetails(subReportsBean,detailBean));
            }
        
        }catch (JAXBException jaxbEx){
            UtilFactory.log(jaxbEx.getMessage(),jaxbEx,"SubContractingReportsStream","getSubReportType()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }
        return subReportsType;
    }

       private SubcontractReportPageType getDetails(SubcontractingReportsInfoBean subReportsBean,
                                          SubcontractingReportsDetailBean detailBean)
        throws CoeusXMLException,CoeusException,DBException,JAXBException{
  
        SubcontractReportPageType reportPage = objFactory.createSubcontractReportPageType();
       
        SubcontractReportPageType.AwardingAgencyNameType agency = objFactory.createSubcontractReportPageTypeAwardingAgencyNameType();
        NameAndAddressTypeType agencyNameType = objFactory.createNameAndAddressTypeType();
        SubcontractReportPageType.VendorTypeType vendorType = objFactory.createSubcontractReportPageTypeVendorTypeType();
      
        reportPage.setAdministeringActivity(detailBean.getAdministeringActivity());
        reportPage.setSponsor(detailBean.getSponsor());
        reportPage.setRemarks(detailBean.getRemarks());
           
        if (formID.equals ("294")){
                agencyNameType.setName(detailBean.getAgencyName());
                agencyNameType.setStreetAddress(detailBean.getAgencyStreetAddress());
                agencyNameType.setCity(detailBean.getAgencyCity());
                agencyNameType.setState(detailBean.getAgencyState());
                agencyNameType.setZipCode(detailBean.getAgencyZip());
                agency.setNameAndAddressType(agencyNameType);
                reportPage.setAwardingAgencyName(agency);
        }
        
        CoeusVector cvVendorAmounts = detailBean.getVendorAmounts();
        //cvVendorAmounts is vector of subReportsAmountsBeans - there will be one 
        // subReportsAmountsBean for each vendor type (i.e. Small Bus, large Bus, etc)
           
        for (int i=0; i<cvVendorAmounts.size();i++){
             SubReportsAmountsBean amountsBean = (SubReportsAmountsBean) cvVendorAmounts.get(i);
             vendorType = objFactory.createSubcontractReportPageTypeVendorTypeType();
             vendorType.setTypeOfVendor(amountsBean.getVendorType());
             vendorType.setActualAmount(amountsBean.getAmount());
             vendorType.setActualPercent(amountsBean.getPercent());
             vendorType.setGoalAmount(amountsBean.getGoalAmount());
             vendorType.setGoalPercent(amountsBean.getGoalPercent());
            
             reportPage.getVendorType().add(vendorType);
         }
        
         
        return reportPage;
    }
  
    private static BigDecimal convDoubleToBigDec(double d){
        return new BigDecimal(d);
    }
    private Calendar getCal(java.util.Date date){
        if(date==null)
            return null;
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(date);
        return cal;
    }
    
                
}
