/*
 * GetAppStatusDetails.java
 *
 * Created on April 26, 2005, 4:00 PM
 */

package edu.mit.coeus.s2s.v2;

import gov.grants.apply.services.applicantwebservices_v2.ApplicantWebServicesPortType;
import gov.grants.apply.services.applicantwebservices_v2.GetApplicationStatusDetailRequest;
import gov.grants.apply.services.applicantwebservices_v2.GetApplicationStatusDetailResponse;
import edu.mit.coeus.s2s.GetAppStatusDetails;
import edu.mit.coeus.s2s.v2.util.S2SStub;

/** 
 *
 * @author  geot
 */
public class GetAppStatusDetailsV2 extends GetAppStatusDetails{
    
    /** Creates a new instance of GetAppStatusDetails */
    public GetAppStatusDetailsV2() {
    }
    
    public Object getStatusDetails(String ggTrackingId)throws Exception{
        return getStatusDetails(ggTrackingId,null);
    }
    public Object getStatusDetails(String ggTrackingId,String proposalNum) throws Exception{
       
            GetApplicationStatusDetailRequest request = new GetApplicationStatusDetailRequest();
                    request.setGrantsGovTrackingNumber(ggTrackingId.trim());

    //        ApplicantIntegrationSoapBindingStub stub = SoapUtils.getApplicantSoapStub();
            /*
             *Implement multi campus. Pass duns number to create the stub with appropriate keystore
             *
             */
            ApplicantWebServicesPortType stub = new S2SStub().getApplicantSoapStub(proposalNum);
            GetApplicationStatusDetailResponse getApplicationStatusDetailResponse = stub.getApplicationStatusDetail(request);
            Object statusDetail = getApplicationStatusDetailResponse.getDetailedStatus();
            return statusDetail.toString();
    }
}
