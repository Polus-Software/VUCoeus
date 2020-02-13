//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.06.04 at 03:49:17 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.reviewcomments;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/Coeus%204.4/Printing/correspondenceTemplates/schema/irb.xsd line 18)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CommitteeId">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="15"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="CommitteeName">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="60"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="CommDescription" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="2000"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="HomeUnitNumber">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="8"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="HomeUnitName">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="60"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="CommitteeTypeCode" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="CommitteeTypeDesc">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="200"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="ScheduleDescription" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="2000"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="MinimumMembersRequired" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="MaxProtocols" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="AdvSubmissionDays" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="DefaultReviewTypeCode" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="DefaultReviewTypeDesc" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="200"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface CommitteeMasterDataType {


    /**
     * Gets the value of the commDescription property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getCommDescription();

    /**
     * Sets the value of the commDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setCommDescription(java.lang.String value);

    /**
     * Gets the value of the advSubmissionDays property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     *     {@link null}
     */
    java.math.BigInteger getAdvSubmissionDays();

    /**
     * Sets the value of the advSubmissionDays property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     *     {@link null}
     */
    void setAdvSubmissionDays(java.math.BigInteger value);

    /**
     * Gets the value of the committeeId property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCommitteeId();

    /**
     * Sets the value of the committeeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCommitteeId(java.lang.String value);

    /**
     * Gets the value of the defaultReviewTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getDefaultReviewTypeCode();

    /**
     * Sets the value of the defaultReviewTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.math.BigInteger}
     */
    void setDefaultReviewTypeCode(java.math.BigInteger value);

    /**
     * Gets the value of the defaultReviewTypeDesc property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getDefaultReviewTypeDesc();

    /**
     * Sets the value of the defaultReviewTypeDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setDefaultReviewTypeDesc(java.lang.String value);

    /**
     * Gets the value of the homeUnitNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getHomeUnitNumber();

    /**
     * Sets the value of the homeUnitNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setHomeUnitNumber(java.lang.String value);

    /**
     * Gets the value of the committeeTypeDesc property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCommitteeTypeDesc();

    /**
     * Sets the value of the committeeTypeDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCommitteeTypeDesc(java.lang.String value);

    /**
     * Gets the value of the homeUnitName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getHomeUnitName();

    /**
     * Sets the value of the homeUnitName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setHomeUnitName(java.lang.String value);

    /**
     * Gets the value of the maxProtocols property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     *     {@link null}
     */
    java.math.BigInteger getMaxProtocols();

    /**
     * Sets the value of the maxProtocols property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     *     {@link null}
     */
    void setMaxProtocols(java.math.BigInteger value);

    /**
     * Gets the value of the scheduleDescription property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getScheduleDescription();

    /**
     * Sets the value of the scheduleDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setScheduleDescription(java.lang.String value);

    /**
     * Gets the value of the committeeTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getCommitteeTypeCode();

    /**
     * Sets the value of the committeeTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setCommitteeTypeCode(java.math.BigInteger value);

    /**
     * Gets the value of the committeeName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCommitteeName();

    /**
     * Sets the value of the committeeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCommitteeName(java.lang.String value);

    /**
     * Gets the value of the minimumMembersRequired property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     *     {@link null}
     */
    java.math.BigInteger getMinimumMembersRequired();

    /**
     * Sets the value of the minimumMembersRequired property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     *     {@link null}
     */
    void setMinimumMembersRequired(java.math.BigInteger value);

}
