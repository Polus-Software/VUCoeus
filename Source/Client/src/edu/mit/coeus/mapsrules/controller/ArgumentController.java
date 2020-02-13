/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.mapsrules.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.mapsrules.bean.BusinessRuleExpBean;
import edu.mit.coeus.mapsrules.bean.BusinessRuleFuncArgsBean;
import edu.mit.coeus.mapsrules.bean.RuleBaseBean;
import edu.mit.coeus.mapsrules.gui.ArgumentsForm;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusLabel;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.NotEquals;
import edu.mit.coeus.utils.query.Or;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 * Class for arguments
 * @author tarique
 */
public class ArgumentController extends RuleController implements ActionListener
{
    
    /** Argument Column*/
    private static final int ARGUMENT_NAME_COL = 0;
    /**Value Column */
    private static final int VALUE_COL = 1;
    /** Description Column*/
    private static final int DESCRIPTION_COL = 2;
    /** Window title*/
    private static final String WINDOW_TITLE = "Arguments";
    /** Width of the dialog*/
    private static final int WIDTH = 500;
    /** Height of the dialog*/
    private static final int HEIGHT = 200;
    /**Dialog window */
    private CoeusDlgWindow dlgArgument;
    /**form instance of argument window */
    private ArgumentsForm argumentsForm;
    /**Table model for table argument */
    private ArgumentTableModel argumentTableModel;
    /** table editor for argument table*/
    private ArgumentEditor argumentEditor;
    /**Renderer for Table Argument */
    private ArgumentCellRenderer argumentCellRenderer;
    /** Creates the base window form object*/
    private edu.mit.coeus.gui.CoeusAppletMDIForm mdiForm 
                             =  edu.mit.coeus.utils.CoeusGuiConstants.getMDIForm();
    /**Message resouces instance for Messages */
    private CoeusMessageResources coeusMessageResources;
    /** business bean*/
    BusinessRuleFuncArgsBean businessRuleFuncArgsBean;
    /**EMPTY String */
    private static final String EMPTY_STRING = "";
    /** constant variable for date*/
    private static final String DATE = "DATE";
    /** constant variable for checking argument type*/
    private static final String  NUMBER = "NUMBER";
    /**Date separators for checking */
    private static final String DATE_SEPARATERS = ":/.,|-";
    /** Vector contains bean*/
    private CoeusVector cvData;
    //Added for case id 3351 - Adding more arguements to rule function- start
    /** Vector containing the original bean*/
    private CoeusVector cvMasterData;
    //Added for case id 3351 - Adding more arguements to rule function- start
    /**char for making server call for argument information */
    private static final char GET_ARG_INFO = 'I';
    /** servlet to get the data */
    private static final String RULE_SERVLET = "/RuleMaintenanceServlet";
    /** Rule Base Bean*/
    private RuleBaseBean ruleBaseBean;
    /**Variable to check ok Pressed */
    private boolean okPressed = false;
    
    
    /**
     * Creates a new instance of ArgumentController
     * @param ruleBaseBean object of RuleBase Bean
     * @param functionType object of actype
     * @throws CoeusException if Exception occurs
     */
    
    public ArgumentController(RuleBaseBean ruleBaseBean
                                    , char functionType) throws CoeusException{
        this.ruleBaseBean = ruleBaseBean;
        coeusMessageResources  =  CoeusMessageResources.getInstance();
        setFunctionType(functionType);
        registerComponents();
    }
    
    /**
     * Method to display Argument window
     */    
    public void display() {
        dlgArgument.setVisible(true);
    }
    
    /** Perform field formatting.
     * enabling, disabling components depending on the different conditions
     */   
    public void formatFields() {
    }
    /**
     * Method to initialize the dialog
     * @throws CoeusException if exception occurs
     */
    public void postInitComponents() throws CoeusException{
        dlgArgument  =  new edu.mit.coeus.gui.CoeusDlgWindow(mdiForm);
        dlgArgument.setResizable(false);
        dlgArgument.setModal(true);
        dlgArgument.setTitle(WINDOW_TITLE );
        dlgArgument.getContentPane().add(argumentsForm);
        dlgArgument.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        dlgArgument.setSize(WIDTH, HEIGHT);

        dlgArgument.setLocationRelativeTo(argumentsForm);
        dlgArgument.setDefaultCloseOperation(edu.mit.coeus.gui.
                                                CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgArgument.addComponentListener(
        new java.awt.event.ComponentAdapter(){
            public void componentShown(java.awt.event.ComponentEvent e){
                requestDefaultFocus();
            }
        });
        dlgArgument.addWindowListener(new java.awt.event.WindowAdapter(){
            public void windowClosing(java.awt.event.WindowEvent we){
                try{
                    performCancelAction();
                }catch (CoeusException exception){
                    exception.printStackTrace();
                    CoeusOptionPane.showErrorDialog(exception.getMessage());
                }
            }
        });
        
        dlgArgument.addEscapeKeyListener(new javax.swing.AbstractAction("escPressed"){
            public void actionPerformed(java.awt.event.ActionEvent ae){
                try{
                    performCancelAction();
                }catch (CoeusException exception){
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
        argumentsForm.btnMore.requestFocusInWindow();
        if(argumentsForm.tblArgument.getRowCount() > 0){
            argumentsForm.tblArgument.setRowSelectionInterval(0,0);
        }
        
    }
    /** Set the Editor and renderer for Table */
    private void setTableEditors() {
        try{
            JTableHeader tableHeader = argumentsForm.tblArgument.getTableHeader();
            tableHeader.setReorderingAllowed(false);
            tableHeader.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
            argumentsForm.tblArgument.setRowHeight(22);
            argumentsForm.tblArgument.setShowHorizontalLines(true);
            argumentsForm.tblArgument.setShowVerticalLines(true);
            argumentsForm.tblArgument.setOpaque(false);
            argumentsForm.tblArgument.setSelectionMode(
                                    DefaultListSelectionModel.SINGLE_SELECTION);
            argumentsForm.tblArgument.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            TableColumn columnArgument;
            int size[] = {150,80,150};
            for(int index = 0;index < size.length;index++){
                columnArgument = argumentsForm.tblArgument.getColumnModel().getColumn(index);
                columnArgument.setPreferredWidth(size[index]);
                columnArgument.setCellEditor(argumentEditor);
                columnArgument.setCellRenderer(argumentCellRenderer);
            }
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
        return argumentsForm;
    }
    
    /**
     * Method to get Form data
     * @return Object
     */    
    public Object getFormData() {
        //Modifed for case id 3351 'Adding more arguements to rule function'- start
//        if(cvData != null && cvData.size() > 0){
//            BusinessRuleFuncArgsBean businessRuleFuncArgsBean
//                = (BusinessRuleFuncArgsBean)cvData.get(0);
//            return businessRuleFuncArgsBean;
//        }
//        return null;
        if(okPressed){
            return cvData;
        }else {
            return cvMasterData;
        }
        //Modifed for case id 3351 'Adding more arguements to rule function'- end
        
    }
    
    /**
     * Method to register components
     */    
    public void registerComponents() {
       cvData = new CoeusVector();
       argumentsForm = new ArgumentsForm();
       argumentTableModel=new ArgumentTableModel();
       argumentEditor=new ArgumentEditor();
       argumentCellRenderer=new ArgumentCellRenderer();
       argumentsForm.tblArgument.setModel(argumentTableModel);
       
       argumentsForm.tblArgument.addMouseListener(new MouseAdapter()
       {
           public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
               try{
                   if(mouseEvent.getClickCount() != 2){
                       return;
                   }
                   argumentEditor.stopCellEditing();
                   int selRow;
                   if(mouseEvent.getSource().equals(argumentsForm.tblArgument)) {
                       selRow = argumentsForm.tblArgument.rowAtPoint(mouseEvent.getPoint());
                   }else {
                       selRow = argumentsForm.tblArgument.getSelectedRow();
                   }
                   
                   BusinessRuleFuncArgsBean bean = (BusinessRuleFuncArgsBean)cvData.get(selRow);
                   if(bean.getWindowName() == null || bean.getWindowName().equals(EMPTY_STRING)){
                       return;
                   }
                   if(selRow != -1){
                       argumentsForm.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
                       BusinessRuleFuncArgsBean functionBean = (BusinessRuleFuncArgsBean)cvData.get(selRow);
                       LookUpFormController lookUpFormController = new LookUpFormController(mdiForm,functionBean);
                       lookUpFormController.setLookUpData(null);
                       BusinessRuleFuncArgsBean resultantBean = (BusinessRuleFuncArgsBean) lookUpFormController.getFormData();
                       //Added For COEUSQA-2308 Java 1.6 Issues start
                       argumentsForm.tblArgument.repaint();                       
                       //Added For COEUSQA-2308 Java 1.6 Issues end
                   }
               }catch (Exception e){
                   e.printStackTrace();
                   CoeusOptionPane.showErrorDialog(e.getMessage());
               }finally{
                   argumentsForm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
               }
           }
       }
       );
       argumentsForm.btnOk.addActionListener(this);
       argumentsForm.btnCancel.addActionListener(this);
       argumentsForm.btnMore.addActionListener(this);
       
       java.awt.Component[] components  =  {
            argumentsForm.btnMore, argumentsForm.btnOk,argumentsForm.btnCancel,
                argumentsForm.scrPnArgument};
        ScreenFocusTraversalPolicy traversePolicy  =  new ScreenFocusTraversalPolicy( components );
        argumentsForm.setFocusTraversalPolicy(traversePolicy);
        argumentsForm.setFocusCycleRoot(true);
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
        
        HashMap hmMainData = new HashMap();
        hmMainData = (HashMap)data;
        //Modified for case id:3351 'Adding more arguements to rule function' - start
        //cvData = (CoeusVector)hmMainData.get(BusinessRuleFuncArgsBean.class);
        CoeusVector cvAllData = (CoeusVector)hmMainData.get(BusinessRuleFuncArgsBean.class);
        //Modified for case id:3351 'Adding more arguements to rule function' - end
        BusinessRuleExpBean businessRuleExpBean 
                =(BusinessRuleExpBean)hmMainData.get(BusinessRuleExpBean.class);
        if(getFunctionType() == TypeConstants.NEW_MODE){
            String functionName = businessRuleExpBean.getLvalue();
            cvData = getArgumentsInformation(functionName);
            //Modified for case id:3351 'Adding more arguements to rule function' - start
            cvMasterData = new CoeusVector();
            BusinessRuleFuncArgsBean tempRuleFuncArgsBean = null;
            BusinessRuleFuncArgsBean newRuleFuncArgsBean = null;
            for(int i=0; i<cvData.size();i++){
                tempRuleFuncArgsBean = (BusinessRuleFuncArgsBean)cvData.get(i);
                newRuleFuncArgsBean = new BusinessRuleFuncArgsBean();
                newRuleFuncArgsBean.setArgumentName(tempRuleFuncArgsBean.getArgumentName());
                newRuleFuncArgsBean.setArgumentType(tempRuleFuncArgsBean.getArgumentType());
                newRuleFuncArgsBean.setDefaultValue(tempRuleFuncArgsBean.getDefaultValue());
                newRuleFuncArgsBean.setFunctionName(tempRuleFuncArgsBean.getFunctionName());
                newRuleFuncArgsBean.setRuleExpDescription(tempRuleFuncArgsBean.getRuleExpDescription());
                newRuleFuncArgsBean.setRuleFuncDescription(tempRuleFuncArgsBean.getRuleFuncDescription());
                newRuleFuncArgsBean.setSequenceNumber(tempRuleFuncArgsBean.getSequenceNumber());
                newRuleFuncArgsBean.setValue(tempRuleFuncArgsBean.getValue());
                newRuleFuncArgsBean.setWindowName(tempRuleFuncArgsBean.getWindowName());
                newRuleFuncArgsBean.setExpressionNumber(tempRuleFuncArgsBean.getExpressionNumber());
                newRuleFuncArgsBean.setAcType(tempRuleFuncArgsBean.getAcType());
                cvMasterData.add(newRuleFuncArgsBean);
            }
//            BusinessRuleFuncArgsBean businessRuleFuncArgsBean
//                    = (BusinessRuleFuncArgsBean)cvData.get(0);
            if(cvData!=null){
                BusinessRuleFuncArgsBean businessRuleFuncArgsBean = null;
                for(int i = 0; i< cvData.size(); i++){
                    businessRuleFuncArgsBean = (BusinessRuleFuncArgsBean)cvData.get(i);
                    businessRuleFuncArgsBean.setAcType(TypeConstants.INSERT_RECORD);
                }
            }
            //Modified for case id:3351 'Adding more arguements to rule function' - end
        }else if(getFunctionType() == TypeConstants.MODIFY_MODE){
            int number;
            number = businessRuleExpBean.getConditionNumber();
            Equals eqCondNo = new Equals("conditionNumber",new Integer(number));
            number = businessRuleExpBean.getExpressionNumber();
            Equals eqExpNo = new Equals("expressionNumber",new Integer(number));
            //Modified for case id:3351 'Adding more arguements to rule function' - start
//            And andCond = new And(eqCondNo , eqExpNo);
//            cvData = cvData.filter(andCond);
            Equals eqAcTypeNull = new Equals("acType", null);
            NotEquals notEquals = new NotEquals("acType", TypeConstants.DELETE_RECORD);
            And andCond = new And((new And(eqCondNo, eqExpNo)) , (new Or(eqAcTypeNull, notEquals)));
            cvMasterData = cvAllData.filter(andCond);
            if(cvMasterData!=null){
                BusinessRuleFuncArgsBean tempRuleFuncArgsBean = null;
                BusinessRuleFuncArgsBean newRuleFuncArgsBean = null;
                for(int i=0; i<cvMasterData.size();i++){
                    tempRuleFuncArgsBean = (BusinessRuleFuncArgsBean)cvMasterData.get(i);
                    newRuleFuncArgsBean = new BusinessRuleFuncArgsBean();
                    newRuleFuncArgsBean.setArgumentName(tempRuleFuncArgsBean.getArgumentName());
                    newRuleFuncArgsBean.setArgumentType(tempRuleFuncArgsBean.getArgumentType());
                    newRuleFuncArgsBean.setDefaultValue(tempRuleFuncArgsBean.getDefaultValue());
                    newRuleFuncArgsBean.setFunctionName(tempRuleFuncArgsBean.getFunctionName());
                    newRuleFuncArgsBean.setRuleExpDescription(tempRuleFuncArgsBean.getRuleExpDescription());
                    newRuleFuncArgsBean.setRuleFuncDescription(tempRuleFuncArgsBean.getRuleFuncDescription());
                    newRuleFuncArgsBean.setSequenceNumber(tempRuleFuncArgsBean.getSequenceNumber());
                    newRuleFuncArgsBean.setValue(tempRuleFuncArgsBean.getValue());
                    newRuleFuncArgsBean.setWindowName(tempRuleFuncArgsBean.getWindowName());
                    newRuleFuncArgsBean.setExpressionNumber(tempRuleFuncArgsBean.getExpressionNumber());
                    newRuleFuncArgsBean.setAcType(tempRuleFuncArgsBean.getAcType());
                    cvData.add(newRuleFuncArgsBean);
                }
            }
            //Modified for case id:3351 'Adding more arguements to rule function' - end
        }
        if(cvData != null && cvData.size() > 0){
            //Modified for case id: 3351 'Adding more arguements to rule function'- start
//            BusinessRuleFuncArgsBean businessRuleFuncArgsBean
//                                = (BusinessRuleFuncArgsBean)cvData.get(0);
            BusinessRuleFuncArgsBean businessRuleFuncArgsBean = null;
            for(int i = 0; i< cvData.size(); i++){
                businessRuleFuncArgsBean = (BusinessRuleFuncArgsBean)cvData.get(i);
                if(businessRuleFuncArgsBean.getValue() == null){
                    businessRuleFuncArgsBean.setValue(businessRuleFuncArgsBean.getDefaultValue());
                }
            }
            //Modified for case id: 3351 'Adding more arguements to rule function'- end
        }
        argumentTableModel.setData(cvData);
        setTableEditors();
        postInitComponents();
        setTableKeyTraversal();
    }
    /**
     * Method to get Argument Information data
     * @param functionName Object of Function Name
     * @throws CoeusException If exception occurs
     * @return Object of CoeusVector
     */    
    public CoeusVector getArgumentsInformation(String functionName) throws CoeusException{
        CoeusVector cvData = new CoeusVector();
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        request.setFunctionType(GET_ARG_INFO);
        request.setDataObject(functionName);
        
        AppletServletCommunicator comm = new AppletServletCommunicator(
                        CoeusGuiConstants.CONNECTION_URL+RULE_SERVLET, request);
        comm.send();
        response = comm.getResponse();
        if(response!=null){
            if(response.isSuccessfulResponse()){
                cvData = (CoeusVector)response.getDataObject();
            }else{
                throw new CoeusException(response.getMessage(),0);
            }
        }
        return cvData;
    }
    
    /**
     * Method to validate form data
     * @throws CoeusUIException throws UI Exception
     * @return boolean true else false
     */    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        if(cvData != null && cvData.size() > 0){
            //Modified for the Case id: 3351 'Adding more arguements to rule function' - start
            //Validates all the beans in the vector
//            BusinessRuleFuncArgsBean businessRuleFuncArgsBean 
//                        =(BusinessRuleFuncArgsBean)cvData.get(0);
//            if(businessRuleFuncArgsBean == null){
//                return true;
//            }else{
            for(int i=0; i<cvData.size(); i++){
                BusinessRuleFuncArgsBean businessRuleFuncArgsBean 
                            =(BusinessRuleFuncArgsBean)cvData.get(i);
                if(businessRuleFuncArgsBean != null){
//                String argType = argumentsForm.tblArgument.getValueAt(0 , 1).toString().trim();
                    String argType = argumentsForm.tblArgument.getValueAt(i , 1).toString().trim();
                    String argName = businessRuleFuncArgsBean.getArgumentName();
                    if(argType== null){
                        argType = EMPTY_STRING;
                    }
                    if(argType.equals(EMPTY_STRING)){
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                        "rulesSelection_exceptionCode.1004"));
                        argumentsForm.tblArgument.setRowSelectionInterval(0,0);
                        return false;
                    }
                    if(businessRuleFuncArgsBean.getArgumentType().toUpperCase().equals(NUMBER)){
                        try{
                            //using only for checking number
                            int intArguType = Integer.parseInt(argType);
                        }catch(NumberFormatException ne){
                            CoeusOptionPane.showInfoDialog("Argument '"+argName+"'" +
                                                " must be a number.");
                            return false;
                        }
                    }else if(businessRuleFuncArgsBean.getArgumentType().toUpperCase().equals(DATE)){

                            DateUtils dateUtils = new DateUtils();
                            boolean dateCheck = dateUtils.validateDate(argType,DATE_SEPARATERS);
                            if(dateCheck){
                                return true;
                            }else{
                                CoeusOptionPane.showInfoDialog("Argument '"+argName+"'" +
                                                            " must be a date.");
                                return false;
                            }
                     }
                }
            }
            //Modified for the Case id: 3351 'Adding more arguements to rule function'- end
        }
        return true;
    }
    /**
     *Method to perform Cancel button action
     */
    private void performCancelAction() throws CoeusException{
        dlgArgument.dispose();
        
    }
    /**
     *Method to perform Ok button action
     */
    private void performOkAction() throws CoeusException,CoeusUIException{
        argumentEditor.stopCellEditing();
            if(validate()){
                setOkPressed(true);
                dlgArgument.dispose();
            }
        //Commented for case id 3351 'Adding more arguements to rule function'- start
//            else{
//                if(argumentsForm.tblArgument.getRowCount() > 0){
//                    argumentsForm.tblArgument.setRowSelectionInterval(0, 0);
//                }
//            }
        //Commented for case id 3351 'Adding more arguements to rule function'- end
    }
    /**
     *Method to perform More button action
     */
    private void performMoreAction() throws CoeusException{
        try{
            //Modified for case id:3351 'Adding more arguements to rule function' - start
            if(argumentsForm.tblArgument.getSelectedRow() == -1){
                CoeusOptionPane.showInfoDialog("Please Select a row");
                return ;
            }else{
                argumentsForm.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
//                BusinessRuleFuncArgsBean businessRuleFuncArgsBean
//                    = (BusinessRuleFuncArgsBean)cvData.get(0);
                BusinessRuleFuncArgsBean businessRuleFuncArgsBean
                = (BusinessRuleFuncArgsBean)cvData.get(argumentsForm.tblArgument.getSelectedRow());
                ArgumentsDescriptionController argumentsDescriptionController
                            =new ArgumentsDescriptionController(businessRuleFuncArgsBean);
                argumentsDescriptionController.display();
                argumentsDescriptionController = null;
            }
            //Modified for case id:3351 'Adding more arguements to rule function'- end
        }finally{
            argumentsForm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }
    /**
     * Actions Events on buttons
     * @param e object of ActionEvent
     */
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Object source = e.getSource();
        try{
            if(source.equals(argumentsForm.btnOk)){
                performOkAction();
                
            }else if(source.equals(argumentsForm.btnMore)){
                performMoreAction();
                
            }else if(source.equals(argumentsForm.btnCancel)){
                performCancelAction();
                
            }
        }catch(CoeusException ce){
            ce.printStackTrace();
            CoeusOptionPane.showErrorDialog(ce.getMessage());
        }catch (CoeusUIException coeusUIException){
            CoeusOptionPane.showDialog(coeusUIException);
        }
    }
    /**Method to clean up the components */
    public void cleanUp(){
        argumentsForm.btnMore.removeActionListener(this);
        argumentsForm.btnCancel.removeActionListener(this);
        argumentsForm.btnOk.removeActionListener(this);
               
        argumentEditor = null;
        argumentCellRenderer = null;
        argumentTableModel = null;
        cvData = null;
        argumentsForm = null;
        dlgArgument = null;
    }
    /**
     * Method for focus traversing from table to components
     */ 
    public void setTableKeyTraversal(){
         javax.swing.InputMap im  =  argumentsForm.tblArgument.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
         KeyStroke tab  =  KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
         KeyStroke shiftTab  =  KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
         final Action oldTabAction  =  argumentsForm.tblArgument.getActionMap().get(im.get(tab));
         Action tabAction  =  new AbstractAction() {
             int row  =  0;
             int column  = 0;
            public void actionPerformed(ActionEvent e) {
                oldTabAction.actionPerformed( e );
                JTable table  =  (JTable)e.getSource();
                boolean selectionOut = false;
                int rowCount  =  table.getRowCount();
                int columnCount  =  table.getColumnCount();
                row  =  table.getSelectedRow();
                column  =  table.getSelectedColumn();
                if((rowCount-1) == row && column == (columnCount-1)){
                    if(getFunctionType() != TypeConstants.DISPLAY_MODE){
                        selectionOut = true;
                        argumentsForm.btnMore.requestFocusInWindow();
                    }
                    else{
                        argumentsForm.btnCancel.requestFocusInWindow();
                    }
                }
                
                if(rowCount < 1){
                    columnCount  =  0;
                    row  =  0;
                    column = 0;
                    argumentsForm.btnMore.requestFocusInWindow();
                    return ;
               }
                
               while (! table.isCellEditable(row, column) ) {
                    column +=  1;
                    if (column  ==  columnCount) {
                        column  =  0;
                        row += 1;
                    }
                    if (row  ==  rowCount) {
                        row  =  0;
                   }
                   if (row  ==  table.getSelectedRow()
                    && column  ==  table.getSelectedColumn()) {
                        break;
                    }
                }
                if(!selectionOut){
                    table.changeSelection(row, column, false, false);
                }
             }
        };
        argumentsForm.tblArgument.getActionMap().put(im.get(tab), tabAction);
        final Action oldShiftTabAction  =  argumentsForm.tblArgument.getActionMap().get(im.get(shiftTab));
        Action tabShiftAction  =  new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                oldShiftTabAction.actionPerformed( e );
                JTable table  =  (JTable)e.getSource();
                int rowCount  =  table.getRowCount();
                int row  =  table.getSelectedRow();
                int column  =  table.getSelectedColumn();
                
                while (! table.isCellEditable(row, column) ) {
                      column -=  1;
                  if (column <=  0) {
                        column  =  VALUE_COL;
                        row -= 1;
                    }
                   if (row < 0) {
                        row  =  rowCount-1;
                    }
                if (row  ==  table.getSelectedRow()
                    && column  ==  table.getSelectedColumn()) {
                        break;
                    }
                }
              table.changeSelection(row, column, false, false);
            }
        };
        argumentsForm.tblArgument.getActionMap().put(im.get(shiftTab), tabShiftAction);
     }
    
    /**
     * Getter for property okPressed.
     * @return Value of property okPressed.
     */
    public boolean isOkPressed() {
        return okPressed;
    }
    
    /**
     * Setter for property okPressed.
     * @param okPressed New value of property okPressed.
     */
    public void setOkPressed(boolean okPressed) {
        this.okPressed = okPressed;
    }
    
    /** 
     *An inner class provides the model for the Argument Window
     *
     */
    class ArgumentTableModel extends javax.swing.table.AbstractTableModel{
        
        private Class colClass[]  =  {String.class, String.class,String.class};
        private String colNames[]  =  {"Argument Name","Value","Description"};
        private CoeusVector cvData;
        int flag = 0;
        public boolean isCellEditable(int row, int col){
            if(getFunctionType() == TypeConstants.DISPLAY_MODE){
                return false;
            }else{
                if(col == 0 || col == 2 ){
                    return false;
                }else{
                    //Modified for case id:3351 'Adding more arguements to rule function'- start
//                    BusinessRuleFuncArgsBean businessRuleFuncArgsBean
//                               = (BusinessRuleFuncArgsBean)cvData.get(0);
                    BusinessRuleFuncArgsBean businessRuleFuncArgsBean
                               = (BusinessRuleFuncArgsBean)cvData.get(row);
                    //Modified for case id:3351 'Adding more arguements to rule function'- end
                    if(businessRuleFuncArgsBean.getWindowName() == null
                       || businessRuleFuncArgsBean.getWindowName().equals(EMPTY_STRING)){
                        return true;
                    }else{
                        return false;
                    }
                }
            }//End of outer else
        }
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        public String getColumnName(int column){
            return colNames[column];
        }
        
        public void setData(CoeusVector cvData){
            this.cvData = cvData;
            
        }
        public int getRowCount() {
           int vecSize = (cvData == null) ? 0 : cvData.size();
             return vecSize;
           
         }
        
        public Object getValueAt(int row, int col) {
            if(cvData != null && cvData.size() > 0) {
                BusinessRuleFuncArgsBean businessRuleFuncArgsBean
                                =  (BusinessRuleFuncArgsBean)cvData.get(row);
                if(businessRuleFuncArgsBean != null){
                    switch(col){
                        case ARGUMENT_NAME_COL:
                            return businessRuleFuncArgsBean.getArgumentName();
                        case VALUE_COL:
//                            if(businessRuleFuncArgsBean.getValue() == null
//                                    || businessRuleFuncArgsBean.getValue().trim().equals(EMPTY_STRING)){
//                                        return businessRuleFuncArgsBean.getDefaultValue();
//                            }
                            return businessRuleFuncArgsBean.getValue();
                        case DESCRIPTION_COL:
                            return businessRuleFuncArgsBean.getRuleExpDescription();
                           // return businessRuleFuncArgsBean.getRuleFuncDescription();
                    }
                }
            }
            return EMPTY_STRING;
        }
        public void setValueAt(Object value,int rowIndex,int colIndex){
            if(cvData == null||cvData.size() == 0){
                return;
            }
            String beanValue,objValue;
            BusinessRuleFuncArgsBean businessRuleFuncArgsBean 
                            = (BusinessRuleFuncArgsBean)cvData.get(rowIndex);
            if(businessRuleFuncArgsBean != null ){
                switch(colIndex){
                    case VALUE_COL:
//                        if(businessRuleFuncArgsBean.getValue() == null
//                                    || businessRuleFuncArgsBean.getValue().trim().equals(EMPTY_STRING)){
//                            beanValue = businessRuleFuncArgsBean.getDefaultValue();
//                        }else{
//                            beanValue = businessRuleFuncArgsBean.getValue();
//                        }
                        beanValue = businessRuleFuncArgsBean.getValue();
                        objValue = value.toString().trim();
                        if(!beanValue.equals(objValue)){
                            businessRuleFuncArgsBean.setValue(objValue);
                            if(businessRuleFuncArgsBean.getAcType()
                                    != null && businessRuleFuncArgsBean.getAcType()
                                        .equals(TypeConstants.INSERT_RECORD)){
                            }else{
                                businessRuleFuncArgsBean.setAcType(TypeConstants.UPDATE_RECORD);
                            }
                           // fireTableDataChanged();
                        }
                        break;
                    case DESCRIPTION_COL:
                        beanValue = businessRuleFuncArgsBean.getRuleFuncDescription();
                        objValue = value.toString().trim();
                        if(!beanValue.equals(objValue)){
                            businessRuleFuncArgsBean.setRuleExpDescription(objValue);
                            if(businessRuleFuncArgsBean.getAcType()
                                    != null && businessRuleFuncArgsBean.getAcType()
                                        .equals(TypeConstants.INSERT_RECORD)){
                            }else{
                                businessRuleFuncArgsBean.setAcType(TypeConstants.UPDATE_RECORD);
                            }
                           // fireTableDataChanged();
                        }
                }
            }
        }
   }
    /*** 
     * Table editor for Argument Editor
     */
     class ArgumentEditor extends DefaultCellEditor {
         private int column;
         private CoeusLabel lblComponent;
         private CoeusTextField txtValue;
         public ArgumentEditor() {
            super(new JComboBox());
            lblComponent=new CoeusLabel();
            lblComponent.setOpaque(false);
            txtValue = new CoeusTextField();
            txtValue.setDocument(new LimitedPlainDocument(200));
        }
       public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.column = column;
            switch (column) {
                case ARGUMENT_NAME_COL:
                    value = (value  ==  null ? EMPTY_STRING : value);
                    lblComponent.setText(value.toString().trim());
                    return lblComponent;
                case VALUE_COL:
                    
                    value = (value  ==  null ? EMPTY_STRING : value);
                    txtValue.setForeground(java.awt.Color.RED);
                    txtValue.setText(value.toString().trim());
                    return txtValue;
                case DESCRIPTION_COL:
                    value = (value  ==  null ? EMPTY_STRING : value);
                    lblComponent.setText(value.toString().trim());
                    return lblComponent;
                   
            }
            return lblComponent;
        }
        
        public Object getCellEditorValue() {
            switch (column) {
                case ARGUMENT_NAME_COL:
                    return lblComponent.getText();
                case VALUE_COL:
                    return txtValue.getText();
                case DESCRIPTION_COL:
                    return lblComponent.getText();
                
            }
            return lblComponent;
        }
      }
    /** 
      * Table Renderer for Argument table
      */
     class ArgumentCellRenderer extends DefaultTableCellRenderer{
        private CoeusLabel lblComponent;
        private CoeusTextField txtDollar;
        public ArgumentCellRenderer(){
            lblComponent  =  new CoeusLabel();
            lblComponent.setOpaque(true);
            lblComponent.setFont(CoeusFontFactory.getNormalFont());
            txtDollar  =   new CoeusTextField();
            lblComponent.setBorder(new EmptyBorder(0,0,0,0));
            lblComponent.setHorizontalAlignment(LEFT);  
            txtDollar.setBorder(new EmptyBorder(0,0,0,0));
        }
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                    boolean hasFocus, int row, int col){
            
            BusinessRuleFuncArgsBean bean 
                        = (BusinessRuleFuncArgsBean)cvData.get(row);
            if(bean.getWindowName() == null || bean.getWindowName().equals(EMPTY_STRING)){
              txtDollar.setForeground(java.awt.Color.RED);   
            }else{
                  txtDollar.setForeground(java.awt.Color.BLACK);
            }
           switch(col){
              case ARGUMENT_NAME_COL:
                  
                  if(isSelected){
                        lblComponent.setBackground(java.awt.Color.YELLOW);
                        lblComponent.setForeground(java.awt.Color.black);
                    }else{
                        lblComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                        lblComponent.setForeground(java.awt.Color.black);
                    }
                    value  =  (value  ==  null ? EMPTY_STRING : value);
                    lblComponent.setText(value.toString().trim());
                    return lblComponent;
                case VALUE_COL:
                    value  =  (value  ==  null ? EMPTY_STRING : value);                                          
                    //txtDollar.setForeground(java.awt.Color.RED);
                    txtDollar.setText(value.toString());
                    return txtDollar;                   
                case DESCRIPTION_COL:
                    if(isSelected){
                        lblComponent.setBackground(java.awt.Color.YELLOW);
                        lblComponent.setForeground(java.awt.Color.black);
                    }else{
                        lblComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                        lblComponent.setForeground(java.awt.Color.black);
                    }
                    value = (value == null ? EMPTY_STRING : value);
                    lblComponent.setText(value.toString().trim());
                    return lblComponent;                 
                    
            }
            return lblComponent;
        }
      }
   }
