/*
 * @(#)ColumnsController.java 1.0 10/17/07
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and
 * variables on 13-FEB-2008 by Leena
 */

package edu.mit.coeus.mapsrules.controller;

//import edu.mit.coeus.brokers.RequesterBean;
//import edu.mit.coeus.brokers.ResponderBean;
//import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.mapsrules.bean.BusinessRuleVariableBean;
import edu.mit.coeus.mapsrules.gui.ColumnsForm;
//import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.tree.TransferableUserRoleData;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceListener;
//import java.awt.event.ActionEvent;
//import java.awt.event.ComponentAdapter;
//import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
//import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
//import javax.swing.JLabel;
//import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 *
 * @author  vinayks
 */
public class ColumnsController extends RuleController implements DragSourceListener,DragGestureListener{
    
    /*Instance of the form
     */
    
    private ColumnsForm  columnsForm;
    /*Instance of the dialog window
     */
    private CoeusDlgWindow dlgColumns;
    
    private String EMPTY_STRING="";
    
    //For setting the window dimensions
    private int WIDTH=800;
    private int HEIGHT=200;
    
    //Index of the Table columns
    private static final int NAME_COLUMN =0;
    private static final int DESCRIPTION_COLUMN=1;
    
    //To create an instance of the MDI form
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    //Instance of the Table model
    private ColumnTableModel columnTableModel;
    
    //Instance of the CoeusVector which holds the form data
    private CoeusVector cvVariablesData;
    
    //Sorts the Table data in descending order
    private boolean sortCodeAsc = true;
    
    //private the Table data in ascending order
    private boolean sortDescAsc = false;
    
    //Instance of the DragSource
    DragSource dragSource;
    
    //Instance of Hand cursor
    Cursor dragCursor = new Cursor(Cursor.HAND_CURSOR);
    
    
    public ColumnsController(){
        registerComponents();
        formatFields();
    }
    
    //This method registers the Listeners and sets the tabel models
    public void registerComponents() {
        columnsForm = new ColumnsForm();
        //Added for case 2785 - Routing enhancements
        columnsForm.setPreferredSize(new Dimension(100,100));
        columnTableModel = new ColumnTableModel();
        
        dragSource = new DragSource();
        //Register the Componenet to be dragged
        dragSource.createDefaultDragGestureRecognizer(columnsForm.tblColumns, DnDConstants.ACTION_MOVE, this);
        
        columnsForm.tblColumns.getTableHeader().addMouseListener(new CustomMouseAdapter());
        columnsForm.tblColumns.setModel(columnTableModel);
        columnsForm.tblColumns.setRowSelectionAllowed(true);
    }
    
    //This method is used to format the components in the form based on some conditions
    public void formatFields() {
    }
    
    public java.awt.Component getControlledUI() {
        
        return columnsForm;
    }
    
    //This method sets the data to the form
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        cvVariablesData = new CoeusVector();
        cvVariablesData = (CoeusVector) data ;
        if(cvVariablesData!=null && cvVariablesData.size()>0){
            columnTableModel.setData(cvVariablesData);
            columnsForm.tblColumns.setRowSelectionInterval(0,0);
        }
        setTableEditors();
    }
    
    public Object getFormData() {
        return null;
    }
    
    //This method is used to display the form.
    public void display() {
        dlgColumns.setVisible(true);
    }
    
    //This methos is used for validations
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return false;
    }
    
    //This method creates and sets the display attributes for the dialog
    public void postInitComponents() {
        dlgColumns = new CoeusDlgWindow(mdiForm);
        dlgColumns.setResizable(false);
        dlgColumns.setModal(true);
        dlgColumns.getContentPane().add(columnsForm);
        dlgColumns.setFont(CoeusFontFactory.getLabelFont());
        dlgColumns.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgColumns.setSize(WIDTH,HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgColumns.getSize();
        dlgColumns.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        dlgColumns.addWindowListener(new WindowAdapter(){
            
            public void windowClosing(WindowEvent we) {
                performCancelAction();
            }
        });
        
        
    }
    public void performCancelAction(){
        dlgColumns.setVisible(false);
    }
    
    
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
    }
    
    
    private void setTableEditors(){
        
        JTableHeader tableHeader = columnsForm.tblColumns.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        
        columnsForm.tblColumns.setRowHeight(22);
        columnsForm.tblColumns.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        columnsForm.tblColumns.setSelectionMode(
                DefaultListSelectionModel.SINGLE_SELECTION);
        columnsForm.tblColumns.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
        columnsForm.tblColumns.setRowSelectionAllowed(true);
        columnsForm.tblColumns.setShowHorizontalLines(true);
        columnsForm.tblColumns.setShowVerticalLines(true);
        columnsForm.tblColumns.setOpaque(false);
        TableColumn columnDetails=columnsForm.tblColumns.getColumnModel().getColumn(NAME_COLUMN);
        columnDetails.setPreferredWidth(200);
        
        columnDetails=columnsForm.tblColumns.getColumnModel().getColumn(DESCRIPTION_COLUMN);
        //Modified for case 2785 - Routing enhancements - start
        //columnDetails.setPreferredWidth(400);
        columnDetails.setPreferredWidth(500);
        //Modified for case 2785 - Routing enhancements - end
    }
    
    
    public void dragDropEnd(java.awt.dnd.DragSourceDropEvent dsde) {
    }
    
    public void dragEnter(java.awt.dnd.DragSourceDragEvent dsde) {
    }
    
    public void dragExit(java.awt.dnd.DragSourceEvent dse) {
    }
    
    public void dragOver(java.awt.dnd.DragSourceDragEvent dsde) {
    }
    
    public void dropActionChanged(java.awt.dnd.DragSourceDragEvent dsde) {
    }
    
    //Method implemented for the drag event.
    public void dragGestureRecognized(java.awt.dnd.DragGestureEvent dragGestureEvent) {
        CoeusVector cvTranferData = new CoeusVector();
        int row = columnsForm.tblColumns.getSelectedRow();
        if(row!=-1){
            BusinessRuleVariableBean businessRuleVariableBean;
            if(cvVariablesData != null && cvVariablesData.size() > 0){
                businessRuleVariableBean = (BusinessRuleVariableBean)cvVariablesData.get(row);
                cvTranferData.addElement(businessRuleVariableBean);
            }
            
            TransferableUserRoleData transferableData = new TransferableUserRoleData(cvTranferData);
            dragSource.startDrag(dragGestureEvent, dragCursor,transferableData, this);
        }
    }
    
    public class CustomMouseAdapter extends MouseAdapter{
        public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
            BusinessRuleVariableBean bean =null;
            Point clickedPoint = mouseEvent.getPoint();
            int xPosition = (int)clickedPoint.getX();
            int selectedRow = columnsForm.tblColumns.getSelectedRow();
            int columnIndex = columnsForm.tblColumns.getColumnModel().getColumnIndexAtX(xPosition);
            if(selectedRow!= -1){
                bean = (BusinessRuleVariableBean)cvVariablesData.get(selectedRow);
            }
            switch(columnIndex){
                case NAME_COLUMN :
                    if(sortCodeAsc) {
                        cvVariablesData.sort("variableName",false);
                        sortCodeAsc = false;
                    }else{
                        cvVariablesData.sort("variableName",true);
                        sortCodeAsc = true;
                    }
                case DESCRIPTION_COLUMN:
                    if(sortDescAsc){
                        cvVariablesData.sort("variableDescription",false);
                        sortDescAsc = false;
                    }else{
                        cvVariablesData.sort("variableDescription",true);
                        sortDescAsc = true;
                    }
            }
            columnTableModel.fireTableDataChanged();
            if(selectedRow!= -1){
                columnsForm.tblColumns.setRowSelectionInterval(
                        cvVariablesData.indexOf(bean),cvVariablesData.indexOf(bean));
                
                columnsForm.tblColumns.scrollRectToVisible(
                        columnsForm.tblColumns.getCellRect(
                        cvVariablesData.indexOf(bean) ,0, true));
            }else{
                columnsForm.tblColumns.setRowSelectionInterval(0,0);
            }
        }
    }
    
    //Table Model for Rule Variables.
    public class ColumnTableModel extends AbstractTableModel{
        public String colNames[]={"Column name","Description"};
        public Class colClass[]={String.class,String.class};
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public String getColumnName(int col){
            return colNames[col];
        }
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        public int getRowCount() {
            if(cvVariablesData==null){
                return 0;
            }else{
                return cvVariablesData.size();
            }
            
        }
        
        public Object getValueAt(int row, int col) {
            BusinessRuleVariableBean bean = (BusinessRuleVariableBean)cvVariablesData.get(row);
            switch(col){
                case NAME_COLUMN :
                    return bean.getVariableName();
                case DESCRIPTION_COLUMN:
                    return bean.getVariableDescription();
            }
            return EMPTY_STRING;
        }
        
        private void setData(CoeusVector cvVariablesData){
            cvVariablesData = cvVariablesData;
        }
        
        
    }//End of Table Model
    
    public void cleanUp(){
        columnsForm = null;
        cvVariablesData = null;
        dlgColumns = null;
    }
}
