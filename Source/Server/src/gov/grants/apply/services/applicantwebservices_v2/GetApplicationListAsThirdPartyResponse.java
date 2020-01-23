
package gov.grants.apply.services.applicantwebservices_v2;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="AvailableApplicationNumber" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ApplicationInfo" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}CFDANumber" minOccurs="0"/>
 *                   &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}FundingOpportunityNumber" minOccurs="0"/>
 *                   &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}CompetitionID" minOccurs="0"/>
 *                   &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}GrantsGovTrackingNumber"/>
 *                   &lt;element name="ReceivedDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *                   &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}GrantsGovApplicationStatus"/>
 *                   &lt;element name="StatusDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *                   &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}AgencyTrackingNumber" minOccurs="0"/>
 *                   &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}SubmissionTitle" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
    "availableApplicationNumber",
    "applicationInfo"
})
@XmlRootElement(name = "GetApplicationListAsThirdPartyResponse")
public class GetApplicationListAsThirdPartyResponse {

    @XmlElement(name = "AvailableApplicationNumber")
    protected int availableApplicationNumber;
    @XmlElement(name = "ApplicationInfo")
    protected List<GetApplicationListAsThirdPartyResponse.ApplicationInfo> applicationInfo;

    /**
     * Gets the value of the availableApplicationNumber property.
     * 
     */
    public int getAvailableApplicationNumber() {
        return availableApplicationNumber;
    }

    /**
     * Sets the value of the availableApplicationNumber property.
     * 
     */
    public void setAvailableApplicationNumber(int value) {
        this.availableApplicationNumber = value;
    }

    /**
     * Gets the value of the applicationInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the applicationInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getApplicationInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GetApplicationListAsThirdPartyResponse.ApplicationInfo }
     * 
     * 
     */
    public List<GetApplicationListAsThirdPartyResponse.ApplicationInfo> getApplicationInfo() {
        if (applicationInfo == null) {
            applicationInfo = new ArrayList<GetApplicationListAsThirdPartyResponse.ApplicationInfo>();
        }
        return this.applicationInfo;
    }


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
     *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}CFDANumber" minOccurs="0"/>
     *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}FundingOpportunityNumber" minOccurs="0"/>
     *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}CompetitionID" minOccurs="0"/>
     *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}GrantsGovTrackingNumber"/>
     *         &lt;element name="ReceivedDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
     *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}GrantsGovApplicationStatus"/>
     *         &lt;element name="StatusDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
     *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}AgencyTrackingNumber" minOccurs="0"/>
     *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}SubmissionTitle" minOccurs="0"/>
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
        "cfdaNumber",
        "fundingOpportunityNumber",
        "competitionID",
        "grantsGovTrackingNumber",
        "receivedDateTime",
        "grantsGovApplicationStatus",
        "statusDateTime",
        "agencyTrackingNumber",
        "submissionTitle"
    })
    public static class ApplicationInfo {

        @XmlElement(name = "CFDANumber", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
        protected String cfdaNumber;
        @XmlElement(name = "FundingOpportunityNumber", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
        protected String fundingOpportunityNumber;
        @XmlElement(name = "CompetitionID", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
        protected String competitionID;
        @XmlElement(name = "GrantsGovTrackingNumber", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", required = true)
        protected String grantsGovTrackingNumber;
        @XmlElement(name = "ReceivedDateTime")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar receivedDateTime;
        @XmlElement(name = "GrantsGovApplicationStatus", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", required = true)
        @XmlSchemaType(name = "string")
        protected GrantsGovApplicationStatusType grantsGovApplicationStatus;
        @XmlElement(name = "StatusDateTime")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar statusDateTime;
        @XmlElement(name = "AgencyTrackingNumber", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
        protected String agencyTrackingNumber;
        @XmlElement(name = "SubmissionTitle", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
        protected String submissionTitle;

        /**
         * Gets the value of the cfdaNumber property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCFDANumber() {
            return cfdaNumber;
        }

        /**
         * Sets the value of the cfdaNumber property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCFDANumber(String value) {
            this.cfdaNumber = value;
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
         * Gets the value of the competitionID property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCompetitionID() {
            return competitionID;
        }

        /**
         * Sets the value of the competitionID property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCompetitionID(String value) {
            this.competitionID = value;
        }

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

    }

}
