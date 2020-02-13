/*
 * NasaSeniorKeyPersonSupplementalDataSheetStream.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.generator;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalBiographyFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalPersonBioPDFBean;
import edu.mit.coeus.propdev.bean.ProposalPersonFormBean;
import edu.mit.coeus.propdev.bean.ProposalPersonTxnBean;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import edu.mit.coeus.s2s.bean.KeyPersonBean;
import edu.mit.coeus.s2s.bean.NasaSeniorKeyPersonSupplementalDataSheetTxnBean;
import edu.mit.coeus.s2s.bean.S2STxnBean;
import edu.mit.coeus.s2s.generator.stream.AttachedFileDataTypeStream;
import edu.mit.coeus.s2s.util.S2SHashValue;
import edu.mit.coeus.utils.CoeusVector;
import gov.grants.apply.forms.nasa_senkpsupdtsht_v1.*;
import gov.grants.apply.system.globallibrary_v2.*;
import java.util.*;
import javax.xml.bind.JAXBException;

/**
 *
 * @author  jenlu
 */
public class NasaSeniorKeyPersonSupplementalDataSheetSteam extends S2SBaseStream  { 
    private gov.grants.apply.forms.nasa_senkpsupdtsht_v1.ObjectFactory objFactory;
    private gov.grants.apply.system.globallibrary_v2.ObjectFactory globallibObjFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;
    private CoeusXMLGenrator xmlGenerator;
    //txn bean
    private NasaSeniorKeyPersonSupplementalDataSheetTxnBean nasaSeniorKeyPersonSmtDtShtTxnBean;    
    private S2STxnBean s2sTxnBean; 
    private ProposalDevelopmentTxnBean propDevTxnBean;
    private ProposalPersonTxnBean proposalPersonTxnBean;
    
    //data beans
    private ProposalDevelopmentFormBean propDevFormBean;
    
    private HashMap hmInfo; 
    private CoeusVector keyPersons;
    private CoeusVector extraPersons; 
    private String propNumber;
    private boolean primeSponsorNasa = false;
    private UtilFactory utilFactory;
    
    private static final String COPI = "Co-PD/PI";
   private static final String PDPI = "PD/PI";
    public static final String STATEMENTOFCOMMITMENT = "STATEMENTOFCOMMITMENT";
    public static final String BUDGETDETAILS = "BUDGETDETAILS";
    //case 2635 start    
    public static final int BUDGETDETAILS_DOC = 3;
    public static final int STATEMENTOFCOMMITMENT_DOC = 4;
    //case 2635 end 
    
    /** Creates a new instance of NasaSeniorKeyPersonSupplementalDataSheetStream */
    public NasaSeniorKeyPersonSupplementalDataSheetSteam() {
        objFactory = new gov.grants.apply.forms.nasa_senkpsupdtsht_v1.ObjectFactory();
        globallibObjFactory = new gov.grants.apply.system.globallibrary_v2.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();   
        
        s2sTxnBean = new S2STxnBean();
        propDevTxnBean = new ProposalDevelopmentTxnBean();
        nasaSeniorKeyPersonSmtDtShtTxnBean = new NasaSeniorKeyPersonSupplementalDataSheetTxnBean();
        proposalPersonTxnBean = new ProposalPersonTxnBean();
    }
    
    private NASASeniorKeyPersonSupplementalDataSheetType getNasaSeniorKeyPersonSupplementalDataSheet()
                throws CoeusXMLException,CoeusException,DBException,JAXBException{
        NASASeniorKeyPersonSupplementalDataSheetType nasaSeniorKeyPersonSmtDtSht = 
                        objFactory.createNASASeniorKeyPersonSupplementalDataSheet();
        try{
            hmInfo = new HashMap(); 
            
            //get proposal master info
            propDevFormBean = propDevTxnBean.getProposalDevelopmentDetails(propNumber); 
            /*case 3135 - removed prime sponsor stuff. do not need prime sponsor 
                  also  do not need PI or key persons */
          //  populatePIAndKeyPersons(); 
            populateInvestigators();
          
            
          /**
            *FormVersion
          */ 
            nasaSeniorKeyPersonSmtDtSht.setFormVersion("1.0");
            
            //set first 8 investigators
            for(int cnt=0; cnt<keyPersons.size(); cnt++){                
                KeyPersonBean keyPersonBean = (KeyPersonBean)keyPersons.get(cnt);  
                if (keyPersonBean != null){         
                    nasaSeniorKeyPersonSmtDtSht.getSeniorKeyPerson().add(getPerson(keyPersonBean));
                }        
            }
            //set extra persons if there are any            
            if (extraPersons != null && extraPersons.size() > 0 ){
                 SeniorKeyPersonAttachmentType sKPAttachment
                    = objFactory.createSeniorKeyPersonAttachmentType();
                int extraPersonNumber = extraPersons.size();
                int attPersons ;
                
                for (int begin = 0; begin < extraPersonNumber; begin = begin+8){
                    NASASeniorKeyPersonSupplementalDataSheetAttType
                        sKPAtt = objFactory.createNASASeniorKeyPersonSupplementalDataSheetAttType();
                    if (extraPersonNumber - begin > 8) attPersons = begin+8;
                    else attPersons = extraPersonNumber;
                    for(int cnt=begin; cnt<attPersons; cnt++){                
                        KeyPersonBean keyPersonBean = (KeyPersonBean)extraPersons.get(cnt);  
                        if (keyPersonBean != null){
                       //     hmInfo = nasaSeniorKeyPersonSmtDtShtTxnBean.getPersonInfo(propNumber,keyPersonBean.getPersonId()); 
                            sKPAtt.getSeniorKeyPerson().add(getPerson(keyPersonBean));
                        }
                    }
                    sKPAttachment.getNASASeniorKeyPersonSupplementalDataSheetAtt().add(sKPAtt);
                    if( begin == 0)
                        nasaSeniorKeyPersonSmtDtSht.setAttachment1("Attached Attachment 1");
                    else if( begin == 8)
                        nasaSeniorKeyPersonSmtDtSht.setAttachment2("Attached Attachment 2");
                    else if( begin == 16)
                        nasaSeniorKeyPersonSmtDtSht.setAttachment3("Attached Attachment 3");
                    else if( begin == 24)
                        nasaSeniorKeyPersonSmtDtSht.setAttachment4("Attached Attachment 4");
                }
                
                nasaSeniorKeyPersonSmtDtSht.setSeniorKeyPersonAttachment(sKPAttachment);
            }//end set extra persons
           
        }catch (JAXBException jaxbEx){
            
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"NasaSeniorKeyPersonSupplementalDataSheetSteam","getNasaSeniorKeyPersonSupplementalDataSheet()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        } 
        return nasaSeniorKeyPersonSmtDtSht;
    }
    
    //case 3135 -this gets co-invs and key persons who are collaborators (do not need key persons)
    
     private void populateInvestigators() throws CoeusException, DBException{    
        ArrayList allPersons = nasaSeniorKeyPersonSmtDtShtTxnBean.get_coinvestigators(propNumber,8);
        KeyPersonBean personBean;
        if(allPersons != null && !allPersons.isEmpty()){
            keyPersons = (CoeusVector)allPersons.get(0);            
            extraPersons = (CoeusVector)allPersons.get(1);            
        }
    }
    

     
      private SeniorKeyPersonType  getPerson(KeyPersonBean keyPerson)throws JAXBException,DBException,CoeusException{
        
        SeniorKeyPersonType seniorKeyPerson = objFactory.createSeniorKeyPersonType();
     
   
        //Set name.
        HumanNameDataType humanNameDataType = 
                        globallibObjFactory.createHumanNameDataType();
        humanNameDataType.setFirstName(keyPerson.getFirstName());
        humanNameDataType.setMiddleName(keyPerson.getMiddleName());
        humanNameDataType.setLastName(keyPerson.getLastName()); 
        seniorKeyPerson.setSeniorKeyPersonName(humanNameDataType);
        
        /****************************************************
          * setNASACoItype
           *logic changed as of case 3135
           * Co-I                 : an mit coinvestigator
           * Co-I/Science PI      : not populating this
           * Co-I/Institutional PI: non mit coinvestigator
           * Co-I/Co-PI (non-U.S.organization only): not an mit person; if rolodex.sponsor_code is not null, check for
           *   foreign sponsor type; otherwise check person's addewss
           * Collaborator         : key person whose role is 'Collaborator'
         *****************************************************/
        String foreign = "NO";  //default
        seniorKeyPerson.setInternationalParticipation("N: No");  //default
        seniorKeyPerson.setUSGovernmentParticipation("N: No");   //default
        
        if (keyPerson.getRole().equals("COLLABORATOR")) {
            seniorKeyPerson.setNASACoItype("Collaborator");
        } else {
            
            //if non-mit person, get foreign information
            if (keyPerson.getNonMITPersonFlag()){ 
                hmInfo = nasaSeniorKeyPersonSmtDtShtTxnBean.getRolodexInfo(propNumber,keyPerson.getPersonId()); 
                if (hmInfo != null && hmInfo.get("FOREIGN") != null) {
                    foreign = hmInfo.get("FOREIGN").toString();
                    if( foreign.equals("YES") ){
                        seniorKeyPerson.setNASACoItype("Co-I/Co-PI (non-U.S.organization only)");  
                        seniorKeyPerson.setInternationalParticipation("Y: Yes");
                    } else
                        seniorKeyPerson.setNASACoItype("Co-I/Institutional PI");
                }
            } else
            //co i is mit person
            seniorKeyPerson.setNASACoItype("Co-I");                
        }
        
        /**********************************************
         * Employee of US government question
         * if not mit person, check sponsor code of rolodex. if federal sponsor, answer yes. otherwise,
         * including if no sponsor, answer no
         * *********************************************
         * Foreign organization question.
         *  if not mit person, check sponsor code of rolodex. if foreign sponsor, answer yes. otherwise,
         *  check country code of person for foreign country. if foreign country, answer yes. otherwise,
         *  including if no country code, answer no
        ***********************************************/
       
           
          if (hmInfo != null && hmInfo.get("USGOV") != null) {
            if (hmInfo.get("USGOV").toString().equals("NO")) {
               seniorKeyPerson.setUSGovernmentParticipation("N: No");
              
            }else
               seniorKeyPerson.setUSGovernmentParticipation("Y: Yes");
               //set U.S. Govermment agency
               if ( hmInfo.get("SPONSOR_CODE") != null){            
                //get Govermment agency's name
                hmInfo = nasaSeniorKeyPersonSmtDtShtTxnBean.getGovermmentagency(hmInfo.get("SPONSOR_CODE").toString());
                if (hmInfo != null && hmInfo.get("AGENCY_NAME") != null){
                    seniorKeyPerson.setFederalAgency(hmInfo.get("AGENCY_NAME").toString());
                }
            }
          }
           
            //set total dollar amount requested. so we don't have budget for rolodex person.
            //seniorKeyPerson.setFederalAgencyDollar("it is not available);
        
    
        //set attachments
        gov.grants.apply.system.attachments_v1.AttachmentGroupMin0Max100DataType attachmentFiles = null;
        attachmentFiles = getKeyPersonAttachments(keyPerson,  STATEMENTOFCOMMITMENT, STATEMENTOFCOMMITMENT_DOC);
   
        if (attachmentFiles != null)
            seniorKeyPerson.setStatementofCommitment(attachmentFiles);
        attachmentFiles = null;

        attachmentFiles = getKeyPersonAttachments(keyPerson,  BUDGETDETAILS, BUDGETDETAILS_DOC);
       
        if (attachmentFiles != null)
            seniorKeyPerson.setBudgetDetails(attachmentFiles);
        
        return seniorKeyPerson;
      }
      
    
//case 2635 start
//    private gov.grants.apply.system.attachments_v1.AttachmentGroupMin0Max100DataType getKeyPersonAttachments(KeyPersonBean keyPerson,String attachmentType)
//    throws CoeusException, DBException,JAXBException{
    private gov.grants.apply.system.attachments_v1.AttachmentGroupMin0Max100DataType getKeyPersonAttachments(KeyPersonBean keyPerson, String attachmentType, int attachmentCode)
    throws CoeusException, DBException,JAXBException{
//case 2635 end
        int bioNumber = 0;
        String personId = keyPerson.getPersonId();
        ProposalPersonBioPDFBean proposalPersonBioPDF=null;
        gov.grants.apply.system.attachments_v1.AttachmentGroupMin0Max100DataType attachmentGroup 
                = attachmentsObjFactory.createAttachmentGroupMin0Max100DataType();
        Attachment attachment = null;                             
        gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFile;
        
        Vector proposalPersons = propDevFormBean.getPropPersons();
//        
//        ProposalPersonFormBean proposalPersonFormBean = 
//            propDevTxnBean.getProposalPersonDetails(propNumber,keyPerson.getPersonId()); 
        for(int perCnt=0; perCnt<proposalPersons.size(); perCnt++){
           
           ProposalPersonFormBean proposalPersonFormBean =
                            (ProposalPersonFormBean)proposalPersons.get(perCnt);
           if(proposalPersonFormBean.getPersonId().equals(personId)){
            Vector propBiographies = proposalPersonFormBean.getPropBiography();
            if(propBiographies != null){
                for(int bioCnt = 0; bioCnt<propBiographies.size(); bioCnt++){
                    ProposalBiographyFormBean propBiographyFormBean = (ProposalBiographyFormBean)propBiographies.get(bioCnt);
                    //case 2635 start
//                    if(propBiographyFormBean.getDescription().trim().equalsIgnoreCase(attachmentType)){ 
                        if(propBiographyFormBean.getDocumentTypeCode()== attachmentCode ){ 
                    //case 2635 end
                        bioNumber = propBiographyFormBean.getBioNumber();
                        proposalPersonBioPDF = new ProposalPersonBioPDFBean();
                        proposalPersonBioPDF.setPersonId(personId);
                        proposalPersonBioPDF.setProposalNumber(propNumber);
                        proposalPersonBioPDF.setBioNumber(bioNumber);
                        proposalPersonBioPDF = proposalPersonTxnBean.getProposalPersonBioPDF(proposalPersonBioPDF);
                        attachment = getKeyPersonPDF(proposalPersonBioPDF, attachmentType);
                        if(attachment != null){
                            //holds statement of commitment attachments for this key person
                            attachedFile = getAttachedFileType(attachment);                        
                            attachmentGroup.getAttachedFile().add(attachedFile);
                        }
                    }
                }//end if(propBiographies != null)
            }//if
        }//for
        }
        return attachmentGroup;
    }
       
    private Attachment getKeyPersonPDF(ProposalPersonBioPDFBean  proposalPersonBioPDF, String attachmentType)
            throws CoeusException, DBException{

        if(proposalPersonBioPDF==null)
            return null;
        //Create hashMap to be used to create unique content id for this attachment
        
        LinkedHashMap hmArg = new LinkedHashMap();
        hmArg.put(ContentIdConstants.PERSON_ID, proposalPersonBioPDF.getPersonId());
        hmArg.put(ContentIdConstants.BIO_NUMBER, String.valueOf(proposalPersonBioPDF.getBioNumber()));
        hmArg.put(ContentIdConstants.DESCRIPTION, attachmentType);
        //check if this attachment has already been added
        Attachment attachment = getAttachment(hmArg);
        if(attachment == null){
            attachment = new Attachment();
            //if no pdf found for requested type return null.
            if(proposalPersonBioPDF.getBioNumber() == 0){
                return null;
            }
            //If there is module name, but no file uploaded
            if(proposalPersonBioPDF == null ||
                proposalPersonBioPDF.getFileBytes() == null){
                    System.out.println("no file found in database");
                return null;
            }
            String contentId = createContentId(hmArg);
            attachment.setContent( proposalPersonBioPDF.getFileBytes());
            String contentType = "application/octet-stream";
            attachment.setFileName(AttachedFileDataTypeStream.addExtension(contentId));
            attachment.setContentId(contentId);
            attachment.setContentType(contentType);
            addAttachment(hmArg, attachment);
        }
        return attachment;
    }
    
    private gov.grants.apply.system.attachments_v1.AttachedFileDataType
                                    getAttachedFileType(Attachment attachment)
         throws JAXBException {

        gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
        gov.grants.apply.system.attachments_v1.AttachedFileDataType.FileLocationType fileLocation;
        gov.grants.apply.system.global_v1.HashValueType hashValueType;

        attachedFileType = attachmentsObjFactory.createAttachedFileDataType();
        if(attachment == null){
            return attachedFileType;
        }
        fileLocation = attachmentsObjFactory.createAttachedFileDataTypeFileLocationType();
    //    hashValueType = globalObjFactoryV2.createHcreateHashValueType();

        fileLocation.setHref(attachment.getContentId());
        attachedFileType.setFileLocation(fileLocation);
        attachedFileType.setFileName(AttachedFileDataTypeStream.addExtension(attachment.getFileName()));
        attachedFileType.setMimeType("application/octet-stream");
        try{
            attachedFileType.setHashValue(S2SHashValue.getValue(attachment.getContent()));
        }catch(Exception ex){
            ex.printStackTrace();
            UtilFactory.log(ex.getMessage(),ex, "RRKeyPersonStream", "getAttachedFile");
            throw new JAXBException(ex);
        }

        return attachedFileType;

    }

     public Object getStream(java.util.HashMap ht) throws JAXBException, CoeusException, DBException {
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getNasaSeniorKeyPersonSupplementalDataSheet();
    }
}
