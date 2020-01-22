//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.sf424c_v1;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply07.grants.gov/forms/schemas/SF424C-V1.0.xsd line 92)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://apply.grants.gov/forms/SF424C-V1.0}CostLineItem" maxOccurs="11" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/forms/SF424C-V1.0}CostSubtotalBeforeContingencies" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/forms/SF424C-V1.0}Contingencies" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/forms/SF424C-V1.0}CostSubtotalAfterContingencies" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/forms/SF424C-V1.0}ProgramIncome" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/forms/SF424C-V1.0}TotalProjectCosts" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface ProjectCostsType {


    /**
     * Gets the value of the programIncome property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.sf424c_v1.ProgramIncome}
     *     {@link gov.grants.apply.forms.sf424c_v1.ProgramIncomeType}
     */
    gov.grants.apply.forms.sf424c_v1.ProgramIncomeType getProgramIncome();

    /**
     * Sets the value of the programIncome property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.sf424c_v1.ProgramIncome}
     *     {@link gov.grants.apply.forms.sf424c_v1.ProgramIncomeType}
     */
    void setProgramIncome(gov.grants.apply.forms.sf424c_v1.ProgramIncomeType value);

    /**
     * Gets the value of the costSubtotalAfterContingencies property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.sf424c_v1.CostSubtotalAfterContingenciesType}
     *     {@link gov.grants.apply.forms.sf424c_v1.CostSubtotalAfterContingencies}
     */
    gov.grants.apply.forms.sf424c_v1.CostSubtotalAfterContingenciesType getCostSubtotalAfterContingencies();

    /**
     * Sets the value of the costSubtotalAfterContingencies property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.sf424c_v1.CostSubtotalAfterContingenciesType}
     *     {@link gov.grants.apply.forms.sf424c_v1.CostSubtotalAfterContingencies}
     */
    void setCostSubtotalAfterContingencies(gov.grants.apply.forms.sf424c_v1.CostSubtotalAfterContingenciesType value);

    /**
     * Gets the value of the totalProjectCosts property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.sf424c_v1.TotalProjectCostsType}
     *     {@link gov.grants.apply.forms.sf424c_v1.TotalProjectCosts}
     */
    gov.grants.apply.forms.sf424c_v1.TotalProjectCostsType getTotalProjectCosts();

    /**
     * Sets the value of the totalProjectCosts property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.sf424c_v1.TotalProjectCostsType}
     *     {@link gov.grants.apply.forms.sf424c_v1.TotalProjectCosts}
     */
    void setTotalProjectCosts(gov.grants.apply.forms.sf424c_v1.TotalProjectCostsType value);

    /**
     * Gets the value of the CostLineItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the CostLineItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCostLineItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link gov.grants.apply.forms.sf424c_v1.CostLineItemType}
     * {@link gov.grants.apply.forms.sf424c_v1.CostLineItem}
     * 
     */
    java.util.List getCostLineItem();

    /**
     * Gets the value of the contingencies property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.sf424c_v1.Contingencies}
     *     {@link gov.grants.apply.forms.sf424c_v1.ContingenciesType}
     */
    gov.grants.apply.forms.sf424c_v1.ContingenciesType getContingencies();

    /**
     * Sets the value of the contingencies property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.sf424c_v1.Contingencies}
     *     {@link gov.grants.apply.forms.sf424c_v1.ContingenciesType}
     */
    void setContingencies(gov.grants.apply.forms.sf424c_v1.ContingenciesType value);

    /**
     * Gets the value of the costSubtotalBeforeContingencies property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.sf424c_v1.CostSubtotalBeforeContingenciesType}
     *     {@link gov.grants.apply.forms.sf424c_v1.CostSubtotalBeforeContingencies}
     */
    gov.grants.apply.forms.sf424c_v1.CostSubtotalBeforeContingenciesType getCostSubtotalBeforeContingencies();

    /**
     * Sets the value of the costSubtotalBeforeContingencies property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.sf424c_v1.CostSubtotalBeforeContingenciesType}
     *     {@link gov.grants.apply.forms.sf424c_v1.CostSubtotalBeforeContingencies}
     */
    void setCostSubtotalBeforeContingencies(gov.grants.apply.forms.sf424c_v1.CostSubtotalBeforeContingenciesType value);

}