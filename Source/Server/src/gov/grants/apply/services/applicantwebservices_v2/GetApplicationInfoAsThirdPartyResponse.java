
package gov.grants.apply.services.applicantwebservices_v2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}GrantsGovTrackingNumber"/>
 *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}StatusDetail"/>
 *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}AgencyTrackingNumber" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}AgencyNotes" minOccurs="0"/>
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
    "statusDetail",
    "agencyTrackingNumber",
    "agencyNotes"
})
@XmlRootElement(name = "GetApplicationInfoAsThirdPartyResponse")
public class GetApplicationInfoAsThirdPartyResponse {

    @XmlElement(name = "GrantsGovTrackingNumber", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", required = true)
    protected String grantsGovTrackingNumber;
    @XmlElement(name = "StatusDetail", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", required = true)
    protected String statusDetail;
    @XmlElement(name = "AgencyTrackingNumber", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
    protected String agencyTrackingNumber;
    @XmlElement(name = "AgencyNotes", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
    protected String agencyNotes;

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
     * Gets the value of the statusDetail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatusDetail() {
        return statusDetail;
    }

    /**
     * Sets the value of the statusDetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatusDetail(String value) {
        this.statusDetail = value;
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
     * Gets the value of the agencyNotes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgencyNotes() {
        return agencyNotes;
    }

    /**
     * Sets the value of the agencyNotes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgencyNotes(String value) {
        this.agencyNotes = value;
    }

}
