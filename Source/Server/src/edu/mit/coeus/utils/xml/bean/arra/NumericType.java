//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.09.18 at 01:32:49 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.arra;


/**
 * A data type for a number value.
 * Java content class for NumericType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/Backup%20%20-%20Mohan/CoeusDocs/Proposal%20Printing/schemas/FederalReportingDataElements_v1/RecoveryRR/Version%201/Version%201.0/Schemas/Subset/niem/niem-core/2.0/niem-corec.xsd line 42)
 * <p>
 * <pre>
 * &lt;complexType name="NumericType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://niem.gov/niem/proxy/xsd/2.0>decimal">
 *       &lt;attribute ref="{http://niem.gov/niem/niem-core/2.0}confidenceNumeric"/>
 *       &lt;attribute ref="{http://niem.gov/niem/niem-core/2.0}toleranceNumeric"/>
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface NumericType
    extends edu.mit.coeus.utils.xml.bean.arra.Decimal
{


    /**
     * Gets the value of the confidenceNumeric property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getConfidenceNumeric();

    /**
     * Sets the value of the confidenceNumeric property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setConfidenceNumeric(java.math.BigDecimal value);

    /**
     * Gets the value of the toleranceNumeric property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getToleranceNumeric();

    /**
     * Sets the value of the toleranceNumeric property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setToleranceNumeric(java.math.BigDecimal value);

}