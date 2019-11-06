/*
 * InvestigatorUnitAdminTypeController.java
 *
 * Created on September 25, 2006, 4:49 PM
 */

package edu.mit.coeus.utils.investigator.invUnitAdminType;

import edu.mit.coeus.award.bean.AwardBaseBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.departmental.gui.PersonDetailForm;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.instprop.bean.InstituteProposalBean;
import edu.mit.coeus.instprop.bean.InvestigatorUnitAdminTypeBean;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.unit.bean.UnitAdministratorBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.EmptyHeaderRenderer;
import edu.mit.coeus.utils.IconRenderer;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.investigator.invCreditSplit.InvCreditSplitObject;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

/**
 *
 * @author  tarique
 */
public class InvestigatorUnitAdminTypeController extends Controller 
    implements ActionListener{
    private static final int HAND_ICON_COLUMN = 0;
    private static final int ADMIN_TYPE = 1;
    private static final int NAME_COLUMN = 2;
    private static final String EMPTY_STRING = "";
    /**panel background color in disable mode  */    
    private static final Color  PANEL_BACKGROUND_COLOR =
    (Color) UIManager.getDefaults().get("Panel.background");
    private static final int WIDTH = 600;
    private static final int HEIGHT = 360;
    private static final String WINDOW_TITLE = " Unit Administrators for ";
    private static final String AWARD_SERVLET = "/AwardMaintenanceServlet";
    private static final String INST_PROP_SERVLET = "/InstituteProposalMaintenanceServlet";
    
    private static final char GET_PROP_UNIT_ADMIN_TYPE_DATA = 'Q';
    private static final char SAVE_PROP_UNIT_ADMIN_TYPE_DATA = 'W';
    
    private static final char GET_AWARD_UNIT_ADMIN_TYPE_DATA = 'y';
    private static final char SAVE_AWARD_UNIT_ADMIN_TYPE_DATA = 'u';
   
    private InvestigatorUnitAdminTypeForm investigatorUnitAdminTypeForm;
    private InvLeadUnitAdminWindowForm invLeadUnitAdminWindowForm ;
    private char functionType;
     /** return boolean to tell form is modified or not */    
    public boolean modified;
    /** Vector which has admin type code */    
    private CoeusVector cvAdminTypeCode;
    private InvUnitAdminTypeTableModel invUnitAdminTypeTableModel;
    private InvUnitAdminTypeCellRenderer invUnitAdminTypeCellRenderer;
    private InvUnitAdminTypeTableCellEditor invUnitAdminTypeTableCellEditor;
    private CoeusVector cvDeletedData;
    private CoeusVector cvAdminType;
    private CoeusDlgWindow dlgAdminType;
    private String moduleName;
    private String moduleNumber;
    private CoeusMessageResources coeusMessageResources;
    private InvCreditSplitObject invCreditSplitObject;
    private String leadUnit;
    private Object formBaseBean;
    private boolean isSaved;
    //when award is created, is based on an institute proposal
    private String awardProposalNumber;
    /**
     * Creates a new instance of InvestigatorUnitAdminTypeController
     * @param invCreditSplitObject
     * @param functionType
     */
    public InvestigatorUnitAdminTypeController(char functionType, 
            InvCreditSplitObject invCreditSplitObject) {
                isSaved = false;
    this.functionType = functionType;
        this.invCreditSplitObject = invCreditSplitObject;
        this.moduleName = invCreditSplitObject.getModuleName();
        this.moduleNumber = invCreditSplitObject.getModuleNumber();
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
        postInitComponents();
        formatFields();
        setTableKeyTraversal();
    }
    
    public void display() {
        dlgAdminType.setVisible(true);
    }
    
    public void formatFields() {
        if(functionType == TypeConstants.DISPLAY_MODE) {
            investigatorUnitAdminTypeForm.btnAdd.setEnabled(false);
            investigatorUnitAdminTypeForm.btnOk.setEnabled(false);
            investigatorUnitAdminTypeForm.btnSync.setEnabled(false);
            investigatorUnitAdminTypeForm.btnDelete.setEnabled(false);
        }
    }
    
    /**
     *
     * @return
     */    
    public java.awt.Component getControlledUI() {
        return investigatorUnitAdminTypeForm;
    }
    
    /**
     *
     * @return
     */    
    public Object getFormData() {
        return null;
    }
    // supporting class to display PersonDetails on
    // double clicking of any Administrator row.
    /**
     * Class for Mouse Clicked
     */    
    class PersonDisplayAdapter extends MouseAdapter {
        /**
         * Method for mouse clicked
         * @param me Mouse Event
         */        
        public void mouseClicked( MouseEvent me ) {
            int selRow = investigatorUnitAdminTypeForm.tblAdminType.getSelectedRow();
            if ( me.getClickCount() == 2) {
                if(selRow != -1){
                    String personName =
                        (String)investigatorUnitAdminTypeForm.tblAdminType.getModel().getValueAt(selRow,2);
                    InvestigatorUnitAdminTypeBean bean = searchPerson(personName);
                    if(bean != null){
                        String personID = bean.getAdministrator();
                        String loginUserName = CoeusGuiConstants.getMDIForm().getUserName();
                        try{
                            new PersonDetailForm(personID,loginUserName,TypeConstants.DISPLAY_MODE);
                        }catch ( Exception e) {
                            e.printStackTrace();
                            CoeusOptionPane.showInfoDialog( e.getMessage() );
                        }
                    }
                }
            }
        }
    }
    /**
     * Method to search Administrator bean
     * @param personName String
     * @return UnitAdministrator
     **/
    private InvestigatorUnitAdminTypeBean searchPerson(String personName){
        if(cvAdminType != null && personName != null){
            InvestigatorUnitAdminTypeBean vectorBean = null;
            for(int i=0;i< cvAdminType.size();i++){
                vectorBean = (InvestigatorUnitAdminTypeBean)cvAdminType.elementAt(i);
                if(vectorBean.getAdminName().equals(personName.trim())){
                    return vectorBean;
                }
            }
        }
        return null;
    }
    /** This method is used to set the listeners to the components.
     */
    public void registerComponents() {
        cvDeletedData = new CoeusVector();
        investigatorUnitAdminTypeForm = new InvestigatorUnitAdminTypeForm();
        invUnitAdminTypeTableModel = new InvUnitAdminTypeTableModel();
        invUnitAdminTypeCellRenderer = new InvUnitAdminTypeCellRenderer();
        invUnitAdminTypeTableCellEditor = new InvUnitAdminTypeTableCellEditor();
        investigatorUnitAdminTypeForm.tblAdminType.setModel(invUnitAdminTypeTableModel);
        
        /** Code for focus traversal - start */
        java.awt.Component[] components = { investigatorUnitAdminTypeForm.btnOk,
        investigatorUnitAdminTypeForm.btnCancel, investigatorUnitAdminTypeForm.btnAdd
        , investigatorUnitAdminTypeForm.btnDelete, investigatorUnitAdminTypeForm.btnSync
        , investigatorUnitAdminTypeForm.scrPnAdminType
        };
        
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        investigatorUnitAdminTypeForm.setFocusTraversalPolicy(traversePolicy);
        investigatorUnitAdminTypeForm.setFocusCycleRoot(true);
        // Added by Noorul for Enhancement on 11-01-07 starts
        if(moduleName.equals(CoeusGuiConstants.AWARD_MODULE)){
            investigatorUnitAdminTypeForm.tbdPnUnitForm.addTab("Award Administrators", investigatorUnitAdminTypeForm.scrpnAdministrators);
        }else if(moduleName.equals(CoeusGuiConstants.INSTITUTE_PROPOSAL_MODULE)){
            investigatorUnitAdminTypeForm.tbdPnUnitForm.addTab("Proposal Administrators", investigatorUnitAdminTypeForm.scrpnAdministrators);
        }        
        // Added by Noorul for Enhancement on 11-01-07 ends
        /** Code for focus traversal - end */
        
        investigatorUnitAdminTypeForm.btnOk.addActionListener(this);
        investigatorUnitAdminTypeForm.btnCancel.addActionListener(this);
        investigatorUnitAdminTypeForm.btnAdd.addActionListener(this);
        investigatorUnitAdminTypeForm.btnDelete.addActionListener(this);
        investigatorUnitAdminTypeForm.btnSync.addActionListener(this);
        investigatorUnitAdminTypeForm.tblAdminType.addMouseListener( new PersonDisplayAdapter());        
        setTableEditors();
    }
    /**
     *Method for setting table
     */
    private void setTableEditors(){
        investigatorUnitAdminTypeForm.tblAdminType.setRowHeight(22);
        investigatorUnitAdminTypeForm.tblAdminType.setShowHorizontalLines(false);
        investigatorUnitAdminTypeForm.tblAdminType.setShowVerticalLines(false);
        investigatorUnitAdminTypeForm.tblAdminType.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JTableHeader header = investigatorUnitAdminTypeForm.tblAdminType.getTableHeader();
        header.setFont(CoeusFontFactory.getLabelFont());
        header.setReorderingAllowed(false);
        
        investigatorUnitAdminTypeForm.tblAdminType.setOpaque(false);
        investigatorUnitAdminTypeForm.tblAdminType.setSelectionMode(
                        DefaultListSelectionModel.SINGLE_SELECTION);
        
        TableColumn column = investigatorUnitAdminTypeForm.tblAdminType.getColumnModel().getColumn(HAND_ICON_COLUMN);
        column.setMaxWidth(25);
        column.setMinWidth(25);
        column.setPreferredWidth(25);
        column.setCellRenderer(new IconRenderer());
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        
        column = investigatorUnitAdminTypeForm.tblAdminType.getColumnModel().getColumn(1);
        column.setPreferredWidth(170);
        column.setCellRenderer(invUnitAdminTypeCellRenderer);
        column.setCellEditor(invUnitAdminTypeTableCellEditor);
        header.setReorderingAllowed(false);
        
        column = investigatorUnitAdminTypeForm.tblAdminType.getColumnModel().getColumn(2);
        column.setPreferredWidth(175);
        column.setCellRenderer(invUnitAdminTypeCellRenderer);
        header.setReorderingAllowed(false);
    }
    /** This method creates and sets the display attributes for the dialog
     */
    public void postInitComponents(){
        
        dlgAdminType = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm());
        dlgAdminType.setResizable(false);
        dlgAdminType.setModal(true);
        dlgAdminType.getContentPane().add(investigatorUnitAdminTypeForm);
        dlgAdminType.setFont(CoeusFontFactory.getLabelFont());
        dlgAdminType.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAdminType.getSize();
        dlgAdminType.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
         dlgAdminType.addComponentListener(
            new ComponentAdapter(){
                public void componentShown(ComponentEvent e){
                    requestDefaultFocus();
                }
        });
        
        dlgAdminType.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
            }
        });
       
        dlgAdminType.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgAdminType.addWindowListener(new WindowAdapter(){
             public void windowClosing(WindowEvent we){
                 performCancelAction();
             }
        });
    }
    /**
     *
     * @throws CoeusException
     */    
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
        InvestigatorUnitAdminTypeBean bean = checkForDuplicate();
        if(bean != null){
            int index = searchIndex(cvAdminType, bean);
            if(index != -1){
                investigatorUnitAdminTypeForm.tblAdminType.requestFocus();
                investigatorUnitAdminTypeForm.tblAdminType.setRowSelectionInterval(index, index);
            }
            return;
        }
        saveUnitAdminData();
    }
    
    /**
     *
     * @param data
     * @throws CoeusException
     */    
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException{        
        leadUnit = (String)data;
        invLeadUnitAdminWindowForm = new InvLeadUnitAdminWindowForm(leadUnit); 
        // Added by Noorul for Enhancement on 11-01-07 starts
        investigatorUnitAdminTypeForm.tbdPnUnitForm.insertTab("Unit Administrators" , null, invLeadUnitAdminWindowForm , "" ,1);  
        // Added by Noorul for Enhancement on 11-01-07 ends
        if(moduleName.equals(CoeusGuiConstants.AWARD_MODULE)){
            dlgAdminType.setTitle("Award "+WINDOW_TITLE+invCreditSplitObject.getModuleNumber());
        }else if(moduleName.equals(CoeusGuiConstants.INSTITUTE_PROPOSAL_MODULE)){
            dlgAdminType.setTitle("Proposal "+WINDOW_TITLE+invCreditSplitObject.getModuleNumber());
        }
        CoeusVector cvMainData = getUnitAdminDataFromServer();
        cvAdminTypeCode = (CoeusVector)cvMainData.get(0);
        cvAdminType = (CoeusVector)cvMainData.get(1);
        //Added for case 2880 - Problem with proposal administrators being copied to award -start
        //For award module, if there is no administrators is present in the award_administrators 
        //table, take the institute proposal administrators. Set the module number, sequenceno.
        //Set the ac type to INSERT_RECORD
        if(moduleName.equals(CoeusGuiConstants.AWARD_MODULE)){
            boolean awardAdminsPresent = ((Boolean)cvMainData.get(2)).booleanValue();
            if(!awardAdminsPresent){
                if(cvAdminType!=null && cvAdminType.size()>0){
                    InvestigatorUnitAdminTypeBean investigatorUnitAdminTypeBean = null;
                    for(int i=0; i< cvAdminType.size(); i++){
                        investigatorUnitAdminTypeBean = (InvestigatorUnitAdminTypeBean)cvAdminType.get(i);
                        investigatorUnitAdminTypeBean.setModuleNumber(invCreditSplitObject.getModuleNumber());
                        investigatorUnitAdminTypeBean.setSequenceNumber(Integer.parseInt(invCreditSplitObject.getSequenceNo()));
                        investigatorUnitAdminTypeBean.setAcType(TypeConstants.INSERT_RECORD);
                    }
                }
            }
        }
        //Added for case 2880 - Problem with proposal administrators being copied to award - end
        invUnitAdminTypeTableModel.setData(cvAdminType);
        invUnitAdminTypeTableModel.fireTableDataChanged();
        if(investigatorUnitAdminTypeForm.tblAdminType.getRowCount() > 0){
            investigatorUnitAdminTypeForm.tblAdminType.setRowSelectionInterval(0,0);
        }
    }
    private CoeusVector getUnitAdminDataFromServer() throws CoeusException{
        CoeusVector cvData = new CoeusVector();
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        String connectTo = null;
        
        request.setDataObject(moduleNumber);
        if(moduleName.equals(CoeusGuiConstants.AWARD_MODULE)){
            Vector vecData = new Vector();
            vecData.add(0, moduleNumber);
            vecData.add(1, getAwardProposalNumber());
            request.setDataObjects(vecData);
            request.setFunctionType(GET_AWARD_UNIT_ADMIN_TYPE_DATA);
            connectTo = CoeusGuiConstants.CONNECTION_URL + AWARD_SERVLET;
            
        }else if(moduleName.equals(CoeusGuiConstants.INSTITUTE_PROPOSAL_MODULE)){
            
            request.setFunctionType(GET_PROP_UNIT_ADMIN_TYPE_DATA);
            connectTo = CoeusGuiConstants.CONNECTION_URL + INST_PROP_SERVLET;
            
        }
        
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if(response != null){
            if(!response.isSuccessfulResponse()){
                throw new CoeusException(response.getMessage());
            }
            cvData = (CoeusVector)response.getDataObject();
        }else{
            throw new CoeusException(response.getMessage());
        }
        
        return cvData;
    }
    private void saveUnitAdminData() throws CoeusException{
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        String connectTo = null;
        Vector dataToServer = new Vector();
        dataToServer.add(0, getFormBaseBean());
        CoeusVector dataToSave = new CoeusVector();
        if(cvDeletedData != null && !cvDeletedData.isEmpty()) {
            dataToSave.addAll(cvDeletedData);
        }
        if(cvAdminType != null && !cvAdminType.isEmpty()) {
            dataToSave.addAll(cvAdminType);
        }
        dataToServer.add(1, dataToSave);
        request.setDataObjects(dataToServer);
        if(moduleName.equals(CoeusGuiConstants.AWARD_MODULE)){
            request.setFunctionType(SAVE_AWARD_UNIT_ADMIN_TYPE_DATA);
            connectTo = CoeusGuiConstants.CONNECTION_URL + AWARD_SERVLET;
            
        }else if(moduleName.equals(CoeusGuiConstants.INSTITUTE_PROPOSAL_MODULE)){
            request.setFunctionType(SAVE_PROP_UNIT_ADMIN_TYPE_DATA);
            connectTo = CoeusGuiConstants.CONNECTION_URL + INST_PROP_SERVLET;
            
        }
        
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if(response != null){
            if(!response.isSuccessfulResponse()){
                throw new CoeusException(response.getMessage());
            }
        }else{
            throw new CoeusException(response.getMessage());
        }
        isSaved = true;
    }
    /**
     *
     * @throws CoeusUIException
     * @return
     */    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return true;
    }
    private void requestDefaultFocus() {
        
        if(functionType == TypeConstants.DISPLAY_MODE){
            investigatorUnitAdminTypeForm.btnCancel.requestFocusInWindow();
        }else{
            investigatorUnitAdminTypeForm.btnOk.requestFocusInWindow();
        }
    }
    /**
     *
     * @param e
     */    
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Object source = e.getSource();
        try{
            dlgAdminType.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
            if(source.equals(investigatorUnitAdminTypeForm.btnCancel)){
                performCancelAction();
            }else if(source.equals(investigatorUnitAdminTypeForm.btnOk)){
                saveFormData();
                if(isSaved) {
                    dlgAdminType.dispose();
                }
            }else if(source.equals(investigatorUnitAdminTypeForm.btnAdd)){
                performAddAction();
            }else if(source.equals(investigatorUnitAdminTypeForm.btnDelete)){
                performDeleteAction();
            }else if(source.equals(investigatorUnitAdminTypeForm.btnSync)){
                performSyncAction();
            }
        }catch(Exception exception) {
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
        finally{
            dlgAdminType.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }
    
    private void performCancelAction() {
        invUnitAdminTypeTableCellEditor.stopCellEditing ();
        if (modified) {
            int option = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
            CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.SELECTION_CANCEL);
            switch( option ) {
                case (CoeusOptionPane.SELECTION_YES ):
                    try{
                        if(validate()){
                            saveFormData();
                            if(isSaved){
                                dlgAdminType.dispose();
                            }
                        }
                    }catch (CoeusUIException cUiEx){
                        cUiEx.printStackTrace();
                        CoeusOptionPane.showErrorDialog(cUiEx.getMessage());
                    }catch (CoeusException cEx){
                        cEx.printStackTrace();
                        CoeusOptionPane.showErrorDialog(cEx.getMessage());
                    }
                    break;
                case(CoeusOptionPane.SELECTION_NO ):
                   dlgAdminType.dispose();
                   break;
                default:
                   break;
            }
        }else{
            dlgAdminType.dispose();
        }
        
    }
    /** Method for Delete Administrator for the department */
    private void performDeleteAction(){
        int rowCount = investigatorUnitAdminTypeForm.tblAdminType.getRowCount();
        if(rowCount == 0){
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey("adminTypeForm_exceptionCode.1001"));
            return;
        }
        int selRow = investigatorUnitAdminTypeForm.tblAdminType.getSelectedRow();
        if( selRow == -1) {
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey("unitAdmTypeForm_exceptionCode.1004"));
            return;
        }
        String msg = coeusMessageResources.parseMessageKey("generalDelConfirm_exceptionCode.2100");
        int selectedOption = CoeusOptionPane.showQuestionDialog(msg+" row? ",
        CoeusOptionPane.OPTION_YES_NO,
        CoeusOptionPane.DEFAULT_YES);
        if( selectedOption == CoeusOptionPane.SELECTION_YES ){
            InvestigatorUnitAdminTypeBean unitAdministratorBean =
                        (InvestigatorUnitAdminTypeBean)cvAdminType.get(selRow);
            cvAdminType.remove(selRow);
            unitAdministratorBean.setAcType(TypeConstants.DELETE_RECORD);
            cvDeletedData.add(unitAdministratorBean);
            modified = true;
            invUnitAdminTypeTableModel.fireTableDataChanged();
            if(cvAdminType.size() > 0){
                if(selRow-1 != -1){
                    investigatorUnitAdminTypeForm.tblAdminType.setRowSelectionInterval(selRow -1 , selRow -1);
                    investigatorUnitAdminTypeForm.tblAdminType.scrollRectToVisible(
                        investigatorUnitAdminTypeForm.tblAdminType.getCellRect(selRow -1 ,0, true));
                }else{
                    investigatorUnitAdminTypeForm.tblAdminType.setRowSelectionInterval(0 , 0);
                    investigatorUnitAdminTypeForm.tblAdminType.scrollRectToVisible(
                        investigatorUnitAdminTypeForm.tblAdminType.getCellRect(0 ,0, true));
                }
            }
        }
        
    }
    private  void performAddAction() {
        if(cvAdminTypeCode == null || cvAdminTypeCode.isEmpty()){
            String msg = coeusMessageResources.parseMessageKey("unitAdmTypeForm_exceptionCode.1001");
            CoeusOptionPane.showErrorDialog(msg);
            return;
        }
        try{
            invUnitAdminTypeTableCellEditor.stopCellEditing();
            //code for lead unit administrator start
//            InvLeadUnitAdminWindowForm personSearch =
//                                    new InvLeadUnitAdminWindowForm(leadUnit);
//            personSearch.display();
//            if(personSearch.isClicked()) {
//                Vector vSelectedPersons = personSearch.getMultipleSelectedRows();
//                if ( vSelectedPersons != null ){
//                    for(int indx = 0; indx < vSelectedPersons.size(); indx++ ){
//                        edu.mit.coeus.unit.bean.UnitAdministratorBean bean 
//            = (edu.mit.coeus.unit.bean.UnitAdministratorBean)vSelectedPersons.get(indx);
//                        
//                        ComboBoxBean selBean = (ComboBoxBean)cvAdminTypeCode.get(0);
//                        int contactTypeCode = -1;
//                        if(selBean != null){
//                            contactTypeCode = Integer.parseInt(selBean.getCode());
//                        }
//                        InvestigatorUnitAdminTypeBean
//                        unitAdministratorBean = new InvestigatorUnitAdminTypeBean();
//                        unitAdministratorBean.setModuleNumber(invCreditSplitObject.getModuleNumber());
//                        unitAdministratorBean.setSequenceNumber(Integer.parseInt(invCreditSplitObject.getSequenceNo()));
//                        unitAdministratorBean.setUnitAdminType(contactTypeCode);
//                        unitAdministratorBean.setAdministrator(bean.getAdministrator());
//                        unitAdministratorBean.setAdminName(bean.getPersonName());
//                        unitAdministratorBean.setAcType(TypeConstants.INSERT_RECORD);
//                        
//                        cvAdminType.add(unitAdministratorBean);
//                        modified = true;
//                        invUnitAdminTypeTableModel.fireTableDataChanged();
//                        int index = searchIndex(cvAdminType, unitAdministratorBean);
//                        if(index != -1){
//                            investigatorUnitAdminTypeForm.tblAdminType.setRowSelectionInterval(index, index);
//                            investigatorUnitAdminTypeForm.tblAdminType.scrollRectToVisible(
//                            investigatorUnitAdminTypeForm.tblAdminType.getCellRect(index ,0, true));
//                        }
//                    }
//                }
//            }
            //code for lead unit administrator end
            CoeusSearch proposalSearch = null;
                proposalSearch = new CoeusSearch( CoeusGuiConstants.getMDIForm(), 
                    "PERSONSEARCH", CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION );
                proposalSearch.showSearchWindow();
                Vector vSelectedPersons = proposalSearch.getMultipleSelectedRows();
                if ( vSelectedPersons != null ){
                    HashMap singlePersonData = null;
                    for(int indx = 0; indx < vSelectedPersons.size(); indx++ ){
                        
                        singlePersonData = (HashMap)vSelectedPersons.get( indx ) ;
                        
                        if( singlePersonData == null || singlePersonData.isEmpty() ){
                            continue;
                        }
                        String personId = checkForNull(singlePersonData.get( "PERSON_ID" ));
                        String personName = checkForNull(singlePersonData.get( "FULL_NAME" ));
                        
                        ComboBoxBean selBean = (ComboBoxBean)cvAdminTypeCode.get(0);
                        int contactTypeCode = -1;
                        if(selBean != null){
                            contactTypeCode = Integer.parseInt(selBean.getCode());
                        }
                        InvestigatorUnitAdminTypeBean
                        unitAdministratorBean = new InvestigatorUnitAdminTypeBean();
                        unitAdministratorBean.setModuleNumber(invCreditSplitObject.getModuleNumber());
                        unitAdministratorBean.setSequenceNumber(Integer.parseInt(invCreditSplitObject.getSequenceNo()));
                        unitAdministratorBean.setUnitAdminType(contactTypeCode);
                        unitAdministratorBean.setAdministrator(personId);
                        unitAdministratorBean.setAdminName(personName);
                        unitAdministratorBean.setAcType(TypeConstants.INSERT_RECORD);
                        
                        cvAdminType.add(unitAdministratorBean);
                        modified = true;
                        invUnitAdminTypeTableModel.fireTableDataChanged();
                        int index = searchIndex(cvAdminType, unitAdministratorBean);
                        if(index != -1){
                            investigatorUnitAdminTypeForm.tblAdminType.setRowSelectionInterval(index, index);
                            investigatorUnitAdminTypeForm.tblAdminType.scrollRectToVisible(
                            investigatorUnitAdminTypeForm.tblAdminType.getCellRect(index ,0, true));
                        }
                    } //end of for loop
            }
        }catch(Exception ce ){
            ce.printStackTrace();
            CoeusOptionPane.showErrorDialog(ce.getMessage());
        }
        
    }
    private List getLeadAdminData() throws CoeusException{
        CoeusVector cvData = null;
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        final String connectTo = CoeusGuiConstants.CONNECTION_URL +"/unitServlet";
        
        request.setDataObject(leadUnit);
        request.setFunctionType('K');
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if(response!=null){
            if(response.isSuccessfulResponse()){
                CoeusVector cvMainData = (CoeusVector)response.getDataObject();
                cvData = (CoeusVector)cvMainData.get(0);
            }else{
                throw new CoeusException(response.getMessage());
            }
        }else{
            throw new CoeusException(response.getMessage());
        }
        
        return cvData;
    }
    /** Method to perform sync action */
    private void performSyncAction() throws Exception{
        invUnitAdminTypeTableCellEditor.stopCellEditing();
        int option = CoeusOptionPane.showQuestionDialog(
        coeusMessageResources.parseMessageKey("adminTypeForm_exceptionCode.1002"),
                                    CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);
        switch( option ) {
            case (CoeusOptionPane.SELECTION_YES):
                setSyncData();
                modified = true;
                break;
            default:
                break;
                
        }
        
    }
    private void setSyncData() throws Exception{
        int sequenceNumber = 0;
        if(cvAdminType != null && !cvAdminType.isEmpty()) {
            if( moduleName.equals(CoeusGuiConstants.INSTITUTE_PROPOSAL_MODULE) ){
                if(getFormBaseBean() != null) {
                    sequenceNumber = ((InstituteProposalBean)getFormBaseBean()).getSequenceNumber();
                }
            }else if( moduleName.equals(CoeusGuiConstants.AWARD_MODULE) ){
                if(getFormBaseBean() != null) {
                    sequenceNumber = ((AwardBaseBean)getFormBaseBean()).getSequenceNumber();
                }
            }
            if(sequenceNumber > 0) {
                int seqNo = 0;
                for(int index = 0; index < cvAdminType.size(); index ++) {
                    InvestigatorUnitAdminTypeBean bean =
                            (InvestigatorUnitAdminTypeBean)cvAdminType.get(index);
                    seqNo = bean.getSequenceNumber();
                    if( seqNo == sequenceNumber) {
                        if(bean.getAcType() == null 
                            || bean.getAcType().equals(TypeConstants.UPDATE_RECORD )) {
                            bean.setAcType(TypeConstants.DELETE_RECORD);
                            cvDeletedData.add(bean);
                        }
                        
                    }
                    
                }
            }
            
        }
        if(cvAdminType != null) {
            cvAdminType.removeAllElements();
            CoeusVector cvType = (CoeusVector)getLeadAdminData();
            if(cvType != null) {
                InvestigatorUnitAdminTypeBean invBean = null;
                for( int index = 0; index < cvType.size() ; index ++) {
                    UnitAdministratorBean bean
                    = (UnitAdministratorBean)cvType.get(index);
                    invBean = new InvestigatorUnitAdminTypeBean();
                    invBean.setModuleNumber(invCreditSplitObject.getModuleNumber());
                    invBean.setSequenceNumber(Integer.parseInt(invCreditSplitObject.getSequenceNo()));
                    invBean.setAdministrator(bean.getAdministrator());
                    invBean.setAdminName(bean.getPersonName());
                    invBean.setUnitAdminType(bean.getUnitAdminTypeCode());
                    invBean.setAcType(TypeConstants.INSERT_RECORD);
                    cvAdminType.add(invBean);
                }
            }
        }
        invUnitAdminTypeTableModel.setData(cvAdminType);
        invUnitAdminTypeTableModel.fireTableDataChanged();
    }
    public void cleanUp(){
        invCreditSplitObject = null;
        investigatorUnitAdminTypeForm.btnAdd.removeActionListener(this);
        investigatorUnitAdminTypeForm.btnCancel.removeActionListener(this);
        investigatorUnitAdminTypeForm.btnDelete.removeActionListener(this);
        investigatorUnitAdminTypeForm.btnSync.removeActionListener(this);
        investigatorUnitAdminTypeForm.btnOk.removeActionListener(this);
        cvAdminType = null;
        cvAdminTypeCode = null;
        cvDeletedData = null;
        invUnitAdminTypeCellRenderer = null;
        invUnitAdminTypeTableCellEditor = null;
        invUnitAdminTypeTableModel = null;
        investigatorUnitAdminTypeForm = null;
        dlgAdminType = null;
        
    }
     /**
     * Method to check null string
     * @param value string
     * @return String value
     */    
    private String checkForNull( Object value ){
        return ( value==null )? EMPTY_STRING : (String)value;
    }
    /**
     * Method to search index of an object
     * @param coeusVector CoeusVector
     * @param object Object
     * @return int
     **/
    public int searchIndex(CoeusVector coeusVector, Object object){
        if(object != null && coeusVector != null){
            InvestigatorUnitAdminTypeBean bean = (InvestigatorUnitAdminTypeBean)object;
            InvestigatorUnitAdminTypeBean vectorBean = null;
            for(int i=0;i<coeusVector.size();i++){
                vectorBean = (InvestigatorUnitAdminTypeBean)coeusVector.elementAt(i);
                if(vectorBean.getModuleNumber().equals(bean.getModuleNumber()) &&
                vectorBean.getAdministrator().equals(bean.getAdministrator()) &&
                vectorBean.getUnitAdminType() == bean.getUnitAdminType() ){
                    return i;
                }
            }
        }
        return -1;
    }
    /**
     * Method to check For Duplicate administrators
     * @return bean
     **/
    public InvestigatorUnitAdminTypeBean checkForDuplicate(){
        if(cvAdminType!= null && cvAdminType.size() > 0){
            Equals contactType;
            Equals personId;
            for(int index = 0;index < cvAdminType.size();index++){
                InvestigatorUnitAdminTypeBean adminBean = (InvestigatorUnitAdminTypeBean)cvAdminType.get(index);
                contactType =
                    new Equals("unitAdminType",new Integer(adminBean.getUnitAdminType()));
                personId = new Equals("administrator", adminBean.getAdministrator());
                And AwContact = new And(contactType , personId);
                CoeusVector cvFilterd = (CoeusVector)cvAdminType.filter(AwContact);
                if(cvFilterd!= null && cvFilterd.size() > 1) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("unitAdmTypeForm_exceptionCode.1006"));
                    return (InvestigatorUnitAdminTypeBean)cvFilterd.get(0);
                }
            }
            
        }
        return null;
    }
    /**
     * Class for table model
     */    
    class InvUnitAdminTypeTableModel extends AbstractTableModel{
        /** String array for column names*/        
        String colNames[] = {"","Admin Type", "Name"};
        /** Class column Type arrays */        
        Class[] colTypes = new Class [] {Object.class , String.class, String.class};
        CoeusVector cvAdminType;
        
        /**
         * method to set data in table model
         * @param cvAdminType vector object
         */        
        public void setData(CoeusVector cvAdminType) {
            this.cvAdminType = cvAdminType;
        }
        
        /**
         * Method to get value
         * @param rowIndex row
         * @param columnIndex column
         * @return Object
         */        
        public Object getValueAt(int rowIndex, int columnIndex) {
            InvestigatorUnitAdminTypeBean investigatorUnitAdminTypeBean 
                = (InvestigatorUnitAdminTypeBean)cvAdminType.get(rowIndex);
            switch(columnIndex) {
                case HAND_ICON_COLUMN:
                    return EMPTY_STRING;
                case ADMIN_TYPE:
                    int adminTypeCode = investigatorUnitAdminTypeBean.getUnitAdminType();
                    CoeusVector filteredVector = cvAdminTypeCode.filter(
                    new Equals("code", ""+adminTypeCode));
                    if(filteredVector!=null && filteredVector.size() > 0){
                        ComboBoxBean comboBoxBean = (ComboBoxBean)filteredVector.get(0);
                        return comboBoxBean;
                    }else{
                        return new ComboBoxBean("","");
                    }
                case NAME_COLUMN:
                    String administrator = investigatorUnitAdminTypeBean.getAdminName()
                            == null ?EMPTY_STRING:investigatorUnitAdminTypeBean.getAdminName();
                    return administrator;
            }
            return EMPTY_STRING;
            
        }
        /**
         * Method to set value in table model
         * @param value which has to set
         * @param row specify the row
         * @param column specify the column
         */        
        public void setValueAt(Object value, int row, int column){
            if(cvAdminType == null) {
                return;
            }
            InvestigatorUnitAdminTypeBean investigatorUnitAdminTypeBean 
                = (InvestigatorUnitAdminTypeBean)cvAdminType.get(row);
            switch(column){
                case ADMIN_TYPE:
                    if(value==null || value.toString().equals(EMPTY_STRING)) {
                        return ;
                    }
                    ComboBoxBean comboBoxBean = 
                        (ComboBoxBean)cvAdminTypeCode.filter(new Equals("description", value.toString())).get(0);
                    int contactTypeCode = -1;
                    if(comboBoxBean != null){
                        contactTypeCode = Integer.parseInt(comboBoxBean.getCode());
                    }
                    if(investigatorUnitAdminTypeBean != null){
                        if(investigatorUnitAdminTypeBean.getUnitAdminType() != contactTypeCode){
                                investigatorUnitAdminTypeBean.setUnitAdminType(contactTypeCode);
                                if( investigatorUnitAdminTypeBean.getAcType() == null ){
                                    investigatorUnitAdminTypeBean.setAcType(TypeConstants.UPDATE_RECORD);
                                }
                                modified = true;
                        }
                    }
                    break;
            }
        }
        
        /**
         * Method to check cell is editable or not
         * @param row specify the row
         * @param column specify in which column
         * @return boolean
         */        
        public boolean isCellEditable(int row, int column) {
            if(functionType == TypeConstants.DISPLAY_MODE){
                return false;
            }
            boolean type = (column == ADMIN_TYPE) ? true : false;
            return type;
        }
        
        /**
         * Method which count the number of column
         * @return integer value
         */        
        public int getColumnCount() {
            return colNames.length;
        }
        
        /**
         * Method which return column class type
         * @param columnIndex specify which column
         * @return Class
         */        
        public Class getColumnClass(int columnIndex) {
            return colTypes [columnIndex];
        }
        
        /**
         * Method to get column name
         * @param column specify which column
         * @return String
         */        
        public String getColumnName(int column) {
            return colNames[column];
        }
        /**
         * Method which return row count
         * @return integer value for count
         */        
        public int getRowCount() {
            if( cvAdminType == null ) {
                return 0;
            }
            return cvAdminType.size();
            
        }
    }
     /*Renderer for the table columns*/
    /**
     * Class for cell renderer
     */    
    class InvUnitAdminTypeCellRenderer extends DefaultTableCellRenderer{
        /**
         * Constructor for cell renderer
         */        
        public InvUnitAdminTypeCellRenderer() {
            BevelBorder bevelBorder = new BevelBorder(BevelBorder.LOWERED, Color.white,Color.lightGray, Color.black, Color.lightGray);
            setBorder(bevelBorder);
        }
        
        /**
         * Method which return the component
         * @param table object
         * @param value of the cell
         * @param isSelected cell is selected or not
         * @param hasFocus cell has got focus or not
         * @param row of the table
         * @param column of the column
         * @return Component
         */        
        public java.awt.Component getTableCellRendererComponent(JTable table,Object value,
        boolean isSelected, boolean hasFocus, int row, int column){
            
            switch(column) {
                case HAND_ICON_COLUMN:
                    setBackground(PANEL_BACKGROUND_COLOR);
                    return this;
                case ADMIN_TYPE:
                    setText(value.toString());
                    setBackground(Color.WHITE);
                    if( functionType == TypeConstants.DISPLAY_MODE ){
                        setBackground(PANEL_BACKGROUND_COLOR);
                    }
                    return this;
                case NAME_COLUMN:
                    setText(value.toString());
                    setBackground(PANEL_BACKGROUND_COLOR);
                    return this;
                    
            }
            return null;
        }
    }
   
    /** Clas for Table Cell editor */
    class InvUnitAdminTypeTableCellEditor extends AbstractCellEditor implements 
        TableCellEditor, ActionListener{
            /** Combo box object which display in editor */            
        private JComboBox cmbContactType;
        /** boolean value for populate combo box */        
        private boolean populated = false;
        /** column value  */        
        private int column;
        /**
         * Constructor for cell editor
         */        
        InvUnitAdminTypeTableCellEditor() {
            cmbContactType = new JComboBox();
            cmbContactType.addActionListener(this);
            if (functionType == TypeConstants.DISPLAY_MODE){
                cmbContactType.setEditable(false);
            }
        }
        /**
         * Method to populate combo box
         */        
        private void populateCombo() {
            cmbContactType.setModel(new DefaultComboBoxModel(cvAdminTypeCode));
        }
        /**
         * Method to get cell editor component
         * @param table object
         * @param value table value
         * @param isSelected editor is selected or not
         * @param row which row
         * @param column which column for component
         * @return Component object
         */        
        public Component getTableCellEditorComponent(JTable table, Object value, 
                boolean isSelected, int row, int column) {
            ComboBoxBean comboBoxBean = (ComboBoxBean)value;
            this.column = column;
            switch(column) {
                case ADMIN_TYPE:
                    if(! populated) {
                        populateCombo();
                        populated = true;
                    }
                    if(comboBoxBean != null){
                        if(comboBoxBean.getDescription() == null || comboBoxBean.getDescription().equals(EMPTY_STRING)) {
                            ComboBoxBean selBean = (ComboBoxBean)cvAdminTypeCode.get(0);
                            cmbContactType.setSelectedItem(selBean);
                            return cmbContactType;
                        }
                        cmbContactType.setSelectedItem(value);
                    }
                    return cmbContactType;
            }
            return null;
        }
        
        /**
         * Method to get Editor value
         * @return Object
         */        
        public Object getCellEditorValue() {
            this.column = column;
            switch(column) {
                case ADMIN_TYPE:
                    return cmbContactType.getSelectedItem();
            }
            return cmbContactType;
        }
        /**
         * Method to count clik on cell
         * @return total click on cell
         */        
        public int getClickCountToStart(){
            return 1;
        }
        /**
         * Method to stop cell editing
         * @return boolean value
         */        
        public boolean stopCellEditing(){
            return super.stopCellEditing();
        }
        
        /**
         * Method to action performed
         * @param e action event
         */        
        public void actionPerformed(ActionEvent e) {
            stopCellEditing();
        }
        
    }
    /**
     *Method for traverse table to components
     **/
    private void setTableKeyTraversal(){
        javax.swing.InputMap im = investigatorUnitAdminTypeForm.tblAdminType.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
        final Action oldTabAction = investigatorUnitAdminTypeForm.tblAdminType.getActionMap().get(im.get(tab));
        Action tabAction = new AbstractAction() {
            int row = 0;
            int column =0;
            public void actionPerformed(ActionEvent e) {
                oldTabAction.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                boolean selectionOut=false;
                int rowCount = table.getRowCount();
                int columnCount = table.getColumnCount();
                row = table.getSelectedRow();
                column = table.getSelectedColumn();
                if((rowCount-1)==row && column==(columnCount-1)){
                    if(functionType != TypeConstants.DISPLAY_MODE){
                        selectionOut=true;
                        investigatorUnitAdminTypeForm.btnOk.requestFocusInWindow();
                        investigatorUnitAdminTypeForm.tblAdminType.setRowSelectionInterval(0,0);
                        investigatorUnitAdminTypeForm.tblAdminType.scrollRectToVisible(
                            investigatorUnitAdminTypeForm.tblAdminType.getCellRect(0 ,0, true));
                    }
                    else{
                        investigatorUnitAdminTypeForm.btnCancel.requestFocusInWindow();
                    }
                }
                if(rowCount<1){
                    columnCount = 0;
                    row = 0;
                    column=0;
                    investigatorUnitAdminTypeForm.btnOk.requestFocusInWindow();
                    return ;
                }
                while (! table.isCellEditable(row, column) ) {
                    column += 1;
                    if (column == columnCount) {
                        column = 0;
                        row +=1;
                    }
                    if (row == rowCount) {
                        row = 0;
                    }
                    if (row == table.getSelectedRow()
                    && column == table.getSelectedColumn()) {
                        break;
                    }
                }
                if(!selectionOut){
                    table.changeSelection(row, column, false, false);
                }
            }
        };
        investigatorUnitAdminTypeForm.tblAdminType.getActionMap().put(im.get(tab), tabAction);
        final Action oldShiftTabAction = investigatorUnitAdminTypeForm.tblAdminType.getActionMap().get(im.get(shiftTab));
        Action tabShiftAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                oldShiftTabAction.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                int rowCount = table.getRowCount();
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                while (! table.isCellEditable(row, column) ) {
                    column -= 1;
                    if (column <= 0) {
                        column = ADMIN_TYPE;
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
                table.changeSelection(row, column, false, false);
            }
        };
        investigatorUnitAdminTypeForm.tblAdminType.getActionMap().put(im.get(shiftTab), tabShiftAction);
    }
    
    /**
     * Getter for property formBaseBean.
     * @return Value of property formBaseBean.
     */
    public java.lang.Object getFormBaseBean() {
        return formBaseBean;
    }    
   
    /**
     * Setter for property formBaseBean.
     * @param formBaseBean New value of property formBaseBean.
     */
    public void setFormBaseBean(java.lang.Object formBaseBean) {
        this.formBaseBean = formBaseBean;
    }
    
    /**
     * Getter for property awardProposalNumber.
     * @return Value of property awardProposalNumber.
     */
    public java.lang.String getAwardProposalNumber() {
        return awardProposalNumber;
    }
    
    /**
     * Setter for property awardProposalNumber.
     * @param awardProposalNumber New value of property awardProposalNumber.
     */
    public void setAwardProposalNumber(java.lang.String awardProposalNumber) {
        this.awardProposalNumber = awardProposalNumber;
    }
    
}
