//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.12.13 at 02:14:23 EST 
//


package edu.mit.coeus.utils.xml.bean.award;


/**
 * Java content class for ScienceCodeDetailType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/temp/jaxb1.0.4/bin/AwardNotice.xsd line 213)
 * <p>
 * <pre>
 * &lt;complexType name="ScienceCodeDetailType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AwardNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SequenceNumber" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface ScienceCodeDetailType {


    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getDescription();

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setDescription(java.lang.String value);

    /**
     * Gets the value of the sequenceNumber property.
     * 
     */
    int getSequenceNumber();

    /**
     * Sets the value of the sequenceNumber property.
     * 
     */
    void setSequenceNumber(int value);

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCode();

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCode(java.lang.String value);

    /**
     * Gets the value of the awardNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAwardNumber();

    /**
     * Sets the value of the awardNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAwardNumber(java.lang.String value);

}
