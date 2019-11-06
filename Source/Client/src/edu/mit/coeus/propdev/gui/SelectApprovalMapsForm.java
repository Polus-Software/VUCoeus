/*
 * SelectApprovalMapsForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on December 24, 2003, 2:58 PM
 */

package edu.mit.coeus.propdev.gui;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Collections;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableModel;
import javax.swing.AbstractAction;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.BorderFactory;
import javax.swing.ListSelectionModel;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.ObjectCloner;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalApprovalMapBean;
import edu.mit.coeus.propdev.bean.ProposalApprovalBean;
import edu.mit.coeus.propdev.bean.UnitMapDetailsBean;
import edu.mit.coeus.propdev.bean.UnitMapBean;

import edu.mit.coeus.bean.UserRolesInfoBean;
import edu.mit.coeus.bean.RoleInfoBean;
import edu.mit.coeus.bean.RoleRightInfoBean;
import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceFormBean;




/** SelectApprovalMapsForm.java
 * @author ranjeeva
 */
public class SelectApprovalMapsForm extends javax.swing.JPanel implements TypeConstants,ActionListener {
    
    /** CoeusDlgWindow instance */
    public CoeusDlgWindow dlgApprovalMapsForm;
    /** Modal flag for this window */
    boolean modal;
    /** Parent Componet instance for which this window is a Dialog */
    private Component parent;
    /** Title of this Modal Window */
    private String title = "Approval Maps";
    /** static variable for functiontype while server side call */
    private static final char GET_APPROVAL_MAPS = 'C';
    /** static variable for RED color */
    private static final Color RED_COLOR = Color.RED;
    /** static variable for BLUE color */
    private static final Color BLUE_COLOR  = Color.BLUE;
    /** flag to check is any Map Selected */
    private boolean isAnyMapSelected;
    
    
    /** holds CoeusMessageResources instance used for reading message Properties. */
    private CoeusMessageResources coeusMessageResources;
    
    /** ProposalDevelopmentFormBean instance to get Sponsor details
     * and for saving the Approval Maps
     */
    private ProposalDevelopmentFormBean proposalDevelopmentFormBean;
    /** ProposalDetailAdminForm for setting save Required flag */
    private ProposalDetailAdminForm proposalDetailAdminForm;
    
    /** proposalNumber instance of the Current Approval Map */
    private String proposalNumber;
    /** unitNumber instance to fetch UnitMaps details */
    private String unitNumber;
    /** current functionType while opening this Form :New/Add/Modify etc */
    private char functionType;
    
    /** Vector holding the parameters passed to Form from ProposalDetailForm */
    private Vector parameters;
    /** Vector holding all unitmaps applicable for this Proposal */
    private Vector allUnitMaps;
    
    /** Vector holding ProposalApprovalMapBean for Panel Rows */
    private Vector vectorOfbean =  new Vector();
    
    /** Vector holding all ProposalApprovalMapBean in the ProposalDevelopmentFormBean instance */
    private CoeusVector vectorProposalApprovalMapBean =  new CoeusVector();
    /** Flag isNewApprovalMapAdded for any New Approval Map selected */
    boolean isNewApprovalMapAdded;
    
    /** Holds Current Selected ProposalApprovalMapBean instance */
    private ProposalApprovalMapBean proposalApprovalMapBean;
    
    private ProposalApprovalMapBean oldApprovalMapBean;
    /** isRoleRightEnabled Flag indicating RoleRights for RIGHT_ID */
    private boolean isRoleRightEnabled;
    /** isSaveRequired Flag for monitoring any Changes to Form Details */
    private boolean isSaveRequired;
    
    // direct map id always 999
    /** Direct Map ID value */
    private int mapsID = 999;
    
    /** String Constant RIGHT_ID used to checked aganist RoleRights/ProposalRoleRights */
    private final static String RIGHT_ID = "MAINTAIN_PROPOSAL_ACCESS";
    
    /** roleID for the Current login */
    private int roleID;
    
    /** Servlet URL */
    private final String conURL = "/ProposalActionServlet";
    /** String constant APPROVE BY LABEL */
    private static final String APPROVEBY_LABEL ="Approve By";
    /** tring constant OR LABEL */
    private static final String OR_LABEL ="Or       ";
    
    /** Color for Setting Panel background for Disabled case */
    private Color greyBackGround = (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background");
    
    //======== START MESSAGES
    /** MESSAGES flags ADD_A_MAP */
    private static final String ADD_A_MAP = "selectApprovalmap_exceptionCode.1130"; //"Do you want to add a map ?";
    /** MESSAGES flags REPLACE_A_MAP */
    private static final String REPLACE_A_MAP = "selectApprovalmap_exceptionCode.1131"; // "Do you want to replace the current map ?";
    /** MESSAGES flags SAVE_CHANGES */
    private static final String SAVE_CHANGES = "budget_baseWindow_exceptionCode.1402"; //"Do you want to save changes ?";
    
    //======== END MESSAGES
    
    
    /** Creates new form SelectApprovalMapsForm */
    public SelectApprovalMapsForm() {
        parent = CoeusGuiConstants.getMDIForm();
        modal = true;
        initComponents();
        initDialogWindow();
        initForm();
        display();
    }
    
    
    /** SelectApprovalMapsForm with paramteres
     * @param parentFrame Component
     * @param isModal boolean
     */
    public SelectApprovalMapsForm(Component parentFrame,boolean isModal) {
        parent = parentFrame;
        modal = isModal;
        initComponents();
        initDialogWindow();
        initForm();
        display();
    }
    
    /** SelectApprovalMapsForm with paramteres
     * @param parameters Vector containg parameters
     * ProposalNumber,
     * Unit Number,functiontype,
     * ProposalDetailAdminForm,ProposalDevelopmentFormBean
     */
    public SelectApprovalMapsForm(Vector parameters){
        parent = CoeusGuiConstants.getMDIForm();
        modal = true;
        this.parameters = parameters;
        initComponents();
        initDialogWindow();
        initForm();
        display();
        
        //this.proposalDevelopmentFormBean = proposalDevelopmentFormBean;
    }
    
    /** setting ProposalDevelopmentFormBean instance
     * @param proposalDevelopmentFormBean ProposalDevelopmentFormBean instance
     */
    public void setBean(ProposalDevelopmentFormBean proposalDevelopmentFormBean) {
        this.proposalDevelopmentFormBean = proposalDevelopmentFormBean;
    }
    
    /** setting ProposalDetailAdminForm instance
     * @param proposalDetailAdminForm ProposalDetailAdminForm instance
     */
    public void setProposalDetailAdminForm(ProposalDetailAdminForm proposalDetailAdminForm) {
        this.proposalDetailAdminForm  = proposalDetailAdminForm;
    }
    
    /** setting ProposalNumber
     * @param proposalID String
     */
    public void setProposalNumber(String proposalID) {
        this.proposalNumber = proposalID;
    }
    /** setting UnitNumber
     * @param unitNumber String
     */
    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }
    
    /** returns is Save required flag
     * @return boolean if<CODE>true<CODE> save is required
     */
    public boolean isSaveRequired() {
        return isSaveRequired;
    }
    
    /** Initialises the Form with the paramters obtained from ProposalDetailForm */
    public void initParameters() {
        
        for(int i=0;i < parameters.size();i++ ) {
            if(i==0) {
                proposalNumber = (String) parameters.get(i);
            }
            if(i==1) {
                unitNumber = (String) parameters.get(i);
            }
            if(i==2) {
                functionType = ((String) parameters.get(i)).charAt(0);
            }
            
            if(i==3) {
                proposalDevelopmentFormBean = (ProposalDevelopmentFormBean) parameters.get(i);
                
                try {
                    
                    Vector vecUserRolesInfoBean = proposalDevelopmentFormBean.getUserRolesInfoBean();
                    UserRolesInfoBean userRolesInfoBean = (UserRolesInfoBean) vecUserRolesInfoBean.get(0);
                    RoleInfoBean roleInfoBean = userRolesInfoBean.getRoleBean();
                    roleID = roleInfoBean.getRoleId();
                    
                } catch(Exception e) {
                    e.getMessage();
                }
            }
            if(i==4) {
                proposalDetailAdminForm = (ProposalDetailAdminForm) parameters.get(i);
            }
        }
    }
    
    /** Use to Initialise the Dialog window containing this Form
     */
    public void initDialogWindow(){
        
        dlgApprovalMapsForm = new CoeusDlgWindow((java.awt.Frame) parent,title,modal);
        dlgApprovalMapsForm.getContentPane().add(this);
        dlgApprovalMapsForm.pack();
        dlgApprovalMapsForm.setFont(CoeusFontFactory.getLabelFont());
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgApprovalMapsForm.getSize();
        dlgApprovalMapsForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgApprovalMapsForm.setResizable(false);
        
    }
    
    
    /** Initialise the Form like registering listeners for components,setting the Form
     * data formatting the Componets based on initial data based on the parameters
     */
    public void initForm() {
        
        coeusMessageResources = CoeusMessageResources.getInstance();
        initParameters();
        formatTable();
        registerEventListeners() ;
        setFormData();
        formatUIComponents();
    }
    
    /** To Display the Form after initialisation
     */
    
    public void display() {
        dlgApprovalMapsForm.setVisible(true);
    }
    
    /** Registering the Event Listeners for Form componets */
    private void registerEventListeners() {
        
        dlgApprovalMapsForm.setDefaultCloseOperation(dlgApprovalMapsForm.DO_NOTHING_ON_CLOSE);
        
        btnSelectMap.addActionListener(this);
        btnRemove.addActionListener(this);
        btnOk.addActionListener(this);
        btnCancel.addActionListener(this);
        
        tblMapList.getSelectionModel().addListSelectionListener(new TableListSelectionListener());
        
        tblMapList.getTableHeader().setReorderingAllowed(false);
        tblMapList.setAutoResizeMode(tblMapList.AUTO_RESIZE_ALL_COLUMNS);
        
        scrPnCurrentTab.getVerticalScrollBar().setUnitIncrement(20);
        scrPnCurrentTab.getVerticalScrollBar().setBlockIncrement(10);
        scrPnSelectedMapList.getVerticalScrollBar().setUnitIncrement(20);
        scrPnSelectedMapList.getVerticalScrollBar().setBlockIncrement(10);
        
       
        
        //tbdPnList.getSelectedComponent().setFont(CoeusFontFactory.getLabelFont());
         
        tbdPnList.addChangeListener(new ChangeListener() {
            
            public void stateChanged(ChangeEvent chgEvt){
                Object source = chgEvt.getSource();

                if(source.equals(tbdPnList)) {
                    if((tbdPnList.getSelectedIndex() == 0) && tbdPnList.getTabCount() > 1) {
                        // Travel all the components while pressing tab button
                        
                        java.awt.Component[] components = {
                            tbdPnList,
                            btnOk,
                            btnRemove,
                            btnCancel
                        };
                        setScreenFocusTraversalPolicy(components);
                        btnRemove.setVisible(true);
                        
                    } else {
                        java.awt.Component[] components = {
                            tbdPnList,
                            btnSelectMap,
                            btnOk,
                            btnCancel
                        };
                        setScreenFocusTraversalPolicy(components);
                        btnRemove.setVisible(false);
                        
                    }
                    
                    
                }
                
            }
        });
        
        dlgApprovalMapsForm.addEscapeKeyListener( new AbstractAction("escPressed") {
            public void actionPerformed(ActionEvent ar) {
                checkIsFormModified();
            }
            
        });
        
        dlgApprovalMapsForm.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                checkIsFormModified();
            }
            
        });
    }
    
    /** Setting the Focus Transeversal Policy for the Form Components
     * @param components Array of Components in order of Foucs Transversal policy
     */
    
    private void setScreenFocusTraversalPolicy(Component[] components) {
        
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        dlgApprovalMapsForm.setFocusTraversalPolicy(traversePolicy);
        dlgApprovalMapsForm.setFocusCycleRoot(true);
        
    }
    
    /** formatting the UI after setting data */
    private void formatUIComponents() {
        
        //for(int index=0; index < tbdPnList.getTabCount();index++)
        //tbdPnList.getComponent(index).setFont(CoeusFontFactory.getNormalFont());
        
        int tabIndex = 0;
        
        if(isAnyMapSelected) {
            tbdPnList.removeAll();
            
            tbdPnList.addTab("Current", pnlCurrentTabPane);
            tbdPnList.addTab("Select List", pnlSelectListPane);
            btnRemove.setVisible(true);
            
        } else {
            tbdPnList.remove(0);
            btnRemove.setVisible(false);
        }
        
        if(isNewApprovalMapAdded) {
            tabIndex = 1;
        }
        
        tbdPnList.setSelectedIndex(tabIndex);
        
        if((functionType == TypeConstants.DISPLAY_MODE) || isRoleRightEnabled == false) {
            btnSelectMap.setEnabled(false);
            btnRemove.setEnabled(false);
            btnOk.setEnabled(false);
            tblMapList.setBackground(greyBackGround);
            pnlSelectedMapList.setBackground(greyBackGround);
            
        }
        
        if(tblMapList.getRowCount() < 1) {
            pnlSelectedMapList.removeAll();
            pnlSelectedMapList.setBackground(greyBackGround);
            btnSelectMap.setEnabled(false);
            btnRemove.setEnabled(false);
            btnOk.setEnabled(false);
        }
        
        scrPnMapList.setBackground(greyBackGround);
        
        dlgApprovalMapsForm.requestFocus();
        
        if(tbdPnList.getTitleAt(tabIndex).equalsIgnoreCase("Current")) {
            
            java.awt.Component[] components = {
                tbdPnList,
                btnOk,
                btnRemove,
                btnCancel
            };
            setScreenFocusTraversalPolicy(components);
            
        } else {
            java.awt.Component[] components = {
                tbdPnList,
                btnSelectMap,
                btnOk,
                btnCancel
            };
            setScreenFocusTraversalPolicy(components);
            if(functionType != TypeConstants.DISPLAY_MODE)
            btnSelectMap.requestFocus(); 
        }
        
            //tbdPnList.getSelectedComponent().setFont(CoeusFontFactory.getLabelFont());        
        
    }
    
    /** customizing the table */
    private void formatTable() {
        
        TableHeaderRenderer tableHeaderRenderer = new TableHeaderRenderer();
        
        //setting Table Column
        TableColumn tableColumn = null;
        
        //setting the width for each Column
        int colSize[] = {75, 80,600};
        
        for(int col = 0; col < colSize.length; col++) {
            tableColumn = tblMapList.getColumnModel().getColumn(col);
            tableColumn.setPreferredWidth(colSize[col]);
            tableColumn.setHeaderRenderer(tableHeaderRenderer);
        }
        
        tblMapList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
    }
    
    /** set the Form with data after getting data from server side call */
    public void setFormData() {
        try {
            //communicates with the server to get the Maps,MapDetails
            retrieveMapDetails();
            
            vectorProposalApprovalMapBean = (CoeusVector)ObjectCloner.deepCopy(proposalDevelopmentFormBean.getApprovalMaps());
            
            if(vectorProposalApprovalMapBean != null && vectorProposalApprovalMapBean.size() > 0 ) {
                
                for(int index=0 ; index < vectorProposalApprovalMapBean.size();index ++ ) {
                    
                    ProposalApprovalMapBean savedProposalApprovalMapBean = (ProposalApprovalMapBean) vectorProposalApprovalMapBean.get(index);
                    if(savedProposalApprovalMapBean != null && ((TypeConstants.INSERT_RECORD.equals(savedProposalApprovalMapBean.getAcType()))
                    || (savedProposalApprovalMapBean.getAcType() == null)
                    )){
                        proposalApprovalMapBean = savedProposalApprovalMapBean;
                        oldApprovalMapBean = (ProposalApprovalMapBean)ObjectCloner.deepCopy(savedProposalApprovalMapBean);
                    }
                    
                }
                
            } else
                vectorProposalApprovalMapBean = new CoeusVector();
            
            StringBuffer strBffr = new StringBuffer();
            lblProposalNumberValue.setText(proposalNumber);
            
            if(proposalDevelopmentFormBean != null) {
                
                if(proposalDevelopmentFormBean.getSponsorCode() != null) {
                    strBffr.append(proposalDevelopmentFormBean.getSponsorCode()+" : ");
                }
                
                strBffr.append(validateSponsorCode(proposalDevelopmentFormBean.getSponsorCode()) );
                lblSponsorValue.setText(strBffr.toString());
            }
            
            if(allUnitMaps != null && allUnitMaps.size() > 0) {
                ((MapListTableModel) tblMapList.getModel()).setData(allUnitMaps);
                tblMapList.getSelectionModel().setSelectionInterval(0,0);
                
            }
            
            if(proposalApprovalMapBean != null && 
                !TypeConstants.DELETE_RECORD.equals(proposalApprovalMapBean.getAcType()) ){
                
                isAnyMapSelected = true;
                vectorOfbean.removeAllElements();
                vectorOfbean.add(proposalApprovalMapBean);
                lblCurrentMapValue.setText(proposalApprovalMapBean.getDescription());
                setSelectedSequentialDetails(vectorOfbean,false);
                
            }
            
            
        }catch(Exception exp){
            exp.printStackTrace();
        }
    }
    
    /** Saves the Form data while closing */
    private void saveFormData() {
        
        if(isSaveRequired()) {
            
            if(vectorProposalApprovalMapBean.size() > 0) {
                sortVector(vectorProposalApprovalMapBean);
                proposalDevelopmentFormBean.setApprovalMaps(vectorProposalApprovalMapBean);
            }else if( oldApprovalMapBean != null  ){
                 if( oldApprovalMapBean.getAcType() == null ) {
                    oldApprovalMapBean.setAcType(TypeConstants.DELETE_RECORD);
                    CoeusVector vecToDB = new CoeusVector();
                    vecToDB.add(oldApprovalMapBean);
                    proposalDevelopmentFormBean.setApprovalMaps(vecToDB);
                 }else if( TypeConstants.INSERT_RECORD.equals(oldApprovalMapBean.getAcType() ) ){
                    proposalDevelopmentFormBean.setApprovalMaps(null);
                 }
            }
            proposalDetailAdminForm.setSaveRequired(true);
        }
        
        dlgApprovalMapsForm.dispose();
    }
    
    /** Check whether form is modified and Prompt window for Confirm Save operation */
    private void checkIsFormModified() {
        
        if(isSaveRequired) {
            
            int option  = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(SAVE_CHANGES),
            //SAVE_CHANGES,
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            CoeusOptionPane.DEFAULT_CANCEL);
            switch(option) {
                case ( JOptionPane.YES_OPTION ):
                    saveFormData();
                    break;
                    
                case ( JOptionPane.NO_OPTION ):
                    dlgApprovalMapsForm.dispose();
                    break;
                case ( JOptionPane.CANCEL_OPTION ) :
                    return;
            }
        } else
            dlgApprovalMapsForm.dispose();
    }
    
    
    /** get the MapDetails from server using Applet Servlet Communication */
    public void retrieveMapDetails() {
        
        Vector requestParameter = new Vector();
        if(unitNumber != null) {
            requestParameter.add(unitNumber);
        }
        else
            requestParameter.add(proposalDevelopmentFormBean.getOwnedBy());
        
        requestParameter.add(proposalNumber);       //proposal number
        requestParameter.add(new Integer(mapsID));  //mapId
        requestParameter.add(new Integer(roleID));  //roleID
        
        
        RequesterBean requesterBean  = new RequesterBean();
        requesterBean.setFunctionType(GET_APPROVAL_MAPS);
        requesterBean.setDataObjects(requestParameter);
        String connectTo = CoeusGuiConstants.CONNECTION_URL + conURL;
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requesterBean );
        comm.send();
        ResponderBean responderBean = comm.getResponse();
        try {
            if(responderBean.isSuccessfulResponse()) {
                if ( responderBean !=null ){
                    
                    Vector responseParameters = (Vector) responderBean.getDataObjects();
                    allUnitMaps = (Vector) responseParameters.get(0);
                    
                    Vector vecProposalRights  = (Vector) responseParameters.get(1);
                    Vector vecRoleRights  = (Vector) responseParameters.get(2);
                    //Modified on 09 Feb 2004 - Chandra
                    //if((proposalApprovalMapBean != null) && (functionType != TypeConstants.ADD_MODE)){
                    //Modified on 09 Feb 2004 - Chandra
                    if(functionType != TypeConstants.ADD_MODE){
                        isRoleRightEnabled = checkForRoleRight(vecProposalRights,RIGHT_ID);
                    } else{
                        isRoleRightEnabled = checkForRoleRight(vecRoleRights,RIGHT_ID);
                    }
                    
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        
    }
    
    /** check the given RIGHT_ID is ther in the Vector of RoleRights
     * @param vecRoleRights Vector of RoleRightInfoBean
     * @param rightID String right_Id that is searched in the Vector
     * @return boolean if <CODE> true </CODE> right_id is found in the Vector of RoleRights
     */
    public boolean checkForRoleRight(Vector vecRoleRights,String rightID) {
        
        boolean isRightID = false;
        
        for(int index=0;index < vecRoleRights.size();index++) {
            
            RoleRightInfoBean proposalRoleRightFormBean = (RoleRightInfoBean) vecRoleRights.get(index);
            if(proposalRoleRightFormBean.getRightId().trim().equalsIgnoreCase(rightID.trim()) ) {
                isRightID = true;
            }
            
        }
        
        return isRightID;
        
    }
    
    
    /** This method will get the sponsor name for the sponsor passed as the param
     * if the sponsor code is wrong it will throw the message as Invalid sponsor code.
     * @param sponsorCode String
     * @return String Description for the given Sponsor Code
     */
    public String validateSponsorCode(String sponsorCode){
        String sponsorDesc=null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
        RequesterBean request = new RequesterBean();
        request.setDataObject("GET_SPONSORINFO");
        request.setId(sponsorCode);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
        }
        if (response.isSuccessfulResponse()) {
            SponsorMaintenanceFormBean sponsorInfo =
            (SponsorMaintenanceFormBean) response.getDataObject();
            if(sponsorInfo!=null && sponsorInfo.getName() !=null){
                sponsorDesc = sponsorInfo.getName();
            }
            if (sponsorDesc == null ) {
                sponsorDesc = "";
                //                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                //                "prop_invalid_sponsor_exceptionCode.2509"));
                
            }
        }
        return sponsorDesc;
    }
    
    /** Action Performed on the Buttons
     * @param actionEvent ActionEvent
     */
    public void actionPerformed( ActionEvent actionEvent ) {
        
        Object source = actionEvent.getSource();
        try{
            //====== Select Map button when clicked
            if( source.equals( btnSelectMap) ){
                
                String message = null;
                message = ADD_A_MAP;
                
                if(proposalApprovalMapBean != null) {
                    message = REPLACE_A_MAP;
                }
                
                int option  = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey(message), //message,
                CoeusOptionPane.OPTION_YES_NO,
                CoeusOptionPane.DEFAULT_NO);
                switch(option) {
                    case ( JOptionPane.YES_OPTION ):
                        
                        lblCurrentMapValue.setText(tblMapList.getModel().getValueAt(tblMapList.getSelectedRow(), 2)+"");
                        
                        UnitMapBean unitMapBean =  (UnitMapBean) allUnitMaps.get(tblMapList.getSelectedRow());
                        vectorOfbean.removeAllElements();
                        vectorOfbean.add(unitMapBean);
                        // Added by ravi. Vector to hold the details and delete the 
                        // beans based on the AcType.
                        setSelectedSequentialDetails(vectorOfbean,false);
                        if( vectorProposalApprovalMapBean != null ) {
                            int vecSize = vectorProposalApprovalMapBean.size();
                            for( int indx = 0; indx < vecSize; indx++ ) {
                                ProposalApprovalMapBean mapBean = 
                                    (ProposalApprovalMapBean)vectorProposalApprovalMapBean.get(indx);
                                if( mapBean != null ) {
                                    if( mapBean.getAcType() == null ) {
                                        mapBean.setAcType(TypeConstants.DELETE_RECORD);
                                        vectorProposalApprovalMapBean.set(indx,mapBean);
                                    }else if( TypeConstants.INSERT_RECORD.equals(mapBean.getAcType()) ) {
                                        vectorProposalApprovalMapBean.remove(indx);
                                        break;
                                    }
                                }
                            }
                        }
                        // End Ravi - Bug fix 
//                        vectorProposalApprovalMapBean.removeAllElements();
//                        if(proposalApprovalMapBean != null && proposalApprovalMapBean.getAcType() == null) {
//                            proposalApprovalMapBean.setAcType(TypeConstants.DELETE_RECORD);
//                            vectorProposalApprovalMapBean.add(proposalApprovalMapBean);
//                        }
                        
                        proposalApprovalMapBean = getProposalApprovalMapBean(unitMapBean);
                        addSelectedProposalApprovalMapBean(proposalApprovalMapBean);
                        
                        isNewApprovalMapAdded  = true;
                        isAnyMapSelected = true;
                        isSaveRequired = true;
                        formatUIComponents();
                        
                        break;
                        
                    case ( JOptionPane.NO_OPTION ):
                        break;
                }
                
                
            }
            //====== Remove button when clicked
            if( source.equals( btnRemove ) ){
                
                try {
                    
                    if(proposalApprovalMapBean != null ) {
                        
                        for( int index=0; index <vectorProposalApprovalMapBean.size(); index++) {
                            proposalApprovalMapBean = (ProposalApprovalMapBean) vectorProposalApprovalMapBean.get(index);
                            if(proposalApprovalMapBean != null ) {
                                
                                if(proposalApprovalMapBean.getAcType() == null) {
                                    proposalApprovalMapBean.setAcType(TypeConstants.DELETE_RECORD);
                                }
                                if(TypeConstants.INSERT_RECORD.equals(proposalApprovalMapBean.getAcType())){
                                    vectorProposalApprovalMapBean.remove(proposalApprovalMapBean);
                                }
                                
                            }
                            
                        }
                        
                    }
                    
                    
                    proposalApprovalMapBean = null;
                    lblCurrentMapValue.setText("");
                    pnlCurrentMapList.removeAll();
                    pnlCurrentMapList.repaint();
                    isSaveRequired = true;
                    
                } catch(Exception exp) {
                    exp.printStackTrace();
                }
            }
            //====== OK button when clicked
            if( source.equals( btnOk ) ){
                saveFormData();
            }
            //====== CANCEL button when clicked
            if( source.equals( btnCancel ) ){
                checkIsFormModified();
            }
            
            
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }
    
    
    /** Adds the New Selected ProposalApprovalMapBean to vectorProposalApprovalMapBean
     * vectorProposalApprovalMapBean holds all ProposalApprovalMapBean
     * @param proposalApprovalMapBean ProposalApprovalMapBean being selected
     */
    private void addSelectedProposalApprovalMapBean(ProposalApprovalMapBean proposalApprovalMapBean) {
        
        if(vectorProposalApprovalMapBean.size() > 0) {
            
            for(int index =0 ; index < vectorProposalApprovalMapBean.size();index++) {
                
                ProposalApprovalMapBean eachProposalApprovalMapBean =  (ProposalApprovalMapBean) vectorProposalApprovalMapBean.get(index);
                if(eachProposalApprovalMapBean != null && eachProposalApprovalMapBean.getAcType() != null && (eachProposalApprovalMapBean.getAcType() == TypeConstants.INSERT_RECORD)) {
                    vectorProposalApprovalMapBean.remove(eachProposalApprovalMapBean);
                }
            }
            
        }
        
        if(proposalApprovalMapBean != null && proposalApprovalMapBean.getAcType() == TypeConstants.INSERT_RECORD) {
            vectorProposalApprovalMapBean.add(proposalApprovalMapBean);
        }
        
    }
    
    /** Sort the vectorProposalApprovalMapBean to keep  deleted ProposalApprovalMapBean before
     * Inserted ProposalApprovalMapBeans to avoid dataBase constraint vailations while saving
     * @param vectorProposalApprovalMapBean Vector contaning all ProposalApprovalMapBean beans
     * @return sorted Vector
     */
    private Vector sortVector(Vector vectorProposalApprovalMapBean) {
        
        Vector deletedVector = new Vector();
        Vector tempVector = new Vector();
        for(int index = 0; index < vectorProposalApprovalMapBean.size();index ++) {
            
            ProposalApprovalMapBean eachProposalApprovalMapBean =  (ProposalApprovalMapBean) vectorProposalApprovalMapBean.get(index);
            
            if(eachProposalApprovalMapBean == null ) {
                vectorProposalApprovalMapBean.remove(eachProposalApprovalMapBean);
                continue;
            }
            
            if(eachProposalApprovalMapBean.getAcType() == TypeConstants.DELETE_RECORD) {
                deletedVector.add(eachProposalApprovalMapBean);
            } else {
                tempVector.add(tempVector.size(),eachProposalApprovalMapBean);
            }
            
            
        }
        
        if(deletedVector.size() > 0) {
            tempVector.addAll(0,deletedVector);
        }
        
        return tempVector;
        
    }
    
    /** Returns a Panel for SequentialStop Row for a given index and Map type (Selcted/Current)
     * @param indexNumber int
     * @param isSelectedMap boolean if <CODE> true </CODE> a Panel for SelectedMap Tab of the Form
     * @return JPanel
     */
    private JPanel getSequentialStopRowPanel(int indexNumber,boolean isSelectedMap) {
        
        JPanel pnlSequentialStopRow = new JPanel();
        javax.swing.JLabel lblSequentialStop = new javax.swing.JLabel();
        javax.swing.JSeparator sptrSequentialStop = new javax.swing.JSeparator();
        GridBagConstraints gridBagConstraints = null;
        
        
        
        if(isSelectedMap == false) {
            
            pnlSequentialStopRow.setLayout(new java.awt.GridBagLayout());
            pnlSequentialStopRow.setMaximumSize(new java.awt.Dimension(600, 30));
            pnlSequentialStopRow.setMinimumSize(new java.awt.Dimension(600, 20));
            pnlSequentialStopRow.setPreferredSize(new java.awt.Dimension(600, 20));
            
            lblSequentialStop.setFont(CoeusFontFactory.getLabelFont());
            lblSequentialStop.setText("Sequential Stop:   "+indexNumber);
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
            pnlSequentialStopRow.add(lblSequentialStop, gridBagConstraints);
            
            sptrSequentialStop.setMaximumSize(new java.awt.Dimension(600, 1));
            sptrSequentialStop.setMinimumSize(new java.awt.Dimension(600, 1));
            sptrSequentialStop.setPreferredSize(new java.awt.Dimension(600, 1));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.insets = new java.awt.Insets(5, 25, 0, 0);
            pnlSequentialStopRow.add(sptrSequentialStop, gridBagConstraints);
            
        }
        
        //========For Select List Tab ==============
        
        if(isSelectedMap) {
            
            javax.swing.JLabel lblDescription = new javax.swing.JLabel();
            javax.swing.JSeparator sptrSequentialStopTop = new javax.swing.JSeparator();
            
            pnlSequentialStopRow.setLayout(new java.awt.GridBagLayout());
            pnlSequentialStopRow.setBackground(new java.awt.Color(255, 255, 255));
            pnlSequentialStopRow.setMaximumSize(new java.awt.Dimension(600, 30));
            pnlSequentialStopRow.setMinimumSize(new java.awt.Dimension(600, 20));
            pnlSequentialStopRow.setPreferredSize(new java.awt.Dimension(600, 20));
            
            sptrSequentialStopTop.setMaximumSize(new java.awt.Dimension(595, 1));
            sptrSequentialStopTop.setMinimumSize(new java.awt.Dimension(595, 1));
            sptrSequentialStopTop.setPreferredSize(new java.awt.Dimension(595, 1));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.weighty = 1.0;
            pnlSequentialStopRow.add(sptrSequentialStopTop, gridBagConstraints);
            
            lblSequentialStop.setFont(CoeusFontFactory.getNormalFont());
            lblSequentialStop.setForeground(BLUE_COLOR);
            lblSequentialStop.setText("Sequential Stop:   "+indexNumber);
            lblSequentialStop.setMaximumSize(new java.awt.Dimension(32767, 32767));
            lblSequentialStop.setMinimumSize(new java.awt.Dimension(170, 16));
            lblSequentialStop.setPreferredSize(new java.awt.Dimension(170, 16));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
            pnlSequentialStopRow.add(lblSequentialStop, gridBagConstraints);
            
            sptrSequentialStop.setMaximumSize(new java.awt.Dimension(595, 1));
            sptrSequentialStop.setMinimumSize(new java.awt.Dimension(595, 1));
            sptrSequentialStop.setPreferredSize(new java.awt.Dimension(595, 1));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
            pnlSequentialStopRow.add(sptrSequentialStop, gridBagConstraints);
            
            lblDescription.setFont(CoeusFontFactory.getNormalFont());
            lblDescription.setForeground(BLUE_COLOR);
            lblDescription.setText("Description");
            lblDescription.setMaximumSize(new java.awt.Dimension(50, 16));
            lblDescription.setMinimumSize(new java.awt.Dimension(50, 16));
            lblDescription.setPreferredSize(new java.awt.Dimension(50, 16));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            pnlSequentialStopRow.add(lblDescription, gridBagConstraints);
        }
        
        if((functionType == TypeConstants.DISPLAY_MODE) || isRoleRightEnabled == false){
            pnlSequentialStopRow.setBackground(greyBackGround);
        }
        
        return pnlSequentialStopRow;
        
    }
    
    /** Returns a Panel for AprrovedBy Row setting the parameters passed to it
     * @param elementValues String Array containing values
     * @param isSelectedMap boolean if <CODE> true </CODE> For SelectedMap Tab
     * @param isRowFormatReq boolean whether Formatting of Row is required based on data to be set
     * @return JPanel
     */
    private JPanel getApprovedByRowPanel(String elementValues[],boolean isSelectedMap,boolean isRowFormatReq) {
        
        JPanel pnlApprobedByRow = new JPanel();
        javax.swing.JLabel lblApproveByRow = new javax.swing.JLabel();
        javax.swing.JLabel lblUserIdRow = new javax.swing.JLabel();
        javax.swing.JLabel lblDescriptionRow = new javax.swing.JLabel();
        GridBagConstraints gridBagConstraints = null;
        
        if(isSelectedMap == false) {
            
            if(isRowFormatReq) {
                lblApproveByRow.setFont(CoeusFontFactory.getNormalFont());
                lblApproveByRow.setForeground(BLUE_COLOR);
                lblApproveByRow.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                lblUserIdRow.setFont(CoeusFontFactory.getNormalFont());
                lblDescriptionRow.setFont(CoeusFontFactory.getNormalFont());
                
            } else {
                lblApproveByRow.setFont(CoeusFontFactory.getNormalFont());
                lblApproveByRow.setForeground(RED_COLOR);
                lblApproveByRow.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                lblUserIdRow.setFont(CoeusFontFactory.getLabelFont());
                lblDescriptionRow.setFont(CoeusFontFactory.getLabelFont());
            }
            
            pnlApprobedByRow.setLayout(new java.awt.GridBagLayout());
            pnlApprobedByRow.setMaximumSize(new java.awt.Dimension(600, 25));
            pnlApprobedByRow.setMinimumSize(new java.awt.Dimension(600, 25));
            pnlApprobedByRow.setPreferredSize(new java.awt.Dimension(600, 25));
            
            lblApproveByRow.setText(elementValues[0]);   //"Approve By");
            lblApproveByRow.setMaximumSize(new java.awt.Dimension(150, 20));
            lblApproveByRow.setMinimumSize(new java.awt.Dimension(150, 20));
            lblApproveByRow.setPreferredSize(new java.awt.Dimension(150, 20));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints.weighty = 1.0;
            pnlApprobedByRow.add(lblApproveByRow, gridBagConstraints);
            
            lblUserIdRow.setText(elementValues[1]); //"user");
            lblUserIdRow.setMaximumSize(new java.awt.Dimension(100, 20));
            lblUserIdRow.setMinimumSize(new java.awt.Dimension(100, 20));
            lblUserIdRow.setPreferredSize(new java.awt.Dimension(100, 20));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints.weighty = 1.0;
            pnlApprobedByRow.add(lblUserIdRow, gridBagConstraints);
            
            lblDescriptionRow.setText(elementValues[2]);    //"Description");
            lblDescriptionRow.setMaximumSize(new java.awt.Dimension(450, 20));
            lblDescriptionRow.setMinimumSize(new java.awt.Dimension(450, 20));
            lblDescriptionRow.setPreferredSize(new java.awt.Dimension(450, 20));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            pnlApprobedByRow.add(lblDescriptionRow, gridBagConstraints);
            
            
            
        }
        
        //========For Select List Tab ==============
        
        if(isSelectedMap) {
            
            pnlApprobedByRow.setLayout(new java.awt.GridBagLayout());
            
            pnlApprobedByRow.setBackground(new java.awt.Color(255, 255, 255));
            pnlApprobedByRow.setMaximumSize(new java.awt.Dimension(600, 30));
            pnlApprobedByRow.setMinimumSize(new java.awt.Dimension(600, 30));
            pnlApprobedByRow.setPreferredSize(new java.awt.Dimension(600, 30));
            
            if(isRowFormatReq) {
                lblApproveByRow.setFont(CoeusFontFactory.getNormalFont());
                lblApproveByRow.setForeground(BLUE_COLOR);
                lblApproveByRow.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                
                //Font(String name, int style, int size)
                java.awt.Font italicFont = new java.awt.Font(lblUserIdRow.getFont().getFontName(),
                java.awt.Font.ITALIC,
                lblUserIdRow.getFont().getSize());
                
                lblUserIdRow.setFont(italicFont);
                lblDescriptionRow.setFont(italicFont);
                lblApproveByRow.setFont(italicFont);
                
            } else {
                lblApproveByRow.setFont(CoeusFontFactory.getNormalFont());
                lblApproveByRow.setForeground(RED_COLOR);
                lblApproveByRow.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                lblUserIdRow.setFont(CoeusFontFactory.getNormalFont());
                lblDescriptionRow.setFont(CoeusFontFactory.getNormalFont());
            }
            
            lblApproveByRow.setText(elementValues[0]);  //"Approve By");
            lblApproveByRow.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            lblApproveByRow.setMaximumSize(new java.awt.Dimension(100, 20));
            lblApproveByRow.setMinimumSize(new java.awt.Dimension(100, 20));
            lblApproveByRow.setPreferredSize(new java.awt.Dimension(100, 20));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints.weighty = 1.0;
            pnlApprobedByRow.add(lblApproveByRow, gridBagConstraints);
            
            lblUserIdRow.setText(elementValues[1]); //"user");
            lblUserIdRow.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            lblUserIdRow.setMaximumSize(new java.awt.Dimension(80, 20));
            lblUserIdRow.setMinimumSize(new java.awt.Dimension(80, 20));
            lblUserIdRow.setPreferredSize(new java.awt.Dimension(80, 20));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints.weighty = 1.0;
            pnlApprobedByRow.add(lblUserIdRow, gridBagConstraints);
            
            lblDescriptionRow.setText(elementValues[2]);
            lblDescriptionRow.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            lblDescriptionRow.setMaximumSize(new java.awt.Dimension(32767, 32767));
            lblDescriptionRow.setMinimumSize(new java.awt.Dimension(520, 20));
            lblDescriptionRow.setPreferredSize(new java.awt.Dimension(520, 20));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            pnlApprobedByRow.add(lblDescriptionRow, gridBagConstraints);
        }
        
        //pnlSelectedMapList.add(pnlApprobedByRow);
        if((functionType == TypeConstants.DISPLAY_MODE) || isRoleRightEnabled == false){
            pnlApprobedByRow.setBackground(greyBackGround);
        }
        
        return pnlApprobedByRow;
        
    }
    
    /** Method to filter a Vector od UnitMapDetailsBean
     * based on level,mapID and PrimaryApprover flag
     * @param mapDetailBean Vector containing ProposalApprovalBean or UnitMapDetailsBean
     * @param level int sequential level
     * @param stopLevel int stop level
     * @param isPrimaryApprover boolean if <CODE> true </CODE> is primaryApprover
     * @param mapId int mapId value
     * @return Vector filtered Vector of ProposalApprovalBean or UnitMapDetailsBean based on the
     * paramters Level Stoplevel and PrimaryApprover flag and mapID
     */
    private Vector getEachSequentialStopData(Vector mapDetailBean,int level,int stopLevel,boolean isPrimaryApprover,int mapId) {
        
        Vector resultedBean = new Vector();
        int levelofBean = 0;
        int stopLevelofBean = 0;
        int mapIDofBean = 0;
        boolean isPrimaryApproverFlagofBean = false;
        UnitMapDetailsBean unitMapDetailsBean = null;
        ProposalApprovalBean proposalApprovalBean = null;
        
        for(int index=0;index < mapDetailBean.size();index++) {
            
            if(mapDetailBean.get(0).getClass() == UnitMapDetailsBean.class) {
                unitMapDetailsBean = (UnitMapDetailsBean) mapDetailBean.get(index);
                levelofBean = unitMapDetailsBean.getLevelNumber() ;
                stopLevelofBean = unitMapDetailsBean.getStopNumber();
                mapIDofBean = unitMapDetailsBean.getMapId();
                isPrimaryApproverFlagofBean = unitMapDetailsBean.isPrimaryApproverFlag();
            }
            
            if(mapDetailBean.get(0).getClass() == ProposalApprovalBean.class) {
                proposalApprovalBean = (ProposalApprovalBean) mapDetailBean.get(index);
                levelofBean = proposalApprovalBean.getLevelNumber() ;
                stopLevelofBean = proposalApprovalBean.getStopNumber();
                mapIDofBean = proposalApprovalBean.getMapId();
                isPrimaryApproverFlagofBean = proposalApprovalBean.isPrimaryApproverFlag();
            }
            
            
            // is MapID is provided then the check is made for that mapID
            //boolean matchAll = false;
            boolean matchEach = false;
            matchEach = (levelofBean == level)?true:false;
            if(matchEach) {
                matchEach = (isPrimaryApproverFlagofBean == isPrimaryApprover)?true:false;
            }
            
            if(matchEach && (mapId != -1 )) {
                if(mapId == mapIDofBean) {
                    matchEach = true;
                }
                else
                    matchEach = false;
            }
            if(matchEach && (stopLevel != -1 )) {
                if(stopLevel == stopLevelofBean) {
                    matchEach = true;
                }
                else
                    matchEach = false;
            }
            if(matchEach) {
                if(mapDetailBean.get(0).getClass() == UnitMapDetailsBean.class) {
                    resultedBean.add(unitMapDetailsBean);
                }
                if(mapDetailBean.get(0).getClass() == ProposalApprovalBean.class) {
                    resultedBean.add(proposalApprovalBean);
                }
            }
            
        }
        return resultedBean;
    }
    
    /** Method to form the ProposalApprovalMapBean from the selected UnitMapBean
     * @param selectedUnitMapBean UnitMapBean which forms ProposalApprovalMapBean
     * @return ProposalApprovalMapBean formed from UnitMapBean
     */
    
    private ProposalApprovalMapBean getProposalApprovalMapBean(UnitMapBean selectedUnitMapBean) {
        
        ProposalApprovalMapBean newProposalApprovalMapBean = new ProposalApprovalMapBean();
        try {
            
            Vector mapDetails = null;
            if(selectedUnitMapBean != null) {
                
                newProposalApprovalMapBean.setProposalNumber(proposalNumber);
                newProposalApprovalMapBean.setMapId(mapsID);
                newProposalApprovalMapBean.setParentMapId(0);
                newProposalApprovalMapBean.setAcType(TypeConstants.INSERT_RECORD);
                newProposalApprovalMapBean.setApprovalStatus("T");
                newProposalApprovalMapBean.setSystemFlag(false);
                newProposalApprovalMapBean.setDescription(selectedUnitMapBean.getDescription());
                mapDetails = selectedUnitMapBean.getMapDetails();
                
                CoeusVector vecProposalApprovalBean  = new CoeusVector();
                
                for(int index=0;index < mapDetails.size(); index++) {
                    
                    UnitMapDetailsBean unitMapDetailsBean = (UnitMapDetailsBean) mapDetails.get(index);
                    ProposalApprovalBean proposalApprovalBean = new ProposalApprovalBean();
                    
                    proposalApprovalBean.setProposalNumber(proposalNumber);
                    proposalApprovalBean.setAcType(TypeConstants.INSERT_RECORD);
                    proposalApprovalBean.setMapId(mapsID);
                    proposalApprovalBean.setDescription(unitMapDetailsBean.getDescription());
                    proposalApprovalBean.setLevelNumber(unitMapDetailsBean.getLevelNumber());
                    proposalApprovalBean.setStopNumber(unitMapDetailsBean.getStopNumber());
                    proposalApprovalBean.setUserId(unitMapDetailsBean.getUserId());
                    proposalApprovalBean.setPrimaryApproverFlag(unitMapDetailsBean.isPrimaryApproverFlag());
                    proposalApprovalBean.setApprovalStatus("T");
                    
                    vecProposalApprovalBean.add(proposalApprovalBean);
                    
                }
                
                newProposalApprovalMapBean.setProposalApprovals(vecProposalApprovalBean);
                
            }
            
        }catch(Exception expection) {
            expection.printStackTrace();
            newProposalApprovalMapBean = null;
        }
        return newProposalApprovalMapBean;
    }
    
//    private Vector sortMapBeans (Vector vecBeansDetails) {
//         Vector mapDetails = null;
//         Vector vecSortedMapDetails = new Vector();
//         Hashtable indexHashTable = new Hashtable ();
//         if(vecBeansDetails.get(0).getClass() == UnitMapBean.class) {
//                
//                UnitMapBean unitMapBean = (UnitMapBean) vecBeansDetails.get(0);
//                if(unitMapBean != null) {
//                    mapDetails = unitMapBean.getMapDetails();
//                }
//                
//                for(int index =0 ;index < mapDetails.size();index++) {
//                    unitMapBean = (UnitMapBean) vecBeansDetails.get(index);
//                    if(indexHashTable.containsKey(new Integer(index+1))) {
//                    unitMapBean.getMapId();
//                    }
//                    
//                }
//            }
//            
//            if(vecBeansDetails.get(0).getClass() == ProposalApprovalMapBean.class) {
//                
//                ProposalApprovalMapBean proposalApprovalMapBean = (ProposalApprovalMapBean) vecBeansDetails.get(0);
//                if(proposalApprovalMapBean != null) {
//                    mapDetails = proposalApprovalMapBean.getProposalApprovals();
//                }
//            }
//         
//         
//    }
    
    /** Method to set Vector of Beans details and Form each Panel rows based on the
     * selected map in the table
     * @param beansDetails Vector of Beans [UnitMapBean or ProposalApprovalMapBean]
     * @param isSelectedMapTab boolean if<CODE> true </CODE> Values are set for Selected Map Tab
     */
    public void setSelectedSequentialDetails(Vector beansDetails,boolean isSelectedMapTab) {
        
        try {
            
            
            Vector mapDetails = null;
            Hashtable seqHashTab = new Hashtable();
            Hashtable seqSubHashTab = new Hashtable();
            boolean isUIUpdated = false;
            int mapId = 0;
            if(beansDetails.get(0).getClass() == UnitMapBean.class) {
                
                UnitMapBean unitMapBean = (UnitMapBean) beansDetails.get(0);
                if(unitMapBean != null) {
                    mapDetails = unitMapBean.getMapDetails();
                }
            }
            
            if(beansDetails.get(0).getClass() == ProposalApprovalMapBean.class) {
                
                ProposalApprovalMapBean proposalApprovalMapBean = (ProposalApprovalMapBean) beansDetails.get(0);
                if(proposalApprovalMapBean != null) {
                    mapDetails = proposalApprovalMapBean.getProposalApprovals();
                }
            }
            if(isSelectedMapTab) {
                pnlSelectedMapList.removeAll();
            }
            else
                pnlCurrentMapList.removeAll();
            
            if(mapDetails == null || mapDetails.size() < 1) {
                return;
            }
            
            for(int index =0;index < mapDetails.size();index ++) {
                String level = null;
                int stopLevel = 0;
                
                if(mapDetails.get(0).getClass() == UnitMapDetailsBean.class) {
                    
                    UnitMapDetailsBean unitMapDetailsBean = (UnitMapDetailsBean) mapDetails.get(index);
                    level = unitMapDetailsBean.getLevelNumber()+"";
                    mapId = unitMapDetailsBean.getMapId();
                    stopLevel = unitMapDetailsBean.getStopNumber();
                }
                
                if(mapDetails.get(0).getClass() == ProposalApprovalBean.class) {
                    
                    ProposalApprovalBean proposalApprovalBean = (ProposalApprovalBean) mapDetails.get(index);
                    level = proposalApprovalBean.getLevelNumber()+"";
                    mapId = proposalApprovalBean.getMapId();
                    stopLevel = proposalApprovalBean.getStopNumber();
                }
                
                
                Vector eachStopDataPrimaryApprovers = filterVectorByLevel(mapDetails,Integer.parseInt(level),stopLevel,true,mapId);
                Vector eachStopDataNonPrimaryApprovers = filterVectorByLevel(mapDetails,Integer.parseInt(level),stopLevel,false,mapId);
                
                
                //=========Adding the SequentialStopHeader  ==================
                boolean isSequentialHeaderYoAdd = false;
                if(!seqHashTab.containsKey(level+mapId)) {
                    seqHashTab.put(level+mapId, level+mapId);
                    isSequentialHeaderYoAdd = true;
                }
                
                if((eachStopDataPrimaryApprovers != null && eachStopDataPrimaryApprovers.size() > 0) && isSequentialHeaderYoAdd) {
                    if(isSelectedMapTab) {
                        pnlSelectedMapList.add(getSequentialStopRowPanel(Integer.parseInt(level),isSelectedMapTab));//isSelectedMapTab
                    }
                    else
                        pnlCurrentMapList.add(getSequentialStopRowPanel(Integer.parseInt(level),isSelectedMapTab));//isSelectedMapTab
                    
                }
                
                //=========Adding Data for each SequentialStopHeader ==========
                
                if((eachStopDataPrimaryApprovers != null && eachStopDataPrimaryApprovers.size() > 0) ) {
                    
                    if(!seqSubHashTab.containsKey(level+mapId+stopLevel)) {
                        setSequentialRows(eachStopDataPrimaryApprovers,eachStopDataNonPrimaryApprovers,isSelectedMapTab);
                        isUIUpdated = true;
                        seqSubHashTab.put(level+mapId+stopLevel, level+mapId+stopLevel);
                    }
                }
                
            }
            
            if(isUIUpdated) {
                
                if(isSelectedMapTab) {
                    pnlSelectedMapList.repaint();
                } else
                    pnlCurrentMapList.repaint();
                
                //dlgApprovalMapsForm.pack();
                dlgApprovalMapsForm.validate();
            }
            
        }catch(Exception exp) {
            exp.getMessage();
        }
        
    }
    
    /** Method to set the SequentialStop details of the Selected UnitMapBean
     * @param mapDetailsPrimaryApprover Vector of beans with PrimaryApprover flag true
     * @param mapDetailsNonPrimaryApprover Vector of beans with Non PrimaryApprovers
     * @param isSelectedMapTab boolean
     */
    
    public void setSequentialRows(Vector mapDetailsPrimaryApprover,Vector mapDetailsNonPrimaryApprover,boolean isSelectedMapTab) {
        
        String valuesForPanel [] = new String[3];
        UnitMapDetailsBean unitMapDetailsBean = null;
        ProposalApprovalBean proposalApprovalBean = null;
        int level = 0;
        int stopLevel = 0;
        int mapID = 0;
        //APPROVEBY_LABEL OR_LABEL
        for(int indexData = 0; indexData < mapDetailsPrimaryApprover.size();indexData ++) {
            
            if(mapDetailsPrimaryApprover.get(0).getClass() == UnitMapDetailsBean.class) {
                
                unitMapDetailsBean =  (UnitMapDetailsBean) mapDetailsPrimaryApprover.get(indexData);
                level = unitMapDetailsBean.getLevelNumber();
                stopLevel = unitMapDetailsBean.getStopNumber();
                mapID = unitMapDetailsBean.getMapId();
                
                valuesForPanel[0] = APPROVEBY_LABEL;
//                valuesForPanel[1] = unitMapDetailsBean.getUserId();
                /*
                 * UserID to UserName Enhancement - Start
                 * Added new property getUpdateUserName to get username.
                 */
                 valuesForPanel[1] = unitMapDetailsBean.getUpdateUserName();
                // UserId to UserName Enhancement - End
                valuesForPanel[2] = unitMapDetailsBean.getDescription();
            }
            if(mapDetailsPrimaryApprover.get(0).getClass() == ProposalApprovalBean.class) {
                proposalApprovalBean = (ProposalApprovalBean) mapDetailsPrimaryApprover.get(indexData);
                level = proposalApprovalBean.getLevelNumber();
                stopLevel = proposalApprovalBean.getStopNumber();
                mapID = proposalApprovalBean.getMapId();
                
                valuesForPanel[0] = APPROVEBY_LABEL;
                //valuesForPanel[1] = proposalApprovalBean.getUserId();
                /*
                 * UserID to UserName Enhancement - Start
                 * Added new property getUpdateUserName to get the username
                 */
                valuesForPanel[1] = proposalApprovalBean.getUpdateUserName();
                // UserId to UserName Enhancement - End
                valuesForPanel[2] = proposalApprovalBean.getDescription();
            }
            addSequentialDetialsRowstoPanel(valuesForPanel,isSelectedMapTab,false);
            
        }
        
        if(mapDetailsNonPrimaryApprover != null && mapDetailsNonPrimaryApprover.size() > 0) {
            
            for(int index= 0; index < mapDetailsNonPrimaryApprover.size();index++) {
                
                if(mapDetailsNonPrimaryApprover.get(0).getClass() == UnitMapDetailsBean.class) {
                    unitMapDetailsBean =  (UnitMapDetailsBean) mapDetailsNonPrimaryApprover.get(index);
                    valuesForPanel[0] = OR_LABEL;
//                    valuesForPanel[1] = unitMapDetailsBean.getUserId();
                /*
                 * UserID to UserName Enhancement - Start
                 * Added new property getUpdateUserName to get username.
                 */ 
                 valuesForPanel[1] = unitMapDetailsBean.getUpdateUserName();
                    // UserId to UserName Enhancement - End
                    valuesForPanel[2] = unitMapDetailsBean.getDescription();
                }
                
                if(mapDetailsNonPrimaryApprover.get(0).getClass() == ProposalApprovalBean.class) {
                    proposalApprovalBean = (ProposalApprovalBean) mapDetailsNonPrimaryApprover.get(index);
                    valuesForPanel[0] = OR_LABEL;
                    //valuesForPanel[1] = proposalApprovalBean.getUserId();
                    /*
                     * UserID to UserName Enhancement - Start
                     * Added new property getUpdateUserName to get the username
                     */
                    valuesForPanel[1] = proposalApprovalBean.getUpdateUserName();
                    // UserId to UserName Enhancement - End
                    valuesForPanel[2] = proposalApprovalBean.getDescription();
                }
                
                addSequentialDetialsRowstoPanel(valuesForPanel,isSelectedMapTab,true);
            }
            
        }
        
        
    }
    
    /** Method that added each Row of Panel to the Main Panel
     * [pnlSelectedMapList,pnlCurrentMapList]
     * @param valuesForPanel String Array containing vales tobe set to each Panel Rows
     * @param isSelectedMapTab boolean
     * @param isRowFormatReq boolean true if formatting is required for the Panel
     */
    public void addSequentialDetialsRowstoPanel(String [] valuesForPanel,boolean isSelectedMapTab,boolean isRowFormatReq) {
        
        if(isSelectedMapTab) {
            pnlSelectedMapList.add(getApprovedByRowPanel(valuesForPanel,isSelectedMapTab,isRowFormatReq));//isSelectedMapTab
        }
        else
            pnlCurrentMapList.add(getApprovedByRowPanel(valuesForPanel,isSelectedMapTab,isRowFormatReq));//isSelectedMapTab
        
    }
    
    
    /** filter a given Vector of ProposalApprovalBeans or UnitMapDetailsBeans
     * for the given parameters
     * @param mapBeans Vector of all ProposalApprovalBeans or UnitMapDetailsBeans
     * @param startLevel int Level of Bean
     * @param stopLevel int Stop Level of Bean
     * @param isPrimaryApprover boolean if true look for Primary Approver flag
     * @param mapId int map Id of Bean
     * @return filtered Vector of ProposalApprovalBeans or UnitMapDetailsBeans
     */
    public Vector filterVectorByLevel(Vector mapBeans,int startLevel,int stopLevel,boolean isPrimaryApprover,int mapId) {
        
        UnitMapDetailsBean  unitMapDetailsBean =  null;
        ProposalApprovalBean proposalApprovalBean = null;
        int mapIDOfBean = 0;
        int startLevelOfBean = 0;
        int stopLevelOfBean = 0;
        boolean isPrimaryApproverOfBean = false;
        Vector filteredVector = new Vector();
        
        try {
            
            for(int indexData = 0; indexData < mapBeans.size();indexData ++) {
                
                if(mapBeans.get(0).getClass() == ProposalApprovalBean.class) {
                    proposalApprovalBean = (ProposalApprovalBean) mapBeans.get(indexData);
                    mapIDOfBean = proposalApprovalBean.getMapId();
                    startLevelOfBean = proposalApprovalBean.getLevelNumber();
                    stopLevelOfBean = proposalApprovalBean.getStopNumber();
                    isPrimaryApproverOfBean = proposalApprovalBean.isPrimaryApproverFlag();
                }
                if(mapBeans.get(0).getClass() == UnitMapDetailsBean.class) {
                    unitMapDetailsBean =  (UnitMapDetailsBean) mapBeans.get(indexData);
                    mapIDOfBean = unitMapDetailsBean.getMapId();
                    startLevelOfBean = unitMapDetailsBean.getLevelNumber();
                    stopLevelOfBean = unitMapDetailsBean.getStopNumber();
                    isPrimaryApproverOfBean = unitMapDetailsBean.isPrimaryApproverFlag();
                }
                
                if(
                (startLevelOfBean == startLevel) &&
                (stopLevelOfBean == stopLevel) &&
                (mapIDOfBean == mapId) &&
                (isPrimaryApproverOfBean == isPrimaryApprover )
                ) {
                    
                    if(mapBeans.get(0).getClass() == UnitMapDetailsBean.class) {
                        //mapBeans.remove(unitMapDetailsBean);
                        filteredVector.add(unitMapDetailsBean);
                    }
                    
                    if(mapBeans.get(0).getClass() == ProposalApprovalBean.class) {
                        //  mapBeans.remove(proposalApprovalBean);
                        filteredVector.add(proposalApprovalBean);
                    }
                    
                }
            }
            
        }catch(Exception exp) {
            exp.printStackTrace();
            filteredVector = null;
        }
        
        return filteredVector;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        lblProposalNumber = new javax.swing.JLabel();
        lblProposalNumberValue = new javax.swing.JLabel();
        lblSponsor = new javax.swing.JLabel();
        lblSponsorValue = new javax.swing.JLabel();
        sptrTopSperator = new javax.swing.JSeparator();
        tbdPnList = new javax.swing.JTabbedPane();
        pnlCurrentTabPane = new javax.swing.JPanel();
        lblCurrentMap = new javax.swing.JLabel();
        lblCurrentMapValue = new javax.swing.JLabel();
        scrPnCurrentTab = new javax.swing.JScrollPane();
        pnlCurrentMapList = new javax.swing.JPanel();
        pnlCurrentMapHeaderRow = new javax.swing.JPanel();
        lblCurrentMapHeaderData = new javax.swing.JLabel();
        sptrCurrentMapHeader = new javax.swing.JSeparator();
        pnlCurrentMapPanelRow = new javax.swing.JPanel();
        lblApproveBy = new javax.swing.JLabel();
        lblUserId = new javax.swing.JLabel();
        lblDescription = new javax.swing.JLabel();
        pnlSelectListPane = new javax.swing.JPanel();
        scrPnMapList = new javax.swing.JScrollPane();
        tblMapList = new javax.swing.JTable();
        scrPnSelectedMapList = new javax.swing.JScrollPane();
        pnlSelectedMapList = new javax.swing.JPanel();
        pnlSelectedMapListHeaderRow = new javax.swing.JPanel();
        sptrSelectedMapListTop = new javax.swing.JSeparator();
        lblSelectedMapListHeaderData = new javax.swing.JLabel();
        sptrSelectedMapList = new javax.swing.JSeparator();
        lblSelectedMapDescr = new javax.swing.JLabel();
        pnlSelectedMapListPanelRow = new javax.swing.JPanel();
        lblSelectedMapListApproveBy = new javax.swing.JLabel();
        lblSelectedMapListUserId = new javax.swing.JLabel();
        lblSelectedMapListDescription = new javax.swing.JLabel();
        btnSelectMap = new javax.swing.JButton();
        pnlButtons = new javax.swing.JPanel();
        btnRemove = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        lblProposalNumber.setFont(CoeusFontFactory.getLabelFont());
        lblProposalNumber.setText("Proposal Number:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(lblProposalNumber, gridBagConstraints);

        lblProposalNumberValue.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(lblProposalNumberValue, gridBagConstraints);

        lblSponsor.setFont(CoeusFontFactory.getLabelFont());
        lblSponsor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSponsor.setText("Sponsor:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(lblSponsor, gridBagConstraints);

        lblSponsorValue.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(lblSponsorValue, gridBagConstraints);

        sptrTopSperator.setBackground(new java.awt.Color(128, 128, 128));
        sptrTopSperator.setMinimumSize(new java.awt.Dimension(628, 10));
        sptrTopSperator.setPreferredSize(new java.awt.Dimension(628, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(sptrTopSperator, gridBagConstraints);

        tbdPnList.setFont(CoeusFontFactory.getLabelFont());
        tbdPnList.setMinimumSize(new java.awt.Dimension(630, 370));
        tbdPnList.setPreferredSize(new java.awt.Dimension(630, 370));
        pnlCurrentTabPane.setLayout(new java.awt.GridBagLayout());

        pnlCurrentTabPane.setFont(CoeusFontFactory.getLabelFont());
        lblCurrentMap.setFont(CoeusFontFactory.getLabelFont());
        lblCurrentMap.setText("Current Map:");
        lblCurrentMap.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblCurrentMap.setMaximumSize(new java.awt.Dimension(75, 40));
        lblCurrentMap.setMinimumSize(new java.awt.Dimension(75, 40));
        lblCurrentMap.setPreferredSize(new java.awt.Dimension(75, 40));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 20, 0);
        pnlCurrentTabPane.add(lblCurrentMap, gridBagConstraints);

        lblCurrentMapValue.setFont(CoeusFontFactory.getNormalFont());
        lblCurrentMapValue.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblCurrentMapValue.setBorder(new javax.swing.border.EtchedBorder());
        lblCurrentMapValue.setMaximumSize(new java.awt.Dimension(525, 40));
        lblCurrentMapValue.setMinimumSize(new java.awt.Dimension(525, 40));
        lblCurrentMapValue.setPreferredSize(new java.awt.Dimension(525, 40));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 20, 0);
        pnlCurrentTabPane.add(lblCurrentMapValue, gridBagConstraints);

        scrPnCurrentTab.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrPnCurrentTab.setMinimumSize(new java.awt.Dimension(600, 250));
        scrPnCurrentTab.setPreferredSize(new java.awt.Dimension(600, 250));
        pnlCurrentMapList.setLayout(new javax.swing.BoxLayout(pnlCurrentMapList, javax.swing.BoxLayout.Y_AXIS));

        pnlCurrentMapList.setBorder(null);
        pnlCurrentMapHeaderRow.setLayout(new java.awt.GridBagLayout());

        pnlCurrentMapHeaderRow.setMaximumSize(new java.awt.Dimension(600, 30));
        pnlCurrentMapHeaderRow.setMinimumSize(new java.awt.Dimension(600, 20));
        pnlCurrentMapHeaderRow.setPreferredSize(new java.awt.Dimension(600, 20));
        lblCurrentMapHeaderData.setFont(CoeusFontFactory.getLabelFont());
        lblCurrentMapHeaderData.setText("Sequential Stop:   1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        pnlCurrentMapHeaderRow.add(lblCurrentMapHeaderData, gridBagConstraints);

        sptrCurrentMapHeader.setMaximumSize(new java.awt.Dimension(600, 1));
        sptrCurrentMapHeader.setMinimumSize(new java.awt.Dimension(600, 1));
        sptrCurrentMapHeader.setPreferredSize(new java.awt.Dimension(600, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 25, 0, 0);
        pnlCurrentMapHeaderRow.add(sptrCurrentMapHeader, gridBagConstraints);

        pnlCurrentMapList.add(pnlCurrentMapHeaderRow);

        pnlCurrentMapPanelRow.setLayout(new java.awt.GridBagLayout());

        pnlCurrentMapPanelRow.setMaximumSize(new java.awt.Dimension(600, 30));
        pnlCurrentMapPanelRow.setMinimumSize(new java.awt.Dimension(600, 30));
        pnlCurrentMapPanelRow.setPreferredSize(new java.awt.Dimension(600, 30));
        lblApproveBy.setFont(CoeusFontFactory.getNormalFont());
        lblApproveBy.setForeground(RED_COLOR);
        lblApproveBy.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblApproveBy.setText("Approve By");
        lblApproveBy.setMaximumSize(new java.awt.Dimension(150, 20));
        lblApproveBy.setMinimumSize(new java.awt.Dimension(150, 20));
        lblApproveBy.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        pnlCurrentMapPanelRow.add(lblApproveBy, gridBagConstraints);

        lblUserId.setFont(CoeusFontFactory.getLabelFont());
        lblUserId.setText("user");
        lblUserId.setMaximumSize(new java.awt.Dimension(100, 20));
        lblUserId.setMinimumSize(new java.awt.Dimension(100, 20));
        lblUserId.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        pnlCurrentMapPanelRow.add(lblUserId, gridBagConstraints);

        lblDescription.setFont(CoeusFontFactory.getLabelFont());
        lblDescription.setText("Description");
        lblDescription.setMaximumSize(new java.awt.Dimension(450, 20));
        lblDescription.setMinimumSize(new java.awt.Dimension(450, 20));
        lblDescription.setPreferredSize(new java.awt.Dimension(450, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlCurrentMapPanelRow.add(lblDescription, gridBagConstraints);

        pnlCurrentMapList.add(pnlCurrentMapPanelRow);

        scrPnCurrentTab.setViewportView(pnlCurrentMapList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        pnlCurrentTabPane.add(scrPnCurrentTab, gridBagConstraints);

        tbdPnList.addTab("Current", pnlCurrentTabPane);

        pnlSelectListPane.setLayout(new java.awt.GridBagLayout());

        pnlSelectListPane.setFont(CoeusFontFactory.getLabelFont());
        scrPnMapList.setBackground(new java.awt.Color(255, 255, 255));
        scrPnMapList.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrPnMapList.setMinimumSize(new java.awt.Dimension(500, 130));
        scrPnMapList.setPreferredSize(new java.awt.Dimension(500, 130));
        tblMapList.setFont(CoeusFontFactory.getNormalFont());
        tblMapList.setModel(new MapListTableModel());
        tblMapList.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        scrPnMapList.setViewportView(tblMapList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        pnlSelectListPane.add(scrPnMapList, gridBagConstraints);

        scrPnSelectedMapList.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrPnSelectedMapList.setMaximumSize(new java.awt.Dimension(600, 170));
        scrPnSelectedMapList.setMinimumSize(new java.awt.Dimension(600, 170));
        scrPnSelectedMapList.setPreferredSize(new java.awt.Dimension(600, 170));
        pnlSelectedMapList.setLayout(new javax.swing.BoxLayout(pnlSelectedMapList, javax.swing.BoxLayout.Y_AXIS));

        pnlSelectedMapList.setBackground(new java.awt.Color(255, 255, 255));
        pnlSelectedMapList.setBorder(null);
        pnlSelectedMapListHeaderRow.setLayout(new java.awt.GridBagLayout());

        pnlSelectedMapListHeaderRow.setBackground(new java.awt.Color(255, 255, 255));
        pnlSelectedMapListHeaderRow.setMaximumSize(new java.awt.Dimension(600, 20));
        pnlSelectedMapListHeaderRow.setMinimumSize(new java.awt.Dimension(600, 20));
        pnlSelectedMapListHeaderRow.setPreferredSize(new java.awt.Dimension(600, 20));
        sptrSelectedMapListTop.setMaximumSize(new java.awt.Dimension(595, 1));
        sptrSelectedMapListTop.setMinimumSize(new java.awt.Dimension(595, 1));
        sptrSelectedMapListTop.setPreferredSize(new java.awt.Dimension(595, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        pnlSelectedMapListHeaderRow.add(sptrSelectedMapListTop, gridBagConstraints);

        lblSelectedMapListHeaderData.setFont(CoeusFontFactory.getNormalFont());
        lblSelectedMapListHeaderData.setForeground(BLUE_COLOR);
        lblSelectedMapListHeaderData.setText("Sequential Stop:   1");
        lblSelectedMapListHeaderData.setMaximumSize(new java.awt.Dimension(32767, 32767));
        lblSelectedMapListHeaderData.setMinimumSize(new java.awt.Dimension(170, 16));
        lblSelectedMapListHeaderData.setPreferredSize(new java.awt.Dimension(170, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        pnlSelectedMapListHeaderRow.add(lblSelectedMapListHeaderData, gridBagConstraints);

        sptrSelectedMapList.setMaximumSize(new java.awt.Dimension(595, 1));
        sptrSelectedMapList.setMinimumSize(new java.awt.Dimension(595, 1));
        sptrSelectedMapList.setPreferredSize(new java.awt.Dimension(595, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        pnlSelectedMapListHeaderRow.add(sptrSelectedMapList, gridBagConstraints);

        lblSelectedMapDescr.setFont(CoeusFontFactory.getNormalFont());
        lblSelectedMapDescr.setForeground(BLUE_COLOR);
        lblSelectedMapDescr.setText("Description");
        lblSelectedMapDescr.setMaximumSize(new java.awt.Dimension(50, 16));
        lblSelectedMapDescr.setMinimumSize(new java.awt.Dimension(50, 16));
        lblSelectedMapDescr.setPreferredSize(new java.awt.Dimension(50, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlSelectedMapListHeaderRow.add(lblSelectedMapDescr, gridBagConstraints);

        pnlSelectedMapList.add(pnlSelectedMapListHeaderRow);

        pnlSelectedMapListPanelRow.setLayout(new java.awt.GridBagLayout());

        pnlSelectedMapListPanelRow.setBackground(new java.awt.Color(255, 255, 255));
        pnlSelectedMapListPanelRow.setMaximumSize(new java.awt.Dimension(600, 30));
        pnlSelectedMapListPanelRow.setMinimumSize(new java.awt.Dimension(600, 30));
        pnlSelectedMapListPanelRow.setPreferredSize(new java.awt.Dimension(600, 30));
        lblSelectedMapListApproveBy.setFont(CoeusFontFactory.getNormalFont());
        lblSelectedMapListApproveBy.setForeground(RED_COLOR);
        lblSelectedMapListApproveBy.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSelectedMapListApproveBy.setText("Approve By");
        lblSelectedMapListApproveBy.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblSelectedMapListApproveBy.setMaximumSize(new java.awt.Dimension(100, 20));
        lblSelectedMapListApproveBy.setMinimumSize(new java.awt.Dimension(100, 20));
        lblSelectedMapListApproveBy.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        pnlSelectedMapListPanelRow.add(lblSelectedMapListApproveBy, gridBagConstraints);

        lblSelectedMapListUserId.setFont(CoeusFontFactory.getNormalFont());
        lblSelectedMapListUserId.setText("user");
        lblSelectedMapListUserId.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblSelectedMapListUserId.setMaximumSize(new java.awt.Dimension(80, 20));
        lblSelectedMapListUserId.setMinimumSize(new java.awt.Dimension(80, 20));
        lblSelectedMapListUserId.setPreferredSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        pnlSelectedMapListPanelRow.add(lblSelectedMapListUserId, gridBagConstraints);

        lblSelectedMapListDescription.setFont(CoeusFontFactory.getNormalFont());
        lblSelectedMapListDescription.setText("Description");
        lblSelectedMapListDescription.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblSelectedMapListDescription.setMaximumSize(new java.awt.Dimension(32767, 32767));
        lblSelectedMapListDescription.setMinimumSize(new java.awt.Dimension(520, 20));
        lblSelectedMapListDescription.setPreferredSize(new java.awt.Dimension(520, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlSelectedMapListPanelRow.add(lblSelectedMapListDescription, gridBagConstraints);

        pnlSelectedMapList.add(pnlSelectedMapListPanelRow);

        scrPnSelectedMapList.setViewportView(pnlSelectedMapList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        pnlSelectListPane.add(scrPnSelectedMapList, gridBagConstraints);

        btnSelectMap.setFont(CoeusFontFactory.getLabelFont());
        btnSelectMap.setMnemonic('S');
        btnSelectMap.setText("Select Map");
        btnSelectMap.setMaximumSize(new java.awt.Dimension(100, 25));
        btnSelectMap.setMinimumSize(new java.awt.Dimension(100, 25));
        btnSelectMap.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 20);
        pnlSelectListPane.add(btnSelectMap, gridBagConstraints);

        tbdPnList.addTab("Select List", pnlSelectListPane);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(tbdPnList, gridBagConstraints);

        pnlButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        pnlButtons.setMinimumSize(new java.awt.Dimension(10, 40));
        pnlButtons.setPreferredSize(new java.awt.Dimension(10, 40));
        btnRemove.setFont(CoeusFontFactory.getLabelFont());
        btnRemove.setMnemonic('R');
        btnRemove.setText("Remove");
        btnRemove.setMaximumSize(new java.awt.Dimension(80, 25));
        btnRemove.setMinimumSize(new java.awt.Dimension(80, 25));
        btnRemove.setPreferredSize(new java.awt.Dimension(80, 25));
        pnlButtons.add(btnRemove);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(80, 25));
        btnOk.setMinimumSize(new java.awt.Dimension(80, 25));
        btnOk.setPreferredSize(new java.awt.Dimension(80, 25));
        pnlButtons.add(btnOk);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMaximumSize(new java.awt.Dimension(80, 25));
        btnCancel.setMinimumSize(new java.awt.Dimension(80, 25));
        btnCancel.setPreferredSize(new java.awt.Dimension(80, 25));
        pnlButtons.add(btnCancel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(pnlButtons, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOk;
    public javax.swing.JButton btnRemove;
    public javax.swing.JButton btnSelectMap;
    public javax.swing.JLabel lblApproveBy;
    public javax.swing.JLabel lblCurrentMap;
    public javax.swing.JLabel lblCurrentMapHeaderData;
    public javax.swing.JLabel lblCurrentMapValue;
    public javax.swing.JLabel lblDescription;
    public javax.swing.JLabel lblProposalNumber;
    public javax.swing.JLabel lblProposalNumberValue;
    public javax.swing.JLabel lblSelectedMapDescr;
    public javax.swing.JLabel lblSelectedMapListApproveBy;
    public javax.swing.JLabel lblSelectedMapListDescription;
    public javax.swing.JLabel lblSelectedMapListHeaderData;
    public javax.swing.JLabel lblSelectedMapListUserId;
    public javax.swing.JLabel lblSponsor;
    public javax.swing.JLabel lblSponsorValue;
    public javax.swing.JLabel lblUserId;
    public javax.swing.JPanel pnlButtons;
    public javax.swing.JPanel pnlCurrentMapHeaderRow;
    public javax.swing.JPanel pnlCurrentMapList;
    public javax.swing.JPanel pnlCurrentMapPanelRow;
    public javax.swing.JPanel pnlCurrentTabPane;
    public javax.swing.JPanel pnlSelectListPane;
    public javax.swing.JPanel pnlSelectedMapList;
    public javax.swing.JPanel pnlSelectedMapListHeaderRow;
    public javax.swing.JPanel pnlSelectedMapListPanelRow;
    public javax.swing.JScrollPane scrPnCurrentTab;
    public javax.swing.JScrollPane scrPnMapList;
    public javax.swing.JScrollPane scrPnSelectedMapList;
    public javax.swing.JSeparator sptrCurrentMapHeader;
    public javax.swing.JSeparator sptrSelectedMapList;
    public javax.swing.JSeparator sptrSelectedMapListTop;
    public javax.swing.JSeparator sptrTopSperator;
    public javax.swing.JTabbedPane tbdPnList;
    public javax.swing.JTable tblMapList;
    // End of variables declaration//GEN-END:variables
    
    /** Table Model for the Table holding the Unit Maps */
    class MapListTableModel extends DefaultTableModel {
        
        /** Vector of UnitMap beans */
        Vector mapBeans;
        /** Column Indexing */
        final int ID = 0;
        /** Column Indexing */
        final int TYPE = 1;
        /** Column Indexing */
        final int DESCRIPTION = 2;
        
        /** setting column Types */
        private Class columnTypes [] = {String.class,String.class,String.class};
        /** setting column Names */
        private String columnNames [] = {"Id", "Type", "Description"};
        
        /** Constructor */
        MapListTableModel() {
            
        }
        
        /** Return the ColumnClass for the Index
         *@param columnIndex int column Index for which the Column Class is required
         *@return Class
         */
        
        public Class getColumnClass(int columnIndex) {
            return columnTypes [columnIndex];
        }
        
        /** Is the Cell Editable at rowIndex and columnIndex
         * @param rowIndex rowIndex
         * @param columnIndex columnIndex
         * @return if <true> the Cell is editable
         */
        public boolean isCellEditable(int rowIndex, int columnIndex){
            return false;
        }
        
        /** to get the Column Name for a columnIndex
         * @param columnIndex columnIndex for which Column Name is retrieved
         * @return String Column Name
         */
        public String getColumnName(int columnIndex) {
            return columnNames[columnIndex];
        }
        
        /** get the Column Count
         * @return int Column Count
         */
        public int getColumnCount() {
            return columnNames.length;
        }
        
        /** To get the RowCount
         * @return int Row Count
         */
        public int getRowCount() {
            if(mapBeans == null) {
                return 0;
            }
            return mapBeans.size();
        }
        
        /** Set the Vector of beans
         * @param mapBeans Vector of unitMap beans
         */
        public void setData(Vector mapBeans) {
            this.mapBeans = mapBeans;
        }
        
        /** Method to get the value for a row and column
         * @param row int
         * @param column int
         * @return Object
         */
        public Object getValueAt(int row,int column) {
            UnitMapBean unitMapBean = (UnitMapBean) mapBeans.get(row);
            
            switch(column) {
                case ID :
                    return unitMapBean.getMapId()+"";
                case TYPE :
                    //System.out.println("getValueAt "+unitMapBean.getMapType());
                    if(unitMapBean.getMapType().equals("I")) {
                        return "Indirect";
                    }
                    if(unitMapBean.getMapType().equals("D")) {
                        return "Direct";
                    }
                    
                case DESCRIPTION :
                    return unitMapBean.getDescription();
            }
            return null;
        }
        
        /** Method to set the value for a row and column
         * @param value Object
         * @param row int
         * @param column int
         */
        public void setValueAt(Object value,int row,int column) {
            
            UnitMapBean unitMapBean = (UnitMapBean) mapBeans.get(row);
            
            switch(column) {
                case ID :
                    unitMapBean.setMapId(Integer.parseInt(value.toString()));
                    break;
                case TYPE :
                    unitMapBean.setMapType(value.toString());
                    break;
                case DESCRIPTION :
                    unitMapBean.setDescription(value.toString());
                    break;
            }
            
        }
    }
    
    /** ListSelectionListener for the Table */
    class TableListSelectionListener implements javax.swing.event.ListSelectionListener{
        
        /** CAlled when Value changes due to row selection
         * @param listSelectionEvent ListSelectionEvent
         */
        public void valueChanged(javax.swing.event.ListSelectionEvent listSelectionEvent) {
            int  selectedRow = 0;
            javax.swing.ListSelectionModel lsm =(javax.swing.ListSelectionModel) listSelectionEvent.getSource();
            
            try {
                
                if(lsm.equals(tblMapList.getSelectionModel())) {
                    
                    if(tblMapList.getRowCount() > 0) {
                        
                        selectedRow = tblMapList.getSelectedRow();
                        //System.out.println("==selectedRow =="+selectedRow );
                        
                        if(selectedRow >-1) {
                            UnitMapBean unitMapBean =  (UnitMapBean) allUnitMaps.get(selectedRow);
                            vectorOfbean.removeAllElements();
                            vectorOfbean.add(unitMapBean);
                            
                            setSelectedSequentialDetails(vectorOfbean,true);
                        }
                    }
                }
                
            }catch (Exception e){
                e.printStackTrace();
                e.getMessage();
            }
            
            
        }
        
        
    }// end of class
    
    
    /** TableHeader Renderer instance */
    class TableHeaderRenderer extends DefaultTableCellRenderer {
        
        /** Label instance */
        private JLabel label;
        /** TableHeaderRenderer */
        TableHeaderRenderer(){
            label = new JLabel();
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setFont(CoeusFontFactory.getLabelFont());
            label.setBorder(BorderFactory.createRaisedBevelBorder());
        }
        
        /** return TableCellRendererComponent
         * @param table Table instance
         * @param value Object
         * @param isSelected boolean
         * @param hasFocus boolean
         * @param row int
         * @param column int
         * @return Component
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            label.setText(value.toString());
            if(column == 2) {
                label.setHorizontalAlignment(JLabel.LEFT);
            }
            
            return label;
        }
        
    }
    
}
