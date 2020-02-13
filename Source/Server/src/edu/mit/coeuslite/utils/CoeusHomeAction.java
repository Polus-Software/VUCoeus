/*
 * CoeusHomeAction.java
 *
 * Created on September 14, 2006, 10:42 AM
 */

package edu.mit.coeuslite.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author  chandrashekara
 */
public class CoeusHomeAction extends CoeusBaseAction{
    
    /** Creates a new instance of CoeusHomeAction */
    public CoeusHomeAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm, 
        HttpServletRequest request, HttpServletResponse response) throws Exception {
            
       return actionMapping.findForward("success");
    }
    
}
