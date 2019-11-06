/*
 * @(#)ProposalDevelopmentUpdateTxnBean.java 1.0 03/17/03 5:00 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 16-SEP-2011
 * by Maharaja Palanichamy
 */

package edu.mit.coeus.propdev.bean;

import edu.mit.coeus.s2s.bean.S2STxnBean;
import edu.mit.coeus.utils.MailActions;
import edu.mit.coeus.routing.bean.RoutingDetailsBean;
import edu.mit.coeus.routing.bean.RoutingMapBean;
import edu.mit.coeus.routing.bean.RoutingUpdateTxnBean;
import edu.mit.coeus.s2s.bean.S2SSubmissionDataTxnBean;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexMaintenanceDataTxnBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.bean.CoeusDataTxnBean;
import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.bean.KeyPersonUnitBean;
import edu.mit.coeus.bean.SpecialReviewFormBean;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.utils.ObjectCloner;
import edu.mit.coeus.departmental.bean.DepartmentPerDegreeFormBean;
import edu.mit.coeus.instprop.bean.InstituteProposalSpecialReviewBean;
import edu.mit.coeus.instprop.bean.InstituteProposalUpdateTxnBean;
import edu.mit.coeus.irb.bean.ProtocolDataTxnBean;
import edu.mit.coeus.irb.bean.ProtocolFundingSourceBean;
import edu.mit.coeus.irb.bean.ProtocolInfoBean;
import edu.mit.coeus.irb.bean.ProtocolLinkBean;
import edu.mit.coeus.irb.bean.ProtocolNotepadBean;
import edu.mit.coeus.irb.bean.ProtocolSpecialReviewFormBean;
import edu.mit.coeus.irb.bean.ProtocolUpdateTxnBean;
import edu.mit.coeus.mail.MailHandler;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mail.bean.PersonRecipientBean;
import edu.mit.coeus.s2s.bean.DBOpportunityInfoBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import java.util.Hashtable;
import java.util.Enumeration;
import java.sql.Date;
import java.util.Vector;
import java.sql.Timestamp;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Iterator;

/**
 * This class provides the methods for performing modify/insert and delete
 * procedure executions for a Proposal Development functionality. Various methods
 * are used to modify/insert the data for "ProposalDevelopmentFormBean" from the
 * Database.All methods are used <code>DBEngineImpl</code> instance for the
 * database interaction.
 *
 * @version 1.0 March 17, 2003, 5:00 PM
 * @author  Mukundan C
 */

public class ProposalDevelopmentUpdateTxnBean {

    // instance of a dbEngine
    private DBEngineImpl dbEngine;

    private final char SAVE_NEW_AMENDMENT = 'n';
    private final char SAVE_NEW_REVISION = 'r';
    private final char SAVE_PROP_TO_PROT = '8';
    private char functionType;


//    private ProposalDevelopmentUpdateTxnBean proposalDevelopmentUpdateTxnBean;
    // holds the dataset name
    private static final String DSN = "Coeus";
    // holds the charater for modify
    //private static final String MODIFY = "U";
    // holds the charater for insert
    public static final int PROTOCOL_COORDINATOR_ID = 200 ;
    private static final String INSERT = "I";
    // holds the userId for the logged in user
    private String userId;

    private TransactionMonitor transMon;
    private static final String rowLockStr = "osp$Development Proposal_";
    private static final String routingLockStr = "osp$Proposal Routing_";
    private static final int PERSON_REMOVAL_MAIL_ACTION_ID=900;
    //Update Time Stamp - Added on 31/12/2003 - start
    private Timestamp dbTimestamp;
    //Update Time Stamp - Added on 31/12/2003 - end

//    private static final int APPROVER_ROLE_ID = 101;

    //private ProposalDevelopmentUpdateTxnBean proposalDevelopmentUpdateTxnBean;
    /** Creates a new instance of ProposalDevelopmentUpdateTxnBean */
    public ProposalDevelopmentUpdateTxnBean() {
        dbEngine = new DBEngineImpl();
    }

    /**
     * Creates new ProposalDevelopmentUpdateTxnBean .
     * @param userId String Loggedin userid
     */
    public ProposalDevelopmentUpdateTxnBean(String userId) throws DBException {
        this.userId = userId;
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        //Coeus Enhancement: Case #1799 start
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        //Coeus Enhancement: Case #1799 end
    }

    /**
     * Method used to update/insert/delete all the details of a Proposal Details
     * related ProposalKeyStatus,ProposalInvestigator,Locations,Sciencecode,
     * Leadunit and Others.
     * <li>To fetch the data, it uses DW_UPDATE_PROPOSAL procedure.
     *
     * @param ProposalDevelopmentFormBean this bean contains data for
     * insert/modify for proposal Info.
     * @return boolean this holds true for successfull insert/modify or
     * false if fails.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean addUpdProposalDevelopment( ProposalDevelopmentFormBean
            proposalDevelopmentFormBean)  throws CoeusException, DBException, ParseException{
        boolean success = false;
        Vector procedures = new Vector(5,3);

        Vector paramProposal= new Vector();
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        //proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(userId); 
        String proposalNumber = proposalDevelopmentFormBean.getProposalNumber();
        paramProposal.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        paramProposal.addElement(new Parameter("PROPOSAL_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+proposalDevelopmentFormBean.getProposalTypeCode()));
        paramProposal.addElement(new Parameter("STATUS_CODE",
                DBEngineConstants.TYPE_INT,
                ""+proposalDevelopmentFormBean.getStatusCode()));
        paramProposal.addElement(new Parameter("CREATION_STATUS_CODE",
                DBEngineConstants.TYPE_INT,
                ""+proposalDevelopmentFormBean.getCreationStatusCode()));
        paramProposal.addElement(new Parameter("BASE_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.getBaseProposalNumber()));
        paramProposal.addElement(new Parameter("CONTINUED_FROM",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.getContinuedFrom()));
        paramProposal.addElement(new Parameter("TEMPLATE_FLAG",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.isTemplateFlag()? "Y": "N") );
        //Commented for case 2406 - Organization and Location - start
//        paramProposal.addElement(new Parameter("ORGANIZATION_ID",
//                DBEngineConstants.TYPE_STRING,
//                proposalDevelopmentFormBean.getOrganizationId()));
//        paramProposal.addElement(new Parameter("PERFORMING_ORGANIZATION_ID",
//                DBEngineConstants.TYPE_STRING,
//                proposalDevelopmentFormBean.getPerformingOrganizationId()));
        //Commented for case 2406 - Organization and Location - start
        paramProposal.addElement(new Parameter("CURRENT_ACCOUNT_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.getCurrentAccountNumber()));
        paramProposal.addElement(new Parameter("CURRENT_AWARD_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.getCurrentAwardNumber()));
        paramProposal.addElement(new Parameter("TITLE",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.getTitle()));
        paramProposal.addElement(new Parameter("CFDA_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.getCfdaNumber()));
        paramProposal.addElement(new Parameter("SPONSOR_CODE",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.getSponsorCode()));
        paramProposal.addElement(new Parameter("PRIME_SPONSOR_CODE",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.getPrimeSponsorCode()));
        paramProposal.addElement(new Parameter("SPONSOR_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.getSponsorProposalNumber()));
        paramProposal.addElement(new Parameter("INTR_COOP_ACTIVITIES_FLAG",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.isIntrCoopActivitiesFlag()? "Y": "N") );
        paramProposal.addElement(new Parameter("INTR_COUNTRY_LIST",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.getIntrCountrylist()));
        paramProposal.addElement(new Parameter("OTHER_AGENCY_FLAG",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.isOtherAgencyFlag()? "Y": "N") );
        paramProposal.addElement(new Parameter("NOTICE_OF_OPPORTUNITY_CODE",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.getNoticeOfOpportunitycode() == 0 ? null :
                    ""+proposalDevelopmentFormBean.getNoticeOfOpportunitycode() ));
        paramProposal.addElement(new Parameter("PROGRAM_ANNOUNCEMENT_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.getProgramAnnouncementNumber()));
        paramProposal.addElement(new Parameter("PROGRAM_ANNOUNCEMENT_TITLE",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.getProgramAnnouncementTitle()));
        paramProposal.addElement(new Parameter("ACTIVITY_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+proposalDevelopmentFormBean.getProposalActivityTypeCode()));
        paramProposal.addElement(new Parameter("REQUESTED_START_DATE_INIT",
                DBEngineConstants.TYPE_DATE,
                proposalDevelopmentFormBean.getRequestStartDateInitial()));
        paramProposal.addElement(new Parameter("REQUESTED_START_DATE_TOTAL",
                DBEngineConstants.TYPE_DATE,
                proposalDevelopmentFormBean.getRequestStartDateTotal()));
        paramProposal.addElement(new Parameter("REQUESTED_END_DATE_INIT",
                DBEngineConstants.TYPE_DATE,
                proposalDevelopmentFormBean.getRequestEndDateInitial()));
        paramProposal.addElement(new Parameter("REQUESTED_END_DATE_TOTAL",
                DBEngineConstants.TYPE_DATE,
                proposalDevelopmentFormBean.getRequestEndDateTotal()));
        paramProposal.addElement(new Parameter("DURATION_MONTHS",
                DBEngineConstants.TYPE_INT,
                ""+proposalDevelopmentFormBean.getDurationMonth()));
        paramProposal.addElement(new Parameter("NUMBER_OF_COPIES",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.getNumberCopies()));
        paramProposal.addElement(new Parameter("DEADLINE_DATE",
                DBEngineConstants.TYPE_DATE,
                proposalDevelopmentFormBean.getDeadLineDate()));
        paramProposal.addElement(new Parameter("DEADLINE_TYPE",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.getDeadLineType()));

        if(proposalDevelopmentFormBean.getMailingAddressId() != -1) {
            paramProposal.addElement(new Parameter("MAILING_ADDRESS_ID",
                    DBEngineConstants.TYPE_INT,
                    ""+proposalDevelopmentFormBean.getMailingAddressId()));
        }else {
            paramProposal.addElement(new Parameter("MAILING_ADDRESS_ID",
                    DBEngineConstants.TYPE_STRING, null));
        }

        paramProposal.addElement(new Parameter("MAIL_BY",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.getMailBy()));
        paramProposal.addElement(new Parameter("MAIL_TYPE",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.getMailType()));
        paramProposal.addElement(new Parameter("CARRIER_CODE_TYPE",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.getCarrierCodeType()));
        paramProposal.addElement(new Parameter("CARRIER_CODE",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.getCarrierCode()));
        paramProposal.addElement(new Parameter("MAIL_DESCRIPTION",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.getMailDescription()));
        paramProposal.addElement(new Parameter("MAIL_ACCOUNT_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.getMailAccountNumber()));
        paramProposal.addElement(new Parameter("SUBCONTRACT_FLAG",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.isSubcontractFlag()? "Y": "N") );
        //No need to Update Budget Status and Narrative Status - April 15, 2004
        /*paramProposal.addElement(new Parameter("NARRATIVE_STATUS",
                    DBEngineConstants.TYPE_STRING,
                            proposalDevelopmentFormBean.getNarrativeStatus()));
        paramProposal.addElement(new Parameter("BUDGET_STATUS",
                    DBEngineConstants.TYPE_STRING,
                            proposalDevelopmentFormBean.getBudgetStatus()));*/
        paramProposal.addElement(new Parameter("OWNED_BY_UNIT",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.getOwnedBy()));
        paramProposal.addElement(new Parameter("CREATE_USER",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.getCreateUser()));
        paramProposal.addElement(new Parameter("NSF_CODE",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.getNsfCode()));
        paramProposal.addElement(new Parameter("AGENCY_PROGRAM_CODE",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.getAgencyProgramCode()));
        paramProposal.addElement(new Parameter("AGENCY_DIVISION_CODE",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.getAgencyDivCode()));      
        
      //COEUSQA-3951         
        paramProposal.addElement(new Parameter("AGENCY_ROUTING_IDENTIFIER",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.getAgencyRoutingIdentifier()));
        paramProposal.addElement(new Parameter("PREV_GG_TRACKID",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.getPreviousGGTrackingID()));        
       //COEUSQA-3951  
        
        // Added for Case 2162  - adding Award Type - Start
        paramProposal.addElement(new Parameter("AWARD_TYPE_CODE",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.getAwardTypeCode()== 0 ? null :
                    ""+proposalDevelopmentFormBean.getAwardTypeCode() ));
        // Added for Case 2162  - adding Award Type - End
        paramProposal.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramProposal.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramProposal.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.getProposalNumber()));
        paramProposal.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                proposalDevelopmentFormBean.getUpdateTimestamp()));
        paramProposal.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                proposalDevelopmentFormBean.getAcType()));

        StringBuffer sqlProposal = new StringBuffer(
                "call UPDATE_PROPOSAL(");
        sqlProposal.append(" <<PROPOSAL_NUMBER>> , ");
        sqlProposal.append(" <<PROPOSAL_TYPE_CODE>> , ");
        sqlProposal.append(" <<STATUS_CODE>> , ");
        sqlProposal.append(" <<CREATION_STATUS_CODE>> , ");
        sqlProposal.append(" <<BASE_PROPOSAL_NUMBER>> , ");
        sqlProposal.append(" <<CONTINUED_FROM>> , ");
        sqlProposal.append(" <<TEMPLATE_FLAG>> , ");
        //Commented for case 2406 - Organization and Location - start
//        sqlProposal.append(" <<ORGANIZATION_ID>> , ");
//        sqlProposal.append(" <<PERFORMING_ORGANIZATION_ID>> , ");
        //Commented for case 2406 - Organization and Location - end
        sqlProposal.append(" <<CURRENT_ACCOUNT_NUMBER>> , ");
        sqlProposal.append(" <<CURRENT_AWARD_NUMBER>> , ");
        sqlProposal.append(" <<TITLE>> , ");
        sqlProposal.append(" <<CFDA_NUMBER>> , ");
        sqlProposal.append(" <<SPONSOR_CODE>> , ");
        sqlProposal.append(" <<PRIME_SPONSOR_CODE>> , ");
        sqlProposal.append(" <<SPONSOR_PROPOSAL_NUMBER>> , ");
        sqlProposal.append(" <<INTR_COOP_ACTIVITIES_FLAG>> , ");
        sqlProposal.append(" <<INTR_COUNTRY_LIST>> , ");
        sqlProposal.append(" <<OTHER_AGENCY_FLAG>> , ");
        sqlProposal.append(" <<NOTICE_OF_OPPORTUNITY_CODE>> , ");
        sqlProposal.append(" <<PROGRAM_ANNOUNCEMENT_NUMBER>> , ");
        sqlProposal.append(" <<PROGRAM_ANNOUNCEMENT_TITLE>> , ");
        sqlProposal.append(" <<ACTIVITY_TYPE_CODE>> , ");
        sqlProposal.append(" <<REQUESTED_START_DATE_INIT>> , ");
        sqlProposal.append(" <<REQUESTED_START_DATE_TOTAL>> , ");
        sqlProposal.append(" <<REQUESTED_END_DATE_INIT>> , ");
        sqlProposal.append(" <<REQUESTED_END_DATE_TOTAL>> , ");
        sqlProposal.append(" <<DURATION_MONTHS>> , ");
        sqlProposal.append(" <<NUMBER_OF_COPIES>> , ");
        sqlProposal.append(" <<DEADLINE_DATE>> , ");
        sqlProposal.append(" <<DEADLINE_TYPE>> , ");
        sqlProposal.append(" <<MAILING_ADDRESS_ID>> , ");
        sqlProposal.append(" <<MAIL_BY>> , ");
        sqlProposal.append(" <<MAIL_TYPE>> , ");
        sqlProposal.append(" <<CARRIER_CODE_TYPE>> , ");
        sqlProposal.append(" <<CARRIER_CODE>> , ");
        sqlProposal.append(" <<MAIL_DESCRIPTION>> , ");
        sqlProposal.append(" <<MAIL_ACCOUNT_NUMBER>> , ");
        sqlProposal.append(" <<SUBCONTRACT_FLAG>> , ");
        //No need to Update Budget Status and Narrative Status - April 15, 2004
        //sqlProposal.append(" <<NARRATIVE_STATUS>> , ");
        //sqlProposal.append(" <<BUDGET_STATUS>> , ");
        sqlProposal.append(" <<OWNED_BY_UNIT>> , ");
        sqlProposal.append(" <<CREATE_USER>> , ");
        sqlProposal.append(" <<UPDATE_USER>> , ");
        sqlProposal.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlProposal.append(" <<NSF_CODE>> , ");
        sqlProposal.append(" <<AGENCY_PROGRAM_CODE>> , ");
        sqlProposal.append(" <<AGENCY_DIVISION_CODE>> , ");
   //COEUSQA-3951   
        sqlProposal.append(" <<AGENCY_ROUTING_IDENTIFIER>> , ");
        sqlProposal.append(" <<PREV_GG_TRACKID>> , ");
  //COEUSQA-3951          
        // Added for Case 2162  - adding Award Type - Start
        sqlProposal.append(" <<AWARD_TYPE_CODE>> , ");
        // Added for Case 2162  - adding Award Type - End
        sqlProposal.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlProposal.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlProposal.append(" <<AC_TYPE>> )");

        ProcReqParameter procProposal  = new ProcReqParameter();
        procProposal.setDSN(DSN);
        procProposal.setParameterInfo(paramProposal);
        procProposal.setSqlCommand(sqlProposal.toString());

        procedures.add(procProposal);

        // inserting proposal persons
        Vector vecPropPersons = proposalDevelopmentFormBean.getPropPersons();
        if ((vecPropPersons != null) && (vecPropPersons.size() >0)){
            //Bug fix by Ajay to delete the Person/Rolodex from person table  if the person is deleted
            //both from Investigator and KeyStudyPerson table: Start 1 31-8-2004

            //System.out.println("vecPropPersons Size:"+vecPropPersons.size());
//            for(int index = 0 ;index<vecPropPersons.size();index++){
//                ProposalPersonFormBean proposalPersonFormBean =
//                        (ProposalPersonFormBean)vecPropPersons.get(index);
//                //System.out.println("Person Id: "+proposalPersonFormBean.getPersonId());
//                //System.out.println("Ac Type: "+proposalPersonFormBean.getAcType());
//            }

            //Commented for bug fix
            //int length = vecPropPersons.size();
            CoeusVector cvPropPersons = filterPersons(proposalDevelopmentFormBean ,vecPropPersons);
            int length = cvPropPersons.size();

            /** bug fix for case #2032 Added by Tarique start 1 */
            Integer sortId = getMaxSortId(vecPropPersons);
            if(vecPropPersons.size() == length ){
                cvPropPersons = filterPIperson(proposalDevelopmentFormBean,cvPropPersons);
            }

            /** bug fix for case #2032 Added by Tarique end 1 */
            //System.out.println("cvPropPersons Size:"+cvPropPersons.size());
            //Bug fix to delete the Person/Rolodex from person table  if the person is deleted
            //both from Investigator and KeyStudyPerson table: End 1 31-8-2004

            for(int index=0;index<length;index++){
                ProposalPersonFormBean proposalPersonFormBean =
                        (ProposalPersonFormBean)/*vecPropPersons*/cvPropPersons.elementAt(index); // Commented vecPropPersons
                if  (proposalPersonFormBean.getAcType() != null) {
                    if ( proposalPersonFormBean.getAcType().equals( INSERT ) ) {
                        DepartmentPersonTxnBean departmentTxnBean = new DepartmentPersonTxnBean();
                        String personId = proposalPersonFormBean.getPersonId();

                        if( proposalPersonFormBean.isPerson() ){
                            proposalPersonFormBean = new ProposalPersonFormBean(
                                    departmentTxnBean.getPersonDetails( personId ) );
                            proposalPersonFormBean.setProposalNumber( proposalNumber );
                            proposalPersonFormBean.setAcType( INSERT );

                        }else{
                            RolodexMaintenanceDataTxnBean rolodexTxnBean =
                                    new RolodexMaintenanceDataTxnBean();
                            RolodexDetailsBean rolodexBean =
                                    rolodexTxnBean.getRolodexMaintenanceDetails( personId );
                            proposalPersonFormBean.setLastName( rolodexBean.getLastName() );
                            proposalPersonFormBean.setFirstName( rolodexBean.getFirstName() );
                            proposalPersonFormBean.setMiddleName( rolodexBean.getMiddleName() );
                            String fullName = rolodexBean.getLastName();
                            if ( rolodexBean.getFirstName() != null
                                    && rolodexBean.getFirstName().trim().length() > 0 ) {
                                fullName += ", "+ rolodexBean.getFirstName();
                            }

                            if ( rolodexBean.getMiddleName() != null
                                    && rolodexBean.getMiddleName().trim().length() > 0 ) {
                                fullName += ", "+ rolodexBean.getMiddleName();
                            }
                            proposalPersonFormBean.setFullName( fullName );
                            proposalPersonFormBean.setPrimaryTitle( rolodexBean.getTitle() );
                            //Case 3583 - START
                            proposalPersonFormBean.setDirTitle(rolodexBean.getTitle());
                            //Case 3583 - END
                            proposalPersonFormBean.setEmailAddress( rolodexBean.getEMail() );
                            proposalPersonFormBean.setOfficePhone( rolodexBean.getPhone() );

                            //Case #1602 Start 1
                            proposalPersonFormBean.setAddress1( rolodexBean.getAddress1() );
                            proposalPersonFormBean.setAddress2( rolodexBean.getAddress2() );
                            proposalPersonFormBean.setAddress3( rolodexBean.getAddress3() );
                            proposalPersonFormBean.setCity( rolodexBean.getCity() );
                            proposalPersonFormBean.setCounty( rolodexBean.getCounty() );
                            proposalPersonFormBean.setState( rolodexBean.getState() );
                            proposalPersonFormBean.setPostalCode( rolodexBean.getPostalCode() );
                            proposalPersonFormBean.setCountryCode( rolodexBean.getCountry() );
                            proposalPersonFormBean.setFaxNumber( rolodexBean.getFax() );
                            proposalPersonFormBean.setMobilePhNumber( rolodexBean.getPhone() );
                            //Case #1602 End 1

                        }
                    }
                    //bug fix for case #2032 Added by Tarique start 2
                    if(sortId == null){
                        sortId = new Integer(1);
                        if(!proposalPersonFormBean.getAcType().equals(TypeConstants.DELETE_RECORD)){
                            proposalPersonFormBean.setSortId(sortId);
                        }
                    }else if(proposalPersonFormBean.getSortId() == null){
                        sortId = new Integer(sortId.intValue()+ 1);
                        if(!proposalPersonFormBean.getAcType().equals(TypeConstants.DELETE_RECORD)){
                            proposalPersonFormBean.setSortId(sortId);
                        }
                    }
                    //bug fix for case #2032 Added by Tarique end 2

                    //Uses the cascading in the prop persons able to delete infomation in degree table
                    procedures.add(addDelPropPerson(proposalPersonFormBean));

                    //Bug Fix code by Ajay for updating person degree Info for person degree table  Start1
                    if(proposalPersonFormBean.getAcType().trim().equals("I")){

                        Vector degreeInfo = getPersonDegreeInfo(proposalPersonFormBean.getPersonId());
                        if(degreeInfo != null && degreeInfo.size() > 0 ){
                            ProposalPerDegreeFormBean  proposalPerDegreeFormBean;
                            for(int count = 0; count < degreeInfo.size() ; count ++){
                                DepartmentPerDegreeFormBean departmentPerDegreeFormBean =
                                        (DepartmentPerDegreeFormBean)degreeInfo.get(count);

                                proposalPerDegreeFormBean = prepareDegreeBean(proposalPersonFormBean.getProposalNumber(),departmentPerDegreeFormBean);
                                procedures.add(addUpdProposalPersonDegree(proposalPerDegreeFormBean));

                            }
                        }
                    }
                    //Bug Fix code by Ajay for updating person degree Info for person degree table  End1


                }
            }
        }

        // inserting new Investigators
ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
boolean hasRight = proposalDevelopmentTxnBean.getParameterCertification("ENABLE_PROP_PERSON_SELF_CERTIFY");
        Vector vecInvestigators = proposalDevelopmentFormBean.getInvestigators();
        if ((vecInvestigators != null) && (vecInvestigators.size() >0)){
            int length = vecInvestigators.size();
            for(int index=0;index<length;index++){
                ProposalInvestigatorFormBean proposalInvestigatorFormBean =
                        (ProposalInvestigatorFormBean)vecInvestigators.elementAt(index);
                // Added for COEUSQA-2637 : CLONE -Proposal dev - YNQ save is not consistent - Start
                // Deletes all the record, then updation and insertion will happen
                if(TypeConstants.DELETE_RECORD.equals(proposalInvestigatorFormBean.getAcType())) {
                    proposalInvestigatorFormBean.setProposalNumber(
                            proposalNumber);
                    procedures.add(addUpdPropInvestigator(proposalInvestigatorFormBean));
      // delete certification while deleting person for PPC start
         if(hasRight){
                 procedures.add(UpdPropInvestigatorCertification(proposalInvestigatorFormBean.getProposalNumber(),proposalInvestigatorFormBean.getPersonId()));
                 }
         //  delete certification while deleting person for PPC end
                    Vector vecInvestigatorsUnits =
                            proposalInvestigatorFormBean.getInvestigatorUnits();

                    if ((vecInvestigatorsUnits != null) &&
                            (vecInvestigatorsUnits.size() >0)){
                        int unitslength = vecInvestigatorsUnits.size();
                        for(int unitIndex=0;unitIndex<unitslength;unitIndex++){
                            ProposalLeadUnitFormBean proposalLeadUnitFormBean =
                                    (ProposalLeadUnitFormBean)
                                    vecInvestigatorsUnits.elementAt(unitIndex);
                            if (proposalLeadUnitFormBean.getAcType()!= null) {
                                proposalLeadUnitFormBean.setProposalNumber(
                                        proposalNumber);
                                procedures.add(addUpdProposalLeadUnit(proposalLeadUnitFormBean));
                            }
                        }
                    }

                    Vector investigatorAnswers = proposalInvestigatorFormBean.getInvestigatorAnswers();
                    if ( investigatorAnswers != null && investigatorAnswers.size() > 0 ) {
                        int ansLength = investigatorAnswers.size();

                        for( int ansIndx = 0; ansIndx < ansLength ; ansIndx++ ) {
                            ProposalYNQFormBean ynqBean =
                                    ( ProposalYNQFormBean ) investigatorAnswers.get( ansIndx );
                            if( ynqBean.getAcType() != null ) {
                                ynqBean.setProposalNumber( proposalNumber );
                                procedures.add( addUpdProposalYNQ( ynqBean ) );
                            }
                        }
                    }
                }
            }
            // Added for COEUSQA-2637 : CLONE -Proposal dev - YNQ save is not consistent - End

            for(int index=0;index<length;index++){
                ProposalInvestigatorFormBean proposalInvestigatorFormBean =
                        (ProposalInvestigatorFormBean)vecInvestigators.elementAt(index);
                if  (proposalInvestigatorFormBean.getAcType() != null &&
                        !TypeConstants.DELETE_RECORD.equals(proposalInvestigatorFormBean.getAcType())) {
                    //proposalInvestigatorFormBean.setUpdateTimestamp(
                    //dbTimestamp);
                    proposalInvestigatorFormBean.setProposalNumber(
                            proposalNumber);
                    //if (!proposalInvestigatorFormBean.getAcType().equalsIgnoreCase("D") ) {
                    procedures.add(addUpdPropInvestigator(proposalInvestigatorFormBean));
                    //}
                }

                Vector vecInvestigatorsUnits =
                        proposalInvestigatorFormBean.getInvestigatorUnits();

                if ((vecInvestigatorsUnits != null) &&
                        (vecInvestigatorsUnits.size() >0)){
                    // we are dealing with investigator units.
                    int unitslength = vecInvestigatorsUnits.size();

                    for(int unitIndex=0;unitIndex<unitslength;unitIndex++){
                        ProposalLeadUnitFormBean proposalLeadUnitFormBean =
                                (ProposalLeadUnitFormBean)
                                vecInvestigatorsUnits.elementAt(unitIndex);
                        if (proposalLeadUnitFormBean.getAcType()!= null) {
                            //proposalLeadUnitFormBean.setUpdateTimestamp(dbTimestamp);
                            proposalLeadUnitFormBean.setProposalNumber(
                                    proposalNumber);
                            //if (!proposalLeadUnitFormBean.getAcType().equalsIgnoreCase("D")) {
                            procedures.add(addUpdProposalLeadUnit(proposalLeadUnitFormBean));
                            //}
                        }
                    }
                }

                Vector investigatorAnswers = proposalInvestigatorFormBean.getInvestigatorAnswers();
                if ( investigatorAnswers != null && investigatorAnswers.size() > 0 ) {
                    int ansLength = investigatorAnswers.size();

                    for( int ansIndx = 0; ansIndx < ansLength ; ansIndx++ ) {
                        ProposalYNQFormBean ynqBean =
                                ( ProposalYNQFormBean ) investigatorAnswers.get( ansIndx );
                        if( ynqBean.getAcType() != null ) {
                            ynqBean.setProposalNumber( proposalNumber );
                            procedures.add( addUpdProposalYNQ( ynqBean ) );
                        }
                    }
                }
            }
        }

        // setting the key person tab
ProposalKeyPersonFormBean  proposalKeyPersonFormBean;
KeyPersonUnitBean proposalKPUnitFormBean;
String acType;
Vector vecKPUnits=proposalDevelopmentFormBean.getKeyStudyPersonnelUnits();
Vector vecKeyPerson =proposalDevelopmentFormBean.getKeyStudyPersonnel();
if ((vecKeyPerson != null) && (vecKeyPerson.size() >0)){
       int keyLength = vecKeyPerson.size();
       for(int keyIndex=0;keyIndex<keyLength;keyIndex++){
            proposalKeyPersonFormBean=(ProposalKeyPersonFormBean)vecKeyPerson.elementAt(keyIndex);
            acType=proposalKeyPersonFormBean.getAcType();
            if  (acType!= null ){
                if(acType.equalsIgnoreCase("D")){
                //delete the unit first.
                    for (Iterator it = vecKPUnits.iterator(); it.hasNext();) {
                        proposalKPUnitFormBean = (KeyPersonUnitBean)it.next();
                        if(proposalKPUnitFormBean.getPersonId().equals(proposalKeyPersonFormBean.getPersonId())){
                        proposalKPUnitFormBean.setAcType("D");
                        proposalKPUnitFormBean.setProposalNumber(proposalNumber);
                        procedures.add(addUpdProposal_kp_Unit(proposalKPUnitFormBean));
                        }
                    }
                }
                   proposalKeyPersonFormBean.setProposalNumber(proposalNumber);
                   
                   procedures.add(addUpdPropKeyPerson(proposalKeyPersonFormBean));
                   // delete certification while deleting person for PPC start
                   if((hasRight) && (acType.equalsIgnoreCase("D")))
                    {
                     procedures.add(UpdPropInvestigatorCertification(proposalKeyPersonFormBean.getProposalNumber(),proposalKeyPersonFormBean.getPersonId()));
                    }
                    // delete certification while deleting person for PPC end
                }
            }
        }

//Load the KP Unit Vector START
vecKPUnits =proposalDevelopmentFormBean.getKeyStudyPersonnelUnits();

if ((vecKPUnits != null) &&(vecKPUnits.size() >0)){
    int unitslength = vecKPUnits.size();
    for(int unitIndex=0;unitIndex<unitslength;unitIndex++){
        proposalKPUnitFormBean =(KeyPersonUnitBean)vecKPUnits.elementAt(unitIndex);
        acType=proposalKPUnitFormBean.getAcType();
        if (acType!= null) {
              proposalKPUnitFormBean.setProposalNumber(proposalNumber);
              procedures.add(addUpdProposal_kp_Unit(proposalKPUnitFormBean));
        }
    }
}
//Load The KP Unit Vector END               



        // setting the locations details
        //Commented for case 2406 - Organizations and Locations - start
//        Vector vecLocations =
//                proposalDevelopmentFormBean.getLocationLists();
//        if ((vecLocations != null) && (vecLocations.size() >0)){
//            int keyLength = vecLocations.size();
//            for(int keyIndex=0;keyIndex<keyLength;keyIndex++){
//                ProposalLocationFormBean proposalLocationFormBean =
//                        (ProposalLocationFormBean)vecLocations.elementAt(keyIndex);
//                if  (proposalLocationFormBean.getAcType() != null ){
//                    //proposalLocationFormBean.setUpdateTimestamp(
//                    //dbTimestamp);
//                    proposalLocationFormBean.setProposalNumber(
//                            proposalNumber);
//                    //if ( !proposalLocationFormBean.getAcType().equalsIgnoreCase("D")) {
//                    procedures.add(addUpdProposalLocations(proposalLocationFormBean));
//                    //}
//                }
//            }
//        }
        //Commented for case 2406 - Organizations and Locations - end
        //Added for case case 2406 - Organizations and Locations - start
        Vector vecSites = proposalDevelopmentFormBean.getSites();
        if(vecSites != null && vecSites.size() > 0){
            ProposalSiteBean proposalSiteBean = null;
            boolean generatedSiteNumber = false;
            int siteNumber = 1;
            for(int count = 0; count<vecSites.size(); count++){
                proposalSiteBean = (ProposalSiteBean)vecSites.get(count);
                if(proposalSiteBean.getAcType()!=null
                        && proposalSiteBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                    if(!generatedSiteNumber){
                        siteNumber = proposalDevelopmentTxnBean.getNextSiteNumber(proposalSiteBean.getProposalNumber());
                        proposalSiteBean.setSiteNumber(siteNumber);
                        generatedSiteNumber = true;
                    }else{
                        siteNumber++;
                        proposalSiteBean.setSiteNumber(siteNumber);
                    }
                }
                procedures.addAll(addUpdDelProposalSites(proposalSiteBean));

            }
        }
        //Added for case 2406 - Organizations and Locations - end

        // setting the data science code tab
        Vector vecScienceCode =
                proposalDevelopmentFormBean.getScienceCode();
        if ((vecScienceCode != null) && (vecScienceCode.size() >0)){
            int keyLength = vecScienceCode.size();
            for(int keyIndex=0;keyIndex<keyLength;keyIndex++){
                ProposalScienceCodeFormBean proposalScienceCodeFormBean =
                        (ProposalScienceCodeFormBean)vecScienceCode.elementAt(keyIndex);
                if  (proposalScienceCodeFormBean.getAcType() != null ){
                    //proposalScienceCodeFormBean.setUpdateTimestamp(
                    //dbTimestamp);
                    proposalScienceCodeFormBean.setProposalNumber(
                            proposalNumber);
                    if ( !proposalScienceCodeFormBean.getAcType().equalsIgnoreCase("U")) {
                        procedures.add(addDeletePropScienceCode(proposalScienceCodeFormBean));
                    }
                }
            }
        }

        // setting the data science code tab
        Vector vecOthers =
                proposalDevelopmentFormBean.getOthers();
        if ((vecOthers != null) && (vecOthers.size() >0)){
            int keyLength = vecOthers.size();
            for(int keyIndex=0;keyIndex<keyLength;keyIndex++){
                ProposalCustomElementsInfoBean proposalCustomElementsInfoBean =
                        (ProposalCustomElementsInfoBean)vecOthers.elementAt(keyIndex);
                if  (proposalCustomElementsInfoBean.getAcType() != null ){
                    //proposalCustomElementsInfoBean.setUpdateTimestamp(
                    //dbTimestamp);
                    proposalCustomElementsInfoBean.setProposalNumber(
                            proposalNumber);
                    //if ( !proposalCustomElementsInfoBean.getAcType().equalsIgnoreCase("D")) {
                    procedures.add(addUpdProposalOther(proposalCustomElementsInfoBean));
                    //}
                }
            }
        }
        ProposalRolesFormBean proposalRolesFormBean = new ProposalRolesFormBean();
//       Vector vUserRoles = null;
        Vector vecUserRoles = proposalDevelopmentFormBean.getPropRolesFormBean();

        if ((vecUserRoles != null) && (vecUserRoles.size() >0)){
            int length = vecUserRoles.size();
            for(int index=0;index<length;index++){
                proposalRolesFormBean =(ProposalRolesFormBean)vecUserRoles.elementAt(index);
                if (proposalRolesFormBean.getAcType() != null ) {
                    proposalRolesFormBean.setProposalNumber( proposalNumber );
                    procedures.add( addDeleteProposalRolesForUser(proposalRolesFormBean) );
                }
            }
        }

        ProposalSpecialReviewFormBean proposalSpecialReviewFormBean = new ProposalSpecialReviewFormBean();
        Vector vSpecialReview = proposalDevelopmentFormBean.getPropSpecialReviewFormBean();

        if ((vSpecialReview != null) && (vSpecialReview.size() >0)){
            int length = vSpecialReview.size();
            //Added on 4th July, 2004 - Prasanna
            int intSpecialRevNumber = proposalDevelopmentTxnBean.getNextPropSpecialReviewNumber(proposalNumber);
            for(int index=0;index<length;index++){
                proposalSpecialReviewFormBean =(ProposalSpecialReviewFormBean)vSpecialReview.elementAt(index);
                if (proposalSpecialReviewFormBean.getAcType() != null ) {
                    //Added on 4th July, 2004 - start
                    if(proposalSpecialReviewFormBean.getAcType().equalsIgnoreCase("I")){
                        intSpecialRevNumber = intSpecialRevNumber + 1;
                        proposalSpecialReviewFormBean.setSpecialReviewNumber(intSpecialRevNumber);
                    }
                    //Added on 4th July, 2004 - end
                    //proposalSpecialReviewFormBean.setProposalNumber( proposalNumber );
                    procedures.add( addUpdDelPropSpecialReview(proposalSpecialReviewFormBean) );
                }
            }
        }

        ProposalYNQBean proposalYNQBean = new ProposalYNQBean();
        Vector vctPropYNQAnswerList = proposalDevelopmentFormBean.getPropYNQAnswerList();

        if ((vctPropYNQAnswerList != null) && (vctPropYNQAnswerList.size() >0)){
            int length = vctPropYNQAnswerList.size();
            for(int index=0;index<length;index++){
                proposalYNQBean =(ProposalYNQBean)vctPropYNQAnswerList.elementAt(index);
                if (proposalYNQBean.getAcType() != null ) {
                    procedures.add( addUpdDeleteProposalYNQ(proposalYNQBean) );
                }
            }
        }

        //Update Proposal Approval Maps and Proposal Approvals
        //Commented for Routing enhancements case 2785 - start
//        CoeusVector vctApprovalMaps = proposalDevelopmentFormBean.getApprovalMaps();
//        ProposalApprovalMapBean proposalApprovalMapBean = null;
//        if(vctApprovalMaps!=null){
//            int length = vctApprovalMaps.size();
//            for(int index=0;index<length;index++){
//                proposalApprovalMapBean =(ProposalApprovalMapBean)vctApprovalMaps.elementAt(index);
//                if (proposalApprovalMapBean!=null && proposalApprovalMapBean.getAcType() != null ) {
//                    procedures.add( addUpdDeleteProposalApprovalMaps(proposalApprovalMapBean) );
//
//                    //Update Approvals for this Map
//                    CoeusVector vctProposalApprovals = proposalApprovalMapBean.getProposalApprovals();
//                    ProposalApprovalBean proposalApprovalBean = new ProposalApprovalBean();
//
//                    if(vctProposalApprovals!=null){
//                        for(int approvalRow = 0; approvalRow < vctProposalApprovals.size(); approvalRow++){
//                            proposalApprovalBean = (ProposalApprovalBean)vctProposalApprovals.elementAt(approvalRow);
//                            if(proposalApprovalBean!=null && proposalApprovalBean.getAcType()!=null){
//                                procedures.add(addUpdDeleteProposalApprovals(proposalApprovalBean));
//                            }
//                        }
//                    }
//                }
//            }
//        }
        //Commented for Routing enhancements case 2785 - end

        //Added for Routing enhancements case 2785 - start
        CoeusVector vctApprovalMaps = proposalDevelopmentFormBean.getApprovalMaps();
        RoutingMapBean routingMapBean = null;
        RoutingUpdateTxnBean routingUpdateTxnBean = new RoutingUpdateTxnBean(userId);
        if(vctApprovalMaps!=null){
            int length = vctApprovalMaps.size();
            for(int index=0;index<length;index++){
                routingMapBean =(RoutingMapBean)vctApprovalMaps.elementAt(index);
                if (routingMapBean!=null && routingMapBean.getAcType() != null ) {
                    if(!routingMapBean.getAcType().equals("D")){
                        procedures.add( routingUpdateTxnBean.addUpdDelRoutingMaps(routingMapBean));
                    }

                    //Update Approvals for this Map
                    CoeusVector vctProposalApprovals = routingMapBean.getRoutingMapDetails();
                    RoutingDetailsBean routingDetailsBean = null;

                    if(vctProposalApprovals!=null){
                        for(int approvalRow = 0; approvalRow < vctProposalApprovals.size(); approvalRow++){
                            routingDetailsBean = (RoutingDetailsBean)vctProposalApprovals.elementAt(approvalRow);
                            if(routingDetailsBean!=null && routingDetailsBean.getAcType()!=null){
                                procedures.add(routingUpdateTxnBean.addUpdDelRoutingDetails(routingDetailsBean));
                            }
                        }
                    }
                    if(routingMapBean.getAcType().equals("D")){
                        procedures.add(routingUpdateTxnBean.addUpdDelRoutingMaps(routingMapBean));
                    }
                }
            }
        }
       //Added for Routing enhancements case 2785 - start

        //Added to Update Narrative User's table when Proposal User Roles is Updated - start
        CoeusVector narrativeUsers = proposalDevelopmentFormBean.getNarrativeUserRights();
        if(narrativeUsers!=null && narrativeUsers.size() > 0){
            ProposalNarrativeModuleUsersFormBean proposalNarrativeModuleUsersFormBean = null;
            for(int index=0;index<narrativeUsers.size();index++){
                proposalNarrativeModuleUsersFormBean = (ProposalNarrativeModuleUsersFormBean)narrativeUsers.elementAt(index);
                if(proposalNarrativeModuleUsersFormBean != null && proposalNarrativeModuleUsersFormBean.getAcType()!=null){
                    procedures.add(addUpdDelProposalNarrativeUsers(proposalNarrativeModuleUsersFormBean));
                }
            }
        }
        //Added to Update Narrative User's table when Proposal User Roles is Updated - end
        try {
            //JIRA COEUSDEV-61 - START
            if (proposalDevelopmentFormBean.getOpportunityInfoBean() != null) {
                S2SSubmissionDataTxnBean txnBean = new S2SSubmissionDataTxnBean();
                DBOpportunityInfoBean dBOpportunityInfoBean = (DBOpportunityInfoBean)proposalDevelopmentFormBean.getOpportunityInfoBean();
                dBOpportunityInfoBean.setProposalNumber(proposalDevelopmentFormBean.getProposalNumber());
                dBOpportunityInfoBean.setAcType('I');
                Vector procs = txnBean.addOpportunityProcs(dBOpportunityInfoBean);
                procedures.addAll(procs);
            }
            //JIRA COEUSDEV-61 - END
        } catch (Exception ex) {
            throw new CoeusException(ex.getMessage());
        }
        if(dbEngine!=null){

            //Bug Fix 2084 Start
            //dbEngine.executeStoreProcs(procedures);
            try{
                dbEngine.executeStoreProcs(procedures);
            }catch (DBException dbEx){
                throw new CoeusException(dbEx.getMessage());
            }
            //Bug Fix 2084 End

        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        /* Case : 2057 Start-1  */
        updateRoleData(proposalRolesFormBean.getProposalNumber(), userId);
        /* Case : 2057 End-1  */
        
//COEUSQA-1457 starts
        Vector listOfDeletedPerson=new Vector();
        Vector listOfDeletedPersonRoles=new Vector();
        //send the mail if any pi is removed.
        if(vecInvestigators!=null){
            String acTypeKP; 
            ProposalInvestigatorFormBean proposalInvestigatorFormBean;
            for (Iterator<ProposalInvestigatorFormBean> it = vecInvestigators.iterator(); it.hasNext();) {
                proposalInvestigatorFormBean = it.next();
                acTypeKP=proposalInvestigatorFormBean.getAcType();
                if(acTypeKP!=null &&acTypeKP.equalsIgnoreCase("D") ){
                listOfDeletedPerson.add(proposalInvestigatorFormBean.getPersonId());
                listOfDeletedPersonRoles.add("Investigator");
                }
            }
        }
  //send the mail if any keyperson is removed.
        
        if(vecKeyPerson!=null){
            String acTypeKP;
            for (Iterator<ProposalKeyPersonFormBean> it = vecKeyPerson.iterator(); it.hasNext();) {
                proposalKeyPersonFormBean=it.next();
                acTypeKP=proposalKeyPersonFormBean.getAcType();
                if(acTypeKP!=null &&acTypeKP.equalsIgnoreCase("D") ){
                listOfDeletedPerson.add(proposalKeyPersonFormBean.getPersonId());
                listOfDeletedPersonRoles.add(proposalKeyPersonFormBean.getProjectRole());
                }
            }
        }
        
        if(listOfDeletedPerson.size()>0){
            try{
                sendRemovalEmailToPropPersons(proposalNumber,listOfDeletedPerson,listOfDeletedPersonRoles,userId);
            }
            catch(Exception ex){}
        }
//COEUSQA-1457 ends

        /*if (proposalDevelopmentFormBean.getAcType() != null &&
                proposalDevelopmentFormBean.getAcType().equals(INSERT)) {

            ProposalRolesFormBean proposalRolesFormBean = new ProposalRolesFormBean();
            proposalRolesFormBean.setAcType(INSERT);
            proposalRolesFormBean.setProposalNumber(
                            proposalDevelopmentFormBean.getProposalNumber());
            proposalRolesFormBean.setRoleId(CoeusConstants.PROPOSAL_ROLE_ID);
            proposalRolesFormBean.setUserId(proposalDevelopmentFormBean.getCreateUser());
            if (!addDeleteProposalRolesForUser(proposalRolesFormBean)) {
                throw new CoeusException("db_exceptionCode.1000");
            }
        }*/
        success = true;
        return success;

    }

    /* Case : 2057 Start-2  */
    public int updateRoleData(String proposalNumber,String loggedinUser)
    throws CoeusException, DBException {
        int number = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("AS_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,""+proposalNumber));
        param.add(new Parameter("AS_USER_ID",
                DBEngineConstants.TYPE_STRING,""+loggedinUser));
        /* calling stored function */
        if(dbEngine!=null){
            try{
                result = dbEngine.executeFunctions("Coeus",
                        "{ <<OUT INTEGER IS_SUCCESS>> = "
                        +" call FN_ASSIGN_NARR_MODULE_RIGHTS( "
                        +" << AS_PROPOSAL_NUMBER >>, << AS_USER_ID >> ) }", param);
            }catch (DBException dbEx){
                throw new CoeusException(dbEx.getMessage());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            //number = new Integer(rowParameter.get("IS_SUCCESS").toString());
            number = Integer.parseInt(rowParameter.get("IS_SUCCESS").toString());
        }
        return number;
    }
    /* Case : 2057 End-2  */

    /**
     * Method for getting Max Sort ID
     * bug fix for case #2032 Added by Tarique start 3
     */
    private Integer getMaxSortId(Vector vecPropPersons){

        Integer sortId = (( ProposalPersonFormBean )
        vecPropPersons.get(0)).getSortId();
        for(int index=1;index<vecPropPersons.size();index++){
            try{
                ProposalPersonFormBean proposalPersonFormBean = ( ProposalPersonFormBean )
                vecPropPersons.get( index );
                if(sortId==null&&proposalPersonFormBean.getSortId()==null)
                    continue;
                else if(sortId==null&&proposalPersonFormBean.getSortId()!=null){
                    sortId=proposalPersonFormBean.getSortId();
                }else{
                    if(proposalPersonFormBean.getSortId()!=null){
                        if(proposalPersonFormBean.getSortId().intValue()>sortId.intValue()){
                            sortId=proposalPersonFormBean.getSortId();
                        }
                    }
                }

            }catch(Exception e){
                // commented for using UtilFactory.log instead of printStackTrace
                UtilFactory.log(e.getMessage(),e,"ProposalDevelopmentUpdateTxnBean", "getMaxSortId");
//                e.printStackTrace();
            }
        }
        return sortId;
    }

    private CoeusVector filterPIperson(ProposalDevelopmentFormBean
            proposalDevelopmentFormBean, CoeusVector cvPropPersons){
        Vector vecInv = proposalDevelopmentFormBean.getInvestigators();
        CoeusVector cvInv = new CoeusVector();
        String personId;
        cvInv.addAll(vecInv);
        Equals eqPI = new Equals("principleInvestigatorFlag", true);
        cvInv = cvInv.filter(eqPI);
        if(cvInv != null &&  cvInv.size() > 0){
            ProposalInvestigatorFormBean proposalInvestigatorFormBean =
                    (ProposalInvestigatorFormBean)cvInv.get(0);
            personId = proposalInvestigatorFormBean.getPersonId();
        }else{
            //If no Investigators is selected only key persons were selected.
            return cvPropPersons;
        }
        Equals eqPersonId = new Equals("personId",personId);
        CoeusVector cvEqPersonId = cvPropPersons.filter(eqPersonId);

        NotEquals nePersonId = new NotEquals("personId",personId);
        CoeusVector cvNePersonId = cvPropPersons.filter(nePersonId);
        cvPropPersons.removeAllElements();
        if(cvEqPersonId !=null && cvEqPersonId.size() > 0){
            cvPropPersons.addAll(0,cvEqPersonId);
        }
        if(cvNePersonId !=null && cvNePersonId.size() > 0){
            cvPropPersons.addAll(cvNePersonId);
        }
        return cvPropPersons;

    }
    /** bug fix for case #2032 Added by Tarique end 3 */

    /**
     * Method used to insert/delete all the details of a ProposalPerson
     * <li>To fetch the data, it uses DW_UPDATE_PROP_PERSON procedure.
     *
     * @param ProposalPersonFormBean this bean contains data for
     * insert/delete for proposal person.
     * @return ProcReqParameter the DSN,StringBuffer and parameter is set in
     *  this class before executing the procedure.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter addDelPropPerson( ProposalPersonFormBean
            proposalPersonFormBean)  throws CoeusException,DBException{
//        boolean success = false;
//        Vector procedures = new Vector(5,3);

        Vector paramPerson = new Vector();
        //Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();

        //Case #1602 Start 2
        /*StringBuffer sqlPropPerson = new StringBuffer(
                                        "call DW_UPDATE_PROP_PERSON(");*/
        StringBuffer sqlPropPerson = new StringBuffer(
                "call UPDATE_PROP_PERSON(");
        //Case #1602 End 2

        sqlPropPerson.append(" <<PROPOSAL_NUMBER>> , ");
        sqlPropPerson.append(" <<PERSON_ID>> , ");
        sqlPropPerson.append(" <<SSN>> , ");
        sqlPropPerson.append(" <<LAST_NAME>> , ");
        sqlPropPerson.append(" <<FIRST_NAME>> , ");
        sqlPropPerson.append(" <<MIDDLE_NAME>> , ");
        sqlPropPerson.append(" <<FULL_NAME>> , ");
        sqlPropPerson.append(" <<PRIOR_NAME>> , ");
        sqlPropPerson.append(" <<USER_NAME>> , ");
        sqlPropPerson.append(" <<EMAIL_ADDRESS>> , ");
        sqlPropPerson.append(" <<DATE_OF_BIRTH>> , ");
        sqlPropPerson.append(" <<AGE>> , ");
        sqlPropPerson.append(" <<AGE_BY_FISCAL_YEAR>> , ");
        sqlPropPerson.append(" <<GENDER>> , ");
        sqlPropPerson.append(" <<RACE>> , ");
        sqlPropPerson.append(" <<EDUCATION_LEVEL>> , ");
        sqlPropPerson.append(" <<DEGREE>> , ");
        sqlPropPerson.append(" <<MAJOR>> , ");
        sqlPropPerson.append(" <<IS_HANDICAPPED>> , ");
        sqlPropPerson.append(" <<HANDICAP_TYPE>> , ");
        sqlPropPerson.append(" <<IS_VETERAN>> , ");
        sqlPropPerson.append(" <<VETERAN_TYPE>> , ");
        sqlPropPerson.append(" <<VISA_CODE>> , ");
        sqlPropPerson.append(" <<VISA_TYPE>> , ");
        sqlPropPerson.append(" <<VISA_RENEWAL_DATE>> , ");
        sqlPropPerson.append(" <<HAS_VISA>> , ");
        sqlPropPerson.append(" <<OFFICE_LOCATION>> , ");
        sqlPropPerson.append(" <<OFFICE_PHONE>> , ");
        sqlPropPerson.append(" <<SECONDRY_OFFICE_LOCATION>> , ");
        sqlPropPerson.append(" <<SECONDRY_OFFICE_PHONE>> , ");
        sqlPropPerson.append(" <<SCHOOL>> , ");
        sqlPropPerson.append(" <<YEAR_GRADUATED>> , ");
        sqlPropPerson.append(" <<DIRECTORY_DEPARTMENT>> , ");
        sqlPropPerson.append(" <<SALUTATION>> , ");
        sqlPropPerson.append(" <<COUNTRY_OF_CITIZENSHIP>> , ");
        sqlPropPerson.append(" <<PRIMARY_TITLE>> , ");
        sqlPropPerson.append(" <<DIRECTORY_TITLE>> , ");
        sqlPropPerson.append(" <<HOME_UNIT>> , ");
        sqlPropPerson.append(" <<IS_FACULTY>> , ");
        sqlPropPerson.append(" <<IS_GRADUATE_STUDENT_STAFF>> , ");
        sqlPropPerson.append(" <<IS_RESEARCH_STAFF>> , ");
        sqlPropPerson.append(" <<IS_SERVICE_STAFF>> , ");
        sqlPropPerson.append(" <<IS_SUPPORT_STAFF>> , ");
        sqlPropPerson.append(" <<IS_OTHER_ACCADEMIC_GROUP>> , ");
        sqlPropPerson.append(" <<IS_MEDICAL_STAFF>> , ");
        sqlPropPerson.append(" <<VACATION_ACCURAL>> , ");
        sqlPropPerson.append(" <<IS_ON_SABBATICAL>> , ");
        sqlPropPerson.append(" <<ID_PROVIDED>> , ");
        sqlPropPerson.append(" <<ID_VERIFIED>> , ");
        sqlPropPerson.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlPropPerson.append(" <<UPDATE_USER>> , ");

        //Case #1777 Start 1
        sqlPropPerson.append(" <<SORT_ID>> , ");
        //Case #1777 End 1

        //Case #1602 Start 3
        sqlPropPerson.append(" <<ADDRESS_LINE_1>> , ");
        sqlPropPerson.append(" <<ADDRESS_LINE_2>> , ");
        sqlPropPerson.append(" <<ADDRESS_LINE_3>> , ");
        sqlPropPerson.append(" <<CITY>> , ");
        sqlPropPerson.append(" <<COUNTY>> , ");
        sqlPropPerson.append(" <<STATE>> , ");
        sqlPropPerson.append(" <<POSTAL_CODE>> , ");
        sqlPropPerson.append(" <<COUNTRY_CODE>> , ");
        sqlPropPerson.append(" <<FAX_NUMBER>> , ");
        sqlPropPerson.append(" <<PAGER_NUMBER>> , ");
        sqlPropPerson.append(" <<MOBILE_PHONE_NUMBER>> , ");
        sqlPropPerson.append(" <<ERA_COMMONS_USER_NAME>> , ");
        //Case #1602 End 3
        //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - Start
        sqlPropPerson.append(" <<DIVISION>> , ");
        //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - End

        sqlPropPerson.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlPropPerson.append(" <<AW_PERSON_ID>> , ");
        sqlPropPerson.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlPropPerson.append(" <<AC_TYPE>> )");


        paramPerson.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getProposalNumber()));
        paramPerson.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getPersonId()));
        paramPerson.addElement(new Parameter("SSN",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getSsn()));
        paramPerson.addElement(new Parameter("LAST_NAME",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getLastName()) );
        paramPerson.addElement(new Parameter("FIRST_NAME",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getFirstName()));
        paramPerson.addElement(new Parameter("MIDDLE_NAME",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getMiddleName()));
        paramPerson.addElement(new Parameter("FULL_NAME",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getFullName()));
        paramPerson.addElement(new Parameter("PRIOR_NAME",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getPriorName()));
        paramPerson.addElement(new Parameter("USER_NAME",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getUserName()) );
        paramPerson.addElement(new Parameter("EMAIL_ADDRESS",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getEmailAddress()));
        paramPerson.addElement(new Parameter("DATE_OF_BIRTH",
                DBEngineConstants.TYPE_DATE,
                proposalPersonFormBean.getDateOfBirth()));
        paramPerson.addElement(new Parameter("AGE",
                DBEngineConstants.TYPE_INT,
                ""+proposalPersonFormBean.getAge()) );
        paramPerson.addElement(new Parameter("AGE_BY_FISCAL_YEAR",
                DBEngineConstants.TYPE_INT,
                ""+proposalPersonFormBean.getAgeByFiscalYear()));
        paramPerson.addElement(new Parameter("GENDER",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getGender()));
        paramPerson.addElement(new Parameter("RACE",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getRace()) );
        paramPerson.addElement(new Parameter("EDUCATION_LEVEL",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getEduLevel()));
        paramPerson.addElement(new Parameter("DEGREE",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getDegree()));
        paramPerson.addElement(new Parameter("MAJOR",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getMajor()));
        paramPerson.addElement(new Parameter("IS_HANDICAPPED",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getHandicap() ? "Y" : "N" ) );
        paramPerson.addElement(new Parameter("HANDICAP_TYPE",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getHandiCapType()));
        paramPerson.addElement(new Parameter("IS_VETERAN",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getVeteran() ? "Y" : "N" ));
        paramPerson.addElement(new Parameter("VETERAN_TYPE",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getVeteranType()) );
        paramPerson.addElement(new Parameter("VISA_CODE",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getVisaCode()));
        paramPerson.addElement(new Parameter("VISA_TYPE",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getVisaType()));
        paramPerson.addElement(new Parameter("VISA_RENEWAL_DATE",
                DBEngineConstants.TYPE_DATE,
                proposalPersonFormBean.getVisaRenDate()) );
        paramPerson.addElement(new Parameter("HAS_VISA",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getHasVisa() ? "Y" : "N" ));
        paramPerson.addElement(new Parameter("OFFICE_LOCATION",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getOfficeLocation()));
        paramPerson.addElement(new Parameter("OFFICE_PHONE",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getOfficePhone()) );
        paramPerson.addElement(new Parameter("SECONDRY_OFFICE_LOCATION",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getSecOfficeLocation()));
        paramPerson.addElement(new Parameter("SECONDRY_OFFICE_PHONE",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getSecOfficePhone()));
        paramPerson.addElement(new Parameter("SCHOOL",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getSchool()) );
        paramPerson.addElement(new Parameter("YEAR_GRADUATED",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getYearGraduated()));
        paramPerson.addElement(new Parameter("DIRECTORY_DEPARTMENT",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getDirDept()));
        paramPerson.addElement(new Parameter("SALUTATION",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getSaltuation()));
        paramPerson.addElement(new Parameter("COUNTRY_OF_CITIZENSHIP",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getCountryCitizenship()) );
        paramPerson.addElement(new Parameter("PRIMARY_TITLE",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getPrimaryTitle()));
        paramPerson.addElement(new Parameter("DIRECTORY_TITLE",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getDirTitle()));
        paramPerson.addElement(new Parameter("HOME_UNIT",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getHomeUnit()) );
        paramPerson.addElement(new Parameter("IS_FACULTY",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getFaculty() ? "Y" : "N" ));
        paramPerson.addElement(new Parameter("IS_GRADUATE_STUDENT_STAFF",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getGraduateStudentStaff() ? "Y" : "N" ));
        paramPerson.addElement(new Parameter("IS_RESEARCH_STAFF",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getResearchStaff() ? "Y" : "N" ));
        paramPerson.addElement(new Parameter("IS_SERVICE_STAFF",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getServiceStaff() ? "Y" : "N" ));
        paramPerson.addElement(new Parameter("IS_SUPPORT_STAFF",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getSupportStaff() ? "Y" : "N" ));
        paramPerson.addElement(new Parameter("IS_OTHER_ACCADEMIC_GROUP",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getOtherAcademicGroup() ? "Y" : "N" ));
        paramPerson.addElement(new Parameter("IS_MEDICAL_STAFF",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getMedicalStaff() ? "Y" : "N" ));
        paramPerson.addElement(new Parameter("VACATION_ACCURAL",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getVacationAccural() ? "Y" : "N" ));
        paramPerson.addElement(new Parameter("IS_ON_SABBATICAL",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getOnSabbatical() ? "Y" : "N" ));
        paramPerson.addElement(new Parameter("ID_PROVIDED",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getProvided()));
        paramPerson.addElement(new Parameter("ID_VERIFIED",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getVerified()));
        paramPerson.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramPerson.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));

        //Case #1777 Start 2
        paramPerson.addElement(new Parameter("SORT_ID",
                DBEngineConstants.TYPE_INTEGER,proposalPersonFormBean.getSortId()));
        //Case #1777 End 2

        //Case #1602 Start 4
        paramPerson.addElement(new Parameter("ADDRESS_LINE_1",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getAddress1()));
        paramPerson.addElement(new Parameter("ADDRESS_LINE_2",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getAddress2()));
        paramPerson.addElement(new Parameter("ADDRESS_LINE_3",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getAddress3()));
        paramPerson.addElement(new Parameter("CITY",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getCity()));
        paramPerson.addElement(new Parameter("COUNTY",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getCounty()));
        paramPerson.addElement(new Parameter("STATE",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getState()));
        paramPerson.addElement(new Parameter("POSTAL_CODE",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getPostalCode()));
        paramPerson.addElement(new Parameter("COUNTRY_CODE",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getCountryCode()));
        paramPerson.addElement(new Parameter("FAX_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getFaxNumber()));
        paramPerson.addElement(new Parameter("PAGER_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getPagerNumber()));
        paramPerson.addElement(new Parameter("MOBILE_PHONE_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getMobilePhNumber()));
        paramPerson.addElement(new Parameter("ERA_COMMONS_USER_NAME",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getEraCommonsUsrName()));
        //Case #1602 End 4
        //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - Start
        if(TypeConstants.INSERT_RECORD.equals(proposalPersonFormBean.getAcType())){
            S2STxnBean s2sTxnBean = new S2STxnBean();
            String divisionName = s2sTxnBean.fn_get_division(proposalPersonFormBean.getHomeUnit());
            paramPerson.addElement(new Parameter("DIVISION",
                    DBEngineConstants.TYPE_STRING, divisionName));
        }else{
            //To update the division value
            if(proposalPersonFormBean.getDivision()!=null && proposalPersonFormBean.getDivision().length()>0){
                paramPerson.addElement(new Parameter("DIVISION",
                        DBEngineConstants.TYPE_STRING,
                        proposalPersonFormBean.getDivision()));
            } else{
                paramPerson.addElement(new Parameter("DIVISION",
                        DBEngineConstants.TYPE_STRING," "));
            }
        }
        //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - End

        paramPerson.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getProposalNumber()));
        paramPerson.addElement(new Parameter("AW_PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getAWPersonId()));
        paramPerson.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                proposalPersonFormBean.getUpdateTimestamp()));
        paramPerson.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                proposalPersonFormBean.getAcType()));

        ProcReqParameter procProp  = new ProcReqParameter();
        procProp.setDSN(DSN);
        procProp.setParameterInfo(paramPerson);
        procProp.setSqlCommand(sqlPropPerson.toString());

        return procProp;
    }


    /**
     * Method used to update/insert/delete all the details of a ProposalInvestigator
     * <li>To fetch the data, it uses DW_UPDATE_PRO_INVES procedure.
     *
     * @param ProposalInvestigatorFormBean this bean contains data for
     * insert/modify for proposal investigator.
     * @return boolean this holds true for successfull insert/modify or
     * false if fails.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter addUpdPropInvestigator( ProposalInvestigatorFormBean
            proposalInvestigatorFormBean)  throws CoeusException,DBException{
//        boolean success = false;
//        Vector procedures = new Vector(5,3);

        Vector paramInves= new Vector();
        //Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        //proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(userId);

        paramInves.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalInvestigatorFormBean.getProposalNumber()));
        paramInves.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                proposalInvestigatorFormBean.getPersonId()));
        paramInves.addElement(new Parameter("PERSON_NAME",
                DBEngineConstants.TYPE_STRING,
                proposalInvestigatorFormBean.getPersonName()));
        paramInves.addElement(new Parameter("PI_INVESTIGATOR_FLAG",
                DBEngineConstants.TYPE_STRING,
                proposalInvestigatorFormBean.isPrincipleInvestigatorFlag()? "Y": "N") );
        paramInves.addElement(new Parameter("FACULTY_FLAG",
                DBEngineConstants.TYPE_STRING,
                proposalInvestigatorFormBean.isFacultyFlag()? "Y": "N") );
        paramInves.addElement(new Parameter("NON_MIT_PERSON_FLAG",
                DBEngineConstants.TYPE_STRING,
                proposalInvestigatorFormBean.isNonMITPersonFlag()? "Y": "N") );
        paramInves.addElement(new Parameter("CONFLICT_OF_INTEREST_FLAG",
                DBEngineConstants.TYPE_STRING,
                proposalInvestigatorFormBean.isConflictOfIntersetFlag()? "Y": "N") );
        paramInves.addElement(new Parameter("PERCENTAGE_EFFORT",
                DBEngineConstants.TYPE_FLOAT,
                new Float(proposalInvestigatorFormBean.getPercentageEffort())));
        paramInves.addElement(new Parameter("FEDR_DEBR_FLAG",
                DBEngineConstants.TYPE_STRING,
                proposalInvestigatorFormBean.isFedrDebrFlag()? "Y": "N") );
        paramInves.addElement(new Parameter("FEDR_DELQ_FLAG",
                DBEngineConstants.TYPE_STRING,
                proposalInvestigatorFormBean.isFedrDelqFlag()? "Y": "N") );
        paramInves.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramInves.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramInves.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalInvestigatorFormBean.getProposalNumber()));
        paramInves.addElement(new Parameter("AW_PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                proposalInvestigatorFormBean.getPersonId()));
        //Modified on 09th Feb 2004 - start
        //paramInves.addElement(new Parameter("AW_UPDATE_USER",
        //            DBEngineConstants.TYPE_STRING, userId));
        paramInves.addElement(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING, proposalInvestigatorFormBean.getUpdateUser()));
        //Modified on 09th Feb 2004 - end
        paramInves.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                proposalInvestigatorFormBean.getUpdateTimestamp()));
        paramInves.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                proposalInvestigatorFormBean.getAcType()));

        //Added for Case# 2229 - Multi PI Enhancement
        paramInves.addElement(new Parameter("MULTI_PI_FLAG",
                DBEngineConstants.TYPE_STRING,
                proposalInvestigatorFormBean.isMultiPIFlag()? "Y": "N") );

        //Added for Case# 2270 - Tracking of Effort - Start
        paramInves.addElement(new Parameter("ACADEMIC_YEAR_EFFORT",
                DBEngineConstants.TYPE_FLOAT,
                new Float(proposalInvestigatorFormBean.getAcademicYearEffort())));
        paramInves.addElement(new Parameter("SUMMER_YEAR_EFFORT",
                DBEngineConstants.TYPE_FLOAT,
                new Float(proposalInvestigatorFormBean.getSummerYearEffort())));
        paramInves.addElement(new Parameter("CALENDAR_YEAR_EFFORT",
                DBEngineConstants.TYPE_FLOAT,
                new Float(proposalInvestigatorFormBean.getCalendarYearEffort())));
        //Added for Case# 2270 -  Tracking of Effort -End

        //Commented for Case# 2270
        // StringBuffer sqlInvestigator = new StringBuffer(
        //         "call DW_UPDATE_PRO_INVES(");

        StringBuffer sqlInvestigator = new StringBuffer(
                "call UPDATE_EPS_PROP_INVESTIGATORS(");
        sqlInvestigator.append(" <<PROPOSAL_NUMBER>> , ");
        sqlInvestigator.append(" <<PERSON_ID>> , ");
        sqlInvestigator.append(" <<PERSON_NAME>> , ");
        sqlInvestigator.append(" <<PI_INVESTIGATOR_FLAG>> , ");
        sqlInvestigator.append(" <<FACULTY_FLAG>> , ");
        sqlInvestigator.append(" <<NON_MIT_PERSON_FLAG>> , ");
        sqlInvestigator.append(" <<CONFLICT_OF_INTEREST_FLAG>> , ");
        sqlInvestigator.append(" <<PERCENTAGE_EFFORT>> , ");
        sqlInvestigator.append(" <<FEDR_DEBR_FLAG>> , ");
        sqlInvestigator.append(" <<FEDR_DELQ_FLAG>> , ");
        sqlInvestigator.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlInvestigator.append(" <<UPDATE_USER>> , ");

        //Added for Case# 2229 - Multi PI Enhancement
        sqlInvestigator.append(" <<MULTI_PI_FLAG>> , ");

        //Added for Case# 2270 - Tracking of Effort - Start
        sqlInvestigator.append(" <<ACADEMIC_YEAR_EFFORT>> , ");
        sqlInvestigator.append(" <<SUMMER_YEAR_EFFORT>> , ");
        sqlInvestigator.append(" <<CALENDAR_YEAR_EFFORT>> , ");
        //Added for Case# 2270 -  Tracking of Effort -End

        sqlInvestigator.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlInvestigator.append(" <<AW_PERSON_ID>> , ");
        sqlInvestigator.append(" <<AW_UPDATE_USER>> , ");
        sqlInvestigator.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlInvestigator.append(" <<AC_TYPE>> )");

        ProcReqParameter procInves  = new ProcReqParameter();
        procInves.setDSN(DSN);
        procInves.setParameterInfo(paramInves);
        procInves.setSqlCommand(sqlInvestigator.toString());

        return procInves;
        /*procedures.add(procInves);

        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;*/

    }

    /**
     * Method used to update/insert/delete all the details of a ProposalKeyPerson
     * <li>To fetch the data, it uses DW_UPDATE_PRO_KEYPERSONS procedure.
     *
     * @param ProposalKeyPersonFormBean this bean contains data for
     * insert/modify for proposal keyperson.
     * @return boolean this holds true for successfull insert/modify or
     * false if fails.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter addUpdPropKeyPerson( ProposalKeyPersonFormBean
            proposalKeyPersonFormBean)  throws CoeusException,DBException{
        //boolean success = false;
        //Vector procedures = new Vector(5,3);

        Vector paramKeyPerson= new Vector();
        //Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        //proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(userId);

        paramKeyPerson.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalKeyPersonFormBean.getProposalNumber()));
        paramKeyPerson.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                proposalKeyPersonFormBean.getPersonId()));
        paramKeyPerson.addElement(new Parameter("PERSON_NAME",
                DBEngineConstants.TYPE_STRING,
                proposalKeyPersonFormBean.getPersonName()));
        paramKeyPerson.addElement(new Parameter("PROJECT_ROLE",
                DBEngineConstants.TYPE_STRING,
                proposalKeyPersonFormBean.getProjectRole()));
        paramKeyPerson.addElement(new Parameter("FACULTY_FLAG",
                DBEngineConstants.TYPE_STRING,
                proposalKeyPersonFormBean.isFacultyFlag()? "Y": "N") );
        paramKeyPerson.addElement(new Parameter("NON_MIT_PERSON_FLAG",
                DBEngineConstants.TYPE_STRING,
                proposalKeyPersonFormBean.isNonMITPersonFlag()? "Y": "N") );
        paramKeyPerson.addElement(new Parameter("PERCENTAGE_EFFORT",
                DBEngineConstants.TYPE_FLOAT,
                new Float(proposalKeyPersonFormBean.getPercentageEffort() )));
// JM 7-11-2011 added academic, summer, and calendar efforts
        paramKeyPerson.addElement(new Parameter("ACADEMIC_YEAR_EFFORT",
                DBEngineConstants.TYPE_FLOAT,
                new Float(proposalKeyPersonFormBean.getAcademicYearEffort() )));
        paramKeyPerson.addElement(new Parameter("SUMMER_YEAR_EFFORT",
                DBEngineConstants.TYPE_FLOAT,
                new Float(proposalKeyPersonFormBean.getSummerYearEffort() )));
        paramKeyPerson.addElement(new Parameter("CALENDAR_YEAR_EFFORT",
                DBEngineConstants.TYPE_FLOAT,
                new Float(proposalKeyPersonFormBean.getCalendarYearEffort() )));
// END
        paramKeyPerson.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramKeyPerson.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramKeyPerson.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalKeyPersonFormBean.getProposalNumber()));
        paramKeyPerson.addElement(new Parameter("AW_PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                proposalKeyPersonFormBean.getPersonId()));
        paramKeyPerson.addElement(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING,
                proposalKeyPersonFormBean.getUpdateUser()));
        paramKeyPerson.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                proposalKeyPersonFormBean.getUpdateTimestamp()));
        paramKeyPerson.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                proposalKeyPersonFormBean.getAcType()));

        StringBuffer sqlKeyPerson = new StringBuffer(
                "call DW_UPDATE_PRO_KEYPERSONS(");
        sqlKeyPerson.append(" <<PROPOSAL_NUMBER>> , ");
        sqlKeyPerson.append(" <<PERSON_ID>> , ");
        sqlKeyPerson.append(" <<PERSON_NAME>> , ");
        sqlKeyPerson.append(" <<PROJECT_ROLE>> , ");
        sqlKeyPerson.append(" <<FACULTY_FLAG>> , ");
        sqlKeyPerson.append(" <<NON_MIT_PERSON_FLAG>> , ");
        sqlKeyPerson.append(" <<PERCENTAGE_EFFORT>> , ");
// JM 7-11-2011 added academic, summer, and calendar efforts
        sqlKeyPerson.append(" <<ACADEMIC_YEAR_EFFORT>> , ");
        sqlKeyPerson.append(" <<SUMMER_YEAR_EFFORT>> , ");
        sqlKeyPerson.append(" <<CALENDAR_YEAR_EFFORT>> , ");
// END
        sqlKeyPerson.append(" <<UPDATE_USER>> , ");
        sqlKeyPerson.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlKeyPerson.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlKeyPerson.append(" <<AW_PERSON_ID>> , ");
        sqlKeyPerson.append(" <<AW_UPDATE_USER>> , ");
        sqlKeyPerson.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlKeyPerson.append(" <<AC_TYPE>> )");

        ProcReqParameter procKeyPerson  = new ProcReqParameter();
        procKeyPerson.setDSN(DSN);
        procKeyPerson.setParameterInfo(paramKeyPerson);
        procKeyPerson.setSqlCommand(sqlKeyPerson.toString());

        return procKeyPerson;
        /*procedures.add(procKeyPerson);

        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;*/

    }

    /**
     * Method used to update/insert/delete all the details of a ProposalScienceCode
     * <li>To fetch the data, it uses DW_UPDATE_PROP_SCIENCE_CODE procedure.
     *
     * @param ProposalScienceCodeFormBean this bean contains data for
     * insert/modify for proposal ScienceCode.
     * @return boolean this holds true for successfull insert/modify or
     * false if fails.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter addDeletePropScienceCode( ProposalScienceCodeFormBean
            proposalScienceCodeFormBean)  throws CoeusException,DBException{
        //boolean success = false;
        //Vector procedures = new Vector(5,3);

        Vector paramScienceCode= new Vector();
        //Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        //proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(userId);

        paramScienceCode.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalScienceCodeFormBean.getProposalNumber()));
        paramScienceCode.addElement(new Parameter("SCIENCE_CODE",
                DBEngineConstants.TYPE_STRING,
                proposalScienceCodeFormBean.getScienceCode()));
        paramScienceCode.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramScienceCode.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramScienceCode.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalScienceCodeFormBean.getProposalNumber()));
        paramScienceCode.addElement(new Parameter("AW_SCIENCE_CODE",
                DBEngineConstants.TYPE_STRING,
                proposalScienceCodeFormBean.getScienceCode()));
        paramScienceCode.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                proposalScienceCodeFormBean.getUpdateTimestamp()));
        paramScienceCode.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                proposalScienceCodeFormBean.getAcType()));

        StringBuffer sqlScienceCode = new StringBuffer(
                "call DW_UPDATE_PROP_SCIENCE_CODE(");
        sqlScienceCode.append(" <<PROPOSAL_NUMBER>> , ");
        sqlScienceCode.append(" <<SCIENCE_CODE>> , ");
        sqlScienceCode.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlScienceCode.append(" <<UPDATE_USER>> , ");
        sqlScienceCode.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlScienceCode.append(" <<AW_SCIENCE_CODE>> , ");
        sqlScienceCode.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlScienceCode.append(" <<AC_TYPE>> )");

        ProcReqParameter procScienceCode  = new ProcReqParameter();
        procScienceCode.setDSN(DSN);
        procScienceCode.setParameterInfo(paramScienceCode);
        procScienceCode.setSqlCommand(sqlScienceCode.toString());

        return procScienceCode;
        /*procedures.add(procScienceCode);

        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;*/

    }

    /**
     *  Method used to update/insert all the details of a Proposal person others
     *  details for person.
     *
     *  @param ProposalCustomElementsInfoBean details of others details.
     *  @return boolean this holds true for successfull insert/modify or
     *  false if fails.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter addUpdProposalOther( ProposalCustomElementsInfoBean
            proposalOthersFormBean)  throws CoeusException,DBException{
        //boolean success = false;
        //Vector procedures = new Vector(5,3);

        Vector paramProposalOther= new Vector();
        //Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        //proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(userId);

        paramProposalOther.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalOthersFormBean.getProposalNumber()));
        paramProposalOther.addElement(new Parameter("COLUMN_NAME",
                DBEngineConstants.TYPE_STRING,
                proposalOthersFormBean.getColumnName()));
        paramProposalOther.addElement(new Parameter("COLUMN_VALUE",
                DBEngineConstants.TYPE_STRING,
                proposalOthersFormBean.getColumnValue()));
        paramProposalOther.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramProposalOther.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramProposalOther.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalOthersFormBean.getProposalNumber()));
        paramProposalOther.addElement(new Parameter("AW_COLUMN_NAME",
                DBEngineConstants.TYPE_STRING,
                proposalOthersFormBean.getColumnName()));
        paramProposalOther.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                proposalOthersFormBean.getUpdateTimestamp()));
        paramProposalOther.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                proposalOthersFormBean.getAcType()));

        StringBuffer sqlProposalOther = new StringBuffer(
                "call DW_UPDATE_EPS_PROP_CUSTOM_DATA(");
        sqlProposalOther.append(" <<PROPOSAL_NUMBER>> , ");
        sqlProposalOther.append(" <<COLUMN_NAME>> , ");
        sqlProposalOther.append(" <<COLUMN_VALUE>> , ");
        sqlProposalOther.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlProposalOther.append(" <<UPDATE_USER>> , ");
        sqlProposalOther.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlProposalOther.append(" <<AW_COLUMN_NAME>> , ");
        sqlProposalOther.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlProposalOther.append(" <<AC_TYPE>> )");

        ProcReqParameter procProposalOther  = new ProcReqParameter();
        procProposalOther.setDSN(DSN);
        procProposalOther.setParameterInfo(paramProposalOther);
        procProposalOther.setSqlCommand(sqlProposalOther.toString());

        return procProposalOther;
        /*procedures.add(procProposalOther);

        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;*/

    }

    /**
     * Method used to update/insert/delete all the details of a ProposalLeadUnit
     * <li>To fetch the data, it uses DW_UPDATE_PROPOSAL_UNITS procedure.
     *
     * @param ProposalLeadUnitFormBean this bean contains data for
     * insert/modify for proposal LeadUnit.
     * @return boolean this holds true for successfull insert/modify or
     * false if fails.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter addUpdProposalLeadUnit( ProposalLeadUnitFormBean
            proposalLeadUnitFormBean)  throws CoeusException,DBException{
        //boolean success = false;
        //Vector procedures = new Vector(5,3);

        Vector paramLeadUnit= new Vector();
        //Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        //proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(userId);

        paramLeadUnit.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalLeadUnitFormBean.getProposalNumber()));
        paramLeadUnit.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                proposalLeadUnitFormBean.getPersonId()));
        paramLeadUnit.addElement(new Parameter("UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalLeadUnitFormBean.getUnitNumber()));
        paramLeadUnit.addElement(new Parameter("LEAD_UNIT_FLAG",
                DBEngineConstants.TYPE_STRING,
                proposalLeadUnitFormBean.isLeadUnitFlag()? "Y": "N"));
        paramLeadUnit.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramLeadUnit.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramLeadUnit.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalLeadUnitFormBean.getProposalNumber()));
        paramLeadUnit.addElement(new Parameter("AW_PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                proposalLeadUnitFormBean.getPersonId()));
        paramLeadUnit.addElement(new Parameter("AW_UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalLeadUnitFormBean.getUnitNumber()));
        //Modified on 09th Feb 2004 - start
        //paramLeadUnit.addElement(new Parameter("AW_UPDATE_USER",
        //            DBEngineConstants.TYPE_STRING, userId));
        paramLeadUnit.addElement(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING, proposalLeadUnitFormBean.getUpdateUser()));
        //Modified on 09th Feb 2004 - end
        paramLeadUnit.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                proposalLeadUnitFormBean.getUpdateTimestamp()));
        paramLeadUnit.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                proposalLeadUnitFormBean.getAcType()));

        StringBuffer sqlLeadUnit = new StringBuffer(
                "call DW_UPDATE_PROPOSAL_UNITS(");
        sqlLeadUnit.append(" <<PROPOSAL_NUMBER>> , ");
        sqlLeadUnit.append(" <<PERSON_ID>> , ");
        sqlLeadUnit.append(" <<UNIT_NUMBER>> , ");
        sqlLeadUnit.append(" <<LEAD_UNIT_FLAG>> , ");
        sqlLeadUnit.append(" <<UPDATE_USER>> , ");
        sqlLeadUnit.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlLeadUnit.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlLeadUnit.append(" <<AW_PERSON_ID>> , ");
        sqlLeadUnit.append(" <<AW_UNIT_NUMBER>> , ");
        sqlLeadUnit.append(" <<AW_UPDATE_USER>> , ");
        sqlLeadUnit.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlLeadUnit.append(" <<AC_TYPE>> )");

        ProcReqParameter procLeadUnit  = new ProcReqParameter();
        procLeadUnit.setDSN(DSN);
        procLeadUnit.setParameterInfo(paramLeadUnit);
        procLeadUnit.setSqlCommand(sqlLeadUnit.toString());

        return procLeadUnit;
        /*procedures.add(procLeadUnit);

        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;*/

    }
    //Proposal Development Save Key Person Units START
    public ProcReqParameter addUpdProposal_kp_Unit( KeyPersonUnitBean
            proposalKPUnitFormBean)  throws CoeusException,DBException{
        //boolean success = false;
        //Vector procedures = new Vector(5,3);

        Vector paramLeadUnit= new Vector();
        //Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        //proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(userId);

        paramLeadUnit.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalKPUnitFormBean.getProposalNumber()));
        paramLeadUnit.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                proposalKPUnitFormBean.getPersonId()));
        paramLeadUnit.addElement(new Parameter("UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalKPUnitFormBean.getUnitNumber()));

        paramLeadUnit.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramLeadUnit.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramLeadUnit.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalKPUnitFormBean.getProposalNumber()));
        paramLeadUnit.addElement(new Parameter("AW_PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                proposalKPUnitFormBean.getPersonId()));
        paramLeadUnit.addElement(new Parameter("AW_UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalKPUnitFormBean.getUnitNumber()));
        //Modified on 09th Feb 2004 - start
        //paramLeadUnit.addElement(new Parameter("AW_UPDATE_USER",
        //            DBEngineConstants.TYPE_STRING, userId));
        paramLeadUnit.addElement(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING, proposalKPUnitFormBean.getUpdateUser()));
        //Modified on 09th Feb 2004 - end
        paramLeadUnit.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                proposalKPUnitFormBean.getUpdateTimestamp()));
        paramLeadUnit.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                proposalKPUnitFormBean.getAcType()));

        StringBuffer sqlLeadUnit = new StringBuffer(
                "call UPDATE_PROPOSAL_KP_UNITS(");
        sqlLeadUnit.append(" <<PROPOSAL_NUMBER>> , ");
        sqlLeadUnit.append(" <<PERSON_ID>> , ");
        sqlLeadUnit.append(" <<UNIT_NUMBER>> , ");
        sqlLeadUnit.append(" <<UPDATE_USER>> , ");
        sqlLeadUnit.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlLeadUnit.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlLeadUnit.append(" <<AW_PERSON_ID>> , ");
        sqlLeadUnit.append(" <<AW_UNIT_NUMBER>> , ");
        sqlLeadUnit.append(" <<AW_UPDATE_USER>> , ");
        sqlLeadUnit.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlLeadUnit.append(" <<AC_TYPE>> )");

        ProcReqParameter procLeadUnit  = new ProcReqParameter();
        procLeadUnit.setDSN(DSN);
        procLeadUnit.setParameterInfo(paramLeadUnit);
        procLeadUnit.setSqlCommand(sqlLeadUnit.toString());

        return procLeadUnit;
        /*procedures.add(procLeadUnit);

        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;*/

    }
    //Proposal Development Save Key Person Units END


    //Commented for case 2406 - Organization and Location - start
    /**
     *  Method used to update/insert all the details of a Proposal Location.
     *  <li>To fetch the data, it uses UPD_PROP_LOCATION procedure.
     *
     *  @param proposalLocationFormBean this bean contains data for insert.
     *  @return boolean this holds true for successfull insert/modify or
     *  false if fails.
     *  this class before executing the procedure.
     *  @exception DBException if the instance of a dbEngine is null.
     */
//    public ProcReqParameter addUpdProposalLocations( ProposalLocationFormBean
//            proposalLocationFormBean)  throws CoeusException,DBException{
//
//        //boolean success = false;
//        //Vector procedures = new Vector(5,3);
//
//        Vector paramLocations = new Vector();
//        //Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
//        //proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(userId);
//
//        paramLocations.addElement(new Parameter("PROPOSAL_NUMBER",
//                DBEngineConstants.TYPE_STRING,
//                proposalLocationFormBean.getProposalNumber()));
//        paramLocations.addElement(new Parameter("LOCATION",
//                DBEngineConstants.TYPE_STRING,
//                proposalLocationFormBean.getProposalLocation()));
//        paramLocations.addElement(new Parameter("ROLODEX_ID",
//                DBEngineConstants.TYPE_STRING,
//                proposalLocationFormBean.getRolodexId() == 0 ? null :
//                    ""+proposalLocationFormBean.getRolodexId()));
//        paramLocations.addElement(new Parameter("UPDATE_USER",
//                DBEngineConstants.TYPE_STRING,userId));
//        paramLocations.addElement(new Parameter("UPDATE_TIMESTAMP",
//                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
//        paramLocations.addElement(new Parameter("AW_PROPOSAL_NUMBER",
//                DBEngineConstants.TYPE_STRING,
//                proposalLocationFormBean.getProposalNumber()));
//        paramLocations.addElement(new Parameter("AW_LOCATION",
//                DBEngineConstants.TYPE_STRING,
//                proposalLocationFormBean.getProposalLocation()));
//        paramLocations.addElement(new Parameter("AW_UPDATE_USER",
//                DBEngineConstants.TYPE_STRING, userId));
//        paramLocations.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
//                DBEngineConstants.TYPE_TIMESTAMP,
//                proposalLocationFormBean.getUpdateTimestamp()));
//        paramLocations.addElement(new Parameter("AC_TYPE",
//                DBEngineConstants.TYPE_STRING,
//                proposalLocationFormBean.getAcType()));
//
//        StringBuffer sqlLocations = new StringBuffer(
//                "call UPD_PROP_LOCATION(");
//        sqlLocations.append(" <<PROPOSAL_NUMBER>> , ");
//        sqlLocations.append(" <<LOCATION>> , ");
//        sqlLocations.append(" <<ROLODEX_ID>> , ");
//        sqlLocations.append(" <<UPDATE_USER>> , ");
//        sqlLocations.append(" <<UPDATE_TIMESTAMP>> , ");
//        sqlLocations.append(" <<AW_PROPOSAL_NUMBER>> , ");
//        sqlLocations.append(" <<AW_LOCATION>> , ");
//        sqlLocations.append(" <<AW_UPDATE_USER>> , ");
//        sqlLocations.append(" <<AW_UPDATE_TIMESTAMP>> , ");
//        sqlLocations.append(" <<AC_TYPE>> )");
//
//        ProcReqParameter procLocations  = new ProcReqParameter();
//        procLocations.setDSN(DSN);
//        procLocations.setParameterInfo(paramLocations);
//        procLocations.setSqlCommand(sqlLocations.toString());
//
//        return procLocations;
//        /*procedures.add(procLocations);
//
//        if(dbEngine!=null){
//            dbEngine.executeStoreProcs(procedures);
//        }else{
//            throw new CoeusException("db_exceptionCode.1000");
//        }
//        success = true;
//        return success;*/
//    }
    //Commented for case 2406 - Organization and Location - end

    /**
     *  Method used to update/insert all the details of a Proposal YesNoQuestion.
     *  <li>To fetch the data, it uses DW_UPDATE_PROP_PERS_YNQ procedure.
     *
     *  @param ProposalYNQFormBean this bean contains data for insert.
     *  @return boolean this holds true for successfull insert/modify or
     *  false if fails.
     *  this class before executing the procedure.
     *  @exception DBException if the instance of a dbEngine is null.
     */
    public ProcReqParameter addUpdProposalYNQ( ProposalYNQFormBean
            proposalYNQFormBean)  throws CoeusException,DBException{
        //boolean success = false;
        //Vector procedures = new Vector(5,3);

        Vector paramProposalYNQ= new Vector();
        //Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        //proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(userId);

        paramProposalYNQ.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalYNQFormBean.getProposalNumber()));
        paramProposalYNQ.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                proposalYNQFormBean.getPersonId()));
        paramProposalYNQ.addElement(new Parameter("QUESTION_ID",
                DBEngineConstants.TYPE_STRING,
                proposalYNQFormBean.getQuestionId()));
        paramProposalYNQ.addElement(new Parameter("ANSWER",
                DBEngineConstants.TYPE_STRING,
                proposalYNQFormBean.getAnswer()));
        paramProposalYNQ.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramProposalYNQ.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramProposalYNQ.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalYNQFormBean.getProposalNumber()));
        paramProposalYNQ.addElement(new Parameter("AW_PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                proposalYNQFormBean.getPersonId()));
        paramProposalYNQ.addElement(new Parameter("AW_QUESTION_ID",
                DBEngineConstants.TYPE_STRING,
                proposalYNQFormBean.getQuestionId()));
        paramProposalYNQ.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                proposalYNQFormBean.getUpdateTimeStamp()));
        paramProposalYNQ.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                proposalYNQFormBean.getAcType()));

        StringBuffer sqlProposalYNQ = new StringBuffer(
                "call DW_UPDATE_PROP_PERS_YNQ(");
        sqlProposalYNQ.append(" <<PROPOSAL_NUMBER>> , ");
        sqlProposalYNQ.append(" <<PERSON_ID>> , ");
        sqlProposalYNQ.append(" <<QUESTION_ID>> , ");
        sqlProposalYNQ.append(" <<ANSWER>> , ");
        sqlProposalYNQ.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlProposalYNQ.append(" <<UPDATE_USER>> , ");
        sqlProposalYNQ.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlProposalYNQ.append(" <<AW_PERSON_ID>> , ");
        sqlProposalYNQ.append(" <<AW_QUESTION_ID>> , ");
        sqlProposalYNQ.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlProposalYNQ.append(" <<AC_TYPE>> )");

        ProcReqParameter procYNQ  = new ProcReqParameter();
        procYNQ.setDSN(DSN);
        procYNQ.setParameterInfo(paramProposalYNQ);
        procYNQ.setSqlCommand(sqlProposalYNQ.toString());
/*        procedures.add(procYNQ);

        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;*/
        return procYNQ;

    }

    /**  Method used to update/Delete proposal certify details of a Proposal screen.
     *  <li>To fetch the data, it uses DW_UPD_PROP_INV_CERTIFICATION procedure.
     *
     * @return boolean true for successful insert/modify
     * @param proposalCertificationFormBean contains Proposal Certificate information.
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the DB instance is not available. */
    public boolean updDeleteProposalCertify( ProposalCertificationFormBean
            proposalCertificationFormBean)  throws CoeusException ,DBException{
        boolean success = false;
        Vector procedures = new Vector(5,3);
        Vector paramPersonCertify= new Vector();
        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        //proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(userId);

        if (proposalCertificationFormBean.getAcType().equals("I")) {
            throw new CoeusException("Check the AC type for Certify");
        }
        paramPersonCertify.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalCertificationFormBean.getProposalNumber()));
        paramPersonCertify.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                proposalCertificationFormBean.getPersonId()));
        paramPersonCertify.addElement(new Parameter("CERTIFIED_FLAG",
                DBEngineConstants.TYPE_STRING,
                proposalCertificationFormBean.isCertifyFlag()? "Y": "N") );
        paramPersonCertify.addElement(new Parameter("DATE_CERTIFIED",
                DBEngineConstants.TYPE_DATE,
                proposalCertificationFormBean.getDateCertify()));
        paramPersonCertify.addElement(new Parameter("DATE_RECEIVED_BY_OSP",
                DBEngineConstants.TYPE_DATE,
                proposalCertificationFormBean.getDateReceivedByOsp()));
        paramPersonCertify.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramPersonCertify.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramPersonCertify.addElement(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING,
                proposalCertificationFormBean.getUpdateUser()));
        paramPersonCertify.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                proposalCertificationFormBean.getUpdateTimestamp()));
        paramPersonCertify.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                proposalCertificationFormBean.getAcType()));

        StringBuffer sqlPersonCertify = new StringBuffer(
                "call DW_UPD_PROP_INV_CERTIFICATION(");
        sqlPersonCertify.append(" <<PROPOSAL_NUMBER>> , ");
        sqlPersonCertify.append(" <<PERSON_ID>> , ");
        sqlPersonCertify.append(" <<CERTIFIED_FLAG>> , ");
        sqlPersonCertify.append(" <<DATE_CERTIFIED>> , ");
        sqlPersonCertify.append(" <<DATE_RECEIVED_BY_OSP>> , ");
        sqlPersonCertify.append(" <<UPDATE_USER>> , ");
        sqlPersonCertify.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlPersonCertify.append(" <<AW_UPDATE_USER>> , ");
        sqlPersonCertify.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlPersonCertify.append(" <<AC_TYPE>> )");

        ProcReqParameter procPersonCertify  = new ProcReqParameter();
        procPersonCertify.setDSN(DSN);
        procPersonCertify.setParameterInfo(paramPersonCertify);
        procPersonCertify.setSqlCommand(sqlPersonCertify.toString());

        procedures.add(procPersonCertify);

        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
    }

    /**  Method used to update/Delete Proposal Viewers.
     *
     * @return boolean indicating whether the update was successfull or not .
     * @param vctProposalUserRoles vector of ProposalRolesFormBean.
     * @param proposalNumber Proposal Number
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the DB instance is not available. */
    public boolean addDeleteProposalViewers(Vector vctProposalUserRoles, String proposalNumber)
    throws CoeusException ,DBException{
        Vector procedures = new Vector(5,3);
//        Vector paramRolesUser= new Vector();
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        boolean success;
        int intRoleSize = vctProposalUserRoles.size();
        ProposalRolesFormBean proposalRolesFormBean;
        ProposalNarrativeModuleUsersFormBean proposalNarrativeModuleUsersFormBean;
//        ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
        ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
        String rolesUserId = "";

        Vector narrativeModules = proposalNarrativeTxnBean.getProposalNarrativeModules(proposalNumber);

        int intNarrModuleSize = 0;
        if(narrativeModules != null) {
            intNarrModuleSize = narrativeModules.size();
        }

        for( int intRow = 0 ; intRow < intRoleSize; intRow++) {
            proposalRolesFormBean = (ProposalRolesFormBean) vctProposalUserRoles.elementAt( intRow );
            if(proposalRolesFormBean != null){
                rolesUserId = proposalRolesFormBean.getUserId();
                proposalRolesFormBean.setUpdateTimestamp(dbTimestamp);
                procedures.add(addDeleteProposalRolesForUser(proposalRolesFormBean));

                for( int intNarrRow = 0 ; intNarrRow < intNarrModuleSize; intNarrRow++) {
                    proposalNarrativeModuleUsersFormBean = new ProposalNarrativeModuleUsersFormBean();
                    if(proposalRolesFormBean != null){

                        Integer moduleIntNumber = (Integer)narrativeModules.elementAt(intNarrRow);
                        int moduleNumber = moduleIntNumber.intValue();
                        proposalNarrativeModuleUsersFormBean.setProposalNumber(proposalNumber);
                        proposalNarrativeModuleUsersFormBean.setModuleNumber(moduleNumber);
                        proposalNarrativeModuleUsersFormBean.setAcType("I");
                        proposalNarrativeModuleUsersFormBean.setAccessType('R');
                        proposalNarrativeModuleUsersFormBean.setUpdateTimestamp(dbTimestamp);
                        proposalNarrativeModuleUsersFormBean.setUpdateUser(userId);
                        proposalNarrativeModuleUsersFormBean.setUserId(rolesUserId);
                        procedures.add(addUpdDelProposalNarrativeUsers(proposalNarrativeModuleUsersFormBean));
                    }
                }
            }
        }


        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
    }



    /**  Method used to update/Delete proposal user role details of a Proposal screen.
     *  <li>To fetch the data, it uses UPD_PROPOSAL_USER_ROLES procedure.
     *
     * @return ProcReqParameter the DSN,StringBuffer and parameter is set in
     *  this class before executing the procedure.
     * @param proposalUserRoleFormBean contains Proposal User Roles information
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the DB instance is not available. */
    public ProcReqParameter addDeleteProposalRolesForUser( ProposalRolesFormBean
            proposalUserRoleFormBean)  throws CoeusException ,DBException{
//        boolean success = false;
//        Vector procedures = new Vector(5,3);
        Vector paramRolesUser= new Vector();
        //Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        //proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(userId);

        paramRolesUser.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalUserRoleFormBean.getProposalNumber()));
        paramRolesUser.addElement(new Parameter("USER_ID",
                DBEngineConstants.TYPE_STRING,
                proposalUserRoleFormBean.getUserId()));
        paramRolesUser.addElement(new Parameter("ROLE_ID",
                DBEngineConstants.TYPE_INT,
                ""+proposalUserRoleFormBean.getRoleId()) );
        paramRolesUser.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramRolesUser.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramRolesUser.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalUserRoleFormBean.getProposalNumber()));
        paramRolesUser.addElement(new Parameter("AW_USER_ID",
                DBEngineConstants.TYPE_STRING,
                proposalUserRoleFormBean.getUserId()));
        paramRolesUser.addElement(new Parameter("AW_ROLE_ID",
                DBEngineConstants.TYPE_INT,
                ""+proposalUserRoleFormBean.getRoleId()) );
        paramRolesUser.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                proposalUserRoleFormBean.getUpdateTimestamp()));
        paramRolesUser.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                proposalUserRoleFormBean.getAcType()));

        StringBuffer sqlRolesUser = new StringBuffer(
                "call UPD_PROPOSAL_USER_ROLES(");
        sqlRolesUser.append(" <<PROPOSAL_NUMBER>> , ");
        sqlRolesUser.append(" <<USER_ID>> , ");
        sqlRolesUser.append(" <<ROLE_ID>> , ");
        sqlRolesUser.append(" <<UPDATE_USER>> , ");
        sqlRolesUser.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlRolesUser.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlRolesUser.append(" <<AW_USER_ID>> , ");
        sqlRolesUser.append(" <<AW_ROLE_ID>> , ");
        sqlRolesUser.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlRolesUser.append(" <<AC_TYPE>> )");

        ProcReqParameter procRolesUser  = new ProcReqParameter();
        procRolesUser.setDSN(DSN);
        procRolesUser.setParameterInfo(paramRolesUser);
        procRolesUser.setSqlCommand(sqlRolesUser.toString());

        return procRolesUser;
    }

    /**  This method is used to insert/update/delete Proposal abstract details.
     *  <li>To update the data, it uses DW_UPDATE_PROP_ABSTRACT procedure.
     *
     * @param proposalAbstractFormBean this bean contains data for
     *  Add/Update/Delete Proposal Abstracts.
     * @return boolean true for successful insert/delete
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the DB instance is not available.
     */

    // Commented by Shivakumar for CLOB implementation - BEGIN
//     public boolean addUpdDeleteProposalAbstract( ProposalAbstractFormBean
//             proposalAbstractFormBean)  throws CoeusException ,DBException{
//        boolean success = false;
//        Vector procedures = new Vector(5,3);
//        Vector paramPropAbstract= new Vector();
//        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
//        //proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(userId);
//
//        paramPropAbstract.addElement(new Parameter("PROPOSAL_NUMBER",
//                    DBEngineConstants.TYPE_STRING,
//                            proposalAbstractFormBean.getProposalNumber()));
//        paramPropAbstract.addElement(new Parameter("ABSTRACT_TYPE_CODE",
//                    DBEngineConstants.TYPE_INT,
//                            ""+proposalAbstractFormBean.getAbstractTypeCode()));
//        paramPropAbstract.addElement(new Parameter("ABSTRACT",
//             DBEngineConstants.TYPE_STRING,
//                        proposalAbstractFormBean.getAbstractText()));
//        paramPropAbstract.addElement(new Parameter("UPDATE_TIMESTAMP",
//                    DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
//        paramPropAbstract.addElement(new Parameter("UPDATE_USER",
//                    DBEngineConstants.TYPE_STRING, userId));
//        paramPropAbstract.addElement(new Parameter("AW_PROPOSAL_NUMBER",
//                    DBEngineConstants.TYPE_STRING,
//                            proposalAbstractFormBean.getProposalNumber()));
//        paramPropAbstract.addElement(new Parameter("AW_ABSTRACT_TYPE_CODE",
//                    DBEngineConstants.TYPE_INT,
//                            ""+proposalAbstractFormBean.getAwAbstractTypeCode()));
//        paramPropAbstract.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
//                    DBEngineConstants.TYPE_TIMESTAMP,proposalAbstractFormBean.getUpdateTimestamp()));
//        paramPropAbstract.addElement(new Parameter("AC_TYPE",
//                    DBEngineConstants.TYPE_STRING,
//                            proposalAbstractFormBean.getAcType()));
//
//        StringBuffer sqlNarrative = new StringBuffer(
//                                        "call DW_UPDATE_PROP_ABSTRACT(");
//        sqlNarrative.append(" <<PROPOSAL_NUMBER>> , ");
//        sqlNarrative.append(" <<ABSTRACT_TYPE_CODE>> , ");
//        sqlNarrative.append(" <<ABSTRACT>> , ");
//        sqlNarrative.append(" <<UPDATE_TIMESTAMP>> , ");
//        sqlNarrative.append(" <<UPDATE_USER>> , ");
//        sqlNarrative.append(" <<AW_PROPOSAL_NUMBER>> , ");
//        sqlNarrative.append(" <<AW_ABSTRACT_TYPE_CODE>> , ");
//        sqlNarrative.append(" <<AW_UPDATE_TIMESTAMP>> , ");
//        sqlNarrative.append(" <<AC_TYPE>> )");
//
//        ProcReqParameter procPropAbstract  = new ProcReqParameter();
//        procPropAbstract.setDSN(DSN);
//        procPropAbstract.setParameterInfo(paramPropAbstract);
//        procPropAbstract.setSqlCommand(sqlNarrative.toString());
//
//        procedures.add(procPropAbstract);
//
//        if(dbEngine!=null){
//            dbEngine.executeStoreProcs(procedures);
//        }else{
//            throw new CoeusException("db_exceptionCode.1000");
//        }
//        success = true;
//        return success;
//    }
    // Commented by Shivakumar for CLOB implementation - END

    // Code added by Shivakumar for CLOB implementation - BEGIN

    public boolean addUpdDeleteProposalAbstract( ProposalAbstractFormBean
            proposalAbstractFormBean)  throws CoeusException ,DBException{
        boolean success = false;
        Vector procedures = new Vector(5,3);
        Vector paramPropAbstract= new Vector();
        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        //proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(userId);

        paramPropAbstract.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalAbstractFormBean.getProposalNumber()));
        paramPropAbstract.addElement(new Parameter("ABSTRACT_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+proposalAbstractFormBean.getAbstractTypeCode()));
        paramPropAbstract.addElement(new Parameter("ABSTRACT",
                DBEngineConstants.TYPE_CLOB,
                proposalAbstractFormBean.getAbstractText()));
        paramPropAbstract.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramPropAbstract.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
//        paramPropAbstract.addElement(new Parameter("AW_PROPOSAL_NUMBER",
//                    DBEngineConstants.TYPE_STRING,
//                            proposalAbstractFormBean.getProposalNumber()));
//        paramPropAbstract.addElement(new Parameter("AW_ABSTRACT_TYPE_CODE",
//                    DBEngineConstants.TYPE_INT,
//                            ""+proposalAbstractFormBean.getAwAbstractTypeCode()));
//        paramPropAbstract.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
//                    DBEngineConstants.TYPE_TIMESTAMP,proposalAbstractFormBean.getUpdateTimestamp()));
//        paramPropAbstract.addElement(new Parameter("AC_TYPE",
//                    DBEngineConstants.TYPE_STRING,
//                            proposalAbstractFormBean.getAcType()));

        StringBuffer sqlProposal = new StringBuffer("");

        if(proposalAbstractFormBean.getAcType().trim().equals("I")){
            sqlProposal.append("insert into osp$eps_prop_abstract(");
            sqlProposal.append(" PROPOSAL_NUMBER , ");
            sqlProposal.append(" ABSTRACT_TYPE_CODE , ");
            sqlProposal.append(" ABSTRACT , ");
            sqlProposal.append(" UPDATE_TIMESTAMP , ");
            sqlProposal.append(" UPDATE_USER ) ");
            sqlProposal.append(" VALUES (");
            sqlProposal.append(" <<PROPOSAL_NUMBER>> , ");
            sqlProposal.append(" <<ABSTRACT_TYPE_CODE>> , ");
            sqlProposal.append(" <<ABSTRACT>> , ");
            sqlProposal.append(" <<UPDATE_TIMESTAMP>> , ");
            sqlProposal.append(" <<UPDATE_USER>> ) ");
            //System.out.println("insert statement=>"+sqlAward.toString());
        }else{


            paramPropAbstract.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                    DBEngineConstants.TYPE_STRING, proposalAbstractFormBean.getProposalNumber()));
            paramPropAbstract.addElement(new Parameter("AW_ABSTRACT_TYPE_CODE",
                    DBEngineConstants.TYPE_INT, ""+proposalAbstractFormBean.getAwAbstractTypeCode()));
            paramPropAbstract.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,proposalAbstractFormBean.getUpdateTimestamp()));

            if(proposalAbstractFormBean.getAcType().trim().equals("U")){
                sqlProposal.append("update osp$eps_prop_abstract set");
                sqlProposal.append(" PROPOSAL_NUMBER =  ");
                sqlProposal.append(" <<PROPOSAL_NUMBER>> , ");
                sqlProposal.append(" ABSTRACT_TYPE_CODE = ");
                sqlProposal.append(" <<ABSTRACT_TYPE_CODE>> , ");
                sqlProposal.append(" ABSTRACT = ");
                sqlProposal.append(" <<ABSTRACT>> , ");
                sqlProposal.append(" UPDATE_TIMESTAMP = ");
                sqlProposal.append(" <<UPDATE_TIMESTAMP>> , ");
                sqlProposal.append(" UPDATE_USER = ");
                sqlProposal.append(" <<UPDATE_USER>>  ");
                sqlProposal.append(" where ");
                sqlProposal.append(" PROPOSAL_NUMBER = ");
                sqlProposal.append(" <<AW_PROPOSAL_NUMBER>> ");
                sqlProposal.append(" and ABSTRACT_TYPE_CODE = ");
                sqlProposal.append(" <<AW_ABSTRACT_TYPE_CODE>> ");
                sqlProposal.append(" and UPDATE_TIMESTAMP = ");
                sqlProposal.append(" <<AW_UPDATE_TIMESTAMP>> ");
            }else if(proposalAbstractFormBean.getAcType().trim().equals("D")){
                sqlProposal.append(" delete from osp$eps_prop_abstract where ");
                sqlProposal.append(" PROPOSAL_NUMBER = ");
                sqlProposal.append(" <<AW_PROPOSAL_NUMBER>> ");
                sqlProposal.append(" and ABSTRACT_TYPE_CODE = ");
                sqlProposal.append(" <<AW_ABSTRACT_TYPE_CODE>> ");
                sqlProposal.append(" and UPDATE_TIMESTAMP = ");
                sqlProposal.append(" <<AW_UPDATE_TIMESTAMP>> ");
                //System.out.println("delete statement=>"+sqlBudget.toString());
            }

        }

        ProcReqParameter procPropAbstract  = new ProcReqParameter();
        procPropAbstract.setDSN(DSN);
        procPropAbstract.setParameterInfo(paramPropAbstract);
        procPropAbstract.setSqlCommand(sqlProposal.toString());

        procedures.add(procPropAbstract);
        // Code commented by Shivakumar for CLOB implementation - BEGIN
//        if(dbEngine!=null){
//            dbEngine.executeStoreProcs(procedures);
//        }else{
//            throw new CoeusException("db_exceptionCode.1000");
//        }
        // Code commented by Shivakumar for CLOB implementation - END
        // Code added by Shivakumar for CLOB implementation - BEGIN
        if(dbEngine!=null){
            java.sql.Connection conn = null;
            try{
                conn = dbEngine.beginTxn();
                if((procedures != null) && (procedures.size() > 0)){
                    dbEngine.batchSQLUpdate(procedures, conn);
                }
                dbEngine.commit(conn);
            }catch(Exception sqlEx){
                dbEngine.rollback(conn);
                throw new CoeusException(sqlEx.getMessage());
            }finally{
                dbEngine.endTxn(conn);
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        // Code added by Shivakumar for CLOB implementation - END



        success = true;
        return success;
    }








    // Code added by Shivakumar for CLOB implementation - END


    /**
     *  This Method is used to Insert/Update/Delete Proposal Yes/No Question Answers.
     *  <li>To update the data, it uses DW_UPDATE_PRO_YNQ procedure.
     *
     *  @param proposalYNQBean contains data for Insert/Update/Delete Proposal Yes/No Question Answers.
     *  @return boolean true for successful insert/update/delete
     *  @exception DBException if the instance of a dbEngine is null.
     *  @exception CoeusException if the DB instance is not available.
     *  @exception ParseException raised while parsing date.
     */
    public ProcReqParameter addUpdDeleteProposalYNQ( ProposalYNQBean
            proposalYNQBean)  throws CoeusException ,DBException, ParseException{
//        boolean success = false;
//        Vector procedures = new Vector(5,3);
        Vector paramPropYNQ= new Vector();
        //Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        //System.out.println("DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp = " + dbTimestamp);
        paramPropYNQ.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalYNQBean.getProposalNumber()));
        paramPropYNQ.addElement(new Parameter("QUESTION_ID",
                DBEngineConstants.TYPE_STRING,
                ""+proposalYNQBean.getQuestionId()));
        paramPropYNQ.addElement(new Parameter("ANSWER",
                DBEngineConstants.TYPE_STRING,
                proposalYNQBean.getAnswer()));
        paramPropYNQ.addElement(new Parameter("EXPLANATION",
                DBEngineConstants.TYPE_STRING,
                proposalYNQBean.getExplanation()));
        paramPropYNQ.addElement(new Parameter("REVIEW_DATE",
                DBEngineConstants.TYPE_DATE,
                convertToDate(proposalYNQBean.getReviewDate())));
        paramPropYNQ.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramPropYNQ.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramPropYNQ.addElement(new Parameter("EXISTING_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalYNQBean.getProposalNumber()));
        paramPropYNQ.addElement(new Parameter("EXISTING_QUESTION_ID",
                DBEngineConstants.TYPE_STRING,
                ""+proposalYNQBean.getQuestionId()));
        paramPropYNQ.addElement(new Parameter("EXISTING_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,proposalYNQBean.getUpdateTimeStamp()));
        paramPropYNQ.addElement(new Parameter("EXISTING_UPDATE_USER",
                DBEngineConstants.TYPE_STRING, proposalYNQBean.getUpdateUser()));

        paramPropYNQ.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING, proposalYNQBean.getAcType()));
        StringBuffer sqlNarrative = new StringBuffer(
                "call DW_UPDATE_PRO_YNQ(");
        sqlNarrative.append(" <<PROPOSAL_NUMBER>> , ");
        sqlNarrative.append(" <<QUESTION_ID>> , ");
        sqlNarrative.append(" <<ANSWER>> , ");
        sqlNarrative.append(" <<EXPLANATION>> , ");
        sqlNarrative.append(" <<REVIEW_DATE>> , ");
        sqlNarrative.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlNarrative.append(" <<UPDATE_USER>> , ");
        sqlNarrative.append(" <<EXISTING_PROPOSAL_NUMBER>> , ");
        sqlNarrative.append(" <<EXISTING_QUESTION_ID>> , ");
        sqlNarrative.append(" <<EXISTING_UPDATE_TIMESTAMP>> , ");
        sqlNarrative.append(" <<EXISTING_UPDATE_USER>> , ");
        sqlNarrative.append(" <<AC_TYPE>> )");

        ProcReqParameter procPropYNQ  = new ProcReqParameter();
        procPropYNQ.setDSN(DSN);
        procPropYNQ.setParameterInfo(paramPropYNQ);
        procPropYNQ.setSqlCommand(sqlNarrative.toString());

        /*procedures.add(procPropYNQ);
        //System.out.println("query :"+procPropYNQ.getSqlCommand());
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            //System.out.println("query execution error");
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;*/
        return procPropYNQ;
    }

    /** This method is used to copy all the details of the given Proposal except Blob data.
     * <li>To copy the data, it uses the function FN_COPY_PROPOSAL.
     *
     * @return int copyFlag indicating whether the copy was successfull.
     * @param sourceProposalNumber is given as the input parameter to the function.
     * @param targetProposalNumber is given as the input parameter to the function.
     * @param budgetFlag is given as the input parameter to the function.
     * @param narrativeFlag is given as the input parameter to the function.
     * @param questionnaireFlag is given as the input parameter to the function.
     * @param unitNumber is given as the input parameter to the function.
     * @param userId is given as the input parameter to the function.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public int copyProposal(String sourceProposalNumber , String targetProposalNumber, char budgetFlag,
            // COEUSQA-2321: Copy Questionnaires for Proposal Development records - Start
//            char narrativeFlag, String unitNumber, String userId)
            char narrativeFlag, char questionnaireFlag, String unitNumber, String userId)
            throws CoeusException, DBException {
        int copyFlag = 0;
        Vector param= new Vector();
//        Vector result = new Vector();
        Vector procedures = new Vector(5,3);

        param.add(new Parameter("SOURCE_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,sourceProposalNumber));
        param.add(new Parameter("TARGET_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,targetProposalNumber));
        param.add(new Parameter("BUDGET_FLAG",
                DBEngineConstants.TYPE_STRING,new Character(budgetFlag).toString()));
        param.add(new Parameter("UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING,unitNumber));
        param.add(new Parameter("USER_ID",
                DBEngineConstants.TYPE_STRING,userId));


        StringBuffer sqlNarrative = new StringBuffer(
                "{ <<OUT INTEGER COPYFLAG>> = call FN_COPY_PROPOSAL(");
        sqlNarrative.append(" <<SOURCE_PROPOSAL_NUMBER>> , ");
        sqlNarrative.append(" <<TARGET_PROPOSAL_NUMBER>> , ");
        sqlNarrative.append(" <<BUDGET_FLAG>> , ");
        sqlNarrative.append(" <<UNIT_NUMBER>> , ");
        sqlNarrative.append(" <<USER_ID>> ) }");

        ProcReqParameter procPropCopy  = new ProcReqParameter();
        procPropCopy.setDSN(DSN);
        procPropCopy.setParameterInfo(param);
        procPropCopy.setSqlCommand(sqlNarrative.toString());

        procedures.add(procPropCopy);
        procedures.add(copyProposalBlobs(sourceProposalNumber, targetProposalNumber, narrativeFlag, unitNumber, userId));
        // COEUSQA-2321: Copy Questionnaires for Proposal Development records
        if(questionnaireFlag == 'Y'){
            procedures.add(copyProposalQuestionnaire(sourceProposalNumber, targetProposalNumber, unitNumber, userId));
        }
        java.sql.Connection conn = null;

        if(dbEngine!=null){
            conn = dbEngine.beginTxn();
            try{
                dbEngine.executeStoreProcs(procedures, conn);

                //Now Update Proposal Person Blob data
                copyProposalPersonBiographyBlobs(sourceProposalNumber, targetProposalNumber, conn);
                //Now Update Narrative Blob Data
                if(narrativeFlag=='Y'){
                    copyProposalNarrativeBlobs(sourceProposalNumber, targetProposalNumber, conn);
                }
                //End Txn
//                dbEngine.endTxn(conn);

            }catch(Throwable ex){//close connection in finally block by geo on 03-06-2007
                dbEngine.rollback(conn);
                UtilFactory.log(ex.getMessage(),ex,"ProposalDevelopmentUpdateTxnBean", "copyProposal");
                throw new DBException(ex.getMessage());
            }finally{
                dbEngine.endTxn(conn);
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        /*Commented following code as this will involve copying LONG RAW Objects and is withheld untill next Phase.
        //If narrative copy blob info also
        if(narrativeFlag=='Y')
        {
            procedures.add(copyProposalNarrativeBlobs(sourceProposalNumber, targetProposalNumber));
        }
        procedures.add(copyProposalPersonBiographyBlobs(sourceProposalNumber, targetProposalNumber));
         */

        //System.out.println("After executeSP");

        copyFlag = 1;
        return copyFlag;
    }

    /** This method is used to copy Blob data of the given Proposal Number.
     * <li>To copy the data, it uses the function FN_COPY_PROPOSAL_BLOBS.
     *
     * @return ProcReqParameter containing DSN, Stored Procedure name and Parameter info.
     * @param sourceProposalNumber is given as the input parameter to the function.
     * @param targetProposalNumber is given as the input parameter to the function.
     * @param narrativeFlag is given as the input parameter to the function.
     * @param unitNumber is given as the input parameter to the function.
     * @param userId is given as the input parameter to the function.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter copyProposalBlobs(String sourceProposalNumber , String targetProposalNumber, char narrativeFlag, String unitNumber, String userId)
    throws CoeusException, DBException {
//        int copyFlag = 0;
        Vector param= new Vector();
//        Vector result = new Vector();
        param.add(new Parameter("SOURCE_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,sourceProposalNumber));
        param.add(new Parameter("TARGET_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,targetProposalNumber));
        param.add(new Parameter("NARRATIVE_FLAG",
                DBEngineConstants.TYPE_STRING,new Character(narrativeFlag).toString()));
        param.add(new Parameter("UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING,unitNumber));
        param.add(new Parameter("USER_ID",
                DBEngineConstants.TYPE_STRING,userId));

        StringBuffer sqlNarrative = new StringBuffer(
                "{ <<OUT INTEGER COPYFLAG>> = call FN_COPY_PROPOSAL_BLOBS(");
        sqlNarrative.append(" <<SOURCE_PROPOSAL_NUMBER>> , ");
        sqlNarrative.append(" <<TARGET_PROPOSAL_NUMBER>> , ");
        sqlNarrative.append(" <<NARRATIVE_FLAG>> , ");
        sqlNarrative.append(" <<UNIT_NUMBER>> , ");
        sqlNarrative.append(" <<USER_ID>> ) }");

        ProcReqParameter procPropCopy  = new ProcReqParameter();
        procPropCopy.setDSN(DSN);
        procPropCopy.setParameterInfo(param);
        procPropCopy.setSqlCommand(sqlNarrative.toString());

        return procPropCopy;
    }

    /** This method is used to copy Blob data of Proposal Narrative.
     * <li>To copy the data, it uses the procedure UPD_NARRATIVE_SOURCE_PDF.
     *
     * @return ProcReqParameter containing DSN, Stored Procedure name and Parameter info.
     * @param sourceProposalNumber is given as the input parameter to the function.
     * @param targetProposalNumber is given as the input parameter to the function.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean copyProposalNarrativeBlobs(String sourceProposalNumber , String targetProposalNumber, java.sql.Connection conn)
    throws CoeusException, DBException {
        boolean success = false;

        ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean(userId);
        Vector vctPDFProcedures = new Vector(3,2);
        Vector vctSourceProcedures = new Vector(3,2);

        //Update PDF data
        //Code modified and added for Case#3374 - updating Narrative timestamp to parent - starts
        Vector proposalNarrativePDF = proposalNarrativeTxnBean.getPropNarrativePDFForProposal(targetProposalNumber, conn);
        Vector proposalNarrativePDFChild = proposalNarrativeTxnBean.getPropNarrativePDFForProposal(sourceProposalNumber, conn);
        if(proposalNarrativePDF!=null && proposalNarrativePDF.size() > 0
            && proposalNarrativePDFChild != null && proposalNarrativePDFChild.size() > 0){

            ProposalNarrativePDFSourceBean targetProposalNarrativePDFSourceBean = null;
//            ProposalNarrativePDFSourceBean sourceProposalNarrativePDFSourceBean = null;
            for(int row = 0; row < proposalNarrativePDF.size(); row++){
                targetProposalNarrativePDFSourceBean = (ProposalNarrativePDFSourceBean)proposalNarrativePDF.elementAt(row);
                //JIRA-CoeusQA-1391 START
                //sourceProposalNarrativePDFSourceBean = (ProposalNarrativePDFSourceBean)proposalNarrativePDFChild.elementAt(row);

                //Get Blob data from Source Proposal
                targetProposalNarrativePDFSourceBean.setProposalNumber(sourceProposalNumber);
                //int moduleNumber = targetProposalNarrativePDFSourceBean.getModuleNumber();
                //targetProposalNarrativePDFSourceBean.setModuleNumber(sourceProposalNarrativePDFSourceBean.getModuleNumber());
                targetProposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(targetProposalNarrativePDFSourceBean, conn);
                if(targetProposalNarrativePDFSourceBean!=null && targetProposalNarrativePDFSourceBean.getFileBytes() != null){
                    targetProposalNarrativePDFSourceBean.setProposalNumber(targetProposalNumber);
                    targetProposalNarrativePDFSourceBean.setAcType("I");
                    vctPDFProcedures.add(proposalNarrativeTxnBean.copyNarrativePDFBlobs(targetProposalNarrativePDFSourceBean));
                }
                //JIRA-CoeusQA-1391 END
            }
            //Code modified and added for Case#3374 - updating Narrative timestamp to parent - ends
            if(dbEngine!=null){
                if(vctPDFProcedures!=null && vctPDFProcedures.size() > 0){
                    dbEngine.batchSQLUpdate(vctPDFProcedures, conn);
                }
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
        }
        //Update Source data
        //Code modified and added for Case#3374 - updating Narrative timestamp to parent - starts
        Vector proposalNarrativeSource = proposalNarrativeTxnBean.getPropNarrativeSourceForProposal(targetProposalNumber, conn);
        Vector proposalNarrativeSourceChild = proposalNarrativeTxnBean.getPropNarrativeSourceForProposal(sourceProposalNumber, conn);
        if(proposalNarrativeSource!=null && proposalNarrativeSource.size() > 0
            && proposalNarrativeSourceChild != null && proposalNarrativeSourceChild.size() > 0){
            ProposalNarrativePDFSourceBean targetProposalNarrativePDFSourceBean = null;
            ProposalNarrativePDFSourceBean sourceProposalNarrativePDFSourceBean = null;
            for(int row = 0; row < proposalNarrativeSource.size(); row++){
                targetProposalNarrativePDFSourceBean = (ProposalNarrativePDFSourceBean)proposalNarrativeSource.elementAt(row);
                sourceProposalNarrativePDFSourceBean = (ProposalNarrativePDFSourceBean)proposalNarrativeSourceChild.elementAt(row);
                //Get Blob data from Source Proposal
                targetProposalNarrativePDFSourceBean.setProposalNumber(sourceProposalNumber);
                int moduleNumber = targetProposalNarrativePDFSourceBean.getModuleNumber();
                targetProposalNarrativePDFSourceBean.setModuleNumber(sourceProposalNarrativePDFSourceBean.getModuleNumber());
                targetProposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativeSource(targetProposalNarrativePDFSourceBean, conn);
                if(targetProposalNarrativePDFSourceBean != null && targetProposalNarrativePDFSourceBean.getFileBytes()!=null){
                    targetProposalNarrativePDFSourceBean.setProposalNumber(targetProposalNumber);
                    targetProposalNarrativePDFSourceBean.setModuleNumber(moduleNumber);
                    targetProposalNarrativePDFSourceBean.setAcType("I");
                    vctSourceProcedures.add(proposalNarrativeTxnBean.copyNarrativeSourceBlobs(targetProposalNarrativePDFSourceBean));
                }
            }
            //Code modified and added for Case#3374 - updating Narrative timestamp to parent - ends
            if(dbEngine!=null){
                if(vctSourceProcedures!=null && vctSourceProcedures.size() > 0){
                    dbEngine.batchSQLUpdate(vctSourceProcedures, conn);
                }
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
        }
        success = true;
        return success;
    }

    /** This method is used to copy Blob data of Proposal Person Biography.
     * <li>To copy the data, it uses the procedure UPD_NARRATIVE_SOURCE_PDF.
     *
     * @return ProcReqParameter containing DSN, Stored Procedure name and Parameter info.
     * @param sourceProposalNumber is given as the input parameter to the function.
     * @param targetProposalNumber is given as the input parameter to the function.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean copyProposalPersonBiographyBlobs(String sourceProposalNumber , String targetProposalNumber, java.sql.Connection conn)
    throws CoeusException, DBException {
        boolean success = false;

        ProposalPersonTxnBean proposalPersonTxnBean = new ProposalPersonTxnBean(userId);
        Vector vctPDFProcedures = new Vector(3,2);
        Vector vctSourceProcedures = new Vector(3,2);

        //Update PDF data
        //Code modified and added for Case#3374 - updating Narrative timestamp to parent - starts
        Vector proposalPersonBioPDF = proposalPersonTxnBean.getProposalPersonBioPDF(targetProposalNumber, conn);
        Vector proposalPersonBioPDFChild = proposalPersonTxnBean.getProposalPersonBioPDF(sourceProposalNumber, conn);
        if(proposalPersonBioPDF!=null && proposalPersonBioPDF.size() > 0
            && proposalPersonBioPDFChild != null && proposalPersonBioPDFChild.size()>0){

            ProposalPersonBioPDFBean targetProposalPersonBioPDFBean = null;
            ProposalPersonBioPDFBean sourceProposalPersonBioPDFBean = null;
            for(int row = 0; row < proposalPersonBioPDF.size(); row++){
                targetProposalPersonBioPDFBean = (ProposalPersonBioPDFBean)proposalPersonBioPDF.elementAt(row);
                sourceProposalPersonBioPDFBean = (ProposalPersonBioPDFBean)proposalPersonBioPDFChild.elementAt(row);
                //Get Blob data from Source Proposal
                targetProposalPersonBioPDFBean.setProposalNumber(sourceProposalNumber);
                int bioNumber = targetProposalPersonBioPDFBean.getBioNumber();
                targetProposalPersonBioPDFBean.setBioNumber(sourceProposalPersonBioPDFBean.getBioNumber());
                targetProposalPersonBioPDFBean = proposalPersonTxnBean.getProposalPersonBioPDF(targetProposalPersonBioPDFBean, conn);
                if(targetProposalPersonBioPDFBean!=null && targetProposalPersonBioPDFBean.getFileBytes() != null){
                    targetProposalPersonBioPDFBean.setProposalNumber(targetProposalNumber);
                    targetProposalPersonBioPDFBean.setBioNumber(bioNumber);
                    targetProposalPersonBioPDFBean.setAcType("I");
                    vctPDFProcedures.add(updateProposalPersonPDFBlobs(targetProposalPersonBioPDFBean));
                }
            }
            //Code modified and added for Case#3374 - updating Narrative timestamp to parent - ends
            if(dbEngine!=null){
                if(vctPDFProcedures!=null && vctPDFProcedures.size() > 0){
                    dbEngine.batchSQLUpdate(vctPDFProcedures, conn);
                }
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
        }
        //Update Source data
        //Code modified and added for Case#3374 - updating Narrative timestamp to parent - starts
        Vector proposalPersonBioSource = proposalPersonTxnBean.getProposalPersonBioSource(targetProposalNumber, conn);
        Vector proposalPersonBioSourceChild = proposalPersonTxnBean.getProposalPersonBioSource(sourceProposalNumber, conn);
        if(proposalPersonBioSource!=null && proposalPersonBioSource.size() > 0
            && proposalPersonBioSourceChild != null && proposalPersonBioSourceChild.size() >0){

            ProposalPersonBioSourceBean targetProposalPersonBioSourceBean = null;
            ProposalPersonBioSourceBean sourceProposalPersonBioSourceBean = null;
            for(int row = 0; row < proposalPersonBioSource.size(); row++){
                targetProposalPersonBioSourceBean = (ProposalPersonBioSourceBean)proposalPersonBioSource.elementAt(row);
                sourceProposalPersonBioSourceBean = (ProposalPersonBioSourceBean)proposalPersonBioSourceChild.elementAt(row);
                //Get Blob data from Source Proposal
                targetProposalPersonBioSourceBean.setProposalNumber(sourceProposalNumber);
                int bioNumber = targetProposalPersonBioSourceBean.getBioNumber();
                targetProposalPersonBioSourceBean.setBioNumber(sourceProposalPersonBioSourceBean.getBioNumber());
                targetProposalPersonBioSourceBean = proposalPersonTxnBean.getProposalPersonBioSource(targetProposalPersonBioSourceBean, conn);
                if(targetProposalPersonBioSourceBean != null && targetProposalPersonBioSourceBean.getFileBytes()!=null){
                    targetProposalPersonBioSourceBean.setProposalNumber(targetProposalNumber);
                    targetProposalPersonBioSourceBean.setBioNumber(bioNumber);
                    targetProposalPersonBioSourceBean.setAcType("I");
                    vctSourceProcedures.add(updateProposalPersonSourceBlobs(targetProposalPersonBioSourceBean));
                }
            }
            //Code modified and added for Case#3374 - updating Narrative timestamp to parent - ends
            if(dbEngine!=null){
                if(vctSourceProcedures!=null && vctSourceProcedures.size() > 0){
                    dbEngine.batchSQLUpdate(vctSourceProcedures, conn);
                }
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
        }
        success = true;
        return success;
    }

    /** This method is used to copy only Blob data of all Proposal Person Biography PDF.
     *
     * @return boolean indicating whether the Updation was success
     * @param proposalPersonBioPDFBean ProposalPersonBioPDFBean
     *
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter updateProposalPersonPDFBlobs(ProposalPersonBioPDFBean proposalPersonBioPDFBean)
    throws CoeusException, DBException {
//        Vector result = new Vector(3, 2);
        Vector parameter = new Vector();
//        boolean isUpdated = false;
//        Vector procedures = new Vector();

        CoeusFunctions coeusFunctions = new CoeusFunctions();
//        Timestamp dbTimeStamp = coeusFunctions.getDBTimestamp();

        parameter.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalPersonBioPDFBean.getProposalNumber()));
        parameter.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING, proposalPersonBioPDFBean.getPersonId()));
        parameter.addElement(new Parameter("BIO_NUMBER",
            DBEngineConstants.TYPE_INT, ""+proposalPersonBioPDFBean.getBioNumber()));
        //Modified for Case#3183 - Proposal hierarchy - starts
//        parameter.addElement(new Parameter("UPDATE_TIMESTAMP",
//            DBEngineConstants.TYPE_TIMESTAMP, dbTimeStamp));
        parameter.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP, proposalPersonBioPDFBean.getUpdateTimestamp()));
        //Modified for Case#3183 - Proposal hierarchy - ends
        parameter.addElement(new Parameter("BIO_PDF",
            DBEngineConstants.TYPE_BLOB, proposalPersonBioPDFBean.getFileBytes()));

        //Modified for case# 3183 - Proposal hierarchy- start
        //Updating the file name of the parent with that of child proposal
        parameter.addElement(new Parameter("FILE_NAME",
            DBEngineConstants.TYPE_STRING, proposalPersonBioPDFBean.getFileName()));
        String statement = "UPDATE OSP$EPS_PROP_PERSON_BIO_PDF SET BIO_PDF = <<BIO_PDF>>, FILE_NAME = <<FILE_NAME>>, UPDATE_TIMESTAMP = <<UPDATE_TIMESTAMP>> "+
                " WHERE PROPOSAL_NUMBER = <<PROPOSAL_NUMBER>> AND PERSON_ID = <<PERSON_ID>> AND BIO_NUMBER = <<BIO_NUMBER>>";
         //Modified for case# 3183 - Proposal hierarchy- end

//        String selectQry = "UPDATE OSP$EPS_PROP_PERSON_BIO_PDF SET BIO_PDF = ? " +
//        " WHERE UPDATE_TIMESTAMP =  ?";

        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(parameter);
        procReqParameter.setSqlCommand(statement);

//        if(dbEngine!=null) {
//            boolean status = dbEngine.insertPdfBlob("Coeus", statement,
//            parameter,selectQry,proposalPersonBioPDFBean.getFileBytes(),dbTimeStamp);
//
//            isUpdated = status ;
//        }else{
//            throw new CoeusException("db_exceptionCode.1000");
//        }
        return procReqParameter;
    }

    /** This method is used to copy only Blob data of all Proposal Person Biography Source.
     *
     * @return boolean indicating whether the Updation was success
     * @param proposalPersonBioSourceBean ProposalPersonBioSourceBean
     *
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter updateProposalPersonSourceBlobs(ProposalPersonBioSourceBean proposalPersonBioSourceBean)
    throws CoeusException, DBException {
//        Vector result = new Vector(3, 2);
        Vector parameter = new Vector();
//        boolean isUpdated = false;
//        Vector procedures = new Vector();

        CoeusFunctions coeusFunctions = new CoeusFunctions();
//        Timestamp dbTimeStamp = coeusFunctions.getDBTimestamp();

        parameter.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalPersonBioSourceBean.getProposalNumber()));
        parameter.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING, proposalPersonBioSourceBean.getPersonId()));
        parameter.addElement(new Parameter("BIO_NUMBER",
            DBEngineConstants.TYPE_INT, ""+proposalPersonBioSourceBean.getBioNumber()));
        //Modified for Case#3183 - Proposal hierarchy - starts
//        parameter.addElement(new Parameter("UPDATE_TIMESTAMP",
//            DBEngineConstants.TYPE_TIMESTAMP, dbTimeStamp));
        parameter.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP, proposalPersonBioSourceBean.getUpdateTimestamp()));
        //Modified for Case#3183 - Proposal hierarchy - ends
        parameter.addElement(new Parameter("BIO_SOURCE",
            DBEngineConstants.TYPE_BLOB, proposalPersonBioSourceBean.getFileBytes()));

       //Modified for case# 3183 - Proposal hierarchy- start
        //Updating the file name of the parent with that of child proposal
        parameter.addElement(new Parameter("FILE_NAME",
            DBEngineConstants.TYPE_STRING, proposalPersonBioSourceBean.getFileName()));
        String statement = "UPDATE OSP$EPS_PROP_PERSON_BIO_SOURCE SET BIO_SOURCE = <<BIO_SOURCE>>, FILE_NAME = <<FILE_NAME>>, UPDATE_TIMESTAMP = <<UPDATE_TIMESTAMP>> "+
                " WHERE PROPOSAL_NUMBER = <<PROPOSAL_NUMBER>> AND PERSON_ID = <<PERSON_ID>> AND BIO_NUMBER = <<BIO_NUMBER>> ";
          //Modified for case# 3183 - Proposal hierarchy- end

//        String selectQry = "UPDATE OSP$EPS_PROP_PERSON_BIO_SOURCE SET BIO_SOURCE = ? " +
//        " WHERE UPDATE_TIMESTAMP =  ?";

        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(parameter);
        procReqParameter.setSqlCommand(statement);

//        if(dbEngine!=null) {
//            boolean status = dbEngine.insertPdfBlob("Coeus", statement,
//            parameter,selectQry,proposalPersonBioSourceBean.getFileBytes(),dbTimeStamp);
//
//            isUpdated = status ;
//        }else{
//            throw new CoeusException("db_exceptionCode.1000");
//        }
        return procReqParameter;
    }

    /**
     *  This Method is used to Insert/Update/Delete Proposal Special Review.
     *  <li>To update the data, it uses DW_UPDATE_PRO_SPREV procedure.
     *
     *  @param proposalSpecialReviewFormBean contains data for Insert/Update/Delete Proposal Yes/No Question Answers.
     *  @return ProcReqParameter containing DSN, SQL parameters and Store Procedure Info
     *  @exception DBException if the instance of a dbEngine is null.
     *  @exception CoeusException if the DB instance is not available.
     */






    public ProcReqParameter addUpdDelPropSpecialReview( ProposalSpecialReviewFormBean
            proposalSpecialReviewFormBean)  throws CoeusException ,DBException{
//        boolean success = false;
//        Vector procedures = new Vector(5,3);
        Vector paramPropSR= new Vector();
        //Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();

        //Commented on 4th July, 2004 - Prasanna
        //int intSpecialRevNumber = 0;
//        ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
        /*
        if(proposalSpecialReviewFormBean.getAcType() != null &&
            proposalSpecialReviewFormBean.getAcType().equalsIgnoreCase("I"))
        {
            intSpecialRevNumber = proposalDevelopmentTxnBean.getNextPropSpecialReviewNumber(proposalSpecialReviewFormBean.getProposalNumber());
            intSpecialRevNumber = intSpecialRevNumber + 1;
        }
        else
        {
            intSpecialRevNumber = proposalSpecialReviewFormBean.getSpecialReviewNumber();
        }*/
        //intSpecialRevNumber = proposalSpecialReviewFormBean.getSpecialReviewNumber();

        paramPropSR.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalSpecialReviewFormBean.getProposalNumber()));
        paramPropSR.addElement(new Parameter("SPECIAL_REVIEW_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+proposalSpecialReviewFormBean.getSpecialReviewNumber()));
        paramPropSR.addElement(new Parameter("SPECIAL_REVIEW_CODE",
                DBEngineConstants.TYPE_INT,
                ""+proposalSpecialReviewFormBean.getSpecialReviewCode()));
        paramPropSR.addElement(new Parameter("APPROVAL_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+proposalSpecialReviewFormBean.getApprovalCode()));
        paramPropSR.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalSpecialReviewFormBean.getProtocolSPRevNumber()));
        paramPropSR.addElement(new Parameter("APPLICATION_DATE",
                DBEngineConstants.TYPE_DATE,
                proposalSpecialReviewFormBean.getApplicationDate()));
        paramPropSR.addElement(new Parameter("APPROVAL_DATE",
                DBEngineConstants.TYPE_DATE,
                proposalSpecialReviewFormBean.getApprovalDate()));
        paramPropSR.addElement(new Parameter("COMMENTS",
                DBEngineConstants.TYPE_STRING,
                proposalSpecialReviewFormBean.getComments()));
        paramPropSR.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramPropSR.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramPropSR.addElement(new Parameter("EXISTING_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalSpecialReviewFormBean.getProposalNumber()));
        paramPropSR.addElement(new Parameter("EXISTING_SPECIAL_REVIEW_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+proposalSpecialReviewFormBean.getSpecialReviewNumber()));
        paramPropSR.addElement(new Parameter("EXISTING_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP, proposalSpecialReviewFormBean.getUpdateTimestamp()));
        paramPropSR.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING, proposalSpecialReviewFormBean.getAcType()));

        StringBuffer sqlNarrative = new StringBuffer(
                "call DW_UPDATE_PRO_SPREV(");
        sqlNarrative.append(" <<PROPOSAL_NUMBER>> , ");
        sqlNarrative.append(" <<SPECIAL_REVIEW_NUMBER>> , ");
        sqlNarrative.append(" <<SPECIAL_REVIEW_CODE>> , ");
        sqlNarrative.append(" <<APPROVAL_TYPE_CODE>> , ");
        sqlNarrative.append(" <<PROTOCOL_NUMBER>> , ");
        sqlNarrative.append(" <<APPLICATION_DATE>> , ");
        sqlNarrative.append(" <<APPROVAL_DATE>> , ");
        sqlNarrative.append(" <<COMMENTS>> , ");
        sqlNarrative.append(" <<UPDATE_USER>> , ");
        sqlNarrative.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlNarrative.append(" <<EXISTING_PROPOSAL_NUMBER>> , ");
        sqlNarrative.append(" <<EXISTING_SPECIAL_REVIEW_NUMBER>> , ");
        sqlNarrative.append(" <<EXISTING_UPDATE_TIMESTAMP>> , ");
        sqlNarrative.append(" <<AC_TYPE>> )");

        ProcReqParameter procPropSR  = new ProcReqParameter();
        procPropSR.setDSN(DSN);
        procPropSR.setParameterInfo(paramPropSR);
        procPropSR.setSqlCommand(sqlNarrative.toString());

        /*procedures.add(procPropSR);

        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;*/
        return procPropSR;
    }

    /**
     *  This Method is used to Insert/Update/Delete Proposal Narrative details data.
     *
     *  @param proposalNarratives contains vector of Proposal Narrative details.
     *  @return boolean this holds true for successfull insert/update/delete or
     *  false if fails.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean addUpdDeleteProposalNarrativeDetails(Vector proposalNarratives)
    throws CoeusException,DBException{
        boolean success = false;
        Vector procedures = new Vector(5,3);
        String proposalNumber = "";
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();

        if ((proposalNarratives != null) && (proposalNarratives.size() >0)){
            int length = proposalNarratives.size();
            for(int index=0;index<length;index++){
                ProposalNarrativeFormBean proposalNarrativeFormBean =
                        (ProposalNarrativeFormBean)proposalNarratives.elementAt(index);
                proposalNumber = proposalNarrativeFormBean.getProposalNumber();
                Vector paramProposalOther= new Vector();
                paramProposalOther.addElement(new Parameter("PROPOSAL_NUMBER",
                        DBEngineConstants.TYPE_STRING,
                        proposalNarrativeFormBean.getProposalNumber()));
                paramProposalOther.addElement(new Parameter("MODULE_NUMBER",
                        DBEngineConstants.TYPE_INT,
                        ""+proposalNarrativeFormBean.getModuleNumber()));
                paramProposalOther.addElement(new Parameter("MODULE_SEQUENCE_NUMBER",
                        DBEngineConstants.TYPE_INT,
                        ""+proposalNarrativeFormBean.getModuleSequenceNumber()));
                paramProposalOther.addElement(new Parameter("MODULE_TITLE",
                        DBEngineConstants.TYPE_STRING,
                        proposalNarrativeFormBean.getModuleTitle()));
                paramProposalOther.addElement(new Parameter("MODULE_STATUS_CODE",
                        DBEngineConstants.TYPE_STRING,
                        new Character(proposalNarrativeFormBean.getModuleStatusCode()).toString()));
                paramProposalOther.addElement(new Parameter("CONTACT_NAME",
                        DBEngineConstants.TYPE_STRING,
                        proposalNarrativeFormBean.getContactName()));
                paramProposalOther.addElement(new Parameter("PHONE_NUMBER",
                        DBEngineConstants.TYPE_STRING,
                        proposalNarrativeFormBean.getPhoneNumber()));
                paramProposalOther.addElement(new Parameter("EMAIL_ADDRESS",
                        DBEngineConstants.TYPE_STRING,
                        proposalNarrativeFormBean.getEmailAddress()));
                paramProposalOther.addElement(new Parameter("COMMENTS",
                        DBEngineConstants.TYPE_STRING,
                        proposalNarrativeFormBean.getComments()));
                //added for Coeus enhancements case:1624  start 1
                paramProposalOther.addElement(new Parameter("NARRATIVE_TYPE_CODE",
                        DBEngineConstants.TYPE_INT,
                        ""+proposalNarrativeFormBean.getNarrativeTypeCode()));
                //added for Coeus enhancements case:1624  end 1
                paramProposalOther.addElement(new Parameter("UPDATE_USER",
                        DBEngineConstants.TYPE_STRING,userId));
                paramProposalOther.addElement(new Parameter("UPDATE_TIMESTAMP",
                        DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
                paramProposalOther.addElement(new Parameter("EXISTING_UPDATE_USER",
                        DBEngineConstants.TYPE_STRING,
                        proposalNarrativeFormBean.getUpdateUser()));
                paramProposalOther.addElement(new Parameter("EXISTING_UPDATE_TIMESTAMP",
                        DBEngineConstants.TYPE_TIMESTAMP,
                        proposalNarrativeFormBean.getUpdateTimestamp()));
                paramProposalOther.addElement(new Parameter("CHOICE",
                        DBEngineConstants.TYPE_STRING,
                        proposalNarrativeFormBean.getAcType()));

                StringBuffer sqlProposalOther = new StringBuffer(
                        "call UPD_PROPOSALNARR(");
                sqlProposalOther.append(" <<PROPOSAL_NUMBER>> , ");
                sqlProposalOther.append(" <<MODULE_NUMBER>> , ");
                sqlProposalOther.append(" <<MODULE_SEQUENCE_NUMBER>> , ");
                sqlProposalOther.append(" <<MODULE_TITLE>> , ");
                sqlProposalOther.append(" <<MODULE_STATUS_CODE>> , ");
                sqlProposalOther.append(" <<CONTACT_NAME>> , ");
                sqlProposalOther.append(" <<PHONE_NUMBER>> , ");
                sqlProposalOther.append(" <<EMAIL_ADDRESS>> , ");
                sqlProposalOther.append(" <<COMMENTS>> , ");
                //added for Coeus enhancements case:1624  start 2
                sqlProposalOther.append(" <<NARRATIVE_TYPE_CODE>> , ");
                //added for Coeus enhancements case:1624  end 2
                sqlProposalOther.append(" <<UPDATE_USER>> , ");
                sqlProposalOther.append(" <<UPDATE_TIMESTAMP>> , ");
                sqlProposalOther.append(" <<EXISTING_UPDATE_USER>> , ");
                sqlProposalOther.append(" <<EXISTING_UPDATE_TIMESTAMP>> , ");
                sqlProposalOther.append(" <<CHOICE>> )");

                ProcReqParameter procProposalOther  = new ProcReqParameter();
                procProposalOther.setDSN(DSN);
                procProposalOther.setParameterInfo(paramProposalOther);
                procProposalOther.setSqlCommand(sqlProposalOther.toString());

                procedures.add(procProposalOther);
                Vector proposalUserRights = proposalNarrativeFormBean.getPropModuleUsers();

                if ((proposalUserRights != null) && (proposalUserRights.size() >0)){
                    int userRightsLength = proposalUserRights.size();
                    for(int userIndex=0;userIndex<userRightsLength;userIndex++){
                        ProposalNarrativeModuleUsersFormBean proposalNarrativeModuleUsersFormBean = (ProposalNarrativeModuleUsersFormBean) proposalUserRights.elementAt(userIndex);
                        proposalNarrativeModuleUsersFormBean.setModuleNumber(proposalNarrativeFormBean.getModuleNumber());
                        procedures.add(addUpdDelProposalNarrativeUsers(proposalNarrativeModuleUsersFormBean));

                    }
                }
                // Added for Proposal Hierarchy Enhancement Case# 3183 - Start
                if(proposalNarrativeFormBean.isParentProposal()&& proposalNarrativeFormBean.getAcType() != null
                    && (proposalNarrativeFormBean.getAcType().equals(TypeConstants.INSERT_RECORD)
                            || proposalNarrativeFormBean.getAcType().equals(TypeConstants.DELETE_RECORD))){
                        procedures.add(updatePropHierLinkData(proposalNarrativeFormBean));
                } // Added for Proposal Hierarchy Enhancement Case# 3183 - End
            }
        }

        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
    }

    /**
     *  Method used to add/update/Delete proposal narrative user details of a Proposal Narrative screen.
     *  <li>To update the data, it uses DW_UPDATE_NARR_USER_RIGHTS procedure.
     *
     *  @param proposalNarrativeModuleUsersFormBean this bean contains narrative user details data.
     *  @return ProcReqParameter
     *  @exception DBException if the instance of a dbEngine is null.
     *  @exception CoeusException if the DB instance is not available.
     */
    public ProcReqParameter addUpdDelProposalNarrativeUsers( ProposalNarrativeModuleUsersFormBean
            proposalNarrativeModuleUsersFormBean)  throws CoeusException ,DBException{
//        boolean success = false;
//        Vector procedures = new Vector(5,3);
        Vector paramNarrative= new Vector();
        //Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        //proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(userId);

        paramNarrative.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalNarrativeModuleUsersFormBean.getProposalNumber()));
        paramNarrative.addElement(new Parameter("USER_ID",
                DBEngineConstants.TYPE_STRING,
                proposalNarrativeModuleUsersFormBean.getUserId()));
        paramNarrative.addElement(new Parameter("MODULE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+proposalNarrativeModuleUsersFormBean.getModuleNumber()) );
        paramNarrative.addElement(new Parameter("ACCESS_TYPE",
                DBEngineConstants.TYPE_STRING,
                new Character(proposalNarrativeModuleUsersFormBean.getAccessType()).toString()));
        paramNarrative.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramNarrative.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramNarrative.addElement(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING,
                proposalNarrativeModuleUsersFormBean.getUpdateUser()));
        paramNarrative.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                proposalNarrativeModuleUsersFormBean.getUpdateTimestamp()));
        paramNarrative.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                proposalNarrativeModuleUsersFormBean.getAcType()));

        StringBuffer sqlNarrative = new StringBuffer(
                "call DW_UPDATE_NARR_USER_RIGHTS(");
        sqlNarrative.append(" <<PROPOSAL_NUMBER>> , ");
        sqlNarrative.append(" <<MODULE_NUMBER>> , ");
        sqlNarrative.append(" <<USER_ID>> , ");
        sqlNarrative.append(" <<ACCESS_TYPE>> , ");
        sqlNarrative.append(" <<UPDATE_USER>> , ");
        sqlNarrative.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlNarrative.append(" <<AW_UPDATE_USER>> , ");
        sqlNarrative.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlNarrative.append(" <<AC_TYPE>> )");

        ProcReqParameter procNarrative  = new ProcReqParameter();
        procNarrative.setDSN(DSN);
        procNarrative.setParameterInfo(paramNarrative);
        procNarrative.setSqlCommand(sqlNarrative.toString());

        //procedures.add(procNarrative);

        /*if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;*/
        return procNarrative;
    }

    /**
     * This method is used to convert date from string value
     * @param dateStr string value
     * @return Date java.sql.Date
     * @exception ParseException raised while date prase.
     */
    public java.sql.Date convertToDate(String dateStr) throws java.text.ParseException {
        if(dateStr==null || dateStr.trim().equals(""))
            return null;
        java.util.Date dt = null;
        if (dateStr != null && dateStr.trim().length() > 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            dt =dateFormat.parse(dateStr);
        }
        return new java.sql.Date(dt.getTime());
    }

    /**
     *  The method used to release the lock of a particular schedule
     *  @param rowId the id for lock to be released
     */
    public void releaseEdit(String rowId){
        transMon.releaseEdit(this.rowLockStr+rowId);
    }

    // Code added by Shivakumar for locking enhancement - BEGIN1
    public void releaseEdit(String rowId,String loggedinUser)
    throws CoeusException,DBException{
        transMon.releaseEdit(this.rowLockStr+rowId, loggedinUser);
    }
    // Code added by Shivakumar for locking enhancement - END1
    // Calling releaseLock method for fixing bug
    public LockingBean releaseLock(String rowId,String loggedinUser)
    throws CoeusException,DBException{
        LockingBean lockingBean = new LockingBean();
        boolean lockCheck = transMon.lockAvailabilityCheck(this.rowLockStr+rowId, loggedinUser);
        if(!lockCheck){
            lockingBean = transMon.releaseLock(this.rowLockStr+rowId, loggedinUser);
        }
        return lockingBean;
    }
//Relesing lock in routing
      public LockingBean releaseRoutingLock(String rowId,String loggedinUser)
    throws CoeusException,DBException{
        LockingBean lockingBean = new LockingBean();
        boolean lockCheck = transMon.lockAvailabilityCheck(this.routingLockStr+rowId, loggedinUser);
        if(!lockCheck){
            lockingBean = transMon.releaseLock(this.routingLockStr+rowId, loggedinUser);
        }
        return lockingBean;
    }


    /**
     *  Method used to insert/update/delete all Proposal Approval Maps.
     *  To update the data, it uses DW_UPDATE_PROP_APPR_MAPS procedure.
     *
     *  @param vctPropMaps Vector of ProposalApprovalMapBean
     *  @return boolean true if updated successfully else false
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter addUpdDeleteProposalApprovalMaps(ProposalApprovalMapBean proposalApprovalMapBean)
    throws CoeusException, DBException{

        Vector param = new Vector();
//        Vector procedures = new Vector(5,3);

        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
//        param.addElement(new Parameter("PROPOSAL_NUMBER",
//            DBEngineConstants.TYPE_STRING, proposalApprovalMapBean.getProposalNumber()));
        param.addElement(new Parameter("MODULE_CODE",
                DBEngineConstants.TYPE_INT, "3"));
        param.addElement(new Parameter("MODULE_ITEM_KEY",
                DBEngineConstants.TYPE_STRING, proposalApprovalMapBean.getProposalNumber()));
        param.addElement(new Parameter("MODULE_ITEM_KEY_SEQUENCE",
                DBEngineConstants.TYPE_STRING, "1"));
        param.addElement(new Parameter("APPROVAL_SEQUENCE",
                DBEngineConstants.TYPE_STRING, "1"));
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        param.addElement(new Parameter("MAP_ID",
                DBEngineConstants.TYPE_INT, ""+proposalApprovalMapBean.getMapId()));
        param.addElement(new Parameter("PARENT_MAP_ID",
                DBEngineConstants.TYPE_INT, ""+proposalApprovalMapBean.getParentMapId()));
        param.addElement(new Parameter("DESCRIPTION",
                DBEngineConstants.TYPE_STRING, proposalApprovalMapBean.getDescription()));
        param.addElement(new Parameter("SYSTEM_FLAG",
                DBEngineConstants.TYPE_STRING, proposalApprovalMapBean.isSystemFlag() == true ? "Y" : "N"));
        param.addElement(new Parameter("APPROVAL_STATUS",
                DBEngineConstants.TYPE_STRING, proposalApprovalMapBean.getApprovalStatus()));
        param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        param.addElement(new Parameter("AW_MODULE_CODE",
                DBEngineConstants.TYPE_INT, "3"));
        param.addElement(new Parameter("AW_MODULE_ITEM_KEY",
                DBEngineConstants.TYPE_STRING, proposalApprovalMapBean.getProposalNumber()));
        param.addElement(new Parameter("AW_MODULE_ITEM_KEY_SEQUENCE",
                DBEngineConstants.TYPE_STRING, "1"));
        param.addElement(new Parameter("AW_APPROVAL_SEQUENCE",
                DBEngineConstants.TYPE_STRING, "1"));
        param.addElement(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING, proposalApprovalMapBean.getUpdateUser()));
//        param.addElement(new Parameter("AW_PROPOSAL_NUMBER",
//            DBEngineConstants.TYPE_STRING, proposalApprovalMapBean.getProposalNumber()));
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end

        param.addElement(new Parameter("AW_MAP_ID",
                DBEngineConstants.TYPE_INT, ""+proposalApprovalMapBean.getMapId()));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP, proposalApprovalMapBean.getUpdateTimeStamp()));
        param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING, proposalApprovalMapBean.getAcType()));

        StringBuffer sql = new StringBuffer(
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        //        "call DW_UPDATE_PROP_APPR_MAPS(");
        //        sql.append(" <<PROPOSAL_NUMBER>> , ");
                "call UPDATE_APPROVAL_MAPS(");
        sql.append(" <<MODULE_CODE>> , ");
        sql.append(" <<MODULE_ITEM_KEY>> , ");
        sql.append(" <<MODULE_ITEM_KEY_SEQUENCE>> , ");
        sql.append(" <<APPROVAL_SEQUENCE>> , ");
        sql.append(" <<MAP_ID>> , ");
        sql.append(" <<PARENT_MAP_ID>> , ");
        sql.append(" <<DESCRIPTION>> , ");
        sql.append(" <<SYSTEM_FLAG>> , ");
        sql.append(" <<APPROVAL_STATUS>> , ");
         sql.append(" <<UPDATE_TIMESTAMP>> , ");
        sql.append(" <<UPDATE_USER>> , ");
//        sql.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sql.append(" <<AW_MODULE_CODE>> , ");
        sql.append(" <<AW_MODULE_ITEM_KEY>> , ");
        sql.append(" <<AW_MODULE_ITEM_KEY_SEQUENCE>> , ");
        sql.append(" <<AW_APPROVAL_SEQUENCE>> , ");
        sql.append(" <<AW_MAP_ID>> , ");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AW_UPDATE_USER>> , ");
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        sql.append(" <<AC_TYPE>> )");

        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());

        //Update Approvals

        return procReqParameter;
    }

    /**
     *  Method used to insert/update/delete all Proposal Approval Maps.
     *  To update the data, it uses DW_UPDATE_PROP_APPR_MAPS procedure.
     *
     *  @param vctPropMaps Vector of ProposalApprovalMapBean
     *  @return boolean true if updated successfully else false
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter addUpdDeleteProposalApprovals(ProposalApprovalBean proposalApprovalBean)
    throws CoeusException, DBException{

        Vector param = new Vector();
//        Vector procedures = new Vector(5,3);

        param = new Vector();
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
//        param.addElement(new Parameter("PROPOSAL_NUMBER",
//                DBEngineConstants.TYPE_STRING, proposalApprovalBean.getProposalNumber()));
        param.addElement(new Parameter("MODULE_CODE",
                DBEngineConstants.TYPE_INT, "3"));
        param.addElement(new Parameter("MODULE_ITEM_KEY",
                DBEngineConstants.TYPE_STRING, proposalApprovalBean.getProposalNumber()));
        param.addElement(new Parameter("MODULE_ITEM_KEY_SEQUENCE",
                DBEngineConstants.TYPE_INT, "1"));
        param.addElement(new Parameter("APPROVAL_SEQUENCE",
                DBEngineConstants.TYPE_INT, "1"));
        param.addElement(new Parameter("MAP_ID",
                DBEngineConstants.TYPE_INT, ""+proposalApprovalBean.getMapId()));
        param.addElement(new Parameter("LEVEL_NUMBER",
                DBEngineConstants.TYPE_INT, ""+proposalApprovalBean.getLevelNumber()));
        param.addElement(new Parameter("STOP_NUMBER",
                DBEngineConstants.TYPE_INT, ""+proposalApprovalBean.getStopNumber()));
        param.addElement(new Parameter("USER_ID",
                DBEngineConstants.TYPE_STRING, proposalApprovalBean.getUserId()));
        param.addElement(new Parameter("PRIMARY_APPROVER_FLAG",
                DBEngineConstants.TYPE_STRING, proposalApprovalBean.isPrimaryApproverFlag() == true ? "Y" : "N"));
        param.addElement(new Parameter("DESCRIPTION",
                DBEngineConstants.TYPE_STRING, proposalApprovalBean.getDescription()));
        param.addElement(new Parameter("APPROVER_NUMBER",
                DBEngineConstants.TYPE_INT, String.valueOf(proposalApprovalBean.getApproverNumber())));
        param.addElement(new Parameter("APPROVAL_STATUS",
                DBEngineConstants.TYPE_STRING, proposalApprovalBean.getApprovalStatus()));
        param.addElement(new Parameter("SUBMISSION_DATE",
                DBEngineConstants.TYPE_DATE, proposalApprovalBean.getSubmissionDate()));
        param.addElement(new Parameter("APPROVAL_DATE",
                DBEngineConstants.TYPE_DATE, proposalApprovalBean.getApprovalDate()));
        //        param.addElement(new Parameter("COMMENTS",
//                DBEngineConstants.TYPE_STRING, proposalApprovalBean.getComments()));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        //        param.addElement(new Parameter("AW_PROPOSAL_NUMBER",
//                DBEngineConstants.TYPE_STRING, proposalApprovalBean.getProposalNumber()));
        param.addElement(new Parameter("AW_MODULE_CODE",
                DBEngineConstants.TYPE_INT, "3"));
        param.addElement(new Parameter("AW_MODULE_ITEM_KEY",
                DBEngineConstants.TYPE_STRING, proposalApprovalBean.getProposalNumber()));
        param.addElement(new Parameter("AW_MODULE_ITEM_KEY_SEQUENCE",
                DBEngineConstants.TYPE_INT, "1"));
        param.addElement(new Parameter("AW_APPROVAL_SEQUENCE",
                DBEngineConstants.TYPE_INT, "1"));
        param.addElement(new Parameter("AW_MAP_ID",
                DBEngineConstants.TYPE_INT, ""+proposalApprovalBean.getMapId()));
        param.addElement(new Parameter("AW_LEVEL_NUMBER",
                DBEngineConstants.TYPE_INT, ""+proposalApprovalBean.getLevelNumber()));
        param.addElement(new Parameter("AW_STOP_NUMBER",
                DBEngineConstants.TYPE_INT, ""+proposalApprovalBean.getStopNumber()));
        param.addElement(new Parameter("AW_USER_ID",
                DBEngineConstants.TYPE_STRING, ""+proposalApprovalBean.getUserId()));
        param.addElement(new Parameter("AW_APPROVER_NUMBER",
                DBEngineConstants.TYPE_INT, String.valueOf(proposalApprovalBean.getApproverNumber())));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP, proposalApprovalBean.getUpdateTimeStamp()));
        param.addElement(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING, proposalApprovalBean.getUserId()));


        param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING, proposalApprovalBean.getAcType()));

        StringBuffer sql = new StringBuffer(
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - start
        //"call DW_UPDATE_PROP_APPR(");
        //"call UPDATE_PROP_APPR(");
        "call UPDATE_APPROVAL_DETAILS(");
//        sql.append(" <<PROPOSAL_NUMBER>> , ");
        sql.append(" <<MODULE_CODE>> , ");
        sql.append(" <<MODULE_ITEM_KEY>> , ");
        sql.append(" <<MODULE_ITEM_KEY_SEQUENCE>> , ");
        sql.append(" <<APPROVAL_SEQUENCE>> , ");
        sql.append(" <<MAP_ID>> , ");
        sql.append(" <<LEVEL_NUMBER>> , ");
        sql.append(" <<STOP_NUMBER>> , ");
        sql.append(" <<USER_ID>> , ");
        sql.append(" <<PRIMARY_APPROVER_FLAG>> , ");
        sql.append(" <<DESCRIPTION>> , ");
        sql.append(" <<APPROVER_NUMBER>> , ");
        sql.append(" <<APPROVAL_STATUS>> , ");
        sql.append(" <<SUBMISSION_DATE>> , ");
        sql.append(" <<APPROVAL_DATE>> , ");
        //sql.append(" <<COMMENTS>> , ");

        sql.append(" <<UPDATE_TIMESTAMP>> , ");
        sql.append(" <<UPDATE_USER>> , ");
        //sql.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sql.append(" <<AW_MODULE_CODE>> , ");
        sql.append(" <<AW_MODULE_ITEM_KEY>> , ");
        sql.append(" <<AW_MODULE_ITEM_KEY_SEQUENCE>> , ");
        sql.append(" <<AW_APPROVAL_SEQUENCE>> , ");
        sql.append(" <<AW_MAP_ID>> , ");
        sql.append(" <<AW_LEVEL_NUMBER>> , ");
        sql.append(" <<AW_STOP_NUMBER>> , ");
        sql.append(" <<AW_USER_ID>> , ");
        sql.append(" <<AW_APPROVER_NUMBER>> , ");
        //sql.append(" <<AW_UPDATE_USER>> , ");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AW_UPDATE_USER>> , ");
        //Modified for Coeus 4.3 Routing enhancement -PT ID:2785 - end
        sql.append(" <<AC_TYPE>> )");

        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());

        return procReqParameter;
    }


    //Bug Fix code by Ajay for updating person degree Info for person degree table  Start2

     /*For setting the data to ProposalPerDegreeFormBean from
      *DepartmentPerDegreeFormBean to enter the degree info to the degree table
      */
    public ProposalPerDegreeFormBean prepareDegreeBean(String proposalNumber , DepartmentPerDegreeFormBean departmentPerDegreeFormBean ){
        ProposalPerDegreeFormBean proposalPerDegreeFormBean = new ProposalPerDegreeFormBean();

        proposalPerDegreeFormBean.setProposalNumber(proposalNumber);
        proposalPerDegreeFormBean.setPersonId(departmentPerDegreeFormBean.getPersonId());
        proposalPerDegreeFormBean.setDegreeCode(departmentPerDegreeFormBean.getDegreeCode());
        proposalPerDegreeFormBean.setGraduationDate(departmentPerDegreeFormBean.getGraduationDate());
        proposalPerDegreeFormBean.setDegree(departmentPerDegreeFormBean.getDegree());
        proposalPerDegreeFormBean.setFieldOfStudy(departmentPerDegreeFormBean.getFieldOfStudy());
        proposalPerDegreeFormBean.setSpecialization(departmentPerDegreeFormBean.getSpecialization());
        proposalPerDegreeFormBean.setSchool(departmentPerDegreeFormBean.getSchool());
        proposalPerDegreeFormBean.setSchoolIdCode(departmentPerDegreeFormBean.getSchoolIdCode());
        proposalPerDegreeFormBean.setSchoolId(departmentPerDegreeFormBean.getSchoolId());

        proposalPerDegreeFormBean.setAcType("I");
        return proposalPerDegreeFormBean;
    }

    /**
     * Gets all the department degree Details for person id . The
     * return Vector(Collection) from OSP$PERSON_DEGREE table.
     * <li>To fetch the data, it uses the procedure DW_GET_PERSON_DEGREE.
     *
     * @param String personId to get degree details for departmental screen
     * @return DepartmentPerDegreeFormBean attributes of the person.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getPersonDegreeInfo(String personId) throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        DepartmentPerDegreeFormBean departmentPerDegreeFormBean = null;
        HashMap degreePersonRow = null;
        param.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,personId));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PERSON_DEGREE( <<PERSON_ID>>, <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector degreePersonList = null;
        if (listSize > 0){
            degreePersonList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                departmentPerDegreeFormBean = new DepartmentPerDegreeFormBean();
                degreePersonRow = (HashMap)result.elementAt(rowIndex);
                departmentPerDegreeFormBean.setPersonId( (String)
                degreePersonRow.get("PERSON_ID"));
                departmentPerDegreeFormBean.setDegreeCode((String)
                degreePersonRow.get("DEGREE_CODE"));
                departmentPerDegreeFormBean.setAwDegreeCode((String)
                degreePersonRow.get("DEGREE_CODE"));
                departmentPerDegreeFormBean.setDegreeDescription((String)
                degreePersonRow.get("DEGREEDESC"));
                departmentPerDegreeFormBean.setGraduationDate(
                        new Date(((Timestamp) degreePersonRow.get(
                        "GRADUATION_DATE")).getTime()));
                departmentPerDegreeFormBean.setDegree( (String)
                degreePersonRow.get("DEGREE"));
                departmentPerDegreeFormBean.setAwDegree( (String)
                degreePersonRow.get("DEGREE"));
                departmentPerDegreeFormBean.setFieldOfStudy( (String)
                degreePersonRow.get("FIELD_OF_STUDY"));
                departmentPerDegreeFormBean.setSpecialization( (String)
                degreePersonRow.get("SPECIALIZATION"));
                departmentPerDegreeFormBean.setSchool( (String)
                degreePersonRow.get("SCHOOL"));
                departmentPerDegreeFormBean.setSchoolIdCode( (String)
                degreePersonRow.get("SCHOOL_ID_CODE"));
                departmentPerDegreeFormBean.setSchoolDescription( (String)
                degreePersonRow.get("SCHOOLDESC"));
                departmentPerDegreeFormBean.setSchoolId( (String)
                degreePersonRow.get("SCHOOL_ID"));
                departmentPerDegreeFormBean.setUpdateTimestamp( (Timestamp)
                degreePersonRow.get("UPDATE_TIMESTAMP"));
                departmentPerDegreeFormBean.setUpdateUser( (String)
                degreePersonRow.get("UPDATE_USER"));
                degreePersonList.add(departmentPerDegreeFormBean);
            }
        }
        return degreePersonList;

    }

    /**
     *  Method used to update/insert all the details of a Proposal person Degree.
     *  <li>To fetch the data, it uses DW_UPDATE_PROP_PS_DEGREE procedure.
     *
     *  @param vctProposalPerDegreeFormBean Vector containing ProposalPerDegreeFormBean for
     *  insert/modifying the proposal person degree details.
     *  @return boolean true for successful insert/modify
     *  @exception DBException if the instance of a dbEngine is null.
     *  @exception CoeusException if the DB instance is not available.
     */
    public ProcReqParameter addUpdProposalPersonDegree(ProposalPerDegreeFormBean proposalPerDegreeFormBean )
    throws CoeusException ,DBException{
//                boolean success = false;
        Vector paramPersonDegree = new Vector();
        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();

        paramPersonDegree.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalPerDegreeFormBean.getProposalNumber()));
        paramPersonDegree.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                proposalPerDegreeFormBean.getPersonId()));
        paramPersonDegree.addElement(new Parameter("DEGREE_CODE",
                DBEngineConstants.TYPE_STRING,
                proposalPerDegreeFormBean.getDegreeCode()));
        paramPersonDegree.addElement(new Parameter("GRADUATION_DATE",
                DBEngineConstants.TYPE_DATE,
                proposalPerDegreeFormBean.getGraduationDate()));
        paramPersonDegree.addElement(new Parameter("DEGREE",
                DBEngineConstants.TYPE_STRING,
                proposalPerDegreeFormBean.getDegree()));
        paramPersonDegree.addElement(new Parameter("FIELD_OF_STUDY",
                DBEngineConstants.TYPE_STRING,
                proposalPerDegreeFormBean.getFieldOfStudy()));
        paramPersonDegree.addElement(new Parameter("SPECIALIZATION",
                DBEngineConstants.TYPE_STRING,
                proposalPerDegreeFormBean.getSpecialization()));
        paramPersonDegree.addElement(new Parameter("SCHOOL",
                DBEngineConstants.TYPE_STRING,
                proposalPerDegreeFormBean.getSchool()));
        paramPersonDegree.addElement(new Parameter("SCHOOL_ID_CODE",
                DBEngineConstants.TYPE_STRING,
                proposalPerDegreeFormBean.getSchoolIdCode()));
        paramPersonDegree.addElement(new Parameter("SCHOOL_ID",
                DBEngineConstants.TYPE_STRING,
                proposalPerDegreeFormBean.getSchoolId()));
        paramPersonDegree.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramPersonDegree.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramPersonDegree.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalPerDegreeFormBean.getProposalNumber()));
        paramPersonDegree.addElement(new Parameter("AW_PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                proposalPerDegreeFormBean.getPersonId()));
        paramPersonDegree.addElement(new Parameter("AW_DEGREE_CODE",
                DBEngineConstants.TYPE_STRING,
                proposalPerDegreeFormBean.getAwDegreeCode()));
        paramPersonDegree.addElement(new Parameter("AW_DEGREE",
                DBEngineConstants.TYPE_STRING,
                proposalPerDegreeFormBean.getAwDegree()));
        paramPersonDegree.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                proposalPerDegreeFormBean.getUpdateTimestamp()));
        paramPersonDegree.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                proposalPerDegreeFormBean.getAcType()));

        StringBuffer sqlPersonDegree = new StringBuffer(
                "call DW_UPDATE_PROP_PS_DEGREE(");
        sqlPersonDegree.append(" <<PROPOSAL_NUMBER>> , ");
        sqlPersonDegree.append(" <<PERSON_ID>> , ");
        sqlPersonDegree.append(" <<DEGREE_CODE>> , ");
        sqlPersonDegree.append(" <<GRADUATION_DATE>> , ");
        sqlPersonDegree.append(" <<DEGREE>> , ");
        sqlPersonDegree.append(" <<FIELD_OF_STUDY>> , ");
        sqlPersonDegree.append(" <<SPECIALIZATION>> , ");
        sqlPersonDegree.append(" <<SCHOOL>> , ");
        sqlPersonDegree.append(" <<SCHOOL_ID_CODE>> , ");
        sqlPersonDegree.append(" <<SCHOOL_ID>> , ");
        sqlPersonDegree.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlPersonDegree.append(" <<UPDATE_USER>> , ");
        sqlPersonDegree.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlPersonDegree.append(" <<AW_PERSON_ID>> , ");
        sqlPersonDegree.append(" <<AW_DEGREE_CODE>> , ");
        sqlPersonDegree.append(" <<AW_DEGREE>> , ");
        sqlPersonDegree.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlPersonDegree.append(" <<AC_TYPE>> )");

        ProcReqParameter procPersonDegree  = new ProcReqParameter();
        procPersonDegree.setDSN(DSN);
        procPersonDegree.setParameterInfo(paramPersonDegree);
        procPersonDegree.setSqlCommand(sqlPersonDegree.toString());
        return procPersonDegree;
    }
    //Bug Fix code by Ajay for updating person degree Info for person degree table  End2

    //Case #1777 Start
    public void updPropPerson(ProposalPersonFormBean proposalPersonFormBean)
    throws CoeusException,DBException{
        Vector procedures = new Vector(5,3);
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();

        procedures.add(addDelPropPerson(proposalPersonFormBean));
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
    }
    //Case #1777 End

    //Bug fix by Ajay to delete the Person/Rolodex from person table  if the person is deleted
    //both from Investigator and KeyStudyPerson table: Start 2 31-8-2004
    public CoeusVector filterPersons(ProposalDevelopmentFormBean proposalDevelopmentFormBean,
            Vector vecPropPersons){
        Vector vecInvPersons = proposalDevelopmentFormBean.getInvestigators();
        Vector vecKeyPerson = proposalDevelopmentFormBean.getKeyStudyPersonnel();
        int indexPerson = 0;
        CoeusVector cvAllPersons = new CoeusVector();
        cvAllPersons.addAll(vecPropPersons);

        CoeusVector cvPersonsToReturn = new CoeusVector();
        CoeusVector cvPropPersons = new CoeusVector();

        Hashtable htPersonData = new Hashtable();
        String personID;
//            String acType;


        if(vecInvPersons != null && vecInvPersons.size()>0){
            cvPropPersons.addAll(vecInvPersons);
        }

        if(vecKeyPerson != null && vecKeyPerson.size()>0){
            cvPropPersons.addAll(vecKeyPerson);
        }


        //Prepare the vectors of persons.
        //The vector contains the persons with same id
        //Put it into hash table with person id as key

        while(cvPropPersons.size() > 0){
            if(cvPropPersons.get(0) instanceof ProposalInvestigatorFormBean){
                ProposalInvestigatorFormBean investigatorFormBean
                        = (ProposalInvestigatorFormBean)cvPropPersons.get(indexPerson);
                personID = investigatorFormBean.getPersonId();
            }else {
              //  KeyPersonBean keyPersonFormBean=(KeyPersonBean)cvPropPersons.get(indexPerson);
                ProposalKeyPersonFormBean keyPersonFormBean = (ProposalKeyPersonFormBean)cvPropPersons.get(indexPerson);
                personID = keyPersonFormBean.getPersonId();
            }

            Equals eqPersonId = new Equals("personId",personID);
            CoeusVector cvFilteredVector = cvPropPersons.filter(eqPersonId);
            htPersonData.put(personID,cvFilteredVector);

            NotEquals nePersonId = new NotEquals("personId",personID);
            cvPropPersons = cvPropPersons.filter(nePersonId);
        }

        Enumeration enumPerson =  htPersonData.keys();
        while(enumPerson.hasMoreElements()){
            String personKey = enumPerson.nextElement().toString();

            CoeusVector cvPerson = (CoeusVector)htPersonData.get(personKey);
            CoeusVector cvFilterForPersons;

            Equals eqNull = new Equals("acType",null);
            Equals eqInsert = new Equals("acType","I");
            Equals eqDelete = new Equals("acType","D");

            cvFilterForPersons = cvPerson.filter(eqNull);

            //If the ac type is null/update do nothing
            if(cvFilterForPersons != null && cvFilterForPersons .size() > 0){
                continue;
            }else{
                CoeusVector cvPersonsInsert = cvPerson.filter(eqInsert);
                CoeusVector cvPersonsDelete = cvPerson.filter(eqDelete);

                //For Inseting and  ,
                //ie, if a person is inserted in Investigator and
                //the same person is deleted in Key study then insert and delete
                //the person in Person table
                if(cvPersonsInsert != null && cvPersonsDelete != null &&
                        cvPersonsInsert.size() > 0 && cvPersonsDelete.size() > 0){
                    Equals eqPersonId = new Equals("personId", personKey);
                    CoeusVector cvPersonBean = cvAllPersons.filter(eqPersonId);
                    if(cvPersonBean.size() > 0 ){
                        try {
                            ProposalPersonFormBean personFormBean = (ProposalPersonFormBean)cvPersonBean.get(0);

                            ProposalPersonFormBean propPersonBean =
                                    (ProposalPersonFormBean)ObjectCloner.deepCopy(personFormBean);
                            propPersonBean.setAcType("D");

                            cvPersonsToReturn.add(propPersonBean);

                            propPersonBean =
                                    (ProposalPersonFormBean)ObjectCloner.deepCopy(personFormBean);
                            propPersonBean.setAcType("I");

                            cvPersonsToReturn.add(propPersonBean);
                        }catch (Exception ex){
                            // commented for using UtilFactory.log instead of printStackTrace
                            UtilFactory.log(ex.getMessage(),ex,"ProposalDevelopmentUpdateTxnBean", "filterPersons");
//                                ex.printStackTrace();
                        }
                    }//End of inner if
                }else if(cvPersonsInsert.size() > 0){//If the person ac type is insert
                    Equals eqPersonId = new Equals("personId", personKey);
                    CoeusVector cvPersonBean = cvAllPersons.filter(eqPersonId);
                    if(cvPersonBean.size() > 0 ){
                        try {
                            ProposalPersonFormBean personFormBean = (ProposalPersonFormBean)cvPersonBean.get(0);
                            ProposalPersonFormBean propPersonBean =
                                    (ProposalPersonFormBean)ObjectCloner.deepCopy(personFormBean);
                            propPersonBean.setAcType("I");

                            cvPersonsToReturn.add(propPersonBean);
                        }catch (Exception ex){
                            // commented for using UtilFactory.log instead of printStackTrace
                            UtilFactory.log(ex.getMessage(),ex,"ProposalDevelopmentUpdateTxnBean", "filterPersons");
//                                ex.printStackTrace();
                        }
                    }
                }else if(cvPersonsDelete.size() > 0){//If the person ac type is delete
                    Equals eqPersonId = new Equals("personId", personKey);
                    CoeusVector cvPersonBean = cvAllPersons.filter(eqPersonId);
                    if(cvPersonBean.size() > 0 ){
                        try {
                            ProposalPersonFormBean personFormBean = (ProposalPersonFormBean)cvPersonBean.get(0);
                            ProposalPersonFormBean propPersonBean =
                                    (ProposalPersonFormBean)ObjectCloner.deepCopy(personFormBean);
                            propPersonBean.setAcType("D");

                            cvPersonsToReturn.add(propPersonBean);
                        }catch (Exception ex){
                            // commented for using UtilFactory.log instead of printStackTrace
                            UtilFactory.log(ex.getMessage(),ex,"ProposalDevelopmentUpdateTxnBean", "filterPersons");
//                                ex.printStackTrace();
                        }
                    }
                }//End of inner else
            }//End of outer else
        }//End of while
        return cvPersonsToReturn;
    }//End FilterPersons

    //Bug fix by Ajay to delete the Person/Rolodex from person table  if the person is deleted
    //both from Investigator and KeyStudyPerson table: End 2 31-8-2004


    //Added for the Coeus Enhancement Case #1799 to get the protocolNotePadBean
    public ProtocolNotepadBean getProtocolNotepadBean(ProtocolFundingSourceBean protocolFundingSourceBean , int maxEntryNumber )
    throws CoeusException,DBException{
        ProtocolNotepadBean protocolNotepadBean = new ProtocolNotepadBean();
        protocolNotepadBean.setProtocolNumber(protocolFundingSourceBean.getProtocolNumber());
        protocolNotepadBean.setSequenceNumber(protocolFundingSourceBean.getSequenceNumber());
        protocolNotepadBean.setEntryNumber(maxEntryNumber);
        if(protocolFundingSourceBean.getAcType() != null && protocolFundingSourceBean.getAcType().equals("I")){
            CoeusMessageResourcesBean coeusMessageResourcesBean =new CoeusMessageResourcesBean();
            String insertComments = coeusMessageResourcesBean.parseMessageKey("notepadInsert_exceptionCode.6000");
            protocolNotepadBean.setComments(insertComments);
        }
        protocolNotepadBean.setRestrictedFlag(false);
        protocolNotepadBean.setAcType("I");
        protocolNotepadBean.setUpdateTimestamp(dbTimestamp);
        return protocolNotepadBean;
    }

    //Added for the Coeus Enhancement Case #1799 to get the protocol link bean
    public ProtocolLinkBean getProtocolLinkBean(ProtocolFundingSourceBean protocolFundingSourceBean,InstituteProposalSpecialReviewBean instPropSpRevBean)
    throws CoeusException,DBException{
        ProtocolLinkBean protocolLinkBean = new ProtocolLinkBean();
        protocolLinkBean.setProtocolNumber(protocolFundingSourceBean.getProtocolNumber());
        protocolLinkBean.setSequenceNumber(protocolFundingSourceBean.getSequenceNumber());
        protocolLinkBean.setModuleCode(2);
        protocolLinkBean.setModuleItemKey(instPropSpRevBean.getProposalNumber());
        protocolLinkBean.setModuleItemSeqNumber(instPropSpRevBean.getSequenceNumber());
        if(protocolFundingSourceBean.getAcType() != null && protocolFundingSourceBean.getAcType().equals("I")){
            CoeusMessageResourcesBean coeusMessageResourcesBean =new CoeusMessageResourcesBean();
            String insertComments = coeusMessageResourcesBean.parseMessageKey("notepadInsert_exceptionCode.6000");
            protocolLinkBean.setComments(insertComments);
            protocolLinkBean.setActionType("I");
        }else{
            CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
            String deleteComments = coeusMessageResourcesBean.parseMessageKey("notepadDelete_exceptionCode.6001");
            protocolLinkBean.setComments(deleteComments);
            protocolLinkBean.setActionType("D");
        }
        return protocolLinkBean;

    }

    //Added for the Coeus Enhancement Case #1799 to perform linking
    public void performProtocolLinkFromProposalDev(CoeusVector cvInstPropData,String unitNumber)
    throws Exception,DBException{
        Vector vecProcedures = new Vector();
        Vector protocolData = null;
        CoeusVector cvInstPropSpecialRev = (CoeusVector)cvInstPropData.get(0);
        InstituteProposalUpdateTxnBean instituteProposalUpdateTxnBean = new InstituteProposalUpdateTxnBean(userId);
        ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean(userId);
        if(cvInstPropSpecialRev != null){
            for(int index = 0 ; index < cvInstPropSpecialRev.size() ; index ++){
                InstituteProposalSpecialReviewBean instPropBean = (InstituteProposalSpecialReviewBean)cvInstPropSpecialRev.get(index);
                if(instPropBean.getSpecialReviewCode() == Integer.parseInt((String)cvInstPropData.get(1))){
                    ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean(userId);

                    //insert the row in protocol funding source table to establich the link
                    if(protocolData == null)
                        protocolData = new Vector();
                    String spRevNum = instPropBean.getProtocolSPRevNumber();
                    CoeusDataTxnBean coeusDataTxnBean = new CoeusDataTxnBean();
                    boolean validProtocolNumber = coeusDataTxnBean.validateProtocolNumber(spRevNum);
                    if(validProtocolNumber){
                        boolean lockCheck = protocolDataTxnBean.lockCheck(spRevNum, userId);
                        if(!lockCheck){
                            //if lock is there then add an 'X' to the protocol number
                            instPropBean.setProtocolSPRevNumber("X"+spRevNum);
                        }else{
                            String indicator = "P1";
                            ProtocolFundingSourceBean protocolFundingSourceBean = null;

                            //first update the inst prop special review
                            instPropBean.setSpecialReviewCode(Integer.parseInt((String)cvInstPropData.get(1)));
                            instPropBean.setApprovalCode(Integer.parseInt((String)cvInstPropData.get(2)));

                            //insert a row to protocol funding source to establish the link
                            edu.mit.coeus.utils.locking.LockingBean lockingBean =  protocolDataTxnBean.getLock(spRevNum, userId, unitNumber);
                            protocolDataTxnBean.transactionCommit();
                            boolean lockAvailable = lockingBean.isGotLock();
                            if(lockAvailable){
                                ProtocolInfoBean protocolInfoBean = (ProtocolInfoBean)protocolDataTxnBean.getProtocolMaintenanceDetails(spRevNum);
                                protocolFundingSourceBean = new ProtocolFundingSourceBean();
                                protocolFundingSourceBean.setProtocolNumber(spRevNum);
                                protocolFundingSourceBean.setSequenceNumber(protocolInfoBean.getSequenceNumber());
                                protocolFundingSourceBean.setFundingSourceTypeCode(5);
                                protocolFundingSourceBean.setFundingSource(instPropBean.getProposalNumber());
                                protocolFundingSourceBean.setAcType("I");
                                // 3847: Unique constraint error with resubmission -Start
//                                vecProcedures.add(protocolUpdateTxnBean.addUpdProtocolFundSource(protocolFundingSourceBean));
                                // Check if Linking is already there
                                boolean isLinkExists = protocolUpdateTxnBean.isProtocolLinkExists(protocolFundingSourceBean);
                                if(!isLinkExists){
                                    // IRB link does NOT exist
                                    vecProcedures.add(protocolUpdateTxnBean.addUpdProtocolFundSource(protocolFundingSourceBean));
                                    ProtocolLinkBean protocolLinkBean = getProtocolLinkBean(protocolFundingSourceBean,instPropBean);
                                    vecProcedures.add(protocolUpdateTxnBean.addUpdProtocolLinks(protocolLinkBean));
                                }
                                // 3847: Unique constraint error with resubmission - End
                            }
                            if(protocolFundingSourceBean != null){
                                int maxEntryNumber = protocolDataTxnBean.getMaxProtocolNotesEntryNumber(protocolFundingSourceBean.getProtocolNumber());
                                maxEntryNumber = maxEntryNumber + 1;
                                ProtocolNotepadBean protocolNotepadBean = getProtocolNotepadBean(protocolFundingSourceBean,maxEntryNumber);
                                vecProcedures.add(protocolUpdateTxnBean.addUpdProtocolNotepad(protocolNotepadBean));
                                // 3847: Unique constraint error with resubmission - Start
//                                ProtocolLinkBean protocolLinkBean = getProtocolLinkBean(protocolFundingSourceBean,instPropBean);
//                                vecProcedures.add(protocolUpdateTxnBean.addUpdProtocolLinks(protocolLinkBean));
                                // 3847: Unique constraint error with resubmission - End
                                protocolUpdateTxnBean.updateFundingSourceIndicator(
                                        protocolFundingSourceBean.getProtocolNumber(),indicator,"7");
//                                CoeusMessageResourcesBean coeusMessageResourcesBean
//                                        =new CoeusMessageResourcesBean();
                                try{
                                    //Modified for COEUSDEV-75:Rework email engine so the email body is picked up from one place
                                    protocolDataTxnBean.sendEmailNotification(ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE,MailActions.SPECIAL_REVIEW_INSERTED,
                                            instPropBean.getProposalNumber(),instPropBean.getSequenceNumber(),protocolFundingSourceBean.getProtocolNumber(), ModuleConstants.PROTOCOL_MODULE_CODE);
                                    //COEUSDEV-75:End
                                }catch(Exception ex){
                                    UtilFactory.log("Could not send the mail during the linking process of Protocol "+protocolFundingSourceBean.getProtocolNumber()+
                                            " and institute proposal "+instPropBean.getProposalNumber(),
                                            ex,
                                            "ProposalDevelopmentUpdateTxnBean",
                                            "performProtocolLinkFromProposalDev");
                                }
                                //COEUSDEV-75:Rework email engine so the email body is picked up from one place
                                if(protocolUpdateTxnBean.updateInboxTable(protocolFundingSourceBean.getProtocolNumber(),ModuleConstants.PROTOCOL_MODULE_CODE,protocolFundingSourceBean.getAcType()) != null){
                                    vecProcedures.addAll(protocolUpdateTxnBean.updateInboxTable(protocolFundingSourceBean.getProtocolNumber(),ModuleConstants.PROTOCOL_MODULE_CODE,protocolFundingSourceBean.getAcType()));
                                }
                                //COEUSDEV-75:End
                                protocolData.addElement(protocolFundingSourceBean);
                            }
                        }
                        instPropBean.setAcType("U");
                        vecProcedures.add(instituteProposalUpdateTxnBean.addUpdInstituteProposalSpecialReview(instPropBean));
                    }
                }
            }
            if(dbEngine!=null){
                if(!vecProcedures.isEmpty()) dbEngine.executeStoreProcs(vecProcedures);
            }else{
                unLockProtocols(protocolData);
                throw new CoeusException("db_exceptionCode.1000");
            }
            unLockProtocols(protocolData);
        }

    }

    //Added for the Coeus Enhancement case:#1799
    private void unLockProtocols(Vector protocolData) throws CoeusException,DBException{
        if(protocolData != null && protocolData.size()>0) {
//            ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean();
            for(int index=0;index<protocolData.size();index++) {
                ProtocolFundingSourceBean bean = (ProtocolFundingSourceBean)protocolData.get(index);
                //protocolUpdateTxnBean.releaseLock(bean.getProtocolNumber(), userId);
                transMon.releaseLock("osp$Protocol_"+bean.getProtocolNumber(),userId);
            }
        }
    }

    //Coeus Enhancement Case #1799 start
    /**
     * Method used to get proposal title from OSP$EPS_PROPOSAL
     * for a given proposal number
     * @param proposalId this is given as input parameter for the
     * procedure to execute.
     * @return String proposalTitle
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public String getProposalTitle(String proposalId)
    throws CoeusException,DBException{
        Vector param = new Vector();
        Vector result = new Vector();
        String proposalTitle = "";

        param.addElement(new Parameter("AV_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalId));

        if(dbEngine!=null){
            result = new Vector(1);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_EPS_PROPOSAL_TITLE(<<AV_PROPOSAL_NUMBER>> , "
                    +" << OUT STRING as_title >>)", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap hash = (HashMap)result.elementAt(0);
            proposalTitle = hash.get("as_title").toString();
        }
        return proposalTitle;
    }

    /**
     * Method used to validate a given proposal number in OSP$PROPOSAL
     * @param proposalNumber this is given as input parameter for the
     * procedure to execute.
     * @return String proposalTitle
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public int validateProposalNumber(String proposalNumber)
    throws CoeusException, DBException {

        Vector param= new Vector();
        Vector result = new Vector();
        int exist = -1;
        param.add(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNumber));



        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER ll_count>> = "
                    +" call FN_IS_VALID_DEV_PROP_NUM(<< PROPOSAL_NUMBER >>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            exist = Integer.parseInt(rowParameter.get("ll_count").toString());
        }
        return exist;
    }
    //Coeus Enhancement Case #1799 end

    //End Coeus Enhancement case:#1799

    //Case 2106 Start
    public void addUpdCreditSplit(CoeusVector cvData) throws CoeusException, DBException{
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        Vector procedures = new Vector(5,3);

        CoeusVector cvInvCreditData = (CoeusVector)cvData.get(0);
        CoeusVector cvUnitCreditData = (CoeusVector)cvData.get(1);

        if(cvInvCreditData != null && cvInvCreditData.size() > 0){
            for(int i = 0  ; i < cvInvCreditData.size() ; i++){
                InvestigatorCreditSplitBean invCreditSplitBean =
                        (InvestigatorCreditSplitBean)cvInvCreditData.get(i);
                if(invCreditSplitBean.getAcType() != null){
                    procedures.add(addUpdInvCredit(invCreditSplitBean));
                }
            }
        }

        if(cvUnitCreditData != null && cvUnitCreditData.size() > 0){
            for(int j = 0  ; j < cvUnitCreditData.size() ; j++){
                InvestigatorCreditSplitBean invCreditSplitBean =
                        (InvestigatorCreditSplitBean)cvUnitCreditData.get(j);
                if(invCreditSplitBean.getAcType() != null){
                    procedures.add(addUpdUnitCredit(invCreditSplitBean));
                }
            }
        }

        if(dbEngine!=null){
            try{
                if(procedures.size()>0){
                    dbEngine.executeStoreProcs(procedures);
                }
            }catch (DBException dbEx){
                throw new CoeusException(dbEx.getMessage());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
    }

    /**
     *  Method used to update/insert all the details of a Investigator credit split
     *  <li>To fetch the data, it uses UPD_EPS_PROP_PER_CREDIT_SPLIT procedure.
     *
     *  @param InvestigatorCreditSplitBean this bean contains data for insert.
     *  @return ProcReqParameter
     *  @exception DBException , CoeusException if the instance of a dbEngine is null.
     */
    public ProcReqParameter addUpdInvCredit( InvestigatorCreditSplitBean invCreditSplitBean)
    throws CoeusException,DBException{
        Vector paramInvCredit = new Vector();

        paramInvCredit.addElement(new Parameter("AV_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, invCreditSplitBean.getModuleNumber()));

        paramInvCredit.addElement(new Parameter("AV_PERSON_ID",
                DBEngineConstants.TYPE_STRING, invCreditSplitBean.getPersonId()));

        paramInvCredit.addElement(new Parameter("AV_INV_CREDIT_TYPE_CODE",
                DBEngineConstants.TYPE_INT, ""+invCreditSplitBean.getInvCreditTypeCode()));

        paramInvCredit.addElement(new Parameter("AV_CREDIT",
                DBEngineConstants.TYPE_DOUBLE_OBJ, invCreditSplitBean.getCredit().equals(new Double(0)) ? null : invCreditSplitBean.getCredit()));

        paramInvCredit.addElement(new Parameter("AV_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));

        paramInvCredit.addElement(new Parameter("AV_UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));

        paramInvCredit.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, invCreditSplitBean.getModuleNumber()));

        paramInvCredit.addElement(new Parameter("AW_PERSON_ID",
                DBEngineConstants.TYPE_STRING, invCreditSplitBean.getPersonId()));

        paramInvCredit.addElement(new Parameter("AW_INV_CREDIT_TYPE_CODE",
                DBEngineConstants.TYPE_INT, ""+invCreditSplitBean.getInvCreditTypeCode()));

        paramInvCredit.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP, invCreditSplitBean.getUpdateTimestamp()));

        paramInvCredit.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING, invCreditSplitBean.getAcType()));

        StringBuffer sqlInvCredit = new StringBuffer(
                "{ call UPD_EPS_PROP_PER_CREDIT_SPLIT(");
        sqlInvCredit.append(" <<AV_PROPOSAL_NUMBER>> , ");
        sqlInvCredit.append(" <<AV_PERSON_ID>> , ");
        sqlInvCredit.append(" <<AV_INV_CREDIT_TYPE_CODE>> , ");
        sqlInvCredit.append(" <<AV_CREDIT>> , ");
        sqlInvCredit.append(" <<AV_UPDATE_TIMESTAMP>> , ");
        sqlInvCredit.append(" <<AV_UPDATE_USER>> , ");
        sqlInvCredit.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlInvCredit.append(" <<AW_PERSON_ID>> , ");
        sqlInvCredit.append(" <<AW_INV_CREDIT_TYPE_CODE>> , ");
        sqlInvCredit.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlInvCredit.append(" <<AC_TYPE>> ) }");

        ProcReqParameter procInvCredit  = new ProcReqParameter();
        procInvCredit.setDSN(DSN);
        procInvCredit.setParameterInfo(paramInvCredit);
        procInvCredit.setSqlCommand(sqlInvCredit.toString());

        return procInvCredit;

    }

    /**
     *  Method used to update/insert all the details of a Investigator credit split
     *  <li>To fetch the data, it uses UPD_EPS_PROP_UNIT_CREDIT_SPLIT procedure.
     *
     *  @param InvestigatorCreditSplitBean this bean contains data for insert.
     *  @return ProcReqParameter
     *  @exception DBException , CoeusException if the instance of a dbEngine is null.
     */
    public ProcReqParameter addUpdUnitCredit( InvestigatorCreditSplitBean invCreditSplitBean)
    throws CoeusException,DBException{
        Vector paramUnitCredit = new Vector();

        paramUnitCredit.addElement(new Parameter("AV_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, invCreditSplitBean.getModuleNumber()));

        paramUnitCredit.addElement(new Parameter("AV_PERSON_ID",
                DBEngineConstants.TYPE_STRING, invCreditSplitBean.getPersonId()));

        paramUnitCredit.addElement(new Parameter("AV_UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING, invCreditSplitBean.getUnitNumber()));

        paramUnitCredit.addElement(new Parameter("AV_INV_CREDIT_TYPE_CODE",
                DBEngineConstants.TYPE_INT, ""+invCreditSplitBean.getInvCreditTypeCode()));

        paramUnitCredit.addElement(new Parameter("AV_CREDIT",
                DBEngineConstants.TYPE_DOUBLE_OBJ, invCreditSplitBean.getCredit().equals(new Double(0)) ? null : invCreditSplitBean.getCredit()));

        paramUnitCredit.addElement(new Parameter("AV_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));

        paramUnitCredit.addElement(new Parameter("AV_UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));

        paramUnitCredit.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, invCreditSplitBean.getModuleNumber()));

        paramUnitCredit.addElement(new Parameter("AW_PERSON_ID",
                DBEngineConstants.TYPE_STRING, invCreditSplitBean.getPersonId()));

        paramUnitCredit.addElement(new Parameter("AW_UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING, invCreditSplitBean.getUnitNumber()));

        paramUnitCredit.addElement(new Parameter("AW_INV_CREDIT_TYPE_CODE",
                DBEngineConstants.TYPE_INT, ""+invCreditSplitBean.getInvCreditTypeCode()));

        paramUnitCredit.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP, invCreditSplitBean.getUpdateTimestamp()));

        paramUnitCredit.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING, invCreditSplitBean.getAcType()));

        StringBuffer sqlUnitCredit = new StringBuffer(
                "{ call UPD_EPS_PROP_UNIT_CREDIT_SPLIT(");
        sqlUnitCredit.append(" <<AV_PROPOSAL_NUMBER>> , ");
        sqlUnitCredit.append(" <<AV_PERSON_ID>> , ");
        sqlUnitCredit.append(" <<AV_UNIT_NUMBER>> , ");
        sqlUnitCredit.append(" <<AV_INV_CREDIT_TYPE_CODE>> , ");
        sqlUnitCredit.append(" <<AV_CREDIT>> , ");
        sqlUnitCredit.append(" <<AV_UPDATE_TIMESTAMP>> , ");
        sqlUnitCredit.append(" <<AV_UPDATE_USER >> , ");
        sqlUnitCredit.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlUnitCredit.append(" <<AW_PERSON_ID>> , ");
        sqlUnitCredit.append(" <<AW_UNIT_NUMBER>> , ");
        sqlUnitCredit.append(" <<AW_INV_CREDIT_TYPE_CODE>> , ");
        sqlUnitCredit.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlUnitCredit.append(" <<AC_TYPE>> ) }");

        ProcReqParameter procUnitCredit  = new ProcReqParameter();
        procUnitCredit.setDSN(DSN);
        procUnitCredit.setParameterInfo(paramUnitCredit);
        procUnitCredit.setSqlCommand(sqlUnitCredit.toString());

        return procUnitCredit;

    }
    //Case 2106 End

    //Commented for case 2406 - Organization and Location - start
    /**
     *  Method used to update/insert all the details of a Proposal Location.
     *  <li>To fetch the data, it uses UPD_PROP_LOCATION procedure.
     *
     *  @param proposalLocationFormBean this bean contains data for insert.
     *  @return boolean this holds true for successfull insert/modify or
     *  false if fails.
     *  this class before executing the procedure.
     *  @exception DBException if the instance of a dbEngine is null.
     *  This is used in Coeuslite proposal Location
     */
//    public boolean addUpdateProposalLocations( ProposalLocationFormBean
//            proposalLocationFormBean)  throws CoeusException,DBException{
////        Vector param = new Vector();
//        Vector procedures = new Vector(5,3);
////        ProcReqParameter procReqParameter = null;
////        param = new Vector();
//        boolean success = false;
//        dbEngine = new DBEngineImpl();
//
//
//        Vector paramLocations = new Vector();
//        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
//        //proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(userId);
//
//        paramLocations.addElement(new Parameter("PROPOSAL_NUMBER",
//                DBEngineConstants.TYPE_STRING,
//                proposalLocationFormBean.getProposalNumber()));
//        paramLocations.addElement(new Parameter("LOCATION",
//                DBEngineConstants.TYPE_STRING,
//                proposalLocationFormBean.getProposalLocation()));
//        paramLocations.addElement(new Parameter("ROLODEX_ID",
//                DBEngineConstants.TYPE_STRING,
//                proposalLocationFormBean.getRolodexId() == 0 ? null :
//                    ""+proposalLocationFormBean.getRolodexId()));
//        paramLocations.addElement(new Parameter("UPDATE_USER",
//                DBEngineConstants.TYPE_STRING,proposalLocationFormBean.getUpdateUser()));
//        paramLocations.addElement(new Parameter("UPDATE_TIMESTAMP",
//                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
//        paramLocations.addElement(new Parameter("AW_PROPOSAL_NUMBER",
//                DBEngineConstants.TYPE_STRING,
//                proposalLocationFormBean.getProposalNumber()));
//        paramLocations.addElement(new Parameter("AW_LOCATION",
//                DBEngineConstants.TYPE_STRING,
//                proposalLocationFormBean.getProposalLocation()));
//        paramLocations.addElement(new Parameter("AW_UPDATE_USER",
//                DBEngineConstants.TYPE_STRING, proposalLocationFormBean.getUpdateUser()));
//        paramLocations.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
//                DBEngineConstants.TYPE_TIMESTAMP,
//                proposalLocationFormBean.getUpdateTimestamp()));
//        paramLocations.addElement(new Parameter("AC_TYPE",
//                DBEngineConstants.TYPE_STRING,
//                proposalLocationFormBean.getAcType()));
//
//        StringBuffer sqlLocations = new StringBuffer(
//                "call UPD_PROP_LOCATION(");
//        sqlLocations.append(" <<PROPOSAL_NUMBER>> , ");
//        sqlLocations.append(" <<LOCATION>> , ");
//        sqlLocations.append(" <<ROLODEX_ID>> , ");
//        sqlLocations.append(" <<UPDATE_USER>> , ");
//        sqlLocations.append(" <<UPDATE_TIMESTAMP>> , ");
//        sqlLocations.append(" <<AW_PROPOSAL_NUMBER>> , ");
//        sqlLocations.append(" <<AW_LOCATION>> , ");
//        sqlLocations.append(" <<AW_UPDATE_USER>> , ");
//        sqlLocations.append(" <<AW_UPDATE_TIMESTAMP>> , ");
//        sqlLocations.append(" <<AC_TYPE>> )");
//
//        ProcReqParameter procLocations  = new ProcReqParameter();
//        procLocations.setDSN(DSN);
//        procLocations.setParameterInfo(paramLocations);
//        procLocations.setSqlCommand(sqlLocations.toString());
//
//        // return procLocations;
//        procedures.add(procLocations);
//
//        if(dbEngine!=null){
//            dbEngine.executeStoreProcs(procedures);
//        }else{
//            throw new CoeusException("db_exceptionCode.1000");
//        }
//        success = true;
//        return success;
//    }
    //Commented for case 2406 - Organization and Location - end

    /** Update the child proposals, if the parent propsal creation status changes
     *Ensure that, the creation status of the child should be same as
     *parent proposal creation status
     *@param Propsoal number
     *@return boolean for updation
     *@ throws CoeusException and DBException
     */
    public boolean updateChildProposalstatus(String proposalNumber, String userId)
    throws CoeusException,DBException{

        int number = 0;
        boolean isSuccess = false;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("AS_PARENT_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.add(new Parameter("AS_UPDATE_USER",
                DBEngineConstants.TYPE_STRING,userId));
        /* calling stored function */
        if(dbEngine!=null){
            try{
                result = dbEngine.executeFunctions("Coeus",
                        "{ <<OUT INTEGER IS_SUCCESS>> = "
                        +" call FN_UPD_CHILD_PROPOSAL_STATUS( "
                        +" << AS_PARENT_PROPOSAL_NUMBER >>, << AS_UPDATE_USER >> ) }", param);
            }catch (DBException dbEx){
                throw new CoeusException(dbEx.getMessage());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            number = Integer.parseInt(rowParameter.get("IS_SUCCESS").toString());
            if(number == 0){
                isSuccess = true;
            }
        }
        return isSuccess;
    }

    // COEUSQA-2321: Copy Questionnaires for Proposal Development records - Start
//    public static void main(String args[]){
//        try{
//            ProposalDevelopmentTxnBean proposalDataTxnBean = new ProposalDevelopmentTxnBean();
//            String targetProposalNumber =  proposalDataTxnBean.getNextProposalNumber();
//            ProposalDevelopmentUpdateTxnBean proposalUpdTxnBean = new ProposalDevelopmentUpdateTxnBean("COEUS");
//
//            int intCopyFlag = proposalUpdTxnBean.copyProposal("00001165", targetProposalNumber, 'Y', 'Y', "000001", "COEUS");
//
//        }catch(Exception ex){
//            // commented for using UtilFactory.log instead of printStackTrace
//            UtilFactory.log(ex.getMessage(),ex,"ProposalDevelopmentUpdateTxnBean", "main");
////             ex.printStackTrace();
//        }
//    }

    // COEUSQA-2321: Copy Questionnaires for Proposal Development records - End
    /** Added for Proposal Hierarchy Enhancement Case# 3183 - Start
     * Update the Proposal Hierarchy link data
     * @param ProposalNarrativeFormBean
     * @return Vector of personbio info
     * @throws CoeusException and DBException
     */
    public ProcReqParameter updatePropHierLinkData( ProposalNarrativeFormBean
    proposalNarrativeFormBean)  throws CoeusException ,DBException{

        Vector paramPersonBio= new Vector();

        paramPersonBio.addElement(new Parameter("PARENT_PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,
        proposalNarrativeFormBean.getProposalNumber()));
        paramPersonBio.addElement(new Parameter("PARENT_MODULE_NUMBER",
        DBEngineConstants.TYPE_INT,
        ""+proposalNarrativeFormBean.getModuleNumber()));
        paramPersonBio.addElement(new Parameter("PERSON_ID",
        DBEngineConstants.TYPE_STRING, ""));
        paramPersonBio.addElement(new Parameter("CHILD_PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,
        proposalNarrativeFormBean.getProposalNumber()));
        paramPersonBio.addElement(new Parameter("CHILD_MODULE_NUMBER",
        DBEngineConstants.TYPE_INT,
        ""+proposalNarrativeFormBean.getModuleNumber()));
        paramPersonBio.addElement(new Parameter("DOCUMENT_TYPE_CODE",
        DBEngineConstants.TYPE_INT,
        ""+proposalNarrativeFormBean.getNarrativeTypeCode()));
        paramPersonBio.addElement(new Parameter("LINK_TYPE",
        DBEngineConstants.TYPE_STRING, "N"));
        paramPersonBio.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramPersonBio.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING, userId));
        paramPersonBio.addElement(new Parameter("AW_PARENT_PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,
        proposalNarrativeFormBean.getProposalNumber()));
        paramPersonBio.addElement(new Parameter("AW_PARENT_MODULE_NUMBER",
        DBEngineConstants.TYPE_INT,
        ""+proposalNarrativeFormBean.getModuleNumber()));
        paramPersonBio.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING, proposalNarrativeFormBean.getAcType()));

        StringBuffer sqlPersonBio = new StringBuffer(
        "call UPD_EPS_PROP_HIER_ATT_LINKS(");
        sqlPersonBio.append(" <<PARENT_PROPOSAL_NUMBER>> , ");
        sqlPersonBio.append(" <<PARENT_MODULE_NUMBER>> , ");
        sqlPersonBio.append(" <<PERSON_ID>> , ");
        sqlPersonBio.append(" <<CHILD_PROPOSAL_NUMBER>> , ");
        sqlPersonBio.append(" <<CHILD_MODULE_NUMBER>> , ");
        sqlPersonBio.append(" <<DOCUMENT_TYPE_CODE>> , ");
        sqlPersonBio.append(" <<LINK_TYPE>> , ");
        sqlPersonBio.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlPersonBio.append(" <<UPDATE_USER>> , ");
        sqlPersonBio.append(" <<AW_PARENT_PROPOSAL_NUMBER>> , ");
        sqlPersonBio.append(" <<AW_PARENT_MODULE_NUMBER>> , ");
        sqlPersonBio.append(" <<AC_TYPE>> )");

        ProcReqParameter procPersonBio  = new ProcReqParameter();
        procPersonBio.setDSN(DSN);
        procPersonBio.setParameterInfo(paramPersonBio);
        procPersonBio.setSqlCommand(sqlPersonBio.toString());

        return procPersonBio;
    } //Added for Proposal Hierarchy Enhancement Case# 3183 - End

    //Added for case 2406 - Organization and Location - start
    /**
     *  Method used to insert/update/delete Proposal Congressional District
     *
     *  @param ProposalSiteBean
     *  @return ProcReqParameter
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    private ProcReqParameter addDeletePropSiteCongDistrict(ProposalCongDistrictBean proposalCongDistrictBean)
    throws CoeusException, DBException{
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalCongDistrictBean.getProposalNumber()));
        param.addElement(new Parameter("SITE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+proposalCongDistrictBean.getSiteNumber()));
        param.addElement(new Parameter("CONG_DISTRICT",
                DBEngineConstants.TYPE_STRING,
                ""+proposalCongDistrictBean.getCongDistrict()));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("AW_CONG_DISTRICT",
                DBEngineConstants.TYPE_STRING,
                ""+proposalCongDistrictBean.getAw_CongDistrict()));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                proposalCongDistrictBean.getUpdateTimestamp()));
        param.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING, proposalCongDistrictBean.getAcType()));
       StringBuffer sql = new StringBuffer(
               "call UPDATE_PROP_SITE_CONG_DISTRICT(");
       sql.append(" <<PROPOSAL_NUMBER>>, ");
       sql.append(" <<SITE_NUMBER>>, ");
       sql.append(" <<CONG_DISTRICT>>, ");
       sql.append(" <<UPDATE_TIMESTAMP>>, ");
       sql.append(" <<UPDATE_USER>>, ");
       sql.append(" <<AW_CONG_DISTRICT>>, ");
       sql.append(" <<AW_UPDATE_TIMESTAMP>>, ");
       sql.append(" <<AC_TYPE>> ) ");

       ProcReqParameter procReqParameter  = new ProcReqParameter();
       procReqParameter.setDSN(DSN);
       procReqParameter.setParameterInfo(param);
       procReqParameter.setSqlCommand(sql.toString());

       return procReqParameter;
    }

    /**
     *  Method used to insert/update/delete Proposal site
     *
     *  @param ProposalSiteBean
     *  @return ProcReqParameter
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    private ProcReqParameter addUpdDelProposalSite(ProposalSiteBean proposalSiteBean)
    throws CoeusException, DBException{
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            proposalSiteBean.getProposalNumber()));
        param.addElement(new Parameter("SITE_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+proposalSiteBean.getSiteNumber()));
        param.addElement(new Parameter("LOCATION_NAME",
            DBEngineConstants.TYPE_STRING,
            ""+proposalSiteBean.getLocationName()));
        param.addElement(new Parameter("LOCATION_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+proposalSiteBean.getLocationTypeCode()));
        param.addElement(new Parameter("ORGANIZATION_ID",
            DBEngineConstants.TYPE_STRING,
            proposalSiteBean.getOrganizationId()));
        String rolodexId = null;
        if(proposalSiteBean.getRolodexDetailsBean() != null){
            rolodexId = proposalSiteBean.getRolodexDetailsBean().getRolodexId();
        }
        if(rolodexId == null || rolodexId.trim().length() == 0){
            rolodexId = "0";
        }
        param.addElement(new Parameter("ROLODEX_ID",
            DBEngineConstants.TYPE_STRING,rolodexId));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,proposalSiteBean.getUpdateTimestamp()));
        param.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING, proposalSiteBean.getAcType()));
       StringBuffer sql = new StringBuffer(
               "call UPDATE_PROPOSAL_SITE(");
       sql.append(" <<PROPOSAL_NUMBER>>, ");
       sql.append(" <<SITE_NUMBER>>, ");
       sql.append(" <<LOCATION_NAME>>, ");
       sql.append(" <<LOCATION_TYPE_CODE>>, ");
       sql.append(" <<ORGANIZATION_ID>>, ");
       sql.append(" <<ROLODEX_ID>>, ");
       sql.append(" <<UPDATE_TIMESTAMP>>, ");
       sql.append(" <<UPDATE_USER>>, ");
       sql.append(" <<AW_UPDATE_TIMESTAMP>>, ");
       sql.append(" <<AC_TYPE>> ) ");

       ProcReqParameter procReqParameter  = new ProcReqParameter();
       procReqParameter.setDSN(DSN);
       procReqParameter.setParameterInfo(param);
       procReqParameter.setSqlCommand(sql.toString());

       return procReqParameter;
    }
    /**
     *  Method used to insert/update/delete Proposal site and the congressional
     *  districts
     *
     *  @param ProposalSiteBean
     *  @return Vector
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    private Vector addUpdDelProposalSites(ProposalSiteBean proposalSiteBean)
    throws CoeusException, DBException{
        Vector vecProcReqParameter = new Vector();
        if(proposalSiteBean != null){
            if(proposalSiteBean.getAcType() != null){
                vecProcReqParameter.add(addUpdDelProposalSite(proposalSiteBean));
            }
            if(proposalSiteBean.getAcType() == null
                    || (proposalSiteBean.getAcType()!=null && !proposalSiteBean.getAcType().equals(TypeConstants.DELETE_RECORD))){
                CoeusVector vecCongDistricts = proposalSiteBean.getCongDistricts();
                if(vecCongDistricts != null && vecCongDistricts.size() > 0){
                    for(int count = 0; count <vecCongDistricts.size(); count++){
                        ProposalCongDistrictBean proposalCongDistrictBean =
                                (ProposalCongDistrictBean)vecCongDistricts.get(count);
                        if(proposalCongDistrictBean.getAcType() != null){
                            proposalCongDistrictBean.setProposalNumber(proposalSiteBean.getProposalNumber());
                            proposalCongDistrictBean.setSiteNumber(proposalSiteBean.getSiteNumber());
                            vecProcReqParameter.add(addDeletePropSiteCongDistrict(proposalCongDistrictBean));
                        }
                    }
                }
            }
        }
        return vecProcReqParameter;
    }
    //Added for case 2406 - Organization and Location - end
    /*Added for Case# COEUSQA-1675-ability to delete Proposal Development proposals - Start*/
    /**
     * This method is used for deleting the proposal
     * @param  String proposalNumber
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return int success
     */
    public int deletePendingProposal(String proposalNumber) throws CoeusException,DBException{
        Vector param = new Vector();
        Vector result = new Vector();
        dbEngine = new DBEngineImpl();
        HashMap nextNumRow = null;
        int success = -1 ;
        try {
            param.add(new Parameter("AV_PROPOSAL_NUMBER",
                    DBEngineConstants.TYPE_STRING, proposalNumber ));
            param.add(new Parameter("AV_UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId ));

            if(dbEngine!=null){
                result = dbEngine.executeFunctions("Coeus",
                        "{<<OUT INTEGER SUCCESS>> = call FN_DELETE_EPS_PROPOSAL( "
                        + " << AV_PROPOSAL_NUMBER >> , <<AV_UPDATE_USER>> )}", param) ;
            }else{
                throw new CoeusException("db_exceptionCode.1000") ;
            }
        } catch (DBException ex) {
            ex.printStackTrace();
        }
        if(!result.isEmpty()) {
            nextNumRow = (HashMap)result.elementAt(0);
            success = Integer.parseInt(nextNumRow.get("SUCCESS").toString());
        }
        return success;
    }
    /*Added for Case# COEUSQA-1675-ability to delete Proposal Development proposals - End*/
    // COEUSQA-2321: Copy Questionnaires for Proposal Development records - Start
    private  ProcReqParameter copyProposalQuestionnaire(String sourceProposalNumber, String targetProposalNumber, String unitNumber, String userId) {

        ProcReqParameter procPropCopy  = new ProcReqParameter();
        try{
            Vector param= new Vector();
            param.add(new Parameter("AS_MODULE_CODE",
                    DBEngineConstants.TYPE_INT, 3));
            param.add(new Parameter("AS_SOURCE_MODULE_ITEM_KEY",
                    DBEngineConstants.TYPE_STRING,sourceProposalNumber));
            param.add(new Parameter("AS_MODULE_SUB_ITEM_CODE",
                    DBEngineConstants.TYPE_INT,0));
            param.add(new Parameter("AS_SOURCE_MODULE_SUB_ITEM_KEY",
                    DBEngineConstants.TYPE_STRING,"0"));
            param.add(new Parameter("AS_TARGET_MODULE_ITEM_KEY",
                    DBEngineConstants.TYPE_STRING, targetProposalNumber ));
            param.add(new Parameter("AS_TARGET_MODULE_SUB_ITEM_KEY",
                    DBEngineConstants.TYPE_STRING, "0" ));
            param.add(new Parameter("AS_UPDATE_USER",
                    DBEngineConstants.TYPE_STRING,userId));

            StringBuffer sqlCommand = new StringBuffer(
                    "{ <<OUT INTEGER COPYFLAG>> = call FN_COPY_MODULE_QNR_ANSWERS(");
            sqlCommand.append(" <<AS_MODULE_CODE>> , ");
            sqlCommand.append(" <<AS_SOURCE_MODULE_ITEM_KEY>> , ");
            sqlCommand.append(" <<AS_MODULE_SUB_ITEM_CODE>> , ");
            sqlCommand.append(" <<AS_SOURCE_MODULE_SUB_ITEM_KEY>> , ");
            sqlCommand.append(" <<AS_TARGET_MODULE_ITEM_KEY>> , ");
            sqlCommand.append(" <<AS_TARGET_MODULE_SUB_ITEM_KEY>> , ");
            sqlCommand.append(" <<AS_UPDATE_USER>> )}");

            procPropCopy.setDSN(DSN);
            procPropCopy.setParameterInfo(param);
            procPropCopy.setSqlCommand(sqlCommand.toString());
        } catch(Exception e) {
            UtilFactory.log(e.getMessage());
        }
        return procPropCopy;

    }
    // COEUSQA-2321: Copy Questionnaires for Proposal Development records - End





     /**
     * Procedures for Stub protocol Creation Functions
      * These Stub functions used for creating protocol from Proposal..
     * * Created Shibu
     */
/**
 *  * *  * THIS METHOD FOR ProtocolInvestigator SAVING......(STREAM PROTOCOL).
 * @param protocolInvestigatorsBean
 * @return
 * @throws DBException
 */
  public ProcReqParameter addStubProtocolInvestigator(ProposalInvestigatorFormBean
            protocolInvestigatorsBean)  throws DBException{
        Vector paramInvestigator = new Vector();

        paramInvestigator.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorsBean.getProposalNumber()));
        paramInvestigator.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT, 1));
        paramInvestigator.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorsBean.getPersonId()));
        paramInvestigator.addElement(new Parameter("PERSON_NAME",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorsBean.getPersonName()));
        paramInvestigator.addElement(new Parameter("NON_EMPLOYEE_FLAG",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorsBean.isNonMITPersonFlag()? "Y": "N") );
        paramInvestigator.addElement(new Parameter("PRINCIPAL_INVESTIGATOR_FLAG",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorsBean.isPrincipleInvestigatorFlag()? "Y": "N") );

        /*paramInvestigator.addElement(new Parameter("FACULTY_FLAG",
        DBEngineConstants.TYPE_STRING,
        protocolInvestigatorsBean.isFacultyFlag()? "Y": "N") );*/

        paramInvestigator.addElement(new Parameter("AFFILIATION_TYPE_CODE",
                DBEngineConstants.TYPE_INT,1));
        paramInvestigator.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramInvestigator.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
        paramInvestigator.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorsBean.getProposalNumber()));
        paramInvestigator.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,1));
        paramInvestigator.addElement(new Parameter("AW_PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorsBean.getPersonId()));
        paramInvestigator.addElement(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramInvestigator.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                protocolInvestigatorsBean.getUpdateTimestamp()));
        paramInvestigator.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorsBean.getAcType()));

        StringBuffer sqlInvestigators = new StringBuffer(
                "call upd_proto_investigator(");
        sqlInvestigators.append(" <<PROTOCOL_NUMBER>> , ");
        sqlInvestigators.append(" <<SEQUENCE_NUMBER>> , ");
        sqlInvestigators.append(" <<PERSON_ID>> , ");
        sqlInvestigators.append(" <<PERSON_NAME>> , ");
        sqlInvestigators.append(" <<NON_EMPLOYEE_FLAG>> , ");
        sqlInvestigators.append(" <<PRINCIPAL_INVESTIGATOR_FLAG>> , ");

        //sqlInvestigators.append(" <<FACULTY_FLAG>> , ");

        sqlInvestigators.append(" <<AFFILIATION_TYPE_CODE>> , ");
        sqlInvestigators.append(" <<UPDATE_USER>> , ");
        sqlInvestigators.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlInvestigators.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlInvestigators.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlInvestigators.append(" <<AW_PERSON_ID>> , ");
        sqlInvestigators.append(" <<AW_UPDATE_USER>> , ");
        sqlInvestigators.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlInvestigators.append(" <<AC_TYPE>> )");

        ProcReqParameter procInvestigator  = new ProcReqParameter();
        procInvestigator.setDSN(DSN);
        procInvestigator.setParameterInfo(paramInvestigator);
        procInvestigator.setSqlCommand(sqlInvestigators.toString());

        return procInvestigator;
    }

    /**
     * *  * THIS METHOD FOR ProtocolInvestigatorUnits SAVING......(STREAM PROTOCOL).
     * InvestigatorUnits.
    * Created Shb
     */
  String sUnitNumber="";
    public ProcReqParameter addStubProtocolInvestigatorUnits( ProposalLeadUnitFormBean protocolInvestigatorUnitsBean)
            throws DBException{
        Vector paramInvestigatorUnit = new Vector();

        paramInvestigatorUnit.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorUnitsBean.getProposalNumber()));
        paramInvestigatorUnit.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,1));
        paramInvestigatorUnit.addElement(new Parameter("UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorUnitsBean.getUnitNumber()));
        paramInvestigatorUnit.addElement(new Parameter("LEAD_UNIT_FLAG",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorUnitsBean.isLeadUnitFlag()? "Y": "N") );

        if(protocolInvestigatorUnitsBean.isLeadUnitFlag()){
            sUnitNumber=protocolInvestigatorUnitsBean.getUnitNumber();
        }
        paramInvestigatorUnit.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorUnitsBean.getPersonId()));
        paramInvestigatorUnit.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramInvestigatorUnit.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
        paramInvestigatorUnit.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorUnitsBean.getProposalNumber()));
        paramInvestigatorUnit.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,1));
        paramInvestigatorUnit.addElement(new Parameter("AW_UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorUnitsBean.getUnitNumber()));
        paramInvestigatorUnit.addElement(new Parameter("AW_PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorUnitsBean.getPersonId()));
        paramInvestigatorUnit.addElement(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramInvestigatorUnit.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                protocolInvestigatorUnitsBean.getUpdateTimestamp()));
        paramInvestigatorUnit.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                protocolInvestigatorUnitsBean.getAcType()));

        StringBuffer sqlInvestigatorsUnit = new StringBuffer(
                "call upd_proto_investigator_unit(");
        sqlInvestigatorsUnit.append(" <<PROTOCOL_NUMBER>> , ");
        sqlInvestigatorsUnit.append(" <<SEQUENCE_NUMBER>> , ");
        sqlInvestigatorsUnit.append(" <<UNIT_NUMBER>> , ");
        sqlInvestigatorsUnit.append(" <<LEAD_UNIT_FLAG>> , ");
        sqlInvestigatorsUnit.append(" <<PERSON_ID>> , ");
        sqlInvestigatorsUnit.append(" <<UPDATE_USER>> , ");
        sqlInvestigatorsUnit.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlInvestigatorsUnit.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlInvestigatorsUnit.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlInvestigatorsUnit.append(" <<AW_UNIT_NUMBER>> , ");
        sqlInvestigatorsUnit.append(" <<AW_PERSON_ID>> , ");
        sqlInvestigatorsUnit.append(" <<AW_UPDATE_USER>> , ");
        sqlInvestigatorsUnit.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlInvestigatorsUnit.append(" <<AC_TYPE>> )");

        ProcReqParameter procInvestigatorUnit  = new ProcReqParameter();
        procInvestigatorUnit.setDSN(DSN);
        procInvestigatorUnit.setParameterInfo(paramInvestigatorUnit);
        procInvestigatorUnit.setSqlCommand(sqlInvestigatorsUnit.toString());

        return procInvestigatorUnit;
    }

    /**
     * *  * THIS METHOD FOR SpecialReview SAVING......(STREAM PROTOCOL).
     * @param protocolSpecialReviewFormBean
     * @return
     * @throws DBException
     */


      public ProcReqParameter addStubProtocolSpecialReview(SpecialReviewFormBean protocolSpecialReviewFormBean,String protNumber)  throws DBException{
        Vector paramSpecialReview = new Vector();

        paramSpecialReview.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING, protNumber));
        paramSpecialReview.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,""+1));
        paramSpecialReview.addElement(new Parameter("SPECIAL_REVIEW_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolSpecialReviewFormBean.getSpecialReviewNumber()));
        paramSpecialReview.addElement(new Parameter("SPECIAL_REVIEW_CODE",
                DBEngineConstants.TYPE_INT,
                ""+protocolSpecialReviewFormBean.getSpecialReviewCode()));
        paramSpecialReview.addElement(new Parameter("APPROVAL_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+protocolSpecialReviewFormBean.getApprovalCode()));
        paramSpecialReview.addElement(new Parameter("SP_REV_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolSpecialReviewFormBean.getProtocolSPRevNumber()));
        paramSpecialReview.addElement(new Parameter("APPLICATION_DATE", DBEngineConstants.TYPE_DATE,
                protocolSpecialReviewFormBean.getApplicationDate()));
        paramSpecialReview.addElement(new Parameter("APPROVAL_DATE",
                DBEngineConstants.TYPE_DATE,
                protocolSpecialReviewFormBean.getApprovalDate()));
        paramSpecialReview.addElement(new Parameter("COMMENTS",
                DBEngineConstants.TYPE_STRING,
                protocolSpecialReviewFormBean.getComments()));
        paramSpecialReview.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramSpecialReview.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
        paramSpecialReview.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
               protNumber));
        paramSpecialReview.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolSpecialReviewFormBean.getSequenceNumber()));
        paramSpecialReview.addElement(new Parameter("AW_SPECIAL_REVIEW_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolSpecialReviewFormBean.getSpecialReviewNumber()));
        paramSpecialReview.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                protocolSpecialReviewFormBean.getUpdateTimestamp()));
        paramSpecialReview.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                protocolSpecialReviewFormBean.getAcType()));

        StringBuffer sqlSpecialReview = new StringBuffer(
                "call update_protocol_sprev(");
        sqlSpecialReview.append(" <<PROTOCOL_NUMBER>> , ");
        sqlSpecialReview.append(" <<SEQUENCE_NUMBER>> , ");
        sqlSpecialReview.append(" <<SPECIAL_REVIEW_NUMBER>> , ");
        sqlSpecialReview.append(" <<SPECIAL_REVIEW_CODE>> , ");
        sqlSpecialReview.append(" <<APPROVAL_TYPE_CODE>> , ");
        sqlSpecialReview.append(" <<SP_REV_PROTOCOL_NUMBER>> , ");
        sqlSpecialReview.append(" <<APPLICATION_DATE>> , ");
        sqlSpecialReview.append(" <<APPROVAL_DATE>> , ");
        sqlSpecialReview.append(" <<COMMENTS>> , ");
        sqlSpecialReview.append(" <<UPDATE_USER>> , ");
        sqlSpecialReview.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlSpecialReview.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlSpecialReview.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlSpecialReview.append(" <<AW_SPECIAL_REVIEW_NUMBER>> , ");
        sqlSpecialReview.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlSpecialReview.append(" <<AC_TYPE>> )");

        ProcReqParameter procSpecialReview  = new ProcReqParameter();
        procSpecialReview.setDSN(DSN);
        procSpecialReview.setParameterInfo(paramSpecialReview);
        procSpecialReview.setSqlCommand(sqlSpecialReview.toString());

        return procSpecialReview;

    }

/**
 *  * THIS METHOD FOR SpecialReviewProtocoll SAVING......(STREAM PROTOCOL).
 * @param protocolSpecialReviewFormBean
 * @param protNumber
 * @return
 * @throws DBException
 */
      public ProcReqParameter addStubProtocolSpecialReviewProp(ProposalSpecialReviewFormBean protocolSpecialReviewFormBean,String protNumber)  throws DBException{
        Vector paramSpecialReview = new Vector();

        paramSpecialReview.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING, protNumber));
        paramSpecialReview.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,""+1));
        paramSpecialReview.addElement(new Parameter("SPECIAL_REVIEW_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolSpecialReviewFormBean.getSpecialReviewNumber()));
        paramSpecialReview.addElement(new Parameter("SPECIAL_REVIEW_CODE",
                DBEngineConstants.TYPE_INT,
                ""+protocolSpecialReviewFormBean.getSpecialReviewCode()));
        paramSpecialReview.addElement(new Parameter("APPROVAL_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+protocolSpecialReviewFormBean.getApprovalCode()));
        paramSpecialReview.addElement(new Parameter("SP_REV_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolSpecialReviewFormBean.getProtocolSPRevNumber()));
        paramSpecialReview.addElement(new Parameter("APPLICATION_DATE", DBEngineConstants.TYPE_DATE,
                protocolSpecialReviewFormBean.getApplicationDate()));
        paramSpecialReview.addElement(new Parameter("APPROVAL_DATE",
                DBEngineConstants.TYPE_DATE,
                protocolSpecialReviewFormBean.getApprovalDate()));
        paramSpecialReview.addElement(new Parameter("COMMENTS",
                DBEngineConstants.TYPE_STRING,
                protocolSpecialReviewFormBean.getComments()));
        paramSpecialReview.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramSpecialReview.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
        paramSpecialReview.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
               protNumber));
        paramSpecialReview.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolSpecialReviewFormBean.getSequenceNumber()));
        paramSpecialReview.addElement(new Parameter("AW_SPECIAL_REVIEW_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolSpecialReviewFormBean.getSpecialReviewNumber()));
        paramSpecialReview.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                protocolSpecialReviewFormBean.getUpdateTimestamp()));
        paramSpecialReview.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                protocolSpecialReviewFormBean.getAcType()));

        StringBuffer sqlSpecialReview = new StringBuffer(
                "call update_protocol_sprev(");
        sqlSpecialReview.append(" <<PROTOCOL_NUMBER>> , ");
        sqlSpecialReview.append(" <<SEQUENCE_NUMBER>> , ");
        sqlSpecialReview.append(" <<SPECIAL_REVIEW_NUMBER>> , ");
        sqlSpecialReview.append(" <<SPECIAL_REVIEW_CODE>> , ");
        sqlSpecialReview.append(" <<APPROVAL_TYPE_CODE>> , ");
        sqlSpecialReview.append(" <<SP_REV_PROTOCOL_NUMBER>> , ");
        sqlSpecialReview.append(" <<APPLICATION_DATE>> , ");
        sqlSpecialReview.append(" <<APPROVAL_DATE>> , ");
        sqlSpecialReview.append(" <<COMMENTS>> , ");
        sqlSpecialReview.append(" <<UPDATE_USER>> , ");
        sqlSpecialReview.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlSpecialReview.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlSpecialReview.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlSpecialReview.append(" <<AW_SPECIAL_REVIEW_NUMBER>> , ");
        sqlSpecialReview.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlSpecialReview.append(" <<AC_TYPE>> )");

        ProcReqParameter procSpecialReview  = new ProcReqParameter();
        procSpecialReview.setDSN(DSN);
        procSpecialReview.setParameterInfo(paramSpecialReview);
        procSpecialReview.setSqlCommand(sqlSpecialReview.toString());

        return procSpecialReview;

    }
      /**
       * * THIS METHOD FOR DefaultProtocolSpecialReview SAVING......(STREAM PROTOCOL).
       * @param protocolSpecialReviewFormBean
       * @return
       * @throws DBException
       */
      public ProcReqParameter addStubDefaultProtocolSpecialReview(ProtocolSpecialReviewFormBean protocolSpecialReviewFormBean)  throws DBException{
        Vector paramSpecialReview = new Vector();

        paramSpecialReview.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolSpecialReviewFormBean.getProtocolNumber()));
        paramSpecialReview.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolSpecialReviewFormBean.getSequenceNumber()));
        paramSpecialReview.addElement(new Parameter("SPECIAL_REVIEW_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolSpecialReviewFormBean.getSpecialReviewNumber()));
        paramSpecialReview.addElement(new Parameter("SPECIAL_REVIEW_CODE",
                DBEngineConstants.TYPE_INT,
                ""+protocolSpecialReviewFormBean.getSpecialReviewCode()));
        paramSpecialReview.addElement(new Parameter("APPROVAL_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+protocolSpecialReviewFormBean.getApprovalCode()));
        paramSpecialReview.addElement(new Parameter("SP_REV_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolSpecialReviewFormBean.getProtocolSPRevNumber()));
        paramSpecialReview.addElement(new Parameter("APPLICATION_DATE",
                DBEngineConstants.TYPE_DATE,
                protocolSpecialReviewFormBean.getApplicationDate()));
        paramSpecialReview.addElement(new Parameter("APPROVAL_DATE",
                DBEngineConstants.TYPE_DATE,
                protocolSpecialReviewFormBean.getApprovalDate()));
        paramSpecialReview.addElement(new Parameter("COMMENTS",
                DBEngineConstants.TYPE_STRING,
                protocolSpecialReviewFormBean.getComments()));
        paramSpecialReview.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramSpecialReview.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
        paramSpecialReview.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolSpecialReviewFormBean.getProtocolNumber()));
        paramSpecialReview.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolSpecialReviewFormBean.getSequenceNumber()));
        paramSpecialReview.addElement(new Parameter("AW_SPECIAL_REVIEW_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolSpecialReviewFormBean.getSpecialReviewNumber()));
        paramSpecialReview.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                protocolSpecialReviewFormBean.getUpdateTimestamp()));
        paramSpecialReview.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                protocolSpecialReviewFormBean.getAcType()));

        StringBuffer sqlSpecialReview = new StringBuffer(
                "call update_protocol_sprev(");
        sqlSpecialReview.append(" <<PROTOCOL_NUMBER>> , ");
        sqlSpecialReview.append(" <<SEQUENCE_NUMBER>> , ");
        sqlSpecialReview.append(" <<SPECIAL_REVIEW_NUMBER>> , ");
        sqlSpecialReview.append(" <<SPECIAL_REVIEW_CODE>> , ");
        sqlSpecialReview.append(" <<APPROVAL_TYPE_CODE>> , ");
        sqlSpecialReview.append(" <<SP_REV_PROTOCOL_NUMBER>> , ");
        sqlSpecialReview.append(" <<APPLICATION_DATE>> , ");
        sqlSpecialReview.append(" <<APPROVAL_DATE>> , ");
        sqlSpecialReview.append(" <<COMMENTS>> , ");
        sqlSpecialReview.append(" <<UPDATE_USER>> , ");
        sqlSpecialReview.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlSpecialReview.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlSpecialReview.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlSpecialReview.append(" <<AW_SPECIAL_REVIEW_NUMBER>> , ");
        sqlSpecialReview.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlSpecialReview.append(" <<AC_TYPE>> )");

        ProcReqParameter procSpecialReview  = new ProcReqParameter();
        procSpecialReview.setDSN(DSN);
        procSpecialReview.setParameterInfo(paramSpecialReview);
        procSpecialReview.setSqlCommand(sqlSpecialReview.toString());

        return procSpecialReview;

    }
/**
 * THIS METHOD FOR GeneralInfo SAVING......(STREAM PROTOCOL).
 * @param protocolInfoBean
 * @return
 * @throws DBException
 */
 public ProcReqParameter addStubProtocolGeneralInfo( ProtocolInfoBean protocolInfoBean)  throws DBException{

         Vector paramInfo = new Vector();
            paramInfo.addElement(new Parameter("PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getProtocolNumber()));
            paramInfo.addElement(new Parameter("SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT, protocolInfoBean.getSequenceNumber()));
            paramInfo.addElement(new Parameter("PROTOCOL_TYPE_CODE",
                    DBEngineConstants.TYPE_INT,
                    ""+protocolInfoBean.getProtocolTypeCode()));
            paramInfo.addElement(new Parameter("PROTOCOL_STATUS_CODE",
                    DBEngineConstants.TYPE_INT,
                    ""+protocolInfoBean.getProtocolStatusCode()));
            paramInfo.addElement(new Parameter("TITLE",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getTitle()));
            paramInfo.addElement(new Parameter("DESCRIPTION",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getDescription()));
            paramInfo.addElement(new Parameter("SPECIAL_REVIEW_INDICATOR",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getSpecialReviewIndicator()));
            paramInfo.addElement(new Parameter("VUL_SUBJECT_INDICATOR",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getVulnerableSubjectIndicator()));
            paramInfo.addElement(new Parameter("APPLICATION_DATE",
                    DBEngineConstants.TYPE_DATE,
                    protocolInfoBean.getApplicationDate()));
            paramInfo.addElement(new Parameter("APPROVAL_DATE",
                    DBEngineConstants.TYPE_DATE,
                    protocolInfoBean.getApprovalDate()));
            paramInfo.addElement(new Parameter("EXPIRATION_DATE",
                    DBEngineConstants.TYPE_DATE,
                    protocolInfoBean.getExpirationDate()));
            paramInfo.addElement(new Parameter("FDA_APPLICATION_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getFDAApplicationNumber()));
            paramInfo.addElement(new Parameter("IS_BILLABLE",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.isBillableFlag()? "Y": "N") );
            paramInfo.addElement(new Parameter("REFERENCE_NUMBER_1",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getRefNum_1()));
            paramInfo.addElement(new Parameter("REFERENCE_NUMBER_2",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getRefNum_2()));
            paramInfo.addElement(new Parameter("KEY_STUDY_PERSON_INDICATOR",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getKeyStudyIndicator()));
            paramInfo.addElement(new Parameter("FUNDING_SOURCE_INDICATOR",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getFundingSourceIndicator()));
            paramInfo.addElement(new Parameter("CORRESPONDENT_INDICATOR",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getCorrespondenceIndicator()));
            paramInfo.addElement(new Parameter("REFERENCE_INDICATOR",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getReferenceIndicator()));
            if(protocolInfoBean.getProjectsIndicator()==null){
                protocolInfoBean.setProjectsIndicator("NO");
            }
            paramInfo.addElement(new Parameter("RELATED_PROJECTS_INDICATOR",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getProjectsIndicator()));
            paramInfo.addElement(new Parameter("CREATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
            paramInfo.addElement(new Parameter("CREATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,
                    protocolInfoBean.getCreateTimestamp()));
            paramInfo.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
            paramInfo.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,
                    dbTimestamp));
            // Added for case # 3090 - start
            paramInfo.addElement(new Parameter("LAST_APPROVAL_DATE",
                    DBEngineConstants.TYPE_DATE,
                    null));
            // Added for case # 3090 - end
            paramInfo.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getProtocolNumber()));
            paramInfo.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT, protocolInfoBean.getSequenceNumber()));
            paramInfo.addElement(new Parameter("AW_UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
            paramInfo.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
            paramInfo.addElement(new Parameter("AC_TYPE",
                    DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getAcType()));

            StringBuffer sqlInfo = new StringBuffer(
                    "call upd_protocol(");
            sqlInfo.append(" <<PROTOCOL_NUMBER>> , ");
            sqlInfo.append(" <<SEQUENCE_NUMBER>> , ");
            sqlInfo.append(" <<PROTOCOL_TYPE_CODE>> , ");
            sqlInfo.append(" <<PROTOCOL_STATUS_CODE>> , ");
            sqlInfo.append(" <<TITLE>> , ");
            sqlInfo.append(" <<DESCRIPTION>> , ");
            sqlInfo.append(" <<SPECIAL_REVIEW_INDICATOR>> , ");
            sqlInfo.append(" <<VUL_SUBJECT_INDICATOR>> , ");
            sqlInfo.append(" <<APPLICATION_DATE>> , ");
            sqlInfo.append(" <<APPROVAL_DATE>> , ");
            sqlInfo.append(" <<EXPIRATION_DATE>> , ");
            sqlInfo.append(" <<FDA_APPLICATION_NUMBER>> , ");
            sqlInfo.append(" <<IS_BILLABLE>> , ");
            sqlInfo.append(" <<REFERENCE_NUMBER_1>> , ");
            sqlInfo.append(" <<REFERENCE_NUMBER_2>> , ");
            sqlInfo.append(" <<KEY_STUDY_PERSON_INDICATOR>> , ");
            sqlInfo.append(" <<FUNDING_SOURCE_INDICATOR>> , ");
            sqlInfo.append(" <<CORRESPONDENT_INDICATOR>> , ");
            sqlInfo.append(" <<REFERENCE_INDICATOR>> , ");
            sqlInfo.append(" <<RELATED_PROJECTS_INDICATOR>> , ");

            sqlInfo.append(" <<CREATE_USER>> , ");
            sqlInfo.append(" <<CREATE_TIMESTAMP>> , ");
            sqlInfo.append(" <<UPDATE_USER>> , ");
            sqlInfo.append(" <<UPDATE_TIMESTAMP>> , ");
             // Added for case # 3090 - start
            sqlInfo.append(" <<LAST_APPROVAL_DATE>> , ");
            // Added for case # 3090 - end
            sqlInfo.append(" <<AW_PROTOCOL_NUMBER>> , ");
            sqlInfo.append(" <<AW_SEQUENCE_NUMBER>> , ");
            sqlInfo.append(" <<AW_UPDATE_USER>> , ");
            sqlInfo.append(" <<AW_UPDATE_TIMESTAMP>> , ");
            sqlInfo.append(" <<AC_TYPE>> )");

            ProcReqParameter procInfo  = new ProcReqParameter();
            procInfo.setDSN(DSN);
            procInfo.setParameterInfo(paramInfo);
            procInfo.setSqlCommand(sqlInfo.toString());

            return procInfo;


    }
 /**
  * For Saving Protocol Roles...
  * @param protocolRolesFormBean
  * @return
  * @throws CoeusException
  * @throws DBException
  */
 public ProcReqParameter addStubProtocolRoles(ProposalRolesFormBean protocolRolesFormBean)
    throws CoeusException , DBException{
        Vector paramRoles = new Vector();
//        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = new CoeusFunctions().getDBTimestamp();

        paramRoles.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolRolesFormBean.getProposalNumber()));
        paramRoles.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+1));
        paramRoles.addElement(new Parameter("ROLE_ID",
                DBEngineConstants.TYPE_INT,
                ""+protocolRolesFormBean.getRoleId()));
        paramRoles.addElement(new Parameter("USER_ID",
                DBEngineConstants.TYPE_STRING,
                protocolRolesFormBean.getUserId()));
        paramRoles.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramRoles.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramRoles.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolRolesFormBean.getProposalNumber()));
        paramRoles.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+1));
        paramRoles.addElement(new Parameter("AW_ROLE_ID",
                DBEngineConstants.TYPE_INT,
                ""+protocolRolesFormBean.getRoleId()));
        paramRoles.addElement(new Parameter("AW_USER_ID",
                DBEngineConstants.TYPE_STRING,
                protocolRolesFormBean.getUserId()));
        paramRoles.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                protocolRolesFormBean.getUpdateTimestamp()));
        paramRoles.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                protocolRolesFormBean.getAcType()));

        StringBuffer sqlRoles = new StringBuffer(
                "call update_protocol_user_roles(");
        sqlRoles.append(" <<PROTOCOL_NUMBER>> , ");
        sqlRoles.append(" <<SEQUENCE_NUMBER>> , ");
        sqlRoles.append(" <<USER_ID>> , ");
        sqlRoles.append(" <<ROLE_ID>> , ");
        sqlRoles.append(" <<UPDATE_USER>> , ");
        sqlRoles.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlRoles.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlRoles.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlRoles.append(" <<AW_USER_ID>> , ");
        sqlRoles.append(" <<AW_ROLE_ID>> , ");
        sqlRoles.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlRoles.append(" <<AC_TYPE>> )");

        ProcReqParameter procRoles  = new ProcReqParameter();
        procRoles.setDSN(DSN);
        procRoles.setParameterInfo(paramRoles);
        procRoles.setSqlCommand(sqlRoles.toString());

        return procRoles;
    }

/**
 *
 * @return
 * @throws DBException
 */
     public String getNextStubProtocolNumber() throws  DBException{
        String protocolNumber = null;
        Vector param= new Vector();
        HashMap nextNumRow = null;
        Vector result = new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeFunctions(DSN,
                    "{ <<OUT INTEGER SEQNUMBER>> = call fn_generate_protocol_number()}",
                    param);
        }else{
            throw new DBException("DB instance is not available");
        }
        if(!result.isEmpty()){
            nextNumRow = (HashMap)result.elementAt(0);
            protocolNumber = nextNumRow.get("SEQNUMBER").toString();
        }
        return protocolNumber;
    }
     /**
      *
      * @param protocolLocationListBean
      * @return
      * @throws DBException
      */
public ProcReqParameter addStubProtocolLocations( ProposalSiteBean protocolLocationListBean)  throws DBException{
        Vector paramLocations = new Vector();

        paramLocations.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolLocationListBean.getProposalNumber()));
        paramLocations.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,protocolLocationListBean.getSiteNumber()));
        paramLocations.addElement(new Parameter("ORGANIZATION_ID",
                DBEngineConstants.TYPE_STRING,
                protocolLocationListBean.getOrganizationId()));

        String rolodexId = null;
        if(protocolLocationListBean.getRolodexDetailsBean() != null){
            rolodexId = protocolLocationListBean.getRolodexDetailsBean().getRolodexId();
        }
        if(rolodexId == null || rolodexId.trim().length() == 0){
            rolodexId = "0";
        }
        paramLocations.addElement(new Parameter("ROLODEX_ID",
                DBEngineConstants.TYPE_STRING,rolodexId ));
        paramLocations.addElement(new Parameter("ORGANIZATION_TYPE_ID",
                DBEngineConstants.TYPE_INT,protocolLocationListBean.getLocationTypeCode()));
        paramLocations.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING,userId));
        paramLocations.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
        paramLocations.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolLocationListBean.getProposalNumber()));
        paramLocations.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT, 1));
        paramLocations.addElement(new Parameter("AW_ORGANIZATION_ID",
                DBEngineConstants.TYPE_STRING,protocolLocationListBean.getOrganizationId()));
        paramLocations.addElement(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramLocations.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                protocolLocationListBean.getUpdateTimestamp()));
        paramLocations.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                protocolLocationListBean.getAcType()));

        StringBuffer sqlLocations = new StringBuffer(
                "call upd_proto_location(");
        sqlLocations.append(" <<PROTOCOL_NUMBER>> , ");
        sqlLocations.append(" <<SEQUENCE_NUMBER>> , ");
        sqlLocations.append(" <<ORGANIZATION_ID>> , ");
        sqlLocations.append(" <<ROLODEX_ID>> , ");
        sqlLocations.append(" <<ORGANIZATION_TYPE_ID>> , ");
        sqlLocations.append(" <<UPDATE_USER>> , ");
        sqlLocations.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlLocations.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlLocations.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlLocations.append(" <<AW_ORGANIZATION_ID>> , ");
        sqlLocations.append(" <<AW_UPDATE_USER>> , ");
        sqlLocations.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlLocations.append(" <<AC_TYPE>> )");

        ProcReqParameter procLocations  = new ProcReqParameter();
        procLocations.setDSN(DSN);
        procLocations.setParameterInfo(paramLocations);
        procLocations.setSqlCommand(sqlLocations.toString());

        return procLocations;
    }
/**
 *
 * @param protocolBean
 * @param functionType
 * @param userId
 * @return
 * @throws CoeusException
 * @throws DBException
 */
 public Vector addStubProtocolInfo(ProtocolInfoBean protocolBean,char functionType,String userId) throws CoeusException,DBException{
        this.functionType = functionType;
          Vector retVector=addStubProtocolInfo(protocolBean);
        String rowId = rowLockStr;

      //  if( success ) {
            String protocolNo = protocolBean.getProtocolNumber();
//            if( functionType == SAVE_PROP_TO_PROT   ){
//                if( protocolNo.length() > 10 ) {
//                    rowId +=protocolNo.substring(0,11);
//                }
//
//            }else if( functionType == 'U' ) {
//                rowId+= protocolNo;
//            }
//            // TransMon.releaseEdit(rowId,userId);
//            // Calling releaseLock method for bug fixing
//            boolean lockCheck = transMon.lockAvailabilityCheck(rowId,userId);
//            if(!lockCheck){
//                LockingBean lockingBean = transMon.releaseLock(rowId,userId);
//            }
      // }
        return retVector;
    }


/**
 *
 * @param protocolInfoBean
 * @return
 * @throws CoeusException
 * @throws DBException
 */
  public Vector addStubProtocolInfo( ProtocolInfoBean protocolInfoBean)
    throws CoeusException, DBException{


        //prps addStubProtocolInfo - all protocol info coming in the format of proposal beans
         /// //  THIS METHOD FOR General Data SAVING......(STREAM PROTOCOL).
        boolean actionLogging = false ;
        Vector returnVector=new Vector();

        Vector paramInfo= new Vector();
        Vector procedures = new Vector(5,3);
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();

        String protocolNumber="121212";
        boolean success = false;
        //int seqNumber = protocolInfoBean.getSequenceNumber()+1;

        //protocolNumber  = protocolInfoBean.getProtocolNumber();
        protocolNumber  = getNextStubProtocolNumber();
        int seqNumber = protocolInfoBean.getSequenceNumber();
        protocolInfoBean.setProtocolNumber(protocolNumber);
        returnVector.add(protocolNumber);
        if (protocolInfoBean.getAcType() != null){
            //Protocol Enhancement -
            if (protocolInfoBean.getAcType().equals("I")) {
                if(protocolInfoBean.getSequenceNumber()==0){
                    protocolInfoBean.setSequenceNumber(1);
                    seqNumber = 1;
                }
                //Added to log actions only if new Protocol is created - start -shibu -20 mar, 2011
                if(seqNumber!=protocolInfoBean.getSequenceNumber()){
                    actionLogging = false ;
                }else{
                    actionLogging = true ;
                }
                seqNumber = protocolInfoBean.getSequenceNumber();
                protocolInfoBean.setCreateTimestamp(dbTimestamp);
                protocolInfoBean.setUpdateTimestamp(dbTimestamp);
               protocolInfoBean.setUpdateUser(userId);
               protocolInfoBean.setApplicationDate(new java.sql.Date(dbTimestamp.getTime()));

//                protocolInfoBean.setUpdateTimestamp(dbTimestamp);
//                String updatUser=protocolInfoBean.getCreateUser();
//                protocolInfoBean.setUpdateUser(updatUser);

            }
              procedures.add(addStubProtocolGeneralInfo(protocolInfoBean));
        }


        // inserting new Investigators
        /// //  THIS METHOD FOR Investigators SAVING......(STREAM PROTOCOL).
        Vector vecInvestigators = protocolInfoBean.getInvestigators();
        if ((vecInvestigators != null) && (vecInvestigators.size() >0)){
            int length = vecInvestigators.size();
            for(int index=0;index<length;index++){
                ProposalInvestigatorFormBean proposalInvestigatorBean=(ProposalInvestigatorFormBean)vecInvestigators.elementAt(index);
                //ProtocolInvestigatorsBean protocolInvestigatorsBean =(ProtocolInvestigatorsBean)vecInvestigators.elementAt(index);
                 proposalInvestigatorBean.setAcType("I");
                if (proposalInvestigatorBean.getAcType() != null) {
                    //protocolInvestigatorsBean.setUpdateTimestamp(dbTimestamp);
                    proposalInvestigatorBean.setProposalNumber(protocolNumber);
                     //if (!protocolInvestigatorsBean.getAcType().equalsIgnoreCase("D") ) {
                    procedures.add(addStubProtocolInvestigator(proposalInvestigatorBean));
                    //}
                }

/// //  THIS METHOD FOR UNIT SAVING......(STREAM PROTOCOL).
                Vector vecInvestigatorsUnits = proposalInvestigatorBean.getInvestigatorUnits();
                if ((vecInvestigatorsUnits != null) &&
                        (vecInvestigatorsUnits.size() >0)){
                    int unitslength = vecInvestigatorsUnits.size();
                    for(int unitIndex=0;unitIndex<unitslength;unitIndex++){
                         ProposalLeadUnitFormBean  protocolInvestigatorUnitsBean = (ProposalLeadUnitFormBean) vecInvestigatorsUnits.elementAt(unitIndex);

                            protocolInvestigatorUnitsBean.setAcType("I");
                        if (protocolInvestigatorUnitsBean.getAcType() != null) {
                            protocolInvestigatorUnitsBean.setProposalNumber(protocolNumber);
                            procedures.add(addStubProtocolInvestigatorUnits(protocolInvestigatorUnitsBean));

                        }
                    }
                }
            }
        }


        /// //  THIS METHOD FOR ORFUNDING SOURCE  SAVING......(STREAM PROTOCOL).
        Vector vecFundSource = protocolInfoBean.getFundingSources();

        if ((vecFundSource != null) && (vecFundSource.size() >0)){
            for(int index=0;index<vecFundSource.size();index++){
                ProtocolFundingSourceBean protocolFundingSourceBean =
                        (ProtocolFundingSourceBean)vecFundSource.elementAt(index);
                         protocolFundingSourceBean.setAcType("I");
                if (protocolFundingSourceBean.getAcType() != null) {
                    protocolFundingSourceBean.setProtocolNumber(protocolNumber);
                    protocolFundingSourceBean.setSequenceNumber(seqNumber);
                    procedures.add(addStubProtocolFundSource(protocolFundingSourceBean));
                }
            }
        }

/// //  THIS METHOD FOR ORGANISATIONS SAVING......(STREAM PROTOCOL).
        Vector vecLocations = protocolInfoBean.getLocationLists();
        if ((vecLocations != null) && (vecLocations.size() >0)){
            int length = vecLocations.size();
            for(int index=0;index<length;index++){
                ProposalSiteBean protocolLocationListBean = (ProposalSiteBean)vecLocations.elementAt(index);
                protocolLocationListBean.setAcType("I");  // setted for default data
                if(protocolLocationListBean.getAcType() != null) {
                        if (protocolLocationListBean.getAcType().equals("I")) {
                            protocolLocationListBean.setProposalNumber(protocolNumber);
                            protocolLocationListBean.setSiteNumber(1);
                            if(protocolLocationListBean.getOrganizationId() != null){
                                procedures.add(addStubProtocolLocations(protocolLocationListBean));
                            }
                        }

                }
            }
        }




// editted for  organisation location  for private function
    /*     Vector vecSites = protocolInfoBean.getLocationLists();
        if(vecSites != null && vecSites.size() > 0){
            ProposalSiteBean protocolLocationListBean = null;
            boolean generatedSiteNumber = false;
            int siteNumber = 1;
            for(int count = 0; count<vecSites.size(); count++){
                protocolLocationListBean = (ProposalSiteBean)vecSites.get(count);
                 protocolLocationListBean.setProposalNumber(protocolNumber);
                 if(protocolLocationListBean.getAcType()!=null
                        && protocolLocationListBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                    protocolLocationListBean.setProposalNumber(protocolNumber);
                    if(!generatedSiteNumber){
                        ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
                        protocolLocationListBean.setSiteNumber(siteNumber);
                        generatedSiteNumber = true;
                    }else{
                        siteNumber++;
                        protocolLocationListBean.setSiteNumber(siteNumber);
                    }
                     procedures.add(addStubProtocolLocations(protocolLocationListBean));
                }
            }
        }*/

          // inserting new special review

/// //  THIS METHOD FOR SpecialReview SAVING......(STREAM PROTOCOL). old method commented..
    /*
//         ProposalSpecialReviewFormBean proposalSpecialReviewFormBean = new ProposalSpecialReviewFormBean();
//        Vector vSpecialReview = proposalDevelopmentFormBean.getPropSpecialReviewFormBean();
//
//        if ((vSpecialReview != null) && (vSpecialReview.size() >0)){
//            int length = vSpecialReview.size();
//            //Added on 4th July, 2004 - Prasanna
//            ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
//            int intSpecialRevNumber = proposalDevelopmentTxnBean.getNextPropSpecialReviewNumber(proposalNumber);
//            for(int index=0;index<length;index++){
//                proposalSpecialReviewFormBean =(ProposalSpecialReviewFormBean)vSpecialReview.elementAt(index);
//                if (proposalSpecialReviewFormBean.getAcType() != null ) {
//                    //Added on 4th July, 2004 - start
//                    if(proposalSpecialReviewFormBean.getAcType().equalsIgnoreCase("I")){
//                        intSpecialRevNumber = intSpecialRevNumber + 1;
//                        proposalSpecialReviewFormBean.setSpecialReviewNumber(intSpecialRevNumber);
//                    }
//                    //Added on 4th July, 2004 - end
//                    //proposalSpecialReviewFormBean.setProposalNumber( proposalNumber );
//                    procedures.add( addUpdDelPropSpecialReview(proposalSpecialReviewFormBean) );
//                }
//            }
//        }

      */


/// //  THIS METHOD FOR SpecialReview SAVING......(STREAM PROTOCOL). vecSpecialReview

         Vector vecSpecialReview = protocolInfoBean.getSpecialReviews();
        if ((vecSpecialReview != null) && (vecSpecialReview.size() >0)){
            int length = vecSpecialReview.size();
            length=length-1;  // for avoiding last special review(Current Sp review)..
            int spRevNumber=1;
            for(int index=0;index<length;index++,spRevNumber++){

                ProposalSpecialReviewFormBean protocolSpecialReviewFormBean =(ProposalSpecialReviewFormBean)vecSpecialReview.elementAt(index);
                    protocolSpecialReviewFormBean.setAcType("I");
                        if  (protocolSpecialReviewFormBean.getAcType() != null) {
                            protocolSpecialReviewFormBean.setUpdateTimestamp(dbTimestamp);
                            String hh=protocolSpecialReviewFormBean.getProtocolSPRevNumber();
                            Date gg=protocolSpecialReviewFormBean.getApplicationDate();
                            protocolSpecialReviewFormBean.setSequenceNumber(1);
                            protocolSpecialReviewFormBean.setSpecialReviewNumber(spRevNumber);
                            procedures.add(addStubProtocolSpecialReviewProp(protocolSpecialReviewFormBean,protocolNumber));

                }
            }
        }



//  THIS METHOD FOR SpecialReview SAVING......(STREAM PROTOCOL). PROPOSALFORMDATA   comment
   /*
        Vector vecSpecialReview = protocolInfoBean.getSpecialReviews();
        if ((vecSpecialReview != null) && (vecSpecialReview.size() >0)){
            int length = vecSpecialReview.size();
            length=length-1;  // for avoiding last special review(Current Sp review)..
            int spRevNumber=1;
            for(int index=0;index<length;index++,spRevNumber++){

              //  ProposalSpecialReviewFormBean protocolSpecialReviewFormBean =(ProposalSpecialReviewFormBean)vecSpecialReview.elementAt(index);
                       SpecialReviewFormBean protocolSpecialReviewFormBean = (SpecialReviewFormBean)vecSpecialReview.elementAt(index);

                        if  (protocolSpecialReviewFormBean.getAcType() != null) {
                            protocolSpecialReviewFormBean.setUpdateTimestamp(dbTimestamp);
                            String hh=protocolSpecialReviewFormBean.getProtocolSPRevNumber();
                  Date gg=protocolSpecialReviewFormBean.getApplicationDate();
                    protocolSpecialReviewFormBean.setSequenceNumber(1);
                    protocolSpecialReviewFormBean.setSpecialReviewNumber(spRevNumber);


                    procedures.add(addStubProtocolSpecialReview(protocolSpecialReviewFormBean,protocolNumber));

                }
            }
        }

*/

//  THIS METHOD FOR USER ROLES SAVING......(STREAM PROTOCOL).
         ProposalRolesFormBean protocolRolesFormBean = new ProposalRolesFormBean();
        Vector vecUserRoles = protocolInfoBean.getUserRoles();
// Commended for user agregator-----------
      /*  if ((vecUserRoles != null) && (vecUserRoles.size() >0)){
            int length = vecUserRoles.size();
            for(int index=0;index<length;index++){
                protocolRolesFormBean =(ProposalRolesFormBean)vecUserRoles.elementAt(index);
                protocolRolesFormBean.setAcType("I");
                if (protocolRolesFormBean.getAcType() != null ) {
                    protocolRolesFormBean.setProposalNumber( protocolNumber );
                    protocolRolesFormBean.setAcType("I");
                    procedures.add( addStubProtocolRoles(protocolRolesFormBean) );
                }

            }
        }else if (seqNumber==1 && protocolInfoBean.getAcType()!=null && protocolInfoBean.getAcType().equals("I")){
       * */
        // Commend for user agregator   End-

            Vector vUserRoles = new Vector();
            protocolRolesFormBean.setProposalNumber(protocolNumber);
//            protocolRolesFormBean.setSequenceNumber(seqNumber);
            protocolRolesFormBean.setRoleId(PROTOCOL_COORDINATOR_ID);
            protocolRolesFormBean.setUserId(userId);
            protocolRolesFormBean.setAcType("I");
            //protocolRolesFormBean.setUpdateTimestamp(dbTimestamp);
            procedures.add(addStubProtocolRoles(protocolRolesFormBean));
      //  }




        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
            success=true;
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        returnVector.add(success);
        return returnVector;

    }
/**
 *  THIS METHOD FOR addStubProtocolFundSource SAVING......(STREAM PROTOCOL).
 * @param protocolFundingSourceBean
 * @return
 * @throws DBException
 */
  public ProcReqParameter addStubProtocolFundSource(ProtocolFundingSourceBean
            protocolFundingSourceBean)  throws DBException{
        //Added for the Coeus Enhancement case:#1799 star step:1
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        //End Coeus Enhancement case:#1799 step:1
        Vector paramFundSource = new Vector();

        paramFundSource.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolFundingSourceBean.getProtocolNumber()));
        paramFundSource.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolFundingSourceBean.getSequenceNumber()));
        paramFundSource.addElement(new Parameter("FUNDING_SOURCE_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+protocolFundingSourceBean.getFundingSourceTypeCode()));

        paramFundSource.addElement(new Parameter("FUNDING_SOURCE",
                DBEngineConstants.TYPE_STRING,
                protocolFundingSourceBean.getFundingSource()));
        paramFundSource.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramFundSource.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
        paramFundSource.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolFundingSourceBean.getProtocolNumber()));
        paramFundSource.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+protocolFundingSourceBean.getSequenceNumber()));

        paramFundSource.addElement(new Parameter("AW_FUNDING_SOURCE_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+protocolFundingSourceBean.getAwFundingSourceTypeCode()));

        paramFundSource.addElement(new Parameter("AW_FUNDING_SOURCE",
                DBEngineConstants.TYPE_STRING,
                protocolFundingSourceBean.getFundingSource()));
        paramFundSource.addElement(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramFundSource.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                protocolFundingSourceBean.getUpdateTimestamp()));
        paramFundSource.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                protocolFundingSourceBean.getAcType()));

        StringBuffer sqlFundSource = new StringBuffer(
                "call upd_proto_funding_source(");
        sqlFundSource.append(" <<PROTOCOL_NUMBER>> , ");
        sqlFundSource.append(" <<SEQUENCE_NUMBER>> , ");
        sqlFundSource.append(" <<FUNDING_SOURCE_TYPE_CODE>> , ");
        sqlFundSource.append(" <<FUNDING_SOURCE>> , ");
        sqlFundSource.append(" <<UPDATE_USER>> , ");
        sqlFundSource.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlFundSource.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlFundSource.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlFundSource.append(" <<AW_FUNDING_SOURCE_TYPE_CODE>> , ");
        sqlFundSource.append(" <<AW_FUNDING_SOURCE>> , ");
        sqlFundSource.append(" <<AW_UPDATE_USER>> , ");
        sqlFundSource.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlFundSource.append(" <<AC_TYPE>> )");

        ProcReqParameter procReqFund  = new ProcReqParameter();
        procReqFund.setDSN(DSN);
        procReqFund.setParameterInfo(paramFundSource);
        procReqFund.setSqlCommand(sqlFundSource.toString());

        return procReqFund;
    }

 //COEUSQA-2984 : Statuses in special review - start
  /**
   * This method is for updating the status code to the OSP$EPS_PROP_SPECIAL_REVIEW.
   * @param proposalNumber
   * @param specialReviewNumber
   * @param protocolNumber
   * @param protocolStatusCode
   * @return
   * @throws DBException
   */
  public boolean updateProtocolStatus(String proposalNumber, int specialReviewNumber, String protocolNumber, int protocolStatusCode)
  throws CoeusException, DBException {
      boolean success = false;
      Vector param= new Vector();
      Vector result = new Vector();
      param.add(new Parameter("AW_PROPOSAL_NUMBER",
              DBEngineConstants.TYPE_STRING,""+proposalNumber));
      param.add(new Parameter("AW_SPECIAL_REVIEW_NUMBER",
              DBEngineConstants.TYPE_INT,""+specialReviewNumber));
      param.add(new Parameter("AS_PROTOCOL_NUMBER",
              DBEngineConstants.TYPE_STRING,""+protocolNumber));
      param.add(new Parameter("AI_PROTOCOL_STATUS_CODE",
              DBEngineConstants.TYPE_INT,""+protocolStatusCode));
      param.add(new Parameter("as_update_user",
              DBEngineConstants.TYPE_STRING,""+userId));
      if(dbEngine!=null){
          try{
              result = dbEngine.executeFunctions("Coeus",
                      "{ <<OUT INTEGER IS_SUCCESS>> = "
                      +" call FN_UPD_PROP_SPREV_PROTO_STATUS( "
                      +"<< AW_PROPOSAL_NUMBER >>, << AW_SPECIAL_REVIEW_NUMBER >>, << AS_PROTOCOL_NUMBER >>, << AI_PROTOCOL_STATUS_CODE >> , <<as_update_user>> ) }", param);
          }catch (DBException dbEx){
              throw new CoeusException(dbEx.getMessage());
          }
      }else{
          throw new CoeusException("db_exceptionCode.1000");
      }
      return success;
  }
  //COEUSQA-2984 : Statuses in special review - end

  //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
  /**
   * This method is for updating the status code to the OSP$EPS_PROP_SPECIAL_REVIEW.
   * @param proposalNumber
   * @param specialReviewNumber
   * @param protocolNumber
   * @param protocolStatusCode
   * @return
   * @throws DBException
   */
  public boolean updateIacucProtocolStatus(String proposalNumber, int specialReviewNumber, String protocolNumber, int protocolStatusCode)
  throws CoeusException, DBException {
      boolean success = false;
      Vector param= new Vector();
      Vector result = new Vector();
      param.add(new Parameter("AW_PROPOSAL_NUMBER",
              DBEngineConstants.TYPE_STRING,""+proposalNumber));
      param.add(new Parameter("AW_SPECIAL_REVIEW_NUMBER",
              DBEngineConstants.TYPE_INT,""+specialReviewNumber));
      param.add(new Parameter("AS_PROTOCOL_NUMBER",
              DBEngineConstants.TYPE_STRING,""+protocolNumber));
      param.add(new Parameter("AI_PROTOCOL_STATUS_CODE",
              DBEngineConstants.TYPE_INT,""+protocolStatusCode));
      param.add(new Parameter("AS_UPDATE_USER",
              DBEngineConstants.TYPE_STRING,""+userId));
      if(dbEngine!=null){
          try{
              result = dbEngine.executeFunctions("Coeus",
                      "{ <<OUT INTEGER IS_SUCCESS>> = "
                      +" call FN_UPD_PROP_SPRV_ACPROT_STATUS( "
                      +"<< AW_PROPOSAL_NUMBER >>, << AW_SPECIAL_REVIEW_NUMBER >>, << AS_PROTOCOL_NUMBER >>, << AI_PROTOCOL_STATUS_CODE >> , <<AS_UPDATE_USER>> ) }", param);
          }catch (DBException dbEx){
              throw new CoeusException(dbEx.getMessage());
          }
      }else{
          throw new CoeusException("db_exceptionCode.1000");
      }
      return success;
  }

  /**
   * This method is for performing the IACUC link from devlopment proposal.
   * @param cvInstPropData
   * @return
   * @throws DBException
   */
  public void performIacucLinkFromProposalDev(CoeusVector cvInstPropData,String unitNumber)
  throws Exception,DBException{
      Vector vecProcedures = new Vector();
      Vector iacucProtoData = null;
      CoeusVector cvInstPropSpecialRev = (CoeusVector)cvInstPropData.get(0);
      InstituteProposalUpdateTxnBean instituteProposalUpdateTxnBean = new InstituteProposalUpdateTxnBean(userId);
      edu.mit.coeus.iacuc.bean.ProtocolUpdateTxnBean iacucProtoUpdateTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolUpdateTxnBean(userId);
      if(cvInstPropSpecialRev != null){
          for(int index = 0 ; index < cvInstPropSpecialRev.size() ; index ++){
              InstituteProposalSpecialReviewBean instPropBean = (InstituteProposalSpecialReviewBean)cvInstPropSpecialRev.get(index);
              if(instPropBean.getSpecialReviewCode() == Integer.parseInt((String)cvInstPropData.get(1))){
                  edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean iacucProtoDataTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean(userId);
                  //insert the row in protocol funding source table to establich the link
                  if(iacucProtoData == null)
                      iacucProtoData = new Vector();
                  String spRevNum = instPropBean.getProtocolSPRevNumber();
                  CoeusDataTxnBean coeusDataTxnBean = new CoeusDataTxnBean();
                  boolean validProtocolNumber = coeusDataTxnBean.validateIacucProtocolNumber(spRevNum);
                  if(validProtocolNumber){
                      boolean lockCheck = iacucProtoDataTxnBean.lockCheck(spRevNum, userId);
                      if(!lockCheck){
                          //if lock is there then add an 'X' to the protocol number
                          instPropBean.setProtocolSPRevNumber("X"+spRevNum);
                      }else{
                          String indicator = "P1";
                          edu.mit.coeus.iacuc.bean.ProtocolFundingSourceBean iacucProtoFundingSourceBean = null;

                          //first update the inst prop special review
                          instPropBean.setSpecialReviewCode(Integer.parseInt((String)cvInstPropData.get(1)));
                          instPropBean.setApprovalCode(Integer.parseInt((String)cvInstPropData.get(2)));

                          //insert a row to protocol funding source to establish the link
                          edu.mit.coeus.utils.locking.LockingBean lockingBean =  iacucProtoDataTxnBean.getLock(spRevNum, userId, unitNumber);
                          iacucProtoDataTxnBean.transactionCommit();
                          boolean lockAvailable = lockingBean.isGotLock();
                          if(lockAvailable){
                              edu.mit.coeus.iacuc.bean.ProtocolInfoBean protocolInfoBean = (edu.mit.coeus.iacuc.bean.ProtocolInfoBean)iacucProtoDataTxnBean.getProtocolMaintenanceDetails(spRevNum);
                              iacucProtoFundingSourceBean = new edu.mit.coeus.iacuc.bean.ProtocolFundingSourceBean();
                              iacucProtoFundingSourceBean.setProtocolNumber(spRevNum);
                              iacucProtoFundingSourceBean.setSequenceNumber(protocolInfoBean.getSequenceNumber());
                              iacucProtoFundingSourceBean.setFundingSourceTypeCode(5);
                              iacucProtoFundingSourceBean.setFundingSource(instPropBean.getProposalNumber());
                              iacucProtoFundingSourceBean.setAcType("I");
                              // Check if Linking is already there
                              boolean isLinkExists = iacucProtoUpdateTxnBean.isProtocolLinkExists(iacucProtoFundingSourceBean);
                              if(!isLinkExists){
                                  // IACUC link does NOT exist
                                  vecProcedures.add(iacucProtoUpdateTxnBean.addUpdProtocolFundSource(iacucProtoFundingSourceBean));
                                  edu.mit.coeus.iacuc.bean.ProtocolLinkBean protocolLinkBean = getIacucProtoLinkBean(iacucProtoFundingSourceBean,instPropBean);
                                  vecProcedures.add(iacucProtoUpdateTxnBean.addUpdProtocolLinks(protocolLinkBean));
                              }
                          }
                          if(iacucProtoFundingSourceBean != null){
                              int maxEntryNumber = iacucProtoDataTxnBean.getMaxProtocolNotesEntryNumber(iacucProtoFundingSourceBean.getProtocolNumber());
                              maxEntryNumber = maxEntryNumber + 1;
                              edu.mit.coeus.iacuc.bean.ProtocolNotepadBean protocolNotepadBean = getIacucNotepadBean(iacucProtoFundingSourceBean,maxEntryNumber);
                              vecProcedures.add(iacucProtoUpdateTxnBean.addUpdProtocolNotepad(protocolNotepadBean));
                              iacucProtoUpdateTxnBean.updateFundingSourceIndicator(
                                      iacucProtoFundingSourceBean.getProtocolNumber(),indicator,"9");
                              try{
                                  iacucProtoDataTxnBean.sendEmailNotification(ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE,MailActions.SPECIAL_REVIEW_INSERTED,
                                          instPropBean.getProposalNumber(),instPropBean.getSequenceNumber(),iacucProtoFundingSourceBean.getProtocolNumber(), ModuleConstants.IACUC_MODULE_CODE);
                              }catch(Exception ex){
                                  UtilFactory.log("Could not send the mail during the linking process of Protocol "+iacucProtoFundingSourceBean.getProtocolNumber()+
                                          " and institute proposal "+instPropBean.getProposalNumber(),
                                          ex,
                                          "ProposalDevelopmentUpdateTxnBean",
                                          "performProtocolLinkFromProposalDev");
                              }

                              vecProcedures.addAll(iacucProtoUpdateTxnBean.updateInboxTable(iacucProtoFundingSourceBean.getProtocolNumber(),ModuleConstants.IACUC_MODULE_CODE,iacucProtoFundingSourceBean.getAcType()));

                              iacucProtoData.addElement(iacucProtoFundingSourceBean);
                          }
                      }
                      instPropBean.setAcType("U");
                      vecProcedures.add(instituteProposalUpdateTxnBean.addUpdInstituteProposalSpecialReview(instPropBean));
                  }
              }
          }
          if(dbEngine!=null){
              if(!vecProcedures.isEmpty()) dbEngine.executeStoreProcs(vecProcedures);
          }else{
              unLockIacucProtocols(iacucProtoData);
              throw new CoeusException("db_exceptionCode.1000");
          }
          unLockIacucProtocols(iacucProtoData);
      }

  }

  /**
   * This method is for updating the link between Institute proposal and IACUC.
   * @param iacucProtoFundingSourceBean
   * @param instPropSpRevBean
   * @return protocolLinkBean
   * @throws DBException
   */
  public edu.mit.coeus.iacuc.bean.ProtocolLinkBean getIacucProtoLinkBean(edu.mit.coeus.iacuc.bean.ProtocolFundingSourceBean iacucProtoFundingSourceBean,InstituteProposalSpecialReviewBean instPropSpRevBean)
  throws CoeusException,DBException{
      edu.mit.coeus.iacuc.bean.ProtocolLinkBean protocolLinkBean = new edu.mit.coeus.iacuc.bean.ProtocolLinkBean();
      protocolLinkBean.setProtocolNumber(iacucProtoFundingSourceBean.getProtocolNumber());
      protocolLinkBean.setSequenceNumber(iacucProtoFundingSourceBean.getSequenceNumber());
      protocolLinkBean.setModuleCode(2);
      protocolLinkBean.setModuleItemKey(instPropSpRevBean.getProposalNumber());
      protocolLinkBean.setModuleItemSeqNumber(instPropSpRevBean.getSequenceNumber());
      if(iacucProtoFundingSourceBean.getAcType() != null && iacucProtoFundingSourceBean.getAcType().equals("I")){
          CoeusMessageResourcesBean coeusMessageResourcesBean =new CoeusMessageResourcesBean();
          String insertComments = coeusMessageResourcesBean.parseMessageKey("notepadInsert_IACUCexceptionCode.6000");
          protocolLinkBean.setComments(insertComments);
          protocolLinkBean.setActionType("I");
      }else{
          CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
          String deleteComments = coeusMessageResourcesBean.parseMessageKey("notepadInsert_IACUCexceptionCode.6001");
          protocolLinkBean.setComments(deleteComments);
          protocolLinkBean.setActionType("D");
      }
      return protocolLinkBean;

  }

  /**
   * This method is for updating the IACUC notepad.
   * @param iacucProtoFundingSourceBean
   * @param maxEntryNumber
   * @return protocolNotepadBean
   * @throws DBException
   */
  public edu.mit.coeus.iacuc.bean.ProtocolNotepadBean getIacucNotepadBean(edu.mit.coeus.iacuc.bean.ProtocolFundingSourceBean iacucProtoFundingSourceBean, int maxEntryNumber )
  throws CoeusException,DBException{
      edu.mit.coeus.iacuc.bean.ProtocolNotepadBean protocolNotepadBean = new edu.mit.coeus.iacuc.bean.ProtocolNotepadBean();
      protocolNotepadBean.setProtocolNumber(iacucProtoFundingSourceBean.getProtocolNumber());
      protocolNotepadBean.setSequenceNumber(iacucProtoFundingSourceBean.getSequenceNumber());
      protocolNotepadBean.setEntryNumber(maxEntryNumber);
      if(iacucProtoFundingSourceBean.getAcType() != null && iacucProtoFundingSourceBean.getAcType().equals("I")){
          CoeusMessageResourcesBean coeusMessageResourcesBean =new CoeusMessageResourcesBean();
          String insertComments = coeusMessageResourcesBean.parseMessageKey("notepadInsert_IACUCexceptionCode.6000");
          protocolNotepadBean.setComments(insertComments);
      }
      protocolNotepadBean.setRestrictedFlag(false);
      protocolNotepadBean.setAcType("I");
      protocolNotepadBean.setUpdateTimestamp(dbTimestamp);
      return protocolNotepadBean;
  }

  /**
   * To release the lock for the protocol after adding it as special review
   */
  private void unLockIacucProtocols(Vector iacucProtoData) throws CoeusException,DBException{
      if(iacucProtoData != null && iacucProtoData.size()>0) {
          for(int index=0;index<iacucProtoData.size();index++) {
              edu.mit.coeus.iacuc.bean.ProtocolFundingSourceBean bean = (edu.mit.coeus.iacuc.bean.ProtocolFundingSourceBean)iacucProtoData.get(index);
              transMon.releaseLock("osp$IACUC Protocol_"+bean.getProtocolNumber(),userId);
          }
      }
  }

  //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB -- end

//    delete certification while deleting person for PPC start
 public ProcReqParameter UpdPropInvestigatorCertification( String proposalNumber,String personID
           )  throws CoeusException,DBException{
             Vector paramInves= new Vector(); 
        paramInves.addElement(new Parameter("AV_MODULE_ITEM_CODE",
                DBEngineConstants.TYPE_INT,3));
        paramInves.addElement(new Parameter("AV_MODULE_SUB_ITEM_CODE",
                DBEngineConstants.TYPE_INT,6));
          paramInves.addElement(new Parameter("AV_MODULE_ITEM_KEY",
                DBEngineConstants.TYPE_STRING,
                proposalNumber));
        paramInves.addElement(new Parameter("AV_MODULE_SUB_ITEM_KEY",
                DBEngineConstants.TYPE_STRING,
                personID));
 
        StringBuffer sqlInvestigator = new StringBuffer(
                "call UPDATE_PPC_CERTIFICATION(");
        sqlInvestigator.append(" <<AV_MODULE_ITEM_CODE>> , ");
        sqlInvestigator.append(" <<AV_MODULE_SUB_ITEM_CODE>> , ");
        sqlInvestigator.append(" <<AV_MODULE_ITEM_KEY>> , ");
        sqlInvestigator.append(" <<AV_MODULE_SUB_ITEM_KEY>> )");
        ProcReqParameter procInves  = new ProcReqParameter();
        procInves.setDSN(DSN);
        procInves.setParameterInfo(paramInves);
        procInves.setSqlCommand(sqlInvestigator.toString());

        return procInves;

 }
 // delete certification while deleting person for PPC end
 
 //COEUS 1457 STARTS
 public boolean sendRemovalEmailToPropPersons(String proposalNumber, Vector toPersonList,Vector toPersonRoles, String loginnedPerson )throws Exception{
     boolean mailSend=true;
     
     Vector vecRecipientsdata=new Vector();
     PersonRecipientBean personRecipientBean=new PersonRecipientBean();
     vecRecipientsdata.add(personRecipientBean);
     if(toPersonList!=null&&toPersonList.size()>0){
         String title = "Title: ";
         String sponsor = "Sponsor: ";
         String announcement = "Sponsor Announcement: ";
         String deadLine="Deadline Date: ";
         String role = "";
         String proposalNumberData="Proposal Number : "+proposalNumber;
         String piData = "PI :";
         String unitData = "Lead Unit :";
         String personId=null;
         String emailId=null;
         ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
         HashMap propDetails=proposalDevelopmentTxnBean.getPropPersonDetailsForMail(proposalNumber);
         if(propDetails!=null){
         MailMessageInfoBean mailMsgInfoBean=null;
         MailHandler mailHandler=new MailHandler();         
         title+=propDetails.get("TITLE");
         sponsor+=propDetails.get("SPONSOR_NAME");
         announcement+=((propDetails.get("PROGRAM_ANNOUNCEMENT_TITLE")==null)?" ":propDetails.get("PROGRAM_ANNOUNCEMENT_TITLE"));
         deadLine+=((propDetails.get("DEADLINE_DATE")==null)?" ":propDetails.get("DEADLINE_DATE"));
         piData+=propDetails.get("PERSON_NAME");
         unitData+=propDetails.get("UNIT_NUMBER")+" : "+propDetails.get("UNIT_NAME");
                        
         for(int i=0;i<toPersonList.size();i++){
             if(toPersonList.get(i) !=null){
                 personId=toPersonList.get(i).toString();
                 role=toPersonRoles.get(i).toString();
                 emailId=getEmailIdForPerson(personId);
                 if(emailId!=null&&emailId.trim().length()>0){
                     personRecipientBean.setEmailId(emailId);
                     personRecipientBean.setPersonId(personId);
                     mailMsgInfoBean=mailHandler.getNotification(ModuleConstants.PROPOSAL_DEV_MODULE_CODE,PERSON_REMOVAL_MAIL_ACTION_ID);
                     if(mailMsgInfoBean != null && mailMsgInfoBean.isActive()){
                         mailMsgInfoBean.setPersonRecipientList(vecRecipientsdata);
                         //mailMsgInfoBean.setSubject("Removed from Proposal");
                         mailMsgInfoBean.appendMessage(piData, "\n");
                         mailMsgInfoBean.appendMessage(unitData, "\n");
                         mailMsgInfoBean.appendMessage(proposalNumberData, "\n") ;
                         mailMsgInfoBean.appendMessage(sponsor, "\n") ;
                         mailMsgInfoBean.appendMessage(deadLine, "\n") ;
                         mailMsgInfoBean.appendMessage(title, "\n") ;
                         mailMsgInfoBean.appendMessage(announcement, "\n") ;
                         mailMsgInfoBean.appendMessage("You have been removed as "+role+ " for the above referenced project by "+loginnedPerson+", "+dbTimestamp.toString()+" .", "\n\n") ;
                         try{
                             mailHandler.sendMail(ModuleConstants.PROPOSAL_DEV_MODULE_CODE,PERSON_REMOVAL_MAIL_ACTION_ID, mailMsgInfoBean);
                            }
                         catch(Exception ex){
                             mailSend=false;
                         }
                     }
                 }
             }
         }//end of inner iteration
     }                  
     }
     return mailSend;
 }
 
 public String getEmailIdForPerson(String personId)throws CoeusException, DBException{
        String emailId=null;
        Vector result = null;
        Vector param= new Vector();
        HashMap protocolDevRow = null;
        param.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,personId));
        if(dbEngine !=null){
            result = dbEngine.executeRequest("Coeus","call GET_PERSON_EMAIL_ID( <<PERSON_ID>>, <<OUT RESULTSET rset>>) ", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
            protocolDevRow = (HashMap)result.elementAt(0);
            emailId=(String) protocolDevRow.get("EMAIL_ADDRESS");
        }
        return emailId;
    }
 //COEUS 1457 ENDS
 
 
}
