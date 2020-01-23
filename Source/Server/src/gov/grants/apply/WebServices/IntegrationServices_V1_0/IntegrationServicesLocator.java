/**
 * IntegrationServicesLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package gov.grants.apply.WebServices.IntegrationServices_V1_0;

public class IntegrationServicesLocator extends org.apache.axis.client.Service implements gov.grants.apply.WebServices.IntegrationServices_V1_0.IntegrationServices {

    // Use to get a proxy class for IntegrationServicesSoapPort
    private final java.lang.String IntegrationServicesSoapPort_address = "https://ws.grants.gov:446/services-server/services/IntegrationServicesSoapPort/";

    public java.lang.String getIntegrationServicesSoapPortAddress() {
        return IntegrationServicesSoapPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String IntegrationServicesSoapPortWSDDServiceName = "IntegrationServicesSoapPort";

    public java.lang.String getIntegrationServicesSoapPortWSDDServiceName() {
        return IntegrationServicesSoapPortWSDDServiceName;
    }

    public void setIntegrationServicesSoapPortWSDDServiceName(java.lang.String name) {
        IntegrationServicesSoapPortWSDDServiceName = name;
    }

    public gov.grants.apply.WebServices.IntegrationServices_V1_0.IntegrationServicesPortType getIntegrationServicesSoapPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(IntegrationServicesSoapPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getIntegrationServicesSoapPort(endpoint);
    }

    public gov.grants.apply.WebServices.IntegrationServices_V1_0.IntegrationServicesPortType getIntegrationServicesSoapPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            gov.grants.apply.WebServices.IntegrationServices_V1_0.IntegrationServicesSoapBindingStub _stub = new gov.grants.apply.WebServices.IntegrationServices_V1_0.IntegrationServicesSoapBindingStub(portAddress, this);
            _stub.setPortName(getIntegrationServicesSoapPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (gov.grants.apply.WebServices.IntegrationServices_V1_0.IntegrationServicesPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                gov.grants.apply.WebServices.IntegrationServices_V1_0.IntegrationServicesSoapBindingStub _stub = new gov.grants.apply.WebServices.IntegrationServices_V1_0.IntegrationServicesSoapBindingStub(new java.net.URL(IntegrationServicesSoapPort_address), this);
                _stub.setPortName(getIntegrationServicesSoapPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("IntegrationServicesSoapPort".equals(inputPortName)) {
            return getIntegrationServicesSoapPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/IntegrationServices-V1.0", "IntegrationServices");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("IntegrationServicesSoapPort"));
        }
        return ports.iterator();
    }

}
