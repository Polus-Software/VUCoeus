/*
 * @(#)RRKeyPersonExpandedStream_V1_2.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.generator;

import com.lowagie.text.DocumentException;
import edu.mit.coeus.s2s.generator.stream.*;

import edu.mit.coeus.organization.bean.OrganizationMaintenanceDataTxnBean;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceFormBean;
import edu.mit.coeus.organization.bean.OrganizationAddressFormBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexMaintenanceDataTxnBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalPersonBioPDFBean;
import edu.mit.coeus.propdev.bean.ProposalPersonTxnBean;
import edu.mit.coeus.propdev.bean.ProposalPersonFormBean;
import edu.mit.coeus.propdev.bean.ProposalBiographyFormBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeFormBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
//coeusqa-2363 start
import edu.mit.coeus.propdev.bean.ProposalPerDegreeFormBean;
import edu.mit.coeus.utils.S2SConstants;
import java.util.Date;
//coeusqa-2363 end
import edu.mit.coeus.s2s.bean.*;
import edu.mit.coeus.s2s.Attachment;
//import edu.mit.coeus.s2s.generator.ContentIdConstants;
import edu.mit.coeus.s2s.generator.stream.bean.ExAttQueryParams;
import edu.mit.coeus.s2s.util.S2SHashValue;

import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import gov.grants.apply.coeus.personprofile.PersonProfileListType;
import gov.grants.apply.forms.rr_keypersonexpanded_1_2_v1_2.*;
import gov.grants.apply.system.globallibrary_v2.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import javax.xml.bind.JAXBException;
import org.apache.fop.apps.FOPException;
import org.w3c.dom.Document;

/**
 * @author  Eleanor Shavell
 * Class for generating the object stream for grants.gov RRKeyPersonexpandedStream_V1_2. It uses jaxb classes
 * which have been created under gov.grants.apply package. Fetch the data
 * from database and attach with the jaxb beans which have been derived from
 * RRKeyPersonexpandedStreamV1_2 schema.
 */

 public class RRKeyPersonExpandedStream_V1_2 extends S2SBaseStream{

    private gov.grants.apply.forms.rr_keypersonexpanded_1_2_v1_2.ObjectFactory objFactory;
    private gov.grants.apply.system.globallibrary_v2.ObjectFactory globalObjFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;
    private gov.grants.apply.coeus.personprofile.ObjectFactory extraPersProfObjFact;

    private CoeusXMLGenrator xmlGenerator;

    //txn beans
    private S2STxnBean s2sTxnBean;
    private OrganizationMaintenanceDataTxnBean orgMaintDataTxnBean;
    private ProposalDevelopmentTxnBean propDevTxnBean;
    private ProposalPersonTxnBean proposalPersonTxnBean;

    //data beans
    private ProposalDevelopmentFormBean propDevFormBean;
    private OrganizationMaintenanceFormBean orgMaintFormBean;

    private HashMap attachmentMap;
    private Hashtable propData;
    private String propNumber;
    private KeyPersonBean PI;
    private CoeusVector keyPersons;
    private CoeusVector extraKeyPersons;

    private Vector extraAttachments;

    private static final String PDPI = "PD/PI";
    private static final String COPI = "Co-PD/PI";
    private static final String OTHER = "Other (Specify)";
    public static final String BIOSKETCH = "BIOSKETCH";
    public static final String CURRENTPENDING = "CURRENTPENDING";

    public static final int BIOSKETCH_DOC = 1;
    public static final int CURRENTPENDING_DOC = 2;

    public static final String PROFILE = "PROFILE";

    public static final int BIOSKETCH_TYPE = 16;
    public static final int CURRENTPENDING_TYPE = 17;
    public static final int PROFILE_TYPE = 18;

    private String organizationID;
    private String perfOrganizationID;


    /** Creates a new instance of RR_KeyPersonExpandedStream_V1_2 */
    public RRKeyPersonExpandedStream_V1_2(){

        objFactory = new gov.grants.apply.forms.rr_keypersonexpanded_1_2_v1_2.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.globallibrary_v2.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();

        s2sTxnBean = new S2STxnBean();
        propDevTxnBean = new ProposalDevelopmentTxnBean();
        orgMaintDataTxnBean = new OrganizationMaintenanceDataTxnBean();
        proposalPersonTxnBean = new ProposalPersonTxnBean();

        extraAttachments = new Vector();
    }

    private RRKeyPersonExpanded12Type getRRKeyPerson() throws CoeusXMLException,
                                                CoeusException,DBException{

        RRKeyPersonExpanded12Type rrKeyPerson = null;

        try{
           //get proposal master info
           propDevFormBean = getPropDevData();
           //get applicant organization info
            orgMaintFormBean  = getOrgData();

           //get existing attachment list
           attachmentMap = getAttachmentMap();
           rrKeyPerson = objFactory.createRRKeyPersonExpanded12();
           //Populate PI, keyPersons and extraKeyPersons
           populatePIAndKeyPersons();
           if(PI != null){
               System.out.println("setPDPI");
               rrKeyPerson.setPDPI(getKeyPerson(PI,false));
           }
           //Holds all key persons except PI.
           java.util.List keyPersonList = rrKeyPerson.getKeyPerson();

           if(keyPersons != null){
               for(int keyPersCnt = 1; keyPersCnt < keyPersons.size(); keyPersCnt++){
                   KeyPersonBean keyPersonBean =
                                (KeyPersonBean)keyPersons.get(keyPersCnt);
                   PersonProfileDataType personProfile = getKeyPerson(keyPersonBean,false);
                   keyPersonList.add(personProfile);
               }
           }
           //For case of more than 40 key persons.  Attach one pdf with all
           genExKeyPersonAttachments();
           //additional biosketchs.
           Attachment otherBioSketchs = getOtherPDF(BIOSKETCH_TYPE);
           if(otherBioSketchs != null){
               gov.grants.apply.forms.rr_keypersonexpanded_1_2_v1_2.RRKeyPersonExpanded12Type.BioSketchsAttachedType
                            otherBioSketchsAttached =
                            objFactory.createRRKeyPersonExpanded12TypeBioSketchsAttachedType();

               gov.grants.apply.system.attachments_v1.AttachedFileDataType
                                attachedFileType = getAttachedFileType(otherBioSketchs);
               otherBioSketchsAttached.setBioSketchAttached(attachedFileType);
               rrKeyPerson.setBioSketchsAttached(otherBioSketchsAttached);
           }
           //For case of more than 40 key persons.  Attach one pdf with all
           //additional current pending reports.
           Attachment otherCurrentPending = getOtherPDF(CURRENTPENDING_TYPE);
           if(otherCurrentPending != null){
               gov.grants.apply.forms.rr_keypersonexpanded_1_2_v1_2.RRKeyPersonExpanded12Type.SupportsAttachedType
                            otherSupportsAttached =
                            objFactory.createRRKeyPersonExpanded12TypeSupportsAttachedType();
               gov.grants.apply.system.attachments_v1.AttachedFileDataType
                                attachedFileType = getAttachedFileType(otherCurrentPending);
               otherSupportsAttached.setSupportAttached(attachedFileType);
               rrKeyPerson.setSupportsAttached(otherSupportsAttached);
           }
           //For case of more than 40 key persons.  Attach one pdf with all
           //additional profile info.
           Attachment additionalProfile = getOtherPDF(PROFILE_TYPE);
           if(additionalProfile != null){
               gov.grants.apply.forms.rr_keypersonexpanded_1_2_v1_2.RRKeyPersonExpanded12Type.AdditionalProfilesAttachedType
                    additionalProfilesAttached =
                    objFactory.createRRKeyPersonExpanded12TypeAdditionalProfilesAttachedType();
               gov.grants.apply.system.attachments_v1.AttachedFileDataType
                                attachedFileType = getAttachedFileType(additionalProfile);
               additionalProfilesAttached.setAdditionalProfileAttached(attachedFileType);
               rrKeyPerson.setAdditionalProfilesAttached(additionalProfilesAttached);
           }

           //Set form version
           rrKeyPerson.setFormVersion("1.2");

         }catch (JAXBException jaxbEx){
            UtilFactory.log(jaxbEx.getMessage(),jaxbEx,"RRKeyPersonExpandedStream_V1_2",
                                                            "getRRKeyPerson()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }
        return rrKeyPerson;
    }
    private void genAttForExKeyPersons() throws CoeusException{
        //merge all biosketches to one pdf
        if (extraKeyPersons != null){
            int size = extraKeyPersons.size();
            byte[] pdfBytes = null;
            try{
                 //case 3348. ArrayList is dynamic, which avoids having null elements in the array
                ArrayList mergeBioList = new ArrayList();
                ArrayList bioBookmarksList = new ArrayList();
                ArrayList pendBookmarksList = new ArrayList();
                ArrayList mergeCurPendList = new ArrayList();

                for(int personCnt=0;personCnt<size;personCnt++){
                    KeyPersonBean keyPersonBean = (KeyPersonBean) extraKeyPersons.elementAt(personCnt);
                    ProposalPersonBioPDFBean proposalPersonBioPDF = new ProposalPersonBioPDFBean();

                    proposalPersonBioPDF = getProposalPersonBioPDF(keyPersonBean.getPersonId(), BIOSKETCH_DOC);

                    if(proposalPersonBioPDF!=null && proposalPersonBioPDF.getFileBytes()!=null && proposalPersonBioPDF.getFileBytes().length>0){
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        stream.write(proposalPersonBioPDF.getFileBytes());
                        mergeBioList.add(stream);

                        bioBookmarksList.add(keyPersonBean.getPersonId());
                    }

                    proposalPersonBioPDF = getProposalPersonBioPDF(keyPersonBean.getPersonId(), CURRENTPENDING_DOC);

                    if(proposalPersonBioPDF!=null && proposalPersonBioPDF.getFileBytes()!=null && proposalPersonBioPDF.getFileBytes().length>0){

                        ByteArrayOutputStream curPendStream = new ByteArrayOutputStream();
                        curPendStream.write(proposalPersonBioPDF.getFileBytes());
                        mergeCurPendList.add(curPendStream);

                        pendBookmarksList.add(keyPersonBean.getPersonId());
                    }

                }
                 ByteArrayOutputStream[] mergeBioArray = new ByteArrayOutputStream[bioBookmarksList.size()];
                ByteArrayOutputStream[] mergeCurPendArray = new ByteArrayOutputStream[pendBookmarksList.size()];
                String[] bioBookmarksArray = new String[bioBookmarksList.size()];
                String[] pendBookmarksArray = new String[pendBookmarksList.size()];

                 //case 3348. can not cast the array
                for(int i=0;i<bioBookmarksList.size();i++){
                    mergeBioArray[i] = (ByteArrayOutputStream)mergeBioList.get(i);
                    bioBookmarksArray[i] = (String)bioBookmarksList.get(i);
                }
                for(int i=0;i<pendBookmarksList.size();i++){
                    mergeCurPendArray[i] = (ByteArrayOutputStream)mergeCurPendList.get(i);
                    pendBookmarksArray[i] = (String)pendBookmarksList.get(i);
                }

                addExtraAttachment(xmlGenerator.mergePdfBytes(mergeBioArray, bioBookmarksArray),
                                        BIOSKETCH_TYPE,BIOSKETCH);

                addExtraAttachment(xmlGenerator.mergePdfBytes(mergeCurPendArray, pendBookmarksArray),
                                        CURRENTPENDING_TYPE,CURRENTPENDING);

            }catch(IOException ex){
                UtilFactory.log(ex.getMessage(),ex, "RRKeyPersonExpandedStreamV1_2", "genExKeyPersonAttachment");
                throw new CoeusException(ex.getMessage());
            }catch(DocumentException ex){
                UtilFactory.log(ex.getMessage(),ex, "RRKeyPersonExpandedStreamV1_2", "genExKeyPersonAttachment");
                throw new CoeusException(ex.getMessage());
            }catch(DBException ex){
                UtilFactory.log(ex.getMessage(),ex, "RRKeyPersonExpandedStreamV1_2", "genExKeyPersonAttachment");
                throw new CoeusException(ex.getMessage());
            }

        }
    }
    private void genExKeyPersonAttachments() throws CoeusException,DBException{
        if (extraKeyPersons != null){
            byte[] pdfBytes = null;
            try{
           extraPersProfObjFact = new gov.grants.apply.coeus.personprofile.ObjectFactory();
           PersonProfileListType extraPersonProfileList = extraPersProfObjFact.createPersonProfileList();

            extraPersonProfileList.setProposalNumber(propNumber);
           List extraPersonList = extraPersonProfileList.getExtraKeyPerson();

            for (int i=0; i<extraKeyPersons.size();i++){
                KeyPersonBean keyPersonBean = (KeyPersonBean) extraKeyPersons.elementAt(i);

                // get key person profile info
                //personProfile is type  gov.grants.apply.forms.rr_keypersonexpanded_v1_2.PersonProfileDataType
                PersonProfileDataType personProfile = getKeyPerson(keyPersonBean,true);
                PersonProfileDataType.ProfileType profType = objFactory.createPersonProfileDataTypeProfileType();
                profType = personProfile.getProfile();
                gov.grants.apply.forms.rr_keypersonexpanded_1_2_v1_2.PersonProfileDataType.ProfileType.OtherProjectRoleCategoryType otherRole;

                gov.grants.apply.system.globallibrary_v2.AddressDataType globalAddress;

                PersonProfileListType.ExtraKeyPersonType extraPerson = extraPersProfObjFact.createPersonProfileListTypeExtraKeyPersonType();
                PersonProfileListType.ExtraKeyPersonType.NameType nameType = extraPersProfObjFact.createPersonProfileListTypeExtraKeyPersonTypeNameType();
                PersonProfileListType.ExtraKeyPersonType.AddressType addressType = extraPersProfObjFact.createPersonProfileListTypeExtraKeyPersonTypeAddressType();

                if (keyPersonBean.getFirstName() != null)  nameType.setFirstName(keyPersonBean.getFirstName());
                if (keyPersonBean.getMiddleName() != null) nameType.setMiddleName(keyPersonBean.getMiddleName());
                if (keyPersonBean.getLastName() != null) nameType.setLastName(keyPersonBean.getLastName());


                extraPerson.setName(nameType);

                globalAddress = profType.getAddress();
                KeyPersonExpV11TxnBean keyPersonExpV11TxnBean = new KeyPersonExpV11TxnBean();

                HashMap hmCountry;
                HashMap hmState;

                if (globalAddress.getStreet1() != null) {
                    if (globalAddress.getStreet1().length() > 55)
                        addressType.setStreet1(globalAddress.getStreet1().substring(0,55));
                    else
                        addressType.setStreet1(globalAddress.getStreet1());

                }

                if (globalAddress.getStreet2() != null) {
                     if (globalAddress.getStreet2().length() > 55)
                         addressType.setStreet2(globalAddress.getStreet2().substring(0,55));
                     else
                         addressType.setStreet2(globalAddress.getStreet2());
                }
                if (globalAddress.getZipPostalCode() != null)  addressType.setZipCode(globalAddress.getZipPostalCode());


                if (globalAddress.getCountry() != null) {
                   hmCountry = keyPersonExpV11TxnBean.getCountryName(globalAddress.getCountry());
                   if (hmCountry.get("COUNTRY_NAME") != null)
                     addressType.setCountry(hmCountry.get("COUNTRY_NAME").toString());
                }

                if (globalAddress.getState() != null) {
                   hmState = keyPersonExpV11TxnBean.getStateName(globalAddress.getState());
                   if (hmState.get("STATE_NAME") != null )
                     addressType.setState(hmState.get("STATE_NAME").toString());
                  }

                if (globalAddress.getCounty() != null) addressType.setCounty(globalAddress.getCounty());
                if (globalAddress.getCity() != null)  addressType.setCity(globalAddress.getCity());

                //case 4249
                if (globalAddress.getProvince() != null) addressType.setCounty(globalAddress.getProvince());
                extraPerson.setAddress(addressType);

                if (profType.getTitle() != null)
                      extraPerson.setTitle(profType.getTitle());
                if (profType.getProjectRole() != null)
                      extraPerson.setProjectRole(profType.getProjectRole());
                if (profType.getCredential() != null)
                       extraPerson.setCredential(profType.getCredential());
                if (profType.getDepartmentName() != null)
                    extraPerson.setDepartmentName(profType.getDepartmentName());
                if (profType.getDivisionName() != null)
                    extraPerson.setDivisionName(profType.getDivisionName());
                if (profType.getEmail() != null)
                    extraPerson.setEmail(profType.getEmail());
                if (profType.getFax() != null)
                    extraPerson.setFax(profType.getFax());
                if (profType.getOrganizationName() != null)
                    extraPerson.setOrganizationName(profType.getOrganizationName());
                if (profType.getPhone() != null)
                    extraPerson.setPhone(profType.getPhone());

                 //degree type and year added for version 1.2 - case 4337
                if (profType.getDegreeType() != null)
                    extraPerson.setDegreeType(profType.getDegreeType());
                if (profType.getDegreeYear() != null)
                    extraPerson.setDegreeYear(profType.getDegreeYear());


                otherRole = profType.getOtherProjectRoleCategory();
                extraPerson.setOtherProjectRoleCategory(otherRole==null?"Unknown":
                            UtilFactory.setNullToUnknown(otherRole.getValue()));

                //need to add code to validate if bio and supports are attached
                extraPerson.setBioSketchAttached("Yes");
                extraPerson.setSupportsAttached("Yes");

                extraPersonList.add(extraPerson);

            }

           //marshell it to XML doc
            CoeusXMLGenrator xmlGen = new CoeusXMLGenrator();
            Document extraKeyPerDoc = xmlGen.marshelObject( extraPersonProfileList,
            "gov.grants.apply.coeus.personprofile");

                InputStream templateIS = getClass().getResourceAsStream("/edu/mit/coeus/s2s/template/additionalkeypersonprofiles.xsl");
                byte[] tmplBytes = new byte[templateIS.available()];
                templateIS.read(tmplBytes);
                pdfBytes = xmlGen.generatePdfBytes(extraKeyPerDoc,tmplBytes,null,
                        this.propDevFormBean.getProposalNumber()+"_"+"ExKeyPerson");
                addExtraAttachment(pdfBytes,PROFILE_TYPE,PROFILE);
            }catch(JAXBException ex){
                UtilFactory.log(ex.getMessage(),ex, "RRKeyPersonStreamV12", "genExKeyPersonAttachment");
                throw new CoeusException(ex.getMessage());
            }catch(FOPException ex){
                UtilFactory.log(ex.getMessage(),ex, "RRKeyPersonStreamV12", "genExKeyPersonAttachment");
                throw new CoeusException(ex.getMessage());
            }catch(IOException ex){
                UtilFactory.log(ex.getMessage(),ex, "RRKeyPersonStreamV12", "genExKeyPersonAttachment");
                throw new CoeusException(ex.getMessage());
            }catch(CoeusXMLException ex){
                UtilFactory.log(ex.getMessage(),ex, "KeyPersonStreamV12", "genExKeyPersonAttachment");
                throw new CoeusException(ex.getMessage());
            }

            genAttForExKeyPersons();
            s2sTxnBean.insertAutoGenNarrativeDetails(extraAttachments);
        }
    }

    private void addExtraAttachment(byte[] pdfBytes,int narrTypeCode,String title){
        if(pdfBytes==null)
            return;
        ProposalNarrativeFormBean propNarrBean = new ProposalNarrativeFormBean();
        propNarrBean.setProposalNumber(propDevFormBean.getProposalNumber());
        propNarrBean.setModuleTitle(title);
        propNarrBean.setComments("Auto generated document for "+title);
        propNarrBean.setModuleStatusCode('C');
        propNarrBean.setNarrativeTypeCode(narrTypeCode);
        ProposalNarrativePDFSourceBean propNarrPDFBean = new ProposalNarrativePDFSourceBean();
        propNarrPDFBean.setProposalNumber(propDevFormBean.getProposalNumber());
        propNarrPDFBean.setAcType("I");
        propNarrPDFBean.setFileBytes(pdfBytes);
        propNarrPDFBean.setFileName(AttachedFileDataTypeStream.addExtension(propDevFormBean.getProposalNumber()+"_"+title));
        extraAttachments.add(new ExAttQueryParams(propNarrBean, propNarrPDFBean));

    }
    private ProposalDevelopmentFormBean getPropDevData() throws DBException,CoeusException{
        if(propNumber==null)
            throw new CoeusXMLException("Proposal Number is Null");
        return propDevTxnBean.getProposalDevelopmentDetails(propNumber);
    }


     private OrganizationMaintenanceFormBean getOrgData()
            throws CoeusXMLException, CoeusException,DBException{
        HashMap hmOrg = s2sTxnBean.getOrganizationID(propNumber,"P");
        if (hmOrg!= null && hmOrg.get("ORGANIZATION_ID") != null){
               organizationID = hmOrg.get("ORGANIZATION_ID").toString();
        }
        return orgMaintDataTxnBean.getOrganizationMaintenanceDetails(organizationID);
    }


    private void populatePIAndKeyPersons() throws CoeusException, DBException{
        ArrayList allKeyPersons = s2sTxnBean.getInvestAndKeyPersons(propNumber,40);
//      allKeyPersons contains two vectors- 1st vector is the first 40 key person
//      2nd vector is the remaining people - all are sorted by sort_id (done in stored procedure)
        if(allKeyPersons != null && !allKeyPersons.isEmpty()){
            keyPersons = (CoeusVector)allKeyPersons.get(0);
            System.out.println("keyPersons.size(): "+keyPersons.size());
            if(keyPersons != null && !keyPersons.isEmpty()){
                PI = (KeyPersonBean)keyPersons.get(0);

            }
            extraKeyPersons = (CoeusVector)allKeyPersons.get(1);
        }
    }

    private PersonProfileDataType getKeyPerson(KeyPersonBean keyPersonBean,boolean extraKeyPersonFlag)
        throws JAXBException, CoeusException, DBException{
        PersonProfileDataType keyPerson = objFactory.createPersonProfileDataType();
        PersonProfileDataType.ProfileType profile =
                        objFactory.createPersonProfileDataTypeProfileType();
        HumanNameDataType humanNameDataType =
                        globalObjFactory.createHumanNameDataType();
        humanNameDataType.setFirstName(keyPersonBean.getFirstName());
        humanNameDataType.setMiddleName(keyPersonBean.getMiddleName());
        humanNameDataType.setLastName(keyPersonBean.getLastName());
        profile.setName(humanNameDataType);
        ProposalPersonFormBean proposalPersonForm = null;
        Vector allPersonDetails = proposalPersonTxnBean.getAllPersonDetails(propNumber);
        for(int personCnt=0; personCnt<allPersonDetails.size(); personCnt++){
            proposalPersonForm =
            (ProposalPersonFormBean)allPersonDetails.get(personCnt);
            if(proposalPersonForm.getPersonId().equals(keyPersonBean.getPersonId())){
                System.out.println("found keyPersonBean.getPersonId(): "+keyPersonBean.getPersonId());
                break;
            }
        }

        if (proposalPersonForm.getEraCommonsUsrName() != null)
            profile.setCredential(proposalPersonForm.getEraCommonsUsrName());
        if (proposalPersonForm.getOfficePhone() != null)
            profile.setPhone(proposalPersonForm.getOfficePhone());
        if (proposalPersonForm.getEmailAddress() != null)
            profile.setEmail(proposalPersonForm.getEmailAddress());
        if (proposalPersonForm.getFaxNumber() != null)
             profile.setFax(proposalPersonForm.getFaxNumber());
        //coeusqa-2363 start
        setDegreeInfo(profile,keyPersonBean.getPersonId());
//        //case 4337
//        if (proposalPersonForm.getDegree() != null)
//            profile.setDegreeType(proposalPersonForm.getDegree());
//        if (proposalPersonForm.getYearGraduated() != null)
//            profile.setDegreeYear(proposalPersonForm.getYearGraduated());
//        //end case 4337
        //coeusqa-2363 end
        String departmentName = proposalPersonForm.getUnitName();
        if(departmentName != null && departmentName.length()>30){
            departmentName = departmentName.substring(0,29);
        }
        if (departmentName != null) profile.setDepartmentName(departmentName);
        String homeUnit = proposalPersonForm.getHomeUnit();
        if(homeUnit == null){
            homeUnit = propDevFormBean.getOwnedBy();
        }
        //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - Start
        //String divisionName = s2sTxnBean.fn_get_division(homeUnit);
        String divisionName = s2sTxnBean.getPropPersonDivision(proposalPersonForm.getProposalNumber()
                        , proposalPersonForm.getPersonId());
        if(divisionName == null){
            divisionName = s2sTxnBean.fn_get_division(homeUnit);
        }
        //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - End
        if(divisionName != null && divisionName.length()>29){
            divisionName = divisionName.substring(0,29);
        }
        profile.setDivisionName(divisionName);
        System.out.println("nonMITPersonFlag: "+keyPersonBean.getNonMITPersonFlag());
        if(keyPersonBean.getNonMITPersonFlag()){
            RolodexMaintenanceDataTxnBean rolodexTxn =
                                    new RolodexMaintenanceDataTxnBean();
            RolodexDetailsBean rolodexDetails =
                rolodexTxn.getRolodexMaintenanceDetails(keyPersonBean.getPersonId());
            profile.setOrganizationName(rolodexDetails.getOrganization());
        }
        else{
            OrganizationAddressFormBean orgAddressFormBean =
            propDevFormBean.getOrganizationAddressFormBean();
            profile.setOrganizationName(orgAddressFormBean.getOrganizationName());
        }
        System.out.println("orgName: "+profile.getOrganizationName());

        AddressDataType address = globalObjFactory.createAddressDataType();
        HashMap hmState;
        HashMap hmCountry;
        KeyPersonExpV11TxnBean keyPersonExpV11TxnBean = new KeyPersonExpV11TxnBean();
//          KeyPersonExpV12TxnBean keyPersonExpV12TxnBean = new KeyPersonExpV12TxnBean();



        if (proposalPersonForm.getAddress1() != null){
              if (proposalPersonForm.getAddress1().length() > 55)
                  address.setStreet1(proposalPersonForm.getAddress1().substring(0,55));
              else
                  address.setStreet1(proposalPersonForm.getAddress1());
        }

        if (proposalPersonForm.getAddress2() != null) {
           if (proposalPersonForm.getAddress2().length() > 55)
                  address.setStreet2(proposalPersonForm.getAddress2().substring(0,55));
           else
                  address.setStreet2(proposalPersonForm.getAddress2());
        }

        if (proposalPersonForm.getCity() != null)
          address.setCity(proposalPersonForm.getCity());
        if (proposalPersonForm.getCounty() != null)
          address.setCounty(proposalPersonForm.getCounty());

        //case 4249 - if country is not US, then use
        //state field to populate province.
        String stateProvince = null;
        if (proposalPersonForm.getState() != null) {
              hmState = keyPersonExpV11TxnBean.getStateName(proposalPersonForm.getState());
              if (hmState.get("STATE_NAME") != null )
                  stateProvince = hmState.get("STATE_NAME").toString();
        }
        if (proposalPersonForm.getCountryCode() != null) {
            hmCountry = keyPersonExpV11TxnBean.getCountryName(proposalPersonForm.getCountryCode());
            if (hmCountry.get("COUNTRY_NAME") != null){
                  address.setCountry(hmCountry.get("COUNTRY_NAME").toString());

                  if (hmCountry.get("COUNTRY_NAME").toString().equals( "USA: UNITED STATES" )) {
                      address.setState(stateProvince);
                  } else {
                      address.setProvince(stateProvince);
                  }
            }
        }






        if (proposalPersonForm.getPostalCode() != null)
          address.setZipPostalCode(proposalPersonForm.getPostalCode());
        profile.setAddress(address);
        //Set Project Role to the appropriate value from Form drop-down list.

        if (keyPersonBean.getRole().equals(PDPI))
               profile.setProjectRole(PDPI);
        else if (keyPersonBean.getRole().equals(COPI)) {
             //case 2229 and 4213 - implement multipis for version 4.3 -
             //check if  sponsor or prime sponsor is nih
            if ( s2sTxnBean.getIsNIHSponsor(propNumber) == 1){
                 //changed for case coeusqa-2528
//                  profile.setProjectRole(OTHER);
//                  keyPersonBean.setRole("Co-Investigator");
                profile.setProjectRole("Co-Investigator");
            } else profile.setProjectRole(COPI);
        }
        //Added for COEUSQA-4077
        else {
            profile.setProjectRole(keyPersonBean.getRole());
       //Added for COEUSQA-4077     
            PersonProfileDataType.ProfileType.OtherProjectRoleCategoryType
                otherProjectRoleCategoryType =
                objFactory.createPersonProfileDataTypeProfileTypeOtherProjectRoleCategoryType();
            //max length of other role is 40
            String otherRole;

              if (keyPersonBean.getRole() != null && keyPersonBean.getRole().length() > 40) {
                otherRole = keyPersonBean.getRole().substring(0,40);
              } else {
                otherRole = UtilFactory.setNullToUnknown(keyPersonBean.getRole());
              }

            otherProjectRoleCategoryType.setValue(otherRole);

            profile.setOtherProjectRoleCategory(otherProjectRoleCategoryType);
        }
        //max length of title for s2s is 45
         if( proposalPersonForm.getPrimaryTitle() !=null && proposalPersonForm.getPrimaryTitle().length()>45)
            profile.setTitle(proposalPersonForm.getPrimaryTitle().substring(0,45));
         else
             profile.setTitle(proposalPersonForm.getPrimaryTitle());
        //coeusqa-2363 start
//        //degree type and year added for version 1.2 - case 4337
//        profile.setDegreeType(proposalPersonForm.getDegree());
//        profile.setDegreeYear(proposalPersonForm.getYearGraduated());
        //coeusqa-2363 end
        //Dont add attachments to attachment list if its an auto generated attachment for type PROFILE
        //Since its been already taken care by merging all extra key person's bios and currentandpending reports to one each
        // and sending it as a separate attachment

        if(!extraKeyPersonFlag){

            Attachment bioSketch = getKeyPersonPDF(keyPersonBean.getPersonId(), BIOSKETCH, BIOSKETCH_DOC);

            if(bioSketch != null){
                //holds biosketch attachments for this key person of pi profile
                PersonProfileDataType.ProfileType.BioSketchsAttachedType bioSketchsAttachedType =
                objFactory.createPersonProfileDataTypeProfileTypeBioSketchsAttachedType();
                gov.grants.apply.system.attachments_v1.AttachedFileDataType
                BSattachedFileType = getAttachedFileType(bioSketch);
                bioSketchsAttachedType.setBioSketchAttached(BSattachedFileType);
                profile.setBioSketchsAttached(bioSketchsAttachedType);
            }

            Attachment currentPending = getKeyPersonPDF(keyPersonBean.getPersonId(),CURRENTPENDING, CURRENTPENDING_DOC);

            if(currentPending != null){
                PersonProfileDataType.ProfileType.SupportsAttachedType supportsAttachedType =
                objFactory.createPersonProfileDataTypeProfileTypeSupportsAttachedType();
                gov.grants.apply.system.attachments_v1.AttachedFileDataType
                CPattachedFileType = getAttachedFileType(currentPending);
                supportsAttachedType.setSupportAttached(CPattachedFileType);
                profile.setSupportsAttached(supportsAttachedType);
            }
        }

        keyPerson.setProfile(profile);
        return keyPerson;
    }


     private ProposalPersonBioPDFBean getProposalPersonBioPDF(String personId, int attachmentCode) throws CoeusException,DBException{

       Vector proposalPersons = propDevFormBean.getPropPersons();
       int bioNumber = 0;
       ProposalPersonBioPDFBean proposalPersonBioPDF=null;
       for(int perCnt=0; perCnt<proposalPersons.size(); perCnt++){

           ProposalPersonFormBean proposalPersonFormBean =
                            (ProposalPersonFormBean)proposalPersons.get(perCnt);
           if(proposalPersonFormBean.getPersonId().equals(personId)){
               Vector propBiographies = proposalPersonFormBean.getPropBiography();
               if(propBiographies != null){
                   for(int bioCnt = 0; bioCnt<propBiographies.size(); bioCnt++){
                        ProposalBiographyFormBean propBiographyFormBean =
                                    (ProposalBiographyFormBean)propBiographies.get(bioCnt);

                        if(propBiographyFormBean.getDocumentTypeCode() == attachmentCode ){

                            bioNumber = propBiographyFormBean.getBioNumber();
                            break;
                        }
                   }
               }
           }
            proposalPersonBioPDF = new ProposalPersonBioPDFBean();
            proposalPersonBioPDF.setPersonId(personId);
            proposalPersonBioPDF.setProposalNumber(propNumber);
            proposalPersonBioPDF.setBioNumber(bioNumber);
            proposalPersonBioPDF =
                    proposalPersonTxnBean.getProposalPersonBioPDF(proposalPersonBioPDF);

       }
        return proposalPersonBioPDF;
    }
   /**
     * Get the biosketch content from database
     */

    private  Attachment getKeyPersonPDF(String personId, String attachmentType, int attachmentCode)
    throws CoeusException, DBException{
        ProposalPersonBioPDFBean proposalPersonBioPDF = getProposalPersonBioPDF(personId,attachmentCode);

        if(proposalPersonBioPDF==null)
            return null;
        //Create hashMap to be used to create unique content id for this attachment

        LinkedHashMap hmArg = new LinkedHashMap();
        hmArg.put(ContentIdConstants.PERSON_ID, personId);
        hmArg.put(ContentIdConstants.BIO_NUMBER, String.valueOf(proposalPersonBioPDF.getBioNumber()));
        hmArg.put(ContentIdConstants.DESCRIPTION, attachmentType);
        //check if this attachment has already been added
        //changed for case coeusdev-844
        Attachment attachment = getAttachment(hmArg);
        if(attachment == null){
           // attachment = new Attachment();
	   }else{
		   //couesdev-844
		   attachmentType+="RRKEXP";
		   hmArg.put(ContentIdConstants.DESCRIPTION, attachmentType);
	   }
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

        return attachment;
    }

    private Attachment getOtherPDF(int attachmentType) throws CoeusException, DBException{
        System.out.println("getOtherPDF for attachmentType "+attachmentType);
        Attachment otherPDF = null;
        ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
        ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean ;
        Vector vctNarrative = proposalNarrativeTxnBean.
                                getPropNarrativePDFForProposal(propNumber);
        if(vctNarrative == null){
            return null;
        }

        S2STxnBean s2sTxnBean = new S2STxnBean();
        LinkedHashMap hmArg = new LinkedHashMap();
        int moduleNum = 0;
        int narrativeType = 0;
        String title = null;

        for (int row=0; row < vctNarrative.size();row++) {
           proposalNarrativePDFSourceBean =
                (ProposalNarrativePDFSourceBean) vctNarrative.elementAt(row);

            moduleNum = proposalNarrativePDFSourceBean.getModuleNumber();
            HashMap narrativeInfo = s2sTxnBean.getNarrativeInfo(propNumber,moduleNum);
            try{
                narrativeType =
                    Integer.parseInt((String)narrativeInfo.get("NARRATIVE_TYPE_CODE"));
            }catch(NumberFormatException numFormEx){
                System.out.println("caught numberformatexception");
                narrativeType = 0;
            }

            if(narrativeType == attachmentType){
                System.out.println("found attachmentType: "+attachmentType);
                hmArg.put(ContentIdConstants.MODULE_NUMBER, String.valueOf(moduleNum));
                hmArg.put(ContentIdConstants.DESCRIPTION,
                                            narrativeInfo.get("DESCRIPTION"));

                otherPDF = getAttachment(hmArg);//check if pdf is already attached.

                if(otherPDF == null ){//not attached, so get and attach.
                    proposalNarrativePDFSourceBean =
                        proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                    if(proposalNarrativePDFSourceBean == null){
                        return null;
                    }
                    String contentId = createContentId(hmArg);
                    otherPDF = new Attachment();
                    otherPDF.setContent( proposalNarrativePDFSourceBean.getFileBytes());
                    String contentType = "application/octet-stream";
                    otherPDF.setFileName(AttachedFileDataTypeStream.addExtension(contentId));
                    otherPDF.setContentId(contentId);
                    otherPDF.setContentType(contentType);
                    addAttachment(hmArg, otherPDF);
                }
                break;
            }
        }
        return otherPDF;
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
//        hashValueType = globalObjectFactory.createHashValueType();

        fileLocation.setHref(attachment.getContentId());
        attachedFileType.setFileLocation(fileLocation);
        attachedFileType.setFileName(AttachedFileDataTypeStream.addExtension(attachment.getFileName()));
        attachedFileType.setMimeType("application/octet-stream");
        try{
            attachedFileType.setHashValue(S2SHashValue.getValue(attachment.getContent()));
        }catch(Exception ex){
            ex.printStackTrace();
            UtilFactory.log(ex.getMessage(),ex, "RRKeyPersonExpandedStreamV1_2", "getAttachedFile");
            throw new JAXBException(ex);
        }

        return attachedFileType;

    }

 //coeusqa-2363 start
     private void setDegreeInfo
    (PersonProfileDataType.ProfileType profile, String personId)throws DBException, CoeusException{
        ProposalPerDegreeFormBean proposalPerDegreeForm = new ProposalPerDegreeFormBean();
        proposalPerDegreeForm =
            proposalPersonTxnBean.getProposalPersonDegreeMax(propNumber, personId);
        if(proposalPerDegreeForm != null){
            if (proposalPerDegreeForm.getDegreeDescription() != null)
                profile.setDegreeType(proposalPerDegreeForm.getDegreeDescription());
            if (proposalPerDegreeForm.getGraduationDate() != null){
                Date graduationDate = proposalPerDegreeForm.getGraduationDate();
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy");
                String degreeYear = sdf.format(graduationDate);
                profile.setDegreeYear(degreeYear);
            }

         }
    }

  //coeus-2363 end

   private String checkNullCountry(String countryCode){
       if(countryCode == null){
           countryCode = "USA";
       }
       return countryCode;
   }

   private String checkNullHomeUnit(String homeUnit){
       if(homeUnit == null){
           homeUnit = "000001";
       }
       return homeUnit;
   }

    public Object getStream(HashMap hm) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)hm.get("PROPOSAL_NUMBER");
        return getRRKeyPerson();
    }


 }
