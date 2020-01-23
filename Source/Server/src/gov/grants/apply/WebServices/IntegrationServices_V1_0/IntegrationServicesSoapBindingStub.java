/**
 * IntegrationServicesSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package gov.grants.apply.WebServices.IntegrationServices_V1_0;

import java.io.InputStream;
import java.util.Iterator;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.SOAPEnvelope;

public class IntegrationServicesSoapBindingStub extends org.apache.axis.client.Stub implements gov.grants.apply.WebServices.IntegrationServices_V1_0.IntegrationServicesPortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[1];
        org.apache.axis.description.OperationDesc oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetXmlFromPureEdge");
        oper.addParameter(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/IntegrationServices-V1.0", "GetXmlFromPureEdgeRequest"), new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/IntegrationServices-V1.0", ">GetXmlFromPureEdgeRequest"), gov.grants.apply.WebServices.IntegrationServices_V1_0._GetXmlFromPureEdgeRequest.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/IntegrationServices-V1.0", ">GetXmlFromPureEdgeResponse"));
        oper.setReturnClass(gov.grants.apply.WebServices.IntegrationServices_V1_0._GetXmlFromPureEdgeResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/IntegrationServices-V1.0", "GetXmlFromPureEdgeResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/soap/", "fault"),
                      "org.xmlsoap.schemas.wsdl.soap.TFault",
                      new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/soap/", "tFault"), 
                      true
                     ));
        _operations[0] = oper;

    }

    public IntegrationServicesSoapBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public IntegrationServicesSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public IntegrationServicesSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/IntegrationServices-V1.0", "EncodingTypeEnum");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.WebServices.IntegrationServices_V1_0.EncodingTypeEnum.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/IntegrationServices-V1.0", ">GetXmlFromPureEdgeResponse");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.WebServices.IntegrationServices_V1_0._GetXmlFromPureEdgeResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/IntegrationServices-V1.0", ">GetXmlFromPureEdgeRequest");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.WebServices.IntegrationServices_V1_0._GetXmlFromPureEdgeRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/IntegrationServices-V1.0", ">OrganizationID");
            cachedSerQNames.add(qName);
            cls = gov.grants.apply.WebServices.IntegrationServices_V1_0._OrganizationID.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/soap/", "tFault");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.soap.TFault.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/wsdl/soap/", "tFaultRes");
            cachedSerQNames.add(qName);
            cls = org.xmlsoap.schemas.wsdl.soap.TFaultRes.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    private org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call =
                    (org.apache.axis.client.Call) super.service.createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                        java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                        _call.registerTypeMapping(cls, qName, sf, df, false);
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", t);
        }
    }

    public gov.grants.apply.WebServices.IntegrationServices_V1_0._GetXmlFromPureEdgeResponse getXmlFromPureEdge(gov.grants.apply.WebServices.IntegrationServices_V1_0._GetXmlFromPureEdgeRequest getXmlFromPureEdgeRequest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        //_call.setSOAPActionURI("https:/ws.grants.gov:446/services-server/services/IntegrationServicesSoapPort/GetXmlFromPureEdge");
        _call.setSOAPActionURI("https://atws.grants.gov:446/general/services/IntegrationServicesSoapPort/GetXmlFromPureEdge");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "GetXmlFromPureEdge"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {getXmlFromPureEdgeRequest});
        
        /*try{
            //System.out.println(_call.getResponseMessage().getSOAPPartAsString());
            SOAPEnvelope sOAPEnvelope = _call.getMessageContext().getMessage().getSOAPPart().getEnvelope();
            javax.xml.soap.SOAPBody soapBody = sOAPEnvelope.getBody();
            Iterator iterator;
            Object object;
            iterator = soapBody.getChildElements();
            //Iterator iterator = soapBody.getChildElements(sOAPEnvelope.createName("<soap:Body>"));
            System.out.println("BODY - START");
            while(iterator.hasNext()) {
                object = iterator.next();
                System.out.println(object.toString());
            }
            System.out.println("BODY - END");
            
            iterator  = sOAPEnvelope.getChildElements();
            System.out.println("ENVELOPE - START");
            while(iterator.hasNext()) {
                object = iterator.next();
                System.out.println(object.toString());
            }
           System.out.println("ENVELOPE - END");
        }catch (Exception exception) {
            exception.printStackTrace();
        }*/
        
        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
            /*Object objects[] = this.getAttachments();
            AttachmentPart attachmentPart;
            InputStream inputStream;
            for(int i=0; i<objects.length; i++){
                System.out.println(objects[i].getClass());
                attachmentPart = (AttachmentPart)objects[i];
                System.out.println(attachmentPart.getContentId()+" : "+attachmentPart.getContentType());
                inputStream = attachmentPart.getDataHandler().getInputStream();
                byte bytes[] = new byte[inputStream.available()];
                inputStream.read(bytes);
                String str = new String(bytes);
                System.out.println(str);
            }*/
            
                return (gov.grants.apply.WebServices.IntegrationServices_V1_0._GetXmlFromPureEdgeResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (gov.grants.apply.WebServices.IntegrationServices_V1_0._GetXmlFromPureEdgeResponse) org.apache.axis.utils.JavaUtils.convert(_resp, gov.grants.apply.WebServices.IntegrationServices_V1_0._GetXmlFromPureEdgeResponse.class);
            }
        }
    }

}
