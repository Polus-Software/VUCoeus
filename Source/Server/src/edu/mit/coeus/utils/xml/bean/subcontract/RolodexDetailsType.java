//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.06.05 at 06:30:57 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.subcontract;


/**
 * Java content class for RolodexDetailsType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/Sun/jwsdp-1.6/jaxb/bin/Subcontract.xsd line 409)
 * <p>
 * <pre>
 * &lt;complexType name="RolodexDetailsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RolodexId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="LastName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FirstName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MiddleName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Suffix" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Prefix" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Title" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Organization" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Address1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Address2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Address3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Fax" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="City" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="County" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StateCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StateDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Pincode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Comments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PhoneNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CountryCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CountryDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SponsorCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SponsorName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OwnedByUnit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OwnedByUnitName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface RolodexDetailsType {


    /**
     * Gets the value of the stateDescription property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getStateDescription();

    /**
     * Sets the value of the stateDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setStateDescription(java.lang.String value);

    /**
     * Gets the value of the sponsorName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getSponsorName();

    /**
     * Sets the value of the sponsorName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setSponsorName(java.lang.String value);

    /**
     * Gets the value of the organization property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getOrganization();

    /**
     * Sets the value of the organization property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setOrganization(java.lang.String value);

    /**
     * Gets the value of the lastName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getLastName();

    /**
     * Sets the value of the lastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setLastName(java.lang.String value);

    /**
     * Gets the value of the pincode property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getPincode();

    /**
     * Sets the value of the pincode property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setPincode(java.lang.String value);

    /**
     * Gets the value of the stateCode property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getStateCode();

    /**
     * Sets the value of the stateCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setStateCode(java.lang.String value);

    /**
     * Gets the value of the address3 property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getAddress3();

    /**
     * Sets the value of the address3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setAddress3(java.lang.String value);

    /**
     * Gets the value of the countryDescription property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getCountryDescription();

    /**
     * Sets the value of the countryDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setCountryDescription(java.lang.String value);

    /**
     * Gets the value of the address2 property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getAddress2();

    /**
     * Sets the value of the address2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setAddress2(java.lang.String value);

    /**
     * Gets the value of the address1 property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getAddress1();

    /**
     * Sets the value of the address1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setAddress1(java.lang.String value);

    /**
     * Gets the value of the firstName property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getFirstName();

    /**
     * Sets the value of the firstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setFirstName(java.lang.String value);

    /**
     * Gets the value of the countryCode property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getCountryCode();

    /**
     * Sets the value of the countryCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setCountryCode(java.lang.String value);

    /**
     * Gets the value of the comments property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getComments();

    /**
     * Sets the value of the comments property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setComments(java.lang.String value);

    /**
     * Gets the value of the suffix property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getSuffix();

    /**
     * Sets the value of the suffix property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setSuffix(java.lang.String value);

    /**
     * Gets the value of the fax property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getFax();

    /**
     * Sets the value of the fax property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setFax(java.lang.String value);

    /**
     * Gets the value of the ownedByUnit property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getOwnedByUnit();

    /**
     * Sets the value of the ownedByUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setOwnedByUnit(java.lang.String value);

    /**
     * Gets the value of the phoneNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getPhoneNumber();

    /**
     * Sets the value of the phoneNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setPhoneNumber(java.lang.String value);

    /**
     * Gets the value of the prefix property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getPrefix();

    /**
     * Sets the value of the prefix property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setPrefix(java.lang.String value);

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getTitle();

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setTitle(java.lang.String value);

    /**
     * Gets the value of the city property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getCity();

    /**
     * Sets the value of the city property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setCity(java.lang.String value);

    /**
     * Gets the value of the middleName property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getMiddleName();

    /**
     * Sets the value of the middleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setMiddleName(java.lang.String value);

    /**
     * Gets the value of the county property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getCounty();

    /**
     * Sets the value of the county property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setCounty(java.lang.String value);

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getEmail();

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setEmail(java.lang.String value);

    /**
     * Gets the value of the rolodexId property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     int
     */
    java.lang.Integer getRolodexId();

    /**
     * Sets the value of the rolodexId property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     int
     */
    void setRolodexId(java.lang.Integer value);

    /**
     * Gets the value of the ownedByUnitName property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getOwnedByUnitName();

    /**
     * Sets the value of the ownedByUnitName property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setOwnedByUnitName(java.lang.String value);

    /**
     * Gets the value of the sponsorCode property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getSponsorCode();

    /**
     * Sets the value of the sponsorCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setSponsorCode(java.lang.String value);

}
