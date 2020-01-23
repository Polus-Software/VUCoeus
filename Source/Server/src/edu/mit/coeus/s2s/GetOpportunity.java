/*
 * GetOpportunity.java
 *
 * Created on March 11, 2005, 1:33 PM
 */

package edu.mit.coeus.s2s;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.bean.S2SHeader;
import edu.mit.coeus.s2s.v2.GetOpportunityV2;
import edu.mit.coeus.s2s.validator.S2SValidationException;
import edu.mit.coeus.utils.UtilFactory;
import gov.grants.apply.soap.util.SoapUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author  geot
 */
public abstract class GetOpportunity {
    
    /** Creates a new instance of GetOpportunity */
    protected GetOpportunity() {
    }
    public static GetOpportunity getInstance(){
    	try {
			if(SoapUtils.getProperty("SOAP_SERVER_VERSION")!=null && SoapUtils.getProperty("SOAP_SERVER_VERSION").equals("V2")){
				return new GetOpportunityV2();
			}else{
				return new GetOpportunityV1();
			}
		} catch (IOException e) {
			UtilFactory.log(e.getMessage(),e,"GetOpportunity", "getInstance");
			return new GetOpportunityV1();
		}

    }
    public abstract ArrayList searchOpportunityList(S2SHeader headerParam) throws S2SValidationException,CoeusException;
    public abstract ArrayList searchOpportunity(S2SHeader headerParam) throws S2SValidationException,CoeusException;
    public abstract ArrayList getOpportunityList(S2SHeader headerParam) throws S2SValidationException,CoeusException;
}