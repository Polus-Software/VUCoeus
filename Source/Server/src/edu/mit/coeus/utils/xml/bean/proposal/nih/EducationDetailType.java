//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.nih;


/**
 * Java content class for EducationDetailType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/jwsdp-1.4/jaxb/bin/nihschema.xsd line 19)
 * <p>
 * <pre>
 * &lt;complexType name="EducationDetailType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DegreeTypeCode" type="{http://www.w3.org/2001/XMLSchema}token" minOccurs="0"/>
 *         &lt;element name="Years" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FieldOfStudyText" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="InstitutionName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="InstitutionLocationText" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface EducationDetailType {


    /**
     * Gets the value of the years property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getYears();

    /**
     * Sets the value of the years property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setYears(java.lang.String value);

    /**
     * Gets the value of the institutionName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getInstitutionName();

    /**
     * Sets the value of the institutionName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setInstitutionName(java.lang.String value);

    /**
     * Gets the value of the degreeTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getDegreeTypeCode();

    /**
     * Sets the value of the degreeTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setDegreeTypeCode(java.lang.String value);

    /**
     * Gets the value of the fieldOfStudyText property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getFieldOfStudyText();

    /**
     * Sets the value of the fieldOfStudyText property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setFieldOfStudyText(java.lang.String value);

    /**
     * Gets the value of the institutionLocationText property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getInstitutionLocationText();

    /**
     * Sets the value of the institutionLocationText property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setInstitutionLocationText(java.lang.String value);

}