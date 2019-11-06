/*
 * TemplateTermsController.java
 *
 * Created on December 17, 2004, 3:09 PM
 */

package edu.mit.coeus.admin.controller;

import edu.mit.coeus.admin.bean.AwardTemplateBean;
import edu.mit.coeus.admin.bean.TemplateBaseBean;
import edu.mit.coeus.award.AwardConstants;
import edu.mit.coeus.award.bean.AwardTermsBean;
import edu.mit.coeus.award.bean.AwardDetailsBean;
import edu.mit.coeus.award.bean.TemplateTermsBean;
//import edu.mit.coeus.award.controller.AwardAddTermsController;
import edu.mit.coeus.award.gui.AwardTermsForm;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;

/**
 *
 * @author  ajaygm
 */
public class TemplateTermsController extends AwardTemplateController implements
ActionListener, ListSelectionListener,MouseListener{
    
    private CoeusVector cvTermsName;
    private CoeusVector cvAllTermsDetails;
    private CoeusVector cvAwardTermsDetails;
    private CoeusVector cvAddTermDetails;
    
    private AwardTermsForm awardTermsForm;
    
    private static final String CONFIRM_SYNC = "awardTerms_exceptionCode.1301";
    
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    private static final int IMAGE_COLUMN = 0;
    private static final int TEXT_COLUMN = 1;
    
    private static final int CODE_COLUMN = 0;
    private static final int DESCRIPTION_COLUMN = 1;
    
    private String termsKey = EMPTY;
    
    private QueryEngine queryEngine;
    private Equals eqTerms;
    //    private AwardTermsBean awardTermsBean;
    private TemplateTermsBean templateTermsBean;
    
    private CoeusMessageResources  coeusMessageResources;
    
    private AwardTermsDetailsTableModel awardTermsDetailsTableModel;
    private AwardTermsTableModel awardTermsTableModel;
    private AwardTermsTableCellRenderer awardTermsTableCellRenderer;
    
    private static final Color PANEL_BACKGROUND_COLOR =
    (Color) UIManager.getDefaults().get("Panel.background");
    
    private int lastSelectedRow;
    private int prevSelectedRow;
    
    private String selectedValue = null;
    
    private static final String SELECT_A_ROW="Please select a row to delete";
    
    private static final String DELETE_CONFIRMATION = "Are you sure you want to remove this row?";
    
    private static final String firstName = "Applicable";
    private static final String lastName = "Terms";
    private static final String termNames [] = {AwardConstants.EQUIPMENT_APPROVAL,
    AwardConstants.INVENTION,
    AwardConstants.OTHER_REQUIREMENT,
    AwardConstants.PROPERTY,AwardConstants.PUBLICATION,
    AwardConstants.REFERENCED_DOCUMENTS,
    AwardConstants.RIGHTS_IN_DATA,
    AwardConstants.SUBCONTRACT_APPROVAL,
    AwardConstants.TRAVEL};

    //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
    private static final int LAST_UPDATE_COLUMN = 2;
    private static final int UPDATED_BY_COLUMN = 3;
    private static final int LAST_UPDATED_COLUMN_WIDTH = 135;
    private static final int UPDATED_BY_COLUMN_WIDTH = 135;
    //COEUSQA-1456 : End
    /** Creates a new instance of TemplateTermsController */
    public TemplateTermsController(TemplateBaseBean templateBaseBean , char functionType) {
        super(templateBaseBean);
        
        coeusMessageResources = CoeusMessageResources.getInstance();
        queryEngine = QueryEngine.getInstance();
        awardTermsForm = new AwardTermsForm();
        setFunctionType(functionType);
        registerComponents();
        setColumnData();
        //formatFields();
        setFormData(templateBaseBean);
    }
    
    /**
     * Display
     * @return void
     **/
    public void display() {
    }
    /**
     * Format fields
     * @return void
     **/
    public void formatFields() {
        // if it is in display mode disabling the buttons.
        awardTermsForm.btnSync.setEnabled(false);
        awardTermsForm.btnSync.setVisible(false);
        //Added with case 2796: Sync To Parent
        awardTermsForm.btnAddSync.setEnabled(false);
        awardTermsForm.btnAddSync.setVisible(false);
        awardTermsForm.btnDelSync.setEnabled(false);
        awardTermsForm.btnDelSync.setVisible(false);
        //2796 End
        if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
            awardTermsForm.btnAdd.setEnabled(false);
            awardTermsForm.btnDelete.setEnabled(false);
            awardTermsForm.btnSync.setEnabled(false);
            awardTermsForm.tblTermDetails.setBackground((Color) UIManager.
            getDefaults().get("Panel.background"));
        }else{
            awardTermsForm.btnAdd.setEnabled(true);
            awardTermsForm.btnDelete.setEnabled(true);
            awardTermsForm.btnSync.setEnabled(true);
            awardTermsForm.tblTermDetails.setBackground(Color.white);
        }
    }
    
    /**
     * To get the controlled UI
     * @return java.awt.Component
     **/
    public Component getControlledUI() {
        return awardTermsForm;
    }
    /**
     * To get the form data
     * @return Object
     **/
    public Object getFormData()	{
        return awardTermsForm;
    }
    /**
     * Registering the components
     * @return void
     **/
    public void registerComponents() {
        //awardTermsForm = new AwardTermsForm();
        awardTermsDetailsTableModel = new AwardTermsDetailsTableModel();
        awardTermsTableModel = new AwardTermsTableModel();
        awardTermsTableCellRenderer = new AwardTermsTableCellRenderer();
        
        awardTermsForm.btnAdd.addActionListener(this);
        awardTermsForm.btnDelete.addActionListener(this);
        awardTermsForm.btnSync.addActionListener(this);
        awardTermsForm.tblTermDetails.addMouseListener(this);
        
        awardTermsForm.tblTermName.getSelectionModel().setSelectionMode(
        ListSelectionModel.SINGLE_SELECTION);
        
        awardTermsForm.tblTermName.getSelectionModel().addListSelectionListener(this);
        
        awardTermsForm.tblTermName.setModel(awardTermsTableModel);
        
        awardTermsForm.tblTermDetails.setModel(awardTermsDetailsTableModel);
        
    }
    
    /** Method to perform some action when the beanUpdated event is triggered
     * here it sets the <CODE>refreshRequired</CODE> flag
     * @param beanEvent takes the beanEvent */
    //    public void beanUpdated(BeanEvent beanEvent) {
    //        if( beanEvent.getSource().getClass().equals(OtherHeaderController.class) ){
    //            if( beanEvent.getBean().getClass().equals(AwardDetailsBean.class)){
    //                setRefreshRequired(true);
    //            }
    //        }
    //    }
    
    /**
     * Method to garbage collect the added listeners
     */
    public void cleanUp() {
        // set all the instance variable to null and it will called during garbage collection
        cvTermsName = null;
        cvAllTermsDetails = null;
        cvAwardTermsDetails = null;
        cvAddTermDetails = null;
        awardTermsForm = null;
        termsKey = null;
        eqTerms = null;
        //        awardTermsBean = null;
        templateTermsBean = null;
        awardTermsDetailsTableModel = null;
        awardTermsTableModel = null;
        awardTermsTableCellRenderer = null;
        selectedValue = null;
    }
    
    /**
     * Setting up the column data
     * @return void
     **/
    private void setColumnData(){
        
        JTableHeader tableHeader = awardTermsForm.tblTermDetails.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        
        //for the single selection
        awardTermsForm.tblTermDetails.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        tableHeader.addMouseListener(new ColumnHeaderListener());
        
        TableColumn column = awardTermsForm.tblTermName.getColumnModel().getColumn(IMAGE_COLUMN);
        column.setMinWidth(20);
        column.setMaxWidth(20);
        column.setPreferredWidth(20);
        column.setResizable(false);
        column.setCellRenderer(awardTermsTableCellRenderer);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        awardTermsForm.tblTermName.setRowHeight(20);
        column = awardTermsForm.tblTermName.getColumnModel().getColumn(TEXT_COLUMN);
        column.setMinWidth(100);
        column.setMaxWidth(250);
        column.setPreferredWidth(180);
        column.setResizable(false);
        column.setCellRenderer(awardTermsTableCellRenderer);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        
        awardTermsForm.tblTermName.setShowGrid(false);
        awardTermsForm.tblTermName.setShowVerticalLines(false);
        awardTermsForm.tblTermName.setShowHorizontalLines(false);
        awardTermsForm.tblTermName.setOpaque(false);
        awardTermsForm.tblTermName.setBackground(PANEL_BACKGROUND_COLOR);
        
        column = awardTermsForm.tblTermDetails.getColumnModel().getColumn(CODE_COLUMN);
        awardTermsForm.tblTermDetails.setRowHeight(22);
        column.setMinWidth(50);
        column.setPreferredWidth(50);
        column.setResizable(true);
        
        column = awardTermsForm.tblTermDetails.getColumnModel().getColumn(DESCRIPTION_COLUMN);
        awardTermsForm.tblTermDetails.setRowHeight(22);
        column.setMinWidth(200);
        //Modified for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
//        column.setPreferredWidth(615);
        column.setPreferredWidth(295);
        //COEUSQA-1456 : End
        column.setResizable(true);
        
        //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
        column = awardTermsForm.tblTermDetails.getColumnModel().getColumn(LAST_UPDATE_COLUMN);
        column.setMinWidth(LAST_UPDATED_COLUMN_WIDTH);
        column.setPreferredWidth(LAST_UPDATED_COLUMN_WIDTH);
        column.setResizable(true);
        
        column = awardTermsForm.tblTermDetails.getColumnModel().getColumn(UPDATED_BY_COLUMN);
        column.setMinWidth(UPDATED_BY_COLUMN_WIDTH);
        column.setPreferredWidth(UPDATED_BY_COLUMN_WIDTH);
        column.setResizable(true);
        //COEUSQA-1456 : End
        
        awardTermsForm.tblTermDetails.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        
    }
    /**
     * save form data
     * @return void
     */
    public void saveFormData() {
        if(getFunctionType() == TypeConstants.COPY_MODE){
               //Modified for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
//            String validationHierarchy[] = {AwardConstants.TRAVEL, AwardConstants.SUBCONTRACT_APPROVAL,
//            AwardConstants.RIGHTS_IN_DATA,AwardConstants.PUBLICATION,
//            AwardConstants.PROPERTY,AwardConstants.OTHER_REQUIREMENT,
//            AwardConstants.REFERENCED_DOCUMENTS,AwardConstants.EQUIPMENT_APPROVAL,
//            AwardConstants.INVENTION};
            String validationHierarchy[] = {AwardConstants.EQUIPMENT_APPROVAL,AwardConstants.INVENTION,
            AwardConstants.OTHER_REQUIREMENT,AwardConstants.PROPERTY,AwardConstants.PUBLICATION,
            AwardConstants.REFERENCED_DOCUMENTS,AwardConstants.RIGHTS_IN_DATA,AwardConstants.SUBCONTRACT_APPROVAL,
            AwardConstants.TRAVEL};
            //COEUSQA-1456 : End
            String key;
            String valTermsKey;
            for(int index = 0; index < validationHierarchy.length; index++) {
                key = validationHierarchy[index];
                valTermsKey = (String) AwardConstants.awardTerms.get(key);
                //checking for mit award number != null and actype != null & acType != D
                Equals eqAwardNumber = new Equals("templateCode",new Integer(templateBaseBean.getTemplateCode()));
                NotEquals notACType = new NotEquals("acType",TypeConstants.DELETE_RECORD);
                Equals acNType = new Equals("acType",null);
                Equals acIType = new Equals("acType","I");
                Equals acDType = new Equals("acType","D");
                Or checkAcType = new Or(acNType,acIType);
                Or actype = new Or(checkAcType,acIType);
                And and = new And(eqAwardNumber, actype);
                try{
                    CoeusVector cvValidateTerms = queryEngine.executeQuery(queryKey,valTermsKey,and);
                    
                    if (cvValidateTerms != null && cvValidateTerms.size() > 0) {
                        for(int i=0;i<cvValidateTerms.size();i++){
                            
//                            System.out.println("Key:"+key);
                            TemplateTermsBean termsBean = (TemplateTermsBean)cvValidateTerms.get(i);
                            if(termsBean.getAcType() == null ){
                                termsBean.setAcType(TypeConstants.INSERT_RECORD);
                            }
//                            System.out.println("Code:  "+termsBean.getTermsCode());
//                            System.out.println(""+termsBean.getTermsDescription());
//                            System.out.println("Ac type:  "+termsBean.getAcType());
                        }
                    }
                }catch (CoeusException e){
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * To display the fields on sorting order depends on the code
     */
    private void sortOnDisplay() {
        if (cvAwardTermsDetails != null && cvAwardTermsDetails.size() > 0) {
            cvAwardTermsDetails.sort("termsCode", true);
        }
    }
    
    /** This class will sort the column values in ascending and descending order
     * based on number of clicks.
     */
    
    public class ColumnHeaderListener extends MouseAdapter {
        //Modified for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
//        String nameBeanId [][] ={
//            {"0","termsCode" },
//            {"1","termsDescription" }
//        };
        String nameBeanId [][] ={
            {"0","termsCode" },
            {"1","termsDescription" },
            {"2","updateTimestamp"},
            {"2","updateUserName"}
        };
        //COEUSQA-1456 : End
        boolean sort =true;
        /** Mouse click handler for the table headers to sort upon the headers
         * @param evt mouse event
         */
        public void mouseClicked(MouseEvent evt) {
            try {
                JTable table = ((JTableHeader)evt.getSource()).getTable();
                TableColumnModel colModel = table.getColumnModel();
                // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(evt.getX());
                // int mColIndex = table.convertColumnIndexToModel(vColIndex);
                if(cvAwardTermsDetails != null && cvAwardTermsDetails.size()>0 &&
                nameBeanId [vColIndex][1].length() >1 ){
                    ((CoeusVector)cvAwardTermsDetails).sort(nameBeanId [vColIndex][1],sort);
                    if (sort) {
                        sort = false;
                    }
                    else {
                        sort = true;
                    }
                    awardTermsDetailsTableModel.fireTableRowsUpdated(
                    0, awardTermsDetailsTableModel.getRowCount());
                }
            } catch(Exception exception) {
                //exception.printStackTrace();
                exception.getMessage();
            }
        }
    }// End of ColumnHeaderListener.................
    
    /**
     * To set the form data
     * @param TemplateBaseBean Object
     * @return void
     **/
    public void setFormData(Object templateBaseBean) {
        if(templateBaseBean != null){
            this.templateBaseBean = (TemplateBaseBean)templateBaseBean;
        }
        cvTermsName = new CoeusVector();
        cvAllTermsDetails = new CoeusVector();
        cvAwardTermsDetails = new CoeusVector();
        cvAddTermDetails = new CoeusVector();
        // on load set the first row as selected.
        awardTermsForm.tblTermName.setRowSelectionInterval(0,0);
        //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
        //Sets the update time and update user
        try {
            CoeusVector cvUpdateDetails = queryEngine.executeQuery(queryKey, "TERMS_TEMPLATE_UPDATE_DETAIL", CoeusVector.FILTER_ACTIVE_BEANS);
            if(cvUpdateDetails != null && cvUpdateDetails.size() > 0) {
                AwardTemplateBean updateDetail = (AwardTemplateBean)cvUpdateDetails.get(0);
                if(getFunctionType() != TypeConstants.COPY_MODE &&
                        updateDetail.getUpdateTimestamp() != null){
                    String lastUpdate = CoeusDateFormat.format(updateDetail.getUpdateTimestamp().toString());
                    String updateUserName = updateDetail.getUpdateUserName();
                    awardTermsForm.pnlUpdateDetails.setVisible(true);
                    awardTermsForm.txtLastUpdate.setText(lastUpdate);
                    awardTermsForm.txtUpdateUser.setText(updateUserName);
                }else{
                    awardTermsForm.txtLastUpdate.setText(CoeusGuiConstants.EMPTY_STRING);
                    awardTermsForm.txtUpdateUser.setText(CoeusGuiConstants.EMPTY_STRING);
                }
            }else{
                awardTermsForm.pnlUpdateDetails.setVisible(false);
            }
        } catch(Exception e) { 
            e.printStackTrace();
        }
        //COEUSQA-1456 : End
    }
    /**
     * validate method
     * @return boolean
     * @throws edu.mit.coeus.exception.CoeusUIException
     **/
    public boolean validate() throws CoeusUIException {
     
        //Modified for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
        //Hierarchy of validation is changed
        //Hierarchy of validation
        //1. Equipment approval
        //2. Invention
        //3. Other requirement
        //4. Property
        //5. Publication
        //6. Referenced documents
        //7. Rights in data
        //8. Subcontract approval
        //9. Travel
//        String validationHierarchy[] = {AwardConstants.TRAVEL, AwardConstants.SUBCONTRACT_APPROVAL,
//        AwardConstants.RIGHTS_IN_DATA,AwardConstants.PUBLICATION,
//        AwardConstants.PROPERTY,AwardConstants.OTHER_REQUIREMENT,
//        AwardConstants.REFERENCED_DOCUMENTS,AwardConstants.EQUIPMENT_APPROVAL,
//        AwardConstants.INVENTION};
        String validationHierarchy[] = {AwardConstants.EQUIPMENT_APPROVAL,AwardConstants.INVENTION,
        AwardConstants.OTHER_REQUIREMENT,AwardConstants.PROPERTY,AwardConstants.PUBLICATION,
        AwardConstants.REFERENCED_DOCUMENTS,AwardConstants.RIGHTS_IN_DATA,AwardConstants.SUBCONTRACT_APPROVAL,
        AwardConstants.TRAVEL};
        //COEUSQA-1456 : End    
        String key;
        String valTermsKey;
        try {
            for(int index = 0; index < validationHierarchy.length; index++) {
                key = validationHierarchy[index];
                valTermsKey = (String) AwardConstants.awardTerms.get(key);
                //checking for mit award number != null and actype != null & acType != D
                NotEquals notEqAwardNumber = new NotEquals("templateCode",new Integer(0));
                NotEquals notACType = new NotEquals("acType",TypeConstants.DELETE_RECORD);
                Equals acType = new Equals("acType",null);
                Or checkAcType = new Or(notACType,acType);
                And newAnd = new And(notEqAwardNumber,checkAcType);
                CoeusVector cvValidateTerms = queryEngine.executeQuery(queryKey,valTermsKey,newAnd);
                if (cvValidateTerms != null && cvValidateTerms.size() > 0) {
                    continue;
                } else {
                    //Added for COEUSQA-1525 : Attachments for Institute Proposal - Start
//                    index = index + 3;
//                    int position = 0;
//                    if (index > 9) {
//                        CoeusOptionPane.showInfoDialog(coeusMessageResources.
//                        parseMessageKey("awardTerms_exceptionCode.13"+index+""));
//                    } else {
//                        CoeusOptionPane.showInfoDialog(coeusMessageResources.
//                        parseMessageKey("awardTerms_exceptionCode.130"+index+""));
//                    }
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.
                        parseMessageKey("awardTerms_exceptionCode.140"+index+""));
                    int position = 0;
                    //COEUSQA-1525 : End
                    for (position = 0; position < termNames.length; position++) {
                        if (termNames[position].equals(key)) {
                            break;
                        }
                    }
                    awardTermsForm.tblTermName.setRowSelectionInterval(position,position);
                    return false;
                }
            }
        } catch (CoeusException exception) {
            exception.printStackTrace();
        }
        
        return true;
    }
    
    /** This method will refresh the form with the modified data
     * @return void
     */
    
    public void refresh(){
        if (!isRefreshRequired())
            return ;
        //Modified for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
        if(getFunctionType() == TypeConstants.COPY_MODE){
            setFunctionType(TypeConstants.MODIFY_MODE);
        }
        //Modified for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - End
        setFormData(null);
        setSaveRequired(true);
        awardTermsForm.tblTermName.clearSelection();
        if( awardTermsForm.tblTermName.getRowCount() > 0 ){
            awardTermsForm.tblTermName.setRowSelectionInterval(0, 0);
        }
        setRefreshRequired(false);
    }
    
    /** This method will specify the action performed
     * @param actionEvent ActionEvent
     * @return void
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (source.equals(awardTermsForm.btnAdd)) {
            performAddOperation();
        } else if (source.equals(awardTermsForm.btnDelete)) {
            performDeleteOperation();
        } else if (source.equals(awardTermsForm.btnSync)) {
            //            performSyncOperation();
        }
    }
    
    /** This method will specify the action performed during the selection change
     * @param listSelectionEvent ListSelectionEvent
     * @return void
     */
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        int selRow = awardTermsForm.tblTermName.getSelectedRow();
        if( selRow == -1 ) return ;
        selectedValue = termNames[selRow];
        String labelName = firstName  +" "+ selectedValue +" "+ lastName;
        awardTermsForm.lblApplicable.setText(labelName);
        termsKey = (String) AwardConstants.awardTerms.get(selectedValue);
        try {
            Equals eqTemplNo = new Equals("templateCode",
            new Integer(templateBaseBean.getTemplateCode()));
            //NotEquals notEqDelete = new NotEquals("acType", TypeConstants.DELETE_RECORD);
            And awardNoAndneDelete = new And(eqTemplNo, CoeusVector.FILTER_ACTIVE_BEANS);
            
            cvAllTermsDetails = queryEngine.getDetails(queryKey, termsKey);
            
            cvAwardTermsDetails = (CoeusVector) ObjectCloner.deepCopy(
            cvAllTermsDetails.filter(awardNoAndneDelete));
            
        } catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        //prevSelectedRow = selRow;
        // while displaying sort it based on the code
        sortOnDisplay();
        awardTermsDetailsTableModel.setData(cvAwardTermsDetails);
        awardTermsDetailsTableModel.fireTableDataChanged();
    }
    
    /**
     * This method will specify the actions during Add operation
     * @return void
     */
    private void performAddOperation() {
        //Filtering for the values to be displayed in the Add screen
        Equals eqAwardNumber = new Equals("templateCode",new Integer(0));
        Equals eqACType = new Equals("acType",TypeConstants.DELETE_RECORD);
        
        Or awardNumberOrAcType = new Or(eqAwardNumber,eqACType);
        int selRow = awardTermsForm.tblTermName.getSelectedRow();
        selectedValue = termNames[selRow];
        String termsKey = (String) AwardConstants.awardTerms.get(selectedValue);
        try {
            cvAllTermsDetails = queryEngine.getDetails(queryKey, termsKey);
        } catch (CoeusException exception) {
            exception.printStackTrace();
        }
        cvAddTermDetails = cvAllTermsDetails.filter(awardNumberOrAcType);
        try {
            awardTermsForm.setCursor( new Cursor(Cursor.WAIT_CURSOR));
            
            TemplateAddTermsController templateAddTermsController =
            new TemplateAddTermsController(mdiForm, queryKey, selectedValue, templateBaseBean);
            
            templateAddTermsController.setFormData(cvAddTermDetails);
            
            if (cvAwardTermsDetails != null && templateAddTermsController != null) {
                //getting the selected values
                CoeusVector cvVal = templateAddTermsController.display();
                if (cvVal != null && cvVal.size() > 0) {
                    cvAwardTermsDetails.addAll(cvVal);
                    awardTermsDetailsTableModel.setData(cvAwardTermsDetails);
                    awardTermsDetailsTableModel.fireTableDataChanged();
                    
                    for (int index = 0; index < cvVal.size(); index++) {
                        AwardTermsBean awardTermsBean = (AwardTermsBean)cvVal.get(index);
                        
                        //queryEngine.setData(queryKey,termsKey,awardTermsBean);
                        Equals eqTermsCode = new Equals("termsCode", new Integer(awardTermsBean.getTermsCode()));
                        queryEngine.removeData(queryKey,termsKey,eqTermsCode);
                        queryEngine.addData(queryKey,termsKey, awardTermsBean);
                    }
                    int selected = awardTermsForm.tblTermName.getSelectedRow();
                    awardTermsTableModel.fireTableRowsUpdated(selected,selected);
                    
                }
            }
        }
        finally{
            awardTermsForm.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ));
        }
        
    }
    
    /**
     * This method will specify the actions during Delete operation
     * @return void
     */
    private void performDeleteOperation() {
        int rowIndex = awardTermsForm.tblTermDetails.getSelectedRow();
        if (rowIndex == -1) {
            CoeusOptionPane.showErrorDialog(SELECT_A_ROW);
            return;
        }
        if (rowIndex >= 0) {
            String mesg = DELETE_CONFIRMATION;
            int selectedOption = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(mesg),
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                
                AwardTermsBean awardTermsBean = (AwardTermsBean)cvAwardTermsDetails.get(rowIndex);
                
                if (awardTermsBean.getAcType() == null) {
                    awardTermsBean.setAcType(TypeConstants.DELETE_RECORD);
                } else if (awardTermsBean.getAcType().equals(TypeConstants.INSERT_RECORD)) {
                    awardTermsBean.setAcType(null);
                    awardTermsBean.setMitAwardNumber(null);
                    awardTermsBean.setSequenceNumber(-1);
                }
                
                //queryEngine.setData(queryKey,termsKey,awardTermsBean);
                Equals eqTermsCode = new Equals("termsCode", new Integer(awardTermsBean.getTermsCode()));
                queryEngine.removeData(queryKey,termsKey,eqTermsCode);
                queryEngine.addData(queryKey,termsKey, awardTermsBean);
                cvAwardTermsDetails.removeElementAt(rowIndex);
                
                awardTermsDetailsTableModel.fireTableRowsDeleted(rowIndex,rowIndex);
                int selected = awardTermsForm.tblTermName.getSelectedRow();
                awardTermsTableModel.fireTableRowsUpdated(selected,selected);
            }
        }
    }
    /**
     * This method will specify the actions during Sync operation
     * @return void
     */
    //    private void performSyncOperation(){
    //        int option = CoeusOptionPane.showQuestionDialog(
    //        coeusMessageResources.parseMessageKey(CONFIRM_SYNC),
    //        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
    //
    //        switch( option ){
    //            case (JOptionPane.YES_OPTION ):
    //                //Call the Sync Terms of the AwardController
    //                if ( syncTerms(EMPTY, getTemplateCode()) ){
    //                    setFormData(null);
    //                    setSaveRequired(true);
    //                    awardTermsForm.tblTermName.clearSelection();
    //                    if( awardTermsForm.tblTermName.getRowCount() > 0 ){
    //                        awardTermsForm.tblTermName.setRowSelectionInterval(0, 0);
    //                    }
    //                }
    //                break;
    //            case (JOptionPane.NO_OPTION ):
    //                break;
    //            default:
    //                break;
    //        }
    //    }
    /**
     * Actions on click of mouse
     * @param mouseEvent java.awt.event.MouseEvent
     * @return void
     **/
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2 && getFunctionType() != TypeConstants.DISPLAY_MODE) {
            performAddOperation();
        }
    }
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    public void mousePressed(MouseEvent e) {
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    /**
     * This is an inner class represents the table model for the Terms
     * screen table
     **/
    public class AwardTermsDetailsTableModel extends AbstractTableModel {
        
        
        //Modified for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
        // represents the column names of the table
//        private String colName[] = {"Code","Description"};
        // represents the column class of the fields of table
//        private Class colClass[] = {String.class, String.class};
        // represents the column names of the table
        private String colName[] = {"Code","Description","Last Updated","Updated By"};
        //COEUSQA-1456 : End
        // represents the column class of the fields of table
        private Class colClass[] = {String.class, String.class,String.class, String.class};
        /**
         * To get the column class of the table
         * @param col int
         * @return Class
         **/
        public Class getColumnClass(int col) {
            return colClass[col];
        }
        
        /**
         * To check whether the table cell is editable or not
         * @param row int
         * @param col int
         * @return boolean
         **/
        public boolean isCellEditable(int row, int col){
            return false;
        }
        /**
         * To get the column count of the table
         * @return int
         **/
        public int getColumnCount() {
            return colName.length;
        }
        
        /**
         * To get the row count of the table
         * @return int
         **/
        public int getRowCount() {
            if (cvAwardTermsDetails == null){
                return 0;
            } else {
                return cvAwardTermsDetails.size();
            }
        }
        
        /**
         * To set the data for the model.
         * @param cvAwardTermsDetails CoeusVector
         * @return void
         **/
        public void setData(CoeusVector cvAwardTermsDetails) {
            cvAwardTermsDetails = cvAwardTermsDetails;
        }
        /**
         * To get the value from the table
         * @param rowIndex int
         * @param columnIndex int
         * @return Object
         **/
        public Object getValueAt(int rowIndex, int columnIndex) {
            //have to change to the value from bean
            TemplateTermsBean templateTermsBean = (TemplateTermsBean)cvAwardTermsDetails.get(rowIndex);
            if (templateTermsBean != null) {
                switch(columnIndex) {
                    case CODE_COLUMN:
                        return ""+templateTermsBean.getTermsCode();
                    case DESCRIPTION_COLUMN:
                        return templateTermsBean.getTermsDescription();
                    //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
                    case LAST_UPDATE_COLUMN:
                        if(getFunctionType() != TypeConstants.COPY_MODE &&
                                templateTermsBean.getUpdateTimestamp() != null){
                            return CoeusDateFormat.format(templateTermsBean.getUpdateTimestamp().toString());
                        }else{
                            return CoeusGuiConstants.EMPTY_STRING;
                        }
                    case UPDATED_BY_COLUMN:
                        if(getFunctionType() != TypeConstants.COPY_MODE &&
                                templateTermsBean.getUpdateUserName() != null){
                            return templateTermsBean.getUpdateUserName();
                        }else{
                            return CoeusGuiConstants.EMPTY_STRING;
                        }
                    //COEUSQA-1456 : End
                }
            }
            return EMPTY;
        }
        
        /**
         * To set the value in the table
         * @param value Object
         * @param row int
         * @param col int
         * @return void
         **/
        public void setValueAt(Object value, int row, int col) {
            //have to set value in bean
        }
        /**
         * To get the column name
         * @param col int
         * @return String
         **/
        public String getColumnName(int col) {
            return colName[col];
        }
    }
    
    /**
     * This is an inner class represents the table model for the Terms
     * screen table
     **/
    public class AwardTermsTableModel extends AbstractTableModel {
        
        // represents the column names of the table
        private String colName[] = {EMPTY,EMPTY};
        // represents the column class of the fields of table
        private Class colClass[] = {ImageIcon.class, String.class};
        
        /**
         * To get the column class of the table
         * @param col int
         * @return Class
         **/
        public Class getColumnClass(int col) {
            return colClass[col];
        }
        
        /**
         * To check whether the table cell is editable or not
         * @param row int
         * @param col int
         * @return boolean
         **/
        public boolean isCellEditable(int row, int col){
            return false;
        }
        
        /**
         * To get the column count of the table
         * @return int
         **/
        public int getColumnCount() {
            return colName.length;
        }
        
        /**
         * To get the row count of the table
         * @return int
         **/
        public int getRowCount() {
            
            return termNames.length;
        }
        
        /**
         * To set the data for the model.
         * @param cvBudgetTableData CoeusVector
         * @return void
         **/
        public void setData(CoeusVector cvTermsName) {
            cvTermsName = cvTermsName;
        }
        /**
         * To get the value from the table
         * @param rowIndex int
         * @param columnIndex int
         * @return Object
         **/
        public Object getValueAt(int rowIndex, int columnIndex) {
            
            switch(columnIndex) {
                case IMAGE_COLUMN:
                    return EMPTY;
                case TEXT_COLUMN:
                    return termNames[rowIndex];
            }
            return EMPTY;
        }
        
        /**
         * To set the value in the table
         * @param value Object
         * @param row int
         * @param col int
         * @return void
         **/
        public void setValueAt(Object value, int row, int col) {
            //have to set value in bean
            
        }
        /**
         * To get the column name
         * @param col int
         * @return String
         **/
        public String getColumnName(int col) {
            return colName[col];
        }
    }
    
    /**
     * This is an inner class represents the table cell renderer for the Award For Budget
     * screen table
     **/
    public class AwardTermsTableCellRenderer extends DefaultTableCellRenderer {
        
        URL emptyPageUrl = getClass().getClassLoader().getResource(CoeusGuiConstants.NEW_ICON);
        URL fillPageUrl = getClass().getClassLoader().getResource(CoeusGuiConstants.DATA_ICON);
        private JLabel lblIcon;
        ImageIcon EMPTY_PAGE_ICON, FILL_PAGE_ICON;
        
        /**
         * Default Constructor
         **/
        public AwardTermsTableCellRenderer() {
            
            lblIcon = new JLabel();
        }
        /**
         * To get the table cell editor component
         * @param table javax.swing.JTable
         * @param value Object
         * @param isSelected boolean
         * @param row int
         * @param column int
         * @return java.awt.Component
         **/
        public Component getTableCellRendererComponent(JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row,
        int column) {
            switch(column){
                case IMAGE_COLUMN:
                    if(emptyPageUrl != null) {
                        EMPTY_PAGE_ICON = new ImageIcon(emptyPageUrl);
                        lblIcon.setIcon(EMPTY_PAGE_ICON);
                    }
                    
                    if(fillPageUrl != null) {
                        FILL_PAGE_ICON = new ImageIcon(fillPageUrl);
                        lblIcon.setIcon(FILL_PAGE_ICON);
                    }
                    
                    selectedValue = termNames[row];
                    
                    String termsKey = (String) AwardConstants.awardTerms.get(selectedValue);
                    CoeusVector cvFilteredAwardTerms = new CoeusVector();
                    CoeusVector cvAllTermsDetails = new CoeusVector();
                    try {
                        Equals eqTemplNo = new Equals("templateCode",
                        new Integer(templateBaseBean.getTemplateCode()));
                        And awardNoAndneDelete = new And(eqTemplNo, CoeusVector.FILTER_ACTIVE_BEANS);
                        cvAllTermsDetails = queryEngine.getDetails(queryKey, termsKey);
                        cvFilteredAwardTerms = (CoeusVector) ObjectCloner.deepCopy(
                        cvAllTermsDetails.filter(awardNoAndneDelete));
                        
                    } catch (CoeusException coeusException) {
                        coeusException.printStackTrace();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    if(cvFilteredAwardTerms != null && cvFilteredAwardTerms.size() > 0) {
                        lblIcon.setIcon(FILL_PAGE_ICON);
                    } else {
                        lblIcon.setIcon(EMPTY_PAGE_ICON);
                    }
                    return lblIcon;
                    
                case TEXT_COLUMN:
                    
            }
            return super.getTableCellRendererComponent(table, value,
            isSelected, hasFocus, row, column);
        }
    }// End of AwardTermsTableCellRenderer class..............
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
            setBorder(new EmptyBorder(0, 0, 0, 0));
            ListCellRenderer renderer = getCellRenderer();
            ((JLabel) renderer).setHorizontalAlignment(JLabel.CENTER);
            setCellRenderer(renderer);
        }
        
        public Component getTableCellRendererComponent(JTable table,
        Object value,boolean isSelected, boolean hasFocus, int row, int column){
            return this;
        }
    }
    
}//End Class
