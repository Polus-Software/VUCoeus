//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.sf424a_v1;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply07.grants.gov/forms/schemas/SF424A-V1.0.xsd line 266)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{http://apply.grants.gov/forms/SF424A-V1.0}ResourceAmountGroup"/>
 *       &lt;/sequence>
 *       &lt;attribute ref="{http://apply.grants.gov/forms/SF424A-V1.0}activityTitle use="required""/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface ResourceLineItemType {


    /**
     * Gets the value of the activityTitle property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getActivityTitle();

    /**
     * Sets the value of the activityTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setActivityTitle(java.lang.String value);

    /**
     * Gets the value of the budgetStateContributionAmount property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getBudgetStateContributionAmount();

    /**
     * Sets the value of the budgetStateContributionAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setBudgetStateContributionAmount(java.math.BigDecimal value);

    /**
     * Gets the value of the budgetOtherContributionAmount property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getBudgetOtherContributionAmount();

    /**
     * Sets the value of the budgetOtherContributionAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setBudgetOtherContributionAmount(java.math.BigDecimal value);

    /**
     * Gets the value of the budgetTotalContributionAmount property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getBudgetTotalContributionAmount();

    /**
     * Sets the value of the budgetTotalContributionAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setBudgetTotalContributionAmount(java.math.BigDecimal value);

    /**
     * Gets the value of the budgetApplicantContributionAmount property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getBudgetApplicantContributionAmount();

    /**
     * Sets the value of the budgetApplicantContributionAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setBudgetApplicantContributionAmount(java.math.BigDecimal value);

}
