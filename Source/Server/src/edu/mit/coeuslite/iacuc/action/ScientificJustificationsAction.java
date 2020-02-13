/*
 * ScientificJustificationsAction.java
 *
 * Created on October 8, 2009, 2:48 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeuslite.iacuc.action;

import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 *
 * @author suganyadevipv
 */
public class ScientificJustificationsAction extends ProtocolBaseAction{
    
    /**
     * Creates a new instance of ScientificJustificationsAction
     */
    public ScientificJustificationsAction() {
    }
     public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
         String navigator ="";
         HttpSession session = request.getSession();
         Map mapMenuList = new HashMap();
//         mapMenuList.put("menuItems",CoeusliteMenuItems.IACUC_MENU_ITEMS);
//         mapMenuList.put("menuCode",CoeusliteMenuItems.IACUC_PROTOCOL_SCIENTIFIC_JUSTIFICATION_MENU);
//         setSelectedMenuList(request, mapMenuList);
         navigator = "success";
                
        return actionMapping.findForward(navigator);
    }

    public void cleanUp() {
    }
}
