//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.06.04 at 03:49:17 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.reviewcomments;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/Coeus%204.4/Printing/correspondenceTemplates/schema/irb.xsd line 179)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PersonID">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="9"/>
 *             &lt;minLength value="6"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="LastName">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="30"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="Middlename" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="30"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="Salutation" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="30"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="Firstname" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="30"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="Fullname">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="90"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="Email" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="60"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="Degree" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="11"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="OfficeLocation" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="30"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="OfficePhone" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="20"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="School" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="50"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="YearGraduated" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="30"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="Department_Organization" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="80"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="Citizenship" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="30"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="PrimaryTitle" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="51"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="DirectoryTitle" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="50"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="HomeUnit" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="UnitNumber">
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;maxLength value="8"/>
 *                     &lt;/restriction>
 *                   &lt;/element>
 *                   &lt;element name="UnitName">
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;maxLength value="60"/>
 *                     &lt;/restriction>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="FacultyFlag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="EmployeeFlag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="AddressLine1" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="80"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="AddressLine2" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="80"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="AddressLine3" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="80"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="City" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="30"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="Country" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="30"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="State" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="30"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="PostalCode" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="15"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="CountryCode" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="3"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="FaxNumber" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="20"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="PagerNumber" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="20"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="MobilePhoneNumber" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="20"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface PersonType {


    /**
     * Gets the value of the homeUnit property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.reviewcomments.PersonType.HomeUnitType}
     */
    edu.mit.coeus.utils.xml.bean.reviewcomments.PersonType.HomeUnitType getHomeUnit();

    /**
     * Sets the value of the homeUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.reviewcomments.PersonType.HomeUnitType}
     */
    void setHomeUnit(edu.mit.coeus.utils.xml.bean.reviewcomments.PersonType.HomeUnitType value);

    /**
     * Gets the value of the pagerNumber property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getPagerNumber();

    /**
     * Sets the value of the pagerNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setPagerNumber(java.lang.String value);

    /**
     * Gets the value of the lastName property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getLastName();

    /**
     * Sets the value of the lastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setLastName(java.lang.String value);

    /**
     * Gets the value of the fullname property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getFullname();

    /**
     * Sets the value of the fullname property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setFullname(java.lang.String value);

    /**
     * Gets the value of the faxNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getFaxNumber();

    /**
     * Sets the value of the faxNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setFaxNumber(java.lang.String value);

    /**
     * Gets the value of the personID property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getPersonID();

    /**
     * Sets the value of the personID property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setPersonID(java.lang.String value);

    /**
     * Gets the value of the firstname property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getFirstname();

    /**
     * Sets the value of the firstname property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setFirstname(java.lang.String value);

    /**
     * Gets the value of the countryCode property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getCountryCode();

    /**
     * Sets the value of the countryCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setCountryCode(java.lang.String value);

    /**
     * Gets the value of the school property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getSchool();

    /**
     * Sets the value of the school property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setSchool(java.lang.String value);

    /**
     * Gets the value of the citizenship property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getCitizenship();

    /**
     * Sets the value of the citizenship property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setCitizenship(java.lang.String value);

    /**
     * Gets the value of the departmentOrganization property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getDepartmentOrganization();

    /**
     * Sets the value of the departmentOrganization property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setDepartmentOrganization(java.lang.String value);

    /**
     * Gets the value of the middlename property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getMiddlename();

    /**
     * Sets the value of the middlename property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setMiddlename(java.lang.String value);

    /**
     * Gets the value of the salutation property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getSalutation();

    /**
     * Sets the value of the salutation property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setSalutation(java.lang.String value);

    /**
     * Gets the value of the primaryTitle property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getPrimaryTitle();

    /**
     * Sets the value of the primaryTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setPrimaryTitle(java.lang.String value);

    /**
     * Gets the value of the employeeFlag property.
     * 
     */
    boolean isEmployeeFlag();

    /**
     * Sets the value of the employeeFlag property.
     * 
     */
    void setEmployeeFlag(boolean value);

    /**
     * Gets the value of the country property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getCountry();

    /**
     * Sets the value of the country property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setCountry(java.lang.String value);

    /**
     * Gets the value of the yearGraduated property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getYearGraduated();

    /**
     * Sets the value of the yearGraduated property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setYearGraduated(java.lang.String value);

    /**
     * Gets the value of the city property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getCity();

    /**
     * Sets the value of the city property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setCity(java.lang.String value);

    /**
     * Gets the value of the mobilePhoneNumber property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getMobilePhoneNumber();

    /**
     * Sets the value of the mobilePhoneNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setMobilePhoneNumber(java.lang.String value);

    /**
     * Gets the value of the directoryTitle property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getDirectoryTitle();

    /**
     * Sets the value of the directoryTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setDirectoryTitle(java.lang.String value);

    /**
     * Gets the value of the postalCode property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getPostalCode();

    /**
     * Sets the value of the postalCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setPostalCode(java.lang.String value);

    /**
     * Gets the value of the officePhone property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getOfficePhone();

    /**
     * Sets the value of the officePhone property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setOfficePhone(java.lang.String value);

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getEmail();

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setEmail(java.lang.String value);

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getState();

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setState(java.lang.String value);

    /**
     * Gets the value of the facultyFlag property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     boolean
     */
    java.lang.Boolean getFacultyFlag();

    /**
     * Sets the value of the facultyFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     boolean
     */
    void setFacultyFlag(java.lang.Boolean value);

    /**
     * Gets the value of the degree property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getDegree();

    /**
     * Sets the value of the degree property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setDegree(java.lang.String value);

    /**
     * Gets the value of the officeLocation property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getOfficeLocation();

    /**
     * Sets the value of the officeLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setOfficeLocation(java.lang.String value);

    /**
     * Gets the value of the addressLine1 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getAddressLine1();

    /**
     * Sets the value of the addressLine1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setAddressLine1(java.lang.String value);

    /**
     * Gets the value of the addressLine2 property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getAddressLine2();

    /**
     * Sets the value of the addressLine2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setAddressLine2(java.lang.String value);

    /**
     * Gets the value of the addressLine3 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getAddressLine3();

    /**
     * Sets the value of the addressLine3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setAddressLine3(java.lang.String value);


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/Coeus%204.4/Printing/correspondenceTemplates/schema/irb.xsd line 295)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="UnitNumber">
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;maxLength value="8"/>
     *           &lt;/restriction>
     *         &lt;/element>
     *         &lt;element name="UnitName">
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;maxLength value="60"/>
     *           &lt;/restriction>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface HomeUnitType {


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

    }

}
