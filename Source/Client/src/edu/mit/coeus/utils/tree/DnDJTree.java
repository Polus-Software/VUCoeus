/*
 * @(#)DnDJTree.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeus.utils.tree;

import edu.mit.coeus.bean.AuthorizationBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.unit.bean.UnitHierarchyFormBean;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.io.*; 
import java.util.Hashtable;
import java.util.Enumeration;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.query.AuthorizationOperator;

/**
 * The class used to represent resuable component Tree with Drag and Drag Option.
 * This class extends JTree to inhert the features of Tree compoenent like
 * selection, adding model the tree sturcture, modifying the tree node, finding
 * tree path. Tree Node are constructed with hierarchy Tree Node class Object.
 * ( this is extension of DefaultMutableTreeNode ). It supports some of
 * features like find the Tree Path of a specific node, obtaining the modified
 * node after the tree constructed and newly created Tree Node Information.
 *
 * @author  Geo Thomas
 * @updated Subramnaya
 * @version 1.0 September 23, 2002, 10:50 AM
 */
public class DnDJTree extends JTree implements
                    TreeSelectionListener, DragGestureListener,
                    DropTargetListener, DragSourceListener, Autoscroll{

    //holds the modified nodes. key as id and data bean as value
    private Hashtable modifiedNodes;

    //holds the newly added nodes. key as Node id and data bean as Node value
    private Hashtable newNodes;

    /** Stores the selected node info
     */
    protected TreePath selectedTreePath = null;

    /**holds current Selected Tree Node Object(Default Mutable Tree Node )
     */
    protected HierarchyNode selectedNode = null;

    // Variables needed for DnD
    private DragSource dragSource = null;

    //hold the Drag source context
    private DragSourceContext dragSourceContext = null;

    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    /** Constructors the DnDJTree Instance with DefaultMutableTreeNode/Hierarchy
     * Tree Node as its Root.
     * @param root The root node of the tree
     */
    public DnDJTree( HierarchyNode root ) {

        super(root);// Root Node represeting the completing Tree.
        addTreeSelectionListener(this);
        dragSource = DragSource.getDefaultDragSource() ;
        DragGestureRecognizer dgr =
        dragSource.createDefaultDragGestureRecognizer(
        this,                             //DragSource
        DnDConstants.ACTION_MOVE, //specifies valid actions
        this                              //DragGestureListener
        );

       /* Eliminates right mouse clicks as valid actions - useful especially
        * if you implement a JPopupMenu for the JTree
        */
        dgr.setSourceActions(dgr.getSourceActions() & ~InputEvent.BUTTON3_MASK);

        /* First argument:  Component to associate the target with
        * Second argument: DropTargetListener
        */
        DropTarget dropTarget = new DropTarget(this, this);
        this.setFont( CoeusFontFactory.getNormalFont() );
        modifiedNodes = new Hashtable();
        newNodes = new Hashtable();
        this.selected = false;
        coeusMessageResources = CoeusMessageResources.getInstance();  
    }


    /**
     * This method is used to get the Resepctive Selected Node (HierarchyNode)
     * from the DnDJTree Instance.
     * @return HierarchyTreeNode DefaultMutableTreeNode instance of specific
     * selected tree node.
     */
    public HierarchyNode getSelectedNode() {
        return selectedNode;
    }


    /**
     * This method is used to Clear the Cache of Programattically maintained
     * stack of modified node registry.
     */
    public void clearAllModifiedNodes() {
        modifiedNodes.clear();
    }

    /**
     * This method is used to Clear the Cache of Programattically maintained
     * stack of new node registry.
     */
    public void clearAllNewNodes() {
        newNodes.clear();
    }


    /**
     * This Method is fired when the dragGesture is changed/recognized.
     * @param e represent the DragGestureEvent instance.
     */
    public void dragGestureRecognized(DragGestureEvent e) {

        //Get the selected node
        HierarchyNode dragNode = getSelectedNode();
            if (dragNode != null && ( this.getCursor().getType() !=
                                                       Cursor.HAND_CURSOR )  ) {

            //Get the Transferable Object
            Transferable transferable = (Transferable) dragNode;

            //Select the appropriate cursor;
            Cursor cursor = DragSource.DefaultCopyNoDrop;
            int action = e.getDragAction();
            if (action == DnDConstants.ACTION_MOVE){
                cursor = new Cursor(Cursor.HAND_CURSOR);
            }

            /*In fact the cursor is set to NoDrop because once an action is rejected
             * by a dropTarget, the dragSourceListener are no more invoked.
             * Setting the cursor to no drop by default is so more logical, because
             * when the drop is accepted by a component, then the cursor is changed by the
             * dropActionChanged of the default DragSource.
             */

            selected = true;
            //begin the drag
            dragSource.startDrag(e, cursor, transferable, this);

        }
    }


    /**
     * This method is invoked for drag drop end.
     * @param dsde DragSourceDropEvent instance
     */
    public void dragDropEnd(DragSourceDropEvent dsde) {
    }

    /**
     * This method is invoked/fired when drag operation is intiated.
     * @param dsde DragSourceDropEvent instance
     */
    public void dragEnter(DragSourceDragEvent dsde) {
    }


    /**
     * This method is invoked/fired when drag Over operation is intiated.
     * @param dsde DragSourceDropEvent instance
     */
    public void dragOver(DragSourceDragEvent dsde) {
    }


    /**
     * This method is invoked/fired when drop operation is intiated.
     * @param dsde DragSourceDropEvent instance
     */
    public void dropActionChanged(DragSourceDragEvent dsde) {
    }


    /**
     * This method is invoked/fired when drag operation is stopped/exited.
     * @param dsde DragSourceDropEvent instance
     */
    public void dragExit(DragSourceEvent dsde) {
    }


    String message;
    boolean accepted;

    /**
     * This method is invoked/fired durin drop operation.
     * @param e DropTargetDropEvent instance
     */

    public void drop(DropTargetDropEvent e) {
        try {

            Transferable tr = e.getTransferable();
            HierarchyNode childInfo = (HierarchyNode)tr.getTransferData(
                                                    HierarchyNode.INFO_FLAVOR );
            //get new parent node
            Point loc = e.getLocation();
            TreePath destinationPath = getPathForLocation(loc.x, loc.y);
            final String msg = testDropTarget(destinationPath, selectedTreePath);
            ITreeNodeInfo selectedNodeData = null;
            if(selectedTreePath!=null && selectedTreePath.getLastPathComponent()!=null){
                selectedNodeData = ( ITreeNodeInfo )(
                                (DefaultMutableTreeNode)
                                selectedTreePath.getLastPathComponent()).
                                getUserObject();
            }

            if (msg != null) {

                e.rejectDrop();
                int answer = -1;
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        CoeusOptionPane.showErrorDialog(msg);
                        setSelectionPath( selectedTreePath );
                        //insted of HierarchPanel its Null.
                    }
                });

                selected = false;
                return;
            }
            
            HierarchyNode newParent = (HierarchyNode)
                                        destinationPath.getLastPathComponent();
            //get old parent node
            HierarchyNode oldParent = ( HierarchyNode )
                                                getSelectedNode().getParent();
            //Added with case 3587: Multicampus enhancements
            //Check for rights in the source,destination and selected units
            if(selectedNodeData instanceof UnitHierarchyFormBean){
                ITreeNodeInfo tmpNewParentNode  = ( ITreeNodeInfo )newParent.
                                                                getUserObject();
                ITreeNodeInfo tmpOldParentNode  = ( ITreeNodeInfo )oldParent.
                                                                getUserObject();
                checkForRights(tmpNewParentNode,tmpOldParentNode,selectedNodeData);
            }
            //3587 End
            message = "Do you want to move "+ selectedNode.toString()+
                " to "+ newParent.getUserObject().toString();
            e.acceptDrop(DnDConstants.ACTION_MOVE);
            int action = e.getDropAction();
            boolean copyAction = (action == DnDConstants.ACTION_COPY);

            //make new child node
            HierarchyNode newChild = childInfo;
            if (!copyAction && oldParent != null )
                oldParent.remove(getSelectedNode());
            if( newParent!=null)  newParent.add(newChild);
            e.getDropTargetContext().dropComplete(true);
            selected = false;

            //expand nodes appropriately - this probably isnt the best way...
            DefaultTreeModel model = (DefaultTreeModel) getModel();
            //model.reload();
            model.reload(oldParent);
            model.reload(newParent);
            //Moved Node Selection Update. Subramanya
            TreePath currentNodePath = new TreePath(
                                          newParent.getLastLeaf().getPath() );
            expandPath( currentNodePath );
            setSelectionPath( currentNodePath );

            ITreeNodeInfo newTmpParentNode  = ( ITreeNodeInfo )newParent.
                                                                getUserObject();
            ITreeNodeInfo oldTmpParentNode  = ( ITreeNodeInfo )oldParent.
                                                                getUserObject();
            ITreeNodeInfo newParentNode = getModifiedNode( newTmpParentNode );
            ITreeNodeInfo oldParentNode = getModifiedNode( oldTmpParentNode );

            newParentNode.setChildrenFlag("Y");
            selectedNodeData.setParentNodeID( newParentNode.getNodeID() );
            if( oldParent.isLeaf() ){
                oldParentNode.setChildrenFlag("N");
            }
            //addModifiedNode holds 3 nodes for specific node movement.
            addModifiedNode( newParentNode );
            addModifiedNode( oldParentNode );
            addModifiedNode( selectedNodeData );
        }catch (java.lang.IllegalStateException ils) {
                ils.printStackTrace();
                e.rejectDrop();
                displayMsg(ils.getMessage());
        }catch (IOException io) {
            io.printStackTrace();
            e.rejectDrop();
            displayMsg(io.getMessage());
        }catch (UnsupportedFlavorException ufe) {
            ufe.printStackTrace();
            e.rejectDrop();
            displayMsg(ufe.getMessage());
        }catch (Exception ex) {
            ex.printStackTrace();
            e.rejectDrop();
            displayMsg(ex.getMessage());
        }
    } //end of method


    /**
     * This method is invoked/fired during drag operation is intiated.
     * @param e DropTargetDragEvent instance
     */
    public void dragEnter(DropTargetDragEvent e) {
    }


    /**
     * This method is invoked/fired when drag operation is intiated.
     * @param dsde DropTargetEvent instance
     */
    public void dragExit(DropTargetEvent dsde ) {
    }


    private static boolean selected;

    /**
     * This method is invoked/fired when drag Over operation is intiated.
     * @param dsde DropTargetEvent instance
     */
    public void dragOver(DropTargetDragEvent dsde ) {

        //set cursor location. Needed in setCursor method
        Point cursorLocationBis = dsde.getLocation();
        TreePath destinationPath =
            getPathForLocation(cursorLocationBis.x, cursorLocationBis.y);
        this.setSelectionPath(destinationPath);
    }


    /**
     * This method is invoked/fired when drop operation is intiated.
     * @param dsde DropTargetEvent instance
     */
    public void dropActionChanged(DropTargetDragEvent dsde ) {
    }


    /**
     * This method is invoked/fired when when do selection on the JTree.
     * @param evt TreeSelectionEvent instance
     */
    public void valueChanged(TreeSelectionEvent evt) {

        if(!selected){
            selectedTreePath = evt.getNewLeadSelectionPath();
            if (selectedTreePath == null) {
                selectedNode = null;
                return;
            }

            selectedNode =(HierarchyNode)selectedTreePath.getLastPathComponent();

        }
    }


    /** Convenience method to test whether drop location is valid
     * @param destination The destination path
     * @param dropper The path for the node to be dropped
     * @return null if no problems, otherwise an explanation
     */
    private String testDropTarget(TreePath destination, TreePath dropper) {
        //Typical Tests for dropping

        //Test 1.
        boolean destinationPathIsNull = destination == null;
        if (destinationPathIsNull){
            //return "Invalid drop location.";
            return coeusMessageResources.parseMessageKey(
                                        "DnDJTree_exceptionCode.1201");
        }

        //Test 2.
        HierarchyNode node = (HierarchyNode) destination.getLastPathComponent();
        if ( !node.getAllowsChildren() )            

            return coeusMessageResources.parseMessageKey(
                                        "DnDJTree_exceptionCode.1202");
            
        if (destination.equals(dropper))

            return coeusMessageResources.parseMessageKey(
                                        "DnDJTree_exceptionCode.1203");
        //Test 3.
        if ( dropper.isDescendant(destination))

            return coeusMessageResources.parseMessageKey(
                                        "DnDJTree_exceptionCode.1204");

        //Test 4.
        if ( dropper.getParentPath().equals(destination))

            return coeusMessageResources.parseMessageKey(
                                        "DnDJTree_exceptionCode.1205");
        return null;
    }



    /**
     *  Adding unit hierarchy form bean instance to the hashtable
     *  @param resNode ITreeNodeInfo instance
     */

    public void addModifiedNode( ITreeNodeInfo resNode ){
        String resID = resNode.getNodeID();
        if(newNodes.get(resID)!=null){
            addNewNode(resNode);
            return;
        }
        if(this.modifiedNodes.get( resID )==null){
            this.modifiedNodes.put( resID, resNode );
        }else{
            this.modifiedNodes.remove( resID );
            this.modifiedNodes.put( resID, resNode );
        }
    }

    /**
     * This method is invoked to get all the modified form this tree structre.
     * it is usually invoked after a set of operation performed on the tree.
     * @param newAORNode ITreeNodeInfo instance.
     * @return ITreeNodeInfo Interface to containing the TreeNode DataBean
     * instance.
     */
    public ITreeNodeInfo getModifiedNode( ITreeNodeInfo newAORNode ){
        ITreeNodeInfo modifiedNode = ( ITreeNodeInfo )modifiedNodes.get(
                                            newAORNode.getNodeID());
        return ( modifiedNode == null ? newAORNode : modifiedNode );
    }


    /**
     * This method returns the collection of all modified nodes.
     * Collection instance contain key (node id ) and data bean instance as
     * it value object.
     * @return Hashtable collection instance.
     */
    public Hashtable getModifiedNodes(){
        return this.modifiedNodes;
    }


    /**
     *  Adding unit hierarchy form bean instance to the hashtable
     *  @param resNode ITreeNodeInfo instance
     */
    public void addNewNode( ITreeNodeInfo resNode ){
        String resID = resNode.getNodeID();
        if(this.newNodes.get( resID )==null){
            this.newNodes.put( resID, resNode );
        }else{
            this.newNodes.remove( resID );
            this.newNodes.put( resID, resNode );
        }
    }

    /**
     * This method is used to return the New instance of Sepcific Tree Node bean.
     * @param node ITreeNodeInfo instance
     * @return ITreeNodeInfo from the collection of newly added nodes.
     */
    public ITreeNodeInfo getNewNode( ITreeNodeInfo node ){
        ITreeNodeInfo newNode = ( ITreeNodeInfo )newNodes.get(
                                            node.getNodeID());
        return ( newNode == null ? node : newNode );
    }

    /**
     * This method is used to get the newly added tree node data beans.
     * @return Hashtable collection instance.
     */
    public Hashtable getNewNodes(){
        return this.newNodes;
    }


    /* displays the message,it gives the error message.
     * @param mesg String
     */
    private void displayMsg( String mesg ) {
        CoeusOptionPane.showErrorDialog(mesg);
    }

    /** Finds the path in tree as specified by the array of names.
     * The names array is a sequence of names where names[0]
     * is the root and names[i] is a child of names[i-1].
     * Comparison is done using String.equals().
     * Returns null if not found.
     * @param name Node name
     * @return TreePath path of the respective tree Node.
     */
    public TreePath findByName(String name) {
        TreeNode root = (TreeNode)this.getModel().getRoot();
        TreePath result = find2(this, new TreePath(root), name);
        return result;
    }

    //supproting method to find the tree path (recursively used)
    private TreePath find2(DnDJTree tree, TreePath parent, String nodeName) {
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

    // Autoscroll Interface...

	private static final int AUTOSCROLL_MARGIN = 12;
	// Ok, we’ve been told to scroll because the mouse cursor is in our
	// scroll zone.


        /**
         * This method is used to perform autoscrolling with in the view port
         * of the tree.
         * @param pt Point instance on the Window
         */
	public void autoscroll(Point pt)
	{
		// Figure out which row we’re on.
		int nRow = getRowForLocation(pt.x, pt.y);

		// If we are not on a row then ignore this autoscroll request
		if (nRow < 0)
			return;

		Rectangle raOuter = getBounds();
		// Now decide if the row is at the top of the screen or at the
		// bottom. We do this to make the previous row (or the next
		// row) visible as appropriate. If we’re at the absolute top or
		// bottom, just return the first or last row respectively.

		nRow =	(pt.y + raOuter.y <= AUTOSCROLL_MARGIN)			// Is row at top of screen?
				 ?
				(nRow <= 0 ? 0 : nRow - 1)						// Yes, scroll up one row
				 :
				(nRow < getRowCount() - 1 ? nRow + 1 : nRow);	// No, scroll down one row

		scrollRowToVisible(nRow);
	}


	/** Calculate the insets for the *JTREE*, not the viewport
	 * the tree is in. This makes it a bit messy.
         * @return Insets represent location frame area.
         */
	public Insets getAutoscrollInsets()
	{
		Rectangle raOuter = getBounds();
		Rectangle raInner = getParent().getBounds();
		return new Insets(
			raInner.y - raOuter.y + AUTOSCROLL_MARGIN, raInner.x - raOuter.x + AUTOSCROLL_MARGIN,
			raOuter.height - raInner.height - raInner.y + raOuter.y + AUTOSCROLL_MARGIN,
			raOuter.width - raInner.width - raInner.x + raOuter.x + AUTOSCROLL_MARGIN);
	}
       
        //Added with case 3587: Multicampus enhancement : start
        private void checkForRights(ITreeNodeInfo newParent,ITreeNodeInfo oldParent , ITreeNodeInfo selectedNode)
                                    throws Exception{
            if(newParent instanceof UnitHierarchyFormBean){
                if(!checkUserCanMaintainHierarchy(newParent.getNodeID())){
                    throw new CoeusException(coeusMessageResources.parseMessageKey("unitHrchyBaseWin_exceptionCode.1119"));
                }
            }
            if(oldParent instanceof UnitHierarchyFormBean){
                if(!checkUserCanMaintainHierarchy(oldParent.getNodeID())){
                    throw new CoeusException(coeusMessageResources.parseMessageKey("unitHrchyBaseWin_exceptionCode.1119"));
                }
            }
            if(selectedNode instanceof UnitHierarchyFormBean){
                if(!checkUserCanMaintainHierarchy(selectedNode.getNodeID())){
                    throw new CoeusException(coeusMessageResources.parseMessageKey("unitHrchyBaseWin_exceptionCode.1119"));
                }
            }
        }
        
        private boolean checkUserCanMaintainHierarchy(String unitNumber){
            boolean hierarchyRight = false;
            String MAINTAIN_HIERARCHY = "MAINTAIN_HIERARCHY";
            Hashtable authorizations = new Hashtable();
            AuthorizationBean authorizationBean = new AuthorizationBean();
            authorizationBean.setFunction(MAINTAIN_HIERARCHY);
            authorizationBean.setFunctionType("RIGHT");
            authorizationBean.setPerson(CoeusGuiConstants.getMDIForm().getUserId());
            authorizationBean.setQualifier(unitNumber);
            authorizationBean.setQualifierType("UNIT");
            authorizations.put(MAINTAIN_HIERARCHY, new AuthorizationOperator(authorizationBean));
            
            RequesterBean requester = new RequesterBean();
            requester.setAuthorizationOperators(authorizations);
            requester.setIsAuthorizationRequired(true);
            
            AppletServletCommunicator comm = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL +"/AuthorizationServlet", requester);
            
            comm.send();
            ResponderBean responder = comm.getResponse();
            if(responder.isSuccessfulResponse()){
                authorizations = responder.getAuthorizationOperators();
                hierarchyRight = ((Boolean)authorizations.get(MAINTAIN_HIERARCHY)).booleanValue();
            }else{
                CoeusOptionPane.showInfoDialog(responder.getMessage());
            }
            return hierarchyRight;
        }
    // 3587: Multi Campus Enahncements - End
} //end of DnDJTree