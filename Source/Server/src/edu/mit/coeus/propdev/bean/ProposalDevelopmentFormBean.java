/*
 * @(#)ProposalDevelopmentFormBean.java 1.0 03/10/03 2:58 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and
 * variables on 24-SEPT-2007 by Divya
 */
package edu.mit.coeus.propdev.bean;

import edu.mit.coeus.organization.bean.*;
import edu.mit.coeus.rolodexmaint.bean.*;
import edu.mit.coeus.routing.bean.RoutingBean;
import edu.mit.coeus.s2s.bean.OpportunityInfoBean;
import java.beans.*;
import java.util.Vector;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.Hashtable;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.utils.CoeusVector;
//import java.util.HashMap;

/**
 * The class used to hold the information of <code>Proposal Details</code>
 *
 * @author  Mukundan.C
 * @version 1.0
 * Created on March 10, 2003, 2:58 PM
 */

public class ProposalDevelopmentFormBean implements CoeusBean, java.io.Serializable {
    
    private static final String PROP_PROPOSAL_TYPE_CODE ="proposalTypeCode";
    private static final String PROP_STATUS_CODE ="statusCode";
    private static final String PROP_CREATION_STATUS_CODE ="creationStatusCode";
    private static final String PROP_TEMPLATE_FLAG ="templateFlag";
    private static final String BASE_PROP_NUMBER = "baseProposalNumber";
    private static final String ORGANIZATION_ID ="organizationId";
    private static final String PER_ORGANIZATION_ID ="performingOrganizationId";
    private static final String CURRENT_ACT_NUMBER = "currentAccountNumber";
    private static final String CURRENT_AWARD_NUMBER = "currentAwardNumber";
    private static final String TITLE = "title";
    private static final String SPONSOR_CODE = "sponsorCode";    
    private static final String AGENCY_PROGRAM_CODE = "agencyProgramCode";
    private static final String AGENCY_DIVISION_CODE = "agencyDivCode"; 
   //COEUSQA-3951
    private static final String AGENCY_ROUTING_IDENTIFIER = "agencyRoutingIdentifier";
    private static final String PREV_GG_TRACKID = "previousGGTrackingID";      
   //COEUSQA-3951
    private static final String SPONSOR_PROP_NUMBER = "sponsorProposalNumber";
    private static final String PRIME_SPONSOR_CODE = "primeSponsorCode";
    private static final String COOP_ACTIVITIES_FLAG = "intrCoopActivitiesFlag";
    private static final String COUNTRY_LIST = "intrCountrylist";
    private static final String OTHER_AGENCY_FLAG = "otherAgencyFlag";
    private static final String NOTICE_OPP_UNITY_CODE = "noticeOfOpportunitycode";
    private static final String PROG_ANNOUNCEMENT_NUMBER = "programAnnouncementNumber";
    private static final String PROG_ANNOUNCEMENT_TITLE = "programAnnouncementTitle";
    private static final String START_DATE = "requestStartDateInitial";
    private static final String END_DATE = "requestEndDateInitial";
    private static final String DURATION_MONTH = "durationMonth";
    private static final String NUMBER_COPIES = "numberCopies";
    private static final String DEAD_LINE_DATE = "deadLineDate";
    private static final String DEAD_LINE_TYPE = "deadLineType";
    private static final String MAIL_ADDRESS_ID = "mailingAddressId";
    private static final String MAILING_ADDRESS = "mailingAddress";
    private static final String MAIL_BY = "mailBy";
    private static final String MAIL_TYPE = "mailType";
    private static final String MAIL_DESCRIPTION = "mailDescription";
    private static final String CARRIES_CODE_TYPE = "carrierCodeType";
    private static final String CARRIER_CODE = "carrierCode";
    private static final String MAIL_ACCOUNT_NUMBER = "mailAccountNumber";
    private static final String OWNED_BY = "ownedBy";
    private static final String NSF_CODE = "nsfCode";
    private static final String PROP_ACTIVITY_TYPE_CODE ="proposalActivityTypeCode";
    private static final String PROP_SUBCONTRACT_FLAG ="subcontractFlag";
    private static final String CFDA_NUMBER = "cfdaNumber";
    private static final String CONTINUED_FROM = "continuedFrom";
    
    // Added for Case 2162  - adding Award Type - Start 
    private static final String ANTICIPIATED_AWARD_TYPE_CODE = "awardTypeCode";
    // Added for Case 2162  - adding Award Type - End
    
    //holds the proposal number
    private String proposalNumber;
    //holds the proposal type code
    private int proposalTypeCode;
    //holds the proposal type description
    private String proposalTypeDesc;
    //holds the status code
    private int statusCode = 1;
    //holds the creation status code
    private int creationStatusCode = 1;
    //holds the base proposal number
    private String baseProposalNumber;
    //holds the continued From
    private String continuedFrom;
    //holds the template flag
    private boolean templateFlag;
    //holds the organization id
    private String organizationId;
    //holds the performing organization id
    private String performingOrganizationId;
    //holds the current account number
    private String currentAccountNumber;
    //holds the current award number
    private String currentAwardNumber;
    //holds the title
    private String title;
    //holds the sponor code
    private String sponsorCode;
    //holds the sponsor name
    private String sponsorName;
    //holds the sponsor proposal number
    private String sponsorProposalNumber;
    //holds the prime sponsor code
    private String primeSponsorCode;
    //holds the prime sponsor name
    private String primeSponsorName;
    // holds the coopactivities flag
    private boolean intrCoopActivitiesFlag;
    // holds the country list
    private String intrCountrylist;
    //holds the other agency flag
    private boolean otherAgencyFlag;
    //holds the notice of opportunity
    private int noticeOfOpportunitycode;
    //holds notice Of Opportunity Description
    private String noticeOfOpportunityDescription;
    //holds the announcement number
    private String programAnnouncementNumber;
    //holds the announcement title
    private String programAnnouncementTitle;
    //holds the activity type code
    private int proposalActivityTypeCode;
    //holds the activity type description
    private String proposalActivityTypeDesc;
    //holds the request start date initial
    private Date requestStartDateInitial;
    //holds the request start date total
    private Date requestStartDateTotal;
    //holds the request end date initial
    private Date requestEndDateInitial;
    //holds the request end date total
    private Date requestEndDateTotal;
    //holds the duration month
    private int durationMonth;
    //holds the number copies
    private String numberCopies;
    //holds the deadline date
    private Date deadLineDate;
    //holds the deadline type
    private String deadLineType;
    //holds the mail address id
    private int mailingAddressId;
    //holds the mailing address name
    private String mailingAddressName;
    //holds the mail address
    private String mailingAddress;
    //holds the mail by
    private String mailBy;
    //holds the mail type
    private String mailType;
    //holds the carried code type
    private String carrierCodeType;
    //holds the carried code
    private String carrierCode;
    //holds the mail description
    private String mailDescription;
    //holds the mail account number
    private String mailAccountNumber;
    //holds the sub contract flag
    private boolean subcontractFlag;
    //holds the narrative status
    private String narrativeStatus = "N";
    //holds the budget status
    private String budgetStatus = "N";
    //holds the owned by
    private String ownedBy  = "000001";
    //holds the description of the unit number
    private String ownedByDesc;
    //holds the nsf code
    private String nsfCode;
    //holds nsf Desscription
    private String nsfCodeDescription;
    //holds the value to set the background color for proposal number
    private int proposalOverrideExists;
    //holds the organization form bean
    private OrganizationAddressFormBean orgAddressFormBean;
    //holds the organization form bean
    private OrganizationAddressFormBean performOrgAddressFormBean;
    //holds the rolodex from bean
    private RolodexDetailsBean rolodexDetailsBean;
    //Commented for case 2406 - Organization and Locations - start
    //holds vector location list
    //private Vector locationLists;
    //Commented for case 2406 - Organization and Locations - end
    //holds protocol keystudy personnel bean
    private Vector keyStudyPersonnelUnits;
    private Vector keyStudyPersonnel;
    private Vector kpUnits;
    //holds protocol investigators bean
    private Vector investigators;
    //holds protocol sciencecode bean
    private Vector scienceCode;
    //holds protocol others bean
    private Vector others;
    //holds protocol lead bean
    private Vector leadUnit;
    //holds create user
    private String createUser;
    //holds create timestamp
    private Timestamp createTimeStamp;
    //holds update user id
    private String updateUser;
    //holds update timestamp
    private Timestamp updateTimestamp;
    //holds account type
    private String acType;

     private PropertyChangeSupport propertySupport;

    private Vector investigatorQuestions; // question list for investigator type
    private Hashtable moreExplanation; // more explanation for the questions
    
    // holds data regarding proposal persons
    private Vector propPersons;
    
    //holds Proposal User Info
    private Vector userInfo;
    //holds UserRoles Info Bean
    private Vector userRolesInfoBean;
    //holds Proposal Roles Form Bean
    private Vector propRolesFormBean;
    //holds Proposal Special Review Form bean
    private Vector propSpecialReviewFormBean; 
    //holds Proposal Yes/No Questions List
    private Vector propYNQuestionList;
    //holds Proposal Yes/No Answer List
    private Vector propYNQAnswerList;
    //holds Proposal Yes/No Expanation List
    private Hashtable propYNQExplanationList;
    //holds createStatusDescription
    private String creationStatusDescription;
    //holds approvalMaps
    private CoeusVector approvalMaps;
    //holds Narrative Modules
    private CoeusVector narrativeModules;
    //holds all Narrative User rights
    private CoeusVector narrativeUserRights;
    
    private String cfdaNumber;
    
    private String agencyProgramCode;
    
    private String agencyDivCode;
 //COEUSQA-3951
    private String agencyRoutingIdentifier;
    
    private String previousGGTrackingID;    
//COEUSQA-3951  
    
    private boolean s2sOppSelected;

    private boolean s2sCandidate;
    
    private ProposalHierarchyBean proposalHierarchyBean;
    
    // Added for Case 2162  - adding Award Type - Start 
    private int awardTypeCode = 0;
    private String awardTypeDesc;
    // Added for Case 2162  - adding Award Type - End
    
    //Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - end
    private RoutingBean routingBean;
    //Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - end
    
    //Added for case 2406 - Organization and Locations - start
    private CoeusVector sites;
    //Added for case 2406 - Organization and Locations - end
    //JIRA COEUSDEV-61 - START - 1
    private OpportunityInfoBean opportunityInfoBean;
    //JIRA COEUSDEV-61 - END - 1
    /** Creates a new instance of PersonCustomElementsInfoBean */
    
    //QUESTIONNAIRE ENHANCEMENT STARTS
    private Vector propQuestionnaireAnswers;

    public String getAgencyRoutingIdentifier() {
        return agencyRoutingIdentifier;
    }
    
    public void setAgencyRoutingIdentifier(String newAgencyRoutingIdentifier) {  
        
          String oldAgencyRoutingIdentifier = agencyRoutingIdentifier;
            this.agencyRoutingIdentifier = newAgencyRoutingIdentifier;
            propertySupport.firePropertyChange(AGENCY_ROUTING_IDENTIFIER,
                oldAgencyRoutingIdentifier, agencyRoutingIdentifier);        
    }

    public String getPreviousGGTrackingID() {
        return previousGGTrackingID;
    }

    public void setPreviousGGTrackingID(String newPreviousGGTrackingID) {
        String oldPreviousGGTrackingID = previousGGTrackingID;
        this.previousGGTrackingID = newPreviousGGTrackingID;
        propertySupport.firePropertyChange(PREV_GG_TRACKID,
                oldPreviousGGTrackingID, previousGGTrackingID);  
    }

    public Vector getPropQuestionnaireAnswers() {
        return propQuestionnaireAnswers;
    }

    public void setPropQuestionnaireAnswers(Vector propQuestionnaireAnswers) {
        this.propQuestionnaireAnswers = propQuestionnaireAnswers;
    }
    //QUESTIONNAIRE ENHANCEMENT ENDS
    
    //ppc certify flag for key person starts
    private boolean keyPersonCertifyParam;

    public boolean isKeyPersonCertifyParam() {
        return keyPersonCertifyParam;
    }

    public void setKeyPersonCertifyParam(boolean keyPersonCertifyParam) {
        this.keyPersonCertifyParam = keyPersonCertifyParam;
    }
    
    //ppc certify flag for key person ends
     public ProposalDevelopmentFormBean() {
         propertySupport = new PropertyChangeSupport( this );
     }

    /**
     * Method used to add propertychange listener to the fields
     * @param listener PropertyChangeListener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    /**
     * Method used to remove propertychange listener
     * @param listener PropertyChangeListener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    // JM 11-21-2012 added COPIED_FROM_PROP_NUM field
    private String copiedFromPropNum;
    
    public String getCopiedFromPropNum() {
        return copiedFromPropNum;
    }

    public void setCopiedFromPropNum(String copiedFromPropNum) {
        this.copiedFromPropNum = copiedFromPropNum;
    }
    // JM END

    public String getProposalNumber() {
        return proposalNumber;
    }

    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }

    public int getProposalTypeCode() {
        return proposalTypeCode;
    }

    public void setProposalTypeCode(int newProposalTypeCode) {
        int oldProposalTypeCode = proposalTypeCode;
        this.proposalTypeCode = newProposalTypeCode;
        propertySupport.firePropertyChange(PROP_PROPOSAL_TYPE_CODE,
                oldProposalTypeCode, proposalTypeCode);
    }

    public String getProposalTypeDesc() {
        return proposalTypeDesc;
    }

    public void setProposalTypeDesc(String proposalTypeDesc) {
        this.proposalTypeDesc = proposalTypeDesc;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int newStatusCode) {
        int oldStatusCode = statusCode;
        this.statusCode = newStatusCode;
        propertySupport.firePropertyChange(PROP_STATUS_CODE,
               oldStatusCode, statusCode);
    }

    public int getCreationStatusCode() {
        return creationStatusCode;
    }

    public void setCreationStatusCode(int newCreationStatusCode) {
        int oldCreationStatusCode = creationStatusCode;
        this.creationStatusCode = newCreationStatusCode;
        propertySupport.firePropertyChange(PROP_CREATION_STATUS_CODE,
               oldCreationStatusCode, creationStatusCode);
    }

    public String getBaseProposalNumber() {
        return baseProposalNumber;
    }

    public void setBaseProposalNumber(String newBaseProposalNumber) {
        String oldBaseProposalNumber = baseProposalNumber;
        this.baseProposalNumber = newBaseProposalNumber;
        propertySupport.firePropertyChange(BASE_PROP_NUMBER,
               oldBaseProposalNumber, baseProposalNumber);
    }

    public String getContinuedFrom() {
        return continuedFrom;
    }

    public void setContinuedFrom(String continuedFrom) {
        String oldContinuedFrom = this.continuedFrom;
        this.continuedFrom = continuedFrom;
        propertySupport.firePropertyChange(CONTINUED_FROM, oldContinuedFrom, continuedFrom);
    }

    public boolean isTemplateFlag() {
        return templateFlag;
    }

    public void setTemplateFlag(boolean newTemplateFlag) {
        boolean oldTemplateFlag = templateFlag;
        this.templateFlag = newTemplateFlag;
        propertySupport.firePropertyChange(PROP_TEMPLATE_FLAG,
               oldTemplateFlag, templateFlag);
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String newOrganizationId) {
        String oldOrganizationId = organizationId;
        this.organizationId = newOrganizationId;
        propertySupport.firePropertyChange(ORGANIZATION_ID,
               oldOrganizationId, organizationId);
    }

    public String getPerformingOrganizationId() {
        return performingOrganizationId;
    }

    public void setPerformingOrganizationId(String newPerformingOrganizationId) {
        String oldPerformingOrganizationId = performingOrganizationId;
        this.performingOrganizationId = newPerformingOrganizationId;
        propertySupport.firePropertyChange(PER_ORGANIZATION_ID,
               oldPerformingOrganizationId, performingOrganizationId);
    }

    public String getCurrentAccountNumber() {
        return currentAccountNumber;
    }

    public void setCurrentAccountNumber(String newCurrentAccountNumber) {
        String oldCurrentAccountNumber = currentAccountNumber;
        this.currentAccountNumber = newCurrentAccountNumber;
        propertySupport.firePropertyChange(CURRENT_ACT_NUMBER,
               oldCurrentAccountNumber, currentAccountNumber);
    }

    public String getCurrentAwardNumber() {
        return currentAwardNumber;
    }

    public void setCurrentAwardNumber(String newCurrentAwardNumber) {
        String oldCurrentAwardNumber = currentAwardNumber;
        this.currentAwardNumber = newCurrentAwardNumber;
        propertySupport.firePropertyChange(CURRENT_AWARD_NUMBER,
               oldCurrentAwardNumber, currentAwardNumber);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String newTitle) {
        String oldTitle = title;
        this.title = newTitle;
        propertySupport.firePropertyChange(TITLE,oldTitle, title);
    }

    public String getSponsorCode() {
        return sponsorCode;
    }

    public void setSponsorCode(String newSponsorCode) {
        String oldSponsorCode = sponsorCode;
        this.sponsorCode = newSponsorCode;
        propertySupport.firePropertyChange(SPONSOR_CODE,
                oldSponsorCode, sponsorCode);
    }

     public String getPrimeSponsorCode() {
        return primeSponsorCode;
    }

    public void setPrimeSponsorCode(String newPrimeSponsorCode) {
        String oldPrimeSponsorCode = primeSponsorCode;
        this.primeSponsorCode = newPrimeSponsorCode;
        propertySupport.firePropertyChange(PRIME_SPONSOR_CODE,
                oldPrimeSponsorCode, primeSponsorCode);
    }

    public String getSponsorProposalNumber() {
        return sponsorProposalNumber;
    }

    public void setSponsorProposalNumber(String newSponsorProposalNumber) {
        String oldSponsorProposalNumber = sponsorProposalNumber;
        this.sponsorProposalNumber = newSponsorProposalNumber;
        propertySupport.firePropertyChange(SPONSOR_PROP_NUMBER,
                oldSponsorProposalNumber, sponsorProposalNumber);
    }

    public boolean isIntrCoopActivitiesFlag() {
        return intrCoopActivitiesFlag;
    }

    public void setIntrCoopActivitiesFlag(boolean newIntrCoopActivitiesFlag) {
        boolean oldIntrCoopActivitiesFlag = intrCoopActivitiesFlag;
        this.intrCoopActivitiesFlag = newIntrCoopActivitiesFlag;
        propertySupport.firePropertyChange(COOP_ACTIVITIES_FLAG,
                oldIntrCoopActivitiesFlag, intrCoopActivitiesFlag);
    }

    public String getIntrCountrylist() {
        return intrCountrylist;
    }

    public void setIntrCountrylist(String newIntrCountrylist) {
        String oldIntrCountrylist = intrCountrylist;
        this.intrCountrylist = newIntrCountrylist;
        propertySupport.firePropertyChange(COUNTRY_LIST,
                oldIntrCountrylist, intrCountrylist);
    }

    public boolean isOtherAgencyFlag() {
        return otherAgencyFlag;
    }

    public void setOtherAgencyFlag(boolean newOtherAgencyFlag) {
        boolean oldOtherAgencyFlag = otherAgencyFlag;
        this.otherAgencyFlag = newOtherAgencyFlag;
        propertySupport.firePropertyChange(OTHER_AGENCY_FLAG,
                oldOtherAgencyFlag, otherAgencyFlag);
    }

    public int getNoticeOfOpportunitycode() {
        return noticeOfOpportunitycode;
    }

    public void setNoticeOfOpportunitycode(int newNoticeOfOpportunitycode) {
        int oldNoticeOfOpportunitycode = noticeOfOpportunitycode;
        this.noticeOfOpportunitycode = newNoticeOfOpportunitycode;
        propertySupport.firePropertyChange(NOTICE_OPP_UNITY_CODE,
                oldNoticeOfOpportunitycode, noticeOfOpportunitycode);
    }

    public String getProgramAnnouncementNumber() {
        return programAnnouncementNumber;
    }

    public void setProgramAnnouncementNumber(String newProgramAnnouncementNumber) {
        String oldProgramAnnouncementNumber = programAnnouncementNumber;
        this.programAnnouncementNumber = newProgramAnnouncementNumber;
        propertySupport.firePropertyChange(PROG_ANNOUNCEMENT_NUMBER,
                oldProgramAnnouncementNumber, programAnnouncementNumber);
    }

    public String getProgramAnnouncementTitle() {
        return programAnnouncementTitle;
    }

    public void setProgramAnnouncementTitle(String newProgramAnnouncementTitle) {
        String oldProgramAnnouncementTitle = programAnnouncementTitle;
        this.programAnnouncementTitle = newProgramAnnouncementTitle;
        propertySupport.firePropertyChange(PROG_ANNOUNCEMENT_TITLE,
                oldProgramAnnouncementTitle, programAnnouncementTitle);
    }

    public int getProposalActivityTypeCode() {
        return proposalActivityTypeCode;
    }

    public void setProposalActivityTypeCode(int newProposalActivityTypeCode) {
        int oldProposalActivityTypeCode = proposalActivityTypeCode;
        this.proposalActivityTypeCode = newProposalActivityTypeCode;
        propertySupport.firePropertyChange(PROP_ACTIVITY_TYPE_CODE,
               oldProposalActivityTypeCode, proposalActivityTypeCode);
    }

    public String getProposalActivityTypeDesc() {
        return proposalActivityTypeDesc;
    }

    public void setProposalActivityTypeDesc(String proposalActivityTypeDesc) {
        this.proposalActivityTypeDesc = proposalActivityTypeDesc;
    }

    public Date getRequestStartDateInitial() {
        return requestStartDateInitial;
    }

    public void setRequestStartDateInitial(Date newRequestStartDateInitial) {
        Date oldRequestStartDateInitial = requestStartDateInitial;
        this.requestStartDateInitial = newRequestStartDateInitial;
        propertySupport.firePropertyChange(START_DATE,
               oldRequestStartDateInitial, requestStartDateInitial);
    }

    public Date getRequestStartDateTotal() {
        return requestStartDateTotal;
    }

    public void setRequestStartDateTotal(Date requestStartDateTotal) {
        this.requestStartDateTotal = requestStartDateTotal;
    }

    public Date getRequestEndDateInitial() {
        return requestEndDateInitial;
    }

    public void setRequestEndDateInitial(Date newRequestEndDateInitial) {
        Date oldRequestEndDateInitial = requestEndDateInitial;
        this.requestEndDateInitial = newRequestEndDateInitial;
        propertySupport.firePropertyChange(END_DATE,
               oldRequestEndDateInitial, requestEndDateInitial);
    }

    public Date getRequestEndDateTotal() {
        return requestEndDateTotal;
    }

    public void setRequestEndDateTotal(Date requestEndDateTotal) {
        this.requestEndDateTotal = requestEndDateTotal;
    }

    public int getDurationMonth() {
        return durationMonth;
    }

    public void setDurationMonth(int newDurationMonth) {
        int oldDurationMonth = durationMonth;
        this.durationMonth = newDurationMonth;
        propertySupport.firePropertyChange(DURATION_MONTH,
               oldDurationMonth, durationMonth);
    }

    public String getNumberCopies() {
        return numberCopies;
    }

    public void setNumberCopies(String newNumberCopies) {
        String oldNumberCopies = numberCopies;
        this.numberCopies = newNumberCopies;
        propertySupport.firePropertyChange(NUMBER_COPIES,
               oldNumberCopies, numberCopies);
    }

    public Date getDeadLineDate() {
        return deadLineDate;
    }

    public void setDeadLineDate(Date newDeadLineDate) {
        Date oldDeadLineDate = deadLineDate;
        this.deadLineDate = newDeadLineDate;
        propertySupport.firePropertyChange(DEAD_LINE_DATE,
               oldDeadLineDate, deadLineDate);
    }

    public String getDeadLineType() {
        return deadLineType;
    }

    public void setDeadLineType(String newDeadLineType) {
        String oldDeadLineType = deadLineType;
        this.deadLineType = newDeadLineType;
        propertySupport.firePropertyChange(DEAD_LINE_TYPE,
               oldDeadLineType, deadLineType);
    }

    public int getMailingAddressId() {
        return mailingAddressId;
    }

    public void setMailingAddressId(int newMailingAddressId) {
        int oldMailingAddressId = mailingAddressId;
        this.mailingAddressId = newMailingAddressId;
        propertySupport.firePropertyChange(MAIL_ADDRESS_ID,
               oldMailingAddressId, mailingAddressId);
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String newMailingAddress) {
        String oldMailingAddress = mailingAddress;
        this.mailingAddress = newMailingAddress;
        propertySupport.firePropertyChange(MAILING_ADDRESS,
               oldMailingAddress, mailingAddress);
    }

    public String getMailBy() {
        return mailBy;
    }

    public void setMailBy(String newMailBy) {
        String oldMailBy = mailBy;
        this.mailBy = newMailBy;
        propertySupport.firePropertyChange(MAIL_BY,
               oldMailBy, mailBy);
    }

    public String getMailType() {
        return mailType;
    }

    public void setMailType(String newMailType) {
        String oldMailType = mailType;
        this.mailType = newMailType;
        propertySupport.firePropertyChange(MAIL_TYPE,
               oldMailType, mailType);
    }

    public String getCarrierCodeType() {
        return carrierCodeType;
    }

    public void setCarrierCodeType(String newCarrierCodeType) {
        String oldCarrierCodeType = carrierCodeType;
        this.carrierCodeType = newCarrierCodeType;
        propertySupport.firePropertyChange(CARRIES_CODE_TYPE,
               oldCarrierCodeType, carrierCodeType);
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String newCarrierCode) {
        String oldCarrierCode = carrierCode;
        this.carrierCode = newCarrierCode;
        propertySupport.firePropertyChange(CARRIER_CODE,
               oldCarrierCode, carrierCode);
    }

    public String getMailDescription() {
        return mailDescription;
    }

    public void setMailDescription(String newMailDescription) {
        String oldMailDescription = mailDescription;
        this.mailDescription = newMailDescription;
        propertySupport.firePropertyChange(MAIL_DESCRIPTION,
               oldMailDescription, mailDescription);
    }

    public String getMailAccountNumber() {
        return mailAccountNumber;
    }

    public void setMailAccountNumber(String newMailAccountNumber) {
        String oldMailAccountNumber = mailAccountNumber;
        this.mailAccountNumber = newMailAccountNumber;
        propertySupport.firePropertyChange(MAIL_ACCOUNT_NUMBER,
               oldMailAccountNumber, mailAccountNumber);
    }

    public boolean isSubcontractFlag() {
        return subcontractFlag;
    }

    public void setSubcontractFlag(boolean newSubcontractFlag) {
        boolean oldSubcontractFlag = subcontractFlag;
        this.subcontractFlag = newSubcontractFlag;
        propertySupport.firePropertyChange(PROP_SUBCONTRACT_FLAG,
               oldSubcontractFlag, subcontractFlag);
    }

    public String getNarrativeStatus() {
        return narrativeStatus;
    }

    public void setNarrativeStatus(String narrativeStatus) {
        this.narrativeStatus = narrativeStatus;
    }

    public String getBudgetStatus() {
        return budgetStatus;
    }

    public void setBudgetStatus(String budgetStatus) {
        this.budgetStatus = budgetStatus;
    }

    public String getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(String newOwnedBy) {
        String oldOwnedBy = ownedBy;
        this.ownedBy = newOwnedBy;
        propertySupport.firePropertyChange(OWNED_BY,
               oldOwnedBy, ownedBy);
    }

    public String getNsfCode() {
        return nsfCode;
    }

    public void setNsfCode(String newNsfCode) {
        String oldNsfCode = nsfCode;
        this.nsfCode = newNsfCode;
        propertySupport.firePropertyChange(NSF_CODE,
               oldNsfCode, nsfCode);
    }
    
    

    public OrganizationAddressFormBean getOrganizationAddressFormBean(){
        return orgAddressFormBean;
    }

    public void setOrganizationAddressFormBean(OrganizationAddressFormBean orgAddressFormBean){
        this.orgAddressFormBean = orgAddressFormBean;
    }

    public RolodexDetailsBean getRolodexDetailsBean(){
        return rolodexDetailsBean;
    }

    public void setRolodexDetailsBean(RolodexDetailsBean rolodexDetailsBean){
        this.rolodexDetailsBean = rolodexDetailsBean;
    }

    public OrganizationAddressFormBean getPerOrganizationAddressFormBean(){
        return performOrgAddressFormBean;
    }

    public void setPerOrganizationAddressFormBean(OrganizationAddressFormBean performOrgAddressFormBean){
        this.performOrgAddressFormBean = performOrgAddressFormBean;
    }
    
    //Commented for case 2406 - Organization and Locations - start
//    public Vector getLocationLists(){
//        return locationLists;
//    }
//
//    public void setLocationLists(Vector locationLists){
//        this.locationLists = locationLists;
//    }
    //Commented for case case 2406 - Organization and Locations - end
    
     public Vector getInvestigators(){
        return investigators;
    }

    public void setInvestigators(Vector investigators){
        this.investigators = investigators;
    }

    public Vector getKeyStudyPersonnel(){
        return keyStudyPersonnel;
    }

    public void setKeyStudyPersonnel(Vector keyStudyPersonnel){
        this.keyStudyPersonnel = keyStudyPersonnel;
    }

    public Vector getScienceCode(){
        return scienceCode;
    }

    public void setScienceCode(Vector scienceCode){
        this.scienceCode = scienceCode;
    }

    public Vector getOthers(){
        return others;
    }

    public void setOthers(Vector others){
        this.others = others;
    }

    public Vector getLeadUnit(){
        return leadUnit;
    }

    public void setLeadUnit(Vector leadUnit){
        this.leadUnit = leadUnit;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Timestamp getCreateTimeStamp() {
        return createTimeStamp;
    }

    public void setCreateTimeStamp(Timestamp createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public String getAcType() {
        return acType;
    }

    public void setAcType(String acType) {
        this.acType = acType;
    }

    /** Getter for property mailingAddressName.
     * @return Value of property mailingAddressName.
     */
    public String getMailingAddressName() {
        return mailingAddressName;
    }

    /** Setter for property mailingAddressName.
     * @param mailingAddressName New value of property mailingAddressName.
     */
    public void setMailingAddressName(String mailingAddressName) {
        this.mailingAddressName = mailingAddressName;
    }
    public void setInvestigatorQuestions(Vector investigatorQuestions){
    		this.investigatorQuestions = investigatorQuestions;
	}
	public Vector getInvestigatorQuestions(){
		return investigatorQuestions;
	}
	/*
 	 * Hashtable contains key = question id + explanation type
 	 * where explanation type will be
 	 * 'E' for Explanation,
 	 * 'P' for Policy and
 	 * 'R' for Regulation.
	 * value = explanation
	*/
    public void setMoreExplanation(Hashtable moreExplanation){
		this.moreExplanation = moreExplanation;
	}
	public Hashtable getMoreExplanation(){
		return moreExplanation;
	}

        /** Getter for property ownedByDesc.
         * @return Value of property ownedByDesc.
         */
        public String getOwnedByDesc() {
            return ownedByDesc;
        }        
    
        /** Setter for property ownedByDesc.
         * @param ownedByDesc New value of property ownedByDesc.
         */
        public void setOwnedByDesc(String ownedByDesc) {
            this.ownedByDesc = ownedByDesc;
        }
        
        /** Getter for property proposalOverrideExists.
         * @return Value of property proposalOverrideExists.
         */
        public int getProposalOverrideExists() {
            return proposalOverrideExists;
        }
        
        /** Setter for property proposalOverrideExists.
         * @param proposalOverrideExists New value of property proposalOverrideExists.
         */
        public void setProposalOverrideExists(int proposalOverrideExists) {
            this.proposalOverrideExists = proposalOverrideExists;
        }
        
        /** Getter for property sponsorName.
         * @return Value of property sponsorName.
         */
        public java.lang.String getSponsorName() {
            return sponsorName;
        }
        
        /** Setter for property sponsorName.
         * @param sponsorName New value of property sponsorName.
         */
        public void setSponsorName(java.lang.String sponsorName) {
            this.sponsorName = sponsorName;
        }
        
        /** Getter for property propPersons.
         * @return Value of property propPersons.
         */
        public Vector getPropPersons() {
            return propPersons;
        }
        
        /** Setter for property propPersons.
         * @param propPersons New value of property propPersons.
         */
        public void setPropPersons(Vector propPersons) {
            this.propPersons = propPersons;
        }

        /** Getter for property userInfo.
         * @return Value of property userInfo.
         */
        public Vector getUserInfo() {
            return userInfo;
        }
        
        /** Setter for property userInfo.
         * @param userInfo New value of property userInfo.
         */
        public void setUserInfo(Vector userInfo) {
            this.userInfo = userInfo;
        }        

        /** Getter for property userRolesInfoBean.
         * @return Value of property userRolesInfoBean.
         */
        public Vector getUserRolesInfoBean() {
            return userRolesInfoBean;
        }
        
        /** Setter for property userRolesInfoBean.
         * @param userRolesInfoBean New value of property userRolesInfoBean.
         */
        public void setUserRolesInfoBean(Vector userRolesInfoBean) {
            this.userRolesInfoBean = userRolesInfoBean;
        }        

        /** Getter for property propRolesFormBean.
         * @return Value of property propRolesFormBean.
         */
        public Vector getPropRolesFormBean() {
            return propRolesFormBean;
        }
        
        /** Setter for property propRolesFormBean.
         * @param propRolesFormBean New value of property propRolesFormBean.
         */
        public void setPropRolesFormBean(Vector propRolesFormBean) {
            this.propRolesFormBean = propRolesFormBean;
        }                
        
        /** Getter for property propSpecialReviewFormBean.
         * @return Value of property propSpecialReviewFormBean.
         */
        public Vector getPropSpecialReviewFormBean() {
            return propSpecialReviewFormBean;
        }
        
        /** Setter for property propSpecialReviewFormBean.
         * @param propSpecialReviewFormBean New value of property propSpecialReviewFormBean.
         */
        public void setPropSpecialReviewFormBean(Vector propSpecialReviewFormBean) {
            this.propSpecialReviewFormBean = propSpecialReviewFormBean;
        }   
        
        /** Getter for property propYNQuestionList.
         * @return Vector of property YNQBean.
         */
        public Vector getPropYNQuestionList() {
            return propYNQuestionList;
        }
        
        /** Setter for property propYNQuestionList.
         * @param propSpecialReviewFormBean New value of property propYNQuestionList.
         */
        public void setPropYNQuestionList(Vector propYNQuestionList) {
            this.propYNQuestionList = propYNQuestionList;
        }   
        
        /** Getter for property propYNQAnswerList.
         * @return Vector of proposalYNQBean.
         */
        public Vector getPropYNQAnswerList() {
            return propYNQAnswerList;
        }
        
        /** Setter for property propYNQAnswerList.
         * @param propYNQAnswerList New value of property propYNQAnswerList.
         */
        public void setPropYNQAnswerList(Vector propYNQAnswerList) {
            this.propYNQAnswerList = propYNQAnswerList;
        }   

        /** Getter for property propYNQExplanationList.
         * @return Vector of proposalYNQExplantionFormBean.
         */
        public Hashtable getPropYNQExplanationList() {
            return propYNQExplanationList;
        }
        
        /** Setter for property propYNQAnswerList.
         * @param propYNQAnswerList New value of property propYNQExplanationList.
         */
        public void setPropYNQExplanationList(Hashtable propYNQExplanationList) {
            this.propYNQExplanationList = propYNQExplanationList;
        }
        
        public boolean isLike(edu.mit.coeus.bean.ComparableBean comparableBean) throws edu.mit.coeus.exception.CoeusException{
            return true;
        }
        
        /** Equals Implementation
         * @param object
         * @return  boolean 
         */        
        public boolean equals(Object object){
            ProposalDevelopmentFormBean proposalDevelopmentFormBean = (ProposalDevelopmentFormBean)object;
            if(proposalDevelopmentFormBean.getProposalNumber().equals(getProposalNumber())){
                return true;
            }else{
                return false;
            }
        }        
        
        /** Getter for property creationStatusDescription.
         * @return Value of property creationStatusDescription.
         */
        public java.lang.String getCreationStatusDescription() {
            return creationStatusDescription;
        }
        
        /** Setter for property creationStatusDescription.
         * @param creationStatusDescription New value of property creationStatusDescription.
         */
        public void setCreationStatusDescription(java.lang.String creationStatusDescription) {
            this.creationStatusDescription = creationStatusDescription;
        }
        
        /** Getter for property approvalMaps.
         * @return Value of property approvalMaps.
         */
        public CoeusVector getApprovalMaps() {
            return approvalMaps;
        }
        
        /** Setter for property approvalMaps.
         * @param approvalMaps New value of property approvalMaps.
         */
        public void setApprovalMaps(CoeusVector approvalMaps) {
            this.approvalMaps = approvalMaps;
        }        
        
        /** Getter for property nsfCodeDescription.
         * @return Value of property nsfCodeDescription.
         */
        public java.lang.String getNsfCodeDescription() {
            return nsfCodeDescription;
        }
        
        /** Setter for property nsfCodeDescription.
         * @param nsfCodeDescription New value of property nsfCodeDescription.
         */
        public void setNsfCodeDescription(java.lang.String nsfCodeDescription) {
            this.nsfCodeDescription = nsfCodeDescription;
        }
        
        /** Getter for property noticeOfOpportunityDescription.
         * @return Value of property noticeOfOpportunityDescription.
         */
        public java.lang.String getNoticeOfOpportunityDescription() {
            return noticeOfOpportunityDescription;
        }
        
        /** Setter for property noticeOfOpportunityDescription.
         * @param noticeOfOpportunityDescription New value of property noticeOfOpportunityDescription.
         */
        public void setNoticeOfOpportunityDescription(java.lang.String noticeOfOpportunityDescription) {
            this.noticeOfOpportunityDescription = noticeOfOpportunityDescription;
        }
        
        /** Getter for property narrativeModules.
         * @return Value of property narrativeModules.
         */
        public edu.mit.coeus.utils.CoeusVector getNarrativeModules() {
            return narrativeModules;
        }
        
        /** Setter for property narrativeModules.
         * @param narrativeModules New value of property narrativeModules.
         */
        public void setNarrativeModules(edu.mit.coeus.utils.CoeusVector narrativeModules) {
            this.narrativeModules = narrativeModules;
        }
        
        /** Getter for property narrativeUserRights.
         * @return Value of property narrativeUserRights.
         */
        public edu.mit.coeus.utils.CoeusVector getNarrativeUserRights() {
            return narrativeUserRights;
        }
        
        /** Setter for property narrativeUserRights.
         * @param narrativeUserRights New value of property narrativeUserRights.
         */
        public void setNarrativeUserRights(edu.mit.coeus.utils.CoeusVector narrativeUserRights) {
            this.narrativeUserRights = narrativeUserRights;
        }        

        /** Getter for property cfdaNumber.
         * @return Value of property cfdaNumber.
         */
        public java.lang.String getCfdaNumber() {
            return cfdaNumber;
        }

        /** Setter for property cfdaNumber.
         * @param cfdaNumber New value of property cfdaNumber.
         */
        public void setCfdaNumber(java.lang.String newCfdaNumber) {
            String oldCfdaNumber = cfdaNumber;
            this.cfdaNumber = newCfdaNumber;
            propertySupport.firePropertyChange(CFDA_NUMBER,
                   oldCfdaNumber, cfdaNumber);
        }
        /*Start of the enhancement, Proposal Development Details window layout Change Case#1622*/

        /**
         * Getter for property agencyProgramCode.
         * @return Value of property agencyProgramCode.
         */
        public java.lang.String getAgencyProgramCode() {
            return agencyProgramCode;
        }        
        
        /**
         * Setter for property agencyProgramCode.
         * @param agencyProgramCode New value of property agencyProgramCode.
         */
        public void setAgencyProgramCode(java.lang.String newAgencyProgramCode) {
            String oldAgencyProgramCode = agencyProgramCode;
            this.agencyProgramCode = newAgencyProgramCode;
            propertySupport.firePropertyChange(AGENCY_PROGRAM_CODE,
                oldAgencyProgramCode, agencyProgramCode);
        }
        
        /**
         * Getter for property agencyDivCode.
         * @return Value of property agencyDivCode.
         */
        public java.lang.String getAgencyDivCode() {
            return agencyDivCode;
        }
        
        /**
         * Setter for property agencyDivCode.
         * @param agencyDivCode New value of property agencyDivCode.
         */
        public void setAgencyDivCode(java.lang.String newAgencyDivCode) {
            String oldAgencyDivCode = agencyDivCode;
            this.agencyDivCode = newAgencyDivCode;
            propertySupport.firePropertyChange(AGENCY_DIVISION_CODE,
                oldAgencyDivCode, agencyDivCode);
        }
        
        /**
         * Getter for property proposalHierarchyBean.
         * @return Value of property proposalHierarchyBean.
         */
        public edu.mit.coeus.propdev.bean.ProposalHierarchyBean getProposalHierarchyBean() {
            return proposalHierarchyBean;
        }        
      
        /**
         * Setter for property proposalHierarchyBean.
         * @param proposalHierarchyBean New value of property proposalHierarchyBean.
         */
        public void setProposalHierarchyBean(edu.mit.coeus.propdev.bean.ProposalHierarchyBean proposalHierarchyBean) {
            this.proposalHierarchyBean = proposalHierarchyBean;
        }        
        
        /**
         * Getter for property s2sOppSelected.
         * @return Value of property s2sOppSelected.
         */
        public boolean isS2sOppSelected() {
            return s2sOppSelected;
        }
        
        /**
         * Setter for property s2sOppSelected.
         * @param s2sOppSelected New value of property s2sOppSelected.
         */
        public void setS2sOppSelected(boolean s2sOppSelected) {
            this.s2sOppSelected = s2sOppSelected;
        }
        
        /**
         * Getter for property s2sCandidate.
         * @return Value of property s2sCandidate.
         */
        public boolean isS2sCandidate() {
            return s2sCandidate;
        }
        
        /**
         * Setter for property s2sCandidate.
         * @param s2sCandidate New value of property s2sCandidate.
         */
        public void setS2sCandidate(boolean s2sCandidate) {
            this.s2sCandidate = s2sCandidate;
        }
        
        /*End of the enhancement, Proposal Development Details window layout Change Case#1622*/

        // Added for Case 2162  - adding Award Type - Start 
        public int getAwardTypeCode() {
            return awardTypeCode;
        }

        public void setAwardTypeCode(int newAwardTypeCode) {
            int oldAwardTypeCode = awardTypeCode;
            this.awardTypeCode = newAwardTypeCode;
        propertySupport.firePropertyChange(ANTICIPIATED_AWARD_TYPE_CODE,
                oldAwardTypeCode, awardTypeCode);
        }
       
        public String getAwardTypeDesc() {
            return awardTypeDesc;
        }

        public void setAwardTypeDesc(String awardTypeDesc) {
            this.awardTypeDesc = awardTypeDesc;
        }
       // Added for Case 2162  - adding Award Type - End

    public RoutingBean getRoutingBean() {
        return routingBean;
    }

    public void setRoutingBean(RoutingBean routingBean) {
        this.routingBean = routingBean;
    }
    //Added for case 2406 - Organization and Locations - start
    public CoeusVector getSites() {
        return sites;
    }

    public void setSites(CoeusVector sites) {
        this.sites = sites;
    }
    //Added for case 2406 - Organization and Locations - end
    //JIRA COEUSDEV-61 - START - 2
    /**
     * @return the opportunityInfoBean
     */
    public OpportunityInfoBean getOpportunityInfoBean() {
        return opportunityInfoBean;
    }

    /**
     * @param opportunityInfoBean the opportunityInfoBean to set
     */
    public void setOpportunityInfoBean(OpportunityInfoBean opportunityInfoBean) {
        this.opportunityInfoBean = opportunityInfoBean;
    }


    /**
     * @return the keyStudyPersonnelUnits
     */
    public Vector getKeyStudyPersonnelUnits() {
        return keyStudyPersonnelUnits;
    }

    /**
     * @param keyStudyPersonnelUnits the keyStudyPersonnelUnits to set
     */
    public void setKeyStudyPersonnelUnits(Vector keyStudyPersonnelUnits) {
        this.keyStudyPersonnelUnits = keyStudyPersonnelUnits;
    }


    /**
     * @return the kpUnits
     */
    public Vector getKpUnits() {
        return kpUnits;
    }

    /**
     * @param kpUnits the kpUnits to set
     */
    public void setKpUnits(Vector kpUnits) {
        this.kpUnits = kpUnits;
    }

    /**
     * Method to get prime sponsoe name
     * @return primeSponsorName
     */
    public String getPrimeSponsorName() {
        return primeSponsorName;
    }

    /**
     * Method to set prime sponsor name
     * @param primeSponsorName 
     */
    public void setPrimeSponsorName(String primeSponsorName) {
        this.primeSponsorName = primeSponsorName;
    }

    
}