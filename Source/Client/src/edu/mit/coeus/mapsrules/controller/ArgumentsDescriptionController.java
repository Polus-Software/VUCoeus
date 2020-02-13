/*
 * ArgumentsInfoController.java
 *
 * Created on October 25, 2005, 6:58 PM
 */

package edu.mit.coeus.mapsrules.controller;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.mapsrules.bean.BusinessRuleFuncArgsBean;
import edu.mit.coeus.mapsrules.gui.ArgumentsDescriptionForm;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author  tarique
 */
public class ArgumentsDescriptionController extends RuleController implements ActionListener{
    
    /** Window title*/
    private static final String WINDOW_TITLE="Arguments Description";
    /** Width of the dialog*/
    private static final int WIDTH=535;
    /** Height of the dialog*/
    private static final int HEIGHT=315;
    private static final int ARGUMENT_NAME = 0;
    private static final int DESCRIPTION = 1;
    private static final int DEFAULT_VALUE = 2;
    private static final int WINDOW_NAME = 3;
    private static final String EMPTY_STRING = "";
    
    /**Dialog window */
    private CoeusDlgWindow dlgArgumentDesc;
    private ArgumentDescriptionTableModel argumentDescriptionTableModel;
    /**form instance of argument window */
    private ArgumentsDescriptionForm argumentsDescriptionForm;
    CoeusVector cvArgsVector;
    /** Creates the base window form object*/
    private edu.mit.coeus.gui.CoeusAppletMDIForm mdiForm
    = edu.mit.coeus.utils.CoeusGuiConstants.getMDIForm();
    /**Message resouces instance for Messages */
    private CoeusMessageResources coeusMessageResources;
    /** Creates a new instance of ArgumentsInfoController */
    public ArgumentsDescriptionController(BusinessRuleFuncArgsBean businessRuleFuncArgsBean) {
        cvArgsVector = new CoeusVector();
        cvArgsVector.add(businessRuleFuncArgsBean);
        try{
            postInitComponents();
            registerComponents();
            setFormData(null);
            setTableColumn();
        }catch(CoeusException coeusException) {
            CoeusOptionPane.showInfoDialog(coeusException.getMessage());
        }
    }
    
    /**
     * Method to display Argument window
     */
    public void display() {
        dlgArgumentDesc.setVisible(true);
    }
    
    /**
     * Method to setting components fields before window opening
     */
    public void formatFields() {
    }
    /**
     * Method to initialize the dialog
     * @throws CoeusException
     */
    
    public void postInitComponents() throws CoeusException{
        argumentsDescriptionForm=new ArgumentsDescriptionForm();
        dlgArgumentDesc = new edu.mit.coeus.gui.CoeusDlgWindow(mdiForm);
        dlgArgumentDesc.setResizable(false);
        dlgArgumentDesc.setModal(true);
        dlgArgumentDesc.setTitle(WINDOW_TITLE );
        argumentsDescriptionForm = new ArgumentsDescriptionForm();
        dlgArgumentDesc.getContentPane().add(argumentsDescriptionForm);
        dlgArgumentDesc.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        dlgArgumentDesc.setSize(WIDTH, HEIGHT);
        
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dlgSize = dlgArgumentDesc.getSize();
        dlgArgumentDesc.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        dlgArgumentDesc.setDefaultCloseOperation(edu.mit.coeus.gui.
        CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgArgumentDesc.addComponentListener(
        new java.awt.event.ComponentAdapter(){
            public void componentShown(java.awt.event.ComponentEvent e){
                requestDefaultFocus();
            }
        });
        dlgArgumentDesc.addWindowListener(new java.awt.event.WindowAdapter(){
            public void windowClosing(java.awt.event.WindowEvent we){
                try{
                    performOkAction();
                }catch (CoeusException exception){
                    exception.printStackTrace();
                    CoeusOptionPane.showErrorDialog(exception.getMessage());
                }
            }
        });
        
        
        dlgArgumentDesc.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                try {
                    performOkAction();
                    return;
                }catch(CoeusException exception) {
                    exception.printStackTrace();
                    CoeusOptionPane.showErrorDialog(exception.getMessage());
                }
            }
        });
    }
    
    /**
     *Method to focus on window open
     */
    private void requestDefaultFocus(){
        argumentsDescriptionForm.scrPnArgumentInfo.requestFocusInWindow();
    }
    
    private void setTableColumn() {
        JTableHeader tableHeader = argumentsDescriptionForm.tblArgumentInfo.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        argumentsDescriptionForm.tblArgumentInfo.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        argumentsDescriptionForm.tblArgumentInfo.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        argumentsDescriptionForm.tblArgumentInfo.setCellSelectionEnabled(true);
        argumentsDescriptionForm.tblArgumentInfo.setRowSelectionAllowed(false);
        argumentsDescriptionForm.tblArgumentInfo.setRowHeight(22);
        
        TableColumn column = argumentsDescriptionForm.tblArgumentInfo.getColumnModel().getColumn(ARGUMENT_NAME);
        column.setPreferredWidth(175);
        column.setResizable(true);
        
        column = argumentsDescriptionForm.tblArgumentInfo.getColumnModel().getColumn(DESCRIPTION);
        column.setPreferredWidth(220);
        column.setResizable(true);
        
        column = argumentsDescriptionForm.tblArgumentInfo.getColumnModel().getColumn(DEFAULT_VALUE);
        column.setPreferredWidth(90);
        column.setResizable(true);
        
        java.awt.Color bgColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        argumentsDescriptionForm.tblArgumentInfo.setBackground(bgColor);
        
    }
    
    /** Set the Editor and renderer for Table */
    private void setTableEditors() {
        try{
            return;
            
        }catch(Exception ce){
            CoeusOptionPane.showErrorDialog(ce.getMessage());
            ce.printStackTrace();
        }
        
    }
    
    /**
     * Method which return form instance
     * @return Component form window object
     */
    public java.awt.Component getControlledUI() {
        return argumentsDescriptionForm;
    }
    
    /**
     * Method to get Form data
     * @return Object
     */
    public Object getFormData() {
        return null;
    }
    
    /**
     * Method to register components
     */
    public void registerComponents() {
        argumentsDescriptionForm.btnOk.addActionListener(this);
        argumentDescriptionTableModel = new ArgumentDescriptionTableModel();
        argumentsDescriptionForm.tblArgumentInfo.setModel(argumentDescriptionTableModel);
    }
    
    /**
     * Method to save form data
     * @throws CoeusException if Exception occurs
     */
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
    }
    
    /**
     * Method to set Form data
     * @param data Object
     * @throws CoeusException throws exception
     */
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        BusinessRuleFuncArgsBean businessRuleFuncArgsBean = (BusinessRuleFuncArgsBean)cvArgsVector.get(0);
        argumentsDescriptionForm.lblFunctionType.setText(businessRuleFuncArgsBean.getFunctionName().toUpperCase());
        argumentDescriptionTableModel.setData(cvArgsVector);
    }
    
    /**
     * Method to validate form data
     * @throws CoeusUIException throws UI Exception
     * @return boolean true else false
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return true;
    }
    
    /**
     *Method to perform Ok button action
     */
    private void performOkAction() throws CoeusException{
        dlgArgumentDesc.dispose();
        cleanUp();
    }
    
    /**
     *Actions Events on buttons
     */
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Object source=e.getSource();
        try{
            if(source.equals(argumentsDescriptionForm.btnOk)){
                performOkAction();
            }
        }catch(CoeusException ce){
            ce.printStackTrace();
            CoeusOptionPane.showErrorDialog(ce.getMessage());
        }
    }
    
    private class ArgumentDescriptionTableModel extends AbstractTableModel {
        
        String colNames[] = {"Argument Name","Description","Default Value"};
        Class[] colTypes = new Class[] {String.class,String.class,String.class};
        
        ArgumentDescriptionTableModel() {
            
        }
        
        public boolean isCellEditable() {
            return false;
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public Class getColumnClass(int columnIndex) {
            return colTypes[columnIndex];
        }
        
        public void setData(CoeusVector cvData){
            cvArgsVector = cvData;
            fireTableDataChanged();
        }
        
        public int getRowCount() {
            if(cvArgsVector != null)
                return cvArgsVector.size();
            else
                return 0;
        }
        
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        public Object getValueAt(int row,int column) {
            BusinessRuleFuncArgsBean businessRuleFuncArgsBean =  (BusinessRuleFuncArgsBean)cvArgsVector.get(row);
            switch(column) {
                case ARGUMENT_NAME:
                    String argName=(businessRuleFuncArgsBean.getArgumentName()== null ?EMPTY_STRING:businessRuleFuncArgsBean.getArgumentName());
                    return argName;
                case DESCRIPTION:
                    String description = (businessRuleFuncArgsBean.getRuleFuncDescription()== null ?EMPTY_STRING:businessRuleFuncArgsBean.getRuleFuncDescription());
                    return description;
                case DEFAULT_VALUE:
                    String defaultVal = (businessRuleFuncArgsBean.getDefaultValue()== null ?EMPTY_STRING:businessRuleFuncArgsBean.getDefaultValue());
                    return defaultVal;
            }
            return EMPTY_STRING;
            
        }
        
        public void setValueAt(Object value,int row,int column) {
            if(cvArgsVector == null)
                return;
            BusinessRuleFuncArgsBean businessRuleFuncArgsBean = (BusinessRuleFuncArgsBean)cvArgsVector.get(row);
            switch(column) {
                case ARGUMENT_NAME:
                    if(value==null || value.toString().equals(EMPTY_STRING)) {
                        return ;
                    }else {
                        businessRuleFuncArgsBean.setArgumentName(value.toString());
                    }
                    break;
                case DESCRIPTION:
                    if(value==null || value.toString().equals(EMPTY_STRING)) {
                        return ;
                    }else {
                        businessRuleFuncArgsBean.setRuleFuncDescription(value.toString());
                    }
                    break;
                case DEFAULT_VALUE:
                    if(value==null || value.toString().equals(EMPTY_STRING)) {
                        return ;
                    }else {
                        businessRuleFuncArgsBean.setDefaultValue(value.toString());
                    }
                    break;
            }
        }
        
    }
    
    
    
    public void cleanUp() {
        cvArgsVector = null;
        argumentsDescriptionForm.btnOk.removeActionListener(this);
        argumentDescriptionTableModel =null;
        argumentsDescriptionForm=null;
        dlgArgumentDesc=null;
        mdiForm=null;
    }
}

