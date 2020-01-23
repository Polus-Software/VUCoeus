//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.03.17 at 08:39:35 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.protocol;


/**
 * Java content class for ProtoAmendRenewSummaryType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/E:/Share/3091/ProtocolSummary.xsd line 679)
 * <p>
 * <pre>
 * &lt;complexType name="ProtoAmendRenewSummaryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ProtocolNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ProtoAmendRenewalNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ProtocolModuleCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ProtocolModuleDesc" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="UpdateTimestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="UpdateUser" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ProtocolModules" type="{http://www.w3.org/2001/ProtocolSummarySchema}ProtocolModulesType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface ProtoAmendRenewSummaryType {


    /**
     * Gets the value of the ProtocolModules property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ProtocolModules property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProtocolModules().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link edu.mit.coeus.utils.xml.bean.protocol.ProtocolModulesType}
     * 
     */
    java.util.List getProtocolModules();

    /**
     * Gets the value of the protocolModuleCode property.
     * 
     */
    int getProtocolModuleCode();

    /**
     * Sets the value of the protocolModuleCode property.
     * 
     */
    void setProtocolModuleCode(int value);

    /**
     * Gets the value of the updateTimestamp property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getUpdateTimestamp();

    /**
     * Sets the value of the updateTimestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setUpdateTimestamp(java.util.Calendar value);

    /**
     * Gets the value of the protocolModuleDesc property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getProtocolModuleDesc();

    /**
     * Sets the value of the protocolModuleDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setProtocolModuleDesc(java.lang.String value);

    /**
     * Gets the value of the protocolNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getProtocolNumber();

    /**
     * Sets the value of the protocolNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setProtocolNumber(java.lang.String value);

    /**
     * Gets the value of the updateUser property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getUpdateUser();

    /**
     * Sets the value of the updateUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setUpdateUser(java.lang.String value);

    /**
     * Gets the value of the protoAmendRenewalNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getProtoAmendRenewalNumber();

    /**
     * Sets the value of the protoAmendRenewalNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setProtoAmendRenewalNumber(java.lang.String value);

}
