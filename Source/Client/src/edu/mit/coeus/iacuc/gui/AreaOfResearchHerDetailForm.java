/*
 * @(#)AreaOfResearchHerDetailForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.iacuc.gui;

import javax.swing.*;
import javax.swing.tree.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.tree.*;
import edu.mit.coeus.brokers.*;
import java.util.Vector;
import java.util.Hashtable;
import java.awt.Color;

/**
 * The class used to represent main window component for
 * Area of Research tree structure. This Component is customized and can be Pluggable 
 * from any module to depecit Area Of Research Tree Nodes. It also privlage 
 * the user for <code>Single/Mulitple</code> selection. 
 * 
 *
 * @author  Subramanya
 * @version 1.0 September 26, 2002, 8:50 PM
 */
public class AreaOfResearchHerDetailForm extends javax.swing.JComponent {

    //Holds the Area Of Research ROOT-ID.
    private String areaOfResearchRootID = null;

    //Holds the set of all TreeNodes.
    private Vector allAORNodeData = null;

    //Holds the JTree Object with Drag/Drop properties Set.
    private DnDJTree researchAreaTree = null;
    
    //holds the Icon used to display the image view
    private Icon mdfIcon = null;
    
    // Variables declaration - do not modify
    private JScrollPane jcrPnAllResearchAreas;
    // End of variables declaration

    private final static String connURL = CoeusGuiConstants.CONNECTION_URL +
                                                     "/AreaOfResearchServlet";
    
    //COEUSQA-2684  IACUC - Areas of research maintenance screen for IACUC areas of research
    private static final char IACUC_AREA_OF_RESEARCH = 'Z';
    /**
     * Creates new form AreaOfResearchHerDetailForm by accepting the RootID of
     * Area Of Research Tree.
     * @param rootID represent the RootID of the Tree Structructure.
     * @throws Exception errnous condition while obtaining the tree data
     */
    public AreaOfResearchHerDetailForm( String rootID ) throws Exception {

        areaOfResearchRootID = rootID;
        allAORNodeData = getAreaOfResearchTreeData( rootID );
        initComponents();
    }

    
    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {

        jcrPnAllResearchAreas = new javax.swing.JScrollPane();
        
        TreeBuilder treeBuilder = new TreeBuilder( allAORNodeData ,
                                                   areaOfResearchRootID );
        this.researchAreaTree  = new DnDJTree( treeBuilder.getHierarchyRoot() );
        researchAreaTree.setRootVisible( false );
        researchAreaTree.setBackground( Color.lightGray );
        researchAreaTree.setFont( CoeusFontFactory.getNormalFont() );
        
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setBackgroundNonSelectionColor(Color.lightGray);

        mdfIcon =  new ImageIcon( getClass().getClassLoader().getResource(
                                                    "images/close.gif"));        
        researchAreaTree.expandRow(0);
        
        // adding listener to the tree multiple selction mode.
        researchAreaTree.getSelectionModel().setSelectionMode
                            ( TreeSelectionModel.SINGLE_TREE_SELECTION );
                
        renderer.setOpenIcon( mdfIcon );
        renderer.setClosedIcon( mdfIcon );
        renderer.setLeafIcon( mdfIcon );
        researchAreaTree.setCellRenderer( renderer );

        
        setLayout( new java.awt.BorderLayout());
        researchAreaTree.revalidate();
        
        jcrPnAllResearchAreas.setToolTipText("AreaOfResearch");                
        jcrPnAllResearchAreas.setViewportView( researchAreaTree );
        jcrPnAllResearchAreas.revalidate();
        
        add( jcrPnAllResearchAreas , java.awt.BorderLayout.CENTER );
        
    }

    
    /** get all the form data from the database by passing the RootID of
     * Area of Research Tree Structure.
     * @param aorRootID represent the RootID ( String )
     * @throws Exception errnous condition while obtaining the tree data
     * @return  Vector collection of Area Of Research Tree Node bean
     */
    public Vector getAreaOfResearchTreeData( String aorRootID ) throws Exception{

        Vector researchData  = new Vector(3,2);
        
        // connect to the database and get the formData for the given organization id
        RequesterBean request = new RequesterBean();
        //COEUSQA-2684  IACUC - Areas of research maintenance screen for IACUC areas of research
        request.setFunctionType(IACUC_AREA_OF_RESEARCH);       
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

    
    /** This Method used to Add or Modify Area Of Research Tree Node
     * ( AreaOfResearchTreeNodeBean) Entries to the DataBase.
     * @return boolean          gives the status of DB Transaction (while
     *                          Inserting / Modifying the Data in the DB).
     * @param aorTreeNodeBean contains collection of Beans for DB Transaction
     *                          Operation.
     * @param type reprsent the transaction type. Ex: 'I'/'U'
     *                          ( Insert / Update )
     * @throws Exception  errnous condition while obtaining the tree data
     */
    public boolean addModifyAORTreeNodeData( Hashtable aorTreeNodeBean, 
                                          char type ) throws Exception {
        
        boolean isAddOprSuccess = false;        
        
        // connect to the database and insert newly added Research Area Tree 
        // Data/Modify the data.
        RequesterBean request = new RequesterBean();
        
        //COEUSQA-2684  IACUC - Areas of research maintenance screen for IACUC areas of research - start
        Vector dataObjects = new Vector();
        dataObjects.addElement(ModuleConstants.IACUC_MODULE_CODE);
        request.setFunctionType( type ); 
        request.setDataObject( aorTreeNodeBean );   
        request.setDataObjects(dataObjects);
        //COEUSQA-2684  IACUC - Areas of research maintenance screen for IACUC areas of research - end 
        AppletServletCommunicator comm = new AppletServletCommunicator(
                                                    connURL, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        isAddOprSuccess = response.isSuccessfulResponse();
        if ( isAddOprSuccess ){
            allAORNodeData = ( Vector )response.getDataObject();
        }else{
            throw new Exception( response.getMessage() );
        }
        return isAddOprSuccess;        
    }
    
    
    /**
     * Method used to get the Display Tree of Area Of Research Nodes.
     * @return  DnDJTree        represent the JTree with Move Attributes 
     *                          associated with it.
     */
    public DnDJTree getAreaOfResearchDisplayTree(){             
        return researchAreaTree;        
    }
    
    
    /**
     * Method used to get Tree Node Data (Live) for display
     * @return  Vector      collection of Tree Node Data Bean.
     */
    public Vector getAreaOfResearchDBData(){            
        return allAORNodeData;        
    }
}