/*
 * @(#)PersonBaseWindow.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

/* PMD check performed, and commented unused imports and variables on 15-MAY-2007
 * by Leena
 */
package edu.mit.coeus.departmental.gui;

//import edu.mit.coeus.gui.CoeusAppletMDIForm;
//import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.utils.saveas.SaveAsDialog;
import edu.mit.coeus.departmental.bean.DepartmentPersonFormBean;
//import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.gui.event.BlockingGlassPane;
import edu.mit.coeus.gui.menu.CoeusFileMenu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import java.util.Hashtable;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;


/** <CODE>PersonBaseWindow</CODE> is a form object which display
 * all the person details and can be used to
 * <CODE>add/modify/display</CODE> the person details
 * This class is instantiated in <CODE>CoeusDepartmentalMenu</CODE>.
 * This class is used to construct the parent window of Person and associate
 * its menu and toolbar with listener. This window will display the Pesron list
 * from which the user can select the person details.
 *
 * @author  Raghunath P.V.
 * @version:
 */

public class PersonBaseWindow extends CoeusInternalFrame implements ActionListener, TypeConstants {
    
    private final String PERSON_SERVLET = "/personMaintenanceServlet";
    private static final char GET_REPORT_DATA = 'K';
    
    private AppletServletCommunicator comm;
    private String connectTo = CoeusGuiConstants.CONNECTION_URL + PERSON_SERVLET;
    private final String PERSON_SEARCH = "personSearch";
//    private final String TITLE_PERSON = "Person Details";
//    private static final String GET_BIOGRAPHY_PDF_DETAILS_FOR_PERSON  = "getBiographyPDFDetails";
    private static final String GET_BIOGRAPHY_DETAILS_FOR_PERSON  = "getBiographyDetails";
    
    //Commented for Coeus 4.3 PT ID - 2388:Person Enhancements - start
    //Instead of DEPARTMENT_PERSON_MODULE, PERSONNEL_MODULE_CODE is used to identify the Person module
    //is taken through the Personnel from the Departmental menu.
    //private static final char DEPARTMENT_PERSON_MODULE = 'D';
    //Commented for Coeus 4.3 PT ID - 2388:Person Enhancements - end
    
    //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - start
    //used to denote the form is taken from the Personnel module of the Maintain Menu
    private static final char PERSONNEL_MODULE_CODE = 'N';
    //holds the function type to check whether the user has modify right
    private static final char USER_HAS_MODIFY_PERSON_RIGHT = 'R';
    //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - end
    
//    private static final int N_MODIFY_PROPOSAL = 0;
    private static char ADDPERSON_RIGHT_FN_TYPE='J';
    private static final String SELECT_PERSON_FOR_REPORT = "currentPendingReport_exceptionCode.1100";
    
    private CoeusAppletMDIForm mdiForm;
    private CoeusSearch coeusSearch;
    private DepartmentPersonFormBean departmentPersonFormBean;
    private final String SEPERATOR="seperator";
    private CoeusMenu menuPerson = null;
    private static BlockingGlassPane blockingGlassPane;
    //private int selectedPersonRow = -1;
    
    // Jtable
    private JTable tblPerson;
    // Scroll bar pane
    private JScrollPane scrPnSearchRes;
    private CoeusMessageResources coeusMessageResources;
    
    private String personId;
//    private String perId;
    private boolean canMaintainBiography;
    private boolean userCanMaintainUnit;
    private boolean userCanMaintainSelectedPerson;
    private boolean isBiographyMaintained;
//    private boolean hasAddpersonRight=false;
    
    //Modified for Coeus 4.3 Person Enhancements - start
    //Added the modify menuitem
    private CoeusMenuItem personAdd,personModify, personDisplay,personDegreeInfo,
            personBiographicalInfo,personReport,personTraining,personDisclosure,
            personSearch;
    
    //Added toolbar buttons for adding and modifying person
    private CoeusToolBarButton btnAddPerson, btnModifyPerson, btnDisplayPerson, btnDisplayDegree,
            btnDisplayBiography, btnReport, btnTrainingInformation, btnClose,
            btnSearch,btnSaveAs;
    
    //holds whether the user has the right to add a person
    private boolean userHasAddRight = false;
    //holds whether the user has the right to  modify a person
    private boolean userHasModifyRight = false;
    //Modified for Coeus 4.3 Person Enhancements - end
    
    private CoeusToolBarButton btnSort;//Added by nadh - 19/01/2005 for Sort ToolBar Button
    
    //Added by nadh - 19/01/2005 for Sort ToolBar Button - Start
    
    //holds sorted columns and its states
    private Vector vecSortedData;
    
    private static final int OK_CLICKED = 0;
    
    //holds column index
    private int oldCol = -1;
    
    //holds column status
    private int status=MultipleTableColumnSorter.NOT_SORTED;
    //Added by Nadh - End
    //Added for bug fixed for case #2376 start 1
    JMenu fileMenu;
    //Added for bug fixed for case #2376 end 2
    
    /** Constructor to create new <CODE>PersonBaseWindow</CODE>
     *
     * @param mdiForm reference to <CODE>CoeusAppletMDIForm</CODE>.
     */
    
    public PersonBaseWindow(CoeusAppletMDIForm mdiForm) {
        
        super("Person", mdiForm);
        this.mdiForm = mdiForm;
        try {
            initComponents();
            mdiForm.putFrame(CoeusGuiConstants.PERSON_BASE_FRAME_TITLE, this);
            mdiForm.getDeskTopPane().add(this);
            this.setSelected(true);
            this.setVisible(true);
            showPersonSearchWindow();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //Added for bug fixed for case #2376 start 2
    private void getPersonFileMenu() {
        CoeusFileMenu coeusFile = new CoeusFileMenu(mdiForm);
        coeusFile.formatMenuItems(CoeusInternalFrame.LIST_MODE);
        fileMenu = coeusFile.getMenu();
        mdiForm.getCoeusMenuBar().remove(0);
        mdiForm.getCoeusMenuBar().add(fileMenu, 0);
        mdiForm.getCoeusMenuBar().validate();
    }
    public void internalFrameActivated(InternalFrameEvent internalFrameEvent) {
        super.internalFrameActivated(internalFrameEvent);
        getPersonFileMenu();
    }
    
    public void internalFrameDeactivated(InternalFrameEvent internalFrameEvent) {
        mdiForm.getCoeusMenuBar().remove(fileMenu);
        mdiForm.getCoeusMenuBar().add(new CoeusFileMenu(mdiForm).getMenu(), 0);
        mdiForm.getCoeusMenuBar().validate();
        super.internalFrameDeactivated(internalFrameEvent);
        
    }
    //Added for bug fixed for case #2376 end 2
    /**
     * Construct the components for the base window
     *
     * throws Exception
     */
    private void initComponents() throws Exception{
        setFrameMenu(getPersonEditMenu());
        setToolsMenu(getPersonToolsMenu());
        setFrameToolBar(getPersonToolBar());
        setFrame(CoeusGuiConstants.PERSON_BASE_FRAME_TITLE);
        setFrameIcon(mdiForm.getCoeusIcon());
        createPersonInfo();
        coeusMessageResources = CoeusMessageResources.getInstance();
    }
    
    //  This method builds the Tools Menu which contains search menu item.
    
    private CoeusMenu getPersonToolsMenu(){
        CoeusMenu coeusMenu;
        Vector fileChildren = new Vector();
        personSearch = new CoeusMenuItem("Search", null, true, true);
        personSearch.setMnemonic('S');
        personSearch.addActionListener(this);
        fileChildren.add(personSearch);
        coeusMenu = new CoeusMenu("Tools", null, fileChildren, true, true);
        coeusMenu.setMnemonic('T');
        return coeusMenu;
    }
    
    // This method instantiates Coeus Search and Populates person
    // search when the departmental person module is loaded.
    private void createPersonInfo() throws Exception{
        
        coeusSearch = new CoeusSearch(mdiForm, PERSON_SEARCH, 0);
        tblPerson = coeusSearch.getEmptyResTable();
        javax.swing.table.TableColumn clmName
                = tblPerson.getColumnModel().getColumn(
                tblPerson.getColumnCount()-1);
        clmName.setPreferredWidth(0);
        clmName.setMaxWidth(0);
        clmName.setMinWidth(0);
        clmName.setWidth(0);
        // Added by chandra. Person Id as an hidden column. - start
        clmName = tblPerson.getColumnModel().getColumn(tblPerson.getColumnCount()-2);
        clmName.setMaxWidth(0);
        clmName.setMinWidth(0);
        clmName.setPreferredWidth(0);
        clmName.setWidth(0);
        // Added by chandra. Person Id as an hidden column. - End
        tblPerson.addMouseListener(new CustomMouseAdapter());
        javax.swing.table.JTableHeader header
                = tblPerson.getTableHeader();
        //header.setResizingAllowed(false);
        header.setReorderingAllowed(false);
        setSearchResultsTable(tblPerson);
        scrPnSearchRes = new JScrollPane();
        scrPnSearchRes.setMinimumSize(new Dimension(22, 15));
        //scrPnSearchRes.setPreferredSize(new Dimension(600, 400));
        if( tblPerson != null ){
            tblPerson.setFont( CoeusFontFactory.getNormalFont() );
        }
        scrPnSearchRes.setViewportView(tblPerson);
        scrPnSearchRes.getViewport().setBackground(Color.white);
        scrPnSearchRes.setForeground(Color.white);
        getContentPane().add(scrPnSearchRes);
    }
    
    // This method shows the search window.
    
    private void showPersonSearchWindow(){
        
        try {
            coeusSearch.showSearchWindow();
            JTable tblResultsTable = coeusSearch.getSearchResTable();
            if (tblResultsTable != null) {
                tblPerson = tblResultsTable;
                javax.swing.table.TableColumn clmName
                        = tblPerson.getColumnModel().getColumn(
                        tblPerson.getColumnCount()-1);
                clmName.setPreferredWidth(0);
                clmName.setMaxWidth(0);
                clmName.setMinWidth(0);
                clmName.setWidth(0);
                
//                // Added by chandra. Person Id as an hidden column. - start
//                clmName
//                    = tblPerson.getColumnModel().getColumn(
//                        tblPerson.getColumnCount()-2);
//                clmName.setMaxWidth(0);
//                clmName.setMinWidth(0);
//                clmName.setPreferredWidth(0);
//                clmName.setWidth(0);
//                // Added by chandra. Person Id as an hidden column. - End
                
                javax.swing.table.JTableHeader header
                        = tblPerson.getTableHeader();
                //header.setResizingAllowed(false);
                header.setReorderingAllowed(false);
                scrPnSearchRes.setViewportView(tblPerson);
                //Added by nadh - 19/01/2005 for table header
                tblPerson.getTableHeader().addMouseListener(new CustomMouseAdapter());
                //Added by Nadh - End
                tblPerson.addMouseListener(new CustomMouseAdapter());
                //Case 2451 start
                vecSortedData = new Vector();
                //Case 2451 end
            }
            this.revalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * constructs Person edit menu with sub menu Add,Dispaly the
     * person Deatil Forms.
     * @return CoeusMenu Protocol edit menu
     */
    private CoeusMenu getPersonEditMenu(){
        if (menuPerson!=null) {
            return menuPerson;
        }
        
        Vector fileChildren = new Vector();
        
        personAdd = new CoeusMenuItem("Add Person..", null, true, true);
        //Added for Coeus 4.3 Person Enhancements - start
        //To set the shotcut key as 'a'
        personAdd.setMnemonic('a');
        
        //To add the modify person menu item
        personModify = new CoeusMenuItem("Modify Person..", null, true, true);
        personModify.setMnemonic('m');
        personModify.addActionListener(this);
        //Added for Coeus 4.3 Person Enhancements - end
        
        personDisplay = new CoeusMenuItem("Display", null, true, true);
        personDisplay.setMnemonic('i');
        personDisplay.addActionListener(this);
        
        personDegreeInfo = new CoeusMenuItem("Degree Info", null, true, true);
        personDegreeInfo.setMnemonic('f');
        personDegreeInfo.addActionListener(this);
        
        personBiographicalInfo = new CoeusMenuItem("Biographical Info", null, true, true);
        //personBiographicalInfo.setMnemonic('C');
        personBiographicalInfo.addActionListener(this);
        
        personReport = new CoeusMenuItem("Current And Pending Report", null, true, true);
        personReport.setMnemonic('R');
        personReport.addActionListener(this);
        
        personTraining = new CoeusMenuItem("Training", null, true, true);
        personTraining.setMnemonic('T');
        personTraining.addActionListener(this);
        
        personDisclosure = new CoeusMenuItem("Financial Interest Disclosure", null, true, true);
        personDisclosure.setMnemonic('F');
        personDisclosure.addActionListener(this);
        //Modified for Coeus 4.3 Person Enhancements - start
        // To check whether the user has Add person right
        userHasAddRight = isUserHavingAddPersonRights();
        //To check whether the user has right to modify a person
        userHasModifyRight = isUserHavingModifyPersonRights();
        fileChildren.add(personAdd);
        if(!userHasAddRight) {
            personAdd.setEnabled(false);
            //Commented to move the seperator below the display
            //fileChildren.add(SEPERATOR);
        }
        fileChildren.add(personModify);
        if(!userHasModifyRight){
            personModify.setEnabled(false);
        }
        fileChildren.add(personDisplay);
        fileChildren.add(SEPERATOR);
        //Added for Coeus 4.3 Person Enhancements - end
        fileChildren.add(personDegreeInfo);
        fileChildren.add(personBiographicalInfo);
        fileChildren.add(personReport);
        fileChildren.add(personTraining);
        //bug fix done by shiji - bug id:1846 - start
        //fileChildren.add(SEPERATOR);
        // fileChildren.add(personDisclosure);
        //bug id : 1846 - end
        
        menuPerson = new CoeusMenu("Edit", null, fileChildren, true, true);
        menuPerson.setMnemonic('E');
        return menuPerson;
    }
    
    /**
     * This method is used to create the tool bar for Save, Add, Mofify and
     * Close buttons.
     *
     * @returns JToolBar person Toolbar
     */
    private JToolBar getPersonToolBar(){
        
        JToolBar personToolBar = new JToolBar();
        
        //Added for Coeus 4.3 Person Enhancements - start
        //Added toolbar buttons for adding and modifying person
        btnAddPerson = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.ADD_ICON)),
                null, "Add a new Person");
        btnModifyPerson = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.EDIT_ICON)),
                null, "Modify selected Person");
        //Added for Coeus 4.3 Person Enhancements - end
        
        btnDisplayPerson = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.DISPLAY_ICON)),
                null, "Display Person Details");
        
        btnDisplayDegree = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.QUALIFICATIONS_ICON)),
                null, "Show educational qualifications of the selected person");
        
        
        btnDisplayBiography = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.BIOGRAPHY_ICON)),
                null, "Show Biographical information of the person");
        
        btnReport = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.REPORT_ICON)),
                null, "Current and Pending Report");
        
        btnTrainingInformation = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.TRAINING_ICON)),
                null, "Training information of the selected person");
        
        btnSearch = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)),
                null, "Search");
        
        btnSaveAs = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.SAVEAS_ICON)),
                null,"Save As");
        
        btnClose = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
                null, "Close");
        
        //Added by nadh - 19/01/2005 for Sort ToolBar Button - Start
        //Modified by shiji for fixing bug id : 1847 : start
        btnSort = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.SORT_ICON)),null,
                "Sort Person");
        //bug id : 1847 : end
        //Added by Nadh - End
        
        //Added for Coeus 4.3 Person Enhancements - start
        //Added toolbar buttons for adding and modifying person
        btnAddPerson.addActionListener(this);
        btnModifyPerson.addActionListener(this);
        //Added for Coeus 4.3 Person Enhancements - end
        
        btnDisplayPerson.addActionListener(this);
        btnDisplayDegree.addActionListener(this);
        btnDisplayBiography.addActionListener(this);
        btnReport.addActionListener(this);
        btnTrainingInformation.addActionListener(this);
        btnSearch.addActionListener(this);
        btnClose.addActionListener(this);
        btnSaveAs.addActionListener(this);
        personAdd.addActionListener(this);
        
        
        btnSort.addActionListener(this);//Added by nadh - 19/01/2005 for Sort ToolBar Button
        
        //Added for Coeus 4.3 Person Enhancements - start
        //Added toolbar buttons for adding and modifying person
        personToolBar.add(btnAddPerson);
        if(!userHasAddRight){
            btnAddPerson.setEnabled(false);
        }
        personToolBar.add(btnModifyPerson);
        if(!userHasModifyRight){
            btnModifyPerson.setEnabled(false);
        }
        //Added for Coeus 4.3 Person Enhancements - end
        personToolBar.add(btnDisplayPerson);
        personToolBar.add(btnDisplayDegree);
        personToolBar.add(btnDisplayBiography);
        personToolBar.add(btnReport);
        personToolBar.add(btnTrainingInformation);
        personToolBar.add(btnSort);//Added by nadh - 19/01/2005 for Sort ToolBar Button
        personToolBar.add(btnSearch);
        personToolBar.add(btnSaveAs);
        
        personToolBar.addSeparator();
        personToolBar.add(btnClose);
        
        personToolBar.setFloatable(false);
        return personToolBar;
    }
    
    public void saveActiveSheet() {
    }
    
    String personName;
    
    /**
     * This method is invokeed when ever Action Event is triggered in the Form
     */
    public void actionPerformed(ActionEvent actionType) {
        try{
            int selectedRow = 0;
            String personId = null;
            personName = "";
            if (tblPerson != null && tblPerson.getRowCount() > 0) {
                selectedRow = tblPerson.getSelectedRow();
                if (selectedRow >= 0) {
                    personId = tblPerson.getValueAt(selectedRow, 0).toString();
                    personName = tblPerson.getValueAt(selectedRow, 1).toString();
                }
            }
            Object actSource = actionType.getSource();
            if (actSource.equals(personDisplay) || actSource.equals(btnDisplayPerson)) {
                //Modified for Coeus 4.3 Person Enhancements - start
                //changed the mode to display mode when taken through the diaplay
                //displayPersonDetails(personId, MODIFY_MODE);
                displayPersonDetails(personId, DISPLAY_MODE);
                //Modified for Coeus 4.3 Person Enhancements - end
            }else if (actSource.equals(btnClose)) {
                closePersonBaseWindow();
            }else if (actSource.equals(personDegreeInfo) || actSource.equals(btnDisplayDegree)) {
                displayDegreeDetails(personId);
            }else if (actSource.equals(personBiographicalInfo) || actSource.equals(btnDisplayBiography)) {
                displayBiographyDetails(personId);
            }else if (actSource.equals(btnSaveAs)) {
                saveAsActiveSheet();
            }else if(actSource.equals(personTraining)|| actSource.equals(btnTrainingInformation)){
                //Modified for Case#4346 -  Person training information not consistently displayed, saved -Start
                //showTrainingInfo();
                showTrainingInfo(personId);
                //Case#4346 - End
            }
            //Modified for Coeus 4.3 Person Enhancements - start
            //To include the btnAddPerson
            else if(actSource.equals(personAdd) || actSource.equals(btnAddPerson)){
                //Modified for Coeus 4.3 Person Enhancements - end
                addPerson();
            }else if(actSource.equals(personReport) || actSource.equals(btnReport)){
                blockEvents(true);
                showCurrentAndPendingReports();
            }
            //Added by nadh - 19/01/2005 for Sort ToolBar Button - Start
            else if (actSource.equals(btnSort)) {
                showSort();
            }//Added by Nadh - End
            if (actSource.equals(personSearch) || actSource.equals(btnSearch)) {
                showPersonSearchWindow();
            }
            //Modified for Coeus 4.3 Person Enhancements - start
            //To include the modify functionality
            else if(actSource.equals(btnModifyPerson) || actSource.equals(personModify)){
                displayPersonDetails(personId , MODIFY_MODE);
            }
            //Modified for Coeus 4.3 Person Enhancements - end
        }catch (CoeusClientException  coeusClientException){
            CoeusOptionPane.showDialog(coeusClientException);
        }catch( Exception err ){
            err.printStackTrace();
            log( err.getMessage());
        } finally{
            blockEvents(false);
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
        SortForm sortForm = new SortForm(tblPerson,vecSortedData);
        Vector sortedData = sortForm.display();
        vecSortedData = (Vector)sortedData.get(1);
        if(((Integer)sortedData.get(0)).intValue() == OK_CLICKED)
            coeusSearch.sortByColumns(tblPerson,vecSortedData);
        else
            return;
    }// Added by Nadh - end
    
    /** This utility method is used for setting the Hour Glass Icon while opening the
     *screen
     */
    public void blockEvents(boolean block) {
        if(blockingGlassPane == null) {
            blockingGlassPane = new BlockingGlassPane();
            CoeusGuiConstants.getMDIForm().setGlassPane(blockingGlassPane);
        }
        blockingGlassPane.block(block);
    }
    
    // Added by chandra to show the currentAndPending Report details
    private void showCurrentAndPendingReports() throws CoeusClientException{
        try{
            CurrentAndPendingReportDetailForm  currentAndPendingReportDetailForm;
            int selectedRow = tblPerson.getSelectedRow();
            if(selectedRow!= -1){
                String personName = (String)tblPerson.getValueAt(selectedRow, 1);
                //modified with case 3505 : Personid is the 0th element - Start
//                int col = tblPerson.getColumnCount()-2;
                String personId = (String)tblPerson.getValueAt(selectedRow, 0);
                //Case 3505 : end
                Hashtable reportingData = getCurrentPendingData(personId);
                // Check if the internalFrame is opened, if yes then pass the data..
                // No need to initialize the form components again
                if( (currentAndPendingReportDetailForm = (CurrentAndPendingReportDetailForm)mdiForm.getFrame(
                        CoeusGuiConstants.CURRENT_AND_PENDING_SUPPORT))!= null ){
                    if( currentAndPendingReportDetailForm.isIcon() ){
                        currentAndPendingReportDetailForm.setIcon(false);
                    }
                    currentAndPendingReportDetailForm.setHeaderValue(personName);
                    currentAndPendingReportDetailForm.setReportData(reportingData);
                    currentAndPendingReportDetailForm.setFormData();
                    currentAndPendingReportDetailForm.setSelected( true );
                    return;
                }
                currentAndPendingReportDetailForm = new CurrentAndPendingReportDetailForm(CoeusGuiConstants.CURRENT_AND_PENDING_SUPPORT, mdiForm);
                currentAndPendingReportDetailForm.initComponents();
                currentAndPendingReportDetailForm.setHeaderValue(personName);
                currentAndPendingReportDetailForm.setReportData(reportingData);
                currentAndPendingReportDetailForm.setFormData();
            }else{
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_PERSON_FOR_REPORT));
            }
        }catch (CoeusClientException coeusClientException){
            coeusClientException.printStackTrace();
            CoeusOptionPane.showDialog(coeusClientException);
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }//end showCurrentAndPendingReports()
    
    // Making server call to get the current and Pending report details
    // Added by chandra
    private Hashtable getCurrentPendingData(String personId) throws CoeusClientException{
        Hashtable data=null;
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType(GET_REPORT_DATA);
        //COEUSQA:3298 - C&P report does not generate from the Person module - Null Pointer Exception Error - Start
        //requesterBean.setDataObject(personId);
        CoeusVector cvDataObject = new CoeusVector();
        cvDataObject.add(personId);
        cvDataObject.add(true);
        requesterBean.setDataObjects(cvDataObject);
        //COEUSQA:3298 - End
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requesterBean);
        comm.setRequest(requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean!= null){
            if(responderBean.isSuccessfulResponse()) {
                data = (Hashtable)responderBean.getDataObject();
            }else{
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
                
            }
        }
        return data;
    }// end getCurrentPendingData(String personId)
    
    // added by Bijosh
    
    private void addPerson(){
        //Modified for Coeus 4.3 Person Enhancements - start
        //AddMITPersonPanel is not used to add a perosn, instead PersonDetailForm is used
//        AddMITPersonPanel addMITPersonPanel;
//        addMITPersonPanel = new AddMITPersonPanel(mdiForm);
//
//        departmentPersonFormBean=addMITPersonPanel.display();
        PersonDetailForm personDetailForm = new PersonDetailForm(ADD_MODE,PERSONNEL_MODULE_CODE);
        personDetailForm.setTblPropPerson(tblPerson);
        personDetailForm.showPersonDetailForm();
        departmentPersonFormBean = personDetailForm.getDeptPersonFormBean();
        if (departmentPersonFormBean!=null && personDetailForm.getSaveRequired()) {
            Vector newRowData = new Vector();
            //To add the person id data
            newRowData.addElement( departmentPersonFormBean.getPersonId());
            newRowData.addElement( departmentPersonFormBean.getFullName() );
            newRowData.addElement( departmentPersonFormBean.getPriorName() );
            newRowData.addElement( departmentPersonFormBean.getUserName() );
            newRowData.addElement( departmentPersonFormBean.getHomeUnit() );
            //To add the school data
            newRowData.addElement( departmentPersonFormBean.getSchool());
            newRowData.addElement( departmentPersonFormBean.getEmailAddress() );
            newRowData.addElement( departmentPersonFormBean.getDirTitle() );
            newRowData.addElement( departmentPersonFormBean.getOfficeLocation() );
            newRowData.addElement( departmentPersonFormBean.getOfficePhone() );
            if (departmentPersonFormBean.getFaculty()) {
                newRowData.addElement( "Y" );
            } else {
                newRowData.addElement( "N" );
            }
            newRowData.addElement( departmentPersonFormBean.getSecOfficeLocation() );
            //To add the secondary office phone
            newRowData.addElement(departmentPersonFormBean.getSecOfficePhone());
            //Modified for Coeus 4.3 Person Enhancements - end
            int lastRow = tblPerson.getRowCount();
            newRowData.addElement(""+lastRow);
            
            if( lastRow > 0 ) {
                ((DefaultTableModel)tblPerson.getModel()).insertRow(lastRow,newRowData);
            }else{
                ((DefaultTableModel)tblPerson.getModel()).insertRow(0,newRowData);
                tblPerson.setRowSelectionInterval(0,0);
            }
        }
        
    }
    
    // Added by chandra
    // Listener for the Training Information
    //Modified for Case#4346 -  Person training information not consistently displayed, saved -Start
    //private void showTrainingInfo(){
    private void showTrainingInfo(String personId){//Case#4346 - End
        PersonTrainingInformationForm  personTrainingInformationForm;
        int selectedRow = tblPerson.getSelectedRow();
        if(selectedRow!= -1){
            String personName = (String)tblPerson.getValueAt(selectedRow, 1);
            // get the person id from the hidden column
            int col = tblPerson.getColumnCount()-2;
            //Commented for Case#4346 -  Person training information not consistently displayed, saved
            //String personId = (String)tblPerson.getValueAt(selectedRow, col);
            personTrainingInformationForm = new PersonTrainingInformationForm(mdiForm, personId,personName);
        }else{
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("person_trainingCode_excetionCode.1101"));
        }
    }
    /**
     * This method is invoked when the user clicks display in the Display menu
     * of Person module
     */
    private void displayBiographyDetails(String pName){
        if (pName == null) {
            log(coeusMessageResources.parseMessageKey(
                    "protoBaseWin_exceptionCode.1052"));
        }else {
            try{
                int selRow = tblPerson.getSelectedRow();
                String personName = (String)tblPerson.getValueAt(selRow, 1);
                String title = "Biography for "+personName;
                String loginName = mdiForm.getUserName();
//                String title = "Biography for "+pName;
                //Vector vecPdfData = getBiographyPDFData(pName);
                Vector vecBiographyData = getBiographyData(pName);
                
                if(pName.equalsIgnoreCase(loginName)){
                    this.canMaintainBiography = true;
                }
                
                if(canMaintainBiography || userCanMaintainSelectedPerson || userCanMaintainUnit){
                    isBiographyMaintained = true;
                }else{
                    isBiographyMaintained = false;
                }
                
                // Edit >> View PDF File , Edit >> Word Files >> Save to Disk, Edit >> PDF Files >> Save to Disk
                // Above will be disabled..
                
                BiographyBaseWindow biographyBaseWindow = new BiographyBaseWindow(title, mdiForm);
                biographyBaseWindow.setPersonName(pName);
                biographyBaseWindow.setPersonId(personId);
                
                biographyBaseWindow.setLoginName(loginName);
                
                if(vecBiographyData != null){
                    if(!isBiographyMaintained){
                        //user having biography records and on whom the logged in user has no rights
                        // toolbar buttons [New], [Delete] and [Save] Disable
                        // set the fuction type as D
                        biographyBaseWindow.setFunctionType(DISPLAY_MODE);
                        biographyBaseWindow.setBiographyData(vecBiographyData);
                        biographyBaseWindow.showWindow();
                    }else{
                        //user having biography records and on whom the logged in user has rights
                        // toolbar buttons [New], [Delete] and [Save]  Enable
                        // set the fuction type as M
                        biographyBaseWindow.setFunctionType(MODIFY_MODE);
                        biographyBaseWindow.setBiographyData(vecBiographyData);
                        biographyBaseWindow.showWindow();
                    }
                    
                }else{
                    if(!isBiographyMaintained){
                        //having no biography records and on whom the logged in user has no rights.
                        CoeusOptionPane.showErrorDialog("There is no biography for this person");
                    }else{
                        //user having no biography records and on whom the logged in user has rights
                        // toolbar buttons [New], [Delete] and [Save]  Enable
                        // set the fuction type as M or A                           //check this
                        biographyBaseWindow.setFunctionType(MODIFY_MODE);
                        biographyBaseWindow.setBiographyData(vecBiographyData);
                        biographyBaseWindow.showWindow();
                    }
                }
                
                /*
                if(!isBiographyMaintained && vecBiographyData == null){
                    CoeusOptionPane.showErrorDialog("There is no biography for this person");
                }else{
                    BiographyBaseWindow biographyBaseWindow = new BiographyBaseWindow(title, mdiForm);
                    biographyBaseWindow.setPersonName(pName);
                    biographyBaseWindow.setLoginName(loginName);
                    biographyBaseWindow.setFunctionType(MODIFY_MODE);
                    biographyBaseWindow.showWindow();
                }*/
            }catch( Exception err ){
                err.printStackTrace();
                CoeusOptionPane.showInfoDialog(err.getMessage());
            }
        }
    }
    
    /*
    private Vector getBiographyPDFData(String personName){
     
        Vector vecBioPdfData = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/personMaintenanceServlet";
        RequesterBean request = new RequesterBean();
        request.setId(personName);
        request.setFunctionType(MODIFY_MODE);
        request.setDataObject(GET_BIOGRAPHY_PDF_DETAILS_FOR_PERSON);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            if (response.isSuccessfulResponse()){
                Vector serverDataObject = (Vector)response.getDataObject();
                personId = (String)response.getId();
                if(serverDataObject != null){
                    vecBioPdfData = (Vector)serverDataObject.elementAt(0);
                    userCanMaintainUnit = ((Boolean)(serverDataObject.elementAt(1))).booleanValue();
                    userCanMaintainSelectedPerson = ((Boolean)(serverDataObject.elementAt(2))).booleanValue();
                }
            }
        }
        return vecBioPdfData;
    }*/
    
    // This method is used to display the biographical information
    
    private Vector getBiographyData(String personName){
        
        Vector vecBioDataFromServer= null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/personMaintenanceServlet";
        RequesterBean request = new RequesterBean();
        request.setId(personName);
        request.setFunctionType(MODIFY_MODE);
        request.setDataObject(GET_BIOGRAPHY_DETAILS_FOR_PERSON);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        
        if (response!=null){
            if (response.isSuccessfulResponse()){
                Vector serverDataObject = (Vector)response.getDataObject();
                personId = (String)response.getId();
                //               perId = personId;
                if(serverDataObject != null){
                    vecBioDataFromServer = (Vector)serverDataObject.elementAt(0);
                    userCanMaintainUnit = ((Boolean)(serverDataObject.elementAt(1))).booleanValue();
                    userCanMaintainSelectedPerson = ((Boolean)(serverDataObject.elementAt(2))).booleanValue();
                    //vecBioPdfData = (Vector)serverDataObject.elementAt(3);
                }
            }
        }
        return vecBioDataFromServer;
    }
    
    
    /**
     * This method is invoked when the user clicks display in the Display menu
     * of Person module
     */
    private void displayPersonDetails(String pName, char functionType){
        if (pName == null) {
            log(coeusMessageResources.parseMessageKey(
                    "protoBaseWin_exceptionCode.1052"));
        }else {
            try{
//                String loginName = mdiForm.getUserName();
                //PersonDetailForm personDetailForm = new PersonDetailForm(pName,loginName,functionType,DEPARTMENT_PERSON_MODULE,N_MODIFY_PROPOSAL, null );
                //Modified for Coeus 4.3 Person Enhancements - start
                //While taking PersonDetailForm through the Personnel module the module type will be PERSONNEL_MODULE_CODE
                //PersonDetailForm personDetailForm = new PersonDetailForm(functionType,DEPARTMENT_PERSON_MODULE);
                PersonDetailForm personDetailForm = new PersonDetailForm(functionType,PERSONNEL_MODULE_CODE);
                //Modified for Coeus 4.3 Person Enhancements - end
                personDetailForm.setPersonName(pName);
                personDetailForm.setTblPropPerson(tblPerson);
                personDetailForm.showPersonDetailForm();
                //Added for Coeus 4.3 Person Enhancements - start
                //To reflect the changes made while modifying in the base window
                if(functionType == MODIFY_MODE && personDetailForm.isSaveRequired() ){
                    departmentPersonFormBean = personDetailForm.getDeptPersonFormBean();
                    int baseTableRow = tblPerson.getSelectedRow();
                    edu.mit.coeus.utils.SearchColumnIndex searchColumnIndex = new edu.mit.coeus.utils.SearchColumnIndex();
                    ((DefaultTableModel)tblPerson.getModel()).setValueAt(
                            departmentPersonFormBean.getFullName(),baseTableRow,
                            searchColumnIndex.getSearchColumnIndex(coeusSearch,"FULL_NAME"));
                    
                    ((DefaultTableModel)tblPerson.getModel()).setValueAt(
                            departmentPersonFormBean.getPriorName(),baseTableRow,
                            searchColumnIndex.getSearchColumnIndex(coeusSearch,"PRIOR_NAME"));
                    
                    ((DefaultTableModel)tblPerson.getModel()).setValueAt(
                            departmentPersonFormBean.getUserName(),baseTableRow,
                            searchColumnIndex.getSearchColumnIndex(coeusSearch,"USER_NAME"));
                    
                    ((DefaultTableModel)tblPerson.getModel()).setValueAt(
                            departmentPersonFormBean.getHomeUnit(),baseTableRow,
                            searchColumnIndex.getSearchColumnIndex(coeusSearch,"HOME_UNIT"));
                    
                    ((DefaultTableModel)tblPerson.getModel()).setValueAt(
                            departmentPersonFormBean.getSchool(),baseTableRow,
                            searchColumnIndex.getSearchColumnIndex(coeusSearch,"SCHOOL"));
                    
                    ((DefaultTableModel)tblPerson.getModel()).setValueAt(
                            departmentPersonFormBean.getEmailAddress(),
                            baseTableRow,searchColumnIndex.getSearchColumnIndex(coeusSearch,"EMAIL_ADDRESS"));
                    
                    ((DefaultTableModel)tblPerson.getModel()).setValueAt(
                            departmentPersonFormBean.getDirTitle(), baseTableRow,
                            searchColumnIndex.getSearchColumnIndex(coeusSearch,"DIRECTORY_TITLE"));
                    
                    //get the PI and lead unit name/number
                    ((DefaultTableModel)tblPerson.getModel()).setValueAt(
                            departmentPersonFormBean.getOfficeLocation(), baseTableRow,
                            searchColumnIndex.getSearchColumnIndex(coeusSearch,"OFFICE_LOCATION"));
                    
                    ((DefaultTableModel)tblPerson.getModel()).setValueAt(
                            (departmentPersonFormBean.getFaculty()? "Y":"N"),baseTableRow,
                            searchColumnIndex.getSearchColumnIndex(coeusSearch,"IS_FACULTY"));
                    
                    ((DefaultTableModel)tblPerson.getModel()).setValueAt(
                            departmentPersonFormBean.getSecOfficeLocation(),
                            baseTableRow,searchColumnIndex.getSearchColumnIndex(coeusSearch,"SECONDRY_OFFICE_LOCATION"));
                    
                    ((DefaultTableModel)tblPerson.getModel()).setValueAt(
                            departmentPersonFormBean.getSecOfficePhone(),
                            baseTableRow,searchColumnIndex.getSearchColumnIndex(coeusSearch,"SECONDRY_OFFICE_PHONE"));
                }
                //Added for Coeus 4.3 Person Enhancements - end
            }catch( Exception err ){
                err.printStackTrace();
                CoeusOptionPane.showInfoDialog(err.getMessage());
            }
        }
    }
    
    /**
     * This method is invoked when the user clicks display in the degree menu
     * of Person module
     */
    private void displayDegreeDetails(String pName){
        int selRow = tblPerson.getSelectedRow();
        String personName =tblPerson.getValueAt(selRow, 1).toString();
        if (pName == null) {
            log(coeusMessageResources.parseMessageKey(
                    "protoBaseWin_exceptionCode.1052"));
        }else {
            try{
                PersonDegreeForm personDegreeForm = new PersonDegreeForm(MODIFY_MODE, CoeusGuiConstants.PERSON_MODULE);
                personDegreeForm.setPersonName(pName);
                /*to set the person name to the title of the dialog
                 and the label text instead of the person id*/
                personDegreeForm.setName(personName);
                personDegreeForm.showDegreeForm();
            }catch( Exception err ){
                err.printStackTrace();
                CoeusOptionPane.showInfoDialog(err.getMessage());
            }
        }
    }
    
    // This method Closes the Person Base Window.
    
    private void closePersonBaseWindow(){
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
    
    public void saveAsActiveSheet() {
        SaveAsDialog saveAsDialog = new SaveAsDialog(tblPerson);
    }
    /*This method is to check whether the user has the right to add a person
     */
    private boolean isUserHavingAddPersonRights() {
        RequesterBean request = new RequesterBean();
        request.setFunctionType(ADDPERSON_RIGHT_FN_TYPE);
        
        //request.setDataObject((txtId.getText()).trim());
        comm = new AppletServletCommunicator(
                connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        boolean hasRight=((Boolean)response.getDataObject()).booleanValue();
        return hasRight;
    }
    //Added for Coeus 4.3 Person Enhancements - start
     /* Checks whether the user has the right to modify a person
      * @return boolean - true if the user has right else false
      */
    private boolean isUserHavingModifyPersonRights() {
        RequesterBean request = new RequesterBean();
        request.setFunctionType(USER_HAS_MODIFY_PERSON_RIGHT);
        comm = new AppletServletCommunicator(
                connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        boolean hasRight=((Boolean)response.getDataObject()).booleanValue();
        return hasRight;
    }
    //Added for Coeus 4.3 Person Enhancements - end
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
                if(oldCol != column ){
                    sortStatus = MultipleTableColumnSorter.NOT_SORTED;
                }
                sortStatus = sortStatus + (me.isShiftDown() ? -1 : 1);
                sortStatus = (sortStatus + 4) % 3 - 1;
                setStatus(sortStatus);
                oldCol = column;
                if(getStatus()==MultipleTableColumnSorter.ASCENDING || getStatus() == MultipleTableColumnSorter.DESCENDING) {
                    Vector newSortedData = new Vector();
                    newSortedData.addElement(tblPerson.getColumnName(column));
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
            }//End Nadh
            if(me.getSource() instanceof JTable) {
                if (me.getClickCount() == 2) {
                    try {
                        int selectedRow = tblPerson.getSelectedRow();
                        String personName = tblPerson.getValueAt(selectedRow, 0).toString();
                        //Modified for Coeus 4.3 Person Enhancements - start
                        //While displaying the mode is set as DISPLAY_MODE
                        //displayPersonDetails(personName , MODIFY_MODE );              //Handle
                        displayPersonDetails(personName , DISPLAY_MODE );
                        //Modified for Coeus 4.3 Person Enhancements - start
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
}
