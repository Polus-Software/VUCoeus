/*
 * GetOpportunity.java
 *
 * Created on March 11, 2005, 1:33 PM
 */

package edu.mit.coeus.s2s.v2;

import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.GetOpportunity;
import edu.mit.coeus.s2s.bean.S2SHeader;
import edu.mit.coeus.s2s.v2.util.Converter;
import edu.mit.coeus.s2s.v2.util.S2SStub;
import edu.mit.coeus.s2s.validator.S2SValidationException;
import edu.mit.coeus.utils.UtilFactory;
import gov.grants.apply.services.applicantwebservices_v2.ApplicantWebServicesPortType;
import gov.grants.apply.services.applicantwebservices_v2.GetOpportunitiesRequest;
import gov.grants.apply.services.applicantwebservices_v2.GetOpportunityListRequest;
import gov.grants.apply.system.applicantcommonelements_v1.OpportunityFilter;

import java.util.ArrayList;
import org.apache.axis.AxisFault;

/**
 *
 * @author  geot
 */
public class GetOpportunityV2 extends GetOpportunity{
    
    /** Creates a new instance of GetOpportunity */
    public GetOpportunityV2() {
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
//        ApplicantIntegrationSoapBindingStub stub = null;
        try{
            GetOpportunitiesRequest request = new GetOpportunitiesRequest();
        /* 
         *Implement multi campus. Pass duns number to create the stub with appropriate keystore
         *
         */
            ApplicantWebServicesPortType stub = new S2SStub().getApplicantSoapStub();
            request.setCFDANumber(headerParam.getCfdaNumber());
            request.setFundingOpportunityNumber(headerParam.getOpportunityId());
            request.setCompetitionID(headerParam.getCompetitionId());
            return Converter.convert2ArrayList(stub.getOpportunities(request));
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
  
    public ArrayList searchOpportunityList(S2SHeader headerParam) throws S2SValidationException, CoeusException {
        ArrayList oppList = getOpportunityListV2(headerParam);
        String cfdaNum = null;
        if(oppList==null && headerParam.getCfdaNumber()!=null){
            headerParam.setOpportunityId(null);
            oppList = getOpportunityListV2(headerParam);
        }
        return oppList;
    }
        public ArrayList getOpportunityListV2(S2SHeader headerParam) throws S2SValidationException,CoeusException{
        try{            
            GetOpportunityListRequest request = new GetOpportunityListRequest();
            OpportunityFilter opportunityFilter = new OpportunityFilter();
            opportunityFilter.setCFDANumber(headerParam.getCfdaNumber());
            opportunityFilter.setCompetitionID(headerParam.getCompetitionId());
            opportunityFilter.setFundingOpportunityNumber(headerParam.getOpportunityId());
            request.setOpportunityFilter(opportunityFilter);           
            ApplicantWebServicesPortType stub = new S2SStub().getApplicantSoapStub();
            return Converter.convert2ArrayList(stub.getOpportunityList(request));            
        }catch(AxisFault ex){
            UtilFactory.log(ex.getMessage(),ex,"S2SValidator", "getOpportunityListV2");
            ex.printStackTrace();
            String faultStr = ex.getFaultReason();
            
            String errMsg= new CoeusMessageResourcesBean().parseMessageKey("exceptionCode.90012");
            String msg = faultStr.indexOf("ConnectException")==-1?faultStr:errMsg;
            throw new CoeusException(msg);
        }catch(Exception ex){
            UtilFactory.log(ex.getMessage(),ex,"GetOpportunityV2", "getOpportunityListV2");
            ex.printStackTrace();
            throw new CoeusException(ex.getMessage());
        }
    }
}