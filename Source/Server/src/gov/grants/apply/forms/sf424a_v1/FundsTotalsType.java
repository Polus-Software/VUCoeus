//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.sf424a_v1;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply07.grants.gov/forms/schemas/SF424A-V1.0.xsd line 226)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{http://apply.grants.gov/forms/SF424A-V1.0}BudgetFundsAmountGroup"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface FundsTotalsType {


    /**
     * Gets the value of the budgetFourthYearAmount property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getBudgetFourthYearAmount();

    /**
     * Sets the value of the budgetFourthYearAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setBudgetFourthYearAmount(java.math.BigDecimal value);

    /**
     * Gets the value of the budgetThirdYearAmount property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getBudgetThirdYearAmount();

    /**
     * Sets the value of the budgetThirdYearAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setBudgetThirdYearAmount(java.math.BigDecimal value);

    /**
     * Gets the value of the budgetFirstYearAmount property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getBudgetFirstYearAmount();

    /**
     * Sets the value of the budgetFirstYearAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setBudgetFirstYearAmount(java.math.BigDecimal value);

    /**
     * Gets the value of the budgetSecondYearAmount property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getBudgetSecondYearAmount();

    /**
     * Sets the value of the budgetSecondYearAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setBudgetSecondYearAmount(java.math.BigDecimal value);

}
