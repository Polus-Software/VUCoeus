//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.12.13 at 02:14:23 EST 
//


package edu.mit.coeus.utils.xml.bean.award;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/temp/jaxb1.0.4/bin/AwardNotice.xsd line 6)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SchoolInfo" type="{}SchoolInfoType" minOccurs="0"/>
 *         &lt;element name="AwardDisclosure" type="{}AwardDisclosureType" minOccurs="0"/>
 *         &lt;element name="Award" type="{}AwardType" minOccurs="0"/>
 *         &lt;element name="AODetails" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="AONumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="AOName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="AOAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="PrintRequirement" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="SignatureRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="AddressListRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="CloseoutRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="CommentsRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="CostSharingRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="EquipmentRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="FlowThruRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ForeignTravelRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="FundingSummaryRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="HierarchyInfoRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="CurrentDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *                   &lt;element name="IndirectCostRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="PaymentRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ProposalDueRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ReportingRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ScienceCodeRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="SpecialReviewRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="SubcontractRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="TechnicalReportingRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="TermsRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="OtherDataRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="MoneyHistoryReport" type="{}MoneyHistoryReportType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface AwardNoticeType {


    /**
     * Gets the value of the aoDetails property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.award.AwardNoticeType.AODetailsType}
     */
    edu.mit.coeus.utils.xml.bean.award.AwardNoticeType.AODetailsType getAODetails();

    /**
     * Sets the value of the aoDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.award.AwardNoticeType.AODetailsType}
     */
    void setAODetails(edu.mit.coeus.utils.xml.bean.award.AwardNoticeType.AODetailsType value);

    /**
     * Gets the value of the awardDisclosure property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.award.AwardDisclosureType}
     */
    edu.mit.coeus.utils.xml.bean.award.AwardDisclosureType getAwardDisclosure();

    /**
     * Sets the value of the awardDisclosure property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.award.AwardDisclosureType}
     */
    void setAwardDisclosure(edu.mit.coeus.utils.xml.bean.award.AwardDisclosureType value);

    /**
     * Gets the value of the MoneyHistoryReport property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the MoneyHistoryReport property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMoneyHistoryReport().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link edu.mit.coeus.utils.xml.bean.award.MoneyHistoryReportType}
     * 
     */
    java.util.List getMoneyHistoryReport();

    /**
     * Gets the value of the award property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.award.AwardType}
     */
    edu.mit.coeus.utils.xml.bean.award.AwardType getAward();

    /**
     * Sets the value of the award property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.award.AwardType}
     */
    void setAward(edu.mit.coeus.utils.xml.bean.award.AwardType value);

    /**
     * Gets the value of the printRequirement property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.award.AwardNoticeType.PrintRequirementType}
     */
    edu.mit.coeus.utils.xml.bean.award.AwardNoticeType.PrintRequirementType getPrintRequirement();

    /**
     * Sets the value of the printRequirement property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.award.AwardNoticeType.PrintRequirementType}
     */
    void setPrintRequirement(edu.mit.coeus.utils.xml.bean.award.AwardNoticeType.PrintRequirementType value);

    /**
     * Gets the value of the schoolInfo property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.award.SchoolInfoType}
     */
    edu.mit.coeus.utils.xml.bean.award.SchoolInfoType getSchoolInfo();

    /**
     * Sets the value of the schoolInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.award.SchoolInfoType}
     */
    void setSchoolInfo(edu.mit.coeus.utils.xml.bean.award.SchoolInfoType value);


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/temp/jaxb1.0.4/bin/AwardNotice.xsd line 12)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="AONumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="AOName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="AOAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface AODetailsType {


        /**
         * Gets the value of the aoName property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getAOName();

        /**
         * Sets the value of the aoName property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setAOName(java.lang.String value);

        /**
         * Gets the value of the aoNumber property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getAONumber();

        /**
         * Sets the value of the aoNumber property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setAONumber(java.lang.String value);

        /**
         * Gets the value of the aoAddress property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getAOAddress();

        /**
         * Sets the value of the aoAddress property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setAOAddress(java.lang.String value);

    }


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/temp/jaxb1.0.4/bin/AwardNotice.xsd line 21)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="SignatureRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="AddressListRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="CloseoutRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="CommentsRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="CostSharingRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="EquipmentRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="FlowThruRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ForeignTravelRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="FundingSummaryRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="HierarchyInfoRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="CurrentDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
     *         &lt;element name="IndirectCostRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="PaymentRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ProposalDueRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ReportingRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ScienceCodeRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="SpecialReviewRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="SubcontractRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="TechnicalReportingRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="TermsRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="OtherDataRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface PrintRequirementType {


        /**
         * Gets the value of the signatureRequired property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getSignatureRequired();

        /**
         * Sets the value of the signatureRequired property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setSignatureRequired(java.lang.String value);

        /**
         * Gets the value of the proposalDueRequired property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getProposalDueRequired();

        /**
         * Sets the value of the proposalDueRequired property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setProposalDueRequired(java.lang.String value);

        /**
         * Gets the value of the closeoutRequired property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCloseoutRequired();

        /**
         * Sets the value of the closeoutRequired property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCloseoutRequired(java.lang.String value);

        /**
         * Gets the value of the equipmentRequired property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getEquipmentRequired();

        /**
         * Sets the value of the equipmentRequired property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setEquipmentRequired(java.lang.String value);

        /**
         * Gets the value of the costSharingRequired property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCostSharingRequired();

        /**
         * Sets the value of the costSharingRequired property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCostSharingRequired(java.lang.String value);

        /**
         * Gets the value of the reportingRequired property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getReportingRequired();

        /**
         * Sets the value of the reportingRequired property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setReportingRequired(java.lang.String value);

        /**
         * Gets the value of the termsRequired property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getTermsRequired();

        /**
         * Sets the value of the termsRequired property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setTermsRequired(java.lang.String value);

        /**
         * Gets the value of the foreignTravelRequired property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getForeignTravelRequired();

        /**
         * Sets the value of the foreignTravelRequired property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setForeignTravelRequired(java.lang.String value);

        /**
         * Gets the value of the technicalReportingRequired property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getTechnicalReportingRequired();

        /**
         * Sets the value of the technicalReportingRequired property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setTechnicalReportingRequired(java.lang.String value);

        /**
         * Gets the value of the scienceCodeRequired property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getScienceCodeRequired();

        /**
         * Sets the value of the scienceCodeRequired property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setScienceCodeRequired(java.lang.String value);

        /**
         * Gets the value of the specialReviewRequired property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getSpecialReviewRequired();

        /**
         * Sets the value of the specialReviewRequired property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setSpecialReviewRequired(java.lang.String value);

        /**
         * Gets the value of the otherDataRequired property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getOtherDataRequired();

        /**
         * Sets the value of the otherDataRequired property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setOtherDataRequired(java.lang.String value);

        /**
         * Gets the value of the currentDate property.
         * 
         * @return
         *     possible object is
         *     {@link java.util.Calendar}
         */
        java.util.Calendar getCurrentDate();

        /**
         * Sets the value of the currentDate property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.util.Calendar}
         */
        void setCurrentDate(java.util.Calendar value);

        /**
         * Gets the value of the commentsRequired property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCommentsRequired();

        /**
         * Sets the value of the commentsRequired property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCommentsRequired(java.lang.String value);

        /**
         * Gets the value of the flowThruRequired property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getFlowThruRequired();

        /**
         * Sets the value of the flowThruRequired property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setFlowThruRequired(java.lang.String value);

        /**
         * Gets the value of the hierarchyInfoRequired property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getHierarchyInfoRequired();

        /**
         * Sets the value of the hierarchyInfoRequired property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setHierarchyInfoRequired(java.lang.String value);

        /**
         * Gets the value of the subcontractRequired property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getSubcontractRequired();

        /**
         * Sets the value of the subcontractRequired property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setSubcontractRequired(java.lang.String value);

        /**
         * Gets the value of the indirectCostRequired property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getIndirectCostRequired();

        /**
         * Sets the value of the indirectCostRequired property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setIndirectCostRequired(java.lang.String value);

        /**
         * Gets the value of the fundingSummaryRequired property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getFundingSummaryRequired();

        /**
         * Sets the value of the fundingSummaryRequired property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setFundingSummaryRequired(java.lang.String value);

        /**
         * Gets the value of the addressListRequired property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getAddressListRequired();

        /**
         * Sets the value of the addressListRequired property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setAddressListRequired(java.lang.String value);

        /**
         * Gets the value of the paymentRequired property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getPaymentRequired();

        /**
         * Sets the value of the paymentRequired property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setPaymentRequired(java.lang.String value);

    }

}