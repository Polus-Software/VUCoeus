//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.02.21 at 04:06:33 IST 
//


package edu.mit.coeuslite.award.print.awardsummary;


/**
 * Java content class for Unittype complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/CHECK%20OUT/Coeus%204.5/Source/Server/src/edu/mit/coeuslite/award/schema/AwardSummary.xsd line 78)
 * <p>
 * <pre>
 * &lt;complexType name="Unittype">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="UnitNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LeadUnitFlag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface Unittype {


    /**
     * Gets the value of the unitNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getUnitNumber();

    /**
     * Sets the value of the unitNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setUnitNumber(java.lang.String value);

    /**
     * Gets the value of the leadUnitFlag property.
     * 
     */
    boolean isLeadUnitFlag();

    /**
     * Sets the value of the leadUnitFlag property.
     * 
     */
    void setLeadUnitFlag(boolean value);

}
