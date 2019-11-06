/*
 * @(#)SubmissionBaseWindow.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

/* PMD check performed, and commented unused imports and variables on 16-JULY-2010
 * by Md.Ehtesham Ansari
 */
package edu.mit.coeus.iacuc.gui;

import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.search.gui.CoeusSearch;
//import edu.mit.coeus.irb.bean.SubmissionDetailsBean;
//import edu.mit.coeus.irb.bean.ProtocolInfoBean;
//import edu.mit.coeus.irb.bean.ProtocolInvestigatorsBean;
//import edu.mit.coeus.irb.bean.ProtocolSubmissionInfoBean;
import edu.mit.coeus.brokers.* ; //prps added
import edu.mit.coeus.utils.* ; //prps added
import edu.mit.coeus.exception.*;
import edu.mit.coeus.iacuc.bean.*;
import edu.mit.coeus.iacuc.controller.ProtocolMailController;
//import edu.mit.coeus.utils.MultipleTableColumnSorter;
//import edu.mit.coeus.utils.SortForm;

//Added by sharath - 19/10/2003 for Save As ToolBar Button - Start
import edu.mit.coeus.utils.saveas.SaveAsDialog;
//Added by sharath - 19/10/2003 for Save As ToolBar Button - End

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
import java.util.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import edu.mit.coeus.irb.bean.AdhocDetailsBean;

/** This class is used to construct the parent window of Protocol Submission and associate
 * its menu and toolbar with listener. T-his window will display the submitted Protocols
 * from which the user can select the protocol details.
 *
 * @author ravikanth
 */

public class SubmissionBaseWindow extends CoeusInternalFrame
        implements ActionListener, Observer {
    
    // Menu items for protocol
    private CoeusMenuItem protocolDisplay,protocolModify,
            submissionSearch, protocolSubmission;
    
    //prps start jan 14 2003
    private CoeusMenuItem printAdhoc ;
    //prps start jan 14 2003
    
    
    //Added by sharath for Review comments from Submission List widow - Start
    private CoeusMenuItem protocolReviewComments;
    private ReviewCommentsForm reviewCommentsForm;
    private static final String SCHEDULE_MAINTENANCE_SERVLET = "/scheduleMaintSrvlt";
//    private static final int PROTOCOL_NUMBER_COLUMN = 0;
//    private static final int COMMITTEE_ID_COLUMN = 6;
//    private static final int SCHEDULE_ID_COLUMN = 7;
    private static final int SUBMISSION_NUMBER_COLUMN = 11;
//    private static final int SEQUENCE_NUMBER_COLUMN = 12;
    private static final int INDEX_COLUMN = 13;
    //Added for code refactoring
     private static final String PROTOCOL_NUMBER = "PROTOCOL_NUMBER";
     private static final String SEQUENCE_NUMBER = "SEQUENCE_NUMBER";
     private static final String SUBMISSION_NUMBER = "SUBMISSION_NUMBER";
     private static final String COMMITTEE_ID = "COMMITTEE_ID";
     private static final String SCHEDULE_ID = "SCHEDULE_ID";
    //Added by sharath for Review comments from Submission List widow - End
    
    // added by manoj for new enhancements 01/09/2003
    //Modified for Coeus enhancement Case #1791  - step 1: start
    //Modified for Coeus enhancement Case #1880  - step 1: start
    private CoeusMenuItem 
            //protocolExpedited,
//            protocolIrbAcknowledgement,
//            protocolClose,protocolTerminate,protocolSuspend,
//            protocolDisapprove,protocolWithdrawn,protocolGrantExemption,protocolIrbReviewNotRequired,protocolCloseEnrollment,
//            substantiveRequired,specificRequired,assignSchedule,
            protocolResponse,designatedApprove,minorRevisionRequired,majorRevisionRequired,returnToPI,iacucAcknow,iacucReviewNotReq,liftHold,adminWithdrawn,disapprove,expire,
            deactivate,adminDeactivate,hold,terminate,suspend,withdrawSubmission,notifyCommittee,
            // Added for COEUSQA-2666: Complete Administrative Review functionality in IACUC
            administrativeApprove,adminstrativeIncomplete;
            // COEUSQA-2666: End
    

    //Coeus enhancement Case #1880 - step 1: end
    //Coeus enhancement Case #1791 - step 1: end
    
    //Added for performing non request actions - start - 1
    private CoeusMenuItem protocolReopen, protocolDA;
    //Added for performing non request actions - end - 1
    
    
    // Toolbar for protocol
    private CoeusToolBarButton btnModifyProtocol;
    private CoeusToolBarButton btnDisplayProtocol;
    private CoeusToolBarButton btnSearchSubmission;
    private CoeusToolBarButton btnSubmissionDetails;
    private CoeusToolBarButton btnCloseSubmission;
    private CoeusToolBarButton btnSort;//Added by nadh - 19/01/2005 for Sort ToolBar Button
    
    //Added by sharath - 19/10/2003 for Save As ToolBar Button - Start
    private CoeusToolBarButton btnSaveAsSubmission;
    //Added by sharath - 19/10/2003 for Save As ToolBar Button - End
    
    String actionDescription; //Added by Vyjayanthi
    
//    private String windowName;
    // holds the search window name
    private final String SUBMISSION_SEARCH = "IACUCPROTOSUBMISSIONSEARCH";
    // holds the instance of CoeusSearch
    private CoeusSearch coeusSearch;
    
    ProtocolDetailForm mainProtocol;
    //Main MDI Form.
    private CoeusAppletMDIForm mdiForm = null;
    // added by manoj to perform protocol actions 03/09/2003
    private ProcessAction processAction = ProcessAction.getInstance();
    // modify functionality
    private final char MODIFY_FUNCTION = 'M';
    // display functionality
    private final char DISPLAY_FUNCTION = 'D';
    
    private final char CHECK_IF_EDITABLE = 'C' ; //prps added
    
    
//    private int selectedprotocolRow = -1;
    // Jtable
    private JTable tblProtocol;
    // Scroll bar pane
    private JScrollPane scrPnSearchRes;
    
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    /*
     * to indicate horizondal seperator in menu items
     */
    private final String SEPERATOR="seperator";
    
    private final String SUBMISSION_DETAILS_SERVLET = "/IacucProtoSubmissionDetailsServlet" ;
    
    private String PIName;
    
    private int baseTableRow;
    
    private HashMap searchResults = new HashMap();
//    private final int NOTIFY_COMMITTEE = 109;
    
    private Vector vDetails;// holds submission details
    
    private Vector vDataObjects;//holds the total vector which includes the submission and action details.
    
    //Added by nadh - 19/01/2005 for Sort ToolBar Button - Start
    
    //holds sorted columns and its states
    private Vector vecSortedData;
    
    private static final int OK_CLICKED = 0;
    
    //holds column index
    private int oldCol = -1;
    
    //holds column status
    private int status=MultipleTableColumnSorter.NOT_SORTED;
    //Added by Nadh - End
    
    //Added for COEUSQA-3399 : IRB Protocol Renewal Reminder notification error - start
    private static final int IACUC_ACKNOWLEDGEMENT_SUB_STATUS = 205;
    private static final int APPROVED_SUB_STATUS = 200;
    //Added for COEUSQA-3399 : IRB Protocol Renewal Reminder notification error.  - end
    
    //Added for case#3243
    private static final String SIMPLE_DATE_FORMAT = "yyyy/MM/dd";    
    private static final int NOTIFY_COMMITTEE_SUBM_STATUS_CODE = 102;
    /** Constructor to create new <CODE>SubmissionBaseWindow</CODE>
     *
     * @param mdiForm reference to <CODE>CoeusAppletMDIForm</CODE>.
     */
    public SubmissionBaseWindow(CoeusAppletMDIForm mdiForm) {
        super("IACUC Protocol Submission List", mdiForm,LIST_MODE);
        this.mdiForm = mdiForm;
        try {
            initComponents();
            mdiForm.putFrame("IACUC Protocol Submission List", this);
            mdiForm.getDeskTopPane().add(this);
            this.setSelected(true);
            this.setVisible(true);
            showSubmissionSearchWindow();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
    /**
     * Construct the components for the base window
     *
     * throws Exception
     */
    private void initComponents() throws Exception {
        setFrameMenu(getSubmissionEditMenu());
        // added by manoj for new enhancement 01/09/2003
        setActionsMenu(getSubmissionActionsMenu());
        setToolsMenu(getSubmissionToolsMenu());
        setFrameToolBar(getSubmissionToolBar());
        setFrame("IACUC Protocol Submission List");
        setFrameIcon(mdiForm.getCoeusIcon());
        createSubmissionInfo();
        tblProtocol.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        coeusMessageResources = CoeusMessageResources.getInstance();
    }
    
    //Added for CoeusSearch enhancement(CustomQuery)   start step:1
    private CoeusToolBarButton[] searchButtons;
    /*  prepares toolbar buttons for custom queries
     * @param vecToolBarData contains button tooltips
     */
    private void prepareCQToolBarButtons(Vector vecToolBarData) {
        if(vecToolBarData == null){
            return;
        }
        int size = vecToolBarData.size();
        searchButtons = new CoeusToolBarButton[size];
        for(int i=0;i<size;i++) {
            searchButtons[i] = new CoeusToolBarButton(new ImageIcon(
                    getClass().getClassLoader().getResource("images/search"+i+".gif")),
                    null, (String)vecToolBarData.get(i));
            searchButtons[i].addActionListener(this);
            searchButtons[i].setActionCommand(""+i);
            JToolBar tbProtocol = getFrameToolBar();
            tbProtocol.add(searchButtons[i],6+i);
            this.revalidate();
        }
    }//End : step:1
    
    /**
     * This methods the empty protocol table for the base window
     *
     */
    private void createSubmissionInfo() throws Exception {
        coeusSearch = new CoeusSearch(mdiForm, SUBMISSION_SEARCH, 0);
        prepareCQToolBarButtons(coeusSearch.getCustomQueryData());//Added  for CoeusSearch enhancement(CustomQuery)   start : step:2
        tblProtocol = coeusSearch.getEmptyResTable();
        scrPnSearchRes = new JScrollPane();
        scrPnSearchRes.setMinimumSize(new Dimension(22, 15));
        //scrPnSearchRes.setPreferredSize(new Dimension(600, 400));
        if( tblProtocol != null ){
            tblProtocol.setFont( CoeusFontFactory.getNormalFont() );
        }
        scrPnSearchRes.setViewportView(tblProtocol);
        scrPnSearchRes.getViewport().setBackground(Color.white);
        scrPnSearchRes.setForeground(Color.white);
        getContentPane().add(scrPnSearchRes);
        javax.swing.table.TableColumn clmName
                = tblProtocol.getColumnModel().getColumn(
                tblProtocol.getColumnCount()-1);
        clmName.setMaxWidth(0);
        clmName.setMinWidth(0);
        clmName.setPreferredWidth(0);
        clmName = tblProtocol.getColumnModel().getColumn(tblProtocol.getColumnCount()-2);
        clmName.setMaxWidth(0);
        clmName.setMinWidth(0);
        clmName.setPreferredWidth(0);
        clmName = tblProtocol.getColumnModel().getColumn(
                SUBMISSION_NUMBER_COLUMN);
        clmName.setMaxWidth(0);
        clmName.setMinWidth(0);
        clmName.setPreferredWidth(0);
        javax.swing.table.JTableHeader header
                = tblProtocol.getTableHeader();
        //header.setResizingAllowed(false);
        header.setReorderingAllowed(false);
        setSearchResultsTable(tblProtocol);
        
    }
    
    /**
     * This methods loads the search window when the protocol module is opened,
     * this helps the user to load the base window with the protocol details.
     *
     */
    private void showSubmissionSearchWindow() {
        try {
            vecSortedData = new Vector();
            coeusSearch.showSearchWindow();
            JTable tblResultsTable = coeusSearch.getSearchResTable();
            //setSearchResultsTable(tblResultsTable);
            if (tblResultsTable != null) {
                tblProtocol = tblResultsTable;
                updateSearchResults();
                scrPnSearchRes.setViewportView(tblProtocol);
                javax.swing.table.TableColumn clmName
                        = tblProtocol.getColumnModel().getColumn(
                        tblProtocol.getColumnCount()-1);
                clmName.setMaxWidth(0);
                clmName.setMinWidth(0);
                clmName.setPreferredWidth(0);
                clmName = tblProtocol.getColumnModel().getColumn(tblProtocol.getColumnCount()-2);
                clmName.setMaxWidth(0);
                clmName.setMinWidth(0);
                clmName.setPreferredWidth(0);
                clmName = tblProtocol.getColumnModel().getColumn(
                        SUBMISSION_NUMBER_COLUMN);
                clmName.setMaxWidth(0);
                clmName.setMinWidth(0);
                clmName.setPreferredWidth(0);
                javax.swing.table.JTableHeader header
                        = tblProtocol.getTableHeader();
                //header.setResizingAllowed(false);
                header.setReorderingAllowed(false);
                tblProtocol.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                tblProtocol.setRowSelectionInterval(0,0);
                //Added by nadh - 19/01/2005 for table header
                tblProtocol.getTableHeader().addMouseListener(new SubmissionMouseAdapter());
                //Added by Nadh - End
                tblProtocol.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent me) {
                        if (me.getClickCount() == 2) {
                            try {
                                int selectedRow = tblProtocol.getSelectedRow();
                                String protocolNumber = getSelectedValue(selectedRow, PROTOCOL_NUMBER);
                                
                                //Bug fix:1062 Hour glass implementation START
                                mdiForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
                                showProtocolDetails(protocolNumber);
                                mdiForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                                //Bug fix:1062 Hour glass implementation END
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
            //this.revalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //Added by Nadh for CustomQuery Search enhancement   start : step:4
    private void buildSearchResultsTable(JTable tblResultsTable) {
        if (tblResultsTable != null) {
            tblProtocol = tblResultsTable;
            javax.swing.table.TableColumn clmName
                    = tblProtocol.getColumnModel().getColumn(
                    tblProtocol.getColumnCount()-1);
            clmName.setMaxWidth(0);
            clmName.setMinWidth(0);
            clmName.setPreferredWidth(0);
            
            clmName = tblProtocol.getColumnModel().getColumn(tblProtocol.getColumnCount()-2);
            clmName.setMaxWidth(0);
            clmName.setMinWidth(0);
            clmName.setPreferredWidth(0);
            
            javax.swing.table.JTableHeader header
                    = tblProtocol.getTableHeader();
            //header.setResizingAllowed(false);
            header.setReorderingAllowed(false);
            scrPnSearchRes.setViewportView(tblProtocol);
            tblProtocol.getTableHeader().addMouseListener(new SubmissionMouseAdapter());
            tblProtocol.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent me) {
                    if (me.getClickCount() == 2) {
                        try {
                            int selectedRow = tblProtocol.getSelectedRow();
                            String protocolNumber = getSelectedValue(selectedRow, PROTOCOL_NUMBER);
                            
                            //BUG FIX,bug Id:1062 - Hour glass Implemntation START
                            mdiForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
                            showProtocolDetails(protocolNumber);
                            mdiForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                            //BUG FIX,bug Id:1062 - Hour glass Implemntation END
                        } catch (Exception e) {
                            e.printStackTrace();
//                                CoeusOptionPane.showErrorDialog(e.getMessage());
                        }
                    }
                }
            });
        }
        this.revalidate();
    }//End step:4
    
    /**
     * constructs Protocol edit menu with sub menu Add,Modify,Dispaly,Copy and
     * protocol Deatil Forms.
     *
     * @return CoeusMenu Protocol edit menu
     */
    private CoeusMenu getSubmissionEditMenu() {
        CoeusMenu mnuProtocol = null;
        Vector fileChildren = new Vector();
        
        protocolModify = new CoeusMenuItem("Modify IACUC Protocol", null, true, true);
        protocolModify.setMnemonic('M');
        protocolModify.addActionListener(this);
        
        protocolDisplay = new CoeusMenuItem("Display IACUC Protocol", null, true, true);
        protocolDisplay.setMnemonic('D');
        protocolDisplay.addActionListener(this);
        
        protocolSubmission = new CoeusMenuItem("Submission Details", null, true, true);
        protocolSubmission.setMnemonic('S');
        protocolSubmission.addActionListener(this);
        
        //Added by sharath for Review comments from Submission List widow - Start
        protocolReviewComments = new CoeusMenuItem("Review Comments", null, true, true);
        protocolReviewComments.setMnemonic('R');
        protocolReviewComments.addActionListener(this);
        //Added by sharath for Review comments from Submission List widow - End
        
        //prps start jan 14 2003
        printAdhoc = new CoeusMenuItem("Generate Correspondence",null,true,true);
        printAdhoc.setMnemonic('C');
        printAdhoc.addActionListener(this);
        //prps end jan 14 2003
        
        fileChildren.add(protocolModify);
        fileChildren.add(protocolDisplay);
        fileChildren.add(protocolSubmission);
        //Added by sharath for Review comments from Submission List widow - Start
        fileChildren.add(protocolReviewComments);
        //Added by sharath for Review comments from Submission List widow - End
        
        //prps start jan 14 2003
        fileChildren.add(SEPERATOR);
        fileChildren.add(printAdhoc) ;
        //prps end jan 14 2003
        
        mnuProtocol = new CoeusMenu("Edit", null, fileChildren, true, true);
        mnuProtocol.setMnemonic('E');
        return mnuProtocol;
        
    }
    // added by manoj for new enhancements
    /**
     * Constructs Submission actions menu with sub menus Expedited approval, Closed,
     * Terminated, Suspend Protocol, Disapproved, Withdrawn
     *
     *
     * @return CoeusMenu Submission Actions menu
     */
    private CoeusMenu getSubmissionActionsMenu() {
        CoeusMenu mnuProtocolActions = null;
        Vector fileChildren = new Vector();
        
//        protocolExpedited = new CoeusMenuItem("Expedited Approval", null, true, true);
//        protocolExpedited.setMnemonic('E');
//        protocolExpedited.addActionListener(this);
//        protocolExpedited.setActionCommand(""+ProtocolActionsInterface.ACTION_EXPEDITED_APPROVAL);
//        
        //Added by nadh for Response Approval enhencement - start
        protocolResponse = new CoeusMenuItem("Response Approval", null, true, true);
        protocolResponse.setMnemonic('R');
        protocolResponse.addActionListener(this);
        protocolResponse.setActionCommand(""+IacucProtocolActionsConstants.RESPONSE_APPROVAL);
        
        designatedApprove = new CoeusMenuItem("Designated Review Approval", null, true, true);
        designatedApprove.setMnemonic('D');
        designatedApprove.addActionListener(this);
        designatedApprove.setActionCommand(""+IacucProtocolActionsConstants.DESIGNATED_REVIEW_APPROVAL);
        
        // Added for COEUSQA-2666: Complete Administrative Review functionality in IACUC - Start
        administrativeApprove = new CoeusMenuItem("Administrative Approval", null, true, true);
        administrativeApprove.setMnemonic('V');
        administrativeApprove.addActionListener(this);
        administrativeApprove.setActionCommand(""+IacucProtocolActionsConstants.ADMINISTRATIVE_APPROVAL);
        // COEUSQA-2666: End
        
        minorRevisionRequired = new CoeusMenuItem("Minor Revisions Required", null, true, true);
        minorRevisionRequired.setMnemonic('M');
        minorRevisionRequired.addActionListener(this);
        minorRevisionRequired.setActionCommand(""+IacucProtocolActionsConstants.MINOR_REVISIONS_REQUIRED);
        
        majorRevisionRequired = new CoeusMenuItem("Major Revisions Required", null, true, true);
        majorRevisionRequired.setMnemonic('A');
        majorRevisionRequired.addActionListener(this);
        majorRevisionRequired.setActionCommand(""+IacucProtocolActionsConstants.MAJOR_REVISIONS_REQUIRED);
        
        returnToPI = new CoeusMenuItem("Return to PI", null, true, true);
        returnToPI.setMnemonic('P');
        returnToPI.addActionListener(this);
        returnToPI.setActionCommand(""+IacucProtocolActionsConstants.RETURNED_TO_PI);
        
        // Added for COEUSQA-2666: Complete Administrative Review functionality in IACUC - start
        adminstrativeIncomplete = new CoeusMenuItem("Administratively Incomplete", null, true, true);
        adminstrativeIncomplete.setMnemonic('C');
        adminstrativeIncomplete.addActionListener(this);
        adminstrativeIncomplete.setActionCommand(""+IacucProtocolActionsConstants.ADMINISTRATIVELY_INCOMPLETE);
        // COEUSQA-2666: End
        
        iacucAcknow = new CoeusMenuItem("IACUC Acknowledgement", null, true, true);
        iacucAcknow.setMnemonic('A');
        iacucAcknow.addActionListener(this);
        iacucAcknow.setActionCommand(""+IacucProtocolActionsConstants.IACUC_ACKNOWLEDGEMENT);
        
        iacucReviewNotReq = new CoeusMenuItem("IACUC Review Not Required", null, true, true);
        iacucReviewNotReq.setMnemonic('I');
        iacucReviewNotReq.addActionListener(this);
        iacucReviewNotReq.setActionCommand(""+IacucProtocolActionsConstants.IACUC_REVIEW_NOT_REQUIRED);
        
        liftHold = new CoeusMenuItem("Lift Hold", null, true, true);
        liftHold.setMnemonic('L');
        liftHold.addActionListener(this);
        liftHold.setActionCommand(""+IacucProtocolActionsConstants.LIFT_HOLD);
        
//        adminWithdrawn = new CoeusMenuItem("Administratively Withdraw", null, true, true);
//        adminWithdrawn.setMnemonic('P');
//        adminWithdrawn.addActionListener(this);
//        adminWithdrawn.setActionCommand(""+IacucProtocolActionsConstants.ADMINISTRATIVELY_WITHDRAWN);
//
        
        disapprove = new CoeusMenuItem("Disapprove", null, true, true);
        disapprove.setMnemonic('D');
        disapprove.addActionListener(this);
        disapprove.setActionCommand(""+IacucProtocolActionsConstants.DISAPPROVED);
        		   
        expire = new CoeusMenuItem("Expire", null, true, true);
        expire.setMnemonic('E');
        expire.addActionListener(this);
        expire.setActionCommand(""+IacucProtocolActionsConstants.EXPIRED);
        
        
        deactivate = new CoeusMenuItem("Deactivate", null, true, true);
        deactivate.setMnemonic('A');
        deactivate.addActionListener(this);
        deactivate.setActionCommand(""+IacucProtocolActionsConstants.DEACTIVATED);
        
        
//        adminDeactivate = new CoeusMenuItem("Administratively Deactivate", null, true, true);
//        adminDeactivate.setMnemonic('P');
//        adminDeactivate.addActionListener(this);
//       adminDeactivate.setActionCommand(""+IacucProtocolActionsConstants.ADMINISTRATIVELY_DEACTIVATED);
        
        hold = new CoeusMenuItem("Hold", null, true, true);
        hold.setMnemonic('H');
        hold.addActionListener(this);
        hold.setActionCommand(""+IacucProtocolActionsConstants.HOLD);
        
        
        terminate = new CoeusMenuItem("Terminate", null, true, true);
        terminate.setMnemonic('T');
        terminate.addActionListener(this);
        terminate.setActionCommand(""+IacucProtocolActionsConstants.TERMINATED);
        
        suspend = new CoeusMenuItem("Suspend", null, true, true);
        suspend.setMnemonic('S');
        suspend.addActionListener(this);
        suspend.setActionCommand(""+IacucProtocolActionsConstants.SUSPENDED);
        
        withdrawSubmission = new CoeusMenuItem("Withdraw Submission", null, true, true);
        withdrawSubmission.setMnemonic('W');
        withdrawSubmission.addActionListener(this);
        withdrawSubmission.setActionCommand(""+IacucProtocolActionsConstants.WITHDRAWN);
        
        notifyCommittee = new CoeusMenuItem("Notify Committee", null, true, true);
        notifyCommittee.setMnemonic('N');
        notifyCommittee.addActionListener(this);
        notifyCommittee.setActionCommand(""+IacucProtocolActionsConstants.NOTIFY_COMMITTEE);
            
        fileChildren.add(protocolResponse);
        fileChildren.add(designatedApprove);
        // COEUSQA-2666: Complete Administrative Review functionality in IACUC
        fileChildren.add(administrativeApprove);
        // COEUSQA-2666: End
        fileChildren.add(SEPERATOR);
        fileChildren.add(minorRevisionRequired);
        fileChildren.add(majorRevisionRequired);
        fileChildren.add(returnToPI);
        // COEUSQA-2666: Complete Administrative Review functionality in IACUC
        fileChildren.add(adminstrativeIncomplete);
        // COEUSQA-2666: End
        fileChildren.add(SEPERATOR);
        fileChildren.add(iacucAcknow);
        fileChildren.add(iacucReviewNotReq);
        fileChildren.add(SEPERATOR);
//        fileChildren.add(adminWithdrawn);
        fileChildren.add(disapprove);
        fileChildren.add(expire);
        fileChildren.add(deactivate);
//        fileChildren.add(adminDeactivate);
        fileChildren.add(hold);
        fileChildren.add(liftHold);
        fileChildren.add(terminate);
        fileChildren.add(suspend);
        fileChildren.add(SEPERATOR);
        fileChildren.add(withdrawSubmission);
        fileChildren.add(SEPERATOR);
        fileChildren.add(notifyCommittee);

//        fileChildren.add(protocolIrbAcknowledgement);//Coeus enhancement Case #1791 - step 3
//        fileChildren.add(protocolGrantExemption);
//        fileChildren.add(protocolIrbReviewNotRequired);//Coeus enhancement Case #1880 - step 3
//        fileChildren.add(substantiveRequired);
//        fileChildren.add(specificRequired);
//        fileChildren.add(SEPERATOR);
//        fileChildren.add(protocolDisapprove);
//        fileChildren.add(protocolCloseEnrollment);
//        fileChildren.add(protocolTerminate);
//        fileChildren.add(protocolClose);
//        fileChildren.add(protocolSuspend);
        //Added for performing non request actions - start - 3
       // fileChildren.add(protocolReopen);
       // fileChildren.add(protocolDA);
        //Added for performing non request actions - end - 3
      //  fileChildren.add(SEPERATOR);
//        fileChildren.add(protocolWithdrawn);
    //    fileChildren.add(SEPERATOR);
//        fileChildren.add(assignSchedule);
        mnuProtocolActions = new CoeusMenu("Actions", null, fileChildren, true, true);
        mnuProtocolActions.setMnemonic('S');
        return mnuProtocolActions;
        
    }
    
    /** Constructs Protocol Tools menu for Search screen with sub menu of <CODE>Search</CODE>
     *
     * @return <CODE>CoeusMenu</CODE> Protocol tools menu.
     */
    public CoeusMenu getSubmissionToolsMenu() {
        CoeusMenu coeusMenu;
        Vector fileChildren = new Vector();
        submissionSearch = new CoeusMenuItem("IACUC Protocol Submission Search", null, true, true);
        submissionSearch.setMnemonic('S');
        submissionSearch.addActionListener(this);
        fileChildren.add(submissionSearch);
        coeusMenu = new CoeusMenu("Tools", null, fileChildren, true, true);
        coeusMenu.setMnemonic('T');
        return coeusMenu;
    }
    
    /**
     * This method is used to create the tool bar for Save, Add, Mofify and
     * Close buttons.
     *
     * @returns JToolBar protocol Toolbar
     */
    private JToolBar getSubmissionToolBar() {
        JToolBar toolbar = new JToolBar();
        
        btnModifyProtocol = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.EDIT_ICON)),
                null, "Modify IACUC Protocol");
        btnDisplayProtocol = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.DISPLAY_ICON)),
                null, "Display IACUC Protocol");
        
        btnSubmissionDetails = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.PROTOCOL_SUBMISSION_ICON)),
                null, "Submission Details");
        
        btnSearchSubmission = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)),
                null, "IACUC Protocol Submission Search");
        
        btnCloseSubmission = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
                null, "Close Submission");
        
        //Added by sharath - 19/10/2003 for Save As ToolBar Button - Start
        btnSaveAsSubmission = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.SAVEAS_ICON)),
                null, "Save As");
        //Added by sharath - 19/10/2003 for Save As ToolBar Button - End
        
        //Added by nadh - 19/01/2005 for Sort ToolBar Button - Start
        btnSort = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.SORT_ICON)),null,
                "Sort IACUC Protocol Submission List");
        //Added by Nadh - End
        
        btnModifyProtocol.addActionListener(this);
        btnDisplayProtocol.addActionListener(this);
        btnSubmissionDetails.addActionListener(this);
        btnSearchSubmission.addActionListener(this);
        btnCloseSubmission.addActionListener(this);
        
        //Added by sharath - 19/10/2003 for Save As ToolBar Button - Start
        btnSaveAsSubmission.addActionListener(this);
        //Added by sharath - 19/10/2003 for Save As ToolBar Button - End
        
        btnSort.addActionListener(this);//Added by nadh - 19/01/2005 for Sort ToolBar Button
        
        toolbar.add(btnModifyProtocol);
        toolbar.add(btnDisplayProtocol);
        toolbar.add(btnSubmissionDetails);
        toolbar.add(btnSort);//Added by nadh - 19/01/2005 for Sort ToolBar Button
        toolbar.add(btnSearchSubmission);
        //Added by sharath - 19/10/2003 for Save As ToolBar Button - Start
        toolbar.add(btnSaveAsSubmission);
        //Added by sharath - 19/10/2003 for Save As ToolBar Button - End
        toolbar.addSeparator();
        toolbar.add(btnCloseSubmission);
        
        toolbar.setFloatable(false);
        return toolbar;
    }
    
    /** Get the Protocol number based on the search index column
     */
    private String getSelectedValue(int row, String colValue)
    throws CoeusClientException,CoeusException{
        int column = getColumnIndexValue(coeusSearch, colValue);
        return (String)tblProtocol.getValueAt(row, column);
    }
    
    /** Get the column index for the selected row, Get the data based on the
     *Display bean data
     */
    private int getColumnIndexValue( CoeusSearch coeusSearch, String columnName)
    throws CoeusClientException,CoeusException{
        int columnIndex = -1;
        try{
            SearchColumnIndex searchIndex = new SearchColumnIndex();
            columnIndex = searchIndex.getSearchColumnIndex(coeusSearch, columnName);
            searchIndex = null;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return columnIndex;
    }
    
    /** All the  actions for the menu and toolbar associated with
     * the <CODE>SubmissionBaseWindow</CODE> will be invoked from this method.
     *
     * @param actionType <CODE>ActionEvent</CODE>, a semantic event which indicates that a
     * component-defined action occured.
     */
    public void actionPerformed(ActionEvent actionType) {
        try{
            int selectedRow = 0;
            String protocolNumber = null;
            int seqNo = 0;
            int subNo = 0;
            String commId = null, schId=null;
            if (tblProtocol != null && tblProtocol.getRowCount() > 0) {
                selectedRow = tblProtocol.getSelectedRow();
                //Check whether row is selected
                if (selectedRow >= 0) {
//                    protocolNumber = tblProtocol.getValueAt(selectedRow, 0).toString();
                    protocolNumber = getSelectedValue(selectedRow, PROTOCOL_NUMBER);
//                    seqNo = Integer.parseInt((String)tblProtocol.getValueAt(
//                                selectedRow,tblProtocol.getColumnCount()-2));
                    seqNo = Integer.parseInt(getSelectedValue(selectedRow, SEQUENCE_NUMBER));
//                    subNo = Integer.parseInt((String)tblProtocol.getValueAt(
//                                selectedRow,SUBMISSION_NUMBER_COLUMN));
                    subNo = Integer.parseInt(getSelectedValue(selectedRow, SUBMISSION_NUMBER));
//                    commId = (String)tblProtocol.getValueAt(selectedRow, 6);
//                    schId = (String)tblProtocol.getValueAt(selectedRow, 7);
                    commId = getSelectedValue(selectedRow,COMMITTEE_ID);
                    schId = getSelectedValue(selectedRow,SCHEDULE_ID);
                }
            }
            
            Object actSource = actionType.getSource();
            
            if (actSource.equals(protocolModify) ||
                    actSource.equals(btnModifyProtocol)) {
                
                modifyProtocolDetails(protocolNumber);
            /* when the menu or tool bar display is clicked showProtocolDetails
             * method will be fired
             */
            } else if (actSource.equals(protocolDisplay) ||
                    actSource.equals(btnDisplayProtocol)) {
                showProtocolDetails(protocolNumber);
            } else if (actSource.equals(protocolSubmission) ||
                    actSource.equals(btnSubmissionDetails)) {
                SubmissionDetailsBean detailsBean = new SubmissionDetailsBean() ;
                detailsBean.setProtocolNumber(protocolNumber) ;
                detailsBean.setScheduleId(null) ; //scheduleId = null from here
                detailsBean.setSequenceNumber(new Integer(seqNo)) ; // seq num is also null
                
                detailsBean.setSubmissionNumber(new Integer(subNo)) ;
                
                Vector vecDetails = getSubmissionDetails(detailsBean) ;
                //Vector vecDetails = detailsBean.getSubmissionDetails() ;
//                Vector vecReviewers = null;
                //vecReviewers = detailsBean.getSelectedReviewers() ;
                // Added for COEUSQA-3012:notification for when a reviewer completes their review in IACUC - start
                Vector vecReviewers = (Vector)vDataObjects.get(0) ;
                // Added for COEUSQA-3012 - end
                if (vecDetails.size() <= 0) {
                    //display appropriate msg
                    CoeusOptionPane.showInfoDialog("Submission Details not available for this protocol") ;
                } else {
                    /* Case 646  - prahalad Mar 12 2004
                    added 'M' as a paramter instead of 'E'
                     */
                    edu.mit.coeus.iacuc.gui.SubmissionDetailsForm frmSubmissionDetailsForm =
                            new SubmissionDetailsForm(mdiForm, "Submission details for Protocol " + protocolNumber, protocolNumber, true, vDataObjects, vecReviewers,  'M', subNo ) ; // prps added subNo-1 instead of -1 - jan 09 2004
                    frmSubmissionDetailsForm.requestDefaultFocusForComponent();
                    //setting up the values for the table protocolactions - Added by Jobin - start
                    //	Vector commActionData = (Vector) vecDetails.get(1);
                    //	frmSubmissionDetailsForm.commAction(commActionData); // Added by Jobin - end
                    frmSubmissionDetailsForm.showForm() ;
                    ProtocolSubmissionInfoBean submissionInfoBean =
                            frmSubmissionDetailsForm.getSavedData();
                    // Added for COEUSQA-3025 : Enhanced functionality to address the IACUC designated member review determination process - Start
                    Vector vecReviewDetermination = frmSubmissionDetailsForm.getReviewDeterminations();
                    if(vecReviewDetermination != null && !vecReviewDetermination.isEmpty()){
                        for(Object determinationDetails : vecReviewDetermination){
                            ProtocolReviewDeterminationBean reviewDeterminationBean = (ProtocolReviewDeterminationBean)determinationDetails;
                            submissionInfoBean.setDeterminationDueDate(reviewDeterminationBean.getDeterminationDueDate());
                        }
                    }
                    // Added for COEUSQA-3025 : Enhanced functionality to address the IACUC designated member review determination process - End
                    if( submissionInfoBean != null ) {
                        updateData( submissionInfoBean );
                    }
                    // Code commented for COEUSQA-2105: No notification for some IRB actions
//                    // 3283: Reviewer Notification Changes:Start
//                    //Notify all reviewers if the reviewer on max submission number is changed.
//                    if(seqNo!=0 && frmSubmissionDetailsForm.isReviewerPersonsChanged()){
//                        //Check if the reviewer change is on maxSubmissionNo
//                        int maxsubmission = 0;
//                        for(int i = 0 ;i<vecDetails.size();i++){
//                            ProtocolSubmissionInfoBean protocolSubmissionBean = (ProtocolSubmissionInfoBean)vecDetails.get(i);
//                            if(protocolSubmissionBean.getSequenceNumber()==seqNo
//                                    && protocolSubmissionBean.getSubmissionNumber()>maxsubmission){
//                                maxsubmission    = protocolSubmissionBean.getSubmissionNumber();
//                            }
//                        }
//                        if(detailsBean.getSubmissionNumber().intValue() == maxsubmission){
//                            ProtocolMailController mailController = new ProtocolMailController(true);
//                            synchronized(mailController) {
//                                mailController.sendMail(ACTION_REVIEWER_CHANGE, protocolNumber, seqNo);
//                                //351 - Change in Reviewer
//                            }
//                        }
//                    }
//                    // 3283 - End
                }
                
            } else if (actSource.equals(btnCloseSubmission)) {
                closeSubmissionBaseWindow();
            }else if (actSource.equals(submissionSearch) ||
                    actSource.equals(btnSearchSubmission)) {
                showSubmissionSearchWindow();
            }else if(actSource.equals(btnSaveAsSubmission)) { ////Added by sharath - 19/10/2003 for Save As ToolBar Button - Start
                //showSaveAsDialog();
                //new implementation
                saveAsActiveSheet();
            }else if( actSource.equals( notifyCommittee ) ){
                //Added a try catch block since the execption was supressed - Ajay
                try{
                   //If condition added for internal issue 1753 start
                    if(!performAssignToSchedule(actSource)){
                        return;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }if(actSource.equals(protocolReviewComments)) {
                reviewComments();
            }

            else if (actSource.equals(protocolResponse ) ||
                    actSource.equals(designatedApprove) ||
                    actSource.equals(hold) ||
                    actSource.equals(withdrawSubmission) ||
                    actSource.equals(liftHold) ||
                    actSource.equals(minorRevisionRequired) ||
                    actSource.equals(majorRevisionRequired) ||
                    actSource.equals(disapprove) ||
                    actSource.equals(notifyCommittee)||
                    actSource.equals(returnToPI) ||
                    actSource.equals(deactivate) ||
                    actSource.equals(expire) ||
                    actSource.equals(iacucAcknow) ||
                    actSource.equals(iacucReviewNotReq) ||
                    actSource.equals(terminate) ||
                    actSource.equals(suspend) ||
                    // Added for COEUSQA-2666: Complete Administrative Review functionality in IACUC - Start
                    actSource.equals(administrativeApprove) ||
                    actSource.equals(adminstrativeIncomplete) 
                    // COEUSQA-2666: End
                    ){
                ProtocolActionsBean actionBean = new ProtocolActionsBean();                 
                actionBean.setProtocolNumber(protocolNumber);
                actionBean.setSequenceNumber(seqNo);
                actionBean.setSubmissionNumber(subNo);                
                actionBean.setCommitteeId(commId);
                actionBean.setScheduleId(schId);
                actionBean.setActionTypeDescription(((CoeusMenuItem)actSource).getText());
                actionBean.setActionTypeCode(Integer.parseInt(actionType.getActionCommand()));
                processAction(actionBean) ; 
            }
            //Added by nadh - 19/01/2005 for Sort ToolBar Button - Start
            else if (actSource.equals(btnSort)) {
                showSort();
            }//Added by Nadh - End
            // prps start jan 14 2003
            else if (actSource.equals(printAdhoc)) {
                showAdhocReports() ;
            } 
            //prps end jan 14 2003
            
            //Added by sharath - 19/10/2003 for Save As ToolBar Button - End
            //Added by sharath  - 20-Oct-2003 for Review comments from Submission List widow - Start
            
            //Added by sharath  - 20-Oct-2003 for Review comments from Submission List widow - End
            //Added by Nadh for CoeusSearch enhancement(CustomQuery)   start : 3-aug-2005
            if (actSource instanceof CoeusToolBarButton && searchButtons != null) {
                String code = null;
                for (int i=0;i<searchButtons.length;i++) {
                    if(actSource.equals(searchButtons[i])){
                        code =  searchButtons[i].getActionCommand();
                        coeusSearch.getSearchQueryResult(code);
                        buildSearchResultsTable(coeusSearch.getSearchResTable());
                    }
                }
                
            }//End : 3-aug-2005
        }catch(CoeusException ex){
            //Printing exception since the execption was supressed - Ajay
            ex.printStackTrace();
            CoeusOptionPane.showDialog(new CoeusClientException(ex));
        }catch( Exception err ){
            CoeusOptionPane.showInfoDialog( err.getMessage() );
        }
    }
    
    // added by Nadh to implement sorting proposals start - 19-01-2004
    /*
     * this method shows the sort window
     * return void
     */
    private void showSort() {
        if(vecSortedData==null) {
            vecSortedData = new Vector();
        }
        SortForm sortForm = new SortForm(tblProtocol,vecSortedData);
        Vector sortedData = sortForm.display();
        vecSortedData = (Vector)sortedData.get(1);
        if(((Integer)sortedData.get(0)).intValue() == OK_CLICKED)
            coeusSearch.sortByColumns(tblProtocol,vecSortedData);
        else
            return;
    }// Added by Nadh - end
    
    //prps start jan 14 2003
    public void showAdhocReports() {
        try {
            AdhocDetailsBean adhocDetailsBean = new AdhocDetailsBean() ;
            if (tblProtocol != null && tblProtocol.getRowCount() > 0) {
                int selectedRow = tblProtocol.getSelectedRow();
                //Check whether row is selected
                if (selectedRow >= 0) {
                    String protocolNumber = getSelectedValue(selectedRow, PROTOCOL_NUMBER);
                    int sequenceNumber = Integer.parseInt(getSelectedValue(selectedRow, SEQUENCE_NUMBER));
                    int submissionNumber  = Integer.parseInt(getSelectedValue(selectedRow, SUBMISSION_NUMBER));
                    String scheduleId = getSelectedValue(selectedRow,SCHEDULE_ID);
                    //Added for the case COEUSDEV-220-Generate Correspondence
                    String committeeId = getSelectedValue(selectedRow,COMMITTEE_ID);
                    adhocDetailsBean.setCommitteeId(committeeId);
                    //Added for the case COEUSDEV-220-Generate Correspondence-end
                    adhocDetailsBean.setProtocolNumber(protocolNumber) ;
                    adhocDetailsBean.setSequenceNumber(sequenceNumber) ;
                    adhocDetailsBean.setScheduleId(scheduleId) ;
                    adhocDetailsBean.setSubmissionNumber(submissionNumber) ;
                    adhocDetailsBean.setModule('U') ;
                    AdhocReportsForm adhocReportsForm = new AdhocReportsForm(adhocDetailsBean) ;
                    adhocReportsForm.showForm() ;
                }
            }
        } catch(Exception ex) {
            CoeusOptionPane.showErrorDialog(ex.getMessage()) ;
        }
    }
    //prps end jan 14 2004
    
    
    
    //prps start dec 12 2003
    // This method will take care of processing actions having many followup actions
    // or actions having recursive followup actions
    // since updateActionStatus method needs to be called for every action. This
    // method cannot be a part of ProcessAction Object
    private void processAction(ProtocolActionsBean actionBean) throws Exception {
        ProtocolActionChangesBean protocolActionChangesBean ;
        actionBean.setActionTriggeredFrom(CoeusGuiConstants.IACUC_SUB_LIST_WINDOW);
        protocolActionChangesBean =  processAction.performOtherAction( actionBean ) ;
        if (protocolActionChangesBean != null) {
            updateActionStatus(protocolActionChangesBean);
            // update the actionBean
            actionBean.setProtocolNumber(protocolActionChangesBean.getProtocolNumber()) ;
            actionBean.setScheduleId(protocolActionChangesBean.getScheduleId()) ;
            actionBean.setSequenceNumber(protocolActionChangesBean.getSequenceNumber()) ;
            actionBean.setSubmissionNumber(protocolActionChangesBean.getSubmissionNumber()) ;
            actionBean.setCommitteeId(protocolActionChangesBean.getCommitteeId()) ;
            
            Vector vecActionDetails = protocolActionChangesBean.getFollowupAction() ;
            if (vecActionDetails.size()>0) {
                Vector vecSubActions = (Vector)vecActionDetails.get(0) ;
                HashMap hashUserPrompt = (HashMap)vecActionDetails.get(1) ;
                HashMap hashUserPromptFlag = (HashMap)vecActionDetails.get(2) ;
                
                if (vecSubActions.size() > 0) // perform sub actions or followup actions.
                {  // for an action there cud be multiple followup actions
                    // or one followup action action might have another sub action (recursively)
                    for (int actionCount = 0; actionCount < vecSubActions.size(); actionCount++) {
                        //Added For ISSUEID#1790 - Message display is incorrect-Follow-up Action  - start
                        //int actionTypeCode = ((Integer)vecSubActions.get(actionCount)).intValue() ;
                        ProtocolActionsBean followActionBean = (ProtocolActionsBean)vecSubActions.get(actionCount);
                        int actionTypeCode = followActionBean.getActionTypeCode();
                        String actionTypeDescription = followActionBean.getActionTypeDescription();
                        //Added For ISSUEID#1790 - Message display is incorrect-Follow-up Action  - end
                        if (hashUserPromptFlag.get(new Integer(actionTypeCode)).toString().equalsIgnoreCase("Y") ) {
                            String promptMessage = hashUserPrompt.get(new Integer(actionTypeCode)).toString() ;
                            if (CoeusOptionPane.SELECTION_YES == CoeusOptionPane.showQuestionDialog(promptMessage,
                                    CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES)) { // prompt the user with msg obtained from database and if the choice is Yes continue follow up action
                                actionBean.setActionTypeCode(actionTypeCode) ;
                                //Added For ISSUEID#1790 - Message display is incorrect-Follow-up Action  - start
                                actionBean.setActionTypeDescription(actionTypeDescription);
                               //Added For ISSUEID#1790 - Message display is incorrect-Follow-up Action  - end
                                processAction(actionBean) ;
                            }
                        } else { // if the prompt is flag is set to N then user will not be prompted for the followup action
                            actionBean.setActionTypeCode(actionTypeCode) ;
                            //Added For ISSUEID#1790 - Message display is incorrect-Follow-up Action  - start
                            actionBean.setActionTypeDescription(actionTypeDescription);
                            //Added For ISSUEID#1790 - Message display is incorrect-Follow-up Action  - end
                            processAction(actionBean) ;
                        }
                    }//end for
                    
                }// end if vec sub actions
            }// end if vecActionDetails
        }
//        else
//        {
//            CoeusOptionPane.showErrorDialog("Action "  + actionBean.getActionTypeDescription() + " failed") ;
//        }
        
    }
    
    
    private final String PROTOCOL_SERVLET = "/IacucProtocolServlet";
    
//prps start dec 12 2003
    
    
    /**
     * This method is used to check whether the given protocol number is already
     * opened in the given mode or not.
     */
    private void checkDuplicateAndShow(String moduleName, String refId, char mode)throws Exception {
        boolean duplicate = false;
        try{
            refId = refId == null ? "" : refId;
            
            // Case# 3549: Wrong screen appearing for Renewal in IRB Protocol Premium
            // If the Selected Protocol is an Amendment or Renewal, set the Module Name
            // to Protocol Amendments or Protocol Renewal
            if(refId.length() > 10){                 
                moduleName = setModuleName(refId, moduleName);                                             
            } else{
                // Normal Protocol
                moduleName = CoeusGuiConstants.IACUC_PROTOCOL_FRAME_TITLE;
            }
            // Case# 3549: Wrong screen appearing for Renewal in IRB Protocol Premium
            
            duplicate = mdiForm.checkDuplicate(moduleName, refId, mode );
            
        }catch(Exception e){
            /* Exception occured.  Record may be already opened in requested mode
               or if the requested mode is edit mode and application is already
               editing any other record. */
            duplicate = true;
            if(e.getMessage().length() > 0 ) {
                CoeusOptionPane.showInfoDialog(e.getMessage());
            }
            /* try to get the requested frame which is already opened */
            CoeusInternalFrame frame = mdiForm.getFrame(moduleName,refId);
            if(frame == null){
                /* if no frame opened for the requested record then the
                   requested mode is edit mode. So get the frame of the
                   editing record. */
                frame = mdiForm.getEditingFrame( moduleName );
            }
            if (frame != null){
                frame.setSelected(true);
                frame.setVisible(true);
            }
            return;
        }
        try{            
            setProtocolWindow(mode, moduleName, refId);
            // Case# 3549: Wrong screen appearing for Renewal in IRB Protocol Premium
            
            mainProtocol.showDialogForm();
            
            int selRow = tblProtocol.getSelectedRow();
            if( mode != DISPLAY_FUNCTION ) {
                mainProtocol.registerObserver( this );
                if( selRow != -1  ){
                    baseTableRow = Integer.parseInt((String)tblProtocol.getValueAt(
                            selRow,tblProtocol.getColumnCount()-1));
                }
            }
            
        }catch ( Exception ex) {
            //ex.printStackTrace();
            try{
                if (!mainProtocol.isModifiable() ) {
                    String msg = coeusMessageResources.parseMessageKey(
                            ex.getMessage());
                    int resultConfirm = CoeusOptionPane.showQuestionDialog(msg,
                            CoeusOptionPane.OPTION_YES_NO,
                            CoeusOptionPane.DEFAULT_YES);
                    if (resultConfirm == 0) {
                        showProtocolDetails(refId);
                    }
                }else {
                    throw new Exception(ex.getMessage());
                }
            }catch (Exception excep){
                excep.printStackTrace();
                throw new Exception(excep.getMessage());
            }
        }
    }
           
    /**
     * This method is invoked when the user clicks modify in the Edit menu
     * of Protocol module
     */
    private void modifyProtocolDetails(String protocolNumber) throws Exception{
        if (protocolNumber == null) {
            log(coeusMessageResources.parseMessageKey(
                    "protoBaseWin_exceptionCode.1051"));
        } else { // this check is to make sure if it is ok for the user to open a protocol for editing
            if(isStatusOkForEditing(protocolNumber)) //prps added this line
            {    //prps added this line
                checkDuplicateAndShow(CoeusGuiConstants.IACUC_PROTOCOL_FRAME_TITLE,
                        protocolNumber,MODIFY_FUNCTION);
            } //prps added this line
        }
    }
    
    
    
    // prps added this new function
    private boolean isStatusOkForEditing(String protocolNumber) {
        boolean statusOk = false ;
        String connectTo = CoeusGuiConstants.CONNECTION_URL+ PROTOCOL_SERVLET;
        // connect to the database and get the formData for the given organization id
        RequesterBean request = new RequesterBean();
        
        request.setFunctionType(CHECK_IF_EDITABLE);
        request.setId(protocolNumber);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        statusOk = response.isSuccessfulResponse() ;
       
        if (statusOk == true) {
            // continue with row lock check
            statusOk = true ;
            
        } else {
            if(response.getDataObject() != null){
                Object obj = response.getDataObject();
                if(obj instanceof CoeusException){
                    CoeusOptionPane.showDialog(new CoeusClientException((CoeusException)obj));
                }
            }else{
                CoeusOptionPane.showErrorDialog(response.getMessage()) ;
            }
            statusOk = false ;
        }
        return  statusOk ;
    }
    
    /**
     * This method is invoked when the user clicks display in the Edit menu
     * of Protocol module
     */
    private void showProtocolDetails(String protocolNumber) {
        if (protocolNumber == null) {
            log(coeusMessageResources.parseMessageKey(
                    "protoBaseWin_exceptionCode.1052"));
        } else {
            try{
                checkDuplicateAndShow(CoeusGuiConstants.IACUC_PROTOCOL_FRAME_TITLE,protocolNumber,DISPLAY_FUNCTION);
            }catch( Exception err ){
                //err.printStackTrace();
                CoeusOptionPane.showInfoDialog(err.getMessage());
            }
        }
    }
    
    /**
     * This method is invoked when the user clicks close in the ToolBar
     * of Protocol module
     */
    private void closeSubmissionBaseWindow() {
        this.doDefaultCloseAction();
    }
    
    /**
     * display alert message
     *
     * @param mesg the message to be displayed
     */
    private void log(String mesg) {
        CoeusOptionPane.showErrorDialog(mesg);
    }
    
    /** This method is called from Save Menu Item under File Menu.
     * Implemented the abstract class declared in parent(<CODE>CoeusInternalFrame</CODE>).
     * Empty body since the Save operation is not required for this list screen.
     */
    public void saveActiveSheet() {
    }
    
    // this method will get the submission details data from the servlet
    //private SubmissionDetailsBean getSubmissionDetails(SubmissionDetailsBean detailsBean) {
    private Vector getSubmissionDetails(SubmissionDetailsBean detailsBean) { //Added by sharath
        Vector vecDetails= new Vector() ;
        try {    // send request
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType('X') ;
            requester.setDataObject(detailsBean) ;
            
            String connectTo =CoeusGuiConstants.CONNECTION_URL
                    + SUBMISSION_DETAILS_SERVLET ;
            AppletServletCommunicator comm
                    = new AppletServletCommunicator(connectTo,requester);
            comm.send();
            
            ResponderBean responder = comm.getResponse();
            if (responder.isSuccessfulResponse()) {
                //detailsBean = (SubmissionDetailsBean)responder.getDataObject() ;
                vDataObjects = (Vector)responder.getDataObject();//Added by sharath
                vecDetails = (Vector)vDataObjects.get(0);//Added by sharath
                //				vecActions = VDataObjects.get(1);
            }
            
        } catch(Exception ex) {
            
            ex.printStackTrace() ;
        }
        
        //return detailsBean ;
        return vecDetails;//Added by sharath
    }
    
    public void update(java.util.Observable observable, Object obj) {
        SearchColumnIndex searchColumnIndex = new SearchColumnIndex();
        if( obj instanceof ProtocolInfoBean ) {
            ProtocolInfoBean protocolBean =  (ProtocolInfoBean) obj;
            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                    protocolBean.getProtocolNumber(),baseTableRow,searchColumnIndex.getSearchColumnIndex(coeusSearch,PROTOCOL_NUMBER));
            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                    protocolBean.getTitle(),baseTableRow,searchColumnIndex.getSearchColumnIndex(coeusSearch,"TITLE"));
            
            //get the PI name
            getPIDetails(protocolBean.getInvestigators());
            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                    PIName,baseTableRow,searchColumnIndex.getSearchColumnIndex(coeusSearch,"PERSON_NAME"));
            int selRow = tblProtocol.getSelectedRow();
            if(  selRow != -1 ) {
                baseTableRow = Integer.parseInt((String)tblProtocol.getValueAt(
                        selRow,tblProtocol.getColumnCount()-1));
            }
        }
        
    }
    
    /** This method is used to get the Primary Investigator details from the
     * collection of Investigators of this Protocol, which will be used to
     * populate the details in the <CODE>ProtocolBaseWindow</CODE>.
     *
     * @param investigators Collection of <CODE>ProtocolInvestigatorsBean</CODE>s of a
     * Protocol.
     */
    public void getPIDetails(Vector investigators ){
        PIName= "";
        int count = investigators.size();
        
        for (int i=0; i<count;i++) {
            ProtocolInvestigatorsBean investigatorBean
                    = (ProtocolInvestigatorsBean) investigators.elementAt(i);
            if (investigatorBean.isPrincipalInvestigatorFlag()) {
                PIName = investigatorBean.getPersonName();
                break;
            }
        }
    }
    //prps end
    
    //Added by sharath - 19/10/2003 for Save As ToolBar Button - Start
    /**
     * This method is called from SaveAs Toolbar Menu Item.
     */
    public void showSaveAsDialog(){
        SaveAsDialog saveAsDialog = new SaveAsDialog(tblProtocol);
    }
    //Added by sharath - 19/10/2003 for Save As ToolBar Button - End
    
    //Added by sharath  - 20-Oct-2003 for Review comments from Submission List widow - Start
    private void reviewComments() throws Exception {
        
        if(reviewCommentsForm == null){
            reviewCommentsForm = new ReviewCommentsForm(true);
        }
        int selectedRow = tblProtocol.getSelectedRow();
        if(selectedRow == -1){
            return ;
        }
//       String protocolNumber = tblProtocol.getValueAt(selectedRow , PROTOCOL_NUMBER_COLUMN).toString();
//       int submissionNumber = Integer.parseInt(tblProtocol.getValueAt(selectedRow, SUBMISSION_NUMBER_COLUMN).toString());
//       int sequenceNumber = Integer.parseInt(tblProtocol.getValueAt(selectedRow, SEQUENCE_NUMBER_COLUMN).toString());
        String protocolNumber = getSelectedValue(selectedRow , PROTOCOL_NUMBER);
        int submissionNumber = Integer.parseInt(getSelectedValue(selectedRow,SUBMISSION_NUMBER));
        int sequenceNumber = Integer.parseInt(getSelectedValue(selectedRow,SEQUENCE_NUMBER));
        
        RequesterBean requesterBean = new RequesterBean();
        ProtocolSubmissionInfoBean protocolSubmissionInfoBean = new ProtocolSubmissionInfoBean();
        protocolSubmissionInfoBean.setProtocolNumber(protocolNumber);
        protocolSubmissionInfoBean.setSequenceNumber(sequenceNumber);
        protocolSubmissionInfoBean.setSubmissionNumber(submissionNumber);
        requesterBean.setDataObject(protocolSubmissionInfoBean);
        requesterBean.setFunctionType('t');
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SCHEDULE_MAINTENANCE_SERVLET, requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(! responderBean.isSuccessfulResponse()){
            reviewCommentsForm.setFunctionType(TypeConstants.DISPLAY_MODE);
        }else {
            reviewCommentsForm.setFunctionType(TypeConstants.MODIFY_MODE);
        }
        
        //reviewCommentsForm.setFormData("0310002101",2);
        reviewCommentsForm.setFormData(protocolNumber, submissionNumber, sequenceNumber);
        reviewCommentsForm.display();
        
    }
    //Added by sharath  - 20-Oct-2003 for Review comments from Submission List widow - End
    
    //added by ravi - 21-oct - START
    private void updateSearchResults() throws Exception{
        int rowCount = tblProtocol.getRowCount();
        if( rowCount > 0 ) {
            searchResults.clear();
            String protocolId = null, submissionNumber = null,indexValue = null;
            for(int indx = 0 ; indx < rowCount ; indx++ ) {
//                protocolId = (String)tblProtocol.getValueAt(indx,PROTOCOL_NUMBER_COLUMN);
                protocolId = getSelectedValue(indx, PROTOCOL_NUMBER);
                if( protocolId.length() > 10 ) {
                    protocolId = protocolId.substring(0,10);
                }
//                submissionNumber = (String)tblProtocol.getValueAt(indx,SUBMISSION_NUMBER_COLUMN);
                submissionNumber = getSelectedValue(indx, SUBMISSION_NUMBER);
                indexValue = (String)tblProtocol.getValueAt(indx,INDEX_COLUMN);
                searchResults.put(protocolId+submissionNumber, indexValue );
            }
        }
    }
    
    private void updateData(ProtocolSubmissionInfoBean submissionInfoBean ){
        String protocolId = submissionInfoBean.getProtocolNumber();
        SearchColumnIndex searchColumnIndex = new SearchColumnIndex();
        if( protocolId.length() > 10 ) {
            protocolId = protocolId.substring(0,10);
        }    
        String submissionNumber = ""+submissionInfoBean.getSubmissionNumber();
        DateUtils dtUtils = new DateUtils();
        String dateValue = "";
        if( searchResults.containsKey(protocolId+submissionNumber) ) {
//            int updateRow = Integer.parseInt( (String)searchResults.get(protocolId+submissionNumber))-1;
            int updateRow = tblProtocol.getSelectedRow();
            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                    submissionInfoBean.getProtocolNumber(), updateRow,
                    searchColumnIndex.getSearchColumnIndex(coeusSearch,PROTOCOL_NUMBER));
            // Added for COEUSQA-3025 : Enhanced functionality to address the IACUC designated member review determination process - Start
            if(submissionInfoBean.getDeterminationDueDate() != null){
                String dueDateFormat = CoeusServerProperties.getProperty(CoeusPropertyKeys.SEARCH_DATE_FORMAT, SIMPLE_DATE_FORMAT);
                //COEUSQA-1477 Dates in Search Results - Start
                dateValue = dtUtils.parseDateForSearchResults(submissionInfoBean.getDeterminationDueDate().toString(), dueDateFormat);
                //dateValue = dtUtils.formatDate(submissionInfoBean.getDeterminationDueDate().toString(), dueDateFormat);
                //COEUSQA-1477 Dates in Search Results - End
            }else{
                dateValue = "";
            }
            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(dateValue, updateRow,
                    searchColumnIndex.getSearchColumnIndex(coeusSearch,"DETERMINATION_DUE_DATE"));
            // Added for COEUSQA-3025 : Enhanced functionality to address the IACUC designated member review determination process - End
            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                    submissionInfoBean.getSubmissionStatusDesc(), updateRow,
                    searchColumnIndex.getSearchColumnIndex(coeusSearch,"SUBMISSION_STATUS_DESCRIPTION"));
            if(submissionInfoBean.getSubmissionDate() != null){
                String dateFormat = CoeusServerProperties.getProperty(CoeusPropertyKeys.SEARCH_DATE_FORMAT, SIMPLE_DATE_FORMAT);
                //COEUSQA-1477 Dates in Search Results - Start
                dateValue = dtUtils.parseDateForSearchResults(submissionInfoBean.getSubmissionDate().toString(), dateFormat);
                //dateValue = dtUtils.formatDate(submissionInfoBean.getSubmissionDate().toString(), dateFormat);
                //COEUSQA-1477 Dates in Search Results - End
            }else{
                dateValue = "";
            }
            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                    dateValue, updateRow, searchColumnIndex.getSearchColumnIndex(coeusSearch,"SUBMISSION_DATE"));
            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                    submissionInfoBean.getSubmissionTypeDesc(), updateRow,
                    searchColumnIndex.getSearchColumnIndex(coeusSearch,"SUBMISSION_TYPE_DESCRIPTION"));
            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                    submissionInfoBean.getSubmissionQualTypeDesc(), updateRow,
                    searchColumnIndex.getSearchColumnIndex(coeusSearch,"QUALIFIER_DESCRIPTION"));
            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                    submissionInfoBean.getProtocolReviewTypeDesc(), updateRow,
                    searchColumnIndex.getSearchColumnIndex(coeusSearch,"REVIEW_DESCRIPTION"));
            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                    submissionInfoBean.getCommitteeId(), updateRow,
                    searchColumnIndex.getSearchColumnIndex(coeusSearch,COMMITTEE_ID));
            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                    submissionInfoBean.getScheduleId(), updateRow,
                    searchColumnIndex.getSearchColumnIndex(coeusSearch,SCHEDULE_ID));
            if(submissionInfoBean.getScheduleDate() != null){                
                //Modified for PT ID#3243 - date format
                String dateFormat = CoeusServerProperties.getProperty(CoeusPropertyKeys.SEARCH_DATE_FORMAT, SIMPLE_DATE_FORMAT);
                //COEUSQA-1477 Dates in Search Results - Start
                dateValue = dtUtils.parseDateForSearchResults(submissionInfoBean.getScheduleDate().toString(), dateFormat);
                //dateValue = dtUtils.formatDate(
                //        submissionInfoBean.getScheduleDate().toString(), dateFormat);
                //COEUSQA-1477 Dates in Search Results - End
            }else{
                dateValue = "";
            }
            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                    dateValue, updateRow,
                    searchColumnIndex.getSearchColumnIndex(coeusSearch,"SCHEDULED_DATE"));
            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                    submissionNumber, updateRow,
                    searchColumnIndex.getSearchColumnIndex(coeusSearch,SUBMISSION_NUMBER));
            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                    ""+submissionInfoBean.getSequenceNumber(), updateRow,
                    searchColumnIndex.getSearchColumnIndex(coeusSearch,SEQUENCE_NUMBER));
            
        }
    }
//    private void updateData(ProtocolActionsBean actionBean ){
//        String protocolId = actionBean.getProtocolNumber();
//        if( protocolId.length() > 10 ) {
//            protocolId = protocolId.substring(0,10);
//        }
//        String submissionNumber = ""+actionBean.getSubmissionNumber();
//        DateUtils dtUtils = new DateUtils();
//        String dateValue = "";
//        if( searchResults.containsKey(protocolId+submissionNumber) ) {
//            int updateRow = Integer.parseInt( (String)searchResults.get(protocolId+submissionNumber));
//            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
//                actionBean.getProtocolNumber(), updateRow, PROTOCOL_NUMBER_COLUMN);
//            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
//                actionBean.getCommitteeId(), updateRow, 6);
//            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
//                submissionNumber, updateRow, SUBMISSION_NUMBER_COLUMN);
//            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
//                ""+actionBean.getSequenceNumber(), updateRow, SEQUENCE_NUMBER_COLUMN);
//
//        }
//    }
    
//    private void performAssignToSchedule() throws Exception {
//        AssignScheduleForm assignSchedule = new AssignScheduleForm();
//        if( assignSchedule.isScheduleSelected() ) {
//            HashMap selectedSubmissions = new HashMap();
//            int[] selIndices = tblProtocol.getSelectedRows();
//            ProtocolActionsBean actionBean = null;
//            for( int indx = 0 ; indx < selIndices.length; indx++ ) {
//                int selRow = selIndices[ indx ];
//                String protocolNumber = (String)tblProtocol.getValueAt(selRow,PROTOCOL_NUMBER_COLUMN);
//                int seqNo = Integer.parseInt(
//                    (String)tblProtocol.getValueAt(selRow,SEQUENCE_NUMBER_COLUMN));
//                int subNo = Integer.parseInt(
//                    (String)tblProtocol.getValueAt(selRow,SUBMISSION_NUMBER_COLUMN));
//                ProtocolSubmissionInfoBean submissionBean = new ProtocolSubmissionInfoBean();
//                submissionBean.setProtocolNumber( protocolNumber );
//                submissionBean.setSequenceNumber( seqNo );
//                submissionBean.setSubmissionNumber( subNo );
//                selectedSubmissions.put( protocolNumber + subNo, submissionBean );
//            }
//            selectedSubmissions = getValidSubmissionsForAssignment( selectedSubmissions ) ;
//            Iterator iterator = selectedSubmissions.values().iterator();
//            Vector submissionsToDB = new Vector();
//            String committeeId = assignSchedule.getSelectedCommitteeId();
//            String scheduleId = assignSchedule.getSelectedScheduleId();
//            while( iterator.hasNext() ) {
//                Object value = iterator.next();
//                if( value instanceof ProtocolSubmissionInfoBean ) {
//                    boolean sendToServer = true;
//                    ProtocolSubmissionInfoBean validBean = (ProtocolSubmissionInfoBean)value;
//                    if( validBean.getScheduleId() != null &&
//                        validBean.getScheduleId().trim().length() > 0 ) {
//                         sendToServer = false;
//                         if( !scheduleId.equalsIgnoreCase( validBean.getScheduleId() ) ) {
//                             int opt = CoeusOptionPane.DEFAULT_NO;
//                             opt = CoeusOptionPane.showQuestionDialog(
//                                validBean.getProtocolNumber()+" already Assinged to Schedule : "+
//                                validBean.getScheduleId()+"\n Do you want to assign it to new Schedule ?",
//                                CoeusOptionPane.OPTION_YES_NO, opt);
//                             if( opt == CoeusOptionPane.SELECTION_YES ) {
//                                 sendToServer = true;
//                             }
//                         }
//                    }else{
//                        sendToServer = true;
//                    }
//                    if( sendToServer ) {
//                        actionBean = new ProtocolActionsBean();
//                        actionBean.setProtocolNumber(validBean.getProtocolNumber());
//                        actionBean.setSequenceNumber(validBean.getSequenceNumber());
//                        actionBean.setSubmissionNumber(validBean.getSubmissionNumber());
//                        actionBean.setActionTypeCode(NOTIFY_COMMITTEE);
//                        actionBean.setCommitteeId( committeeId );
//                        actionBean.setScheduleId( scheduleId );
//                        submissionsToDB.addElement( actionBean );
//                    }
//                }else if( value instanceof String ) {
//                    CoeusOptionPane.showErrorDialog(value.toString());
//                }
//            }
//            if( submissionsToDB.size() > 0 ) {
//                saveAssignedSubmissions( submissionsToDB );
//            }
//        }
//    }
    
    // This method is modified for enhancements to Refine Notify Committee action. - by Nadh
    //start 3 aug 2004
       private boolean performAssignToSchedule( Object actSource ) throws Exception {
        int[] selIndices = tblProtocol.getSelectedRows();
        int selRow;
        if(selIndices.length==1){
            selRow = tblProtocol.getSelectedRow();
            String committeeId = getSelectedValue(selRow,COMMITTEE_ID);
            String scheduleId = getSelectedValue(selRow,SCHEDULE_ID);
            //Bug Fix:1545 Checking for null in if conditon.
            //commented for common method canPerFormNotifyCommittee
            //if( scheduleId.trim().length() != 0 ) {
//            if( scheduleId != null && scheduleId.trim().length() != 0 ) {
//                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("iacucProtoSubmission_exceptionCode.1002"));
//                return false;
//            }

            int subNo = Integer.parseInt(getSelectedValue(selRow, SUBMISSION_NUMBER));
            String protocolNumber = getSelectedValue(selRow, PROTOCOL_NUMBER);
            int seqNo = Integer.parseInt(getSelectedValue(selRow,SEQUENCE_NUMBER));
            SubmissionDetailsBean detailsBean = new SubmissionDetailsBean() ;
            detailsBean.setProtocolNumber(protocolNumber) ;
            detailsBean.setScheduleId(null) ; //scheduleId = null from here
            detailsBean.setSequenceNumber(new Integer(seqNo)) ; // seq num is also null
            detailsBean.setSubmissionNumber(new Integer(subNo)) ;
            //vDetails = getSubmissionDetails(detailsBean) ;
            // inside of the method canPerformNotifyCommittee we ser calling this method
            //Added for internal issue #1753 start
            if(!canPerformNotifyCommittee(committeeId, scheduleId,detailsBean)){
                return false;
            }
            //Added for internal issue #1753  end
            //ProtocolSubmissionInfoBean protocolSubmissionInfoBean = (ProtocolSubmissionInfoBean)vecDetails.get();
            String committeeName = getCurrentRecord(subNo);
            Vector vData = new Vector();
            vData.addElement(committeeId);
            vData.addElement(committeeName);
            AssignScheduleForm assignSchedule = 
                    new AssignScheduleForm(vData, "Notify Committee", 
                    CoeusGuiConstants.EMPTY_STRING, IacucProtocolActionsConstants.NOTIFY_COMMITTEE);
            if(!assignSchedule.isScheduleSelected() ){
                return true;
            }
            ProtocolActionsBean actionBean = new ProtocolActionsBean();
            actionBean.setProtocolNumber(protocolNumber);
            actionBean.setSequenceNumber(seqNo);
            actionBean.setSubmissionNumber(subNo);
            actionBean.setActionTypeCode(IacucProtocolActionsConstants.NOTIFY_COMMITTEE);
            actionBean.setActionTypeDescription( ((CoeusMenuItem)actSource).getText() );
            actionBean.setCommitteeId( assignSchedule.getSelectedCommitteeId() );
            actionBean.setScheduleId( assignSchedule.getSelectedScheduleId() );
            // actionBean.setActionTypeDescription(((CoeusMenuItem)actSource).getText()); //prps commented this dec 12 2002
            saveAssignedSubmissions( actionBean );
        }else{
            AssignScheduleForm assignSchedule = new AssignScheduleForm("Notify Committee", CoeusGuiConstants.EMPTY_STRING, IacucProtocolActionsConstants.NOTIFY_COMMITTEE);
            if( assignSchedule.isScheduleSelected() ) {
//                HashMap selectedSubmissions = new HashMap();
                ProtocolActionsBean actionBean = null;
                String committeeId = assignSchedule.getSelectedCommitteeId();
                String scheduleId = assignSchedule.getSelectedScheduleId();
                if ( tblProtocol.getModel() instanceof TableSorter ) {
                    ((TableSorter)tblProtocol.getModel()).setSortingRequired(false);
                }
                for( int indx = 0 ; indx < selIndices.length; indx++ ) {
                    selRow = selIndices[ indx ];
                    String protocolNumber = getSelectedValue(selRow, PROTOCOL_NUMBER);
                    int seqNo = Integer.parseInt(getSelectedValue(selRow,SEQUENCE_NUMBER));
                    int subNo = Integer.parseInt(getSelectedValue(selRow,SUBMISSION_NUMBER ));
                    actionBean = new ProtocolActionsBean();
                    actionBean.setProtocolNumber(protocolNumber);
                    actionBean.setSequenceNumber(seqNo);
                    actionBean.setSubmissionNumber(subNo);
                    actionBean.setActionTypeCode(IacucProtocolActionsConstants.NOTIFY_COMMITTEE);
                    actionBean.setActionTypeDescription( ((CoeusMenuItem)actSource).getText() );
                    actionBean.setCommitteeId( committeeId );
                    actionBean.setScheduleId( scheduleId );
                    // actionBean.setActionTypeDescription(((CoeusMenuItem)actSource).getText()); //prps commented this dec 12 2002
                    saveAssignedSubmissions( actionBean );
                }
                if ( tblProtocol.getModel() instanceof TableSorter ) {
                    ((TableSorter)tblProtocol.getModel()).setSortingRequired(true);
                }
            }
        }
        
        return true;
    }
       
       //Added for the internal issue #1753 start
       private boolean canPerformNotifyCommittee(final String committeeId , final String scheduleId,
                                                 final SubmissionDetailsBean detailsBean){
           boolean canPerformNotifyCommittee = true;
           
           if( scheduleId != null && scheduleId.trim().length() != 0 ) {
               CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("iacucProtoSubmission_exceptionCode.1002"));
               canPerformNotifyCommittee = false;
           }
           else if((scheduleId == null || "".equals(scheduleId)) && (committeeId == null || "".equals(committeeId))){
               CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("iacucProtoSubmission_exceptionCode.1001"));
               canPerformNotifyCommittee = false;
           }else{
               vDetails = getSubmissionDetails(detailsBean);
               if(vDetails != null){
                   ProtocolSubmissionInfoBean submissionInfoBean = null;
                   int submissionNumber = detailsBean.getSubmissionNumber();
                   
                   for(Object obj : vDetails){
                       submissionInfoBean = (ProtocolSubmissionInfoBean)obj;
                       if(submissionNumber == submissionInfoBean.getSubmissionNumber()){
                           //Added for COEUSQA-3399 : IRB Protocol Renewal Reminder notification error  - start
                           //If submission status code is 205(Iacuc Acknowledgement) and 200(Approved)then we should allow to perform notify committee action
                           if(IACUC_ACKNOWLEDGEMENT_SUB_STATUS == submissionInfoBean.getSubmissionStatusCode() || APPROVED_SUB_STATUS == submissionInfoBean.getSubmissionStatusCode()){
                               canPerformNotifyCommittee = true;
                           }else if(submissionInfoBean.getSubmissionStatusCode() != NOTIFY_COMMITTEE_SUBM_STATUS_CODE){
                               CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("iacucProtoSubmission_exceptionCode.1000"));
                               canPerformNotifyCommittee = false;
                           }
                           
                           //Added for COEUSQA-3399 : IRB Protocol Renewal Reminder notification error  - end
                           break;
                       }
                   }
              }
           }

           return canPerformNotifyCommittee;
       }
         //Added for the internal issue #1753 end 
    //nadh nadh
    /* This method is added for enhancements to Refine Notify Committee action.
     *@author Nadh
     *@returns String
     *starts 3 aug 2004
     */
    private String getCurrentRecord(int submissionNumber) {

        String commName="";
        for (int idx = 0 ; idx < vDetails.size() ; idx++) {
            ProtocolSubmissionInfoBean validateSubmissionInfoBean
                    = (ProtocolSubmissionInfoBean)vDetails.get(idx);

            if (validateSubmissionInfoBean.getSubmissionNumber() == submissionNumber) {
                //recIdx = idx ;
                commName = validateSubmissionInfoBean.getCommitteeName();
            }
        }
        return commName ;
    }
    //Nadh end
    
//    private HashMap getValidSubmissionsForAssignment(HashMap selectedSubmissions )
//        throws Exception {
//
//        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/protocolSubSrvlt";
//        RequesterBean request = new RequesterBean();
//        request.setFunctionType('B');
//        request.setDataObject( selectedSubmissions );
//        AppletServletCommunicator comm = new AppletServletCommunicator(
//                connectTo, request);
//        setCursor( new Cursor( Cursor.WAIT_CURSOR ) );
//        comm.send();
//        ResponderBean response = comm.getResponse();
//        setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
//
//        if (response.isSuccessfulResponse()) {
//            return ( HashMap )response.getDataObject();
//        }else{
//            throw new Exception(response.getMessage());
//        }
//
//    }
    
    private void saveAssignedSubmissions( ProtocolActionsBean actionBean ) {
        Vector dataObjects = new Vector();
        dataObjects.addElement( actionBean );
        // boolean which specifies whether to release lock or not ( used in Schedules )
        dataObjects.addElement( new Boolean(false) );
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/IacucProtocolActionServlet";
        RequesterBean request = new RequesterBean();
        request.setFunctionType('X');
        request.setDataObjects( dataObjects );
        AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, request);
        setCursor( new Cursor( Cursor.WAIT_CURSOR ) );
        comm.send();
        ResponderBean response = comm.getResponse();
        setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
        
        if (!response.isSuccessfulResponse()) {
            CoeusOptionPane.showInfoDialog( response.getMessage() );
        }else{
            Object responseObject = response.getDataObject();
            Vector responseVec = (Vector)responseObject;
            if ( responseVec != null  && responseVec.size() >=4  && responseVec.get(3) != null ){
                Vector docList = (Vector) responseVec.elementAt(3);
                DocumentList documentList = new DocumentList(CoeusGuiConstants.getMDIForm(),true);
                documentList.setProtocolNumber(actionBean.getProtocolNumber());
                documentList.setMenuAction(actionBean.getActionTypeDescription());
                //Added for Coeus4.3 enhancement - Email Notification - start
                documentList.setActionBean(actionBean);
                //Added for Coeus4.3 enhancement - Email Notification - end
                documentList.loadForm(docList);
                documentList.display();
            }else{
                //CoeusOptionPane.showInfoDialog("This protocol has no Documents");
                CoeusOptionPane.showInfoDialog("Requested action on Protocol " + actionBean.getProtocolNumber() + " completed successfully") ;
            }
            if( responseVec.size() > 4 ){
                updateActionStatus((ProtocolActionChangesBean)responseVec.elementAt(4));
            }
            //COEUSQA:1724-Email Notifications For All Actions In IACUC - 'Notify Committee' - start
            ProtocolMailController mailController = new ProtocolMailController();
            synchronized(mailController) {
                mailController.sendMail(actionBean.getActionTypeCode(), actionBean.getProtocolNumber(), actionBean.getSequenceNumber());
            }
            //COEUSQA:1724-Email Notifications For All Actions In IACUC - 'Notify Committee' - start
        }
    }
    
    private void updateActionStatus( ProtocolActionChangesBean actionChangeBean ) {
        if( actionChangeBean != null ) {
            SearchColumnIndex searchColumnIndex = new SearchColumnIndex();
            String protocolId = actionChangeBean.getProtocolNumber();
            if( protocolId.length() > 10 ) {
                protocolId = protocolId.substring(0,10);
            }
            int submissionNumber = actionChangeBean.getSubmissionNumber();         
//            int updateRow = Integer.parseInt( (String)searchResults.get(protocolId+submissionNumber));
            int updateRow = tblProtocol.getSelectedRow();
            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                    protocolId,updateRow,searchColumnIndex.getSearchColumnIndex(coeusSearch,PROTOCOL_NUMBER));
            
            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                    actionChangeBean.getSubmissionStatusDescription(), updateRow,
                    searchColumnIndex.getSearchColumnIndex(coeusSearch,"SUBMISSION_STATUS_DESCRIPTION"));
            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                    actionChangeBean.getSubmissionTypeDescription(), updateRow,
                    searchColumnIndex.getSearchColumnIndex(coeusSearch,"SUBMISSION_TYPE_DESCRIPTION"));
            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                    actionChangeBean.getCommitteeId(), updateRow,
                    searchColumnIndex.getSearchColumnIndex(coeusSearch,COMMITTEE_ID));
            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                    actionChangeBean.getScheduleId(), updateRow,
                    searchColumnIndex.getSearchColumnIndex(coeusSearch,SCHEDULE_ID));
            
            String dateValue = "";
            if(actionChangeBean.getScheduleDate() != null){
                //Modified for PT ID#3243 - date format
                String dateFormat = CoeusServerProperties.getProperty(CoeusPropertyKeys.SEARCH_DATE_FORMAT, SIMPLE_DATE_FORMAT);
                //COEUSQA-1477 Dates in Search Results - Start
                dateValue = new DateUtils().parseDateForSearchResults(actionChangeBean.getScheduleDate().toString(), dateFormat);
                //dateValue = new DateUtils().formatDate(
                //        actionChangeBean.getScheduleDate().toString(), dateFormat);
            }else{
                dateValue = "";
            }
            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                    dateValue, updateRow,
                    searchColumnIndex.getSearchColumnIndex(coeusSearch,"SCHEDULED_DATE"));
            
            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                    ""+submissionNumber, updateRow,
                    searchColumnIndex.getSearchColumnIndex(coeusSearch,SUBMISSION_NUMBER));
            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                    ""+actionChangeBean.getSequenceNumber(), updateRow,
                    searchColumnIndex.getSearchColumnIndex(coeusSearch,SEQUENCE_NUMBER));
        }
        
    }
    
    public void saveAsActiveSheet() {
        SaveAsDialog saveAsDialog = new SaveAsDialog(tblProtocol);
    }
    
    /**
     * Getter for property status.
     * @return Value of property status.
     */
    public int getStatus() {
        return status;
    }
    
    /**
     * Setter for property status.
     * @param status New value of property status.
     */
    public void setStatus(int status) {
        this.status = status;
    }
    //Added by Nadh to get the column header and its status Start 19-01-2005
    //Inner Class Mouse Adapter - START
    class SubmissionMouseAdapter extends MouseAdapter{
        public void mouseClicked(MouseEvent mouseEvent){
            JTableHeader tblHeader = (JTableHeader) mouseEvent.getSource();
            TableColumnModel columnModel = tblHeader.getColumnModel();
            int viewColumn = columnModel.getColumnIndexAtX(mouseEvent.getX());
            int column = columnModel.getColumn(viewColumn).getModelIndex();
            int sortStatus = getStatus();
            if(oldCol != column ){
                sortStatus = MultipleTableColumnSorter.NOT_SORTED;
            }
            sortStatus = sortStatus + (mouseEvent.isShiftDown() ? -1 : 1);
            sortStatus = (sortStatus + 4) % 3 - 1;
            setStatus(sortStatus);
            oldCol = column;
            if(getStatus()==MultipleTableColumnSorter.ASCENDING || getStatus() == MultipleTableColumnSorter.DESCENDING) {
                Vector newSortedData = new Vector();
                newSortedData.addElement(tblProtocol.getColumnName(column));
                newSortedData.addElement(new Integer(column));
                newSortedData.addElement(new Boolean(status == 1 ? true : false));
                if(vecSortedData == null){
                    vecSortedData = new Vector();
                }
                vecSortedData.removeAllElements();
                vecSortedData.addElement(newSortedData);
            }else {
                vecSortedData = null;
            }
            
        }
    }// Added by Nadh - End
    
    //COEUSQA-1724-New Amendment/Renewal type -start
    /*This method is used to set the amendment/renewal details Title
     *@param String refId
     *@param String moduleName
     *@return  String moduleName
     */
    private String setModuleName(final String refId, String moduleName) {        
        if( refId.charAt(10) == CoeusConstants.IACUC_AMENDMENT) {
            // Amendment
            moduleName = CoeusGuiConstants.IACUC_AMENDMENT_DETAILS_TITLE;            
        }else if(refId.charAt(10) == CoeusConstants.IACUC_RENEWAL){
            // Renewal
            moduleName = CoeusGuiConstants.IACUC_RENEWAL_DETAILS_TITLE;            
        }else if(refId.charAt(10) == CoeusConstants.IACUC_RENEWAL_WITH_AMENDMENT){
            // Renewal/Amendment
            moduleName = CoeusGuiConstants.IACUC_RENEWAL_WITH_AMENDMENT_TITLE;                    
        }else if(refId.charAt(10) == CoeusConstants.IACUC_CONTINUATION_CONTINUING_REVIEW){
            // Continuation/Continuing Review
            moduleName = CoeusGuiConstants.IACUC_CONTINUATION_RENEWAL_DETAILS_TITLE;                    
        }else if(refId.charAt(10) == CoeusConstants.IACUC_CONTI_CONTINUING_REVIEW_WITH_AMEND){
            // Continuation/Continuing Review with Amendment
            moduleName = CoeusGuiConstants.IACUC_CONTINUATION_RENEWAL_AMEND_DETAILS_TITLE;                    
        }
        return moduleName;
    }
    
    /*This method is used to set the amendment/renewal details Title 
     *@param char mode    
     *@param String moduleName
     *@param String refId   
     */
     private void setProtocolWindow(final char mode, final String moduleName, final String refId) throws Exception {                                     
        if( moduleName.equalsIgnoreCase(CoeusGuiConstants.IACUC_RENEWAL_DETAILS_TITLE) ){
            mainProtocol = new ProtocolDetailForm(refId, mode,
                    CoeusGuiConstants.PROTOCOL_RENEWAL_CODE);
        }else if( moduleName.equalsIgnoreCase(CoeusGuiConstants.IACUC_AMENDMENT_DETAILS_TITLE) ){
            mainProtocol = new ProtocolDetailForm(refId, mode,
                    CoeusGuiConstants.PROTOCOL_AMENDMENT_CODE);
        }else if( moduleName.equalsIgnoreCase(CoeusGuiConstants.IACUC_RENEWAL_WITH_AMENDMENT_TITLE) ){
            mainProtocol = new ProtocolDetailForm(refId, mode,
                    CoeusGuiConstants.PROTOCOL_AMENDMENT_WITH_RENEWAL_CODE);
        }else if( moduleName.equalsIgnoreCase(CoeusGuiConstants.IACUC_CONTINUATION_RENEWAL_DETAILS_TITLE) ){
            mainProtocol = new ProtocolDetailForm(refId, mode,
                    CoeusGuiConstants.PROTOCOL_CONTINUING_WITH_RENEWAL_CODE);
        }else if( moduleName.equalsIgnoreCase(CoeusGuiConstants.IACUC_CONTINUATION_RENEWAL_AMEND_DETAILS_TITLE) ){
            mainProtocol = new ProtocolDetailForm(refId, mode,
                    CoeusGuiConstants.PROTOCOL_CONTINUING_WITH_RENEWAL_AMEND_CODE);
        }        
        else {
            mainProtocol = new ProtocolDetailForm( mode, refId, mdiForm);
        }
    }
    //COEUSQA-1724-New Amendment/Renewal type -end
    
}

