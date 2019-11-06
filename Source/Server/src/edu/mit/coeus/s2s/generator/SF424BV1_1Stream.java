 /*
 * @(#)SF424BV1_1Stream.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.s2s.generator;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import java.util.Calendar;
import java.util.TimeZone;
import javax.xml.bind.JAXBException;
import gov.grants.apply.forms.sf424b_v1_1.*;

import edu.mit.coeus.departmental.bean.DepartmentPersonFormBean;
import edu.mit.coeus.organization.bean.OrganizationAddressFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.utils.xml.bean.proposal.bean.ProposalPrintingTxnBean;

public class SF424BV1_1Stream extends S2SBaseStream{ 
    private gov.grants.apply.forms.sf424b_v1_1.ObjectFactory objFactory;
//    private gov.grants.apply.system.globallibrary_v2.ObjectFactory globallibraryObjFactory;
    private CoeusXMLGenrator xmlGenerator;
   
    private String propNumber;
    private UtilFactory utilFactory;
    
    /** Creates a new instance of SF424BV1_1Stream */
    public SF424BV1_1Stream() {
        objFactory = new gov.grants.apply.forms.sf424b_v1_1.ObjectFactory();
//        globallibraryObjFactory = new gov.grants.apply.system.globallibrary_v2.ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();   
    }
    
    private AssuranceType getSF424B()
                throws CoeusXMLException,CoeusException,DBException,JAXBException{
       AssuranceType SF424B = objFactory.createAssurances();          
       try{
           /**
            * FormVersion
            */
           SF424B.setCoreSchemaVersion("1.1");  
           SF424B.setProgramType("Non-Construction");
           SF424B.setFormVersionIdentifier("1.1");
           ProposalDevelopmentTxnBean propDevTxnBean = new ProposalDevelopmentTxnBean();
           ProposalDevelopmentFormBean propDevFormBean = propDevTxnBean.getProposalDevelopmentDetails(propNumber);
           OrganizationAddressFormBean orgAddressFormBean = propDevFormBean.getOrganizationAddressFormBean();
           if (orgAddressFormBean != null)
                SF424B.setApplicantOrganizationName(orgAddressFormBean.getOrganizationName());
           ProposalPrintingTxnBean propPrintingTxnBean = new ProposalPrintingTxnBean();
           DepartmentPersonFormBean aorBean = propPrintingTxnBean.getAuthorizedRep(propNumber);   
           if (aorBean != null)  { 
                AuthorizedRepresentativeType authorizedRepresentative = objFactory.createAuthorizedRepresentative();
                authorizedRepresentative.setRepresentativeTitle(aorBean.getPrimaryTitle());
                //case 3182 start
                authorizedRepresentative.setRepresentativeName(aorBean.getFullName());
                //case 3182 end 
                SF424B.setAuthorizedRepresentative(authorizedRepresentative);                
           }
           
           //case 3182 start        
           SF424B.setSubmittedDate(getTodayDate());
           //case 3182 end 
       
       }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"SF424BV1_1Stream","getSF424B()");
            throw new CoeusXMLException(jaxbEx.getMessage());
       }
       
       return SF424B;
    }
    private Calendar getTodayDate() {
      Calendar cal = Calendar.getInstance(TimeZone.getDefault()); 
      java.util.Date today = cal.getTime();
      cal.setTime(today);
      return cal;
    }
       
    public Object getStream(java.util.HashMap ht) throws JAXBException, CoeusException, DBException {
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getSF424B();
    }
}
