/*
 * GetAppStatusDetails.java
 *
 * Created on April 26, 2005, 4:00 PM
 */

package edu.mit.coeus.s2s;

import java.io.IOException;

import edu.mit.coeus.s2s.bean.S2SHeader;
import edu.mit.coeus.s2s.util.S2SStub;
import edu.mit.coeus.s2s.v2.GetAppStatusDetailsV2;
import edu.mit.coeus.s2s.v2.GetOpportunityV2;
import edu.mit.coeus.utils.UtilFactory;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.ApplicantIntegrationSoapBindingStub;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationStatusDetailRequest;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationStatusDetailResponse;
import gov.grants.apply.soap.util.SoapUtils;
import gov.grants.apply.system.Global_V1_0.StringMin1Max240Type;

/**
 *
 * @author  geot
 */
public abstract class GetAppStatusDetails {
    
    /** Creates a new instance of GetAppStatusDetails */
    protected GetAppStatusDetails() {
    }
    
    public static GetAppStatusDetails getInstance(){
    	try {
			if(SoapUtils.getProperty("SOAP_SERVER_VERSION")!=null && SoapUtils.getProperty("SOAP_SERVER_VERSION").equals("V2")){
				return new GetAppStatusDetailsV2();
			}else{
				return new GetAppStatusDetailsV1();
			}
		} catch (IOException e) {
			UtilFactory.log(e.getMessage(),e,"GetOpportunity", "getInstance");
			return new GetAppStatusDetailsV1();
		}

    }
    
    public abstract Object getStatusDetails(String ggTrackingId)throws Exception;
    public abstract Object getStatusDetails(String ggTrackingId,String proposalNum)throws Exception;
}
