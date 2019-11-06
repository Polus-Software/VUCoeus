/*
 * SubcontractFdpReportsStream.java
 *
 * Created on February 1, 2012, 3:07 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.xml.generator;

import edu.mit.coeus.award.bean.AwardBean;
import edu.mit.coeus.award.bean.AwardCommentsBean;
import edu.mit.coeus.award.bean.AwardDetailsBean;
import edu.mit.coeus.award.bean.AwardHeaderBean;
import edu.mit.coeus.award.bean.AwardInvestigatorsBean;
import edu.mit.coeus.award.bean.AwardLookUpDataTxnBean;
import edu.mit.coeus.award.bean.AwardReportTxnBean;
import edu.mit.coeus.award.bean.AwardTxnBean;
import edu.mit.coeus.bean.CoeusTypeBean;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.irb.bean.PersonInfoFormBean;
import edu.mit.coeus.irb.bean.PersonInfoTxnBean;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceDataTxnBean;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceFormBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexMaintenanceDataTxnBean;
import edu.mit.coeus.subcontract.bean.RTFFormBean;
import edu.mit.coeus.subcontract.bean.SubContractAmountInfoBean;
import edu.mit.coeus.subcontract.bean.SubContractAmountReleased;
import edu.mit.coeus.subcontract.bean.SubContractBean;
import edu.mit.coeus.subcontract.bean.SubContractCustomDataBean;
import edu.mit.coeus.subcontract.bean.SubContractFundingSourceBean;
import edu.mit.coeus.subcontract.bean.SubContractTxnBean;
import edu.mit.coeus.subcontract.bean.SubcontractCloseoutBean;
import edu.mit.coeus.subcontract.bean.SubcontractContactDetailsBean;
import edu.mit.coeus.subcontract.bean.SubcontractReportBean;
import edu.mit.coeus.subcontract.bean.SubcontractTemplateInfoBean;
import edu.mit.coeus.unit.bean.UnitDataTxnBean;
import edu.mit.coeus.unit.bean.UnitDetailFormBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.utils.xml.bean.subcontractfdpreports.award.AwardHeaderType;
import edu.mit.coeus.utils.xml.bean.subcontractfdpreports.award.AwardType;
import edu.mit.coeus.utils.xml.bean.subcontractfdpreports.award.impl.AwardHeaderTypeImpl;
import edu.mit.coeus.utils.xml.bean.subcontractfdpreports.award.impl.AwardTypeImpl;
import edu.mit.coeus.utils.xml.bean.subcontractfdpreports.subcontract.*;
import edu.mit.coeus.utils.xml.bean.subcontractfdpreports.subcontract.impl.SubContractDataImpl;
import edu.mit.coeus.utils.xml.bean.subcontractfdpreports.subcontract.impl.SubContractDataTypeImpl;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import javax.xml.bind.JAXBException;

/**
 *
 * @author mohann
 */
public class SubcontractFdpReportsStream  extends ReportBaseStream{
    private edu.mit.coeus.utils.xml.bean.subcontractfdpreports.subcontract.ObjectFactory subContractObjFactory;
    private edu.mit.coeus.utils.xml.bean.subcontractfdpreports.award.ObjectFactory awardObjFactory;
    private CoeusVector cvContactTypes = null;
    
    /** Creates a new instance of SubcontractFdpReportsStream */
    
    public SubcontractFdpReportsStream() {
    }
    
    public Object getObjectStream(Hashtable htData) throws DBException,CoeusException{
        
        String loggedInUser = (String)htData.get("LOGGED_IN_USER");
        String reportType = (String)htData.get("REPORT_TYPE");
        
        if(loggedInUser == null || reportType == null) {
            CoeusException coeusException = new CoeusException();
            coeusException.setMessage("No data found");
            throw coeusException;
        }
        
        Object object = null;
        try {
            object = getSubcontractFdpDetails(htData);
        } catch (Exception ex) {
            CoeusException coeusException = new CoeusException();
            coeusException.setMessage(ex.getMessage());
            throw coeusException;
        }
        
        return object;
    }
    
    
    
    /**
     * Methos to set FDP details
     * @param params 
     * @throws edu.mit.coeus.xml.generator.CoeusXMLException 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws javax.xml.bind.JAXBException 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @throws java.text.ParseException 
     * @throws java.io.IOException 
     * @return subContractDataType
     */
    private Object getSubcontractFdpDetails(Hashtable params) throws CoeusXMLException, CoeusException, JAXBException, CoeusException, DBException, ParseException, IOException{
        String subContractCode = null;
        SubContractBean   subContractBean  =   (SubContractBean)params.get(CoeusConstants.SUBCONTRACT_BEAN);
        String queryKey = subContractBean.getSubContractCode() + subContractBean.getSequenceNumber();
        QueryEngine.getInstance().addDataCollection(queryKey,(Hashtable)params.get(CoeusConstants.SUBCONTRACT_DATA_COLLECTION));
        params.remove(CoeusConstants.SUBCONTRACT_DATA_COLLECTION);
        
        SubContractDataType subContractDataType = new SubContractDataImpl();
        
        subContractObjFactory = new edu.mit.coeus.utils.xml.bean.subcontractfdpreports.subcontract.ObjectFactory();
        awardObjFactory = new edu.mit.coeus.utils.xml.bean.subcontractfdpreports.award.ObjectFactory();
        
        SubContractTxnBean subContractTxnBean = new SubContractTxnBean();
        
        //Load Contact type details
        getContactTypes();
        
        subContractDataType = (SubContractDataType) getSubcontractDetais(subContractBean, subContractDataType);
        
        subContractDataType = (SubContractDataType) getPrintRequirements(params, subContractDataType);
        
        subContractDataType = (SubContractDataType) getSchoolInfo(subContractDataType);
        
        subContractCode = subContractBean.getSubContractCode();
        
        // Get the data for the Subcontract contacts details - Added by chandra
        CoeusVector cvRolodexData = subContractTxnBean.getSubcontractContacts(subContractCode);
        
        subContractDataType = (SubContractDataType) getSubcontractContactDetails(cvRolodexData, subContractBean, subContractDataType);
        
        //CloseOut details
        subContractDataType = (SubContractDataType) getSubcontractCloseOutDetails(subContractBean, subContractDataType);
        
        // FundingSource Details.
        subContractDataType = (SubContractDataType) getSubcontractFundingSourceDetails(subContractBean, subContractDataType);
        
        //AmountInfo Details
        subContractDataType = (SubContractDataType) getSubcontractAmountInfoDetails(subContractBean, subContractDataType);
        
        // Amount Released Details
        subContractDataType = (SubContractDataType) getSubcontractAmountReleasedDetails(subContractBean, subContractDataType);
        
        // Auditor Details
        subContractDataType = (SubContractDataType) getSubcontractAuditorDetails(subContractBean, subContractDataType);
        
        // Get Person Details for OSP_ADMINSTRATOR & ADMINISTRATIVE_OFFICER from Unit table
        subContractDataType = (SubContractDataType) getSubcontractOspAndAdminDetails(subContractBean, subContractDataType);
        
        // Get Principal Investigator Details
        subContractDataType = (SubContractDataType) getSubcontractPIDetails(subContractBean, subContractDataType);
        
        //Custom data
        subContractDataType = (SubContractDataType) getSubcontractOthersDataDetails(subContractBean, subContractDataType);
        
        // SubcontractTemplateInfo
        subContractDataType = (SubContractDataType) getSubcontractTemplateInfo(subContractBean, subContractDataType);
        
        // Subcontract Report details
        subContractDataType = (SubContractDataType) getSubcontractReport(subContractBean, subContractDataType);
        
        // Award Details
        SubContractFundingSourceBean subContractFundingSourceBean = (SubContractFundingSourceBean)params.get(CoeusConstants.SUBCONTRACT_SELECTED_FUNDING_SOURCE);
        if(subContractFundingSourceBean != null){
            subContractDataType = (SubContractDataType) getAwardDetails(subContractBean, subContractDataType,subContractFundingSourceBean);
            subContractDataType = (SubContractDataType) getPrimePrincipalInvestigatorDetails(subContractBean, subContractDataType,subContractFundingSourceBean);
        }
        
        subContractDataType = (SubContractDataType) getPrimeAdministrativeContact(cvRolodexData, subContractBean, subContractDataType);
        
        subContractDataType = (SubContractDataType) getPrimeFinancialContact(cvRolodexData, subContractBean, subContractDataType);
        
        subContractDataType = (SubContractDataType) getPrimeAuthorizedOfficial(cvRolodexData, subContractBean, subContractDataType);
        
        subContractDataType = (SubContractDataType) getAdministrativeContact(cvRolodexData, subContractBean, subContractDataType);
        
        subContractDataType = (SubContractDataType) getFinancialContact(cvRolodexData, subContractBean, subContractDataType);
        
        subContractDataType = (SubContractDataType) getAuthorizedOfficial(cvRolodexData, subContractBean, subContractDataType);
        
        subContractDataType = (SubContractDataType) getPrimeRecipientContactsDetail(subContractBean, subContractDataType);
        
        return subContractDataType;
    }
    
    
    /**
     * Method to set subcontract details
     * @param subContractBean 
     * @param subContractDataType 
     * @throws javax.xml.bind.JAXBException 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @throws java.text.ParseException 
     * @return subContractDataType
     */
    private SubContractDataType getSubcontractDetais(SubContractBean subContractBean, SubContractDataType subContractDataType) throws JAXBException, CoeusException, DBException, ParseException{
        SubContractDataType.SubcontractDetailType subcontractDetailType = new SubContractDataTypeImpl.SubcontractDetailTypeImpl();
        subcontractDetailType.setAccountNumber(subContractBean.getAccountNumber());
        
        if (subContractBean.getAccountNumber() != null) {
            subcontractDetailType.setAccountNumber(subContractBean.getAccountNumber());
        }
        if (subContractBean.getArchiveLocation() != null) {
            subcontractDetailType.setArchiveLoc(subContractBean.getArchiveLocation());
        }
        
        if (subContractBean.getCloseOutDate()!=null) {
            Calendar closeDate= null;
            closeDate = Calendar.getInstance();
            closeDate.setTime(subContractBean.getCloseOutDate());
            subcontractDetailType.setCloseoutDate(closeDate);
        }
        if (subContractBean.getCloseOutIndicator() != null) {
            subcontractDetailType.setCloseoutIndicator(subContractBean.getCloseOutIndicator());
        }
        if(subContractBean.getComments() != null) {
            subcontractDetailType.setComments(subContractBean.getComments());
        }
        Calendar endDate= null;
        if (subContractBean.getEndDate()!=null) {
            endDate = Calendar.getInstance();
            endDate.setTime(subContractBean.getEndDate());
            subcontractDetailType.setEndDate(endDate);
        }
        if (subContractBean.getFundingSourceIndicator() != null) {
            subcontractDetailType.setFundingSourceIndicator(subContractBean.getFundingSourceIndicator());
        }
        if (subContractBean.getPurchaseOrderNumber() != null) {
            subcontractDetailType.setPONumber(subContractBean.getPurchaseOrderNumber());
        }
        if (subContractBean.getRequisitionerId() != null) {
            subcontractDetailType.setRequistionerID(subContractBean.getRequisitionerId());
        }
        if (subContractBean.getRequisitionerName() != null) {
            subcontractDetailType.setRequistionerName(subContractBean.getRequisitionerName());
        }
        if (subContractBean.getRequisitionerUnit() != null) {
            subcontractDetailType.setRequistionerUnit(subContractBean.getRequisitionerUnit());
        }
        
        Calendar startDate= null;
        if (subContractBean.getStartDate()!=null) {
            startDate = Calendar.getInstance();
            startDate.setTime(subContractBean.getStartDate());
            subcontractDetailType.setStartDate(startDate);
        }
        if (subContractBean.getSubAwardTypeDescription() != null) {
            subcontractDetailType.setSubAwardType(subContractBean.getSubAwardTypeDescription());
        }
        if (subContractBean.getSubContractorName() != null) {
            subcontractDetailType.setSubcontractorName(subContractBean.getSubContractorName());
        }
        if (subContractBean.getTitle() != null) {
            subcontractDetailType.setTitle(subContractBean.getTitle());
        }
        if (subContractBean.getVendorNumber() != null) {
            subcontractDetailType.setVendorNumber(subContractBean.getVendorNumber());
        }
        subcontractDetailType.setSubContractCode(subContractBean.getSubContractCode());
        
        subcontractDetailType.setSequenceNumber( String.valueOf(subContractBean.getSequenceNumber()));
        
        if(subContractBean.getSubContractId() !=null ){
            subcontractDetailType.setSubcontractorID( subContractBean.getSubContractId());
        }
        
        //SubAwardTypeCode
        subcontractDetailType.setSubAwardTypeCode(String.valueOf(subContractBean.getSubAwardTypeCode()));
        //StatusCode
        subcontractDetailType.setStatusCode(String.valueOf(subContractBean.getStatusCode()));
        
        if(subContractBean.getSiteInvestigatorName() !=null){
            subcontractDetailType.setSiteInvestigator(subContractBean.getSiteInvestigatorName());
        }
        
        if(subContractBean.getNegotiationNumber() != null){
            subcontractDetailType.setNegotiationNumber(subContractBean.getNegotiationNumber());
        }
        if(subContractBean.getCostTypeDescription() != null){
            subcontractDetailType.setCostType(subContractBean.getCostTypeDescription());
        }
        
        if(subContractBean.getDateOfFullyExecuted() != null){
            subcontractDetailType.setDateOfFullyExcecuted(formatDate(subContractBean.getDateOfFullyExecuted()));
        }
        
        if(subContractBean.getRequistionNumber() != null){
            subcontractDetailType.setRequisitionNumber(subContractBean.getRequistionNumber());
        }
        subcontractDetailType.setSubcontractorDetails(getSubcontractorDetails(subContractBean,  subContractDataType));
        
        subcontractDetailType.setSubcontractorOrgRolodexDetails(getSubcontractorOrgRolodexDetails(subContractBean,  subContractDataType));
        
        subcontractDetailType.setSiteInvestigatorDetails(getSiteInvestigatorDetails(subContractBean,  subContractDataType));
        
        subContractDataType.setSubcontractDetail(subcontractDetailType);
        
        return subContractDataType;
    }
    
    /**
     * MEthos to set contact details
     * @param cvRolodexData 
     * @param subContractBean 
     * @param subContractDataType 
     * @throws javax.xml.bind.JAXBException 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return subContractDataType
     */
    private SubContractDataType getSubcontractContactDetails(CoeusVector cvRolodexData, SubContractBean subContractBean, SubContractDataType subContractDataType) throws JAXBException, CoeusException, DBException {
        
        SubContractDataType.SubcontractContactsType subcontractContactsType;
        SubContractDataType.SubcontractContactsType.RolodexDetailsType rolodexType;
        
        if (cvRolodexData != null && !cvRolodexData.isEmpty()) {
            for(Object obj : cvRolodexData){
                subcontractContactsType =   subContractObjFactory.createSubContractDataTypeSubcontractContactsType();
                rolodexType = subContractObjFactory.createSubContractDataTypeSubcontractContactsTypeRolodexDetailsType();
                
                SubcontractContactDetailsBean subcontractContactDetailsBean = (SubcontractContactDetailsBean) obj;
                subcontractContactsType.setContactTypeCode(String.valueOf(subcontractContactDetailsBean.getContactTypeCode()));
                if (subcontractContactDetailsBean.getContactTypeDescription() != null) {
                    subcontractContactsType.setContactTypeDesc(subcontractContactDetailsBean.getContactTypeDescription());
                }
                subcontractContactsType.setSubcontractCode(subcontractContactDetailsBean.getSubContractCode());
                subcontractContactsType.setSequenceNumber(String.valueOf(subcontractContactDetailsBean.getSequenceNumber()));
                
                if(subcontractContactDetailsBean.getContactTypeDescription() !=null){
                    subcontractContactsType.setContactTypeDesc(subcontractContactDetailsBean.getContactTypeDescription());
                }
                
                rolodexType.setRolodexName(getRolodexName(subcontractContactDetailsBean));
                if (subcontractContactDetailsBean.getAddress1() != null) {
                    rolodexType.setAddress1(subcontractContactDetailsBean.getAddress1());
                }
                if (subcontractContactDetailsBean.getAddress2() != null) {
                    rolodexType.setAddress2(subcontractContactDetailsBean.getAddress2());
                }
                if (subcontractContactDetailsBean.getAddress3() != null) {
                    rolodexType.setAddress3(subcontractContactDetailsBean.getAddress3());
                }
                if (subcontractContactDetailsBean.getCity() != null) {
                    rolodexType.setCity(subcontractContactDetailsBean.getCity());
                }
                if (subcontractContactDetailsBean.getComments() != null) {
                    rolodexType.setComments(subcontractContactDetailsBean.getComments());
                }
                if (subcontractContactDetailsBean.getCountryCode() != null) {
                    rolodexType.setCountryCode(subcontractContactDetailsBean.getCountryCode());
                }
                if (subcontractContactDetailsBean.getCountryName() != null) {
                    rolodexType.setCountryDescription(subcontractContactDetailsBean.getCountryName());
                }
                if (subcontractContactDetailsBean.getCounty() != null) {
                    rolodexType.setCounty(subcontractContactDetailsBean.getCounty());
                }
                if (subcontractContactDetailsBean.getEmailAddress() != null) {
                    rolodexType.setEmail(subcontractContactDetailsBean.getEmailAddress());
                }
                if (subcontractContactDetailsBean.getFaxNumber() != null) {
                    rolodexType.setFax(subcontractContactDetailsBean.getFaxNumber());
                }
                if (subcontractContactDetailsBean.getFirstName() != null) {
                    rolodexType.setFirstName(subcontractContactDetailsBean.getFirstName());
                }
                if (subcontractContactDetailsBean.getLastName() != null) {
                    rolodexType.setLastName(subcontractContactDetailsBean.getLastName());
                }
                if (subcontractContactDetailsBean.getMiddleName() != null) {
                    rolodexType.setMiddleName(subcontractContactDetailsBean.getMiddleName());
                }
                if (subcontractContactDetailsBean.getOrganization() != null) {
                    rolodexType.setOrganization(subcontractContactDetailsBean.getOrganization());
                }
                
                rolodexType.setRolodexId(String.valueOf(subcontractContactDetailsBean.getRolodexId()));
                if(subcontractContactDetailsBean.getStateName() !=null){
                    rolodexType.setStateDescription(subcontractContactDetailsBean.getStateName());
                }
                
                int rolodxId = subcontractContactDetailsBean.getRolodexId();
                RolodexMaintenanceDataTxnBean rolodexMaintenanceDataTxnBean = new RolodexMaintenanceDataTxnBean();
                String rolodexId = String.valueOf(rolodxId);
                RolodexDetailsBean rolodexBean = rolodexMaintenanceDataTxnBean.getRolodexMaintenanceDetails(rolodexId);
                if (rolodexBean.getOwnedByUnit() != null) {
                    rolodexType.setOwnedByUnit(rolodexBean.getOwnedByUnit());
                }
                if (rolodexBean.getPhone() != null) {
                    rolodexType.setPhoneNumber(rolodexBean.getPhone());
                }
                if (rolodexBean.getPostalCode() != null) {
                    rolodexType.setPincode(rolodexBean.getPostalCode());
                }
                if (rolodexBean.getPrefix() != null) {
                    rolodexType.setPrefix(rolodexBean.getPrefix());
                }
                if (rolodexBean.getSponsorCode() != null) {
                    rolodexType.setSponsorCode(rolodexBean.getSponsorCode());
                }
                if (rolodexBean.getSponsorName() != null) {
                    rolodexType.setSponsorName(rolodexBean.getSponsorName());
                }
                if (rolodexBean.getState() != null) {
                    rolodexType.setStateCode(rolodexBean.getState());
                }
                if (rolodexBean.getSuffix() != null) {
                    rolodexType.setSuffix(rolodexBean.getSuffix());
                }
                if (rolodexBean.getTitle() != null) {
                    rolodexType.setTitle(rolodexBean.getTitle());
                }
                
                if(rolodexBean.getCountry() !=null ){
                    rolodexType.setCounty(rolodexBean.getCountry());
                }
                if(rolodexBean.getOwnedByUnit() !=null){
                    UnitDataTxnBean unitDataTxnBean = new UnitDataTxnBean();
                    UnitDetailFormBean unitDetailFormBean = unitDataTxnBean.getUnitDetails(rolodexBean.getOwnedByUnit());
                    unitDetailFormBean.getUnitName();
                    if (unitDetailFormBean.getUnitName() != null) {
                        rolodexType.setOwnedByUnitName(unitDetailFormBean.getUnitName());
                    }
                }
                subcontractContactsType.setRolodexDetails(rolodexType);
                subContractDataType.getSubcontractContacts().add(subcontractContactsType);
            }
        }
        
        
        return subContractDataType;
    }
    
    /**
     * Method to set close out details
     * @param subContractBean 
     * @param subContractDataType 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @throws javax.xml.bind.JAXBException 
     * @return subContractDataType
     */
    private SubContractDataType getSubcontractCloseOutDetails(SubContractBean subContractBean, SubContractDataType subContractDataType) throws CoeusException, DBException, JAXBException{
        String queryKey = subContractBean.getSubContractCode()+subContractBean.getSequenceNumber();
        QueryEngine queryEngine = QueryEngine.getInstance();
        CoeusVector cvCloseoutData = queryEngine.getDetails(queryKey, SubcontractCloseoutBean.class);
        
        CoeusVector cvCloseOutTypes = queryEngine.getDetails(queryKey, KeyConstants.CLOSEOUT_TYPES);
        HashMap hmCloseOutTypes = new HashMap();
        if(cvCloseOutTypes !=null && cvCloseOutTypes.size() > 0){
            for (int index =0; index < cvCloseOutTypes.size(); index++){
                ComboBoxBean comboBoxBean = (ComboBoxBean) cvCloseOutTypes.get(index);
                hmCloseOutTypes.put(comboBoxBean.getCode(), comboBoxBean.getDescription());
            }
        }
        
        SubContractDataType.CloseoutDetailsType closeOutType;
        if (cvCloseoutData != null && cvCloseoutData.size() > 0) {
            for (int index = 0; index < cvCloseoutData.size(); index++) {
                closeOutType = subContractObjFactory.createSubContractDataTypeCloseoutDetailsType();
                
                SubcontractCloseoutBean subcontractCloseoutBean = (SubcontractCloseoutBean)cvCloseoutData.get(index);
                closeOutType.setCloseoutNumber(String.valueOf(subcontractCloseoutBean.getCloseoutNumber()));
                closeOutType.setCloseoutTypeCode(String.valueOf(subcontractCloseoutBean.getCloseoutTypeCode()));
                if (subcontractCloseoutBean.getComment() != null) {
                    closeOutType.setComments(subcontractCloseoutBean.getComment());
                }
                
                Calendar dateFollowUp = null;
                if (subcontractCloseoutBean.getDateFollowUp() !=null) {
                    dateFollowUp = Calendar.getInstance();
                    dateFollowUp.setTime(subcontractCloseoutBean.getDateFollowUp());
                    closeOutType.setDateFollowup(dateFollowUp);
                }
                Calendar dateReceived = null;
                if (subcontractCloseoutBean.getDateReceived() !=null) {
                    dateReceived = Calendar.getInstance();
                    dateReceived.setTime(subcontractCloseoutBean.getDateReceived());
                    closeOutType.setDateReceived(dateReceived);
                }
                Calendar dateRequested = null;
                if (subcontractCloseoutBean.getDateRequested() !=null) {
                    dateRequested = Calendar.getInstance();
                    dateRequested.setTime(subcontractCloseoutBean.getDateRequested());
                    closeOutType.setDateRequested(dateRequested);
                }
                
                if(subcontractCloseoutBean.getSubContractCode() !=null){
                    closeOutType.setSubcontractCode(subcontractCloseoutBean.getSubContractCode());
                }
                
                closeOutType.setSequencenumber(String.valueOf(subcontractCloseoutBean.getSequenceNumber()));
                // CloseoutTypeDesc
                if(hmCloseOutTypes !=null && hmCloseOutTypes.size() > 0){
                    if( hmCloseOutTypes.get(String.valueOf(subcontractCloseoutBean.getCloseoutTypeCode())) !=null){
                        closeOutType.setCloseoutTypeDesc(hmCloseOutTypes.get(String.valueOf(subcontractCloseoutBean.getCloseoutTypeCode())).toString());
                    }
                }
                
                subContractDataType.getCloseoutDetails().add(closeOutType);
            }
        }
        return   subContractDataType;
    }
    
    /**
     * Methos to set funding source details
     * @param subContractBean 
     * @param subContractDataType 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws javax.xml.bind.JAXBException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return subContractDataType
     */
    private SubContractDataType getSubcontractFundingSourceDetails(SubContractBean subContractBean, SubContractDataType subContractDataType) throws CoeusException, JAXBException, DBException {
        String queryKey = subContractBean.getSubContractCode()+subContractBean.getSequenceNumber();
        QueryEngine queryEngine = QueryEngine.getInstance();
        CoeusVector cvFundingSourceData = queryEngine.getDetails(queryKey, SubContractFundingSourceBean.class);
        
        SubContractDataType.FundingSourceType fundingSourceType;
        //case 2901 end
        if (cvFundingSourceData != null && !cvFundingSourceData.isEmpty()) {
            for (int index = 0; index < cvFundingSourceData.size(); index++) {
                fundingSourceType = subContractObjFactory.createSubContractDataTypeFundingSourceType();
                SubContractFundingSourceBean subContractFundingSourceBean = (SubContractFundingSourceBean)cvFundingSourceData.get(index);
                if (subContractFundingSourceBean.getAccountNumber() != null) {
                    fundingSourceType.setAccountNumber(subContractFundingSourceBean.getAccountNumber());
                }
                BigDecimal amountDecimal = new BigDecimal(subContractFundingSourceBean.getObligatedAmount());
                fundingSourceType.setAmount(amountDecimal.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                
                if (subContractFundingSourceBean.getMitAwardNumber() != null) {
                    fundingSourceType.setAwardNumber(subContractFundingSourceBean.getMitAwardNumber());
                }
                
                Calendar finalExpirationDate = null;
                if (subContractFundingSourceBean.getFinalExpirationDate()!=null) {
                    finalExpirationDate = Calendar.getInstance();
                    finalExpirationDate.setTime(subContractFundingSourceBean.getFinalExpirationDate());
                    fundingSourceType.setFinalExpirationDate(finalExpirationDate);
                }
                
                if (subContractFundingSourceBean.getSponsorName() != null) {
                    fundingSourceType.setSponsor(subContractFundingSourceBean.getSponsorName());
                }
                if (subContractFundingSourceBean.getStatusDescription() != null) {
                    fundingSourceType.setStatus(subContractFundingSourceBean.getStatusDescription());
                }
                
                //set SubcontractorCode
                if(subContractFundingSourceBean.getSubContractCode() !=null){
                    fundingSourceType.setSubcontractorCode(subContractFundingSourceBean.getSubContractCode());
                }
                //set SequenceNumber
                fundingSourceType.setSequenceNumber(String.valueOf(subContractFundingSourceBean.getSequenceNumber()));
                
                //Set Sponsor Award Number
                if ( subContractFundingSourceBean.getMitAwardNumber() !=null ){
                    SubContractTxnBean subContractTxnBean = new SubContractTxnBean();
                    String sponsorAwardNumber = subContractTxnBean.getSponsorAwardNumber(subContractFundingSourceBean.getMitAwardNumber());
                    if(sponsorAwardNumber !=null && !sponsorAwardNumber.equals("")){
                        fundingSourceType.setSponsorAwardNumber(sponsorAwardNumber);
                    }
                }
                subContractDataType.getFundingSource().add(fundingSourceType);
            }
        }
        return subContractDataType;
    }
    
    /**
     * Method to set amount info detailss
     * @param subContractBean 
     * @param subContractDataType 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return subContractDataType
     */
    private SubContractDataType getSubcontractAmountInfoDetails(SubContractBean subContractBean, SubContractDataType subContractDataType) throws CoeusException, DBException {
        String queryKey = subContractBean.getSubContractCode()+subContractBean.getSequenceNumber();
        QueryEngine queryEngine = QueryEngine.getInstance();
        CoeusVector cvSubContractAmountInfoData = queryEngine.getDetails(queryKey, SubContractAmountInfoBean.class);
        
        SubContractDataType.SubcontractAmountInfoType amountInfoType = new SubContractDataTypeImpl.SubcontractAmountInfoTypeImpl();
        double obligatedAmt = 0 ;
        double anticipatedAmt = 0 ;
        if (cvSubContractAmountInfoData != null && !cvSubContractAmountInfoData.isEmpty()) {
            cvSubContractAmountInfoData.sort("lineNumber",false);
            anticipatedAmt = cvSubContractAmountInfoData.sum("anticipatedChange");
            obligatedAmt = cvSubContractAmountInfoData.sum("obligatedChange");

            amountInfoType = new SubContractDataTypeImpl.SubcontractAmountInfoTypeImpl();
            SubContractAmountInfoBean subContractAmountInfoBean = (SubContractAmountInfoBean)cvSubContractAmountInfoData.get(0);
            BigDecimal amountInfo = new BigDecimal(anticipatedAmt);
            amountInfoType.setAnticipatedAmount(amountInfo.setScale(2,BigDecimal.ROUND_HALF_DOWN));
            amountInfo = new BigDecimal(subContractAmountInfoBean.getAnticipatedChange());
            amountInfoType.setAnticipatedChange(amountInfo.setScale(2,BigDecimal.ROUND_HALF_DOWN));
            
            if (subContractAmountInfoBean.getComments() != null) {
                amountInfoType.setComments(subContractAmountInfoBean.getComments());
            }
            
            Calendar effectiveDate = null;
            if (subContractAmountInfoBean.getEffectiveDate()!=null) {
                effectiveDate = Calendar.getInstance();
                effectiveDate.setTime(subContractAmountInfoBean.getEffectiveDate());
                amountInfoType.setEffectiveDate(effectiveDate);
            }
            amountInfoType.setLineNumber(String.valueOf(subContractAmountInfoBean.getLineNumber()));
            
            amountInfo = new BigDecimal(obligatedAmt);
            amountInfoType.setObligatedAmount(amountInfo.setScale(2,BigDecimal.ROUND_HALF_DOWN));
            amountInfo = new BigDecimal(subContractAmountInfoBean.getObligatedChange());
            
            if(subContractAmountInfoBean.getSubContractCode() !=null){
                amountInfoType.setSubcontractCode(subContractAmountInfoBean.getSubContractCode());
            }
            //set SequenceNumber
            amountInfoType.setSequenceNumber(String.valueOf(subContractAmountInfoBean.getSequenceNumber()));
            
            amountInfoType.setObligatedChange(amountInfo.setScale(2,BigDecimal.ROUND_HALF_DOWN));
            
            Calendar performanceStartDate = null;
            if (subContractAmountInfoBean.getPerformanceStartDate()!=null) {
                performanceStartDate = Calendar.getInstance();
                performanceStartDate.setTime(subContractAmountInfoBean.getPerformanceStartDate());
                amountInfoType.setPerformanceStartDate(performanceStartDate);
            }
            
            Calendar performanceEndDate = null;
            if (subContractAmountInfoBean.getPerformanceEndDate()!=null) {
                performanceEndDate = Calendar.getInstance();
                performanceEndDate.setTime(subContractAmountInfoBean.getPerformanceEndDate());
                amountInfoType.setPerformanceEndDate(performanceEndDate);
            }
            
            if(subContractAmountInfoBean.getModificationNumber() != null){
                amountInfoType.setModificationNumber(subContractAmountInfoBean.getModificationNumber());
            }
            
            Calendar modificationEffectiveDate = null;
            if (subContractAmountInfoBean.getModificationEffectiveDate()!=null) {
                modificationEffectiveDate = Calendar.getInstance();
                modificationEffectiveDate.setTime(subContractAmountInfoBean.getModificationEffectiveDate());
                amountInfoType.setModificationEffectiveDate(modificationEffectiveDate);
            }
            
            subContractDataType.getSubcontractAmountInfo().add(amountInfoType);
        }
        return subContractDataType;
    }
    
    /**
     * Method to set amount released details
     * @param subContractBean 
     * @param subContractDataType 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return subContractDataType
     */
    private SubContractDataType getSubcontractAmountReleasedDetails(SubContractBean subContractBean, SubContractDataType subContractDataType)throws CoeusException, DBException {
        String queryKey = subContractBean.getSubContractCode()+subContractBean.getSequenceNumber();
        QueryEngine queryEngine = QueryEngine.getInstance();
        CoeusVector cvSubContractAmountReleaseData = queryEngine.getDetails(queryKey, SubContractAmountReleased.class);
        SubContractDataType.SubcontractAmountReleasedType amountReleasedType;
        if (cvSubContractAmountReleaseData != null && !cvSubContractAmountReleaseData.isEmpty()) {
            for (int index = 0; index < cvSubContractAmountReleaseData.size(); index++) {
                SubContractAmountReleased subContractAmountReleased = (SubContractAmountReleased)cvSubContractAmountReleaseData.get(index);
                amountReleasedType = new SubContractDataTypeImpl.SubcontractAmountReleasedTypeImpl();
                BigDecimal amountReleased = new BigDecimal(subContractAmountReleased.getAmountReleased());
                amountReleasedType.setAmountReleased(amountReleased.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                
                if (subContractAmountReleased.getComments() != null) {
                    amountReleasedType.setComments(subContractAmountReleased.getComments());
                }
                
                Calendar effectiveDate = null;
                if (subContractAmountReleased.getEffectiveDate()!=null) {
                    effectiveDate = Calendar.getInstance();
                    effectiveDate.setTime(subContractAmountReleased.getEffectiveDate());
                    amountReleasedType.setEffectiveDate(effectiveDate);
                }
                amountReleasedType.setLineNumber(String.valueOf(subContractAmountReleased.getLineNumber()));
                
                //set SubcontractorCode
                if(subContractAmountReleased.getSubContractCode() !=null){
                    amountReleasedType.setSubcontractCode(subContractAmountReleased.getSubContractCode());
                }
                //set SequenceNumber
                amountReleasedType.setSequenceNumber(String.valueOf(subContractAmountReleased.getSequenceNumber()));
                //Set StartDate
                Calendar startDate2 = null;
                if (subContractAmountReleased.getStartDate()!=null) {
                    startDate2 = Calendar.getInstance();
                    startDate2.setTime(subContractAmountReleased.getStartDate());
                    amountReleasedType.setStartDate(startDate2);
                }
                //Set EndDate
                Calendar endDate2 = null;
                if (subContractAmountReleased.getEndDate()!=null) {
                    endDate2 = Calendar.getInstance();
                    endDate2.setTime(subContractAmountReleased.getEndDate());
                    amountReleasedType.setEndDate(endDate2);
                }
                //setInvoice
                if(subContractAmountReleased.getInvoiceNumber() !=null){
                    amountReleasedType.setInvoiceNumber(subContractAmountReleased.getInvoiceNumber());
                }
                subContractDataType.getSubcontractAmountReleased().add(amountReleasedType);
            }
        }
        return subContractDataType;
    }
    
    /**
     * Method to set auditor details
     * @param subContractBean 
     * @param subContractDataType 
     * @throws javax.xml.bind.JAXBException 
     * @return subContractDataType
     */
    private SubContractDataType getSubcontractAuditorDetails(SubContractBean subContractBean, SubContractDataType subContractDataType) throws JAXBException {
        SubContractDataType.AuditorDataType auditorDataType;
        CoeusVector cvAuditorData = subContractBean.getAuditorData();
        OrganizationType organizationType = subContractObjFactory.createOrganizationType();
        
        if (cvAuditorData != null &&   cvAuditorData.size() > 0){
            for(int index = 0; index < cvAuditorData.size(); index++){
                RolodexDetailsBean rolodexDetailsBean = (RolodexDetailsBean) cvAuditorData.get(index);
                auditorDataType = subContractObjFactory.createSubContractDataTypeAuditorDataType();
                RolodexDetailsType rolodexDetailsType = subContractObjFactory.createRolodexDetailsType();
                
                rolodexDetailsType.setRolodexName(getRolodexName(rolodexDetailsBean));
                if(rolodexDetailsBean.getFirstName() !=null){
                    rolodexDetailsType.setFirstName(rolodexDetailsBean.getFirstName());
                }
                if(rolodexDetailsBean.getMiddleName() !=null){
                    rolodexDetailsType.setMiddleName(rolodexDetailsBean.getMiddleName());
                }
                if(rolodexDetailsBean.getLastName() !=null){
                    rolodexDetailsType.setLastName(rolodexDetailsBean.getLastName());
                }
                if(rolodexDetailsBean.getAddress1() !=null){
                    rolodexDetailsType.setAddress1(rolodexDetailsBean.getAddress1());
                }
                if(rolodexDetailsBean.getAddress2() !=null){
                    rolodexDetailsType.setAddress2(rolodexDetailsBean.getAddress2());
                }
                if(rolodexDetailsBean.getAddress3() !=null){
                    rolodexDetailsType.setAddress3(rolodexDetailsBean.getAddress3());
                }
                if(rolodexDetailsBean.getCity() !=null){
                    rolodexDetailsType.setCity(rolodexDetailsBean.getCity());
                }
                if(rolodexDetailsBean.getState() !=null){
                    rolodexDetailsType.setStateDescription(rolodexDetailsBean.getState());
                }
                if(rolodexDetailsBean.getPostalCode() !=null){
                    rolodexDetailsType.setPincode(rolodexDetailsBean.getPostalCode());
                }
                if(rolodexDetailsBean.getPrefix() !=null){
                    rolodexDetailsType.setPrefix(rolodexDetailsBean.getPrefix());
                }
                organizationType.setCognizantAuditor(rolodexDetailsType);
                auditorDataType.getOrganizationDetails().add(organizationType);
                subContractDataType.setAuditorData(auditorDataType);
            }
        }
        return subContractDataType;
    }
    
    /**
     * Methos to set OSP and Admin details
     * @param subContractBean 
     * @param subContractDataType 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws javax.xml.bind.JAXBException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return subContractDataType
     */
    private SubContractDataType getSubcontractOspAndAdminDetails(SubContractBean subContractBean, SubContractDataType subContractDataType) throws CoeusException, JAXBException, DBException {
        String requisitionerUnit;
        SubContractTxnBean subContractTxnBean = new SubContractTxnBean();
        SubContractDataType.AdministrativeOfficerType administrativeOfficerType;
        PersonDetailsType ospAdministratorType;
        requisitionerUnit = subContractBean.getRequisitionerUnit();
        PersonInfoBean personInfoBean = null;
        
        if (requisitionerUnit !=null ){
            UnitDataTxnBean unitDataTxnBean = new UnitDataTxnBean();
            UnitDetailFormBean unitDetailFormBean = unitDataTxnBean.getUnitDetails(requisitionerUnit);
            String ospAdminId = unitDetailFormBean.getOspAdminId();
            String AdministrativeOffId = unitDetailFormBean.getAdminOfficerId();
            
            //Get the OSPAdministrativeOffice data
            personInfoBean = subContractTxnBean.getPersonInfo(ospAdminId);
            
            ospAdministratorType = subContractObjFactory.createPersonDetailsType();
            if(personInfoBean !=null){
                if( personInfoBean.getFirstName() !=null){
                    ospAdministratorType.setFirstName( personInfoBean.getFirstName());
                }
                if( personInfoBean.getMiddleName() !=null){
                    ospAdministratorType.setMiddleName( personInfoBean.getMiddleName());
                }
                if( personInfoBean.getLastName() !=null){
                    ospAdministratorType.setLastName( personInfoBean.getLastName());
                }
                if( personInfoBean.getOffPhone() !=null){
                    ospAdministratorType.setOfficePhone( personInfoBean.getOffPhone());
                }
                if( personInfoBean.getSecOffPhone() !=null){
                    ospAdministratorType.setSecondryOfficePhone( personInfoBean.getSecOffPhone());
                }
                if( personInfoBean.getEmail() !=null){
                    ospAdministratorType.setEmailAddress( personInfoBean.getEmail());
                }
                if( personInfoBean.getDirTitle() !=null){
                    ospAdministratorType.setDirectoryTitle( personInfoBean.getDirTitle());
                }
                if( personInfoBean.getFax() !=null){
                    ospAdministratorType.setFaxNumber( personInfoBean.getFax());
                }
                if( personInfoBean.getCity() !=null){
                    ospAdministratorType.setCity( personInfoBean.getCity());
                }
                if( personInfoBean.getState() !=null){
                    ospAdministratorType.setState( personInfoBean.getState());
                }
                if( personInfoBean.getPostalCode() !=null){
                    ospAdministratorType.setPostalCode( personInfoBean.getPostalCode());
                }
                subContractDataType.getOspAdministrator().add(ospAdministratorType);
            }
            
            // Get Administrative Office Data for person
            administrativeOfficerType = new SubContractDataTypeImpl.AdministrativeOfficerTypeImpl();
            
            PersonDetailsType adminPersonDetailsType;
            
            personInfoBean = null;
            personInfoBean = subContractTxnBean.getPersonInfo(AdministrativeOffId);
            if(personInfoBean !=null){
                adminPersonDetailsType = subContractObjFactory.createPersonDetailsType();
                if( personInfoBean.getFirstName() !=null){
                    adminPersonDetailsType.setFirstName( personInfoBean.getFirstName());
                }
                if( personInfoBean.getMiddleName() !=null){
                    adminPersonDetailsType.setMiddleName( personInfoBean.getMiddleName());
                }
                if( personInfoBean.getLastName() !=null){
                    adminPersonDetailsType.setLastName( personInfoBean.getLastName());
                }
                if( personInfoBean.getOffPhone() !=null){
                    adminPersonDetailsType.setOfficePhone( personInfoBean.getOffPhone());
                }
                if( personInfoBean.getSecOffPhone() !=null){
                    adminPersonDetailsType.setSecondryOfficePhone( personInfoBean.getSecOffPhone());
                }
                if( personInfoBean.getEmail() !=null){
                    adminPersonDetailsType.setEmailAddress( personInfoBean.getEmail());
                }
                if( personInfoBean.getDirTitle() !=null){
                    adminPersonDetailsType.setDirectoryTitle( personInfoBean.getDirTitle());
                }
                if( personInfoBean.getFax() !=null){
                    adminPersonDetailsType.setFaxNumber( personInfoBean.getFax());
                }
                if( personInfoBean.getCity() !=null){
                    adminPersonDetailsType.setCity( personInfoBean.getCity());
                }
                if( personInfoBean.getState() !=null){
                    adminPersonDetailsType.setState( personInfoBean.getState());
                }
                if( personInfoBean.getPostalCode() !=null){
                    adminPersonDetailsType.setPostalCode( personInfoBean.getPostalCode());
                }
                administrativeOfficerType.setAdministrativeOfficerDetails(adminPersonDetailsType);
                subContractDataType.setAdministrativeOfficer(administrativeOfficerType);
            }
        }
        return subContractDataType;
    }
    
    /**
     * Method to set PI details
     * @param subContractBean 
     * @param subContractDataType 
     * @throws javax.xml.bind.JAXBException 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return subContractDataType
     */
    private SubContractDataType getSubcontractPIDetails(SubContractBean subContractBean, SubContractDataType subContractDataType) throws JAXBException, CoeusException, DBException {
        String requisitionerId;
        PersonInfoBean personInfoBean;
        SubContractTxnBean subContractTxnBean = new SubContractTxnBean();
        requisitionerId = subContractBean.getRequisitionerId();
        if (requisitionerId !=null){
            PersonDetailsType principalInvestigatorType;
            personInfoBean = null;
            principalInvestigatorType = subContractObjFactory.createPersonDetailsType();
            personInfoBean = subContractTxnBean.getPersonInfo(requisitionerId);
            if(personInfoBean !=null){
                if( personInfoBean.getFirstName() !=null){
                    principalInvestigatorType.setFirstName( personInfoBean.getFirstName());
                }
                if( personInfoBean.getMiddleName() !=null){
                    principalInvestigatorType.setMiddleName( personInfoBean.getMiddleName());
                }
                if( personInfoBean.getLastName() !=null){
                    principalInvestigatorType.setLastName( personInfoBean.getLastName());
                }
                if( personInfoBean.getAddress1() !=null){
                    principalInvestigatorType.setAddressLine1(personInfoBean.getAddress1());
                }
                
                if( personInfoBean.getAddress2() !=null){
                    principalInvestigatorType.setAddressLine2(personInfoBean.getAddress2());
                }
                
                if( personInfoBean.getAddress3() !=null){
                    principalInvestigatorType.setAddressLine3(personInfoBean.getAddress3());
                }
                if( personInfoBean.getOffPhone() !=null){
                    principalInvestigatorType.setOfficePhone( personInfoBean.getOffPhone());
                }
                if( personInfoBean.getSecOffPhone() !=null){
                    principalInvestigatorType.setSecondryOfficePhone( personInfoBean.getSecOffPhone());
                }
                if( personInfoBean.getEmail() !=null){
                    principalInvestigatorType.setEmailAddress( personInfoBean.getEmail());
                }
                if( personInfoBean.getDirTitle() !=null){
                    principalInvestigatorType.setDirectoryTitle( personInfoBean.getDirTitle());
                }
                if( personInfoBean.getFax() !=null){
                    principalInvestigatorType.setFaxNumber( personInfoBean.getFax());
                }
                if( personInfoBean.getCity() !=null){
                    principalInvestigatorType.setCity( personInfoBean.getCity());
                }
                if( personInfoBean.getState() !=null){
                    principalInvestigatorType.setState( personInfoBean.getState());
                }
                if( personInfoBean.getPostalCode() !=null){
                    principalInvestigatorType.setPostalCode( personInfoBean.getPostalCode());
                }
                if( personInfoBean.getHomeUnit()!=null){
                    principalInvestigatorType.setDepartmentName(subContractTxnBean.getUnitName(personInfoBean.getHomeUnit()));
                }
                subContractDataType.getPrincipalInvestigator().add(principalInvestigatorType);
            }
        }
        return subContractDataType;
    }
    
    /**
     * Methos to set subcontract details
     * @param subContractBean 
     * @param subContractDataType 
     * @throws javax.xml.bind.JAXBException 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @throws java.text.ParseException 
     * @return subContractDataType
     */
    private OrganizationType getSubcontractorDetails(SubContractBean subContractBean, SubContractDataType subContractDataType) throws JAXBException, CoeusException, DBException, ParseException{
        OrganizationMaintenanceDataTxnBean orgMaintenanceDataTxnBean = new OrganizationMaintenanceDataTxnBean();
        OrganizationType organizationType = subContractObjFactory.createOrganizationType();
        OrganizationMaintenanceFormBean orgFormBean = orgMaintenanceDataTxnBean.getOrganizationMaintenanceDetails(subContractBean.getSubContractId());
        organizationType.setOrganizationID(orgFormBean.getOrganizationId());
        organizationType.setOrganizationName(orgFormBean.getOrganizationName());
        organizationType.setContactAddressId(orgFormBean.getContactAddressId());
        if(orgFormBean.getAddress() !=null){
            organizationType.setAddress(orgFormBean.getAddress());
        }
        if(orgFormBean.getCableAddress() !=null){
            organizationType.setCableAddress(orgFormBean.getCableAddress());
        }
        if(orgFormBean.getTelexNumber() !=null){
            organizationType.setTelexNumber(orgFormBean.getTelexNumber());
        }
        if(orgFormBean.getCounty() !=null){
            organizationType.setCounty(orgFormBean.getCounty());
        }
        if(orgFormBean.getCongressionalDistrict() !=null){
            organizationType.setCongressionalDistrict(orgFormBean.getCongressionalDistrict());
        }
        if(orgFormBean.getIncorporatedIn() !=null){
            organizationType.setIncorporatedIn(orgFormBean.getIncorporatedIn());
        }
        
        organizationType.setNumberOfEmployees(orgFormBean.getNumberOfExmployees());
        
        if(orgFormBean.getIrsTaxExcemption() !=null){
            organizationType.setIrsTaxExcemption(orgFormBean.getIrsTaxExcemption());
        }
        if(orgFormBean.getFederalEmployerID() !=null){
            organizationType.setFedralEmployerId(orgFormBean.getFederalEmployerID());
        }
        if(orgFormBean.getMassTaxExcemptNum() !=null){
            organizationType.setMassTaxExcemptNum(orgFormBean.getMassTaxExcemptNum());
        }
        
        if(orgFormBean.getAgencySymbol() !=null){
            organizationType.setAgencySymbol(orgFormBean.getAgencySymbol());
        }
        if(orgFormBean.getVendorCode() !=null){
            organizationType.setVendorCode(orgFormBean.getVendorCode());
        }
        if(orgFormBean.getComGovEntityCode() !=null){
            organizationType.setComGovEntityCode(orgFormBean.getComGovEntityCode());
        }
        if(orgFormBean.getMassEmployeeClaim() !=null){
            organizationType.setMassEmployeeClaim(orgFormBean.getMassEmployeeClaim());
        }
        if(orgFormBean.getDunsNumber() !=null){
            organizationType.setDunsNumber(orgFormBean.getDunsNumber());
        }
        if(orgFormBean.getDunsPlusFourNumber() !=null){
            organizationType.setDunsPlusFourNumber(orgFormBean.getDunsPlusFourNumber());
        }
        if(orgFormBean.getDodacNumber() !=null){
            organizationType.setDodacNumber(orgFormBean.getDodacNumber());
        }
        if(orgFormBean.getCageNumber() !=null){
            organizationType.setCageNumber(orgFormBean.getCageNumber());
        }
        if(orgFormBean.getHumanSubAssurance() !=null){
            organizationType.setHumanSubAssurance(orgFormBean.getHumanSubAssurance());
        }
        if(orgFormBean.getAnimalWelfareAssurance() !=null){
            organizationType.setAnimalWelfareAssurance(orgFormBean.getAnimalWelfareAssurance());
        }
        
//        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar scienceMisconductComplDate = null;
        if (orgFormBean.getScienceMisconductComplDate()!=null) {
            scienceMisconductComplDate = Calendar.getInstance();
            Date convertedDate = formatter.parse(orgFormBean.getScienceMisconductComplDate());
            scienceMisconductComplDate.setTime(convertedDate);
            organizationType.setScienceMisconductComplDate(scienceMisconductComplDate);
        }
        
        if(orgFormBean.getPhsAcount() !=null){
            organizationType.setPhsAcount(orgFormBean.getPhsAcount());
        }
        if(orgFormBean.getNsfInstitutionalCode() !=null){
            organizationType.setNsfInstitutionalCode(orgFormBean.getNsfInstitutionalCode());
        }
        if(orgFormBean.getIndirectCostRateAgreement() !=null){
            organizationType.setIndirectCostRateAgreement(orgFormBean.getIndirectCostRateAgreement());
        }
        organizationType.setOnrResidentRep(orgFormBean.getOnrResidentRep());
        return organizationType;
    }
    
    /**
     * Method to set custom data details
     * @param subContractBean 
     * @param subContractDataType 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @throws javax.xml.bind.JAXBException 
     * @return subContractDataType
     */
    private SubContractDataType getSubcontractOthersDataDetails(SubContractBean subContractBean, SubContractDataType subContractDataType) throws CoeusException, DBException, JAXBException {
        String queryKey = subContractBean.getSubContractCode()+subContractBean.getSequenceNumber();
        QueryEngine queryEngine = QueryEngine.getInstance();
        CoeusVector cvOtherDetailsListDb = queryEngine.getDetails(queryKey, SubContractCustomDataBean.class);
        if(cvOtherDetailsListDb != null && !cvOtherDetailsListDb.isEmpty()){
            SubContractCustomDataBean customInfoBeanDb = null;
            OtherDataType otherDataType = null;
            for(Object obj : cvOtherDetailsListDb){
                customInfoBeanDb = (SubContractCustomDataBean)obj;
                otherDataType = subContractObjFactory.createOtherDataType();
                otherDataType.setColumnName(customInfoBeanDb.getColumnName());
                otherDataType.setColumnValue(customInfoBeanDb.getColumnValue());
                if(customInfoBeanDb.getUpdateTimestamp() != null){
                    otherDataType.setUpdateTimestamp(formatDate(customInfoBeanDb.getUpdateTimestamp()));
                }
                otherDataType.setUpdateUser(customInfoBeanDb.getUpdateUser());
                subContractDataType.getOthersData().add(otherDataType);
            }
        }
        return subContractDataType;
    }
    
    
    
    /**
     * Method to set template info details
     * @param subContractBean 
     * @param subContractDataType 
     * @throws javax.xml.bind.JAXBException 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return subContractDataType
     */
    private SubContractDataType getSubcontractTemplateInfo(SubContractBean subContractBean, SubContractDataType subContractDataType) throws JAXBException, CoeusException, DBException {
        String queryKey = subContractBean.getSubContractCode()+subContractBean.getSequenceNumber();
        QueryEngine queryEngine = QueryEngine.getInstance();
        CoeusVector cvSubContractTemplateInfo = queryEngine.getDetails(queryKey, SubcontractTemplateInfoBean.class);
        SubcontractTemplateInfoBean subcontractTemplateInfoBean  = null;
        if(cvSubContractTemplateInfo != null && !cvSubContractTemplateInfo.isEmpty()){
            subcontractTemplateInfoBean = (SubcontractTemplateInfoBean)cvSubContractTemplateInfo.get(0);
        }
        SubContractDataType.SubcontractTemplateInfoType subcontractTemplateInfoType = subContractObjFactory.createSubContractDataTypeSubcontractTemplateInfoType();
        if(subcontractTemplateInfoBean != null ){
            subcontractTemplateInfoType.setSowOrSubProposalBudget(String.valueOf(subcontractTemplateInfoBean.getSowOrSubProposalBudget()));
            Calendar subPropDate = null;
            if (subcontractTemplateInfoBean.getSubProposalDate()!=null) {
                subPropDate = Calendar.getInstance();
                subPropDate.setTime(subcontractTemplateInfoBean.getSubProposalDate());
                subcontractTemplateInfoType.setSubProposalDate(subPropDate);
                
            }
            subcontractTemplateInfoType.setInvoiceOrPaymentContact(String.valueOf(subcontractTemplateInfoBean.getInvoiceOrPaymentContactTypeCode()));
            if(subcontractTemplateInfoBean.getInvoiceOrPaymentContactTypeDesc() !=null){
                subcontractTemplateInfoType.setInvoiceOrPaymentContactDescription(subcontractTemplateInfoBean.getInvoiceOrPaymentContactTypeDesc());
            }
            subcontractTemplateInfoType.setFinalStmtOfCostsContact(String.valueOf(subcontractTemplateInfoBean.getFinalStmtOfCostsContactTypeCode()));
            if(subcontractTemplateInfoBean.getFinalStmtOfCostsContactTypeDesc() !=null){
                subcontractTemplateInfoType.setFinalStmtOfCostsContactDescription(subcontractTemplateInfoBean.getFinalStmtOfCostsContactTypeDesc());
            }
            subcontractTemplateInfoType.setChangeRequestsContact(String.valueOf(subcontractTemplateInfoBean.getChangeRequestsContactTypeCode()));
            if(subcontractTemplateInfoBean.getChangeRequestsContactTypeDesc() !=null){
                subcontractTemplateInfoType.setChangeRequestsContactDescription(subcontractTemplateInfoBean.getChangeRequestsContactTypeDesc());
            }
            subcontractTemplateInfoType.setTerminationContact(String.valueOf(subcontractTemplateInfoBean.getTerminationContactTypeCode()));
            if(subcontractTemplateInfoBean.getTerminationContactTypeDesc() !=null){
                subcontractTemplateInfoType.setTerminationContactDescription(subcontractTemplateInfoBean.getTerminationContactTypeDesc());
            }
            subcontractTemplateInfoType.setNoCostExtensionContact(String.valueOf(subcontractTemplateInfoBean.getNoCostExtensionContactTypeCode()));
            if(subcontractTemplateInfoBean.getNoCostExtensionContactTypeDesc() !=null){
                subcontractTemplateInfoType.setNoCostExtensionContactDescription(subcontractTemplateInfoBean.getNoCostExtensionContactTypeDesc());
            }
            subcontractTemplateInfoType.setPerfSiteDiffFromOrgAddr(String.valueOf(subcontractTemplateInfoBean.getPerfSiteDiffFromOrgAddr()));
            subcontractTemplateInfoType.setPerfSiteSameAsSubPiAddr(String.valueOf(subcontractTemplateInfoBean.getPerfSiteSameAsSubPiAddr()));
            subcontractTemplateInfoType.setSubRegisteredInCcr(String.valueOf(subcontractTemplateInfoBean.getSubRegisteredInCcr()));
            subcontractTemplateInfoType.setSubExemptFromReportingComp(String.valueOf(subcontractTemplateInfoBean.getSubExemptFromReportingComp()));
            if(subcontractTemplateInfoBean.getParentDunsNumber() !=null){
                subcontractTemplateInfoType.setParentDunsNumber(subcontractTemplateInfoBean.getParentDunsNumber());
            }
            if(subcontractTemplateInfoBean.getParentCongressionalDistrict() !=null){
                subcontractTemplateInfoType.setParentCongressionalDistrict(subcontractTemplateInfoBean.getParentCongressionalDistrict());
            }
            subcontractTemplateInfoType.setExemptFromRprtgExecComp(String.valueOf(subcontractTemplateInfoBean.getExemptFromRprtgExecComp()));
            subcontractTemplateInfoType.setCopyRights(String.valueOf(subcontractTemplateInfoBean.getCopyrightTypeCode()));
            if(subcontractTemplateInfoBean.getCopyrightTypeDesc() !=null){
                subcontractTemplateInfoType.setCopyRightsDescription(subcontractTemplateInfoBean.getCopyrightTypeDesc());
            }
            subcontractTemplateInfoType.setAutomaticCarryForward(String.valueOf(subcontractTemplateInfoBean.getAutomaticCarryForward()));
            subcontractTemplateInfoType.setCarryForwardRequestsSentTo(String.valueOf(subcontractTemplateInfoBean.getCarryForwardRequestsSentTo()));
            if(subcontractTemplateInfoBean.getCarryForwardRequestsSentToDesc() !=null){
                subcontractTemplateInfoType.setCarryForwardRequestsSentToDescription(subcontractTemplateInfoBean.getCarryForwardRequestsSentToDesc());
            }
            subcontractTemplateInfoType.setTreatmentPrgmIncomeAdditive(String.valueOf(subcontractTemplateInfoBean.getTreatmentPrgmIncomeAdditive()));
            if(subcontractTemplateInfoBean.getApplicableProgramRegulations() !=null){
                subcontractTemplateInfoType.setApplicableProgramRegulations(subcontractTemplateInfoBean.getApplicableProgramRegulations());
            }
            Calendar appProgramREGSDate = null;
            if (subcontractTemplateInfoBean.getApplicableProgramRegsDate()!=null) {
                appProgramREGSDate = Calendar.getInstance();
                appProgramREGSDate.setTime(subcontractTemplateInfoBean.getApplicableProgramRegsDate());
                subcontractTemplateInfoType.setApplicableProgramRegsDate(appProgramREGSDate);
            }
            
            subContractDataType.getSubcontractTemplateInfo().add(subcontractTemplateInfoType);
        }
        
        return subContractDataType;
    }
    
    /**
     * Method to set subcontract report
     * @param subContractBean 
     * @param subContractDataType 
     * @throws javax.xml.bind.JAXBException 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return subContractDataType
     */
    private SubContractDataType getSubcontractReport(SubContractBean subContractBean, SubContractDataType subContractDataType) throws JAXBException, CoeusException, DBException {
        String queryKey = subContractBean.getSubContractCode()+subContractBean.getSequenceNumber();
        QueryEngine queryEngine = QueryEngine.getInstance();
        CoeusVector cvSubContractReport = queryEngine.getDetails(queryKey, SubcontractReportBean.class);
        if(cvSubContractReport != null && !cvSubContractReport.isEmpty()){
            for(Object reportDetails : cvSubContractReport){
                SubContractDataType.SubcontractReportsType subcontractReportsType = subContractObjFactory.createSubContractDataTypeSubcontractReportsType();
                SubcontractReportBean subcontractReportBean = (SubcontractReportBean)reportDetails;
                if(subcontractReportBean != null ){
                    subcontractReportsType.setSubcontractCode(subcontractReportBean.getSubContractCode());
                    subcontractReportsType.setSequenceNumber(subcontractReportBean.getSequenceNumber()+"");
                    subcontractReportsType.setReportTypeCode(subcontractReportBean.getReportTypeCode()+"");
                    subcontractReportsType.setReportTypeDescription(subcontractReportBean.getReportTypeDescription());
                    if(subcontractReportBean.getUpdateTimestamp() != null){
                        subcontractReportsType.setUpdateTimestamp(formatDate(subcontractReportBean.getUpdateTimestamp()));
                    }
                    subcontractReportsType.setUpdateUser(subcontractReportBean.getUpdateUser());
                    subContractDataType.getSubcontractReports().add(subcontractReportsType);
                }
            }
        }
        return subContractDataType;
    }
    
    /**
     * Method to set award details
     * @param subContractBean 
     * @param subContractDataType 
     * @param SubContractFundingSourceBean 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @throws javax.xml.bind.JAXBException 
     * @throws java.io.IOException 
     * @return subContractDataType
     */
    private SubContractDataType getAwardDetails(SubContractBean subContractBean, SubContractDataType subContractDataType,
            SubContractFundingSourceBean SubContractFundingSourceBean)throws CoeusException, DBException, JAXBException, IOException {
        AwardType  awardType  = new AwardTypeImpl();
        int SPECIAL_RATE_COMMENT = 7;
        
        AwardType.AwardDetailsType awardDetailsType = awardObjFactory.createAwardTypeAwardDetailsType();
        AwardTxnBean awardTxnBean = new AwardTxnBean();
        AwardReportTxnBean awardReportTxnBean = new AwardReportTxnBean();
        
        String mitAwardNumber = SubContractFundingSourceBean.getMitAwardNumber();
        AwardBean awardBean = awardTxnBean.getAwardDetails(mitAwardNumber,-1);
        String accountNumber = awardReportTxnBean.getAccountForAward(mitAwardNumber);
        CoeusVector cvInvestigators = awardTxnBean.getAwardInvestigators(mitAwardNumber);
        int seqNumber = awardBean.getSequenceNumber();
        
        AwardHeaderBean awardHeaderBean = awardBean.getAwardHeaderBean();
        // AwardHeader
        AwardHeaderType awardHeaderType = new AwardHeaderTypeImpl();
        awardHeaderType.setAccountNumber(accountNumber);
        awardHeaderType.setAwardNumber(mitAwardNumber);
        awardHeaderType.setModificationNumber(awardBean.getModificationNumber());
        awardHeaderType.setNSFCode(awardBean.getNsfCode());
        awardHeaderType.setNSFDescription(awardBean.getNsfDescription());
        String principleInvestigator = null;
        
        PersonInfoTxnBean personInfoTxnBean = new PersonInfoTxnBean();
        if(cvInvestigators != null && !cvInvestigators.isEmpty()){
            for (int index=0;index<cvInvestigators.size();index++) {
                AwardInvestigatorsBean awardInvestigatorsBean = (AwardInvestigatorsBean)cvInvestigators.get(index);
                if (awardInvestigatorsBean.isPrincipalInvestigatorFlag()) {
                    principleInvestigator = awardInvestigatorsBean.getPersonName();
                    awardHeaderType.setPIName(principleInvestigator);
                    break;
                }
            }
        }
        awardHeaderType.setSequenceNumber(seqNumber);
        awardHeaderType.setSponsorAwardNumber(awardBean.getSponsorAwardNumber());
        awardHeaderType.setSponsorCode(awardBean.getSponsorCode());
        awardHeaderType.setSponsorDescription(awardBean.getSponsorName());
        awardHeaderType.setStatusCode(String.valueOf(awardBean.getStatusCode()));
        awardHeaderType.setStatusDescription(awardBean.getStatusDescription());
        awardHeaderType.setTitle(awardHeaderBean.getTitle());
        awardDetailsType.setAwardHeader(awardHeaderType);
        AwardType.AwardDetailsType.OtherHeaderDetailsType otherHeaderDetailsType = awardObjFactory.createAwardTypeAwardDetailsTypeOtherHeaderDetailsType();
        otherHeaderDetailsType.setAccountTypeCode(""+awardHeaderBean.getAccountTypeCode());
        otherHeaderDetailsType.setAccountTypeDesc(awardHeaderBean.getAccountTypeDescription());
        otherHeaderDetailsType.setActivityTypeCode(awardHeaderBean.getActivityTypeCode());
        otherHeaderDetailsType.setActivityTypeDesc(awardHeaderBean.getActivityTypeDescription());
        otherHeaderDetailsType.setAwardTypeCode(awardHeaderBean.getAwardTypeCode());
        otherHeaderDetailsType.setAwardTypeDesc(awardHeaderBean.getAwardTypeDescription());
        otherHeaderDetailsType.setBasisPaymentCode(String.valueOf(awardHeaderBean.getBasisOfPaymentCode()));
        UserMaintDataTxnBean userMainDataTxnBean = new UserMaintDataTxnBean();
        otherHeaderDetailsType.setUpdateUser(userMainDataTxnBean.getUserName(awardBean.getUpdateUser()));
        Calendar lastUpdatedDate = Calendar.getInstance();
        lastUpdatedDate.setTime(awardBean.getUpdateTimestamp());
        otherHeaderDetailsType.setLastUpdate(lastUpdatedDate);
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
        AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
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
        awardDetailsType.setOtherHeaderDetails(otherHeaderDetailsType);
        //AwardDetails
        awardDetailsType.setApprvdEquipmentIndicator(awardBean.getApprvdEquipmentIndicator());
        awardDetailsType.setApprvdForeginTripIndicator(awardBean.getApprvdForeignTripIndicator());
        awardDetailsType.setApprvdSubcontractIndicator(awardBean.getApprvdSubcontractIndicator());
        awardDetailsType.setCostSharingIndicator(awardBean.getCostSharingIndicator());
        AwardDetailsBean awardDetailsBean = awardBean.getAwardDetailsBean();
        if (awardDetailsBean.getAwardEffectiveDate()!=null) {
            Calendar effectiveDate = Calendar.getInstance();
            effectiveDate.setTime(awardDetailsBean.getAwardEffectiveDate());
            awardDetailsType.setEffectiveDate(effectiveDate);
        }
        if (awardDetailsBean.getBeginDate()!=null) {
            Calendar beginDate = Calendar.getInstance();
            beginDate.setTime(awardDetailsBean.getBeginDate());
            awardDetailsType.setBeginDate(beginDate);
        }
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
        
        awardType.setAwardDetails(awardDetailsType);
        
        subContractDataType.getAward().add(awardType);
        
        return subContractDataType;
    }
    
    /**
     * Method to set print requirments
     * @param params 
     * @param subContractDataType 
     * @throws javax.xml.bind.JAXBException 
     * @return subContractDataType
     */
    private SubContractDataType getPrintRequirements(Hashtable params, SubContractDataType subContractDataType) throws JAXBException, CoeusException, DBException {
        
        SubContractDataType.PrintRequirementType printRequirementType = new SubContractDataTypeImpl.PrintRequirementTypeImpl();
        
//        String contextPath = (String) params.get("APP_CONTEXT_PATH");
        
        printRequirementType.setAttachment3BRequired("N");
        printRequirementType.setAttachment4Required("N");
        printRequirementType.setAttachment4ARequired("N");
        printRequirementType.setAttachment5Required("N");

        Vector vecFDPTemplate = (Vector)params.get(CoeusConstants.SUBCONTRACT_FDP_TEMPLATE_ATTACHMENT);
        if(vecFDPTemplate != null && !vecFDPTemplate.isEmpty()){
            CoeusFunctions coeusFunctions = new CoeusFunctions();
            String attachment4 = coeusFunctions.getParameterValue("SUBCONTRACT_FDP_ATTACHMENT_4_FORM_ID");
            String attachment3B = coeusFunctions.getParameterValue("SUBCONTRACT_FDP_ATTACHMENT_3B_FORM_ID");
            String attachment5 = coeusFunctions.getParameterValue("SUBCONTRACT_FDP_ATTACHMENT_5_FORM_ID");
            
            if(attachment3B != null && attachment4 != null && 
                    !"".equals(attachment3B) && !"".equals(attachment4) ){
                for(Object rtfForm : vecFDPTemplate){
                    RTFFormBean rTFFormBean = (RTFFormBean)rtfForm;
                    if(rTFFormBean.getFormId().equals(attachment4)){
                        printRequirementType.setAttachment4Required("Y");
                    }
                    if(rTFFormBean.getFormId().equals(attachment3B)){
                        printRequirementType.setAttachment3BRequired("Y");
                    }
                    if(rTFFormBean.getFormId().equals(attachment5)){
                        printRequirementType.setAttachment5Required("Y");
                    }
                }
            }
        }
        String absPathImage = getClass().getClassLoader().getResource("/edu/mit/coeus/xml/images").getPath();
        printRequirementType.setImageCheckedPath(absPathImage);
        printRequirementType.setImageUncheckedPath(absPathImage);
        
        subContractDataType.setPrintRequirement(printRequirementType);
        
        return subContractDataType;
    }
    
    /**
     * Method to set school info
     * @param subContractDataType 
     * @throws javax.xml.bind.JAXBException 
     * @throws java.io.IOException 
     * @return subContractDataType
     */
    private SubContractDataType getSchoolInfo(SubContractDataType subContractDataType) throws JAXBException, IOException {
        
        SubContractDataType.SchoolInfoTypeType  schoolInfoType = subContractObjFactory.createSubContractDataTypeSchoolInfoTypeType();
        InputStream is = getClass().getResourceAsStream("/coeus.properties");
        Properties coeusProps = new Properties();
        coeusProps.load(is);
        String schoolName = coeusProps.getProperty("SCHOOL_NAME");
        String schoolAcronym = coeusProps.getProperty("SCHOOL_ACRONYM");
        
        schoolInfoType.setSchoolName(schoolName);
        schoolInfoType.setAcronym(schoolAcronym);
        subContractDataType.setSchoolInfoType(schoolInfoType);
        
        return subContractDataType;
    }
    
    /**
     * Method to set prime administrative contact
     * @param cvRolodexData 
     * @param subContractBean 
     * @param subContractDataType 
     * @throws javax.xml.bind.JAXBException 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return subContractDataType
     */
    private SubContractDataType getPrimeAdministrativeContact(CoeusVector cvRolodexData, SubContractBean subContractBean, SubContractDataType subContractDataType) throws JAXBException, CoeusException, DBException {
        
        SubContractDataType.PrimeAdministrativeContactType primeAdministrativeContactType = subContractObjFactory.createSubContractDataTypePrimeAdministrativeContactType();
        RolodexDetailsType rolodexDetailsType = null;
        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        String primeAdministrativeContactOption = coeusFunctions.getParameterValue(CoeusConstants.FDP_PRIME_ADMINISTRATIVE_CONTACT_CODE);
        int contactTypeCode;
        
        
        if( primeAdministrativeContactOption != null && cvRolodexData !=null && !cvRolodexData.isEmpty()){
            try{
                contactTypeCode = Integer.parseInt(primeAdministrativeContactOption);
            }catch(NumberFormatException ex){
                contactTypeCode = 0;
            }
            Equals eqContactTypeCode = new Equals("contactTypeCode",contactTypeCode);
            CoeusVector cvFilterContacts = cvRolodexData.filter(eqContactTypeCode);
            cvFilterContacts.sort("updateTimestamp",false);
            
            if(cvFilterContacts !=null && !cvFilterContacts.isEmpty()){
                
                SubcontractContactDetailsBean subcontractContactDetailsBean = (SubcontractContactDetailsBean) cvFilterContacts.get(0);
                
                primeAdministrativeContactType.setContactTypeCode(String.valueOf(subcontractContactDetailsBean.getContactTypeCode()));
                
                rolodexDetailsType = subContractObjFactory.createRolodexDetailsType();
                
                primeAdministrativeContactType.setContactTypeDesc(getContractTypeCodeDescription(subcontractContactDetailsBean.getContactTypeCode()));
                
                rolodexDetailsType.setRolodexName(getRolodexName(subcontractContactDetailsBean));
                if (subcontractContactDetailsBean.getAddress1() != null) {
                    rolodexDetailsType.setAddress1(subcontractContactDetailsBean.getAddress1());
                }
                if (subcontractContactDetailsBean.getAddress2() != null) {
                    rolodexDetailsType.setAddress2(subcontractContactDetailsBean.getAddress2());
                }
                if (subcontractContactDetailsBean.getAddress3() != null) {
                    rolodexDetailsType.setAddress3(subcontractContactDetailsBean.getAddress3());
                }
                if (subcontractContactDetailsBean.getCity() != null) {
                    rolodexDetailsType.setCity(subcontractContactDetailsBean.getCity());
                }
                if (subcontractContactDetailsBean.getComments() != null) {
                    rolodexDetailsType.setComments(subcontractContactDetailsBean.getComments());
                }
                if (subcontractContactDetailsBean.getCountryCode() != null) {
                    rolodexDetailsType.setCountryCode(subcontractContactDetailsBean.getCountryCode());
                }
                if (subcontractContactDetailsBean.getCountryName() != null) {
                    rolodexDetailsType.setCountryDescription(subcontractContactDetailsBean.getCountryName());
                }
                if (subcontractContactDetailsBean.getCounty() != null) {
                    rolodexDetailsType.setCounty(subcontractContactDetailsBean.getCounty());
                }
                if (subcontractContactDetailsBean.getEmailAddress() != null) {
                    rolodexDetailsType.setEmail(subcontractContactDetailsBean.getEmailAddress());
                }
                if (subcontractContactDetailsBean.getFaxNumber() != null) {
                    rolodexDetailsType.setFax(subcontractContactDetailsBean.getFaxNumber());
                }
                if (subcontractContactDetailsBean.getFirstName() != null) {
                    rolodexDetailsType.setFirstName(subcontractContactDetailsBean.getFirstName());
                }
                if (subcontractContactDetailsBean.getLastName() != null) {
                    rolodexDetailsType.setLastName(subcontractContactDetailsBean.getLastName());
                }
                if (subcontractContactDetailsBean.getMiddleName() != null) {
                    rolodexDetailsType.setMiddleName(subcontractContactDetailsBean.getMiddleName());
                }
                if (subcontractContactDetailsBean.getOrganization() != null) {
                    rolodexDetailsType.setOrganization(subcontractContactDetailsBean.getOrganization());
                }
                
                rolodexDetailsType.setRolodexId(subcontractContactDetailsBean.getRolodexId());
                if(subcontractContactDetailsBean.getStateName() !=null){
                    rolodexDetailsType.setStateDescription(subcontractContactDetailsBean.getStateName());
                }
                
                int rolodxId = subcontractContactDetailsBean.getRolodexId();
                RolodexMaintenanceDataTxnBean rolodexMaintenanceDataTxnBean = new RolodexMaintenanceDataTxnBean();
                String rolodexId = String.valueOf(rolodxId);
                RolodexDetailsBean rolodexBean = rolodexMaintenanceDataTxnBean.getRolodexMaintenanceDetails(rolodexId);
                if (rolodexBean.getOwnedByUnit() != null) {
                    rolodexDetailsType.setOwnedByUnit(rolodexBean.getOwnedByUnit());
                }
                if (rolodexBean.getPhone() != null) {
                    rolodexDetailsType.setPhoneNumber(rolodexBean.getPhone());
                }
                if (rolodexBean.getPostalCode() != null) {
                    rolodexDetailsType.setPincode(rolodexBean.getPostalCode());
                }
                if (rolodexBean.getPrefix() != null) {
                    rolodexDetailsType.setPrefix(rolodexBean.getPrefix());
                }
                if (rolodexBean.getSponsorCode() != null) {
                    rolodexDetailsType.setSponsorCode(rolodexBean.getSponsorCode());
                }
                if (rolodexBean.getSponsorName() != null) {
                    rolodexDetailsType.setSponsorName(rolodexBean.getSponsorName());
                }
                if (rolodexBean.getState() != null) {
                    rolodexDetailsType.setStateCode(rolodexBean.getState());
                }
                if (rolodexBean.getSuffix() != null) {
                    rolodexDetailsType.setSuffix(rolodexBean.getSuffix());
                }
                if (rolodexBean.getTitle() != null) {
                    rolodexDetailsType.setTitle(rolodexBean.getTitle());
                }
                
                if(rolodexBean.getCountry() !=null ){
                    rolodexDetailsType.setCounty(rolodexBean.getCountry());
                }
                if(rolodexBean.getOwnedByUnit() !=null){
                    UnitDataTxnBean unitDataTxnBean = new UnitDataTxnBean();
                    UnitDetailFormBean unitDetailFormBean = unitDataTxnBean.getUnitDetails(rolodexBean.getOwnedByUnit());
                    unitDetailFormBean.getUnitName();
                    if (unitDetailFormBean.getUnitName() != null) {
                        rolodexDetailsType.setOwnedByUnitName(unitDetailFormBean.getUnitName());
                    }
                }
                
            }
            
            primeAdministrativeContactType.setRolodexDetails(rolodexDetailsType);
        }
        subContractDataType.getPrimeAdministrativeContact().add(primeAdministrativeContactType);
        
        return subContractDataType;
    }
    
    /**
     * Method to set prime financial contact
     * @param cvRolodexData 
     * @param subContractBean 
     * @param subContractDataType 
     * @throws javax.xml.bind.JAXBException 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return subContractDataType
     */
    private SubContractDataType getPrimeFinancialContact(CoeusVector cvRolodexData, SubContractBean subContractBean, SubContractDataType subContractDataType) throws JAXBException, CoeusException, DBException {
        
        SubContractDataType.PrimeFinancialContactType primeFinancialContactType = subContractObjFactory.createSubContractDataTypePrimeFinancialContactType();
        RolodexDetailsType rolodexDetailsType = null;
        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        String primeFinancialContactOption = coeusFunctions.getParameterValue(CoeusConstants.FDP_PRIME_FINANCIAL_CONTACT_CODE);
        int contactTypeCode;
        
        
        if( primeFinancialContactOption != null && cvRolodexData !=null && !cvRolodexData.isEmpty()){
            try{
                contactTypeCode = Integer.parseInt(primeFinancialContactOption);
            }catch(NumberFormatException ex){
                contactTypeCode = 0;
            }
            Equals eqContactTypeCode = new Equals("contactTypeCode",contactTypeCode);
            CoeusVector cvFilterContacts = cvRolodexData.filter(eqContactTypeCode);
            cvFilterContacts.sort("updateTimestamp",false);
            
            if(cvFilterContacts !=null && !cvFilterContacts.isEmpty()){
                
                SubcontractContactDetailsBean subcontractContactDetailsBean = (SubcontractContactDetailsBean) cvFilterContacts.get(0);
                
                primeFinancialContactType.setContactTypeCode(String.valueOf(subcontractContactDetailsBean.getContactTypeCode()));
                
                rolodexDetailsType = subContractObjFactory.createRolodexDetailsType();
                
                primeFinancialContactType.setContactTypeDesc(getContractTypeCodeDescription(subcontractContactDetailsBean.getContactTypeCode()));
                
                rolodexDetailsType.setRolodexName(getRolodexName(subcontractContactDetailsBean));
                if (subcontractContactDetailsBean.getAddress1() != null) {
                    rolodexDetailsType.setAddress1(subcontractContactDetailsBean.getAddress1());
                }
                if (subcontractContactDetailsBean.getAddress2() != null) {
                    rolodexDetailsType.setAddress2(subcontractContactDetailsBean.getAddress2());
                }
                if (subcontractContactDetailsBean.getAddress3() != null) {
                    rolodexDetailsType.setAddress3(subcontractContactDetailsBean.getAddress3());
                }
                if (subcontractContactDetailsBean.getCity() != null) {
                    rolodexDetailsType.setCity(subcontractContactDetailsBean.getCity());
                }
                if (subcontractContactDetailsBean.getComments() != null) {
                    rolodexDetailsType.setComments(subcontractContactDetailsBean.getComments());
                }
                if (subcontractContactDetailsBean.getCountryCode() != null) {
                    rolodexDetailsType.setCountryCode(subcontractContactDetailsBean.getCountryCode());
                }
                if (subcontractContactDetailsBean.getCountryName() != null) {
                    rolodexDetailsType.setCountryDescription(subcontractContactDetailsBean.getCountryName());
                }
                if (subcontractContactDetailsBean.getCounty() != null) {
                    rolodexDetailsType.setCounty(subcontractContactDetailsBean.getCounty());
                }
                if (subcontractContactDetailsBean.getEmailAddress() != null) {
                    rolodexDetailsType.setEmail(subcontractContactDetailsBean.getEmailAddress());
                }
                if (subcontractContactDetailsBean.getFaxNumber() != null) {
                    rolodexDetailsType.setFax(subcontractContactDetailsBean.getFaxNumber());
                }
                if (subcontractContactDetailsBean.getFirstName() != null) {
                    rolodexDetailsType.setFirstName(subcontractContactDetailsBean.getFirstName());
                }
                if (subcontractContactDetailsBean.getLastName() != null) {
                    rolodexDetailsType.setLastName(subcontractContactDetailsBean.getLastName());
                }
                if (subcontractContactDetailsBean.getMiddleName() != null) {
                    rolodexDetailsType.setMiddleName(subcontractContactDetailsBean.getMiddleName());
                }
                if (subcontractContactDetailsBean.getOrganization() != null) {
                    rolodexDetailsType.setOrganization(subcontractContactDetailsBean.getOrganization());
                }
                
                rolodexDetailsType.setRolodexId(subcontractContactDetailsBean.getRolodexId());
                if(subcontractContactDetailsBean.getStateName() !=null){
                    rolodexDetailsType.setStateDescription(subcontractContactDetailsBean.getStateName());
                }
                
                int rolodxId = subcontractContactDetailsBean.getRolodexId();
                RolodexMaintenanceDataTxnBean rolodexMaintenanceDataTxnBean = new RolodexMaintenanceDataTxnBean();
                String rolodexId = String.valueOf(rolodxId);
                RolodexDetailsBean rolodexBean = rolodexMaintenanceDataTxnBean.getRolodexMaintenanceDetails(rolodexId);
                if (rolodexBean.getOwnedByUnit() != null) {
                    rolodexDetailsType.setOwnedByUnit(rolodexBean.getOwnedByUnit());
                }
                if (rolodexBean.getPhone() != null) {
                    rolodexDetailsType.setPhoneNumber(rolodexBean.getPhone());
                }
                if (rolodexBean.getPostalCode() != null) {
                    rolodexDetailsType.setPincode(rolodexBean.getPostalCode());
                }
                if (rolodexBean.getPrefix() != null) {
                    rolodexDetailsType.setPrefix(rolodexBean.getPrefix());
                }
                if (rolodexBean.getSponsorCode() != null) {
                    rolodexDetailsType.setSponsorCode(rolodexBean.getSponsorCode());
                }
                if (rolodexBean.getSponsorName() != null) {
                    rolodexDetailsType.setSponsorName(rolodexBean.getSponsorName());
                }
                if (rolodexBean.getState() != null) {
                    rolodexDetailsType.setStateCode(rolodexBean.getState());
                }
                if (rolodexBean.getSuffix() != null) {
                    rolodexDetailsType.setSuffix(rolodexBean.getSuffix());
                }
                if (rolodexBean.getTitle() != null) {
                    rolodexDetailsType.setTitle(rolodexBean.getTitle());
                }
                
                if(rolodexBean.getCountry() !=null ){
                    rolodexDetailsType.setCounty(rolodexBean.getCountry());
                }
                if(rolodexBean.getOwnedByUnit() !=null){
                    UnitDataTxnBean unitDataTxnBean = new UnitDataTxnBean();
                    UnitDetailFormBean unitDetailFormBean = unitDataTxnBean.getUnitDetails(rolodexBean.getOwnedByUnit());
                    unitDetailFormBean.getUnitName();
                    if (unitDetailFormBean.getUnitName() != null) {
                        rolodexDetailsType.setOwnedByUnitName(unitDetailFormBean.getUnitName());
                    }
                }
                
            }
            
            primeFinancialContactType.setRolodexDetails(rolodexDetailsType);
        }
        subContractDataType.getPrimeFinancialContact().add(primeFinancialContactType);
        
        return subContractDataType;
    }
    
    /**
     * Methos to set prime authorized official
     * @param cvRolodexData 
     * @param subContractBean 
     * @param subContractDataType 
     * @throws javax.xml.bind.JAXBException 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return subContractDataType
     */
    private SubContractDataType getPrimeAuthorizedOfficial(CoeusVector cvRolodexData, SubContractBean subContractBean, SubContractDataType subContractDataType) throws JAXBException, CoeusException, DBException {
        
        SubContractDataType.PrimeAuthorizedOfficialType primeAuthorizedOfficialType = subContractObjFactory.createSubContractDataTypePrimeAuthorizedOfficialType();
        RolodexDetailsType rolodexDetailsType = null;
        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        String primeAuthorizedContactOption = coeusFunctions.getParameterValue(CoeusConstants.FDP_PRIME_AUTHORIZED_OFFICIAL_CODE);
        int contactTypeCode;
        
        
        if( primeAuthorizedContactOption != null && cvRolodexData !=null && !cvRolodexData.isEmpty()){
            try{
                contactTypeCode = Integer.parseInt(primeAuthorizedContactOption);
            }catch(NumberFormatException ex){
                contactTypeCode = 0;
            }
            Equals eqContactTypeCode = new Equals("contactTypeCode",contactTypeCode);
            CoeusVector cvFilterContacts = cvRolodexData.filter(eqContactTypeCode);
            cvFilterContacts.sort("updateTimestamp",false);
            
            if(cvFilterContacts !=null && !cvFilterContacts.isEmpty()){
                
                SubcontractContactDetailsBean subcontractContactDetailsBean = (SubcontractContactDetailsBean) cvFilterContacts.get(0);
                
                primeAuthorizedOfficialType.setContactTypeCode(String.valueOf(subcontractContactDetailsBean.getContactTypeCode()));
                
                rolodexDetailsType = subContractObjFactory.createRolodexDetailsType();
                primeAuthorizedOfficialType.setContactTypeDesc(getContractTypeCodeDescription(subcontractContactDetailsBean.getContactTypeCode()));
                
                rolodexDetailsType.setRolodexName(getRolodexName(subcontractContactDetailsBean));
                if (subcontractContactDetailsBean.getAddress1() != null) {
                    rolodexDetailsType.setAddress1(subcontractContactDetailsBean.getAddress1());
                }
                if (subcontractContactDetailsBean.getAddress2() != null) {
                    rolodexDetailsType.setAddress2(subcontractContactDetailsBean.getAddress2());
                }
                if (subcontractContactDetailsBean.getAddress3() != null) {
                    rolodexDetailsType.setAddress3(subcontractContactDetailsBean.getAddress3());
                }
                if (subcontractContactDetailsBean.getCity() != null) {
                    rolodexDetailsType.setCity(subcontractContactDetailsBean.getCity());
                }
                if (subcontractContactDetailsBean.getComments() != null) {
                    rolodexDetailsType.setComments(subcontractContactDetailsBean.getComments());
                }
                if (subcontractContactDetailsBean.getCountryCode() != null) {
                    rolodexDetailsType.setCountryCode(subcontractContactDetailsBean.getCountryCode());
                }
                if (subcontractContactDetailsBean.getCountryName() != null) {
                    rolodexDetailsType.setCountryDescription(subcontractContactDetailsBean.getCountryName());
                }
                if (subcontractContactDetailsBean.getCounty() != null) {
                    rolodexDetailsType.setCounty(subcontractContactDetailsBean.getCounty());
                }
                if (subcontractContactDetailsBean.getEmailAddress() != null) {
                    rolodexDetailsType.setEmail(subcontractContactDetailsBean.getEmailAddress());
                }
                if (subcontractContactDetailsBean.getFaxNumber() != null) {
                    rolodexDetailsType.setFax(subcontractContactDetailsBean.getFaxNumber());
                }
                if (subcontractContactDetailsBean.getFirstName() != null) {
                    rolodexDetailsType.setFirstName(subcontractContactDetailsBean.getFirstName());
                }
                if (subcontractContactDetailsBean.getLastName() != null) {
                    rolodexDetailsType.setLastName(subcontractContactDetailsBean.getLastName());
                }
                if (subcontractContactDetailsBean.getMiddleName() != null) {
                    rolodexDetailsType.setMiddleName(subcontractContactDetailsBean.getMiddleName());
                }
                if (subcontractContactDetailsBean.getOrganization() != null) {
                    rolodexDetailsType.setOrganization(subcontractContactDetailsBean.getOrganization());
                }
                
                rolodexDetailsType.setRolodexId(subcontractContactDetailsBean.getRolodexId());
                if(subcontractContactDetailsBean.getStateName() !=null){
                    rolodexDetailsType.setStateDescription(subcontractContactDetailsBean.getStateName());
                }
                
                int rolodxId = subcontractContactDetailsBean.getRolodexId();
                RolodexMaintenanceDataTxnBean rolodexMaintenanceDataTxnBean = new RolodexMaintenanceDataTxnBean();
                String rolodexId = String.valueOf(rolodxId);
                RolodexDetailsBean rolodexBean = rolodexMaintenanceDataTxnBean.getRolodexMaintenanceDetails(rolodexId);
                if (rolodexBean.getOwnedByUnit() != null) {
                    rolodexDetailsType.setOwnedByUnit(rolodexBean.getOwnedByUnit());
                }
                if (rolodexBean.getPhone() != null) {
                    rolodexDetailsType.setPhoneNumber(rolodexBean.getPhone());
                }
                if (rolodexBean.getPostalCode() != null) {
                    rolodexDetailsType.setPincode(rolodexBean.getPostalCode());
                }
                if (rolodexBean.getPrefix() != null) {
                    rolodexDetailsType.setPrefix(rolodexBean.getPrefix());
                }
                if (rolodexBean.getSponsorCode() != null) {
                    rolodexDetailsType.setSponsorCode(rolodexBean.getSponsorCode());
                }
                if (rolodexBean.getSponsorName() != null) {
                    rolodexDetailsType.setSponsorName(rolodexBean.getSponsorName());
                }
                if (rolodexBean.getState() != null) {
                    rolodexDetailsType.setStateCode(rolodexBean.getState());
                }
                if (rolodexBean.getSuffix() != null) {
                    rolodexDetailsType.setSuffix(rolodexBean.getSuffix());
                }
                if (rolodexBean.getTitle() != null) {
                    rolodexDetailsType.setTitle(rolodexBean.getTitle());
                }
                
                if(rolodexBean.getCountry() !=null ){
                    rolodexDetailsType.setCounty(rolodexBean.getCountry());
                }
                if(rolodexBean.getOwnedByUnit() !=null){
                    UnitDataTxnBean unitDataTxnBean = new UnitDataTxnBean();
                    UnitDetailFormBean unitDetailFormBean = unitDataTxnBean.getUnitDetails(rolodexBean.getOwnedByUnit());
                    unitDetailFormBean.getUnitName();
                    if (unitDetailFormBean.getUnitName() != null) {
                        rolodexDetailsType.setOwnedByUnitName(unitDetailFormBean.getUnitName());
                    }
                }
                
            }
            
            primeAuthorizedOfficialType.setRolodexDetails(rolodexDetailsType);
        }
        subContractDataType.getPrimeAuthorizedOfficial().add(primeAuthorizedOfficialType);
        
        return subContractDataType;
    }
    
    
    /**
     * Method to set administrative contact
     * @param cvRolodexData 
     * @param subContractBean 
     * @param subContractDataType 
     * @throws javax.xml.bind.JAXBException 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return subContractDataType
     */
    private SubContractDataType getAdministrativeContact(CoeusVector cvRolodexData, SubContractBean subContractBean, SubContractDataType subContractDataType) throws JAXBException, CoeusException, DBException {
        
        SubContractDataType.AdministrativeContactType administrativeContactType = subContractObjFactory.createSubContractDataTypeAdministrativeContactType();
        RolodexDetailsType rolodexDetailsType = null;
        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        String strContactOption = coeusFunctions.getParameterValue(CoeusConstants.FDP_SUB_ADMINISTRATIVE_CONTACT_CODE);
        int contactTypeCode;
        
        
        if( strContactOption != null && cvRolodexData !=null && !cvRolodexData.isEmpty()){
            try{
                contactTypeCode = Integer.parseInt(strContactOption);
            }catch(NumberFormatException ex){
                contactTypeCode = 0;
            }
            Equals eqContactTypeCode = new Equals("contactTypeCode",contactTypeCode);
            CoeusVector cvFilterContacts = cvRolodexData.filter(eqContactTypeCode);
            cvFilterContacts.sort("updateTimestamp",false);
            
            if(cvFilterContacts !=null && !cvFilterContacts.isEmpty()){
                
                SubcontractContactDetailsBean subcontractContactDetailsBean = (SubcontractContactDetailsBean) cvFilterContacts.get(0);
                
                administrativeContactType.setContactTypeCode(String.valueOf(subcontractContactDetailsBean.getContactTypeCode()));
                
                rolodexDetailsType = subContractObjFactory.createRolodexDetailsType();
                administrativeContactType.setContactTypeDesc(getContractTypeCodeDescription(subcontractContactDetailsBean.getContactTypeCode()));
                
                rolodexDetailsType.setRolodexName(getRolodexName(subcontractContactDetailsBean));
                if (subcontractContactDetailsBean.getAddress1() != null) {
                    rolodexDetailsType.setAddress1(subcontractContactDetailsBean.getAddress1());
                }
                if (subcontractContactDetailsBean.getAddress2() != null) {
                    rolodexDetailsType.setAddress2(subcontractContactDetailsBean.getAddress2());
                }
                if (subcontractContactDetailsBean.getAddress3() != null) {
                    rolodexDetailsType.setAddress3(subcontractContactDetailsBean.getAddress3());
                }
                if (subcontractContactDetailsBean.getCity() != null) {
                    rolodexDetailsType.setCity(subcontractContactDetailsBean.getCity());
                }
                if (subcontractContactDetailsBean.getComments() != null) {
                    rolodexDetailsType.setComments(subcontractContactDetailsBean.getComments());
                }
                if (subcontractContactDetailsBean.getCountryCode() != null) {
                    rolodexDetailsType.setCountryCode(subcontractContactDetailsBean.getCountryCode());
                }
                if (subcontractContactDetailsBean.getCountryName() != null) {
                    rolodexDetailsType.setCountryDescription(subcontractContactDetailsBean.getCountryName());
                }
                if (subcontractContactDetailsBean.getCounty() != null) {
                    rolodexDetailsType.setCounty(subcontractContactDetailsBean.getCounty());
                }
                if (subcontractContactDetailsBean.getEmailAddress() != null) {
                    rolodexDetailsType.setEmail(subcontractContactDetailsBean.getEmailAddress());
                }
                if (subcontractContactDetailsBean.getFaxNumber() != null) {
                    rolodexDetailsType.setFax(subcontractContactDetailsBean.getFaxNumber());
                }
                if (subcontractContactDetailsBean.getFirstName() != null) {
                    rolodexDetailsType.setFirstName(subcontractContactDetailsBean.getFirstName());
                }
                if (subcontractContactDetailsBean.getLastName() != null) {
                    rolodexDetailsType.setLastName(subcontractContactDetailsBean.getLastName());
                }
                if (subcontractContactDetailsBean.getMiddleName() != null) {
                    rolodexDetailsType.setMiddleName(subcontractContactDetailsBean.getMiddleName());
                }
                if (subcontractContactDetailsBean.getOrganization() != null) {
                    rolodexDetailsType.setOrganization(subcontractContactDetailsBean.getOrganization());
                }
                
                rolodexDetailsType.setRolodexId(subcontractContactDetailsBean.getRolodexId());
                if(subcontractContactDetailsBean.getStateName() !=null){
                    rolodexDetailsType.setStateDescription(subcontractContactDetailsBean.getStateName());
                }
                
                int rolodxId = subcontractContactDetailsBean.getRolodexId();
                RolodexMaintenanceDataTxnBean rolodexMaintenanceDataTxnBean = new RolodexMaintenanceDataTxnBean();
                String rolodexId = String.valueOf(rolodxId);
                RolodexDetailsBean rolodexBean = rolodexMaintenanceDataTxnBean.getRolodexMaintenanceDetails(rolodexId);
                if (rolodexBean.getOwnedByUnit() != null) {
                    rolodexDetailsType.setOwnedByUnit(rolodexBean.getOwnedByUnit());
                }
                if (rolodexBean.getPhone() != null) {
                    rolodexDetailsType.setPhoneNumber(rolodexBean.getPhone());
                }
                if (rolodexBean.getPostalCode() != null) {
                    rolodexDetailsType.setPincode(rolodexBean.getPostalCode());
                }
                if (rolodexBean.getPrefix() != null) {
                    rolodexDetailsType.setPrefix(rolodexBean.getPrefix());
                }
                if (rolodexBean.getSponsorCode() != null) {
                    rolodexDetailsType.setSponsorCode(rolodexBean.getSponsorCode());
                }
                if (rolodexBean.getSponsorName() != null) {
                    rolodexDetailsType.setSponsorName(rolodexBean.getSponsorName());
                }
                if (rolodexBean.getState() != null) {
                    rolodexDetailsType.setStateCode(rolodexBean.getState());
                }
                if (rolodexBean.getSuffix() != null) {
                    rolodexDetailsType.setSuffix(rolodexBean.getSuffix());
                }
                if (rolodexBean.getTitle() != null) {
                    rolodexDetailsType.setTitle(rolodexBean.getTitle());
                }
                
                if(rolodexBean.getCountry() !=null ){
                    rolodexDetailsType.setCounty(rolodexBean.getCountry());
                }
                if(rolodexBean.getOwnedByUnit() !=null){
                    UnitDataTxnBean unitDataTxnBean = new UnitDataTxnBean();
                    UnitDetailFormBean unitDetailFormBean = unitDataTxnBean.getUnitDetails(rolodexBean.getOwnedByUnit());
                    unitDetailFormBean.getUnitName();
                    if (unitDetailFormBean.getUnitName() != null) {
                        rolodexDetailsType.setOwnedByUnitName(unitDetailFormBean.getUnitName());
                    }
                }
                
            }
            
            administrativeContactType.setRolodexDetails(rolodexDetailsType);
        }
        subContractDataType.getAdministrativeContact().add(administrativeContactType);
        
        return subContractDataType;
    }
    
    /**
     * Method to set financialcontact
     * @param cvRolodexData 
     * @param subContractBean 
     * @param subContractDataType 
     * @throws javax.xml.bind.JAXBException 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return subContractDataType
     */
    private SubContractDataType getFinancialContact(CoeusVector cvRolodexData, SubContractBean subContractBean, SubContractDataType subContractDataType) throws JAXBException, CoeusException, DBException {
        
        SubContractDataType.FinancialContactType financialContactType = subContractObjFactory.createSubContractDataTypeFinancialContactType();
        RolodexDetailsType rolodexDetailsType = null;
        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        String strContactOption = coeusFunctions.getParameterValue(CoeusConstants.FDP_SUB_FINANCIAL_CONTACT_CODE);
        int contactTypeCode;
        
        
        if( strContactOption != null && cvRolodexData !=null && !cvRolodexData.isEmpty()){
            try{
                contactTypeCode = Integer.parseInt(strContactOption);
            }catch(NumberFormatException ex){
                contactTypeCode = 0;
            }
            Equals eqContactTypeCode = new Equals("contactTypeCode",contactTypeCode);
            CoeusVector cvFilterContacts = cvRolodexData.filter(eqContactTypeCode);
            cvFilterContacts.sort("updateTimestamp",false);
            
            if(cvFilterContacts !=null && !cvFilterContacts.isEmpty()){
                
                SubcontractContactDetailsBean subcontractContactDetailsBean = (SubcontractContactDetailsBean) cvFilterContacts.get(0);
                
                financialContactType.setContactTypeCode(String.valueOf(subcontractContactDetailsBean.getContactTypeCode()));
                
                rolodexDetailsType = subContractObjFactory.createRolodexDetailsType();
                financialContactType.setContactTypeDesc(getContractTypeCodeDescription(subcontractContactDetailsBean.getContactTypeCode()));
                
                rolodexDetailsType.setRolodexName(getRolodexName(subcontractContactDetailsBean));
                if (subcontractContactDetailsBean.getAddress1() != null) {
                    rolodexDetailsType.setAddress1(subcontractContactDetailsBean.getAddress1());
                }
                if (subcontractContactDetailsBean.getAddress2() != null) {
                    rolodexDetailsType.setAddress2(subcontractContactDetailsBean.getAddress2());
                }
                if (subcontractContactDetailsBean.getAddress3() != null) {
                    rolodexDetailsType.setAddress3(subcontractContactDetailsBean.getAddress3());
                }
                if (subcontractContactDetailsBean.getCity() != null) {
                    rolodexDetailsType.setCity(subcontractContactDetailsBean.getCity());
                }
                if (subcontractContactDetailsBean.getComments() != null) {
                    rolodexDetailsType.setComments(subcontractContactDetailsBean.getComments());
                }
                if (subcontractContactDetailsBean.getCountryCode() != null) {
                    rolodexDetailsType.setCountryCode(subcontractContactDetailsBean.getCountryCode());
                }
                if (subcontractContactDetailsBean.getCountryName() != null) {
                    rolodexDetailsType.setCountryDescription(subcontractContactDetailsBean.getCountryName());
                }
                if (subcontractContactDetailsBean.getCounty() != null) {
                    rolodexDetailsType.setCounty(subcontractContactDetailsBean.getCounty());
                }
                if (subcontractContactDetailsBean.getEmailAddress() != null) {
                    rolodexDetailsType.setEmail(subcontractContactDetailsBean.getEmailAddress());
                }
                if (subcontractContactDetailsBean.getFaxNumber() != null) {
                    rolodexDetailsType.setFax(subcontractContactDetailsBean.getFaxNumber());
                }
                if (subcontractContactDetailsBean.getFirstName() != null) {
                    rolodexDetailsType.setFirstName(subcontractContactDetailsBean.getFirstName());
                }
                if (subcontractContactDetailsBean.getLastName() != null) {
                    rolodexDetailsType.setLastName(subcontractContactDetailsBean.getLastName());
                }
                if (subcontractContactDetailsBean.getMiddleName() != null) {
                    rolodexDetailsType.setMiddleName(subcontractContactDetailsBean.getMiddleName());
                }
                if (subcontractContactDetailsBean.getOrganization() != null) {
                    rolodexDetailsType.setOrganization(subcontractContactDetailsBean.getOrganization());
                }
                
                rolodexDetailsType.setRolodexId(subcontractContactDetailsBean.getRolodexId());
                if(subcontractContactDetailsBean.getStateName() !=null){
                    rolodexDetailsType.setStateDescription(subcontractContactDetailsBean.getStateName());
                }
                
                int rolodxId = subcontractContactDetailsBean.getRolodexId();
                RolodexMaintenanceDataTxnBean rolodexMaintenanceDataTxnBean = new RolodexMaintenanceDataTxnBean();
                String rolodexId = String.valueOf(rolodxId);
                RolodexDetailsBean rolodexBean = rolodexMaintenanceDataTxnBean.getRolodexMaintenanceDetails(rolodexId);
                if (rolodexBean.getOwnedByUnit() != null) {
                    rolodexDetailsType.setOwnedByUnit(rolodexBean.getOwnedByUnit());
                }
                if (rolodexBean.getPhone() != null) {
                    rolodexDetailsType.setPhoneNumber(rolodexBean.getPhone());
                }
                if (rolodexBean.getPostalCode() != null) {
                    rolodexDetailsType.setPincode(rolodexBean.getPostalCode());
                }
                if (rolodexBean.getPrefix() != null) {
                    rolodexDetailsType.setPrefix(rolodexBean.getPrefix());
                }
                if (rolodexBean.getSponsorCode() != null) {
                    rolodexDetailsType.setSponsorCode(rolodexBean.getSponsorCode());
                }
                if (rolodexBean.getSponsorName() != null) {
                    rolodexDetailsType.setSponsorName(rolodexBean.getSponsorName());
                }
                if (rolodexBean.getState() != null) {
                    rolodexDetailsType.setStateCode(rolodexBean.getState());
                }
                if (rolodexBean.getSuffix() != null) {
                    rolodexDetailsType.setSuffix(rolodexBean.getSuffix());
                }
                if (rolodexBean.getTitle() != null) {
                    rolodexDetailsType.setTitle(rolodexBean.getTitle());
                }
                
                if(rolodexBean.getCountry() !=null ){
                    rolodexDetailsType.setCounty(rolodexBean.getCountry());
                }
                if(rolodexBean.getOwnedByUnit() !=null){
                    UnitDataTxnBean unitDataTxnBean = new UnitDataTxnBean();
                    UnitDetailFormBean unitDetailFormBean = unitDataTxnBean.getUnitDetails(rolodexBean.getOwnedByUnit());
                    unitDetailFormBean.getUnitName();
                    if (unitDetailFormBean.getUnitName() != null) {
                        rolodexDetailsType.setOwnedByUnitName(unitDetailFormBean.getUnitName());
                    }
                }
                
            }
            
            financialContactType.setRolodexDetails(rolodexDetailsType);
        }
        subContractDataType.getFinancialContact().add(financialContactType);
        
        return subContractDataType;
    }
    
    /**
     * Method to set authorized offical
     * @param cvRolodexData 
     * @param subContractBean 
     * @param subContractDataType 
     * @throws javax.xml.bind.JAXBException 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return subContractDataType
     */
    private SubContractDataType getAuthorizedOfficial(CoeusVector cvRolodexData, SubContractBean subContractBean, SubContractDataType subContractDataType) throws JAXBException, CoeusException, DBException {
        
        SubContractDataType.AuthorizedOfficialType authorizedOfficialType = subContractObjFactory.createSubContractDataTypeAuthorizedOfficialType();
        RolodexDetailsType rolodexDetailsType = null;
        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        String strContactOption = coeusFunctions.getParameterValue(CoeusConstants.FDP_SUB_AUTHORIZED_OFFICIAL_CODE);
        int contactTypeCode;
        
        
        if( strContactOption != null && cvRolodexData !=null && !cvRolodexData.isEmpty()){
            try{
                contactTypeCode = Integer.parseInt(strContactOption);
            }catch(NumberFormatException ex){
                contactTypeCode = 0;
            }
            Equals eqContactTypeCode = new Equals("contactTypeCode",contactTypeCode);
            CoeusVector cvFilterContacts = cvRolodexData.filter(eqContactTypeCode);
            cvFilterContacts.sort("updateTimestamp",false);
            
            if(cvFilterContacts !=null && !cvFilterContacts.isEmpty()){
                
                SubcontractContactDetailsBean subcontractContactDetailsBean = (SubcontractContactDetailsBean) cvFilterContacts.get(0);
                
                authorizedOfficialType.setContactTypeCode(String.valueOf(subcontractContactDetailsBean.getContactTypeCode()));
                
                rolodexDetailsType = subContractObjFactory.createRolodexDetailsType();
                authorizedOfficialType.setContactTypeDesc(getContractTypeCodeDescription(subcontractContactDetailsBean.getContactTypeCode()));
                
                rolodexDetailsType.setRolodexName(getRolodexName(subcontractContactDetailsBean));
                
                if (subcontractContactDetailsBean.getAddress1() != null) {
                    rolodexDetailsType.setAddress1(subcontractContactDetailsBean.getAddress1());
                }
                if (subcontractContactDetailsBean.getAddress2() != null) {
                    rolodexDetailsType.setAddress2(subcontractContactDetailsBean.getAddress2());
                }
                if (subcontractContactDetailsBean.getAddress3() != null) {
                    rolodexDetailsType.setAddress3(subcontractContactDetailsBean.getAddress3());
                }
                if (subcontractContactDetailsBean.getCity() != null) {
                    rolodexDetailsType.setCity(subcontractContactDetailsBean.getCity());
                }
                if (subcontractContactDetailsBean.getComments() != null) {
                    rolodexDetailsType.setComments(subcontractContactDetailsBean.getComments());
                }
                if (subcontractContactDetailsBean.getCountryCode() != null) {
                    rolodexDetailsType.setCountryCode(subcontractContactDetailsBean.getCountryCode());
                }
                if (subcontractContactDetailsBean.getCountryName() != null) {
                    rolodexDetailsType.setCountryDescription(subcontractContactDetailsBean.getCountryName());
                }
                if (subcontractContactDetailsBean.getCounty() != null) {
                    rolodexDetailsType.setCounty(subcontractContactDetailsBean.getCounty());
                }
                if (subcontractContactDetailsBean.getEmailAddress() != null) {
                    rolodexDetailsType.setEmail(subcontractContactDetailsBean.getEmailAddress());
                }
                if (subcontractContactDetailsBean.getFaxNumber() != null) {
                    rolodexDetailsType.setFax(subcontractContactDetailsBean.getFaxNumber());
                }
                if (subcontractContactDetailsBean.getFirstName() != null) {
                    rolodexDetailsType.setFirstName(subcontractContactDetailsBean.getFirstName());
                }
                if (subcontractContactDetailsBean.getLastName() != null) {
                    rolodexDetailsType.setLastName(subcontractContactDetailsBean.getLastName());
                }
                if (subcontractContactDetailsBean.getMiddleName() != null) {
                    rolodexDetailsType.setMiddleName(subcontractContactDetailsBean.getMiddleName());
                }
                if (subcontractContactDetailsBean.getOrganization() != null) {
                    rolodexDetailsType.setOrganization(subcontractContactDetailsBean.getOrganization());
                }
                
                rolodexDetailsType.setRolodexId(subcontractContactDetailsBean.getRolodexId());
                if(subcontractContactDetailsBean.getStateName() !=null){
                    rolodexDetailsType.setStateDescription(subcontractContactDetailsBean.getStateName());
                }
                
                int rolodxId = subcontractContactDetailsBean.getRolodexId();
                RolodexMaintenanceDataTxnBean rolodexMaintenanceDataTxnBean = new RolodexMaintenanceDataTxnBean();
                String rolodexId = String.valueOf(rolodxId);
                RolodexDetailsBean rolodexBean = rolodexMaintenanceDataTxnBean.getRolodexMaintenanceDetails(rolodexId);
                if (rolodexBean.getOwnedByUnit() != null) {
                    rolodexDetailsType.setOwnedByUnit(rolodexBean.getOwnedByUnit());
                }
                if (rolodexBean.getPhone() != null) {
                    rolodexDetailsType.setPhoneNumber(rolodexBean.getPhone());
                }
                if (rolodexBean.getPostalCode() != null) {
                    rolodexDetailsType.setPincode(rolodexBean.getPostalCode());
                }
                if (rolodexBean.getPrefix() != null) {
                    rolodexDetailsType.setPrefix(rolodexBean.getPrefix());
                }
                if (rolodexBean.getSponsorCode() != null) {
                    rolodexDetailsType.setSponsorCode(rolodexBean.getSponsorCode());
                }
                if (rolodexBean.getSponsorName() != null) {
                    rolodexDetailsType.setSponsorName(rolodexBean.getSponsorName());
                }
                if (rolodexBean.getState() != null) {
                    rolodexDetailsType.setStateCode(rolodexBean.getState());
                }
                if (rolodexBean.getSuffix() != null) {
                    rolodexDetailsType.setSuffix(rolodexBean.getSuffix());
                }
                if (rolodexBean.getTitle() != null) {
                    rolodexDetailsType.setTitle(rolodexBean.getTitle());
                }
                
                if(rolodexBean.getCountry() !=null ){
                    rolodexDetailsType.setCounty(rolodexBean.getCountry());
                }
                if(rolodexBean.getOwnedByUnit() !=null){
                    UnitDataTxnBean unitDataTxnBean = new UnitDataTxnBean();
                    UnitDetailFormBean unitDetailFormBean = unitDataTxnBean.getUnitDetails(rolodexBean.getOwnedByUnit());
                    unitDetailFormBean.getUnitName();
                    if (unitDetailFormBean.getUnitName() != null) {
                        rolodexDetailsType.setOwnedByUnitName(unitDetailFormBean.getUnitName());
                    }
                }
                
            }
            
            authorizedOfficialType.setRolodexDetails(rolodexDetailsType);
        }
        subContractDataType.getAuthorizedOfficial().add(authorizedOfficialType);
        
        return subContractDataType;
    }
    
    
    
    /**
     * Method to get prime recipient contacts details
     * @param subContractBean 
     * @param subContractDataType 
     * @throws javax.xml.bind.JAXBException 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @throws java.text.ParseException 
     * @return subContractDataType
     */
    private SubContractDataType getPrimeRecipientContactsDetail(SubContractBean subContractBean, SubContractDataType subContractDataType) throws JAXBException, CoeusException, DBException, ParseException {
        SubContractDataType.PrimeRecipientContactsType primeRecipientContactsType = subContractObjFactory.createSubContractDataTypePrimeRecipientContactsType();
        
        OrganizationMaintenanceDataTxnBean orgMaintenanceDataTxnBean = new OrganizationMaintenanceDataTxnBean();
        OrganizationType organizationType = subContractObjFactory.createOrganizationType();
        RolodexDetailsType rolodexDetailsType = subContractObjFactory.createRolodexDetailsType();
        
        if(subContractBean.getRequisitionerUnit() !=null ){
            
            primeRecipientContactsType.setRequisitionerUnitNumber(subContractBean.getRequisitionerUnit());
            primeRecipientContactsType.setRequisitionerUnitName(subContractBean.getRequisitionerUnitName());
            
            //Get the Organization based on the RequisitionerUnit.
            
            String  organizationId = orgMaintenanceDataTxnBean.getOrganizationId(subContractBean.getRequisitionerUnit());
            
            OrganizationMaintenanceFormBean orgFormBean = orgMaintenanceDataTxnBean.getOrganizationMaintenanceDetails(organizationId);
            
            organizationType.setOrganizationID(orgFormBean.getOrganizationId());
            organizationType.setOrganizationName(orgFormBean.getOrganizationName());
            organizationType.setContactAddressId(orgFormBean.getContactAddressId());
            if(orgFormBean.getAddress() !=null){
                organizationType.setAddress(orgFormBean.getAddress());
            }
            if(orgFormBean.getCableAddress() !=null){
                organizationType.setCableAddress(orgFormBean.getCableAddress());
            }
            if(orgFormBean.getTelexNumber() !=null){
                organizationType.setTelexNumber(orgFormBean.getTelexNumber());
            }
            if(orgFormBean.getCounty() !=null){
                organizationType.setCounty(orgFormBean.getCounty());
            }
            if(orgFormBean.getCongressionalDistrict() !=null){
                organizationType.setCongressionalDistrict(orgFormBean.getCongressionalDistrict());
            }
            if(orgFormBean.getIncorporatedIn() !=null){
                organizationType.setIncorporatedIn(orgFormBean.getIncorporatedIn());
            }
            
            organizationType.setNumberOfEmployees(orgFormBean.getNumberOfExmployees());
            
            if(orgFormBean.getIrsTaxExcemption() !=null){
                organizationType.setIrsTaxExcemption(orgFormBean.getIrsTaxExcemption());
            }
            if(orgFormBean.getFederalEmployerID() !=null){
                organizationType.setFedralEmployerId(orgFormBean.getFederalEmployerID());
            }
            if(orgFormBean.getMassTaxExcemptNum() !=null){
                organizationType.setMassTaxExcemptNum(orgFormBean.getMassTaxExcemptNum());
            }
            
            if(orgFormBean.getAgencySymbol() !=null){
                organizationType.setAgencySymbol(orgFormBean.getAgencySymbol());
            }
            if(orgFormBean.getVendorCode() !=null){
                organizationType.setVendorCode(orgFormBean.getVendorCode());
            }
            if(orgFormBean.getComGovEntityCode() !=null){
                organizationType.setComGovEntityCode(orgFormBean.getComGovEntityCode());
            }
            if(orgFormBean.getMassEmployeeClaim() !=null){
                organizationType.setMassEmployeeClaim(orgFormBean.getMassEmployeeClaim());
            }
            if(orgFormBean.getDunsNumber() !=null){
                organizationType.setDunsNumber(orgFormBean.getDunsNumber());
            }
            if(orgFormBean.getDunsPlusFourNumber() !=null){
                organizationType.setDunsPlusFourNumber(orgFormBean.getDunsPlusFourNumber());
            }
            if(orgFormBean.getDodacNumber() !=null){
                organizationType.setDodacNumber(orgFormBean.getDodacNumber());
            }
            if(orgFormBean.getCageNumber() !=null){
                organizationType.setCageNumber(orgFormBean.getCageNumber());
            }
            if(orgFormBean.getHumanSubAssurance() !=null){
                organizationType.setHumanSubAssurance(orgFormBean.getHumanSubAssurance());
            }
            if(orgFormBean.getAnimalWelfareAssurance() !=null){
                organizationType.setAnimalWelfareAssurance(orgFormBean.getAnimalWelfareAssurance());
            }
            
            //SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Calendar scienceMisconductComplDate = null;
            if (orgFormBean.getScienceMisconductComplDate()!=null) {
                scienceMisconductComplDate = Calendar.getInstance();
                Date convertedDate = formatter.parse(orgFormBean.getScienceMisconductComplDate());
                scienceMisconductComplDate.setTime(convertedDate);
                organizationType.setScienceMisconductComplDate(scienceMisconductComplDate);
            }
            
            if(orgFormBean.getPhsAcount() !=null){
                organizationType.setPhsAcount(orgFormBean.getPhsAcount());
            }
            if(orgFormBean.getNsfInstitutionalCode() !=null){
                organizationType.setNsfInstitutionalCode(orgFormBean.getNsfInstitutionalCode());
            }
            if(orgFormBean.getIndirectCostRateAgreement() !=null){
                organizationType.setIndirectCostRateAgreement(orgFormBean.getIndirectCostRateAgreement());
            }
            organizationType.setOnrResidentRep(orgFormBean.getOnrResidentRep());
            
            
            primeRecipientContactsType.setRequisitionerOrgDetails(organizationType);
            
            RolodexMaintenanceDataTxnBean rolodexMaintenanceDataTxnBean = new RolodexMaintenanceDataTxnBean();
            RolodexDetailsBean rolodexDetailsBean = rolodexMaintenanceDataTxnBean.getRolodexMaintenanceDetails(String.valueOf(orgFormBean.getContactAddressId()));
            
            rolodexDetailsType.setRolodexName(getRolodexName(rolodexDetailsBean));
            
            if(rolodexDetailsBean.getFirstName() !=null){
                rolodexDetailsType.setFirstName(rolodexDetailsBean.getFirstName());
            }
            if(rolodexDetailsBean.getMiddleName() !=null){
                rolodexDetailsType.setMiddleName(rolodexDetailsBean.getMiddleName());
            }
            if(rolodexDetailsBean.getLastName() !=null){
                rolodexDetailsType.setLastName(rolodexDetailsBean.getLastName());
            }
            if(rolodexDetailsBean.getAddress1() !=null){
                rolodexDetailsType.setAddress1(rolodexDetailsBean.getAddress1());
            }
            if(rolodexDetailsBean.getAddress2() !=null){
                rolodexDetailsType.setAddress2(rolodexDetailsBean.getAddress2());
            }
            if(rolodexDetailsBean.getAddress3() !=null){
                rolodexDetailsType.setAddress3(rolodexDetailsBean.getAddress3());
            }
            if(rolodexDetailsBean.getCity() !=null){
                rolodexDetailsType.setCity(rolodexDetailsBean.getCity());
            }
            if(rolodexDetailsBean.getState() !=null){
                rolodexDetailsType.setStateDescription(rolodexDetailsBean.getState());
            }
            if(rolodexDetailsBean.getPostalCode() !=null){
                rolodexDetailsType.setPincode(rolodexDetailsBean.getPostalCode());
            }
            if(rolodexDetailsBean.getPrefix() !=null){
                rolodexDetailsType.setPrefix(rolodexDetailsBean.getPrefix());
            }
            if(rolodexDetailsBean.getFax() !=null){
                rolodexDetailsType.setFax(rolodexDetailsBean.getFax());
            }
            if(rolodexDetailsBean.getEMail() !=null){
                rolodexDetailsType.setEmail(rolodexDetailsBean.getEMail());
            }
            
            if (rolodexDetailsBean.getOwnedByUnit() != null) {
                rolodexDetailsType.setOwnedByUnit(rolodexDetailsBean.getOwnedByUnit());
            }
            if (rolodexDetailsBean.getPhone() != null) {
                rolodexDetailsType.setPhoneNumber(rolodexDetailsBean.getPhone());
            }
            if (rolodexDetailsBean.getPostalCode() != null) {
                rolodexDetailsType.setPincode(rolodexDetailsBean.getPostalCode());
            }
            if (rolodexDetailsBean.getPrefix() != null) {
                rolodexDetailsType.setPrefix(rolodexDetailsBean.getPrefix());
            }
            if (rolodexDetailsBean.getSponsorCode() != null) {
                rolodexDetailsType.setSponsorCode(rolodexDetailsBean.getSponsorCode());
            }
            if (rolodexDetailsBean.getSponsorName() != null) {
                rolodexDetailsType.setSponsorName(rolodexDetailsBean.getSponsorName());
            }
            if (rolodexDetailsBean.getState() != null) {
                rolodexDetailsType.setStateCode(rolodexDetailsBean.getState());
            }
            if (rolodexDetailsBean.getSuffix() != null) {
                rolodexDetailsType.setSuffix(rolodexDetailsBean.getSuffix());
            }
            if (rolodexDetailsBean.getTitle() != null) {
                rolodexDetailsType.setTitle(rolodexDetailsBean.getTitle());
            }
            
            if(rolodexDetailsBean.getCountry() !=null ){
                rolodexDetailsType.setCounty(rolodexDetailsBean.getCountry());
            }
            if(rolodexDetailsBean.getOwnedByUnit() !=null){
                UnitDataTxnBean unitDataTxnBean = new UnitDataTxnBean();
                UnitDetailFormBean unitDetailFormBean = unitDataTxnBean.getUnitDetails(rolodexDetailsBean.getOwnedByUnit());
                unitDetailFormBean.getUnitName();
                if (unitDetailFormBean.getUnitName() != null) {
                    rolodexDetailsType.setOwnedByUnitName(unitDetailFormBean.getUnitName());
                }
            }
            
            primeRecipientContactsType.setOrgRolodexDetails(rolodexDetailsType);
        }
        subContractDataType.setPrimeRecipientContacts(primeRecipientContactsType);
        
        return subContractDataType;
    }
    
    /**
     * Methos to get prime principal investigator details
     * @param subContractBean 
     * @param subContractDataType 
     * @param subContractFundingSourceBean 
     * @throws javax.xml.bind.JAXBException 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return subContractDataType
     */
    private SubContractDataType getPrimePrincipalInvestigatorDetails(SubContractBean subContractBean, SubContractDataType subContractDataType, SubContractFundingSourceBean subContractFundingSourceBean) throws JAXBException, CoeusException, DBException {
        SubContractTxnBean subContractTxnBean = new SubContractTxnBean();
        
        AwardTxnBean awardTxnBean = new AwardTxnBean();
        
        String mitAwardNumber = subContractFundingSourceBean.getMitAwardNumber();
        
        CoeusVector cvInvestigators = awardTxnBean.getAwardInvestigators(mitAwardNumber);
        PersonInfoTxnBean personInfoTxnBean = new PersonInfoTxnBean();
        if(cvInvestigators != null && !cvInvestigators.isEmpty()){
            for (int index=0;index<cvInvestigators.size();index++) {
                AwardInvestigatorsBean awardInvestigatorsBean = (AwardInvestigatorsBean)cvInvestigators.get(index);
                if (awardInvestigatorsBean.isPrincipalInvestigatorFlag()) {
                    PersonInfoFormBean personInfoFormBean = personInfoTxnBean.getPersonInfo(awardInvestigatorsBean.getPersonId());
                    PersonDetailsType principalInvestigatorType = subContractObjFactory.createPersonDetailsType();
                    if(personInfoFormBean !=null){
                        if( personInfoFormBean.getFirstName() !=null){
                            principalInvestigatorType.setFirstName( personInfoFormBean.getFirstName());
                        }
                        if( personInfoFormBean.getLastName() !=null){
                            principalInvestigatorType.setLastName( personInfoFormBean.getLastName());
                        }
                        if( personInfoFormBean.getFullName() !=null){
                            principalInvestigatorType.setFullName(personInfoFormBean.getFullName());
                        }
                        
                        if( personInfoFormBean.getAddressLine1() !=null){
                            principalInvestigatorType.setAddressLine1(personInfoFormBean.getAddressLine1());
                        }
                        
                        if( personInfoFormBean.getAddressLine2() !=null){
                            principalInvestigatorType.setAddressLine2(personInfoFormBean.getAddressLine2());
                        }
                        
                        if( personInfoFormBean.getAddressLine3() !=null){
                            principalInvestigatorType.setAddressLine3(personInfoFormBean.getAddressLine3());
                        }
                        if( personInfoFormBean.getOffPhone() !=null){
                            principalInvestigatorType.setOfficePhone( personInfoFormBean.getOffPhone());
                        }
                        if( personInfoFormBean.getSecOffPhone() !=null){
                            principalInvestigatorType.setSecondryOfficePhone( personInfoFormBean.getSecOffPhone());
                        }
                        if( personInfoFormBean.getMobilePhoneNumber() !=null){
                            principalInvestigatorType.setMobilePhoneNumber( personInfoFormBean.getMobilePhoneNumber());
                        }
                        if( personInfoFormBean.getEmail() !=null){
                            principalInvestigatorType.setEmailAddress( personInfoFormBean.getEmail());
                        }
                        if( personInfoFormBean.getDirTitle() !=null){
                            principalInvestigatorType.setDirectoryTitle( personInfoFormBean.getDirTitle());
                        }
                        if( personInfoFormBean.getFaxNumber() !=null){
                            principalInvestigatorType.setFaxNumber( personInfoFormBean.getFaxNumber());
                        }
                        if( personInfoFormBean.getCity() !=null){
                            principalInvestigatorType.setCity( personInfoFormBean.getCity());
                        }
                        if( personInfoFormBean.getState() !=null){
                            principalInvestigatorType.setState( personInfoFormBean.getState());
                        }
                        if( personInfoFormBean.getPostalCode() !=null){
                            principalInvestigatorType.setPostalCode( personInfoFormBean.getPostalCode());
                        }
                        if( personInfoFormBean.getHomeUnit()!=null){
                            principalInvestigatorType.setDepartmentName(subContractTxnBean.getUnitName(personInfoFormBean.getHomeUnit()));
                        }
                        subContractDataType.getPrimePrincipalInvestigator().add(principalInvestigatorType);
                    }
                    break;
                }
            }
        }
        
        return subContractDataType;
    }
    
    /**
     * Methos to get the Organization rolodex details
     * @param subContractBean 
     * @param subContractDataType 
     * @throws javax.xml.bind.JAXBException 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return rolodexDetailsType
     */
    private RolodexDetailsType getSubcontractorOrgRolodexDetails(SubContractBean subContractBean, SubContractDataType subContractDataType) throws JAXBException, CoeusException, DBException {
        
        
        RolodexDetailsType rolodexDetailsType = subContractObjFactory.createRolodexDetailsType();
        
        OrganizationMaintenanceDataTxnBean orgMaintenanceDataTxnBean = new OrganizationMaintenanceDataTxnBean();
        OrganizationMaintenanceFormBean orgFormBean = orgMaintenanceDataTxnBean.getOrganizationMaintenanceDetails(subContractBean.getSubContractId());
        RolodexMaintenanceDataTxnBean rolodexMaintenanceDataTxnBean = new RolodexMaintenanceDataTxnBean();
        RolodexDetailsBean rolodexDetailsBean = rolodexMaintenanceDataTxnBean.getRolodexMaintenanceDetails(String.valueOf(orgFormBean.getContactAddressId()));
        
        rolodexDetailsType.setRolodexName(getRolodexName(rolodexDetailsBean));
        
        if(rolodexDetailsBean.getFirstName() !=null){
            rolodexDetailsType.setFirstName(rolodexDetailsBean.getFirstName());
        }
        if(rolodexDetailsBean.getMiddleName() !=null){
            rolodexDetailsType.setMiddleName(rolodexDetailsBean.getMiddleName());
        }
        if(rolodexDetailsBean.getLastName() !=null){
            rolodexDetailsType.setLastName(rolodexDetailsBean.getLastName());
        }
        if(rolodexDetailsBean.getAddress1() !=null){
            rolodexDetailsType.setAddress1(rolodexDetailsBean.getAddress1());
        }
        if(rolodexDetailsBean.getAddress2() !=null){
            rolodexDetailsType.setAddress2(rolodexDetailsBean.getAddress2());
        }
        if(rolodexDetailsBean.getAddress3() !=null){
            rolodexDetailsType.setAddress3(rolodexDetailsBean.getAddress3());
        }
        if(rolodexDetailsBean.getCity() !=null){
            rolodexDetailsType.setCity(rolodexDetailsBean.getCity());
        }
        if(rolodexDetailsBean.getState() !=null){
            rolodexDetailsType.setStateDescription(rolodexDetailsBean.getState());
        }
        if(rolodexDetailsBean.getPostalCode() !=null){
            rolodexDetailsType.setPincode(rolodexDetailsBean.getPostalCode());
        }
        if(rolodexDetailsBean.getPrefix() !=null){
            rolodexDetailsType.setPrefix(rolodexDetailsBean.getPrefix());
        }
        if(rolodexDetailsBean.getFax() !=null){
            rolodexDetailsType.setFax(rolodexDetailsBean.getFax());
        }
        if(rolodexDetailsBean.getEMail() !=null){
            rolodexDetailsType.setEmail(rolodexDetailsBean.getEMail());
        }
        if (rolodexDetailsBean.getOwnedByUnit() != null) {
            rolodexDetailsType.setOwnedByUnit(rolodexDetailsBean.getOwnedByUnit());
        }
        if (rolodexDetailsBean.getPhone() != null) {
            rolodexDetailsType.setPhoneNumber(rolodexDetailsBean.getPhone());
        }
        if (rolodexDetailsBean.getPostalCode() != null) {
            rolodexDetailsType.setPincode(rolodexDetailsBean.getPostalCode());
        }
        if (rolodexDetailsBean.getPrefix() != null) {
            rolodexDetailsType.setPrefix(rolodexDetailsBean.getPrefix());
        }
        if (rolodexDetailsBean.getSponsorCode() != null) {
            rolodexDetailsType.setSponsorCode(rolodexDetailsBean.getSponsorCode());
        }
        if (rolodexDetailsBean.getSponsorName() != null) {
            rolodexDetailsType.setSponsorName(rolodexDetailsBean.getSponsorName());
        }
        if (rolodexDetailsBean.getState() != null) {
            rolodexDetailsType.setStateCode(rolodexDetailsBean.getState());
        }
        if (rolodexDetailsBean.getSuffix() != null) {
            rolodexDetailsType.setSuffix(rolodexDetailsBean.getSuffix());
        }
        if (rolodexDetailsBean.getTitle() != null) {
            rolodexDetailsType.setTitle(rolodexDetailsBean.getTitle());
        }
        
        if(rolodexDetailsBean.getCountry() !=null ){
            rolodexDetailsType.setCounty(rolodexDetailsBean.getCountry());
        }
        if(rolodexDetailsBean.getOwnedByUnit() !=null){
            UnitDataTxnBean unitDataTxnBean = new UnitDataTxnBean();
            UnitDetailFormBean unitDetailFormBean = unitDataTxnBean.getUnitDetails(rolodexDetailsBean.getOwnedByUnit());
            unitDetailFormBean.getUnitName();
            if (unitDetailFormBean.getUnitName() != null) {
                rolodexDetailsType.setOwnedByUnitName(unitDetailFormBean.getUnitName());
            }
        }
        
        
        
        return rolodexDetailsType;
    }
    
    /**
     * Method to get the site investigator details
     * @param subContractBean 
     * @param subContractDataType 
     * @throws javax.xml.bind.JAXBException 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return rolodexDetailsType
     */
    private RolodexDetailsType getSiteInvestigatorDetails(SubContractBean subContractBean, SubContractDataType subContractDataType) throws JAXBException, CoeusException, DBException {
        
        
        RolodexDetailsType rolodexDetailsType = subContractObjFactory.createRolodexDetailsType();
        
        OrganizationMaintenanceDataTxnBean orgMaintenanceDataTxnBean = new OrganizationMaintenanceDataTxnBean();
        
        RolodexMaintenanceDataTxnBean rolodexMaintenanceDataTxnBean = new RolodexMaintenanceDataTxnBean();
        RolodexDetailsBean rolodexDetailsBean = rolodexMaintenanceDataTxnBean.getRolodexMaintenanceDetails(String.valueOf(subContractBean.getSiteInvestigator()));
        
        rolodexDetailsType.setRolodexName(getRolodexName(rolodexDetailsBean));
        
        if(rolodexDetailsBean.getFirstName() !=null){
            rolodexDetailsType.setFirstName(rolodexDetailsBean.getFirstName());
        }
        if(rolodexDetailsBean.getMiddleName() !=null){
            rolodexDetailsType.setMiddleName(rolodexDetailsBean.getMiddleName());
        }
        if(rolodexDetailsBean.getLastName() !=null){
            rolodexDetailsType.setLastName(rolodexDetailsBean.getLastName());
        }
        if(rolodexDetailsBean.getAddress1() !=null){
            rolodexDetailsType.setAddress1(rolodexDetailsBean.getAddress1());
        }
        if(rolodexDetailsBean.getAddress2() !=null){
            rolodexDetailsType.setAddress2(rolodexDetailsBean.getAddress2());
        }
        if(rolodexDetailsBean.getAddress3() !=null){
            rolodexDetailsType.setAddress3(rolodexDetailsBean.getAddress3());
        }
        if(rolodexDetailsBean.getCity() !=null){
            rolodexDetailsType.setCity(rolodexDetailsBean.getCity());
        }
        if(rolodexDetailsBean.getState() !=null){
            rolodexDetailsType.setStateDescription(rolodexDetailsBean.getState());
        }
        if(rolodexDetailsBean.getPostalCode() !=null){
            rolodexDetailsType.setPincode(rolodexDetailsBean.getPostalCode());
        }
        if(rolodexDetailsBean.getPrefix() !=null){
            rolodexDetailsType.setPrefix(rolodexDetailsBean.getPrefix());
        }
        
        if (rolodexDetailsBean.getOwnedByUnit() != null) {
            rolodexDetailsType.setOwnedByUnit(rolodexDetailsBean.getOwnedByUnit());
        }
        if (rolodexDetailsBean.getPhone() != null) {
            rolodexDetailsType.setPhoneNumber(rolodexDetailsBean.getPhone());
        }
        if (rolodexDetailsBean.getPostalCode() != null) {
            rolodexDetailsType.setPincode(rolodexDetailsBean.getPostalCode());
        }
        if (rolodexDetailsBean.getPrefix() != null) {
            rolodexDetailsType.setPrefix(rolodexDetailsBean.getPrefix());
        }
        if (rolodexDetailsBean.getSponsorCode() != null) {
            rolodexDetailsType.setSponsorCode(rolodexDetailsBean.getSponsorCode());
        }
        if (rolodexDetailsBean.getSponsorName() != null) {
            rolodexDetailsType.setSponsorName(rolodexDetailsBean.getSponsorName());
        }
        if (rolodexDetailsBean.getState() != null) {
            rolodexDetailsType.setStateCode(rolodexDetailsBean.getState());
        }
        if (rolodexDetailsBean.getSuffix() != null) {
            rolodexDetailsType.setSuffix(rolodexDetailsBean.getSuffix());
        }
        if (rolodexDetailsBean.getTitle() != null) {
            rolodexDetailsType.setTitle(rolodexDetailsBean.getTitle());
        }
        if(rolodexDetailsBean.getFax() !=null){
            rolodexDetailsType.setFax(rolodexDetailsBean.getFax());
        }
        if(rolodexDetailsBean.getEMail() !=null){
            rolodexDetailsType.setEmail(rolodexDetailsBean.getEMail());
        }
        if(rolodexDetailsBean.getCountry() !=null ){
            rolodexDetailsType.setCounty(rolodexDetailsBean.getCountry());
        }
        if(rolodexDetailsBean.getOwnedByUnit() !=null){
            UnitDataTxnBean unitDataTxnBean = new UnitDataTxnBean();
            UnitDetailFormBean unitDetailFormBean = unitDataTxnBean.getUnitDetails(rolodexDetailsBean.getOwnedByUnit());
            unitDetailFormBean.getUnitName();
            if (unitDetailFormBean.getUnitName() != null) {
                rolodexDetailsType.setOwnedByUnitName(unitDetailFormBean.getUnitName());
            }
        }
        
        
        
        return rolodexDetailsType;
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
     * Method to get the contact type description
     * @param contactTypeCode 
     * @return contactTypeCodeDesc
     */
    private String getContractTypeCodeDescription(int contactTypeCode){
        String contactTypeCodeDesc = null;
        if(cvContactTypes != null && !cvContactTypes.isEmpty()){
            Equals eqContactCode = new Equals("typeCode",contactTypeCode);
            CoeusVector cvFilteredType = cvContactTypes.filter(eqContactCode);
            if(cvFilteredType != null && !cvFilteredType.isEmpty()){
                contactTypeCodeDesc = ((CoeusTypeBean)cvFilteredType.get(0)).getTypeDescription();
            }
        }
        
        return contactTypeCodeDesc;
    }
    
    
    /**
     * Method to get the contact types
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return cvContactTypes
     */
    private CoeusVector getContactTypes() throws CoeusException, DBException{
        SubContractTxnBean subContractTxnBean = new SubContractTxnBean();
        cvContactTypes = subContractTxnBean.getContactTypesForModule(ModuleConstants.SUBCONTRACTS_MODULE_CODE);
        return cvContactTypes;
    }
    
    /**
     * Method to get the rolodexName
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return cvContactTypes
     */
    private String getRolodexName(Object objBean){
        
        String rolodexName = null;
        String firstName = null;
        String lastName = null;
        String middleName = null;
        String nameSuffix = null;
        String namePreffix = null;
        String orgName = null;
        
        if (objBean instanceof RolodexDetailsBean){
            RolodexDetailsBean rolodexDetailsBean = (RolodexDetailsBean)objBean;
            firstName = checkForNull(rolodexDetailsBean.getFirstName());
            lastName = checkForNull(rolodexDetailsBean.getLastName());
            middleName = checkForNull(rolodexDetailsBean.getMiddleName());
            nameSuffix = checkForNull(rolodexDetailsBean.getSuffix());
            namePreffix = checkForNull(rolodexDetailsBean.getPrefix());
            orgName = rolodexDetailsBean.getOrganization();
        } else if(objBean instanceof SubcontractContactDetailsBean ){
            SubcontractContactDetailsBean subcontractContactDetailsBean = (SubcontractContactDetailsBean)objBean;
            firstName = checkForNull(subcontractContactDetailsBean.getFirstName());
            lastName = checkForNull(subcontractContactDetailsBean.getLastName());
            middleName = checkForNull(subcontractContactDetailsBean.getMiddleName());
            nameSuffix = checkForNull(subcontractContactDetailsBean.getSuffix());
            namePreffix = checkForNull(subcontractContactDetailsBean.getPrefix());
            orgName = subcontractContactDetailsBean.getOrganization();
        }
       /* construct full name of the rolodex if his last name is present
          otherwise use his organization name to display in person name
          column of investigator table */
        
        if ( lastName.length() > 0) {
            rolodexName = ( lastName + " "+nameSuffix +", "+ namePreffix
                    +" "+firstName +" "+ middleName ).trim();
        } else {
            rolodexName = orgName;
        }
        return rolodexName;
    }
    
    //supporting method to check for null value
    private String checkForNull( Object value ){
        return (value==null)? "":value.toString();        
    }

}

