//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.09.09 at 02:29:50 CDT 
//


package gov.grants.apply.forms.planned_report_v1_0;


/**
 * Java content class for PlannedReport_TotalsDataType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/Petitforms/schema/PlannedReport-V1.0.xsd line 52)
 * <p>
 * <pre>
 * &lt;complexType name="PlannedReport_TotalsDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AmericanIndian" type="{http://apply.grants.gov/forms/PlannedReport-V1.0}PlannedReport_0_to_9999999999_DataType"/>
 *         &lt;element name="Asian" type="{http://apply.grants.gov/forms/PlannedReport-V1.0}PlannedReport_0_to_9999999999_DataType"/>
 *         &lt;element name="Hawaiian" type="{http://apply.grants.gov/forms/PlannedReport-V1.0}PlannedReport_0_to_9999999999_DataType"/>
 *         &lt;element name="Black" type="{http://apply.grants.gov/forms/PlannedReport-V1.0}PlannedReport_0_to_9999999999_DataType"/>
 *         &lt;element name="White" type="{http://apply.grants.gov/forms/PlannedReport-V1.0}PlannedReport_0_to_9999999999_DataType"/>
 *         &lt;element name="MultipleRace" type="{http://apply.grants.gov/forms/PlannedReport-V1.0}PlannedReport_0_to_9999999999_DataType"/>
 *         &lt;element name="Total" type="{http://apply.grants.gov/forms/PlannedReport-V1.0}PlannedReport_0_to_99999999999_DataType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface PlannedReportTotalsDataType {


    /**
     * Gets the value of the multipleRace property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getMultipleRace();

    /**
     * Sets the value of the multipleRace property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setMultipleRace(java.math.BigInteger value);

    /**
     * Gets the value of the white property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getWhite();

    /**
     * Sets the value of the white property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setWhite(java.math.BigInteger value);

    /**
     * Gets the value of the asian property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getAsian();

    /**
     * Sets the value of the asian property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setAsian(java.math.BigInteger value);

    /**
     * Gets the value of the americanIndian property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getAmericanIndian();

    /**
     * Sets the value of the americanIndian property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setAmericanIndian(java.math.BigInteger value);

    /**
     * Gets the value of the black property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getBlack();

    /**
     * Sets the value of the black property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setBlack(java.math.BigInteger value);

    /**
     * Gets the value of the hawaiian property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getHawaiian();

    /**
     * Sets the value of the hawaiian property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setHawaiian(java.math.BigInteger value);

    /**
     * Gets the value of the total property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getTotal();

    /**
     * Sets the value of the total property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setTotal(java.math.BigInteger value);

}