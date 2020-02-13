/*
 * CoeusGuiConstants.java
 *
 * Created on September 1, 2002, 3:15 PM
 */

/* PMD check performed, and commented unused imports and variables on 08-JULY-2010
 * by Satheesh Kumar
 */
package edu.mit.coeus.utils;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
/* JM */
import edu.vanderbilt.coeus.utils.CustomFunctions;
/* END JM */

/**
 * This class is used as look up parameter class which will give the
 * Module Label Title, Images Names, Connection String, MDI Form instance.
 *
 * @author  geo
 * @version 1.0
 */

public class CoeusGuiConstants {

    private static String IMAGE_DIRECTORY="images";
    //private static String sep = File.separator;
    private static String sep = "/";
    public static String HELP_URL = "/help.htm";
    //Set of Values to hold the function Type like edit/modify, add, disaply/view
    //mode falg.
    public static final char EDIT_MODE = 'M' ;
    public static final char MODIFY_MODE = 'M';
    public static final char ADD_MODE = 'A';
    public static final char DISPLAY_MODE = 'D';
    public static final char AMEND_MODE = 'E';
    public static final char NEW_AMENDMENT = 'N';
   /*Added for case#4278-In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am -start*/
    public static final char RENEWAL_WITH_AMENDMENT  = 'L';
    /*Constants added for new Amendment/Renewal Type Actions*/
    public static final char CONTINUING_WITH_RENEWAL  = '#';
    public static final char CONTINUING_WITH_RENEWAL_AMENDMENT  = '&';
    /*Added for case#4278-In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am -end*/
      
    public static final char RENEWAL = 'R';
    //Swing Login Mode
    public static String SWING_LOGIN_MODE = "";
    public static final String USERID = "USERID";
    
    /** To indicate that protocol is open in Administrative Corrections mode
     * Added for IRB Enhancement to modify protocol after submitting for review */
    public static final String ADMINISTRATIVE_CORRECTIONS = "C";

    // holds string value used to specify that the record has to be inserted.
    public static final String INSERT_RECORD = "I";

    // holds string value used to specify that the record has to be updated.
    public static final String UPADTE_RECORD = "U";

    /* holds string value used to specify that the record has to be marked as
     * deleted. */
    public static final String DELETE_RECORD = "D";

    // JM 2-14-2014 routing queue servlet
    public final static String ROUTING_QUEUE_SERVLET = "/RoutingQueueServlet";
    
    //used to hold the proposal servlet entry
    public final static String PROPOSAL_SERVLET = "/ProposalMaintenanceServlet";

    //used to hold the coeus function servlet alias name
    public final static String FUNCTION_SERVLET = "/coeusFunctionsServlet";

    //hols the coeus unit servlet alias name
    public final static String UNIT_SERVLET = "/unitServlet";

    // Holds the Coeus User Maintenance Servlet alias name
    public final static String USER_SERVLET = "/userMaintenanceServlet";

    private static String path = IMAGE_DIRECTORY+sep;
    //public static String CONNECTION_URL = "http://localhost:80/CoeusApplet";
    /**
     * Conneection URL.
     */
    public static String CONNECTION_URL = "";
    //images
    
    
// JM 6-23-2011 added icon for help gidget
    public static final String HELP_ICON = path+"iconHelp.gif";
// END    
    
    /* JM 5-25-2016 validation checks, Grants.gov labels */
    public static final String VALIDATIONS = "Validation Checks";
    public static final String GRANTS_GOV = "Grants.gov";
    /* JM END */
    
    /**
     * Coeus Image path
     */
    public static final String COEUS_ICON = "images/coeus16.gif";

    /* JM 3-30-2015 new icons - every size you could want */
    /* JM 4-15-2016 update for both sets of icons */
    public static final String ICON128_VUMC = "images/icons_vumc/icon_128x128.png";
    public static final String ICON96_VUMC = "images/icons_vumc/icon_96x96.png";
    public static final String ICON64_VUMC = "images/icons_vumc/icon_64x64.png";
    public static final String ICON48_VUMC = "images/icons_vumc/icon_48x48.png";
    public static final String ICON32_VUMC = "images/icons_vumc/icon_32x32.png";
    public static final String ICON24_VUMC = "images/icons_vumc/icon_24x24.png";
    public static final String ICON16_VUMC = "images/icons_vumc/icon_16x16.png";

    public static final String ICON128_VU = "images/icons_vu/icon_128x128.png";
    public static final String ICON96_VU = "images/icons_vu/icon_96x96.png";
    public static final String ICON64_VU = "images/icons_vu/icon_64x64.png";
    public static final String ICON48_VU = "images/icons_vu/icon_48x48.png";
    public static final String ICON32_VU = "images/icons_vu/icon_32x32.png";
    public static final String ICON24_VU = "images/icons_vu/icon_24x24.png";
    public static final String ICON16_VU = "images/icons_vu/icon_16x16.png";
    /* JM END */    
    
    /**
     * Inbox Image path
     */
    public static final String INBOX_ICON=path+"inbox.gif";
    /**
     * Awards Image path
     */
    public static final String AWARDS_ICON=path+"awards.gif";
    /**
     * Proposal Image path
     */
    public static final String PROPOSAL_ICON=path+"proposal.gif";
    
    public static final String CATEGORY_ICON=path+"categories.gif";

    /**
     * Role Rights Image Path
     */
    public static final String ROLE_RIGHTS_ICON=path+"rights.gif";
    
    /*New Public Message Image path*/
    
    public static final String BLACK_ICON=path+"blackIcon.gif";
    // Images for the RTF releated icons.
    public static final String RIGHT_ALIGNMENT = path+"rightalign.gif";
    public static final String CENTER_ALIGNMENT = path+"centeralign.gif";
    public static final String LEFT_ALIGNMENT = path+"leftalign.gif";
    public static final String JUSTIFY = path+"justify.gif";
    
    public static final String BOLD_ICON = path+"bold.gif";
    public static final String ITALIC_ICON = path+"italic.gif";
    public static final String STRIKE_ICON = path+"strikethrough.gif";
    public static final String UNDERLINE_ICON = path+"underline.gif";
    public static final String PRINT_PREVIEW_ICON = path+"printpeview.gif";

    // Images for the Subcontracting Reports
    public static final String SUB_SELECT_ALL_ICON = path+"sub_select.gif";
    public static final String SUB_DESELECT_ALL_ICON  = path+"sub_deselect.gif";
    public static final String SUB_GOALS_ICON  = path+"sub_goals.gif";
    public static final String SUB_VALIDATION_ICON  = path+"sub_validation.gif";
    // End 
    
    //Case #1621 & 1622 Start
    public static final String NONE_ICON = path+"none.gif";
     
    public static final String COMPLETE_ICON = path+"complete.gif";
      
    public static final String INCOMPLETE_ICON = path+"incomplete.gif";
    //Case #1621 & 1622 End
    
    /**
     * Proposal Development Image path
     */
    public static final String PROPOSAL_DEVELOPMENT_ICON=path+"proposalDevelopment.gif";
    /**
     * Rolodex Image path
     */
    public static final String ROLODEX_ICON=path+"rolodex.gif";
    /**
     * Sponsor Image path
     */
    public static final String SPONSOR_ICON=path+"sponsor.gif";
    /*
     *  Template Icon(XML) Path
     */
    public static final String XML_ICON = path+"xml.gif";
        /**
     * Sub Contract Image path
     */

    public static final String SUBCONTRACT_ICON=path+"subContract.gif";
    
    
    /** Image Icon for the IP Review - Institute Proposal Module
     */
    public static final String IP_REVIEW_ICON = path+"ipReview.gif";

    /**
     * User Details Image Path
     */
    public static final String DETAILS_ICON = path+"details.gif";

    /**
     * Negotation Image path
     */
    public static final String NEGOTIATIONS_ICON=path+"negotiations.gif";
    /**
     * Business Rule Image path
     */
    public static final String BUSINESS_RULES_ICON=path+"businessRules.gif";
    /**
     * Map Image path
     */

    /** for proposal Action servlet
     */
    public static final String PROPOSAL_ACTION_SERVLET = "/ProposalActionServlet";

    public static final String MAP_ICON=path+"map.gif";
    /**
     * Personnel Image path
     */
    public static final String PERSONNAL_ICON=path+"personnal.gif";
    /**
     * Users Image path
     */
    public static final String USERS_ICON=path+"users.gif";
    /**
     * Unit Hierarchy Image path
     */
    public static final String UNIT_HIERARCHY_ICON=path+"unitHierarchy.gif";
    /**
     * Cascade Image path
     */
    public static final String CASCADE_ICON=path+"cascade.gif";
    /**
     * Title Horizontal Image path
     */
    public static final String TILE_HORIZONTAL_ICON=path+"tileHorizontal.gif";
    /**
     * Title Vertical Image path
     */
    public static final String TILE_VERTICAL_ICON=path+"tileVertical.gif";
    /**
     * Layer Image path
     */
    public static final String LAYER_ICON=path+"layer.gif";
    /**
     * Tick Image path
     */
    public static final String TICK_ICON=path+"tick.gif";
    /**
     * Exit Image path
     */
    public static final String EXIT_ICON=path+"exit.gif";
    /**
     * Medusa Image path
     */
    public static final String MEDUSA_ICON=path+"medusa.gif";
    /**
     * Close Floder Image path
     */
    public static final String CLOSED_ICON=path+"closedfolder.gif";
    /**
     * Add Image path
     */
    public static final String ADD_ICON = path+"add.gif";
    /**
     * Disabled Add Image path
     */
    public static final String DADD_ICON = path+"dadd.gif";

    public static final String NEW_ICON = path + "new.gif";


    /**
     * Save Image path
     */
    public static final String SAVE_ICON = path+"save.gif";

    /** For Data over ride in proposal development
     */
    public static final String DATA_ICON = path+"dataOverride.gif";

    /**
     * Disabled Save Image path
     */
    public static final String DSAVE_ICON = path+"dsave.gif";

    /**
     * Move Image path
     */
    public static final String MOVE_ICON = path+"move.gif";
    /**
     * Find Image path
     */
    public static final String FIND_ICON = path+"find.gif";
    /**
     * Display Image path
     */
    public static final String DISPLAY_ICON = path+"display.gif";
    /**
     * Edit Image path
     */
    public static final String EDIT_ICON = path+"edit.gif";

    /**
     * Disabled Edit Image path
     */
    public static final String DEDIT_ICON = path+"dedit.gif";



    /**
     * Delete Image path
     */
    public static final String DELETE_ICON = path+"delete.gif";

    /**
     * Disabled Delete Image path
     */
    public static final String DDELETE_ICON = path+"ddelete.gif";
    /**
     * Sort Image path
     */
    public static final String SORT_ICON = path+"sort.gif";
    //public  static final String COPY_ICON = path+"copy.gif";
    /**
     * Copy Image path
     */
    public static final String COPY_ICON = path+"copy.gif";
    /**
     * Reference Image path
     */
    public static final String REFRENCES_ICON = path+"refrences.gif";
    /**
     * Save Image path
     */
    public static final String SEARCH_ICON = path+"search.gif";
    /**
     * Save as Image path
     */
    public static final String SAVEAS_ICON = path+"saveAs.gif";
    /**
     * Close Image path
     */
    public static final String CLOSE_ICON = path+"close.gif";
    /**
     * hand Icon Image path
     */
    public static final String HAND_ICON = path+"handIcon.gif";

    /**
     * row Icon Image path
     */
    public static final String ROW_ICON = path+"row.ico";

    /**
     * Up arrow Image path
     */
    public static final String UP_ARROW_ICON = path+"up.gif";
    /**
     * Down arrow Image path
     */
    public static final String DOWN_ARROW_ICON = path+"down.gif";
    /**
     * Active Role Icon Image path
     */
    public static final String ACTIVE_ROLE_ICON = path+"rolepa.gif";
    

    /**
     * Inactive Role Icon Image path
     */
    public static final String INACTIVE_ROLE_ICON = path+"rolepi.gif";

    /**
     * Active User Icon Image path
     */
    public static final String ACTIVE_USER_ICON = path+"usera.gif";

    /**
     * Inactive User Icon Image path
     */
    public static final String INACTIVE_USER_ICON = path+"useri.gif";

    /**
     * Active Proposal Role Icon Image path
     */
    public static final String PROP_ACTIVE_ROLE_ICON = path+"proprolepa.gif";

    /**
     * Inactive Proposal Role Icon Image path
     */
    public static final String PROP_INACTIVE_ROLE_ICON = path+"proprolepi.gif";

    /**
     * Active Admin Role Icon
     */
    public static final String ADMIN_ACTIVE_ROLE_ICON = path+"adminrolea.gif";

    /*
     * Inactive Admin Role Icon
     */
    public static final String ADMIN_INACTIVE_ROLE_ICON = path+"adminrolei.gif";

    /**
     * Active System Role Icon
     */
    public static final String SYSTEM_ACTIVE_ROLE_ICON = path+"systemrolea.gif";

    /**
     * Inactive System Role Icon
     */
    public static final String SYSTEM_INACTIVE_ROLE_ICON = path+"systemrolei.gif";

    /**
     * Descend Yes
     */
    public static final String DESCEND_YES_ICON = path+"descendy.gif";

    /**
     * Descend No
     */
    public static final String DESCEND_NO_ICON = path+"descendn.gif";

    /**
     * User Roles Image path
     */
    public static final String USER_ROLES_ICON = path+"roles.gif";

    /**
     * Static Trash Image path
     */
    public static final String TRASH_ICON = path+"trash.gif";

    /**
     * Animated Trash Image path
     */
    public static final String ANIMATED_TRASH_ICON = path+"trash2.gif";

    /**
     * Abstract Image path
     */
    public static final String ABSTRACT_ICON = path+"abstract.gif";

    /**
     * Biography Image path
     */
    public static final String BIOGRAPHY_ICON = path+"biography.gif";

    /**
     * Disabled Approve Image path
     */
    public static final String DAPPROVE_ICON = path+"dapprove.gif";

    //Added by Vyjayanthi
    /** Enabled Approve Image path */
    public static final String EAPPROVE_ICON = path+"eapprove.gif";

    /**
     * Disabled Left Arrow Image path
     */
    public static final String DLEFT_ARROW_ICON = path+"dleftArrow.gif";

    /**
     * Disabled PDF Image path
     */
    public static final String DISABLED_PDF_ICON = path+"dpdf.gif";

    /**
     * Eabled PDF Image path
     */
    public static final String ENABLED_PDF_ICON = path+"epdf.gif";

    /**
     * Disabled Right Arrow Image path
     */
    public static final String DRIGHT_ARROW_ICON = path+"drightArrow.gif";

    /**
     * Disabled Submit for Approval Image path
     */
    public static final String DSUBMIT_APPROVAL_ICON = path+"dsubmitApproval.gif";

    /**
     * Enabled Left Arrow Image path
     */
    public static final String ELEFT_ARROW_ICON = path+"eleftArrow.gif";

    /**
     * Enabled Right Arrow Image path
     */
    public static final String ERIGHT_ARROW_ICON = path+"erightArrow.gif";

    /**
     * Enabled Submit for Approval Image path
     */
    public static final String ESUBMIT_APPROVAL_ICON = path+"esubmitApproval.gif";

    /**
     * Narrative Image path
     */
    public static final String NARRATIVE_ICON = path+"narrative.gif";

    /**
     * Narrative Image path
     */
    public static final String GRANTS_GOV_LOGO_ICON = path+"GrantsGovLogo.gif";
    /**
     * Notepad Image path
     */
    public static final String NOTEPAD_ICON = path+"notepad.gif";

    /**
     * Notification Image path
     */
    public static final String NOTIFICATION_ICON = path+"notification.gif";

    /**
     * Persons Image path
     */
    public static final String PERSONS_ICON = path+"persons.gif";

    /**
     * Print Image path
     */
    public static final String PRINT_ICON = path+"print.gif";

    /**
     * Qualifications Image path
     */
    public static final String QUALIFICATIONS_ICON = path+"qualifications.gif";

    /**
     * Report Image path
     */
    public static final String REPORT_ICON = path+"report.gif";

    /**
     * Special Review Image path
     */
    public static final String SPECIAL_REVIEW_ICON = path+"specialreview.gif";

    /**
     *Committee Image Path
     */
    public static final String COMMITTEE_ICON = path+"committee.gif";

    /**
     *Protocol Image Path
     */
    public static final String PROTOCOL_ICON = path+"protocol.gif";

     // added new icon for protocol submission base window by Manoj 02/09/2003
   /**
     *Protocol Submission Image Path
     */
    public static final String PROTOCOL_SUBMISSION_BASE_ICON = path+"protocolSubmission.gif";
    
    //Added for case id COEUSQA-2717 icons for IACUC to Coeus Premium start 
    /**
     * IACUC Protocol Image Path
     */
    
    public static final String IACUC_PROTOCOL_ICON = path+"iacucProtocol.gif";

    /**
     * IACUC Protocol Submission Image Path
     */
    
    public static final String IACUC_PROTOCOL_SUBMISSION_BASE_ICON = path+"iacucProtocolSubmission.gif";
    
   //Added for case id COEUSQA-2717 icons for IACUC to Coeus Premium end
    
    /**
     *Protocol Submission Image Path
     */
    public static final String PROTOCOL_SUBMISSION_ICON = path+"protsubdetails.gif";


    /**
     * Correspondence Template Upload Image Path
     */
    public static final String CORRESPONDENCE_UPLOAD_ICON = path+"upload.gif";

    /**
     * Correspondence node Image Path
     */
    public static final String CORRESPONDENCE_NODE_ICON = path+"newNode.gif";

        /**
     * Correspondence Templates Image Path
     */
    public static final String CORRESPONDENCE_TEMPLATES_ICON = path+"templates.gif";


    /**
     *Schedule Image Path
     */
    public static final String SCHEDULE_ICON = path+"schedule.gif";

    /**
     * Training Image path
     */
    public static final String TRAINING_ICON = path+"training.gif";

    /**
     * Word Image path
     */
    public static final String WORD_ICON = path+"word.gif";

    /**
     * Yes/No Questions Image path
     */
    public static final String YES_NO_ICON = path+"YesNo.gif";


    /**
     * Enabled Move Up Arrow Image path
     */
    public static final String ENABLED_MOVE_UP_ICON = path+"moveup.gif";

    public static final String MEMO_ICON = path+"memo.gif";

    /**
     * Enabled Move Down Arrow Image path
     */
    public static final String ENABLED_MOVE_DOWN_ICON = path+"movedown.gif";

    /**
     * Disabled Move Up Arrow Image path
     */
    public static final String DISABLED_MOVE_UP_ICON = path+"dmoveup.gif";

    /**
     * Disabled Move Down Arrow Image path
     */
    public static final String DISABLED_MOVE_DOWN_ICON = path+"dmovedown.gif";

    public static final String HAND2_ICON = path+"handIcon2.gif";

    public static final String ENABLED_SUMMARY_ICON = path+"esummary.gif";
    public static final String DISABLED_SUMMARY_ICON = path+"dsummary.gif";

    public static final String CALCULATE_BUDGET = path+"calculate.gif";

    public static final String GENERATE_ALL_PERIODS = path+"generateAllPeriods.gif";

    public static final String CUSTOMIZE_VIEW = path+"customize.gif";

    public static final String RATES_FOR_PROPOSAL = path+"rates.gif";

   public static final String JUSTIFIED = path+"justified.gif";

   public static final String NOT_JUSTIFIED = path+"notJustified.gif";

   public static final String REFRESH_ICON = path+"refreshview.gif";

   public static final String STATUS_ICON = path+"changestatus.gif";

   //CASE #1167: Change from closedfolder.gif to notify.gif
   public static final String NOTIFY_ICON = path +"notify.gif";

   /* CASE #1167 Begin */
   public static final String BUDGET_ICON = path +"budget.gif";
   /* CASE #1167 End */

   public static final String PRINT_SELECTED_PERIOD_ICON = path + "printSelectedPeriod.gif";

   public static final String SCALE_ICON = path + "scale.gif";

   public static final String FIRST_PAGE_ICON = path + "firstPage.gif";

   public static final String NEXT_PAGE_ICON = path + "nextPage.gif";

   public static final String PRIOR_PAGE_ICON = path + "priorPage.gif";

   public static final String LAST_PAGE_ICON = path + "lastPage.gif";
   
   // added for select template
   public static final String SELECT_TEMPLATE_ICON = path + "SelectTemplate.gif";   
   
    /**
     * Reset Image path
     */
    public static final String RESET_ICON = path+"reset.gif";   
   

    //START=========Added For ShowRouting
     public static final String VERIFY_ICON_PATH = path+"verify.gif";
     public static final String APPROVE_ICON_PATH = path+"approved.gif";
     public static final String REJECT_ICON_PATH = path+"rejected.gif";
     public static final String PASS_ICON_PATH = path+"passed.gif";
     public static final String PRIMARY_ICON_PATH = path+"primary.gif";
     public static final String ALTERNATE_ICON_PATH = path+"alternate.gif";
     public static final String CHILD_TREE_NODE = path+"library.gif";

     public static final String PRIMARY_VERIFY = path+"primaryverify.gif";
     public static final String PRIMARY_APPROVE = path+"primaryapprov.gif";
     public static final String PRIMARY_PASSED = path+"primarypassed.gif";
     public static final String PRIMARY_REJECT = path+"primaryreject.gif";
     public static final String PRIMARY_WAITING = path+"primarywaiting.gif";
     public static final String PRIMARY_APPROVE_OTHER = path + "primapprovother.gif";

     public static final String ALTERNATE_VERIFY = path+"altverify.gif";
     public static final String ALTERNATE_APPROVE = path+"altapprov.gif";
     public static final String ALTERNATE_PASSED = path+"altpassed.gif";
     public static final String ALTERNATE_REJECT = path+"altreject.gif";
     public static final String ALTERNATE_WAITING = path+"altwaiting.gif";
     public static final String ALTERNATE_APPROVE_OTHER = path+"altapprovother.gif";

    //======END===For ShowRouting
    //COEUSQA:3441 - Recalled Proposal notifications, record status changes, and inbox messages - Start
     public static final String RECALLED_ICON_PATH = path+"recalled.gif";
    //COEUSQA:3441 - End

     public static final String ACTIVE_AWARD_ICON = path + "activeAward.gif";
     public static final String PENDING_AWARD_ICON = path + "pendingAward.gif";
     public static final String HOLD_AWARD_ICON = path + "holdAward.gif";
     public static final String OTHER_AWARD_ICON = path + "otherAward.gif";

     // Added by chandra
     public static final String AWARD_HIERARCHY = path+"award_hierarchy.gif";
     public static final String EXPAND_ALL = path+"expand.gif";
     public static final String COLLAPSE_ALL = path+"collapse.gif";

     //Added by Bijosh
     public static final String CUSTOMIZE_VIEW_ICON = path+"customizeview.gif";
     public static final String SELECT_ALL_ICON = path+"selectall.gif";
     public static final String VIEW1_ICON = path+"view1.gif";
     public static final String VIEW2_ICON = path+"view2.gif";
     public static final String VIEW3_ICON = path+"view3.gif";
     public static final String PRINT_SELECTED_ICON = path+"selectprint.gif";

     // Added by chandra - Vary in the size dof the medusa related icons.
     // Separate copy of the same image with different name.

     public static final String MEDUSA_AWARD_ICON = path+"medusa_awards.gif";
     public static final String MEDUSA_INST_PROPOSAL_ICON = path+"medusa_proposal.gif";
     public static final String MEDUSA_SUBCONTRACT_ICON = path+"medusa_subContract.gif";
     public static final String MEDUSA_DEV_PROP_ICON = path+"medusa_proposalDevelopment.gif";
     public static final String  MEDUSA_NEGOTIATION_ICON = path+"medusa_negotiations.gif";

     public static final String NEGOTIATION_FOLDMARK_ICON = path + "foldmark.gif";
     
     /** Images for the Business Rules tree Nodes
      */
     public static final String  RULE_TRUE_NODE = path+"truenode.gif";
     public static final String  RULE_NEXT_NODE = path+"nextnode.gif";
     public static final String  RULE_PARENT_NODE = path+"parentnode.gif";

     public static final String NOTEPAD_FRAME_TITLE = "Notepad";
    /**
     * Message window title
     */
    public static final String MESSAGE_WINDOW_TITLE = "Coeus";
    /**
     * Organization frame title
     */
    public static final String ORGANIZATION_FRAME_TITLE = "Maintain Organization";
    /**
     * Organization window title
     */
    public static final String ORGANIZATION_WINDOW_TITLE = "Maintain Organization";
    //"Organization Details";
    /**
     * Rolodex title
     */
    public static final String TITLE_ROLODEX = "Rolodex";
    /**
     * Committee frame title
     */
    public static final String COMMITTEE_FRAME_TITLE = "Committee Details";
    /**
     * Committee frame title
     */
    public static final String COMMITTEEBASE_FRAME_TITLE = "Committee";
    /**
     * Protocol frame title
     */
    public static final String PROTOCOL_FRAME_TITLE = "IRB Protocol Details";
    /**
     * Protcol Main frame title
     */
    public static final String PROTOCOLBASE_FRAME_TITLE = "IRB Protocol";
    
   // Added for COEUSQA-2717 Add icons for IACUC to Coeus Premium start.
    
   /**
    *  Iacuc Protcol Main frame title
    */
    public static final String IACUC_PROTOCOL_BASE_FRAME_TITLE = "IACUC Protocol";
    
       
   /**
    *  Iacuc Protcol submission list Main frame title
    */
    
    public static final String IACUC_PROTO_SUB_LIST_BASE_FRAME_TITLE = "IACUC Protocol Submission List";
    
   // Added for COEUSQA-2717 Add icons for IACUC to Coeus Premium start end.
    /**
     * Committee frame title
     */
    public static final String COMMITTEE_TITLE = "Committee";
    /**
     * Unit Hierarchy Maintenance frame title
     */
    public static final String TITLE_UNIT_HIERARCHY = "Unit Hierarchy Maintenance";
    /**
     * IRB Area of Research frame title
     */
    public static final String TITLE_AREA_OF_RESEARCH = "IRB Areas Of Research";
     /** 
      * IACUC Area of Research frame title
     */
    public static final String TITLE_IACUC_AREA_OF_RESEARCH = "IACUC Areas Of Research";
    /**
     * Schedule Maintenance frame title
     */
    public static final String SPONSOR_FRAME_TITLE = "Sponsor Maintenance";

    /**
     * Schedule frame title
     */
    public static final String SCHEDULE_DETAILS_TITLE = "Schedule Details";

    /**
     * Proposal base window title
     */
    public static final String PROPOSAL_BASE_FRAME_TITLE = "Development Proposal List";

    /**
     * Proposal frame title
     */
    public static final String PROPOSAL_DETAILS_FRAME_TITLE = "Proposal Details";

    /**
     * Proposal Abstracts frame title
     */
    public static final String PROPOSAL_ABSTRACTS_FRAME_TITLE = "Proposal Abstracts";
    /**
     * Proposal Narrative frame title
     */
    public static final String PROPOSAL_NARRATIVE_FRAME_TITLE = "Narrative for Proposal ";
    // Added for COEUSQA-1692_User Access - Maintenance_start
    /**
     * Unit User Role Maintenence Base Window Title.
     */
    public static final String UNIT_USER_ROLE_MAINTENENCE_WINDOW_TITLE = "User Maintenance For : ";
    // Added for COEUSQA-1692_User Access - Maintenance_end
    /**
     * User Role Maintenence Base Window Title.
     */
    public static final String USER_ROLE_MAINTENENCE_BASE_WINDOW_FRAME_TITLE = "User Maintenence";
    /*IACUC Protocol Amendment Titles-start*/   
    public static final String IACUC_AMENDMENT_DETAILS_TITLE = "IACUC Protocol Amendment";
    public static final String IACUC_RENEWAL_DETAILS_TITLE = "IACUC Protocol Renewal";
    public static final String IACUC_RENEWAL_WITH_AMENDMENT_TITLE = "IACUC Protocol Renewal with Amendment";
    public static final String IACUC_CONTINUATION_RENEWAL_DETAILS_TITLE = "IACUC Protocol Continuation/Continuing Review";    
    public static final String IACUC_CONTINUATION_RENEWAL_AMEND_DETAILS_TITLE = "IACUC Protocol Continuation/Continuing Review with Amendment";
    /*IACUC Protocol Amendment Titles-end*/   
    
    /*IRB Protocol Amendment Titles-start*/   
    public static final String AMENDMENT_DETAILS_TITLE = "IRB Protocol Amendment";
    public static final String RENEWAL_DETAILS_TITLE = "IRB Protocol Renewal";
    public static final String RENEWAL_WITH_AMENDMENT_TITLE = "IRB Protocol Renewal with Amendment";
    /*IRB Protocol Amendment Titles-start*/   
   
    /*onstants define For new Amendment/Renewal type Title*/
    
    
    /*Added for case#4278-In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am -start*/    
  
    /*Added for case#4278-In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am -end*/
    
    public static final String NEW_AMENDMENT_TITLE = "New Amendment";
    //Added for Quesion answer
    public static final String PROPOSAL_QUESTIONNAIRE = "Proposal Questionnaires";
    
    public static final String PROTOCOL_QUESTIONNAIRE = "Protocol Questionnaire";
    
    public static final String PROTOCOL_MODULE = "PROTOCOL MODULE";
    
    public static final String QUESTIONNAIRE_TITLE = "Questionnaire Maintenance";
    
    public static final String QUESTIONNAIRE_FRAME_TITLE = "Questionnaire List";
    // Rules Frame Title
    public static final String BUSINESS_RULE_FRAME_TITLE = "Business Rule Maintenance for unit";
    /**
     * Medusa Details
     */
    public static final String MEDUSA_BASE_FRAME_TITLE = "Medusa - Award, Institute Proposal and Development Proposal Links";

    /**
     * Birt Report Title
     */
     public static final String REPORT_FRAME_TITLE = "Report Maintenance";

    public static final String MEDUSA_NEGOTIATION = "Medusa";
    //public static final String NEW_REVISION_TITLE = "New Revision";

    /**
     * Massachusetts Institute Of Technology
     */
    public static final String MIT = "Massachusetts Institute Of Technology";

    /**
     * Person Search title
     */
    public static final String PERSON_SEARCH = "PERSONSEARCH";
    /**
     * Rolodex Search title
     */
    public static final String ROLODEX_SEARCH = "ROLODEXSEARCH";

    // holds the search window name
    public static final String PROPOSAL_SEARCH = "PROPOSALDEVSEARCH";

    /**
     * Schedule frame title
     */
    public static final String SCHEDULE_DETAILS_FRAME_TITLE = "Schedule Details";

    /**
     * Person Main frame title
     */
    public static final String PERSON_BASE_FRAME_TITLE = "Person";
    
     public static final String MAPS_BASE_FRAME_TITLE = "Map Maintenance For unit";

    //public static final String DATE_FORMAT = "
    private static CoeusAppletMDIForm mdiForm;

    //jenlu start
    public static final String TITLE_CODE_TABLE = "Code Table Maintenance" ;

    //jenlu end

    /**
     * Schedule base frame title
     */
    public static final String SCHEDULE_BASE_FRAME_TITLE = "Schedules";

     // Modified for COEUSQA-2824_IRB and IACUC protocol access permissions should not include approver_start
    /**
     * Specifies the role id for Protocol Approver
     */
     public static final int IRB_PROTOCOL_APPROVER_ID = 201;
     // Modified for COEUSQA-2824_IRB and IACUC protocol access permissions should not include approver_end

     public static final String PERSON_BIOGRAPHY_BASE_FRAME_TITLE = "PersonBiography";

     public static final String PROP_PERSON_BIOGRAPHY_FRAME_TITLE = "ProposalPersonnel";
     
     /**
     * Sponsor Hierarchy base frame title
     */
     
     public static final String SPONSORHIERARCHY_BASE_WINDOW = "Maintain Sponsor Hierarchy";
// Commented for COEUSQA-2824_IRB and IACUC protocol access permissions should not include approver_start
//    /**
//     * Specifies the role id for Protocol Aggregator
//     */
//     public static final int PROTOCOL_AGGREGATOR_ID = 201;
// Commented for COEUSQA-2824_IRB and IACUC protocol access permissions should not include approver_start
    /**
     * Specifies the role id for Protocol Coordinator
     */
     public static final int PROTOCOL_COORDINATOR_ID = 200;


    /**
     * Specifies the role id for Proposal Aggregator
     */
     public static final int PROPOSAL_AGGREGATOR_ID = 100;

    /**
     * Specifies the role id for Proposal Approver
     */
     public static final int PROPOSAL_APPROVER_ID = 101;


    /**
     * Proposal Status code values
     */
    public static final int PROPOSAL_IN_PROGRESS = 1;
    public static final int PROPOSAL_APPROVAL_IN_PROGRESS = 2;
    public static final int PROPOSAL_REJECTED = 3;
    public static final int PROPOSAL_APPROVED = 4;
    public static final int PROPOSAL_SUBMITTED = 5;
    public static final int PROPOSAL_POST_SUB_APPROVAL = 6;
    public static final int PROPOSAL_POST_SUB_REJECTION = 7;

    /**
     * Specifies the roleId
     * simultaneously.
     */
    public static final int PROPOSAL_VIEWER_ROLE_ID = 104;

    /**
     * Specifies Institute Unit Number
     */
    public static final String INSTITUTE_UNIT_NUMBER = "000001";

    /**
     * Specifies the maximum number of proposal display sheets that can be opened
     * simultaneously.
     */
    public static final int MAX_PROP_DISPLAY_SHEETS = 2;

    /**
     *  Correspondence details
     */
    //public static final String PROTO_CORRESP_TYPE_BASE_FRAME_TITLE = "Correspondence Types";

    /**
     *  IRB Correspondence details
    */
     public static final String PROTO_CORRESP_TYPE_BASE_FRAME_TITLE = "IRB Correspondence Types";
    /**
     * The following are the constants for all the modules.
     */
    public static final String PROPOSAL_MODULE = "PROPOSAL MODULE";
    public static final String PERSON_MODULE = "PERSON MODULE";

    //Case 2106 Start
    public static final String AWARD_MODULE = "AWARD MODULE";
    public static final String INSTITUTE_PROPOSAL_MODULE = "INSTITUTE PROPOSAL MODULE";
    //Case 2106 End
    
    public static final int PROTOCOL_DETAIL_CODE = 0;
    public static final int PROTOCOL_AMENDMENT_CODE = 1;
    public static final int PROTOCOL_RENEWAL_CODE = 2;
    /*Added for case#-In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Amendment -start*/
    public static final int PROTOCOL_AMENDMENT_WITH_RENEWAL_CODE = 3;
    public static final int PROTOCOL_CONTINUING_WITH_RENEWAL_CODE = 4;
    public static final int PROTOCOL_CONTINUING_WITH_RENEWAL_AMEND_CODE = 5;
    /*Added for case#4278-In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Amendment -end*/
    public static final int LOCK_SUCCESSFUL = 1;
    public static final int LOCK_UNSUCCESSFUL = 0;

    //Budget Constants - Start
    public static final String BUDGET_FRAME_TITLE = "Budget";
    //Budget Constants - End

    //Award Constants - Start
    public static final String AWARD_FRAME_TITLE = "Award List";

    public  static final String INSTITUTE_PROPOSAL_FRAME_TITLE = "Institutional Proposal List";

    public static final String PROPOSAL_LOG_FRAME_TITLE = "Proposal Log List";

    public static final String CORRECT_INSTITUTE_PROPOSAL_BASE_WINDOW = "Correct Institute Proposal";
    
    public static final String CURRENT_AND_PENDING_SUPPORT = "Current and Pending Support";

    public static final String AWARD_BASE_WINDOW = "Award Base Window";
    
    public static final String AWARD_TEMPLATE_BASE_WINDOW = "Award Template Base Window";

    public static final String NEGOTIATION_DETAILS_FRAME_TITLE = "Negotiation";

    public static final String NEGOTIATION_DETAILS = "Negotiation Details";

    /** To indicate that Negotiation is opened from Negotiation List */
    public static final String NEGOTIATION_LIST = "NegotiationList";

    /** To indicate that Negotiation is opened from Institute Proposal List */
    public static final String INSTITUTE_PROPOSAL_LIST = "IPList";

    public static final String REPORTING_REQ_BASE_WINDOW = "Reporting Requirements Base Window";

    public static final String AWARD_REPORTING_REQ = "Award Reporting Requirements";
    
    
    public static final int MAX_AWARD_DISPLAY_SHEETS = 5;

    public static final int MAX_INST_PROPOSAL_DISPLAY_SHEETS = 5;

    public static final int MAX_REP_REQ_DISPLAY_SHEETS = 3;

    public static final int MAX_NEGOTIATION_DISPLAY_SHEETS = 5;

    public static final String AWARD_NEW_ENTRY_ICON = path + "award_new_entry.gif";
    public static final String REPORTING_REQUIREMENTS_ICON = path + "reporting_req.gif";
    public static final String GREEN = path + "green.gif";
    public static final String YELLOW = path + "yellow.gif";
    public static final String BLUE = path + "blue.gif";
    public static final String RED = path + "red.gif";
    //Award Constants - End
    
    //Subcontract Constants - Start
    
    public static final String CORRECT_SUBCONTRACT_BASE_WINDOW = "Correct Subcontract";
    public static final String SUBCONTRACT_FRAME_TITLE = "Subcontract List";
    public static final int MAX_SUBCONTRACT_DISPLAY_SHEETS = 5;
    
    //Subcontract Constants - End
    
    public static final String IP_TEMP_LOG_ICON = path + "ip_temp_log.gif";
    public static final String IP_MERGE_ICON = path + "ip_merge.gif";

    // 2930: Auto-delete Current Locks based on new parameter  - Start
//    // Added by Shivakumar
//     public static int POLLING_INTERVAL = 0;
    // 2930: Auto-delete Current Locks based on new parameter  - End
     public static final String COST_ELEMENT_FRAME_TITLE = "Maintain Cost Element";
     public static final String SUBCONTRACTING_REPORTS = "Subcontracting Reports";
     
     public static final String S2S_SUB_LIST_FRAME_TITLE = "S2S Submission List";
     public static final String COEUS_LABEL_PATH = "/edu/mit/coeus/resources/CoeusLabels.properties";
     //#Case 3855 -- start represents property file for attachment path
       public static final String COEUS_DISPLAY_PROPERTY_PATH  = "/edu/mit/coeus/resources/CoeusDisplay.properties";
     // #Case 3855 -- end
     
     //Added for proposal hierachy
     public static final String PROVISION_ICON = path+"provision.gif";
     public static final String FINAL_ICON = path+"final.gif";
     public static final String BUDGET_MODULE = "Budget";
     public static final String PARENT_PROP_HIE_ICON = path+"parent.gif"; 
     public static final String CHILD_PROP_HIE_ICON = path+"child.gif";
     //Added for coeus4.3 questionnaire enhancement case#2946
     public static final String CONDITIONAL_PROP_HIE_ICON = path+"conditional.gif";
     public static final String QUESTION_PROP_HIE_ICON = path+"question.gif";
     //Added for Sync ICON by tarique
     public static final String SYNC_ICON = path+"sync.gif";
     public static final String BUDGET_FINAL_ICON = path +"budgetFinal.gif";
     
     /*
      *Added by Geo to use the message id in coeus exception for not shwoing the form
      */
     public static final String DO_NOT_SHOW_FORM = "DO_NOT_SHOW_FORM";
     //Added for Coeus 4.3 enhancement PT ID 2210: View Protocol History - start
     public static final String HISTORY_NEW_ICON = path + "history_new.gif";
     public static final String HISTORY_MODIFIED_ICON = path + "history_modified.gif";
     public static final String HISTORY_DELETED_ICON = path + "history_deleted.gif";
     public static final String PROTOCOL_HISTORY_WINDOW = "Protocol History";
     //Added for Coeus 4.3 enhancement PT ID 2210: View Protocol History - end
     //Added for Coeus 4.3 PT ID 2232 - Custom Roles - start
     public static final String CREATE_ROLE_ICON = path + "createRole.gif";
     public static final String MODIFY_ROLE_ICON = path + "modifyRole.gif";
     //Added for Coeus 4.3 PT ID 2232 - Custom Roles - end
     
     //Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - start
     public static final String ENABLED_ATTACHMENT_ICON = path + "ebinary_attachment.gif";
     public static final String DISABLED_ATTACHMENT_ICON = path + "dbinary_attachment.gif";
    public static String SECONDARY_LOGIN_MODE = null;
     //Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - end
     //Added for COEUSQA-1525 : Attachments for Institute Proposal - Start
      public static final String INSTITUTE_PROPOSAL_ATTACHMENT_FRAME_TITLE = "Attachment for Institute Proposal";
     //COEUSQA-1525 : End
     private CoeusGuiConstants(){
     }
    /** 
     * This method will set the coeus mdi form.
     * @param newMDIForm CoeusAppletMDIForm
     */
    public static void setMDIForm(CoeusAppletMDIForm newMDIForm){
        mdiForm = newMDIForm;
    }

    /**
     * This method will get the coeus mdi form.
     * @return CoeusAppletMDIForm coeus appliction from.
     */
    public static CoeusAppletMDIForm  getMDIForm(){
        return mdiForm;
    }
    public static String getPath(){
        return path;
    }
    
    /**
     * Email Image path
     */
    public static final String EMAIL_ICON=path+"email.gif";
    
    /**
     * Common Date Strings used in Coeus Premium
     * Added with case 4185
     *
     */
    public static final String UI_DATE_FORMAT       = "dd-MMM-yyyy";
    public static final String TIMESTAMP_FORMAT     = "dd-MMM-yyyy hh:mm:ss";
    public static final String DEFAULT_DATE_FORMAT  = "MM/dd/yyyy";
    public static final String DATE_SEPARATORS      = ":/.,|-";
    
    public static final String EMPTY_STRING         = "";
    
    public static final String IACUC_PROTOCOL_FRAME_TITLE = "IACUC Protocol Details";
    /**
     * IACUC specific Icon Image path
     */
     public static final String IACUC_ACTIVE_ROLE_ICON = path +"iacucActiveRole.gif";
     public static final String IACUC_IN_ACTIVE_ROLE_ICON = path +"iacucInActiveRole.gif";
    //IACUC Changes - End
    
       /**
     *  IACUC Correspondence details
     */
    public static final String IACUC_CORRESP_TYPE_BASE_FRAME_TITLE = "IACUC Correspondence Types";  
      
    /*
     * IACUC Tab Names 
     */
    public static final String IACUC_DETAIL_TAB_NAME = "Protocol";
    public static final String IACUC_INVESTIGATOR_TAB_NAME = "Investigator";
    public static final String IACUC_STUDY_PER_TAB_NAME = "Study Personnel";
    public static final String IACUC_CORRESPONDENTS_TAB_NAME = "Correspondents";
    public static final String IACUC_AOR_TAB_NAME = "Area of Research";
    public static final String IACUC_FUNDING_SOURCE_TAB_NAME = "Funding Source";
    public static final String IACUC_ACTIONS_TAB_NAME = "Actions";
    //Renamed tabs with CoeusQQA-2551:Rework how user enters species,study groups and procedures in IACUC protocols
    public static final String IACUC_SPECIES_TAB_NAME = "Species / Groups";
    public static final String IACUC_STUDY_GROUP_TAB_NAME = "Procedures";
    //CoeusQA:2551 End
    public static final String IACUC_SCIENTIFIC_JUST_TAB_NAME = "Scientific Justification";
    public static final String IACUC_ALTERNATIVE_SEARCH_TAB_NAME = "Alternatives Search";
    public static final String IACUC_ATTACHMENTS_TAB_NAME = "Attachments";
    public static final String IACUC_SPECIAL_REVIEW_TAB_NAME = "Special Review";
    public static final String IACUC_NOTES_TAB_NAME = "Notes";
    public static final String IACUC_AMEND_REN_SUMM_TAB_NAME = "Amendments & Renewals";
    public static final String IACUC_AMEND_SUMM_TAB_NAME = "Amendment Summary";
    public static final String IACUC_REN_SUMMARY_TAB_NAME = "Renewal Summary";
    public static final String IACUC_OTHERS_TAB_NAME = "Others";
    /*
     * IACUC display tab order 
     */
    public static final int IACUC_DETAIL_TAB_ORDER_INDEX = 0;
    public static final int IACUC_INVESTIGATOR_TAB_ORDER_INDEX = 1;
    public static final int IACUC_STUDY_PER_TAB_ORDER_INDEX = 2;
    public static final int IACUC_CORRESPONDENTS_TAB_ORDER_INDEX = 3;
    public static final int IACUC_AOR_TAB_ORDER_INDEX = 4;
    public static final int IACUC_FUNDING_SOURCE_TAB_ORDER_INDEX = 5;
    public static final int IACUC_ACTIONS_TAB_ORDER_INDEX = 6;
    public static final int IACUC_SPECIES_TAB_ORDER_INDEX = 7;
    public static final int IACUC_STUDY_GROUP_TAB_ORDER_INDEX = 8;
    public static final int IACUC_SCIENTIFIC_JUST_TAB_ORDER_INDEX = 9;
    public static final int IACUC_ALTERNATIVE_SEARCH_TAB_ORDER_INDEX = 10;
    public static final int IACUC_ATTACHMENTS_TAB_ORDER_INDEX = 11;
    public static final int IACUC_SPECIAL_REVIEW_TAB_ORDER_INDEX = 12;
    public static final int IACUC_NOTES_TAB_ORDER_INDEX = 13;
    public static final int IACUC_AMEND_REN_SUMM_TAB_ORDER_INDEX = 14;
    public static final int IACUC_OTHERS_TAB_ORDER_INDEX = 15;

    /*
     * IACUC Tab Index for exception
     * For others don't change the index, index is common for all the modules
     */
    public static final int IACUC_DETAIL_EXCEP_TAB_INDEX = 0;
    public static final int IACUC_INVESTIGATOR_EXCEP_TAB_INDEX = 1;
    public static final int IACUC_STUDY_PER_EXCEP_TAB_INDEX = 2;
    public static final int IACUC_CORRESPONDENTS_EXCEP_TAB_INDEX = 3;
    public static final int IACUC_AOR_EXCEP_TAB_INDEX = 4;
    public static final int IACUC_FUNDING_SOURCE_EXCEP_TAB_INDEX = 5;
    public static final int IACUC_ACTIONS_EXCEP_TAB_INDEX = 6;
    public static final int IACUC_SPECIES_EXCEP_TAB_INDEX = 7;
    public static final int IACUC_STUDY_GROUP_EXCEP_TAB_INDEX = 8;
    public static final int IACUC_SCIENTIFIC_JUST_EXCEP_TAB_INDEX = 9;
    public static final int IACUC_ALTERNATIVE_SEARCH_EXCEP_TAB_INDEX = 10;
    public static final int IACUC_ATTACHMENTS_EXCEP_TAB_INDEX = 11;
    public static final int IACUC_SPECIAL_REVIEW_EXCEP_TAB_INDEX = 12;
    public static final int IACUC_OTHERS_EXCEP_TAB_INDEX = 13;
    public static final int IACUC_NOTES_EXCEP_TAB_INDEX = 14;
    public static final int IACUC_AMEND_REN_SUMM_EXCEP_TAB_INDEX = 15;
    // Added for IACUC Questionnaire implementation - Start
    public static final String IACUC_PROTOCOL_QUESTIONNAIRE = "IACUC Protocol Questionnaire";    
    // Added for IACUC Questionnaire implementation - End
    //Added for Case id COEUSQA-1724_Reviewer View of Protocol start
    public static final String IACUC_LIST_WINDOW = "PL";
    public static final String IACUC_PROTO_DETAIL_WINDOW = "PD";
    public static final String IACUC_SUB_LIST_WINDOW = "PS";
    public static final String IACUC_SCHEDULE_DETAIL_WINDOW = "SD";
    //Added for Case id COEUSQA-1724_Reviewer View of Protocol end
    //COEUSQA-2542_Allow Protocol Reviewer to upload Attachments - Start
    /**
     *  Review Attachment details
     */
    public static final String REVIEW_ATTACHMENTS_TITLE = "Review Attachments"; 
    //COEUSQA-2542_Allow Protocol Reviewer to upload Attachments - End
    //COEUSQA-2735 Cost sharing distribution for Sub awards - Start
    public static final String SUB_AWARD_COST_SHARING_TITLE = "Sub Award Cost Sharing for ";
    //COEUSQA-2735 Cost sharing distribution for Sub awards - End
    
    // Added COEUSQA-3230 : Move IACUC protocol roles below - Start
    // O - OSP Level Right or Role
    // S - Departmental level right or Role
    // P - Proposal Level Right or Role
    // R - IRB Protocol Level Right or Role
    // I - IACUC Protocol Level Right or Role
    // T - Report Right or Role
    // For changing the order of the role or right display in UI, can be done here
    // Need to change in get_roles_for_unit.sql,get_user_roles_for_unit.sql procedure as well
    public static final String roleRightSortOrder[] = {"O", "S", "P", "R", "I","T"};
    // Added COEUSQA-3230 : Move IACUC protocol roles below - End

}
