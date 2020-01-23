/*
 * AreaSearchForm.java
 *
 * Created on March 17, 2005, 5:17 PM
 */

package edu.mit.coeuslite.iacuc.form;

import edu.mit.coeuslite.irb.action.TreeView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author  shijiv
 */
public class AreaSearchForm extends ProtocolBaseActionForm {
    
   // private String areaCode;
   // private String areaDesc;
    private TreeView treeview;
    /** Creates a new instance of AreaSearchForm */
    public AreaSearchForm() {
    }
     
    
     public  void reset(ActionMapping mapping, HttpServletRequest request){
         
     }
     
     public ActionErrors validate(ActionMapping mapping, HttpServletRequest req){
         return new ActionErrors();
     }
     
     /**
      * Getter for property treeview.
      * @return Value of property treeview.
      */
     public TreeView getTreeview() {
         return treeview;
     }
     
     /**
      * Setter for property treeview.
      * @param treeview New value of property treeview.
      */
     public void setTreeView(TreeView treeview) {
         this.treeview = treeview;
     }
     
}
