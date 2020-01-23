/*
 * ApplicationStatDetailInformation.java
 *
 * Created on 11/8/2004
 *
 * @author  Mark Sommer
 */
package gov.grants.apply.valueObject;

import java.io.Serializable;

/**
 * Value object class of application list status detail information.
 * 
 * @author Mark Sommer
 *  
 */
public class ApplicationStatusDetailInformation
    implements Serializable
{

	private String grantsGovTrackingNumber;
	private String statusDetail;

	/** Creates a new instance of applicationStatusDetailInformation */
	public ApplicationStatusDetailInformation() throws Exception {
		;
	}

	// GETTERS

	/**
	 * @return the string of Grants gov tracking number.
	 */
	public String getGrantsGovTrackingNumber() {
		return this.grantsGovTrackingNumber;
	}

	/**
	 * @return the string of the status detail.
	 */
	public String getStatusDetail() {
		return this.statusDetail;
	}

	// SETTERS
	/**
	 * @param grantsGovTrackingNumber
	 *            the string of the Grants gov tracking number.
	 */
	public void setGrantsGovTrackingNumber(String grantsGovTrackingNumber) {
		this.grantsGovTrackingNumber = grantsGovTrackingNumber;
	}

	/**
	 * @param statusDetail
	 *            the string of the status Detail.
	 */
	public void setStatusDetail(String statusDetail) {
		this.statusDetail = statusDetail;
	}

}
