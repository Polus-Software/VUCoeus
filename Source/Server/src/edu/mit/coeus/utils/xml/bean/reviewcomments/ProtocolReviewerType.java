//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.06.04 at 03:49:17 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.reviewcomments;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/Coeus%204.4/Printing/correspondenceTemplates/schema/irb.xsd line 915)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://irb.mit.edu/irbnamespace}Person"/>
 *         &lt;element name="ReviewerTypeDesc" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ReviewerTypeCode" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface ProtocolReviewerType {


    /**
     * Gets the value of the reviewerTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getReviewerTypeCode();

    /**
     * Sets the value of the reviewerTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setReviewerTypeCode(java.math.BigInteger value);

    /**
     * Gets the value of the reviewerTypeDesc property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getReviewerTypeDesc();

    /**
     * Sets the value of the reviewerTypeDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setReviewerTypeDesc(java.lang.String value);

    /**
     * Gets the value of the person property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.reviewcomments.PersonType}
     *     {@link edu.mit.coeus.utils.xml.bean.reviewcomments.Person}
     */
    edu.mit.coeus.utils.xml.bean.reviewcomments.PersonType getPerson();

    /**
     * Sets the value of the person property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.reviewcomments.PersonType}
     *     {@link edu.mit.coeus.utils.xml.bean.reviewcomments.Person}
     */
    void setPerson(edu.mit.coeus.utils.xml.bean.reviewcomments.PersonType value);

}