/*
 * @(#)AreaOfResearchHierarchyForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeus.irb.gui;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import edu.mit.coeus.utils.tree.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.search.gui.*;


/**
 * The class used to represent main window for
 * Area of Research tree structure. This component help in selecting particular
 * Tree Node from the Tree Structure. This Window is Pluggable 
 * from any module of to depecit & Select Area Of Research Tree Nodes.
 *
 * @author  Subramanya
 * @version 1.0 September 23, 2002, 4:50 PM
 */
public class AreaOfResearchHierarchyForm extends JComponent
                                                implements ActionListener{
                                                
    //Holds the set of all TreeNodes.
    private Vector allAORNodeData = null;

    /*Holds the JTree Object with Drag/Drop properties Set.
     *private DnDJTree researchAreaTree = null;
     */
    private JTree researchAreaTree = null;
    
    //Holds the collection of Objects with AreaOfResearch Code / Description.
    private Hashtable selectedAORNode = null;
    
    //Holds the parent JDialog which intiated this component.
    private CoeusDlgWindow dlgCompDialog = null;

    //Research Area Code Root Node ID. Ex:0000001
    private static final String ROOT_NODE_ID = "000001";
        
    //Holds the Selected Hierarchy Tree Node 
    private HierarchyNode selectedTreeNode = null;
    
    // Holds the Tree 
    //private DnDJTree trAreaOfResearchLive = null;
    
    
    

    
     /** Creates new form AreaOfResearchHierarchyForm with Parent Dialog Object
      * where it needs to be populated.
      * @param parentDialog represent the Parent Dialog ( JDialog )
      * @throws Exception  obtaining the data from the database.
      */
    public AreaOfResearchHierarchyForm( CoeusDlgWindow parentDialog ) throws Exception {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dlgCompDialog = parentDialog;
        allAORNodeData = getAreaOfResearchTreeData( ROOT_NODE_ID );        
        initComponents();
        dlgCompDialog.setResizable(false);
        dlgCompDialog.setSize(560,400);
        Dimension dlgSize = dlgCompDialog.getSize();
        dlgCompDialog.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        screenSize = null;
        selectedAORNode = new Hashtable();
    }

    
    /** This method is called from within the constructor to
     * initialize the form.    
     */
    private void initComponents() {
        
        jcrPnAllResearchAreas = new javax.swing.JScrollPane();
        btnOk = new javax.swing.JButton();
        btnOk.addActionListener( this );
        
        btnCancel = new javax.swing.JButton();
        btnCancel.addActionListener( this );
        
        btnSearch = new javax.swing.JButton();
        btnSearch.addActionListener( this );
      
        TreeBuilder treeBuilder = new TreeBuilder( allAORNodeData ,
                                                   ROOT_NODE_ID );
        this.researchAreaTree  = new JTree( treeBuilder.getHierarchyRoot() );
        
        researchAreaTree.setRootVisible( false );
        researchAreaTree.setBackground( Color.lightGray );
        researchAreaTree.setFont( CoeusFontFactory.getNormalFont() );
        
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setBackgroundNonSelectionColor(Color.lightGray);
        researchAreaTree.expandRow(0);
         
        // adding listener to the tree multiple selction mode.
        researchAreaTree.getSelectionModel().setSelectionMode
                           ( TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION );
        
        ImageIcon treeNodeIcon = new ImageIcon( getClass().getClassLoader().
                                    getResource("images/close.gif") );
        // not available in jdk 1.3.1_01
        //researchAreaTree.setDragEnabled( false );
        
        renderer.setOpenIcon( treeNodeIcon );
        renderer.setClosedIcon( treeNodeIcon );
        renderer.setLeafIcon( treeNodeIcon );
        researchAreaTree.setCellRenderer( renderer );        
        
        dlgCompDialog.getContentPane().setLayout(new GridBagLayout());
        dlgCompDialog.setModal( true );
        dlgCompDialog.setTitle( "IRB Area Of Research - Select Node");
        dlgCompDialog.setSize( new Dimension( 350, 275 ) );
        dlgCompDialog.setResizable( false );        
        researchAreaTree.revalidate();        
        GridBagConstraints gridBagConstraints1;        
        jcrPnAllResearchAreas.setPreferredSize(new Dimension(300, 200));        
        
        jcrPnAllResearchAreas.setViewportView( researchAreaTree );
        jcrPnAllResearchAreas.revalidate();

        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.gridheight = 8;
        gridBagConstraints1.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints1.ipadx = 212;
        gridBagConstraints1.ipady = 200;
        gridBagConstraints1.insets = new Insets(2, 3, 2, 3);
        gridBagConstraints1.anchor = GridBagConstraints.CENTER;
        dlgCompDialog.getContentPane().add(
                                    jcrPnAllResearchAreas, gridBagConstraints1);

        btnOk.setText("OK");
        btnOk.setFont( CoeusFontFactory.getLabelFont() );
        btnOk.setMnemonic('O');
        
        btnOk.setMaximumSize(new Dimension(73, 26));
        btnOk.setMinimumSize(new Dimension(73, 26));
        btnOk.setPreferredSize(new Dimension(73, 26));
        
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.insets = new Insets(7, 7, 7, 7);
        dlgCompDialog.getContentPane().add(btnOk, gridBagConstraints1);

        btnCancel.setText("Cancel");
        btnCancel.setFont( CoeusFontFactory.getLabelFont() );
        btnCancel.setMnemonic( 'C' );
        
        btnCancel.setMaximumSize(new Dimension(73, 26));
        btnCancel.setMinimumSize(new Dimension(73, 26));
        btnCancel.setPreferredSize(new Dimension(73, 26));
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 2;
        gridBagConstraints1.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints1.insets = new Insets(7, 7, 7, 7);
        dlgCompDialog.getContentPane().add( btnCancel, gridBagConstraints1 );
        
        btnSearch.setText("Search");
        btnSearch.setFont(CoeusFontFactory.getLabelFont());
        btnSearch.setMnemonic('S');
        
        btnSearch.setMaximumSize(new Dimension(76,26));
        btnSearch.setMinimumSize(new Dimension(76,26));
        btnSearch.setPreferredSize(new Dimension(76,26));
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 3;
        gridBagConstraints1.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints1.insets = new Insets(7, 7, 7, 7);
        dlgCompDialog.getContentPane().add( btnSearch, gridBagConstraints1 );
        // Added by Chandra 12/9/2003
        java.awt.Component[] components = {researchAreaTree,btnOk,btnCancel,btnSearch};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        dlgCompDialog.getContentPane().setFocusTraversalPolicy(traversePolicy);
        dlgCompDialog.getContentPane().setFocusCycleRoot(true);
        // End Chandra
       
    }


    // Variables declaration - do not modify
    private javax.swing.JScrollPane jcrPnAllResearchAreas;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSearch;
  
    
    /** get all the form Tree data ( Area Of Research ) from the database.
     * This call makes a connection to the servlet ( /AreaOfResearchServlet )
     * construct RequesterBean and sends the request throught this. It fetches
     * Response from the servlet through Response Object.
     *
     * @return Vector          contains the collection of all Tree Node Data
     *                          Beans.
     * @param aorRootID represent the Root Node ID of the Tree.
     * @throws Exception  obtaining the data from the database
     */
    public Vector getAreaOfResearchTreeData( String aorRootID ) throws Exception{

        String connURL = CoeusGuiConstants.CONNECTION_URL +
                                                     "/AreaOfResearchServlet";
        Vector researchData  = new Vector(3,2);        
        RequesterBean request = new RequesterBean();
        request.setFunctionType('G');
        request.setId( aorRootID );
        request.setDataObject(request);

        AppletServletCommunicator comm = new AppletServletCommunicator(
                                                    connURL, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( response.isSuccessfulResponse() ){
            researchData = (Vector)response.getDataObject();
        }else{
            throw new Exception(response.getMessage());
        }
        return researchData;
    }
    
    public void requestDefaultFocusForComponent(){
        researchAreaTree.requestFocusInWindow();
    }
    /** Method Overriden to Handle Button Actions like OK/CANCEL.
     * @param  actionType      Type of Button Pressed.
     */
     public void actionPerformed( ActionEvent actionType ){
        
        Object actSource = actionType.getSource();
        if( actSource.equals( btnOk )){                       
            TreePath[] selectedNodes = researchAreaTree.getSelectionPaths();  
            if( selectedNodes != null && selectedNodes.length > 0 ){
                selectedAORNode = getSelectedNodes( selectedNodes );    
            }
            dlgCompDialog.dispose();   
        }else if(actSource.equals(btnSearch)){
            
            try{      
            
            CoeusSearch aorSearch = new CoeusSearch( dlgCompDialog, "AORSEARCH", 
                                                    CoeusSearch.TWO_TABS ) ;
            aorSearch.showSearchWindow();
            
            String aorCode = aorSearch.getSelectedValue()+" : ";
            if( aorCode == null ){                
               return;                 
            }
            
            researchAreaTree.expandPath( 
                       new TreePath(researchAreaTree.getModel().getRoot()));
           
            TreePath aorSelectedPath = findByName( researchAreaTree, 
                                                    aorCode );
            System.out.println("The Selected path is" + aorSelectedPath);
            researchAreaTree.expandPath( aorSelectedPath );
            researchAreaTree.setSelectionPath( aorSelectedPath );
            researchAreaTree.scrollRowToVisible(
                    researchAreaTree.getRowForPath(aorSelectedPath));            
            
            }catch( Exception err ){
                err.printStackTrace();
            }            
        }else if(actSource.equals(btnCancel)){
            
            
            dlgCompDialog.dispose(); 
        }
     
     }
     
    
     /**
      * This method gets All the selected Nodes from the Tree as a collection
      * Object containing NodeID( unique ) and Description ( String )
      * @return Hashtable   collection of NodeID/Description of tree Nodes 
      *                     selected.
      */
     public Hashtable getSelectedAORNodes(){         
         return selectedAORNode;         
     }
     
     
     /** This Method used to check whether the root Node is selected. This is
      * supporting method used in scenario of modification. Not to
      * allow the user to Modify the root node.
      * @return  True if Node is selected else False
      */
     public boolean isAORTreeNodeSelected(){
        
         boolean isSelected = false;         
         if( selectedAORNode != null && selectedAORNode.size() > 0 ){             
            isSelected = true;            
         }         
         return isSelected;         
     }
     
     // supporting method to get All the Tree Nodes from the specific
     // Tree Node path
     private Hashtable getSelectedNodes( TreePath[] selNodes ){
          
          TreePath selNodePath = null;  
          Object hrTreeNode = null;
          String nodeID = null;
          String nodeDescription = null;          
          
          for( int indx = 0; indx < selNodes.length ; indx ++ ){              
              selNodePath = selNodes[ indx ] ;              
              if( selNodePath != null ){                
                   selectedTreeNode = ( HierarchyNode )selNodePath.
                                                        getLastPathComponent();                   
                   hrTreeNode = selectedTreeNode.getUserObject();                 
                   nodeID =  (( ITreeNodeInfo )hrTreeNode ).getNodeID();
                   nodeDescription =  (( ITreeNodeInfo )hrTreeNode ).
                                                        getNodeDescription();  
                   
                   //check the Parent Nodes Already Exists
                   boolean isParentExists = checkParentNodeExists(selNodePath );                                 
                   
                   /*The Base Root ID can't be selected.
                   && ! nodeID.equalsIgnoreCase( 
                                                               ROOT_NODE_ID ) */
                   //Root Node Selection Allowed. Client Request Change Jan' 8 2003
                   if(  !isParentExists ){                       
                        selectedAORNode.put( nodeID, nodeDescription );                        
                   }
              }              
          }          
          return selectedAORNode ;          
     }
          
     //to check the parent Node already exists in the tree node path.     
     private boolean checkParentNodeExists( TreePath selPath ){
         
         boolean isParentExists = false;
         String prNode = null;
         Object parentTreeNode = null;         
         for( int indx = 1; indx < selPath.getPathCount(); indx++ ){         
                parentTreeNode = (( HierarchyNode )selPath.
                                        getPathComponent(indx)).getUserObject();                
                prNode =  (( ITreeNodeInfo )parentTreeNode ).getNodeID();                
                if( selectedAORNode.containsKey( prNode ) ){                    
                    isParentExists = true;
                    break;
                }
         }         
         return isParentExists;         
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

            Object o = node;
            if (node.getChildCount() >= 0) {                 
                for (Enumeration e=node.children(); e.hasMoreElements(); ) {                    
                    TreeNode n = (TreeNode)e.nextElement();
                    TreePath path = parent.pathByAddingChild(n);                    
                    TreePath result = find2(tree, path, nodeName);
                    if(result!=null)
                        return result;
                    // Found a match
                }
            }
        }        
        // No match at this branch
        return null;
    }     

}