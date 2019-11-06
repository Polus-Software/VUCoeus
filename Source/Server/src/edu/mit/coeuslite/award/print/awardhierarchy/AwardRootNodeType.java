//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.01.12 at 04:36:38 IST 
//


package edu.mit.coeuslite.award.print.awardhierarchy;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/NetBean7.1/COEUS4.5YORN/Source/Server/src/edu/mit/coeuslite/award/schema/AwardHierarchy.xsd line 8)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AwardItems" type="{}AwardDetails" maxOccurs="unbounded"/>
 *         &lt;element name="MITAccountNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AwardPI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AwardDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface AwardRootNodeType {


    /**
     * Gets the value of the awardDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getAwardDate();

    /**
     * Sets the value of the awardDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setAwardDate(java.util.Calendar value);

    /**
     * Gets the value of the mitAccountNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getMITAccountNumber();

    /**
     * Sets the value of the mitAccountNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setMITAccountNumber(java.lang.String value);

    /**
     * Gets the value of the awardPI property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAwardPI();

    /**
     * Sets the value of the awardPI property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAwardPI(java.lang.String value);

    /**
     * Gets the value of the AwardItems property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the AwardItems property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAwardItems().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link edu.mit.coeuslite.award.print.awardhierarchy.AwardDetails}
     * 
     */
    java.util.List getAwardItems();

}
