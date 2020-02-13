/*
 * GetApplication.java
 *
 * Created on March 16, 2005, 5:09 PM
 */

package edu.mit.coeus.s2s;

import java.io.IOException;

import edu.mit.coeus.s2s.bean.ApplicationInfoBean;
import edu.mit.coeus.s2s.bean.S2SHeader;
import edu.mit.coeus.s2s.v2.GetApplicationV2;
import edu.mit.coeus.utils.UtilFactory;
import gov.grants.apply.soap.util.SoapUtils;

/**
 *
 * @author  geot
 */
public abstract class GetApplication {
    
    /** Creates a new instance of GetApplication */
    protected GetApplication() {
    }
    public static GetApplication getInstance(){
    	try {
			if(SoapUtils.getProperty("SOAP_SERVER_VERSION")!=null && SoapUtils.getProperty("SOAP_SERVER_VERSION").equals("V2")){
				return new GetApplicationV2();
			}else{
				return new GetApplicationV1();
			}
		} catch (IOException e) {
			UtilFactory.log(e.getMessage(),e,"SubmissionEngine", "getInstance");
			return new GetApplicationV1();
		}

    }
    public abstract ApplicationInfoBean[] getApplicationList(S2SHeader headerParam) throws Exception;
    
}
