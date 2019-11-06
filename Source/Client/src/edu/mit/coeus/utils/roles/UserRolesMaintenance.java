/*
 * @(#)UserRolesMaintenance.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on April 8, 2003, 3:09 PM
 */

package edu.mit.coeus.utils.roles;

import java.util.Vector;
import java.awt.*;
import java.awt.dnd.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.datatransfer.*;

import javax.swing.*;
import javax.swing.tree.TreePath;

import java.util.HashMap;
import java.util.Observer;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.brokers.*;

import edu.mit.coeus.bean.*;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.tree.TransferableUserRoleData;
import edu.mit.coeus.utils.tree.UserRoleNode;
import edu.mit.coeus.search.gui.CoeusSearch;

/**
 * UserRolesMaintenance is used assing protocol/ proposal roles to the users of this
 * application. By default all the users whose unit number matches with the 
 * protocol's/ Proposal's lead unit will be shown when this screen is opened in ADD mode.
 * @author  Ravikanth
 */
public class UserRolesMaintenance extends JComponent 
    implements TypeConstants,ActionListener{
    
    private String moduleNumber;
    private char functionType;
    
    private Vector users = new Vector();
    private Vector userRoleDetails = new Vector();
//    private Vector existingUsers = new Vector();
    private final char GET_USER_DETAILS = 'Y';
    //private ImageIcon trashIcon,animatedTrashIcon;
    protected ImageIcon trashIcon,animatedTrashIcon;
    //private DropTarget dropTarget;
    protected DropTarget dropTarget;
    //private CoeusDlgWindow parent;
    protected  CoeusDlgWindow parent;
    private boolean saveRequired;
    private Vector userRolesData;
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    private LockObservable observable = new LockObservable();
    
    private int adminRoleId = -1;
    private int mandatoryRoleId = -1;
    private boolean showSponsor;
    //private String moduleLabelName;
    protected String moduleLabelName;
    private String sponsorName;
    /**
     * Creates new UserRolesMaintenance with the given protocol/ proposal number,
     * function type and the collection of data to be displayed in UserRolesTable
     * and UserRolesTree.
     *
     * @param moduleNumber String representing the Protocol/ Proposal Number.
     * @param userAndRoleDetails Collection of data for UserRolesTable and UserRolesTree
     * @param funcType char representing the dialog opened mode.
     */
    public UserRolesMaintenance(String moduleNumber, 
        Vector userAndRoleDetails, char funcType ) {
        this.moduleNumber = moduleNumber;
        this.functionType = funcType;
        if( userAndRoleDetails != null && userAndRoleDetails.size() == 2 ) {
            this.users = ( Vector ) userAndRoleDetails.elementAt( 0 );
            this.userRoleDetails = ( Vector ) userAndRoleDetails.elementAt( 1 );
        }
    }

    /**
     * Method which initializes all the form components and sets the default data
     * to the components.
     * @param parentComponent reference to the calling dialog window.
     * @return JComponent reference to this component.
     */
    public JComponent showUserRolesMaintenance(CoeusDlgWindow parentComponent ) throws Exception{
        initComponents();
        java.awt.Component[] components = {tblUsers,rolesTree,btnOk,btnCancel,btnUsers,btnRights};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        pnlMain.setFocusTraversalPolicy(traversePolicy);
        pnlMain.setFocusCycleRoot(true);

        this.parent = parentComponent; 
        lblModuleLabel.setText( moduleLabelName );
        trashIcon = new ImageIcon(getClass().getClassLoader().getResource( 
                    CoeusGuiConstants.TRASH_ICON ));
        
        animatedTrashIcon = new ImageIcon(getClass().getClassLoader().getResource( 
                    CoeusGuiConstants.ANIMATED_TRASH_ICON ));
        
        lblTrashImage.setIcon(trashIcon);
        dropTarget = new DropTarget(lblTrashImage,new LabelDropTargetListener());
        tblUsers.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
        setFormData();
        formatFields();
        btnOk.addActionListener(this);
        btnCancel.addActionListener(this);
        btnUsers.addActionListener(this);
        btnRights.addActionListener(this);
        coeusMessageResources = CoeusMessageResources.getInstance();
        rolesTree.setAdminRoleId( adminRoleId );
        rolesTree.setMandatoryRoleId( mandatoryRoleId );
        
        return pnlMain;
    }
    
    
    public JComponent showComponents(CoeusDlgWindow parentComponent){
        
        java.awt.Component[] components = {tblUsers,rolesTree,btnOk,btnCancel,btnUsers,btnRights};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        pnlMain.setFocusTraversalPolicy(traversePolicy);
        pnlMain.setFocusCycleRoot(true);

        this.parent = parentComponent; 
        lblModuleLabel.setText( moduleLabelName );
        trashIcon = new ImageIcon(getClass().getClassLoader().getResource( 
                    CoeusGuiConstants.TRASH_ICON ));
        
        animatedTrashIcon = new ImageIcon(getClass().getClassLoader().getResource( 
                    CoeusGuiConstants.ANIMATED_TRASH_ICON ));
        
        lblTrashImage.setIcon(trashIcon);
        dropTarget = new DropTarget(lblTrashImage,new LabelDropTargetListener());
        tblUsers.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
        setFormData();
        formatFields();
        btnOk.addActionListener(this);
        btnCancel.addActionListener(this);
        btnUsers.addActionListener(this);
        btnRights.addActionListener(this);
        coeusMessageResources = CoeusMessageResources.getInstance();
        rolesTree.setAdminRoleId( adminRoleId );
        rolesTree.setMandatoryRoleId( mandatoryRoleId );
        
        return pnlMain;
    }
    

    /* supporting method which  sets the data to all of the form components */
    private void setFormData(){
        if ( moduleNumber != null ) {
            lblModule.setText( moduleNumber );
        }
        if( sponsorName != null ) {
            lblSponsorName.setText( sponsorName );
        }
        if( users != null && users.size() > 0 ) {
            tblUsers.setUserTableData( users, getTableColumnNames());
            if( tblUsers.getModel() instanceof TableSorter ) {
                ((TableSorter)tblUsers.getModel()).sortByColumn(1);
            }
            
            if( functionType == DISPLAY_MODE ) {
                tblUsers.clearSelection();
            }
        }
        
        if( userRoleDetails != null && userRoleDetails.size() > 0 ) {
            //existingUsers = getUserRoleInfo ( userRoleDetails );
            rolesTree.createTreeData( userRoleDetails );
            addUserInfo ( userRoleDetails );
        }
    }
    /* supporting method which returns the column names to be used for UserRolesTable
     */
    private Vector getTableColumnNames(){
        Vector colNames = new Vector();
        colNames.addElement("Role");
        colNames.addElement("User ID");
        colNames.addElement("User Name");
        colNames.addElement("Unit Number");
        colNames.addElement("Unit Name");
        colNames.addElement("Index");
        return colNames;
    }
    /* supporting method which set enable status for all form components */
    private void formatFields(){
        boolean enabled = functionType == DISPLAY_MODE ? false : true;
        tblUsers.setEnabled(enabled);
        rolesTree.setEnableDragDrop( enabled );
        btnOk.setEnabled(enabled);
        btnUsers.setEnabled(enabled);
        lblSponsor.setVisible( showSponsor );
        lblSponsorName.setVisible( showSponsor );
    }
    
    /* supporting method which sets all the user details to the user table */
    private void addUserInfo ( Vector userRoleDetails ) {
        int rolesSize = userRoleDetails.size();
        Vector extractedUsers = new Vector();
        UserRolesInfoBean bean,childBean;
        RoleInfoBean roleBean;
        UserInfoBean userBean;
        Vector children;
        int childCount;
        for(int index = 0 ; index < rolesSize ; index++ ) {
            bean = (UserRolesInfoBean) userRoleDetails.elementAt( index );
            if( bean.isRole() ) {
                roleBean = bean.getRoleBean();
                if( bean.hasChildren() ){
                    children = bean.getUsers();
                    childCount = children.size();
                    for(int childIndex = 0; childIndex < childCount ; childIndex++ ) {
                        childBean = (UserRolesInfoBean) children.elementAt(childIndex);
                        userBean = childBean.getUserBean();
                        tblUsers.addRow( userBean ) ;
                    }
                }
            }
        }
    }
    public class LabelDropTargetListener implements DropTargetListener {
     
        public void dragEnter(DropTargetDragEvent dtde){
        }

        public void dragOver(DropTargetDragEvent dtde){
            
        }
        public void dropActionChanged(DropTargetDragEvent dtde){
        }
        public void dragExit(DropTargetEvent dte){
        }
        public void drop(DropTargetDropEvent dtde){
            Transferable tr = dtde.getTransferable();
            
            UserRolesInfoBean userRolesBean = new UserRolesInfoBean(); 
            Object data = new Object();
            try {
                 data = tr.getTransferData(
                        TransferableUserRoleData.ROLE_FLAVOR );
            } catch ( Exception e){
                dtde.getDropTargetContext().dropComplete(false);
            }
            if( data instanceof UserRolesInfoBean ) {
                //dtde.acceptDrop(DnDConstants.ACTION_MOVE);
                lblTrashImage.setIcon( animatedTrashIcon );
                AnimateIcon animate = new AnimateIcon();
               
                lblTrashImage.setIcon( animatedTrashIcon );
                animate.start();
                dtde.getDropTargetContext().dropComplete(true);
            }
        }
    }
    
    /* Class used to animate the trash icom by starting it in a new thread */
    class AnimateIcon extends Thread {
        Runnable runThread;
        AnimateIcon( ) {
            runThread = new Runnable () {
                            public void run () {
                                lblTrashImage.setIcon( trashIcon );
                            }
                        };
        }
        
        public void run() {
            try{
                Thread.sleep(500);
                SwingUtilities.invokeLater( runThread );
            } catch ( Exception e) {
            }
        }
    }
    public void actionPerformed( ActionEvent ae ) {
        Object source = ae.getSource();
        saveRequired = rolesTree.isModified();
        if( source.equals( btnOk ) ){
            closeUserRoles();
        }else if( source.equals( btnCancel ) ){
            if( isSaveRequired() ){
                String msg = coeusMessageResources.parseMessageKey(
                "saveConfirmCode.1002");

                int confirm = CoeusOptionPane.showQuestionDialog(msg,
                CoeusOptionPane.OPTION_YES_NO_CANCEL,
                CoeusOptionPane.DEFAULT_YES);
                switch(confirm){
                    case ( JOptionPane.NO_OPTION ) :
                        saveRequired = false;
                        closeUserRoles();
                        break;
                    case ( JOptionPane.YES_OPTION ) :
                        closeUserRoles();
                        break;
                }
            }else{
                closeUserRoles();
            }
                
        }else if ( source.equals ( btnRights ) ) {
            
            TreePath selectedPath = rolesTree.getSelectionPath();
            if( selectedPath != null ) {
                UserRoleNode selectedNode = ( UserRoleNode )selectedPath.getLastPathComponent();
                UserRolesInfoBean userRoleBean = selectedNode.getDataObject();
                
                if( userRoleBean.isRole() ) {
                    RoleInfoBean roleBean = userRoleBean.getRoleBean();
                    RightsForRoleForm rightsForm 
                        = new RightsForRoleForm(""+roleBean.getRoleId(),
                                roleBean.getRoleName() );
                }else{
                    CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(
                            "protoRoles_exceptionCode.2600"));
                }
            }else {
                CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(
                        "protoRoles_exceptionCode.2600"));
            }
            
        }else if ( source.equals( btnUsers ) ) {
            try {
                CoeusSearch userSearch 
                    = new CoeusSearch( CoeusGuiConstants.getMDIForm(), "USERSEARCH",
                        CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION ); 
                userSearch.showSearchWindow();
                Vector selectedUsers = userSearch.getMultipleSelectedRows(); 
                if ( selectedUsers != null ){
                    HashMap user = null;
                    for(int indx = 0; indx < selectedUsers.size(); indx++ ){

                        user = (HashMap)selectedUsers.get( indx ) ;                    
                        if( user == null || user.isEmpty() ){
                            continue;
                        }
                        String userId = checkForNull(user.get( "USER_ID" ));
                        String userName = checkForNull(user.get( "USER_NAME" ));
                        String homeUnit = checkForNull( user.get( "UNIT_NUMBER" ));
                        String unitName = checkForNull(user.get( "UNIT_NAME" ));
                        char status =  user.get("STATUS") == null ? ' ' 
                                    : ((String)user.get("STATUS")).charAt(0);
                        Vector newRow = new Vector();
                        newRow.addElement( ""+status );
                        newRow.addElement( userId );
                        newRow.addElement( userName );
                        newRow.addElement( homeUnit );
                        newRow.addElement( unitName );
                        int rowCount = tblUsers.getRowCount();
                        newRow.addElement(""+rowCount);
                        tblUsers.addRow( newRow );
                    }
                }
            }catch ( Exception ex ) {
                CoeusOptionPane.showInfoDialog( ex.getMessage() );
            }
        }
    }
    private String checkForNull( Object value ){
        return (value==null)? "":value.toString();
    }

    /** Returns whether data is to be saved or not.
     * @return true if data is to be saved else false.
     */
    public boolean isSaveRequired() {
        return saveRequired;
    }    
    /**
     * Sets the saveRequired flag with the given parameter value.
     * @param save boolean value to be set to the saveRequired flag.
     */
    public void setSaveRequired( boolean save ) {
        saveRequired = save;
    }

    /**
     * Returns whether any changes have been made to the roles tree.
     * @return boolean true if changes have been made to tree else false.
     */
    public boolean isDataChanged() {
        return rolesTree.isModified();
    }
    
    /** Method which returns collection of UserRolesInfoBean 
     * @return Collection of UserRolesInfoBean from roles tree.
     */
    public java.util.Vector getUserRolesData() {
        userRolesData = rolesTree.getUserRoles();
        return userRolesData;
    }
    
    /** Returns Admin Role Id.
     * @return int represents the admin role id.
     */
    public int getAdminRoleId() {
        return adminRoleId;
    }    

    /** Sets admin role id
     * @param adminRoleId id to be used in validations during DnD.
     */
    public void setAdminRoleId(int adminRoleId) {
        this.adminRoleId = adminRoleId;
    }    

    /** Method used to set the role id to which atleast one user should be present.
     * @param mandatoryRoleId String representing the role id.
     */
    public void setMandatoryRoleId(int mandatoryRoleId) {
        this.mandatoryRoleId = mandatoryRoleId;
    }

    /** Method which specifies whether to show Sponsor code and name or not.
     * @param showSponsor boolean value used to show / hide sponsor details.
     */
    public void setShowSponsor(boolean showSponsor) {
        this.showSponsor = showSponsor;
    }    
   
    /**
     * Method used to set the label name to be displayed for showing module number
     * (Protocol/ Proposal Number etc.) 
     * @param moduleLabelName String representing the name to be displayed as
     * module label.
     */
     public void setModuleLabelName( String moduleLabelName ) {
        this.moduleLabelName = moduleLabelName;
     }
     
     /**
      * Method used to set the selected sponsor name.
      * @param sponsorName String representing the name of the Sponsor.
      */
     public void setSponsorName( String sponsorName ) {
        this.sponsorName = sponsorName;
     }
     
     protected void callInitComponents(){
         initComponents();
     }
     
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        pnlModuleDescription = new javax.swing.JPanel();
        pnlDesc = new javax.swing.JPanel();
        lblModuleLabel = new javax.swing.JLabel();
        lblModule = new javax.swing.JLabel();
        lblSponsor = new javax.swing.JLabel();
        lblSponsorName = new javax.swing.JLabel();
        pnlButtons = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnUsers = new javax.swing.JButton();
        btnRights = new javax.swing.JButton();
        scrPnUsers = new javax.swing.JScrollPane();
        tblUsers = new edu.mit.coeus.utils.UserRolesTable();
        scrPnRoles = new javax.swing.JScrollPane();
        rolesTree = new edu.mit.coeus.utils.tree.UserRoleTree();
        pnlTrashLabel = new javax.swing.JPanel();
        lblTrashImage = new javax.swing.JLabel();
        seperator = new javax.swing.JSeparator();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(703, 446));
        setPreferredSize(new java.awt.Dimension(918, 556));
        pnlMain.setLayout(new java.awt.GridBagLayout());

        pnlModuleDescription.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        pnlDesc.setLayout(new java.awt.GridBagLayout());

        pnlDesc.setMinimumSize(new java.awt.Dimension(163, 27));
        lblModuleLabel.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlDesc.add(lblModuleLabel, gridBagConstraints);

        lblModule.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlDesc.add(lblModule, gridBagConstraints);

        lblSponsor.setText("Sponsor:");
        lblSponsor.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlDesc.add(lblSponsor, gridBagConstraints);

        lblSponsorName.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlDesc.add(lblSponsorName, gridBagConstraints);

        pnlModuleDescription.add(pnlDesc);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        pnlMain.add(pnlModuleDescription, gridBagConstraints);

        pnlButtons.setLayout(new java.awt.GridBagLayout());

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('o');
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(81, 27));
        btnOk.setMinimumSize(new java.awt.Dimension(81, 27));
        btnOk.setPreferredSize(new java.awt.Dimension(81, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 2);
        pnlButtons.add(btnOk, gridBagConstraints);

        btnCancel.setMnemonic('C');
        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setText("Cancel");
        btnCancel.setPreferredSize(new java.awt.Dimension(81, 27));
        btnCancel.setMaximumSize(new java.awt.Dimension(81, 27));
        btnCancel.setMinimumSize(new java.awt.Dimension(81, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 2);
        pnlButtons.add(btnCancel, gridBagConstraints);

        btnUsers.setMnemonic('U');
        btnUsers.setFont(CoeusFontFactory.getLabelFont());
        btnUsers.setText("Users");
        btnUsers.setPreferredSize(new java.awt.Dimension(81, 27));
        btnUsers.setMaximumSize(new java.awt.Dimension(81, 27));
        btnUsers.setMinimumSize(new java.awt.Dimension(81, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 2);
        pnlButtons.add(btnUsers, gridBagConstraints);

        btnRights.setMnemonic('R');
        btnRights.setFont(CoeusFontFactory.getLabelFont());
        btnRights.setText("Rights");
        btnRights.setPreferredSize(new java.awt.Dimension(81, 27));
        btnRights.setMaximumSize(new java.awt.Dimension(81, 27));
        btnRights.setMinimumSize(new java.awt.Dimension(81, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 2);
        pnlButtons.add(btnRights, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(30, 5, 0, 5);
        pnlMain.add(pnlButtons, gridBagConstraints);

        scrPnUsers.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Users", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP, CoeusFontFactory.getLabelFont()));
        scrPnUsers.setMinimumSize(new java.awt.Dimension(500, 360));
        scrPnUsers.setPreferredSize(new java.awt.Dimension(535, 400));
        tblUsers.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        scrPnUsers.setViewportView(tblUsers);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        pnlMain.add(scrPnUsers, gridBagConstraints);

        scrPnRoles.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Roles", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP, CoeusFontFactory.getLabelFont()));
        scrPnRoles.setMaximumSize(new java.awt.Dimension(400, 400));
        scrPnRoles.setMinimumSize(new java.awt.Dimension(100, 100));
        scrPnRoles.setPreferredSize(new java.awt.Dimension(280, 300));
        rolesTree.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        rolesTree.setDropable(true);
        rolesTree.setFont(CoeusFontFactory.getNormalFont());
        rolesTree.setMoveNode(true);
        scrPnRoles.setViewportView(rolesTree);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        pnlMain.add(scrPnRoles, gridBagConstraints);

        pnlTrashLabel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        pnlTrashLabel.setPreferredSize(new java.awt.Dimension(80, 80));
        lblTrashImage.setFont(CoeusFontFactory.getLabelFont());
        lblTrashImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTrashImage.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblTrashImage.setPreferredSize(new java.awt.Dimension(80, 80));
        pnlTrashLabel.add(lblTrashImage);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(30, 5, 5, 5);
        pnlMain.add(pnlTrashLabel, gridBagConstraints);

        seperator.setForeground(java.awt.Color.black);
        seperator.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        pnlMain.add(seperator, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(pnlMain, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

     /** Getter for property rolesTree.
      * @return Value of property rolesTree.
      */
     public edu.mit.coeus.utils.tree.UserRoleTree getRolesTree() {
         return rolesTree;
     }     

     /** Getter for property userRoleDetails.
      * @return Value of property userRoleDetails.
      *
      */
     public java.util.Vector getUserRoleDetails() {
         return userRoleDetails;
     }     
    
     /** Setter for property userRoleDetails.
      * @param userRoleDetails New value of property userRoleDetails.
      *
      */
     public void setUserRoleDetails(java.util.Vector userRoleDetails) {
         this.userRoleDetails = userRoleDetails;
     }
     
     public void registerLockObservable(Observer observer) {
         observable.addObserver(observer); 
     }
     
     private void closeUserRoles() {
         parent.dispose();
         observable.setLockStatus(-1);
         observable.notifyObservers();
     }
     // This method will be called from parent window while window close(X).
     public void closeAndFire(){
         observable.setLockStatus(-1);
         observable.notifyObservers();
     }
     
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOk;
    public javax.swing.JButton btnRights;
    public javax.swing.JButton btnUsers;
    public javax.swing.JLabel lblModule;
    public javax.swing.JLabel lblModuleLabel;
    public javax.swing.JLabel lblSponsor;
    public javax.swing.JLabel lblSponsorName;
    public javax.swing.JLabel lblTrashImage;
    public javax.swing.JPanel pnlButtons;
    public javax.swing.JPanel pnlDesc;
    public javax.swing.JPanel pnlMain;
    public javax.swing.JPanel pnlModuleDescription;
    public javax.swing.JPanel pnlTrashLabel;
    public edu.mit.coeus.utils.tree.UserRoleTree rolesTree;
    public javax.swing.JScrollPane scrPnRoles;
    public javax.swing.JScrollPane scrPnUsers;
    public javax.swing.JSeparator seperator;
    public edu.mit.coeus.utils.UserRolesTable tblUsers;
    // End of variables declaration//GEN-END:variables
    
}
