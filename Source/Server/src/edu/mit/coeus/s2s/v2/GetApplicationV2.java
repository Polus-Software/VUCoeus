/*
 * GetApplication.java
 *
 * Created on March 16, 2005, 5:09 PM
 */

package edu.mit.coeus.s2s.v2;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.GetApplication;
import edu.mit.coeus.s2s.bean.ApplicationInfoBean;
import edu.mit.coeus.s2s.bean.S2SHeader;
import edu.mit.coeus.s2s.util.Converter;
import edu.mit.coeus.s2s.v2.util.S2SStub;
import edu.mit.coeus.utils.S2SConstants;
import edu.mit.coeus.utils.UtilFactory;
import gov.grants.apply.services.applicantwebservices_v2.ApplicantWebServicesPortType;
import gov.grants.apply.services.applicantwebservices_v2.GetApplicationListRequest;
import gov.grants.apply.services.applicantwebservices_v2.GetApplicationListResponse;
import gov.grants.apply.system.grantscommonelements_v1.ApplicationFilter;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 *
 * @author  geot
 */
public class GetApplicationV2 extends GetApplication {
    
    /** Creates a new instance of GetApplication */
    public GetApplicationV2() {
    }
    
    public ApplicationInfoBean getApplication(S2SHeader headerParam) throws Exception{
        ApplicationInfoBean[] list = getApplicationList(headerParam);
        if(list.length!=1)
            throw new CoeusException("Grants.gov web service returns "+list.length+
                    " application(s) for Proposal number :"+headerParam.getSubmissionTitle());
        return list[0];
    }
    
    public synchronized ApplicationInfoBean[] getApplicationList(S2SHeader headerParam) throws Exception{
//        ApplicantIntegrationSoapBindingStub stub = SoapUtils.getApplicantSoapStub();
        /*
         *Implement multi campus. Pass duns number to create the stub with appropriate keystore
         *
         */
        ApplicantWebServicesPortType stub = new S2SStub().getApplicantSoapStub(headerParam.getSubmissionTitle());
        List<GetApplicationListResponse.ApplicationInfo> appInfoList = null;
        try{
	        GetApplicationListResponse appRes = stub.getApplicationList(createAppListRequest(headerParam));
	        appInfoList = appRes.getApplicationInfo();
        }catch(Exception ex){
        	ex.printStackTrace();
        }
        int len = appInfoList==null?0:appInfoList.size();
        ApplicationInfoBean [] appList = new ApplicationInfoBean[len];
        for(int i=0;i<len;i++){
            appList[i] = wrapResponse(appInfoList.get(i));
        }
       return appList; 
    }
    
    private static ApplicationInfoBean wrapResponse(GetApplicationListResponse.ApplicationInfo response){
        ApplicationInfoBean info = new ApplicationInfoBean();
        info.setAgencyTrackingNumber(UtilFactory.convertNull(response.getAgencyTrackingNumber()));
        info.setCfdaNumber(UtilFactory.convertNull(response.getCFDANumber()));
        info.setCompetitionID(UtilFactory.convertNull(response.getCompetitionID()));
        info.setGrantsGovTrackingNumber(UtilFactory.convertNull(response.getGrantsGovTrackingNumber()));
        info.setOpportunityID(response.getFundingOpportunityNumber());
        
        GregorianCalendar cal = response.getReceivedDateTime().toGregorianCalendar();
        if(cal!=null){
            cal.setTimeZone(TimeZone.getTimeZone(S2SConstants.EST_TIMEZONE_ID));
            info.setReceivedDateTime(Converter.convertCal2Timestamp(cal));
        }
        info.setStatus(UtilFactory.convertNull(response.getGrantsGovApplicationStatus().value()));
        cal = response.getStatusDateTime().toGregorianCalendar();
        if(cal!=null){
            cal.setTimeZone(TimeZone.getTimeZone(S2SConstants.EST_TIMEZONE_ID));
            info.setStatusDate(Converter.convertCal2Timestamp(cal));
        }
        info.setSubmissionTitle(UtilFactory.convertNull(response.getSubmissionTitle()));
        return info;
    }
    /**
     * Creates an application list request
     *
     * @param form the GetApplicationListForm
     * @return the _GetApplicationListRequest
     */
    private static GetApplicationListRequest createAppListRequest(S2SHeader headerParam) {
        GetApplicationListRequest applicationListRequest = new GetApplicationListRequest();
        List<ApplicationFilter> applicationFilterList = applicationListRequest.getApplicationFilter();
//        if (headerParam.getStatus() != null) {
//            ApplicationFilter applicationFilter_status = new ApplicationFilter();
//            applicationFilter_status.setFilter("Status");
//            applicationFilter_status.setFilterValue(headerParam.getStatus());
////            UtilFactory.log("Adding a Status filter of: "+headerParam.getStatus());
//            applicationFilterList.add(applicationFilter_status);
//        }
        
        if (headerParam.getCfdaNumber() != null && !headerParam.getCfdaNumber().equals("")) {
            ApplicationFilter applicationFilter_CFDANum = new ApplicationFilter();
            applicationFilter_CFDANum.setFilter("CFDANumber");
            applicationFilter_CFDANum.setFilterValue(headerParam.getCfdaNumber());
//            UtilFactory.log("Adding a CFDANum filter of: "+ headerParam.getCfdaNumber());
            applicationFilterList.add(applicationFilter_CFDANum);
        }
        
        if (headerParam.getOpportunityId() != null) {
            ApplicationFilter applicationFilter_OpportunityID = new ApplicationFilter();
            applicationFilter_OpportunityID.setFilter("OpportunityID");
            applicationFilter_OpportunityID.setFilterValue(headerParam.getOpportunityId());
//            UtilFactory.log("Adding a OppId filter of: "+ headerParam.getOpportunityId());
            applicationFilterList.add(applicationFilter_OpportunityID);
        }
        
        if (headerParam.getSubmissionTitle() != null) {
            ApplicationFilter applicationFilter_SubmTittle = new ApplicationFilter();
            applicationFilter_SubmTittle.setFilter("SubmissionTitle");
            applicationFilter_SubmTittle.setFilterValue(headerParam.getSubmissionTitle());
//            UtilFactory.log("Adding a OppId filter of: "+ headerParam.getSubmissionTitle());
            applicationFilterList.add(applicationFilter_SubmTittle);
        }
        return applicationListRequest;
    }
}
