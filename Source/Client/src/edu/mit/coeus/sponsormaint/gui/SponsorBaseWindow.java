/*
 * @(#)SponsorBaseWindow.java 1.0 10/09/02 3:03 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 *
 */

/* PMD check performed, and commented unused imports and variables on 20-APR-2011
 * by Bharati
 */

package edu.mit.coeus.sponsormaint.gui;

import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.sponsormaint.gui.SponsorMaintenanceForm;
import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceFormBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.BaseWindowObservable;
import edu.mit.coeus.utils.MultipleTableColumnSorter;
import edu.mit.coeus.utils.SortForm;
import edu.mit.coeus.utils.saveas.SaveAsDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.util.Vector;
//import java.util.Observable;
import java.util.Observer;

//import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

/**
 * This class is used to construct the parent window for SponsorMaintenance
 * and associate its menu and toolbar with listener. This window will display 
 * the Sponsor details from which the user can select the sponsor. 
 * This class calls CoeusMenu and CoeusToolBar factory for construction of
 * menu and toolbar, and CoeusSearch for creating the sponsor table.
 *
 * @author  MukundanC
 * @version 1.0 October 09, 2002, 3:03 PM
 */

public class SponsorBaseWindow extends CoeusInternalFrame
                                        implements ActionListener,Observer {
                                            
    //Added by Chandra - To check whether the right is there or not..call server
    private Vector vecRights = new Vector();
    
    private static final String MAINTAIN_SPONSOR_FORMS = "MAINTAIN_SPONSOR_FORMS";
    private static final String ADD_SPONSOR = "ADD_SPONSOR";
    private static final String MAINTAIN_SPONSOR = "MAINTAIN_SPONSOR";
    private static final char SPONSOR_RIGHT_TYPE = 'Y' ;
    // End Chandra                                        

    // Menu items for Sponsor
    private CoeusMenuItem mnItAddSponsor,mnItModifySponsor,
        mnItDisplaySponsor,mnItDeleteSponsor,mnItSponsorForms,mnItSponsorSearch;

    // Toolbar for Sponsor
    private CoeusToolBarButton btnAddSponsor;
    private CoeusToolBarButton btnModifySponsor;
    private CoeusToolBarButton btnDisplaySponsor;
    private CoeusToolBarButton btnDeleteSponsor;
    private CoeusToolBarButton btnSortSponsor;
    private CoeusToolBarButton btnSaveSponsor;
    private CoeusToolBarButton btnSearchSponsor;
    private CoeusToolBarButton btnClosedSponsor;

    // search name for sponsor module
    private final String SPONSOR_SEARCH = "sponsorSearch";
    // title for Base window for sponsor
    private final String TITLE_SPONSOR = "Sponsor Maintenance";
    // title for Add sponsor dialog screen
    private final String ADD_TITLE  = "Add New Sponsor";
    // title for Modify sponsor dialog screen
    private final String MODIFY_TITLE = "Modify Sponsor";
    // title for Display sponsor dialog screen
    private final String DISPLAY_TITLE = "Display Sponsor";
    
    //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
    private final String SPONSOR_STATUS_SEARCH = "SPONSORSTATUSSEARCH";
    private final String ACTIVE = "Active";
    private final String ACTIVE_STATUS = "A";
    private final String INACTIVE = "Inactive";
    private final String INACTIVE_STATUS = "I";
    //Constants for sponsor_colummn_names 
    private static final int SPONSOR_SPONSOR_CODE_COLUMN_INDEX = 0;
    private static final int SPONSOR_SPONSOR_NAME_COLUMN_INDEX = 1;
    private static final int SPONSOR_ACRONYM_COLUMN_INDEX = 2;
    private static final int SPONSOR_SPONSOR_TYPE_COLUMN_INDEX = 3;
    private static final int SPONSOR_STATUS_COLUMN_INDEX = 4;
    private static final int SPONSOR_POSTAL_CODE_COLUMN_INDEX = 5;
    private static final int SPONSOR_STATE_COLUMN_INDEX = 6;
    private static final int SPONSOR_STATE_NAME_COLUMN_INDEX = 7;
    private static final int SPONSOR_COUNTRY_NAME_COLUMN_INDEX = 8;
    private static final int SPONSOR_AUDIT_REPORT_COLUMN_INDEX = 9;
    private static final int SPONSOR_DUN_AND_BRADSTREET_COLUMN_INDEX = 10;
    private static final int SPONSOR_DUNS_PLUS_FOUR_COLUMN_INDEX = 11;
    private static final int SPONSOR_DODAC_NUMBER_COLUMN_INDEX = 12;
    private static final int SPONSOR_CAGE_NUMBER_COLUMN_INDEX = 13;
    private static final int SPONSOR_OWNED_BY_UNIT_COLUMN_INDEX = 14;
    //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end

    private SponsorMaintenanceForm frmSponsor;
    private SponsorMaintenanceFormBean spBean;
    private RolodexDetailsBean rolDetailBean;
    private String connectionURL ="";
    private AppletServletCommunicator comm;
    private CoeusSearch coeusSearch;
    private JTable tblSearchResultsTable;
    private RequesterBean requester;
    private ResponderBean response;
    private Vector responseData = new Vector();
    private String sponsorCode ="";
    private CoeusMessageResources coeusMessageResources;
    /**
     * Connection String to the Server Side will connect to sponsorservlet.
     */
    private final String CONNECT_TO =
        CoeusGuiConstants.CONNECTION_URL + "/spMntServlet";

    //Main MDI Form.
    private CoeusAppletMDIForm mdiForm = null;
    //Scroll bar pane
    private JScrollPane scrPnSearchRes ;
    private int baseTableRow;
    
    //holds sorted columns and its states
    private Vector vecSortedData;
    
    private static final int OK_CLICKED = 0;
    
    //holds column index
    private int oldCol = -1;
    
    //holds column status
    private int status=MultipleTableColumnSorter.NOT_SORTED;

    /**
     * Constructor to create Sponsor base window
     *
     * @param mdiForm Coeus MDI form name
     * @exception Exception if any error
     */
    public SponsorBaseWindow(CoeusAppletMDIForm mdiForm)  throws Exception {
        super(CoeusGuiConstants.SPONSOR_FRAME_TITLE, mdiForm,LIST_MODE);
        this.mdiForm = mdiForm;
        initComponents();
        mdiForm.putFrame(CoeusGuiConstants.SPONSOR_FRAME_TITLE, this);
        mdiForm.getDeskTopPane().add(this);
        this.setVisible(true);
        showSearchWindow();
    }

    /**
     * Constructor to create Sponsor base window
     *
     * @param frameName internal frame name where the Sponsor list will be displayed
     * @param mdiForm Coeus MDI form name
     * @exception Exception if any error
     */
    public SponsorBaseWindow(String frameName, CoeusAppletMDIForm mdiForm)
                            throws Exception {
        super(frameName, mdiForm,LIST_MODE);
        this.mdiForm = mdiForm;
        initComponents();
        showSearchWindow();
    }

    /**
     * Construct the components for the Sponsor base window
     * @exception Exception if any error
     */
    private void initComponents() throws Exception {
        setFrameMenu(getSponsorEditMenu());
        setToolsMenu(getSponsorToolsMenu());
        setFrameToolBar(getSponsorToolBar());
        setFrame(TITLE_SPONSOR);
        setFrameIcon(mdiForm.getCoeusIcon());
        createSponsorTable();
        
        vecRights.add(MAINTAIN_SPONSOR_FORMS);
        vecRights.add(ADD_SPONSOR);
        vecRights.add(MAINTAIN_SPONSOR);
        
        // Added by chandra Starts- 17 Jan 2004 - Check for the user right
        Vector vecSponsorRightDetaills = checkUserRight();
        boolean hasSponsorForm = ((Boolean) vecSponsorRightDetaills.get(0)).booleanValue();
        boolean hasSponsorRight = ((Boolean) vecSponsorRightDetaills.get(1)).booleanValue();
        mnItSponsorForms.setEnabled(hasSponsorForm);
        btnAddSponsor.setEnabled(hasSponsorRight);
        mnItAddSponsor.setEnabled(hasSponsorRight);
        // Added by chandra Ends - 17 Jan 2004 - Check for the user right
        coeusMessageResources = CoeusMessageResources.getInstance();
    }

    /**
     * This used create the Table for base window for sponsor module
     * @exception Exception if any error
     */
    private void createSponsorTable() throws Exception{
        //Modified for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
        //coeusSearch = new CoeusSearch(mdiForm, SPONSOR_SEARCH, 0);
        coeusSearch = new CoeusSearch(mdiForm,SPONSOR_STATUS_SEARCH, 0);
        //modified  for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
        tblSearchResultsTable= coeusSearch.getEmptyResTable();
        javax.swing.table.TableColumn clmName
        = tblSearchResultsTable.getColumnModel().getColumn(
        tblSearchResultsTable.getColumnCount()-1);
        clmName.setPreferredWidth(0);
        clmName.setMaxWidth(0);
        clmName.setMinWidth(0);
        clmName.setWidth(0);
        javax.swing.table.JTableHeader header
        = tblSearchResultsTable.getTableHeader();
        //header.setResizingAllowed(false);
        header.setReorderingAllowed(false);
        setSearchResultsTable(tblSearchResultsTable);
        scrPnSearchRes = new JScrollPane();
        scrPnSearchRes.setMinimumSize(new Dimension(22, 15));
        //scrPnSearchRes.setPreferredSize(new Dimension(600, 400));
        if( tblSearchResultsTable != null ){
            tblSearchResultsTable.setFont( CoeusFontFactory.getNormalFont() );
        }
        scrPnSearchRes.setViewportView(tblSearchResultsTable);
        tblSearchResultsTable.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent me){
                if (me.getClickCount() == 2) {
                    try{
                        int selectedRow =
                        tblSearchResultsTable.getSelectedRow();
                        String sponsorCode =
                        tblSearchResultsTable.getValueAt(
                        selectedRow,0).toString();
                        SponsorMaintenanceForm  frmSponsor =
                        new SponsorMaintenanceForm('D',sponsorCode);
                        frmSponsor.showForm(mdiForm,DISPLAY_TITLE,true);
                    }
                    catch(Exception e){
                        CoeusOptionPane.showInfoDialog(e.getMessage());
                    }
                }
            }
        });
        
        scrPnSearchRes.getViewport().setBackground(Color.white);
        scrPnSearchRes.setForeground(Color.white);
        getContentPane().add(scrPnSearchRes);
    }

    /**
     * constructs Sponsor edit menu with sub menu Add,Modify,Dispaly,Delete and
     * Sponsor Forms.
     *
     * @return CoeusMenu Sponsor edit menu
     */
    private CoeusMenu getSponsorEditMenu() {
        CoeusMenu mnuSponsor = null;
        Vector fileChildren = new Vector();

        mnItAddSponsor = new CoeusMenuItem("Add",null,true,true);
        mnItAddSponsor.addActionListener(this);
        mnItAddSponsor.setMnemonic('A');

        mnItModifySponsor = new CoeusMenuItem("Modify",null,true,true);
        mnItModifySponsor.setMnemonic('M');
        mnItModifySponsor.addActionListener(this);

        mnItDisplaySponsor = new CoeusMenuItem("Display",null,true,true);
        mnItDisplaySponsor.setMnemonic('i');
        mnItDisplaySponsor.addActionListener(this);

        mnItDeleteSponsor = new CoeusMenuItem("Delete",null,true,true);
        mnItDeleteSponsor.setMnemonic('d');
        mnItDeleteSponsor.addActionListener(this);

        mnItSponsorForms = new CoeusMenuItem("Sponsor Forms...",null,true,true);
        mnItSponsorForms.setMnemonic('f');
        mnItSponsorForms.addActionListener(this);

        /* Adding the sub menu with listener to the vector */
        fileChildren.add(mnItAddSponsor);
        fileChildren.add(mnItModifySponsor);
        fileChildren.add(mnItDisplaySponsor);
        fileChildren.add(mnItDeleteSponsor);
        fileChildren.add(mnItSponsorForms);

        /* the vector of sub menu is added to main menu */
        mnuSponsor = new CoeusMenu("Edit",null,fileChildren,true,true);
        mnuSponsor.setMnemonic('E');
        return mnuSponsor;
    }

    /**
     * constructs Sponsor Tools menu for search screen with sub menu of Search
     *
     * @return CoeusMenu Sponsor tools menu
     */
    public CoeusMenu getSponsorToolsMenu(){
        CoeusMenu coeusMenu;
        Vector fileChildren = new Vector();
        mnItSponsorSearch = new CoeusMenuItem("Search",null,true,true);
        mnItSponsorSearch.setMnemonic('S');
        mnItSponsorSearch.addActionListener(this);
        fileChildren.add(mnItSponsorSearch);
        coeusMenu = new CoeusMenu("Tools",null,fileChildren,true,true);
        coeusMenu.setMnemonic('T');
        return coeusMenu;
    }

    /**
     * Sponsor ToolBar is a which provides the Icons for Performing
     * Add, Mofify,Display,Delete,Search and  Close buttons.
     * CoeusToolBarButton is used to provide the image and tooltips
     * is provided for the buttons.
     *
     * @returns JToolBar Sponsor Toolbar
     */
    private JToolBar getSponsorToolBar() {
        JToolBar tlBrSponsorToolbar = new JToolBar();

        btnAddSponsor = new CoeusToolBarButton(new ImageIcon(
                    getClass().getClassLoader().getResource(CoeusGuiConstants.ADD_ICON)),
                        null,"Add Sponsor");
        btnModifySponsor = new CoeusToolBarButton(new ImageIcon(
                    getClass().getClassLoader().getResource(CoeusGuiConstants.EDIT_ICON)),
                        null,"Edit Sponsor");
        btnDisplaySponsor = new CoeusToolBarButton(new ImageIcon(
                    getClass().getClassLoader().getResource(CoeusGuiConstants.DISPLAY_ICON)),
                        null,"Display Sponsor");
        btnDeleteSponsor = new CoeusToolBarButton(new ImageIcon(
                    getClass().getClassLoader().getResource(CoeusGuiConstants.DELETE_ICON)),
                        null,"Delete Sponsor");
        btnSortSponsor = new CoeusToolBarButton(new ImageIcon(
                    getClass().getClassLoader().getResource(CoeusGuiConstants.SORT_ICON)),
                        null,"Sort Sponsor Maintenance");
        btnSearchSponsor = new CoeusToolBarButton(new ImageIcon(
                    getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)),
                            null,"Search");
        btnSaveSponsor = new CoeusToolBarButton(new ImageIcon(
                    getClass().getClassLoader().getResource(CoeusGuiConstants.SAVEAS_ICON)),
                            null,"Save As");
        btnClosedSponsor = new CoeusToolBarButton(new ImageIcon(
                    getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
                            null,"Close");

        /* adding listener to the tool bars */
        btnAddSponsor.addActionListener(this);
        btnModifySponsor.addActionListener(this);
        btnDisplaySponsor.addActionListener(this);
        btnDeleteSponsor.addActionListener(this);
        btnSearchSponsor.addActionListener(this);
        btnClosedSponsor.addActionListener(this);
        btnSaveSponsor.addActionListener(this);
        btnSortSponsor.addActionListener(this);
        /* adding to the tool bar */
        tlBrSponsorToolbar.add(btnAddSponsor);
        tlBrSponsorToolbar.add(btnModifySponsor);
        tlBrSponsorToolbar.add(btnDisplaySponsor);
        tlBrSponsorToolbar.add(btnDeleteSponsor);
        tlBrSponsorToolbar.add(btnSortSponsor);
        tlBrSponsorToolbar.add(btnSearchSponsor);
        tlBrSponsorToolbar.add(btnSaveSponsor);
        tlBrSponsorToolbar.addSeparator();
        tlBrSponsorToolbar.add(btnClosedSponsor);

        tlBrSponsorToolbar.setFloatable(false);
        return tlBrSponsorToolbar;
    }

    /**
     * Action while firing the events for menu and toolbar items
     * 
     * @param actionType ActionEvent
     */
    public void actionPerformed(ActionEvent actionType) {
        try{
        Object actSource = actionType.getSource();
        /* when the menu or tool bar add is clicked mnAddSponsor method will be fired*/
        if (actSource.equals(mnItAddSponsor) || actSource.equals(btnAddSponsor)) {
            addSponsor();
        /* when the menu or tool bar modify is clicked modifySponsor method will
         * be fired
         */
        } else if (actSource.equals(mnItModifySponsor) ||
                                         actSource.equals(btnModifySponsor)) {
            modifySponsor();
       /* when the menu or tool bar display is clicked displaySponsor method will
        * be fired
        */
        } else if (actSource.equals(mnItDisplaySponsor) ||
                                        actSource.equals(btnDisplaySponsor)) {
            displaySponsor();
       /* when the menu or tool bar delete is clicked deleteSponsor method will
        * be fired
        */
        } else if (actSource.equals(mnItDeleteSponsor) ||
                                         actSource.equals(btnDeleteSponsor)) {
            deleteSponsor();
        /* when the tool bar close is clicked frame will be closed */
        } else if (actSource.equals(btnClosedSponsor)) {
            if(this!=null){
                this.doDefaultCloseAction();
            }
        } else if (actSource.equals(btnSaveSponsor)) {
            //saveSponsor();
            //new implementation
            saveAsActiveSheet();
        }
        
        else if(actSource.equals(mnItSponsorForms)){
            showSponsorMaintainance();
        }
       
        else if (actSource.equals(btnSortSponsor)) {
            showSort();
        }

       /* when the tools menu or tool bar Search is clicked showSearchWindow
        * method will be fired will bring the search screen.
        */
        if (actSource.equals(mnItSponsorSearch) ||
                                        actSource.equals(btnSearchSponsor))  {
            try{
                showSearchWindow();
            }catch(Exception ex){
                log(ex.getMessage());
            }
        }
        }catch(Exception e){
            e.printStackTrace();
            log(e.getMessage());
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
        SortForm sortForm = new SortForm(tblSearchResultsTable,vecSortedData);
        Vector sortedData = sortForm.display();
        vecSortedData = (Vector)sortedData.get(1);
        if(((Integer)sortedData.get(0)).intValue() == OK_CLICKED)
            coeusSearch.sortByColumns(tblSearchResultsTable,vecSortedData);
        else
            return;
    }// end Nadh
    
    private void showSponsorMaintainance(){
        SponsorFormMaintainance  sponsorFormMaintainance=null;
        int selRow = tblSearchResultsTable.getSelectedRow();
        if(selRow!=-1){
            String sponsorCode = (String)tblSearchResultsTable.getValueAt(selRow, 0);
            String sponsorName = (String)tblSearchResultsTable.getValueAt(selRow, 1);
            sponsorFormMaintainance = new SponsorFormMaintainance();
            sponsorFormMaintainance.setSponsorCode(sponsorCode);
            sponsorFormMaintainance.setSponsorName(sponsorName);
            sponsorFormMaintainance.setFormData();
            sponsorFormMaintainance.display();
        }else{
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("Please select a row. "));
        }
    }

    /**
     * This method is invoked when the user clicks add in the Edit menu
     * of Sponsor module
     */
    private void addSponsor() {
         /*
          * Call the SponsorMaintenanceForm with the function Type as 'I' ,
          * other parameter  null b'coz we are adding
          * The form will be shown as New Sponsor window for the user entry.
          */
        frmSponsor = new SponsorMaintenanceForm('I',"");
        frmSponsor.registerObserver( this );
        /* Call the Show method of form with the Add New Sponsor title and
         * as modal window
         */
        frmSponsor.showForm(mdiForm,ADD_TITLE,true);
        //insertRowInBaseWindow(frmSponsor.getDataToUpdate());
    }

    /**
     * This method is invoked when the user clicks modify in the Edit menu
     * of Sponsor module
     */
    private void modifySponsor() throws Exception{
        if (tblSearchResultsTable == null ) {
            /* Check any data present in the sponsor internal frame window */
            logWarning(coeusMessageResources.parseMessageKey(
                                            "spnrBaseWin_exceptionCode.1111"));
        }else if (tblSearchResultsTable.getSelectedRow() < 0) {
            /* Check any row is selected before Modify */
            logWarning(coeusMessageResources.parseMessageKey(
                                            "spnrBaseWin_exceptionCode.1111"));
        } else {
            /* the sponsor code is selected from the modal window */
            sponsorCode = tblSearchResultsTable.getValueAt(
            tblSearchResultsTable.getSelectedRow(),0).toString();
            requester = new RequesterBean();
             /*
              * The functionType "M" is set and sent to notify the servlet
              * to check whether the user has right to modify the data or not.
              */
            requester.setFunctionType('M');
            requester.setRequestedForm(TITLE_SPONSOR);
            requester.setDataObject(sponsorCode);
            comm = new AppletServletCommunicator(CONNECT_TO, requester);
            comm.send();
            /* the data is end to the servlet for modify */
            response = comm.getResponse();
            if (response != null && response.isSuccessfulResponse()) {
                /*get the information from the response bean*/
                responseData = response.getDataObjects();
                spBean = new SponsorMaintenanceFormBean();
                rolDetailBean = new RolodexDetailsBean();
                if ( (responseData != null) && (responseData.size() > 0)){
                   /*
                    * get the first element from the vector returned by the
                    * response bean.Expected information is Boolean object
                    */
                    boolean allowModify = (responseData.get(0) == null ? false
                    :((Boolean)responseData.get(0)).booleanValue());
                     /*
                      * get the second element from the vector returned by the
                      * response bean.Expected information is String object
                      * specifies the infomation
                      */
                    String informationMessage = (responseData.get(1).toString()
                    == null ? "Not Available" :responseData.get(1).toString());
                      /*
                       * get the third element from the vector returned by the
                       * response bean.Expected information is
                       * SponsorMaintenanceFormBean object
                       */
                    spBean  = (SponsorMaintenanceFormBean)responseData.get(2);
                    rolDetailBean  = (RolodexDetailsBean)responseData.get(3);
                    if (!allowModify) {
                       /* If the user doesn't have right to modify the sponsor
                        * information.Inform the user with the returned
                        * information from the servlet as informatin message.
                        * Prompt the user for the Display of the information.
                        */
                        int resultConfirm = CoeusOptionPane.showQuestionDialog(
                                            informationMessage,
                                            CoeusOptionPane.OPTION_YES_NO,
                                            CoeusOptionPane.DEFAULT_YES);
                        if (resultConfirm == 0) {
                        /* if the resultConfirm is zero the user wants to
                         * display the data
                         */
                            sponsorCode = tblSearchResultsTable.getValueAt(
                            tblSearchResultsTable.getSelectedRow(),0).toString();
                            frmSponsor = new SponsorMaintenanceForm('D',sponsorCode);
                            frmSponsor.showForm(mdiForm,DISPLAY_TITLE,true);
                        }
                    }else {
                        int selRow = tblSearchResultsTable.getSelectedRow(); 
                        if(  selRow != -1 ) {
                            baseTableRow = selRow;
//                            baseTableRow = Integer.parseInt((String)tblSearchResultsTable.getValueAt(
//                            selRow,tblSearchResultsTable.getColumnCount()-1));
                        }
                        /* the user has right to modify the sponsor data */
                        sponsorCode = tblSearchResultsTable.getValueAt(
                        tblSearchResultsTable.getSelectedRow(),0).toString();
                        frmSponsor = new SponsorMaintenanceForm('U',spBean,rolDetailBean);
                        frmSponsor.registerObserver( this );
                        frmSponsor.showForm(mdiForm,MODIFY_TITLE,true);
                        /*updateRowInBaseWindow(tblSearchResultsTable.getSelectedRow(),
                                frmSponsor.getDataToUpdate());*/
                    }
                }else {
                }
            }else{
                if (response.isLocked()) {
                    /* the row is being locked by some other user
                     */
                    String msg = coeusMessageResources.parseMessageKey(
                                    "sponsor_row_clocked_exceptionCode.222222");
                    int resultConfirm = CoeusOptionPane.showQuestionDialog(msg,
                    CoeusOptionPane.OPTION_YES_NO,
                    CoeusOptionPane.DEFAULT_YES);
                    if (resultConfirm == 0) {
                        /* Call the RolodexDetails form with functionType
                         * as 'V'  and RolodexDetailsBean as inormation and
                         * the connectionURl string information
                         */
                        displaySponsor();
                    }
                }else{
                    throw new Exception(response.getMessage());
                }
            }
        }
    }

    /**
     * This method is invoked when the user clicks display in the Edit menu
     * of Sponsor module
     */
    private void displaySponsor() {
        if (tblSearchResultsTable == null ) {
            /* Check any data present in the sponsor internal frame window */
            logWarning(coeusMessageResources.parseMessageKey(
                                            "spnrBaseWin_exceptionCode.1112"));
        }else if (tblSearchResultsTable.getSelectedRow() < 0) {
            /* Check any row is selected before display */
            logWarning(coeusMessageResources.parseMessageKey(
                                            "spnrBaseWin_exceptionCode.1112"));
        }else {
            /* if the user wants display the sponsor details for sponsor code
             * getting from the modal window
             */
            sponsorCode = tblSearchResultsTable.getValueAt(
            tblSearchResultsTable.getSelectedRow(),0).toString();
            /*  the functionType "D" for display */
            frmSponsor = new SponsorMaintenanceForm('D',sponsorCode);
            frmSponsor.showForm(mdiForm,DISPLAY_TITLE,true);
        }
    }

    /**
     * Save Sponsor
     */
    private void saveSponsor() {
        //to be implemented in the next phase
    }

    /**
     * This method is invoked when the user clicks delete in the Edit menu
     * of Sponsor module
     */
    private void deleteSponsor() throws Exception{
        if (tblSearchResultsTable == null ) {
            logWarning(coeusMessageResources.parseMessageKey(
                                            "spnrBaseWin_exceptionCode.1113"));
        }else if (tblSearchResultsTable.getSelectedRow() < 0) {
            logWarning(coeusMessageResources.parseMessageKey(
                                            "spnrBaseWin_exceptionCode.1113"));
        //}else if(){
        }else {
            /* get selected sponsor code from the modal window for check
             * before delete
             */
            sponsorCode = tblSearchResultsTable.getValueAt(
            tblSearchResultsTable.getSelectedRow(),0).toString();
            requester = new RequesterBean();
              /*
               * The functionType "C" is set and sent to notify the servlet
               * to check whether the user has right to deletethe data or not.
               */
            requester.setFunctionType('C');
            requester.setRequestedForm(TITLE_SPONSOR);
            requester.setDataObject(sponsorCode);
            comm = new AppletServletCommunicator(CONNECT_TO, requester);
            /* connecting to the servlet */
            comm.send();
            /* the data is end to the servlet for delete */
            response = comm.getResponse();
            if (response != null) {
                if(!response.isSuccessfulResponse()){
                    throw new Exception(response.getMessage());
                }
                responseData = response.getDataObjects();
                if ( (responseData != null) && (responseData.size() > 0)){
                    boolean allowDelete = (responseData.get(0) == null ? false
                    : ((Boolean)responseData.get(0)).booleanValue());
                    String informationMessage = (responseData.get(1).toString()
                    == null ? "Not Available" :responseData.get(1).toString());
                    if (!allowDelete) {
                        /* the user doesn't have the rights to delete the record */
                        CoeusOptionPane.showInfoDialog(informationMessage);
                    } else{
                     /* resultConfirm will ask the user whether he wants to
                      * delete or not
                      */
                        int resultConfirm = CoeusOptionPane.showQuestionDialog(
                                            informationMessage,
                                            CoeusOptionPane.OPTION_YES_NO,
                                            CoeusOptionPane.DEFAULT_YES);
                        if (resultConfirm == 0) {
                            /* if the yes the functionType "E" is set to notify
                             * the servlet this record to be deleted
                             */
                            sponsorCode = tblSearchResultsTable.getValueAt(
                            tblSearchResultsTable.getSelectedRow(),0).toString();
                            requester  = new RequesterBean();
                            requester.setFunctionType('E');
                            requester.setDataObject(sponsorCode);
                            comm = new AppletServletCommunicator(
                            CONNECT_TO, requester);
                            comm.send();
                            response = comm.getResponse();
                            if (response != null){
                                String msgInfo = (response.getDataObject()
                                == null ? " Not Available"
                                : response.getDataObject().toString());
                                /* this method is called after delete the modal
                                 * window should be refreshed with new data
                                 */
                                deleteRowInBaseWindow(tblSearchResultsTable.getSelectedRow());
                            }
                        }
                    }
                }else {
                }
            }else{
            }
            // end of check
        }
    }

    /**
     * This methods loads the search window when the sponsor module is opened,
     * this helps the user to load the base window with the sponsor details.
     *
     */
    private void showSearchWindow() throws Exception{
        //Case 2451 start
            vecSortedData = new Vector();
            //Case 2451 end
        coeusSearch.showSearchWindow();
        JTable tblResultsTable = coeusSearch.getSearchResTable();
        if (tblResultsTable!=null){
            tblSearchResultsTable=tblResultsTable;
            javax.swing.table.TableColumn clmName
                = tblSearchResultsTable.getColumnModel().getColumn(
                    tblSearchResultsTable.getColumnCount()-1);
            clmName.setPreferredWidth(0);
            clmName.setMaxWidth(0);
            clmName.setMinWidth(0);
            clmName.setWidth(0);
            setSearchResultsTable(tblSearchResultsTable);
            scrPnSearchRes.setViewportView(tblSearchResultsTable);
            javax.swing.table.JTableHeader header
                = tblSearchResultsTable.getTableHeader();
            //header.setResizingAllowed(false);
            header.setReorderingAllowed(false);
            tblSearchResultsTable.getTableHeader().addMouseListener(new SponsorMouseAdapter());
            tblSearchResultsTable.addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent me){
                    if (me.getClickCount() == 2) {
                        try{
                            int selectedRow =
                                    tblSearchResultsTable.getSelectedRow();
                            String sponsorCode =
                            tblSearchResultsTable.getValueAt(
                                                    selectedRow,0).toString();
                            //BUG FIX:1062 hour glass implementation start
                            mdiForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
                            SponsorMaintenanceForm  frmSponsor =
                                new SponsorMaintenanceForm('D',sponsorCode);
                            frmSponsor.showForm(mdiForm,DISPLAY_TITLE,true);
                            mdiForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                            //BUG FIX:1062 hour glass implementation end
                        }
                        catch(Exception e){
                            CoeusOptionPane.showInfoDialog(e.getMessage());
                        }
                    }
                }
            });
        }
        this.revalidate();
    }

    /**
     * This method is invoked when the data is deleted it will refresh the base
     * window with the existing record
     */
    private void deleteRowInBaseWindow(int row){
        /** Modified by chandra on 23-Nov-2004
         *While deleting a row, delete the index and by passing selected row.
         *Don't delete the selectedRow,insted get the selected row index and then
         *delete the row.
         */
        int orgRow=0;
        if(  row != -1 ) {
//            int colCount = tblSearchResultsTable.getColumnCount()-1;
//            orgRow = Integer.parseInt((String)tblSearchResultsTable.getValueAt(
//            row,tblSearchResultsTable.getColumnCount()-1));
            ((DefaultTableModel)tblSearchResultsTable.getModel()).removeRow(row);
            validate();
        }//End Chandra 
    }

    /**
     * This method is invoked when the data is updated it will refresh the base
     * window with the updated record
     */
     private void updateRowInBaseWindow(int rowNumber, Vector rowData){
        if (rowData!=null) {
            ((DefaultTableModel)tblSearchResultsTable.getModel()).removeRow(
                                                                    rowNumber);
            rowData.addElement("");
            ((DefaultTableModel)tblSearchResultsTable.getModel()).insertRow(
                                                            rowNumber,rowData);
            tblSearchResultsTable.setRowSelectionInterval(rowNumber,rowNumber);
            validate();
        }
    }

    /**
     * This method is invoked when new sponsor added to the database it will add
     * new row with data in the base window
     */
    private void insertRowInBaseWindow(Vector rowData){
        if (rowData!=null) {
            rowData.addElement("");
            ((DefaultTableModel)tblSearchResultsTable.getModel()).insertRow(
                                                                    0,rowData);
            tblSearchResultsTable.setRowSelectionInterval(0,0);
            validate();
        }
    }

    /**
     * display error message from the server on mdi form
     * 
     * @param mesg String
     */
    public void log(String mesg) {
        CoeusOptionPane.showErrorDialog(mesg);
    }

    /**
     * display warning message from the server on mdi form
     *
     * @param mesg String
     */
    public void logWarning(String mesg) {
        CoeusOptionPane.showWarningDialog(mesg);
    }

    /**
     * This method called from Save Menu Item under File Menu.
     * Implemented the abstract class declared in parent(CoeusInternalFrame).
     * Empty body since the Save operation is not required for this list screen.
     */
    public void saveActiveSheet() {
    }

    public void update(java.util.Observable observable, Object obj) {
        if( obj instanceof SponsorMaintenanceFormBean ) {
            SponsorMaintenanceFormBean spBean = (SponsorMaintenanceFormBean) obj;
            if (((BaseWindowObservable)observable).getFunctionType() == 'I'){
                Vector rowData = new Vector();
                rowData.add(spBean.getSponsorCode());
                rowData.add(spBean.getName());
                rowData.add(spBean.getAcronym());
                rowData.add(spBean.getTypeDescription());
                
                //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
                if(ACTIVE_STATUS.equalsIgnoreCase(spBean.getStatus().trim())) {
                rowData.add(ACTIVE);
                }
                 else if(INACTIVE_STATUS.equalsIgnoreCase(spBean.getStatus().trim())) {
                 rowData.add(INACTIVE);
                }
                //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
                
                rowData.add(spBean.getPostalCode());
                rowData.add(spBean.getState());
                rowData.add(spBean.getStateDescription());
                rowData.add(spBean.getCountryName());
                rowData.add(spBean.getAuditReport());
                rowData.add(spBean.getDuns());
                rowData.add(spBean.getDuns4());
                rowData.add(spBean.getDodc());
                rowData.add(spBean.getCage());
                rowData.add(spBean.getOwnedBy());
                //for index which came from search
                int lastRow = tblSearchResultsTable.getRowCount(); 
                rowData.addElement(""+lastRow);
                if( lastRow >= 0 ) {
                    ((DefaultTableModel)tblSearchResultsTable.getModel()).insertRow(lastRow,rowData);
                }else{
                    ((DefaultTableModel)tblSearchResultsTable.getModel()).insertRow(0,rowData);
                    
                }
                baseTableRow = lastRow; 
               //Added and modified for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start 
            }else if( ((BaseWindowObservable)observable).getFunctionType() == 'U'){
                (tblSearchResultsTable.getModel()).setValueAt(
                        //spBean.getSponsorCode(),baseTableRow,0);
                        spBean.getSponsorCode(),baseTableRow,SPONSOR_SPONSOR_CODE_COLUMN_INDEX);
                (tblSearchResultsTable.getModel()).setValueAt(
                        //spBean.getName(),baseTableRow,1);
                        spBean.getName(),baseTableRow,SPONSOR_SPONSOR_NAME_COLUMN_INDEX);
                (tblSearchResultsTable.getModel()).setValueAt(
                        //spBean.getAcronym(),baseTableRow,2);
                        spBean.getAcronym(),baseTableRow,SPONSOR_ACRONYM_COLUMN_INDEX);
                (tblSearchResultsTable.getModel()).setValueAt(
                        //spBean.getTypeDescription(),baseTableRow,3);
                        spBean.getTypeDescription(),baseTableRow,SPONSOR_SPONSOR_TYPE_COLUMN_INDEX);
                //Updating the data by selecting active or inactive status
                if(ACTIVE_STATUS.equalsIgnoreCase(spBean.getStatus().trim())) {
                    //(tblSearchResultsTable.getModel()).setValueAt(ACTIVE,baseTableRow,4);
                    (tblSearchResultsTable.getModel()).setValueAt(ACTIVE,baseTableRow,SPONSOR_STATUS_COLUMN_INDEX);
                } else if(INACTIVE_STATUS.equalsIgnoreCase(spBean.getStatus().trim())) {
                    //(tblSearchResultsTable.getModel()).setValueAt(INACTIVE,baseTableRow,4);
                    (tblSearchResultsTable.getModel()).setValueAt(INACTIVE,baseTableRow,SPONSOR_STATUS_COLUMN_INDEX);
                }
                (tblSearchResultsTable.getModel()).setValueAt(
                        //spBean.getPostalCode(),baseTableRow,4);
                        spBean.getPostalCode(),baseTableRow,SPONSOR_POSTAL_CODE_COLUMN_INDEX);
                (tblSearchResultsTable.getModel()).setValueAt(
                        // spBean.getState(),baseTableRow,5);
                        spBean.getState(),baseTableRow,SPONSOR_STATE_COLUMN_INDEX);
                (tblSearchResultsTable.getModel()).setValueAt(
                        //spBean.getStateDescription(),baseTableRow,6);
                        spBean.getStateDescription(),baseTableRow,SPONSOR_STATE_NAME_COLUMN_INDEX);
                (tblSearchResultsTable.getModel()).setValueAt(
                        //spBean.getCountryName(),baseTableRow,7);
                        spBean.getCountryName(),baseTableRow,SPONSOR_COUNTRY_NAME_COLUMN_INDEX);
                (tblSearchResultsTable.getModel()).setValueAt(
                        //spBean.getAuditReport(),baseTableRow,8);
                        spBean.getAuditReport(),baseTableRow,SPONSOR_AUDIT_REPORT_COLUMN_INDEX);
                (tblSearchResultsTable.getModel()).setValueAt(
                        //spBean.getDuns(),baseTableRow,9);
                        spBean.getDuns(),baseTableRow,SPONSOR_DUN_AND_BRADSTREET_COLUMN_INDEX);
                (tblSearchResultsTable.getModel()).setValueAt(
                        //spBean.getDuns4(),baseTableRow,10);
                        spBean.getDuns4(),baseTableRow,SPONSOR_DUNS_PLUS_FOUR_COLUMN_INDEX);
                (tblSearchResultsTable.getModel()).setValueAt(
                        //spBean.getDodc(),baseTableRow,11);
                        spBean.getDodc(),baseTableRow,SPONSOR_DODAC_NUMBER_COLUMN_INDEX);
                (tblSearchResultsTable.getModel()).setValueAt(
                        //spBean.getCage(),baseTableRow,12);
                        spBean.getCage(),baseTableRow,SPONSOR_CAGE_NUMBER_COLUMN_INDEX);
                (tblSearchResultsTable.getModel()).setValueAt(
                        //spBean.getOwnedBy(),baseTableRow,13);
                        spBean.getOwnedBy(),baseTableRow,SPONSOR_OWNED_BY_UNIT_COLUMN_INDEX);
                //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
                int selRow = tblSearchResultsTable.getSelectedRow();
                if(  selRow != -1 ) {
                    baseTableRow = selRow;
//                    baseTableRow = Integer.parseInt((String)tblSearchResultsTable.getValueAt(
//                    selRow,tblSearchResultsTable.getColumnCount()-1));
                }
          
            }
        }
    }
    
    public void saveAsActiveSheet() {
        SaveAsDialog saveAsDialog = new SaveAsDialog(tblSearchResultsTable);
    }
    
    /** Making a server side call to get the right details of the user. If the User
     *doesn't have OSP level right, i.e., SPONSOR_MAINTAIN_FORMS disable Sponsor Formsmenu item.
     *If the user doesn't have other unit level 
     *rights i.e., ADD_SPONSOR and  MAINTAIN_SPONSOR disable Add menu items and 
     *corresponding tool bar button. 
     *This method will return vector of two boolean objects. One contains the
     *OSP Right i.e., SPONSOR_MAINTAIN_FORMS and other contains the ADD_SPONSOR and 
     *MAINITAIN_SPONSOR boolean object
     *
     *Added by chandra - Jan 17 2004
     */
    private Vector checkUserRight() {
        Vector vecSponsorRight = null;
        RequesterBean request = new RequesterBean();
        request.setDataObjects(vecRights);
        request.setFunctionType(SPONSOR_RIGHT_TYPE);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(CONNECT_TO, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        
        if (response.isSuccessfulResponse()) {
            if(response.getDataObjects() != null){
                vecSponsorRight = (Vector)response.getDataObjects();
            }else{
                CoeusOptionPane.showErrorDialog(response.getMessage()) ;
            }
        }else{
            CoeusOptionPane.showErrorDialog(response.getMessage());
        }
        return vecSponsorRight;
        
    }// Added by chandra - End
    
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
    class SponsorMouseAdapter extends MouseAdapter{
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
                newSortedData.addElement(tblSearchResultsTable.getColumnName(column));
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
}// end of file
