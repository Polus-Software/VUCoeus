/*
 * @(#)OrganizationBaseBaseWindow.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.organization.gui;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.TableSorter;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.saveas.SaveAsDialog;

//Added by Chandra
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.AppletServletCommunicator;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import java.util.Observable;
import java.util.Observer;

import edu.mit.coeus.utils.BaseWindowObservable;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceFormBean;
import edu.mit.coeus.utils.MultipleTableColumnSorter;
import edu.mit.coeus.utils.SortForm;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

/**
 * This class is used to construct the parent window of Organization and associate
 * its menu and toolbar with listener. This window will display the organizations
 * in table form, from which the user can select the organization details
 *
 * @author  Guptha
 * @version 1.0 October 10, 2002, 6:05 PM
 * @modified by Sagin
 * @date 24-10-02
 * Description : As part of Java V1.3 compatibility, Replaced all null Vectors 
 *               with new Vector() instance.
 *
 */

public class OrganizationBaseWindow extends CoeusInternalFrame 
    implements ActionListener, Observer {
        // Added by chandra
        private static final String MAINTAIN_ORGANIZATION = "MAINTAIN_ORGANIZATION";
        private static final String USER_HAS_OSP_RIGHT = "FN_USER_HAS_OSP_RIGHT";
        private static final String FUNCTION_SERVLET = "/coeusFunctionsServlet";
        String connectTo = CoeusGuiConstants.CONNECTION_URL+ FUNCTION_SERVLET;
        // End Chandra

    // Menu items for organization
    private CoeusMenuItem mnItmAddOrganization,mnItmModifyOrganization,
            mnItmDisplayOrganization,mnItmSearchOrganization,mnItmSortOrganization,
            mnItmSaveAsOrganization,mnItmCloseOrganization;

    // Toolbar for organization
    private CoeusToolBarButton btnmnItmAddOrganization, btnModifyOrganization,
            btnDisplayOrganization,btnSearchOrganization, btnSortOrganization,
            btnSaveAsOrganization, btnCloseOrganization;
    // add functionality
    private final char ADD_FUNCTION = 'I';
    // modify functionality
    private final char MODIFY_FUNCTION = 'U';
    // display functionality
    private final char DISPLAY_FUNCTION = 'D';
    // unique frame name
    private String frameName;
    // table to be displayed in the frame
    private JTable organizationTable = null;
    // search identifier to bring up the organization search windnow
    public static final String SEARCH_IDENTIFIER = "OrganizationSearch";
    public static final String ORGANIZATION_FRAME_NAME = "ORGANIZATION";
    
    //Main MDI Form
    private CoeusAppletMDIForm mdiForm;
    // search window instance
    private CoeusSearch coeusSearch;
    // scroll pane which holds the organization table
    private JScrollPane scrlPnSearchRes;
       
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
    
    /**
     * Constructor to create organization base window
     *
     * @param frameTitle internal frame title
     * @param frameName internal frame name where the organization list will be displayed
     * @param mdiForm Coeus MDI form
     */

    public OrganizationBaseWindow(String frameTitle, String frameName,
                                  CoeusAppletMDIForm mdiForm) {
        super(frameTitle, mdiForm,LIST_MODE);
        this.frameName = frameName;
        this.mdiForm = mdiForm;
        initComponents();
        showOrganizationTable();
    }
    /**
     * Constructor to create organization base window
     *
     * @param mdiForm a reference of CoeusAppletMDIForm
     */

    public OrganizationBaseWindow(CoeusAppletMDIForm mdiForm) {
        super(CoeusGuiConstants.ORGANIZATION_FRAME_TITLE, mdiForm,LIST_MODE);
        this.frameName = ORGANIZATION_FRAME_NAME;
        this.mdiForm = mdiForm;
        initComponents();
        showOrganizationTable();
    }    
    /**
     * Construct the components for the base window. This method constructs the menu,toolbar
     * for organization window and associate with mdi form.
     */
    
    private void initComponents() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        
        // associate the menu to the frame
        setFrameMenu(organizationEditMenu());
        // associate the tool menu to the frame
        setToolsMenu(organizationToolsMenu());
        // associate the toolbar to the frame
        setFrameToolBar(organizationToolBar());
        // set a name for the frame
        setFrame(frameName);
        // set the frame icon
        setFrameIcon(mdiForm.getCoeusIcon());
        // initially show an empty table in the frame
        showEmptyOrganizationTable();
        // set this frame with the mdi form.
        mdiForm.putFrame(CoeusGuiConstants.ORGANIZATION_FRAME_TITLE, this);
        // add this frame component with mdi form.
        mdiForm.getDeskTopPane().add(this);
        setVisible(true);
        
        //Check whether the User has rights to Maintain Organization- Added by chandra
        boolean hasOrgRight = isUserHasRight();
        btnModifyOrganization.setEnabled(hasOrgRight);
        btnmnItmAddOrganization.setEnabled(hasOrgRight);
        mnItmAddOrganization.setEnabled(hasOrgRight);
        mnItmModifyOrganization.setEnabled(hasOrgRight);
    }

    /**
     * Create and Show empty table in the frame before popup the organization search widnow.
     */
    private void showEmptyOrganizationTable() {
        scrlPnSearchRes = new JScrollPane();
        try {
            coeusSearch = new CoeusSearch(mdiForm, SEARCH_IDENTIFIER, 0);
            organizationTable = coeusSearch.getEmptyResTable();
            javax.swing.table.TableColumn clmName 
                = organizationTable.getColumnModel().getColumn(
                    organizationTable.getColumnCount()-1);
            clmName.setPreferredWidth(0);
            clmName.setMaxWidth(0);
            clmName.setMinWidth(0);
            clmName.setWidth(0);
            javax.swing.table.JTableHeader header 
                = organizationTable.getTableHeader();
            //header.setResizingAllowed(false);
            header.setReorderingAllowed(false);
            setSearchResultsTable(organizationTable);
            scrlPnSearchRes.setMinimumSize(new Dimension(22, 15));
            //scrlPnSearchRes.setPreferredSize(new Dimension(600, 400));
            if( organizationTable != null ){
                organizationTable.setFont( CoeusFontFactory.getNormalFont() );
            }
            scrlPnSearchRes.setViewportView(organizationTable);
            scrlPnSearchRes.getViewport().setBackground(Color.white);
            scrlPnSearchRes.setForeground(java.awt.Color.white);
            getContentPane().add(scrlPnSearchRes);
        } catch (Exception e) {
            e.printStackTrace();
            log("Coeus Search is not available.." + e.getMessage());
        }
    }

    /**
     * Create and show the organization table in the frame. This method pops up the organization
     * search window, where the user can enter search criteria to get the organizations. Selected
     * organization will be shown in the internal in tabular form.
     * Double clicking on the row of organization table will display the organization details.
     */
    private void showOrganizationTable() {
        JTable orgTable=null;
        try {
            coeusSearch.showSearchWindow();
            orgTable = coeusSearch.getSearchResTable();
        } catch (Exception e) {
            e.printStackTrace();
            log("Coeus Search is not available.." + e.getMessage());
        }

        if (orgTable != null) {
            organizationTable = orgTable;
            javax.swing.table.TableColumn clmName 
                = organizationTable.getColumnModel().getColumn(
                    organizationTable.getColumnCount()-1);
            clmName.setPreferredWidth(0);
            clmName.setMaxWidth(0);
            clmName.setMinWidth(0);
            clmName.setWidth(0);
            javax.swing.table.JTableHeader header 
                = organizationTable.getTableHeader();
            //header.setResizingAllowed(false);
            header.setReorderingAllowed(false);
            scrlPnSearchRes.setViewportView(organizationTable);
            revalidate();

            setResultData(coeusSearch.getResultRecords());
            organizationTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            organizationTable.addRowSelectionInterval(0, 0);
            //Added by nadh - 19/01/2005 for table header
                organizationTable.getTableHeader().addMouseListener(new OrganizationMouseAdapter());
                //Added by Nadh - End
            //listener
            organizationTable.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent me) {
                    if (me.getClickCount() == 2) {
                        int selectedRow = organizationTable.getSelectedRow();
                        String orgId = organizationTable.getValueAt(selectedRow, 0).toString();
                        try{
                            displayOrganization(orgId);
                        }catch(Exception ex){
                            ex.printStackTrace();
                            log(ex.getMessage());
                        }
                    }
                }
            });
        }
    }

    /**
     * constructs organization edit menu
     *
     * @return CoeusMenu organization edit menu
     */
    private CoeusMenu organizationEditMenu() {
        CoeusMenu mnuorganization = null;
        Vector fileChildren = new Vector();
        mnItmAddOrganization = new CoeusMenuItem("Add", null, true, true);
        mnItmAddOrganization.addActionListener(this);
        mnItmAddOrganization.setMnemonic('A');

        mnItmModifyOrganization = new CoeusMenuItem("Modify", null, true, true);
        mnItmModifyOrganization.setMnemonic('M');
        mnItmModifyOrganization.addActionListener(this);

        mnItmDisplayOrganization = new CoeusMenuItem("Display", null, true, true);
        mnItmDisplayOrganization.setMnemonic('i');
        mnItmDisplayOrganization.addActionListener(this);

        fileChildren.add(mnItmAddOrganization);
        fileChildren.add(mnItmModifyOrganization);
        fileChildren.add(mnItmDisplayOrganization);

        mnuorganization = new CoeusMenu("Edit", null, fileChildren, true, true);
        mnuorganization.setMnemonic('E');
        return mnuorganization;
    }

    /**
     * Constructs organization tools menu
     *
     * @return CoeusMenu organization tools menu
     */
    public CoeusMenu organizationToolsMenu() {
        CoeusMenu toolsMenu;
        java.util.Vector fileChildren = new java.util.Vector();
        mnItmSearchOrganization = new CoeusMenuItem("Search", null, true, true);
        fileChildren.add(mnItmSearchOrganization);
        toolsMenu = new CoeusMenu("Tools", null, fileChildren, true, true);
        toolsMenu.setMnemonic('T');
        mnItmSearchOrganization.addActionListener(this);
        return toolsMenu;
    }

    /**
     * Constructs organization ToolBar.
     *
     * @returns JToolBar organization Toolbar
     */
    private JToolBar organizationToolBar() {
        JToolBar toolbar = new JToolBar();

        btnmnItmAddOrganization = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.ADD_ICON)), null, "Add organization");
        btnModifyOrganization = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.EDIT_ICON)), null, "Modify organization");
        btnDisplayOrganization = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.DISPLAY_ICON)), null, "Display organization");
        btnSortOrganization = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.SORT_ICON)), null, "Sort the organization");
        btnSearchOrganization = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)), null, "Search the organization");
        btnSaveAsOrganization = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.SAVEAS_ICON)), null, "Save As");
        btnCloseOrganization = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)), null, "Close the organization");

        btnmnItmAddOrganization.addActionListener(this);
        btnModifyOrganization.addActionListener(this);
        btnDisplayOrganization.addActionListener(this);
        btnSortOrganization.addActionListener(this);
        btnSearchOrganization.addActionListener(this);
        btnSaveAsOrganization.addActionListener(this);
        btnCloseOrganization.addActionListener(this);

        toolbar.add(btnmnItmAddOrganization);
        toolbar.add(btnModifyOrganization);
        toolbar.add(btnDisplayOrganization);
        toolbar.addSeparator();
        toolbar.add(btnSortOrganization);
        toolbar.add(btnSearchOrganization);
        toolbar.add(btnSaveAsOrganization);
        toolbar.addSeparator();
        toolbar.add(btnCloseOrganization);

        toolbar.setFloatable(false);
        return toolbar;
    }

    /**
     * Actions while firing the events for menu and toolbar items
     */
    public void actionPerformed(ActionEvent actionType) {
        int selectedRow = 0;
        String orgId = null;
        if (organizationTable != null && organizationTable.getRowCount() > 0) {
            selectedRow = organizationTable.getSelectedRow();
            //Check whether row is selected
            if (selectedRow >= 0) {
                orgId = organizationTable.getValueAt(selectedRow, 0).toString();
                //Bug fix for case:2658 starts
                baseTableRow = selectedRow;
//                baseTableRow = Integer.parseInt((String)organizationTable.getValueAt(
//                selectedRow,organizationTable.getColumnCount()-1));
                //Bug fix for case:2658 ends
            }
        }
        Object actSource = actionType.getSource();
        try{
            if (actSource.equals(mnItmAddOrganization) || actSource.equals(btnmnItmAddOrganization)) {
                addOrganization(null);
            } else if (actSource.equals(mnItmModifyOrganization) || actSource.equals(btnModifyOrganization)) {
                modifyOrganization(orgId);
            } else if (actSource.equals(mnItmDisplayOrganization) || actSource.equals(btnDisplayOrganization)) {
                displayOrganization(orgId);
            } else if (actSource.equals(mnItmSearchOrganization) || actSource.equals(btnSearchOrganization)) {
                showOrganizationTable();
            } else if (actSource.equals(btnSaveAsOrganization)) {
                saveAsActiveSheet();
            } else if (actSource.equals(mnItmCloseOrganization) || actSource.equals(btnCloseOrganization)) {
                mdiForm.getSelectedFrame().doDefaultCloseAction();
            }
            //Added by nadh - 19/01/2005 for Sort ToolBar Button - Start
            else if (actSource.equals(btnSortOrganization)) {
                showSort();
            }//Added by Nadh - End
        }catch(Exception ex){
            ex.printStackTrace();
            log(ex.getMessage());
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
        SortForm sortForm = new SortForm(organizationTable,vecSortedData);
        Vector sortedData = sortForm.display();
        vecSortedData = (Vector)sortedData.get(1);
        if(((Integer)sortedData.get(0)).intValue() == OK_CLICKED)
            coeusSearch.sortByColumns(organizationTable,vecSortedData);
        else
            return;
    }// Added by Nadh - end   
    
    /**
     * To add a new Organization. This method shows a dialog form to add the organization
     * details. After successul operation, the base window will get updated with the
     * newly added organization details.
     *
     * @param orgId organization id which will be null always
     */
    private void addOrganization(String orgId) throws Exception{
        // create the organization form component
        DetailForm frmOrgDetailForm = new DetailForm(ADD_FUNCTION, orgId);
        frmOrgDetailForm.registerObserver( this );
        // show the organization detail form in dialog
        frmOrgDetailForm.showDialogForm(mdiForm);
        // update the base window with newly added record
//        insertRowInBaseWinow(frmOrgDetailForm.getDataToUpdate());
    }

    /**
     * To Modify a Organization details. This method shows a dialog form to modify the organization
     * details. After successul operation, the base window will get updated with the
     * modified organization details.
     *
     * @param orgId organization id to fetch details
     */
    private void modifyOrganization(String orgId) throws Exception{
        DetailForm frmOrgDetailForm = null;
        if (orgId == null) {
            throw new Exception(coeusMessageResources.parseMessageKey(
                                            "protoBaseWin_exceptionCode.1051")); 
        } else {
            frmOrgDetailForm = new DetailForm(MODIFY_FUNCTION, orgId);
            if(frmOrgDetailForm.isLocked()){
                    /* the row is being locked by some other user
                     */
                String msg = coeusMessageResources.parseMessageKey(
                "organization_row_clocked_exceptionCode.333333");
                int resultConfirm = CoeusOptionPane.showQuestionDialog(msg,
                CoeusOptionPane.OPTION_YES_NO,
                CoeusOptionPane.DEFAULT_YES);
                if (resultConfirm == 0) {
                        /* Call the RolodexDetails form with functionType
                         * as 'V'  and RolodexDetailsBean as inormation and
                         * the connectionURl string information
                         */
                    displayOrganization(orgId);
                }
            }else{
                frmOrgDetailForm.registerObserver( this );
                frmOrgDetailForm.showDialogForm(mdiForm);
//                updateRowInBaseWinow(organizationTable.getSelectedRow(), 
//                    frmOrgDetailForm.getDataToUpdate());
            }
        
    }
    }
    /* Added by chandra - To check wheter the user  have MAINTAIN_ORGANIZATION 
     *right or not. If yes show the toolbar buttons else disable the buttons. - 
     *Modified on 01/16/2003.Call server whether the user has right or not.
     */
    private boolean isUserHasRight() {
        boolean hasOSPRights = false;
        RequesterBean request = new RequesterBean();
        request.setId(MAINTAIN_ORGANIZATION);
        request.setDataObject(USER_HAS_OSP_RIGHT);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        
        if (response.isSuccessfulResponse()) {
            //added by manoj to display the authorization message as info message
            if(response.getDataObject() != null){
                Boolean obj = (Boolean) response.getDataObject();
                hasOSPRights = obj.booleanValue();
            }else{
                CoeusOptionPane.showErrorDialog(response.getMessage()) ;
                hasOSPRights = false ;
            }
        }
        
        return  hasOSPRights ;
        
    }// End of the method. Added by chandra - End

    /**
     * To Display Organization details. This method shows a dialog form to display the organization
     * details.
     *
     * @param orgId organization id to fetch details
     */
    private void displayOrganization(String orgId) throws Exception{
        if (orgId == null) {
            log(coeusMessageResources.parseMessageKey(
                                            "protoBaseWin_exceptionCode.1052"));
        } else {
            DetailForm frmOrgDetailForm = new DetailForm(DISPLAY_FUNCTION, orgId);
            frmOrgDetailForm.showDialogForm(mdiForm);
        }
    }

    /**
     * Update the organization table in the base window.
     *
     * @param rowNumber the selected row in the table
     * @param rowData the column values of the row
     */
    private void updateRowInBaseWinow(int rowNumber, Vector rowData) {
        
        if (rowData != null) {
            rowData.addElement("");
            // remove the selected row
            ((DefaultTableModel) organizationTable.getModel()).removeRow(rowNumber);
            // insert the new row data
            ((DefaultTableModel) organizationTable.getModel()).insertRow(rowNumber, rowData);
            // select the row
            organizationTable.setRowSelectionInterval(rowNumber,rowNumber);
            // refresh the table
            validate();
        }
    }

    /**
     * insert a new record into the organization table
     *
     * @param rowData the column values of the row
     */
    private void insertRowInBaseWinow(Vector rowData) {
        if (rowData != null) {
            rowData.addElement("");
            // insert the new row data
            ((DefaultTableModel) organizationTable.getModel()).insertRow(0, rowData);
            // select the row
            organizationTable.setRowSelectionInterval(0,0);
            // refresh the table
            validate();
        }
    }

    /**
     * display alert message
     *
     * @param mesg the message to be displayed
     */
    private void log(String mesg) {
        CoeusOptionPane.showErrorDialog(mesg);
    }
    
    /**
     * This method called from Save Menu Item under File Menu.
     * Implemented the abstract class declared in parent(CoeusInternalFrame).
     * Empty body since the Save operation is not required for this list screen.
     */
    public void saveActiveSheet() {
    }
    
    public void update(Observable o, Object arg) {
        if( arg instanceof OrganizationMaintenanceFormBean ) {
            OrganizationMaintenanceFormBean orgBean = 
                ( OrganizationMaintenanceFormBean ) arg;
            if( ((BaseWindowObservable)o).getFunctionType() == ADD_FUNCTION){
                Vector rowData = new Vector();
                rowData.add(orgBean.getOrganizationId());
                rowData.add(orgBean.getOrganizationName());
                rowData.add(orgBean.getAddress());
                rowData.add(orgBean.getCongressionalDistrict());
                rowData.add(orgBean.getFederalEmployerID());
                rowData.add(orgBean.getVendorCode());
                rowData.add(orgBean.getDunsNumber());
                rowData.add(orgBean.getDunsPlusFourNumber());
                rowData.add(orgBean.getDodacNumber());
                rowData.add(orgBean.getCageNumber());
                //for index which came from search
                int lastRow = organizationTable.getRowCount(); 
                rowData.addElement(""+lastRow);
                if( lastRow >= 0 ) {
                    ((DefaultTableModel)organizationTable.getModel()).insertRow(lastRow,rowData);
                }else{
                    ((DefaultTableModel)organizationTable.getModel()).insertRow(0,rowData);
                    
                }
                baseTableRow = lastRow; 
                
            }else if( ((BaseWindowObservable)o).getFunctionType() == 'U'){
                ((DefaultTableModel)organizationTable.getModel()).setValueAt(
                    orgBean.getOrganizationId(),baseTableRow,0);
                ((DefaultTableModel)organizationTable.getModel()).setValueAt(
                    orgBean.getOrganizationName(),baseTableRow,1);
                ((DefaultTableModel)organizationTable.getModel()).setValueAt(
                    orgBean.getAddress(),baseTableRow,2);
                ((DefaultTableModel)organizationTable.getModel()).setValueAt(
                    orgBean.getCongressionalDistrict(),baseTableRow,3);
                ((DefaultTableModel)organizationTable.getModel()).setValueAt(
                    orgBean.getFederalEmployerID(),baseTableRow,4);
                ((DefaultTableModel)organizationTable.getModel()).setValueAt(
                    orgBean.getVendorCode(),baseTableRow,5);
                ((DefaultTableModel)organizationTable.getModel()).setValueAt(
                    orgBean.getDunsNumber(),baseTableRow,6);
                ((DefaultTableModel)organizationTable.getModel()).setValueAt(
                    orgBean.getDunsPlusFourNumber(),baseTableRow,7);
                ((DefaultTableModel)organizationTable.getModel()).setValueAt(
                    orgBean.getDodacNumber(),baseTableRow,8);
                ((DefaultTableModel)organizationTable.getModel()).setValueAt(
                    orgBean.getCageNumber(),baseTableRow,9);
                // Bug Fix 2114 - start - step1
                ((DefaultTableModel)organizationTable.getModel()).fireTableDataChanged();
                // Bug Fix 2114 - End - Step1
                int selRow = organizationTable.getSelectedRow(); 
                if(  selRow != -1 ) {
                    // Bug Fix 2114 - start - Step2
                    baseTableRow = organizationTable.getSelectedRow();
                }
                organizationTable.setRowSelectionInterval(baseTableRow,baseTableRow);
                // Bug Fix 2114 - End - Step2
          
            }
            
        }
    }
    
    public void saveAsActiveSheet() {
        SaveAsDialog saveAsDialog = new SaveAsDialog(organizationTable);
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
    class OrganizationMouseAdapter extends MouseAdapter{
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
                newSortedData.addElement(organizationTable.getColumnName(column));
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
