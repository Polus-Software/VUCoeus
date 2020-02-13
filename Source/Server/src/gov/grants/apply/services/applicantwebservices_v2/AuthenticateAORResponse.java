
package gov.grants.apply.services.applicantwebservices_v2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import gov.grants.apply.system.grantscommonelements_v1.SecurityMessage;
import gov.grants.apply.system.grantscommonelements_v1.Token;


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
 *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}Token"/>
 *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}SecurityMessage" minOccurs="0"/>
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
    "token",
    "securityMessage"
})
@XmlRootElement(name = "AuthenticateAORResponse")
public class AuthenticateAORResponse {

    @XmlElement(name = "Token", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", required = true)
    protected Token token;
    @XmlElement(name = "SecurityMessage", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
    protected SecurityMessage securityMessage;

    /**
     * Gets the value of the token property.
     * 
     * @return
     *     possible object is
     *     {@link Token }
     *     
     */
    public Token getToken() {
        return token;
    }

    /**
     * Sets the value of the token property.
     * 
     * @param value
     *     allowed object is
     *     {@link Token }
     *     
     */
    public void setToken(Token value) {
        this.token = value;
    }

    /**
     * Gets the value of the securityMessage property.
     * 
     * @return
     *     possible object is
     *     {@link SecurityMessage }
     *     
     */
    public SecurityMessage getSecurityMessage() {
        return securityMessage;
    }

    /**
     * Sets the value of the securityMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link SecurityMessage }
     *     
     */
    public void setSecurityMessage(SecurityMessage value) {
        this.securityMessage = value;
    }

}
