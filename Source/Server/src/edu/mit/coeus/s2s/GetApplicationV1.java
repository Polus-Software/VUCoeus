/*
 * GetApplication.java
 *
 * Created on March 16, 2005, 5:09 PM
 */

package edu.mit.coeus.s2s;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.bean.ApplicationInfoBean;
import edu.mit.coeus.s2s.bean.S2SHeader;
import edu.mit.coeus.utils.UtilFactory;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.ApplicantIntegrationSoapBindingStub;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.ApplicationInformationType;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationListRequest;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationListRequest_ApplicationFilter;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationListRequest_ApplicationFilter_Filter;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationListResponse;
import gov.grants.apply.soap.util.SoapUtils;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import edu.mit.coeus.s2s.util.Converter;
import edu.mit.coeus.s2s.util.S2SStub;
import edu.mit.coeus.utils.S2SConstants;
import java.util.TimeZone;

/**
 *
 * @author  geot
 */
public class GetApplicationV1 extends GetApplication{
    
    public ApplicationInfoBean getApplication(S2SHeader headerParam) throws Exception{
        ApplicationInfoBean[] list = getApplicationList(headerParam);
        if(list.length!=1)
            throw new CoeusException("Grants.gov web service returns "+list.length+
                    " application(s) for Proposal number :"+headerParam.getSubmissionTitle());
        return list[0];
    }
    
    public ApplicationInfoBean[] getApplicationList(S2SHeader headerParam) throws Exception{
//        ApplicantIntegrationSoapBindingStub stub = SoapUtils.getApplicantSoapStub();
        /*
         *Implement multi campus. Pass duns number to create the stub with appropriate keystore
         *
         */
        ApplicantIntegrationSoapBindingStub stub = new S2SStub().getApplicantSoapStub(headerParam.getSubmissionTitle());
        _GetApplicationListResponse appRes = stub.getApplicationList(createAppListRequest(headerParam));
        ApplicationInformationType[] appInfoList = appRes.getApplicationInformation();
        int len = appInfoList==null?0:appInfoList.length;
        ApplicationInfoBean [] appList = new ApplicationInfoBean[len];
        for(int i=0;i<len;i++){
            appList[i] = wrapResponse(appRes.getApplicationInformation(i));
        }
       return appList; 
    }
    
    private static ApplicationInfoBean wrapResponse(ApplicationInformationType response){
        ApplicationInfoBean info = new ApplicationInfoBean();
        info.setAgencyTrackingNumber(UtilFactory.convertNull(response.getAgencyTrackingNumber()));
        info.setCfdaNumber(UtilFactory.convertNull(response.getCFDANumber()));
        info.setCompetitionID(UtilFactory.convertNull(response.getCompetitionID()));
        info.setGrantsGovTrackingNumber(UtilFactory.convertNull(response.getGrants_govTrackingNumber()));
        info.setOpportunityID(response.getOpportunityID().getValue());
        
        Calendar cal = response.getReceivedDateTime();
        if(cal!=null){
            cal.setTimeZone(TimeZone.getTimeZone(S2SConstants.EST_TIMEZONE_ID));
            info.setReceivedDateTime(Converter.convertCal2Timestamp(cal));
        }
        info.setStatus(UtilFactory.convertNull(response.getGrantsGovApplicationStatus()));
        cal = response.getStatusDateTime();
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
    private static _GetApplicationListRequest createAppListRequest(S2SHeader headerParam) {
        List list = new ArrayList();
//        UtilFactory.log("Getting Filter Values");
        if (headerParam.getStatus() != null) {
            _GetApplicationListRequest_ApplicationFilter applicationFilter_status = new _GetApplicationListRequest_ApplicationFilter();
            applicationFilter_status.setFilter(
                    _GetApplicationListRequest_ApplicationFilter_Filter.Status);
            applicationFilter_status.setFilterValue(headerParam.getStatus());
//            UtilFactory.log("Adding a Status filter of: "+headerParam.getStatus());
            list.add(applicationFilter_status);
        }
        
        if (headerParam.getCfdaNumber() != null) {
            _GetApplicationListRequest_ApplicationFilter applicationFilter_CFDANum = new _GetApplicationListRequest_ApplicationFilter();
            applicationFilter_CFDANum.setFilter(_GetApplicationListRequest_ApplicationFilter_Filter.CFDANumber);
            applicationFilter_CFDANum.setFilterValue(headerParam.getCfdaNumber());
//            UtilFactory.log("Adding a CFDANum filter of: "+ headerParam.getCfdaNumber());
            list.add(applicationFilter_CFDANum);
        }
        
        if (headerParam.getOpportunityId() != null) {
            _GetApplicationListRequest_ApplicationFilter applicationFilter_OpportunityID = new _GetApplicationListRequest_ApplicationFilter();
            applicationFilter_OpportunityID.setFilter(_GetApplicationListRequest_ApplicationFilter_Filter.OpportunityID);
            applicationFilter_OpportunityID.setFilterValue(headerParam.getOpportunityId());
//            UtilFactory.log("Adding a OppId filter of: "+ headerParam.getOpportunityId());
            list.add(applicationFilter_OpportunityID);
        }
        
        if (headerParam.getSubmissionTitle() != null) {
            _GetApplicationListRequest_ApplicationFilter applicationFilter_SubmTittle = new _GetApplicationListRequest_ApplicationFilter();
            applicationFilter_SubmTittle.setFilter(_GetApplicationListRequest_ApplicationFilter_Filter.SubmissionTitle);
            applicationFilter_SubmTittle.setFilterValue(headerParam.getSubmissionTitle());
//            UtilFactory.log("Adding a OppId filter of: "+ headerParam.getSubmissionTitle());
            list.add(applicationFilter_SubmTittle);
        }

        _GetApplicationListRequest getApplicationListRequest = new _GetApplicationListRequest();
        getApplicationListRequest.setApplicationFilter((
                    _GetApplicationListRequest_ApplicationFilter[]) 
                        list.toArray(
                            new _GetApplicationListRequest_ApplicationFilter[0]));
        return getApplicationListRequest;
    }
    public static void main (String args[]){
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+00:00"));
        Calendar c = Calendar.getInstance();
        System.out.println("before settig time zone=> "+Converter.convertCal2Timestamp(c));
        c.setTimeZone(TimeZone.getTimeZone(S2SConstants.EST_TIMEZONE_ID));
        System.out.println("after settig time zone=> "+Converter.convertCal2Timestamp(c));
    }
}
