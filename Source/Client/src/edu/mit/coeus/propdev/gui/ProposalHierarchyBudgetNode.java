/*
 * ProposalHierarchyTreeNode.java
 *
 * Created on August 16, 2005, 4:09 PM
 */

package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.propdev.bean.ProposalBudgetBean;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author  nadhgj
 */
public class ProposalHierarchyBudgetNode extends DefaultMutableTreeNode{
    
    ProposalBudgetBean obj = null;
    
    /** Creates a new instance of ProposalHierarchyTreeNode */
    public ProposalHierarchyBudgetNode() {
    }
    public ProposalHierarchyBudgetNode(Object userObject) {
        super(userObject);
        this.obj = (ProposalBudgetBean)userObject;
    }
    public void setDataObject(Object obj){
        this.obj = (ProposalBudgetBean)obj;
    }
    
    public Object getDataObject(){
       return obj;
    }
}
