/*
 * @(#)UnitHierarchyPanel.java.java September 2, 2002, 10:29 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.unit.gui;

import javax.swing.*;
import javax.swing.tree.*;

import java.util.*;

import edu.mit.coeus.unit.bean.UnitHierarchyFormBean;
import edu.mit.coeus.utils.tree.DnDJTree;
import edu.mit.coeus.utils.tree.TreeBuilder;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.gui.CoeusFontFactory;

import java.awt.*;

import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.event.TreeSelectionEvent;

import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.*;

/**
 * The class used to provide necessary methods to create and manipulate unit
 *  hierarchy tree and the contatiner which holds the tree. This class will 
 *  make use of the reusable components from the edu.mit.coeus.utils.tree 
 *  package.
 * @author  Geo
 * @version 1.0 September 2, 2002, 10:29 PM
 */
public class UnitHierarchyForm extends javax.swing.JComponent {
    private Vector unitHierarchyNodes;
    private UnitHierarchyFormBean rootHierarchy;
    private String rootUnitNumber;
    
    // JM 7-23-2015 is user application administrator
    private boolean isAdmin = false;
    
    /* JM 7-23-2015 custom hierarchy constructor */
    public UnitHierarchyForm(String rootUnitNumber,String userId) throws Exception{
        this.rootUnitNumber = rootUnitNumber;
        checkIsAdmin(userId); 
        initComponents();
    }
    /* JM END */
    
    /** Creates new form UnitHierarchyPanel */
    public UnitHierarchyForm(String rootUnitNumber) throws Exception{
        this.rootUnitNumber = rootUnitNumber;
        initComponents();
    }
    /**
     *  The method use to get the unit hierarchy tree which has been created
     *  by using <code>TreeBuilder</code> class in the <code>tree</code> package.
     */
    public edu.mit.coeus.utils.tree.DnDJTree getUnitHierarchyTree(){
        return trUnitHierarchy;
    }
    /* This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() throws Exception{
        scrPnUnitHierarchy = new javax.swing.JScrollPane();
        //get the tree from vector
        TreeBuilder treeBuilder = new TreeBuilder(
                getUnitHierarchyData(rootUnitNumber),rootUnitNumber);
        trUnitHierarchy = new DnDJTree(treeBuilder.getHierarchyRoot());
        trUnitHierarchy.setRootVisible(false);
        
        trUnitHierarchy.setBackground(Color.lightGray);
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setBackgroundNonSelectionColor(Color.lightGray);
        Icon icn =  new ImageIcon(getClass().getClassLoader().getResource("images/close.gif"));
        trUnitHierarchy.expandRow(0);
        trUnitHierarchy.getSelectionModel().setSelectionMode
                  (TreeSelectionModel.SINGLE_TREE_SELECTION);
        renderer.setOpenIcon(icn);
        renderer.setClosedIcon(icn);
        renderer.setLeafIcon(icn);
        
        trUnitHierarchy.setCellRenderer(renderer);
        setLayout(new java.awt.BorderLayout());
        trUnitHierarchy.revalidate();
        scrPnUnitHierarchy.setViewportView(trUnitHierarchy);
        scrPnUnitHierarchy.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrPnUnitHierarchy.revalidate();
        this.add(scrPnUnitHierarchy);
    }
    /**
     *  The method used to refresh the existing tree. This method will be 
     *  invoked after saving the unit hierarchy data. It will fetch the 
     *  new data from the database and construct the tree.
     */
    public void refreshTree() throws Exception{
        TreeBuilder treeBuilder = new TreeBuilder(
                getUnitHierarchyData(rootUnitNumber),rootUnitNumber);
        trUnitHierarchy = new DnDJTree(treeBuilder.getHierarchyRoot());
        trUnitHierarchy.setRootVisible(false);
        
        trUnitHierarchy.setBackground(Color.lightGray);
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setBackgroundNonSelectionColor(Color.lightGray);
        Icon icn =  new ImageIcon(getClass().getClassLoader().getResource("images/close.gif"));
        trUnitHierarchy.expandRow(0);
        trUnitHierarchy.getSelectionModel().setSelectionMode
                  (TreeSelectionModel.SINGLE_TREE_SELECTION);
        renderer.setOpenIcon(icn);
        renderer.setClosedIcon(icn);
        renderer.setLeafIcon(icn);
        
        trUnitHierarchy.setCellRenderer(renderer);
        setLayout(new java.awt.BorderLayout());
        trUnitHierarchy.revalidate();
        scrPnUnitHierarchy.setViewportView(trUnitHierarchy);
        scrPnUnitHierarchy.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrPnUnitHierarchy.revalidate();
    }
     /**
     * Get unit hierarchy data from the database.
     * @param unit number
     * @return collection of unit hierarchy data as UnitHierarchyFormBean 
     * instances.
     */
    public Vector getUnitHierarchyData(String unitNumber) throws Exception{
        String connURL = CoeusGuiConstants.CONNECTION_URL+"/unitServlet";
        Vector unitHeierarchyData  = new Vector(3,2);
        // connect to the database and get the formData for the given organization id
        RequesterBean request = new RequesterBean();
        /* JM 7-23-2015 get hierarchy based on admin permissions */
        if (isAdmin) {
        	request.setFunctionType('H');
        }
        else {
        	request.setFunctionType('V');        	
        }
        /* JM END */
        request.setId(unitNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(
                                                    connURL, request);
        comm.send();
        ResponderBean response = comm.getResponse();

        if (response.isSuccessfulResponse()){
            unitHeierarchyData = (Vector)response.getDataObject();
        }else{
            throw new Exception(response.getMessage());
        }
        return unitHeierarchyData;
    }
    
    // JM 7-17-2015 check if user is application administrator and if so show status
    public void checkIsAdmin(String userId) {
	    edu.vanderbilt.coeus.utils.UserPermissions perm = new edu.vanderbilt.coeus.utils.UserPermissions(userId);
	    isAdmin = false;
	    try {
			isAdmin = perm.hasRole(1); // role_id = 1 is the Application Administrator role
		} catch (CoeusClientException e) {
			System.out.println("Unable to determine administrator rights for user");
			e.printStackTrace();
		}
    }
    // JM END

    // Variables declaration 
    private javax.swing.JScrollPane scrPnUnitHierarchy;
    private DnDJTree trUnitHierarchy;
    // End of variables declaration
}
