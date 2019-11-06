/*
 * GetSubmissionV2.java
 *
 * Created on June 16, 2018, 5:09 PM
 */

package edu.mit.coeus.s2s.v2;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.GetSubmission;
import edu.mit.coeus.s2s.bean.SubmissionInfoBean;
import edu.mit.coeus.s2s.bean.S2SHeader;
import edu.mit.coeus.s2s.util.Converter;
import edu.mit.coeus.s2s.v2.util.S2SStub;
import edu.mit.coeus.utils.S2SConstants;
import edu.mit.coeus.utils.UtilFactory;
import gov.grants.apply.services.applicantwebservices_v2.ApplicantWebServicesPortType;
import gov.grants.apply.services.applicantwebservices_v2.GetSubmissionListRequest;
import gov.grants.apply.services.applicantwebservices_v2.GetSubmissionListResponse;
import gov.grants.apply.system.applicantcommonelements_v1.SubmissionDetails;
import gov.grants.apply.system.applicantcommonelements_v1.SubmissionFilter;
import gov.grants.apply.system.applicantcommonelements_v1.SubmissionFilterType;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 *
 * @author  farsana
 */
public class GetSubmissionV2 extends GetSubmission {
    
    /** Creates a new instance of GetSubmission */
    public GetSubmissionV2() {
    }
    
    public SubmissionInfoBean getSubmission(S2SHeader headerParam) throws Exception{
        SubmissionInfoBean[] list = getSubmissionList(headerParam);
        if(list.length!=1)
            throw new CoeusException("Grants.gov web service returns "+list.length+
                    " submission(s) for Proposal number :"+headerParam.getSubmissionTitle());
        return list[0];
    }
    
    public synchronized SubmissionInfoBean[] getSubmissionList(S2SHeader headerParam) throws Exception{
//        ApplicantIntegrationSoapBindingStub stub = SoapUtils.getApplicantSoapStub();
        /*
         *Implement multi campus. Pass duns number to create the stub with appropriate keystore
         *
         */
        ApplicantWebServicesPortType stub = new S2SStub().getApplicantSoapStub(headerParam.getSubmissionTitle());
        List<SubmissionDetails> appInfoList = null;
        try{
	        GetSubmissionListResponse appRes = stub.getSubmissionList(createSubListRequest(headerParam));
	        appInfoList = appRes.getSubmissionDetails();
        }catch(Exception ex){
        	ex.printStackTrace();
        }
        int len = appInfoList==null?0:appInfoList.size();
        SubmissionInfoBean [] appList = new SubmissionInfoBean[len];
        for(int i=0;i<len;i++){
            appList[i] = wrapResponse(appInfoList.get(i));
        }
       return appList; 
    }
    
    private static SubmissionInfoBean wrapResponse(SubmissionDetails response){
        SubmissionInfoBean info = new SubmissionInfoBean();
        info.setAgencyTrackingNumber(UtilFactory.convertNull(response.getAgencyTrackingNumber()));
        info.setPackageID(UtilFactory.convertNull(response.getPackageID()));
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
     * Creates an submission list request
     *
     * @param form the GetSubmissionListForm
     * @return the _GetSubmissionListRequest
     */
    private static GetSubmissionListRequest createSubListRequest(S2SHeader headerParam) {
        GetSubmissionListRequest submissionListRequest = new GetSubmissionListRequest();
        List<SubmissionFilter> submissionFilterList = submissionListRequest.getSubmissionFilter();
        
        if (headerParam.getPackageId()!= null && !headerParam.getPackageId().equals("")) {
            SubmissionFilter submissionFilter_PackageId = new SubmissionFilter();
            submissionFilter_PackageId.setType(SubmissionFilterType.PACKAGE_ID);
            submissionFilter_PackageId.setValue(headerParam.getPackageId());
//            UtilFactory.log("Adding a CFDANum filter of: "+ headerParam.getCfdaNumber());
            submissionFilterList.add(submissionFilter_PackageId);
        }
        
        if (headerParam.getOpportunityId() != null) {
            SubmissionFilter submissionFilter_OpportunityID = new SubmissionFilter();
            submissionFilter_OpportunityID.setType(SubmissionFilterType.FUNDING_OPPORTUNITY_NUMBER);
            submissionFilter_OpportunityID.setValue(headerParam.getOpportunityId());
//            UtilFactory.log("Adding a OppId filter of: "+ headerParam.getOpportunityId());
            submissionFilterList.add(submissionFilter_OpportunityID);
        }
        
        if (headerParam.getGrantsGovTrackingNum()!= null) {
            SubmissionFilter submissionFilter_GrantsGovTrackingNum = new SubmissionFilter();
            submissionFilter_GrantsGovTrackingNum.setType(SubmissionFilterType.GRANTS_GOV_TRACKING_NUMBER);
            submissionFilter_GrantsGovTrackingNum.setValue(headerParam.getGrantsGovTrackingNum());
//            UtilFactory.log("Adding a OppId filter of: "+ headerParam.getSubmissionTitle());
            submissionFilterList.add(submissionFilter_GrantsGovTrackingNum);
        }
        
        if (headerParam.getSubmissionTitle() != null) {
            SubmissionFilter submissionFilter_SubmTittle = new SubmissionFilter();
            submissionFilter_SubmTittle.setType(SubmissionFilterType.SUBMISSION_TITLE);
            submissionFilter_SubmTittle.setValue(headerParam.getSubmissionTitle());
//            UtilFactory.log("Adding a OppId filter of: "+ headerParam.getSubmissionTitle());
            submissionFilterList.add(submissionFilter_SubmTittle);
        }
        
        if (headerParam.getStatus()!= null) {
            SubmissionFilter submissionFilter_Status = new SubmissionFilter();
            submissionFilter_Status.setType(SubmissionFilterType.STATUS);
            submissionFilter_Status.setValue(headerParam.getStatus());
//            UtilFactory.log("Adding a OppId filter of: "+ headerParam.getSubmissionTitle());
            submissionFilterList.add(submissionFilter_Status);
        }
        return submissionListRequest;
    }
}
