/*
 * @(#)CommitteeBaseBaseWindow.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.irb.gui;


import edu.mit.coeus.departmental.gui.LookUpWindowConstants;
import edu.mit.coeus.gui.CoeusTableWindow;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.toolbar.*;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.organization.gui.DetailForm;
import edu.mit.coeus.irb.bean.CommitteeMaintenanceFormBean;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.OtherLookupBean;
import edu.mit.coeus.utils.TableSorter;
import edu.mit.coeus.utils.BaseWindowObservable;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.utils.MultipleTableColumnSorter;
import edu.mit.coeus.utils.SortForm;

//Added by sharath - 19/10/2003 for Save As ToolBar Button - Start
import edu.mit.coeus.utils.saveas.SaveAsDialog;
//Added by sharath - 19/10/2003 for Save As ToolBar Button - End

import javax.swing.*;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.beans.*;

import java.util.Vector;
import java.util.Observable;
import java.util.Observer;

import java.applet.AppletContext;
import java.net.URL;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

/** This class is used to construct the parent window of Committee and associate
 * its menu and toolbar with listener. This window will display all the committees
 * in IRB, from which the user can select the committee details.
 *
 * @author Phaneendra Kumar
 * @version 1.0 September 27, 2002, 8:33 PM
 * @updated Subramanya
 * @version 1.1 October 18, 2002, 12.57PM
 * @updated Subramanya
 * @version 1.2 November 04, 2002, 4.25PM
 */

public class CommitteeBaseWindow extends CoeusInternalFrame
implements ActionListener, Observer {
    
    // Menu items for committee
    private CoeusMenuItem addCommittee,modifyCommittee,displayCommittee;
    // Toolbar for committee
    private CoeusToolBarButton btnAddCommittee;
    private CoeusToolBarButton btnModifyCommittee;
    private CoeusToolBarButton btnDisplayCommittee;
    private CoeusToolBarButton btnSearchCommittee;
    private CoeusToolBarButton btnCloseCommittee;
    private CoeusToolBarButton btnSort;//Added by nadh - 19/01/2005 for Sort ToolBar Button
    //Added by sharath - 19/10/2003 for Save As ToolBar Button - Start
    private CoeusToolBarButton btnSaveAsCommittee;
    //Added by sharath - 19/10/2003 for Save As ToolBar Button - End
    
    /**
     * Connection String to the Server Side.
     */
    private final String AREA_OF_RESEARCH_CONNECTION_URL =
    CoeusGuiConstants.CONNECTION_URL + "/CommitteeServlet";
    
    //Main MDI Form.
    private CoeusAppletMDIForm mdiForm = null;
    
    private CommitteeDetailsForm committeeDetails= null;
    
    // modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
    private edu.mit.coeus.iacuc.gui.CommitteeDetailsForm iacucCommDetails = null;
    // modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
    private  Vector columnNames;
    
    private Vector committeeData;
    
    private Vector committeeTableData;
    
    private int selectedCommitteeRow = -1;
    
    private JTable tblCommittee;
    
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    private int baseTableRow;
    
    //Added by nadh - 19/01/2005 for Sort ToolBar Button - Start
    private MultipleTableColumnSorter sorter;
    
    //holds sorted columns and its states
    private Vector vecSortedData;
    
    private static final int OK_CLICKED = 0;
    
    //holds column index
    private int oldCol = -1;
    
    //holds column status
    private int status=MultipleTableColumnSorter.NOT_SORTED;
    //Added by Nadh - End
    
    //COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
    private String committeeTypeCode = "";
    private String committeeType = "";
    private static final int COMMITTEE_INDEX = 2;
    private static final String COMM_WINDOW_TITLE = "Select Type for New Committee";
    private static final String IRB_COMM_TYPE_CODE = "1";
    private static final String IRB_COMM_TYPE = "IRB";
    private static final String IACUC_COMM_TYPE_CODE ="2";
    private static final String IACUC_COMM_TYPE = "IACUC";
    //COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
    
    /** Constructor to create new <CODE>CommitteeBaseWindow</CODE> with the given
     * <CODE>CoeusAppletMDIForm</CODE> as parent.
     * @param mdiForm reference to <CODE>CoeusAppletMDIForm</CODE>.
     */
    public CommitteeBaseWindow(CoeusAppletMDIForm mdiForm) {
        super("Committee", mdiForm,LIST_MODE);
        this.mdiForm = mdiForm;
        initComponents();
        mdiForm.putFrame(CoeusGuiConstants.COMMITTEEBASE_FRAME_TITLE,this);
        mdiForm.getDeskTopPane().add(this);
    }
    
    /** Constructor to create <CODE>CommitteeBaseWindow</CODE> with the given frame name and
     * parent component. This will be called from <CODE>CoeusMaintainMenu</CODE> when
     * the Committee menu item is selected in Maintain menu of the application.
     *
     * @param frameName String used to identify this InternalFrame.
     * @param mdiForm reference to <CODE>CoeusAppletMDIForm</CODE>.
     */
    public CommitteeBaseWindow(String frameName, CoeusAppletMDIForm mdiForm) {
        super(frameName, mdiForm);
        this.mdiForm = mdiForm;
        initComponents();
        mdiForm.putFrame(CoeusGuiConstants.COMMITTEEBASE_FRAME_TITLE,this);
        mdiForm.getDeskTopPane().add(this);
    }
    
    /**
     * Initialize the components the components for the base window
     */
    private void initComponents() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        
        setFrameMenu(committeeEditMenu());
        setToolsMenu(null);
        final JToolBar committeeToolBar = committeeToolBar();
        this.setFrameToolBar(committeeToolBar);
        this.setFrame(CoeusGuiConstants.COMMITTEEBASE_FRAME_TITLE);
        this.setFrameIcon(mdiForm.getCoeusIcon());
        JScrollPane scrlPnSearchRes = new JScrollPane();
        scrlPnSearchRes.setMinimumSize(new Dimension(22, 15));
        scrlPnSearchRes.setPreferredSize(new Dimension(600, 400));
        scrlPnSearchRes.setViewportView(createCommitteeInfo());
        scrlPnSearchRes.getViewport().setBackground(Color.white);
        scrlPnSearchRes.setForeground(java.awt.Color.white);
        getContentPane().add(scrlPnSearchRes);
    }
    
    /**
     * This method is used to set the column names for the Committee list table.
     * @param columnNames Vector which consists of names used to display as
     * headers in Committee list table.
     */
    public void setColumnNames(Vector columnNames){
        this.columnNames = columnNames;
    }
    
    /**
     * This method is used to get the column names used in Committee list table.
     * @return Vector which consists of names used to display as headers
     * in Committee list table.
     */
    public Vector getColumnNames(){
        return columnNames;
    }
    
    /**
     * This method is used to set the data used to populate the Committee list
     * table used in this form.
     * @param committeeData Collection of data for all Committees displayed in
     * this form.
     */
    public void setCommitteeData(Vector committeeData){
        int beanLength = 0;
        if(committeeData != null){
            beanLength = committeeData.size();
        }
        if(committeeTableData!=null && committeeTableData.size()>0){
            committeeTableData.removeAllElements();
        }
        if(committeeTableData == null){
            committeeTableData = new Vector();
        }
        for(int beanIndex=0;beanIndex<beanLength;beanIndex++){
            CommitteeMaintenanceFormBean committeeBean =
            (CommitteeMaintenanceFormBean)
            committeeData.elementAt(beanIndex);
            Vector committeeRowData = new Vector();
            committeeRowData.addElement(committeeBean.getCommitteeId());
            committeeRowData.addElement(committeeBean.getCommitteeName());
            //IACUC CHANGES - Start
            committeeRowData.addElement(committeeBean.getCommitteeTypeDesc());
            //IACUC CHANGES - End
            committeeRowData.addElement(committeeBean.getUnitNumber());
            committeeRowData.addElement(committeeBean.getUnitName());
            committeeRowData.addElement(committeeBean.getDescription());
            committeeRowData.addElement(""+beanIndex); // for holding index value for sorting
            committeeTableData.addElement(committeeRowData);
        }
        
    }
    
    /**
     * This method is used to get the data used to populate the Committee list
     * table used in this form.
     * @return Collection of data used to populate the Committee list
     * table used in this form.
     */
    public Vector getCommitteeData(){
        return committeeTableData;
    }
    
    /**
     *  The method used to create the committee info table
     *
     * @return JTable
     */
    private JTable createCommitteeInfo(){
        Vector colName =  new Vector();
        colName.add("Committee ID");
        colName.add("Committee Name");
        //IACUC Changes - Start
        colName.add("Committee Type");
        //IACUC Changes - End
        colName.add("Home Unit Number");
        colName.add("Unit Name");
        colName.add("Description");
        colName.add("Index");
        setColumnNames(colName);
        setCommitteeData(getCommitteList());
        tblCommittee = new JTable();
        tblCommittee.setFont(CoeusFontFactory.getNormalFont());
        tblCommittee.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        /**
         * Updated For : Table Sorting Case Insensitive
         *
         * Updated by Subramanya Feb' 20 2003
         */
        
        if(committeeTableData != null && committeeTableData.size() > 0){
            //commented by Nadh - start 19-01-2005
            /*TableSorter sorter = new TableSorter(
            new DefaultTableModel(getCommitteeData(),getColumnNames()){
                public boolean isCellEditable(int row,int col){
                    return false;
                }
            }, false ); //ADDED THIS
            tblCommittee.setModel(sorter);
            sorter.addMouseListenerToHeaderInTable(tblCommittee);
            //Commentted Nadh - end
            /*tblCommittee.setModel(new DefaultTableModel(getCommitteeData(),
            getColumnNames()){
                public boolean isCellEditable(int row,int col){
                    return false;
                }
            });*/
            //Added by nadh - 19/01/2005 for Sort ToolBar Button - Start
            sorter = new MultipleTableColumnSorter(new DefaultTableModel(getCommitteeData(),getColumnNames()){
                public boolean isCellEditable(int row,int col){
                    return false;
                }
            });
            //Added by Nadh - End
            tblCommittee.setModel(sorter);
            sorter.setTableHeader(tblCommittee.getTableHeader());
            
            tblCommittee.addRowSelectionInterval(0,0);
        }else{
            //Added by nadh - 19/01/2005 for Sort ToolBar Button - Start
            sorter = new MultipleTableColumnSorter(new DefaultTableModel(new Object[][]{},getColumnNames().toArray()){
                public boolean isCellEditable(int row,int col){
                    return false;
                }
            });
            //Added by Nadh - End
            tblCommittee.setModel(sorter);
            sorter.setTableHeader(tblCommittee.getTableHeader());
            //commented by Nadh - start 19-01-2005
           /* TableSorter sorter = new TableSorter(
            new DefaultTableModel(new Object[][]{},getColumnNames().toArray()){
                public boolean isCellEditable(int row,int col){
                    return false;
                }
            }, false ); //ADDED THIS
            
            tblCommittee.setModel(sorter);
            sorter.addMouseListenerToHeaderInTable(tblCommittee);
            //Commentted Nadh - end
            /*tblCommittee.setModel(new DefaultTableModel(
            new Object[][]{},getColumnNames().toArray()){
                public boolean isCellEditable(int row,int col){
                    return false;
                }
            });*/
        }
        TableColumn column = tblCommittee.getColumn("Index");
        column.setMaxWidth(0);
        column.setMinWidth(0);
        column.setPreferredWidth(0);
        
        tblCommittee.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblCommittee.getTableHeader().addMouseListener(new CommitteeMouseAdapter());
        tblCommittee.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent me){
                String keyValue ="";
                if (me.getClickCount() == 2) {
                    try{
                        //Bug fix:1062 hour glass implementation
                        mdiForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
                        displayCommittee();
                        mdiForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                    } catch(Exception e){
                        //e.printStackTrace();
                    }
                }
            }
        });
        return tblCommittee;
    }
    
    /**
     * constructs Committee edit menu
     *
     * @return JMenu committee edit menu
     */
    private CoeusMenu committeeEditMenu() {
        CoeusMenu mnuCommittee = null;
        Vector fileChildren = new Vector();
        /* This is to add a new committee */
        addCommittee = new CoeusMenuItem("Add",null,true,true);
        addCommittee.addActionListener(this);
        addCommittee.setMnemonic('A');
        
        /* This is to modify the selected committee details */
        modifyCommittee = new CoeusMenuItem("Modify",null,true,true);
        modifyCommittee.setMnemonic('M');
        modifyCommittee.addActionListener(this);
        
        /* This is to display the selected committee details */
        displayCommittee = new CoeusMenuItem("Display",null,true,true);
        displayCommittee.setMnemonic('i');
        displayCommittee.addActionListener(this);
        
        fileChildren.add(addCommittee);
        fileChildren.add(modifyCommittee);
        fileChildren.add(displayCommittee);
        
        /* This is the mail Edit menu which will be added to the main MDIMenu*/
        mnuCommittee = new CoeusMenu("Edit",null,fileChildren,true,true);
        mnuCommittee.setMnemonic('E');
        return mnuCommittee;
        
    }
    
    
    /**
     * Committee ToolBar is a which provides the Icons for Performing
     * Save, Add, Mofify, Close buttons.
     *
     * @returns JToolBar committee Toolbar
     */
    private JToolBar committeeToolBar() {
        JToolBar toolbar = new JToolBar();
        /* This is the tool bar button to add a new committee*/
        btnAddCommittee = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ADD_ICON)),null,
        "Add Committee");
        /* This is the tool bar button to modify the selected committee */
        btnModifyCommittee = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.EDIT_ICON)),null,
        "Modify Committee");
        /* This is the tool bar button used to display the selected committee*/
        btnDisplayCommittee = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DISPLAY_ICON)),null,
        "Display Committee");
        
        /* This is used to close the existing opened committee details window */
        btnCloseCommittee = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),null,
        "Close Committee");
        
        //Added by nadh - 19/01/2005 for Sort ToolBar Button - Start
        btnSort = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SORT_ICON)),null,
        "Sort Committee");
        //Added by Nadh - End
        
        //Added by sharath - 19/10/2003 for Save As ToolBar Button - Start
        btnSaveAsCommittee = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SAVEAS_ICON)),
        null, "Save As");
        //Added by sharath - 19/10/2003 for Save As ToolBar Button - End
        
        
        btnAddCommittee.addActionListener(this);
        btnModifyCommittee.addActionListener(this);
        btnDisplayCommittee.addActionListener(this);
        btnCloseCommittee.addActionListener(this);
        
        //Added by nadh - 19/01/2005 for Sort ToolBar Button - Start
        btnSort.addActionListener(this);
        //Added by Nadh - End
        
        //Added by sharath - 19/10/2003 for Save As ToolBar Button - Start
        btnSaveAsCommittee.addActionListener(this);
        //Added by sharath - 19/10/2003 for Save As ToolBar Button - End
        
        toolbar.add(btnAddCommittee);
        toolbar.add(btnModifyCommittee);
        toolbar.add(btnDisplayCommittee);
        toolbar.addSeparator();
        toolbar.add(btnSort);//Added by nadh - 19/01/2005 for Sort ToolBar Button
        //Added by sharath - 19/10/2003 for Save As ToolBar Button - Start
        toolbar.add(btnSaveAsCommittee);
        //Added by sharath - 19/10/2003 for Save As ToolBar Button - End
        toolbar.addSeparator();
        toolbar.add(btnCloseCommittee);
        
        toolbar.setFloatable(false);
        return toolbar;
    }
    
    /** Overridden method of <code>ActionListener</code>. Actions to be performed
     * for all the menu items and toolbars associated with this form are specified
     * in this method.
     * @param actionType <CODE>ActionEvent</CODE>, a semantic event which indicates that a
     * component-defined action occured.
     */
    public void actionPerformed(ActionEvent actionType) {
        try {
            Object actSource = actionType.getSource();
            if (actSource.equals(addCommittee)
            || actSource.equals(btnAddCommittee)) {
                addCommittee();
            } else if (actSource.equals(modifyCommittee) ||
            actSource.equals(btnModifyCommittee)) {
                modifyCommittee();
            } else if (actSource.equals(displayCommittee) ||
            actSource.equals(btnDisplayCommittee)) {
                displayCommittee();
            }else if (actSource.equals(btnCloseCommittee)) {
                closeCommittee();
            }
            //Added by nadh - 19/01/2005 for Sort ToolBar Button - Start
            else if (actSource.equals(btnSort)) {
                showSort();
            }//Added by Nadh - End
            
            //Added by sharath - 19/10/2003 for Save As ToolBar Button - Start
            else if(actSource.equals(btnSaveAsCommittee)) {
                //showSaveAsDialog();
                //new implementation
               saveAsActiveSheet();
            }
            //Added by sharath - 19/10/2003 for Save As ToolBar Button - End
        }catch(Exception e){
            //e.printStackTrace();
            if(e instanceof CoeusException){
                System.out.println("CoeusException");
            }else if (e instanceof PropertyVetoException){
                //do nothing
            }else {
                CoeusOptionPane.showInfoDialog(e.getMessage());
            }
            
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
        SortForm sortForm = new SortForm(tblCommittee,vecSortedData);
        Vector sortedData = sortForm.display();
        vecSortedData = (Vector)sortedData.get(1);
        if(((Integer)sortedData.get(0)).intValue() == OK_CLICKED)
            sorter.doSort(tblCommittee,vecSortedData);
        else
            return;
    }// Added by Nadh - end
    
    /** Modified for COEUSQA-2685_IACUC - comm member areas of 
     research should point to IACUC areas of research*/
    /** 
     *  This method is used to add a committee.
     *  @throws Exception
     */
    private void addCommittee() throws Exception{        
        createCommittee('A',"",mdiForm,-1,"");        
    }
    
    
    /** Modified for COEUSQA-2685_IACUC - comm member areas of 
        research should point to IACUC areas of research*/    
    
    /** 
     * This method is used to modify a committee
     * @throws Exception
     */
    private void modifyCommittee() throws Exception{
        int selectedRow = tblCommittee.getSelectedRow();
        if(selectedRow != -1){
            selectedCommitteeRow = selectedRow;
            String keyValue = tblCommittee.getValueAt(selectedRow,0).toString();            
            createCommittee('M',keyValue,mdiForm,selectedRow,getCommitteeTypeCode(selectedRow));            
        }
    }
    
    /** Modified for COEUSQA-2685_IACUC - comm member areas of 
        research should point to IACUC areas of research*/ 
    /**
     * This method is used to display the selected committee information
     * @throws Exception
     */
    private void displayCommittee() throws Exception {
        int selectedRow = tblCommittee.getSelectedRow();
        if(selectedRow != -1){
            String keyValue = tblCommittee.getValueAt(selectedRow,0).toString();            
            createCommittee('D',keyValue,mdiForm,selectedRow,getCommitteeTypeCode(selectedRow));            
        }
    }
    
    /*
     *  This method used to close the committe Base window.
     */
    private void closeCommittee(){
        this.doDefaultCloseAction();
    }
    
    
    /** Modified for COEUSQA-2685_IACUC - comm member areas of 
        research should point to IACUC areas of research*/ 
    /**
     *  Method used to create the committee detail.
     *
     * @param functionType
     * @param committeeId
     * @param mdiForm
     * @param committeeTypeCode
     * @throws Exception
     */
    private void createCommittee(char functionType,String committeeId,
    CoeusAppletMDIForm mdiForm, int selectedRow,String committeeTypeCode ) throws Exception{
        
        // COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start        
        Vector vecCommTypes = new Vector();
        ComboBoxBean comboBoxBean;
        if(functionType == 'A'){
            vecCommTypes = getCommitteeTypes();            
            OtherLookupBean otherLookupBean =
                    new OtherLookupBean(COMM_WINDOW_TITLE, vecCommTypes, LookUpWindowConstants.COMMITEE_TYPES);
            CoeusTableWindow coeusTableWindow =
                    new CoeusTableWindow(otherLookupBean);
            int selRow = otherLookupBean.getSelectedInd();
            if(selRow >= 0){
                comboBoxBean = (ComboBoxBean)vecCommTypes.elementAt(selRow);
                if(comboBoxBean != null){
                    committeeTypeCode = comboBoxBean.getCode();
                    committeeType = comboBoxBean.getDescription();
                }
            }
        }
        // COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
        
        /**
         * If the committeDetails form is not open then the instance will be
         * created and the committeeData instance will be set to the Committee
         * Details form to update or insert the changed/new committee
         * information in the committeeData.
         *
         */
        boolean duplicate = false;
        try{
            /* check whether the requested record already opened */
            duplicate = mdiForm.checkDuplicate(
            CoeusGuiConstants.COMMITTEE_FRAME_TITLE, committeeId, functionType);
        }catch(Exception e){
            /* Exception occured.  Record may be already opened in requested mode
               or if the requested mode is edit mode and application is already
               editing any other record. */
            duplicate = true;
            if(e.getMessage().length() > 0){
                CoeusOptionPane.showInfoDialog(e.getMessage());
            }
            /* try to get the requested frame which is already opened */
            CoeusInternalFrame frame = mdiForm.getFrame(
            CoeusGuiConstants.COMMITTEE_FRAME_TITLE,committeeId);
            if(frame == null){
                /* if no frame opened for the requested record then the
                   requested mode is edit mode. So get the frame of the
                   editing record. */
                frame = mdiForm.getEditingFrame(
                CoeusGuiConstants.COMMITTEE_FRAME_TITLE );
            }
            if (frame != null){
                frame.setSelected(true);
                frame.setVisible(true);
            }
            return;
        }
        try{
            // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start            
            if(IRB_COMM_TYPE_CODE.equals(committeeTypeCode)){
                committeeDetails = new CommitteeDetailsForm(functionType,committeeId, mdiForm);
                committeeDetails.showCommitteDetailForm();
                committeeDetails.setCommitteesheet(tblCommittee);
                if( functionType != 'D') {
                    committeeDetails.registerObserver( this );
                    if( selectedRow != -1 ) {
                        baseTableRow = Integer.parseInt((String)tblCommittee.getValueAt(
                                selectedRow,tblCommittee.getColumnCount()-1));
                    }
                }
            }else if(IACUC_COMM_TYPE_CODE.equals(committeeTypeCode)){
                iacucCommDetails =
                        new edu.mit.coeus.iacuc.gui.CommitteeDetailsForm(functionType,committeeId, mdiForm);
                iacucCommDetails.showCommitteDetailForm();
                iacucCommDetails.setCommitteesheet(tblCommittee);
                if( functionType != 'D') {
                    iacucCommDetails.registerObserver( this );
                    if( selectedRow != -1 ) {
                        baseTableRow = Integer.parseInt((String)tblCommittee.getValueAt(
                                selectedRow,tblCommittee.getColumnCount()-1));
                    }
                }
            }
            // modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
            
            // added by manoj to catch authorization messages and to display as information messages
        }catch(CoeusException ex){
            CoeusOptionPane.showDialog(new CoeusClientException(ex));
        }catch ( Exception e ) {
            //e.printStackTrace();
            try {
                // modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
                boolean isModifiable = false;
                if(IRB_COMM_TYPE_CODE.equals(committeeTypeCode)){
                    isModifiable = committeeDetails.isModifiable();
                }else if(IACUC_COMM_TYPE_CODE.equals(committeeTypeCode)){
                    isModifiable = iacucCommDetails.isModifiable();
                }                
//                if (!committeeDetails.isModifiable() ) {
                if (isModifiable) {
                // modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
                    String msg = coeusMessageResources.parseMessageKey(
                    "committee_row_clocked_exceptionCode.444444");
                    int resultConfirm = CoeusOptionPane.showQuestionDialog(msg,
                    CoeusOptionPane.OPTION_YES_NO,
                    CoeusOptionPane.DEFAULT_YES);
                    if (resultConfirm == 0) {
                    /* Call the RolodexDetails form with functionType
                     * as 'V'  and RolodexDetailsBean as inormation and
                     * the connectionURl string information
                     */
                        displayCommittee();
                    }
                }else {
                    throw new Exception(e.getMessage());
                }
            }catch(Exception ex){
                //ex.printStackTrace();
                throw new Exception(ex.getMessage());
            }
        }
    }
    
    /** This method is used to get the details of all the Committees from the
     * database.
     *
     * @return Collection of <CODE>CommitteeMaintenanceFormBean</CODE> whose values
     * will be used to populate the Committee list table.
     */
    public Vector getCommitteList() {
        /**
         * This sends the functionType as 'G' to the servlet indiacting to
         * get the details of all existing committees with the required
         * information
         */
        
        Vector vecBeans = new Vector();
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/comMntServlet";
        RequesterBean request = new RequesterBean();
        request.setFunctionType('G');
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response.isSuccessfulResponse()) {
            vecBeans = (Vector)(
            (Vector) response.getDataObjects()).elementAt(0);
        } else {
            log(response.getMessage());
        }
        return vecBeans;
    }
    
    /**
     * display message from the server on mdi form
     */
    private void log(String mesg) {
        CoeusOptionPane.showErrorDialog(mesg);
    }
    
    /** This method is called from Save menu item under File Menu.
     * Implemented the abstract class declared in parent(<CODE>CoeusInternalFrame</CODE>).
     * Empty body since the Save operation is not required for this list screen.
     */
    public void saveActiveSheet() {
    }
    
    /*
     * The method used to update a committee detail row in the
     * CommitteeBase window
     */
    public void update(Observable observable, Object obj){
        if( obj instanceof CommitteeMaintenanceFormBean ) {
            try {
                CommitteeMaintenanceFormBean committeeBean =
                (CommitteeMaintenanceFormBean) obj;
                Vector committeeRow = new Vector();
                committeeRow.add(committeeBean.getCommitteeId());
                committeeRow.add(committeeBean.getCommitteeName());
                //Added for COEUSQA-1724 : IACUC module  - Start
                committeeRow.add(committeeBean.getCommitteeTypeDesc());
                //COEUSQA-1724  - End
                committeeRow.add(committeeBean.getUnitNumber());
                committeeRow.add(committeeBean.getUnitName());
                committeeRow.add(committeeBean.getDescription());
                int lastRow = tblCommittee.getRowCount();
                committeeRow.addElement(""+lastRow);
                if (((BaseWindowObservable)observable).getFunctionType() == 'A'){
                    ((DefaultTableModel)tblCommittee.getModel()).insertRow(lastRow,
                    committeeRow);
                    baseTableRow = lastRow;
                }else if (((BaseWindowObservable)observable).getFunctionType() == 'M'){
                    ((DefaultTableModel)tblCommittee.getModel()).setValueAt(
                    committeeBean.getCommitteeId(),baseTableRow,0);
                    ((DefaultTableModel)tblCommittee.getModel()).setValueAt(
                    committeeBean.getCommitteeName(),baseTableRow,1);
                    //Added for COEUSQA-1724 : IACUC module  - Start
                    ((DefaultTableModel)tblCommittee.getModel()).setValueAt(
                            committeeBean.getCommitteeTypeDesc(),baseTableRow,2);
                    //COEUSQA-1724  - End
                    ((DefaultTableModel)tblCommittee.getModel()).setValueAt(
                    committeeBean.getUnitNumber(),baseTableRow,3);
                    ((DefaultTableModel)tblCommittee.getModel()).setValueAt(
                    committeeBean.getUnitName(),baseTableRow,4);
                    ((DefaultTableModel)tblCommittee.getModel()).setValueAt(
                    committeeBean.getDescription(),baseTableRow,5);
                    int selRow = tblCommittee.getSelectedRow();
                    if(  selRow != -1 ) {
                        baseTableRow = Integer.parseInt((String)tblCommittee.getValueAt(
                        selRow,tblCommittee.getColumnCount()-1));
                    }
                }
            }catch(Exception e){
                //e.printStackTrace();
            }
        }
    }
    
    //Added by sharath - 19/10/2003 for Save As ToolBar Button - Start
    /** 
     * This method is called from SaveAs Toolbar Menu Item.
     */
    public void showSaveAsDialog(){
        //SaveAsDialog saveAsDialog = new SaveAsDialog(tblProposal.getModel());
        SaveAsDialog saveAsDialog = new SaveAsDialog(tblCommittee);
    }
    
    public void saveAsActiveSheet() {
         SaveAsDialog saveAsDialog = new SaveAsDialog(tblCommittee);
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
    class CommitteeMouseAdapter extends MouseAdapter{
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
                newSortedData.addElement(tblCommittee.getColumnName(column));
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
    
    // Added for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
    /**
     * Get Committee Types
     * @return vector of committee types 
     * @throws  Exception
     */
    private Vector getCommitteeTypes() throws Exception{
        
        Vector vecCommitteeTypes = new Vector();
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
        RequesterBean request = new RequesterBean();
        request.setDataObject("GET_COMMITTEE_TYPES");
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        
        if (response!=null){
            if (response.isSuccessfulResponse()){
                vecCommitteeTypes = (Vector)response.getDataObject();
            }
        }
        
        return vecCommitteeTypes;
    }
    
    /**
     *  Get the Committee Type Code in selected row from
     *  table tblCommittee
     *  @param selectedRow
     *  @return committeeTypeCode
     */
    private String getCommitteeTypeCode(int selectedRow){
        
        committeeType = tblCommittee.getValueAt(selectedRow,COMMITTEE_INDEX).toString();
        if(committeeType.equalsIgnoreCase(IRB_COMM_TYPE)){
            committeeTypeCode = IRB_COMM_TYPE_CODE;
        }else if(committeeType.equalsIgnoreCase(IACUC_COMM_TYPE)){
            committeeTypeCode = IACUC_COMM_TYPE_CODE;
        }
        return committeeTypeCode;
    }
    
    // Added for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
}
