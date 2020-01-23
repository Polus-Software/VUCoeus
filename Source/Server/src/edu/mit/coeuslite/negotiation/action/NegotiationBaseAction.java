/*
 * @(#)NegotiationBaseAction.java 1.0 July 2, 2009, 3:03 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeuslite.negotiation.action;

import edu.mit.coeus.negotiation.bean.NegotiationHeaderBean;
import edu.mit.coeus.negotiation.bean.NegotiationTxnBean;
import edu.mit.coeuslite.irb.bean.ReadProtocolDetails;
import edu.mit.coeuslite.utils.CoeusBaseAction;
import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.wmc.coeuslite.budget.bean.ReadXMLData;
import java.util.Vector;
import javax.servlet.http.*;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author satheeshkumarkn
 * @version
 */
public abstract class NegotiationBaseAction extends CoeusBaseAction {
    
    private static final String NEGOTIATION_HEADER_BEAN = "negotiationHeaderBean";
    private static final String SUB_HEADER = "negotiationSubHeaderVector";
    private static final String XML_PATH = "/edu/mit/coeuslite/negotiation/xml/NegotiationSubMenu.xml";
    private static final String XML_MENU_PATH = "/edu/mit/coeuslite/negotiation/xml/NegotiationMenu.xml";
    private static final String NEGOTIATION_MENU_ITEMS = "negotiationMenuItems";
    private static final String NEGOTIATION_NUMBER = "negotiationNumber";
    private static final String LEAD_UNIT = "leadUnit";
    
    
    public NegotiationBaseAction(){
    }
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception{
        fetchSubheaderDetails();
        return performExecuteNegotiation(actionMapping,actionForm,request,response);
    }
    public abstract ActionForward performExecuteNegotiation(ActionMapping actionMapping,
            ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception;
    
    /** This method reads the xml file and gets the subheader data
     **/
    private void fetchSubheaderDetails()throws Exception{
        javax.servlet.ServletContext application = getServlet().getServletConfig().getServletContext();
        Vector cvNegotiationSubHeader = new Vector();
        ReadProtocolDetails readNegotiationSubHeaderDetails = new ReadProtocolDetails();
        cvNegotiationSubHeader = (Vector)application.getAttribute(SUB_HEADER);
        if(cvNegotiationSubHeader == null || cvNegotiationSubHeader.size()==0){
            cvNegotiationSubHeader = readNegotiationSubHeaderDetails.readXMLDataForSubHeader(XML_PATH);
            application.setAttribute(SUB_HEADER,cvNegotiationSubHeader);
        }
    }
    /**This method is used to get the Negotiation Menu from NegotiationMenu.xml
     *@param request - HttpServletRequestObject.
     */
    protected void getNegotiationMenus(HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        Vector vctNegotiationMenuItems  = null;
        ReadXMLData readXMLData = new ReadXMLData();
        vctNegotiationMenuItems = readXMLData.readXMLDataForMenu(XML_MENU_PATH);
//        if(vctNegotiationMenuItems!=null && !vctNegotiationMenuItems.isEmpty()){
//            MenuBean menuBean;
//            for(int i=0;i<vctNegotiationMenuItems.size();i++){
//                menuBean = (MenuBean)vctNegotiationMenuItems.get(i);
//            }
//        }
        session.setAttribute(NEGOTIATION_MENU_ITEMS, vctNegotiationMenuItems);
    }
    /**This method is used to get the Negotiation header details for DB.
     *@param request - HttpServletRequestObject.
     *@param negotiationNumber - negotiationNumber to get header details.
     */
    protected void getNegotiationHeaderDetails(String negotiationNumber, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        if(negotiationNumber!= null && !negotiationNumber.equals(EMPTY_STRING)){
            NegotiationTxnBean negotiationTxnBean = new NegotiationTxnBean();
            NegotiationHeaderBean negotiationHeaderBean = negotiationTxnBean.getNegotiationHeader(negotiationNumber);
            session.setAttribute(NEGOTIATION_HEADER_BEAN,negotiationHeaderBean);
            session.setAttribute(NEGOTIATION_NUMBER,negotiationHeaderBean.getProposalNumber());
            session.setAttribute(LEAD_UNIT,negotiationHeaderBean.getLeadUnit());
        }else{
            session.removeAttribute(NEGOTIATION_NUMBER);
            session.removeAttribute(LEAD_UNIT);
        }
    }
    protected void setSelectedStatusMenu(String menuCode, HttpSession session){
        Vector menuItemsVector  = null;
        menuItemsVector=(Vector)session.getAttribute(NEGOTIATION_MENU_ITEMS);
        Vector modifiedVector = new Vector();
        for (int index=0; index<menuItemsVector.size();index++) {
            MenuBean menuBean = (MenuBean)menuItemsVector.get(index);
            String menuId = menuBean.getMenuId();
            if (menuId.equals(menuCode)) {
                menuBean.setSelected(true);
            } else {
                menuBean.setSelected(false);
            }
            modifiedVector.add(menuBean);
        }
        session.setAttribute(NEGOTIATION_MENU_ITEMS, modifiedVector);
    }
    
}
