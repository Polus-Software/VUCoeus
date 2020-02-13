/*
 * QuestionnaireNode.java
 *
 * Created on September 19, 2006, 3:49 PM
 */

package edu.mit.coeus.questionnaire.controller;

import edu.mit.coeus.questionnaire.bean.QuestionnaireBean;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author  chandrashekara
 */
public class QuestionnaireNode extends DefaultMutableTreeNode{
    private QuestionnaireBean node;
    /** Creates a new instance of QuestionnaireNode */
    public QuestionnaireNode() {
    }
    
     public QuestionnaireNode(Object userObj) {
        super(userObj);
        node = (QuestionnaireBean)userObj;
    }
    
    public QuestionnaireBean getDataObject() {
        return node;
    }
    
    public void setDataObject(QuestionnaireBean obj) {
        this.node = obj;
    }
    
}
