//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.12.13 at 02:14:23 EST 
//


package edu.mit.coeus.utils.xml.bean.award;


/**
 * Java content class for InvestigatorUnitsType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/temp/jaxb1.0.4/bin/AwardNotice.xsd line 68)
 * <p>
 * <pre>
 * &lt;complexType name="InvestigatorUnitsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AwardNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SequenceNumber" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="PersonId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UnitNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UnitName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LeadUnit" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="OSPAdminName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AdministrativeOfficer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AdministrativeOfficerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UnitHead" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UnitHeadName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DeanVp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DeanVpName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OtherIndividualToNotify" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OtherIndividualToNotifyName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UnitAdministrator" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="UnitAdministratorTypeCode" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *                   &lt;element name="Administrator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="AdministratorName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface InvestigatorUnitsType {


    /**
     * Gets the value of the unitHeadName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getUnitHeadName();

    /**
     * Sets the value of the unitHeadName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setUnitHeadName(java.lang.String value);

    /**
     * Gets the value of the administrativeOfficer property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getAdministrativeOfficer();

    /**
     * Sets the value of the administrativeOfficer property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setAdministrativeOfficer(java.lang.String value);

    /**
     * Gets the value of the administrativeOfficerName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getAdministrativeOfficerName();

    /**
     * Sets the value of the administrativeOfficerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setAdministrativeOfficerName(java.lang.String value);

    /**
     * Gets the value of the unitNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getUnitNumber();

    /**
     * Sets the value of the unitNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setUnitNumber(java.lang.String value);

    /**
     * Gets the value of the deanVp property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getDeanVp();

    /**
     * Sets the value of the deanVp property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setDeanVp(java.lang.String value);

    /**
     * Gets the value of the otherIndividualToNotifyName property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getOtherIndividualToNotifyName();

    /**
     * Sets the value of the otherIndividualToNotifyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setOtherIndividualToNotifyName(java.lang.String value);

    /**
     * Gets the value of the unitHead property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getUnitHead();

    /**
     * Sets the value of the unitHead property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setUnitHead(java.lang.String value);

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
     * Gets the value of the ospAdminName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getOSPAdminName();

    /**
     * Sets the value of the ospAdminName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setOSPAdminName(java.lang.String value);

    /**
     * Gets the value of the deanVpName property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getDeanVpName();

    /**
     * Sets the value of the deanVpName property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setDeanVpName(java.lang.String value);

    /**
     * Gets the value of the UnitAdministrator property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the UnitAdministrator property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUnitAdministrator().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link edu.mit.coeus.utils.xml.bean.award.InvestigatorUnitsType.UnitAdministratorType}
     * 
     */
    java.util.List getUnitAdministrator();

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

    /**
     * Gets the value of the unitName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getUnitName();

    /**
     * Sets the value of the unitName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setUnitName(java.lang.String value);

    /**
     * Gets the value of the personId property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getPersonId();

    /**
     * Sets the value of the personId property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setPersonId(java.lang.String value);

    /**
     * Gets the value of the leadUnit property.
     * 
     */
    boolean isLeadUnit();

    /**
     * Sets the value of the leadUnit property.
     * 
     */
    void setLeadUnit(boolean value);

    /**
     * Gets the value of the otherIndividualToNotify property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getOtherIndividualToNotify();

    /**
     * Sets the value of the otherIndividualToNotify property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setOtherIndividualToNotify(java.lang.String value);


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/temp/jaxb1.0.4/bin/AwardNotice.xsd line 86)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="UnitAdministratorTypeCode" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
     *         &lt;element name="Administrator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="AdministratorName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface UnitAdministratorType {


        /**
         * Gets the value of the unitAdministratorTypeCode property.
         * 
         * @return
         *     possible object is
         *     int{@link null}
         */
        java.lang.Integer getUnitAdministratorTypeCode();

        /**
         * Sets the value of the unitAdministratorTypeCode property.
         * 
         * @param value
         *     allowed object is
         *     int{@link null}
         */
        void setUnitAdministratorTypeCode(java.lang.Integer value);

        /**
         * Gets the value of the administrator property.
         * 
         * @return
         *     possible object is
         *     {@link null}
         *     {@link java.lang.String}
         */
        java.lang.String getAdministrator();

        /**
         * Sets the value of the administrator property.
         * 
         * @param value
         *     allowed object is
         *     {@link null}
         *     {@link java.lang.String}
         */
        void setAdministrator(java.lang.String value);

        /**
         * Gets the value of the administratorName property.
         * 
         * @return
         *     possible object is
         *     {@link null}
         *     {@link java.lang.String}
         */
        java.lang.String getAdministratorName();

        /**
         * Sets the value of the administratorName property.
         * 
         * @param value
         *     allowed object is
         *     {@link null}
         *     {@link java.lang.String}
         */
        void setAdministratorName(java.lang.String value);

    }

}