//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.rar;


/**
 * Java content class for HumanSubjectsType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/jwsdp-1.4/jaxb/bin/rarschema.xsd line 358)
 * <p>
 * <pre>
 * &lt;complexType name="HumanSubjectsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="HumanSubjectsUsedQuestion" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="ExemptionNumber" type="{http://www.w3.org/2001/XMLSchema}token" maxOccurs="6"/>
 *           &lt;sequence>
 *             &lt;element name="AssuranceNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *             &lt;element name="IRBApprovalDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *           &lt;/sequence>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface HumanSubjectsType {


    /**
     * Gets the value of the ExemptionNumber property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ExemptionNumber property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExemptionNumber().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link java.lang.String}
     * 
     */
    java.util.List getExemptionNumber();

    /**
     * Gets the value of the humanSubjectsUsedQuestion property.
     * 
     */
    boolean isHumanSubjectsUsedQuestion();

    /**
     * Sets the value of the humanSubjectsUsedQuestion property.
     * 
     */
    void setHumanSubjectsUsedQuestion(boolean value);

    /**
     * Gets the value of the assuranceNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAssuranceNumber();

    /**
     * Sets the value of the assuranceNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAssuranceNumber(java.lang.String value);

    /**
     * Gets the value of the irbApprovalDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getIRBApprovalDate();

    /**
     * Sets the value of the irbApprovalDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setIRBApprovalDate(java.util.Calendar value);

}
