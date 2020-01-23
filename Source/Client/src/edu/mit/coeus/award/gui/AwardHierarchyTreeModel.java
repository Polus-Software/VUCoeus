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

/**
 * AwardHierarchyTreeModel.java
 * Created on April 6, 2004, 10:50 AM
 * @author  Vyjayanthi
 */
public class AwardHierarchyTreeModel extends DefaultTreeModel {
    
    /** Holds the tree root */
    private AwardHierarchyNode root;
    
    /** Holds an instance of the <CODE>AwardHierarchyDataMediator</CODE> */
    private AwardHierarchyDataMediator awardHierarchyDataMediator;
    
    /** Creates a new AwardHierarchyTreeModel
     * @param root takes a TreeNode
     */
    public AwardHierarchyTreeModel(TreeNode root) {
        super(root);
        this.root = (AwardHierarchyNode)root;
    }
    
    /**
     * Sets the data to the model
     */
    public void setData(AwardHierarchyDataMediator awardHierarchyDataMediator) {
        this.awardHierarchyDataMediator = awardHierarchyDataMediator;
    }
    
    /**
     * Returns the child of parent at index index in the parent's child array.
     */
    public Object getChild(Object parent, int index) {
        AwardHierarchyBean awardHierarchyBean = 
        awardHierarchyDataMediator.getValue((AwardHierarchyBean)parent);
        //awardHierarchyBean.getChildren().sort(AwardHierarchyTree.MIT_AWARD_NUMBER_FIELD, true);
        return awardHierarchyBean.getChildren().get(index);
    }
    
    /**
     * Returns the root of the tree.
     */
    public Object getRoot() {
        return root.getDataObject();
    }
    
    /** Returns true if node is a leaf.
     * @param node takes the AwardHierarchyBean
     */
    public boolean isLeaf(Object node) {
        AwardHierarchyBean awardHierarchyBean = 
        awardHierarchyDataMediator.getValue((AwardHierarchyBean)node);
        if( awardHierarchyBean.getChildCount() == 0 ){
            return true;
        }else{
            return false;
        }
    }
    
    /** 
     * Returns the index of child in parent.
     */
    public int getIndexOfChild(Object parent, Object child) {
        AwardHierarchyBean awardHierarchyBean = 
        awardHierarchyDataMediator.getValue((AwardHierarchyBean)parent);
        if( awardHierarchyBean.getChildCount() == 0 ){
            return -1;
        }
        int index = awardHierarchyBean.getChildren().indexOf(child);
        return index;
    }
    
    /** To get the child count of the given parent node
     * Returns the number of children of parent.
     * @param parent takes the AwardHierarchyBean
     */
    public int getChildCount(Object parent) {
        AwardHierarchyBean awardHierarchyBean = 
        awardHierarchyDataMediator.getValue((AwardHierarchyBean)parent);
        return awardHierarchyBean.getChildCount();
    }
        
    /** Abstract method of TreeModel */
    public void valueForPathChanged(TreePath path, Object newValue) {
        System.out.println("*** valueForPathChanged : "
                           + path + " --> " + newValue);
    }
    
    /** To redirect the call to the mediator's forceReload
     * @param awardHierarchyBean
     */
    public void forceReload(AwardHierarchyBean awardHierarchyBean) {
        awardHierarchyDataMediator.forceReload(awardHierarchyBean);
    }
    
}