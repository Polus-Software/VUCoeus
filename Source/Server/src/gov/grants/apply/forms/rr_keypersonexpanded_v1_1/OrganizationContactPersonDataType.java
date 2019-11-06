//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.rr_keypersonexpanded_v1_1;


/**
 * Java content class for OrganizationContactPersonDataType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/RR_KeyPersonExpanded-V1.1.xsd line 119)
 * <p>
 * <pre>
 * &lt;complexType name="OrganizationContactPersonDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Name" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}HumanNameDataType"/>
 *         &lt;element name="Title" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}HumanTitleDataType" minOccurs="0"/>
 *         &lt;element name="Address" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}AddressDataType"/>
 *         &lt;element name="Phone" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}TelephoneNumberDataType"/>
 *         &lt;element name="Fax" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}TelephoneNumberDataType" minOccurs="0"/>
 *         &lt;element name="Email" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}EmailDataType" minOccurs="0"/>
 *         &lt;element name="OrganizationName" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}OrganizationNameDataType" minOccurs="0"/>
 *         &lt;element name="DepartmentName" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}DepartmentNameDataType" minOccurs="0"/>
 *         &lt;element name="DivisionName" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}DivisionNameDataType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface OrganizationContactPersonDataType {


    /**
     * Gets the value of the departmentName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getDepartmentName();

    /**
     * Sets the value of the departmentName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setDepartmentName(java.lang.String value);

    /**
     * Gets the value of the divisionName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getDivisionName();

    /**
     * Sets the value of the divisionName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setDivisionName(java.lang.String value);

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getEmail();

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setEmail(java.lang.String value);

    /**
     * Gets the value of the fax property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getFax();

    /**
     * Sets the value of the fax property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setFax(java.lang.String value);

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.system.globallibrary_v2.AddressDataType}
     */
    gov.grants.apply.system.globallibrary_v2.AddressDataType getAddress();

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.system.globallibrary_v2.AddressDataType}
     */
    void setAddress(gov.grants.apply.system.globallibrary_v2.AddressDataType value);

    /**
     * Gets the value of the organizationName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getOrganizationName();

    /**
     * Sets the value of the organizationName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setOrganizationName(java.lang.String value);

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getTitle();

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setTitle(java.lang.String value);

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.system.globallibrary_v2.HumanNameDataType}
     */
    gov.grants.apply.system.globallibrary_v2.HumanNameDataType getName();

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.system.globallibrary_v2.HumanNameDataType}
     */
    void setName(gov.grants.apply.system.globallibrary_v2.HumanNameDataType value);

    /**
     * Gets the value of the phone property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getPhone();

    /**
     * Sets the value of the phone property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setPhone(java.lang.String value);

}
