/*
 * @(#)PerformanceSiteStream_V1_4.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.generator;
 

import edu.mit.coeus.organization.bean.OrganizationMaintenanceDataTxnBean;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceFormBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexMaintenanceDataTxnBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.s2s.bean.PerformanceSiteV1_2TxnBean;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.bean.S2STxnBean;
import edu.mit.coeus.s2s.generator.stream.AttachedFileDataTypeStream;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.s2s.Attachment;
 
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import gov.grants.apply.forms.performancesite_1_4_v1.*;
import gov.grants.apply.system.globallibrary_v2.*;
import java.util.HashMap;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import edu.mit.coeus.s2s.util.S2SHashValue;
import java.util.Vector;
import javax.xml.bind.JAXBException;




 public class PerformanceSiteStream_V1_4 extends S2SBaseStream{
    private gov.grants.apply.forms.performancesite_1_4_v1.ObjectFactory objFactory;
    private gov.grants.apply.system.globallibrary_v2.ObjectFactory globalLibObjFactory;
    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
    private CoeusXMLGenrator xmlGenerator;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;
    
    private UtilFactory utilFactory;
     
    //txn beans
    private OrganizationMaintenanceDataTxnBean orgMaintDataTxnBean;
    private RolodexMaintenanceDataTxnBean rolodexMaintDataTxnBean;
    private PerformanceSiteV1_2TxnBean perfSiteTxnBean;
    
    private OrganizationMaintenanceFormBean perfOrgFormBean;
    private RolodexDetailsBean rolodexDetailsBean;
    
    private String propNumber;   
   
    private String organizationID;

    gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;

  
       public PerformanceSiteStream_V1_4(){
        objFactory = new gov.grants.apply.forms.performancesite_1_4_v1.ObjectFactory();
        globalLibObjFactory = new gov.grants.apply.system.globallibrary_v2.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        orgMaintDataTxnBean = new OrganizationMaintenanceDataTxnBean();
        rolodexMaintDataTxnBean = new RolodexMaintenanceDataTxnBean();
        perfSiteTxnBean = new PerformanceSiteV1_2TxnBean();
        
    } 
    
    private PerformanceSite14Type getPerformanceSite()
                            throws CoeusXMLException,CoeusException,DBException{
        PerformanceSite14Type performanceSite = null;
        
        try{
            performanceSite = objFactory.createPerformanceSite14();
            //version
            performanceSite.setFormVersion("1.4");
             
            //primary site - this is performing organization info
             perfOrgFormBean = getOrgData();
                          
            performanceSite.setPrimarySite(getPrimarySiteLocationDataType(perfOrgFormBean));  
                   
            //get other site data
            ArrayList otherSitesList = perfSiteTxnBean.getOtherSiteList(propNumber);
            //othersitesList is an array of hashMaps. the hashmap keys are
            //PROPOSAL_NUMBER ,LOCATION_NAME,LOCATION_TYPE_CODE,ORGANIZATION_ID,ROLODEX_ID,SITE_NUMBER
            
            java.util.List otherSites = performanceSite.getOtherSite();
            //populate other site data.
            HashMap hmSite;
            SiteLocationDataType otherSite ;
            if (otherSitesList != null) {
              for(int siteIndex=0; siteIndex<otherSitesList.size(); siteIndex++){
                  hmSite = (HashMap) otherSitesList.get(siteIndex);
                  
                  otherSite = getOtherSiteLocationDataType(hmSite);
                
                  otherSites.add(otherSite);
                }
            }

        //attachment
          attachedFileType = getAttachedSite();
          if (attachedFileType.getFileName() != null)

                performanceSite.setAttachedFile(attachedFileType);


         }catch (JAXBException jaxbEx){
            UtilFactory.log(jaxbEx.getMessage(),jaxbEx," PerformanceSiteStream_V1_4",
                                                    "get PerformanceSite()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }
        return performanceSite;
    }    
    
   
   //getOrgData - returns organizationBean with performing org data
    private OrganizationMaintenanceFormBean getOrgData()
            throws CoeusXMLException, CoeusException,DBException{
        HashMap hmOrg = perfSiteTxnBean.getPerfOrgId(propNumber);
        if (hmOrg!= null && hmOrg.get("ORGANIZATION_ID") != null){
               organizationID = hmOrg.get("ORGANIZATION_ID").toString();           
        }
        return orgMaintDataTxnBean.getOrganizationMaintenanceDetails(organizationID);
    }
    
    
     private RolodexDetailsBean getRolodexData(int rolodexId)
       throws CoeusXMLException,CoeusException,DBException{
        if(rolodexId == 0 )
            throw new CoeusXMLException("Rolodex id is zero");   
        return rolodexMaintDataTxnBean.getRolodexMaintenanceDetails(Integer.toString(rolodexId ));  
    }
     
      private gov.grants.apply.forms.performancesite_1_4_v1.SiteLocationDataType getOtherSiteLocationDataType
             (HashMap hmSite) 
                throws JAXBException, DBException, CoeusException{
        String stateCode = null;
        String countryCode = null;
        String stateDesc = null;
        String countryDesc = null;
        HashMap row = null;
        String orgName=null;
        String orgId=null;
        String dunsNumber = null;
        String siteType=null;
        int rolodexId ;
        int siteNumber;
     
       SiteLocationDataType siteLocationDataType = objFactory.createSiteLocationDataType();
       siteType = hmSite.get("LOCATION_TYPE_CODE").toString();
       String strSite = hmSite.get("SITE_NUMBER").toString();
       siteNumber = Integer.parseInt(strSite);
      
 
       orgName = hmSite.get("LOCATION_NAME").toString();
      
       String strRolodexId = hmSite.get("ROLODEX_ID").toString();
       rolodexId = Integer.parseInt(strRolodexId);
      
       //get rolodex information for org contact

       if (rolodexId == 0)
           rolodexDetailsBean = null;
       else if  (rolodexId != 0) {
         rolodexDetailsBean = getRolodexData(rolodexId);
        
         if (rolodexDetailsBean.getCountry() != null) {
                countryCode = rolodexDetailsBean.getCountry();
                if (perfSiteTxnBean.getCountryName(countryCode) != null ) {
                  row = perfSiteTxnBean.getCountryName(countryCode);
                  countryDesc = row.get("COUNTRY_NAME").toString();    
                  rolodexDetailsBean.setCountryName(countryDesc);
                }
         }
      
         if (rolodexDetailsBean.getState() != null) {
                stateCode = rolodexDetailsBean.getState();
                if (perfSiteTxnBean.getStateName(countryCode,stateCode) != null) {
                     row = perfSiteTxnBean.getStateName(countryCode,stateCode);
                     stateDesc = row.get("STATE_NAME").toString();
                }
          }
                
          //if country is not USA,  state data populates province 
          
            if (countryCode != null){
              if (countryCode.equals("USA")) {
                  rolodexDetailsBean.setState(stateCode);
              } else {
                   if (stateCode != null)
                     rolodexDetailsBean.setProvince(stateCode);
              }
            }
       }
       
       //site typ 4 is performance site; others are organizations
       if (!siteType.equals("4")) {
            orgId = hmSite.get("ORGANIZATION_ID").toString();
            if (orgId != null) {
                 HashMap hm = perfSiteTxnBean.getDunsNumber(orgId);
                 if (hm != null && hm.get("DUNS_NUMBER") != null)
                    siteLocationDataType.setDUNSNumber(hm.get("DUNS_NUMBER").toString());
                 if (getIndividual(orgId) != null)
                     siteLocationDataType.setIndividual(getIndividual(orgId));
            }
        } 
           
         siteLocationDataType.setOrganizationName(orgName);

         if (rolodexDetailsBean != null) {
           siteLocationDataType.setOrganizationName(rolodexDetailsBean.getOrganization()); //JM 5-25-2011 want name from rolodex instead of manually keyed location per 4.4.2
           siteLocationDataType.setAddress(getAddressType(rolodexDetailsBean));
         
           HashMap hmCongDist = perfSiteTxnBean.getCongDistrict(propNumber, siteNumber);
           if (hmCongDist.get("CONGDIST") != null)
             siteLocationDataType.setCongressionalDistrictProgramProject((String) hmCongDist.get("CONGDIST").toString());
           if (dunsNumber != null)
             siteLocationDataType.setDUNSNumber(dunsNumber);
         }
       return siteLocationDataType;
    }
     
      
    
      private gov.grants.apply.forms.performancesite_1_4_v1.SiteLocationDataType getPrimarySiteLocationDataType
             (OrganizationMaintenanceFormBean perfOrgFormBean) 
                throws JAXBException, DBException, CoeusException{
        String stateCode = null;
        String countryCode = null;
        String stateDesc = null;
        String countryDesc = null;
        HashMap row = null;
         
        String dunsNumber = null;

        SiteLocationDataType siteLocationDataType = objFactory.createSiteLocationDataType();
       
        siteLocationDataType.setOrganizationName(perfOrgFormBean.getOrganizationName());
        String orgId = perfOrgFormBean.getOrganizationId();
        HashMap hm = perfSiteTxnBean.getDunsNumber(orgId);
        if (hm != null && hm.get("DUNS_NUMBER") != null)
          siteLocationDataType.setDUNSNumber(hm.get("DUNS_NUMBER").toString());
        if (getIndividual(orgId) != null)
                     siteLocationDataType.setIndividual(getIndividual(orgId));
        
        //get rolodex information for org contact

        RolodexDetailsBean rolodexDetailsBean = getRolodexData(perfOrgFormBean.getContactAddressId());
        
        if (rolodexDetailsBean.getCountry() != null) {
                countryCode = rolodexDetailsBean.getCountry();
                if (perfSiteTxnBean.getCountryName(countryCode) != null ) {
                  row = perfSiteTxnBean.getCountryName(countryCode);
                  countryDesc = row.get("COUNTRY_NAME").toString();    
                  rolodexDetailsBean.setCountryName(countryDesc);
                }
        }
      
        if (rolodexDetailsBean.getState() != null) {
                stateCode = rolodexDetailsBean.getState();
                if (perfSiteTxnBean.getStateName(countryCode,stateCode) != null) {
                     row = perfSiteTxnBean.getStateName(countryCode,stateCode);
                     stateDesc = row.get("STATE_NAME").toString();
                }
        }
                
        //if country is not USA,  state data populates province   

          if (countryCode != null) {
              if (countryCode.equals("USA")) {
                    rolodexDetailsBean.setState(stateCode);
              } else {
                   if (stateCode!= null)
                     rolodexDetailsBean.setProvince(stateCode);
              }
          }
        
         
                   
         siteLocationDataType.setAddress(getAddressType(rolodexDetailsBean));
         
         //get site number for perf org
         HashMap hmSite = perfSiteTxnBean.getPerfOrgSiteNumber(propNumber);
         String strSite = hmSite.get("SITE_NUMBER").toString();
         int siteNumber = Integer.parseInt(strSite);

         HashMap hmCongDist = perfSiteTxnBean.getCongDistrict(propNumber, siteNumber);
         if (hmCongDist != null && hmCongDist.get("CONGDIST") != null)
           siteLocationDataType.setCongressionalDistrictProgramProject((String) hmCongDist.get("CONGDIST").toString());
         
       
      
       return siteLocationDataType;
    }
     
    private String getIndividual(String orgID)
            throws CoeusException, DBException{
        String indYNQ = null;
        HashMap hmInd = perfSiteTxnBean.getIndivYNQ(orgID);
        if (hmInd != null && hmInd.get("INDIVIDUAL") != null) {
            indYNQ = hmInd.get("INDIVIDUAL").toString();
        }
            
        return indYNQ;
        
    }
    private gov.grants.apply.system.globallibrary_v2.AddressDataType getAddressType
                (RolodexDetailsBean rolodexDetailsBean) 
                throws JAXBException, DBException,CoeusException{
        HashMap hmRow = new HashMap();
        String countryDesc = null;
        String stateDesc = null;
        
        //Street 1 and City and country are mandatory.
        AddressDataType addressType = 
                        globalLibObjFactory.createAddressDataType();
        if(rolodexDetailsBean.getAddress1() != null)
            addressType.setStreet1(rolodexDetailsBean.getAddress1());
        if(rolodexDetailsBean.getAddress2() != null)
             addressType.setStreet2(rolodexDetailsBean.getAddress2());
        if(rolodexDetailsBean.getCity() != null)
           addressType.setCity(rolodexDetailsBean.getCity());
      
        String state = rolodexDetailsBean.getState() == null ? null : rolodexDetailsBean.getState().toString();
        String country = rolodexDetailsBean.getCountry() == null ? null : rolodexDetailsBean.getCountry().toString();

        
        if (country != null){
                  if (rolodexDetailsBean.getCountryName() != null) {
                     hmRow = perfSiteTxnBean.getCountryName(country);
                     countryDesc = hmRow.get("COUNTRY_NAME").toString();    
                     rolodexDetailsBean.setCountryName(countryDesc);
                }
        }
      
        if (state != null) {
                if (perfSiteTxnBean.getStateName(country,state) != null) {
                     hmRow = perfSiteTxnBean.getStateName(country,state);
                     stateDesc = hmRow.get("STATE_NAME").toString();
                }
        }
                
        //if country is not USA,  state data populates province  
   
        if (country != null){
         if (country.equals("USA")) {
             addressType.setState(stateDesc);
   
         } else {
               if (state != null)
                   addressType.setProvince(stateDesc);
     
         }
        }
        
   
        if(rolodexDetailsBean.getPostalCode() != null)
            addressType.setZipPostalCode(rolodexDetailsBean.getPostalCode());
        if(rolodexDetailsBean.getCounty() != null)
            addressType.setCounty(rolodexDetailsBean.getCounty());

       if(rolodexDetailsBean.getCountry() != null) 
           addressType.setCountry(rolodexDetailsBean.getCountryName());   
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
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"RRPerformanceSiteStream_V1_4","getAttachedSite()");
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
        UtilFactory.log(ex.getMessage(),ex, "RRPerformanceSiteStream_V1_4", "getAttachedFile");
        throw new JAXBException(ex);
    }

    return attachedFileType;

}
   
    public Object getStream(HashMap hashMap) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)hashMap.get("PROPOSAL_NUMBER");
        return getPerformanceSite();
    } 
    
        
    
}