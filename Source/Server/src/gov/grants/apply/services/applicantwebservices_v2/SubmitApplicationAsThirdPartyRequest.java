
package gov.grants.apply.services.applicantwebservices_v2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import gov.grants.apply.system.grantscommonelements_v1.Attachment;
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
 *         &lt;element name="ApplicationXML" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{http://apply.grants.gov/system/GrantsCommonElements-V1.0}Attachment" maxOccurs="unbounded" minOccurs="0"/>
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
    "applicationXML",
    "attachment"
})
@XmlRootElement(name = "SubmitApplicationAsThirdPartyRequest")
public class SubmitApplicationAsThirdPartyRequest {

    @XmlElement(name = "Token", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0", required = true)
    protected Token token;
    @XmlElement(name = "ApplicationXML", required = true)
    protected String applicationXML;
    @XmlElement(name = "Attachment", namespace = "http://apply.grants.gov/system/GrantsCommonElements-V1.0")
    protected List<Attachment> attachment;

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
     * Gets the value of the applicationXML property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicationXML() {
        return applicationXML;
    }

    /**
     * Sets the value of the applicationXML property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicationXML(String value) {
        this.applicationXML = value;
    }

    /**
     * Gets the value of the attachment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attachment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttachment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Attachment }
     * 
     * 
     */
    public List<Attachment> getAttachment() {
        if (attachment == null) {
            attachment = new ArrayList<Attachment>();
        }
        return this.attachment;
    }

}
