//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.09.18 at 01:32:49 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.arra;


/**
 * A data type for an officer within a Recipient organization.
 * Java content class for RecipientOfficerType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/Backup%20%20-%20Mohan/CoeusDocs/Proposal%20Printing/schemas/FederalReportingDataElements_v1/RecoveryRR/Version%201/Version%201.0/Schemas/Extension/recoveryrr-ec.xsd line 275)
 * <p>
 * <pre>
 * &lt;complexType name="RecipientOfficerType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://niem.gov/niem/structures/2.0}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element ref="{http://niem.gov/niem/niem-core/2.0}PersonFullName"/>
 *         &lt;element ref="{urn:us:gov:recoveryrr-ext}OfficerCompensation"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface RecipientOfficerType
    extends edu.mit.coeus.utils.xml.bean.arra.ComplexObjectType
{


    /**
     * Gets the value of the officerCompensation property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     *     {@link null}
     */
    java.math.BigDecimal getOfficerCompensation();

    /**
     * Sets the value of the officerCompensation property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     *     {@link null}
     */
    void setOfficerCompensation(java.math.BigDecimal value);

    /**
     * Gets the value of the personFullName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getPersonFullName();

    /**
     * Sets the value of the personFullName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setPersonFullName(java.lang.String value);

}