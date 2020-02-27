
package gov.grants.apply.services.applicantwebservices_v2;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "ApplicantWebServices-V2.0", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0", wsdlLocation = "classpath:edu/mit/coeus/s2s/v2/wsdl/ApplicantWebServices-V2.0.wsdl")
public class ApplicantWebServicesV20
    extends Service
{

    private final static URL APPLICANTWEBSERVICESV20_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(gov.grants.apply.services.applicantwebservices_v2.ApplicantWebServicesV20 .class.getName());

    static {
        APPLICANTWEBSERVICESV20_WSDL_LOCATION = gov.grants.apply.services.applicantwebservices_v2.ApplicantWebServicesV20.class.getClassLoader().getResource("edu/mit/coeus/s2s/v2/wsdl/ApplicantWebServices-V2_0.wsdl");
    }

    public ApplicantWebServicesV20(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation,serviceName);
    }

    public ApplicantWebServicesV20() {
        super(APPLICANTWEBSERVICESV20_WSDL_LOCATION, new QName("http://apply.grants.gov/services/ApplicantWebServices-V2.0", "ApplicantWebServices-V2.0"));
    	//super(null,new QName("http://apply.grants.gov/services/ApplicantWebServices-V2.0", "ApplicantWebServices-V2.0"));
    }

    /**
     * 
     * @return
     *     returns ApplicantWebServicesPortType
     */
    @WebEndpoint(name = "ApplicantWebServicesSoapPort")
    public ApplicantWebServicesPortType getApplicantWebServicesSoapPort() {
        return super.getPort(new QName("http://apply.grants.gov/services/ApplicantWebServices-V2.0", "ApplicantWebServicesSoapPort"), ApplicantWebServicesPortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ApplicantWebServicesPortType
     */
    @WebEndpoint(name = "ApplicantWebServicesSoapPort")
    public ApplicantWebServicesPortType getApplicantWebServicesSoapPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://apply.grants.gov/services/ApplicantWebServices-V2.0", "ApplicantWebServicesSoapPort"), ApplicantWebServicesPortType.class, features);
    }

}
