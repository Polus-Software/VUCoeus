/*
 * GroupsController.java
 *
 * Created on November 23, 2004, 11:56 AM
 */

package edu.mit.coeus.sponsorhierarchy.controller;


import edu.mit.coeus.sponsorhierarchy.gui.GroupsForm;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.tree.TransferableUserRoleData;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.sponsormaint.bean.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.dnd.*;
import java.util.Vector;
/**
 *
 * @author  surekhan
 */
public class GroupsController extends SponsorHierarchyController implements 
                    DragSourceListener,DragGestureListener{
    
    private GroupsForm groupsForm;
    
    private CoeusDlgWindow dlgGroups;
   
    private CoeusAppletMDIForm mdiForm;
    
    private static final int WIDTH = 325;
    
    private static final int HEIGHT = 290;
    
    private static final String WINDOW_TITLE = "Groups";
    
    private static final String TITLE = "Sponsors";
    
    private static final int CODE = 0;
    
    private static final int SPONSOR_NAME = 1;
    
    private GroupsTableModel groupsTableModel;
    
    private String name;
    
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
    "/spMntServlet";
    
    private CoeusVector cvSponsors;
    private int[] rows;
    private static final String EMPTY_STRING = "";
    
    //variables for sorting purpose. will be true if last sort was Ascending
    //else will be false.
    private boolean sortCodeAsc = true;
    private boolean sortDescAsc = false;
    
    private JInternalFrame frame;
    
    DragSource dragSource;
    
    Cursor dragCursor = new Cursor(Cursor.HAND_CURSOR);
    
    private CoeusVector cvData;
    /** Creates a new instance of GroupsController */
    public GroupsController(CoeusVector cvData) {
        this.cvData = cvData;
        name = hierarchyName;
        cvSponsors = new CoeusVector();
        postInitComponents();
        setFormData(cvData);
        registerComponents();
        setTabelEditors();
       
    }
    
    private void postInitComponents(){
        groupsForm = new GroupsForm();
        dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(groupsForm.tblGroups, DnDConstants.ACTION_MOVE, this);
        frame = new JInternalFrame();
        groupsTableModel = new GroupsTableModel();
        groupsForm.tblGroups.setModel(groupsTableModel);
    }
   
    public void display() {
        frame.setVisible(true);
    }
    
    public void formatFields() {
    }
    
    public java.awt.Component getControlledUI() {
        return groupsForm;
    }
    
    public Object getFormData() {
        return null;
    }
    
    public void registerComponents() {
    }
    
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
    }
    
    public void setFormData(Object data) {
     cvData = (CoeusVector)data;
     groupsTableModel.setData(cvData);
    }
    
    public void refreshTableData(CoeusVector data){
        cvData.addAll(data);
        setFormData(cvData);
    }
    
    private void setTabelEditors(){
        groupsForm.tblGroups.setRowHeight(22);
        JTableHeader tableHeader = groupsForm.tblGroups.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        groupsForm.tblGroups.setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        groupsForm.tblGroups.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        TableSorter sorter = new TableSorter((AbstractTableModel)groupsForm.tblGroups.getModel(),false);
        groupsForm.tblGroups.setModel(sorter);
        sorter.addMouseListenerToHeaderInTable(groupsForm.tblGroups);
        
        TableColumn column = groupsForm.tblGroups.getColumnModel().getColumn(CODE);
        
        column.setPreferredWidth(70);
        column.setResizable(true);
        
        column = groupsForm.tblGroups.getColumnModel().getColumn(SPONSOR_NAME);
        column. setPreferredWidth(300);
        column.setResizable(true);
        
    }
    
    
    public boolean validate()  {
        return true;
    }
    
    public void cleanUp(){
    }
    
    public void dragDropEnd(DragSourceDropEvent dsde) {
        if(dsde.getDropSuccess()) {
            for(int index=rows.length-1; index>=0; index--) {
                cvData.remove(getSelectedRowIndex(groupsForm.tblGroups.getValueAt(rows[index], 0).toString()));
                groupsTableModel.fireTableRowsDeleted(rows[index], rows[index]);
            }
        }
    }
    
    public void dragEnter(DragSourceDragEvent dsde) {
    }
    
    public void dragExit(DragSourceEvent dse) {
    }
    
    public void dragOver(DragSourceDragEvent dsde) {
    }
    
    public void dropActionChanged(DragSourceDragEvent dsde) {
    }
    
    public void dragGestureRecognized(DragGestureEvent dragGestureEvent) {
        rows = groupsForm.tblGroups.getSelectedRows();
        if(rows.length > 0) {
            Vector cvMovebleData = new Vector();
            for(int index=0; index<rows.length; index++) {
                cvMovebleData.add((SponsorHierarchyBean)cvData.get(
                           getSelectedRowIndex(groupsForm.tblGroups.getValueAt(rows[index], 0).toString())));
            }
            TransferableUserRoleData transferableData = new TransferableUserRoleData(cvMovebleData);
            dragSource.startDrag(dragGestureEvent, dragCursor,transferableData, this);
        }
    }
    //Added for bug fix 1512 - nadh
    private int getSelectedRowIndex(String str) {
        int index;
        for(index=0;index<cvData.size();index++) 
            if(str.equals(((SponsorHierarchyBean)cvData.get(index)).getSponsorCode()))
                return index;
        return index;
    }// end Bug fix 1512 - nadh
    class GroupsTableModel extends AbstractTableModel {
        String colNames[] = { "Code" , "Sponsor Name" };
        Class[] colTypes  =  {String.class , String.class};
        
         /* If the cell is editable,return true else return false*/
        public boolean isCellEditable(int row, int col){
            return false;
        }

        public int getColumnCount() {
            return colNames.length;
        }
        
         /* returns the column names*/
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        /**
         * Returns the column class
         */
        public Class getColumnClass(int columnIndex) {
            return colTypes [columnIndex];
        }
        
        public int getRowCount() {
            if(cvData==null){
                return 0;
            }else{
                return cvData.size();
            }
        }
        
        public void setData(CoeusVector cvData){
            cvData = cvData;
            fireTableDataChanged();
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            SponsorHierarchyBean bean = (SponsorHierarchyBean)cvData.get(rowIndex);
            switch(columnIndex){
                case CODE:
                    return bean.getSponsorCode();
                case SPONSOR_NAME:
                    return bean.getSponsorName();
            }
            return EMPTY_STRING;
        }
    }
    
    
}
