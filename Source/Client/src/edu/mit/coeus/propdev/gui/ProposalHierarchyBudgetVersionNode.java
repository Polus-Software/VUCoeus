/*
 * ProposalHierarchyTreeNode.java
 *
 * Created on August 16, 2005, 4:09 PM
 */

package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.propdev.bean.ProposalBudgetVersionBean;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author  nadhgj
 */
public class ProposalHierarchyBudgetVersionNode extends DefaultMutableTreeNode{
    
    ProposalBudgetVersionBean obj = null;
    
    /** Creates a new instance of ProposalHierarchyTreeNode */
    public ProposalHierarchyBudgetVersionNode() {
    }
    public ProposalHierarchyBudgetVersionNode(Object userObject) {
        super(userObject);
        this.obj = (ProposalBudgetVersionBean)userObject;
    }
    public void setDataObject(Object obj){
        this.obj = (ProposalBudgetVersionBean)obj;
    }
    
    public Object getDataObject(){
       return obj;
    }
}
