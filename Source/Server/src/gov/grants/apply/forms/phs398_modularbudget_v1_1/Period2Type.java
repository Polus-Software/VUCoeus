//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.phs398_modularbudget_v1_1;


/**
 * Java content class for Period2Type complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/PHS398_ModularBudget-V1.1.xsd line 86)
 * <p>
 * <pre>
 * &lt;complexType name="Period2Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BudgetPeriodStartDate2" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="BudgetPeriodEndDate2" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="BudgetPeriod2">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *             &lt;minInclusive value="0"/>
 *             &lt;maxInclusive value="5"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="DirectCost2">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="DirectCostLessConsortiumFandA2" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType"/>
 *                   &lt;element name="ConsortiumFandA2" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                   &lt;element name="TotalFundsRequestedDirectCosts2" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="IndirectCost2" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="IndirectCostItems2" maxOccurs="4" minOccurs="0">
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
 *                   &lt;element name="CognizantFederalAgency2" minOccurs="0">
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;minLength value="0"/>
 *                       &lt;maxLength value="180"/>
 *                     &lt;/restriction>
 *                   &lt;/element>
 *                   &lt;element name="IndirectCostAgreementDate2" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *                   &lt;element name="TotalFundsRequestedIndirectCost2" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="TotalFundsRequestedDirectIndirectCosts2" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface Period2Type {


    /**
     * Gets the value of the directCost2 property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.phs398_modularbudget_v1_1.Period2Type.DirectCost2Type}
     */
    gov.grants.apply.forms.phs398_modularbudget_v1_1.Period2Type.DirectCost2Type getDirectCost2();

    /**
     * Sets the value of the directCost2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.phs398_modularbudget_v1_1.Period2Type.DirectCost2Type}
     */
    void setDirectCost2(gov.grants.apply.forms.phs398_modularbudget_v1_1.Period2Type.DirectCost2Type value);

    /**
     * Gets the value of the totalFundsRequestedDirectIndirectCosts2 property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getTotalFundsRequestedDirectIndirectCosts2();

    /**
     * Sets the value of the totalFundsRequestedDirectIndirectCosts2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setTotalFundsRequestedDirectIndirectCosts2(java.math.BigDecimal value);

    /**
     * Gets the value of the indirectCost2 property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.phs398_modularbudget_v1_1.Period2Type.IndirectCost2Type}
     */
    gov.grants.apply.forms.phs398_modularbudget_v1_1.Period2Type.IndirectCost2Type getIndirectCost2();

    /**
     * Sets the value of the indirectCost2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.phs398_modularbudget_v1_1.Period2Type.IndirectCost2Type}
     */
    void setIndirectCost2(gov.grants.apply.forms.phs398_modularbudget_v1_1.Period2Type.IndirectCost2Type value);

    /**
     * Gets the value of the budgetPeriod2 property.
     * 
     */
    int getBudgetPeriod2();

    /**
     * Sets the value of the budgetPeriod2 property.
     * 
     */
    void setBudgetPeriod2(int value);

    /**
     * Gets the value of the budgetPeriodStartDate2 property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getBudgetPeriodStartDate2();

    /**
     * Sets the value of the budgetPeriodStartDate2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setBudgetPeriodStartDate2(java.util.Calendar value);

    /**
     * Gets the value of the budgetPeriodEndDate2 property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getBudgetPeriodEndDate2();

    /**
     * Sets the value of the budgetPeriodEndDate2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setBudgetPeriodEndDate2(java.util.Calendar value);


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/PHS398_ModularBudget-V1.1.xsd line 99)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="DirectCostLessConsortiumFandA2" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType"/>
     *         &lt;element name="ConsortiumFandA2" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
     *         &lt;element name="TotalFundsRequestedDirectCosts2" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface DirectCost2Type {


        /**
         * Gets the value of the totalFundsRequestedDirectCosts2 property.
         * 
         * @return
         *     possible object is
         *     {@link java.math.BigDecimal}
         */
        java.math.BigDecimal getTotalFundsRequestedDirectCosts2();

        /**
         * Sets the value of the totalFundsRequestedDirectCosts2 property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.math.BigDecimal}
         */
        void setTotalFundsRequestedDirectCosts2(java.math.BigDecimal value);

        /**
         * Gets the value of the directCostLessConsortiumFandA2 property.
         * 
         * @return
         *     possible object is
         *     {@link java.math.BigDecimal}
         */
        java.math.BigDecimal getDirectCostLessConsortiumFandA2();

        /**
         * Sets the value of the directCostLessConsortiumFandA2 property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.math.BigDecimal}
         */
        void setDirectCostLessConsortiumFandA2(java.math.BigDecimal value);

        /**
         * Gets the value of the consortiumFandA2 property.
         * 
         * @return
         *     possible object is
         *     {@link java.math.BigDecimal}
         */
        java.math.BigDecimal getConsortiumFandA2();

        /**
         * Sets the value of the consortiumFandA2 property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.math.BigDecimal}
         */
        void setConsortiumFandA2(java.math.BigDecimal value);

    }


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/PHS398_ModularBudget-V1.1.xsd line 108)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="IndirectCostItems2" maxOccurs="4" minOccurs="0">
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
     *         &lt;element name="CognizantFederalAgency2" minOccurs="0">
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;minLength value="0"/>
     *             &lt;maxLength value="180"/>
     *           &lt;/restriction>
     *         &lt;/element>
     *         &lt;element name="IndirectCostAgreementDate2" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
     *         &lt;element name="TotalFundsRequestedIndirectCost2" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface IndirectCost2Type {


        /**
         * Gets the value of the cognizantFederalAgency2 property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCognizantFederalAgency2();

        /**
         * Sets the value of the cognizantFederalAgency2 property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCognizantFederalAgency2(java.lang.String value);

        /**
         * Gets the value of the totalFundsRequestedIndirectCost2 property.
         * 
         * @return
         *     possible object is
         *     {@link java.math.BigDecimal}
         */
        java.math.BigDecimal getTotalFundsRequestedIndirectCost2();

        /**
         * Sets the value of the totalFundsRequestedIndirectCost2 property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.math.BigDecimal}
         */
        void setTotalFundsRequestedIndirectCost2(java.math.BigDecimal value);

        /**
         * Gets the value of the IndirectCostItems2 property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the IndirectCostItems2 property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getIndirectCostItems2().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link gov.grants.apply.forms.phs398_modularbudget_v1_1.Period2Type.IndirectCost2Type.IndirectCostItems2Type}
         * 
         */
        java.util.List getIndirectCostItems2();

        /**
         * Gets the value of the indirectCostAgreementDate2 property.
         * 
         * @return
         *     possible object is
         *     {@link java.util.Calendar}
         */
        java.util.Calendar getIndirectCostAgreementDate2();

        /**
         * Sets the value of the indirectCostAgreementDate2 property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.util.Calendar}
         */
        void setIndirectCostAgreementDate2(java.util.Calendar value);


        /**
         * Java content class for anonymous complex type.
         * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/PHS398_ModularBudget-V1.1.xsd line 111)
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
        public interface IndirectCostItems2Type {


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
