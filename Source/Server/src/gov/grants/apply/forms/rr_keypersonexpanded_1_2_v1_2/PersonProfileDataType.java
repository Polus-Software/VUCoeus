//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.rr_keypersonexpanded_1_2_v1_2;


/**
 * Java content class for PersonProfileDataType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/RR_KeyPersonExpanded_1_2-V1.2.xsd line 37)
 * <p>
 * <pre>
 * &lt;complexType name="PersonProfileDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Profile">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Name" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}HumanNameDataType"/>
 *                   &lt;element name="Title" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}HumanTitleDataType" minOccurs="0"/>
 *                   &lt;element name="Address" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}AddressDataType"/>
 *                   &lt;element name="Phone" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}TelephoneNumberDataType"/>
 *                   &lt;element name="Fax" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}TelephoneNumberDataType" minOccurs="0"/>
 *                   &lt;element name="Email" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}EmailDataType"/>
 *                   &lt;element name="OrganizationName" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}OrganizationNameDataType" minOccurs="0"/>
 *                   &lt;element name="DepartmentName" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}DepartmentNameDataType" minOccurs="0"/>
 *                   &lt;element name="DivisionName" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}DivisionNameDataType" minOccurs="0"/>
 *                   &lt;element name="Credential" minOccurs="0">
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;minLength value="1"/>
 *                       &lt;maxLength value="60"/>
 *                     &lt;/restriction>
 *                   &lt;/element>
 *                   &lt;element name="ProjectRole" type="{http://apply.grants.gov/forms/RR_KeyPersonExpanded_1_2-V1-2}ProjectRoleDataType"/>
 *                   &lt;element name="OtherProjectRoleCategory" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;simpleContent>
 *                         &lt;extension base="&lt;http://apply.grants.gov/forms/RR_KeyPersonExpanded_1_2-V1-2>OtherProjectRoleStringDataType">
 *                         &lt;/extension>
 *                       &lt;/simpleContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="DegreeType" minOccurs="0">
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;minLength value="1"/>
 *                       &lt;maxLength value="75"/>
 *                     &lt;/restriction>
 *                   &lt;/element>
 *                   &lt;element name="DegreeYear" minOccurs="0">
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;minLength value="1"/>
 *                       &lt;maxLength value="25"/>
 *                     &lt;/restriction>
 *                   &lt;/element>
 *                   &lt;element name="BioSketchsAttached">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="BioSketchAttached" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="SupportsAttached" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="SupportAttached" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType" minOccurs="0"/>
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
public interface PersonProfileDataType {


    /**
     * Gets the value of the profile property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.rr_keypersonexpanded_1_2_v1_2.PersonProfileDataType.ProfileType}
     */
    gov.grants.apply.forms.rr_keypersonexpanded_1_2_v1_2.PersonProfileDataType.ProfileType getProfile();

    /**
     * Sets the value of the profile property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.rr_keypersonexpanded_1_2_v1_2.PersonProfileDataType.ProfileType}
     */
    void setProfile(gov.grants.apply.forms.rr_keypersonexpanded_1_2_v1_2.PersonProfileDataType.ProfileType value);


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/RR_KeyPersonExpanded_1_2-V1.2.xsd line 40)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Name" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}HumanNameDataType"/>
     *         &lt;element name="Title" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}HumanTitleDataType" minOccurs="0"/>
     *         &lt;element name="Address" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}AddressDataType"/>
     *         &lt;element name="Phone" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}TelephoneNumberDataType"/>
     *         &lt;element name="Fax" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}TelephoneNumberDataType" minOccurs="0"/>
     *         &lt;element name="Email" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}EmailDataType"/>
     *         &lt;element name="OrganizationName" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}OrganizationNameDataType" minOccurs="0"/>
     *         &lt;element name="DepartmentName" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}DepartmentNameDataType" minOccurs="0"/>
     *         &lt;element name="DivisionName" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}DivisionNameDataType" minOccurs="0"/>
     *         &lt;element name="Credential" minOccurs="0">
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;minLength value="1"/>
     *             &lt;maxLength value="60"/>
     *           &lt;/restriction>
     *         &lt;/element>
     *         &lt;element name="ProjectRole" type="{http://apply.grants.gov/forms/RR_KeyPersonExpanded_1_2-V1-2}ProjectRoleDataType"/>
     *         &lt;element name="OtherProjectRoleCategory" minOccurs="0">
     *           &lt;complexType>
     *             &lt;simpleContent>
     *               &lt;extension base="&lt;http://apply.grants.gov/forms/RR_KeyPersonExpanded_1_2-V1-2>OtherProjectRoleStringDataType">
     *               &lt;/extension>
     *             &lt;/simpleContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="DegreeType" minOccurs="0">
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;minLength value="1"/>
     *             &lt;maxLength value="75"/>
     *           &lt;/restriction>
     *         &lt;/element>
     *         &lt;element name="DegreeYear" minOccurs="0">
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;minLength value="1"/>
     *             &lt;maxLength value="25"/>
     *           &lt;/restriction>
     *         &lt;/element>
     *         &lt;element name="BioSketchsAttached">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="BioSketchAttached" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="SupportsAttached" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="SupportAttached" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType" minOccurs="0"/>
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
    public interface ProfileType {


        /**
         * Gets the value of the credential property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCredential();

        /**
         * Sets the value of the credential property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCredential(java.lang.String value);

        /**
         * Gets the value of the supportsAttached property.
         * 
         * @return
         *     possible object is
         *     {@link gov.grants.apply.forms.rr_keypersonexpanded_1_2_v1_2.PersonProfileDataType.ProfileType.SupportsAttachedType}
         */
        gov.grants.apply.forms.rr_keypersonexpanded_1_2_v1_2.PersonProfileDataType.ProfileType.SupportsAttachedType getSupportsAttached();

        /**
         * Sets the value of the supportsAttached property.
         * 
         * @param value
         *     allowed object is
         *     {@link gov.grants.apply.forms.rr_keypersonexpanded_1_2_v1_2.PersonProfileDataType.ProfileType.SupportsAttachedType}
         */
        void setSupportsAttached(gov.grants.apply.forms.rr_keypersonexpanded_1_2_v1_2.PersonProfileDataType.ProfileType.SupportsAttachedType value);

        /**
         * Gets the value of the bioSketchsAttached property.
         * 
         * @return
         *     possible object is
         *     {@link gov.grants.apply.forms.rr_keypersonexpanded_1_2_v1_2.PersonProfileDataType.ProfileType.BioSketchsAttachedType}
         */
        gov.grants.apply.forms.rr_keypersonexpanded_1_2_v1_2.PersonProfileDataType.ProfileType.BioSketchsAttachedType getBioSketchsAttached();

        /**
         * Sets the value of the bioSketchsAttached property.
         * 
         * @param value
         *     allowed object is
         *     {@link gov.grants.apply.forms.rr_keypersonexpanded_1_2_v1_2.PersonProfileDataType.ProfileType.BioSketchsAttachedType}
         */
        void setBioSketchsAttached(gov.grants.apply.forms.rr_keypersonexpanded_1_2_v1_2.PersonProfileDataType.ProfileType.BioSketchsAttachedType value);

        /**
         * Gets the value of the phone property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getPhone();

        /**
         * Sets the value of the phone property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setPhone(java.lang.String value);

        /**
         * Gets the value of the degreeType property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getDegreeType();

        /**
         * Sets the value of the degreeType property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setDegreeType(java.lang.String value);

        /**
         * Gets the value of the departmentName property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getDepartmentName();

        /**
         * Sets the value of the departmentName property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setDepartmentName(java.lang.String value);

        /**
         * Gets the value of the degreeYear property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getDegreeYear();

        /**
         * Sets the value of the degreeYear property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setDegreeYear(java.lang.String value);

        /**
         * Gets the value of the email property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getEmail();

        /**
         * Sets the value of the email property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setEmail(java.lang.String value);

        /**
         * Gets the value of the divisionName property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getDivisionName();

        /**
         * Sets the value of the divisionName property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setDivisionName(java.lang.String value);

        /**
         * Gets the value of the fax property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getFax();

        /**
         * Sets the value of the fax property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setFax(java.lang.String value);

        /**
         * Gets the value of the projectRole property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getProjectRole();

        /**
         * Sets the value of the projectRole property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setProjectRole(java.lang.String value);

        /**
         * Gets the value of the otherProjectRoleCategory property.
         * 
         * @return
         *     possible object is
         *     {@link gov.grants.apply.forms.rr_keypersonexpanded_1_2_v1_2.PersonProfileDataType.ProfileType.OtherProjectRoleCategoryType}
         */
        gov.grants.apply.forms.rr_keypersonexpanded_1_2_v1_2.PersonProfileDataType.ProfileType.OtherProjectRoleCategoryType getOtherProjectRoleCategory();

        /**
         * Sets the value of the otherProjectRoleCategory property.
         * 
         * @param value
         *     allowed object is
         *     {@link gov.grants.apply.forms.rr_keypersonexpanded_1_2_v1_2.PersonProfileDataType.ProfileType.OtherProjectRoleCategoryType}
         */
        void setOtherProjectRoleCategory(gov.grants.apply.forms.rr_keypersonexpanded_1_2_v1_2.PersonProfileDataType.ProfileType.OtherProjectRoleCategoryType value);

        /**
         * Gets the value of the address property.
         * 
         * @return
         *     possible object is
         *     {@link gov.grants.apply.system.globallibrary_v2.AddressDataType}
         */
        gov.grants.apply.system.globallibrary_v2.AddressDataType getAddress();

        /**
         * Sets the value of the address property.
         * 
         * @param value
         *     allowed object is
         *     {@link gov.grants.apply.system.globallibrary_v2.AddressDataType}
         */
        void setAddress(gov.grants.apply.system.globallibrary_v2.AddressDataType value);

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
         * Gets the value of the organizationName property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getOrganizationName();

        /**
         * Sets the value of the organizationName property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setOrganizationName(java.lang.String value);

        /**
         * Gets the value of the name property.
         * 
         * @return
         *     possible object is
         *     {@link gov.grants.apply.system.globallibrary_v2.HumanNameDataType}
         */
        gov.grants.apply.system.globallibrary_v2.HumanNameDataType getName();

        /**
         * Sets the value of the name property.
         * 
         * @param value
         *     allowed object is
         *     {@link gov.grants.apply.system.globallibrary_v2.HumanNameDataType}
         */
        void setName(gov.grants.apply.system.globallibrary_v2.HumanNameDataType value);


        /**
         * Java content class for anonymous complex type.
         * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/RR_KeyPersonExpanded_1_2-V1.2.xsd line 84)
         * <p>
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="BioSketchAttached" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         */
        public interface BioSketchsAttachedType {


            /**
             * Gets the value of the bioSketchAttached property.
             * 
             * @return
             *     possible object is
             *     {@link gov.grants.apply.system.attachments_v1.AttachedFileDataType}
             */
            gov.grants.apply.system.attachments_v1.AttachedFileDataType getBioSketchAttached();

            /**
             * Sets the value of the bioSketchAttached property.
             * 
             * @param value
             *     allowed object is
             *     {@link gov.grants.apply.system.attachments_v1.AttachedFileDataType}
             */
            void setBioSketchAttached(gov.grants.apply.system.attachments_v1.AttachedFileDataType value);

        }


        /**
         * Java content class for anonymous complex type.
         * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/RR_KeyPersonExpanded_1_2-V1.2.xsd line 61)
         * <p>
         * <pre>
         * &lt;complexType>
         *   &lt;simpleContent>
         *     &lt;extension base="&lt;http://apply.grants.gov/forms/RR_KeyPersonExpanded_1_2-V1-2>OtherProjectRoleStringDataType">
         *     &lt;/extension>
         *   &lt;/simpleContent>
         * &lt;/complexType>
         * </pre>
         * 
         */
        public interface OtherProjectRoleCategoryType {


            /**
             * Gets the value of the value property.
             * 
             * @return
             *     possible object is
             *     {@link java.lang.String}
             */
            java.lang.String getValue();

            /**
             * Sets the value of the value property.
             * 
             * @param value
             *     allowed object is
             *     {@link java.lang.String}
             */
            void setValue(java.lang.String value);

        }


        /**
         * Java content class for anonymous complex type.
         * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/RR_KeyPersonExpanded_1_2-V1.2.xsd line 91)
         * <p>
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="SupportAttached" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         */
        public interface SupportsAttachedType {


            /**
             * Gets the value of the supportAttached property.
             * 
             * @return
             *     possible object is
             *     {@link gov.grants.apply.system.attachments_v1.AttachedFileDataType}
             */
            gov.grants.apply.system.attachments_v1.AttachedFileDataType getSupportAttached();

            /**
             * Sets the value of the supportAttached property.
             * 
             * @param value
             *     allowed object is
             *     {@link gov.grants.apply.system.attachments_v1.AttachedFileDataType}
             */
            void setSupportAttached(gov.grants.apply.system.attachments_v1.AttachedFileDataType value);

        }

    }

}
