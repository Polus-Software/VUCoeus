/*
 * ResponseFormater.java
 *
 * Created on October 19, 2004, 2:13 PM
 */

package edu.mit.coeus.s2s;

import edu.mit.coeus.s2s.bean.S2SSubmissionStatusBean;
import edu.mit.coeus.utils.UtilFactory;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.ApplicationInformationType;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.OpportunityInformationType;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationListResponse;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetOpportunityListResponse;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._SubmitApplicationResponse;
import gov.grants.apply.struts.util.Utils;
import gov.grants.apply.valueObject.ApplicationListInformation;
import gov.grants.apply.valueObject.GetOpportunityListInformation;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author  geot
 */
public class ReplyFormater {
//    private UtilFactory UtilFactory;
    /** Creates a new instance of ResponseFormater */
    public ReplyFormater() {
//        UtilFactory = new UtilFactory();
    }
    /**
     *
     * Formats a reply
     *
     * @param the getApplicationListResponse
     * @return An List of the ApplicationInformationType
     * @throws Exception
     */
    public List formatReply(
    _GetApplicationListResponse getApplicationListResponse)
    throws Exception {
        ArrayList applicationList = new ArrayList();
        
        ApplicationInformationType applicationInformationType = null;
        
        if (getApplicationListResponse != null) {
            ApplicationInformationType[] application = getApplicationListResponse
            .getApplicationInformation();
            
            if (application != null && application.length > 0) {
                UtilFactory.log("The server returned " + " " + application.length
                + " applications");
                for (int i = 0; i < application.length; i++) {
                    applicationList.add(getApplicationInformationType(application[i]));
                }
            }
        }
        return applicationList;
    }
    public ApplicationListInformation getApplicationInformationType(ApplicationInformationType appInfoType)
                throws Exception{
        ApplicationListInformation wrapper = new ApplicationListInformation();
        if (appInfoType.getCFDANumber() != null) {
            wrapper.setCFDANumber(appInfoType.getCFDANumber()
            .getValue());
        }
        if (appInfoType.getOpportunityID() != null) {
            wrapper.setOpportunityID(appInfoType
            .getOpportunityID().getValue());
        }
        if (appInfoType.getCompetitionID() != null) {
            wrapper.setCompetitionID(appInfoType.getCompetitionID().getValue());
        }
        if (appInfoType.getGrants_govTrackingNumber() != null) {
            wrapper.setGrantsGovTrackingNumber(appInfoType
            .getGrants_govTrackingNumber().getValue());
        }
        if (appInfoType.getGrantsGovApplicationStatus() != null) {
            wrapper.setStatus(appInfoType
            .getGrantsGovApplicationStatus().getValue());
        }
        if (appInfoType.getStatusDateTime() != null) {
            wrapper.setStatusDate(Utils.formatDate(appInfoType
            .getStatusDateTime().getTime()));
        }
        if (appInfoType.getReceivedDateTime() != null) {
            wrapper.setReceivedDateTime(Utils
            .formatDate(appInfoType
            .getReceivedDateTime().getTime()));
            
        }
        if (appInfoType.getAgencyTrackingNumber() != null) {
            wrapper.setAgencyTrackingNumber(appInfoType
            .getAgencyTrackingNumber().getValue());
        }
        
        return wrapper;
        
    }
        
        
        /**
	 *
	 * Retrieves opportunities.
	 *
	 * @param response the GetOpportunityListResponse.
	 * @return a list of OpportunityInformationType.
	 * @throws Exception
	 */
	//===================================================
	// Method to add the data to the list
	//===================================================
	public List formatReply(_GetOpportunityListResponse response)
		throws Exception {

		List al = new ArrayList();
		OpportunityInformationType oit = new OpportunityInformationType();

		if (response != null) {
			OpportunityInformationType[] oitL =
				response.getOpportunityInformation();
			if (oitL != null) {
				for (int i = 0; i < oitL.length; i++) {

					GetOpportunityListInformation GOLI =
						new GetOpportunityListInformation();

					GOLI.setOpportunity_id(
						oitL[i].getOpportunityID().getValue());
					GOLI.setOpportunity_title(
						oitL[i].getOpportunityTitle().toString());

					if (oitL[i].getOpeningDate() != null) {
						GOLI.setOpening_date(
							Utils.formatDate(
								oitL[i].getOpeningDate().getTime()).substring(
								0,
								8));
					} else {
						GOLI.setOpening_date("");
					}
					if (oitL[i].getClosingDate() != null) {
						GOLI.setClosing_date(
							Utils.formatDate(
								oitL[i].getClosingDate().getTime()).substring(
								0,
								8));
					} else {
						GOLI.setClosing_date("");
					}
					if (oitL[i].getCFDANumber().getValue() != null) {
						GOLI.setCfda_number(oitL[i].getCFDANumber().getValue());
					} else {
						GOLI.setCfda_number("");
					}
					if (oitL[i].getCompetitionID().getValue() != null) {
						GOLI.setCompetition_id(
							oitL[i].getCompetitionID().getValue());
					} else {
						GOLI.setCompetition_id("");
					}
					if (oitL[i].getSchemaURL() != null) {
						GOLI.setSchema_url(oitL[i].getSchemaURL());
					} else {
						GOLI.setSchema_url("");
					}
					if (oitL[i].getInstructionURL() != null) {
						GOLI.setInstruction_url(oitL[i].getInstructionURL());
					} else {
						GOLI.setInstruction_url("");
					}
					al.add(GOLI);
				}
			}
		}
		return al;
	}
	public S2SSubmissionStatusBean formatReply(_SubmitApplicationResponse response)
		throws Exception {
            return null;
        }
}
