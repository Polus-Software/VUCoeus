/*
 * ArgumentListAction.java
 *
 * Created on July 21, 2006, 1:42 PM
 */

package edu.mit.coeuslite.utils;

import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author  chandrashekara
 */
public class ArgumentListAction extends CoeusBaseAction{
    private HttpServletRequest request;
    private HttpSession session;
    private static final String EMPTY_STRING = "";
    private static final String W_ARG_VALUE_LIST = "w_arg_value_list";
    private static final String W_ARG_CODE_TBL = "W_ARG_CODE_TBL";
    private static final String W_SELECT_COST_ELEMENT = "w_select_cost_element";
    private static final String ARGUMENT_LIST = "getArgValueList";
    private static final String ARGUMENT_CODE_TABLE ="getArgCodeTbl";
    private static final String ARGUMENT_LIST_DATA = "argumentList";
    private static final String WINDOW_NAME = "searchName";
    private static final String ARGUMENT_NAME = "argument";
    private WebTxnBean webTxnBean;
    /** Creates a new instance of ArgumentListAction */
    public ArgumentListAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm, 
        HttpServletRequest request, HttpServletResponse response) throws Exception {
        
            this.request = request;
            this.session = request.getSession();
            String navigator = EMPTY_STRING;
            webTxnBean = new WebTxnBean();
            String windowName = request.getParameter(WINDOW_NAME);
            String argumentName = request.getParameter(ARGUMENT_NAME);
            session.removeAttribute(ARGUMENT_LIST_DATA);
            navigator = buildArgumentData(windowName,argumentName);
            return actionMapping.findForward(navigator);
    }
    
    private String buildArgumentData(String windowName, String argumentName) throws Exception{
        Vector vecLookupdata = null;
        String navigator = EMPTY_STRING;
        if(windowName!= null && windowName.equalsIgnoreCase(W_ARG_VALUE_LIST)){
            vecLookupdata = getArgumentsData(ARGUMENT_LIST,argumentName );
            session.setAttribute(ARGUMENT_LIST_DATA,vecLookupdata);
            navigator = W_ARG_VALUE_LIST;
        }else if(windowName!= null && windowName.equalsIgnoreCase(W_ARG_CODE_TBL)){
            vecLookupdata = getArgumentsData(ARGUMENT_CODE_TABLE,argumentName);
            session.setAttribute(ARGUMENT_LIST_DATA,vecLookupdata);
            navigator = W_ARG_VALUE_LIST;
        }else if(windowName!=null && windowName.equalsIgnoreCase(W_SELECT_COST_ELEMENT)){
            vecLookupdata = getCostElementData();
            session.setAttribute(ARGUMENT_LIST_DATA,vecLookupdata);
            navigator = W_ARG_VALUE_LIST;
        }
        return navigator;
    }
    
    private Vector getArgumentsData(String key, String argumentName) throws Exception{
        HashMap hmlookupArgument = null;
        Hashtable htArgumentData = null;
        Vector vecArgumentData = null;
            if(argumentName!= null && !argumentName.equals(EMPTY_STRING)){
                hmlookupArgument = new HashMap();
                hmlookupArgument.put("argumentName",argumentName);
                htArgumentData =
                (Hashtable)webTxnBean.getResults(request, key, hmlookupArgument);
                vecArgumentData = (Vector)htArgumentData.get(key);
            }
        return vecArgumentData;
    }
    
    private Vector getCostElementData() throws Exception{
        Hashtable htCostElementData = (Hashtable)webTxnBean.getResults(request , "getCostElementList" , null );
        Vector vecCostElementData = (Vector)htCostElementData.get("getCostElementList");
        return vecCostElementData ;
    }
    
    
    
}
