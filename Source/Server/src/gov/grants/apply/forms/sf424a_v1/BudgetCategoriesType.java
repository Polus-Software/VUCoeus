//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.sf424a_v1;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply07.grants.gov/forms/schemas/SF424A-V1.0.xsd line 25)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://apply.grants.gov/forms/SF424A-V1.0}CategorySet" maxOccurs="4" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/forms/SF424A-V1.0}CategoryTotals" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface BudgetCategoriesType {


    /**
     * Gets the value of the categoryTotals property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.sf424a_v1.CategoryTotals}
     *     {@link gov.grants.apply.forms.sf424a_v1.CategoryTotalsType}
     */
    gov.grants.apply.forms.sf424a_v1.CategoryTotalsType getCategoryTotals();

    /**
     * Sets the value of the categoryTotals property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.sf424a_v1.CategoryTotals}
     *     {@link gov.grants.apply.forms.sf424a_v1.CategoryTotalsType}
     */
    void setCategoryTotals(gov.grants.apply.forms.sf424a_v1.CategoryTotalsType value);

    /**
     * Gets the value of the CategorySet property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the CategorySet property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCategorySet().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link gov.grants.apply.forms.sf424a_v1.CategorySet}
     * {@link gov.grants.apply.forms.sf424a_v1.CategorySetType}
     * 
     */
    java.util.List getCategorySet();

}
