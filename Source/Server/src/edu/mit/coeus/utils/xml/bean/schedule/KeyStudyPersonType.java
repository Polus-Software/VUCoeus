//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.03.26 at 12:29:58 PM IST 
//


package edu.mit.coeus.utils.xml.bean.schedule;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/VSS/SVN/coeusSource/Branches/4.5/Printing/correspondenceTemplates/schema/irb.xsd line 158)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://irb.mit.edu/irbnamespace}Person"/>
 *         &lt;element name="Role" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="60"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="Affiliation">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="200"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface KeyStudyPersonType {


    /**
     * Gets the value of the affiliation property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAffiliation();

    /**
     * Sets the value of the affiliation property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAffiliation(java.lang.String value);

    /**
     * Gets the value of the role property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getRole();

    /**
     * Sets the value of the role property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setRole(java.lang.String value);

    /**
     * Gets the value of the person property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.schedule.PersonType}
     *     {@link edu.mit.coeus.utils.xml.bean.schedule.Person}
     */
    edu.mit.coeus.utils.xml.bean.schedule.PersonType getPerson();

    /**
     * Sets the value of the person property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.schedule.PersonType}
     *     {@link edu.mit.coeus.utils.xml.bean.schedule.Person}
     */
    void setPerson(edu.mit.coeus.utils.xml.bean.schedule.PersonType value);

}
