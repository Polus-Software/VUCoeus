/*
 * PrintCertificationStream.java
 *
 * Created on March 3, 2005, 3:50 PM
 */

package edu.mit.coeus.xml.generator;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.organization.bean.OrganizationAddressFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalYNQFormBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.utils.xml.bean.printCertification.*;
import java.io.IOException;
import java.util.*;
import javax.xml.bind.JAXBException;



/**
 *
 * @author  jenlu
 */
public class PrintCertificationStream  extends ReportBaseStream{
    private ObjectFactory objFactory;
    private CoeusXMLGenrator xmlGenerator;
    private Vector vePerson;
    //start case 2358
    private String proposalNum;
//    private String proposalTitle;
//    private String agencyName;
//    private String agencyId;
//    private String primeAgencyId;
    private ProposalDevelopmentFormBean proposalDevelopmentFormBean;
    //end case 2358
    private static final String packageName = "edu.mit.coeus.utils.xml.bean.printCertification";
    
    /** Creates a new instance of PrintCertificationStream */
    public PrintCertificationStream() {
        objFactory = new ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();
    }
    
    public org.w3c.dom.Document getStream(Vector veData) throws Exception {
        //start case 2358
//        this.proposalNum = (String)veData.get(0);
//        this.proposalTitle = (String)veData.get(1);
//        this.agencyName = (String)veData.get(2);
//        this.agencyId = (String)veData.get(3);
//        this.primeAgencyId = (String)veData.get(4);
//        vePerson = (Vector)veData.get(5);  
        proposalDevelopmentFormBean = (ProposalDevelopmentFormBean)veData.get(0);
        this.proposalNum = proposalDevelopmentFormBean.getProposalNumber();
        vePerson = (Vector)veData.get(1);
        //end case 2358
        PrintCertificationType printCertificationType = getPrintCertification();       
         return xmlGenerator.marshelObject(printCertificationType,packageName);
    }
    
    public org.w3c.dom.Document getStream(java.util.Hashtable params) throws DBException,CoeusException {
        PrintCertificationType printCertificationType = (PrintCertificationType)params.get("PrintCertificationType");
        return xmlGenerator.marshelObject(printCertificationType,packageName);
    }
    
    public Object getObjectStream(Hashtable params) throws DBException,CoeusException{
        Vector veData = (Vector)params.get("DATA");
        proposalDevelopmentFormBean = (ProposalDevelopmentFormBean)veData.get(0);
        this.proposalNum = proposalDevelopmentFormBean.getProposalNumber();
        
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        PrintCertificationType printCertificationType;
        String personName;
        Vector vecPersonData = (Vector)veData.get(1);
        if(vecPersonData != null) {
            for(int index = 0; index < vecPersonData.size(); index++) {
                vePerson = (Vector)vecPersonData.get(index);
                personName = (String)vePerson.get(1);
                printCertificationType = getPrintCertification();
                linkedHashMap.put(personName, printCertificationType);
            }
        }
        return linkedHashMap;
        
        //vePerson = (Vector)veData.get(1);
        //PrintCertificationType printCertificationType = getPrintCertification(); 
        //return printCertificationType;
    }
    
     private PrintCertificationType getPrintCertification() throws CoeusXMLException,CoeusException,DBException{
         PrintCertificationType printCertificationType = null;
         try {
            printCertificationType = objFactory.createPrintCertification();
            printCertificationType.setProposalNumber(proposalNum);
            //start case 2358
//            printCertificationType.setProposalTitle(proposalTitle);
            printCertificationType.setProposalTitle(proposalDevelopmentFormBean.getTitle());
            //end case 2358
            Investigator investigator = objFactory.createInvestigator();
            investigator.setPersonID((String)vePerson.get(0));
            investigator.setPersonName((String)vePerson.get(1));
            investigator.setPrincipalInvFlag(((String)vePerson.get(2)).equalsIgnoreCase("true")? true:false);
            printCertificationType.setInvestigator(investigator);
           
            Sponsor sponsor = objFactory.createSponsor(); 
            //start case 2358
//            sponsor.setSponsorName(agencyName);
            sponsor.setSponsorName(proposalDevelopmentFormBean.getSponsorName());
            //end case 2358
            // change for 5/2006: the certification will be dependent on how questions are answered in COUES.
            // we need to know sponsor/primeSponsor is federal or non-federal
            
            ProposalDevelopmentTxnBean propTxnBean = new ProposalDevelopmentTxnBean();
            
            int sponsorType = propTxnBean.checkAgencyType(proposalDevelopmentFormBean.getSponsorCode(),proposalDevelopmentFormBean.getPrimeSponsorCode());
            if (sponsorType == 0)sponsor.setSponsorType("FED");
            else sponsor.setSponsorType("NONFED");
            printCertificationType.setSponsor(sponsor);
            
            printCertificationType.setSchoolInfo(getSchoolInfoType());
            
//            ProposalDevelopmentTxnBean propTxnBean = new ProposalDevelopmentTxnBean();
//            Vector veCertifications = propTxnBean.getCertifications(agencyId,primeAgencyId );
//            Vector veCertificationsXmlData = new Vector();
//            int certiSize = veCertifications == null?0:veCertifications.size();            
//            for(int index = 0; index < certiSize; index++){
//                Certification certification = objFactory.createCertification();
//                certification.setStmtNumber(index + 1);
//                certification.setStatement((String)veCertifications.get(index));
//                veCertificationsXmlData.add(certification);                
//            }
            
            ProposalYNQFormBean proposalYNQFormBean;
            Vector veCertifications = propTxnBean.getCertifyAnswers(proposalNum,investigator.getPersonID());
            Vector veCertificationsXmlData = new Vector();
            int certiSize = veCertifications == null?0:veCertifications.size();            
            for(int index = 0; index < certiSize; index++){
                Certification certification = objFactory.createCertification();
                proposalYNQFormBean = new ProposalYNQFormBean();
                proposalYNQFormBean = (ProposalYNQFormBean)veCertifications.get(index);
                //start case 2358
//                if (proposalYNQFormBean != null 
//                     && proposalYNQFormBean.getQuestionId() != null 
//                         && proposalYNQFormBean.getQuestionId().startsWith("P")){
                
                 if (proposalYNQFormBean != null 
                     && proposalYNQFormBean.getQuestionId() != null){
                //end case 2358
                   
                    certification.setQuestionID(proposalYNQFormBean.getQuestionId());
                    certification.setAnswer(proposalYNQFormBean.getAnswer());
                    //case coeusdev-272 start
                    if (proposalYNQFormBean.getQuestionId().equals("P3")
                            && proposalYNQFormBean.getAnswer().equals("N")
                             && (proposalDevelopmentFormBean.getProposalActivityTypeCode()== 3 || proposalDevelopmentFormBean.getProposalActivityTypeCode()== 7))
                        //set answer as fellowship when P3 with answer N and activitype is 3 or 7
                        certification.setAnswer("F");
                    //case coeusdev-272 end
                    veCertificationsXmlData.add(certification);  
                    
                }                     
            }
			// Added to set the current date property
			// Last update in the person YNQ will be set to currentdate, if no data exists current date will be set
//            if(veCertifications != null && !veCertifications.isEmpty()){
//                Calendar currentDate = Calendar.getInstance();
//                ProposalYNQFormBean ynqFormDetails = (ProposalYNQFormBean)veCertifications.get(0);
//                currentDate.setTime(ynqFormDetails.getUpdateTimeStamp());
//                printCertificationType.setCurrentDate(currentDate);
//            }else{
//                Calendar currentDate = Calendar.getInstance();
//                currentDate.setTime(new Date());
//                printCertificationType.setCurrentDate(currentDate);
//            }
            if(veCertifications != null && !veCertifications.isEmpty()){
                Calendar lastUpdateTimeStamp = Calendar.getInstance();
                ProposalYNQFormBean ynqFormDetails = (ProposalYNQFormBean)veCertifications.get(0);
                lastUpdateTimeStamp.setTime(ynqFormDetails.getUpdateTimeStamp());
                printCertificationType.setLastUpdateTimeStamp(lastUpdateTimeStamp);
                printCertificationType.setLastUpdateUser(ynqFormDetails.getUpdateUser());
            } 

            //case print old certification start
            // get old certification, if there is one.
            veCertifications = propTxnBean.getCertifications(proposalDevelopmentFormBean.getSponsorCode(),proposalDevelopmentFormBean.getPrimeSponsorCode());
            certiSize = veCertifications == null?0:veCertifications.size();  
            for(int index = 0; index < certiSize; index++){
                Certification certification = objFactory.createCertification();
                certification.setStmtNumber(index + 1);
                certification.setStatement((String)veCertifications.get(index));
                veCertificationsXmlData.add(certification);                
            }
            //case print old certification end 01/04/07
            printCertificationType.getCertifications().addAll(veCertificationsXmlData);
            
            //get mit logo path
            String absPathImage = getClass().getClassLoader().getResource("/edu/mit/coeus/xml/images").getPath();
            //start case 2358
//            printCertificationType.setOtherInfo(absPathImage);
            printCertificationType.setLogoPath(absPathImage);
            
            HashMap hmInfo = new HashMap();
            hmInfo = propTxnBean.getPropLeadUnitOSPAdminHomeUnit(proposalNum);
            if (hmInfo != null &&  hmInfo.get("UNIT_NAME") != null){
               printCertificationType.setOfficeName(hmInfo.get("UNIT_NAME").toString());            
            }
//            
            printCertificationType.setOrganizationInfo(getOrganization());
            //end case 2358

            Calendar currentDate = Calendar.getInstance();
            currentDate.setTime(new Date());
            printCertificationType.setCurrentDate(currentDate);

         }catch (JAXBException jaxbEx){
            UtilFactory.log(jaxbEx.getMessage(),jaxbEx,"PrintCertificationStream","getStream()");
            throw new CoeusXMLException(jaxbEx.getMessage());
       
        }
        return  printCertificationType;
     }
     //start case 2358
      private OrganizationType getOrganization() throws JAXBException,
                                        CoeusException,DBException{
        try{
            OrganizationType organizationType = objFactory.createOrganizationType();
            OrganizationAddressFormBean organizationAddressFormBean = proposalDevelopmentFormBean.getOrganizationAddressFormBean();
            RolodexDetailsBean rolodexDetailsBean = proposalDevelopmentFormBean.getRolodexDetailsBean();
            if (organizationAddressFormBean != null ) {
                organizationType.setOrganizationID(proposalDevelopmentFormBean.getOrganizationId());
                organizationType.setOrganizationName(organizationAddressFormBean.getOrganizationName());
                
            }
            if (rolodexDetailsBean != null ) {
                String contactName, temp;
                //keep this code to show contact person name.
                //set contact with person name and organiztion
//                String firstName = edu.mit.coeus.utils.Utils.convertNull(
//                                    rolodexDetailsBean.getFirstName());
//                String middleName = edu.mit.coeus.utils.Utils.convertNull(
//                                        rolodexDetailsBean.getMiddleName());
//                String lastName = edu.mit.coeus.utils.Utils.convertNull(
//                                        rolodexDetailsBean.getLastName());
//                String prefix = edu.mit.coeus.utils.Utils.convertNull(
//                                        rolodexDetailsBean.getPrefix());
//                String suffix = edu.mit.coeus.utils.Utils.convertNull(
//                                        rolodexDetailsBean.getSuffix());
//                if (lastName.length() > 0) {
//                    contactName = (lastName + " "+ suffix +", "+ 
//                        prefix + " "+ firstName + " "+ middleName).trim();
//                } else {
//                    contactName = edu.mit.coeus.utils.Utils.convertNull(
//                                rolodexDetailsBean.getOrganization());
//                }
//                temp = rolodexDetailsBean.getOrganization();
//                if (temp != null && temp.length() > 0) {
//                    if (!temp.equals(contactName)) {
//                        contactName = contactName + "\n"+ temp ;
//                    } else {
//                        contactName = temp ;
//                    }
//                }
                
                //or set contact only with organiztion
                contactName = rolodexDetailsBean.getOrganization();
                
                if (contactName != null && contactName.length() > 0 
                    && !contactName.equalsIgnoreCase(organizationAddressFormBean.getOrganizationName()))
                    organizationType.setContactName(contactName);
                            
                //set address
                temp = rolodexDetailsBean.getAddress1();
                if (temp != null && temp.length() > 0) 
                    organizationType.setAddress1(temp);
                
                temp = rolodexDetailsBean.getAddress2();
                if (temp != null && temp.length() > 0) 
                    organizationType.setAddress2(temp);
                
                temp = rolodexDetailsBean.getAddress3();
                if (temp != null && temp.length() > 0) 
                    organizationType.setAddress3(temp);
                
                temp = rolodexDetailsBean.getCity();
                if (temp != null && temp.length() > 0) 
                    organizationType.setCity(temp);
                
                temp = rolodexDetailsBean.getState();
                if (temp != null && temp.length() > 0){
                    Vector states = rolodexDetailsBean.getStates();
                    ComboBoxBean stateBean;
                    for(int loopIndex = 0; loopIndex < states.size(); loopIndex++) {
                        stateBean = (ComboBoxBean) states.elementAt(loopIndex);
                        if (stateBean.getCode().equals(temp)) {
                            temp = stateBean.getDescription();
                        }
                    }
                    organizationType.setState(temp);
                }
                
                temp = rolodexDetailsBean.getPostalCode();
                if (temp != null && temp.length() > 0) 
                    organizationType.setPostCode(temp);
                
                temp = rolodexDetailsBean.getCountry();
                if (temp != null && temp.length() > 0) {
                    Vector countries = rolodexDetailsBean.getCountries();
                    ComboBoxBean countryBean;
                    for(int loopIndex = 0 ; loopIndex < countries.size() ; loopIndex++ ){
                        countryBean = (ComboBoxBean) countries.elementAt(loopIndex);
                        if (countryBean.getCode().equals(temp)) {
                            temp = countryBean.getDescription();
                        }
                    }
                    organizationType.setCountry(temp);
                }
            }
            return organizationType;
        }catch (Exception ex) {
            UtilFactory.log(ex.getMessage(),ex,"PrintCertificationStream","getOrganization()");
            throw new CoeusException(ex.getMessage());
        }
    }//end case 2358
      
      private SchoolInfoType getSchoolInfoType() throws JAXBException,
                                        CoeusException,DBException{
        try{
        SchoolInfoType schoolInfoType = objFactory.createSchoolInfoType();
        String schoolName = CoeusProperties.getProperty(CoeusPropertyKeys.SCHOOL_NAME);
        String schoolAcronym = CoeusProperties.getProperty(CoeusPropertyKeys.SCHOOL_ACRONYM);
        // SchoolInfoType
        schoolInfoType.setSchoolName(schoolName);
        schoolInfoType.setAcronym(schoolAcronym);
        return schoolInfoType;
        }catch (Exception ex) {
            UtilFactory.log(ex.getMessage(),ex,"PrintCertificationStream","getSchoolInfoType()");
            throw new CoeusException(ex.getMessage());
        }
    }
}
