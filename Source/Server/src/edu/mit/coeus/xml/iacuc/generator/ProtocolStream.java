/*
 * ProtocolStream.java
 *
 * Created on November 21, 2003, 4:03 PM
 */

/* PMD check performed, and commented unused imports and variables on 10-OCT-2010
 * by George J Nirappeal
 */

package edu.mit.coeus.xml.iacuc.generator;

import edu.mit.coeus.bean.RoleInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.bean.UserRolesInfoBean;
import edu.mit.coeus.iacuc.bean.CommitteeMembershipDetailsBean;
import edu.mit.coeus.iacuc.bean.IrbWindowConstants;
import edu.mit.coeus.iacuc.bean.MembershipTxnBean;
import edu.mit.coeus.iacuc.bean.MinuteEntryInfoBean;
import edu.mit.coeus.iacuc.bean.ProtoCorrespRecipientsBean;
import edu.mit.coeus.iacuc.bean.ProtocolActionsBean;
import edu.mit.coeus.iacuc.bean.ProtocolAlternativeSearchBean;
import edu.mit.coeus.iacuc.bean.ProtocolAmendRenewalBean;
import edu.mit.coeus.iacuc.bean.ProtocolCorrespondentsBean;
import edu.mit.coeus.iacuc.bean.ProtocolCustomElementsInfoBean;
import edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean;
import edu.mit.coeus.iacuc.bean.ProtocolExceptionBean;
import edu.mit.coeus.iacuc.bean.ProtocolFundingSourceBean;
import edu.mit.coeus.iacuc.bean.ProtocolInfoBean;
import edu.mit.coeus.iacuc.bean.ProtocolInvestigatorsBean;
import edu.mit.coeus.iacuc.bean.ProtocolKeyPersonnelBean;
import edu.mit.coeus.iacuc.bean.ProtocolLocationListBean;
import edu.mit.coeus.iacuc.bean.ProtocolModuleBean;
import edu.mit.coeus.iacuc.bean.ProtocolNotepadBean;
import edu.mit.coeus.iacuc.bean.ProtocolPersonsResponsibleBean;
import edu.mit.coeus.iacuc.bean.ProtocolPrinciplesBean;
import edu.mit.coeus.iacuc.bean.ProtocolReasearchAreasBean;
import edu.mit.coeus.iacuc.bean.ProtocolReferencesBean;
import edu.mit.coeus.iacuc.bean.ProtocolReviewerInfoBean;
import edu.mit.coeus.iacuc.bean.ProtocolSpecialReviewFormBean;
import edu.mit.coeus.iacuc.bean.ProtocolSpeciesBean;
import edu.mit.coeus.iacuc.bean.ProtocolStudyGroupBean;
import edu.mit.coeus.iacuc.bean.ProtocolStudyGroupLocationBean;
import edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean;
import edu.mit.coeus.iacuc.bean.ProtocolSubmissionTxnBean;
import edu.mit.coeus.iacuc.bean.ProtocolUserRoleInfoBean;
import edu.mit.coeus.iacuc.bean.ScheduleMaintenanceTxnBean;
import edu.mit.coeus.iacuc.bean.SubmissionDetailsTxnBean;
import edu.mit.coeus.iacuc.bean.UploadDocumentBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.xml.generator.ReportBaseStream;
import java.io.IOException;
import java.math.BigInteger;

import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceDataTxnBean;
import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceFormBean;
import edu.mit.coeus.unit.bean.UnitDataTxnBean;
import edu.mit.coeus.unit.bean.UnitDetailFormBean;
import edu.mit.coeus.xml.iacuc.* ;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.xml.generator.XMLGeneratorTxnBean;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import javax.xml.bind.JAXBException;


public class ProtocolStream extends ReportBaseStream{
    ObjectFactory objFactory ;
    ProtocolDataTxnBean protocolDataTxnBean ;
    ProtocolType protocol ;
    
    private static final String STRING_VALUE_ONE = "1";
    private static final String STRING_VALUE_ZERO = "0";
    private static final String EMPTY_STRING = "";
    //Constants for new modules in iacuc end
    
    /** Creates a new instance of ProtocolStream */
    public ProtocolStream(ObjectFactory objFactory) {
        this.objFactory = objFactory ;
        protocolDataTxnBean = new ProtocolDataTxnBean() ;
    }
    
    //Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
    public ProtocolStream() {
       this(new ObjectFactory());
    }
   
  
    /**
     *  The method getObjectStream will populate all the protocol values in the protocolType
     *  object and return the protocolType type object.
     *  @param  htData Hashtable
     *  @return protocolType ProtocolType
     *  @throws DBException,CoeusException
     */
     public Object getObjectStream(Hashtable htData) 
                     throws DBException,CoeusException{
        try{
            
            String loggedInUser = (String)htData.get(CoeusConstants.LOGGED_IN_USER);
            String reportType = (String)htData.get(CoeusConstants.REPORT_TYPE);
            
            if(loggedInUser == null || reportType == null) {
                CoeusException coeusException = new CoeusException();
                coeusException.setMessage("No data found");
                throw coeusException;
            }
            ProtocolType protocolType = null;
            
            if(htData.get(CoeusConstants.PROTOCOL_INFO_BEAN) != null){
                
                ProtocolInfoBean protocolInfoBean = (ProtocolInfoBean)htData.get("PROTOCOL_INFO_BEAN");
                protocolType = getProtocol(protocolInfoBean.getProtocolNumber(),
                                            protocolInfoBean.getSequenceNumber());
                
                PrintRequirementType printRequirementType = getRequiredModulesForSummary(htData);
              
                if(protocolType != null && printRequirementType != null){
                    protocolType.getPrintRequirement().add(printRequirementType);
                }

            }
            return protocolType;
            
        }catch (Exception exception) {
            throw new CoeusException(exception.getMessage());
        }
    }
  // This method refactered for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes 
    // this method is used to get complete protocol data
    public ProtocolType getProtocol(String protocolId, int sequenceNumber)
    throws CoeusException, DBException, javax.xml.bind.JAXBException {
        protocol = objFactory.createProtocol() ;  // always use CreateProtocol not createProtocolType method as createProtocolTYpe method will not add <Protocol> tag to the o/p
        
        // get all possible info abt a procol and build the xml
        ProtocolInfoBean protocolInfoBean  = protocolDataTxnBean.getProtocolInfo(protocolId, sequenceNumber);
        // add protocolmaster data to protocol
        protocol.setProtocolMasterData(getProtocolMasterData(protocolId, sequenceNumber)) ;
        // SchoolInfoType
        try{
            String schoolName = CoeusProperties.getProperty(CoeusPropertyKeys.SCHOOL_NAME);
            String schoolAcronym = CoeusProperties.getProperty(CoeusPropertyKeys.SCHOOL_ACRONYM);
            SchoolInfoType schoolInfoType =  objFactory.createSchoolInfoType();
            schoolInfoType.setSchoolName(schoolName);
            schoolInfoType.setAcronym(schoolAcronym);
            protocol.setSchoolInfo(schoolInfoType);
        }catch(IOException ioe){
            throw new CoeusException(ioe);
        }
        // create personStream variable
        PersonStream personStream = new PersonStream(objFactory) ;
        
        addInvestigators(protocolInfoBean, personStream) ;
        addKeyStudyPerson(protocolInfoBean, personStream) ;
        addCorrespondent(protocolInfoBean, personStream) ;
        addResearchArea(protocolInfoBean, personStream) ;
        addFundingSource(protocolInfoBean, personStream) ;
        addSpecialReview(protocolInfoBean, personStream) ;
        addSubmissionDetails(protocolInfoBean, personStream) ;
        
        setProtocolDetails(protocolId, sequenceNumber);
        
        return protocol ;
    }
    
    // this method is used to get  protocol data 
    public ProtocolType getProtocolType(String protocolId, int sequenceNumber)
    throws CoeusException, DBException, javax.xml.bind.JAXBException {
        protocol = objFactory.createProtocolType() ;  // always use CreateProtocol not createProtocolType method as createProtocolTYpe method will not add <Protocol> tag to the o/p

        // get all possible info abt a procol and build the xml
        ProtocolInfoBean protocolInfoBean  = protocolDataTxnBean.getProtocolInfo(protocolId, sequenceNumber);
        // add protocolmaster data to protocol
        protocol.setProtocolMasterData(getProtocolMasterData(protocolId, sequenceNumber)) ;
        // SchoolInfoType
        try{
            String schoolName = CoeusProperties.getProperty(CoeusPropertyKeys.SCHOOL_NAME);
            String schoolAcronym = CoeusProperties.getProperty(CoeusPropertyKeys.SCHOOL_ACRONYM);
            SchoolInfoType schoolInfoType =  objFactory.createSchoolInfoType();
            schoolInfoType.setSchoolName(schoolName);
            schoolInfoType.setAcronym(schoolAcronym);
            protocol.setSchoolInfo(schoolInfoType);
        }catch(IOException ioe){
            throw new CoeusException(ioe);
        }
        // create personStream variable
        PersonStream personStream = new PersonStream(objFactory) ;

        addInvestigators(protocolInfoBean, personStream) ;
        addKeyStudyPerson(protocolInfoBean, personStream) ;
        addCorrespondent(protocolInfoBean, personStream) ;
        addResearchArea(protocolInfoBean, personStream) ;
        addFundingSource(protocolInfoBean, personStream) ;
        addSpecialReview(protocolInfoBean, personStream) ;
        addSubmissionDetails(protocolInfoBean, personStream) ;

        setProtocolDetails(protocolId, sequenceNumber);

        return protocol ;
    }
    
    // This method refactered for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes
    // this method is used to get complete protocol data for a particular submission
    public ProtocolType getProtocol(String protocolId, int sequenceNumber, int submissionNumber) throws CoeusException, DBException, javax.xml.bind.JAXBException {
        protocol = objFactory.createProtocol() ;  // always use CreateProtocol not createProtocolType method as createProtocolTYpe method will not add <Protocol> tag to the o/p
        
        // get all possible info abt a procol and build the xml
        ProtocolInfoBean protocolInfoBean  = protocolDataTxnBean.getProtocolInfo(protocolId, sequenceNumber);
        
        // add protocolmaster data to protocol
        protocol.setProtocolMasterData(getProtocolMasterData(protocolId, sequenceNumber)) ;
        // SchoolInfoType
        try{
            String schoolName = CoeusProperties.getProperty(CoeusPropertyKeys.SCHOOL_NAME);
            String schoolAcronym = CoeusProperties.getProperty(CoeusPropertyKeys.SCHOOL_ACRONYM);
            SchoolInfoType schoolInfoType =  objFactory.createSchoolInfoType();
            schoolInfoType.setSchoolName(schoolName);
            schoolInfoType.setAcronym(schoolAcronym);
            protocol.setSchoolInfo(schoolInfoType);
        }catch(IOException ioe){
            throw new CoeusException(ioe);
        }
        // create personStream variable
        PersonStream personStream = new PersonStream(objFactory) ;
        
        addInvestigators(protocolInfoBean, personStream) ;
        addKeyStudyPerson(protocolInfoBean, personStream) ;
        addCorrespondent(protocolInfoBean, personStream) ;
        addResearchArea(protocolInfoBean, personStream) ;
        addFundingSource(protocolInfoBean, personStream) ;
        addSpecialReview(protocolInfoBean, personStream) ;
        
        addSubmissionDetails(protocolInfoBean, submissionNumber, personStream, "Yes") ;
        // prps start - feb 17 2004
        // reiterate again with the parent submission number
        XMLGeneratorTxnBean xmlBean = new XMLGeneratorTxnBean() ;
        int parentSubmissionNumber = xmlBean.getParentSubmissionNumber(protocolInfoBean.getProtocolNumber(), submissionNumber ) ;
        //todo: over writing will take place use approprate condition
        addSubmissionDetails(protocolInfoBean, parentSubmissionNumber, personStream, "No") ;
        
        setProtocolDetails(protocolId, sequenceNumber);
        
        return protocol ;
       
    }
    
    // this method is used to get  protocol data for a particular submission
    public ProtocolType getProtocolType(String protocolId, int sequenceNumber, int submissionNumber) throws CoeusException, DBException, javax.xml.bind.JAXBException {
        protocol = objFactory.createProtocolType() ;  // always use CreateProtocol not createProtocolType method as createProtocolTYpe method will not add <Protocol> tag to the o/p

        // get all possible info abt a procol and build the xml
        ProtocolInfoBean protocolInfoBean  = protocolDataTxnBean.getProtocolInfo(protocolId, sequenceNumber);

        // add protocolmaster data to protocol
        protocol.setProtocolMasterData(getProtocolMasterData(protocolId, sequenceNumber)) ;
        // SchoolInfoType
        try{
            String schoolName = CoeusProperties.getProperty(CoeusPropertyKeys.SCHOOL_NAME);
            String schoolAcronym = CoeusProperties.getProperty(CoeusPropertyKeys.SCHOOL_ACRONYM);
            SchoolInfoType schoolInfoType =  objFactory.createSchoolInfoType();
            schoolInfoType.setSchoolName(schoolName);
            schoolInfoType.setAcronym(schoolAcronym);
            protocol.setSchoolInfo(schoolInfoType);
        }catch(IOException ioe){
            throw new CoeusException(ioe);
        }
        // create personStream variable
        PersonStream personStream = new PersonStream(objFactory) ;

        addInvestigators(protocolInfoBean, personStream) ;
        addKeyStudyPerson(protocolInfoBean, personStream) ;
        addCorrespondent(protocolInfoBean, personStream) ;
        addResearchArea(protocolInfoBean, personStream) ;
        addFundingSource(protocolInfoBean, personStream) ;
        addSpecialReview(protocolInfoBean, personStream) ;

        addSubmissionDetails(protocolInfoBean, submissionNumber, personStream, "Yes") ;
        // prps start - feb 17 2004
        // reiterate again with the parent submission number
        XMLGeneratorTxnBean xmlBean = new XMLGeneratorTxnBean() ;
        int parentSubmissionNumber = xmlBean.getParentSubmissionNumber(protocolInfoBean.getProtocolNumber(), submissionNumber ) ;
        //todo: over writing will take place use approprate condition
        addSubmissionDetails(protocolInfoBean, parentSubmissionNumber, personStream, "No") ;

        setProtocolDetails(protocolId, sequenceNumber);

        return protocol ;

    }

    
    //Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
    /**
     *  The method setProtocolDetails act as a integrater method which will fill all
     *  the required values for the ProtocolType object by invoking the methods for all
     *  submodules for a protocol.
     *  @param protocolId String
     *  @param sequenceNumber int
     *  @throws DBException,CoeusException,JAXBException
     */
    private void  setProtocolDetails(String protocolId, int sequenceNumber)
                             throws DBException,CoeusException,JAXBException{
        
        
        List speciesList = getSpeciesType(protocolId, sequenceNumber);
        if(speciesList != null){
            protocol.getSpecies().addAll(speciesList);
        }
        
        // Commented for COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC - Start
//        List exceptionTypeList = getExceptionTypes(protocolId, sequenceNumber);
//        if(exceptionTypeList != null){
//            protocol.getException().addAll(exceptionTypeList);
//        }
        // Commented for COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC - End
        List principleTypeList = getPrinciplesType(protocolId, sequenceNumber);
        if(principleTypeList!= null){
            protocol.getPrinciples().addAll(principleTypeList);
        }
        
        List alteDbSearTypesList= getAlternateDbSearchTypes(protocolId, sequenceNumber);
        if(alteDbSearTypesList!= null){
            protocol.getAlternateDbSearch().addAll(alteDbSearTypesList);
        }
        List studyGroupList = getStudyGroups(protocolId, sequenceNumber);
        if( studyGroupList != null){
            protocol.getStudyGroup().addAll(studyGroupList);
        }
        
        List actionTypeList = getActions(protocolId, sequenceNumber);
        if(actionTypeList != null){
            protocol.getActions().addAll(actionTypeList);
        }
        
        List otherDataList = getOthersDatas(protocolId, sequenceNumber);
        if(otherDataList != null){
            protocol.getOthersData().addAll(otherDataList);
        }
        
        List documentList = getDocuments(protocolId, sequenceNumber);
        if(documentList != null){
            protocol.getDocuments().addAll(documentList);
        }
        
        List organizationList = getOrganization(protocolId, sequenceNumber);
        if(organizationList !=null){
            protocol.getOrganization().addAll(organizationList);
        }

        List noteList = getNotes(protocolId, sequenceNumber);
        if(noteList != null){
            protocol.getNotes().addAll(noteList);
        }
        
        List referenceList = getReferences(protocolId, sequenceNumber);
        if(referenceList != null){
            protocol.getReferences().addAll(referenceList);
        }
        
        List roleList = getUserRoles(protocolId, sequenceNumber);
        if(roleList != null){
            protocol.getUserRoles().addAll(roleList);
        }
        
        List amendmentRenewalList = getAmendRenewals(protocolId);
        if(amendmentRenewalList != null){
            protocol.getAmenRenewal().addAll(amendmentRenewalList);
        }     
        
        List amendmentRenewalSummaryList = getAmendRenewalSummary(protocolId);
        if(amendmentRenewalSummaryList !=null){
            protocol.getAmendRenewSummary().addAll(amendmentRenewalSummaryList);
        }
        
        List protoCorrespList = getProtocolCorrespondence(protocolId, sequenceNumber);
        if(protoCorrespList!= null){
            protocol.getProtoCorresp().addAll(protoCorrespList);
            
        }
       
        
    }
    
    /**
     * The method getRequiredModulesForSummary will identifies which all modules summary
     * need to be printed based on the selection made by the user in the application
     * @param htData Hashtable
     * @return requiredModules
     * @throws JAXBException
     */
     
       private PrintRequirementType getRequiredModulesForSummary(Hashtable htData)
                                                          throws JAXBException{
           
           PrintRequirementType requiredModules = null;

            requiredModules = objFactory.createPrintRequirementType();
    
            if (((Boolean)htData.get(CoeusConstants.ACTIONS)).booleanValue()) {
                requiredModules.setActionsRequired(STRING_VALUE_ONE);
            } else {
                requiredModules.setActionsRequired(STRING_VALUE_ZERO);
            }
            if (((Boolean)htData.get(CoeusConstants.AMENDMENT_RENEWAL_SUMMARY)).booleanValue()) {
                requiredModules.setAmendRenewModulesRequired(STRING_VALUE_ONE);
            } else {
                requiredModules.setAmendRenewModulesRequired(STRING_VALUE_ZERO);
            }
            if (((Boolean)htData.get(CoeusConstants.AMENDMENT_RENEWAL_HISTORY)).booleanValue()) {
                requiredModules.setAmendRenewSRequired(STRING_VALUE_ONE);
            } else {
                requiredModules.setAmendRenewSRequired(STRING_VALUE_ZERO);
            }
            if (((Boolean)htData.get(CoeusConstants.ATTACHMENTS)).booleanValue()) {
                requiredModules.setDocumentsRequired(STRING_VALUE_ONE);
            } else {
                requiredModules.setDocumentsRequired(STRING_VALUE_ZERO);
            }
            if (((Boolean)htData.get(CoeusConstants.AREA_OF_RESEARCH)).booleanValue()) {
                requiredModules.setResearchAreasRequired(STRING_VALUE_ONE);
            } else {
                requiredModules.setResearchAreasRequired(STRING_VALUE_ZERO);
            }
            if (((Boolean)htData.get(CoeusConstants.CORRESPONDENTS)).booleanValue()) {
                requiredModules.setCorrespondentsRequired(STRING_VALUE_ONE);
            } else {
                requiredModules.setCorrespondentsRequired(STRING_VALUE_ZERO);
            }
            if (((Boolean)htData.get(CoeusConstants.FUNDING_SOURCE)).booleanValue()) {
                requiredModules.setFundingSourcesRequired(STRING_VALUE_ONE);
            } else {
                requiredModules.setFundingSourcesRequired(STRING_VALUE_ZERO);
            }
            if (((Boolean)htData.get(CoeusConstants.INVESTIGATOR)).booleanValue()) {
                requiredModules.setInvestigatorsRequired(STRING_VALUE_ONE);
            } else {
                requiredModules.setInvestigatorsRequired(STRING_VALUE_ZERO);
            }
            if (((Boolean)htData.get(CoeusConstants.NOTES)).booleanValue()) {
                requiredModules.setNotesRequired(STRING_VALUE_ONE);
            } else {
                requiredModules.setNotesRequired(STRING_VALUE_ZERO);
            }
            if (((Boolean)htData.get(CoeusConstants.ORGANIZATION)).booleanValue()) {
                requiredModules.setOrganizationRequired(STRING_VALUE_ONE);
            } else {
                requiredModules.setOrganizationRequired(STRING_VALUE_ZERO);
            }
            if (((Boolean)htData.get(CoeusConstants.OTHER_DATA)).booleanValue()) {
                requiredModules.setOtherDataRequired(STRING_VALUE_ONE);
            } else {
                requiredModules.setOtherDataRequired(STRING_VALUE_ZERO);
            }
            if (((Boolean)htData.get(CoeusConstants.PROTOCOL_DETAIL)).booleanValue()) {
                requiredModules.setProtocolDetailsRequired(STRING_VALUE_ONE);
            } else {
                requiredModules.setProtocolDetailsRequired(STRING_VALUE_ZERO);
            }
            if (((Boolean)htData.get(CoeusConstants.REFERENCES)).booleanValue()) {
                requiredModules.setReferencesRequired(STRING_VALUE_ONE);
            } else {
                requiredModules.setReferencesRequired(STRING_VALUE_ZERO);
            }

            if (((Boolean)htData.get(CoeusConstants.ROLES)).booleanValue()) {
                requiredModules.setUserRolesRequired(STRING_VALUE_ONE);
            } else {
                requiredModules.setUserRolesRequired(STRING_VALUE_ZERO);
            }
            if (((Boolean)htData.get(CoeusConstants.SPECIAL_REVIEW)).booleanValue()) {
                requiredModules.setSpecialReviewRequired(STRING_VALUE_ONE);
            } else {
                requiredModules.setSpecialReviewRequired(STRING_VALUE_ZERO);
            }
            if (((Boolean)htData.get(CoeusConstants.STUDY_PERSONNEL)).booleanValue()) {
                requiredModules.setKeyPersonsRequired(STRING_VALUE_ONE);
            } else {
                requiredModules.setKeyPersonsRequired(STRING_VALUE_ZERO);
            }

            if (((Boolean)htData.get(CoeusConstants.SPECIES_GROUP)).booleanValue()) {
                requiredModules.setSpeciesGroupRequired(STRING_VALUE_ONE);
            } else {
                requiredModules.setSpeciesGroupRequired(STRING_VALUE_ZERO);
            }
            
            if (((Boolean)htData.get(CoeusConstants.PROCEDURES)).booleanValue()) {
                requiredModules.setProceduresRequired(STRING_VALUE_ONE);
            } else {
                requiredModules.setProceduresRequired(STRING_VALUE_ZERO);
            }
            
            if (((Boolean)htData.get(CoeusConstants.ALTERNATIVE_SEARCH)).booleanValue()) {
                requiredModules.setAlternativeSearchesRequired(STRING_VALUE_ONE);
            } else {
                requiredModules.setAlternativeSearchesRequired(STRING_VALUE_ZERO);
            }
            
            if (((Boolean)htData.get(CoeusConstants.SCIENTIFIC_JUSTIFICATION)).booleanValue()) {
                requiredModules.setPrinciplesRequired(STRING_VALUE_ONE);
            } else {
                requiredModules.setPrinciplesRequired(STRING_VALUE_ZERO);
            }

            
            Calendar currentDate = Calendar.getInstance();
            currentDate.setTime(new Date());
            
            requiredModules.setCurrentDate(currentDate);
            
            return requiredModules;
            
        }
       
     //Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end
       
    /**
     *  It will identifies all ProtocolStudyGroupBean using protocolNumber and sequence number which is passed to
     *  the method. Creates StudyGroupType type objects form this data and return the list of StudyGroupType type 
     *  objects.
     *  @param protocoalNumber String.
     *  @param sequenceNumber int.
     *  @throws JAXBException, DBException, CoeusException.
     *  @returns studyGroupList List
     */
    
    
    private List getStudyGroups(final String protocoalNumber, final int sequenceNumber)
                                     throws JAXBException, DBException, CoeusException{
        
        final List studyGroupListDb = protocolDataTxnBean.getStudyGroups(protocoalNumber, sequenceNumber);
        Vector  studyGroupList = new Vector();
        
        if(studyGroupListDb != null){
            ProtocolStudyGroupBean studyGroupDb = null;
            StudyGroupType studyGroupType = null;
            
            for(Object obj : studyGroupListDb){
                studyGroupDb = (ProtocolStudyGroupBean)obj;
                studyGroupType = objFactory.createStudyGroupType();
                
                studyGroupType.setStudyGroupId(studyGroupDb.getStudyGroupId());
                studyGroupType.setSpeciesGroup(studyGroupDb.getSpeciesGroupName());
                studyGroupType.setSpeciesCode(studyGroupDb.getSpeciesCode());
                studyGroupType.setSpeciesDesc(studyGroupDb.getSpeciesName());
                studyGroupType.setProcedureCode(studyGroupDb.getProcedureCode());
                studyGroupType.setProcedureDesc(studyGroupDb.getProcedureName());
                studyGroupType.setProcedureCategoryCode(studyGroupDb.getProcedureCategoryCode());
                studyGroupType.setProcedureCategoryDesc(studyGroupDb.getProcedureCategoryName());
                studyGroupType.setPainCategoryCode(studyGroupDb.getPainCategoryCode());
                studyGroupDb.setPainCategoryName(studyGroupDb.getPainCategoryName());
                studyGroupType.setCount(studyGroupDb.getSpeciesCount());
                
                if(studyGroupDb.getUpdateTimestamp() != null){
                    studyGroupType.setUpdateTimestamp(formatDate(studyGroupDb.getUpdateTimestamp()));
                }
                studyGroupType.setUpdateUser(studyGroupDb.getUpdateUser());
           
                studyGroupType.getLocation().addAll(getStudyGroupLocationType(protocoalNumber,
                        sequenceNumber,studyGroupType.getStudyGroupId()));
                // Added for COEUSQA_2633 : Ability to indicate which procedures IACUC study personnel will perform and training qualifications for species - Start
                studyGroupType.getPersonResponsible().addAll(getPersonsResponsibleType(protocoalNumber,sequenceNumber,
                                                                                       studyGroupType.getStudyGroupId(),
                                                                                       studyGroupType.getSpeciesCode(),
                                                                                       studyGroupType.getProcedureCode()));
                // Added for COEUSQA_2633 : Ability to indicate which procedures IACUC study personnel will perform and training qualifications for species - End
                studyGroupType.getCustomData().addAll(getStudyGroupCustomDataType(protocoalNumber,
                        sequenceNumber,studyGroupType.getStudyGroupId()));
                
   
                studyGroupList.add(studyGroupType);
            }
        }
        
        return studyGroupList;
        
    }
    
    /**
     * Identifies all ProtocolStudyGroupLocationBean objects using protocoalNumber, sequenceNumber and studyGroupId
     * which are passed to the method. Creates StudyGroupLocationType type objects from this data. Returns the list of
     * StudyGroupLocationType type objects.
     * @param protocolNumber String.
     * @param sequenceNumber int.
     * @param studyGroupId int.
     * @throws JAXBException, DBException, CoeusException
     * @return locationList List.
     */
    private List getStudyGroupLocationType(final String protocoalNumber,
                                            final int sequenceNumber, final int studyGroupId)
                                            throws JAXBException, DBException, CoeusException{

        final List locationListDb = protocolDataTxnBean.getStudyGroupLocations(protocoalNumber, sequenceNumber, studyGroupId);
        
        final Vector locationList = new Vector();
        
        if(locationListDb != null){
            ProtocolStudyGroupLocationBean locationBeanDb = null;
            StudyGroupLocationType locationType = null;
            
            for(Object obj : locationListDb){
                locationBeanDb = (ProtocolStudyGroupLocationBean)obj;
                locationType = objFactory.createStudyGroupLocationType();
                locationType.setStudyGroupLocationId(locationBeanDb.getStudyGroupLocationId());
                locationType.setLocationId(locationBeanDb.getLocationId());
                locationType.setLocationName(locationBeanDb.getLocationName());
                locationType.setLocationTypeCode(locationBeanDb.getLocationTypeCode());
                locationType.setLocationTypeDesc(locationBeanDb.getLocationTypeDesc());
                locationType.setDescription(locationBeanDb.getStudyGroupLocationName());
                // Added for COEUSQA-2625_Add another level of detail for Location in IACUC protocol - Start    
                locationType.setLocationRoom(locationBeanDb.getLocationRoom());
                 // Added for COEUSQA-2625 - End    
                if(locationBeanDb.getUpdateTimestamp() != null){
                   locationType.setUpdateTimestamp(formatDate(locationBeanDb.getUpdateTimestamp())) ;
                }
                
                locationType.setUpdateUser(locationBeanDb.getUpdateUser());
                locationList.add(locationType);
            }
            
        }
        
        return locationList;
    }
    
    // Added for COEUSQA_2633 : Ability to indicate which procedures IACUC study personnel will perform and training qualifications for species - Start
    /**
     * Identifies all ProtocolPersonsResponsibleBean objects using protocolNumber,sequenceNumber,studyGroupId,speciesCode,procedureCode
     * which are passed to the method. Creates PersonResponsibleType type objects from this data. Returns the list of
     * PersonResponsibleType type objects.
     * @param protocolNumber String.
     * @param sequenceNumber int.
     * @param studyGroupId int.
     * @param speciesCode int.
     * @param procedureCode int.
     * @throws JAXBException, DBException, CoeusException
     * @return personsResponsibleList List.
     */
    private List getPersonsResponsibleType(String protocolNumber,
                                            int sequenceNumber,
                                            int studyGroupId,
                                            int speciesCode,
                                            int procedureCode) throws JAXBException, DBException, CoeusException{

        final List personsResponsibleListDB = protocolDataTxnBean.getPersonsResponsibleData(protocolNumber,
                                                                                  sequenceNumber,studyGroupId,
                                                                                  speciesCode,procedureCode);
        
        final Vector personsResponsibleList = new Vector();
        
        if(personsResponsibleListDB != null){
            ProtocolPersonsResponsibleBean personResponsibleBean = null;
            PersonResponsibleType personResponsibleType = null;
            
            for(Object obj : personsResponsibleListDB){
                personResponsibleBean = (ProtocolPersonsResponsibleBean)obj;
                personResponsibleType = objFactory.createPersonResponsibleType();
                personResponsibleType.setInvestigatorKeyPerson(personResponsibleBean.getPersonName());
                String trainedFlag = "No";
                if(personResponsibleBean.isTrained()){
                    trainedFlag = "Yes";
                }
                personResponsibleType.setPersonTrainedFlag(trainedFlag);
                if(personResponsibleBean.getUpdateTimestamp() != null){
                   personResponsibleType.setUpdateTimestamp(formatDate(personResponsibleBean.getUpdateTimestamp())) ;
                }
                personResponsibleType.setUpdateUser(personResponsibleType.getUpdateUser());
                personsResponsibleList.add(personResponsibleType);
            }
            
        }
        
        return personsResponsibleList;
    }
    // Added for COEUSQA_2633 : Ability to indicate which procedures IACUC study personnel will perform and training qualifications for species - End
    
    /**
     * Identifies all ProtocolCustomElementsInfoBean objects using protocoalNumber, sequenceNumber and studyGroupId
     * which are passed to the method. Creates StudyGroupCustomDataType type objects from this data. Returns the list of
     * StudyGroupCustomDataType type objects.
     * @param protocolNumber String.
     * @param sequenceNumber int.
     * @param studyGroupId int.
     * @throws JAXBException, DBException, CoeusException
     * @return custormDataList List.
     */
    
    private List getStudyGroupCustomDataType(String protocoalNumber,
                                        final int sequenceNumber, final int studyGroupId)
                                        throws JAXBException, DBException, CoeusException{
        
       
        final List custormDataListDb = protocolDataTxnBean.getStudyGroupOthersDetails(protocoalNumber, sequenceNumber,studyGroupId);
        final Vector custormDataList = new Vector();
        
        if(custormDataListDb != null){
            ProtocolCustomElementsInfoBean customElementDb = null;
            StudyGroupCustomDataType customDataType = null;
            
            for(Object obj : custormDataListDb){
                customElementDb =(ProtocolCustomElementsInfoBean)obj;
                customDataType = objFactory.createStudyGroupCustomDataType();
                
                customDataType.setColumnName(customElementDb.getColumnName());
                customDataType.setColumnValue(customElementDb.getColumnValue());
                
                if(customElementDb.getUpdateTimestamp()!= null){
                   customDataType.setUpdateTimestamp(formatDate(customElementDb.getUpdateTimestamp()));
                }
                customDataType.setUpdateUser(customElementDb.getUpdateUser());
                
                custormDataList.add(customDataType);
            }
        }
        return custormDataList;
    }
    
    /**
     *  Will identifies all ProtocolSpeciesBean objects using protocolNumber and sequenceNumber which is passed
     *  to the method. Creates SpeciesType object using this data. Returns the list of SpeciesType of objects.
     *  @param protocolNumber String.
     *  @param sequenceNumber int.
     *  @throws JAXBException, DBException, CoeusException.
     *  @return speciesList List.
     */
    
    private List getSpeciesType(final String protocolNumber, final int sequenceNumber)
                                    throws JAXBException, DBException, CoeusException{
        
        final List speciesListDb = protocolDataTxnBean.getProtocolSpecies(protocolNumber,sequenceNumber);
        final Vector  speciesList = new Vector();
        
        if(speciesListDb != null){
            ProtocolSpeciesBean speciesTypeDb = null;
            SpeciesType speciesType = null;
            
            for(Object obj : speciesListDb){
                speciesTypeDb = (ProtocolSpeciesBean)obj;
                speciesType = objFactory.createSpeciesType();
                
                speciesType.setSpeciesCode(speciesTypeDb.getSpeciesCode());
                speciesType.setSpeciesId(createBigInteger(speciesTypeDb.getSpeciesId()));
                speciesType.setSpeciesDesc(speciesTypeDb.getSpeciesName());
                
                if(speciesTypeDb.isUsdaCovered()){
                    speciesType.setIsUsdaCovered("Yes") ;
                }else{
                    speciesType.setIsUsdaCovered("No") ;
                }
                
                speciesType.setStrain(speciesTypeDb.getStrain());
                speciesType.setSpeciesCount(speciesTypeDb.getSpeciesCount());
                speciesType.setSpeciesGroup(speciesTypeDb.getSpeciesGroupName());
                speciesType.setPainCategoryCode(speciesTypeDb.getPainCategoryCode());
                speciesType.setPainCategoryDesc(speciesTypeDb.getPainCategoryName());
                speciesType.setCountTypeCode(speciesTypeDb.getSpeciesCountTypeCode());
                speciesType.setCountTypeDesc(speciesTypeDb.getspeciesCountTypeName());
                if(speciesTypeDb.getUpdateTimestamp() != null){
                    speciesType.setUpdateTimestamp(formatDate(speciesTypeDb.getUpdateTimestamp()));
                }
                speciesType.setUpdateUser(speciesTypeDb.getUpdateUser());
                // Added for COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC - Start
                speciesType.setExceptionPresent(speciesTypeDb.isExceptionsPresent() ? "Yes" : "No");
                if(speciesTypeDb.isExceptionsPresent()){
                    List exceptionTypeList = getExceptionTypes(protocolNumber, sequenceNumber,speciesTypeDb.getSpeciesId());
                    if(exceptionTypeList != null){
                        speciesType.getException().addAll(exceptionTypeList);
                    }
                }
                // Added for COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC - End
                speciesList.add(speciesType);
            }
            
        }
        
        return speciesList;
    }
    
    /**
     *  It will identifies all ProtocolExceptionBean objects using protocolNumber and sequenceNumber which is 
     *  passed to the method. Creates ExceptionType type objects using this data. Returns the list of ExceptionType
     *  object.
     *  @param protocolNumber String.
     *  @param sequenceNumber int.
     *  @throws JAXBException, DBException, CoeusException.
     *  @return exceptionTypeList List.
     */
    // Modified for COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC - Start
//    private List getExceptionTypes(final String protocolNumber, final int sequenceNumber)
//                                    throws JAXBException, DBException, CoeusException{
//            final List exceptionTypeListDb = protocolDataTxnBean.getIacucProtocolExceptions(protocolNumber,sequenceNumber);
    private List getExceptionTypes(final String protocolNumber, final int sequenceNumber, final int speciesId)
                                    throws JAXBException, DBException, CoeusException{// Modified for COEUSQA-2724 - End
        
        final List exceptionTypeListDb = protocolDataTxnBean.getIacucProtocolExceptions(protocolNumber,sequenceNumber,speciesId);
        final Vector exceptionTypeList = new Vector();
        
        if(exceptionTypeListDb != null){
            
            ProtocolExceptionBean exceptionsBeanDb = null;
            ExceptionType exceptionType = null;
            
            for(Object obj : exceptionTypeListDb){
                exceptionsBeanDb = (ProtocolExceptionBean)obj;
                exceptionType = objFactory.createExceptionType();
                
                exceptionType.setExceptionCategoryCode(exceptionsBeanDb.getExceptionCategoryCode());
                exceptionType.setExceptionCategoryDesc(exceptionsBeanDb.getExceptionCategoryDesc());
                exceptionType.setExceptionId(createBigInteger(exceptionsBeanDb.getExceptionId()));
                exceptionType.setDescription(exceptionsBeanDb.getExceptionDescription());
               
                if(exceptionsBeanDb.getUpdateTimestamp() != null){
                   exceptionType.setUpdateTimestamp(formatDate(exceptionsBeanDb.getUpdateTimestamp())); 
                }
                exceptionType.setUpdateUser(exceptionsBeanDb.getUpdateUser());
                
                exceptionTypeList.add(exceptionType);
            }
            
        }
        
        return exceptionTypeList;
    }
    
    /**
     *  It will identifies all ProtocolPrinciplesBean object using protocolNumber and sequenceNumber which is 
     *  passed to the method. Cretes PrinciplesType type objects from this data. Returns the list of PrinciplesType
     *  type objects.
     *  @param protocolNumber String.
     *  @param sequenceNumber int.
     *  @throws JAXBException, DBException, CoeusException.
     *  @return principlesTypeList List.
     */
    
    private List getPrinciplesType(final String protocolNumber, final int sequenceNumber)
                                        throws JAXBException, DBException, CoeusException{
        
        final List principlesTypeListDb = protocolDataTxnBean.getIacucProtocolPrinciples(protocolNumber, sequenceNumber);
        final Vector principlesTypeList = new Vector();
        
        if(principlesTypeListDb != null){
            PrinciplesType principleType = null;
            ProtocolPrinciplesBean principleTypeDb = null;
            
            for(Object obj : principlesTypeListDb){
                principleTypeDb = (ProtocolPrinciplesBean)obj;
                principleType = objFactory.createPrinciplesType();
              
                principleType.setReductionPrinciple(principleTypeDb.getPrincipleOfReduction());
                principleType.setRefinementPrinciple(principleTypeDb.getPrincipleOfRefinement());
                principleType.setReplacementPrinciple(principleTypeDb.getPrincipleOfReplacement());
                // Commented for COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC - Start
//                principleType.setExceptionFlag(principleTypeDb.isExceptionPresent() ? "Yes" : "No");
                // Commented for COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC - End                
                if(principleTypeDb.getUpdateTimestamp() != null){
                  principleType.setUpdateTimestamp(formatDate(principleTypeDb.getUpdateTimestamp()));  
                }
                principleType.setUpdateUser(principleTypeDb.getUpdateUser());
                principlesTypeList.add(principleType);
            }
        }
        
        return principlesTypeList;
        
    }
    
    /**
     *  Identifies all ProtocolAlternativeSearchBean type objects using protocolNumber and sequenceNumber.
     *  Creates AlternateDbSearchType type object from this data. Returns the list of AlternateDbSearchType 
     *  type objects.
     *  @param protocolNumber String.
     *  @param sequenceNumber int.
     *  @throws JAXBException, DBException, CoeusException.
     *  @return alterDbSearchList List.
     */
    
    private List getAlternateDbSearchTypes(final String protocolNumber, final int sequenceNumber)
                                        throws JAXBException, DBException, CoeusException{
        List alterDbSearchListDb = protocolDataTxnBean.getIacucProtocolAlterDBSearches(protocolNumber, sequenceNumber);
        Vector alterDbSearchList = new Vector();
        if(alterDbSearchListDb != null){
            AlternateDbSearchType alterDbSearchType = null;
            ProtocolAlternativeSearchBean alterSearchDb = null;
            for(Object obj : alterDbSearchListDb){
                alterSearchDb = (ProtocolAlternativeSearchBean)obj;
                alterDbSearchType = objFactory.createAlternateDbSearchType();
                alterDbSearchType.setSearchDate(formatDate(alterSearchDb.getSearchDate()));
                alterDbSearchType.setDatbaseId(alterSearchDb.getDatabaseSearchedCode());
                alterDbSearchType.setDatabasDesc(alterSearchDb.getDatabaseSeartched());
                alterDbSearchType.setSearchId(alterSearchDb.getAlternativeSearchId());
                alterDbSearchType.setYearsSearched(alterSearchDb.getYearsSearched());
                alterDbSearchType.setKeywordsSearched(alterSearchDb.getKeyWordsSearched());
                alterDbSearchType.setComments(alterSearchDb.getComments());
                
                if(alterSearchDb.getUpdateTimestamp()!= null){
                    alterDbSearchType.setUpdateTimestamp(formatDate(alterSearchDb.getUpdateTimestamp()));
                }
                alterDbSearchType.setUpdateUser(alterSearchDb.getUpdateUser());
                
                alterDbSearchList.add(alterDbSearchType);
            }
        }
        return alterDbSearchList;
    }
    
    
    // This method is used to get just the master data
    //tested all setter elements.
    public ProtocolMasterDataType getProtocolMasterData(String protocolId, int sequenceNumber) throws CoeusException, DBException, javax.xml.bind.JAXBException {
        ProtocolMasterDataType protocolMaster = objFactory.createProtocolMasterDataType() ;
        ProtocolInfoBean protocolInfoBean  = protocolDataTxnBean.getProtocolInfo(protocolId, sequenceNumber);
        protocolMaster.setProtocolNumber(protocolInfoBean.getProtocolNumber()) ;
        protocolMaster.setSequenceNumber(createBigInteger(protocolInfoBean.getSequenceNumber())) ;
        protocolMaster.setProtocolTypeCode(createBigInteger(protocolInfoBean.getProtocolTypeCode())) ;
        protocolMaster.setProtocolTypeDesc(protocolInfoBean.getProtocolTypeDesc()) ;
        protocolMaster.setProjectTypeCode(createBigInteger(protocolInfoBean.getProjectTypeCode()));
        

        if(protocolInfoBean.getProjectTypeDesc() != null){
            protocolMaster.setProjectTypeDesc(protocolInfoBean.getProjectTypeDesc());
        }else{
            protocolMaster.setProjectTypeDesc("");
        }
        
        protocolMaster.setProtocolStatusCode(createBigInteger(protocolInfoBean.getProtocolStatusCode())) ;
        protocolMaster.setProtocolStatusDesc(protocolInfoBean.getProtocolStatusDesc()) ;
        
        protocolMaster.setProtocolTitle(protocolInfoBean.getTitle()) ;
        
        if (protocolInfoBean.getDescription() != null) {
            protocolMaster.setProtocolDescription(protocolInfoBean.getDescription()) ;
        }
        
        if (protocolInfoBean.getApplicationDate() != null) {
            protocolMaster.setApplicationDate(convertDateStringToCalendar(protocolInfoBean.getApplicationDate().toString())) ;
        }
        if (protocolInfoBean.getApprovalDate() != null) {
            protocolMaster.setApprovalDate(convertDateStringToCalendar(protocolInfoBean.getApprovalDate().toString())) ;
        }
        
        //Added for COEUSQA-3499 Add Last Approval Date to IRB schema -Start
        if (protocolInfoBean.getLastApprovalDate() != null) {
            protocolMaster.setLastApprovalDate(convertDateStringToCalendar(protocolInfoBean.getLastApprovalDate().toString())) ;
        }
        //COEUSQA-3499 -End
        
        if (protocolInfoBean.getExpirationDate() != null) {
            protocolMaster.setExpirationDate(convertDateStringToCalendar(protocolInfoBean.getExpirationDate().toString())) ;
        }
        
        protocolMaster.setBillableFlag(protocolInfoBean.isBillableFlag()) ;
        
        if (protocolInfoBean.getFDAApplicationNumber() != null) {
            protocolMaster.setFdaApplicationNumber(protocolInfoBean.getFDAApplicationNumber()) ;
        }
        
        if (protocolInfoBean.getRefNum_1() != null) {
            protocolMaster.setRefNumber1(protocolInfoBean.getRefNum_1()) ;
        }
        
        if (protocolInfoBean.getRefNum_2() != null) {
            protocolMaster.setRefNumber2(protocolInfoBean.getRefNum_2()) ;
        }
        
        protocolMaster.setLayStatement1(protocolInfoBean.getLayStmt1());
        protocolMaster.setLayStatement2(protocolInfoBean.getLayStmt2());
        Vector vecInvestigator = protocolInfoBean.getInvestigators();
        if(vecInvestigator != null && !vecInvestigator.isEmpty()){
            for(Object investigatorDetails : vecInvestigator){
                ProtocolInvestigatorsBean investigatorBean = (ProtocolInvestigatorsBean)investigatorDetails;
                if(investigatorBean.isPrincipalInvestigatorFlag()){
                    protocolMaster.setPrincipleInvestigatorName(investigatorBean.getPersonName());
                }
            }
        }
        // Added for COEUSQA-2628-Add to Study Group screen a large comment box called "Overview and Timeline" - Start
        protocolMaster.setOverviewTimeline(protocolInfoBean.getOverviewTimeline());
        // Added for COEUSQA-2628-Add to Study Group screen a large comment box called "Overview and Timeline" - End

        return protocolMaster ;
    }
    
    
    private void addInvestigators(ProtocolInfoBean protocolInfoBean, PersonStream personStream) throws CoeusException, DBException, javax.xml.bind.JAXBException {
        // get the investigators for this protocol
        
        Vector vecInvestigator = protocolDataTxnBean.getProtocolInvestigators(protocolInfoBean.getProtocolNumber(),
                protocolInfoBean.getSequenceNumber()) ;
        if (vecInvestigator != null) {
            for (int vecCount = 0 ; vecCount < vecInvestigator.size() ; vecCount++) {
                ProtocolInvestigatorsBean protocolInvestigatorsBean =
                        (ProtocolInvestigatorsBean) vecInvestigator.get(vecCount) ;
                protocol.getInvestigator().add(personStream.getInvestigator(protocolInvestigatorsBean)) ;
            }// end for
        }// end if  vecInvestigators
        
    }
    
    
    private void addKeyStudyPerson(ProtocolInfoBean protocolInfoBean, PersonStream personStream) throws CoeusException, DBException, javax.xml.bind.JAXBException {
        // get KeyStudyperson for this protocol
        
        Vector vecKeyStudyPerson = protocolDataTxnBean.getProtocolKeyPersonList(protocolInfoBean.getProtocolNumber(),
                protocolInfoBean.getSequenceNumber()) ;
        if (vecKeyStudyPerson != null) {
            for (int vecCount = 0 ; vecCount < vecKeyStudyPerson.size() ; vecCount++) {
                KeyStudyPersonType keyStudyPerson = objFactory.createKeyStudyPersonType() ;
                ProtocolKeyPersonnelBean protocolKeyStudyPersonBean =
                        (ProtocolKeyPersonnelBean) vecKeyStudyPerson.get(vecCount) ;
                protocol.getKeyStudyPerson().add(personStream.getKeyStudyPersonnel(protocolKeyStudyPersonBean)) ;
            }// end for
        }// end if  vecKeyStudyPerson
        
    }
    
    
    private void addCorrespondent(ProtocolInfoBean protocolInfoBean, PersonStream personStream) throws CoeusException, DBException, javax.xml.bind.JAXBException {
        // get correspondent for this protocol
        
        Vector vecCorrespondent = protocolDataTxnBean.getProtocolCorrespondents(protocolInfoBean.getProtocolNumber(),
                protocolInfoBean.getSequenceNumber()) ;
        if (vecCorrespondent != null) {
            for (int vecCount = 0 ; vecCount < vecCorrespondent.size() ; vecCount++) {
                CorrespondentType correspondent = objFactory.createCorrespondentType() ;
                ProtocolCorrespondentsBean protocolCorrespondentsBean =
                        (ProtocolCorrespondentsBean) vecCorrespondent.get(vecCount) ;
                protocol.getCorrespondent().add(personStream.getCorrespondent(protocolCorrespondentsBean)) ;
                
            }// end for
        }// end if  vecCorrespondent
        
    }
    
    
    private void addResearchArea(ProtocolInfoBean protocolInfoBean, PersonStream personStream) throws CoeusException, DBException, javax.xml.bind.JAXBException {
        // get Research Area for this protocol
        
        Vector vecResearchArea = protocolDataTxnBean.getProtocolResearchArea(protocolInfoBean.getProtocolNumber(),
                protocolInfoBean.getSequenceNumber()) ;
        ResearchAreaStream researchAreaStream = new ResearchAreaStream(objFactory) ;
        if (vecResearchArea != null) {
            for (int vecCount=0 ; vecCount < vecResearchArea.size() ; vecCount++) {
                ProtocolReasearchAreasBean protocolReasearchAreasBean =
                        (ProtocolReasearchAreasBean) vecResearchArea.get(vecCount) ;
                protocol.getResearchArea().add(researchAreaStream.getResearchArea(protocolReasearchAreasBean, "Protocol")) ;
            }// end for
        } // end if vecResearchArea
    }
    
    private void addFundingSource(ProtocolInfoBean protocolInfoBean, PersonStream personStream) throws CoeusException, DBException, javax.xml.bind.JAXBException {
        //case 1522 start
        int fundingSourceTypeCode;
        String fundingSourceName, fundingSourceCode;
        //case 1522 end
        Vector vecFundingSource = protocolDataTxnBean.getProtocolFundingSources(protocolInfoBean.getProtocolNumber(),
                protocolInfoBean.getSequenceNumber()) ;
        
        if (vecFundingSource != null) {
            for (int vecCount=0 ; vecCount < vecFundingSource.size() ; vecCount++) {
                
                FundingSourceType fundingSource = objFactory.createFundingSourceType();
                ProtocolFundingSourceBean protocolFundingSourceBean =
                        (ProtocolFundingSourceBean) vecFundingSource.get(vecCount) ;
                
                fundingSourceCode = protocolFundingSourceBean.getFundingSource();
                fundingSourceTypeCode = protocolFundingSourceBean.getFundingSourceTypeCode() ;
                fundingSourceName = getFundingSourceNameForType(fundingSourceTypeCode, fundingSourceCode);
                fundingSource.setFundingSourceName(fundingSourceName) ;
                fundingSource.setFundingSource(fundingSourceCode);
                fundingSource.setTypeOfFundingSource(protocolFundingSourceBean.getFundingSourceTypeDesc()) ;
                if(protocolFundingSourceBean.getUpdateTimestamp() != null){
                     fundingSource.setUpdateTimestamp(formatDate(protocolFundingSourceBean.getUpdateTimestamp()));
                }
                
                fundingSource.setUpdateUser(protocolFundingSourceBean.getUpdateUser());
                protocol.getFundingSource().add(fundingSource) ;
                
            } // end for
        }  // end if vecFundingSource
        
        
        
    }
    
    //added for case 1522
    private String getFundingSourceNameForType(int sourceType, String sourceCode)
    throws CoeusException, DBException {
        String name=null;
        if (sourceType ==  1){
            // get sponsor name
            SponsorMaintenanceDataTxnBean sponsorTxnBean
                    = new SponsorMaintenanceDataTxnBean();
            
            SponsorMaintenanceFormBean sponsorBean
                    = sponsorTxnBean.getSponsorMaintenanceDetails(sourceCode);
            if (sponsorBean !=null) {
                name = sponsorBean.getName();
            }
            
        } else if (sourceType ==  2){
            // get unit name
            UnitDataTxnBean unitTxnBean = new UnitDataTxnBean();
            
            UnitDetailFormBean unitBean
                    = unitTxnBean.getUnitDetails(sourceCode);
            if (unitBean!=null){
                name = unitBean.getUnitName();
            }
        } else {
            // other
            name = sourceCode;
        }
        return name;
    }
    
    private void addSpecialReview(ProtocolInfoBean protocolInfoBean, PersonStream personStream) throws CoeusException, DBException, javax.xml.bind.JAXBException {
        // get Special Review for this protocol
        
        Vector vecSpecialReview = protocolDataTxnBean.getProtocolSpecialReview(protocolInfoBean.getProtocolNumber(),
                protocolInfoBean.getSequenceNumber()) ;
        if (vecSpecialReview != null) {
            for (int vecCount=0 ; vecCount < vecSpecialReview.size() ; vecCount++) {
                SpecialReviewType specialReview = objFactory.createSpecialReviewType() ;
                ProtocolSpecialReviewFormBean specialReviewBean =
                        (ProtocolSpecialReviewFormBean) vecSpecialReview.get(vecCount) ;
                
                java.util.GregorianCalendar calDate = new java.util.GregorianCalendar();
                if (specialReviewBean.getApplicationDate() != null) {
                    specialReview.setSpecialReviewApplicationDate(convertDateStringToCalendar(specialReviewBean.getApplicationDate().toString())) ;
                } else { // insert blank date
                    specialReview.setSpecialReviewApplicationDate(calDate) ;
                }
                
                if (specialReviewBean.getApprovalDate() != null) {
                    specialReview.setSpecialReviewApprovalDate(convertDateStringToCalendar(specialReviewBean.getApprovalDate().toString())) ;
                } else { // insert blank date
                    specialReview.setSpecialReviewApprovalDate(calDate) ;
                }
                
                specialReview.setSpecialReviewApprovalTypeCode(createBigInteger(specialReviewBean.getApprovalCode())) ;
                specialReview.setSpecialReviewApprovalTypeDesc(specialReviewBean.getApprovalDescription()) ;
                specialReview.setSpecialReviewComments(specialReviewBean.getComments()) ;
                specialReview.setSpecialReviewNumber(createBigInteger(specialReviewBean.getSpecialReviewNumber())) ;
                specialReview.setSpecialReviewProtocolNumber(specialReviewBean.getProtocolNumber()) ;
                specialReview.setSpecialReviewTypeCode(createBigInteger(specialReviewBean.getSpecialReviewCode())) ;
                specialReview.setSpecialReviewTypeDesc(specialReviewBean.getSpecialReviewDescription()) ;
                
                if(specialReviewBean.getUpdateTimestamp() != null){
                    specialReview.setUpdateTimestamp(formatDate(specialReviewBean.getUpdateTimestamp()));
                }
                specialReview.setUpdateUser(specialReviewBean.getUpdateUser());
                protocol.getSpecialReview().add(specialReview) ;
            } //end for
            
        } // end if vecSpecialReview
    }
    
    // get a particular submission detail for a protocol
    private void addSubmissionDetails(ProtocolInfoBean protocolInfoBean,
            int submissionNumber, PersonStream personStream, String currentFlag)
            throws CoeusException, DBException, javax.xml.bind.JAXBException{
        // get submission details for this protocol
        
        ProtocolSubmissionTxnBean submissionTxnBean = new ProtocolSubmissionTxnBean() ;
        ProtocolSubmissionInfoBean submissionInfoBean =
                submissionTxnBean.getSubmissionForSubmissionNumber(protocolInfoBean.getProtocolNumber(),submissionNumber) ;
        
        ProtocolType.SubmissionsType submission = objFactory.createProtocolTypeSubmissionsType() ;
        SubmissionDetailsType submissionDetail = objFactory.createSubmissionDetailsType() ;
        
        if(submissionInfoBean == null){
            return;
        }
        XMLGeneratorTxnBean xmlGeneratorTxnBean = new XMLGeneratorTxnBean() ;
        submissionDetail.setAbstainerCount(createBigInteger(submissionInfoBean.getAbstainerCount())) ;
        submissionDetail.setNoVote(createBigInteger(submissionInfoBean.getNoVoteCount())) ;
        submissionDetail.setProtocolNumber(submissionInfoBean.getProtocolNumber()) ;
        submissionDetail.setProtocolReviewTypeCode(createBigInteger(submissionInfoBean.getProtocolReviewTypeCode())) ;
        submissionDetail.setProtocolReviewTypeDesc(submissionInfoBean.getProtocolReviewTypeDesc()) ;
        
        Vector vecReviewers = submissionTxnBean.getProtocolReviewers(submissionInfoBean.getProtocolNumber(),
                submissionInfoBean.getSequenceNumber(), submissionInfoBean.getSubmissionNumber() ) ;
        
        if (vecReviewers != null) {
            for (int vecCount = 0 ; vecCount < vecReviewers.size() ; vecCount++) {
                ProtocolReviewerInfoBean protocolReviewerInfoBean = (ProtocolReviewerInfoBean) vecReviewers.get(vecCount) ;
                submissionDetail.getProtocolReviewer().add(personStream.getReviewer(protocolReviewerInfoBean));
            }
        }
        
        submissionDetail.setSubmissionComments(submissionInfoBean.getComments()) ;
        
        java.util.GregorianCalendar calDate = new java.util.GregorianCalendar();
        if (submissionInfoBean.getSubmissionDate() != null) {
            submissionDetail.setSubmissionDate(convertDateStringToCalendar(submissionInfoBean.getSubmissionDate().toString())) ;
        }else {//todo: check this logic
            
            submissionDetail.setSubmissionDate(calDate) ;
        }
        
        submissionDetail.setSubmissionNumber(createBigInteger(submissionInfoBean.getSubmissionNumber())) ;
        submissionDetail.setSubmissionStatusCode(createBigInteger(submissionInfoBean.getSubmissionStatusCode())) ;
        
        submissionDetail.setSubmissionStatusDesc(submissionInfoBean.getSubmissionStatusDesc()) ;
        submissionDetail.setSubmissionTypeCode(createBigInteger(submissionInfoBean.getSubmissionTypeCode())) ;
        submissionDetail.setSubmissionTypeDesc(submissionInfoBean.getSubmissionTypeDesc()) ;
        submissionDetail.setSubmissionTypeQualifierCode(createBigInteger(submissionInfoBean.getSubmissionQualTypeCode())) ;
        
        xmlGeneratorTxnBean = new XMLGeneratorTxnBean() ;
        String votingComments =  xmlGeneratorTxnBean.getVotingComments(submissionInfoBean.getProtocolNumber(),submissionInfoBean.getScheduleId());
        submissionDetail.setVotingComments(votingComments) ;
        
        SubmissionDetailsType.ActionTypeType actionTypeInfo = objFactory.createSubmissionDetailsTypeActionTypeType();
        actionTypeInfo.setActionId(createBigInteger(submissionInfoBean.getActionId()));
        actionTypeInfo.setActionTypeCode(createBigInteger(submissionInfoBean.getActionTypeCode()));
        actionTypeInfo.setActionTypeDescription(submissionInfoBean.getActionTypeDesc());
        actionTypeInfo.setActionDate(convertDateStringToCalendar(submissionInfoBean.getActionDate().toString()));
        actionTypeInfo.setActionComments(submissionInfoBean.getActionComments());
        submissionDetail.setActionType(actionTypeInfo);
        submissionDetail.setYesVote(createBigInteger(submissionInfoBean.getYesVoteCount())) ;
        
        submission.setCurrentSubmissionFlag(currentFlag) ;
        
        submission.setSubmissionDetails(submissionDetail) ;
        
        
        ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean = new ScheduleMaintenanceTxnBean() ;
        Vector  vecMinutes =  scheduleMaintenanceTxnBean.getMinutesForSubmission(submissionInfoBean.getProtocolNumber(), submissionInfoBean.getSubmissionNumber());
        if (vecMinutes != null) {
            
            MinutesStream minutesStream = new MinutesStream(objFactory);
            // Modified for COEUSQA-3046 : An incomplete review comment should not appear in the correspondence to the PI - Start
            // Only completed reviewer's review comments will be printed.
//                       Vector vecMinuteStream = minutesStream.getProtocolMinutes(vecMinutes, submissionInfoBean.getScheduleId(),submissionInfoBean.getProtocolNumber(), submissionInfoBean.getSubmissionNumber()) ;
            Vector vecReviewComments = new Vector();
            Vector vecNonCompleteReviewComm = new Vector();
            if(vecReviewers != null && !vecReviewers.isEmpty()){
                for(Object reviewerDetails : vecReviewers){
                    ProtocolReviewerInfoBean reviewerInfomation = (ProtocolReviewerInfoBean)reviewerDetails;
                    String reviewerPersonId = reviewerInfomation.getPersonId();
                    if(reviewerInfomation.isReviewComplete() && !vecMinutes.isEmpty()){
                        for(Object minutesDetails : vecMinutes){
                            MinuteEntryInfoBean minuteEntryDetails = (MinuteEntryInfoBean)minutesDetails;
                            String creatorPersonId = minuteEntryDetails.getPersonId();
                            // Checks the reviewer personId and review comments personId(personid will be comments creator id) are same
                            if(reviewerPersonId.equals(creatorPersonId)){
                                vecReviewComments.add(minuteEntryDetails);
                            }
                        }
                        // Added for COEUSQA-3194 : bug with XML/XSD changes with review comments  - Start
                    }else if(!vecMinutes.isEmpty()){
                        for(Object minutesDetails : vecMinutes){
                            MinuteEntryInfoBean minuteEntryDetails = (MinuteEntryInfoBean)minutesDetails;
                            String creatorPersonId = minuteEntryDetails.getPersonId();
                            // Checks the reviewer personId and review comments personId(personid will be comments creator id) are same
                            // Checks whether the comments added by admin(Minute Entry Type Code == 3)
                            if(reviewerPersonId.equals(creatorPersonId) && minuteEntryDetails.getMinuteEntryTypeCode() != 3){
                                vecNonCompleteReviewComm.add(minuteEntryDetails);
                            }
                        }
                    }
                    // Removes all the comments in the original collection for which the review is not completed
                    vecMinutes.removeAll(vecNonCompleteReviewComm);
                    // Added for COEUSQA-3194 : bug with XML/XSD changes with review comments  - END
                }
                
            }
            
            // Modified for COEUSQA-3194 : bug with XML/XSD changes with review comments  - Start
            // Vector will have only the Reviewer complete comments and admin comments(MinuteEntryCode = 3)
//                       Vector vecMinuteStream = minutesStream.getProtocolMinutes(vecReviewComments,
//                               submissionInfoBean.getScheduleId(),
//                               submissionInfoBean.getProtocolNumber(),
//                               submissionInfoBean.getSubmissionNumber());
            Vector vecMinuteStream = minutesStream.getProtocolMinutes(vecMinutes,
                    submissionInfoBean.getScheduleId(),
                    submissionInfoBean.getProtocolNumber(),
                    submissionInfoBean.getSubmissionNumber());
            // Modified for COEUSQA-3194 : bug with XML/XSD changes with review comments  - END
            // Modified for COEUSQA-3046 : An incomplete review comment should not appear in the correspondence to the PI - End
            
            if (vecMinuteStream.size() >0) {
                submission.getMinutes().addAll(vecMinuteStream) ;
            }
        }// end if vecMinutes
        
        
        // add committee details
        if (submissionInfoBean.getCommitteeId() != null || "".equals(submissionInfoBean.getCommitteeId())) {
            // add committee details
            String committeeId = submissionInfoBean.getCommitteeId() ;
            CommitteeStream committeeStream = new CommitteeStream(objFactory) ;
            CommitteeMasterDataType committeeMasterData = committeeStream.getCommitteeMasterData(committeeId) ;
            submission.setCommitteeMasterData(committeeMasterData) ;
            
            // add committee member details
            MembershipTxnBean membersTxnBean = new MembershipTxnBean() ;
            Vector vecMembers = membersTxnBean.getMembershipListCurrent(committeeId) ;
            
            if (vecMembers!= null) {
                for (int memCount=0 ; memCount< vecMembers.size() ; memCount++) {
                    CommitteeMembershipDetailsBean membershipBean =
                            (CommitteeMembershipDetailsBean) vecMembers.get(memCount) ;
                    // add committeeMember
                    submission.getCommitteeMember().add(committeeStream.getCommitteeMember(membershipBean)) ;
                } // end for vecMembers
            } // end if
        }
        
        // add Schedule details
        if (submissionInfoBean.getScheduleId() != null &&  !"".equals(submissionInfoBean.getScheduleId())) {
            
            // set current schedule details
            String currScheduleId = submissionInfoBean.getScheduleId() ;
            String committeeId = submissionInfoBean.getCommitteeId() ;
            
            ScheduleStream scheduleStream = new ScheduleStream(objFactory) ;
            ScheduleMasterDataType currentSchedule = scheduleStream.getScheduleMasterData(currScheduleId) ;
            submission.setScheduleMasterData(currentSchedule) ;
            
            
            HashMap hashSchedule = xmlGeneratorTxnBean.getPreviousAndNextSchedule(committeeId, currScheduleId) ;
            ScheduleSummaryType scheduleSummaryType = null;
            if (hashSchedule.get("PREVIOUS_ID")!= null) {
                String previousId = hashSchedule.get("PREVIOUS_ID").toString() ;
                ScheduleMasterDataType prevSchedule = scheduleStream.getScheduleMasterData(previousId) ;
                //ProtocolType.SubmissionsType.PrevScheduleType prevScheduleType = objFactory.createProtocolTypeSubmissionsTypePrevScheduleType() ;
                scheduleSummaryType = objFactory.createScheduleSummaryType();
                scheduleSummaryType.setScheduleMasterData(prevSchedule);
                //prevScheduleType.setScheduleMasterData(prevSchedule) ;
                
                //submission.setPrevSchedule(prevScheduleType) ;
                 submission.setPrevSchedule(scheduleSummaryType) ;
            }
            
            if (hashSchedule.get("NEXT_ID")!= null) {
                String nextId = hashSchedule.get("NEXT_ID").toString() ;
                ScheduleMasterDataType nextSchedule = scheduleStream.getScheduleMasterData(nextId) ;
                //ProtocolType.SubmissionsType.NextScheduleType nextScheduleType = objFactory.createProtocolTypeSubmissionsTypeNextScheduleType() ;
                scheduleSummaryType = objFactory.createScheduleSummaryType();
                scheduleSummaryType.setScheduleMasterData(nextSchedule);
                //nextScheduleType.setScheduleMasterData(nextSchedule) ;
                
                //submission.setNextSchedule(nextScheduleType) ;
                submission.setNextSchedule(scheduleSummaryType);
                
            }
            
        }
        
        protocol.getSubmissions().add(submission) ;
        
    }
    
    // changed this method for case 1474. Instead of getting all submissions for a protocol, if there is
    // no current submission, then just get the last submission
    private void addSubmissionDetails(ProtocolInfoBean protocolInfoBean, PersonStream personStream) throws CoeusException, DBException, javax.xml.bind.JAXBException {
        // get last submission details for this protocol
        SubmissionDetailsTxnBean submissionDetailsTxnBean = new SubmissionDetailsTxnBean() ;
        Vector vecSubmissionDetails = submissionDetailsTxnBean.getDataSubmissionDetails(protocolInfoBean.getProtocolNumber()) ;
        if (vecSubmissionDetails != null && vecSubmissionDetails.size() > 0  ) {
            int lastSub = vecSubmissionDetails.size() - 1;
            
            ProtocolSubmissionInfoBean lastSubmissionInfoBean =(ProtocolSubmissionInfoBean) vecSubmissionDetails.get(lastSub) ;
            ProtocolSubmissionInfoBean submissionInfoBean =(ProtocolSubmissionInfoBean) vecSubmissionDetails.get(lastSub) ;
            ProtocolType.SubmissionsType submission = objFactory.createProtocolTypeSubmissionsType() ;
            SubmissionDetailsType submissionDetail = objFactory.createSubmissionDetailsType() ;
            submissionDetail.setAbstainerCount(createBigInteger(submissionInfoBean.getAbstainerCount())) ;
            submissionDetail.setNoVote(createBigInteger(submissionInfoBean.getNoVoteCount()));
            submissionDetail.setProtocolNumber(submissionInfoBean.getProtocolNumber()) ;
            
            submissionDetail.setProtocolReviewTypeCode(createBigInteger(submissionInfoBean.getProtocolReviewTypeCode())) ;
            submissionDetail.setProtocolReviewTypeDesc(submissionInfoBean.getProtocolReviewTypeDesc()) ;
            
            ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean() ;
            Vector vecReviewers = protocolSubmissionTxnBean.getProtocolReviewers(submissionInfoBean.getProtocolNumber(),
                    submissionInfoBean.getSequenceNumber(), submissionInfoBean.getSubmissionNumber() ) ;
            if (vecReviewers != null) {
                for (int vecCount = 0 ; vecCount < vecReviewers.size() ; vecCount++) {
                    ProtocolReviewerInfoBean protocolReviewerInfoBean = (ProtocolReviewerInfoBean) vecReviewers.get(vecCount) ;
                    submissionDetail.getProtocolReviewer().add(personStream.getReviewer(protocolReviewerInfoBean));
                }
            }
            
            submissionDetail.setSubmissionComments(submissionInfoBean.getComments()) ;
            
            java.util.GregorianCalendar calDate = new java.util.GregorianCalendar();
            
            if (submissionInfoBean.getSubmissionDate() != null){
                submissionDetail.setSubmissionDate(convertDateStringToCalendar(submissionInfoBean.getSubmissionDate().toString())) ;
            }else{//todo: check this logic
                submissionDetail.setSubmissionDate(calDate) ;
            }
            submissionDetail.setSubmissionNumber(createBigInteger(submissionInfoBean.getSubmissionNumber())) ;
            submissionDetail.setSubmissionStatusCode(createBigInteger(submissionInfoBean.getSubmissionStatusCode())) ;
            
            submissionDetail.setSubmissionStatusDesc(submissionInfoBean.getSubmissionStatusDesc()) ;
            submissionDetail.setSubmissionTypeCode(createBigInteger(submissionInfoBean.getSubmissionTypeCode())) ;
            submissionDetail.setSubmissionTypeDesc(submissionInfoBean.getSubmissionTypeDesc()) ;
            submissionDetail.setSubmissionTypeQualifierCode(createBigInteger(submissionInfoBean.getSubmissionQualTypeCode())) ;
            submissionDetail.setSubmissionTypeQualifierDesc(submissionInfoBean.getSubmissionQualTypeDesc()) ;
            submissionDetail.setVotingComments(submissionInfoBean.getVotingComments()) ;
            submissionDetail.setYesVote(createBigInteger(submissionInfoBean.getYesVoteCount())) ;
            //eleanor start - sept-10-04 - add the action info
            SubmissionDetailsType.ActionTypeType actionTypeInfo = objFactory.createSubmissionDetailsTypeActionTypeType();
            actionTypeInfo.setActionId(createBigInteger(submissionInfoBean.getActionId()));
            actionTypeInfo.setActionTypeCode(createBigInteger(submissionInfoBean.getActionTypeCode()));
            actionTypeInfo.setActionTypeDescription(submissionInfoBean.getActionTypeDesc());
            actionTypeInfo.setActionDate(convertDateStringToCalendar(submissionInfoBean.getActionDate().toString()));
            actionTypeInfo.setActionComments(submissionInfoBean.getActionComments());
            submissionDetail.setActionType(actionTypeInfo);
            submission.setCurrentSubmissionFlag("No") ;
            //prps end - feb 17 2004
            submission.setSubmissionDetails(submissionDetail) ;
            
            // add Minutes to protocol
            ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean = new ScheduleMaintenanceTxnBean() ;
            Vector  vecMinutes =  scheduleMaintenanceTxnBean.getMinutes(submissionInfoBean.getScheduleId()) ;
            if (vecMinutes != null){
                MinutesStream minutesStream = new MinutesStream(objFactory) ;
                for (int minLoop = 0 ; minLoop < vecMinutes.size() ; minLoop++ ) {
                    MinuteEntryInfoBean minuteEntryInfoBean = (MinuteEntryInfoBean) vecMinutes.get(minLoop) ;
                    // the minute entries should be of type ProtocolType
                    if (minuteEntryInfoBean.getProtocolNumber() != null) {
                        if (minuteEntryInfoBean.getProtocolNumber().equals(protocolInfoBean.getProtocolNumber())) {
                            // add each minute to protocolSubmission
                            submission.getMinutes().add(minutesStream.getMinute(minuteEntryInfoBean, submissionInfoBean.getScheduleId())) ;
                        }// end if
                    } // end if
                } // end for
            }// end if vecMinutes
            
            
            // add committee details
            if (submissionInfoBean.getCommitteeId() != null || "".equals(submissionInfoBean.getCommitteeId())) {
                // add committee details
                String committeeId = submissionInfoBean.getCommitteeId() ;
                CommitteeStream committeeStream = new CommitteeStream(objFactory) ;
                CommitteeMasterDataType committeeMasterData = committeeStream.getCommitteeMasterData(committeeId) ;
                submission.setCommitteeMasterData(committeeMasterData) ;
                
                // add committee member details
                MembershipTxnBean membersTxnBean = new MembershipTxnBean() ;
                Vector vecMembers = membersTxnBean.getMembershipListCurrent(committeeId) ;
                if (vecMembers!= null) {
                    for (int memCount=0 ; memCount< vecMembers.size() ; memCount++) {
                        CommitteeMembershipDetailsBean membershipBean =
                                (CommitteeMembershipDetailsBean) vecMembers.get(memCount) ;
                        // add committeeMember
                        submission.getCommitteeMember().add(committeeStream.getCommitteeMember(membershipBean)) ;
                    } // end for vecMembers
                } // end if
            }
            
            // add Schedule details
            if (submissionInfoBean.getScheduleId() != null && !"".equals(submissionInfoBean.getScheduleId())) {
                // set current schedule details
                String currScheduleId = submissionInfoBean.getScheduleId() ;
                String committeeId = submissionInfoBean.getCommitteeId() ;
                
                ScheduleStream scheduleStream = new ScheduleStream(objFactory) ;
                ScheduleMasterDataType currentSchedule = scheduleStream.getScheduleMasterData(currScheduleId) ;
                submission.setScheduleMasterData(currentSchedule) ;
                
                // check for previous and next schedule
                
                XMLGeneratorTxnBean xmlGeneratorTxnBean = new XMLGeneratorTxnBean() ;
                HashMap hashSchedule = xmlGeneratorTxnBean.getPreviousAndNextSchedule(committeeId, currScheduleId) ;
                ScheduleSummaryType scheduleSummaryType = null;
                
                if (hashSchedule.get("PREVIOUS_ID")!= null) {
                    String previousId = hashSchedule.get("PREVIOUS_ID").toString() ;
                    ScheduleMasterDataType prevSchedule = scheduleStream.getScheduleMasterData(previousId) ;
                    
                    //ProtocolType.SubmissionsType.PrevScheduleType prevScheduleType = objFactory.createProtocolTypeSubmissionsTypePrevScheduleType() ;
                    //prevScheduleType.setScheduleMasterData(prevSchedule) ;
                    //submission.setPrevSchedule(prevScheduleType) ;
                    
                     scheduleSummaryType = objFactory.createScheduleSummaryType();
                     scheduleSummaryType.setScheduleMasterData(prevSchedule);
                     submission.setPrevSchedule(scheduleSummaryType);
                }
                
                if (hashSchedule.get("NEXT_ID")!= null) {
                    String nextId = hashSchedule.get("NEXT_ID").toString() ;
                    ScheduleMasterDataType nextSchedule = scheduleStream.getScheduleMasterData(nextId) ;
                    
//                    ProtocolType.SubmissionsType.NextScheduleType nextScheduleType = objFactory.createProtocolTypeSubmissionsTypeNextScheduleType() ;
//                    nextScheduleType.setScheduleMasterData(nextSchedule) ;
//                    submission.setNextSchedule(nextScheduleType) ;
                    
                    scheduleSummaryType = objFactory.createScheduleSummaryType();
                    scheduleSummaryType.setScheduleMasterData(nextSchedule);
                    submission.setPrevSchedule(scheduleSummaryType);
                }
            }
            
            protocol.getSubmissions().add(submission) ;
        }// end if vecSubmissionDetails
        
    }
    
    
    
    
    public Calendar convertDateStringToCalendar(String dateStr) {
        java.util.GregorianCalendar calDate = new java.util.GregorianCalendar();
        DateUtils dtUtils = new DateUtils();
        if (dateStr != null) {
            if (dateStr.indexOf('-')!= -1) { // if the format obtd is YYYY-MM-DD
                dateStr = dtUtils.formatDate(dateStr,"MM/dd/yyyy");
            }
            calDate.set(Integer.parseInt(dateStr.substring(6,10)),
                    Integer.parseInt(dateStr.substring(0,2)) - 1,
                    Integer.parseInt(dateStr.substring(3,5))) ;
            
            return calDate ;
        }
        return null ;
    }
    
    private BigInteger createBigInteger(final int intNumber){
        Integer number = new Integer(intNumber);
        return new BigInteger(String.valueOf(number));
    }
    
    //Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
    /**
     *  The method getActions identifies all actions performed on a protocol with sequecne number.
     *  @param protocolNumber String
     *  @param sequenceNumber int
     *  @return return actionsList List
     *  @throws DBException, CoeusException, JAXBException
     */
    private List getActions(final String protocolNumber, final int sequenceNumber) 
                            throws DBException,CoeusException,JAXBException{
        Vector actionList = null;
        final List actionsListDb = protocolDataTxnBean.getProtocolActions(protocolNumber, sequenceNumber);
        
        if(actionsListDb != null){
            actionList = new Vector();
            ProtocolActionsBean protocolActionsBeanDb = null;
            ProtocolActionsType actionType = null;
            
            for(Object obj : actionsListDb){
                protocolActionsBeanDb = (ProtocolActionsBean)obj;
                actionType = objFactory.createProtocolActionsType();
                
                actionType.setActionId(protocolActionsBeanDb.getActionId());
                actionType.setActionTypeDesc(protocolActionsBeanDb.getActionTypeDescription());
                actionType.setActionTypeCode(protocolActionsBeanDb.getActionTypeCode());
                actionType.setSubmissionNumber(protocolActionsBeanDb.getSubmissionNumber());
                actionType.setComments(protocolActionsBeanDb.getComments());
                
                if(protocolActionsBeanDb.getUpdateTimestamp()!= null){
                    actionType.setUpdateTimestamp(formatDate(protocolActionsBeanDb.getUpdateTimestamp()));
                }
                
                actionType.setUpdateUser(protocolActionsBeanDb.getUpdateUser());
                actionType.setActionDate(formatDate(protocolActionsBeanDb.getActionDate()));
                actionType.setApprovalDate(formatDate(protocolActionsBeanDb.getApprovalDate()));
                
                //todo: fill the list for acitontype
                actionList.add(actionType);
            }
        }
        return actionList;
    }
    

    
    /**
     *  The method getOthersDatas identifies all customelements  of a protocol with sequecne number.
     *  @param protocolNumber String
     *  @param sequenceNumber int
     *  @return return OthersDataList List
     *  @throws DBException, CoeusException, JAXBException
     */
           
    private List getOthersDatas(final String protocolNumber, final int sequenceNumber) 
                            throws DBException,CoeusException,JAXBException{
        Vector othersDataList = null;
        final List otherDetailsListDb = protocolDataTxnBean.getProtocolOthersDetails(protocolNumber, sequenceNumber);
        
        if(otherDetailsListDb != null){
            othersDataList = new Vector();
            ProtocolCustomElementsInfoBean customInfoBeanDb = null;
            OtherDataType otherDataType = null;
            
            for(Object obj : otherDetailsListDb){
                customInfoBeanDb = (ProtocolCustomElementsInfoBean)obj;
                otherDataType = objFactory.createOtherDataType();
                
                otherDataType.setColumnName(customInfoBeanDb.getColumnName());
                otherDataType.setColumnValue(customInfoBeanDb.getColumnValue());
                   
                if(customInfoBeanDb.getUpdateTimestamp() != null){
                    otherDataType.setUpdateTimestamp(formatDate(customInfoBeanDb.getUpdateTimestamp()));
                }
                
                otherDataType.setUpdateUser(customInfoBeanDb.getUpdateUser());
                othersDataList.add(otherDataType);
            }
        }
        
        return othersDataList;
    }
    
    
    /**
     *  The method getDocuments identifies all documents of a protocol with sequecne number.
     *  @param protocolNumber String
     *  @param sequenceNumber int
     *  @return return documentList List
     *  @throws DBException, CoeusException, JAXBException
     */
    
   private List getDocuments(final String protocolNumber, final int sequenceNumber) 
                            throws DBException,CoeusException,JAXBException{
       Vector documentList = null;
       final List documentListDb = protocolDataTxnBean.getProtocolDocumentsForTheSeqeuence(protocolNumber,sequenceNumber);
    
       if(documentListDb != null){
           documentList = new Vector();
           UploadDocumentBean documetBeanDb = null;
           DocumentType documentType = null;
           
           for(Object obj : documentListDb){
               documetBeanDb = (UploadDocumentBean)obj;
               documentType = objFactory.createDocumentType();
               
               documentType.setDocumentId(documetBeanDb.getDocumentId());
               documentType.setDocumentTypeCode(documetBeanDb.getDocCode());
               documentType.setDocumentTypeDesc(documetBeanDb.getDocType());
               documentType.setDocumentStatusCode(documetBeanDb.getStatusCode());
               documentType.setDescription(documetBeanDb.getDescription());
               documentType.setFileName(documetBeanDb.getFileName());
               documentType.setVersionNumber(documetBeanDb.getVersionNumber());
               // todo: identify the getterdocumentType.setDocumentTypeGroup(documetBeanDb.ge)
               if(documetBeanDb.getUpdateTimestamp() != null){
                   documentType.setUpdateTimestamp(formatDate(documetBeanDb.getUpdateTimestamp()));
               }
               documentType.setUpdateUser(documetBeanDb.getUpdateUser());
                       
               documentList.add(documentType);
           }
       }
       
       return documentList;
   }
  

    /**
     *  The method getOrganization identifies all Organization of a protocol with sequecne number.
     *  @param protocolNumber String
     *  @param sequenceNumber int
     *  @return return organizationList List
     *  @throws DBException, CoeusException, JAXBException
     */
   
   private List getOrganization(final String protocolNumber, final int sequenceNumber) 
                            throws DBException,CoeusException,JAXBException{
       Vector organizationList = null;
       final List organizationListDb = protocolDataTxnBean.getProtocolLocationList(protocolNumber, sequenceNumber);
       LocationType locationType = null;
       
       if(organizationListDb != null){
           organizationList = new Vector();
           ProtocolLocationListBean locationBeanDb = null;
           locationType = objFactory.createLocationType();
           
           for(Object obj : organizationListDb){
               locationBeanDb = (ProtocolLocationListBean)obj;
               locationType = objFactory.createLocationType();
               
               locationType.setOrgTypeCode(locationBeanDb.getOrganizationTypeId());
               locationType.setOrgTypeDesc(locationBeanDb.getOrganizationTypeName());
               locationType.setOrganizationId(locationBeanDb.getOrganizationId());
               String address = locationBeanDb.getAddress();
               if(address !=null && !"".equals(address)){
                   address = locationBeanDb.getAddress();
                   address =  address.replaceAll("[$#]", " ");
               }
               locationType.setRolodexId(locationBeanDb.getRolodexId());
               locationType.setAddress(address);
               locationType.setOrgName(locationBeanDb.getOrganizationName());
               locationType.setAnimalWelfareAssurance(
                       locationBeanDb.getAnimalWelfareAssurance() == null ? EMPTY_STRING :
                           locationBeanDb.getAnimalWelfareAssurance() );
               if(locationBeanDb.getUpdateTimestamp() != null){
                   locationType.setUpdateTimestamp(formatDate(locationBeanDb.getUpdateTimestamp()));
               }

               locationType.setUpdateUser(locationBeanDb.getUpdateUser());
               
               organizationList.add(locationType);
           }
       }
       
       return organizationList;
   }
   
    /**
     *  The method getNotes identifies all notes of a protocol with sequecne number.
     *  @param protocolNumber String
     *  @param sequenceNumber int
     *  @return return organizationList List
     *  @throws DBException, CoeusException, JAXBException
     */
   
  private List getNotes(final String protocolNumber, final int sequenceNumber) 
                        throws DBException,CoeusException,JAXBException{
        Vector noteList = null;
        final List noteListDb = protocolDataTxnBean.getProtocolNotes(protocolNumber,sequenceNumber);
        NotesType notesType = null;
        
        if(noteListDb != null){
            ProtocolNotepadBean notePadBeanDb = null;
            noteList = new Vector();
            
            for(Object obj : noteListDb){
               notePadBeanDb = (ProtocolNotepadBean)obj;
               notesType = objFactory.createNotesType();
               
               notesType.setEntryNumber(notePadBeanDb.getEntryNumber());
               notesType.setComments(notePadBeanDb.getComments());
               
               if(notePadBeanDb.isRestrictedFlag()){
                   notesType.setRestrictedView("Y");
               }else{
                   notesType.setRestrictedView("N");
               }
               
               if(notePadBeanDb.getUpdateTimestamp() != null){
                 notesType.setUpdateTimestamp(formatDate(notePadBeanDb.getUpdateTimestamp()));
               }
               
               notesType.setUpdateUser(notePadBeanDb.getUpdateUser());
               noteList.add(notesType);
            }
        }
        
        return noteList;
   }
  

     /**
     *  The method getReferences identifies all References of a protocol with sequecne number.
     *  @param protocolNumber String
     *  @param sequenceNumber int
     *  @return return organizationList List
     *  @throws DBException, CoeusException, JAXBException
     */
   
    
     private List getReferences(final String protocolNumber, final int sequenceNumber) 
                            throws DBException,CoeusException,JAXBException{
      Vector referencesList = null;
      final List referencesListDb = protocolDataTxnBean.getProtocolReferences(protocolNumber,sequenceNumber);
      
      if(referencesListDb != null){
          
          ProtocolReferencesBean referenceBeanDb = null;
          referencesList = new Vector();
          ReferencesType referenceType = null;
          
          for(Object obj : referencesListDb){
             referenceBeanDb = (ProtocolReferencesBean)obj;
             referenceType = objFactory.createReferencesType();
             
             referenceType.setReferenceNumber(referenceBeanDb.getReferenceNumber());
             referenceType.setReferenceTypeCode(referenceBeanDb.getReferenceTypeCode());
             referenceType.setReferenceTypeDesc(referenceBeanDb.getReferenceTypeDescription());
             referenceType.setReferenceKey(referenceBeanDb.getReferenceKey());
             
             if(referenceBeanDb.getApplicationDate() != null){
                referenceType.setApplicationDate(formatDate(referenceBeanDb.getApplicationDate()));
             } 
             if(referenceBeanDb.getApprovalDate() != null){
                referenceType.setApprovalDate(formatDate(referenceBeanDb.getApprovalDate()));
             } 
             referenceType.setComments(referenceBeanDb.getComments());
             if(referenceBeanDb.getUpdateTimestamp() != null){
                referenceType.setUpdateTimestamp(formatDate(referenceBeanDb.getUpdateTimestamp()));  
             }
             referenceType.setUpdateUser(referenceBeanDb.getUpdateUser());
             
             referencesList.add(referenceType);
          }
             
      }
      
      return referencesList;
  }

     
    /**
     *  The method getUserRoles identifies all Roles of a protocol with sequecne number.
     *  @param protocolNumber String
     *  @param sequenceNumber int
     *  @return return organizationList List
     *  @throws DBException, CoeusException, JAXBException
     */
   
     
  private List getUserRoles(final String protocolNumber, final int sequenceNumber) 
                            throws DBException,CoeusException,JAXBException{
      
      Vector roleList = null;
      final List usersListDb = protocolDataTxnBean.getProtocolUserRoles(protocolNumber, sequenceNumber);
      
      if(usersListDb != null){
          roleList = new Vector();
          UserRolesInfoBean userRoleInfoBean = null;
          RolesType rolesType = null;
          
          for(Object Obj : usersListDb){
              userRoleInfoBean = (UserRolesInfoBean)Obj;
              rolesType = objFactory.createRolesType();
              
              rolesType.setRoleId(userRoleInfoBean.getRoleBean().getRoleId());
              rolesType.setRoleName(userRoleInfoBean.getRoleBean().getRoleName());
              
              if(userRoleInfoBean.getUsers() != null){
                  List userInEachRoleList = getUsersInEachRole(userRoleInfoBean.getUsers(),
                                                               userRoleInfoBean.getRoleBean());
                      
                  if(userInEachRoleList != null){
                      rolesType.getUserRoles().addAll(userInEachRoleList);
                  }
              }
              
              roleList.add(rolesType);
          }
      }
     
      return roleList;
  }
  
  
    /**
     *  The method getUsersInEachRole identifies all users in each role of a protocol with sequecne number.
     *  @param protocolNumber String
     *  @param sequenceNumber int
     *  @return return organizationList List
     *  @throws DBException, CoeusException, JAXBException
     */
  
  private List getUsersInEachRole(final List usersInEachRoleListDb, final RoleInfoBean roleInfoBean) 
                        throws DBException,CoeusException,JAXBException{
      
      Vector usersInEachRoleList = null;
      
      if(usersInEachRoleListDb != null){
          usersInEachRoleList = new Vector();
          
          ProtocolUserRoleInfoBean userRoleInfoBean = null;
          UserRolesType userRolesType = null;
          RolesType rolesType = null;
          UserInfoBean userInfoBean = null;
          
          for(Object Obj : usersInEachRoleListDb){
              userRoleInfoBean = (ProtocolUserRoleInfoBean)Obj;
              userInfoBean = userRoleInfoBean.getUserBean();
              userRolesType = objFactory.createUserRolesType();
              
              userRolesType.setRoleId(roleInfoBean.getRoleId());
              userRolesType.setRoleDesc(roleInfoBean.getRoleDesc());
              userRolesType.setUserId(userInfoBean.getUserId());
              userRolesType.setUnitName(userInfoBean.getUnitName());
              userRolesType.setUnitNumber(userInfoBean.getUnitNumber());
              userRolesType.setPersonId(userInfoBean.getPersonId());
              userRolesType.setPersonName(userInfoBean.getPersonName());
              userRolesType.setUserName(userInfoBean.getUserName());
              
              if(userInfoBean.getUpdateTimestamp() != null){
                  userRolesType.setUpdateTimestamp(formatDate(userInfoBean.getUpdateTimestamp()));
              }
              
              userRolesType.setUpdateUser(userInfoBean.getUpdateUser());
                      
              usersInEachRoleList.add(userRolesType);
   
          }
      }
     
      return usersInEachRoleList;
  }

    /**
     *  The method getAmendRenewals identifies all AmendRenewals of a protocol .
     *  @param protocolNumber String
     *  @return return organizationList List
     *  @throws DBException, CoeusException, JAXBException
     */
          
 private List getAmendRenewals(final String protocolNumber) 
                        throws DBException,CoeusException,JAXBException{
     Vector amendRenewalList = null;
     final List amendRenewalListDb = protocolDataTxnBean.getAllAmendmentsRenewals(protocolNumber);
     
     if(amendRenewalListDb != null){
         ProtocolAmendRenewalBean amendRenewalBeanDb = null; 
         AmendRenewalType amendRenewalType = null;
         amendRenewalList = new Vector();
         
         for(Object obj : amendRenewalListDb){
            amendRenewalBeanDb = (ProtocolAmendRenewalBean)obj;
            amendRenewalType = objFactory.createAmendRenewalType();
            
            amendRenewalType.setAmendRenNumber(amendRenewalBeanDb.getProtocolAmendRenewalNumber());
            
            if(amendRenewalBeanDb.getDateCreated() != null){
                amendRenewalType.setDateCreated(formatDate(amendRenewalBeanDb.getDateCreated()));
            }
            amendRenewalType.setSummary(amendRenewalBeanDb.getSummary());
            amendRenewalType.setAmendmentType(getAmendmentType(amendRenewalBeanDb.getProtocolAmendRenewalNumber()));
            amendRenewalType.setProtocolStatusCode(amendRenewalBeanDb.getProtocolStatus());
            amendRenewalType.setProtocolStatusDesc(amendRenewalBeanDb.getProtocolStatusDescription());
            String protoAmendRenewNumber = amendRenewalBeanDb.getProtocolAmendRenewalNumber();
            String protoAmendRenewVersion = EMPTY_STRING;
            if(protoAmendRenewNumber != null && protoAmendRenewNumber.length() >= 14 ) {
                protoAmendRenewVersion = protoAmendRenewNumber.substring(11);
            }

            
            
            amendRenewalType.setVersion(protoAmendRenewVersion);
            amendRenewalType.setModuleCode(String.valueOf(ModuleConstants.IACUC_MODULE_CODE));
            amendRenewalType.setModuleDescription(ModuleConstants.IACUC_MODULE_DESCRIPTION);
            
            if(amendRenewalBeanDb.getUpdateTimestamp() != null){
                amendRenewalType.setUpdateTimestamp(formatDate(amendRenewalBeanDb.getUpdateTimestamp()));
            }
            
            amendRenewalType.setUpdateUser(amendRenewalBeanDb.getUpdateUser());
            
            amendRenewalList.add(amendRenewalType);
           
         }
     }
     return amendRenewalList;
 }  
 
 
    /**
     *  The method getAmendmentType identifies type of 
     *  Amendment using  amendment renewvel number.
     *  @param amendRenewalNumber String
     *  @return return amendmentType String 
     *  @throws DBException, CoeusException, JAXBException
     */
  
 
 private String getAmendmentType(final String amendRenewalNumber){
     
      String type = amendRenewalNumber.substring(10,11);
      String amendmentType = "";
      
      if(type!=null && type.equals(Character.toString(CoeusConstants.IACUC_AMENDMENT))) {
          amendmentType = "Amendment";
      }else if(type!=null && type.equals(Character.toString(CoeusConstants.IACUC_RENEWAL))) {
          amendmentType = "Renewal";
      }else if(type!=null && type.equals(Character.toString(CoeusConstants.IACUC_RENEWAL_WITH_AMENDMENT))) {
          amendmentType = "Renewal/Amendment";
      }else if(type!=null && type.equals(Character.toString(CoeusConstants.IACUC_CONTINUATION_CONTINUING_REVIEW))) {
          amendmentType = "Continuation/Continuing Review";
      }else if(type!=null && type.equals(Character.toString(CoeusConstants.IACUC_CONTI_CONTINUING_REVIEW_WITH_AMEND))) {
          amendmentType = "Continuation/Continuing Review with Amendment";
      }
      
      return amendmentType;
     
 }
 
    /**
     *  The method getAmendRenewalSummary identifies 
     *  editiable modules of all amendment renewal of a particular protocol.
     *  @param protocolNumber String
     *  @return return amendmentRenewalSummaryList List
     *  @throws DBException, CoeusException, JAXBException
     */
          
  private List getAmendRenewalSummary(final String protocolNumber) 
                        throws DBException,CoeusException,JAXBException{

     final List amendRenewalListDb = protocolDataTxnBean.getAllAmendmentsRenewals(protocolNumber);
     List amendmentRenewalSummaryList = null;
     
     if(amendRenewalListDb != null){
         ProtocolAmendRenewalBean amendRenewalBeanDb = null; 
         amendmentRenewalSummaryList = new Vector();
         List summaryList = null;
         
         for(Object obj : amendRenewalListDb){
            amendRenewalBeanDb = (ProtocolAmendRenewalBean)obj;
            summaryList = getEachAmendRenewSummary(
                    amendRenewalBeanDb.getProtocolAmendRenewalNumber(), amendRenewalBeanDb.getSummary());
            
            if(summaryList != null){
                amendmentRenewalSummaryList.addAll(summaryList);
            }
          
         }
     }
     
     return amendmentRenewalSummaryList;
  }
  
    /**
     *  The method getAmendRenewalSummary identifies 
     *  editiable modules of a particular amendment renewal .
     *  @param amendRenewalNumber String
     *  @return return amendRenewEditableDataList List
     *  @throws DBException, CoeusException, JAXBException
     */
  
 private List getEachAmendRenewSummary(final String amendRenewalNumber, final String amendRenewalSummary) 
                        throws DBException,CoeusException,JAXBException{
     
     List amendRenewEditableDataListDb = protocolDataTxnBean.getProtoAmendRenewEditableModules(amendRenewalNumber);
     Vector vecProtocolModules = protocolDataTxnBean.getAmendRenewEditableModules();
     HashMap hmProtocolModules = null;
     if(vecProtocolModules != null && !vecProtocolModules.isEmpty()){
         hmProtocolModules = (HashMap)vecProtocolModules.get(0);
     }
     List amendRenewEditableDataList = null;
     
     if(amendRenewEditableDataListDb != null){
         AmendRenewSummaryType amendRenewSummaryType = null;
         amendRenewEditableDataList = new Vector();
         ProtocolModuleBean moduleBean = null;
         amendRenewSummaryType = objFactory.createAmendRenewSummaryType();
         amendRenewSummaryType.setSummary(amendRenewalSummary);
         amendRenewSummaryType.setProtoAmendRenewalNumber(amendRenewalNumber);
         for(Object obj : amendRenewEditableDataListDb){
             String moduleCode = obj.toString();
             
             ProtocolModuleType protocolModuleType = objFactory.createProtocolModuleType();
             
             protocolModuleType.setProtocolModuleCode(moduleCode);
             protocolModuleType.setProtocolModuleDesc(hmProtocolModules.get(moduleCode).toString());
                                    
             amendRenewSummaryType.getProtocolModules().add(protocolModuleType);

         }
          amendRenewEditableDataList.add(amendRenewSummaryType);
     }
     
     return amendRenewEditableDataList;
 }
 
    /**
     *  The method getProtocolModuleDescription identifies 
     *  ModuleDescription of a modules by using the module code.
     *  @param protocolModuleCode String
     *  @return return moduleDescription String
     */
 
 private String getProtocolModuleDescription(final String protocolModuleCode){
     
     String moduleDescription = "";
     
     if(IrbWindowConstants.GENERAL_INFO.equals(protocolModuleCode)){
         moduleDescription = IrbWindowConstants.GENERAL_INFO_LABEL;
     }else if(IrbWindowConstants.INVESTIGATOR.equals(protocolModuleCode)){
         moduleDescription = IrbWindowConstants.INVESTIGATOR_LABEL;
     }else if(IrbWindowConstants.KEY_STUDY_PERSONS.equals(protocolModuleCode)){
         moduleDescription = IrbWindowConstants.KEY_STUDY_PERSONS_LABEL;
     }else if(IrbWindowConstants.AREA_OF_RESEARCH.equals(protocolModuleCode)){
         moduleDescription = IrbWindowConstants.AREA_OF_RESEARCH_LABEL;
     }else if(IrbWindowConstants.FUNDING_SOURCE.equals(protocolModuleCode)){
         moduleDescription = IrbWindowConstants.FUNDING_SOURCE_LABEL;
     }else if(IrbWindowConstants.SPECIAL_REVIEW.equals(protocolModuleCode)){
         moduleDescription = IrbWindowConstants.SPECIAL_REVIEW_LABEL;
     }else if(IrbWindowConstants.UPLOAD_DOCUMENTS.equals(protocolModuleCode)){
         moduleDescription = IrbWindowConstants.UPLOAD_DOCUMENTS_LABEL;
     }else if(IrbWindowConstants.CORRESPONDENTS.equals(protocolModuleCode)){
         moduleDescription = IrbWindowConstants.CORRESPONDENTS_LABEL;
     }else if(IrbWindowConstants.NOTES.equals(protocolModuleCode)){
         moduleDescription = IrbWindowConstants.NOTES_LABEL;
     }else if(IrbWindowConstants.IDENTIFIERS.equals(protocolModuleCode)){
         moduleDescription = IrbWindowConstants.IDENTIFIERS_LABEL;
     }else if(IrbWindowConstants.ORGANIZATION.equals(protocolModuleCode)){
         moduleDescription = IrbWindowConstants.ORGANIZATION_LABEL;
     }else if(IrbWindowConstants.OTHERS.equals(protocolModuleCode)){
         moduleDescription = IrbWindowConstants.OTHERS_LABEL;
     }else if(IrbWindowConstants.UPLOAD_OTHER_DOCUMENTS.equals(protocolModuleCode)){
         moduleDescription = IrbWindowConstants.UPLOAD_OTHER_DOCUMENTS_LABEL;
     }else if(IrbWindowConstants.SPECIES_STUDY_GROUP.equals(protocolModuleCode)){
         moduleDescription = IrbWindowConstants.SPECIES_STUDY_GROUP_LABEL;
     }

     else if(IrbWindowConstants.ALTERNATIVE_SEARCH.equals(protocolModuleCode)){
         moduleDescription = IrbWindowConstants.ALTERNATIVE_SEARCH_LABEL;
     }
     else if(IrbWindowConstants.SCIENTIFIC_JUSTIFICATION.equals(protocolModuleCode)){
         moduleDescription = IrbWindowConstants.SCIENTIFIC_JUSTIFICATION_LABEL;
     }
     
     return moduleDescription;
 }
 
    /**
     *  The method getProtocolCorrespondence identifies 
     *  all correspondence generated for a protocol.
     *  @param protocolNumber String
     *  @param sequenceNumber String
     *  @return return correspTypeList List
     *  @throws DBException, CoeusException, JAXBException
     */
 
 private List getProtocolCorrespondence(final String protocolNumber,final int sequenceNumber)
                                throws DBException,CoeusException,JAXBException{
     
     final List actionsListDb = protocolDataTxnBean.getProtocolActions(protocolNumber, sequenceNumber);
     List correspTypeList = null;
 
      if(actionsListDb != null){
            ProtocolActionsBean protocolActionsBeanDb = null;
            correspTypeList = new Vector();
            List correspListForEachAction = null;
            
            for(Object obj : actionsListDb){
                protocolActionsBeanDb = (ProtocolActionsBean)obj;
             
                correspListForEachAction = getCorrespondenceForEachAction(protocolNumber, sequenceNumber,
                                                                    protocolActionsBeanDb.getActionId());
                
                
                if(correspListForEachAction != null && !correspListForEachAction.isEmpty()){
                    correspTypeList.addAll(correspListForEachAction);
                }
            }
      }
     
     return correspTypeList;
 }
 
    /**
     *  The method getCorrespondenceForEachAction identifies 
     *  all correspondence generated for a protocol for each action.
     *  @param protocolNumber String
     *  @param sequenceNumber String
     *  @param actionId int
     *  @return return correspondenceList List
     *  @throws DBException, CoeusException, JAXBException
     */
 
 private List getCorrespondenceForEachAction(final String protocolNumber, 
                            final int sequenceNumber, final int actionId)
                            throws DBException, CoeusException, JAXBException{
     
     List correspondenceListDb = protocolDataTxnBean.getAllCorrespondenceDocuments(protocolNumber, sequenceNumber, actionId);
     List correspondenceList = null;
     
     if(correspondenceListDb != null){
         
         ProtoCorrespRecipientsBean correspRecipientsBeanDb = null;
         ProtoCorrespType protoCorrespType = null;
         correspondenceList = new Vector();
         
         for(Object obj : correspondenceListDb){
             correspRecipientsBeanDb = (ProtoCorrespRecipientsBean)obj;
             protoCorrespType = objFactory.createProtoCorrespType();
             
             protoCorrespType.setProtoCorrespTypeCode(correspRecipientsBeanDb.getProtoCorrespTypeCode());
             protoCorrespType.setDescription(correspRecipientsBeanDb.getProtoCorrespDescription());
             protoCorrespType.setModuleId(String.valueOf(ModuleConstants.IACUC_MODULE_CODE));
             
             if(correspRecipientsBeanDb.getUpdateTimestamp() != null){
                 protoCorrespType.setUpdateTimestamp(formatDate(correspRecipientsBeanDb.getUpdateTimestamp()));
             }
             protoCorrespType.setUpdateUser(correspRecipientsBeanDb.getUpdateUser());
           
             //todo: fill the datas for the following two lists.
             //CorrespondenceDefRecip
             //CorrespondenceRecipients
             
             correspondenceList.add(protoCorrespType);
         }
         
     }
     
     return correspondenceList;
 }
 
 
    /**
     *  Returns java.util.Calendar type object. Creates a calendar type object and set it time to date which is 
     *  passed to the method. Return the Calendar type object.
     *  @param date Date.
     *  @return calendar Calendar.
     */
    private Calendar formatDate(final Date date){
        Calendar calendar =null;
        if(date != null){
            calendar = calendar.getInstance();
            calendar.setTime(date);
        }
        
        return calendar;
    }

}
//Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end