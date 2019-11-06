/*
 * GetAppStatusDetails.java
 *
 * Created on April 26, 2005, 4:00 PM
 */

package edu.mit.coeus.s2s;

import edu.mit.coeus.s2s.bean.S2SHeader;
import edu.mit.coeus.s2s.util.S2SStub;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.ApplicantIntegrationSoapBindingStub;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationStatusDetailRequest;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationStatusDetailResponse;
import gov.grants.apply.soap.util.SoapUtils;
import gov.grants.apply.system.Global_V1_0.StringMin1Max240Type;

/**
 *
 * @author  geot
 */
public class GetAppStatusDetailsV1 extends GetAppStatusDetails{
    
    /** Creates a new instance of GetAppStatusDetails */
    public GetAppStatusDetailsV1() {
    }
    
    public Object getStatusDetails(String ggTrackingId)throws Exception{
        return getStatusDetails(ggTrackingId,null);
    }
    public Object getStatusDetails(String ggTrackingId,String proposalNum)throws Exception{
        
        _GetApplicationStatusDetailRequest request = new _GetApplicationStatusDetailRequest();
		request.setGrants_govTrackingNumber(new StringMin1Max240Type(ggTrackingId.trim()));

//        ApplicantIntegrationSoapBindingStub stub = SoapUtils.getApplicantSoapStub();
        /*
         *Implement multi campus. Pass duns number to create the stub with appropriate keystore
         *
         */
        ApplicantIntegrationSoapBindingStub stub = new S2SStub().getApplicantSoapStub(proposalNum);
        _GetApplicationStatusDetailResponse getApplicationStatusDetailResponse = stub
				.getApplicationStatusDetail(request);
        Object statusDetail = getApplicationStatusDetailResponse
				.getDetailedStatus();
        return statusDetail.toString();
    }
}
