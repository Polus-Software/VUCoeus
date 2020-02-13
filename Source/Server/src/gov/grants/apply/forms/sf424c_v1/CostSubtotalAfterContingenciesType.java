//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.sf424c_v1;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply07.grants.gov/forms/schemas/SF424C-V1.0.xsd line 57)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{http://apply.grants.gov/forms/SF424C-V1.0}CostAmountGroup"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface CostSubtotalAfterContingenciesType {


    /**
     * Gets the value of the budgetNonAllowableCostAmount property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getBudgetNonAllowableCostAmount();

    /**
     * Sets the value of the budgetNonAllowableCostAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setBudgetNonAllowableCostAmount(java.math.BigDecimal value);

    /**
     * Gets the value of the budgetEstimatedCostAmount property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getBudgetEstimatedCostAmount();

    /**
     * Sets the value of the budgetEstimatedCostAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setBudgetEstimatedCostAmount(java.math.BigDecimal value);

    /**
     * Gets the value of the budgetTotalAllowableCostAmount property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getBudgetTotalAllowableCostAmount();

    /**
     * Sets the value of the budgetTotalAllowableCostAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setBudgetTotalAllowableCostAmount(java.math.BigDecimal value);

}
