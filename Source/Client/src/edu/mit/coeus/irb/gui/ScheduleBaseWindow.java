/*
 * @(#)ScheduleBaseWindow.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.irb.gui;

import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.utils.*; 
import edu.mit.coeus.irb.bean.ScheduleDetailsBean;

//Added by sharath - 19/10/2003 for Save As ToolBar Button - Start
import edu.mit.coeus.utils.saveas.SaveAsDialog;
//Added by sharath - 19/10/2003 for Save As ToolBar Button - End

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import java.util.Observable;
import java.util.Observer;
import javax.swing.table.DefaultTableModel;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.utils.locking.LockingException;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

/** This class is used to construct the parent window of Schedule and associate
 * its menu and toolbar with listener. This window will display the Schedules
 * from which the user can select the Schedule details.
 *
 * @author Ravikanth
 * @version 1.0 March 18, 2003, 12:53 PM
 */

public class ScheduleBaseWindow extends CoeusInternalFrame 
    implements ActionListener, Observer {

    // Menu items for Schedule
    private CoeusMenuItem scheduleDisplay,scheduleModify, scheduleSearch;

    // Toolbar for Schedule
    private CoeusToolBarButton btnModifySchedule;
    private CoeusToolBarButton btnDisplaySchedule;
    private CoeusToolBarButton btnSearchSchedule;
    private CoeusToolBarButton btnCloseSchedule;
    private CoeusToolBarButton btnSort;//Added by nadh - 19/01/2005 for Sort ToolBar Button
    
    //Added by sharath - 19/10/2003 for Save As ToolBar Button - Start
    private CoeusToolBarButton btnSaveAsSchedule;
    //Added by sharath - 19/10/2003 for Save As ToolBar Button - End

    private String windowName;
    // holds the search window name
    private final String SCHEDULE_SEARCH = "SCHEDULESEARCH";
    // holds the instance of CoeusSearch
    private CoeusSearch coeusSearch;

    ScheduleDetailsForm scheduleDetailsForm;
    //Main MDI Form.
    private CoeusAppletMDIForm mdiForm = null;

    // modify functionality
    private final char MODIFY_FUNCTION = 'M';
    // display functionality
    private final char DISPLAY_FUNCTION = 'D';



    private int selectedScheduleRow = -1;
    // Jtable
    private JTable tblSchedule;
    // Scroll bar pane
    private JScrollPane scrPnSearchRes;

    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    private int baseTableRow;
    
    //Added by nadh - 19/01/2005 for Sort ToolBar Button - Start
    
    //holds sorted columns and its states
    private Vector vecSortedData;
    
    private static final int OK_CLICKED = 0;
    
    //holds column index
    private int oldCol = -1;
    
    //holds column status
    private int status=MultipleTableColumnSorter.NOT_SORTED;
    //Added by Nadh - End

    //Added for case#3243
    private static final String SIMPLE_DATE_FORMAT = "yyyy/MM/dd";      
    //IACUC Changes - Start
    edu.mit.coeus.iacuc.gui.ScheduleDetailsForm iacucScheduleDetailsForm;
    private static final int COMMITTEE_TYPE_COLUMN =7;
    private static final int IRB_COMMITTEE = 1;
    private static final int IACUC_COMMITTEE = 2;
    //IACUC Changes - End
    /** Constructor to create new <CODE>ScheduleBaseWindow</CODE>
     *
     * @param mdiForm reference to <CODE>CoeusAppletMDIForm</CODE>.
     */
    public ScheduleBaseWindow(CoeusAppletMDIForm mdiForm) {
        super(CoeusGuiConstants.SCHEDULE_BASE_FRAME_TITLE, mdiForm,LIST_MODE);
        this.mdiForm = mdiForm;
        try {
            initComponents();
            mdiForm.putFrame(CoeusGuiConstants.SCHEDULE_BASE_FRAME_TITLE, this);
            mdiForm.getDeskTopPane().add(this);
            this.setSelected(true);
            this.setVisible(true);
            showScheduleSearchWindow();
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
        setFrameMenu(getScheduleEditMenu());
        setToolsMenu(getScheduleToolsMenu());
        setFrameToolBar(getScheduleToolBar());
        setFrame(CoeusGuiConstants.SCHEDULE_BASE_FRAME_TITLE);
        setFrameIcon(mdiForm.getCoeusIcon());
        createScheduleInfo();
        coeusMessageResources = CoeusMessageResources.getInstance();
    }
    
    //Added for CoeusSearch enhancement(CustomQuery)   start step:1
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
    }//End : step:1

    /**
     * This method creates the empty Schedule table for the base window
     *
     */
    private void createScheduleInfo() throws Exception {
        coeusSearch = new CoeusSearch(mdiForm, SCHEDULE_SEARCH, 0);
        prepareCQToolBarButtons(coeusSearch.getCustomQueryData());//Added  for CoeusSearch enhancement(CustomQuery)   start : step:2
        tblSchedule = coeusSearch.getEmptyResTable();
        javax.swing.table.TableColumn clmName
            = tblSchedule.getColumnModel().getColumn(
                tblSchedule.getColumnCount()-1);
        clmName.setPreferredWidth(0);
        clmName.setMaxWidth(0);
        clmName.setMinWidth(0);
        clmName.setWidth(0);
        javax.swing.table.JTableHeader header
            = tblSchedule.getTableHeader();
        //header.setResizingAllowed(false);
        header.setReorderingAllowed(false);
        setSearchResultsTable(tblSchedule);
        scrPnSearchRes = new JScrollPane();
        scrPnSearchRes.setMinimumSize(new Dimension(22, 15));
        //scrPnSearchRes.setPreferredSize(new Dimension(600, 400));
        if( tblSchedule != null ){
            tblSchedule.setFont( CoeusFontFactory.getNormalFont() );
        }
        scrPnSearchRes.setViewportView(tblSchedule);
        scrPnSearchRes.getViewport().setBackground(Color.white);
        scrPnSearchRes.setForeground(Color.white);
        getContentPane().add(scrPnSearchRes);

    }

    /**
     * This methods loads the search window when the Schedule module is opened,
     * this helps the user to load the base window with the Schedule details.
     *
     */
    private void showScheduleSearchWindow() {
        try {
             //Case 2451 start
            vecSortedData = new Vector();
            //Case 2451 end
            coeusSearch.showSearchWindow();
            JTable tblResultsTable = coeusSearch.getSearchResTable();
            //setSearchResultsTable(tblResultsTable);
            if (tblResultsTable != null) {
                tblSchedule = tblResultsTable;
                javax.swing.table.TableColumn clmName
                    = tblSchedule.getColumnModel().getColumn(
                        tblSchedule.getColumnCount()-1);
                clmName.setPreferredWidth(0);
                clmName.setMaxWidth(0);
                clmName.setMinWidth(0);
                clmName.setWidth(0);
                javax.swing.table.JTableHeader header
                    = tblSchedule.getTableHeader();
                //header.setResizingAllowed(false);
                header.setReorderingAllowed(false);
                scrPnSearchRes.setViewportView(tblSchedule);
                //Added by nadh - 19/01/2005 for table header
                tblSchedule.getTableHeader().addMouseListener(new ScheduleMouseAdapter());
                //Added by Nadh - End
                tblSchedule.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent me) {
                        if (me.getClickCount() == 2) {
                            try {
                                int selectedRow = tblSchedule.getSelectedRow();
                                String scheduleId =
                                tblSchedule.getValueAt(
                                selectedRow, 0).toString();
                                showScheduleDetails(scheduleId);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
            this.revalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //Added by Nadh for CustomQuery Search enhancement   start : step:4
    private void buildSearchResultsTable(JTable tblResultsTable) {
        if (tblResultsTable != null) {
                tblSchedule = tblResultsTable;
                javax.swing.table.TableColumn clmName
                = tblSchedule.getColumnModel().getColumn(
                tblSchedule.getColumnCount()-1);
                clmName.setMaxWidth(0);
                clmName.setMinWidth(0);
                clmName.setPreferredWidth(0);
                
                clmName = tblSchedule.getColumnModel().getColumn(tblSchedule.getColumnCount()-2);
                clmName.setMaxWidth(0);
                clmName.setMinWidth(0);
                clmName.setPreferredWidth(0);
                
                javax.swing.table.JTableHeader header
                = tblSchedule.getTableHeader();
                //header.setResizingAllowed(false);
                header.setReorderingAllowed(false);
                scrPnSearchRes.setViewportView(tblSchedule);
                tblSchedule.getTableHeader().addMouseListener(new ScheduleMouseAdapter());
                tblSchedule.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent me) {
                        if (me.getClickCount() == 2) {
                            try {
                                int selectedRow = tblSchedule.getSelectedRow();
                                String scheduleNumber =
                                tblSchedule.getValueAt(
                                selectedRow, 0).toString();
                                //BUG FIX,bug Id:1062 - Hour glass Implemntation START
                                mdiForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
                                showScheduleDetails(scheduleNumber);
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
     * constructs Schedule edit menu with sub menu Modify and Dispaly  and
     * Schedule Deatil Forms.
     *
     * @return CoeusMenu Schedule edit menu
     */
    private CoeusMenu getScheduleEditMenu() {
        CoeusMenu mnuSchedule = null;
        Vector fileChildren = new Vector();

        scheduleModify = new CoeusMenuItem("Modify", null, true, true);
        scheduleModify.setMnemonic('M');
        scheduleModify.addActionListener(this);

        scheduleDisplay = new CoeusMenuItem("Display", null, true, true);
        scheduleDisplay.setMnemonic('D');
        scheduleDisplay.addActionListener(this);

        fileChildren.add(scheduleModify);
        fileChildren.add(scheduleDisplay);

        mnuSchedule = new CoeusMenu("Edit", null, fileChildren, true, true);
        mnuSchedule.setMnemonic('E');
        return mnuSchedule;

    }

    /** Constructs Schedule Tools menu for Search screen with sub menu of <CODE>Search</CODE>
     *
     * @return <CODE>CoeusMenu</CODE> Schedule tools menu.
     */
    public CoeusMenu getScheduleToolsMenu() {
        CoeusMenu coeusMenu;
        Vector fileChildren = new Vector();
        scheduleSearch = new CoeusMenuItem("Search", null, true, true);
        scheduleSearch.setMnemonic('S');
        scheduleSearch.addActionListener(this);
        fileChildren.add(scheduleSearch);
        coeusMenu = new CoeusMenu("Tools", null, fileChildren, true, true);
        coeusMenu.setMnemonic('T');
        return coeusMenu;
    }

    /**
     * This method is used to create the tool bar for Save, Mofify and
     * Close buttons.
     *
     * @returns JToolBar Schedule Toolbar
     */
    private JToolBar getScheduleToolBar() {
        JToolBar toolbar = new JToolBar();

        btnModifySchedule = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.EDIT_ICON)),
        null, "Modify Schedule");
        
        btnDisplaySchedule = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DISPLAY_ICON)),
        null, "Display Schedule");

        btnSearchSchedule = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)),
        null, "Search");

        btnCloseSchedule = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
        null, "Close Schedule");
        
        //Added by sharath - 19/10/2003 for Save As ToolBar Button - Start
        btnSaveAsSchedule = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SAVEAS_ICON)),
        null, "Save As");
        //Added by sharath - 19/10/2003 for Save As ToolBar Button - End
        
        //Added by nadh - 19/01/2005 for Sort ToolBar Button - Start
        btnSort = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SORT_ICON)),null,
        "Sort Schedule");
        //Added by Nadh - End
        
        btnModifySchedule.addActionListener(this);
        btnDisplaySchedule.addActionListener(this);
        btnSearchSchedule.addActionListener(this);
        btnCloseSchedule.addActionListener(this);
        //Added by sharath - 19/10/2003 for Save As ToolBar Button - Start
        btnSaveAsSchedule.addActionListener(this);
        //Added by sharath - 19/10/2003 for Save As ToolBar Button - End
        
        btnSort.addActionListener(this);//Added by nadh - 19/01/2005 for Sort ToolBar Button
        
        toolbar.add(btnModifySchedule);
        toolbar.add(btnDisplaySchedule);
        toolbar.add(btnSort);//Added by nadh - 19/01/2005 for Sort ToolBar Button
        toolbar.add(btnSearchSchedule);
        //Added by sharath - 19/10/2003 for Save As ToolBar Button - Start
        toolbar.add(btnSaveAsSchedule);
        //Added by sharath - 19/10/2003 for Save As ToolBar Button - End
        toolbar.addSeparator();
        toolbar.add(btnCloseSchedule);

        toolbar.setFloatable(false);
        return toolbar;
    }

    /** All the  actions for the menu and toolbar associated with
     * the <CODE>ScheduleBaseWindow</CODE> will be invoked from this method.
     *
     * @param actionType <CODE>ActionEvent</CODE>, a semantic event which indicates that a
     * component-defined action occured.
     */
    public void actionPerformed(ActionEvent actionType) {
        try{
            int selectedRow = 0;
            String scheduleId = null;
            if (tblSchedule != null && tblSchedule.getRowCount() > 0) {
                selectedRow = tblSchedule.getSelectedRow();
                //Check whether row is selected
                if (selectedRow >= 0) {
                    scheduleId
                    = tblSchedule.getValueAt(selectedRow, 0).toString();
                }
            }

            Object actSource = actionType.getSource();

            if (actSource.equals(scheduleModify) ||
                actSource.equals(btnModifySchedule)) {
                    modifyScheduleDetails(scheduleId);
                /* when the menu or tool bar display is clicked showScheduleDetails
                 * method will be fired
                 */
            } else if (actSource.equals(scheduleDisplay) ||
            actSource.equals(btnDisplaySchedule)) {
                showScheduleDetails(scheduleId);
            } else if (actSource.equals(btnCloseSchedule)) {
                closeScheduleBaseWindow();
            }
            //Added by nadh - 19/01/2005 for Sort ToolBar Button - Start
            else if (actSource.equals(btnSort)) {
                showSort();
            }//Added by Nadh - End
        /* when the Toolsmenu search is clicked showScheduleSearchWindow
         * method will be fired
         */
            if (actSource.equals(scheduleSearch) ||
            actSource.equals(btnSearchSchedule)) {
                showScheduleSearchWindow();
            }
            //Added by sharath - 19/10/2003 for Save As ToolBar Button - Start
            if(actSource.equals(btnSaveAsSchedule)) {
                //showSaveAsDialog();
                //new implementation
                saveAsActiveSheet();
            }
            //Added by sharath - 19/10/2003 for Save As ToolBar Button - End
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
            
        }catch(LockingException ex){
            CoeusOptionPane.showDialog(new CoeusClientException(ex));
        }catch(CoeusException ex){
            CoeusOptionPane.showDialog(new CoeusClientException(ex));
        }catch( Exception err ){
            err.printStackTrace();
            CoeusOptionPane.showErrorDialog(err.getMessage() );
//            log(err.getMessage() );
            // commented by manoj to fix the bug#3 ( BugFixesRequiredinIRB11(1).doc ) 17/09/2003 
/*            int selRow = tblSchedule.getSelectedRow();
            if(selRow != -1){
                TableSorter sorter = (TableSorter) tblSchedule.getModel();
                int row = sorter.getIndexForRow( selRow );
                sorter.removeRow( row );
                
                //((DefaultTableModel)tblSchedule.getModel()).removeRow(selRow);
                if(tblSchedule.getRowCount() > selRow){
                    tblSchedule.setRowSelectionInterval(selRow,selRow);
                }
            }
            */
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
        SortForm sortForm = new SortForm(tblSchedule,vecSortedData);
        Vector sortedData = sortForm.display();
        vecSortedData = (Vector)sortedData.get(1);
        if(((Integer)sortedData.get(0)).intValue() == OK_CLICKED)
            coeusSearch.sortByColumns(tblSchedule,vecSortedData);
        else
            return;
    }// Added by Nadh - end

    /**
     * This method is used to check whether the given protocol number is already 
     * opened in the given mode or not. 
     */
    private void checkDuplicateAndShow(String refId, char mode)throws Exception {
        boolean duplicate = false;
        try{
            refId = refId == null ? "" : refId;
            duplicate = mdiForm.checkDuplicate(
                CoeusGuiConstants.SCHEDULE_DETAILS_FRAME_TITLE, refId, mode );
        }catch(Exception e){
            /* Exception occured.  Record may be already opened in requested mode
               or if the requested mode is edit mode and application is already
               editing any other record. */
            duplicate = true;
            if(e.getMessage().length() > 0 ) {
                CoeusOptionPane.showInfoDialog(e.getMessage());
            }
            /* try to get the requested frame which is already opened */
            CoeusInternalFrame frame = mdiForm.getFrame(
                    CoeusGuiConstants.SCHEDULE_DETAILS_FRAME_TITLE,refId);
            if(frame == null){
                /* if no frame opened for the requested record then the 
                   requested mode is edit mode. So get the frame of the
                   editing record. */
                frame = mdiForm.getEditingFrame( 
                    CoeusGuiConstants.SCHEDULE_DETAILS_FRAME_TITLE );
            }
            if (frame != null){
                frame.setSelected(true);
                frame.setVisible(true);
            }
            return;
        }
        //Modified for IACUC Changes
        int committeeTypeCode = 0;
        if (tblSchedule != null && tblSchedule.getRowCount() > 0) {
            int selectedRow = tblSchedule.getSelectedRow();
            if (selectedRow >= 0) {
                committeeTypeCode = Integer.parseInt(tblSchedule.getValueAt(selectedRow, COMMITTEE_TYPE_COLUMN).toString());
            }
        }
        if(committeeTypeCode == IRB_COMMITTEE){
            try{
                scheduleDetailsForm = new ScheduleDetailsForm( mode, refId, mdiForm);
                scheduleDetailsForm.showScheduleDetailForm();
                int selRow = tblSchedule.getSelectedRow();
                if( mode != DISPLAY_FUNCTION ) {
                    scheduleDetailsForm.registerObserver( this );
                    if (  selRow != -1 ){
                        baseTableRow = Integer.parseInt((String)tblSchedule.getValueAt(
                                selRow,tblSchedule.getColumnCount()-1));
                    }
                }
                
            }catch ( LockingException ex) {
                //ex.printStackTrace();
                //throw new Exception(ex.getMessage());
//            try{
                if (scheduleDetailsForm.isLocked() ) {
                    String msg = coeusMessageResources.parseMessageKey(
                            ex.getMessage());
                    int resultConfirm = CoeusOptionPane.showQuestionDialog(msg,
                            CoeusOptionPane.OPTION_YES_NO,
                            CoeusOptionPane.DEFAULT_YES);
                    if (resultConfirm == 0) {
                        showScheduleDetails(refId);
                    }
                }else {
                    throw new Exception(ex.getMessage());
                }
//            }catch (Exception excep){
//                throw new Exception(excep.getMessage());
//            }
            }catch(CoeusException ex){
                throw ex;
            }
        }else if(committeeTypeCode == IACUC_COMMITTEE){
              try{
                iacucScheduleDetailsForm = new edu.mit.coeus.iacuc.gui.ScheduleDetailsForm( mode, refId, mdiForm);
                iacucScheduleDetailsForm.showScheduleDetailForm();
                int selRow = tblSchedule.getSelectedRow();
                if( mode != DISPLAY_FUNCTION ) {
                    iacucScheduleDetailsForm.registerObserver( this );
                    if (  selRow != -1 ){
                        baseTableRow = Integer.parseInt((String)tblSchedule.getValueAt(
                                selRow,tblSchedule.getColumnCount()-1));
                    }
                }
                
            }catch ( LockingException ex) {
                if (iacucScheduleDetailsForm.isLocked() ) {
                    String msg = coeusMessageResources.parseMessageKey(
                            ex.getMessage());
                    int resultConfirm = CoeusOptionPane.showQuestionDialog(msg,
                            CoeusOptionPane.OPTION_YES_NO,
                            CoeusOptionPane.DEFAULT_YES);
                    if (resultConfirm == 0) {
                        showScheduleDetails(refId);
                    }
                }else {
                    throw new Exception(ex.getMessage());
                }
            }catch(CoeusException ex){
                throw ex;
            }
        }
        //IACUC Changes - End     
    }
    
    /**
     * This method is invoked when the user clicks modify in the Edit menu
     * of Schedule module
     */
    private void modifyScheduleDetails(String scheduleId) throws Exception{
        if (scheduleId == null) {
            log(coeusMessageResources.parseMessageKey(
            "protoBaseWin_exceptionCode.1051"));
        } else {
            checkDuplicateAndShow(scheduleId,MODIFY_FUNCTION);
        }
    }



/**
 * This method is invoked when the user clicks display in the Edit menu
 * of Schedule module
 */
private void showScheduleDetails(String scheduleId) {
    if (scheduleId == null) {
        log(coeusMessageResources.parseMessageKey(
        "protoBaseWin_exceptionCode.1052"));
    } else {
        try{
            checkDuplicateAndShow(scheduleId,DISPLAY_FUNCTION);
        }catch(CoeusException ex){
            CoeusOptionPane.showDialog(new CoeusClientException(ex));
        }catch( Exception err ){
            log(err.getMessage());
            // commented by manoj to fix the bug#3 ( BugFixesRequiredinIRB11(1).doc ) 17/09/2003 
/*            int selRow = tblSchedule.getSelectedRow();
            if(selRow != -1){
                TableSorter sorter = (TableSorter) tblSchedule.getModel();
                int row = sorter.getIndexForRow( selRow );
                sorter.removeRow( row );
                
                //((DefaultTableModel)tblSchedule.getModel()).removeRow(selRow);
                if(tblSchedule.getRowCount() > selRow){
                    tblSchedule.setRowSelectionInterval(selRow,selRow);
                }
            }
            */
        }
    }
}

/**
 * This method is invoked when the user clicks close in the ToolBar
 * of Protocol module
 */
private void closeScheduleBaseWindow() {
    this.doDefaultCloseAction();
}

/**
 * display alert message
 *
 * @param mesg the message to be displayed
 */
private void log(String mesg) {
    //added by nadh for the fix of #1172 
//    int selectedRow = 0;
//    String scheduleId = null;
//    if (tblSchedule != null && tblSchedule.getRowCount() > 0) {
//        selectedRow = tblSchedule.getSelectedRow();
//        //Check whether row is selected
//        if (selectedRow >= 0) {
//            scheduleId
//            = tblSchedule.getValueAt(selectedRow, 0).toString();
//        }
//    }
//    int option = CoeusOptionPane.showQuestionDialog(
//                    mesg+". Do you want to open this schedule in display mode.?", 
//                CoeusOptionPane.OPTION_YES_NO,
//                CoeusOptionPane.DEFAULT_YES);
//                switch( option ){
//                    case ( JOptionPane.YES_OPTION ):
//                        
//                            showScheduleDetails(scheduleId);
//                        break;
//                    case ( JOptionPane.NO_OPTION ):
//                        break;
//                }
//     //end nadh bug fix #1172         
    CoeusOptionPane.showInfoDialog(mesg); //commented by nadh
}

/** This method is called from Save Menu Item under File Menu.
 * Implemented the abstract class declared in parent(<CODE>CoeusInternalFrame</CODE>).
 * Empty body since the Save operation is not required for this list screen.
 */
public void saveActiveSheet() {
}

    public void update(Observable observable, Object obj) {
        if( obj instanceof ScheduleDetailsBean ) {
            ScheduleDetailsBean scheduleDetails = (ScheduleDetailsBean) obj ;
            edu.mit.coeus.utils.DateUtils dtUtils = new edu.mit.coeus.utils.DateUtils();
            Vector scheduleRow = new Vector();
            String schDate = "";
            String deadline = "";
            String place = scheduleDetails.getScheduledPlace() == null ? ""
            : scheduleDetails.getScheduledPlace();
            scheduleRow.add(scheduleDetails.getScheduleId());
            //Code modified for PT ID#3243 - start
            String dateFormat = CoeusServerProperties.getProperty(CoeusPropertyKeys.SEARCH_DATE_FORMAT, SIMPLE_DATE_FORMAT);            
            if(scheduleDetails.getScheduleDate() != null){
                //COEUSQA-1477 Dates in Search Results - Start
                schDate = dtUtils.parseDateForSearchResults(scheduleDetails.getScheduleDate().toString(), dateFormat);
                //schDate = dtUtils.formatDate(
                //    scheduleDetails.getScheduleDate().toString(), dateFormat);
                //COEUSQA-1477 Dates in Search Results - End
                scheduleRow.add(schDate);

            }else{
                scheduleRow.add("");
            }
            scheduleRow.add( place );
            scheduleRow.add(scheduleDetails.getScheduleStatusDesc());
            if(scheduleDetails.getProtocolSubDeadLine()!= null){
                //COEUSQA-1477 Dates in Search Results - Start
                deadline = dtUtils.parseDateForSearchResults(scheduleDetails.getProtocolSubDeadLine().toString(), dateFormat);
                //deadline = dtUtils.formatDate(
                //    scheduleDetails.getProtocolSubDeadLine().toString(), dateFormat);
                scheduleRow.add(deadline);
            }else{
                scheduleRow.add("");
            }
            //Code modified for PT ID#3243 - end
            scheduleRow.add(scheduleDetails.getCommitteeId());
            scheduleRow.add(scheduleDetails.getCommitteeName());
            
            
            if (((BaseWindowObservable)observable).getFunctionType() == 'M'  ){
                ((DefaultTableModel)tblSchedule.getModel()).setValueAt(
                    scheduleDetails.getScheduleId(),baseTableRow,0);
                ((DefaultTableModel)tblSchedule.getModel()).setValueAt(
                    schDate,baseTableRow,1);
                ((DefaultTableModel)tblSchedule.getModel()).setValueAt(
                    place,baseTableRow,2);
                ((DefaultTableModel)tblSchedule.getModel()).setValueAt(
                    scheduleDetails.getScheduleStatusDesc(),baseTableRow,3);
                ((DefaultTableModel)tblSchedule.getModel()).setValueAt(
                    deadline,baseTableRow,4);
                ((DefaultTableModel)tblSchedule.getModel()).setValueAt(
                    scheduleDetails.getCommitteeId(),baseTableRow,5);
                ((DefaultTableModel)tblSchedule.getModel()).setValueAt(
                    scheduleDetails.getCommitteeName(),baseTableRow,6);
                int selRow = tblSchedule.getSelectedRow();
                if(  selRow != -1 ) {
                    baseTableRow = Integer.parseInt((String)tblSchedule.getValueAt(
                                        selRow,tblSchedule.getColumnCount()-1));
                }
                
                //tblSchedules.setRowSelectionInterval(rowNum,rowNum);
            }
        }
    }
    
    //Added by sharath - 19/10/2003 for Save As ToolBar Button - Start
    /** 
     * This method is called from SaveAs Toolbar Menu Item.
     */
    public void showSaveAsDialog(){
        SaveAsDialog saveAsDialog = new SaveAsDialog(tblSchedule);
    }
    
    public void saveAsActiveSheet() {
         SaveAsDialog saveAsDialog = new SaveAsDialog(tblSchedule);
    }//Added by sharath - 19/10/2003 for Save As ToolBar Button - End
    
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
    class ScheduleMouseAdapter extends MouseAdapter{
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
                newSortedData.addElement(tblSchedule.getColumnName(column));
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
