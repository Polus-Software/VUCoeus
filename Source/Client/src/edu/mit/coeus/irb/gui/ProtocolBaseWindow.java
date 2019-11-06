/*
 * @(#)ProtocolBaseWindow.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

/* PMD check performed, and commented unused imports and variables on 27-JUNE-2007
 * by Nandkumar S N
 */

package edu.mit.coeus.irb.gui;

//import edu.mit.coeus.gui.CoeusAppletMDIForm;
//import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.irb.controller.ProtocolHistoryController;
import edu.mit.coeus.propdev.bean.ProposalAwardHierarchyLinkBean;
import edu.mit.coeus.propdev.gui.MedusaDetailForm;
//import edu.mit.coeus.mail.controller.ActionValidityChecking;
import edu.mit.coeus.search.gui.CoeusSearch;
//import edu.mit.coeus.utils.CoeusGuiConstants;
//import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.irb.gui.ProcessAction; //Added by Vyjayanthi
import edu.mit.coeus.irb.gui.ProtocolActionsInterface; //Added by Vyjayanthi
import edu.mit.coeus.irb.bean.ProtocolInfoBean;
import edu.mit.coeus.irb.bean.ProtocolInvestigatorsBean;
import edu.mit.coeus.irb.bean.ProtocolInvestigatorUnitsBean;
import edu.mit.coeus.irb.bean.ProtocolActionsBean;
import edu.mit.coeus.irb.bean.ProtocolActionChangesBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusClientException;

//Added by sharath - 19/10/2003 for Save As ToolBar Button - Start
import edu.mit.coeus.utils.saveas.SaveAsDialog;
//Added by sharath - 19/10/2003 for Save As ToolBar Button - End

import edu.mit.coeus.brokers.* ; //prps added
import edu.mit.coeus.utils.* ; //prps added

//import edu.mit.coeus.irb.gui.ScheduleActionInputForm;
//import edu.mit.coeus.utils.MultipleTableColumnSorter;
//import edu.mit.coeus.utils.SortForm;
//added by Nadh


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
//import java.util.Vector;
//import java.util.Observer;
//import java.util.Observable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

/** This class is used to construct the parent window of Protocol and associate
 * its menu and toolbar with listener. This window will display the Protocols
 * from which the user can select the protocol details.
 *
 * @author Mukundan C
 * @version 1.0 October 18, 2002, 3:03 PM
 * @updated Subramanya S
 * @version 1.1 Noverber 04, 2002, 6.25PM
 * For Copy Functionality Implementation.
 */

public class ProtocolBaseWindow extends CoeusInternalFrame
        implements ActionListener, ProtocolActionsInterface, Observer, MouseListener {    //ProtocolActionsInterface added by Vyjayanthi
    
    // Menu items for protocol
    //Modified for Coeus 4.3 enhancement -PT ID 2210 - View Protcol History -start
    //Added the protocolHistory menu item
    private CoeusMenuItem protocolDisplay,protocolAdd,protocolModify,
            protocolCopy,mnItProtocolSearch, protocolSummary,protocolNewAmend,
            protocolRenewal, protocolHistory,
            /*Added for Case# 3018 -create ability to delete pending studies - Start*/
            protocolDelete,
            /*Added for Case# 3018 -create ability to delete pending studies - End*/
            //Modified for Coeus 4.3 enhancement -PT ID 2210 - View Protcol History - end
            /*Added for Case# 4259 -New Amendment/Renewal in Menu - Start*/
            protocolRenewWithAmend;
            /*Added for Case# 4259 -New Amendment/Renewal in Menu - End*/
    //Added by Vyjayanthi - 30/07/2004 for IRB Enhancement
    private CoeusMenuItem administrativeCorrection;
    
    //Added by Vyjayanthi - 30/07/2004 for IRB Enhancement
    private final char ADMINISTRATIVE_CORRECTION = 'J';
    
    //Added by Vyjayanthi - 02/08/2004 for IRB Enhancement
    private Vector vecProtocolData;
    
    //Added by Vyjayanthi on 01/09/03
    //Start
    //Menu items for Actions
    // Modified for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start
    private CoeusMenuItem protocolRequestToTerminate, protocolRequestToClose,
            protocolRequestSuspension, protocolClosed, protocolTerminated, protocolSuspend,
            requestCloseEnrollment,closeEnrollment,protocolSuspendByDSMB,protocolExpire,protocolAbandon;
    // Modified for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end
    
    //Added for performing Protocol Actions - start - 1
    private CoeusMenuItem protocolRequestToReopen, protocolRequestToDA, protocolNotify, protocolReopen, protocolDA;
    //Added for performing Protocol Actions - end - 1
    
    private ProcessAction processAction;
    
    //private int sequenceNumber;
    //End
    
    // Toolbar for protocol
    private CoeusToolBarButton btnAddProtocol;
    private CoeusToolBarButton btnModifyProtocol;
    private CoeusToolBarButton btnDisplayProtocol;
    private CoeusToolBarButton btnSearchProtocol;
    private CoeusToolBarButton btnSaveAsProtocol;
    private CoeusToolBarButton btnSummaryProtocol;
    private CoeusToolBarButton btnCloseProtocol;
    
    private CoeusToolBarButton btnSort;//Added by nadh - 19/01/2005 for Sort ToolBar Button
    
    /*Added for Case# 3018 -create ability to delete pending studies - Start*/
    private CoeusToolBarButton btnDeleteProtocol;
    /*Added for Case# 3018 -create ability to delete pending studies - End*/
    
    //holds the Copy button
    private CoeusToolBarButton btnCopyProtocol;
    
    //private String windowName;
    // holds the search window name
    private final String PROTOCOL_SEARCH = "protocolSearch";
    // holds the instance of CoeusSearch
    private CoeusSearch coeusSearch;
    
    ProtocolDetailForm mainProtocol;
    //Main MDI Form.
    private CoeusAppletMDIForm mdiForm = null;
    
    // add functionality
    private final char ADD_FUNCTION = 'A';
    // modify functionality
    private final char MODIFY_FUNCTION = 'M';
    // display functionality
    private final char DISPLAY_FUNCTION = 'D';
    
    //holds the Copy type falg
    private final char COPY_FUNCTION = 'E';
    
    private final char CHECK_IF_EDITABLE = 'C' ; //prps added
    
    /*Added for Case# 3018 -create ability to delete pending studies - Start*/
    private final char DELETE_PROTOCOL = 'i';
    private final char GET_DELETE_RIGHTS = 'u';
    private boolean isAuthorised = false; 
    /*Added for Case# 3018 -create ability to delete pending studies - End*/
    //private int selectedprotocolRow = -1;
    // Jtable
    private JTable tblProtocol;
    // Scroll bar pane
    private JScrollPane scrPnSearchRes;
    
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    /*private final String AMEND_VERSION = " A000";
     
    private final String REVISION_VERSION = " R000";*/
    
    /*
     * to indicate horizondal seperator in menu items
     */
    private final String SEPERATOR="seperator";
    
    //holds the selected row of base table whose details are shown in this form
    private int baseTableRow;
    
    private String PIName,leadUnitNumber,leadUnitName;
    
    /** holds ScheduleActionInputForm instance */
    //private ScheduleActionInputForm inputForm ;
    
    //private String EMPTY=""; //Added by Nadh
    
    //private boolean lockSchedule = true; //Added by Nadh
    
    //private String userInput = null; //Added by Nadh
    
    //private Vector reviewComments = null; //Added by Nadh
    
    //private boolean releaseLock; //Added by Nadh
    
    //Added by nadh - 19/01/2005 for Sort ToolBar Button - Start
    
    //holds sorted columns and its states
    private Vector vecSortedData;
    
    private static final int OK_CLICKED = 0;
    
    //holds column index
    private int oldCol = -1;
    
    //holds column status
    private int status=MultipleTableColumnSorter.NOT_SORTED;
    //Added by Nadh - End
    // static value for PROTOCOL_NUMBER
    private static final String PROTOCOL_NUMBER = "PROTOCOL_NUMBER";
    
    //Added for case#3243
    private static final String SIMPLE_DATE_FORMAT = "yyyy/MM/dd";    
    private static final String SEQUENCE_COLUMN_HEADER_NAME = "Sequence Number";
    //COEUSQA:2653 - Add Protocols to Medusa - Start
    private CoeusToolBarButton btnMedusa;
    //COEUSQS:2653 - End
    
    //COEUSQA:3503 - Protocol Copying - Option to Copy Attachments and Questionnaires - Start
    private static final String SEQUENCE_NUMBER = "SEQUENCE_NUMBER";
    private static final String ENABLE_COPY_PROTO_QNR = "ENABLE_COPY_PROTO_QNR";
    private static final String ENABLE_COPY_PROTO_ATTACHMENT = "ENABLE_COPY_PROTO_ATTACHMENT";
    private static final String ENABLE_COPY_PROTO_OTHER_ATTACHMENT = "ENABLE_COPY_PROTO_OTHER_ATTACHMENT";
    private static final String GET_PARAMETER_VALUE = "GET_PARAMETER_VALUE";
    private static final char CHECK_CAN_COPY_QUESTIONNAIRE = 'd';
    private static final String QUESTIONNAIRE_SERVLET = "/questionnaireServlet";
    private static final char COPY_PROTOCOL = 'P';
    //COEUSQA:3503 - End
    
    /** Constructor to create new <CODE>ProtocolBaseWindow</CODE>
     *
     * @param mdiForm reference to <CODE>CoeusAppletMDIForm</CODE>.
     */
    public ProtocolBaseWindow(CoeusAppletMDIForm mdiForm) {
        //Modified for COEUSQA-2580_Change menu item name from "Protocol" to "IRB Protocol" on Maintain menu in Premium_Start
        super("IRB Protocol", mdiForm,LIST_MODE);
        //Modified for COEUSQA-2580_Change menu item name from "Protocol" to "IRB Protocol" on Maintain menu in Premium_End
        this.mdiForm = mdiForm;
        try {
            initComponents();
            mdiForm.putFrame(CoeusGuiConstants.PROTOCOLBASE_FRAME_TITLE, this);
            mdiForm.getDeskTopPane().add(this);
            this.setSelected(true);
            this.setVisible(true);
            showProtocolSearchWindow();
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }
    
    
    /**
     * Construct the components for the base window
     *
     * throws Exception
     */
    private void initComponents() throws Exception {
        setFrameMenu(getProtocolEditMenu());
        setActionsMenu(getProtocolActionsMenu());   //Added by Vyjayanthi
        setToolsMenu(getProtocolToolsMenu());
        setFrameToolBar(getProtocolToolBar());
        setFrame(CoeusGuiConstants.PROTOCOLBASE_FRAME_TITLE);
        setFrameIcon(mdiForm.getCoeusIcon());
        createProtocolInfo();
        tblProtocol.addMouseListener(this);
        coeusMessageResources = CoeusMessageResources.getInstance();
    }
    
    //Added by Nadh for CoeusSearch enhancement(CustomQuery)   start : 3-aug-2005
    private CoeusToolBarButton[] searchButtons;
    /*  prepares toolbar buttons for custom queries
     * @param vecToolBarData contains button tooltips
     */
    private void prepareCQToolBarButtons(Vector vecToolBarData) {
        if(vecToolBarData == null) return;
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
    }//End : 3-aug-2005
    
    
    /**
     * This methods the empty protocol table for the base window
     *
     */
    private void createProtocolInfo() throws Exception {
        coeusSearch = new CoeusSearch(mdiForm, PROTOCOL_SEARCH, 0);
        prepareCQToolBarButtons(coeusSearch.getCustomQueryData());//Added by Nadh for CoeusSearch enhancement(CustomQuery)   start : 3-aug-2005
        tblProtocol = coeusSearch.getEmptyResTable();
        javax.swing.table.TableColumn clmName = tblProtocol.getColumnModel().getColumn(
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
        setSearchResultsTable(tblProtocol);
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
        
    }
    
    //Added by Nadh for CustomQuery Search enhancement   start : 3-aug-2005
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
            tblProtocol.getTableHeader().addMouseListener(new ProtocolMouseAdapter());
            tblProtocol.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent me) {
                    if (me.getClickCount() == 2) {
                        try {
                            int selectedRow = tblProtocol.getSelectedRow();
                            // Get the protocol Number from the search result
                            String protocolNumber = getSelectedValue(selectedRow,PROTOCOL_NUMBER);
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
    }//End 3-aug-2005
    
    /**
     * This methods loads the search window when the protocol module is opened,
     * this helps the user to load the base window with the protocol details.
     *
     */
    private void showProtocolSearchWindow() {
        try {
            //Case 2451 start
            vecSortedData = new Vector();
            //Case 2451 End
            coeusSearch.showSearchWindow();
            JTable tblResultsTable = coeusSearch.getSearchResTable();
            buildSearchResultsTable(tblResultsTable);
            //commented by nadh for customQuery search enhancement start 3-aug-2005
            //setSearchResultsTable(tblResultsTable);
//            if (tblResultsTable != null) {
//                tblProtocol = tblResultsTable;
//                javax.swing.table.TableColumn clmName
//                = tblProtocol.getColumnModel().getColumn(
//                tblProtocol.getColumnCount()-1);
//                clmName.setMaxWidth(0);
//                clmName.setMinWidth(0);
//                clmName.setPreferredWidth(0);
//
//                clmName = tblProtocol.getColumnModel().getColumn(tblProtocol.getColumnCount()-2);
//                clmName.setMaxWidth(0);
//                clmName.setMinWidth(0);
//                clmName.setPreferredWidth(0);
//
//                javax.swing.table.JTableHeader header
//                = tblProtocol.getTableHeader();
//                //header.setResizingAllowed(false);
//                header.setReorderingAllowed(false);
//                scrPnSearchRes.setViewportView(tblProtocol);
//                tblProtocol.getTableHeader().addMouseListener(new ProtocolMouseAdapter());
//                tblProtocol.addMouseListener(new MouseAdapter() {
//                    public void mouseClicked(MouseEvent me) {
//                        if (me.getClickCount() == 2) {
//                            try {
//                                int selectedRow = tblProtocol.getSelectedRow();
//                                String protocolNumber =
//                                tblProtocol.getValueAt(
//                                selectedRow, 0).toString();
//                                //BUG FIX,bug Id:1062 - Hour glass Implemntation START
//                                mdiForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
//                                showProtocolDetails(protocolNumber);
//                                mdiForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
//                                //BUG FIX,bug Id:1062 - Hour glass Implemntation END
//                            } catch (Exception e) {
//                                e.printStackTrace();
////                                CoeusOptionPane.showErrorDialog(e.getMessage());
//                            }
//                        }
//                    }
//                });
//            }
//            this.revalidate();
            //End   3-aug-2005
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * constructs Protocol edit menu with sub menu Add,Modify,Dispaly,Copy and
     * protocol Detail Forms.
     *
     * @return CoeusMenu Protocol edit menu
     */
    private CoeusMenu getProtocolEditMenu() {
        CoeusMenu mnuProtocol = null;
        Vector fileChildren = new Vector();
        
        protocolAdd = new CoeusMenuItem("Add IRB Protocol", null, true, true);
        protocolAdd.setMnemonic('A');
        protocolAdd.addActionListener(this);
        
        protocolModify = new CoeusMenuItem("Modify IRB Protocol", null, true, true);
        protocolModify.setMnemonic('M');
        protocolModify.addActionListener(this);
        
        protocolDisplay = new CoeusMenuItem("Display IRB Protocol", null, true, true);
        protocolDisplay.setMnemonic('D');
        protocolDisplay.addActionListener(this);
        
        /*Added for Case# 3018 -create ability to delete pending studies - Start*/
        protocolDelete = new CoeusMenuItem("Delete IRB Protocol", null, true, true);
        protocolDelete.setMnemonic('e');
        protocolDelete.addActionListener(this);
        /*Added for Case# 3018 -create ability to delete pending studies - End*/
        protocolCopy = new CoeusMenuItem("Copy IRB Protocol", null, true, true);
        protocolCopy.setMnemonic('C');
        protocolCopy.addActionListener(this);
        
        protocolSummary = new CoeusMenuItem("View Summary", null, true, true);
        protocolSummary.setMnemonic('S');
        protocolSummary.addActionListener(this);
        
        //Added for Coeus4.3 enhancement - PT ID:2210 - View Protocol History - start
        protocolHistory = new CoeusMenuItem("View History", null, true, true);
        protocolHistory.setMnemonic('H');
        protocolHistory.addActionListener(this);
        //Added for Coeus4.3 enhancement - PT ID:2210 - View Protocol History - end
        
        //Added by Vyjayanthi - 30/07/2004 for IRB Enhancement - Start
        administrativeCorrection = new CoeusMenuItem("Administrative Correction", null, true, true);
        administrativeCorrection.setMnemonic('i');
        administrativeCorrection.addActionListener(this);
        //Added by Vyjayanthi - 30/07/2004 for IRB Enhancement - End
        
        protocolNewAmend = new CoeusMenuItem("New Amendment", null,true,true);
        protocolNewAmend.setMnemonic('N');
        protocolNewAmend.addActionListener(this);
        
        protocolRenewal = new CoeusMenuItem("Protocol Renewal", null,true,true);
        protocolRenewal.setMnemonic('R');
        protocolRenewal.addActionListener(this);
        
        /*Added for Case# 4259 -New Amendment/Renewal in Menu - Start*/
        //Added for COEUSQA-3122 IACUC - Rename "New Renewal/Amendment" menu item - start
        //protocolRenewWithAmend = new CoeusMenuItem("New Renewal/Amendment", null,true,true);
        protocolRenewWithAmend = new CoeusMenuItem("New Renewal with Amendment", null,true,true);
        //Added for COEUSQA-3122 IACUC - Rename "New Renewal/Amendment" menu item - end
        protocolRenewWithAmend.setMnemonic('W');
        protocolRenewWithAmend.addActionListener(this);
        /*Added for Case# 4259 -New Amendment/Renewal in Menu - End*/
        
        // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start        
        protocolAbandon = new CoeusMenuItem("Abandon Protocol", null, true, true);
        protocolAbandon.setMnemonic('b');
        protocolAbandon.addActionListener(this);
        protocolAbandon.setActionCommand( "" + ACTION_PROTOCOL_ABANDON);
        // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end
        
        protocolRequestToClose = new CoeusMenuItem("Request to Close", null, true, true);
        protocolRequestToClose.setMnemonic('C');
        protocolRequestToClose.addActionListener(this);
        protocolRequestToClose.setActionCommand( "" + ACTION_REQUEST_TO_CLOSE);
        
        requestCloseEnrollment = new CoeusMenuItem("Request to Close Enrollment",null,true,true);
        requestCloseEnrollment.setMnemonic('q');
        requestCloseEnrollment.addActionListener(this);
        requestCloseEnrollment.setActionCommand( "" + ACTION_REQUEST_TO_CLOSE_ENROLMENT);
        
        protocolRequestToTerminate = new CoeusMenuItem("Request to Terminate", null, true, true);
        protocolRequestToTerminate.setMnemonic('T');
        protocolRequestToTerminate.addActionListener(this);
        protocolRequestToTerminate.setActionCommand( "" + ACTION_REQUEST_FOR_TERMINATION);
        
        protocolRequestSuspension = new CoeusMenuItem("Request for Suspension", null, true, true);
        protocolRequestSuspension.setMnemonic('U');
        protocolRequestSuspension.addActionListener(this);
        protocolRequestSuspension.setActionCommand( "" + ACTION_REQUEST_FOR_SUSPENSION);
        
        //Added for performing Protocol Actions - start - 2
        protocolRequestToReopen = new CoeusMenuItem("Request to Re-open Enrollment", null, true, true);
        protocolRequestToReopen.setMnemonic('R');
        protocolRequestToReopen.addActionListener(this);
        protocolRequestToReopen.setActionCommand( "" + ACTION_REQUEST_TO_REOPEN_ENROLLMENT);
        
        protocolRequestToDA = new CoeusMenuItem("Request for Data Analysis", null, true, true);
        protocolRequestToDA.setMnemonic('D');
        protocolRequestToDA.addActionListener(this);
        protocolRequestToDA.setActionCommand( "" + ACTION_REQUEST_TO_DATA_ANALYSIS);
        //Added for performing Protocol Actions - end - 2
        
        fileChildren.add(protocolAdd);
        fileChildren.add(protocolModify);
        fileChildren.add(protocolDisplay);
        /*Added for Case# 3018 -create ability to delete pending studies - Start*/        
        fileChildren.add(protocolDelete);
        /*Added for Case# 3018 -create ability to delete pending studies - End*/
        fileChildren.add(protocolCopy);
        fileChildren.add(SEPERATOR);
        fileChildren.add(protocolSummary);
        fileChildren.add(protocolHistory);
        fileChildren.add(SEPERATOR);
        //Added by Vyjayanthi - 30/07/2004 for IRB Enhancement - Start
        fileChildren.add(administrativeCorrection);
        fileChildren.add(SEPERATOR);
        //Added by Vyjayanthi - 30/07/2004 for IRB Enhancement - End
        fileChildren.add(protocolNewAmend);
        fileChildren.add(protocolRenewal);
        /*Added for Case# 4259 -New Amendment/Renewal in Menu - Start*/ 
        fileChildren.add(protocolRenewWithAmend);
        /*Added for Case# 4259 -New Amendment/Renewal in Menu - End*/ 
        fileChildren.add(SEPERATOR);
        // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start        
        fileChildren.add(protocolAbandon);
        // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end
        fileChildren.add(protocolRequestToClose);
        fileChildren.add(requestCloseEnrollment);
        //Added for performing Protocol Actions - start - 3
        fileChildren.add(protocolRequestToReopen);
        fileChildren.add(protocolRequestToDA);
        //Added for performing Protocol Actions - end - 3
        
        // fileChildren.add(protocolRequestToTerminate); // prps commented this on feb 05 2004
        fileChildren.add(protocolRequestSuspension);
        
        mnuProtocol = new CoeusMenu("Edit", null, fileChildren, true, true);
        mnuProtocol.setMnemonic('E');
        return mnuProtocol;
        
    }
    
    //Added by Vyjayanthi on 01/09/03
    //Start
    /**
     * constructs Protocol Actions menu with sub menu Withdraw Protocol,Request to Terminate,
     * Request to Close, Request for Suspension, Closed, Terminated, Suspend
     * protocol Detail Forms.
     *
     * @return CoeusMenu Protocol Actions menu
     */
    private CoeusMenu getProtocolActionsMenu() {
        CoeusMenu mnuProtocol = null;
        Vector fileChildren = new Vector();
        processAction = ProcessAction.getInstance();
        
        protocolClosed = new CoeusMenuItem("Close", null, true, true);
        protocolClosed.setMnemonic('L');
        protocolClosed.addActionListener(this);
        protocolClosed.setActionCommand( "" + ACTION_PROTOCOL_CLOSED);
        
        protocolTerminated = new CoeusMenuItem("Terminate", null, true, true);
        protocolTerminated.setMnemonic('E');
        protocolTerminated.addActionListener(this);
        protocolTerminated.setActionCommand( "" + ACTION_PROTOCOL_TERMINATED);
        
        protocolSuspend = new CoeusMenuItem("Suspend", null, true, true);
        protocolSuspend.setMnemonic('P');
        protocolSuspend.addActionListener(this);
        protocolSuspend.setActionCommand( "" + ACTION_PROTOCOL_SUSPEND);
        
        protocolSuspendByDSMB = new CoeusMenuItem("Suspend by DSMB", null, true, true);
        protocolSuspendByDSMB.setMnemonic('Y');
        protocolSuspendByDSMB.addActionListener(this);
        protocolSuspendByDSMB.setActionCommand( "" + ACTION_PROTOCOL_SUSPEND_BY_DSMB);
        
        protocolExpire = new CoeusMenuItem("Expire", null, true, true);
        protocolExpire.setMnemonic('X');
        protocolExpire.addActionListener(this);
        protocolExpire.setActionCommand( "" + ACTION_PROTOCOL_EXPIRE);
        
        closeEnrollment = new CoeusMenuItem("Close Enrollment",null,true,true);
        closeEnrollment.setMnemonic('l');
        closeEnrollment.addActionListener(this);
        closeEnrollment.setActionCommand( "" + ACTION_PROTOCOL_CLOSE_ENROLMENT);
        
        //Added for performing Protocol Actions - start - 4
        protocolNotify = new CoeusMenuItem("Notify IRB", null, true, true);
        protocolNotify.setMnemonic('N');
        protocolNotify.addActionListener(this);
        protocolNotify.setActionCommand( "" + ACTION_NOTIFY_IRB);
        
        protocolReopen = new CoeusMenuItem("Re-open Enrollment", null, true, true);
        protocolReopen.setMnemonic('r');
        protocolReopen.addActionListener(this);
        protocolReopen.setActionCommand( "" + ACTION_REOPEN_ENROLLMENT);
        
        protocolDA = new CoeusMenuItem("Data Analysis", null, true, true);
        protocolDA.setMnemonic('d');
        protocolDA.addActionListener(this);
        protocolDA.setActionCommand( "" + ACTION_DATA_ANALYSIS);
        //Added for performing Protocol Actions - end - 4
        
        //fileChildren.add(protocolRequestToClose);
        
        //fileChildren.add(SEPERATOR);
        fileChildren.add(closeEnrollment);
        fileChildren.add(protocolTerminated);
        fileChildren.add(protocolClosed);
        fileChildren.add(protocolSuspend);
        fileChildren.add(protocolSuspendByDSMB);
        fileChildren.add(protocolExpire);
        //Added for performing Protocol Actions - start - 5
        fileChildren.add(protocolNotify);
        fileChildren.add(protocolReopen);
        fileChildren.add(protocolDA);
        //Added for performing Protocol Actions - end - 5
        
        mnuProtocol = new CoeusMenu("Actions", null, fileChildren, true, true);
        mnuProtocol.setMnemonic('I');
        return mnuProtocol;
        
    }
    
    
    //End
    
    /** Constructs Protocol Tools menu for Search screen with sub menu of <CODE>Search</CODE>
     *
     * @return <CODE>CoeusMenu</CODE> Protocol tools menu.
     */
    public CoeusMenu getProtocolToolsMenu() {
        CoeusMenu coeusMenu;
        Vector fileChildren = new Vector();
        mnItProtocolSearch = new CoeusMenuItem("IRB Protocol Search", null, true, true);
        mnItProtocolSearch.setMnemonic('S');
        mnItProtocolSearch.addActionListener(this);
        fileChildren.add(mnItProtocolSearch);
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
    private JToolBar getProtocolToolBar() {
        JToolBar toolbar = new JToolBar();
        btnAddProtocol = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.ADD_ICON)),
                null, "Add IRB Protocol");
        btnModifyProtocol = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.EDIT_ICON)),
                null, "Modify IRB Protocol");
        btnDisplayProtocol = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.DISPLAY_ICON)),
                null, "Display IRB Protocol");
        
        
        /*Added for Case# 3018 -create ability to delete pending studies - Start*/
        btnDeleteProtocol = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.DELETE_ICON)),
                null, "Delete IRB Protocol");
        /*Added for Case# 3018 -create ability to delete pending studies - End*/
        
        btnCopyProtocol = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.COPY_ICON)),
                null, "Copy IRB Protocol");
        
        btnSearchProtocol = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)),
                null, "IRB Protocol Search");
        
        btnSaveAsProtocol = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.SAVEAS_ICON)),
                null, "Save As");
        
        btnCloseProtocol = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
                null, "Close IRB Protocol");
        
        btnSummaryProtocol = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_SUMMARY_ICON)),
                null, "View IRB Protocol Summary");
        
        //Added by nadh - 19/01/2005 for Sort ToolBar Button - Start
        btnSort = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.SORT_ICON)),null,
                "Sort IRB Protocol List");
        //Added by Nadh - End
        
        btnMedusa = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.MEDUSA_ICON)),null,
                "Medusa");
        
        btnAddProtocol.addActionListener(this);
        btnModifyProtocol.addActionListener(this);
        btnDisplayProtocol.addActionListener(this);
        
        /*Added for Case# 3018 -create ability to delete pending studies - Start*/
        btnDeleteProtocol.addActionListener(this);
        /*Added for Case# 3018 -create ability to delete pending studies - End*/
        
        btnSearchProtocol.addActionListener(this);
        btnSaveAsProtocol.addActionListener(this);
        btnSummaryProtocol.addActionListener(this);
        btnCloseProtocol.addActionListener(this);
        btnCopyProtocol.addActionListener(this);
        btnSort.addActionListener(this);//Added by nadh - 19/01/2005 for Sort ToolBar Button
        
        btnMedusa.addActionListener(this);
        
        toolbar.add(btnAddProtocol);
        toolbar.add(btnModifyProtocol);
        toolbar.add(btnDisplayProtocol);
        
        /*Added for Case# 3018 -create ability to delete pending studies - Start*/
        toolbar.add(btnDeleteProtocol);
        /*Added for Case# 3018 -create ability to delete pending studies - End*/
        
        toolbar.add(btnCopyProtocol);
        toolbar.add(btnSort);//Added by nadh - 19/01/2005 for Sort ToolBar Button
        toolbar.add(btnSearchProtocol);
        toolbar.add(btnSummaryProtocol);
        
        //Added by sharath - 19/10/2003 for Save As ToolBar Button - Start
        toolbar.add(btnSaveAsProtocol);
        //Added by sharath - 19/10/2003 for Save As ToolBar Button - End
        toolbar.add(btnMedusa);
        toolbar.addSeparator();
        
        toolbar.add(btnCloseProtocol);                
        
        toolbar.setFloatable(false);
        return toolbar;
    }
    
    /** All the  actions for the menu and toolbar associated with
     * the <CODE>ProtocolBaseWindow</CODE> will be invoked from this method.
     *
     * @param actionType <CODE>ActionEvent</CODE>, a semantic event which indicates that a
     * component-defined action occured.
     */
    public void actionPerformed(ActionEvent actionType) {
        try{
            int selectedRow = 0;            
            String protocolNumber = null;
            if (tblProtocol != null && tblProtocol.getRowCount() > 0) {
                selectedRow = tblProtocol.getSelectedRow();
                //Check whether row is selected
                if (selectedRow >= 0) {
                    // Get the protocol Number from the search result
                    protocolNumber = getSelectedValue(selectedRow, PROTOCOL_NUMBER);                    
                }
            }
            
            Object actSource = actionType.getSource();
            
        /* when the menu or tool bar add is clicked addProtocolDetails
         * method will be fired
         */
            if (actSource.equals(protocolAdd) || actSource.equals(btnAddProtocol)) {
                addProtocolDetails();
            /* when the menu or tool bar modify is clicked modifyProtocolDetails
             * method will be fired
             */
            } else if (actSource.equals(protocolModify)
            || actSource.equals(btnModifyProtocol)) {
                
                modifyProtocolDetails(protocolNumber);
            /* when the menu or tool bar display is clicked showProtocolDetails
             * method will be fired
             */
            } else if (actSource.equals(protocolDisplay)
            || actSource.equals(btnDisplayProtocol)) {
                showProtocolDetails(protocolNumber);
            } else if (actSource.equals(protocolCopy)
            || actSource.equals(btnCopyProtocol)) {
                //Copy Functionality Call - Copy From one protocol to another with
                //new Protocol ID(Max of protocol ID+1) and Sequence Number 1
                copyProtocolDetails(protocolNumber);
                
            } else if (actSource.equals(btnCloseProtocol)) {
                closeProtocolBaseWindow();
            }else if( actSource.equals( protocolSummary )
            || actSource.equals( btnSummaryProtocol ) ){
                showSummaryDetails();
            }
            //Added for Coeus4.3 enhancement - PT ID:2210 - View Protocol History - start
            else if(actSource.equals(protocolHistory)){
                showProtocolHistory();
            }
            //Added for Coeus4.3 enhancement - PT ID:2210 - View Protocol History - end
            else if ( actSource.equals( protocolNewAmend ) ) {
                createNewAmendment(protocolNumber);
            }else if ( actSource.equals( protocolRenewal ) ) {
//              createProtocolRenewal(protocolNumber);
                //Added for case#4278-start
                createProtocolRenewal(protocolNumber, CoeusGuiConstants.RENEWAL);
                //Added for case#4278-end
            }
            /*Added for Case# 4259 -New Amendment/Renewal in Menu - Start*/ 
            else if ( actSource.equals(protocolRenewWithAmend ) ) {
//              createProtocolRenewal(protocolNumber);
                //Added for case#4278-start
                createProtocolRenewal(protocolNumber,CoeusGuiConstants.RENEWAL_WITH_AMENDMENT);
                //Added for case#4278-end
            /*Added for Case# 4259 -New Amendment/Renewal in Menu - End*/    
            }else if (actSource.equals(mnItProtocolSearch)
            || actSource.equals(btnSearchProtocol)) {
                showProtocolSearchWindow();
            }
            /*Added for Case# 3018 -create ability to delete pending studies - Start*/
            else if (actSource.equals(protocolDelete)
            || actSource.equals(btnDeleteProtocol)) {
                performDelete(protocolNumber,selectedRow);
            }
            /*Added for Case# 3018 -create ability to delete pending studies - End*/
            else if( actSource.equals(administrativeCorrection) ){
                //Added by Vyjayanthi - 30/07/2004 for IRB Enhancement - Start
                modifyProtocolDetails(CoeusGuiConstants.ADMINISTRATIVE_CORRECTIONS, protocolNumber);
                //Added by Vyjayanthi - 30/07/2004 for IRB Enhancement - End
            }else if(actSource.equals(btnSaveAsProtocol)) {
                //Added by sharath - 19/10/2003 for Save As ToolBar Button - Start
                //showSaveAsDialog();
                //new implementation
                saveAsActiveSheet();
                
                //Added by sharath - 19/10/2003 for Save As ToolBar Button - End
            }
            //Added by nadh - 19/01/2005 for Sort ToolBar Button - Start
            else if (actSource.equals(btnSort)) {
                showSort();
            }//Added by Nadh - End
            //COEUSQA:2653 - Add Protocols to Medusa - Start
            else if (actSource.equals(btnMedusa)) {
                showMedusaWindow(protocolNumber);
            } //COEUSQA:2653 - End
            else{
                
                
                //Added by Vyjayanthi
                //Start
                selectedRow = tblProtocol.getSelectedRow();
                //Modified to get the sequence number column index from the Table header
//                int column = tblProtocol.getColumnCount()-2;
                selectedRow = tblProtocol.getSelectedRow();
                 int column = 0;
                 for(int index=0;index<tblProtocol.getColumnModel().getColumnCount();index++){
                     String columnName = tblProtocol.getColumnModel().getColumn(index).getHeaderValue().toString();
                     if(columnName != null && SEQUENCE_COLUMN_HEADER_NAME.equalsIgnoreCase(columnName)){
                         column = index;
                         break;
                     }
                 }
                //End
                
                int seqNumber = 0;
                if (selectedRow != -1 && column != -1 && actSource instanceof CoeusMenuItem ){
                    String strSequenceNumber = (String)tblProtocol.getValueAt(selectedRow, column);
                    seqNumber = Integer.parseInt(strSequenceNumber);
                    ProtocolActionsBean actionBean = new ProtocolActionsBean();
                    actionBean.setProtocolNumber( protocolNumber );
                    actionBean.setSequenceNumber( seqNumber );
                    actionBean.setActionTypeDescription( ((CoeusMenuItem)actSource).getText() );
                    actionBean.setActionTypeCode( Integer.parseInt(actionType.getActionCommand()) );
                    
                    if ( actSource.equals( protocolClosed )
                    || actSource.equals( protocolTerminated )
                    || actSource.equals( protocolSuspend )
                    || actSource.equals( protocolSuspendByDSMB )
                    || actSource.equals( protocolExpire )
                    || actSource.equals( closeEnrollment )
                    //Added for performing Protocol Actions - start - 6
                    || actSource.equals( protocolNotify )
                    || actSource.equals( protocolReopen )
                    || actSource.equals( protocolDA )){
                        //Added for performing Protocol Actions - end - 6
                        //Bug Fix:1359 Start 1
                        //baseTableRow = Integer.parseInt((String)tblProtocol.getValueAt(
                        //selectedRow,tblProtocol.getColumnCount()-1));
                        baseTableRow = selectedRow;
                        //Bug Fix:1359 End 1
                        
                        //prps start dec 15 2003
                        processAction(actionBean) ;
                        //prps end dec 15 2003
                        // prps commented this line - dec 15 2003
                        // updateActionStatus( processAction.performOtherAction(
                        //     actionBean ) );
                    // Modified for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start
                    }else if ( actSource.equals( protocolRequestToTerminate )
                    || actSource.equals( protocolAbandon )
                    || actSource.equals( protocolRequestToClose )
                    || actSource.equals( protocolRequestSuspension )
                    || actSource.equals(requestCloseEnrollment)
                    //Added for performing Protocol Actions - start - 7
                    || actSource.equals(protocolRequestToReopen)
                    || actSource.equals(protocolRequestToDA)){
                    // Modified for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end
                        //Added for performing Protocol Actions - end - 7
                        //Bug Fix:1359 Start 2
                        //baseTableRow = Integer.parseInt((String)tblProtocol.getValueAt(
                        //selectedRow,tblProtocol.getColumnCount()-1));
                        baseTableRow = selectedRow;
                        //Bug Fix:1359 End 2
                        //prps start dec 15 2003
                        
                        //nadh commented this  - jul 30 2004
                        
                            /* int opt = CoeusOptionPane.DEFAULT_NO;
                            opt = CoeusOptionPane.showQuestionDialog(
                                coeusMessageResources.parseMessageKey("processAction_exceptionCode.1020"),
                                CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                            if( opt == CoeusOptionPane.SELECTION_YES )
                            {
                                processAction(actionBean) ;
                            }  */
                        //prps end dec 15 2003
                        // prps commented this line - dec 15 2003
                        // updateActionStatus( processAction.processRequest(
                        //     actionBean ) );
                        
                        //nadh end commenting - jul 30 2004
                        
                        //Added for enhancement to bring up protocol action window - by Nadh
                        //start jul 30 2004
//                            String titleMsg = ((CoeusMenuItem)actSource).getText();
//                            titleMsg = titleMsg + "-" + actionBean.getProtocolNumber();
//                            String msgPrompt = coeusMessageResources.parseMessageKey("processAction_exceptionCode.1020");
//                            inputForm = new ScheduleActionInputForm( CoeusGuiConstants.getMDIForm(), titleMsg, msgPrompt,EMPTY) ;
//                            inputForm.setLockSchedule( lockSchedule );
//                            inputForm.setActionBean( actionBean );
//                            inputForm.showForm() ;
//                            userInput = inputForm.getUserInput() ;
//                            reviewComments = inputForm.getReviewComments();
//                            if (inputForm.performAction()) {
//                                actionBean.setComments( userInput );
//                                actionBean.setReviewComments( reviewComments );
//                                //releaseLock = inputForm.releaseScheduleLock();
                        processAction(actionBean);
//                            }//nadh end jul 30 2004
                    }
                }
                
            }
            //End
            
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
            ex.printStackTrace();
            CoeusOptionPane.showDialog(new CoeusClientException(ex));
        }catch( Exception err ){
            err.printStackTrace();
            log( err.getMessage() );
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
    
    //prps start dec 12 2003
    // This method will take care of processing actions having many followup actions
    // or actions having recursive followup actions
    // since updateActionStatus method needs to be called for every action. This
    // method cannot be a part of ProcessAction Object
    private void processAction(ProtocolActionsBean actionBean) throws Exception {
        ProtocolActionChangesBean protocolActionChangesBean ;
        protocolActionChangesBean =  processAction.performOtherAction( actionBean ) ;
        if (protocolActionChangesBean!= null) {
            updateActionStatus(protocolActionChangesBean);
            // update the actionBean
            actionBean.setProtocolNumber(protocolActionChangesBean.getProtocolNumber()) ;
            actionBean.setScheduleId(protocolActionChangesBean.getScheduleId()) ;
            actionBean.setSequenceNumber(protocolActionChangesBean.getSequenceNumber()) ;
            actionBean.setSubmissionNumber(protocolActionChangesBean.getSubmissionNumber()) ;
            
            Vector vecActionDetails = protocolActionChangesBean.getFollowupAction() ;
            if (vecActionDetails.size()>0) {
                Vector vecSubActions = (Vector)vecActionDetails.get(0) ;
                HashMap hashUserPrompt = (HashMap)vecActionDetails.get(1) ;
                HashMap hashUserPromptFlag = (HashMap)vecActionDetails.get(2) ;
                
                if (vecSubActions.size() > 0) // perform sub actions or followup actions.
                {  // for an action there cud be multiple followup actions
                    // or one followup action action might have another sub action (recursively)
                    for (int actionCount = 0; actionCount < vecSubActions.size(); actionCount++) {
                        int actionTypeCode = ((Integer)vecSubActions.get(actionCount)).intValue() ;
                        if (hashUserPromptFlag.get(new Integer(actionTypeCode)).toString().equalsIgnoreCase("Y") ) {
                            String promptMessage = hashUserPrompt.get(new Integer(actionTypeCode)).toString() ;
                            if (CoeusOptionPane.SELECTION_YES == CoeusOptionPane.showQuestionDialog(promptMessage,
                                    CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES)) { // prompt the user with msg obtained from database and if the choice is Yes continue follow up action
                                actionBean.setActionTypeCode(actionTypeCode) ;
                                processAction(actionBean) ;
                            }
                        } else { // if the prompt is flag is set to N then user will not be prompted for the followup action
                            actionBean.setActionTypeCode(actionTypeCode) ;
                            processAction(actionBean) ;
                        }
                    }//end for
                    
                }// end if vec sub actions
            }// end if vecActionDetails
            /** Coeus4.3 email notification - start*/
            //Commented for COEUSDEV-317 : Notification not working correctly in IRB Module - Start
            //Notification is prompted in ProcesAction performOtherAction method
//            ActionValidityChecking checkValid = new ActionValidityChecking();
//            synchronized(checkValid) {
//                checkValid.sendMail("114", "7", protocolActionChangesBean.getProtocolNumber(), protocolActionChangesBean.getSequenceNumber()+"");
//            }
            //COEUSDEV-317 : End
            //Coeus4.3 email notification - end
        }
////        else
////        {
////            CoeusOptionPane.showErrorDialog("Action "  + actionBean.getActionTypeDescription() + " failed") ;
////        }
    }
    
    
    
//prps start dec 12 2003
    
    
    
    private void showSummaryDetails() throws Exception{
        int selRow = tblProtocol.getSelectedRow();
        if( selRow != -1 ) {
//            String protocolNumber = (String)tblProtocol.getValueAt(selRow,0);
            // Get the protocol Number from the search result
            String protocolNumber = getSelectedValue(selRow,PROTOCOL_NUMBER);
            ProtocolSummaryForm summaryForm = new ProtocolSummaryForm(protocolNumber,true);
            summaryForm.setProtocolTable(tblProtocol);
            summaryForm.showForm();
        }
    }
    
    //Added for Coeus4.3 enhancement - PT ID:2210 - View Protocol History - start
    public void showProtocolHistory()throws Exception {
        int selRow = tblProtocol.getSelectedRow();
        if( selRow != -1 ) {
            // Get the protocol Number from the search result
            String protocolNumber = getSelectedValue(selRow,PROTOCOL_NUMBER);
            ProtocolHistoryController protHistoryController = new ProtocolHistoryController(protocolNumber);
            if(protHistoryController.isUserHasViewRight()){
                if(protHistoryController.getMaxSequenceNo()!=1){
                    protHistoryController.display();
                }else{
                    CoeusOptionPane.showWarningDialog(
                            coeusMessageResources.parseMessageKey("protocolHistoryExceptionCode.1001"));
                }
            }else{
                CoeusOptionPane.showWarningDialog(
                        coeusMessageResources.parseMessageKey("protocolHistoryExceptionCode.1002"));
            }
        }else{
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("protocolHistoryExceptionCode.1003"));
        }
    }
    //Added for Coeus4.3 enhancement - PT ID:2210 - View Protocol History - end
    
    /**
     * This method is invoked when the user clicks add in the Edit menu
     * of Protocol module
     */
    private void addProtocolDetails() throws Exception {
        checkDuplicateAndShow(CoeusGuiConstants.PROTOCOL_FRAME_TITLE,"", ADD_FUNCTION);
    }
    
    /**
     * This method is invoked when the user clicks Copy in the Edit menu
     * of Protocol module
     */
    private void copyProtocolDetails(String protocolNumber) throws Exception {
        if (protocolNumber == null) {
            log(coeusMessageResources.parseMessageKey(
                    "protoBaseWin_exceptionCode.1126"));
//Commented and Added for Case#Case#3940 In premium the copied protocol with the original protocols approval date - Start  
//             
//        } else {
//            //Code modified for coeus4.3 Amendments/Renewal enhancements
////            checkDuplicateAndShow(CoeusGuiConstants.PROTOCOL_FRAME_TITLE,
////            protocolNumber, COPY_FUNCTION);
//            checkDuplicateAndShow(CoeusGuiConstants.PROTOCOL_FRAME_TITLE,
//                    protocolNumber, 'P');
//        }            
          }else {
            if(protocolNumber.length() > 10){
                if(protocolNumber.charAt(10) == 'A' || protocolNumber.charAt(10) == 'R'){
                    CoeusOptionPane.showInfoDialog(
                            coeusMessageResources.parseMessageKey("protoBaseWin_exceptionCode.1000"));
                }                
            }else {                
                checkDuplicateAndShow(CoeusGuiConstants.PROTOCOL_FRAME_TITLE,
                        protocolNumber, 'P');
            }            
          }
//Commented and Added for Case#3940 In premium the copied protocol with the original protocols approval date  - End      
    }
    
    
    /**
     * This method is invoked when the user clicks New Amendment in the Edit menu
     * of Protocol module
     */
    private void createNewAmendment(String protocolNumber) throws Exception {
        if (protocolNumber == null) {
            log(coeusMessageResources.parseMessageKey(
                    "protoBaseWin_exceptionCode.1126"));
        } else {
            checkDuplicateAndShow(CoeusGuiConstants.NEW_AMENDMENT_TITLE,
                    protocolNumber, CoeusGuiConstants.NEW_AMENDMENT);
        }
    }
    
    /**
     * This method is invoked when the user clicks New Revision in the Edit menu
     * of Protocol module
     */
//    private void createProtocolRenewal(String protocolNumber) throws Exception {
//        if (protocolNumber == null) {
//            log(coeusMessageResources.parseMessageKey(
//                    "protoBaseWin_exceptionCode.1126"));
//        } else {
//            checkDuplicateAndShow(CoeusGuiConstants.RENEWAL_DETAILS_TITLE,
//                    protocolNumber, CoeusGuiConstants.RENEWAL);
//        }
//    }
//    
    /*Added for case#4278-start*/
    private void createProtocolRenewal(String protocolNumber, char amendmentOrRenewal) throws Exception	{
        if (protocolNumber == null){
            log(coeusMessageResources.parseMessageKey(
                    "protoBaseWin_exceptionCode.1126"));
        } else {            
            if (amendmentOrRenewal == CoeusGuiConstants.RENEWAL) {
                checkDuplicateAndShow(CoeusGuiConstants.RENEWAL_DETAILS_TITLE,
                        protocolNumber, amendmentOrRenewal);
            } else {
                //checking for the Renewal/Amendment
                checkDuplicateAndShow(CoeusGuiConstants.RENEWAL_WITH_AMENDMENT_TITLE,
                        protocolNumber, amendmentOrRenewal);
            }
        }
    }
	/*Added for case#4278-end*/
    
    
    /**
     * This method is used to check whether the given protocol number is already
     * opened in the given mode or not.
     */
    private void checkDuplicateAndShow(String moduleName, String refId, char mode)throws Exception {
        boolean duplicate = false;
        
        //Added by Vyjayanthi - 05/08/2004 for IRB Enhancement - Start
        String protocolNo = "", completeProtocolNo = "";
        if( refId != null && refId.length() > 0 ){
            protocolNo = refId.substring(0, 9);
            completeProtocolNo = new String(refId);
        }
        boolean adminCorrectionMode = false;
        //Added by Vyjayanthi - 05/08/2004 for IRB Enhancement - End
        
        try{
            refId = refId == null ? "" : refId;
            
            //Added by Vyjayanthi - 30/07/2004 for IRB Enhancement - Start
            // 1. To check if protocol window is already open if opened in
            // Administrative Corrections mode
            // 2. Open Amendments and Renewals from Protocol list - Case#678
            String version = "";
            if( refId.length() > 10 ){
                version = refId.substring(11);
            }
            if( moduleName.equalsIgnoreCase(CoeusGuiConstants.ADMINISTRATIVE_CORRECTIONS) ){
                //Selected Protocol is opened in Administrative Corrections mode
                moduleName = CoeusGuiConstants.PROTOCOL_FRAME_TITLE;
                adminCorrectionMode = true;
            }else if( refId.length() > 10 && refId.charAt(10) == 'A' ) {
                //Selected Protocol is an amendment
                moduleName = CoeusGuiConstants.AMENDMENT_DETAILS_TITLE;
                refId = protocolNo + " Version " + version;
            }else if( refId.length() > 10 && refId.charAt(10) == 'R' ){
                //Selected Protocol is a Renewal
                moduleName = CoeusGuiConstants.RENEWAL_DETAILS_TITLE;
                refId = protocolNo + " Version " + version;
            }
            //Added by Vyjayanthi - 30/07/2004 for IRB Enhancement - End
            
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
            //Added by Vyjayanthi - 09/08/2004 for IRB Enhancement - Start
            //To set back the module name after checking for duplicate frame
            if( adminCorrectionMode ) {
                moduleName = CoeusGuiConstants.ADMINISTRATIVE_CORRECTIONS;
            }
            //Added by Vyjayanthi - 09/08/2004 for IRB Enhancement - End
            
            if( moduleName.equalsIgnoreCase(CoeusGuiConstants.PROTOCOL_FRAME_TITLE) ) {
                
                //COEUSQA:3503 - Protocol Copying - Option to Copy Attachments and Questionnaires - Start
                // mainProtocol = new ProtocolDetailForm( mode, refId, mdiForm);
                if(mode == COPY_PROTOCOL){
                    int selectedRow = tblProtocol.getSelectedRow();
                    String seqNumber = getSelectedValue(selectedRow, SEQUENCE_NUMBER);
                    String copyQnr = getParameterValue(ENABLE_COPY_PROTO_QNR);
                    String copyAttachments = getParameterValue(ENABLE_COPY_PROTO_ATTACHMENT);
                    String copyOtherAttach = getParameterValue(ENABLE_COPY_PROTO_OTHER_ATTACHMENT);
                    if("1".equals(copyQnr) || "1".equals(copyAttachments) || "1".equals(copyOtherAttach)) {
                        CopyProtocolForm copyProtocolForm = new CopyProtocolForm(mdiForm,refId,mdiForm.getUserName(),seqNumber);
                        copyProtocolForm.showCopyProtocolForm();
                        copyProtocolForm.display();
                        if(copyProtocolForm.isCancel() == false){
                            if(copyProtocolForm.isCopyQnr()){
                                boolean canCopy = canCopyQuestionnaires(refId, seqNumber);
                                
                                if(!canCopy){
                                    int selectedOption = CoeusOptionPane.showQuestionDialog(
                                            coeusMessageResources.parseMessageKey("protocol_Copy_exceptionCode.1054"),
                                            CoeusOptionPane.OPTION_YES_NO,
                                            JOptionPane.YES_OPTION);
                                    
                                    if(selectedOption == JOptionPane.YES_OPTION){
                                        copyProtocolForm.setCopyQnr(false);
                                    }else if(selectedOption == JOptionPane.NO_OPTION){
                                        return;
                                    }
                                }
                            }
                            mainProtocol = new ProtocolDetailForm( mode, refId, mdiForm, copyProtocolForm.isCopyQnr(), copyProtocolForm.isCopyAttachments(), copyProtocolForm.isCopyOtherAttachments());
                            
                        }else {
                            return;
                        }
                    } else {
                        mainProtocol = new ProtocolDetailForm( mode, refId, mdiForm);
                    }
                } else {
                    mainProtocol = new ProtocolDetailForm( mode, refId, mdiForm);
                }
                //COEUSQA:3503 - End
                
            }else if( moduleName.equalsIgnoreCase(CoeusGuiConstants.NEW_AMENDMENT_TITLE) ){
                mainProtocol = new ProtocolDetailForm(refId,mode,
                        CoeusGuiConstants.PROTOCOL_AMENDMENT_CODE);
            }else if( moduleName.equalsIgnoreCase(CoeusGuiConstants.RENEWAL_DETAILS_TITLE) ){
                mainProtocol = new ProtocolDetailForm(completeProtocolNo, mode,
                        CoeusGuiConstants.PROTOCOL_RENEWAL_CODE);
            }
            //Added by Vyjayanthi - 30/07/2004 for IRB Enhancement - Start
            else if( moduleName.equalsIgnoreCase(CoeusGuiConstants.AMENDMENT_DETAILS_TITLE) ){
                mainProtocol = new ProtocolDetailForm(completeProtocolNo, mode,
                        CoeusGuiConstants.PROTOCOL_AMENDMENT_CODE);
            }else if( moduleName.equalsIgnoreCase(CoeusGuiConstants.ADMINISTRATIVE_CORRECTIONS) ){
                mainProtocol = new ProtocolDetailForm(
                        CoeusGuiConstants.ADMINISTRATIVE_CORRECTIONS, mode, refId, mdiForm);
                mainProtocol.setProtocolData(vecProtocolData);
            }
            //Added for the case#4278-In Comments on Action Details, indicate Renewal withAmendment for submissions created via New Am -start
            else if( moduleName.equalsIgnoreCase(CoeusGuiConstants.RENEWAL_WITH_AMENDMENT_TITLE) ){
                mainProtocol = new ProtocolDetailForm(completeProtocolNo, mode,
                        CoeusGuiConstants.PROTOCOL_RENEWAL_CODE);
            }
            //Added for the case#4278-In Comments on Action Details, indicate Renewal withAmendment for submissions created via New Am -end
            //Added by Vyjayanthi - 30/07/2004 for IRB Enhancement - End
            
            int selRow = tblProtocol.getSelectedRow();
            //commented as actions can happen even in display mode and base table
            // should be updated accordingly
//            if(  mode != DISPLAY_FUNCTION ) {
            mainProtocol.registerObserver( this );
            if( selRow != -1 ) {
                //Bug Fix:1359 Start 3
                //baseTableRow = Integer.parseInt((String)tblProtocol.getValueAt(
                //selRow,tblProtocol.getColumnCount()-1));
                baseTableRow = selRow;
                //Bug Fix:1359 End 3
            }
//            }
            mainProtocol.showDialogForm();
        }catch(CoeusException ex){
            if(ex.getMessage().equals(CoeusGuiConstants.DO_NOT_SHOW_FORM)) return;
            CoeusOptionPane.showDialog(new CoeusClientException(ex));
        }catch ( Exception ex) {
            
            ex.printStackTrace();
            System.out.println("exception Messages:"+ex.getMessage());
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
                checkDuplicateAndShow(CoeusGuiConstants.PROTOCOL_FRAME_TITLE,
                        protocolNumber,MODIFY_FUNCTION);
            } //prps added this line
        }
    }
    
    //Added by Vyjayanthi - 30/07/2004 for IRB Enhancement
    /** Called in Administrative Corrections mode
     * @mode holds the mode to open Protocol
     * @protocolNumber holds the protocol number
     */
    private void modifyProtocolDetails(String mode, String protocolNumber) throws Exception{
        if (protocolNumber == null) {
            log(coeusMessageResources.parseMessageKey(
                    "protoBaseWin_exceptionCode.1051"));
        } else {
            if( canPerformCorrection(protocolNumber) ){
                checkDuplicateAndShow(mode, protocolNumber, MODIFY_FUNCTION);
            }
        }
    }
    
    //Added by Vyjayanthi - 02/08/2004 for IRB Enhancement
    /** Checks if the protocol can be opened for Administrative Correction
     * @param protocolNumber holds the protocol number
     * @return canModify holds true if protocol can be opened, false otherwise
     */
    private boolean canPerformCorrection(String protocolNumber) {
        boolean canModify = false;
        String connectTo = CoeusGuiConstants.CONNECTION_URL+ PROTOCOL_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(ADMINISTRATIVE_CORRECTION);
        request.setId(protocolNumber);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        canModify = response.isSuccessfulResponse() ;
        if( canModify ) {
            canModify = true ;
            this.vecProtocolData = response.getDataObjects();
        }else {
            if(response.getDataObject() != null){
                Object obj = response.getDataObject();
                if(obj instanceof CoeusException){
                    CoeusOptionPane.showDialog(new CoeusClientException((CoeusException)obj));
                }
            }else{
                CoeusOptionPane.showErrorDialog(response.getMessage()) ;
                canModify = false ;
            }
        }
        return canModify;
    }
    
    // prps added this new constant
    private final String PROTOCOL_SERVLET = "/protocolMntServlet";
    
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
        System.out.println("** Server side validation for edit returned with " + statusOk) ;
        if (statusOk == true) {
            // continue with row lock check
            statusOk = true ;
        } else {
            //added by manoj to display the authorization message as info message
            if(response.getDataObject() != null){
                Object obj = response.getDataObject();
                if(obj instanceof CoeusException){
                    CoeusOptionPane.showDialog(new CoeusClientException((CoeusException)obj));
                }
            }else{
                CoeusOptionPane.showErrorDialog(response.getMessage()) ;
                statusOk = false ;
                
                //////          int choice=  CoeusOptionPane.showQuestionDialog(response.getMessage(), CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES)  ;
                //////          if (choice == CoeusOptionPane.SELECTION_YES)
                //////          {   // show the screen in display mode
                //////             try
                //////             {
                //////              mainProtocol = new ProtocolDetailForm( DISPLAY_FUNCTION, protocolNumber, mdiForm);
                //////              mainProtocol.showDialogForm();
                //////              mainProtocol.setProtocolsheet(tblProtocol);
                //////              statusOk = false ; // returning false bcoz if user likes see the screen in display mode there
                //////                              // there is no need to do Row Lock check
                //////             }catch(Exception ex)
                //////             {  ex.printStackTrace() ; }
                //////
                //////          }
                //////          else
                //////          {
                //////             statusOk = false ; // if user doesnt like to view the screen in display mode then no need to go further and
                //////                           // check for row lock
                //////          }
                
            }
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
                mdiForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
                checkDuplicateAndShow(CoeusGuiConstants.PROTOCOL_FRAME_TITLE,protocolNumber,DISPLAY_FUNCTION);
                mdiForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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
    private void closeProtocolBaseWindow() {
        this.doDefaultCloseAction();
    }
    
    /**
     * display alert message
     *
     * @param mesg the message to be displayed
     */
    private void log(String mesg) {
        CoeusOptionPane.showWarningDialog(mesg);
    }
    
    /** This method is called from Save Menu Item under File Menu.
     * Implemented the abstract class declared in parent(<CODE>CoeusInternalFrame</CODE>).
     * Empty body since the Save operation is not required for this list screen.
     */
    public void saveActiveSheet() {
    }
    
    /** Once a new protocol is created and while updating the List window,the column
     *name is taken based on the property it had.
     *Add all the data and sort according to the key(column) and send the data back
     *@param SearchColumnIndex specifies display column data, protocolBean which
     *contains the updated data
     */
    
    private Vector buildNewProtocolData(edu.mit.coeus.utils.SearchColumnIndex searchColumnIndex,
            ProtocolInfoBean protocolBean){
        DateUtils dtUtils = new edu.mit.coeus.utils.DateUtils();
        Vector data = new Vector();
        String applDate = "";
        String apprDate = "";
        String expDate = "";
        getPIDetails(protocolBean.getInvestigators());
        if(protocolBean.getApplicationDate() != null){
            applDate = dtUtils.formatDate(
                    protocolBean.getApplicationDate().toString(),"dd-MMM-yyyy");
        }
        if(protocolBean.getApprovalDate() != null){
            apprDate = dtUtils.formatDate(
                    protocolBean.getApprovalDate().toString(),"dd-MMM-yyyy");
        }
        if(protocolBean.getExpirationDate() != null){
            expDate = dtUtils.formatDate(
                    protocolBean.getExpirationDate().toString(),"dd-MMM-yyyy");
        }
        
        java.util.WeakHashMap mapData = new java.util.WeakHashMap();
        mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
                coeusSearch,PROTOCOL_NUMBER)), protocolBean.getProtocolNumber());
        mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
                coeusSearch,"PROTOCOL_TYPE_DESCRIPTION")), protocolBean.getProtocolTypeDesc());
        mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
                coeusSearch,"PROTOCOL_STATUS_DESCRIPTION")), protocolBean.getProtocolStatusDesc());
        mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
                coeusSearch,"TITLE")), protocolBean.getTitle());
        mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
                coeusSearch,"DESCRIPTION")), protocolBean.getDescription());
        mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
                coeusSearch,"APPLICATION_DATE")), applDate);
        mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
                coeusSearch,"APPROVAL_DATE")), apprDate);
        mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
                coeusSearch,"EXPIRATION_DATE")), expDate);
        mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
                coeusSearch,"PERSON_NAME")),PIName );
        mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
                coeusSearch,"UNIT_NUMBER")),leadUnitNumber );
        mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
                coeusSearch,"UNIT_NAME")),leadUnitName );
        mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
                coeusSearch,"SEQUENCE_NUMBER")),""+protocolBean.getSequenceNumber());
        
        java.util.ArrayList keySet = new java.util.ArrayList(mapData.keySet());
        java.util.Collections.sort(keySet);
        java.util.Iterator iterator = keySet.iterator();
        while(iterator.hasNext()){
            data.add(mapData.get(iterator.next()));
        }
        return data;
    }
    
    public void update(Observable observable, Object obj) {
        if( obj instanceof ProtocolInfoBean ) {
            //Code added for PT ID#3243
            String dateFormat = CoeusServerProperties.getProperty(CoeusPropertyKeys.SEARCH_DATE_FORMAT, SIMPLE_DATE_FORMAT);
            edu.mit.coeus.utils.SearchColumnIndex searchColumnIndex = new edu.mit.coeus.utils.SearchColumnIndex();
            DateUtils dtUtils = new edu.mit.coeus.utils.DateUtils();
            ProtocolInfoBean protocolBean =  (ProtocolInfoBean) obj;
            String applDate = "";
            String apprDate = "";
            String expDate = "";
            //Code modified for PT ID#3243 - start
            if(protocolBean.getApplicationDate() != null){
                //COEUSQA-1477 Dates in Search Results - Start
                applDate = dtUtils.parseDateForSearchResults(protocolBean.getApplicationDate().toString(), dateFormat);
                //applDate = dtUtils.formatDate(
                //        protocolBean.getApplicationDate().toString(), dateFormat);
                //COEUSQA-1477 Dates in Search Results - End
            }
            if(protocolBean.getApprovalDate() != null){
                //COEUSQA-1477 Dates in Search Results - Start
                apprDate = dtUtils.parseDateForSearchResults(protocolBean.getApprovalDate().toString(), dateFormat);
                //apprDate = dtUtils.formatDate(
                //        protocolBean.getApprovalDate().toString(), dateFormat);
                //COEUSQA-1477 Dates in Search Results - End
            }
            if(protocolBean.getExpirationDate() != null){
                //COEUSQA-1477 Dates in Search Results - Start
                expDate = dtUtils.parseDateForSearchResults(protocolBean.getExpirationDate().toString(), dateFormat);
                //expDate = dtUtils.formatDate(
                //        protocolBean.getExpirationDate().toString(), dateFormat);
                //COEUSQA-1477 Dates in Search Results - End
            }
            //Code modified for PT ID#3243 - end
            Vector protocolRow = buildNewProtocolData(searchColumnIndex, protocolBean);
            
            if (((BaseWindowObservable)observable).getFunctionType() == 'A'
                    || mainProtocol.getFunctionType() ==  COPY_FUNCTION ){
                //for index which came from search
                int lastRow = tblProtocol.getRowCount(); //tblProtocolsheet.getRowCount() - 1 ;
                protocolRow.addElement(""+lastRow);
                if( lastRow >= 0 ) {
                    ((DefaultTableModel)tblProtocol.getModel()).insertRow(lastRow,protocolRow);
                    // Bug fix #1606 - start
                    ((DefaultTableModel)tblProtocol.getModel()).fireTableRowsInserted(lastRow,lastRow);
                    String protoNumber = protocolBean.getProtocolNumber();
                    try{
                        for(int index = 0; index<tblProtocol.getRowCount();index++ ){
                            //                        String cmpProtoNumber = (String)tblProtocol.getValueAt(index, 0);
                            String cmpProtoNumber = getSelectedValue(index, PROTOCOL_NUMBER);
                            if(protoNumber.equals(cmpProtoNumber)){
                                tblProtocol.setRowSelectionInterval(index,index);
                                lastRow = index;
                                break;
                            }
                        }
                    }catch (Exception ex){
                        CoeusOptionPane.showErrorDialog(ex.getMessage());
                    }
                    // Bug fix #1606 - End
                }else{
                    ((DefaultTableModel)tblProtocol.getModel()).insertRow(0,protocolRow);
                    // Bug fix #1606 - Start
                    tblProtocol.setRowSelectionInterval(0,0);
                    // Bug fix #1606 - End
                    
                }
                baseTableRow = lastRow; //tblProtocolsheet.getSelectedRow();
            //Code commented for PT ID#3243, setting of new values back to list window made mandatory even in ADD mode
            }//else if ( ((BaseWindowObservable)observable).getFunctionType() == 'M'  ){
                // Set the data based on property column index
                ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                        protocolBean.getProtocolNumber(),baseTableRow,searchColumnIndex.getSearchColumnIndex(coeusSearch,PROTOCOL_NUMBER));
                ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                        protocolBean.getProtocolTypeDesc(),baseTableRow,searchColumnIndex.getSearchColumnIndex(coeusSearch,"PROTOCOL_TYPE_DESCRIPTION"));
                ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                        protocolBean.getProtocolStatusDesc(),baseTableRow,searchColumnIndex.getSearchColumnIndex(coeusSearch,"PROTOCOL_STATUS_DESCRIPTION"));
                ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                        protocolBean.getTitle(),baseTableRow,searchColumnIndex.getSearchColumnIndex(coeusSearch,"TITLE"));
                ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                        protocolBean.getDescription(),baseTableRow,searchColumnIndex.getSearchColumnIndex(coeusSearch,"DESCRIPTION"));
                ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                        applDate,baseTableRow,searchColumnIndex.getSearchColumnIndex(coeusSearch,"APPLICATION_DATE"));
                ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                        apprDate,baseTableRow,searchColumnIndex.getSearchColumnIndex(coeusSearch,"APPROVAL_DATE"));
                ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                        expDate,baseTableRow,searchColumnIndex.getSearchColumnIndex(coeusSearch,"EXPIRATION_DATE"));
                //get the PI and lead unit name/number
                ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                        PIName,baseTableRow,searchColumnIndex.getSearchColumnIndex(coeusSearch,"PERSON_NAME"));
                ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                        leadUnitNumber,baseTableRow,searchColumnIndex.getSearchColumnIndex(coeusSearch,"UNIT_NUMBER"));
                ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                        leadUnitName,baseTableRow,searchColumnIndex.getSearchColumnIndex(coeusSearch,"UNIT_NAME"));
                ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                        ""+protocolBean.getSequenceNumber(),baseTableRow,searchColumnIndex.getSearchColumnIndex(coeusSearch,"SEQUENCE_NUMBER"));
                // sorter will select the row after modification
                //tblProtocolsheet.setRowSelectionInterval(rowNum,rowNum);
                //baseTableRow = tblProtocolsheet.getSelectedRow();
                //int selRow = tblProtocol.getSelectedRow();
                
                // Bug fix #1606
                /*if(  selRow != -1 ) {
                    //Bug Fix:1359 Start 4
                    //baseTableRow = Integer.parseInt((String)tblProtocol.getValueAt(
                    //selRow,tblProtocol.getColumnCount()-1));
                    //baseTableRow = selRow;
                    //Bug Fix:1359 End 4
                }*/
                tblProtocol.setColumnSelectionInterval(0,0);
                tblProtocol.setRowSelectionInterval(baseTableRow,baseTableRow);
                // Bug fix #1606
                
            //}
        }else if ( obj instanceof ProtocolActionChangesBean ) {
            updateActionStatus((ProtocolActionChangesBean)obj);
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
        PIName=leadUnitNumber=leadUnitName="";
        // Modified for COEUSQA-3820 : Coeus 4.5.1: Protocol Routing Issue - Record Close Error - Start
        // investigator null check and size check is done
        if(investigators != null && !investigators.isEmpty()){
            int count = investigators.size();
            for (int i=0; i<count;i++) {
                ProtocolInvestigatorsBean investigatorBean
                        = (ProtocolInvestigatorsBean) investigators.elementAt(i);
                if (investigatorBean.isPrincipalInvestigatorFlag()) {
                    PIName = investigatorBean.getPersonName();
                    Vector investigatorUnits
                            = investigatorBean.getInvestigatorUnits();
                    int cnt = investigatorUnits.size();
                    for (int j=0; i<cnt;i++) {
                        ProtocolInvestigatorUnitsBean unitsBean
                                = (ProtocolInvestigatorUnitsBean)
                                investigatorUnits.elementAt(j);
                        if (unitsBean.isLeadUnitFlag()){
                            leadUnitName = unitsBean.getUnitName();
                            leadUnitNumber = unitsBean.getUnitNumber();
                        }
                    }
                    break;
                }
            }
        }
        // Modified for COEUSQA-3820 : Coeus 4.5.1: Protocol Routing Issue - Record Close Error - End
    }
    
    //Added by sharath - 19/10/2003 for Save As ToolBar Button - Start
    /**
     * This method is called from SaveAs Toolbar Menu Item.
     */
    public void showSaveAsDialog(){
        SaveAsDialog saveAsDialog = new SaveAsDialog(tblProtocol);
    }
    //Added by sharath - 19/10/2003 for Save As ToolBar Button - End
    
    private void updateProtocolActionData(ProtocolActionChangesBean actionChangesBean, int baseTableRow ){
        SearchColumnIndex searchColumnIndex = new SearchColumnIndex();
        ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                actionChangesBean.getProtocolNumber(),baseTableRow,searchColumnIndex.getSearchColumnIndex(coeusSearch,PROTOCOL_NUMBER));
        if(actionChangesBean.getProtocolStatusDescription()!= null){
            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                    actionChangesBean.getProtocolStatusDescription(),baseTableRow,searchColumnIndex.getSearchColumnIndex(coeusSearch,"PROTOCOL_STATUS_DESCRIPTION"));
        }
        ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
                ""+actionChangesBean.getSequenceNumber(),baseTableRow,searchColumnIndex.getSearchColumnIndex(coeusSearch,"SEQUENCE_NUMBER"));
        ((DefaultTableModel)tblProtocol.getModel()).fireTableRowsUpdated(baseTableRow, baseTableRow);
    }
    private void updateActionStatus(ProtocolActionChangesBean actionChangesBean ){
        if( actionChangesBean != null ) {
            updateProtocolActionData(actionChangesBean, baseTableRow);
//            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
//            actionChangesBean.getProtocolNumber(),baseTableRow,0);
//            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
//            actionChangesBean.getProtocolStatusDescription(),baseTableRow,2);
//            /*((DefaultTableModel)tblProtocol.getModel()).setValueAt(
//            apprDate,baseTableRow,6);
//            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
//            expDate,baseTableRow,7);*/
//            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
//            ""+actionChangesBean.getSequenceNumber(),baseTableRow,11);
            int selRow = tblProtocol.getSelectedRow();
            if(  selRow != -1 ) {
                //Bug Fix:1359 Start 5
                //baseTableRow = Integer.parseInt((String)tblProtocol.getValueAt(
                //selRow,tblProtocol.getColumnCount()-1));
                baseTableRow = selRow;
                //Bug Fix:1359 End 5
            }
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
    class ProtocolMouseAdapter extends MouseAdapter{
        public void mouseClicked(MouseEvent mouseEvent){
            JTableHeader tblHeader = (JTableHeader) mouseEvent.getSource();
            TableColumnModel columnModel = tblHeader.getColumnModel();
            int viewColumn = columnModel.getColumnIndexAtX(mouseEvent.getX());
            int column = columnModel.getColumn(viewColumn).getModelIndex();
            int sortStatus = getStatus();
            if(oldCol != column )
                sortStatus = MultipleTableColumnSorter.NOT_SORTED;
            sortStatus = sortStatus + (mouseEvent.isShiftDown() ? -1 : 1);
            sortStatus = (sortStatus + 4) % 3 - 1;
            setStatus(sortStatus);
            oldCol = column;
            if(getStatus()==MultipleTableColumnSorter.ASCENDING || getStatus() == MultipleTableColumnSorter.DESCENDING) {
                Vector newSortedData = new Vector();
                newSortedData.addElement(tblProtocol.getColumnName(column));
                newSortedData.addElement(new Integer(column));
                newSortedData.addElement(new Boolean(status == 1 ? true : false));
                if(vecSortedData == null)
                    vecSortedData = new Vector();
                vecSortedData.removeAllElements();
                vecSortedData.addElement(newSortedData);
            }else {
                vecSortedData = null;
            }
            
        }
    }// Added by Nadh - End
    
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
    /** Case 2477
     */
    public void mouseClicked(MouseEvent me) {
        if (me.getClickCount() == 2) {
            try {
                int selectedRow = tblProtocol.getSelectedRow();
                String protocolNumber = getSelectedValue(selectedRow,PROTOCOL_NUMBER);
                mdiForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
                showProtocolDetails(protocolNumber);
                mdiForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                
            } catch (Exception e) {
                e.printStackTrace();
                //                                CoeusOptionPane.showErrorDialog(e.getMessage());
            }
        }
    }/** Case 2477
     */
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    public void mousePressed(MouseEvent e) {
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    /*Added for Case# 3018 -create ability to delete pending studies - Start*/
    
    /** 
     * This method is used to perform Delete on the given protocol number
     * @param String protocolNumber, int selectedRow
     * @throws Exception
     */
    
    private void performDelete(String protocolNumber, int selectedRow)throws Exception{
        if (protocolNumber == null) {
            log(coeusMessageResources.parseMessageKey(
                    "deleteProtocol_exceptionCode.1007"));
        } else {
            getDeleteRights(protocolNumber);
            if(isAuthorised){
                int opt = CoeusOptionPane.DEFAULT_NO;
                //Modified for Case# 3781_Rename Delete Protocol - Start
                if(protocolNumber.length() >10 ){
                    if(protocolNumber.lastIndexOf('A') != -1){
                        opt = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey("deleteProtocol_exceptionCode.1008"),
                        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                    }else if(protocolNumber.lastIndexOf('R') != -1){
                       opt = CoeusOptionPane.showQuestionDialog(
                       coeusMessageResources.parseMessageKey("deleteProtocol_exceptionCode.1009"),
                        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES); 
                    }
                }else{
                    opt = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey("deleteProtocol_exceptionCode.1004"),
                        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                }
                //Modified for Case# 3781_Rename Delete Protocol - End
                if( opt == CoeusOptionPane.SELECTION_YES ) {
                    deleteProtocol(protocolNumber,selectedRow);
                }
            }else{
                CoeusOptionPane.showWarningDialog(
                        coeusMessageResources.parseMessageKey("deleteProtocol_exceptionCode.1003"));
            }
        }
    }
    
    
    /**
     * This method is used for deleting the protocol,
     * if the protocol is not deleted, then an error message is thrown
     * if the protocol is successfully deleted, then the protocol list table is refreshed
     * @param  String protocolNumber,int selectedRow 
     * @throws Exception 
     */
    private void deleteProtocol(String protocolNumber,int selectedRow)throws Exception{
        boolean deleteProtocol = false;
        String connectTo = CoeusGuiConstants.CONNECTION_URL+ PROTOCOL_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(DELETE_PROTOCOL);
        request.setId(protocolNumber);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            if (response.isSuccessfulResponse()){
                deleteProtocol = ((Boolean)response.getDataObject()).booleanValue();
            }
            if(!deleteProtocol){
                String errMsg = response.getMessage();
                if(errMsg != null){
                    // Case# 3018:Delete Pending Studies - For Displaying Locking Message as Warning
//                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(errMsg));
//                    CoeusOptionPane.showWarningDialog(errMsg); 
                    CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(errMsg));
                }
            }else{
                ((DefaultTableModel)tblProtocol.getModel()).removeRow(selectedRow);
                int rowCount = tblProtocol.getRowCount();
                ((DefaultTableModel)tblProtocol.getModel()).fireTableRowsDeleted( 0, rowCount);
                if(rowCount > 1){
                    tblProtocol.setRowSelectionInterval(0,0);
                }
            }
        }
    }
    
    /**
     * To get the delete rights of the user for the given protocol number
     * @param String protocolNumber
     * @throws Exception 
     */
    private void getDeleteRights(String protocolNumber) throws Exception{
         
        String connectTo = CoeusGuiConstants.CONNECTION_URL+ PROTOCOL_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(GET_DELETE_RIGHTS);
        request.setId(protocolNumber);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response!= null){
            isAuthorised = ((Boolean)response.getDataObject()).booleanValue();
        }
    }
    /*Added for Case# 3018 -create ability to delete pending studies - End*/
    
    //COEUSQA:2653 - Add Protocols to Medusa - Start
    /**
     * Method to open the Medusa screen for IRB protocol Number
     * @param protocolNumber
     */
    private void  showMedusaWindow(String protocolNumber){
        try{
            ProposalAwardHierarchyLinkBean linkBean;
            MedusaDetailForm medusaDetailform;
            
            linkBean = new ProposalAwardHierarchyLinkBean();
            linkBean.setIrbProtocolNumber(protocolNumber);
            linkBean.setBaseType(CoeusConstants.IRB_PROTOCOL);
            if( ( medusaDetailform = (MedusaDetailForm)mdiForm.getFrame(
                    CoeusGuiConstants.MEDUSA_BASE_FRAME_TITLE))!= null ){
                if( medusaDetailform.isIcon() ){
                    medusaDetailform.setIcon(false);
                }
                medusaDetailform.setSelectedNodeId(protocolNumber);
                medusaDetailform.setSelected( true );
                return;
            }
            medusaDetailform = new MedusaDetailForm(mdiForm,linkBean);
            medusaDetailform.setVisible(true);
            
            
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    //COEUSQA:2653 - End
    
   //COEUSQA:3503 - Protocol Copying - Option to Copy Attachments and Questionnaires - Start
   /**
   * Method to get the value for the parameter 
   * @return value
   */ 
   public String getParameterValue(String parameter){
        final String connectTo = CoeusGuiConstants.CONNECTION_URL+ "/coeusFunctionsServlet";
        String value = CoeusGuiConstants.EMPTY_STRING;
        RequesterBean requester = new RequesterBean();
        ResponderBean responder = null;
        requester.setDataObject(GET_PARAMETER_VALUE);
        Vector vecParameter = new Vector();
        vecParameter.add(parameter);
        requester.setDataObjects(vecParameter);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            value =(String) responder.getDataObject();
        }
        return value;
    }      
    
    /**
     * Method to check whether the questionnaires can be copied to protocol
     *
     * @param protocolNumber 
     * @param sequenceNumber 
     * @return 
     */
    private boolean canCopyQuestionnaires(String protocolNumber, String sequenceNumber) {
        Boolean canCopy = false;
        Vector moduleData = new Vector();
        moduleData.add(ModuleConstants.PROTOCOL_MODULE_CODE);
        moduleData.add(0);
        moduleData.add(protocolNumber);
        moduleData.add(sequenceNumber);
        
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(CHECK_CAN_COPY_QUESTIONNAIRE);
        requester.setDataObjects(moduleData);
        edu.mit.coeus.utils.AppletServletCommunicator comm
                = new edu.mit.coeus.utils.AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + QUESTIONNAIRE_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( response.isSuccessfulResponse() ){
            canCopy = (Boolean)response.getDataObject();
        }
        
        return canCopy;
    }
   
   //COEUSQA:3503 - End
}
