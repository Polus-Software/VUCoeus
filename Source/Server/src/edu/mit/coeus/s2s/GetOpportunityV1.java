/*
 * GetOpportunity.java
 *
 * Created on March 11, 2005, 1:33 PM
 */

package edu.mit.coeus.s2s;

import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.bean.S2SHeader;
import edu.mit.coeus.s2s.util.Converter;
import edu.mit.coeus.s2s.util.S2SStub;
import edu.mit.coeus.s2s.validator.S2SValidationException;
import edu.mit.coeus.utils.UtilFactory;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.ApplicantIntegrationSoapBindingStub;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetOpportunityListRequest;
import gov.grants.apply.soap.util.SoapUtils;
import gov.grants.apply.system.Global_V1_0.StringMin1Max100Type;
import gov.grants.apply.system.Global_V1_0.StringMin1Max15Type;
import java.util.ArrayList;
import org.apache.axis.AxisFault;

/**
 *
 * @author  geot
 */
public class GetOpportunityV1 extends GetOpportunity{
    
    /** Creates a new instance of GetOpportunity */
    public GetOpportunityV1() {
    }
    public ArrayList searchOpportunity(S2SHeader headerParam) throws S2SValidationException,CoeusException{
        ArrayList oppList = getOpportunityList(headerParam);
        String cfdaNum = null;
        if(oppList==null && headerParam.getCfdaNumber()!=null){
            headerParam.setOpportunityId(null);
//            headerParam.setCfdaNumber(cfdaNum);
            oppList = getOpportunityList(headerParam);
        }        
//        if(oppList==null){
//            cfdaNum = headerParam.getCfdaNumber();
//            headerParam.setCfdaNumber(null);
//            oppList = getOpportunityList(headerParam);
//        }
        return oppList;
            
    }
    public ArrayList getOpportunityList(S2SHeader headerParam) throws S2SValidationException,CoeusException{
        ApplicantIntegrationSoapBindingStub stub = null;
        try{
            _GetOpportunityListRequest request = new _GetOpportunityListRequest();
//            stub = SoapUtils.getApplicantSoapStub();
        /*
         *Implement multi campus. Pass duns number to create the stub with appropriate keystore
         *
         */
            stub = new S2SStub().getApplicantSoapStub();
            request.setCFDANumber(new StringMin1Max15Type(
            UtilFactory.convertNull(headerParam.getCfdaNumber())));
            request.setOpportunityID(new StringMin1Max100Type(
            UtilFactory.convertNull(headerParam.getOpportunityId())));
            request.setCompetitionID(new StringMin1Max100Type(
            UtilFactory.convertNull(headerParam.getCompetitionId())));
            return Converter.convert2ArrayList(stub.getOpportunityList(request));
        }catch(AxisFault ex){
            UtilFactory.log(ex.getMessage(),ex,"S2SValidator", "validate");
            ex.printStackTrace();
            String faultStr = ex.getFaultReason();
            
            String errMsg= new CoeusMessageResourcesBean().parseMessageKey("exceptionCode.90012");
            String msg = faultStr.indexOf("ConnectException")==-1?faultStr:errMsg;
            throw new CoeusException(msg);
        }catch(Exception ex){
            UtilFactory.log(ex.getMessage(),ex,"S2SValidator", "validate");
            ex.printStackTrace();
            throw new CoeusException(ex.getMessage());
        }
    }

    @Override
    public ArrayList searchOpportunityList(S2SHeader headerParam) throws S2SValidationException, CoeusException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}