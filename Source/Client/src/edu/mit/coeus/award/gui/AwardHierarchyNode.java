/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.award.gui;

import java.awt.*;
import javax.swing.tree.*;

import edu.mit.coeus.award.bean.AwardHierarchyBean;


/**
 * AwardHierarchyNode.java
 * Created on March 11, 2004, 10:22 AM
 * @author  Vyjayanthi
 */
public class AwardHierarchyNode extends javax.swing.tree.DefaultMutableTreeNode {
    
    /** Holds an instance of <CODE>AwardHierarchyBean</CODE>
     * each node corresponds to an instance of <CODE>AwardHierarchyBean</CODE>
     */
    private AwardHierarchyBean dataObject;
  
    /** Creates a new instance of AwardHierarchyNode */
    public AwardHierarchyNode() {
    }
    
    /** Creates a new instance of AwardHierarchyNode
     * @param userObject holds an instance of <CODE>AwardHierarchyBean</CODE>
     */
    public AwardHierarchyNode(AwardHierarchyBean userObject){
        super(userObject);
        this.dataObject = userObject;
    }
    
    /** Getter for property dataObject.
     * @return Value of property dataObject.
     *
     */
    public edu.mit.coeus.award.bean.AwardHierarchyBean getDataObject() {
        return dataObject;
    }
    
    /** Setter for property dataObject.
     * @param dataObject New value of property dataObject.
     *
     */
    public void setDataObject(edu.mit.coeus.award.bean.AwardHierarchyBean dataObject) {
        this.dataObject = dataObject;
    }
    
}
