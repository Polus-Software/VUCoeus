/*
 * UpdateStatusAction.java
 *
 * Created on May 31, 2005, 12:19 PM
 */

package edu.mit.coeuslite.irb.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
//start--1
import edu.mit.coeuslite.utils.CoeusLiteConstants;
//end--1
import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author  nadhgj
 */
public class UpdateStatusAction extends ProtocolBaseAction {
    
    //Removing instance variable case# 2960
    //private WebTxnBean webTxnBean ;
    
    /** Creates a new instance of UpdateStatusAction */
    public UpdateStatusAction() {
        //Removing instance variable case# 2960
        //webTxnBean = new WebTxnBean();
    }
    public org.apache.struts.action.ActionForward performExecute(ActionMapping mapping, 
                                                                 ActionForm form, 
                                                                 HttpServletRequest req,
                                                                 HttpServletResponse res) throws Exception {
        //start--2                                                                 
        Integer protocolStatusCode = new Integer(CoeusLiteConstants.PROTOCOL_STATUS_CODE);
        //end--2
        HttpSession session = req.getSession();
        //start--3
        Vector vecMenuItems = (Vector)session.getAttribute(CoeusLiteConstants.MENU_ITEMS);
        //end--3
        //Modified for instance variable case#2960.
        WebTxnBean webTxnBean = new WebTxnBean();        
        int size = vecMenuItems.size();
        MenuBean menuBean;
        for(int index = 0; index < size; index++) {
            menuBean = (MenuBean)vecMenuItems.get(index);
            if(!menuBean.isDataSaved() && !menuBean.getMenuId().equals("003")){
                //Menu Not Saved. Cannot Submit Protocol 
                String msg = menuBean.getMenuName() + " Not Saved. Cannot Submit Protocol.";
                throw new CoeusException(msg);
            }
        }
        
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId();
        
        HashMap hmProtocol = new HashMap();
        //start--4
        hmProtocol.put(CoeusLiteConstants.PROTOCOL_NUMBER, (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER)+session.getId());
        //end--5
        hmProtocol.put("StatusCode", protocolStatusCode);
        webTxnBean.getResults(req, "updateProtocolStatus", hmProtocol );
        return mapping.findForward("success");
    }
    
    public void cleanUp() {
        
    }
    
    
    
}
