//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.rr_subawardbudget_v1_1;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/Coeus/S2S/build/cust/RR_SubawardBudget-V1.1.xsd line 12)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ATT1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ATT2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ATT3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ATT4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ATT5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ATT6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ATT7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ATT8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ATT9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ATT10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BudgetAttachments" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://apply.grants.gov/forms/RR_Budget-V1.0}RR_Budget" maxOccurs="10" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="FormVersion" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}FormVersionDataType" fixed="1.1" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface RRSubawardBudgetType {


    /**
     * Gets the value of the att4 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getATT4();

    /**
     * Sets the value of the att4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setATT4(java.lang.String value);

    /**
     * Gets the value of the att2 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getATT2();

    /**
     * Sets the value of the att2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setATT2(java.lang.String value);

    /**
     * Gets the value of the att1 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getATT1();

    /**
     * Sets the value of the att1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setATT1(java.lang.String value);

    /**
     * Gets the value of the att6 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getATT6();

    /**
     * Sets the value of the att6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setATT6(java.lang.String value);

    /**
     * Gets the value of the formVersion property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link java.lang.String}
     */
    java.lang.String getFormVersion();

    /**
     * Sets the value of the formVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link java.lang.String}
     */
    void setFormVersion(java.lang.String value);

    /**
     * Gets the value of the att8 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getATT8();

    /**
     * Sets the value of the att8 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setATT8(java.lang.String value);

    /**
     * Gets the value of the att3 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getATT3();

    /**
     * Sets the value of the att3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setATT3(java.lang.String value);

    /**
     * Gets the value of the att5 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getATT5();

    /**
     * Sets the value of the att5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setATT5(java.lang.String value);

    /**
     * Gets the value of the budgetAttachments property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.rr_subawardbudget_v1_1.RRSubawardBudgetType.BudgetAttachmentsType}
     */
    gov.grants.apply.forms.rr_subawardbudget_v1_1.RRSubawardBudgetType.BudgetAttachmentsType getBudgetAttachments();

    /**
     * Sets the value of the budgetAttachments property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.rr_subawardbudget_v1_1.RRSubawardBudgetType.BudgetAttachmentsType}
     */
    void setBudgetAttachments(gov.grants.apply.forms.rr_subawardbudget_v1_1.RRSubawardBudgetType.BudgetAttachmentsType value);

    /**
     * Gets the value of the att10 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getATT10();

    /**
     * Sets the value of the att10 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setATT10(java.lang.String value);

    /**
     * Gets the value of the att7 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getATT7();

    /**
     * Sets the value of the att7 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setATT7(java.lang.String value);

    /**
     * Gets the value of the att9 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getATT9();

    /**
     * Sets the value of the att9 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setATT9(java.lang.String value);


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/Coeus/S2S/build/cust/RR_SubawardBudget-V1.1.xsd line 25)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element ref="{http://apply.grants.gov/forms/RR_Budget-V1.0}RR_Budget" maxOccurs="10" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface BudgetAttachmentsType {


        /**
         * Gets the value of the RRBudget property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the RRBudget property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getRRBudget().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link gov.grants.apply.forms.rr_budget_v1.RRBudgetType}
         * {@link gov.grants.apply.forms.rr_budget_v1.RRBudget}
         * 
         */
        java.util.List getRRBudget();

    }

}
