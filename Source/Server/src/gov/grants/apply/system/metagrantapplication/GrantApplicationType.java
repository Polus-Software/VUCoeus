//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.06.12 at 03:28:23 IST 
//


package gov.grants.apply.system.metagrantapplication;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/E:/S2S/build/CustMetaGrantApplication.xsd line 20)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://apply.grants.gov/system/Header-V1.0}GrantSubmissionHeader" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/system/Header_2_0-V2.0}Header_2_0" minOccurs="0"/>
 *         &lt;element name="Forms">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence maxOccurs="unbounded">
 *                   &lt;any/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{http://apply.grants.gov/system/Footer-V1.0}GrantSubmissionFooter" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface GrantApplicationType {


    /**
     * Gets the value of the grantSubmissionFooter property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.system.footer_v1.GrantSubmissionFooter}
     *     {@link gov.grants.apply.system.footer_v1.GrantSubmissionFooterType}
     */
    gov.grants.apply.system.footer_v1.GrantSubmissionFooterType getGrantSubmissionFooter();

    /**
     * Sets the value of the grantSubmissionFooter property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.system.footer_v1.GrantSubmissionFooter}
     *     {@link gov.grants.apply.system.footer_v1.GrantSubmissionFooterType}
     */
    void setGrantSubmissionFooter(gov.grants.apply.system.footer_v1.GrantSubmissionFooterType value);

    /**
     * Gets the value of the forms property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.system.metagrantapplication.GrantApplicationType.FormsType}
     */
    gov.grants.apply.system.metagrantapplication.GrantApplicationType.FormsType getForms();

    /**
     * Sets the value of the forms property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.system.metagrantapplication.GrantApplicationType.FormsType}
     */
    void setForms(gov.grants.apply.system.metagrantapplication.GrantApplicationType.FormsType value);

    /**
     * Gets the value of the header20 property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.system.header_2_0_v2.Header20}
     *     {@link gov.grants.apply.system.header_2_0_v2.Header20Type}
     */
    gov.grants.apply.system.header_2_0_v2.Header20Type getHeader20();

    /**
     * Sets the value of the header20 property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.system.header_2_0_v2.Header20}
     *     {@link gov.grants.apply.system.header_2_0_v2.Header20Type}
     */
    void setHeader20(gov.grants.apply.system.header_2_0_v2.Header20Type value);

    /**
     * Gets the value of the grantSubmissionHeader property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.system.header_v1.GrantSubmissionHeader}
     *     {@link gov.grants.apply.system.header_v1.GrantSubmissionHeaderType}
     */
    gov.grants.apply.system.header_v1.GrantSubmissionHeaderType getGrantSubmissionHeader();

    /**
     * Sets the value of the grantSubmissionHeader property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.system.header_v1.GrantSubmissionHeader}
     *     {@link gov.grants.apply.system.header_v1.GrantSubmissionHeaderType}
     */
    void setGrantSubmissionHeader(gov.grants.apply.system.header_v1.GrantSubmissionHeaderType value);


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/E:/S2S/build/CustMetaGrantApplication.xsd line 25)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence maxOccurs="unbounded">
     *         &lt;any/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface FormsType {


        /**
         * Gets the value of the Any property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the Any property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAny().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link java.lang.Object}
         * 
         */
        java.util.List getAny();

    }

}