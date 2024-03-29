//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.03.17 at 08:39:35 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.protocol;


/**
 * Java content class for ProtoCorrespType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/E:/Share/3091/ProtocolSummary.xsd line 724)
 * <p>
 * <pre>
 * &lt;complexType name="ProtoCorrespType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ProtoCorrespTypeCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="UpdateTimestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="UpdateUser" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ModuleId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ProtocolCorrespondence" type="{http://www.w3.org/2001/ProtocolSummarySchema}ProtocolCorrespondenceType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ProtoCorrespDefRecip" type="{http://www.w3.org/2001/ProtocolSummarySchema}ProtoCorrespDefRecipType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ProtoCorrespRecipients" type="{http://www.w3.org/2001/ProtocolSummarySchema}ProtoCorrespRecipientsType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ProtoCorrespTempl" type="{http://www.w3.org/2001/ProtocolSummarySchema}ProtoCorrespTemplType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface ProtoCorrespType {


    /**
     * Gets the value of the ProtoCorrespDefRecip property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ProtoCorrespDefRecip property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProtoCorrespDefRecip().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link edu.mit.coeus.utils.xml.bean.protocol.ProtoCorrespDefRecipType}
     * 
     */
    java.util.List getProtoCorrespDefRecip();

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
     * Gets the value of the ProtoCorrespRecipients property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ProtoCorrespRecipients property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProtoCorrespRecipients().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link edu.mit.coeus.utils.xml.bean.protocol.ProtoCorrespRecipientsType}
     * 
     */
    java.util.List getProtoCorrespRecipients();

    /**
     * Gets the value of the ProtoCorrespTempl property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ProtoCorrespTempl property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProtoCorrespTempl().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link edu.mit.coeus.utils.xml.bean.protocol.ProtoCorrespTemplType}
     * 
     */
    java.util.List getProtoCorrespTempl();

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
     * Gets the value of the protoCorrespTypeCode property.
     * 
     */
    int getProtoCorrespTypeCode();

    /**
     * Sets the value of the protoCorrespTypeCode property.
     * 
     */
    void setProtoCorrespTypeCode(int value);

    /**
     * Gets the value of the ProtocolCorrespondence property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ProtocolCorrespondence property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProtocolCorrespondence().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link edu.mit.coeus.utils.xml.bean.protocol.ProtocolCorrespondenceType}
     * 
     */
    java.util.List getProtocolCorrespondence();

    /**
     * Gets the value of the moduleId property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getModuleId();

    /**
     * Sets the value of the moduleId property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setModuleId(java.lang.String value);

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
