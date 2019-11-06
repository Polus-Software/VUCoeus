//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.phs398_modularbudget_v1_1;


/**
 * Java content class for Period3Type complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/PHS398_ModularBudget-V1.1.xsd line 152)
 * <p>
 * <pre>
 * &lt;complexType name="Period3Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BudgetPeriodStartDate3" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="BudgetPeriodEndDate3" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="BudgetPeriod3">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *             &lt;minInclusive value="0"/>
 *             &lt;maxInclusive value="5"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="DirectCost3">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="DirectCostLessConsortiumFandA3" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType"/>
 *                   &lt;element name="ConsortiumFandA3" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                   &lt;element name="TotalFundsRequestedDirectCosts3" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="IndirectCost3" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="IndirectCostItems3" maxOccurs="4" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="IndirectCostTypeDescription" minOccurs="0">
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                 &lt;minLength value="0"/>
 *                                 &lt;maxLength value="64"/>
 *                               &lt;/restriction>
 *                             &lt;/element>
 *                             &lt;element name="IndirectCostRate" minOccurs="0">
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                 &lt;minInclusive value="0.00"/>
 *                                 &lt;maxInclusive value="999.99"/>
 *                                 &lt;totalDigits value="5"/>
 *                                 &lt;fractionDigits value="2"/>
 *                               &lt;/restriction>
 *                             &lt;/element>
 *                             &lt;element name="IndirectCostBase" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                             &lt;element name="IndirectCostFundsRequested" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="CognizantFederalAgency3" minOccurs="0">
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;minLength value="0"/>
 *                       &lt;maxLength value="180"/>
 *                     &lt;/restriction>
 *                   &lt;/element>
 *                   &lt;element name="IndirectCostAgreementDate3" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *                   &lt;element name="TotalFundsRequestedIndirectCost3" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="TotalFundsRequestedDirectIndirectCosts3" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface Period3Type {


    /**
     * Gets the value of the totalFundsRequestedDirectIndirectCosts3 property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getTotalFundsRequestedDirectIndirectCosts3();

    /**
     * Sets the value of the totalFundsRequestedDirectIndirectCosts3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setTotalFundsRequestedDirectIndirectCosts3(java.math.BigDecimal value);

    /**
     * Gets the value of the budgetPeriod3 property.
     * 
     */
    int getBudgetPeriod3();

    /**
     * Sets the value of the budgetPeriod3 property.
     * 
     */
    void setBudgetPeriod3(int value);

    /**
     * Gets the value of the indirectCost3 property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.phs398_modularbudget_v1_1.Period3Type.IndirectCost3Type}
     */
    gov.grants.apply.forms.phs398_modularbudget_v1_1.Period3Type.IndirectCost3Type getIndirectCost3();

    /**
     * Sets the value of the indirectCost3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.phs398_modularbudget_v1_1.Period3Type.IndirectCost3Type}
     */
    void setIndirectCost3(gov.grants.apply.forms.phs398_modularbudget_v1_1.Period3Type.IndirectCost3Type value);

    /**
     * Gets the value of the budgetPeriodEndDate3 property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getBudgetPeriodEndDate3();

    /**
     * Sets the value of the budgetPeriodEndDate3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setBudgetPeriodEndDate3(java.util.Calendar value);

    /**
     * Gets the value of the budgetPeriodStartDate3 property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getBudgetPeriodStartDate3();

    /**
     * Sets the value of the budgetPeriodStartDate3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setBudgetPeriodStartDate3(java.util.Calendar value);

    /**
     * Gets the value of the directCost3 property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.phs398_modularbudget_v1_1.Period3Type.DirectCost3Type}
     */
    gov.grants.apply.forms.phs398_modularbudget_v1_1.Period3Type.DirectCost3Type getDirectCost3();

    /**
     * Sets the value of the directCost3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.phs398_modularbudget_v1_1.Period3Type.DirectCost3Type}
     */
    void setDirectCost3(gov.grants.apply.forms.phs398_modularbudget_v1_1.Period3Type.DirectCost3Type value);


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/PHS398_ModularBudget-V1.1.xsd line 165)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="DirectCostLessConsortiumFandA3" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType"/>
     *         &lt;element name="ConsortiumFandA3" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
     *         &lt;element name="TotalFundsRequestedDirectCosts3" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface DirectCost3Type {


        /**
         * Gets the value of the totalFundsRequestedDirectCosts3 property.
         * 
         * @return
         *     possible object is
         *     {@link java.math.BigDecimal}
         */
        java.math.BigDecimal getTotalFundsRequestedDirectCosts3();

        /**
         * Sets the value of the totalFundsRequestedDirectCosts3 property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.math.BigDecimal}
         */
        void setTotalFundsRequestedDirectCosts3(java.math.BigDecimal value);

        /**
         * Gets the value of the consortiumFandA3 property.
         * 
         * @return
         *     possible object is
         *     {@link java.math.BigDecimal}
         */
        java.math.BigDecimal getConsortiumFandA3();

        /**
         * Sets the value of the consortiumFandA3 property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.math.BigDecimal}
         */
        void setConsortiumFandA3(java.math.BigDecimal value);

        /**
         * Gets the value of the directCostLessConsortiumFandA3 property.
         * 
         * @return
         *     possible object is
         *     {@link java.math.BigDecimal}
         */
        java.math.BigDecimal getDirectCostLessConsortiumFandA3();

        /**
         * Sets the value of the directCostLessConsortiumFandA3 property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.math.BigDecimal}
         */
        void setDirectCostLessConsortiumFandA3(java.math.BigDecimal value);

    }


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/PHS398_ModularBudget-V1.1.xsd line 174)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="IndirectCostItems3" maxOccurs="4" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="IndirectCostTypeDescription" minOccurs="0">
     *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                       &lt;minLength value="0"/>
     *                       &lt;maxLength value="64"/>
     *                     &lt;/restriction>
     *                   &lt;/element>
     *                   &lt;element name="IndirectCostRate" minOccurs="0">
     *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *                       &lt;minInclusive value="0.00"/>
     *                       &lt;maxInclusive value="999.99"/>
     *                       &lt;totalDigits value="5"/>
     *                       &lt;fractionDigits value="2"/>
     *                     &lt;/restriction>
     *                   &lt;/element>
     *                   &lt;element name="IndirectCostBase" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
     *                   &lt;element name="IndirectCostFundsRequested" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="CognizantFederalAgency3" minOccurs="0">
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;minLength value="0"/>
     *             &lt;maxLength value="180"/>
     *           &lt;/restriction>
     *         &lt;/element>
     *         &lt;element name="IndirectCostAgreementDate3" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
     *         &lt;element name="TotalFundsRequestedIndirectCost3" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface IndirectCost3Type {


        /**
         * Gets the value of the IndirectCostItems3 property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the IndirectCostItems3 property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getIndirectCostItems3().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link gov.grants.apply.forms.phs398_modularbudget_v1_1.Period3Type.IndirectCost3Type.IndirectCostItems3Type}
         * 
         */
        java.util.List getIndirectCostItems3();

        /**
         * Gets the value of the indirectCostAgreementDate3 property.
         * 
         * @return
         *     possible object is
         *     {@link java.util.Calendar}
         */
        java.util.Calendar getIndirectCostAgreementDate3();

        /**
         * Sets the value of the indirectCostAgreementDate3 property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.util.Calendar}
         */
        void setIndirectCostAgreementDate3(java.util.Calendar value);

        /**
         * Gets the value of the cognizantFederalAgency3 property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCognizantFederalAgency3();

        /**
         * Sets the value of the cognizantFederalAgency3 property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCognizantFederalAgency3(java.lang.String value);

        /**
         * Gets the value of the totalFundsRequestedIndirectCost3 property.
         * 
         * @return
         *     possible object is
         *     {@link java.math.BigDecimal}
         */
        java.math.BigDecimal getTotalFundsRequestedIndirectCost3();

        /**
         * Sets the value of the totalFundsRequestedIndirectCost3 property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.math.BigDecimal}
         */
        void setTotalFundsRequestedIndirectCost3(java.math.BigDecimal value);


        /**
         * Java content class for anonymous complex type.
         * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/PHS398_ModularBudget-V1.1.xsd line 177)
         * <p>
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="IndirectCostTypeDescription" minOccurs="0">
         *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *             &lt;minLength value="0"/>
         *             &lt;maxLength value="64"/>
         *           &lt;/restriction>
         *         &lt;/element>
         *         &lt;element name="IndirectCostRate" minOccurs="0">
         *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
         *             &lt;minInclusive value="0.00"/>
         *             &lt;maxInclusive value="999.99"/>
         *             &lt;totalDigits value="5"/>
         *             &lt;fractionDigits value="2"/>
         *           &lt;/restriction>
         *         &lt;/element>
         *         &lt;element name="IndirectCostBase" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
         *         &lt;element name="IndirectCostFundsRequested" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         */
        public interface IndirectCostItems3Type {


            /**
             * Gets the value of the indirectCostTypeDescription property.
             * 
             * @return
             *     possible object is
             *     {@link java.lang.String}
             */
            java.lang.String getIndirectCostTypeDescription();

            /**
             * Sets the value of the indirectCostTypeDescription property.
             * 
             * @param value
             *     allowed object is
             *     {@link java.lang.String}
             */
            void setIndirectCostTypeDescription(java.lang.String value);

            /**
             * Gets the value of the indirectCostRate property.
             * 
             * @return
             *     possible object is
             *     {@link java.math.BigDecimal}
             */
            java.math.BigDecimal getIndirectCostRate();

            /**
             * Sets the value of the indirectCostRate property.
             * 
             * @param value
             *     allowed object is
             *     {@link java.math.BigDecimal}
             */
            void setIndirectCostRate(java.math.BigDecimal value);

            /**
             * Gets the value of the indirectCostBase property.
             * 
             * @return
             *     possible object is
             *     {@link java.math.BigDecimal}
             */
            java.math.BigDecimal getIndirectCostBase();

            /**
             * Sets the value of the indirectCostBase property.
             * 
             * @param value
             *     allowed object is
             *     {@link java.math.BigDecimal}
             */
            void setIndirectCostBase(java.math.BigDecimal value);

            /**
             * Gets the value of the indirectCostFundsRequested property.
             * 
             * @return
             *     possible object is
             *     {@link java.math.BigDecimal}
             */
            java.math.BigDecimal getIndirectCostFundsRequested();

            /**
             * Sets the value of the indirectCostFundsRequested property.
             * 
             * @param value
             *     allowed object is
             *     {@link java.math.BigDecimal}
             */
            void setIndirectCostFundsRequested(java.math.BigDecimal value);

        }

    }

}
