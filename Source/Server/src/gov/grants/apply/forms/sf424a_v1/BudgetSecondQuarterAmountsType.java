//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.sf424a_v1;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply07.grants.gov/forms/schemas/SF424A-V1.0.xsd line 129)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{http://apply.grants.gov/forms/SF424A-V1.0}ForecastedAmountGroup"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface BudgetSecondQuarterAmountsType {


    /**
     * Gets the value of the budgetTotalForecastedAmount property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getBudgetTotalForecastedAmount();

    /**
     * Sets the value of the budgetTotalForecastedAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setBudgetTotalForecastedAmount(java.math.BigDecimal value);

    /**
     * Gets the value of the budgetNonFederalForecastedAmount property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getBudgetNonFederalForecastedAmount();

    /**
     * Sets the value of the budgetNonFederalForecastedAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setBudgetNonFederalForecastedAmount(java.math.BigDecimal value);

    /**
     * Gets the value of the budgetFederalForecastedAmount property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getBudgetFederalForecastedAmount();

    /**
     * Sets the value of the budgetFederalForecastedAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setBudgetFederalForecastedAmount(java.math.BigDecimal value);

}
