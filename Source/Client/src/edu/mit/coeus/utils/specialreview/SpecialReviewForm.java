/**
 * @(#)SpecialReviewForm.java  1.0  April 12, 2003, 12:17 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 13-JULY-2011
 * by Bharati  
 */

package edu.mit.coeus.utils.specialreview;

import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.irb.gui.ProtocolDetailForm;
import edu.mit.coeus.propdev.gui.ProposalDetailForm;
import edu.mit.coeus.propdev.gui.ProposalOrganizationAdminForm;
import edu.mit.coeus.propdev.gui.ProposalSpecialReviewForm;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.AuthorizationOperator;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.Operator;
import edu.vanderbilt.coeus.gui.CoeusHelpGidget;

import java.math.BigDecimal;

import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.AbstractCellEditor;
import javax.swing.table.TableCellEditor;



/**
 * SpecialReviewForm is an abstract form object which is used to display
 * the Special Reviews and it is used to add/delete/display/Modify the Special Reviews.
 * @version 1.0 April 12, 2003, 12:17 PM
 * @author Raghunath P.V.
 */
public class SpecialReviewForm extends javax.swing.JComponent implements TypeConstants, ActionListener, FocusListener, ListSelectionListener,ItemListener,MouseListener{
    
    private char functionType;
    private boolean saveRequired;
   
    //Vector to hold special review type Codes - Lookup values
    private Vector vecSpecialReviewCode;
    private Vector vecDeletedSpecialreviewCodes;
    //Vector to hold approval type Codes - Lookup values
    private Vector vecApprovalTypeCode;
    //Vector to hold special review data
    private Vector vecSpecialReviewData;
    //Vector to hold Special Review Va444idate Beans used for validations - Lookup values
    private Vector vecValidateRules;
    //Vector To hold  special review Description Values
    private Vector vecSpecialReviewCodeDescriptions;
    //Vector To hold  Approval Description Values
    private Vector vecApprovalCodeDescriptions;
    // Combo used as a Cell Editor for special review code
    private JComboBox specialCombo;
    // Combo used as a Cell Editor for special review code
    private JComboBox approvalCombo;
    /* This is used to hold MDI form reference */
    private CoeusAppletMDIForm mdiReference;
    private boolean firstEntry = false;
    // This will hold the latest row Selected and this is set in list selection listener
    // for bug fix 958
    private int selRow;
    //holds the last selected Row
    private int lastSelectedRow = 0;
    //holds the previous comment
    private String prvComments = "";
    //holds the  comment
    private String absComments = "";
    //holds the so far saved info
    private boolean isFormDataUpdated = false;

    /** instance of DateUtils object which will be used for formatting dates */
    private DateUtils dtUtils;
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    private SpecialReviewFormBean specialReviewFormBean;
    /** used to parse the dates which are represented as strings */
    private java.text.SimpleDateFormat dtFormat;
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
    private static final int ZERO_COUNT = 0;
    private int previousSelRow = -1;
    //Added for Coeus Enhancement Case #1799 - start:  step 1
    private boolean isHumanSub = false;
    private static final String AUTH_SERVLET = "/AuthorizationServlet";
    private String specialRevTypeCodeParam= "";
    private Vector humanSubRows;
    private int enableProtocolLink;
    private boolean viewProtocol, viewAnyProtocol;
    private static final String ENTER_PROTOCOL = "SpecialReviewForm_exceptionCode.1065";
    private static final String DUPLICATE_PROTOCOL = "SpecialReviewForm_exceptionCode.1066";
    private static final String VIEW_PROTOCOL = "SpecialReviewForm_exceptionCode.1067";
    private static final String CHOOSE_PROTOCOL = "SpecialReviewForm_exceptionCode.1068";
    private String moduleCode;
    private ColumnValueEditor columnValueEditor;
    //Coeus Enhancement Case #1799 - end:  step 1
    private TableCellEditor dateEditComponent;
    ScreenFocusTraversalPolicy traversal;
    
    private String EMPTY_STRING = "";
    //Added for Case#4354 - When ENABLE_PROTOCOL_TO_DEV_PROPOSAL is set to ON - Start
    private static final String EXEMPT_STATUS = "203";
    private static final String EMPTY_SPACE = " ";
    //Case#4353 - End

    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
    private int enableIacucProtocolLink;
    private boolean isAnimalSub = false;
    private Vector animalSubRows;
    private String specialRevTypeCodeParamForIacuc = "";
    private Vector vecSpRvData;
    private static final String HUMAN_SUBJECTS = "Human Subjects";
    private static final String ANIMAL_USAGE = "Animal Usage";
    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
    
    private Vector protocolSpecialReview;
    private Vector protocolInvestigator;
    private Vector protocolGeneralInfo;
     private Vector protocolOrganization;
    
    /** Creates new form SpecialReviewForm */
    public SpecialReviewForm() {
    }
    
    /** Creates new form <CODE>SpecialReviewForm</CODE>
     *
     * @param functionType this will open the different mode like Display. 'D' specifies that the form is in Display Mode
     * @param vecSpecialReviewData is a Vector of Special review info beans.
     * @param strModuleCode specifies the module from which this class is instantiated.
     */
    public SpecialReviewForm(char functionType, Vector vecSpecialReviewData, String strModuleCode) {
        
        this.functionType = functionType;
        this.vecSpecialReviewData = vecSpecialReviewData;
        this.vecDeletedSpecialreviewCodes = new Vector();
        this.dtUtils = new DateUtils();
        this.dtFormat = new java.text.SimpleDateFormat("MM/dd/yyyy");
        approvalCombo = new JComboBox();
        approvalCombo.setFont(CoeusFontFactory.getNormalFont());
        specialCombo = new JComboBox();
        specialCombo.setFont(CoeusFontFactory.getNormalFont());
        //Added for Coeus Enhancement Case #1799 - start:  step 2
        this.moduleCode = strModuleCode;
        humanSubRows= new Vector();
        //Coeus Enhancement Case #1799 - end:  step 2
        //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
        animalSubRows= new Vector();
        //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
        dateEditComponent = new DateEditor("ApplDate"); //// Handle
        initComponents();
        setTableKeyTraversal();
        //if(strModuleCode.equals("PROPOSAL")) {
        pnlProposalDescription.setVisible(false);
        sptrProposalDescription.setVisible(false);
        //}
        
        if(functionType == 'D' ){
            //txtArea  .setBackground();
            txtAreaComments.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        }
    }
    
    
    
    /** This is setter method for Proposal Description Panel.
     * This method sets the label and value label objects present in the Proposal Description Panel
     * @param proposalNumber Proposal Number to be displayed
     * @param sponsorName  Sponsor Code and Name to be displayed
     */
    public void setProposalDescription(String proposalNumber, String sponsorName) {
        lblProposalValue.setText(proposalNumber);
        lblProposalNo.setText("Proposal Number:");
        lblSponsorValue.setText(sponsorName);
        lblSponsor.setText("Sponsor:");
    }
    
    /** This method is used to add given the Button Objects into the Pannel
     * @param btnOk OK button to be added to panel
     * @param btnCancel Cancel button to be added to panel
     */
    public void setButtonsReference(javax.swing.JButton btnOk, javax.swing.JButton btnCancel ){
        java.awt.GridBagConstraints gridBagConstraints;
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        pnlOkCancelButtons.add(btnOk,gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        pnlOkCancelButtons.add(btnCancel,gridBagConstraints);
        Component[] comp = { tblSpecialReview,btnAdd,btnDelete,btnFind,txtAreaComments,btnOk,btnCancel };
        traversal = new ScreenFocusTraversalPolicy(comp);
        setFocusTraversalPolicy(traversal);
        setFocusCycleRoot(true);
    }
    
    /** This method is used to initialize the components, set the data in the components.
     * @param mdiForm is a reference of CoeusAppletMDIForm
     * @return a JPanel containing all the components with the data populated.
     */
    public JComponent showSpecialReviewForm(CoeusAppletMDIForm
            mdiForm){
        
        this.mdiReference = mdiForm;
        //initComponents();
        
        // This method adds the listeners to all the buttons and to the Jtable
        setListenersForButtons();
        //This method is used to Set AcTypes
        setAcTypes();
        
        // This method Adds the data to the form i.e. Jtable
        setFormData();
        // This method enables/ disables the Buttons based on the function type.
        formatFields();
        // This method does the settings to the jtable.
        setTableEditors();
        /* This logic is used to select the first row in the list of available
           rows in JTable*/
        /*Moved the code to enableDisableButtons method for reusability.
          Modified with COEUSDEV 271:special review not selected when copying from proposal to award*/
        enableDisableButtons();
        // setting bold property for table header values
        tblSpecialReview.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        coeusMessageResources = CoeusMessageResources.getInstance();
        if( traversal == null ) {
            Component[] comp = { tblSpecialReview,btnAdd,btnDelete,txtAreaComments };
            traversal = new ScreenFocusTraversalPolicy(comp);
            setFocusTraversalPolicy(traversal);
            //Commented for COEUSDEV 271:special review not selected when copying from proposal to award
//            setFocusCycleRoot(true);
        }
        return this;
    }
    public void requestDefaultFocusForComponent(){
        tblSpecialReview.requestFocusInWindow();
    }
    /**
     * This Method is used to set the Vector which contains the special review Form beans.
     *
     * @param specialReviewData is a vector which contain the collection of SpecialReviewInfoBeans
     */
    public void setSpecialReviewData(Vector specialReviewData){
        this.vecSpecialReviewData = specialReviewData;
        setAcTypes();
    }
    
    private JTable getTableInstance(){
        return tblSpecialReview;
    }
    
    /** This method is used to set the Vector which contain the values for
     * validating the row data based on the Special Review code and approval code
     *
     * @param validateVector is a Vector which contain the SRApprovalInfoBean collection
     */
    
    public void setValidateVector(Vector validateVector){
        this.vecValidateRules = validateVector;
    }
    
    private boolean display = true;//For Coeus Enhancement Case #1799
    public void setTableKeyTraversal(){
        
        javax.swing.InputMap im = tblSpecialReview.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
        final Action oldTabAction = tblSpecialReview.getActionMap().get(im.get(tab));
        Action tabAction = new AbstractAction() {
            int row = 0;
            int column =0;
            public void actionPerformed(ActionEvent e) {
                display = true;
                oldTabAction.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                int rowCount = table.getRowCount();
                int columnCount = table.getColumnCount();
                row = table.getSelectedRow();
                column = table.getSelectedColumn();
                if(rowCount<1){
                    columnCount = 0;
                    row = 0;
                    column=0;
                    //projectIncomeForm.btnOk.requestFocusInWindow();
                    return ;
                }
                while (! table.isCellEditable(row, column) ) {
                    
                    column += 1;
                    if(column > 5){
                        column = 0;
                        row+=1;
                    }
                    if (row == rowCount) {
                        row = 0;
                    }
                    
                    if (row == table.getSelectedRow()
                    && column == table.getSelectedColumn()) {
                        break;
                    }
                }
                //Added for the Coeus Enhancement Case #1799
                if(!moduleCode.equals("PROPOSAL")){
                    if(isHumanSub){
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                tblSpecialReview.changeSelection(row, column, false, false);
                            }
                        });
                    }else {
                        tblSpecialReview.changeSelection(row, column, false, false);
                    }
                }else{
                    table.changeSelection(row, column, false, false);
                }
                //End Coeus Enhancement Case #1799
                
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                if(!moduleCode.equals("PROPOSAL")){
                    if(isAnimalSub){
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                tblSpecialReview.changeSelection(row, column, false, false);
                            }
                        });
                    }else {
                        tblSpecialReview.changeSelection(row, column, false, false);
                    }
                }else{
                    table.changeSelection(row, column, false, false);
                }
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
                
            }
        };
        tblSpecialReview.getActionMap().put(im.get(tab), tabAction);
        final Action oldShiftTabAction = tblSpecialReview.getActionMap().get(im.get(shiftTab));
        Action tabShiftAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                oldShiftTabAction.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                int rowCount = table.getRowCount();
                int columnCount = table.getColumnCount();
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                
                while (! table.isCellEditable(row, column) ) {
                    column -= 1;
                    if (column <= 0) {
                        column = 3;
                        row -=1;
                    }
                    if (row < 0) {
                        row = rowCount-1;
                    }
                    if (row == table.getSelectedRow()
                    && column == table.getSelectedColumn()) {
                        break;
                    }
                }
                if(isHumanSub){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            tblSpecialReview.changeSelection(tblSpecialReview.getSelectedRow(), tblSpecialReview.getSelectedColumn(), false, false);
                        }
                    });
                }else {
                    tblSpecialReview.changeSelection(tblSpecialReview.getSelectedRow(), tblSpecialReview.getSelectedColumn(), false, false);
                }
            }
        };
        tblSpecialReview.getActionMap().put(im.get(shiftTab), tabShiftAction);
    }
    
    
    
    /**
     * This method is used to trigger the save required flag,
     * when the user Clicks the Save button and the user is in the Editor Field.
     */
    
    public void handleEditableComponents(){
        String acType = "";
        int rowCount = tblSpecialReview.getRowCount();
        String oldComments="";
        if( rowCount > 0 ) {
            int selRow = tblSpecialReview.getSelectedRow();
            int colNum = tblSpecialReview.getSelectedColumn();
            if(selRow != -1){
                if(tblSpecialReview.isEditing()){
                    //                    tblSpecialReview.getCellEditor(selRow,colNum).cancelCellEditing();// Added by chandra for the bug fix #1043-14th July 2004
                    tblSpecialReview.getCellEditor(selRow,colNum).stopCellEditing();// Added by chandra for the bug fix #1043- 14th July 2004
                }
                if( vecSpecialReviewData != null  &&
                        vecSpecialReviewData.size() > selRow){
                    SpecialReviewFormBean specialReviewFormBean =
                            (SpecialReviewFormBean)vecSpecialReviewData.get(selRow);
                    if(specialReviewFormBean.getComments()!= null){
                        oldComments = specialReviewFormBean.getComments().trim();
                    }
                    String newComments = txtAreaComments.getText().trim();
                    if(firstEntry){
                        if( ( oldComments == null && newComments != null) ||
                                ( oldComments != null && newComments == null) ||
                                ( ( oldComments != null && newComments != null) &&
                                !oldComments.equals(newComments) ) ){
                            saveRequired = true;
                            specialReviewFormBean.setComments(newComments);
                            acType = specialReviewFormBean.getAcType();
                            if( acType == null ) {                                
                                specialReviewFormBean.setAcType(UPDATE_RECORD);
                            }
                        }
                        //                        else{
                        //                            specialReviewFormBean.setAcType(UPDATE_RECORD);
                        //                        }
                    }
                }
            }
        }
    }
    
    /** This method is used to perform Validations.
     *
     * @return true if the validation succeed, false otherwise.
     * @throws Exception is a exception to be thrown in the client side.
     */
    public boolean validateData() throws Exception{
        
        boolean valid=true;
        int rowCount = tblSpecialReview.getRowCount();
        if(rowCount >= 0){
            if(tblSpecialReview.getCellEditor() != null){
                tblSpecialReview.getCellEditor().stopCellEditing();
            }
           
            for(int inInd=0; inInd < rowCount ;inInd++){
                
                String stSpecialDesc = (String)((DefaultTableModel)
                tblSpecialReview.getModel()).
                        getValueAt(inInd,1);
                String stApprovalDesc = (String)((DefaultTableModel)
                tblSpecialReview.getModel()).
                        getValueAt(inInd,2);
                if((stSpecialDesc == null) || (stSpecialDesc.trim().length() <= 0)){
                    valid=false;
                    tblSpecialReview.setRowSelectionInterval(inInd,inInd);
                    errorMessage(coeusMessageResources.parseMessageKey(
                            "SpecialReviewForm_exceptionCode.1063"));
                    break;
                }
                
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                //If special review is of type human subject then enable the find button
                
                
                if(specialRevTypeCodeParam.equals("1") && stSpecialDesc.equals("Human Subjects")){
                    // 4154: Problems in IRB Linking - Start
                    if(enableProtocolLink == 1 && functionType!=TypeConstants.DISPLAY_MODE) {
                        if(specialRevTypeCodeParam != null && !EMPTY_STRING.equals(specialRevTypeCodeParam) &&
                                Integer.parseInt(getIDForName(stSpecialDesc, vecSpecialReviewCode))==Integer.parseInt(specialRevTypeCodeParam)) {
                            //Modified for COEUSDEV-312 :First Protocol on Proposal not being saved on Award record- Start
//                        if(stApprovalDesc.equals(null)  ||  stApprovalDesc.equals("")) {
//                        if(stApprovalDesc == null  ||  stApprovalDesc.equals("")) {
                            if(moduleCode.equals("INSTITUTE_PROPOSAL") ||
                                    moduleCode.equals("Award_Module") ||
                                    moduleCode.trim().equals("PROPOSAL")){
                                //COEUSDEV-312 : End
                                // To Identify the Invalid Protocol Numbers with Human Subjects Spl Review
                                // entered while the Link Param is 0.
                                // If there is no Approval Type, set human subject flag true.
                                isHumanSub = true;
                            } else{
                                isHumanSub = false;
                            }
                        }else {
                            isHumanSub = false;
                        }
                        btnFind.setEnabled(isHumanSub);
                    }
                    if(isHumanSub){
                        String protocolNumber = (String)tblSpecialReview.getValueAt(inInd, 3);
                        ProtocolInfoBean protocolInfoBean = getProtocolDetails(protocolNumber);
                        if(protocolInfoBean == null || protocolInfoBean.getProtocolNumber() == null){
                            //Modified for COEUSDEV-312 :First Protocol on Proposal not being saved on Award record- Start
                            //To set focus after validation
                            setRequestFocusInSpecialReviewThread(inInd,3);
                            //COEUSDEV-312 : End
                            errorMessage(coeusMessageResources.parseMessageKey("SpecialReviewForm_exceptionCode.1065"));
                        }
                    }
                    //  4154: Problems in IRB Linking - End
                    if((moduleCode.equals("INSTITUTE_PROPOSAL") || moduleCode.equals("Award_Module")) && isHumanSub){
                        if((stApprovalDesc == null) || (stApprovalDesc.trim().length() <= 0)){
                            valid=false;
                            tblSpecialReview.setRowSelectionInterval(inInd,inInd);
                            //Added for the Coeus Enhancement Case #1799 - start:  step 4
                            if(isHumanSub){
                                errorMessage(coeusMessageResources.parseMessageKey(ENTER_PROTOCOL));
                                
                                break;
                            }else{
                                errorMessage(coeusMessageResources.parseMessageKey(
                                        "SpecialReviewForm_exceptionCode.1064"));
                                break;
                            }//End Coeus Enhancement case:#1799 step:4
                        }
                    }else if(moduleCode.equals("PROPOSAL") && isHumanSub){
                        // String protoNum = (String)tblSpecialReview.getValueAt(inInd, 3);
                        if((stApprovalDesc == null) || (stApprovalDesc.trim().length() <= 0)){
                            valid=false;
                            tblSpecialReview.setRowSelectionInterval(inInd,inInd);
                            // 4154: Problems in IRB Linking - Start
                            if(isHumanSub){
                                errorMessage(coeusMessageResources.parseMessageKey(ENTER_PROTOCOL));
                                break;
                                // 4154: Problems in IRB Linking - End
                            }else{errorMessage(coeusMessageResources.parseMessageKey(
                                    "SpecialReviewForm_exceptionCode.1064"));
                            break;
                            }
                        }
                    }
                    //If special review is of type human subject then enable the find button
                }else if(specialRevTypeCodeParamForIacuc.equals("2")){
                     if(enableIacucProtocolLink == 1 && functionType!=TypeConstants.DISPLAY_MODE) {
                        if(specialRevTypeCodeParamForIacuc != null && !EMPTY_STRING.equals(specialRevTypeCodeParamForIacuc) &&
                                Integer.parseInt(getIDForName(stSpecialDesc, vecSpecialReviewCode))==Integer.parseInt(specialRevTypeCodeParamForIacuc)) {
                            if(moduleCode.equals("INSTITUTE_PROPOSAL") ||
                                    moduleCode.equals("Award_Module") ||
                                    moduleCode.trim().equals("PROPOSAL")){                          
                                // To Identify the Invalid Protocol Numbers with Animal Subjects Spl Review
                                // entered while the Link Param is 0.
                                // If there is no Approval Type, set Animal subject flag true.
                                isAnimalSub = true;
                            } else{
                                isAnimalSub = false;
                            }
                        }else {
                            isAnimalSub = false;
                        }
                        btnFind.setEnabled(isAnimalSub);
                    }
                     
                    //If Special Review is valid iacuc protocol then sync its actual status
                    if(isAnimalSub){
                        String protocolNumber = (String)tblSpecialReview.getValueAt(inInd, 3);
                        edu.mit.coeus.iacuc.bean.ProtocolInfoBean protocolInfoBean = getIacucProtocolDetails(protocolNumber);
                        if(protocolInfoBean == null || protocolInfoBean.getProtocolNumber() == null){
                            //To set focus after validation
                            setRequestFocusInSpecialReviewThread(inInd,3);
                            errorMessage(coeusMessageResources.parseMessageKey("SpecialReviewForm_exceptionCode.1065"));
                        }
                    }
       
                    if(moduleCode.equals("INSTITUTE_PROPOSAL") || moduleCode.equals("Award_Module")){
                        if((stApprovalDesc == null) || (stApprovalDesc.trim().length() <= 0)){
                            valid=false;
                            tblSpecialReview.setRowSelectionInterval(inInd,inInd);
                            if(isAnimalSub){
                                errorMessage(coeusMessageResources.parseMessageKey(ENTER_PROTOCOL));
                                
                                break;
                            }else{
                                errorMessage(coeusMessageResources.parseMessageKey(
                                        "SpecialReviewForm_exceptionCode.1064"));
                                break;
                            }
                        }
                    }else if(moduleCode.equals("PROPOSAL")){
                        if((stApprovalDesc == null) || (stApprovalDesc.trim().length() <= 0)){
                            valid=false;
                            tblSpecialReview.setRowSelectionInterval(inInd,inInd);
                            if(isAnimalSub){
                                errorMessage(coeusMessageResources.parseMessageKey(ENTER_PROTOCOL));
                                break;
                            }else{errorMessage(coeusMessageResources.parseMessageKey(
                                    "SpecialReviewForm_exceptionCode.1064"));
                            break;
                            }
                        }
                    }
                }
                
                //Added for COEUSQA-- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
                
                String specialDesc = (String)tblSpecialReview.getValueAt(inInd, 1);
                String approvalDesc = (String)tblSpecialReview.getValueAt(inInd, 2);
                String proNum = (String)tblSpecialReview.getValueAt(inInd, 3);
                String apDate = (String)tblSpecialReview.getValueAt(inInd, 4);
                String arDate = (String)tblSpecialReview.getValueAt(inInd, 5);
                if(specialDesc != null && specialDesc.trim().length() > 0 &&
                        approvalDesc != null && approvalDesc.trim().length() > 0){
                    
                    String srCode = getIDForName(specialDesc, vecSpecialReviewCode);
                    String aprCode = getIDForName(approvalDesc, vecApprovalTypeCode);
                    int sCode = Integer.parseInt(srCode);
                    int appCode = Integer.parseInt(aprCode);
                    boolean validate = validateRowData(inInd,sCode, appCode, proNum, (String)apDate, arDate);
                    if(!validate){
                        valid = false;
                        tblSpecialReview.setRowSelectionInterval(inInd,inInd);
                        break;
                    }
                }
            }
            //Added for the Coeus Enhancement Case #1799 - start:  step 5
            boolean duplicate = checkDuplicateProtocol();
            if(!duplicate){
                valid = false;
                //Added for the case# COEUSDEV-312-First Protocol on Proposal not being saved on Award record.-start
                // While closing the Develepoment ProposalScreen, if there are duplicate protocol for human subjects then
                //it gives validation messaga "Duplicat Protocol" and again a save confirmation message is displayed
                //Click "Yes" in the confirmation message, the validation message is displayed again.
                //The loop continues infinitely on click of "Yes". It will come out of the loop on click of "No"
                //So the CoeusUIException is thrown by using log Method
                log(coeusMessageResources.parseMessageKey(
                        DUPLICATE_PROTOCOL));
                //CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DUPLICATE_PROTOCOL));                
                //Added for the case# COEUSDEV-312-First Protocol on Proposal not being saved on Award record.-End                
            }
            //Added for the Coeus Enhancement Case #1799
            if(!checkValidProtocol()) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("SpecialReviewForm_exceptionCode.1069"));
                setValidProtocols();
                valid = false;
            }
            
            if(!valid){
                return false;
            }
            
        }
        return true;
    }
    
    //Added for the Coeus Enhancement Case #1799
    private void setValidProtocols() {
        if(vecSpecialReviewData!=null && vecSpecialReviewData.size() > 0){
            for(int index = 0; index < vecSpecialReviewData.size(); index++){
                SpecialReviewFormBean specialReviewFormBean = ( SpecialReviewFormBean )vecSpecialReviewData.get(index);
                String protocolNumber = specialReviewFormBean.getProtocolSPRevNumber();
                if(protocolNumber != null && protocolNumber.startsWith("X")){
                    protocolNumber = protocolNumber.substring(1);
                    tblSpecialReview.setValueAt(protocolNumber, index, 3);
                    specialReviewFormBean.setProtocolSPRevNumber(protocolNumber);
                    specialReviewFormBean.setAcType(CoeusGuiConstants.UPADTE_RECORD);
                }
            }
        }
    }
    
    //Added for the Coeus Enhancement Case #1799
    private boolean checkValidProtocol() {
        //To tell the user to establish a link with the protocol by giving a valid protocol number
        if(vecSpecialReviewData!=null && vecSpecialReviewData.size() > 0){
            for(int index = 0; index < vecSpecialReviewData.size(); index++){
                SpecialReviewFormBean specialReviewFormBean = ( SpecialReviewFormBean )vecSpecialReviewData.get(index);
                String protocolNumber = specialReviewFormBean.getProtocolSPRevNumber();
                if(protocolNumber != null && protocolNumber.startsWith("X")){
                    saveRequired = true;
                    return false;
                }
            }
        }
        return true;
        //End Coeus Enhancement Case #1799 - start:  step 5
    }
    
    //Added for the Coeus Enhancement Case #1799 - start:  step 6
    private boolean checkDuplicateProtocol(){
        int specialReviewCode=0;
        String protocolNum;
        CoeusVector cvData = new CoeusVector();
        And specialRevCodeAndProtocolNumEquals;
        if(vecSpecialReviewData!=null && vecSpecialReviewData.size() > 0){
            for(int index = 0; index < vecSpecialReviewData.size(); index++){
                SpecialReviewFormBean specialReviewFormBean = ( SpecialReviewFormBean )vecSpecialReviewData.get(index);
                specialReviewCode = specialReviewFormBean.getSpecialReviewCode();
                protocolNum = specialReviewFormBean.getProtocolSPRevNumber();
                int size = -1;
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                //checking whether entered protocol is duplicate. If it is duplicate then it gives warning msg as duplicate protocol
                if(protocolNum != null){
                    if(specialRevTypeCodeParam != null && !EMPTY_STRING.equals(specialRevTypeCodeParam)
                    && specialReviewCode == Integer.parseInt(specialRevTypeCodeParam) && enableProtocolLink == 1) {
                        size = filter(protocolNum,specialReviewCode);
                    } else if(specialRevTypeCodeParamForIacuc != null && !EMPTY_STRING.equals(specialRevTypeCodeParamForIacuc)
                    && specialReviewCode == Integer.parseInt(specialRevTypeCodeParamForIacuc) && enableIacucProtocolLink == 1){
                        size = filter(protocolNum,specialReviewCode);
                    }
                    if (size > 1){
                        return false;
                    }
                }
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end                   
            }
        }
        return true;
    }
    
    public int filter(String protoNum, int reviewCode) {
        int specialReviewCode=0;
        String protocolNum;
        int count = 0;
        SpecialReviewFormBean bean = null;
        for(int index = 0; index < vecSpecialReviewData.size() ; index++){
            bean = (SpecialReviewFormBean)vecSpecialReviewData.elementAt(index);
            specialReviewCode = bean.getSpecialReviewCode();
            protocolNum = bean.getProtocolSPRevNumber();
            if(protoNum.equals(protocolNum) && reviewCode == specialReviewCode)
                count+=1;
        }
        return count;
    }//End Coeus Enhancement case:#1799 step:6
    
    /** This method is used to get the Form data.
     *
     * @return Vector of SpecialReviewFormBean's
     */
    public Vector getSpecialReviewData(){
        
        if(vecDeletedSpecialreviewCodes != null){
            int delSize = vecDeletedSpecialreviewCodes.size();
            SpecialReviewFormBean specialReviewBean = null;
            for(int index = 0; index < delSize; index++){
                specialReviewBean = (SpecialReviewFormBean)vecDeletedSpecialreviewCodes.get(index);
                if(specialReviewBean != null && vecSpecialReviewData !=null){
                    vecSpecialReviewData.insertElementAt(specialReviewBean,index);
                }
            }
        }
        return vecSpecialReviewData;
    }
    
    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
    /** This method is used to get the Form data.
     *
     * @return Vector of SpecialReviewData
     */
      public Vector getSpecialReviewDataForMail(){
        return vecSpecialReviewData;
    }
    ///Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
    
    // Testing
    private void printData(){
        if(vecSpecialReviewData != null){
            SpecialReviewFormBean pBean = null;
            for(int index = 0; index < vecSpecialReviewData.size(); index++){
                pBean = (SpecialReviewFormBean)vecSpecialReviewData.get(index);
                if(pBean != null){
                    //System.out.println("In Detail Form Values to Server");
                    //System.out.println("Bean "+index);
                    //System.out.println("In Actype "+pBean.getAcType());
                    //System.out.println("In getApprovalCode "+pBean.getApprovalCode());
                    //System.out.println("In getSpecialReviewCode "+pBean.getSpecialReviewCode());
                    //System.out.println("In getSpecialReviewNumber "+pBean.getSpecialReviewNumber());
                }
            }
        }
    }
    
    
    /** This method is used to find out whether modifications done to the data
     * have been saved or not.
     *
     * @return true if data is not saved after modifications, else false.
     */
    public boolean isSaveRequired() {
        handleEditableComponents();
        return saveRequired;
    }
    
    /** This method is used to set whether modifications are to be saved or not.
     *
     * @param saveRequired boolean true if data is to be saved after modifications,
     * else false.
     */
    public void setSaveRequired(boolean saveRequired){
        this.saveRequired = saveRequired;
    }
    
    /** Method to get the functionType
     *
     * @return a <CODE>Char</CODE> representation of functionType.
     */
    public char getFunctionType(){
        
        return this.functionType;
        
    }
    /** Method to set the functionType
     *
     * @param fType is functionType to be set like 'D', 'I', 'M'
     */
    public void setFunctionType(char fType){
        this.functionType = fType;
    }
    
    /**
     * This method is used to set the available Special Review Type Codes
     *
     * @param reviewCodes collection of available Special Review Type Codes
     */
    public void setSpecialReviewTypeCodes(Vector reviewCodes){
        this.vecSpecialReviewCode = reviewCodes;
        if(vecSpecialReviewCode != null){
            vecSpecialReviewCodeDescriptions = getDescriptionsForCombo(vecSpecialReviewCode);
            
        }
        if(vecSpecialReviewCodeDescriptions != null){
            // Commented by chandra for the fix #920 - start
            //            specialCombo.setModel(new CoeusComboBox(
            //                                        vecSpecialReviewCodeDescriptions,
            //                                        false).getModel());
            // Commented by chandra for the fix #920 - end
            
            specialCombo.addItemListener(new ItemListener(){
                public void itemStateChanged( ItemEvent item ){
                    //                    int row = tblSpecialReview.getSelectedRow();
                    int row = selRow;
                    int col = tblSpecialReview.getSelectedColumn();
                    SpecialReviewFormBean pbean = null;
                    String prevValue = "";
                    if(vecSpecialReviewData != null && row != -1){
                        pbean = (SpecialReviewFormBean)vecSpecialReviewData.elementAt(row);
                        if(pbean != null){
                            int code = pbean.getSpecialReviewCode();
                            prevValue = getDescriptionForId(code, vecSpecialReviewCode);
                            String curValue = ((JComboBox)item.getSource()).getSelectedItem().toString();
                            if(curValue != null) {//&& prevValue != null)
                                if(!curValue.equalsIgnoreCase(prevValue)){
                                    saveRequired = true;
                                    String stCode = getIDForName(curValue, vecSpecialReviewCode);
                                    pbean.setSpecialReviewCode(new Integer(stCode).intValue());
                                    pbean.setSpecialReviewDescription(curValue);
                                    String aType = pbean.getAcType();
                                    if(!aType.equalsIgnoreCase(INSERT_RECORD)){
                                        pbean.setAcType(UPDATE_RECORD);
                                    }
                                    
                                }
                            }
                        }
                    }
                }
            });
        }
    }
    
    /**
     * This method is used to set the available approval Types
     *
     * @param approvalTypes collection of available approval Types
     */
    public void setApprovalTypes(Vector approvalTypes){
        
        this.vecApprovalTypeCode = approvalTypes;
        if(vecApprovalTypeCode != null){
            vecApprovalCodeDescriptions = getDescriptionsForCombo(vecApprovalTypeCode);
        }
        if(vecApprovalCodeDescriptions != null){
            //            approvalCombo.setModel(new CoeusComboBox(
            //                                        vecApprovalCodeDescriptions,
            //                                        false).getModel());
            approvalCombo.addItemListener(new ItemListener(){
                
                public void itemStateChanged( ItemEvent itemEvent ){
                    
                    //                    int row = tblSpecialReview.getSelectedRow();
                    int row = selRow;
                    int col = tblSpecialReview.getSelectedColumn();
                    SpecialReviewFormBean pbean = null;
                    String prevValue = "";
                    if(vecSpecialReviewData != null && row != -1){
                        pbean = (SpecialReviewFormBean)vecSpecialReviewData.elementAt(row);
                        if(pbean != null){
                            int code = pbean.getApprovalCode();
                            prevValue = getDescriptionForId(code, vecApprovalTypeCode);
                            String curValue = ((JComboBox)itemEvent.getSource()).getSelectedItem().toString();
                            if(curValue != null) {
                                if(!curValue.equalsIgnoreCase(prevValue)){
                                    saveRequired = true;
                                    String stCode = getIDForName(curValue, vecApprovalTypeCode);
                                    pbean.setApprovalCode(new Integer(stCode).intValue());
                                    pbean.setApprovalDescription(curValue);
                                    String aType = pbean.getAcType();
                                    if(!(prevValue.equals("")) && !aType.equalsIgnoreCase(INSERT_RECORD)){
                                        pbean.setAcType(UPDATE_RECORD);
                                    }
                                    //((DefaultTableModel)tblSpecialReview.getModel()).setValueAt(curValue,row,col); // Handle
                                }
                            }
                            //((DefaultTableModel)tblSpecialReview.getModel()).setValueAt(prevValue,row,col); //// Handle
                        }
                    }
                }
            });
        }
    }
    
    // This method is used to set the actype as Insert mode to the beans which has the actype null.
    
    private void setAcTypesToServer(){
        
        if(vecSpecialReviewData != null){
            
            int size = vecSpecialReviewData.size();
            SpecialReviewFormBean specialReviewFormBean = null;
            for(int index = 0; index < size; index++){
                
                specialReviewFormBean = (SpecialReviewFormBean)vecSpecialReviewData.get(index);
                
                if(specialReviewFormBean != null){
                    
                    String acTye = specialReviewFormBean.getAcType();
                    if(acTye == null || acTye.equalsIgnoreCase("null")){
                        specialReviewFormBean.setAcType(INSERT_RECORD);
                    }
                }
            }
        }
    }
    
    private void setAcTypes(){
        
        if(vecSpecialReviewData != null){
            
            int size = vecSpecialReviewData.size();
            SpecialReviewFormBean specialReviewFormBean = null;
            for(int index = 0; index < size; index++){
                
                specialReviewFormBean = (SpecialReviewFormBean)vecSpecialReviewData.get(index);
                
                if(specialReviewFormBean != null){
                    
                    String acTye = specialReviewFormBean.getAcType();
                    if(acTye == null){
                        specialReviewFormBean.setAcType("null");
                    }
                }
            }
        }
    }
    
    /**
     * This method is used to form the String array. Each String is a combination of
     * Special Review Code, Approval Code, Protocol flag, Application Date Flag, Approval Date Flag
     *
     * @return String[] of combination of Special Review Code, Approval Code, Protocol flag, Application Date Flag, Approval Date Flag
     */
    
    private String[] formValidateString(){
        SRApprovalInfoBean srBean = null;
        String[] validateStringCollection = null;
        if(vecValidateRules != null){
            int size = vecValidateRules.size();
            validateStringCollection = new String[size];
            String protocolFlag = null;
            String approvalDateFlag = null;
            String applicationDateFlag = null;
            int approvalCode;
            int specialReviewCode;
            for(int index = 0; index < size ; index++){
                srBean = (SRApprovalInfoBean)vecValidateRules.elementAt(index);
                String validate = null;
                if(srBean != null){
                    
                    specialReviewCode = srBean.getSpecialReviewCode();
                    approvalCode = srBean.getApprovalTypeCode();
                    protocolFlag = srBean.getProtocolFlag();
                    applicationDateFlag = srBean.getApplicationDateFlag();
                    approvalDateFlag = srBean.getApprovalDateFlag();
                    
                    validate = ""+specialReviewCode + approvalCode + protocolFlag
                            + applicationDateFlag + approvalDateFlag;
                    validateStringCollection[index] = validate;
                }
            }
        }
        return validateStringCollection;
    }
    
    /**
     * This method is used to get the Combination of Protocol Flag,
     * Application Date Flag, and Approval date Flag for the input combination
     * of Special Review Code and Approval Code.
     *
     * @return String
     *
     */
    private String getValidateString(int specialCode , int approval){
        
        String[] validate = formValidateString();
        String paramString = "" + specialCode + approval;
        String resultString = null;
        if(validate != null){
            int size = validate.length;
            String stTemp = null;
            for(int index = 0; index < size; index++){
                stTemp = validate[index];
                if(stTemp.startsWith(paramString)){
                    resultString = stTemp.substring(paramString.length());
                }
            }
        }
        return resultString;
    }
    
    /**
     * This Method validates the Table Row Data.
     * In a Row, For the selected combination of Approval Code and
     * SPecial Review Code, this method performs the mandatory checking of the
     * protocol number entry, Application date entry, and Approval Date entry.
     *
     * @return boolean contains true if Row data is valid else contains false
     * @param code is the Special review code
     * @param aCode is the Approvzl code
     * @param protocolNum is the protocolNumber
     * @param applDate is the application date
     * @param apprDate is the approval date
     */
    private boolean validateRowData(int selRow,int code , int aCode, String protocolNum,
            String applDate, String apprDate) throws Exception{
        
        String stData =  getValidateString(code, aCode);
        String protocolNumberFlag = null;
        String appliDateFlag = null;
        String arDateFlag = null;
        
        if(stData != null){
            
            protocolNumberFlag = ""+stData.charAt(0);
            appliDateFlag = ""+stData.charAt(1);
            arDateFlag = ""+stData.charAt(2);
            
            if(protocolNumberFlag != null){
                if(protocolNumberFlag.trim().equalsIgnoreCase("Y")) {
                    if(protocolNum == null || protocolNum.trim().length() <=0){
                        tblSpecialReview.setRowSelectionInterval(selRow,selRow);
                        errorMessage(coeusMessageResources.parseMessageKey(
                                "You have to enter 'protocol number' with your current choice of special review and approval type"));
                        return false;
                    }
                }
            }
            if(appliDateFlag != null){
                if(appliDateFlag.trim().equalsIgnoreCase("Y")) {
                    if(applDate == null || applDate.trim().length() <=0){
                        tblSpecialReview.setRowSelectionInterval(selRow,selRow);
                        errorMessage(coeusMessageResources.parseMessageKey(
                                "You have to enter 'Application Date' with your current choice of special review and approval type"));
                        return false;
                    }
                }
            }
            if(arDateFlag != null){
                if(arDateFlag.trim().equalsIgnoreCase("Y")) {
                    if(apprDate == null || apprDate.trim().length() <=0){
                        tblSpecialReview.setRowSelectionInterval(selRow,selRow);
                        errorMessage(coeusMessageResources.parseMessageKey(
                                "You have to enter 'Approval Date' with your current choice of special review and approval type"));
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    /** This method is used to show the alert messages to the user.
     * @param mesg is a message to alert the user.
     * @throws Exception is the <CODE>Exception</CODE> to throw in the client side.
     */
    
    private void errorMessage(String mesg) throws Exception{
        throw new Exception(mesg);
    }
    
    /** This method is used to set the listeners to the components.
     */
    private void setListenersForButtons(){
        
        btnAdd.addActionListener(this);
        btnDelete.addActionListener(this);
        txtAreaComments.addFocusListener( this );
        specialCombo.addItemListener(this);
        //For the Coeus Enhancement case:#1799 step:7
        btnFind.addActionListener(this);
        //End Coeus Enhancement case:#1799 step:7
        tblSpecialReview.addMouseListener(this);
        btnStartProtocol.addActionListener(this);
        ListSelectionModel specialReviewSelectionModel = tblSpecialReview.getSelectionModel();
        specialReviewSelectionModel.addListSelectionListener( this );
        specialReviewSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
        
    }
    
    /** This method disables/ enables the components based on the function type.
     */
    public void formatFields(){
        
        boolean enabled = functionType != DISPLAY_MODE ? true : false;
        btnAdd.setEnabled(enabled);
        btnDelete.setEnabled(enabled);
        txtAreaComments.setEnabled(enabled);
        btnStartProtocol.setEnabled(false);
        if(this.moduleCode.equals("PROPOSAL")&& enableProtocolLink == 1)
                btnStartProtocol.setVisible(true);
            else
                btnStartProtocol.setVisible(false);
        //Added by Amit 11/18/2003
        if(functionType == CoeusGuiConstants.DISPLAY_MODE){
            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
            txtAreaComments.setDisabledTextColor(Color.BLACK);
            tblSpecialReview.setBackground(bgListColor);
            tblSpecialReview.setSelectionBackground(bgListColor );
            tblSpecialReview.setSelectionForeground(Color.black);
        } else{
            tblSpecialReview.setBackground(Color.white);
            tblSpecialReview.setSelectionBackground(Color.white);
            tblSpecialReview.setSelectionForeground(Color.black);
        }
        //end Amit
        
        //For the Coeus Enhancement case:#1799 step:8
        btnFind.setEnabled(false);
    }
    
    /** This method is used to set the form data.
     */
    public void setFormData(){
        
         Vector vcDataPopulate = new Vector();
        Vector vcData=null;
        String approvalDesc = null;
        vecDeletedSpecialreviewCodes.clear();
        if((vecSpecialReviewData!= null) &&
                (vecSpecialReviewData.size() > 0)){
            
            for(int inCtrdata=0;
            inCtrdata < vecSpecialReviewData.size();
            inCtrdata++){
                
                specialReviewFormBean = (SpecialReviewFormBean)
                vecSpecialReviewData.get(inCtrdata);
                
                
                String specialReviewDesc = specialReviewFormBean.getSpecialReviewDescription();
                String specialReviewCode = ""+specialReviewFormBean.getSpecialReviewCode();
                
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                //If special review is linked to protocol then it should sync the status of that special review
                approvalDesc = specialReviewFormBean.getApprovalDescription();
                                
                if(specialReviewCode.equals("1")){
                    //Added for Coeus Enhancement Case #1799 - start:  step 9
                    if(specialReviewCode.equals(specialRevTypeCodeParam)) {
                        if(!humanSubRows.contains(new Integer(inCtrdata)))
                            humanSubRows.add(new Integer(inCtrdata));
                    }
                    //Coeus Enhancement Case #1799 - end:  step 9
                    
                    // Case# 3110: Special review in prop dev linked to protocols - Start
                    if(specialReviewFormBean.getApprovalCode() == 5){
                        if(specialReviewFormBean.getProtocolSPRevNumber()!= null){
                            ProtocolInfoBean protocolInfoBean = null;
                            try {
                                protocolInfoBean = getProtocolDetails(specialReviewFormBean.getProtocolSPRevNumber().trim());
                            } catch (CoeusException ex) {
                                ex.printStackTrace();
                            }
                            if(protocolInfoBean!= null){
                                if(protocolInfoBean.getProtocolStatusDesc() != null && !protocolInfoBean.getProtocolStatusDesc().equals(CoeusGuiConstants.EMPTY_STRING)){
                                    approvalDesc = protocolInfoBean.getProtocolStatusDesc();
                                    specialReviewFormBean.setApprovalDescription(protocolInfoBean.getProtocolStatusDesc());
                                }
                                if(protocolInfoBean.getApplicationDate() != null && !protocolInfoBean.getApplicationDate().equals(CoeusGuiConstants.EMPTY_STRING)){
                                    specialReviewFormBean.setApplicationDate(protocolInfoBean.getApplicationDate());
                                }
                                if(protocolInfoBean.getApprovalDate() != null && !protocolInfoBean.getApprovalDate().equals(CoeusGuiConstants.EMPTY_STRING)){
                                    specialReviewFormBean.setApprovalDate(protocolInfoBean.getApprovalDate());
                                }
                            }
                        }
                    }
                }else if(specialReviewCode.equals("2")){
                    if(specialReviewCode.equals(specialRevTypeCodeParamForIacuc)) {
                        if(!animalSubRows.contains(new Integer(inCtrdata)))
                            animalSubRows.add(new Integer(inCtrdata));
                    }
                    approvalDesc = specialReviewFormBean.getApprovalDescription();
                    
                    //If special review is linked to IACUC then it should sync the status of that special review
                    if(specialReviewFormBean.getApprovalCode() == 7){
                        if(specialReviewFormBean.getProtocolSPRevNumber()!= null){
                            edu.mit.coeus.iacuc.bean.ProtocolInfoBean protocolInfoBean = null;
                            try {
                                protocolInfoBean = getIacucProtocolDetails(specialReviewFormBean.getProtocolSPRevNumber().trim());
                            } catch (CoeusException ex) {
                                ex.printStackTrace();
                            }
                            if(protocolInfoBean!= null){
                                if(protocolInfoBean.getProtocolStatusDesc() != null && !protocolInfoBean.getProtocolStatusDesc().equals(CoeusGuiConstants.EMPTY_STRING)){
                                    approvalDesc = protocolInfoBean.getProtocolStatusDesc();
                                    specialReviewFormBean.setApprovalDescription(protocolInfoBean.getProtocolStatusDesc());
                                }
                                if(protocolInfoBean.getApplicationDate() != null && !protocolInfoBean.getApplicationDate().equals(CoeusGuiConstants.EMPTY_STRING)){
                                    specialReviewFormBean.setApplicationDate(protocolInfoBean.getApplicationDate());
                                }
                                if(protocolInfoBean.getApprovalDate() != null && !protocolInfoBean.getApprovalDate().equals(CoeusGuiConstants.EMPTY_STRING)){
                                    specialReviewFormBean.setApprovalDate(protocolInfoBean.getApprovalDate());
                                }
                            }
                        }
                    }
                }
                
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
                
                // Case# 3110: Special review in prop dev linked to protocols - End
                
                int specialReviewNumber = specialReviewFormBean.getSpecialReviewNumber();
                
                String protocolSpecialReviewNum = specialReviewFormBean.getProtocolSPRevNumber();
                
                Date applicationDate = specialReviewFormBean.getApplicationDate();
                Date approvalDate = specialReviewFormBean.getApprovalDate();
                String comments = specialReviewFormBean.getComments();
                vcData= new Vector();
                
                vcData.addElement("");
                // vcData.addElement(new ComboBoxBean(specialReviewCode, specialReviewDesc));
                
                
                vcData.addElement(specialReviewDesc);
                vcData.addElement(approvalDesc);
                vcData.addElement(protocolSpecialReviewNum);
                if(applicationDate != null){
                    vcData.addElement(dtUtils.formatDate(applicationDate.toString(),"dd-MMM-yyyy"));
                }else{
                    vcData.addElement("");
                }
                if(approvalDate != null){
                    vcData.addElement(dtUtils.formatDate(approvalDate.toString(),"dd-MMM-yyyy"));
                }else{
                    vcData.addElement("");
                }
                vcData.addElement(protocolSpecialReviewNum);
                vcData.addElement("");
                vcData.addElement("");
                vcDataPopulate.addElement(vcData);
                
            }
            
            ((DefaultTableModel)tblSpecialReview.getModel()).
                    setDataVector(vcDataPopulate,getColumnNames());
            ((DefaultTableModel)tblSpecialReview.getModel()).
                    fireTableDataChanged();
            
            // If the Table contains more than one row.. move the cursor to the first row..
            if( tblSpecialReview.getRowCount() > 0 ){
                tblSpecialReview.setRowSelectionInterval(0,0);
            }else{
                btnDelete.setEnabled(false);
                btnFind.setEnabled(false);
            }
        }
        //Case 2442 start
        ((DefaultTableModel)tblSpecialReview.getModel()).
                setDataVector(vcDataPopulate,getColumnNames());
        ((DefaultTableModel)tblSpecialReview.getModel()).
                fireTableDataChanged();
        // while saving the special Review Comments by CTRL+s , comments are not displayed in screen but it is saved
        //this because the Text is set to Empty, so it is commented now.
        //Added for the case#COEUSDEV-312- First Protocol on Proposal not being saved on Award record-Start
        // txtAreaComments.setText(EMPTY_STRING);
        //Added for the case#COEUSDEV-312- First Protocol on Proposal not being saved on Award record.-End        
        //Case 2442 End
    }
    
    /** This method is used to set the Cell Editors to the JTable..
     */
    public void setTableEditors(){
        
        //        ColumnValueEditor columnValueEditor = new ColumnValueEditor(20);
        columnValueEditor = new ColumnValueEditor(20);
        ColumnValueRenderer columnValueRenderer = new ColumnValueRenderer();
        
        tblSpecialReview.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumn column = tblSpecialReview.getColumnModel().getColumn(0);
        column.setMinWidth(30);
        column.setMaxWidth(30);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        
        txtAreaComments.setLineWrap(true);
        
        scrPnCommentsContainer.setViewportView(txtAreaComments);
        
        column.setResizable(false);
        column.setCellRenderer(new IconRenderer());
        column.setPreferredWidth(30);
        
        JTableHeader header = tblSpecialReview.getTableHeader();
        header.setReorderingAllowed(false);
        //header.setResizingAllowed(false);
        
        tblSpecialReview.setRowHeight(24);
        
        tblSpecialReview.setOpaque(false);
        tblSpecialReview.setShowVerticalLines(false);
        tblSpecialReview.setShowHorizontalLines(false);
        //tblSpecialReview.setSelectionBackground(Color.white);
        //tblSpecialReview.setSelectionForeground(Color.black);
        
        
        tblSpecialReview.setSelectionMode(
                DefaultListSelectionModel.SINGLE_SELECTION);
        
        column = tblSpecialReview.getColumnModel().getColumn(1);
        
        column.setMinWidth(220);
        //column.setMaxWidth(220);
        //column.setPreferredWidth(220);
        //column.setResizable(false);
        //column.setCellRenderer(new ColumnValueRenderer());
        
        column.setCellEditor(new ComboEditor());
        // commnetd by chandra for the bug fix #920 - start
        
        /*column.setCellEditor(new DefaultCellEditor(specialCombo){
            public Object getCellEditorValue(){
                return ((JComboBox)getComponent()).getSelectedItem().toString();
            }
        });*/
        
        // commnetd by chandra for the bug fix #920 - end
        
        column = tblSpecialReview.getColumnModel().getColumn(2);
        column.setMinWidth(100);
        
        //column.setMaxWidth(100);
        //column.setPreferredWidth(100);
        //column.setResizable(false);
        //column.setCellRenderer(columnValueRenderer);
        
        column.setCellEditor(new ComboEditor());
        // commnetd by chandra for the bug fix #920 - start
        
        //        column.setCellEditor(new DefaultCellEditor(approvalCombo ){
        //            public Object getCellEditorValue(){
        //                return ((JComboBox)getComponent()).getSelectedItem().
        //                        toString();
        //
        //            }
        //        });
        
        // commnetd by chandra for the bug fix #920 - End
        
        column = tblSpecialReview.getColumnModel().getColumn(3);
        column.setMinWidth(90);
        
        //column.setMaxWidth(90);
        //column.setPreferredWidth(90);
        //column.setResizable(false);
        //column.setCellRenderer(columnValueRenderer);
        column.setCellEditor(columnValueEditor);
        
        column = tblSpecialReview.getColumnModel().getColumn(4);
        column.setMinWidth(90);
        
        //column.setMaxWidth(90);
        //column.setPreferredWidth(90);
        //column.setResizable(false);
        column.setCellEditor(dateEditComponent);
        
        column = tblSpecialReview.getColumnModel().getColumn(5);
        column.setMinWidth(90);
        
        //column.setMaxWidth(90);
        //column.setPreferredWidth(90);
        //column.setResizable(false);
        column.setCellEditor(dateEditComponent);
        
        column = tblSpecialReview.getColumnModel().getColumn(6);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        
        column = tblSpecialReview.getColumnModel().getColumn(7);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        
        column = tblSpecialReview.getColumnModel().getColumn(8);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        
    }
    
    
    public void setAwardDetailDescription(JComponent pnlData){
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        //        gridBagConstraints.gridx = 0;
        //        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        //gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        pnlProposalDescriptionContainer.add(pnlData,gridBagConstraints);
    }
    
    /** This method is used to get vector of Special Review Descriptions
     * @return Vector of Special Review Descriptions
     */
    
    private Vector getDescriptionsForCombo(Vector vecDescValues) {
        
        ComboBoxBean comboEntry = null;
        Vector vecSpecialdesc = new Vector();
        
        Vector locSpecialType = vecDescValues;
        
        if( locSpecialType == null ){
            return vecDescValues;
        }else{
            int specialCodesSize = locSpecialType.size();
            for( int indx = 0; indx < specialCodesSize; indx++ ){
                comboEntry = ( ComboBoxBean ) locSpecialType.get( indx );
                if(comboEntry != null){
                    vecSpecialdesc.addElement( comboEntry.getDescription() );
                }
            }
            //COEUSQA-2320_Show in Lite for Special Review in Code table_Start
            vecSpecialdesc.insertElementAt("",0);
            //COEUSQA-2320_Show in Lite for Special Review in Code table_End
            return vecSpecialdesc;
        }
    }
    
    /* This method is used to get available type Code value for the
     * available type description selected in JComboBox
     * @return String description
     */
    private String getIDForName( String selType, Vector vecTypeCodes ){
        
        String stTypeID = "1";
        ComboBoxBean comboEntry = null;
        Vector locSpecialType = vecTypeCodes;
        if( locSpecialType == null || selType == null ){
            return stTypeID;
        }
        int specialCodesSize = locSpecialType.size();
        for( int indx = 0; indx < specialCodesSize; indx++ ){
            
            comboEntry = ( ComboBoxBean ) locSpecialType.get( indx );
            if( ((String)comboEntry.getDescription()).equalsIgnoreCase(
                    selType)  ){
                stTypeID = comboEntry.getCode();
                break;
            }
        }
        return stTypeID;
    }
    
    /* This method is used to get the description for the corresponding id in corresponding vector
     * @return String description
     */
    
    private String getDescriptionForId(int id, Vector vecTypeCodes){
        
        String stDesc = null;
        String stId = new String(""+id);
        ComboBoxBean comboEntry = null;
        if(vecTypeCodes != null){
            int specialCodesSize = vecTypeCodes.size();
            for( int indx = 0; indx < specialCodesSize; indx++ ){
                
                comboEntry = ( ComboBoxBean ) vecTypeCodes.get( indx );
                if( ((String)comboEntry.getCode()).equalsIgnoreCase(stId)  ){
                    stDesc = comboEntry.getDescription();
                    break;
                }
            }
        }
        return stDesc;
    }
    
    /**
     * This method is used to get all the column names of the JTable.
     * @return Vector of Column Names
     */
    private Vector getColumnNames(){
        
        Enumeration enumColNames = tblSpecialReview.getColumnModel().getColumns();
        Vector vecColNames = new Vector();
        while(enumColNames.hasMoreElements()){
            String strName = (String)((TableColumn)
            enumColNames.nextElement()).getHeaderValue();
            vecColNames.addElement(strName);
        }
        return vecColNames;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlTableContainer = new javax.swing.JPanel();
        scrPnPane1 = new javax.swing.JScrollPane();
        tblSpecialReview = new javax.swing.JTable(){
            //    int colIndex;
            //    int rowIndex;
            public void changeSelection(int row, int column, boolean toggle, boolean extend){
                super.changeSelection(row, column, toggle, extend);
                //                this.colIndex = column;
                //                this.rowIndex = row;
                javax.swing.SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        //                        if(colIndex==0){
                            //                            rowIndex= rowIndex-1;
                            //                            //colIndex = 5;
                            //                        }else if(colIndex >5){
                            //                            rowIndex = rowIndex+1;
                            //                        }
                        //                        if(colIndex==0){
                            //                            getTableInstance().setRowSelectionInterval(rowIndex,rowIndex);
                            //                            getTableInstance().setColumnSelectionInterval(1,1);
                            //                            getTableInstance().editCellAt(rowIndex, 1);
                            //                            getTableInstance().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            //                        }
                        //                        if(colIndex > 5) {
                            //                            getTableInstance().setRowSelectionInterval(rowIndex,rowIndex);
                            //                            getTableInstance().setColumnSelectionInterval(1,1);
                            //                            getTableInstance().editCellAt(rowIndex, 1);
                            //                            getTableInstance().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            //                        }
                        getTableInstance().dispatchEvent(new java.awt.event.KeyEvent(
                            getTableInstance(),java.awt.event.KeyEvent.KEY_PRESSED,0,0,java.awt.event.KeyEvent.VK_F2,
                            java.awt.event.KeyEvent.CHAR_UNDEFINED) );
                }
            });
        }
    };
    pnlComments = new javax.swing.JPanel();
    scrPnCommentsContainer = new javax.swing.JScrollPane();
    txtAreaComments = new javax.swing.JTextArea();
    lblSpecialReviewComments = new javax.swing.JLabel();
    pnlButtonsContainer = new javax.swing.JPanel();
    pnlAddDeleteButtons = new javax.swing.JPanel();
    btnAdd = new javax.swing.JButton();
    btnDelete = new javax.swing.JButton();
    btnFind = new javax.swing.JButton();
    btnStartProtocol = new javax.swing.JButton();
    pnlOkCancelButtons = new javax.swing.JPanel();
    pnlProposalDescriptionContainer = new javax.swing.JPanel();
    pnlProposalDescription = new javax.swing.JPanel();
    lblProposalNo = new javax.swing.JLabel();
    lblProposalValue = new javax.swing.JLabel();
    lblSponsor = new javax.swing.JLabel();
    lblSponsorValue = new javax.swing.JLabel();
    sptrProposalDescription = new javax.swing.JSeparator();

    setLayout(new java.awt.GridBagLayout());

    pnlTableContainer.setMaximumSize(new java.awt.Dimension(660, 260));
    pnlTableContainer.setMinimumSize(new java.awt.Dimension(660, 260));
    pnlTableContainer.setPreferredSize(new java.awt.Dimension(660, 260));
    pnlTableContainer.setLayout(new java.awt.BorderLayout());

    scrPnPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Special Reviews", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
    scrPnPane1.setMaximumSize(new java.awt.Dimension(640, 250));
    scrPnPane1.setMinimumSize(new java.awt.Dimension(640, 250));
    scrPnPane1.setPreferredSize(new java.awt.Dimension(640, 250));

    tblSpecialReview.setFont(CoeusFontFactory.getNormalFont());
    tblSpecialReview.setModel(new CustomTableModel());
    tblSpecialReview.setShowHorizontalLines(false);
    tblSpecialReview.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            tblSpecialReviewMouseClicked(evt);
        }
    });
    scrPnPane1.setViewportView(tblSpecialReview);

    pnlTableContainer.add(scrPnPane1, java.awt.BorderLayout.CENTER);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 10;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
    gridBagConstraints.insets = new java.awt.Insets(4, 3, 2, 3);
    add(pnlTableContainer, gridBagConstraints);

    pnlComments.setMaximumSize(new java.awt.Dimension(660, 130));
    pnlComments.setMinimumSize(new java.awt.Dimension(660, 130));
    pnlComments.setPreferredSize(new java.awt.Dimension(660, 130));
    pnlComments.setLayout(new java.awt.GridBagLayout());

    scrPnCommentsContainer.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    scrPnCommentsContainer.setMaximumSize(new java.awt.Dimension(660, 120));
    scrPnCommentsContainer.setMinimumSize(new java.awt.Dimension(660, 100));
    scrPnCommentsContainer.setPreferredSize(new java.awt.Dimension(660, 115));

    txtAreaComments.setDocument(new LimitedPlainDocument( 2000 ));
    txtAreaComments.setFont(CoeusFontFactory.getNormalFont());
    txtAreaComments.setLineWrap(true);
    txtAreaComments.setWrapStyleWord(true);
    scrPnCommentsContainer.setViewportView(txtAreaComments);

    pnlComments.add(scrPnCommentsContainer, new java.awt.GridBagConstraints());

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.gridwidth = 10;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(0, 3, 10, 3);
    add(pnlComments, gridBagConstraints);

    lblSpecialReviewComments.setFont(CoeusFontFactory.getLabelFont());
    lblSpecialReviewComments.setText("  Special Review Comments :");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
    add(lblSpecialReviewComments, gridBagConstraints);

    // JM 12-3-2013 panel for page usage directions
    CoeusHelpGidget gidgetUsage = new CoeusHelpGidget("specialReview_helpCode.1000");
    JScrollPane scrPnUsage = new JScrollPane(gidgetUsage.createHelpScrollArea());
    scrPnUsage.setMaximumSize(new java.awt.Dimension(650, 170));
    scrPnUsage.setMinimumSize(new java.awt.Dimension(650, 150));
    scrPnUsage.setPreferredSize(new java.awt.Dimension(650, 185));

    JPanel pnlUsage = gidgetUsage.createHelpPanel();
    pnlUsage.add(scrPnUsage, java.awt.BorderLayout.CENTER);
     
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.gridwidth = 15;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 3, 10, 3);
    add(pnlUsage, gridBagConstraints);
    // JM END

    pnlButtonsContainer.setLayout(new java.awt.GridBagLayout());

    pnlAddDeleteButtons.setLayout(new java.awt.GridBagLayout());

    btnAdd.setFont(CoeusFontFactory.getLabelFont());
    btnAdd.setMnemonic('A');
    btnAdd.setText("Add");
    btnAdd.setMaximumSize(new java.awt.Dimension(106, 26));
    btnAdd.setMinimumSize(new java.awt.Dimension(106, 26));
    btnAdd.setPreferredSize(new java.awt.Dimension(85, 26));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
    pnlAddDeleteButtons.add(btnAdd, gridBagConstraints);

    btnDelete.setFont(CoeusFontFactory.getLabelFont());
    btnDelete.setMnemonic('D');
    btnDelete.setText("Delete");
    btnDelete.setMaximumSize(new java.awt.Dimension(106, 26));
    btnDelete.setMinimumSize(new java.awt.Dimension(106, 26));
    btnDelete.setPreferredSize(new java.awt.Dimension(85, 26));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
    pnlAddDeleteButtons.add(btnDelete, gridBagConstraints);

    btnFind.setFont(CoeusFontFactory.getLabelFont());
    btnFind.setMnemonic('F');
    btnFind.setText("Find");
    btnFind.setMaximumSize(new java.awt.Dimension(106, 26));
    btnFind.setMinimumSize(new java.awt.Dimension(106, 26));
    btnFind.setPreferredSize(new java.awt.Dimension(85, 26));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
    pnlAddDeleteButtons.add(btnFind, gridBagConstraints);

    btnStartProtocol.setFont(CoeusFontFactory.getLabelFont());
    btnStartProtocol.setText("Start Protocol");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.ipadx = 1;
    gridBagConstraints.ipady = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    pnlAddDeleteButtons.add(btnStartProtocol, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
    gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
    gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
    pnlButtonsContainer.add(pnlAddDeleteButtons, gridBagConstraints);

    pnlOkCancelButtons.setLayout(new java.awt.GridBagLayout());
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    pnlButtonsContainer.add(pnlOkCancelButtons, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 10;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
    gridBagConstraints.gridheight = 3;
    gridBagConstraints.ipady = 27;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(22, 2, 2, 2);
    add(pnlButtonsContainer, gridBagConstraints);

    pnlProposalDescriptionContainer.setLayout(new java.awt.GridBagLayout());

    pnlProposalDescription.setLayout(new java.awt.GridBagLayout());

    lblProposalNo.setFont(CoeusFontFactory.getLabelFont());
    lblProposalNo.setText("Proposal Number:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
    pnlProposalDescription.add(lblProposalNo, gridBagConstraints);

    lblProposalValue.setFont(CoeusFontFactory.getNormalFont());
    lblProposalValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    lblProposalValue.setText("xxxxxxxx");
    lblProposalValue.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
    pnlProposalDescription.add(lblProposalValue, gridBagConstraints);

    lblSponsor.setFont(CoeusFontFactory.getLabelFont());
    lblSponsor.setText("Sponsor:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
    pnlProposalDescription.add(lblSponsor, gridBagConstraints);

    lblSponsorValue.setFont(CoeusFontFactory.getNormalFont());
    lblSponsorValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    lblSponsorValue.setMaximumSize(new java.awt.Dimension(300, 17));
    lblSponsorValue.setMinimumSize(new java.awt.Dimension(300, 17));
    lblSponsorValue.setPreferredSize(new java.awt.Dimension(300, 17));
    lblSponsorValue.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
    pnlProposalDescription.add(lblSponsorValue, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    pnlProposalDescriptionContainer.add(pnlProposalDescription, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 11;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
    add(pnlProposalDescriptionContainer, gridBagConstraints);

    sptrProposalDescription.setBackground(java.awt.Color.black);
    sptrProposalDescription.setForeground(java.awt.Color.black);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 11;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    add(sptrProposalDescription, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    
    private void tblSpecialReviewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSpecialReviewMouseClicked
        // Add your handling code here:
        javax.swing.SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                getTableInstance().dispatchEvent(new java.awt.event.KeyEvent(
                        getTableInstance(),java.awt.event.KeyEvent.KEY_PRESSED,0,0,java.awt.event.KeyEvent.VK_F2,
                        java.awt.event.KeyEvent.CHAR_UNDEFINED) );
            }
        });
    }//GEN-LAST:event_tblSpecialReviewMouseClicked
    
    /** This the action Performed method
     * @param actionEvent Action Event
     */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object actionSource = actionEvent.getSource();
        if(actionSource.equals(btnAdd)){
            //Added for Coeus Enhancement Case #1799 - start:  step 10
            isHumanSub=false;
            //Coeus Enhancement Case #1799 - end:  step 10
            int rowCount = tblSpecialReview.getRowCount();
            tblSpecialReview.requestFocusInWindow();
            try{
                
                if(rowCount > 0 && tblSpecialReview.getCellEditor() != null){
                    //tblSpecialReview.getCellEditor().cancelCellEditing();
                    //modified for the Bug Fix:1732 start
                    tblSpecialReview.getCellEditor().stopCellEditing();
                    //Bug Fix:1732 End
                }
                Vector newRowData = new Vector();
                TableColumn clmTable = tblSpecialReview.getColumnModel().getColumn(1);
                
                //            clmTable.setCellEditor(new DefaultCellEditor(specialCombo ){
                //                public Object getCellEditorValue(){
                //                   return ((JComboBox)getComponent()).getSelectedItem().toString();
                //                }
                //            });
                
                clmTable = tblSpecialReview.getColumnModel().getColumn(2);
                //clmTable.setCellRenderer(new ColumnValueRenderer());
                //            clmTable.setCellEditor(new DefaultCellEditor(approvalCombo ){
                //                public Object getCellEditorValue(){
                //                    return ((JComboBox)getComponent()).getSelectedItem().toString();
                //                }
                //            });
                
                //newRowData.addElement("");                
                String specialDesc = (String)getDescriptionsForCombo(vecSpecialReviewCode).elementAt(0);
                String stSpecialCode = getIDForName(specialDesc, vecSpecialReviewCode);
                newRowData.addElement(specialDesc);
                newRowData.addElement("");
                String approvalDesc = (String)getDescriptionsForCombo(vecApprovalTypeCode).elementAt(0);
                String stApprovalCode = getIDForName(approvalDesc, vecApprovalTypeCode);
                //  newRowData.addElement(approvalDesc);
                newRowData.addElement("");
                
                newRowData.addElement("");
                newRowData.addElement("");
                newRowData.addElement("");
                newRowData.addElement("");
                newRowData.addElement("");
                newRowData.addElement("");
                
                int spCode = new Integer(stSpecialCode).intValue();
                int apCode = new Integer(stApprovalCode).intValue();
                
                // Create a new Bean and Add to Special Review Vector
                
                SpecialReviewFormBean pBean = new SpecialReviewFormBean();
                
                pBean.setAcType(INSERT_RECORD);
                pBean.setApprovalCode(apCode);
                pBean.setSpecialReviewCode(spCode);
                pBean.setSpecialReviewDescription(specialDesc);
                pBean.setApprovalDescription(approvalDesc);
                
                if(vecSpecialReviewData != null){
                    vecSpecialReviewData.addElement(pBean);
                }else{
                    vecSpecialReviewData = new Vector();
                    vecSpecialReviewData.addElement(pBean);
                }
                
                ((DefaultTableModel)tblSpecialReview.getModel()).addRow( newRowData );
                ((DefaultTableModel)tblSpecialReview.getModel()).fireTableDataChanged();
                
                if(tblSpecialReview.getRowCount()==1){
                    btnDelete.setEnabled(true);
                    txtAreaComments.setText("");
                    tblSpecialReview.setRowSelectionInterval( 0,0);
                    tblSpecialReview.setColumnSelectionInterval(1,1);
                    tblSpecialReview.editCellAt(0,1);
                }else if(tblSpecialReview.getRowCount()==0){
                    btnDelete.setEnabled(false);
                    btnFind.setEnabled(false);
                }
                
                int lastRow = tblSpecialReview.getRowCount() - 1;
                // Bug fix #1679 . check for the = operator also- Start
                if(lastRow >= 0){
                    // Bug Fix #1679 -end
                    btnDelete.setEnabled(true);
                    txtAreaComments.setEnabled(true);
                    txtAreaComments.setText("");
                    tblSpecialReview.setRowSelectionInterval( lastRow, lastRow );
                    tblSpecialReview.setColumnSelectionInterval(1,1);
                    
                    tblSpecialReview.editCellAt(lastRow,1);
                    tblSpecialReview.getEditorComponent().requestFocusInWindow();
                    
                    tblSpecialReview.scrollRectToVisible(tblSpecialReview.getCellRect(
                            lastRow ,ZERO_COUNT, true));
                }
                saveRequired=true;
                lastSelectedRow = lastRow;
            }catch(Exception e){
                e.printStackTrace();
            }
            
            //        //Added by Amit 11/20/2003 for IRB_DEF_Gen_6
            //        tblSpecialReview.setRowSelectionInterval(0,0);
            //        tblSpecialReview.setColumnSelectionInterval(1,1);
            //        tblSpecialReview.requestFocusInWindow();
            //        //tblSpecialReview.editCellAt(rowCount,1);
            //        //tblSpecialReview.getEditorComponent().requestFocusInWindow();
            //        //End Amit
            
        }else if(actionSource.equals(btnDelete)){
            // Added by chandra . After deleting all rows and try to insert
            // throwing the exception.Hence the code
            if(tblSpecialReview.getCellEditor() != null){
                //tblSpecialReview.getCellEditor().cancelCellEditing();
                tblSpecialReview.getCellEditor().stopCellEditing();
            }// End chandra 12 - Aug-2004.
            
            int totalRows = tblSpecialReview.getRowCount();
            if (totalRows > 0) {
                int selectedRow = tblSpecialReview.getSelectedRow();
                if (selectedRow != -1) {
                    int selectedOption = CoeusOptionPane.
                            showQuestionDialog(
                            coeusMessageResources.parseMessageKey(
                            "protocol_SpecialReviewForm_delConfirmCode.1050"),
                            CoeusOptionPane.OPTION_YES_NO,
                            CoeusOptionPane.DEFAULT_YES);
                    if (0 == selectedOption) {
                        SpecialReviewFormBean sReviewBean = null;
                        
                        if(vecSpecialReviewData != null){
                            sReviewBean =
                                    (SpecialReviewFormBean) vecSpecialReviewData.get( selectedRow );
                        }
                        
                        if( (sReviewBean.getAcType() != null ) &&
                                ( ! sReviewBean.getAcType().equalsIgnoreCase(INSERT_RECORD) )) {
                            
                            vecDeletedSpecialreviewCodes.addElement( sReviewBean );
                        }
                        
                        if( sReviewBean != null ){
                            
                            sReviewBean.setAcType( DELETE_RECORD );
                            saveRequired = true;
                        }
                        
                        vecSpecialReviewData.removeElementAt( selectedRow );
                        
                        
                        ((DefaultTableModel)
                        tblSpecialReview.getModel()).removeRow(selectedRow);
                        
                        ((DefaultTableModel)
                        tblSpecialReview.getModel()).fireTableDataChanged();
                        saveRequired = true;
                        int newRowCount = tblSpecialReview.getRowCount();
                        if(newRowCount == 0){
                            btnDelete.setEnabled(false);
                            txtAreaComments.setEnabled(false);
                            txtAreaComments.setText("");
                            txtAreaComments.setCaretPosition(0);
                            btnFind.setEnabled(false);
                        }else if (newRowCount > selectedRow) {
                            (tblSpecialReview.getSelectionModel())
                            .setSelectionInterval(selectedRow,
                                    selectedRow);
                            SpecialReviewFormBean firstBean =
                                    (SpecialReviewFormBean)vecSpecialReviewData.get(selectedRow);
                            if(firstBean != null){
                                txtAreaComments.setText( firstBean.getComments() );
                                txtAreaComments.setCaretPosition(0);
                            }
                            tblSpecialReview.requestFocusInWindow(); //included by raghuSV to set focus on table upon delete
                        } else {
                            tblSpecialReview.setRowSelectionInterval( newRowCount - 1,
                                    newRowCount -1 );
                            tblSpecialReview.scrollRectToVisible( tblSpecialReview.getCellRect(
                                    newRowCount - 1 ,
                                    ZERO_COUNT, true));
                            SpecialReviewFormBean firstBean =
                                    (SpecialReviewFormBean)vecSpecialReviewData.get( newRowCount -1 );
                            
                            if(firstBean != null){
                                txtAreaComments.setText( firstBean.getComments() );
                                txtAreaComments.setCaretPosition(0);
                                
                            }
                        }
                    }
                    
                }else{
                    CoeusOptionPane.
                            showErrorDialog(
                            coeusMessageResources.parseMessageKey(
                            "protoFndSrcFrm_exceptionCode.1057"));
                }
            }
            //Added for the Coeus Enhancement case:#1799 start step:11
        }else if(actionSource.equals(btnFind)){
            try {
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                //showProtocolSearch();
                String spRvDesc = null;
                int selectedRow = tblSpecialReview.getSelectedRow();
                int rowCount =  tblSpecialReview.getRowCount();                
                if( selectedRow!= -1 && selectedRow >= 0 && vecSpecialReviewData != null && selectedRow <= rowCount ){
                    SpecialReviewFormBean curBean = (SpecialReviewFormBean)vecSpecialReviewData.get( selectedRow );
                    if( txtAreaComments.hasFocus() ){
                        curBean = (SpecialReviewFormBean)vecSpecialReviewData.get(  lastSelectedRow );
                        curBean.setComments( txtAreaComments.getText() );
                        return;
                        
                    }else{
                        lastSelectedRow = selectedRow;
                    }
                    int spRvCode = curBean.getSpecialReviewCode(); 
                    //If parameter SPL_REV_TYPE_CODE_HUMAN is enabled and form value for special review code is same as parameter value
                    //then it should allow for protocol search.
                    if(enableProtocolLink == 1 && spRvCode ==Integer.parseInt(specialRevTypeCodeParam)){
                        showProtocolSearch();
                        //If parameter IACUC_SPL_REV_TYPE_CODE is enabled and form value for special review code is same as parameter value
                        //then it should allow for Iacuc protocol search.
                    }else if(enableIacucProtocolLink == 1 && spRvCode == Integer.parseInt(specialRevTypeCodeParamForIacuc)){
                        showIacucProtocolSearch();
                    }
                }
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
            }catch(Exception ex) {
                
            }
        }//End Coeus Enhancement case:#1799 step:11
        else if(actionSource.equals(btnStartProtocol)){
            callSaveProtocol();
        }
    }

    private  void callSaveProtocol(){
      //  ProposalDetailForm proposalDtetailsob = new ProposalDetailForm();
    //    ProposalSpecialReviewForm objnew = new ProposalSpecialReviewForm();
      //  objnew.setNewProtocolData();

       // ProposalOrganizationAdminForm jj = new  ProposalOrganizationAdminForm();
    //    ProposalInvestigatorForm proposalInvestigator = new ProposalInvestigatorForm();
        //private ProposalDetailForm proposalDtetail = null;
       // ProposalDetailAdminForm proposalDetails = new ProposalDetailAdminForm();
     //   objnew.setNewProtocolData();

    }
      private  void saveProtocol(){
//        ProposalSpecialReviewForm objnew = new ProposalSpecialReviewForm();
//        ProposalOrganizationAdminForm jjx = new  ProposalOrganizationAdminForm();
//        ProposalInvestigatorForm proposalInvestigator = new ProposalInvestigatorForm();
//        //private ProposalDetailForm proposalDtetail = null;
//        ProposalDetailAdminForm proposalDetails = new ProposalDetailAdminForm();
//        objnew.setNewProtocolData();

    }
    //Added for the Coeus Enhancement case:#1799 start step:12
    private void showProtocolSearch() throws Exception{
        columnValueEditor.stopCellEditing();
        //Code modified for Case#3070 - Ability to modify funding source
//        CoeusSearch protocolSearch = new CoeusSearch( CoeusGuiConstants.getMDIForm(),"PROTOCOLSEARCH",
        CoeusSearch protocolSearch = new CoeusSearch( CoeusGuiConstants.getMDIForm(),"ORIGINALPROTOCOLSEARCH",
                CoeusSearch.TWO_TABS) ;
        protocolSearch.showSearchWindow();
        HashMap protocolRow = protocolSearch.getSelectedRow();
        tblSpecialReview.setValueAt(protocolRow.get("PROTOCOL_NUMBER"), selRow,3);
        //Modified by shiji for bug id : 2021 step1 -start
        if(moduleCode.trim().equals("INSTITUTE_PROPOSAL") || moduleCode.trim().equals("Award_Module") ||moduleCode.trim().equals("PROPOSAL") ){
            //bug id : 2021 step1 - end
            tblSpecialReview.setValueAt(protocolRow.get("PROTOCOL_STATUS_DESCRIPTION"),selRow,2);
            if(protocolRow.get("APPROVAL_DATE") != null){
                tblSpecialReview.setValueAt(protocolRow.get("APPROVAL_DATE"),selRow,5);
            }else{
                tblSpecialReview.setValueAt("",selRow,4);
            }
            if(protocolRow.get("APPLICATION_DATE") != null){
                tblSpecialReview.setValueAt(protocolRow.get("APPLICATION_DATE"),selRow,4);
            }else{
                tblSpecialReview.setValueAt("",selRow,5);
            }
        }
        
        SpecialReviewFormBean bean = (SpecialReviewFormBean)vecSpecialReviewData.get(selRow);
        if(bean.getAcType().equals(TypeConstants.INSERT_RECORD)){
            bean.setPrevSpRevProtocolNumber(bean.getProtocolSPRevNumber());
        }else if(bean.getAcType()==null || bean.getAcType().equals("null")) {
            bean.setAcType(TypeConstants.UPDATE_RECORD);
        }
        //Addded for Case#4354 - When ENABLE_PROTOCOL_TO_DEV_PROPOSAL is set to ON - Start
        String protocolStatusCode = CoeusGuiConstants.EMPTY_STRING;
        if(protocolRow != null){
            protocolStatusCode = protocolRow.get("PROTOCOL_STATUS_CODE")+CoeusGuiConstants.EMPTY_STRING;
        }
        ProtocolSubmissionInfoBean protocolSubmissionInfoBean = null;
        //Checks if the protocol status code is '203', then gets the protocol submission details 
        //and protocol exempt check list details 
        if(protocolStatusCode != null && protocolStatusCode.equals(EXEMPT_STATUS)){
            String connectTo = CoeusGuiConstants.CONNECTION_URL + "/SubmissionDetailsServlet";
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType('c');
             if(protocolRow != null){
                requester.setId((String)protocolRow.get("PROTOCOL_NUMBER"));
             }
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requester);
            comm.send();
            ResponderBean response = comm.getResponse();
            if(!response.isSuccessfulResponse()){
                throw new CoeusException(response.getMessage(), 1);
            }else{
                 //ProtocolSubmissionInfoBean will have submission details and protocol exempt check list
                 protocolSubmissionInfoBean = (ProtocolSubmissionInfoBean)response.getDataObject();
            }
        }
        if(protocolSubmissionInfoBean != null && protocolSubmissionInfoBean.getProtocolExemptCheckList() != null 
                &&protocolSubmissionInfoBean.getProtocolExemptCheckList().size() > 0){
            Vector vcExemptCheclList = protocolSubmissionInfoBean.getProtocolExemptCheckList();
            StringBuffer checkListForComments = new StringBuffer();
            if(vcExemptCheclList != null && vcExemptCheclList.size() > 0){
                for(int index = 0; index<vcExemptCheclList.size();index++){
                   ProtocolReviewTypeCheckListBean protoExemptCheckListBean =  (ProtocolReviewTypeCheckListBean)vcExemptCheclList.get(index);
                   //Appends "E" before every check list code
                   checkListForComments.append("E");
                   checkListForComments.append(protoExemptCheckListBean.getCheckListCode());
                   if(index != vcExemptCheclList.size()-1){
                       checkListForComments.append(","+EMPTY_SPACE);
                   }
                }
                txtAreaComments.setText(checkListForComments.toString());
                bean.setComments(checkListForComments.toString());
            }
        }
        //Case#4354 - End
        bean.setProtocolSPRevNumber((String)protocolRow.get("PROTOCOL_NUMBER"));
        bean.setProtoSequenceNumber(Integer.parseInt(protocolRow.get("SEQUENCE_NUMBER").toString()));
        saveRequired = true;
    }//End Coeus Enhancement case:#1799 step:12
    
    /** On the TextArea Focus gained get the Comments from the text area.
     * @param focusEvent  Focus Event
     */
    public void focusGained(java.awt.event.FocusEvent focusEvent) {
        Object source = focusEvent.getSource();
        int selectedRow = tblSpecialReview.getSelectedRow();
        if( source.equals( txtAreaComments )){
            if( selectedRow == -1 && tblSpecialReview.getRowCount() > 0 ){
                showWarningMessage();
                tblSpecialReview.requestFocus();
            }
        }
        prvComments = txtAreaComments.getText();
        if(prvComments == null){
            prvComments = "";
        }
    }
    
    /**  Supporting method to show the warning message
     */
    private void showWarningMessage(){
        if( functionType != DISPLAY_MODE ) {
            CoeusOptionPane.showWarningDialog(
                    coeusMessageResources.parseMessageKey(
                    "protocol_SpecialReviewForm_exceptionCode.1053"));
        }
    }
    
    /** This method fires when ever focus is lost from the JTextArea.
     *  It sets the changed value to the corresponding bean.
     *  And sets the ACtype to the bean.
     *  @param focusEvent  Focus Event
     */
    public void focusLost(java.awt.event.FocusEvent focusEvent) {
        String curComments = "";
        if ( !focusEvent.isTemporary()) {
            Object source = focusEvent.getSource();
            int selectedRow = lastSelectedRow;
            if( source.equals( txtAreaComments ) &&
                    lastSelectedRow <= tblSpecialReview.getRowCount()  ){
                SpecialReviewFormBean prBean = null;
                if(vecSpecialReviewData != null){
                    prBean = (SpecialReviewFormBean )vecSpecialReviewData.get(selectedRow);
                }
                if(prBean != null){
                    prBean.setComments( txtAreaComments.getText() );
                    //Changes made check from equals to not equals
                    String acTyp = prBean.getAcType();
                    if( (acTyp != null) ||( !acTyp.equalsIgnoreCase(INSERT_RECORD) )){
                        curComments = txtAreaComments.getText().trim();
                        if( (!curComments.equalsIgnoreCase(prvComments))
                        && (!acTyp.equalsIgnoreCase(INSERT_RECORD))) {
                            
                            prBean.setAcType( UPDATE_RECORD );
                            saveRequired = true;
                        }
                    }
                    if(vecSpecialReviewData != null){
                        // Added by chandra. to fire the saveConfirmation for Award module - start 18 June 2004
                        if( (!curComments.equalsIgnoreCase(prvComments))){
                            saveRequired = true;
                        }
                        //added by chandra - end 18 June 2004
                        vecSpecialReviewData.setElementAt(prBean,selectedRow);
                    }
                }
            }
        }
    }
    
    /** This method is fired whenever the table row is changed.
     * This method contains the implementation of changing the text area contents whenever table row is changed.
     * @param listSelectionEvent  List Selection Event
     */
    public void valueChanged(javax.swing.event.ListSelectionEvent listSelectionEvent) {
        
//        if(listSelectionEvent.getValueIsAdjusting())
//            return;
        int selectedRow = tblSpecialReview.getSelectedRow();
        selRow = selectedRow;
        String comment = null;
        int rowCount =  tblSpecialReview.getRowCount();
        if( selectedRow!= -1 && selectedRow >= 0  && selectedRow <= rowCount &&
                firstEntry && vecSpecialReviewData != null){
            
            SpecialReviewFormBean curBean = (SpecialReviewFormBean)
            vecSpecialReviewData.get( selectedRow );
            if( txtAreaComments.hasFocus() ){
                curBean = (SpecialReviewFormBean)vecSpecialReviewData.get(  lastSelectedRow );
                curBean.setComments( txtAreaComments.getText() );
                return;
                
            }else{
                lastSelectedRow = selectedRow;
            }
            
            //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start                  
            String spRvDesc = curBean.getSpecialReviewDescription();
            //Added for the Coeus Enhancement case:#1799 start step:13
            //If special review type is uman Subjects then enable the find button
            if(enableProtocolLink == 1 && functionType!=TypeConstants.DISPLAY_MODE && spRvDesc.equals(HUMAN_SUBJECTS) ) {
                if(specialRevTypeCodeParam != null && !EMPTY_STRING.equals(specialRevTypeCodeParam) &&
                        curBean.getSpecialReviewCode()==Integer.parseInt(specialRevTypeCodeParam)) {
                    isHumanSub = true;
                }else {
                    isHumanSub = false;
                }
              
                btnFind.setEnabled(isHumanSub);
            }
            //End Coeus Enhancement case:#1799 start step:13
            
            //If special review type is Animal usage then enable the find button
            if(enableIacucProtocolLink == 1 && functionType!=TypeConstants.DISPLAY_MODE && spRvDesc.equals(ANIMAL_USAGE)) {
                if(specialRevTypeCodeParamForIacuc != null && !EMPTY_STRING.equals(specialRevTypeCodeParamForIacuc) &&
                        curBean.getSpecialReviewCode()==Integer.parseInt(specialRevTypeCodeParamForIacuc)) {
                    isAnimalSub = true;
                }else {
                    isAnimalSub = false;
                }
              
                btnFind.setEnabled(isAnimalSub);
            }
            //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
            
            if( curBean != null ){
                comment = curBean.getComments();
                if( curBean.getAcType() == null ){
                    // curBean.setAcType(UPDATE_RECORD);
                }
                if( comment == null){
                    comment = "";
                }
                txtAreaComments.setText( comment );
                txtAreaComments.setCaretPosition(0);
            }
        }
        firstEntry = true ;
    }
    
    public void itemStateChanged(ItemEvent itemEvent) {
        
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAdd;
    public javax.swing.JButton btnDelete;
    public javax.swing.JButton btnFind;
    public javax.swing.JButton btnStartProtocol;
    public javax.swing.JLabel lblProposalNo;
    public javax.swing.JLabel lblProposalValue;
    public javax.swing.JLabel lblSpecialReviewComments;
    public javax.swing.JLabel lblSponsor;
    public javax.swing.JLabel lblSponsorValue;
    public javax.swing.JPanel pnlAddDeleteButtons;
    public javax.swing.JPanel pnlButtonsContainer;
    public javax.swing.JPanel pnlComments;
    public javax.swing.JPanel pnlOkCancelButtons;
    public javax.swing.JPanel pnlProposalDescription;
    public javax.swing.JPanel pnlProposalDescriptionContainer;
    public javax.swing.JPanel pnlTableContainer;
    public javax.swing.JScrollPane scrPnCommentsContainer;
    public javax.swing.JScrollPane scrPnPane1;
    public javax.swing.JSeparator sptrProposalDescription;
    public javax.swing.JTable tblSpecialReview;
    public javax.swing.JTextArea txtAreaComments;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the protocolSpecialReview
     */
    public Vector getProtocolSpecialReview() {
        return protocolSpecialReview;
    }

    /**
     * @param protocolSpecialReview the protocolSpecialReview to set
     */
    public void setProtocolSpecialReview(Vector protocolSpecialReview) {
        this.protocolSpecialReview = protocolSpecialReview;
    }

    /**
     * @return the protocolInvestigator
     */
    public Vector getProtocolInvestigator() {
        return protocolInvestigator;
    }

    /**
     * @param protocolInvestigator the protocolInvestigator to set
     */
    public void setProtocolInvestigator(Vector protocolInvestigator) {
        this.protocolInvestigator = protocolInvestigator;
    }

    /**
     * @return the protocolGeneralInfo
     */
    public Vector getProtocolGeneralInfo() {
        return protocolGeneralInfo;
    }

    /**
     * @param protocolGeneralInfo the protocolGeneralInfo to set
     */
    public void setProtocolGeneralInfo(Vector protocolGeneralInfo) {
        this.protocolGeneralInfo = protocolGeneralInfo;
    }

    /**
     * @return the protocolOrganization
     */
    public Vector getProtocolOrganization() {
        return protocolOrganization;
    }

    /**
     * @param protocolOrganization the protocolOrganization to set
     */
    public void setProtocolOrganization(Vector protocolOrganization) {
        this.protocolOrganization = protocolOrganization;
    }

    // End of variables declaration
    
    /**
     * This is a Custom table model for the special review Jtable.
     */
    public class CustomTableModel extends DefaultTableModel{
        
        /**
         * Constructor
         */
        public CustomTableModel(){
            super(new Object[][]{}, new Object []
            {"Icon", "Special Review", "Approval", "Protocol No.", "Appl. Date", "Appr. Date", "SpecialRevNum", "SeqNo", "protocolNo"});
        }
        
        /** This method checks whether a given Cell is editable or not.
         * @param row Row
         * @param col Column
         * @return  boolean whether cell can be edited or not.
         */
        public boolean isCellEditable(int row, int col){
            //Added for the Coeus Enhancement case:#1799 start step:14
            if((functionType == DISPLAY_MODE) || (col == 0) ||  col > 5){
                return false;
            }else {
                //Modified COEUSDEV-312 :First Protocol on Proposal not being saved on Award record. - Start
                //Checks whehther protocol link is enabled and  selected row is Human Subjects, 
                //then the cell is made non-editable
                //Modified by shiji for bug id : 2021 step2 - start
//                if(moduleCode.equals("INSTITUTE_PROPOSAL") || moduleCode.equals("Award_Module") || moduleCode.trim().equals("PROPOSAL")){
//                    //bug id : 2021 step2 - end
//                    if(isHumanSub && (col==2 || col>3 )) {
//                        return false;
//                    }
//                }
                String stSpecialDesc = (String)tblSpecialReview.getValueAt(row,1);
                if(enableProtocolLink == 1) {
                    if(!EMPTY_STRING.equals(specialRevTypeCodeParam) &&
                            Integer.parseInt(getIDForName(stSpecialDesc, vecSpecialReviewCode))==Integer.parseInt(specialRevTypeCodeParam)) {
                        if(moduleCode.equals("INSTITUTE_PROPOSAL") ||
                                moduleCode.equals("Award_Module") ||
                                moduleCode.trim().equals("PROPOSAL")){
                            isHumanSub = true;
                            if(col==2 || col>3 ) {
                                return false;
                            }
                        }else{
                            isHumanSub = false;
                        }
                    }else{
                        isHumanSub = false;
                    }
                }else{
                    isHumanSub = false;
                }
               
                //COEUSDEV-312 : End
                
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                //Checks whehther IACUC link is enabled and  selected row is Animal Subjects, 
                //then the cell is made non-editable
                if(enableIacucProtocolLink == 1) {
                    if(!EMPTY_STRING.equals(specialRevTypeCodeParamForIacuc) &&
                            Integer.parseInt(getIDForName(stSpecialDesc, vecSpecialReviewCode))==Integer.parseInt(specialRevTypeCodeParamForIacuc)) {
                        if(moduleCode.equals("INSTITUTE_PROPOSAL") ||
                                moduleCode.equals("Award_Module") ||
                                moduleCode.trim().equals("PROPOSAL")){
                            isAnimalSub = true;
                            if(col==2 || col>3 ) {
                                return false;
                            }
                        }else{
                            isAnimalSub = false;
                        }
                    }else{
                        isAnimalSub = false;
                    }
                }else{
                    isAnimalSub = false;
                }
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
                
                return true;
            }
            //Coeus Enhancement Case #1799 - end:  step 14
        }
        
        /** This method is invoked when ever the user changes the
         * contents in the table cell
         * @param row Row
         * @param column Column
         */
        public void fireTableCellUpdated(int row,int column){
            super.fireTableCellUpdated(row,column);
        }
        
        /** This method is used to get the Column Class
         * @param col Column
         * @return Class
         */
        public Class getColumnClass(int col){
            return Object.class;
        }
        
        public void setValueAt(Object aValue, int row, int column) {
            super.setValueAt(aValue, row, column);
        }
        
        public Object getValueAt(int row, int column) {
            Object retValue;
            
            retValue = super.getValueAt(row, column);
            return retValue;
        }
        
        
        
    }
    
    /*
     * Inner class to set the editor for date columns/cells.
     */
    class DateEditor extends AbstractCellEditor
        implements TableCellEditor,MouseListener {
        
        private String colName;
        private static final String DATE_SEPARATERS = ":/.,|-";
        private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
        private CoeusTextField dateComponent = new CoeusTextField();
        private String stDateValue;
        private int selectedRow;
        private int selectedColumn;
        boolean temporary;
        DateEditor(String colName) {
            this.colName = colName;
            ((JTextField)dateComponent).setFont(CoeusFontFactory.getNormalFont());
            
            dateComponent.addMouseListener(this);
            dateComponent.addFocusListener(new FocusAdapter(){
                public void focusGained(FocusEvent fe){
                    temporary = false;
                }
                public void focusLost(FocusEvent fe){
                    if ( !fe.isTemporary()  ){
                        if(!temporary){
                            stopCellEditing();
                        }
                    }
                }
            });
        }
        
        /**
         * Date validation
         */
        
        private void validateEditorComponent(){
            temporary = true;
            String formattedDate = null;
            String editingValue = (String) getCellEditorValue();
            if (editingValue != null && editingValue.trim().length() > 0) {
                // validate date field
                formattedDate = new DateUtils().formatDate(editingValue,
                        DATE_SEPARATERS,REQUIRED_DATEFORMAT);
                if(formattedDate == null) {
                    //Case # 2039- start
                    formattedDate = new DateUtils().restoreDate(editingValue, DATE_SEPARATERS);
                    if( formattedDate == null || formattedDate.equals(editingValue)) {
                        //Case # 2039- End
                        // invalid date
                        CoeusOptionPane.showErrorDialog("Please enter valid date");
                        dateComponent.setText(stDateValue);
                    }
                }else{
                    // valid date
                    dateComponent.setText(formattedDate);
                    if(!editingValue.equals(stDateValue)){
                        setModel(formattedDate);
                    }
                }
            }
            if( ((editingValue == null ) || (editingValue.trim().length()== 0 )) &&
                    (stDateValue != null) && (stDateValue.trim().length()>= 0 )){
                saveRequired = true;
                setModel(null);
            }
        }
        
        // Sets the editing value to the Bean
        private void setModel(String formatDate){//editingValue
            saveRequired=true;
            String appDate = dtUtils.restoreDate(formatDate,"/:-,");
            SpecialReviewFormBean pBean =
                    (SpecialReviewFormBean)vecSpecialReviewData.elementAt(selectedRow);
            String aType = pBean.getAcType();
            try{
                if(selectedColumn == 4){
                    if(appDate != null){//editingValue    Handle
                        pBean.setApplicationDate(new java.sql.Date(dtFormat.parse(appDate).getTime()));
                    }else{
                        pBean.setApplicationDate(null);
                    }
                }else if(selectedColumn == 5){
                    if(appDate != null){
                        pBean.setApprovalDate(new java.sql.Date(dtFormat.parse(appDate).getTime()));
                    }else{
                        pBean.setApprovalDate(null);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            if(aType != null){
                if(!aType.equalsIgnoreCase(INSERT_RECORD)){
                    pBean.setAcType(UPDATE_RECORD);
                }
            }
        }
        
        /** This nethod is used to get Editor component of the Cell
         * @param table Table
         * @param value Object
         * @param isSelected boolean
         * @param row Row
         * @param column Column
         * @return Component
         */
        public Component getTableCellEditorComponent(JTable table,Object value,
                boolean isSelected, int row,int column){
            
            selectedRow = row;
            selectedColumn = column;
            JTextField tfield =(JTextField)dateComponent;
            String currentValue = (String)value;
            String stTempValue =(String)tblSpecialReview.getValueAt(row,column);
            if(stTempValue != null){
                stDateValue = dtUtils.restoreDate(stTempValue,DATE_SEPARATERS) ;
            }
            if( ( currentValue != null  ) && (currentValue.trim().length()!= 0) ){
                String newValue = dtUtils.restoreDate(currentValue,
                        DATE_SEPARATERS) ;
                tfield.setText(newValue);
                return dateComponent;
            }
            
            tfield.setText( ((String)value));
            return dateComponent;
        }
        
        /**
         * Forwards the message from the CellEditor to the delegate.
         * @return boolean contains true if editing was stopped; false otherwise
         */
        public boolean stopCellEditing() {
            validateEditorComponent();
            return super.stopCellEditing();
        }
        
        /** Returns the value contained in the editor.
         * @return the value contained in the editor
         */
        public Object getCellEditorValue() {
            return ((JTextField)dateComponent).getText();
        }
        
        /**
         * Invoked when an cell has been selected or deselected by the user.
         * The code written for this method performs the operations that need to
         * occur when an cell is selected (or deselected).
         * @param e an ItemEvent.
         */
        public void itemStateChanged(ItemEvent e) {
            super.fireEditingStopped();
        }
        
        /** Gets Click Count To Start
         * @return  int
         */
        public int getClickCountToStart(){
            return 1;
        }
        
        public void mouseClicked(MouseEvent mouseEvent) {
            if(mouseEvent.getClickCount() != 2) return ;
            try {
                
                //Bug Fix 2083 Start 1
                /*if(authorizationCheck()){
                    SpecialReviewFormBean bean = (SpecialReviewFormBean)vecSpecialReviewData.get(selRow);
                    if(bean.getProtocolSPRevNumber() != null && !bean.getProtocolSPRevNumber().equals("")){
                 
                        ProtocolDetailForm protocolForm = new ProtocolDetailForm(TypeConstants.DISPLAY_MODE,bean.getProtocolSPRevNumber(),CoeusGuiConstants.getMDIForm());
                        protocolForm.showDialogForm();
                    }
                }else{
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(VIEW_PROTOCOL));
                }*/
                
                columnValueEditor.stopCellEditing();
                
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                int selectedRow = tblSpecialReview.getSelectedRow();
                String spRvDesc = null;
                
                int rowCount =  tblSpecialReview.getRowCount();
                if( selectedRow!= -1 && selectedRow >= 0 && vecSpecialReviewData != null && selectedRow <= rowCount ){
                    SpecialReviewFormBean curBean = (SpecialReviewFormBean)vecSpecialReviewData.get( selectedRow );
                    if( txtAreaComments.hasFocus() ){
                        curBean = (SpecialReviewFormBean)vecSpecialReviewData.get(  lastSelectedRow );
                        curBean.setComments( txtAreaComments.getText() );
                        return;
                        
                    }else{
                        lastSelectedRow = selectedRow;
                    }
                    spRvDesc = curBean.getSpecialReviewDescription();
                }
                
                //if Special Review Type is Human Subjects then it should display the IRB details in Display mode
                if(selectedRow != -1){
                    if(spRvDesc.equals(HUMAN_SUBJECTS)){
                        String protocolNo = (String)tblSpecialReview.getValueAt(selectedRow,3);
                        // 4154: Problems in IRB Linking - Start
                        ProtocolInfoBean protocolInfoBean = getProtocolDetails(protocolNo);
                        if(protocolInfoBean != null && protocolInfoBean.getProtocolNumber() != null){
                            if(!protocolNo.trim().equals("")){
                                if(authorizationCheck()){
                                    ProtocolDetailForm protocolForm = new ProtocolDetailForm(TypeConstants.DISPLAY_MODE, protocolNo ,CoeusGuiConstants.getMDIForm());
                                    protocolForm.showDialogForm();
//                                }
                                }
                            }
                        }
                    }else if(spRvDesc.equals(ANIMAL_USAGE)){
                        String protocolNo = (String)tblSpecialReview.getValueAt(selectedRow,3);
                        //Check for the valid protocol, if it is valid then check whether user has the right to view that protocol
                        edu.mit.coeus.iacuc.bean.ProtocolInfoBean protocolInfoBean = validIACUCProtocol(protocolNo);
                        if(protocolInfoBean != null && protocolInfoBean.getProtocolNumber() != null){
                            if(!protocolNo.trim().equals("")){
                                if(authorizationCheckForIacuc()){
                                    edu.mit.coeus.iacuc.gui.ProtocolDetailForm protocolForm = new edu.mit.coeus.iacuc.gui.ProtocolDetailForm(TypeConstants.DISPLAY_MODE, protocolNo ,CoeusGuiConstants.getMDIForm());
                                    protocolForm.showDialogForm();
                                }
                            }
                        }
                    }else{
                        CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey("SpecialReviewForm_exceptionCode.1070"));
                    }
                    //Bug Fix 2083 End 1
                }
                // 4154: Problems in IRB Linking - End
            }catch(Exception ex) {
                ex.printStackTrace();
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
        
    }
    
    /**
     * This Class is used as a Renderer to all the Column Cells.
     */
    public class ColumnValueRenderer extends JTextField implements TableCellRenderer {
        
        private int selRow;
        private int selCol;
        /**
         * Constructor
         */
        public ColumnValueRenderer() {
            setOpaque(true);
            setBorder(new EmptyBorder(0,0,0,0));
            tblSpecialReview.editCellAt(selRow,selCol);
            setFont(CoeusFontFactory.getNormalFont());
        }
        
        /**
         * This method is used to get the Renderer component for the Table Cell
         * @param table Table Object
         * @param value Object
         * @param isSelected Is Selected or not
         * @param hasFocus Has focus or not
         * @param row Row
         * @param column Column
         * @return  Component
         */
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            
            selRow = row;
            selCol = column;
            setText( (value == null) ? "" : value.toString() );
            return this;
        }
    }
    
    /**
     * This is a Default Cell Editor  JTectField Component for the protocol Number.
     */
    class ColumnValueEditor extends AbstractCellEditor
            implements TableCellEditor,MouseListener{
        private CoeusTextField txtDesc;
        private int selectedRow ;
        private int selectedCol ;
        private String stProtocolSPRevNumber;
        // Constructor which sets the size of TextField
        ColumnValueEditor(int len ){
            
            //super(new JTextField());
            txtDesc = new CoeusTextField();
            txtDesc.setFont(CoeusFontFactory.getNormalFont());
            txtDesc.setDocument(new LimitedPlainDocument(len));
            txtDesc.addMouseListener(this);
//            txtDesc.addFocusListener(new FocusAdapter(){
//                public void focusLost(FocusEvent fe){
//                    if (!fe.isTemporary() ){
//                        stopCellEditing();
//
//                        //tblSpecialReview.setRowSelectionInterval(selectedRow,selectedRow);
//                    }
//                }
//            });
        }
        // This method sets the edited value in the bean.
        
        private void setEditorValueToBean(String editingValue){
            SpecialReviewFormBean pBean = null;
            int row = tblSpecialReview.getSelectedRow();
            if(selectedRow != row) return;
            if( (editingValue == null )){// || (editingValue.trim().length()== 0 )
                ((JTextField)txtDesc).setText( "");
                ((DefaultTableModel)tblSpecialReview.getModel()).setValueAt("",selectedRow,selectedCol); // Handle
                tblSpecialReview.setRowSelectionInterval(selectedRow, selectedRow);
            }else{
                ((JTextField)txtDesc).setText( editingValue);
                if(!editingValue.equalsIgnoreCase(stProtocolSPRevNumber)){
                    saveRequired = true;
                    if(vecSpecialReviewData != null){
                        
                        pBean = (SpecialReviewFormBean)vecSpecialReviewData.elementAt(selectedRow);
                        if(pBean != null){
                            pBean.setProtocolSPRevNumber(editingValue);
                            //Added for fix special review bug in new entry, start by tarique
                            pBean.setPrevSpRevProtocolNumber(pBean.getProtocolSPRevNumber());
                            //Added for fix special review bug in new entry, end by tarique
                            String aType = pBean.getAcType();
                            
                            if (aType != null){
                                if(!aType.equalsIgnoreCase(INSERT_RECORD)) {
                                    pBean.setAcType( UPDATE_RECORD );
                                    saveRequired = true;
                                    stProtocolSPRevNumber = editingValue;
                                }
                            }
                            if(vecSpecialReviewData != null){
                                vecSpecialReviewData.setElementAt(pBean,selectedRow);
                            }
                        }
                    }
                    ((DefaultTableModel)tblSpecialReview.getModel()).setValueAt(editingValue,selectedRow,selectedCol);// Handle
                }else{
                    ((DefaultTableModel)tblSpecialReview.getModel()).setValueAt(stProtocolSPRevNumber,selectedRow,selectedCol);// Handle
                }
            }
            
        }
        
        /** This method gets the Cell Editor Component
         * @param table Table Object
         * @param value Object Value
         * @param isSelected Whether selected or not
         * @param row Row
         * @param column Column
         * @return  Component
         */
        public Component getTableCellEditorComponent(JTable table,
                Object value,
                boolean isSelected,
                int row,
                int column){
            
            selectedRow = row;
            selectedCol = column;
            stProtocolSPRevNumber = (String)tblSpecialReview.getValueAt(row,3);
            if(stProtocolSPRevNumber == null){
                stProtocolSPRevNumber = "";
            }
            String newValue = ( String ) value ;
            if( newValue != null && newValue.length() > 0 ){
                txtDesc.setText( (String)newValue );
            }else{
                txtDesc.setText("");
            }
            this.selectedRow = row;
            return txtDesc;
        }
        
        
        /** This method gets the Click Count to start.
         * @return  int
         */
        public int getClickCountToStart(){
            return 1;
        }
        
        /**
         * This method returns the value contained in the editor.
         * @return Object the value contained in the editor
         */
        public Object getCellEditorValue() {
            //Modified COEUSDEV-312 :First Protocol on Proposal not being saved on Award record. - Start
            //Gets the Protocol Number column value based on the column selection
//            return ((JTextField)txtDesc).getText();
            if(tblSpecialReview.getSelectedColumn() == 3){
                return ((JTextField)txtDesc).getText();
            }else{
                return (String)tblSpecialReview.getValueAt(tblSpecialReview.getSelectedRow(),3);
            }
            //COEUSDEV-312 : End
        }
        
        /** This methods returns whether the Cell Editing has to stopped
         * @return  boolean
         */
        public boolean stopCellEditing() {
            String editingValue = (String)getCellEditorValue();
            
            int r = tblSpecialReview.getSelectedRow();
            
            //Added for the Coeus Enhancement case:#1799 start step:15
//            if(moduleCode.equals("PROPOSAL"))
//                isHumanSub = true;
            if(isHumanSub && !"".equals(editingValue)){
                try {
                    ProtocolInfoBean protocolBean = validProtocol(editingValue);
                    if(protocolBean != null){
//                        if(display) {
//                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(CHOOSE_PROTOCOL));
//                            display = false;
//                            isHumanSub = false;
//                        }
//                        cancelCellEditing();
//                        editingValue = "";
//                        setRequestFocusInSpecialReviewThread(selRow,3);
//                    }else {
                        tblSpecialReview.setValueAt(protocolBean.getProtocolNumber(), selRow,3);
                        //Modified by shiji for bug id : 2021 step2 - start
                        if(moduleCode.trim().equals("INSTITUTE_PROPOSAL") || moduleCode.trim().equals("Award_Module") || moduleCode.trim().equals("PROPOSAL")){
                            //bug id : 2021 step2 - end
                            // 4154: Problems in IRB Linking - Start
//                            tblSpecialReview.setValueAt(protocolBean.getProtocolStatusDesc(),selRow,2);
                            if(protocolBean.getProtocolStatusDesc() != null){
                                tblSpecialReview.setValueAt(protocolBean.getProtocolStatusDesc(),selRow,2);
                            }
                            if(protocolBean.getApplicationDate() != null){
                                String appDate = dtUtils.formatDate(protocolBean.getApplicationDate().toString(),"dd-MMM-yyyy");
                                tblSpecialReview.setValueAt(appDate,selRow,4);
                            }
                            if(protocolBean.getApprovalDate() != null){
                                String apprvDate = dtUtils.formatDate(protocolBean.getApprovalDate().toString(),"dd-MMM-yyyy");
                                tblSpecialReview.setValueAt(apprvDate,selRow,5);
                            } else if(protocolBean.getProtocolNumber() != null) {
                                tblSpecialReview.setValueAt("",selRow,5);
                            }
                            // 4154: Problems in IRB Linking - End
                            SpecialReviewFormBean formBean = (SpecialReviewFormBean)vecSpecialReviewData.get(selRow);
                            formBean.setProtoSequenceNumber(protocolBean.getSequenceNumber());
                            if(formBean.getAcType().equals(TypeConstants.INSERT_RECORD)) {
                                if((protocolBean.getProtocolNumber()!=null) && formBean.getProtocolSPRevNumber().equals(protocolBean.getProtocolNumber())){
                                    formBean.setPrevSpRevProtocolNumber(formBean.getPrevSpRevProtocolNumber());
                                }else{
                                    formBean.setPrevSpRevProtocolNumber(formBean.getProtocolSPRevNumber());
                                }
                            }
                        }
                    }
                }catch(CoeusException coeusEx) {
                    CoeusOptionPane.showInfoDialog(coeusEx.getMessage());
                }
            }//End Coeus Enhancement case:#1799 start step:15
            
            //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
            if(isAnimalSub && !"".equals(editingValue)){
                try {
                    edu.mit.coeus.iacuc.bean.ProtocolInfoBean protocolBean = validIACUCProtocol(editingValue);
                    if(protocolBean != null){
                        tblSpecialReview.setValueAt(protocolBean.getProtocolNumber(), selRow,3);
                        if(moduleCode.trim().equals("INSTITUTE_PROPOSAL") || moduleCode.trim().equals("Award_Module") || moduleCode.trim().equals("PROPOSAL")){
                            if(protocolBean.getProtocolStatusDesc() != null){
                                tblSpecialReview.setValueAt(protocolBean.getProtocolStatusDesc(),selRow,2);
                            }
                            if(protocolBean.getApplicationDate() != null){
                                String appDate = dtUtils.formatDate(protocolBean.getApplicationDate().toString(),"dd-MMM-yyyy");
                                tblSpecialReview.setValueAt(appDate,selRow,4);
                            }
                            if(protocolBean.getApprovalDate() != null){
                                String apprvDate = dtUtils.formatDate(protocolBean.getApprovalDate().toString(),"dd-MMM-yyyy");
                                tblSpecialReview.setValueAt(apprvDate,selRow,5);
                            } else if(protocolBean.getProtocolNumber() != null) {
                                tblSpecialReview.setValueAt("",selRow,5);
                            }
                            SpecialReviewFormBean formBean = (SpecialReviewFormBean)vecSpecialReviewData.get(selRow);
                            formBean.setProtoSequenceNumber(protocolBean.getSequenceNumber());
                            if(formBean.getAcType().equals(TypeConstants.INSERT_RECORD)) {
                                if(formBean.getProtocolSPRevNumber().equals(protocolBean.getProtocolNumber())){
                                    formBean.setPrevSpRevProtocolNumber(formBean.getPrevSpRevProtocolNumber());
                                }else{
                                    formBean.setPrevSpRevProtocolNumber(formBean.getProtocolSPRevNumber());
                                }
                            }
                        }
                    }
                }catch(CoeusException coeusEx) {
                    CoeusOptionPane.showInfoDialog(coeusEx.getMessage());
                }
            }
            //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
            
            setEditorValueToBean(editingValue);
            
            if(selectedRow != -1){
                //tblSpecialReview.setRowSelectionInterval(selectedRow, selectedRow);
            }
            return super.stopCellEditing();
        }
        
        //Added for the Coeus Enhancement case:#1799 start step:16
        private ProtocolInfoBean validProtocol(String protocolNum) throws CoeusException{
            ProtocolInfoBean infoBean = null;
            if (protocolNum != null && protocolNum.trim().length() > 0 && !protocolNum.equals("")) {
                //Code added for Case#3070 - Ability to modify funding source - starts
                if(protocolNum.indexOf("A") != -1 || protocolNum.indexOf("R") != -1){
                    protocolNum = "A001";
                }
                //Code added for Case#3070 - Ability to modify funding source - ends
                String connectTo = CoeusGuiConstants.CONNECTION_URL + "/protocolMntServlet";
                RequesterBean requester = new RequesterBean();
                requester.setFunctionType('f');
                requester.setDataObject(protocolNum);
                AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requester);
                comm.send();
                ResponderBean response = comm.getResponse();
                if(!response.isSuccessfulResponse()){
                    throw new CoeusException(response.getMessage(), 1);
                }else{
                    infoBean = (ProtocolInfoBean)response.getDataObject();
                }
            }
            return infoBean;
        }//End Coeus Enhancement case:#1799 start step:16
        
        /**
         * This method is used to call Stop Editing
         */
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
        
        public void mouseClicked(MouseEvent mouseEvent ) {
            if(mouseEvent.getClickCount() != 2) return ;
            try {
                //Bug Fix 2083 Start 2
                /*if(authorizationCheck()){
                    SpecialReviewFormBean bean = (SpecialReviewFormBean)vecSpecialReviewData.get(selRow);
                    if(bean.getProtocolSPRevNumber() != null && !bean.getProtocolSPRevNumber().equals("")){
                        //Check for valid protocol
                        ProtocolInfoBean infoBean = validProtocol(bean.getProtocolSPRevNumber());
                        if(infoBean!= null){
                            ProtocolDetailForm protocolForm = new ProtocolDetailForm(TypeConstants.DISPLAY_MODE,bean.getProtocolSPRevNumber(),CoeusGuiConstants.getMDIForm());
                            protocolForm.showDialogForm();
                        }
                    }
                }else{
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(VIEW_PROTOCOL));
                }*/
                
                columnValueEditor.stopCellEditing();              
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                int selectedRow = tblSpecialReview.getSelectedRow();
                String spRvDesc = null;
                
                int rowCount =  tblSpecialReview.getRowCount();
                if( selectedRow!= -1 && selectedRow >= 0 && vecSpecialReviewData != null && selectedRow <= rowCount ){
                    SpecialReviewFormBean curBean = (SpecialReviewFormBean)vecSpecialReviewData.get( selectedRow );
                    if( txtAreaComments.hasFocus() ){
                        curBean = (SpecialReviewFormBean)vecSpecialReviewData.get(  lastSelectedRow );
                        curBean.setComments( txtAreaComments.getText() );
                        return;
                        
                    }else{
                        lastSelectedRow = selectedRow;
                    }
                    spRvDesc = curBean.getSpecialReviewDescription();
                }
                
                //if Special Review Type is Human Subjects then it should display the IRB details in Display mode
                if(selectedRow != -1){
                    if(spRvDesc.equals(HUMAN_SUBJECTS)){
                        String protocolNo = (String)tblSpecialReview.getValueAt(selectedRow,3);
                        // 4154: Problems in IRB Linking - Start
                        ProtocolInfoBean protocolInfoBean = validProtocol(protocolNo);
                        if(protocolInfoBean != null && protocolInfoBean.getProtocolNumber() != null){
                            if(!protocolNo.trim().equals("")){
                                if(authorizationCheck()){
                                    //Check for valid protocol
//                                ProtocolInfoBean infoBean = validProtocol(protocolNo);
//                                if(infoBean!= null){
                                    ProtocolDetailForm protocolForm = new ProtocolDetailForm(TypeConstants.DISPLAY_MODE, protocolNo ,CoeusGuiConstants.getMDIForm());
                                    protocolForm.showDialogForm();
//                                }
                                }
                            }
                        }
                        //if Special Review Type is Human Subjects then it should display the IACUC details in Display mode
                    } else if(spRvDesc.equals(ANIMAL_USAGE)){
                        String protocolNo = (String)tblSpecialReview.getValueAt(selectedRow,3);
                        //Check for the valid protocol, if it is valid then check whether user has the right to view that protocol
                        edu.mit.coeus.iacuc.bean.ProtocolInfoBean protocolInfoBean = validIACUCProtocol(protocolNo);
                        if(protocolInfoBean != null && protocolInfoBean.getProtocolNumber() != null){
                            if(!protocolNo.trim().equals("")){
                                if(authorizationCheckForIacuc()){
                                    edu.mit.coeus.iacuc.gui.ProtocolDetailForm protocolForm = new edu.mit.coeus.iacuc.gui.ProtocolDetailForm(TypeConstants.DISPLAY_MODE, protocolNo ,CoeusGuiConstants.getMDIForm());
                                    protocolForm.showDialogForm();
                                }
                            }
                        }
                    } else{
                        CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey("SpecialReviewForm_exceptionCode.1070"));
                    }
                }
                //Bug Fix 2083 End 2
                // 4154: Problems in IRB Linking - End
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
            }catch(Exception ex) {
                ex.printStackTrace();
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
        
    }
    
    /** This class is created for the bug fix #920.  This class provides
     *the Editor for the Special Review Type and Approval Type combo boxes.
     *The ItemStateChange event also handled here.
     * Added by chandra
     */
    
    public class ComboEditor extends AbstractCellEditor implements TableCellEditor{
        
        private CoeusComboBox cmbSpecialReview;
        private CoeusComboBox cmbApproval;
        private SpecialReviewFormBean comboBoxBean;
        
        private int column;
        
        ComboEditor(){
            cmbSpecialReview = new CoeusComboBox();
            cmbApproval = new CoeusComboBox();
            populateSpecialReviewCombo();
            populateApproveCombo();
            
            
            if(vecSpecialReviewCode != null){
                vecSpecialReviewCodeDescriptions = getDescriptionsForCombo(vecSpecialReviewCode);
            }
            if(vecSpecialReviewCodeDescriptions != null){
                cmbSpecialReview.addItemListener(new ItemListener(){
                    public void itemStateChanged( ItemEvent item ){
                        if(item.getStateChange() == ItemEvent.DESELECTED) {
                            //item is first deselected. no need to do anything here.
                            return ;
                        }
                        //                        int row = tblSpecialReview.getSelectedRow();
                        int row = selRow;
                        int col = tblSpecialReview.getSelectedColumn();
                        SpecialReviewFormBean pbean = null;
                        String prevValue = "";
                        isHumanSub=false;
                        isAnimalSub=false;
                        if(vecSpecialReviewData != null && row != -1){
                            pbean = (SpecialReviewFormBean)vecSpecialReviewData.elementAt(row);
                            if(pbean != null){
                                int code = pbean.getSpecialReviewCode();
                                prevValue = getDescriptionForId(code, vecSpecialReviewCode);
                                String curValue = ((JComboBox)item.getSource()).getSelectedItem().toString();
                                if(curValue != null) {//&& prevValue != null)
                                    //Modified for Coeus Enhancement Case #1799 - start:  step 17
                                    String stCode = getIDForName(curValue, vecSpecialReviewCode);
                                    
                                    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                                    if(stCode.equals("1")){
                                        if(enableProtocolLink == 1) {
                                            if((specialRevTypeCodeParam != null && !EMPTY_STRING.equals(specialRevTypeCodeParam) &&
                                                    code == Integer.parseInt(specialRevTypeCodeParam)) &&
                                                    code != Integer.parseInt(stCode) || stCode.equals(specialRevTypeCodeParam) &&
                                                    code != Integer.parseInt(stCode)){
                                                tblSpecialReview.setValueAt("", row , 2);
                                                tblSpecialReview.setValueAt("", row , 3);
                                                tblSpecialReview.setValueAt("", row , 4);
                                                tblSpecialReview.setValueAt("", row , 5);
                                                SpecialReviewFormBean specRevFormBean = (SpecialReviewFormBean)vecSpecialReviewData.get(row);
                                                specRevFormBean.setProtocolSPRevNumber("");
                                                specRevFormBean.setApprovalDescription("");
                                                specRevFormBean.setApplicationDate(null);
                                                specRevFormBean.setApprovalDate(null);
                                                ((DefaultTableModel)tblSpecialReview.getModel()).
                                                        fireTableDataChanged();
                                            }
                                            if(stCode.equals(specialRevTypeCodeParam) && (!curValue.equals(""))) {
                                                isHumanSub=true;
                                                //isFindEnable(true);
                                                ((DefaultTableModel)tblSpecialReview.getModel()).
                                                        fireTableDataChanged();
                                                if(!humanSubRows.contains(new Integer(row)))
                                                    humanSubRows.addElement(new Integer(row));
                                                
                                            }else if(humanSubRows.contains(new Integer(row))) {
                                                humanSubRows.remove(new Integer(row));
                                                ((DefaultTableModel)tblSpecialReview.getModel()).
                                                        fireTableDataChanged();
                                            }else if(!stCode.equals(specialRevTypeCodeParam)) {
                                                
                                                //isFindEnable(false);
                                            }
                                        }
                                    }else if(stCode.equals("2")){
                                        if(enableIacucProtocolLink == 1) {
                                            if((specialRevTypeCodeParamForIacuc != null && !EMPTY_STRING.equals(specialRevTypeCodeParamForIacuc) &&
                                                    code == Integer.parseInt(specialRevTypeCodeParamForIacuc)) &&
                                                    code != Integer.parseInt(stCode) || stCode.equals(specialRevTypeCodeParamForIacuc) &&
                                                    code != Integer.parseInt(stCode)){
                                                tblSpecialReview.setValueAt("", row , 2);
                                                tblSpecialReview.setValueAt("", row , 3);
                                                tblSpecialReview.setValueAt("", row , 4);
                                                tblSpecialReview.setValueAt("", row , 5);
                                                SpecialReviewFormBean specRevFormBean = (SpecialReviewFormBean)vecSpecialReviewData.get(row);
                                                specRevFormBean.setProtocolSPRevNumber("");
                                                specRevFormBean.setApprovalDescription("");
                                                specRevFormBean.setApplicationDate(null);
                                                specRevFormBean.setApprovalDate(null);
                                                ((DefaultTableModel)tblSpecialReview.getModel()).
                                                        fireTableDataChanged();
                                            }
                                            if(stCode.equals(specialRevTypeCodeParamForIacuc) && (!curValue.equals(""))) {
                                                isAnimalSub=true;
                                                
                                                ((DefaultTableModel)tblSpecialReview.getModel()).
                                                        fireTableDataChanged();
                                                if(!animalSubRows.contains(new Integer(row)))
                                                    animalSubRows.addElement(new Integer(row));
                                                
                                            }else if(animalSubRows.contains(new Integer(row))) {
                                                animalSubRows.remove(new Integer(row));
                                                ((DefaultTableModel)tblSpecialReview.getModel()).
                                                        fireTableDataChanged();
                                            }else if(!stCode.equals(specialRevTypeCodeParamForIacuc)) {
                                                
                                                //isFindEnable(false);
                                            }
                                        }
                                    }
                                    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
                                    
                                    //Coeus Enhancement Case #1799 - end:  step17
                                    if(!curValue.equalsIgnoreCase(prevValue)){
                                        saveRequired = true;
                                        pbean.setSpecialReviewCode(new Integer(stCode).intValue());
                                        pbean.setSpecialReviewDescription(curValue);
                                        String aType = pbean.getAcType();
                                        if(!aType.equalsIgnoreCase(INSERT_RECORD)){
                                            pbean.setAcType(UPDATE_RECORD);
                                        }
                                        
                                    }                                  
                                }
                            }
                        }
                        btnFind.setEnabled(isHumanSub);
                        btnStartProtocol.setEnabled(isHumanSub);
                        tblSpecialReview.setRowSelectionInterval(row, row);
                        tblSpecialReview.setColumnSelectionInterval(1,1);
                    }
                });
            }
            
            
            if(vecApprovalTypeCode != null){
                vecApprovalCodeDescriptions = getDescriptionsForCombo(vecApprovalTypeCode);
            }
            if(vecApprovalCodeDescriptions != null){
                cmbApproval.addItemListener(new ItemListener(){
                    public void itemStateChanged( ItemEvent itemEvent ){
                        //bug fix #958
                        if(itemEvent.getStateChange() == ItemEvent.DESELECTED) {
                            //item is first deselected. no need to do anything here.
                            return ;
                        }
                        //                        int row = tblSpecialReview.getSelectedRow();
                        int row = selRow;
                        int col = tblSpecialReview.getSelectedColumn();
                        SpecialReviewFormBean pbean = null;
                        String prevValue = "";
                        if(vecSpecialReviewData != null && row != -1){
                            pbean = (SpecialReviewFormBean)vecSpecialReviewData.elementAt(row);
                            if(pbean != null){
                                int code = pbean.getApprovalCode();
                                prevValue = getDescriptionForId(code, vecApprovalTypeCode);
                                String curValue = ((JComboBox)itemEvent.getSource()).getSelectedItem().toString();
                                if(curValue != null) {
                                    if(!curValue.equalsIgnoreCase(prevValue)){
                                        saveRequired = true;
                                        String stCode = getIDForName(curValue, vecApprovalTypeCode);
                                        pbean.setApprovalCode(new Integer(stCode).intValue());
                                        pbean.setApprovalDescription(curValue);
                                        String aType = pbean.getAcType();
                                        // bug fix 1732
                                        if(prevValue!= null && !(prevValue.equals("")) && !aType.equalsIgnoreCase(INSERT_RECORD)){
                                            pbean.setAcType(UPDATE_RECORD);
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }
        
        /** Added for the Coeus Enhancement case:#1799 start step:17
         * this method sets focus back to component
         * @return void
         */
        private void isFindEnable(final boolean isEnable,final int row){
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    btnFind.setEnabled(isEnable);
                    tblSpecialReview.setRowSelectionInterval(row, row);
                }
            });
        }//End Coeus Enhancement case:#1799 start step:17
        
        /** Populate the SpecialReview combox data. Insert all the elements
         *from the vector to the combobox model.
         */
        private void populateSpecialReviewCombo(){
            //            if(vecSpecialReviewCode!= null && vecSpecialReviewCode.size()> 0){
            //                cmbSpecialReview.setModel(new DefaultComboBoxModel(vecSpecialReviewCode));
            //            }
            //Bug Fix #1732 - start
            int size = vecSpecialReviewCode.size();
            ComboBoxBean comboBoxBean;
            cmbSpecialReview.addItem(new ComboBoxBean("",""));
            for(int index = 0; index < size; index++) {
                comboBoxBean = (ComboBoxBean)vecSpecialReviewCode.get(index);
                cmbSpecialReview.addItem(comboBoxBean);
            }//Bug Fix #1732 - End
        }
        
        /** Populate the Approval Type combox data. Insert all the elements
         *from the vector to the combobox model.
         */
        private void populateApproveCombo(){
            //            if(vecApprovalTypeCode!= null && vecApprovalTypeCode.size() > 0){
            //                cmbApproval.setModel(new DefaultComboBoxModel(vecApprovalTypeCode));
            //            }
            //Bug Fix #1732 - start
            int size = vecApprovalTypeCode.size();
            ComboBoxBean comboBoxBean;
            cmbApproval.addItem(new ComboBoxBean("",""));
            for(int index = 0; index < size; index++) {
                comboBoxBean = (ComboBoxBean)vecApprovalTypeCode.get(index);
                cmbApproval.addItem(comboBoxBean);
            }//Bug Fix #1732 - End
        }
        
        
        /**
         * @param table
         * @param value
         * @param isSelected
         * @param row
         * @param column
         * @return
         */
        public java.awt.Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.column= column;
            selRow = row;
            switch(column){
                case 1:
                    if( value != null && !((String)value).trim().equals("")){
                        ComboBoxBean specialReviewType = new ComboBoxBean();
                        specialReviewType.setDescription(value.toString());
                        cmbSpecialReview.setSelectedItem(specialReviewType);
                    }else{
                        cmbSpecialReview.setSelectedIndex(0);
                    }
                    return cmbSpecialReview;
                case 2:
                    if( value != null && !((String)value).trim().equals("")){
                        ComboBoxBean approvalType = new ComboBoxBean();
                        approvalType.setDescription(value.toString());
                        cmbApproval.setSelectedItem(approvalType);
                    }else{
                        cmbApproval.setSelectedIndex(0);
                    }
                    return cmbApproval;
            }
            return cmbSpecialReview;
        }
        
        /**
         * @return a component value which it stores in the corresponding
         *combo box.
         */
        public Object getCellEditorValue() {
            switch(column){
                case 1:
                    return cmbSpecialReview.getSelectedItem().toString();
                case 2:
                    return cmbApproval.getSelectedItem().toString();
            }
            return cmbSpecialReview;
        }
    }
    //Added for Coeus Enhancement Case #1799 - start:  step 18
    public void setSpecialRevTypeCode(String code) {
        this.specialRevTypeCodeParam=code;
    }
    
    /**
     * Getter for property enableProtocolLink.
     * @return Value of property enableProtocolLink.
     */
    public int getEnableProtocolLink() {
        return enableProtocolLink;
    }
    
    /**
     * Setter for property enableProtocolLink.
     * @param enableProtocolLink New value of property enableProtocolLink.
     */
    public void setEnableProtocolLink(int enableProtocolLink) {
        this.enableProtocolLink = enableProtocolLink;
    }
    
    //to open the protocoldetailform on double click of the human subjects row
    public void mouseClicked(MouseEvent event) {
        if(event.getClickCount() != 2) return ;
        columnValueEditor.stopCellEditing();
        try {
            
            //Bug Fix 2083 Start 3
            /*if(authorizationCheck()){
                SpecialReviewFormBean bean = (SpecialReviewFormBean)vecSpecialReviewData.get(selRow);
                if(bean.getProtocolSPRevNumber() != null && !bean.getProtocolSPRevNumber().equals("")){
                    ProtocolDetailForm protocolForm = new ProtocolDetailForm(TypeConstants.DISPLAY_MODE,bean.getProtocolSPRevNumber(),CoeusGuiConstants.getMDIForm());
                    protocolForm.showDialogForm();
                }
            }else{
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(VIEW_PROTOCOL));
            }*/
            columnValueEditor.stopCellEditing();
            //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
            int selectedRow = tblSpecialReview.getSelectedRow();
            String spRvDesc = null;
            
            int rowCount =  tblSpecialReview.getRowCount();
            if( selectedRow!= -1 && selectedRow >= 0 && vecSpecialReviewData != null && selectedRow <= rowCount ){
                SpecialReviewFormBean curBean = (SpecialReviewFormBean)vecSpecialReviewData.get( selectedRow );
                if( txtAreaComments.hasFocus() ){
                    curBean = (SpecialReviewFormBean)vecSpecialReviewData.get(  lastSelectedRow );
                    curBean.setComments( txtAreaComments.getText() );
                    return;
                    
                }else{
                    lastSelectedRow = selectedRow;
                }
                spRvDesc = curBean.getSpecialReviewDescription();
            }
            
            //if Special Review Type is Human Subjects then it should display the IRB details in Display mode
            
            if(selectedRow != -1){
                if(spRvDesc.equals(HUMAN_SUBJECTS)){
                    String protocolNo = (String)tblSpecialReview.getValueAt(selectedRow,3);
                    // 4154: Problems in IRB Linking - Start
                    ProtocolInfoBean protocolInfoBean = getProtocolDetails(protocolNo);
                    if(protocolInfoBean != null && protocolInfoBean.getProtocolNumber() != null){
                        if(!protocolNo.trim().equals("")){
                            if(authorizationCheck()){
                                ProtocolDetailForm protocolForm = new ProtocolDetailForm(TypeConstants.DISPLAY_MODE, protocolNo ,CoeusGuiConstants.getMDIForm());
                                protocolForm.showDialogForm();
//                                }
                            }
                        }
                    }
                }else if(spRvDesc.equals(ANIMAL_USAGE)){
                    String protocolNo = (String)tblSpecialReview.getValueAt(selectedRow,3);
                    //Check for the valid protocol, if it is valid then check whether user has the right to view that protocol
                    edu.mit.coeus.iacuc.bean.ProtocolInfoBean protocolInfoBean = validIACUCProtocol(protocolNo);
                    if(protocolInfoBean != null && protocolInfoBean.getProtocolNumber() != null){
                        if(!protocolNo.trim().equals("")){
                            if(authorizationCheckForIacuc()){
                                edu.mit.coeus.iacuc.gui.ProtocolDetailForm protocolForm = new edu.mit.coeus.iacuc.gui.ProtocolDetailForm(TypeConstants.DISPLAY_MODE, protocolNo ,CoeusGuiConstants.getMDIForm());
                                protocolForm.showDialogForm();
                            }
                        }
                    }
                } else{
                    CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey("SpecialReviewForm_exceptionCode.1070"));
                }
                //Bug Fix 2083 End 3
            }
            // 4154: Problems in IRB Linking - End
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        //}
    }

    /**
     * this method checks for user rights.
     */
    private boolean authorizationCheck() {
        /*boolean hasRight = false;
        RequesterBean requester;
        ResponderBean responder;
         
        requester = new RequesterBean();
        Hashtable authorizations = new Hashtable();
         
        AuthorizationBean authorizationBean;
        AuthorizationOperator authorizationOperator;
        String VIEW_PROTOCOL = "VIEW_PROTOCOL";
        String VIEW_ANY_PROTOCOL = "VIEW_ANY_PROTOCOL";
         
        // Determine whether user has right to view a protocol
        authorizationBean = new AuthorizationBean();
        authorizationBean.setFunction(VIEW_PROTOCOL);
        authorizationBean.setFunctionType("RIGHT_ID");
        authorizationBean.setPerson(CoeusGuiConstants.getMDIForm().getUserId());
        authorizationOperator = new AuthorizationOperator(authorizationBean);
        authorizations.put(VIEW_PROTOCOL, authorizationOperator);
         
        // Determine whether user has right to to view any protocol
        authorizationBean = new AuthorizationBean();
        authorizationBean.setFunction(VIEW_ANY_PROTOCOL);
        authorizationBean.setFunctionType("RIGHT_ID");
        authorizationBean.setPerson(CoeusGuiConstants.getMDIForm().getUserId());
        authorizationOperator = new AuthorizationOperator(authorizationBean);
        authorizations.put(VIEW_ANY_PROTOCOL, authorizationOperator);
         
        requester.setAuthorizationOperators(authorizations);
        requester.setIsAuthorizationRequired(true);
         
        AppletServletCommunicator comm = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + AUTH_SERVLET, requester);
         
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            authorizations = responder.getAuthorizationOperators();
        }else{
            CoeusOptionPane.showInfoDialog(responder.getMessage());
        }
         
        viewProtocol = ((Boolean)authorizations.get(VIEW_PROTOCOL)).booleanValue();
        viewAnyProtocol = ((Boolean)authorizations.get(VIEW_ANY_PROTOCOL)).booleanValue();
         
        if(viewProtocol && viewAnyProtocol){
            hasRight = true;
        }else{
            hasRight = false;
        }*/
        
        //Bug Fix 2083 Start 4
        boolean hasRight = false;
        int selectedRow = tblSpecialReview.getSelectedRow();
        if(selectedRow != -1){
            String protocolNo = (String)tblSpecialReview.getValueAt(selectedRow,3);
            
            Vector dataObjects = null;
            
            String PROTOCOL_SERVLET = "/protocolMntServlet";
            
            String connectTo = CoeusGuiConstants.CONNECTION_URL+ PROTOCOL_SERVLET;
            RequesterBean request = new RequesterBean();
            request.setFunctionType('D');
            request.setId(protocolNo);
            AppletServletCommunicator comm
                    = new AppletServletCommunicator(connectTo, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if (response == null) {
                response = new ResponderBean();
                response.setResponseStatus(false);
                response.setMessage(coeusMessageResources.parseMessageKey(
                        "server_exceptionCode.1000"));
            }
            if (response.isSuccessfulResponse()) {
                hasRight = true;
            }else {
                if(response.getDataObject() != null ){
                    Object obj = response.getDataObject();
                    if(obj instanceof CoeusException){
                        CoeusOptionPane.showErrorDialog( ( (CoeusException)obj ).getMessage() );
                    }else{
                        CoeusOptionPane.showErrorDialog( response.getMessage() );
                    }//End of inner else
                }
            }//End of outer else
        }//End of outer if
        //Bug Fix 2083 End 4
        
        return hasRight;
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    public void mousePressed(MouseEvent e) {
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    
    /** this method sets focus back to component
     * @return void
     */
    private void setRequestFocusInSpecialReviewThread(final int selrow , final int selcol){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                tblSpecialReview.requestFocusInWindow();
                tblSpecialReview.changeSelection( selrow, selcol, true, false);
                tblSpecialReview.setRowSelectionInterval(selrow, selrow);
            }
        });
    }
    
    /**
     * Getter for property vecDeletedSpecialreviewCodes.
     * @return Value of property vecDeletedSpecialreviewCodes.
     */
    public java.util.Vector getVecDeletedSpecialreviewCodes() {
        return vecDeletedSpecialreviewCodes;
    }
    
    /**
     * Setter for property vecDeletedSpecialreviewCodes.
     * @param vecDeletedSpecialreviewCodes New value of property vecDeletedSpecialreviewCodes.
     */
    public void setVecDeletedSpecialreviewCodes(java.util.Vector vecDeletedSpecialreviewCodes) {
        this.vecDeletedSpecialreviewCodes = vecDeletedSpecialreviewCodes;
    }
    
    //Coeus Enhancement Case #1799 - end:  step 18
    // Case# 3110: Special review in prop dev linked to protocols - Start
    private ProtocolInfoBean getProtocolDetails(String protocolNumber)throws CoeusException{
        ProtocolInfoBean infoBean = new ProtocolInfoBean();
        if (protocolNumber != null && protocolNumber.trim().length() > 0 && !protocolNumber.equals("")) {
            String connectTo = CoeusGuiConstants.CONNECTION_URL + "/protocolMntServlet";
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType('f');
            requester.setDataObject(protocolNumber);
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requester);
            comm.send();
            ResponderBean response = comm.getResponse();
            if(!response.isSuccessfulResponse()){
                throw new CoeusException(response.getMessage(), 1);
            }else{
                infoBean = (ProtocolInfoBean)response.getDataObject();
            }
        }
        return infoBean;
    }
    // Case# 3110: Special review in prop dev linked to protocols - End
    //Added for COEUSDEV 271:special review not selected when copying from proposal to award
    public void enableDisableButtons() {
        
        if( tblSpecialReview!=null && tblSpecialReview.getRowCount() > ZERO_COUNT ){
            
            tblSpecialReview.setRowSelectionInterval(ZERO_COUNT,ZERO_COUNT);
            SpecialReviewFormBean firstBean =
                    (SpecialReviewFormBean)vecSpecialReviewData.get( ZERO_COUNT );
            
            if(firstBean != null){
                txtAreaComments.setText( firstBean.getComments() );
                txtAreaComments.setCaretPosition(0);
            }
            
        }else{
            // If no Data is there then set the delete button to disable mode.
            txtAreaComments.setEnabled(false);
            btnDelete.setEnabled(false);
            btnFind.setEnabled(false);
        }
    }
    //COEUSDEV 271 End
    //Added for the case# COEUSDEV-312-First Protocol on Proposal not being saved on Award record.-start
    /** This method is used throw the exception with the given message.
     * @param mesg String which will describe the exception
     * @throws Exception with the given custom message.
     */
    private void log(String mesg) throws CoeusUIException{
        CoeusUIException coeusUIException = new CoeusUIException(mesg,CoeusUIException.WARNING_MESSAGE);
        coeusUIException.setTabIndex(0);
        throw coeusUIException;
        
    }
    //Added for the case# COEUSDEV-312-First Protocol on Proposal not being saved on Award record.-End
    
    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
     
    //To get the IACUC protocol details
    private edu.mit.coeus.iacuc.bean.ProtocolInfoBean getIacucProtocolDetails(String protocolNumber)throws CoeusException{
        edu.mit.coeus.iacuc.bean.ProtocolInfoBean infoBean = new edu.mit.coeus.iacuc.bean.ProtocolInfoBean();
        if (protocolNumber != null && protocolNumber.trim().length() > 0 && !protocolNumber.equals("")) {
            String connectTo = CoeusGuiConstants.CONNECTION_URL + "/IacucProtocolServlet";
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType('f');
            requester.setDataObject(protocolNumber);
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requester);
            comm.send();
            ResponderBean response = comm.getResponse();
            if(!response.isSuccessfulResponse()){
                throw new CoeusException(response.getMessage(), 1);
            }else{
                infoBean = (edu.mit.coeus.iacuc.bean.ProtocolInfoBean)response.getDataObject();
            }
        }
        return infoBean;
    }
    
    //Setting the value for the parameter IACUC_SPL_REV_TYPE_CODE
    public void setSpecialRevTypeCodeForIacuc(String code) {
        this.specialRevTypeCodeParamForIacuc=code;
    }
    
    /**
     * Getter for property enableIacucProtocolLink.
     * @return Value of property enableIacucProtocolLink.
     */
    public int getEnableIacucProtocolLink() {
        return enableIacucProtocolLink;
    }
    
    /**
     * Setter for property enableIacucProtocolLink.
     * @param enableIacucProtocolLink New value of property enableIacucProtocolLink.
     */
    public void setEnableIacucProtocolLink(int enableIacucProtocolLink) {
        this.enableIacucProtocolLink = enableIacucProtocolLink;
    }
    
    //This method searches for the IACUC protocol based on the user selection
    private void showIacucProtocolSearch() throws Exception{
        columnValueEditor.stopCellEditing();
        CoeusSearch protocolSearch = new CoeusSearch( CoeusGuiConstants.getMDIForm(),"IACUCPROTOCOLSEARCH",
                CoeusSearch.TWO_TABS) ;
        protocolSearch.showSearchWindow();
        HashMap protocolRow = protocolSearch.getSelectedRow();
        tblSpecialReview.setValueAt(protocolRow.get("PROTOCOL_NUMBER"), selRow,3);
        if(moduleCode.trim().equals("INSTITUTE_PROPOSAL") || moduleCode.trim().equals("Award_Module") ||moduleCode.trim().equals("PROPOSAL") ){
            
            tblSpecialReview.setValueAt(protocolRow.get("PROTOCOL_STATUS_DESCRIPTION"),selRow,2);
            if(protocolRow.get("APPROVAL_DATE") != null){
                tblSpecialReview.setValueAt(protocolRow.get("APPROVAL_DATE"),selRow,5);
            }else{
                tblSpecialReview.setValueAt("",selRow,4);
            }
            if(protocolRow.get("APPLICATION_DATE") != null){
                tblSpecialReview.setValueAt(protocolRow.get("APPLICATION_DATE"),selRow,4);
            }else{
                tblSpecialReview.setValueAt("",selRow,5);
            }
        }
        
        SpecialReviewFormBean bean = (SpecialReviewFormBean)vecSpecialReviewData.get(selRow);
        if(bean.getAcType().equals(TypeConstants.INSERT_RECORD)){
            bean.setPrevSpRevProtocolNumber(bean.getProtocolSPRevNumber());
        }else if(bean.getAcType()==null || bean.getAcType().equals("null")) {
            bean.setAcType(TypeConstants.UPDATE_RECORD);
        }
        String protocolStatusCode = CoeusGuiConstants.EMPTY_STRING;
        if(protocolRow != null){
            protocolStatusCode = protocolRow.get("PROTOCOL_STATUS_CODE")+CoeusGuiConstants.EMPTY_STRING;
        }
        ProtocolSubmissionInfoBean protocolSubmissionInfoBean = null;
        
       /* if(protocolStatusCode != null && protocolStatusCode.equals(EXEMPT_STATUS)){
            String connectTo = CoeusGuiConstants.CONNECTION_URL + "/SubmissionDetailsServlet";
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType('c');
            if(protocolRow != null){
                requester.setId((String)protocolRow.get("PROTOCOL_NUMBER"));
            }
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requester);
            comm.send();
            ResponderBean response = comm.getResponse();
            if(!response.isSuccessfulResponse()){
                throw new CoeusException(response.getMessage(), 1);
            }else{
                
                protocolSubmissionInfoBean = (ProtocolSubmissionInfoBean)response.getDataObject();
            }
        }
        if(protocolSubmissionInfoBean != null && protocolSubmissionInfoBean.getProtocolExemptCheckList() != null
                &&protocolSubmissionInfoBean.getProtocolExemptCheckList().size() > 0){
            Vector vcExemptCheclList = protocolSubmissionInfoBean.getProtocolExemptCheckList();
            StringBuffer checkListForComments = new StringBuffer();
            if(vcExemptCheclList != null && vcExemptCheclList.size() > 0){
                for(int index = 0; index<vcExemptCheclList.size();index++){
                    ProtocolReviewTypeCheckListBean protoExemptCheckListBean =  (ProtocolReviewTypeCheckListBean)vcExemptCheclList.get(index);
                    
                    checkListForComments.append("E");
                    checkListForComments.append(protoExemptCheckListBean.getCheckListCode());
                    if(index != vcExemptCheclList.size()-1){
                        checkListForComments.append(","+EMPTY_SPACE);
                    }
                }
                txtAreaComments.setText(checkListForComments.toString());
                bean.setComments(checkListForComments.toString());
            }
        }*/
        
        bean.setProtocolSPRevNumber((String)protocolRow.get("PROTOCOL_NUMBER"));
        bean.setProtoSequenceNumber(Integer.parseInt(protocolRow.get("SEQUENCE_NUMBER").toString()));
        saveRequired = true;
    }
    
    //Checking the validity for the IACUC protocol
     private edu.mit.coeus.iacuc.bean.ProtocolInfoBean validIACUCProtocol(String protocolNum) throws CoeusException{
            edu.mit.coeus.iacuc.bean.ProtocolInfoBean infoBean = null;
            if (protocolNum != null && protocolNum.trim().length() > 0 && !protocolNum.equals("")) {
                if(protocolNum.indexOf("A") != -1 || protocolNum.indexOf("R") != -1){
                    protocolNum = "A001";
                }
                String connectTo = CoeusGuiConstants.CONNECTION_URL + "/IacucProtocolServlet";
                RequesterBean requester = new RequesterBean();
                requester.setFunctionType('f');
                requester.setDataObject(protocolNum);
                AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requester);
                comm.send();
                ResponderBean response = comm.getResponse();
                if(!response.isSuccessfulResponse()){
                    throw new CoeusException(response.getMessage(), 1);
                }else{
                    infoBean = (edu.mit.coeus.iacuc.bean.ProtocolInfoBean)response.getDataObject();
                }
            }
            return infoBean;
        }
     
     //checking whether user has the right to view the IACUC protocol
     private boolean authorizationCheckForIacuc(){
         boolean hasRight = false;
         int selectedRow = tblSpecialReview.getSelectedRow();
         if(selectedRow != -1){
             String protocolNo = (String)tblSpecialReview.getValueAt(selectedRow,3);
             
             Vector dataObjects = null;
              
             String connectTo = CoeusGuiConstants.CONNECTION_URL+  "/IacucProtocolServlet";
             RequesterBean request = new RequesterBean();
             request.setFunctionType('D');
             request.setId(protocolNo);
             AppletServletCommunicator comm
                     = new AppletServletCommunicator(connectTo, request);
             comm.send();
             ResponderBean response = comm.getResponse();
             if (response == null) {
                 response = new ResponderBean();
                 response.setResponseStatus(false);
                 response.setMessage(coeusMessageResources.parseMessageKey(
                         "server_exceptionCode.1000"));
             }
             if (response.isSuccessfulResponse()) {
                 hasRight = true;
             }else {
                 if(response.getDataObject() != null ){
                     Object obj = response.getDataObject(); 
                     if(obj instanceof CoeusException){
                         CoeusOptionPane.showErrorDialog( ( (CoeusException)obj ).getMessage() );
                     }else{
                         CoeusOptionPane.showErrorDialog( response.getMessage() );
                     }
                 }
             }
         }     
         return hasRight;
     }

    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
}

