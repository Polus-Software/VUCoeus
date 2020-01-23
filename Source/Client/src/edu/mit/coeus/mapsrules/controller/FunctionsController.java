/*
 * FunctionsController.java
 *
 * Created on October 17, 2005, 12:00 PM
 */

/* PMD check performed, and commented unused imports and variables on 12-JULY-2010
 * by Satheesh Kumar
 */

package edu.mit.coeus.mapsrules.controller;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.mapsrules.bean.BusinessRuleFunctionBean;
import edu.mit.coeus.mapsrules.gui.FunctionsForm;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.tree.TransferableUserRoleData;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 *
 * @author  vinayks
 */
public class FunctionsController extends RuleController implements ListSelectionListener, 
DragSourceListener,DragGestureListener{
            
    //Instance of the Form being controlled
    private FunctionsForm functionsForm ;
    
    //Instance of the table model
    private FunctionsTableModel functionsTableModel ;
    
    //Instance of the Dialog Window
    private CoeusDlgWindow dlgFunctions;
    
    //Dimesions to the Form
    private int WIDTH = 100;
    private int HEIGHT =200;
    
    //Instance of the MDI form
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    //Index of the Table columns
    private static final int NAME_COLUMN = 0;
    
    private String EMPTY_STRING ="";
    
    // Instance of the CoeusVector Which holds the form data
    private CoeusVector cvFunctionName;
    private CoeusVector cvFunctionDesc;
    
    //Instance of the DragSource
    DragSource dragSource;
    
    //Instance of the Hand Cursor
    Cursor dragCursor = new Cursor(Cursor.HAND_CURSOR);
    
    // Added for IACUC Business rule implementation - Start
    private static final int FUNCTION_NAME_COLUMN_WIDTH = 320;
    // Added for IACUC Business rule implementation - End
            
    /** Creates a new instance of FunctionsController */
    public FunctionsController() {
        registerComponents();
        formatFields();
    }
    
    //This method Registers the listeners and table models to the components
    public void registerComponents() {
        functionsForm = new FunctionsForm();
        functionsTableModel = new FunctionsTableModel();
        
        dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(functionsForm.tblFunctionName, DnDConstants.ACTION_MOVE, this);
        
        functionsForm.tblFunctionName.setModel(functionsTableModel);
        ListSelectionModel selectionModel = functionsForm.tblFunctionName.getSelectionModel();
        selectionModel.addListSelectionListener(this);
        functionsForm.tblFunctionName.setSelectionModel(selectionModel);
    }
    
    //This method is to for enable/disable the components based on the functionType
    public void formatFields() {
    }
    
    public java.awt.Component getControlledUI() {
        return functionsForm ;
        
    }
    
    //This methods sets the form data
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        CoeusVector cvData = (CoeusVector)data;

        cvFunctionName = new CoeusVector();
        
        cvFunctionName = (CoeusVector)cvData.get(0);
        cvFunctionDesc = (CoeusVector)cvData.get(1);
       
        if(cvFunctionName != null && cvFunctionName.size()>0){            
            functionsTableModel.setData(cvFunctionName);
            functionsForm.tblFunctionName.setRowSelectionInterval(0,0);
        }//End of outer if

        setTableEditors();
    }
    
    public Object getFormData() {
        return null;
    }
    
    //Method used to display the form
    public void display() {
        dlgFunctions.setVisible(true);
        
    }
    
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
    }
    
    //Method used for validations,returns a boolean true or false
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return false;
    }
    
    //This method creates and sets the display attributes for the dialog
    public void postInitComponents() {
        dlgFunctions = new CoeusDlgWindow(mdiForm);
        dlgFunctions.setResizable(false);
        dlgFunctions.setModal(true);
        dlgFunctions.getContentPane().add(functionsForm);
        dlgFunctions.setFont(CoeusFontFactory.getLabelFont());
        dlgFunctions.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgFunctions.setSize(WIDTH,HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgFunctions.getSize();
        dlgFunctions.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        dlgFunctions.addWindowListener(new WindowAdapter(){
            
            public void windowClosing(WindowEvent we) {
                performCancelAction();
            }
        });
        
        
    }
    
    public void performCancelAction(){
        dlgFunctions.setVisible(false);
    }
    
    private void setTableEditors(){
        JTableHeader tableHeader = functionsForm.tblFunctionName.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        functionsForm.tblFunctionName.setRowHeight(22);
        functionsForm.tblFunctionName.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        functionsForm.tblFunctionName.setShowHorizontalLines(true);
        functionsForm.tblFunctionName.setShowVerticalLines(true);
        functionsForm.tblFunctionName.setOpaque(false);
        functionsForm.tblFunctionName.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        functionsForm.tblFunctionName.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
        functionsForm.tblFunctionName.setRowSelectionAllowed(true);
        functionsForm.txtArFunctionDescription.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
        functionsForm.txtArFunctionDescription.setEditable(false);
        
        TableColumn columnDetails=functionsForm.tblFunctionName.getColumnModel().getColumn(NAME_COLUMN);
        // Modified for IACUC Business rule implementation - Start
//        columnDetails.setPreferredWidth(250);
        columnDetails.setPreferredWidth(FUNCTION_NAME_COLUMN_WIDTH);
        // Modified for IACUC Business rule implementation - Start
    }
    
    public void valueChanged(javax.swing.event.ListSelectionEvent e) {
        int row = functionsForm.tblFunctionName.getSelectedRow();        
        if(row != -1){
            BusinessRuleFunctionBean bean = (BusinessRuleFunctionBean)cvFunctionName.get(row);      
            Equals eqFuncName = new Equals("functionName" , bean.getFunctionName());
            CoeusVector cvFilteredVec = cvFunctionDesc.filter(eqFuncName);
            if(cvFilteredVec != null && cvFilteredVec.size()>0){
                BusinessRuleFunctionBean ruleFunctionBean = 
                                (BusinessRuleFunctionBean)cvFilteredVec.get(0);
                functionsForm.txtArFunctionDescription.setText(ruleFunctionBean.getDescription());
            }//End of if
            
        }//End of oouter if
    }
    
    public void dragDropEnd(java.awt.dnd.DragSourceDropEvent dsde) {
    }
    
    public void dragEnter(java.awt.dnd.DragSourceDragEvent dsde) {
    }
    
    public void dragExit(java.awt.dnd.DragSourceEvent dse) {
    }
    
    //Method implemented for drag event
    public void dragGestureRecognized(java.awt.dnd.DragGestureEvent dragGestureEvent) {
        CoeusVector cvTranferData = new CoeusVector();
        int row = functionsForm.tblFunctionName.getSelectedRow();
        if(row != -1){
            BusinessRuleFunctionBean functionBean;
            if(cvFunctionName != null && cvFunctionName.size() > 0){
                functionBean = (BusinessRuleFunctionBean)cvFunctionName.get(row);
                cvTranferData.addElement(functionBean);
            }
            
            TransferableUserRoleData transferableData = new TransferableUserRoleData(cvTranferData);
            dragSource.startDrag(dragGestureEvent, dragCursor,transferableData, this);
        }
    }
    
    public void dragOver(java.awt.dnd.DragSourceDragEvent dsde) {
    }
    
    public void dropActionChanged(java.awt.dnd.DragSourceDragEvent dsde) {
    }
    
    //Table model for the Function Name
    public class FunctionsTableModel extends AbstractTableModel{
        private String []colNames ={"Function Name"};
        private Class []colClass ={String.class};
        
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
            if(cvFunctionName == null){
                return 0;
            }else{
                return cvFunctionName.size();
            }
        }
        
        public Object getValueAt(int row, int col) {
            BusinessRuleFunctionBean bean =(BusinessRuleFunctionBean)cvFunctionName.get(row);
            switch(col){
                case NAME_COLUMN :
                    return bean.getFunctionName();
            }
            return EMPTY_STRING;
        }
        
        private void setData(CoeusVector cvFunctionName){
            cvFunctionName = cvFunctionName;
        }
    }//End of Table Model..
    
    public void cleanUp(){
        functionsForm = null;
        cvFunctionName = null;
        cvFunctionDesc = null;        
    }
}
