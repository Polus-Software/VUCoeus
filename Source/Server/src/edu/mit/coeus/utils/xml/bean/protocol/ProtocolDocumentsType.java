//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.03.17 at 08:39:35 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.protocol;


/**
 * Java content class for ProtocolDocumentsType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/E:/Share/3091/ProtocolSummary.xsd line 276)
 * <p>
 * <pre>
 * &lt;complexType name="ProtocolDocumentsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ProtocolNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SequenceNumber" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="DocumentTypeCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FileName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="UpdateTimestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="UpdateUser" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="VersionNumber" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="DocumentStatusCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="DocumentId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ProtocolDocumentStatus" type="{http://www.w3.org/2001/ProtocolSummarySchema}ProtocolDocumentStatusType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ProtocolDocument" type="{http://www.w3.org/2001/ProtocolSummarySchema}ProtocolDocumentType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ProtocolOtherDocuments" type="{http://www.w3.org/2001/ProtocolSummarySchema}ProtocolOtherDocumentsType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface ProtocolDocumentsType {


    /**
     * Gets the value of the ProtocolOtherDocuments property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ProtocolOtherDocuments property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProtocolOtherDocuments().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link edu.mit.coeus.utils.xml.bean.protocol.ProtocolOtherDocumentsType}
     * 
     */
    java.util.List getProtocolOtherDocuments();

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
     * Gets the value of the documentId property.
     * 
     */
    int getDocumentId();

    /**
     * Sets the value of the documentId property.
     * 
     */
    void setDocumentId(int value);

    /**
     * Gets the value of the ProtocolDocumentStatus property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ProtocolDocumentStatus property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProtocolDocumentStatus().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link edu.mit.coeus.utils.xml.bean.protocol.ProtocolDocumentStatusType}
     * 
     */
    java.util.List getProtocolDocumentStatus();

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
     * Gets the value of the documentStatusCode property.
     * 
     */
    int getDocumentStatusCode();

    /**
     * Sets the value of the documentStatusCode property.
     * 
     */
    void setDocumentStatusCode(int value);

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
     * Gets the value of the ProtocolDocument property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ProtocolDocument property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProtocolDocument().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link edu.mit.coeus.utils.xml.bean.protocol.ProtocolDocumentType}
     * 
     */
    java.util.List getProtocolDocument();

    /**
     * Gets the value of the fileName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getFileName();

    /**
     * Sets the value of the fileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setFileName(java.lang.String value);

    /**
     * Gets the value of the versionNumber property.
     * 
     */
    int getVersionNumber();

    /**
     * Sets the value of the versionNumber property.
     * 
     */
    void setVersionNumber(int value);

    /**
     * Gets the value of the documentTypeCode property.
     * 
     */
    int getDocumentTypeCode();

    /**
     * Sets the value of the documentTypeCode property.
     * 
     */
    void setDocumentTypeCode(int value);

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