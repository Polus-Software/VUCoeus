/*
 * ValidFrequencyController.java
 *
 * Created on November 23, 2004, 2:50 PM
 */

package edu.mit.coeus.admin.controller;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.admin.gui.ValidFrequencyForm;
import edu.mit.coeus.admin.gui.AddValidFrequencyBasisForm;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.bean.FrequencyBaseBean;
import edu.mit.coeus.bean.FrequencyBean;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.admin.bean.*;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.MultipleTableColumnSorter;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.ObjectCloner;

import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Hashtable;
import javax.swing.table.TableModel;

/**
 *
 * @author  chandrashekara
 */
public class ValidFrequencyController extends AdminController implements ActionListener,ItemListener,ListSelectionListener{
    private ValidFrequencyForm validFrequencyForm;
    private static final String EMPTY_STRING = "";
    private ValidFrequencyTableModel validFrequencyTableModel;
    private CoeusAppletMDIForm mdiForm;
    private static final int CODE_COLUMN = 0;
    private static final int DESCRIPTION_COLUMN=1;
    private CoeusDlgWindow dlgValidFrequency;
    private static final int WIDTH= 595;
    private static final int HEIGHT = 330 ;
    private final String GET_SERVLET ="/AdminMaintenanceServlet";
    private final String connectTo = CoeusGuiConstants.CONNECTION_URL+ GET_SERVLET;
    private static final char GET_VALID_FREQUENCY_AND_FREQUENCY_BASIS = 'C';
    private static final int USER_RIGHT_DATA = 0;
    private static final int FREQUENCY_DATA = 1;
    private static final int FREQUENCY_BASIS_DATA = 2;
    private static final int ALL_FREQUENCY_BASE_DATA = 3;
    private CoeusVector cvFrequency,cvFrequencyBasis,cvFileterBasis,cvAllFrequencyBaseData;
    private boolean right;
    private FrequencyBean frequencyBean;
    private CoeusVector cvData;
    private CoeusMessageResources coeusMessageResources;
    private CoeusVector cvDeletedItem;
    private MultipleTableColumnSorter sorter;
    
    /*There are no rows to be deleted*/
    private static final String DELETE_MSG = "frequeny_exceptionCode.1401";
    
    /*Are you sure you want to delete the record.*/
    private static final String DELETE_CONFIRMATION = "frequeny_exceptionCode.1402";
    
    /*Do u want to save the changes*/
    private static final String CANCEL_CONFIRMATION = "frequeny_exceptionCode.1403";
    private CoeusVector cvSaveFrequency;
    private CoeusVector cvAddedRecords;
    private CoeusVector cvFilteredFinalVector;
    private CoeusVector cvNewAddedData;
    private boolean dataModified;
    private String frequencyCode;
    private CoeusVector cvTempRecords;
    
    /*Bug Fix:N002*/
    private static final int CANCEL_CLICKED = 1;
    
    /** Creates a new instance of ValidFrequencyController */
    public ValidFrequencyController(CoeusAppletMDIForm mdiForm) throws CoeusException{
        this.mdiForm = mdiForm;
        validFrequencyForm = new ValidFrequencyForm();
        cvFilteredFinalVector = new CoeusVector();
        cvData = new CoeusVector();
        cvNewAddedData = new CoeusVector();
        cvDeletedItem = new CoeusVector();
        cvSaveFrequency = new CoeusVector();
        cvAddedRecords = new CoeusVector();
        cvTempRecords = new CoeusVector();
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
        formatFields();
        setFormData(null);
        setColumnData();
        postInitComponents();
        display();
    }
    
    /*to display the form*/
    public void display() {
        int rowCount = validFrequencyForm.tblFrequencyBase.getRowCount();
        if(rowCount!=0){
            validFrequencyForm.tblFrequencyBase.setRowSelectionInterval(0,0);
        }
      //  setRequestFocusInThread(validFrequencyForm.btnAdd);
        dlgValidFrequency.show();
    }
    
    /*to format the fields that is enabling and disabling the fields depending upon the mode*/
    public void formatFields() {
    }
    
    /*to return the component*/
    public java.awt.Component getControlledUI() {
        return validFrequencyForm;
    }
    
    /*to get the form data*/
    public Object getFormData() {
        return null;
    }
    
    /**
     * Registers listener and other components
     */
    public void registerComponents() {
        
        Component[] component = {validFrequencyForm.cmbFrequency,validFrequencyForm.pnlFrequency,
        validFrequencyForm.btnOK,validFrequencyForm.btnCancel ,
        validFrequencyForm.btnAdd,validFrequencyForm.btnDelete};
        
        ScreenFocusTraversalPolicy policy = new ScreenFocusTraversalPolicy(component);
        validFrequencyForm.setFocusTraversalPolicy(policy);
        validFrequencyForm.setFocusCycleRoot(true);
        
        validFrequencyTableModel = new ValidFrequencyTableModel();
        validFrequencyForm.tblFrequencyBase.setModel(validFrequencyTableModel);
        validFrequencyForm.tblFrequencyBase.getSelectionModel().addListSelectionListener(this);
        validFrequencyForm.tblFrequencyBase.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        validFrequencyForm.cmbFrequency.addItemListener(this);
        validFrequencyForm.btnAdd.addActionListener(this);
        validFrequencyForm.btnCancel.addActionListener(this);
        validFrequencyForm.btnDelete.addActionListener(this);
        validFrequencyForm.btnOK.addActionListener(this);
        
    }
    
    /*to get the data from the server and set the data to the form*/
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        cvFrequency= new CoeusVector();
        cvFrequencyBasis = new CoeusVector();
        cvAllFrequencyBaseData = new CoeusVector();
        Hashtable frequencyData = getFrequencyData();
        
        right = ((Boolean)frequencyData.get(new Integer(USER_RIGHT_DATA))).booleanValue();
        cvFrequency = (CoeusVector)frequencyData.get(new Integer(FREQUENCY_DATA));
        cvFrequencyBasis = (CoeusVector)frequencyData.get(new Integer(FREQUENCY_BASIS_DATA));
        cvAllFrequencyBaseData = (CoeusVector)frequencyData.get(new Integer(ALL_FREQUENCY_BASE_DATA));
        
        //validFrequencyForm.cmbFrequency.setModel(new DefaultComboBoxModel(cvFrequency));
        setComboValue(cvFrequency);
        validFrequencyForm.cmbFrequency.setShowCode(true);
        
        if(!right){
            validFrequencyForm.btnAdd.setEnabled(false);
            validFrequencyForm.btnDelete.setEnabled(false);
            validFrequencyForm.btnOK.setEnabled(false);
            validFrequencyForm.tblFrequencyBase.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        }else{
            validFrequencyForm.btnAdd.setEnabled(true);
            validFrequencyForm.btnDelete.setEnabled(true);
            validFrequencyForm.btnOK.setEnabled(true);
        }
    }
    
    /*to set the combo values to it and to set the first element initially*/
    private void setComboValue(CoeusVector cvData){
        if(cvData != null && cvData.size()>0){
            int size = cvData.size();
            for(int index = 0 ; index < size ; index++){
                validFrequencyForm.cmbFrequency.addItem(
                (FrequencyBean)cvData.elementAt(index));
            }
            frequencyBean= (FrequencyBean)cvData.elementAt(1);
            validFrequencyForm.cmbFrequency.setSelectedItem(frequencyBean);
            frequencyCode = frequencyBean.getCode();
        }
    }
    
    /*to get the frequency basis data i.e the table data*/
    private  Hashtable getFrequencyData() throws CoeusException{
        Hashtable data = null;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(GET_VALID_FREQUENCY_AND_FREQUENCY_BASIS);
        
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        
        comm.send();
        ResponderBean responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            data = (Hashtable)responder.getDataObject();
        }
        return data;
    }
    
    /** This method will specify the action performed
     * @param actionEvent ActionEvent
     * @return void
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        try{
            dlgValidFrequency.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
            if(source.equals(validFrequencyForm.btnCancel)){
                performCancelAction();
            }else if(source.equals(validFrequencyForm.btnAdd)){
                performAddAction();
            }else if(source.equals(validFrequencyForm.btnDelete)){
                performDeleteAction();
            }else if(source.equals(validFrequencyForm.btnOK)){
                if(dataModified){
                    saveFormData();
                }else{
                    dlgValidFrequency.dispose();
                }
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
        finally{
            dlgValidFrequency.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
        
    }
    //}
    
    
    /*this method is to get the records from the addValidFrequencyform to the Valid frequency main form*/
    private void performAddAction(){
        CoeusVector cvNewAddData = new CoeusVector();
        Object selectedItem = validFrequencyForm.cmbFrequency.getSelectedItem();
        String frequencyCode =((ComboBoxBean)validFrequencyForm.cmbFrequency.getSelectedItem()).getCode();
        AddValidFrequencyBasisForm addValidFrequencyBasisForm = new AddValidFrequencyBasisForm(
        mdiForm,selectedItem.toString(),frequencyCode,cvData);
        /*Bug Fix:N002 */
        if(addValidFrequencyBasisForm.display() == CANCEL_CLICKED)
            return;
        
        CoeusVector cvAddData = addValidFrequencyBasisForm.performCopyAction();
        /*Bug Fix:N002*/
        if(cvAddData == null || cvAddData.size() < 1)
            return;
        for(int i=0;i<cvAddData.size();i++){
            ValidFrequencyBean baseBean = (ValidFrequencyBean)cvAddData.get(i);
            FrequencyBaseBean bean = new FrequencyBaseBean();
            bean.setCode(baseBean.getFrequenctBaseCode());
            bean.setDescription(baseBean.getDescription());
            bean.setFrequencyCode(Integer.parseInt(frequencyCode));
            bean.setAcType(TypeConstants.INSERT_RECORD);
            if(bean != null){
                cvFileterBasis.add(bean);
                cvNewAddedData.add(bean);
                cvAddedRecords.add(bean);
                cvTempRecords.add(bean);
                CoeusVector cvTemp = new CoeusVector();
                cvAddedRecords = search(cvAddedRecords,frequencyCode);
                validFrequencyTableModel.setData(cvFileterBasis);
            }
            dataModified = true;
        }
        
        validFrequencyForm.tblFrequencyBase.setRowSelectionInterval(0,0);
    }
    
    private CoeusVector search(CoeusVector cvData , String code){
            CoeusVector cvAddData = cvData;
            CoeusVector cvNewAddData = new CoeusVector();
            String freqCode = code;
            Equals eqFrequency;
            eqFrequency = new Equals("frequencyCode",new Integer(freqCode));
            CoeusVector cvTemp = cvFrequencyBasis.filter(eqFrequency);
            if(cvTemp == null || cvTemp.size() <= 0){
                for(int j = 0 ; j < cvAddData.size() ; j++){
                    FrequencyBaseBean beanData = (FrequencyBaseBean)cvAddData.get(j);
                    cvNewAddData.add(beanData);
                }
            }
            
            for(int i = 0; i < cvTemp.size() ; i++){
                FrequencyBaseBean bean = (FrequencyBaseBean)cvTemp.get(i);
                for(int j=0 ; j < cvAddData.size() ; j++){
                    FrequencyBaseBean dataBean = (FrequencyBaseBean)cvAddData.get(j);
                    if(bean.getFrequencyCode() == dataBean.getFrequencyCode() && bean.getDescription().equals(dataBean.getDescription())){
                         cvAddData.removeElementAt(j);
                    }else{
                    }
                }
                
            }
            return cvAddData;
    }
    
    
    /*this method is to delete the record from the validFrequency table*/
    private void performDeleteAction(){
        int row = validFrequencyForm.tblFrequencyBase.getSelectedRow();
        if(row == -1){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DELETE_MSG));
            return;
        }
        
        if(row != -1 && row >= 0){
            String mesg = DELETE_CONFIRMATION;
            int selectedOption = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(mesg),
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                FrequencyBaseBean deletedBean = (FrequencyBaseBean)cvFileterBasis.get(row);
                
                cvTempRecords.add(deletedBean);
                if(cvFileterBasis!=null && cvFileterBasis.size() > 0){
                    if(cvFileterBasis.contains(deletedBean)){
                        cvDeletedItem.add(deletedBean);
                    }
                    cvFileterBasis.remove(row);
                    validFrequencyTableModel.fireTableRowsDeleted(row, row);
                    deletedBean.setAcType(TypeConstants.DELETE_RECORD);
                    
                }
            }
            dataModified = true;
        }
        
    }
    
    /*the action performed on the change of the combo values in the validFrequency screen*/
    public void itemStateChanged(ItemEvent itemEvent) {
       // CoeusVector cvTemp = new CoeusVector();
        try{
            Object source = itemEvent.getSource();
            String code = ((ComboBoxBean)validFrequencyForm.cmbFrequency.getSelectedItem()).getCode();
            cvFileterBasis = new CoeusVector();
            Equals eqFrequency;
            if(itemEvent.getStateChange() == ItemEvent.SELECTED) {
                if(source.equals(validFrequencyForm.cmbFrequency)){
                    eqFrequency = new Equals("frequencyCode",new Integer(code));
                    cvFileterBasis = cvFrequencyBasis.filter(eqFrequency);
                    cvSaveFrequency = (CoeusVector)ObjectCloner.deepCopy(cvFileterBasis);
                    //cvTemp = cvFileterBasis;
                    if(cvAddedRecords != null && cvAddedRecords.size() > 0){
                        for(int i = 0; i < cvAddedRecords.size() ; i++){
                            FrequencyBaseBean bean = (FrequencyBaseBean)cvAddedRecords.get(i);
                            int freqCode = Integer.parseInt(code);
                            if(freqCode == bean.getFrequencyCode()){
                                cvFileterBasis.add(bean);
                                
                            }
                        }
                    }
                    
                    if(cvDeletedItem != null && cvDeletedItem.size() > 0){
                        for(int i = 0; i < cvDeletedItem.size() ; i++ ){
                            FrequencyBaseBean deletedBean = (FrequencyBaseBean)cvDeletedItem.get(i);
                            int frequencyCodeValue = Integer.parseInt(code);
                            if(frequencyCodeValue == deletedBean.getFrequencyCode()){
                                cvFileterBasis.remove(deletedBean);
                            }
                        }
                    }
                    
                    if(cvFileterBasis == null || cvFileterBasis.size()<0){
                        validFrequencyTableModel.setData(null);
                    }else{
                        
                        validFrequencyTableModel.setData(cvFileterBasis);
                    }
                    cvData = cvFileterBasis;
                    
                }
            }
            if(cvFileterBasis != null && cvFileterBasis.size()>0){
                
                validFrequencyForm.tblFrequencyBase.setRowSelectionInterval(0,0);
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
    /*to perform if any validations exists*/
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return false;
    }
    
    /*to save the form data*/
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
        CoeusVector cvSaveData = new CoeusVector();
        cvSaveData = setSaveFrequencyData();
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType('J');
        if(cvSaveData != null && cvSaveData.size() > 0){
            requesterBean.setDataObject(cvSaveData);
            AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
            appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + GET_SERVLET);
            appletServletCommunicator.setRequest(requesterBean);
            appletServletCommunicator.send();
            ResponderBean responderBean = appletServletCommunicator.getResponse();
            if(responderBean!= null){
                if(!responderBean.isSuccessfulResponse()){
                    throw new CoeusException(responderBean.getMessage(), 1);
                }else{
                    cvFileterBasis = new CoeusVector();
                    cvFileterBasis = (CoeusVector)responderBean.getDataObject();
                }
            }
            
            dlgValidFrequency.setVisible(false);
        }else{
            dlgValidFrequency.setVisible(false);
        }
    }
    
   
    /*this method is to add the added and the deleted records to the final vector */
    private CoeusVector setSaveFrequencyData(){
        /*add all the deleted vectors to the save vector*/ 
        if(cvDeletedItem != null && cvDeletedItem.size() >0){
            cvSaveFrequency = searchAndDelete(cvDeletedItem);
            for(int i =0;i<cvSaveFrequency.size();i++){
                /*If the bean already exists then the remove the bean from the save vector*/
                FrequencyBaseBean bean = (FrequencyBaseBean)cvSaveFrequency.get(i);
                 int freqCode = bean.getFrequencyCode();
                 Equals eqFrequency;
                 eqFrequency = new Equals("frequencyCode",new Integer(freqCode));
                 CoeusVector cvTemp = cvFrequencyBasis.filter(eqFrequency);
                 if(!cvTemp.contains(bean)){
                     cvSaveFrequency.remove(bean);
                 }
           }
        }
        
        /*add all the added vectors to the save vector*/
        if(cvAddedRecords != null && cvAddedRecords.size() >0){
            for(int i = 0; i<cvAddedRecords.size();i++ ){
                FrequencyBaseBean bean = (FrequencyBaseBean)cvAddedRecords.get(i);
                if(cvFileterBasis.contains(bean)){
                    cvSaveFrequency.add(bean);
                }
            }
        }
      return cvSaveFrequency;
    }
    
     /*if the record deleted has the Actype I also then remove the vector*/
     private CoeusVector searchAndDelete(CoeusVector cvData){
         CoeusVector cvAddAndDeleteBean = cvData;
         for(int i=0;i<cvNewAddedData.size();i++){
             FrequencyBaseBean bean = (FrequencyBaseBean)cvNewAddedData.get(i);
             for(int j=0 ; j < cvAddAndDeleteBean.size() ; j++ ){
                 FrequencyBaseBean dataBean = (FrequencyBaseBean)cvAddAndDeleteBean.get(j);
                 if(dataBean.getFrequencyCode() == bean.getFrequencyCode() && dataBean.getDescription().equals(bean.getDescription())){
                     if(bean.getAcType() == TypeConstants.INSERT_RECORD){
                         cvAddAndDeleteBean.remove(i);
                     }
                 }
            }
         }
        return cvAddAndDeleteBean;
     }

    
    /**
     * Setting up the column data
     * @return void
     **/
    private void setColumnData(){
        JTableHeader tableHeader = validFrequencyForm.tblFrequencyBase.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        if( sorter == null ) {
            sorter = new MultipleTableColumnSorter((TableModel)validFrequencyForm.tblFrequencyBase.getModel());
            sorter.setTableHeader(validFrequencyForm.tblFrequencyBase.getTableHeader());
            validFrequencyForm.tblFrequencyBase.setModel(sorter);
        }
        validFrequencyForm.tblFrequencyBase.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        validFrequencyForm.tblFrequencyBase.setRowHeight(22);
        validFrequencyForm.tblFrequencyBase.setShowHorizontalLines(true);
        validFrequencyForm.tblFrequencyBase.setShowVerticalLines(true);
        validFrequencyForm.tblFrequencyBase.setOpaque(true);
        validFrequencyForm.tblFrequencyBase.setRowSelectionAllowed(true);
        
        TableColumn column = validFrequencyForm.tblFrequencyBase.getColumnModel().getColumn(CODE_COLUMN);
        column.setPreferredWidth(75);
        column.setResizable(true);
        
        column = validFrequencyForm.tblFrequencyBase.getColumnModel().getColumn(DESCRIPTION_COLUMN);
        column.setPreferredWidth(390);
        column.setResizable(true);
        
        if(cvFileterBasis != null && cvFileterBasis.size()>0){
            validFrequencyForm.tblFrequencyBase.setRowSelectionInterval(0,0);
        }
    }
    
    /** Specifies the Modal window */
    private void postInitComponents() {
        dlgValidFrequency = new CoeusDlgWindow(mdiForm);
        dlgValidFrequency.getContentPane().add(validFrequencyForm);
        dlgValidFrequency.setTitle("Valid Frequency and Frequency Basis");
        dlgValidFrequency.setFont(CoeusFontFactory.getLabelFont());
        dlgValidFrequency.setModal(true);
        dlgValidFrequency.setResizable(false);
        dlgValidFrequency.setSize(WIDTH,HEIGHT);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgValidFrequency.getSize();
        dlgValidFrequency.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgValidFrequency.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
                return;
            }
        });
        dlgValidFrequency.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgValidFrequency.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performCancelAction();
                return;
            }
        });
        
        dlgValidFrequency.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
    }
    
    /*to set the focus to the combo on opening*/
    private void setWindowFocus(){
        //validFrequencyForm.cmbFrequency.requestFocusInWindow();
        setRequestFocusInThread(validFrequencyForm.cmbFrequency);
    }
    
    /*the action performed on the click of the canecl button ,
     *if data modified saves the data else disposes the window*/
    private void performCancelAction(){
        try{
            if(dataModified){
                String mesg = CANCEL_CONFIRMATION;
                int selectedOption = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey(mesg),
                CoeusOptionPane.OPTION_YES_NO_CANCEL,
                CoeusOptionPane.DEFAULT_YES);
                if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                    saveFormData();
                } else if(selectedOption == CoeusOptionPane.OPTION_OK_CANCEL){
                    dlgValidFrequency.setVisible(false);
                }
            }else{
                dlgValidFrequency.setVisible(false);
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
    public void valueChanged(ListSelectionEvent e) {
        
    }
    
    /*
     *It's an inner class which specifies the table model
     */
    public class ValidFrequencyTableModel extends AbstractTableModel{
        
        // represents the column names of the columns of table
        private String[] colName = {"Code","Description"};
        
        // represents the column class of the fields of table
        private Class[] colClass = {String.class,String.class};
        
        
        /*returns true if the cell is editable else returns false*/
        public boolean isCellEditable(int row, int col){
            return false;
        }
        
        /**
         * To get the column count
         * @return int
         **/
        public int getColumnCount() {
            return colName.length;
        }
        
        
        /**
         * To get the column count
         * @param col int
         * @return String
         **/
        public String getColumnName(int col){
            return colName[col];
        }
        
        /**
         * To get the column class of the table
         * @param col int
         * @return Class
         **/
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        /**
         * To set the  data in the table
         * @param cvTableData CoeusVector
         * @return void
         **/
        public void setData(CoeusVector cvFileterBasis){
            cvFileterBasis = cvFileterBasis;
            fireTableDataChanged();
        }
        
        
        /**
         * To get the row count
         * @return int
         **/
        public int getRowCount() {
            if(cvFileterBasis==null){
                return 0;
            }else{
                return cvFileterBasis.size();
            }
            
        }
        
        /**
         * To get the value from the table
         * @param rowIndex int
         * @param columnIndex int
         * @return Object
         **/
        public Object getValueAt(int row, int col) {
            FrequencyBaseBean frequencyBaseBean = (FrequencyBaseBean)cvFileterBasis.get(row);
            switch(col){
                case CODE_COLUMN:
                    return frequencyBaseBean.getCode();
                case DESCRIPTION_COLUMN:
                    return frequencyBaseBean.getDescription();
            }
            return EMPTY_STRING;
        }
    }
    
    private void setRequestFocusInThread(final Component component) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }
    
}
