/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeus.s2s;

import edu.mit.coeus.s2s.v2.util.S2SStub;
import edu.mit.coeus.utils.UtilFactory;
import gov.grants.apply.services.applicantwebservices_v2.ApplicantWebServicesPortType;
import gov.grants.apply.services.applicantwebservices_v2.GetApplicationInfoRequest;
import gov.grants.apply.services.applicantwebservices_v2.GetApplicationInfoResponse;

/**
 *
 * @author anishk
 */
public class GetApplicationInfo {

    protected GetApplicationInfo() {
    }

    public static GetApplicationInfo getInstance() {
        try {
            return new GetApplicationInfo();
        } catch (Exception e) {
            UtilFactory.log(e.getMessage(), e, "GetApplicationInfo", "getInstance");
            return new GetApplicationInfo();
        }
    }

    public Object getStatusDetails(String ggTrackingId) throws Exception {
        return getStatusDetails(ggTrackingId, null);
    }

    public Object getStatusDetails(String ggTrackingId, String proposalNum) throws Exception {

        GetApplicationInfoRequest request = new GetApplicationInfoRequest();
        request.setGrantsGovTrackingNumber(ggTrackingId.trim());
        ApplicantWebServicesPortType stub = new S2SStub().getApplicantSoapStub(proposalNum);
        GetApplicationInfoResponse getApplicationInfoResponse = stub.getApplicationInfo(request);
        Object statusDetail = getApplicationInfoResponse.getStatusDetail();
        return statusDetail.toString();
    }

}
