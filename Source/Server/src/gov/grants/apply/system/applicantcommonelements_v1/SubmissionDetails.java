
package gov.grants.apply.system.applicantcommonelements_v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import gov.grants.apply.system.grantscommontypes_v1.GrantsGovApplicationStatusType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}GrantsGovTrackingNumber" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}AgencyTrackingNumber" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}GrantsGovApplicationStatus" minOccurs="0"/>
 *         &lt;element name="ReceivedDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="StatusDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}FundingOpportunityNumber" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}SubmissionTitle" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}PackageID" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "grantsGovTrackingNumber",
    "agencyTrackingNumber",
    "grantsGovApplicationStatus",
    "receivedDateTime",
    "statusDateTime",
    "fundingOpportunityNumber",
    "submissionTitle",
    "packageID"
})
@XmlRootElement(name = "SubmissionDetails")
public class SubmissionDetails {

    @XmlElement(name = "GrantsGovTrackingNumber", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
    protected String grantsGovTrackingNumber;
    @XmlElement(name = "AgencyTrackingNumber", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
    protected String agencyTrackingNumber;
    @XmlElement(name = "GrantsGovApplicationStatus", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
    @XmlSchemaType(name = "string")
    protected GrantsGovApplicationStatusType grantsGovApplicationStatus;
    @XmlElement(name = "ReceivedDateTime")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar receivedDateTime;
    @XmlElement(name = "StatusDateTime")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar statusDateTime;
    @XmlElement(name = "FundingOpportunityNumber", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
    protected String fundingOpportunityNumber;
    @XmlElement(name = "SubmissionTitle", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
    protected String submissionTitle;
    @XmlElement(name = "PackageID", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
    protected String packageID;

    /**
     * Gets the value of the grantsGovTrackingNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGrantsGovTrackingNumber() {
        return grantsGovTrackingNumber;
    }

    /**
     * Sets the value of the grantsGovTrackingNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGrantsGovTrackingNumber(String value) {
        this.grantsGovTrackingNumber = value;
    }

    /**
     * Gets the value of the agencyTrackingNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgencyTrackingNumber() {
        return agencyTrackingNumber;
    }

    /**
     * Sets the value of the agencyTrackingNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgencyTrackingNumber(String value) {
        this.agencyTrackingNumber = value;
    }

    /**
     * Gets the value of the grantsGovApplicationStatus property.
     * 
     * @return
     *     possible object is
     *     {@link GrantsGovApplicationStatusType }
     *     
     */
    public GrantsGovApplicationStatusType getGrantsGovApplicationStatus() {
        return grantsGovApplicationStatus;
    }

    /**
     * Sets the value of the grantsGovApplicationStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link GrantsGovApplicationStatusType }
     *     
     */
    public void setGrantsGovApplicationStatus(GrantsGovApplicationStatusType value) {
        this.grantsGovApplicationStatus = value;
    }

    /**
     * Gets the value of the receivedDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getReceivedDateTime() {
        return receivedDateTime;
    }

    /**
     * Sets the value of the receivedDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setReceivedDateTime(XMLGregorianCalendar value) {
        this.receivedDateTime = value;
    }

    /**
     * Gets the value of the statusDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getStatusDateTime() {
        return statusDateTime;
    }

    /**
     * Sets the value of the statusDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setStatusDateTime(XMLGregorianCalendar value) {
        this.statusDateTime = value;
    }

    /**
     * Gets the value of the fundingOpportunityNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFundingOpportunityNumber() {
        return fundingOpportunityNumber;
    }

    /**
     * Sets the value of the fundingOpportunityNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFundingOpportunityNumber(String value) {
        this.fundingOpportunityNumber = value;
    }

    /**
     * Gets the value of the submissionTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubmissionTitle() {
        return submissionTitle;
    }

    /**
     * Sets the value of the submissionTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubmissionTitle(String value) {
        this.submissionTitle = value;
    }

    /**
     * Gets the value of the packageID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPackageID() {
        return packageID;
    }

    /**
     * Sets the value of the packageID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPackageID(String value) {
        this.packageID = value;
    }

}
