/**
 * ApplicantIntegrationPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0;

public interface ApplicantIntegrationPortType extends java.rmi.Remote {
    public gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetOpportunityListResponse getOpportunityList(gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetOpportunityListRequest getOpportunityListRequest) throws java.rmi.RemoteException;
    public gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._SubmitApplicationResponse submitApplication(gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._SubmitApplicationRequest submitApplicationRequest) throws java.rmi.RemoteException;
    public gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationListResponse getApplicationList(gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationListRequest getApplicationListRequest) throws java.rmi.RemoteException;
    public gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationStatusDetailResponse getApplicationStatusDetail(gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationStatusDetailRequest getApplicationStatusDetailRequest) throws java.rmi.RemoteException;
}
