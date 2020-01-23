 /*
 * @(#)CD511V1_1Stream.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */


package edu.mit.coeus.s2s.generator;

import edu.mit.coeus.departmental.bean.DepartmentPersonFormBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.organization.bean.OrganizationAddressFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.xml.bean.proposal.bean.ProposalPrintingTxnBean;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import gov.grants.apply.forms.cd511_v1_1.*;
import gov.grants.apply.system.globallibrary_v2.*;
import java.util.Calendar;
import java.util.TimeZone;
import javax.xml.bind.JAXBException;

public class CD511V1_1Stream extends S2SBaseStream{ 
    private gov.grants.apply.forms.cd511_v1_1.ObjectFactory objFactory;
    private gov.grants.apply.system.globallibrary_v2.ObjectFactory globallibraryObjFactory;
    private CoeusXMLGenrator xmlGenerator;
   
    private String propNumber;
    private UtilFactory utilFactory;
    
    private ProposalDevelopmentTxnBean propDevTxnBean;
    /** Creates a new instance of CD511V1_1Stream */
    public CD511V1_1Stream() {
        objFactory = new gov.grants.apply.forms.cd511_v1_1.ObjectFactory();
        globallibraryObjFactory = new gov.grants.apply.system.globallibrary_v2.ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();   
        propDevTxnBean = new ProposalDevelopmentTxnBean();
    }
    
    
    private CD511Type getCD511()
                throws CoeusXMLException,CoeusException,DBException,JAXBException{
       CD511Type cd511 = objFactory.createCD511();          
       try{
           /**
            * FormVersion
            */
           cd511.setFormVersion("1.1");  
           //get proposal master info
           ProposalDevelopmentFormBean propDevFormBean = propDevTxnBean.getProposalDevelopmentDetails(propNumber);
           OrganizationAddressFormBean orgAddressFormBean = propDevFormBean.getOrganizationAddressFormBean();
           cd511.setOrganizationName(orgAddressFormBean.getOrganizationName());
           cd511.setAwardNumber(propDevFormBean.getCurrentAwardNumber());
           cd511.setProjectName(propDevFormBean.getTitle());
//           RolodexDetailsBean rolodexDetailsBean = propDevFormBean.getRolodexDetailsBean();
           ProposalPrintingTxnBean propPrintingTxnBean = new ProposalPrintingTxnBean();
           DepartmentPersonFormBean aorBean = propPrintingTxnBean.getAuthorizedRep(propNumber);   
           if (aorBean != null){
               cd511.setContactName(getContactName(aorBean));
               cd511.setTitle(aorBean.getPrimaryTitle());
               // CD511-V1.1 data analysis file said:
               // if this application is submitted through Grants.gov 
               // leave signature to blank
               cd511.setSignature("  ");
//               cd511.setSignature(aorBean.getFullName());
               cd511.setSubmittedDate(getTodayDate());
           }
           
       }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"CD511V1_1Stream","getCD511()");
            throw new CoeusXMLException(jaxbEx.getMessage());
       }    
        
        return cd511;
    }
    
    private ProposalDevelopmentFormBean getPropDevData() throws DBException,CoeusException{
        if(propNumber==null) 
            throw new CoeusXMLException("Proposal Number is Null");
        return propDevTxnBean.getProposalDevelopmentDetails(propNumber);
    }
    
    private HumanNameDataType  getContactName(DepartmentPersonFormBean pBean)
                  throws JAXBException,  CoeusException,DBException{
   
    HumanNameDataType nameType = globallibraryObjFactory.createHumanNameDataType(); 
   
    if (pBean.getFirstName() != null)
        nameType.setFirstName(UtilFactory.convertNull(pBean.getFirstName()));  
    if (pBean.getLastName() != null)
        nameType.setLastName(UtilFactory.convertNull(pBean.getLastName()));
    if(pBean.getMiddleName() != null)
        nameType.setMiddleName(UtilFactory.convertNull(pBean.getMiddleName()));
//    if (pBean.getPrefix() != null)
//        nameType.setPrefixName(UtilFactory.convertNull(pBean.getPrefix()));
//    if (pBean.getSuffix() != null)
//        nameType.setSuffixName(UtilFactory.convertNull(pBean.getSuffix()));
    
    return nameType;
   }
     
     private Calendar getTodayDate() {
      Calendar cal = Calendar.getInstance(TimeZone.getDefault()); 
      java.util.Date today = cal.getTime();
      cal.setTime(today);
      return cal;
    }
    public Object getStream(java.util.HashMap ht) throws JAXBException, CoeusException, DBException {
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getCD511();
    }
    
    
}
