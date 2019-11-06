/*
 * SubAwardBudgetController.java
 *
 * Created on February 16, 2006, 1:56 PM
 */
/* PMD check performed, and commented unused imports and variables 
 * on 25-JULY-2011 by Satheesh Kumar K N
 */
package edu.mit.coeus.budget.controller; 

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.budget.BudgetSubAwardConstants;
import edu.mit.coeus.budget.bean.BudgetBean;
import edu.mit.coeus.budget.bean.BudgetDetailBean;
import edu.mit.coeus.budget.bean.BudgetPeriodBean;
import edu.mit.coeus.budget.bean.BudgetSubAwardAttachmentBean;
import edu.mit.coeus.budget.bean.BudgetSubAwardBean;
import edu.mit.coeus.budget.bean.BudgetSubAwardDetailBean;
import edu.mit.coeus.budget.gui.BudgetSubAward;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.propdev.bean.ProposalHierarchyLinkBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusFileChooser;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UserUtils;
import edu.mit.coeus.utils.document.*;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.GreaterThan;
import edu.mit.coeus.utils.query.QueryEngine;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;
import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel; // JM
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

// JM 6-25-2013 added combo box
import javax.swing.JComboBox;
// JM END

/**
 * Controller for BudgetSubAward form
 * @author sharathk
 */
public class BudgetSubAwardController extends Controller implements ActionListener, ListSelectionListener, MouseListener{
    
    private BudgetSubAward subAwardBudget;
    private BudgetSubAwardTableModel subAwardBudgetTableModel;
    private BudgetSubAwardEditor subAwardBudgetEditor;
    private CoeusAppletMDIForm mdiForm;
    private CoeusDlgWindow subAwardDlgWindow;
    private BudgetBean budgetBean;
    private List data;
    private String userId;
    private List deletedData;
    private CoeusMessageResources coeusMessageResources;
    // COEUSQA-1925: Narrative upload recall the last folder from which an upload Can't be done
//    private JFileChooser fileChooser;
    private CoeusFileChooser fileChooser;
    private BudgetSubAwardFileFilter budgetSubAwardFileFilter;
    
    private static final String connect = CoeusGuiConstants.CONNECTION_URL+"/BudgetSubAwardServlet";
    private static final String streaming = CoeusGuiConstants.CONNECTION_URL+"/StreamingServlet";
    private static final String ConnectBudgetServlet = CoeusGuiConstants.CONNECTION_URL+"/BudgetMaintenanceServlet";
    private static final char GET_BUDGET_PERIOD = 'i';
    
    // Modified for COEUSQA-2115 : Subaward budgeting for Proposal Development - Start
//    private int width = 725;
    private int width = 775;
    // Modified for COEUSQA-2115 : Subaward budgeting for Proposal Development - End
    //Modified And Added for Case#3404-Outstanding proposal hierarchy changes- Start
    private int height = 310;
    private int detailHeight = 250;    
    private Vector cvHierarchyLinks;
    //Modified and Added for Case#3404-Outstanding proposal hierarchy changes - End
    public static final int SUB_AWARD_NUM_COLUMN = 0;
    public static final int ORGANIZATION_NAME_COLUMN = 1;
    public static final int FORM_NAME_COLUMN = 2;
    public static final int PDF_COLUMN = 3;
    public static final int XML_COLUMN = 4;
    // JM 6-25-2012 organization id
    public static final int ORGANIZATION_ID_COL = 5;
    // JM END
    
    // JM 6-25-2012 added organization id
    private int columnWidth[] = {25, 175, 150, 150, 100, 0};
    private JComboBox cmbOrganization;
    private Vector vOrganizations;
    // JM END
    
    private List lstFileNames;
    
    private boolean removing = false;
    private int deletingRow = -1;
    protected static final String EMPTY_STRING = "";
    
    private static final String DATE_FORMAT = "dd-MMM-yyyy hh:mm:ss a";
    
    private static final String TITLE = "Sub Award Budget";
    private static final String ENTER_ORGANIZATION_NAME = "budgetSubAward_exceptionCode.1552";
    private static final String ENTER_PDF_FILE = "budgetSubAward_exceptionCode.1553";
    private static final String WANT_TO_SAVE_CHANGES = "budget_saveChanges_exceptionCode.1210";
    private static final String XML_OUT_OF_SYNC = "budgetSubAward_exceptionCode.1554";
    private static final String SELECT_ROW_TO_DELETE = "budgetSubAward_exceptionCode.1555";
    private static final String DELETE_ROW = "budgetSubAward_exceptionCode.1556";
    // Added for COEUSQA-2115 : Subaward budgeting for Proposal Development - Start
    private Vector vecBudgetPeriods = null;
    private boolean isPeriodsGenerated = false;
    private boolean refreshBudgetDetails = false;
    private BudgetSubAwardDetailController subAwardDetailController;
    private static final String COST_ELEMENT_INACTIVE = "budgetSubAward_exceptionCode.1557";
    // Added for COEUSQA-2115 : Subaward budgeting for Proposal Development - End
            
    // JM 6-26-2013 need proposalNumber
    String proposalNumber;
    // JM END
    
    /** Creates a new instance of SubAwardBudgetController */
    public BudgetSubAwardController() {
        setFunctionType(TypeConstants.DISPLAY_MODE);
        initComponents();
        registerComponents();
    }
    
    /**
     * Creates a new instance of SubAwardBudgetController
     * @param functionType function type
     */
    public BudgetSubAwardController(char functionType) {
        setFunctionType(functionType);
        initComponents();
        registerComponents();
    }
    
    // JM 6-26-2013 new instance where proposalNumber is passed in
    /**
     * Creates a new instance of SubAwardBudgetController
     * @param functionType function type
     */
    public BudgetSubAwardController(char functionType, String proposalNumber) {
        setFunctionType(functionType);
        this.proposalNumber = proposalNumber;
        initComponents();
        registerComponents();
    }
    
    private void initComponents() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        mdiForm = CoeusGuiConstants.getMDIForm();
        subAwardBudget = new BudgetSubAward();
        userId = mdiForm.getUserId();
        
        JTableHeader header = subAwardBudget.tblSubAwardBudget.getTableHeader();
        header.setFont(CoeusFontFactory.getLabelFont());
        
        // JM 6-28-2013 updated to send orgs
        subAwardBudgetTableModel = new BudgetSubAwardTableModel(getFunctionType(),mdiForm.getUserId(),getProposalSubOrgs());
        // JM END
        subAwardBudget.tblSubAwardBudget.setModel(subAwardBudgetTableModel);
        for(int index = 0; index < columnWidth.length; index++) {
            //subAwardBudget.tblSubAwardBudget.getColumnModel().getColumn(index).setMaxWidth(columnWidth[index]);
            subAwardBudget.tblSubAwardBudget.getColumnModel().getColumn(index).setMinWidth(columnWidth[index]/2);
            subAwardBudget.tblSubAwardBudget.getColumnModel().getColumn(index).setPreferredWidth(columnWidth[index]);
        }
        BudgetSubAwardRenderer subAwardBudgetRenderer = new BudgetSubAwardRenderer();
        subAwardBudget.tblSubAwardBudget.getColumnModel().getColumn(PDF_COLUMN).setCellRenderer(subAwardBudgetRenderer);
        subAwardBudget.tblSubAwardBudget.getColumnModel().getColumn(XML_COLUMN).setCellRenderer(subAwardBudgetRenderer);
        
        if(getFunctionType() != TypeConstants.DISPLAY_MODE) {
            subAwardBudgetEditor = new BudgetSubAwardEditor();
            // JM 6-25-2013 changed organization name to combo box
            subAwardBudget.tblSubAwardBudget.getColumnModel().getColumn(ORGANIZATION_NAME_COLUMN).setCellEditor(new DefaultCellEditor(getOrgCmb()));
            //subAwardBudget.tblSubAwardBudget.getColumnModel().getColumn(ORGANIZATION_NAME_COLUMN).setCellEditor(subAwardBudgetEditor);
            // JM END
            subAwardBudget.tblSubAwardBudget.getColumnModel().getColumn(PDF_COLUMN).setCellEditor(subAwardBudgetEditor);
            subAwardBudget.tblSubAwardBudget.getColumnModel().getColumn(XML_COLUMN).setCellEditor(subAwardBudgetEditor);
        }
        subAwardBudget.tblSubAwardBudget.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        //Display Details by Default
        //subAwardBudget.pnlDetails.setVisible(false);
        subAwardBudget.chkDetails.setSelected(true);
        
        subAwardDlgWindow = new CoeusDlgWindow(mdiForm, TITLE, true);
        subAwardDlgWindow.getContentPane().add(subAwardBudget);
        subAwardDlgWindow.setSize(width, height + detailHeight);
        
        subAwardBudget.setPreferredSize(new Dimension(width, height + detailHeight));
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        subAwardDlgWindow.setLocation(screenSize.width/2 - (width/2), screenSize.height/2 - ((height + detailHeight)/2));
    }
    
    // JM 6-25-2013 create combo box of organization name
    private JComboBox getOrgCmb() {
        cmbOrganization = new JComboBox();
        
        vOrganizations  = getProposalSubOrgs();

        CoeusVector cvOrgs = new CoeusVector();
    	cvOrgs.add(0, "");

        HashMap mapRow = new HashMap();
    	for (int c=0;c<vOrganizations.size();c++) {
            mapRow = (HashMap) vOrganizations.elementAt(c);
        	cvOrgs.add(c+1,(String)mapRow.get("LOCATION_NAME"));
    	}
    	
    	cmbOrganization.setModel(new DefaultComboBoxModel(cvOrgs));
        return cmbOrganization;
    }
    // JM END
    
    // JM 6-27-2012 method to set organization ID from name selected
    /*
    public String getOrganizationId() {
    	System.out.println("vOrganizations :: " + vOrganizations);
    	System.out.println("selected cmbBox option :: "+ cmbOrganization.getSelectedIndex());
        
        HashMap map = (HashMap) vOrganizations.get(cmbOrganization.getSelectedIndex());
        String orgId = (String) map.get("ORGANIZATION_ID");        
        return orgId;
    }
    */
    // JM 6-25-2013 method to get proposal subaward organizations
    private Vector getProposalSubOrgs() {
        Vector result =  new Vector(); 
    	
    	if (proposalNumber != null) {
	        RequesterBean requesterBean = new RequesterBean();
	        requesterBean.setDataObject(proposalNumber);
	        requesterBean.setFunctionType(BudgetSubAwardConstants.GET_PROP_ORGS_FOR_SUB);
	        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
	        appletServletCommunicator.setConnectTo(connect);
	        appletServletCommunicator.setRequest(requesterBean);
	        appletServletCommunicator.send();
	        ResponderBean responderBean = appletServletCommunicator.getResponse();
	        
	        try {
				if(responderBean.hasResponse()) {
					result = (Vector) responderBean.getDataObject();
				}
			} catch (CoeusException e) {
				System.out.println("Could not retrieve proposal organizations");
				e.printStackTrace();
			}            
    	}

    	return result; 
    }
    // JM END
    
    /**
     * displays the Budget Sub Award Dialog Window
     */
    public void display() {
        subAwardDlgWindow.setVisible(true);
    }
    
    public void formatFields() {
    }
    
    /**
     * returns controlled Component
     * @return controlled Component
     */
    public java.awt.Component getControlledUI() {
        return subAwardBudget;
    }
    
    /**
     * returns the controlled data
     * @return data used by the GUI
     */
    public Object getFormData() {
        return data;
    }
    
    /**
     * registers components with event handlers and enables/disables components depending on function type
     */
    public void registerComponents() {
        subAwardBudget.btnCancel.addActionListener(this);
        subAwardBudget.chkDetails.addActionListener(this);
        if(getFunctionType() != TypeConstants.DISPLAY_MODE) {
            subAwardBudget.btnAdd.addActionListener(this);
            subAwardBudget.btnDelete.addActionListener(this);
            subAwardBudget.btnOk.addActionListener(this);
            subAwardBudget.lstAttachments.addMouseListener(this);
            subAwardBudget.btnTranslate.addActionListener(this);
            subAwardBudget.btnUploadPDF.addActionListener(this);

        }else {
            subAwardBudget.btnAdd.setEnabled(false);
            subAwardBudget.btnDelete.setEnabled(false);
            subAwardBudget.btnOk.setEnabled(false);
            subAwardBudget.btnTranslate.setEnabled(false);
            subAwardBudget.btnUploadPDF.setEnabled(false);
            
            subAwardBudget.txtArComments.setEnabled(false);
            subAwardBudget.lstAttachments.addMouseListener(this);
        }
        // Added for COEUSQA-2115 : Subaward budgeting for Proposal Development - Start
        subAwardBudget.btnSubAwardDetails.addActionListener(this);
        // Added for COEUSQA-2115 : Subaward budgeting for Proposal Development - End
        subAwardBudget.btnViewPDF.addActionListener(this);
        subAwardBudget.btnViewXML.addActionListener(this);
        subAwardBudget.txtArStatus.setEnabled(false);
        
        //Make Table Single Selection
        subAwardBudget.tblSubAwardBudget.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        //Setup List Selection Listener so as to update details panel when row selection changes.
        subAwardBudget.tblSubAwardBudget.getSelectionModel().addListSelectionListener(this);
        
        subAwardDlgWindow.addEscapeKeyListener(
                new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                close();
                return;
            }
        });
        subAwardDlgWindow.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        
        subAwardDlgWindow.addWindowListener(new WindowAdapter(){
            //public void windowActivated(WindowEvent we) {
            //requestDefaultFocus();
            //}
            public void windowClosing(WindowEvent we){
                close();
            }
        });
    }
    
    public void saveFormData(){
    }
    
    /**
     * populates the contents of the pdf file before saving
     * @return List of BudgetSubAwardBean to be saved
     * @throws java.io.IOException if the PDF file has to be uploaded and cannot be read
     */
    private List getDataToSave() throws IOException {
        if(lstFileNames == null) {
            lstFileNames = new ArrayList();
        }else {
            lstFileNames.clear();
        }
        
        List lstDataToSave = new ArrayList();
        if(deletedData != null && deletedData.size() > 0) {
            lstDataToSave.addAll(deletedData);
        }
        if(data != null && data.size() > 0) {
            BudgetSubAwardBean budgetSubAwardBean;
            for(int index = 0; index < data.size(); index++) {
                budgetSubAwardBean = (BudgetSubAwardBean)data.get(index);
//                if(budgetSubAwardBean.getAcType() == null && budgetSubAwardBean.getPdfAcType() == null && budgetSubAwardBean.getXmlAcType() == null) {
//                    continue;
//                }
                if(budgetSubAwardBean.getPdfAcType() != null) {
                    FileInputStream fileInputStream = new FileInputStream(budgetSubAwardBean.getPdfFileName());
                    byte byteData[] = new byte[fileInputStream.available()];
                    fileInputStream.read(byteData);
                    budgetSubAwardBean.setSubAwardPDF(byteData);
                    lstFileNames.add(budgetSubAwardBean.getPdfFileName());
                    budgetSubAwardBean.setPdfFileName(new File(budgetSubAwardBean.getPdfFileName()).getName());
                }
                lstDataToSave.add(budgetSubAwardBean);
            }
        }
        return lstDataToSave;
    }
    
    /**
     * Sets the Data to be displayed to the form
     * @param data data to be set to the GUI
     * @throws edu.mit.coeus.exception.CoeusException if any error occurs
     */
    public void setFormData(Object data)throws CoeusException {
        if(data instanceof BudgetBean) {
            budgetBean = (BudgetBean)data;
            RequesterBean requesterBean = new RequesterBean();
            requesterBean.setDataObject(budgetBean);
            requesterBean.setFunctionType(BudgetSubAwardConstants.GET_BUDGET_SUB_AWARD);
            AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
            appletServletCommunicator.setConnectTo(connect);
            appletServletCommunicator.setRequest(requesterBean);
            appletServletCommunicator.send();
            ResponderBean responderBean = appletServletCommunicator.getResponse();
            
            //Modified and Added for Case#3404-Outstanding proposal hierarchy changes -Start
//            if(responderBean.hasResponse()) {
//                data = responderBean.getDataObject();
//            }
            Vector vecResultData =  new Vector();             
            if(responderBean.hasResponse()) {
                vecResultData = responderBean.getDataObjects();
                data = vecResultData.get(0);
                cvHierarchyLinks = (Vector)vecResultData.get(1);
            }            
            //Modified and Added for Case#3404-Outstanding proposal hierarchy changes -End
            
            subAwardBudget.lblProposalNumber.setText(budgetBean.getProposalNumber());
            subAwardBudget.lblVersionNumber.setText(""+budgetBean.getVersionNumber());
            
            // JM 6-26-2013 
            proposalNumber = budgetBean.getProposalNumber();
            // JM END
            
        }
        this.data = (List)data;
        subAwardBudgetTableModel.setData(this.data);
        if(getFunctionType() != TypeConstants.DISPLAY_MODE) {
            subAwardBudgetEditor.setData(this.data);
        }
        //Select the first Row if exists
        if(this.data != null && this.data.size() > 0) {
            subAwardBudget.tblSubAwardBudget.setRowSelectionInterval(0, 0);
        }
        
        // Added for COEUSQA-2115 : Subaward budgeting for Proposal Development - Start
        // User can enter sub award details only when periods are generated
        //COEUSQA-4061
        CoeusVector cvBudgetPeriod = new CoeusVector(); 
        
        cvBudgetPeriod = getBudgetPeriodData(budgetBean.getProposalNumber() , budgetBean.getVersionNumber());     
        
        if((!isPeriodsGenerated() && cvBudgetPeriod.size() != 1 )|| subAwardBudget.tblSubAwardBudget.getRowCount() < 1){
            subAwardBudget.btnSubAwardDetails.setEnabled(false);
        }
        //COEUSQA-4061
        // Added for COEUSQA-2115 : Subaward budgeting for Proposal Development - End

    }
    
    /**
     * validates the form
     * @throws edu.mit.coeus.exception.CoeusUIException if any error occurs
     * @return returns true if validation reules have been satisfied
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        BudgetSubAwardBean budgetSubAwardBean;
        for(int index = 0; index < data.size(); index++) {
            budgetSubAwardBean = (BudgetSubAwardBean)data.get(index);
            validate(budgetSubAwardBean, index);
        }
        return true;
    }
    
    private boolean validate(BudgetSubAwardBean budgetSubAwardBean, int index)throws edu.mit.coeus.exception.CoeusUIException {
        if(budgetSubAwardBean.getOrganizationName() == null || budgetSubAwardBean.getOrganizationName().trim().equals("")) {
            CoeusUIException coeusUIException = new CoeusUIException(coeusMessageResources.parseMessageKey(ENTER_ORGANIZATION_NAME), CoeusUIException.WARNING_MESSAGE);
            coeusUIException.setTabIndex(index);
            throw coeusUIException;
        }
        // PDF is not mandatory
//        else if(budgetSubAwardBean.getPdfFileName() == null) {
//            CoeusUIException coeusUIException = new CoeusUIException(coeusMessageResources.parseMessageKey(ENTER_PDF_FILE), CoeusUIException.WARNING_MESSAGE);
//            coeusUIException.setTabIndex(index);
//            throw coeusUIException;
//        }
        return true;
    }
    
    /**
     * Close the Dialog Window if is not modified else display save required dialog.
     */
    private void close(){
        boolean saveRequired = false;
        String messageKey = null;
        updateComments();
        if(getFunctionType() != TypeConstants.DISPLAY_MODE && isSaveRequired()) {
            saveRequired = true;
            if(xmlGenerationRequired()){
                messageKey = XML_OUT_OF_SYNC;
            }else{
                messageKey = WANT_TO_SAVE_CHANGES;
            }
        }else if(getFunctionType() != TypeConstants.DISPLAY_MODE && xmlGenerationRequired()) {
            saveRequired = true;
            messageKey = XML_OUT_OF_SYNC;
        }
        
        if(saveRequired) {
            int selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(messageKey), CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_YES);
            if(selection == CoeusOptionPane.SELECTION_YES){
                save(true);
            }else if(selection == CoeusOptionPane.SELECTION_NO) {
                subAwardDlgWindow.dispose();
            }
        }else{
            subAwardDlgWindow.dispose();
        }
    }
    
    public boolean isSaveRequired() {
        boolean saveRequired = false;
        if(deletedData != null && deletedData.size() > 0) {
            saveRequired = true;
        }
        BudgetSubAwardBean budgetSubAwardBean;
        for(int index = 0; index < data.size(); index++) {
            budgetSubAwardBean = (BudgetSubAwardBean)data.get(index);
            if(budgetSubAwardBean.getAcType() != null || budgetSubAwardBean.getXmlAcType() != null ||
                    budgetSubAwardBean.getPdfAcType() != null) {
                saveRequired = true;
            }
        }
        return saveRequired;
    }
    
    /**
     * returns true if XML has to be generated for this sub award, else returns false.
     * @return true if XML has to be generated for this sub award, else returns false.
     */
    private boolean xmlGenerationRequired() {
        boolean xmlOutOfSync = false;
        BudgetSubAwardBean budgetSubAwardBean;
        for(int index = 0; index < data.size(); index++) {
            budgetSubAwardBean = (BudgetSubAwardBean)data.get(index);
            if(budgetSubAwardBean.getXmlAcType()!=null || budgetSubAwardBean.getXmlUpdateTimestamp() == null) {
                xmlOutOfSync = true;
                break;
            }
        }
        
        return xmlOutOfSync;
    }
    
    /**
     * Validate and Save the Data to Server
     */
    private void save(boolean closeAfterSave) {
        List lstDataToSave = null;
        try{
            subAwardBudgetEditor.stopCellEditing();
            int selectedRow = subAwardBudget.tblSubAwardBudget.getSelectedRow();
            BudgetSubAwardBean budgetSubAwardBean;
            if(selectedRow != -1) {
                budgetSubAwardBean = (BudgetSubAwardBean)data.get(selectedRow);
                String strComments = subAwardBudget.txtArComments.getText().trim();
                if((budgetSubAwardBean.getComments() == null && strComments.length() > 0) ||
                        (budgetSubAwardBean.getComments() != null && !budgetSubAwardBean.getComments().equals(strComments))) {
                    budgetSubAwardBean.setComments(subAwardBudget.txtArComments.getText().trim());
                    if(budgetSubAwardBean.getAcType() == null) {
                        budgetSubAwardBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                }
            }
            
            validate();
            
            lstDataToSave = getDataToSave();
            
            RequesterBean requesterBean = new RequesterBean();
            requesterBean.setDataObject(lstDataToSave);
            requesterBean.setFunctionType(BudgetSubAwardConstants.SAVE_BUDGET_SUB_AWARD);//Save BudgetSubAward
            AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(connect, requesterBean);
            appletServletCommunicator.send();
            
            ResponderBean responderBean = appletServletCommunicator.getResponse();
            boolean success = responderBean.hasResponse();
            List list = null;
            // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
            // Once the sub award details are modified and saved, budget details will be refreshed.
            setRefreshBudgetDetails(true);
            if(success){
                Object object = responderBean.getDataObject();
                list = (List)object;
                boolean isCostElementInactive = ((Boolean)list.get(1)).booleanValue();
                // When any of the cost elements(SUBCONTRACTOR_F_AND_A_LT_25K,SUBCONTRACTOR_F_AND_A_GT_25K,SUBCONTRACTOR_DIRECT_F_AND_A_LT_25K,SUBCONTRACTOR_DIRECT_F_AND_A_GT_25K) 
                // defined in the following parameter and its been used for the creation of the line item and inactive
                // Line item wont be created for the sub award which uses the inactive cost element
                if(isCostElementInactive){
                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(COST_ELEMENT_INACTIVE));
                }
            }
            // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - End
            if(success && !closeAfterSave) {
                deletedData = null;
                Object object = responderBean.getDataObject();
                if(object instanceof List) {
                    list = (List)object;
                    // Modified for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
//                    sync(list);
                    sync((List)list.get(0));
                    // Modified for COEUSQA-2115 Subaward budgeting for Proposal Development - End
                }
                subAwardBudgetTableModel.fireTableDataChanged();
                displayDetails();
                subAwardBudget.tblSubAwardBudget.setRowSelectionInterval(selectedRow, selectedRow);
            }
            
            if(closeAfterSave) {
                subAwardDlgWindow.dispose();
            }
        }catch (CoeusUIException coeusUIException) {
            subAwardBudget.tblSubAwardBudget.setRowSelectionInterval(coeusUIException.getTabIndex(), coeusUIException.getTabIndex());
            CoeusOptionPane.showDialog(coeusUIException);
            //Don't refresh here, since this exception is thrown by validate method
        }catch (Exception exception) {
            CoeusOptionPane.showErrorDialog(exception.getMessage());
            exception.printStackTrace();
            refresh(lstDataToSave, null, null);
        }
    }

    public void setBudgetBean(BudgetBean budgetBean) {
        this.budgetBean = budgetBean;
    }
    
    private void sync(List savedData) {
        BudgetSubAwardBean budgetSubAwardBean, savedbudgetSubAwardBean;
        int savedDataIndex = 0;
        
        String fileName;
        for(int index = 0; index < data.size(); index++) {
            budgetSubAwardBean = (BudgetSubAwardBean)data.get(index);
//            if(budgetSubAwardBean.getAcType() == null && budgetSubAwardBean.getPdfAcType() == null && budgetSubAwardBean.getXmlAcType() == null) {
//                    continue;
//            }//End IF
            
            savedbudgetSubAwardBean = (BudgetSubAwardBean)savedData.get(savedDataIndex);
            
            budgetSubAwardBean.setAcType(null);
            if(budgetSubAwardBean.getPdfAcType() != null) {
                if(savedbudgetSubAwardBean.getTranslationComments() != null &&
                        !savedbudgetSubAwardBean.getTranslationComments().equals(BudgetSubAwardConstants.XML_GENERATED_SUCCESSFULLY)){
                    //There was problem while generating XML
                    fileName = lstFileNames.get(0).toString();
                    savedbudgetSubAwardBean.setPdfFileName(fileName);
                }
                lstFileNames.remove(0);
                //So that 0th element would be next element
            }
            
            data.set(index, savedbudgetSubAwardBean);
            savedData.remove(0);
           
        }//End For
    }
    
    /**
     * this method refreshes the display.
     * is called if any error occurs after save.
     * @param lstDataToSave List of BudgetSubAwardBeans
     * @param list translation comments
     * @param timestamp timestamp of database operations(i.e. when the xml was generated, pdf saved)
     * @return string messahe to be displayed
     */
    private String refresh(List lstDataToSave, List list, Timestamp timestamp) {
        StringBuffer message = new StringBuffer();
        BudgetSubAwardBean budgetSubAwardBean;
        
        String str = null, fileName;
        for(int index = 0; index < lstDataToSave.size(); index++) {
            budgetSubAwardBean = (BudgetSubAwardBean)lstDataToSave.get(index);
            
            //No need to update/refresh Deleted beans
            if((budgetSubAwardBean.getAcType() != null && budgetSubAwardBean.getAcType().equals(TypeConstants.DELETE_RECORD))
            || (budgetSubAwardBean.getAcType() != null && !budgetSubAwardBean.getAcType().equals(TypeConstants.INSERT_RECORD) && budgetSubAwardBean.getPdfAcType() == null && budgetSubAwardBean.getXmlAcType() == null)){
                continue;
            }
            
            if(list != null && list.size() > 0) {
                str = list.get(index).toString();
                budgetSubAwardBean.setTranslationComments(str);
            }
            
            //If Beans are Inserted. update AwSubAwardNumber and timestamp.
            if(budgetSubAwardBean.getAcType() != null && budgetSubAwardBean.getAcType().equals(TypeConstants.INSERT_RECORD)) {
                budgetSubAwardBean.setAwSubAwardNumber(budgetSubAwardBean.getSubAwardNumber());
                budgetSubAwardBean.setUpdateTimestamp(timestamp);
            }
            
            if(str != null && str.equals(BudgetSubAwardConstants.XML_GENERATED_SUCCESSFULLY)){
                if(timestamp != null) {
                    if(budgetSubAwardBean.getAcType() != null) {
                        budgetSubAwardBean.setUpdateUser(mdiForm.getUserId());
                        budgetSubAwardBean.setUpdateTimestamp(timestamp);
                    }
                    if(budgetSubAwardBean.getPdfAcType() != null) {
                        budgetSubAwardBean.setPdfUpdateUser(mdiForm.getUserId());
                        budgetSubAwardBean.setPdfUpdateTimestamp(timestamp);
                        budgetSubAwardBean.setXmlUpdateUser(mdiForm.getUserId());
                        budgetSubAwardBean.setXmlUpdateTimestamp(timestamp);
                    }
                }
                budgetSubAwardBean.setAcType(null);
                budgetSubAwardBean.setPdfAcType(null);
                budgetSubAwardBean.setXmlAcType(null);
            }else {
                if(message.length() != 0) {
                    message.append("\n\n"); //Append Next Line and an Empty Line
                }
                message.append("Sub Award Num:"+budgetSubAwardBean.getSubAwardNumber());
                message.append(", File Name:");
                if(budgetSubAwardBean.getPdfAcType() != null) {
                    fileName = lstFileNames.get(0).toString();
                    lstFileNames.remove(0); //So that 0th element would be next element
                    if(timestamp != null) {
                        budgetSubAwardBean.setPdfUpdateUser(mdiForm.getUserId());
                        budgetSubAwardBean.setPdfUpdateTimestamp(timestamp);
                    }
                }else {
                    fileName = budgetSubAwardBean.getPdfFileName();
                }
                message.append(fileName);
                message.append("\n"+str);
                
                //budgetSubAwardBean.setAcType(null);
                //budgetSubAwardBean.setPdfAcType(TypeConstants.UPDATE_RECORD);
                budgetSubAwardBean.setPdfFileName(fileName);
            }
        }//End For
        
        //refresh view of selected Sub Award
        displayDetails();
        
        return message.toString();
    }
    
    public static void main(String s[]) {
        try{
            BudgetSubAwardController subAwardBudgetController = new BudgetSubAwardController(TypeConstants.MODIFY_MODE);
            List list = new ArrayList();
            subAwardBudgetController.setFormData(list);
            subAwardBudgetController.display();
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        try{
            subAwardDlgWindow.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            blockEvents(true);
            
            Object source = actionEvent.getSource();
            if(source.equals(subAwardBudget.chkDetails)) {
                boolean selected = subAwardBudget.chkDetails.isSelected();
                width = subAwardBudget.getWidth();
                height = subAwardBudget.getHeight();
                detailHeight = subAwardBudget.pnlDetails.getHeight();
                if(selected) {
                    //subAwardDlgWindow.setSize(width, height + detailHeight);
                    subAwardBudget.setPreferredSize(new Dimension(width, height + detailHeight));
                }else {
                    height = height - detailHeight;
                    subAwardDlgWindow.setSize(width, height);
                    subAwardBudget.setPreferredSize(new Dimension(width, height));
                }
                subAwardBudget.pnlDetails.setVisible(selected);
                subAwardDlgWindow.pack();
                displayDetails();
            }else if(source.equals(subAwardBudget.btnAdd)) {
                //subAwardBudgetTableModel.addRow();
                addRow();
            }else if(source.equals(subAwardBudget.btnDelete)) {
                int selectedRow = subAwardBudget.tblSubAwardBudget.getSelectedRow();
                if(selectedRow == -1) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_ROW_TO_DELETE));
                    return ;
                }else{
                    int selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(DELETE_ROW), CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
                    if(selection == CoeusOptionPane.SELECTION_YES) {
                        delete(selectedRow);
                        // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
                        int subAwardCount = subAwardBudget.tblSubAwardBudget.getRowCount();
                        if(subAwardCount == 0){
                            subAwardBudget.btnSubAwardDetails.setEnabled(false);
                        }
                        // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - End
                    }
                }
            }else if(source.equals(subAwardBudget.btnOk)) {
                save(true);
            }else if(source.equals(subAwardBudget.btnCancel)){
                close();
            }else if(source.equals(subAwardBudget.btnTranslate)) {
                //translate();
                save(false);
            }else if(source.equals(subAwardBudget.btnUploadPDF)) {
                uploadPdf();
            }else if(source.equals(subAwardBudget.btnViewPDF)) {
                displayContents(BudgetSubAwardConstants.PDF);
            }else if(source.equals(subAwardBudget.btnViewXML)) {
                displayContents(BudgetSubAwardConstants.XML);
            // Added for COEUSQA-2115 : Subaward budgeting for Proposal Development - Start
            }else if(source.equals(subAwardBudget.btnSubAwardDetails)) {
                if(getFunctionType() != TypeConstants.DISPLAY_MODE) {
                    save(false);
                }
                displaySubAwardDetails(getFunctionType());
            }
            // Added for COEUSQA-2115 : Subaward budgeting for Proposal Development - End
        }catch (Exception exception) {
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }finally{
            subAwardDlgWindow.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            blockEvents(false);
        }
    }
    
    int lastSelectedRow = -1;

    public void valueChanged(javax.swing.event.ListSelectionEvent listSelectionEvent) {
        if(!listSelectionEvent.getValueIsAdjusting()) {
            updateComments();
            displayDetails();
            //Added for Case#3404-Outstanding proposal hierarchy changes - Start
            if(isParentProposal()){
                updateParentLabel();
            }
            //Added for Case#3404-Outstanding proposal hierarchy changes - End
        }
    }
    
    //Added for Case#3404-Outstanding proposal hierarchy changes - Start
    /*
     * Displaying the child proposal Number from where the Sub Award is.
     * 
     */
    private void updateParentLabel(){
        if(cvHierarchyLinks != null && cvHierarchyLinks.size() >0){
            if(!removing){
                if(lastSelectedRow != -1 && lastSelectedRow != deletingRow) {
                    BudgetSubAwardBean prevBudgetSubAwardBean = (BudgetSubAwardBean)data.get(lastSelectedRow);
                    for(int index =0 ;index <cvHierarchyLinks.size();index++){
                        ProposalHierarchyLinkBean proposalHierarchyLinkBean = (ProposalHierarchyLinkBean)cvHierarchyLinks.get(index);
                        if(proposalHierarchyLinkBean.getParentModuleNumber() == prevBudgetSubAwardBean.getSubAwardNumber()){
                            subAwardBudget.lblParent.setText("   This attachment was uploaded from proposal number : "+proposalHierarchyLinkBean.getChildProposalNumber()+" and cannot be edited in this proposal");
                        }
                    }
                }
            }
        }
    }
    //Added for Case#3404-Outstanding proposal hierarchy changes - End
    /**
     * updates comments for sub award.
     */
    private void updateComments() {
        if(data == null || (data != null && data.size() == 0)) {
            //No Rows to update Comments.
            return ;
        }
        if(!removing){
            if(lastSelectedRow != -1 && lastSelectedRow != deletingRow) {
                BudgetSubAwardBean prevBudgetSubAwardBean = (BudgetSubAwardBean)data.get(lastSelectedRow);
                if((prevBudgetSubAwardBean.getComments() == null && subAwardBudget.txtArComments.getText().trim().length() > 0)
                || (prevBudgetSubAwardBean.getComments() != null && !prevBudgetSubAwardBean.getComments().equals(subAwardBudget.txtArComments.getText().trim()))) {
                    prevBudgetSubAwardBean.setComments(subAwardBudget.txtArComments.getText().trim());
                    if(prevBudgetSubAwardBean.getAcType() == null) {
                        prevBudgetSubAwardBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                }//End If comment updated
            }//End if Not Deleting Row
        }//End if not removing
    }
    
    /**
     * displays details panel and updates the information on the detail panel.
     */
    private void displayDetails(){
        //Set the Comments to previously selected Row
        if(!removing){
            int selectedRow = -1;
//            if(lastSelectedRow != -1 && lastSelectedRow != deletingRow) {
//                BudgetSubAwardBean prevBudgetSubAwardBean = (BudgetSubAwardBean)data.get(lastSelectedRow);
//                prevBudgetSubAwardBean.setComments(subAwardBudget.txtArComments.getText().trim());
//            }
            
            //Clear Details Screen before setting values
            subAwardBudget.pnlSubAwardLastUpdated.txtLastUpdated.setText(EMPTY_STRING);
            subAwardBudget.pnlPDFLastUpdated.txtLastUpdated.setText(EMPTY_STRING);
            subAwardBudget.pnlXMLLastUpdated.txtLastUpdated.setText(EMPTY_STRING);
            subAwardBudget.pnlSubAwardLastUpdated.txtUser.setText(EMPTY_STRING);
            subAwardBudget.pnlPDFLastUpdated.txtUser.setText(EMPTY_STRING);
            subAwardBudget.pnlXMLLastUpdated.txtUser.setText(EMPTY_STRING);
            subAwardBudget.lblNamespaceValue.setText(EMPTY_STRING);
            
            subAwardBudget.txtArStatus.setText(EMPTY_STRING);
            subAwardBudget.lstAttachments.setListData(new String[0]);
            subAwardBudget.lblPdfFileName.setText(EMPTY_STRING);
            
            selectedRow = subAwardBudget.tblSubAwardBudget.getSelectedRow();
            
            if(selectedRow == -1){
                return ;
            }
            
            BudgetSubAwardBean budgetSubAwardBean = (BudgetSubAwardBean)data.get(selectedRow);
            
            if(this.subAwardBudget.chkDetails.isSelected()) {
                DateUtils dateUtils = new DateUtils();
                if(budgetSubAwardBean.getUpdateTimestamp() != null){
                    subAwardBudget.pnlSubAwardLastUpdated.txtLastUpdated.setText(dateUtils.formatDate(budgetSubAwardBean.getUpdateTimestamp(), DATE_FORMAT));
                }
                /*
                 * UserId to UserName Enhancement - Start
                 * Added to avoid the repetetive database interaction for getting 
                 * the username instead of going to the database everytime mainting the values locally in HashMap
                 */
                //subAwardBudget.pnlSubAwardLastUpdated.txtUser.setText(budgetSubAwardBean.getUpdateUser());
                subAwardBudget.pnlSubAwardLastUpdated.txtUser.setText(UserUtils.getDisplayName(budgetSubAwardBean.getUpdateUser()));
                
                if(budgetSubAwardBean.getPdfUpdateTimestamp() != null){
                    subAwardBudget.pnlPDFLastUpdated.txtLastUpdated.setText(dateUtils.formatDate(budgetSubAwardBean.getPdfUpdateTimestamp(), DATE_FORMAT));
                }
                subAwardBudget.pnlPDFLastUpdated.txtUser.setText(UserUtils.getDisplayName(budgetSubAwardBean.getPdfUpdateUser()));
                
                if(budgetSubAwardBean.getXmlUpdateTimestamp() != null){
                    subAwardBudget.pnlXMLLastUpdated.txtLastUpdated.setText(dateUtils.formatDate(budgetSubAwardBean.getXmlUpdateTimestamp(), DATE_FORMAT));
                }
                //subAwardBudget.pnlXMLLastUpdated.txtUser.setText(budgetSubAwardBean.getXmlUpdateUser());
                subAwardBudget.pnlXMLLastUpdated.txtUser.setText(UserUtils.getDisplayName(budgetSubAwardBean.getXmlUpdateUser()));
                //UserId to UserName Enhancement End.
                
                subAwardBudget.txtArStatus.setText(budgetSubAwardBean.getTranslationComments());
                subAwardBudget.lblNamespaceValue.setText(budgetSubAwardBean.getNamespace());
            }//End if chk Details isSelected
            
            subAwardBudget.txtArComments.setText(budgetSubAwardBean.getComments());
            
            subAwardBudget.lblPdfFileName.setText(budgetSubAwardBean.getPdfFileName());
            
            //Set Attachments - START
            List lstAttachments = budgetSubAwardBean.getAttachments();
            if(lstAttachments != null && lstAttachments.size() > 0) {
                String arrContentId[] = new String[lstAttachments.size()];
                BudgetSubAwardAttachmentBean budgetSubAwardAttachmentBean;
                for(int index = 0; index < arrContentId.length; index++){
                    budgetSubAwardAttachmentBean = (BudgetSubAwardAttachmentBean)lstAttachments.get(index);
                    arrContentId[index] = budgetSubAwardAttachmentBean.getContentId();
                }
                subAwardBudget.lstAttachments.setListData(arrContentId);
            }
            //Set Attachments - END
            
            //Enable/Disable buttons for selected sub award - START
            /*boolean enableTranslate = false;
            if(budgetSubAwardBean.getXmlUpdateTimestamp() == null || budgetSubAwardBean.getPdfAcType() != null) {
                //XML is Generated and in Sync
                enableTranslate = true;
            }
            subAwardBudget.btnTranslate.setEnabled(enableTranslate);
            */
            
            boolean enableViewPdf = false;
            if(budgetSubAwardBean.getPdfFileName() != null) {
                enableViewPdf = true;
            }
            subAwardBudget.btnViewPDF.setEnabled(enableViewPdf);
            
            boolean enableViewXml = false;
            if(budgetSubAwardBean.getXmlUpdateTimestamp() != null) {
                enableViewXml = true;
            }
            subAwardBudget.btnViewXML.setEnabled(enableViewXml);
            //Enable/Disable buttons for selected sub award - END
            lastSelectedRow = selectedRow;
        }
    }
    
    /**
     * adds an empty row at the end of the sub award(s)
     */
    private void addRow() throws CoeusException{
        // PDF is not mandatory
//        String fileName = displayPDFFileDialog();
//        if(fileName == null) {
//           //Cancelled
//            return ;
//        }
        
        BudgetSubAwardBean budgetSubAwardBean = new BudgetSubAwardBean();
        budgetSubAwardBean.setProposalNumber(budgetBean.getProposalNumber());
        budgetSubAwardBean.setVersionNumber(budgetBean.getVersionNumber());
        budgetSubAwardBean.setAcType(TypeConstants.INSERT_RECORD);
//        budgetSubAwardBean.setPdfAcType(TypeConstants.INSERT_RECORD);
        budgetSubAwardBean.setSubAwardStatusCode(1);
        // PDF is not mandatory
//        budgetSubAwardBean.setPdfFileName(fileName);
        budgetSubAwardBean.setUpdateUser(userId);
      //COEUSQA-4061   
        CoeusVector cvBudgetPeriod = new CoeusVector(); 
        
        cvBudgetPeriod = getBudgetPeriodData(budgetSubAwardBean.getProposalNumber(),budgetSubAwardBean.getVersionNumber()); 
        
     //COEUSQA-4061
        int rowIndex = 0;
        if(data == null || data.size() == 0) {
            budgetSubAwardBean.setSubAwardNumber(1);
            // Added for COEUSQA-2115 : Subaward budgeting for Proposal Development - Start
            // Sub Award Details button will be enabled only when the periods are generated or budget has one period
            if(isPeriodsGenerated() || cvBudgetPeriod.size() == 1){
                subAwardBudget.btnSubAwardDetails.setEnabled(true);
            }
            // Added for COEUSQA-2115 : Subaward budgeting for Proposal Development - End
        }else{
            rowIndex = data.size();
            BudgetSubAwardBean lastBean = (BudgetSubAwardBean)data.get(rowIndex - 1);
            budgetSubAwardBean.setSubAwardNumber(lastBean.getSubAwardNumber() + 1);
        }
        if(!subAwardBudget.isEnabled()){
            subAwardBudget.btnSubAwardDetails.setEnabled(true);
        }
        data.add(budgetSubAwardBean);
        subAwardBudgetTableModel.fireTableRowsInserted(rowIndex, rowIndex);
        subAwardBudget.tblSubAwardBudget.setRowSelectionInterval(rowIndex, rowIndex);
        
    }
    
    /**
     * deletes the selected row
     * @param selectedRow row to be deleted
     */
    private void delete(int selectedRow) {
        removing = true;
        deletingRow = selectedRow;
        //subAwardBudgetTableModel.deleteRow(selectedRow);
        deleteRow(selectedRow);
        
        //Select a Row
        int selectRow = 0;
        int rowCount = subAwardBudgetTableModel.getRowCount();
        if(selectedRow == 0 && rowCount > 0) {
            //Select First Row
            selectRow = 0;
        }else if(selectedRow == rowCount) {
            //Select Last Row
            selectRow = rowCount - 1;
        }else {
            //Select This Row
            selectRow = selectedRow;
        }
        removing = false;
        if(selectRow != -1) {
            subAwardBudget.tblSubAwardBudget.setRowSelectionInterval(selectRow, selectRow);
        }else{
            //If All rows Deleted, then Details panel should be cleared
            displayDetails();
        }
        deletingRow = -1;
    }
    
    /**
     * deletes the selected row.
     * @param selectedRow row to be deleted.
     */
    private void deleteRow(int selectedRow){
        BudgetSubAwardBean budgetSubAwardBean = (BudgetSubAwardBean)data.get(selectedRow);
        if(budgetSubAwardBean.getAcType() == null || budgetSubAwardBean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
            budgetSubAwardBean.setAcType(TypeConstants.DELETE_RECORD);
            budgetSubAwardBean.setUpdateUser(userId);
            if(deletedData ==null) {
                deletedData = new ArrayList();
            }
            deletedData.add(budgetSubAwardBean);
        }
        data.remove(selectedRow);
        
        //If Last Row nothing to do
        //else Update Sub Award Number for rest of the Rows
        /*int size = data.size();
        if(selectedRow != size) {
            for(int index = selectedRow; index < size; index++) {
                budgetSubAwardBean = (BudgetSubAwardBean)data.get(index);
                budgetSubAwardBean.setSubAwardNumber(index + 1);
                if(budgetSubAwardBean.getAcType() == null) {
                    budgetSubAwardBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
            }//End For
        }//End If
        */
        
        subAwardBudgetTableModel.fireTableRowsDeleted(selectedRow, selectedRow);
    }//End Method Delete Row
    
    private void displayContents(String file)throws CoeusException, MalformedURLException{
        int selectedRow = subAwardBudget.tblSubAwardBudget.getSelectedRow();
        if(selectedRow == -1) {
            CoeusOptionPane.showInfoDialog("Select a Row");
            return ;
        }
        BudgetSubAwardBean budgetSubAwardBean= (BudgetSubAwardBean)this.data.get(selectedRow);
        if(file.equals(BudgetSubAwardConstants.PDF) && budgetSubAwardBean.getPdfAcType() != null) {
            //PDF Updated. open file from local filesystem
            File fileObj = new File(budgetSubAwardBean.getPdfFileName());
            URL url = fileObj.toURL();
            URLOpener.openUrl(url);
        }else {
            displayContents(file, budgetSubAwardBean);
        }
    }
    
    /**
     * displayes the contents of the PDF or XMl file
     * @param content specified either XML or PDF
     * @param budgetSubAwardBean budget sub award bean
     */
    private void displayContents(String file, BudgetSubAwardBean budgetSubAwardBean){
        try {
            subAwardDlgWindow.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            blockEvents(true);
            
            DocumentBean documentBean = new DocumentBean();
            Map map = new HashMap();
            map.put(BudgetSubAwardConstants.PROPOSAL_NUMBER, budgetSubAwardBean.getProposalNumber());
            map.put(BudgetSubAwardConstants.VERSION_NUMBER, ""+budgetSubAwardBean.getVersionNumber());
            map.put(BudgetSubAwardConstants.SUB_AWARD_NUM, ""+budgetSubAwardBean.getSubAwardNumber());
            map.put(BudgetSubAwardConstants.FILE, file);
            map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.budget.report.SubAwardDocumentReader");
            documentBean.setParameterMap(map);
            
            RequesterBean requesterBean = new RequesterBean();
            requesterBean.setDataObject(documentBean);
            requesterBean.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
            
            AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
            appletServletCommunicator.setConnectTo(streaming);
            appletServletCommunicator.setRequest(requesterBean);
            appletServletCommunicator.send();
            ResponderBean responderBean = appletServletCommunicator.getResponse();
            
            Object data = null;
            if(responderBean.hasResponse()) {
                data = responderBean.getDataObject();
                if(data != null && data instanceof Map) {
                   map = (Map)data;
                   String url = (String)map.get(DocumentConstants.DOCUMENT_URL);
                   URL urlObj = new URL(url);
                   URLOpener.openUrl(urlObj);
                }
            }
            
            //URLOpener.openUrl(((JTextField)source).getText());
            
        } catch (Exception coeusException) {
            CoeusOptionPane.showErrorDialog(coeusException.getMessage() == null ? "Error Occured" : coeusException.getMessage());
        }finally{
            subAwardDlgWindow.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            blockEvents(false);
        }
    }
    
    private void displayAttachment(BudgetSubAwardAttachmentBean budgetSubAwardAttachmentBean) {
        try {
            subAwardDlgWindow.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            blockEvents(true);
            
            DocumentBean documentBean = new DocumentBean();
            Map map = new HashMap();
            map.put(BudgetSubAwardConstants.PROPOSAL_NUMBER, budgetSubAwardAttachmentBean.getProposalNumber());
            map.put(BudgetSubAwardConstants.VERSION_NUMBER, ""+budgetSubAwardAttachmentBean.getVersionNumber());
            map.put(BudgetSubAwardConstants.SUB_AWARD_NUM, ""+budgetSubAwardAttachmentBean.getSubAwardNumber());
            map.put(BudgetSubAwardConstants.CONTENT_ID, ""+budgetSubAwardAttachmentBean.getContentId());
            map.put(BudgetSubAwardConstants.FILE, BudgetSubAwardConstants.ATTACHMENT);
            map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.budget.report.SubAwardDocumentReader");
            documentBean.setParameterMap(map);
            
            RequesterBean requesterBean = new RequesterBean();
            requesterBean.setDataObject(documentBean);
            requesterBean.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
            
            AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
            appletServletCommunicator.setConnectTo(streaming);
            appletServletCommunicator.setRequest(requesterBean);
            appletServletCommunicator.send();
            ResponderBean responderBean = appletServletCommunicator.getResponse();
            
            Object data = null;
            if(responderBean.hasResponse()) {
                data = responderBean.getDataObject();
                if(data != null && data instanceof Map) {
                   map = (Map)data;
                   String url = (String)map.get(DocumentConstants.DOCUMENT_URL);
                   URL urlObj = new URL(url);
                   URLOpener.openUrl(urlObj);
                }
            }
            
            //URLOpener.openUrl(((JTextField)source).getText());
            
        } catch (Exception coeusException) {
            CoeusOptionPane.showErrorDialog(coeusException.getMessage() == null ? "Error Occured" : coeusException.getMessage());
        }finally{
            subAwardDlgWindow.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            blockEvents(false);
        }
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 2) {
            //Display Attachment in browser
            int selectedIndex;
            selectedIndex = subAwardBudget.tblSubAwardBudget.getSelectedRow();
            BudgetSubAwardBean budgetSubAwardBean = (BudgetSubAwardBean)data.get(selectedIndex);
            selectedIndex = subAwardBudget.lstAttachments.getSelectedIndex();
            BudgetSubAwardAttachmentBean budgetSubAwardAttachmentBean = (BudgetSubAwardAttachmentBean)budgetSubAwardBean.getAttachments().get(selectedIndex);
            displayAttachment(budgetSubAwardAttachmentBean);
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
    
    /*
    private void translate() throws CoeusUIException, CoeusException, IOException {
        int selectedRow = subAwardBudget.tblSubAwardBudget.getSelectedRow();
        if(selectedRow == -1) {
            CoeusOptionPane.showInfoDialog("Select a Row");
            return ;
        }
        
        subAwardBudgetEditor.stopCellEditing();
        BudgetSubAwardBean budgetSubAwardBean = (BudgetSubAwardBean)data.get(selectedRow);
        String strComments = subAwardBudget.txtArComments.getText().trim();
        if((budgetSubAwardBean.getComments() == null && strComments.length() > 0) ||
                (budgetSubAwardBean.getComments() != null && !budgetSubAwardBean.getComments().equals(strComments))) {
            budgetSubAwardBean.setComments(subAwardBudget.txtArComments.getText().trim());
            if(budgetSubAwardBean.getAcType() == null) {
                budgetSubAwardBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
        }
        
        validate(budgetSubAwardBean, selectedRow);
        
        FileInputStream fileInputStream = new FileInputStream(budgetSubAwardBean.getPdfFileName());
        byte byteData[] = new byte[fileInputStream.available()];
        fileInputStream.read(byteData);
        budgetSubAwardBean.setSubAwardPDF(byteData);
        //Store filename in a variable incase save fails
        //the filename is used to retry save
        String fileName = budgetSubAwardBean.getPdfFileName();
        
        budgetSubAwardBean.setPdfFileName(new File(budgetSubAwardBean.getPdfFileName()).getName());
        
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setDataObject(budgetSubAwardBean);
        //requesterBean.setFunctionType(S2SConstants.GET_XML_FROM_PURE_EDGE);
        //AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(S2S_SERVLET, requesterBean);
        
        requesterBean.setFunctionType(BudgetSubAwardConstants.TRANSLATE);
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(connect, requesterBean);
        appletServletCommunicator.send();
        
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        try{
            boolean success = responderBean.hasResponse();
            List list = null;
            Timestamp timestamp = null;
            if(success) {
                deletedData = null;
                budgetSubAwardBean = (BudgetSubAwardBean)responderBean.getDataObject();
                budgetSubAwardBean.setAcType(null);
                if(budgetSubAwardBean.getTranslationComments() != null &&
                    budgetSubAwardBean.getTranslationComments().equals(BudgetSubAwardConstants.XML_GENERATED_SUCCESSFULLY)) {
                    budgetSubAwardBean.setXmlAcType(null);
                }else {
                    //WebService returned with error
                    CoeusOptionPane.showErrorDialog(budgetSubAwardBean.getTranslationComments());
                }
                budgetSubAwardBean.setPdfAcType(null);
                data.set(selectedRow, budgetSubAwardBean);
                subAwardBudgetTableModel.fireTableRowsUpdated(selectedRow, selectedRow);
                displayDetails();
            }else {
                budgetSubAwardBean.setPdfFileName(fileName);
            }
        }catch (CoeusException coeusException) {
            //if response was not suceesful
            budgetSubAwardBean.setPdfFileName(fileName);
            throw coeusException;
        }
        
    }
    */
        
    private void uploadPdf() {
        int selectedRow = subAwardBudget.tblSubAwardBudget.getSelectedRow();
        if(selectedRow == -1) {
            CoeusOptionPane.showInfoDialog("Select a Row");
            return ;
        }
        String fileName = displayPDFFileDialog();
        if(fileName == null) {
           //Cancelled
            return ;
        }
        BudgetSubAwardBean budgetSubAwardBean = (BudgetSubAwardBean)data.get(selectedRow);
        budgetSubAwardBean.setPdfFileName(fileName);
        budgetSubAwardBean.setPdfAcType(TypeConstants.UPDATE_RECORD);
        subAwardBudget.btnTranslate.setEnabled(true);
        subAwardBudgetTableModel.fireTableRowsUpdated(selectedRow, selectedRow);
    }
    
    /**
     * returns an selected PDF file.
     * returns null if user cancels the file selction.
     */
    private String displayPDFFileDialog() {
        String fileName = null;
        if(fileChooser == null) {
            // COEUSQA-1925: Narrative upload recall the last folder from which an upload Can't be done - Start
//            fileChooser = new JFileChooser();
            fileChooser = new CoeusFileChooser(CoeusGuiConstants.getMDIForm());
            if(budgetSubAwardFileFilter == null) {
                budgetSubAwardFileFilter = new BudgetSubAwardFileFilter();
            }
            fileChooser.setFileFilter(budgetSubAwardFileFilter);
            fileChooser.setAcceptAllFileFilterUsed(false);
        }
//        int selection = fileChooser.showOpenDialog(subAwardBudget);
        fileChooser.showFileChooser();
        if(fileChooser.isFileSelected()){
      //  if(selection == JFileChooser.APPROVE_OPTION) {
//            File file = fileChooser.getSelectedFile();
              File file = fileChooser.getFileName();
              // COEUSQA-1925: Narrative upload recall the last folder from which an upload Can't be done - End
            fileName = file.getAbsolutePath();
        }
        return fileName;
    }
    
    //Inner Class - START
    
    class BudgetSubAwardEditor extends DefaultCellEditor implements ActionListener{
        
        private List data;
        
        private JPanel pnlEditor;
        private JTextField txtFile;
        private JButton btnBrowse;
        private JButton btnDisplayPDF;
        private JButton btnDisplayXML;
        
        private int editingColumn;
        private int editingRow;
        private String fileName;
        
        public BudgetSubAwardEditor() {
            super(new JTextField());
            pnlEditor = new JPanel();
            pnlEditor.setLayout(new BorderLayout(0,0));
            JPanel pnlButtons = new JPanel();
            pnlButtons.setLayout(new BorderLayout(0,0));
            
            txtFile = new JTextField();
            txtFile.setEditable(false);
            txtFile.setBackground(Color.white);
            btnBrowse = new JButton("...");
            btnBrowse.setToolTipText("Browse");
            btnBrowse.setPreferredSize(new Dimension(15, btnBrowse.getHeight()));
            btnDisplayPDF = new JButton(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.DISPLAY_ICON)));
            btnDisplayPDF.setToolTipText("Display PDF");
            btnDisplayPDF.setPreferredSize(new Dimension(15, btnDisplayPDF.getHeight()));
            pnlEditor.add(txtFile, BorderLayout.CENTER);
            
            pnlButtons.add(btnBrowse, BorderLayout.WEST);
            pnlButtons.add(btnDisplayPDF, BorderLayout.EAST);
            
            pnlEditor.add(pnlButtons, BorderLayout.EAST);
            
            btnDisplayXML = new JButton("View XML", new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.DISPLAY_ICON)));
            btnDisplayXML.setToolTipText("Display XML");
            
            btnBrowse.addActionListener(this);
            btnDisplayPDF.addActionListener(this);
            btnDisplayXML.addActionListener(this);
        }
        
        public void setData(List data){
            this.data = data;
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            Component retValue;
            editingColumn = column;
            editingRow = row;
            if(column == BudgetSubAwardController.PDF_COLUMN){
                fileName = null;
                BudgetSubAwardBean budgetSubAwardBean = (BudgetSubAwardBean)data.get(row);
                txtFile.setText(budgetSubAwardBean.getPdfFileName());
                return pnlEditor;
            }else if(column == BudgetSubAwardController.XML_COLUMN) {
                BudgetSubAwardBean budgetSubAwardBean = (BudgetSubAwardBean)data.get(row);
                 boolean enable =   (budgetSubAwardBean.getXmlUpdateTimestamp() != null && budgetSubAwardBean.getXmlAcType() == null);
                //XML is Generated and in Sync
                btnDisplayXML.setEnabled(enable);
                return btnDisplayXML;
            }
            retValue = super.getTableCellEditorComponent(table, value, isSelected, row, column);
            return retValue;
        }
        
        public int getClickCountToStart() {
            return 1;
        }
        
        public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
            Object source = actionEvent.getSource();
            if(source.equals(btnBrowse)) {
                // COEUSQA-1925: Narrative upload recall the last folder from which an upload Can't be done - Start
//                if(fileChooser == null) {
//                    fileChooser = new JFileChooser();
//                    if(budgetSubAwardFileFilter == null) {
//                        budgetSubAwardFileFilter = new BudgetSubAwardFileFilter();
//                    }
//                    fileChooser.setFileFilter(budgetSubAwardFileFilter);
//                    fileChooser.setAcceptAllFileFilterUsed(false);
//                }
//                int selection = fileChooser.showOpenDialog(pnlEditor);
//                if(selection == JFileChooser.APPROVE_OPTION) {
//                    File file = fileChooser.getSelectedFile();
//                    fileName = file.getAbsolutePath();
//                    BudgetSubAwardBean budgetSubAwardBean = (BudgetSubAwardBean)data.get(editingRow);
//                    budgetSubAwardBean.setPdfAcType(TypeConstants.UPDATE_RECORD);
//                }
//                this.stopCellEditing();
                // COEUSQA-1925: Narrative upload recall the last folder from which an upload Can't be done - End
            }else if(source.equals(btnDisplayPDF)) {
                BudgetSubAwardBean budgetSubAwardBean = (BudgetSubAwardBean)data.get(editingRow);
                displayContents(BudgetSubAwardConstants.PDF, budgetSubAwardBean);
            }else if(source.equals(btnDisplayXML)) {
                BudgetSubAwardBean budgetSubAwardBean = (BudgetSubAwardBean)data.get(editingRow);
                displayContents(BudgetSubAwardConstants.XML, budgetSubAwardBean);
            }
        }
        
        public Object getCellEditorValue() {
            Object retValue;
            if(editingColumn == BudgetSubAwardController.PDF_COLUMN) {
                return fileName;
            }
            retValue = super.getCellEditorValue();
            return retValue;
        }
        
    }//End SubAwardBudgetEditor
    
    // Added for COEUSQA-2115 : Subaward budgeting for Proposal Development - Start
    /**
     * Method to display the sub award details for the periods
     * @param functionType 
     */
    private void displaySubAwardDetails(char functionType) throws CoeusException{
        if(subAwardBudget.tblSubAwardBudget.getCellEditor() != null){
            subAwardBudget.tblSubAwardBudget.getCellEditor().stopCellEditing();
        }
        int selectedRow = subAwardBudget.tblSubAwardBudget.getSelectedRow();
        if(selectedRow > -1){
            BudgetSubAwardBean budgetSubAward = (BudgetSubAwardBean)data.get(selectedRow);
            // Sub Award details window will be opened only when organization name is provided
            if(budgetSubAward.getOrganizationName() == null || CoeusGuiConstants.EMPTY_STRING.equals(budgetSubAward.getOrganizationName().trim())){
                // This validation is fired during save itself
//                CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(ENTER_ORGANIZATION_NAME));
            }else{
                Vector vecSubAwardPeriodDetails = budgetSubAward.getSubAwardPeriodDetails();
                // Check the sub award has any entry in the database and set it to the form,
                // other wise based on the generated periods sub award period collection will be created
                if(vecSubAwardPeriodDetails == null || (vecSubAwardPeriodDetails != null && vecSubAwardPeriodDetails.isEmpty())){
                    vecSubAwardPeriodDetails = new Vector();
                    Equals eqNull = new Equals("acType", null);
                    String queryKey = budgetBean.getProposalNumber()+budgetBean.getVersionNumber();
                    QueryEngine queryEngine = QueryEngine.getInstance();
                    vecBudgetPeriods = queryEngine.getDetails(queryKey, BudgetPeriodBean.class);
                    GreaterThan periodGreaterThanOne = new GreaterThan("budgetPeriod",new Integer(1));
                    CoeusVector vecBudgetDetails = queryEngine.executeQuery(queryKey, BudgetDetailBean.class, periodGreaterThanOne);
                    if(vecBudgetPeriods != null && !vecBudgetPeriods.isEmpty()){
                        for(Object budgetPeriodDetails : vecBudgetPeriods){
                            BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean)budgetPeriodDetails;
                            BudgetSubAwardDetailBean subAwardDetailBean = new BudgetSubAwardDetailBean();
                            subAwardDetailBean.setBudgetPeriod(budgetPeriodBean.getBudgetPeriod());
                            subAwardDetailBean.setVersionNumber(budgetPeriodBean.getVersionNumber());
                            subAwardDetailBean.setPeriodStartDate(budgetPeriodBean.getStartDate());
                            subAwardDetailBean.setPeriodEndDate(budgetPeriodBean.getEndDate());
                            subAwardDetailBean.setAcType(TypeConstants.INSERT_RECORD);
                            vecSubAwardPeriodDetails.add(subAwardDetailBean);
                        }
                    }
                }else if(vecSubAwardPeriodDetails != null && !vecSubAwardPeriodDetails.isEmpty()){
                    Equals eqNull = new Equals("acType", null);
                    String queryKey = budgetBean.getProposalNumber()+budgetBean.getVersionNumber();
                    QueryEngine queryEngine = QueryEngine.getInstance();
                    vecBudgetPeriods = queryEngine.getDetails(queryKey, BudgetPeriodBean.class);
                    
                    if(vecBudgetPeriods!= null && (vecBudgetPeriods.size() != vecSubAwardPeriodDetails.size())){
                        for(Object periodDetail : vecBudgetPeriods){
                            BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean)periodDetail;
                            int budgetPeriod = budgetPeriodBean.getBudgetPeriod();
                            boolean hasSubAwardDetails = false;
                            for(Object subAwardDetail : vecSubAwardPeriodDetails){
                                BudgetSubAwardDetailBean subAwardDetailBean = (BudgetSubAwardDetailBean)subAwardDetail;
                                if(budgetPeriod == subAwardDetailBean.getBudgetPeriod()){
                                    hasSubAwardDetails = true;
                                    break;
                                }
                            }
                            if(!hasSubAwardDetails){
                                BudgetSubAwardDetailBean subAwardDetailBean = new BudgetSubAwardDetailBean();
                                subAwardDetailBean.setBudgetPeriod(budgetPeriodBean.getBudgetPeriod());
                                subAwardDetailBean.setVersionNumber(budgetPeriodBean.getVersionNumber());
                                subAwardDetailBean.setPeriodStartDate(budgetPeriodBean.getStartDate());
                                subAwardDetailBean.setPeriodEndDate(budgetPeriodBean.getEndDate());
                                subAwardDetailBean.setAcType(TypeConstants.INSERT_RECORD);
                                vecSubAwardPeriodDetails.add(subAwardDetailBean);
                            }
                        }
                    }
                }
                
                budgetSubAward.setSubAwardPeriodDetails(vecSubAwardPeriodDetails);
                subAwardDetailController = new BudgetSubAwardDetailController(getFunctionType(),budgetSubAward);
                subAwardDetailController.setFormData(vecSubAwardPeriodDetails);
                subAwardDetailController.display();
            }
        }
    }
    private CoeusVector getBudgetPeriodData (String proposalNumber , int versionNumber) throws CoeusException{
            CoeusVector cvBudgetPeriod = new CoeusVector(); 
            RequesterBean requester = new RequesterBean();
            ResponderBean responder = null;
            requester.setFunctionType(GET_BUDGET_PERIOD);
            Vector vecParameter = new Vector();
            vecParameter.add(proposalNumber);
            vecParameter.add(new Integer(versionNumber));
            requester.setDataObjects(vecParameter);
            AppletServletCommunicator comm
                    = new AppletServletCommunicator(ConnectBudgetServlet, requester);
            comm.send();
            responder = comm.getResponse();
            if(responder.isSuccessfulResponse()){
                cvBudgetPeriod = (CoeusVector) responder.getDataObjects();            
            } 
            if( cvBudgetPeriod == null){
                return new CoeusVector();
            }
              return cvBudgetPeriod;
    }   
    //Inner Class - END

    /**
     * Method to check periods are generated
     * @return isPeriodsGenerated
     */
    public boolean isPeriodsGenerated() {
        return isPeriodsGenerated;
    }

    /**
     * Method to set whether period is generated
     * @param isPeriodsGenerated 
     */
    public void setPeriodsGenerated(boolean isPeriodsGenerated) {
        this.isPeriodsGenerated = isPeriodsGenerated;
    }

    /**
     * Method to get whether budget to be refreshed
     * @return refreshBudgetDetails
     */
    public boolean isRefreshBudgetDetails() {
        return refreshBudgetDetails;
    }

    /**
     * Method to set whether budget to be refreshed
     * @param refreshBudgetDetails 
     */
    public void setRefreshBudgetDetails(boolean refreshBudgetDetails) {
        this.refreshBudgetDetails = refreshBudgetDetails;
    }
 

    
}//End SubAwardBudgetController



class BudgetSubAwardTableModel extends DefaultTableModel{
    
    private List data;
    // JM 6-25-2013 added organization id and dropdown for name; new instance of controller
    private String columnNames[] = {"No", "Organization Name", "Form Name", "PDF", "XML", ""};
    private Vector organizations;
    // JM END
    private char mode;
    private String userId;
    
    // JM 6-28-2013 updated initiation to pass in orgs
    public BudgetSubAwardTableModel(char mode, String userId, Vector organizations){
        this.mode = mode;
        this.userId = userId;
        // JM 6-28-2013 need controller
        this.organizations = organizations;
        // JM END
    }
    
    // JM 6-27-2012 method to set organization ID from name selected
    public String getOrganizationId(String orgName) {
    	String orgId = "";
        HashMap map;
    	for(int g=0; g<this.organizations.size(); g++) {
        	map = (HashMap) this.organizations.get(g);
        	if (orgName.equals(map.get("LOCATION_NAME"))) {
        		orgId = (String) map.get("ORGANIZATION_ID");
        	}
        }
        return orgId;
    }
    
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        BudgetSubAwardBean budgetSubAwardBean = (BudgetSubAwardBean)data.get(rowIndex);
        switch (columnIndex) {
            case BudgetSubAwardController.ORGANIZATION_NAME_COLUMN:
                if(budgetSubAwardBean.getOrganizationName() != null && budgetSubAwardBean.getOrganizationName().equals(value.toString())) {
                    return ;
                }
                budgetSubAwardBean.setOrganizationName(value.toString());
                // JM 6-28-2013 added to set org ID
                budgetSubAwardBean.setOrganizationId(getOrganizationId(value.toString()));
                // JM END
                if(budgetSubAwardBean.getAcType() == null) {
                    budgetSubAwardBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
                budgetSubAwardBean.setUpdateUser(userId);
                break;
            case BudgetSubAwardController.PDF_COLUMN:
                if(value == null) {
                    return ;
                }
                budgetSubAwardBean.setPdfFileName(value.toString());
                if(budgetSubAwardBean.getPdfAcType() == null) {
                    budgetSubAwardBean.setPdfAcType(TypeConstants.UPDATE_RECORD);
                }
                budgetSubAwardBean.setPdfUpdateUser(userId);
                
                break;
        }
    }
    
    public int getRowCount() {
        if(data == null) return 0;
        return data.size();
    }
    
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if(mode == TypeConstants.DISPLAY_MODE) {
            return false;
        }
        
        if(columnIndex == BudgetSubAwardController.ORGANIZATION_NAME_COLUMN){// || columnIndex == BudgetSubAwardController.PDF_COLUMN || columnIndex == BudgetSubAwardController.XML_COLUMN){
            return true;
        }
        return false;
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        BudgetSubAwardBean budgetSubAwardBean = (BudgetSubAwardBean)data.get(rowIndex);
        
        switch (columnIndex) {
            case BudgetSubAwardController.SUB_AWARD_NUM_COLUMN:
                //return ""+budgetSubAwardBean.getSubAwardNumber();
                return ""+(rowIndex + 1);
            case BudgetSubAwardController.ORGANIZATION_NAME_COLUMN:
                return budgetSubAwardBean.getOrganizationName();
            case BudgetSubAwardController.FORM_NAME_COLUMN:
                return budgetSubAwardBean.getFormName();
            case BudgetSubAwardController.PDF_COLUMN:
                return new Boolean(budgetSubAwardBean.getPdfFileName() != null);
            case BudgetSubAwardController.XML_COLUMN:
                //return new Boolean(budgetSubAwardBean.getXmlUpdateTimestamp() != null && budgetSubAwardBean.getXmlAcType() == null);
                return budgetSubAwardBean;
            // JM 6-28-2013 added for organization ID
            case BudgetSubAwardController.ORGANIZATION_ID_COL:
                return budgetSubAwardBean.getOrganizationId();
            // JM END
        }
        
        return null;
    }
    
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }
    
    public Class getColumnClass(int columnIndex) {
        return String.class;
    }
    
    public int getColumnCount() {
        return columnNames.length;
    }
    
    /**
     * Getter for property data.
     * @return Value of property data.
     */
    public java.util.List getData() {
        return data;
    }
    
    /**
     * Setter for property data.
     * @param data New value of property data.
     */
    public void setData(java.util.List data) {
        this.data = data;
    }
}//End SubAwardBudgetTableModel

class BudgetSubAwardRenderer extends DefaultTableCellRenderer {
    
    private JLabel lblSaved, lblNotSaved;
    
    public BudgetSubAwardRenderer() {
        ImageIcon savedImageIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.COMPLETE_ICON));
        ImageIcon notSavedImageIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.NONE_ICON));
        lblSaved = new JLabel(savedImageIcon);
        lblNotSaved = new JLabel(notSavedImageIcon);
    }
    
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if(column == BudgetSubAwardController.PDF_COLUMN && value instanceof Boolean){
            Boolean bool = (Boolean)value;
            if(bool.booleanValue() == true){
                return lblSaved;
            }else {
                lblNotSaved.setText(BudgetSubAwardController.EMPTY_STRING);
                return lblNotSaved;
            }
        }else if(column == BudgetSubAwardController.XML_COLUMN && value instanceof BudgetSubAwardBean) {
            BudgetSubAwardBean budgetSubAwardBean = (BudgetSubAwardBean)value;
            if(budgetSubAwardBean.getXmlUpdateTimestamp() != null && budgetSubAwardBean.getPdfAcType() == null) {
                //XML is Generated and in Sync
                return lblSaved;
            }else {
                //Either XML not generated or Not in Sync with PDF
                //if(budgetSubAwardBean.getXmlAcType() == null || budgetSubAwardBean.getXmlUpdateTimestamp() == null) {
                if(budgetSubAwardBean.getXmlUpdateTimestamp() == null) {
                    lblNotSaved.setText("Not Generated");
                }else {
                    lblNotSaved.setText("Out of Sync");
                }
                return lblNotSaved;
            }
        }
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
    
}//End SubAwardBudgetRenderer

class BudgetSubAwardFileFilter extends FileFilter{
    public boolean accept(File file) {
        if(file.getName().endsWith("pdf") || file.isDirectory()) {
            return true;
        }
        return false;
    }
    
    public String getDescription() {
        return "PDF Files";
    }
    
}
