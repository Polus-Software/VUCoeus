/*
 * AddReportFreqController.java
 *
 * Created on November 25, 2004, 11:50 AM
 */

package edu.mit.coeus.admin.controller;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.exception.*;

import edu.mit.coeus.admin.gui.AddReportFreqForm;
import edu.mit.coeus.bean.ReportBean;
import edu.mit.coeus.bean.ValidReportClassReportFrequencyBean;
import edu.mit.coeus.utils.MultipleTableColumnSorter;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.NotEquals;
import edu.mit.coeus.utils.query.QueryEngine;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observer;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;


/**
 *
 * @author  ajaygm
 */
public class AddReportFreqController extends AdminController
implements ActionListener,ItemListener{
    
    //Creates an instance of Query Engine
    private QueryEngine queryEngine;
    
    //Creates an instance Coeus Message Resources
    private CoeusMessageResources coeusMessageResources;
    
    //Creates an instance of ClassReportFreqForm
    private AddReportFreqForm addReportFreqForm;
    
    
    //Creates an instance of dialog
    private CoeusDlgWindow dlgAddReportFreqForm;
    
    //Gets an instance of Coeus Appplet MDI From
    private CoeusAppletMDIForm mdiForm;
    
    //Listener
    private BaseWindowObservable observable  = new BaseWindowObservable();
    
    //To get the key Clicked.
    //0 - Cancel clicked
    //1 - OK clicked
    private int BUTTON_CLICKED = 0;
    private final int CANCEL_CLICKED = 0;
    private final int OK_CLICKED = 1;
    
    
    //Vector to hold frequencies
    CoeusVector cvFrequencyFromServer = new CoeusVector();
    CoeusVector cvTypeData = new CoeusVector();
    CoeusVector cvAllFreqData = new CoeusVector();
    CoeusVector cvFinalData;
    CoeusVector cvSelectedFreq = new CoeusVector();;
    boolean freqFound = false;
    
    //To set the window title
    private static final String WINDOW_TITLE =  "Add Report Frequency";
    
    //For Setting the dimentions of teh dialog
    private static final int WIDTH = 425;
    private static final int HEIGHT = 300;
    
    //Column indexes for the table
    private static final int CODE_INDEX = 0;
    private static final int DESCRIPTION_INDEX = 1;
    
    //Empty string
    private static final String EMPTY_STRING = "";
    
    //Validation Message
    private static final String SELECT_ROW = "classReportFrequency_exceptionCode.1306";
    
    //To know wheather new Type is added 
    boolean newTypeAdded;
    
    //Bean which contains newly selected Type
    ReportBean newReportBean = new ReportBean();
    
    //For Setting the table model to Freq Table
    private AddReportFreqTableModel addReportFreqTableModel ;
    
    private MultipleTableColumnSorter sorter;
    
    AddReportFreqController(){
    }
    
    /** Creates a new instance of AddReportFreqController */
    public AddReportFreqController(CoeusAppletMDIForm mdiForm, CoeusVector cvTypeData,
    CoeusVector cvFrequency, CoeusVector cvClassAndFreq) {
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        this.mdiForm = mdiForm;
        this.cvFrequencyFromServer = cvFrequency;
        this.cvTypeData = cvTypeData;
        registerComponents();
        try{
            setFormData(cvClassAndFreq);
        }catch (CoeusException ce){
            ce.printStackTrace();
        }
        postInitComponents();
        display();
        
       
    }
    
    /**
     * Registers the Observer
     */    
    public void registerObserver( Observer observer ) {
        observable.addObserver( observer );
    }
    
    
    /** This method creates and sets the display attributes for the dialog
     */
    public void postInitComponents(){
        dlgAddReportFreqForm = new CoeusDlgWindow(mdiForm);
        dlgAddReportFreqForm.setResizable(false);
        dlgAddReportFreqForm.setModal(true);
        dlgAddReportFreqForm.getContentPane().add(addReportFreqForm);
        
        dlgAddReportFreqForm.setTitle(WINDOW_TITLE);
        dlgAddReportFreqForm.setFont(CoeusFontFactory.getLabelFont());
        dlgAddReportFreqForm.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAddReportFreqForm.getSize();
        dlgAddReportFreqForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgAddReportFreqForm.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                requestDefaultFocus();
            }
        });
        
        //code for disposing the window
        dlgAddReportFreqForm.addWindowListener(new WindowAdapter(){
            public void windowOpening(WindowEvent we){
            }
            public void windowClosing(WindowEvent we){
                dlgAddReportFreqForm.dispose();
            }
        });
        
        dlgAddReportFreqForm.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                dlgAddReportFreqForm.dispose();
            }});
        //code for disposing the window ends
            
         /*Performs the OK clicked action if enter is pressed*/
         addReportFreqForm.tblFrequency.addKeyListener (new KeyAdapter (){
            public void keyPressed (KeyEvent kEvent){
                if( kEvent.getKeyCode () == KeyEvent.VK_ENTER){
                    addReportFreqForm.btnOK.doClick ();
                    kEvent.consume ();
                }
            }
        });
    }
    
    /** To set the default focus for the component
     */
    public void requestDefaultFocus(){
        addReportFreqForm.cmbType.requestFocusInWindow();
    }
    
    
    /** Displays the Form which is being controlled.
     */
    public void display() {
        dlgAddReportFreqForm.setVisible(true);
    }
    
    /** Perform field formatting.
     * enabling, disabling components depending on the different conditions
     */
    public void formatFields() {
    }
    
    /** An overridden method of the controller
     * @return classReportFreqForm returns the controlled form component
     */
    public java.awt.Component getControlledUI() {
        return addReportFreqForm;
    }
    
    /** Returns the form data
     * @return returns the form data
     */
    public Object getFormData() {
        return null;
    }
    
    /** This method is used to set the listeners to the components.
     */
    public void registerComponents() {
        //Add listeners to all the buttons
        addReportFreqForm = new AddReportFreqForm();
        addReportFreqForm.btnOK.addActionListener(this);
        addReportFreqForm.btnCancel.addActionListener(this);
        addReportFreqForm.cmbType.addItemListener(this);
        
        /** Code for focus traversal - start */
        java.awt.Component[] components = { addReportFreqForm.cmbType,
        addReportFreqForm.btnOK,addReportFreqForm.btnCancel,
        addReportFreqForm.tblFrequency
        };
        
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        addReportFreqForm.setFocusTraversalPolicy(traversePolicy);
        addReportFreqForm.setFocusCycleRoot(true);
        
        /** Code for focus traversal - end */
        addReportFreqTableModel = new AddReportFreqTableModel();
        addReportFreqForm.tblFrequency.setModel(addReportFreqTableModel);
        setTableEditors();
    }
    
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
    }
    
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        CoeusVector cvClassAndFreq = (CoeusVector)data;
        
        ComboBoxBean cmbClass = (ComboBoxBean)cvClassAndFreq.get(0);
        addReportFreqForm.lblClassValue.setText(cmbClass.getDescription().trim());
        
        addReportFreqForm.cmbType.setModel(new DefaultComboBoxModel(cvTypeData));

        cvAllFreqData = (CoeusVector)cvClassAndFreq.get(1);
        ComboBoxBean cmbTypeBean = (ComboBoxBean)cvClassAndFreq.get(2);
        if(cmbTypeBean!=null){
            ComboBoxBean cmbBean = new ComboBoxBean(cmbTypeBean.getCode(),cmbTypeBean.getDescription().trim());
            addReportFreqForm.cmbType.setSelectedItem(cmbBean);
        }
        extractData();
    }

    
    /** Validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if
     * validation is through else returns false.
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return false;
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent
     */
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Object source = e.getSource();
        
        if(source.equals(addReportFreqForm.btnOK)){
            if(addReportFreqForm.tblFrequency.getSelectedRow() == -1){
                CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey(SELECT_ROW));
            }else{
                setButtonClicked(OK_CLICKED);
                CoeusVector cvSelFreq = performUpdate();
                setSelectedFreq(cvSelFreq);
                dlgAddReportFreqForm.dispose();
            }
        }else if(source.equals((addReportFreqForm.btnCancel))){
            dlgAddReportFreqForm.dispose();
        }
    }
    
    //Sets the table properties
    private void setTableEditors(){
        TableColumn column;
        JTableHeader tableHeader;
        tableHeader = addReportFreqForm.tblFrequency.getTableHeader();
        if( sorter == null ) {
                sorter = new MultipleTableColumnSorter((TableModel)addReportFreqForm.tblFrequency.getModel());
                sorter.setTableHeader(addReportFreqForm.tblFrequency.getTableHeader());
                addReportFreqForm.tblFrequency.setModel(sorter);
                
        }
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        addReportFreqForm.tblFrequency.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        addReportFreqForm.tblFrequency.setRowHeight(22);
        addReportFreqForm.tblFrequency.setOpaque(false);
        addReportFreqForm.tblFrequency.setSelectionMode(
        DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        addReportFreqForm.tblFrequency.setShowHorizontalLines(false);
        addReportFreqForm.tblFrequency.setShowVerticalLines(false);

        int preferWidth[] = {60, 325};
        for(int index = 0; index < preferWidth.length; index++) {
            column = addReportFreqForm.tblFrequency.getColumnModel().getColumn(index);
            column.setPreferredWidth(preferWidth[index]);
        }
    }//End setTableEditors
    
    //Table model for Type Table
    class AddReportFreqTableModel extends AbstractTableModel{
        
        private CoeusVector cvTempFreqData = new CoeusVector();
        private String columnName[] = {"Code","Description"};
        private Class colClass[] = {String.class,String.class};
        
        /**
         * Returns the number of columns in the model. A
         * <code>JTable</code> uses this method to determine how many columns it
         * should create and display by default.
         *
         * @return the number of columns in the model
         * @see #getRowCount
         */
        public int getColumnCount() {
            return columnName.length;
        }
        
        /**
         * Returns the most specific superclass for all the cell values
         * in the column.  This is used by the <code>JTable</code> to set up a
         * default renderer and editor for the column.
         *
         * @param columnIndex  the index of the column
         * @return the common ancestor class of the object values in the model.
         */
        public Class getColumnClass(int columnIndex) {
            return colClass[columnIndex];
        }
        
        /**
         * Returns the name of the column at <code>columnIndex</code>.  This is used
         * to initialize the table's column header name.  Note: this name does
         * not need to be unique; two columns in a table can have the same name.
         *
         * @param	columnIndex	the index of the column
         * @return  the name of the column
         */
        public String getColumnName(int columnIndex) {
            return columnName[columnIndex];
        }
        
        public int getRowCount() {
            return cvTempFreqData.size();
        }
        
        public void setData(CoeusVector cvTypeData){
            cvTempFreqData.removeAllElements();
            cvTempFreqData.addAll(cvTypeData);
        }
        
        /**
         * Sets the value in the cell at <code>columnIndex</code> and
         * <code>rowIndex</code> to <code>aValue</code>.
         *
         * @param	aValue		 the new value
         * @param	rowIndex	 the row whose value is to be changed
         * @param	columnIndex 	 the column whose value is to be changed
         * @see #getValueAt
         * @see #isCellEditable
         */
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        }
        
        /**
         * Returns the value for the cell at <code>columnIndex</code> and
         * <code>rowIndex</code>.
         *
         * @param	rowIndex	the row whose value is to be queried
         * @param	columnIndex 	the column whose value is to be queried
         * @return	the value Object at the specified cell
         */
        public Object getValueAt(int rowIndex, int columnIndex) {
            if(cvTempFreqData.size()<1){
                return null ;
            }
            ComboBoxBean cmbTypeData = (ComboBoxBean)cvTempFreqData.get(rowIndex);
            switch(columnIndex){
                case CODE_INDEX:
                    return cmbTypeData.getCode();
                    
                case DESCRIPTION_INDEX:
                    return cmbTypeData.getDescription();
            }
            return EMPTY_STRING;
        }
        
        /**
         * Returns true if the cell at <code>rowIndex</code> and
         * <code>columnIndex</code>
         * is editable.  Otherwise, <code>setValueAt</code> on the cell will not
         * change the value of that cell.
         *
         * @param	rowIndex	the row whose value to be queried
         * @param	columnIndex	the column whose value to be queried
         * @return	true if the cell is editable
         * @see #setValueAt
         */
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }//End ClassReportTypeTableModel

    
    //Extracts data table and combobox
    private void extractData(){
        freqFound = false;
        ComboBoxBean cmbTypeBean = (ComboBoxBean) addReportFreqForm.cmbType.getSelectedItem();
        Equals eqReportCode = new Equals("reportCode",new Integer(cmbTypeBean.getCode()));
        for(int index =0 ;index < cvAllFreqData.size() ;index++){
            CoeusVector cvDataFind = (CoeusVector)cvAllFreqData.get(index);
            CoeusVector cvDataFound = cvDataFind.filter(eqReportCode);
            if(cvDataFound != null && cvDataFound.size()>0){
                getFrequencyData(cvDataFind);
                freqFound = true;
            }
        }
        if(!freqFound){
            addReportFreqTableModel.setData(cvFrequencyFromServer);
            addReportFreqTableModel.fireTableDataChanged();
        }
    }//End extractData
    
    //Gets the frequeny data for the corresponding Type
    private void getFrequencyData(CoeusVector cvFreqData){
        cvFinalData = new CoeusVector();
        cvFinalData.addAll(cvFrequencyFromServer);
        
        for(int index = 0 ;index < cvFreqData.size(); index ++){
            ValidReportClassReportFrequencyBean validReportCRFBean =
            (ValidReportClassReportFrequencyBean)cvFreqData.get(index);
            NotEquals neFreq = new NotEquals("code",""+validReportCRFBean.getFrequencyCode());
            cvFinalData = cvFinalData.filter(neFreq);
        }
        addReportFreqTableModel.setData(cvFinalData);
        addReportFreqTableModel.fireTableDataChanged();
    }//End getFrequencyData
    
    
    public void itemStateChanged(java.awt.event.ItemEvent e) {
        
        if(e.getStateChange() == e.DESELECTED){
            return ;
        }
    
        Object source = e.getSource();
        
        if(source.equals(addReportFreqForm.cmbType)){
            extractData();
        }
    }//End itemStateChanged
    
    //Perfoms the operations which are required before 
    //updating the parent window.
    private CoeusVector performUpdate(){
        boolean found = false;
        int selRows [] = addReportFreqForm.tblFrequency.getSelectedRows();
        if(freqFound){
            for(int index = 0 ; index<selRows.length;index++){
                
                //Bug Fix:1685 Start 1
                int selIndex =
                    getSelectedRowIndex(addReportFreqForm.tblFrequency.getValueAt(selRows[index],0).toString());
                //ComboBoxBean cmbSelFreqBean = (ComboBoxBean)cvFinalData.elementAt(selRows[index]);
                ComboBoxBean cmbSelFreqBean = (ComboBoxBean)cvFinalData.elementAt(selIndex);
                //Bug Fix:1685 End 1
                
                cvSelectedFreq.add(cmbSelFreqBean);
                found = true;
                ComboBoxBean cmbBean = (ComboBoxBean)addReportFreqForm.cmbType.getSelectedItem();
                ReportBean reportBean  = new ReportBean();
                reportBean.setCode(cmbBean.getCode());
                reportBean.setDescription(cmbBean.getDescription());
                newReportBean = reportBean;
            }
        }else{
            for(int index = 0 ; index<selRows.length;index++){
            
                //Bug Fix:1685 Start 2
                int selIndex = 
                    selIndex = getSelRowForFrequency(addReportFreqForm.tblFrequency.getValueAt(selRows[index],0).toString());//Modified for Bug fix #1825
                //ComboBoxBean cmbSelFreqBean = (ComboBoxBean)cvFrequencyFromServer.elementAt(selRows[index]);
                ComboBoxBean cmbSelFreqBean = (ComboBoxBean)cvFrequencyFromServer.elementAt(selIndex);
                //Bug Fix:1685 End 2 
                
                cvSelectedFreq.add(cmbSelFreqBean);
                found = true;
            }
            setNewTypeAdded(true);
            newReportBean  = (ReportBean)addReportFreqForm.cmbType.getSelectedItem();
        }
        return cvSelectedFreq;
    }
    
    //Bug Fix:1685 Start 3
    private int getSelectedRowIndex(String strIndex) {
        int index;
        for(index=0;index<cvFinalData.size();index++){ 
            String strCode =((ComboBoxBean)cvFinalData.elementAt(index)).getCode();
            if(strIndex.equals(strCode)){
                return index;
            }
        }
        return index;
    }
    //Bug Fix:1685 End 3
    
    //Added for Bug fix #1825 Start 
    private int getSelRowForFrequency(String strIndex) {
        int index;
        for(index=0;index<cvFrequencyFromServer.size();index++){ 
            String strCode =((ComboBoxBean)cvFrequencyFromServer.elementAt(index)).getCode();
            if(strIndex.equals(strCode)){
                return index;
            }
        }
        return index;
    }//Bug fix #1825 end 
    
    /**
     * Getter for property BUTTON_CLICKED.
     * @return Value of property BUTTON_CLICKED.
     */
    public int getButtonClicked() {
        return BUTTON_CLICKED;
    }
    
    /**
     * Setter for property BUTTON_CLICKED.
     * @param BUTTON_CLICKED New value of property BUTTON_CLICKED.
     */
    public void setButtonClicked(int BUTTON_CLICKED) {
        this.BUTTON_CLICKED = BUTTON_CLICKED;
    }
    
    /**
     * Getter for property cvSelectedFreq.
     * @return Value of property cvSelectedFreq.
     */
    public edu.mit.coeus.utils.CoeusVector getSelectedFreq() {
        return cvSelectedFreq;
    }//End getSelectedFreq
    
    /**
     * Setter for property cvSelectedFreq.
     * @param cvSelectedFreq New value of property cvSelectedFreq.
     */
    public void setSelectedFreq(edu.mit.coeus.utils.CoeusVector cvSelectedFreq) {
        this.cvSelectedFreq = cvSelectedFreq;
    }
    
    /**
     * Getter for property newTypeAdded.
     * @return Value of property newTypeAdded.
     */
    public boolean isNewTypeAdded() {
        return newTypeAdded;
    }
    
    /**
     * Setter for property newTypeAdded.
     * @param newTypeAdded New value of property newTypeAdded.
     */
    public void setNewTypeAdded(boolean newTypeAdded) {
        this.newTypeAdded = newTypeAdded;
    }
    
    //Returns the newly selected type.
    //ie, type which is not present in the 
    //Class report freq window.
    public ReportBean getNewSelectedType(){
        return newReportBean;
    }
}//End AddReportFreqController
