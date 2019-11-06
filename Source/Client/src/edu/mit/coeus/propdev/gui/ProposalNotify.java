/**
 * @(#)ProposalNotify.java  1.0  December 23, 2003, 10:54 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.CoeusFontFactory;

import edu.mit.coeus.propdev.gui.*;
import edu.mit.coeus.propdev.bean.*;

import edu.mit.coeus.utils.tree.*;

import edu.mit.coeus.irb.bean.*;

import edu.mit.coeus.bean.*;
import edu.mit.coeus.utils.*;
//import edu.mit.coeus.utils.CoeusGuiConstants;
//import edu.mit.coeus.utils.CoeusOptionPane;
//import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.tree.UserRoleTree;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;

import javax.swing.*;
import javax.swing.JTree.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.DropTarget;
import java.util.Vector;
import java.util.Enumeration;
import java.io.IOException;
import java.beans.*;

/**
 *
 * @author  Amit Jadhav. 12/23/03
 */
public class ProposalNotify extends javax.swing.JComponent implements DropTargetListener,ActionListener, ListSelectionListener{
    
    private boolean dropable=true;
    private boolean enableDragDrop = true;
    private boolean dragingInside = false;
    private boolean expandRole;
    
    private Vector vecAddedUsers = new Vector();
    private Vector vecDelUsers = new Vector();
    
    private UserRoleTree selfRef;
    
    //holds the proposal ID.
    private String proposalID=null;
    private String sponsorDetails="";
    
    //holds whether save is required
    private boolean saveRequired;
    private CoeusMessageResources coeusMessageResources;
    private CoeusDlgWindow dlgWindow;
    private ProposalDevelopmentFormBean proposalDevelopmentFormBean;
    
    private String msg;
    
    //Code added by Vyjayanthi
    /** Holds the connection string */
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
                                     CoeusGuiConstants.PROPOSAL_ACTION_SERVLET;
    
    //To get the sponsor code and description for the given proposal
    private static final char GET_SPONSOR_FOR_PROPOSAL = 'a';
    
    public ProposalNotify(){
    }
    
    /** Creates new form ProposalNotify */
    public ProposalNotify(ProposalDevelopmentFormBean proposalDevelopmentFormBean) {
        coeusMessageResources = CoeusMessageResources.getInstance();
        this.proposalDevelopmentFormBean=proposalDevelopmentFormBean;
        
        proposalID = proposalDevelopmentFormBean.getProposalNumber();
        if(proposalDevelopmentFormBean.getSponsorCode() != null)
            sponsorDetails = proposalDevelopmentFormBean.getSponsorCode()+" : ";
        if(proposalDevelopmentFormBean.getSponsorName() != null)
            sponsorDetails += proposalDevelopmentFormBean.getSponsorName();
        initComponents();
        btnRemove.setEnabled(false);
        treeUserRole.createTreeData(getTreeData());
        new DropTarget(lstSendTo,this);
        lstSendTo.setModel(new DefaultListModel());
        registerComponents();
        dlgWindow = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(),"Coeus",true);
        setCloseEvents();
        setTreeNodes();
        txtAreaMessage.setWrapStyleWord(true);
        txtAreaMessage.setLineWrap(true);
    }
    
    //Code Added by Vyjayanthi on 02-Feb-2004 - Start
    /** Creates new form ProposalNotify, used to display this form
     * when Notepad is opened from the Base window
     * @param proposalNumber accepts the proposal number
     */
    public ProposalNotify(String proposalNumber, String message){
        coeusMessageResources = CoeusMessageResources.getInstance();
        proposalID = proposalNumber;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(GET_SPONSOR_FOR_PROPOSAL);
        requester.setDataObject(proposalNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(
            CONNECTION_STRING, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        ComboBoxBean comboBoxBean = new ComboBoxBean();
        if ( response != null ){
            if( response.isSuccessfulResponse() ){
                comboBoxBean = (ComboBoxBean)response.getDataObject();
            }
        }
        //modified for the bug fix:1647 start step:1
        if(comboBoxBean != null){
            if( comboBoxBean.getCode() != null ){
                sponsorDetails = (String)comboBoxBean.getCode() + " : ";
            }
        }//end step:1
        
        //modified for the bug fix:1647 start step:2
        if(comboBoxBean != null){
            if( comboBoxBean.getDescription() != null){
                sponsorDetails += (String)comboBoxBean.getDescription();
            }
        }//end step:2
        
        initComponents();
        btnRemove.setEnabled(false);
        treeUserRole.createTreeData(getTreeData());
        new DropTarget(lstSendTo,this);
        lstSendTo.setModel(new DefaultListModel());
        registerComponents();
        dlgWindow = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(),"Coeus",true);
        setCloseEvents();
        setTreeNodes();
        txtAreaMessage.setWrapStyleWord(true);
        txtAreaMessage.setLineWrap(true);
        txtAreaMessage.setText(message);
    }
    //Code Added by Vyjayanthi on 02-Feb-2004 - End
    
    private void setTreeNodes(){
        UserRoleNodeRenderer nodeRenderer = new UserRoleNodeRenderer();
        nodeRenderer.setActiveRoleIcon( new ImageIcon( getClass().getClassLoader().getResource(
        CoeusGuiConstants.PROP_ACTIVE_ROLE_ICON ) ) );
        nodeRenderer.setInactiveRoleIcon( new ImageIcon( getClass().getClassLoader().getResource(
        CoeusGuiConstants.PROP_INACTIVE_ROLE_ICON ) ) );
        treeUserRole.setCellRenderer( nodeRenderer );
    }
    
    private void setCloseEvents(){
        dlgWindow.addEscapeKeyListener( new AbstractAction("escPressed") {
            public void actionPerformed(ActionEvent ar) {
                try{
                    performWindowClosing();
                }catch(Exception exc){
                }
            }
        });
        
        dlgWindow.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                try{
                    performWindowClosing();
                }catch(Exception exc){
                }
            }
            public void windowOpened(WindowEvent we){
                 btnOk.requestFocusInWindow();
                 btnOk.setFocusable(true);
                 btnOk.requestFocus();
            }
        });
    }
    private void registerComponents(){
        lstSendTo.addListSelectionListener(this);
        Component[] comp = {treeUserRole, txtAreaMessage, btnOk, btnCancel, btnRemove };
        ScreenFocusTraversalPolicy traversal = new ScreenFocusTraversalPolicy(comp);
        setFocusTraversalPolicy(traversal);
        setFocusCycleRoot(true);
    }
    
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        int userList = lstSendTo.getModel().getSize();
        if(userList <= 0){
            btnRemove.setEnabled(false);
        }
        else{
            btnRemove.setEnabled(true);
        }
    }
    
    public void showNotify() {
        dlgWindow.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dlgWindow.getContentPane().add(this);
        dlgWindow.setResizable(false);
        dlgWindow.pack();
        dlgWindow.setLocation(CoeusDlgWindow.CENTER);
        dlgWindow.show();
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        coeusTextField1 = new edu.mit.coeus.utils.CoeusTextField();
        scrPaneSendTo = new javax.swing.JScrollPane();
        lstSendTo = new javax.swing.JList();
        scrPanePlayers = new javax.swing.JScrollPane();
        treeUserRole = new edu.mit.coeus.utils.tree.UserRoleTree();
        lblPropNumber = new javax.swing.JLabel();
        lblPropNumberDesc = new javax.swing.JLabel();
        lblSponsorNumber = new javax.swing.JLabel();
        lblSponNumberDesc = new javax.swing.JLabel();
        scrPaneMessage = new javax.swing.JScrollPane();
        txtAreaMessage = new javax.swing.JTextArea();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();

        coeusTextField1.setText("coeusTextField1");

        setLayout(new java.awt.GridBagLayout());

        scrPaneSendTo.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Send to", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, CoeusFontFactory.getLabelFont()));
        scrPaneSendTo.setMinimumSize(new java.awt.Dimension(250, 225));
        scrPaneSendTo.setPreferredSize(new java.awt.Dimension(250, 225));
        lstSendTo.setBackground((java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background"));
        lstSendTo.setFont(CoeusFontFactory.getNormalFont());
        scrPaneSendTo.setViewportView(lstSendTo);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(scrPaneSendTo, gridBagConstraints);

        scrPanePlayers.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Players", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, CoeusFontFactory.getLabelFont()));
        scrPanePlayers.setMinimumSize(new java.awt.Dimension(250, 225));
        scrPanePlayers.setPreferredSize(new java.awt.Dimension(250, 225));
        treeUserRole.setBackground((java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background"));
        treeUserRole.setCopyAllChildren(true);
        treeUserRole.setFont(CoeusFontFactory.getNormalFont());
        scrPanePlayers.setViewportView(treeUserRole);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(scrPanePlayers, gridBagConstraints);

        lblPropNumber.setFont(CoeusFontFactory.getLabelFont());
        lblPropNumber.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPropNumber.setText("Proposal Number: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblPropNumber, gridBagConstraints);

        lblPropNumberDesc.setFont(CoeusFontFactory.getNormalFont());
        lblPropNumberDesc.setText(proposalID);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblPropNumberDesc, gridBagConstraints);

        lblSponsorNumber.setFont(CoeusFontFactory.getLabelFont());
        lblSponsorNumber.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSponsorNumber.setText("Sponsor Number: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        add(lblSponsorNumber, gridBagConstraints);

        lblSponNumberDesc.setFont(CoeusFontFactory.getNormalFont());
        lblSponNumberDesc.setText(sponsorDetails);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        add(lblSponNumberDesc, gridBagConstraints);

        scrPaneMessage.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Message:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, CoeusFontFactory.getLabelFont()));
        scrPaneMessage.setMinimumSize(new java.awt.Dimension(200, 100));
        txtAreaMessage.setDocument(new LimitedPlainDocument(2000));
        txtAreaMessage.setFont(CoeusFontFactory.getNormalFont());
        txtAreaMessage.setRows(5);
        txtAreaMessage.setWrapStyleWord(true);
        scrPaneMessage.setViewportView(txtAreaMessage);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(scrPaneMessage, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setMinimumSize(new java.awt.Dimension(80, 26));
        btnOk.setPreferredSize(new java.awt.Dimension(80, 26));
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 5, 5);
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMinimumSize(new java.awt.Dimension(80, 26));
        btnCancel.setPreferredSize(new java.awt.Dimension(80, 26));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(btnCancel, gridBagConstraints);

        btnRemove.setFont(CoeusFontFactory.getLabelFont());
        btnRemove.setMnemonic('R');
        btnRemove.setText("Remove");
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(btnRemove, gridBagConstraints);

    }//GEN-END:initComponents
    
    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
         saveNotify();   
    }//GEN-LAST:event_btnOkActionPerformed
    
    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        int selected[] = lstSendTo.getSelectedIndices();
        for(int index = 0; index < selected.length; index++) {
            ((DefaultListModel)lstSendTo.getModel()).remove(selected[index]-index);
        }
        int size = ((DefaultListModel)lstSendTo.getModel()).getSize();
        System.out.println("THe list size is   "+size);
        if( size > 0 ){
            lstSendTo.setSelectedIndex(0);
        }
    }//GEN-LAST:event_btnRemoveActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        try{
            performWindowClosing();
        }catch(Exception e){
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void saveNotify(){
        Vector vecInboxBean = new Vector();
        String rowId = null;
        rowId = proposalID;
        int userList = lstSendTo.getModel().getSize();
        
        if(txtAreaMessage.getText().trim().length() != 0){
            
            for(int ind = 0; ind < userList; ind++) {
                String userId = ((UserRolesInfoBean)lstSendTo.getModel().getElementAt(ind)).getUserBean().getUserId();
                InboxBean inBean = new InboxBean();
                MessageBean messBean = new MessageBean();
                messBean.setMessage(txtAreaMessage.getText());
                inBean.setProposalNumber(proposalID);
                /* Case :1828 Start */
                inBean.setModuleCode(3);
                /* CASe :1828 End */
                inBean.setToUser(userId);
                inBean.setFromUser(CoeusGuiConstants.getMDIForm().getUserName());
                inBean.setAcType("I");
                messBean.setAcType("I");
                inBean.setMessageBean(messBean);
                inBean.setSubjectType('N');
                inBean.setOpenedFlag('N');
                vecInboxBean.add(inBean);
            }

            if(userList > 0){
                try{
                    RequesterBean reqBean = new RequesterBean();
                    reqBean.setFunctionType('J');
                    reqBean.setDataObjects(vecInboxBean);
                    
                    String connectTo = CoeusGuiConstants.CONNECTION_URL+CoeusGuiConstants.PROPOSAL_ACTION_SERVLET;
                    AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,reqBean);
                    comm.send();
                    ResponderBean response = comm.getResponse();
                    if(!response.isSuccessfulResponse()){
                        CoeusOptionPane.showInfoDialog( response.getMessage() );
                    }
                    saveRequired = true;
                    dlgWindow.dispose();
                }
                catch(Exception exc){
                    exc.printStackTrace();
                }
            }else{
                msg = coeusMessageResources.parseMessageKey("notify_exceptionCode.2804"); //"Designate at least one recipient for the Message.";
                CoeusOptionPane.showInfoDialog(msg);
            }
        }else{
            msg = coeusMessageResources.parseMessageKey("notify_exceptionCode.2802"); //"Enter the Message.";
            CoeusOptionPane.showInfoDialog(msg);
            txtAreaMessage.requestFocusInWindow();
        }
    }
    
    
    
    /**
     * This method is used to perform the Window closing operation
     */
    //This method totally modified 
    private void performWindowClosing() throws Exception{
        
        int option = JOptionPane.NO_OPTION;
        if( isSaveRequired() ){
            msg = coeusMessageResources.parseMessageKey("saveConfirmCode.1002");
            option = CoeusOptionPane.showQuestionDialog(msg,CoeusOptionPane.OPTION_YES_NO_CANCEL,
                                                                        CoeusOptionPane.DEFAULT_YES);
            switch(option){
                case JOptionPane.YES_OPTION :  saveNotify();
                                               break;
                
                case JOptionPane.NO_OPTION  :  dlgWindow.dispose();
                                               break;
                
                case JOptionPane.CANCEL_OPTION : System.out.println("Selected option is cancel ");
                                                 return ;   
            }
            
        }else{
            dlgWindow.dispose();
        }
    }
    
    /** This method is used to get the saveRequired Flag
     *
     * @return boolean true if changes are made in the form, else false
     */
    public boolean isSaveRequired(){
        
        int lstSize = lstSendTo.getModel().getSize();
        //System.out.println("The size of the list is "+lstSize);
        String msg=txtAreaMessage.getText();
        msg=(msg==null?"":msg);
        //System.out.println("The message is "+msg);
        if(lstSize > 0 || !msg.equalsIgnoreCase("") ){
            saveRequired = true;
        }else{
            saveRequired = false;
        }
        return saveRequired;
    }
    
    /**
     * This method is used to specify whether this tree component should be used
     * as drop target or not.
     *
     * @param drop  boolean value which specifies the tree accepts any data to be
     * dropped on it or not.
     *
     */
    public void setDropable(boolean drop){
        this.dropable = drop;
    }
    
    /** Getter for property enableDragDrop.
     * @return Value of property enableDragDrop.
     */
    public boolean isEnableDragDrop() {
        return enableDragDrop;
    }
    
    /** Setter for property enableDragDrop.
     * @param enableDragDrop New value of property enableDragDrop.
     */
    public void setEnableDragDrop(boolean enableDragDrop) {
        this.enableDragDrop = enableDragDrop;
    }
    
    /**
     * Method which specifies whether this component accepts any data to be dropped
     * on it or not.
     * @return  boolean true if this components accepts any data to be dropped on it
     * else false.
     */
    public boolean isDropable(){
        return dropable;
    }
    
    public void dragEnter(java.awt.dnd.DropTargetDragEvent dtde) {
    }
    
    public void dragExit(java.awt.dnd.DropTargetEvent dte) {
    }
    
    public void dragOver(java.awt.dnd.DropTargetDragEvent dtde) {
    }
    
    public void drop(java.awt.dnd.DropTargetDropEvent dtde) {
        if( dropable && enableDragDrop  && !dragingInside ){
            try{
                Point loc = dtde.getLocation();
                
                TreePath destinationPath = treeUserRole.getSelectionPath();
                Transferable tr = dtde.getTransferable();
                Object data = tr.getTransferData(TransferableUserRoleData.ROLE_FLAVOR );
                 /* check whether multiple items have been dragged or only
                       one item.*/
                if ( data instanceof Vector ){
                        /* mulitiple items dragged, so add all the items in the
                           vector */
                    Vector dataVector = ( Vector ) data ;
                    for ( int indx = 0; indx < dataVector.size(); indx++ ){
                        addUserList( dataVector.elementAt( indx ), destinationPath );
                        /** Added to have the added object inside the Vector **/
                        //vecAddedUsers.addElement(dataVector.elementAt( indx ));
                    }
                }else{
                    /* single item dragged, so add the dragged item */
                    addUserList( data , destinationPath);
                    /** Added to have the added object inside the Vector **/
                }
                //((DefaultTreeModel)getModel()).reload();
                dtde.getDropTargetContext().dropComplete(true);
                //                        setSelectionPath( destinationPath );
                
            } catch ( Exception ioe ) {
                ioe.printStackTrace();
                dtde.getDropTargetContext().dropComplete(false);
            }
        }
    }//end of drop
    
    
    /**
     * Method used to expand/ collapse all the nodes in the tree.
     * @param tree JTree whose nodes are to be expanded/ collapsed.
     * @param expand  boolean true to expand all nodes, false to collapse.
     */
    public void expandAll(JTree tree, boolean expand) {
        TreeNode root = (TreeNode)tree.getModel().getRoot();

        // Traverse tree from root
        expandAll(tree, new TreePath(root), expand);
    }
    
    private void expandAll(JTree tree, TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode)e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }

        // Expansion or collapse must be done bottom-up
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }
    
    /**
     * Supporting method used to construct the UserNode from the given UserInfoBean
     * and add it to the specified parent node.
     */
    private void addUserList( Object userData , TreePath destinationPath ){
        UserRolesInfoBean userRolesBean = new UserRolesInfoBean();
        
        RoleInfoBean roleInfoBean = null;
        int numUser = lstSendTo.getModel().getSize();
        String userId = ((UserRolesInfoBean)userData).getUserBean().getUserId();
        boolean found = false;
        int insertIndex = 0;
        for(int index = 0; index < numUser; index++){
            String listId = ((UserRolesInfoBean)lstSendTo.getModel().getElementAt(index)).getUserBean().getUserId();
            //check for duplicate user in list.
            if(userId.equalsIgnoreCase(listId)){
                found = true;
                break;
            }else if ( listId.compareToIgnoreCase(userId) < 0){
                insertIndex++;
            }
        }
        if( !found) {
            
            ((DefaultListModel)lstSendTo.getModel()).insertElementAt(userData, insertIndex);
            // ((DefaultListModel)lstSendTo.getModel()).addElement(userData);
        }
        
        //            UserRoleNode newChild = new UserRoleNode(userRolesBean);
        ////            modified = true;
        //            newParent.add(newChild);
        //            userRoleInfoBean = (UserRolesInfoBean)newParent.getUserObject();
        //            try{
        //                userRoleInfoBean = (UserRolesInfoBean)ObjectCloner.deepCopy((UserRolesInfoBean)newParent.getUserObject());
        //            }catch(Exception ex){
        //                ex.printStackTrace();
        //            }
        
        //            userRoleInfoBean.setRoleBean(userRoleInfoBean.getRoleBean());
        //
        //            userRoleInfoBean.setUserBean((UserInfoBean) userData);
        //            vecAddedUsers.add(userRoleInfoBean);
        //          }
    }//end of addUserList
    
    public void dropActionChanged(java.awt.dnd.DropTargetDragEvent dtde) {
    }
    
    
    private Vector getTreeData(){
        Vector treeData = new Vector();
        try{
            //connect to server and release the lock
            String rowId = null;
            rowId = proposalID;
            RequesterBean requester = new RequesterBean();
            requester.setDataObject(rowId);
            requester.setFunctionType('A');
            String connectTo =CoeusGuiConstants.CONNECTION_URL +CoeusGuiConstants.PROPOSAL_ACTION_SERVLET;
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);
            comm.send();
            ResponderBean res = comm.getResponse();
            treeData = res.getDataObjects();
        }catch(Exception e){
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
        
        //        Vector treeData = new Vector();
        //            for(int index1=0;index1<5;index1++){
        //                RoleInfoBean roleInfoBean = new RoleInfoBean();
        //                roleInfoBean.setRoleId(index1);
        //                roleInfoBean.setRoleName("Role "+ index1);
        //                roleInfoBean.setStatus('A');
        //                //int roleId = roleInfoBean.getRoleId();
        //                UserRolesInfoBean userRolesInfoBean = new UserRolesInfoBean();
        //                 TransferableUserRoleData data = new TransferableUserRoleData(userRolesInfoBean);
        //                userRolesInfoBean.setIsRole(true);
        //                userRolesInfoBean.setRoleBean(roleInfoBean);
        //                Vector users = new Vector();
        //                userRolesInfoBean.setHasChildren(true);
        //                //Vector users = new Vector();
        //                for(int indx =0 ; indx < 5; indx++ ) {
        //                    UserInfoBean userInfoBean = new UserInfoBean();
        //                    userInfoBean.setPersonId(""+indx);
        //                    userInfoBean.setPersonName("Person "+ indx);
        //                    userInfoBean.setStatus('A');
        //                    userInfoBean.setUserId(""+indx);
        //                    userInfoBean.setUserName("User "+indx);
        //                    UserRolesInfoBean userRolesInfoBean2 = new UserRolesInfoBean();
        //                    userRolesInfoBean2.setUserBean(userInfoBean);
        //                    userRolesInfoBean2.setIsRole(false);
        //                    userRolesInfoBean2.setHasChildren(false);
        //                    users.addElement(userRolesInfoBean2);
        //
        //                }
        //                userRolesInfoBean.setUsers(users);
        //                treeData.add(userRolesInfoBean);
        //            }
        return treeData;
    }

    public void actionPerformed(ActionEvent e) {
    }
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOk;
    public javax.swing.JButton btnRemove;
    public edu.mit.coeus.utils.CoeusTextField coeusTextField1;
    public javax.swing.JLabel lblPropNumber;
    public javax.swing.JLabel lblPropNumberDesc;
    public javax.swing.JLabel lblSponNumberDesc;
    public javax.swing.JLabel lblSponsorNumber;
    public javax.swing.JList lstSendTo;
    public javax.swing.JScrollPane scrPaneMessage;
    public javax.swing.JScrollPane scrPanePlayers;
    public javax.swing.JScrollPane scrPaneSendTo;
    public edu.mit.coeus.utils.tree.UserRoleTree treeUserRole;
    public javax.swing.JTextArea txtAreaMessage;
    // End of variables declaration//GEN-END:variables
    
}
