//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.03.17 at 08:39:35 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.protocol;


/**
 * Java content class for ProtocolLinksType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/E:/Share/3091/ProtocolSummary.xsd line 378)
 * <p>
 * <pre>
 * &lt;complexType name="ProtocolLinksType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ProtocolNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SequenceNumber" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ModuleCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ModuleItemKey" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ModuleItemSequenceNumber" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ActionType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Comments" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="UpdateTimestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="UpdateUser" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface ProtocolLinksType {


    /**
     * Gets the value of the moduleCode property.
     * 
     */
    int getModuleCode();

    /**
     * Sets the value of the moduleCode property.
     * 
     */
    void setModuleCode(int value);

    /**
     * Gets the value of the comments property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getComments();

    /**
     * Sets the value of the comments property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setComments(java.lang.String value);

    /**
     * Gets the value of the actionType property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getActionType();

    /**
     * Sets the value of the actionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setActionType(java.lang.String value);

    /**
     * Gets the value of the updateTimestamp property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getUpdateTimestamp();

    /**
     * Sets the value of the updateTimestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setUpdateTimestamp(java.util.Calendar value);

    /**
     * Gets the value of the protocolNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getProtocolNumber();

    /**
     * Sets the value of the protocolNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setProtocolNumber(java.lang.String value);

    /**
     * Gets the value of the moduleItemKey property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getModuleItemKey();

    /**
     * Sets the value of the moduleItemKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setModuleItemKey(java.lang.String value);

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
     * Gets the value of the moduleItemSequenceNumber property.
     * 
     */
    int getModuleItemSequenceNumber();

    /**
     * Sets the value of the moduleItemSequenceNumber property.
     * 
     */
    void setModuleItemSequenceNumber(int value);

    /**
     * Gets the value of the updateUser property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getUpdateUser();

    /**
     * Sets the value of the updateUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setUpdateUser(java.lang.String value);

}
