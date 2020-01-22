//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.rar;


/**
 * Java content class for BudgetSummaryType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/jwsdp-1.4/jaxb/bin/rarschema.xsd line 347)
 * <p>
 * <pre>
 * &lt;complexType name="BudgetSummaryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="InitialBudgetTotals" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace}BudgetTotalsType"/>
 *         &lt;element name="AllBudgetTotals" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace}BudgetTotalsType"/>
 *         &lt;element name="BudgetPeriod" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace}BudgetPeriodType" maxOccurs="5"/>
 *         &lt;element name="BudgetJustification" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace}DescriptionBlockType" minOccurs="0"/>
 *         &lt;element name="BudgetDirectCostsTotal" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *         &lt;element name="BudgetIndirectCostsTotal" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *         &lt;element name="BudgetCostsTotal" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface BudgetSummaryType {


    /**
     * Gets the value of the budgetDirectCostsTotal property.
     * 
     * @return
     *     possible object is
     *     {@link org.w3._2001.xmlschema.AnyType}
     */
    org.w3._2001.xmlschema.AnyType getBudgetDirectCostsTotal();

    /**
     * Sets the value of the budgetDirectCostsTotal property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.w3._2001.xmlschema.AnyType}
     */
    void setBudgetDirectCostsTotal(org.w3._2001.xmlschema.AnyType value);

    /**
     * Gets the value of the budgetJustification property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.DescriptionBlockType}
     */
    edu.mit.coeus.utils.xml.bean.proposal.rar.DescriptionBlockType getBudgetJustification();

    /**
     * Sets the value of the budgetJustification property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.DescriptionBlockType}
     */
    void setBudgetJustification(edu.mit.coeus.utils.xml.bean.proposal.rar.DescriptionBlockType value);

    /**
     * Gets the value of the initialBudgetTotals property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.BudgetTotalsType}
     */
    edu.mit.coeus.utils.xml.bean.proposal.rar.BudgetTotalsType getInitialBudgetTotals();

    /**
     * Sets the value of the initialBudgetTotals property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.BudgetTotalsType}
     */
    void setInitialBudgetTotals(edu.mit.coeus.utils.xml.bean.proposal.rar.BudgetTotalsType value);

    /**
     * Gets the value of the budgetIndirectCostsTotal property.
     * 
     * @return
     *     possible object is
     *     {@link org.w3._2001.xmlschema.AnyType}
     */
    org.w3._2001.xmlschema.AnyType getBudgetIndirectCostsTotal();

    /**
     * Sets the value of the budgetIndirectCostsTotal property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.w3._2001.xmlschema.AnyType}
     */
    void setBudgetIndirectCostsTotal(org.w3._2001.xmlschema.AnyType value);

    /**
     * Gets the value of the BudgetPeriod property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the BudgetPeriod property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBudgetPeriod().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link edu.mit.coeus.utils.xml.bean.proposal.rar.BudgetPeriodType}
     * 
     */
    java.util.List getBudgetPeriod();

    /**
     * Gets the value of the budgetCostsTotal property.
     * 
     * @return
     *     possible object is
     *     {@link org.w3._2001.xmlschema.AnyType}
     */
    org.w3._2001.xmlschema.AnyType getBudgetCostsTotal();

    /**
     * Sets the value of the budgetCostsTotal property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.w3._2001.xmlschema.AnyType}
     */
    void setBudgetCostsTotal(org.w3._2001.xmlschema.AnyType value);

    /**
     * Gets the value of the allBudgetTotals property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.BudgetTotalsType}
     */
    edu.mit.coeus.utils.xml.bean.proposal.rar.BudgetTotalsType getAllBudgetTotals();

    /**
     * Sets the value of the allBudgetTotals property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.BudgetTotalsType}
     */
    void setAllBudgetTotals(edu.mit.coeus.utils.xml.bean.proposal.rar.BudgetTotalsType value);

}