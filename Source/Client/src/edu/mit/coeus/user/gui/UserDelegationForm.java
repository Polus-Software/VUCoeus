/*
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * UserDelegationForm.java
 *
 * Created on June 26, 2003, 11:51 AM
 */

package edu.mit.coeus.user.gui;

import edu.mit.coeus.exception.CoeusException;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.user.bean.UserDelegationController;
import edu.mit.coeus.user.bean.UserDelegationsBean;

/** Using this UI a logged in user can:
 * 1) Request/Delegate work to others.
 * 2) Accept/Deny work delegated to him.
 * @author senthil/sharath
 */
public class UserDelegationForm extends javax.swing.JComponent{
    
    private CoeusDlgWindow userDelegationDlgWindow;
    private UserDelegationMediator userDelegationMediator;
    private UserDelegationController userDelegationController;
    private CoeusMessageResources coeusMessageResources;
    private ResponsibilitiesTableModel responsibilitiesTableModel;
    private ResponsibilitiesRenderer responsibilitiesRenderer;
    private DateUtils dateUtils;
    private String lastDate =EMPTY;
    
    private Vector delegations, delegatedTo;
    private UserDelegationsBean userDelegationsBean;
    
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    private Frame owner;
    private boolean modal;
    private boolean dirty;
    
    private static final String DEFAULT_TITLE = "Delegations";
    
    private static final int WIDTH = 600;
    private static final int HEIGHT = 350;
    
    private static final String EMPTY = "";
    
    //class Variables --> Messages
    private static final String BASE = "";
    private static final String COULD_NOT_RETREIVE_DELEGATIONS = BASE+"3001";//"An error occurred retrieving delegations";
    
    private static final String NOT_DELEGATED = "Your approval responsiblities are NOT delegated to any other user.";
    private static final String CLICK_DELEGATE = "To delegate your responsibilities, choose a user and click Delegate.";
    private static final String CURRENTLY_DELEGATED = "Your approval responsibilities are currently delegated.";
    private static final String MORE_THAN_ONE_DELEGATIONS_FOUND = "An error occurred retrieving delegation. More than one active or requested delegation was found";
    private static final String CANNOT_DELEGATE_WORK = "You may not delegate your work because other users have delegated work to you.";
    private static final String DELEGATE_WORK = "Are you sure you want to delegate your work?";
    private static final String ENTER_DELEGATED_TO = "A delegated-to person must be entered";
    private static final String CANNOT_DELEGATE_TO_SELF = "You may not delegate work to yourself";
    private static final String ENTER_EFFECTIVE_DATE = "An effective date must be entered for the delegation";
    private static final String DELEGATED_TO_SOMEONE = "This person has delegated work to someone else.";
    private static final String ACCEPTED_CANNOT_DELEGATE = "You have accepted delegated work.  You cannot delegate to someone else";
    private static final String PREVIOUSLY_REJECTED = "This person has previously rejected your delegation";
    private static final String INVALID_DATE = " is an invalid date. Please Input a valid effective date";
    private static final String ENTER_LATER_DATE = "The delegation effective date must be later than today";
    private static final String APPROVAL_RESPONSIBILITIES_DELEGATED = "Your approval responsibilities are currently delegated";
    private static final String DELEGATION_FAILED = "The delegation process failed";
    private static final String END_DELEGATION = "Are you sure you want to end your delegation?";
    private static final String SELECT_DELEGATION_ROW = "Please select a delegation row";
    private static final String CANNOT_CHANGE_STATUS_OF_DELEGATION = "You may not change the status of this delegation";
    private static final String ACCEPT_DELEGATIONS = "Are you sure you want to accept this delegation?";
    private static final String REJECT_DELEGATIONS = "Are you sure you want to reject this delegation?";
    private static final String DELEGATION_UPDATED = "The delegation has been successfully updated";
    private static final String SAVE_CHANGES = "Do you want to save the delegation? ";
    
    private static final Color COLOR_BACKGROUND = (Color) UIManager.getDefaults().get("Pane.background");
    private static final Color COLOR_FOREGROUND = Color.black;
    
    //status
    private static final char NOT_ACCEPTED = 'Q';
    private static final char REQUESTED = 'Q';
    private static final char ACCEPTED = 'P';
    private static final char REJECTED = 'R';
    private static final char CLOSED = 'C';
    
    //Status Text
    private static final String MSG_REQUESTED = "Requested";
    private static final String MSG_ACCEPTED = "Accepted";
    private static final String MSG_REJECTED = "Rejected";
    private static final String MSG_CLOSED = "Closed";
    
    //Table Column Indexes
    private static final int DELEGATED_BY_COLUMN = 0;
    private static final int EFFECTIVE_DATE_COLUMN = 1;
    private static final int END_DATE_COLUMN = 2;
    private static final int STATUS_COLUMN = 3;
    
    private static final int NONE_SELECTED = -1;
    
    /** Creates new form UserDelegationForm
     * @param owner owner
     * @param modal modality
     */
    public UserDelegationForm(Frame owner, boolean modal) {
        this.owner = owner;
        this.modal = modal;
        initComponents();
        postInitComponents();
    }
    
    /** sets up the Delegated Responsibilities Table.
     */
    private void setTable() {
        if(delegations != null) {
        //Setting up Delegations Table
        Class colTypes[] = {String.class, Date.class, Date.class, String.class};
        String colNames[] = {"Delegated By", "Effective Date", "End Date", "Status"};
        responsibilitiesTableModel = new ResponsibilitiesTableModel(colTypes, colNames);
        tblResponsibilities.setModel(responsibilitiesTableModel);
        
        tblResponsibilities.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        
        responsibilitiesRenderer = new ResponsibilitiesRenderer();
        tblResponsibilities.getColumnModel().getColumn(STATUS_COLUMN).setCellRenderer(responsibilitiesRenderer);
        }
    }
    
    /** creates a CoeusDialog and
     * registers components with event handlers.
     */
    private void postInitComponents() {
        //Setting Colors
        txtUserName.setBackground(COLOR_BACKGROUND);
        txtUserName.setDisabledTextColor(COLOR_FOREGROUND);
        txtStatus.setBackground(COLOR_BACKGROUND);
        
        txtStatus.setDisabledTextColor(COLOR_FOREGROUND);
        tblResponsibilities.setBackground(COLOR_BACKGROUND);
        
        //Setting focus traversal...
        java.awt.Component[] components = {txtUserId, txtEffectiveDt, txtUserName, btnUserSearch, tblResponsibilities, btnClose,btnDelegate,btnRemove,btnAccept,btnDeny
        /*Added for Case#3682 - Enhancements related to Delegations - Start*/ 
        ,btnDelete
        /*Added for Case#3682 - Enhancements related to Delegations - End*/};        
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true); 
        
        userDelegationController = new UserDelegationController();
        coeusMessageResources = CoeusMessageResources.getInstance();
        dateUtils = new DateUtils();
        
        //Will Always be disabled
        txtUserName.setEnabled(false);
        txtStatus.setEnabled(false);
        
        //Registering Components with Listener
        userDelegationMediator = new UserDelegationMediator();
        btnAccept.addActionListener(userDelegationMediator);
        btnClose.addActionListener(userDelegationMediator);
        btnDelegate.addActionListener(userDelegationMediator);
        btnDeny.addActionListener(userDelegationMediator);
        //Added for Case#3682 - Enhancements related to Delegations - Start
        btnDelete.addActionListener(userDelegationMediator);
        //Added for Case#3682 - Enhancements related to Delegations - End
        btnRemove.addActionListener(userDelegationMediator);
        btnUserSearch.addActionListener(userDelegationMediator);
        
        txtUserId.addMouseListener(userDelegationMediator);
        txtUserId.addFocusListener(userDelegationMediator);
        txtEffectiveDt.addFocusListener(userDelegationMediator);
        
        //Setting up Dialog to display UserDelegations
        userDelegationDlgWindow = new CoeusDlgWindow(owner, modal);
        userDelegationDlgWindow.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        userDelegationDlgWindow.addWindowListener(userDelegationMediator);
        userDelegationDlgWindow.getContentPane().add(this);
        setTitle(DEFAULT_TITLE);
        userDelegationDlgWindow.setSize(WIDTH,HEIGHT);
        userDelegationDlgWindow.setResizable(false);
        
        //Bug Fix: Reordering & Font - START
        tblResponsibilities.getTableHeader().setReorderingAllowed(false);
        tblResponsibilities.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        //Bug Fix: Reordering & Font - END
        
    }
    
    /** Fills the details in the  delegated to Panel.
     * and Delegated Responsibilities.
     */
    public void setDelegations() {
        try{
            Vector data = (Vector)userDelegationController.getDelegations(mdiForm.getUserId());
            delegatedTo = (Vector)data.get(0); //Login User delegated work to other User
            delegations = (Vector)data.get(1); //users who have delegated work to Login User
            
            //Checks for Delegated To
            if(delegatedTo == null || delegatedTo.size() < 1) {
                btnRemove.setEnabled(false);
                lblStatus.setVisible(false);
                txtStatus.setVisible(false);
                
                setEnabledDelegation(true);
                
                lblDelegation1.setText(NOT_DELEGATED);
                lblDelegation2.setText(CLICK_DELEGATE);
            }
            else if(delegatedTo.size() == 1) {
                displayDelegatedTo((UserDelegationsBean)delegatedTo.get(0));
            }
            else if(delegatedTo.size() > 1) {
                CoeusOptionPane.showErrorDialog(userDelegationDlgWindow, MORE_THAN_ONE_DELEGATIONS_FOUND);
                displayDelegatedTo((UserDelegationsBean)delegatedTo.get(0));
            }
            //End Checks for Delegated To
            
            //Checks for Delegated By
            int delegationsSize;
            if(delegations != null) {
                delegationsSize= delegations.size();
            }
            else {
                delegationsSize = 0;
            }
            lblResponsibilities.setText("Responsibilities delegated to you ( "+mdiForm.getFullName()+" )");
            
            if(delegationsSize < 1) {
                //Accept and Deny Buttons should be made visible
                btnAccept.setVisible(false);
                btnDeny.setVisible(false);
                //Added for Case#3682 - Enhancements related to Delegations - Start
                btnDelete.setVisible(false);
                //Added for Case#3682 - Enhancements related to Delegations - End
            }
            else if(delegationsSize >= 1) {
                btnAccept.setEnabled(false);
                btnDeny.setEnabled(false);                
                for(int index = 0; index < delegationsSize; index++) {
                    userDelegationsBean = (UserDelegationsBean)delegations.get(index);
                    //Added for Case#3682 - Enhancements related to Delegations - Start
//                    if(userDelegationsBean.getStatus() == REJECTED || userDelegationsBean.getStatus() == CLOSED){
//                        btnDelete.setEnabled(true);
//                    }else{
//                        btnDelete.setEnabled(false);
//                    }
                   //Added for Case#3682 - Enhancements related to Delegations - End
                    if(userDelegationsBean.getStatus() == REQUESTED || userDelegationsBean.getStatus() == ACCEPTED) {
                        lblDelegation1.setText(NOT_DELEGATED);
                        lblDelegation2.setText(CANNOT_DELEGATE_WORK);
                        
                        btnRemove.setEnabled(false);
                        setEnabledDelegation(false);
                        btnAccept.setEnabled(true);
                        btnDeny.setEnabled(true);                        
                        break;
                    }//end if                    
                }//end for
            }
            //End Checks for Delegated By
            
        }catch (CoeusClientException coeusClientException) {
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(COULD_NOT_RETREIVE_DELEGATIONS));
            coeusClientException.printStackTrace();
        }
    }
    
    /** Displays the Dialog
     */
    public void display() {
        reset();
        setDelegations();
        setTable();
        
        userDelegationDlgWindow.setLocation(CoeusDlgWindow.CENTER);
        
        userDelegationDlgWindow.setVisible(true);
    }
    
    private void reset() {
        lastDate = EMPTY;
        txtUserId.setText(EMPTY);
        txtUserName.setText(EMPTY);
        txtStatus.setText(EMPTY);
        txtEffectiveDt.setText(EMPTY);
        dirty = false;
    }
    
    /** Sets the Title for the Dialog
     * @param title Dialog title
     */
    public void setTitle(String title) {
        userDelegationDlgWindow.setTitle(title);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        sptrBar = new javax.swing.JSeparator();
        pnlDetail = new javax.swing.JPanel();
        pnlUser = new javax.swing.JPanel();
        lblDelegated = new javax.swing.JLabel();
        lblEffectiveDt = new javax.swing.JLabel();
        txtUserId = new javax.swing.JTextField();
        txtEffectiveDt = new javax.swing.JTextField();
        txtUserName = new javax.swing.JTextField();
        btnUserSearch = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();
        txtStatus = new javax.swing.JTextField();
        pnlLabel = new javax.swing.JPanel();
        lblResponsibilities = new javax.swing.JLabel();
        pnlButtons = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnDelegate = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        scrPnResponsibilities = new javax.swing.JScrollPane();
        tblResponsibilities = new javax.swing.JTable();
        btnDeny = new javax.swing.JButton();
        btnAccept = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        lblDelegation1 = new javax.swing.JLabel();
        lblDelegation2 = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        setMaximumSize(new java.awt.Dimension(700, 450));
        setMinimumSize(new java.awt.Dimension(650, 450));
        setPreferredSize(new java.awt.Dimension(600, 350));
        sptrBar.setMaximumSize(new java.awt.Dimension(650, 2));
        sptrBar.setMinimumSize(new java.awt.Dimension(600, 2));
        sptrBar.setPreferredSize(new java.awt.Dimension(650, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        add(sptrBar, gridBagConstraints);

        pnlDetail.setLayout(new java.awt.GridBagLayout());

        pnlDetail.setMaximumSize(new java.awt.Dimension(650, 300));
        pnlDetail.setMinimumSize(new java.awt.Dimension(650, 300));
        pnlDetail.setPreferredSize(new java.awt.Dimension(600, 300));
        pnlUser.setLayout(new java.awt.GridBagLayout());

        pnlUser.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        pnlUser.setMaximumSize(new java.awt.Dimension(500, 75));
        pnlUser.setMinimumSize(new java.awt.Dimension(500, 75));
        pnlUser.setPreferredSize(new java.awt.Dimension(500, 75));
        lblDelegated.setFont(CoeusFontFactory.getLabelFont());
        lblDelegated.setText("Delegated To: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlUser.add(lblDelegated, gridBagConstraints);

        lblEffectiveDt.setFont(CoeusFontFactory.getLabelFont());
        lblEffectiveDt.setText("Effective On: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        pnlUser.add(lblEffectiveDt, gridBagConstraints);

        txtUserId.setFont(CoeusFontFactory.getNormalFont());
        txtUserId.setMaximumSize(new java.awt.Dimension(150, 20));
        txtUserId.setMinimumSize(new java.awt.Dimension(150, 20));
        txtUserId.setPreferredSize(new java.awt.Dimension(150, 20));
        pnlUser.add(txtUserId, new java.awt.GridBagConstraints());

        txtEffectiveDt.setFont(CoeusFontFactory.getNormalFont());
        txtEffectiveDt.setMaximumSize(new java.awt.Dimension(150, 20));
        txtEffectiveDt.setMinimumSize(new java.awt.Dimension(150, 20));
        txtEffectiveDt.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        pnlUser.add(txtEffectiveDt, gridBagConstraints);

        txtUserName.setFont(CoeusFontFactory.getNormalFont());
        txtUserName.setMaximumSize(new java.awt.Dimension(200, 20));
        txtUserName.setMinimumSize(new java.awt.Dimension(200, 20));
        txtUserName.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        pnlUser.add(txtUserName, gridBagConstraints);

        btnUserSearch.setFont(CoeusFontFactory.getLabelFont());
        btnUserSearch.setIcon(new ImageIcon(getClass().getClassLoader().getResource(
            CoeusGuiConstants.SEARCH_ICON)));
btnUserSearch.setMaximumSize(new java.awt.Dimension(43, 20));
btnUserSearch.setMinimumSize(new java.awt.Dimension(43, 20));
btnUserSearch.setPreferredSize(new java.awt.Dimension(25, 20));
gridBagConstraints = new java.awt.GridBagConstraints();
gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
pnlUser.add(btnUserSearch, gridBagConstraints);

lblStatus.setFont(CoeusFontFactory.getLabelFont());
lblStatus.setText("Status");
gridBagConstraints = new java.awt.GridBagConstraints();
gridBagConstraints.gridx = 2;
gridBagConstraints.gridy = 1;
gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
pnlUser.add(lblStatus, gridBagConstraints);

txtStatus.setFont(CoeusFontFactory.getNormalFont());
txtStatus.setPreferredSize(new java.awt.Dimension(150, 22));
gridBagConstraints = new java.awt.GridBagConstraints();
gridBagConstraints.gridx = 3;
gridBagConstraints.gridy = 1;
gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
pnlUser.add(txtStatus, gridBagConstraints);

gridBagConstraints = new java.awt.GridBagConstraints();
gridBagConstraints.gridx = 0;
gridBagConstraints.gridy = 0;
gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
pnlDetail.add(pnlUser, gridBagConstraints);

pnlLabel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

pnlLabel.setMaximumSize(new java.awt.Dimension(400, 25));
pnlLabel.setMinimumSize(new java.awt.Dimension(400, 25));
pnlLabel.setPreferredSize(new java.awt.Dimension(400, 25));
lblResponsibilities.setFont(CoeusFontFactory.getLabelFont());
lblResponsibilities.setForeground(java.awt.Color.blue);
lblResponsibilities.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
lblResponsibilities.setText("Responsibilities delegated to you ");
pnlLabel.add(lblResponsibilities);

gridBagConstraints = new java.awt.GridBagConstraints();
gridBagConstraints.gridx = 0;
gridBagConstraints.gridy = 1;
gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
pnlDetail.add(pnlLabel, gridBagConstraints);

pnlButtons.setLayout(new java.awt.GridBagLayout());

pnlButtons.setMaximumSize(new java.awt.Dimension(100, 75));
pnlButtons.setMinimumSize(new java.awt.Dimension(100, 75));
pnlButtons.setPreferredSize(new java.awt.Dimension(100, 75));
btnClose.setFont(CoeusFontFactory.getLabelFont());
btnClose.setMnemonic('C');
btnClose.setText("Close");
btnClose.setMaximumSize(new java.awt.Dimension(90, 30));
btnClose.setMinimumSize(new java.awt.Dimension(90, 30));
btnClose.setPreferredSize(new java.awt.Dimension(90, 25));
gridBagConstraints = new java.awt.GridBagConstraints();
gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
gridBagConstraints.weightx = 1.0;
pnlButtons.add(btnClose, gridBagConstraints);

btnDelegate.setFont(CoeusFontFactory.getLabelFont());
btnDelegate.setMnemonic('D');
btnDelegate.setText("Delegate");
btnDelegate.setMaximumSize(new java.awt.Dimension(90, 30));
btnDelegate.setMinimumSize(new java.awt.Dimension(90, 30));
btnDelegate.setPreferredSize(new java.awt.Dimension(90, 25));
gridBagConstraints = new java.awt.GridBagConstraints();
gridBagConstraints.gridx = 0;
gridBagConstraints.gridy = 1;
gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
gridBagConstraints.weightx = 1.0;
pnlButtons.add(btnDelegate, gridBagConstraints);

btnRemove.setFont(CoeusFontFactory.getLabelFont());
btnRemove.setMnemonic('R');
btnRemove.setText("Remove");
btnRemove.setMaximumSize(new java.awt.Dimension(90, 30));
btnRemove.setMinimumSize(new java.awt.Dimension(90, 30));
btnRemove.setPreferredSize(new java.awt.Dimension(90, 25));
gridBagConstraints = new java.awt.GridBagConstraints();
gridBagConstraints.gridx = 0;
gridBagConstraints.gridy = 2;
gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
gridBagConstraints.weightx = 1.0;
pnlButtons.add(btnRemove, gridBagConstraints);

gridBagConstraints = new java.awt.GridBagConstraints();
gridBagConstraints.gridheight = 2;
gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
gridBagConstraints.weightx = 1.0;
gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
pnlDetail.add(pnlButtons, gridBagConstraints);

scrPnResponsibilities.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
scrPnResponsibilities.setMaximumSize(new java.awt.Dimension(500, 200));
scrPnResponsibilities.setMinimumSize(new java.awt.Dimension(500, 200));
scrPnResponsibilities.setPreferredSize(new java.awt.Dimension(500, 200));
tblResponsibilities.setModel(new javax.swing.table.DefaultTableModel(
    new Object [][] {

    },
    new String [] {
        "Delegated By", "Effective Date", "End Date", "Status"
    }
    ) {
        boolean[] canEdit = new boolean [] {
            false, false, false, false
        };

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    scrPnResponsibilities.setViewportView(tblResponsibilities);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridheight = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weighty = 1.0;
    pnlDetail.add(scrPnResponsibilities, gridBagConstraints);

    btnDeny.setFont(CoeusFontFactory.getLabelFont());
    btnDeny.setText("Deny");
    btnDeny.setMaximumSize(new java.awt.Dimension(90, 25));
    btnDeny.setMinimumSize(new java.awt.Dimension(90, 25));
    btnDeny.setPreferredSize(new java.awt.Dimension(90, 25));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    pnlDetail.add(btnDeny, gridBagConstraints);

    btnAccept.setFont(CoeusFontFactory.getLabelFont());
    btnAccept.setText("Accept");
    btnAccept.setMaximumSize(new java.awt.Dimension(90, 25));
    btnAccept.setMinimumSize(new java.awt.Dimension(90, 25));
    btnAccept.setPreferredSize(new java.awt.Dimension(90, 25));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    pnlDetail.add(btnAccept, gridBagConstraints);

    btnDelete.setFont(CoeusFontFactory.getLabelFont());
    btnDelete.setMnemonic('e');
    btnDelete.setText("Delete");
    btnDelete.setMaximumSize(new java.awt.Dimension(90, 25));
    btnDelete.setMinimumSize(new java.awt.Dimension(90, 25));
    btnDelete.setPreferredSize(new java.awt.Dimension(90, 25));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    pnlDetail.add(btnDelete, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weighty = 1.0;
    add(pnlDetail, gridBagConstraints);

    lblDelegation1.setFont(CoeusFontFactory.getLabelFont());
    lblDelegation1.setForeground(java.awt.Color.blue);
    lblDelegation1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    lblDelegation1.setText("Your approval responsibilities are not Delegated to any other user");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
    add(lblDelegation1, gridBagConstraints);

    lblDelegation2.setFont(CoeusFontFactory.getLabelFont());
    lblDelegation2.setForeground(java.awt.Color.blue);
    lblDelegation2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    lblDelegation2.setText("To delegate your responsibilities, choose a user and click delegate.");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
    add(lblDelegation2, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAccept;
    public javax.swing.JButton btnClose;
    public javax.swing.JButton btnDelegate;
    public javax.swing.JButton btnDelete;
    public javax.swing.JButton btnDeny;
    public javax.swing.JButton btnRemove;
    public javax.swing.JButton btnUserSearch;
    public javax.swing.JLabel lblDelegated;
    public javax.swing.JLabel lblDelegation1;
    public javax.swing.JLabel lblDelegation2;
    public javax.swing.JLabel lblEffectiveDt;
    public javax.swing.JLabel lblResponsibilities;
    public javax.swing.JLabel lblStatus;
    public javax.swing.JPanel pnlButtons;
    public javax.swing.JPanel pnlDetail;
    public javax.swing.JPanel pnlLabel;
    public javax.swing.JPanel pnlUser;
    public javax.swing.JScrollPane scrPnResponsibilities;
    public javax.swing.JSeparator sptrBar;
    public javax.swing.JTable tblResponsibilities;
    public javax.swing.JTextField txtEffectiveDt;
    public javax.swing.JTextField txtStatus;
    public javax.swing.JTextField txtUserId;
    public javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables
    
    /** For Testing Purpose Only
     * @param s Command Line String array
     */
    public static void main(String s[]) {
        UserDelegationForm userDelegationForm = new UserDelegationForm(new javax.swing.JFrame(),true);
        userDelegationForm.setTitle("Testing");
        userDelegationForm.display();
    }
    
    /** enables or disables the Delegated To Panel and Delegate Button.
     * @param enabled true/false used to enable/disable the Delegated To Panel.
     */
    private void setEnabledDelegation(boolean enabled) {
        txtUserId.setEnabled(enabled);
        txtEffectiveDt.setEnabled(enabled);
        btnUserSearch.setEnabled(enabled);
        btnDelegate.setEnabled(enabled);
    }
    
    /** Displays Information in Delegated To Panel
     * from the userDelegationBean.
     * @param userDelegationsBean UserDelegationBean from which the data has to be displayed.
     */
    public void displayDelegatedTo(UserDelegationsBean userDelegationsBean) {
        lblDelegation1.setText(EMPTY);
        lblDelegation2.setText(CURRENTLY_DELEGATED);
        
        //userDelegationsBean = (UserDelegationsBean)delegatedTo.get(0);
        txtUserId.setText(userDelegationsBean.getDelegatedTo());
        txtUserName.setText(userDelegationsBean.getUserName());
        
        Date effectDate = userDelegationsBean.getEffectiveDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(effectDate);
        lastDate = (calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.DATE)+"/"+calendar.get(Calendar.YEAR);
        
        txtEffectiveDt.setText(dateUtils.formatDate(lastDate, "/-:", "dd-MMM-yyyy"));
        
        String status = EMPTY;
        switch (userDelegationsBean.getStatus()) {
            case REQUESTED:
                status = MSG_REQUESTED;
                break;
            case REJECTED:
                status = MSG_REJECTED;
                break;
            case ACCEPTED:
                status = MSG_ACCEPTED;
                break;
            case CLOSED:
                status = MSG_CLOSED;
                break;
        }
        txtStatus.setText(status);
        
        if(userDelegationsBean.getEffectiveDate().after(new Date(System.currentTimeMillis() - (24*60*60*1000))) && userDelegationsBean.getStatus()==NOT_ACCEPTED) {
            btnRemove.setEnabled(true);
            //Modified for Case#3682 - Enhancements related to Delegations - Start
            setEnabledDelegation(false);
            //Modified for Case#3682 - Enhancements related to Delegations - End
            btnDelegate.setEnabled(false);
        }
        else {
            //btnRemove.setEnabled(false);
            setEnabledDelegation(false);
        }
    }
    
    /** Invoked when Close button is clicked.
     */
    private void close() {
        //System.out.println("Close");
        //userDelegationDlgWindow.setVisible(false);
        if(dirty && btnDelegate.isEnabled()) {
            int selection = CoeusOptionPane.showQuestionDialog(SAVE_CHANGES, CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
            if(selection == CoeusOptionPane.SELECTION_YES) {
                userDelegationMediator.delegate();
                return ;
            }
        }
        userDelegationDlgWindow.dispose();
    }
    
    /** Mediator for User Delegation
     */
    private class UserDelegationMediator extends WindowAdapter
    implements ActionListener, MouseListener, FocusListener{
        
        /** Source of actionEvent.
         */
        private Object source;
        
        private CoeusSearch coeusSearch;
        
        private SimpleDateFormat simDateFormat;
        private ParsePosition parsePosition;
        
        
        private static final String USER_SEARCH = "USERSEARCH";
        private static final String USER_ID_KEY = "USER_ID";
        private static final String USER_NAME_KEY = "USER_NAME";
        
        /** creates a new instance of DelegationMediator.
         */
        UserDelegationMediator() {
            DateFormat dateFormat = DateFormat.getInstance();
            //dateFormat.setLenient(false);
            parsePosition = new ParsePosition(0);
            simDateFormat = (SimpleDateFormat)dateFormat;
            //simDateFormat.applyPattern("MM/dd/yyyy");
            simDateFormat.applyPattern("dd-MMM-yyyy");
        }
        
        /** Invoked when an action occurs.
         * @param actionEvent ActionEvent
         */
        public void actionPerformed(ActionEvent actionEvent) {
            source = actionEvent.getSource();
            if(source.equals(btnAccept)) accept();
            else if(source.equals(btnClose)) close();
            else if(source.equals(btnDelegate)) delegate();
            else if(source.equals(btnDeny)) deny();
            else if(source.equals(btnRemove)) remove();
            else if(source.equals(btnUserSearch)) userSearch();
            //Added for Case#3682 - Enhancements related to Delegations - Start
            else if(source.equals(btnDelete)) delete();
            //Added for Case#3682 - Enhancements related to Delegations - End
        }
        
        //Added for Case#3682 - Enhancements related to Delegations - Start
        /*
         * Delete Delegations of status 'Closed' and 'Rejected'
         */
        private void delete(){
            
            int selectedRow;
            selectedRow = tblResponsibilities.getSelectedRow();
            if(selectedRow == NONE_SELECTED) {
                CoeusOptionPane.showInfoDialog(SELECT_DELEGATION_ROW);
                return ;
            }
            //Some Row Selected Go Ahead
            userDelegationsBean = (UserDelegationsBean)delegations.get(selectedRow);
            String status = EMPTY;
            switch (userDelegationsBean.getStatus()) {
                case REQUESTED:
                    status = MSG_REQUESTED;
                    break;
                case REJECTED:
                    status = MSG_REJECTED;
                    break;
                case ACCEPTED:
                    status = MSG_ACCEPTED;
                    break;
                case CLOSED:
                    status = MSG_CLOSED;
                    break;
            }
            if(userDelegationsBean.getStatus() != CLOSED && userDelegationsBean.getStatus() != REJECTED  ) {
                CoeusOptionPane.showErrorDialog("Cannot delete delegation of status "+ status );
                return ;
            }
            int selection = CoeusOptionPane.showQuestionDialog("Do you want to delete the selected delegation ?",
                    CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_OK);
            if(selection == CoeusOptionPane.SELECTION_NO) return ;
            
            userDelegationsBean.setAw_DelegatedBy(userDelegationsBean.getDelegatedBy());
            userDelegationsBean.setAw_DelegatedTo(userDelegationsBean.getDelegatedTo());
            userDelegationsBean.setAw_EffectiveDate(userDelegationsBean.getEffectiveDate());
            userDelegationsBean.setAw_Status(userDelegationsBean.getStatus());
            try {
                userDelegationController.deleteDelegation(userDelegationsBean);
            } catch (CoeusException ex) {
                ex.printStackTrace();
            } catch (CoeusClientException ex) {
                ex.printStackTrace();
            }
            setDelegations();
            responsibilitiesTableModel.fireTableDataChanged();
        }
        //Added for Case#3682 - Enhancements related to Delegations - End
        
        /** Invoked when Accept button is clicked.
         * Accepts a selected Responsibility.
         */
        private void accept() {
            //System.out.println("Accept");
            changeStatus(ACCEPTED);
            //Added for Case#3682 - Enhancements related to Delegations - Start
            setDelegations();
            responsibilitiesTableModel.fireTableDataChanged();
            //Added for Case#3682 - Enhancements related to Delegations - End
            
        }
        
        /** changes the status of the selected Responsibility Delegated as
         * either Accepted/Denied.
         * @param status The following values are allowed:
         * 1) ACCEPTED if the responsibility has been accepted.
         * 2) REJECTED if the responsibility has been denied.
         *
         * else throws IllegalArgumentException
         */
        private void changeStatus(char status) {
            if(status != ACCEPTED && status != REJECTED) {
                throw new IllegalArgumentException("Status should be either ACCEPTED(P) or REJECTED(R)");
            }
            
            int selectedRow;
            selectedRow = tblResponsibilities.getSelectedRow();
            if(selectedRow == NONE_SELECTED) {
                CoeusOptionPane.showInfoDialog(SELECT_DELEGATION_ROW);
                return ;
            }
            //Some Row Selected Go Ahead
            userDelegationsBean = (UserDelegationsBean)delegations.get(selectedRow);
            if(userDelegationsBean.getStatus() != REQUESTED) {
                CoeusOptionPane.showErrorDialog(CANNOT_CHANGE_STATUS_OF_DELEGATION);
                return ;
            }
            int selection = CoeusOptionPane.SELECTION_NO;
            if(status == ACCEPTED) {
                selection = CoeusOptionPane.showQuestionDialog(ACCEPT_DELEGATIONS, CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
            }
            else if(status == REJECTED) {
                selection = CoeusOptionPane.showQuestionDialog(REJECT_DELEGATIONS, CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
            }
            if(selection == CoeusOptionPane.SELECTION_NO) return ;
            //Selected Yes Go Ahead with saving
            //set AW Properties
            userDelegationsBean.setAw_DelegatedBy(userDelegationsBean.getDelegatedBy());
            userDelegationsBean.setAw_DelegatedTo(userDelegationsBean.getDelegatedTo());
            userDelegationsBean.setAw_EffectiveDate(userDelegationsBean.getEffectiveDate());
            userDelegationsBean.setAw_Status(userDelegationsBean.getStatus());
            
            userDelegationsBean.setStatus(status);
            
            try{
                userDelegationController.updateDelegatedStatus(userDelegationsBean);
            }catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        
        /** Invoked when Delegate button is clicked.
         * Delegates Responsibilities to other users.
         */
        private void delegate() {
            //System.out.println("Delegate");
            int selection = CoeusOptionPane.showQuestionDialog(DELEGATE_WORK, CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_OK);
            if(selection == CoeusOptionPane.SELECTION_NO) return ;
            //Clicked Yes
            if(txtUserId.getText().trim().equals(EMPTY)) {
                CoeusOptionPane.showInfoDialog(ENTER_DELEGATED_TO);
                return ;
            }
            if(txtUserId.getText().trim().equals(mdiForm.getUserId().trim())) {
                CoeusOptionPane.showInfoDialog(CANNOT_DELEGATE_TO_SELF);
                txtUserId.setText(EMPTY);
                return ;
            }
            if(txtEffectiveDt.getText().trim().equals(EMPTY)) {
                CoeusOptionPane.showInfoDialog(ENTER_EFFECTIVE_DATE);
                return ;
            }
            //check for date being less than today.
            Date effectiveDate=null;
            try{
                //effectiveDate = simDateFormat.parse(lastDate);
                effectiveDate = simDateFormat.parse(txtEffectiveDt.getText());
                
                if(effectiveDate == null) {
                    CoeusOptionPane.showInfoDialog(txtEffectiveDt.getText()+INVALID_DATE);
                    txtEffectiveDt.setText(EMPTY);
                    return ;
                }
                int compare = effectiveDate.compareTo(new Date(System.currentTimeMillis() - (24*60*60*1000)));
                if(compare < 0 ) {
                    CoeusOptionPane.showInfoDialog(ENTER_LATER_DATE);
                    return ;
                }
            }catch (ParseException parseException) {
                
            }
            
            try{
                int value = userDelegationController.canDelegate(mdiForm.getUserId(), txtUserId.getText(), effectiveDate);
                if(value == 1) {
                    //can Save
                    UserDelegationsBean userDelegationsBean = new UserDelegationsBean();
                    userDelegationsBean.setDelegatedBy(mdiForm.getUserId());
                    userDelegationsBean.setDelegatedTo(txtUserId.getText().trim());
                    userDelegationsBean.setEffectiveDate(new java.sql.Date(effectiveDate.getTime()));
                    userDelegationsBean.setStatus(REQUESTED);
                    try{
                        userDelegationController.create(userDelegationsBean);
                        //Disable Delegate to
                        lblStatus.setVisible(true);
                        txtStatus.setVisible(true);
                        txtStatus.setText(MSG_REQUESTED);
                        setEnabledDelegation(false);
                        //btnRemove.setEnabled(true);
                        
                        CoeusOptionPane.showInfoDialog(DELEGATION_UPDATED);
                        lblDelegation1.setText(EMPTY);
                        lblDelegation2.setText(CURRENTLY_DELEGATED);
                    }catch (CoeusClientException coeusClientException) {
                        CoeusOptionPane.showInfoDialog(DELEGATION_FAILED);
                    }
                    
                }
                else if(value == -1) CoeusOptionPane.showInfoDialog(DELEGATED_TO_SOMEONE);
                else if(value == -2) CoeusOptionPane.showInfoDialog(ACCEPTED_CANNOT_DELEGATE);
                else if(value == -3) CoeusOptionPane.showInfoDialog(PREVIOUSLY_REJECTED);
                
                if(value == -1 || value == -2 || value == -3) {
                    txtUserId.setText(EMPTY);
                    txtUserName.setText(EMPTY);
                    txtEffectiveDt.setText(EMPTY);
                }
            }catch (CoeusClientException coeusClientException) {
                //Show Dialog
            }
        }
        
        /** Invoked when Deny Button is clicked.
         *
         */
        private void deny() {
            //System.out.println("Deny");
            changeStatus(REJECTED);
            //Added for Case#3682 - Enhancements related to Delegations - Start
            setDelegations();
            responsibilitiesTableModel.fireTableDataChanged();
            //Added for Case#3682 - Enhancements related to Delegations - End
        }
        
        /** Invoked when Remove button is clicked.
         */
        private void remove() {
            //System.out.println("Remove");
            int selection;
            UserDelegationsBean userDelegationsBean = (UserDelegationsBean)delegatedTo.get(0);
            if(userDelegationsBean.getDelegatedTo().equals(txtUserId.getText().trim())) {
                selection = CoeusOptionPane.showQuestionDialog(END_DELEGATION, CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
            }
            else {
                Date effectDate = userDelegationsBean.getEffectiveDate();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(effectDate);
                lastDate = (calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.DATE)+"/"+calendar.get(Calendar.YEAR);
                String strEffectDate = dateUtils.formatDate(lastDate, "/-:", "dd-MMM-yyyy");
                selection = CoeusOptionPane.showQuestionDialog("Do you want to remove the original delegation to "+userDelegationsBean.getDelegatedTo()+" effective on "+strEffectDate+" ?",CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
            }
            
            if(selection == CoeusOptionPane.SELECTION_NO) return ;
            //Clicked YES --> Remove
            //set AW properties
            userDelegationsBean.setAw_DelegatedBy(userDelegationsBean.getDelegatedBy());
            userDelegationsBean.setAw_DelegatedTo(userDelegationsBean.getDelegatedTo());
            userDelegationsBean.setAw_EffectiveDate(userDelegationsBean.getEffectiveDate());
            userDelegationsBean.setAw_Status(userDelegationsBean.getStatus());
            
            userDelegationsBean.setStatus(CLOSED);
            userDelegationsBean.setEndDate(new java.sql.Date(new Date().getTime()));
            try{
                userDelegationController.update(userDelegationsBean);
                
                //Removal Successful
                txtUserId.setText(EMPTY);
                txtUserName.setText(EMPTY);
                //Bug Fix : After Removal set date as empty - START
                lastDate = EMPTY;
                //Bug Fix : After Removal set date as empty - END
                txtEffectiveDt.setText(EMPTY);
                txtStatus.setText(EMPTY);
                
                lblStatus.setVisible(false);
                txtStatus.setVisible(false);
                
                btnRemove.setEnabled(false);
                setEnabledDelegation(true);
                
                lblDelegation1.setText(NOT_DELEGATED);
                lblDelegation2.setText(CLICK_DELEGATE);
                
                dirty = false;
                
            }catch (CoeusClientException coeusClientException) {
                CoeusOptionPane.showErrorDialog(DELEGATION_FAILED);
            }
        }
        
        /** Invoked when User Search button is clicked.
         */
        private void userSearch() {
            //System.out.println("User Search");
            try{
                coeusSearch = new CoeusSearch(userDelegationDlgWindow, USER_SEARCH, CoeusSearch.TWO_TABS);
                coeusSearch.showSearchWindow();
                HashMap selectedRow= coeusSearch.getSelectedRow();
                if(selectedRow != null) {
                    txtUserId.setText(selectedRow.get(USER_ID_KEY).toString().trim());
                    txtUserName.setText(selectedRow.get(USER_NAME_KEY).toString().trim());
                    btnDelegate.setEnabled(true);
                    dirty = true;
                }
            }catch (Exception exception) {
                exception.printStackTrace();
            }
            
        }
        
        /** Invoked when window is closed.
         * @param windowEvent WindowEvent
         */
        public void windowClosing(WindowEvent windowEvent) {
            //System.out.println("Window Closing");
            close();
        }
        //Setting of initial focus...
        public void windowActivated(WindowEvent evnt)
        {
            if(txtUserId.isEnabled()) {
                txtUserId.setRequestFocusEnabled(true);
                txtUserId.requestFocusInWindow();
            }else {
                btnClose.setRequestFocusEnabled(true);
                btnClose.requestFocusInWindow();
            }
        }
        /**
         */
        public void mouseClicked(MouseEvent mouseEvent) {
            //Delegated To (UserId) Double Click
            int clickCount = mouseEvent.getClickCount();
            if(clickCount == 2 && txtUserId.isEnabled()) {
                userSearch();
            }//end if
        }
        
        public void mouseEntered(MouseEvent mouseEvent) {
        }
        
        public void mouseExited(MouseEvent mouseEvent) {
        }
        
        public void mousePressed(MouseEvent mouseEvent) {
        }
        
        public void mouseReleased(MouseEvent mouseEvent) {
        }
        
        public void focusGained(FocusEvent focusEvent) {
            Object source = focusEvent.getSource();
            if(source.equals(txtEffectiveDt)) {
                txtEffectiveDt.setText(lastDate);
            }
            //System.out.println("Focus Gained"+lastDate);
        }
        
        public void focusLost(FocusEvent focusEvent) {
            Object source = focusEvent.getSource();
            if(source.equals(txtUserId)) {
                try{
                    if(txtUserId.getText().trim().equals(EMPTY)) {
                        txtUserName.setText(EMPTY);
                        return ;
                    }
                    String userName = userDelegationController.getUserName(txtUserId.getText().trim());
                    txtUserName.setText(userName);
                    dirty = true;
                }catch (Exception exception) {
                    //exception.printStackTrace();
                    CoeusOptionPane.showErrorDialog(userDelegationDlgWindow, txtUserId.getText()+" is not a valid user id");
                    txtUserId.setText(EMPTY);
                }
            }//end tab out event for userId(Delegated To)
            else if(source.equals(txtEffectiveDt)) {
                try{
                    if(txtEffectiveDt.getText().trim().equals(EMPTY)) return ;
                    String formattedDate = dateUtils.formatDate(txtEffectiveDt.getText().trim(),"/-:", "dd-MMM-yyyy");
                    if(formattedDate == null) {
                        CoeusOptionPane.showErrorDialog(txtEffectiveDt.getText()+INVALID_DATE);
                        lastDate = EMPTY;
                        txtEffectiveDt.setText(EMPTY);
                    }
                    else {
                        lastDate = txtEffectiveDt.getText().trim();
                        txtEffectiveDt.setText(formattedDate);
                    }
                    //System.out.println("Focus Lost"+lastDate);
                }catch (Exception exception) {
                    CoeusOptionPane.showErrorDialog(userDelegationDlgWindow, txtEffectiveDt.getText()+INVALID_DATE);
                    lastDate = EMPTY;
                    txtEffectiveDt.setText(EMPTY);                    
                }
            }//end tab out event for effective date
        }
        
    }//End Class UserDelegationMediator
    
    //Class UserDelegationTableModel
    class ResponsibilitiesTableModel extends AbstractTableModel {
        //private Vector dataVector;
        private Class colTypes[];
        protected String colNames[];
        
        ResponsibilitiesTableModel(Class colTypes[], String colNames[]) {
            //super(0, colTypes.length);
            this.colTypes = colTypes;
            this.colNames = colNames;
            //setRowCount(delegations.size());
        }
        
        public int getRowCount(){
            if(delegations == null)
                return 0;
            else
                return delegations.size();
        }        
        
        public boolean isCellEditable(int row, int column) {
            return false;
        }
        
        public Object getValueAt(int row, int column) {
            userDelegationsBean = (UserDelegationsBean)delegations.get(row);
            switch (column) {
                case 0:
                    return userDelegationsBean.getDelegatedBy();
                case 1:
                    return userDelegationsBean.getEffectiveDate();
                case 2:
                    return userDelegationsBean.getEndDate();
                case 3:
                    return EMPTY+userDelegationsBean.getStatus();
                default:
                    return EMPTY;
            }//end switch
        }
        
        public String getColumnName(int column) {
            return colNames[column];
        }

        public int getColumnCount() {
            return colNames.length;
        }
               
    }
    //End Class UserDelegationTableModel
    
    //Renderer
    class ResponsibilitiesRenderer extends DefaultTableCellRenderer {
        
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, MSG_REQUESTED, isSelected, hasFocus, row, column).setBackground(COLOR_BACKGROUND);
            switch(value.toString().trim().charAt(0)) {
                case REQUESTED:
                    value = MSG_REQUESTED;
                    super.getTableCellRendererComponent(table, MSG_REQUESTED, isSelected, hasFocus, row, column).setBackground(Color.yellow);
                    break;
                case ACCEPTED:
                    value = MSG_ACCEPTED;
                    break;
                case CLOSED:
                    value = MSG_CLOSED;
                    break;
                case REJECTED:
                    value = MSG_REJECTED;
                    break;
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
        
    }
    //End Renderer
    
}//End Class UserDelegationForm



