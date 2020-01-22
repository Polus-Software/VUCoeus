//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.rr_fednonfedbudget_v1;


/**
 * Java content class for SectACompensationDataType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply.grants.gov/forms/schemas/RR_FedNonFedBudget-V1.0.xsd line 129)
 * <p>
 * <pre>
 * &lt;complexType name="SectACompensationDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CalendarMonths" type="{http://apply.grants.gov/forms/RR_FedNonFedBudget-V1.0}Month2whole2fracDataType" minOccurs="0"/>
 *         &lt;element name="AcademicMonths" type="{http://apply.grants.gov/forms/RR_FedNonFedBudget-V1.0}Month2whole2fracDataType" minOccurs="0"/>
 *         &lt;element name="SummerMonths" type="{http://apply.grants.gov/forms/RR_FedNonFedBudget-V1.0}Month2whole2fracDataType" minOccurs="0"/>
 *         &lt;element name="RequestedSalary" type="{http://apply.grants.gov/system/Global-V1.0}DecimalMin1Max14Places2Type"/>
 *         &lt;element name="FringeBenefits" type="{http://apply.grants.gov/system/Global-V1.0}DecimalMin1Max14Places2Type"/>
 *         &lt;element name="Total" type="{http://apply.grants.gov/forms/RR_FedNonFedBudget-V1.0}TotalDataType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface SectACompensationDataType {


    /**
     * Gets the value of the academicMonths property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getAcademicMonths();

    /**
     * Sets the value of the academicMonths property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setAcademicMonths(java.math.BigDecimal value);

    /**
     * Gets the value of the calendarMonths property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getCalendarMonths();

    /**
     * Sets the value of the calendarMonths property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setCalendarMonths(java.math.BigDecimal value);

    /**
     * Gets the value of the summerMonths property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getSummerMonths();

    /**
     * Sets the value of the summerMonths property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setSummerMonths(java.math.BigDecimal value);

    /**
     * Gets the value of the total property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.rr_fednonfedbudget_v1.TotalDataType}
     */
    gov.grants.apply.forms.rr_fednonfedbudget_v1.TotalDataType getTotal();

    /**
     * Sets the value of the total property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.rr_fednonfedbudget_v1.TotalDataType}
     */
    void setTotal(gov.grants.apply.forms.rr_fednonfedbudget_v1.TotalDataType value);

    /**
     * Gets the value of the fringeBenefits property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getFringeBenefits();

    /**
     * Sets the value of the fringeBenefits property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setFringeBenefits(java.math.BigDecimal value);

    /**
     * Gets the value of the requestedSalary property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getRequestedSalary();

    /**
     * Sets the value of the requestedSalary property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setRequestedSalary(java.math.BigDecimal value);

}