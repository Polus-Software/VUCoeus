/*
 * InvestigatorCreditController.java
 *
 * Created on February 20, 2006, 11:36 AM
 */

package edu.mit.coeus.utils.investigator.invCreditSplit;

import edu.mit.coeus.brokers.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.propdev.bean.InvCreditTypeBean;
import edu.mit.coeus.propdev.bean.InvestigatorCreditSplitBean;
import edu.mit.coeus.propdev.bean.ProposalInvestigatorFormBean;
import edu.mit.coeus.propdev.bean.ProposalLeadUnitFormBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DollarCurrencyTextField;
import edu.mit.coeus.utils.FormattedDocument;
import edu.mit.coeus.utils.JTextFieldFilter;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author  ajaygm
 */
public class InvestigatorCreditSplitController extends Controller 
implements ActionListener{
    
    /** Holds an instance of <CODE>CommentsHistoryForm</CODE> */
    private InvestigatorCreditSplitForm invCreditSplitForm;

    /**
     * To create an instance of MDIform
     */  
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    /**
     * Instance of the Dialog
     */ 
    private CoeusDlgWindow dlgInvCreditForm;
    
    /**
     * Instance of Coeus Message Resources
     */   
    private CoeusMessageResources coeusMessageResources;
    
    /*
     * Holds the function type of the dialog
     */
    private char functionType;
    
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
                                                 getDefaults().get("Panel.background");
    
    private static final String EMPTY_STRING = "";
    
    private String moduleName;
    private String moduleNumber;
    private String sequenceNo;
    
    /*Setting the dimentions*/
    private static final String WINDOW_TITLE = "Investigator Credit Split";
    private static final int WIDTH = 830;
    private static final int HEIGHT =  450;
    
    private static final String PROPOSAL_ACTION_SERVLET = "/ProposalActionServlet";
    private static final String AWARD_SERVLET = "/AwardMaintenanceServlet";
    private static final String INST_PROP_SERVLET = "/InstituteProposalMaintenanceServlet";
    
    private static final char GET_PROP_INV_CREDIT_SPLIT_DATA = 'd';
    private static final char SAVE_PROP_INV_CREDIT_SPLIT_DATA = 'e';
    
    private static final char GET_AWARD_INV_CREDIT_SPLIT_DATA = 'w';
    private static final char SAVE_AWARD_INV_CREDIT_SPLIT_DATA = 'x';
    
    private static final char GET_INST_PROP_INV_CREDIT_SPLIT_DATA = 'U';
    private static final char SAVE_INST_PROP_INV_CREDIT_SPLIT_DATA = 'v';
    
    private InvCreditTableModel invCreditTableModel;
    private InvCreditTabelEditor invCreditTabelEditor;
    private InvTableRenderer invTableRenderer;
    private CoeusVector cvColNames;
    private CoeusVector cvInvTableData;
    private CoeusVector cvDataFromServer; 
    private CoeusVector cvInvCreditTypes ; 
    private CoeusVector cvInvMasterData;
    
    private Hashtable invData;
    private Hashtable unitData;
    
    private static final String SAVE_CHANGES = "saveConfirmCode.1002";
    // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
    private QueryEngine queryEngine;
    private String queryKey;
    // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End
            
    /** Creates a new instance of InvestigatorCreditController */
    public InvestigatorCreditSplitController(char functionType) {
        this.functionType = functionType;
        coeusMessageResources = coeusMessageResources.getInstance();
        registerComponents();
        postInitComponents();
        formatFields();
    }
    
    public void display() {
        dlgInvCreditForm.setVisible(true);
    }
    
    public void formatFields() {
        if(functionType == TypeConstants.DISPLAY_MODE){
            invCreditSplitForm.btnOK.setEnabled(false);
        }
    }
    
    public java.awt.Component getControlledUI() {
        return invCreditSplitForm;
    }
    
    public Object getFormData() {
        return null;
    }
    
    /** This method is used to set the listeners to the components.
     */
    public void registerComponents() {
        invCreditSplitForm = new InvestigatorCreditSplitForm();
        
        /** Code for focus traversal - start */
        java.awt.Component[] components = { invCreditSplitForm.btnOK ,
        invCreditSplitForm.btnCancel , invCreditSplitForm.tblDetails
        };
        
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        invCreditSplitForm.setFocusTraversalPolicy(traversePolicy);
        invCreditSplitForm.setFocusCycleRoot(true);
         
        /** Code for focus traversal - end */
        
        invCreditSplitForm.btnOK.addActionListener(this);
        invCreditSplitForm.btnCancel.addActionListener(this);
        
    }
    
     /** This method creates and sets the display attributes for the dialog
     */
    public void postInitComponents(){
        
        dlgInvCreditForm = new CoeusDlgWindow(mdiForm);
        dlgInvCreditForm.setResizable(false);
        dlgInvCreditForm.setModal(true);
        dlgInvCreditForm.getContentPane().add(invCreditSplitForm);
        dlgInvCreditForm.setTitle(WINDOW_TITLE);
        dlgInvCreditForm.setFont(CoeusFontFactory.getLabelFont());
        dlgInvCreditForm.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgInvCreditForm.getSize();
        dlgInvCreditForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
         dlgInvCreditForm.addComponentListener(
            new ComponentAdapter(){
                public void componentShown(ComponentEvent e){
                    requestDefaultFocus();
                }
        });
        
        dlgInvCreditForm.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
            }
        });
       
        dlgInvCreditForm.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgInvCreditForm.addWindowListener(new WindowAdapter(){
             public void windowClosing(WindowEvent we){
                 performCancelAction();
             }
        });
     //code for disposing the window ends
        
    }
    
    /** Method to set the default focus for the component
    */
    public void requestDefaultFocus(){
        invCreditSplitForm.btnCancel.requestFocusInWindow();
        if(cvInvTableData.size() > 1){
            invCreditSplitForm.tblDetails.setRowSelectionInterval(1,1);
        }
    }
    
    private void performCancelAction(){
        invCreditTabelEditor.stopCellEditing ();
        if(isSaveRequired()){
            int option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(SAVE_CHANGES),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            JOptionPane.YES_OPTION);
            switch( option ) {
                case (JOptionPane.YES_OPTION ):
                    try{
                        if(validate()){
                            saveFormData();
                            dlgInvCreditForm.dispose();
                        }
                    }catch (CoeusUIException cUiEx){
                        cUiEx.printStackTrace();
                        CoeusOptionPane.showErrorDialog(cUiEx.getMessage());
                    }catch (CoeusException cEx){
                        cEx.printStackTrace();
                        CoeusOptionPane.showErrorDialog(cEx.getMessage());
                    }
                    break;
                case(JOptionPane.NO_OPTION ):
                   dlgInvCreditForm.dispose();
                    break;
                default:
                    break;
            }
        }else{
            dlgInvCreditForm.dispose();
        }
    }
    
    private void setTableEditors(){
        
        JTableHeader tableHeader = invCreditSplitForm.tblDetails.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());

        invCreditSplitForm.tblDetails.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        invCreditSplitForm.tblDetails.setRowHeight(22);
        invCreditSplitForm.tblDetails.setSelectionBackground(java.awt.Color.yellow);
        invCreditSplitForm.tblDetails.setSelectionForeground(java.awt.Color.white);
        invCreditSplitForm.tblDetails.setShowHorizontalLines(false);
        invCreditSplitForm.tblDetails.setShowVerticalLines(false);
        invCreditSplitForm.tblDetails.setOpaque(true);

        invCreditSplitForm.tblDetails.setSelectionMode(
                                    DefaultListSelectionModel.SINGLE_SELECTION);

            

        TableColumn column;
        for (int i = 0 ; i < cvColNames.size() ; i++){
            
            column = invCreditSplitForm.tblDetails.getColumnModel().getColumn(i);
            if(i == 0){
                column.setPreferredWidth(358);
            }else{
                column.setPreferredWidth(120);
            }
            column.setCellEditor(invCreditTabelEditor);
            column.setCellRenderer(invTableRenderer);
        }
    }
    
    
    private class InvCreditTableModel extends AbstractTableModel{
        
        CoeusVector cvTempData = new CoeusVector();
        
        public String getColumnName(int index) {
           return (String)cvColNames.get(index);
        }
        
        public int getColumnCount() {
            return cvColNames.size();
        }        
        
        public int getRowCount() {
            return cvTempData.size();
        }        
        
        public void setData(CoeusVector cvData){
            cvTempData = cvData;
        }
        
        public Object getValueAt(int row, int column) {
            InvestigatorCreditSplitBean invCreditSplitBean = 
                                (InvestigatorCreditSplitBean)cvTempData.get(row);
            if(column == 0){
                return invCreditSplitBean.getDescription();
            }else{
                /*Equals eqDesc = new Equals("description" , invCreditSplitBean.getDescription());
                Equals eqType = new Equals("invCreditTypeCode" , new Integer(column));
                And eqDescAndeqType = new And(eqDesc , eqType);
                CoeusVector cvFilteredData = cvInvMasterData.filter(eqDescAndeqType);*/
                
                Equals eqDesc = new Equals("description" , invCreditSplitBean.getDescription());
                Equals eqType = new Equals("invCreditTypeCode" , new Integer(column));
                Equals eqPersonId = new Equals("personId" , invCreditSplitBean.getPersonId());
                
                And eqDescAndeqType = new And(eqDesc , eqType);
                
                And filter = new And(eqDescAndeqType , eqPersonId);
                
                CoeusVector cvFilteredData = cvInvMasterData.filter(filter);
                
                if(cvFilteredData.size() > 0){
                    InvestigatorCreditSplitBean bean = 
                        (InvestigatorCreditSplitBean)cvFilteredData.get(0);
                    return bean.getCredit();
                }else{
                    return new Double(0);
                }
                
            }
        }
       
        public void setValueAt(Object obj, int row, int column) {
            InvestigatorCreditSplitBean invCreditSplitBean = 
                                (InvestigatorCreditSplitBean)cvTempData.get(row);
            if(column > 0){
                Equals eqDesc = new Equals("description" , invCreditSplitBean.getDescription());
                Equals eqType = new Equals("invCreditTypeCode" , new Integer(column));
                Equals eqPersonId = new Equals("personId" , invCreditSplitBean.getPersonId());
                
                And eqDescAndeqType = new And(eqDesc , eqType);
                
                And filter = new And(eqDescAndeqType , eqPersonId);
                
                CoeusVector cvFilteredData = cvInvMasterData.filter(filter);
                if(cvFilteredData.size() > 0){
                    InvestigatorCreditSplitBean bean = 
                            (InvestigatorCreditSplitBean)cvFilteredData.get(0);
                    
                    Double value = null;
                    
                    if(obj.toString().trim().equals(EMPTY_STRING)){
                        value  = new Double(0);
                    }else{
                        value = new Double(obj.toString());
                    }
                    if(!value.equals(bean.getCredit())){
                        bean.setCredit(value);
                        
                        if(bean.getAcType() == null){
                            bean.setAcType(TypeConstants.UPDATE_RECORD);
                        }
                        setSaveRequired(true);
                        updateTotalField(bean.getPersonId() , column);
                    }
                }
            }//End of outer if
        }
        
        public boolean isCellEditable(int row, int column) {
            if(functionType == TypeConstants.DISPLAY_MODE){
                return false;
            }else{
                InvestigatorCreditSplitBean  bean = (InvestigatorCreditSplitBean)cvTempData.get(row);
                if(column == 0){
                    return false;
                }else if(bean.getDescription().endsWith("Total")){
                    return false;
                }else{
                    return true;
                }
            }
        }
        
        private void updateTotalField(String personId, int invCreditTypeCode){
            double totalAmt = 0;
            
            //Add up the units' total
            Equals eqPersonId = new Equals("personId" , personId);
            NotEquals neUnitNo = new NotEquals("unitNumber" , null);
            Equals eqType = new Equals("invCreditTypeCode" , new Integer(invCreditTypeCode));

            And eqPersonIdAndneUnitNo = new And(eqPersonId , neUnitNo);
            And filterAnd = new And(eqPersonIdAndneUnitNo , eqType);
            
            CoeusVector cvFilteredData = cvInvMasterData.filter(filterAnd);
            
            if(cvFilteredData.size() > 0){
                for(int i =0 ; i < cvFilteredData.size() ; i++){
                    InvestigatorCreditSplitBean creditBean = 
                            (InvestigatorCreditSplitBean)cvFilteredData.get(i);
                    totalAmt = totalAmt + creditBean.getCredit().doubleValue();
                    totalAmt = ((double)Math.round (totalAmt*Math.pow(10.0, 2) )) / 100;
                }
            }
            
            //Update the unit total to the corresponding bean.
            Equals eqDesc = new Equals("description" , "Unit Total");
            And eqDescAndeqType = new And(eqDesc , eqType);
            And filter = new And(eqDescAndeqType , eqPersonId);
            
            cvFilteredData = cvInvMasterData.filter(filter);
            
            if(cvFilteredData.size() > 0){
                InvestigatorCreditSplitBean bean= (InvestigatorCreditSplitBean)cvFilteredData.get(0);
                bean.setCredit(new Double(totalAmt));
            }
            
            
            //Add up the investigators' total
            Equals eqPerson = new Equals("investigator" , true);
            And eqPerAndeqType = new And(eqPerson , eqType);
            
            cvFilteredData = cvInvMasterData.filter(eqPerAndeqType);
            
            double invTotalAmt = 0;
            if(cvFilteredData.size() > 0){
                for(int i =0 ; i < cvFilteredData.size() ; i++){
                    InvestigatorCreditSplitBean creditBean = 
                            (InvestigatorCreditSplitBean)cvFilteredData.get(i);
                    invTotalAmt = invTotalAmt + creditBean.getCredit().doubleValue();
                    invTotalAmt = ((double)Math.round (invTotalAmt*Math.pow(10.0, 2) )) / 100;
                }
            }
            
            //Update the Investigator total to the corresponding bean.
            eqDesc = new Equals("description" , "Investigator Total");
            eqDescAndeqType = new And(eqDesc , eqType);
            
            cvFilteredData = cvInvMasterData.filter(eqDescAndeqType);
             if(cvFilteredData.size() > 0){
                InvestigatorCreditSplitBean bean= (InvestigatorCreditSplitBean)cvFilteredData.get(0);
                bean.setCredit(new Double(invTotalAmt));
            }
            
            fireTableDataChanged();
        }
    }
    
    private class InvCreditTabelEditor extends AbstractCellEditor implements TableCellEditor{
        private CoeusTextField txtComponent;

        int column;
        
        InvCreditTabelEditor(){
            txtComponent = new CoeusTextField();
            DecimalFormat decimalFormat = (DecimalFormat)NumberFormat.getNumberInstance();
            decimalFormat.setMinimumIntegerDigits(0);
            decimalFormat.setMaximumIntegerDigits(3);

            decimalFormat.setMinimumFractionDigits(2);
            decimalFormat.setMaximumFractionDigits(2);
            decimalFormat.setDecimalSeparatorAlwaysShown(true);

            FormattedDocument formattedDocument = new FormattedDocument(decimalFormat,txtComponent);
            txtComponent.setDocument(formattedDocument);
            txtComponent.setHorizontalAlignment(JTextField.RIGHT);
        }
        
        public Object getCellEditorValue() {
            return txtComponent.getText();
        }
        
        public java.awt.Component getTableCellEditorComponent(javax.swing.JTable jTable, Object value, boolean param, int row, int column) {
            this.column = column;
            if(column > 0){
                if(value == null){
                    txtComponent.setText("0");
                }else{
                    txtComponent.setText(value.toString());
                }
            }
            return txtComponent;
        }
    }
    
    private class InvTableRenderer extends DefaultTableCellRenderer implements TableCellRenderer{
        private CoeusTextField txtComponent;
        private JLabel lblText,lblAmount;
        
        InvTableRenderer(){
            txtComponent = new CoeusTextField();
            txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            lblText = new JLabel();
            lblText.setOpaque(true);
            
        }
        
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
            InvestigatorCreditSplitBean  bean = (InvestigatorCreditSplitBean)cvInvTableData.get(row);
            if(column == 0){
                lblText.setHorizontalAlignment(JLabel.LEFT);
            }else{
                lblText.setHorizontalAlignment(JLabel.RIGHT);
            }
            
            if(functionType == TypeConstants.DISPLAY_MODE ){
                if(bean.isInvestigator() || row == cvInvTableData.size()-1){
                    lblText.setBackground(java.awt.Color.DARK_GRAY);
                    lblText.setForeground(java.awt.Color.WHITE);
                    lblText.setFont(CoeusFontFactory.getLabelFont());
                }else{
                    lblText.setBackground(disabledBackground);
                    lblText.setForeground(java.awt.Color.BLACK);
                    if(bean.getDescription().endsWith("Total")){
                        lblText.setFont(CoeusFontFactory.getLabelFont());
                    }else{
                        lblText.setFont(CoeusFontFactory.getNormalFont());
                    }
                }
            }else if(isSelected){
                if(bean.isInvestigator() || row == cvInvTableData.size()-1){
                    lblText.setFont(CoeusFontFactory.getLabelFont());
                }else{
                    if(bean.getDescription().endsWith("Total")){
                        lblText.setFont(CoeusFontFactory.getLabelFont());
                    }else{
                        lblText.setFont(CoeusFontFactory.getNormalFont());
                    }
                }

                lblText.setBackground(java.awt.Color.YELLOW);
                lblText.setForeground(java.awt.Color.black);
            }else{
                if(bean.isInvestigator() || row == cvInvTableData.size()-1){
                    lblText.setBackground(java.awt.Color.DARK_GRAY);
                    lblText.setForeground(java.awt.Color.WHITE);
                    lblText.setFont(CoeusFontFactory.getLabelFont());
                }else{
                    lblText.setBackground(java.awt.Color.white);
                    lblText.setForeground(java.awt.Color.black);
                    if(bean.getDescription().endsWith("Total")){
                        lblText.setFont(CoeusFontFactory.getLabelFont());
                        if(bean.getDescription().startsWith("Unit")){
                            lblText.setBackground(disabledBackground);
                        }
                    }else{
                        lblText.setFont(CoeusFontFactory.getNormalFont());
                    }
                }
            }

            if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                txtComponent.setText(EMPTY_STRING);
                lblText.setText(txtComponent.getText());
            }else{
                txtComponent.setText(value.toString());
                lblText.setText(txtComponent.getText());
            }
            return lblText;
            
        }//End getTableCellRendererComponent
        
    }
    
    public void saveFormData() throws CoeusException{
        CoeusVector cvDataToServer = updateData();
        
        CoeusVector cvData = new CoeusVector();
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        String connectTo = null;
        
        request.setDataObject(cvDataToServer);
        if(moduleName.equals(CoeusGuiConstants.PROPOSAL_MODULE)){
            
            request.setFunctionType(SAVE_PROP_INV_CREDIT_SPLIT_DATA);
            connectTo = CoeusGuiConstants.CONNECTION_URL + PROPOSAL_ACTION_SERVLET;
        // Commented for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start           
//        }else if(moduleName.equals(CoeusGuiConstants.AWARD_MODULE)){
//            
//            request.setFunctionType(SAVE_AWARD_INV_CREDIT_SPLIT_DATA);
//            connectTo = CoeusGuiConstants.CONNECTION_URL + AWARD_SERVLET;
        // Commented for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End
        }else if(moduleName.equals(CoeusGuiConstants.INSTITUTE_PROPOSAL_MODULE)){
            
            request.setFunctionType(SAVE_INST_PROP_INV_CREDIT_SPLIT_DATA);
            connectTo = CoeusGuiConstants.CONNECTION_URL + INST_PROP_SERVLET;
            
        }
        
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if(response!=null){
            if(!response.isSuccessfulResponse()){
                throw new CoeusException(response.getMessage());
            }
        }else{
            throw new CoeusException(response.getMessage());
        }
    }
    
    private CoeusVector updateData(){
        
        CoeusVector cvData = new CoeusVector();
        CoeusVector cvInvData , cvUnitData;
        
        Equals eqInv = new Equals("investigator" , true);
        NotEquals neUnit = new NotEquals ("unitNumber" , null);
        
        cvInvData = cvInvMasterData.filter(eqInv);
        cvData.add(cvInvData);
        
        cvUnitData = cvInvMasterData.filter(neUnit);
        cvData.add(cvUnitData);
        
        if(moduleName.equals(CoeusGuiConstants.AWARD_MODULE) ||
            moduleName.equals(CoeusGuiConstants.INSTITUTE_PROPOSAL_MODULE)){
                CoeusVector cvProcessSeqData = new CoeusVector();
                cvProcessSeqData.addAll(cvInvData);
                cvProcessSeqData.addAll(cvUnitData);
                processSequenceLogic(cvProcessSeqData);
        }
        
        return cvData;
    }
    
    private void processSequenceLogic(CoeusVector cvProcessSeqData){
        boolean processSeqLogic = false;
        
        Equals eqAcType = new Equals("acType" , ""+TypeConstants.UPDATE_RECORD);
        CoeusVector cvFilteredData = cvProcessSeqData.filter(eqAcType);
        
        if(cvFilteredData.size() > 0){
            InvestigatorCreditSplitBean creditSplitBean = 
                        (InvestigatorCreditSplitBean)cvFilteredData.get(0);
            NotEquals neSeqNo = new NotEquals("sequenceNo" , new Integer(creditSplitBean.getAwSequenceNo()));
            
            cvFilteredData = cvFilteredData.filter(neSeqNo);
            if(cvFilteredData.size() > 0){
                processSeqLogic = true;
            }
        }
        
        if(!processSeqLogic){
            eqAcType = new Equals("acType" , null);
            cvFilteredData = cvProcessSeqData.filter(eqAcType);
            if(cvFilteredData.size() > 0){
                InvestigatorCreditSplitBean creditSplitBean = 
                            (InvestigatorCreditSplitBean)cvFilteredData.get(0);

                eqAcType = new Equals("acType" , ""+TypeConstants.INSERT_RECORD);
                NotEquals neSeqNo = new NotEquals("sequenceNo" , new Integer(creditSplitBean.getAwSequenceNo()));
                And filterAnd = new And(eqAcType , neSeqNo);

                cvFilteredData = cvProcessSeqData.filter(filterAnd);
                if(cvFilteredData.size() > 0){
                    processSeqLogic = true;
                }
            }
        }//End of outer if
     
        if(processSeqLogic){
            for(int i = 0 ; i < cvInvMasterData.size() ; i++){
                InvestigatorCreditSplitBean creditSplitBean = 
                            (InvestigatorCreditSplitBean)cvInvMasterData.get(i);
                creditSplitBean.setAcType(TypeConstants.INSERT_RECORD);
            }
        }//End of if
    }
    
    public void setFormData(Object data) {
        
        try{
            InvCreditSplitObject invCreditSplitObject = (InvCreditSplitObject)data;
            this.moduleName = invCreditSplitObject.getModuleName();
            this.moduleNumber = invCreditSplitObject.getModuleNumber();
            Vector vecData = (Vector)invCreditSplitObject.getInvData();
            setInvData((Hashtable)vecData.get(0));
            setUnitData((Hashtable)vecData.get(1));
            // Modified for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start            
//            cvDataFromServer = getCreditSplitDataFromServer();
            if(moduleName.equals(CoeusGuiConstants.AWARD_MODULE)){
                queryEngine = QueryEngine.getInstance();
                queryKey = invCreditSplitObject.getModuleNumber() + invCreditSplitObject.getSequenceNo();
                cvDataFromServer = new CoeusVector();
                cvDataFromServer.add(queryEngine.getDetails(queryKey,CoeusConstants.INVESTIGATOR_CREDIT_TYPES_KEY));
                cvDataFromServer.add(queryEngine.getDetails(queryKey,CoeusConstants.INVESTIGATOR_CREDIT_SPLIT_KEY));
                cvDataFromServer.add(queryEngine.getDetails(queryKey,CoeusConstants.INVESTIGATOR_UNIT_CREDIT_SPLIT_KEY));
            }else{
                cvDataFromServer = getCreditSplitDataFromServer();
            }
            // Modified for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End
            if(moduleName.equals(CoeusGuiConstants.AWARD_MODULE) || 
               moduleName.equals(CoeusGuiConstants.INSTITUTE_PROPOSAL_MODULE)){
                   sequenceNo = invCreditSplitObject.getSequenceNo();
            }
            
            if(cvDataFromServer != null && cvDataFromServer.size() > 0){
                cvInvCreditTypes = (CoeusVector)cvDataFromServer.get(0);
            }
            
            setColNames(cvInvCreditTypes);
            prepareData();
        }catch (CoeusException cEx){
            cEx.printStackTrace();
            CoeusOptionPane.showErrorDialog(cEx.getMessage());
        }
        
        invCreditTableModel = new InvCreditTableModel();
        invCreditSplitForm.tblDetails.setModel(invCreditTableModel);
        
        invCreditTabelEditor = new InvCreditTabelEditor();        
        invTableRenderer = new InvTableRenderer();
        invCreditTableModel.setData(cvInvTableData);
        setTableEditors();
        
        
        //Update the total fields when the dialog is opened.
        if(cvInvCreditTypes != null && cvInvCreditTypes.size() > 0){
            for(int index = 0 ; index < cvInvCreditTypes.size() ; index ++){
                InvCreditTypeBean invCreditTypeBean = 
                                (InvCreditTypeBean )cvInvCreditTypes.get(index);
                
                
                //Get the persons id. This is used for totaling of his units 
                Equals eqPerson = new Equals("investigator" , true);
                Equals eqTypeCode = new Equals("invCreditTypeCode" , new Integer(invCreditTypeBean.getInvCreditTypeCode()));
                And filter = new And(eqPerson , eqTypeCode);
                
                CoeusVector cvPersons = cvInvMasterData.filter(filter);
                if(cvPersons.size() > 0){
                    for(int j = 0 ; j < cvPersons.size() ; j ++){
                        InvestigatorCreditSplitBean invCreditSplitBean = 
                            (InvestigatorCreditSplitBean)cvPersons.get(j);
                        invCreditTableModel.updateTotalField(
                            invCreditSplitBean.getPersonId() , invCreditTypeBean.getInvCreditTypeCode());
                        
                    }//End of for 
                }//End of if
            }//End of outer for
        }//End of outer if
    }
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        if(cvInvCreditTypes != null && cvInvCreditTypes.size() > 0){
            for(int index = 0 ; index < cvInvCreditTypes.size() ; index ++){
                InvCreditTypeBean invCreditTypeBean = 
                                (InvCreditTypeBean )cvInvCreditTypes.get(index);
                
                if(invCreditTypeBean.isAddsToHundred()){
                    Equals eqDesc = new Equals("description" , "Unit Total");
                    Equals eqType = new Equals("invCreditTypeCode" , new Integer(invCreditTypeBean.getInvCreditTypeCode()));
                    NotEquals neCredit = new NotEquals("credit" , new Double(100));
                    
                    //Validate for unit totals
                    And eqDescAndeqType = new And(eqDesc , eqType);
                    And filterAnd = new And(eqDescAndeqType , neCredit);
                    
                    CoeusVector cvFilData = cvInvMasterData.filter(filterAnd);
                    
                    if(cvFilData.size() > 0){
                        InvestigatorCreditSplitBean invCreditSplitBean = 
                            (InvestigatorCreditSplitBean)cvFilData.get(0);
                        
                        String mesg = "Unit " + invCreditTypeBean.getDescription() + " of "+ invCreditSplitBean.getPersonName() + " does not add up to hundred";
                        CoeusOptionPane.showInfoDialog(mesg);
                        return false;
                        /*double credit = invCreditSplitBean.getCredit().doubleValue();
                        if(credit < 100){
                            String mesg = "Unit " + invCreditSplitBean.getDescription() + " of "+ invCreditSplitBean.getPersonName() + "does not add up to hundred";
                            CoeusOptionPane.showInfoDialog(mesg);
                            return false;
                        }*/
                    }
                    
                    //Validate for Investigator total
                    eqDesc = new Equals("description" , "Investigator Total");
                    eqDescAndeqType = new And(eqDesc , eqType);
                    filterAnd = new And(eqDescAndeqType , neCredit);
                    
                    cvFilData = cvInvMasterData.filter(filterAnd);
                    if(cvFilData.size() > 0){
                        InvestigatorCreditSplitBean invCreditSplitBean = 
                            (InvestigatorCreditSplitBean)cvFilData.get(0);
                        String mesg = invCreditTypeBean.getDescription() + " of investigators does not add up to hundred";
                        CoeusOptionPane.showInfoDialog(mesg);
                        return false;
                        
                        /*double credit = invCreditSplitBean.getCredit().doubleValue();
                        if(credit < 100){
                            String mesg = invCreditSplitBean.getDescription() + " of invesitgators does not add up to hundred";
                            CoeusOptionPane.showInfoDialog(mesg);
                            return false;
                        }*/
                    }
                }//End of outer if
            }
        }//End of if
        
        return true;
    }
 
    /*Method used for the generating the table data
     */
    private void prepareData(){
        cvInvTableData = new CoeusVector();
        
        /*Buid a data vector which is set to the table*/
        Enumeration keys  = invData.keys();
        InvestigatorCreditSplitBean invCreditSplitBean = null;
        while (keys.hasMoreElements()){
            ProposalInvestigatorFormBean investigatorBean =
                (ProposalInvestigatorFormBean)invData.get(keys.nextElement());
            
            //Adding the Investigator
            String strInv = investigatorBean.getPersonName();
            if(investigatorBean.isPrincipleInvestigatorFlag()){
                strInv = strInv + " (PI)";
            }
            
            invCreditSplitBean = setBeanData(investigatorBean, null ,  strInv , 0 , true);
            cvInvTableData.add(invCreditSplitBean);


            //Adding the Units for the corresponding Investigator
            Vector vecUnitData = (Vector)unitData.get(investigatorBean.getPersonId());
            if(vecUnitData != null && vecUnitData.size() > 0){
                for(int index = 0 ; index < vecUnitData.size() ; index++){
                    ProposalLeadUnitFormBean investigatorUnitsBean =  ( ProposalLeadUnitFormBean ) vecUnitData.get( index );
                    String description = investigatorUnitsBean.getUnitNumber()+ " - " + investigatorUnitsBean.getUnitName();
                    invCreditSplitBean = setBeanData(investigatorBean , investigatorUnitsBean.getUnitNumber(), 
                                                     description , index , false);
                    cvInvTableData.add(invCreditSplitBean);
                }
            }//End of if 

            //Adding the unit total 
            invCreditSplitBean = setBeanData(investigatorBean , null , "Unit Total" , 0 , false);
            cvInvTableData.add(invCreditSplitBean);
        }
        
        //The last row will be the investigator total
        invCreditSplitBean = setBeanData(null , null ,  "Investigator Total" , 0 , false);
        cvInvTableData.add(invCreditSplitBean);

        /*Buid a data vector which is actually 
         *used for saving and setting the actual changes of the table data
         */
        createMasterData();
    }
    
    /* Method which builds the data vector 
     * which is actually used of saving and 
     * setting the actual changes of the table data
     */
    private void createMasterData(){
        cvInvMasterData = new CoeusVector();
        
        Enumeration keys  = invData.keys();
        InvestigatorCreditSplitBean invCreditSplitBean = null;
        while (keys.hasMoreElements()){
            ProposalInvestigatorFormBean investigatorBean =
                (ProposalInvestigatorFormBean)invData.get(keys.nextElement());
            
            //Create bean for ecah investigator credit type code
            for(int i = 0; i < cvInvCreditTypes.size() ; i++){
                InvCreditTypeBean invCreditTypeBean = (InvCreditTypeBean)cvInvCreditTypes.get(i);
                int invCreditTypeCode = invCreditTypeBean.getInvCreditTypeCode();
                
                //Adding the Investigator
                String strInv = investigatorBean.getPersonName();
                if(investigatorBean.isPrincipleInvestigatorFlag()){
                    strInv = strInv + " (PI)";
                }
                
                invCreditSplitBean = setBeanData(investigatorBean, null , strInv , invCreditTypeCode , true);
                cvInvMasterData.add(invCreditSplitBean);
                


                //Adding the Units for the corresponding Investigator
                Vector vecUnitData = (Vector)unitData.get(investigatorBean.getPersonId());
                if(vecUnitData != null && vecUnitData.size() > 0){
                    for(int index = 0 ; index < vecUnitData.size() ; index++){
                        ProposalLeadUnitFormBean investigatorUnitsBean =  ( ProposalLeadUnitFormBean ) vecUnitData.get( index );
                        String description = investigatorUnitsBean.getUnitNumber()+ " - " + investigatorUnitsBean.getUnitName();
                        invCreditSplitBean = setBeanData(investigatorBean , investigatorUnitsBean.getUnitNumber(), 
                                                         description , invCreditTypeCode , false);
                        cvInvMasterData.add(invCreditSplitBean);
                    }
                }//End of if 

                //Adding the unit total 
                invCreditSplitBean = setBeanData(investigatorBean , null , "Unit Total" , invCreditTypeCode , false);
                cvInvMasterData.add(invCreditSplitBean);
            }
        }
        
        for(int i = 0; i < cvInvCreditTypes.size() ; i++){
            InvCreditTypeBean invCreditTypeBean = (InvCreditTypeBean)cvInvCreditTypes.get(i);
            int invCreditTypeCode = invCreditTypeBean.getInvCreditTypeCode();
            
            //The last row will the investigator total
            invCreditSplitBean = setBeanData(null , null,  "Investigator Total" , invCreditTypeCode , false);
            cvInvMasterData.add(invCreditSplitBean);
         }
        
        
        //Update the saved data got from db to the master data vector
        CoeusVector cvPropPerData = (CoeusVector)cvDataFromServer.get(1);
        CoeusVector cvPropUnitData = (CoeusVector)cvDataFromServer.get(2);
        
        CoeusVector cvFilData = null;
        if(cvPropPerData != null && cvPropPerData.size() >0){
            for(int index = 0 ; index < cvPropPerData.size() ; index++){
                invCreditSplitBean = (InvestigatorCreditSplitBean)cvPropPerData.get(index);
                Equals eqPersonId = new Equals("personId" , invCreditSplitBean.getPersonId());
                Equals eqInvCreditCode = new Equals("invCreditTypeCode" , new Integer(""+invCreditSplitBean.getInvCreditTypeCode()));
                Equals eqInv = new Equals("investigator" , true);
                
                And eqPerIdAndeqInvCredit = new And(eqPersonId , eqInvCreditCode);
                And filter = new And (eqPerIdAndeqInvCredit ,eqInv);
                
                cvFilData = cvInvMasterData.filter(filter);
                if(cvFilData != null && cvFilData.size() >0){
                    InvestigatorCreditSplitBean bean = 
                                    (InvestigatorCreditSplitBean)cvFilData.get(0);
                    
                    bean.setAcType(invCreditSplitBean.getAcType());
                    bean.setAwSequenceNo(invCreditSplitBean.getAwSequenceNo());
                    bean.setCredit(invCreditSplitBean.getCredit());
                    bean.setUpdateTimestamp(invCreditSplitBean.getUpdateTimestamp());
                    bean.setUpdateUser(invCreditSplitBean.getUpdateUser());
                 }
            }//End of for
        }//End of outer if
        
        if(cvPropUnitData != null && cvPropUnitData.size() >0){
            for(int index = 0 ; index < cvPropUnitData.size() ; index++){
                invCreditSplitBean = (InvestigatorCreditSplitBean)cvPropUnitData.get(index);
                /*Equals eqPersonId = new Equals("personId" , invCreditSplitBean.getPersonId());
                Equals eqInvCreditCode = new Equals("invCreditTypeCode" , new Integer(""+invCreditSplitBean.getInvCreditTypeCode()));
                Equals eqInv = new Equals("investigator" , true);
                
                And eqPerIdAndeqInvCredit = new And(eqPersonId , eqInvCreditCode);
                And filter = new And (eqPerIdAndeqInvCredit ,eqInv);*/
                
                Equals eqPersonId = new Equals("personId" , invCreditSplitBean.getPersonId());
                Equals eqUnitNo = new Equals("unitNumber" , invCreditSplitBean.getUnitNumber());
                And eqPersonIdAndeqUnitNo = new And (eqPersonId , eqUnitNo);
                
                Equals eqInvCreditCode = new Equals("invCreditTypeCode" , new Integer(""+invCreditSplitBean.getInvCreditTypeCode()));
                And filterAnd = new And(eqPersonIdAndeqUnitNo , eqInvCreditCode);
                
                cvFilData = cvInvMasterData.filter(filterAnd);
                if(cvFilData != null && cvFilData.size() >0){
                    InvestigatorCreditSplitBean bean = 
                                    (InvestigatorCreditSplitBean)cvFilData.get(0);
                    bean.setAcType(invCreditSplitBean.getAcType());
                    bean.setAwSequenceNo(invCreditSplitBean.getAwSequenceNo());
                    bean.setCredit(invCreditSplitBean.getCredit());
                    bean.setUpdateTimestamp(invCreditSplitBean.getUpdateTimestamp());
                    bean.setUpdateUser(invCreditSplitBean.getUpdateUser());
                 }
            }//End of for
        }//End of outer if
    }
    
    private void setColNames(CoeusVector cvData){
        cvColNames = new CoeusVector();
        if(cvData == null || cvData.size() == 0){
            CoeusOptionPane.showInfoDialog("There are no entries for Investigator Credit Type in Code Table");
            return ;
        }
        
        String colName = " ";
        cvColNames.add(colName);
        for(int i = 0 ; i < cvData.size() ; i++){
            InvCreditTypeBean invCreditTypeBean = (InvCreditTypeBean)cvData.get(i);
            colName = invCreditTypeBean.getDescription();
            cvColNames.add(colName);
        }
    }
    
    private InvestigatorCreditSplitBean setBeanData(ProposalInvestigatorFormBean investigatorBean, 
            String unitNumber , String desc ,int invTypeCode , boolean isInvestigator){
        InvestigatorCreditSplitBean invCreditSplitBean = new InvestigatorCreditSplitBean();
            
        invCreditSplitBean.setCredit(new Double(0));
        invCreditSplitBean.setModuleNumber(moduleNumber);
        invCreditSplitBean.setInvCreditTypeCode(invTypeCode);
        invCreditSplitBean.setDescription(desc);
        
        if(moduleName.equals(CoeusGuiConstants.AWARD_MODULE) || 
           moduleName.equals(CoeusGuiConstants.INSTITUTE_PROPOSAL_MODULE)){
               invCreditSplitBean.setSequenceNo(Integer.parseInt(sequenceNo));
        }
        
        if(investigatorBean != null){
            invCreditSplitBean.setPersonId(investigatorBean.getPersonId());
            invCreditSplitBean.setPersonName(investigatorBean.getPersonName());
            invCreditSplitBean.setPiFlag(investigatorBean.isPrincipleInvestigatorFlag());
            invCreditSplitBean.setInvestigator(isInvestigator);
            invCreditSplitBean.setAcType(TypeConstants.INSERT_RECORD);
            if(unitNumber != null){
                invCreditSplitBean.setUnitNumber(unitNumber);
            }
        }
        
        return invCreditSplitBean;
    }
    
    /**
     * Getter for property InvData.
     * @return Value of property htInvData.
     */
    public java.util.Hashtable getInvData() {
        return invData;
    }
    
    /**
     * Setter for property InvData.
     * @param InvData New value of property htInvData.
     */
    public void setInvData(java.util.Hashtable invData) {
        this.invData = invData;
    }
    
    /**
     * Getter for property UnitData.
     * @return Value of property UnitData.
     */
    public java.util.Hashtable getUnitData() {
        return unitData;
    }
    
    /**
     * Setter for property UnitData.
     * @param htUnitData New value of property htUnitData.
     */
    public void setUnitData(java.util.Hashtable unitData) {
        this.unitData = unitData;
    }
    
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        try{
            dlgInvCreditForm.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            if(source.equals(invCreditSplitForm.btnOK)){
                invCreditTabelEditor.stopCellEditing();
                 if(validate()){
                     if(isSaveRequired()){
                         // Modified for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
//                         saveFormData();
                         if(moduleName.equals(CoeusGuiConstants.AWARD_MODULE)){
                             CoeusVector cvCreditSplit = updateData();
                             CoeusVector cvInvCreditData = (CoeusVector)cvCreditSplit.get(0);
                             CoeusVector cvUnitCreditData = (CoeusVector)cvCreditSplit.get(1);
                             Hashtable htDataCollection = queryEngine.getDataCollection(queryKey);
                             htDataCollection.put(CoeusConstants.INVESTIGATOR_CREDIT_SPLIT_KEY, cvInvCreditData == null ? new CoeusVector() : cvInvCreditData);
                             htDataCollection.put(CoeusConstants.INVESTIGATOR_UNIT_CREDIT_SPLIT_KEY, cvUnitCreditData == null ? new CoeusVector() : cvUnitCreditData);
                             queryEngine.addDataCollection(queryKey,htDataCollection);
                             
                         }else{
                             saveFormData();
                         }
                         // Modified for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End
                     }
                     dlgInvCreditForm.dispose();
                 }
            }else if(source.equals(invCreditSplitForm.btnCancel)){
                performCancelAction();
            }
        }catch (CoeusUIException cUiEx){
            cUiEx.printStackTrace();
            CoeusOptionPane.showErrorDialog(cUiEx.getMessage());
        }catch (CoeusException cEx){
            cEx.printStackTrace();
            CoeusOptionPane.showErrorDialog(cEx.getMessage());
        }finally{
            dlgInvCreditForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));           
        }
    }
    
    private CoeusVector getCreditSplitDataFromServer() throws CoeusException{
        CoeusVector cvData = new CoeusVector();
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        String connectTo = null;
        
        request.setDataObject(moduleNumber);
        
        
        if(moduleName.equals(CoeusGuiConstants.PROPOSAL_MODULE)){
            
            request.setFunctionType(GET_PROP_INV_CREDIT_SPLIT_DATA);
            connectTo = CoeusGuiConstants.CONNECTION_URL + PROPOSAL_ACTION_SERVLET;
        // Commented for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
//        }else if(moduleName.equals(CoeusGuiConstants.AWARD_MODULE)){
//            
//            request.setFunctionType(GET_AWARD_INV_CREDIT_SPLIT_DATA);
//            connectTo = CoeusGuiConstants.CONNECTION_URL + AWARD_SERVLET;
        // Commented for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End    
        }else if(moduleName.equals(CoeusGuiConstants.INSTITUTE_PROPOSAL_MODULE)){
            
            request.setFunctionType(GET_INST_PROP_INV_CREDIT_SPLIT_DATA);
            connectTo = CoeusGuiConstants.CONNECTION_URL + INST_PROP_SERVLET;
            
        }
        
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if(response!=null){
            if(response.isSuccessfulResponse()){
                cvData = (CoeusVector)response.getDataObject();
            }else{
                throw new CoeusException(response.getMessage());
            }
        }else{
            throw new CoeusException(response.getMessage());
        }
        
        return cvData;
    }
    
    public void cleanUp(){
        invData = null;
        unitData = null;
        
        cvColNames = null;
        cvInvTableData = null;
        cvDataFromServer = null;
        cvInvCreditTypes = null;
        cvInvMasterData = null;

        invCreditTableModel = null;
        invCreditTabelEditor = null;
        invTableRenderer = null;
        
        invCreditSplitForm = null;
        dlgInvCreditForm = null;
    }
}
