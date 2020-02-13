
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
 *         &lt;element name="AORUserID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AORPassword" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}DUNS" minOccurs="0"/>
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
    "aorUserID",
    "aorPassword",
    "duns"
})
@XmlRootElement(name = "AuthenticateAORRequest")
public class AuthenticateAORRequest {

    @XmlElement(name = "AORUserID", required = true)
    protected String aorUserID;
    @XmlElement(name = "AORPassword", required = true)
    protected String aorPassword;
    @XmlElement(name = "DUNS", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
    protected String duns;

    /**
     * Gets the value of the aorUserID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAORUserID() {
        return aorUserID;
    }

    /**
     * Sets the value of the aorUserID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAORUserID(String value) {
        this.aorUserID = value;
    }

    /**
     * Gets the value of the aorPassword property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAORPassword() {
        return aorPassword;
    }

    /**
     * Sets the value of the aorPassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAORPassword(String value) {
        this.aorPassword = value;
    }

    /**
     * Gets the value of the duns property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDUNS() {
        return duns;
    }

    /**
     * Sets the value of the duns property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDUNS(String value) {
        this.duns = value;
    }

}
