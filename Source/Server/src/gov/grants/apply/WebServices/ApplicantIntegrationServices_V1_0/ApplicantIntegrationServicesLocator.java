/**
 * ApplicantIntegrationServicesLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0;

public class ApplicantIntegrationServicesLocator extends org.apache.axis.client.Service implements gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.ApplicantIntegrationServices {

    public ApplicantIntegrationServicesLocator(){
        super();
    }
    /**
     * Constructs a new Service object as above, but also passing in
     * the EngineConfiguration which should be used to set up the
     * AxisClient.
     */
    public ApplicantIntegrationServicesLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }
    
    // Use to get a proxy class for ApplicantIntegrationSoapPort
    private final java.lang.String ApplicantIntegrationSoapPort_address = "http://localhost:8080/app-s2s-server/services/ApplicantIntegrationSoapPort/";

    public java.lang.String getApplicantIntegrationSoapPortAddress() {
        return ApplicantIntegrationSoapPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ApplicantIntegrationSoapPortWSDDServiceName = "ApplicantIntegrationSoapPort";

    public java.lang.String getApplicantIntegrationSoapPortWSDDServiceName() {
        return ApplicantIntegrationSoapPortWSDDServiceName;
    }

    public void setApplicantIntegrationSoapPortWSDDServiceName(java.lang.String name) {
        ApplicantIntegrationSoapPortWSDDServiceName = name;
    }

    public gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.ApplicantIntegrationPortType getApplicantIntegrationSoapPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ApplicantIntegrationSoapPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getApplicantIntegrationSoapPort(endpoint);
    }

    public gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.ApplicantIntegrationPortType getApplicantIntegrationSoapPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.ApplicantIntegrationSoapBindingStub _stub = new gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.ApplicantIntegrationSoapBindingStub(portAddress, this);
            _stub.setPortName(getApplicantIntegrationSoapPortWSDDServiceName());
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
            if (gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.ApplicantIntegrationPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.ApplicantIntegrationSoapBindingStub _stub = new gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.ApplicantIntegrationSoapBindingStub(new java.net.URL(ApplicantIntegrationSoapPort_address), this);
                _stub.setPortName(getApplicantIntegrationSoapPortWSDDServiceName());
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
        if ("ApplicantIntegrationSoapPort".equals(inputPortName)) {
            return getApplicantIntegrationSoapPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "ApplicantIntegrationServices");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("ApplicantIntegrationSoapPort"));
        }
        return ports.iterator();
    }

}
