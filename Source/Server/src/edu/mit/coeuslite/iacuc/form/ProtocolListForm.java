/*
 * ProtocolListForm.java
 *
 * Created on March 10, 2005, 4:12 PM
 */

package edu.mit.coeuslite.iacuc.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author  vinayks
 */
public class ProtocolListForm extends ProtocolBaseActionForm {
    
    /** Creates a new instance of ProtocolListForm */
    public ProtocolListForm() {
    }
    
    public void reset(ActionMapping mapping,HttpServletRequest req){
    
    }
    
     public ActionErrors validate(ActionMapping mapping,HttpServletRequest req){
        
        return new ActionErrors();
    }
    
}

