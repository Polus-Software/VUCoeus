/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * AwardScienceCodeController.java
 *
 * Created on May 31, 2004, 10:00 AM
 */

package edu.mit.coeus.award.controller;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import edu.mit.coeus.award.gui.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.ucsd.coeus.personalization.controller.AbstractController;


/**
 *
 * @author  surekhan
 */
public class AwardScienceCodeController extends AwardController
implements ActionListener, MouseListener{
    
    private AwardScienceCodeForm awardScienceCodeForm;
    private CoeusDlgWindow dlgAwardScienceCode;
    private AwardDetailsBean awardDetailsBean;
    private CoeusMessageResources coeusMessageResources;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private CoeusVector cvScienceCode;
    private CoeusVector cvDeletedData;
    private edu.mit.coeus.utils.Utils Utils;
    private static final String SCIENCE_CODE_SEARCH = "SCIENCECODESEARCH";
    private static final String SAVE_CHANGES = "saveConfirmCode.1002";
    private static final String DELETE_CONFIRMATION = "award_exceptionCode.1009";
    private char functionType;
    private QueryEngine queryEngine;
    private static final int WIDTH = 680;
    private static final int HEIGHT = 340;
    private ScienceCodeTableModel scienceCodeTableModel;
    private static final int CODE = 0;
    private static final int DESCRIPTION = 1;
    private static final String EMPTY_STRING = "";
    private static final String WINDOW_TITLE = "Science Code";
    public boolean modified;
    private CoeusVector cvData;
    
    //variables for sorting purpose. will be true if last sort was Ascending
    //else will be false.
    private boolean sortCodeAsc = true;
    private boolean sortDescAsc = false;
    
    
    /** Creates a new instance of AwardScienceCodeController */
    public AwardScienceCodeController(AwardBaseBean awardBaseBean, char functionType) {
        super(awardBaseBean);
        this.functionType = functionType;
        cvScienceCode = new CoeusVector();
        cvDeletedData = new CoeusVector();
        cvData = new CoeusVector();
        queryEngine = QueryEngine.getInstance();
        awardDetailsBean = new AwardDetailsBean();
        scienceCodeTableModel = new ScienceCodeTableModel();
        postInitComponents();
        setFormData(null);
        registerComponents();
        formatFields();
        setTableEditors();
    }
    
    private void postInitComponents(){
        awardScienceCodeForm = new AwardScienceCodeForm();
        dlgAwardScienceCode = new CoeusDlgWindow(mdiForm);
        dlgAwardScienceCode.setResizable(false);
        dlgAwardScienceCode.setModal(true);
        dlgAwardScienceCode.getContentPane().add(awardScienceCodeForm);
        dlgAwardScienceCode.setFont(CoeusFontFactory.getLabelFont());
        dlgAwardScienceCode.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgAwardScienceCode.setSize(WIDTH, HEIGHT);
        dlgAwardScienceCode.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
                return;
            }
        });
        
        
        dlgAwardScienceCode.addWindowListener(new WindowAdapter(){
            
            public void windowClosing(WindowEvent we){
                
                performCancelAction();
                
            }
        });
        //code for disposing the window ends
        
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAwardScienceCode.getSize();
        dlgAwardScienceCode.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
      
        dlgAwardScienceCode.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                requestDefaultFocus();
            }
        });
    }
    
    
    public void display() {
    	//    	rdias - UCSD's coeus personalization - Begin
    	AbstractController persnref = AbstractController.getPersonalizationControllerRef();
        persnref.customize_Form(getControlledUI(),"GENERIC");
        persnref.customize_Form(awardScienceCodeForm.awardHeaderForm1,"GENERIC");
        //		rdias - UCSD's coeus personalization - End    	
        
        dlgAwardScienceCode.show();
    }
    
    
    /** To set the default focus for the component
     */
    private void requestDefaultFocus(){
        if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
            awardScienceCodeForm.btnCancel.requestFocus();
        }else{
            awardScienceCodeForm.btnCancel.requestFocus();
        }
    }
    
    /*formats the components*/
    public void formatFields() {
        if(functionType == TypeConstants.DISPLAY_MODE){
            awardScienceCodeForm.btnAdd.setEnabled(false);
            awardScienceCodeForm.btnDelete.setEnabled(false);
            awardScienceCodeForm.btnOk.setEnabled(false);
            awardScienceCodeForm.tblScienceCode.setEnabled(false);
            
            java.awt.Color bgColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
            awardScienceCodeForm.tblScienceCode.setBackground(bgColor);
            awardScienceCodeForm.tblScienceCode.setSelectionBackground(bgColor);
            
            awardScienceCodeForm.btnAdd.addActionListener(this);
            awardScienceCodeForm.btnCancel.addActionListener(this);
            awardScienceCodeForm.btnDelete.addActionListener(this);
            awardScienceCodeForm.btnOk.addActionListener(this);
            
            
        }
    }
    
    /** returns controlled UI.
     * @return Controlled UI
     */
    public java.awt.Component getControlledUI() {
        return awardScienceCodeForm;
    }
    /*gets the form data*/
    public Object getFormData() {
        return awardScienceCodeForm;
    }
    
    /*registers components and listeners*/
    public void registerComponents() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        java.awt.Component[] components = {awardScienceCodeForm.btnCancel,
        awardScienceCodeForm.btnOk,
        awardScienceCodeForm.btnAdd,
        awardScienceCodeForm.btnDelete,
        };
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        awardScienceCodeForm.setFocusTraversalPolicy(traversePolicy);
        awardScienceCodeForm.setFocusCycleRoot(true);
        
        scienceCodeTableModel = new ScienceCodeTableModel();
        awardScienceCodeForm.tblScienceCode.setModel(scienceCodeTableModel);
        
        //listener for header for sorting purpose
        awardScienceCodeForm.tblScienceCode.getTableHeader().addMouseListener(this);
        
        awardScienceCodeForm.btnAdd.addActionListener(this);
        awardScienceCodeForm.btnCancel.addActionListener(this);
        awardScienceCodeForm.btnDelete.addActionListener(this);
        awardScienceCodeForm.btnOk.addActionListener(this);
    }
    
    /*saves the form data*/
    public void saveFormData() {
        CoeusVector codeObject = new CoeusVector();
        if(modified) {
            if(cvDeletedData != null && cvDeletedData.size()>0){
                codeObject.addAll(cvDeletedData);
            }
            
            if(cvScienceCode != null && cvScienceCode.size() > 0){
                codeObject.addAll(cvScienceCode);
            }
            
            if(codeObject != null){
                for(int index = 0;index < codeObject.size() ; index++){
                    AwardScienceCodeBean dataBean = (AwardScienceCodeBean)codeObject.get(index);
                    if(dataBean.getAcType() != null){
                        try{
                            if(dataBean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
                                dataBean.setAcType(TypeConstants.DELETE_RECORD);
                                queryEngine.delete(queryKey,dataBean);
                                dataBean.setAcType(TypeConstants.INSERT_RECORD);
                                queryEngine.insert(queryKey,dataBean);
                            }else if(dataBean.getAcType().equals(TypeConstants.DELETE_RECORD)){
                                dataBean.setAcType(TypeConstants.DELETE_RECORD);
                                queryEngine.delete(queryKey,dataBean);
                            }else if(dataBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                                dataBean.setAcType(TypeConstants.INSERT_RECORD);
                                queryEngine.insert(queryKey,dataBean);
                            }
                        }catch (CoeusException coeusException){
                            coeusException.printStackTrace();
                        }
                    }
                }
            }
        }
    }
    
    /*set the form data*/
    public void setFormData(Object data) {
        
        try{
            cvData = queryEngine.executeQuery(queryKey,
            AwardDetailsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            
            awardDetailsBean = (AwardDetailsBean)cvData.get(0);
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
        try{
            cvScienceCode = queryEngine.executeQuery(
            queryKey, AwardScienceCodeBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            
            if(cvScienceCode!= null && cvScienceCode.size() > 0){
                cvScienceCode.sort("scienceCode");
                
            }
        }catch(CoeusException coeusException) {
            coeusException.printStackTrace();
        }
        awardScienceCodeForm.awardHeaderForm1.setFormData(awardDetailsBean);
        //Case #2336 start
        awardScienceCodeForm.awardHeaderForm1.lblSequenceNumberValue.setText(EMPTY_STRING+awardBaseBean.getSequenceNumber());
        //Case #2336 end
        dlgAwardScienceCode.setTitle(WINDOW_TITLE);
    }
    
     /** validates the form.
     * returns false if validation fails.
     * else returns true.
     * @throws CoeusUIException if any exception occurs / validation fails.
     * @return returns false if validation fails.
     * else returns true.
     */    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return true;
    }
    
    /**
     * To make the class variable instances null
     */
    public void cleanUp() {
        awardScienceCodeForm = null;
        dlgAwardScienceCode = null;
        //  awardScienceCodeBean = null;
        awardDetailsBean = null;
        cvScienceCode = null;
        cvDeletedData = null;
        Utils = null;
        scienceCodeTableModel = null;
        cvData = null;
    }
    
    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        if(source.equals(awardScienceCodeForm.btnAdd)){
            performAddAction(SCIENCE_CODE_SEARCH);
        }else if(source.equals(awardScienceCodeForm.btnDelete)){
            performDeleteAction();
        }else if(source.equals(awardScienceCodeForm.btnOk)){
            saveFormData();
            close();
        }else if(source.equals(awardScienceCodeForm.btnCancel)){
            performCancelAction();
        }
    }
    
    private void close(){
        dlgAwardScienceCode.dispose();
    }
    
    private void performCancelAction(){
        if(modified){
            int option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(SAVE_CHANGES),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            JOptionPane.YES_OPTION);
            switch( option ) {
                case (JOptionPane.YES_OPTION ):
                    saveFormData();
                    close();
                    break;
                case(JOptionPane.NO_OPTION ):
                    close();
                    break;
                default:
                    break;
            }
        }else{
            close();
        }
    }
   /*To add an empty row to the table*/ 
    private void performAddAction(String strSearchType){
        try{
            AwardScienceCodeBean newBean = new AwardScienceCodeBean() ;
            CoeusSearch coeusSearch =
            new CoeusSearch(mdiForm, strSearchType,
            CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION );
            
            coeusSearch.showSearchWindow();
            Vector cvSelectedCodes = coeusSearch.getMultipleSelectedRows();
            if( cvSelectedCodes != null ){
                
                HashMap singleData = null;
                int size = cvSelectedCodes.size();
                for(int index = 0; index < size; index++ ){
                    singleData = (HashMap)cvSelectedCodes.get( index ) ;
                    if(singleData != null){
                        String code = Utils.
                        convertNull(singleData.get( "SCIENCE_CODE" ));
                        String description = Utils.
                        convertNull(singleData.get( "DESCRIPTION" ));
                        
                        boolean duplicate = checkDuplicateScienceCode(code);
                        if(!duplicate){
                            newBean = new AwardScienceCodeBean();
                            newBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
                            newBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
                            newBean.setScienceCode(code);
                            newBean.setDescription(description);
                            newBean.setAcType(TypeConstants.INSERT_RECORD);
                            modified = true;
                            cvScienceCode.add(newBean);
                            scienceCodeTableModel.fireTableRowsInserted(
                            scienceCodeTableModel.getRowCount()+1,scienceCodeTableModel.getRowCount()+1);
                            int lastRow = awardScienceCodeForm.tblScienceCode.getRowCount()-1;
                            if(lastRow >= 0){
                                //awardScienceCodeForm.tblScienceCode.setRowSelectionInterval(lastRow,lastRow);
                                awardScienceCodeForm.tblScienceCode.scrollRectToVisible(
                                awardScienceCodeForm.tblScienceCode.getCellRect(lastRow, CODE, true));
                            }
                            awardScienceCodeForm.btnDelete.setEnabled(true);
                        }
                    }
                }
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
        
    }
   
    /*To delete the rows in the table*/
    private void performDeleteAction(){
        int rowCount = awardScienceCodeForm.tblScienceCode.getRowCount();
        int selectedOption = -1;
        int selRow = awardScienceCodeForm.tblScienceCode.getSelectedRow();
        if(selRow== -1) return;
        int rowIndex = 0;
        if(rowCount == 0) return ;
        
        
        int[] selectedRows = awardScienceCodeForm.tblScienceCode.getSelectedRows();
        if(selectedRows.length==1){
            selectedOption = CoeusOptionPane.
            showQuestionDialog(
            coeusMessageResources.parseMessageKey(
            "award_exceptionCode.1009"),
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
            for(rowIndex = selectedRows.length - 1; rowIndex >=0 ; rowIndex--) {
                AwardScienceCodeBean deletedBean = (AwardScienceCodeBean)cvScienceCode.get(selectedRows[rowIndex]);
                cvDeletedData.add(deletedBean);
                if (deletedBean.getAcType() == null ||
                deletedBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
                    cvDeletedData.add(deletedBean);
                }
                if(cvScienceCode!=null && cvScienceCode.size() > 0){
                    cvScienceCode.remove(selectedRows[rowIndex]);
                    scienceCodeTableModel.fireTableRowsDeleted(rowIndex, rowIndex);
                    modified = true;
                    deletedBean.setAcType(TypeConstants.DELETE_RECORD);
                }
            }
            
        }
    }
    
    private boolean checkDuplicateScienceCode(String code){
        
        boolean duplicate = false;
        String oldId = "";
        int size = awardScienceCodeForm.tblScienceCode.getRowCount();
        for(int rowIndex = 0; rowIndex < size; rowIndex++){
            oldId = (String)awardScienceCodeForm.tblScienceCode.getValueAt(rowIndex,0);
            if(oldId != null){
                if(oldId.equals(code)){
                    duplicate = true;
                    break;
                }
            }
        }
        return duplicate;
    }
    
    private void setTableEditors(){
        awardScienceCodeForm.tblScienceCode.setRowHeight(22);
        JTableHeader tableHeader = awardScienceCodeForm.tblScienceCode.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        awardScienceCodeForm.tblScienceCode.setOpaque(false);
        
        TableColumn column = awardScienceCodeForm.tblScienceCode.getColumnModel().getColumn(CODE);
        column.setMaxWidth(170);
        column.setMinWidth(150);
        column.setPreferredWidth(150);
        column.setResizable(true);
        tableHeader.setReorderingAllowed(false);
        
        
        column = awardScienceCodeForm.tblScienceCode.getColumnModel().getColumn(DESCRIPTION);
        column.setMaxWidth(450);
        column.setMinWidth(435);
        column.setPreferredWidth(435);
        column.setResizable(true);
        tableHeader.setReorderingAllowed(false);
    }
    
    public void mouseClicked(MouseEvent mouseEvent) {
        
        int size = awardScienceCodeForm.tblScienceCode.getRowCount();
        Point clickedPoint = mouseEvent.getPoint();
        int xPosition = (int)clickedPoint.getX();
        int columnIndex = awardScienceCodeForm.tblScienceCode.getColumnModel().getColumnIndexAtX(xPosition);
        switch (columnIndex) {
            case CODE:
                if(sortCodeAsc) {
                    //Code already sorted in Ascending order. Sort now in Descending order.
                    cvScienceCode.sort("scienceCode", false);
                    sortCodeAsc = false;
                }else {
                    //Code already sorted in Descending order. Sort now in Ascending order.
                    cvScienceCode.sort("scienceCode", true);
                    sortCodeAsc = true;
                }
                break;
            case DESCRIPTION:
                if(sortDescAsc){
                    cvScienceCode.sort("description",false);
                    sortDescAsc = false;
                }else {
                    cvScienceCode.sort("description",true);
                    sortDescAsc = true;
                }
                break;
        }//End Switch
        scienceCodeTableModel.fireTableDataChanged();
        for(int rowIndex = 0; rowIndex < size; rowIndex++){
            String code = (String)awardScienceCodeForm.tblScienceCode.getValueAt(rowIndex,0);
                    }
    }//End Mouse Click
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    public void mousePressed(MouseEvent e) {
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    class ScienceCodeTableModel extends AbstractTableModel {
        
        String colNames[] = {"Code" , "Description" };
        Class[] colTypes = new Class [] {String.class , String.class };
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public void setData(CoeusVector cvScienceCode){
            cvScienceCode = cvScienceCode;
        }
        
        public Class getColumnClass(int col){
            return colTypes[col];
        }
        public String getColumnName(int col){
            return colNames[col];
        }
        
        public int getRowCount() {
            if(cvScienceCode== null){
                return 0;
            }else{
                return cvScienceCode.size();
            }
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            AwardScienceCodeBean bean = (AwardScienceCodeBean)cvScienceCode.get(rowIndex);
            switch(columnIndex){
                case CODE:
                    return bean.getScienceCode();
                case DESCRIPTION:
                    return bean.getDescription();
            }
            return EMPTY_STRING;
        }
        
    }
    
}
