/*
 * LARateMaintainanceForm.java
 *
 * Created on March 2, 2004, 5:38 PM
 */

package edu.mit.coeus.unit.gui;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.unit.gui.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.budget.bean.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.utils.ObjectCloner;


import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.ParseException;

/**
 *
 * @author  chandru
 */
public class LARateMaintainanceForm extends javax.swing.JComponent 
implements ActionListener,ItemListener{
    
    private static final int HAND_ICON_COLUMN = 0;
    private static final int FISCAL_YEAR_COLUMN = 1;
    private static final int CAMPUS_COLUMN= 2;
    private static final int START_DATE_COLUMN = 3;
    private static final int RATE_COLUMN = 4;
    //Added code for case #1748 by tarique start 1
    private static final int UPDATE_TIME = 5;
    private static final int UPDATE_USER = 6;
    //Added code for case #1748 by tarique end 1
    private Vector vecLaRates = null;
    
    private CoeusVector cvRateClass = null;
    private CoeusVector cvRateType = null;
    private CoeusVector cvRates; //; = null;
    private CoeusVector cvFilteredRateType = null;
    private CoeusVector cvFilteredRates = null;
    private CoeusVector cvDeletedRates = null;
    private boolean hasRights = false;
    
    private ComboBoxBean comboBean = null;
        
    private static final String EMPTY_STRING = "";
    private CoeusAppletMDIForm mdiForm;
    private String unitNumber = EMPTY_STRING;
    private String unitName = EMPTY_STRING;
    private CoeusDlgWindow dlgLARate;
    private static final int WIDTH = 768;
    private static final int HEIGHT = 420;
    private LARatesTableModel lARatesTableModel;
    private LARateCellEditor lARateCellEditor;
    private LARateCellRenderer  lARateCellRenderer;
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private static final String REQUIRED_DATE_FORMAT = "dd-MMM-yyyy";
    private DateUtils dtUtils;
    private static final char LA_RATE = 'B';
    private static final char LA_RATE_UPDATE = 'C';
    private final String LA_RATE_SERVLET ="/unitServlet";
    private final String connect  = CoeusGuiConstants.CONNECTION_URL + LA_RATE_SERVLET;
    private RateClassBean  rateClassBean;
    private RateTypeBean  rateTypeBean;
    private InstituteLARatesBean instituteLARatesBean;
    private String rateClassCode  = EMPTY_STRING;
    private String rateTypeCode = EMPTY_STRING;
    private static final String DELETE_CONFIRMATION = "budgetPersons_exceptionCode.1305";
    private String mesg = EMPTY_STRING;
    private CoeusMessageResources coeusMessageResources;
    private boolean modified = false;
    private static final String INVALID_START_DATE = "laRate_Date_exceptionCode.1105";
    private static final String DUPLICATE_INFORMATION = "laRateDuplicate_exceptionCode.1107";
    private static final String ENTER_START_DATE = "laRate_exceptionCode.1106";
    private static final String DATE_SEPARATERS = ":/.,|-";
    private Vector vecSortedData;
    boolean sort = false;


    /** To specify the date format*/
    private java.text.SimpleDateFormat dtFormat
    = new java.text.SimpleDateFormat("MM/dd/yyyy");
    
    /** Creates new form LARateMaintainanceForm */
    public LARateMaintainanceForm(CoeusAppletMDIForm mdiForm,String unitNumber,String unitName) {
        this.mdiForm = mdiForm;
        this.unitNumber = unitNumber;
        this.unitName = unitName;
        coeusMessageResources = CoeusMessageResources.getInstance();
        cvDeletedRates = new CoeusVector();
        cvFilteredRates = new CoeusVector();
        //cvRates = new CoeusVector();
        dtUtils = new DateUtils();
        initComponents();  
        registerComponents();
        setTableEditors();
        setFormData();     
        setColumnData();
        postInitComponents();
        display();
    }
    
    
    private void postInitComponents(){
        String unitInfo = unitNumber + " : " + unitName;
        dlgLARate = new CoeusDlgWindow(mdiForm);
        dlgLARate.getContentPane().add(this);
                
        //Bug #2258 ----Start
        // Changed the spelling of 'Maintainance' to 'Maintenance'
        dlgLARate.setTitle("LA Rate Maintenance for  " + unitInfo); 
        //Bug #2258 -----End
        
        dlgLARate.setFont(CoeusFontFactory.getLabelFont());
        dlgLARate.setModal(true);
        dlgLARate.setResizable(false);
        dlgLARate.setSize(WIDTH,HEIGHT);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgLARate.getSize();
        dlgLARate.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgLARate.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                lARateCellEditor.stopCellEditing();
                performCancelAction();
                return;
            }
        });
        dlgLARate.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgLARate.addWindowListener(new WindowAdapter(){
             public void windowActivated(WindowEvent we){
                 setDefaultFocus();
             }
            public void windowClosing(WindowEvent we){
                performCancelAction();
                return;
            }
        });
    }
    
    private void setDefaultFocus(){
        if(hasRights){
            cmbRateClass.requestFocusInWindow();
        }else{
            btnCancel.requestFocusInWindow();
        }
    }
    
    private void setColumnData(){
       JTableHeader tableHeader = tblLARateMaintainance.getTableHeader();
       tableHeader.setReorderingAllowed(false);
       tableHeader.setFont(CoeusFontFactory.getLabelFont());
       
       tblLARateMaintainance.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
       tblLARateMaintainance.setRowHeight(22);
       tblLARateMaintainance.setSelectionBackground(java.awt.Color.white);
       tblLARateMaintainance.setSelectionForeground(java.awt.Color.black);
       tblLARateMaintainance.setShowHorizontalLines(false);
       tblLARateMaintainance.setShowVerticalLines(false);
       tblLARateMaintainance.setOpaque(false);
       tblLARateMaintainance.setSelectionMode(
            DefaultListSelectionModel.SINGLE_SELECTION);
       
       TableColumn column = tblLARateMaintainance.getColumnModel().getColumn(0);
       column.setMinWidth(30);
       column.setMaxWidth(50);
       column.setPreferredWidth(30);
       column.setResizable(false);
       column.setCellRenderer(new IconRenderer());
       column.setHeaderRenderer(new EmptyHeaderRenderer());
       
       
       column = tblLARateMaintainance.getColumnModel().getColumn(1);
       column.setMinWidth(80);
       column.setMaxWidth(100);
       column.setPreferredWidth(90);
       column.setResizable(true);
       column.setCellRenderer(lARateCellRenderer);
       column.setCellEditor(lARateCellEditor);
       
       column = tblLARateMaintainance.getColumnModel().getColumn(2);
       column.setMinWidth(70);
       column.setMaxWidth(110);
       column.setPreferredWidth(80);
       column.setResizable(true);
       column.setCellRenderer(lARateCellRenderer);
       column.setCellEditor(lARateCellEditor);
       
       column = tblLARateMaintainance.getColumnModel().getColumn(3);
       column.setMinWidth(90);
       column.setMaxWidth(150);
       column.setPreferredWidth(100);
       column.setResizable(true);
       column.setCellRenderer(lARateCellRenderer);
       column.setCellEditor(lARateCellEditor);
       
       column = tblLARateMaintainance.getColumnModel().getColumn(4);
       column.setMinWidth(90);
       column.setMaxWidth(140);
       column.setPreferredWidth(98);
       column.setResizable(true);
       column.setCellRenderer(lARateCellRenderer);
       column.setCellEditor(lARateCellEditor);
       //Added code for case #1748 by tarique start 2
       column = tblLARateMaintainance.getColumnModel().getColumn(5);
       column.setMinWidth(90);
       column.setMaxWidth(170);
       column.setPreferredWidth(150);
       column.setResizable(true);
       column.setCellRenderer(lARateCellRenderer);
       column.setCellEditor(lARateCellEditor);
       
       column = tblLARateMaintainance.getColumnModel().getColumn(6);
       column.setMinWidth(90);
       column.setMaxWidth(120);
       column.setPreferredWidth(110);
       column.setResizable(true);
       column.setCellRenderer(lARateCellRenderer);
       column.setCellEditor(lARateCellEditor);
       //Added code for case #1748 by tarique end 2
    }
    
     public void display(){
         if(tblLARateMaintainance.getRowCount() > 0 ){
             tblLARateMaintainance.setRowSelectionInterval(0,0);
         }
        cvFilteredRates.sort("fiscalYear");
        dlgLARate.setVisible(true);
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source.equals(btnOk)){
            performOkAction();
        }else if(source.equals(btnAdd)){
            performAddAction();
        }else if(source.equals(btnDelete)){
            performDeleteAction();
        }else if(source.equals(btnCancel)){
            performCancelAction();
        }
    }
    
    private void performDeleteAction(){
        lARateCellEditor.stopCellEditing();
        int rowIndex = tblLARateMaintainance.getSelectedRow();
        int rowCount = tblLARateMaintainance.getRowCount();
        if(rowCount > 0 && rowIndex != -1 && rowIndex >= 0){
            mesg = DELETE_CONFIRMATION;
            int selectedOption = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(mesg),
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            if(selectedOption == CoeusOptionPane.SELECTION_YES){
                InstituteLARatesBean deletedBean = (InstituteLARatesBean)cvFilteredRates.get(rowIndex);
                //Modified to delete Bean from Base Vector when Deleted by User - 08 April, 2004
//                if (deletedBean.getAcType() == null ||
//                deletedBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
                    cvDeletedRates.add(deletedBean);
                    cvRates.remove(deletedBean);
//                }
                if(cvFilteredRates!=null && cvFilteredRates.size() > 0){
                    cvFilteredRates.remove(rowIndex);
                    lARatesTableModel.fireTableRowsDeleted(rowIndex, rowIndex);
                    deletedBean.setAcType(TypeConstants.DELETE_RECORD);
                    modified = true;                    
                }
                if(rowIndex >0){
                    
                    tblLARateMaintainance.setRowSelectionInterval(
                    rowIndex-1,rowIndex-1);
                    tblLARateMaintainance.scrollRectToVisible(
                    tblLARateMaintainance.getCellRect(
                    rowIndex-1 ,0, true));
                }else{
                    if(tblLARateMaintainance.getRowCount()>0){
                        tblLARateMaintainance.setRowSelectionInterval(0,0);
                    }
                }
                
                if(rowIndex >0){
                    tblLARateMaintainance.setRowSelectionInterval(
                    rowIndex-1,rowIndex-1);
                    tblLARateMaintainance.scrollRectToVisible(
                    tblLARateMaintainance.getCellRect(rowIndex-1 ,0, true));
                    
                }else{
                    if(tblLARateMaintainance.getRowCount()>0){
                        tblLARateMaintainance.setRowSelectionInterval(0,0);
                    }
                }
            }
        }
    }
    
    public void performOkAction(){
       // if(cvFilteredRates!=null && cvFilteredRates.size() > 0){
            lARateCellEditor.stopCellEditing();
       // }
        if(validateData()){
            if(modified){
                updateRates();
                dlgLARate.dispose();
            }else{
                dlgLARate.dispose();
            }
        }
    }
    
    public void performAddAction(){
        lARateCellEditor.stopCellEditing();
        int rowCount = tblLARateMaintainance.getRowCount();
        String rateClassDesc = ((ComboBoxBean)cmbRateClass.getSelectedItem()).getDescription();
        int rateClassCode = Integer.parseInt(((ComboBoxBean)cmbRateClass.getSelectedItem()).getCode());
        int rateTypeCode = Integer.parseInt(((ComboBoxBean)cmbRateType.getSelectedItem()).getCode());
        
        AddLARateForm addLaRateForm;
        addLaRateForm = new AddLARateForm(mdiForm, true, unitNumber, unitName,rateClassDesc,
        rateClassCode,rateTypeCode,cvFilteredRates);
        InstituteLARatesBean instituteBean= addLaRateForm.display();
        
        if(instituteBean == null) {
            //Clicked on Cancel.  Don't add row            
            return ;
        }
        if(addLaRateForm.isBothSelected()){
            if(instituteBean!=null){
                instituteBean.setOnOffCampusFlag(true);
            }
            try{
                InstituteLARatesBean instituteBeanBoth = (InstituteLARatesBean)ObjectCloner.deepCopy(instituteBean);
                instituteBeanBoth.setOnOffCampusFlag(false);
                if(cvFilteredRates!=null){
                    modified= true;
                    
                    instituteBeanBoth.setAcType(TypeConstants.INSERT_RECORD);
                    instituteBean.setAcType(TypeConstants.INSERT_RECORD);
                    
                    cvFilteredRates.add(instituteBean);
                    cvFilteredRates.add(instituteBeanBoth);
                    cvFilteredRates.sort("fiscalYear");
                        for(int index = 0; index < cvFilteredRates.size(); index++){
                            instituteLARatesBean = (InstituteLARatesBean)cvFilteredRates.get(index);
                            if(cvRates.indexOf(instituteLARatesBean) == -1){
                                cvRates.add(instituteLARatesBean);
                            }
                        }
                }else{
                    cvFilteredRates = new CoeusVector();
                    
                    instituteBeanBoth.setAcType(TypeConstants.INSERT_RECORD);
                    instituteBean.setAcType(TypeConstants.INSERT_RECORD);
                    cvFilteredRates.add(instituteBean);
                    cvFilteredRates.add(instituteBeanBoth);
                    cvFilteredRates.sort("fiscalYear");
                    if(cvRates!=null){
                        for(int index = 0; index < cvFilteredRates.size(); index++){
                            instituteLARatesBean = (InstituteLARatesBean)cvFilteredRates.get(index);
                            if(cvRates.indexOf(instituteLARatesBean) == -1){
                                cvRates.add(instituteLARatesBean);
                            }
                        }
                        modified= true;
                    }else{
                        cvRates = new CoeusVector();
                        
                        for(int index = 0; index < cvFilteredRates.size(); index++){
                            instituteLARatesBean = (InstituteLARatesBean)cvFilteredRates.get(index);
                            if(cvRates.indexOf(instituteLARatesBean) == -1){
                                cvRates.add(instituteLARatesBean);
                            }
                        }
                        modified = true;
                    }
                }
                
          if(rowCount==0){
            lARatesTableModel.fireTableRowsInserted(rowCount, rowCount);
        }else{
            lARatesTableModel.fireTableRowsInserted(rowCount, rowCount+1);
        }
        
        int lastRow = tblLARateMaintainance.getRowCount()-1;
        if(lastRow >= 0){
            tblLARateMaintainance.setRowSelectionInterval( lastRow, lastRow );
            tblLARateMaintainance.scrollRectToVisible(
            tblLARateMaintainance.getCellRect(lastRow ,0, true));
        }
        lARateCellEditor.txtDateComponent.requestFocus();
                return;
            }catch (Exception exception) {
                exception.printStackTrace();
            }
        }else if(instituteBean!=null){
            if(cvFilteredRates!= null){
                modified = true;
                cvFilteredRates.add(instituteBean);
                cvFilteredRates.sort("fiscalYear");
                
                for(int index = 0; index < cvFilteredRates.size(); index++){
                    instituteLARatesBean = (InstituteLARatesBean)cvFilteredRates.get(index);
                    if(cvRates.indexOf(instituteLARatesBean) == -1){
                        cvRates.add(instituteLARatesBean);
                    }
                }
            }else{
                cvFilteredRates = new CoeusVector();
                cvFilteredRates.add(instituteBean);
                for(int index = 0; index < cvFilteredRates.size(); index++){
                    instituteLARatesBean = (InstituteLARatesBean)cvFilteredRates.get(index);
                    if(cvRates.indexOf(instituteLARatesBean) == -1){
                        cvRates.add(instituteLARatesBean);
                    }
                }
                modified= true;
            }
        }
        
        if(rowCount==0){
            lARatesTableModel.fireTableRowsInserted(rowCount, rowCount);
        }else{
            lARatesTableModel.fireTableRowsInserted(rowCount, rowCount+1);
        }
        
        int lastRow = tblLARateMaintainance.getRowCount()-1;
        if(lastRow >= 0){
            tblLARateMaintainance.setRowSelectionInterval( lastRow, lastRow );
            tblLARateMaintainance.scrollRectToVisible(
            tblLARateMaintainance.getCellRect(lastRow ,0, true));
        }
        lARateCellEditor.txtDateComponent.requestFocus();
    }
    
    private void performCancelAction(){
        lARateCellEditor.stopCellEditing();
        if(modified){
            confirmClosing();
        }else{
            dlgLARate.setVisible(false);
        }
    }
    
    
    private void confirmClosing(){
        try{
            int option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,CoeusOptionPane.DEFAULT_CANCEL);
            if(option == CoeusOptionPane.SELECTION_YES){
                performOkAction();
            }else if(option == CoeusOptionPane.SELECTION_NO){
                dlgLARate.setVisible(false);
            }else if(option==CoeusOptionPane.SELECTION_CANCEL){
                return;
            }
        }catch(Exception exception){
            exception.getMessage();
        }
    }
    
    private void setFormData(){
        vecLaRates = getFormData(unitNumber);
        cvRateClass = (CoeusVector)vecLaRates.get(0);
        cvRateType = (CoeusVector)vecLaRates.get(1);
        cvRates = (CoeusVector)vecLaRates.get(2);
        if(cvRates== null){
            cvRates = new CoeusVector();
        }
        Boolean obj = (Boolean) vecLaRates.get(3);
        hasRights = obj.booleanValue();// Right check for MAINTAIN_UNIT_LA_RATES
        
        if(!hasRights){
            btnAdd.setEnabled(hasRights);
            btnDelete.setEnabled(hasRights);
            btnOk.setEnabled(hasRights);
            cmbRateClass.setBackground(
            (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            cmbRateType.setBackground(
            (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            
            lARateCellEditor.txtDateComponent.setBackground(
            (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            
            lARateCellEditor.txtCurrencyComp.setBackground(
            (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        }
        setComboData();
    }
    
    
    
    private Vector getFormData(String unitNumber){
        Vector data = null;
        
        RequesterBean requester;
        ResponderBean responder;
        
        requester = new RequesterBean();
        requester.setFunctionType(LA_RATE);
        requester.setDataObject(unitNumber);
        
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connect, requester);
        
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            data = (Vector)responder.getDataObjects();
        }
        return data;
    }
    
    private boolean validateData(){
        //  if(tblLARateMaintainance.getRowCount() > 0){
        lARateCellEditor.stopCellEditing();
        //  }
        InstituteLARatesBean instituteLARatesBean;
        if(cvFilteredRates!=null){
            for(int index = 0; index < cvFilteredRates.size(); index ++){
                instituteLARatesBean = (InstituteLARatesBean)cvFilteredRates.get(index);
                if(instituteLARatesBean.getStartDate() == null ||
                instituteLARatesBean.getStartDate().equals(EMPTY_STRING)){
                    
                    CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(ENTER_START_DATE));
                    
                    
                    tblLARateMaintainance.editCellAt(index,START_DATE_COLUMN);
                    lARateCellEditor.txtDateComponent.requestFocus();
                    tblLARateMaintainance.setRowSelectionInterval(index ,index );
                    tblLARateMaintainance.scrollRectToVisible(
                    tblLARateMaintainance.getCellRect(index, START_DATE_COLUMN, true));
                    return false;
                }
                //Commented the code for 3632 - Data Error in rates maintenance when current rate is 0 - start
                //User is allowed to enter 0 for rate
//                else if(instituteLARatesBean.getInstituteRate()==0.00 ||
//                instituteLARatesBean.getInstituteRate()==.00){
//                    
//                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("laRate_rate_exceptionCode.1103"));
//                    tblLARateMaintainance.editCellAt(index,RATE_COLUMN);
//                    lARateCellEditor.txtDateComponent.requestFocus();
//                    tblLARateMaintainance.setRowSelectionInterval(index ,index );
//                    tblLARateMaintainance.scrollRectToVisible(
//                    tblLARateMaintainance.getCellRect(index, RATE_COLUMN, true));
//                    return false;
//                }
                //Commented the code for 3632 - Data Error in rates maintenance when current rate is 0 - end
            }
        }
        return true;
    }
    
    private void setComboData(){
        //Setting the rate class type values to the rate class combo.
        if( ( cvRateClass != null ) && ( cvRateClass.size() > 0 )
        && ( cmbRateClass.getItemCount() == 0 ) ) {
            int rateClassSize = cvRateClass.size();
            for(int index = 0 ; index < rateClassSize ; index++){
                cmbRateClass.addItem(
                (RateClassBean)cvRateClass.elementAt(index));
            }
            rateClassBean = (RateClassBean)cvRateClass.elementAt(0);
            cmbRateClass.setSelectedItem(rateClassBean);
        }
    }
    
    private void registerComponents(){
        btnAdd.addActionListener(this);
        btnCancel.addActionListener(this);
        btnDelete.addActionListener(this);
        btnOk.addActionListener(this);
        cmbRateClass.addItemListener(this);
        cmbRateType.addItemListener(this);
        
        lARateCellEditor = new LARateCellEditor();
        lARateCellRenderer = new LARateCellRenderer();
        lARatesTableModel = new LARatesTableModel();
        tblLARateMaintainance.setModel(lARatesTableModel);
        
        Component[] comp = {cmbRateClass,cmbRateType,tblLARateMaintainance, btnOk,
        btnCancel,btnAdd,btnDelete};
        ScreenFocusTraversalPolicy traversal = new ScreenFocusTraversalPolicy(comp);
        setFocusTraversalPolicy(traversal);
        setFocusCycleRoot(true);
        
    }
    /** To update the chnages that are made while changing the rates for the
     *table
     */
    private void updateRates(){
        lARateCellEditor.stopCellEditing();
        CoeusVector dataObject = new CoeusVector();
        // check whether the delete vector is null
        if(cvDeletedRates!=null && cvDeletedRates.size() > 0){
            dataObject.addAll(cvDeletedRates);
           dataObject.addAll(cvRates);
        }else{
            // check if the rates vector is null or not. If null create a new
            // instance of that.To avoid null pointer, when there is no data
            // while deleting.
            //if(cvFilteredRates!=null && cvFilteredRates.size() > 0){
            if(cvRates!=null && cvRates.size() > 0){
                //dataObject.addAll(cvFilteredRates);
                dataObject.addAll(cvRates);
            }else{
                //cvFilteredRates = new CoeusVector();
                cvRates= new CoeusVector();
                //dataObject.addAll(cvFilteredRates);
                dataObject.addAll(cvRates);
            }
        }
        
        dataObject = dataObject.filter(new NotEquals("acType", null));
        if(dataObject!=null && dataObject.size() > 0){
            Vector vctDataObjects = new Vector(3,2);
            RequesterBean requester;
            ResponderBean responder;
            
            requester = new RequesterBean();
            requester.setFunctionType(LA_RATE_UPDATE);
            vctDataObjects.addElement(unitNumber);
            vctDataObjects.addElement(dataObject);
            requester.setDataObjects(vctDataObjects);
            
            AppletServletCommunicator comm
            = new AppletServletCommunicator(connect, requester);
            
            comm.send();
            responder = comm.getResponse();
            if(responder.isSuccessfulResponse()){
                vctDataObjects = responder.getDataObjects();
                cvRates = (CoeusVector)vctDataObjects.elementAt(2);
                //System.out.println("Updated successfully");
            }else{
                //System.out.println("Updation failed");
                System.out.println("");
            }
        }
        
    }
    
    /** An Inner class provides the model data for the table
     */
    public class LARatesTableModel extends AbstractTableModel implements TableModel{
        //Modified code for case #1748 by tarique start 3
        private String colNames[] = {EMPTY_STRING,"Fiscal Year","Campus","Start Date","Rate","Update Timestamp","Update User"};
        private Class colClass[] = {ImageIcon.class,String.class,String.class,
                    String.class,Double.class, String.class,String.class};
        //Modified code for case #1748 by tarique end 3
        LARatesTableModel(){
        }
        public boolean isCellEditable(int row, int col){
            // If the user doen't have right then stop cell editable for all col &row
            if(!hasRights){
                return false;
            }else{
                //Modified code for case #1748 by tarique start 4
                if(col==HAND_ICON_COLUMN || col==FISCAL_YEAR_COLUMN || col==CAMPUS_COLUMN 
                    || col == UPDATE_TIME || col == UPDATE_USER) {
                //Modified code for case #1748 by tarique end 4         
                    return false;
                }else{
                    return true;
                }
            }
        }
        
        public int getColumnCount(){
            return colNames.length;
        }
        
        public Class getColumnClass(int columnIndex) {
            return colClass [columnIndex];
        }
        
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        public void addRow(InstituteLARatesBean instituteLARatesBean) {
            cvFilteredRates.add(instituteLARatesBean);
        }
        
        public int getRowCount(){
            if(cvFilteredRates==null){
                return 0;
            }else{
                return cvFilteredRates.size();
            }
        }
         public CoeusVector getData(){
            return cvFilteredRates;
        }
        
        public void setData(CoeusVector cvFilteredRates){
            cvFilteredRates = cvFilteredRates;
            fireTableDataChanged();
        }
        public Object getValueAt(int row, int col){
            InstituteLARatesBean instituteLARatesBean = (InstituteLARatesBean)cvFilteredRates.get(row);
            
            switch(col){
                case FISCAL_YEAR_COLUMN:
                    return instituteLARatesBean.getFiscalYear();
                case CAMPUS_COLUMN:
                    return new Boolean(instituteLARatesBean.isOnOffCampusFlag());
                case START_DATE_COLUMN:
                    return instituteLARatesBean.getStartDate();
                case RATE_COLUMN:
                    return  new Double(instituteLARatesBean.getInstituteRate());
                //Added code for case #1748 by tarique start 5
                case UPDATE_TIME :
                    if(instituteLARatesBean.getUpdateTimestamp() == null){
                        return EMPTY_STRING;
                    }
                    return instituteLARatesBean.getUpdateTimestamp().toString();
                case UPDATE_USER :
                    if(instituteLARatesBean.getUpdateUser() == null ){
                        return EMPTY_STRING;
                    }
                    return instituteLARatesBean.getUpdateUser();
                //Added code for case #1748 by tarique end 5
                    
            }
            return EMPTY_STRING;
        }
        
        public void setValueAt(Object value, int row, int col){
            if(cvFilteredRates == null) return;
            InstituteLARatesBean instituteLARatesBean = (InstituteLARatesBean)cvFilteredRates.get(row);
            String message=null;
            Date date = null;
            String strDate=null;
            double cost;
            //Modified for case 3632 - Data Error in rates maintenance when current rate is 0 - start
            //valueChanged will be set to true if there is change in the data value while modification
            boolean valueChanged = false;
            switch(col){
                case START_DATE_COLUMN:
                    try{
                        strDate = dtUtils.formatDate(
                        value.toString(), DATE_SEPARATERS, REQUIRED_DATE_FORMAT);
                        strDate = dtUtils.restoreDate(strDate, DATE_SEPARATERS);
                        if(strDate==null) {
                            throw new CoeusException();
                        }
                        date = dtFormat.parse(strDate.trim());
                    }catch (ParseException parseException) {
                        parseException.printStackTrace();
                        message = coeusMessageResources.parseMessageKey(
                        INVALID_START_DATE);
                        CoeusOptionPane.showErrorDialog(message);
                        return ;
                    }
                    catch (CoeusException coeusException) {
                        message = coeusMessageResources.parseMessageKey(
                        INVALID_START_DATE);
                        CoeusOptionPane.showErrorDialog(message);
                        return ;
                    }
                    if(instituteLARatesBean.getStartDate().equals(date)) break;
                    boolean duplicate = isDuplicateDate(instituteLARatesBean, date);
                    if(!duplicate){
                        instituteLARatesBean.setStartDate(new java.sql.Date(date.getTime()));
                        modified = true;
                        valueChanged = true;
                        break;
                    }else{
                        CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(DUPLICATE_INFORMATION));
                        break;
                        // modified = true;
                        //return ;
                    }
                    
                case RATE_COLUMN:
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        cost = 0.0;
                    }else{
                        cost = new Double(value.toString()).doubleValue();
                    }
                    if(instituteLARatesBean.getInstituteRate()==cost)break;
                    instituteLARatesBean.setInstituteRate(cost);
                    modified = true;
                    valueChanged = true;
                    break;
                    
            }
            //Set the ac type only if the data is changed
            //if(modified){
            if(valueChanged){
                if(instituteLARatesBean.getAcType()==null){
                    instituteLARatesBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
            }
            valueChanged = false;
        }
        //Modified for case 3632 - Data Error in rates maintenance when current rate is 0 - end
    }// End of LARatesTableModel class.........
    
    
    private boolean isDuplicateDate(InstituteLARatesBean laRatesBean, Date date){
        Equals eqDate;
        Equals eqFiscalYear;
        Equals eqCampus;
        And eqDateAndeqFiscalYear;
        And eqCampusAndeqDateAndeqFiscalYear;
        //InstituteLARatesBean rateBean;
        CoeusVector cvValidDate = new CoeusVector();
        
        if(cvFilteredRates!= null && cvFilteredRates.size() > 0){
            //for(int index =0; index <
            eqDate = new Equals("startDate", date);
            eqFiscalYear = new Equals("fiscalYear", laRatesBean.getFiscalYear());
            eqCampus = new Equals("onOffCampusFlag",laRatesBean.isOnOffCampusFlag());
            eqDateAndeqFiscalYear = new And(eqDate, eqFiscalYear);
            eqCampusAndeqDateAndeqFiscalYear = new And(eqDateAndeqFiscalYear,eqCampus);
            cvValidDate = cvFilteredRates.filter(eqCampusAndeqDateAndeqFiscalYear);
            if(cvValidDate.size() > 0){
                return true;
            }
        }
        return false;
    }
    
    public class LARateCellEditor extends AbstractCellEditor implements TableCellEditor{
        
        private JTextField txtComponent;
        private CurrencyField txtCurrencyComp;
        private JTextField txtDateComponent;
        private int column;
        
        public LARateCellEditor(){
            txtComponent = new JTextField();
            txtCurrencyComp = new CurrencyField();
            txtDateComponent = new JTextField();
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected, int row, int column) {
            
            this.column = column;
            switch(column){
                case FISCAL_YEAR_COLUMN:
                case CAMPUS_COLUMN:
                    txtComponent.setText(value.toString());
                    return txtComponent;
                case START_DATE_COLUMN:
                    txtDateComponent.setText(dtUtils.formatDate(value.toString(),SIMPLE_DATE_FORMAT));
                    return txtDateComponent;
                case RATE_COLUMN:
                    txtCurrencyComp.setText(value.toString());
                    return txtCurrencyComp;
                //Added code for case #1748 by tarique start 6
                case UPDATE_TIME :
                case UPDATE_USER :
                    txtComponent.setText(value.toString());
                    return txtComponent;
                //Added code for case #1748 by tarique end 6
            }
            return txtComponent;
        }
        public Object getCellEditorValue() {
            switch(column){
                case FISCAL_YEAR_COLUMN:
                case CAMPUS_COLUMN:
                    return txtComponent.getText();
                case START_DATE_COLUMN:
                    return txtDateComponent.getText();
                case RATE_COLUMN:
                    return txtCurrencyComp.getText();
                //Added code for case #1748 by tarique start 7
                case UPDATE_TIME :
                case UPDATE_USER :
                    return txtComponent.getText();
                //Added code for case #1748 by tarique end 7
                default:
                    return ((JTextField)txtComponent).getText();
            }
        }
        
        public int getClickCountToStart(){
            return 1;
        }
    }// End of LARateCellEditor class.........
    
    public class LARateCellRenderer extends DefaultTableCellRenderer{
        private JTextField txtComponent;
        private JTextField txtDateComponent;
        private CurrencyField txtCurrencyComp;
        private JTextField txtCampus;
        
        public LARateCellRenderer(){
            txtComponent = new JTextField();
            txtDateComponent = new JTextField();
            txtCurrencyComp = new CurrencyField();
            txtCampus = new JTextField();
            
            txtComponent.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            txtCampus.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            txtComponent.setForeground(Color.black);
            txtCampus.setForeground(Color.black);
        }
        
        public Component getTableCellRendererComponent(JTable table,Object value,
        boolean isSelected, boolean hasFocus, int row, int column){
            switch(column){
                case FISCAL_YEAR_COLUMN:
                    txtComponent.setText(value.toString());
                    return txtComponent;
                case CAMPUS_COLUMN:
                    instituteLARatesBean = (InstituteLARatesBean)cvFilteredRates.get(row);
                    if(instituteLARatesBean.isOnOffCampusFlag()== true){
                        value = "On";
                    }else{
                        value = "Off";
                    }
                    txtCampus.setText(value.toString());
                    return txtCampus;
                case START_DATE_COLUMN:
                    value = dtUtils.formatDate(value.toString(),REQUIRED_DATE_FORMAT);
                    txtDateComponent.setText(value.toString());
                    return txtDateComponent;
                case RATE_COLUMN:
                    txtCurrencyComp.setText(value.toString());
                    return txtCurrencyComp;
                //Added code for case #1748 by tarique start 8
                case UPDATE_TIME :
                    if(value.toString().equals(EMPTY_STRING)){
                        txtComponent.setText(value.toString());
                        return txtComponent;
                    }
                    txtComponent.setText(CoeusDateFormat.format(value.toString()));
                    return txtComponent;
                case UPDATE_USER :
                    if(value.toString().equals(EMPTY_STRING)){
                        txtComponent.setText(value.toString());
                        return txtComponent;
                    }
                    txtComponent.setText(value.toString());
                    return txtComponent;
                //Added code for case #1748 by tarique end 8
            }
            return txtComponent;
        }
        
    }
    
    
    public void itemStateChanged(ItemEvent itemEvent) {
        lARateCellEditor.stopCellEditing();
        String code  = ((ComboBoxBean)cmbRateClass.getSelectedItem()).getCode();
        Object source = itemEvent.getSource();
        if(itemEvent.getStateChange() == ItemEvent.SELECTED) {
            
            if(source ==cmbRateClass){
                cmbRateType.removeItemListener(this);
                cmbRateType.removeAllItems();
                cmbRateType.addItemListener(this);
                
                Equals eqRateClassCode = new Equals("rateClassCode", new Integer(code));
                cvFilteredRateType = cvRateType.filter(eqRateClassCode);
                if( ( cvFilteredRateType != null ) && ( cvFilteredRateType.size() > 0 )){
                    for(int index = 0 ; index < cvFilteredRateType.size(); index++){
                        cmbRateType.removeItemListener(this);
                        cmbRateType.addItem(
                        (RateTypeBean)cvFilteredRateType.elementAt(index));
                        cmbRateType.addItemListener(this);
                    }
                    rateTypeBean = (RateTypeBean)cvFilteredRateType.elementAt(0);
                    cmbRateType.removeItemListener(this);
                    cmbRateType.setSelectedItem(rateTypeBean);
                    cmbRateType.addItemListener(this);
                }
                Equals eqRateClassCodes;
                Equals eqRateTypeCode;
                
                rateClassCode  = ((ComboBoxBean)cmbRateClass.getSelectedItem()).getCode();
                rateTypeCode = ((ComboBoxBean)cmbRateType.getSelectedItem()).getCode();
                
                eqRateClassCodes = new Equals("rateClassCode", new Integer(rateClassCode));
                eqRateTypeCode = new Equals("rateTypeCode", new Integer(rateTypeCode));
                And rateClassAndRateType = new And(eqRateClassCodes,eqRateTypeCode);
                if(cvRates!=null){
                    cvFilteredRates = cvRates.filter(rateClassAndRateType);
                    cvFilteredRates = cvFilteredRates.filter(CoeusVector.FILTER_ACTIVE_BEANS);
                }
                lARatesTableModel.setData(cvFilteredRates);
                //lARatesTableModel.fireTableDataChanged();
            }
            else if(source == cmbRateType){
                Equals eqRateClassCode;
                Equals eqRateTypeCode;
                
                rateClassCode  = ((ComboBoxBean)cmbRateClass.getSelectedItem()).getCode();
                rateTypeCode = ((ComboBoxBean)cmbRateType.getSelectedItem()).getCode();
                
                eqRateClassCode = new Equals("rateClassCode", new Integer(rateClassCode));
                eqRateTypeCode = new Equals("rateTypeCode", new Integer(rateTypeCode));
                And rateClassAndRateType = new And(eqRateClassCode,eqRateTypeCode);
                if(cvRates!=null){
                    cvFilteredRates = cvRates.filter(rateClassAndRateType);
                    cvFilteredRates = cvFilteredRates.filter(CoeusVector.FILTER_ACTIVE_BEANS);
                }
                lARatesTableModel.setData(cvFilteredRates);
                //lARatesTableModel.fireTableDataChanged();
            }
            
            if(tblLARateMaintainance.getRowCount() > 0 ){
                tblLARateMaintainance.setRowSelectionInterval(0,0);
            }
            cvFilteredRates.sort("fiscalYear");
        }//End If Selected
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblRateClass = new javax.swing.JLabel();
        lblRateType = new javax.swing.JLabel();
        jcrPnLARateMaintainance = new javax.swing.JScrollPane();
        tblLARateMaintainance =  new javax.swing.JTable(){
            public void changeSelection(int row, int column, boolean toggle, boolean extend){
                super.changeSelection(row, column, toggle, extend);
                javax.swing.SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        tblLARateMaintainance.dispatchEvent(new java.awt.event.KeyEvent(
                            tblLARateMaintainance,java.awt.event.KeyEvent.KEY_PRESSED,0,0,java.awt.event.KeyEvent.VK_F2,
                            java.awt.event.KeyEvent.CHAR_UNDEFINED) );
                }
            });
        }
    };
    cmbRateClass = new edu.mit.coeus.utils.CoeusComboBox();
    cmbRateType = new edu.mit.coeus.utils.CoeusComboBox();
    btnOk = new javax.swing.JButton();
    btnCancel = new javax.swing.JButton();
    btnAdd = new javax.swing.JButton();
    btnDelete = new javax.swing.JButton();

    setLayout(new java.awt.GridBagLayout());

    setMinimumSize(new java.awt.Dimension(760, 420));
    setPreferredSize(new java.awt.Dimension(760, 420));
    setVerifyInputWhenFocusTarget(false);
    lblRateClass.setFont(CoeusFontFactory.getLabelFont());
    lblRateClass.setText("Rate Class:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(7, 4, 0, 0);
    add(lblRateClass, gridBagConstraints);

    lblRateType.setFont(CoeusFontFactory.getLabelFont());
    lblRateType.setText("Rate Type:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
    add(lblRateType, gridBagConstraints);

    jcrPnLARateMaintainance.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jcrPnLARateMaintainance.setMinimumSize(new java.awt.Dimension(150, 150));
    jcrPnLARateMaintainance.setPreferredSize(new java.awt.Dimension(500, 150));
    tblLARateMaintainance.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {},
            {},
            {},
            {}
        },
        new String [] {

        }
    ));
    tblLARateMaintainance.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            tblLARateMaintainanceMouseClicked(evt);
        }
    });

    jcrPnLARateMaintainance.setViewportView(tblLARateMaintainance);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 4;
    gridBagConstraints.gridheight = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
    add(jcrPnLARateMaintainance, gridBagConstraints);

    cmbRateClass.setMinimumSize(new java.awt.Dimension(210, 20));
    cmbRateClass.setPreferredSize(new java.awt.Dimension(210, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(6, 5, 0, 0);
    add(cmbRateClass, gridBagConstraints);

    cmbRateType.setMinimumSize(new java.awt.Dimension(210, 20));
    cmbRateType.setPreferredSize(new java.awt.Dimension(210, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
    add(cmbRateType, gridBagConstraints);

    btnOk.setFont(CoeusFontFactory.getLabelFont());
    btnOk.setMnemonic('O');
    btnOk.setText("OK");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 4;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 4);
    add(btnOk, gridBagConstraints);

    btnCancel.setFont(CoeusFontFactory.getLabelFont());
    btnCancel.setMnemonic('C');
    btnCancel.setText("Cancel");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 4;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 4);
    add(btnCancel, gridBagConstraints);

    btnAdd.setFont(CoeusFontFactory.getLabelFont());
    btnAdd.setMnemonic('A');
    btnAdd.setText("Add");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 4;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
    gridBagConstraints.insets = new java.awt.Insets(16, 4, 0, 4);
    add(btnAdd, gridBagConstraints);

    btnDelete.setFont(CoeusFontFactory.getLabelFont());
    btnDelete.setMnemonic('D');
    btnDelete.setText("Delete");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 4;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 4);
    add(btnDelete, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
     private void setTableEditors(){
        tblLARateMaintainance.setRowHeight(22);
        tblLARateMaintainance.setShowHorizontalLines(false);
        tblLARateMaintainance.setShowVerticalLines(false);
        JTableHeader tableHeader = tblLARateMaintainance.getTableHeader();
        tableHeader.addMouseListener(new ColumnHeaderListener());
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tblLARateMaintainance.setOpaque(false);
        tblLARateMaintainance.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        
        TableColumn column = tblLARateMaintainance.getColumnModel().getColumn(HAND_ICON_COLUMN);
        column.setPreferredWidth(30);
        column.setCellRenderer(new edu.mit.coeus.utils.IconRenderer());
        column.setHeaderRenderer(new edu.mit.coeus.utils.EmptyHeaderRenderer());
        
        column = tblLARateMaintainance.getColumnModel().getColumn(FISCAL_YEAR_COLUMN);
        column.setPreferredWidth(125);
        column.setResizable(true);
        column.setCellEditor(lARateCellEditor);
        column.setCellRenderer(lARateCellRenderer);
        tableHeader.setReorderingAllowed(false);
        
        column = tblLARateMaintainance.getColumnModel().getColumn(CAMPUS_COLUMN);
        column.setPreferredWidth(85);
        column.setResizable(true);
        column.setCellEditor(lARateCellEditor);
        column.setCellRenderer(lARateCellRenderer);
        tableHeader.setReorderingAllowed(false);
        
        column = tblLARateMaintainance.getColumnModel().getColumn(START_DATE_COLUMN);
        column.setPreferredWidth(100);
        column.setResizable(true);
        column.setCellEditor(lARateCellEditor);
        column.setCellRenderer(lARateCellRenderer);
        tableHeader.setReorderingAllowed(false);
        
        column = tblLARateMaintainance.getColumnModel().getColumn(RATE_COLUMN);
        column.setPreferredWidth(100);
        column.setResizable(true);
        column.setCellEditor(lARateCellEditor);
        column.setCellRenderer(lARateCellRenderer);
        tableHeader.setReorderingAllowed(false);
        

        //Added for case #1748 by tarique start 4
        column = tblLARateMaintainance.getColumnModel().getColumn(UPDATE_TIME);
        column.setPreferredWidth(135);
        column.setResizable(true);
        column.setCellEditor(lARateCellEditor);
        column.setCellRenderer(lARateCellRenderer);
        tableHeader.setReorderingAllowed(false);
        
        column = tblLARateMaintainance.getColumnModel().getColumn(UPDATE_USER);
        column.setPreferredWidth(85);
        column.setResizable(true);
        column.setCellEditor(lARateCellEditor);
        column.setCellRenderer(lARateCellRenderer);
        tableHeader.setReorderingAllowed(false);
        //Added for case #1748 by tarique end 4
        
    }
    public class ColumnHeaderListener extends MouseAdapter {
        String nameBeanId [][] ={
            {"0","" },
            {"1","fiscalYear" },
            {"2","onOffCampusFlag"},
            {"3","startDate" },
            {"4","instituteRate" },
        };
        
     //For the Bug Fix:1724 Column Header sorting start 
//        boolean sort = true;
        boolean sort = false;
        //ENd Bug Fix:1724 Column Header sorting
        /**
         * @param evt
         */
        public void mouseClicked(MouseEvent evt) {
            //try {
                
                JTable table = ((JTableHeader)evt.getSource()).getTable();
                TableColumnModel colModel = table.getColumnModel();
                Object scr = evt.getSource();
                // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(evt.getX());
                int column = colModel.getColumn(vColIndex).getModelIndex();
                if(scr.equals(tblLARateMaintainance.getTableHeader())){
                    CoeusVector cvData =  lARatesTableModel.getData();
                    if(cvData != null && cvData.size()>0 &&
                    nameBeanId [vColIndex][1].length() >1 ){
                        ((CoeusVector)cvData).sort(nameBeanId [vColIndex][1],sort);
                        if(sort)
                            sort = false;
                        else
                            sort = true;
                        lARatesTableModel.fireTableRowsUpdated(0, lARatesTableModel.getRowCount());
                    }
                }
//            } catch(Exception exception) {
//                exception.getMessage();
//            }
            
        }
    }
    private void tblLARateMaintainanceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLARateMaintainanceMouseClicked
        // Add your handling code here:
        javax.swing.SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                tblLARateMaintainance.dispatchEvent(new java.awt.event.KeyEvent(
                tblLARateMaintainance,java.awt.event.KeyEvent.KEY_PRESSED,0,0,java.awt.event.KeyEvent.VK_F2,
                java.awt.event.KeyEvent.CHAR_UNDEFINED) );
            }
    }//GEN-LAST:event_tblLARateMaintainanceMouseClicked
    );
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAdd;
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnDelete;
    public javax.swing.JButton btnOk;
    public edu.mit.coeus.utils.CoeusComboBox cmbRateClass;
    public edu.mit.coeus.utils.CoeusComboBox cmbRateType;
    public javax.swing.JScrollPane jcrPnLARateMaintainance;
    public javax.swing.JLabel lblRateClass;
    public javax.swing.JLabel lblRateType;
    public javax.swing.JTable tblLARateMaintainance;
    // End of variables declaration//GEN-END:variables
     /** This is Iconrendere to display HAND icon for the selected row in the table
     */
    static class IconRenderer  extends DefaultTableCellRenderer {
        
        
        /** This holds the Image Icon of Hand Icon
         */
        private final ImageIcon HAND_ICON =
        new ImageIcon(getClass().getClassLoader().getResource(
        CoeusGuiConstants.HAND_ICON));
        private final ImageIcon EMPTY_ICON = null;
        /** Default Constructor*/
        IconRenderer() {
        }
        
        public Component getTableCellRendererComponent(JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row,
        int column) {
            
            setText((String)value);
            setOpaque(false);
            /* if row is selected the place the icon in this cell wherever this
               renderer is used. */
            if( isSelected ){
                setIcon(HAND_ICON);
            }else{
                setIcon(EMPTY_ICON);
            }
            return this;
        }
        
    }//End Icon Rendering inner class
    
    
    /**
     * Inner class which is used to provide empty header for the Icon Column.
     */
    
    class EmptyHeaderRenderer extends JList implements TableCellRenderer {
        /**
         * Default constructor to set the default foreground/background
         * and border properties of this renderer for a cell.
         */
        EmptyHeaderRenderer() {
            setOpaque(true);
            setForeground(UIManager.getColor("TableHeader.foreground"));
            setBackground(UIManager.getColor("TableHeader.background"));
            setBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0));
            ListCellRenderer renderer = getCellRenderer();
            ((JLabel) renderer).setHorizontalAlignment(JLabel.CENTER);
            setCellRenderer(renderer);
        }
        
        public Component getTableCellRendererComponent(JTable table,
        Object value,boolean isSelected, boolean hasFocus, int row, int column){
            return this;
        }
    }
}
