/*
 * IPScienceCodeController.java
 *
 * Created on April 29, 2004, 2:34 PM
 */

package edu.mit.coeus.instprop.controller;

import edu.mit.coeus.instprop.gui.IPScienceCodeForm;
import edu.mit.coeus.instprop.controller.InstituteProposalController;
import edu.mit.coeus.instprop.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.search.gui.*;



import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Vector;

/** /**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * 
* @author chandru
*/
public class IPScienceCodeController extends InstituteProposalController 
implements ActionListener{
    
    private static final String EMPTY_STRING = "";
    private static final String SCIENCE_CODE_SEARCH = "SCIENCECODESEARCH";
    private IPScienceCodeForm scienceCodeForm;
    private char functionType;
    private InstituteProposalBaseBean instituteProposalBaseBean;
    private InstituteProposalScienceCodeBean instituteProposalScienceCodeBean;
    private CoeusVector cvScienceCode;
    private CoeusVector cvDeletedData;
    private QueryEngine queryEngine;
    private ScienceCodeTableModel scienceCodeTableModel;
    private edu.mit.coeus.utils.Utils Utils;
    private CoeusMessageResources coeusMessageResources;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    public boolean saveRequied;
    private int sequenceNumber;
    private String proposalNumber = EMPTY_STRING;
    
    
    private static final int CODE_COLUMN = 0;
    private static final int DESC_COLUMN = 1;
    
    /** Creates a new instance of IPScienceCodeController */
    public IPScienceCodeController(InstituteProposalBaseBean instituteProposalBaseBean, char functionType) {
        super(instituteProposalBaseBean);
        this.functionType = functionType;
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
        setFormData(instituteProposalBaseBean);
        if(scienceCodeForm.tblScienceCodes.getRowCount() < 1){
            scienceCodeForm.btnDelete1.setEnabled(false);
        }
        formatFields();
        setColumnData();
    }
    
    public void display() {
    }
    
    public void formatFields() {
        if(functionType==DISPLAY_PROPOSAL){
            scienceCodeForm.btnAdd1.setEnabled(false);
            scienceCodeForm.btnDelete1.setEnabled(false);
            
        }
    }
    
    public java.awt.Component getControlledUI() {
        return scienceCodeForm;
    }
    
    public Object getFormData() {
        return null;
    }
    
    public void registerComponents() {
        scienceCodeForm = new IPScienceCodeForm();
        if(functionType!= DISPLAY_PROPOSAL){
            scienceCodeForm.btnAdd1.addActionListener(this);
            scienceCodeForm.btnDelete1.addActionListener(this);
        }
        
        scienceCodeTableModel = new ScienceCodeTableModel();
        scienceCodeForm.tblScienceCodes.setModel(scienceCodeTableModel);
    }
  
    
    public void setFormData(Object instituteProposalBaseBean) {
        try{
            this.instituteProposalBaseBean = (InstituteProposalBaseBean)instituteProposalBaseBean;
            
            proposalNumber = this.instituteProposalBaseBean .getProposalNumber();
            sequenceNumber = this.instituteProposalBaseBean .getSequenceNumber();
            
            cvScienceCode = new CoeusVector();
            cvDeletedData = new CoeusVector();
            cvScienceCode  = queryEngine.executeQuery(
                    queryKey, InstituteProposalScienceCodeBean.class, CoeusVector.FILTER_ACTIVE_BEANS);

            if(cvScienceCode!= null && cvScienceCode.size() > 0){
                cvScienceCode.sort("scienceCode");
                scienceCodeTableModel.setData(cvScienceCode);
                scienceCodeTableModel.fireTableDataChanged();
            }
        }catch (CoeusException  coeusException){
            coeusException.printStackTrace();
        }
    }
    
      public void saveFormData() {
          CoeusVector dataObject = new CoeusVector();
          
          if(isSaveRequied()){
              
              if(cvDeletedData!= null && cvDeletedData.size() >0){
                  dataObject.addAll(cvDeletedData);
              }
              
              if(cvScienceCode!= null && cvScienceCode.size() >0){
                  dataObject.addAll(cvScienceCode);
              }
              
              if(dataObject!=null){
                  for(int index = 0; index < dataObject.size(); index++){
                      InstituteProposalScienceCodeBean bean =
                      (InstituteProposalScienceCodeBean) dataObject.get(index);
                      if(bean.getAcType()!= null){
                          try{
                              if(bean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
                                  //First delete the existing science code and then insert the same. This is
                                  //required since primary keys can be modified
                                  bean.setAcType(TypeConstants.DELETE_RECORD);
                                  queryEngine.delete(queryKey, bean);
                                  bean.setAcType(TypeConstants.INSERT_RECORD);
                                  queryEngine.insert(queryKey, bean);
                              }else if(bean.getAcType().equals(TypeConstants.DELETE_RECORD)){
                                  bean.setAcType(TypeConstants.DELETE_RECORD);
                                  queryEngine.delete(queryKey, bean);
                              }else if(bean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                                  bean.setAcType(TypeConstants.INSERT_RECORD);
                                  queryEngine.insert(queryKey, bean);
                              }
                          }catch (CoeusException coeusException){
                              coeusException.printStackTrace();
                          }
                          finally{
                              setSaveRequied(false);
                          }
                      }
                  }
              }
          }
      }
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return false;
    }
    private void setColumnData(){
        JTableHeader tableHeader = scienceCodeForm.tblScienceCodes.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.addMouseListener(new ColumnHeaderListener());

        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        scienceCodeForm.tblScienceCodes.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        scienceCodeForm.tblScienceCodes.setRowHeight(22);
        scienceCodeForm.tblScienceCodes.setSelectionBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Table.selectionBackground"));
        scienceCodeForm.tblScienceCodes.setSelectionForeground(java.awt.Color.white);
        scienceCodeForm.tblScienceCodes.setShowHorizontalLines(true);
        scienceCodeForm.tblScienceCodes.setShowVerticalLines(true);
        scienceCodeForm.tblScienceCodes.setOpaque(false);
        tableHeader.setResizingAllowed(true);
        scienceCodeForm.tblScienceCodes.setSelectionMode(
        DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        TableColumn column = scienceCodeForm.tblScienceCodes.getColumnModel().getColumn(CODE_COLUMN);
        column.setMinWidth(100);
        column.setPreferredWidth(110);
        column.setResizable(true);
        
        column = scienceCodeForm.tblScienceCodes.getColumnModel().getColumn(DESC_COLUMN);
        column.setMinWidth(320);
        column.setPreferredWidth(396);
        column.setResizable(true);
        
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source.equals(scienceCodeForm.btnAdd1)){
            performAddAction(SCIENCE_CODE_SEARCH);
        }else if(source.equals(scienceCodeForm.btnDelete1)){
            performDeleteAction();
        }
    }
    
    
    private void performDeleteAction(){
        int rowCount = scienceCodeForm.tblScienceCodes.getRowCount();
        int selectedOption = -1;
        int selRow = scienceCodeForm.tblScienceCodes.getSelectedRow();
        if(selRow== -1) return;
        int rowIndex = 0;
        if(rowCount == 0) return ;
        
        int[] selectedRows = scienceCodeForm.tblScienceCodes.getSelectedRows();
        if(selectedRows.length==1){
            selectedOption = CoeusOptionPane.
            showQuestionDialog(
            coeusMessageResources.parseMessageKey(
            "generalDelConfirm_exceptionCode.2100") + " row?",
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
        }else {
            selectedOption = CoeusOptionPane.
            showQuestionDialog(
            "Are you sure you want to delete these "
            + selectedRows.length+ " rows?",
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
        }
        if(selectedOption == 0){
             for(rowIndex = selectedRows.length-1; rowIndex >=0 ; rowIndex--) {
                    InstituteProposalScienceCodeBean deletedBean = (InstituteProposalScienceCodeBean)cvScienceCode.get(selectedRows[rowIndex]);
                    if (deletedBean.getAcType() == null ||
                    deletedBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
                        cvDeletedData.add(deletedBean);
                    }
                    if(cvScienceCode!=null && cvScienceCode.size() > 0){
                        cvScienceCode.remove(selectedRows[rowIndex]);
                        scienceCodeTableModel.fireTableRowsDeleted(rowIndex, rowIndex);
                        saveRequied = true;
                        deletedBean.setAcType(TypeConstants.DELETE_RECORD);
                    }
             }
            if(rowIndex >0){
                
                scienceCodeForm.tblScienceCodes.setRowSelectionInterval(
                rowIndex-1,rowIndex-1);
                scienceCodeForm.tblScienceCodes.scrollRectToVisible(
                scienceCodeForm.tblScienceCodes.getCellRect(
                rowIndex-1 ,0, true));
            }else{
                if(scienceCodeForm.tblScienceCodes.getRowCount()>0){
                    scienceCodeForm.tblScienceCodes.setRowSelectionInterval(0,0);
                }
            }
             // Disable to Del button if there is no row found
             if(scienceCodeForm.tblScienceCodes.getRowCount() == 0){
                 scienceCodeForm.btnDelete1.setEnabled(false);
             }
        }
    }

    
    
    private void performAddAction(String strSearchType){
        try{
            InstituteProposalScienceCodeBean newBean;
            CoeusSearch coeusSearch =
                    new CoeusSearch(mdiForm, strSearchType, 
                    CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION );
            
            coeusSearch.showSearchWindow();
            Vector vecSelectedScienceCodes = coeusSearch.getMultipleSelectedRows(); 
            
            if( vecSelectedScienceCodes != null ){
                
                HashMap singleData = null;
                int size = vecSelectedScienceCodes.size();
                for(int index = 0; index < size; index++ ){
                    singleData = (HashMap)vecSelectedScienceCodes.get( index ) ;     
                    if(singleData != null){
                        String code = Utils.
                            convertNull(singleData.get( "SCIENCE_CODE" ));
                        String description = Utils.
                            convertNull(singleData.get( "DESCRIPTION" ));
                        
                        boolean duplicate = checkDuplicateScienceCode(code);
                        Vector vecData = null;
                        if(!duplicate){
                            newBean = new InstituteProposalScienceCodeBean();
                            newBean.setProposalNumber(proposalNumber);
                            newBean.setSequenceNumber(sequenceNumber);
                            newBean.setScienceCode(code);
                            newBean.setScienceDescription(description);
                            newBean.setAcType(TypeConstants.INSERT_RECORD);
                            saveRequied = true;
                            cvScienceCode.add(newBean);
                            cvScienceCode.sort("scienceCode", true);
                            scienceCodeTableModel.fireTableRowsInserted(
                            scienceCodeTableModel.getRowCount()+1,scienceCodeTableModel.getRowCount()+1);
                            int lastRow = scienceCodeForm.tblScienceCodes.getRowCount()-1;
                            if(lastRow >= 0){
                                scienceCodeForm.tblScienceCodes.setRowSelectionInterval(lastRow,lastRow);
                                scienceCodeForm.tblScienceCodes.scrollRectToVisible(
                                scienceCodeForm.tblScienceCodes.getCellRect(lastRow, CODE_COLUMN, true));
                            }
                            scienceCodeForm.btnDelete1.setEnabled(true); 
                        }
                    }
                }
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
    /** 
     *  Method used to validate whether the Science Code is duplicate or not 
     */
    
    private boolean checkDuplicateScienceCode(String code){
        
        boolean duplicate = false;
        String oldId = "";
        int size = scienceCodeForm.tblScienceCodes.getRowCount();
        for(int rowIndex = 0; rowIndex < size; rowIndex++){
            
            oldId = (String)scienceCodeForm.tblScienceCodes.getValueAt(rowIndex,0);
            if(oldId != null){
                if(oldId.equals(code)){
                    duplicate = true;
                    break;
                }
            }
        }
        return duplicate;
    }
    
    /** Getter for property saveRequied.
     * @return Value of property saveRequied.
     *
     */
    public boolean isSaveRequied() {
        return saveRequied;
    }
    
    /** Setter for property saveRequied.
     * @param saveRequied New value of property saveRequied.
     *
     */
    public void setSaveRequied(boolean saveRequied) {
        this.saveRequied = saveRequied;
    }
    
    
     public void setRefreshRequired(boolean refreshRequired) {
        super.setRefreshRequired(refreshRequired);
    }
    
    public boolean isRefreshRequired() {
        boolean retValue;
        
        retValue = super.isRefreshRequired();
        return retValue;
    }
    
    public void refresh() {
        if (isRefreshRequired()) {
            setFormData(instituteProposalBaseBean);
            setRefreshRequired(false);
        }
    }
    
    
    public void setDefaultFocusForComponent(){
        if(functionType!= DISPLAY_PROPOSAL){
            scienceCodeForm.btnAdd1.requestFocusInWindow();
        }else{
            if(scienceCodeForm.tblScienceCodes.getRowCount() > 0){
                scienceCodeForm.tblScienceCodes.setRowSelectionInterval(0,0);
            }
        }
    }
        
    
    public class ScienceCodeTableModel extends AbstractTableModel{
        private String colName[] = {"Code", "Description"};
        private Class colClass[] = {Integer.class, String.class};
        
        public int getColumnCount() {
            return colName.length;
        }
        
        public void setData(CoeusVector cvScienceCode){
            cvScienceCode = cvScienceCode;
        }
        
        public Class getColumnClass(int col){
            return colClass[col];
        }
        public String getColumnName(int col){
            return colName[col];
        }
        
        public int getRowCount() {
            if(cvScienceCode== null){
                return 0;
            }else{
                return cvScienceCode.size();
            }
        }
        
        public Object getValueAt(int row, int col) {
            InstituteProposalScienceCodeBean dataBean = (InstituteProposalScienceCodeBean)
                                    cvScienceCode.get(row);
            switch(col){
                case CODE_COLUMN:
                    return dataBean.getScienceCode();
                case DESC_COLUMN:
                    return dataBean.getScienceDescription();
            }
            return EMPTY_STRING;
        }
    }
    
    /** This class will sort the column values in ascending and descending order
     *based on number of clicks. This will sort only Name, Job code and Effective date
     *columns only which are primary keys.
     */
    
    public class ColumnHeaderListener extends java.awt.event.MouseAdapter {
        String nameBeanId [][] ={
            {"0","scienceCode"},
            {"1","scienceDescription"}
        };
        boolean sort =true;
        /**
         * @param evt
         */        
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            try {
                
                JTable table = ((JTableHeader)evt.getSource()).getTable();
                TableColumnModel colModel = table.getColumnModel();
                
                // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(evt.getX());
                if(cvScienceCode!=null && cvScienceCode.size()>0 &&
                nameBeanId [vColIndex][1].length() >1 ){
                    ((CoeusVector)cvScienceCode).sort(nameBeanId [vColIndex][1],sort,true);
                    if(sort)
                        sort = false;
                    else
                        sort = true;
                    scienceCodeTableModel.fireTableRowsUpdated(0, scienceCodeTableModel.getRowCount());
                }
            } catch(Exception exception) {
                exception.getMessage();
            }
        }
    }// End of ColumnHeaderListener................
    
}
