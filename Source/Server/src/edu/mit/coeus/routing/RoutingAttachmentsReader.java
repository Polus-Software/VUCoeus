/*
 * @(#)RoutingAttachmentsReader.java 1.0 10/18/02 10:29 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.routing;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.routing.bean.RoutingAttachmentBean;
import edu.mit.coeus.routing.bean.RoutingTxnBean;
import edu.mit.coeus.utils.document.CoeusDocument;
import edu.mit.coeus.utils.document.DocumentReader;
import java.util.List;
import java.util.Map;

/**
 *
 * @author leenababu
 */
public class RoutingAttachmentsReader implements DocumentReader{
    
    private static final String GET_ROUTING_ATTACHMENT = "GET_ROUTING_ATTACHMENT";
    /**
     * Creates a new instance of RoutingAttachmentsReader
     */
    public RoutingAttachmentsReader() {
    }
    
    public CoeusDocument read(Map map) throws Exception {
        CoeusDocument coeusDocument = new CoeusDocument();
        String functionType = (String)map.get("FUNCTION_TYPE");
        RoutingAttachmentBean routingAttachmentBean = (RoutingAttachmentBean)map.get("DATA");
        if(functionType.equals(GET_ROUTING_ATTACHMENT)){
            routingAttachmentBean = new RoutingTxnBean().getRoutingAttachment(routingAttachmentBean);
        }
        
        if(routingAttachmentBean!=null){
            coeusDocument.setDocumentName(routingAttachmentBean.getFileName());
            coeusDocument.setDocumentData(routingAttachmentBean.getFileBytes());
        }else{
            throw new CoeusException("Could not fetch Routing Attachment");
        }        
        
        return coeusDocument;
    }
    public boolean isAuthorized(List lstAuthorizationBean) throws CoeusException {
        return true;
    }
}
