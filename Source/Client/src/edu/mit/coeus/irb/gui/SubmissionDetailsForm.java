/*
 * SubmissionDetailsForm.java
 *
 * Created on March 27, 2003, 5:15 PM
 */

/* PMD check performed, and commented unused imports and variables on 25-JAN-2011
 * by Bharati
 */

package edu.mit.coeus.irb.gui;

import edu.mit.coeus.gui.* ;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.irb.bean.* ;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;

import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.util.Date;
import java.beans.*;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusDlgWindow ;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.brokers.* ;
import edu.mit.coeus.irb.bean.CommitteeMembershipDetailsBean;
import edu.mit.coeus.irb.bean.ProtocolReviewerInfoBean;
import edu.mit.coeus.bean.CheckListBean;
import edu.mit.coeus.irb.bean.ProtocolSubmissionInfoBean;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.irb.controller.ProtocolMailController;

public class SubmissionDetailsForm extends CoeusDlgWindow 

//Case #2080 Start 1
implements Observer
//Case #2080 End 1
{
    
     //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    /* This is used to know whether data is modified or add  */
    private boolean saveRequired = false;  
        
    /** char which specifies the form is opened in Add mode */
    private static final char ADD_MODE = 'A';

    /** char which specifies the form is opened in Modify mode */
    private static final char MODIFY_MODE = 'M';

    /** holds string value used to specify that the record has to be inserted.*/
    private static final String INSERT_RECORD = "I";

    /** holds string value used to specify that the record has to be updated. */
    private static final String UPDATE_RECORD = "U";

    /** holds string value used to specify that the record has to be marked as
        deleted. */
    private static final String DELETE_RECORD = "D";

    /** holds character value used to specify that the record has to be saved. */
    private static final char SAVE_RECORD = 'S';

    /** holds character value used to specify that the request is to fetch the
        schedules for a specified committee */
    private static final char SCHEDULE_SELECTION = 'V';

    /** holds character value used to specify that the request is to fetch the
        reviewers for a specified schedule */
    private static final char REVIEWER_SELECTION = 'R';

    //prps start - jul 15 2003
    private static final char PROTO_MAX_SUB_NUM = 'X' ;
    //prps end
    
    /** holds character value used to specify that the request is to fetch the
        count of protocols submitted to a schedule */
    private static final char PROTO_SUB_COUNT = 'C';

    /** char which specifies the mode in which the form is opened */
    private char functionType;

     /** reference object of ProtocolSubmissionInfoBean which will be used to
        get and set the values of the form objects */
    private ProtocolSubmissionInfoBean submissionBean;

    /** reference object of ProtocolReviewerInfoBean which will be used to
        get and set the values of the form object */
    private ProtocolReviewerInfoBean reviewerBean;

    /** reference object of ProtocolInfoBean which is passed as argument from
        ProtocolDetailsForm to get the protocol number and sequence number */
    private ProtocolInfoBean protocolInfo;
    
    private ScheduleDetailsBean scheduleDetailsBean ;
    
    /** This class instance will be popped up when Review Comments button will be 
     *clicked. This is to view whether the protocol contains Minute details
     */
    private ReviewCommentsForm reviewCommentsForm = new ReviewCommentsForm(true);
    
    
    private Frame parent;
   
    private CoeusDlgWindow thisWindow ;
    
    private Vector vecSubmissionDetails ;
    
    private String title ;
    
    private CoeusFontFactory fontFactory;
    
    private String protocolId ;
    
    private String oldCommitteeId ;
    private String oldScheduleId ;
    
    private String newCommitteeId ;
    private String newScheduleId ; 
    private int noVoteCount = 0 ;
    private int yesVoteCount = 0 ;
    private int abstainerCount = 0 ;
    private String votingComments = "" ;
    
    //prps check start - these vars are not reqd... just to avoid compilation error 
    // i'm adding this
    /** reference object of CommitteeSelectionForm */
    private CommitteeSelectionForm committeeSelectionForm;

    /** reference object of ScheduleSelectionForm */
    //private ScheduleSelectionForm scheduleSelectionForm;
    private ScheduleSelectionWindow  scheduleSelectionWindow ;
    
    /** reference object of ReviewerSelectionForm */
    private ReviewerSelectionForm reviewerSelectionForm;
    //prps check end
    
    /** collection object which holds the available protocol submission types */
    private ArrayList submissionTypes;

    /** collection object which holds the available protocol submission type
        qualifiers */
    private ArrayList typeQualifiers;

    /** collection object which holds the available protocol review types */
    private ArrayList reviewTypes;

    /** collection object which holds the available protocol reviewer types */
    private ArrayList reviewerTypes;

    /** holds the sequence number of protocol extracted from protocolInfo */
    private int seqNo;

     /** collection object which holds the list of recommended committees where
        this particular protocol can be submitted. */
    private ArrayList committeeList;
    
    //private ProtocolSubmissionInfoBean submissionBean ;
        /** collection which stores all the  selected reviewers */
    private ArrayList reviewersList;
    
    /** collection which stores all active reviewers */
    private ArrayList availableReviewersList;
    
    /** integer used as loop variable*/
    private int revIndex;

    /** boolean value which specifies whether the protocol has been successfully
       submitted or not */
    private boolean dataSaved;

    /** boolean value which specifies any changes has been made to
       reviewer table */
    private boolean reviewersModified;


    /** boolean value used in identifying existing reviewers */
    private boolean found;
    private boolean identifyReviewers = true;
    
     //prps start - jul 21 2003
    // this mode will be of three types - OPTIONAL, MANDATORY, DENIED
    //OPTIONAL -  it is optional to select a committee & schedule before submission
    //MANDATORY - it is mandatory to select a commmitee & schedule before submission
    //DENIED - user will not see the committee & schedule selection screen but will be able
    // to submit the protocol
    private char submissionMode = 'O' ;
    private static final char OPTIONAL = 'O' ;
    private static final char MANDATORY = 'M' ;
    private static final char DENIED = 'D' ;
    private static final char VIEW = 'V' ;
    
    /* Case 646  - prahalad start Mar 12 2004  */
    
    private static final char EDIT =  'M' ;  // 'E' ; 
    /* Case 646  - prahalad start Mar 12 2004  */
    //prps end
    
    DateUtils dtUtils = new DateUtils() ;
    
    // this will have the total number of submission on a protocol
    private int rowCount = 0 ;
    // current submission record being displayed
    private int curRecord = 0 ; 
    
    // this is the list of submission status. If the current record has one of this status then allow submission from this screen
    Vector vecNotAllowedList ;
    
    private Vector vecSelectedReviewers ;
    // Added for COEUQA-3012:Need notification for when a reviewer completes their review in  IACUC - start  
    private Vector vecReviewer;    
    private Vector vecDefaultReviewer; 
    // Added for COEUQA-3012 - end
    
    //HashMap hashRow ;
    
    char functionMode ; //function mode 'view' is from protocolActions table and 'edit' is from submissiondetails
    
    int setRecord ;
    
    //Added By sharath for CheckList - START
    private static final String EXPEDITED = "Expedited";
    private static final String EXEMPT = "Exempt";
    
    private final String LOSE_CHECKED_LIST_INFO = 
        coeusMessageResources.parseMessageKey("checkList_confirmation_exceptionCode.1118");
    private final String SELECT_ATLEAST_ONE_CHECKLIST = 
        coeusMessageResources.parseMessageKey("checkList_mandatory_exceptionCode.1119");

    private static final String CHECK_LIST_FOR = "Checklist for ";
    private String selectedItem = null;
    private int selectedIndex;
    private Vector vecExpedited, vecExempt, vecExpeditedToModify, vecExemptToModify;
    private CheckList checkList = new CheckList(CoeusGuiConstants.getMDIForm(), true);
    //Added By sharath for CheckList - END
    
    //added by ravi for validating max protocols submitted for a schedule.
    private int maxProtocols;
    private boolean checkScheduleLoad;
    //end of add by ravi
    
    private boolean triggerCheckListWarning = true;
    
    private int submissionNo;
    private boolean commentsLocked;
    private boolean releaseScheduleLock;
    
    private String scheduleNumber = "";
    
     /* Case 646  - prahalad start Mar 12 2004  */
    
    private char screenMode ;
    /* Case 646  - prahalad End Mar 12 2004  */
	 private Vector vecAction = new Vector();
	 

    //Case #2080 Start 2
    private ProtocolActionsForm protocolActionForm = new ProtocolActionsForm();
    private Vector vecActionDateData;
    //COEUSQA:3187 - Error when correcting Action Date on the Submission Details window - Start
    private boolean isInValidActionDate = false;
    //COEUSQA:3187 - End
    //Added for case#3088 - Error updating review comments
    private static final String SCHEDULE_MAINTENENCE_SERVLET = "/scheduleMaintSrvlt";
    //Code added for Case#3554 - Notify IRB enhancement
    private static final String STREMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
    private boolean windowOpen = true;
    private ComboBoxBean comboBean = null;
    private static final String FYI = "112";
    // Added for COEUQA-3012: Notification  for when a reviewer completes their review in IRB - start
    private static final int REVIEWER_REVIEW_COMPLETE_NOTIFICATION_CODE = 405;
    // Added for COEUQA-3012 - end
    //Case #2080 End 2
    //Added with Case 3283: Reviewer Notification
    private boolean reviewerPersonsChanged;
    //3283 End
    // 3282: reviewer view of protocols - Start
    private Vector recommendedActionTypes;
    private JCheckBox chkReviewComplete,chkTmpReviewComplete;
    // 3282: reviewer view of protocols - End
    //
   /** Creates new form sample */
    public SubmissionDetailsForm(Frame parent, String title, String protocolId, boolean modal, 
       final Vector vSubmissionDetails, Vector vecSelectedReviewer, 
            char functionMode, int setRecord ) 
    {
        //super(parent, title, modal);
        
        this.parent = parent ;
        this.title = title ;
        this.vecSubmissionDetails = (Vector)vSubmissionDetails.get(0);
		this.vecAction = (Vector)vSubmissionDetails.get(1);
        this.protocolId = protocolId;
        
        //this.vecSelectedReviewers = vecSelectedReviewers ;
        
        this.vecDefaultReviewer = vecSelectedReviewer;
        
        this.setRecord = setRecord ;
        
        vecNotAllowedList = new Vector() ;
        vecNotAllowedList.add(new String("200") ) ; // Approved
        
        rowCount = vecSubmissionDetails.size() ;
        // this screen might not display the latest sub num data by default
        if (setRecord == -1)
        {   // set the current record as the latest  submission
            curRecord = rowCount -1 ;
        }
        else
        { // the setRecord obtained here is the submission number
          // as the current submission is to be dislayed 
            curRecord = getCurrentRecord(setRecord) ;
        }
        
            
        // if this screen is opened in View mode then there is no need to check if the user
        // has rights to modify. If it is opened in Edit mode then perform check
        this.functionMode = functionMode ;
        
        /* Case 646  - prahalad start Mar 12 2004  */
        // assign this only once this mode will not change every time user clicks prev or next btn
        this.screenMode = functionMode ;
        
        // prps commented this - Case 646
//        if (functionMode == EDIT)
//        {    
//           this.functionMode = checkUserCanEditSubmissionDetails()== true?EDIT:VIEW ;
//        }
        
        /* Case 646  - prahalad end Mar 12 2004  */
        
         
        
        
        initComponents();
        //prps start - Apr 29 2004
        txtSubmissionStatusCode.setVisible(false) ;
        //prps end - Apr 29 2004
        
        // added by manoj for focus traverse among components 16/09/2003
        // starts
        java.awt.Component[] components = {cmbSubmissionType,cmbReviewType,cmbTypeQualifier,
        btnCommittee,btnSchedule,btnCheckList,btnPrev,btnNext,btnReviewComments,btnViewAttachment,btnSubmit,btnCancel};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        pnlMain.setFocusTraversalPolicy(traversePolicy);
        pnlMain.setFocusCycleRoot(true);
        // ends
		 //Added by Jobin - start
//        ProtocolActionsForm protocolActionForm = new ProtocolActionsForm();
//		protocolActionForm.showActions(vecAction);
//		scrpneActions.setViewportView(protocolActionForm.showProtocolActionsForm(CoeusGuiConstants.getMDIForm())); //Added by Jobin - end
		
        setDefaultData() ;
        setReviewerTableModel();
        tblAvailableReviewers.getTableHeader().setFont(
                                    CoeusFontFactory.getLabelFont());
        tblSelectedReviewers.getTableHeader().setFont(
                                    CoeusFontFactory.getLabelFont());
        
         showRecord(curRecord ) ; // show the max submission record first  
         
//        traversePolicy.getFirstComponent(pnlMain).requestFocusInWindow(); 
         //added by Nadh to make btnSubmit button enable 
         //start 9 aug 2004
         registerKeyListeners();
         //end Nadh 9 aug 2004
         saveRequired = false ;
           
    }
	//Added by Jobin - start
    /**
	 * To set the value for the table 
	 */
	public void commAction(Vector vecAction){
		this.vecAction = vecAction;
	}
	//Added by Jobin - end
	
    //prps start - jan 13 2004
    // This method will return the index of the vector corresponding to the current vector
	
    private int getCurrentRecord(int submissionNumber)
    {
        int recIdx = 0 ;
        for (int idx = 0 ; idx < vecSubmissionDetails.size() ; idx++)
        {    
            ProtocolSubmissionInfoBean validateSubmissionInfoBean 
                        = (ProtocolSubmissionInfoBean)vecSubmissionDetails.get(idx);
            
            if (validateSubmissionInfoBean.getSubmissionNumber() == submissionNumber)
            {
                recIdx = idx ;
            }    
        }
        return recIdx ;
    }
    
    //prps end - jan 13 2004
    
    
    private void showRecord(int recNumber)
    {
    initialiseData(recNumber) ;
        /* Case 646  - prahalad start Mar 12 2004  */
        // if the screen opened in edit mode then check this        
        if (screenMode == EDIT)
        {    
           this.functionMode = checkUserCanEditSubmissionDetails()== true?EDIT:VIEW ;
        }
        /* Case 646  - prahalad start Mar 12 2004  */
        controlEditingnNavigation() ;
        reviewCommentsForm.setSaveToDatabase(false);
    }
    
   // there is no need to pass userid as an agrument as AppletServletCommunicator will  
   // add the userid/Name to the request object. 
    private boolean checkUserCanEditSubmissionDetails() 
    {
        String connectTo = CoeusGuiConstants.CONNECTION_URL
                + "/SubmissionDetailsServlet";
          
        //prps added this on jan 08 2003
        ProtocolSubmissionInfoBean validateSubmissionInfoBean 
                = (ProtocolSubmissionInfoBean)vecSubmissionDetails.get(curRecord);
        
        
        RequesterBean request = new RequesterBean();
        request.setFunctionType('R');
        request.setId(protocolId);
        request.setDataObject(validateSubmissionInfoBean) ;
        AppletServletCommunicator comm
            = new AppletServletCommunicator(connectTo, request);

        setCursor( new Cursor( Cursor.WAIT_CURSOR ) );        
        comm.send();
        ResponderBean response = comm.getResponse();
        setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );

        if (!response.isSuccessfulResponse()) 
        {
            return false;
        }
        return true ;
    }
    
    private void formatFields( boolean enable) {
            if(enable)
            {
                //for selectReviewers
                tblSelectedReviewers.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
                //tblSelectedReviewers.setSelectionBackground((Color) UIManager.getDefaults().get("Panel.background"));
                tblSelectedReviewers.setSelectionForeground(Color.black);
                
                //for availableReviewers
                tblAvailableReviewers.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
                //tblAvailableReviewers.setSelectionBackground((Color) UIManager.getDefaults().get("Panel.background"));
                tblAvailableReviewers.setSelectionForeground(Color.black);
                /* Added for enhancements to Modify Submission details after submission is complete 
                * to Voting comments and vote counts editable - by Nadh
                * Start 9 aug 2004
                */
                txtSubmissionYesVoteCount.setOpaque(true);
                txtSubmissionNoVoteCount.setOpaque(true);
                txtAbstainerCount.setOpaque(true);
                txtVotingComments.setOpaque(true);
               // end Nadh 9 aug 2004
                
            }
            btnSubmit.setEnabled( !enable ) ;
            //btnPrev.setVisible(true) ;
            //btnNext.setVisible(true) ;
            btnCancel.setVisible(enable) ;
            cmbReviewType.setEnabled(enable) ;
            cmbSubmissionType.setEnabled(enable) ;
            cmbTypeQualifier.setEnabled(enable) ;
            btnCommittee.setEnabled(enable) ;
            btnSchedule.setEnabled(enable) ;
            tblAvailableReviewers.setEnabled(enable) ;
            tblSelectedReviewers.setEnabled(enable) ;
            btnDelete.setEnabled(enable) ;
            btnAdd.setEnabled(enable) ;
            /* Added for enhancements to Modify Submission details after submission is complete 
            * to make Voting comments and vote counts editable - by Nadh
            * Start 9 aug 2004
            */
            txtSubmissionYesVoteCount.setEnabled(enable) ;
            txtSubmissionNoVoteCount.setEnabled(enable) ;
            txtAbstainerCount.setEnabled(enable) ;
            txtVotingComments.setEnabled(enable) ;
            //end Nadh 9 aug 2004
            if( enable ) {
                tblSelectedReviewers.setBackground(Color.white);
                //tblSelectedReviewers.setSelectionBackground((Color) UIManager.getDefaults().get("Panel.background"));
                tblSelectedReviewers.setSelectionForeground(Color.white);
                
                //for availableReviewers
                tblAvailableReviewers.setBackground(Color.white);
                //tblAvailableReviewers.setSelectionBackground((Color) UIManager.getDefaults().get("Panel.background"));
                tblAvailableReviewers.setSelectionForeground(Color.white);
                
                RequesterBean requesterBean = new RequesterBean();  
                ProtocolSubmissionInfoBean protocolSubmissionInfoBean = 
                    (ProtocolSubmissionInfoBean)vecSubmissionDetails.get(curRecord);
                
                requesterBean.setDataObject(protocolSubmissionInfoBean);
                requesterBean.setFunctionType('T');
                AppletServletCommunicator comm = new AppletServletCommunicator(
                    CoeusGuiConstants.CONNECTION_URL + "/scheduleMaintSrvlt", 
                    requesterBean);
                comm.send();
                ResponderBean responderBean = comm.getResponse();
                if(! responderBean.isSuccessfulResponse()){
                   reviewCommentsForm.setFunctionType(TypeConstants.DISPLAY_MODE); 
                }else {
                   reviewCommentsForm.setFunctionType(TypeConstants.MODIFY_MODE); 
                }
//                cmbSubmissionType.requestFocusInWindow();
            }else{
                reviewCommentsForm.setFunctionType(TypeConstants.DISPLAY_MODE);
//                btnSubmit.requestFocusInWindow();
            }
    }
    
    private void controlEditingnNavigation()
    {   // enable this btn only if ther r any changes
        btnSubmit.setEnabled(false) ;
        
              if( functionMode == EDIT ) {
                formatFields( true );  
              }else{
                formatFields( false );
              }
        
        // this is the navigation stuff - will be same for everything
        if (rowCount == 1) // just one record
        {
            btnPrev.setEnabled(false) ;
            btnNext.setEnabled(false) ;
        }
        else if (curRecord == rowCount - 1 ) // first record shown
        {
            btnNext.setEnabled(false) ;
            btnPrev.setEnabled(true) ;
        }
        else if (curRecord == 0)
        {
            btnPrev.setEnabled(false) ; // last one shown
            btnNext.setEnabled(true) ;
        }    
        else
        {
            btnPrev.setEnabled(true) ;
            btnNext.setEnabled(true) ;
        }
    }
    
        private void setReviewerTableFormat(){
        tblAvailableReviewers.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION);
        tblAvailableReviewers.getTableHeader().setReorderingAllowed(false);
        tblAvailableReviewers.getTableHeader().setResizingAllowed(false);
        
        tblAvailableReviewers.setFont(CoeusFontFactory.getNormalFont());
        
        tblSelectedReviewers.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION);
        tblSelectedReviewers.getTableHeader().setReorderingAllowed(false);
        tblSelectedReviewers.setFont(CoeusFontFactory.getNormalFont());
        
        //code to to make resize work properly...for selectedReviewers.
        tblSelectedReviewers.getTableHeader().setResizingAllowed(true);
        tblSelectedReviewers.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        // 3282: Reviewer View of Protocol materials - Start
//        tblSelectedReviewers.getTableHeader().setPreferredSize(new Dimension(0, 30));
//        TableColumn column = tblSelectedReviewers.getColumn("Reviewer Type");
//        column.setMinWidth(100);
        SelectedReviewersTableHeaderRenderer selectedReviewersTableHeaderRenderer = new SelectedReviewersTableHeaderRenderer();
        
        TableColumn column = tblSelectedReviewers.getColumnModel().getColumn(3);
        column.setMinWidth(70);
        column.setPreferredWidth(70);
        column.setHeaderValue("<html>Reviewer<br> Type</html>");
        column.setHeaderRenderer(selectedReviewersTableHeaderRenderer);
        
        /* setting CoeusComboBox as editor to Reviewer Type column */
        if(reviewerTypes != null && reviewerTypes.size()>0){
            JComboBox  coeusCombo = new JComboBox(getReviewerTypes());
            coeusCombo.addItemListener(new ItemListener(){
                public void itemStateChanged(ItemEvent ie){
//                  modified by ravi for enabling OK button if reviewer type is changed  
//                    saveRequired = true;
                    recordChanged();
//                    end of modified by ravi
                }
            });
            coeusCombo.setFont(CoeusFontFactory.getNormalFont());
            column.setCellEditor(new DefaultCellEditor(coeusCombo ));
        }
//        column = tblSelectedReviewers.getColumn("Name");
//        column.setMinWidth(80);
        
        column = tblAvailableReviewers.getColumn("Id");
        column.setMaxWidth(0);
        column.setMinWidth(0);
        column.setPreferredWidth(0);

        column = tblAvailableReviewers.getColumn("Emp");
        column.setMaxWidth(0);
        column.setMinWidth(0);
        column.setPreferredWidth(0);
//        
//        column = tblSelectedReviewers.getColumn("Emp");
//        column.setMaxWidth(0);
//        column.setMinWidth(0);
//        column.setPreferredWidth(0);
//        
//        column = tblSelectedReviewers.getColumn("Id");
//        column.setMaxWidth(0);
//        column.setMinWidth(0);
//        column.setPreferredWidth(0);
        
        column = tblSelectedReviewers.getColumnModel().getColumn(0);
        column.setMaxWidth(0);
        column.setMinWidth(0);
        column.setPreferredWidth(0);
        
        column = tblSelectedReviewers.getColumnModel().getColumn(1);
        column.setMaxWidth(65);
        column.setMinWidth(65);
        column.setPreferredWidth(65);
        column.setHeaderRenderer(selectedReviewersTableHeaderRenderer);
        column.setHeaderValue("<html>Review<br>Complete</html>");
        column.setHeaderRenderer(selectedReviewersTableHeaderRenderer);
        chkReviewComplete = new  JCheckBox();
        column.setCellEditor(new DefaultCellEditor( chkReviewComplete){
            
            public Component getTableCellEditorComponent(JTable table, Object value,
                    boolean isSelected,
                    int row, int column) {
                
                chkReviewComplete.setSelected(((Boolean)(value)).booleanValue());
                chkReviewComplete.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                return chkReviewComplete;
                
            }
            
        }
        );
        chkTmpReviewComplete = new  JCheckBox();
        column.setCellRenderer(new DefaultTableCellRenderer( ){
            public Component getTableCellRendererComponent(JTable table,Object value,
                    boolean isSelected,boolean hasFocus, int row, int col) {
                
                chkTmpReviewComplete.setSelected(((Boolean)(value)).booleanValue());
                chkTmpReviewComplete.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                return chkTmpReviewComplete;
            }
        }
        );

        column = tblSelectedReviewers.getColumnModel().getColumn(2);        
        column.setMinWidth(115);
        column.setPreferredWidth(115);
        column.setHeaderRenderer(selectedReviewersTableHeaderRenderer);
        
        column = tblSelectedReviewers.getColumnModel().getColumn(4);
        column.setMaxWidth(80);
        column.setMinWidth(80);
        column.setPreferredWidth(80);
        column.setHeaderValue("<html>Assigned<br> Date</html>");
        column.setHeaderRenderer(selectedReviewersTableHeaderRenderer);
        
        column = tblSelectedReviewers.getColumnModel().getColumn(5);
        column.setMaxWidth(80);
        column.setMinWidth(80);
        column.setPreferredWidth(80);
        column.setHeaderRenderer(selectedReviewersTableHeaderRenderer);
        
        column = tblSelectedReviewers.getColumnModel().getColumn(6);
//        column.setMaxWidth(150);
        column.setMinWidth(150);
        column.setPreferredWidth(150);
        column.setHeaderRenderer(selectedReviewersTableHeaderRenderer);
        
        if(recommendedActionTypes != null && recommendedActionTypes.size()>0){
            JComboBox  coeusCombo = new JComboBox(recommendedActionTypes);
            coeusCombo.addItemListener(new ItemListener(){
                public void itemStateChanged(ItemEvent ie){
                    saveRequired = true;
                }
            });
            coeusCombo.setFont(CoeusFontFactory.getNormalFont());
            column.setCellEditor(new DefaultCellEditor(coeusCombo ));
        }
        
        column = tblSelectedReviewers.getColumnModel().getColumn(7);
        column.setMaxWidth(0);
        column.setMinWidth(0);
        column.setPreferredWidth(0);
        // 3282: Reviewer View of Protocol materials - End
        
    }
    
    
    
    private void setReviewerFormData(){
        
        if(availableReviewersList != null){
            ((DefaultTableModel)tblAvailableReviewers.getModel()).setDataVector(
                constructAvailRevTableData(),getAvailRevColumnNames());

            ((DefaultTableModel)
                tblAvailableReviewers.getModel()).fireTableDataChanged();
        }
            
        if( reviewersList != null ) {
            ((DefaultTableModel)tblSelectedReviewers.getModel()).setDataVector(
                constructSelRevTableData(),getSelRevColumnNames());

            ((DefaultTableModel)
                tblSelectedReviewers.getModel()).fireTableDataChanged();
        }
        int availRevCount = tblAvailableReviewers.getRowCount();
        int selRevCount = tblSelectedReviewers.getRowCount();
        if( availRevCount > 0 && selRevCount > 0 ){
            
            for(int selRevIndex = 0; selRevIndex < selRevCount; selRevIndex++){
                String selPersonId = (String)
                    tblSelectedReviewers.getValueAt(selRevIndex,0);
                availRevCount = tblAvailableReviewers.getRowCount();
                for(int availRevIndex = 0; availRevIndex < availRevCount ;
                     availRevIndex++){
                    String availPersonId = (String)
                        tblAvailableReviewers.getValueAt(availRevIndex,0);
                        
                    if(selPersonId.equals(availPersonId)){
                        ((DefaultTableModel)
                            tblAvailableReviewers.getModel()).removeRow(
                                availRevIndex);
                        availRevCount--;
                    }
                }
            }
                        
        }
    
    }
    
    
    private void setReviewerTableModel(){
           // 3282: Reviewer View of Protocol materials - Start
//        tblSelectedReviewers.setModel(new javax.swing.table.DefaultTableModel(
//        new Object [][] {},
//        new String [] {
//            "Id","Name", "Reviewer Type","Emp"
//        }
//        ) {
//            boolean[] canEdit = new boolean [] {false, false, true,false};
//            
//            public boolean isCellEditable(int rowIndex, int columnIndex) {
//                return canEdit [columnIndex];
//            }
//        });
        
        
        tblSelectedReviewers.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {},
                new String [] {
            "Id","Review Complete","Name", "Reviewer Type","Assigned Date","Due Date","Recommend Action","Emp"
        }
        ) {
            boolean[] canEdit = new boolean [] {false, true, false, true, true, true, true, false};
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
            public void setValueAt(Object value, int row, int column) {
                
                DateUtils dateUtils = new DateUtils();
                Date date       = null;
                String strDate  = null;
                SimpleDateFormat simpleDateFormat;
                DefaultTableModel tableModel = (DefaultTableModel)tblSelectedReviewers.getModel();
                //Modified for COEUSDEV-237 : Investigator cannot see review comments - Start
//                Vector vecData =(Vector) tableModel.getDataVector().elementAt(row);
                Vector vecReviewData = (Vector)tableModel.getDataVector();
                Vector vecData = new Vector();
                if(vecReviewData != null && vecReviewData.size()>0 && row < vecReviewData.size()){
                    vecData = (Vector)vecReviewData.elementAt(row);
                //COEUSDEV-237 : End    
                    switch(column) {
                        
                        case 4 :
                        case 5 :
                            if (value.toString().trim().length() > 0) {
                                strDate = dateUtils.formatDate(value.toString().trim(), CoeusGuiConstants.DATE_SEPARATORS, CoeusGuiConstants.UI_DATE_FORMAT);
                                strDate = dateUtils.restoreDate(strDate, CoeusGuiConstants.DATE_SEPARATORS);
                                if(strDate==null) {
                                    CoeusOptionPane.showErrorDialog("Please enter a valid Date");
                                }else{
                                    vecData.setElementAt(strDate, column);
                                    updateRowInTable(tblSelectedReviewers,vecData);
                                }
                            } else{
                                vecData.setElementAt("", column);
                                updateRowInTable(tblSelectedReviewers,vecData);
                            }
                            break;
                        default:
                            vecData.setElementAt(value, column);
                            updateRowInTable(tblSelectedReviewers,vecData);
                    }
                }
            }
            
        });
        // 3282: Reviewer View of Protocol materials - End
        
        tblAvailableReviewers.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {},
        new String [] {
            "Id", "Name","Emp"
        }
        ) {
            boolean[] canEdit = new boolean [] {false, false,false};
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
    
    }
    
    
   /**
     * Supporting method used to get the column names of available reveviewers 
     * table 
     * @return  colNames Vector which consists of column header names.
     */    
    
    private Vector getAvailRevColumnNames(){
        
        Enumeration enumColumns 
                = tblAvailableReviewers.getColumnModel().getColumns();
        Vector colNames = new Vector();
        while( enumColumns.hasMoreElements()){
            colNames.addElement(((TableColumn)
                enumColumns.nextElement()).getHeaderValue().toString());
        }
        return colNames;
    }
    
    /**
     * Supporting method which constructs vector of vectors from the 
     * collection of CommitteeMembershipDetailsBean.
     * @return  availRevTableData vector which will be used in displaying 
     * available reviewers table data
     */    
    
    private Vector constructAvailRevTableData() {
        /** holds the values to be displayed in available reveiwers table */
        Vector availRevTableData;

        availRevTableData = new Vector();
        CommitteeMembershipDetailsBean memberBean;
        Vector tableRowData;
        int availRevSize = availableReviewersList.size();
        for( int rowIndex = 0 ; rowIndex < availRevSize ; rowIndex++ ) {
                    
            /* extract person name from bean and construct
             * vector of vectors.
             */
            memberBean = ( CommitteeMembershipDetailsBean )
                    availableReviewersList.get( rowIndex );
            tableRowData = new Vector();
            tableRowData.addElement(memberBean.getPersonId());
            tableRowData.addElement(memberBean.getPersonName());
            tableRowData.addElement(new Boolean(
                (memberBean.getNonEmployeeFlag() == 'y' || 
                memberBean.getNonEmployeeFlag() == 'Y') ? true : false) );
            availRevTableData.addElement(tableRowData);
        }
        return availRevTableData;
    }
    
    

    /**
     * Supporting method used to get the column names of selected reveviewers 
     * table 
     * @return  colNames vector which consists of column header names.
     */    
    
    private Vector getSelRevColumnNames(){
        
        Enumeration enumColumns 
            = tblSelectedReviewers.getColumnModel().getColumns();
        Vector colNames = new Vector();
        while( enumColumns.hasMoreElements()){
            colNames.addElement(((TableColumn)
                enumColumns.nextElement()).getHeaderValue().toString());
        }
        return colNames;
    }
    
    /**
     * Supporting method which constructs vector of vectors from
     * the collection of ProtocolReviewerInfoBean.
     * @return  selRevTableData vector which will be used in displaying 
     * selected reviewers table data
     */    
    
    private Vector constructSelRevTableData() {
        /** holds the values to be displayed in selected reveiwers table */
        Vector selRevTableData;
        DateUtils dateUtils = new DateUtils();
        selRevTableData = new Vector();
        ProtocolReviewerInfoBean reviewerBean;
        Vector tableRowData;
        int revSize = reviewersList.size();
        for( int rowIndex = 0 ; rowIndex < revSize; rowIndex++ ) {
            /* extract person name and reviewer type from bean and construct
             * double dimensional object array.
             */
            reviewerBean = ( ProtocolReviewerInfoBean )
                    reviewersList.get( rowIndex );
            tableRowData = new Vector();
            tableRowData.addElement(reviewerBean.getPersonId());
            tableRowData.addElement(new Boolean(reviewerBean.isReviewComplete()));
            tableRowData.addElement(reviewerBean.getPersonName());
            tableRowData.addElement(reviewerBean.getReviewerTypeDesc());
            // 3282: Reviewer View of Protocol materials -Start
            if(reviewerBean.getAssignedDate() != null){
                String strAssignedDate = reviewerBean.getAssignedDate().toString();
                tableRowData.addElement(dateUtils.formatDate(strAssignedDate,CoeusGuiConstants.DEFAULT_DATE_FORMAT));
            }else{
                tableRowData.addElement("");
            }
            if(reviewerBean.getDueDate() != null){
                String strDueDate = reviewerBean.getDueDate().toString();
                tableRowData.addElement(dateUtils.formatDate(strDueDate,CoeusGuiConstants.DEFAULT_DATE_FORMAT));
            }else{
                tableRowData.addElement("");
            }
              
            ComboBoxBean cmbBean = null;
            boolean recommendedActionPresent = false;
            for(int index = 0; index < recommendedActionTypes.size(); index++){
                cmbBean= (ComboBoxBean) recommendedActionTypes .get(index);
                if(reviewerBean.getRecommendedActionCode().equalsIgnoreCase(cmbBean.getCode())){
                    recommendedActionPresent = true;
                    break;
                }
            }
            if(recommendedActionPresent){
                tableRowData.addElement(cmbBean);
            } else {
                tableRowData.addElement(new ComboBoxBean("",""));
            }
            // 3282: Reviewer View of Protocol materials - End
            tableRowData.addElement(new Boolean(reviewerBean.isNonEmployee()) );
            selRevTableData.addElement(tableRowData);
        }
        return selRevTableData;
    }
    
    
    /** This method is used to set the available Committee Members who are
     * eligible to become Reviewers.
     *
     * @param availableReviewers ArrayList which consits of collection of
     * <CODE>ProtocolReviewerInfoBean</CODE>.
     */
    public void setAvailableReviewers(ArrayList availableReviewers){
        this.availableReviewersList = availableReviewers;
        setReviewerFormData() ;
        setReviewerTableFormat() ;
    }
    
    /** This method is used to set the available reviewer types for selected
     * reviewers.
     *
     * @param revTypes ArrayList which consists of collection of
     * <CODE>ComboBoxBean</CODE> with <CODE>reviewerTypeCode</CODE> and <CODE>reviewerTypeDescription</CODE> as values.
     */
    public void setReviewerTypes(ArrayList revTypes){
        this.reviewerTypes = revTypes;
        setReviewerTableFormat() ;
    }
    
    
    
    
    /**
     * This method returns the reviwer types
     * @return revTypes collection of ComboBoxBeans which consists of 
     * reviewer type descriptions
     */
    
    private Vector getReviewerTypes(){
    
        Vector revTypes = new Vector();
        int revSize = reviewerTypes.size();
        if(reviewerTypes != null){
            for(int index = 0; index < revSize; index++ ){
                revTypes.addElement(((ComboBoxBean)
                    reviewerTypes.get(index)).toString());
            }
        }

        return revTypes;
    
    }
    
    /**
     * This method is used to add a specified row in the specified table
     * in sorted order
     *
     * @param table JTable to which the specified row to be added
     * @param vtr Vector which consists of data that has to be added as a row in 
     * a table
     */
    private void insertRowInTable(JTable table, Vector vtr){
        DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
        Vector dVector = tableModel.getDataVector();
        int rowCount = dVector.size();
        int rowIndex;
        Vector vecElement;
        
        // find the position for inserting by looping through the list
        for(rowIndex =0;rowIndex<rowCount;rowIndex++){
            vecElement = (Vector)dVector.elementAt(rowIndex);
            if( ( ((String)vecElement.elementAt(0)).compareTo(
                    (String)vtr.elementAt(0))) < 0){
                continue;
            }
            else {
                break;
            }
            
        }
        saveRequired=true;
        tableModel.insertRow(rowIndex,vtr);
        tableModel.fireTableDataChanged();
    }
    
    //  3282: Reviewer View of Protocol materials - Start
    /**
     * Methos used to Update the data in the Data Vector od Table Model
     * @param JTable table
     * @param Vector vector
     * @return void
     */
    private void updateRowInTable(JTable table, Vector vector){
        DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
        Vector dVector = tableModel.getDataVector();
        int rowCount = dVector.size();
        int rowIndex;
        Vector vecElement;     
        
        for(rowIndex =0;rowIndex<rowCount;rowIndex++){
            vecElement = (Vector)dVector.elementAt(rowIndex);
            if( ( ((String)vecElement.elementAt(2)).compareTo(
                    (String)vector.elementAt(2))) == 0){
                tableModel.removeRow(rowIndex);
                tableModel.insertRow(rowIndex,vector);
            }             
        }
        tableModel.fireTableDataChanged();
        recordChanged();  
    }
    // 3282: Reviewer View of Protocol materials - End
    
    /** This method is used to get the Reviewers selected by the user
     *
     * @return Collection of vectors which is given by the table model.
     */
    public Vector getSelectedReviewers(){
        int selRowCount = tblSelectedReviewers.getRowCount();
        if(selRowCount > 0){
            return ((DefaultTableModel)
                tblSelectedReviewers.getModel()).getDataVector();
        }else{
            return null;
        }
    }
    
    
    /**
     * This method is used to get all the reviewers for a given schedule,
     * to display in the available reviewers table of ReviewerSelectionForm
     *
     * @param scheduleId represents the schedule id which is used to fetch all
     * active members of the committee for that schedule
     * @return schedules collection of ProtocolReviewerInfoBean
     */

    private Vector getAvailableReviewersForSchedule( String scheduleId ) {
        // If the schedule number is zero then get it from the instance value
        if(scheduleId== null || scheduleId.equals("")){
            this.scheduleNumber  = scheduleId;
        }
        Vector vecBeans = new Vector();
        String connectTo = CoeusGuiConstants.CONNECTION_URL
                + "/protocolSubSrvlt";
        RequesterBean request = new RequesterBean();
        request.setFunctionType(REVIEWER_SELECTION);
        request.setId(scheduleId);
        request.setDataObject(protocolId);
        AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, request);
        /**
         * Updated for REF ID :0003  Feb'21 2003.
         * Hour Glass implementation while DB Trsactions Wait
         * by Subramanya Feb' 21 2003
         */
        setCursor( new Cursor( Cursor.WAIT_CURSOR ) );        
        comm.send();
        ResponderBean response = comm.getResponse();
        setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );

        if (response.isSuccessfulResponse()) {
            vecBeans = (Vector)(
                (Vector) response.getDataObjects()).elementAt(0);
        }
        return vecBeans;

    }
    
    /**
     * This method is used to get the number of protocols submitted to a given
     * schedule.
     *
     * @param scheduleId String representing the schedule number to which the
     * protocol submission count is to be fetched.
     *
     * @return count which represents the number of protocols submitted to the
     * specified schedule.
     */
    private int getProtocolSubCount( String scheduleId ) {
        
        String connectTo = CoeusGuiConstants.CONNECTION_URL
        + "/protocolSubSrvlt";
        RequesterBean request = new RequesterBean();
        request.setFunctionType(PROTO_SUB_COUNT);
        request.setId(scheduleId);
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connectTo, request);
        setCursor( new Cursor( Cursor.WAIT_CURSOR ) );
        comm.send();
        ResponderBean response = comm.getResponse();
        setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
        
        Integer count = new Integer(0);
        if (response.isSuccessfulResponse()) {
            count = (Integer)((Vector) response.getDataObjects()).elementAt(0);
        }
        if(count != null){
            return count.intValue();
        }
        return 0;
    }
    

            
    /**
     * This method is used to get all the reviewers for a given schedule,
     * to display in the available reviewers table of ReviewerSelectionForm
     *
     * @param scheduleId represents the schedule id which is used to fetch all
     * active members of the committee for that schedule
     * @return schedules collection of ProtocolReviewerInfoBean
     */

    private Vector getAvailableReviewersForCommittee( String committeeId ) {

        Vector vecBeans = new Vector();
        String connectTo = CoeusGuiConstants.CONNECTION_URL
                + "/protocolSubSrvlt";
        RequesterBean request = new RequesterBean();
        request.setFunctionType('W');
        //request.setId(committeeId);
        Vector dataObject = new Vector();
        dataObject.addElement(committeeId);
        dataObject.addElement(protocolId);
        request.setDataObject(dataObject);
        AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, request);
        /**
         * Updated for REF ID :0003  Feb'21 2003.
         * Hour Glass implementation while DB Trsactions Wait
         * by Subramanya Feb' 21 2003
         */
        setCursor( new Cursor( Cursor.WAIT_CURSOR ) );        
        comm.send();
        ResponderBean response = comm.getResponse();
        setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );

        if (response.isSuccessfulResponse()) {
            vecBeans = (Vector)(
                (Vector) response.getDataObjects()).elementAt(0);
        }
        return vecBeans;

    }
    
    
    
    
    
     /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        pnlSubmission = new javax.swing.JPanel();
        lblSubmissionTypeCode = new javax.swing.JLabel();
        lblSubmissionTypeQualCode = new javax.swing.JLabel();
        lblSubmissionStatusCode = new javax.swing.JLabel();
        txtSubmissionStatusDesc = new javax.swing.JTextField();
        lblSubmissionDate = new javax.swing.JLabel();
        txtSubmissionDate = new javax.swing.JTextField();
        lblSubmissionYesVoteCount = new javax.swing.JLabel();
        txtSubmissionYesVoteCount = new javax.swing.JTextField();
        lblSubmissionVoteCount = new javax.swing.JLabel();
        txtSubmissionNoVoteCount = new javax.swing.JTextField();
        lblCommitteeId = new javax.swing.JLabel();
        txtCommiteeId = new javax.swing.JTextField();
        lblCommitteeName = new javax.swing.JLabel();
        txtCommitteeName = new javax.swing.JTextField();
        lblScheduleDate = new javax.swing.JLabel();
        txtScheduleDate = new javax.swing.JTextField();
        lblSchedulePlace = new javax.swing.JLabel();
        txtSchedulePlace = new javax.swing.JTextField();
        lblReviewType = new javax.swing.JLabel();
        lblSubmissionNoVoteCount = new javax.swing.JLabel();
        lblProtocolId = new javax.swing.JLabel();
        txtProtocolId = new edu.mit.coeus.utils.CoeusTextField();
        lblProtocolTitle = new javax.swing.JLabel();
        lblPI = new javax.swing.JLabel();
        txtPI = new edu.mit.coeus.utils.CoeusTextField();
        lblApplicationDate = new javax.swing.JLabel();
        txtApplicationDate = new edu.mit.coeus.utils.CoeusTextField();
        lblScheduleId = new javax.swing.JLabel();
        txtScheduleId = new edu.mit.coeus.utils.CoeusTextField();
        lblAbstainerCount = new javax.swing.JLabel();
        txtAbstainerCount = new edu.mit.coeus.utils.CoeusTextField();
        btnCommittee = new javax.swing.JButton();
        btnSchedule = new javax.swing.JButton();
        cmbSubmissionType = new edu.mit.coeus.utils.CoeusComboBox();
        cmbReviewType = new edu.mit.coeus.utils.CoeusComboBox();
        cmbTypeQualifier = new javax.swing.JComboBox();
        ProtocolDetails = new javax.swing.JLabel();
        lblSubmissionDetails = new javax.swing.JLabel();
        VotingDetails = new javax.swing.JLabel();
        scrPnCommentsContainer = new javax.swing.JScrollPane();
        txtVotingComments = new javax.swing.JTextArea();
        scrpnlProtocolTitle = new javax.swing.JScrollPane();
        txtProtocolTitle = new javax.swing.JTextArea();
        lblVotingComments = new javax.swing.JLabel();
        pnlReview = new javax.swing.JPanel();
        scrPnSelectedReviewers = new javax.swing.JScrollPane();
        tblSelectedReviewers = new javax.swing.JTable();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        scrPnAvailableReviewers = new javax.swing.JScrollPane();
        tblAvailableReviewers = new javax.swing.JTable();
        lblReviewers = new javax.swing.JLabel();
        txtSubmissionStatusCode = new edu.mit.coeus.utils.CoeusTextField();
        pnlBtn = new javax.swing.JPanel();
        btnCancel = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnSubmit = new javax.swing.JButton();
        btnCheckList = new javax.swing.JButton();
        btnReviewComments = new javax.swing.JButton();
        btnViewAttachment = new javax.swing.JButton();
        scrpneActions = new javax.swing.JScrollPane();
        lblActions = new javax.swing.JLabel();

        pnlMain.setLayout(new java.awt.GridBagLayout());

        pnlMain.setMinimumSize(new java.awt.Dimension(800, 700));
        pnlMain.setPreferredSize(new java.awt.Dimension(800, 700));
        pnlMain.setAutoscrolls(true);
        pnlSubmission.setLayout(new java.awt.GridBagLayout());

        pnlSubmission.setMinimumSize(new java.awt.Dimension(658, 605));
        pnlSubmission.setPreferredSize(new java.awt.Dimension(658, 605));
        lblSubmissionTypeCode.setFont(CoeusFontFactory.getLabelFont());
        lblSubmissionTypeCode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSubmissionTypeCode.setText("Submission Type :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 4);
        pnlSubmission.add(lblSubmissionTypeCode, gridBagConstraints);

        lblSubmissionTypeQualCode.setFont(CoeusFontFactory.getLabelFont());
        lblSubmissionTypeQualCode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSubmissionTypeQualCode.setText("Type Qual :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 3, 4);
        pnlSubmission.add(lblSubmissionTypeQualCode, gridBagConstraints);

        lblSubmissionStatusCode.setFont(CoeusFontFactory.getLabelFont());
        lblSubmissionStatusCode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSubmissionStatusCode.setText("Submission Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 3);
        pnlSubmission.add(lblSubmissionStatusCode, gridBagConstraints);

        txtSubmissionStatusDesc.setFont(CoeusFontFactory.getNormalFont());
        txtSubmissionStatusDesc.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtSubmissionStatusDesc.setEnabled(false);
        txtSubmissionStatusDesc.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.ipadx = 238;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlSubmission.add(txtSubmissionStatusDesc, gridBagConstraints);

        lblSubmissionDate.setFont(CoeusFontFactory.getLabelFont());
        lblSubmissionDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSubmissionDate.setText("Date :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 4, 4);
        pnlSubmission.add(lblSubmissionDate, gridBagConstraints);

        txtSubmissionDate.setFont(CoeusFontFactory.getNormalFont());
        txtSubmissionDate.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtSubmissionDate.setEnabled(false);
        txtSubmissionDate.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.ipadx = 120;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlSubmission.add(txtSubmissionDate, gridBagConstraints);

        lblSubmissionYesVoteCount.setFont(CoeusFontFactory.getLabelFont());
        lblSubmissionYesVoteCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSubmissionYesVoteCount.setText("Yes :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 70, 0, 0);
        pnlSubmission.add(lblSubmissionYesVoteCount, gridBagConstraints);

        txtSubmissionYesVoteCount.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,3));
        txtSubmissionYesVoteCount.setFont(CoeusFontFactory.getNormalFont());
        txtSubmissionYesVoteCount.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtSubmissionYesVoteCount.setEnabled(false);
        txtSubmissionYesVoteCount.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 100, 0, 0);
        pnlSubmission.add(txtSubmissionYesVoteCount, gridBagConstraints);

        lblSubmissionVoteCount.setFont(CoeusFontFactory.getLabelFont());
        lblSubmissionVoteCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSubmissionVoteCount.setText("Vote Count :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 1, 5);
        pnlSubmission.add(lblSubmissionVoteCount, gridBagConstraints);

        txtSubmissionNoVoteCount.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,3));
        txtSubmissionNoVoteCount.setFont(CoeusFontFactory.getNormalFont());
        txtSubmissionNoVoteCount.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtSubmissionNoVoteCount.setEnabled(false);
        txtSubmissionNoVoteCount.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 25, 0, 0);
        pnlSubmission.add(txtSubmissionNoVoteCount, gridBagConstraints);

        lblCommitteeId.setFont(CoeusFontFactory.getLabelFont());
        lblCommitteeId.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCommitteeId.setText("Committee Id :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 3, 4);
        pnlSubmission.add(lblCommitteeId, gridBagConstraints);

        txtCommiteeId.setFont(CoeusFontFactory.getNormalFont());
        txtCommiteeId.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtCommiteeId.setEnabled(false);
        txtCommiteeId.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.ipadx = 120;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlSubmission.add(txtCommiteeId, gridBagConstraints);

        lblCommitteeName.setFont(CoeusFontFactory.getLabelFont());
        lblCommitteeName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCommitteeName.setText("Committee Name :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 3, 4);
        pnlSubmission.add(lblCommitteeName, gridBagConstraints);

        txtCommitteeName.setFont(CoeusFontFactory.getNormalFont());
        txtCommitteeName.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtCommitteeName.setEnabled(false);
        txtCommitteeName.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 345;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlSubmission.add(txtCommitteeName, gridBagConstraints);

        lblScheduleDate.setFont(CoeusFontFactory.getLabelFont());
        lblScheduleDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblScheduleDate.setText("Schedule Date :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.ipadx = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 120, 0, 4);
        pnlSubmission.add(lblScheduleDate, gridBagConstraints);

        txtScheduleDate.setFont(CoeusFontFactory.getNormalFont());
        txtScheduleDate.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtScheduleDate.setEnabled(false);
        txtScheduleDate.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 120;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 225, 0, 5);
        pnlSubmission.add(txtScheduleDate, gridBagConstraints);

        lblSchedulePlace.setFont(CoeusFontFactory.getLabelFont());
        lblSchedulePlace.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSchedulePlace.setText("Place :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        pnlSubmission.add(lblSchedulePlace, gridBagConstraints);

        txtSchedulePlace.setFont(CoeusFontFactory.getNormalFont());
        txtSchedulePlace.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtSchedulePlace.setEnabled(false);
        txtSchedulePlace.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 120;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlSubmission.add(txtSchedulePlace, gridBagConstraints);

        lblReviewType.setFont(CoeusFontFactory.getLabelFont());
        lblReviewType.setText("Review Type :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        pnlSubmission.add(lblReviewType, gridBagConstraints);

        lblSubmissionNoVoteCount.setFont(CoeusFontFactory.getLabelFont());
        lblSubmissionNoVoteCount.setText("No :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 0);
        pnlSubmission.add(lblSubmissionNoVoteCount, gridBagConstraints);

        lblProtocolId.setFont(CoeusFontFactory.getLabelFont());
        lblProtocolId.setText("Protoocol ID :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 5, 4);
        pnlSubmission.add(lblProtocolId, gridBagConstraints);

        txtProtocolId.setEnabled(false);
        txtProtocolId.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 120;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlSubmission.add(txtProtocolId, gridBagConstraints);

        lblProtocolTitle.setFont(CoeusFontFactory.getLabelFont());
        lblProtocolTitle.setText("Protocol Title :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 43, 4);
        pnlSubmission.add(lblProtocolTitle, gridBagConstraints);

        lblPI.setFont(CoeusFontFactory.getLabelFont());
        lblPI.setText("PI :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 3, 5);
        pnlSubmission.add(lblPI, gridBagConstraints);

        txtPI.setEnabled(false);
        txtPI.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 120;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlSubmission.add(txtPI, gridBagConstraints);

        lblApplicationDate.setFont(CoeusFontFactory.getLabelFont());
        lblApplicationDate.setText("ApplicationDate :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        pnlSubmission.add(lblApplicationDate, gridBagConstraints);

        txtApplicationDate.setEnabled(false);
        txtApplicationDate.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 123;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlSubmission.add(txtApplicationDate, gridBagConstraints);

        lblScheduleId.setFont(CoeusFontFactory.getLabelFont());
        lblScheduleId.setText("Schedule Id :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 4);
        pnlSubmission.add(lblScheduleId, gridBagConstraints);

        txtScheduleId.setEnabled(false);
        txtScheduleId.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 120;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlSubmission.add(txtScheduleId, gridBagConstraints);

        lblAbstainerCount.setFont(CoeusFontFactory.getLabelFont());
        lblAbstainerCount.setText("Abstainer :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 144, 0, 0);
        pnlSubmission.add(lblAbstainerCount, gridBagConstraints);

        txtAbstainerCount.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,3));
        txtAbstainerCount.setEnabled(false);
        txtAbstainerCount.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 206, 0, 0);
        pnlSubmission.add(txtAbstainerCount, gridBagConstraints);

        btnCommittee.setFont(CoeusFontFactory.getLabelFont());
        btnCommittee.setText("Select Committee");
        btnCommittee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCommitteeSelect(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 1, 0);
        pnlSubmission.add(btnCommittee, gridBagConstraints);

        btnSchedule.setFont(CoeusFontFactory.getLabelFont());
        btnSchedule.setText("Select Schedule");
        btnSchedule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnScheduleSelect(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        pnlSubmission.add(btnSchedule, gridBagConstraints);

        cmbSubmissionType.setMinimumSize(new java.awt.Dimension(100, 20));
        cmbSubmissionType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbSubmissionTypeItemStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 114;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlSubmission.add(cmbSubmissionType, gridBagConstraints);

        cmbReviewType.setMinimumSize(new java.awt.Dimension(100, 20));
        cmbReviewType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbReviewTypeItemStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlSubmission.add(cmbReviewType, gridBagConstraints);

        cmbTypeQualifier.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTypeQualifierItemStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlSubmission.add(cmbTypeQualifier, gridBagConstraints);

        ProtocolDetails.setFont(CoeusFontFactory.getLabelFont());
        ProtocolDetails.setForeground(new java.awt.Color(0, 0, 204));
        ProtocolDetails.setText("Protocol Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlSubmission.add(ProtocolDetails, gridBagConstraints);

        lblSubmissionDetails.setFont(CoeusFontFactory.getLabelFont());
        lblSubmissionDetails.setForeground(new java.awt.Color(0, 0, 204));
        lblSubmissionDetails.setText("Submission Details :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        pnlSubmission.add(lblSubmissionDetails, gridBagConstraints);

        VotingDetails.setFont(CoeusFontFactory.getLabelFont());
        VotingDetails.setForeground(new java.awt.Color(0, 0, 204));
        VotingDetails.setText("Voting Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.ipady = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlSubmission.add(VotingDetails, gridBagConstraints);

        scrPnCommentsContainer.setMinimumSize(new java.awt.Dimension(423, 50));
        scrPnCommentsContainer.setPreferredSize(new java.awt.Dimension(423, 50));
        txtVotingComments.setDocument(new LimitedPlainDocument( 2000 ));
        txtVotingComments.setFont(CoeusFontFactory.getNormalFont());
        txtVotingComments.setLineWrap(true);
        txtVotingComments.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtVotingComments.setMinimumSize(new java.awt.Dimension(420, 20));
        txtVotingComments.setPreferredSize(new java.awt.Dimension(420, 20));
        txtVotingComments.setEnabled(false);
        txtVotingComments.setOpaque(false);
        scrPnCommentsContainer.setViewportView(txtVotingComments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        pnlSubmission.add(scrPnCommentsContainer, gridBagConstraints);

        txtProtocolTitle.setDocument(new LimitedPlainDocument( 2000 ));
        txtProtocolTitle.setFont(CoeusFontFactory.getNormalFont());
        txtProtocolTitle.setLineWrap(true);
        txtProtocolTitle.setRows(200);
        txtProtocolTitle.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtProtocolTitle.setPreferredSize(new java.awt.Dimension(420, 3400));
        txtProtocolTitle.setEnabled(false);
        txtProtocolTitle.setOpaque(false);
        scrpnlProtocolTitle.setViewportView(txtProtocolTitle);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 1, 0);
        pnlSubmission.add(scrpnlProtocolTitle, gridBagConstraints);

        lblVotingComments.setFont(CoeusFontFactory.getLabelFont());
        lblVotingComments.setText("Comments :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 5);
        pnlSubmission.add(lblVotingComments, gridBagConstraints);

        pnlReview.setLayout(new java.awt.GridBagLayout());

        pnlReview.setPreferredSize(new java.awt.Dimension(600, 600));
        scrPnSelectedReviewers.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Selected Reviewers", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        scrPnSelectedReviewers.setMinimumSize(new java.awt.Dimension(200, 140));
        scrPnSelectedReviewers.setPreferredSize(new java.awt.Dimension(200, 140));
        tblSelectedReviewers.setFont(CoeusFontFactory.getLabelFont());
        scrPnSelectedReviewers.setViewportView(tblSelectedReviewers);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 175;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlReview.add(scrPnSelectedReviewers, gridBagConstraints);

        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setText("<<");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(50, 10, 0, 10);
        pnlReview.add(btnAdd, gridBagConstraints);

        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setText(">>");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 50, 10);
        pnlReview.add(btnDelete, gridBagConstraints);

        scrPnAvailableReviewers.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Available Reviewers", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        scrPnAvailableReviewers.setPreferredSize(new java.awt.Dimension(100, 140));
        tblAvailableReviewers.setFont(CoeusFontFactory.getLabelFont());
        scrPnAvailableReviewers.setViewportView(tblAvailableReviewers);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 175;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlReview.add(scrPnAvailableReviewers, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlSubmission.add(pnlReview, gridBagConstraints);

        lblReviewers.setFont(CoeusFontFactory.getLabelFont());
        lblReviewers.setForeground(new java.awt.Color(0, 0, 204));
        lblReviewers.setText("Reviewers");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 4);
        pnlSubmission.add(lblReviewers, gridBagConstraints);

        txtSubmissionStatusCode.setEditable(false);
        txtSubmissionStatusCode.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlSubmission.add(txtSubmissionStatusCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        pnlMain.add(pnlSubmission, gridBagConstraints);

        pnlBtn.setLayout(new java.awt.GridBagLayout());

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        pnlBtn.add(btnCancel, gridBagConstraints);

        btnPrev.setFont(CoeusFontFactory.getLabelFont());
        btnPrev.setMnemonic('P');
        btnPrev.setText("Previous");
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        pnlBtn.add(btnPrev, gridBagConstraints);

        btnNext.setFont(CoeusFontFactory.getLabelFont());
        btnNext.setMnemonic('N');
        btnNext.setText("Next");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        pnlBtn.add(btnNext, gridBagConstraints);

        btnSubmit.setFont(CoeusFontFactory.getLabelFont());
        btnSubmit.setMnemonic('O');
        btnSubmit.setText("OK");
        btnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 41;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        pnlBtn.add(btnSubmit, gridBagConstraints);

        btnCheckList.setFont(CoeusFontFactory.getLabelFont());
        btnCheckList.setText("CheckList");
        btnCheckList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckListActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(70, 0, 2, 0);
        pnlBtn.add(btnCheckList, gridBagConstraints);

        btnReviewComments.setFont(CoeusFontFactory.getLabelFont());
        btnReviewComments.setMnemonic('R');
        btnReviewComments.setText("Review Comments");
        btnReviewComments.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReviewCommentsActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        pnlBtn.add(btnReviewComments, gridBagConstraints);

        btnViewAttachment.setFont(CoeusFontFactory.getLabelFont());
        btnViewAttachment.setMnemonic('V');
        btnViewAttachment.setText("View Attachment");
        btnViewAttachment.setEnabled(false);
        btnViewAttachment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewAttachmentActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        pnlBtn.add(btnViewAttachment, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = 95;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        pnlMain.add(pnlBtn, gridBagConstraints);

        scrpneActions.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        scrpneActions.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrpneActions.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrpneActions.setMinimumSize(new java.awt.Dimension(600, 100));
        scrpneActions.setPreferredSize(new java.awt.Dimension(600, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weighty = 1.0;
        pnlMain.add(scrpneActions, gridBagConstraints);

        lblActions.setFont(CoeusFontFactory.getLabelFont());
        lblActions.setForeground(new java.awt.Color(0, 0, 204));
        lblActions.setText("Action Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        pnlMain.add(lblActions, gridBagConstraints);

        getContentPane().add(pnlMain, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents

    private void btnViewAttachmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewAttachmentActionPerformed
        try {
// TODO add your handling code here:
            viewSubmissionDocument();
        } catch (Exception ex) {
            ex.printStackTrace();
            CoeusOptionPane.showErrorDialog(ex.getMessage());
        }
    }//GEN-LAST:event_btnViewAttachmentActionPerformed

    private void btnReviewCommentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReviewCommentsActionPerformed
        // Add your handling code here:
         //Case #2080 Start 6
           if(protocolActionForm.getTblActionForm().getEditorComponent()!= null){
                protocolActionForm.getTblActionForm().getCellEditor().stopCellEditing();
           }//Case #2080 Start 6
        if( reviewCommentsForm.getFunctionType() == TypeConstants.MODIFY_MODE && !commentsLocked ) {
            reviewCommentsForm.setLockSchedule( true );
        }else{
            reviewCommentsForm.setLockSchedule( false );
		}
        try{
            ProtocolSubmissionInfoBean protocolSubmissionInfoBean = 
                (ProtocolSubmissionInfoBean)vecSubmissionDetails.get(curRecord);    
            //Commented/Added for case#3088 - Error updating review comments - start
//            reviewCommentsForm.setFormData(submissionBean, 
//                (Vector)ObjectCloner.deepCopy(protocolSubmissionInfoBean.getProtocolReviewComments()));
            Vector vecReviewComments = getReviewComments();
            reviewCommentsForm.setFormData(submissionBean, 
                    (Vector)ObjectCloner.deepCopy(vecReviewComments.get(1)));
            //Commented/Added for case#3088 - Error updating review comments - end
            if( reviewCommentsForm.getFunctionType() == TypeConstants.MODIFY_MODE ) {
                commentsLocked = true;
            }
            reviewCommentsForm.display();
            if( reviewCommentsForm.isSaveRequired() ) {
                recordChanged();
                releaseScheduleLock = true;
                protocolSubmissionInfoBean.setProtocolReviewComments(reviewCommentsForm.getData() );
                vecSubmissionDetails.set(curRecord, protocolSubmissionInfoBean);
            }
        }catch(Exception ex) {
            CoeusOptionPane.showInfoDialog(ex.getMessage());
        }
    }//GEN-LAST:event_btnReviewCommentsActionPerformed

    private void btnCheckListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckListActionPerformed
        // Add your handling code here:
        //Added By sharath for CheckList - START
         //Case #2080 Start 6
           if(protocolActionForm.getTblActionForm().getEditorComponent()!= null){
                protocolActionForm.getTblActionForm().getCellEditor().stopCellEditing();
           }//Case #2080 Start 6
        String selectedItem = cmbReviewType.getSelectedItem().toString().trim();
        if(selectedItem.equalsIgnoreCase(EXPEDITED)){
            checkList.setFormData(vecExpedited);
            checkList.setTitle(CHECK_LIST_FOR + EXPEDITED);
        }
        else if(selectedItem.equalsIgnoreCase(EXEMPT)) {
            checkList.setFormData(vecExempt);
            checkList.setTitle(CHECK_LIST_FOR + EXEMPT);
        }
        
        if(cmbReviewType.isEnabled()) {
            checkList.setEditable(true);
        }else{
            checkList.setEditable(false);
        }
        
        int button = checkList.display();
        
        if(button == checkList.OK && checkList.isSaveRequired()) { 
            btnSubmit.setEnabled(true);
            saveRequired =true ;
            if(selectedItem.equalsIgnoreCase(EXPEDITED)){
                vecExpedited = checkList.getFormData();
            }else if(selectedItem.equalsIgnoreCase(EXEMPT)) {
                vecExempt = checkList.getFormData();
            }
        }
        //Added By sharath for CheckList - END
    }//GEN-LAST:event_btnCheckListActionPerformed
    
    //Added By sharath for CheckList - START
    /**
     * Prepares CheckList to be displayed by replacing the MasterData CheckListBeans 
     * with toModify CheckListBeans.
     */
    private void prepareCheckList(Vector masterData, Vector toModify) {
        if(toModify == null || toModify.size() < 1) return ;
        String code;
        toModify:for(int indexToModify = 0; indexToModify < toModify.size(); indexToModify++) {
            code = ((CheckListBean)toModify.get(indexToModify)).getCheckListCode();
            for(int index = 0; index < masterData.size(); index++) {
                if(code.equals( ((CheckListBean)masterData.get(index)).getCheckListCode()) ) {
                    masterData.set(index, toModify.get(indexToModify));
                    continue toModify;
                }
            }//For master Data
        }//For toModify
    }//End prepareCheckList(Vector masterData, Vector toModify)
    
    /**
     *Prepares Vector of ProtocolReviewTypeCheckListBean to Send to Server
     *by checking with MasterData and toModify and applying proper AC Types.
     */
    private Vector getSubmissionBeans(Vector masterData, Vector toModify) {
        Vector data = new Vector();
        ProtocolReviewTypeCheckListBean protocolReviewTypeCheckListBean;
        CheckListBean checkListBean;
        
        if(toModify != null && toModify.size() > 0) {
            String code;
            boolean isChecked;
            String description;
            masterData:for(int index = 0; index < masterData.size(); index++) {
                code = ((CheckListBean)masterData.get(index)).getCheckListCode();
                isChecked = ((CheckListBean)masterData.get(index)).isChecked();
                description = ((CheckListBean)masterData.get(index)).getDescription();
                
                for(int indexToModify = 0; indexToModify < toModify.size(); indexToModify++) {
                    checkListBean = (CheckListBean)toModify.get(indexToModify);
                    if(code.equals( checkListBean.getCheckListCode() ) ) {
                    if(!isChecked) {
                        //protocolReviewTypeCheckListBean = new ProtocolReviewTypeCheckListBean();
                        //protocolReviewTypeCheckListBean.setCheckListCode(checkListBean.getCheckListCode());
                        //protocolReviewTypeCheckListBean.setDescription(checkListBean.getDescription());
                        protocolReviewTypeCheckListBean = (ProtocolReviewTypeCheckListBean)toModify.get(indexToModify);
                        protocolReviewTypeCheckListBean.setAcType(DELETE_RECORD);
                        data.add(protocolReviewTypeCheckListBean);
                        continue masterData;
                    }
                    else continue masterData;
                    }
                }//End For toModify
                if(isChecked) {
                    protocolReviewTypeCheckListBean = new ProtocolReviewTypeCheckListBean();
                    protocolReviewTypeCheckListBean.setCheckListCode(code);
                    protocolReviewTypeCheckListBean.setDescription(description);
                    protocolReviewTypeCheckListBean.setAcType(INSERT_RECORD);
                    protocolReviewTypeCheckListBean.setProtocolNumber(protocolInfo.getProtocolNumber());
                    protocolReviewTypeCheckListBean.setSequenceNumber(protocolInfo.getSequenceNumber());
                    data.add(protocolReviewTypeCheckListBean);
                }
            }//End For master Data
        }//End if
        else {
            for(int index = 0; index < masterData.size(); index++) {
                checkListBean = (CheckListBean)masterData.get(index);
                if(checkListBean.isChecked()) {
                    protocolReviewTypeCheckListBean = new ProtocolReviewTypeCheckListBean();
                    protocolReviewTypeCheckListBean.setCheckListCode(checkListBean.getCheckListCode());
                    protocolReviewTypeCheckListBean.setDescription(checkListBean.getDescription());
                    protocolReviewTypeCheckListBean.setAcType(INSERT_RECORD);
                    protocolReviewTypeCheckListBean.setProtocolNumber(protocolInfo.getProtocolNumber());
                    protocolReviewTypeCheckListBean.setSequenceNumber(protocolInfo.getSequenceNumber());
                    data.add(protocolReviewTypeCheckListBean);
                }
            }
        }//Not Selected
        return data;
    }
    
    /**
     * Modifies the CheckListBeans state as checked/unchecked.
     */
    private void setCheckedState(Vector data, boolean checked) {
        for(int index = 0; index < data.size(); index++) {
            ((CheckListBean)data.get(index)).setIsChecked(checked);
        }
    }
    
    //Added By sharath for CheckList - END
    
    private void cmbTypeQualifierItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_cmbTypeQualifierItemStateChanged
    {//GEN-HEADEREND:event_cmbTypeQualifierItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED)
        {    
            recordChanged() ;
        }  
    }//GEN-LAST:event_cmbTypeQualifierItemStateChanged

    private void cmbReviewTypeItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_cmbReviewTypeItemStateChanged
    {//GEN-HEADEREND:event_cmbReviewTypeItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED)
        {    
            recordChanged() ;
        } 
        //Added By sharath for CheckList - START
        if(evt.getStateChange() == evt.DESELECTED){
            return ;
        }
        
        String newSelectedItem = cmbReviewType.getSelectedItem().toString().trim();
        if(selectedItem == null) selectedItem = newSelectedItem;
        else if((selectedItem.equalsIgnoreCase(EXEMPT) || selectedItem.equalsIgnoreCase(EXPEDITED)) && !selectedItem.equalsIgnoreCase(newSelectedItem)) {
            int checkedSize = 0;
            if(selectedItem.equalsIgnoreCase(EXEMPT)) {
                checkList.setFormData(vecExempt);
                checkedSize = checkList.getData(true).size();
            }
            else if(selectedItem.equalsIgnoreCase(EXPEDITED)) {
                checkList.setFormData(vecExpedited);
                checkedSize = checkList.getData(true).size();
            }
            // modified by ravi for not asking for message if the combo value is
            //changed due to record change
            if(checkedSize > 0 && triggerCheckListWarning) { 
            int selection = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(LOSE_CHECKED_LIST_INFO),CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);
            if(selection == CoeusOptionPane.SELECTION_YES) {
                selectedItem = newSelectedItem;
            }
            else if(selection == CoeusOptionPane.SELECTION_NO) {
                cmbReviewType.setSelectedIndex(selectedIndex);
            }
            }
            else selectedItem = newSelectedItem;
        }else{
            selectedItem = newSelectedItem;
        }
        
        if(selectedItem.equalsIgnoreCase(EXPEDITED)) {
            selectedIndex = cmbReviewType.getSelectedIndex();
            btnCheckList.setEnabled(true);
        }
        else if(selectedItem.equalsIgnoreCase(EXEMPT)) {
            selectedIndex = cmbReviewType.getSelectedIndex();
            btnCheckList.setEnabled(true);
        }
        else btnCheckList.setEnabled(false);
        
        //Added By sharath for CheckList - END        
    }//GEN-LAST:event_cmbReviewTypeItemStateChanged

    private void cmbSubmissionTypeItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_cmbSubmissionTypeItemStateChanged
    {//GEN-HEADEREND:event_cmbSubmissionTypeItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED)
        {
           ComboBoxBean comboBeanNew = (ComboBoxBean)cmbSubmissionType.getSelectedItem();
           if(comboBeanNew.getCode().equals(FYI) && !windowOpen){
               CoeusOptionPane.showWarningDialog("Cannot change submission type to FYI");
               cmbSubmissionType.setSelectedItem(this.comboBean);
               return;
           }
           this.comboBean = comboBeanNew;
           recordChanged() ;
        }    
    }//GEN-LAST:event_cmbSubmissionTypeItemStateChanged
    private void recordChanged()
    {
        btnPrev.setEnabled(false) ;
        btnSubmit.setEnabled(true) ;
        saveRequired =true ;
    }         
    
    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSubmitActionPerformed
    {//GEN-HEADEREND:event_btnSubmitActionPerformed
        // validate & submit the protocol
        
        try
        {
           if (functionMode == EDIT)
           {   int row = -1; 
               //Case #2080 Start 3
               boolean isNotificationGenerated = false;
               if(protocolActionForm.getTblActionForm().getEditorComponent()!= null){                    
                   //COEUSQA:3187 - Error when correcting Action Date on the Submission Details window - Start
                   row = protocolActionForm.getTblActionForm().getEditingRow();
                   int column = protocolActionForm.getTblActionForm().getEditingColumn();
                   //COEUSQA:3187 - End
                   protocolActionForm.getTblActionForm().getCellEditor().stopCellEditing();
                   //COEUSQA:3187 - Error when correcting Action Date on the Submission Details window - Start
                   protocolActionForm.getTblActionForm().editCellAt(row, column);
                   //COEUSQA:3187 - End
               }
               //COEUSQA:3187 - Error when correcting Action Date on the Submission Details window - Start
               else {
                   protocolActionForm.setActionDateValidationFired(false);
               }
               //COEUSQA:3187 - End
               //Case #2080 Start 3
               //COEUSQA:3187 - Error when correcting Action Date on the Submission Details window - Start
                  if(vecActionDateData != null && vecActionDateData.size() > 0) {
                   for(int actionIndex=0;actionIndex<vecActionDateData.size();actionIndex++){
                       if(row > -1 && row == actionIndex){
                           continue;
                       }
                       ProtocolActionsBean protocolActionsBean = (ProtocolActionsBean)vecActionDateData.get(actionIndex);
                       if(protocolActionsBean.getActionDate() == null ) {
                           if(!protocolActionForm.isActionDateValidationFired()) {
                               CoeusOptionPane.showInfoDialog(
                                       coeusMessageResources.parseMessageKey("protoSubmissionFrm_empty_date_exceptionCode.2013"));
                               isInValidActionDate = true;
                               break;
                           }
                       }
                   }
                  }
               //COEUSQA:3187 - End
               // 3282: Reviewer view of Protocols - Start
               if(tblSelectedReviewers != null && tblSelectedReviewers.getCellEditor() != null){
                   tblSelectedReviewers.getCellEditor().stopCellEditing();               
               }
               // 3282: Reviewer view of Protocols - End
               //COEUSQA:3187 - Error when correcting Action Date on the Submission Details window - Start
               if(!isInValidActionDate){
               //COEUSQA:3187 - End
                 // if (validateSubmit()) //prps added this if condtn  server side validation - jul 21 2003
                      submitProtocol();
                      // Added for COEUSQA-3012:Need notification for when a reviewer completes their review in  IACUC - start  
                      // Added for COEUSQA-3096 : "Unknown Error" When Trying to Schedule an FYI Submission - Start
                      // Provide null and isEmpty check for the selected reviewer collection
                      if(vecDefaultReviewer != null && !vecDefaultReviewer.isEmpty()){
                          ProtocolSubmissionInfoBean protocolSubmissionInfoBean = (ProtocolSubmissionInfoBean)vecDefaultReviewer.get(curRecord);
                          vecReviewer = protocolSubmissionInfoBean.getProtocolReviewer();
                          if (vecReviewer != null && !vecReviewer.isEmpty()){
                              for(Object selectedReviewerObj : vecReviewer){
                                  ProtocolReviewerInfoBean selectedReviewerInfo =(ProtocolReviewerInfoBean)selectedReviewerObj;
                                  if(selectedReviewerInfo.isReviewComplete() && !selectedReviewerInfo.isAwReviewComplete()){
                                      isNotificationGenerated = true;
                                  }
                                  
                              }
                          }

                      }
                }
                      // Added for COEUSQA-3096 : "Unknown Error" When Trying to Schedule an FYI Submission - End
                      if( dataSaved ){ 
                          CoeusOptionPane.showInfoDialog("Submission data saved successfully") ;
                      //Sending Mail Notification
                      if(isNotificationGenerated){
                              ProtocolMailController mailController = new ProtocolMailController();
                              synchronized(mailController) {
                                  mailController.sendMail( REVIEWER_REVIEW_COMPLETE_NOTIFICATION_CODE,submissionBean.getProtocolNumber(),submissionBean.getSequenceNumber());
                              }
                       }
                       //COEUSQA-3012:Need notification for when a reviewer completes their review in  IACUC - end
                          
                          //Case #2080 Start 4
                          protocolActionForm.unRegisterObserver(this);
                          //Case #2080 End 4
            
                          thisWindow.dispose() ;
                      }
                     
           }
           else
           {
                if( commentsLocked && txtScheduleId.getText()!= null ) {
                    releaseUpdateLock();
                }
                
                //Case #2080 Start 4
                protocolActionForm.unRegisterObserver(this);
                //Case #2080 End 4
            
                thisWindow.dispose() ;
           }    
        }catch(Exception e){
            e.printStackTrace();
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }    
       
    }//GEN-LAST:event_btnSubmitActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNextActionPerformed
    {//GEN-HEADEREND:event_btnNextActionPerformed
        //Case #2080 Start 6
           if(protocolActionForm.getTblActionForm().getEditorComponent()!= null){
                protocolActionForm.getTblActionForm().getCellEditor().stopCellEditing();
           }//Case #2080 Start 6
        if (curRecord + 1 < rowCount)
        {
            curRecord++ ;
            setCursor( new Cursor( Cursor.WAIT_CURSOR ) ); 
            showRecord(curRecord) ;
            //Added for Case #1264 - Start 1
            setProtocolActionsData(curRecord);
            //Added for Case #1264 - end 1
            saveRequired = false;
            setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
        }    
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPrevActionPerformed
    {//GEN-HEADEREND:event_btnPrevActionPerformed
        //Added for Case #1264 Start 5
        if(protocolActionForm.getTblActionForm().getEditorComponent()!= null){
            protocolActionForm.getTblActionForm().getCellEditor().stopCellEditing();
        }
        //Added for Case #1264 End 5
        if (curRecord - 1 >= 0) {
            curRecord-- ;
            setCursor( new Cursor( Cursor.WAIT_CURSOR ) );
            showRecord(curRecord) ;
            //Added for Case #1264 - Start 2
            setProtocolActionsData(curRecord); 
            //Added for Case #1264 - End 2
            saveRequired = false;
            setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
        }
        
    }//GEN-LAST:event_btnPrevActionPerformed
    
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAddActionPerformed
    {//GEN-HEADEREND:event_btnAddActionPerformed
        // Add your handling code here:
         Vector vecRow = new Vector();
        int rowNum = tblAvailableReviewers.getSelectedRow();
        DefaultTableModel model 
               = (DefaultTableModel)tblAvailableReviewers.getModel();
        if ( (rowNum != -1) && (rowNum <model.getRowCount()) ){
            vecRow.add(model.getValueAt(rowNum,0));
            vecRow.add(new Boolean(false));
            vecRow.add(model.getValueAt(rowNum,1));
            String revType = ((ComboBoxBean)reviewerTypes.get(0)).toString();
            vecRow.add(revType);
            // 3282: Reviewer View of Protocol materials - Start
            java.sql.Date  currentDate = new java.sql.Date(new java.util.Date().getTime());
            vecRow.add(new DateUtils().formatDate(currentDate.toString(),CoeusGuiConstants.DEFAULT_DATE_FORMAT));            
            vecRow.add("");
            vecRow.add(new ComboBoxBean ("",""));
            // 3282: Reviewer View of Protocol materials - End
            vecRow.add(model.getValueAt(rowNum, 2));
            
            insertRowInTable(tblSelectedReviewers,vecRow);
            model.removeRow(rowNum);
            recordChanged() ;
        }
        
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDeleteActionPerformed
    {//GEN-HEADEREND:event_btnDeleteActionPerformed
        // Add your handling code here:
        Vector vecRow = new Vector();
        int rowNum = tblSelectedReviewers.getSelectedRow();
        DefaultTableModel model 
                = (DefaultTableModel)tblSelectedReviewers.getModel();
        if ( (rowNum != -1) && (rowNum <model.getRowCount()) ){
            
            vecRow.add(model.getValueAt(rowNum,0));
            vecRow.addElement(model.getValueAt(rowNum,2));
            vecRow.add(model.getValueAt(rowNum, 7));
            
            // insert the deleted reviewer into available reviewers list.
            insertRowInTable(tblAvailableReviewers,vecRow);

            model.removeRow(rowNum);
            model.fireTableDataChanged();
            recordChanged() ;
            
            int newRowCount = model.getRowCount();
            // select the next row if exists
            if (newRowCount > rowNum) {
                (tblSelectedReviewers.getSelectionModel())
                    .setSelectionInterval(rowNum, 
                                                rowNum);
            } else {
                (tblSelectedReviewers.getSelectionModel())
                    .setSelectionInterval(newRowCount - 1, 
                        newRowCount - 1);
            }
        }
        
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnScheduleSelect(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnScheduleSelect
    {//GEN-HEADEREND:event_btnScheduleSelect
        // Schedule change will not initialise the reviewers...
        //Case #2080 Start 5
               if(protocolActionForm.getTblActionForm().getEditorComponent()!= null){
                    protocolActionForm.getTblActionForm().getCellEditor().stopCellEditing();
               }//Case #2080 Start 5
        if (txtCommiteeId.getText() != null || txtCommiteeId.getText().trim().length() == 0)
        {              
              Vector schedules = getSchedules(txtCommiteeId.getText()) ;
               scheduleSelectionWindow = new ScheduleSelectionWindow(parent, "Schedule Selection", true, new ArrayList(schedules)) ;
            Dimension screenSize
            = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension dlgSize = scheduleSelectionWindow.getSize();
            scheduleSelectionWindow.setLocation(
            screenSize.width/2 - (dlgSize.width/2),
            screenSize.height/2 - (dlgSize.height/2));
            scheduleSelectionWindow.show() ;
            scheduleDetailsBean = scheduleSelectionWindow.getScheduleDetailsBean() ;
            if (scheduleDetailsBean != null)
            {
                txtScheduleId.setText(scheduleDetailsBean.getScheduleId()) ;
                txtSchedulePlace.setText(""+scheduleDetailsBean.getScheduledPlace());
                String dateStr = dtUtils.formatDate(scheduleDetailsBean.getScheduleDate().toString(), "dd-MMM-yyyy");
                txtScheduleDate.setText(dateStr) ;
                newScheduleId = txtScheduleId.getText() ;
                maxProtocols = scheduleDetailsBean.getMaxProtocols();
                recordChanged();
                checkScheduleLoad = true;
                // modified by ravi
                Vector tempSelectedReviewers = vecSelectedReviewers;
                this.vecSelectedReviewers = null ;
                initialiseReviewers() ;
                vecSelectedReviewers = tempSelectedReviewers;

                //end of modification by ravi
                
                availableReviewersList =  new ArrayList(getAvailableReviewersForSchedule(txtScheduleId.getText())) ;
                setReviewerFormData();
                setReviewerTableFormat();
                tblAvailableReviewers.getTableHeader().setFont(
                            CoeusFontFactory.getLabelFont());

                tblSelectedReviewers.getTableHeader().setFont(
                            CoeusFontFactory.getLabelFont());
            }    
       }
        else
        {
            CoeusOptionPane.showErrorDialog("Select a Committee") ;
        }    
    }//GEN-LAST:event_btnScheduleSelect

    private void btnCommitteeSelect(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCommitteeSelect
    {//GEN-HEADEREND:event_btnCommitteeSelect
        
    // get the default committee list and display it on teh committeeSelectionWindow   
    //Case #2080 Start 4
       if(protocolActionForm.getTblActionForm().getEditorComponent()!= null){
            protocolActionForm.getTblActionForm().getCellEditor().stopCellEditing();
       }//Case #2080 Start 4
    committeeList = getDefaultCommitteeList() ;    
        
    CommitteeSelectionWindow  committeeSelectionWindow = new CommitteeSelectionWindow(parent, "Committee Selection", true, committeeList) ; //new ArrayList(getCommitteList()
    Dimension screenSize
        = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = committeeSelectionWindow.getSize();
        committeeSelectionWindow.setLocation(
        screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
		        
    committeeSelectionWindow.show(true) ;
    
    if (committeeSelectionWindow.getCommitteeId() != null)
    {
        txtCommiteeId.setText(committeeSelectionWindow.getCommitteeId()) ;
        txtCommitteeName.setText(committeeSelectionWindow.getCommitteeName()) ;
        newCommitteeId = txtCommiteeId.getText() ;
        
        // clear schedule & re-initiliase reviewers
        txtScheduleId.setText("") ;
        txtScheduleDate.setText("") ;
        txtSchedulePlace.setText(""); // added by manoj for new requirements 16/09/2003
        newScheduleId = "" ;
        checkScheduleLoad = false;
        maxProtocols = 0;
        // modified by ravi
        Vector tempSelectedReviewers = vecSelectedReviewers;
        this.vecSelectedReviewers = null ;
        initialiseReviewers() ;
        vecSelectedReviewers = tempSelectedReviewers;
        
        //end of modification by ravi
        
        recordChanged() ;
    }    
        
    }//GEN-LAST:event_btnCommitteeSelect

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancelActionPerformed
    {//GEN-HEADEREND:event_btnCancelActionPerformed
         int row = -1;
            //Case #2080 Start 6
           if(protocolActionForm.getTblActionForm().getEditorComponent()!= null){               
             //COEUSQA:3187 - Error when correcting Action Date on the Submission Details window - Start
             row = protocolActionForm.getTblActionForm().getEditingRow();
             int column = protocolActionForm.getTblActionForm().getEditingColumn();
             //COEUSQA:3187 - End
             protocolActionForm.getTblActionForm().getCellEditor().stopCellEditing();
             //COEUSQA:3187 - Error when correcting Action Date on the Submission Details window - Start
             protocolActionForm.getTblActionForm().editCellAt(row, column);
             //COEUSQA:3187 - End
           }
           //COEUSQA:3187 - Error when correcting Action Date on the Submission Details window - Start
           else {
             protocolActionForm.setActionDateValidationFired(false);
           }
           //COEUSQA:3187 - End
           //Case #2080 Start 6
         //COEUSQA:3187 - Error when correcting Action Date on the Submission Details window - Start
         if(vecActionDateData != null && vecActionDateData.size() > 0) {
             for (int actionIndex=0;actionIndex<vecActionDateData.size();actionIndex++) {
                 if(row > -1 && row == actionIndex){
                     continue;
                 }
                 ProtocolActionsBean protocolActionsBean = (ProtocolActionsBean)vecActionDateData.get(actionIndex);
                 if(protocolActionsBean.getActionDate() == null ) {
                     if(!protocolActionForm.isActionDateValidationFired()) {
                         // fireActionDateMessage = true;
                         CoeusOptionPane.showInfoDialog(
                                 coeusMessageResources.parseMessageKey("protoSubmissionFrm_empty_date_exceptionCode.2013"));
                         isInValidActionDate = true;
                         break;
                     }
                 }
             }
         }
         //COEUSQA:3187 - End
        int option = CoeusOptionPane.SELECTION_NO;
         if (functionMode == EDIT)
         {   
            /* take the user confirmation for saving the details if anything has
               been modified before closing the dialog. */
          //COEUSQA:3187 - Error when correcting Action Date on the Submission Details window - Start
          if(!isInValidActionDate){
          //COEUSQA:3187 - End
            if(isSaveRequired())
            {   
                option  = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),  
                        CoeusOptionPane.OPTION_YES_NO_CANCEL,
                        CoeusOptionPane.DEFAULT_YES);
            }
            if(option == CoeusOptionPane.SELECTION_YES){
                try{
                    submitProtocol();
                    CoeusOptionPane.showInfoDialog("Submission data saved successfully") ;
                    
                    //Case #2080 Start 5
                    protocolActionForm.unRegisterObserver(this);
                    //Case #2080 End 5
                    
                    thisWindow.dispose() ;
                }catch(Exception e){
                    e.printStackTrace();
                    CoeusOptionPane.showErrorDialog(e.getMessage());
                }
            }else if(option == CoeusOptionPane.SELECTION_NO){
                saveRequired = false;
                /** Bug Fix #2072 -start
             */
//                if( commentsLocked && txtScheduleId.getText()!= null ) {
//                    releaseUpdateLock();
//                }/** Bug Fix #2072 -End*/
                
                //Case #2080 Start 6
                protocolActionForm.unRegisterObserver(this);
                //Case #2080 End 6
            
                thisWindow.dispose();
            }
            else if(option == CoeusOptionPane.SELECTION_CANCEL){
                saveRequired = true ;
                thisWindow.show() ;
            }
          }
       }
       else
       {    /** Bug Fix #2072 -start
             */
           if( commentsLocked && txtScheduleId.getText()!= null ) {
                    releaseUpdateLock();
            }/** Bug Fix #2072 -End */
           
           //Case #2080 Start 7
           protocolActionForm.unRegisterObserver(this);
           //Case #2080 End 7
           
           thisWindow.dispose();
       }
    }//GEN-LAST:event_btnCancelActionPerformed
    
   
    private ArrayList getDefaultCommitteeList()
    {
        ArrayList defCommitteeList = null ;
        String connectTo = CoeusGuiConstants.CONNECTION_URL
                 + "/SubmissionDetailsServlet";
        try
        {
            RequesterBean request = new RequesterBean();
            request.setFunctionType('C');
            request.setId(protocolId) ;
            AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);

            setCursor( new Cursor( Cursor.WAIT_CURSOR ) );        
            comm.send();
            ResponderBean response = comm.getResponse() ;
            setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );

            if (!response.isSuccessfulResponse()) {
                throw new Exception( response.getMessage());
           }
            else
            {
                defCommitteeList = new ArrayList((Vector)response.getDataObject()) ;
            }    
            
        }catch(Exception ex)
        {
            ex.printStackTrace() ;
        }
           return defCommitteeList ; 
    }
    
    
    
     public void initialiseData(int recNumber)
     {
        java.util.Date dt = null;
        
        triggerCheckListWarning = false;

        btnCheckList.setMnemonic('k');
        btnSchedule.setMnemonic('h');
        btnCommittee.setMnemonic('t');
        
        
        ProtocolSubmissionInfoBean protocolSubmissionInfoBean = (ProtocolSubmissionInfoBean)vecSubmissionDetails.get(recNumber);
        submissionBean = protocolSubmissionInfoBean;
        vecSelectedReviewers = protocolSubmissionInfoBean.getProtocolReviewer();
        setCheckedState(vecExempt, false);
        setCheckedState(vecExpedited, false);
        vecExpeditedToModify = protocolSubmissionInfoBean.getProtocolExpeditedCheckList();
        vecExemptToModify = protocolSubmissionInfoBean.getProtocolExemptCheckList();
        
        if(vecExpeditedToModify != null && vecExpeditedToModify.size() > 0) {
                setCheckedState(vecExpeditedToModify, true);
            }
            
            if(vecExemptToModify != null && vecExemptToModify.size() > 0) {
                setCheckedState(vecExemptToModify, true);
            }
            
        prepareCheckList(vecExempt, vecExemptToModify);
        prepareCheckList(vecExpedited, vecExpeditedToModify);
        
        
        // protocol details start
        txtProtocolId.setText(protocolSubmissionInfoBean.getProtocolNumber());
        protocolId = protocolSubmissionInfoBean.getProtocolNumber();
        seqNo = protocolSubmissionInfoBean.getSequenceNumber();
        submissionNo = protocolSubmissionInfoBean.getSubmissionNumber();
        txtProtocolTitle.setText(protocolSubmissionInfoBean.getTitle()==null? " " : protocolSubmissionInfoBean.getTitle()) ;
        txtPI.setText(protocolSubmissionInfoBean.getPIName()==null? "" : protocolSubmissionInfoBean.getPIName()) ;
        
        //Commented By sharath for Application Date Format
        //txtApplicationDate.setText(hashRow.get("APPLICATION_DATE")==null? "" : hashRow.get("APPLICATION_DATE").toString()) ;
        //Added By sharath for Application Date Format - START
        if(protocolSubmissionInfoBean.getApplicationDate() != null) {
            String dateStr = dtUtils.formatDate(protocolSubmissionInfoBean.getApplicationDate().toString(), "dd-MMM-yyyy");
            txtApplicationDate.setText(dateStr) ;
        }
        //Added By sharath for Application Date Format - END
        
        //protocol details end
        
        //submission details start
        windowOpen = true;
       for(int idx = 0; idx < cmbSubmissionType.getItemCount();
                    idx++){
                if(((ComboBoxBean)cmbSubmissionType.getItemAt(
                        idx)).getCode().equals(""+protocolSubmissionInfoBean.getSubmissionTypeCode())){
                    cmbSubmissionType.setSelectedIndex(idx);
                }
            }
       windowOpen = false;
        for(int idx = 0; idx < cmbReviewType.getItemCount();
                    idx++){
                if(((ComboBoxBean)cmbReviewType.getItemAt(
                        idx)).getCode().equals(""+protocolSubmissionInfoBean.getProtocolReviewTypeCode())){
                    cmbReviewType.setSelectedIndex(idx);
                }
            }
                
         for(int idx = 0; idx < cmbTypeQualifier.getItemCount();
                    idx++){
                if(((ComboBoxBean)cmbTypeQualifier.getItemAt(
                        idx)).getCode().equals(""+protocolSubmissionInfoBean.getSubmissionQualTypeCode())){
                    cmbTypeQualifier.setSelectedIndex(idx);
                }
            }
        
        txtSubmissionStatusCode.setText(""+protocolSubmissionInfoBean.getSubmissionStatusCode()) ;
        txtSubmissionStatusDesc.setText(protocolSubmissionInfoBean.getSubmissionStatusDesc()==null?"" :protocolSubmissionInfoBean.getSubmissionStatusDesc().toString()) ;
                
        if (protocolSubmissionInfoBean.getSubmissionDate()!=null)
        {   
            String dateStr = dtUtils.formatDate(protocolSubmissionInfoBean.getSubmissionDate().toString(), "dd-MMM-yyyy");
            txtSubmissionDate.setText(dateStr) ;
        } 
        else
        {
            txtSubmissionDate.setText("") ;
        }
        //submission details end
        
        
        // committee start
        txtCommiteeId.setText(protocolSubmissionInfoBean.getCommitteeId()==null? "" : protocolSubmissionInfoBean.getCommitteeId().toString()) ;
        oldCommitteeId = protocolSubmissionInfoBean.getCommitteeId()==null? new String() : protocolSubmissionInfoBean.getCommitteeId().toString() ;
        newCommitteeId = protocolSubmissionInfoBean.getCommitteeId()==null? new String() : protocolSubmissionInfoBean.getCommitteeId().toString() ;
        
        txtCommitteeName.setText(protocolSubmissionInfoBean.getCommitteeName()==null? "" : protocolSubmissionInfoBean.getCommitteeName().toString()) ;
        // committee end
        
        //schedule start
        txtScheduleId.setText(protocolSubmissionInfoBean.getScheduleId()==null? "" : protocolSubmissionInfoBean.getScheduleId().toString()) ;
        oldScheduleId = protocolSubmissionInfoBean.getScheduleId()==null? new String() : protocolSubmissionInfoBean.getScheduleId().toString() ;
        newScheduleId = protocolSubmissionInfoBean.getScheduleId()==null? new String() : protocolSubmissionInfoBean.getScheduleId().toString() ;
        
        if (protocolSubmissionInfoBean.getScheduleDate()!=null) 
        {   
            String dateStr = dtUtils.formatDate(protocolSubmissionInfoBean.getScheduleDate().toString(), "dd-MMM-yyyy");
            txtScheduleDate.setText(dateStr) ;
        } 
        else
        {
            txtScheduleDate.setText("") ;
        }
        
        txtSchedulePlace.setText(protocolSubmissionInfoBean.getSchedulePlace()) ;
        //schedule end
        
        //Voting details start
        txtSubmissionYesVoteCount.setText(""+protocolSubmissionInfoBean.getYesVoteCount()) ;
        txtSubmissionNoVoteCount.setText(""+protocolSubmissionInfoBean.getNoVoteCount()) ;
        txtAbstainerCount.setText(""+protocolSubmissionInfoBean.getAbstainerCount()) ;
        txtVotingComments.setText(protocolSubmissionInfoBean.getVotingComments()) ;
        //voting details end
        triggerCheckListWarning = true;
        // reviewers start
        initialiseReviewers() ;
        // reviewers end
        //added by ravi to reset saveRequired to false , if set while populating data - START
        saveRequired = false;
        //added by ravi  - END
    }
    
     private void initialiseReviewers()
     {
        // already selected reviewers for this protocol
        if (vecSelectedReviewers != null)
        {    
            reviewersList = new ArrayList(vecSelectedReviewers) ;
        }
        else
        {
            reviewersList = null ;
            int count = ((DefaultTableModel)tblAvailableReviewers.getModel()).getRowCount() ;
            for (int idx =0 ; idx< count ; idx++)
            {    
                ((DefaultTableModel)tblAvailableReviewers.getModel()).removeRow(0) ;
            }
            
            count = ((DefaultTableModel)tblSelectedReviewers.getModel()).getRowCount() ;
            for (int idx =0 ; idx< count ; idx++)
            {    
                ((DefaultTableModel)tblSelectedReviewers.getModel()).removeRow(0) ;
            }
        }
        
        
        // complete list of available reviewers
        if( txtScheduleId.getText() != null && txtScheduleId.getText().trim().length() > 0 ) {
            availableReviewersList = new ArrayList(getAvailableReviewersForSchedule(txtScheduleId.getText()));
        }else{
            availableReviewersList =  new ArrayList(getAvailableReviewersForCommittee(txtCommiteeId.getText())) ;
        }
        setReviewerFormData();
        setReviewerTableFormat();
     }
     
       
    private boolean isVotingDetailsChanged()
    {
       if (!txtSubmissionYesVoteCount.getText().trim().equals(""+yesVoteCount))
       {
          return true ;
       }
       if (!txtSubmissionNoVoteCount.getText().trim().equals(""+noVoteCount))
       {
           return true ;
       }
       if (!txtAbstainerCount.getText().trim().equals(""+abstainerCount))
       {
           return true ;
       }
       if (!txtVotingComments.getText().trim().equals(votingComments))
       {
           return true ;
       }
       
        return false ;
    }
     
    //added by nadh for enhancements of irb to make btnSubmit button enable
    //start 9 aug 2004
    private void registerKeyListeners(){
        txtSubmissionYesVoteCount.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent e){
                btnSubmit.setEnabled(true);
            }
        });
        txtSubmissionNoVoteCount.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent e){
                btnSubmit.setEnabled(true);
            }
        });
        txtAbstainerCount.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent e){
                btnSubmit.setEnabled(true);
            }
        });
        txtVotingComments.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent e){
                btnSubmit.setEnabled(true);
            }
        });
    }
    //end Nadh 9 aug 2004
    public void showForm()
    {
        thisWindow = new CoeusDlgWindow(parent, title, true);
        	
         thisWindow.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
         thisWindow.addWindowListener( 
            new WindowAdapter(){
                public void windowActivated(WindowEvent we){
                    requestDefaultFocusForComponent();
                }
                public void windowClosing(WindowEvent we){
                    btnCancelActionPerformed(null)  ;
                }
        });           
        thisWindow.addEscapeKeyListener(
            new AbstractAction("escPressed"){
                public void actionPerformed(ActionEvent ae){
                    btnCancelActionPerformed(ae);
                }
        });
       //JScrollPane scrollPane = new JScrollPane(pnlMain) ; 
       thisWindow.getContentPane().add(pnlMain) ; //  scrollPane);
       /** This btn is commented for temporary - chandra 22/10/2003
        */
       //thisWindow.setSize(725, 700);     
       //thisWindow.setSize(800,723);
	   thisWindow.pack();
       Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
       
       Dimension dlgSize = thisWindow.getSize();
       thisWindow.setLocation(screenSize.width/2 - (dlgSize.width/2),
       (screenSize.height/2 - (dlgSize.height/2) - 15));
       
       //thisWindow.setResizable(true) ;
       thisWindow.setResizable(false);
       
       //Case #2080 Start 8
	   // Added by Jobin for showing the table value - start
	   /* ProtocolActionsForm protocolActionForm = new ProtocolActionsForm();
		protocolActionForm.showActions(vecAction);
		scrpneActions.setViewportView(protocolActionForm.showProtocolActionsForm(CoeusGuiConstants.getMDIForm())); //Added by Jobin - end*/
		// Jobin - end
                
       //protocolActionForm = new ProtocolActionsForm();
       protocolActionForm.registerObserver(this);
       protocolActionForm.showActions(vecAction);
       scrpneActions.setViewportView(protocolActionForm.showProtocolActionsForm(CoeusGuiConstants.getMDIForm()));
       //Added for case #1264 start 
       setProtocolActionsData(curRecord);
       //Added for case #1264 end
                
       if(functionMode == EDIT){
           protocolActionForm.setOpenedFormSubWdw(true);         
       }else{
           protocolActionForm.setOpenedFormSubWdw(false);
       }
       //Case #2080 End 8
       //Code added for Case#3554 - Notify IRB enhancement - starts
       this.comboBean = (ComboBoxBean)cmbSubmissionType.getSelectedItem();
       if(this.comboBean.getCode().equals(FYI)){
           cmbSubmissionType.setEnabled(false);
       }
       if(vecAction != null && vecAction.size() > 0){
           ProtocolActionsBean actionBean = (ProtocolActionsBean)vecAction.get(0);
           btnViewAttachment.setEnabled(actionBean.isIsDocumentExists());
       }
       windowOpen = false;
       //Code added for Case#3554 - Notify IRB enhancement - ends
       thisWindow.show() ;
    }
 
    public void requestDefaultFocusForComponent(){
       if( cmbSubmissionType.isEnabled() ) {
            cmbSubmissionType.requestFocusInWindow();
       }else{
            btnSubmit.requestFocusInWindow();
       }
    
    }
    /**
     * This method is used to fetch all the lookup details required for protocol
     * submission. 
     */
    private void getLookupDetails() throws Exception{
        String connectTo = CoeusGuiConstants.CONNECTION_URL
                + "/protocolSubSrvlt";

        /* connect to the database and get the Schedule Details for the given
           schedule id */
        RequesterBean request = new RequesterBean();
        
        //Added By sharath for CheckList - START
        protocolInfo = new ProtocolInfoBean();
        //HashMap submissionDetails = (HashMap)vecSubmissionDetails.get(0);
        ProtocolSubmissionInfoBean protocolSubmissionInfoBean = (ProtocolSubmissionInfoBean)vecSubmissionDetails.get(0);
        
        String protocolNumber;
        int sequenceNumber;
        try{
            protocolNumber = protocolSubmissionInfoBean.getProtocolNumber(); //submissionDetails.get("PROTOCOL_NUMBER").toString().trim();
            sequenceNumber = protocolSubmissionInfoBean.getSequenceNumber();//Integer.parseInt(submissionDetails.get("SEQUENCE_NUMBER").toString().trim());
        
            protocolInfo.setProtocolNumber(protocolNumber);
            protocolInfo.setSequenceNumber(sequenceNumber);
            request.setDataObject(protocolInfo);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        //Added By sharath for CheckList - END
        
        //Form will always be opened for Modify Mode - start
        //request.setFunctionType('A');
        request.setFunctionType(MODIFY_MODE);
        //Form will always be opened for Modify Mode - end
        
        request.setId(""); // set id to nothing("") so that the servlet gets back just the data reqd for combos
        AppletServletCommunicator comm = new AppletServletCommunicator(
                                                           connectTo, request);
        /**
         * Updated for REF ID :0003  Feb'21 2003.
         * Hour Glass implementation while DB Trsactions Wait
         * by Subramanya Feb' 21 2003
         */
        setCursor( new Cursor( Cursor.WAIT_CURSOR ) );        
        comm.send();
        ResponderBean response = comm.getResponse();
        setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
        
        Vector dataObjects = response.getDataObjects();
        if (response.isSuccessfulResponse()) {
            /* get all the lookup details required for submissionTypes,
               submissionTypeQualifiers, protocol review types and protocol
               reviewer types */
            submissionTypes = new ArrayList((Vector)dataObjects.get(0));
            typeQualifiers = new ArrayList((Vector)dataObjects.get(1));
            reviewTypes = new ArrayList((Vector)dataObjects.get(2));
            reviewerTypes = new ArrayList((Vector)dataObjects.get(3));
            
            //Added By sharath for CheckList - START
            vecExpedited = (Vector)dataObjects.get(4);
            vecExempt = (Vector)dataObjects.get(5);
            
            /*vecExpeditedToModify = (Vector)dataObjects.get(6);
            if(vecExpeditedToModify != null && vecExpeditedToModify.size() > 0) {
                setCheckedState(vecExpeditedToModify, true);
            }
            
            vecExemptToModify = (Vector)dataObjects.get(7);
            if(vecExemptToModify != null && vecExemptToModify.size() > 0) {
                setCheckedState(vecExemptToModify, true);
            }
            
            
            prepareCheckList(vecExempt, vecExemptToModify);
            prepareCheckList(vecExpedited, vecExpeditedToModify);
            */
            //Added By sharath for CheckList - END
            
            if(dataObjects.size() > 9 && (Vector)dataObjects.get(9)!=null) { //if(dataObjects.size() > 4){
                /* if there are any committees whose home unit and research areas
                   matches with that of protocol then get the list of those 
                   committees */
                //committeeList = new ArrayList((Vector)dataObjects.get(4));
                committeeList = new ArrayList((Vector)dataObjects.get(9));
                /* if the protocol has been submitted then get the submission
                   details from the database */
                // Commented coz, the type of value is vector and it is assigned 
                //to ProtocolSubmissionInfoBean.
//                submissionBean = (ProtocolSubmissionInfoBean)
//                    //dataObjects.get(5);
//                    dataObjects.get(10);
                              
            }
            // 3282: reviewer view of protocols - Start
            if(dataObjects.size() >12){
                recommendedActionTypes = (Vector) dataObjects.get(13);
            }
            // 3282: reviewer view of protocols - End
        }else{
            String message = response.getMessage();
            throw new Exception(message);
        }
    }

    
   /**
     * This method is used to set the form data as well as all lookup data by
     * extracting details from submissionBean.
     */
    private void setDefaultData()
    {
        try
        {
            getLookupDetails() ;
        }
        catch(Exception ex)
        {
            ex.printStackTrace() ;
        }
        ComboBoxBean comboBean;
        if( submissionTypes != null ){
            /* add a blank row in Submission Type combo box so that the default
               selection will be blank */
            cmbSubmissionType.addItem(new ComboBoxBean("",""));
            for ( int loopIndex = 0 ; loopIndex < submissionTypes.size();
                loopIndex++ ) {
                /* extract ComboBoxBean from the collection of submissionTypes */    
                comboBean = (ComboBoxBean)submissionTypes.get(loopIndex);
                cmbSubmissionType.addItem(comboBean);
            }
        }

        if( typeQualifiers != null ){
            /* add a blank row in Submission Type Qualifier combo box so that 
               the default selection will be blank */
            cmbTypeQualifier.addItem(new ComboBoxBean("",""));
            for ( int loopIndex = 0 ; loopIndex < typeQualifiers.size();
                loopIndex++ ) {
                    
                comboBean = (ComboBoxBean)typeQualifiers.get(loopIndex);
                cmbTypeQualifier.addItem(comboBean);
            }

        }

        if( reviewTypes != null ){
            /* add a blank row in Protocol Review Type combo box so that the 
               default selection will be blank */
            cmbReviewType.addItem(new ComboBoxBean("",""));
            for ( int loopIndex = 0 ; loopIndex < reviewTypes.size();
                loopIndex++ ) {
                comboBean = (ComboBoxBean)reviewTypes.get(loopIndex);
                cmbReviewType.addItem(comboBean);
            }
        }

        saveRequired = false;

    }

    
    private Vector getSchedules(String committeeId){

        Vector vecBeans = new Vector();
        String connectTo = CoeusGuiConstants.CONNECTION_URL
                + "/protocolSubSrvlt";
        RequesterBean request = new RequesterBean();
        request.setFunctionType('V');
        request.setId(committeeId);
        AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, request);
        /**
         * Updated for REF ID :0003  Feb'21 2003.
         * Hour Glass implementation while DB Trsactions Wait
         * by Subramanya Feb' 21 2003
         */
        setCursor( new Cursor( Cursor.WAIT_CURSOR ) );        
        comm.send();
        ResponderBean response = comm.getResponse();
        setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );

        if (response.isSuccessfulResponse()) {
            vecBeans = (Vector)(
                (Vector) response.getDataObjects()).elementAt(0);
        }
        return vecBeans;
    }

    //*** submission begin
    
    
    /** This method returns the ProtocolSubmissionInfoBean after constructing it
     * from the selected data in the forms.
     *
     * @return submissionBean ProtocolSubmissionInfoBean with all the data
     * populated from the form objects.
     * @throws Exception Exception with custom message while constructing bean
     * value from any form object goes wrong.
     */
    private ProtocolSubmissionInfoBean getSubmissionData() throws Exception 
    {
        final int SUBMIT_TO_COMMITTEE = 100 ;
                
        /** collection object which holds all the available reviewers*/
        Vector existingReviewers ;
//        if(functionType == ADD_MODE){
//            
//            submissionBean.setAcType(INSERT_RECORD);
//        }else{
//            /* in modify mode add the propery change listener to the bean so that
//               modifications to any of the bean values can be catched */
//            submissionBean.addPropertyChangeListener(
//                new PropertyChangeListener(){
//                    public void propertyChange(PropertyChangeEvent pce){
//                        /* if any of the bean values changes set the acType of
//                           the bean to UPDATE_RECORD so that the corresponding 
//                           table row will be updated */
//                        
//                    }
//                }
//            );
//        }

        submissionBean = new ProtocolSubmissionInfoBean();
        submissionBean.setAcType(UPDATE_RECORD);
        
         //prps start 21 jul 2003
        // add commitee id (new field in OSP$Protocol_submission) to submissionBean
       if (isCommitteeChanged())
       {    
            submissionBean.setCommitteeId(newCommitteeId) ;
       }
       else
       {
            submissionBean.setCommitteeId(oldCommitteeId.trim().length()<=0?null:oldCommitteeId) ;
       }
       //prps end
        
        /* get the details of the selected schedule */
        if( isScheduleChanged() )
        {
            submissionBean.setScheduleId(newScheduleId) ;
            submissionBean.setAWScheduleId(oldScheduleId) ;
        }
        else
        {
            submissionBean.setScheduleId(oldScheduleId.trim().length()<=0?null:oldScheduleId) ; 
            submissionBean.setAWScheduleId(oldScheduleId.trim().length()<=0?null:oldScheduleId) ;
        }
        
        dtUtils = new edu.mit.coeus.utils.DateUtils();
        java.text.SimpleDateFormat dtFormat = new java.text.SimpleDateFormat("MM/dd/yyyy");
        
        //Added by sharath for chenging Data from Hashtable to Bean - START
        ProtocolSubmissionInfoBean protocolSubmissionInfoBean = (ProtocolSubmissionInfoBean)vecSubmissionDetails.get(curRecord);
        Date submissionDate = protocolSubmissionInfoBean.getSubmissionDate();
        //Added by sharath for chenging Data from Hashtable to Bean - END
        
        //if (hashRow.get("SCHEDULED_DATE")==null) //Commented By sharath
        if(submissionDate == null)
        {    
            submissionBean.setScheduleDate(null) ;
        }   
        else
        {
            if (txtScheduleDate.getText().trim().length()> 0)
            {    
                submissionBean.setScheduleDate(new java.sql.Date(
                            dtFormat.parse( dtUtils.restoreDate(txtScheduleDate.getText(),
                                            "/-:,")).getTime())) ;
            }   
        }    
        
        //if (hashRow.get("SUBMISSION_DATE")==null) //Commented By sharath
        if(submissionDate == null)
        {    
            submissionBean.setSubmissionDate(null) ;
        }
        else
        {
            if (txtSubmissionDate.getText().trim().length()> 0)
            {
                submissionBean.setSubmissionDate(new java.sql.Date(
                            dtFormat.parse( dtUtils.restoreDate(txtSubmissionDate.getText(),
                                            "/-:,")).getTime())) ;
            }   
        }
        
        submissionBean.setUpdateTimestamp((java.sql.Timestamp) protocolSubmissionInfoBean.getUpdateTimestamp());//hashRow.get("UPDATE_TIMESTAMP")) ; //Commented By sharath
        
        submissionBean.setSubmissionStatusCode(Integer.parseInt(txtSubmissionStatusCode.getText())) ;
        
        ComboBoxBean comboBean = (ComboBoxBean)cmbSubmissionType.getSelectedItem();
        submissionBean.setProtocolNumber(protocolId);
        submissionBean.setSequenceNumber(seqNo) ;
        submissionBean.setAWSequenceNumber(seqNo) ;
        submissionBean.setSubmissionTypeCode(Integer.parseInt(comboBean.getCode()));
        submissionBean.setSubmissionNumber(submissionNo);
        //Commented this to update the same Submission record instead of the Max Submission No. Rec - start
//        //prps start jul 15 2003
//        submissionBean.setSubmissionNumber(getMaxSubmissionNumber(protocolId))  ;
//        //prps end
        //end
        submissionBean.setSubmissionTypeDesc(comboBean.getDescription());
        comboBean = ( ComboBoxBean ) cmbReviewType.getSelectedItem();
        submissionBean.setProtocolReviewTypeCode(Integer.parseInt(comboBean.getCode()));
        submissionBean.setProtocolReviewTypeDesc(comboBean.getDescription());
        comboBean = ( ComboBoxBean ) cmbTypeQualifier.getSelectedItem();
        /* as the protocol submission type qualifier selection is not mandatory
           check whether user has selected any value. if so set the selected code
           and description to the bean */
        if(comboBean.getCode().length() > 0 )
        {
            submissionBean.setSubmissionQualTypeCode(Integer.parseInt(comboBean.getCode()));
            submissionBean.setSubmissionQualTypeDesc(comboBean.getDescription());
        }
     
        //voting details
        submissionBean.setNoVoteCount(Integer.parseInt(txtSubmissionNoVoteCount.getText().trim()==null ? "0" :txtSubmissionNoVoteCount.getText())) ;
        submissionBean.setYesVoteCount(Integer.parseInt(txtSubmissionYesVoteCount.getText().trim()==null ? "0" :txtSubmissionYesVoteCount.getText())) ;
        submissionBean.setAbstainerCount(Integer.parseInt(txtAbstainerCount.getText().trim()==null ? "0" :txtAbstainerCount.getText())) ;
        submissionBean.setVotingComments(txtVotingComments.getText().trim() == null ?null:txtVotingComments.getText().trim()) ;
        
        /* if user has changed the schedule then set all reviewers details for
           that schedule as DELETE_RECORD */
        existingReviewers =  vecSelectedReviewers ; // submissionBean.getProtocolReviewer();
        
        /* check whether user has changed the schedule then delete all the
               the existing reviewers */
        // modified by ravi from "&&" to "||" condition ,bcoz if either is true all the
        // existing reviewers should be deleted.
            if(isScheduleChanged() || getSelectedReviewers() == null )
             {
               if(existingReviewers != null)
               {
                    for( int index = 0 ; index < existingReviewers.size();
                            index++ ){
                        reviewerBean = (ProtocolReviewerInfoBean)
                            existingReviewers.get(index);
                        reviewerBean.setAcType(DELETE_RECORD);
                        existingReviewers.set(index,reviewerBean);
                    }
                }
            }
        
           // if there was not change in schedule but review form was changed then
          // go on with the following steps    
        
           Vector currentReviewers = getSelectedReviewers();
           /* loop through all reviewer table rows and construct beans and check
            * whether this reviewer is already exists or not. If not exists
            * setAcType to INSERT_RECORD and send. If exists, add property
            * change listener and set all the new data to the bean. If any
            * data is changed then the listener will fire and changes the
            * acType of the bean to UPDATE_RECORD
            */
            if(currentReviewers != null)
            {
                int reviewerTypesSize = reviewerTypes.size();
                int currentRevSize = currentReviewers.size();
                for(int currIndex = 0; currIndex < currentRevSize; currIndex++){
                    found = false;
                    int foundIndex = -1;
                    Vector reviewerRow = (Vector)currentReviewers.get(currIndex);
                    ProtocolReviewerInfoBean newReviewerBean = new ProtocolReviewerInfoBean();

                    // populate bean data
                    newReviewerBean.setProtocolNumber(protocolId);
                    newReviewerBean.setSequenceNumber(seqNo);
                    //commented as scheduleId is removed from reviewers table
                    //newReviewerBean.setScheduleId( submissionBean.getScheduleId());
                    newReviewerBean.setPersonId(reviewerRow.elementAt(0).toString());
                    // 3282: Reviewer View of Protocol materials - Start
//                    newReviewerBean.setIsNonEmployee(((Boolean)reviewerRow.elementAt(3)).booleanValue());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CoeusGuiConstants.DEFAULT_DATE_FORMAT);
                    if(reviewerRow.elementAt(4) != null && !"".equals(reviewerRow.elementAt(4))){
                        Date strDate =  simpleDateFormat.parse((String) reviewerRow.elementAt(4));
                        newReviewerBean.setAssignedDate(new java.sql.Date(strDate.getTime()));
                    }
                    if(reviewerRow.elementAt(5) != null && !"".equals(reviewerRow.elementAt(5))){                        
                        Date strDate =  simpleDateFormat.parse((String) reviewerRow.elementAt(5));
                        newReviewerBean.setDueDate(new java.sql.Date(strDate.getTime()));
                    }
                    if(reviewerRow.get(6) != null){
                        ComboBoxBean cmbBean = (ComboBoxBean)reviewerRow.get(6);
                        if(cmbBean != null ){
                            newReviewerBean.setRecommendedActionCode(cmbBean.getCode());
                        } else{
                            newReviewerBean.setRecommendedActionCode("");
                        }
                    }
                    
                    if( reviewerRow.get(2) != null){
                        Boolean reviewComplete = (Boolean) reviewerRow.get(1);
                        newReviewerBean.setReviewComplete(reviewComplete.booleanValue());
                    }
                    
                    newReviewerBean.setIsNonEmployee(((Boolean)reviewerRow.elementAt(7)).booleanValue());
                    // 3282: Reviewer View of Protocol materials - End
                    
                    // get reviewType code for the selected review type
                    for(int codeIndex=0; codeIndex < reviewerTypesSize;codeIndex++)
                    {
                        ComboBoxBean sCode = (ComboBoxBean) reviewerTypes.get(codeIndex);
                        if(sCode.getDescription().equals( reviewerRow.elementAt(3).toString())){
                            newReviewerBean.setReviewerTypeCode( Integer.parseInt(sCode.getCode()));
                            newReviewerBean.setReviewerTypeDesc( sCode.getDescription());
                            break;
                        }
                    }

                    // loop through available reviewers
                    if(existingReviewers != null){
                        for(revIndex = 0 ; revIndex < existingReviewers.size(); revIndex++ )
                        {
                            reviewerBean = (ProtocolReviewerInfoBean) existingReviewers.get(revIndex);
                            // add property change listener
                            reviewerBean.addPropertyChangeListener(
                                new PropertyChangeListener(){
                                    public void propertyChange(
                                            PropertyChangeEvent pce){
                                        reviewerBean.setAcType(UPDATE_RECORD);
                                        reviewersModified = true;
                                    }
                                }
                            );
                            if( reviewerBean.getPersonId().equals( newReviewerBean.getPersonId()))
                            {
                                found  = true;
                                foundIndex = revIndex;
                                break;
                            }
                        }
                    }
                    if( !found ){
                        // new reviewer so add it to existing reviewers
                        newReviewerBean.setAcType(INSERT_RECORD);
                        newReviewerBean.setAWPersonId(null);
                        newReviewerBean.setAWReviewerTypeCode(0);
                        reviewersModified = true;
                        reviewerPersonsChanged = true;//3283
                        if(existingReviewers == null){
                            existingReviewers = new Vector();
                        }
                        existingReviewers.addElement(newReviewerBean);
                    }else{
                        /* set the selected reviewer type for the existing 
                           reviewer and update in existing reviewers list */
                        reviewerBean.setReviewerTypeCode(newReviewerBean.getReviewerTypeCode());
                        // 3282: Reviewer View of Protocol materials - Start
                        reviewerBean.setAssignedDate(newReviewerBean.getAssignedDate());
                        reviewerBean.setDueDate(newReviewerBean.getDueDate());
                        reviewerBean.setRecommendedActionCode(newReviewerBean.getRecommendedActionCode());
                        reviewerBean.setReviewComplete(newReviewerBean.isReviewComplete());
                        // 3282: Reviewer View of Protocol materials - End
                        if(foundIndex != -1){
                            reviewerBean.setAcType(UPDATE_RECORD) ;
                            existingReviewers.set(foundIndex, reviewerBean);
                        }

                    }


                }
                /* loop through all available reviewers and check whether any
                 * reviewer has been deleted. If yes set acType to DELETE_RECORD
                 * and send.
                 */
                if(existingReviewers != null){
                    ProtocolReviewerInfoBean existingReviewer;
                    for( int index = 0 ; index < existingReviewers.size();
                            index++ ){
                        existingReviewer = (ProtocolReviewerInfoBean)
                            existingReviewers.get(index);
                        found = false;
                        for(int currIndex = 0;
                            currIndex < currentReviewers.size(); currIndex++){
                                Vector reviewerRow
                                    = (Vector)currentReviewers.get(currIndex);
                                if( existingReviewer.getPersonId().equals(
                                        reviewerRow.elementAt(0))){
                                            found = true;
                                            break;
                                }

                        }

                        if( !found ) {
                            existingReviewer.setAcType(DELETE_RECORD);
                            reviewersModified = true;
                            reviewerPersonsChanged = true;//3283
                            existingReviewers.set(index,existingReviewer);
                        }

                    }
                }
         }
        
           
        submissionBean.setProtocolReviewComments(
            protocolSubmissionInfoBean.getProtocolReviewComments());   
        submissionBean.setProtocolReviewer(existingReviewers);
        if(reviewersModified && functionType == MODIFY_MODE){
            submissionBean.setSequenceNumber(seqNo);
            submissionBean.setAcType(UPDATE_RECORD);
        }
        return submissionBean;
    }



public void submitProtocol() throws Exception
{
        if( validateData() )
        {
            
            /* in add mode check whether the reviewerSelectionForm is showed or
               not. If not showed, ask user whether he wants to identify the
               reviewers or not and show the reviewer selection form if he wishes.
               In Modify mode check whether there are any reviewers already selected.
               If not selected, check whether user is in ScheduleSelectionForm. 
               If yes, ask user to identify reviewers. If user is in 
               CommitteeSelectionForm and wishes to save the modifications by
               clicking 'X' button , dont ask the user to identify the reviewers
               though there are no reviewers selected.
             */
            if( functionType == ADD_MODE ) {
                if ( reviewerSelectionForm == null ){
                    identifyReviewers = true;
                }
            }else if ( functionType == MODIFY_MODE ) {
                if( ( submissionBean.getProtocolReviewer() == null ) 
                        &&   ( scheduleSelectionWindow != null ))
                {
                            identifyReviewers = true;
                }
            }
            
            saveSubmissionDetails();
            

        }
    }

    /**
     * This method is used to save the submission details to database after
     * getting confirmation from the user.
     */
    private void saveSubmissionDetails() throws Exception
    {
        /* setting protocol status to "Submitted to IRB" after submitting the
           protocol to a committee schedule */
        final int SUBMIT_TO_IRB = 101 ;
        // if commitee or schedule has changed then save change is reqd
//commented by ravi as we are setting saveRequired when committee/sechedule is seleted
//        if(!oldCommitteeId.equals(newCommitteeId))
//        {
//            saveRequired = true;
//        }
//        else if(!oldScheduleId.equals(newScheduleId))
//        {
//            saveRequired = true;
//        }
//        else if (isVotingDetailsChanged())
//        {
//            saveRequired = true;
//        }    
        
        /*Added for enhancements to Modify Submission details after submission is complete - by Nadh
         * Start 9 aug 2004
         */
        if (isVotingDetailsChanged()) 
        {
             saveRequired = true;
        }    
        //Nadh end 9 aug 2004
        if(isSaveRequired())
        {
//            int option = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(
//                        "protoSubmissionFrm_exceptionCode.2005"),
//                        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
            if(true) //option == CoeusOptionPane.SELECTION_YES)
            {
                ProtocolSubmissionInfoBean submissionInfoBean = getSubmissionData();
                String connectTo = CoeusGuiConstants.CONNECTION_URL
                        + "/SubmissionDetailsServlet";

                RequesterBean request = new RequesterBean();
                request.setFunctionType('U');
                //Added By sharath for CheckList - START
                /*Vector checkList;
                if((vecExemptToModify == null || vecExemptToModify.size() == 0)
                &&(vecExpeditedToModify == null || vecExpeditedToModify.size() == 0)) {
                    //First Time
                    submissionInfoBean.setProtocolExemptCheckList(getSubmissionBeans(vecExempt, null));
                    submissionInfoBean.setProtocolExpeditedCheckList(getSubmissionBeans(vecExpedited, null));
                }*/
                if(selectedItem.equalsIgnoreCase(EXEMPT)) {
                    setCheckedState(vecExpedited, false);
                    //submissionInfoBean.setProtocolExemptCheckList();
                }
                else if(selectedItem.equalsIgnoreCase(EXPEDITED)) {
                    setCheckedState(vecExempt, false);
                    //submissionInfoBean.setProtocolExpeditedCheckList();
                }
                else {
                    setCheckedState(vecExempt, false);
                    setCheckedState(vecExpedited, false);
                }
                submissionInfoBean.setProtocolExemptCheckList(getSubmissionBeans(vecExempt, vecExemptToModify));
                submissionInfoBean.setProtocolExpeditedCheckList(getSubmissionBeans(vecExpedited, vecExpeditedToModify));
                
                //Vector data = new Vector();
                //data.add(protocolInfo);
                //request.setDataObjects(data);
                
                //Added By sharath for CheckList - END
                Vector dataObjects = new Vector();
                dataObjects.addElement(submissionInfoBean);
                dataObjects.addElement(new Boolean( releaseScheduleLock ) );
                
                //Case #2080 Start 9
                dataObjects.addElement(vecActionDateData);
                //Case #2080 End 9
                
                request.setDataObjects(dataObjects);
                AppletServletCommunicator comm
                    = new AppletServletCommunicator(connectTo, request);

                setCursor( new Cursor( Cursor.WAIT_CURSOR ) );        
                comm.send();
                ResponderBean response = comm.getResponse();
                setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );

                if (!response.isSuccessfulResponse()) {
                    throw new Exception( response.getMessage());
               }else{
                   dataSaved = true;
                   submissionBean = ( ProtocolSubmissionInfoBean )response.getDataObject();
               }
           }
        }else{
            if( commentsLocked && txtScheduleId.getText()!= null ) {
                releaseUpdateLock();
            }
            
            //Case #2080 Start 10
            protocolActionForm.unRegisterObserver(this);
            //Case #2080 End 10
            
            thisWindow.dispose();
        }
    }

    public ProtocolSubmissionInfoBean getSavedData() {
        return submissionBean;
    }
    
     private int getMaxSubmissionNumber( String protocolId ) 
     {
        String connectTo = CoeusGuiConstants.CONNECTION_URL
                + "/protocolSubSrvlt";
        RequesterBean request = new RequesterBean();
        request.setFunctionType(PROTO_MAX_SUB_NUM);
        request.setId(protocolId);
        AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, request);
        /**
         * Updated for REF ID :0003  Feb'21 2003.
         * Hour Glass implementation while DB Trsactions Wait
         * by Subramanya Feb' 21 2003
         */
        setCursor( new Cursor( Cursor.WAIT_CURSOR ) );        
        comm.send();
        ResponderBean response = comm.getResponse();
        setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
        
        Integer count = new Integer(0);
        if (response.isSuccessfulResponse()) {
            count = (Integer)((Vector) response.getDataObjects()).elementAt(0);
        }
        if(count != null){
            return count.intValue();
        }
        return 0;
    }
   
     /** This method is used to get the selected Schedule from the list of
     * schedules displayed.
     * @return <CODE>ScheduleDetailsBean</CODE> which consists of
     * schedule id, scheduled date and maximum number of protocols reviewed in
     * that Schedule.
     */
   
    
    public ScheduleDetailsBean getSelectedSchedule()
    {
        return scheduleDetailsBean ;
    }    
       
     
     
      /** This method is used to check the validity of data entered by user.
     *
     * @return boolean true if all the entered data is valid else false.
     * @throws Exception Exception with the custom message will be thrown if the
     * data for any paritcular field is not valid.
     */
    private boolean validateData() throws Exception{
        /** string which holds the selected submission type description */
        String submissionType;

        /** string which holds the selected protocol review type description */
        String reviewType;

        submissionType = ((ComboBoxBean)
            cmbSubmissionType.getSelectedItem()).toString();

        reviewType =  ((ComboBoxBean)
            cmbReviewType.getSelectedItem()).toString();

        if(submissionType.trim().length() == 0){
            log(coeusMessageResources.parseMessageKey(
                "protoSubmissionFrm_exceptionCode.2000"));
            return false;
        }else if( reviewType.trim().length() == 0 ){
            log(coeusMessageResources.parseMessageKey(
                "protoSubmissionFrm_exceptionCode.2001"));
            return false;   
        } //prps start - jul 21 2003 added this validation(submission mode == MANDATORY)
        else if (submissionMode == MANDATORY) 
        {
            if (isCommitteeSelected()) 
            {    
                log(coeusMessageResources.parseMessageKey(
                    "commBaseWin_exceptionCode.1007"));
                return false;
            }
        }
        else if (submissionMode == MANDATORY)
        {
            if( isScheduleSelected())
            {
                log(coeusMessageResources.parseMessageKey(
                        "commSchdDetFrm_exceptionCode.1026"));
                return false;
            }
        }
        
        //Added By sharath for CheckList - START
        //to check if atleast one checklist is Selected
        if(selectedItem.equalsIgnoreCase(EXEMPT)) {
            checkList.setFormData(vecExempt);
            if(checkList.getData(true).size() == 0) {
                log(SELECT_ATLEAST_ONE_CHECKLIST);
                return false;
            }
        }else if(selectedItem.equalsIgnoreCase(EXPEDITED)) {
            checkList.setFormData(vecExpedited);
            if(checkList.getData(true).size() == 0){
                log(SELECT_ATLEAST_ONE_CHECKLIST);
                return false;
            }
        }
        //Added By sharath for CheckList - END
        if( checkScheduleLoad ) {
            int currentProtoCount = getProtocolSubCount( txtScheduleId.getText() );
            /* check whether schedule exceeds its maximum protocol
               review count. if exceeds inform the user with this detail.
               If he still wants to submit to the same schedule, let him
               submit */
            if ( currentProtoCount >= maxProtocols ) {
                int option = CoeusOptionPane.showQuestionDialog(

                coeusMessageResources.parseMessageKey(
                "protoSubmissionFrm_exceptionCode.2002"),
                CoeusOptionPane.OPTION_YES_NO,
                CoeusOptionPane.DEFAULT_YES);
                if( option == CoeusOptionPane.SELECTION_NO){
                    return false;
                }
            }
        
        }
        // 3282: Reviewer view of protocols - Start
        // Throw Validation message if the Assiged date is After the due date
        // for the selected reviewers.
        
        Vector vecSelReviewers = getSelectedReviewers();
        if(vecSelReviewers != null && vecSelReviewers.size() > 0){
            int size = vecSelReviewers.size();
            Vector reviewerData;
            String strAssigedDate = "";
            String strDueDate = "";
            Date assigedDate, dueDate = null;
            DateUtils dateUtils = new DateUtils();
            Date date  = null;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm/dd/yyyy");
            for(int index = 0; index < size; index++){
                reviewerData = (Vector) vecSelReviewers.elementAt(index);
                if( reviewerData != null ){
                    strAssigedDate = (String)reviewerData.elementAt(4);
                    strDueDate = (String) reviewerData.elementAt(5);
                    if(strAssigedDate != null && !strAssigedDate.equals("") &&
                            strDueDate != null && !strDueDate.equals("")){
                        assigedDate = simpleDateFormat.parse(strAssigedDate);
                        dueDate = simpleDateFormat.parse(strDueDate);
                        if(assigedDate.after(dueDate)){
                            CoeusOptionPane.showErrorDialog(
                                    coeusMessageResources.parseMessageKey("protoSubmissionFrm_exceptionCode.2012"));
                            return false;
                        }
                    }
                }
            }
        }
        
        // 3282: Reviewer view of protocols - End
        return true;
    }
    
    public boolean isCommitteeSelected()
    {
        if (txtCommiteeId.getText() != null)
        {    
            if (txtCommiteeId.getText().trim().length() <= 0)
            {
                return false ;
            }
        }
        return true ;
    }
    
    
    public boolean isScheduleSelected()
    {
        if (txtScheduleId.getText() != null)
        {    
            if (txtScheduleId.getText().trim().length() <= 0)
            {
                return false ;
            }
        }
        return true ;
    }
    
    
    /** This method is used to throw the exception with the specifed message.
     *
     * @param msg String representing custom message to display.
     * @throws Exception with specified custom message.
     */
    private static void log(String msg) throws Exception {
        throw new Exception(msg);
    }

   /** This method is used to check whether user has modified any existing
     * details.
     *
     * @return true if user modified any details, else false.
     */
    public boolean isSaveRequired()
    {
        return saveRequired;
    }
    
     /**
     * This method is used to check whether user has submitted the protocol 
     * to any other schedule than the one he selected earlier.
     *
     * @return boolean true if user changed his selection else false.
     */
    private boolean isScheduleChanged()
    {
        if(!oldScheduleId.equals(newScheduleId))
        {
            return true ;
        }    
       
        return false ;
    }
    
    private boolean isCommitteeChanged()
    {
        if(!oldCommitteeId.equals(newCommitteeId))
        {
            return true ;
        }
        return false ;
    }
    
   private boolean isReviewersChanged()
    {// just check if the old review list or the selected reviewers same
     // as the current one, if it so then return false else true   
        
        if (vecSelectedReviewers.equals(getSelectedReviewers()))
        {
            return true ;
        }
        return false ;
    }
   
    private void releaseUpdateLock() {
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/scheduleMaintSrvlt";
        RequesterBean requester = new RequesterBean();
        int subCount = vecSubmissionDetails.size();
        if( subCount > 0 ) {
            ProtocolSubmissionInfoBean protocolSubmissionInfoBean = 
                (ProtocolSubmissionInfoBean)vecSubmissionDetails.get( subCount - 1 );
            String lockedScheduleId = protocolSubmissionInfoBean.getScheduleId();
            if( lockedScheduleId != null ) {
                requester.setDataObject(lockedScheduleId);
                requester.setFunctionType('Z');
                AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);
                //comm.releaseUpdateLock(refId,"/scheduleMaintSrvlt");
                comm.send();
                ResponderBean res = comm.getResponse();
                if (res != null && !res.isSuccessfulResponse()){
                    CoeusOptionPane.showErrorDialog(res.getMessage());
                    return;
                }
            }
        }
    
    }
    //*** submission end
    
    //Case #2080 Start 11
    public void update(Observable o, Object arg) {
        if(arg instanceof Vector){
            //COEUSQA:3187 - Error when correcting Action Date on the Submission Details window - Start
//            vecActionDateData = (Vector)arg;
//            btnSubmit.setEnabled(true);
//            saveRequired = true;
            Vector vecActionDetails = (Vector)arg;
            String isActionDateInValid = (String)vecActionDetails.get(0);
            if("true".equals(isActionDateInValid)){
                isInValidActionDate = true;
            }else{
                isInValidActionDate = false;
                btnSubmit.setEnabled(true);
                saveRequired = true;
            }
            vecActionDateData = (Vector)vecActionDetails.get(1);
            //COEUSQA:3187 - End
        }
    }
      
    //Case #2080 End 11
    //Added for Case #1264 - Start 3
    private void setProtocolActionsData(int curRecord){
            
            ProtocolSubmissionInfoBean protocolSubmissionInfoBean =
                (ProtocolSubmissionInfoBean)vecSubmissionDetails.get(curRecord);
            Vector newCVActions = getActionsDetails(protocolSubmissionInfoBean);
            if(newCVActions!=null && newCVActions.size() > 0){
                protocolActionForm.showActions(newCVActions);
                protocolActionForm.setFormData();
            }
           this.comboBean = (ComboBoxBean)cmbSubmissionType.getSelectedItem();
           if(this.comboBean.getCode().equals(FYI)){
               cmbSubmissionType.setEnabled(false);
           }            
           if(newCVActions != null && newCVActions.size() > 0){
               ProtocolActionsBean actionBean = (ProtocolActionsBean)newCVActions.get(0);
               btnViewAttachment.setEnabled(actionBean.isIsDocumentExists());
           }            
    }
    private Vector getActionsDetails(ProtocolSubmissionInfoBean detailsBean)
    {
        Vector vecDetails= new Vector() ;
        try
       {   
          RequesterBean requester = new RequesterBean();
          requester.setFunctionType('A') ;
          requester.setDataObject(detailsBean) ;
          final String SUBMISSION_DETAILS_SERVLET = "/SubmissionDetailsServlet" ;
          String connectTo =CoeusGuiConstants.CONNECTION_URL
            + SUBMISSION_DETAILS_SERVLET ;
            AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo,requester);
            comm.send();

            ResponderBean responder = comm.getResponse();
            if (responder.isSuccessfulResponse())
            {
                vecDetails = (Vector)responder.getDataObject();
            }
          
        }catch(Exception ex){
            System.out.println("  *** Error in getting action details ***  ") ; 
            ex.printStackTrace() ;
       }
        return vecDetails;
    }
    //Added for Case #1264 - End 3
   
    //Added for case#3088 - Error updating review comments - start
    private Vector getReviewComments() throws Exception{
        Vector vecReviewComments = new Vector();
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setDataObject(submissionBean);
        requesterBean.setFunctionType('R');        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL+SCHEDULE_MAINTENENCE_SERVLET);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();        
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean.isSuccessfulResponse()){
            vecReviewComments = responderBean.getDataObjects();
        }    
        return vecReviewComments;
    }
    //Added for case#3088 - Error updating review comments - end
    
    /**
     * Code added for Case#3554 - Notify IRB enhancement
     * This method facilitates the view of blob data
     * @throws Exception
     */
    private void viewSubmissionDocument() throws Exception{
        
        String templateUrl =  null;
        DocumentBean documentBean = new DocumentBean();
        RequesterBean requesterBean = new RequesterBean();
        ProtocolActionDocumentBean protocolActionDocumentBean = new ProtocolActionDocumentBean();
        protocolActionDocumentBean.setProtocolNumber(submissionBean.getProtocolNumber());
        protocolActionDocumentBean.setSequenceNumber(submissionBean.getSequenceNumber());
        protocolActionDocumentBean.setSubmissionNumber(submissionBean.getSubmissionNumber());
        protocolActionDocumentBean.setDocumentId(1);
        Map map = new HashMap();
        map.put("DOCUMENT_TYPE", "SUBMISSION_DOC_DB");
        map.put("PROTO_ACTION_BEAN", protocolActionDocumentBean);
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.irb.ProtocolDocumentReader");
        documentBean.setParameterMap(map);
        requesterBean.setDataObject(documentBean);
        requesterBean.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(STREMING_SERVLET, requesterBean);
        comm.send();
        ResponderBean responderBean = comm.getResponse();
        responderBean = comm.getResponse();
        if(!responderBean.isSuccessfulResponse()){
            throw new CoeusException(responderBean.getMessage(),0);
        }
        map = (Map)responderBean.getDataObject();
        templateUrl = (String)map.get(DocumentConstants.DOCUMENT_URL);       
        
        try{
            URL urlObj = new URL(templateUrl);
            URLOpener.openUrl(urlObj);
        }catch(MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();            
        }catch(Exception ue) {
            ue.printStackTrace() ;
        }        
    }    
    // 3282: reviewer view of protocols- Start
    private class SelectedReviewersTableHeaderRenderer extends DefaultTableCellRenderer{
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setFont(table.getTableHeader().getFont());
            setText((value == null) ? "" : value.toString());
            setPreferredSize(new Dimension(50,35));
            setBorder(UIManager.getBorder("TableHeader.cellBorder"));
            setHorizontalAlignment(JLabel.CENTER);
            return this;
        }
        
    }
    
    
    public Vector getRecommendedActionTypes() {
        return recommendedActionTypes;
    }
    
    public void setRecommendedActionTypes(Vector recommendedActionTypes) {
        this.recommendedActionTypes = recommendedActionTypes;
    }
    // 3282: reviewer view of protocols - End
    //Added for case 3283: Reviewer Notification
    public boolean isReviewerPersonsChanged() {
        return reviewerPersonsChanged;
    }
    //3283 End
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel ProtocolDetails;
    public javax.swing.JLabel VotingDetails;
    public javax.swing.JButton btnAdd;
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnCheckList;
    public javax.swing.JButton btnCommittee;
    public javax.swing.JButton btnDelete;
    public javax.swing.JButton btnNext;
    public javax.swing.JButton btnPrev;
    public javax.swing.JButton btnReviewComments;
    public javax.swing.JButton btnSchedule;
    public javax.swing.JButton btnSubmit;
    public javax.swing.JButton btnViewAttachment;
    public edu.mit.coeus.utils.CoeusComboBox cmbReviewType;
    public edu.mit.coeus.utils.CoeusComboBox cmbSubmissionType;
    public javax.swing.JComboBox cmbTypeQualifier;
    public javax.swing.JLabel lblAbstainerCount;
    public javax.swing.JLabel lblActions;
    public javax.swing.JLabel lblApplicationDate;
    public javax.swing.JLabel lblCommitteeId;
    public javax.swing.JLabel lblCommitteeName;
    public javax.swing.JLabel lblPI;
    public javax.swing.JLabel lblProtocolId;
    public javax.swing.JLabel lblProtocolTitle;
    public javax.swing.JLabel lblReviewType;
    public javax.swing.JLabel lblReviewers;
    public javax.swing.JLabel lblScheduleDate;
    public javax.swing.JLabel lblScheduleId;
    public javax.swing.JLabel lblSchedulePlace;
    public javax.swing.JLabel lblSubmissionDate;
    public javax.swing.JLabel lblSubmissionDetails;
    public javax.swing.JLabel lblSubmissionNoVoteCount;
    public javax.swing.JLabel lblSubmissionStatusCode;
    public javax.swing.JLabel lblSubmissionTypeCode;
    public javax.swing.JLabel lblSubmissionTypeQualCode;
    public javax.swing.JLabel lblSubmissionVoteCount;
    public javax.swing.JLabel lblSubmissionYesVoteCount;
    public javax.swing.JLabel lblVotingComments;
    public javax.swing.JPanel pnlBtn;
    public javax.swing.JPanel pnlMain;
    public javax.swing.JPanel pnlReview;
    public javax.swing.JPanel pnlSubmission;
    public javax.swing.JScrollPane scrPnAvailableReviewers;
    public javax.swing.JScrollPane scrPnCommentsContainer;
    public javax.swing.JScrollPane scrPnSelectedReviewers;
    public javax.swing.JScrollPane scrpneActions;
    public javax.swing.JScrollPane scrpnlProtocolTitle;
    public javax.swing.JTable tblAvailableReviewers;
    public javax.swing.JTable tblSelectedReviewers;
    public edu.mit.coeus.utils.CoeusTextField txtAbstainerCount;
    public edu.mit.coeus.utils.CoeusTextField txtApplicationDate;
    public javax.swing.JTextField txtCommiteeId;
    public javax.swing.JTextField txtCommitteeName;
    public edu.mit.coeus.utils.CoeusTextField txtPI;
    public edu.mit.coeus.utils.CoeusTextField txtProtocolId;
    public javax.swing.JTextArea txtProtocolTitle;
    public javax.swing.JTextField txtScheduleDate;
    public edu.mit.coeus.utils.CoeusTextField txtScheduleId;
    public javax.swing.JTextField txtSchedulePlace;
    public javax.swing.JTextField txtSubmissionDate;
    public javax.swing.JTextField txtSubmissionNoVoteCount;
    public edu.mit.coeus.utils.CoeusTextField txtSubmissionStatusCode;
    public javax.swing.JTextField txtSubmissionStatusDesc;
    public javax.swing.JTextField txtSubmissionYesVoteCount;
    public javax.swing.JTextArea txtVotingComments;
    // End of variables declaration//GEN-END:variables
    
}
