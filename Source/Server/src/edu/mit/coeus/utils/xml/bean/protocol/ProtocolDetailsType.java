//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.03.17 at 08:39:35 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.protocol;


/**
 * Java content class for ProtocolDetailsType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/E:/Share/3091/ProtocolSummary.xsd line 165)
 * <p>
 * <pre>
 * &lt;complexType name="ProtocolDetailsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ProtocolNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SequenceNumber" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ProtocolTypeCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ProtocolStatusCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ProtocolStatusDesc" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Title" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ApplicationDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="ApprovalDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="ExpirationDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="FdaApplicationNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ReferenceNumber1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ReferenceNumber2" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IsBillable" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SpecialReviewIndicator" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="VulnerableSubjectIndicator" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="KeyStudyPersonIndicator" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FundingSourceIndicator" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CorrespondentIndicator" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ReferenceIndicator" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="RelatedProjectsIndicator" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CreateTimestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="CreateUser" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="UpdateTimestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="UpdateUser" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LastApprovalDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="ProtocolOrg" type="{http://www.w3.org/2001/ProtocolSummarySchema}ProtocolOrgType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ProtocolLocation" type="{http://www.w3.org/2001/ProtocolSummarySchema}ProtocolLocationType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ProtocolStatus" type="{http://www.w3.org/2001/ProtocolSummarySchema}ProtocolStatusType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="protocolType" type="{http://www.w3.org/2001/ProtocolSummarySchema}protocolType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ProtocolTypeDesc" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MeetingDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="Investigator" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface ProtocolDetailsType {


    /**
     * Gets the value of the protocolStatusDesc property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getProtocolStatusDesc();

    /**
     * Sets the value of the protocolStatusDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setProtocolStatusDesc(java.lang.String value);

    /**
     * Gets the value of the createTimestamp property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getCreateTimestamp();

    /**
     * Sets the value of the createTimestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setCreateTimestamp(java.util.Calendar value);

    /**
     * Gets the value of the keyStudyPersonIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getKeyStudyPersonIndicator();

    /**
     * Sets the value of the keyStudyPersonIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setKeyStudyPersonIndicator(java.lang.String value);

    /**
     * Gets the value of the referenceNumber1 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getReferenceNumber1();

    /**
     * Sets the value of the referenceNumber1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setReferenceNumber1(java.lang.String value);

    /**
     * Gets the value of the referenceNumber2 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getReferenceNumber2();

    /**
     * Sets the value of the referenceNumber2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setReferenceNumber2(java.lang.String value);

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
     * Gets the value of the lastApprovalDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getLastApprovalDate();

    /**
     * Sets the value of the lastApprovalDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setLastApprovalDate(java.util.Calendar value);

    /**
     * Gets the value of the ProtocolStatus property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ProtocolStatus property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProtocolStatus().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link edu.mit.coeus.utils.xml.bean.protocol.ProtocolStatusType}
     * 
     */
    java.util.List getProtocolStatus();

    /**
     * Gets the value of the approvalDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getApprovalDate();

    /**
     * Sets the value of the approvalDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setApprovalDate(java.util.Calendar value);

    /**
     * Gets the value of the expirationDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getExpirationDate();

    /**
     * Sets the value of the expirationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setExpirationDate(java.util.Calendar value);

    /**
     * Gets the value of the referenceIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getReferenceIndicator();

    /**
     * Sets the value of the referenceIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setReferenceIndicator(java.lang.String value);

    /**
     * Gets the value of the createUser property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCreateUser();

    /**
     * Sets the value of the createUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCreateUser(java.lang.String value);

    /**
     * Gets the value of the vulnerableSubjectIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getVulnerableSubjectIndicator();

    /**
     * Sets the value of the vulnerableSubjectIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setVulnerableSubjectIndicator(java.lang.String value);

    /**
     * Gets the value of the protocolTypeCode property.
     * 
     */
    int getProtocolTypeCode();

    /**
     * Sets the value of the protocolTypeCode property.
     * 
     */
    void setProtocolTypeCode(int value);

    /**
     * Gets the value of the applicationDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getApplicationDate();

    /**
     * Sets the value of the applicationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setApplicationDate(java.util.Calendar value);

    /**
     * Gets the value of the fdaApplicationNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getFdaApplicationNumber();

    /**
     * Sets the value of the fdaApplicationNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setFdaApplicationNumber(java.lang.String value);

    /**
     * Gets the value of the relatedProjectsIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getRelatedProjectsIndicator();

    /**
     * Sets the value of the relatedProjectsIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setRelatedProjectsIndicator(java.lang.String value);

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

    /**
     * Gets the value of the protocolTypeDesc property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getProtocolTypeDesc();

    /**
     * Sets the value of the protocolTypeDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setProtocolTypeDesc(java.lang.String value);

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
     * Gets the value of the ProtocolOrg property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ProtocolOrg property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProtocolOrg().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link edu.mit.coeus.utils.xml.bean.protocol.ProtocolOrgType}
     * 
     */
    java.util.List getProtocolOrg();

    /**
     * Gets the value of the investigator property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getInvestigator();

    /**
     * Sets the value of the investigator property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setInvestigator(java.lang.String value);

    /**
     * Gets the value of the ProtocolType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ProtocolType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProtocolType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link edu.mit.coeus.utils.xml.bean.protocol.ProtocolType}
     * 
     */
    java.util.List getProtocolType();

    /**
     * Gets the value of the correspondentIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCorrespondentIndicator();

    /**
     * Sets the value of the correspondentIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCorrespondentIndicator(java.lang.String value);

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
     * Gets the value of the meetingDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getMeetingDate();

    /**
     * Sets the value of the meetingDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setMeetingDate(java.util.Calendar value);

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getTitle();

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setTitle(java.lang.String value);

    /**
     * Gets the value of the ProtocolLocation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ProtocolLocation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProtocolLocation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link edu.mit.coeus.utils.xml.bean.protocol.ProtocolLocationType}
     * 
     */
    java.util.List getProtocolLocation();

    /**
     * Gets the value of the fundingSourceIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getFundingSourceIndicator();

    /**
     * Sets the value of the fundingSourceIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setFundingSourceIndicator(java.lang.String value);

    /**
     * Gets the value of the protocolStatusCode property.
     * 
     */
    int getProtocolStatusCode();

    /**
     * Sets the value of the protocolStatusCode property.
     * 
     */
    void setProtocolStatusCode(int value);

    /**
     * Gets the value of the isBillable property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getIsBillable();

    /**
     * Sets the value of the isBillable property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setIsBillable(java.lang.String value);

    /**
     * Gets the value of the specialReviewIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getSpecialReviewIndicator();

    /**
     * Sets the value of the specialReviewIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setSpecialReviewIndicator(java.lang.String value);

}
