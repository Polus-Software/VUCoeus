//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.sf424_v1;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply.grants.gov/forms/schemas/SF424-V1.0.xsd line 423)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://apply.grants.gov/forms/SF424-V1.0}OrganizationIdentifyingInformation" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/forms/SF424-V1.0}Address" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/forms/SF424-V1.0}DelinquentFederalDebtIndicator" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/forms/SF424-V1.0}DelinquentFederalDebtExplanation" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/forms/SF424-V1.0}CongressionalDistrict" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface SubmittingOrganizationType {


    /**
     * Gets the value of the congressionalDistrict property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCongressionalDistrict();

    /**
     * Sets the value of the congressionalDistrict property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCongressionalDistrict(java.lang.String value);

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.sf424_v1.AddressType}
     *     {@link gov.grants.apply.forms.sf424_v1.Address}
     */
    gov.grants.apply.forms.sf424_v1.AddressType getAddress();

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.sf424_v1.AddressType}
     *     {@link gov.grants.apply.forms.sf424_v1.Address}
     */
    void setAddress(gov.grants.apply.forms.sf424_v1.AddressType value);

    /**
     * Gets the value of the delinquentFederalDebtExplanation property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getDelinquentFederalDebtExplanation();

    /**
     * Sets the value of the delinquentFederalDebtExplanation property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setDelinquentFederalDebtExplanation(java.lang.String value);

    /**
     * Gets the value of the organizationIdentifyingInformation property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.sf424_v1.OrganizationIdentifyingInformation}
     *     {@link gov.grants.apply.forms.sf424_v1.OrganizationIdentifyingInformationType}
     */
    gov.grants.apply.forms.sf424_v1.OrganizationIdentifyingInformationType getOrganizationIdentifyingInformation();

    /**
     * Sets the value of the organizationIdentifyingInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.sf424_v1.OrganizationIdentifyingInformation}
     *     {@link gov.grants.apply.forms.sf424_v1.OrganizationIdentifyingInformationType}
     */
    void setOrganizationIdentifyingInformation(gov.grants.apply.forms.sf424_v1.OrganizationIdentifyingInformationType value);

    /**
     * Gets the value of the delinquentFederalDebtIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getDelinquentFederalDebtIndicator();

    /**
     * Sets the value of the delinquentFederalDebtIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setDelinquentFederalDebtIndicator(java.lang.String value);

}
