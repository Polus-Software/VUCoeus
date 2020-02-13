//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.rar;


/**
 * NSF extension
 * Java content class for ProposalPersonType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/jwsdp-1.4/jaxb/bin/rarschema.xsd line 635)
 * <p>
 * <pre>
 * &lt;complexType name="ProposalPersonType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;sequence>
 *           &lt;element name="Name" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace}PersonFullNameType"/>
 *           &lt;element name="SSN" type="{http://www.w3.org/2001/XMLSchema}token" minOccurs="0"/>
 *           &lt;element name="DOB" type="{http://www.w3.org/2001/XMLSchema}token" minOccurs="0"/>
 *           &lt;element name="Phone" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}PhoneNumberType" minOccurs="0"/>
 *           &lt;element name="Email" type="{http://www.w3.org/2001/XMLSchema}token" minOccurs="0"/>
 *           &lt;element name="Degree" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="ProjectRole" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace}ProjectRoleType"/>
 *           &lt;element name="PercentEffort" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *           &lt;element name="FundingMonths" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}MonthNumberType" minOccurs="0"/>
 *           &lt;element name="SummerFundingMonths" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}MonthNumberType" minOccurs="0"/>
 *           &lt;element name="AcademicFundingMonths" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}MonthNumberType" minOccurs="0"/>
 *           &lt;element name="RequestedCost" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}CurrencyType"/>
 *           &lt;element name="AccountIdentifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface ProposalPersonType {


    /**
     * Gets the value of the ssn property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getSSN();

    /**
     * Sets the value of the ssn property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setSSN(java.lang.String value);

    /**
     * Gets the value of the degree property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getDegree();

    /**
     * Sets the value of the degree property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setDegree(java.lang.String value);

    /**
     * Gets the value of the accountIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAccountIdentifier();

    /**
     * Sets the value of the accountIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAccountIdentifier(java.lang.String value);

    /**
     * Gets the value of the requestedCost property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getRequestedCost();

    /**
     * Sets the value of the requestedCost property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setRequestedCost(java.math.BigDecimal value);

    /**
     * Gets the value of the summerFundingMonths property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getSummerFundingMonths();

    /**
     * Sets the value of the summerFundingMonths property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setSummerFundingMonths(java.math.BigDecimal value);

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
     * this is calendar months
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getFundingMonths();

    /**
     * this is calendar months
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setFundingMonths(java.math.BigDecimal value);

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
     * Gets the value of the academicFundingMonths property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getAcademicFundingMonths();

    /**
     * Sets the value of the academicFundingMonths property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setAcademicFundingMonths(java.math.BigDecimal value);

    /**
     * Gets the value of the percentEffort property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getPercentEffort();

    /**
     * Sets the value of the percentEffort property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setPercentEffort(java.math.BigDecimal value);

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.PersonFullNameType}
     */
    edu.mit.coeus.utils.xml.bean.proposal.rar.PersonFullNameType getName();

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.PersonFullNameType}
     */
    void setName(edu.mit.coeus.utils.xml.bean.proposal.rar.PersonFullNameType value);

    /**
     * Gets the value of the dob property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getDOB();

    /**
     * Sets the value of the dob property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setDOB(java.lang.String value);

}
