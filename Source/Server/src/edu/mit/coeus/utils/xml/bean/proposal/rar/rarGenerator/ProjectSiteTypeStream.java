/*
 * ProjectSiteTypeStream.java
 *
 * Created on September 7, 2004
 */

package edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator;

/**
 *
 * @author  ele
 */

import edu.mit.coeus.propdev.bean.* ;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceFormBean;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceDataTxnBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.s2s.generator.stream.bean.RRPerformanceSiteTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.xml.bean.proposal.rar.*;
import edu.mit.coeus.utils.xml.bean.proposal.common.commonGenerator.PostalAddressTypeStream;
import edu.mit.coeus.utils.xml.bean.proposal.common.commonGenerator.ContactInfoTypeStream;
import java.util.ArrayList;
import java.util.HashMap;


public class ProjectSiteTypeStream {
    
    ObjectFactory objFactory ;
    edu.mit.coeus.utils.xml.bean.proposal.common.ObjectFactory commonObjFactory;
   
    ProjectSiteType projectSiteType;
    RRPerformanceSiteTxnBean perfSiteTxnBean;
    
   
    
    
    /** Creates a new instance of ProjectSiteTypeStream */
    public ProjectSiteTypeStream(ObjectFactory objFactory, edu.mit.coeus.utils.xml.bean.proposal.common.ObjectFactory commonObjFactory) {
         this.objFactory = objFactory ;
         this.commonObjFactory = commonObjFactory;
         perfSiteTxnBean = new RRPerformanceSiteTxnBean();
    }
    
    
    public ProjectSiteType getProjectSite(
        OrganizationMaintenanceFormBean orgBean, RolodexDetailsBean rolodexBean)
    throws CoeusException, DBException, javax.xml.bind.JAXBException
   
    {     
           
       projectSiteType = objFactory.createProjectSiteType();
       projectSiteType.setOrganizationName(orgBean.getOrganizationName());
       projectSiteType.setCongressionalDistrict(orgBean.getCongressionalDistrict());
    
       
       PostalAddressTypeStream postalAddressTypeStream = new PostalAddressTypeStream(commonObjFactory);
       projectSiteType.setPostalAddress(postalAddressTypeStream.getPostalAddressInfo(rolodexBean));
     
     
    return projectSiteType;
    }


 public ArrayList getAlternateProjectSites(
        String propNumber)
    throws CoeusException, DBException, javax.xml.bind.JAXBException
   
    {    ArrayList otherSites = new ArrayList();
         PostalAddressTypeStream postalAddressTypeStream ;
         ArrayList otherSiteList = perfSiteTxnBean.getOtherSiteList(propNumber);
         HashMap hmOtherSites = new HashMap();
         //populate other site data.

         if (otherSiteList != null)  //case 2406 changes below
         for(int siteCnt=0; siteCnt<otherSiteList.size(); siteCnt++){
           
             hmOtherSites = (HashMap)otherSiteList.get(siteCnt);
             
            RolodexDetailsBean rolodexBean = (RolodexDetailsBean) hmOtherSites.get("rolodexBean");
            String congDist = (String) hmOtherSites.get("congDist");                 
           
            projectSiteType = objFactory.createProjectSiteType();
            projectSiteType.setOrganizationName(rolodexBean.getOrganization());
            postalAddressTypeStream = new PostalAddressTypeStream(commonObjFactory);
            projectSiteType.setPostalAddress(postalAddressTypeStream.getPostalAddressInfo(rolodexBean));
            projectSiteType.setCongressionalDistrict(congDist);  
            otherSites.add(projectSiteType);
            }
            
       
       return otherSites;
    }
}
