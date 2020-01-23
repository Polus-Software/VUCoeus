/*
 * ApplicationListInformation.java
 *
 * Created on 6/29/2004
 *
 * @author  Feng Qian
 */
package gov.grants.apply.valueObject;

import java.io.Serializable;

/**
 * Value object class of application list information.
 * 
 * @author Feng Qian
 *
 */
public class ApplicationListInformation 
    implements Serializable
{
	
    private String opportunityID;
    private String competitionID;
    private String cFDANumber;
    private String grantsGovTrackingNumber;
    private String receivedDateTime;
    private String status;
    private String statusDate;
    private String agencyTrackingNumber;
    private String submissionTitle;
	
	/**
	 * @return Returns the submissionTitle.
	 */
	public String getSubmissionTitle() {
		return submissionTitle;
	}
	/**
	 * @param submissionTitle The submissionTitle to set.
	 */
	public void setSubmissionTitle(String submissionTitle) {
		this.submissionTitle = submissionTitle;
	}
    /** Creates a new instance of applicationListInformation */
    public ApplicationListInformation()  throws Exception {
	;
    }

	// GETTERS
	/**
	 * @return the string of the opportunity ID.
	 */

    public String getOpportunityID(){
        return this.opportunityID;
    }

    /**
	 * @return the string of the competition ID.
	 */
	public String getCompetitionID(){
        return this.competitionID;
    }

    /**
	 * @return the string of the CFDA number.
	 */
	public String getCFDANumber(){
        return this.cFDANumber;
    }

    /**
	 * @return the string of Grants gov tracking number.
	 */
	public String getGrantsGovTrackingNumber(){
        return this.grantsGovTrackingNumber;
    }

    /**
	 * @return the string of the received date time.
	 */
	public String getReceivedDateTime(){
        return this.receivedDateTime;
    }	
    /**
	 * @return the string of the status.
	 */
	public String getStatus(){
        return this.status;
    }

	// SETTERS	
	/**
	 * @param opportunityID the string of opportunity ID.
	 */
    public void setOpportunityID( String opportunityID ){
        this.opportunityID = opportunityID;
    }
		
    /**
	 * @param competitionID the string of competition ID.
	 */
	public void setCompetitionID( String competitionID ){
        this.competitionID = competitionID;
    }
		
    /**
	 * @param cFDANumber the string of the CFDA number.
	 */
	public void setCFDANumber( String cFDANumber ){
        this.cFDANumber = cFDANumber;
    }
		
    /**
	 * @param grantsGovTrackingNumber the string of the Grants gov tracking number.
	 */
	public void setGrantsGovTrackingNumber( String grantsGovTrackingNumber ){
        this.grantsGovTrackingNumber = grantsGovTrackingNumber;
    }
	
    /**
	 * @param receivedDateTime the string of the received date time.
	 */
	public void setReceivedDateTime( String receivedDateTime ){
        this.receivedDateTime = receivedDateTime;
    }
		
    /**
	 * @param status the string of the status.
	 */
	public void setStatus( String status ){
        this.status = status;
    }

	/**
	 * @return the agencyTrackingNumber.
	 */
	public String getAgencyTrackingNumber() {
		return agencyTrackingNumber;
	}
	/**
	 * @param agencyTrackingNumber the string of the agency tracking number.
	 */
	public void setAgencyTrackingNumber(String agencyTrackingNumber) {
		this.agencyTrackingNumber = agencyTrackingNumber;
	}
	/**
	 * @return the string of the status date.
	 */
	public String getStatusDate() {
		return statusDate;
	}
	/**
	 * @param the string of the status date.
	 */
	public void setStatusDate(String statusDate) {
		this.statusDate = statusDate;
	}
}
