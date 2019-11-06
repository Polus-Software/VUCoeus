/*
 * HomeAction.java
 *
 * Created on March 7, 2005, 1:05 PM
 */

package edu.mit.coeuslite.irb.action;

import edu.mit.coeuslite.irb.form.HomeForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author  chandrashekara
 */
public class HomeAction extends Action{
    
    /** Creates a new instance of HomeAction */
    public HomeAction() {
    }
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
        HttpServletRequest req, HttpServletResponse res){
            HomeForm homeForm = (HomeForm)form;
            return mapping.findForward("success");
    }
    
}
