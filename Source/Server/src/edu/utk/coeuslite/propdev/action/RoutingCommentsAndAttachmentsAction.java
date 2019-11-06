/*
 * RoutingCommentsAndAttachmentsAction.java
 *
 * Created on May 25, 2011, 12:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 02-JUNE-2011
 * by Manjunatha
 */

package edu.utk.coeuslite.propdev.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.routing.bean.RoutingAttachmentBean;
import edu.mit.coeus.routing.bean.RoutingDetailsBean;
import edu.mit.coeus.routing.bean.RoutingMapBean;
import edu.mit.coeus.routing.bean.RoutingTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentIdGenerator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author manjunathabn
 */

public class RoutingCommentsAndAttachmentsAction extends ProposalBaseAction  {
    
    private String approvalFlags [][] = {
        {"W","Waiting for Approval"},
        {"A","Approved"},
        {"R","Rejected"},
        {"P","Passed"},
        {"L","Passed by other"},
        {"O","Approved by other"},
        {"B","Bypassed"},
        {"J","Rejected by other"},
        {"D", "Delegated"},
        {"T", "To be submitted"}
    };
    
    private static final String MAP_NUMBER = "mapNumber";
    private static final String STOP_NUMBER = "stopNumber";
    private static final String ROUTING_NUMBER = "routingNumber";
    private static final String APPROVER_NUMBER = "approverNumber";
    private static final String LEVEL_NUMBER = "levelNumber";
    
    /**
     * Creates a new instance of RoutingCommentsAndAttachmentsAction
     */
    public RoutingCommentsAndAttachmentsAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        
        if("/routingCommentsAndAttachments".equals(actionMapping.getPath())) {
            String routingNumber = request.getParameter(ROUTING_NUMBER);
            session = request.getSession();
            CoeusVector cvExistingApproversWithCommentsAttachements = new CoeusVector();
            
            UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user"+session.getId());
            
            if(routingNumber != null) {
                // Fetch the Data
                cvExistingApproversWithCommentsAttachements = getAppCommentsAttachmentsDetails(request,routingNumber);
                session.setAttribute("cvApprDetails", cvExistingApproversWithCommentsAttachements);
                if(cvExistingApproversWithCommentsAttachements.size() == 0) {
                    return actionMapping.findForward("error");
                }
                return actionMapping.findForward("success");
            }
            return actionMapping.findForward("error");
        }
        
        if("/routingAttachments".equals(actionMapping.getPath())) {
            Vector vecAttachments = getAttachments(request);
            request.getSession().setAttribute("ApprovalRoutingAttachments", vecAttachments);
            String[] attachment = request.getParameterValues("attachment");
            int selectedIndex = Integer.parseInt(request.getParameter("index"));
            String templateURL= viewDocument(session, selectedIndex);
            session.setAttribute("url", templateURL);
            response.sendRedirect(request.getContextPath()+templateURL);
            return null;
        }
        return null;
        
    }
    
    /*
     * To get the all Approvers Comments and Attachments Details
     */
    private CoeusVector getAppCommentsAttachmentsDetails(HttpServletRequest request, String routingNumber) throws Exception{
        HttpSession session = request.getSession();
        CoeusVector vctApprovalMaps = null;
        CoeusVector cvApprRowDetails = null;
        int parentNumber = 0;
        int mapNumber = 0;
        
        CoeusVector cvExistingApproversWithCommentsAttachements = new CoeusVector();
        RoutingTxnBean routingTxnBean = new RoutingTxnBean();
        vctApprovalMaps = routingTxnBean.getRoutingMaps(routingNumber);
        
        CoeusVector vecAppOrderData = new CoeusVector();
        if(vctApprovalMaps != null && vctApprovalMaps.size() > 0) {            
            getOrderedMap(vctApprovalMaps, vecAppOrderData, parentNumber);
            for (Object appOrderedData : vecAppOrderData) {
                RoutingMapBean routingMapBean = (RoutingMapBean)appOrderedData;
                mapNumber = routingMapBean.getMapNumber();
                cvApprRowDetails = routingTxnBean.getRoutingDetailsForMap(routingNumber, mapNumber);
                
                if(cvApprRowDetails != null && cvApprRowDetails.size() > 0) {
                    for (Object appRowDetails : cvApprRowDetails) {
                        RoutingDetailsBean routingDetailsBean = (RoutingDetailsBean)appRowDetails;
                        if( (routingDetailsBean.getComments() != null && routingDetailsBean.getComments().size() > 0) &&
                                (routingDetailsBean.getAttachments() != null && routingDetailsBean.getAttachments().size() > 0)
                                || (routingDetailsBean.getComments().size() > 0 && routingDetailsBean.getAttachments().size() == 0)
                                || (routingDetailsBean.getComments().size() == 0 && routingDetailsBean.getAttachments().size() > 0) ) {
                            
                            routingDetailsBean.setApprovalStatus(getApprovalStatusDescription(routingDetailsBean.getApprovalStatus()));
                            cvExistingApproversWithCommentsAttachements.add(routingDetailsBean);
                        }
                    }
                }
            }
        }
        return cvExistingApproversWithCommentsAttachements;
    }
    
    
    /*
     * To sort the details based on Mapnumber
     */
    private void getOrderedMap(Vector vecApprovalRouteMaps, Vector vecAppOrderData, int parentNumber){
        if(vecApprovalRouteMaps != null){
            Vector vecFilteredData = new Vector();
            for (Object approvalRouteMaps : vecApprovalRouteMaps) {
                RoutingMapBean routingMapBean = (RoutingMapBean)approvalRouteMaps;
                if(routingMapBean != null && ((Integer)routingMapBean.getParentMapNumber()).intValue() == parentNumber){
                    vecFilteredData.add(routingMapBean);
                }
            }
            for (Object appFilteredData : vecFilteredData) {
                RoutingMapBean routingMapBean = (RoutingMapBean)appFilteredData;
                vecAppOrderData.add(routingMapBean);
                getOrderedMap(vecApprovalRouteMaps, vecAppOrderData, ((Integer)routingMapBean.getMapNumber()).intValue());
            }
        }
    }
    
    /*
     * To add the Approval Statuc text for approval status
     */
    private String getApprovalStatusDescription(String approvalStatusCode) {
        if(approvalStatusCode != null) {
            for(int index=0;index<approvalFlags.length;index++) {
                if(approvalFlags[index][0].equals(approvalStatusCode)) {
                    return approvalFlags[index][1];
                }
            }
        }
        return EMPTY_STRING;
    }
    
    /*
     * Method to get the attachment details for corresponding
     * routingNumber, mapNumber, levelNumber, stopNumber and approverNumber
     */
    public Vector getAttachments(HttpServletRequest request) throws Exception{
        RoutingTxnBean routingTxnBean = new RoutingTxnBean();
        String routingNumber = request.getParameter(ROUTING_NUMBER);
        int mapNumber = Integer.parseInt(request.getParameter(MAP_NUMBER));
        int levelNumber = Integer.parseInt(request.getParameter(LEVEL_NUMBER));
        int stopNumber = Integer.parseInt(request.getParameter(STOP_NUMBER));
        int approverNumber = Integer.parseInt(request.getParameter(APPROVER_NUMBER));
        Vector vecComments = routingTxnBean.getRoutingAttachments(
                routingNumber, mapNumber, levelNumber, stopNumber, approverNumber);
        return vecComments;
    }
    
    /*
     * Method to view the attached document
     */
    private String viewDocument(final HttpSession session, final int selectedIndex)throws Exception{
        
        final List routingAttachments = (List)session.getAttribute("ApprovalRoutingAttachments");
        final DocumentBean documentBean = new DocumentBean();
        final Map map = new HashMap();
        final RoutingAttachmentBean routingAttachmentBean = (RoutingAttachmentBean)routingAttachments.get(selectedIndex);
        
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.routing.RoutingAttachmentsReader");
        map.put("FUNCTION_TYPE","GET_ROUTING_ATTACHMENT");
        map.put("DATA", routingAttachmentBean);
        documentBean.setParameterMap(map);
        String docId = DocumentIdGenerator.generateDocumentId();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("/StreamingServlet");
        stringBuffer.append("?");
        stringBuffer.append(DocumentConstants.DOC_ID);
        stringBuffer.append("=");
        stringBuffer.append(docId);
        session.setAttribute(docId, documentBean);
        return stringBuffer.toString();
    }
    
}




