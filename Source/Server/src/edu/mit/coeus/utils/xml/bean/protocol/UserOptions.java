//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.03.17 at 08:39:35 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.protocol;


/**
 * Java content class for UserOptions complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/E:/Share/3091/Questionnaire.xsd line 19)
 * <p>
 * <pre>
 * &lt;complexType name="UserOptions">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="UserOptionsInfo" type="{http://www.w3.org/2001/QuesionnaireSchema}UserOptionsInfoType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface UserOptions {


    /**
     * Gets the value of the userOptionsInfo property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.protocol.UserOptionsInfoType}
     */
    edu.mit.coeus.utils.xml.bean.protocol.UserOptionsInfoType getUserOptionsInfo();

    /**
     * Sets the value of the userOptionsInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.protocol.UserOptionsInfoType}
     */
    void setUserOptionsInfo(edu.mit.coeus.utils.xml.bean.protocol.UserOptionsInfoType value);

}
