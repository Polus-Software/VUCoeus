/*
 * InstRateClassTypesController.java
 *
 * Created on August 17, 2004, 11:07 AM
 */

package edu.mit.coeus.rates.controller;

/**
 *
 * @author  nadhgj
 */

import javax.swing.JTable;
import javax.swing.AbstractCellEditor;
import javax.swing.JTextField;
import javax.swing.DefaultListSelectionModel;
import java.awt.event.*;
import java.awt.*;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableColumn;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.BevelBorder;


import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.rates.gui.InstRateClassTypesForm;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.rates.controller.RatesController;
import edu.mit.coeus.rates.bean.*;
import edu.mit.coeus.rates.bean.InstituteRatesBean;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.rates.bean.RateClassBean;
import edu.mit.coeus.utils.CoeusUtils;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.Equals;





public class InstRateClassTypesController extends RatesController implements ActionListener, ListSelectionListener{
    
    //Represents the column header names for the Rate Class table
    private static final int RATECLASS_HAND_COLUMN = 0;
    private static final int RATECLASS_CLASSCODE_COLUMN = 1;
    private static final int RATECLASS_DESCRIPTION_COLUMN = 2;
    private static final int RATECLASS_TYPE_COLUMN=3;
    
    // Represents the column header names for the Rate Type table 
    private static final int RATETYPE_HAND_COLUMN = 0;
    private static final int RATETYPE_CLASSCODE_COLUMN = 1;
    private static final int RATETYPE_DESCRIPTION_COLUMN = 2;
    
    
    private InstRateClassTypesForm instRateClassTypesForm;
    private RateClassCellEditor rateClassCellEditor;
    private RateClassTableModel rateClassTableModel;
    private RateClassTableCellRenderer rateClassTableCellRenderer; 
    private RateTypeCellEditor rateTypeCellEditor;
    private RateTypeTableModel rateTypeTableModel;
    private RateTypeTableCellRenderer rateTypeTableCellRenderer;
    
   //Query engine instance
    private QueryEngine queryEngine;
    
    //holds vector of RateClassBeans
    private CoeusVector cvRateClass,cvRateClassTemp;
    
    //holds vector of RateTypeBeans
    private CoeusVector cvRateType, cvTypeResultsTemp;
    
    //creating mdiform instance
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    private String EMPTY_STRING = "";
    
    private boolean dataChanged;
    
    private char functionType;
    
    //values for comboBox
    private static final String[] code = {"E","L","Y","O","I","V","X"};
    private static final String[] values = {"EB","LA","LA With EB","OH","Inflation","Vacation","Others"};
    
    //instance of RateClassBean
    private RateClassBean rateClassBean;
    
    //holds filtered RateTypeBeans
    private CoeusVector cvTypeResults;
    
    
    private boolean canClassNext = false;
    
    private boolean canClassAdd = true;
    private boolean msgDisplayed;
    //used for incrementing rateclasscode
    private int classNextValue = 1;
    
    //used for incrementing ratetypecode
    private int typeNextValue = 1;
    
    //holds selected row  rateclasscode
    private String rateClassCode;
    
    //CoeusMessageResources instance
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();;
    /** Creates a new instance of InstRateClassTypesController */
    public InstRateClassTypesController(InstituteRatesBean instituteRatesBean, char functionType){
        super(instituteRatesBean);
        this.functionType = functionType;
        this.instituteRatesBean = instituteRatesBean;
        queryEngine = QueryEngine.getInstance();
        registerComponents();
        setTableKeyTraversal();
        setTableKeyRateTypeTraversal();
        setColumnData();
        setFormData(instituteRatesBean);
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        rateClassCellEditor.stopCellEditing();
        rateTypeCellEditor.stopCellEditing();
        msgDisplayed = false;
        if (source.equals(instRateClassTypesForm.btnClassAdd)){
            performAddAction();
        }else if (source.equals(instRateClassTypesForm.btnTypeAdd)){
            performRateTypeAddAction();
        }
    }
    
    //registering components with the listeners and initializing the necessary components
    public void registerComponents() {
        instRateClassTypesForm = new InstRateClassTypesForm();
        instRateClassTypesForm.tblRateClass.setBackground(Color.LIGHT_GRAY);
        // Registering with all the listeners
        instRateClassTypesForm.tblRateClass.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        instRateClassTypesForm.tblRateType.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //instRateClassTypesForm.tblRateClass.setSurrendersFocusOnKeystroke(true);
        //instRateClassTypesForm.tblRateType.setSurrendersFocusOnKeystroke(true);
        instRateClassTypesForm.tblRateClass.getSelectionModel().addListSelectionListener(this);
        instRateClassTypesForm.tblRateType.getSelectionModel().addListSelectionListener(this);
        rateTypeTableModel = new RateTypeTableModel();
        rateClassTableModel = new RateClassTableModel();
        
        // Setting up the table model
        instRateClassTypesForm.tblRateClass.setModel(rateClassTableModel);
        instRateClassTypesForm.tblRateType.setModel(rateTypeTableModel);
        
        // creating the instances of editors and renderers.
        rateTypeCellEditor = new RateTypeCellEditor();
        rateTypeTableCellRenderer = new RateTypeTableCellRenderer();
        rateClassCellEditor = new RateClassCellEditor();
        rateClassTableCellRenderer = new RateClassTableCellRenderer();
        instRateClassTypesForm.btnClassAdd.addActionListener(this);
        instRateClassTypesForm.btnTypeAdd.addActionListener(this);
    }
    
    /* Setting the table header, column width, renderer and editor for the tables 
     * returns void
     */
    private void setColumnData() {
        JTableHeader tableHeader = instRateClassTypesForm.tblRateClass.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tableHeader.addMouseListener(new ColumnHeaderListener());
        
        instRateClassTypesForm.tblRateClass.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        instRateClassTypesForm.tblRateClass.setRowHeight(22);
        instRateClassTypesForm.tblRateClass.setShowHorizontalLines(false);
        instRateClassTypesForm.tblRateClass.setShowVerticalLines(false);
        instRateClassTypesForm.tblRateClass.setOpaque(false);
        instRateClassTypesForm.tblRateClass.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        
        TableColumn column = instRateClassTypesForm.tblRateClass.getColumnModel().getColumn(RATECLASS_HAND_COLUMN);
        column.setMaxWidth(30);
        column.setMinWidth(30);
        column.setPreferredWidth(30);
        column.setResizable(false);
        column.setCellRenderer(new IconRenderer());
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        
        column = instRateClassTypesForm.tblRateClass.getColumnModel().getColumn(RATECLASS_CLASSCODE_COLUMN);
        column.setMinWidth(78);
        column.setPreferredWidth(78);
        column.setResizable(false);
        column.setCellEditor(rateClassCellEditor);
        column.setCellRenderer(rateClassTableCellRenderer);
        
        column = instRateClassTypesForm.tblRateClass.getColumnModel().getColumn(RATECLASS_DESCRIPTION_COLUMN);
        column.setMaxWidth(250);
        column.setMinWidth(250);
        column.setPreferredWidth(250);
        column.setResizable(false);
        column.setCellEditor(rateClassCellEditor);
        column.setCellRenderer(rateClassTableCellRenderer);
        
        column = instRateClassTypesForm.tblRateClass.getColumnModel().getColumn(RATECLASS_TYPE_COLUMN);
        column.setMaxWidth(93);
        column.setMinWidth(93);
        column.setPreferredWidth(93);
        column.setResizable(false);
        column.setCellEditor(rateClassCellEditor);
        column.setCellRenderer(rateClassTableCellRenderer);
        
        
        // Setting the table header, column width, renderer and editor for the 
        // display unit details header
        JTableHeader rateTypeHeader = instRateClassTypesForm.tblRateType.getTableHeader();
        rateTypeHeader.addMouseListener(new ColumnHeaderListener());
        rateTypeHeader.setReorderingAllowed(false);
        rateTypeHeader.setFont(CoeusFontFactory.getLabelFont());
        
        instRateClassTypesForm.tblRateType.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        instRateClassTypesForm.tblRateType.setRowHeight(22);

        instRateClassTypesForm.tblRateType.setShowHorizontalLines(false);
        instRateClassTypesForm.tblRateType.setShowVerticalLines(false);
        instRateClassTypesForm.tblRateType.setOpaque(false);
        instRateClassTypesForm.tblRateType.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        
        TableColumn ratetypeColumn = instRateClassTypesForm.tblRateType.getColumnModel().getColumn(RATETYPE_HAND_COLUMN);
        ratetypeColumn.setMaxWidth(30);
        ratetypeColumn.setMinWidth(30);
        ratetypeColumn.setPreferredWidth(30);
        ratetypeColumn.setResizable(false);
        ratetypeColumn.setCellRenderer(new IconRenderer());
        ratetypeColumn.setHeaderRenderer(new EmptyHeaderRenderer());
        
        ratetypeColumn = instRateClassTypesForm.tblRateType.getColumnModel().getColumn(RATETYPE_CLASSCODE_COLUMN);
        ratetypeColumn.setMaxWidth(78);
        ratetypeColumn.setMinWidth(78);
        ratetypeColumn.setPreferredWidth(78);
        ratetypeColumn.setResizable(false);
        ratetypeColumn.setCellRenderer(rateTypeTableCellRenderer);
        ratetypeColumn.setCellEditor(rateTypeCellEditor);
        
        ratetypeColumn = instRateClassTypesForm.tblRateType.getColumnModel().getColumn(RATETYPE_DESCRIPTION_COLUMN);
        ratetypeColumn.setMaxWidth(343 );
        ratetypeColumn.setMinWidth(343);
        ratetypeColumn.setPreferredWidth(343);
        ratetypeColumn.setResizable(false);
        ratetypeColumn.setCellRenderer(rateTypeTableCellRenderer);
        ratetypeColumn.setCellEditor(rateTypeCellEditor);
    }// end of setting 
    
    
    
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if(cvRateClass != null && cvRateClass.size() > 0){
            if( !listSelectionEvent.getValueIsAdjusting()){ 
                ListSelectionModel source = (ListSelectionModel)listSelectionEvent.getSource();
                if (source.equals(instRateClassTypesForm.tblRateType.getSelectionModel())) {
                    rateTypeCellEditor.stopCellEditing();
                    msgDisplayed = false;
                }
                if(msgDisplayed ) return;
                int selRow=0;
                if (source.equals(instRateClassTypesForm.tblRateClass.getSelectionModel())) {
                    selRow = instRateClassTypesForm.tblRateClass.getSelectedRow();
                    if (selRow != -1) {
                        rateClassCellEditor.stopCellEditing();
                        rateClassBean= (RateClassBean)cvRateClass.get(selRow);
                        rateClassCode = (String)instRateClassTypesForm.tblRateClass.getValueAt(selRow,1);
                        Equals eqFilter = new Equals("rateClassCode",new Integer(rateClassCode));
                        cvTypeResults = cvRateType.filter(eqFilter);
                        cvTypeResultsTemp = cvRateType.filter(eqFilter);
                        rateTypeCellEditor.stopCellEditing();
                        typeNextValue =0;
                        rateTypeTableModel.setData(cvTypeResults);
                        rateTypeTableModel.fireTableDataChanged();
                        if(cvTypeResults.size() > 0 && cvTypeResults != null) {
                            instRateClassTypesForm.tblRateType.setRowSelectionInterval(0,0);
                        }
                    }
                }
                
            }
        }
    }
    
    
    
    /** This class will sort the column values in ascending and descending order
     *based on number of clicks. 
     */
    
public class ColumnHeaderListener extends MouseAdapter {
    String nameBeanId [][] ={
        {"0","" },
        {"1","rateClassCode" },
        {"2","description"},
        {"3","rateClassType" },
        {"4","rateTypeCode"}
    };
    boolean sort =true;
    /**
     * @param evt
     */        
    public void mouseClicked(MouseEvent evt) {
        try {
            JTable table = ((JTableHeader)evt.getSource()).getTable();
            TableColumnModel colModel = table.getColumnModel();
            Object scr = evt.getSource();
            // The index of the column whose header was clicked
            int vColIndex = colModel.getColumnIndexAtX(evt.getX());
            if(scr.equals(instRateClassTypesForm.tblRateClass.getTableHeader())){
                rateClassCellEditor.stopCellEditing();
               if(cvRateClass!=null && cvRateClass.size()>0 &&
                nameBeanId [vColIndex][1].length() >1 ){
                    ((CoeusVector)cvRateClass).sort(nameBeanId [vColIndex][1],sort);
                    if(sort)
                        sort = false;
                    else
                        sort = true;
                    rateClassTableModel.fireTableRowsUpdated(0, rateClassTableModel.getRowCount());
                    instRateClassTypesForm.tblRateClass.clearSelection();
                    instRateClassTypesForm.tblRateClass.setRowSelectionInterval(0, 0);
               }
            }
            if(scr.equals(instRateClassTypesForm.tblRateType.getTableHeader())){
                rateTypeCellEditor.stopCellEditing();
                cvTypeResults = rateTypeTableModel.getData();

               if(cvTypeResults!=null && cvTypeResults.size()>0 &&
                nameBeanId [vColIndex][1].length() >1 ){
                    if(vColIndex == 1) vColIndex = 4;
                    ((CoeusVector)cvTypeResults).sort(nameBeanId [vColIndex][1],sort);
                    if(sort)
                        sort = false;
                    else
                        sort = true;
                    rateTypeTableModel.fireTableRowsUpdated(0, rateTypeTableModel.getRowCount());
                }
            }
        } catch(Exception exception) {
                exception.getMessage();
        }
    }
}// End of ColumnHeaderListener.................
    
    
     
    public void display() {
        
    }
    
    public void formatFields() {
        
        if(functionType == CoeusGuiConstants.DISPLAY_MODE){

            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        
            instRateClassTypesForm.tblRateType.setBackground(bgListColor);    
            instRateClassTypesForm.tblRateType.setSelectionBackground(bgListColor );
            instRateClassTypesForm.tblRateType.setSelectionForeground(java.awt.Color.BLACK);            
            
            instRateClassTypesForm.tblRateClass.setBackground(bgListColor);    
            instRateClassTypesForm.tblRateClass.setSelectionBackground(bgListColor );
            instRateClassTypesForm.tblRateClass.setSelectionForeground(java.awt.Color.BLACK);
            instRateClassTypesForm.btnClassAdd.setEnabled(false);
            instRateClassTypesForm.btnTypeAdd.setEnabled(false);
        }
        else{
            instRateClassTypesForm.tblRateType.setBackground(java.awt.Color.white);            
            instRateClassTypesForm.tblRateType.setSelectionBackground(java.awt.Color.white);
            instRateClassTypesForm.tblRateType.setSelectionForeground(java.awt.Color.black);            
            
            instRateClassTypesForm.tblRateClass.setBackground(java.awt.Color.white);            
            instRateClassTypesForm.tblRateClass.setSelectionBackground(java.awt.Color.white);
            instRateClassTypesForm.tblRateClass.setSelectionForeground(java.awt.Color.black);
            instRateClassTypesForm.btnClassAdd.setEnabled(true);
            instRateClassTypesForm.btnTypeAdd.setEnabled(true);
        }
    }
    
    /* returns InstRateTypesFrom instance
     *@ return Component.
     */
    public Component getControlledUI() {
        return instRateClassTypesForm;
    }
    
    
    public Object getFormData() {
        return null;
    }
    
    /* saves form data into the query engine
     *@ return void.
     */
    public void saveFormData() throws CoeusException {
       rateClassCellEditor.stopCellEditing();
       rateTypeCellEditor.stopCellEditing();
        if( dataChanged ) {
           if( cvRateClass != null ) {
                String classAcType = null;
                int rateClassCount = cvRateClass.size();
                for( int indx = 0; indx < rateClassCount; indx++) {
                    RateClassBean rateBean = (RateClassBean)cvRateClass.get(indx);
                    classAcType = rateBean.getAcType();
                    if(TypeConstants.UPDATE_RECORD.equals(classAcType) ){
                        queryEngine.update(queryKey, KeyConstants.RATE_CLASS_DATA, rateBean);
                    }else if( TypeConstants.INSERT_RECORD.equals(classAcType)){
                        queryEngine.insert(queryKey, KeyConstants.RATE_CLASS_DATA, rateBean);
                    }
                }
           }
           if(cvRateType != null ) {
              for( int typeIndx = 0; typeIndx < cvRateType.size(); typeIndx++){
                    RateTypeBean rateTypeBean = 
                        (RateTypeBean)cvRateType.get(typeIndx);
                    String typeAcType = rateTypeBean.getAcType();
                    if( TypeConstants.UPDATE_RECORD.equals(typeAcType) ){
                        queryEngine.update(queryKey, KeyConstants.RATE_TYPE_DATA, rateTypeBean);
                    }else if( TypeConstants.INSERT_RECORD.equals(typeAcType)){
                        queryEngine.insert(queryKey, KeyConstants.RATE_TYPE_DATA,rateTypeBean);
                    }
                }

           }
        }
    }
    
    /* sets data to the form
     *@ retun void
     */
    public void setFormData(Object instituteRatesBean) {
        try{
        instituteRatesBean  = (InstituteRatesBean)instituteRatesBean;
            CoeusVector cvTypeResults = new CoeusVector();
            cvRateClass = queryEngine.getDetails(queryKey, KeyConstants.RATE_CLASS_DATA);
            cvRateType = queryEngine.getDetails(queryKey, KeyConstants.RATE_TYPE_DATA);
            
            cvRateClassTemp = queryEngine.getDetails(queryKey, KeyConstants.RATE_CLASS_DATA);
            rateClassTableModel.setData(cvRateClass);
            rateClassCellEditor.stopCellEditing();
            
            if(cvRateClass != null && cvRateClass.size() > 0) {
                rateClassCode = (String)instRateClassTypesForm.tblRateClass.getValueAt(0,1);
                Equals eqFilter = new Equals("rateClassCode",new Integer(rateClassCode));
                cvTypeResults = cvRateType.filter(eqFilter);
                cvTypeResultsTemp = cvRateType.filter(eqFilter);
                rateTypeCellEditor.stopCellEditing();
                rateTypeTableModel.setData(cvTypeResults);
                if(cvTypeResults.size() > 0 && cvTypeResults != null) {
                    instRateClassTypesForm.tblRateClass.setRowSelectionInterval(0,0);
                    instRateClassTypesForm.tblRateType.setRowSelectionInterval(0,0);
                }
            }
          
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
        formatFields();
    }


    
    /* validate the form data
     *@ return boolean
     */
    public boolean validate() {
        if(dataChanged) {
            rateClassCellEditor.stopCellEditing();
            rateTypeCellEditor.stopCellEditing();
            CoeusVector cvOtherResults,cvInflationResults,cvVacationResults;
            CoeusVector cvTableData = rateClassTableModel.getData();
            RateClassBean compareClassBean;
            RateTypeBean compareTypeBean;
            String rateClassType = "E";
            int count = 0;
            Equals eqFilter = new Equals("rateClassType",rateClassType);
            cvOtherResults = cvTableData.filter(eqFilter);
            eqFilter = null;
            rateClassType = "I";
            eqFilter = new Equals("rateClassType",rateClassType);
            cvInflationResults = cvTableData.filter(eqFilter);
            eqFilter = null;
            rateClassType = "V";
            eqFilter = new Equals("rateClassType",rateClassType);
            cvVacationResults = cvTableData.filter(eqFilter);
            eqFilter = null;
            for(int index = 0 ; index < cvTableData.size(); index++) {
                compareClassBean = (RateClassBean)cvTableData.get(index);
                if(EMPTY_STRING.equals(compareClassBean.getDescription())) {
                    if(!canClassAdd && !msgDisplayed) 
                    CoeusOptionPane.showInfoDialog("Enter the description. ");
                    instRateClassTypesForm.tblRateClass.requestFocusInWindow();
                    setRequestFocusInClassThread(index,2);
                    instRateClassTypesForm.tblRateClass.clearSelection();
                    instRateClassTypesForm.tblRateClass.setRowSelectionInterval(index,index);
                    return false;
                }
                cvTypeResults = cvRateType.filter(new Equals("rateClassCode", new Integer(compareClassBean.getRateClassCode())));
                for(int indx = 0 ; indx < cvTypeResults.size(); indx++) {
                compareTypeBean = (RateTypeBean)cvTypeResults.get(indx);
                    if(EMPTY_STRING.equals(compareTypeBean.getDescription())) {
                    if(!canClassAdd && !msgDisplayed )
                        CoeusOptionPane.showInfoDialog("Enter the description. ");
                    instRateClassTypesForm.tblRateClass.requestFocusInWindow();
                    instRateClassTypesForm.tblRateClass.clearSelection();
                    instRateClassTypesForm.tblRateClass.setRowSelectionInterval(index,index);
                    ListSelectionEvent listSelectionEvent = new ListSelectionEvent(instRateClassTypesForm.tblRateClass.getSelectionModel(), index, index, false);
                    valueChanged(listSelectionEvent);
                    instRateClassTypesForm.tblRateType.requestFocusInWindow();
                    setRequestFocusInTypeThread(indx,2);
                    instRateClassTypesForm.tblRateType.setRowSelectionInterval(indx,indx);
                    return false;
                    }
                }
            }
            if(cvOtherResults.size() > 1) {
                rateClassType = "E";
                CoeusOptionPane.showInfoDialog("Two Rate Classes cannot be of type EB");
                for(int index = 0 ; index < cvTableData.size(); index++) {
                    compareClassBean = (RateClassBean)cvTableData.get(index);
                    if(rateClassType.equals(compareClassBean.getRateClassType())) {
                        count++;
                        if(count > 1) {
                            instRateClassTypesForm.tblRateClass.requestFocusInWindow();
                            instRateClassTypesForm.tblRateClass.editCellAt(index,RATECLASS_TYPE_COLUMN);
                            instRateClassTypesForm.tblRateClass.getEditorComponent().requestFocusInWindow();
                            instRateClassTypesForm.tblRateClass.setRowSelectionInterval(index,index);
                            return false;
                        }
                    }
                }
                return false;
            }else if(cvInflationResults.size() > 1) {
                CoeusOptionPane.showInfoDialog("The Rate Class can not be of type Inflation");
                rateClassType = "I";
                for(int index = 0 ; index < cvTableData.size(); index++) {
                    compareClassBean = (RateClassBean)cvTableData.get(index);
                    if(rateClassType.equals(compareClassBean.getRateClassType())) {
                        count++;
                        if(count > 1) {
                            instRateClassTypesForm.tblRateClass.requestFocusInWindow();
                            instRateClassTypesForm.tblRateClass.editCellAt(index,RATECLASS_TYPE_COLUMN);
                            instRateClassTypesForm.tblRateClass.getEditorComponent().requestFocusInWindow();
                            instRateClassTypesForm.tblRateClass.setRowSelectionInterval(index,index);
                            return false;
                        }
                    }
                }
                return false;
            }else if(cvVacationResults.size() > 1) {
                CoeusOptionPane.showInfoDialog("The Rate Class can not be of type Vacation");
                rateClassType = "V";
                for(int index = 0 ; index < cvTableData.size(); index++) {
                    compareClassBean = (RateClassBean)cvTableData.get(index);
                    if(rateClassType.equals(compareClassBean.getRateClassType())) {
                        count++;
                        if(count > 1) {
                            instRateClassTypesForm.tblRateClass.requestFocusInWindow();
                            instRateClassTypesForm.tblRateClass.editCellAt(index,RATECLASS_TYPE_COLUMN);
                            instRateClassTypesForm.tblRateClass.getEditorComponent().requestFocusInWindow();
                            instRateClassTypesForm.tblRateClass.setRowSelectionInterval(index,index);
                        }
                    }
                }
                return false;
            }
        }
        return true;
    }
    
   /*adds a new row at the end of the tblRateClass with incremented code and type as OH
    *@ returns void
    */
   private void performAddAction() {
        if(!canClassAdd ) return;
        canClassAdd = false;
        RateClassBean newBean= new RateClassBean();
        newBean.setRateClassCode(classNextValue());
        newBean.setDescription("");
        newBean.setRateClassType("O");
        
        newBean.setAcType(TypeConstants.INSERT_RECORD);
        dataChanged = true;
        cvRateClass  = rateClassTableModel.getData();
        if( cvRateClass == null ) {
            cvRateClass = new CoeusVector();
        }
        cvRateClass.add(newBean);
        rateClassTableModel.fireTableRowsInserted(
            rateClassTableModel.getRowCount()+1,rateClassTableModel.getRowCount() + 1);
        
        int lastRow = instRateClassTypesForm.tblRateClass.getRowCount() - 1;
        if (lastRow >= 0){
            instRateClassTypesForm.tblRateClass.setRowSelectionInterval(lastRow,lastRow);
            instRateClassTypesForm.tblRateClass.scrollRectToVisible(
            instRateClassTypesForm.tblRateClass.getCellRect(lastRow, 
                                                RATECLASS_DESCRIPTION_COLUMN, true));
        }
        instRateClassTypesForm.tblRateClass.requestFocusInWindow();
        instRateClassTypesForm.tblRateClass.editCellAt(lastRow,RATECLASS_DESCRIPTION_COLUMN);
        instRateClassTypesForm.tblRateClass.getEditorComponent().requestFocusInWindow();
    }
   
   /* increment the tblrateclass code 
    *@ return int
    */
   private int classNextValue() {
       if(!canClassNext) {
           if(cvRateClassTemp.size() > 0 && cvRateClassTemp != null) {
               cvRateClassTemp.sort("rateClassCode",false);
               RateClassBean bean = (RateClassBean)cvRateClassTemp.get(0);
               classNextValue = bean.getRateClassCode();
               classNextValue = classNextValue+1;
               canClassNext = true;
               return classNextValue;
           }else {
               return classNextValue;
           }
       } else {
           classNextValue = classNextValue+1;
           return classNextValue;
       }
       
   }
   
   /* increment the tblrateclass code 
    *@ return int
    */
   private int typeNextValue() {

       if(cvTypeResults.size() > 0 && cvTypeResults != null) {
               cvTypeResults.sort("rateTypeCode",false);
               RateTypeBean bean = (RateTypeBean)cvTypeResults.get(0);
               cvTypeResults.sort("rateTypeCode",true);
               typeNextValue = bean.getRateTypeCode();
               typeNextValue = typeNextValue+1;
               return typeNextValue;

       } else {
           typeNextValue = typeNextValue+1;
           return typeNextValue;
       }
       
   }
   
   /*adds a new row at the end of the tblRatetype with incremented code
    *@ returns void
    */
   private void performRateTypeAddAction() {
        if(!canClassAdd) 
        return;
        cvTypeResults = rateTypeTableModel.getData();
        RateTypeBean newBean = new RateTypeBean();
        int incValue = typeNextValue();
        newBean.setCode(Integer.toString(incValue));
        newBean.setRateTypeCode(incValue);
        newBean.setDescription(EMPTY);
        newBean.setAcType(TypeConstants.INSERT_RECORD);
        newBean.setRateClassCode(Integer.parseInt(rateClassCode));
        dataChanged = true;
        if( cvTypeResults == null ) {
            cvTypeResults = new CoeusVector();
        }
        cvRateType.add(newBean);
        cvTypeResults.add(newBean);
        rateTypeTableModel.fireTableRowsInserted(
        rateTypeTableModel.getRowCount()+1,rateTypeTableModel.getRowCount() + 1);

        int lastRow = instRateClassTypesForm.tblRateType.getRowCount() - 1;
        if (lastRow >= 0){
            instRateClassTypesForm.tblRateType.setRowSelectionInterval(lastRow,lastRow);
            instRateClassTypesForm.tblRateType.scrollRectToVisible(
            instRateClassTypesForm.tblRateType.getCellRect(lastRow, 
                                                RATETYPE_DESCRIPTION_COLUMN, true));
        }
        instRateClassTypesForm.tblRateType.requestFocusInWindow();
        instRateClassTypesForm.tblRateType.editCellAt(lastRow,RATETYPE_DESCRIPTION_COLUMN);
        instRateClassTypesForm.tblRateType.getEditorComponent().requestFocusInWindow();
   }
        
   
   
   public class RateClassTableModel extends AbstractTableModel {
       
    private CoeusVector cvRateClassData = new CoeusVector();
  //  private RateClassBean rateClassBean;
    private String[] colNames = {EMPTY_STRING, "Type Code","Description","Type"};
    /** Specifies the column class and its data types as objects */
    private Class colClass[] = {ImageIcon.class, Double.class, String.class, String.class};
    
    /**
     * Creates a new instance of RateClassTableModel
     */
    public RateClassTableModel() {
    }
    /**
     *This method is to check whether the specified cell is editable or not
     *@param int row
     *@param int col
     *@return boolean
     */
    public boolean isCellEditable(int row, int col) {
        if(col==0 || col==1 || functionType==TypeConstants.DISPLAY_MODE ){
            return false;
        }else{
            return true;
        }
    }
    /**
     *This method is to get the column name
     *@param int column
     *@return String
     */
    public String getColumnName(int column) {
        return colNames[column];
    }
    
    /**
     *This method is to get the column class
     *@param int columnIndex
     *@return Class
     */
    public Class getColumnClass(int columnIndex) {
        return colClass [columnIndex];
    }
    
    /**
     *This method is to get the column count
     *@return int
     */
    public int getColumnCount() {
        return colNames.length;
    }
    
    /**
     *This method is to get the row count
     *@return int
     */
    public int getRowCount() {
        if (cvRateClassData == null) return 0;
        return cvRateClassData.size();
    }
    
    /**
     *This method will specifies the data for the table model. Depending upon the value
     *passed, it will hold the Award or Institute Proposal Detail vestor
     *@param CoeusVector cvData
     *@return void
     */
    public void setData(CoeusVector cvData){
        cvRateClassData = cvData;
    }
    
    /**
     *This method is to get the value with respect to the row and column
     *@param int row
     *@param int col
     *@return Object
     */
    public Object getValueAt(int row, int col) {
           rateClassBean = (RateClassBean) cvRateClassData.get(row);
        switch(col) {
            case RATECLASS_HAND_COLUMN:
                return EMPTY_STRING;
            case RATECLASS_CLASSCODE_COLUMN:
                return Integer.toString(rateClassBean.getRateClassCode());
            case RATECLASS_DESCRIPTION_COLUMN:
                return rateClassBean.getDescription();
            case RATECLASS_TYPE_COLUMN:
                String typeCode=rateClassBean.getRateClassType();
                for(int index=0;index<code.length;index++) {
                    if(typeCode.equals(code[index])){
                       ComboBoxBean comboBoxBean = new ComboBoxBean(code[index],values[index]);
                        return comboBoxBean;
                    }
                }
        }
        return EMPTY_STRING;
    }
    
    /**
     *This method is to set the value with respect to the row and column
     *@param Object value
     *@param int row
     *@param int col
     *@return void
     */
    public void setValueAt(Object value, int row, int col) {
        rateClassBean = (RateClassBean) cvRateClassData.get(row);
        String acType = rateClassBean.getAcType();
        switch(col) {
            case RATECLASS_HAND_COLUMN:
                break;
            case RATECLASS_CLASSCODE_COLUMN:
                if(Integer.parseInt(value.toString().trim())!= rateClassBean.getRateClassCode()) {
                    rateClassBean.setRateClassCode(Integer.parseInt(value.toString().trim()));
                }
                break;
            case RATECLASS_DESCRIPTION_COLUMN:
                if (!value.toString().trim().equals(rateClassBean.getDescription().trim())){
                        rateClassBean.setDescription(value.toString().trim());
                        dataChanged = true;
                        canClassAdd = true;
                        if( null == acType ) { 
                            rateClassBean.setAcType(TypeConstants.UPDATE_RECORD);
                        }

                }
                
                if (value == null || value.toString().trim().equals(EMPTY_STRING)){
                    canClassAdd = false;
                    if( msgDisplayed) {
                        msgDisplayed = false;
                        setRequestFocusInClassThread(row,col);
                        return ;
                    }
                     
                   rateClassCellEditor.cancelCellEditing();
                   CoeusOptionPane.showInfoDialog("Enter The description.");
                   
                   msgDisplayed = true;
                   setRequestFocusInClassThread(row,col);
                   return ;
                }
                
               break;
            case RATECLASS_TYPE_COLUMN:
                for(int index=0;index<code.length;index++) {
                    if((value.toString()).equals(values[index])){
                        ComboBoxBean comboBoxBean = new ComboBoxBean(code[index],values[index]);
                        if (!value.toString().trim().equals(rateClassBean.getRateClassType().trim())){
                            dataChanged = true;
                            rateClassBean.setRateClassType(comboBoxBean.getCode());
                                if( null == acType ) { 
                                    rateClassBean.setAcType(TypeConstants.UPDATE_RECORD);
                                }
                        }
                    }
                }
             break; 
        }
        
    }
    
    public CoeusVector getData(){
        return cvRateClassData;
    }
    
    
   }
 private void setRequestFocusInClassThread(final int selrow , final int selcol){
        SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            instRateClassTypesForm.tblRateClass.requestFocusInWindow();
            instRateClassTypesForm.tblRateClass.changeSelection( selrow, selcol, true, false);
        }
        });
    } 
   public class RateClassCellEditor extends AbstractCellEditor implements TableCellEditor{
    //Represents the code field
    public JLabel lblCodeComponent;
    //Represents the textfield
    public CoeusTextField txtDescription;
    //Represents the combobox
    public CoeusComboBox cmbType;
    //Represnts the column
    private int column;
    //Represents the empty string
    private boolean typeComboPopulated = false;
    /**
     * Constructor
     */
    public RateClassCellEditor() {
        txtDescription = new CoeusTextField();
        txtDescription.setDocument(new LimitedPlainDocument(200));
        lblCodeComponent = new JLabel();
        cmbType = new CoeusComboBox();
    }
    
    private void populateTypeCombo() {
        ComboBoxBean comboBoxBean;
        for(int i = 0; i<code.length;i++){
            comboBoxBean = new ComboBoxBean(code[i],values[i]);
            cmbType.addItem(comboBoxBean);
        }
           
    } 
    
    /**
     *This method is to get the table cell editor component
     * @param JTable table
     * @param Object value
     * @param boolean isSelected
     * @param int row
     * @param int column
     * @return Component
     */
    public Component getTableCellEditorComponent(JTable table, Object value,
    boolean isSelected, int row, int column) {
        this.column = column;
        
        switch (column) {
            case RATECLASS_CLASSCODE_COLUMN:
                if(value.toString()!= null && !value.toString().trim().equals("")){
                    lblCodeComponent.setText(value.toString());
                }else{
                    lblCodeComponent.setText("");
                }
                return lblCodeComponent;
            case RATECLASS_DESCRIPTION_COLUMN:
                if(value==null || value.toString().equals(EMPTY_STRING)){
                    txtDescription.setText(EMPTY_STRING);
                }else{
                    txtDescription.setText(value.toString());
                }
                instRateClassTypesForm.tblRateClass.setRowSelectionInterval(row,row);
                return txtDescription;
            case RATECLASS_TYPE_COLUMN:
                if(! typeComboPopulated) {
                    populateTypeCombo();
                    typeComboPopulated = true;
                }
                cmbType.setSelectedItem(value);
                instRateClassTypesForm.tblRateClass.setRowSelectionInterval(row,row);
                return cmbType;
        }
        
        return cmbType;
    }
    /**
     * This method is to get the cell editor value
     * @return Object
     */
    public Object getCellEditorValue() {
        switch(column){
                case RATECLASS_CLASSCODE_COLUMN:
                    return lblCodeComponent.getText();
                case RATECLASS_DESCRIPTION_COLUMN:
                    return txtDescription.getText();
                case RATECLASS_TYPE_COLUMN:
                    return cmbType.getSelectedItem();    
        }
        return cmbType;
    }
   }// End Rate class Editor
   
   public class RateClassTableCellRenderer extends DefaultTableCellRenderer implements TableCellRenderer {
    
    private CoeusTextField txtDescriptionComponent,cmbText;
    private JLabel lblText;
    private Color displayColor;
    
    
    /** Constructor */
    public RateClassTableCellRenderer() {
        BevelBorder bevelBorder = new BevelBorder(BevelBorder.LOWERED, Color.white,Color.lightGray, Color.black, Color.lightGray);
        setBorder(bevelBorder);
        lblText = new JLabel();
        lblText.setOpaque(true);
        lblText.setHorizontalAlignment(JLabel.LEFT);
        txtDescriptionComponent = new CoeusTextField();
        lblText.setBorder(new javax.swing.border.BevelBorder(BevelBorder.LOWERED, Color.white,Color.lightGray, Color.black, Color.lightGray));
        cmbText = new CoeusTextField();
        
        displayColor = (Color)UIManager.getDefaults().get("Panel.background");
    }
    
    /**
     * @param JTable table
     * @param Object value
     * @param boolean isSelected
     * @param boolean hasFocus
     * @param boolean hasFocus
     * @param int row
     * @param int col
     * @return Component
     */
    
    public Component getTableCellRendererComponent(JTable table,Object value,
    boolean isSelected,boolean hasFocus ,int row, int col) {
        switch (col) {
            
            case RATECLASS_CLASSCODE_COLUMN:
                lblText.setForeground(java.awt.Color.BLACK);
                lblText.setText(value.toString());
                return lblText;
            case RATECLASS_DESCRIPTION_COLUMN:
                if( functionType == CoeusGuiConstants.DISPLAY_MODE ) {
                    txtDescriptionComponent.setBackground(displayColor);
                    txtDescriptionComponent.setForeground(java.awt.Color.black);
                }else {
                txtDescriptionComponent.setBackground(java.awt.Color.white);
                txtDescriptionComponent.setForeground(java.awt.Color.black);
                }
                if(value==null || value.toString().equals(EMPTY_STRING)){
                    txtDescriptionComponent.setText(null);
                }else{
                    txtDescriptionComponent.setText(value.toString());
                }
                return txtDescriptionComponent;
            case RATECLASS_TYPE_COLUMN:
                if( functionType == CoeusGuiConstants.DISPLAY_MODE ) {
                    cmbText.setBackground(displayColor);
                    cmbText.setForeground(java.awt.Color.black);
                }else {
                cmbText.setBackground(java.awt.Color.white);
                cmbText.setForeground(java.awt.Color.black);
                }
                cmbText.setText(value.toString());
                return cmbText;
        }
        return cmbText;
    }
   }
   
   public class RateTypeTableModel extends AbstractTableModel {
    
    /**
     * Creates a new instance of RateTypeTableModel
     */
    public RateTypeTableModel() {
    }
    // Represents the Coeus Vector
    private CoeusVector cvRateTypeData;
    
    /** Specifies the column Names */
    private String[] colNames = {EMPTY_STRING, "Type Code","Description"};
    
    /** Specifies the column class and its data types as objects */
    private Class colClass[] = {ImageIcon.class, Double.class, String.class};
    
    private RateTypeBean rateTypeBean;
    /**
     *This method is to check whether the specified cell is editable or not
     *@param int row
     *@param int col
     *@return boolean
     */
   public boolean isCellEditable(int row, int col) {
        
        if(col==0 || col==1 || functionType==TypeConstants.DISPLAY_MODE ){
            return false;
        }else{
        return true;
        }
    }
    /**
     *This method is to get the column name
     *@param int column
     *@return String
     */
    public String getColumnName(int column) {
        return colNames[column];
    }
    
    /**
     *This method is to get the column class
     *@param int columnIndex
     *@return Class
     */
    public Class getColumnClass(int columnIndex) {
        return colClass [columnIndex];
    }
    
    /**
     *This method is to get the column count
     *@return int
     */
    public int getColumnCount() {
        return colNames.length;
    }
    
    /**
     *This method is to get the row count
     *@return int
     */
    public int getRowCount() {
        if (cvRateTypeData == null) return 0;
        return cvRateTypeData.size();
    }
    
    /**
     *This method will specifies the data for the table model. Depending upon the value
     *passed, it will hold the Award or Institute Proposal Detail vestor
     *@param CoeusVector cvData
     *@return void
     */
    public void setData(CoeusVector cvData){
        cvRateTypeData = cvData;
    }
    
    /**
     *This method is to get the value with respect to the row and column
     *@param int row
     *@param int col
     *@return Object
     */
    public Object getValueAt(int row, int col) {
           rateTypeBean = (RateTypeBean) cvRateTypeData.get(row);
        switch(col) {
            case RATETYPE_HAND_COLUMN:
                return EMPTY_STRING;
            case RATETYPE_CLASSCODE_COLUMN:
                return rateTypeBean.getCode();
            case RATETYPE_DESCRIPTION_COLUMN:
                return rateTypeBean.getDescription();
            
        }
        return EMPTY_STRING;
    }
    

    
    /**
     *This method is to set the value with respect to the row and column
     *@param Object value
     *@param int row
     *@param int col
     *@return void
     */
    public void setValueAt(Object value, int row, int col) {
        rateTypeBean = (RateTypeBean) cvRateTypeData.get(row);
                
        String acType = rateTypeBean.getAcType();
        switch(col) {
            case RATECLASS_HAND_COLUMN:
                break;
            case RATETYPE_CLASSCODE_COLUMN:
                if(!(value.toString().trim()).equals(rateTypeBean.getCode())) {
                    rateTypeBean.setCode(value.toString().trim());
                    dataChanged = true;
                }
                break;
            case RATETYPE_DESCRIPTION_COLUMN:
                if (!value.toString().trim().equals(rateTypeBean.getDescription().trim())){
                        rateTypeBean.setDescription(value.toString().trim());
                        dataChanged = true;
                        canClassAdd = true;
                        if( acType == null) { 
                            rateTypeBean.setAcType(TypeConstants.UPDATE_RECORD);
                        }

                }
                
                if (value== null || value.toString().trim().equals(EMPTY_STRING)){
                    canClassAdd = false;
                    if( msgDisplayed) {
                        msgDisplayed = false;
                        setRequestFocusInTypeThread(row,col);
                        return ;
                    }
                   rateTypeCellEditor.cancelCellEditing();
                   CoeusOptionPane.showInfoDialog("Enter The description.");
                   
                   msgDisplayed = true;
                   setRequestFocusInTypeThread(row,col);
                   return ;
                }
                break;
        }
    }
    
    public CoeusVector getData(){
        return cvRateTypeData;
    }
   }
   
   private void setRequestFocusInTypeThread(final int selrow , final int selcol){
        SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            instRateClassTypesForm.tblRateType.changeSelection( selrow, selcol, true, false);
            instRateClassTypesForm.tblRateType.requestFocusInWindow();
        }
        });
    }
   
   public class RateTypeCellEditor extends AbstractCellEditor implements TableCellEditor{
    //Represents the currency field
    JLabel lblCodeComponent;
    //Represents the textfield
    CoeusTextField txtDescription;
    //Represnts the column
    private int column;
    
    /**
     * Constructor
     */
    public RateTypeCellEditor() {
        txtDescription = new CoeusTextField();
        lblCodeComponent = new JLabel();
//        txtDescription.setBorder(new javax.swing.border.BevelBorder(BevelBorder.LOWERED, Color.white,Color.lightGray, Color.black, Color.lightGray));
        lblCodeComponent.setBorder(new javax.swing.border.BevelBorder(BevelBorder.LOWERED, Color.white,Color.lightGray, Color.black, Color.lightGray));
        postRegisterComponents();
    }
    
    
    
    /**
     * This method is to register the components
     * @return void
     */
    private void postRegisterComponents() {
        txtDescription.setDocument(new LimitedPlainDocument(200));
    }
    
    public void setViewRateTypeDetailsListener(MouseListener listener) {
        txtDescription.addMouseListener(listener);
    }
    
    /**
     *This method is to get the table cell editor component
     * @param JTable table
     * @param Object value
     * @param boolean isSelected
     * @param int row
     * @param int column
     * @return Component
     */
    public Component getTableCellEditorComponent(JTable table, Object value,
    boolean isSelected, int row, int column) {
        this.column = column;
        
        switch (column) {
            case RATETYPE_CLASSCODE_COLUMN:
                lblCodeComponent.setText((String)value);
                return lblCodeComponent;
            case RATETYPE_DESCRIPTION_COLUMN:
                if(value==null || value.toString().equals(EMPTY_STRING)){
                    txtDescription.setText(null);
                }else{
                    txtDescription.setText(value.toString());
                }
                instRateClassTypesForm.tblRateType.setRowSelectionInterval(row,row);
                return txtDescription;
        }
        
        return txtDescription;
    }
    /**
     * This method is to get the cell editor value
     * @return Object
     */
    public Object getCellEditorValue() {
        switch(column){
                case RATETYPE_CLASSCODE_COLUMN:
                    return lblCodeComponent.getText();
                case RATETYPE_DESCRIPTION_COLUMN:
                    return txtDescription.getText();
        }
        return ((JTextField)txtDescription).getText();
    }
    
        
   }
   
   public class RateTypeTableCellRenderer extends DefaultTableCellRenderer implements TableCellRenderer {
    
    private CoeusTextField txtDescriptionComponent;
    private JLabel lblText;
    
    private Color displayColor;
    
    
    /** Constructor */
    public RateTypeTableCellRenderer() {
        BevelBorder bevelBorder = new BevelBorder(BevelBorder.LOWERED, Color.white,Color.lightGray, Color.black, Color.lightGray);
        setBorder(bevelBorder);
        lblText = new JLabel();
        lblText.setOpaque(true);
        lblText.setHorizontalAlignment(JLabel.LEFT);
        txtDescriptionComponent = new CoeusTextField();
        lblText.setBorder(new javax.swing.border.BevelBorder(BevelBorder.LOWERED, Color.white,Color.lightGray, Color.black, Color.lightGray));
        displayColor = (Color)UIManager.getDefaults().get("Panel.background");
    }
    
    /**
     * @param JTable table
     * @param Object value
     * @param boolean isSelected
     * @param boolean hasFocus
     * @param boolean hasFocus
     * @param int row
     * @param int col
     * @return Component
     */
    
    public Component getTableCellRendererComponent(JTable table,Object value,
        boolean isSelected,boolean hasFocus ,int row, int col) {
            switch (col) {

                case RATETYPE_CLASSCODE_COLUMN:
                    lblText.setForeground(java.awt.Color.BLACK);
                    lblText.setText((String)value);
                    return lblText;
                case RATETYPE_DESCRIPTION_COLUMN:
                    if( functionType == CoeusGuiConstants.DISPLAY_MODE ) {
                        txtDescriptionComponent.setBackground(displayColor);
                        txtDescriptionComponent.setForeground(java.awt.Color.black);
                    }else {
                    txtDescriptionComponent.setBackground(java.awt.Color.white);
                    txtDescriptionComponent.setForeground(java.awt.Color.black);
                    }
                    if(value==null || value.toString().equals(EMPTY_STRING)){
                    txtDescriptionComponent.setText(null);
                    }else{
                    txtDescriptionComponent.setText(value.toString());
                    }
                    return txtDescriptionComponent;
            }
            return super.getTableCellRendererComponent(table,value,
            isSelected, hasFocus,row,col);
        }
   }
   
   public boolean isSaveRequired(){
       rateClassCellEditor.stopCellEditing();
       rateTypeCellEditor.stopCellEditing();
       return dataChanged;
   }
   
   
   
   
   
   
   
   
    // This method will provide the key travrsal for the table cells
    // It specifies the tab and shift tab order.
    public void setTableKeyTraversal(){
        
         javax.swing.InputMap im = instRateClassTypesForm.tblRateClass.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        // Have the enter key work the same as the tab key
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
        
        // Override the default tab behaviour
        // Tab to the next editable cell. When no editable cells goto next cell.
        final Action oldTabAction = instRateClassTypesForm.tblRateClass.getActionMap().get(im.get(tab));
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
        instRateClassTypesForm.tblRateClass.getActionMap().put(im.get(tab), tabAction);
        
        
        
        
        // for the shift+tab action
        
        // Override the default tab behaviour
        // Tab to the previous editable cell. When no editable cells goto next cell.
        
        final Action oldTabAction1 = instRateClassTypesForm.tblRateClass.getActionMap().get(im.get(shiftTab));
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
                        column = 2;
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
        instRateClassTypesForm.tblRateClass.getActionMap().put(im.get(shiftTab), tabAction1);
        
        
    }
   
   
   public void setTableKeyRateTypeTraversal(){
        
         javax.swing.InputMap im = instRateClassTypesForm.tblRateType.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        // Have the enter key work the same as the tab key
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
        
        // Override the default tab behaviour
        // Tab to the next editable cell. When no editable cells goto next cell.
        final Action oldTabAction = instRateClassTypesForm.tblRateType.getActionMap().get(im.get(tab));
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
        instRateClassTypesForm.tblRateType.getActionMap().put(im.get(tab), tabAction);
        
        
        
        
        // for the shift+tab action
        
        // Override the default tab behaviour
        // Tab to the previous editable cell. When no editable cells goto next cell.
        
        final Action oldTabAction1 = instRateClassTypesForm.tblRateType.getActionMap().get(im.get(shiftTab));
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
                        column = 2;
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
        instRateClassTypesForm.tblRateType.getActionMap().put(im.get(shiftTab), tabAction1);
        
        
    }
   
   public void cleanUp() {
        instRateClassTypesForm = null;
        cvRateClass = null;
        cvRateClassTemp = null;
        cvRateType = null;
        cvTypeResultsTemp = null;
        rateClassBean = null;
        cvTypeResults = null;
        coeusMessageResources = null;
        rateClassTableModel = null;
        rateTypeTableModel = null;
        rateClassCellEditor = null;
        rateTypeCellEditor = null;
        rateClassTableCellRenderer = null;
        rateTypeTableCellRenderer = null;
   }

}
        