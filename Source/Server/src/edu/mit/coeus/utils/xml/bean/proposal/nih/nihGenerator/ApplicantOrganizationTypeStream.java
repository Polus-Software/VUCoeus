/*
 * ApplicantOrganizationTypeStream.java
 *
 * Created on Mar 3, 2004
 */

package edu.mit.coeus.utils.xml.bean.proposal.nih.nihGenerator;

import java.util.* ;
import java.math.BigInteger;

import edu.mit.coeus.propdev.bean.*;
import edu.mit.coeus.organization.bean.*;
import edu.mit.coeus.rolodexmaint.bean.*;
import edu.mit.coeus.propdev.bean.* ;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.xml.bean.proposal.nih.*;
import edu.mit.coeus.utils.xml.bean.proposal.common.commonGenerator.PostalAddressTypeStream;
import edu.mit.coeus.departmental.bean.DepartmentPersonFormBean;
  
public class ApplicantOrganizationTypeStream
{
    ObjectFactory objFactory ;
    edu.mit.coeus.utils.xml.bean.proposal.common.ObjectFactory commonObjFactory;
    edu.mit.coeus.utils.xml.bean.proposal.rar.ObjectFactory rarObjFactory;
    ApplicantOrganizationType applicantOrganizationType;
    edu.mit.coeus.utils.xml.bean.proposal.rar.ApplicantOrganizationType  rarApplicantOrgType;
 
 
    
    /** Creates a new instance of ApplicantOrganizationTypeStream */
    public ApplicantOrganizationTypeStream(ObjectFactory objFactory,
                                 edu.mit.coeus.utils.xml.bean.proposal.rar.ObjectFactory rarObjFactory,
                                 edu.mit.coeus.utils.xml.bean.proposal.common.ObjectFactory commonObjFactory) 
    {
        this.objFactory = objFactory ;
        this.rarObjFactory = rarObjFactory;
        this.commonObjFactory = commonObjFactory;
      
    }

  
      public ApplicantOrganizationType getNihApplicantOrgTypeInfo(OrganizationMaintenanceFormBean orgBean,
                  RolodexDetailsBean rolodexBean, DepartmentPersonFormBean orgContactPersonBean
                        ) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {  
       OrganizationMaintenanceDataTxnBean orgMaintDataTxnBean = new OrganizationMaintenanceDataTxnBean();
       OrganizationListBean[] orgTypes = orgMaintDataTxnBean.getSelectedOrganizationList(orgBean.getOrganizationId());
 
       
       //set the organization information
       applicantOrganizationType = objFactory.createApplicantOrganizationType();
        
       if (orgBean.getOrganizationName() == null){
           applicantOrganizationType.setOrganizationName("Unknown");
       }else  applicantOrganizationType.setOrganizationName(orgBean.getOrganizationName());
   
       if (orgBean.getDunsNumber() == null) {
           applicantOrganizationType.setOrganizationDUNS("Unknown");
       }else  applicantOrganizationType.setOrganizationDUNS(orgBean.getDunsNumber());
       
       if (orgBean.getFederalEmployerID() == null) {
           applicantOrganizationType.setOrganizationEIN("Unknown");
       }else  applicantOrganizationType.setOrganizationEIN(orgBean.getFederalEmployerID());
       
       //addition jan 30 for phs account
       if (orgBean.getPhsAcount() != null){
           applicantOrganizationType.setPHSAccountID(orgBean.getPhsAcount());
       }
        //use first org type, since schema allows for only one and coeus allows multiple
   
      if (orgTypes == null){
           applicantOrganizationType.setOrganizationCategoryCode("Unknown");
           applicantOrganizationType.setOrganizationCategoryDescription("Unknown");
       }
       applicantOrganizationType.setOrganizationCategoryCode(Integer.toString(orgTypes[0].getOrganizationTypeCode()));
       applicantOrganizationType.setOrganizationCategoryDescription(orgTypes[0].getDescription());
    
       
       
       if (orgBean.getCongressionalDistrict() == null) {
            applicantOrganizationType.setOrganizationCongressionalDistrict("Unknown");
       } else applicantOrganizationType.setOrganizationCongressionalDistrict(orgBean.getCongressionalDistrict());
                                                                     
          
       PostalAddressTypeStream postalAddressTypeStream = new PostalAddressTypeStream(commonObjFactory);
       applicantOrganizationType.setOrganizationAddress(postalAddressTypeStream.getPostalAddressInfo(rolodexBean));
     
       ApplicantOrganizationType.OrganizationClassificationType orgClassification;
       orgClassification = objFactory.createApplicantOrganizationTypeOrganizationClassificationType();
       
       edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator.ApplicantOrganizationTypeStream rarStream
           = new edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator.ApplicantOrganizationTypeStream(rarObjFactory,commonObjFactory);
       
       rarApplicantOrgType = rarStream.getApplicantOrgTypeInfo(orgBean, rolodexBean,orgContactPersonBean);
        
       applicantOrganizationType.setOrganizationContactPerson(rarApplicantOrgType.getOrganizationContactPerson());
       applicantOrganizationType.setCageNumber(rarApplicantOrgType.getCageNumber());
      
       orgClassification.setCategoryCode(Integer.toString(orgTypes[0].getOrganizationTypeCode()));
       orgClassification.setSubCategoryCode(orgTypes[0].getDescription());
       applicantOrganizationType.setOrganizationClassification(orgClassification);
       
       return applicantOrganizationType;
    }
}
