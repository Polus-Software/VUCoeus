/**
 * @(#)RolodexBaseWindow.java 1.0 11/10/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */



package edu.mit.coeus.rolodexmaint.gui;

import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.toolbar.*;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexMaintController;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
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
import java.util.Observable;
import java.util.Observer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

/**
 * This class is the Base window for Rolodex Maintenance,this has the methods
 * for the events on Rolodex specific Edit menu options like Add,Modify,Display,
 * Copy and  Delete operations and associate its menu and toolbar with listener.
 * The Rolodex Details window will be shown based on the menu selected.
 *
 * @version 1.0 October 11, 2002, 12:16 AM
 * @author  Phaneendra Kumar
 */
public class RolodexBaseWindow extends CoeusInternalFrame
implements ActionListener, Observer{
     /*
      * Rolodex Edit menu items used by RolodexListener
      */
    private CoeusMenuItem addRolodex,modifyRolodex,displayRolodex,deleteRolodex;
    private CoeusMenuItem copyRolodex,referencesRolodex,rolodexSearch;
     /*
      * Rolodex Toolbar buttons used by RolodexListener
      */
    private CoeusToolBarButton btnRolodex,btnAddRolodex,btnModifyRolodex;
    private CoeusToolBarButton btnDisplayRolodex;
    private CoeusToolBarButton btnDeleteRolodex,btnCopyRolodex,btnRefRolodex;
    private CoeusToolBarButton btnSortRolodex,btnRolodexSearch,btnSaveAs,btnClosed;
    private CoeusAppletMDIForm mdiForm;
    private JTable rolodexsheet;
    /*
     * reference of Title for the Rolodex window
     */
    private final String ADD_TITLE  = "Add New Rolodex";
    private final String MODIFY_TITLE = "Modify Rolodex";
    private final String COPY_TITLE  = "Copy Rolodex";
    private final String DISPLAY_TITLE = "Display Rolodex";
    private final String ROLODEX_SERVLET = "/rolMntServlet";
    private final String ROLODEX_TITLE = "Rolodex";
    //COEUSQA-1528 Add the functionality to set a status on a Rolodex entry START 
    private final String ROLODEX_STATUS_SEARCH = "ROLODEXSTATUSSEARCH";
    private final String ACTIVE = "Active";
    private final String ACTIVE_STATUS = "A";
    private final String INACTIVE = "Inactive";
    private final String INACTIVE_STATUS = "I";
     //COEUSQA-1528 Add the functionality to set a status on a Rolodex entry END
    private RolodexMaintController rldxController;
    private CoeusSearch coeusSearch = null;
    private JScrollPane scrlPnSearchRes;
    private RolodexMaintenanceDetailForm frmRolodex= null;
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    //Added by chandra Starts  - Jan 17 2003
    private Vector vecRolRights = new Vector();
    private static final String SYSTEM_MAINTAINANCE = "SYSTEM_MAINTENANCE";
    private static final String ADD_ROLODEX = "ADD_ROLODEX";
    private static final String MAINTAIN_ROLODEX = "MAINTAIN_ROLODEX";
    private static final char ROLODEX_RIGHT = 'Y';
    private String connectTo = CoeusGuiConstants.CONNECTION_URL + ROLODEX_SERVLET;
    //Added by chandra Ends - Jan 17 2003
    
    private int baseTableRow;
    
    //holds sorted columns and its states
    private Vector vecSortedData;
    
    private static final int OK_CLICKED = 0;
    
    //holds column index
    private int oldCol = -1;
    
    //holds column status
    private int status=MultipleTableColumnSorter.NOT_SORTED;
    private boolean deleteFlag;
    
    /** Constructor to create RolodexBaseWindow
     *
     * @param mdiForm CoeusMDIForm.
     * @throws Exception  exception
     */
    public RolodexBaseWindow(CoeusAppletMDIForm mdiForm) throws Exception {
        super(CoeusGuiConstants.TITLE_ROLODEX, mdiForm,LIST_MODE);
        this.mdiForm = mdiForm;
        initComponents();
        mdiForm.putFrame( CoeusGuiConstants.TITLE_ROLODEX ,this);
        mdiForm.getDeskTopPane().add(this);
        setVisible(true);
        setSearchResults();
        rldxController = new RolodexMaintController();
    }
    
    /**
     * Construct the components for the base window
     */
    private void initComponents() throws Exception{
        coeusMessageResources = CoeusMessageResources.getInstance();
        
        /* Added by Chandra - 01/17/2004 - Add Rights for ADD_ROLODEX,
         *MAINTAIN_ROLODEX and SYSTEM_ROLOEX
         */
        vecRolRights.add(SYSTEM_MAINTAINANCE);
        vecRolRights.add(ADD_ROLODEX);
        vecRolRights.add(MAINTAIN_ROLODEX);
        // End Chandra  - 01/17/2004
        setFrameMenu(rolodexEditMenu());
        setToolsMenu(rolodexToolsMenu());
        setFrameToolBar(rolodexToolBar());
        setFrame( CoeusGuiConstants.TITLE_ROLODEX );
        setFrameIcon(mdiForm.getCoeusIcon());
        
        // Added by chandra - 01/17/2004
        Vector vecRolodexDetails = checkUserRight();
        boolean hasSystemRight = ((Boolean) vecRolodexDetails.get(0)).booleanValue();
        boolean hasMaintainRight = ((Boolean) vecRolodexDetails.get(1)).booleanValue();
        // checking for the System Right.. OSP Right SYSTEM_MAINTENANCE
        referencesRolodex.setEnabled(hasSystemRight);
        btnRefRolodex.setEnabled(hasSystemRight);
        
        // checking for the ADD_SPONSOR and MAINTAIN_SPONSOR right.
        addRolodex.setEnabled(hasMaintainRight);
        copyRolodex.setEnabled(hasMaintainRight);
        btnAddRolodex.setEnabled(hasMaintainRight);
        btnCopyRolodex.setEnabled(hasMaintainRight);
        //End chandra - 01/17/2004
        
        scrlPnSearchRes = new JScrollPane();
        scrlPnSearchRes.setMinimumSize(new Dimension(22, 15));
        scrlPnSearchRes.setPreferredSize(new Dimension(600, 400));
        
        scrlPnSearchRes.setViewportView(createRolodex());
        if( rolodexsheet != null ){
            rolodexsheet.setFont( CoeusFontFactory.getNormalFont() );
        }
        scrlPnSearchRes.getViewport().setBackground(Color.white);
        scrlPnSearchRes.setForeground(java.awt.Color.white);
        getContentPane().add(scrlPnSearchRes);
    }
    
    
    /**
     * Constructs Rolodex Edit menu
     *
     * @return CoeusMenu that return edit menu items
     */
    public CoeusMenu rolodexEditMenu(){
        CoeusMenu coeusMenu;
        java.util.Vector fileChildren = new java.util.Vector();
        addRolodex = new CoeusMenuItem("Add",null,true,true);
        addRolodex.setMnemonic('A');
        addRolodex.addActionListener(this);
        modifyRolodex  = new CoeusMenuItem("Modify",null,true,true);
        modifyRolodex.setMnemonic('M');
        modifyRolodex.addActionListener(this);
        displayRolodex = new CoeusMenuItem("Display",null,true,true);
        displayRolodex.setMnemonic('I');
        displayRolodex.addActionListener(this);
        deleteRolodex = new CoeusMenuItem("Delete",null,true,true);
        deleteRolodex.setMnemonic('D');
        deleteRolodex.addActionListener(this);
        copyRolodex = new CoeusMenuItem("Copy",null,true,true);
        copyRolodex.setMnemonic('C');
        copyRolodex.addActionListener(this);
        referencesRolodex = new CoeusMenuItem("References...",null,true,true);
        referencesRolodex.setMnemonic('R');
        referencesRolodex.addActionListener(this);
        
        fileChildren.add(addRolodex);
        fileChildren.add(modifyRolodex);
        fileChildren.add(displayRolodex);
        fileChildren.add(deleteRolodex);
        fileChildren.add(copyRolodex);
        fileChildren.add(referencesRolodex);
        
        coeusMenu = new CoeusMenu("Edit",null,fileChildren,true,true);
        coeusMenu.setMnemonic('E');
        return coeusMenu;
    }
    
    /**
     * RolodexToolBar is a toolbar for rolodex functionality
     *
     * @return JToolBar rolodex toolbar
     */
    public JToolBar rolodexToolBar(){
        JToolBar toolbar = new JToolBar();
        btnAddRolodex = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ADD_ICON)),null,"Add"); //add_icon),"Add","Add a new rolodex entry");
        btnModifyRolodex = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.EDIT_ICON)),null,"Modify");
        btnDisplayRolodex = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DISPLAY_ICON)),null,"Display");
        btnDeleteRolodex = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DELETE_ICON)),null,"Delete");
        btnCopyRolodex = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.COPY_ICON)),null,"Copy");
        btnRefRolodex = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.REFRENCES_ICON)),null,"References");
        btnSortRolodex = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SORT_ICON)),null,"Sort Rolodex");
        btnRolodexSearch = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)),null,"Search");
        btnSaveAs = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SAVEAS_ICON)),null,"Save As");
        btnClosed = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),null,"Close");
        
        btnAddRolodex.addActionListener(this);
        btnModifyRolodex.addActionListener(this);
        btnDisplayRolodex.addActionListener(this);
        btnDeleteRolodex.addActionListener(this);
        btnCopyRolodex.addActionListener(this);
        btnRefRolodex.addActionListener(this);
        btnSortRolodex.addActionListener(this);
        btnRolodexSearch.addActionListener(this);
        btnSaveAs.addActionListener(this);
        btnClosed.addActionListener(this);
        
        toolbar.add(btnAddRolodex);
        toolbar.add(btnModifyRolodex);
        toolbar.add(btnDisplayRolodex);
        toolbar.add(btnDeleteRolodex);
        toolbar.add(btnCopyRolodex);
        toolbar.addSeparator();
        toolbar.add(btnRefRolodex);
        toolbar.add(btnSortRolodex);
        toolbar.add(btnRolodexSearch);
        toolbar.add(btnSaveAs);
        toolbar.addSeparator();
        toolbar.add(btnClosed);
        
        toolbar.setFloatable(false);
        return toolbar;
    }
    
    
    /**
     * Constructs tool menu
     *
     * @return CoeusMenu that return tool menu items
     */
    public CoeusMenu rolodexToolsMenu(){
        CoeusMenu coeusMenu;
        java.util.Vector fileChildren = new java.util.Vector();
        rolodexSearch = new CoeusMenuItem("Search",null,true,true);
        rolodexSearch.setMnemonic('S');
        rolodexSearch.addActionListener(this);
        fileChildren.add(rolodexSearch);
        coeusMenu = new CoeusMenu("Tools",null,fileChildren,true,true);
        coeusMenu.setMnemonic('T');
        return coeusMenu;
    }
    
    
    /**
     * This is to get the Rolodexsheet table
     *
     * @return rolodexsheet JTable with the Rolodexdata
     */
    private JTable getRolodexsheet() {
        return rolodexsheet;
    }
    
    /**
     * This is to set the Rolodexsheet table
     *
     * @param rolodexsheet: JTable with the Rolodexdata
     */
    private void setRolodexsheet(JTable rolodexsheet){
        this.rolodexsheet = rolodexsheet;
        rolodexsheet.setSelectionMode(rolodexsheet.getSelectionModel().SINGLE_SELECTION);
        if( rolodexsheet!= null && rolodexsheet.getRowCount() > 0 ){
            rolodexsheet.addRowSelectionInterval(0, 0);
        }
    }
    
    
    /**
     * This is to create Rolodexsheet with Rolodex data
     *
     * @return rolodexsheet JTable with the Rolodexdata
     */
    private JTable createRolodex() throws Exception{
        coeusSearch = new CoeusSearch(mdiForm, ROLODEX_STATUS_SEARCH, 0);
        rolodexsheet = coeusSearch.getEmptyResTable();
        javax.swing.table.TableColumn clmName
        = rolodexsheet.getColumnModel().getColumn(
        rolodexsheet.getColumnCount()-1);
        clmName.setPreferredWidth(0) ;
        clmName.setMaxWidth(0);
        clmName.setMinWidth(0);
        clmName.setWidth(0);
        javax.swing.table.JTableHeader header = rolodexsheet.getTableHeader();
        //header.setResizingAllowed(false);
        header.setReorderingAllowed(false);
        setRolodexsheet(rolodexsheet);
        return rolodexsheet;
    }
    
    /**
     * This is set Rolodexsheet with retrieved search results Rolodex data
     *
     */
    private void setSearchResults() throws Exception{
        // Case 2451 start
        vecSortedData = new Vector();
        // Case 2451 End
        coeusSearch.showSearchWindow();
        JTable resultsTable = coeusSearch.getSearchResTable();
        if (resultsTable != null){
            rolodexsheet = resultsTable;
            javax.swing.table.TableColumn clmName
            = rolodexsheet.getColumnModel().getColumn(
            rolodexsheet.getColumnCount()-1);
            clmName.setPreferredWidth(0);
            clmName.setMaxWidth(0);
            clmName.setMinWidth(0);
            clmName.setWidth(0);
            javax.swing.table.JTableHeader header
            = rolodexsheet.getTableHeader();
            //header.setResizingAllowed(false);
            header.setReorderingAllowed(false);
            setRolodexsheet(rolodexsheet);
            scrlPnSearchRes.setViewportView(rolodexsheet);
            if (rolodexsheet != null){
                rolodexsheet.getTableHeader().addMouseListener(new RolodexMouseAdapter());
                rolodexsheet.addMouseListener(new MouseAdapter(){
                    public void mouseClicked(MouseEvent me){
                        if (me.getClickCount() == 2) {
                            try{
                                String keyValue =
                                rolodexsheet.getValueAt(
                                rolodexsheet.getSelectedRow(),0).toString();
                                RolodexMaintController rolodexController =
                                new RolodexMaintController();
                                RolodexDetailsBean rldxBean =
                                rolodexController.displayRolodexInfo(keyValue);
                                RolodexMaintenanceDetailForm frmRolodex =
                                new RolodexMaintenanceDetailForm('V',rldxBean);
                                //BUG FIX--1062,hour glass implementation START
                                mdiForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
                                /* Call the Show method of form with the NEW ROLODEX
                                 * title and as modal window
                                 */
                                frmRolodex.showForm(mdiForm,"Display Rolodex",true);
                                mdiForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                                //BUG FIX--1062,hour glass implementation END
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
    }
    
    /**  Method used to perform all the events fired on Rolodex specific Edit menu.
     *  <li>To fetch the data, it uses select_Rolodex procedure<gt>
     *
     * @param ae ActionEvent
     */
    public void actionPerformed(ActionEvent ae) {
        /* Get the selected menu item */
        Object menuItem = ae.getSource();
        try {
        /* Fetch the RolodexInternal Frame from the mdiForm which
         * is already opened
         */
            /* If the menu selected is Edit - > ADD*/
            if ( (menuItem.equals(addRolodex)) ||
            (menuItem.equals(btnAddRolodex) ))     {
                /*Call this method to show a new RolodexDetails window */
                
                addNewRolodex();
                
                /* If the menu selected is Edit - > Modify */
            }else if ( (menuItem.equals(modifyRolodex)) ||
            (menuItem.equals(btnModifyRolodex) )) {
                modifyRolodex();
                /* If the menu selected is Edit - > Display */
            }else if ( (menuItem.equals(displayRolodex)) ||
            (menuItem.equals(btnDisplayRolodex) )) {
                displayRolodex();
                
                /* If the menu selected is Edit - > References */
            }else if ( (menuItem.equals(referencesRolodex)) ||
            (menuItem.equals(btnRefRolodex))){
                
                referencesRolodex();
            }
            else if ( (menuItem.equals(deleteRolodex)) ||
            (menuItem.equals(btnDeleteRolodex)) ) {
                if(!deleteFlag){
                    deleteFlag = true;
                    deleteRolodex();
                    
                }
                /* If the menu selected is Edit - > Copy */
            }else if ( (menuItem.equals(copyRolodex)) ||
            (menuItem.equals(btnCopyRolodex) )) {
                copyRolodex();
                /* If the menu selected is Edit - > References*/
            }
            //saves the table to desired destination
            else if (menuItem.equals(btnSaveAs) ) {
                saveAsActiveSheet();
            }else if (menuItem.equals(btnClosed)) {
                CoeusInternalFrame frame
                = mdiForm.getFrame(CoeusGuiConstants.TITLE_ROLODEX);
                if (frame != null){
                    frame.doDefaultCloseAction();
                    mdiForm.removeFrame(CoeusGuiConstants.TITLE_ROLODEX);
                }
                
            }else if (menuItem.equals(btnSortRolodex)){
                showSort();
            }else if ((menuItem == rolodexSearch) ||
            (menuItem == btnRolodexSearch)){
                setSearchResults();
            }
            
        }catch(Exception e){
            e.printStackTrace();
            CoeusOptionPane.showInfoDialog(e.getMessage());
        }finally {
            deleteFlag = false;
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
        SortForm sortForm = new SortForm(rolodexsheet,vecSortedData);
        Vector sortedData = sortForm.display();
        vecSortedData = (Vector)sortedData.get(1);
        if(((Integer)sortedData.get(0)).intValue() == OK_CLICKED)
            coeusSearch.sortByColumns(rolodexsheet,vecSortedData);
        else
            return;
    }// end Nadh
    
    /**
     *  Method used to add a  new Rolodex Information.
     *
     * @throws Exception
     */
    private void addNewRolodex() throws Exception{
        ResponderBean response = getResponseFromDB("",'F');
        RolodexDetailsBean rldxBean = new RolodexDetailsBean();
        if( (response != null) && (response.getDataObjects() != null ) &&
        ( response.getDataObjects().size() > 0 ) )  {
            
            /* Get the information set at the servlet for the new Rolodex window */
            Vector responseData = response.getDataObjects();
            rldxBean.setStates(
            (responseData.get(0) == null ? new Vector() :
                (Vector)responseData.get(0)));
                /* Get the Vector of Countries information set at the Server and
                 * set into the RoldoexDetails bean
                 */
                rldxBean.setCountries((
                responseData.get(1) == null ? new Vector() :
                    (Vector)responseData.get(1)));
        }
        /* Call the Rolodex Details form with the function Type as 'I' and the
         * Rolodex details bean. The form will be shown as New Rolodex Details
         * window for the user entry.
         */
         //COEUSQA-1528 Add the functionality to set a status on a Rolodex entry START 
        rldxBean.setStatus(ACTIVE_STATUS);
         //COEUSQA-1528 Add the functionality to set a status on a Rolodex entry END 
        showRolodex('I',rldxBean,ADD_TITLE);
    }
    
    
    /**
     *  Method used to check for modification rights for the logged in user
     *  and if allowed it will display for modification.
     *
     *  @throws Exception
     */
    private void modifyRolodex() throws Exception{
        String rolodexId = getSelectedRolodexId();
        ResponderBean response = getResponseFromDB(rolodexId,'M');
        /*check whether the vector of information returned is null or not*/
        if (( response != null ) && (!response.isSuccessfulResponse()) ){
            if (response.isLocked()) {
                    /* the row is being locked by some other user
                     */
                String msg = coeusMessageResources.parseMessageKey(
                "rolodex_row_clocked_exceptionCode.111111");
                int resultConfirm = CoeusOptionPane.showQuestionDialog(msg,
                CoeusOptionPane.OPTION_YES_NO,
                CoeusOptionPane.DEFAULT_YES);
                if (resultConfirm == 0) {
                        /* Call the RolodexDetails form with functionType
                         * as 'V'  and RolodexDetailsBean as inormation and
                         * the connectionURl string information
                         */
                    displayRolodex();
                }
            }else{
                throw new Exception(response.getMessage());
            }
        }else if ( ( response != null ) && (response.getDataObjects() != null) &&
        (response.getDataObjects().size() > 0)) {
            
            Vector responseData = response.getDataObjects();
             /* get the first element from the vector returned by the
              * response bean.Expected information is Boolean object
              */
            boolean allowModify =
            (responseData.get(0) == null ? false :
                ((Boolean)responseData.get(0)).booleanValue());
                    /* get the second element from the vector returned by the
                     * response bean.Expected information is String object
                     * specifies the infomation
                     */
                String informationMessage =
                (responseData.get(1).toString() == null ?
                "Not Available" :responseData.get(1).toString());
                 /* get the third element from the vector returned by
                  * the response bean.Expected information is
                  * RolodexDetailsBean object
                  */
                RolodexDetailsBean rldxBean  =
                (RolodexDetailsBean)responseData.get(2);
                /* check whether allowModify is true/false */
                if (!allowModify) {
                    /* If the user doesn't have right to modify the rolodex
                     * information.Inform the user with the returned
                     * information from the servlet as informatin message.
                     * Prompt the user for the Display of the information.
                     */
                    
                    int resultConfirm =
                    CoeusOptionPane.showQuestionDialog(informationMessage,
                    CoeusOptionPane.OPTION_YES_NO,
                    CoeusOptionPane.DEFAULT_YES);
                    if (resultConfirm == 0) {
                        /* Call the RolodexDetails form with functionType
                         * as 'V'  and RolodexDetailsBean as inormation and
                         * the connectionURl string information
                         */
                        showRolodex('V',rldxBean,DISPLAY_TITLE);
                    }
                }else {
                    showRolodex('M',rldxBean,MODIFY_TITLE);
                }
        }
        
        
    }
    
    /**
     *  Method used to display the Rolodex details
     *
     *  @throws Exception
     */
    private void displayRolodex() throws Exception{
        String rolodexId = getSelectedRolodexId();
        RolodexDetailsBean rldxBean = null;
     /* set the requester Bean functionType
      * as 'V' to notify the servlet to get the rolodex info
      * set the title of the window as "COPY ROLODEX".
      */
        ResponderBean response = getResponseFromDB(rolodexId,'V');
      /* If the function Type is other than 'U' or 'D'.
       * Check whether response is null or not
       */
        if (response != null) {
            rldxBean  = (RolodexDetailsBean)response.getDataObject();
        }
        /* call the RolodexDetail form with the same function type
         * and the RolodexDetails bean and with the connectionURL.
         */
        showRolodex('V',rldxBean,DISPLAY_TITLE);
        
    }
    
    /**
     *  Method used to copy the existing Rolodex information to create a new
     *  Rolodex Details
     *
     *  @throws Exception
     */
    private void copyRolodex() throws Exception{
         String rolodexId = getSelectedRolodexId();
        RolodexDetailsBean rldxBean = null;
     /* set the requester Bean functionType
      * as 'V' to notify the servlet to get the rolodex info
      * set the title of the window as "COPY ROLODEX".
      */
        ResponderBean response = getResponseFromDB(rolodexId,'V');
      /* If the function Type is other than 'U' or 'D'.
       * Check whether response is null or not
       */
        if (response != null) {
            rldxBean  = (RolodexDetailsBean)response.getDataObject();
            rldxBean.setRolodexId("");
            /* JM 3-17-2015 clear flags and comments on copied rolodex record */
            rldxBean.setSponsorAddressFlag("N");
            rldxBean.setComments("");
            rldxBean.setDeleteFlag("");
            rldxBean.setStatus("A");
            rldxBean.setSponsorCode("");
            /* JM END */
            
        }
        /* call the RolodexDetail form with the same function type
         * and the RolodexDetails bean and with the connectionURL.
         */
        showRolodex('C',rldxBean,COPY_TITLE);
        
    }
    
    /**
     *  Method used to check for deletion rights for the logged in user
     *  and if allowed it will ask for confirmation to the user before deleting
     *  the actual information from the database.
     *
     *  @throws Exception
     */
    private void deleteRolodex() throws Exception{
        int selectedRow = rolodexsheet.getSelectedRow();
        String rolodexId = getSelectedRolodexId();
     /* set the requester Bean functionType
      * as 'C' to notify the servlet to check for deletion right for the
      * logged in user for the selected RolodexId.
      * set the title of the window as "MODIFY ROLODEX".
      */
        ResponderBean response = getResponseFromDB(rolodexId,'C');
        /*check whether the vector of information returned is null or not*/
        /*check whether the vector of information returned is null or not*/
        if (( response != null ) && (response.getDataObjects() != null) &&
        (response.getDataObjects().size() > 0) && (response.isSuccessfulResponse())){
            Vector responseData = response.getDataObjects();
            boolean allowDelete =
            (responseData.get(0) == null ? false :
                ((Boolean)responseData.get(0)).booleanValue());
                    /* get the second element from the vector returned by
                     * the response bean.Expected information is String object
                     * specifies the infomation
                     */
                String informationMessage =
                (responseData.get(1) == null ? "Not Available" :
                    responseData.get(1).toString());
                    /* check whether allowModify is true/false */
                    if (!allowDelete) {
                        /* If the logged in user doesn't have right to delete
                         * the selected rolodex information.Inform the user
                         * with the returned information from the servlet
                         * as informatin message.
                         */
                        throw new Exception(informationMessage);
                    }else{
                        /* If the user has right to delete the selected
                         * information confirm with the user before delting
                         * the information
                         */
                        int resultConfirm = CoeusOptionPane.showQuestionDialog(
                        informationMessage,
                        CoeusOptionPane.OPTION_YES_NO,
                        CoeusOptionPane.DEFAULT_YES);
                        if (resultConfirm == 0) {
                            response = getResponseFromDB(rolodexId,'D');
                            if (response != null){
                                /* If the deletion is successful at the server
                                 * then refresh the RolodexSheet with the latest
                                 * information.
                                 */
                                //                                ((DefaultTableModel)
                                //                                rolodexsheet.getModel()).removeRow(selectedRow);
                                //                                ((DefaultTableModel)
                                //                                rolodexsheet.getModel()).fireTableDataChanged();
                                /** Adde by chandra.
                                 *While deleting a row, delete the index and by passing selected row.
                                 *Don't delete the selectedRow,insted get the selected row index and then
                                 *delete the row
                                 * 22 Nov 20004
                                 */
                                int orgRow=0;
                                if(  selectedRow != -1 ) {
//                                    orgRow = Integer.parseInt((String)rolodexsheet.getValueAt(
//                                    selectedRow,rolodexsheet.getColumnCount()-1));
                                    /*for BugFix:while deleting mutiple rows,if we delete by using the row index 
                                     *the deletion doesnt happen in the correct order*/
                                    ((DefaultTableModel)rolodexsheet.getModel()).removeRow(selectedRow);
                                    validate();
                                }
                                int previousRow = selectedRow-1;
                                if(previousRow!=-1){
                                    rolodexsheet.setRowSelectionInterval(previousRow,previousRow);
                                }// End Chandra - 22 Nov-2004
                                if(rolodexsheet.getRowCount() > 0) {
                                    if(previousRow == -1)
                                        rolodexsheet.setRowSelectionInterval(0,0);
                                }
                            }else {
                                throw new Exception(
                                "Rolodex Info deletion operation failed for Rolodex Id " +
                                rolodexId);
                            }
                        }
                        
                    }
        }else {
            if ( (response != null ) && (!response.isSuccessfulResponse())){
                throw new Exception(response.getMessage());
            }
            throw new Exception(
            coeusMessageResources.parseMessageKey(
            "roldxBaseWin_exceptionCode.1103"));
        }
    }
    
    /**
     *  References option for Rolodex Details...
     */
    private void referencesRolodex()  throws Exception{
        int selectedRow = rolodexsheet.getSelectedRow();
        String rolodexId = getSelectedRolodexId();
        RolodexReferencesPanel rolodexReferencesPanel=new RolodexReferencesPanel(mdiForm,rolodexId);
    }
    /**
     *  Method used to get the selected RolodexId in the Rolodex sheet
     *
     *  @return String selected RolodexId
     *  @throws Exception
     */
    private String getSelectedRolodexId() throws Exception{
        String rolodexId = "";
        if ( ((
        (DefaultTableModel)
        rolodexsheet.getModel()).getRowCount()
        == 0) ||
        (rolodexsheet.getSelectedRow() < 0) ) {
                /* If the there is no row selected in RolodexWindow  ...raise
                 * and error message to the user
                 */
            throw new Exception(coeusMessageResources.parseMessageKey(
            "roldxBaseWin_exceptionCode.1104"));
            
        }else {
                /* If there is  a row selected in Rolodex Window get the first
                 * column value in the selected row from the table
                 * assign it to the rolodexId variable
                 */
            rolodexId =
            rolodexsheet.getValueAt(
            rolodexsheet.getSelectedRow(),
            0).toString();
                /* Call this method to get the rolodex window with proper
                 * checking for modifying the Rolodex info*/
            
        }
        return rolodexId;
        
    }
    
    
    /**
     * This is a generic method used to communicate with the servlet by the
     * requester bean.
     * This returns the response from the Database to the called method
     *
     *  @return ResponderBean
     *  @throws Exception
     */
    private ResponderBean getResponseFromDB(Object keyValue,char functionType ){
        RequesterBean requester = new RequesterBean();
        ResponderBean response=null;
        
        requester.setFunctionType(functionType);
        /*set the RolodexId as dataObject for the requester bean  */
        requester.setDataObject(keyValue);
        /*send the requesterBean to the servlet */
        response = rldxController.sendToServer(ROLODEX_SERVLET,requester);
        return response;
    }
    
    
    /**
     * This is a generic method used to show the RolodexMaintenance details form
     *
     * @param functionType
     * @param RolodexDetailsBean
     * @param title
     */
    private void showRolodex(char functionType,RolodexDetailsBean rldxBean,
    String title){
        
      /* Call the RolodexDetails form with functionType
       * as functionType  and RolodexDetailsBean as inormation
       */
        frmRolodex = new RolodexMaintenanceDetailForm(functionType,
        rldxBean);
        //added by ravi for implementing observer pattern
        int selRow = rolodexsheet.getSelectedRow();
        if(  functionType != 'V') {
            frmRolodex.registerObserver( this );
            if( selRow != -1 ) {
                baseTableRow = Integer.parseInt((String)rolodexsheet.getValueAt(
                selRow,rolodexsheet.getColumnCount()-1));
            }
        }
        
        /* Show the RolodexDetails form in display
         * mode to the user
         */
        
        frmRolodex.showForm(mdiForm,title,true);
        /*if (functionType == 'I' || functionType == 'C' ) {
            //int selRow =rolodexsheet.getRowCount();
            if (frmRolodex.getRolodexRowData() != null ) {
                insertRowInBaseWinow(frmRolodex.getRolodexRowData());
            }else{
            }
        }else if  (functionType == 'M') {
            if ( ((
            (DefaultTableModel)
            rolodexsheet.getModel()).getRowCount()
            == 0) ||
            (rolodexsheet.getSelectedRow() < 0) ) {
           // If the there is no row selected in RolodexWindow  ...raise
           //  and error message to the user
         
                // throw new Exception("Please select a rolodex entry to modify");
            }else {
                selRow = rolodexsheet.getSelectedRow();
                if (frmRolodex.getRolodexRowData() != null ) {
                    updateRowInBaseWinow(rolodexsheet.getSelectedRow(),
                    frmRolodex.getRolodexRowData());
                }
            }
        }*/
    }
    
    /**
     * This is a generic method to refresh the rolodex sheet after
     * modifying the existing rolodex information.
     *
     * @param rowNumber
     * @param rowData
     */
    private void updateRowInBaseWinow(int rowNumber, Vector rowData){
        if (rowData!=null) {
            rowData.addElement("");
            ((DefaultTableModel)rolodexsheet.getModel()).removeRow(rowNumber);
            ((DefaultTableModel)rolodexsheet.getModel()).insertRow(rowNumber,rowData);
            rolodexsheet.setRowSelectionInterval(rowNumber,rowNumber);
            validate();
        }
    }
    
    
    /**
     * This is a generic method to refresh the rolodex sheet after
     * adding the new rolodex information.
     *
     * @param rowData
     */
    private void insertRowInBaseWinow(Vector rowData){
        if (rowData!=null) {
            
            rowData.addElement("");
            ((DefaultTableModel)rolodexsheet.getModel()).insertRow(0,rowData);
            rolodexsheet.setRowSelectionInterval(0,0);
            
            validate();
        }else{
        }
    }
    
    /**
     * This method called from Save Menu Item under File Menu.
     * Implemented the abstract class declared in parent(CoeusInternalFrame).
     * Empty body since the Save operation is not required for this list screen.
     */
    public void saveActiveSheet() {
    }
    
    public void update(Observable observable, Object obj) {
        if( obj instanceof RolodexDetailsBean ) {
            RolodexDetailsBean rldxBean = (RolodexDetailsBean) obj;
            //            if (((BaseWindowObservable)observable).getFunctionType() == 'I') {
            if ((((BaseWindowObservable)observable).getFunctionType() == 'I') || (((BaseWindowObservable)observable).getFunctionType() == 'C')){
                Vector rowData = new Vector();
                rowData.add(rldxBean.getRolodexId());
                rowData.add(rldxBean.getSponsorCode());
                rowData.add(rldxBean.getLastName());
                rowData.add(rldxBean.getFirstName());
                rowData.add(rldxBean.getMiddleName());
                rowData.add(rldxBean.getSuffix());
                rowData.add(rldxBean.getPrefix());
                rowData.add(rldxBean.getTitle());
                rowData.add(rldxBean.getOrganization());
                 //COEUSQA-1528 Add the functionality to set a status on a Rolodex entry START 
           if(ACTIVE_STATUS.equalsIgnoreCase(rldxBean.getStatus().trim())) {
                rowData.add(ACTIVE);
           }
           else if(INACTIVE_STATUS.equalsIgnoreCase(rldxBean.getStatus().trim())) {
                rowData.add(INACTIVE);
           }
                 //COEUSQA-1528 Add the functionality to set a status on a Rolodex entry END
                rowData.add(rldxBean.getAddress1());
                rowData.add(rldxBean.getAddress2());
                rowData.add(rldxBean.getAddress3());
                rowData.add(rldxBean.getFax());
                rowData.add(rldxBean.getEMail());
                rowData.add(rldxBean.getCounty());
                rowData.add(rldxBean.getCity());
                rowData.add(rldxBean.getState());
                //                rowData.add(rldxBean.getState());
                rowData.add(rldxBean.getPostalCode());
                rowData.add(rldxBean.getComments());
                rowData.add(rldxBean.getPhone());
                rowData.add(rldxBean.getCountry());
                              
                //for index which came from search
                int lastRow = rolodexsheet.getRowCount();
                rowData.add(""+lastRow);
                if( lastRow >= 0 ) {
                    ((DefaultTableModel)rolodexsheet.getModel()).insertRow(lastRow,rowData);
                    /*added for BugFix:1546&1547*/
                    rolodexsheet.setValueAt(new Integer(lastRow).toString(),lastRow,rolodexsheet.getColumnCount()-1);
                }else{
                    
                    ((DefaultTableModel)rolodexsheet.getModel()).insertRow(0,rowData);
                    /*added for BugFix:1546&1547*/
                    rolodexsheet.setValueAt(new Integer(0).toString(),0,rolodexsheet.getColumnCount()-1);
                    
                }
                baseTableRow = lastRow; //tblProtocolsheet.getSelectedRow();
                
            }else if( ((BaseWindowObservable)observable).getFunctionType() == 'M'){
                ((DefaultTableModel)rolodexsheet.getModel()).setValueAt(
                rldxBean.getRolodexId(),baseTableRow,0);
                ((DefaultTableModel)rolodexsheet.getModel()).setValueAt(
                rldxBean.getSponsorCode(),baseTableRow,1);
                ((DefaultTableModel)rolodexsheet.getModel()).setValueAt(
                rldxBean.getLastName(),baseTableRow,2);
                ((DefaultTableModel)rolodexsheet.getModel()).setValueAt(
                rldxBean.getFirstName(),baseTableRow,3);
                ((DefaultTableModel)rolodexsheet.getModel()).setValueAt(
                rldxBean.getMiddleName(),baseTableRow,4);
                ((DefaultTableModel)rolodexsheet.getModel()).setValueAt(
                rldxBean.getSuffix(),baseTableRow,5);
                ((DefaultTableModel)rolodexsheet.getModel()).setValueAt(
                rldxBean.getPrefix(),baseTableRow,6);
                ((DefaultTableModel)rolodexsheet.getModel()).setValueAt(
                rldxBean.getTitle(),baseTableRow,7);
                ((DefaultTableModel)rolodexsheet.getModel()).setValueAt(
                rldxBean.getOrganization(),baseTableRow,8);
                 //COEUSQA-1528 Add the functionality to set a status on a Rolodex entry START 
                if(ACTIVE_STATUS.equalsIgnoreCase(rldxBean.getStatus().trim())) {
                    ((DefaultTableModel)rolodexsheet.getModel()).setValueAt(ACTIVE,baseTableRow,9);
                }
                else if(INACTIVE_STATUS.equalsIgnoreCase(rldxBean.getStatus().trim())) {
                   ((DefaultTableModel)rolodexsheet.getModel()).setValueAt(INACTIVE,baseTableRow,9);
                }
                //COEUSQA-1528 Add the functionality to set a status on a Rolodex entry START 
                ((DefaultTableModel)rolodexsheet.getModel()).setValueAt(
                rldxBean.getAddress1(),baseTableRow,10);
                ((DefaultTableModel)rolodexsheet.getModel()).setValueAt(
                rldxBean.getAddress2(),baseTableRow,11);
                ((DefaultTableModel)rolodexsheet.getModel()).setValueAt(
                rldxBean.getAddress3(),baseTableRow,12);
                ((DefaultTableModel)rolodexsheet.getModel()).setValueAt(
                rldxBean.getFax(),baseTableRow,13);
                ((DefaultTableModel)rolodexsheet.getModel()).setValueAt(
                rldxBean.getEMail(),baseTableRow,14);
                ((DefaultTableModel)rolodexsheet.getModel()).setValueAt(
                rldxBean.getCounty(),baseTableRow,15);
                ((DefaultTableModel)rolodexsheet.getModel()).setValueAt(
                rldxBean.getCity(),baseTableRow,16);
                ((DefaultTableModel)rolodexsheet.getModel()).setValueAt(
                rldxBean.getState(),baseTableRow,17);
                //                ((DefaultTableModel)rolodexsheet.getModel()).setValueAt(
                //                    rldxBean.getState(),baseTableRow,17);
                ((DefaultTableModel)rolodexsheet.getModel()).setValueAt(
                rldxBean.getPostalCode(),baseTableRow,18);
                ((DefaultTableModel)rolodexsheet.getModel()).setValueAt(
                rldxBean.getComments(),baseTableRow,19);
                ((DefaultTableModel)rolodexsheet.getModel()).setValueAt(
                rldxBean.getPhone(),baseTableRow,20);
                ((DefaultTableModel)rolodexsheet.getModel()).setValueAt(
                rldxBean.getCountry(),baseTableRow,21);
                
                // sorter will select the row after modification
                int selRow = rolodexsheet.getSelectedRow();
                if(  selRow != -1 ) {
                    baseTableRow = Integer.parseInt((String)rolodexsheet.getValueAt(
                    selRow,rolodexsheet.getColumnCount()-1));
                }
                
            }
            
        }
    }
    
    public void saveAsActiveSheet() {
        SaveAsDialog saveAsDialog = new SaveAsDialog(rolodexsheet);
    }
    
    /** Making a server side call to get the right details of the user. If the User
     *doesn't have OSP level right, i.e., SYSTEM_MAINTAINACE disable Reference
     *menu item and reference toolbar button. If the user doesn't have other unit level
     *rights i.e., ADD_ROLODEX, MAINTAIN_ROLODEX disable Copy, Add menu items and
     *corresponding tool bar buttons.
     *This method will return vector of two boolean objects. One contains the
     *OSP Right i.e., SYSTEM_MAINTAINANCE and other contains the ADD_ROLODEX and
     *MAINITAIN_ROLODEX boolean object
     *
     *Added by chandra - Jan 17 2004
     */
    private Vector checkUserRight(){
        Vector vecRoldexRights = null;
        RequesterBean request = new RequesterBean();
        request.setDataObjects(vecRolRights);
        request.setFunctionType(ROLODEX_RIGHT);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response.isSuccessfulResponse()) {
            if(response.getDataObjects() != null){
                vecRoldexRights = (Vector)response.getDataObjects();
            }else{
                CoeusOptionPane.showErrorDialog(response.getMessage()) ;
            }
        }else{
            CoeusOptionPane.showErrorDialog(response.getMessage());
        }
        return vecRoldexRights;
    }// End of checkUserRight method -  Added by chandra end- Jan 17 2004
    
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
    class RolodexMouseAdapter extends MouseAdapter{
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
                newSortedData.addElement(rolodexsheet.getColumnName(column));
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
    
}
