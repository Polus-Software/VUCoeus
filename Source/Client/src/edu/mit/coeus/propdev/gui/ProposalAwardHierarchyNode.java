/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * ProposalAwardHierarchyNode.java
 *
 * Created on January 13, 2004, 6:27 PM
 */

package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.propdev.bean.*;
import javax.swing.tree.*;

/**
 *
 * @author  chandrashekara
 */
public class ProposalAwardHierarchyNode extends javax.swing.tree.DefaultMutableTreeNode {
    
    private ProposalAwardHierarchyLinkBean linkBean;
    /** Creates a new instance of ProposalAwardHierarchyNode */
    public ProposalAwardHierarchyNode() {
    }
    public ProposalAwardHierarchyNode(ProposalAwardHierarchyLinkBean userObject) {
        super(userObject);
        this.linkBean = userObject;
    }
    public void setDataObject(ProposalAwardHierarchyLinkBean obj){
        this.linkBean = obj;
    }
    
    public ProposalAwardHierarchyLinkBean getDataObject(){
       return linkBean;
    }
    
    
}
