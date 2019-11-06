/*
 * @(#)UserRolesTable.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on April 10, 2003, 7:56 PM
 */

package edu.mit.coeus.utils;

import java.awt.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;

import java.util.Hashtable;
import java.util.List;
import java.util.Iterator;
import java.util.Vector;
import java.util.TreeSet;

import java.io.*;
import java.io.IOException;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.utils.tree.TransferableUserRoleData;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.TableSorter;

/**
 * This class is a subclass of JTable which is used to hold user details and
 * provides drag functionality.
 */

public class UserRolesTable extends JTable implements DragSourceListener, DragGestureListener {
    
    private Vector dataset = null;
    private TreeSet userSet  = new TreeSet();
    /**
     * enables this component to be a Drag Source
     */
    DragSource dragSource = null;
    
    Cursor rowCursor = new Cursor(Cursor.HAND_CURSOR);
    
    private TableSorter sorter;
    
    /**
     * Default constructor which initializes the DropTarget and DragSource.
     */
    public UserRolesTable() {
        //dataset = new Vector();
        DefaultTableModel tableModel
        = new DefaultTableModel( new Object[][]{}, getDefaultColumnNames()){
            public boolean isCellEditable( int row, int col ) {
                return false;
            }
        };
        setModel( tableModel );
        /*sorter = new TableSorter( tableModel , false );
        setModel( sorter );
        sorter.addMouseListenerToHeaderInTable( this );*/

        setTableColumnSizes();
        dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer( this,
        DnDConstants.ACTION_MOVE, this);
        getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        setFont( CoeusFontFactory.getNormalFont() );
        //setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    /**
     * Constructs UserRolesTable with the given data and column names.
     * @param dataValues Collection of data to be shown in the table.
     * @param colName Collection of column names to be used for the table.
     */
    public UserRolesTable( Vector dataValues, Vector colName ) {
        dataValues = ( ( dataValues == null ) ? new Vector() : dataValues );
        DefaultTableModel tableModel =
        new DefaultTableModel( getUserTableData( dataValues ), colName ){
            public boolean isCellEditable( int row, int col ) {
                return false;
            }
        };
        setModel( tableModel );
        /*sorter = new TableSorter( tableModel , false );
        setModel( sorter );
        sorter.addMouseListenerToHeaderInTable( this );*/

        setTableColumnSizes();
        dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer( this,
        DnDConstants.ACTION_MOVE, this);
        getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        setFont( CoeusFontFactory.getNormalFont() );
    }
    
    /**
     * Sets the table data with the given data values and column names.
     *
     * @param dataValues Collection of data to be shown in the table.
     * @param colNames Collection of column names to be used for the table.
     */
    public void setUserTableData( Vector dataValues, Vector colNames ){
        dataValues = ( ( dataValues == null ) ? new Vector() : dataValues );
        ( ( DefaultTableModel ) getModel()  ).setDataVector(
        getUserTableData( dataValues ), colNames);
        ( ( DefaultTableModel ) getModel() ).fireTableDataChanged();
        
        sorter = new TableSorter( ( DefaultTableModel ) getModel()
        , false );
        setModel( sorter );
        sorter.addMouseListenerToHeaderInTable( this );
        
        setTableColumnSizes();
    }
    
    /*public int getSelectedRow() {
        return sorter.getIndexForRow(super.getSelectedRow());
    }
    
    public int[] getSelectedRows() {
        return sorter.getIndexForRows(super.getSelectedRows());
    }*/
    
    /* Supporting method to set the column sizes to the table */
    private void setTableColumnSizes(){
        setOpaque( false );
        setCellSelectionEnabled( false );
        setRowSelectionAllowed( true );
        
        TableColumn column = getColumnModel().getColumn(0);
        
        column.setMaxWidth(40);
        column.setMinWidth(20);
        column.setPreferredWidth(30);
        column.setHeaderValue("  ");
        column.setCellRenderer( new StatusCellRenderer() );
        column.setResizable( false );
        //for User ID column
        column = getColumnModel().getColumn(1);
        //column.setMaxWidth(60);
        column.setMinWidth(60);
        column.setPreferredWidth(70);
        column.sizeWidthToFit();
        
        //for User Name column
        column = getColumnModel().getColumn(2);
        //column.setMaxWidth(100);
        column.setMinWidth(100);
        column.setPreferredWidth(110);
        
        //for Unit Number column
        column = getColumnModel().getColumn(3);
        column.setMinWidth(100);

        //for Unit Name column
        column = getColumnModel().getColumn(4);
        //column.setMaxWidth(120);
        column.setMinWidth(190);
        //column.setPreferredWidth(120);

        //for hidden index column
        column = getColumnModel().getColumn(5);
        column.setMaxWidth(0);
        column.setMinWidth(0);
        column.setPreferredWidth(0);
        getTableHeader().setReorderingAllowed( false );
        //getTableHeader().setResizingAllowed( false );
    }
    /**
     * Overloaded method which extracts the user details from the UserInfoBean and
     * constructs a vector which is used to add to the table.
     * @param newBean UserInfoBean consisting of new user data to be added to the
     * table
     */
    
    public void addRow( UserInfoBean newBean ){
        Vector newRow = getUserRow( newBean );
        newRow.addElement( ""+getRowCount() );
        addRow( newRow );
    }
    
    public void modifyRow( UserInfoBean newBean) {
        Vector newRow = getUserRow( newBean );
        //newRow.addElement( ""+getRowCount() );
        modifyRow(newRow);
    }
    
    public void modifyRow( Vector newRow) {
        /*DefaultTableModel tableModel = (DefaultTableModel )getModel();
        Vector dVector = tableModel.getDataVector();
        int rowCount = getRowCount();//dVector.size();
        int rowIndex=0;
        //Vector vecElement=null;
        for(rowIndex =0;rowIndex<rowCount;rowIndex++){
            // vecElement = (Vector)dVector.elementAt(rowIndex);
            String userId = ( String )getValueAt( rowIndex, 1 );
            if( userId.equals((String)newRow.elementAt(1))){
                for(int colIndex = 0; colIndex < newRow.size(); colIndex++) {
                    tableModel.setValueAt(newRow.get(colIndex),rowIndex,colIndex);
                }
                tableModel.fireTableDataChanged();
                setRowSelectionInterval(rowIndex,rowIndex);
                break;
            }
        }*/
        /*int selectedRow = getSelectedRow();
        String userId = ( String )newRow.get(1);
        int rowIndex=0;
        for(rowIndex = 0; rowIndex < sorter.getRowCount(); rowIndex++){
            if( userId.equals((String)sorter.getValueAt(rowIndex, 1).toString())){
                for(int colIndex = 0; colIndex < newRow.size(); colIndex++) {
                    sorter.setValueAt(newRow.get(colIndex),rowIndex,colIndex);
                }
                sorter.fireTableDataChanged();
                setRowSelectionInterval(rowIndex,rowIndex);
                break;
            }
        }*/
        int selectedRow = getSelectedRow();
        /*for(int colIndex = 0; colIndex < newRow.size(); colIndex++) {
            sorter.setValueAt(newRow.get(colIndex),selectedRow,colIndex);
        }*/
        int hiddenIndex = Integer.parseInt( (String)getValueAt(
                            selectedRow,getColumnCount() - 1 ) ); 
        for(int colIndex = 0; colIndex < newRow.size(); colIndex++) {
            sorter.setValueAt(newRow.get(colIndex),hiddenIndex,colIndex);
        }
        
        
/*        sorter.removeRow(sorter.indexes[selectedRow]);
        newRow.setElementAt(""+hiddenIndex,newRow.size()-1);
        sorter.insertRow(selectedRow, newRow);*/
        
    }
    
    /**
     * Adds the given user details to the table in sorted order of their userid.
     * @param newRow Vector which consists of user details.
     */
    public void addRow( Vector newRow ) {
        if( userSet.add( newRow.elementAt( 1 ) ) ){
            DefaultTableModel tableModel = (DefaultTableModel )getModel();
            /*Vector dVector = tableModel.getDataVector();*/
            int rowCount = getRowCount();//dVector.size();
            int rowIndex=0;
            //Vector vecElement=null;
            for(rowIndex =0;rowIndex<rowCount;rowIndex++){
                // vecElement = (Vector)dVector.elementAt(rowIndex);
                String userId = ( String )getValueAt( rowIndex, 1 );
                if( userId.compareTo((String)newRow.elementAt(1)) < 0){
                    continue;
                }
                else {
                    break;
                }
                
            }
            if( tableModel instanceof TableSorter ) {
                ( (TableSorter) tableModel).insertRow( rowCount,newRow );
            }else{
                tableModel.insertRow( rowCount,newRow );
                sorter = new TableSorter( tableModel , false );
                setModel( sorter );
                sorter.addMouseListenerToHeaderInTable( this );
                setTableColumnSizes();
            }
            //sorter.fireTableDataChanged();
            //setRowSelectionInterval(rowIndex,rowIndex);
        }
    }
    
    /* Supporting method which constructs UserInfoBean for the specified table row */
    private UserInfoBean constructBean( int row ) {
        UserInfoBean userBean = new UserInfoBean();
        
        userBean.setStatus( getValueAt( row, 0 ).toString().charAt(0) );
        userBean.setUserId( getValueAt( row, 1 ).toString() );
        userBean.setUserName( getValueAt( row, 2 ).toString() );
        userBean.setUnitNumber( getValueAt( row, 3 ).toString() );
        userBean.setUnitName( getValueAt( row, 4 ).toString() );
        return userBean;
    }
    
    /**
     * a drag gesture has been initiated
     *
     */
    
    public void dragGestureRecognized( DragGestureEvent event) {
        
        int selRowsCount = getSelectedRowCount();
        int selRow = 0;
        if ( selRowsCount > 0 ){
            int selRowIndices[] = getSelectedRows();
            Vector multipleUsers = new Vector();
            for( int indx = 0 ; indx < selRowIndices.length ; indx++ ){
                selRow = selRowIndices[ indx ];
                //UserInfoBean dragData = ( UserInfoBean ) dataset.elementAt( selRow );
                
                UserInfoBean dragData = new UserInfoBean();
                dragData = constructBean( selRow );
                multipleUsers.addElement( dragData );
            }
            // as the name suggests, starts the dragging
            TransferableUserRoleData data = new TransferableUserRoleData(multipleUsers);
            dragSource.startDrag( event, rowCursor, data, this);
            
        }
        
    }
    
    /**
     * this message goes to DragSourceListener, informing it that
     * the dragging has ended
     *
     */
    
    public void dragDropEnd(DragSourceDropEvent event) {
    }
    
    /**
     * this message goes to DragSourceListener, informing it that
     * the dragging has entered the DropSite
     *
     */
    
    public void dragEnter(DragSourceDragEvent event) {
    }
    
    /**
     * this message goes to DragSourceListener, informing it that the dragging
     * has exited the DropSite
     *
     */
    
    public void dragExit(DragSourceEvent event) {
        
    }
    
    /**
     * this message goes to DragSourceListener, informing it that
     * the dragging is currently ocurring over the DropSite
     *
     */
    
    public void dragOver(DragSourceDragEvent event) {
    }
    
    /**
     * is invoked when the user changes the dropAction
     *
     */
    
    public void dropActionChanged( DragSourceDragEvent event) {
    }
    
    private static String[] getDefaultColumnNames(){
        String colNames[] = { "Role", "User ID", "User Name", "Unit Number",
        "Unit Name","Index" };
        
        return colNames;
    }
    /* Supporting method which constructs table data by extracting all user details
     * from the collection of UserInfoBean.
     */
    private Vector getUserTableData( Vector data ){
        Vector tableData = new Vector();
        int totalNoRows = data.size();
        for( int indx = 0 ; indx < totalNoRows ; indx++ ){
            Vector row = getUserRow( (UserInfoBean)data.get(indx) );
            if( userSet.add( row.elementAt( 1 ) ) ){
                row.addElement(""+indx);
                tableData.addElement( row );
            }
        }
        return tableData;
    }
    /* supporting method used to construct table row from the UserInfoBean */
    private Vector getUserRow( UserInfoBean infoBean ){
        Vector rowData = new Vector();
        rowData.addElement( ""+infoBean.getStatus() );//Icon Renderer.
        rowData.addElement( infoBean.getUserId() );
        rowData.addElement( infoBean.getUserName() );
        rowData.addElement(infoBean.getUnitNumber() );
        rowData.addElement( infoBean.getUnitName() );
        return rowData;
    }
    
    public int getUserRowInTable(UserInfoBean userInfoBean) {
        return getUserRowInTable(userInfoBean.getUserId());
    }
    
    public int getUserRowInTable(String userId) {
        int ID_COLUMN = 1;
        for(int row = 0; row < getRowCount(); row++) {
            if(userId.trim().equals(getValueAt(row, ID_COLUMN).toString().toString())) {
                return row;
            }
        }
        return -1;
    }
    
    /**
     * This method will return the user selected in the UserRolesTable.
     */
    public UserInfoBean getSelectedUser(){
        TableModel tblModel = getModel();
        int row = getSelectedRow();
        return constructBean(row);
    }
    
    /**
     * Custom cell renderer which is used in this table. Renders different icons to
     * the first column depending on the status of the user.
     */
    class StatusCellRenderer extends DefaultTableCellRenderer {
        ImageIcon activeUserIcon, inactiveUserIcon;
        String activeUserImageName, inactiveUserImageName;
        public StatusCellRenderer() {
            /* try to read from the URL, if unable to read , take the default values.
             * If UserRolesTable is used as a component in Forte Form editor, it can't read
             * from URL, so then it will take the icons from the strings given and
             * bring up the component.
             */
            
            activeUserImageName = "/images/usera.gif";
            inactiveUserImageName = "/images/useri.gif";
            java.net.URL actUser = getClass().getClassLoader().getResource( CoeusGuiConstants.ACTIVE_USER_ICON );
            if( actUser != null ) {
                activeUserIcon = new ImageIcon( actUser );
            }else{
                activeUserIcon = new ImageIcon( activeUserImageName );
            }
            java.net.URL inactUser = getClass().getClassLoader().getResource( CoeusGuiConstants.INACTIVE_USER_ICON );
            if( inactUser != null ) {
                inactiveUserIcon = new ImageIcon( inactUser );
            }else{
                inactiveUserIcon = new ImageIcon( inactiveUserImageName );
            }
        }
        // /images/usera.gif
        // /images/useri.gif
        
        public Component getTableCellRendererComponent(JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row,
        int column) {
            
            setOpaque(false);
            if( value != null ) {
                if( value.toString().equals("A") ) {
                    setIcon( activeUserIcon );
                }else if( value.toString().equals("I") ) {
                    setIcon( inactiveUserIcon );
                }
            }
            return this;
            
        }
        
    }
    
}
