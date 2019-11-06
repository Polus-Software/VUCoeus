
package gov.grants.apply.system.grantscommonelements_v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import gov.grants.apply.system.grantscommontypes_v1.AORStatusType;


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
 *         &lt;element name="UserID" type="{http://apply.grants.gov/system/GrantsCommonTypes-V1.0}UserIDType"/>
 *         &lt;element name="TokenId" type="{http://apply.grants.gov/system/GrantsCommonTypes-V1.0}TokenIdType" minOccurs="0"/>
 *         &lt;element name="DUNS" type="{http://apply.grants.gov/system/GrantsCommonTypes-V1.0}DUNSType" minOccurs="0"/>
 *         &lt;element name="FullName" type="{http://apply.grants.gov/system/GrantsCommonTypes-V1.0}FullNameType" minOccurs="0"/>
 *         &lt;element name="AORStatus" type="{http://apply.grants.gov/system/GrantsCommonTypes-V1.0}AORStatusType"/>
 *         &lt;element name="TokenExpiration" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
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
    "userID",
    "tokenId",
    "duns",
    "fullName",
    "aorStatus",
    "tokenExpiration"
})
@XmlRootElement(name = "Token")
public class Token {

    @XmlElement(name = "UserID", required = true)
    protected String userID;
    @XmlElement(name = "TokenId")
    protected String tokenId;
    @XmlElement(name = "DUNS")
    protected String duns;
    @XmlElement(name = "FullName")
    protected String fullName;
    @XmlElement(name = "AORStatus", required = true)
    @XmlSchemaType(name = "string")
    protected AORStatusType aorStatus;
    @XmlElement(name = "TokenExpiration")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar tokenExpiration;

    /**
     * Gets the value of the userID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Sets the value of the userID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserID(String value) {
        this.userID = value;
    }

    /**
     * Gets the value of the tokenId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTokenId() {
        return tokenId;
    }

    /**
     * Sets the value of the tokenId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTokenId(String value) {
        this.tokenId = value;
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

    /**
     * Gets the value of the fullName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the value of the fullName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFullName(String value) {
        this.fullName = value;
    }

    /**
     * Gets the value of the aorStatus property.
     * 
     * @return
     *     possible object is
     *     {@link AORStatusType }
     *     
     */
    public AORStatusType getAORStatus() {
        return aorStatus;
    }

    /**
     * Sets the value of the aorStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link AORStatusType }
     *     
     */
    public void setAORStatus(AORStatusType value) {
        this.aorStatus = value;
    }

    /**
     * Gets the value of the tokenExpiration property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTokenExpiration() {
        return tokenExpiration;
    }

    /**
     * Sets the value of the tokenExpiration property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTokenExpiration(XMLGregorianCalendar value) {
        this.tokenExpiration = value;
    }

}
