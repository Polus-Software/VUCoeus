//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.04.27 at 09:21:38 AM IST 
//


package edu.mit.coeus.xml.instprop;


/**
 * Java content class for NoticeOfOppType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/VSS/SVN/coeusSource/Branches/4.5/Printing/schemas/instituteProposal.xsd line 186)
 * <p>
 * <pre>
 * &lt;complexType name="NoticeOfOppType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NoticeOfOppcode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NoticeOfOppDesc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface NoticeOfOppType {


    /**
     * Gets the value of the noticeOfOppcode property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getNoticeOfOppcode();

    /**
     * Sets the value of the noticeOfOppcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setNoticeOfOppcode(java.lang.String value);

    /**
     * Gets the value of the noticeOfOppDesc property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getNoticeOfOppDesc();

    /**
     * Sets the value of the noticeOfOppDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setNoticeOfOppDesc(java.lang.String value);

}
