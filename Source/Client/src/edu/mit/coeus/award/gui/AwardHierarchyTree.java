/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.award.gui;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.text.*;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.exception.CoeusClientException;

/**
 * AwardHierarchyTree.java
 * Created on March 11, 2004, 9:24 AM
 * @author  Vyjayanthi
 */
public class AwardHierarchyTree extends javax.swing.JComponent {
    
    /** Holds the selected node's mit award number and account number if it exists */
    private String selectedMitAwardNumber;
    
    /** Holds an instance of the <CODE>AwardTreeNodeRenderer</CODE>*/
    private AwardTreeNodeRenderer awardTreeNodeRenderer;
    
    /** Holds an instance of the TreeSelectionListener */
    private TreeSelectionListener treeSelectionListener;
    
    /** Holds the data to build the Award Hierarchy tree */
    private CoeusVector cvAwardHierarchy = new CoeusVector();
    
    /** Holds all the child award numbers for the selected award number */
    private CoeusVector cvDescendents = new CoeusVector();
    
    /** Holds an instance of <CODE>AwardHierarchyDataMediator</CODE> */
    private AwardHierarchyDataMediator awardHierarchyDataMediator;
    
    /** Holds boolean data if user has rights to Create/Modify/View awards
     * first element will be true if user has right to create award, false otherwise
     * second element will be true if user has right to modify award, false otherwise
     * third element will be true if user has right to view award, false otherwise
     */
    private CoeusVector cvUserRights = new CoeusVector();
    
    /** Holds an instance of Award Hierarchy tree model */
    private AwardHierarchyTreeModel awardHierarchyTreeModel;
    
    private static final int ACTIVE_STATUS_CODE = 1;
    private static final int PENDING_STATUS_CODE = 3;
    private static final int HOLD_STATUS_CODE = 6;
    
    private static final char GET_AWARD_HIERARCHY = 'A';
        
    private static final String SEMI_COLON = " : ";
    private static final String EMPTY_STRING = "";
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
        "/AwardMaintenanceServlet";
    
    private static final String ROOT_AWARD_NUMBER = "000000-000";
    public static final String MIT_AWARD_NUMBER_FIELD = "mitAwardNumber";
    public static final String PARENT_MIT_AWARD_NUMBER_FIELD = "parentMitAwardNumber";
    
    private static final java.awt.Color TABLE_SELECTION_COLOR = 
        (java.awt.Color) javax.swing.UIManager.getDefaults().get("Table.selectionBackground");
    private static final java.awt.Color PANEL_BACKGROUND_COLOR = 
        (Color) UIManager.getDefaults().get("Panel.background");
        
    
    /** Creates new form AwardHierarchyTree */
    public AwardHierarchyTree(){
        initComponents();
        awardHierarchyDataMediator = new AwardHierarchyDataMediator();
        awardTreeNodeRenderer = new AwardTreeNodeRenderer();
        registerComponents();
    }

    /** This method is used to set the listeners to the components. */
    private void registerComponents(){
        treeAwardHierarchy.setCellRenderer(awardTreeNodeRenderer);
        treeAwardHierarchy.getSelectionModel().setSelectionMode(
            TreeSelectionModel.SINGLE_TREE_SELECTION);
    }
    
    /** Method to get the award hierarchy data from the database */
    private CoeusVector getFormData() throws CoeusClientException{
        CoeusVector cvData = new CoeusVector();
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(GET_AWARD_HIERARCHY);
        requester.setDataObject(selectedMitAwardNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            Vector vecData = response.getDataObjects();
            cvData = (CoeusVector)vecData.get(0);
            cvUserRights.addElement(vecData.get(1));
            cvUserRights.addElement(vecData.get(2));
            cvUserRights.addElement(vecData.get(3));
            return cvData;
        }else {
            throw new CoeusClientException(response.getMessage(),CoeusClientException.ERROR_MESSAGE);
        }
    }
    
    /** Method to set the award hierarchy
     */
    private void setFormData(boolean dataAvailable){
        try{
            
            if( !dataAvailable ){
                cvAwardHierarchy = getFormData();
                awardHierarchyDataMediator.setHierarchyData(cvAwardHierarchy);
            }
            AwardHierarchyNode root = new AwardHierarchyNode((AwardHierarchyBean)cvAwardHierarchy.get(0));
            
            awardHierarchyTreeModel = new AwardHierarchyTreeModel(root);
            awardHierarchyTreeModel.setData(awardHierarchyDataMediator);
            treeAwardHierarchy.setModel(awardHierarchyTreeModel);
            setSelectedObject(selectedMitAwardNumber);
            
            treeAwardHierarchy.setShowsRootHandles(true);
            
            /*addNode(treeRoot, (AwardHierarchyBean)cvAwardHierarchy.get(0));
            (( DefaultTreeModel )treeAwardHierarchy.getModel() ).reload();
            */
            //expandAll(treeAwardHierarchy, true);
        }catch(CoeusClientException coeusClientException){
            CoeusOptionPane.showDialog(coeusClientException);
            coeusClientException.printStackTrace();
        }
    }

    /** Method to set the tree data by getting data from the database
     * @param selectedMitAwardNumber the selected mit award number
     */    
    public void construct(String selectedMitAwardNumber) {
        this.selectedMitAwardNumber = selectedMitAwardNumber;
        treeAwardHierarchy.addTreeSelectionListener(treeSelectionListener);
        setFormData(false);
    }
    
    
    /** To refresh the tree data in hierararchy after awards are copied as Child Awards
     * @param mitAwardNumber
     * @param cvAwardHierarchy
     * @param awardReload
     */
    public void construct(String selectedMitAwardNumber, CoeusVector cvAwardHierarchy, AwardHierarchyBean awardReload) {
        this.selectedMitAwardNumber = selectedMitAwardNumber;
        this.cvAwardHierarchy = cvAwardHierarchy;
        awardHierarchyDataMediator.setHierarchyData(cvAwardHierarchy);
        
        if(awardReload != null) {
            //Update the child count and children of the bean in the mediator 
            //when award is copied as Child
            awardHierarchyDataMediator.forceReload(awardReload);
        }
        
        if( treeAwardHierarchy.getTreeSelectionListeners() == null || 
        treeAwardHierarchy.getTreeSelectionListeners().length <= 0 ){
            treeAwardHierarchy.addTreeSelectionListener(treeSelectionListener);
        }
        setFormData(true);
    }
    
    /** Method to set the tree data passing the data values
     * @param mitAwardNumber
     * @param cvAwardHierarchy
     */
    public void construct(String selectedMitAwardNumber, CoeusVector cvAwardHierarchy) {
        construct(selectedMitAwardNumber, cvAwardHierarchy, null);
    }
    
    /** To get all the descendents for a given award
     * @return cvDescendents holds all the descendent award numbers for the parent
     * @param parent takes the AwardHierarchyBean
     */
    public CoeusVector getDescendents(AwardHierarchyBean parent) {
        cvDescendents.addElement(parent);
        AwardHierarchyBean parentBean = (AwardHierarchyBean)parent;
        Equals eqParentMitAwardNumber = new Equals(
            PARENT_MIT_AWARD_NUMBER_FIELD, parentBean.getMitAwardNumber());
        CoeusVector cvChildData = cvAwardHierarchy.filter(eqParentMitAwardNumber);
        cvChildData.sort(MIT_AWARD_NUMBER_FIELD, true);
        if( cvChildData != null && cvChildData.size() > 0 ) {
            for( int index = 0; index < cvChildData.size(); index++ ) {
                AwardHierarchyBean childAwardHierarchyBean = 
                    (AwardHierarchyBean)cvChildData.get(index);
                getDescendents(childAwardHierarchyBean);
            }
        }
        return cvDescendents;
    }
    
    /** Method to reset the cvDescendents vector */
    public void resetDescendents(){
        cvDescendents = new CoeusVector();
    }
    
    /** Method to construct the tree by adding child nodes to the root node of the tree
     * @param parent holds the parent node
     * @param awardHierarchyBean holds the data bean
     */
    /*private void addNode(DefaultMutableTreeNode parent, AwardHierarchyBean awardHierarchyBean ){
        AwardHierarchyNode childAwardHierarchyNode = new AwardHierarchyNode(awardHierarchyBean);
        parent.add(childAwardHierarchyNode);
        
        Equals eqParentMitAwardNumber = new Equals(
            PARENT_MIT_AWARD_NUMBER_FIELD, awardHierarchyBean.getMitAwardNumber());
        CoeusVector cvChildren = cvAwardHierarchy.filter(eqParentMitAwardNumber);
        cvChildren.sort(MIT_AWARD_NUMBER_FIELD, true);
        if( cvChildren != null && cvChildren.size() > 0 ) {
            for( int index = 0; index < cvChildren.size(); index++ ) {
                AwardHierarchyBean childAwardHierarchyBean = 
                    (AwardHierarchyBean)cvChildren.get(index);
                addNode(childAwardHierarchyNode, childAwardHierarchyBean);
            }
        }
    }

    /**
     * Method used to expand/ collapse all the nodes in the tree.
     * @param tree JTree whose nodes are to be expanded/ collapsed.
     * @param expand  boolean true to expand all nodes, false to collapse.
     */
    /*
    public void expandAll(JTree tree, boolean expand) {
        TreeNode root = (TreeNode)tree.getModel().getRoot();
        // Traverse tree from root
        expandAll(tree, new TreePath(root), expand);
    }
     */
    
    /**
     * @param tree
     * @param parent
     * @param expand
     */  
    /*
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
     */
    
    /** Recursive method to select the tree node based on the mit award number
     * @param node DefaultMutableTreeNode
     */
    /*
    private void selectNode(DefaultMutableTreeNode node) {
        
        if( node != null ) {
            for(int index =0 ;index < cvAwardHierarchy.size(); index++) {                
                AwardHierarchyBean awardHierarchyBean = (AwardHierarchyBean )cvAwardHierarchy.get(index);
                if(awardHierarchyBean.getMitAwardNumber().equals(getSelectedMitAwardNumber()) ){
                    treeAwardHierarchy.setSelectionPath(new TreePath(node.getPath()));
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
     */
    
    /** Getter for property selectedMitAwardNumber.
     * @return Value of property selectedMitAwardNumber.
     *
     */
    public java.lang.String getSelectedMitAwardNumber() {
        return selectedMitAwardNumber;
    }    
    
    /** Setter for property selectedMitAwardNumber.
     * @param selectedMitAwardNumber New value of property selectedMitAwardNumber.
     *
     */
    public void setSelectedMitAwardNumber(java.lang.String selectedMitAwardNumber) {
        this.selectedMitAwardNumber = selectedMitAwardNumber;
    }
    
    /** To get the selected bean in the tree hierarchy
     * @return selectedBean if any tree node is selected, null otherwise
     */
    public AwardHierarchyBean getSelectedObject() {
        TreePath path = treeAwardHierarchy.getSelectionPath();
        if( path!= null ) {
            AwardHierarchyBean selectedBean = (AwardHierarchyBean)path.getLastPathComponent();
            return selectedBean;
        }
        return null;
    }
    
    /** To select the node in the tree hierarchy corresponding to the mitAwardNumber
     * @param mitAwardNumber 
     */
    public void setSelectedObject(String mitAwardNumber) {
        TreePath path;
        Equals eqAwdNo = new Equals(MIT_AWARD_NUMBER_FIELD, mitAwardNumber);
        CoeusVector cvAwd = cvAwardHierarchy.filter(eqAwdNo);
        AwardHierarchyBean awardHierarchyBean;
        if(cvAwd != null && cvAwd.size() > 0) {
            //cvAwd contains only one element
            awardHierarchyBean = (AwardHierarchyBean)cvAwd.get(0);
        }else {
            return ;
        }
        
        Vector vecPath = new Vector();
        
        //Add the bean corresponding to the mitAwardNumber
        vecPath.add(awardHierarchyBean);
        
        AwardHierarchyBean parentAwardHierarchyBean = null;
        Equals eqParentAwdNo = new Equals(MIT_AWARD_NUMBER_FIELD, awardHierarchyBean.getParentMitAwardNumber());
        cvAwd = cvAwardHierarchy.filter(eqParentAwdNo);
        if(cvAwd != null && cvAwd.size() > 0) {
            
            //Get the parent of the obtained awardHierarchyBean
            parentAwardHierarchyBean = (AwardHierarchyBean)cvAwd.get(0);
            
            //Add the bean's parentAwardHierarchyBean
            vecPath.add(parentAwardHierarchyBean);
        }
        
        if(parentAwardHierarchyBean != null) {
            while(! parentAwardHierarchyBean.getParentMitAwardNumber().equals(ROOT_AWARD_NUMBER)){
                /* Add all the beans from the bean to be selected to its parent
                 * to build the tree path
                 */
                eqParentAwdNo = new Equals(MIT_AWARD_NUMBER_FIELD, parentAwardHierarchyBean.getParentMitAwardNumber());
                cvAwd = cvAwardHierarchy.filter(eqParentAwdNo);
                parentAwardHierarchyBean = (AwardHierarchyBean)cvAwd.get(0);
                vecPath.add(parentAwardHierarchyBean);
            }
        }
        
        int size = vecPath.size();
        Object pathArr[] = new Object[size];
        int index, pos;
        for(index = 0, pos = size - 1; index < size; index++, pos--) {
            /* Add the elements of the vector vecPath to the pathArr in reverse order
             * to build the tree path from the root to the node to be selected
             */
            pathArr[index] = vecPath.get(pos);
        }
        
        //Construct the treepath passing the pathArr
        path = new TreePath(pathArr);
        
        //Expand the treepath
        treeAwardHierarchy.expandPath(path);

        //Set the above constructed path for the node to be selected
        treeAwardHierarchy.addSelectionPath(path);
    }
    
    /** Getter for property treeSelectionListener.
     * @return Value of property treeSelectionListener.
     *
     */
    public javax.swing.event.TreeSelectionListener getTreeSelectionListener() {
        return treeSelectionListener;
    }
    
    /** Setter for property treeSelectionListener.
     * @param treeSelectionListener New value of property treeSelectionListener.
     *
     */
    public void setTreeSelectionListener(javax.swing.event.TreeSelectionListener treeSelectionListener) {
        this.treeSelectionListener = treeSelectionListener;
    }
    
    /** Getter for property cvUserRights.
     * @return Value of property cvUserRights.
     *
     */
    public edu.mit.coeus.utils.CoeusVector getUserRights() {
        return cvUserRights;
    }
    
    /** Getter for property cvAwardHierarchy.
     * @return Value of property cvAwardHierarchy.
     *
     */
    public edu.mit.coeus.utils.CoeusVector getAwardHierarchyData() {
        return cvAwardHierarchy;
    }    
    
    /** Setter for property cvAwardHierarchy.
     * Also sets the data vector to the tree model
     * @param cvAwardHierarchy New value of property cvAwardHierarchy.
     *
     */
    public void setAwardHierarchyData(edu.mit.coeus.utils.CoeusVector cvAwardHierarchy) {
        this.cvAwardHierarchy = cvAwardHierarchy;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        scrPnAwardHierarchyTree = new javax.swing.JScrollPane();
        treeAwardHierarchy = new javax.swing.JTree();

        setLayout(new java.awt.BorderLayout());

        scrPnAwardHierarchyTree.setBorder(null);
        treeAwardHierarchy.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        treeAwardHierarchy.setFont(CoeusFontFactory.getNormalFont());
        scrPnAwardHierarchyTree.setViewportView(treeAwardHierarchy);

        add(scrPnAwardHierarchyTree, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JScrollPane scrPnAwardHierarchyTree;
    public javax.swing.JTree treeAwardHierarchy;
    // End of variables declaration//GEN-END:variables
    
    /** Inner Class AwardTreeNodeRenderer - Start
     */
    class AwardTreeNodeRenderer extends DefaultTreeCellRenderer{
        
        /** Holds the image icons
         * imgActive displays a green flag indicating Active status( status code = 1 )
         * imgPending displays a blue flag indicating Hold status( status code = 6 )
         * imgHold displays a yellow flag indicating Pending status( status code = 3 )
         * imgOthers displays a red flag inicating Closed/Terminated/Inactive status
         * (status code other than 1, 3 and 6)
         */
        ImageIcon imgActive, imgPending, imgHold, imgOthers;
        
        /** Creates new AwardTreeNodeRenderer */
        AwardTreeNodeRenderer(){
            
            java.net.URL imageURLActive = getClass().getClassLoader().getResource( CoeusGuiConstants.ACTIVE_AWARD_ICON);
            java.net.URL imageURLPending = getClass().getClassLoader().getResource( CoeusGuiConstants.PENDING_AWARD_ICON);
            java.net.URL imageURLHold = getClass().getClassLoader().getResource( CoeusGuiConstants.HOLD_AWARD_ICON);
            java.net.URL imageURLOthers = getClass().getClassLoader().getResource( CoeusGuiConstants.OTHER_AWARD_ICON);
            
            imgActive = new ImageIcon(imageURLActive);
            imgPending = new ImageIcon(imageURLPending);
            imgHold = new ImageIcon(imageURLHold);
            imgOthers = new ImageIcon(imageURLOthers);
            
        }
        
        public Component getTreeCellRendererComponent(JTree tree, Object value,
        boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

            setBackgroundNonSelectionColor(PANEL_BACKGROUND_COLOR);
            setBackgroundSelectionColor(TABLE_SELECTION_COLOR);

            if( selected ) {
                setForeground(Color.white);
                setBorderSelectionColor(TABLE_SELECTION_COLOR);
            }else{
                setForeground(Color.black);                
            }

            if( value instanceof AwardHierarchyBean ){
                
                AwardHierarchyBean awardHierarchyBean = (AwardHierarchyBean)value;
                
                //Set the gap between the icon and text
                setIconTextGap(6);
                
                if( awardHierarchyBean.getStatusCode() == ACTIVE_STATUS_CODE ){
                    setIcon(imgActive);
                }else if( awardHierarchyBean.getStatusCode() == PENDING_STATUS_CODE ){
                    setIcon(imgPending);
                }else if( awardHierarchyBean.getStatusCode() == HOLD_STATUS_CODE ){
                    setIcon(imgHold);
                }else{
                    setIcon(imgOthers);
                }
                setText(awardHierarchyBean.getMitAwardNumber() + SEMI_COLON + 
                    (awardHierarchyBean.getAccountNumber() == null || 
                    awardHierarchyBean.getAccountNumber().trim().equals(EMPTY_STRING) ?
                    EMPTY_STRING : awardHierarchyBean.getAccountNumber() ));
            }
            return this;
        }
    }
    //Inner Class AwardTreeNodeRenderer - End
}//End Class Award Hierarchy


