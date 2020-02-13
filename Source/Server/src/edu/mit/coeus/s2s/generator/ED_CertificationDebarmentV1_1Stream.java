/*
 * ED_CertificationDebarmentV1_1Stream.java
 *
 * Created on March 7, 2007, 3:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
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
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.xml.bean.proposal.bean.ProposalPrintingTxnBean;
import gov.grants.apply.forms.ed_certificationdebarment_v1_1.*;
import gov.grants.apply.system.globallibrary_v2.*;
import java.util.*;
import javax.xml.bind.JAXBException;


public class ED_CertificationDebarmentV1_1Stream extends S2SBaseStream{ 
    
    private gov.grants.apply.forms.ed_certificationdebarment_v1_1.ObjectFactory objFactory;
    private gov.grants.apply.system.globallibrary_v2.ObjectFactory globallibraryObjFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;
    private CoeusXMLGenrator xmlGenerator;
   
    private String propNumber;
    private UtilFactory utilFactory;
    private DateUtils dateUtils;
 
    
    /** Creates a new instance of ED_CertificationDebarmentV1_1Stream */
    public ED_CertificationDebarmentV1_1Stream() {
        objFactory = new gov.grants.apply.forms.ed_certificationdebarment_v1_1.ObjectFactory();
        globallibraryObjFactory = new gov.grants.apply.system.globallibrary_v2.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator(); 
    }
    
    private  CertificationDebarmentType  getCertificationDebarment()
                throws CoeusXMLException,CoeusException,DBException,JAXBException{
       CertificationDebarmentType  ED_Certification = objFactory.createCertificationDebarment();          
       try{
           ProposalPrintingTxnBean propPrintingTxnBean = new ProposalPrintingTxnBean();
           
           
           /** aorBean holds information for AOR */
            DepartmentPersonFormBean aorBean;
            aorBean = propPrintingTxnBean.getAuthorizedRep(propNumber); 
            
            /** 
            * FormVersion
            */
           ED_Certification.setFormVersion("1.1");  
           
           //set name of applicant
           ED_CertificationDebarmentTxnBean ed_certificationForm = new ED_CertificationDebarmentTxnBean();
           HashMap hmInfo = new HashMap();
           hmInfo =  ed_certificationForm.getOrganizationName(propNumber);
           if (hmInfo != null && hmInfo.get("ORGANIZATION_NAME") != null){              
               if (hmInfo.get("ORGANIZATION_NAME").toString().length() > 60)
                   ED_Certification.setOrganizationName(hmInfo.get("ORGANIZATION_NAME").toString().substring(0,60));
               else    
                   ED_Certification.setOrganizationName(hmInfo.get("ORGANIZATION_NAME").toString());
           }
           
           ED_Certification.setAuthorizedRepresentativeName(getName(aorBean));
           
           if(aorBean.getPrimaryTitle() !=null && aorBean.getPrimaryTitle().length()>45)
                ED_Certification.setAuthorizedRepresentativeTitle(aorBean.getPrimaryTitle().substring(0,45));
           else
                ED_Certification.setAuthorizedRepresentativeTitle(aorBean.getPrimaryTitle());
           
           ED_Certification.setAuthorizedRepresentativeSignature(aorBean.getFullName());
           
           ED_Certification.setSubmittedDate(dateUtils.getLocalCalendar());
           //get attachment
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


                   if ( narrativeType == 58 ) { 
                       //ED_CertificationDebarment
                      if (attachment == null) {
                        proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                        Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                        if (narrativeAttachment.getContent() != null){
                            gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
                            attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                           ED_Certification.setAttachment(attachedFileType);

                         }
                      } 
                   }
               }        
           
        }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"ED_CertificationDebarmentV1_1Stream", "getCertificationDebarment()");
            throw new CoeusXMLException(jaxbEx.getMessage());
       }    
        
        return ED_Certification;
    }
    
    private  HumanNameDataType getName(DepartmentPersonFormBean perBean)
	throws CoeusException, JAXBException {
	HumanNameDataType humanNameDataType = globallibraryObjFactory.createHumanNameDataType();
        humanNameDataType.setFirstName(perBean.getFirstName());
        humanNameDataType.setLastName(perBean.getLastName());
        humanNameDataType.setMiddleName(perBean.getMiddleName());
        
        return humanNameDataType;
  }
    
    public Object getStream(java.util.HashMap ht) throws JAXBException, CoeusException, DBException {
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getCertificationDebarment();
    }
}


