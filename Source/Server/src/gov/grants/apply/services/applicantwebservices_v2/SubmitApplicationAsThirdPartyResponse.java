
package gov.grants.apply.services.applicantwebservices_v2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element name="ReceivedDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
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
    "receivedDateTime"
})
@XmlRootElement(name = "SubmitApplicationAsThirdPartyResponse")
public class SubmitApplicationAsThirdPartyResponse {

    @XmlElement(name = "GrantsGovTrackingNumber", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", required = true)
    protected String grantsGovTrackingNumber;
    @XmlElement(name = "ReceivedDateTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar receivedDateTime;

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

}
