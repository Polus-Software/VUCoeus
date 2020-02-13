/*
 * DocumentListController.java
 *
 * Created on August 28, 2003, 10:39 AM
 */

package edu.mit.coeus.irb.bean;

import java.util.Vector;

import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import java.util.HashMap;
import java.util.Map;

/** Communicates with the server to get/set Information.
 * @author sharathk
 */
public class DocumentListController {
    
    private static final String STREAMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
    private static final String connectTo = CoeusGuiConstants.CONNECTION_URL + "/protocolMntServlet";
    private static final char DOCUMENT_LIST = 'd';
    private static final char DOCUMENT_URL = 'e';
    
    private boolean hasRight = false;//PERFORM_IRB_ACTIONS_ON_PROTO
    
    /** Creates a new instance of DocumentListController */
    public DocumentListController() {
    }
    
    /**
     *  Method used to send the requester Bean to the servlet for database communication.
     * @param requester a RequesterBean which consist of userId and servlet details.
     * @return ResponderBean
     */
    private ResponderBean sendToServer(RequesterBean requester) {
        //String connectTo =CoeusGuiConstants.CONNECTION_URL+srvComponentName;
        AppletServletCommunicator comm = new AppletServletCommunicator(
        STREAMING_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        return response;
    }
    
    /** gets the document details to be viewed.
     * @param protocolActionsBean ProtocolActionsBean for which the Information has to be returned.
     * @return document details.
     */    
    public Vector getDocumentDetails(ProtocolActionsBean protocolActionsBean) {
       RequesterBean request = new RequesterBean();
       request.setDataObject(protocolActionsBean);
       request.setFunctionType(DOCUMENT_LIST);
       AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
       comm.send();
       ResponderBean response = comm.getResponse();
       if(response.getDataObjects() != null){
           Boolean boolObj = (Boolean)response.getDataObjects().get(0);
           hasRight = boolObj.booleanValue();
       }
       return (Vector)response.getDataObject();
    }
    
    /** returns the document url to be viewed.
     * @param protoCorrespRecipientsBean ProtoCorrespRecipientsBean for which the docuemtn has to be viewed.
     * @return url of the document to view.
     */    
    public String getDocumentURL(ProtoCorrespRecipientsBean protoCorrespRecipientsBean) {
       RequesterBean request = new RequesterBean();
//       request.setDataObject(protoCorrespRecipientsBean);
//       request.setFunctionType(DOCUMENT_URL);
       //For Streaming
       DocumentBean documentBean = new DocumentBean();
       Map map = new HashMap();
       map.put("DOCUMENT_TYPE", "VIEW_APPROVAL_LETTER");
       map.put("DATA", protoCorrespRecipientsBean);
       map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.irb.ScheduleDocumentReader");
       documentBean.setParameterMap(map);
       request.setDataObject(documentBean);
       request.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
       //For Streaming
       ResponderBean response = sendToServer(request);
       map = (Map)response.getDataObject();
       String reportUrl = (String)map.get(DocumentConstants.DOCUMENT_URL);
       return reportUrl;
       //return response.getDataObject().toString();
    }
     /**
     * Getter for property hasRight.
     * @return Value of property hasRight.
     */
    public boolean isHasRight() {
        return hasRight;
    }
    
    /**
     * Setter for property hasRight.
     * @param hasRight New value of property hasRight.
     */
    public void setHasRight(boolean hasRight) {
        this.hasRight = hasRight;
    }
    
}
