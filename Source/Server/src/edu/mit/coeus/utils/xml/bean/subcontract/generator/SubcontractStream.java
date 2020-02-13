/*
 * InstPropStream.java
 *
 * Created on December 13, 2004, 4:02 PM
 */

package edu.mit.coeus.utils.xml.bean.subcontract.generator;

import edu.mit.coeus.award.bean.AwardHeaderBean;
import edu.mit.coeus.award.bean.AwardTxnBean;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceDataTxnBean;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceFormBean;
import edu.mit.coeus.rolodexmaint.bean.*;
import edu.mit.coeus.rolodexmaint.bean.RolodexMaintenanceDataTxnBean;
import edu.mit.coeus.subcontract.bean.*;
import edu.mit.coeus.unit.bean.UnitDataTxnBean;
import edu.mit.coeus.unit.bean.UnitDetailFormBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.xml.bean.subcontract.*;
import edu.mit.coeus.utils.xml.bean.subcontract.impl.*;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Calendar;
import java.util.HashMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.util.Hashtable;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class SubcontractStream {
	
	private ByteArrayOutputStream streamData;
	
	/** Creates a new instance of InstPropStream */
	public SubcontractStream() {
		
	}
	// Get the data , person name and mode from servlet and set the values to the
	//JAXBeans and add to the stream .
	public ByteArrayOutputStream getStreamData(Hashtable variableValues) throws JAXBException,CoeusException,DBException{
		SubContractDataType subContractDataType = new SubContractDataImpl();
		Calendar calendar = Calendar.getInstance();
		if (variableValues != null && variableValues.size() > 0) {
			
			CoeusVector cvDetailData = (CoeusVector)variableValues.get(SubContractBean.class);
			SubContractDataType.SubcontractDetailType detailType = new SubContractDataTypeImpl.SubcontractDetailTypeImpl();
                      // Added for  Case #3338 - Add new elements the person and rolodex details to Subcontract schema -Start
                        CoeusVector cvAuditorData = new CoeusVector();
                        String requisitionerUnit = null;
                        String requisitionerId = null;
                        String subContractId = null;
                        String subContractCode = null;
                        int subContractSequenceNo = 0;
                      // Added for  Case #3338 - Add new elements the person and rolodex details to Subcontract schema -End
			
			if (cvDetailData != null && cvDetailData.size() > 0) {
				for (int index = 0; index < cvDetailData.size(); index++) {
					SubContractBean subContractBean = (SubContractBean)cvDetailData.get(index);
					if (subContractBean.getAccountNumber() != null) {
						detailType.setAccountNumber(subContractBean.getAccountNumber());
					}
					if (subContractBean.getArchiveLocation() != null) {
						detailType.setArchiveLoc(subContractBean.getArchiveLocation());
					}
                                        //coeusqa-288 start
//					if (calendar != null && subContractBean.getCloseOutDate() != null) {
//						calendar.setTime(subContractBean.getCloseOutDate());
//						detailType.setCloseoutDate(calendar);
//					}
                                        if (subContractBean.getCloseOutDate()!=null) {
                                            Calendar closeDate= null;
                                            closeDate = Calendar.getInstance();
                                            closeDate.setTime(subContractBean.getCloseOutDate());
                                            detailType.setCloseoutDate(closeDate);
                                        }
                                        //coeusqa-2880 end
					if (subContractBean.getCloseOutIndicator() != null) {
						detailType.setCloseoutIndicator(subContractBean.getCloseOutIndicator());
					}
					if(subContractBean.getComments() != null) {
						detailType.setComments(subContractBean.getComments());
					}
                                        // Commented for  Case #3338 - Add new elements the person and rolodex details to Subcontract schema -Start
//					if (calendar != null && subContractBean.getEndDate() != null) {
//						calendar.setTime(subContractBean.getEndDate());
//						detailType.setEndDate(calendar);
//					}
                                        // Commented for  Case #3338 - Add new elements the person and rolodex details to Subcontract schema -End
                                       // Added for  Case #3338 - Add new elements the person and rolodex details to Subcontract schema -Start
                                        Calendar endDate= null;
                                        if (subContractBean.getEndDate()!=null) {
                                            endDate = Calendar.getInstance();
                                            endDate.setTime(subContractBean.getEndDate());
                                            detailType.setEndDate(endDate);
					}
                                        // Added for  Case #3338 - Add new elements the person and rolodex details to Subcontract schema -End
					if (subContractBean.getFundingSourceIndicator() != null) {
						detailType.setFundingSourceIndicator(subContractBean.getFundingSourceIndicator());
					}
					if (subContractBean.getPurchaseOrderNumber() != null) {
						detailType.setPONumber(subContractBean.getPurchaseOrderNumber());
					}
					if (subContractBean.getRequisitionerId() != null) {
						detailType.setRequistionerID(subContractBean.getRequisitionerId());
                                                requisitionerId = subContractBean.getRequisitionerId();
					}
					if (subContractBean.getRequisitionerName() != null) {
						detailType.setRequistionerName(subContractBean.getRequisitionerName());
					}
					if (subContractBean.getRequisitionerUnit() != null) {
						detailType.setRequistionerUnit(subContractBean.getRequisitionerUnit());
                                                requisitionerUnit = subContractBean.getRequisitionerUnit();
					}
                                        // Modified for  Case #3338 - Add new elements the person and rolodex details to Subcontract schema -Start
//					if (calendar != null && subContractBean.getStartDate() != null) {
//						calendar.setTime(subContractBean.getStartDate());
//						detailType.setStartDate(calendar);
//					}
                                        Calendar startDate= null;
                                        if (subContractBean.getStartDate()!=null) {
                                            startDate = Calendar.getInstance();
                                            startDate.setTime(subContractBean.getStartDate());
                                            detailType.setStartDate(startDate);
                                        }
                                        // Modified for  Case #3338 - Add new elements the person and rolodex details to Subcontract schema -End
					if (subContractBean.getSubAwardTypeDescription() != null) {
						detailType.setSubAwardType(subContractBean.getSubAwardTypeDescription());
					}
					if (subContractBean.getSubContractorName() != null) {
						detailType.setSubcontractorName(subContractBean.getSubContractorName());
					}
					if (subContractBean.getTitle() != null) {
						detailType.setTitle(subContractBean.getTitle());
					}
					if (subContractBean.getVendorNumber() != null) {
						detailType.setVendorNumber(subContractBean.getVendorNumber());
					}
					detailType.setSubContractCode(subContractBean.getSubContractCode());
                                        subContractCode = subContractBean.getSubContractCode();
                                        //Case #3338 - Add new elements the person and rolodex details to Subcontract schema -Start 
                                        detailType.setSequenceNumber( String.valueOf(subContractBean.getSequenceNumber()));
                                        subContractSequenceNo = subContractBean.getSequenceNumber();
                                        //SubcontractorID
                                        if(subContractBean.getSubContractId() !=null ){
                                            detailType.setSubcontractorID( subContractBean.getSubContractId()); 
                                            subContractId = subContractBean.getSubContractId();
                                        }
                                         //SubAwardTypeCode
                                        detailType.setSubAwardTypeCode(String.valueOf(subContractBean.getSubAwardTypeCode()));
                                        
                                        //StatusCode
                                        detailType.setStatusCode(String.valueOf(subContractBean.getStatusCode()));
                                        
                                       // detailType.setAcronym();
                                        //set Auditor Data from Subcontract bean - Get COGNIZANT_AUDITOR value from Organization table
                                        if(subContractBean.getAuditorData() !=null && subContractBean.getAuditorData().size() >0){
                                            cvAuditorData = subContractBean.getAuditorData();
                                        }
                                        
                                         //Case #3338 - Add new elements the person and rolodex details to Subcontract schema -End
					subContractDataType.setSubcontractDetail(detailType);
					
				}
			}
			
			CoeusVector cvRolodexData = (CoeusVector)variableValues.get(SubcontractContactDetailsBean.class);
                        //case 2901 start
//			SubContractDataType.SubcontractContactsType subcontractContactsType = new SubContractDataTypeImpl.SubcontractContactsTypeImpl();
//			SubContractDataType.SubcontractContactsType.RolodexDetailsType rolodexType = new SubContractDataTypeImpl.SubcontractContactsTypeImpl.RolodexDetailsTypeImpl();
                        SubContractDataType.SubcontractContactsType subcontractContactsType;
			SubContractDataType.SubcontractContactsType.RolodexDetailsType rolodexType;
			//case 2901 end
                        if (cvRolodexData != null && cvRolodexData.size() > 0) {
				for (int index = 0; index < cvRolodexData.size(); index++) {
					//case 2901 start
                                        subcontractContactsType = new SubContractDataTypeImpl.SubcontractContactsTypeImpl();
                                        rolodexType = new SubContractDataTypeImpl.SubcontractContactsTypeImpl.RolodexDetailsTypeImpl();
                                        //case 2901 end 
					SubcontractContactDetailsBean subcontractContactDetailsBean = (SubcontractContactDetailsBean)cvRolodexData.get(index);
					subcontractContactsType.setContactTypeCode(String.valueOf(subcontractContactDetailsBean.getContactTypeCode()));
					if (subcontractContactDetailsBean.getContactTypeDescription() != null) {
						subcontractContactsType.setContactTypeDesc(subcontractContactDetailsBean.getContactTypeDescription());
					}
					subcontractContactsType.setSubcontractCode(subcontractContactDetailsBean.getSubContractCode());
					subcontractContactsType.setSequenceNumber(String.valueOf(subcontractContactDetailsBean.getSequenceNumber()));
                                        
                                         //Case #3338 - Add new elements the person and rolodex details to Subcontract schema -Start
                                         // set ContactTypeDescription
                                        if(subcontractContactDetailsBean.getContactTypeDescription() !=null){
                                            subcontractContactsType.setContactTypeDesc(subcontractContactDetailsBean.getContactTypeDescription());
                                        }
                                        //Case #3338 - Add new elements the person and rolodex details to Subcontract schema -End
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
					
                                         //Case #3338 - Add new elements the person and rolodex details to Subcontract schema -Start
					rolodexType.setRolodexId(String.valueOf(subcontractContactDetailsBean.getRolodexId()));
                                        if(subcontractContactDetailsBean.getStateName() !=null){
                                            rolodexType.setStateDescription(subcontractContactDetailsBean.getStateName());
                                        }
                                        
                                         //Case #3338 - Add new elements the person and rolodex details to Subcontract schema -End
                                        
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
                                        //Case #3338 - Add new elements the person and rolodex details to Subcontract schema -Start
                                                                                
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
                                        
                                       //Case #3338 - Add new elements the person and rolodex details to Subcontract schema -End
					subcontractContactsType.setRolodexDetails(rolodexType);
					subContractDataType.getSubcontractContacts().add(subcontractContactsType);
				}
			}
			CoeusVector cvCloseoutData = (CoeusVector)variableValues.get(SubcontractCloseoutBean.class);
                        // Modified  for  Case #3338 - Add new elements the person and rolodex details to Subcontract schema -Start
			SubContractTxnBean  subContractTxnBean  = new SubContractTxnBean();
                        CoeusVector cvCloseOutTypes = subContractTxnBean.getSubcontractCloseoutTypes();
                        HashMap hmCloseOutTypes = new HashMap();
                        if(cvCloseOutTypes !=null && cvCloseOutTypes.size() > 0){                            
                            for (int index =0; index < cvCloseOutTypes.size(); index++){
                              ComboBoxBean comboBoxBean = (ComboBoxBean) cvCloseOutTypes.get(index);
                              hmCloseOutTypes.put(comboBoxBean.getCode(), comboBoxBean.getDescription());
                            }
                        }
                        // Modified  for  Case #3338 - Add new elements the person and rolodex details to Subcontract schema -End
			//case 2901 start
//                        SubContractDataType.CloseoutDetailsType closeOutType = new SubContractDataTypeImpl.CloseoutDetailsTypeImpl();
                        SubContractDataType.CloseoutDetailsType closeOutType;
                        //case 2901 end                        
			if (cvCloseoutData != null && cvCloseoutData.size() > 0) {
				for (int index = 0; index < cvCloseoutData.size(); index++) {
                                    //case 2901 start
                                    closeOutType = new SubContractDataTypeImpl.CloseoutDetailsTypeImpl();
                                    //case 2901 end
					SubcontractCloseoutBean subcontractCloseoutBean = (SubcontractCloseoutBean)cvCloseoutData.get(index);
					closeOutType.setCloseoutNumber(String.valueOf(subcontractCloseoutBean.getCloseoutNumber()));
					closeOutType.setCloseoutTypeCode(String.valueOf(subcontractCloseoutBean.getCloseoutTypeCode()));
					if (subcontractCloseoutBean.getComment() != null) {
						closeOutType.setComments(subcontractCloseoutBean.getComment());
					}
//					if (calendar != null && subcontractCloseoutBean.getDateFollowUp() != null) {
//						calendar.setTime(subcontractCloseoutBean.getDateFollowUp());
//						closeOutType.setDateFollowup(calendar);
//					}
//					if (calendar != null && subcontractCloseoutBean.getDateReceived() != null) {
//						calendar.setTime(subcontractCloseoutBean.getDateReceived());
//						closeOutType.setDateReceived(calendar);
//					}
//					if (calendar != null && subcontractCloseoutBean.getDateRequested() != null) {
//						calendar.setTime(subcontractCloseoutBean.getDateRequested());
//						closeOutType.setDateRequested(calendar);
//					}
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
                                        //Case #3338 - Add new elements the person and rolodex details to Subcontract schema -Start
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
                                        //Case #3338 - Add new elements the person and rolodex details to Subcontract schema -End
					subContractDataType.getCloseoutDetails().add(closeOutType);
				}
			}
			CoeusVector cvFundingSourceData = (CoeusVector)variableValues.get(SubContractFundingSourceBean.class);
			//case 2901 start
//                        SubContractDataType.FundingSourceType fundingSourceType = new SubContractDataTypeImpl.FundingSourceTypeImpl();
                        SubContractDataType.FundingSourceType fundingSourceType;
                        //case 2901 end                        
			if (cvFundingSourceData != null && cvFundingSourceData.size() > 0) {
				for (int index = 0; index < cvFundingSourceData.size(); index++) {
					//case 2901 start
                                        fundingSourceType = new SubContractDataTypeImpl.FundingSourceTypeImpl();
                                        //case 2901 end
                                        SubContractFundingSourceBean subContractFundingSourceBean = (SubContractFundingSourceBean)cvFundingSourceData.get(index);
					if (subContractFundingSourceBean.getAccountNumber() != null) {
						fundingSourceType.setAccountNumber(subContractFundingSourceBean.getAccountNumber());
					}
					BigDecimal amountDecimal = new BigDecimal(subContractFundingSourceBean.getObligatedAmount());
                                        //Added for COEUSQA-2275 Subcontract printing - Error printing amounts when amount has cents -Start
//					fundingSourceType.setAmount(amountDecimal);
                                        fundingSourceType.setAmount(amountDecimal.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                                        //Added for COEUSQA-2275 Subcontract printing - Error printing amounts when amount has cents - End
					
					if (subContractFundingSourceBean.getMitAwardNumber() != null) {
						fundingSourceType.setAwardNumber(subContractFundingSourceBean.getMitAwardNumber());
					}
                                        // Modified for  Case #3338 - Add new elements the person and rolodex details to Subcontract schema -Start
//					if (calendar != null && subContractFundingSourceBean.getFinalExpirationDate() != null) {
//						calendar.setTime(subContractFundingSourceBean.getFinalExpirationDate());
//						fundingSourceType.setFinalExpirationDate(calendar);
//					}
                                        Calendar finalExpirationDate = null;
                                        if (subContractFundingSourceBean.getFinalExpirationDate()!=null) {
                                            finalExpirationDate = Calendar.getInstance();
                                            finalExpirationDate.setTime(subContractFundingSourceBean.getFinalExpirationDate());
                                            fundingSourceType.setFinalExpirationDate(finalExpirationDate);
					}
                                        // Modified for  Case #3338 - Add new elements the person and rolodex details to Subcontract schema -End
                    if (subContractFundingSourceBean.getSponsorName() != null) {                    
						fundingSourceType.setSponsor(subContractFundingSourceBean.getSponsorName());
					}
					if (subContractFundingSourceBean.getStatusDescription() != null) {
						fundingSourceType.setStatus(subContractFundingSourceBean.getStatusDescription());
					}
                                        //Case #3338 - Add new elements the person and rolodex details to Subcontract schema -Start 
                                        //set SubcontractorCode
                                        if(subContractFundingSourceBean.getSubContractCode() !=null){
                                                fundingSourceType.setSubcontractorCode(subContractFundingSourceBean.getSubContractCode());
                                        }
                                        //set SequenceNumber
                                        fundingSourceType.setSequenceNumber(String.valueOf(subContractFundingSourceBean.getSequenceNumber()));
                                        
                                        //Set Sponsor Award Number 
                                        
                                        if ( subContractFundingSourceBean.getMitAwardNumber() !=null ){
                                            String sponsorAwardNumber = subContractTxnBean.getSponsorAwardNumber(subContractFundingSourceBean.getMitAwardNumber());
                                            if(sponsorAwardNumber !=null && !sponsorAwardNumber.equals("")){
                                                fundingSourceType.setSponsorAwardNumber(sponsorAwardNumber);
                                            }
                                        }
                                        
                                        //Case #3338 - Add new elements the person and rolodex details to Subcontract schema -End
					subContractDataType.getFundingSource().add(fundingSourceType);
				}
			}
			//Code commented and modified for Case#3338 - Adding new elements to Subcontract schema
			//CoeusVector cvSubContractAmountInfoData = (CoeusVector)variableValues.get(SubContractAmountInfoBean.class);
                        CoeusVector cvSubContractAmountInfoData = subContractTxnBean.getSubContAmtInfoForMaxSeq(subContractCode);
			//case 2901 start
//                        SubContractDataType.SubcontractAmountInfoType amountInfoType = new SubContractDataTypeImpl.SubcontractAmountInfoTypeImpl();
                        SubContractDataType.SubcontractAmountInfoType amountInfoType;
                        double obligatedAmt = 0 ;
                        //case 2901 end                        
			if (cvSubContractAmountInfoData != null && cvSubContractAmountInfoData.size() > 0) {
				for (int index = 0; index < cvSubContractAmountInfoData.size(); index++) {
					//case 2901 start
                                        amountInfoType = new SubContractDataTypeImpl.SubcontractAmountInfoTypeImpl();
                                        //case 2901 end
                                        SubContractAmountInfoBean subContractAmountInfoBean = (SubContractAmountInfoBean)cvSubContractAmountInfoData.get(index);
					//Added for COEUSQA-2275 Subcontract printing - Error printing amounts when amount has cents -Start
//					BigDecimal amountInfo = new BigDecimal(subContractAmountInfoBean.getAnticipatedAmount());
//					amountInfoType.setAnticipatedAmount(amountInfo);
//					amountInfo = new BigDecimal(subContractAmountInfoBean.getAnticipatedChange());
//                                      amountInfoType.setAnticipatedChange(amountInfo);
                                        
                                        BigDecimal amountInfo = new BigDecimal(subContractAmountInfoBean.getAnticipatedAmount());
                                        amountInfoType.setAnticipatedAmount(amountInfo.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                                        amountInfo = new BigDecimal(subContractAmountInfoBean.getAnticipatedChange());
                                        amountInfoType.setAnticipatedChange(amountInfo.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                                        
                                        
                                        //Added for COEUSQA-2275 Subcontract printing - Error printing amounts when amount has cents - End
					
					if (subContractAmountInfoBean.getComments() != null) {
						amountInfoType.setComments(subContractAmountInfoBean.getComments());
					}
                                         //Case #3338 - Add new elements the person and rolodex details to Subcontract schema -Start 
//					if (calendar != null && subContractAmountInfoBean.getEffectiveDate() != null) {
//						calendar.setTime(subContractAmountInfoBean.getEffectiveDate());
//						amountInfoType.setEffectiveDate(calendar);
//					}
                                        
                                        Calendar effectiveDate = null;
                                        if (subContractAmountInfoBean.getEffectiveDate()!=null) {
                                            effectiveDate = Calendar.getInstance();
                                            effectiveDate.setTime(subContractAmountInfoBean.getEffectiveDate());
                                            amountInfoType.setEffectiveDate(effectiveDate);
					}
					amountInfoType.setLineNumber(String.valueOf(subContractAmountInfoBean.getLineNumber()));
                                        //Added for COEUSQA-2275 Subcontract printing - Error printing amounts when amount has cents -Start
//					amountInfo = new BigDecimal(subContractAmountInfoBean.getObligatedAmount());
//					amountInfoType.setObligatedAmount(amountInfo);
//					amountInfo = new BigDecimal(subContractAmountInfoBean.getObligatedChange());
                                        
                                        amountInfo = new BigDecimal(subContractAmountInfoBean.getObligatedAmount());
                                        amountInfoType.setObligatedAmount(amountInfo.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                                        amountInfo = new BigDecimal(subContractAmountInfoBean.getObligatedChange());
                                        //Added for COEUSQA-2275 Subcontract printing - Error printing amounts when amount has cents -End
					
                                         //Case #3338 - Add new elements the person and rolodex details to Subcontract schema -Start 
                                        //set SubcontractorCode
                                        if(subContractAmountInfoBean.getSubContractCode() !=null){
                                                amountInfoType.setSubcontractCode(subContractAmountInfoBean.getSubContractCode());
                                        }
                                        //set SequenceNumber
                                        amountInfoType.setSequenceNumber(String.valueOf(subContractAmountInfoBean.getSequenceNumber()));  
                                        if(index == 0){
                                            obligatedAmt = subContractAmountInfoBean.getObligatedAmount();
                                        }
                                        //Case #3338 - Add new elements the person and rolodex details to Subcontract schema -End
                                        //Added for COEUSQA-2275 Subcontract printing - Error printing amounts when amount has cents -Start
//                                      amountInfoType.setObligatedChange(amountInfo);
                                        amountInfoType.setObligatedChange(amountInfo.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                                        //Added for COEUSQA-2275 Subcontract printing - Error printing amounts when amount has cents -End
					subContractDataType.getSubcontractAmountInfo().add(amountInfoType);
				}
			}
			//Code commented and modified for Case#3338 - Adding new elements to Subcontract schema
			//CoeusVector cvSubContractAmountReleaseData = (CoeusVector)variableValues.get(SubContractAmountReleased.class);
                        CoeusVector cvSubContractAmountReleaseData = subContractTxnBean.getSubContAmtReleasedForMaxSeq(subContractCode);
			//case 2901 start
//                        SubContractDataType.SubcontractAmountReleasedType amountReleasedType = new SubContractDataTypeImpl.SubcontractAmountReleasedTypeImpl();
                        SubContractDataType.SubcontractAmountReleasedType amountReleasedType;
                        //case 2901 end                        
			if (cvSubContractAmountReleaseData != null && cvSubContractAmountReleaseData.size() > 0) {
				for (int index = 0; index < cvSubContractAmountReleaseData.size(); index++) {
					SubContractAmountReleased subContractAmountReleased = (SubContractAmountReleased)cvSubContractAmountReleaseData.get(index);
					//case 2901 start
                                        amountReleasedType = new SubContractDataTypeImpl.SubcontractAmountReleasedTypeImpl();
                                        //case 2901 end
                                        BigDecimal amountReleased = new BigDecimal(subContractAmountReleased.getAmountReleased());
                                        //Added for COEUSQA-2275 Subcontract printing - Error printing amounts when amount has cents -Start
//					amountReleasedType.setAmountReleased(amountReleased);
                                        amountReleasedType.setAmountReleased(amountReleased.setScale(2,BigDecimal.ROUND_HALF_DOWN));
                                        //Added for COEUSQA-2275 Subcontract printing - Error printing amounts when amount has cents -End
					if (subContractAmountReleased.getComments() != null) {
						amountReleasedType.setComments(subContractAmountReleased.getComments());
					}
                                       //Case #3338 - Add new elements the person and rolodex details to Subcontract schema -Start
//					if (calendar != null && subContractAmountReleased.getEffectiveDate() != null) {
//						calendar.setTime(subContractAmountReleased.getEffectiveDate());
//						amountReleasedType.setEffectiveDate(calendar);
//					}
                                        Calendar effectiveDate = null;
                                        if (subContractAmountReleased.getEffectiveDate()!=null) {
                                            effectiveDate = Calendar.getInstance();
                                            effectiveDate.setTime(subContractAmountReleased.getEffectiveDate());
                                            amountReleasedType.setEffectiveDate(effectiveDate);
					}
                                         //Case #3338 - Add new elements the person and rolodex details to Subcontract schema -End
					amountReleasedType.setLineNumber(String.valueOf(subContractAmountReleased.getLineNumber()));
                                        //Case #3338 - Add new elements the person and rolodex details to Subcontract schema -Start 
                                        //set SubcontractorCode
                                        if(subContractAmountReleased.getSubContractCode() !=null){
                                                amountReleasedType.setSubcontractCode(subContractAmountReleased.getSubContractCode());
                                        }
                                        //set SequenceNumber
                                        amountReleasedType.setSequenceNumber(String.valueOf(subContractAmountReleased.getSequenceNumber()));    
                                        //Set StartDate
                                        Calendar startDate = null;
                                        if (subContractAmountReleased.getStartDate()!=null) {
                                            startDate = Calendar.getInstance();
                                            startDate.setTime(subContractAmountReleased.getStartDate());
                                            amountReleasedType.setStartDate(startDate);
                                        }
                                        //Set EndDate
                                        Calendar endDate = null;
                                        if (subContractAmountReleased.getEndDate()!=null) {
                                            endDate = Calendar.getInstance();
                                            endDate.setTime(subContractAmountReleased.getEndDate());
                                            amountReleasedType.setEndDate(endDate);
                                        }
                                        //setInvoice
                                         if(subContractAmountReleased.getInvoiceNumber() !=null){
                                                amountReleasedType.setInvoiceNumber(subContractAmountReleased.getInvoiceNumber());
                                        }
                                        //set Percent Released  
//                                        if(index == 0){
                                            double totalAmtReleased = cvSubContractAmountReleaseData.sum("amountReleased");
                                            double percentReleased = totalAmtReleased / obligatedAmt * 100;
                                            amountReleasedType.setPercentReleased(new BigDecimal(percentReleased));
//                                        }
                                        //Case #3338 - Add new elements the person and rolodex details to Subcontract schema -End
                                        
					subContractDataType.getSubcontractAmountReleased().add(amountReleasedType);
				}
			}
                         //Case #3338 - Add new elements the person and rolodex details to Subcontract schema -Start
                        SubContractDataType.AuditorDataType auditorDataType;
                        
                        if (cvAuditorData != null &&   cvAuditorData.size() > 0){
                            for(int index = 0; index < cvAuditorData.size(); index++){
                                RolodexDetailsBean rolodexDetailsBean = (RolodexDetailsBean) cvAuditorData.get(index);
                                
                                auditorDataType = new SubContractDataTypeImpl.AuditorDataTypeImpl();
                                
                                if(rolodexDetailsBean.getFirstName() !=null){
                                    auditorDataType.setFirstName(rolodexDetailsBean.getFirstName());
                                }
                                if(rolodexDetailsBean.getMiddleName() !=null){
                                    auditorDataType.setMiddleName(rolodexDetailsBean.getMiddleName());
                                }
                                if(rolodexDetailsBean.getLastName() !=null){
                                    auditorDataType.setLastName(rolodexDetailsBean.getLastName());
                                }
                                if(rolodexDetailsBean.getAddress1() !=null){
                                    auditorDataType.setAddress1(rolodexDetailsBean.getAddress1());
                                }
                                if(rolodexDetailsBean.getAddress2() !=null){
                                    auditorDataType.setAddress2(rolodexDetailsBean.getAddress2());
                                }
                                if(rolodexDetailsBean.getAddress3() !=null){
                                    auditorDataType.setAddress3(rolodexDetailsBean.getAddress3());
                                }
                                if(rolodexDetailsBean.getCity() !=null){
                                    auditorDataType.setCity(rolodexDetailsBean.getCity());
                                }
                                if(rolodexDetailsBean.getState() !=null){
                                    auditorDataType.setStateDescription(rolodexDetailsBean.getState());
                                }
                                if(rolodexDetailsBean.getPostalCode() !=null){
                                    auditorDataType.setPincode(rolodexDetailsBean.getPostalCode());
                                }
                                if(rolodexDetailsBean.getPrefix() !=null){
                                    auditorDataType.setPrefix(rolodexDetailsBean.getPrefix());
                                }
                                
                                subContractDataType.getAuditorData().add(auditorDataType);
                            }
                        }
                        
                        // Get Person Details for OSP_ADMINSTRATOR & ADMINISTRATIVE_OFFICER from Unit table
                        SubContractDataType.AdministrativeOfficerType administrativeOfficerType;
                        SubContractDataType.OspAdministratorType ospAdministratorType;
                        PersonInfoBean personInfoBean = null;
                        
                        if (requisitionerUnit !=null ){
                            UnitDataTxnBean unitDataTxnBean = new UnitDataTxnBean();
                            UnitDetailFormBean unitDetailFormBean = unitDataTxnBean.getUnitDetails(requisitionerUnit);
                            String ospAdminId = unitDetailFormBean.getOspAdminId();
                            String AdministrativeOffId = unitDetailFormBean.getAdminOfficerId();
                            
                            //Get the OSPAdministrativeOffice data
                            personInfoBean = subContractTxnBean.getPersonInfo(ospAdminId);
                            
                            ospAdministratorType = new SubContractDataTypeImpl.OspAdministratorTypeImpl();
                            
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
                            personInfoBean = null;
                            personInfoBean = subContractTxnBean.getPersonInfo(AdministrativeOffId);
                            if(personInfoBean !=null){
                                if( personInfoBean.getFirstName() !=null){
                                    administrativeOfficerType.setFirstName( personInfoBean.getFirstName());
                                }
                                if( personInfoBean.getMiddleName() !=null){
                                    administrativeOfficerType.setMiddleName( personInfoBean.getMiddleName());
                                }
                                if( personInfoBean.getLastName() !=null){
                                    administrativeOfficerType.setLastName( personInfoBean.getLastName());
                                }
                                if( personInfoBean.getOffPhone() !=null){
                                    administrativeOfficerType.setOfficePhone( personInfoBean.getOffPhone());
                                }
                                if( personInfoBean.getSecOffPhone() !=null){
                                    administrativeOfficerType.setSecondryOfficePhone( personInfoBean.getSecOffPhone());
                                }
                                if( personInfoBean.getEmail() !=null){
                                    administrativeOfficerType.setEmailAddress( personInfoBean.getEmail());
                                }
                                if( personInfoBean.getDirTitle() !=null){
                                    administrativeOfficerType.setDirectoryTitle( personInfoBean.getDirTitle());
                                }
                                if( personInfoBean.getFax() !=null){
                                    administrativeOfficerType.setFaxNumber( personInfoBean.getFax());
                                }
                                if( personInfoBean.getCity() !=null){
                                    administrativeOfficerType.setCity( personInfoBean.getCity());
                                }
                                if( personInfoBean.getState() !=null){
                                    administrativeOfficerType.setState( personInfoBean.getState());
                                }
                                if( personInfoBean.getPostalCode() !=null){
                                    administrativeOfficerType.setPostalCode( personInfoBean.getPostalCode());
                                }
                                subContractDataType.getAdministrativeOfficer().add(administrativeOfficerType);
                            }
                        }
                        
                        if (requisitionerId !=null){
                            // Get Principal Investigator Data for person
                            SubContractDataType.PrincipalInvestigatorType principalInvestigatorType = new SubContractDataTypeImpl.PrincipalInvestigatorTypeImpl();
                            personInfoBean = null;
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
                         SubContractDataType.OtherDataType otherDataType = new SubContractDataTypeImpl.OtherDataTypeImpl();
                        if( subContractId !=null){                           
                            OrganizationMaintenanceDataTxnBean organizationMaintenanceDataTxnBean = new OrganizationMaintenanceDataTxnBean();
                            RolodexMaintenanceDataTxnBean rolodexMaintenanceDataTxnBean = new RolodexMaintenanceDataTxnBean();
                            OrganizationMaintenanceFormBean organizationMaintenanceFormBean =  organizationMaintenanceDataTxnBean.getOrganizationMaintenanceDetails(subContractId);
                            //Setting Acronym
                            String acronym = organizationMaintenanceFormBean.getCableAddress();
                            if(acronym != null){
                                otherDataType.setAcronumCableAddress(acronym);
                            }
                        }
                            //Set BPG Values
                            //if( variableValues.get("bpgLineItemNumber") !=null){
                            //    String bpgLineItemNumber = (String) variableValues.get("bpgLineItemNumber");
                                HashMap hmBgpData =  subContractTxnBean.geSubcontractBpgValues(subContractCode); 
                                
                                
                                if (hmBgpData.get("BPG239_AMT") != null){
                                    String bpg239Amt = (String) hmBgpData.get("BPG239_AMT");
                                    otherDataType.setBPG239AMT(new BigDecimal(bpg239Amt));
                                }
                                if (hmBgpData.get("BPG236_AMT") != null){
                                    String bpg236Amt = (String) hmBgpData.get("BPG236_AMT");
                                    otherDataType.setBPG236AMT(new BigDecimal(bpg236Amt));
                                }
                            //}
                            //Funding Source
                            if ( cvFundingSourceData != null &&  cvFundingSourceData.size() > 0 ){
                                SubContractFundingSourceBean fundingSourceBean = (SubContractFundingSourceBean)cvFundingSourceData.get(0);
                                AwardTxnBean awardTxnBean = new AwardTxnBean();
                                if (fundingSourceBean.getMitAwardNumber() != null) {
                                    String mitAwardNumber = fundingSourceBean.getMitAwardNumber();
                                    //  String leadUnit = awardTxnBean.getLeadUnitForAward(mitAwardNumber);
                                    if(awardTxnBean.getLeadUnitForAward(mitAwardNumber) !=null ){
                                        otherDataType.setFundingSourceUnit(awardTxnBean.getLeadUnitForAward(mitAwardNumber));
                                    }
                                    AwardHeaderBean awardHeaderBean = awardTxnBean.getAwardHeader(mitAwardNumber);
                                    if ( awardHeaderBean !=null && awardHeaderBean.getCfdaNumber() !=null){
                                        otherDataType.setCFDANumber(awardHeaderBean.getCfdaNumber());
                                    }
                                }
                            }
                            subContractDataType.getOtherData().add(otherDataType);
                       
                        //Added for Case #3338 - Add new elements the person and rolodex details to Subcontract schema  - End
                        
                        
			CoeusVector cvUserValues = (CoeusVector)variableValues.get("USER_VALUES");
			SubContractDataType.UserDetailsType userDetailType = new SubContractDataTypeImpl.UserDetailsTypeImpl();
			if (cvUserValues != null && cvUserValues.size() > 0) {
                           // Modified  for  Case #3338 - Add new elements the person and rolodex details to Subcontract schema -Start
                              String userTitle = null;
                              String userName = null;
                              for ( int index =0; index < cvUserValues.size();index++){ 
                                  HashMap hmUserValues = (HashMap) cvUserValues.get(index);
                                  if (hmUserValues !=null && hmUserValues.size() > 0 && hmUserValues.get("USER_TITLE") !=null){
                                      userTitle = hmUserValues.get("USER_TITLE").toString();
                                  }
                                  if (hmUserValues !=null && hmUserValues.size() > 0 && hmUserValues.get("USER_NAME") !=null){
                                      userName = hmUserValues.get("USER_NAME").toString();
                                  }
                              }
                               // Modified  for  Case #3338 - Add new elements the person and rolodex details to Subcontract schema -End   
//				String userTitle = (String)cvUserValues.get(0);
//				String userName = (String)cvUserValues.get(1);
				if (userName != null && !userName.equals("")) {
					userDetailType.setUserName(userName);
				}
				if (userTitle != null && !userTitle.equals("")) {
					userDetailType.setUserTitle(userTitle);
				}
				if (calendar != null) {
					calendar.setTime(new Date());
					userDetailType.setToday(calendar);
				}
				subContractDataType.setUserDetails(userDetailType);
			}
                        
                        //Custom Datas
						//Added for Case#3338 - Adding new elements to Subcontract schema - starts
                        SubContractDataType.CustomDataType customDataType = null;
                        CoeusVector cvCustomData = subContractTxnBean.getSubContractCustomData(subContractCode);
                        if(cvCustomData != null && cvCustomData.size() > 0){
                            for(int index = 0; index < cvCustomData.size(); index++){
                                SubContractCustomDataBean subContractCustomDataBean =
                                    (SubContractCustomDataBean) cvCustomData.get(index);
                                customDataType = new SubContractDataTypeImpl.CustomDataTypeImpl();
                                customDataType.setColumnValue(subContractCustomDataBean.getColumnValue());
                                customDataType.setColumnName(subContractCustomDataBean.getColumnLabel());
                                subContractDataType.getCustomData().add(customDataType);
                            }
                        }
						//Added for Case#3338 - Adding new elements to Subcontract schema - ends
		}
		JAXBContext jaxbContext = JAXBContext.newInstance("edu.mit.coeus.utils.xml.bean.subcontract");
		Marshaller marshaller = jaxbContext.createMarshaller();
		ObjectFactory objFactory = new ObjectFactory();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
		ByteArrayOutputStream streamData = new ByteArrayOutputStream();
		marshaller.marshal(subContractDataType, streamData);
		return streamData;
	}
}
