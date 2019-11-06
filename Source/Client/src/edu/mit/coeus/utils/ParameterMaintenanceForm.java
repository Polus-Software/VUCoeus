/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */


/* PMD check performed, and commented unused imports and variables on 25-FEB-2011
 * by Bharati 
 */

package edu.mit.coeus.utils;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import edu.mit.coeus.bean.CoeusParameterBean;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;

/**
 * ParameterMaintenanceForm.java
 * Created on August 17, 2004, 12:41 PM
 * @author  Vyjayanthi
 */
public class ParameterMaintenanceForm extends javax.swing.JComponent
implements ActionListener {
    
    public CoeusDlgWindow dlgParameters;
    private CoeusVector cvParameters;
    private ParameterTableModel parameterTableModel;
    private ParameterTableRenderer parameterTableRenderer;
    private ParameterTableEditor parameterTableEditor;
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    
    private static final int WIDTH = 660;
    private static final int HEIGHT = 390;
    public final int PARAMETER_COL = 0;
    public final int VALUE_COL = 1;
    private final String GET_ALL_PARAMETERS = "GET_ALL_PARAMETERS";
    private final String UPDATE_PARAMETER_DATA = "UPDATE_PARAMETER_DATA";
    
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
            "/coeusFunctionsServlet";
    private static final String ENTER_VALUE = "Enter value for parameter : ";
    private static final String SAVE_CHANGES = "saveConfirmCode.1002";
    //Added for Case#2402 - use a parameter to set the length of the account number throughout app - Start
    private static final int ACCOUNT_NUMBER_MAX_VALUE = 100;
    private static final int ACCOUNT_NUMBER_MIN_VALUE = 1;
    private static final String INVALID_MAX_ACCOUNT_NUMBER_LENGTH_VALUE = "parameter_exceptionCode.1000";
    //Case#2402 - End
    //Added for COEUSQA-2291 : Hide Reviewer Name in Review Comments - Start
    private static final String INVALID_IRB_DISPLAY_REVIEWER_NAME_VALUE = "parameter_exceptionCode.1001";
    //COEUSQA-2291 : End
    //Added for COEUSQA-2290 : New Minute entry type for Review Comments - Start
    private static final String INVALID_IRB_MINUTE_TYPE_REVIEWER_COMMENT_VALUE = "parameter_exceptionCode.1002";
    //COEUSQA-2290 : End
    
    //Added for COEUSQA-3332 : IACUC - Add Parameter to set Default Number of Days for Review Determination Date - start
    private static final String INVALID_DEFAULT_DAYS_IACUC_DETERMINATN_DUE_DATE = "parameter_exceptionCode.1004";
    //Added for COEUSQA-3332 : IACUC - Add Parameter to set Default Number of Days for Review Determination Date - end
    
    //added for COEUSQA -1728 : parameter to define the start date of fiscal year - start
    private int fiscalMonth;
    private int fiscalDate;
    private static final int FISCAL_YEAR_START_MAX_VALUE = 31;
    private static final int FISCAL_YEAR_START_MIN_VALUE = 1;
    private static final int FISCAL_YEAR_MONTH_START = 1;
    private static final int FISCAL_YEAR_MONTH_END = 12;
    private static final String INVALID_FISCAL_YEAR_START_DATE = "parameter_exceptionCode.1003";
    //added for COEUSQA -1728 - end
    
    private static final String INVALID_IACUC_DISPLAY_REVIEWER_NAME_VALUE = "iacuc_parameter_exceptionCode.1001";
    private static final String INVALID_IACUC_MINUTE_TYPE_REVIEWER_COMMENT_VALUE = "iacuc_parameter_exceptionCode.1002";

    /** Creates new form ParameterMaintenanceForm */
    public ParameterMaintenanceForm() {
        initComponents();
        postInitComponents();
        setFormData();
        setTableKeyTraversal();
    }
    
    /** To initialize other properties of the form
     */
    private void postInitComponents() {
        dlgParameters = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), true);
        dlgParameters.getContentPane().add(this);
        dlgParameters.setResizable(false);
        dlgParameters.setFont(CoeusFontFactory.getLabelFont());
        dlgParameters.setTitle("Parameter Maintenance");
        dlgParameters.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgParameters.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgParameters.getSize();
        dlgParameters.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
       
        dlgParameters.addEscapeKeyListener(
            new javax.swing.AbstractAction("escPressed"){
                public void actionPerformed(java.awt.event.ActionEvent ae){
                    performWindowClosing();
                }
        });
        
        dlgParameters.addWindowListener(new java.awt.event.WindowAdapter(){            
            
            public void windowOpened(java.awt.event.WindowEvent we) {
                setDefaultFocus();
            }
            
            public void windowClosing(java.awt.event.WindowEvent we){
                performWindowClosing();
                return;
            }
        });
        
        btnOK.addActionListener(this);
        btnCancel.addActionListener(this);
        
        parameterTableModel = new ParameterTableModel();
        parameterTableRenderer = new ParameterTableRenderer();
        parameterTableEditor = new ParameterTableEditor();
        tblParameters.setModel(parameterTableModel);
        tblParameters.setShowGrid(false);
        tblParameters.setRowHeight(22);
        tblParameters.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        
        TableColumn column = tblParameters.getColumnModel().getColumn(0);
        column.setPreferredWidth(340);
        column.setCellRenderer(parameterTableRenderer);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        
        column = tblParameters.getColumnModel().getColumn(1);
        column.setPreferredWidth(200);
        column.setCellRenderer(parameterTableRenderer);
        column.setCellEditor(parameterTableEditor);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        
        /** Code for focus traversal - start */
        java.awt.Component[] components = { btnOK, btnCancel, tblParameters};
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);        
        /** Code for focus traversal - end */
    }
    
    /**
     * This method is used to perform the Window closing operation. Before closing
     * the window it checks the saveRequired flag and the function type.
     * If the saveRequired is true then it saves the details to the
     * database else dispose this JDialog.
     */
    private void performWindowClosing() {
        parameterTableEditor.stopCellEditing();
        if( parameterTableModel.isDataModified() ){
            int selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(SAVE_CHANGES), CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_YES);
            switch( selection ){
                case CoeusOptionPane.SELECTION_YES:
                    try{
                        saveFormData();
                    }catch (CoeusClientException coeusClientException){
                        coeusClientException.printStackTrace();
                    }
                    break;
                case CoeusOptionPane.SELECTION_NO:
                    dlgParameters.dispose();
                    break;
            }
        }else {
            dlgParameters.dispose();
        }
    }
    
    /** To set the default focus for the components
     */
    private void setDefaultFocus() {
        btnOK.requestFocus();
    }
    
    /** Method to set data to the form
     */
    private void setFormData(){
        try{
            cvParameters = getParameterData();
            parameterTableModel.setData(cvParameters);
        }catch(CoeusClientException coeusClientException){
            coeusClientException.printStackTrace();
        }
    }
    
    /** Returns the parameter data
     * @return data returns the form data
     * @throws CoeusClientException coeus Client Exception
     */
    public CoeusVector getParameterData() throws CoeusClientException {
        RequesterBean requester = new RequesterBean();
        requester.setDataObject(GET_ALL_PARAMETERS);
        AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            CoeusVector cvData = (CoeusVector)response.getDataObject();
            return cvData;
        }else {
            throw new CoeusClientException(response.getMessage());
        }
    }
    
    /** Validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if
     * validation is through else returns false.
     */
    public boolean validateData() throws edu.mit.coeus.exception.CoeusUIException {
        for( int index = 0; index < cvParameters.size(); index++ ) {
            CoeusParameterBean parameterBean = (CoeusParameterBean)cvParameters.get(index);
            if(parameterBean.getParameterValue() == null ||
            parameterBean.getParameterValue().trim().length() == 0 ) {
                CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(ENTER_VALUE + parameterBean.getParameterName()));
                requestFocus(index);
                return false;
            }
            //Added for Case#2402 - use a parameter to set the length of the account number throughout app - Start
            //If MAX_ACCOUNT_NUMBER_LENGTH parameter value is entered greater than 35, then Warning dialog box is displayed
            if(parameterBean.getParameterName().equalsIgnoreCase(CoeusConstants.MAX_ACCOUNT_NUMBER_LENGTH)
                             && (!parameterBean.getParameterValue().matches("\\d+")
                             || Integer.parseInt(parameterBean.getParameterValue()) > ACCOUNT_NUMBER_MAX_VALUE 
                             || Integer.parseInt(parameterBean.getParameterValue()) < ACCOUNT_NUMBER_MIN_VALUE)){
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(coeusMessageResources.parseMessageKey(INVALID_MAX_ACCOUNT_NUMBER_LENGTH_VALUE)));
                requestFocus(index);
                return false;
            }
            //Case#2402 - End
            
            //Added for COEUSQA-3332 : IACUC - Add Parameter to set Default Number of Days for Review Determination Date - start
            //if value for parameter DEFAULT_DAYS_IACUC_DETERMINATN_DUE_DATE is other than numeric
            //then Warning dialog box is displayed
            if(CoeusConstants.DEFAULT_DAYS_IACUC_DETERMINATN_DUE_DATE.equalsIgnoreCase(parameterBean.getParameterName())
            && (!parameterBean.getParameterValue().trim().matches("\\d+"))){
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(coeusMessageResources.parseMessageKey(INVALID_DEFAULT_DAYS_IACUC_DETERMINATN_DUE_DATE)));
                requestFocus(index);
                return false;
            }
            //Added for COEUSQA-3332 : IACUC - Add Parameter to set Default Number of Days for Review Determination Date - end
            
            //added for COEUSQA -1728 : parameter to define the start date of fiscal year - start
            //Restricted the charecters by taking the  ASCII value for  0 to 9 in FISCAL_YEAR_START parameter value
            //If month and dates are greater than 12 and 31, then it displays the warning message            
              if(CoeusConstants.FISCAL_YEAR_START.equalsIgnoreCase(parameterBean.getParameterName())){
                 int commaIndex = 0;
                 int length = parameterBean.getParameterValue().length();
                 if(length>0){
                      for(int fiscalIndex=0;fiscalIndex<length;fiscalIndex++){
                          char ch = parameterBean.getParameterValue().charAt(fiscalIndex);                          
                          if((int)ch > 47 && (int)ch < 58 || ((int)ch == 44 || (int)ch == 47) && (fiscalIndex > 0 || fiscalIndex <=2)){
                          if(','==ch){
                              commaIndex = fiscalIndex; 
                              break;
                          }else if('/'==ch){
                              commaIndex = fiscalIndex; 
                              break;
                          } 
                      }
                      }
                  }
                  String fiscalMonthValue = parameterBean.getParameterValue().substring(0,commaIndex);
                  String fiscalDayValue = parameterBean.getParameterValue().substring(commaIndex+1,length);
                  if(commaIndex == 0 || CoeusGuiConstants.EMPTY_STRING.equals(fiscalMonthValue)){
                       CoeusOptionPane.showInfoDialog(
                            coeusMessageResources.parseMessageKey(coeusMessageResources.parseMessageKey(INVALID_FISCAL_YEAR_START_DATE)));
                     requestFocus(index);                   
                     return false;
                  }
                  int fiscalDate = 0;
                   int fiscalMonth = 0;
                  try{
                        fiscalDate = Integer.parseInt(fiscalDayValue);
                        fiscalMonth = Integer.parseInt(fiscalMonthValue);
                  }catch(NumberFormatException ex){
                      CoeusOptionPane.showInfoDialog(
                            coeusMessageResources.parseMessageKey(coeusMessageResources.parseMessageKey(INVALID_FISCAL_YEAR_START_DATE)));
                     requestFocus(index);                   
                     return false;
                  }
                  if((fiscalDate > FISCAL_YEAR_START_MAX_VALUE) || (fiscalDate < FISCAL_YEAR_START_MIN_VALUE)|| (fiscalMonth > FISCAL_YEAR_MONTH_END) ||
                        (fiscalMonth < FISCAL_YEAR_MONTH_START)){      
                     CoeusOptionPane.showInfoDialog(
                            coeusMessageResources.parseMessageKey(coeusMessageResources.parseMessageKey(INVALID_FISCAL_YEAR_START_DATE)));
                     requestFocus(index);                   
                     return false;
                }                                  
            }
            //added for COEUSQA -1728 : parameter to define the start date of fiscal year - end
            
            //Added for COEUSQA-2291 : Hide Reviewer Name in Review Comments - Start
            //IRB_DISPLAY_REVIEWER_NAME should be either '0' or '1'(To dislpay reviewer name for all comments)
            if(parameterBean.getParameterName().equalsIgnoreCase(CoeusConstants.IRB_DISPLAY_REVIEWER_NAME)
            && (!parameterBean.getParameterValue().matches("\\d+")
            || Integer.parseInt(parameterBean.getParameterValue()) > 1
                    || Integer.parseInt(parameterBean.getParameterValue()) < 0)){
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(coeusMessageResources.parseMessageKey(INVALID_IRB_DISPLAY_REVIEWER_NAME_VALUE)));
                requestFocus(index);
                return false;
            }
            //COEUSQA-2291 : End
            //Added for Case id COEUSQA-1724_Reviewer View of Protocol start
            if(parameterBean.getParameterName().equalsIgnoreCase(CoeusConstants.IACUC_DISPLAY_REVIEWER_NAME)
            && (!parameterBean.getParameterValue().matches("\\d+")
            || Integer.parseInt(parameterBean.getParameterValue()) > 1
                    || Integer.parseInt(parameterBean.getParameterValue()) < 0)){
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(coeusMessageResources.parseMessageKey(INVALID_IACUC_DISPLAY_REVIEWER_NAME_VALUE)));
                requestFocus(index);
                return false;
            }
            //Added for Case id COEUSQA-1724_Reviewer View of Protocol end
            //Added for COEUSQA-2290 : New Minute entry type for Review Comments - Start
            //IRB_MINUTE_TYPE_REVIEWER_COMMENT value should be valid number
            if(parameterBean.getParameterName().equalsIgnoreCase(CoeusConstants.IRB_MINUTE_TYPE_REVIEWER_COMMENT)
            && !parameterBean.getParameterValue().matches("\\d+")){
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(coeusMessageResources.parseMessageKey(INVALID_IRB_MINUTE_TYPE_REVIEWER_COMMENT_VALUE)));
                requestFocus(index);
                return false;
            }
            //COEUSQA-2290 : End
            
            //Added for Case id COEUSQA-1724_Reviewer View of Protocol start
            //IACUC_MINUTE_TYPE_REVIEWER_COMMENT value should be valid number
            if(parameterBean.getParameterName().equalsIgnoreCase(CoeusConstants.IACUC_MINUTE_TYPE_REVIEWER_COMMENT)
            && !parameterBean.getParameterValue().matches("\\d+")){
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(coeusMessageResources.parseMessageKey(INVALID_IACUC_MINUTE_TYPE_REVIEWER_COMMENT_VALUE)));
                requestFocus(index);
                return false;
            }
          
           //Added for Case id COEUSQA-1724_Reviewer View of Protocol end
        }        
        return true;
    }

    /** Sets the focus to the given row
     * @param row the row index
     */
    private void requestFocus(final int row){
        //Modified for Case#2402 - use a parameter to set the length of the account number throughout app - Start
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                tblParameters.requestFocusInWindow();
//                tblParameters.changeSelection( row, 1, true, false);
//            }
//        });
        
        //Focus is set to the Corresponding field
        tblParameters.setRowSelectionInterval(row,row);
        tblParameters.setColumnSelectionInterval(1,1);
        tblParameters.editCellAt(row,1);
        tblParameters.getEditorComponent().requestFocusInWindow();
        
    }
    
    /** Saves the Form Data.
     * @throws CoeusClientException coeus client exception
     */
    public void saveFormData() throws edu.mit.coeus.exception.CoeusClientException {
        try{
            parameterTableEditor.stopCellEditing();
            if( !parameterTableModel.isDataModified() ) {
                dlgParameters.dispose();
                return ;
            }
            if( validateData() ){
                //Save data to the database
                RequesterBean requester = new RequesterBean();
                requester.setDataObject(UPDATE_PARAMETER_DATA);
                requester.setDataObjects(cvParameters);
                AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
                comm.send();
                ResponderBean response = comm.getResponse();
                if(response.isSuccessfulResponse()){
                    dlgParameters.dispose();
                }else {
                    throw new CoeusClientException(response.getMessage());
                }
            }
        }catch (CoeusUIException coeusUIException){
            coeusUIException.printStackTrace();
        }
    }
    
    /** Displays the Form which is being controlled.
     */
    public void display() {
        dlgParameters.show();
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if( source.equals(btnOK) ){
            try{
                saveFormData();
            }catch (CoeusClientException coeusClientException){
                coeusClientException.printStackTrace();
            }
        }else if( source.equals(btnCancel) ){
            performWindowClosing();
        }
    }
    
    //Inner Class Table Model - Start
    class ParameterTableModel extends AbstractTableModel {
        private CoeusVector cvParameters;
        private final int colCount = 2;
        private boolean dataModified;
        
        private static final String EMPTY_STRING = "";
        
        public int getColumnCount() {
            return colCount;
        }
        
        public int getRowCount() {
            if( cvParameters == null ){
                return 0;
            }else {
                return cvParameters.size();
            }
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            CoeusParameterBean bean = (CoeusParameterBean)cvParameters.get(rowIndex);
            switch(columnIndex){
                case PARAMETER_COL:
                    return bean.getParameterName();
                case VALUE_COL:
                    return bean.getParameterValue();
            }
            return EMPTY_STRING;
        }
        
        public void setData(CoeusVector cvParameters){
            this.cvParameters = cvParameters;
        }
        
        public void setValueAt(Object value, int rowIndex, int columnIndex) {
            CoeusParameterBean bean = (CoeusParameterBean)cvParameters.get(rowIndex);
            switch( columnIndex ){
                case VALUE_COL:
                    if( value != null ){//For Case Fix #1867 start
                        if( bean.getParameterValue() == null || !value.toString().equals(bean.getParameterValue().trim())){//Case fix End #1867
                            bean.setParameterValue(value.toString());
                            bean.setAcType(TypeConstants.UPDATE_RECORD);
                            dataModified = true;
                        }
                    }
            }
            
        }
        
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if( columnIndex == PARAMETER_COL ){
                return false;
            }else {
                return true;
            }            
        }
        
        public boolean isDataModified(){
            return dataModified;
        }
        
    }//Inner Class Table Model - End
   
    //Inner class ParameterTableRenderer - Start
    class ParameterTableRenderer extends DefaultTableCellRenderer 
    implements TableCellRenderer {
        private final java.awt.Color PANEL_BACKGROUND = 
            (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background");
        private static final String SPACE = "  ";
        private JLabel lblParam;
        private CoeusTextField txtValue;
        
        ParameterTableRenderer() {
            lblParam = new JLabel();
            lblParam.setFont(CoeusFontFactory.getLabelFont());

            txtValue = new CoeusTextField();
            txtValue.setFont(CoeusFontFactory.getNormalFont());
        }
        
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            switch(column){
                case PARAMETER_COL:
                    lblParam.setText(SPACE +value.toString());
                    return lblParam;
                case VALUE_COL:
                
                    txtValue.setText((String)value);

                    return txtValue;
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }        
        
    }
    //Inner class ParameterTableRenderer - End
    
    class ParameterTableEditor extends AbstractCellEditor
    implements TableCellEditor, ActionListener {
        private CoeusTextField txtValue;
        private int column;
        
        ParameterTableEditor(){
            txtValue = new CoeusTextField();
            txtValue.setDocument(new LimitedPlainDocument(250));
            txtValue.addActionListener(this); 
        }
        
        public java.awt.Component getTableCellEditorComponent(
        JTable table, Object value, boolean isSelected, int row, int column) {
            this.column = column;
            switch(column){
                case VALUE_COL:
                    if(isSelected){
                        txtValue.selectAll();
                    }
                    txtValue.setText((String)value);//For Case Fix #1867
                    return txtValue;
            }
            return txtValue;
        }
        
        public Object getCellEditorValue() {
            switch (column) {
                case VALUE_COL:
                   //System.out.println(txtValue.getText());
                    return txtValue.getText();
            }
            return "";
        }
        
        public int getClickCountToStart(){
            return 1;
        }       
        
        public void actionPerformed(ActionEvent actionEvent) {
            try{
                saveFormData();
            }catch (CoeusClientException coeusClientException){
                coeusClientException.printStackTrace();
            }
            return ;
        }
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnParameter = new javax.swing.JScrollPane();
        tblParameters = new javax.swing.JTable(){
            public void changeSelection(int row, int column, boolean toggle, boolean extend){
                super.changeSelection(row, column, toggle, extend);
                javax.swing.SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        tblParameters.dispatchEvent(new java.awt.event.KeyEvent(
                            tblParameters,java.awt.event.KeyEvent.KEY_PRESSED,0,0,java.awt.event.KeyEvent.VK_F2,
                            java.awt.event.KeyEvent.CHAR_UNDEFINED) );
                    }
                });
            }
        };
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        scrPnParameter.setBorder(new javax.swing.border.EtchedBorder());
        tblParameters.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblParameters.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblParameters.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblParametersMouseClicked(evt);
            }
        });

        scrPnParameter.setViewportView(tblParameters);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(scrPnParameter, gridBagConstraints);

        btnOK.setFont(CoeusFontFactory.getLabelFont());
        btnOK.setMnemonic('O');
        btnOK.setLabel("OK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        add(btnOK, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(btnCancel, gridBagConstraints);

    }//GEN-END:initComponents

    private void tblParametersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblParametersMouseClicked
        // TODO add your handling code here:
            javax.swing.SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                tblParameters.dispatchEvent(new java.awt.event.KeyEvent(
                tblParameters,java.awt.event.KeyEvent.KEY_PRESSED,0,0,java.awt.event.KeyEvent.VK_F2,
                java.awt.event.KeyEvent.CHAR_UNDEFINED) );
            }
        });
    }//GEN-LAST:event_tblParametersMouseClicked
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOK;
    public javax.swing.JScrollPane scrPnParameter;
    public javax.swing.JTable tblParameters;
    // End of variables declaration//GEN-END:variables
    


    // This method will provide the key travrsal for the table cells
    // It specifies the tab and shift tab order.
    public void setTableKeyTraversal(){
        
        javax.swing.InputMap im = tblParameters.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        // Have the enter key work the same as the tab key
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
        
        // Override the default tab behaviour
        // Tab to the next editable cell. When no editable cells goto next cell.
        final Action oldTabAction = tblParameters.getActionMap().get(im.get(tab));
        Action tabAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                oldTabAction.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                int rowCount = table.getRowCount();
                int columnCount = table.getColumnCount();
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                
                while (! table.isCellEditable(row, column) ) {
                    column += 1;
                    
                    if (column == columnCount) {
                        column = 0;
                        row +=1;
                    }
                    
                    if (row == rowCount) {
                        row = 0;
                    }
                    
                    // Back to where we started, get out.
                    
                    if (row == table.getSelectedRow()
                    && column == table.getSelectedColumn()) {
                        break;
                    }
                }
                
                table.changeSelection(row, column, false, false);
            }
        };
        tblParameters.getActionMap().put(im.get(tab), tabAction);
        
        // for the shift+tab action
        
        // Override the default tab behaviour
        // Tab to the previous editable cell. When no editable cells goto next cell.
        
        final Action oldTabAction1 = tblParameters.getActionMap().get(im.get(shiftTab));
        Action tabAction1 = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                oldTabAction1.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                int rowCount = table.getRowCount();
                int columnCount = table.getColumnCount();
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                
                while (! table.isCellEditable(row, column) ) {
                    
                    column -= 1;
                    
                    if (column <= 0) {
                        column = 1;
                        row -=1;
                    }
                    
                    if (row < 0) {
                        row = rowCount-1;
                    }
                    
                    // Back to where we started, get out.
                    
                    if (row == table.getSelectedRow()
                    && column == table.getSelectedColumn()) {
                        break;
                    }
                }
                
                table.changeSelection(row, column, false, false);
            }
        };
        tblParameters.getActionMap().put(im.get(shiftTab), tabAction1);        
        
    }
}
