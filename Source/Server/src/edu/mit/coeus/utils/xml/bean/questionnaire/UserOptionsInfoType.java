//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.11.22 at 02:53:02 PM IST 
//


package edu.mit.coeus.utils.xml.bean.questionnaire;


/**
 * Java content class for UserOptionsInfoType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/E:/questionnaire%20printing/Questionnaire.xsd line 24)
 * <p>
 * <pre>
 * &lt;complexType name="UserOptionsInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PrintAnswers" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PrintAnsweredQuestionsOnly" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface UserOptionsInfoType {


    /**
     * Gets the value of the printAnsweredQuestionsOnly property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getPrintAnsweredQuestionsOnly();

    /**
     * Sets the value of the printAnsweredQuestionsOnly property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setPrintAnsweredQuestionsOnly(java.lang.String value);

    /**
     * Gets the value of the printAnswers property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getPrintAnswers();

    /**
     * Sets the value of the printAnswers property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setPrintAnswers(java.lang.String value);

}
