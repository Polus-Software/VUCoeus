//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.12.13 at 02:14:23 EST 
//


package edu.mit.coeus.utils.xml.bean.award;


/**
 * Java content class for AwardDisclosureType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/temp/jaxb1.0.4/bin/AwardNotice.xsd line 748)
 * <p>
 * <pre>
 * &lt;complexType name="AwardDisclosureType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}AwardHeader" minOccurs="0"/>
 *         &lt;element name="DisclosureItem" type="{}DisclosureItemType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="AwardValidation" type="{}AwardValidationType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="EnableAwardValidation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DisclosureValidation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface AwardDisclosureType {


    /**
     * Gets the value of the awardHeader property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.award.AwardHeaderType}
     *     {@link edu.mit.coeus.utils.xml.bean.award.AwardHeader}
     */
    edu.mit.coeus.utils.xml.bean.award.AwardHeaderType getAwardHeader();

    /**
     * Sets the value of the awardHeader property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.award.AwardHeaderType}
     *     {@link edu.mit.coeus.utils.xml.bean.award.AwardHeader}
     */
    void setAwardHeader(edu.mit.coeus.utils.xml.bean.award.AwardHeaderType value);

    /**
     * Gets the value of the DisclosureItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the DisclosureItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDisclosureItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link edu.mit.coeus.utils.xml.bean.award.DisclosureItemType}
     * 
     */
    java.util.List getDisclosureItem();

    /**
     * Gets the value of the enableAwardValidation property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getEnableAwardValidation();

    /**
     * Sets the value of the enableAwardValidation property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setEnableAwardValidation(java.lang.String value);

    /**
     * Gets the value of the AwardValidation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the AwardValidation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAwardValidation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link edu.mit.coeus.utils.xml.bean.award.AwardValidationType}
     * 
     */
    java.util.List getAwardValidation();

    /**
     * Gets the value of the disclosureValidation property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getDisclosureValidation();

    /**
     * Sets the value of the disclosureValidation property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setDisclosureValidation(java.lang.String value);

}