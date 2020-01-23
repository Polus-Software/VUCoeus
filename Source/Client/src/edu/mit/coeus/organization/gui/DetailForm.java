/*
 * @(#)DetailForm.java 1.0 8/13/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.organization.gui;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.organization.bean.*;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.CoeusMessageResources;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Observable;
import java.util.Observer;
import edu.mit.coeus.utils.BaseWindowObservable;
/**
 * <code> DetailForm </code> is a form which display
 * all the organization details in the Organization Module and can be used to add/modify the 
 * organization information. 
 * This class is instantiated in OrganizationBaseWindow.
 *
 * @version :1.0 August 13, 2002, 1:35 PM
 * @author Guptha K
 *
 */
public class DetailForm extends JComponent {

    private JPanel pnlForm;
    private char functionType;
    public static int selectedTabIndex = 0;
    
    // organization details beans
    OrganizationMaintenanceFormBean orgMaintFormData;
    OrganizationListBean[] orgSelectedListData,orgListData;
    QuestionListBean[] questionsList;
    OrganizationYNQBean[] orgQuestionsList;
    Hashtable expList;
    OrganizationAuditBean[] auditList;
    OrganizationIDCBean[] idcList;
    IDCRateTypesBean[] idcRateTypesList;

    // forms
    NameAddressForm nameAddressTab;
    Organization2Form orgnization2Tab;
    Organization3Form orgnization3Tab;
    QuestionForm orgnizationQuestionTab;
    TypeForm orgnizationTypeTab;
    AuditForm orgnizationAuditTab;
    IDCForm orgnizationIDCTab;

    private String orgId;
    private CoeusDlgWindow coeusDialog;
    private CoeusAppletMDIForm mdiForm;

    private JTabbedPane tbdPnOrganization;
    private JButton btnCancel;
    private Vector rowData;
    
    private boolean locked;
    
    private boolean generateOrganizationID = false; //generate organization id flag

    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    private BaseWindowObservable observable = new BaseWindowObservable();
    /**
     * Default constructor
     */
    public DetailForm() {
    }

    /**
     * Constructor, which constructs the Organization maintenance detail form with the
     * given form data values. The functionality of the form (enable/disable the
     * components) will be decided based on the parameter functionType.
     *
     * @param functionType char  decides to enable/disable the form controls A- Add mode
     *                           D- Display mode M- Modify mode
     * @param dataObjects OrganizationMaintenance FormData
     */
    public DetailForm(char functionType, Vector dataObjects) {
        this.functionType = functionType;
        setValues(dataObjects);
        initComponents();
        if(functionType == 'I'){
            getOrganizationIDConfig();
        }    
    }

    /**
     * Constructor, which constructs the Organization maintenance detail form with the
     * given organization id. The functionality of the form (enable/disable the
     * components) will be decided based on the parameter functionType.
     *
     * @param functionType char  decides to enable/disable the form controls A- Add mode
     *                           D- Display mode M- Modify mode
     * @param orgId Organization id
     */
    public DetailForm(char functionType, String orgId) throws Exception{
        this.functionType = functionType;
        this.orgId = orgId;
        Vector dataObjects = getOrganizationDetails(orgId);
        if(dataObjects!=null){
            setValues(dataObjects);
            initComponents();
        }
        if(functionType == 'I'){
            getOrganizationIDConfig();
        }    
    }
    public void registerObserver( Observer observer ) {
        observable.addObserver( observer );
    }

    /**
     * Display the organization maintenance details form in a dialog window. This dialog
     * window will be shown with reference to the mdi form. It will be centered to the
     * size of physical screen.
     *
     * @param mdiForm the CoeusAppletMDIForm reference
     */
    public void showDialogForm(CoeusAppletMDIForm mdiForm) {
        this.mdiForm = mdiForm;
        // create dialog
        coeusDialog = new CoeusDlgWindow(mdiForm, CoeusGuiConstants.ORGANIZATION_WINDOW_TITLE, true) {
            protected JRootPane createRootPane() {
                ActionListener actionListener = new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        try{
                            validateAndCancelData(true);
                        }catch(Exception ex){
                            ex.printStackTrace();
                            log(ex.getMessage());
                        }
                    }
                };
                JRootPane rootPane = new JRootPane();
                KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0);
                rootPane.registerKeyboardAction(actionListener, stroke,
                    JComponent.WHEN_IN_FOCUSED_WINDOW);
                return rootPane;
            }
        };
        coeusDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        // get the panel of tabs and add it into dialog
        coeusDialog.getContentPane().add(pnlForm, BorderLayout.CENTER);
        // center the dialog to the physical screen
        coeusDialog.setSize(660, 450);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = coeusDialog.getSize();
        coeusDialog.setLocation(screenSize.width / 2 - (dlgSize.width / 2),
                screenSize.height / 2 - (dlgSize.height / 2));
        screenSize = null;
        coeusDialog.setResizable(false);

        // close the dialog on click of window's X button
        coeusDialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                validateAndCancelData(true);
            }

            // set the focus to controls before loading the components
            public void windowOpened(WindowEvent e) {
                if (functionType == 'D') {
                    btnCancel.requestFocus();
                } else if (functionType == 'U') {
                    nameAddressTab.setFocusToName(generateOrganizationID);
                } else {
                    //check whether we need to generate organization id
                    if(generateOrganizationID) {
                        nameAddressTab.setFocusToName(generateOrganizationID);
                    }else {
                        nameAddressTab.setFocusToId();
                    }
                }
            }
        });
        //Esc key window closing...
        coeusDialog.addEscapeKeyListener(
                new AbstractAction("escPressed"){
                    public void actionPerformed(ActionEvent ae) {
                        validateAndCancelData(true);
                    }
            });
        coeusDialog.setVisible(true);
    }

    /**
     *  Method to find the requested record of oraganization is locked or not.
     *  This flag will set when the locked exception comes from the server.
     */
    boolean isLocked(){
        return locked;
    }
    /**
     * set the bean values. The data are from the server after
     *
     * @param dataObjects vector contain the organization maintenance data
     * 0th element contains organization name address details
     * 1st element contain organization type list
     * 2nd element contain selected organization type list
     * 3rd element contain questions list
     * 4th element contain selected questions list
     * 5th element contain the questions explanations
     * 6th element contain audit list
     * 7th element contain idc list
     */
    private void setValues(Vector dataObjects) {
        // get the bean objects from the vector
        if (dataObjects != null) {
            orgMaintFormData = (OrganizationMaintenanceFormBean) dataObjects.elementAt(0);
            // organization list
            orgListData = (OrganizationListBean[]) dataObjects.elementAt(1);
            // selected organization type list
            orgSelectedListData = (OrganizationListBean[]) dataObjects.elementAt(2);
            // questions list
            questionsList = (QuestionListBean[]) dataObjects.elementAt(3);
            // the organization question list
            orgQuestionsList = (OrganizationYNQBean[]) dataObjects.elementAt(4);
            // the all question explanations
            expList = (Hashtable) dataObjects.elementAt(5);
            //get the audit
            auditList = (OrganizationAuditBean[]) dataObjects.elementAt(6);
            //get the IDC
            idcList = (OrganizationIDCBean[]) dataObjects.elementAt(7);
            //get the IDC Rate Types
            idcRateTypesList = (IDCRateTypesBean[]) dataObjects.elementAt(8);
        }
    }

    /**
     * This method connect to the server and gets the organization details for the
     * specified id.
     *
     * @param orgId organization id
     * @return Vector contain the organization details.
     */
    private Vector getOrganizationDetails(String orgId) throws Exception {
        Vector dataObjects = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/orgMntServlet";
        // connect to the database and get the formData for the given organization id
        RequesterBean request = new RequesterBean();
        request.setFunctionType(functionType);
        request.setId(orgId);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();        
        ResponderBean response = comm.getResponse();
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
            response.setMessage(coeusMessageResources.parseMessageKey(
                    "server_exceptionCode.1000"));
        }
        if (response.isSuccessfulResponse()) {
            dataObjects = response.getDataObjects();
        } else {
            if(response.isLocked()){
                this.locked = true;
                return null;
            }else{
                throw new Exception(response.getMessage());
            }
        }
        return dataObjects;
    }


    /**
     * This method is used to check whether we need to auto generate 
     * organization id and the method will return true
     * if required else false.
     *
     * @return boolean true if generate organization id else false
     */
    private boolean getOrganizationIDConfig() {
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/orgMntServlet";
        // connect to the database and get parameter config to identify whether we need to generate organization id
        RequesterBean request = new RequesterBean();
        request.setFunctionType('C');
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();        
        ResponderBean response = comm.getResponse();
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
            response.setMessage(coeusMessageResources.parseMessageKey(
                    "server_exceptionCode.1000"));
        }
        if (response.isSuccessfulResponse()) {
            /* parameter flag to indicate whether we need to generate sponsor code */
            generateOrganizationID = ((String)response.getDataObject()).equalsIgnoreCase("TRUE"); 
        } 

        //System.out.println("client generate organz value " + generateOrganizationID);
        return generateOrganizationID;
        
    }

    /**
     * initialize the components.
     * Creates a panel and add the components.
     */
    private void initComponents() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        
        pnlForm = new JPanel();
        pnlForm.setLayout(new BorderLayout());
        pnlForm.add(createForm(), BorderLayout.CENTER);
        pnlForm.setSize(650, 300);
        pnlForm.setLocation(200, 200);

        JPanel pnlButtons = new JPanel();
        pnlButtons.setLayout(new BorderLayout(10,10));

        JPanel pnlOk = new JPanel();        
        JButton btnOk = new JButton("OK");                                    
        btnOk.setEnabled(functionType == 'D' ? false : true);
        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                validateAndSaveData();
            }
        });
        btnOk.setMnemonic('O');
        btnOk.setFont(CoeusFontFactory.getLabelFont());

        btnCancel = new JButton("Cancel");
        
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                validateAndCancelData(true);
            }
        });
        btnCancel.setMnemonic('C');        
        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        
        pnlOk.setLayout(new GridBagLayout() );
        java.awt.GridBagConstraints gridBagConstraints3;
        gridBagConstraints3 = new java.awt.GridBagConstraints();
       
        btnOk.setPreferredSize(new java.awt.Dimension(73, 25));
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 0;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.insets = new java.awt.Insets(20, 10, 8, 2);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlOk.add(btnOk, gridBagConstraints3);
        
        btnCancel.setPreferredSize(new java.awt.Dimension(73, 25));
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 1;
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints3.insets = new java.awt.Insets(5, 11, 11, 1);        
        pnlOk.add(btnCancel, gridBagConstraints3);
        pnlOk.setAlignmentX(java.awt.Container.TOP_ALIGNMENT);
        pnlButtons.add(pnlOk,BorderLayout.NORTH);
        pnlForm.add(pnlButtons, BorderLayout.EAST);
        this.setLayout(new BorderLayout());
        add(pnlForm);        
    }

    
    /**
     * Creates Organization Maintenance tabs.
     *
     * @return JtbdPnOrganization tabbed pane that contains organization details
     */
    public JTabbedPane createForm() {
        tbdPnOrganization = new CoeusTabbedPane(CoeusTabbedPane.CTRL_T);
        
        //updated by Subramanya Nov'7 2002 - get the index of selected Tab.
        tbdPnOrganization.addChangeListener( new ChangeListener(){
            public void stateChanged( ChangeEvent chgEvent ){                
                selectedTabIndex = tbdPnOrganization.getSelectedIndex();       
            }
        });
        
        // create NameAddress tab
        nameAddressTab = new NameAddressForm(functionType, orgMaintFormData);
        // create Organization2 tab
        orgnization2Tab = new Organization2Form(functionType, orgMaintFormData);
        // create Organization3 tab
        orgnization3Tab = new Organization3Form(functionType, orgMaintFormData);
        // create OrganizationType tab
        orgnizationTypeTab = new TypeForm(functionType, orgSelectedListData, orgListData);
        // create Organization question tab
        orgnizationQuestionTab = new QuestionForm(functionType, questionsList, orgQuestionsList, expList);
        // create Organization audit tab
        orgnizationAuditTab = new AuditForm(functionType, auditList);
        // create Organization IDC tab
        orgnizationIDCTab = new IDCForm(functionType, idcList, idcRateTypesList);

        // set the tab into tab pane
        tbdPnOrganization.addTab("Name & Address", nameAddressTab);
        tbdPnOrganization.addTab("Organization-2", orgnization2Tab);
        tbdPnOrganization.addTab("Organization-3", orgnization3Tab);
        tbdPnOrganization.addTab("Type", orgnizationTypeTab);
        tbdPnOrganization.addTab("Question", orgnizationQuestionTab);
        tbdPnOrganization.addTab("Audit", orgnizationAuditTab);
        tbdPnOrganization.addTab("IDC", orgnizationIDCTab);
        tbdPnOrganization.setFont(CoeusFontFactory.getNormalFont());
        tbdPnOrganization.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    validateAndCancelData(false);
                }
            }
        });
        return tbdPnOrganization;
    }

    /**
     * Do validataion on click of cancel button. This method will monitor any changes has been
     * occured in the organization detail form. If there is any changes, the respective alert
     * message will be thrown to the user to save the data.
     *
     * @param showCancelButton boolean value to determin the type of alert to be shown
     * true - will show yes,no and cancel button in the alert message
     * false - will show yes and no button in the alert message
     */
    public void validateAndCancelData(boolean showCancelButton) {
        boolean dataChanged = true;
        orgnizationAuditTab.stopTableEditing();
        if (!nameAddressTab.isSaveRequired()) {
            if (!orgnization2Tab.isSaveRequired()) {
                if (!orgnization3Tab.isSaveRequired()) {
                    if (!orgnizationTypeTab.isSaveRequired()) {
                        if (!orgnizationQuestionTab.isSaveRequired()) {
                            if (!orgnizationAuditTab.isSaveRequired()) {
                                if (!orgnizationIDCTab.isSaveRequired()){
                                    dataChanged = false;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (dataChanged && functionType != 'D') {
            int answer = 0;
            if (showCancelButton) {
                answer = CoeusOptionPane.showQuestionDialog(
                            coeusMessageResources.parseMessageKey(
                                "saveConfirmCode.1002"), 
                    CoeusOptionPane.OPTION_YES_NO_CANCEL, 
                    CoeusOptionPane.DEFAULT_YES);
            } else {
                answer = CoeusOptionPane.showQuestionDialog(
                            coeusMessageResources.parseMessageKey(
                                "saveConfirmCode.1002"), 
                            CoeusOptionPane.OPTION_YES_NO_CANCEL, 
                            CoeusOptionPane.DEFAULT_YES);
            }
            if (answer == JOptionPane.YES_OPTION) {
                validateAndSaveData();
            } else if (answer == JOptionPane.NO_OPTION) {
                releaseUpdateLock();
                coeusDialog.dispose();
            } else {
                return;
            }
        } else {
            releaseUpdateLock();
            coeusDialog.dispose();
        }

    }
    
    private void releaseUpdateLock(){
        try{
            if ( functionType == 'U' ) {
                String rowId = orgMaintFormData.getOrganizationId().trim();
                RequesterBean requester = new RequesterBean();
                requester.setDataObject(rowId);
                requester.setFunctionType('Z');
                String connectTo =CoeusGuiConstants.CONNECTION_URL+"/orgMntServlet";
                AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);
                /**
                 * Updated for REF ID :0003  Feb'21 2003.
                 * Hour Glass implementation while DB Trsactions Wait
                 * by Subramanya Feb' 21 2003
                 */
                if( coeusDialog != null ){
                    coeusDialog.setCursor( new Cursor( Cursor.WAIT_CURSOR ) );        
                }
                
                comm.send();        
                ResponderBean res = comm.getResponse();

                if( coeusDialog != null ){
                    coeusDialog.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
                }
                
                
                if (res != null && !res.isSuccessfulResponse()){
                    CoeusOptionPane.showErrorDialog(res.getMessage());
                    return;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
        
    }

    /**
     * Client side validation on click of OK button. 
     * This method validates all the panel screens and if the validation is 
     * successful it save and close the Organization details else it alerts the 
     * user with the corresponding informative message and the organization detail form remains.
     */
    
    public void validateAndSaveData() {
        
        if (!nameAddressTab.validateData(generateOrganizationID)) {
            tbdPnOrganization.setSelectedIndex(0);
        } else if (!orgnization3Tab.validateData()) {    // validate org form 3
            tbdPnOrganization.setSelectedIndex(2);
        } else if (!orgnizationTypeTab.validateData()) { //validate type
            tbdPnOrganization.setSelectedIndex(3);
        } else if (!orgnizationQuestionTab.validateData()) {  //validate question
            tbdPnOrganization.setSelectedIndex(4);
        } else if (!orgnizationAuditTab.validateData()) {   //validate Audit
            tbdPnOrganization.setSelectedIndex(5);
        } else if (!orgnizationIDCTab.validateData()) {   //validate IDC
            tbdPnOrganization.setSelectedIndex(6);
        } else {
            saveData();
            coeusDialog.dispose();
        }
    }

    /**
     * Method to save the organization details data. 
     */
    public void saveData() {
        Vector dataObjects = new Vector();
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/orgMntUpdateServlet";

        RequesterBean request = new RequesterBean();
        // set the function type
        request.setFunctionType(functionType);
        // get the name and address data
        nameAddressTab.setData();
        // get organization 2 details
        orgnization2Tab.setData();
        // get organization 3 details
        orgnization3Tab.setData();
        orgMaintFormData.setAcType("" + functionType);
        // get organization type details
        OrganizationListBean[] selList = orgnizationTypeTab.getFormData();
        // get organization question details
        orgnizationQuestionTab.setData();
        
        //Bug Fix: Validation , saving was not happening properly Start 1
        OrganizationYNQBean[] orgQuesList = orgnizationQuestionTab.getOrgQuestionList();
        //Bug Fix: Validation , saving was not happening properly End 1
        
        // get organization audit data
        OrganizationAuditBean[] auditData = orgnizationAuditTab.getFormData();
        // get organization idc data
        OrganizationIDCBean[] idcData = orgnizationIDCTab.getFormData();
        // save the data. Keep all the bean object into vector and send it to server
        dataObjects.addElement(orgMaintFormData);
        dataObjects.addElement(selList);
        
        //Bug Fix: Validation , saving was not happening properly Start 2 
        //dataObjects.addElement(orgQuestionsList);
        dataObjects.addElement(orgQuesList);
        //Bug Fix: Validation , saving was not happening properly End 2
        
        dataObjects.addElement(auditData);
        dataObjects.addElement(idcData);
        request.setDataObjects(dataObjects);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        coeusDialog.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
        /* updated by ravi on 18-02-03 for ignoring mouse clicks 
           when the system is busy with some operation */
        mdiForm.getGlassPane().setVisible(true);
        comm.send();
        ResponderBean response = comm.getResponse();
        coeusDialog.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        
        mdiForm.getGlassPane().setVisible(false);
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
            response.setMessage(coeusMessageResources.parseMessageKey(
                    "server_exceptionCode.1000"));
        }
        if (response.isSuccessfulResponse()) {
            dataObjects = response.getDataObjects();
            if (functionType == 'I'){
                orgMaintFormData = (OrganizationMaintenanceFormBean) response.getDataObject();
                //System.out.println("client - successfull response - org id " + orgMaintFormData.getOrganizationId());
            }    
            observable.setFunctionType( functionType );
            observable.notifyObservers(orgMaintFormData);
            coeusDialog.dispose();
            setDataToUpdate();
        } else {
            log(response.getMessage());
        }
    }

    /**
     *  Method used to refresh the base window.
     */
    public void setDataToUpdate() {
        rowData = new Vector();
        rowData.add(orgMaintFormData.getOrganizationId());
        rowData.add(orgMaintFormData.getOrganizationName());
        rowData.add(orgMaintFormData.getAddress());
        rowData.add(orgMaintFormData.getCongressionalDistrict());
        rowData.add(orgMaintFormData.getFederalEmployerID());
        rowData.add(orgMaintFormData.getVendorCode());
        rowData.add(orgMaintFormData.getDunsNumber());
        rowData.add(orgMaintFormData.getDunsPlusFourNumber());
        rowData.add(orgMaintFormData.getDodacNumber());
        rowData.add(orgMaintFormData.getCageNumber());
    }

    /**
     * method to get the data to be updated into base window
     *
     * @return Vector contains the column values to be updated into base window
     */
    public Vector getDataToUpdate() {
        return rowData;
    }

    /**
     * display alert message
     *
     * @param mesg the message to be displayed
     */
    public void log(String mesg) {
        CoeusOptionPane.showErrorDialog(mesg);
    }
}
