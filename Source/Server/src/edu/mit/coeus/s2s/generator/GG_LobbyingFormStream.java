/*
 * GG_LobbyingFormStream.java
 *
* Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.generator;

import edu.mit.coeus.departmental.bean.DepartmentPersonFormBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.s2s.bean.*;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.s2s.util.S2SHashValue;
import edu.mit.coeus.utils.xml.bean.proposal.bean.ProposalPrintingTxnBean;
import gov.grants.apply.forms.gg_lobbyingform_v1.*;
import gov.grants.apply.system.globallibrary_v1.*;
import java.util.*;
import javax.xml.bind.JAXBException;


public class GG_LobbyingFormStream extends S2SBaseStream{ 
    private gov.grants.apply.forms.gg_lobbyingform_v1.ObjectFactory objFactory;
    private gov.grants.apply.system.globallibrary_v1.ObjectFactory globallibraryObjFactory;
    private CoeusXMLGenrator xmlGenerator;
   
    private String propNumber;
    private UtilFactory utilFactory;
    
//    private ProposalPrintingTxnBean propPrintingTxnBean;
//     /** aorBean holds information for AOR */
//    private DepartmentPersonFormBean aorBean;
    
    /** Creates a new instance of GG_LobbyingFormStream */
    public GG_LobbyingFormStream() {
        objFactory = new gov.grants.apply.forms.gg_lobbyingform_v1.ObjectFactory();
        globallibraryObjFactory = new gov.grants.apply.system.globallibrary_v1.ObjectFactory();
//        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator(); 
    }
    
    private  LobbyingFormType  getGG_LobbyingForm()
                throws CoeusXMLException,CoeusException,DBException,JAXBException{
       LobbyingFormType  lobbyingForm = objFactory.createLobbyingForm(); 
                    
       try{
            ProposalPrintingTxnBean propPrintingTxnBean  = new ProposalPrintingTxnBean();
             /** aorBean holds information for AOR */
             DepartmentPersonFormBean aorBean;
             //get AOR
            aorBean = propPrintingTxnBean.getAuthorizedRep(propNumber);   
            /**
            * FormVersion
            */
           lobbyingForm.setFormVersion("1.0");  
           
           //set Applicant's OrganizationName
           GG_LobbyingFormV11TxnBean gg_lobbyingForm = new GG_LobbyingFormV11TxnBean();
           HashMap hmInfo = new HashMap();
           hmInfo =  gg_lobbyingForm.getOrganizationName(propNumber);
           if (hmInfo != null && hmInfo.get("ORGANIZATION_NAME") != null){
               if (hmInfo.get("ORGANIZATION_NAME").toString().length() > 120)
                   lobbyingForm.setApplicantName(hmInfo.get("ORGANIZATION_NAME").toString().substring(0,120));
               else    
                   lobbyingForm.setApplicantName(hmInfo.get("ORGANIZATION_NAME").toString());
           }
                    
           //set NAME AND TITLE OF AUTHORIZED REPRESENTATIVE
           lobbyingForm.setAuthorizedRepresentativeName(getName(aorBean));
            if(aorBean.getPrimaryTitle() !=null && aorBean.getPrimaryTitle().length()>45)
                lobbyingForm.setAuthorizedRepresentativeTitle(aorBean.getPrimaryTitle().substring(0,45));
           else
                lobbyingForm.setAuthorizedRepresentativeTitle(aorBean.getPrimaryTitle());
           
           lobbyingForm.setAuthorizedRepresentativeSignature(aorBean.getFullName());
           lobbyingForm.setSubmittedDate(getTodayDate());
       }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"GG_LobbyingFormStream", "getGG_LobbyingForm()");
            throw new CoeusXMLException(jaxbEx.getMessage());
       }    
        
        return lobbyingForm;
    }
    
    private  HumanNameDataType getName(DepartmentPersonFormBean perBean)
	throws CoeusException, JAXBException {
	HumanNameDataType humanNameDataType = globallibraryObjFactory.createHumanNameDataType();
        humanNameDataType.setFirstName(perBean.getFirstName());
        humanNameDataType.setLastName(perBean.getLastName());
        humanNameDataType.setMiddleName(perBean.getMiddleName());
        
        return humanNameDataType;
  }
    
    private Calendar getTodayDate() {
      Calendar cal = Calendar.getInstance(TimeZone.getDefault()); 
      java.util.Date today = cal.getTime();
      cal.setTime(today);
      return cal;
    }
    
    public Object getStream(java.util.HashMap ht) throws JAXBException, CoeusException, DBException {
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getGG_LobbyingForm();
    }
}
