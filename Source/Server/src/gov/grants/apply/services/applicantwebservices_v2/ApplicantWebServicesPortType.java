
package gov.grants.apply.services.applicantwebservices_v2;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "ApplicantWebServicesPortType", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({
    gov.grants.apply.services.applicantwebservices_v2.ObjectFactory.class,
    gov.grants.apply.system.applicantcommonelements_v1.ObjectFactory.class,
    gov.grants.apply.system.grantscommonelements_v1.ObjectFactory.class,
    org.xmlsoap.schemas.wsdl.ObjectFactory.class,
    org.xmlsoap.schemas.wsdl.soap.ObjectFactory.class,
    gov.grants.apply.system.grantscommontypes_v1.ObjectFactory.class
})
public interface ApplicantWebServicesPortType {


    /**
     * 
     * @param getOpportunitiesRequest
     * @return
     *     returns gov.grants.apply.services.applicantwebservices_v2.GetOpportunitiesResponse
     * @throws ErrorMessage
     */
    @WebMethod(operationName = "GetOpportunities", action = "http://ws.grants.gov:446/grantsws-applicant/services/v2/ApplicantWebServicesSoapPort/GetOpportunities")
    @WebResult(name = "GetOpportunitiesResponse", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0", partName = "GetOpportunitiesResponse")
    public GetOpportunitiesResponse getOpportunities(
        @WebParam(name = "GetOpportunitiesRequest", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0", partName = "GetOpportunitiesRequest")
        GetOpportunitiesRequest getOpportunitiesRequest)
        throws ErrorMessage
    ;

    /**
     * 
     * @param submitApplicationRequest
     * @return
     *     returns gov.grants.apply.services.applicantwebservices_v2.SubmitApplicationResponse
     * @throws ErrorMessage
     */
    @WebMethod(operationName = "SubmitApplication", action = "http://ws.grants.gov:446/grantsws-applicant/services/v2/ApplicantWebServicesSoapPort/SubmitApplication")
    @WebResult(name = "SubmitApplicationResponse", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0", partName = "SubmitApplicationResponse")
    public SubmitApplicationResponse submitApplication(
        @WebParam(name = "SubmitApplicationRequest", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0", partName = "SubmitApplicationRequest")
        SubmitApplicationRequest submitApplicationRequest)
        throws ErrorMessage
    ;

    /**
     * 
     * @param submitApplicationAsThirdPartyRequest
     * @return
     *     returns gov.grants.apply.services.applicantwebservices_v2.SubmitApplicationAsThirdPartyResponse
     * @throws ErrorMessage
     */
    @WebMethod(operationName = "SubmitApplicationAsThirdParty", action = "http://ws.grants.gov:446/grantsws-applicant/services/v2/ApplicantWebServicesSoapPort/SubmitApplicationAsThirdParty")
    @WebResult(name = "SubmitApplicationAsThirdPartyResponse", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0", partName = "SubmitApplicationAsThirdPartyResponse")
    public SubmitApplicationAsThirdPartyResponse submitApplicationAsThirdParty(
        @WebParam(name = "SubmitApplicationAsThirdPartyRequest", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0", partName = "SubmitApplicationAsThirdPartyRequest")
        SubmitApplicationAsThirdPartyRequest submitApplicationAsThirdPartyRequest)
        throws ErrorMessage
    ;

    /**
     * 
     * @param getApplicationListRequest
     * @return
     *     returns gov.grants.apply.services.applicantwebservices_v2.GetApplicationListResponse
     * @throws ErrorMessage
     */
    @WebMethod(operationName = "GetApplicationList", action = "http://ws.grants.gov:446/grantsws-applicant/services/v2/ApplicantWebServicesSoapPort/GetApplicationList")
    @WebResult(name = "GetApplicationListResponse", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0", partName = "GetApplicationListResponse")
    public GetApplicationListResponse getApplicationList(
        @WebParam(name = "GetApplicationListRequest", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0", partName = "GetApplicationListRequest")
        GetApplicationListRequest getApplicationListRequest)
        throws ErrorMessage
    ;

    /**
     * 
     * @param getApplicationListAsThirdPartyRequest
     * @return
     *     returns gov.grants.apply.services.applicantwebservices_v2.GetApplicationListAsThirdPartyResponse
     * @throws ErrorMessage
     */
    @WebMethod(operationName = "GetApplicationListAsThirdParty", action = "http://ws.grants.gov:446/grantsws-applicant/services/v2/ApplicantWebServicesSoapPort/GetApplicationListAsThirdParty")
    @WebResult(name = "GetApplicationListAsThirdPartyResponse", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0", partName = "GetApplicationListAsThirdPartyResponse")
    public GetApplicationListAsThirdPartyResponse getApplicationListAsThirdParty(
        @WebParam(name = "GetApplicationListAsThirdPartyRequest", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0", partName = "GetApplicationListAsThirdPartyRequest")
        GetApplicationListAsThirdPartyRequest getApplicationListAsThirdPartyRequest)
        throws ErrorMessage
    ;

    /**
     * 
     * @param getApplicationStatusDetailRequest
     * @return
     *     returns gov.grants.apply.services.applicantwebservices_v2.GetApplicationStatusDetailResponse
     * @throws ErrorMessage
     */
    @WebMethod(operationName = "GetApplicationStatusDetail", action = "http://ws.grants.gov:446/grantsws-applicant/services/v2/ApplicantWebServicesSoapPort/GetApplicationStatusDetail")
    @WebResult(name = "GetApplicationStatusDetailResponse", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0", partName = "GetApplicationStatusDetailResponse")
    public GetApplicationStatusDetailResponse getApplicationStatusDetail(
        @WebParam(name = "GetApplicationStatusDetailRequest", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0", partName = "GetApplicationStatusDetailRequest")
        GetApplicationStatusDetailRequest getApplicationStatusDetailRequest)
        throws ErrorMessage
    ;

    /**
     * 
     * @param getApplicationInfoRequest
     * @return
     *     returns gov.grants.apply.services.applicantwebservices_v2.GetApplicationInfoResponse
     * @throws ErrorMessage
     */
    @WebMethod(operationName = "GetApplicationInfo", action = "http://ws.grants.gov:446/grantsws-applicant/services/v2/ApplicantWebServicesSoapPort/GetApplicationInfo")
    @WebResult(name = "GetApplicationInfoResponse", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0", partName = "GetApplicationInfoResponse")
    public GetApplicationInfoResponse getApplicationInfo(
        @WebParam(name = "GetApplicationInfoRequest", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0", partName = "GetApplicationInfoRequest")
        GetApplicationInfoRequest getApplicationInfoRequest)
        throws ErrorMessage
    ;

    /**
     * 
     * @param getApplicationInfoAsThirdPartyRequest
     * @return
     *     returns gov.grants.apply.services.applicantwebservices_v2.GetApplicationInfoAsThirdPartyResponse
     * @throws ErrorMessage
     */
    @WebMethod(operationName = "GetApplicationInfoAsThirdParty", action = "http://ws.grants.gov:446/grantsws-applicant/services/v2/ApplicantWebServicesSoapPort/GetApplicationInfoAsThirdParty")
    @WebResult(name = "GetApplicationInfoAsThirdPartyResponse", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0", partName = "GetApplicationInfoAsThirdPartyResponse")
    public GetApplicationInfoAsThirdPartyResponse getApplicationInfoAsThirdParty(
        @WebParam(name = "GetApplicationInfoAsThirdPartyRequest", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0", partName = "GetApplicationInfoAsThirdPartyRequest")
        GetApplicationInfoAsThirdPartyRequest getApplicationInfoAsThirdPartyRequest)
        throws ErrorMessage
    ;

    /**
     * 
     * @param authenticateAORRequest
     * @return
     *     returns gov.grants.apply.services.applicantwebservices_v2.AuthenticateAORResponse
     * @throws ErrorMessage
     */
    @WebMethod(operationName = "AuthenticateAOR", action = "http://ws.grants.gov:446/grantsws-applicant/services/v2/ApplicantWebServicesSoapPort/AuthenticateAOR")
    @WebResult(name = "AuthenticateAORResponse", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0", partName = "AuthenticateAORResponse")
    public AuthenticateAORResponse authenticateAOR(
        @WebParam(name = "AuthenticateAORRequest", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0", partName = "AuthenticateAORRequest")
        AuthenticateAORRequest authenticateAORRequest)
        throws ErrorMessage
    ;

    /**
     * 
     * @param getApplicationZipRequest
     * @return
     *     returns gov.grants.apply.services.applicantwebservices_v2.GetApplicationZipResponse
     * @throws ErrorMessage
     */
    @WebMethod(operationName = "GetApplicationZip", action = "https://ws.grants.gov:446/grantsws-agency/services/v2/AgencyWebServicesSoapPort/GetApplicationZip")
    @WebResult(name = "GetApplicationZipResponse", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0", partName = "GetApplicationZipResponse")
    public GetApplicationZipResponse getApplicationZip(
        @WebParam(name = "GetApplicationZipRequest", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0", partName = "GetApplicationZipRequest")
        GetApplicationZipRequest getApplicationZipRequest)
        throws ErrorMessage
    ;

    /**
     * 
     * @param getOpportunitiesPlusCompTitleRequest
     * @return
     *     returns gov.grants.apply.services.applicantwebservices_v2.GetOpportunitiesPlusCompTitleResponse
     * @throws ErrorMessage
     */
    @WebMethod(operationName = "GetOpportunitiesPlusCompTitle", action = "http://ws.grants.gov:446/grantsws-applicant/services/v2/ApplicantWebServicesSoapPort/GetOpportunitiesPlusCompTitle")
    @WebResult(name = "GetOpportunitiesPlusCompTitleResponse", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0", partName = "GetOpportunitiesPlusCompTitleResponse")
    public GetOpportunitiesPlusCompTitleResponse getOpportunitiesPlusCompTitle(
        @WebParam(name = "GetOpportunitiesPlusCompTitleRequest", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0", partName = "GetOpportunitiesPlusCompTitleRequest")
        GetOpportunitiesPlusCompTitleRequest getOpportunitiesPlusCompTitleRequest)
        throws ErrorMessage
    ;

    /**
     * 
     * @param getOpportunitiesExpandedRequest
     * @return
     *     returns gov.grants.apply.services.applicantwebservices_v2.GetOpportunitiesExpandedResponse
     * @throws ErrorMessage
     */
    @WebMethod(operationName = "GetOpportunitiesExpanded", action = "http://ws.grants.gov:446/grantsws-applicant/services/v2/ApplicantWebServicesSoapPort/GetOpportunitiesExpanded")
    @WebResult(name = "GetOpportunitiesExpandedResponse", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0", partName = "GetOpportunitiesExpandedResponse")
    public GetOpportunitiesExpandedResponse getOpportunitiesExpanded(
        @WebParam(name = "GetOpportunitiesExpandedRequest", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0", partName = "GetOpportunitiesExpandedRequest")
        GetOpportunitiesExpandedRequest getOpportunitiesExpandedRequest)
        throws ErrorMessage
    ;

    /**
     * 
     * @param getOpportunityListRequest
     * @return
     *     returns gov.grants.apply.services.applicantwebservices_v2.GetOpportunityListResponse
     * @throws ErrorMessage
     */
    @WebMethod(operationName = "GetOpportunityList", action = "http://ws.grants.gov:446/grantsws-applicant/services/v2/ApplicantWebServicesSoapPort/GetOpportunityList")
    @WebResult(name = "GetOpportunityListResponse", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0", partName = "GetOpportunityListResponse")
    public GetOpportunityListResponse getOpportunityList(
        @WebParam(name = "GetOpportunityListRequest", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0", partName = "GetOpportunityListRequest")
        GetOpportunityListRequest getOpportunityListRequest)
        throws ErrorMessage
    ;

    /**
     * 
     * @param getSubmissionListRequest
     * @return
     *     returns gov.grants.apply.services.applicantwebservices_v2.GetSubmissionListResponse
     * @throws ErrorMessage
     */
    @WebMethod(operationName = "GetSubmissionList", action = "http://ws.grants.gov:446/grantsws-applicant/services/v2/ApplicantWebServicesSoapPort/GetSubmissionList")
    @WebResult(name = "GetSubmissionListResponse", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0", partName = "GetSubmissionListResponse")
    public GetSubmissionListResponse getSubmissionList(
        @WebParam(name = "GetSubmissionListRequest", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0", partName = "GetSubmissionListRequest")
        GetSubmissionListRequest getSubmissionListRequest)
        throws ErrorMessage
    ;

    /**
     * 
     * @param getSubmissionListAsThirdPartyRequest
     * @return
     *     returns gov.grants.apply.services.applicantwebservices_v2.GetSubmissionListAsThirdPartyResponse
     * @throws ErrorMessage
     */
    @WebMethod(operationName = "GetSubmissionListAsThirdParty", action = "http://ws.grants.gov:446/grantsws-applicant/services/v2/ApplicantWebServicesSoapPort/GetSubmissionListAsThirdParty")
    @WebResult(name = "GetSubmissionListAsThirdPartyResponse", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0", partName = "GetSubmissionListAsThirdPartyResponse")
    public GetSubmissionListAsThirdPartyResponse getSubmissionListAsThirdParty(
        @WebParam(name = "GetSubmissionListAsThirdPartyRequest", targetNamespace = "http://apply.grants.gov/services/ApplicantWebServices-V2.0", partName = "GetSubmissionListAsThirdPartyRequest")
        GetSubmissionListAsThirdPartyRequest getSubmissionListAsThirdPartyRequest)
        throws ErrorMessage
    ;

}