/*
 * GetXMLFromPureEdge.java
 *
 * Created on June 7, 2006, 3:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.s2s;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.S2SConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.mail.ByteArrayDataSource;
import gov.grants.apply.WebServices.IntegrationServices_V1_0.IntegrationServicesLocator;
import gov.grants.apply.WebServices.IntegrationServices_V1_0.IntegrationServicesSoapBindingStub;
import gov.grants.apply.WebServices.IntegrationServices_V1_0._GetXmlFromPureEdgeRequest;
import gov.grants.apply.WebServices.IntegrationServices_V1_0._GetXmlFromPureEdgeResponse;
import gov.grants.apply.WebServices.IntegrationServices_V1_0._OrganizationID;
import gov.grants.apply.soap.util.SoapUtils;
import java.io.InputStream;
import java.net.*;
import javax.activation.DataHandler;
import javax.xml.soap.AttachmentPart;
import org.apache.axis.client.Call;

/**
 *
 * @author sharathk
 */
public class GetXMLFromPureEdge {
    
    private Object attachments[];
    
    /** Creates a new instance of GetXMLFromPureEdge */
    public GetXMLFromPureEdge() {
    }
    
    public char[] getXML(byte[] xfd) throws CoeusException{
        if(xfd == null || xfd.length == 0) {
            throw new CoeusException("XFD contained in Bean was NULL or EMPTY");
        }
        try{
            IntegrationServicesLocator service = new IntegrationServicesLocator();
            SoapUtils soapUtils = new SoapUtils();
            URL url = new URL(soapUtils.getSoapURL(S2SConstants.SOAP_SERVICE_SERVER_HOST, S2SConstants.SOAP_SERVICE_SERVER_PORT));
            IntegrationServicesSoapBindingStub stub = new IntegrationServicesSoapBindingStub(url, service);
            _GetXmlFromPureEdgeRequest req = new _GetXmlFromPureEdgeRequest();
            
            _OrganizationID orgId = new _OrganizationID();
            //orgId.setValue("0571231920000");
            orgId.setValue("000000000");
            req.setOrganizationID(orgId);
            
            org.apache.axis.attachments.AttachmentPart attachmentPart = new org.apache.axis.attachments.AttachmentPart();
            DataHandler attachmentFile = new DataHandler(new ByteArrayDataSource(xfd, "application/octet-stream"));
            
            // Tell the stub that the message being formed also contains an
            //attachment, and it is of type MIME encoding.
            
            stub._setProperty(Call.ATTACHMENT_ENCAPSULATION_FORMAT, Call.ATTACHMENT_ENCAPSULATION_FORMAT_MIME);
            attachmentPart.setDataHandler(attachmentFile);
            attachmentPart.setContentId("123");
            attachmentPart.setContentType("application/octet-stream");
            stub.addAttachment(attachmentPart);
            
            _GetXmlFromPureEdgeResponse xmlResponse = stub.getXmlFromPureEdge(req);
            
            attachments = stub.getAttachments();
            InputStream inputStream;
            //AttachmentPart xmlAttachmentPart;
            //byte bytes[];
            
                javax.xml.soap.AttachmentPart xmlAttachmentPart;
                //InputStream inputStream;
                //for(int i=0; i<attachments.length; i++){
                    //System.out.println(attachments[i].getClass());
                    xmlAttachmentPart = (javax.xml.soap.AttachmentPart)attachments[0];
                    //System.out.println(attachmentPart.getContentId()+" : "+attachmentPart.getContentType());
                    inputStream = xmlAttachmentPart.getDataHandler().getInputStream();
                    byte xmlBytes[] = new byte[inputStream.available()];
                    inputStream.read(xmlBytes);
                    inputStream.close();
                    String str = new String(xmlBytes);
                    //System.out.println(str);
                //}
            
            return str.toCharArray();
            
        }catch (Exception exception) {
            UtilFactory.log(exception.getMessage(), exception, "GetXMLFromPureEdge","getXML");
            CoeusException coeusException = new CoeusException(exception);
            coeusException.setMessage(exception.getMessage() == null ? "No Message" : exception.getMessage());
            coeusException.setStackTrace(exception.getStackTrace());
            throw coeusException;
        }
    }

    public Object[] getAttachments() {
        return attachments;
    }

    public void setAttachments(Object[] attachments) {
        this.attachments = attachments;
    }
    

}
