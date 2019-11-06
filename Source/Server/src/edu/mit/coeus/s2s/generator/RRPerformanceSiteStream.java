/*
 * @(#)RRPerformanceSiteStream.java May 5,2005
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
import edu.mit.coeus.s2s.generator.stream.bean.RRPerformanceSiteTxnBean;
import edu.mit.coeus.s2s.generator.stream.AttachedFileDataTypeStream;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
 
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import gov.grants.apply.system.metagrantapplication.GrantApplicationType;
import gov.grants.apply.forms.rr_performancesite_v1.*;
import gov.grants.apply.system.globallibrary_v1.*;
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
 * @author  Coeus Dev Team
 * @Created on May 5, 2005, 10:12 AM
 * Class for generating the object stream for grants.gov RR_PerformanceSite. It uses jaxb classes
 * which have been created under gov.grants.atapply package. Fetch the data 
 * from database and attach with the jaxb beans which have been derived from 
 * RR_PerformanceSite schema.
 */

 public class RRPerformanceSiteStream extends S2SBaseStream{
    private gov.grants.apply.forms.rr_performancesite_v1.ObjectFactory objFactory;
    private gov.grants.apply.system.globallibrary_v1.ObjectFactory globalLibObjFactory;
    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
    private CoeusXMLGenrator xmlGenerator;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;

    gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
    
    private UtilFactory utilFactory;
     
    //txn beans
    private ProposalDevelopmentTxnBean propDevTxnBean;
    private OrganizationMaintenanceDataTxnBean orgMaintDataTxnBean;
    private RolodexMaintenanceDataTxnBean rolodexMaintDataTxnBean;
    private RRPerformanceSiteTxnBean perfSiteTxnBean;
    
    /** propDevFormBean holds proposal master info   */
    private ProposalDevelopmentFormBean propDevFormBean;
    /** orgMaintFormBean holds performing organization master info   */
    private OrganizationMaintenanceFormBean perfOrgFormBean;
    /** rolodexDetailsBean holds rolodex information for performing orgContact */
    private RolodexDetailsBean perfOrgContactRolodexDetailsBean;
    
    private Hashtable propData;
    private String propNumber;
    
    private static final String UNKNOWN = "UNKNOWN";
     //start case 2406
    private String organizationID;
    private String perfOrganizationID;
    //end case 2406
  
    /** Creates a new instance of SF424Stream */
    public RRPerformanceSiteStream(){
        System.out.println("***inside RRPerformanceSiteStream constructor()");
        this.propNumber = propNumber;
        objFactory = new gov.grants.apply.forms.rr_performancesite_v1.ObjectFactory();
        globalLibObjFactory = new gov.grants.apply.system.globallibrary_v1.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();
        propDevTxnBean = new ProposalDevelopmentTxnBean();
        orgMaintDataTxnBean = new OrganizationMaintenanceDataTxnBean();
        rolodexMaintDataTxnBean = new RolodexMaintenanceDataTxnBean();
        perfSiteTxnBean = new RRPerformanceSiteTxnBean();
    } 
    
    private RRPerformanceSiteType getRRPerformanceSite() 
                            throws CoeusXMLException,CoeusException,DBException{
        RRPerformanceSiteType performanceSite = null;
        
        try{
            performanceSite = objFactory.createRRPerformanceSite();

             //get proposal master info
            propDevFormBean = getPropDevData();
            //get performing organization info
             //start case 2406
//            perfOrgFormBean = getOrgData(propDevFormBean.getPerformingOrganizationId());
             perfOrgFormBean = getPerfOrgData();
            //get rolodex information for performing org contact
            perfOrgContactRolodexDetailsBean = 
                            getRolodexData(perfOrgFormBean.getContactAddressId());
            performanceSite.setFormVersion("1.0");
            performanceSite.setPrimarySite(
                getSiteLocationDataType(perfOrgContactRolodexDetailsBean)); 
            //get other site data
            ArrayList otherSiteList = perfSiteTxnBean.getOtherSiteList(propNumber);
            //get reference to otherSite member of RRPerfomanceSiteType instance
            java.util.List otherSites = performanceSite.getOtherSite();
            //populate other site data.
             //case 2406 start
            HashMap hmOtherSites = new HashMap();
            if (otherSiteList != null) {
                
                for(int siteCnt=0; siteCnt<otherSiteList.size(); siteCnt++){
                    hmOtherSites = (HashMap)otherSiteList.get(siteCnt);
                    RolodexDetailsBean rolodexDetails = (RolodexDetailsBean) hmOtherSites.get("rolodexBean");
//                  RolodexDetailsBean rolodexDetails = 
//                                (RolodexDetailsBean)otherSiteList.get(siteCnt);
                    if(!rolodexDetails.getOrganization().equals
                                    (perfOrgFormBean.getOrganizationName())){
                        SiteLocationDataType otherSite = 
                                    getSiteLocationDataType(rolodexDetails);
                        otherSites.add(otherSite);
                    }
                }
            }
            
            //added march 9,2006
            attachedFileType = getAttachedSite();
            if (attachedFileType.getFileName() != null)
       //     if (attachedFileType != null)
                performanceSite.setAttachedFile(attachedFileType);

         }catch (JAXBException jaxbEx){
            UtilFactory.log(jaxbEx.getMessage(),jaxbEx,"RRPerformanceSiteStream",
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
     
    private SiteLocationDataType getSiteLocationDataType
                (RolodexDetailsBean rolodexDetailsBean) throws JAXBException{
       SiteLocationDataType siteLocationDataType = objFactory.createSiteLocationDataType();
       siteLocationDataType.setOrganizationName(rolodexDetailsBean.getOrganization());
       siteLocationDataType.setAddress(getAddressType(rolodexDetailsBean));
       return siteLocationDataType;
    }
     
     
    private SiteLocationDataType.AddressType getAddressType
                (RolodexDetailsBean rolodexDetailsBean) throws JAXBException{
        //Street 1 and City are mandatory.
        SiteLocationDataType.AddressType addressType = 
                        objFactory.createSiteLocationDataTypeAddressType();
        addressType.setStreet1(checkNull(rolodexDetailsBean.getAddress1()));
        addressType.setStreet2(rolodexDetailsBean.getAddress2());
        addressType.setCity(checkNull(rolodexDetailsBean.getCity()));
        addressType.setState(rolodexDetailsBean.getState());
        addressType.setZipCode(rolodexDetailsBean.getPostalCode());
        addressType.setCounty(rolodexDetailsBean.getCounty());
        addressType.setCountry(rolodexDetailsBean.getCountry());        
        return addressType;
                                    
    }
    
     /**************************************
     *  getAttachedSite
     *  added March 9,2006
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
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"RRPerformanceSiteStream","getAttachedSite()");
            throw new CoeusXMLException(jaxbEx.getMessage());
         }
        }
     
     private gov.grants.apply.system.attachments_v1.AttachedFileDataType getAttachedFileType(Attachment attachment) 
     throws JAXBException {
    
    gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
    gov.grants.apply.system.attachments_v1.AttachedFileDataType.FileLocationType fileLocation;
    gov.grants.apply.system.global_v1.HashValueType hashValueType;

    attachedFileType = attachmentsObjFactory.createAttachedFileDataType();
//     if(attachment == null){
//            return attachedFileType;
//        }
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
        UtilFactory.log(ex.getMessage(),ex, "PHSModularBudgetStream", "getAttachedFile");
        throw new JAXBException(ex);
    }

    return attachedFileType;
           
}
    
    
     
        
   
    public Object getStream(HashMap hashMap) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)hashMap.get("PROPOSAL_NUMBER");
        return getRRPerformanceSite();
    } 
    
   private String checkNull (String s) {
       
       if (s == null){
           return "Unknown";
       }else {
           return s;
       }
      
   }    
    
}
