//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.phs398_researchplan_1_2_v1_2;


/**
 * Java content class for OtherResearchPlanSectionsType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/PHS398_ResearchPlan_1_2-V1.2.xsd line 122)
 * <p>
 * <pre>
 * &lt;complexType name="OtherResearchPlanSectionsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="VertebrateAnimals" minOccurs="0">
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
 *         &lt;element name="SelectAgentResearch" minOccurs="0">
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
 *         &lt;element name="MultiplePDPILeadershipPlan" minOccurs="0">
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
 *         &lt;element name="ConsortiumContractualArrangements" minOccurs="0">
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
 *         &lt;element name="LettersOfSupport" minOccurs="0">
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
 *         &lt;element name="ResourceSharingPlans" minOccurs="0">
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
public interface OtherResearchPlanSectionsType {


    /**
     * Gets the value of the lettersOfSupport property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.LettersOfSupportType}
     */
    gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.LettersOfSupportType getLettersOfSupport();

    /**
     * Sets the value of the lettersOfSupport property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.LettersOfSupportType}
     */
    void setLettersOfSupport(gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.LettersOfSupportType value);

    /**
     * Gets the value of the consortiumContractualArrangements property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.ConsortiumContractualArrangementsType}
     */
    gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.ConsortiumContractualArrangementsType getConsortiumContractualArrangements();

    /**
     * Sets the value of the consortiumContractualArrangements property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.ConsortiumContractualArrangementsType}
     */
    void setConsortiumContractualArrangements(gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.ConsortiumContractualArrangementsType value);

    /**
     * Gets the value of the selectAgentResearch property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.SelectAgentResearchType}
     */
    gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.SelectAgentResearchType getSelectAgentResearch();

    /**
     * Sets the value of the selectAgentResearch property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.SelectAgentResearchType}
     */
    void setSelectAgentResearch(gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.SelectAgentResearchType value);

    /**
     * Gets the value of the vertebrateAnimals property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.VertebrateAnimalsType}
     */
    gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.VertebrateAnimalsType getVertebrateAnimals();

    /**
     * Sets the value of the vertebrateAnimals property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.VertebrateAnimalsType}
     */
    void setVertebrateAnimals(gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.VertebrateAnimalsType value);

    /**
     * Gets the value of the resourceSharingPlans property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.ResourceSharingPlansType}
     */
    gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.ResourceSharingPlansType getResourceSharingPlans();

    /**
     * Sets the value of the resourceSharingPlans property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.ResourceSharingPlansType}
     */
    void setResourceSharingPlans(gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.ResourceSharingPlansType value);

    /**
     * Gets the value of the multiplePDPILeadershipPlan property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.MultiplePDPILeadershipPlanType}
     */
    gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.MultiplePDPILeadershipPlanType getMultiplePDPILeadershipPlan();

    /**
     * Sets the value of the multiplePDPILeadershipPlan property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.MultiplePDPILeadershipPlanType}
     */
    void setMultiplePDPILeadershipPlan(gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.MultiplePDPILeadershipPlanType value);


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/PHS398_ResearchPlan_1_2-V1.2.xsd line 146)
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
    public interface ConsortiumContractualArrangementsType {


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
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/PHS398_ResearchPlan_1_2-V1.2.xsd line 153)
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
    public interface LettersOfSupportType {


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
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/PHS398_ResearchPlan_1_2-V1.2.xsd line 139)
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
    public interface MultiplePDPILeadershipPlanType {


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
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/PHS398_ResearchPlan_1_2-V1.2.xsd line 160)
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
    public interface ResourceSharingPlansType {


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
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/PHS398_ResearchPlan_1_2-V1.2.xsd line 132)
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
    public interface SelectAgentResearchType {


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
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/PHS398_ResearchPlan_1_2-V1.2.xsd line 125)
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
    public interface VertebrateAnimalsType {


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
