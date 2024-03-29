//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.phs398_modularbudget_v1_1;


/**
 * Java content class for CumBudgetType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/PHS398_ModularBudget-V1.1.xsd line 284)
 * <p>
 * <pre>
 * &lt;complexType name="CumBudgetType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EntirePeriodTotalCost">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="CumulativeDirectCostLessConsortiumFandA" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType"/>
 *                   &lt;element name="CumulativeConsortiumFandA" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType" minOccurs="0"/>
 *                   &lt;element name="CumulativeTotalFundsRequestedDirectCosts" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType"/>
 *                   &lt;element name="CumulativeTotalFundsRequestedIndirectCost" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType" minOccurs="0"/>
 *                   &lt;element name="CumulativeTotalFundsRequestedDirectIndirectCosts" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="BudgetJustifications" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="PersonnelJustification" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="attFile" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="ConsortiumJustification" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="attFile" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="AdditionalNarrativeJustification" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="attFile" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
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
public interface CumBudgetType {


    /**
     * Gets the value of the entirePeriodTotalCost property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.phs398_modularbudget_v1_1.CumBudgetType.EntirePeriodTotalCostType}
     */
    gov.grants.apply.forms.phs398_modularbudget_v1_1.CumBudgetType.EntirePeriodTotalCostType getEntirePeriodTotalCost();

    /**
     * Sets the value of the entirePeriodTotalCost property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.phs398_modularbudget_v1_1.CumBudgetType.EntirePeriodTotalCostType}
     */
    void setEntirePeriodTotalCost(gov.grants.apply.forms.phs398_modularbudget_v1_1.CumBudgetType.EntirePeriodTotalCostType value);

    /**
     * Gets the value of the budgetJustifications property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.phs398_modularbudget_v1_1.CumBudgetType.BudgetJustificationsType}
     */
    gov.grants.apply.forms.phs398_modularbudget_v1_1.CumBudgetType.BudgetJustificationsType getBudgetJustifications();

    /**
     * Sets the value of the budgetJustifications property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.phs398_modularbudget_v1_1.CumBudgetType.BudgetJustificationsType}
     */
    void setBudgetJustifications(gov.grants.apply.forms.phs398_modularbudget_v1_1.CumBudgetType.BudgetJustificationsType value);


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/PHS398_ModularBudget-V1.1.xsd line 298)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="PersonnelJustification" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="attFile" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="ConsortiumJustification" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="attFile" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="AdditionalNarrativeJustification" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="attFile" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType"/>
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
    public interface BudgetJustificationsType {


        /**
         * Gets the value of the personnelJustification property.
         * 
         * @return
         *     possible object is
         *     {@link gov.grants.apply.forms.phs398_modularbudget_v1_1.CumBudgetType.BudgetJustificationsType.PersonnelJustificationType}
         */
        gov.grants.apply.forms.phs398_modularbudget_v1_1.CumBudgetType.BudgetJustificationsType.PersonnelJustificationType getPersonnelJustification();

        /**
         * Sets the value of the personnelJustification property.
         * 
         * @param value
         *     allowed object is
         *     {@link gov.grants.apply.forms.phs398_modularbudget_v1_1.CumBudgetType.BudgetJustificationsType.PersonnelJustificationType}
         */
        void setPersonnelJustification(gov.grants.apply.forms.phs398_modularbudget_v1_1.CumBudgetType.BudgetJustificationsType.PersonnelJustificationType value);

        /**
         * Gets the value of the additionalNarrativeJustification property.
         * 
         * @return
         *     possible object is
         *     {@link gov.grants.apply.forms.phs398_modularbudget_v1_1.CumBudgetType.BudgetJustificationsType.AdditionalNarrativeJustificationType}
         */
        gov.grants.apply.forms.phs398_modularbudget_v1_1.CumBudgetType.BudgetJustificationsType.AdditionalNarrativeJustificationType getAdditionalNarrativeJustification();

        /**
         * Sets the value of the additionalNarrativeJustification property.
         * 
         * @param value
         *     allowed object is
         *     {@link gov.grants.apply.forms.phs398_modularbudget_v1_1.CumBudgetType.BudgetJustificationsType.AdditionalNarrativeJustificationType}
         */
        void setAdditionalNarrativeJustification(gov.grants.apply.forms.phs398_modularbudget_v1_1.CumBudgetType.BudgetJustificationsType.AdditionalNarrativeJustificationType value);

        /**
         * Gets the value of the consortiumJustification property.
         * 
         * @return
         *     possible object is
         *     {@link gov.grants.apply.forms.phs398_modularbudget_v1_1.CumBudgetType.BudgetJustificationsType.ConsortiumJustificationType}
         */
        gov.grants.apply.forms.phs398_modularbudget_v1_1.CumBudgetType.BudgetJustificationsType.ConsortiumJustificationType getConsortiumJustification();

        /**
         * Sets the value of the consortiumJustification property.
         * 
         * @param value
         *     allowed object is
         *     {@link gov.grants.apply.forms.phs398_modularbudget_v1_1.CumBudgetType.BudgetJustificationsType.ConsortiumJustificationType}
         */
        void setConsortiumJustification(gov.grants.apply.forms.phs398_modularbudget_v1_1.CumBudgetType.BudgetJustificationsType.ConsortiumJustificationType value);


        /**
         * Java content class for anonymous complex type.
         * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/PHS398_ModularBudget-V1.1.xsd line 315)
         * <p>
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="attFile" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         */
        public interface AdditionalNarrativeJustificationType {


            /**
             * Gets the value of the attFile property.
             * 
             * @return
             *     possible object is
             *     {@link gov.grants.apply.system.attachments_v1.AttachedFileDataType}
             */
            gov.grants.apply.system.attachments_v1.AttachedFileDataType getAttFile();

            /**
             * Sets the value of the attFile property.
             * 
             * @param value
             *     allowed object is
             *     {@link gov.grants.apply.system.attachments_v1.AttachedFileDataType}
             */
            void setAttFile(gov.grants.apply.system.attachments_v1.AttachedFileDataType value);

        }


        /**
         * Java content class for anonymous complex type.
         * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/PHS398_ModularBudget-V1.1.xsd line 308)
         * <p>
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="attFile" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         */
        public interface ConsortiumJustificationType {


            /**
             * Gets the value of the attFile property.
             * 
             * @return
             *     possible object is
             *     {@link gov.grants.apply.system.attachments_v1.AttachedFileDataType}
             */
            gov.grants.apply.system.attachments_v1.AttachedFileDataType getAttFile();

            /**
             * Sets the value of the attFile property.
             * 
             * @param value
             *     allowed object is
             *     {@link gov.grants.apply.system.attachments_v1.AttachedFileDataType}
             */
            void setAttFile(gov.grants.apply.system.attachments_v1.AttachedFileDataType value);

        }


        /**
         * Java content class for anonymous complex type.
         * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/PHS398_ModularBudget-V1.1.xsd line 301)
         * <p>
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="attFile" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         */
        public interface PersonnelJustificationType {


            /**
             * Gets the value of the attFile property.
             * 
             * @return
             *     possible object is
             *     {@link gov.grants.apply.system.attachments_v1.AttachedFileDataType}
             */
            gov.grants.apply.system.attachments_v1.AttachedFileDataType getAttFile();

            /**
             * Sets the value of the attFile property.
             * 
             * @param value
             *     allowed object is
             *     {@link gov.grants.apply.system.attachments_v1.AttachedFileDataType}
             */
            void setAttFile(gov.grants.apply.system.attachments_v1.AttachedFileDataType value);

        }

    }


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/PHS398_ModularBudget-V1.1.xsd line 287)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="CumulativeDirectCostLessConsortiumFandA" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType"/>
     *         &lt;element name="CumulativeConsortiumFandA" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType" minOccurs="0"/>
     *         &lt;element name="CumulativeTotalFundsRequestedDirectCosts" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType"/>
     *         &lt;element name="CumulativeTotalFundsRequestedIndirectCost" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType" minOccurs="0"/>
     *         &lt;element name="CumulativeTotalFundsRequestedDirectIndirectCosts" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface EntirePeriodTotalCostType {


        /**
         * Gets the value of the cumulativeTotalFundsRequestedDirectIndirectCosts property.
         * 
         * @return
         *     possible object is
         *     {@link java.math.BigDecimal}
         */
        java.math.BigDecimal getCumulativeTotalFundsRequestedDirectIndirectCosts();

        /**
         * Sets the value of the cumulativeTotalFundsRequestedDirectIndirectCosts property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.math.BigDecimal}
         */
        void setCumulativeTotalFundsRequestedDirectIndirectCosts(java.math.BigDecimal value);

        /**
         * Gets the value of the cumulativeTotalFundsRequestedDirectCosts property.
         * 
         * @return
         *     possible object is
         *     {@link java.math.BigDecimal}
         */
        java.math.BigDecimal getCumulativeTotalFundsRequestedDirectCosts();

        /**
         * Sets the value of the cumulativeTotalFundsRequestedDirectCosts property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.math.BigDecimal}
         */
        void setCumulativeTotalFundsRequestedDirectCosts(java.math.BigDecimal value);

        /**
         * Gets the value of the cumulativeTotalFundsRequestedIndirectCost property.
         * 
         * @return
         *     possible object is
         *     {@link java.math.BigDecimal}
         */
        java.math.BigDecimal getCumulativeTotalFundsRequestedIndirectCost();

        /**
         * Sets the value of the cumulativeTotalFundsRequestedIndirectCost property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.math.BigDecimal}
         */
        void setCumulativeTotalFundsRequestedIndirectCost(java.math.BigDecimal value);

        /**
         * Gets the value of the cumulativeConsortiumFandA property.
         * 
         * @return
         *     possible object is
         *     {@link java.math.BigDecimal}
         */
        java.math.BigDecimal getCumulativeConsortiumFandA();

        /**
         * Sets the value of the cumulativeConsortiumFandA property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.math.BigDecimal}
         */
        void setCumulativeConsortiumFandA(java.math.BigDecimal value);

        /**
         * Gets the value of the cumulativeDirectCostLessConsortiumFandA property.
         * 
         * @return
         *     possible object is
         *     {@link java.math.BigDecimal}
         */
        java.math.BigDecimal getCumulativeDirectCostLessConsortiumFandA();

        /**
         * Sets the value of the cumulativeDirectCostLessConsortiumFandA property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.math.BigDecimal}
         */
        void setCumulativeDirectCostLessConsortiumFandA(java.math.BigDecimal value);

    }

}
