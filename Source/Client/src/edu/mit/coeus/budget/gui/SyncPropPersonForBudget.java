/*
 * SyncPropPersonForBudget.java
 *
 * Created on September 8, 2005, 12:25 PM
 */

/* PMD check performed, and commented unused imports and variables on 04-MAY-2011
 * by Maharaja Palanichamy
 */

package edu.mit.coeus.budget.gui;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.budget.bean.AppointmentsBean;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.bean.BudgetPersonSyncBean;
import edu.mit.coeus.budget.bean.BudgetPersonsBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
//import edu.mit.coeus.irb.bean.PersonInfoFormBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusComboBox;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
//import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.Equals;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusListener;
//import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
//import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

/**
 *
 * @author  chandrashekara
 */
public class SyncPropPersonForBudget extends javax.swing.JComponent implements ActionListener,FocusListener{
    private static final String EMPTY_STRING = "";
    private static final int NAME_COLUMN = 0;
    private static final int APPOINTMENT_TYPE_COLUMN = 1;
    private static final int JOB_CODE_COLUMN = 2;
    private CoeusVector cvPersonData;
    private CoeusDlgWindow dlgAppointmentsForPerson;
    private CoeusAppletMDIForm mdiForm;
    //Added for Case 3869 - Save not working for budget person - start
//    private String appointments[]={"9M DURATION","10M DURATION","11M DURATION",
//    "12M DURATION","REG EMPLOYEE","SUM EMPLOYEE","TMP EMPLOYEE"};
    //Added for Case 3869 - Save not working for budget person - end
    private BudgetInfoBean budgetInfoBean ;
    private String personId;
    private SyncPropPersonForBudgetTableModel syncPropPersonForBudgetTableModel;
    private SyncProposalPerForBudgetEditor syncProposalPerForBudgetEditor;
    private SyncPropPerForBudgetPerRenderer syncPropPerForBudgetPerRenderer;
    private static final char GET_APPOINTMENTS_FOR_PERSON = 'D';
    private static final char GET_ALL_APPOINTMENTS_FOR_PERSON = 'm';
    // Added for Coeus 4.3 enhancements
    private static final String GET_PARAMETER_VALUE = "GET_PARAMETER_VALUE";
    
    private static final char UPDATE_PERSONS = 'e';
    private static final int HEIGHT = 210;
    private static final int WIDTH = 470;
    private static final String TITLE = "Sync Budget Persons";
    private CoeusVector cvPersonIds;
    private CoeusVector cvappointmentData;
    private boolean clickedAction;
    private CoeusVector cvValidPersonData;
    private String fullName;
    private boolean cancelClicked = true;
    // Added for Coeus 4.3 enhancement
    private CoeusMessageResources coeusMessageResources;
    //Added for Case 3869 - Save not working for budget person - start
    private Vector vecAppointmentTypes;
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
    //private final String REG_EMPLOYEE = "REG_EMPLOYEE";
    private final String REG_EMPLOYEE = "12M DURATION";
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
    //Added for Case 3869 - Save not working for budget person - end
    //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
    private static final String HIERARCHY_SERVLET = "/ProposalHierarchyServlet";
    private static final char CHECK_VIEW_INSTITUTIONAL_SALARIES_RIGHT = 'U';    
    //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
    private final char GET_APPOINTMENT_TYPE ='1';
    private final char GET_ACTIVE_APPOINTMENT_TYPE ='2';
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
    
    /** Creates new form SyncPropPersonForBudget */
    public SyncPropPersonForBudget(CoeusAppletMDIForm mdiForm,CoeusVector cvPersonIds,
    BudgetInfoBean budgetInfoBean ) throws CoeusException{
        this.mdiForm = mdiForm;
        this.budgetInfoBean  = budgetInfoBean ;
        this.cvPersonIds  =cvPersonIds;
        initComponents();
        registerComponents();
        setPersonData();
        setTableEditors();
        postInitComponents();
        setTableKeyTraversal();
    }
    
    public void setPersonData() throws CoeusException{
        
        CoeusVector cvServerData = getPersonDetails(cvPersonIds);
        //Modified for Case 3869 - Save not working for budget person - start
        //Gets the personInfo and the appointmentTypes from the database
        if(cvServerData != null && cvServerData.size() >=2){
            CoeusVector cvPersonServerData = (CoeusVector)cvServerData.get(0);
            //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
            //vecAppointmentTypes = (Vector)cvServerData.get(1);
            //to set the appointment types data
            fetchAppointmentTypes();
            //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
            syncProposalPerForBudgetEditor.setData();
            cvPersonData = new CoeusVector();
            lblHeader.setText("Select Appointment Type and Jobcode for following persons");
            BudgetPersonSyncBean budgetPersonSyncBean = null;
            CoeusVector cvNullData = null;
            cvappointmentData = new CoeusVector();
            CoeusVector cvMultipleAppointments = new CoeusVector();

            if(cvPersonServerData!= null && cvPersonServerData.size() > 0){
                for(int index = 0; index < cvPersonServerData.size() ; index++){
                    budgetPersonSyncBean = (BudgetPersonSyncBean)cvPersonServerData.get(index);
                    if(budgetPersonSyncBean.getAppointmentType() == null){
                        cvNullData = updateNullData(budgetPersonSyncBean);
                        cvPersonData.addAll(cvNullData);
                    }else{
                        cvMultipleAppointments.addElement(budgetPersonSyncBean);
                    }
                }

                updateMultipleAppointments(cvMultipleAppointments);
            }
            syncPropPersonForBudgetTableModel.setData(cvPersonData);
        }
        //Modified for Case 3869 - Save not working for budget person - end
    }
    /** Update the vector of beans if multiple appoinements exists for the number of
     *persons.
     *@param CoeusVector containing the mulptiple appoinements for the selected 
     *Budget
     */
    private void updateMultipleAppointments(CoeusVector cvMultipleAppointments) throws CoeusException{
       CoeusVector cvFilterPersons = null;
       Equals eqPersonId = null;
       BudgetPersonSyncBean personSyncBean = null;
        if(cvMultipleAppointments!= null && cvMultipleAppointments.size() > 0){
            for(int index = 0; index < cvMultipleAppointments.size() ; index++){
                BudgetPersonSyncBean budgetPersonSyncBean = (BudgetPersonSyncBean)cvMultipleAppointments.get(index);
                eqPersonId = new Equals("personId",budgetPersonSyncBean.getPersonId());
                cvFilterPersons = cvMultipleAppointments.filter(eqPersonId);
                if(personSyncBean!= null && personSyncBean.getPersonId().equals(budgetPersonSyncBean.getPersonId())){
                    continue ;
                }
                if(cvFilterPersons!= null && cvFilterPersons.size() > 1){
                    personSyncBean = (BudgetPersonSyncBean )cvFilterPersons.get(1);
                    AppointmentsForPersonForm appointmentsForPersonForm = new AppointmentsForPersonForm(
                    mdiForm,true,personSyncBean.getPersonId(),personSyncBean.getFullName(),
                    getAppointmentPersons(personSyncBean.getPersonId()));
                    AppointmentsBean appointmentsBean = appointmentsForPersonForm.display();
                    BudgetPersonSyncBean syncBean = new BudgetPersonSyncBean();
                    syncBean.setAcType(TypeConstants.INSERT_RECORD);
                    syncBean.setAppointmentType(appointmentsBean.getAppointmentType());
                    syncBean.setFullName(personSyncBean.getFullName());
                    syncBean.setJobCode(appointmentsBean.getJobCode());
                    syncBean.setPersonId(personSyncBean.getPersonId());
                    //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
                    //syncBean.setSalary(appointmentsBean.getSalary());
                    if(hasRightToViewInstitutionalSalaries(personSyncBean.getPersonId())){
                        syncBean.setSalary(appointmentsBean.getSalary());
                    }else{
                        syncBean.setSalary(0.00);
                    }
                    //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End
                    //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module
                    syncBean.setSalaryAnniversaryDate(personSyncBean.getSalaryAnniversaryDate());
                    cvappointmentData.addElement(syncBean);
                }
            }
        }
    }
    
   
   /** Update if the person doesn't have appoinement type and Job code
    *@param contains the vector of persons which doen't have
    *either AppoinentType or Job code
    *@returns the CoeusVector of persons which has all the details required for 
    *the Budget persons
    */ 
    private CoeusVector updateNullData(BudgetPersonSyncBean budgetPersonSyncBean){
        CoeusVector cvNullData = new CoeusVector();
        BudgetPersonSyncBean syncBean = new BudgetPersonSyncBean();
        syncBean.setPersonId(budgetPersonSyncBean.getPersonId());
        syncBean.setFullName(budgetPersonSyncBean.getFullName());
        if(budgetPersonSyncBean.getJobCode() == null){
            syncBean.setJobCode(EMPTY_STRING);
        }else{
            syncBean.setJobCode(budgetPersonSyncBean.getJobCode());
        }
        syncBean.setSalary(budgetPersonSyncBean.getSalary());
        syncBean.setAcType(TypeConstants.INSERT_RECORD);
        //Added for Case 3869 - Save not working for budget person - start
        //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
        //syncBean.setAppointmentType(REG_EMPLOYEE);
        syncBean.setAppointmentType(setActiveAppointmentType());
        //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
        //Added for Case 3869 - Save not working for budget person - end
        //Include Rolodex in Budget Persons - Enhancement - START - 1
        syncBean.setNonEmployee(budgetPersonSyncBean.isNonEmployee());
        //Include Rolodex in Budget Persons - Enhancement - END - 1
        //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module
        syncBean.setSalaryAnniversaryDate(budgetPersonSyncBean.getSalaryAnniversaryDate());
        cvNullData.addElement(syncBean);
        return cvNullData;
    }
    
    /** Get the Persons Appointment Type. If more than one Appointment Type generate
     *Another form to select. Making a server side call.
     */
    
    public CoeusVector getAppointmentPersons(String personId) throws CoeusException{
        final String BUDGET_PERSONS ="/BudgetMaintenanceServlet";
        final String connectTo = CoeusGuiConstants.CONNECTION_URL+ BUDGET_PERSONS;
        CoeusVector vctAppointments = null;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(GET_APPOINTMENTS_FOR_PERSON);
        requester.setDataObject(personId);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        
        comm.send();
        ResponderBean responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            vctAppointments = (CoeusVector)responder.getDataObject();
            
        }
        return vctAppointments;
    }
    
    
    
    /** register the form components for the initialization and needful action to be
     *performed
     */
    public void registerComponents(){
        //Added for Case 3869 - Save not working for budget person - start
        vecAppointmentTypes = new Vector();
        //Added for Case 3869 - Save not working for budget person - end
        syncPropPersonForBudgetTableModel = new SyncPropPersonForBudgetTableModel();
        syncProposalPerForBudgetEditor = new SyncProposalPerForBudgetEditor();
        syncPropPerForBudgetPerRenderer = new SyncPropPerForBudgetPerRenderer();
        tblPropBudgetSync.setModel(syncPropPersonForBudgetTableModel);
        btnCancel.addActionListener(this);
        btnOk.addActionListener(this);
        btnCancel.addFocusListener(this);
        java.awt.Component[] components = {tblPropBudgetSync,btnOk,btnCancel};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        this.setFocusTraversalPolicy(traversePolicy);
        this.setFocusCycleRoot(true);
        
    }
    /** Get the form data and set it to the DTO for the updation
     */
    public CoeusVector getFormData() throws CoeusException{
        BudgetPersonsBean budgetPersonsBean  = null;
        cvValidPersonData = new CoeusVector();
        /** If OK button is not clicked and has multiple appoinents data
         *then create a new instance and then add  cvappointmentData to the
         *existing
         */
        if(!clickedAction){
            cvPersonData = new CoeusVector();
        }
        cvPersonData.addAll(cvappointmentData);
        if(cvPersonData!= null && cvPersonData.size()> 0){
            for(int index = 0; index < cvPersonData.size(); index++){
                BudgetPersonSyncBean budgetPersonSyncBean = (BudgetPersonSyncBean)cvPersonData.get(index);
                budgetPersonsBean = new BudgetPersonsBean();
                
                Date effectiveDate = budgetInfoBean.getStartDate();
                String jobCode = budgetPersonSyncBean.getJobCode();
                String appoinitmentType = budgetPersonSyncBean.getAppointmentType();
                String proposalNumber = budgetInfoBean.getProposalNumber();
                int versionNumber = budgetInfoBean.getVersionNumber();
                double calculationBase = budgetPersonSyncBean.getSalary();
                // set the values to the bean for updation
                
                budgetPersonsBean.setProposalNumber(proposalNumber);
                budgetPersonsBean.setAw_ProposalNumber(proposalNumber);
                
                budgetPersonsBean.setVersionNumber(versionNumber);
                budgetPersonsBean.setAw_VersionNumber(versionNumber);
                
                budgetPersonsBean.setPersonId(budgetPersonSyncBean.getPersonId());
                budgetPersonsBean.setAw_PersonId(budgetPersonSyncBean.getPersonId());
                
                budgetPersonsBean.setFullName(budgetPersonSyncBean.getFullName());
                //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module -Start
                budgetPersonsBean.setSalaryAnniversaryDate(
                        budgetPersonSyncBean.getSalaryAnniversaryDate());
                budgetPersonsBean.setAw_SalaryAnniversaryDate(
                        budgetPersonSyncBean.getSalaryAnniversaryDate());
                //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module -End
                budgetPersonsBean.setJobCode(jobCode);
                budgetPersonsBean.setAw_JobCode(jobCode);
                
                if(appoinitmentType != null) {
                    budgetPersonsBean.setAppointmentType(appoinitmentType);
                    budgetPersonsBean.setAw_AppointmentType(appoinitmentType);
                }else{
                    budgetPersonsBean.setAw_AppointmentType(EMPTY_STRING);
                    budgetPersonsBean.setAppointmentType(EMPTY_STRING);
                }
                budgetPersonsBean.setEffectiveDate(effectiveDate);
                budgetPersonsBean.setAw_EffectiveDate(effectiveDate);
                
                budgetPersonsBean.setCalculationBase(calculationBase);
                budgetPersonsBean.setAw_CalculationBase(calculationBase);
                budgetPersonsBean.setAcType(TypeConstants.INSERT_RECORD);
                //Include Rolodex in Budget Persons - Enhancement - START - 2
                budgetPersonsBean.setNonEmployee(budgetPersonSyncBean.isNonEmployee());
                budgetPersonsBean.setAw_nonEmployeeFlag(budgetPersonSyncBean.isNonEmployee());
                //Include Rolodex in Budget Persons - Enhancement - END - 2
                cvValidPersonData.add(budgetPersonsBean);
            }
        }
        return cvValidPersonData;
    }
    
    private boolean validateData(){
        syncProposalPerForBudgetEditor.stopCellEditing();
        if(cvPersonData!= null && cvPersonData.size() > 0){
            for(int index=0; index <cvPersonData.size(); index++){
                BudgetPersonSyncBean budgetPersonSyncBean = (BudgetPersonSyncBean)cvPersonData.get(index);
                if(budgetPersonSyncBean.getJobCode()==null || budgetPersonSyncBean.getJobCode().equals(EMPTY_STRING)){
                    tblPropBudgetSync.setRowSelectionInterval(index, index);
                    tblPropBudgetSync.setColumnSelectionInterval(JOB_CODE_COLUMN, JOB_CODE_COLUMN);
                    tblPropBudgetSync.editCellAt(index,JOB_CODE_COLUMN);
                    tblPropBudgetSync.scrollRectToVisible(tblPropBudgetSync.getCellRect(index ,JOB_CODE_COLUMN, true));
                    tblPropBudgetSync.getEditorComponent().requestFocusInWindow();
                    CoeusOptionPane.showInfoDialog("Please enter Job code for "+budgetPersonSyncBean.getFullName()+"  ");
                    return false;
                }
            }
        }
        return true;
    }
    
    
    /** Set the table, table header, column header values for the selected
     *table
     */
    private void setTableEditors(){
        JTableHeader tableHeader = tblPropBudgetSync.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tblPropBudgetSync.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblPropBudgetSync.setRowHeight(22);
        tblPropBudgetSync.setSelectionBackground(java.awt.Color.YELLOW);
        tblPropBudgetSync.setSelectionForeground(java.awt.Color.black);
        tblPropBudgetSync.setShowHorizontalLines(false);
        tblPropBudgetSync.setShowVerticalLines(false);
        tblPropBudgetSync.setOpaque(false);
        tblPropBudgetSync.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        
        
        TableColumn column = tblPropBudgetSync.getColumnModel().getColumn(NAME_COLUMN);
        column.setPreferredWidth(130);
        column.setCellRenderer(syncPropPerForBudgetPerRenderer);
        
        column = tblPropBudgetSync.getColumnModel().getColumn(APPOINTMENT_TYPE_COLUMN);
        column.setPreferredWidth(158);
        column.setCellRenderer(syncPropPerForBudgetPerRenderer);
        column.setCellEditor(syncProposalPerForBudgetEditor);
        
        column= tblPropBudgetSync.getColumnModel().getColumn(JOB_CODE_COLUMN);
        column.setPreferredWidth(75);
        column.setCellRenderer(syncPropPerForBudgetPerRenderer);
        column.setCellEditor(syncProposalPerForBudgetEditor);
        
        
    }
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        try{
            if(source.equals(btnCancel)){
                cancelAction();
                clickedAction = false;
                cancelClicked = false;
            }else if(source.equals(btnOk)){
                savePersonData();
                clickedAction = true;
            }
        }catch (CoeusException exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
    }
    
    private void cancelAction(){
        dlgAppointmentsForPerson.setVisible(false);
    }
    
    /** Save the individual person data after validating the
     *required entries.
     */
    private void savePersonData() throws CoeusException{
        if(validateData()){
            dlgAppointmentsForPerson.setVisible(false);
        }
    }
    
    
    /** Initialize the dialog box and set the necessary properticies to the
     *the dialog box. Add the necessary listener for the dialog box as
     *Coeus standars
     */
    private void postInitComponents(){
        dlgAppointmentsForPerson = new CoeusDlgWindow(mdiForm);
        dlgAppointmentsForPerson.setModal(true);
        dlgAppointmentsForPerson.getContentPane().add(this);
        dlgAppointmentsForPerson.pack();
        dlgAppointmentsForPerson.setResizable(false);
        dlgAppointmentsForPerson.setTitle(TITLE);
        dlgAppointmentsForPerson.setSize(WIDTH,HEIGHT);
        dlgAppointmentsForPerson.setLocation(CoeusDlgWindow.CENTER);
        
        dlgAppointmentsForPerson.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                cancelAction();
                return;
            }
        });
        dlgAppointmentsForPerson.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgAppointmentsForPerson.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                cancelAction();
                return;
            }
        });
        
        dlgAppointmentsForPerson.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
    }
    /** Set the default focus when the form is loaded
     */
    private void setWindowFocus(){
        btnOk.requestFocusInWindow();
        boolean isApptEditable = tblPropBudgetSync.isCellEditable(0,APPOINTMENT_TYPE_COLUMN);
        boolean jobCodeEditable = tblPropBudgetSync.isCellEditable(0,JOB_CODE_COLUMN);
        
        if(tblPropBudgetSync.getRowCount() > 0){
            tblPropBudgetSync.setRowSelectionInterval(0,0);
            
            if(isApptEditable){
                tblPropBudgetSync.setRowSelectionInterval(0,0);
                tblPropBudgetSync.setColumnSelectionInterval(APPOINTMENT_TYPE_COLUMN,APPOINTMENT_TYPE_COLUMN);
                tblPropBudgetSync.editCellAt(0,APPOINTMENT_TYPE_COLUMN);
                tblPropBudgetSync.getEditorComponent().requestFocusInWindow();
            }
            if(!isApptEditable &&jobCodeEditable){
                tblPropBudgetSync.setRowSelectionInterval(0,0);
                tblPropBudgetSync.setColumnSelectionInterval(JOB_CODE_COLUMN,JOB_CODE_COLUMN);
                tblPropBudgetSync.editCellAt(0,JOB_CODE_COLUMN);
                tblPropBudgetSync.getEditorComponent().requestFocusInWindow();
            }
            
        }
        
        
    }
    
    public void display(){
        if(cvPersonData!= null && cvPersonData.size() > 0){
            dlgAppointmentsForPerson.setVisible(true);
        }
    }
    
    /** An Inner class provides model for the table which holds the persons
     *appointment details.
     */
    public class SyncPropPersonForBudgetTableModel extends AbstractTableModel{
        
        private String colNames[] = {"Name", "Appointment Type", "Job Code"};
        private Class colClass[] = {String.class,String.class,String.class};
        
        public String getColumnName(int col){
            return colNames[col];
        }
        public int getColumnCount() {
            return colNames.length;
        }
        
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        public void setData(CoeusVector cvPersonData){
            cvPersonData = cvPersonData;
        }
        
        public int getRowCount() {
            if(cvPersonData== null){
                return 0;
            }else{
                return cvPersonData.size();
            }
        }
        
        public boolean isCellEditable(int row, int col){
            if(col==NAME_COLUMN){
                return false;
            }else{
                return true;
            }
        }
        
        public Object getValueAt(int row, int col){
            BudgetPersonSyncBean budgetPersonSyncBean = (BudgetPersonSyncBean)cvPersonData.get(row);
            switch(col){
                case NAME_COLUMN:
                    return budgetPersonSyncBean.getFullName();
                case APPOINTMENT_TYPE_COLUMN:
                    String appointmentType = budgetPersonSyncBean.getAppointmentType();
                    if(appointmentType== null){
                        appointmentType = EMPTY_STRING;
                    }
                    if(appointmentType.equals(EMPTY_STRING)){
                        //Modified for Case 3869 - Save not working for budget person - start
                        //budgetPersonSyncBean.setAppointmentType(appointments[4]);
                        //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
                        //budgetPersonSyncBean.setAppointmentType(REG_EMPLOYEE);
                        budgetPersonSyncBean.setAppointmentType(setActiveAppointmentType());
                        //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
                        //Modified for Case 3869 - Save not working for budget person - end
                        return budgetPersonSyncBean.getAppointmentType();
                        
                    }else{
                        return budgetPersonSyncBean.getAppointmentType();
                    }
                case JOB_CODE_COLUMN:
                    return budgetPersonSyncBean.getJobCode();
            }
            return EMPTY_STRING;
        }
        
        public void setValueAt(Object value, int row, int col){
            BudgetPersonSyncBean budgetPersonSyncBean = (BudgetPersonSyncBean)cvPersonData.get(row);
            switch(col){
                case APPOINTMENT_TYPE_COLUMN:
                    if(value == null)return;
                    if(budgetPersonSyncBean.getAppointmentType()!= null &&
                    budgetPersonSyncBean.getAppointmentType().equals(value.toString())) break;
                    budgetPersonSyncBean.setAppointmentType(value.toString());
                    break;
                    
                case JOB_CODE_COLUMN:
                    if(value == null)return;
                    if(budgetPersonSyncBean.getJobCode()!= null &&
                    budgetPersonSyncBean.getJobCode().equals(value.toString()))break;
                    budgetPersonSyncBean.setJobCode(value.toString());
                    break;
            }
        }
    }
    
    /** An Inner class , provides editor for the Job code and Appointment Type columns
     *If the appoinent type is null, then it populates the types of
     *appointment
     */
    public class SyncProposalPerForBudgetEditor extends AbstractCellEditor implements TableCellEditor{
        private int column;
        private CoeusTextField txtComponent;
        //Modifed for Case 3869 - Save not working for budget person - start
        //private JComboBox cmbAppointmnetType;
        private CoeusComboBox cmbAppointmnetType;
        private ComboBoxBean comboBoxBean;
        //Modifed for Case 3869 - Save not working for budget person - end
        
        public SyncProposalPerForBudgetEditor(){
            txtComponent = new CoeusTextField();
            //Modifed for Case 3869 - Save not working for budget person - start
            //cmbAppointmnetType = new JComboBox(appointments);
            cmbAppointmnetType = new CoeusComboBox();
            cmbAppointmnetType.setModel(new DefaultComboBoxModel(vecAppointmentTypes));
            comboBoxBean = new ComboBoxBean();
            //Modifed for Case 3869 - Save not working for budget person - end
        }
        
        public java.awt.Component getTableCellEditorComponent(
        javax.swing.JTable table, Object value, boolean isSelected, int row, int column) {
            this.column = column;
            
            switch(column){
                case NAME_COLUMN:
                    txtComponent.setText(value.toString());
                    return txtComponent;
                case JOB_CODE_COLUMN:
                    txtComponent.setDocument(new LimitedPlainDocument(6));
                    txtComponent.setText(value.toString());
                    return txtComponent;
                case APPOINTMENT_TYPE_COLUMN:
                    if(value.toString().equals(EMPTY_STRING)){
                        //Modified for Case 3869 - Save not working for budget person - start
                        //cmbAppointmnetType.setSelectedItem(appointments[4]);
                        comboBoxBean.setDescription(REG_EMPLOYEE);
                        cmbAppointmnetType.setSelectedItem(comboBoxBean);
                        //Modified for Case 3869 - Save not working for budget person - end
                        return cmbAppointmnetType;
                    }else{
                        //Modified for Case 3869 - Save not working for budget person - start
                        //cmbAppointmnetType.setSelectedItem(value.toString());
                        comboBoxBean.setDescription(value.toString());
                        //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
                        cmbAppointmnetType.removeAllItems();
                        Vector vecModifiedVectorData = constructActiveAppointmentTypes(value.toString());
                        cmbAppointmnetType.setModel(new DefaultComboBoxModel(vecModifiedVectorData));
                        //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
                        cmbAppointmnetType.setSelectedItem(new ComboBoxBean("",value.toString()));
                        //Modified for Case 3869 - Save not working for budget person - end
                        return cmbAppointmnetType;
                    }
            }
            return txtComponent;
        }
        
        public Object getCellEditorValue() {
            switch (column) {
                case NAME_COLUMN:
                    return txtComponent.getText();
                case JOB_CODE_COLUMN:
                    return txtComponent.getText();
                case APPOINTMENT_TYPE_COLUMN:
                    return cmbAppointmnetType.getSelectedItem();
            }
            return ((CoeusTextField)txtComponent).getText();
        }
        //Added for Case 3869 - Save not working for budget person - start
        public void setData(){
            cmbAppointmnetType.setModel(new DefaultComboBoxModel(vecAppointmentTypes));
        }
        //Added for Case 3869 - Save not working for budget person - end
        
    }// End of SyncProposalPerForBudgetEditor
    
    
    /** An Inner class of the BudgetPersonController. Specifies the renderer for the
     * table component
     */
    private class SyncPropPerForBudgetPerRenderer extends DefaultTableCellRenderer{
        private CoeusTextField txtComponent;
        /** */
        SyncPropPerForBudgetPerRenderer(){
            txtComponent = new CoeusTextField();
            txtComponent.setBorder(new EmptyBorder(0,0,0,0));
        }
        /** Overridden method of the rendrer
         * @param table
         * @param value
         * @param isSelected
         * @param hasFocus
         * @param row
         * @param column
         * @return
         */
        public Component getTableCellRendererComponent(JTable table,Object value,
        boolean isSelected, boolean hasFocus,int row,int column){
            // Added for Coeus 4.3 enhancement
            value = (value==null)?EMPTY_STRING:value;
            BudgetPersonSyncBean budgetPersonSyncBean = (BudgetPersonSyncBean)cvPersonData.get(row);
            switch(column){
                
                case NAME_COLUMN:
                    if(isSelected){
                        txtComponent.setBackground(java.awt.Color.YELLOW);
                        txtComponent.setForeground(java.awt.Color.black);
                    }
                    else {
                        txtComponent.setBackground(java.awt.Color.white);
                        txtComponent.setForeground(java.awt.Color.black);
                    }
                    
                    txtComponent.setText(value.toString());
                    return txtComponent;
                    
                case JOB_CODE_COLUMN:
                    if(isSelected){
                        txtComponent.setBackground(java.awt.Color.YELLOW);
                        txtComponent.setForeground(java.awt.Color.black);
                    }
                    else {
                        txtComponent.setBackground(java.awt.Color.white);
                        txtComponent.setForeground(java.awt.Color.black);
                    }
                    // Added for Coeus 4.3 enhancement - starts
                    if(value.equals(EMPTY_STRING)){
                        value = getDefaultJobCode();
                        value = (value==null)?EMPTY_STRING:value;
                    }
                    budgetPersonSyncBean.setJobCode(value.toString());
                    // Added for Coeus 4.3 enhancement - ends                    
                    
                    txtComponent.setText(value.toString());
                    return txtComponent;
                case APPOINTMENT_TYPE_COLUMN:
                    if(isSelected){
                        txtComponent.setBackground(java.awt.Color.YELLOW);
                        txtComponent.setForeground(java.awt.Color.black);
                    }
                    else {
                        txtComponent.setBackground(java.awt.Color.white);
                        txtComponent.setForeground(java.awt.Color.black);
                    }

                    txtComponent.setText(value.toString());
                    return txtComponent;
            }
            return super.getTableCellRendererComponent(table, value,
            isSelected, hasFocus, row, column);
        }
    }
    
    
    public CoeusVector getPersonDetails(CoeusVector cvPersonIds) throws CoeusException{
        final String BUDGET_PERSONS ="/BudgetMaintenanceServlet";
        final String connectTo = CoeusGuiConstants.CONNECTION_URL+ BUDGET_PERSONS;
        CoeusVector vctAppointments = null;
        RequesterBean requester = new RequesterBean();
        ResponderBean responder = null;
        requester.setFunctionType(GET_ALL_APPOINTMENTS_FOR_PERSON);
         //Added for Case id 3155 unable to create budget for multiple person entries - start
        Vector serverObjects = new Vector();
        serverObjects.add(0, budgetInfoBean.getProposalNumber());
        serverObjects.add(1, cvPersonIds);
        requester.setDataObjects(serverObjects);
        //Added for Case id 3155 unable to create budget for multiple person entries- end
        
        //Commented for Case id 3155 - unable to create budget for multiple person entries
        //requester.setDataObjects(cvPersonIds);
               
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            vctAppointments = (CoeusVector)responder.getDataObjects();
            
        }
        return vctAppointments;
    }
    
    
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnPropBudgetSync = new javax.swing.JScrollPane();
        tblPropBudgetSync = new javax.swing.JTable(){
            public void changeSelection(int row, int column, boolean toggle, boolean extend){
                super.changeSelection(row, column, toggle, extend);
                javax.swing.SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        tblPropBudgetSync.dispatchEvent(new java.awt.event.KeyEvent(
                            tblPropBudgetSync,java.awt.event.KeyEvent.KEY_PRESSED,0,0,java.awt.event.KeyEvent.VK_F2,
                            java.awt.event.KeyEvent.CHAR_UNDEFINED) );
                    }
                });
            }
        };
        pnlButtons = new javax.swing.JPanel();
        btnCancel = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        lblHeader = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        scrPnPropBudgetSync.setBorder(new javax.swing.border.EtchedBorder());
        scrPnPropBudgetSync.setMinimumSize(new java.awt.Dimension(380, 150));
        scrPnPropBudgetSync.setPreferredSize(new java.awt.Dimension(390, 150));
        tblPropBudgetSync.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblPropBudgetSync.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPropBudgetSyncMouseClicked(evt);
            }
        });

        scrPnPropBudgetSync.setViewportView(tblPropBudgetSync);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(scrPnPropBudgetSync, gridBagConstraints);

        pnlButtons.setLayout(new java.awt.GridBagLayout());

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Close");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        pnlButtons.add(btnCancel, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(61, 23));
        btnOk.setMinimumSize(new java.awt.Dimension(60, 23));
        btnOk.setPreferredSize(new java.awt.Dimension(60, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        pnlButtons.add(btnOk, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 0);
        add(pnlButtons, gridBagConstraints);

        lblHeader.setFont(CoeusFontFactory.getLabelFont());
        lblHeader.setText("jLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(lblHeader, gridBagConstraints);

    }//GEN-END:initComponents
    
    private void tblPropBudgetSyncMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPropBudgetSyncMouseClicked
        // TODO add your handling code here:
        javax.swing.SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                tblPropBudgetSync.dispatchEvent(new java.awt.event.KeyEvent(
                tblPropBudgetSync,java.awt.event.KeyEvent.KEY_PRESSED,0,0,java.awt.event.KeyEvent.VK_F2,
                java.awt.event.KeyEvent.CHAR_UNDEFINED) );
            }
        });
    }//GEN-LAST:event_tblPropBudgetSyncMouseClicked
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOk;
    public javax.swing.JLabel lblHeader;
    public javax.swing.JPanel pnlButtons;
    public javax.swing.JScrollPane scrPnPropBudgetSync;
    public javax.swing.JTable tblPropBudgetSync;
    // End of variables declaration//GEN-END:variables
    
    
    
    // This method will provide the key travrsal for the table cells
    // It specifies the tab and shift tab order.
    public void setTableKeyTraversal(){
        
        javax.swing.InputMap im = tblPropBudgetSync.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        // Have the enter key work the same as the tab key
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
        
        // Override the default tab behaviour
        // Tab to the next editable cell. When no editable cells goto next cell.
        final Action oldTabAction = tblPropBudgetSync.getActionMap().get(im.get(tab));
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
                    if(row==0 && column==1){
                        btnOk.requestFocusInWindow();
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
        tblPropBudgetSync.getActionMap().put(im.get(tab), tabAction);
        
        
        // for the shift+tab action, Override the default tab behaviour
        // Tab to the previous editable cell. When no editable cells goto next cell.
        
        final Action oldTabAction1 = tblPropBudgetSync.getActionMap().get(im.get(shiftTab));
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
        tblPropBudgetSync.getActionMap().put(im.get(shiftTab), tabAction1);
        
        
    }
    
    public void focusGained(java.awt.event.FocusEvent e) {
    }
    
    public void focusLost(java.awt.event.FocusEvent e) {
        int selRow = tblPropBudgetSync.getSelectedRow();
        boolean isApEditable = tblPropBudgetSync.isCellEditable(selRow, APPOINTMENT_TYPE_COLUMN);
        boolean isJobEditable = tblPropBudgetSync.isCellEditable(selRow, JOB_CODE_COLUMN);
        if(e.getSource().equals(btnCancel)){
            if(isApEditable && cancelClicked){
                if(selRow!= -1){
                    tblPropBudgetSync.setRowSelectionInterval(selRow,selRow);
                    tblPropBudgetSync.setColumnSelectionInterval(APPOINTMENT_TYPE_COLUMN,APPOINTMENT_TYPE_COLUMN);
                    tblPropBudgetSync.editCellAt(selRow,APPOINTMENT_TYPE_COLUMN);
                }
                return ;
            }
            if(!isApEditable  && isJobEditable){
                tblPropBudgetSync.setRowSelectionInterval(selRow,selRow);
                tblPropBudgetSync.setColumnSelectionInterval(JOB_CODE_COLUMN,JOB_CODE_COLUMN);
                tblPropBudgetSync.editCellAt(selRow,JOB_CODE_COLUMN);
                return ;
            }else{
                btnOk.requestFocusInWindow();
            }
        }
    }
    
    /**
     * To get default job code from the parameter table for given parameter(job code).
     * @return String
     */    
    public String getDefaultJobCode(){
        final String connectTo = CoeusGuiConstants.CONNECTION_URL+ "/coeusFunctionsServlet";
        final String PARAMETER = "DEFAULT_JOB_CODE";
        String value = EMPTY_STRING;
        CoeusVector vctAppointments = null;
        RequesterBean requester = new RequesterBean();
        ResponderBean responder = null;
        requester.setDataObject(GET_PARAMETER_VALUE);
        Vector vecParameter = new Vector();
        vecParameter.add(PARAMETER);
        requester.setDataObjects(vecParameter);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            value =(String) responder.getDataObject();
        }
        return value;
    }
    
    //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
     /**
     *     
     * To check if user has the rights to view institutional salaries
     * @return boolean value for right     
     * @throws CoeusException if exception
     */
    protected boolean hasRightToViewInstitutionalSalaries(String appointmentPersonId) throws CoeusException{
        String connectTo = CoeusGuiConstants.CONNECTION_URL + HIERARCHY_SERVLET;
        RequesterBean request = new RequesterBean();
        Boolean hasRight = null;
        CoeusVector cvDataToServer = new CoeusVector();
        //To set the proposal number, function type
        cvDataToServer.add(budgetInfoBean.getProposalNumber());       
        cvDataToServer.add(false);
        cvDataToServer.add(budgetInfoBean);
        cvDataToServer.add(appointmentPersonId);
        request.setDataObject(cvDataToServer);
        request.setFunctionType(CHECK_VIEW_INSTITUTIONAL_SALARIES_RIGHT);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!= null && response.isSuccessfulResponse()){
            hasRight = (Boolean)response.getDataObject();
        }else {
            throw new CoeusException(response.getMessage());
        }
        return hasRight;
    }
    //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End
    
    //Added for Case 3869 - Save not working for budget person - start
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
    /**
     * Fetch all the appointment types from the database
     */
    public void fetchAppointmentTypes(){
        String connectTo = CoeusGuiConstants.CONNECTION_URL+ "/BudgetMaintenanceServlet";
        RequesterBean requester = new RequesterBean();
        ResponderBean responder = null;
        requester.setFunctionType(GET_APPOINTMENT_TYPE);
               
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requester);        
        comm.send();
        responder = comm.getResponse();
        try{
            if(responder.hasResponse()){
                HashMap hmAppData = (HashMap)responder.getDataObject();
                vecAppointmentTypes = constructAppointmentTypeVector(hmAppData);
                if(vecAppointmentTypes == null){
                    vecAppointmentTypes = new Vector();
                }
            }
        }catch(CoeusException e){
            vecAppointmentTypes = new Vector();
            e.printStackTrace();
        }
    }
    
   /**
     * Fetch all the appointment types from the database
     * @return Vector vecActiveData
     */
    public Vector fetchActiveAppointmentTypes(){
        String connectTo = CoeusGuiConstants.CONNECTION_URL+ "/BudgetMaintenanceServlet";
        RequesterBean requester = new RequesterBean();
        ResponderBean responder = null;
        Vector vecActiveData = null;
        requester.setFunctionType(GET_ACTIVE_APPOINTMENT_TYPE);
               
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requester);        
        comm.send();
        responder = comm.getResponse();
        try{
            if(responder.hasResponse()){
                HashMap hmAppData = (HashMap)responder.getDataObject();
                vecActiveData = constructAppointmentTypeVector(hmAppData);
                if(vecActiveData == null){
                    vecActiveData = new Vector();
                }
            }
        }catch(CoeusException e){
            vecActiveData = new Vector();
            e.printStackTrace();
        }
        return vecActiveData;
    }
    
    /**
     * to create the appointment types data with inactive data
     * @param HashMap hmData
     * @return Vector vecApptData
     */
    public Vector constructAppointmentTypeVector(HashMap hmData){
        Vector vecApptData = new Vector();
        Set<Map.Entry<String, String>> setData = hmData.entrySet();
        for(Map.Entry<String,String> mapData : setData){
            ComboBoxBean cmbBean = new ComboBoxBean();
            cmbBean.setCode(mapData.getKey());
            cmbBean.setDescription(mapData.getKey());
            vecApptData.add(cmbBean);
        }
        return vecApptData;
    }
    
    /**
     * to create the appointment types data with inactive data
     * @param String selectedValue
     * @return Vector modifiedActiveAppointmentTypes
     */
    private Vector constructActiveAppointmentTypes(String selectedValue){
        Vector activeAppointmentTypes = null;
        Vector modifiedActiveAppointmentTypes = null;
        String selectedCode = "";
        boolean entryFlag = true;
        //to fetch the active appointment types
        activeAppointmentTypes = fetchActiveAppointmentTypes();
        //to fetch all the active appointment types
        fetchAppointmentTypes();
        for(Object vecApptData : vecAppointmentTypes) {
            ComboBoxBean cmbBean = (ComboBoxBean)vecApptData;
            if(selectedValue.equalsIgnoreCase(cmbBean.getCode())) {
                selectedCode = cmbBean.getCode();
            }
        }
        
        for(Object vecActiveApptData : activeAppointmentTypes) {
            if(modifiedActiveAppointmentTypes==null || modifiedActiveAppointmentTypes.size() == 0){
                if(selectedValue != null && vecActiveApptData != null) {
                    ComboBoxBean cmbBean = (ComboBoxBean)vecActiveApptData;
                    if(selectedValue.equals(cmbBean.getDescription())){
                        modifiedActiveAppointmentTypes = activeAppointmentTypes;
                        entryFlag = false;
                    }
                }
            }
        }
        if(entryFlag){
            int size = activeAppointmentTypes.size();
            modifiedActiveAppointmentTypes = new Vector();
            for(Object vecActiveAppointmentData : activeAppointmentTypes) {
                ComboBoxBean cmbActiveBean = (ComboBoxBean)vecActiveAppointmentData;
                modifiedActiveAppointmentTypes.add(cmbActiveBean);
            }
            ComboBoxBean cmbSelectedBean = new ComboBoxBean();
            cmbSelectedBean.setDescription(selectedValue);
            cmbSelectedBean.setCode(selectedCode);
            modifiedActiveAppointmentTypes.add(cmbSelectedBean);
        }
        return modifiedActiveAppointmentTypes;
    }
    
    /**
     * to fetch the active appointment type data     
     * @return String strAppointmentType
     */
    private String setActiveAppointmentType(){
        //to fetch the active appointment types
        String strAppointmentType = EMPTY_STRING;
        String strActiveAppointmentType = EMPTY_STRING;
        Vector vecAppointmentTypes = fetchActiveAppointmentTypes();
        boolean entryFlag = true;
        int counter = 0;
        //to check if the active types have the regular employee
        for(Object cmbAppointmentData:vecAppointmentTypes){
            ComboBoxBean cmbBean = (ComboBoxBean)cmbAppointmentData;
            if(REG_EMPLOYEE.equals(cmbBean.getCode())){
                entryFlag = false;
                strAppointmentType = REG_EMPLOYEE;
            }
            if(counter==0){
                strActiveAppointmentType = cmbBean.getCode();
            }
            counter++;
        }
        //if reg employee is not active then set the first occuring active type for selection
        if(entryFlag){
            strAppointmentType = strActiveAppointmentType;
        }
        return strAppointmentType;
    }
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
}