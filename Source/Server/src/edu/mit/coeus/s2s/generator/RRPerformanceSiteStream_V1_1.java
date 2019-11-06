/*
 * @(#)RRPerformanceSiteStream_V1_1.java 
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.generator;
 
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceDataTxnBean;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceFormBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexMaintenanceDataTxnBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.s2s.generator.stream.bean.RRPerformanceSiteTxnBeanV1_1;
import edu.mit.coeus.s2s.generator.stream.AttachedFileDataTypeStream;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
 

import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import gov.grants.apply.forms.rr_performancesite_v1_1.*;
import gov.grants.apply.system.globallibrary_v2.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import java.util.ArrayList;
import javax.xml.bind.JAXBException;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.s2s.bean.S2STxnBean;
import edu.mit.coeus.s2s.util.S2SHashValue;
import java.util.LinkedHashMap;

/**
 * @author  ele
 */

 public class RRPerformanceSiteStream_V1_1 extends S2SBaseStream{
    private gov.grants.apply.forms.rr_performancesite_v1_1.ObjectFactory objFactory;
    private gov.grants.apply.system.globallibrary_v2.ObjectFactory globalLibObjFactory;
    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
    private CoeusXMLGenrator xmlGenerator;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;

    gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
    
    private UtilFactory utilFactory;
     
    //txn beans
    private ProposalDevelopmentTxnBean propDevTxnBean;
    private OrganizationMaintenanceDataTxnBean orgMaintDataTxnBean;
    private RolodexMaintenanceDataTxnBean rolodexMaintDataTxnBean;
    private RRPerformanceSiteTxnBeanV1_1 perfSiteTxnBean;
    
    /** propDevFormBean holds proposal master info   */
    private ProposalDevelopmentFormBean propDevFormBean;
    /** orgMaintFormBean holds performing organization master info   */
    private OrganizationMaintenanceFormBean perfOrgFormBean;
    /** rolodexDetailsBean holds rolodex information for performing orgContact */
    private RolodexDetailsBean perfOrgContactRolodexDetailsBean;
    
    private Hashtable propData;
    private String propNumber;
    
    //start case 2406
    private String organizationID;
    private String perfOrganizationID;
    //end case 2406
   
  
       public RRPerformanceSiteStream_V1_1(){
        this.propNumber = propNumber;
        objFactory = new gov.grants.apply.forms.rr_performancesite_v1_1.ObjectFactory();
        globalLibObjFactory = new gov.grants.apply.system.globallibrary_v2.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();
        propDevTxnBean = new ProposalDevelopmentTxnBean();
        orgMaintDataTxnBean = new OrganizationMaintenanceDataTxnBean();
        rolodexMaintDataTxnBean = new RolodexMaintenanceDataTxnBean();
        perfSiteTxnBean = new RRPerformanceSiteTxnBeanV1_1();
    } 
    
    private RRPerformanceSiteType getRRPerformanceSite() 
                            throws CoeusXMLException,CoeusException,DBException{
        RRPerformanceSiteType performanceSite = null;
        String stateCode = null;
        String countryCode = null;
        String stateDesc= null;
        String countryDesc = null;
        HashMap row = null;
        try{
            performanceSite = objFactory.createRRPerformanceSite();

             //get proposal master info
            propDevFormBean = getPropDevData();
            //get performing organization info
            //start case 2406
//            perfOrgFormBean = getOrgData(propDevFormBean.getPerformingOrganizationId());
             perfOrgFormBean = getPerfOrgData();
             //end case 2406
            //get rolodex information for performing org contact
            perfOrgContactRolodexDetailsBean = 
                            getRolodexData(perfOrgFormBean.getContactAddressId());
            //case 4352 and 4249 changes below
            if (perfOrgContactRolodexDetailsBean.getCountry() != null) {
                countryCode = perfOrgContactRolodexDetailsBean.getCountry();
                if (perfSiteTxnBean.getCountryName(countryCode) != null) {
                  row = perfSiteTxnBean.getCountryName(countryCode);
                  countryDesc = row.get("COUNTRY_NAME").toString();    
                  perfOrgContactRolodexDetailsBean.setCountryName(countryDesc);
                }
            }
            if (perfOrgContactRolodexDetailsBean.getState() != null) {
                stateCode = perfOrgContactRolodexDetailsBean.getState();
                if (perfSiteTxnBean.getStateName(stateCode) != null) {
                  row = perfSiteTxnBean.getStateName(stateCode);
                  stateDesc = row.get("STATE_NAME").toString();
                }
            }
                //bug fix
           //if country is not USA,  state data populates province
            if (countryCode != null){
              if (countryCode.equals("USA")) {
                  perfOrgContactRolodexDetailsBean.setState(stateCode);
              } else {
                 if (stateDesc != null)
                    perfOrgContactRolodexDetailsBean.setProvince(stateCode);
              }
            }
            
                         
            performanceSite.setFormVersion("1.1");
            performanceSite.setPrimarySite(
                getSiteLocationDataType(perfOrgContactRolodexDetailsBean)); 
            //get other site data
            ArrayList otherSiteList = perfSiteTxnBean.getOtherSiteList(propNumber);
            //get reference to otherSite member of RRPerfomanceSiteType instance
            java.util.List otherSites = performanceSite.getOtherSite();
            //populate other site data.
            //case 2406 start
            if (otherSiteList != null)
            for(int siteCnt=0; siteCnt<otherSiteList.size(); siteCnt++){
                RolodexDetailsBean rolodexDetails = 
                                (RolodexDetailsBean)otherSiteList.get(siteCnt);
                //commented out for coeusdev-376
      //          if(!rolodexDetails.getOrganization().equals
     //                               (perfOrgFormBean.getOrganizationName())){
                    countryCode = rolodexDetails.getCountry() == null ? null : rolodexDetails.getCountry().toString();
                    stateCode =  rolodexDetails.getState() == null ? null : rolodexDetails.getState().toString();
                  
                    if (countryCode != null) {
                        if (perfSiteTxnBean.getCountryName(countryCode) != null) {
                          row = perfSiteTxnBean.getCountryName(countryCode);
                          countryDesc = row.get("COUNTRY_NAME").toString(); 
                          rolodexDetails.setCountryName(countryDesc);
                        }
                    }       
                    if (stateCode != null) {
                        if (perfSiteTxnBean.getStateName(stateCode) != null) {
                          row = perfSiteTxnBean.getStateName(stateCode);
                          stateDesc = row.get("STATE_NAME").toString();
                        }
                    }
                     //CASE 4552 - null pointer exception
                    //if country is not USA,  state data populates province        
                   if (countryCode != null) {
                       if (countryCode.equals("USA")) {
                            rolodexDetails.setState(stateCode);
                       }else {
                         if (stateCode != null)
                            rolodexDetails.setProvince(stateCode); 
                       }
                     }
                                                                                               
                    SiteLocationDataType otherSite = 
                                    getSiteLocationDataType(rolodexDetails);
                    otherSites.add(otherSite);
             //   }
            }
            
           
            attachedFileType = getAttachedSite();
            if (attachedFileType.getFileName() != null)
      
                performanceSite.setAttachedFile(attachedFileType);

         }catch (JAXBException jaxbEx){
            UtilFactory.log(jaxbEx.getMessage(),jaxbEx,"RRPerformanceSiteStream_V1_1",
                                                    "getRRPerformanceSite()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }
        return performanceSite;
    }    
    
    private ProposalDevelopmentFormBean getPropDevData() throws DBException,CoeusException{
        if(propNumber==null) 
            throw new CoeusXMLException("Proposal Number is Null");
        return propDevTxnBean.getProposalDevelopmentDetails(propNumber);
    }
    
     //start case 2406
//    private OrganizationMaintenanceFormBean getOrgData(String orgID)
//       throws CoeusXMLException,CoeusException,DBException{
//        if(orgID==null) 
//            throw new CoeusXMLException("Organization id is Null");   
//        return orgMaintDataTxnBean.getOrganizationMaintenanceDetails(orgID);
//    }
    
   
    private OrganizationMaintenanceFormBean getPerfOrgData()
            throws CoeusXMLException, CoeusException,DBException{
        HashMap hmOrg = perfSiteTxnBean.getOrganizationID(propNumber,"P");
        if (hmOrg!= null && hmOrg.get("ORGANIZATION_ID") != null){
               organizationID = hmOrg.get("ORGANIZATION_ID").toString();           
        }
        return orgMaintDataTxnBean.getOrganizationMaintenanceDetails(organizationID);
    }
    //end case 2406
    
     private RolodexDetailsBean getRolodexData(int rolodexId)
       throws CoeusXMLException,CoeusException,DBException{
        if(rolodexId == 0 )
            throw new CoeusXMLException("Rolodex id is zero");   
        return rolodexMaintDataTxnBean.getRolodexMaintenanceDetails(Integer.toString(rolodexId ));  
    }
     
    private gov.grants.apply.forms.rr_performancesite_v1_1.SiteLocationDataType getSiteLocationDataType
                (RolodexDetailsBean rolodexDetailsBean) 
                throws JAXBException, DBException, CoeusException{
       SiteLocationDataType siteLocationDataType = objFactory.createSiteLocationDataType();
       siteLocationDataType.setOrganizationName(rolodexDetailsBean.getOrganization());
       siteLocationDataType.setAddress(getAddressType(rolodexDetailsBean));
      
       return siteLocationDataType;
    }
     
     
    private gov.grants.apply.system.globallibrary_v2.AddressDataType getAddressType
                (RolodexDetailsBean rolodexDetailsBean) 
                throws JAXBException, DBException,CoeusException{
        HashMap hmRow = new HashMap();
        String countryName = null;
        String stateName = null;
        //Street 1 and City and country are mandatory.
        AddressDataType addressType = 
                        globalLibObjFactory.createAddressDataType();
        if(rolodexDetailsBean.getAddress1() != null)
            addressType.setStreet1(rolodexDetailsBean.getAddress1());
        if(rolodexDetailsBean.getAddress2() != null)
             addressType.setStreet2(rolodexDetailsBean.getAddress2());
        if(rolodexDetailsBean.getCity() != null)
           addressType.setCity(rolodexDetailsBean.getCity());
        //case 2752
        String state = rolodexDetailsBean.getState() == null ? null : rolodexDetailsBean.getState().toString();
        String country = rolodexDetailsBean.getCountry() == null ? null : rolodexDetailsBean.getCountry().toString();
            
         if (country != null) {
                if (perfSiteTxnBean.getCountryName(country) != null) {
                          hmRow = perfSiteTxnBean.getCountryName(country);
                          countryName = hmRow.get("COUNTRY_NAME").toString(); 
                          addressType.setCountry(countryName);
                 } 
         }
          if (state != null) {
                if (perfSiteTxnBean.getStateName(state) != null) {
                          hmRow = perfSiteTxnBean.getStateName(state);
                          stateName = hmRow.get("STATE_NAME").toString();
                 }
          }
                     
          //if country is not USA,  state data populates province        
          //case 4552 
          if (country != null){
            if (country.equals("USA")) {
                    addressType.setState(stateName);
            } else {
                if (stateName != null)
                     addressType.setProvince(stateName);                              
            }
          }
        
        if(rolodexDetailsBean.getPostalCode() != null)
            addressType.setZipPostalCode(rolodexDetailsBean.getPostalCode());
        if(rolodexDetailsBean.getCounty() != null)
            addressType.setCounty(rolodexDetailsBean.getCounty());
//        if(rolodexDetailsBean.getCountry() != null) 
//            addressType.setCountry(rolodexDetailsBean.getCountryName());   
        return addressType;
                                    
    }
    
     /**************************************
     *  getAttachedSite
     **************************************/
     private gov.grants.apply.system.attachments_v1.AttachedFileDataType getAttachedSite()
        throws CoeusXMLException,CoeusException,DBException,JAXBException{
    
        gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType =
           attachmentsObjFactory.createAttachedFileDataType();
        
        try{
          String description;
          int narrativeType;
          int moduleNum;
          Attachment attachment = null;
          
          ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
          ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean ;
                      
          Vector vctNarrative = proposalNarrativeTxnBean.getPropNarrativePDFForProposal(propNumber);
              
          S2STxnBean s2sTxnBean = new S2STxnBean();
          LinkedHashMap hmArg = new LinkedHashMap();
                     
          HashMap hmNarrative = new HashMap();
                  
           int size=vctNarrative==null?0:vctNarrative.size();
           for (int row=0; row < size;row++) {
               proposalNarrativePDFSourceBean = (ProposalNarrativePDFSourceBean) vctNarrative.elementAt(row);
                           
               moduleNum = proposalNarrativePDFSourceBean.getModuleNumber();   
  
               String fileNameForOtherType = proposalNarrativePDFSourceBean.getFileName();
               
               hmNarrative = new HashMap();
               hmNarrative = s2sTxnBean.getNarrativeInfo(propNumber,moduleNum);
               narrativeType = Integer.parseInt(hmNarrative.get("NARRATIVE_TYPE_CODE").toString());
               description = hmNarrative.get("DESCRIPTION").toString();
      
               hmArg.put(ContentIdConstants.MODULE_NUMBER, Integer.toString(moduleNum));            
               hmArg.put(ContentIdConstants.DESCRIPTION, description);
               
               attachment = getAttachment(hmArg);
                 
               if ( narrativeType == 40 ) {
                   //performance sites
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                    Attachment sitesAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                    if (sitesAttachment.getContent() != null){                           
                        attachedFileType = getAttachedFileType(sitesAttachment);
                     }
                  } 
               }
              }  //end for
             
            return attachedFileType;
     
     
         }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"RRPerformanceSiteStream_V1_1","getAttachedSite()");
            throw new CoeusXMLException(jaxbEx.getMessage());
         }
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
        UtilFactory.log(ex.getMessage(),ex, "RRPerformanceSiteStream_V1_1", "getAttachedFile");
        throw new JAXBException(ex);
    }

    return attachedFileType;
           
}
    
    
     
        
   
    public Object getStream(HashMap hashMap) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)hashMap.get("PROPOSAL_NUMBER");
        return getRRPerformanceSite();
    } 
    
  
      
    
}
