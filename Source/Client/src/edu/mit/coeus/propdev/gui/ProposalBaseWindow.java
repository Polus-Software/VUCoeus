/*
 * @(#)ProposalBaseWindow.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.propdev.gui;


import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.s2s.bean.OpportunityInfoBean;
import edu.mit.coeus.s2s.controller.OpportunitySearchController;
import edu.mit.coeus.search.gui.ProposalSearch;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.departmental.gui.*;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalInvestigatorFormBean;
import edu.mit.coeus.utils.saveas.SaveAsDialog;
import edu.mit.coeus.propdev.gui.ProposalNotepadForm;   //Added by Vyjayanthi
import edu.mit.coeus.propdev.gui.MedusaDetailForm;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.propdev.bean.ProposalAwardHierarchyLinkBean;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.budget.controller.SelectBudgetController;
import edu.mit.coeus.gui.menu.CoeusFileMenu;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import java.util.Observable;
import java.util.Observer;
import javax.swing.table.DefaultTableModel;
import edu.mit.coeus.utils.BaseWindowObservable;
import java.util.HashMap;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.event.InternalFrameEvent;


// JM 11-19-2012 get parameters
import edu.vanderbilt.coeus.utils.CustomFunctions;
// JM END


/** This class is used to construct the parent window of Proposal development 
 * and associate its menu and toolbar with listener. This window will display 
 * the Proposals from which the user can select the proposal details.
 *
 * @author Ravikanth
 * @version 1.0 March 16, 2003, 3:27 PM
 */

public class ProposalBaseWindow extends CoeusInternalFrame 
    implements ActionListener, TypeConstants, LookUpWindowConstants, Observer {

    // Menu items for proposal 
    private CoeusMenuItem proposalCopy, proposalNew, proposalModify, proposalDisplay
    ,proposalSearch, proposalNotepad, proposalViewer, proposalAdminDetails, proposalMedusa,
    proposalBudget, proposalNarrative,sort,create,join,remove, newProposalFromGG,
    /*Added for Case# COEUSQA-1675 -ability to delete Proposal Development proposals - Start*/
    proposalDelete;
    /*Added for Case# COEUSQA-1675 -ability to delete Proposal Development proposals - End*/
    
     /*
     * to indicate horizondal seperator in menu items
     */
    private final String SEPERATOR="seperator";
    
    
    
    //Stores the Role ID which is required for checking user rights to create new proposal
   // private final int ROLE=7; -  Case Id 1856 and 1860
      
    //Stores the RIGHT ID which is required for checking user rights to create new proposal
    private final String CREATE_RIGHT="CREATE_PROPOSAL";
    
    //Stores the RIGHT ID which is required for checking user rights to modify proposal
    private final String MODIFY_RIGHT="MODIFY_PROPOSAL";
    //Added by shiji for Right Checking - step 1 : start
    private final String VIEW_ANY_PROPOSAL_RIGHT="VIEW_ANY_PROPOSAL";
    
    //Added for bug id 1856 step 1 : start
    private final String MODIFY_NARRATIVE_RIGHT="MODIFY_NARRATIVE";
    
    private final String MODIFY_BUDGET_RIGHT="MODIFY_BUDGET";
    //bug id 1856 step 1 : end
    
    private final String MODIFY_ANY_PROPOSAL_RIGHT="MODIFY_ANY_PROPOSAL";
    //Right Checking - step 1 : end
    //Stores the RIGHT ID which is required for checking user rights to view proposal
    private final String VIEW_RIGHT="VIEW_PROPOSAL";
    
    private final String USER_HAS_PROPOSAL_ROLE ="USER_HAS_PROP_ROLE";
    
    private final String USER_HAS_OSP_RIGHT ="USER_HAS_OSP_RIGHT";
    
    private final String APPROVE_PROPOSAL_RIGHT ="APPROVE_PROPOSAL";
    
    //Added for Case#3587 - multicampus enhancement  - Start
    private final String USER_HAS_DEPARTMENTAL_RIGHT = "USER_HAS_DEPARTMENTAL_RIGHT";
    //Case#3587 - End
    
    private final int PROPOSAL_ROLE = 101;

    // Toolbar for Proposal
    private CoeusToolBarButton btnNewProposal;
    private CoeusToolBarButton btnModifyProposal;
    private CoeusToolBarButton btnDisplayProposal;
    private CoeusToolBarButton btnCopyProposal;
    
    private CoeusToolBarButton btnNotepad;
    private CoeusToolBarButton btnMedusa;
    private CoeusToolBarButton btnSort;
    
    private CoeusToolBarButton btnSearchProposal;
    private CoeusToolBarButton btnSaveAsProposal;
    private CoeusToolBarButton btnCloseProposal;

    /*Added for Case# COEUSQA-1675 -ability to delete Proposal Development proposals - Start*/
    private CoeusToolBarButton btnDeleteProposal;
    /*Added for Case# COEUSQA-1675 -ability to delete Proposal Development proposals - End*/
    private String windowName;

    // holds the instance of ProposalSearch
    private ProposalSearch searchWindow;

    //Main MDI Form.
    private CoeusAppletMDIForm mdiForm = null;

    // Jtable
    private JTable tblProposal;
    // Scroll bar pane
    private JScrollPane scrPnSearchRes;

    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    private ProposalDetailForm mainProposal;
    
    //holds lead Unit Number required while creating New Proposal
    private String unitNumber;
    
    //holds lead Unit Name required while creating New Proposal
    private String unitName;
    
//    //holds the count of number of Proposal sheets opened in Display mode
//    public static int displaySheetCount = 0;
    private int baseTableRow;
    private String PIName="";
    
    // holds the proposal status
    private Vector vecProposalStatus;
    
    //holds the selected row's proposal number
    private String proposalNumber;  //Added by Vyjayanthi to open budget
    
    //holds an instance of the QueryEngine
    private QueryEngine queryEngine;    //Added by Vyjayanthi to open budget
    
    private static final char PROPOSAL_STATUS = 'o' ;
    private static final String PROPOSAL_SERVLET = "/ProposalMaintenanceServlet";
    private static final String connectTo = CoeusGuiConstants.CONNECTION_URL + PROPOSAL_SERVLET;
    private static final String HIERARCHY_SERVLET = "/ProposalHierarchyServlet";
    private static final char CAN_BUILD_HIERARCHY = 'D'; 
    private static final char CREATE_PARENT_PROPOSAL = 'E';
    private static final String CREATE_ACTION = "create";
    
    /*Added for Case# COEUSQA-1675 -ability to delete Proposal Development proposals - Start*/
    private final char DELETE_PROPOSAL = 'i';
    private final char GET_DELETE_RIGHTS = 'l';
    private boolean isAuthorised = false;
    //COEUSQA-2548_Delete Proposal action issue with unlinked hierarchy proposal requested from same Proposal List window_Start
    private String isPropInHierarchy = "N";
    //COEUSQA-2548_Delete Proposal action issue with unlinked hierarchy proposal requested from same Proposal List window_end
    /*Added for Case# COEUSQA-1675 -ability to delete Proposal Development proposals - End*/
    
    private ProposalNarrativeForm narrativeForm;
    
    //holds sorted columns and its states
    private Vector vecSortedData;
    
    private static final int OK_CLICKED = 0;
    
    //holds column index
    private int oldCol = -1;
    
    //holds column status
    private int status=MultipleTableColumnSorter.NOT_SORTED;
    
     //Added for bug fixed for Case#2302 start 1
    private JMenu fileMenu;
    //Added for bug fixed for Case#2302 end 1
    
    //Added for case#3243
    private static final String SIMPLE_DATE_FORMAT = "yyyy/MM/dd"; 
    
    //Added for COEUSQA-1579 : For Hierarchy Proposal, Approval in Progress, cannot sync after narratives updated on child proposal - Start
    private static final char PROPOSAL_CREATION_STATUS_CODE = 'h';
    //COEUSQA-1579 : End
    
    // JM 11-16-2012 need copy parameters
    private static final String DISALLOW_PROP_COPY_STATUS = "DISALLOW_PROP_COPY_STATUS";
    // JM END
    
    /** Constructor to create new <CODE>ProposalBaseWindow</CODE>
     *
     * @param mdiForm reference to <CODE>CoeusAppletMDIForm</CODE>.
     */
    public ProposalBaseWindow(CoeusAppletMDIForm mdiForm) {
        super(CoeusGuiConstants.PROPOSAL_BASE_FRAME_TITLE, mdiForm,LIST_MODE);
        this.mdiForm = mdiForm;
        try {
            initComponents();
            mdiForm.putFrame(CoeusGuiConstants.PROPOSAL_BASE_FRAME_TITLE, this);
            mdiForm.getDeskTopPane().add(this);
            this.setSelected(true);
            this.setVisible(true);
            formatFields();
            showProposalSearchWindow();
            
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
          //Commented for bug fixed for Case#2302 start 2
       // getProposalFileMenu();
        //Commented for bug fixed for Case#2302 start 2
        setFrameMenu(getProposalEditMenu());
        setToolsMenu(getProposalToolsMenu());
        setFrameToolBar(getProposalToolBar());
//        insertPrintMenu();
        setFrame(CoeusGuiConstants.PROPOSAL_BASE_FRAME_TITLE);
        setFrameIcon(mdiForm.getCoeusIcon());
        createProposalInfo();
        coeusMessageResources = CoeusMessageResources.getInstance();
    }
    /**
     *  For adding a new menu item for print in File Menu.
     *  Added by geo on 02-Sep-2004
     */
//    private void insertPrintMenu(){
//        printProposal = new CoeusMenuItem("Print",null,true,true);
//        printProposal.setMnemonic('P');
//        mdiForm.getFileMenu().insert(printProposal, 6);
//        printProposal.addActionListener(this);
//    }
    /**
     * This methods the empty Proposal table for the base window
     *
     */
    private void createProposalInfo() throws Exception {
        searchWindow = new ProposalSearch(mdiForm, CoeusGuiConstants.PROPOSAL_SEARCH, 0);
        tblProposal = searchWindow.getEmptyResTable();
        javax.swing.table.TableColumn clmName
            = tblProposal.getColumnModel().getColumn(
                tblProposal.getColumnCount()-1);
        clmName.setPreferredWidth(0);
        clmName.setMaxWidth(0);
        clmName.setMinWidth(0);
        clmName.setWidth(0);
        tblProposal.addMouseListener(new CustomMouseAdapter());
        javax.swing.table.JTableHeader header
            = tblProposal.getTableHeader();
        //header.setResizingAllowed(false);
        header.setReorderingAllowed(false);
        setSearchResultsTable(tblProposal);
        scrPnSearchRes = new JScrollPane();
        scrPnSearchRes.setMinimumSize(new Dimension(22, 15));
        //scrPnSearchRes.setPreferredSize(new Dimension(600, 400));
        if( tblProposal != null ){
            tblProposal.setFont( CoeusFontFactory.getNormalFont() );
        }
        scrPnSearchRes.setViewportView(tblProposal);
        scrPnSearchRes.getViewport().setBackground(Color.white);
        scrPnSearchRes.setForeground(Color.white);
        getContentPane().add(scrPnSearchRes);

    }

    /**
     * This method is used to enable/disable form fields like Tool Bar items, Menu Items, etc.,
     */
    private void formatFields(){
        if(!hasCreateProposalRights())
        {
            proposalNew.setEnabled(false);
            proposalCopy.setEnabled(false);
            btnNewProposal.setEnabled(false);
            btnCopyProposal.setEnabled(false);
            newProposalFromGG.setEnabled(false);
        }
    }
    
    /**
     * This methods loads the search window when the Proposal module is opened,
     * this helps the user to load the base window with the Proposal details.
     *
     */
    private void showProposalSearchWindow() {
        try {
           
            searchWindow.showSearchWindow();
            JTable tblResultsTable = searchWindow.getSearchResTable();
            //setSearchResultsTable(tblResultsTable);
            if (tblResultsTable != null) {
                tblProposal = tblResultsTable;
                javax.swing.table.TableColumn clmName
                    = tblProposal.getColumnModel().getColumn(
                        tblProposal.getColumnCount()-1);
                clmName.setPreferredWidth(0);
                clmName.setMaxWidth(0);
                clmName.setMinWidth(0);
                clmName.setWidth(0);
                javax.swing.table.JTableHeader header
                    = tblProposal.getTableHeader();
                //header.setResizingAllowed(false);
                header.setReorderingAllowed(false);
                header.addMouseListener(new CustomMouseAdapter());
                scrPnSearchRes.setViewportView(tblProposal);
                tblProposal.addMouseListener(new CustomMouseAdapter());
            }
            this.revalidate();
            int selRow = tblProposal.getSelectedRow();
            final String propNum;
            if(tblProposal!= null && tblProposal.getRowCount() > 0){
                 propNum = getSelectedProposalNumber(selRow,"PROPOSAL_NUMBER");
                  //Case 2451 start
                  vecSortedData = new Vector();
                 //Case 2451 End

                 // Added by chandra. To fix  #948
                 // Override the functionality of Ctrl+C.
                 // 12th July 2004 - Start
                 tblResultsTable.addKeyListener(new KeyAdapter() {
                 public void keyPressed(KeyEvent e) {
                    KeyStroke keyStroke = KeyStroke.getKeyStroke(e.getKeyCode(), e.getModifiers());
                    if(keyStroke.equals(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK))) {
                        try{
                            copyProposalDetails(propNum);
                        }catch (Exception exception){
                            CoeusOptionPane.showErrorDialog(exception.getMessage());
                        }
                    }
                  }
                });
            }
            // End chandra 12th July 2004
            
            
        } catch (Exception e) {
            e.printStackTrace() ;
        }
    }
        private void getProposalFileMenu() {
        sort = new CoeusMenuItem("Sort", null, true, true);
        sort.setMnemonic('o');
        //Modified for bug fixed for case #2302 start 3
        //JMenu fileMenu = mdiForm.getCoeusMenuBar().getMenu(0);//.add(sort,5);
        CoeusFileMenu coeusFile = new CoeusFileMenu(mdiForm);
        fileMenu = coeusFile.getMenu();//.add(sort,5);
        fileMenu.add(sort,5);
        fileMenu.insertSeparator(6);
        mdiForm.getCoeusMenuBar().remove(0);
        mdiForm.getCoeusMenuBar().add(fileMenu, 0);
        mdiForm.getCoeusMenuBar().validate();
        //Modified for bug fixed for case #2302 end 3 
        sort.addActionListener(this);
         
    }
    //Added for bug fixed for Case#2302 start 4
    public void internalFrameActivated(InternalFrameEvent internalFrameEvent) {
        super.internalFrameActivated(internalFrameEvent);
        getProposalFileMenu();
    }
    
    public void internalFrameDeactivated(InternalFrameEvent internalFrameEvent) {
        mdiForm.getCoeusMenuBar().remove(fileMenu);
        mdiForm.getCoeusMenuBar().add(new CoeusFileMenu(mdiForm).getMenu(), 0);
        mdiForm.getCoeusMenuBar().validate();
        super.internalFrameDeactivated(internalFrameEvent);
        
    }
    //Added for bug fixed for Case#2302 end 4
    /**
     * constructs Proposal edit menu with sub menu Add,Modify,Dispaly,Amend and
     * Proposal Deatil Forms.
     *
     * @return CoeusMenu Proposal edit menu
     */
    private CoeusMenu getProposalEditMenu() {
        CoeusMenu mnuProposal = null;
        Vector fileChildren = new Vector();

        proposalNew = new CoeusMenuItem("New Proposal...", null, true, true);
        proposalNew.setMnemonic('N');
        proposalNew.addActionListener(this);

        proposalModify = new CoeusMenuItem("Modify Proposal...", null, true, true);
        proposalModify.setMnemonic('M');
        proposalModify.addActionListener(this);

        proposalDisplay = new CoeusMenuItem("Display Proposal...", null, true, true);
        proposalDisplay.setMnemonic('D');
        proposalDisplay.addActionListener(this);
        
            
        /*Added for Case# COEUSQA-1675 -ability to delete Proposal Development proposals - Start*/
        proposalDelete = new CoeusMenuItem("Delete", null, true, true);
        proposalDelete.setMnemonic('e');
        proposalDelete.addActionListener(this);
       /*Added for Case# COEUSQA-1675 -ability to delete Proposal Development proposals - End*/

        proposalCopy = new CoeusMenuItem("Copy Proposal...", null, true, true);
        proposalCopy.setMnemonic('C');
        proposalCopy.addActionListener(this);
        
        
        proposalCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,KeyEvent.CTRL_MASK));

        proposalNotepad = new CoeusMenuItem("Notepad...", null, true, true);
        proposalNotepad.setMnemonic('t');
        proposalNotepad.addActionListener(this);

        proposalViewer = new CoeusMenuItem("Add Proposal Viewer", null, true, true);
        proposalViewer.setMnemonic('V');
        proposalViewer.addActionListener(this);
        
        proposalAdminDetails = new CoeusMenuItem("Proposal Admin Details", null, true, true);
        proposalAdminDetails.setMnemonic('A');
        proposalAdminDetails.addActionListener(this);
        
        proposalMedusa = new CoeusMenuItem("Medusa", null, true, true);
        proposalMedusa.setMnemonic('u');
        proposalMedusa.addActionListener(this);
        proposalMedusa.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,KeyEvent.CTRL_MASK));
        
        //Added by Vyjayanthi to open budget
        proposalBudget = new CoeusMenuItem("Budget", null, true, true);
        proposalBudget.setMnemonic('B');
        proposalBudget.addActionListener(this);

        proposalNarrative = new CoeusMenuItem("Narrative", null, true, true);
        proposalNarrative.setMnemonic('r');
        proposalNarrative.addActionListener(this);
        
        create = new CoeusMenuItem("Create Hierarchy", null, true, true);
        create.setMnemonic('H');
        create.addActionListener(this);
        
        join = new CoeusMenuItem("Join Hierarchy", null, true, true);
        join.setMnemonic('J');
        join.addActionListener(this);
        
        remove = new CoeusMenuItem("Remove Hierarchy", null, true, true);
        remove.setMnemonic('R');
        remove.addActionListener(this);
        //JIRA COEUSDEV 61 - START
        //JIRA COEUSQA-2475 - START
        newProposalFromGG = new CoeusMenuItem("Create Proposal from Grants.Gov Opportunity", null, true, true);
        //JIRA COEUSQA-2475 - END
        newProposalFromGG.setMnemonic('O');
        newProposalFromGG.addActionListener(this);
        //JIRA COEUSDEV 61 - END
        
         /*Added for Case# COEUSQA-1675 -ability to delete Proposal Development proposals - Start*/        
        fileChildren.add(proposalDelete);
        /*Added for Case# COEUSQA-1675 -ability to delete Proposal Development proposals- End*/
        fileChildren.add(proposalCopy);
        fileChildren.add(proposalNew);
        fileChildren.add(proposalModify);
        fileChildren.add(proposalDisplay);
        fileChildren.add(SEPERATOR);    //Added by Vyjayanthi to open budget
        fileChildren.add(proposalBudget);   //Added by Vyjayanthi to open budget
        fileChildren.add(proposalNarrative);
        fileChildren.add(SEPERATOR);
        fileChildren.add(proposalNotepad);
        fileChildren.add(proposalViewer);
        fileChildren.add(SEPERATOR);
        fileChildren.add(proposalAdminDetails);
        fileChildren.add(SEPERATOR);
        fileChildren.add(proposalMedusa);
        fileChildren.add(SEPERATOR);
//        fileChildren.add(create);
//        fileChildren.add(join);
//        fileChildren.add(remove);
        fileChildren.add(newProposalFromGG);

        mnuProposal = new CoeusMenu("Edit", null, fileChildren, true, true);
        mnuProposal.setMnemonic('E');
        return mnuProposal;

    }

    /** Constructs Proposal Tools menu for Search screen with sub menu of <CODE>Search</CODE>
     *
     * @return <CODE>CoeusMenu</CODE> Proposal tools menu.
     */
    public CoeusMenu getProposalToolsMenu() {
        CoeusMenu coeusMenu;
        Vector fileChildren = new Vector();
        proposalSearch = new CoeusMenuItem("Search", null, true, true);
        proposalSearch.setMnemonic('S');
        proposalSearch.addActionListener(this);
        fileChildren.add(proposalSearch);
        coeusMenu = new CoeusMenu("Tools", null, fileChildren, true, true);
        coeusMenu.setMnemonic('T');
        return coeusMenu;
    }

    /**
     * This method is used to create the tool bar for Save, Add, Mofify and
     * Close buttons.
     *
     * @returns JToolBar Proposal Toolbar
     */
    private JToolBar getProposalToolBar() {
        JToolBar toolbar = new JToolBar();

        btnNewProposal = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ADD_ICON)),
        null, "Add a new Proposal");

        btnNewProposal.setDisabledIcon(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DADD_ICON)));

        btnModifyProposal = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.EDIT_ICON)),
        null, "Modify selected Proposal");

        btnModifyProposal.setDisabledIcon(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DEDIT_ICON)));
        
        btnDisplayProposal = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DISPLAY_ICON)),
        null, "Display selected Proposal");
        
        /*Added for Case# COEUSQA-1675 -ability to delete Proposal Development proposals - Start*/
        btnDeleteProposal = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.DELETE_ICON)),
                null, "Delete Proposal");
        /*Added for Case# COEUSQA-1675 -ability to delete Proposal Development proposals - End*/

        btnCopyProposal = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.COPY_ICON)),
        null, "Copy Proposal");

        btnSearchProposal = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)),
        null, "Search for Proposal");

        btnNotepad = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.NOTEPAD_ICON)),
        null, "Notepad");
        
        btnMedusa = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.MEDUSA_ICON)),
        null, "Medusa");
        
        btnSort = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SORT_ICON)),
        null, "Sort Proposals");

        btnSaveAsProposal = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SAVEAS_ICON)),
        null, "Save As");

        btnCloseProposal = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
        null, "Close");
        /*Added for Case# COEUSQA-1675 -ability to delete Proposal Development proposals - Start*/
        btnDeleteProposal.addActionListener(this);
        /*Added for Case# COEUSQA-1675 -ability to delete Proposal Development proposals - End*/
        btnCopyProposal.addActionListener(this);
        btnNewProposal.addActionListener(this);
        btnModifyProposal.addActionListener(this);
        btnDisplayProposal.addActionListener(this);
        
        btnNotepad.addActionListener(this);
        btnMedusa.addActionListener(this);
        btnSort.addActionListener(this);
        
        btnSearchProposal.addActionListener(this);
        btnSaveAsProposal.addActionListener(this);
        btnCloseProposal.addActionListener(this);

        toolbar.add(btnNewProposal);
        toolbar.add(btnModifyProposal);
        toolbar.add(btnDisplayProposal);
        /*Added for Case# COEUSQA-1675 -ability to delete Proposal Development proposals - Start*/
        toolbar.add(btnDeleteProposal);
        /*Added for Case# COEUSQA-1675 -ability to delete Proposal Development proposals - End*/
        toolbar.add(btnCopyProposal);
        toolbar.addSeparator();
        toolbar.add(btnNotepad);
        toolbar.addSeparator();
        toolbar.add(btnMedusa);
        toolbar.addSeparator();
        toolbar.add(btnSort);
        toolbar.add(btnSearchProposal);
        toolbar.add(btnSaveAsProposal);
        toolbar.addSeparator();
        toolbar.add(btnCloseProposal);

        toolbar.setFloatable(false);
        return toolbar;
    }

    /** All the  actions for the menu and toolbar associated with
     * the <CODE>ProposalBaseWindow</CODE> will be invoked from this method.
     *
     * @param actionType <CODE>ActionEvent</CODE>, a semantic event which indicates that a
     * component-defined action occured.
     */
    public void actionPerformed(ActionEvent actionType) {
        try{
            /*Bug Fix 1425,added by surekha*/
            mdiForm.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            int selectedRow = 0;
            String proposalNumber = null;
            if (tblProposal != null && tblProposal.getRowCount() > 0) {
                selectedRow = tblProposal.getSelectedRow();
                //Check whether row is selected
                if (selectedRow >= 0) {
                    proposalNumber = getSelectedProposalNumber(selectedRow,"PROPOSAL_NUMBER");
                }
            }

            Object actSource = actionType.getSource();

        /* when the menu or tool bar add is clicked addProposalDetails
         * method will be fired
         */
            if (actSource.equals(proposalNew) || actSource.equals(btnNewProposal)) {
                addProposalDetails();
            /* when the menu or tool bar modify is clicked modifyProposalDetails
             * method will be fired
             */
            } else if (actSource.equals(proposalModify) ||
            actSource.equals(btnModifyProposal)) {

                modifyProposalDetails(proposalNumber);
            /* when the menu or tool bar display is clicked showProposalDetails
             * method will be fired
             */
            } else if (actSource.equals(proposalDisplay) ||
            actSource.equals(btnDisplayProposal)) {
                showProposalDetails(proposalNumber);
            }
            /*Added for Case# COEUSQA-1675 -ability to delete Proposal Development proposals - Start*/
            else if (actSource.equals(proposalDelete)
            || actSource.equals(btnDeleteProposal)) {
                performDelete(proposalNumber,selectedRow);
            }
            /*Added for Case# COEUSQA-1675 -ability to delete Proposal Development proposals - End*/ 
            
            else if (actSource.equals(proposalCopy) ||
            actSource.equals(btnCopyProposal)) {
                //Amend Functionality Call - Copy From one Proposal to another with
                //new Proposal ID(Max of Proposal ID+1) and Sequence Number 1
            	// JM 11-16-2012 check if proposal can be copied
                CustomFunctions function = new CustomFunctions();
             	String[] disallowedStatuses = function.getParameterValues(DISALLOW_PROP_COPY_STATUS);
                String propStatus = getSelectedProposalNumber(selectedRow, "CREATION_STATUS_CODE").toString();
             	java.util.Set<String> STATUSES = new java.util.HashSet<String>(java.util.Arrays.asList(disallowedStatuses));
				if (STATUSES.contains(propStatus)) {
					String message = "Proposals with the following statuses cannot be copied:  ";
					for (int d=0; d<disallowedStatuses.length; d++) {
						message += disallowedStatuses[d] + ", ";
					}
					message = message.substring(0,message.length() - 2) + "  ";
					CoeusOptionPane.showErrorDialog(message);
				}
				else {
                copyProposalDetails(proposalNumber);
				}
                // JM END

            } else if (actSource.equals(btnCloseProposal)) {
                closeProposalBaseWindow();
            }else if (actSource.equals(proposalAdminDetails)){
                showProposalAdminDetails(proposalNumber);
            }else if (actSource.equals(proposalViewer)){
                showProposalViewer(proposalNumber);
            }else if( actSource.equals(btnNotepad) || 
            actSource.equals(proposalNotepad)){
                showNotepadWindow();    //Added by Vyjayanthi
            }else if(actSource.equals(proposalMedusa) || actSource.equals(btnMedusa)){
                showMedusaWidnow();
            }else if( actSource.equals(proposalBudget) ){
                showBudget();   //Added by Vyjayanthi to open budget
            }else if(actSource.equals(btnSort) || actSource.equals(sort)){
                showSort();
            }
            else if( actSource.equals( proposalNarrative ) ) {
                showNarrative(proposalNumber,'M');
            }else if(actSource.equals(create)){
                createHierarchy();
            }else if(actSource.equals(newProposalFromGG)){
                //JIRA COEUSDEV 61 - START
                if (hasCreateProposalRights()) {
                    OpportunitySearchController opportunitySearchController = new OpportunitySearchController(mdiForm);
                    OpportunityInfoBean bean = opportunitySearchController.display();
                    if (bean != null) {
                        //Create New proposal for this opportunity
                        //call new proposal and pass this opportunity and save it along with the new proposal
                        addProposalDetails();
                        if (mainProposal != null) {
                            mainProposal.setOpportunityInfoBeanForGG(bean);
                        }
                    }
                } else {
                    log(coeusMessageResources.parseMessageKey("proposal_BaseWin_exceptionCode.7102"));
                }
                //JIRA COEUSDEV 61 - END
            }
        /* when the Toolsmenu search is clicked showProposalSearchWindow
         * method will be fired
         */
            if (actSource.equals(proposalSearch) ||
            actSource.equals(btnSearchProposal)) {
                showProposalSearchWindow();
            }
        /* when the Toolsmenu SaveAs is clicked showSaveAsDialog
         * method will be fired
         */
            if (actSource.equals(btnSaveAsProposal)) {
            //    showSaveAsDialog();
            //new implementation
                saveAsActiveSheet();
            }
            
        }catch( Exception err ){
            err.printStackTrace();
            CoeusOptionPane.showInfoDialog(coeusMessageResources.
                                            parseMessageKey(err.getMessage()));
        }finally {
            mdiForm.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }/*Bug Fix:1425 ,added bysurekha*/
    }
    
    
    private void createHierarchy() throws CoeusException,Exception{
        int code=0 ;
        Vector data = canBuildHierarchy();
        code = ((Integer)data.get(1)).intValue();
        if(code==100){
          CoeusOptionPane.showErrorDialog("It is Already Linked");
          return ;
        }else if(code==200){
            CoeusOptionPane.showErrorDialog("Proposal status should be In Progress OR Rejected");
            return ;
        }else if(code==300){
            CoeusOptionPane.showErrorDialog("Budget Status should be Complete");  
        }else if(code==400){
            CoeusOptionPane.showErrorDialog("There should be an Investigator");
            return;
        }else if(code==500){
            CoeusOptionPane.showErrorDialog("Parent can't be linked to the hierarchy");
        }else{
            // Do the Hierarchy without any Error.
            createParentProposal();
        }
    }
    
    private Vector canBuildHierarchy() throws CoeusException, Exception{
        int selectedRow = tblProposal.getSelectedRow();
        Vector data = null;
        if( selectedRow !=1 ){ 
            String proposalNumber = getSelectedProposalNumber(selectedRow,"PROPOSAL_NUMBER");
            String connectTo = CoeusGuiConstants.CONNECTION_URL +HIERARCHY_SERVLET;
            RequesterBean request = new RequesterBean();
            request.setFunctionType(CAN_BUILD_HIERARCHY);
            request.setDataObject(proposalNumber);
            request.setId(CREATE_ACTION);

            AppletServletCommunicator comm
            = new AppletServletCommunicator( connectTo, request );
            comm.send();
            ResponderBean response = comm.getResponse();
            if (response == null) {
                response = new ResponderBean();
                response.setResponseStatus(false);
            }
            if (response.isSuccessfulResponse()) {
                data = response.getDataObjects();
            }else{
                throw new CoeusException(response.getMessage());
            }
        }
        return data;
    }// End canBuildHierarchy
    
    
    private void createParentProposal() throws CoeusException,Exception{
        
        Vector data  = createAndUpdateParent();
        ProposalDevelopmentFormBean formBean = null;
        int createFlag = 0 ;
        if(data!= null && data.size()>0) {
            createFlag = Integer.parseInt(data.elementAt(0).toString());
            formBean = (ProposalDevelopmentFormBean) data.elementAt(1);
            String msg = "Parent Proposal "+
            formBean.getProposalNumber() +" has been created.Do you want to open it ?";
            int resultConfirm = CoeusOptionPane.showQuestionDialog(msg,
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            if(resultConfirm==0){
                modifyProposalDetails(formBean.getProposalNumber());
            }
        }
        if(createFlag< 0) {
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
            "Error creating the parent proposal"));
            return;
        }
    }
    
    private Vector createAndUpdateParent() throws CoeusException,Exception{
        int selectedRow = tblProposal.getSelectedRow();
        Vector data = null;
        if( selectedRow != -1 ){
            
            String proposalNumber = getSelectedProposalNumber(selectedRow, "PROPOSAL_NUMBER");
            String unitNumber = getSelectedProposalNumber(selectedRow, "UNIT_NUMBER");
            

            String connectTo = CoeusGuiConstants.CONNECTION_URL + HIERARCHY_SERVLET;
            RequesterBean request = new RequesterBean();
            Vector vecProcParams = new Vector();
            vecProcParams.addElement(proposalNumber);
            vecProcParams.addElement(unitNumber);
            request.setDataObjects(vecProcParams);
            request.setFunctionType(CREATE_PARENT_PROPOSAL);
            AppletServletCommunicator comm
                    = new AppletServletCommunicator(connectTo, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if (response!=null){
                if (response.isSuccessfulResponse()){
                    data = (Vector)response.getDataObjects();
                }
                else {
                    throw new CoeusException(response.getMessage());
                }
            }
        }
        return data;
    }
    
    // added by Nadh to implement sorting proposals start - 18-01-2004
    /*
     * this method shows the sort window
     * return void
     */
    private void showSort() {
        if(vecSortedData==null) {
            vecSortedData = new Vector();
        }
        SortForm sortForm = new SortForm(tblProposal,vecSortedData);
        Vector sortedData = sortForm.display();
        vecSortedData = (Vector)sortedData.get(1);
        if(((Integer)sortedData.get(0)).intValue() == OK_CLICKED)
            searchWindow.sortByColumns(tblProposal,vecSortedData);
        else
            return;
    }// end Nadh
    
    //Code Added by Vyjayanthi to open budget - Start
    /** Display the Select Budget screen
     */
    private void showBudget(){
        int selectedRow = tblProposal.getSelectedRow();
        if( selectedRow == -1 ) return ;
        SelectBudgetController selectBudgetController = new SelectBudgetController();
        try{
            proposalNumber = getSelectedProposalNumber(selectedRow,"PROPOSAL_NUMBER");
            String proposalStatus = getSelectedProposalNumber(selectedRow, "CREATION_STATUS_CODE").toString();
            selectBudgetController.setProposalId(proposalNumber);
            //Added by shiji for Right Checking - step 2 : start
            selectBudgetController.setIsBudgetOpenedFromPropList(true);
            
            if(selectedRow >=0){
                String unitNumber = getSelectedProposalNumber(selectedRow, "UNIT_NUMBER");
                selectBudgetController.setUnitNumber(unitNumber);
            }
            //Right Checking - step 2 : end
            selectBudgetController.setFormData();
            selectBudgetController.display();
        }catch (CoeusClientException coeusClientException){
            if( !coeusClientException.getMessageKey().equals("") ){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                coeusClientException.getMessage()));
            }
        }catch (Exception ex){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                ex.getMessage()));
        }
    }
    //Code Added by Vyjayanthi to open budget - End

    private void showNarrative(String proposalID, char fType){
        if( proposalID != null ){
            try{
                mdiForm.checkDuplicate(CoeusGuiConstants.PROPOSAL_NARRATIVE_FRAME_TITLE, 
                    proposalID, fType );
            }catch(Exception e){
                // Exception occured.  Record may be already opened in requested mode
                //   or if the requested mode is edit mode and application is already
                //   editing any other record. 
                if(e.getMessage().length() > 0 ) {
                    CoeusOptionPane.showInfoDialog(e.getMessage());
                }
                // try to get the requested frame which is already opened 
                CoeusInternalFrame frame = mdiForm.getFrame(
                        CoeusGuiConstants.PROPOSAL_NARRATIVE_FRAME_TITLE,proposalID);
                if(frame == null){
                    // if no frame opened for the requested record then the 
                    //   requested mode is edit mode. So get the frame of the
                    //   editing record. 
                    frame = mdiForm.getEditingFrame( 
                        CoeusGuiConstants.PROPOSAL_NARRATIVE_FRAME_TITLE );
                }
                if (frame != null){
                    try{
                        frame.setSelected(true);
                        frame.setVisible(true);
                    }catch(Exception ex) {
                        ex.printStackTrace();
                    }
                }
                return;
            }        

            try{
                narrativeForm = new ProposalNarrativeForm( proposalID, fType, mdiForm );
                //Added by shiji for Right Checking - step 3 : start
                narrativeForm.setIsNarrativeOpenedFromPropList(true);
                int selectedRow = tblProposal.getSelectedRow();
                if(selectedRow >=0){
                    String unitNumber = getSelectedProposalNumber(selectedRow,"UNIT_NUMBER");
                    narrativeForm.setUnitNumber(unitNumber);
                }
                //Right Checking - step 2 : end
                //Added for the Coeus Enhancement case:#1767
                //Modified COEUSQA-1579 : For Hierarchy Proposal, Approval in Progress, cannot sync after narratives updated on child proposal
                //Getting the proposal status code from the database
//                String statusDescription = getSelectedProposalNumber(selectedRow, "CREATION_STATUS_CODE");
//                String statusCode = getProposalStatusDesc(statusDescription);
                if(selectedRow >= 0){
                    String proposalNumber = getSelectedProposalNumber(selectedRow,"PROPOSAL_NUMBER");
                    int statusCode = getProposalStatusCode(proposalNumber);
                    narrativeForm.setProposalStatusCode(statusCode);
                    
            		// JM 4-10-2015 check if narratives are locked
                    if (statusCode == 2) {
        	    		CustomFunctions fn = new CustomFunctions();
        	    		LockingBean lockingBean = (LockingBean) fn.getNarrativeLock(proposalID);
        	    		String lockingUser = "";
        	    		
        	    		boolean locked = false;
        	    		if (lockingBean.getUpdateUser() != null) {
        	    			locked = true;
        	    			lockingUser = lockingBean.getUpdateUser();
        	    		}
        	    		
        	    		if (locked) {
        	    			if (lockingBean.getUpdateUserName() != null) {
        	    				lockingUser = lockingBean.getUpdateUserName();
        	    			}
        	    			String lockMessage = "Narratives are currently in use and locked by " + lockingUser + "  ";
        	    			CoeusOptionPane.showErrorDialog(lockMessage);
        	    		}
                    }
            		// JM END
                }
                //COEUSQA-1579 : end 
                //End Coeus Enhancement case:#1767
                narrativeForm.showNarrativeForm();
            }catch ( Exception e) {
                String msg = e.getMessage();
                if( msg != null && msg.length() > 0 && !( msg.equals(coeusMessageResources.parseMessageKey(
                "protoDetFrm_exceptionCode.1130")) )){
                    CoeusOptionPane.showInfoDialog(e.getMessage());
                }
            }
        }else{
            log(coeusMessageResources.parseMessageKey("protoBaseWin_exceptionCode.1052"));
        }
    }
    
    
    private void  showMedusaWidnow(){
               try{
                   ProposalAwardHierarchyLinkBean linkBean;
                   MedusaDetailForm medusaDetailform;
                   //CoeusInternalFrame frame = mdiForm.getFrame(CoeusGuiConstants.MEDUSA_BASE_FRAME_TITLE);
                   int selectedRow = tblProposal.getSelectedRow();
                   if( selectedRow >= 0 ){
                       String proposalNumber = getSelectedProposalNumber(selectedRow,"PROPOSAL_NUMBER");
                       linkBean = new ProposalAwardHierarchyLinkBean();
                       linkBean.setDevelopmentProposalNumber(proposalNumber);
                       linkBean.setBaseType(CoeusConstants.DEV_PROP);
                       if( ( medusaDetailform = (MedusaDetailForm)mdiForm.getFrame(
                            CoeusGuiConstants.MEDUSA_BASE_FRAME_TITLE))!= null ){
                           if( medusaDetailform.isIcon() ){
                               medusaDetailform.setIcon(false);
                           }
                           medusaDetailform.setSelectedNodeId(proposalNumber);
                           medusaDetailform.setSelected( true );
                           return;
                       }
                       medusaDetailform = new MedusaDetailForm(mdiForm,linkBean);
                       medusaDetailform.setVisible(true);
                   }else{
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                                "protoBaseWin_exceptionCode.1052"));
                   }
                   
               }catch(Exception exception){
                   exception.printStackTrace();
               }
           }
    
    //Modified for bug id 1856 step 2 : start
    //Code Added by Vyjayanthi - Start
    /** To display the notepad window if not already open */
    private void showNotepadWindow(){
        try{
        //Check if Notepad is already opened
        CoeusInternalFrame frame = mdiForm.getFrame(CoeusGuiConstants.NOTEPAD_FRAME_TITLE);
        if(frame == null){
            int selectedRow = tblProposal.getSelectedRow();
            if( selectedRow == -1 ) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                "protoBaseWin_exceptionCode.1052"));
                return ;
            }
            String proposalNumber = getSelectedProposalNumber(selectedRow,"PROPOSAL_NUMBER");
            String unitNumber = getSelectedProposalNumber(selectedRow,"UNIT_NUMBER");
            ProposalAwardHierarchyLinkBean linkBean = new ProposalAwardHierarchyLinkBean();
            linkBean.setDevelopmentProposalNumber(proposalNumber);
            linkBean.setBaseType(CoeusConstants.DEV_PROP);
            ProposalNotepadForm proposalNotepadForm = new ProposalNotepadForm(linkBean, mdiForm);
            proposalNotepadForm.setProposalUnitNumber(unitNumber);
            proposalNotepadForm.postInitComponents();
            if(proposalNotepadForm.hasOSPRight || proposalNotepadForm.hasRight || hasDisplayProposalRights()){
                proposalNotepadForm.display();
            }else {
                CoeusOptionPane.showInfoDialog("You do not have rights to view the Notepad");
            }
        }else{
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
            "proposal_Notepad_exceptionCode.7116"));
        }
        }catch (Exception ex){
            CoeusOptionPane.showErrorDialog(ex.getMessage());
        }
    }
    //Code Added by Vyjayanthi - End
    //bug id 1856 step 2 : end
    
    private void showProposalAdminDetails(String proposalNumber){
        if (proposalNumber == null) {
            log(coeusMessageResources.parseMessageKey(
            "protoBaseWin_exceptionCode.1052"));
        }
        else{
            int selectedRow = tblProposal.getSelectedRow();
            try{
                proposalNumber = getSelectedProposalNumber(selectedRow, "PROPOSAL_NUMBER");
                String statusDescription = getSelectedProposalNumber(selectedRow, "CREATION_STATUS_CODE");
                if(statusDescription.trim().equalsIgnoreCase("In Progress") ||
                statusDescription.equalsIgnoreCase("Rejected")){
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                    "proposal_AdminDetails_exceptionCode.7109"));
                    return;
                }
                
                
                ProposalAdminDetailsForm proposalAdminDetailsForm =
                new ProposalAdminDetailsForm(proposalNumber,DISPLAY_MODE);
            }catch( Exception err ){
                err.printStackTrace();
                CoeusOptionPane.showInfoDialog(err.getMessage());
            }
        }
    }
    
    /**
     * This method is invoked when the user clicks ADD PROPOSAL VIEWER in the Edit menu
     * of Proposal module
     */
    private void showProposalViewer(String proposalNumber){
        int selectedRow = tblProposal.getSelectedRow();
        if (proposalNumber == null) {
            log(coeusMessageResources.parseMessageKey(
            "protoBaseWin_exceptionCode.1052"));
        } else {
            try{
                //System.out.println("Status Description Is "+statusDescription);
                boolean valid = performValidation(proposalNumber);
                // Added by chandra 10/02/04- start
                // pass the description and get the code. If the codes are matching then display the message
                String statusDescription = getSelectedProposalNumber(selectedRow,"CREATION_STATUS_CODE");
                String statusCode = getProposalStatusDesc(statusDescription);
                if(statusCode.equals("1")){
                    log("Cannot perform this function on a proposal in progress");
                }else if(statusCode.equals("3")){
                    log("Cannot perform this function on a rejected proposal");
                    // Added by chandra 10/02/04- End
                }else{
                    if(!valid){
                        
                        log("You do not have a right to add proposal viewer");
                    }else{
                
                        String sponsorCode = getSelectedProposalNumber(selectedRow, "SPONSOR_CODE");
                        String sponsorName = getSelectedProposalNumber(selectedRow, "SPONSOR_NAME");
                        String sponsorCodeName = sponsorCode + " : " + sponsorName;
                        if (sponsorCode.equals(null)) {
                            sponsorCodeName = "";
                        }
                        ProposalViewersForm proposalViewform = 
                                            new ProposalViewersForm(proposalNumber,
                                                CoeusGuiConstants.PROPOSAL_VIEWER_ROLE_ID,sponsorCodeName);
                    }                    
                }
                /*
                if(statusDescription.equalsIgnoreCase("In Progress")){
                    log("Cannot perform this function on a proposal in progress");
                }else if(statusDescription.equalsIgnoreCase("Rejected")){
                    log("Cannot perform this function on a rejected proposal");
                }else{
                    //System.out.println("Value of valid is "+valid);
                    if(!valid){
                        
                        log("You do not have a right to add proposal viewer");
                    }else{
                
                        String sponsorCode = tblProposal.getValueAt(selectedRow, 8).toString();
                        String sponsorName = tblProposal.getValueAt(selectedRow, 9).toString();
                        String sponsorCodeName = sponsorCode + " : " + sponsorName;
                        if (sponsorCode.equals(null)) {
                            sponsorCodeName = "";
                        }
                        ProposalViewersForm proposalViewform = 
                                            new ProposalViewersForm(proposalNumber,
                                                CoeusGuiConstants.PROPOSAL_VIEWER_ROLE_ID,sponsorCodeName);
                    }
                }*/
            }catch( Exception err ){
               // err.printStackTrace();
                CoeusOptionPane.showInfoDialog(err.getMessage());
            }
        }
    }
    
    private boolean performValidation(String proposalNumber){
        
        Vector rightData = null;
        boolean hasRights = false;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";                                                                
        RequesterBean request = new RequesterBean();
        Vector vecFnParams = new Vector();
        
        Vector vecGetParams = new Vector();
        vecGetParams.addElement(USER_HAS_PROPOSAL_ROLE);
        //Modified for Case#3587 - multicampus enhancement  - Start
//        vecGetParams.addElement(USER_HAS_OSP_RIGHT);
        vecGetParams.addElement(USER_HAS_DEPARTMENTAL_RIGHT);
        //Case#3587 - End
        vecFnParams.addElement(mdiForm.getUserName());
        vecFnParams.addElement(new Integer(PROPOSAL_ROLE));
        vecFnParams.addElement(proposalNumber);
        vecFnParams.addElement(APPROVE_PROPOSAL_RIGHT);
        
        Vector vecDataToServer = new Vector();
        vecDataToServer.addElement(vecGetParams);
        vecDataToServer.addElement(vecFnParams);
        
        request.setDataObjects(vecDataToServer);
        
        request.setDataObject("GET_RIGHTS_FOR_PROPOSAL");
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();

        if (response!=null){
            if (response.isSuccessfulResponse()){
                rightData = (Vector)response.getDataObject();
            }
        }
        
        if(rightData != null){
            int userHasRole = ((Integer)rightData.elementAt(0)).intValue();
            int userHasOSPRight = ((Integer)rightData.elementAt(1)).intValue();
            
            //System.out.println("userHasRole is "+userHasRole);
            //System.out.println("userHasOSPRight is "+userHasOSPRight);
            
            if(userHasRole == 0 && userHasOSPRight == 0){
                hasRights = false;
            }else{
                hasRights = true;
            }
        }
        return hasRights;
    }
    /**
     * This method is invoked when the user clicks add in the Edit menu
     * of Proposal module
     */
    private void addProposalDetails() throws Exception {
        if (validateNewProposal()) {
            checkDuplicateAndShow("", ADD_MODE);
        }
    }

    /**
     * This method is invoked when the user clicks amend in the Edit menu
     * of Proposal module
     */
    private void copyProposalDetails(String proposalNumber) throws Exception {
        
        
        int selectedRow = tblProposal.getSelectedRow();
        // If no rows selected don't performa any action
        if(selectedRow==-1){
            return ;
        }
        String propNumber = getSelectedProposalNumber(selectedRow,"PROPOSAL_NUMBER");
        /*Added by chandra - it has to take the selected row value not the global value
         *Bug 948 - Selecting the first row value
         */
//        if (proposalNumber == null) {
          if (propNumber == null) {
            log(coeusMessageResources.parseMessageKey(
            "protoBaseWin_exceptionCode.1052"));
        } else {
            try{
                String sponsorCode = getSelectedProposalNumber(selectedRow,"SPONSOR_CODE");
                String sponsorName = getSelectedProposalNumber(selectedRow,"SPONSOR_NAME");
                String sponsorCodeName = sponsorCode + " : " + sponsorName;
                //Added for case#2903 - Modify all dev proposals should allow you to copy proposal
                //To get the unitNumber for MODIFY_ANY_PROPOSAL right checking.
                this.unitNumber = getSelectedProposalNumber(selectedRow, "UNIT_NUMBER");
                if ( sponsorCode == null ) {
                    sponsorCodeName = "";
                }
                /*Added by chandra - it has to take the selected row value not the global value
                 *Bug 948 - Selecting the first row value
                 *commented by chandra
                 */ 
//                CopyProposalForm copyProposalForm = 
//                                    new CopyProposalForm(mdiForm,proposalNumber,sponsorCodeName,mdiForm.getUserName());
                CopyProposalForm copyProposalForm = 
                                    new CopyProposalForm(mdiForm,propNumber,sponsorCodeName,mdiForm.getUserName());
                //Added for case#2903 - Modify all dev proposals should allow you to copy proposal
                //To set the unitNumber for MODIFY_ANY_PROPOSAL right checking.                
                copyProposalForm.setUnitNumber(unitNumber);
                copyProposalForm.setTableReference(tblProposal);
                copyProposalForm.showCopyProposalForm();
                copyProposalForm.display();
            }catch( Exception err ){
               // err.printStackTrace();
                CoeusOptionPane.showInfoDialog(err.getMessage());
            }
        }
    }
    /**
     * This method is used to check whether the given Proposal number is already 
     * opened in the given mode or not. 
     */
    private void checkDuplicateAndShow(String refId, char mode)throws Exception {
        boolean duplicate = false;
        try{
            refId = refId == null ? "" : refId;
            duplicate = mdiForm.checkDuplicate(
                CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE, refId, mode );
        }catch(Exception e){
            // Exception occured.  Record may be already opened in requested mode
            //   or if the requested mode is edit mode and application is already
            //   editing any other record. 
            duplicate = true;
            if(e.getMessage().length() > 0 ) {
                CoeusOptionPane.showInfoDialog(e.getMessage());
            }
            // try to get the requested frame which is already opened 
            CoeusInternalFrame frame = mdiForm.getFrame(
                    CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE,refId);
            if(frame == null){
                // if no frame opened for the requested record then the 
                //   requested mode is edit mode. So get the frame of the
                //   editing record. 
                frame = mdiForm.getEditingFrame( 
                    CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE );
            }
            if (frame != null){
                frame.setSelected(true);
                frame.setVisible(true);
            }
            return;
        }
        try{
            mainProposal = new ProposalDetailForm( mode, refId, mdiForm);
            if (mode == ADD_MODE) {
                mainProposal.setUnitNumber(unitNumber);
                mainProposal.setUnitName(unitName);
            }
            //Added by shiji for Right Checking - step 4 : start
            int selRow = tblProposal.getSelectedRow();
            if(mode == MODIFY_MODE) {
                String unitNumber = getSelectedProposalNumber(selRow, "UNIT_NUMBER");
                mainProposal.setUnitNumber(unitNumber);
            }
            //Right Checking - step 2 : end
            mainProposal.registerObserver( this );
            
            if( selRow != -1 ) {
                // Bug fix #1434
//                baseTableRow = Integer.parseInt((String)tblProposal.getValueAt(
//                selRow,tblProposal.getColumnCount()-1));
                // Bug fix #1434
                baseTableRow  = selRow;
            }
            mainProposal.showDialogForm();
            mainProposal.setProposalSheet(tblProposal);
            mainProposal.setBaseSearch(searchWindow);
        }catch ( Exception ex) {
           // ex.printStackTrace();
            try{
                if (!mainProposal.isModifiable() ) {
                    //Modified for case# 3439 to include the locking message - start
//                    String msg = coeusMessageResources.parseMessageKey(
//                    "proposal_row_clocked_exceptionCode.777777");
                    int resultConfirm = CoeusOptionPane.showQuestionDialog(ex.getMessage(),
                    CoeusOptionPane.OPTION_YES_NO,
                    CoeusOptionPane.DEFAULT_YES);
                    //Modified for case# 3439to include the locking message - end
                    if (resultConfirm == 0) {
                        showProposalDetails(refId);
                    }
                }else {
                    throw new Exception(ex.getMessage());
                }
            }catch (Exception excep){
                //excep.printStackTrace();
                throw new Exception(excep.getMessage());
            }
        }
    }
    
    /**
     * This method is invoked when the user clicks modify in the Edit menu
     * of Proposal module
     */
    private void modifyProposalDetails(String proposalNumber) throws Exception{
        int selRow = tblProposal.getSelectedRow();
        proposalNumber = getSelectedProposalNumber(selRow,"PROPOSAL_NUMBER");
        if (proposalNumber == null) {
            log(coeusMessageResources.parseMessageKey(
            "protoBaseWin_exceptionCode.1051"));
        } else {
            checkDuplicateAndShow(proposalNumber,MODIFY_MODE);
        }
    }
    
    /** Get the proposal number based on the search index column
     */
    private String getSelectedProposalNumber(int row, String colValue) throws Exception{
        int column = getColumnIndexValue(searchWindow, colValue);
        return (String)tblProposal.getValueAt(row, column);
    }
    
    /** Get the column index for the selected row, Get the data based on the 
     *Display bean data
     */
    private int getColumnIndexValue( ProposalSearch searchWindow, String columnName) {
        int columnIndex = -1;
        try{
            SearchColumnIndex searchIndex = new SearchColumnIndex();
            columnIndex = searchIndex.getSearchColumnIndex(searchWindow, columnName);
            searchIndex = null;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return columnIndex;
        
    }



/**
 * This method is invoked when the user clicks display in the Edit menu
 * of Proposal module
 */
private void showProposalDetails(String proposalNumber) {
    int selRow = tblProposal.getSelectedRow();
    if (proposalNumber == null) {
        log(coeusMessageResources.parseMessageKey(
        "protoBaseWin_exceptionCode.1052"));
    } else {
        try{
            proposalNumber = getSelectedProposalNumber(selRow,"PROPOSAL_NUMBER");
            if (validateDisplayProposal()) {
                checkDuplicateAndShow(proposalNumber,DISPLAY_MODE);
//                ++displaySheetCount;
            }
        }catch( Exception err ){
            //err.printStackTrace();
            CoeusOptionPane.showInfoDialog(err.getMessage());
        }
    }
}

/**
 * This method is invoked when the user clicks display in the Edit menu
 * of Proposal module
 *
 * @return boolean which indicates whether the validation was success or failure.
 */
private boolean validateDisplayProposal() {
    boolean displayProposal = false;

//    if (displaySheetCount == CoeusGuiConstants.MAX_PROP_DISPLAY_SHEETS) {
//            log(coeusMessageResources.parseMessageKey(
//                                "proposal_BaseWin_exceptionCode.7103"));
//        return displayProposal;
//    }
    
    if (hasDisplayProposalRights()) {
        displayProposal = true;
    } else {
        log(coeusMessageResources.parseMessageKey(
                                "proposal_BaseWin_exceptionCode.7104"));
    }
    
    return displayProposal;
}

/**
 * This method is invoked when the user clicks New in the Edit menu
 * of Proposal module
 *
 * @return boolean which indicates whether the validation was success or failure.
 */
private boolean validateNewProposal() {
    boolean openNewProposal = false;
    Vector userUnits = null;
    ComboBoxBean comboBoxBean;
    
    if (hasCreateProposalRights()) {
        userUnits = getUserUnits();
        if (userUnits != null) {
            if (userUnits.size() == 1) {
                comboBoxBean = (ComboBoxBean) userUnits.elementAt(0);
                if (comboBoxBean != null) {
                    unitNumber = comboBoxBean.getCode();
                    unitName = comboBoxBean.getDescription();
                    openNewProposal = true;
                }

            } else if (userUnits.size() > 1) {

                String windowTitle = "Select Unit for New Proposal";
                OtherLookupBean otherLookupBean =
                    new OtherLookupBean(windowTitle, userUnits, UNIT_COLUMN_NAMES);
                CoeusTableWindow coeusTableWindow =
                                            new CoeusTableWindow(otherLookupBean); 
                int selRow = otherLookupBean.getSelectedInd();
                if(selRow >= 0){
                    comboBoxBean = (ComboBoxBean)userUnits.elementAt(selRow);
                    if(comboBoxBean != null){
                        unitNumber = comboBoxBean.getCode();
                        unitName = comboBoxBean.getDescription();
                        openNewProposal = true;
                    }
                }
            }
        }
    } else {
        log(coeusMessageResources.parseMessageKey(
                                "proposal_BaseWin_exceptionCode.7102"));
    }
    return openNewProposal;
}

/**
 * This method is invoked when the user clicks New in the Edit menu
 * of Proposal module. Checks whether the User has rights to Create Proposal
 *
 * @return boolean. true indicates has rights and false indicates no rights.
 */
private boolean hasCreateProposalRights(){

    boolean hasRights = false;
    String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";                                                                
    RequesterBean request = new RequesterBean();
    Vector vecFnParams = new Vector();
    vecFnParams.addElement(mdiForm.getUserName());
    //vecFnParams.addElement(ROLE+"");
    vecFnParams.addElement(CREATE_RIGHT);
    request.setDataObjects(vecFnParams);
    request.setDataObject("GET_USER_HAS_PROPOSAL_RIGHTS");
    AppletServletCommunicator comm
            = new AppletServletCommunicator(connectTo, request);
    comm.send();
    ResponderBean response = comm.getResponse();
    /** Case Id 1856 and 1860.
     *Check the right not the role. Check CREATE_PROPOSAL right insted of checking 
     *against role_id = 7
     */
    if (response!=null){
        if (response.isSuccessfulResponse()){
                hasRights = ((Boolean)response.getDataObject()).booleanValue();
           
        }
    }
/** Case Id 1856 and 1860. Check the right not the role.
 *start
//    if (response!=null){
//        if (response.isSuccessfulResponse()){
//            int right = Integer.parseInt(response.getId());
//            if (right == 1) {
//                hasRights = true;
//            }
//        }
//    }
 *End
 */
    return hasRights;
}
//Modified for bug id 1856 step 3 : start
//Modified by shiji for Right Checking - step 5 : start
private boolean hasDisplayProposalRights(){
    boolean hasRights = false;
    String proposalNumber;
    Vector vecFnParams = new Vector();
    int selectedRow = tblProposal.getSelectedRow();
    String unitNumber = "";
    try{
    unitNumber = getSelectedProposalNumber(selectedRow,"UNIT_NUMBER");
    String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
    RequesterBean request = new RequesterBean();
    //modified for case #1860 start 1
    String getStatus = getSelectedProposalNumber(selectedRow, "CREATION_STATUS_CODE");
    String getStatusCode = getProposalStatusDesc(getStatus);
    //modified for case #1860 end 1
    AppletServletCommunicator comm
    = new AppletServletCommunicator(connectTo, request);
    request.setId(mdiForm.getUserName());
    request.setFunctionType('b');
    request.setDataObject("GET_USER_HAS_ANY_OSP_RIGHTS");
    comm.send();
    ResponderBean response = comm.getResponse();
    if (response!=null){
        if (response.isSuccessfulResponse()){
             int right = Integer.parseInt(response.getId());
                if (right == 1) {
                    //hasRights = true;
                    //modified for case #1860 start 2
                    if(getStatusCode.equals("2") || getStatusCode.equals("4") || getStatusCode.equals("5")
                    || getStatusCode.equals("6") || getStatusCode.equals("7")) {
                        hasRights = true;
                    }
                    //modified for case #1860 end 2
                }
        }
    }
    
    /** Check if the User has VIEW_ANY_PROPOSAL in lead unit of the proposal. 
     *pass the lead unit number of the selected proposal and find
     *whether right is there are not.
     *Call FN_USER_HAS_RIGHT(userId, leadUnit, VIEW_ANY_PROPOSAL) for right
     *checking
     */
    if(!hasRights) {
        connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
        request.setId(unitNumber);
        vecFnParams.addElement(mdiForm.getUserName());
        vecFnParams.addElement(unitNumber);
        vecFnParams.addElement(VIEW_ANY_PROPOSAL_RIGHT);
        request.setFunctionType('b');
        request.setDataObjects(vecFnParams);
        comm.setConnectTo(connectTo);
        comm.setRequest(request);
        comm.send();
        response = comm.getResponse();
        if (response!=null){
            if (response.isSuccessfulResponse()){
                hasRights = ((Boolean) response.getDataObject()).booleanValue();
            }
        }
    }
    /** If the user doesn't find the MODIFY_ANY_PROPOSAL right at the top level,
     *then pass the lead unit of the selected proposal and find
     *whether MODIFY_ANY_PROPOSAL right is there are not.
     *Call FN_USER_HAS_RIGHT(userId, leadUnit, MODIFY_ANY_PROPOSAL) for right
     *checking
     */
    if(!hasRights) {
        connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
        selectedRow = tblProposal.getSelectedRow();
        vecFnParams.setElementAt(MODIFY_ANY_PROPOSAL_RIGHT, 2);
        request.setDataObjects(vecFnParams);
        comm.setConnectTo(connectTo);
        comm.setRequest(request);
        comm.send();
        response = comm.getResponse();
        if (response!=null){
            if (response.isSuccessfulResponse()){
                hasRights = ((Boolean) response.getDataObject()).booleanValue();
            }
        }
    }
    
    /** If the user doesn't find the right at the lead unit level ,
     *then pass the proposal number of the selected proposal and find
     *whether MODIFY_PROPOSAL right is there are not.
     *Call FN_USER_HAS_PROP_RIGHT(userId, proposalNumber, MODIFY_PROPOSAL) for right
     *checking
     */
    if (!hasRights) {
        connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
        selectedRow = tblProposal.getSelectedRow();
        proposalNumber = getSelectedProposalNumber(selectedRow,"PROPOSAL_NUMBER");
        vecFnParams.setElementAt(proposalNumber, 1);
        vecFnParams.setElementAt(MODIFY_RIGHT, 2);
        request.setDataObjects(vecFnParams);
        request.setFunctionType('U');
        comm.setConnectTo(connectTo);
        comm.setRequest(request);
        comm.send();
        response = comm.getResponse();

        if (response!=null){
            if (response.isSuccessfulResponse()){
                hasRights  = ((Boolean) response.getDataObject()).booleanValue();
//                int right = ((Integer) response.getDataObject()).intValue();
//                if (right == 1) {
//                    hasRights = true;
//                }
            }
        }
    }
    //Check if User has MODIFY_NARRATIVE for this particular proposal 
    if(!hasRights) {
        connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
        selectedRow = tblProposal.getSelectedRow();
        vecFnParams.setElementAt(MODIFY_NARRATIVE_RIGHT, 2);
        request.setDataObjects(vecFnParams);
        comm.setConnectTo(connectTo);
        comm.setRequest(request);
        comm.send();
        response = comm.getResponse();
        
        if (response!=null){
            if (response.isSuccessfulResponse()){
                hasRights  = ((Boolean) response.getDataObject()).booleanValue();
//                int right = ((Integer) response.getDataObject()).intValue();
//                if (right == 1) {
//                    hasRights = true;
//                }
            }
        }
    }
    
    //Check if User has MODIFY_BUDGET for this particular proposal 
    if(!hasRights) {
        connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
        selectedRow = tblProposal.getSelectedRow();
        vecFnParams.setElementAt(MODIFY_BUDGET_RIGHT, 2);
        request.setDataObjects(vecFnParams);
        comm.setConnectTo(connectTo);
        comm.setRequest(request);
        comm.send();
        response = comm.getResponse();
        
        if (response!=null){
            if (response.isSuccessfulResponse()){
                hasRights  = ((Boolean) response.getDataObject()).booleanValue();
//                int right = ((Integer) response.getDataObject()).intValue();
//                if (right == 1) {
//                    hasRights = true;
//                }
            }
        }
    }
    
    /** If the user doesn't find the right at the lead unit level ,
     *then pass the proposal number of the selected proposal and find
     *whether VIEW_PROPOSAL right is there are not.
     *Call FN_USER_HAS_PROP_RIGHT(userId, proposalNumber, VIEW_PROPOSAL) for right
     *checking
     */
    if(!hasRights) {
        connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
        selectedRow = tblProposal.getSelectedRow();
        vecFnParams.setElementAt(VIEW_RIGHT, 2);
        request.setDataObjects(vecFnParams);
        comm.setConnectTo(connectTo);
        comm.setRequest(request);
        comm.send();
        response = comm.getResponse();
        
        if (response!=null){
            if (response.isSuccessfulResponse()){
                hasRights  = ((Boolean) response.getDataObject()).booleanValue();
//                int right = ((Integer) response.getDataObject()).intValue();
//                if (right == 1) {
//                    hasRights = true;
//                }
            }
        }
    }
    //IF user has any OSP right, and the proposal status is (2:Approval In Progress,4: Approved,5: Submitted, 
    //6. Post-Submission Approval or 7. Post-Submission Rejection). 
    if(!hasRights) {
        connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
        selectedRow = tblProposal.getSelectedRow();
        //vecFnParams.setElementAt(VIEW_RIGHT, 2);
        String status = getSelectedProposalNumber(selectedRow,"CREATION_STATUS_CODE");
        String statusCode = getProposalStatusDesc(status);
        request.setId(mdiForm.getUserName());
        request.setDataObject("GET_USER_HAS_ANY_OSP_RIGHTS");
        comm.setConnectTo(connectTo);
        comm.setRequest(request);
        comm.send();
        response = comm.getResponse();
        
        if (response!=null){
            if (response.isSuccessfulResponse()){
                int right = Integer.parseInt(response.getId());
                if (right == 1) {
                    if(statusCode.equals("2") || statusCode.equals("4") || statusCode.equals("5")
                    || statusCode.equals("6") || statusCode.equals("7")) {
                        hasRights = true;
                    }
                }
            }
        }
    }
    }catch (Exception ex){
       CoeusOptionPane.showErrorDialog(ex.getMessage());
    }
    
    return hasRights;
}
//bug id 1856 step 3 : end


/*private boolean hasDisplayProposalRights(){
    boolean hasRights = false;
    String proposalNumber;
    Vector vecFnParams = new Vector();
    String topLevelUnit=null;
    String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
    RequesterBean request = new RequesterBean();
    int selectedRow = tblProposal.getSelectedRow();
    String unitNumber = (String)tblProposal.getValueAt(selectedRow, 4);

    request.setId(unitNumber);
    //request.setUserName(mdiForm.getUserName());
    request.setDataObject("FN_GET_CAMPUS_FOR_DEPT");
    AppletServletCommunicator comm
            = new AppletServletCommunicator(connectTo, request);
    comm.send();
    ResponderBean response = comm.getResponse();

    if (response!=null){
        if (response.isSuccessfulResponse()){
            topLevelUnit = (String)response.getDataObject();
        }
    }
    /** Checks if the user has the right at the top level unit,
     *then pass the top level unit of the selected proposal and find
     *whether right is there are not.
     *Call FN_USER_HAS_RIGHT(userId, topLevelUnit, VIEW_ANY_PROPOSAL) for right
     *checking
     */
    /*if(!topLevelUnit.equals(null)) {
      connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
        
        selectedRow = tblProposal.getSelectedRow();
        vecFnParams.addElement(mdiForm.getUserName());
        vecFnParams.addElement(topLevelUnit);
        vecFnParams.addElement(VIEW_ANY_PROPOSAL_RIGHT);
        request.setDataObjects(vecFnParams);  
        request.setFunctionType('b');
        comm.setConnectTo(connectTo);
        comm.setRequest(request);
        comm.send();
        response = comm.getResponse();

        if (response!=null){
            if (response.isSuccessfulResponse()){
                 hasRights = ((Boolean) response.getDataObject()).booleanValue();
            }
        }
    }
    /** If the user doesn't find the right at the top level unit,
     *then pass the lead unit number of the selected proposal and find
     *whether right is there are not.
     *Call FN_USER_HAS_RIGHT(userId, leadUnit, VIEW_ANY_PROPOSAL) for right
     *checking
     */
    /*if(!hasRights) {
      connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
        
        selectedRow = tblProposal.getSelectedRow();
        vecFnParams.setElementAt(unitNumber, 1);
        request.setDataObjects(vecFnParams);
        comm.setConnectTo(connectTo);
        comm.setRequest(request);
        comm.send();
        response = comm.getResponse();  
        if (response!=null){
            if (response.isSuccessfulResponse()){
                 hasRights = ((Boolean) response.getDataObject()).booleanValue();
            }
        }
    }
    /** If the user doesn't find the VIEW_ANY_PROPOSAL right at lead unit level also,
     *then pass the top level unit of the selected proposal and find
     *whether MODIFY_ANY_PROPOSAL right is there are not.
     *Call FN_USER_HAS_RIGHT(userId, topLevelUnit, MODIFY_ANY_PROPOSAL) for right
     *checking
     */
   /* if(!hasRights) {
      connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
        
        selectedRow = tblProposal.getSelectedRow();
        vecFnParams.setElementAt(topLevelUnit, 1);
        vecFnParams.setElementAt(MODIFY_ANY_PROPOSAL_RIGHT, 2);
        request.setDataObjects(vecFnParams);
        comm.setConnectTo(connectTo);
        comm.setRequest(request);
        comm.send();
        response = comm.getResponse();  
        if (response!=null){
            if (response.isSuccessfulResponse()){
                 hasRights = ((Boolean) response.getDataObject()).booleanValue();
            }
        }
    }
    /** If the user doesn't find the MODIFY_ANY_PROPOSAL right at the top level,
     *then pass the lead unit of the selected proposal and find
     *whether MODIFY_ANY_PROPOSAL right is there are not.
     *Call FN_USER_HAS_RIGHT(userId, leadUnit, MODIFY_ANY_PROPOSAL) for right
     *checking
     */
   /* if(!hasRights) {
      connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
        
        selectedRow = tblProposal.getSelectedRow();
        vecFnParams.setElementAt(unitNumber, 1);
        request.setDataObjects(vecFnParams);
        comm.setConnectTo(connectTo);
        comm.setRequest(request);
        comm.send();
        response = comm.getResponse();  
        if (response!=null){
            if (response.isSuccessfulResponse()){
                 hasRights = ((Boolean) response.getDataObject()).booleanValue();
            }
        }
    }
    /** If the user doesn't find the right at the lead unit level ,
     *then pass the proposal number of the selected proposal and find
     *whether VIEW_PROPOSAL right is there are not.
     *Call FN_USER_HAS_PROP_RIGHT(userId, proposalNumber, VIEW_PROPOSAL) for right
     *checking
     */
   /* if(!hasRights) {
        connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
        selectedRow = tblProposal.getSelectedRow();
        proposalNumber = tblProposal.getValueAt(selectedRow, 0).toString();
        vecFnParams.setElementAt(proposalNumber, 1);
        vecFnParams.setElementAt(VIEW_RIGHT, 2);
        request.setDataObjects(vecFnParams);
        request.setFunctionType('U');
        comm.setConnectTo(connectTo);
        comm.setRequest(request);
        comm.send();
        response = comm.getResponse();

        if (response!=null){
            if (response.isSuccessfulResponse()){
                int right = ((Integer) response.getDataObject()).intValue();
                if (right == 1) {
                    hasRights = true;
                }
            }
        } 
    }
    /** If the user doesn't find the right at the lead unit level ,
     *then pass the proposal number of the selected proposal and find
     *whether MODIFY_PROPOSAL right is there are not.
     *Call FN_USER_HAS_PROP_RIGHT(userId, proposalNumber, MODIFY_PROPOSAL) for right
     *checking
     */
    /*if (!hasRights) {
        connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
        
        selectedRow = tblProposal.getSelectedRow();
        vecFnParams.setElementAt(MODIFY_RIGHT, 2);
        request.setDataObjects(vecFnParams);
        comm.setConnectTo(connectTo);
        comm.setRequest(request);
        comm.send();
        response = comm.getResponse();

        if (response!=null){
            if (response.isSuccessfulResponse()){
                int right = ((Integer) response.getDataObject()).intValue();
                if (right == 1) {
                    hasRights = true;
                }
            }
        }
    }

   return hasRights;  
}*/
//Right Checking - step 5 : end
//Commented by shiji for Right Checking - step 6 : start
/**
 * This method is invoked when the user clicks Display in the Edit menu
 * of Proposal module. Checks whether the User has rights to View Proposal
 *
 * @return boolean. true indicates has rights and false indicates no rights.
 */
/*private boolean hasDisplayProposalRights(){

    boolean hasRights = false;
    String proposalNumber;
    Vector vecFnParams = new Vector();
    String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
    RequesterBean request = new RequesterBean();
    
    request.setId(mdiForm.getUserName());
    request.setDataObject("GET_USER_HAS_ANY_OSP_RIGHTS");
    AppletServletCommunicator comm
            = new AppletServletCommunicator(connectTo, request);
    comm.send();
    ResponderBean response = comm.getResponse();

    if (response!=null){
        if (response.isSuccessfulResponse()){
            int right = Integer.parseInt(response.getId());
            if (right == 1) {
                hasRights = true;
            }
        }
    }
    if (!hasRights) {
        connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
        
        int selectedRow = tblProposal.getSelectedRow();
        proposalNumber = tblProposal.getValueAt(selectedRow, 0).toString();
        vecFnParams.addElement(mdiForm.getUserName());
        vecFnParams.addElement(proposalNumber);
        vecFnParams.addElement(VIEW_RIGHT);
        request.setDataObjects(vecFnParams);
        request.setFunctionType('U');
        comm.setConnectTo(connectTo);
        comm.setRequest(request);
        comm.send();
        response = comm.getResponse();

        if (response!=null){
            if (response.isSuccessfulResponse()){
                int right = ((Integer) response.getDataObject()).intValue();
                if (right == 1) {
                    hasRights = true;
                }
            }
        }
    }
    if (!hasRights) {
        connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
        
        int selectedRow = tblProposal.getSelectedRow();
        vecFnParams.setElementAt(MODIFY_RIGHT, 2);
        request.setDataObjects(vecFnParams);
        comm.setConnectTo(connectTo);
        comm.setRequest(request);
        comm.send();
        response = comm.getResponse();

        if (response!=null){
            if (response.isSuccessfulResponse()){
                int right = ((Integer) response.getDataObject()).intValue();
                if (right == 1) {
                    hasRights = true;
                }
            }
        }
    } 
    return hasRights;
}*/
//Right Checking - step 6 : end
/**
 * This method is invoked when the user clicks New in the Edit menu
 * of Proposal module
 *
 * @return Vector which contains the list of Unit Numbers for the loggedin user
 */
private Vector getUserUnits(){

    Vector vecUserUnits = null;
    String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";                                                                
    RequesterBean request = new RequesterBean();
    Vector vecProcParams = new Vector();
    vecProcParams.addElement(mdiForm.getUserName());
    vecProcParams.addElement(CREATE_RIGHT);
    //request.setFunctionType('U');
    request.setDataObjects(vecProcParams);
    request.setDataObject("GET_USER_UNITS");
    AppletServletCommunicator comm
            = new AppletServletCommunicator(connectTo, request);
    comm.send();
    ResponderBean response = comm.getResponse();

    if (response!=null){
        if (response.isSuccessfulResponse()){
            vecUserUnits = (Vector)response.getDataObject();
        }
    }
    return vecUserUnits;
}


/**
 * This method is invoked when the user clicks close in the ToolBar
 * of Proposal module
 */
private void closeProposalBaseWindow() {
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

    /** 
     * This method is called from SaveAs Toolbar Menu Item.
     */
    public void showSaveAsDialog(){
        //SaveAsDialog saveAsDialog = new SaveAsDialog(tblProposal.getModel());
        SaveAsDialog saveAsDialog = new SaveAsDialog(searchWindow.getSearchResults());
    }
    
    public void update(Observable o, Object arg) {
        edu.mit.coeus.utils.SearchColumnIndex searchColumnIndex = new edu.mit.coeus.utils.SearchColumnIndex();
        if( arg instanceof ProposalDevelopmentFormBean ) {
            ProposalDevelopmentFormBean proposalDevelopmentFormBean = 
                (ProposalDevelopmentFormBean)arg;
            String deadlineDate="";
            DateUtils dtUtils = new DateUtils();
            //Code modified for PT ID#3243 - start
            String dateFormat = CoeusServerProperties.getProperty(CoeusPropertyKeys.SEARCH_DATE_FORMAT, SIMPLE_DATE_FORMAT);
            if(proposalDevelopmentFormBean.getDeadLineDate() != null){
                //COEUSQA-1477 Dates in Search Results - Start
                deadlineDate = dtUtils.parseDateForSearchResults(
                    proposalDevelopmentFormBean.getDeadLineDate().toString(), dateFormat);
                //deadlineDate = dtUtils.formatDate(
                //    proposalDevelopmentFormBean.getDeadLineDate().toString(), dateFormat);
                //COEUSQA-1477 Dates in Search Results - End
            }
            //Code modified for PT ID#3243 - end
            //System.out.println("getting pi details");
            getPIDetails( proposalDevelopmentFormBean.getInvestigators() );
            if( ((BaseWindowObservable)o).getFunctionType() == ADD_MODE ||  
                    ((BaseWindowObservable)o).getFunctionType() == COPY_MODE ) {
                HashMap mapData = new HashMap();
                Vector newRowData = buildNewProposalData(searchColumnIndex,proposalDevelopmentFormBean);
                int lastRow = tblProposal.getRowCount();
                newRowData.addElement(""+lastRow);
                if( lastRow > 0 ) {
                    ((DefaultTableModel)tblProposal.getModel()).insertRow(lastRow,newRowData);
                    // Added for COEUSQA-3739 : Proposal Dev search screen displays duplicate PD numbers - Need to Fix - Start
                    // When the 1st row proposal number matches with the updated proposal number, then first row is selected,
                    // else last row in the list is selected, this is applicable only for new proposal's
                    String proposalNumFromTable = (String)tblProposal.getValueAt(0,0);
                    String proposalNumber = proposalDevelopmentFormBean.getProposalNumber();
                    if(proposalNumber.equals(proposalNumFromTable)){
                        tblProposal.setRowSelectionInterval(0,0);    
                    }else{
                        tblProposal.setRowSelectionInterval(lastRow,lastRow);
                    }
                    // Added for COEUSQA-3739 : Proposal Dev search screen displays duplicate PD numbers - Need to Fix - End
                    
                }else{
                    ((DefaultTableModel)tblProposal.getModel()).insertRow(0,newRowData);
                    tblProposal.setRowSelectionInterval(0,0);
                }
                // Added by chandra. This has to select the row which is added and
                // presently selected row - 7th July 2004
                // Commented for COEUSQA-3739 : Proposal Dev search screen displays duplicate PD numbers - Need to Fix - Start
//                baseTableRow = lastRow;
                // Commented for COEUSQA-3739 : Proposal Dev search screen displays duplicate PD numbers - Need to Fix - End
                // end chandra 7th July 2004
               
            //Code commented for PT ID#3243, setting of new values back to list window made mandatory even in ADD mode
            }else{
                ((DefaultTableModel)tblProposal.getModel()).setValueAt(
                    proposalDevelopmentFormBean.getProposalNumber(),baseTableRow,
                        searchColumnIndex.getSearchColumnIndex(searchWindow,"PROPOSAL_NUMBER"));
                ((DefaultTableModel)tblProposal.getModel()).setValueAt(
                    proposalDevelopmentFormBean.getProposalTypeDesc(),baseTableRow,
                        searchColumnIndex.getSearchColumnIndex(searchWindow,"TYPE"));
                ((DefaultTableModel)tblProposal.getModel()).setValueAt(
                   ""+proposalDevelopmentFormBean.getCreationStatusDescription(),baseTableRow,
                    searchColumnIndex.getSearchColumnIndex(searchWindow,"CREATION_STATUS_CODE"));
                ((DefaultTableModel)tblProposal.getModel()).setValueAt(
                    proposalDevelopmentFormBean.getTitle(),baseTableRow,
                        searchColumnIndex.getSearchColumnIndex(searchWindow,"TITLE"));

                ((DefaultTableModel)tblProposal.getModel()).setValueAt(
                    proposalDevelopmentFormBean.getOwnedBy(),baseTableRow,
                        searchColumnIndex.getSearchColumnIndex(searchWindow,"UNIT_NUMBER"));
                ((DefaultTableModel)tblProposal.getModel()).setValueAt(
                    proposalDevelopmentFormBean.getOwnedByDesc(),
                        baseTableRow,searchColumnIndex.getSearchColumnIndex(searchWindow,"UNIT_NAME"));
                ((DefaultTableModel)tblProposal.getModel()).setValueAt(PIName,baseTableRow,
                    searchColumnIndex.getSearchColumnIndex(searchWindow,"PERSON_NAME"));

                //get the PI and lead unit name/number
                ((DefaultTableModel)tblProposal.getModel()).setValueAt(deadlineDate,baseTableRow,
                    searchColumnIndex.getSearchColumnIndex(searchWindow,"DEADLINE_DATE"));

                ((DefaultTableModel)tblProposal.getModel()).setValueAt(
                    proposalDevelopmentFormBean.getSponsorCode(),baseTableRow,
                        searchColumnIndex.getSearchColumnIndex(searchWindow,"SPONSOR_CODE"));
                ((DefaultTableModel)tblProposal.getModel()).setValueAt(
                    proposalDevelopmentFormBean.getSponsorName(),
                        baseTableRow,searchColumnIndex.getSearchColumnIndex(searchWindow,"SPONSOR_NAME"));
                // 4083: Multiple rows for same proposal # showing up in proposal development list when saving - Start
//                int selRow = tblProposal.getSelectedRow();
//                if(  selRow != -1 ) {
//                    // Bug fix #1434
////                    baseTableRow = Integer.parseInt((String)tblProposal.getValueAt(
////                    selRow,tblProposal.getColumnCount()-1));
//                    // Bug fix #1434
//                    baseTableRow = selRow;
//                }
                tblProposal.setRowSelectionInterval(baseTableRow, baseTableRow);
                // 4083: Multiple rows for same proposal # showing up in proposal development list when saving - End
                //tblProposal.setRowSelectionInterval(rowNum, rowNum );
            }
        }
        
    }
   
    /** Build the new proposal data based on the search column index
     *@param columnIndex, proposalDevelopmentFormBean
     */
    private Vector buildNewProposalData(SearchColumnIndex searchColumnIndex,
    ProposalDevelopmentFormBean proposalDevelopmentFormBean) {
        String deadlineDate="";
        Vector data = new Vector();
        try{
            DateUtils dtUtils = new DateUtils();
            if(proposalDevelopmentFormBean.getDeadLineDate() != null){
                deadlineDate = dtUtils.formatDate(
                proposalDevelopmentFormBean.getDeadLineDate().toString(),"dd-MMM-yyyy");
            }
            java.util.WeakHashMap mapData = new java.util.WeakHashMap();
            mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
            searchWindow,"PROPOSAL_NUMBER")), proposalDevelopmentFormBean.getProposalNumber());
            mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
            searchWindow,"TYPE")),proposalDevelopmentFormBean.getProposalTypeDesc());
            mapData.put(new Integer(searchColumnIndex.getSearchColumnIndex(
            searchWindow,"CREATION_STATUS_CODE")),""+proposalDevelopmentFormBean.getCreationStatusDescription() );
            mapData.put( new Integer(searchColumnIndex.getSearchColumnIndex(
            searchWindow,"TITLE")),proposalDevelopmentFormBean.getTitle());
            mapData.put( new Integer(searchColumnIndex.getSearchColumnIndex(
            searchWindow,"UNIT_NUMBER")),proposalDevelopmentFormBean.getOwnedBy());
            mapData.put( new Integer(searchColumnIndex.getSearchColumnIndex(
            searchWindow,"UNIT_NAME")),proposalDevelopmentFormBean.getOwnedByDesc() );
            mapData.put( new Integer(searchColumnIndex.getSearchColumnIndex(
            searchWindow,"PERSON_NAME")),PIName);
            mapData.put( new Integer(searchColumnIndex.getSearchColumnIndex(
            searchWindow,"DEADLINE_DATE")),deadlineDate );
            mapData.put( new Integer(searchColumnIndex.getSearchColumnIndex(
            searchWindow,"SPONSOR_CODE")), proposalDevelopmentFormBean.getSponsorCode());
            mapData.put( new Integer(searchColumnIndex.getSearchColumnIndex(
            searchWindow,"SPONSOR_NAME")), proposalDevelopmentFormBean.getSponsorName() );
            java.util.ArrayList keySet = new java.util.ArrayList(mapData.keySet());
            java.util.Collections.sort(keySet);
            java.util.Iterator iterator = keySet.iterator();
            while(iterator.hasNext()){
                data.add(mapData.get(iterator.next()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }
  
    /** This method is used to get the Primary Investigator details from the
     * collection of Investigators of this Proposal, which will be used to
     * populate the details in the <CODE>ProposalBaseWindow</CODE>.
     *
     * @param investigators Collection of <CODE>ProposalInvestigatorFormBean</CODE>s of a
     * Proposal.
     */
    public void getPIDetails(Vector investigators ){
        int count = ( investigators == null ? 0 : investigators.size() );
        // 4083: Multiple rows for same proposal # showing up in proposal development list when saving - Start
        if(count == 0){
            PIName = "";
        } else {
            // 4083: Multiple rows for same proposal # showing up in proposal development list when saving - End
            for (int i=0; i<count;i++) {
                ProposalInvestigatorFormBean investigatorBean
                        = (ProposalInvestigatorFormBean) investigators.elementAt(i);
                if (investigatorBean.isPrincipleInvestigatorFlag()) {
                    PIName = investigatorBean.getPersonName();
                    break;
                }
            }
        }
    }
     public void saveAsActiveSheet() {
        SaveAsDialog saveAsDialog = new SaveAsDialog(tblProposal);
    }
     
    /* Added by chandra - 10/02/2004
     *To get proposalz status code and descrtiption of the proposal. Get the vector of 
     *combo box beans and add check for the values. Making server call to get the code and 
     *description
     */ 
     private Vector getProposalStatus() throws CoeusClientException{
        Vector vecStatusDetails=null;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(PROPOSAL_STATUS);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            vecStatusDetails = response.getDataObjects();
            return vecStatusDetails;
        }else {
            throw new CoeusClientException(response.getMessage());
        }
     }// End of server call.....................chandra - 10/02/2004
     
     
     /** Added by chandra 10/02/2004 - 
      *Get the proposal status code by passing the prosposal status description.
      *get the id and desc for the proposal status.
      */
      private String getProposalStatusDesc(String proposalStatusDesc){
          String proposalStatusCode = "";
          try{
            vecProposalStatus = getProposalStatus();
            int statusSize = vecProposalStatus .size();
            for(int index = 0 ; index < statusSize ; index++){
                ComboBoxBean bean = (ComboBoxBean)vecProposalStatus.elementAt(index);
                if (bean.getDescription().equalsIgnoreCase(proposalStatusDesc)){
                    proposalStatusCode = bean.getCode();
                    break;
                }
            }
          }catch(CoeusClientException coeusClientException){
              coeusClientException.printStackTrace();
          }
         return proposalStatusCode;
    }// End of getProposalStatusDesc.....................chandra - 10/02/2004

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
      
      class CustomMouseAdapter extends MouseAdapter {
          public void mouseClicked(MouseEvent me) {
              //Added by Nadh to get the column header and its status Start 18-01-2005
              if(me.getSource() instanceof JTableHeader) {
                  JTableHeader tblHeader = (JTableHeader) me.getSource();
                  TableColumnModel columnModel = tblHeader.getColumnModel();
                  int viewColumn = columnModel.getColumnIndexAtX(me.getX());
                  int column = columnModel.getColumn(viewColumn).getModelIndex();
                  int sortStatus = getStatus();
                  if(oldCol != column )
                      sortStatus = MultipleTableColumnSorter.NOT_SORTED;
                  sortStatus = sortStatus + (me.isShiftDown() ? -1 : 1);
                  sortStatus = (sortStatus + 4) % 3 - 1;
                  setStatus(sortStatus);
                  oldCol = column;
                  if(getStatus()==MultipleTableColumnSorter.ASCENDING || getStatus() == MultipleTableColumnSorter.DESCENDING) {
                      Vector newSortedData = new Vector();
                      newSortedData.addElement(tblProposal.getColumnName(column));
                      newSortedData.addElement(new Integer(column));
                      newSortedData.addElement(new Boolean(status == 1 ? true : false));
                      if(vecSortedData == null)
                          vecSortedData = new Vector();
                      vecSortedData.removeAllElements();
                      vecSortedData.addElement(newSortedData);
                  }else {
                      vecSortedData = null;
                  }
              }//End Nadh
              if(!(me.getSource() instanceof JTableHeader)) {
                  if (me.getClickCount() == 2) {
                      try {
                          int selectedRow = tblProposal.getSelectedRow();
                          String proposalNumber = getSelectedProposalNumber(selectedRow, "PROPOSAL_NUMBER");
                          showProposalDetails(proposalNumber);
                      } catch (Exception e) {
                          e.printStackTrace();
                      }
                  }
              }
          }
      }
      //Added for COEUSQA-1579 : For Hierarchy Proposal, Approval in Progress, cannot sync after narratives updated on child proposal - Start
      /*
       * Method to get the proposal status code
       * @param proposalNumber
       * @return proposalStatusCode
       */
      private int getProposalStatusCode(String proposalNumber)throws Exception{
          int proposalStatusCode = 0;
          String connectTo = CoeusGuiConstants.CONNECTION_URL +PROPOSAL_SERVLET;
          RequesterBean request = new RequesterBean();
          request.setFunctionType(PROPOSAL_CREATION_STATUS_CODE);
          request.setDataObject(proposalNumber);
          AppletServletCommunicator comm
                  = new AppletServletCommunicator( connectTo, request );
          comm.send();
          ResponderBean response = comm.getResponse();
          if (response == null) {
              response = new ResponderBean();
              response.setResponseStatus(false);
          }
          if (response.isSuccessfulResponse()) {
              proposalStatusCode = ((Integer)response.getDataObject()).intValue();
          }else{
              throw new CoeusException(response.getMessage());
          }
          
          return proposalStatusCode;
      }
      //COEUSQA-1579 : End
        /*Added for Case# COEUSQA-1675 -ability to delete Proposal Development proposals - Start*/    
    /** 
     * This method is used to perform Delete on the given proposalNumber 
     * @param String proposalNumber, int selectedRow
     * @throws Exception
     */
    
      private void performDelete(String proposalNumber, int selectedRow)throws Exception{
//Commented for COEUSQA-2548_Delete Proposal action issue with unlinked hierarchy proposal requested from same Proposal List window_Start          
//          String isPropInHierarchy = getSelectedProposalNumber(selectedRow, "IS_PROP_IN_HIERARCHY");
//Commented for COEUSQA-2548_Delete Proposal action issue with unlinked hierarchy proposal requested from same Proposal List window_end
          if (proposalNumber == null) {
              log(coeusMessageResources.parseMessageKey(
                      "deleteProposal_exceptionCode.1003"));
          } else {
//Modified for COEUSQA-2548_Delete Proposal action issue with unlinked hierarchy proposal requested from same Proposal List window_Start
//              getDeleteRights(proposalNumber,selectedRow);
              getDeleteRightsAndHeirarchyDetails(proposalNumber,selectedRow);
//Modified for COEUSQA-2548_Delete Proposal action issue with unlinked hierarchy proposal requested from same Proposal List window_End
              //Cheked whether u have right to delete and proposal in in progress
              if(isAuthorised){
                  if(isPropInHierarchy != null && "Y".equalsIgnoreCase(isPropInHierarchy)){
                      CoeusOptionPane.showWarningDialog(
                              coeusMessageResources.parseMessageKey("deleteProposal_exceptionCode.1006"));
                  }else{
                      int opt = CoeusOptionPane.DEFAULT_NO;
                      opt = CoeusOptionPane.showQuestionDialog(
                              coeusMessageResources.parseMessageKey("deleteProposal_exceptionCode.1001"),
                              CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                      if( opt == CoeusOptionPane.SELECTION_YES ) {
                          deleteProposal(proposalNumber,selectedRow);
                      }
                  }
              } else{
                  CoeusOptionPane.showWarningDialog(
                          coeusMessageResources.parseMessageKey("deleteProposal_exceptionCode.1007"));
              }
          }
      }
      

    /**
     * This method is used for deleting the Proposal,
     * if the Proposal is not deleted, then an error message is thrown
     * if the Proposal is successfully deleted, then the Proposal list table is refreshed
     * @param  String ProposalNumber,int selectedRow 
     * @throws Exception 
     */
    private void deleteProposal(String proposalNumber,int selectedRow)throws Exception{
        boolean deleteProposal = false;
        String connectTo = CoeusGuiConstants.CONNECTION_URL+ PROPOSAL_SERVLET;
        Vector vecProcParams = new Vector();
        RequesterBean request = new RequesterBean();
        request.setFunctionType(DELETE_PROPOSAL);
        request.setId(proposalNumber);
        String unitNumber = getSelectedProposalNumber(selectedRow, "UNIT_NUMBER");
        vecProcParams.addElement(unitNumber);
        request.setDataObjects(vecProcParams);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            if (response.isSuccessfulResponse()){
                deleteProposal = ((Boolean)response.getDataObject()).booleanValue();
            }
            if(!deleteProposal){
                String errMsg = response.getMessage();
                if(errMsg != null){
                    CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(errMsg));
                }
            }else{
                ((DefaultTableModel)tblProposal.getModel()).removeRow(selectedRow);
                int rowCount = tblProposal.getRowCount();
                ((DefaultTableModel)tblProposal.getModel()).fireTableRowsDeleted( 0, rowCount);
                if(rowCount > 1){
                    tblProposal.setRowSelectionInterval(0,0);
                }
            }
        }
    }
    
    /**
     * To get the delete rights of the user for the given proposal number
     * @param String proposalNumber
     * @throws Exception 
     */
//Commented and Added for COEUSQA-2548_Delete Proposal action issue with unlinked hierarchy proposal requested from same Proposal List window_start
//    private void getDeleteRights(String proposalNumber, int selectedRow) +throws Exception{
     private void getDeleteRightsAndHeirarchyDetails(String proposalNumber, int selectedRow) throws Exception{
//Commented and Added for COEUSQA-2548_Delete Proposal action issue with unlinked hierarchy proposal requested from same Proposal List window_end         
        String connectTo = CoeusGuiConstants.CONNECTION_URL+ PROPOSAL_SERVLET;
        RequesterBean request = new RequesterBean();
        Vector vecProcParams = new Vector();
        request.setFunctionType(GET_DELETE_RIGHTS);
        request.setId(proposalNumber);
        String unitNumber = getSelectedProposalNumber(selectedRow, "UNIT_NUMBER");
        vecProcParams.addElement(unitNumber);
        request.setDataObjects(vecProcParams);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response!= null){
            //Commented and Added for COEUSQA-2548_Delete Proposal action issue with unlinked 
            //hierarchy proposal requested from same Proposal List window_start
//            isAuthorised = ((Boolean)response.getDataObject()).booleanValue();
            isAuthorised = new Boolean(response.getDataObjects().get(0).toString());
            isPropInHierarchy = (String)response.getDataObjects().get(1);
            //Commented and Added for COEUSQA-2548_Delete Proposal action issue with unlinked 
            //hierarchy proposal requested from same Proposal List window_End
        }
    }
    /*Added for Case# COEUSQA-1675 -ability to delete Proposal Development proposals - End*/
}

