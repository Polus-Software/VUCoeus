/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.gui;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import javax.swing.event.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.propdev.gui.*;
import edu.mit.coeus.propdev.bean.*;

/** This screen allows the user to Approve, Reject or Bypass the proposal
 * ProposalRoutingForm.java
 * @author  Vyjayanthi
 * Created on January 5, 2004, 10:36 AM
 */
public class ProposalRoutingForm extends javax.swing.JComponent
implements ActionListener, ListSelectionListener {
    
    public CoeusDlgWindow dlgProposalRouting;
    
    /** Holds an instance of ListSelectionModel */
    private ListSelectionModel routingSelectionModel;
    
    /** Holds an instance of ProposalApprovalForm */
    private ProposalApprovalForm proposalApprovalForm;
    
    /** Holds an instance of ProposalRejectionForm */
    private ProposalRejectionForm proposalRejectionForm;
    
    /** Holds the ProposalDevelopmentFormBean */
    private ProposalDevelopmentFormBean proposalDevelopmentFormBean;
    
    /** Holds an instance of the CoeusAppletMDIForm */
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    //    /** Holds the map id, level number and stop number */
    private ProposalApprovalBean currentApprovalBean;
    
    /** Holds an instance of ProposalApprovalMapBean */
    private ProposalApprovalMapBean proposalApprovalMapBean;
    
    //private Vector vecProposalApprovalMapBeans;
    private CoeusVector vecProposalApprovalMapBeans;
    
    /** Flag to check if submit to sponsor action is performed successfully 
     * to enable/disable the Submit To Sponsor menu item in Proposal Detail Form
     */
    private boolean sponsorMenuEnabled = true;
    
    private BaseWindowObservable observable = new BaseWindowObservable();
    
    /** Holds CoeusMessageResources instance used for reading message Properties. */
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    
    private ProposalApprovalMapBean requiredProposalApprovalMapBean;
    
    private Vector vecParameters;
    
    private static final String USER_ID_FIELD = "userId";
    private static final String APPROVAL_STATUS_FIELD = "approvalStatus";
    private static final String PARENT_MAP_ID_FIELD = "parentMapId";
    private static final String WAITING_FOR_APPROVAL = "W";
    //Commented during Code-review
//    private static final char GET_PROP_ROUTING_DATA = 'P';//GET_PROP_ROUTING_DATA_FOR_APPROVE
    private static char serverFuntionType = 'Z';
    
    private static final String NO_CURRENT_STOP = "proposal_Action_exceptionCode.8011" ; //"There is no current stop"
    private static final String STOP_NOT_WAITING_FOR_APPROVAL = "proposal_Action_exceptionCode.8012" ; //"This stop is not Waiting for Approval"
    private static final String NO_ROUTING_MAP = "showRouting_exceptionCode.1138"; //"No routing maps have been defined for this proposal";
    
    private Frame parent;
    private boolean modal;
    
    private static final int WIDTH = 540;
    private static final int HEIGHT = 450;
    
    //# Added by Ranjeev
    private static final String TO_BE_SUBMITTED = "T";//QUERY_CONSTANTS
//    private static final String PASSED = "P";
    private static final String APPROVED = "A";
    private static final String REJECTED = "R";
    //Commented during Code-review
//    private static final String REJECTED_BY_OTHERS = "J";
//    private static final String BYPASSED = "B";
    
    private static final String BUILD_MAPS_OPTION_PARAM = "D";
    boolean isRoutingDataAvailable;
    
    private DefaultMutableTreeNode treeRoot;
    Color colorPanelBackground ;
    boolean isByPassFlag ;
    private boolean parentProposal;
    //#END Added by Ranjeev
    
    
    /** Holds the user data displayed in the table panel */
    private CoeusVector cvExistingApprovers;
    
    /** Holds all the approvers data */
    private CoeusVector cvEntireApprovers;
    
    /** Holds the user data displayed in the tree panel */
    private CoeusVector cvTreeView;
    
    //tempVector to hold the Map Beans
    private CoeusVector tempVecProposalApprovalMapBeans = new CoeusVector();
    //    private static final String SEQUENTIAL_STOP = "Sequential Stop ";
    
    /** Holds the connection string */
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
    "/ProposalActionServlet";
    
    private static final String TITLE = "Proposal Routing";
    //    private static final String EMPTY_STRING = "";
    
    /** Creates new form ProposalRoutingForm
     * @param parent holds the parent frame
     * @param propDevFormBean holds the proposal development data
     * @param modal holds true if modal, false otherwise
     */
    public ProposalRoutingForm(Frame parent, ProposalDevelopmentFormBean propDevFormBean, char serverFuntionType,boolean modal) {
        this.parent = parent;
        this.modal = modal;
        this.proposalDevelopmentFormBean = propDevFormBean;
        this.serverFuntionType = serverFuntionType;
        initComponents();
        ((DefaultTreeModel)treeSequentialStop.getModel()).setRoot(new ProposalApprovalMapBeanNode(new ProposalApprovalMapBean()));
        treeSequentialStop.setRootVisible(false);
        postInitComponents();
        registerComponents();
        getFormData();
        setFormData();
    }
    
    /** This method is called from within the constructor to
     * initialize the form. */
    private void postInitComponents(){
        dlgProposalRouting = new CoeusDlgWindow( parent, modal);
        dlgProposalRouting.getContentPane().add(this);
        dlgProposalRouting.setResizable(false);
        dlgProposalRouting.setTitle(TITLE);
        dlgProposalRouting.setFont(CoeusFontFactory.getLabelFont());
        dlgProposalRouting.setSize(WIDTH,HEIGHT);
        dlgProposalRouting.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgProposalRouting.getSize();
        dlgProposalRouting.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        dlgProposalRouting.setVisible(false);
    }
    
    /** This method is used to set the listeners to the components. */
    private void registerComponents(){
        
        /** Code for focus traversal - start */
        java.awt.Component[] components = { txtArComments,treeSequentialStop,tblProposalRouting, btnBypass,btnClose, btnReject,btnApprove };
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
        
        /** Code for focus traversal - end */
        
        routingSelectionModel = tblProposalRouting.getSelectionModel();
        routingSelectionModel.addListSelectionListener( this );
        routingSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
        tblProposalRouting.setSelectionModel( routingSelectionModel );
        
        btnApprove.addActionListener(this);
        btnBypass.addActionListener(this);
        btnClose.addActionListener(this);
        btnReject.addActionListener(this);
        
        dlgProposalRouting.addEscapeKeyListener( new AbstractAction("escPressed") {
            public void actionPerformed(ActionEvent ar) {
                close();
            }
            
        });
        
        dlgProposalRouting.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                close();
            }
            
        });
        
        btnClose.requestFocus(); 
        
    }
    
    public void registerObserver( Observer observer ) {
        observable.addObserver( observer );
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnTree = new javax.swing.JScrollPane();
        treeSequentialStop = new javax.swing.JTree();
        scrPnSequentialStop = new javax.swing.JScrollPane();
        tblProposalRouting = new edu.mit.coeus.propdev.gui.ProposalRoutingTable();
        scrPnComments = new javax.swing.JScrollPane();
        txtArComments = new javax.swing.JTextArea();
        btnApprove = new javax.swing.JButton();
        btnReject = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        lblComments = new javax.swing.JLabel();
        btnBypass = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        scrPnTree.setMinimumSize(new java.awt.Dimension(22, 175));
        scrPnTree.setPreferredSize(new java.awt.Dimension(3, 175));
        scrPnTree.setViewportView(treeSequentialStop);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 7, 0, 0);
        add(scrPnTree, gridBagConstraints);

        scrPnSequentialStop.setMinimumSize(new java.awt.Dimension(22, 175));
        scrPnSequentialStop.setPreferredSize(new java.awt.Dimension(3, 175));
        scrPnSequentialStop.setViewportView(tblProposalRouting);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 0, 0);
        add(scrPnSequentialStop, gridBagConstraints);

        scrPnComments.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrPnComments.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrPnComments.setMinimumSize(new java.awt.Dimension(375, 50));
        scrPnComments.setPreferredSize(new java.awt.Dimension(375, 50));
        scrPnComments.setEnabled(false);
        txtArComments.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        txtArComments.setFont(CoeusFontFactory.getNormalFont());
        scrPnComments.setViewportView(txtArComments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 2);
        add(scrPnComments, gridBagConstraints);

        btnApprove.setFont(CoeusFontFactory.getLabelFont());
        btnApprove.setMnemonic('A');
        btnApprove.setText("Approve");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(btnApprove, gridBagConstraints);

        btnReject.setFont(CoeusFontFactory.getLabelFont());
        btnReject.setMnemonic('R');
        btnReject.setText("Reject");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(btnReject, gridBagConstraints);

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(btnClose, gridBagConstraints);

        lblComments.setFont(CoeusFontFactory.getNormalFont());
        lblComments.setForeground(java.awt.Color.blue);
        lblComments.setText("Comments:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 7, 0, 0);
        add(lblComments, gridBagConstraints);

        btnBypass.setFont(CoeusFontFactory.getLabelFont());
        btnBypass.setMnemonic('B');
        btnBypass.setText("Bypass");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(btnBypass, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnApprove;
    public javax.swing.JButton btnBypass;
    public javax.swing.JButton btnClose;
    public javax.swing.JButton btnReject;
    public javax.swing.JLabel lblComments;
    public javax.swing.JScrollPane scrPnComments;
    public javax.swing.JScrollPane scrPnSequentialStop;
    public javax.swing.JScrollPane scrPnTree;
    public edu.mit.coeus.propdev.gui.ProposalRoutingTable tblProposalRouting;
    public javax.swing.JTree treeSequentialStop;
    public javax.swing.JTextArea txtArComments;
    // End of variables declaration//GEN-END:variables
    
    //    public static void main(String s[]){
    //        JFrame frame = new JFrame("Proposal Routing");
    //        frame.setSize(540, 450);
    //        ProposalRoutingForm form = new ProposalRoutingForm(frame, new ProposalDevelopmentFormBean(), true);
    //        frame.getContentPane().add(form);
    //        frame.show();
    //    }
    
    public CoeusVector sortByPrimaryApprover(CoeusVector cvProposalApprovalBean) {
        CoeusVector cvSortedData =  new CoeusVector();
        for(int index=0; index < cvProposalApprovalBean.size(); index++) {
            
            ProposalApprovalBean proposalApprovalBean = (ProposalApprovalBean ) cvProposalApprovalBean.get(index);
            
            if( cvSortedData.contains(proposalApprovalBean) ) continue;
            
            Equals eqLevelNumber = new Equals("levelNumber", new Integer(proposalApprovalBean.getLevelNumber() ));
            Equals eqStopNumber = new Equals("stopNumber", new Integer(proposalApprovalBean.getStopNumber() ));
            And eqLevelAndEqStop = new And(eqLevelNumber, eqStopNumber);
            CoeusVector cvFilteredData = cvProposalApprovalBean.filter(eqLevelAndEqStop);
            if( cvFilteredData.size() > 1 ){
                for( int subIndex = 0; subIndex < cvFilteredData.size(); subIndex++ ){
                    ProposalApprovalBean tempProposalApprovalBean = (ProposalApprovalBean)cvFilteredData.get(subIndex);
                    if( tempProposalApprovalBean.isPrimaryApproverFlag() ){
                        cvSortedData.addElement(tempProposalApprovalBean);
                        cvFilteredData.removeElement(tempProposalApprovalBean);
                        cvSortedData.addAll(cvFilteredData);
                        break;
                    }else{
                        continue;
                    }
                }
            }else{
                cvSortedData.addElement(proposalApprovalBean);
            }
            
//            if( proposalApprovalBean.isPrimaryApproverFlag() )
            System.out.println(proposalApprovalBean.getUserId()+"====="+proposalApprovalBean.getLevelNumber()+"====="+proposalApprovalBean.getStopNumber()+" proposalApprovalBean "+proposalApprovalBean.isPrimaryApproverFlag());
            
        }
        return cvSortedData; 
    }
    
    /** Sets the form data */
    private void setFormData(){
        
        if((proposalDevelopmentFormBean.getCreationStatusCode() == 2) ||
        (proposalDevelopmentFormBean.getCreationStatusCode() == 6)) {
            enableApproveRejectButtons(true);
        }else
            enableApproveRejectButtons(false);
        
        tblProposalRouting.setModel(tblProposalRouting.getTableModel());
        tblProposalRouting.formatTable();
       
        //ADDED by Ranjeev
        
        if(vecProposalApprovalMapBeans != null && vecProposalApprovalMapBeans.size() > 0) {
//            if( cvExistingApprovers != null && cvExistingApprovers.size() > 0 ){
                //Set the table data for the selected node
                //tblProposalRouting.setTableData(addSequentialStops(cvExistingApprovers));
//            }
         
            treeSequentialStop.setCellRenderer(new TreeNodeRenderer() );
            treeSequentialStop.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            
            //Creates the Tree with the Vector of ProposalApprovalMapBean
            createTree();
            
            //Set the current approval bean
            setCurrentApprovalBean();
            
            treeSequentialStop.addTreeSelectionListener(new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent evt) {
                    // Get all nodes whose selection status has changed
                    try {
                        TreePath path = treeSequentialStop.getSelectionPath();
//                        TreePath [] paths = evt.getPaths();   //Commented during Code-review
                        if( path == null ) return ;
                        ProposalApprovalMapBeanNode selectedProposalApprovalMapBeanNode = (ProposalApprovalMapBeanNode)path.getLastPathComponent();
                        ProposalApprovalMapBean selectedProposalApprovalMapBean = selectedProposalApprovalMapBeanNode.getDataObject();
                        cvExistingApprovers = selectedProposalApprovalMapBean.getProposalApprovals();
                        
                        if( cvExistingApprovers != null && cvExistingApprovers.size() > 0 ){
//                            cvExistingApprovers.sort(sortingFields,true);
//                            CoeusVector cvFormatedData = sortByPrimaryApprover(cvExistingApprovers);
                            //Set the table data for the selected node
                            tblProposalRouting.setTableData(addSequentialStops(cvExistingApprovers));
                            tblProposalRouting.setRowSelectionInterval(1,1);
                        }
                        
                    }catch (Exception exp ) {
                        exp.printStackTrace();
                    }
                    
                }
            });
            
            //Set the Tree Node Selection corresponding to the map id of the current bean
            setTreeNodeSelection();
            
            //TreePath [] paths = treeSequentialStop.getPathForRow(row);
            //treeSequentialStop.setSelectionRow(0);
            //treeSequentialStop.setSelectionPath(path);
            
            treeSequentialStop.setBackground(colorPanelBackground);
            //Check if ByPass Button is to be Visible
            if(isByPassFlag){
                btnBypass.setVisible(true);
            }else{
                btnBypass.setVisible(false);
            }
            
            //Set the first row as the selected row
            if(tblProposalRouting.getRowCount() > 0){
                tblProposalRouting.setRowSelectionInterval(1, 1);
            }
            
        } else {
            isRoutingDataAvailable = false;
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_ROUTING_MAP));
        }
        
        
    }
    
    /**
     * Set the Tree Node Selection based on ApprovalStatus Flag passed to it
     */
//     private boolean setTreeNodeSelection(String approvalStatus) {
//        
//     
//         boolean isSelectionNodeIdentified  = false;
//                        
//        try {
//       
//        TreePath path = null;
//        for(int index =0 ;index < treeSequentialStop.getRowCount(); index++) {
//            path = treeSequentialStop.getPathForRow(index);
//            ProposalApprovalMapBeanNode selectedProposalApprovalMapBeanNode = (ProposalApprovalMapBeanNode)path.getPathComponent(treeSequentialStop.getRowForPath(path));
//            ProposalApprovalMapBean selectedProposalApprovalMapBean = selectedProposalApprovalMapBeanNode.getDataObject();
//            CoeusVector  cvEachMapBeanApprovers = selectedProposalApprovalMapBean.getProposalApprovals();
//            for(int subIndex =0 ;subIndex < cvEachMapBeanApprovers.size(); subIndex++) {
//                
//                ProposalApprovalBean proposalApprovalBean = (ProposalApprovalBean )cvEachMapBeanApprovers.get(subIndex);
//                if(proposalApprovalBean.getApprovalStatus().equalsIgnoreCase(approvalStatus)) {
//                    treeSequentialStop.setSelectionPath(path);
//                    requiredProposalApprovalMapBean = selectedProposalApprovalMapBean;
//                    cvExistingApprovers = selectedProposalApprovalMapBeanNode.getDataObject().getProposalApprovals();
//                    isSelectionNodeIdentified = true;
//                    subIndex = cvEachMapBeanApprovers.size();
//                    index = treeSequentialStop.getRowCount();
//                }
//            }
//        }
//        if((isSelectionNodeIdentified == false ) && ( treeSequentialStop.getRowCount() > 0))
//            treeSequentialStop.setSelectionPath(treeSequentialStop.getPathForRow(treeSequentialStop.getRowCount() - 1));
//        
//        }catch(Exception treeSelectionExp) {
//           // treeSelectionExp.printStackTrace();
//        }
//        return isSelectionNodeIdentified;
//    }
     private boolean setTreeNodeSelection() {
        
     
         boolean isSelectionNodeIdentified  = false;
                        
        try {
            treeSequentialStop.clearSelection();
            selectNode(treeRoot);
            TreePath selectedPath = treeSequentialStop.getSelectionPath();
            if( selectedPath == null) {
                isSelectionNodeIdentified = false;
            }else{
                isSelectionNodeIdentified = true;
            }
        if((isSelectionNodeIdentified == false ) && ( treeSequentialStop.getRowCount() > 0)){
            treeSequentialStop.setSelectionPath(treeSequentialStop.getPathForRow(treeSequentialStop.getRowCount() - 1));
        }
        
        }catch(Exception treeSelectionExp) {
            treeSelectionExp.printStackTrace();
        }
        return isSelectionNodeIdentified;
    }
    
     private void selectNode(DefaultMutableTreeNode node) {
        if( node != null ) {

            ProposalApprovalMapBean selectedProposalApprovalMapBean = 
                ((ProposalApprovalMapBeanNode)node).getDataObject();
            CoeusVector  cvEachMapBeanApprovers = selectedProposalApprovalMapBean.getProposalApprovals();
            int approversSize = 0;
            if( cvEachMapBeanApprovers != null ) {
                approversSize = cvEachMapBeanApprovers.size();
            }
            for(int subIndex =0 ;subIndex < approversSize; subIndex++) {
                
                ProposalApprovalBean proposalApprovalBean = (ProposalApprovalBean )cvEachMapBeanApprovers.get(subIndex);
                if(proposalApprovalBean.getMapId() == currentApprovalBean.getMapId() ){
//                if(proposalApprovalBean.getApprovalStatus().equalsIgnoreCase(approvalStatus)) {
                    treeSequentialStop.setSelectionPath(new TreePath(node.getPath()));
                    requiredProposalApprovalMapBean = selectedProposalApprovalMapBean;
                    cvExistingApprovers = ((ProposalApprovalMapBeanNode)node).getDataObject().getProposalApprovals();
                    //isSelectionNodeIdentified = true;
                    return;
                }
            }
            if( node.getChildCount() > 0 ) {
                Enumeration enumChildren = node.children();
                while(enumChildren.hasMoreElements()){
                    DefaultMutableTreeNode childNode = 
                        (DefaultMutableTreeNode)enumChildren.nextElement();
                    selectNode(childNode);
                }
            }
        }
         
     }
     
    /** Creates the Tree with the Vector of ProposalMapBean obtained from server
     *  This Method builds the Tree using the Vector of ProposalApprovalMapBeans
     */
      public void createTree() {
        
        if(treeRoot != null){
            treeRoot.removeAllChildren();
        }
        
        //Commented during Code-review
//        Hashtable hashTable = new Hashtable();
        
        tempVecProposalApprovalMapBeans.removeAllElements();
        tempVecProposalApprovalMapBeans.addAll(vecProposalApprovalMapBeans);
        tempVecProposalApprovalMapBeans.sort(PARENT_MAP_ID_FIELD, true);
        
        // setting the Root Node
        ProposalApprovalMapBean proposalApprovalMapBean = getParentMapBean();
        treeRoot = (DefaultMutableTreeNode)((DefaultTreeModel)treeSequentialStop.getModel()).getRoot();
        addNode(treeRoot,proposalApprovalMapBean);
        (( DefaultTreeModel )treeSequentialStop.getModel() ).reload();
        expandAll(treeSequentialStop,true);
        
        
//        String selectedNodeValue = getLastMapIdValue(vecProposalApprovalMapBeans);
//        TreePath selectedPath = treeSequentialStop.getSelectionPath();
//         
//         if( selectedPath != null ) {
//             ProposalApprovalMapBeanNode selectedNode = ( ProposalApprovalMapBeanNode )selectedPath.getLastPathComponent();
//             ProposalApprovalMapBean proposalApprovalMapBeanLinkBean = selectedNode.getDataObject();
//             selectedNodeValue = proposalApprovalMapBeanLinkBean.getDescription().toString();
//             cvExistingApprovers = proposalApprovalMapBeanLinkBean.getProposalApprovals();
//         }
//         TreePath newSelectedPath = findByName(treeSequentialStop,selectedNodeValue );
//         if( newSelectedPath != null ) {
//             treeSequentialStop.expandPath( newSelectedPath );
//             treeSequentialStop.setSelectionPath( newSelectedPath );
//             treeSequentialStop.scrollRowToVisible(
//             treeSequentialStop.getRowForPath(newSelectedPath));
//             
//         }
         
         
    }
      
      public String getLastMapIdValue(CoeusVector vecProposalApprovalMapBeans) {

        ProposalApprovalMapBean parentProposalApprovalMapBean = null;
        CoeusVector cvSortedMapBeans = new CoeusVector();
        cvSortedMapBeans.addAll(vecProposalApprovalMapBeans);
        ProposalApprovalMapBean proposalApprovalMapBean;
        for(int index = 0; index < cvSortedMapBeans.size();index++) {
            
            proposalApprovalMapBean = (ProposalApprovalMapBean) cvSortedMapBeans.get(index);
            int  mapId = proposalApprovalMapBean.getMapId();
            Equals eqMapID = new Equals(PARENT_MAP_ID_FIELD, new Integer(mapId));
            CoeusVector parentVector =  cvSortedMapBeans.filter(eqMapID);
            if (parentVector == null || parentVector.size() == 0) {
                parentProposalApprovalMapBean = proposalApprovalMapBean;
                index = tempVecProposalApprovalMapBeans.size();
            } else {
                cvSortedMapBeans.remove(proposalApprovalMapBean);
                index = 0;
            }
        }
        return parentProposalApprovalMapBean.getDescription();
    }
       
      

      
      /**
     * To get the TreePath from the selected Tree Node.
     * @param tree DnDJTree Instance of complete AOR data
     * @param name Name of the String to be Searched in the tree Nodes.
     *
     * @return TreePath complete path from root to the match found node.
     */
    public TreePath findByName( JTree tree, String name ) {
        
        TreeNode root = (TreeNode)tree.getModel().getRoot();
        TreePath result = find2(tree, new TreePath(root), name);
        return result;
    }
    
    
    //supporting method for findByName method - recurcive implementation.
    private TreePath find2(JTree tree, TreePath parent, String nodeName) {
        
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (node != null && node.toString().trim().startsWith(nodeName)) {
            return parent;
        }else{
            
//            Object o = node;  //Commented during Code-review
            if (node.getChildCount() >= 0) {
                for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                    TreeNode nodes = (TreeNode)e.nextElement();
                    TreePath path = parent.pathByAddingChild(nodes);
                    TreePath result = find2(tree, path, nodeName);
                    if(result!=null){
                        return result;
                    }
                    // Found a match
                }
            }
        }
        // No match at this branch
        return null;
    }
    
      
      private void addNode(DefaultMutableTreeNode parent,ProposalApprovalMapBean proposalApprovalMapBean){
        ProposalApprovalMapBeanNode childProposalApprovalMapBeanNode = new ProposalApprovalMapBeanNode(proposalApprovalMapBean);
        
        parent.add(childProposalApprovalMapBeanNode);
         Equals equalsmapID = new Equals(PARENT_MAP_ID_FIELD, new Integer(proposalApprovalMapBean.getMapId()));
            CoeusVector children =  vecProposalApprovalMapBeans.filter(equalsmapID);
            if( children != null && children.size() > 0 ) {
                for( int indx = 0; indx < children.size(); indx++ ) {
                    ProposalApprovalMapBean childApprovalMapBean = 
                        (ProposalApprovalMapBean)children.elementAt(indx);
                    addNode(childProposalApprovalMapBeanNode, childApprovalMapBean);
                }
            }
    }      

    public void createTree_old() {
        
        if(treeRoot != null){
            treeRoot.removeAllChildren();
        }
        
        Hashtable hashTable = new Hashtable();
        
        tempVecProposalApprovalMapBeans.removeAllElements();
        tempVecProposalApprovalMapBeans.addAll(vecProposalApprovalMapBeans);
        tempVecProposalApprovalMapBeans.sort(PARENT_MAP_ID_FIELD, true);
        
        // setting the Root Node
        ProposalApprovalMapBean proposalApprovalMapBean = getParentMapBean();
        ProposalApprovalMapBeanNode proposalApprovalMapBeanNode;
        proposalApprovalMapBeanNode = new ProposalApprovalMapBeanNode(proposalApprovalMapBean);
        
        ((DefaultTreeModel)treeSequentialStop.getModel()).setRoot(proposalApprovalMapBeanNode);
        
        treeRoot = (DefaultMutableTreeNode)((DefaultTreeModel)treeSequentialStop.getModel()).getRoot();
        
        hashTable.put(((ProposalApprovalMapBean)proposalApprovalMapBeanNode.getDataObject()).getParentMapId()+"",treeRoot );
        
        //parentProposalApprovalMapBeanNode = treeRoot;
        ProposalApprovalMapBeanNode parentProposalApprovalMapBeanNode = null;
        ProposalApprovalMapBeanNode childProposalApprovalMapBeanNode;
        for(int index = 0; index < tempVecProposalApprovalMapBeans.size();index++) {
            
            childProposalApprovalMapBeanNode = new ProposalApprovalMapBeanNode(getParentMapBean());
            ProposalApprovalMapBean proposalApprovalMapBeans = childProposalApprovalMapBeanNode.getDataObject();
            
            parentProposalApprovalMapBeanNode = (ProposalApprovalMapBeanNode) hashTable.get(proposalApprovalMapBeans.getParentMapId()+"");
            if(parentProposalApprovalMapBeanNode == null){
                treeRoot.add(childProposalApprovalMapBeanNode);
                hashTable.put(((ProposalApprovalMapBean)childProposalApprovalMapBeanNode.getDataObject()).getParentMapId()+"",childProposalApprovalMapBeanNode);
            }
            else {
                
                //parentProposalApprovalMapBeanNode.add(childProposalApprovalMapBeanNode);
                //treeRoot.add(childProposalApprovalMapBeanNode);
                //hashTable.put(proposalApprovalMapBeans.getParentMapId()+"",childProposalApprovalMapBeanNode);
            }
            
            (( DefaultTreeModel )treeSequentialStop.getModel() ).reload();
            
            
        }
   
    }
 
    
    
    /**
     *Getting the Each Node MapBean from the Vector of ProposalApprovalMapBean
     */
    
    public ProposalApprovalMapBean getNodeMapBean(CoeusVector vecProposalApprovalMapBeans) {
        
        ProposalApprovalMapBean parentProposalApprovalMapBean = null;
        for(int index = 0; index < vecProposalApprovalMapBeans.size();index++) {
            
            ProposalApprovalMapBean proposalApprovalMapBeans = (ProposalApprovalMapBean) vecProposalApprovalMapBeans.get(index);
            int parentNodeID = proposalApprovalMapBeans.getParentMapId();
            NotEquals notEqualsmapID = new NotEquals("mapId",new Integer(parentNodeID));
            CoeusVector parentVector =  vecProposalApprovalMapBeans.filter(notEqualsmapID);
            
            for(int subindex = 0; subindex < vecProposalApprovalMapBeans.size();subindex ++) {
                ProposalApprovalMapBean subProposalApprovalMapBean =  (ProposalApprovalMapBean) vecProposalApprovalMapBeans.get(index);
                if(parentNodeID != subProposalApprovalMapBean.getMapId()){
                    parentProposalApprovalMapBean = proposalApprovalMapBeans;
                }
            }
        }
        
        return parentProposalApprovalMapBean;
    }
    
    
    /**
     * Method Returns the ProposalApprovalMapBean Map  from the Vector of ProposalApprovalMapBean
     */
    public ProposalApprovalMapBean getParentMapBean() {
        //tempVecProposalApprovalMapBeans
        
        ProposalApprovalMapBean parentProposalApprovalMapBean = null;
        CoeusVector cvSortedMapBeans = new CoeusVector();
        ProposalApprovalMapBean proposalApprovalMapBean;
        for(int index = 0; index < tempVecProposalApprovalMapBeans.size();index++) {
            
            proposalApprovalMapBean = (ProposalApprovalMapBean) tempVecProposalApprovalMapBeans.get(index);
            int parentNodeID = proposalApprovalMapBean.getParentMapId();
            Equals eqMapID = new Equals("mapId",new Integer(parentNodeID));
            CoeusVector parentVector =  tempVecProposalApprovalMapBeans.filter(eqMapID);
            if (parentVector == null || parentVector.size() == 0) {
                parentProposalApprovalMapBean = proposalApprovalMapBean;
                tempVecProposalApprovalMapBeans.remove(index);
                index = tempVecProposalApprovalMapBeans.size();
            }
        }
        return parentProposalApprovalMapBean;
    }
    
    /**
     * Getting the Parent Map Id from the Vector containing ProposalApprovalMapBean
     */
    
    public int getParentMapId(Vector vecProposalApprovalMapBeans) {
        
        int parentMapId = 0;
        
        for(int index = 0; index < vecProposalApprovalMapBeans.size();index++) {
            
            ProposalApprovalMapBean proposalApprovalMapBeans = (ProposalApprovalMapBean) vecProposalApprovalMapBeans.get(index);
            int parentNodeID = proposalApprovalMapBean.getParentMapId();
            
            for(int subindex = 0; subindex < vecProposalApprovalMapBeans.size();subindex ++) {
                ProposalApprovalMapBean subProposalApprovalMapBean =  (ProposalApprovalMapBean) vecProposalApprovalMapBeans.get(index);
                if(parentNodeID == subProposalApprovalMapBean.getMapId()){
                    break;
                }else
                    parentMapId =  parentNodeID;
            }
        }
        
        return parentMapId;
    }
    
    
    private void setCurrentApprovalBean(){
        Equals eqActualUserId = new Equals(USER_ID_FIELD, mdiForm.getUserId());
        Equals eqUpperUserId = new Equals(USER_ID_FIELD, mdiForm.getUserId().toUpperCase());
        Or eqUserId = new Or(eqActualUserId, eqUpperUserId);
        Equals eqApprovalStatus = new Equals(APPROVAL_STATUS_FIELD, WAITING_FOR_APPROVAL);
        And eqUserIdAndEqApprovalStatus = new And(eqUserId, eqApprovalStatus);
        //        ProposalApprovalBean proposalApprovalBean;
        currentApprovalBean = new ProposalApprovalBean();
        //Get the  waiting for approval row
        CoeusVector cvFilteredData = cvEntireApprovers.filter(eqUserIdAndEqApprovalStatus);//cvExistingApprovers
        if( cvFilteredData != null && cvFilteredData.size() > 0 ){
            //Get the bean corresponding to the last index in the vector
            currentApprovalBean = (ProposalApprovalBean)cvFilteredData.get(cvFilteredData.size() - 1);
        }else{
            //This user is not waiting for approval
            enableApproveRejectButtons(false);
            
            //Get the bean corresponding to the first index in the vector
            if(cvEntireApprovers != null && cvEntireApprovers.size() > 0)
                currentApprovalBean = (ProposalApprovalBean)cvEntireApprovers.get(0);//or cvExistingApprovers
            
        }
        //        partialBean.setProposalNumber(proposalApprovalBean.getProposalNumber());
        //        partialBean.setMapId(proposalApprovalBean.getMapId());
        //        partialBean.setLevelNumber(proposalApprovalBean.getLevelNumber());
        //        partialBean.setStopNumber(proposalApprovalBean.getStopNumber());
        //        partialBean.setComments(proposalApprovalBean.getComments());
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        try{
            mdiForm.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            if( source.equals(btnApprove) ){
                performApproveAction();
            }else if( source.equals(btnBypass) ){
                performBypassAction();
            }else if( source.equals(btnReject) ){
                performRejectAction();
            }else if( source.equals(btnClose) ){
                close();//added by ranjeev     //dlgProposalRouting.dispose();
            }
        } finally{
                mdiForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }
    
    /** Method to enable/disable Approve and Reject buttons when showRouting is selected
     * @param enable true if set, false otherwise
     */
    public void enableApproveRejectButtons(boolean enable){
        if( enable ){
            btnApprove.setEnabled(true);
            btnReject.setEnabled(true);
        }else{
            btnApprove.setEnabled(false);
            btnReject.setEnabled(false);
        }
    }
    
    /** Allows the user to approve the proposal */
    private void performApproveAction(){
        proposalApprovalForm = new ProposalApprovalForm(parent, modal);
        proposalApprovalForm.setParentProposal(isParentProposal());
        proposalApprovalForm.setProposalDevelopmentFormBean(proposalDevelopmentFormBean);
        proposalApprovalForm.setCurrentApprovalBean(currentApprovalBean);
        proposalApprovalForm.setObservable(observable);
        proposalApprovalForm.setFormData(cvExistingApprovers);
        
        boolean approveClicked = proposalApprovalForm.display();
        sponsorMenuEnabled = proposalApprovalForm.isSponsorMenuEnabled();
        if( approveClicked ){
            observable.notifyObservers(proposalApprovalForm.getProposalDevelopmentFormBean());
            enableApproveRejectButtons(false);
            proposalApprovalForm.setApprovalActionPerformed(false);
        }
        getFormData();
        createTree();
        setCurrentApprovalBean();
        setTreeNodeSelection();
        tblProposalRouting.setTableData(addSequentialStops(cvExistingApprovers));
        dlgProposalRouting.pack();
        dlgProposalRouting.validate();
        
    }
    
    /** Allows the user to bypass the proposal */
    private void performBypassAction(){
        int selectedRow = tblProposalRouting.getSelectedRow();
        if( selectedRow < 0 ){
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey(NO_CURRENT_STOP));
            return;
        }else{
            //Approval status of the selected row should be waiting for approval
            //if(selectedRow > 0)
            //selectedRow -= 1;
            //currentApprovalBean = (ProposalApprovalBean)cvExistingApprovers.get(selectedRow);
            currentApprovalBean = tblProposalRouting.getSelectedProposalApprovalBean(selectedRow);
            
            
            if( !currentApprovalBean.getApprovalStatus().equals(WAITING_FOR_APPROVAL) ){
                CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey(STOP_NOT_WAITING_FOR_APPROVAL));
                return;
            }
        }
        proposalApprovalForm = new ProposalApprovalForm(parent, modal);
        proposalApprovalForm.setParentProposal(isParentProposal());
        proposalApprovalForm.setProposalDevelopmentFormBean(proposalDevelopmentFormBean);
        proposalApprovalForm.setCurrentApprovalBean(currentApprovalBean);
        proposalApprovalForm.setFormData(cvExistingApprovers);
        //To set the appropriate buttons for Bypass action and remove all Approve buttons
        proposalApprovalForm.formatButtons(true);
        boolean bypassClicked = proposalApprovalForm.display();
        if( bypassClicked ){
            observable.notifyObservers(proposalApprovalForm.getProposalDevelopmentFormBean());
            enableApproveRejectButtons(false);
            getFormData();
            createTree();
            tblProposalRouting.setTableData(addSequentialStops(cvExistingApprovers));
            
        }
        setTreeNodeSelection();
    }
    
    /** Displays the ProposalRejectionForm and allows the user to Reject the proposal */
    private void performRejectAction(){
        proposalRejectionForm = new ProposalRejectionForm(parent, modal);
        proposalRejectionForm.setParenProposal(isParentProposal());
        proposalRejectionForm.setCurrentApprovalBean(currentApprovalBean);
        boolean rejectClicked = proposalRejectionForm.display();
        if( rejectClicked ){
            observable.notifyObservers(proposalRejectionForm.getProposalDevelopmentFormBean());
            enableApproveRejectButtons(false);
            getFormData();
            createTree();
            setCurrentApprovalBean();
            tblProposalRouting.setTableData(addSequentialStops(cvExistingApprovers));
            
        }
        
        setTreeNodeSelection();
    }
    
    /** This method sets the comments based on the valueChanged of listSelectionEvent
     * @param listSelectionEvent takes the listSelectionEvent */
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if( listSelectionEvent.getValueIsAdjusting() ) return ;
        try {
            
            Object source = listSelectionEvent.getSource();
            int selectedRow = tblProposalRouting.getSelectedRow();
            if( (source.equals(routingSelectionModel) )&& (selectedRow >= 0 ) &&
            (cvExistingApprovers != null)) {
                //System.out.println(selectedRow);
                
                
                ProposalApprovalBean proposalApprovalBean = null;// = tblProposalRouting.getSelectedProposalApprovalBean(selectedRow);
                
//                if(proposalApprovalBean == null) {
                    if(selectedRow > 0){
                        selectedRow -= 1;
                    }
                    if(cvExistingApprovers != null && cvExistingApprovers.size() > 0){
//                        if(selectedRow > 0){
//                            selectedRow = selectedRow -1;
//                        }
                        proposalApprovalBean= (ProposalApprovalBean)cvExistingApprovers.get(selectedRow);
                    }
                    
//                }
                
                txtArComments.setLineWrap(true);
                txtArComments.setWrapStyleWord(true);
                txtArComments.setEditable(false);
                txtArComments.setText(proposalApprovalBean.getComments());
                
                dlgProposalRouting.pack();
                dlgProposalRouting.validate();
                
            }
        }catch(Exception exp) {
            exp.printStackTrace();
        }
    }
    
    /** Method to get all the beans from the server */
    private void getFormData(){
        //Get the user data into cvExistingApprovers and tree data into cvTreeView
        try {
            isRoutingDataAvailable = false;
            Vector vecRequestParameters = new Vector();
            vecRequestParameters.add(proposalDevelopmentFormBean.getProposalNumber());
            vecRequestParameters.add(proposalDevelopmentFormBean.getOwnedBy());
            vecRequestParameters.add(BUILD_MAPS_OPTION_PARAM);
            
            cvExistingApprovers = new CoeusVector();
            vecProposalApprovalMapBeans = new CoeusVector();
            cvTreeView = new CoeusVector();
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType(serverFuntionType); //GET_PROP_ROUTING_DATA
            //requester.setDataObject(proposalDevelopmentFormBean.getProposalNumber());
            requester.setDataObjects(vecRequestParameters);
            
            AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
            comm.send();
            ResponderBean response = comm.getResponse();
            Vector vecData = new Vector();
            if ( response != null ){
                if( response.isSuccessfulResponse() ){
                    vecData = response.getDataObjects();
                    //modified by Ranjeev
                    
                    try {
                        
                        if(vecData != null && vecData .size() > 0) {
                            int buildMapID = 0;
                            if(vecData.get(0).getClass() == Integer.class) {
                                buildMapID = ((Integer) vecData.get(0)).intValue();
                                if(buildMapID < 1){
                                    return;
                                }
                            }
                            
                            if(vecData.get(1) != null && vecData.get(1).getClass() == CoeusVector.class) {
                                vecProposalApprovalMapBeans = (CoeusVector)vecData.get(1);
                                //Set the approvers
                                cvEntireApprovers = new CoeusVector();
                                for( int index = 0; index < vecProposalApprovalMapBeans.size(); index++ ){
                                    ProposalApprovalMapBean proposalApprovalMapBean = 
                                        (ProposalApprovalMapBean)vecProposalApprovalMapBeans.get(index);
                                    if(proposalApprovalMapBean.getProposalApprovals() != null)
                                        cvEntireApprovers.addAll(proposalApprovalMapBean.getProposalApprovals());
                                }
                                proposalApprovalMapBean = (ProposalApprovalMapBean)vecProposalApprovalMapBeans.get(0);
                                cvExistingApprovers = proposalApprovalMapBean.getProposalApprovals();
                            }
                            
                            if(vecData.get(2).getClass() == Boolean.class) {
                                //  Checking the enable bypass buuton if user has right
                                //  and there are some stops Waiting for approval
                                
                                if(((Boolean) vecData.get(2)).booleanValue()) {
                                    //if(cvExistingApprovers != null && cvExistingApprovers.size() > 0)
                                    if(cvEntireApprovers != null && cvEntireApprovers.size() > 0){
                                        isByPassFlag = isAnyWaitingState(cvEntireApprovers);//cvExistingApprovers
                                    }
                                }
                                
                            }
                            
                            isRoutingDataAvailable = true;
                        }
                        
                    }catch(Exception exp) {
                        exp.printStackTrace();
                    }
                    
                }
            }
            
        }catch(Exception exp) {
            exp.printStackTrace();
        }
        
        
    }
    
    /**
     * Method to Check any Waiting For approval State in the Vector of vecProposalApprovalMap
     */
    private boolean  isAnyWaitingState(CoeusVector vecProposalApprovalMap) {
        Equals equalsWait = new Equals("approvalStatus", WAITING_FOR_APPROVAL);
        CoeusVector vecFilteredMap  = vecProposalApprovalMap.filter(equalsWait);
        if(vecFilteredMap != null && vecFilteredMap.size() > 0){
            return true;
        }else{
            return false;
        }
    }
    
    
    /**
     * Method to Check a given Approval Map Status in the Vector of vecProposalApprovalMap
     */
    private boolean  isMatchingState(CoeusVector vecProposalApprovalMap,String approvalStatus) {
        Equals equalsWait = new Equals("approvalStatus",approvalStatus);
        CoeusVector vecFilteredMap  = vecProposalApprovalMap.filter(equalsWait);
        if(vecFilteredMap != null && vecFilteredMap.size() > 0){
            return true;
        }else{
            return false;
        }
    }
    
    /** Displays the screen */
    public boolean display(){
        if(isRoutingDataAvailable){
            dlgProposalRouting.setVisible(true);
        }else{
            close();
        }
        return sponsorMenuEnabled;
    }
    
    /** Close To Dispose the Form*/
    public void close(){
        dlgProposalRouting.dispose();
    }
    
    
    /** Getter for property vecParameters.
     * @return Value of property vecParameters.
     *
     */
    public java.util.Vector getVecParameters() {
        return vecParameters;
    }
    
    /** Setter for property vecParameters.
     * @param vecParameters New value of property vecParameters.
     *
     */
    public void setVecParameters(java.util.Vector vecParameters) {
        this.vecParameters = vecParameters;
    }
    
    
    // START ===== Added by Ranjeev
    
    /**
     * Method to Add the Sequential Row to Table tblProposalRouting while Setting the
     * Vector of ProposalApprovalBeans. This Bean is a dunmmy bean which is checked in
     * ProposalRouting Table Renderer while rendering the rows.
     */
    
    public CoeusVector  addSequentialStops(CoeusVector vecProposalApprovalBean) {
        Hashtable eachSeqlevel = new Hashtable();
        CoeusVector tempcVecProposalApprovalBean = new CoeusVector();
        String sortParameter [] = {"levelNumber","stopNumber"};
        vecProposalApprovalBean.sort(sortParameter,true);
        for(int index=0;index < vecProposalApprovalBean.size();index++) {
            
            ProposalApprovalBean proposalApprovalBean = (ProposalApprovalBean) vecProposalApprovalBean.get(index);
            String levelKey = proposalApprovalBean.getLevelNumber()+"";
            if(!eachSeqlevel.containsKey((Object) levelKey)) {
                eachSeqlevel.put(levelKey,levelKey);
                
                ProposalApprovalBean newProposalApprovalBean = new ProposalApprovalBean();
                newProposalApprovalBean.setProposalNumber(proposalApprovalBean.getProposalNumber());
                newProposalApprovalBean.setMapId(proposalApprovalBean.getLevelNumber());
                newProposalApprovalBean.setDescription(proposalApprovalBean.getDescription());
                newProposalApprovalBean.setLevelNumber(proposalApprovalBean.getLevelNumber());
                newProposalApprovalBean.setStopNumber(proposalApprovalBean.getStopNumber());
                newProposalApprovalBean.setUserId(proposalApprovalBean.getUserId());
                newProposalApprovalBean.setPrimaryApproverFlag(proposalApprovalBean.isPrimaryApproverFlag());
                newProposalApprovalBean.setApprovalStatus("Z");
                newProposalApprovalBean.setComments(null);
                tempcVecProposalApprovalBean.add(tempcVecProposalApprovalBean.size(),newProposalApprovalBean);
                
            }
            tempcVecProposalApprovalBean.add(proposalApprovalBean);
            
            
        }
        return tempcVecProposalApprovalBean;
        
    }
    
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
    
    /** Getter for property proposalDevelopmentFormBean.
     * @return Value of property proposalDevelopmentFormBean.
     *
     */
    public edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean getProposalDevelopmentFormBean() {
        return proposalDevelopmentFormBean;
    }    
    
    /** Setter for property proposalDevelopmentFormBean.
     * @param proposalDevelopmentFormBean New value of property proposalDevelopmentFormBean.
     *
     */
    public void setProposalDevelopmentFormBean(edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean proposalDevelopmentFormBean) {
        this.proposalDevelopmentFormBean = proposalDevelopmentFormBean;
    }
    
    /**
     * Getter for property parentProposal.
     * @return Value of property parentProposal.
     */
    public boolean isParentProposal() {
        return parentProposal;
    }
    
    /**
     * Setter for property parentProposal.
     * @param parentProposal New value of property parentProposal.
     */
    public void setParentProposal(boolean parentProposal) {
        this.parentProposal = parentProposal;
    }
    
    /** Represent the Node of the Tree that Contains the ProposalApprovalMapBean  for each Node*/
    class ProposalApprovalMapBeanNode extends javax.swing.tree.DefaultMutableTreeNode {
        
        private ProposalApprovalMapBean proposalApprovalMapBean;
        /** Creates a new instance of ProposalApprovalMapBeanNode */
        public ProposalApprovalMapBeanNode() {
        }
        public ProposalApprovalMapBeanNode(ProposalApprovalMapBean proposalApprovalMapBean) {
            super(proposalApprovalMapBean);
            this.proposalApprovalMapBean = proposalApprovalMapBean;
        }
        public void setDataObject(ProposalApprovalMapBean proposalApprovalMapBean){
            this.proposalApprovalMapBean =proposalApprovalMapBean;
        }
        
        public ProposalApprovalMapBean getDataObject(){
            return proposalApprovalMapBean;
        }
    }
    
    /** Tree Node Renderer for the Tree treeSequentialStop */ 
    
    class TreeNodeRenderer extends DefaultTreeCellRenderer{
        
        ImageIcon imgVerifyIcon,imageChildIcon,imgApproveIcon,imgPassIcon,imgRejectIcon;
       
        TreeNodeRenderer() {
            
            java.net.URL imageURLVerifyIcon = getClass().getClassLoader().getResource( CoeusGuiConstants.VERIFY_ICON_PATH);
            java.net.URL imageURLChildIcon = getClass().getClassLoader().getResource( CoeusGuiConstants.CHILD_TREE_NODE );
            java.net.URL imageURLApproveIcon = getClass().getClassLoader().getResource( CoeusGuiConstants.APPROVE_ICON_PATH );
            
            java.net.URL imageURLPassIcon = getClass().getClassLoader().getResource( CoeusGuiConstants.PASS_ICON_PATH  );
            java.net.URL imageURLRejectIcon = getClass().getClassLoader().getResource( CoeusGuiConstants.REJECT_ICON_PATH);
            
            imgVerifyIcon = new ImageIcon(imageURLVerifyIcon);
            imageChildIcon = new ImageIcon(imageURLChildIcon);
            imgApproveIcon = new ImageIcon(imageURLApproveIcon);
            
            imgPassIcon = new ImageIcon(imageURLPassIcon );
            imgRejectIcon = new ImageIcon(imageURLRejectIcon);
            
            
        }
        
        public Component getTreeCellRendererComponent(
        JTree tree,
        Object value,
        boolean selected,
        boolean expanded,
        boolean leaf,
        int row,
        boolean hasFocus) {
            
            
            
            
             super.getTreeCellRendererComponent(tree, value,selected, expanded, leaf, row, hasFocus);
            Object obj = ((ProposalApprovalMapBeanNode )value).getDataObject();

            
            if( obj instanceof ProposalApprovalMapBean  ){
                
                ProposalApprovalMapBean proposalApprovalMapBean = (ProposalApprovalMapBean) obj;
                //                QUERY_CONSTANTS = "T";
                //                PASSED_CONSTANTS = "P";
                //                APPROVE_CONSTANTS = "A";
                //                REJECTED_CONSTANTS = "R";
                
                
                
                setIconTextGap(7);
                setFont(CoeusFontFactory.getNormalFont());
                
                setIcon(imgVerifyIcon);
                if(row > 0) {
                    //Check with the status of the ProposalApprovalMapBean
                    if( proposalApprovalMapBean.getApprovalStatus().equalsIgnoreCase(TO_BE_SUBMITTED) ){
                        setIcon(imgVerifyIcon);
                    }else if( proposalApprovalMapBean.getApprovalStatus().equalsIgnoreCase(APPROVED) ){
                        setIcon(imgApproveIcon);
                    }else if( proposalApprovalMapBean.getApprovalStatus().equalsIgnoreCase(REJECTED) ){
                        setIcon(imgRejectIcon);
                    }else if( proposalApprovalMapBean.getApprovalStatus().equalsIgnoreCase("P") ){
                        setIcon(imageChildIcon);
                    }
                    //Commented the following used for checking with the vector
//                    if(isMatchingState(proposalApprovalMapBean.getProposalApprovals(), APPROVED))
//                        setIcon(imgApproveIcon);
//                    
//                    if(isMatchingState(proposalApprovalMapBean.getProposalApprovals(), BYPASSED))
//                        setIcon(imgApproveIcon);
//                    
//                    if(isMatchingState(proposalApprovalMapBean.getProposalApprovals(), TO_BE_SUBMITTED))
//                        setIcon(imgVerifyIcon);
//                    
//                    if(isMatchingState(proposalApprovalMapBean.getProposalApprovals(), WAITING_FOR_APPROVAL))
//                        setIcon(imageChildIcon);
//                    
//                    if(isMatchingState(proposalApprovalMapBean.getProposalApprovals(), REJECTED))
//                        setIcon(imgRejectIcon);
                }
                setText(proposalApprovalMapBean.getDescription());
            }
            
            setBackgroundNonSelectionColor((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            return this;
            
        }
    }
    
    
    // END===== Added by Ranjeev
    //public get
    
}
