/*
 * MetaRuleNode.java
 *
 * Created on October 10, 2005, 4:54 PM
 */

package edu.mit.coeus.mapsrules.controller;

import edu.mit.coeus.mapsrules.bean.MetaRuleDetailBean;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author  chandrashekara
 */
public class MetaRuleNode extends DefaultMutableTreeNode{
    MetaRuleDetailBean node;
    /** Creates a new instance of RuleNode */
    public MetaRuleNode() {
    }
    
    public MetaRuleNode(Object userObj) {
        super(userObj);
        node = (MetaRuleDetailBean)userObj;
    }
    
    public MetaRuleDetailBean getDataObject() {
        return node;
    }
    
    public void setDataObject(MetaRuleDetailBean obj) {
        this.node = obj;
    }
}


